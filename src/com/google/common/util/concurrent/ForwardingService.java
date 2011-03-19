begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Throwables
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
name|ExecutionException
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
annotation|@
name|Override
specifier|public
name|ListenableFuture
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
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|ListenableFuture
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
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
comment|/**    * A sensible default implementation of {@link #startAndWait()}, in terms of    * {@link #start}. If you override {@link #start}, you may wish to override    * {@link #startAndWait()} to forward to this implementation.    */
DECL|method|standardStartAndWait ()
specifier|protected
name|State
name|standardStartAndWait
parameter_list|()
block|{
try|try
block|{
return|return
name|Futures
operator|.
name|makeUninterruptible
argument_list|(
name|start
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
throw|throw
name|Throwables
operator|.
name|propagate
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**    * A sensible default implementation of {@link #stopAndWait()}, in terms of    * {@link #stop}. If you override {@link #stop}, you may wish to override    * {@link #stopAndWait()} to forward to this implementation.    */
DECL|method|standardStopAndWait ()
specifier|protected
name|State
name|standardStopAndWait
parameter_list|()
block|{
try|try
block|{
return|return
name|Futures
operator|.
name|makeUninterruptible
argument_list|(
name|stop
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
throw|throw
name|Throwables
operator|.
name|propagate
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

