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

begin_comment
comment|/**  * Wraps an event that was posted, but which had no subscribers and thus could  * not be delivered.  *  *<p>Subscribing a DeadEvent handler is useful for debugging or logging, as it  * can detect misconfigurations in a system's event distribution.  *  * @author Cliff Biffle  * @since Guava release 10  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|DeadEvent
specifier|public
class|class
name|DeadEvent
block|{
DECL|field|source
specifier|private
specifier|final
name|Object
name|source
decl_stmt|;
DECL|field|event
specifier|private
specifier|final
name|Object
name|event
decl_stmt|;
comment|/**    * Creates a new DeadEvent.    *    * @param source  object broadcasting the DeadEvent (generally the    *                {@link EventBus}).    * @param event   the event that could not be delivered.    */
DECL|method|DeadEvent (Object source, Object event)
specifier|public
name|DeadEvent
parameter_list|(
name|Object
name|source
parameter_list|,
name|Object
name|event
parameter_list|)
block|{
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
name|this
operator|.
name|event
operator|=
name|event
expr_stmt|;
block|}
comment|/**    * Returns the object that originated this event (<em>not</em> the object that    * originated the wrapped event).  This is generally an {@link EventBus}.    *    * @return the source of this event.    */
DECL|method|getSource ()
specifier|public
name|Object
name|getSource
parameter_list|()
block|{
return|return
name|source
return|;
block|}
comment|/**    * Returns the wrapped, 'dead' event, which the system was unable to deliver    * to any registered handler.    *    * @return the 'dead' event that could not be delivered.    */
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
block|}
end_class

end_unit

