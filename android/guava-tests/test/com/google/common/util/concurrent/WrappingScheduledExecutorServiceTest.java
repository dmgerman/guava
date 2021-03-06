begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not  * use this file except in compliance with the License. You may obtain a copy of  * the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the  * License for the specific language governing permissions and limitations under  * the License.  */
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|ScheduledExecutorService
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
name|ScheduledFuture
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Test for {@link WrappingScheduledExecutorService}  *  * @author Luke Sandberg  */
end_comment

begin_class
DECL|class|WrappingScheduledExecutorServiceTest
specifier|public
class|class
name|WrappingScheduledExecutorServiceTest
extends|extends
name|TestCase
block|{
DECL|field|DO_NOTHING
specifier|private
specifier|static
specifier|final
name|Runnable
name|DO_NOTHING
init|=
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
block|{}
block|}
decl_stmt|;
DECL|method|testSchedule ()
specifier|public
name|void
name|testSchedule
parameter_list|()
block|{
name|MockExecutor
name|mock
init|=
operator|new
name|MockExecutor
argument_list|()
decl_stmt|;
name|TestExecutor
name|testExecutor
init|=
operator|new
name|TestExecutor
argument_list|(
name|mock
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
comment|// https://errorprone.info/bugpattern/FutureReturnValueIgnored
name|Future
argument_list|<
name|?
argument_list|>
name|possiblyIgnoredError
init|=
name|testExecutor
operator|.
name|schedule
argument_list|(
name|DO_NOTHING
argument_list|,
literal|10
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|)
decl_stmt|;
name|mock
operator|.
name|assertLastMethodCalled
argument_list|(
literal|"scheduleRunnable"
argument_list|,
literal|10
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
comment|// https://errorprone.info/bugpattern/FutureReturnValueIgnored
name|Future
argument_list|<
name|?
argument_list|>
name|possiblyIgnoredError1
init|=
name|testExecutor
operator|.
name|schedule
argument_list|(
name|Executors
operator|.
name|callable
argument_list|(
name|DO_NOTHING
argument_list|)
argument_list|,
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|mock
operator|.
name|assertLastMethodCalled
argument_list|(
literal|"scheduleCallable"
argument_list|,
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
DECL|method|testSchedule_repeating ()
specifier|public
name|void
name|testSchedule_repeating
parameter_list|()
block|{
name|MockExecutor
name|mock
init|=
operator|new
name|MockExecutor
argument_list|()
decl_stmt|;
name|TestExecutor
name|testExecutor
init|=
operator|new
name|TestExecutor
argument_list|(
name|mock
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
comment|// https://errorprone.info/bugpattern/FutureReturnValueIgnored
name|Future
argument_list|<
name|?
argument_list|>
name|possiblyIgnoredError
init|=
name|testExecutor
operator|.
name|scheduleWithFixedDelay
argument_list|(
name|DO_NOTHING
argument_list|,
literal|100
argument_list|,
literal|10
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|)
decl_stmt|;
name|mock
operator|.
name|assertLastMethodCalled
argument_list|(
literal|"scheduleWithFixedDelay"
argument_list|,
literal|100
argument_list|,
literal|10
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
comment|// https://errorprone.info/bugpattern/FutureReturnValueIgnored
name|Future
argument_list|<
name|?
argument_list|>
name|possiblyIgnoredError1
init|=
name|testExecutor
operator|.
name|scheduleAtFixedRate
argument_list|(
name|DO_NOTHING
argument_list|,
literal|3
argument_list|,
literal|7
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|mock
operator|.
name|assertLastMethodCalled
argument_list|(
literal|"scheduleAtFixedRate"
argument_list|,
literal|3
argument_list|,
literal|7
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
DECL|class|WrappedCallable
specifier|private
specifier|static
specifier|final
class|class
name|WrappedCallable
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Callable
argument_list|<
name|T
argument_list|>
block|{
DECL|field|delegate
specifier|private
specifier|final
name|Callable
argument_list|<
name|T
argument_list|>
name|delegate
decl_stmt|;
DECL|method|WrappedCallable (Callable<T> delegate)
specifier|public
name|WrappedCallable
parameter_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|T
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|delegate
operator|.
name|call
argument_list|()
return|;
block|}
block|}
DECL|class|WrappedRunnable
specifier|private
specifier|static
specifier|final
class|class
name|WrappedRunnable
implements|implements
name|Runnable
block|{
DECL|field|delegate
specifier|private
specifier|final
name|Runnable
name|delegate
decl_stmt|;
DECL|method|WrappedRunnable (Runnable delegate)
specifier|public
name|WrappedRunnable
parameter_list|(
name|Runnable
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|delegate
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|TestExecutor
specifier|private
specifier|static
specifier|final
class|class
name|TestExecutor
extends|extends
name|WrappingScheduledExecutorService
block|{
DECL|method|TestExecutor (MockExecutor mock)
specifier|public
name|TestExecutor
parameter_list|(
name|MockExecutor
name|mock
parameter_list|)
block|{
name|super
argument_list|(
name|mock
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|wrapTask (Callable<T> callable)
specifier|protected
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
block|{
return|return
operator|new
name|WrappedCallable
argument_list|<
name|T
argument_list|>
argument_list|(
name|callable
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|wrapTask (Runnable command)
specifier|protected
name|Runnable
name|wrapTask
parameter_list|(
name|Runnable
name|command
parameter_list|)
block|{
return|return
operator|new
name|WrappedRunnable
argument_list|(
name|command
argument_list|)
return|;
block|}
block|}
DECL|class|MockExecutor
specifier|private
specifier|static
specifier|final
class|class
name|MockExecutor
implements|implements
name|ScheduledExecutorService
block|{
DECL|field|lastMethodCalled
name|String
name|lastMethodCalled
init|=
literal|""
decl_stmt|;
DECL|field|lastInitialDelay
name|long
name|lastInitialDelay
decl_stmt|;
DECL|field|lastDelay
name|long
name|lastDelay
decl_stmt|;
DECL|field|lastUnit
name|TimeUnit
name|lastUnit
decl_stmt|;
DECL|method|assertLastMethodCalled (String method, long delay, TimeUnit unit)
name|void
name|assertLastMethodCalled
parameter_list|(
name|String
name|method
parameter_list|,
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|method
argument_list|,
name|lastMethodCalled
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|delay
argument_list|,
name|lastDelay
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|unit
argument_list|,
name|lastUnit
argument_list|)
expr_stmt|;
block|}
DECL|method|assertLastMethodCalled (String method, long initialDelay, long delay, TimeUnit unit)
name|void
name|assertLastMethodCalled
parameter_list|(
name|String
name|method
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
name|assertEquals
argument_list|(
name|method
argument_list|,
name|lastMethodCalled
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|initialDelay
argument_list|,
name|lastInitialDelay
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|delay
argument_list|,
name|lastDelay
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|unit
argument_list|,
name|lastUnit
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|schedule (Runnable command, long delay, TimeUnit unit)
specifier|public
name|ScheduledFuture
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
name|assertThat
argument_list|(
name|command
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|WrappedRunnable
operator|.
name|class
argument_list|)
expr_stmt|;
name|lastMethodCalled
operator|=
literal|"scheduleRunnable"
expr_stmt|;
name|lastDelay
operator|=
name|delay
expr_stmt|;
name|lastUnit
operator|=
name|unit
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|schedule (Callable<V> callable, long delay, TimeUnit unit)
specifier|public
parameter_list|<
name|V
parameter_list|>
name|ScheduledFuture
argument_list|<
name|V
argument_list|>
name|schedule
parameter_list|(
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
name|assertThat
argument_list|(
name|callable
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|WrappedCallable
operator|.
name|class
argument_list|)
expr_stmt|;
name|lastMethodCalled
operator|=
literal|"scheduleCallable"
expr_stmt|;
name|lastDelay
operator|=
name|delay
expr_stmt|;
name|lastUnit
operator|=
name|unit
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|scheduleAtFixedRate ( Runnable command, long initialDelay, long period, TimeUnit unit)
specifier|public
name|ScheduledFuture
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
name|assertThat
argument_list|(
name|command
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|WrappedRunnable
operator|.
name|class
argument_list|)
expr_stmt|;
name|lastMethodCalled
operator|=
literal|"scheduleAtFixedRate"
expr_stmt|;
name|lastInitialDelay
operator|=
name|initialDelay
expr_stmt|;
name|lastDelay
operator|=
name|period
expr_stmt|;
name|lastUnit
operator|=
name|unit
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|scheduleWithFixedDelay ( Runnable command, long initialDelay, long delay, TimeUnit unit)
specifier|public
name|ScheduledFuture
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
name|assertThat
argument_list|(
name|command
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|WrappedRunnable
operator|.
name|class
argument_list|)
expr_stmt|;
name|lastMethodCalled
operator|=
literal|"scheduleWithFixedDelay"
expr_stmt|;
name|lastInitialDelay
operator|=
name|initialDelay
expr_stmt|;
name|lastDelay
operator|=
name|delay
expr_stmt|;
name|lastUnit
operator|=
name|unit
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|// No need to test these methods as they are handled by WrappingExecutorServiceTest
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
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|invokeAll (Collection<? extends Callable<T>> tasks)
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
name|ExecutionException
throws|,
name|InterruptedException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
name|ExecutionException
throws|,
name|InterruptedException
throws|,
name|TimeoutException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|isShutdown ()
specifier|public
name|boolean
name|isShutdown
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|isTerminated ()
specifier|public
name|boolean
name|isTerminated
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|submit (Callable<T> task)
specifier|public
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|submit (Runnable task)
specifier|public
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|submit (Runnable task, T result)
specifier|public
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

