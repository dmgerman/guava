begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2014 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReferenceFieldUpdater
operator|.
name|newUpdater
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
name|GwtCompatible
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
name|Executors
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
name|RunnableFuture
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
name|AtomicReferenceFieldUpdater
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

begin_comment
comment|/**  * A {@link RunnableFuture} that also implements the {@link ListenableFuture}  * interface.  *   *<p>This should be used in preference to {@link ListenableFutureTask} when possible for   * performance reasons.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|TrustedListenableFutureTask
class|class
name|TrustedListenableFutureTask
parameter_list|<
name|V
parameter_list|>
extends|extends
name|AbstractFuture
operator|.
name|TrustedFuture
argument_list|<
name|V
argument_list|>
implements|implements
name|RunnableFuture
argument_list|<
name|V
argument_list|>
block|{
DECL|field|RUNNER
specifier|private
specifier|static
specifier|final
name|AtomicReferenceFieldUpdater
argument_list|<
name|TrustedListenableFutureTask
argument_list|,
name|Thread
argument_list|>
name|RUNNER
init|=
name|newUpdater
argument_list|(
name|TrustedListenableFutureTask
operator|.
name|class
argument_list|,
name|Thread
operator|.
name|class
argument_list|,
literal|"runner"
argument_list|)
decl_stmt|;
comment|/**    * Creates a {@code ListenableFutureTask} that will upon running, execute the    * given {@code Callable}.    *    * @param callable the callable task    */
DECL|method|create (Callable<V> callable)
specifier|static
parameter_list|<
name|V
parameter_list|>
name|TrustedListenableFutureTask
argument_list|<
name|V
argument_list|>
name|create
parameter_list|(
name|Callable
argument_list|<
name|V
argument_list|>
name|callable
parameter_list|)
block|{
return|return
operator|new
name|TrustedListenableFutureTask
argument_list|<
name|V
argument_list|>
argument_list|(
name|callable
argument_list|)
return|;
block|}
comment|/**    * Creates a {@code ListenableFutureTask} that will upon running, execute the    * given {@code Runnable}, and arrange that {@code get} will return the    * given result on successful completion.    *    * @param runnable the runnable task    * @param result the result to return on successful completion. If you don't    *     need a particular result, consider using constructions of the form:    *     {@code ListenableFuture<?> f = ListenableFutureTask.create(runnable,    *     null)}    */
DECL|method|create ( Runnable runnable, @Nullable V result)
specifier|static
parameter_list|<
name|V
parameter_list|>
name|TrustedListenableFutureTask
argument_list|<
name|V
argument_list|>
name|create
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
annotation|@
name|Nullable
name|V
name|result
parameter_list|)
block|{
return|return
operator|new
name|TrustedListenableFutureTask
argument_list|<
name|V
argument_list|>
argument_list|(
name|Executors
operator|.
name|callable
argument_list|(
name|runnable
argument_list|,
name|result
argument_list|)
argument_list|)
return|;
block|}
DECL|field|task
specifier|private
name|Callable
argument_list|<
name|V
argument_list|>
name|task
decl_stmt|;
comment|// These two fields are used to interrupt running tasks.  The thread executing the task publishes
comment|// itself to the 'runner' field and the thread interrupting sets 'doneInterrupting' when it has
comment|// finished interrupting.
DECL|field|runner
specifier|private
specifier|volatile
name|Thread
name|runner
decl_stmt|;
DECL|field|doneInterrupting
specifier|private
specifier|volatile
name|boolean
name|doneInterrupting
decl_stmt|;
DECL|method|TrustedListenableFutureTask (Callable<V> callable)
name|TrustedListenableFutureTask
parameter_list|(
name|Callable
argument_list|<
name|V
argument_list|>
name|callable
parameter_list|)
block|{
name|this
operator|.
name|task
operator|=
name|checkNotNull
argument_list|(
name|callable
argument_list|)
expr_stmt|;
block|}
DECL|method|run ()
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
operator|!
name|RUNNER
operator|.
name|compareAndSet
argument_list|(
name|this
argument_list|,
literal|null
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
argument_list|)
condition|)
block|{
return|return;
comment|// someone else has run or is running.
block|}
try|try
block|{
comment|// Read before checking isDone to ensure that a cancel race doesn't cause us to read null.
name|Callable
argument_list|<
name|V
argument_list|>
name|localTask
init|=
name|task
decl_stmt|;
comment|// Ensure we haven't been cancelled or already run.
if|if
condition|(
operator|!
name|isDone
argument_list|()
condition|)
block|{
name|doRun
argument_list|(
name|localTask
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|setException
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|task
operator|=
literal|null
expr_stmt|;
name|runner
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|wasInterrupted
argument_list|()
condition|)
block|{
comment|// We were interrupted, it is possible that the interrupted bit hasn't been set yet.  Wait
comment|// for the interrupting thread to set 'doneInterrupting' to true. See interruptTask().
comment|// We want to wait so that we don't interrupt the _next_ thing run on the thread.
comment|// Note. We don't reset the interrupted bit, just wait for it to be set.
comment|// If this is a thread pool thread, the thread pool will reset it for us.  Otherwise, the
comment|// interrupted bit may have been intended for something else, so don't clear it.
while|while
condition|(
operator|!
name|doneInterrupting
condition|)
block|{
name|Thread
operator|.
name|yield
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|cancel (boolean mayInterruptIfRunning)
annotation|@
name|Override
specifier|public
name|boolean
name|cancel
parameter_list|(
name|boolean
name|mayInterruptIfRunning
parameter_list|)
block|{
if|if
condition|(
name|super
operator|.
name|cancel
argument_list|(
name|mayInterruptIfRunning
argument_list|)
condition|)
block|{
name|task
operator|=
literal|null
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|interruptTask ()
annotation|@
name|Override
specifier|protected
specifier|final
name|void
name|interruptTask
parameter_list|()
block|{
comment|// interruptTask is guaranteed to be called at most once and if runner is non-null when that
comment|// happens then it must have been the first thread that entered run().  So there is no risk that
comment|// we are interrupting the wrong thread.
name|Thread
name|currentRunner
init|=
name|runner
decl_stmt|;
if|if
condition|(
name|currentRunner
operator|!=
literal|null
condition|)
block|{
name|currentRunner
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
name|doneInterrupting
operator|=
literal|true
expr_stmt|;
block|}
comment|/**    * Template method for calculating and setting the value. Guaranteed to be called at most once.    *    *<p>Extracted as an extension point for subclasses that wish to modify behavior.    * See Futures.combine (which has specialized exception handling).    */
DECL|method|doRun (Callable<V> localTask)
name|void
name|doRun
parameter_list|(
name|Callable
argument_list|<
name|V
argument_list|>
name|localTask
parameter_list|)
throws|throws
name|Exception
block|{
name|set
argument_list|(
name|localTask
operator|.
name|call
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

