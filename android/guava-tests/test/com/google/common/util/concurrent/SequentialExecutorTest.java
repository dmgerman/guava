begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|MoreExecutors
operator|.
name|newSequentialExecutor
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
name|Uninterruptibles
operator|.
name|awaitUninterruptibly
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
name|collect
operator|.
name|ImmutableList
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
name|collect
operator|.
name|Lists
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
name|collect
operator|.
name|Queues
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
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
name|Executor
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
name|RejectedExecutionException
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
comment|/**  * Tests {@link SequentialExecutor}.  *  * @author JJ Furman  */
end_comment

begin_class
DECL|class|SequentialExecutorTest
specifier|public
class|class
name|SequentialExecutorTest
extends|extends
name|TestCase
block|{
DECL|class|FakeExecutor
specifier|private
specifier|static
class|class
name|FakeExecutor
implements|implements
name|Executor
block|{
DECL|field|tasks
name|Queue
argument_list|<
name|Runnable
argument_list|>
name|tasks
init|=
name|Queues
operator|.
name|newArrayDeque
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|execute (Runnable command)
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|command
parameter_list|)
block|{
name|tasks
operator|.
name|add
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
DECL|method|hasNext ()
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
operator|!
name|tasks
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|runNext ()
name|void
name|runNext
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"expected at least one task to run"
argument_list|,
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|tasks
operator|.
name|remove
argument_list|()
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
DECL|method|runAll ()
name|void
name|runAll
parameter_list|()
block|{
while|while
condition|(
name|hasNext
argument_list|()
condition|)
block|{
name|runNext
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|field|fakePool
specifier|private
name|FakeExecutor
name|fakePool
decl_stmt|;
DECL|field|e
specifier|private
name|SequentialExecutor
name|e
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|fakePool
operator|=
operator|new
name|FakeExecutor
argument_list|()
expr_stmt|;
name|e
operator|=
operator|new
name|SequentialExecutor
argument_list|(
name|fakePool
argument_list|)
expr_stmt|;
block|}
DECL|method|testConstructingWithNullExecutor_fails ()
specifier|public
name|void
name|testConstructingWithNullExecutor_fails
parameter_list|()
block|{
try|try
block|{
operator|new
name|SequentialExecutor
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have failed with NullPointerException."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testBasics ()
specifier|public
name|void
name|testBasics
parameter_list|()
block|{
specifier|final
name|AtomicInteger
name|totalCalls
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|Runnable
name|intCounter
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
name|totalCalls
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
comment|// Make sure that no other tasks are scheduled to run while this is running.
name|assertFalse
argument_list|(
name|fakePool
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|assertFalse
argument_list|(
name|fakePool
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|.
name|execute
argument_list|(
name|intCounter
argument_list|)
expr_stmt|;
comment|// A task should have been scheduled
name|assertTrue
argument_list|(
name|fakePool
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|.
name|execute
argument_list|(
name|intCounter
argument_list|)
expr_stmt|;
comment|// Our executor hasn't run any tasks yet.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|totalCalls
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|fakePool
operator|.
name|runAll
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|totalCalls
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// Queue is empty so no runner should be scheduled.
name|assertFalse
argument_list|(
name|fakePool
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
comment|// Check that execute can be safely repeated
name|e
operator|.
name|execute
argument_list|(
name|intCounter
argument_list|)
expr_stmt|;
name|e
operator|.
name|execute
argument_list|(
name|intCounter
argument_list|)
expr_stmt|;
name|e
operator|.
name|execute
argument_list|(
name|intCounter
argument_list|)
expr_stmt|;
comment|// No change yet.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|totalCalls
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|fakePool
operator|.
name|runAll
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|totalCalls
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|fakePool
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOrdering ()
specifier|public
name|void
name|testOrdering
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|Integer
argument_list|>
name|callOrder
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
class|class
name|FakeOp
implements|implements
name|Runnable
block|{
specifier|final
name|int
name|op
decl_stmt|;
name|FakeOp
parameter_list|(
name|int
name|op
parameter_list|)
block|{
name|this
operator|.
name|op
operator|=
name|op
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|callOrder
operator|.
name|add
argument_list|(
name|op
argument_list|)
expr_stmt|;
block|}
block|}
name|e
operator|.
name|execute
argument_list|(
operator|new
name|FakeOp
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|e
operator|.
name|execute
argument_list|(
operator|new
name|FakeOp
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|e
operator|.
name|execute
argument_list|(
operator|new
name|FakeOp
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|fakePool
operator|.
name|runAll
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|,
name|callOrder
argument_list|)
expr_stmt|;
block|}
DECL|method|testRuntimeException_doesNotStopExecution ()
specifier|public
name|void
name|testRuntimeException_doesNotStopExecution
parameter_list|()
block|{
specifier|final
name|AtomicInteger
name|numCalls
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|Runnable
name|runMe
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
name|numCalls
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"FAKE EXCEPTION!"
argument_list|)
throw|;
block|}
block|}
decl_stmt|;
name|e
operator|.
name|execute
argument_list|(
name|runMe
argument_list|)
expr_stmt|;
name|e
operator|.
name|execute
argument_list|(
name|runMe
argument_list|)
expr_stmt|;
name|fakePool
operator|.
name|runAll
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|numCalls
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testInterrupt_beforeRunRestoresInterruption ()
specifier|public
name|void
name|testInterrupt_beforeRunRestoresInterruption
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Run a task on the composed Executor that interrupts its thread (i.e. this thread).
name|fakePool
operator|.
name|execute
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
argument_list|)
expr_stmt|;
comment|// Run a task that expects that it is not interrupted while it is running.
name|e
operator|.
name|execute
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
name|assertThat
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|isInterrupted
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// Run these together.
name|fakePool
operator|.
name|runAll
argument_list|()
expr_stmt|;
comment|// Check that this thread has been marked as interrupted again now that the thread has been
comment|// returned by SequentialExecutor. Clear the bit while checking so that the test doesn't hose
comment|// JUnit or some other test case.
name|assertThat
argument_list|(
name|Thread
operator|.
name|interrupted
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
DECL|method|testInterrupt_doesNotInterruptSubsequentTask ()
specifier|public
name|void
name|testInterrupt_doesNotInterruptSubsequentTask
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Run a task that interrupts its thread (i.e. this thread).
name|e
operator|.
name|execute
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
argument_list|)
expr_stmt|;
comment|// Run a task that expects that it is not interrupted while it is running.
name|e
operator|.
name|execute
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
name|assertThat
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|isInterrupted
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// Run those tasks together.
name|fakePool
operator|.
name|runAll
argument_list|()
expr_stmt|;
comment|// Check that the interruption of a SequentialExecutor's task is restored to the thread once
comment|// it is yielded. Clear the bit while checking so that the test doesn't hose JUnit or some other
comment|// test case.
name|assertThat
argument_list|(
name|Thread
operator|.
name|interrupted
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
DECL|method|testInterrupt_doesNotStopExecution ()
specifier|public
name|void
name|testInterrupt_doesNotStopExecution
parameter_list|()
block|{
specifier|final
name|AtomicInteger
name|numCalls
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|Runnable
name|runMe
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
name|numCalls
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
name|e
operator|.
name|execute
argument_list|(
name|runMe
argument_list|)
expr_stmt|;
name|e
operator|.
name|execute
argument_list|(
name|runMe
argument_list|)
expr_stmt|;
name|fakePool
operator|.
name|runAll
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|numCalls
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Thread
operator|.
name|interrupted
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDelegateRejection ()
specifier|public
name|void
name|testDelegateRejection
parameter_list|()
block|{
specifier|final
name|AtomicInteger
name|numCalls
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
specifier|final
name|AtomicBoolean
name|reject
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|true
argument_list|)
decl_stmt|;
specifier|final
name|SequentialExecutor
name|executor
init|=
operator|new
name|SequentialExecutor
argument_list|(
operator|new
name|Executor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|r
parameter_list|)
block|{
if|if
condition|(
name|reject
operator|.
name|get
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RejectedExecutionException
argument_list|()
throw|;
block|}
name|r
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|Runnable
name|task
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
name|numCalls
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
try|try
block|{
name|executor
operator|.
name|execute
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
name|RejectedExecutionException
name|expected
parameter_list|)
block|{     }
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|numCalls
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|reject
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
name|task
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|numCalls
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTaskThrowsError ()
specifier|public
name|void
name|testTaskThrowsError
parameter_list|()
throws|throws
name|Exception
block|{
class|class
name|MyError
extends|extends
name|Error
block|{}
specifier|final
name|CyclicBarrier
name|barrier
init|=
operator|new
name|CyclicBarrier
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|// we need to make sure the error gets thrown on a different thread.
name|ExecutorService
name|service
init|=
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|SequentialExecutor
name|executor
init|=
operator|new
name|SequentialExecutor
argument_list|(
name|service
argument_list|)
decl_stmt|;
name|Runnable
name|errorTask
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
throw|throw
operator|new
name|MyError
argument_list|()
throw|;
block|}
block|}
decl_stmt|;
name|Runnable
name|barrierTask
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
decl_stmt|;
name|executor
operator|.
name|execute
argument_list|(
name|errorTask
argument_list|)
expr_stmt|;
name|service
operator|.
name|execute
argument_list|(
name|barrierTask
argument_list|)
expr_stmt|;
comment|// submit directly to the service
comment|// the barrier task runs after the error task so we know that the error has been observed by
comment|// SequentialExecutor by the time the barrier is satified
name|barrier
operator|.
name|await
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
name|barrierTask
argument_list|)
expr_stmt|;
comment|// timeout means the second task wasn't even tried
name|barrier
operator|.
name|await
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|service
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testRejectedExecutionThrownWithMultipleCalls ()
specifier|public
name|void
name|testRejectedExecutionThrownWithMultipleCalls
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|SettableFuture
argument_list|<
name|?
argument_list|>
name|future
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
specifier|final
name|Executor
name|delegate
init|=
operator|new
name|Executor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|task
parameter_list|)
block|{
if|if
condition|(
name|future
operator|.
name|set
argument_list|(
literal|null
argument_list|)
condition|)
block|{
name|awaitUninterruptibly
argument_list|(
name|latch
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|RejectedExecutionException
argument_list|()
throw|;
block|}
block|}
decl_stmt|;
specifier|final
name|SequentialExecutor
name|executor
init|=
operator|new
name|SequentialExecutor
argument_list|(
name|delegate
argument_list|)
decl_stmt|;
specifier|final
name|ExecutorService
name|blocked
init|=
name|Executors
operator|.
name|newCachedThreadPool
argument_list|()
decl_stmt|;
name|Future
argument_list|<
name|?
argument_list|>
name|first
init|=
name|blocked
operator|.
name|submit
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
name|executor
operator|.
name|execute
argument_list|(
name|Runnables
operator|.
name|doNothing
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|future
operator|.
name|get
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
try|try
block|{
name|executor
operator|.
name|execute
argument_list|(
name|Runnables
operator|.
name|doNothing
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RejectedExecutionException
name|expected
parameter_list|)
block|{     }
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
try|try
block|{
name|first
operator|.
name|get
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|expected
parameter_list|)
block|{
name|assertThat
argument_list|(
name|expected
argument_list|)
operator|.
name|hasCauseThat
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|RejectedExecutionException
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
specifier|final
name|Runnable
index|[]
name|currentTask
init|=
operator|new
name|Runnable
index|[
literal|1
index|]
decl_stmt|;
specifier|final
name|Executor
name|delegate
init|=
operator|new
name|Executor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|task
parameter_list|)
block|{
name|currentTask
index|[
literal|0
index|]
operator|=
name|task
expr_stmt|;
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
name|currentTask
index|[
literal|0
index|]
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"theDelegate"
return|;
block|}
block|}
decl_stmt|;
name|Executor
name|sequential1
init|=
name|newSequentialExecutor
argument_list|(
name|delegate
argument_list|)
decl_stmt|;
name|Executor
name|sequential2
init|=
name|newSequentialExecutor
argument_list|(
name|delegate
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|sequential1
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"theDelegate"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|sequential1
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|sequential2
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
index|[]
name|whileRunningToString
init|=
operator|new
name|String
index|[
literal|1
index|]
decl_stmt|;
name|sequential1
operator|.
name|execute
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
name|whileRunningToString
index|[
literal|0
index|]
operator|=
literal|""
operator|+
name|currentTask
index|[
literal|0
index|]
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"my runnable's toString"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|whileRunningToString
index|[
literal|0
index|]
argument_list|)
operator|.
name|contains
argument_list|(
literal|"my runnable's toString"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

