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
comment|/**  * {@link FluentFuture} that forwards all calls to a delegate.  *  *<h3>Extension</h3>  *  * If you want a class like {@code FluentFuture} but with extra methods, we recommend declaring your  * own subclass of {@link ListenableFuture}, complete with a method like {@link #from} to adapt an  * existing {@code ListenableFuture}, implemented atop a {@link ForwardingListenableFuture} that  * forwards to that future and adds the desired methods.  */
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
DECL|class|ForwardingFluentFuture
name|final
name|class
name|ForwardingFluentFuture
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|FluentFuture
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
DECL|method|ForwardingFluentFuture (ListenableFuture<V> delegate)
name|ForwardingFluentFuture
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
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
block|;   }
expr|@
name|Override
DECL|method|addListener (Runnable listener, Executor executor)
specifier|public
name|void
name|addListener
argument_list|(
name|Runnable
name|listener
argument_list|,
name|Executor
name|executor
argument_list|)
block|{
name|delegate
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|executor
argument_list|)
block|;   }
expr|@
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

begin_function
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|toString
argument_list|()
return|;
block|}
end_function

unit|}
end_unit

