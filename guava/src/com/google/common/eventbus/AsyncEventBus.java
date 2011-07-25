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
name|annotations
operator|.
name|Beta
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
name|ConcurrentLinkedQueue
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

begin_comment
comment|/**  * An {@link EventBus} that takes the Executor of your choice and uses it to  * dispatch events, allowing dispatch to occur asynchronously.  *  * @author Cliff Biffle  * @since Guava release 10  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|AsyncEventBus
specifier|public
class|class
name|AsyncEventBus
extends|extends
name|EventBus
block|{
DECL|field|executor
specifier|private
specifier|final
name|Executor
name|executor
decl_stmt|;
comment|/** the queue of events is shared across all threads */
DECL|field|eventsToDispatch
specifier|private
specifier|final
name|ConcurrentLinkedQueue
argument_list|<
name|EventWithHandler
argument_list|>
name|eventsToDispatch
init|=
operator|new
name|ConcurrentLinkedQueue
argument_list|<
name|EventWithHandler
argument_list|>
argument_list|()
decl_stmt|;
comment|/**    * Creates a new AsyncEventBus that will use {@code executor} to dispatch    * events.  Assigns {@code identifier} as the bus's name for logging purposes.    *    * @param identifier short name for the bus, for logging purposes.    * @param executor   Executor to use to dispatch events. It is the caller's    *        responsibility to shut down the executor after the last event has    *        been posted to this event bus.    */
DECL|method|AsyncEventBus (String identifier, Executor executor)
specifier|public
name|AsyncEventBus
parameter_list|(
name|String
name|identifier
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|super
argument_list|(
name|identifier
argument_list|)
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
comment|/**    * Creates a new AsyncEventBus that will use {@code executor} to dispatch    * events.    *    * @param executor Executor to use to dispatch events. It is the caller's    *        responsibility to shut down the executor after the last event has    *        been posted to this event bus.    */
DECL|method|AsyncEventBus (Executor executor)
specifier|public
name|AsyncEventBus
parameter_list|(
name|Executor
name|executor
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|enqueueEvent (Object event, EventHandler handler)
specifier|protected
name|void
name|enqueueEvent
parameter_list|(
name|Object
name|event
parameter_list|,
name|EventHandler
name|handler
parameter_list|)
block|{
name|eventsToDispatch
operator|.
name|offer
argument_list|(
operator|new
name|EventWithHandler
argument_list|(
name|event
argument_list|,
name|handler
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Dispatch {@code events} in the order they were posted, regardless of    * the posting thread.    */
annotation|@
name|Override
DECL|method|dispatchQueuedEvents ()
specifier|protected
name|void
name|dispatchQueuedEvents
parameter_list|()
block|{
while|while
condition|(
literal|true
condition|)
block|{
name|EventWithHandler
name|eventWithHandler
init|=
name|eventsToDispatch
operator|.
name|poll
argument_list|()
decl_stmt|;
if|if
condition|(
name|eventWithHandler
operator|==
literal|null
condition|)
block|{
break|break;
block|}
name|dispatch
argument_list|(
name|eventWithHandler
operator|.
name|event
argument_list|,
name|eventWithHandler
operator|.
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Calls the {@link #executor} to dispatch {@code event} to {@code handler}.    */
annotation|@
name|Override
DECL|method|dispatch (final Object event, final EventHandler handler)
specifier|protected
name|void
name|dispatch
parameter_list|(
specifier|final
name|Object
name|event
parameter_list|,
specifier|final
name|EventHandler
name|handler
parameter_list|)
block|{
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"synthetic-access"
argument_list|)
specifier|public
name|void
name|run
parameter_list|()
block|{
name|AsyncEventBus
operator|.
name|super
operator|.
name|dispatch
argument_list|(
name|event
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

