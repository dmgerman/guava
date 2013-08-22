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

begin_comment
comment|/**  * An object with an operational state, plus asynchronous {@link #startAsync()} and  * {@link #stopAsync()} lifecycle methods to transition between states. Example services include  * webservers, RPC servers and timers.  *  *<p>The normal lifecycle of a service is:  *<ul>  *<li>{@linkplain State#NEW NEW} -&gt;  *<li>{@linkplain State#STARTING STARTING} -&gt;  *<li>{@linkplain State#RUNNING RUNNING} -&gt;  *<li>{@linkplain State#STOPPING STOPPING} -&gt;  *<li>{@linkplain State#TERMINATED TERMINATED}  *</ul>  *  *<p>There are deviations from this if there are failures or if {@link Service#stopAsync} is called  * before the {@link Service} reaches the {@linkplain State#RUNNING RUNNING} state. The set of legal  * transitions form a<a href="http://en.wikipedia.org/wiki/Directed_acyclic_graph">DAG</a>,  * therefore every method of the listener will be called at most once. N.B. The {@link State#FAILED}  * and {@link State#TERMINATED} states are terminal states, once a service enters either of these  * states it cannot ever leave them.  *  *<p>Implementors of this interface are strongly encouraged to extend one of the abstract classes  * in this package which implement this interface and make the threading and state management  * easier.  *  * @author Jesse Wilson  * @author Luke Sandberg  * @since 9.0 (in 1.0 as {@code com.google.common.base.Service})  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|Service
specifier|public
interface|interface
name|Service
block|{
comment|/**    * If the service state is {@link State#NEW}, this initiates service startup and returns    * immediately. If the service has already been started, this method returns immediately without    * taking action. A stopped service may not be restarted.    *    * @deprecated Use {@link #startAsync()} instead of this method to start the {@link Service} or    * use a {@link Listener} to asynchronously wait for service startup.    *    * @return a future for the startup result, regardless of whether this call initiated startup.    *         Calling {@link ListenableFuture#get} will block until the service has finished    *         starting, and returns one of {@link State#RUNNING}, {@link State#STOPPING} or    *         {@link State#TERMINATED}. If the service fails to start, {@link ListenableFuture#get}    *         will throw an {@link ExecutionException}, and the service's state will be    *         {@link State#FAILED}. If it has already finished starting, {@link ListenableFuture#get}    *         returns immediately. Cancelling this future has no effect on the service.    */
DECL|method|start ()
annotation|@
name|Deprecated
name|ListenableFuture
argument_list|<
name|State
argument_list|>
name|start
parameter_list|()
function_decl|;
comment|/**    * Initiates service startup (if necessary), returning once the service has finished starting.    * Unlike calling {@code start().get()}, this method throws no checked exceptions, and it cannot    * be {@linkplain Thread#interrupt interrupted}.    *    * @deprecated Use {@link #startAsync()} and {@link #awaitRunning} instead of this method.    *    * @throws UncheckedExecutionException if startup failed    * @return the state of the service when startup finished.    */
DECL|method|startAndWait ()
annotation|@
name|Deprecated
name|State
name|startAndWait
parameter_list|()
function_decl|;
comment|/**    * If the service state is {@link State#NEW}, this initiates service startup and returns    * immediately. A stopped service may not be restarted.    *    * @return this    * @throws IllegalStateException if the service is not {@link State#NEW}    *    * @since 15.0    */
DECL|method|startAsync ()
name|Service
name|startAsync
parameter_list|()
function_decl|;
comment|/**    * Returns {@code true} if this service is {@linkplain State#RUNNING running}.    */
DECL|method|isRunning ()
name|boolean
name|isRunning
parameter_list|()
function_decl|;
comment|/**    * Returns the lifecycle state of the service.    */
DECL|method|state ()
name|State
name|state
parameter_list|()
function_decl|;
comment|/**    * If the service is {@linkplain State#STARTING starting} or {@linkplain State#RUNNING running},    * this initiates service shutdown and returns immediately. If the service is    * {@linkplain State#NEW new}, it is {@linkplain State#TERMINATED terminated} without having been    * started nor stopped. If the service has already been stopped, this method returns immediately    * without taking action.    *    * @deprecated Use {@link #stopAsync} instead of this method to initiate service shutdown or use a    * service {@link Listener} to asynchronously wait for service shutdown.    *    * @return a future for the shutdown result, regardless of whether this call initiated shutdown.    *         Calling {@link ListenableFuture#get} will block until the service has finished shutting    *         down, and either returns {@link State#TERMINATED} or throws an    *         {@link ExecutionException}. If it has already finished stopping,    *         {@link ListenableFuture#get} returns immediately. Cancelling this future has no effect    *         on the service.    */
DECL|method|stop ()
annotation|@
name|Deprecated
name|ListenableFuture
argument_list|<
name|State
argument_list|>
name|stop
parameter_list|()
function_decl|;
comment|/**    * Initiates service shutdown (if necessary), returning once the service has finished stopping. If    * this is {@link State#STARTING}, startup will be cancelled. If this is {@link State#NEW}, it is    * {@link State#TERMINATED terminated} without having been started nor stopped. Unlike calling    * {@code stop().get()}, this method throws no checked exceptions.    *    * @deprecated Use {@link #stopAsync} and {@link #awaitTerminated} instead of this method.    *    * @throws UncheckedExecutionException if the service has failed or fails during shutdown    * @return the state of the service when shutdown finished.    */
DECL|method|stopAndWait ()
annotation|@
name|Deprecated
name|State
name|stopAndWait
parameter_list|()
function_decl|;
comment|/**    * If the service is {@linkplain State#STARTING starting} or {@linkplain State#RUNNING running},    * this initiates service shutdown and returns immediately. If the service is    * {@linkplain State#NEW new}, it is {@linkplain State#TERMINATED terminated} without having been    * started nor stopped. If the service has already been stopped, this method returns immediately    * without taking action.    *    * @return this    * @since 15.0    */
DECL|method|stopAsync ()
name|Service
name|stopAsync
parameter_list|()
function_decl|;
comment|/**    * Waits for the {@link Service} to reach the {@linkplain State#RUNNING running state}.    *    * @throws IllegalStateException if the service reaches a state from which it is not possible to    *     enter the {@link State#RUNNING} state. e.g. if the {@code state} is    *     {@code State#TERMINATED} when this method is called then this will throw an    *     IllegalStateException.    *    * @since 15.0    */
DECL|method|awaitRunning ()
name|void
name|awaitRunning
parameter_list|()
function_decl|;
comment|/**    * Waits for the {@link Service} to reach the {@linkplain State#RUNNING running state} for no    * more than the given time.    *    * @param timeout the maximum time to wait    * @param unit the time unit of the timeout argument    * @throws TimeoutException if the service has not reached the given state within the deadline    * @throws IllegalStateException if the service reaches a state from which it is not possible to    *     enter the {@link State#RUNNING RUNNING} state. e.g. if the {@code state} is    *     {@code State#TERMINATED} when this method is called then this will throw an    *     IllegalStateException.    *    * @since 15.0    */
DECL|method|awaitRunning (long timeout, TimeUnit unit)
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
function_decl|;
comment|/**    * Waits for the {@link Service} to reach the {@linkplain State#TERMINATED terminated state}.    *    * @throws IllegalStateException if the service {@linkplain State#FAILED fails}.    *    * @since 15.0    */
DECL|method|awaitTerminated ()
name|void
name|awaitTerminated
parameter_list|()
function_decl|;
comment|/**    * Waits for the {@link Service} to reach a terminal state (either    * {@link Service.State#TERMINATED terminated} or {@link Service.State#FAILED failed}) for no    * more than the given time.    *    * @param timeout the maximum time to wait    * @param unit the time unit of the timeout argument    * @throws TimeoutException if the service has not reached the given state within the deadline    * @throws IllegalStateException if the service {@linkplain State#FAILED fails}.    * @since 15.0    */
DECL|method|awaitTerminated (long timeout, TimeUnit unit)
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
function_decl|;
comment|/**    * Returns the {@link Throwable} that caused this service to fail.    *    * @throws IllegalStateException if this service's state isn't {@linkplain State#FAILED FAILED}.    *    * @since 14.0    */
DECL|method|failureCause ()
name|Throwable
name|failureCause
parameter_list|()
function_decl|;
comment|/**    * Registers a {@link Listener} to be {@linkplain Executor#execute executed} on the given    * executor.  The listener will have the corresponding transition method called whenever the    * service changes state. The listener will not have previous state changes replayed, so it is    * suggested that listeners are added before the service starts.    *    *<p>There is no guaranteed ordering of execution of listeners, but any listener added through    * this method is guaranteed to be called whenever there is a state change.    *    *<p>Exceptions thrown by a listener will be propagated up to the executor. Any exception thrown    * during {@code Executor.execute} (e.g., a {@code RejectedExecutionException} or an exception    * thrown by {@linkplain MoreExecutors#sameThreadExecutor inline execution}) will be caught and    * logged.    *    * @param listener the listener to run when the service changes state is complete    * @param executor the executor in which the listeners callback methods will be run. For fast,    *     lightweight listeners that would be safe to execute in any thread, consider    *     {@link MoreExecutors#sameThreadExecutor}.    * @since 13.0    */
DECL|method|addListener (Listener listener, Executor executor)
name|void
name|addListener
parameter_list|(
name|Listener
name|listener
parameter_list|,
name|Executor
name|executor
parameter_list|)
function_decl|;
comment|/**    * The lifecycle states of a service.    *    *<p>The ordering of the {@link State} enum is defined such that if there is a state transition    * from {@code A -> B} then {@code A.compareTo(B}< 0}.  N.B. The converse is not true, i.e. if    * {@code A.compareTo(B}< 0} then there is<b>not</b> guaranteed to be a valid state transition    * {@code A -> B}.    *    * @since 9.0 (in 1.0 as {@code com.google.common.base.Service.State})    */
annotation|@
name|Beta
comment|// should come out of Beta when Service does
DECL|enum|State
enum|enum
name|State
block|{
comment|/**      * A service in this state is inactive. It does minimal work and consumes      * minimal resources.      */
DECL|enumConstant|NEW
name|NEW
block|{
annotation|@
name|Override
name|boolean
name|isTerminal
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
block|,
comment|/**      * A service in this state is transitioning to {@link #RUNNING}.      */
DECL|enumConstant|STARTING
name|STARTING
block|{
annotation|@
name|Override
name|boolean
name|isTerminal
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
block|,
comment|/**      * A service in this state is operational.      */
DECL|enumConstant|RUNNING
name|RUNNING
block|{
annotation|@
name|Override
name|boolean
name|isTerminal
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
block|,
comment|/**      * A service in this state is transitioning to {@link #TERMINATED}.      */
DECL|enumConstant|STOPPING
name|STOPPING
block|{
annotation|@
name|Override
name|boolean
name|isTerminal
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
block|,
comment|/**      * A service in this state has completed execution normally. It does minimal work and consumes      * minimal resources.      */
DECL|enumConstant|TERMINATED
name|TERMINATED
block|{
annotation|@
name|Override
name|boolean
name|isTerminal
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
block|,
comment|/**      * A service in this state has encountered a problem and may not be operational. It cannot be      * started nor stopped.      */
DECL|enumConstant|FAILED
name|FAILED
block|{
annotation|@
name|Override
name|boolean
name|isTerminal
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
block|;
comment|/** Returns true if this state is terminal. */
DECL|method|isTerminal ()
specifier|abstract
name|boolean
name|isTerminal
parameter_list|()
function_decl|;
block|}
comment|/**    * A listener for the various state changes that a {@link Service} goes through in its lifecycle.    *    *<p>All methods are no-ops by default, implementors should override the ones they care about.    *    * @author Luke Sandberg    * @since 15.0 (present as an interface in 13.0)    */
annotation|@
name|Beta
comment|// should come out of Beta when Service does
DECL|class|Listener
specifier|abstract
class|class
name|Listener
block|{
comment|/**      * Called when the service transitions from {@linkplain State#NEW NEW} to      * {@linkplain State#STARTING STARTING}. This occurs when {@link Service#start} or      * {@link Service#startAndWait} is called the first time.      */
DECL|method|starting ()
specifier|public
name|void
name|starting
parameter_list|()
block|{}
comment|/**      * Called when the service transitions from {@linkplain State#STARTING STARTING} to      * {@linkplain State#RUNNING RUNNING}. This occurs when a service has successfully started.      */
DECL|method|running ()
specifier|public
name|void
name|running
parameter_list|()
block|{}
comment|/**      * Called when the service transitions to the {@linkplain State#STOPPING STOPPING} state. The      * only valid values for {@code from} are {@linkplain State#STARTING STARTING} or      * {@linkplain State#RUNNING RUNNING}.  This occurs when {@link Service#stop} is called.      *      * @param from The previous state that is being transitioned from.      */
DECL|method|stopping (State from)
specifier|public
name|void
name|stopping
parameter_list|(
name|State
name|from
parameter_list|)
block|{}
comment|/**      * Called when the service transitions to the {@linkplain State#TERMINATED TERMINATED} state.      * The {@linkplain State#TERMINATED TERMINATED} state is a terminal state in the transition      * diagram.  Therefore, if this method is called, no other methods will be called on the      * {@link Listener}.      *      * @param from The previous state that is being transitioned from.  The only valid values for      *     this are {@linkplain State#NEW NEW}, {@linkplain State#RUNNING RUNNING} or      *     {@linkplain State#STOPPING STOPPING}.      */
DECL|method|terminated (State from)
specifier|public
name|void
name|terminated
parameter_list|(
name|State
name|from
parameter_list|)
block|{}
comment|/**      * Called when the service transitions to the {@linkplain State#FAILED FAILED} state. The      * {@linkplain State#FAILED FAILED} state is a terminal state in the transition diagram.      * Therefore, if this method is called, no other methods will be called on the {@link Listener}.      *      * @param from The previous state that is being transitioned from.  Failure can occur in any      *     state with the exception of {@linkplain State#NEW NEW} or      *     {@linkplain State#TERMINATED TERMINATED}.      * @param failure The exception that caused the failure.      */
DECL|method|failed (State from, Throwable failure)
specifier|public
name|void
name|failed
parameter_list|(
name|State
name|from
parameter_list|,
name|Throwable
name|failure
parameter_list|)
block|{}
block|}
block|}
end_interface

end_unit

