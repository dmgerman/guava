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
name|util
operator|.
name|concurrent
operator|.
name|GeneratedMonitorTest
operator|.
name|startThread
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
name|joinUninterruptibly
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
name|GeneratedMonitorTest
operator|.
name|FlagGuard
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReference
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
comment|/**  * Supplemental tests for {@link Monitor}.  *  *<p>This test class contains various test cases that don't fit into the test case generation in  * {@link GeneratedMonitorTest}.  *  * @author Justin T. Sampson  */
end_comment

begin_class
DECL|class|SupplementalMonitorTest
specifier|public
class|class
name|SupplementalMonitorTest
extends|extends
name|TestCase
block|{
DECL|method|testLeaveWithoutEnterThrowsIMSE ()
specifier|public
name|void
name|testLeaveWithoutEnterThrowsIMSE
parameter_list|()
block|{
name|Monitor
name|monitor
init|=
operator|new
name|Monitor
argument_list|()
decl_stmt|;
try|try
block|{
name|monitor
operator|.
name|leave
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"expected IllegalMonitorStateException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalMonitorStateException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testGetWaitQueueLengthWithWrongMonitorThrowsIMSE ()
specifier|public
name|void
name|testGetWaitQueueLengthWithWrongMonitorThrowsIMSE
parameter_list|()
block|{
name|Monitor
name|monitor1
init|=
operator|new
name|Monitor
argument_list|()
decl_stmt|;
name|Monitor
name|monitor2
init|=
operator|new
name|Monitor
argument_list|()
decl_stmt|;
name|FlagGuard
name|guard
init|=
operator|new
name|FlagGuard
argument_list|(
name|monitor2
argument_list|)
decl_stmt|;
try|try
block|{
name|monitor1
operator|.
name|getWaitQueueLength
argument_list|(
name|guard
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected IllegalMonitorStateException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalMonitorStateException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testHasWaitersWithWrongMonitorThrowsIMSE ()
specifier|public
name|void
name|testHasWaitersWithWrongMonitorThrowsIMSE
parameter_list|()
block|{
name|Monitor
name|monitor1
init|=
operator|new
name|Monitor
argument_list|()
decl_stmt|;
name|Monitor
name|monitor2
init|=
operator|new
name|Monitor
argument_list|()
decl_stmt|;
name|FlagGuard
name|guard
init|=
operator|new
name|FlagGuard
argument_list|(
name|monitor2
argument_list|)
decl_stmt|;
try|try
block|{
name|monitor1
operator|.
name|hasWaiters
argument_list|(
name|guard
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected IllegalMonitorStateException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalMonitorStateException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testNullMonitorInGuardConstructorThrowsNPE ()
specifier|public
name|void
name|testNullMonitorInGuardConstructorThrowsNPE
parameter_list|()
block|{
try|try
block|{
operator|new
name|FlagGuard
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected NullPointerException"
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
DECL|method|testIsFair ()
specifier|public
name|void
name|testIsFair
parameter_list|()
block|{
name|assertTrue
argument_list|(
operator|new
name|Monitor
argument_list|(
literal|true
argument_list|)
operator|.
name|isFair
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|Monitor
argument_list|(
literal|false
argument_list|)
operator|.
name|isFair
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOccupiedMethods ()
specifier|public
name|void
name|testOccupiedMethods
parameter_list|()
block|{
name|Monitor
name|monitor
init|=
operator|new
name|Monitor
argument_list|()
decl_stmt|;
name|verifyOccupiedMethodsInCurrentThread
argument_list|(
name|monitor
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|verifyOccupiedMethodsInAnotherThread
argument_list|(
name|monitor
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|monitor
operator|.
name|enter
argument_list|()
expr_stmt|;
try|try
block|{
name|verifyOccupiedMethodsInCurrentThread
argument_list|(
name|monitor
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|verifyOccupiedMethodsInAnotherThread
argument_list|(
name|monitor
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|monitor
operator|.
name|enter
argument_list|()
expr_stmt|;
try|try
block|{
name|verifyOccupiedMethodsInCurrentThread
argument_list|(
name|monitor
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|verifyOccupiedMethodsInAnotherThread
argument_list|(
name|monitor
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|monitor
operator|.
name|leave
argument_list|()
expr_stmt|;
block|}
name|verifyOccupiedMethodsInCurrentThread
argument_list|(
name|monitor
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|verifyOccupiedMethodsInAnotherThread
argument_list|(
name|monitor
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|monitor
operator|.
name|leave
argument_list|()
expr_stmt|;
block|}
name|verifyOccupiedMethodsInCurrentThread
argument_list|(
name|monitor
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|verifyOccupiedMethodsInAnotherThread
argument_list|(
name|monitor
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|verifyOccupiedMethodsInCurrentThread (Monitor monitor, boolean expectedIsOccupied, boolean expectedIsOccupiedByCurrentThread, int expectedOccupiedDepth)
specifier|private
specifier|static
name|void
name|verifyOccupiedMethodsInCurrentThread
parameter_list|(
name|Monitor
name|monitor
parameter_list|,
name|boolean
name|expectedIsOccupied
parameter_list|,
name|boolean
name|expectedIsOccupiedByCurrentThread
parameter_list|,
name|int
name|expectedOccupiedDepth
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|expectedIsOccupied
argument_list|,
name|monitor
operator|.
name|isOccupied
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedIsOccupiedByCurrentThread
argument_list|,
name|monitor
operator|.
name|isOccupiedByCurrentThread
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedOccupiedDepth
argument_list|,
name|monitor
operator|.
name|getOccupiedDepth
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|verifyOccupiedMethodsInAnotherThread (final Monitor monitor, boolean expectedIsOccupied, boolean expectedIsOccupiedByCurrentThread, int expectedOccupiedDepth)
specifier|private
specifier|static
name|void
name|verifyOccupiedMethodsInAnotherThread
parameter_list|(
specifier|final
name|Monitor
name|monitor
parameter_list|,
name|boolean
name|expectedIsOccupied
parameter_list|,
name|boolean
name|expectedIsOccupiedByCurrentThread
parameter_list|,
name|int
name|expectedOccupiedDepth
parameter_list|)
block|{
specifier|final
name|AtomicBoolean
name|actualIsOccupied
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
specifier|final
name|AtomicBoolean
name|actualIsOccupiedByCurrentThread
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
specifier|final
name|AtomicInteger
name|actualOccupiedDepth
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
specifier|final
name|AtomicReference
argument_list|<
name|Throwable
argument_list|>
name|thrown
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|()
decl_stmt|;
name|joinUninterruptibly
argument_list|(
name|startThread
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
name|actualIsOccupied
operator|.
name|set
argument_list|(
name|monitor
operator|.
name|isOccupied
argument_list|()
argument_list|)
expr_stmt|;
name|actualIsOccupiedByCurrentThread
operator|.
name|set
argument_list|(
name|monitor
operator|.
name|isOccupiedByCurrentThread
argument_list|()
argument_list|)
expr_stmt|;
name|actualOccupiedDepth
operator|.
name|set
argument_list|(
name|monitor
operator|.
name|getOccupiedDepth
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|thrown
operator|.
name|set
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|thrown
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedIsOccupied
argument_list|,
name|actualIsOccupied
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedIsOccupiedByCurrentThread
argument_list|,
name|actualIsOccupiedByCurrentThread
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedOccupiedDepth
argument_list|,
name|actualOccupiedDepth
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

