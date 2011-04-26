begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|TimeUnit
import|;
end_import

begin_comment
comment|/**  * Utilities for treating interruptible operations as uninterruptible.  * In all cases, if a thread is interrupted during such a call, the call  * continues to block until the result is available or the timeout elapses,  * and only then re-interrupts the thread.  *  *<p>For operations involving {@link java.util.concurrent.Future},  * see {@link UninterruptibleFuture}.  *  * @author Anthony Zana  * @since Guava release 10  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|Uninterruptibles
specifier|public
specifier|final
class|class
name|Uninterruptibles
block|{
comment|// Implementation Note: As of 3-7-11, the logic for each blocking/timeout
comment|// methods is identical, save for method being invoked.
comment|// (Which is also identical to the logic in Futures.makeUninterruptiple)
comment|/**    * Invokes {@code latch.}{@link CountDownLatch#await() await()}    * uninterruptibly.    */
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
name|ignored
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
comment|/**    * Invokes    * {@code latch.}{@link CountDownLatch#await(long, TimeUnit)    * await(long, TimeUnit)} uninterruptibly.    */
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
comment|// TODO(user): Support Sleeper somehow (wrapper or interface method)?
comment|/**    * Invokes {@code unit.}{@link TimeUnit#sleep(long) sleep(sleepFor)}    * uninterruptibly.    */
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
comment|// TODO(user): Add support for joinUninterruptibly and waitUninterruptibly.
DECL|method|Uninterruptibles ()
specifier|private
name|Uninterruptibles
parameter_list|()
block|{}
block|}
end_class

end_unit

