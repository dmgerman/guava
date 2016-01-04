begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2006 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
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

begin_comment
comment|/**  * Implementation of {@code Futures#withTimeout}.  *  *<p>Future that delegates to another but will finish early (via a {@link TimeoutException} wrapped  * in an {@link ExecutionException}) if the specified duration expires. The delegate future is  * interrupted and cancelled if it times out.  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|TimeoutFuture
specifier|final
class|class
name|TimeoutFuture
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
block|{
DECL|method|create ( ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor)
specifier|static
parameter_list|<
name|V
parameter_list|>
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|create
parameter_list|(
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|delegate
parameter_list|,
name|long
name|time
parameter_list|,
name|TimeUnit
name|unit
parameter_list|,
name|ScheduledExecutorService
name|scheduledExecutor
parameter_list|)
block|{
name|TimeoutFuture
argument_list|<
name|V
argument_list|>
name|result
init|=
operator|new
name|TimeoutFuture
argument_list|<
name|V
argument_list|>
argument_list|(
name|delegate
argument_list|)
decl_stmt|;
name|TimeoutFuture
operator|.
name|Fire
argument_list|<
name|V
argument_list|>
name|fire
init|=
operator|new
name|TimeoutFuture
operator|.
name|Fire
argument_list|<
name|V
argument_list|>
argument_list|(
name|result
argument_list|)
decl_stmt|;
name|result
operator|.
name|timer
operator|=
name|scheduledExecutor
operator|.
name|schedule
argument_list|(
name|fire
argument_list|,
name|time
argument_list|,
name|unit
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|addListener
argument_list|(
name|fire
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|// Memory visibility of these fields.
comment|// There are two cases to consider.
comment|// 1. visibility of the writes to these fields to Fire.run
comment|//    The initial write to delegateRef is made definitely visible via the semantics of
comment|//    addListener/SES.schedule.  The later racy write in cancel() is not guaranteed to be
comment|//    observed, however that is fine since the correctness is based on the atomic state in
comment|//    our base class.
comment|//    The initial write to timer is never definitely visible to Fire.run since it is assigned
comment|//    after SES.schedule is called. Therefore Fire.run has to check for null.  However, it
comment|//    should be visible if Fire.run is called by delegate.addListener since addListener is
comment|//    called after the assignment to timer, and importantly this is the main situation in which
comment|//    we need to be able to see the write.
comment|// 2. visibility of the writes to cancel
comment|//    Since these fields are non-final that means that TimeoutFuture is not being 'safely
comment|//    published', thus a motivated caller may be able to expose the reference to another thread
comment|//    that would then call cancel() and be unable to cancel the delegate.
comment|//    There are a number of ways to solve this, none of which are very pretty, and it is
comment|//    currently believed to be a purely theoretical problem (since the other actions should
comment|//    supply sufficient write-barriers).
DECL|field|delegateRef
annotation|@
name|Nullable
specifier|private
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|delegateRef
decl_stmt|;
DECL|field|timer
annotation|@
name|Nullable
specifier|private
name|Future
argument_list|<
name|?
argument_list|>
name|timer
decl_stmt|;
DECL|method|TimeoutFuture (ListenableFuture<V> delegate)
specifier|private
name|TimeoutFuture
parameter_list|(
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegateRef
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
comment|/** A runnable that is called when the delegate or the timer completes. */
DECL|class|Fire
specifier|private
specifier|static
specifier|final
class|class
name|Fire
parameter_list|<
name|V
parameter_list|>
implements|implements
name|Runnable
block|{
DECL|field|timeoutFutureRef
annotation|@
name|Nullable
name|TimeoutFuture
argument_list|<
name|V
argument_list|>
name|timeoutFutureRef
decl_stmt|;
DECL|method|Fire (TimeoutFuture<V> timeoutFuture)
name|Fire
parameter_list|(
name|TimeoutFuture
argument_list|<
name|V
argument_list|>
name|timeoutFuture
parameter_list|)
block|{
name|this
operator|.
name|timeoutFutureRef
operator|=
name|timeoutFuture
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
comment|// If either of these reads return null then we must be after a successful cancel or another
comment|// call to this method.
name|TimeoutFuture
argument_list|<
name|V
argument_list|>
name|timeoutFuture
init|=
name|timeoutFutureRef
decl_stmt|;
if|if
condition|(
name|timeoutFuture
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|delegate
init|=
name|timeoutFuture
operator|.
name|delegateRef
decl_stmt|;
if|if
condition|(
name|delegate
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|/*        * If we're about to complete the TimeoutFuture, we want to release our reference to it.        * Otherwise, we'll pin it (and its result) in memory until the timeout task is GCed. (The        * need to clear our reference to the TimeoutFuture is the reason we use a *static* nested        * class with a manual reference back to the "containing" class.)        *        * This has the nice-ish side effect of limiting reentrancy: run() calls        * timeoutFuture.setException() calls run(). That reentrancy would already be harmless,        * since timeoutFuture can be set (and delegate cancelled) only once. (And "set only once"        * is important for other reasons: run() can still be invoked concurrently in different        * threads, even with the above null checks.)        */
name|timeoutFutureRef
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|delegate
operator|.
name|isDone
argument_list|()
condition|)
block|{
name|timeoutFuture
operator|.
name|setFuture
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
comment|// TODO(lukes): this stack trace is particularly useless (all it does is point at the
comment|// scheduledexecutorservice thread), consider eliminating it altogether?
name|timeoutFuture
operator|.
name|setException
argument_list|(
operator|new
name|TimeoutException
argument_list|(
literal|"Future timed out: "
operator|+
name|delegate
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|delegate
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|afterDone ()
specifier|protected
name|void
name|afterDone
parameter_list|()
block|{
name|maybePropagateCancellation
argument_list|(
name|delegateRef
argument_list|)
expr_stmt|;
name|Future
argument_list|<
name|?
argument_list|>
name|localTimer
init|=
name|timer
decl_stmt|;
comment|// Try to cancel the timer as an optimization.
comment|// timer may be null if this call to run was by the timer task since there is no happens-before
comment|// edge between the assignment to timer and an execution of the timer task.
if|if
condition|(
name|localTimer
operator|!=
literal|null
condition|)
block|{
name|localTimer
operator|.
name|cancel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|delegateRef
operator|=
literal|null
expr_stmt|;
name|timer
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

