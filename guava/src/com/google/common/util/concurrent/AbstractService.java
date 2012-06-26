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
name|Lists
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
name|util
operator|.
name|concurrent
operator|.
name|Service
operator|.
name|State
import|;
end_import

begin_comment
comment|// javadoc needs this
end_comment

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|concurrent
operator|.
name|locks
operator|.
name|ReentrantLock
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

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|concurrent
operator|.
name|GuardedBy
import|;
end_import

begin_comment
comment|/**  * Base class for implementing services that can handle {@link #doStart} and  * {@link #doStop} requests, responding to them with {@link #notifyStarted()}  * and {@link #notifyStopped()} callbacks. Its subclasses must manage threads  * manually; consider {@link AbstractExecutionThreadService} if you need only a  * single execution thread.  *  * @author Jesse Wilson  * @since 1.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|AbstractService
specifier|public
specifier|abstract
class|class
name|AbstractService
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
name|AbstractService
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|lock
specifier|private
specifier|final
name|ReentrantLock
name|lock
init|=
operator|new
name|ReentrantLock
argument_list|()
decl_stmt|;
DECL|field|startup
specifier|private
specifier|final
name|Transition
name|startup
init|=
operator|new
name|Transition
argument_list|()
decl_stmt|;
DECL|field|shutdown
specifier|private
specifier|final
name|Transition
name|shutdown
init|=
operator|new
name|Transition
argument_list|()
decl_stmt|;
comment|/**    * The listeners to notify during a state transition.    */
annotation|@
name|GuardedBy
argument_list|(
literal|"lock"
argument_list|)
DECL|field|listeners
specifier|private
specifier|final
name|List
argument_list|<
name|ListenerExecutorPair
argument_list|>
name|listeners
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
comment|/**    * The exception that caused this service to fail.  This will be {@code null}    * unless the service has failed.    */
annotation|@
name|GuardedBy
argument_list|(
literal|"lock"
argument_list|)
annotation|@
name|Nullable
DECL|field|failure
specifier|private
name|Throwable
name|failure
decl_stmt|;
comment|/**    * The internal state, which equals external state unless    * shutdownWhenStartupFinishes is true.    */
annotation|@
name|GuardedBy
argument_list|(
literal|"lock"
argument_list|)
DECL|field|state
specifier|private
name|State
name|state
init|=
name|State
operator|.
name|NEW
decl_stmt|;
comment|/**    * If true, the user requested a shutdown while the service was still starting    * up.    */
annotation|@
name|GuardedBy
argument_list|(
literal|"lock"
argument_list|)
DECL|field|shutdownWhenStartupFinishes
specifier|private
name|boolean
name|shutdownWhenStartupFinishes
init|=
literal|false
decl_stmt|;
comment|/**    * This method is called by {@link #start} to initiate service startup. The    * invocation of this method should cause a call to {@link #notifyStarted()},    * either during this method's run, or after it has returned. If startup    * fails, the invocation should cause a call to {@link    * #notifyFailed(Throwable)} instead.    *    *<p>This method should return promptly; prefer to do work on a different    * thread where it is convenient. It is invoked exactly once on service    * startup, even when {@link #start} is called multiple times.    */
DECL|method|doStart ()
specifier|protected
specifier|abstract
name|void
name|doStart
parameter_list|()
function_decl|;
comment|/**    * This method should be used to initiate service shutdown. The invocation    * of this method should cause a call to {@link #notifyStopped()}, either    * during this method's run, or after it has returned. If shutdown fails, the    * invocation should cause a call to {@link #notifyFailed(Throwable)} instead.    *    *<p>This method should return promptly; prefer to do work on a different    * thread where it is convenient. It is invoked exactly once on service    * shutdown, even when {@link #stop} is called multiple times.    */
DECL|method|doStop ()
specifier|protected
specifier|abstract
name|void
name|doStop
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|start ()
specifier|public
specifier|final
name|ListenableFuture
argument_list|<
name|State
argument_list|>
name|start
parameter_list|()
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|state
operator|==
name|State
operator|.
name|NEW
condition|)
block|{
name|starting
argument_list|()
expr_stmt|;
name|doStart
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|startupFailure
parameter_list|)
block|{
comment|// put the exception in the future, the user can get it via Future.get()
name|notifyFailed
argument_list|(
name|startupFailure
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
return|return
name|startup
return|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
specifier|final
name|ListenableFuture
argument_list|<
name|State
argument_list|>
name|stop
parameter_list|()
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|state
operator|==
name|State
operator|.
name|NEW
condition|)
block|{
name|state
operator|=
name|State
operator|.
name|TERMINATED
expr_stmt|;
name|terminated
argument_list|(
name|State
operator|.
name|NEW
argument_list|)
expr_stmt|;
name|startup
operator|.
name|set
argument_list|(
name|State
operator|.
name|TERMINATED
argument_list|)
expr_stmt|;
name|shutdown
operator|.
name|set
argument_list|(
name|State
operator|.
name|TERMINATED
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|State
operator|.
name|STARTING
condition|)
block|{
name|shutdownWhenStartupFinishes
operator|=
literal|true
expr_stmt|;
name|startup
operator|.
name|set
argument_list|(
name|State
operator|.
name|STOPPING
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|State
operator|.
name|RUNNING
condition|)
block|{
name|state
operator|=
name|State
operator|.
name|STOPPING
expr_stmt|;
name|stopping
argument_list|(
name|State
operator|.
name|RUNNING
argument_list|)
expr_stmt|;
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|shutdownFailure
parameter_list|)
block|{
comment|// put the exception in the future, the user can get it via Future.get()
name|notifyFailed
argument_list|(
name|shutdownFailure
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
return|return
name|shutdown
return|;
block|}
annotation|@
name|Override
DECL|method|startAndWait ()
specifier|public
name|State
name|startAndWait
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
annotation|@
name|Override
DECL|method|stopAndWait ()
specifier|public
name|State
name|stopAndWait
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
comment|/**    * Implementing classes should invoke this method once their service has    * started. It will cause the service to transition from {@link    * State#STARTING} to {@link State#RUNNING}.    *    * @throws IllegalStateException if the service is not    *     {@link State#STARTING}.    */
DECL|method|notifyStarted ()
specifier|protected
specifier|final
name|void
name|notifyStarted
parameter_list|()
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|state
operator|!=
name|State
operator|.
name|STARTING
condition|)
block|{
name|IllegalStateException
name|failure
init|=
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot notifyStarted() when the service is "
operator|+
name|state
argument_list|)
decl_stmt|;
name|notifyFailed
argument_list|(
name|failure
argument_list|)
expr_stmt|;
throw|throw
name|failure
throw|;
block|}
name|running
argument_list|()
expr_stmt|;
if|if
condition|(
name|shutdownWhenStartupFinishes
condition|)
block|{
name|stop
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|startup
operator|.
name|set
argument_list|(
name|State
operator|.
name|RUNNING
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Implementing classes should invoke this method once their service has    * stopped. It will cause the service to transition from {@link    * State#STOPPING} to {@link State#TERMINATED}.    *    * @throws IllegalStateException if the service is neither {@link    *     State#STOPPING} nor {@link State#RUNNING}.    */
DECL|method|notifyStopped ()
specifier|protected
specifier|final
name|void
name|notifyStopped
parameter_list|()
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|state
operator|!=
name|State
operator|.
name|STOPPING
operator|&&
name|state
operator|!=
name|State
operator|.
name|RUNNING
condition|)
block|{
name|IllegalStateException
name|failure
init|=
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot notifyStopped() when the service is "
operator|+
name|state
argument_list|)
decl_stmt|;
name|notifyFailed
argument_list|(
name|failure
argument_list|)
expr_stmt|;
throw|throw
name|failure
throw|;
block|}
name|terminated
argument_list|(
name|state
argument_list|)
expr_stmt|;
name|shutdown
operator|.
name|set
argument_list|(
name|State
operator|.
name|TERMINATED
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Invoke this method to transition the service to the    * {@link State#FAILED}. The service will<b>not be stopped</b> if it    * is running. Invoke this method when a service has failed critically or    * otherwise cannot be started nor stopped.    */
DECL|method|notifyFailed (Throwable cause)
specifier|protected
specifier|final
name|void
name|notifyFailed
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|cause
argument_list|)
expr_stmt|;
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|state
operator|==
name|State
operator|.
name|STARTING
condition|)
block|{
name|startup
operator|.
name|setException
argument_list|(
name|cause
argument_list|)
expr_stmt|;
name|shutdown
operator|.
name|setException
argument_list|(
operator|new
name|Exception
argument_list|(
literal|"Service failed to start."
argument_list|,
name|cause
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|State
operator|.
name|STOPPING
condition|)
block|{
name|shutdown
operator|.
name|setException
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|State
operator|.
name|RUNNING
condition|)
block|{
name|shutdown
operator|.
name|setException
argument_list|(
operator|new
name|Exception
argument_list|(
literal|"Service failed while running"
argument_list|,
name|cause
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|State
operator|.
name|NEW
operator|||
name|state
operator|==
name|State
operator|.
name|TERMINATED
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Failed while in state:"
operator|+
name|state
argument_list|,
name|cause
argument_list|)
throw|;
block|}
name|failed
argument_list|(
name|state
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
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
name|state
argument_list|()
operator|==
name|State
operator|.
name|RUNNING
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
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|shutdownWhenStartupFinishes
operator|&&
name|state
operator|==
name|State
operator|.
name|STARTING
condition|)
block|{
return|return
name|State
operator|.
name|STOPPING
return|;
block|}
else|else
block|{
return|return
name|state
return|;
block|}
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
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
name|checkNotNull
argument_list|(
name|listener
argument_list|,
literal|"listener"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|executor
argument_list|,
literal|"executor"
argument_list|)
expr_stmt|;
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|state
operator|!=
name|State
operator|.
name|TERMINATED
operator|&&
name|state
operator|!=
name|State
operator|.
name|FAILED
condition|)
block|{
name|listeners
operator|.
name|add
argument_list|(
operator|new
name|ListenerExecutorPair
argument_list|(
name|listener
argument_list|,
name|executor
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
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
name|getClass
argument_list|()
operator|.
name|getSimpleName
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
comment|/**    * A change from one service state to another, plus the result of the change.    */
DECL|class|Transition
specifier|private
class|class
name|Transition
extends|extends
name|AbstractFuture
argument_list|<
name|State
argument_list|>
block|{
annotation|@
name|Override
DECL|method|get (long timeout, TimeUnit unit)
specifier|public
name|State
name|get
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
throws|,
name|TimeoutException
throws|,
name|ExecutionException
block|{
try|try
block|{
return|return
name|super
operator|.
name|get
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|TimeoutException
argument_list|(
name|AbstractService
operator|.
name|this
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|GuardedBy
argument_list|(
literal|"lock"
argument_list|)
DECL|method|starting ()
specifier|private
name|void
name|starting
parameter_list|()
block|{
name|state
operator|=
name|State
operator|.
name|STARTING
expr_stmt|;
for|for
control|(
name|Listener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|starting
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|GuardedBy
argument_list|(
literal|"lock"
argument_list|)
DECL|method|running ()
specifier|private
name|void
name|running
parameter_list|()
block|{
name|state
operator|=
name|State
operator|.
name|RUNNING
expr_stmt|;
for|for
control|(
name|Listener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|running
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|GuardedBy
argument_list|(
literal|"lock"
argument_list|)
DECL|method|stopping (State from)
specifier|private
name|void
name|stopping
parameter_list|(
name|State
name|from
parameter_list|)
block|{
name|state
operator|=
name|State
operator|.
name|STOPPING
expr_stmt|;
for|for
control|(
name|Listener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|stopping
argument_list|(
name|from
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|GuardedBy
argument_list|(
literal|"lock"
argument_list|)
DECL|method|terminated (State from)
specifier|private
name|void
name|terminated
parameter_list|(
name|State
name|from
parameter_list|)
block|{
name|state
operator|=
name|State
operator|.
name|TERMINATED
expr_stmt|;
for|for
control|(
name|Listener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|terminated
argument_list|(
name|from
argument_list|)
expr_stmt|;
block|}
comment|// There are no more state transitions so we can clear this out.
name|listeners
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GuardedBy
argument_list|(
literal|"lock"
argument_list|)
DECL|method|failed (State from, Throwable cause)
specifier|private
name|void
name|failed
parameter_list|(
name|State
name|from
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|failure
operator|=
name|cause
expr_stmt|;
name|state
operator|=
name|State
operator|.
name|FAILED
expr_stmt|;
for|for
control|(
name|Listener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|failed
argument_list|(
name|from
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
comment|// There are no more state transitions so we can clear this out.
name|listeners
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**    * A {@link Service.Listener} that schedules the callbacks of the delegate listener on an    * {@link Executor}.    */
DECL|class|ListenerExecutorPair
specifier|private
specifier|static
class|class
name|ListenerExecutorPair
implements|implements
name|Listener
block|{
DECL|field|listener
specifier|final
name|Listener
name|listener
decl_stmt|;
DECL|field|executor
specifier|final
name|Executor
name|executor
decl_stmt|;
DECL|method|ListenerExecutorPair (Listener listener, Executor executor)
name|ListenerExecutorPair
parameter_list|(
name|Listener
name|listener
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|this
operator|.
name|listener
operator|=
name|listener
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
comment|/**      * Executes the given {@link Runnable} on {@link #executor} logging and swallowing all      * exceptions      */
DECL|method|execute (Runnable runnable)
name|void
name|execute
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
try|try
block|{
name|executor
operator|.
name|execute
argument_list|(
name|runnable
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"Exception while executing listener "
operator|+
name|listener
operator|+
literal|" with executor "
operator|+
name|executor
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|starting ()
specifier|public
name|void
name|starting
parameter_list|()
block|{
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
name|listener
operator|.
name|starting
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|running ()
specifier|public
name|void
name|running
parameter_list|()
block|{
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
name|listener
operator|.
name|running
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stopping (final State from)
specifier|public
name|void
name|stopping
parameter_list|(
specifier|final
name|State
name|from
parameter_list|)
block|{
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
name|listener
operator|.
name|stopping
argument_list|(
name|from
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|terminated (final State from)
specifier|public
name|void
name|terminated
parameter_list|(
specifier|final
name|State
name|from
parameter_list|)
block|{
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
name|listener
operator|.
name|terminated
argument_list|(
name|from
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|failed (final State from, final Throwable failure)
specifier|public
name|void
name|failed
parameter_list|(
specifier|final
name|State
name|from
parameter_list|,
specifier|final
name|Throwable
name|failure
parameter_list|)
block|{
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
name|listener
operator|.
name|failed
argument_list|(
name|from
argument_list|,
name|failure
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

