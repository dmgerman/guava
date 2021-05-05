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
name|common
operator|.
name|base
operator|.
name|Preconditions
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
comment|/**  * A {@link ListenableFuture} which forwards all its method calls to another future. Subclasses  * should override one or more methods to modify the behavior of the backing future as desired per  * the<a href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p>Most subclasses can just use {@link SimpleForwardingListenableFuture}.  *  * @author Shardul Deo  * @since 4.0  */
end_comment

begin_annotation
annotation|@
name|SuppressWarnings
argument_list|(
literal|"ShouldNotSubclass"
argument_list|)
end_annotation

begin_annotation
annotation|@
name|CanIgnoreReturnValue
end_annotation

begin_comment
comment|// TODO(cpovirk): Consider being more strict.
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
DECL|class|ForwardingListenableFuture
specifier|public
specifier|abstract
name|class
name|ForwardingListenableFuture
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|ForwardingFuture
argument_list|<
name|V
argument_list|>
expr|implements
name|ListenableFuture
argument_list|<
name|V
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingListenableFuture ()
specifier|protected
name|ForwardingListenableFuture
argument_list|()
block|{}
expr|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|delegate
argument_list|()
block|;    @
name|Override
DECL|method|addListener (Runnable listener, Executor exec)
specifier|public
name|void
name|addListener
argument_list|(
name|Runnable
name|listener
argument_list|,
name|Executor
name|exec
argument_list|)
block|{
name|delegate
argument_list|()
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|exec
argument_list|)
block|;   }
comment|// TODO(cpovirk): Use standard Javadoc form for SimpleForwarding* class and constructor
comment|/**    * A simplified version of {@link ForwardingListenableFuture} where subclasses can pass in an    * already constructed {@link ListenableFuture} as the delegate.    *    * @since 9.0    */
DECL|class|SimpleForwardingListenableFuture
specifier|public
specifier|abstract
specifier|static
name|class
name|SimpleForwardingListenableFuture
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|ForwardingListenableFuture
argument_list|<
name|V
argument_list|>
block|{
DECL|field|delegate
specifier|private
name|final
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|delegate
block|;
DECL|method|SimpleForwardingListenableFuture (ListenableFuture<V> delegate)
specifier|protected
name|SimpleForwardingListenableFuture
argument_list|(
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|delegate
argument_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
block|;     }
expr|@
name|Override
DECL|method|delegate ()
specifier|protected
name|final
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|delegate
argument_list|()
block|{
return|return
name|delegate
return|;
block|}
block|}
end_expr_stmt

unit|}
end_unit

