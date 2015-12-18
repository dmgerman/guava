begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.util.concurrent.testing
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
operator|.
name|testing
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
name|util
operator|.
name|concurrent
operator|.
name|MoreExecutors
operator|.
name|newDirectExecutorService
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
name|util
operator|.
name|concurrent
operator|.
name|ForwardingListenableFuture
operator|.
name|SimpleForwardingListenableFuture
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
name|util
operator|.
name|concurrent
operator|.
name|ListenableFuture
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
name|util
operator|.
name|concurrent
operator|.
name|ListenableScheduledFuture
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
name|util
operator|.
name|concurrent
operator|.
name|ListeningExecutorService
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
name|util
operator|.
name|concurrent
operator|.
name|ListeningScheduledExecutorService
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
name|AbstractExecutorService
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
name|Delayed
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

begin_comment
comment|/**  * A ScheduledExecutorService that executes all scheduled actions immediately  * in the calling thread.  *  * See {@link TestingExecutors#sameThreadScheduledExecutor()} for a full list of  * constraints.  *  * @author John Sirois  * @author Zach van Schouwen  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|SameThreadScheduledExecutorService
class|class
name|SameThreadScheduledExecutorService
extends|extends
name|AbstractExecutorService
implements|implements
name|ListeningScheduledExecutorService
block|{
DECL|field|delegate
specifier|private
specifier|final
name|ListeningExecutorService
name|delegate
init|=
name|newDirectExecutorService
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|shutdown ()
specifier|public
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
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|unit
argument_list|,
literal|"unit must not be null!"
argument_list|)
expr_stmt|;
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
annotation|@
name|Override
DECL|method|submit (Callable<T> task)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ListenableFuture
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
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|task
argument_list|,
literal|"task must not be null!"
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|submit
argument_list|(
name|task
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|submit (Runnable task, T result)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ListenableFuture
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
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|task
argument_list|,
literal|"task must not be null!"
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|result
argument_list|,
literal|"result must not be null!"
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|submit
argument_list|(
name|task
argument_list|,
name|result
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|submit (Runnable task)
specifier|public
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|submit
parameter_list|(
name|Runnable
name|task
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|task
argument_list|,
literal|"task must not be null!"
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|submit
argument_list|(
name|task
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|invokeAll ( Collection<? extends Callable<T>> tasks)
specifier|public
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
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|tasks
argument_list|,
literal|"tasks must not be null!"
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|invokeAll
argument_list|(
name|tasks
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|invokeAll ( Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
specifier|public
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
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|tasks
argument_list|,
literal|"tasks must not be null!"
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|unit
argument_list|,
literal|"unit must not be null!"
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|invokeAll
argument_list|(
name|tasks
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
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|tasks
argument_list|,
literal|"tasks must not be null!"
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|invokeAny
argument_list|(
name|tasks
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|invokeAny (Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
specifier|public
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
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|tasks
argument_list|,
literal|"tasks must not be null!"
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|unit
argument_list|,
literal|"unit must not be null!"
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|invokeAny
argument_list|(
name|tasks
argument_list|,
name|timeout
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|execute (Runnable command)
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|command
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|command
argument_list|,
literal|"command must not be null!"
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|execute
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|schedule (Runnable command, long delay, TimeUnit unit)
specifier|public
name|ListenableScheduledFuture
argument_list|<
name|?
argument_list|>
name|schedule
parameter_list|(
name|Runnable
name|command
parameter_list|,
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|command
argument_list|,
literal|"command must not be null"
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|unit
argument_list|,
literal|"unit must not be null!"
argument_list|)
expr_stmt|;
return|return
name|schedule
argument_list|(
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
operator|.
name|callable
argument_list|(
name|command
argument_list|)
argument_list|,
name|delay
argument_list|,
name|unit
argument_list|)
return|;
block|}
DECL|class|ImmediateScheduledFuture
specifier|private
specifier|static
class|class
name|ImmediateScheduledFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|SimpleForwardingListenableFuture
argument_list|<
name|V
argument_list|>
implements|implements
name|ListenableScheduledFuture
argument_list|<
name|V
argument_list|>
block|{
DECL|field|exception
specifier|private
name|ExecutionException
name|exception
decl_stmt|;
DECL|method|ImmediateScheduledFuture (ListenableFuture<V> future)
specifier|protected
name|ImmediateScheduledFuture
parameter_list|(
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|future
parameter_list|)
block|{
name|super
argument_list|(
name|future
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|unit
argument_list|,
literal|"unit must not be null!"
argument_list|)
expr_stmt|;
return|return
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getDelay (TimeUnit unit)
specifier|public
name|long
name|getDelay
parameter_list|(
name|TimeUnit
name|unit
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|unit
argument_list|,
literal|"unit must not be null!"
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|compareTo (Delayed other)
specifier|public
name|int
name|compareTo
parameter_list|(
name|Delayed
name|other
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|other
argument_list|,
literal|"other must not be null!"
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|schedule (final Callable<V> callable, long delay, TimeUnit unit)
specifier|public
parameter_list|<
name|V
parameter_list|>
name|ListenableScheduledFuture
argument_list|<
name|V
argument_list|>
name|schedule
parameter_list|(
specifier|final
name|Callable
argument_list|<
name|V
argument_list|>
name|callable
parameter_list|,
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|callable
argument_list|,
literal|"callable must not be null!"
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|unit
argument_list|,
literal|"unit must not be null!"
argument_list|)
expr_stmt|;
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|delegateFuture
init|=
name|submit
argument_list|(
name|callable
argument_list|)
decl_stmt|;
return|return
operator|new
name|ImmediateScheduledFuture
argument_list|<
name|V
argument_list|>
argument_list|(
name|delegateFuture
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|scheduleAtFixedRate (Runnable command, long initialDelay, long period, TimeUnit unit)
specifier|public
name|ListenableScheduledFuture
argument_list|<
name|?
argument_list|>
name|scheduleAtFixedRate
parameter_list|(
name|Runnable
name|command
parameter_list|,
name|long
name|initialDelay
parameter_list|,
name|long
name|period
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"scheduleAtFixedRate is not supported."
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|scheduleWithFixedDelay (Runnable command, long initialDelay, long delay, TimeUnit unit)
specifier|public
name|ListenableScheduledFuture
argument_list|<
name|?
argument_list|>
name|scheduleWithFixedDelay
parameter_list|(
name|Runnable
name|command
parameter_list|,
name|long
name|initialDelay
parameter_list|,
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"scheduleWithFixedDelay is not supported."
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

