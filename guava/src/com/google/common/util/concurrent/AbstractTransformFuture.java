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
name|Futures
operator|.
name|getDone
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
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|ForOverride
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
comment|/** Implementations of {@code Futures.transform*}. */
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
parameter_list|,
name|T
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
argument_list|<>
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
argument_list|<>
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
comment|/*    * In certain circumstances, this field might theoretically not be visible to an afterDone() call    * triggered by cancel(). For details, see the comments on the fields of TimeoutFuture.    */
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
comment|/*      * Any of the setException() calls below can fail if the output Future is cancelled between now      * and then. This means that we're silently swallowing an exception -- maybe even an Error. But      * this is no worse than what FutureTask does in that situation. Additionally, because the      * Future was cancelled, its listeners have been run, so its consumers will not hang.      *      * Contrast this to the situation we have if setResult() throws, a situation described below.      */
name|I
name|sourceResult
decl_stmt|;
try|try
block|{
name|sourceResult
operator|=
name|getDone
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
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
comment|// Bug in inputFuture.get(). Propagate to the output Future so that its consumers don't hang.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
comment|/*        * StackOverflowError, OutOfMemoryError (e.g., from allocating ExecutionException), or        * something. Try to treat it like a RuntimeException. If we overflow the stack again, the        * resulting Error will propagate upward up to the root call to set().        */
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return;
block|}
name|T
name|transformResult
decl_stmt|;
try|try
block|{
name|transformResult
operator|=
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
return|return;
block|}
finally|finally
block|{
name|function
operator|=
literal|null
expr_stmt|;
block|}
comment|/*      * If set()/setValue() throws an Error, we let it propagate. Why? The most likely Error is a      * StackOverflowError (from deep transform(..., directExecutor()) nesting), and calling      * setException(stackOverflowError) would fail:      *      * - If the stack overflowed before set()/setValue() could even store the result in the output      * Future, then a call setException() would likely also overflow.      *      * - If the stack overflowed after set()/setValue() stored its result, then a call to      * setException() will be a no-op because the Future is already done.      *      * Both scenarios are bad: The output Future might never complete, or, if it does complete, it      * might not run some of its listeners. The likely result is that the app will hang. (And of      * course stack overflows are bad news in general. For example, we may have overflowed in the      * middle of defining a class. If so, that class will never be loadable in this process.) The      * best we can do (since logging may overflow the stack) is to let the error propagate. Because      * it is an Error, it won't be caught and logged by AbstractFuture.executeListener. Instead, it      * can propagate through many layers of AbstractTransformFuture up to the root call to set().      *      * https://github.com/google/guava/issues/2254      *      * Other kinds of Errors are possible:      *      * - OutOfMemoryError from allocations in setFuture(): The calculus here is similar to      * StackOverflowError: We can't reliably call setException(error).      *      * - Any kind of Error from a listener. Even if we could distinguish that case (by exposing some      * extra state from AbstractFuture), our options are limited: A call to setException() would be      * a no-op. We could log, but if that's what we really want, we should modify      * AbstractFuture.executeListener to do so, since that method would have the ability to continue      * to execute other listeners.      *      * What about RuntimeException? If there is a bug in set()/setValue() that produces one, it will      * propagate, too, but only as far as AbstractFuture.executeListener, which will catch and log      * it.      */
name|setResult
argument_list|(
name|transformResult
argument_list|)
expr_stmt|;
block|}
comment|/** Template method for subtypes to actually run the transform. */
annotation|@
name|ForOverride
DECL|method|doTransform (F function, @Nullable I result)
specifier|abstract
annotation|@
name|Nullable
name|T
name|doTransform
parameter_list|(
name|F
name|function
parameter_list|,
annotation|@
name|Nullable
name|I
name|result
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/** Template method for subtypes to actually set the result. */
annotation|@
name|ForOverride
DECL|method|setResult (@ullable T result)
specifier|abstract
name|void
name|setResult
parameter_list|(
annotation|@
name|Nullable
name|T
name|result
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|afterDone ()
specifier|protected
specifier|final
name|void
name|afterDone
parameter_list|()
block|{
name|maybePropagateCancellationTo
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
annotation|@
name|Override
DECL|method|pendingToString ()
specifier|protected
name|String
name|pendingToString
parameter_list|()
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
name|String
name|superString
init|=
name|super
operator|.
name|pendingToString
argument_list|()
decl_stmt|;
name|String
name|resultString
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|localInputFuture
operator|!=
literal|null
condition|)
block|{
name|resultString
operator|=
literal|"inputFuture=["
operator|+
name|localInputFuture
operator|+
literal|"], "
expr_stmt|;
block|}
if|if
condition|(
name|localFunction
operator|!=
literal|null
condition|)
block|{
return|return
name|resultString
operator|+
literal|"function=["
operator|+
name|localFunction
operator|+
literal|"]"
return|;
block|}
elseif|else
if|if
condition|(
name|superString
operator|!=
literal|null
condition|)
block|{
return|return
name|resultString
operator|+
name|superString
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**    * An {@link AbstractTransformFuture} that delegates to an {@link AsyncFunction} and {@link    * #setFuture(ListenableFuture)}.    */
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
argument_list|,
name|ListenableFuture
argument_list|<
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
DECL|method|doTransform ( AsyncFunction<? super I, ? extends O> function, @Nullable I input)
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|O
argument_list|>
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
annotation|@
name|Nullable
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
literal|"Did you mean to return immediateFuture(null)? %s"
argument_list|,
name|function
argument_list|)
expr_stmt|;
return|return
name|outputFuture
return|;
block|}
annotation|@
name|Override
DECL|method|setResult (ListenableFuture<? extends O> result)
name|void
name|setResult
parameter_list|(
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|O
argument_list|>
name|result
parameter_list|)
block|{
name|setFuture
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * An {@link AbstractTransformFuture} that delegates to a {@link Function} and {@link    * #set(Object)}.    */
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
argument_list|,
name|O
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
annotation|@
name|Nullable
DECL|method|doTransform (Function<? super I, ? extends O> function, @Nullable I input)
name|O
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
annotation|@
name|Nullable
name|I
name|input
parameter_list|)
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|input
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setResult (@ullable O result)
name|void
name|setResult
parameter_list|(
annotation|@
name|Nullable
name|O
name|result
parameter_list|)
block|{
name|set
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

