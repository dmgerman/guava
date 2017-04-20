begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_comment
comment|/**  * Context for an exception thrown by a subscriber.  *  * @since 16.0  */
end_comment

begin_class
DECL|class|SubscriberExceptionContext
specifier|public
class|class
name|SubscriberExceptionContext
block|{
DECL|field|eventBus
specifier|private
specifier|final
name|EventBus
name|eventBus
decl_stmt|;
DECL|field|event
specifier|private
specifier|final
name|Object
name|event
decl_stmt|;
DECL|field|subscriber
specifier|private
specifier|final
name|Object
name|subscriber
decl_stmt|;
DECL|field|subscriberMethod
specifier|private
specifier|final
name|Method
name|subscriberMethod
decl_stmt|;
comment|/**    * @param eventBus The {@link EventBus} that handled the event and the subscriber. Useful for    *     broadcasting a a new event based on the error.    * @param event The event object that caused the subscriber to throw.    * @param subscriber The source subscriber context.    * @param subscriberMethod the subscribed method.    */
DECL|method|SubscriberExceptionContext ( EventBus eventBus, Object event, Object subscriber, Method subscriberMethod)
name|SubscriberExceptionContext
parameter_list|(
name|EventBus
name|eventBus
parameter_list|,
name|Object
name|event
parameter_list|,
name|Object
name|subscriber
parameter_list|,
name|Method
name|subscriberMethod
parameter_list|)
block|{
name|this
operator|.
name|eventBus
operator|=
name|checkNotNull
argument_list|(
name|eventBus
argument_list|)
expr_stmt|;
name|this
operator|.
name|event
operator|=
name|checkNotNull
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|this
operator|.
name|subscriber
operator|=
name|checkNotNull
argument_list|(
name|subscriber
argument_list|)
expr_stmt|;
name|this
operator|.
name|subscriberMethod
operator|=
name|checkNotNull
argument_list|(
name|subscriberMethod
argument_list|)
expr_stmt|;
block|}
comment|/**    * @return The {@link EventBus} that handled the event and the subscriber. Useful for broadcasting    *     a a new event based on the error.    */
DECL|method|getEventBus ()
specifier|public
name|EventBus
name|getEventBus
parameter_list|()
block|{
return|return
name|eventBus
return|;
block|}
comment|/**    * @return The event object that caused the subscriber to throw.    */
DECL|method|getEvent ()
specifier|public
name|Object
name|getEvent
parameter_list|()
block|{
return|return
name|event
return|;
block|}
comment|/**    * @return The object context that the subscriber was called on.    */
DECL|method|getSubscriber ()
specifier|public
name|Object
name|getSubscriber
parameter_list|()
block|{
return|return
name|subscriber
return|;
block|}
comment|/**    * @return The subscribed method that threw the exception.    */
DECL|method|getSubscriberMethod ()
specifier|public
name|Method
name|getSubscriberMethod
parameter_list|()
block|{
return|return
name|subscriberMethod
return|;
block|}
block|}
end_class

end_unit

