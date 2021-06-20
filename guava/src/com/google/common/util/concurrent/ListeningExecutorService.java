begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|DoNotMock
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|ExecutorService
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
name|RejectedExecutionException
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
comment|/**  * An {@link ExecutorService} that returns {@link ListenableFuture} instances. To create an instance  * from an existing {@link ExecutorService}, call {@link  * MoreExecutors#listeningDecorator(ExecutorService)}.  *  * @author Chris Povirk  * @since 10.0  */
end_comment

begin_interface
annotation|@
name|DoNotMock
argument_list|(
literal|"Use TestingExecutors.sameThreadScheduledExecutor, or wrap a real Executor from "
operator|+
literal|"java.util.concurrent.Executors with MoreExecutors.listeningDecorator"
argument_list|)
annotation|@
name|GwtIncompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|interface|ListeningExecutorService
specifier|public
interface|interface
name|ListeningExecutorService
extends|extends
name|ExecutorService
block|{
comment|/**    * @return a {@code ListenableFuture} representing pending completion of the task    * @throws RejectedExecutionException {@inheritDoc}    */
annotation|@
name|Override
DECL|method|submit (Callable<T> task)
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|ListenableFuture
argument_list|<
name|T
argument_list|>
name|submit
argument_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|task
argument_list|)
expr_stmt|;
comment|/**    * @return a {@code ListenableFuture} representing pending completion of the task    * @throws RejectedExecutionException {@inheritDoc}    */
annotation|@
name|Override
DECL|method|submit (Runnable task)
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|submit
parameter_list|(
name|Runnable
name|task
parameter_list|)
function_decl|;
comment|/**    * @return a {@code ListenableFuture} representing pending completion of the task    * @throws RejectedExecutionException {@inheritDoc}    */
annotation|@
name|Override
DECL|method|submit ( Runnable task, @ParametricNullness T result)
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|ListenableFuture
argument_list|<
name|T
argument_list|>
name|submit
argument_list|(
name|Runnable
name|task
argument_list|,
annotation|@
name|ParametricNullness
name|T
name|result
argument_list|)
expr_stmt|;
comment|/**    * {@inheritDoc}    *    *<p>All elements in the returned list must be {@link ListenableFuture} instances. The easiest    * way to obtain a {@code List<ListenableFuture<T>>} from this method is an unchecked (but safe)    * cast:    *    *<pre>    *   {@code @SuppressWarnings("unchecked") // guaranteed by invokeAll contract}    *   {@code List<ListenableFuture<T>> futures = (List) executor.invokeAll(tasks);}    *</pre>    *    * @return A list of {@code ListenableFuture} instances representing the tasks, in the same    *     sequential order as produced by the iterator for the given task list, each of which has    *     completed.    * @throws RejectedExecutionException {@inheritDoc}    * @throws NullPointerException if any task is null    */
annotation|@
name|Override
DECL|method|invokeAll (Collection<? extends Callable<T>> tasks)
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|List
argument_list|<
name|Future
argument_list|<
name|T
argument_list|>
argument_list|>
name|invokeAll
argument_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Callable
argument_list|<
name|T
argument_list|>
argument_list|>
name|tasks
argument_list|)
throws|throws
name|InterruptedException
expr_stmt|;
comment|/**    * {@inheritDoc}    *    *<p>All elements in the returned list must be {@link ListenableFuture} instances. The easiest    * way to obtain a {@code List<ListenableFuture<T>>} from this method is an unchecked (but safe)    * cast:    *    *<pre>    *   {@code @SuppressWarnings("unchecked") // guaranteed by invokeAll contract}    *   {@code List<ListenableFuture<T>> futures = (List) executor.invokeAll(tasks, timeout, unit);}    *</pre>    *    * @return a list of {@code ListenableFuture} instances representing the tasks, in the same    *     sequential order as produced by the iterator for the given task list. If the operation did    *     not time out, each task will have completed. If it did time out, some of these tasks will    *     not have completed.    * @throws RejectedExecutionException {@inheritDoc}    * @throws NullPointerException if any task is null    */
annotation|@
name|Override
DECL|method|invokeAll ( Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|List
argument_list|<
name|Future
argument_list|<
name|T
argument_list|>
argument_list|>
name|invokeAll
argument_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Callable
argument_list|<
name|T
argument_list|>
argument_list|>
name|tasks
operator|,
name|long
name|timeout
operator|,
name|TimeUnit
name|unit
argument_list|)
throws|throws
name|InterruptedException
expr_stmt|;
block|}
end_interface

end_unit

