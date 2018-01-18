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
name|Platform
operator|.
name|isInstanceOfThrowableClass
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
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_comment
comment|/** Implementations of {@code Futures.catching*}. */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AbstractCatchingFuture
specifier|abstract
class|class
name|AbstractCatchingFuture
parameter_list|<
name|V
parameter_list|,
name|X
extends|extends
name|Throwable
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
name|V
argument_list|>
implements|implements
name|Runnable
block|{
DECL|method|create ( ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor)
specifier|static
parameter_list|<
name|V
parameter_list|,
name|X
extends|extends
name|Throwable
parameter_list|>
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|create
parameter_list|(
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|input
parameter_list|,
name|Class
argument_list|<
name|X
argument_list|>
name|exceptionType
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|X
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|fallback
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|CatchingFuture
argument_list|<
name|V
argument_list|,
name|X
argument_list|>
name|future
init|=
operator|new
name|CatchingFuture
argument_list|<>
argument_list|(
name|input
argument_list|,
name|exceptionType
argument_list|,
name|fallback
argument_list|)
decl_stmt|;
name|input
operator|.
name|addListener
argument_list|(
name|future
argument_list|,
name|rejectionPropagatingExecutor
argument_list|(
name|executor
argument_list|,
name|future
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|future
return|;
block|}
DECL|method|create ( ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor)
specifier|static
parameter_list|<
name|X
extends|extends
name|Throwable
parameter_list|,
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
name|?
extends|extends
name|V
argument_list|>
name|input
parameter_list|,
name|Class
argument_list|<
name|X
argument_list|>
name|exceptionType
parameter_list|,
name|AsyncFunction
argument_list|<
name|?
super|super
name|X
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|fallback
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|AsyncCatchingFuture
argument_list|<
name|V
argument_list|,
name|X
argument_list|>
name|future
init|=
operator|new
name|AsyncCatchingFuture
argument_list|<>
argument_list|(
name|input
argument_list|,
name|exceptionType
argument_list|,
name|fallback
argument_list|)
decl_stmt|;
name|input
operator|.
name|addListener
argument_list|(
name|future
argument_list|,
name|rejectionPropagatingExecutor
argument_list|(
name|executor
argument_list|,
name|future
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|future
return|;
block|}
comment|/*    * In certain circumstances, this field might theoretically not be visible to an afterDone() call    * triggered by cancel(). For details, see the comments on the fields of TimeoutFuture.    */
DECL|field|inputFuture
annotation|@
name|NullableDecl
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|inputFuture
decl_stmt|;
DECL|field|exceptionType
annotation|@
name|NullableDecl
name|Class
argument_list|<
name|X
argument_list|>
name|exceptionType
decl_stmt|;
DECL|field|fallback
annotation|@
name|NullableDecl
name|F
name|fallback
decl_stmt|;
DECL|method|AbstractCatchingFuture ( ListenableFuture<? extends V> inputFuture, Class<X> exceptionType, F fallback)
name|AbstractCatchingFuture
parameter_list|(
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|inputFuture
parameter_list|,
name|Class
argument_list|<
name|X
argument_list|>
name|exceptionType
parameter_list|,
name|F
name|fallback
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
name|exceptionType
operator|=
name|checkNotNull
argument_list|(
name|exceptionType
argument_list|)
expr_stmt|;
name|this
operator|.
name|fallback
operator|=
name|checkNotNull
argument_list|(
name|fallback
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
name|V
argument_list|>
name|localInputFuture
init|=
name|inputFuture
decl_stmt|;
name|Class
argument_list|<
name|X
argument_list|>
name|localExceptionType
init|=
name|exceptionType
decl_stmt|;
name|F
name|localFallback
init|=
name|fallback
decl_stmt|;
if|if
condition|(
name|localInputFuture
operator|==
literal|null
operator||
name|localExceptionType
operator|==
literal|null
operator||
name|localFallback
operator|==
literal|null
operator||
name|isCancelled
argument_list|()
condition|)
block|{
return|return;
block|}
name|inputFuture
operator|=
literal|null
expr_stmt|;
comment|// For an explanation of the cases here, see the comments on AbstractTransformFuture.run.
name|V
name|sourceResult
init|=
literal|null
decl_stmt|;
name|Throwable
name|throwable
init|=
literal|null
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
name|ExecutionException
name|e
parameter_list|)
block|{
name|throwable
operator|=
name|checkNotNull
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
name|e
parameter_list|)
block|{
comment|// this includes cancellation exception
name|throwable
operator|=
name|e
expr_stmt|;
block|}
if|if
condition|(
name|throwable
operator|==
literal|null
condition|)
block|{
name|set
argument_list|(
name|sourceResult
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|isInstanceOfThrowableClass
argument_list|(
name|throwable
argument_list|,
name|localExceptionType
argument_list|)
condition|)
block|{
name|setException
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
comment|// TODO(cpovirk): Test that fallback is not run in this case.
return|return;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// verified safe by isInstanceOfThrowableClass
name|X
name|castThrowable
init|=
operator|(
name|X
operator|)
name|throwable
decl_stmt|;
name|T
name|fallbackResult
decl_stmt|;
try|try
block|{
name|fallbackResult
operator|=
name|doFallback
argument_list|(
name|localFallback
argument_list|,
name|castThrowable
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
return|return;
block|}
finally|finally
block|{
name|exceptionType
operator|=
literal|null
expr_stmt|;
name|fallback
operator|=
literal|null
expr_stmt|;
block|}
name|setResult
argument_list|(
name|fallbackResult
argument_list|)
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
name|V
argument_list|>
name|localInputFuture
init|=
name|inputFuture
decl_stmt|;
name|Class
argument_list|<
name|X
argument_list|>
name|localExceptionType
init|=
name|exceptionType
decl_stmt|;
name|F
name|localFallback
init|=
name|fallback
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
name|localExceptionType
operator|!=
literal|null
operator|&&
name|localFallback
operator|!=
literal|null
condition|)
block|{
return|return
name|resultString
operator|+
literal|"exceptionType=["
operator|+
name|localExceptionType
operator|+
literal|"], fallback=["
operator|+
name|localFallback
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
comment|/** Template method for subtypes to actually run the fallback. */
annotation|@
name|ForOverride
annotation|@
name|NullableDecl
DECL|method|doFallback (F fallback, X throwable)
specifier|abstract
name|T
name|doFallback
parameter_list|(
name|F
name|fallback
parameter_list|,
name|X
name|throwable
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/** Template method for subtypes to actually set the result. */
annotation|@
name|ForOverride
DECL|method|setResult (@ullableDecl T result)
specifier|abstract
name|void
name|setResult
parameter_list|(
annotation|@
name|NullableDecl
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
name|exceptionType
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|fallback
operator|=
literal|null
expr_stmt|;
block|}
comment|/**    * An {@link AbstractCatchingFuture} that delegates to an {@link AsyncFunction} and {@link    * #setFuture(ListenableFuture)}.    */
DECL|class|AsyncCatchingFuture
specifier|private
specifier|static
specifier|final
class|class
name|AsyncCatchingFuture
parameter_list|<
name|V
parameter_list|,
name|X
extends|extends
name|Throwable
parameter_list|>
extends|extends
name|AbstractCatchingFuture
argument_list|<
name|V
argument_list|,
name|X
argument_list|,
name|AsyncFunction
argument_list|<
name|?
super|super
name|X
argument_list|,
name|?
extends|extends
name|V
argument_list|>
argument_list|,
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
argument_list|>
block|{
DECL|method|AsyncCatchingFuture ( ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback)
name|AsyncCatchingFuture
parameter_list|(
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|input
parameter_list|,
name|Class
argument_list|<
name|X
argument_list|>
name|exceptionType
parameter_list|,
name|AsyncFunction
argument_list|<
name|?
super|super
name|X
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|fallback
parameter_list|)
block|{
name|super
argument_list|(
name|input
argument_list|,
name|exceptionType
argument_list|,
name|fallback
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doFallback ( AsyncFunction<? super X, ? extends V> fallback, X cause)
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|doFallback
parameter_list|(
name|AsyncFunction
argument_list|<
name|?
super|super
name|X
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|fallback
parameter_list|,
name|X
name|cause
parameter_list|)
throws|throws
name|Exception
block|{
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|replacement
init|=
name|fallback
operator|.
name|apply
argument_list|(
name|cause
argument_list|)
decl_stmt|;
name|checkNotNull
argument_list|(
name|replacement
argument_list|,
literal|"AsyncFunction.apply returned null instead of a Future. "
operator|+
literal|"Did you mean to return immediateFuture(null)?"
argument_list|)
expr_stmt|;
return|return
name|replacement
return|;
block|}
annotation|@
name|Override
DECL|method|setResult (ListenableFuture<? extends V> result)
name|void
name|setResult
parameter_list|(
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
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
comment|/**    * An {@link AbstractCatchingFuture} that delegates to a {@link Function} and {@link    * #set(Object)}.    */
DECL|class|CatchingFuture
specifier|private
specifier|static
specifier|final
class|class
name|CatchingFuture
parameter_list|<
name|V
parameter_list|,
name|X
extends|extends
name|Throwable
parameter_list|>
extends|extends
name|AbstractCatchingFuture
argument_list|<
name|V
argument_list|,
name|X
argument_list|,
name|Function
argument_list|<
name|?
super|super
name|X
argument_list|,
name|?
extends|extends
name|V
argument_list|>
argument_list|,
name|V
argument_list|>
block|{
DECL|method|CatchingFuture ( ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback)
name|CatchingFuture
parameter_list|(
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|input
parameter_list|,
name|Class
argument_list|<
name|X
argument_list|>
name|exceptionType
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|X
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|fallback
parameter_list|)
block|{
name|super
argument_list|(
name|input
argument_list|,
name|exceptionType
argument_list|,
name|fallback
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|NullableDecl
DECL|method|doFallback (Function<? super X, ? extends V> fallback, X cause)
name|V
name|doFallback
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|X
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|fallback
parameter_list|,
name|X
name|cause
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|fallback
operator|.
name|apply
argument_list|(
name|cause
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setResult (@ullableDecl V result)
name|void
name|setResult
parameter_list|(
annotation|@
name|NullableDecl
name|V
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

