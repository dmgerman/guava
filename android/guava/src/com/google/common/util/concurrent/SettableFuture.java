begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
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
comment|/**  * A {@link ListenableFuture} whose result can be set by a {@link #set(Object)}, {@link  * #setException(Throwable)} or {@link #setFuture(ListenableFuture)} call. It can also, like any  * other {@code Future}, be {@linkplain #cancel cancelled}.  *  *<p>{@code SettableFuture} is the recommended {@code ListenableFuture} implementation when your  * task cannot be implemented with {@link ListeningExecutorService}, the various {@link Futures}  * utility methods, or {@link ListenableFutureTask}. Those APIs have less opportunity for developer  * error. If your needs are more complex than {@code SettableFuture} supports, use {@link  * AbstractFuture}, which offers an extensible version of the API.  *  * @author Sven Mawson  * @since 9.0 (in 1.0 as {@code ValueFuture})  */
end_comment

begin_annotation
annotation|@
name|GwtCompatible
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|class|SettableFuture
specifier|public
name|final
name|class
name|SettableFuture
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|AbstractFuture
operator|.
name|TrustedFuture
argument_list|<
name|V
argument_list|>
block|{
comment|/**    * Creates a new {@code SettableFuture} that can be completed or cancelled by a later method call.    */
DECL|method|create ()
specifier|public
specifier|static
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
name|SettableFuture
argument_list|<
name|V
argument_list|>
name|create
argument_list|()
block|{
return|return
operator|new
name|SettableFuture
argument_list|<>
argument_list|()
return|;
block|}
expr|@
name|CanIgnoreReturnValue
expr|@
name|Override
DECL|method|set (@arametricNullness V value)
specifier|public
name|boolean
name|set
argument_list|(
annotation|@
name|ParametricNullness
name|V
name|value
argument_list|)
block|{
return|return
name|super
operator|.
name|set
argument_list|(
name|value
argument_list|)
return|;
block|}
end_expr_stmt

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|setException (Throwable throwable)
specifier|public
name|boolean
name|setException
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
return|return
name|super
operator|.
name|setException
argument_list|(
name|throwable
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|setFuture (ListenableFuture<? extends V> future)
specifier|public
name|boolean
name|setFuture
parameter_list|(
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|future
parameter_list|)
block|{
return|return
name|super
operator|.
name|setFuture
argument_list|(
name|future
argument_list|)
return|;
block|}
end_function

begin_constructor
DECL|method|SettableFuture ()
specifier|private
name|SettableFuture
parameter_list|()
block|{}
end_constructor

unit|}
end_unit

