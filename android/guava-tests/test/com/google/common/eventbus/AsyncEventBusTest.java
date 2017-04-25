begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.eventbus
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|eventbus
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
name|collect
operator|.
name|Lists
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Test case for {@link AsyncEventBus}.  *  * @author Cliff Biffle  */
end_comment

begin_class
DECL|class|AsyncEventBusTest
specifier|public
class|class
name|AsyncEventBusTest
extends|extends
name|TestCase
block|{
DECL|field|EVENT
specifier|private
specifier|static
specifier|final
name|String
name|EVENT
init|=
literal|"Hello"
decl_stmt|;
comment|/** The executor we use to fake asynchronicity. */
DECL|field|executor
specifier|private
name|FakeExecutor
name|executor
decl_stmt|;
DECL|field|bus
specifier|private
name|AsyncEventBus
name|bus
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|executor
operator|=
operator|new
name|FakeExecutor
argument_list|()
expr_stmt|;
name|bus
operator|=
operator|new
name|AsyncEventBus
argument_list|(
name|executor
argument_list|)
expr_stmt|;
block|}
DECL|method|testBasicDistribution ()
specifier|public
name|void
name|testBasicDistribution
parameter_list|()
block|{
name|StringCatcher
name|catcher
init|=
operator|new
name|StringCatcher
argument_list|()
decl_stmt|;
name|bus
operator|.
name|register
argument_list|(
name|catcher
argument_list|)
expr_stmt|;
comment|// We post the event, but our Executor will not deliver it until instructed.
name|bus
operator|.
name|post
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|events
init|=
name|catcher
operator|.
name|getEvents
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"No events should be delivered synchronously."
argument_list|,
name|events
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// Now we find the task in our Executor and explicitly activate it.
name|List
argument_list|<
name|Runnable
argument_list|>
name|tasks
init|=
name|executor
operator|.
name|getTasks
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"One event dispatch task should be queued."
argument_list|,
literal|1
argument_list|,
name|tasks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|tasks
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"One event should be delivered."
argument_list|,
literal|1
argument_list|,
name|events
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Correct string should be delivered."
argument_list|,
name|EVENT
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * An {@link Executor} wanna-be that simply records the tasks it's given.    * Arguably the Worst Executor Ever.    *    * @author cbiffle    *    */
DECL|class|FakeExecutor
specifier|public
specifier|static
class|class
name|FakeExecutor
implements|implements
name|Executor
block|{
DECL|field|tasks
name|List
argument_list|<
name|Runnable
argument_list|>
name|tasks
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|execute (Runnable task)
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|task
parameter_list|)
block|{
name|tasks
operator|.
name|add
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
DECL|method|getTasks ()
specifier|public
name|List
argument_list|<
name|Runnable
argument_list|>
name|getTasks
parameter_list|()
block|{
return|return
name|tasks
return|;
block|}
block|}
block|}
end_class

end_unit
