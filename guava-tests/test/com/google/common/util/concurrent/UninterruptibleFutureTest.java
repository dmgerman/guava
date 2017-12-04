begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|concurrent
operator|.
name|InterruptionUtil
operator|.
name|repeatedlyInterruptTestThread
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
name|Uninterruptibles
operator|.
name|getUninterruptibly
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
name|MINUTES
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
name|testing
operator|.
name|TearDown
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
name|TearDownStack
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|// TODO(azana/cpovirk): Should this be merged into UninterruptiblesTest?
end_comment

begin_comment
comment|/**  * Unit test for {@link Uninterruptibles#getUninterruptibly}  *  * @author Kevin Bourrillion  * @author Chris Povirk  */
end_comment

begin_class
DECL|class|UninterruptibleFutureTest
specifier|public
class|class
name|UninterruptibleFutureTest
extends|extends
name|TestCase
block|{
DECL|field|sleeper
specifier|private
name|SleepingRunnable
name|sleeper
decl_stmt|;
DECL|field|delayedFuture
specifier|private
name|Future
argument_list|<
name|Boolean
argument_list|>
name|delayedFuture
decl_stmt|;
DECL|field|tearDownStack
specifier|private
specifier|final
name|TearDownStack
name|tearDownStack
init|=
operator|new
name|TearDownStack
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
block|{
specifier|final
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
decl_stmt|;
name|tearDownStack
operator|.
name|addTearDown
argument_list|(
operator|new
name|TearDown
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|sleeper
operator|=
operator|new
name|SleepingRunnable
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|delayedFuture
operator|=
name|executor
operator|.
name|submit
argument_list|(
name|sleeper
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|tearDownStack
operator|.
name|addTearDown
argument_list|(
operator|new
name|TearDown
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|Thread
operator|.
name|interrupted
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
block|{
name|tearDownStack
operator|.
name|runTearDown
argument_list|()
expr_stmt|;
block|}
comment|/**    * This first test doesn't test anything in Uninterruptibles, just demonstrates some normal    * behavior of futures so that you can contrast the next test with it.    */
DECL|method|testRegularFutureInterrupted ()
specifier|public
name|void
name|testRegularFutureInterrupted
parameter_list|()
throws|throws
name|ExecutionException
block|{
comment|/*      * Here's the order of events that we want.      *      * 1. The client thread begins to block on a get() call to a future.      * 2. The client thread is interrupted sometime before the result would be      *   available.      * 3. We expect the client's get() to throw an InterruptedException.      * 4. We expect the client thread's interrupt state to be false.      * 5. The client thread again makes a blocking call to get().      * 6. Now the result becomes available.      * 7. We expect get() to return this result.      * 8. We expect the test thread's interrupt state to be false.      */
name|InterruptionUtil
operator|.
name|requestInterruptIn
argument_list|(
literal|200
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Thread
operator|.
name|interrupted
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|delayedFuture
operator|.
name|get
argument_list|(
literal|10000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected to be interrupted"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|expected
parameter_list|)
block|{     }
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// we were interrupted, but it's been cleared now
name|assertFalse
argument_list|(
name|Thread
operator|.
name|interrupted
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sleeper
operator|.
name|completed
argument_list|)
expr_stmt|;
try|try
block|{
name|assertTrue
argument_list|(
name|delayedFuture
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|assertTrue
argument_list|(
name|sleeper
operator|.
name|completed
argument_list|)
expr_stmt|;
block|}
DECL|method|testMakeUninterruptible_timeoutPreservedThroughInterruption ()
specifier|public
name|void
name|testMakeUninterruptible_timeoutPreservedThroughInterruption
parameter_list|()
throws|throws
name|ExecutionException
block|{
name|repeatedlyInterruptTestThread
argument_list|(
literal|100
argument_list|,
name|tearDownStack
argument_list|)
expr_stmt|;
try|try
block|{
name|getUninterruptibly
argument_list|(
name|delayedFuture
argument_list|,
literal|500
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected to time out"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|expected
parameter_list|)
block|{     }
name|assertTrue
argument_list|(
name|Thread
operator|.
name|interrupted
argument_list|()
argument_list|)
expr_stmt|;
comment|// clears the interrupt state, too
name|assertFalse
argument_list|(
name|sleeper
operator|.
name|completed
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getUninterruptibly
argument_list|(
name|delayedFuture
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Thread
operator|.
name|interrupted
argument_list|()
argument_list|)
expr_stmt|;
comment|// clears the interrupt state, too
name|assertTrue
argument_list|(
name|sleeper
operator|.
name|completed
argument_list|)
expr_stmt|;
block|}
DECL|class|SleepingRunnable
specifier|private
specifier|static
class|class
name|SleepingRunnable
implements|implements
name|Runnable
block|{
DECL|field|millis
specifier|final
name|int
name|millis
decl_stmt|;
DECL|field|completed
specifier|volatile
name|boolean
name|completed
decl_stmt|;
DECL|method|SleepingRunnable (int millis)
specifier|public
name|SleepingRunnable
parameter_list|(
name|int
name|millis
parameter_list|)
block|{
name|this
operator|.
name|millis
operator|=
name|millis
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
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|millis
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|wontHappen
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
name|completed
operator|=
literal|true
expr_stmt|;
block|}
block|}
DECL|method|testMakeUninterruptible_untimed_uninterrupted ()
specifier|public
name|void
name|testMakeUninterruptible_untimed_uninterrupted
parameter_list|()
throws|throws
name|Exception
block|{
name|runUntimedInterruptsTest
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|testMakeUninterruptible_untimed_interrupted ()
specifier|public
name|void
name|testMakeUninterruptible_untimed_interrupted
parameter_list|()
throws|throws
name|Exception
block|{
name|runUntimedInterruptsTest
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|testMakeUninterruptible_untimed_multiplyInterrupted ()
specifier|public
name|void
name|testMakeUninterruptible_untimed_multiplyInterrupted
parameter_list|()
throws|throws
name|Exception
block|{
name|runUntimedInterruptsTest
argument_list|(
literal|38
argument_list|)
expr_stmt|;
block|}
DECL|method|testMakeUninterruptible_timed_uninterrupted ()
specifier|public
name|void
name|testMakeUninterruptible_timed_uninterrupted
parameter_list|()
throws|throws
name|Exception
block|{
name|runTimedInterruptsTest
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|testMakeUninterruptible_timed_interrupted ()
specifier|public
name|void
name|testMakeUninterruptible_timed_interrupted
parameter_list|()
throws|throws
name|Exception
block|{
name|runTimedInterruptsTest
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|testMakeUninterruptible_timed_multiplyInterrupted ()
specifier|public
name|void
name|testMakeUninterruptible_timed_multiplyInterrupted
parameter_list|()
throws|throws
name|Exception
block|{
name|runTimedInterruptsTest
argument_list|(
literal|38
argument_list|)
expr_stmt|;
block|}
DECL|method|runUntimedInterruptsTest (int times)
specifier|private
specifier|static
name|void
name|runUntimedInterruptsTest
parameter_list|(
name|int
name|times
parameter_list|)
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
throws|,
name|TimeoutException
block|{
name|SettableFuture
argument_list|<
name|String
argument_list|>
name|future
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
name|FutureTask
argument_list|<
name|Boolean
argument_list|>
name|interruptReporter
init|=
name|untimedInterruptReporter
argument_list|(
name|future
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|runNInterruptsTest
argument_list|(
name|times
argument_list|,
name|future
argument_list|,
name|interruptReporter
argument_list|)
expr_stmt|;
block|}
DECL|method|runTimedInterruptsTest (int times)
specifier|private
specifier|static
name|void
name|runTimedInterruptsTest
parameter_list|(
name|int
name|times
parameter_list|)
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
throws|,
name|TimeoutException
block|{
name|SettableFuture
argument_list|<
name|String
argument_list|>
name|future
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
name|FutureTask
argument_list|<
name|Boolean
argument_list|>
name|interruptReporter
init|=
name|timedInterruptReporter
argument_list|(
name|future
argument_list|)
decl_stmt|;
name|runNInterruptsTest
argument_list|(
name|times
argument_list|,
name|future
argument_list|,
name|interruptReporter
argument_list|)
expr_stmt|;
block|}
DECL|method|runNInterruptsTest ( int times, SettableFuture<String> future, FutureTask<Boolean> interruptReporter)
specifier|private
specifier|static
name|void
name|runNInterruptsTest
parameter_list|(
name|int
name|times
parameter_list|,
name|SettableFuture
argument_list|<
name|String
argument_list|>
name|future
parameter_list|,
name|FutureTask
argument_list|<
name|Boolean
argument_list|>
name|interruptReporter
parameter_list|)
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
throws|,
name|TimeoutException
block|{
name|Thread
name|waitingThread
init|=
operator|new
name|Thread
argument_list|(
name|interruptReporter
argument_list|)
decl_stmt|;
name|waitingThread
operator|.
name|start
argument_list|()
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
name|times
condition|;
name|i
operator|++
control|)
block|{
name|waitingThread
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
name|future
operator|.
name|set
argument_list|(
name|RESULT
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|times
operator|>
literal|0
argument_list|,
operator|(
name|boolean
operator|)
name|interruptReporter
operator|.
name|get
argument_list|(
literal|20
argument_list|,
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Confirms that the test code triggers {@link InterruptedException} in a standard {@link Future}.    */
DECL|method|testMakeUninterruptible_plainFutureSanityCheck ()
specifier|public
name|void
name|testMakeUninterruptible_plainFutureSanityCheck
parameter_list|()
throws|throws
name|Exception
block|{
name|SettableFuture
argument_list|<
name|String
argument_list|>
name|future
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
name|FutureTask
argument_list|<
name|Boolean
argument_list|>
name|wasInterrupted
init|=
name|untimedInterruptReporter
argument_list|(
name|future
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Thread
name|waitingThread
init|=
operator|new
name|Thread
argument_list|(
name|wasInterrupted
argument_list|)
decl_stmt|;
name|waitingThread
operator|.
name|start
argument_list|()
expr_stmt|;
name|waitingThread
operator|.
name|interrupt
argument_list|()
expr_stmt|;
try|try
block|{
name|wasInterrupted
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|expected
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|expected
operator|.
name|getCause
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|expected
operator|.
name|getCause
argument_list|()
operator|instanceof
name|InterruptedException
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testMakeUninterruptible_timedGetZeroTimeoutAttempted ()
specifier|public
name|void
name|testMakeUninterruptible_timedGetZeroTimeoutAttempted
parameter_list|()
throws|throws
name|TimeoutException
throws|,
name|ExecutionException
block|{
name|SettableFuture
argument_list|<
name|String
argument_list|>
name|future
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
name|future
operator|.
name|set
argument_list|(
name|RESULT
argument_list|)
expr_stmt|;
comment|/*      * getUninterruptibly should call the timed get method once with a      * wait of 0 seconds (and it should succeed, since the result is already      * available).      */
name|assertEquals
argument_list|(
name|RESULT
argument_list|,
name|getUninterruptibly
argument_list|(
name|future
argument_list|,
literal|0
argument_list|,
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testMakeUninterruptible_timedGetNegativeTimeoutAttempted ()
specifier|public
name|void
name|testMakeUninterruptible_timedGetNegativeTimeoutAttempted
parameter_list|()
throws|throws
name|TimeoutException
throws|,
name|ExecutionException
block|{
name|SettableFuture
argument_list|<
name|String
argument_list|>
name|future
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
name|future
operator|.
name|set
argument_list|(
name|RESULT
argument_list|)
expr_stmt|;
comment|/*      * The getUninterruptibly should call the timed get method once with a      * wait of -1 seconds (and it should succeed, since the result is already      * available).      */
name|assertEquals
argument_list|(
name|RESULT
argument_list|,
name|getUninterruptibly
argument_list|(
name|future
argument_list|,
operator|-
literal|1
argument_list|,
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|untimedInterruptReporter ( final Future<?> future, final boolean allowInterruption)
specifier|private
specifier|static
name|FutureTask
argument_list|<
name|Boolean
argument_list|>
name|untimedInterruptReporter
parameter_list|(
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|future
parameter_list|,
specifier|final
name|boolean
name|allowInterruption
parameter_list|)
block|{
return|return
operator|new
name|FutureTask
argument_list|<>
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
throws|throws
name|Exception
block|{
name|Object
name|actual
decl_stmt|;
if|if
condition|(
name|allowInterruption
condition|)
block|{
name|actual
operator|=
name|future
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|actual
operator|=
name|getUninterruptibly
argument_list|(
name|future
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|RESULT
argument_list|,
name|actual
argument_list|)
expr_stmt|;
return|return
name|Thread
operator|.
name|interrupted
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|timedInterruptReporter (final Future<?> future)
specifier|private
specifier|static
name|FutureTask
argument_list|<
name|Boolean
argument_list|>
name|timedInterruptReporter
parameter_list|(
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|future
parameter_list|)
block|{
return|return
operator|new
name|FutureTask
argument_list|<>
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
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|RESULT
argument_list|,
name|getUninterruptibly
argument_list|(
name|future
argument_list|,
literal|10
argument_list|,
name|MINUTES
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Thread
operator|.
name|interrupted
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|field|RESULT
specifier|private
specifier|static
specifier|final
name|String
name|RESULT
init|=
literal|"result"
decl_stmt|;
block|}
end_class

end_unit

