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
name|sameThreadExecutor
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
name|ExecutionList
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
specifier|protected
name|ExecutionList
name|list
init|=
operator|new
name|ExecutionList
argument_list|()
decl_stmt|;
DECL|field|exec
specifier|protected
name|Executor
name|exec
init|=
name|Executors
operator|.
name|newCachedThreadPool
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
name|exec
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
annotation|@
name|Override
DECL|method|run ()
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
name|ExecutionList
name|list
init|=
operator|new
name|ExecutionList
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|THROWING_RUNNABLE
argument_list|,
name|sameThreadExecutor
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
name|sameThreadExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testNulls ()
specifier|public
name|void
name|testNulls
parameter_list|()
throws|throws
name|Exception
block|{
name|NullPointerTester
name|tester
init|=
operator|new
name|NullPointerTester
argument_list|()
decl_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|Executor
operator|.
name|class
argument_list|,
name|sameThreadExecutor
argument_list|()
argument_list|)
expr_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|Runnable
operator|.
name|class
argument_list|,
name|DO_NOTHING
argument_list|)
expr_stmt|;
name|tester
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
DECL|field|DO_NOTHING
specifier|private
specifier|static
specifier|final
name|Runnable
name|DO_NOTHING
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
block|{     }
block|}
decl_stmt|;
block|}
end_class

end_unit

