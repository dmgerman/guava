begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|min
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|NANOSECONDS
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
name|FutureTask
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
comment|/**  * A {@link FutureTask} that also implements the {@link ListenableFuture} interface. Unlike {@code  * FutureTask}, {@code ListenableFutureTask} does not provide an overrideable {@link  * FutureTask#done() done()} method. For similar functionality, call {@link #addListener}.  *  *<p>Few users should use this class. It is intended primarily for those who are implementing an  * {@code ExecutorService}. Most users should call {@link ListeningExecutorService#submit(Callable)  * ListeningExecutorService.submit} on a service obtained from {@link  * MoreExecutors#listeningDecorator}.  *  * @author Sven Mawson  * @since 1.0  */
end_comment

begin_annotation
annotation|@
name|GwtIncompatible
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|class|ListenableFutureTask
specifier|public
name|class
name|ListenableFutureTask
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|FutureTask
argument_list|<
name|V
argument_list|>
expr|implements
name|ListenableFuture
argument_list|<
name|V
argument_list|>
block|{
comment|// TODO(cpovirk): explore ways of making ListenableFutureTask final. There are some valid reasons
comment|// such as BoundedQueueExecutorService to allow extends but it would be nice to make it final to
comment|// avoid unintended usage.
comment|// The execution list to hold our listeners.
DECL|field|executionList
specifier|private
name|final
name|ExecutionList
name|executionList
operator|=
operator|new
name|ExecutionList
argument_list|()
block|;
comment|/**    * Creates a {@code ListenableFutureTask} that will upon running, execute the given {@code    * Callable}.    *    * @param callable the callable task    * @since 10.0    */
DECL|method|create (Callable<V> callable)
specifier|public
specifier|static
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
name|ListenableFutureTask
argument_list|<
name|V
argument_list|>
name|create
argument_list|(
name|Callable
argument_list|<
name|V
argument_list|>
name|callable
argument_list|)
block|{
return|return
operator|new
name|ListenableFutureTask
argument_list|<
name|V
argument_list|>
argument_list|(
name|callable
argument_list|)
return|;
block|}
comment|/**    * Creates a {@code ListenableFutureTask} that will upon running, execute the given {@code    * Runnable}, and arrange that {@code get} will return the given result on successful completion.    *    * @param runnable the runnable task    * @param result the result to return on successful completion. If you don't need a particular    *     result, consider using constructions of the form: {@code ListenableFuture<?> f =    *     ListenableFutureTask.create(runnable, null)}    * @since 10.0    */
DECL|method|create ( Runnable runnable, @ParametricNullness V result)
specifier|public
specifier|static
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
name|ListenableFutureTask
argument_list|<
name|V
argument_list|>
name|create
argument_list|(
name|Runnable
name|runnable
argument_list|,
annotation|@
name|ParametricNullness
name|V
name|result
argument_list|)
block|{
return|return
operator|new
name|ListenableFutureTask
argument_list|<
name|V
argument_list|>
argument_list|(
name|runnable
argument_list|,
name|result
argument_list|)
return|;
block|}
end_expr_stmt

begin_expr_stmt
DECL|method|ListenableFutureTask (Callable<V> callable)
name|ListenableFutureTask
argument_list|(
name|Callable
argument_list|<
name|V
argument_list|>
name|callable
argument_list|)
block|{
name|super
argument_list|(
name|callable
argument_list|)
block|;   }
DECL|method|ListenableFutureTask (Runnable runnable, @ParametricNullness V result)
name|ListenableFutureTask
argument_list|(
name|Runnable
name|runnable
argument_list|,
annotation|@
name|ParametricNullness
name|V
name|result
argument_list|)
block|{
name|super
argument_list|(
name|runnable
argument_list|,
name|result
argument_list|)
block|;   }
expr|@
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
name|executionList
operator|.
name|add
argument_list|(
name|listener
argument_list|,
name|exec
argument_list|)
block|;   }
expr|@
name|CanIgnoreReturnValue
expr|@
name|Override
expr|@
name|ParametricNullness
DECL|method|get (long timeout, TimeUnit unit)
specifier|public
name|V
name|get
argument_list|(
name|long
name|timeout
argument_list|,
name|TimeUnit
name|unit
argument_list|)
throws|throws
name|TimeoutException
throws|,
name|InterruptedException
throws|,
name|ExecutionException
block|{
name|long
name|timeoutNanos
operator|=
name|unit
operator|.
name|toNanos
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
end_expr_stmt

begin_if
if|if
condition|(
name|timeoutNanos
operator|<=
name|OverflowAvoidingLockSupport
operator|.
name|MAX_NANOSECONDS_THRESHOLD
condition|)
block|{
return|return
name|super
operator|.
name|get
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|)
return|;
block|}
end_if

begin_comment
comment|// Waiting 68 years should be enough for any program.
end_comment

begin_return
return|return
name|super
operator|.
name|get
argument_list|(
name|min
argument_list|(
name|timeoutNanos
argument_list|,
name|OverflowAvoidingLockSupport
operator|.
name|MAX_NANOSECONDS_THRESHOLD
argument_list|)
argument_list|,
name|NANOSECONDS
argument_list|)
return|;
end_return

begin_comment
unit|}
comment|/** Internal implementation detail used to invoke the listeners. */
end_comment

begin_function
unit|@
name|Override
DECL|method|done ()
specifier|protected
name|void
name|done
parameter_list|()
block|{
name|executionList
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
end_function

unit|}
end_unit

