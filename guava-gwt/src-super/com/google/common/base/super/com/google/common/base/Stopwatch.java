begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
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
name|checkNotNull
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
name|checkState
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
name|DAYS
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
name|HOURS
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
name|MILLISECONDS
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
name|MINUTES
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
name|NANOSECONDS
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
name|GwtCompatible
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
name|javax
operator|.
name|annotation
operator|.
name|CheckReturnValue
import|;
end_import

begin_comment
comment|/**  * An object that measures elapsed time in nanoseconds. It is useful to measure  * elapsed time using this class instead of direct calls to {@link  * System#nanoTime} for a few reasons:  *  *<ul>  *<li>An alternate time source can be substituted, for testing or performance  *     reasons.  *<li>As documented by {@code nanoTime}, the value returned has no absolute  *     meaning, and can only be interpreted as relative to another timestamp  *     returned by {@code nanoTime} at a different time. {@code Stopwatch} is a  *     more effective abstraction because it exposes only these relative values,  *     not the absolute ones.  *</ul>  *  *<p>Basic usage:  *<pre>  *   Stopwatch stopwatch = Stopwatch.{@link #createStarted createStarted}();  *   doSomething();  *   stopwatch.{@link #stop stop}(); // optional  *  *   long millis = stopwatch.elapsed(MILLISECONDS);  *  *   log.info("time: " + stopwatch); // formatted string like "12.3 ms"</pre>  *  *<p>Stopwatch methods are not idempotent; it is an error to start or stop a  * stopwatch that is already in the desired state.  *  *<p>When testing code that uses this class, use  * {@link #createUnstarted(Ticker)} or {@link #createStarted(Ticker)} to  * supply a fake or mock ticker.  *<!-- TODO(kevinb): restore the "such as" --> This allows you to  * simulate any valid behavior of the stopwatch.  *  *<p><b>Note:</b> This class is not thread-safe.  *  * @author Kevin Bourrillion  * @since 10.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|Stopwatch
specifier|public
specifier|final
class|class
name|Stopwatch
block|{
DECL|field|ticker
specifier|private
specifier|final
name|Ticker
name|ticker
decl_stmt|;
DECL|field|isRunning
specifier|private
name|boolean
name|isRunning
decl_stmt|;
DECL|field|elapsedNanos
specifier|private
name|long
name|elapsedNanos
decl_stmt|;
DECL|field|startTick
specifier|private
name|long
name|startTick
decl_stmt|;
comment|/**    * Creates (but does not start) a new stopwatch using {@link System#nanoTime}    * as its time source.    *    * @since 15.0    */
annotation|@
name|CheckReturnValue
DECL|method|createUnstarted ()
specifier|public
specifier|static
name|Stopwatch
name|createUnstarted
parameter_list|()
block|{
return|return
operator|new
name|Stopwatch
argument_list|()
return|;
block|}
comment|/**    * Creates (but does not start) a new stopwatch, using the specified time    * source.    *    * @since 15.0    */
annotation|@
name|CheckReturnValue
DECL|method|createUnstarted (Ticker ticker)
specifier|public
specifier|static
name|Stopwatch
name|createUnstarted
parameter_list|(
name|Ticker
name|ticker
parameter_list|)
block|{
return|return
operator|new
name|Stopwatch
argument_list|(
name|ticker
argument_list|)
return|;
block|}
comment|/**    * Creates (and starts) a new stopwatch using {@link System#nanoTime}    * as its time source.    *    * @since 15.0    */
annotation|@
name|CheckReturnValue
DECL|method|createStarted ()
specifier|public
specifier|static
name|Stopwatch
name|createStarted
parameter_list|()
block|{
return|return
operator|new
name|Stopwatch
argument_list|()
operator|.
name|start
argument_list|()
return|;
block|}
comment|/**    * Creates (and starts) a new stopwatch, using the specified time    * source.    *    * @since 15.0    */
annotation|@
name|CheckReturnValue
DECL|method|createStarted (Ticker ticker)
specifier|public
specifier|static
name|Stopwatch
name|createStarted
parameter_list|(
name|Ticker
name|ticker
parameter_list|)
block|{
return|return
operator|new
name|Stopwatch
argument_list|(
name|ticker
argument_list|)
operator|.
name|start
argument_list|()
return|;
block|}
comment|/**    * Creates (but does not start) a new stopwatch using {@link System#nanoTime}    * as its time source.    *    * @deprecated Use {@link Stopwatch#createUnstarted()} instead.    */
annotation|@
name|Deprecated
DECL|method|Stopwatch ()
name|Stopwatch
parameter_list|()
block|{
name|this
argument_list|(
name|Ticker
operator|.
name|systemTicker
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates (but does not start) a new stopwatch, using the specified time    * source.    *    * @deprecated Use {@link Stopwatch#createUnstarted(Ticker)} instead.    */
annotation|@
name|Deprecated
DECL|method|Stopwatch (Ticker ticker)
name|Stopwatch
parameter_list|(
name|Ticker
name|ticker
parameter_list|)
block|{
name|this
operator|.
name|ticker
operator|=
name|checkNotNull
argument_list|(
name|ticker
argument_list|,
literal|"ticker"
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns {@code true} if {@link #start()} has been called on this stopwatch,    * and {@link #stop()} has not been called since the last call to {@code    * start()}.    */
annotation|@
name|CheckReturnValue
DECL|method|isRunning ()
specifier|public
name|boolean
name|isRunning
parameter_list|()
block|{
return|return
name|isRunning
return|;
block|}
comment|/**    * Starts the stopwatch.    *    * @return this {@code Stopwatch} instance    * @throws IllegalStateException if the stopwatch is already running.    */
DECL|method|start ()
specifier|public
name|Stopwatch
name|start
parameter_list|()
block|{
name|checkState
argument_list|(
operator|!
name|isRunning
argument_list|,
literal|"This stopwatch is already running."
argument_list|)
expr_stmt|;
name|isRunning
operator|=
literal|true
expr_stmt|;
name|startTick
operator|=
name|ticker
operator|.
name|read
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Stops the stopwatch. Future reads will return the fixed duration that had    * elapsed up to this point.    *    * @return this {@code Stopwatch} instance    * @throws IllegalStateException if the stopwatch is already stopped.    */
DECL|method|stop ()
specifier|public
name|Stopwatch
name|stop
parameter_list|()
block|{
name|long
name|tick
init|=
name|ticker
operator|.
name|read
argument_list|()
decl_stmt|;
name|checkState
argument_list|(
name|isRunning
argument_list|,
literal|"This stopwatch is already stopped."
argument_list|)
expr_stmt|;
name|isRunning
operator|=
literal|false
expr_stmt|;
name|elapsedNanos
operator|+=
name|tick
operator|-
name|startTick
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Sets the elapsed time for this stopwatch to zero,    * and places it in a stopped state.    *    * @return this {@code Stopwatch} instance    */
DECL|method|reset ()
specifier|public
name|Stopwatch
name|reset
parameter_list|()
block|{
name|elapsedNanos
operator|=
literal|0
expr_stmt|;
name|isRunning
operator|=
literal|false
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|elapsedNanos ()
specifier|private
name|long
name|elapsedNanos
parameter_list|()
block|{
return|return
name|isRunning
condition|?
name|ticker
operator|.
name|read
argument_list|()
operator|-
name|startTick
operator|+
name|elapsedNanos
else|:
name|elapsedNanos
return|;
block|}
comment|/**    * Returns the current elapsed time shown on this stopwatch, expressed    * in the desired time unit, with any fraction rounded down.    *    *<p>Note that the overhead of measurement can be more than a microsecond, so    * it is generally not useful to specify {@link TimeUnit#NANOSECONDS}    * precision here.    *    * @since 14.0 (since 10.0 as {@code elapsedTime()})    */
annotation|@
name|CheckReturnValue
DECL|method|elapsed (TimeUnit desiredUnit)
specifier|public
name|long
name|elapsed
parameter_list|(
name|TimeUnit
name|desiredUnit
parameter_list|)
block|{
return|return
name|desiredUnit
operator|.
name|convert
argument_list|(
name|elapsedNanos
argument_list|()
argument_list|,
name|NANOSECONDS
argument_list|)
return|;
block|}
DECL|method|chooseUnit (long nanos)
specifier|private
specifier|static
name|TimeUnit
name|chooseUnit
parameter_list|(
name|long
name|nanos
parameter_list|)
block|{
if|if
condition|(
name|DAYS
operator|.
name|convert
argument_list|(
name|nanos
argument_list|,
name|NANOSECONDS
argument_list|)
operator|>
literal|0
condition|)
block|{
return|return
name|DAYS
return|;
block|}
if|if
condition|(
name|HOURS
operator|.
name|convert
argument_list|(
name|nanos
argument_list|,
name|NANOSECONDS
argument_list|)
operator|>
literal|0
condition|)
block|{
return|return
name|HOURS
return|;
block|}
if|if
condition|(
name|MINUTES
operator|.
name|convert
argument_list|(
name|nanos
argument_list|,
name|NANOSECONDS
argument_list|)
operator|>
literal|0
condition|)
block|{
return|return
name|MINUTES
return|;
block|}
if|if
condition|(
name|SECONDS
operator|.
name|convert
argument_list|(
name|nanos
argument_list|,
name|NANOSECONDS
argument_list|)
operator|>
literal|0
condition|)
block|{
return|return
name|SECONDS
return|;
block|}
if|if
condition|(
name|MILLISECONDS
operator|.
name|convert
argument_list|(
name|nanos
argument_list|,
name|NANOSECONDS
argument_list|)
operator|>
literal|0
condition|)
block|{
return|return
name|MILLISECONDS
return|;
block|}
if|if
condition|(
name|MICROSECONDS
operator|.
name|convert
argument_list|(
name|nanos
argument_list|,
name|NANOSECONDS
argument_list|)
operator|>
literal|0
condition|)
block|{
return|return
name|MICROSECONDS
return|;
block|}
return|return
name|NANOSECONDS
return|;
block|}
DECL|method|abbreviate (TimeUnit unit)
specifier|private
specifier|static
name|String
name|abbreviate
parameter_list|(
name|TimeUnit
name|unit
parameter_list|)
block|{
switch|switch
condition|(
name|unit
condition|)
block|{
case|case
name|NANOSECONDS
case|:
return|return
literal|"ns"
return|;
case|case
name|MICROSECONDS
case|:
return|return
literal|"\u03bcs"
return|;
comment|// Î¼s
case|case
name|MILLISECONDS
case|:
return|return
literal|"ms"
return|;
case|case
name|SECONDS
case|:
return|return
literal|"s"
return|;
case|case
name|MINUTES
case|:
return|return
literal|"min"
return|;
case|case
name|HOURS
case|:
return|return
literal|"h"
return|;
case|case
name|DAYS
case|:
return|return
literal|"d"
return|;
default|default:
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

