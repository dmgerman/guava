begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not  * use this file except in compliance with the License.  You may obtain a copy  * of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the  * License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.util.concurrent.testing
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
operator|.
name|testing
package|;
end_package

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
name|util
operator|.
name|concurrent
operator|.
name|ListenableFuture
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
name|Executors
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Abstract test case parent for anything implementing {@link ListenableFuture}. Tests the two get  * methods and the addListener method.  *  * @author Sven Mawson  * @since 10.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
DECL|class|AbstractListenableFutureTest
specifier|public
specifier|abstract
class|class
name|AbstractListenableFutureTest
extends|extends
name|TestCase
block|{
DECL|field|latch
specifier|protected
name|CountDownLatch
name|latch
decl_stmt|;
DECL|field|future
specifier|protected
name|ListenableFuture
argument_list|<
name|Boolean
argument_list|>
name|future
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create a latch and a future that waits on the latch.
name|latch
operator|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|future
operator|=
name|createListenableFuture
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
literal|null
argument_list|,
name|latch
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Make sure we have no waiting threads.
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
comment|/** Constructs a listenable future with a value available after the latch has counted down. */
DECL|method|createListenableFuture ( V value, Exception except, CountDownLatch waitOn)
specifier|protected
specifier|abstract
parameter_list|<
name|V
parameter_list|>
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|createListenableFuture
parameter_list|(
name|V
name|value
parameter_list|,
name|Exception
name|except
parameter_list|,
name|CountDownLatch
name|waitOn
parameter_list|)
function_decl|;
comment|/** Tests that the {@link Future#get()} method blocks until a value is available. */
DECL|method|testGetBlocksUntilValueAvailable ()
specifier|public
name|void
name|testGetBlocksUntilValueAvailable
parameter_list|()
throws|throws
name|Throwable
block|{
name|assertFalse
argument_list|(
name|future
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|future
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|CountDownLatch
name|successLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|Throwable
index|[]
name|badness
init|=
operator|new
name|Throwable
index|[
literal|1
index|]
decl_stmt|;
comment|// Wait on the future in a separate thread.
operator|new
name|Thread
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|assertSame
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|future
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|successLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|badness
index|[
literal|0
index|]
operator|=
name|t
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// Release the future value.
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|successLatch
operator|.
name|await
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|badness
index|[
literal|0
index|]
operator|!=
literal|null
condition|)
block|{
throw|throw
name|badness
index|[
literal|0
index|]
throw|;
block|}
name|assertTrue
argument_list|(
name|future
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|future
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** Tests that the {@link Future#get(long, TimeUnit)} method times out correctly. */
DECL|method|testTimeoutOnGetWorksCorrectly ()
specifier|public
name|void
name|testTimeoutOnGetWorksCorrectly
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
comment|// The task thread waits for the latch, so we expect a timeout here.
try|try
block|{
name|future
operator|.
name|get
argument_list|(
literal|20
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have timed out trying to get the value."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|expected
parameter_list|)
block|{     }
finally|finally
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Tests that a canceled future throws a cancellation exception.    *    *<p>This method checks the cancel, isCancelled, and isDone methods.    */
DECL|method|testCanceledFutureThrowsCancellation ()
specifier|public
name|void
name|testCanceledFutureThrowsCancellation
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFalse
argument_list|(
name|future
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|future
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|CountDownLatch
name|successLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|// Run cancellation in a separate thread as an extra thread-safety test.
operator|new
name|Thread
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|future
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CancellationException
name|expected
parameter_list|)
block|{
name|successLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ignored
parameter_list|)
block|{
comment|// All other errors are ignored, we expect a cancellation.
block|}
block|}
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|future
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|future
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|future
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|future
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|future
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|successLatch
operator|.
name|await
argument_list|(
literal|200
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenersNotifiedOnError ()
specifier|public
name|void
name|testListenersNotifiedOnError
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CountDownLatch
name|successLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|listenerLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|ExecutorService
name|exec
init|=
name|Executors
operator|.
name|newCachedThreadPool
argument_list|()
decl_stmt|;
name|future
operator|.
name|addListener
argument_list|(
name|listenerLatch
operator|::
name|countDown
argument_list|,
name|exec
argument_list|)
expr_stmt|;
operator|new
name|Thread
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|future
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CancellationException
name|expected
parameter_list|)
block|{
name|successLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ignored
parameter_list|)
block|{
comment|// No success latch count down.
block|}
block|}
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|future
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|future
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|future
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|successLatch
operator|.
name|await
argument_list|(
literal|200
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|listenerLatch
operator|.
name|await
argument_list|(
literal|200
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|exec
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|exec
operator|.
name|awaitTermination
argument_list|(
literal|100
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
comment|/**    * Tests that all listeners complete, even if they were added before or after the future was    * finishing. Also acts as a concurrency test to make sure the locking is done correctly when a    * future is finishing so that no listeners can be lost.    */
DECL|method|testAllListenersCompleteSuccessfully ()
specifier|public
name|void
name|testAllListenersCompleteSuccessfully
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
name|ExecutorService
name|exec
init|=
name|Executors
operator|.
name|newCachedThreadPool
argument_list|()
decl_stmt|;
name|int
name|listenerCount
init|=
literal|20
decl_stmt|;
specifier|final
name|CountDownLatch
name|listenerLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|listenerCount
argument_list|)
decl_stmt|;
comment|// Test that listeners added both before and after the value is available
comment|// get called correctly.
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|20
condition|;
name|i
operator|++
control|)
block|{
comment|// Right in the middle start up a thread to close the latch.
if|if
condition|(
name|i
operator|==
literal|10
condition|)
block|{
operator|new
name|Thread
argument_list|(
parameter_list|()
lambda|->
name|latch
operator|.
name|countDown
argument_list|()
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
name|future
operator|.
name|addListener
argument_list|(
name|listenerLatch
operator|::
name|countDown
argument_list|,
name|exec
argument_list|)
expr_stmt|;
block|}
name|assertSame
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|future
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// Wait for the listener latch to complete.
name|listenerLatch
operator|.
name|await
argument_list|(
literal|500
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|exec
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|exec
operator|.
name|awaitTermination
argument_list|(
literal|500
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

