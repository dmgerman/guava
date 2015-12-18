begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|base
operator|.
name|Preconditions
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayDeque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Deque
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
comment|/**  * Executor ensuring that all Runnables submitted are executed in order,  * using the provided Executor, and serially such that no two will ever  * be running at the same time.  *  *<p>Tasks submitted to {@link #execute(Runnable)} are executed in FIFO order.  *  *<p>Tasks can also be prepended to the queue to be executed in LIFO order before any other  * submitted tasks. Primarily intended for the currently executing task to be able to schedule a  * continuation task.  *  *<p>Execution on the queue can be {@linkplain #suspend suspended}, e.g. while waiting for an RPC,  * and execution can be {@linkplain #resume resumed} later.  *  *<p>The execution of tasks is done by one thread as long as there are tasks left in the queue and  * execution has not been suspended. (Even if one task is {@linkplain Thread#interrupt interrupted},  * execution of subsequent tasks continues.) {@code RuntimeException}s thrown by tasks are simply  * logged and the executor keeps trucking. If an {@code Error} is thrown, the error will propagate  * and execution will stop until it is restarted by external calls.  */
end_comment

begin_class
DECL|class|SerializingExecutor
specifier|final
class|class
name|SerializingExecutor
implements|implements
name|Executor
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|SerializingExecutor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|/** Underlying executor that all submitted Runnable objects are run on. */
DECL|field|executor
specifier|private
specifier|final
name|Executor
name|executor
decl_stmt|;
annotation|@
name|GuardedBy
argument_list|(
literal|"internalLock"
argument_list|)
DECL|field|queue
specifier|private
specifier|final
name|Deque
argument_list|<
name|Runnable
argument_list|>
name|queue
init|=
operator|new
name|ArrayDeque
argument_list|<
name|Runnable
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|GuardedBy
argument_list|(
literal|"internalLock"
argument_list|)
DECL|field|isWorkerRunning
specifier|private
name|boolean
name|isWorkerRunning
init|=
literal|false
decl_stmt|;
annotation|@
name|GuardedBy
argument_list|(
literal|"internalLock"
argument_list|)
DECL|field|suspensions
specifier|private
name|int
name|suspensions
init|=
literal|0
decl_stmt|;
DECL|field|internalLock
specifier|private
specifier|final
name|Object
name|internalLock
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
DECL|method|SerializingExecutor (Executor executor)
specifier|public
name|SerializingExecutor
parameter_list|(
name|Executor
name|executor
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|executor
argument_list|)
expr_stmt|;
block|}
comment|/**    * Adds a task to the queue and makes sure a worker thread is running, unless the queue has been    * suspended.    *    *<p>If this method throws, e.g. a {@code RejectedExecutionException} from the delegate executor,    * execution of tasks will stop until a call to this method or to {@link #resume()} is    * made.    */
DECL|method|execute (Runnable task)
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|task
parameter_list|)
block|{
synchronized|synchronized
init|(
name|internalLock
init|)
block|{
name|queue
operator|.
name|add
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
name|startQueueWorker
argument_list|()
expr_stmt|;
block|}
comment|/**    * Prepends a task to the front of the queue and makes sure a worker thread is running, unless the    * queue has been suspended.    */
DECL|method|executeFirst (Runnable task)
specifier|public
name|void
name|executeFirst
parameter_list|(
name|Runnable
name|task
parameter_list|)
block|{
synchronized|synchronized
init|(
name|internalLock
init|)
block|{
name|queue
operator|.
name|addFirst
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
name|startQueueWorker
argument_list|()
expr_stmt|;
block|}
comment|/**    * Suspends the running of tasks until {@link #resume()} is called. This can be called multiple    * times to increase the suspensions count and execution will not continue until {@link #resume}    * has been called the same number of times as {@code suspend} has been.    *    *<p>Any task that has already been pulled off the queue for execution will be completed    * before execution is suspended.    */
DECL|method|suspend ()
specifier|public
name|void
name|suspend
parameter_list|()
block|{
synchronized|synchronized
init|(
name|internalLock
init|)
block|{
name|suspensions
operator|++
expr_stmt|;
block|}
block|}
comment|/**    * Continue execution of tasks after a call to {@link #suspend()}. More accurately, decreases the    * suspension counter, as has been incremented by calls to {@link #suspend}, and resumes execution    * if the suspension counter is zero.    *    *<p>If this method throws, e.g. a {@code RejectedExecutionException} from the delegate executor,    * execution of tasks will stop until a call to this method or to {@link #execute(Runnable)} or    * {@link #executeFirst(Runnable)} is made.    *    * @throws java.lang.IllegalStateException if this executor is not suspended.    */
DECL|method|resume ()
specifier|public
name|void
name|resume
parameter_list|()
block|{
synchronized|synchronized
init|(
name|internalLock
init|)
block|{
name|Preconditions
operator|.
name|checkState
argument_list|(
name|suspensions
operator|>
literal|0
argument_list|)
expr_stmt|;
name|suspensions
operator|--
expr_stmt|;
block|}
name|startQueueWorker
argument_list|()
expr_stmt|;
block|}
DECL|method|startQueueWorker ()
specifier|private
name|void
name|startQueueWorker
parameter_list|()
block|{
synchronized|synchronized
init|(
name|internalLock
init|)
block|{
comment|// We sometimes try to start a queue worker without knowing if there is any work to do.
if|if
condition|(
name|queue
operator|.
name|peek
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|suspensions
operator|>
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|isWorkerRunning
condition|)
block|{
return|return;
block|}
name|isWorkerRunning
operator|=
literal|true
expr_stmt|;
block|}
name|boolean
name|executionRejected
init|=
literal|true
decl_stmt|;
try|try
block|{
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|QueueWorker
argument_list|()
argument_list|)
expr_stmt|;
name|executionRejected
operator|=
literal|false
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|executionRejected
condition|)
block|{
comment|// The best we can do is to stop executing the queue, but reset the state so that
comment|// execution can be resumed later if the caller so wishes.
synchronized|synchronized
init|(
name|internalLock
init|)
block|{
name|isWorkerRunning
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**    * Worker that runs tasks off the queue until it is empty or the queue is suspended.    */
DECL|class|QueueWorker
specifier|private
specifier|final
class|class
name|QueueWorker
implements|implements
name|Runnable
block|{
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|workOnQueue
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
synchronized|synchronized
init|(
name|internalLock
init|)
block|{
name|isWorkerRunning
operator|=
literal|false
expr_stmt|;
block|}
throw|throw
name|e
throw|;
comment|// The execution of a task has ended abnormally.
comment|// We could have tasks left in the queue, so should perhaps try to restart a worker,
comment|// but then the Error will get delayed if we are using a direct (same thread) executor.
block|}
block|}
DECL|method|workOnQueue ()
specifier|private
name|void
name|workOnQueue
parameter_list|()
block|{
while|while
condition|(
literal|true
condition|)
block|{
name|Runnable
name|task
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|internalLock
init|)
block|{
comment|// TODO(user): How should we handle interrupts and shutdowns?
if|if
condition|(
name|suspensions
operator|==
literal|0
condition|)
block|{
name|task
operator|=
name|queue
operator|.
name|poll
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|task
operator|==
literal|null
condition|)
block|{
name|isWorkerRunning
operator|=
literal|false
expr_stmt|;
return|return;
block|}
block|}
try|try
block|{
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|log
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"Exception while executing runnable "
operator|+
name|task
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

