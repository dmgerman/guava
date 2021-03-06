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
name|allAsList
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
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
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
name|Function
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
name|testing
operator|.
name|GcFinalization
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
name|testing
operator|.
name|TestLogHandler
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
name|J2ObjCIncompatible
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|WeakReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|logging
operator|.
name|Logger
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
annotation|@
name|GwtIncompatible
annotation|@
name|J2ObjCIncompatible
comment|// gc
annotation|@
name|AndroidIncompatible
DECL|method|testCancellationWithReferencedObject ()
specifier|public
name|void
name|testCancellationWithReferencedObject
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|toBeGCed
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|WeakReference
argument_list|<
name|Object
argument_list|>
name|ref
init|=
operator|new
name|WeakReference
argument_list|<>
argument_list|(
name|toBeGCed
argument_list|)
decl_stmt|;
specifier|final
name|SettableFuture
argument_list|<
name|Void
argument_list|>
name|settableFuture
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|ignored
init|=
name|serializer
operator|.
name|submitAsync
argument_list|(
operator|new
name|AsyncCallable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
name|call
parameter_list|()
block|{
return|return
name|settableFuture
return|;
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
name|serializer
operator|.
name|submit
argument_list|(
name|toStringCallable
argument_list|(
name|toBeGCed
argument_list|)
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
name|toBeGCed
operator|=
literal|null
expr_stmt|;
name|GcFinalization
operator|.
name|awaitClear
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
DECL|method|toStringCallable (final Object object)
specifier|private
specifier|static
name|Callable
argument_list|<
name|String
argument_list|>
name|toStringCallable
parameter_list|(
specifier|final
name|Object
name|object
parameter_list|)
block|{
return|return
operator|new
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|call
parameter_list|()
block|{
return|return
name|object
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|testCancellationDuringReentrancy ()
specifier|public
name|void
name|testCancellationDuringReentrancy
parameter_list|()
throws|throws
name|Exception
block|{
name|TestLogHandler
name|logHandler
init|=
operator|new
name|TestLogHandler
argument_list|()
decl_stmt|;
name|Logger
operator|.
name|getLogger
argument_list|(
name|AbstractFuture
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|addHandler
argument_list|(
name|logHandler
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Future
argument_list|<
name|?
argument_list|>
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Runnable
index|[]
name|manualExecutorTask
init|=
operator|new
name|Runnable
index|[
literal|1
index|]
decl_stmt|;
name|Executor
name|manualExecutor
init|=
operator|new
name|Executor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|task
parameter_list|)
block|{
name|manualExecutorTask
index|[
literal|0
index|]
operator|=
name|task
expr_stmt|;
block|}
block|}
decl_stmt|;
name|results
operator|.
name|add
argument_list|(
name|serializer
operator|.
name|submit
argument_list|(
name|Callables
operator|.
name|returning
argument_list|(
literal|null
argument_list|)
argument_list|,
name|manualExecutor
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
index|[]
name|thingToCancel
init|=
operator|new
name|Future
argument_list|<
name|?
argument_list|>
index|[
literal|1
index|]
decl_stmt|;
name|results
operator|.
name|add
argument_list|(
name|serializer
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|()
block|{
name|thingToCancel
index|[
literal|0
index|]
operator|.
name|cancel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|thingToCancel
index|[
literal|0
index|]
operator|=
name|serializer
operator|.
name|submit
argument_list|(
name|Callables
operator|.
name|returning
argument_list|(
literal|null
argument_list|)
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
name|results
operator|.
name|add
argument_list|(
name|thingToCancel
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
comment|// Enqueue more than enough tasks to force reentrancy.
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|results
operator|.
name|add
argument_list|(
name|serializer
operator|.
name|submit
argument_list|(
name|Callables
operator|.
name|returning
argument_list|(
literal|null
argument_list|)
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|manualExecutorTask
index|[
literal|0
index|]
operator|.
name|run
argument_list|()
expr_stmt|;
for|for
control|(
name|Future
argument_list|<
name|?
argument_list|>
name|result
range|:
name|results
control|)
block|{
if|if
condition|(
operator|!
name|result
operator|.
name|isCancelled
argument_list|()
condition|)
block|{
name|result
operator|.
name|get
argument_list|(
literal|10
argument_list|,
name|SECONDS
argument_list|)
expr_stmt|;
block|}
comment|// TODO(cpovirk): Verify that the cancelled futures are exactly ones that we expect.
block|}
name|assertThat
argument_list|(
name|logHandler
operator|.
name|getStoredLogRecords
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testAvoidsStackOverflow_manySubmitted ()
specifier|public
name|void
name|testAvoidsStackOverflow_manySubmitted
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|SettableFuture
argument_list|<
name|Void
argument_list|>
name|settableFuture
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
name|ArrayList
argument_list|<
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|50_001
argument_list|)
decl_stmt|;
name|results
operator|.
name|add
argument_list|(
name|serializer
operator|.
name|submitAsync
argument_list|(
operator|new
name|AsyncCallable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
name|call
parameter_list|()
block|{
return|return
name|settableFuture
return|;
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|50_000
condition|;
name|i
operator|++
control|)
block|{
name|results
operator|.
name|add
argument_list|(
name|serializer
operator|.
name|submit
argument_list|(
name|Callables
operator|.
expr|<
name|Void
operator|>
name|returning
argument_list|(
literal|null
argument_list|)
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|settableFuture
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|getDone
argument_list|(
name|allAsList
argument_list|(
name|results
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAvoidsStackOverflow_manyCancelled ()
specifier|public
name|void
name|testAvoidsStackOverflow_manyCancelled
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|SettableFuture
argument_list|<
name|Void
argument_list|>
name|settableFuture
init|=
name|SettableFuture
operator|.
name|create
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
name|submitAsync
argument_list|(
operator|new
name|AsyncCallable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
name|call
parameter_list|()
block|{
return|return
name|settableFuture
return|;
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|50_000
condition|;
name|i
operator|++
control|)
block|{
name|serializer
operator|.
name|submit
argument_list|(
name|Callables
operator|.
expr|<
name|Void
operator|>
name|returning
argument_list|(
literal|null
argument_list|)
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
block|}
name|ListenableFuture
argument_list|<
name|Integer
argument_list|>
name|stackDepthCheck
init|=
name|serializer
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|call
parameter_list|()
block|{
return|return
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getStackTrace
argument_list|()
operator|.
name|length
return|;
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
name|settableFuture
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getDone
argument_list|(
name|stackDepthCheck
argument_list|)
argument_list|)
operator|.
name|isLessThan
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getStackTrace
argument_list|()
operator|.
name|length
operator|+
literal|100
argument_list|)
expr_stmt|;
block|}
DECL|method|testAvoidsStackOverflow_alternatingCancelledAndSubmitted ()
specifier|public
name|void
name|testAvoidsStackOverflow_alternatingCancelledAndSubmitted
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|SettableFuture
argument_list|<
name|Void
argument_list|>
name|settableFuture
init|=
name|SettableFuture
operator|.
name|create
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
name|submitAsync
argument_list|(
operator|new
name|AsyncCallable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
name|call
parameter_list|()
block|{
return|return
name|settableFuture
return|;
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|25_000
condition|;
name|i
operator|++
control|)
block|{
name|serializer
operator|.
name|submit
argument_list|(
name|Callables
operator|.
expr|<
name|Void
operator|>
name|returning
argument_list|(
literal|null
argument_list|)
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
name|unused
operator|=
name|serializer
operator|.
name|submit
argument_list|(
name|Callables
operator|.
expr|<
name|Void
operator|>
name|returning
argument_list|(
literal|null
argument_list|)
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ListenableFuture
argument_list|<
name|Integer
argument_list|>
name|stackDepthCheck
init|=
name|serializer
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|call
parameter_list|()
block|{
return|return
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getStackTrace
argument_list|()
operator|.
name|length
return|;
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
name|settableFuture
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getDone
argument_list|(
name|stackDepthCheck
argument_list|)
argument_list|)
operator|.
name|isLessThan
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getStackTrace
argument_list|()
operator|.
name|length
operator|+
literal|100
argument_list|)
expr_stmt|;
block|}
DECL|method|add (final int delta)
specifier|private
specifier|static
name|Function
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|add
parameter_list|(
specifier|final
name|int
name|delta
parameter_list|)
block|{
return|return
operator|new
name|Function
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|apply
parameter_list|(
name|Integer
name|input
parameter_list|)
block|{
return|return
name|input
operator|+
name|delta
return|;
block|}
block|}
return|;
block|}
DECL|method|asyncAdd ( final ListenableFuture<Integer> future, final int delta, final Executor executor)
specifier|private
specifier|static
name|AsyncCallable
argument_list|<
name|Integer
argument_list|>
name|asyncAdd
parameter_list|(
specifier|final
name|ListenableFuture
argument_list|<
name|Integer
argument_list|>
name|future
parameter_list|,
specifier|final
name|int
name|delta
parameter_list|,
specifier|final
name|Executor
name|executor
parameter_list|)
block|{
return|return
operator|new
name|AsyncCallable
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|Integer
argument_list|>
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|Futures
operator|.
name|transform
argument_list|(
name|future
argument_list|,
name|add
argument_list|(
name|delta
argument_list|)
argument_list|,
name|executor
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|class|LongHolder
specifier|private
specifier|static
specifier|final
class|class
name|LongHolder
block|{
DECL|field|count
name|long
name|count
decl_stmt|;
block|}
DECL|field|ITERATION_COUNT
specifier|private
specifier|static
specifier|final
name|int
name|ITERATION_COUNT
init|=
literal|50_000
decl_stmt|;
DECL|field|DIRECT_EXECUTIONS_PER_THREAD
specifier|private
specifier|static
specifier|final
name|int
name|DIRECT_EXECUTIONS_PER_THREAD
init|=
literal|100
decl_stmt|;
annotation|@
name|GwtIncompatible
comment|// threads
DECL|method|testAvoidsStackOverflow_multipleThreads ()
specifier|public
name|void
name|testAvoidsStackOverflow_multipleThreads
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|LongHolder
name|holder
init|=
operator|new
name|LongHolder
argument_list|()
decl_stmt|;
specifier|final
name|ArrayList
argument_list|<
name|ListenableFuture
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|lengthChecks
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Integer
argument_list|>
name|completeLengthChecks
decl_stmt|;
specifier|final
name|int
name|baseStackDepth
decl_stmt|;
name|ExecutorService
name|service
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|5
argument_list|)
decl_stmt|;
try|try
block|{
comment|// Avoid counting frames from the executor itself, or the ExecutionSequencer
name|baseStackDepth
operator|=
name|serializer
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|call
parameter_list|()
block|{
return|return
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getStackTrace
argument_list|()
operator|.
name|length
return|;
block|}
block|}
argument_list|,
name|service
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
specifier|final
name|SettableFuture
argument_list|<
name|Void
argument_list|>
name|settableFuture
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|unused
init|=
name|serializer
operator|.
name|submitAsync
argument_list|(
operator|new
name|AsyncCallable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
name|call
parameter_list|()
block|{
return|return
name|settableFuture
return|;
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|50_000
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|%
name|DIRECT_EXECUTIONS_PER_THREAD
operator|==
literal|0
condition|)
block|{
comment|// after some number of iterations, switch threads
name|unused
operator|=
name|serializer
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|()
block|{
name|holder
operator|.
name|count
operator|++
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|,
name|service
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|i
operator|%
name|DIRECT_EXECUTIONS_PER_THREAD
operator|==
name|DIRECT_EXECUTIONS_PER_THREAD
operator|-
literal|1
condition|)
block|{
comment|// When at max depth, record stack trace depth
name|lengthChecks
operator|.
name|add
argument_list|(
name|serializer
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|call
parameter_list|()
block|{
name|holder
operator|.
name|count
operator|++
expr_stmt|;
return|return
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getStackTrace
argument_list|()
operator|.
name|length
return|;
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Otherwise, schedule a task on directExecutor
name|unused
operator|=
name|serializer
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|()
block|{
name|holder
operator|.
name|count
operator|++
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|settableFuture
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|completeLengthChecks
operator|=
name|allAsList
argument_list|(
name|lengthChecks
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|service
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|holder
operator|.
name|count
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ITERATION_COUNT
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|length
range|:
name|completeLengthChecks
control|)
block|{
comment|// Verify that at max depth, less than one stack frame per submitted task was consumed
name|assertThat
argument_list|(
name|length
operator|-
name|baseStackDepth
argument_list|)
operator|.
name|isLessThan
argument_list|(
name|DIRECT_EXECUTIONS_PER_THREAD
operator|/
literal|2
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"ObjectToString"
argument_list|)
comment|// Intended behavior
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
name|unused
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

