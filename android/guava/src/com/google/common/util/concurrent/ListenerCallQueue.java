begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2014 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|Preconditions
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
name|Queues
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
name|Queue
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
name|concurrent
operator|.
name|GuardedBy
import|;
end_import

begin_comment
comment|/**  * A list of listeners for implementing a concurrency friendly observable object.  *  *<p>Listeners are registered once via {@link #addListener} and then may be invoked by {@linkplain  * #enqueue enqueueing} and then {@linkplain #dispatch dispatching} events.  *  *<p>The API of this class is designed to make it easy to achieve the following properties  *  *<ul>  *<li>Multiple events for the same listener are never dispatched concurrently.  *<li>Events for the different listeners are dispatched concurrently.  *<li>All events for a given listener dispatch on the provided {@link #executor}.  *<li>It is easy for the user to ensure that listeners are never invoked while holding locks.  *</ul>  *  * The last point is subtle. Often the observable object will be managing its own internal state  * using a lock, however it is dangerous to dispatch listeners while holding a lock because they  * might run on the {@code directExecutor()} or be otherwise re-entrant (call back into your  * object). So it is important to not call {@link #dispatch} while holding any locks. This is why  * {@link #enqueue} and {@link #dispatch} are 2 different methods. It is expected that the decision  * to run a particular event is made during the state change, but the decision to actually invoke  * the listeners can be delayed slightly so that locks can be dropped. Also, because {@link  * #dispatch} is expected to be called concurrently, it is idempotent.  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|ListenerCallQueue
specifier|final
class|class
name|ListenerCallQueue
parameter_list|<
name|L
parameter_list|>
block|{
comment|// TODO(cpovirk): consider using the logger associated with listener.getClass().
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
name|ListenerCallQueue
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|// TODO(chrisn): promote AppendOnlyCollection for use here.
DECL|field|listeners
specifier|private
specifier|final
name|List
argument_list|<
name|PerListenerQueue
argument_list|<
name|L
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
name|PerListenerQueue
argument_list|<
name|L
argument_list|>
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
comment|/** Method reference-compatible listener event. */
DECL|interface|Event
interface|interface
name|Event
parameter_list|<
name|L
parameter_list|>
block|{
comment|/** Call a method on the listener. */
DECL|method|call (L listener)
name|void
name|call
parameter_list|(
name|L
name|listener
parameter_list|)
function_decl|;
block|}
comment|/**    * Adds a listener that will be called using the given executor when events are later {@link    * #enqueue enqueued} and {@link #dispatch dispatched}.    */
DECL|method|addListener (L listener, Executor executor)
specifier|public
name|void
name|addListener
parameter_list|(
name|L
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
name|listeners
operator|.
name|add
argument_list|(
operator|new
name|PerListenerQueue
argument_list|<>
argument_list|(
name|listener
argument_list|,
name|executor
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Enqueues an event to be run on currently known listeners.    *    *<p>The {@code toString} method of the Event itself will be used to describe the event in the    * case of an error.    *    * @param event the callback to execute on {@link #dispatch}    */
DECL|method|enqueue (Event<L> event)
specifier|public
name|void
name|enqueue
parameter_list|(
name|Event
argument_list|<
name|L
argument_list|>
name|event
parameter_list|)
block|{
name|enqueueHelper
argument_list|(
name|event
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
comment|/**    * Enqueues an event to be run on currently known listeners, with a label.    *    * @param event the callback to execute on {@link #dispatch}    * @param label a description of the event to use in the case of an error    */
DECL|method|enqueue (Event<L> event, String label)
specifier|public
name|void
name|enqueue
parameter_list|(
name|Event
argument_list|<
name|L
argument_list|>
name|event
parameter_list|,
name|String
name|label
parameter_list|)
block|{
name|enqueueHelper
argument_list|(
name|event
argument_list|,
name|label
argument_list|)
expr_stmt|;
block|}
DECL|method|enqueueHelper (Event<L> event, Object label)
specifier|private
name|void
name|enqueueHelper
parameter_list|(
name|Event
argument_list|<
name|L
argument_list|>
name|event
parameter_list|,
name|Object
name|label
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|event
argument_list|,
literal|"event"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|label
argument_list|,
literal|"label"
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|listeners
init|)
block|{
for|for
control|(
name|PerListenerQueue
argument_list|<
name|L
argument_list|>
name|queue
range|:
name|listeners
control|)
block|{
name|queue
operator|.
name|add
argument_list|(
name|event
argument_list|,
name|label
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Dispatches all events enqueued prior to this call, serially and in order, for every listener.    *    *<p>Note: this method is idempotent and safe to call from any thread    */
DECL|method|dispatch ()
specifier|public
name|void
name|dispatch
parameter_list|()
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
name|dispatch
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * A special purpose queue/executor that dispatches listener events serially on a configured    * executor. Each event event can be added and dispatched as separate phases.    *    *<p>This class is very similar to {@link SequentialExecutor} with the exception that events can    * be added without necessarily executing immediately.    */
DECL|class|PerListenerQueue
specifier|private
specifier|static
specifier|final
class|class
name|PerListenerQueue
parameter_list|<
name|L
parameter_list|>
implements|implements
name|Runnable
block|{
DECL|field|listener
specifier|final
name|L
name|listener
decl_stmt|;
DECL|field|executor
specifier|final
name|Executor
name|executor
decl_stmt|;
annotation|@
name|GuardedBy
argument_list|(
literal|"this"
argument_list|)
DECL|field|waitQueue
specifier|final
name|Queue
argument_list|<
name|ListenerCallQueue
operator|.
name|Event
argument_list|<
name|L
argument_list|>
argument_list|>
name|waitQueue
init|=
name|Queues
operator|.
name|newArrayDeque
argument_list|()
decl_stmt|;
annotation|@
name|GuardedBy
argument_list|(
literal|"this"
argument_list|)
DECL|field|labelQueue
specifier|final
name|Queue
argument_list|<
name|Object
argument_list|>
name|labelQueue
init|=
name|Queues
operator|.
name|newArrayDeque
argument_list|()
decl_stmt|;
annotation|@
name|GuardedBy
argument_list|(
literal|"this"
argument_list|)
DECL|field|isThreadScheduled
name|boolean
name|isThreadScheduled
decl_stmt|;
DECL|method|PerListenerQueue (L listener, Executor executor)
name|PerListenerQueue
parameter_list|(
name|L
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
name|checkNotNull
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|checkNotNull
argument_list|(
name|executor
argument_list|)
expr_stmt|;
block|}
comment|/** Enqueues a event to be run. */
DECL|method|add (ListenerCallQueue.Event<L> event, Object label)
specifier|synchronized
name|void
name|add
parameter_list|(
name|ListenerCallQueue
operator|.
name|Event
argument_list|<
name|L
argument_list|>
name|event
parameter_list|,
name|Object
name|label
parameter_list|)
block|{
name|waitQueue
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|labelQueue
operator|.
name|add
argument_list|(
name|label
argument_list|)
expr_stmt|;
block|}
comment|/**      * Dispatches all listeners {@linkplain #enqueue enqueued} prior to this call, serially and in      * order.      */
DECL|method|dispatch ()
name|void
name|dispatch
parameter_list|()
block|{
name|boolean
name|scheduleEventRunner
init|=
literal|false
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
operator|!
name|isThreadScheduled
condition|)
block|{
name|isThreadScheduled
operator|=
literal|true
expr_stmt|;
name|scheduleEventRunner
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|scheduleEventRunner
condition|)
block|{
try|try
block|{
name|executor
operator|.
name|execute
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
comment|// reset state in case of an error so that later dispatch calls will actually do something
synchronized|synchronized
init|(
name|this
init|)
block|{
name|isThreadScheduled
operator|=
literal|false
expr_stmt|;
block|}
comment|// Log it and keep going.
name|logger
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"Exception while running callbacks for "
operator|+
name|listener
operator|+
literal|" on "
operator|+
name|executor
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|boolean
name|stillRunning
init|=
literal|true
decl_stmt|;
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
name|ListenerCallQueue
operator|.
name|Event
argument_list|<
name|L
argument_list|>
name|nextToRun
decl_stmt|;
name|Object
name|nextLabel
decl_stmt|;
synchronized|synchronized
init|(
name|PerListenerQueue
operator|.
name|this
init|)
block|{
name|Preconditions
operator|.
name|checkState
argument_list|(
name|isThreadScheduled
argument_list|)
expr_stmt|;
name|nextToRun
operator|=
name|waitQueue
operator|.
name|poll
argument_list|()
expr_stmt|;
name|nextLabel
operator|=
name|labelQueue
operator|.
name|poll
argument_list|()
expr_stmt|;
if|if
condition|(
name|nextToRun
operator|==
literal|null
condition|)
block|{
name|isThreadScheduled
operator|=
literal|false
expr_stmt|;
name|stillRunning
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
comment|// Always run while _not_ holding the lock, to avoid deadlocks.
try|try
block|{
name|nextToRun
operator|.
name|call
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
comment|// Log it and keep going.
name|logger
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"Exception while executing callback: "
operator|+
name|listener
operator|+
literal|" "
operator|+
name|nextLabel
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|stillRunning
condition|)
block|{
comment|// An Error is bubbling up. We should mark ourselves as no longer running. That way, if
comment|// anyone tries to keep using us, we won't be corrupted.
synchronized|synchronized
init|(
name|PerListenerQueue
operator|.
name|this
init|)
block|{
name|isThreadScheduled
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

