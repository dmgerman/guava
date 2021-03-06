begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2014 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|Callables
operator|.
name|returning
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
name|Futures
operator|.
name|getDone
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
name|TestPlatform
operator|.
name|verifyThreadWasNotInterrupted
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
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
name|CyclicBarrier
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
name|atomic
operator|.
name|AtomicBoolean
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
name|atomic
operator|.
name|AtomicInteger
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
comment|/** Test case for {@link TrustedListenableFutureTask}. */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|TrustedListenableFutureTaskTest
specifier|public
class|class
name|TrustedListenableFutureTaskTest
extends|extends
name|TestCase
block|{
DECL|method|testSuccessful ()
specifier|public
name|void
name|testSuccessful
parameter_list|()
throws|throws
name|Exception
block|{
name|TrustedListenableFutureTask
argument_list|<
name|Integer
argument_list|>
name|task
init|=
name|TrustedListenableFutureTask
operator|.
name|create
argument_list|(
name|returning
argument_list|(
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|task
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|task
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|task
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|getDone
argument_list|(
name|task
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCancelled ()
specifier|public
name|void
name|testCancelled
parameter_list|()
throws|throws
name|Exception
block|{
name|TrustedListenableFutureTask
argument_list|<
name|Integer
argument_list|>
name|task
init|=
name|TrustedListenableFutureTask
operator|.
name|create
argument_list|(
name|returning
argument_list|(
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|task
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|task
operator|.
name|cancel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|task
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|task
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|task
operator|.
name|wasInterrupted
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|getDone
argument_list|(
name|task
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CancellationException
name|expected
parameter_list|)
block|{     }
name|verifyThreadWasNotInterrupted
argument_list|()
expr_stmt|;
block|}
DECL|method|testFailed ()
specifier|public
name|void
name|testFailed
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Exception
name|e
init|=
operator|new
name|Exception
argument_list|()
decl_stmt|;
name|TrustedListenableFutureTask
argument_list|<
name|Integer
argument_list|>
name|task
init|=
name|TrustedListenableFutureTask
operator|.
name|create
argument_list|(
operator|new
name|Callable
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|call
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
name|e
throw|;
block|}
block|}
argument_list|)
decl_stmt|;
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|task
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|task
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|getDone
argument_list|(
name|task
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|executionException
parameter_list|)
block|{
name|assertThat
argument_list|(
name|executionException
argument_list|)
operator|.
name|hasCauseThat
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|GwtIncompatible
comment|// blocking wait
DECL|method|testCancel_interrupted ()
specifier|public
name|void
name|testCancel_interrupted
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|AtomicBoolean
name|interruptedExceptionThrown
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
specifier|final
name|CountDownLatch
name|enterLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|exitLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|TrustedListenableFutureTask
argument_list|<
name|Integer
argument_list|>
name|task
init|=
name|TrustedListenableFutureTask
operator|.
name|create
argument_list|(
operator|new
name|Callable
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|enterLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
try|try
block|{
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
operator|.
name|await
argument_list|()
expr_stmt|;
comment|// wait forever
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|interruptedExceptionThrown
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
finally|finally
block|{                 }
block|}
block|}
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|task
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|Thread
name|thread
init|=
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|exitLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
name|enterLatch
operator|.
name|await
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|task
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|task
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|task
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|task
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|task
operator|.
name|wasInterrupted
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|task
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CancellationException
name|expected
parameter_list|)
block|{     }
name|exitLatch
operator|.
name|await
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|interruptedExceptionThrown
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// blocking wait
DECL|method|testRunIdempotency ()
specifier|public
name|void
name|testRunIdempotency
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|int
name|numThreads
init|=
literal|10
decl_stmt|;
specifier|final
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|numThreads
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1000
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
specifier|final
name|TrustedListenableFutureTask
argument_list|<
name|Integer
argument_list|>
name|task
init|=
name|TrustedListenableFutureTask
operator|.
name|create
argument_list|(
operator|new
name|Callable
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|call
parameter_list|()
block|{
return|return
name|counter
operator|.
name|incrementAndGet
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
specifier|final
name|CyclicBarrier
name|barrier
init|=
operator|new
name|CyclicBarrier
argument_list|(
name|numThreads
operator|+
literal|1
argument_list|)
decl_stmt|;
name|Runnable
name|wrapper
init|=
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|awaitUnchecked
argument_list|(
name|barrier
argument_list|)
expr_stmt|;
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
name|awaitUnchecked
argument_list|(
name|barrier
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
literal|10
condition|;
name|j
operator|++
control|)
block|{
name|executor
operator|.
name|execute
argument_list|(
name|wrapper
argument_list|)
expr_stmt|;
block|}
name|barrier
operator|.
name|await
argument_list|()
expr_stmt|;
comment|// release the threads!
name|barrier
operator|.
name|await
argument_list|()
expr_stmt|;
comment|// wait for them all to complete
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|task
operator|.
name|get
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|counter
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// blocking wait
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CountDownLatch
name|enterLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|exitLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|TrustedListenableFutureTask
argument_list|<
name|Void
argument_list|>
name|task
init|=
name|TrustedListenableFutureTask
operator|.
name|create
argument_list|(
operator|new
name|Callable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|enterLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
operator|.
name|await
argument_list|()
expr_stmt|;
comment|// wait forever
return|return
literal|null
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|task
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|Thread
name|thread
init|=
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|exitLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|,
literal|"Custom thread name"
argument_list|)
decl_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
name|enterLatch
operator|.
name|await
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|task
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|task
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|)
operator|.
name|contains
argument_list|(
literal|"Custom thread name"
argument_list|)
expr_stmt|;
name|task
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|exitLatch
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// used only in GwtIncomaptible tests
DECL|method|awaitUnchecked (CyclicBarrier barrier)
specifier|private
name|void
name|awaitUnchecked
parameter_list|(
name|CyclicBarrier
name|barrier
parameter_list|)
block|{
try|try
block|{
name|barrier
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

