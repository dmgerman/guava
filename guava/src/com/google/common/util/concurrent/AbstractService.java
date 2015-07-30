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
name|checkArgument
import|;
end_import

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
name|checkState
import|;
end_import

begin_import
import|import static
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
operator|.
name|FAILED
import|;
end_import

begin_import
import|import static
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
operator|.
name|NEW
import|;
end_import

begin_import
import|import static
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
operator|.
name|RUNNING
import|;
end_import

begin_import
import|import static
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
operator|.
name|STARTING
import|;
end_import

begin_import
import|import static
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
operator|.
name|STOPPING
import|;
end_import

begin_import
import|import static
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
operator|.
name|TERMINATED
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
name|util
operator|.
name|concurrent
operator|.
name|ListenerCallQueue
operator|.
name|Callback
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
name|Monitor
operator|.
name|Guard
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
name|com
operator|.
name|google
operator|.
name|j2objc
operator|.
name|annotations
operator|.
name|WeakOuter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

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

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|concurrent
operator|.
name|Immutable
import|;
end_import

begin_comment
comment|/**  * Base class for implementing services that can handle {@link #doStart} and {@link #doStop}  * requests, responding to them with {@link #notifyStarted()} and {@link #notifyStopped()}  * callbacks. Its subclasses must manage threads manually; consider  * {@link AbstractExecutionThreadService} if you need only a single execution thread.  *  * @author Jesse Wilson  * @author Luke Sandberg  * @since 1.0  */
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
DECL|field|STARTING_CALLBACK
specifier|private
specifier|static
specifier|final
name|Callback
argument_list|<
name|Listener
argument_list|>
name|STARTING_CALLBACK
init|=
operator|new
name|Callback
argument_list|<
name|Listener
argument_list|>
argument_list|(
literal|"starting()"
argument_list|)
block|{
annotation|@
name|Override
name|void
name|call
parameter_list|(
name|Listener
name|listener
parameter_list|)
block|{
name|listener
operator|.
name|starting
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
DECL|field|RUNNING_CALLBACK
specifier|private
specifier|static
specifier|final
name|Callback
argument_list|<
name|Listener
argument_list|>
name|RUNNING_CALLBACK
init|=
operator|new
name|Callback
argument_list|<
name|Listener
argument_list|>
argument_list|(
literal|"running()"
argument_list|)
block|{
annotation|@
name|Override
name|void
name|call
parameter_list|(
name|Listener
name|listener
parameter_list|)
block|{
name|listener
operator|.
name|running
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
DECL|field|STOPPING_FROM_STARTING_CALLBACK
specifier|private
specifier|static
specifier|final
name|Callback
argument_list|<
name|Listener
argument_list|>
name|STOPPING_FROM_STARTING_CALLBACK
init|=
name|stoppingCallback
argument_list|(
name|STARTING
argument_list|)
decl_stmt|;
DECL|field|STOPPING_FROM_RUNNING_CALLBACK
specifier|private
specifier|static
specifier|final
name|Callback
argument_list|<
name|Listener
argument_list|>
name|STOPPING_FROM_RUNNING_CALLBACK
init|=
name|stoppingCallback
argument_list|(
name|RUNNING
argument_list|)
decl_stmt|;
DECL|field|TERMINATED_FROM_NEW_CALLBACK
specifier|private
specifier|static
specifier|final
name|Callback
argument_list|<
name|Listener
argument_list|>
name|TERMINATED_FROM_NEW_CALLBACK
init|=
name|terminatedCallback
argument_list|(
name|NEW
argument_list|)
decl_stmt|;
DECL|field|TERMINATED_FROM_RUNNING_CALLBACK
specifier|private
specifier|static
specifier|final
name|Callback
argument_list|<
name|Listener
argument_list|>
name|TERMINATED_FROM_RUNNING_CALLBACK
init|=
name|terminatedCallback
argument_list|(
name|RUNNING
argument_list|)
decl_stmt|;
DECL|field|TERMINATED_FROM_STOPPING_CALLBACK
specifier|private
specifier|static
specifier|final
name|Callback
argument_list|<
name|Listener
argument_list|>
name|TERMINATED_FROM_STOPPING_CALLBACK
init|=
name|terminatedCallback
argument_list|(
name|STOPPING
argument_list|)
decl_stmt|;
DECL|method|terminatedCallback (final State from)
specifier|private
specifier|static
name|Callback
argument_list|<
name|Listener
argument_list|>
name|terminatedCallback
parameter_list|(
specifier|final
name|State
name|from
parameter_list|)
block|{
return|return
operator|new
name|Callback
argument_list|<
name|Listener
argument_list|>
argument_list|(
literal|"terminated({from = "
operator|+
name|from
operator|+
literal|"})"
argument_list|)
block|{
annotation|@
name|Override
name|void
name|call
parameter_list|(
name|Listener
name|listener
parameter_list|)
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
return|;
block|}
DECL|method|stoppingCallback (final State from)
specifier|private
specifier|static
name|Callback
argument_list|<
name|Listener
argument_list|>
name|stoppingCallback
parameter_list|(
specifier|final
name|State
name|from
parameter_list|)
block|{
return|return
operator|new
name|Callback
argument_list|<
name|Listener
argument_list|>
argument_list|(
literal|"stopping({from = "
operator|+
name|from
operator|+
literal|"})"
argument_list|)
block|{
annotation|@
name|Override
name|void
name|call
parameter_list|(
name|Listener
name|listener
parameter_list|)
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
return|;
block|}
DECL|field|monitor
specifier|private
specifier|final
name|Monitor
name|monitor
init|=
operator|new
name|Monitor
argument_list|()
decl_stmt|;
DECL|field|isStartable
specifier|private
specifier|final
name|Guard
name|isStartable
init|=
operator|new
name|IsStartableGuard
argument_list|()
decl_stmt|;
annotation|@
name|WeakOuter
DECL|class|IsStartableGuard
specifier|private
specifier|final
class|class
name|IsStartableGuard
extends|extends
name|Guard
block|{
DECL|method|IsStartableGuard ()
name|IsStartableGuard
parameter_list|()
block|{
name|super
argument_list|(
name|AbstractService
operator|.
name|this
operator|.
name|monitor
argument_list|)
expr_stmt|;
block|}
DECL|method|isSatisfied ()
annotation|@
name|Override
specifier|public
name|boolean
name|isSatisfied
parameter_list|()
block|{
return|return
name|state
argument_list|()
operator|==
name|NEW
return|;
block|}
block|}
DECL|field|isStoppable
specifier|private
specifier|final
name|Guard
name|isStoppable
init|=
operator|new
name|IsStoppableGuard
argument_list|()
decl_stmt|;
annotation|@
name|WeakOuter
DECL|class|IsStoppableGuard
specifier|private
specifier|final
class|class
name|IsStoppableGuard
extends|extends
name|Guard
block|{
DECL|method|IsStoppableGuard ()
name|IsStoppableGuard
parameter_list|()
block|{
name|super
argument_list|(
name|AbstractService
operator|.
name|this
operator|.
name|monitor
argument_list|)
expr_stmt|;
block|}
DECL|method|isSatisfied ()
annotation|@
name|Override
specifier|public
name|boolean
name|isSatisfied
parameter_list|()
block|{
return|return
name|state
argument_list|()
operator|.
name|compareTo
argument_list|(
name|RUNNING
argument_list|)
operator|<=
literal|0
return|;
block|}
block|}
DECL|field|hasReachedRunning
specifier|private
specifier|final
name|Guard
name|hasReachedRunning
init|=
operator|new
name|HasReachedRunningGuard
argument_list|()
decl_stmt|;
annotation|@
name|WeakOuter
DECL|class|HasReachedRunningGuard
specifier|private
specifier|final
class|class
name|HasReachedRunningGuard
extends|extends
name|Guard
block|{
DECL|method|HasReachedRunningGuard ()
name|HasReachedRunningGuard
parameter_list|()
block|{
name|super
argument_list|(
name|AbstractService
operator|.
name|this
operator|.
name|monitor
argument_list|)
expr_stmt|;
block|}
DECL|method|isSatisfied ()
annotation|@
name|Override
specifier|public
name|boolean
name|isSatisfied
parameter_list|()
block|{
return|return
name|state
argument_list|()
operator|.
name|compareTo
argument_list|(
name|RUNNING
argument_list|)
operator|>=
literal|0
return|;
block|}
block|}
DECL|field|isStopped
specifier|private
specifier|final
name|Guard
name|isStopped
init|=
operator|new
name|IsStoppedGuard
argument_list|()
decl_stmt|;
annotation|@
name|WeakOuter
DECL|class|IsStoppedGuard
specifier|private
specifier|final
class|class
name|IsStoppedGuard
extends|extends
name|Guard
block|{
DECL|method|IsStoppedGuard ()
name|IsStoppedGuard
parameter_list|()
block|{
name|super
argument_list|(
name|AbstractService
operator|.
name|this
operator|.
name|monitor
argument_list|)
expr_stmt|;
block|}
DECL|method|isSatisfied ()
annotation|@
name|Override
specifier|public
name|boolean
name|isSatisfied
parameter_list|()
block|{
return|return
name|state
argument_list|()
operator|.
name|isTerminal
argument_list|()
return|;
block|}
block|}
comment|/**    * The listeners to notify during a state transition.    */
annotation|@
name|GuardedBy
argument_list|(
literal|"monitor"
argument_list|)
DECL|field|listeners
specifier|private
specifier|final
name|List
argument_list|<
name|ListenerCallQueue
argument_list|<
name|Listener
argument_list|>
argument_list|>
name|listeners
init|=
name|Collections
operator|.
name|synchronizedList
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|ListenerCallQueue
argument_list|<
name|Listener
argument_list|>
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
comment|/**    * The current state of the service.  This should be written with the lock held but can be read    * without it because it is an immutable object in a volatile field.  This is desirable so that    * methods like {@link #state}, {@link #failureCause} and notably {@link #toString} can be run    * without grabbing the lock.      *     *<p>To update this field correctly the lock must be held to guarantee that the state is     * consistent.    */
annotation|@
name|GuardedBy
argument_list|(
literal|"monitor"
argument_list|)
DECL|field|snapshot
specifier|private
specifier|volatile
name|StateSnapshot
name|snapshot
init|=
operator|new
name|StateSnapshot
argument_list|(
name|NEW
argument_list|)
decl_stmt|;
comment|/** Constructor for use by subclasses. */
DECL|method|AbstractService ()
specifier|protected
name|AbstractService
parameter_list|()
block|{}
comment|/**    * This method is called by {@link #startAsync} to initiate service startup. The invocation of     * this method should cause a call to {@link #notifyStarted()}, either during this method's run,    * or after it has returned. If startup fails, the invocation should cause a call to    * {@link #notifyFailed(Throwable)} instead.    *    *<p>This method should return promptly; prefer to do work on a different thread where it is    * convenient. It is invoked exactly once on service startup, even when {@link #startAsync} is     * called multiple times.    */
DECL|method|doStart ()
specifier|protected
specifier|abstract
name|void
name|doStart
parameter_list|()
function_decl|;
comment|/**    * This method should be used to initiate service shutdown. The invocation of this method should    * cause a call to {@link #notifyStopped()}, either during this method's run, or after it has    * returned. If shutdown fails, the invocation should cause a call to    * {@link #notifyFailed(Throwable)} instead.    *    *<p> This method should return promptly; prefer to do work on a different thread where it is    * convenient. It is invoked exactly once on service shutdown, even when {@link #stopAsync} is     * called multiple times.    */
DECL|method|doStop ()
specifier|protected
specifier|abstract
name|void
name|doStop
parameter_list|()
function_decl|;
DECL|method|startAsync ()
annotation|@
name|Override
specifier|public
specifier|final
name|Service
name|startAsync
parameter_list|()
block|{
if|if
condition|(
name|monitor
operator|.
name|enterIf
argument_list|(
name|isStartable
argument_list|)
condition|)
block|{
try|try
block|{
name|snapshot
operator|=
operator|new
name|StateSnapshot
argument_list|(
name|STARTING
argument_list|)
expr_stmt|;
name|starting
argument_list|()
expr_stmt|;
name|doStart
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|startupFailure
parameter_list|)
block|{
name|notifyFailed
argument_list|(
name|startupFailure
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|monitor
operator|.
name|leave
argument_list|()
expr_stmt|;
name|executeListeners
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Service "
operator|+
name|this
operator|+
literal|" has already been started"
argument_list|)
throw|;
block|}
return|return
name|this
return|;
block|}
DECL|method|stopAsync ()
annotation|@
name|Override
specifier|public
specifier|final
name|Service
name|stopAsync
parameter_list|()
block|{
if|if
condition|(
name|monitor
operator|.
name|enterIf
argument_list|(
name|isStoppable
argument_list|)
condition|)
block|{
try|try
block|{
name|State
name|previous
init|=
name|state
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|previous
condition|)
block|{
case|case
name|NEW
case|:
name|snapshot
operator|=
operator|new
name|StateSnapshot
argument_list|(
name|TERMINATED
argument_list|)
expr_stmt|;
name|terminated
argument_list|(
name|NEW
argument_list|)
expr_stmt|;
break|break;
case|case
name|STARTING
case|:
name|snapshot
operator|=
operator|new
name|StateSnapshot
argument_list|(
name|STARTING
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|stopping
argument_list|(
name|STARTING
argument_list|)
expr_stmt|;
break|break;
case|case
name|RUNNING
case|:
name|snapshot
operator|=
operator|new
name|StateSnapshot
argument_list|(
name|STOPPING
argument_list|)
expr_stmt|;
name|stopping
argument_list|(
name|RUNNING
argument_list|)
expr_stmt|;
name|doStop
argument_list|()
expr_stmt|;
break|break;
case|case
name|STOPPING
case|:
case|case
name|TERMINATED
case|:
case|case
name|FAILED
case|:
comment|// These cases are impossible due to the if statement above.
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"isStoppable is incorrectly implemented, saw: "
operator|+
name|previous
argument_list|)
throw|;
default|default:
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"Unexpected state: "
operator|+
name|previous
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|shutdownFailure
parameter_list|)
block|{
name|notifyFailed
argument_list|(
name|shutdownFailure
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|monitor
operator|.
name|leave
argument_list|()
expr_stmt|;
name|executeListeners
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|this
return|;
block|}
DECL|method|awaitRunning ()
annotation|@
name|Override
specifier|public
specifier|final
name|void
name|awaitRunning
parameter_list|()
block|{
name|monitor
operator|.
name|enterWhenUninterruptibly
argument_list|(
name|hasReachedRunning
argument_list|)
expr_stmt|;
try|try
block|{
name|checkCurrentState
argument_list|(
name|RUNNING
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|monitor
operator|.
name|leave
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|awaitRunning (long timeout, TimeUnit unit)
annotation|@
name|Override
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
if|if
condition|(
name|monitor
operator|.
name|enterWhenUninterruptibly
argument_list|(
name|hasReachedRunning
argument_list|,
name|timeout
argument_list|,
name|unit
argument_list|)
condition|)
block|{
try|try
block|{
name|checkCurrentState
argument_list|(
name|RUNNING
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|monitor
operator|.
name|leave
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// It is possible due to races the we are currently in the expected state even though we
comment|// timed out. e.g. if we weren't event able to grab the lock within the timeout we would never
comment|// even check the guard.  I don't think we care too much about this use case but it could lead
comment|// to a confusing error message.
throw|throw
operator|new
name|TimeoutException
argument_list|(
literal|"Timed out waiting for "
operator|+
name|this
operator|+
literal|" to reach the RUNNING state. "
operator|+
literal|"Current state: "
operator|+
name|state
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|awaitTerminated ()
annotation|@
name|Override
specifier|public
specifier|final
name|void
name|awaitTerminated
parameter_list|()
block|{
name|monitor
operator|.
name|enterWhenUninterruptibly
argument_list|(
name|isStopped
argument_list|)
expr_stmt|;
try|try
block|{
name|checkCurrentState
argument_list|(
name|TERMINATED
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|monitor
operator|.
name|leave
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|awaitTerminated (long timeout, TimeUnit unit)
annotation|@
name|Override
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
if|if
condition|(
name|monitor
operator|.
name|enterWhenUninterruptibly
argument_list|(
name|isStopped
argument_list|,
name|timeout
argument_list|,
name|unit
argument_list|)
condition|)
block|{
try|try
block|{
name|checkCurrentState
argument_list|(
name|TERMINATED
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|monitor
operator|.
name|leave
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// It is possible due to races the we are currently in the expected state even though we
comment|// timed out. e.g. if we weren't event able to grab the lock within the timeout we would never
comment|// even check the guard.  I don't think we care too much about this use case but it could lead
comment|// to a confusing error message.
throw|throw
operator|new
name|TimeoutException
argument_list|(
literal|"Timed out waiting for "
operator|+
name|this
operator|+
literal|" to reach a terminal state. "
operator|+
literal|"Current state: "
operator|+
name|state
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/** Checks that the current state is equal to the expected state. */
annotation|@
name|GuardedBy
argument_list|(
literal|"monitor"
argument_list|)
DECL|method|checkCurrentState (State expected)
specifier|private
name|void
name|checkCurrentState
parameter_list|(
name|State
name|expected
parameter_list|)
block|{
name|State
name|actual
init|=
name|state
argument_list|()
decl_stmt|;
if|if
condition|(
name|actual
operator|!=
name|expected
condition|)
block|{
if|if
condition|(
name|actual
operator|==
name|FAILED
condition|)
block|{
comment|// Handle this specially so that we can include the failureCause, if there is one.
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Expected the service to be "
operator|+
name|expected
operator|+
literal|", but the service has FAILED"
argument_list|,
name|failureCause
argument_list|()
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Expected the service to be "
operator|+
name|expected
operator|+
literal|", but was "
operator|+
name|actual
argument_list|)
throw|;
block|}
block|}
comment|/**    * Implementing classes should invoke this method once their service has started. It will cause    * the service to transition from {@link State#STARTING} to {@link State#RUNNING}.    *    * @throws IllegalStateException if the service is not {@link State#STARTING}.    */
DECL|method|notifyStarted ()
specifier|protected
specifier|final
name|void
name|notifyStarted
parameter_list|()
block|{
name|monitor
operator|.
name|enter
argument_list|()
expr_stmt|;
try|try
block|{
comment|// We have to examine the internal state of the snapshot here to properly handle the stop
comment|// while starting case.
if|if
condition|(
name|snapshot
operator|.
name|state
operator|!=
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
name|snapshot
operator|.
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
if|if
condition|(
name|snapshot
operator|.
name|shutdownWhenStartupFinishes
condition|)
block|{
name|snapshot
operator|=
operator|new
name|StateSnapshot
argument_list|(
name|STOPPING
argument_list|)
expr_stmt|;
comment|// We don't call listeners here because we already did that when we set the
comment|// shutdownWhenStartupFinishes flag.
name|doStop
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|snapshot
operator|=
operator|new
name|StateSnapshot
argument_list|(
name|RUNNING
argument_list|)
expr_stmt|;
name|running
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|monitor
operator|.
name|leave
argument_list|()
expr_stmt|;
name|executeListeners
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Implementing classes should invoke this method once their service has stopped. It will cause    * the service to transition from {@link State#STOPPING} to {@link State#TERMINATED}.    *    * @throws IllegalStateException if the service is neither {@link State#STOPPING} nor    *         {@link State#RUNNING}.    */
DECL|method|notifyStopped ()
specifier|protected
specifier|final
name|void
name|notifyStopped
parameter_list|()
block|{
name|monitor
operator|.
name|enter
argument_list|()
expr_stmt|;
try|try
block|{
comment|// We check the internal state of the snapshot instead of state() directly so we don't allow
comment|// notifyStopped() to be called while STARTING, even if stop() has already been called.
name|State
name|previous
init|=
name|snapshot
operator|.
name|state
decl_stmt|;
if|if
condition|(
name|previous
operator|!=
name|STOPPING
operator|&&
name|previous
operator|!=
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
name|previous
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
name|snapshot
operator|=
operator|new
name|StateSnapshot
argument_list|(
name|TERMINATED
argument_list|)
expr_stmt|;
name|terminated
argument_list|(
name|previous
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|monitor
operator|.
name|leave
argument_list|()
expr_stmt|;
name|executeListeners
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Invoke this method to transition the service to the {@link State#FAILED}. The service will    *<b>not be stopped</b> if it is running. Invoke this method when a service has failed critically    * or otherwise cannot be started nor stopped.    */
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
name|monitor
operator|.
name|enter
argument_list|()
expr_stmt|;
try|try
block|{
name|State
name|previous
init|=
name|state
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|previous
condition|)
block|{
case|case
name|NEW
case|:
case|case
name|TERMINATED
case|:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Failed while in state:"
operator|+
name|previous
argument_list|,
name|cause
argument_list|)
throw|;
case|case
name|RUNNING
case|:
case|case
name|STARTING
case|:
case|case
name|STOPPING
case|:
name|snapshot
operator|=
operator|new
name|StateSnapshot
argument_list|(
name|FAILED
argument_list|,
literal|false
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|failed
argument_list|(
name|previous
argument_list|,
name|cause
argument_list|)
expr_stmt|;
break|break;
case|case
name|FAILED
case|:
comment|// Do nothing
break|break;
default|default:
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"Unexpected state: "
operator|+
name|previous
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|monitor
operator|.
name|leave
argument_list|()
expr_stmt|;
name|executeListeners
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
return|return
name|snapshot
operator|.
name|externalState
argument_list|()
return|;
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
name|snapshot
operator|.
name|failureCause
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
name|monitor
operator|.
name|enter
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|state
argument_list|()
operator|.
name|isTerminal
argument_list|()
condition|)
block|{
name|listeners
operator|.
name|add
argument_list|(
operator|new
name|ListenerCallQueue
argument_list|<
name|Listener
argument_list|>
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
name|monitor
operator|.
name|leave
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
comment|/**     * Attempts to execute all the listeners in {@link #listeners} while not holding the    * {@link #monitor}.    */
DECL|method|executeListeners ()
specifier|private
name|void
name|executeListeners
parameter_list|()
block|{
if|if
condition|(
operator|!
name|monitor
operator|.
name|isOccupiedByCurrentThread
argument_list|()
condition|)
block|{
comment|// iterate by index to avoid concurrent modification exceptions
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|listeners
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|listeners
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|GuardedBy
argument_list|(
literal|"monitor"
argument_list|)
DECL|method|starting ()
specifier|private
name|void
name|starting
parameter_list|()
block|{
name|STARTING_CALLBACK
operator|.
name|enqueueOn
argument_list|(
name|listeners
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GuardedBy
argument_list|(
literal|"monitor"
argument_list|)
DECL|method|running ()
specifier|private
name|void
name|running
parameter_list|()
block|{
name|RUNNING_CALLBACK
operator|.
name|enqueueOn
argument_list|(
name|listeners
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GuardedBy
argument_list|(
literal|"monitor"
argument_list|)
DECL|method|stopping (final State from)
specifier|private
name|void
name|stopping
parameter_list|(
specifier|final
name|State
name|from
parameter_list|)
block|{
if|if
condition|(
name|from
operator|==
name|State
operator|.
name|STARTING
condition|)
block|{
name|STOPPING_FROM_STARTING_CALLBACK
operator|.
name|enqueueOn
argument_list|(
name|listeners
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|from
operator|==
name|State
operator|.
name|RUNNING
condition|)
block|{
name|STOPPING_FROM_RUNNING_CALLBACK
operator|.
name|enqueueOn
argument_list|(
name|listeners
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
annotation|@
name|GuardedBy
argument_list|(
literal|"monitor"
argument_list|)
DECL|method|terminated (final State from)
specifier|private
name|void
name|terminated
parameter_list|(
specifier|final
name|State
name|from
parameter_list|)
block|{
switch|switch
condition|(
name|from
condition|)
block|{
case|case
name|NEW
case|:
name|TERMINATED_FROM_NEW_CALLBACK
operator|.
name|enqueueOn
argument_list|(
name|listeners
argument_list|)
expr_stmt|;
break|break;
case|case
name|RUNNING
case|:
name|TERMINATED_FROM_RUNNING_CALLBACK
operator|.
name|enqueueOn
argument_list|(
name|listeners
argument_list|)
expr_stmt|;
break|break;
case|case
name|STOPPING
case|:
name|TERMINATED_FROM_STOPPING_CALLBACK
operator|.
name|enqueueOn
argument_list|(
name|listeners
argument_list|)
expr_stmt|;
break|break;
case|case
name|STARTING
case|:
case|case
name|TERMINATED
case|:
case|case
name|FAILED
case|:
default|default:
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
annotation|@
name|GuardedBy
argument_list|(
literal|"monitor"
argument_list|)
DECL|method|failed (final State from, final Throwable cause)
specifier|private
name|void
name|failed
parameter_list|(
specifier|final
name|State
name|from
parameter_list|,
specifier|final
name|Throwable
name|cause
parameter_list|)
block|{
comment|// can't memoize this one due to the exception
operator|new
name|Callback
argument_list|<
name|Listener
argument_list|>
argument_list|(
literal|"failed({from = "
operator|+
name|from
operator|+
literal|", cause = "
operator|+
name|cause
operator|+
literal|"})"
argument_list|)
block|{
annotation|@
name|Override
name|void
name|call
parameter_list|(
name|Listener
name|listener
parameter_list|)
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
block|}
operator|.
name|enqueueOn
argument_list|(
name|listeners
argument_list|)
expr_stmt|;
block|}
comment|/**    * An immutable snapshot of the current state of the service. This class represents a consistent    * snapshot of the state and therefore it can be used to answer simple queries without needing to    * grab a lock.    */
annotation|@
name|Immutable
DECL|class|StateSnapshot
specifier|private
specifier|static
specifier|final
class|class
name|StateSnapshot
block|{
comment|/**      * The internal state, which equals external state unless      * shutdownWhenStartupFinishes is true.      */
DECL|field|state
specifier|final
name|State
name|state
decl_stmt|;
comment|/**      * If true, the user requested a shutdown while the service was still starting      * up.      */
DECL|field|shutdownWhenStartupFinishes
specifier|final
name|boolean
name|shutdownWhenStartupFinishes
decl_stmt|;
comment|/**      * The exception that caused this service to fail.  This will be {@code null}      * unless the service has failed.      */
annotation|@
name|Nullable
DECL|field|failure
specifier|final
name|Throwable
name|failure
decl_stmt|;
DECL|method|StateSnapshot (State internalState)
name|StateSnapshot
parameter_list|(
name|State
name|internalState
parameter_list|)
block|{
name|this
argument_list|(
name|internalState
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|StateSnapshot ( State internalState, boolean shutdownWhenStartupFinishes, @Nullable Throwable failure)
name|StateSnapshot
parameter_list|(
name|State
name|internalState
parameter_list|,
name|boolean
name|shutdownWhenStartupFinishes
parameter_list|,
annotation|@
name|Nullable
name|Throwable
name|failure
parameter_list|)
block|{
name|checkArgument
argument_list|(
operator|!
name|shutdownWhenStartupFinishes
operator|||
name|internalState
operator|==
name|STARTING
argument_list|,
literal|"shudownWhenStartupFinishes can only be set if state is STARTING. Got %s instead."
argument_list|,
name|internalState
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
operator|!
operator|(
name|failure
operator|!=
literal|null
operator|^
name|internalState
operator|==
name|FAILED
operator|)
argument_list|,
literal|"A failure cause should be set if and only if the state is failed.  Got %s and %s "
operator|+
literal|"instead."
argument_list|,
name|internalState
argument_list|,
name|failure
argument_list|)
expr_stmt|;
name|this
operator|.
name|state
operator|=
name|internalState
expr_stmt|;
name|this
operator|.
name|shutdownWhenStartupFinishes
operator|=
name|shutdownWhenStartupFinishes
expr_stmt|;
name|this
operator|.
name|failure
operator|=
name|failure
expr_stmt|;
block|}
comment|/** @see Service#state() */
DECL|method|externalState ()
name|State
name|externalState
parameter_list|()
block|{
if|if
condition|(
name|shutdownWhenStartupFinishes
operator|&&
name|state
operator|==
name|STARTING
condition|)
block|{
return|return
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
comment|/** @see Service#failureCause() */
DECL|method|failureCause ()
name|Throwable
name|failureCause
parameter_list|()
block|{
name|checkState
argument_list|(
name|state
operator|==
name|FAILED
argument_list|,
literal|"failureCause() is only valid if the service has failed, service is %s"
argument_list|,
name|state
argument_list|)
expr_stmt|;
return|return
name|failure
return|;
block|}
block|}
block|}
end_class

end_unit

