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
comment|/**  * Base class for services that do not need a thread while "running"  * but may need one during startup and shutdown. Subclasses can  * implement {@link #startUp} and {@link #shutDown} methods, each  * which run in a executor which by default uses a separate thread  * for each method.  *  * @author Chris Nokleberg  * @since 1.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|AbstractIdleService
specifier|public
specifier|abstract
class|class
name|AbstractIdleService
implements|implements
name|Service
block|{
comment|/* use AbstractService for state management */
DECL|field|delegate
specifier|private
specifier|final
name|Service
name|delegate
init|=
operator|new
name|AbstractService
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
specifier|final
name|void
name|doStart
parameter_list|()
block|{
name|executor
argument_list|(
name|State
operator|.
name|STARTING
argument_list|)
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|startUp
argument_list|()
expr_stmt|;
name|notifyStarted
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|notifyFailed
argument_list|(
name|t
argument_list|)
expr_stmt|;
throw|throw
name|Throwables
operator|.
name|propagate
argument_list|(
name|t
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
specifier|final
name|void
name|doStop
parameter_list|()
block|{
name|executor
argument_list|(
name|State
operator|.
name|STOPPING
argument_list|)
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|shutDown
argument_list|()
expr_stmt|;
name|notifyStopped
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|notifyFailed
argument_list|(
name|t
argument_list|)
expr_stmt|;
throw|throw
name|Throwables
operator|.
name|propagate
argument_list|(
name|t
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|/** Start the service. */
DECL|method|startUp ()
specifier|protected
specifier|abstract
name|void
name|startUp
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/** Stop the service. */
DECL|method|shutDown ()
specifier|protected
specifier|abstract
name|void
name|shutDown
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**    * Returns the {@link Executor} that will be used to run this service.    * Subclasses may override this method to use a custom {@link Executor}, which    * may configure its worker thread with a specific name, thread group or    * priority. The returned executor's {@link Executor#execute(Runnable)    * execute()} method is called when this service is started and stopped,    * and should return promptly.    *    * @param state {@link Service.State#STARTING} or    *     {@link Service.State#STOPPING}, used by the default implementation for    *     naming the thread    */
DECL|method|executor (final State state)
specifier|protected
name|Executor
name|executor
parameter_list|(
specifier|final
name|State
name|state
parameter_list|)
block|{
return|return
operator|new
name|Executor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|command
parameter_list|)
block|{
operator|new
name|Thread
argument_list|(
name|command
argument_list|,
name|getServiceName
argument_list|()
operator|+
literal|" "
operator|+
name|state
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getServiceName
argument_list|()
operator|+
literal|" ["
operator|+
name|state
argument_list|()
operator|+
literal|"]"
return|;
block|}
comment|// We override instead of using ForwardingService so that these can be final.
DECL|method|start ()
annotation|@
name|Override
specifier|public
specifier|final
name|ListenableFuture
argument_list|<
name|State
argument_list|>
name|start
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|start
argument_list|()
return|;
block|}
DECL|method|startAndWait ()
annotation|@
name|Override
specifier|public
specifier|final
name|State
name|startAndWait
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|startAndWait
argument_list|()
return|;
block|}
DECL|method|isRunning ()
annotation|@
name|Override
specifier|public
specifier|final
name|boolean
name|isRunning
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|isRunning
argument_list|()
return|;
block|}
DECL|method|state ()
annotation|@
name|Override
specifier|public
specifier|final
name|State
name|state
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|state
argument_list|()
return|;
block|}
DECL|method|stop ()
annotation|@
name|Override
specifier|public
specifier|final
name|ListenableFuture
argument_list|<
name|State
argument_list|>
name|stop
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|stop
argument_list|()
return|;
block|}
DECL|method|stopAndWait ()
annotation|@
name|Override
specifier|public
specifier|final
name|State
name|stopAndWait
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|stopAndWait
argument_list|()
return|;
block|}
DECL|method|addListener (Listener listener, Executor executor)
annotation|@
name|Override
specifier|public
specifier|final
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
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|executor
argument_list|)
expr_stmt|;
block|}
DECL|method|failureCause ()
annotation|@
name|Override
specifier|public
specifier|final
name|Throwable
name|failureCause
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|failureCause
argument_list|()
return|;
block|}
DECL|method|getServiceName ()
specifier|private
name|String
name|getServiceName
parameter_list|()
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

