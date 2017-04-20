begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|testing
operator|.
name|TearDownStack
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Tests for {@link Monitor}, either interruptible or uninterruptible.  *  * @author Justin T. Sampson  */
end_comment

begin_class
DECL|class|MonitorTestCase
specifier|public
specifier|abstract
class|class
name|MonitorTestCase
extends|extends
name|TestCase
block|{
DECL|class|TestGuard
specifier|public
class|class
name|TestGuard
extends|extends
name|Monitor
operator|.
name|Guard
block|{
DECL|field|satisfied
specifier|private
specifier|volatile
name|boolean
name|satisfied
decl_stmt|;
DECL|method|TestGuard (boolean satisfied)
specifier|public
name|TestGuard
parameter_list|(
name|boolean
name|satisfied
parameter_list|)
block|{
name|super
argument_list|(
name|MonitorTestCase
operator|.
name|this
operator|.
name|monitor
argument_list|)
expr_stmt|;
name|this
operator|.
name|satisfied
operator|=
name|satisfied
expr_stmt|;
block|}
DECL|method|isSatisfied ()
annotation|@
name|Override
specifier|public
name|boolean
name|isSatisfied
parameter_list|()
block|{
return|return
name|this
operator|.
name|satisfied
return|;
block|}
DECL|method|setSatisfied (boolean satisfied)
specifier|public
name|void
name|setSatisfied
parameter_list|(
name|boolean
name|satisfied
parameter_list|)
block|{
name|this
operator|.
name|satisfied
operator|=
name|satisfied
expr_stmt|;
block|}
block|}
DECL|field|interruptible
specifier|private
specifier|final
name|boolean
name|interruptible
decl_stmt|;
DECL|field|monitor
specifier|private
name|Monitor
name|monitor
decl_stmt|;
DECL|field|tearDownStack
specifier|private
specifier|final
name|TearDownStack
name|tearDownStack
init|=
operator|new
name|TearDownStack
argument_list|(
literal|true
argument_list|)
decl_stmt|;
DECL|field|thread1
specifier|private
name|TestThread
argument_list|<
name|Monitor
argument_list|>
name|thread1
decl_stmt|;
DECL|field|thread2
specifier|private
name|TestThread
argument_list|<
name|Monitor
argument_list|>
name|thread2
decl_stmt|;
DECL|method|MonitorTestCase (boolean interruptible)
specifier|protected
name|MonitorTestCase
parameter_list|(
name|boolean
name|interruptible
parameter_list|)
block|{
name|this
operator|.
name|interruptible
operator|=
name|interruptible
expr_stmt|;
block|}
DECL|method|setUp ()
annotation|@
name|Override
specifier|protected
specifier|final
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|fair
init|=
operator|new
name|Random
argument_list|()
operator|.
name|nextBoolean
argument_list|()
decl_stmt|;
name|monitor
operator|=
operator|new
name|Monitor
argument_list|(
name|fair
argument_list|)
expr_stmt|;
name|tearDownStack
operator|.
name|addTearDown
argument_list|(
name|thread1
operator|=
operator|new
name|TestThread
argument_list|<
name|Monitor
argument_list|>
argument_list|(
name|monitor
argument_list|,
literal|"TestThread #1"
argument_list|)
argument_list|)
expr_stmt|;
name|tearDownStack
operator|.
name|addTearDown
argument_list|(
name|thread2
operator|=
operator|new
name|TestThread
argument_list|<
name|Monitor
argument_list|>
argument_list|(
name|monitor
argument_list|,
literal|"TestThread #2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|tearDown ()
annotation|@
name|Override
specifier|protected
specifier|final
name|void
name|tearDown
parameter_list|()
block|{
name|tearDownStack
operator|.
name|runTearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|enter ()
specifier|private
name|String
name|enter
parameter_list|()
block|{
return|return
name|interruptible
condition|?
literal|"enterInterruptibly"
else|:
literal|"enter"
return|;
block|}
DECL|method|tryEnter ()
specifier|private
name|String
name|tryEnter
parameter_list|()
block|{
return|return
literal|"tryEnter"
return|;
block|}
DECL|method|enterIf ()
specifier|private
name|String
name|enterIf
parameter_list|()
block|{
return|return
name|interruptible
condition|?
literal|"enterIfInterruptibly"
else|:
literal|"enterIf"
return|;
block|}
DECL|method|tryEnterIf ()
specifier|private
name|String
name|tryEnterIf
parameter_list|()
block|{
return|return
literal|"tryEnterIf"
return|;
block|}
DECL|method|enterWhen ()
specifier|private
name|String
name|enterWhen
parameter_list|()
block|{
return|return
name|interruptible
condition|?
literal|"enterWhen"
else|:
literal|"enterWhenUninterruptibly"
return|;
block|}
DECL|method|waitFor ()
specifier|private
name|String
name|waitFor
parameter_list|()
block|{
return|return
name|interruptible
condition|?
literal|"waitFor"
else|:
literal|"waitForUninterruptibly"
return|;
block|}
DECL|method|leave ()
specifier|private
name|String
name|leave
parameter_list|()
block|{
return|return
literal|"leave"
return|;
block|}
DECL|method|testMutualExclusion ()
specifier|public
specifier|final
name|void
name|testMutualExclusion
parameter_list|()
throws|throws
name|Exception
block|{
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
name|enter
argument_list|()
argument_list|)
expr_stmt|;
name|thread2
operator|.
name|callAndAssertBlocks
argument_list|(
name|enter
argument_list|()
argument_list|)
expr_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
name|leave
argument_list|()
argument_list|)
expr_stmt|;
name|thread2
operator|.
name|assertPriorCallReturns
argument_list|(
name|enter
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTryEnter ()
specifier|public
specifier|final
name|void
name|testTryEnter
parameter_list|()
throws|throws
name|Exception
block|{
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
literal|true
argument_list|,
name|tryEnter
argument_list|()
argument_list|)
expr_stmt|;
name|thread2
operator|.
name|callAndAssertReturns
argument_list|(
literal|false
argument_list|,
name|tryEnter
argument_list|()
argument_list|)
expr_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
literal|true
argument_list|,
name|tryEnter
argument_list|()
argument_list|)
expr_stmt|;
name|thread2
operator|.
name|callAndAssertReturns
argument_list|(
literal|false
argument_list|,
name|tryEnter
argument_list|()
argument_list|)
expr_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
name|leave
argument_list|()
argument_list|)
expr_stmt|;
name|thread2
operator|.
name|callAndAssertReturns
argument_list|(
literal|false
argument_list|,
name|tryEnter
argument_list|()
argument_list|)
expr_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
name|leave
argument_list|()
argument_list|)
expr_stmt|;
name|thread2
operator|.
name|callAndAssertReturns
argument_list|(
literal|true
argument_list|,
name|tryEnter
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSystemStateMethods ()
specifier|public
specifier|final
name|void
name|testSystemStateMethods
parameter_list|()
throws|throws
name|Exception
block|{
name|checkSystemStateMethods
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
name|enter
argument_list|()
argument_list|)
expr_stmt|;
name|checkSystemStateMethods
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
name|enter
argument_list|()
argument_list|)
expr_stmt|;
name|checkSystemStateMethods
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
name|leave
argument_list|()
argument_list|)
expr_stmt|;
name|checkSystemStateMethods
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
name|leave
argument_list|()
argument_list|)
expr_stmt|;
name|checkSystemStateMethods
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|checkSystemStateMethods (int enterCount)
specifier|private
name|void
name|checkSystemStateMethods
parameter_list|(
name|int
name|enterCount
parameter_list|)
throws|throws
name|Exception
block|{
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
name|enterCount
operator|!=
literal|0
argument_list|,
literal|"isOccupied"
argument_list|)
expr_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
name|enterCount
operator|!=
literal|0
argument_list|,
literal|"isOccupiedByCurrentThread"
argument_list|)
expr_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
name|enterCount
argument_list|,
literal|"getOccupiedDepth"
argument_list|)
expr_stmt|;
name|thread2
operator|.
name|callAndAssertReturns
argument_list|(
name|enterCount
operator|!=
literal|0
argument_list|,
literal|"isOccupied"
argument_list|)
expr_stmt|;
name|thread2
operator|.
name|callAndAssertReturns
argument_list|(
literal|false
argument_list|,
literal|"isOccupiedByCurrentThread"
argument_list|)
expr_stmt|;
name|thread2
operator|.
name|callAndAssertReturns
argument_list|(
literal|0
argument_list|,
literal|"getOccupiedDepth"
argument_list|)
expr_stmt|;
block|}
DECL|method|testEnterWhen_initiallyTrue ()
specifier|public
specifier|final
name|void
name|testEnterWhen_initiallyTrue
parameter_list|()
throws|throws
name|Exception
block|{
name|TestGuard
name|guard
init|=
operator|new
name|TestGuard
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
name|enterWhen
argument_list|()
argument_list|,
name|guard
argument_list|)
expr_stmt|;
block|}
DECL|method|testEnterWhen_initiallyFalse ()
specifier|public
specifier|final
name|void
name|testEnterWhen_initiallyFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|TestGuard
name|guard
init|=
operator|new
name|TestGuard
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|thread1
operator|.
name|callAndAssertWaits
argument_list|(
name|enterWhen
argument_list|()
argument_list|,
name|guard
argument_list|)
expr_stmt|;
name|monitor
operator|.
name|enter
argument_list|()
expr_stmt|;
name|guard
operator|.
name|setSatisfied
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|monitor
operator|.
name|leave
argument_list|()
expr_stmt|;
name|thread1
operator|.
name|assertPriorCallReturns
argument_list|(
name|enterWhen
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEnterWhen_alreadyOccupied ()
specifier|public
specifier|final
name|void
name|testEnterWhen_alreadyOccupied
parameter_list|()
throws|throws
name|Exception
block|{
name|TestGuard
name|guard
init|=
operator|new
name|TestGuard
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|thread2
operator|.
name|callAndAssertReturns
argument_list|(
name|enter
argument_list|()
argument_list|)
expr_stmt|;
name|thread1
operator|.
name|callAndAssertBlocks
argument_list|(
name|enterWhen
argument_list|()
argument_list|,
name|guard
argument_list|)
expr_stmt|;
name|thread2
operator|.
name|callAndAssertReturns
argument_list|(
name|leave
argument_list|()
argument_list|)
expr_stmt|;
name|thread1
operator|.
name|assertPriorCallReturns
argument_list|(
name|enterWhen
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEnterIf_initiallyTrue ()
specifier|public
specifier|final
name|void
name|testEnterIf_initiallyTrue
parameter_list|()
throws|throws
name|Exception
block|{
name|TestGuard
name|guard
init|=
operator|new
name|TestGuard
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
literal|true
argument_list|,
name|enterIf
argument_list|()
argument_list|,
name|guard
argument_list|)
expr_stmt|;
name|thread2
operator|.
name|callAndAssertBlocks
argument_list|(
name|enter
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEnterIf_initiallyFalse ()
specifier|public
specifier|final
name|void
name|testEnterIf_initiallyFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|TestGuard
name|guard
init|=
operator|new
name|TestGuard
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
literal|false
argument_list|,
name|enterIf
argument_list|()
argument_list|,
name|guard
argument_list|)
expr_stmt|;
name|thread2
operator|.
name|callAndAssertReturns
argument_list|(
name|enter
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEnterIf_alreadyOccupied ()
specifier|public
specifier|final
name|void
name|testEnterIf_alreadyOccupied
parameter_list|()
throws|throws
name|Exception
block|{
name|TestGuard
name|guard
init|=
operator|new
name|TestGuard
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|thread2
operator|.
name|callAndAssertReturns
argument_list|(
name|enter
argument_list|()
argument_list|)
expr_stmt|;
name|thread1
operator|.
name|callAndAssertBlocks
argument_list|(
name|enterIf
argument_list|()
argument_list|,
name|guard
argument_list|)
expr_stmt|;
name|thread2
operator|.
name|callAndAssertReturns
argument_list|(
name|leave
argument_list|()
argument_list|)
expr_stmt|;
name|thread1
operator|.
name|assertPriorCallReturns
argument_list|(
literal|true
argument_list|,
name|enterIf
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTryEnterIf_initiallyTrue ()
specifier|public
specifier|final
name|void
name|testTryEnterIf_initiallyTrue
parameter_list|()
throws|throws
name|Exception
block|{
name|TestGuard
name|guard
init|=
operator|new
name|TestGuard
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
literal|true
argument_list|,
name|tryEnterIf
argument_list|()
argument_list|,
name|guard
argument_list|)
expr_stmt|;
name|thread2
operator|.
name|callAndAssertBlocks
argument_list|(
name|enter
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTryEnterIf_initiallyFalse ()
specifier|public
specifier|final
name|void
name|testTryEnterIf_initiallyFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|TestGuard
name|guard
init|=
operator|new
name|TestGuard
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
literal|false
argument_list|,
name|tryEnterIf
argument_list|()
argument_list|,
name|guard
argument_list|)
expr_stmt|;
name|thread2
operator|.
name|callAndAssertReturns
argument_list|(
name|enter
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTryEnterIf_alreadyOccupied ()
specifier|public
specifier|final
name|void
name|testTryEnterIf_alreadyOccupied
parameter_list|()
throws|throws
name|Exception
block|{
name|TestGuard
name|guard
init|=
operator|new
name|TestGuard
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|thread2
operator|.
name|callAndAssertReturns
argument_list|(
name|enter
argument_list|()
argument_list|)
expr_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
literal|false
argument_list|,
name|tryEnterIf
argument_list|()
argument_list|,
name|guard
argument_list|)
expr_stmt|;
block|}
DECL|method|testWaitFor_initiallyTrue ()
specifier|public
specifier|final
name|void
name|testWaitFor_initiallyTrue
parameter_list|()
throws|throws
name|Exception
block|{
name|TestGuard
name|guard
init|=
operator|new
name|TestGuard
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
name|enter
argument_list|()
argument_list|)
expr_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
name|waitFor
argument_list|()
argument_list|,
name|guard
argument_list|)
expr_stmt|;
block|}
DECL|method|testWaitFor_initiallyFalse ()
specifier|public
specifier|final
name|void
name|testWaitFor_initiallyFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|TestGuard
name|guard
init|=
operator|new
name|TestGuard
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|thread1
operator|.
name|callAndAssertReturns
argument_list|(
name|enter
argument_list|()
argument_list|)
expr_stmt|;
name|thread1
operator|.
name|callAndAssertWaits
argument_list|(
name|waitFor
argument_list|()
argument_list|,
name|guard
argument_list|)
expr_stmt|;
name|monitor
operator|.
name|enter
argument_list|()
expr_stmt|;
name|guard
operator|.
name|setSatisfied
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|monitor
operator|.
name|leave
argument_list|()
expr_stmt|;
name|thread1
operator|.
name|assertPriorCallReturns
argument_list|(
name|waitFor
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testWaitFor_withoutEnter ()
specifier|public
specifier|final
name|void
name|testWaitFor_withoutEnter
parameter_list|()
throws|throws
name|Exception
block|{
name|TestGuard
name|guard
init|=
operator|new
name|TestGuard
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|thread1
operator|.
name|callAndAssertThrows
argument_list|(
name|IllegalMonitorStateException
operator|.
name|class
argument_list|,
name|waitFor
argument_list|()
argument_list|,
name|guard
argument_list|)
expr_stmt|;
block|}
DECL|method|testNulls ()
specifier|public
name|void
name|testNulls
parameter_list|()
block|{
name|monitor
operator|.
name|enter
argument_list|()
expr_stmt|;
comment|// Inhibit IllegalMonitorStateException
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|setDefault
argument_list|(
name|TimeUnit
operator|.
name|class
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|setDefault
argument_list|(
name|Monitor
operator|.
name|Guard
operator|.
name|class
argument_list|,
operator|new
name|TestGuard
argument_list|(
literal|true
argument_list|)
argument_list|)
operator|.
name|testAllPublicInstanceMethods
argument_list|(
name|monitor
argument_list|)
expr_stmt|;
block|}
comment|// TODO: Test enter(long, TimeUnit).
comment|// TODO: Test enterWhen(Guard, long, TimeUnit).
comment|// TODO: Test enterIf(Guard, long, TimeUnit).
comment|// TODO: Test waitFor(Guard, long, TimeUnit).
comment|// TODO: Test getQueueLength().
comment|// TODO: Test hasQueuedThreads().
comment|// TODO: Test getWaitQueueLength(Guard).
comment|// TODO: Test automatic signaling before leave, waitFor, and reentrant enterWhen.
comment|// TODO: Test blocking to re-enter monitor after being signaled.
comment|// TODO: Test interrupts with both interruptible and uninterruptible monitor.
comment|// TODO: Test multiple waiters: If guard is still satisfied, signal next waiter.
comment|// TODO: Test multiple waiters: If guard is no longer satisfied, do not signal next waiter.
block|}
end_class

end_unit

