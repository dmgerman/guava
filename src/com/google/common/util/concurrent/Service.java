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
name|Future
import|;
end_import

begin_comment
comment|/**  * An object with an operational state, plus asynchronous {@link #start()} and  * {@link #stop()} lifecycle methods to transfer into and out of this state.  * Example services include webservers, RPC servers and timers. The normal  * lifecycle of a service is:  *<ul>  *<li>{@link State#NEW} -&gt;</li>  *<li>{@link State#STARTING} -&gt;</li>  *<li>{@link State#RUNNING} -&gt;</li>  *<li>{@link State#STOPPING} -&gt;</li>  *<li>{@link State#TERMINATED}</li>  *</ul>  *  * If the service fails while starting, running or stopping, its state will be  * {@link State#FAILED}, and its behavior is undefined. Such a service cannot be  * started nor stopped.  *  *<p>Implementors of this interface are strongly encouraged to extend {@link  * AbstractService}, {@link AbstractExecutionThreadService}, or {@link  * AbstractIdleService}, which make the threading and state management easier.  *  * @author Jesse Wilson  * @since 9 (in version 1 as {@code com.google.common.base.Service})  */
end_comment

begin_interface
annotation|@
name|Beta
comment|// TODO(kevinb): make abstract class?
DECL|interface|Service
specifier|public
interface|interface
name|Service
extends|extends
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Service
block|{
comment|/**    * If the service state is {@link State#NEW}, this initiates service startup    * and returns immediately. If the service has already been started, this    * method returns immediately without taking action. A stopped service may not    * be restarted.    *    * @return a future for the startup result, regardless of whether this call    *     initiated startup. Calling {@link Future#get} will block until the    *     service has finished starting, and returns one of {@link    *     State#RUNNING}, {@link State#STOPPING} or {@link State#TERMINATED}. If    *     the service fails to start, {@link Future#get} will throw an {@link    *     ExecutionException}, and the service's state will be {@link    *     State#FAILED}. If it has already finished starting, {@link Future#get}    *     returns immediately. Cancelling the returned future is unsupported and    *     always returns {@code false}.    */
DECL|method|start ()
name|Future
argument_list|<
name|State
argument_list|>
name|start
parameter_list|()
function_decl|;
comment|/**    * Initiates service startup (if necessary), returning once the service has    * finished starting. Unlike calling {@code start().get()}, this method throws    * no checked exceptions, and it cannot be {@linkplain Thread#interrupt    * interrupted}.    *    * @throws RuntimeException if startup failed    * @return the state of the service when startup finished.    */
DECL|method|startAndWait ()
name|State
name|startAndWait
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
comment|/**    * If the service is {@linkplain State#STARTING starting} or {@linkplain    * State#RUNNING running}, this initiates service shutdown and returns    * immediately. If the service is {@linkplain State#NEW new}, it is    * {@linkplain State#TERMINATED terminated} without having been started nor    * stopped.  If the service has already been stopped, this method returns    * immediately without taking action.    *    * @return a future for the shutdown result, regardless of whether this call    *     initiated shutdown. Calling {@link Future#get} will block until the    *     service has finished shutting down, and either returns {@link    *     State#TERMINATED} or throws an {@link ExecutionException}. If it has    *     already finished stopping, {@link Future#get} returns immediately.    *     Cancelling this future is unsupported and always returns {@code    *     false}.    */
DECL|method|stop ()
name|Future
argument_list|<
name|State
argument_list|>
name|stop
parameter_list|()
function_decl|;
comment|/**    * Initiates service shutdown (if necessary), returning once the service has    * finished stopping. If this is {@link State#STARTING}, startup will be    * cancelled. If this is {@link State#NEW}, it is {@link State#TERMINATED    * terminated} without having been started nor stopped. Unlike calling {@code    * stop().get()}, this method throws no checked exceptions.    *    * @throws InterruptedRuntimeException if the thread was interrupted while    *      waiting for the service to finish shutting down.    * @throws RuntimeException if shutdown failed    * @return the state of the service when shutdown finished.    */
DECL|method|stopAndWait ()
name|State
name|stopAndWait
parameter_list|()
function_decl|;
comment|// TODO(cpovirk): uncomment when removing base.Service
comment|//  /**
comment|//   * The lifecycle states of a service.
comment|//   *
comment|//   * @since 9 (in version 1 as {@code com.google.common.base.Service.State})
comment|//   */
comment|//  @Beta // should come out of Beta when Service does
comment|//  enum State {
comment|//    /**
comment|//     * A service in this state is inactive. It does minimal work and consumes
comment|//     * minimal resources.
comment|//     */
comment|//    NEW,
comment|//
comment|//    /**
comment|//     * A service in this state is transitioning to {@link #RUNNING}.
comment|//     */
comment|//    STARTING,
comment|//
comment|//    /**
comment|//     * A service in this state is operational.
comment|//     */
comment|//    RUNNING,
comment|//
comment|//    /**
comment|//     * A service in this state is transitioning to {@link #TERMINATED}.
comment|//     */
comment|//    STOPPING,
comment|//
comment|//    /**
comment|//     * A service in this state has completed execution normally. It does minimal
comment|//     * work and consumes minimal resources.
comment|//     */
comment|//    TERMINATED,
comment|//
comment|//    /**
comment|//     * A service in this state has encountered a problem and may not be
comment|//     * operational. It cannot be started nor stopped.
comment|//     */
comment|//    FAILED
comment|//  }
block|}
end_interface

end_unit

