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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
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
comment|/**  * Wraps a single-argument 'handler' method on a specific object, and ensures  * that only one thread may enter the method at a time.  *  *<p>Beyond synchronization, this class behaves identically to  * {@link EventHandler}.  *  * @author Cliff Biffle  */
end_comment

begin_class
DECL|class|SynchronizedEventHandler
specifier|final
class|class
name|SynchronizedEventHandler
extends|extends
name|EventHandler
block|{
comment|/**    * Creates a new SynchronizedEventHandler to wrap {@code method} on    * {@code target}.    *    * @param target  object to which the method applies.    * @param method  handler method.    */
DECL|method|SynchronizedEventHandler (Object target, Method method)
specifier|public
name|SynchronizedEventHandler
parameter_list|(
name|Object
name|target
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
name|super
argument_list|(
name|target
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|handleEvent (Object event)
specifier|public
name|void
name|handleEvent
parameter_list|(
name|Object
name|event
parameter_list|)
throws|throws
name|InvocationTargetException
block|{
comment|// https://code.google.com/p/guava-libraries/issues/detail?id=1403
synchronized|synchronized
init|(
name|this
init|)
block|{
name|super
operator|.
name|handleEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

