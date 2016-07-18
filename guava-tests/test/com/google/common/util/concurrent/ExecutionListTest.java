begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|concurrent
operator|.
name|MoreExecutors
operator|.
name|directExecutor
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
name|testing
operator|.
name|NullPointerTester
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
comment|/**  * Unit tests for {@link ExecutionList}.  *  * @author Nishant Thakkar  * @author Sven Mawson  */
end_comment

begin_class
DECL|class|ExecutionListTest
specifier|public
class|class
name|ExecutionListTest
extends|extends
name|TestCase
block|{
DECL|field|list
specifier|private
specifier|final
name|ExecutionList
name|list
init|=
operator|new
name|ExecutionList
argument_list|()
decl_stmt|;
DECL|method|testRunOnPopulatedList ()
specifier|public
name|void
name|testRunOnPopulatedList
parameter_list|()
throws|throws
name|Exception
block|{
name|Executor
name|exec
init|=
name|Executors
operator|.
name|newCachedThreadPool
argument_list|()
decl_stmt|;
name|CountDownLatch
name|countDownLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|MockRunnable
argument_list|(
name|countDownLatch
argument_list|)
argument_list|,
name|exec
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|MockRunnable
argument_list|(
name|countDownLatch
argument_list|)
argument_list|,
name|exec
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|MockRunnable
argument_list|(
name|countDownLatch
argument_list|)
argument_list|,
name|exec
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|countDownLatch
operator|.
name|getCount
argument_list|()
argument_list|,
literal|3L
argument_list|)
expr_stmt|;
name|list
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// Verify that all of the runnables execute in a reasonable amount of time.
name|assertTrue
argument_list|(
name|countDownLatch
operator|.
name|await
argument_list|(
literal|1L
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testExecute_idempotent ()
specifier|public
name|void
name|testExecute_idempotent
parameter_list|()
block|{
specifier|final
name|AtomicInteger
name|runCalled
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
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
name|runCalled
operator|.
name|getAndIncrement
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
name|list
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|runCalled
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|list
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|runCalled
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testExecute_idempotentConcurrently ()
specifier|public
name|void
name|testExecute_idempotentConcurrently
parameter_list|()
throws|throws
name|InterruptedException
block|{
specifier|final
name|CountDownLatch
name|okayToRun
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|AtomicInteger
name|runCalled
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
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
name|okayToRun
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|runCalled
operator|.
name|getAndIncrement
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
name|Runnable
name|execute
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
name|list
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
name|Thread
name|thread1
init|=
operator|new
name|Thread
argument_list|(
name|execute
argument_list|)
decl_stmt|;
name|Thread
name|thread2
init|=
operator|new
name|Thread
argument_list|(
name|execute
argument_list|)
decl_stmt|;
name|thread1
operator|.
name|start
argument_list|()
expr_stmt|;
name|thread2
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|runCalled
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|okayToRun
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|thread1
operator|.
name|join
argument_list|()
expr_stmt|;
name|thread2
operator|.
name|join
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|runCalled
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAddAfterRun ()
specifier|public
name|void
name|testAddAfterRun
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Run the previous test
name|testRunOnPopulatedList
argument_list|()
expr_stmt|;
comment|// If it passed, then verify an Add will be executed without calling run
name|CountDownLatch
name|countDownLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|MockRunnable
argument_list|(
name|countDownLatch
argument_list|)
argument_list|,
name|Executors
operator|.
name|newCachedThreadPool
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|countDownLatch
operator|.
name|await
argument_list|(
literal|1L
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testOrdering ()
specifier|public
name|void
name|testOrdering
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|AtomicInteger
name|integer
init|=
operator|new
name|AtomicInteger
argument_list|()
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
literal|10
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|expectedCount
init|=
name|i
decl_stmt|;
name|list
operator|.
name|add
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
name|integer
operator|.
name|compareAndSet
argument_list|(
name|expectedCount
argument_list|,
name|expectedCount
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
name|MoreExecutors
operator|.
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|list
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|integer
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MockRunnable
specifier|private
class|class
name|MockRunnable
implements|implements
name|Runnable
block|{
DECL|field|countDownLatch
name|CountDownLatch
name|countDownLatch
decl_stmt|;
DECL|method|MockRunnable (CountDownLatch countDownLatch)
name|MockRunnable
parameter_list|(
name|CountDownLatch
name|countDownLatch
parameter_list|)
block|{
name|this
operator|.
name|countDownLatch
operator|=
name|countDownLatch
expr_stmt|;
block|}
DECL|method|run ()
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|countDownLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testExceptionsCaught ()
specifier|public
name|void
name|testExceptionsCaught
parameter_list|()
block|{
name|list
operator|.
name|add
argument_list|(
name|THROWING_RUNNABLE
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
name|list
operator|.
name|execute
argument_list|()
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|THROWING_RUNNABLE
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testNulls ()
specifier|public
name|void
name|testNulls
parameter_list|()
block|{
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|testAllPublicInstanceMethods
argument_list|(
operator|new
name|ExecutionList
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|field|THROWING_RUNNABLE
specifier|private
specifier|static
specifier|final
name|Runnable
name|THROWING_RUNNABLE
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
name|RuntimeException
argument_list|()
throw|;
block|}
block|}
decl_stmt|;
block|}
end_class

end_unit

