begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|max
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
name|MICROSECONDS
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
name|Beta
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
name|annotations
operator|.
name|VisibleForTesting
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
name|base
operator|.
name|Stopwatch
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
name|util
operator|.
name|concurrent
operator|.
name|SmoothRateLimiter
operator|.
name|SmoothBursty
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
name|util
operator|.
name|concurrent
operator|.
name|SmoothRateLimiter
operator|.
name|SmoothWarmingUp
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_comment
comment|/**  * A rate limiter. Conceptually, a rate limiter distributes permits at a configurable rate. Each  * {@link #acquire()} blocks if necessary until a permit is available, and then takes it. Once  * acquired, permits need not be released.  *  *<p>{@code RateLimiter} is safe for concurrent use: It will restrict the total rate of calls from  * all threads. Note, however, that it does not guarantee fairness.  *  *<p>Rate limiters are often used to restrict the rate at which some physical or logical resource  * is accessed. This is in contrast to {@link java.util.concurrent.Semaphore} which restricts the  * number of concurrent accesses instead of the rate (note though that concurrency and rate are  * closely related, e.g. see<a href="http://en.wikipedia.org/wiki/Little%27s_law">Little's  * Law</a>).  *  *<p>A {@code RateLimiter} is defined primarily by the rate at which permits are issued. Absent  * additional configuration, permits will be distributed at a fixed rate, defined in terms of  * permits per second. Permits will be distributed smoothly, with the delay between individual  * permits being adjusted to ensure that the configured rate is maintained.  *  *<p>It is possible to configure a {@code RateLimiter} to have a warmup period during which time  * the permits issued each second steadily increases until it hits the stable rate.  *  *<p>As an example, imagine that we have a list of tasks to execute, but we don't want to submit  * more than 2 per second:  *  *<pre>{@code  * final RateLimiter rateLimiter = RateLimiter.create(2.0); // rate is "2 permits per second"  * void submitTasks(List<Runnable> tasks, Executor executor) {  *   for (Runnable task : tasks) {  *     rateLimiter.acquire(); // may wait  *     executor.execute(task);  *   }  * }  * }</pre>  *  *<p>As another example, imagine that we produce a stream of data, and we want to cap it at 5kb per  * second. This could be accomplished by requiring a permit per byte, and specifying a rate of 5000  * permits per second:  *  *<pre>{@code  * final RateLimiter rateLimiter = RateLimiter.create(5000.0); // rate = 5000 permits per second  * void submitPacket(byte[] packet) {  *   rateLimiter.acquire(packet.length);  *   networkService.send(packet);  * }  * }</pre>  *  *<p>It is important to note that the number of permits requested<i>never</i> affects the  * throttling of the request itself (an invocation to {@code acquire(1)} and an invocation to {@code  * acquire(1000)} will result in exactly the same throttling, if any), but it affects the throttling  * of the<i>next</i> request. I.e., if an expensive task arrives at an idle RateLimiter, it will be  * granted immediately, but it is the<i>next</i> request that will experience extra throttling,  * thus paying for the cost of the expensive task.  *  * @author Dimitris Andreou  * @since 13.0  */
end_comment

begin_comment
comment|// TODO(user): switch to nano precision. A natural unit of cost is "bytes", and a micro precision
end_comment

begin_comment
comment|// would mean a maximum rate of "1MB/s", which might be small in some cases.
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
DECL|class|RateLimiter
specifier|public
specifier|abstract
class|class
name|RateLimiter
block|{
comment|/**    * Creates a {@code RateLimiter} with the specified stable throughput, given as "permits per    * second" (commonly referred to as<i>QPS</i>, queries per second).    *    *<p>The returned {@code RateLimiter} ensures that on average no more than {@code    * permitsPerSecond} are issued during any given second, with sustained requests being smoothly    * spread over each second. When the incoming request rate exceeds {@code permitsPerSecond} the    * rate limiter will release one permit every {@code (1.0 / permitsPerSecond)} seconds. When the    * rate limiter is unused, bursts of up to {@code permitsPerSecond} permits will be allowed, with    * subsequent requests being smoothly limited at the stable rate of {@code permitsPerSecond}.    *    * @param permitsPerSecond the rate of the returned {@code RateLimiter}, measured in how many    *     permits become available per second    * @throws IllegalArgumentException if {@code permitsPerSecond} is negative or zero    */
comment|// TODO(user): "This is equivalent to
comment|// {@code createWithCapacity(permitsPerSecond, 1, TimeUnit.SECONDS)}".
DECL|method|create (double permitsPerSecond)
specifier|public
specifier|static
name|RateLimiter
name|create
parameter_list|(
name|double
name|permitsPerSecond
parameter_list|)
block|{
comment|/*      * The default RateLimiter configuration can save the unused permits of up to one second. This      * is to avoid unnecessary stalls in situations like this: A RateLimiter of 1qps, and 4 threads,      * all calling acquire() at these moments:      *      * T0 at 0 seconds      * T1 at 1.05 seconds      * T2 at 2 seconds      * T3 at 3 seconds      *      * Due to the slight delay of T1, T2 would have to sleep till 2.05 seconds, and T3 would also      * have to sleep till 3.05 seconds.      */
return|return
name|create
argument_list|(
name|permitsPerSecond
argument_list|,
name|SleepingStopwatch
operator|.
name|createFromSystemTimer
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|create (double permitsPerSecond, SleepingStopwatch stopwatch)
specifier|static
name|RateLimiter
name|create
parameter_list|(
name|double
name|permitsPerSecond
parameter_list|,
name|SleepingStopwatch
name|stopwatch
parameter_list|)
block|{
name|RateLimiter
name|rateLimiter
init|=
operator|new
name|SmoothBursty
argument_list|(
name|stopwatch
argument_list|,
literal|1.0
comment|/* maxBurstSeconds */
argument_list|)
decl_stmt|;
name|rateLimiter
operator|.
name|setRate
argument_list|(
name|permitsPerSecond
argument_list|)
expr_stmt|;
return|return
name|rateLimiter
return|;
block|}
comment|/**    * Creates a {@code RateLimiter} with the specified stable throughput, given as "permits per    * second" (commonly referred to as<i>QPS</i>, queries per second), and a<i>warmup period</i>,    * during which the {@code RateLimiter} smoothly ramps up its rate, until it reaches its maximum    * rate at the end of the period (as long as there are enough requests to saturate it). Similarly,    * if the {@code RateLimiter} is left<i>unused</i> for a duration of {@code warmupPeriod}, it    * will gradually return to its "cold" state, i.e. it will go through the same warming up process    * as when it was first created.    *    *<p>The returned {@code RateLimiter} is intended for cases where the resource that actually    * fulfills the requests (e.g., a remote server) needs "warmup" time, rather than being    * immediately accessed at the stable (maximum) rate.    *    *<p>The returned {@code RateLimiter} starts in a "cold" state (i.e. the warmup period will    * follow), and if it is left unused for long enough, it will return to that state.    *    * @param permitsPerSecond the rate of the returned {@code RateLimiter}, measured in how many    *     permits become available per second    * @param warmupPeriod the duration of the period where the {@code RateLimiter} ramps up its rate,    *     before reaching its stable (maximum) rate    * @param unit the time unit of the warmupPeriod argument    * @throws IllegalArgumentException if {@code permitsPerSecond} is negative or zero or {@code    *     warmupPeriod} is negative    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should accept a java.time.Duration
DECL|method|create (double permitsPerSecond, long warmupPeriod, TimeUnit unit)
specifier|public
specifier|static
name|RateLimiter
name|create
parameter_list|(
name|double
name|permitsPerSecond
parameter_list|,
name|long
name|warmupPeriod
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|warmupPeriod
operator|>=
literal|0
argument_list|,
literal|"warmupPeriod must not be negative: %s"
argument_list|,
name|warmupPeriod
argument_list|)
expr_stmt|;
return|return
name|create
argument_list|(
name|permitsPerSecond
argument_list|,
name|warmupPeriod
argument_list|,
name|unit
argument_list|,
literal|3.0
argument_list|,
name|SleepingStopwatch
operator|.
name|createFromSystemTimer
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|create ( double permitsPerSecond, long warmupPeriod, TimeUnit unit, double coldFactor, SleepingStopwatch stopwatch)
specifier|static
name|RateLimiter
name|create
parameter_list|(
name|double
name|permitsPerSecond
parameter_list|,
name|long
name|warmupPeriod
parameter_list|,
name|TimeUnit
name|unit
parameter_list|,
name|double
name|coldFactor
parameter_list|,
name|SleepingStopwatch
name|stopwatch
parameter_list|)
block|{
name|RateLimiter
name|rateLimiter
init|=
operator|new
name|SmoothWarmingUp
argument_list|(
name|stopwatch
argument_list|,
name|warmupPeriod
argument_list|,
name|unit
argument_list|,
name|coldFactor
argument_list|)
decl_stmt|;
name|rateLimiter
operator|.
name|setRate
argument_list|(
name|permitsPerSecond
argument_list|)
expr_stmt|;
return|return
name|rateLimiter
return|;
block|}
comment|/**    * The underlying timer; used both to measure elapsed time and sleep as necessary. A separate    * object to facilitate testing.    */
DECL|field|stopwatch
specifier|private
specifier|final
name|SleepingStopwatch
name|stopwatch
decl_stmt|;
comment|// Can't be initialized in the constructor because mocks don't call the constructor.
DECL|field|mutexDoNotUseDirectly
annotation|@
name|NullableDecl
specifier|private
specifier|volatile
name|Object
name|mutexDoNotUseDirectly
decl_stmt|;
DECL|method|mutex ()
specifier|private
name|Object
name|mutex
parameter_list|()
block|{
name|Object
name|mutex
init|=
name|mutexDoNotUseDirectly
decl_stmt|;
if|if
condition|(
name|mutex
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|mutex
operator|=
name|mutexDoNotUseDirectly
expr_stmt|;
if|if
condition|(
name|mutex
operator|==
literal|null
condition|)
block|{
name|mutexDoNotUseDirectly
operator|=
name|mutex
operator|=
operator|new
name|Object
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|mutex
return|;
block|}
DECL|method|RateLimiter (SleepingStopwatch stopwatch)
name|RateLimiter
parameter_list|(
name|SleepingStopwatch
name|stopwatch
parameter_list|)
block|{
name|this
operator|.
name|stopwatch
operator|=
name|checkNotNull
argument_list|(
name|stopwatch
argument_list|)
expr_stmt|;
block|}
comment|/**    * Updates the stable rate of this {@code RateLimiter}, that is, the {@code permitsPerSecond}    * argument provided in the factory method that constructed the {@code RateLimiter}. Currently    * throttled threads will<b>not</b> be awakened as a result of this invocation, thus they do not    * observe the new rate; only subsequent requests will.    *    *<p>Note though that, since each request repays (by waiting, if necessary) the cost of the    *<i>previous</i> request, this means that the very next request after an invocation to {@code    * setRate} will not be affected by the new rate; it will pay the cost of the previous request,    * which is in terms of the previous rate.    *    *<p>The behavior of the {@code RateLimiter} is not modified in any other way, e.g. if the {@code    * RateLimiter} was configured with a warmup period of 20 seconds, it still has a warmup period of    * 20 seconds after this method invocation.    *    * @param permitsPerSecond the new stable rate of this {@code RateLimiter}    * @throws IllegalArgumentException if {@code permitsPerSecond} is negative or zero    */
DECL|method|setRate (double permitsPerSecond)
specifier|public
specifier|final
name|void
name|setRate
parameter_list|(
name|double
name|permitsPerSecond
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|permitsPerSecond
operator|>
literal|0.0
operator|&&
operator|!
name|Double
operator|.
name|isNaN
argument_list|(
name|permitsPerSecond
argument_list|)
argument_list|,
literal|"rate must be positive"
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|mutex
argument_list|()
init|)
block|{
name|doSetRate
argument_list|(
name|permitsPerSecond
argument_list|,
name|stopwatch
operator|.
name|readMicros
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doSetRate (double permitsPerSecond, long nowMicros)
specifier|abstract
name|void
name|doSetRate
parameter_list|(
name|double
name|permitsPerSecond
parameter_list|,
name|long
name|nowMicros
parameter_list|)
function_decl|;
comment|/**    * Returns the stable rate (as {@code permits per seconds}) with which this {@code RateLimiter} is    * configured with. The initial value of this is the same as the {@code permitsPerSecond} argument    * passed in the factory method that produced this {@code RateLimiter}, and it is only updated    * after invocations to {@linkplain #setRate}.    */
DECL|method|getRate ()
specifier|public
specifier|final
name|double
name|getRate
parameter_list|()
block|{
synchronized|synchronized
init|(
name|mutex
argument_list|()
init|)
block|{
return|return
name|doGetRate
argument_list|()
return|;
block|}
block|}
DECL|method|doGetRate ()
specifier|abstract
name|double
name|doGetRate
parameter_list|()
function_decl|;
comment|/**    * Acquires a single permit from this {@code RateLimiter}, blocking until the request can be    * granted. Tells the amount of time slept, if any.    *    *<p>This method is equivalent to {@code acquire(1)}.    *    * @return time spent sleeping to enforce rate, in seconds; 0.0 if not rate-limited    * @since 16.0 (present in 13.0 with {@code void} return type})    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|acquire ()
specifier|public
name|double
name|acquire
parameter_list|()
block|{
return|return
name|acquire
argument_list|(
literal|1
argument_list|)
return|;
block|}
comment|/**    * Acquires the given number of permits from this {@code RateLimiter}, blocking until the request    * can be granted. Tells the amount of time slept, if any.    *    * @param permits the number of permits to acquire    * @return time spent sleeping to enforce rate, in seconds; 0.0 if not rate-limited    * @throws IllegalArgumentException if the requested number of permits is negative or zero    * @since 16.0 (present in 13.0 with {@code void} return type})    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|acquire (int permits)
specifier|public
name|double
name|acquire
parameter_list|(
name|int
name|permits
parameter_list|)
block|{
name|long
name|microsToWait
init|=
name|reserve
argument_list|(
name|permits
argument_list|)
decl_stmt|;
name|stopwatch
operator|.
name|sleepMicrosUninterruptibly
argument_list|(
name|microsToWait
argument_list|)
expr_stmt|;
return|return
literal|1.0
operator|*
name|microsToWait
operator|/
name|SECONDS
operator|.
name|toMicros
argument_list|(
literal|1L
argument_list|)
return|;
block|}
comment|/**    * Reserves the given number of permits from this {@code RateLimiter} for future use, returning    * the number of microseconds until the reservation can be consumed.    *    * @return time in microseconds to wait until the resource can be acquired, never negative    */
DECL|method|reserve (int permits)
specifier|final
name|long
name|reserve
parameter_list|(
name|int
name|permits
parameter_list|)
block|{
name|checkPermits
argument_list|(
name|permits
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|mutex
argument_list|()
init|)
block|{
return|return
name|reserveAndGetWaitLength
argument_list|(
name|permits
argument_list|,
name|stopwatch
operator|.
name|readMicros
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**    * Acquires a permit from this {@code RateLimiter} if it can be obtained without exceeding the    * specified {@code timeout}, or returns {@code false} immediately (without waiting) if the permit    * would not have been granted before the timeout expired.    *    *<p>This method is equivalent to {@code tryAcquire(1, timeout, unit)}.    *    * @param timeout the maximum time to wait for the permit. Negative values are treated as zero.    * @param unit the time unit of the timeout argument    * @return {@code true} if the permit was acquired, {@code false} otherwise    * @throws IllegalArgumentException if the requested number of permits is negative or zero    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should accept a java.time.Duration
DECL|method|tryAcquire (long timeout, TimeUnit unit)
specifier|public
name|boolean
name|tryAcquire
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|tryAcquire
argument_list|(
literal|1
argument_list|,
name|timeout
argument_list|,
name|unit
argument_list|)
return|;
block|}
comment|/**    * Acquires permits from this {@link RateLimiter} if it can be acquired immediately without delay.    *    *<p>This method is equivalent to {@code tryAcquire(permits, 0, anyUnit)}.    *    * @param permits the number of permits to acquire    * @return {@code true} if the permits were acquired, {@code false} otherwise    * @throws IllegalArgumentException if the requested number of permits is negative or zero    * @since 14.0    */
DECL|method|tryAcquire (int permits)
specifier|public
name|boolean
name|tryAcquire
parameter_list|(
name|int
name|permits
parameter_list|)
block|{
return|return
name|tryAcquire
argument_list|(
name|permits
argument_list|,
literal|0
argument_list|,
name|MICROSECONDS
argument_list|)
return|;
block|}
comment|/**    * Acquires a permit from this {@link RateLimiter} if it can be acquired immediately without    * delay.    *    *<p>This method is equivalent to {@code tryAcquire(1)}.    *    * @return {@code true} if the permit was acquired, {@code false} otherwise    * @since 14.0    */
DECL|method|tryAcquire ()
specifier|public
name|boolean
name|tryAcquire
parameter_list|()
block|{
return|return
name|tryAcquire
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
name|MICROSECONDS
argument_list|)
return|;
block|}
comment|/**    * Acquires the given number of permits from this {@code RateLimiter} if it can be obtained    * without exceeding the specified {@code timeout}, or returns {@code false} immediately (without    * waiting) if the permits would not have been granted before the timeout expired.    *    * @param permits the number of permits to acquire    * @param timeout the maximum time to wait for the permits. Negative values are treated as zero.    * @param unit the time unit of the timeout argument    * @return {@code true} if the permits were acquired, {@code false} otherwise    * @throws IllegalArgumentException if the requested number of permits is negative or zero    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should accept a java.time.Duration
DECL|method|tryAcquire (int permits, long timeout, TimeUnit unit)
specifier|public
name|boolean
name|tryAcquire
parameter_list|(
name|int
name|permits
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|long
name|timeoutMicros
init|=
name|max
argument_list|(
name|unit
operator|.
name|toMicros
argument_list|(
name|timeout
argument_list|)
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|checkPermits
argument_list|(
name|permits
argument_list|)
expr_stmt|;
name|long
name|microsToWait
decl_stmt|;
synchronized|synchronized
init|(
name|mutex
argument_list|()
init|)
block|{
name|long
name|nowMicros
init|=
name|stopwatch
operator|.
name|readMicros
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|canAcquire
argument_list|(
name|nowMicros
argument_list|,
name|timeoutMicros
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
name|microsToWait
operator|=
name|reserveAndGetWaitLength
argument_list|(
name|permits
argument_list|,
name|nowMicros
argument_list|)
expr_stmt|;
block|}
block|}
name|stopwatch
operator|.
name|sleepMicrosUninterruptibly
argument_list|(
name|microsToWait
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|canAcquire (long nowMicros, long timeoutMicros)
specifier|private
name|boolean
name|canAcquire
parameter_list|(
name|long
name|nowMicros
parameter_list|,
name|long
name|timeoutMicros
parameter_list|)
block|{
return|return
name|queryEarliestAvailable
argument_list|(
name|nowMicros
argument_list|)
operator|-
name|timeoutMicros
operator|<=
name|nowMicros
return|;
block|}
comment|/**    * Reserves next ticket and returns the wait time that the caller must wait for.    *    * @return the required wait time, never negative    */
DECL|method|reserveAndGetWaitLength (int permits, long nowMicros)
specifier|final
name|long
name|reserveAndGetWaitLength
parameter_list|(
name|int
name|permits
parameter_list|,
name|long
name|nowMicros
parameter_list|)
block|{
name|long
name|momentAvailable
init|=
name|reserveEarliestAvailable
argument_list|(
name|permits
argument_list|,
name|nowMicros
argument_list|)
decl_stmt|;
return|return
name|max
argument_list|(
name|momentAvailable
operator|-
name|nowMicros
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**    * Returns the earliest time that permits are available (with one caveat).    *    * @return the time that permits are available, or, if permits are available immediately, an    *     arbitrary past or present time    */
DECL|method|queryEarliestAvailable (long nowMicros)
specifier|abstract
name|long
name|queryEarliestAvailable
parameter_list|(
name|long
name|nowMicros
parameter_list|)
function_decl|;
comment|/**    * Reserves the requested number of permits and returns the time that those permits can be used    * (with one caveat).    *    * @return the time that the permits may be used, or, if the permits may be used immediately, an    *     arbitrary past or present time    */
DECL|method|reserveEarliestAvailable (int permits, long nowMicros)
specifier|abstract
name|long
name|reserveEarliestAvailable
parameter_list|(
name|int
name|permits
parameter_list|,
name|long
name|nowMicros
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
name|Locale
operator|.
name|ROOT
argument_list|,
literal|"RateLimiter[stableRate=%3.1fqps]"
argument_list|,
name|getRate
argument_list|()
argument_list|)
return|;
block|}
DECL|class|SleepingStopwatch
specifier|abstract
specifier|static
class|class
name|SleepingStopwatch
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|SleepingStopwatch ()
specifier|protected
name|SleepingStopwatch
parameter_list|()
block|{}
comment|/*      * We always hold the mutex when calling this. TODO(cpovirk): Is that important? Perhaps we need      * to guarantee that each call to reserveEarliestAvailable, etc. sees a value>= the previous?      * Also, is it OK that we don't hold the mutex when sleeping?      */
DECL|method|readMicros ()
specifier|protected
specifier|abstract
name|long
name|readMicros
parameter_list|()
function_decl|;
DECL|method|sleepMicrosUninterruptibly (long micros)
specifier|protected
specifier|abstract
name|void
name|sleepMicrosUninterruptibly
parameter_list|(
name|long
name|micros
parameter_list|)
function_decl|;
DECL|method|createFromSystemTimer ()
specifier|public
specifier|static
name|SleepingStopwatch
name|createFromSystemTimer
parameter_list|()
block|{
return|return
operator|new
name|SleepingStopwatch
argument_list|()
block|{
specifier|final
name|Stopwatch
name|stopwatch
init|=
name|Stopwatch
operator|.
name|createStarted
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|long
name|readMicros
parameter_list|()
block|{
return|return
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|MICROSECONDS
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|sleepMicrosUninterruptibly
parameter_list|(
name|long
name|micros
parameter_list|)
block|{
if|if
condition|(
name|micros
operator|>
literal|0
condition|)
block|{
name|Uninterruptibles
operator|.
name|sleepUninterruptibly
argument_list|(
name|micros
argument_list|,
name|MICROSECONDS
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|;
block|}
block|}
DECL|method|checkPermits (int permits)
specifier|private
specifier|static
name|void
name|checkPermits
parameter_list|(
name|int
name|permits
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|permits
operator|>
literal|0
argument_list|,
literal|"Requested permits (%s) must be positive"
argument_list|,
name|permits
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

