begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|annotations
operator|.
name|Beta
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
name|base
operator|.
name|Service
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
name|collect
operator|.
name|ForwardingObject
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
name|Future
import|;
end_import

begin_comment
comment|/**  * A {@link Service} that forwards all method calls to another service.  *  * @author Chris Nokleberg  * @since 1  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|ForwardingService
specifier|public
specifier|abstract
class|class
name|ForwardingService
extends|extends
name|ForwardingObject
implements|implements
name|Service
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingService ()
specifier|protected
name|ForwardingService
parameter_list|()
block|{}
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
specifier|abstract
name|Service
name|delegate
parameter_list|()
function_decl|;
DECL|method|start ()
comment|/*@Override*/
specifier|public
name|Future
argument_list|<
name|State
argument_list|>
name|start
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|start
argument_list|()
return|;
block|}
DECL|method|state ()
comment|/*@Override*/
specifier|public
name|State
name|state
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|state
argument_list|()
return|;
block|}
DECL|method|stop ()
comment|/*@Override*/
specifier|public
name|Future
argument_list|<
name|State
argument_list|>
name|stop
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|stop
argument_list|()
return|;
block|}
DECL|method|startAndWait ()
comment|/*@Override*/
specifier|public
name|State
name|startAndWait
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|startAndWait
argument_list|()
return|;
block|}
DECL|method|stopAndWait ()
comment|/*@Override*/
specifier|public
name|State
name|stopAndWait
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|stopAndWait
argument_list|()
return|;
block|}
DECL|method|isRunning ()
comment|/*@Override*/
specifier|public
name|boolean
name|isRunning
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|isRunning
argument_list|()
return|;
block|}
block|}
end_class

end_unit

