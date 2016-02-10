begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|annotations
operator|.
name|GwtIncompatible
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
name|Supplier
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_comment
comment|/**  * Base class for services that can implement {@link #startUp}, {@link #run} and {@link #shutDown}  * methods. This class uses a single thread to execute the service; consider {@link AbstractService}  * if you would like to manage any threading manually.  *  * @author Jesse Wilson  * @since 1.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
DECL|class|AbstractExecutionThreadService
specifier|public
specifier|abstract
class|class
name|AbstractExecutionThreadService
implements|implements
name|Service
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|AbstractExecutionThreadService
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
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
name|Executor
name|executor
init|=
name|MoreExecutors
operator|.
name|renamingDecorator
argument_list|(
name|executor
argument_list|()
argument_list|,
operator|new
name|Supplier
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|serviceName
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|executor
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
comment|// If stopAsync() is called while starting we may be in the STOPPING state in
comment|// which case we should skip right down to shutdown.
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
block|{
comment|// TODO(lukes): if guava ever moves to java7, this would be a good
comment|// candidate for a suppressed exception, or maybe we could generalize
comment|// Closer.Suppressor
name|logger
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Error while attempting to shut down the service"
operator|+
literal|" after failure."
argument_list|,
name|ignored
argument_list|)
expr_stmt|;
block|}
name|notifyFailed
argument_list|(
name|t
argument_list|)
expr_stmt|;
return|return;
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
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|AbstractExecutionThreadService
operator|.
name|this
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
decl_stmt|;
comment|/**    * Constructor for use by subclasses.    */
DECL|method|AbstractExecutionThreadService ()
specifier|protected
name|AbstractExecutionThreadService
parameter_list|()
block|{}
comment|/**    * Start the service. This method is invoked on the execution thread.    *    *<p>By default this method does nothing.    */
DECL|method|startUp ()
specifier|protected
name|void
name|startUp
parameter_list|()
throws|throws
name|Exception
block|{}
comment|/**    * Run the service. This method is invoked on the execution thread. Implementations must respond    * to stop requests. You could poll for lifecycle changes in a work loop:    *    *<pre>    *   public void run() {    *     while ({@link #isRunning()}) {    *       // perform a unit of work    *     }    *   }    *</pre>    *    *<p>...or you could respond to stop requests by implementing {@link #triggerShutdown()}, which    * should cause {@link #run()} to return.    */
DECL|method|run ()
specifier|protected
specifier|abstract
name|void
name|run
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**    * Stop the service. This method is invoked on the execution thread.    *    *<p>By default this method does nothing.    */
comment|// TODO: consider supporting a TearDownTestCase-like API
DECL|method|shutDown ()
specifier|protected
name|void
name|shutDown
parameter_list|()
throws|throws
name|Exception
block|{}
comment|/**    * Invoked to request the service to stop.    *    *<p>By default this method does nothing.    */
DECL|method|triggerShutdown ()
specifier|protected
name|void
name|triggerShutdown
parameter_list|()
block|{}
comment|/**    * Returns the {@link Executor} that will be used to run this service. Subclasses may override    * this method to use a custom {@link Executor}, which may configure its worker thread with a    * specific name, thread group or priority. The returned executor's {@link    * Executor#execute(Runnable) execute()} method is called when this service is started, and should    * return promptly.    *    *<p>The default implementation returns a new {@link Executor} that sets the name of its threads    * to the string returned by {@link #serviceName}    */
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
name|MoreExecutors
operator|.
name|newThread
argument_list|(
name|serviceName
argument_list|()
argument_list|,
name|command
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|serviceName
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
annotation|@
name|Override
DECL|method|isRunning ()
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
annotation|@
name|Override
DECL|method|state ()
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
comment|/**    * @since 13.0    */
annotation|@
name|Override
DECL|method|addListener (Listener listener, Executor executor)
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
comment|/**    * @since 14.0    */
annotation|@
name|Override
DECL|method|failureCause ()
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
comment|/**    * @since 15.0    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|startAsync ()
specifier|public
specifier|final
name|Service
name|startAsync
parameter_list|()
block|{
name|delegate
operator|.
name|startAsync
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * @since 15.0    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|stopAsync ()
specifier|public
specifier|final
name|Service
name|stopAsync
parameter_list|()
block|{
name|delegate
operator|.
name|stopAsync
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * @since 15.0    */
annotation|@
name|Override
DECL|method|awaitRunning ()
specifier|public
specifier|final
name|void
name|awaitRunning
parameter_list|()
block|{
name|delegate
operator|.
name|awaitRunning
argument_list|()
expr_stmt|;
block|}
comment|/**    * @since 15.0    */
annotation|@
name|Override
DECL|method|awaitRunning (long timeout, TimeUnit unit)
specifier|public
specifier|final
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
annotation|@
name|Override
DECL|method|awaitTerminated ()
specifier|public
specifier|final
name|void
name|awaitTerminated
parameter_list|()
block|{
name|delegate
operator|.
name|awaitTerminated
argument_list|()
expr_stmt|;
block|}
comment|/**    * @since 15.0    */
annotation|@
name|Override
DECL|method|awaitTerminated (long timeout, TimeUnit unit)
specifier|public
specifier|final
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
operator|.
name|awaitTerminated
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns the name of this service. {@link AbstractExecutionThreadService} may include the name    * in debugging output.    *    *<p>Subclasses may override this method.    *    * @since 14.0 (present in 10.0 as getServiceName)    */
DECL|method|serviceName ()
specifier|protected
name|String
name|serviceName
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

