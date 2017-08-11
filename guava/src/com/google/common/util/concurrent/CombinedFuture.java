begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|collect
operator|.
name|ImmutableCollection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|j2objc
operator|.
name|annotations
operator|.
name|WeakOuter
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|RejectedExecutionException
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
comment|/**  * Aggregate future that computes its value by calling a callable.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|CombinedFuture
specifier|final
class|class
name|CombinedFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|AggregateFuture
argument_list|<
name|Object
argument_list|,
name|V
argument_list|>
block|{
DECL|method|CombinedFuture ( ImmutableCollection<? extends ListenableFuture<?>> futures, boolean allMustSucceed, Executor listenerExecutor, AsyncCallable<V> callable)
name|CombinedFuture
parameter_list|(
name|ImmutableCollection
argument_list|<
name|?
extends|extends
name|ListenableFuture
argument_list|<
name|?
argument_list|>
argument_list|>
name|futures
parameter_list|,
name|boolean
name|allMustSucceed
parameter_list|,
name|Executor
name|listenerExecutor
parameter_list|,
name|AsyncCallable
argument_list|<
name|V
argument_list|>
name|callable
parameter_list|)
block|{
name|init
argument_list|(
operator|new
name|CombinedFutureRunningState
argument_list|(
name|futures
argument_list|,
name|allMustSucceed
argument_list|,
operator|new
name|AsyncCallableInterruptibleTask
argument_list|(
name|callable
argument_list|,
name|listenerExecutor
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|CombinedFuture ( ImmutableCollection<? extends ListenableFuture<?>> futures, boolean allMustSucceed, Executor listenerExecutor, Callable<V> callable)
name|CombinedFuture
parameter_list|(
name|ImmutableCollection
argument_list|<
name|?
extends|extends
name|ListenableFuture
argument_list|<
name|?
argument_list|>
argument_list|>
name|futures
parameter_list|,
name|boolean
name|allMustSucceed
parameter_list|,
name|Executor
name|listenerExecutor
parameter_list|,
name|Callable
argument_list|<
name|V
argument_list|>
name|callable
parameter_list|)
block|{
name|init
argument_list|(
operator|new
name|CombinedFutureRunningState
argument_list|(
name|futures
argument_list|,
name|allMustSucceed
argument_list|,
operator|new
name|CallableInterruptibleTask
argument_list|(
name|callable
argument_list|,
name|listenerExecutor
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|CombinedFutureRunningState
specifier|private
specifier|final
class|class
name|CombinedFutureRunningState
extends|extends
name|RunningState
block|{
DECL|field|task
specifier|private
name|CombinedFutureInterruptibleTask
name|task
decl_stmt|;
DECL|method|CombinedFutureRunningState ( ImmutableCollection<? extends ListenableFuture<?>> futures, boolean allMustSucceed, CombinedFutureInterruptibleTask task)
name|CombinedFutureRunningState
parameter_list|(
name|ImmutableCollection
argument_list|<
name|?
extends|extends
name|ListenableFuture
argument_list|<
name|?
argument_list|>
argument_list|>
name|futures
parameter_list|,
name|boolean
name|allMustSucceed
parameter_list|,
name|CombinedFutureInterruptibleTask
name|task
parameter_list|)
block|{
name|super
argument_list|(
name|futures
argument_list|,
name|allMustSucceed
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|task
operator|=
name|task
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|collectOneValue (boolean allMustSucceed, int index, @Nullable Object returnValue)
name|void
name|collectOneValue
parameter_list|(
name|boolean
name|allMustSucceed
parameter_list|,
name|int
name|index
parameter_list|,
annotation|@
name|Nullable
name|Object
name|returnValue
parameter_list|)
block|{}
annotation|@
name|Override
DECL|method|handleAllCompleted ()
name|void
name|handleAllCompleted
parameter_list|()
block|{
name|CombinedFutureInterruptibleTask
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
name|execute
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|checkState
argument_list|(
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|releaseResourcesAfterFailure ()
name|void
name|releaseResourcesAfterFailure
parameter_list|()
block|{
name|super
operator|.
name|releaseResourcesAfterFailure
argument_list|()
expr_stmt|;
name|this
operator|.
name|task
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|interruptTask ()
name|void
name|interruptTask
parameter_list|()
block|{
name|CombinedFutureInterruptibleTask
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
block|}
annotation|@
name|WeakOuter
DECL|class|CombinedFutureInterruptibleTask
specifier|private
specifier|abstract
class|class
name|CombinedFutureInterruptibleTask
parameter_list|<
name|T
parameter_list|>
extends|extends
name|InterruptibleTask
argument_list|<
name|T
argument_list|>
block|{
DECL|field|listenerExecutor
specifier|private
specifier|final
name|Executor
name|listenerExecutor
decl_stmt|;
DECL|field|thrownByExecute
specifier|volatile
name|boolean
name|thrownByExecute
init|=
literal|true
decl_stmt|;
DECL|method|CombinedFutureInterruptibleTask (Executor listenerExecutor)
specifier|public
name|CombinedFutureInterruptibleTask
parameter_list|(
name|Executor
name|listenerExecutor
parameter_list|)
block|{
name|this
operator|.
name|listenerExecutor
operator|=
name|checkNotNull
argument_list|(
name|listenerExecutor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isDone ()
specifier|final
name|boolean
name|isDone
parameter_list|()
block|{
return|return
name|CombinedFuture
operator|.
name|this
operator|.
name|isDone
argument_list|()
return|;
block|}
DECL|method|execute ()
specifier|final
name|void
name|execute
parameter_list|()
block|{
try|try
block|{
name|listenerExecutor
operator|.
name|execute
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RejectedExecutionException
name|e
parameter_list|)
block|{
if|if
condition|(
name|thrownByExecute
condition|)
block|{
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|afterRanInterruptibly (T result, Throwable error)
specifier|final
name|void
name|afterRanInterruptibly
parameter_list|(
name|T
name|result
parameter_list|,
name|Throwable
name|error
parameter_list|)
block|{
if|if
condition|(
name|error
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|error
operator|instanceof
name|ExecutionException
condition|)
block|{
name|setException
argument_list|(
name|error
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|error
operator|instanceof
name|CancellationException
condition|)
block|{
name|cancel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setException
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|setValue
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setValue (T value)
specifier|abstract
name|void
name|setValue
parameter_list|(
name|T
name|value
parameter_list|)
function_decl|;
block|}
annotation|@
name|WeakOuter
DECL|class|AsyncCallableInterruptibleTask
specifier|private
specifier|final
class|class
name|AsyncCallableInterruptibleTask
extends|extends
name|CombinedFutureInterruptibleTask
argument_list|<
name|ListenableFuture
argument_list|<
name|V
argument_list|>
argument_list|>
block|{
DECL|field|callable
specifier|private
specifier|final
name|AsyncCallable
argument_list|<
name|V
argument_list|>
name|callable
decl_stmt|;
DECL|method|AsyncCallableInterruptibleTask (AsyncCallable<V> callable, Executor listenerExecutor)
specifier|public
name|AsyncCallableInterruptibleTask
parameter_list|(
name|AsyncCallable
argument_list|<
name|V
argument_list|>
name|callable
parameter_list|,
name|Executor
name|listenerExecutor
parameter_list|)
block|{
name|super
argument_list|(
name|listenerExecutor
argument_list|)
expr_stmt|;
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
annotation|@
name|Override
DECL|method|runInterruptibly ()
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|runInterruptibly
parameter_list|()
throws|throws
name|Exception
block|{
name|thrownByExecute
operator|=
literal|false
expr_stmt|;
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|result
init|=
name|callable
operator|.
name|call
argument_list|()
decl_stmt|;
return|return
name|checkNotNull
argument_list|(
name|result
argument_list|,
literal|"AsyncCallable.call returned null instead of a Future. "
operator|+
literal|"Did you mean to return immediateFuture(null)?"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setValue (ListenableFuture<V> value)
name|void
name|setValue
parameter_list|(
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|value
parameter_list|)
block|{
name|setFuture
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|callable
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
annotation|@
name|WeakOuter
DECL|class|CallableInterruptibleTask
specifier|private
specifier|final
class|class
name|CallableInterruptibleTask
extends|extends
name|CombinedFutureInterruptibleTask
argument_list|<
name|V
argument_list|>
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
DECL|method|CallableInterruptibleTask (Callable<V> callable, Executor listenerExecutor)
specifier|public
name|CallableInterruptibleTask
parameter_list|(
name|Callable
argument_list|<
name|V
argument_list|>
name|callable
parameter_list|,
name|Executor
name|listenerExecutor
parameter_list|)
block|{
name|super
argument_list|(
name|listenerExecutor
argument_list|)
expr_stmt|;
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
annotation|@
name|Override
DECL|method|runInterruptibly ()
name|V
name|runInterruptibly
parameter_list|()
throws|throws
name|Exception
block|{
name|thrownByExecute
operator|=
literal|false
expr_stmt|;
return|return
name|callable
operator|.
name|call
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setValue (V value)
name|void
name|setValue
parameter_list|(
name|V
name|value
parameter_list|)
block|{
name|CombinedFuture
operator|.
name|this
operator|.
name|set
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|callable
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

