begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2018 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|ExecutionSequencer
operator|.
name|RunningState
operator|.
name|CANCELLED
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
name|ExecutionSequencer
operator|.
name|RunningState
operator|.
name|NOT_RUN
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
name|ExecutionSequencer
operator|.
name|RunningState
operator|.
name|STARTED
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
name|Futures
operator|.
name|immediateCancelledFuture
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
name|Futures
operator|.
name|immediateFuture
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
name|Futures
operator|.
name|immediateVoidFuture
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
name|MoreExecutors
operator|.
name|directExecutor
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
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
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
import|;
end_import

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Serializes execution of tasks, somewhat like an "asynchronous {@code synchronized} block." Each  * {@linkplain #submit enqueued} callable will not be submitted to its associated executor until the  * previous callable has returned -- and, if the previous callable was an {@link AsyncCallable}, not  * until the {@code Future} it returned is {@linkplain Future#isDone done} (successful, failed, or  * cancelled).  *  *<p>This class has limited support for cancellation and other "early completion":  *  *<ul>  *<li>While calls to {@code submit} and {@code submitAsync} return a {@code Future} that can be  *       cancelled, cancellation never propagates to a task that has started to run -- neither to  *       the callable itself nor to any {@code Future} returned by an {@code AsyncCallable}.  *       (However, cancellation can prevent an<i>unstarted</i> task from running.) Therefore, the  *       next task will wait for any running callable (or pending {@code Future} returned by an  *       {@code AsyncCallable}) to complete, without interrupting it (and without calling {@code  *       cancel} on the {@code Future}). So beware:<i>Even if you cancel every precededing {@code  *       Future} returned by this class, the next task may still have to wait.</i>.  *<li>Once an {@code AsyncCallable} returns a {@code Future}, this class considers that task to  *       be "done" as soon as<i>that</i> {@code Future} completes in any way. Notably, a {@code  *       Future} is "completed" even if it is cancelled while its underlying work continues on a  *       thread, an RPC, etc. The {@code Future} is also "completed" if it fails "early" -- for  *       example, if the deadline expires on a {@code Future} returned from {@link  *       Futures#withTimeout} while the {@code Future} it wraps continues its underlying work. So  *       beware:<i>Your {@code AsyncCallable} should not complete its {@code Future} until it is  *       safe for the next task to start.</i>  *</ul>  *  *<p>An additional limitation: this class serializes execution of<i>tasks</i> but not any  *<i>listeners</i> of those tasks.  *  *<p>This class is similar to {@link MoreExecutors#newSequentialExecutor}. This class is different  * in a few ways:  *  *<ul>  *<li>Each task may be associated with a different executor.  *<li>Tasks may be of type {@code AsyncCallable}.  *<li>Running tasks<i>cannot</i> be interrupted. (Note that {@code newSequentialExecutor} does  *       not return {@code Future} objects, so it doesn't support interruption directly, either.  *       However, utilities that<i>use</i> that executor have the ability to interrupt tasks  *       running on it. This class, by contrast, does not expose an {@code Executor} API.)  *</ul>  *  *<p>If you don't need the features of this class, you may prefer {@code newSequentialExecutor} for  * its simplicity and ability to accommodate interruption.  *  * @since 26.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|ExecutionSequencer
specifier|public
specifier|final
class|class
name|ExecutionSequencer
block|{
DECL|method|ExecutionSequencer ()
specifier|private
name|ExecutionSequencer
parameter_list|()
block|{}
comment|/** Creates a new instance. */
DECL|method|create ()
specifier|public
specifier|static
name|ExecutionSequencer
name|create
parameter_list|()
block|{
return|return
operator|new
name|ExecutionSequencer
argument_list|()
return|;
block|}
comment|/** This reference acts as a pointer tracking the head of a linked list of ListenableFutures. */
DECL|field|ref
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|ListenableFuture
argument_list|<
annotation|@
name|Nullable
name|Void
argument_list|>
argument_list|>
name|ref
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|(
name|immediateVoidFuture
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|latestTaskQueue
specifier|private
name|ThreadConfinedTaskQueue
name|latestTaskQueue
init|=
operator|new
name|ThreadConfinedTaskQueue
argument_list|()
decl_stmt|;
comment|/**    * This object is unsafely published, but avoids problematic races by relying exclusively on the    * identity equality of its Thread field so that the task field is only accessed by a single    * thread.    */
DECL|class|ThreadConfinedTaskQueue
specifier|private
specifier|static
specifier|final
class|class
name|ThreadConfinedTaskQueue
block|{
comment|/**      * This field is only used for identity comparisons with the current thread. Field assignments      * are atomic, but do not provide happens-before ordering; however:      *      *<ul>      *<li>If this field's value == currentThread, we know that it's up to date, because write      *       operations in a thread always happen-before subsequent read operations in the same      *       thread      *<li>If this field's value == null because of unsafe publication, we know that it isn't the      *       object associated with our thread, because if it was the publication wouldn't have been      *       unsafe and we'd have seen our thread as the value. This state is also why a new      *       ThreadConfinedTaskQueue object must be created for each inline execution, because      *       observing a null thread does not mean the object is safe to reuse.      *<li>If this field's value is some other thread object, we know that it's not our thread.      *<li>If this field's value == null because it originally belonged to another thread and that      *       thread cleared it, we still know that it's not associated with our thread      *<li>If this field's value == null because it was associated with our thread and was      *       cleared, we know that we're not executing inline any more      *</ul>      *      * All the states where thread != currentThread are identical for our purposes, and so even      * though it's racy, we don't care which of those values we get, so no need to synchronize.      */
DECL|field|thread
annotation|@
name|CheckForNull
name|Thread
name|thread
decl_stmt|;
comment|/** Only used by the thread associated with this object */
DECL|field|nextTask
annotation|@
name|CheckForNull
name|Runnable
name|nextTask
decl_stmt|;
comment|/** Only used by the thread associated with this object */
DECL|field|nextExecutor
annotation|@
name|CheckForNull
name|Executor
name|nextExecutor
decl_stmt|;
block|}
comment|/**    * Enqueues a task to run when the previous task (if any) completes.    *    *<p>Cancellation does not propagate from the output future to a callable that has begun to    * execute, but if the output future is cancelled before {@link Callable#call()} is invoked,    * {@link Callable#call()} will not be invoked.    */
DECL|method|submit ( Callable<T> callable, Executor executor)
specifier|public
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|ListenableFuture
argument_list|<
name|T
argument_list|>
name|submit
argument_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
argument_list|,
name|Executor
name|executor
argument_list|)
block|{
name|checkNotNull
argument_list|(
name|callable
argument_list|)
block|;
name|checkNotNull
argument_list|(
name|executor
argument_list|)
block|;
return|return
name|submitAsync
argument_list|(
operator|new
name|AsyncCallable
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|T
argument_list|>
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|immediateFuture
argument_list|(
name|callable
operator|.
name|call
argument_list|()
argument_list|)
return|;
block|}
expr|@
name|Override
specifier|public
name|String
name|toString
argument_list|()
block|{
return|return
name|callable
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

begin_operator
operator|,
end_operator

begin_expr_stmt
name|executor
end_expr_stmt

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_comment
unit|}
comment|/**    * Enqueues a task to run when the previous task (if any) completes.    *    *<p>Cancellation does not propagate from the output future to the future returned from {@code    * callable} or a callable that has begun to execute, but if the output future is cancelled before    * {@link AsyncCallable#call()} is invoked, {@link AsyncCallable#call()} will not be invoked.    */
end_comment

begin_expr_stmt
DECL|method|submitAsync ( AsyncCallable<T> callable, Executor executor)
unit|public
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|ListenableFuture
argument_list|<
name|T
argument_list|>
name|submitAsync
argument_list|(
name|AsyncCallable
argument_list|<
name|T
argument_list|>
name|callable
argument_list|,
name|Executor
name|executor
argument_list|)
block|{
name|checkNotNull
argument_list|(
name|callable
argument_list|)
block|;
name|checkNotNull
argument_list|(
name|executor
argument_list|)
block|;
name|TaskNonReentrantExecutor
name|taskExecutor
operator|=
operator|new
name|TaskNonReentrantExecutor
argument_list|(
name|executor
argument_list|,
name|this
argument_list|)
block|;
name|AsyncCallable
argument_list|<
name|T
argument_list|>
name|task
operator|=
operator|new
name|AsyncCallable
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|T
argument_list|>
name|call
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|taskExecutor
operator|.
name|trySetStarted
argument_list|()
condition|)
block|{
return|return
name|immediateCancelledFuture
argument_list|()
return|;
block|}
end_expr_stmt

begin_return
return|return
name|callable
operator|.
name|call
argument_list|()
return|;
end_return

begin_function
unit|}            @
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|callable
operator|.
name|toString
argument_list|()
return|;
block|}
end_function

begin_comment
unit|};
comment|/*      * Four futures are at play here:      * taskFuture is the future tracking the result of the callable.      * newFuture is a future that completes after this and all prior tasks are done.      * oldFuture is the previous task's newFuture.      * outputFuture is the future we return to the caller, a nonCancellationPropagating taskFuture.      *      * newFuture is guaranteed to only complete once all tasks previously submitted to this instance      * have completed - namely after oldFuture is done, and taskFuture has either completed or been      * cancelled before the callable started execution.      */
end_comment

begin_decl_stmt
name|SettableFuture
argument_list|<
annotation|@
name|Nullable
name|Void
argument_list|>
name|newFuture
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
end_decl_stmt

begin_decl_stmt
name|ListenableFuture
argument_list|<
annotation|@
name|Nullable
name|Void
argument_list|>
name|oldFuture
init|=
name|ref
operator|.
name|getAndSet
argument_list|(
name|newFuture
argument_list|)
decl_stmt|;
end_decl_stmt

begin_comment
comment|// Invoke our task once the previous future completes.
end_comment

begin_decl_stmt
name|TrustedListenableFutureTask
argument_list|<
name|T
argument_list|>
name|taskFuture
init|=
name|TrustedListenableFutureTask
operator|.
name|create
argument_list|(
name|task
argument_list|)
decl_stmt|;
end_decl_stmt

begin_expr_stmt
name|oldFuture
operator|.
name|addListener
argument_list|(
name|taskFuture
argument_list|,
name|taskExecutor
argument_list|)
expr_stmt|;
end_expr_stmt

begin_decl_stmt
name|ListenableFuture
argument_list|<
name|T
argument_list|>
name|outputFuture
init|=
name|Futures
operator|.
name|nonCancellationPropagating
argument_list|(
name|taskFuture
argument_list|)
decl_stmt|;
end_decl_stmt

begin_comment
comment|// newFuture's lifetime is determined by taskFuture, which can't complete before oldFuture
end_comment

begin_comment
comment|// unless taskFuture is cancelled, in which case it falls back to oldFuture. This ensures that
end_comment

begin_comment
comment|// if the future we return is cancelled, we don't begin execution of the next task until after
end_comment

begin_comment
comment|// oldFuture completes.
end_comment

begin_decl_stmt
name|Runnable
name|listener
init|=
parameter_list|()
lambda|->
block|{
if|if
condition|(
name|taskFuture
operator|.
name|isDone
argument_list|()
condition|)
block|{
comment|// Since the value of oldFuture can only ever be immediateFuture(null) or setFuture of
comment|// a future that eventually came from immediateFuture(null), this doesn't leak
comment|// throwables or completion values.
name|newFuture
operator|.
name|setFuture
argument_list|(
name|oldFuture
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|outputFuture
operator|.
name|isCancelled
argument_list|()
operator|&&
name|taskExecutor
operator|.
name|trySetCancelled
argument_list|()
condition|)
block|{
comment|// If this CAS succeeds, we know that the provided callable will never be invoked,
comment|// so when oldFuture completes it is safe to allow the next submitted task to
comment|// proceed. Doing this immediately here lets the next task run without waiting for
comment|// the cancelled task's executor to run the noop AsyncCallable.
comment|//
comment|// ---
comment|//
comment|// If the CAS fails, the provided callable already started running (or it is about
comment|// to). Our contract promises:
comment|//
comment|// 1. not to execute a new callable until the old one has returned
comment|//
comment|// If we were to cancel taskFuture, that would let the next task start while the old
comment|// one is still running.
comment|//
comment|// Now, maybe we could tweak our implementation to not start the next task until the
comment|// callable actually completes. (We could detect completion in our wrapper
comment|// `AsyncCallable task`.) However, our contract also promises:
comment|//
comment|// 2. not to cancel any Future the user returned from an AsyncCallable
comment|//
comment|// We promise this because, once we cancel that Future, we would no longer be able to
comment|// tell when any underlying work it is doing is done. Thus, we might start a new task
comment|// while that underlying work is still running.
comment|//
comment|// So that is why we cancel only in the case of CAS success.
name|taskFuture
operator|.
name|cancel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
end_decl_stmt

begin_comment
comment|// Adding the listener to both futures guarantees that newFuture will aways be set. Adding to
end_comment

begin_comment
comment|// taskFuture guarantees completion if the callable is invoked, and adding to outputFuture
end_comment

begin_comment
comment|// propagates cancellation if the callable has not yet been invoked.
end_comment

begin_expr_stmt
name|outputFuture
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|taskFuture
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
end_expr_stmt

begin_return
return|return
name|outputFuture
return|;
end_return

begin_expr_stmt
unit|}    enum
DECL|enum|RunningState
name|RunningState
block|{
DECL|enumConstant|NOT_RUN
name|NOT_RUN
block|,
DECL|enumConstant|CANCELLED
name|CANCELLED
block|,
DECL|enumConstant|STARTED
name|STARTED
block|,   }
comment|/**    * This class helps avoid a StackOverflowError when large numbers of tasks are submitted with    * {@link MoreExecutors#directExecutor}. Normally, when the first future completes, all the other    * tasks would be called recursively. Here, we detect that the delegate executor is executing    * inline, and maintain a queue to dispatch tasks iteratively. There is one instance of this class    * per call to submit() or submitAsync(), and each instance supports only one call to execute().    *    *<p>This class would certainly be simpler and easier to reason about if it were built with    * ThreadLocal; however, ThreadLocal is not well optimized for the case where the ThreadLocal is    * non-static, and is initialized/removed frequently - this causes churn in the Thread specific    * hashmaps. Using a static ThreadLocal to avoid that overhead would mean that different    * ExecutionSequencer objects interfere with each other, which would be undesirable, in addition    * to increasing the memory footprint of every thread that interacted with it. In order to release    * entries in thread-specific maps when the ThreadLocal object itself is no longer referenced,    * ThreadLocal is usually implemented with a WeakReference, which can have negative performance    * properties; for example, calling WeakReference.get() on Android will block during an    * otherwise-concurrent GC cycle.    */
DECL|class|TaskNonReentrantExecutor
specifier|private
specifier|static
name|final
name|class
name|TaskNonReentrantExecutor
expr|extends
name|AtomicReference
argument_list|<
name|RunningState
argument_list|>
expr|implements
name|Executor
operator|,
name|Runnable
block|{
comment|/**      * Used to update and read the latestTaskQueue field. Set to null once the runnable has been run      * or queued.      */
DECL|field|sequencer
block|@
name|CheckForNull
name|ExecutionSequencer
name|sequencer
block|;
comment|/**      * Executor the task was set to run on. Set to null when the task has been queued, run, or      * cancelled.      */
DECL|field|delegate
block|@
name|CheckForNull
name|Executor
name|delegate
block|;
comment|/**      * Set before calling delegate.execute(); set to null once run, so that it can be GCed; this      * object may live on after, if submitAsync returns an incomplete future.      */
DECL|field|task
block|@
name|CheckForNull
name|Runnable
name|task
block|;
comment|/** Thread that called execute(). Set in execute, cleared when delegate.execute() returns. */
DECL|field|submitting
block|@
name|CheckForNull
name|Thread
name|submitting
block|;
DECL|method|TaskNonReentrantExecutor (Executor delegate, ExecutionSequencer sequencer)
specifier|private
name|TaskNonReentrantExecutor
argument_list|(
name|Executor
name|delegate
argument_list|,
name|ExecutionSequencer
name|sequencer
argument_list|)
block|{
name|super
argument_list|(
name|NOT_RUN
argument_list|)
block|;
name|this
operator|.
name|delegate
operator|=
name|delegate
block|;
name|this
operator|.
name|sequencer
operator|=
name|sequencer
block|;     }
expr|@
name|Override
DECL|method|execute (Runnable task)
specifier|public
name|void
name|execute
argument_list|(
name|Runnable
name|task
argument_list|)
block|{
comment|// If this operation was successfully cancelled already, calling the runnable will be a noop.
comment|// This also avoids a race where if outputFuture is cancelled, it will call taskFuture.cancel,
comment|// which will call newFuture.setFuture(oldFuture), to allow the next task in the queue to run
comment|// without waiting for the user's executor to run our submitted Runnable. However, this can
comment|// interact poorly with the reentrancy-avoiding behavior of this executor - when the operation
comment|// before the cancelled future completes, it will synchronously complete both the newFuture
comment|// from the cancelled operation and its own. This can cause one runnable to queue two tasks,
comment|// breaking the invariant this method relies on to iteratively run the next task after the
comment|// previous one completes.
if|if
condition|(
name|get
argument_list|()
operator|==
name|RunningState
operator|.
name|CANCELLED
condition|)
block|{
name|delegate
operator|=
literal|null
expr_stmt|;
name|sequencer
operator|=
literal|null
expr_stmt|;
return|return;
block|}
name|submitting
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
block|;
try|try
block|{
comment|/*          * requireNonNull is safe because we don't null out `sequencer` except:          *          * - above, where we return (in which case we never get here)          *          * - in `run`, which can't run until this Runnable is submitted to an executor, which          *   doesn't happen until below. (And this Executor -- yes, the object is both a Runnable          *   and an Executor -- is used for only a single `execute` call.)          */
name|ThreadConfinedTaskQueue
name|submittingTaskQueue
init|=
name|requireNonNull
argument_list|(
name|sequencer
argument_list|)
operator|.
name|latestTaskQueue
decl_stmt|;
if|if
condition|(
name|submittingTaskQueue
operator|.
name|thread
operator|==
name|submitting
condition|)
block|{
name|sequencer
operator|=
literal|null
expr_stmt|;
comment|// Submit from inside a reentrant submit. We don't know if this one will be reentrant (and
comment|// can't know without submitting something to the executor) so queue to run iteratively.
comment|// Task must be null, since each execution on this executor can only produce one more
comment|// execution.
name|checkState
argument_list|(
name|submittingTaskQueue
operator|.
name|nextTask
operator|==
literal|null
argument_list|)
expr_stmt|;
name|submittingTaskQueue
operator|.
name|nextTask
operator|=
name|task
expr_stmt|;
comment|// requireNonNull(delegate) is safe for reasons similar to requireNonNull(sequencer).
name|submittingTaskQueue
operator|.
name|nextExecutor
operator|=
name|requireNonNull
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|delegate
operator|=
literal|null
expr_stmt|;
block|}
end_expr_stmt

begin_else
else|else
block|{
comment|// requireNonNull(delegate) is safe for reasons similar to requireNonNull(sequencer).
name|Executor
name|localDelegate
init|=
name|requireNonNull
argument_list|(
name|delegate
argument_list|)
decl_stmt|;
name|delegate
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|task
operator|=
name|task
expr_stmt|;
name|localDelegate
operator|.
name|execute
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
end_else

begin_block
unit|} finally
block|{
comment|// Important to null this out here - if we did *not* execute inline, we might still
comment|// run() on the same thread that called execute() - such as in a thread pool, and think
comment|// that it was happening inline. As a side benefit, avoids holding on to the Thread object
comment|// longer than necessary.
name|submitting
operator|=
literal|null
expr_stmt|;
block|}
end_block

begin_expr_stmt
unit|}      @
name|SuppressWarnings
argument_list|(
literal|"ShortCircuitBoolean"
argument_list|)
expr|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
argument_list|()
block|{
name|Thread
name|currentThread
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
block|;
if|if
condition|(
name|currentThread
operator|!=
name|submitting
condition|)
block|{
comment|/*          * requireNonNull is safe because we set `task` before submitting this Runnable to an          * Executor, and we don't null it out until here.          */
name|Runnable
name|localTask
init|=
name|requireNonNull
argument_list|(
name|task
argument_list|)
decl_stmt|;
name|task
operator|=
literal|null
expr_stmt|;
name|localTask
operator|.
name|run
argument_list|()
expr_stmt|;
return|return;
block|}
comment|// Executor called reentrantly! Make sure that further calls don't overflow stack. Further
comment|// reentrant calls will see that their current thread is the same as the one set in
comment|// latestTaskQueue, and queue rather than calling execute() directly.
name|ThreadConfinedTaskQueue
name|executingTaskQueue
operator|=
operator|new
name|ThreadConfinedTaskQueue
argument_list|()
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|executingTaskQueue
operator|.
name|thread
operator|=
name|currentThread
expr_stmt|;
end_expr_stmt

begin_comment
comment|/*        * requireNonNull is safe because we don't null out `sequencer` except:        *        * - after the requireNonNull call below. (And this object has its Runnable.run override        *   called only once, just as it has its Executor.execute override called only once.)        *        * - if we return immediately from `execute` (in which case we never get here)        *        * - in the "reentrant submit" case of `execute` (in which case we must have started running a        *   user task -- which means that we already got past this code (or else we exited early        *   above))        */
end_comment

begin_comment
comment|// Unconditionally set; there is no risk of throwing away a queued task from another thread,
end_comment

begin_comment
comment|// because in order for the current task to run on this executor the previous task must have
end_comment

begin_comment
comment|// already started execution. Because each task on a TaskNonReentrantExecutor can only produce
end_comment

begin_comment
comment|// one execute() call to another instance from the same ExecutionSequencer, we know by
end_comment

begin_comment
comment|// induction that the task that launched this one must not have added any other runnables to
end_comment

begin_comment
comment|// that thread's queue, and thus we cannot be replacing a TaskAndThread object that would
end_comment

begin_comment
comment|// otherwise have another task queued on to it. Note the exception to this, cancellation, is
end_comment

begin_comment
comment|// specially handled in execute() - execute() calls triggered by cancellation are no-ops, and
end_comment

begin_comment
comment|// thus don't count.
end_comment

begin_expr_stmt
name|requireNonNull
argument_list|(
name|sequencer
argument_list|)
operator|.
name|latestTaskQueue
operator|=
name|executingTaskQueue
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|sequencer
operator|=
literal|null
expr_stmt|;
end_expr_stmt

begin_try
try|try
block|{
comment|// requireNonNull is safe, as discussed above.
name|Runnable
name|localTask
init|=
name|requireNonNull
argument_list|(
name|task
argument_list|)
decl_stmt|;
name|task
operator|=
literal|null
expr_stmt|;
name|localTask
operator|.
name|run
argument_list|()
expr_stmt|;
comment|// Now check if our task attempted to reentrantly execute the next task.
name|Runnable
name|queuedTask
decl_stmt|;
name|Executor
name|queuedExecutor
decl_stmt|;
comment|// Intentionally using non-short-circuit operator
while|while
condition|(
operator|(
name|queuedTask
operator|=
name|executingTaskQueue
operator|.
name|nextTask
operator|)
operator|!=
literal|null
operator|&
operator|(
name|queuedExecutor
operator|=
name|executingTaskQueue
operator|.
name|nextExecutor
operator|)
operator|!=
literal|null
condition|)
block|{
name|executingTaskQueue
operator|.
name|nextTask
operator|=
literal|null
expr_stmt|;
name|executingTaskQueue
operator|.
name|nextExecutor
operator|=
literal|null
expr_stmt|;
name|queuedExecutor
operator|.
name|execute
argument_list|(
name|queuedTask
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// Null out the thread field, so that we don't leak a reference to Thread, and so that
comment|// future `thread == currentThread()` calls from this thread don't incorrectly queue instead
comment|// of executing. Don't null out the latestTaskQueue field, because the work done here
comment|// may have scheduled more operations on another thread, and if those operations then
comment|// trigger reentrant calls that thread will have updated the latestTaskQueue field, and
comment|// we'd be interfering with their operation.
name|executingTaskQueue
operator|.
name|thread
operator|=
literal|null
expr_stmt|;
block|}
end_try

begin_function
unit|}      private
DECL|method|trySetStarted ()
name|boolean
name|trySetStarted
parameter_list|()
block|{
return|return
name|compareAndSet
argument_list|(
name|NOT_RUN
argument_list|,
name|STARTED
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|trySetCancelled ()
specifier|private
name|boolean
name|trySetCancelled
parameter_list|()
block|{
return|return
name|compareAndSet
argument_list|(
name|NOT_RUN
argument_list|,
name|CANCELLED
argument_list|)
return|;
block|}
end_function

unit|} }
end_unit

