begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Iterator
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
name|ConcurrentLinkedQueue
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
name|ThreadSafe
import|;
end_import

begin_comment
comment|/**  * A thread-safe queue of listeners, each with an associated {@code Executor}, that guarantees  * that every {@code Runnable} that is {@linkplain #add added} will be  * {@link Executor#execute(Runnable) executed} in the same order that it was added.  *  *<p>While similar in structure and API to {@link ExecutionList}, this class differs in several  * ways:  *  *<ul>  *<li>This class makes strict ordering guarantees. ExecutionList makes no ordering guarantees.  *<li>{@link ExecutionQueue#execute} executes all currently pending listeners. Later calls  *        to {@link ExecutionQueue#add} are delayed until the<em>next</em> call to execute.  *        {@link ExecutionList#execute()} executes all current listeners and also causes immediate  *        execution on subsequent calls to {@link ExecutionList#add}.  *</ul>  *  *<p>These differences make {@link ExecutionQueue} suitable for when you need to execute callbacks  * multiple times in response to different events. ExecutionList is suitable for when you have a  * single event.  *  *<p>For example, this implements a simple atomic data structure that lets a listener  * asynchronously listen to changes to a value:<pre>   {@code  *   interface CountListener {  *     void update(int v);  *   }  *  *   class AtomicListenableCounter {  *     private int value;  *     private final ExecutionQueue queue = new ExecutionQueue();  *     private final CountListener listener;  *     private final Executor executor;  *  *     AtomicListenableCounter(CountListener listener, Executor executor) {  *       this.listener = listener;  *       this.exeucutor = executor;  *     }  *  *     void add(int amt) {  *       synchronized (this) {  *         v += amt;  *         final int currentValue = v;  *         queue.add(new Runnable() {  *           public void run() {  *             listener.update(currentValue);  *           }  *         }, executor);  *       }  *       queue.execute();  *   }  * }}</pre>  *  *<p>This AtomicListenableCounter allows a listener to be run asynchronously on every update and  * the ExecutionQueue enforces that:  *  *<ul>  *<li>The listener is never run with the lock held (even if the executor is the  *       {@link MoreExecutors#sameThreadExecutor()})  *<li>The listeners are never run out of order  *<li>Each added listener is called only once.  *</ul>  *  *<p>Exceptions thrown by a listener will be propagated up to the executor. Any exception thrown  * during {@code Executor.execute} (e.g., a {@code RejectedExecutionException} or an exception  * thrown by {@linkplain MoreExecutors#sameThreadExecutor inline execution}) will be caught and  * logged.  *  * @author Luke Sandberg  */
end_comment

begin_class
annotation|@
name|ThreadSafe
DECL|class|ExecutionQueue
specifier|final
class|class
name|ExecutionQueue
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
name|ExecutionQueue
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|/** The listeners to execute in order.  */
DECL|field|queuedListeners
specifier|private
specifier|final
name|ConcurrentLinkedQueue
argument_list|<
name|RunnableExecutorPair
argument_list|>
name|queuedListeners
init|=
name|Queues
operator|.
name|newConcurrentLinkedQueue
argument_list|()
decl_stmt|;
comment|/**    * This lock is used with {@link RunnableExecutorPair#submit} to ensure that each listener is    * executed at most once.    */
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
comment|/**    * Adds the {@code Runnable} and accompanying {@code Executor} to the queue of listeners to    * execute.    *    *<p>Note: This method will never directly invoke {@code executor.execute(runnable)}, though your    * runnable may be executed before it returns if another thread has concurrently called    * {@link #execute}.    */
DECL|method|add (Runnable runnable, Executor executor)
specifier|public
name|void
name|add
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|queuedListeners
operator|.
name|add
argument_list|(
operator|new
name|RunnableExecutorPair
argument_list|(
name|runnable
argument_list|,
name|executor
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Executes all listeners in the queue.    *    *<p>Note that there is no guarantee that concurrently {@linkplain #add added} listeners will be    * executed prior to the return of this method, only that all calls to {@link #add} that    * happen-before this call will be executed.    */
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
block|{
comment|// We need to make sure that listeners are submitted to their executors in the correct order. So
comment|// we cannot remove a listener from the queue until we know that it has been submited to its
comment|// executor.  So we use an iterator and only call remove after submit.  This iterator is 'weakly
comment|// consistent' which means it observes the list in the correct order but not neccesarily all of
comment|// it (i.e. concurrently added or removed items may or may not be observed correctly by this
comment|// iterator).  This is fine because 1. our contract says we may not execute all concurrently
comment|// added items and 2. calling listener.submit is idempotent, so it is safe (and generally cheap)
comment|// to call it multiple times.
comment|// TODO(user): we are relying on an underdocumented feature of ConcurrentLinkedQueue, the
comment|// general strategy in other JDK libraries appears to be bring-your-own-queue :(  Consider doing
comment|// that.
name|Iterator
argument_list|<
name|RunnableExecutorPair
argument_list|>
name|iterator
init|=
name|queuedListeners
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|iterator
operator|.
name|next
argument_list|()
operator|.
name|submit
argument_list|()
expr_stmt|;
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * The listener object for the queue.    *    *<p>This ensures that:    *<ol>    *<li>{@link #executor executor}.{@link Executor#execute execute} is called at most once    *<li>{@link #runnable runnable}.{@link Runnable#run run} is called at most once by the    *        executor    *<li>{@link #lock lock} is not held when {@link #runnable runnable}.{@link Runnable#run run}    *       is called    *<li>no thread calling {@link #submit} can return until the task has been accepted by the    *       executor    *</ol>    */
DECL|class|RunnableExecutorPair
specifier|private
specifier|final
class|class
name|RunnableExecutorPair
implements|implements
name|Runnable
block|{
DECL|field|executor
specifier|private
specifier|final
name|Executor
name|executor
decl_stmt|;
DECL|field|runnable
specifier|private
specifier|final
name|Runnable
name|runnable
decl_stmt|;
comment|/**      * Should be set to {@code true} after {@link #executor}.{@link Executor#execute execute} has      * been called.      */
annotation|@
name|GuardedBy
argument_list|(
literal|"lock"
argument_list|)
DECL|field|hasBeenExecuted
specifier|private
name|boolean
name|hasBeenExecuted
init|=
literal|false
decl_stmt|;
DECL|method|RunnableExecutorPair (Runnable runnable, Executor executor)
name|RunnableExecutorPair
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|this
operator|.
name|runnable
operator|=
name|checkNotNull
argument_list|(
name|runnable
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
comment|/** Submit this listener to its executor */
DECL|method|submit ()
specifier|private
name|void
name|submit
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
operator|!
name|hasBeenExecuted
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
name|runnable
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
block|}
finally|finally
block|{
comment|// If the executor was the sameThreadExecutor we may have already released the lock, so
comment|// check for that here.
if|if
condition|(
name|lock
operator|.
name|isHeldByCurrentThread
argument_list|()
condition|)
block|{
name|hasBeenExecuted
operator|=
literal|true
expr_stmt|;
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|run ()
annotation|@
name|Override
specifier|public
specifier|final
name|void
name|run
parameter_list|()
block|{
comment|// If the executor was the sameThreadExecutor then we might still be holding the lock and
comment|// hasBeenExecuted may not have been assigned yet so we unlock now to ensure that we are not
comment|// still holding the lock while execute is called.
if|if
condition|(
name|lock
operator|.
name|isHeldByCurrentThread
argument_list|()
condition|)
block|{
name|hasBeenExecuted
operator|=
literal|true
expr_stmt|;
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
name|runnable
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

