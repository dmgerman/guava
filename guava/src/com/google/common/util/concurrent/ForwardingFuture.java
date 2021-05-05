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
name|common
operator|.
name|collect
operator|.
name|ForwardingObject
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
comment|/**  * A {@link Future} which forwards all its method calls to another future. Subclasses should  * override one or more methods to modify the behavior of the backing future as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p>Most subclasses can just use {@link SimpleForwardingFuture}.  *  * @author Sven Mawson  * @since 1.0  */
end_comment

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
name|SuppressWarnings
argument_list|(
literal|"ShouldNotSubclass"
argument_list|)
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|class|ForwardingFuture
specifier|public
specifier|abstract
name|class
name|ForwardingFuture
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|ForwardingObject
expr|implements
name|Future
argument_list|<
name|V
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingFuture ()
specifier|protected
name|ForwardingFuture
argument_list|()
block|{}
expr|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|Future
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|delegate
argument_list|()
block|;    @
name|Override
DECL|method|cancel (boolean mayInterruptIfRunning)
specifier|public
name|boolean
name|cancel
argument_list|(
name|boolean
name|mayInterruptIfRunning
argument_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|cancel
argument_list|(
name|mayInterruptIfRunning
argument_list|)
return|;
block|}
expr|@
name|Override
DECL|method|isCancelled ()
specifier|public
name|boolean
name|isCancelled
argument_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|isCancelled
argument_list|()
return|;
block|}
end_expr_stmt

begin_function
annotation|@
name|Override
DECL|method|isDone ()
specifier|public
name|boolean
name|isDone
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|isDone
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|Override
annotation|@
name|ParametricNullness
DECL|method|get ()
specifier|public
name|V
name|get
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
return|return
name|delegate
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|Override
annotation|@
name|ParametricNullness
DECL|method|get (long timeout, TimeUnit unit)
specifier|public
name|V
name|get
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
throws|,
name|TimeoutException
block|{
return|return
name|delegate
argument_list|()
operator|.
name|get
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|)
return|;
block|}
end_function

begin_comment
comment|// TODO(cpovirk): Use standard Javadoc form for SimpleForwarding* class and constructor
end_comment

begin_comment
comment|/**    * A simplified version of {@link ForwardingFuture} where subclasses can pass in an already    * constructed {@link Future} as the delegate.    *    * @since 9.0    */
end_comment

begin_expr_stmt
DECL|class|SimpleForwardingFuture
specifier|public
specifier|abstract
specifier|static
name|class
name|SimpleForwardingFuture
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
block|{
DECL|field|delegate
specifier|private
name|final
name|Future
argument_list|<
name|V
argument_list|>
name|delegate
block|;
DECL|method|SimpleForwardingFuture (Future<V> delegate)
specifier|protected
name|SimpleForwardingFuture
argument_list|(
name|Future
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
name|Future
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
end_expr_stmt

unit|} }
end_unit

