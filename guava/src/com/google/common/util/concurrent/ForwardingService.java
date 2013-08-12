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
name|Executor
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
name|TimeUnit
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
name|TimeoutException
import|;
end_import

begin_comment
comment|/**  * A {@link Service} that forwards all method calls to another service.  *  * @deprecated Instead of using a {@link ForwardingService}, consider using the  * {@link Service.Listener} functionality to hook into the {@link Service}  * lifecycle, or if you really do need to provide access to some Service  * methods, consider just providing the few that you actually need (e.g. just  * {@link #startAndWait()}) and not implementing Service.  *  * @author Chris Nokleberg  * @since 1.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|Deprecated
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
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|start ()
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
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|stop ()
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
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|startAndWait ()
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
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|stopAndWait ()
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
comment|/**    * @since 13.0    */
DECL|method|addListener (Listener listener, Executor executor)
annotation|@
name|Override
specifier|public
name|void
name|addListener
parameter_list|(
name|Listener
name|listener
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|delegate
argument_list|()
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|executor
argument_list|)
expr_stmt|;
block|}
comment|/**    * @since 14.0    */
DECL|method|failureCause ()
annotation|@
name|Override
specifier|public
name|Throwable
name|failureCause
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|failureCause
argument_list|()
return|;
block|}
comment|/**    * @since 15.0    */
DECL|method|startAsync ()
annotation|@
name|Override
specifier|public
name|Service
name|startAsync
parameter_list|()
block|{
name|delegate
argument_list|()
operator|.
name|startAsync
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * @since 15.0    */
DECL|method|stopAsync ()
annotation|@
name|Override
specifier|public
name|Service
name|stopAsync
parameter_list|()
block|{
name|delegate
argument_list|()
operator|.
name|stopAsync
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * @since 15.0    */
DECL|method|awaitRunning ()
annotation|@
name|Override
specifier|public
name|void
name|awaitRunning
parameter_list|()
block|{
name|delegate
argument_list|()
operator|.
name|awaitRunning
argument_list|()
expr_stmt|;
block|}
comment|/**    * @since 15.0    */
DECL|method|awaitRunning (long timeout, TimeUnit unit)
annotation|@
name|Override
specifier|public
name|void
name|awaitRunning
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|TimeoutException
block|{
name|delegate
argument_list|()
operator|.
name|awaitRunning
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|)
expr_stmt|;
block|}
comment|/**    * @since 15.0    */
DECL|method|awaitTerminated ()
annotation|@
name|Override
specifier|public
name|void
name|awaitTerminated
parameter_list|()
block|{
name|delegate
argument_list|()
operator|.
name|awaitTerminated
argument_list|()
expr_stmt|;
block|}
comment|/**    * @since 15.0    */
DECL|method|awaitTerminated (long timeout, TimeUnit unit)
annotation|@
name|Override
specifier|public
name|void
name|awaitTerminated
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|TimeoutException
block|{
name|delegate
argument_list|()
operator|.
name|awaitTerminated
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|)
expr_stmt|;
block|}
comment|/**    * A sensible default implementation of {@link #startAndWait()}, in terms of    * {@link #start}. If you override {@link #start}, you may wish to override    * {@link #startAndWait()} to forward to this implementation.    * @since 9.0    */
DECL|method|standardStartAndWait ()
specifier|protected
name|State
name|standardStartAndWait
parameter_list|()
block|{
return|return
name|Futures
operator|.
name|getUnchecked
argument_list|(
name|start
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * A sensible default implementation of {@link #stopAndWait()}, in terms of    * {@link #stop}. If you override {@link #stop}, you may wish to override    * {@link #stopAndWait()} to forward to this implementation.    * @since 9.0    */
DECL|method|standardStopAndWait ()
specifier|protected
name|State
name|standardStopAndWait
parameter_list|()
block|{
return|return
name|Futures
operator|.
name|getUnchecked
argument_list|(
name|stop
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

