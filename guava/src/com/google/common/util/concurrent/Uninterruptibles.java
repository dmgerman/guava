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

begin_comment
comment|/**  * Utilities for treating interruptible operations as uninterruptible. In all cases, if a thread is  * interrupted during such a call, the call continues to block until the result is available or the  * timeout elapses, and only then re-interrupts the thread.  *  * @author Anthony Zana  * @since 10.0  */
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
DECL|class|Uninterruptibles
specifier|public
specifier|final
class|class
name|Uninterruptibles
block|{
comment|// Implementation Note: As of 3-7-11, the logic for each blocking/timeout
comment|// methods is identical, save for method being invoked.
comment|/**    * Invokes {@code latch.}{@link CountDownLatch#await() await()} uninterruptibly.    */
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
comment|/**    * Invokes {@code latch.}{@link CountDownLatch#await(long, TimeUnit) await(timeout, unit)}    * uninterruptibly.    */
annotation|@
name|CanIgnoreReturnValue
comment|// TODO(cpovirk): Consider being more strict.
annotation|@
name|GwtIncompatible
comment|// concurrency
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
comment|/**    * Invokes {@code toJoin.}{@link Thread#join() join()} uninterruptibly.    */
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
comment|/**    * Invokes {@code future.}{@link Future#get() get()} uninterruptibly. To get uninterruptibility    * and remove checked exceptions, see {@link Futures#getUnchecked}.    *    *<p>If instead, you wish to treat {@link InterruptedException} uniformly with other exceptions,    * see {@link Futures#getChecked(Future, Class) Futures.getChecked}.    *    * @throws ExecutionException if the computation threw an exception    * @throws CancellationException if the computation was cancelled    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|getUninterruptibly (Future<V> future)
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|V
name|getUninterruptibly
parameter_list|(
name|Future
argument_list|<
name|V
argument_list|>
name|future
parameter_list|)
throws|throws
name|ExecutionException
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
comment|/**    * Invokes {@code future.}{@link Future#get(long, TimeUnit) get(timeout, unit)} uninterruptibly.    *    *<p>If instead, you wish to treat {@link InterruptedException} uniformly with other exceptions,    * see {@link Futures#getChecked(Future, Class) Futures.getChecked}.    *    * @throws ExecutionException if the computation threw an exception    * @throws CancellationException if the computation was cancelled    * @throws TimeoutException if the wait timed out    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|GwtIncompatible
comment|// TODO
DECL|method|getUninterruptibly (Future<V> future, long timeout, TimeUnit unit)
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|V
name|getUninterruptibly
parameter_list|(
name|Future
argument_list|<
name|V
argument_list|>
name|future
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|ExecutionException
throws|,
name|TimeoutException
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
block|}
comment|/**    * Invokes {@code unit.}{@link TimeUnit#timedJoin(Thread, long) timedJoin(toJoin, timeout)}    * uninterruptibly.    */
annotation|@
name|GwtIncompatible
comment|// concurrency
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
comment|/**    * Invokes {@code queue.}{@link BlockingQueue#take() take()} uninterruptibly.    */
annotation|@
name|GwtIncompatible
comment|// concurrency
DECL|method|takeUninterruptibly (BlockingQueue<E> queue)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
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
comment|/**    * Invokes {@code queue.}{@link BlockingQueue#put(Object) put(element)} uninterruptibly.    *    * @throws ClassCastException if the class of the specified element prevents it from being added    *     to the given queue    * @throws IllegalArgumentException if some property of the specified element prevents it from    *     being added to the given queue    */
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
comment|// TODO(user): Support Sleeper somehow (wrapper or interface method)?
comment|/**    * Invokes {@code unit.}{@link TimeUnit#sleep(long) sleep(sleepFor)} uninterruptibly.    */
annotation|@
name|GwtIncompatible
comment|// concurrency
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
comment|/**    * Invokes {@code semaphore.}{@link Semaphore#tryAcquire(int, long, TimeUnit) tryAcquire(1,    * timeout, unit)} uninterruptibly.    *    * @since 18.0    */
annotation|@
name|GwtIncompatible
comment|// concurrency
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
comment|/**    * Invokes {@code semaphore.}{@link Semaphore#tryAcquire(int, long, TimeUnit) tryAcquire(permits,    * timeout, unit)} uninterruptibly.    *    * @since 18.0    */
annotation|@
name|GwtIncompatible
comment|// concurrency
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
comment|// TODO(user): Add support for waitUninterruptibly.
DECL|method|Uninterruptibles ()
specifier|private
name|Uninterruptibles
parameter_list|()
block|{}
block|}
end_class

end_unit

