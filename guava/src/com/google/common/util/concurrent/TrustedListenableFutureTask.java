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
name|TrutestedFutureInterruptibleTask
name|task
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
operator|new
name|TrutestedFutureInterruptibleTask
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
name|TrutestedFutureInterruptibleTask
name|localTask
init|=
name|task
decl_stmt|;
if|if
condition|(
name|localTask
operator|!=
literal|null
condition|)
block|{
name|localTask
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|done ()
annotation|@
name|Override
name|void
name|done
parameter_list|()
block|{
name|super
operator|.
name|done
argument_list|()
expr_stmt|;
comment|// Free all resources associated with the running task
name|this
operator|.
name|task
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"Interruption not supported"
argument_list|)
DECL|method|interruptTask ()
annotation|@
name|Override
specifier|protected
specifier|final
name|void
name|interruptTask
parameter_list|()
block|{
name|TrutestedFutureInterruptibleTask
name|localTask
init|=
name|task
decl_stmt|;
if|if
condition|(
name|localTask
operator|!=
literal|null
condition|)
block|{
name|localTask
operator|.
name|interruptTask
argument_list|()
expr_stmt|;
block|}
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
DECL|class|TrutestedFutureInterruptibleTask
specifier|final
class|class
name|TrutestedFutureInterruptibleTask
extends|extends
name|InterruptibleTask
block|{
DECL|field|callable
specifier|private
specifier|final
name|Callable
argument_list|<
name|V
argument_list|>
name|callable
decl_stmt|;
DECL|method|TrutestedFutureInterruptibleTask (Callable<V> callable)
name|TrutestedFutureInterruptibleTask
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
name|callable
operator|=
name|checkNotNull
argument_list|(
name|callable
argument_list|)
expr_stmt|;
block|}
DECL|method|runInterruptibly ()
annotation|@
name|Override
name|void
name|runInterruptibly
parameter_list|()
block|{
comment|// Ensure we haven't been cancelled or already run.
if|if
condition|(
operator|!
name|isDone
argument_list|()
condition|)
block|{
try|try
block|{
name|doRun
argument_list|(
name|callable
argument_list|)
expr_stmt|;
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
block|}
block|}
DECL|method|wasInterrupted ()
annotation|@
name|Override
name|boolean
name|wasInterrupted
parameter_list|()
block|{
return|return
name|TrustedListenableFutureTask
operator|.
name|this
operator|.
name|wasInterrupted
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

