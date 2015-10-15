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
name|MoreExecutors
operator|.
name|directExecutor
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
name|rejectionPropagatingExecutor
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
name|Uninterruptibles
operator|.
name|getUninterruptibly
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
name|base
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|UndeclaredThrowableException
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
name|CancellationException
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Implementations of {@code Futures.transform*}.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AbstractTransformFuture
specifier|abstract
class|class
name|AbstractTransformFuture
parameter_list|<
name|I
parameter_list|,
name|O
parameter_list|,
name|F
parameter_list|>
extends|extends
name|AbstractFuture
operator|.
name|TrustedFuture
argument_list|<
name|O
argument_list|>
implements|implements
name|Runnable
block|{
DECL|method|create ( ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function)
specifier|static
parameter_list|<
name|I
parameter_list|,
name|O
parameter_list|>
name|ListenableFuture
argument_list|<
name|O
argument_list|>
name|create
parameter_list|(
name|ListenableFuture
argument_list|<
name|I
argument_list|>
name|input
parameter_list|,
name|AsyncFunction
argument_list|<
name|?
super|super
name|I
argument_list|,
name|?
extends|extends
name|O
argument_list|>
name|function
parameter_list|)
block|{
name|AsyncTransformFuture
argument_list|<
name|I
argument_list|,
name|O
argument_list|>
name|output
init|=
operator|new
name|AsyncTransformFuture
argument_list|<
name|I
argument_list|,
name|O
argument_list|>
argument_list|(
name|input
argument_list|,
name|function
argument_list|)
decl_stmt|;
name|input
operator|.
name|addListener
argument_list|(
name|output
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|output
return|;
block|}
DECL|method|create ( ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor)
specifier|static
parameter_list|<
name|I
parameter_list|,
name|O
parameter_list|>
name|ListenableFuture
argument_list|<
name|O
argument_list|>
name|create
parameter_list|(
name|ListenableFuture
argument_list|<
name|I
argument_list|>
name|input
parameter_list|,
name|AsyncFunction
argument_list|<
name|?
super|super
name|I
argument_list|,
name|?
extends|extends
name|O
argument_list|>
name|function
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|AsyncTransformFuture
argument_list|<
name|I
argument_list|,
name|O
argument_list|>
name|output
init|=
operator|new
name|AsyncTransformFuture
argument_list|<
name|I
argument_list|,
name|O
argument_list|>
argument_list|(
name|input
argument_list|,
name|function
argument_list|)
decl_stmt|;
name|input
operator|.
name|addListener
argument_list|(
name|output
argument_list|,
name|rejectionPropagatingExecutor
argument_list|(
name|executor
argument_list|,
name|output
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|output
return|;
block|}
DECL|method|create ( ListenableFuture<I> input, Function<? super I, ? extends O> function)
specifier|static
parameter_list|<
name|I
parameter_list|,
name|O
parameter_list|>
name|ListenableFuture
argument_list|<
name|O
argument_list|>
name|create
parameter_list|(
name|ListenableFuture
argument_list|<
name|I
argument_list|>
name|input
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|I
argument_list|,
name|?
extends|extends
name|O
argument_list|>
name|function
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|function
argument_list|)
expr_stmt|;
name|TransformFuture
argument_list|<
name|I
argument_list|,
name|O
argument_list|>
name|output
init|=
operator|new
name|TransformFuture
argument_list|<
name|I
argument_list|,
name|O
argument_list|>
argument_list|(
name|input
argument_list|,
name|function
argument_list|)
decl_stmt|;
name|input
operator|.
name|addListener
argument_list|(
name|output
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|output
return|;
block|}
DECL|method|create ( ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor)
specifier|static
parameter_list|<
name|I
parameter_list|,
name|O
parameter_list|>
name|ListenableFuture
argument_list|<
name|O
argument_list|>
name|create
parameter_list|(
name|ListenableFuture
argument_list|<
name|I
argument_list|>
name|input
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|I
argument_list|,
name|?
extends|extends
name|O
argument_list|>
name|function
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|function
argument_list|)
expr_stmt|;
name|TransformFuture
argument_list|<
name|I
argument_list|,
name|O
argument_list|>
name|output
init|=
operator|new
name|TransformFuture
argument_list|<
name|I
argument_list|,
name|O
argument_list|>
argument_list|(
name|input
argument_list|,
name|function
argument_list|)
decl_stmt|;
name|input
operator|.
name|addListener
argument_list|(
name|output
argument_list|,
name|rejectionPropagatingExecutor
argument_list|(
name|executor
argument_list|,
name|output
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|output
return|;
block|}
comment|// In theory, this field might not be visible to a cancel() call in certain circumstances. For
comment|// details, see the comments on the fields of TimeoutFuture.
DECL|field|inputFuture
annotation|@
name|Nullable
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|I
argument_list|>
name|inputFuture
decl_stmt|;
DECL|field|function
annotation|@
name|Nullable
name|F
name|function
decl_stmt|;
DECL|method|AbstractTransformFuture (ListenableFuture<? extends I> inputFuture, F function)
name|AbstractTransformFuture
parameter_list|(
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|I
argument_list|>
name|inputFuture
parameter_list|,
name|F
name|function
parameter_list|)
block|{
name|this
operator|.
name|inputFuture
operator|=
name|checkNotNull
argument_list|(
name|inputFuture
argument_list|)
expr_stmt|;
name|this
operator|.
name|function
operator|=
name|checkNotNull
argument_list|(
name|function
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
specifier|final
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|I
argument_list|>
name|localInputFuture
init|=
name|inputFuture
decl_stmt|;
name|F
name|localFunction
init|=
name|function
decl_stmt|;
if|if
condition|(
name|isCancelled
argument_list|()
operator||
name|localInputFuture
operator|==
literal|null
operator||
name|localFunction
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|inputFuture
operator|=
literal|null
expr_stmt|;
name|function
operator|=
literal|null
expr_stmt|;
name|I
name|sourceResult
decl_stmt|;
try|try
block|{
name|sourceResult
operator|=
name|getUninterruptibly
argument_list|(
name|localInputFuture
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CancellationException
name|e
parameter_list|)
block|{
comment|// Cancel this future and return.
comment|// At this point, inputFuture is cancelled and outputFuture doesn't exist, so the value of
comment|// mayInterruptIfRunning is irrelevant.
name|cancel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
comment|// Set the cause of the exception as this future's exception.
name|setException
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|doTransform
argument_list|(
name|localFunction
argument_list|,
name|sourceResult
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|e
parameter_list|)
block|{
comment|// Set the cause of the exception as this future's exception.
name|setException
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// This exception is irrelevant in this thread, but useful for the client.
name|setException
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Template method for subtypes to actually run the transform. */
DECL|method|doTransform (F function, I result)
specifier|abstract
name|void
name|doTransform
parameter_list|(
name|F
name|function
parameter_list|,
name|I
name|result
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|Override
DECL|method|done ()
specifier|final
name|void
name|done
parameter_list|()
block|{
name|maybePropagateCancellation
argument_list|(
name|inputFuture
argument_list|)
expr_stmt|;
name|this
operator|.
name|inputFuture
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|function
operator|=
literal|null
expr_stmt|;
block|}
comment|/**    * An {@link AbstractTransformFuture} that delegates to an {@link AsyncFunction} and    * {@link #setFuture(ListenableFuture)} to implement {@link #doTransform}.    */
DECL|class|AsyncTransformFuture
specifier|private
specifier|static
specifier|final
class|class
name|AsyncTransformFuture
parameter_list|<
name|I
parameter_list|,
name|O
parameter_list|>
extends|extends
name|AbstractTransformFuture
argument_list|<
name|I
argument_list|,
name|O
argument_list|,
name|AsyncFunction
argument_list|<
name|?
super|super
name|I
argument_list|,
name|?
extends|extends
name|O
argument_list|>
argument_list|>
block|{
DECL|method|AsyncTransformFuture ( ListenableFuture<? extends I> inputFuture, AsyncFunction<? super I, ? extends O> function)
name|AsyncTransformFuture
parameter_list|(
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|I
argument_list|>
name|inputFuture
parameter_list|,
name|AsyncFunction
argument_list|<
name|?
super|super
name|I
argument_list|,
name|?
extends|extends
name|O
argument_list|>
name|function
parameter_list|)
block|{
name|super
argument_list|(
name|inputFuture
argument_list|,
name|function
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doTransform (AsyncFunction<? super I, ? extends O> function, I input)
name|void
name|doTransform
parameter_list|(
name|AsyncFunction
argument_list|<
name|?
super|super
name|I
argument_list|,
name|?
extends|extends
name|O
argument_list|>
name|function
parameter_list|,
name|I
name|input
parameter_list|)
throws|throws
name|Exception
block|{
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|O
argument_list|>
name|outputFuture
init|=
name|function
operator|.
name|apply
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|checkNotNull
argument_list|(
name|outputFuture
argument_list|,
literal|"AsyncFunction.apply returned null instead of a Future. "
operator|+
literal|"Did you mean to return immediateFuture(null)?"
argument_list|)
expr_stmt|;
name|setFuture
argument_list|(
name|outputFuture
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * An {@link AbstractTransformFuture} that delegates to a {@link Function} and    * {@link #set(Object)} to implement {@link #doTransform}.    */
DECL|class|TransformFuture
specifier|private
specifier|static
specifier|final
class|class
name|TransformFuture
parameter_list|<
name|I
parameter_list|,
name|O
parameter_list|>
extends|extends
name|AbstractTransformFuture
argument_list|<
name|I
argument_list|,
name|O
argument_list|,
name|Function
argument_list|<
name|?
super|super
name|I
argument_list|,
name|?
extends|extends
name|O
argument_list|>
argument_list|>
block|{
DECL|method|TransformFuture ( ListenableFuture<? extends I> inputFuture, Function<? super I, ? extends O> function)
name|TransformFuture
parameter_list|(
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|I
argument_list|>
name|inputFuture
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|I
argument_list|,
name|?
extends|extends
name|O
argument_list|>
name|function
parameter_list|)
block|{
name|super
argument_list|(
name|inputFuture
argument_list|,
name|function
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doTransform (Function<? super I, ? extends O> function, I input)
name|void
name|doTransform
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|I
argument_list|,
name|?
extends|extends
name|O
argument_list|>
name|function
parameter_list|,
name|I
name|input
parameter_list|)
block|{
comment|// TODO(lukes): move the UndeclaredThrowable catch block here?
name|set
argument_list|(
name|function
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
