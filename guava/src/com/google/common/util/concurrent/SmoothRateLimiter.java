begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.util.concurrent
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|min
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|GwtIncompatible
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|math
operator|.
name|LongMath
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_class
annotation|@
name|GwtIncompatible
DECL|class|SmoothRateLimiter
specifier|abstract
class|class
name|SmoothRateLimiter
extends|extends
name|RateLimiter
block|{
comment|/*    * How is the RateLimiter designed, and why?    *    * The primary feature of a RateLimiter is its "stable rate", the maximum rate that    * is should allow at normal conditions. This is enforced by "throttling" incoming    * requests as needed, i.e. compute, for an incoming request, the appropriate throttle time,    * and make the calling thread wait as much.    *    * The simplest way to maintain a rate of QPS is to keep the timestamp of the last    * granted request, and ensure that (1/QPS) seconds have elapsed since then. For example,    * for a rate of QPS=5 (5 tokens per second), if we ensure that a request isn't granted    * earlier than 200ms after the last one, then we achieve the intended rate.    * If a request comes and the last request was granted only 100ms ago, then we wait for    * another 100ms. At this rate, serving 15 fresh permits (i.e. for an acquire(15) request)    * naturally takes 3 seconds.    *    * It is important to realize that such a RateLimiter has a very superficial memory    * of the past: it only remembers the last request. What if the RateLimiter was unused for    * a long period of time, then a request arrived and was immediately granted?    * This RateLimiter would immediately forget about that past underutilization. This may    * result in either underutilization or overflow, depending on the real world consequences    * of not using the expected rate.    *    * Past underutilization could mean that excess resources are available. Then, the RateLimiter    * should speed up for a while, to take advantage of these resources. This is important    * when the rate is applied to networking (limiting bandwidth), where past underutilization    * typically translates to "almost empty buffers", which can be filled immediately.    *    * On the other hand, past underutilization could mean that "the server responsible for    * handling the request has become less ready for future requests", i.e. its caches become    * stale, and requests become more likely to trigger expensive operations (a more extreme    * case of this example is when a server has just booted, and it is mostly busy with getting    * itself up to speed).    *    * To deal with such scenarios, we add an extra dimension, that of "past underutilization",    * modeled by "storedPermits" variable. This variable is zero when there is no    * underutilization, and it can grow up to maxStoredPermits, for sufficiently large    * underutilization. So, the requested permits, by an invocation acquire(permits),    * are served from:    * - stored permits (if available)    * - fresh permits (for any remaining permits)    *    * How this works is best explained with an example:    *    * For a RateLimiter that produces 1 token per second, every second    * that goes by with the RateLimiter being unused, we increase storedPermits by 1.    * Say we leave the RateLimiter unused for 10 seconds (i.e., we expected a request at time    * X, but we are at time X + 10 seconds before a request actually arrives; this is    * also related to the point made in the last paragraph), thus storedPermits    * becomes 10.0 (assuming maxStoredPermits>= 10.0). At that point, a request of acquire(3)    * arrives. We serve this request out of storedPermits, and reduce that to 7.0 (how this is    * translated to throttling time is discussed later). Immediately after, assume that an    * acquire(10) request arriving. We serve the request partly from storedPermits,    * using all the remaining 7.0 permits, and the remaining 3.0, we serve them by fresh permits    * produced by the rate limiter.    *    * We already know how much time it takes to serve 3 fresh permits: if the rate is    * "1 token per second", then this will take 3 seconds. But what does it mean to serve 7    * stored permits? As explained above, there is no unique answer. If we are primarily    * interested to deal with underutilization, then we want stored permits to be given out    * /faster/ than fresh ones, because underutilization = free resources for the taking.    * If we are primarily interested to deal with overflow, then stored permits could    * be given out /slower/ than fresh ones. Thus, we require a (different in each case)    * function that translates storedPermits to throtting time.    *    * This role is played by storedPermitsToWaitTime(double storedPermits, double permitsToTake).    * The underlying model is a continuous function mapping storedPermits    * (from 0.0 to maxStoredPermits) onto the 1/rate (i.e. intervals) that is effective at the given    * storedPermits. "storedPermits" essentially measure unused time; we spend unused time    * buying/storing permits. Rate is "permits / time", thus "1 / rate = time / permits".    * Thus, "1/rate" (time / permits) times "permits" gives time, i.e., integrals on this    * function (which is what storedPermitsToWaitTime() computes) correspond to minimum intervals    * between subsequent requests, for the specified number of requested permits.    *    * Here is an example of storedPermitsToWaitTime:    * If storedPermits == 10.0, and we want 3 permits, we take them from storedPermits,    * reducing them to 7.0, and compute the throttling for these as a call to    * storedPermitsToWaitTime(storedPermits = 10.0, permitsToTake = 3.0), which will    * evaluate the integral of the function from 7.0 to 10.0.    *    * Using integrals guarantees that the effect of a single acquire(3) is equivalent    * to { acquire(1); acquire(1); acquire(1); }, or { acquire(2); acquire(1); }, etc,    * since the integral of the function in [7.0, 10.0] is equivalent to the sum of the    * integrals of [7.0, 8.0], [8.0, 9.0], [9.0, 10.0] (and so on), no matter    * what the function is. This guarantees that we handle correctly requests of varying weight    * (permits), /no matter/ what the actual function is - so we can tweak the latter freely.    * (The only requirement, obviously, is that we can compute its integrals).    *    * Note well that if, for this function, we chose a horizontal line, at height of exactly    * (1/QPS), then the effect of the function is non-existent: we serve storedPermits at    * exactly the same cost as fresh ones (1/QPS is the cost for each). We use this trick later.    *    * If we pick a function that goes /below/ that horizontal line, it means that we reduce    * the area of the function, thus time. Thus, the RateLimiter becomes /faster/ after a    * period of underutilization. If, on the other hand, we pick a function that    * goes /above/ that horizontal line, then it means that the area (time) is increased,    * thus storedPermits are more costly than fresh permits, thus the RateLimiter becomes    * /slower/ after a period of underutilization.    *    * Last, but not least: consider a RateLimiter with rate of 1 permit per second, currently    * completely unused, and an expensive acquire(100) request comes. It would be nonsensical    * to just wait for 100 seconds, and /then/ start the actual task. Why wait without doing    * anything? A much better approach is to /allow/ the request right away (as if it was an    * acquire(1) request instead), and postpone /subsequent/ requests as needed. In this version,    * we allow starting the task immediately, and postpone by 100 seconds future requests,    * thus we allow for work to get done in the meantime instead of waiting idly.    *    * This has important consequences: it means that the RateLimiter doesn't remember the time    * of the _last_ request, but it remembers the (expected) time of the _next_ request. This    * also enables us to tell immediately (see tryAcquire(timeout)) whether a particular    * timeout is enough to get us to the point of the next scheduling time, since we always    * maintain that. And what we mean by "an unused RateLimiter" is also defined by that    * notion: when we observe that the "expected arrival time of the next request" is actually    * in the past, then the difference (now - past) is the amount of time that the RateLimiter    * was formally unused, and it is that amount of time which we translate to storedPermits.    * (We increase storedPermits with the amount of permits that would have been produced    * in that idle time). So, if rate == 1 permit per second, and arrivals come exactly    * one second after the previous, then storedPermits is _never_ increased -- we would only    * increase it for arrivals _later_ than the expected one second.    */
comment|/**    * This implements the following function where coldInterval = coldFactor * stableInterval.    *    *          ^ throttling    *          |    *    cold  +                  /    * interval |                 /.    *          |                / .    *          |               /  .<-- "warmup period" is the area of the trapezoid between    *          |              /   .       thresholdPermits and maxPermits    *          |             /    .    *          |            /     .    *          |           /      .    *   stable +----------/  WARM .    * interval |          .   UP  .    *          |          . PERIOD.    *          |          .       .    *        0 +----------+-------+--------------> storedPermits    *          0 thresholdPermits maxPermits    * Before going into the details of this particular function, let's keep in mind the basics:    * 1) The state of the RateLimiter (storedPermits) is a vertical line in this figure.    * 2) When the RateLimiter is not used, this goes right (up to maxPermits)    * 3) When the RateLimiter is used, this goes left (down to zero), since if we have storedPermits,    *    we serve from those first    * 4) When _unused_, we go right at a constant rate! The rate at which we move to    *    the right is chosen as maxPermits / warmupPeriod.  This ensures that the time it takes to    *    go from 0 to maxPermits is equal to warmupPeriod.    * 5) When _used_, the time it takes, as explained in the introductory class note, is    *    equal to the integral of our function, between X permits and X-K permits, assuming    *    we want to spend K saved permits.    *    *    In summary, the time it takes to move to the left (spend K permits), is equal to the    *    area of the function of width == K.    *    *    Assuming we have saturated demand, the time to go from maxPermits to thresholdPermits is    *    equal to warmupPeriod.  And the time to go from thresholdPermits to 0 is    *    warmupPeriod/2.  (The reason that this is warmupPeriod/2 is to maintain the behavior of    *    the original implementation where coldFactor was hard coded as 3.)    *    *  It remains to calculate thresholdsPermits and maxPermits.    *    *  - The time to go from thresholdPermits to 0 is equal to the integral of the function between    *    0 and thresholdPermits.  This is thresholdPermits * stableIntervals.  By (5) it is also    *    equal to warmupPeriod/2.  Therefore    *    *        thresholdPermits = 0.5 * warmupPeriod / stableInterval.    *    *  - The time to go from maxPermits to thresholdPermits is equal to the integral of the function    *    between thresholdPermits and maxPermits.  This is the area of the pictured trapezoid, and it    *    is equal to 0.5 * (stableInterval + coldInterval) * (maxPermits - thresholdPermits).  It is    *    also equal to warmupPeriod, so    *    *        maxPermits = thresholdPermits + 2 * warmupPeriod / (stableInterval + coldInterval).    */
DECL|class|SmoothWarmingUp
specifier|static
specifier|final
class|class
name|SmoothWarmingUp
extends|extends
name|SmoothRateLimiter
block|{
DECL|field|warmupPeriodMicros
specifier|private
specifier|final
name|long
name|warmupPeriodMicros
decl_stmt|;
comment|/**      * The slope of the line from the stable interval (when permits == 0), to the cold interval      * (when permits == maxPermits)      */
DECL|field|slope
specifier|private
name|double
name|slope
decl_stmt|;
DECL|field|thresholdPermits
specifier|private
name|double
name|thresholdPermits
decl_stmt|;
DECL|field|coldFactor
specifier|private
name|double
name|coldFactor
decl_stmt|;
DECL|method|SmoothWarmingUp ( SleepingStopwatch stopwatch, long warmupPeriod, TimeUnit timeUnit, double coldFactor)
name|SmoothWarmingUp
parameter_list|(
name|SleepingStopwatch
name|stopwatch
parameter_list|,
name|long
name|warmupPeriod
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|,
name|double
name|coldFactor
parameter_list|)
block|{
name|super
argument_list|(
name|stopwatch
argument_list|)
expr_stmt|;
name|this
operator|.
name|warmupPeriodMicros
operator|=
name|timeUnit
operator|.
name|toMicros
argument_list|(
name|warmupPeriod
argument_list|)
expr_stmt|;
name|this
operator|.
name|coldFactor
operator|=
name|coldFactor
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doSetRate (double permitsPerSecond, double stableIntervalMicros)
name|void
name|doSetRate
parameter_list|(
name|double
name|permitsPerSecond
parameter_list|,
name|double
name|stableIntervalMicros
parameter_list|)
block|{
name|double
name|oldMaxPermits
init|=
name|maxPermits
decl_stmt|;
name|double
name|coldIntervalMicros
init|=
name|stableIntervalMicros
operator|*
name|coldFactor
decl_stmt|;
name|thresholdPermits
operator|=
literal|0.5
operator|*
name|warmupPeriodMicros
operator|/
name|stableIntervalMicros
expr_stmt|;
name|maxPermits
operator|=
name|thresholdPermits
operator|+
literal|2.0
operator|*
name|warmupPeriodMicros
operator|/
operator|(
name|stableIntervalMicros
operator|+
name|coldIntervalMicros
operator|)
expr_stmt|;
name|slope
operator|=
operator|(
name|coldIntervalMicros
operator|-
name|stableIntervalMicros
operator|)
operator|/
operator|(
name|maxPermits
operator|-
name|thresholdPermits
operator|)
expr_stmt|;
if|if
condition|(
name|oldMaxPermits
operator|==
name|Double
operator|.
name|POSITIVE_INFINITY
condition|)
block|{
comment|// if we don't special-case this, we would get storedPermits == NaN, below
name|storedPermits
operator|=
literal|0.0
expr_stmt|;
block|}
else|else
block|{
name|storedPermits
operator|=
operator|(
name|oldMaxPermits
operator|==
literal|0.0
operator|)
condition|?
name|maxPermits
comment|// initial state is cold
else|:
name|storedPermits
operator|*
name|maxPermits
operator|/
name|oldMaxPermits
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|storedPermitsToWaitTime (double storedPermits, double permitsToTake)
name|long
name|storedPermitsToWaitTime
parameter_list|(
name|double
name|storedPermits
parameter_list|,
name|double
name|permitsToTake
parameter_list|)
block|{
name|double
name|availablePermitsAboveThreshold
init|=
name|storedPermits
operator|-
name|thresholdPermits
decl_stmt|;
name|long
name|micros
init|=
literal|0
decl_stmt|;
comment|// measuring the integral on the right part of the function (the climbing line)
if|if
condition|(
name|availablePermitsAboveThreshold
operator|>
literal|0.0
condition|)
block|{
name|double
name|permitsAboveThresholdToTake
init|=
name|min
argument_list|(
name|availablePermitsAboveThreshold
argument_list|,
name|permitsToTake
argument_list|)
decl_stmt|;
name|micros
operator|=
call|(
name|long
call|)
argument_list|(
name|permitsAboveThresholdToTake
operator|*
operator|(
name|permitsToTime
argument_list|(
name|availablePermitsAboveThreshold
argument_list|)
operator|+
name|permitsToTime
argument_list|(
name|availablePermitsAboveThreshold
operator|-
name|permitsAboveThresholdToTake
argument_list|)
operator|)
operator|/
literal|2.0
argument_list|)
expr_stmt|;
name|permitsToTake
operator|-=
name|permitsAboveThresholdToTake
expr_stmt|;
block|}
comment|// measuring the integral on the left part of the function (the horizontal line)
name|micros
operator|+=
operator|(
name|stableIntervalMicros
operator|*
name|permitsToTake
operator|)
expr_stmt|;
return|return
name|micros
return|;
block|}
DECL|method|permitsToTime (double permits)
specifier|private
name|double
name|permitsToTime
parameter_list|(
name|double
name|permits
parameter_list|)
block|{
return|return
name|stableIntervalMicros
operator|+
name|permits
operator|*
name|slope
return|;
block|}
annotation|@
name|Override
DECL|method|coolDownIntervalMicros ()
name|double
name|coolDownIntervalMicros
parameter_list|()
block|{
return|return
name|warmupPeriodMicros
operator|/
name|maxPermits
return|;
block|}
block|}
comment|/**    * This implements a "bursty" RateLimiter, where storedPermits are translated to    * zero throttling. The maximum number of permits that can be saved (when the RateLimiter is    * unused) is defined in terms of time, in this sense: if a RateLimiter is 2qps, and this    * time is specified as 10 seconds, we can save up to 2 * 10 = 20 permits.    */
DECL|class|SmoothBursty
specifier|static
specifier|final
class|class
name|SmoothBursty
extends|extends
name|SmoothRateLimiter
block|{
comment|/** The work (permits) of how many seconds can be saved up if this RateLimiter is unused? */
DECL|field|maxBurstSeconds
specifier|final
name|double
name|maxBurstSeconds
decl_stmt|;
DECL|method|SmoothBursty (SleepingStopwatch stopwatch, double maxBurstSeconds)
name|SmoothBursty
parameter_list|(
name|SleepingStopwatch
name|stopwatch
parameter_list|,
name|double
name|maxBurstSeconds
parameter_list|)
block|{
name|super
argument_list|(
name|stopwatch
argument_list|)
expr_stmt|;
name|this
operator|.
name|maxBurstSeconds
operator|=
name|maxBurstSeconds
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doSetRate (double permitsPerSecond, double stableIntervalMicros)
name|void
name|doSetRate
parameter_list|(
name|double
name|permitsPerSecond
parameter_list|,
name|double
name|stableIntervalMicros
parameter_list|)
block|{
name|double
name|oldMaxPermits
init|=
name|this
operator|.
name|maxPermits
decl_stmt|;
name|maxPermits
operator|=
name|maxBurstSeconds
operator|*
name|permitsPerSecond
expr_stmt|;
if|if
condition|(
name|oldMaxPermits
operator|==
name|Double
operator|.
name|POSITIVE_INFINITY
condition|)
block|{
comment|// if we don't special-case this, we would get storedPermits == NaN, below
name|storedPermits
operator|=
name|maxPermits
expr_stmt|;
block|}
else|else
block|{
name|storedPermits
operator|=
operator|(
name|oldMaxPermits
operator|==
literal|0.0
operator|)
condition|?
literal|0.0
comment|// initial state
else|:
name|storedPermits
operator|*
name|maxPermits
operator|/
name|oldMaxPermits
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|storedPermitsToWaitTime (double storedPermits, double permitsToTake)
name|long
name|storedPermitsToWaitTime
parameter_list|(
name|double
name|storedPermits
parameter_list|,
name|double
name|permitsToTake
parameter_list|)
block|{
return|return
literal|0L
return|;
block|}
annotation|@
name|Override
DECL|method|coolDownIntervalMicros ()
name|double
name|coolDownIntervalMicros
parameter_list|()
block|{
return|return
name|stableIntervalMicros
return|;
block|}
block|}
comment|/**    * The currently stored permits.    */
DECL|field|storedPermits
name|double
name|storedPermits
decl_stmt|;
comment|/**    * The maximum number of stored permits.    */
DECL|field|maxPermits
name|double
name|maxPermits
decl_stmt|;
comment|/**    * The interval between two unit requests, at our stable rate. E.g., a stable rate of 5 permits    * per second has a stable interval of 200ms.    */
DECL|field|stableIntervalMicros
name|double
name|stableIntervalMicros
decl_stmt|;
comment|/**    * The time when the next request (no matter its size) will be granted. After granting a    * request, this is pushed further in the future. Large requests push this further than small    * requests.    */
DECL|field|nextFreeTicketMicros
specifier|private
name|long
name|nextFreeTicketMicros
init|=
literal|0L
decl_stmt|;
comment|// could be either in the past or future
DECL|method|SmoothRateLimiter (SleepingStopwatch stopwatch)
specifier|private
name|SmoothRateLimiter
parameter_list|(
name|SleepingStopwatch
name|stopwatch
parameter_list|)
block|{
name|super
argument_list|(
name|stopwatch
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doSetRate (double permitsPerSecond, long nowMicros)
specifier|final
name|void
name|doSetRate
parameter_list|(
name|double
name|permitsPerSecond
parameter_list|,
name|long
name|nowMicros
parameter_list|)
block|{
name|resync
argument_list|(
name|nowMicros
argument_list|)
expr_stmt|;
name|double
name|stableIntervalMicros
init|=
name|SECONDS
operator|.
name|toMicros
argument_list|(
literal|1L
argument_list|)
operator|/
name|permitsPerSecond
decl_stmt|;
name|this
operator|.
name|stableIntervalMicros
operator|=
name|stableIntervalMicros
expr_stmt|;
name|doSetRate
argument_list|(
name|permitsPerSecond
argument_list|,
name|stableIntervalMicros
argument_list|)
expr_stmt|;
block|}
DECL|method|doSetRate (double permitsPerSecond, double stableIntervalMicros)
specifier|abstract
name|void
name|doSetRate
parameter_list|(
name|double
name|permitsPerSecond
parameter_list|,
name|double
name|stableIntervalMicros
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|doGetRate ()
specifier|final
name|double
name|doGetRate
parameter_list|()
block|{
return|return
name|SECONDS
operator|.
name|toMicros
argument_list|(
literal|1L
argument_list|)
operator|/
name|stableIntervalMicros
return|;
block|}
annotation|@
name|Override
DECL|method|queryEarliestAvailable (long nowMicros)
specifier|final
name|long
name|queryEarliestAvailable
parameter_list|(
name|long
name|nowMicros
parameter_list|)
block|{
return|return
name|nextFreeTicketMicros
return|;
block|}
annotation|@
name|Override
DECL|method|reserveEarliestAvailable (int requiredPermits, long nowMicros)
specifier|final
name|long
name|reserveEarliestAvailable
parameter_list|(
name|int
name|requiredPermits
parameter_list|,
name|long
name|nowMicros
parameter_list|)
block|{
name|resync
argument_list|(
name|nowMicros
argument_list|)
expr_stmt|;
name|long
name|returnValue
init|=
name|nextFreeTicketMicros
decl_stmt|;
name|double
name|storedPermitsToSpend
init|=
name|min
argument_list|(
name|requiredPermits
argument_list|,
name|this
operator|.
name|storedPermits
argument_list|)
decl_stmt|;
name|double
name|freshPermits
init|=
name|requiredPermits
operator|-
name|storedPermitsToSpend
decl_stmt|;
name|long
name|waitMicros
init|=
name|storedPermitsToWaitTime
argument_list|(
name|this
operator|.
name|storedPermits
argument_list|,
name|storedPermitsToSpend
argument_list|)
operator|+
call|(
name|long
call|)
argument_list|(
name|freshPermits
operator|*
name|stableIntervalMicros
argument_list|)
decl_stmt|;
name|this
operator|.
name|nextFreeTicketMicros
operator|=
name|LongMath
operator|.
name|saturatedAdd
argument_list|(
name|nextFreeTicketMicros
argument_list|,
name|waitMicros
argument_list|)
expr_stmt|;
name|this
operator|.
name|storedPermits
operator|-=
name|storedPermitsToSpend
expr_stmt|;
return|return
name|returnValue
return|;
block|}
comment|/**    * Translates a specified portion of our currently stored permits which we want to    * spend/acquire, into a throttling time. Conceptually, this evaluates the integral    * of the underlying function we use, for the range of    * [(storedPermits - permitsToTake), storedPermits].    *    *<p>This always holds: {@code 0<= permitsToTake<= storedPermits}    */
DECL|method|storedPermitsToWaitTime (double storedPermits, double permitsToTake)
specifier|abstract
name|long
name|storedPermitsToWaitTime
parameter_list|(
name|double
name|storedPermits
parameter_list|,
name|double
name|permitsToTake
parameter_list|)
function_decl|;
comment|/**    * Returns the number of microseconds during cool down that we have to wait to get a new permit.    */
DECL|method|coolDownIntervalMicros ()
specifier|abstract
name|double
name|coolDownIntervalMicros
parameter_list|()
function_decl|;
comment|/**    * Updates {@code storedPermits} and {@code nextFreeTicketMicros} based on the current time.    */
DECL|method|resync (long nowMicros)
name|void
name|resync
parameter_list|(
name|long
name|nowMicros
parameter_list|)
block|{
comment|// if nextFreeTicket is in the past, resync to now
if|if
condition|(
name|nowMicros
operator|>
name|nextFreeTicketMicros
condition|)
block|{
name|storedPermits
operator|=
name|min
argument_list|(
name|maxPermits
argument_list|,
name|storedPermits
operator|+
operator|(
name|nowMicros
operator|-
name|nextFreeTicketMicros
operator|)
operator|/
name|coolDownIntervalMicros
argument_list|()
argument_list|)
expr_stmt|;
name|nextFreeTicketMicros
operator|=
name|nowMicros
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

