begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2018 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|Futures
operator|.
name|getDone
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
name|util
operator|.
name|concurrent
operator|.
name|MoreExecutors
operator|.
name|directExecutor
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
name|CountDownLatch
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/** Tests for {@link ExecutionSequencer} */
end_comment

begin_class
DECL|class|ExecutionSequencerTest
specifier|public
class|class
name|ExecutionSequencerTest
extends|extends
name|TestCase
block|{
DECL|field|executor
name|ExecutorService
name|executor
decl_stmt|;
DECL|field|serializer
specifier|private
name|ExecutionSequencer
name|serializer
decl_stmt|;
DECL|field|firstFuture
specifier|private
name|SettableFuture
argument_list|<
name|Void
argument_list|>
name|firstFuture
decl_stmt|;
DECL|field|firstCallable
specifier|private
name|TestCallable
name|firstCallable
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|executor
operator|=
name|Executors
operator|.
name|newCachedThreadPool
argument_list|()
expr_stmt|;
name|serializer
operator|=
name|ExecutionSequencer
operator|.
name|create
argument_list|()
expr_stmt|;
name|firstFuture
operator|=
name|SettableFuture
operator|.
name|create
argument_list|()
expr_stmt|;
name|firstCallable
operator|=
operator|new
name|TestCallable
argument_list|(
name|firstFuture
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
DECL|method|testCallableStartsAfterFirstFutureCompletes ()
specifier|public
name|void
name|testCallableStartsAfterFirstFutureCompletes
parameter_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unused"
block|,
literal|"nullness"
block|}
argument_list|)
name|Future
argument_list|<
name|?
argument_list|>
name|possiblyIgnoredError
init|=
name|serializer
operator|.
name|submitAsync
argument_list|(
name|firstCallable
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
name|TestCallable
name|secondCallable
init|=
operator|new
name|TestCallable
argument_list|(
name|Futures
operator|.
expr|<
name|Void
operator|>
name|immediateFuture
argument_list|(
literal|null
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unused"
block|,
literal|"nullness"
block|}
argument_list|)
name|Future
argument_list|<
name|?
argument_list|>
name|possiblyIgnoredError1
init|=
name|serializer
operator|.
name|submitAsync
argument_list|(
name|secondCallable
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|firstCallable
operator|.
name|called
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|secondCallable
operator|.
name|called
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|firstFuture
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|secondCallable
operator|.
name|called
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
DECL|method|testCancellationNotPropagatedIfAlreadyStarted ()
specifier|public
name|void
name|testCancellationNotPropagatedIfAlreadyStarted
parameter_list|()
block|{
name|serializer
operator|.
name|submitAsync
argument_list|(
name|firstCallable
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|firstFuture
operator|.
name|isCancelled
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
DECL|method|testCancellationDoesNotViolateSerialization ()
specifier|public
name|void
name|testCancellationDoesNotViolateSerialization
parameter_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unused"
block|,
literal|"nullness"
block|}
argument_list|)
name|Future
argument_list|<
name|?
argument_list|>
name|possiblyIgnoredError
init|=
name|serializer
operator|.
name|submitAsync
argument_list|(
name|firstCallable
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
name|TestCallable
name|secondCallable
init|=
operator|new
name|TestCallable
argument_list|(
name|Futures
operator|.
expr|<
name|Void
operator|>
name|immediateFuture
argument_list|(
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
name|secondFuture
init|=
name|serializer
operator|.
name|submitAsync
argument_list|(
name|secondCallable
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
name|TestCallable
name|thirdCallable
init|=
operator|new
name|TestCallable
argument_list|(
name|Futures
operator|.
expr|<
name|Void
operator|>
name|immediateFuture
argument_list|(
literal|null
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unused"
block|,
literal|"nullness"
block|}
argument_list|)
name|Future
argument_list|<
name|?
argument_list|>
name|possiblyIgnoredError1
init|=
name|serializer
operator|.
name|submitAsync
argument_list|(
name|thirdCallable
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
name|secondFuture
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|secondCallable
operator|.
name|called
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|thirdCallable
operator|.
name|called
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|firstFuture
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|secondCallable
operator|.
name|called
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|thirdCallable
operator|.
name|called
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
DECL|method|testCancellationMultipleThreads ()
specifier|public
name|void
name|testCancellationMultipleThreads
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BlockingCallable
name|blockingCallable
init|=
operator|new
name|BlockingCallable
argument_list|()
decl_stmt|;
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
name|unused
init|=
name|serializer
operator|.
name|submit
argument_list|(
name|blockingCallable
argument_list|,
name|executor
argument_list|)
decl_stmt|;
name|ListenableFuture
argument_list|<
name|Boolean
argument_list|>
name|future2
init|=
name|serializer
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Boolean
name|call
parameter_list|()
block|{
return|return
name|blockingCallable
operator|.
name|isRunning
argument_list|()
return|;
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
comment|// Wait for the first task to be started in the background. It will block until we explicitly
comment|// stop it.
name|blockingCallable
operator|.
name|waitForStart
argument_list|()
expr_stmt|;
comment|// Give the second task a chance to (incorrectly) start up while the first task is running.
name|assertThat
argument_list|(
name|future2
operator|.
name|isDone
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
comment|// Stop the first task. The second task should then run.
name|blockingCallable
operator|.
name|stop
argument_list|()
expr_stmt|;
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|executor
operator|.
name|awaitTermination
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|getDone
argument_list|(
name|future2
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
DECL|method|testSecondTaskWaitsForFirstEvenIfCancelled ()
specifier|public
name|void
name|testSecondTaskWaitsForFirstEvenIfCancelled
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BlockingCallable
name|blockingCallable
init|=
operator|new
name|BlockingCallable
argument_list|()
decl_stmt|;
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
name|future1
init|=
name|serializer
operator|.
name|submit
argument_list|(
name|blockingCallable
argument_list|,
name|executor
argument_list|)
decl_stmt|;
name|ListenableFuture
argument_list|<
name|Boolean
argument_list|>
name|future2
init|=
name|serializer
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Boolean
name|call
parameter_list|()
block|{
return|return
name|blockingCallable
operator|.
name|isRunning
argument_list|()
return|;
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
comment|// Wait for the first task to be started in the background. It will block until we explicitly
comment|// stop it.
name|blockingCallable
operator|.
name|waitForStart
argument_list|()
expr_stmt|;
comment|// This time, cancel the future for the first task. The task remains running, only the future
comment|// is cancelled.
name|future1
operator|.
name|cancel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// Give the second task a chance to (incorrectly) start up while the first task is running.
comment|// (This is the assertion that fails.)
name|assertThat
argument_list|(
name|future2
operator|.
name|isDone
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
comment|// Stop the first task. The second task should then run.
name|blockingCallable
operator|.
name|stop
argument_list|()
expr_stmt|;
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|executor
operator|.
name|awaitTermination
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|getDone
argument_list|(
name|future2
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|Future
argument_list|<
name|?
argument_list|>
name|first
init|=
name|serializer
operator|.
name|submitAsync
argument_list|(
name|firstCallable
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
name|TestCallable
name|secondCallable
init|=
operator|new
name|TestCallable
argument_list|(
name|SettableFuture
operator|.
expr|<
name|Void
operator|>
name|create
argument_list|()
argument_list|)
decl_stmt|;
name|Future
argument_list|<
name|?
argument_list|>
name|second
init|=
name|serializer
operator|.
name|submitAsync
argument_list|(
name|secondCallable
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|secondCallable
operator|.
name|called
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|second
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|secondCallable
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|firstFuture
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|second
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|secondCallable
operator|.
name|future
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|BlockingCallable
specifier|private
specifier|static
class|class
name|BlockingCallable
implements|implements
name|Callable
argument_list|<
name|Void
argument_list|>
block|{
DECL|field|startLatch
specifier|private
specifier|final
name|CountDownLatch
name|startLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|stopLatch
specifier|private
specifier|final
name|CountDownLatch
name|stopLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|running
specifier|private
specifier|volatile
name|boolean
name|running
init|=
literal|false
decl_stmt|;
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|Void
name|call
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|running
operator|=
literal|true
expr_stmt|;
name|startLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|stopLatch
operator|.
name|await
argument_list|()
expr_stmt|;
name|running
operator|=
literal|false
expr_stmt|;
return|return
literal|null
return|;
block|}
DECL|method|waitForStart ()
specifier|public
name|void
name|waitForStart
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|startLatch
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|stopLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
DECL|method|isRunning ()
specifier|public
name|boolean
name|isRunning
parameter_list|()
block|{
return|return
name|running
return|;
block|}
block|}
DECL|class|TestCallable
specifier|private
specifier|static
specifier|final
class|class
name|TestCallable
implements|implements
name|AsyncCallable
argument_list|<
name|Void
argument_list|>
block|{
DECL|field|future
specifier|private
specifier|final
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
name|future
decl_stmt|;
DECL|field|called
specifier|private
name|boolean
name|called
init|=
literal|false
decl_stmt|;
DECL|method|TestCallable (ListenableFuture<Void> future)
specifier|private
name|TestCallable
parameter_list|(
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
name|future
parameter_list|)
block|{
name|this
operator|.
name|future
operator|=
name|future
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|called
operator|=
literal|true
expr_stmt|;
return|return
name|future
return|;
block|}
block|}
block|}
end_class

end_unit
