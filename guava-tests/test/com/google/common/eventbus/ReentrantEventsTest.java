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

begin_comment
comment|/**  * Validate that {@link EventBus} behaves carefully when listeners publish  * their own events.  *  * @author Jesse Wilson  */
end_comment

begin_class
DECL|class|ReentrantEventsTest
specifier|public
class|class
name|ReentrantEventsTest
extends|extends
name|TestCase
block|{
DECL|field|FIRST
specifier|static
specifier|final
name|String
name|FIRST
init|=
literal|"one"
decl_stmt|;
DECL|field|SECOND
specifier|static
specifier|final
name|Double
name|SECOND
init|=
literal|2.0d
decl_stmt|;
DECL|field|bus
specifier|final
name|EventBus
name|bus
init|=
operator|new
name|EventBus
argument_list|()
decl_stmt|;
DECL|method|testNoReentrantEvents ()
specifier|public
name|void
name|testNoReentrantEvents
parameter_list|()
block|{
name|ReentrantEventsHater
name|hater
init|=
operator|new
name|ReentrantEventsHater
argument_list|()
decl_stmt|;
name|bus
operator|.
name|register
argument_list|(
name|hater
argument_list|)
expr_stmt|;
name|bus
operator|.
name|post
argument_list|(
name|FIRST
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ReentrantEventHater expected 2 events"
argument_list|,
name|Lists
operator|.
expr|<
name|Object
operator|>
name|newArrayList
argument_list|(
name|FIRST
argument_list|,
name|SECOND
argument_list|)
argument_list|,
name|hater
operator|.
name|eventsReceived
argument_list|)
expr_stmt|;
block|}
DECL|class|ReentrantEventsHater
specifier|public
class|class
name|ReentrantEventsHater
block|{
DECL|field|ready
name|boolean
name|ready
init|=
literal|true
decl_stmt|;
DECL|field|eventsReceived
name|List
argument_list|<
name|Object
argument_list|>
name|eventsReceived
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
annotation|@
name|Subscribe
DECL|method|listenForStrings (String event)
specifier|public
name|void
name|listenForStrings
parameter_list|(
name|String
name|event
parameter_list|)
block|{
name|eventsReceived
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|ready
operator|=
literal|false
expr_stmt|;
try|try
block|{
name|bus
operator|.
name|post
argument_list|(
name|SECOND
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|ready
operator|=
literal|true
expr_stmt|;
block|}
block|}
annotation|@
name|Subscribe
DECL|method|listenForDoubles (Double event)
specifier|public
name|void
name|listenForDoubles
parameter_list|(
name|Double
name|event
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"I received an event when I wasn't ready!"
argument_list|,
name|ready
argument_list|)
expr_stmt|;
name|eventsReceived
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testEventOrderingIsPredictable ()
specifier|public
name|void
name|testEventOrderingIsPredictable
parameter_list|()
block|{
name|EventProcessor
name|processor
init|=
operator|new
name|EventProcessor
argument_list|()
decl_stmt|;
name|bus
operator|.
name|register
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|EventRecorder
name|recorder
init|=
operator|new
name|EventRecorder
argument_list|()
decl_stmt|;
name|bus
operator|.
name|register
argument_list|(
name|recorder
argument_list|)
expr_stmt|;
name|bus
operator|.
name|post
argument_list|(
name|FIRST
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"EventRecorder expected events in order"
argument_list|,
name|Lists
operator|.
expr|<
name|Object
operator|>
name|newArrayList
argument_list|(
name|FIRST
argument_list|,
name|SECOND
argument_list|)
argument_list|,
name|recorder
operator|.
name|eventsReceived
argument_list|)
expr_stmt|;
block|}
DECL|class|EventProcessor
specifier|public
class|class
name|EventProcessor
block|{
DECL|method|listenForStrings (String event)
annotation|@
name|Subscribe
specifier|public
name|void
name|listenForStrings
parameter_list|(
name|String
name|event
parameter_list|)
block|{
name|bus
operator|.
name|post
argument_list|(
name|SECOND
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|EventRecorder
specifier|public
class|class
name|EventRecorder
block|{
DECL|field|eventsReceived
name|List
argument_list|<
name|Object
argument_list|>
name|eventsReceived
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|method|listenForEverything (Object event)
annotation|@
name|Subscribe
specifier|public
name|void
name|listenForEverything
parameter_list|(
name|Object
name|event
parameter_list|)
block|{
name|eventsReceived
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

