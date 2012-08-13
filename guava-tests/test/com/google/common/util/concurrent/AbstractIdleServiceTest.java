begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|junit
operator|.
name|contrib
operator|.
name|truth
operator|.
name|Truth
operator|.
name|ASSERT
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
name|List
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
comment|/**  * Tests for {@link AbstractIdleService}.  *  * @author Chris Nokleberg  * @author Ben Yu  */
end_comment

begin_class
DECL|class|AbstractIdleServiceTest
specifier|public
class|class
name|AbstractIdleServiceTest
extends|extends
name|TestCase
block|{
comment|// Functional tests using real thread. We only verify publicly visible state.
comment|// Interaction assertions are done by the single-threaded unit tests.
DECL|class|FunctionalTest
specifier|public
specifier|static
class|class
name|FunctionalTest
extends|extends
name|TestCase
block|{
DECL|class|DefaultService
specifier|private
specifier|static
class|class
name|DefaultService
extends|extends
name|AbstractIdleService
block|{
DECL|method|startUp ()
annotation|@
name|Override
specifier|protected
name|void
name|startUp
parameter_list|()
throws|throws
name|Exception
block|{}
DECL|method|shutDown ()
annotation|@
name|Override
specifier|protected
name|void
name|shutDown
parameter_list|()
throws|throws
name|Exception
block|{}
block|}
DECL|method|testServiceStartStop ()
specifier|public
name|void
name|testServiceStartStop
parameter_list|()
throws|throws
name|Exception
block|{
name|AbstractIdleService
name|service
init|=
operator|new
name|DefaultService
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Service
operator|.
name|State
operator|.
name|RUNNING
argument_list|,
name|service
operator|.
name|startAndWait
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Service
operator|.
name|State
operator|.
name|RUNNING
argument_list|,
name|service
operator|.
name|state
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Service
operator|.
name|State
operator|.
name|TERMINATED
argument_list|,
name|service
operator|.
name|stopAndWait
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Service
operator|.
name|State
operator|.
name|TERMINATED
argument_list|,
name|service
operator|.
name|state
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testStart_failed ()
specifier|public
name|void
name|testStart_failed
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Exception
name|exception
init|=
operator|new
name|Exception
argument_list|(
literal|"deliberate"
argument_list|)
decl_stmt|;
name|AbstractIdleService
name|service
init|=
operator|new
name|DefaultService
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|startUp
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
name|exception
throw|;
block|}
block|}
decl_stmt|;
try|try
block|{
name|service
operator|.
name|startAndWait
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|assertSame
argument_list|(
name|exception
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|Service
operator|.
name|State
operator|.
name|FAILED
argument_list|,
name|service
operator|.
name|state
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testStop_failed ()
specifier|public
name|void
name|testStop_failed
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Exception
name|exception
init|=
operator|new
name|Exception
argument_list|(
literal|"deliberate"
argument_list|)
decl_stmt|;
name|AbstractIdleService
name|service
init|=
operator|new
name|DefaultService
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|shutDown
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
name|exception
throw|;
block|}
block|}
decl_stmt|;
name|service
operator|.
name|startAndWait
argument_list|()
expr_stmt|;
try|try
block|{
name|service
operator|.
name|stopAndWait
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|assertSame
argument_list|(
name|exception
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|Service
operator|.
name|State
operator|.
name|FAILED
argument_list|,
name|service
operator|.
name|state
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testStart ()
specifier|public
name|void
name|testStart
parameter_list|()
block|{
name|TestService
name|service
init|=
operator|new
name|TestService
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|service
operator|.
name|startUpCalled
argument_list|)
expr_stmt|;
name|service
operator|.
name|startAndWait
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|service
operator|.
name|startUpCalled
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Service
operator|.
name|State
operator|.
name|RUNNING
argument_list|,
name|service
operator|.
name|state
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|service
operator|.
name|transitionStates
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|Service
operator|.
name|State
operator|.
name|STARTING
argument_list|)
expr_stmt|;
block|}
DECL|method|testStart_failed ()
specifier|public
name|void
name|testStart_failed
parameter_list|()
block|{
specifier|final
name|Exception
name|exception
init|=
operator|new
name|Exception
argument_list|(
literal|"deliberate"
argument_list|)
decl_stmt|;
name|TestService
name|service
init|=
operator|new
name|TestService
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|startUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|startUp
argument_list|()
expr_stmt|;
throw|throw
name|exception
throw|;
block|}
block|}
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|service
operator|.
name|startUpCalled
argument_list|)
expr_stmt|;
try|try
block|{
name|service
operator|.
name|startAndWait
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|assertSame
argument_list|(
name|exception
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|service
operator|.
name|startUpCalled
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Service
operator|.
name|State
operator|.
name|FAILED
argument_list|,
name|service
operator|.
name|state
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|service
operator|.
name|transitionStates
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|Service
operator|.
name|State
operator|.
name|STARTING
argument_list|)
expr_stmt|;
block|}
DECL|method|testStop_withoutStart ()
specifier|public
name|void
name|testStop_withoutStart
parameter_list|()
block|{
name|TestService
name|service
init|=
operator|new
name|TestService
argument_list|()
decl_stmt|;
name|service
operator|.
name|stopAndWait
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|service
operator|.
name|startUpCalled
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|service
operator|.
name|shutDownCalled
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Service
operator|.
name|State
operator|.
name|TERMINATED
argument_list|,
name|service
operator|.
name|state
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|service
operator|.
name|transitionStates
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testStop_afterStart ()
specifier|public
name|void
name|testStop_afterStart
parameter_list|()
block|{
name|TestService
name|service
init|=
operator|new
name|TestService
argument_list|()
decl_stmt|;
name|service
operator|.
name|startAndWait
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|service
operator|.
name|startUpCalled
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|service
operator|.
name|shutDownCalled
argument_list|)
expr_stmt|;
name|service
operator|.
name|stopAndWait
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|service
operator|.
name|startUpCalled
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|service
operator|.
name|shutDownCalled
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Service
operator|.
name|State
operator|.
name|TERMINATED
argument_list|,
name|service
operator|.
name|state
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|service
operator|.
name|transitionStates
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|Service
operator|.
name|State
operator|.
name|STARTING
argument_list|,
name|Service
operator|.
name|State
operator|.
name|STOPPING
argument_list|)
expr_stmt|;
block|}
DECL|method|testStop_failed ()
specifier|public
name|void
name|testStop_failed
parameter_list|()
block|{
specifier|final
name|Exception
name|exception
init|=
operator|new
name|Exception
argument_list|(
literal|"deliberate"
argument_list|)
decl_stmt|;
name|TestService
name|service
init|=
operator|new
name|TestService
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|shutDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|shutDown
argument_list|()
expr_stmt|;
throw|throw
name|exception
throw|;
block|}
block|}
decl_stmt|;
name|service
operator|.
name|startAndWait
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|service
operator|.
name|startUpCalled
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|service
operator|.
name|shutDownCalled
argument_list|)
expr_stmt|;
try|try
block|{
name|service
operator|.
name|stopAndWait
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|assertSame
argument_list|(
name|exception
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|service
operator|.
name|startUpCalled
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|service
operator|.
name|shutDownCalled
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Service
operator|.
name|State
operator|.
name|FAILED
argument_list|,
name|service
operator|.
name|state
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|service
operator|.
name|transitionStates
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|Service
operator|.
name|State
operator|.
name|STARTING
argument_list|,
name|Service
operator|.
name|State
operator|.
name|STOPPING
argument_list|)
expr_stmt|;
block|}
DECL|method|testServiceToString ()
specifier|public
name|void
name|testServiceToString
parameter_list|()
block|{
name|AbstractIdleService
name|service
init|=
operator|new
name|TestService
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"TestService [NEW]"
argument_list|,
name|service
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|startAndWait
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TestService [RUNNING]"
argument_list|,
name|service
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|stopAndWait
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TestService [TERMINATED]"
argument_list|,
name|service
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTimeout ()
specifier|public
name|void
name|testTimeout
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create a service whose executor will never run its commands
name|Service
name|service
init|=
operator|new
name|TestService
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Executor
name|executor
parameter_list|(
name|Service
operator|.
name|State
name|state
parameter_list|)
block|{
return|return
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
name|command
parameter_list|)
block|{}
block|}
return|;
block|}
block|}
decl_stmt|;
try|try
block|{
name|service
operator|.
name|start
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected timeout"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
name|ASSERT
operator|.
name|that
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|Service
operator|.
name|State
operator|.
name|STARTING
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|TestService
specifier|private
specifier|static
class|class
name|TestService
extends|extends
name|AbstractIdleService
block|{
DECL|field|startUpCalled
name|int
name|startUpCalled
init|=
literal|0
decl_stmt|;
DECL|field|shutDownCalled
name|int
name|shutDownCalled
init|=
literal|0
decl_stmt|;
DECL|field|transitionStates
specifier|final
name|List
argument_list|<
name|State
argument_list|>
name|transitionStates
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|method|startUp ()
annotation|@
name|Override
specifier|protected
name|void
name|startUp
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|startUpCalled
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|shutDownCalled
argument_list|)
expr_stmt|;
name|startUpCalled
operator|++
expr_stmt|;
name|assertEquals
argument_list|(
name|State
operator|.
name|STARTING
argument_list|,
name|state
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|shutDown ()
annotation|@
name|Override
specifier|protected
name|void
name|shutDown
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|startUpCalled
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|shutDownCalled
argument_list|)
expr_stmt|;
name|shutDownCalled
operator|++
expr_stmt|;
name|assertEquals
argument_list|(
name|State
operator|.
name|STOPPING
argument_list|,
name|state
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|executor (Service.State state)
annotation|@
name|Override
specifier|protected
name|Executor
name|executor
parameter_list|(
name|Service
operator|.
name|State
name|state
parameter_list|)
block|{
name|transitionStates
operator|.
name|add
argument_list|(
name|state
argument_list|)
expr_stmt|;
return|return
name|MoreExecutors
operator|.
name|sameThreadExecutor
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

