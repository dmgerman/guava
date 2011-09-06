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
comment|/**  * Base class for services that can implement {@link #startUp}, {@link #run} and  * {@link #shutDown} methods. This class uses a single thread to execute the  * service; consider {@link AbstractService} if you would like to manage any  * threading manually.  *  * @author Jesse Wilson  * @since 1.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|AbstractExecutionThreadService
specifier|public
specifier|abstract
class|class
name|AbstractExecutionThreadService
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
argument_list|()
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
if|if
condition|(
name|isRunning
argument_list|()
condition|)
block|{
try|try
block|{
name|AbstractExecutionThreadService
operator|.
name|this
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
try|try
block|{
name|shutDown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ignored
parameter_list|)
block|{}
throw|throw
name|t
throw|;
block|}
block|}
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
annotation|@
name|Override
specifier|protected
name|void
name|doStop
parameter_list|()
block|{
name|triggerShutdown
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|/**    * Start the service. This method is invoked on the execution thread.    */
DECL|method|startUp ()
specifier|protected
name|void
name|startUp
parameter_list|()
throws|throws
name|Exception
block|{}
comment|/**    * Run the service. This method is invoked on the execution thread.    * Implementations must respond to stop requests. You could poll for lifecycle    * changes in a work loop:    *<pre>    *   public void run() {    *     while ({@link #isRunning()}) {    *       // perform a unit of work    *     }    *   }    *</pre>    * ...or you could respond to stop requests by implementing {@link    * #triggerShutdown()}, which should cause {@link #run()} to return.    */
DECL|method|run ()
specifier|protected
specifier|abstract
name|void
name|run
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**    * Stop the service. This method is invoked on the execution thread.    */
comment|// TODO: consider supporting a TearDownTestCase-like API
DECL|method|shutDown ()
specifier|protected
name|void
name|shutDown
parameter_list|()
throws|throws
name|Exception
block|{}
comment|/**    * Invoked to request the service to stop.    */
DECL|method|triggerShutdown ()
specifier|protected
name|void
name|triggerShutdown
parameter_list|()
block|{}
comment|/**    * Returns the {@link Executor} that will be used to run this service.    * Subclasses may override this method to use a custom {@link Executor}, which    * may configure its worker thread with a specific name, thread group or    * priority. The returned executor's {@link Executor#execute(Runnable)    * execute()} method is called when this service is started, and should return    * promptly.    */
DECL|method|executor ()
specifier|protected
name|Executor
name|executor
parameter_list|()
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
comment|/**    * Returns the name of this service. {@link AbstractExecutionThreadService} may include the name    * in debugging output.    *    *<p>Subclasses may override this method.    *    * @since 10.0    */
DECL|method|getServiceName ()
specifier|protected
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

