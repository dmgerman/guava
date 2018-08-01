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
name|MoreExecutors
operator|.
name|directExecutor
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

begin_comment
comment|/**  * Serializes execution of a set of operations. This class guarantees that a submitted callable will  * not be called before previously submitted callables (and any {@code Future}s returned from them)  * have completed.  *  *<p>This class implements a superset of the behavior of {@link  * MoreExecutors#newSequentialExecutor}. If your tasks all run on the same underlying executor and  * don't need to wait for {@code Future}s returned from {@code AsyncCallable}s, use it instead.  *  * @since 26.0  */
end_comment

begin_class
annotation|@
name|Beta
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
DECL|enum|RunningState
enum|enum
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
comment|/** This reference acts as a pointer tracking the head of a linked list of ListenableFutures. */
DECL|field|ref
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|ListenableFuture
argument_list|<
name|Object
argument_list|>
argument_list|>
name|ref
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|(
name|immediateFuture
argument_list|(
literal|null
argument_list|)
argument_list|)
decl_stmt|;
comment|/**    * Enqueues a task to run when the previous task (if any) completes.    *    *<p>Cancellation does not propagate from the output future to a callable that has begun to    * execute, but if the output future is cancelled before {@link Callable#call()} is invoked,    * {@link Callable#call()} will not be invoked.    */
DECL|method|submit (final Callable<T> callable, Executor executor)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ListenableFuture
argument_list|<
name|T
argument_list|>
name|submit
parameter_list|(
specifier|final
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|callable
argument_list|)
expr_stmt|;
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
block|}
argument_list|,
name|executor
argument_list|)
return|;
block|}
comment|/**    * Enqueues a task to run when the previous task (if any) completes.    *    *<p>Cancellation does not propagate from the output future to the future returned from {@code    * callable} or a callable that has begun to execute, but if the output future is cancelled before    * {@link AsyncCallable#call()} is invoked, {@link AsyncCallable#call()} will not be invoked.    */
DECL|method|submitAsync ( final AsyncCallable<T> callable, final Executor executor)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ListenableFuture
argument_list|<
name|T
argument_list|>
name|submitAsync
parameter_list|(
specifier|final
name|AsyncCallable
argument_list|<
name|T
argument_list|>
name|callable
parameter_list|,
specifier|final
name|Executor
name|executor
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|callable
argument_list|)
expr_stmt|;
specifier|final
name|AtomicReference
argument_list|<
name|RunningState
argument_list|>
name|runningState
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|(
name|NOT_RUN
argument_list|)
decl_stmt|;
specifier|final
name|AsyncCallable
argument_list|<
name|T
argument_list|>
name|task
init|=
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
name|runningState
operator|.
name|compareAndSet
argument_list|(
name|NOT_RUN
argument_list|,
name|STARTED
argument_list|)
condition|)
block|{
return|return
name|immediateCancelledFuture
argument_list|()
return|;
block|}
return|return
name|callable
operator|.
name|call
argument_list|()
return|;
block|}
block|}
decl_stmt|;
comment|/*      * Four futures are at play here:      * taskFuture is the future tracking the result of the callable.      * newFuture is a future that completes after this and all prior tasks are done.      * oldFuture is the previous task's newFuture.      * outputFuture is the future we return to the caller, a nonCancellationPropagating taskFuture.      *      * newFuture is guaranteed to only complete once all tasks previously submitted to this instance      * have completed - namely after oldFuture is done, and taskFuture has either completed or been      * cancelled before the callable started execution.      */
specifier|final
name|SettableFuture
argument_list|<
name|Object
argument_list|>
name|newFuture
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
specifier|final
name|ListenableFuture
argument_list|<
name|?
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
comment|// Invoke our task once the previous future completes.
specifier|final
name|ListenableFuture
argument_list|<
name|T
argument_list|>
name|taskFuture
init|=
name|Futures
operator|.
name|submitAsync
argument_list|(
name|task
argument_list|,
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
name|runnable
parameter_list|)
block|{
name|oldFuture
operator|.
name|addListener
argument_list|(
name|runnable
argument_list|,
name|executor
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
specifier|final
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
comment|// newFuture's lifetime is determined by taskFuture, which can't complete before oldFuture
comment|// unless taskFuture is cancelled, in which case it falls back to oldFuture. This ensures that
comment|// if the future we return is cancelled, we don't begin execution of the next task until after
comment|// oldFuture completes.
name|Runnable
name|listener
init|=
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
if|if
condition|(
name|taskFuture
operator|.
name|isDone
argument_list|()
comment|// If this CAS succeeds, we know that the provided callable will never be invoked,
comment|// so when oldFuture completes it is safe to allow the next submitted task to
comment|// proceed.
operator|||
operator|(
name|outputFuture
operator|.
name|isCancelled
argument_list|()
operator|&&
name|runningState
operator|.
name|compareAndSet
argument_list|(
name|NOT_RUN
argument_list|,
name|CANCELLED
argument_list|)
operator|)
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
block|}
block|}
decl_stmt|;
comment|// Adding the listener to both futures guarantees that newFuture will aways be set. Adding to
comment|// taskFuture guarantees completion if the callable is invoked, and adding to outputFuture
comment|// propagates cancellation if the callable has not yet been invoked.
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
return|return
name|outputFuture
return|;
block|}
block|}
end_class

end_unit

