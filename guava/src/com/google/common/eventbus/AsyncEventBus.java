begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|Executor
import|;
end_import

begin_comment
comment|/**  * An {@link EventBus} that takes the Executor of your choice and uses it to dispatch events,  * allowing dispatch to occur asynchronously.  *  * @author Cliff Biffle  * @since 10.0  */
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
comment|/**    * Creates a new AsyncEventBus that will use {@code executor} to dispatch events. Assigns {@code    * identifier} as the bus's name for logging purposes.    *    * @param identifier short name for the bus, for logging purposes.    * @param executor Executor to use to dispatch events. It is the caller's responsibility to shut    *     down the executor after the last event has been posted to this event bus.    */
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
argument_list|,
name|executor
argument_list|,
name|Dispatcher
operator|.
name|legacyAsync
argument_list|()
argument_list|,
name|LoggingHandler
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates a new AsyncEventBus that will use {@code executor} to dispatch events.    *    * @param executor Executor to use to dispatch events. It is the caller's responsibility to shut    *     down the executor after the last event has been posted to this event bus.    * @param subscriberExceptionHandler Handler used to handle exceptions thrown from subscribers.    *     See {@link SubscriberExceptionHandler} for more information.    * @since 16.0    */
DECL|method|AsyncEventBus (Executor executor, SubscriberExceptionHandler subscriberExceptionHandler)
specifier|public
name|AsyncEventBus
parameter_list|(
name|Executor
name|executor
parameter_list|,
name|SubscriberExceptionHandler
name|subscriberExceptionHandler
parameter_list|)
block|{
name|super
argument_list|(
literal|"default"
argument_list|,
name|executor
argument_list|,
name|Dispatcher
operator|.
name|legacyAsync
argument_list|()
argument_list|,
name|subscriberExceptionHandler
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates a new AsyncEventBus that will use {@code executor} to dispatch events.    *    * @param executor Executor to use to dispatch events. It is the caller's responsibility to shut    *     down the executor after the last event has been posted to this event bus.    */
DECL|method|AsyncEventBus (Executor executor)
specifier|public
name|AsyncEventBus
parameter_list|(
name|Executor
name|executor
parameter_list|)
block|{
name|super
argument_list|(
literal|"default"
argument_list|,
name|executor
argument_list|,
name|Dispatcher
operator|.
name|legacyAsync
argument_list|()
argument_list|,
name|LoggingHandler
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

