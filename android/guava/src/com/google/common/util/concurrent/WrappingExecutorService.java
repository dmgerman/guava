begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|Throwables
operator|.
name|throwIfUnchecked
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
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|Executors
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

begin_comment
comment|/**  * An abstract {@code ExecutorService} that allows subclasses to {@linkplain #wrapTask(Callable)  * wrap} tasks before they are submitted to the underlying executor.  *  *<p>Note that task wrapping may occur even if the task is never executed.  *  *<p>For delegation without task-wrapping, see {@link ForwardingExecutorService}.  *  * @author Chris Nokleberg  */
end_comment

begin_class
annotation|@
name|CanIgnoreReturnValue
comment|// TODO(cpovirk): Consider being more strict.
annotation|@
name|GwtIncompatible
DECL|class|WrappingExecutorService
specifier|abstract
class|class
name|WrappingExecutorService
implements|implements
name|ExecutorService
block|{
DECL|field|delegate
specifier|private
specifier|final
name|ExecutorService
name|delegate
decl_stmt|;
DECL|method|WrappingExecutorService (ExecutorService delegate)
specifier|protected
name|WrappingExecutorService
parameter_list|(
name|ExecutorService
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
comment|/**    * Wraps a {@code Callable} for submission to the underlying executor. This method is also applied    * to any {@code Runnable} passed to the default implementation of {@link #wrapTask(Runnable)}.    */
DECL|method|wrapTask (Callable<T> callable)
specifier|protected
specifier|abstract
parameter_list|<
name|T
parameter_list|>
name|Callable
argument_list|<
name|T
argument_list|>
name|wrapTask
parameter_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
parameter_list|)
function_decl|;
comment|/**    * Wraps a {@code Runnable} for submission to the underlying executor. The default implementation    * delegates to {@link #wrapTask(Callable)}.    */
DECL|method|wrapTask (Runnable command)
specifier|protected
name|Runnable
name|wrapTask
parameter_list|(
name|Runnable
name|command
parameter_list|)
block|{
specifier|final
name|Callable
argument_list|<
name|Object
argument_list|>
name|wrapped
init|=
name|wrapTask
argument_list|(
name|Executors
operator|.
name|callable
argument_list|(
name|command
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|wrapped
operator|.
name|call
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|throwIfUnchecked
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
return|;
block|}
comment|/**    * Wraps a collection of tasks.    *    * @throws NullPointerException if any element of {@code tasks} is null    */
DECL|method|wrapTasks (Collection<? extends Callable<T>> tasks)
specifier|private
specifier|final
parameter_list|<
name|T
parameter_list|>
name|ImmutableList
argument_list|<
name|Callable
argument_list|<
name|T
argument_list|>
argument_list|>
name|wrapTasks
parameter_list|(
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
parameter_list|)
block|{
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|Callable
argument_list|<
name|T
argument_list|>
argument_list|>
name|builder
init|=
name|ImmutableList
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Callable
argument_list|<
name|T
argument_list|>
name|task
range|:
name|tasks
control|)
block|{
name|builder
operator|.
name|add
argument_list|(
name|wrapTask
argument_list|(
name|task
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
comment|// These methods wrap before delegating.
annotation|@
name|Override
DECL|method|execute (Runnable command)
specifier|public
specifier|final
name|void
name|execute
parameter_list|(
name|Runnable
name|command
parameter_list|)
block|{
name|delegate
operator|.
name|execute
argument_list|(
name|wrapTask
argument_list|(
name|command
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|submit (Callable<T> task)
specifier|public
specifier|final
parameter_list|<
name|T
parameter_list|>
name|Future
argument_list|<
name|T
argument_list|>
name|submit
parameter_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|task
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|submit
argument_list|(
name|wrapTask
argument_list|(
name|checkNotNull
argument_list|(
name|task
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|submit (Runnable task)
specifier|public
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|submit
parameter_list|(
name|Runnable
name|task
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|submit
argument_list|(
name|wrapTask
argument_list|(
name|task
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|submit (Runnable task, T result)
specifier|public
specifier|final
parameter_list|<
name|T
parameter_list|>
name|Future
argument_list|<
name|T
argument_list|>
name|submit
parameter_list|(
name|Runnable
name|task
parameter_list|,
name|T
name|result
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|submit
argument_list|(
name|wrapTask
argument_list|(
name|task
argument_list|)
argument_list|,
name|result
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|invokeAll (Collection<? extends Callable<T>> tasks)
specifier|public
specifier|final
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|Future
argument_list|<
name|T
argument_list|>
argument_list|>
name|invokeAll
parameter_list|(
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
parameter_list|)
throws|throws
name|InterruptedException
block|{
return|return
name|delegate
operator|.
name|invokeAll
argument_list|(
name|wrapTasks
argument_list|(
name|tasks
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|invokeAll ( Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
specifier|public
specifier|final
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|Future
argument_list|<
name|T
argument_list|>
argument_list|>
name|invokeAll
parameter_list|(
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
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
block|{
return|return
name|delegate
operator|.
name|invokeAll
argument_list|(
name|wrapTasks
argument_list|(
name|tasks
argument_list|)
argument_list|,
name|timeout
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|invokeAny (Collection<? extends Callable<T>> tasks)
specifier|public
specifier|final
parameter_list|<
name|T
parameter_list|>
name|T
name|invokeAny
parameter_list|(
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
parameter_list|)
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
return|return
name|delegate
operator|.
name|invokeAny
argument_list|(
name|wrapTasks
argument_list|(
name|tasks
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|invokeAny (Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
specifier|public
specifier|final
parameter_list|<
name|T
parameter_list|>
name|T
name|invokeAny
parameter_list|(
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
parameter_list|,
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
name|invokeAny
argument_list|(
name|wrapTasks
argument_list|(
name|tasks
argument_list|)
argument_list|,
name|timeout
argument_list|,
name|unit
argument_list|)
return|;
block|}
comment|// The remaining methods just delegate.
annotation|@
name|Override
DECL|method|shutdown ()
specifier|public
specifier|final
name|void
name|shutdown
parameter_list|()
block|{
name|delegate
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|shutdownNow ()
specifier|public
specifier|final
name|List
argument_list|<
name|Runnable
argument_list|>
name|shutdownNow
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|shutdownNow
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isShutdown ()
specifier|public
specifier|final
name|boolean
name|isShutdown
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|isShutdown
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isTerminated ()
specifier|public
specifier|final
name|boolean
name|isTerminated
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|isTerminated
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|awaitTermination (long timeout, TimeUnit unit)
specifier|public
specifier|final
name|boolean
name|awaitTermination
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
block|{
return|return
name|delegate
operator|.
name|awaitTermination
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|)
return|;
block|}
block|}
end_class

end_unit
