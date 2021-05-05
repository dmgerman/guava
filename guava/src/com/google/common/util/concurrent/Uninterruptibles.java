begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|Verify
operator|.
name|verify
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
name|util
operator|.
name|concurrent
operator|.
name|Internal
operator|.
name|toNanosSaturated
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
name|base
operator|.
name|Preconditions
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
name|time
operator|.
name|Duration
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
name|BlockingQueue
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
name|CancellationException
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
name|CountDownLatch
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
name|ExecutionException
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
name|ExecutorService
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
name|Future
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
name|Semaphore
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeoutException
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
name|locks
operator|.
name|Condition
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
name|locks
operator|.
name|Lock
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
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Utilities for treating interruptible operations as uninterruptible. In all cases, if a thread is  * interrupted during such a call, the call continues to block until the result is available or the  * timeout elapses, and only then re-interrupts the thread.  *  * @author Anthony Zana  * @since 10.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|Uninterruptibles
specifier|public
specifier|final
class|class
name|Uninterruptibles
block|{
comment|// Implementation Note: As of 3-7-11, the logic for each blocking/timeout
comment|// methods is identical, save for method being invoked.
comment|/** Invokes {@code latch.}{@link CountDownLatch#await() await()} uninterruptibly. */
annotation|@
name|GwtIncompatible
comment|// concurrency
DECL|method|awaitUninterruptibly (CountDownLatch latch)
specifier|public
specifier|static
name|void
name|awaitUninterruptibly
parameter_list|(
name|CountDownLatch
name|latch
parameter_list|)
block|{
name|boolean
name|interrupted
init|=
literal|false
decl_stmt|;
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|interrupted
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|interrupted
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Invokes {@code latch.}{@link CountDownLatch#await(long, TimeUnit) await(timeout, unit)}    * uninterruptibly.    *    * @since 28.0    */
annotation|@
name|CanIgnoreReturnValue
comment|// TODO(cpovirk): Consider being more strict.
annotation|@
name|GwtIncompatible
comment|// concurrency
annotation|@
name|Beta
DECL|method|awaitUninterruptibly (CountDownLatch latch, Duration timeout)
specifier|public
specifier|static
name|boolean
name|awaitUninterruptibly
parameter_list|(
name|CountDownLatch
name|latch
parameter_list|,
name|Duration
name|timeout
parameter_list|)
block|{
return|return
name|awaitUninterruptibly
argument_list|(
name|latch
argument_list|,
name|toNanosSaturated
argument_list|(
name|timeout
argument_list|)
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
return|;
block|}
comment|/**    * Invokes {@code latch.}{@link CountDownLatch#await(long, TimeUnit) await(timeout, unit)}    * uninterruptibly.    */
annotation|@
name|CanIgnoreReturnValue
comment|// TODO(cpovirk): Consider being more strict.
annotation|@
name|GwtIncompatible
comment|// concurrency
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should accept a java.time.Duration
DECL|method|awaitUninterruptibly (CountDownLatch latch, long timeout, TimeUnit unit)
specifier|public
specifier|static
name|boolean
name|awaitUninterruptibly
parameter_list|(
name|CountDownLatch
name|latch
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|boolean
name|interrupted
init|=
literal|false
decl_stmt|;
try|try
block|{
name|long
name|remainingNanos
init|=
name|unit
operator|.
name|toNanos
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
name|long
name|end
init|=
name|System
operator|.
name|nanoTime
argument_list|()
operator|+
name|remainingNanos
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
comment|// CountDownLatch treats negative timeouts just like zero.
return|return
name|latch
operator|.
name|await
argument_list|(
name|remainingNanos
argument_list|,
name|NANOSECONDS
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|interrupted
operator|=
literal|true
expr_stmt|;
name|remainingNanos
operator|=
name|end
operator|-
name|System
operator|.
name|nanoTime
argument_list|()
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|interrupted
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Invokes {@code condition.}{@link Condition#await(long, TimeUnit) await(timeout, unit)}    * uninterruptibly.    *    * @since 28.0    */
annotation|@
name|GwtIncompatible
comment|// concurrency
annotation|@
name|Beta
DECL|method|awaitUninterruptibly (Condition condition, Duration timeout)
specifier|public
specifier|static
name|boolean
name|awaitUninterruptibly
parameter_list|(
name|Condition
name|condition
parameter_list|,
name|Duration
name|timeout
parameter_list|)
block|{
return|return
name|awaitUninterruptibly
argument_list|(
name|condition
argument_list|,
name|toNanosSaturated
argument_list|(
name|timeout
argument_list|)
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
return|;
block|}
comment|/**    * Invokes {@code condition.}{@link Condition#await(long, TimeUnit) await(timeout, unit)}    * uninterruptibly.    *    * @since 23.6    */
annotation|@
name|GwtIncompatible
comment|// concurrency
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should accept a java.time.Duration
DECL|method|awaitUninterruptibly (Condition condition, long timeout, TimeUnit unit)
specifier|public
specifier|static
name|boolean
name|awaitUninterruptibly
parameter_list|(
name|Condition
name|condition
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|boolean
name|interrupted
init|=
literal|false
decl_stmt|;
try|try
block|{
name|long
name|remainingNanos
init|=
name|unit
operator|.
name|toNanos
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
name|long
name|end
init|=
name|System
operator|.
name|nanoTime
argument_list|()
operator|+
name|remainingNanos
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
return|return
name|condition
operator|.
name|await
argument_list|(
name|remainingNanos
argument_list|,
name|NANOSECONDS
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|interrupted
operator|=
literal|true
expr_stmt|;
name|remainingNanos
operator|=
name|end
operator|-
name|System
operator|.
name|nanoTime
argument_list|()
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|interrupted
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/** Invokes {@code toJoin.}{@link Thread#join() join()} uninterruptibly. */
annotation|@
name|GwtIncompatible
comment|// concurrency
DECL|method|joinUninterruptibly (Thread toJoin)
specifier|public
specifier|static
name|void
name|joinUninterruptibly
parameter_list|(
name|Thread
name|toJoin
parameter_list|)
block|{
name|boolean
name|interrupted
init|=
literal|false
decl_stmt|;
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
name|toJoin
operator|.
name|join
argument_list|()
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|interrupted
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|interrupted
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Invokes {@code unit.}{@link TimeUnit#timedJoin(Thread, long) timedJoin(toJoin, timeout)}    * uninterruptibly.    *    * @since 28.0    */
annotation|@
name|GwtIncompatible
comment|// concurrency
annotation|@
name|Beta
DECL|method|joinUninterruptibly (Thread toJoin, Duration timeout)
specifier|public
specifier|static
name|void
name|joinUninterruptibly
parameter_list|(
name|Thread
name|toJoin
parameter_list|,
name|Duration
name|timeout
parameter_list|)
block|{
name|joinUninterruptibly
argument_list|(
name|toJoin
argument_list|,
name|toNanosSaturated
argument_list|(
name|timeout
argument_list|)
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
expr_stmt|;
block|}
comment|/**    * Invokes {@code unit.}{@link TimeUnit#timedJoin(Thread, long) timedJoin(toJoin, timeout)}    * uninterruptibly.    */
annotation|@
name|GwtIncompatible
comment|// concurrency
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should accept a java.time.Duration
DECL|method|joinUninterruptibly (Thread toJoin, long timeout, TimeUnit unit)
specifier|public
specifier|static
name|void
name|joinUninterruptibly
parameter_list|(
name|Thread
name|toJoin
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|toJoin
argument_list|)
expr_stmt|;
name|boolean
name|interrupted
init|=
literal|false
decl_stmt|;
try|try
block|{
name|long
name|remainingNanos
init|=
name|unit
operator|.
name|toNanos
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
name|long
name|end
init|=
name|System
operator|.
name|nanoTime
argument_list|()
operator|+
name|remainingNanos
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
comment|// TimeUnit.timedJoin() treats negative timeouts just like zero.
name|NANOSECONDS
operator|.
name|timedJoin
argument_list|(
name|toJoin
argument_list|,
name|remainingNanos
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|interrupted
operator|=
literal|true
expr_stmt|;
name|remainingNanos
operator|=
name|end
operator|-
name|System
operator|.
name|nanoTime
argument_list|()
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|interrupted
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Invokes {@code future.}{@link Future#get() get()} uninterruptibly.    *    *<p>Similar methods:    *    *<ul>    *<li>To retrieve a result from a {@code Future} that is already done, use {@link    *       Futures#getDone Futures.getDone}.    *<li>To treat {@link InterruptedException} uniformly with other exceptions, use {@link    *       Futures#getChecked(Future, Class) Futures.getChecked}.    *<li>To get uninterruptibility and remove checked exceptions, use {@link    *       Futures#getUnchecked}.    *</ul>    *    * @throws ExecutionException if the computation threw an exception    * @throws CancellationException if the computation was cancelled    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|ParametricNullness
DECL|method|getUninterruptibly (Future<V> future)
specifier|public
specifier|static
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
name|V
name|getUninterruptibly
argument_list|(
name|Future
argument_list|<
name|V
argument_list|>
name|future
argument_list|)
throws|throws
name|ExecutionException
block|{
name|boolean
name|interrupted
operator|=
literal|false
expr_stmt|;
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
return|return
name|future
operator|.
name|get
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|interrupted
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|interrupted
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

begin_comment
comment|/**    * Invokes {@code future.}{@link Future#get(long, TimeUnit) get(timeout, unit)} uninterruptibly.    *    *<p>Similar methods:    *    *<ul>    *<li>To retrieve a result from a {@code Future} that is already done, use {@link    *       Futures#getDone Futures.getDone}.    *<li>To treat {@link InterruptedException} uniformly with other exceptions, use {@link    *       Futures#getChecked(Future, Class, long, TimeUnit) Futures.getChecked}.    *<li>To get uninterruptibility and remove checked exceptions, use {@link    *       Futures#getUnchecked}.    *</ul>    *    * @throws ExecutionException if the computation threw an exception    * @throws CancellationException if the computation was cancelled    * @throws TimeoutException if the wait timed out    * @since 28.0    */
end_comment

begin_annotation
annotation|@
name|CanIgnoreReturnValue
end_annotation

begin_annotation
annotation|@
name|GwtIncompatible
end_annotation

begin_comment
comment|// java.time.Duration
end_comment

begin_annotation
annotation|@
name|Beta
end_annotation

begin_annotation
annotation|@
name|ParametricNullness
end_annotation

begin_expr_stmt
DECL|method|getUninterruptibly ( Future<V> future, Duration timeout)
specifier|public
specifier|static
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
name|V
name|getUninterruptibly
argument_list|(
name|Future
argument_list|<
name|V
argument_list|>
name|future
argument_list|,
name|Duration
name|timeout
argument_list|)
throws|throws
name|ExecutionException
throws|,
name|TimeoutException
block|{
end_expr_stmt

begin_return
return|return
name|getUninterruptibly
argument_list|(
name|future
argument_list|,
name|toNanosSaturated
argument_list|(
name|timeout
argument_list|)
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
return|;
end_return

begin_comment
unit|}
comment|/**    * Invokes {@code future.}{@link Future#get(long, TimeUnit) get(timeout, unit)} uninterruptibly.    *    *<p>Similar methods:    *    *<ul>    *<li>To retrieve a result from a {@code Future} that is already done, use {@link    *       Futures#getDone Futures.getDone}.    *<li>To treat {@link InterruptedException} uniformly with other exceptions, use {@link    *       Futures#getChecked(Future, Class, long, TimeUnit) Futures.getChecked}.    *<li>To get uninterruptibility and remove checked exceptions, use {@link    *       Futures#getUnchecked}.    *</ul>    *    * @throws ExecutionException if the computation threw an exception    * @throws CancellationException if the computation was cancelled    * @throws TimeoutException if the wait timed out    */
end_comment

begin_expr_stmt
unit|@
name|CanIgnoreReturnValue
expr|@
name|GwtIncompatible
comment|// TODO
expr|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should accept a java.time.Duration
expr|@
name|ParametricNullness
DECL|method|getUninterruptibly ( Future<V> future, long timeout, TimeUnit unit)
specifier|public
specifier|static
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
name|V
name|getUninterruptibly
argument_list|(
name|Future
argument_list|<
name|V
argument_list|>
name|future
argument_list|,
name|long
name|timeout
argument_list|,
name|TimeUnit
name|unit
argument_list|)
throws|throws
name|ExecutionException
throws|,
name|TimeoutException
block|{
name|boolean
name|interrupted
operator|=
literal|false
expr_stmt|;
end_expr_stmt

begin_try
try|try
block|{
name|long
name|remainingNanos
init|=
name|unit
operator|.
name|toNanos
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
name|long
name|end
init|=
name|System
operator|.
name|nanoTime
argument_list|()
operator|+
name|remainingNanos
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
comment|// Future treats negative timeouts just like zero.
return|return
name|future
operator|.
name|get
argument_list|(
name|remainingNanos
argument_list|,
name|NANOSECONDS
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|interrupted
operator|=
literal|true
expr_stmt|;
name|remainingNanos
operator|=
name|end
operator|-
name|System
operator|.
name|nanoTime
argument_list|()
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|interrupted
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
end_try

begin_comment
unit|}
comment|/** Invokes {@code queue.}{@link BlockingQueue#take() take()} uninterruptibly. */
end_comment

begin_function
unit|@
name|GwtIncompatible
comment|// concurrency
DECL|method|takeUninterruptibly (BlockingQueue<E> queue)
specifier|public
specifier|static
argument_list|<
name|E
argument_list|>
name|E
name|takeUninterruptibly
parameter_list|(
name|BlockingQueue
argument_list|<
name|E
argument_list|>
name|queue
parameter_list|)
block|{
name|boolean
name|interrupted
init|=
literal|false
decl_stmt|;
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
return|return
name|queue
operator|.
name|take
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|interrupted
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|interrupted
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_function

begin_comment
comment|/**    * Invokes {@code queue.}{@link BlockingQueue#put(Object) put(element)} uninterruptibly.    *    * @throws ClassCastException if the class of the specified element prevents it from being added    *     to the given queue    * @throws IllegalArgumentException if some property of the specified element prevents it from    *     being added to the given queue    */
end_comment

begin_function
annotation|@
name|GwtIncompatible
comment|// concurrency
DECL|method|putUninterruptibly (BlockingQueue<E> queue, E element)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|void
name|putUninterruptibly
parameter_list|(
name|BlockingQueue
argument_list|<
name|E
argument_list|>
name|queue
parameter_list|,
name|E
name|element
parameter_list|)
block|{
name|boolean
name|interrupted
init|=
literal|false
decl_stmt|;
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
name|queue
operator|.
name|put
argument_list|(
name|element
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|interrupted
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|interrupted
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_function

begin_comment
comment|// TODO(user): Support Sleeper somehow (wrapper or interface method)?
end_comment

begin_comment
comment|/**    * Invokes {@code unit.}{@link TimeUnit#sleep(long) sleep(sleepFor)} uninterruptibly.    *    * @since 28.0    */
end_comment

begin_function
annotation|@
name|GwtIncompatible
comment|// concurrency
annotation|@
name|Beta
DECL|method|sleepUninterruptibly (Duration sleepFor)
specifier|public
specifier|static
name|void
name|sleepUninterruptibly
parameter_list|(
name|Duration
name|sleepFor
parameter_list|)
block|{
name|sleepUninterruptibly
argument_list|(
name|toNanosSaturated
argument_list|(
name|sleepFor
argument_list|)
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
expr_stmt|;
block|}
end_function

begin_comment
comment|// TODO(user): Support Sleeper somehow (wrapper or interface method)?
end_comment

begin_comment
comment|/** Invokes {@code unit.}{@link TimeUnit#sleep(long) sleep(sleepFor)} uninterruptibly. */
end_comment

begin_function
annotation|@
name|GwtIncompatible
comment|// concurrency
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should accept a java.time.Duration
DECL|method|sleepUninterruptibly (long sleepFor, TimeUnit unit)
specifier|public
specifier|static
name|void
name|sleepUninterruptibly
parameter_list|(
name|long
name|sleepFor
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|boolean
name|interrupted
init|=
literal|false
decl_stmt|;
try|try
block|{
name|long
name|remainingNanos
init|=
name|unit
operator|.
name|toNanos
argument_list|(
name|sleepFor
argument_list|)
decl_stmt|;
name|long
name|end
init|=
name|System
operator|.
name|nanoTime
argument_list|()
operator|+
name|remainingNanos
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
comment|// TimeUnit.sleep() treats negative timeouts just like zero.
name|NANOSECONDS
operator|.
name|sleep
argument_list|(
name|remainingNanos
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|interrupted
operator|=
literal|true
expr_stmt|;
name|remainingNanos
operator|=
name|end
operator|-
name|System
operator|.
name|nanoTime
argument_list|()
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|interrupted
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_function

begin_comment
comment|/**    * Invokes {@code semaphore.}{@link Semaphore#tryAcquire(int, long, TimeUnit) tryAcquire(1,    * timeout, unit)} uninterruptibly.    *    * @since 28.0    */
end_comment

begin_function
annotation|@
name|GwtIncompatible
comment|// concurrency
annotation|@
name|Beta
DECL|method|tryAcquireUninterruptibly (Semaphore semaphore, Duration timeout)
specifier|public
specifier|static
name|boolean
name|tryAcquireUninterruptibly
parameter_list|(
name|Semaphore
name|semaphore
parameter_list|,
name|Duration
name|timeout
parameter_list|)
block|{
return|return
name|tryAcquireUninterruptibly
argument_list|(
name|semaphore
argument_list|,
name|toNanosSaturated
argument_list|(
name|timeout
argument_list|)
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Invokes {@code semaphore.}{@link Semaphore#tryAcquire(int, long, TimeUnit) tryAcquire(1,    * timeout, unit)} uninterruptibly.    *    * @since 18.0    */
end_comment

begin_function
annotation|@
name|GwtIncompatible
comment|// concurrency
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should accept a java.time.Duration
DECL|method|tryAcquireUninterruptibly ( Semaphore semaphore, long timeout, TimeUnit unit)
specifier|public
specifier|static
name|boolean
name|tryAcquireUninterruptibly
parameter_list|(
name|Semaphore
name|semaphore
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|tryAcquireUninterruptibly
argument_list|(
name|semaphore
argument_list|,
literal|1
argument_list|,
name|timeout
argument_list|,
name|unit
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Invokes {@code semaphore.}{@link Semaphore#tryAcquire(int, long, TimeUnit) tryAcquire(permits,    * timeout, unit)} uninterruptibly.    *    * @since 28.0    */
end_comment

begin_function
annotation|@
name|GwtIncompatible
comment|// concurrency
annotation|@
name|Beta
DECL|method|tryAcquireUninterruptibly ( Semaphore semaphore, int permits, Duration timeout)
specifier|public
specifier|static
name|boolean
name|tryAcquireUninterruptibly
parameter_list|(
name|Semaphore
name|semaphore
parameter_list|,
name|int
name|permits
parameter_list|,
name|Duration
name|timeout
parameter_list|)
block|{
return|return
name|tryAcquireUninterruptibly
argument_list|(
name|semaphore
argument_list|,
name|permits
argument_list|,
name|toNanosSaturated
argument_list|(
name|timeout
argument_list|)
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Invokes {@code semaphore.}{@link Semaphore#tryAcquire(int, long, TimeUnit) tryAcquire(permits,    * timeout, unit)} uninterruptibly.    *    * @since 18.0    */
end_comment

begin_function
annotation|@
name|GwtIncompatible
comment|// concurrency
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should accept a java.time.Duration
DECL|method|tryAcquireUninterruptibly ( Semaphore semaphore, int permits, long timeout, TimeUnit unit)
specifier|public
specifier|static
name|boolean
name|tryAcquireUninterruptibly
parameter_list|(
name|Semaphore
name|semaphore
parameter_list|,
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
name|boolean
name|interrupted
init|=
literal|false
decl_stmt|;
try|try
block|{
name|long
name|remainingNanos
init|=
name|unit
operator|.
name|toNanos
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
name|long
name|end
init|=
name|System
operator|.
name|nanoTime
argument_list|()
operator|+
name|remainingNanos
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
comment|// Semaphore treats negative timeouts just like zero.
return|return
name|semaphore
operator|.
name|tryAcquire
argument_list|(
name|permits
argument_list|,
name|remainingNanos
argument_list|,
name|NANOSECONDS
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|interrupted
operator|=
literal|true
expr_stmt|;
name|remainingNanos
operator|=
name|end
operator|-
name|System
operator|.
name|nanoTime
argument_list|()
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|interrupted
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_function

begin_comment
comment|/**    * Invokes {@code lock.}{@link Lock#tryLock(long, TimeUnit) tryLock(timeout, unit)}    * uninterruptibly.    *    * @since 30.0    */
end_comment

begin_function
annotation|@
name|GwtIncompatible
comment|// concurrency
annotation|@
name|Beta
DECL|method|tryLockUninterruptibly (Lock lock, Duration timeout)
specifier|public
specifier|static
name|boolean
name|tryLockUninterruptibly
parameter_list|(
name|Lock
name|lock
parameter_list|,
name|Duration
name|timeout
parameter_list|)
block|{
return|return
name|tryLockUninterruptibly
argument_list|(
name|lock
argument_list|,
name|toNanosSaturated
argument_list|(
name|timeout
argument_list|)
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Invokes {@code lock.}{@link Lock#tryLock(long, TimeUnit) tryLock(timeout, unit)}    * uninterruptibly.    *    * @since 30.0    */
end_comment

begin_function
annotation|@
name|GwtIncompatible
comment|// concurrency
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should accept a java.time.Duration
DECL|method|tryLockUninterruptibly (Lock lock, long timeout, TimeUnit unit)
specifier|public
specifier|static
name|boolean
name|tryLockUninterruptibly
parameter_list|(
name|Lock
name|lock
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|boolean
name|interrupted
init|=
literal|false
decl_stmt|;
try|try
block|{
name|long
name|remainingNanos
init|=
name|unit
operator|.
name|toNanos
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
name|long
name|end
init|=
name|System
operator|.
name|nanoTime
argument_list|()
operator|+
name|remainingNanos
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
return|return
name|lock
operator|.
name|tryLock
argument_list|(
name|remainingNanos
argument_list|,
name|NANOSECONDS
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|interrupted
operator|=
literal|true
expr_stmt|;
name|remainingNanos
operator|=
name|end
operator|-
name|System
operator|.
name|nanoTime
argument_list|()
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|interrupted
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_function

begin_comment
comment|/**    * Invokes {@code executor.}{@link ExecutorService#awaitTermination(long, TimeUnit)    * awaitTermination(long, TimeUnit)} uninterruptibly with no timeout.    *    * @since 30.0    */
end_comment

begin_function
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
comment|// concurrency
DECL|method|awaitTerminationUninterruptibly (ExecutorService executor)
specifier|public
specifier|static
name|void
name|awaitTerminationUninterruptibly
parameter_list|(
name|ExecutorService
name|executor
parameter_list|)
block|{
comment|// TODO(cpovirk): We could optimize this to avoid calling nanoTime() at all.
name|verify
argument_list|(
name|awaitTerminationUninterruptibly
argument_list|(
name|executor
argument_list|,
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|NANOSECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_comment
comment|/**    * Invokes {@code executor.}{@link ExecutorService#awaitTermination(long, TimeUnit)    * awaitTermination(long, TimeUnit)} uninterruptibly.    *    * @since 30.0    */
end_comment

begin_function
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
comment|// concurrency
DECL|method|awaitTerminationUninterruptibly ( ExecutorService executor, Duration timeout)
specifier|public
specifier|static
name|boolean
name|awaitTerminationUninterruptibly
parameter_list|(
name|ExecutorService
name|executor
parameter_list|,
name|Duration
name|timeout
parameter_list|)
block|{
return|return
name|awaitTerminationUninterruptibly
argument_list|(
name|executor
argument_list|,
name|toNanosSaturated
argument_list|(
name|timeout
argument_list|)
argument_list|,
name|NANOSECONDS
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Invokes {@code executor.}{@link ExecutorService#awaitTermination(long, TimeUnit)    * awaitTermination(long, TimeUnit)} uninterruptibly.    *    * @since 30.0    */
end_comment

begin_function
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
comment|// concurrency
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
DECL|method|awaitTerminationUninterruptibly ( ExecutorService executor, long timeout, TimeUnit unit)
specifier|public
specifier|static
name|boolean
name|awaitTerminationUninterruptibly
parameter_list|(
name|ExecutorService
name|executor
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|boolean
name|interrupted
init|=
literal|false
decl_stmt|;
try|try
block|{
name|long
name|remainingNanos
init|=
name|unit
operator|.
name|toNanos
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
name|long
name|end
init|=
name|System
operator|.
name|nanoTime
argument_list|()
operator|+
name|remainingNanos
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
return|return
name|executor
operator|.
name|awaitTermination
argument_list|(
name|remainingNanos
argument_list|,
name|NANOSECONDS
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|interrupted
operator|=
literal|true
expr_stmt|;
name|remainingNanos
operator|=
name|end
operator|-
name|System
operator|.
name|nanoTime
argument_list|()
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|interrupted
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_function

begin_comment
comment|// TODO(user): Add support for waitUninterruptibly.
end_comment

begin_constructor
DECL|method|Uninterruptibles ()
specifier|private
name|Uninterruptibles
parameter_list|()
block|{}
end_constructor

unit|}
end_unit

