begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Handler for exceptions thrown by event subscribers.  */
end_comment

begin_interface
DECL|interface|SubscriberExceptionHandler
specifier|public
interface|interface
name|SubscriberExceptionHandler
block|{
comment|/**    * Handles exceptions thrown by subscribers.    */
DECL|method|handleException (Throwable exception, SubscriberExceptionContext context)
name|void
name|handleException
parameter_list|(
name|Throwable
name|exception
parameter_list|,
name|SubscriberExceptionContext
name|context
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

