begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Futures
operator|.
name|transformAsync
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
name|awaitUninterruptibly
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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|UndeclaredThrowableException
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
name|ExecutionException
import|;
end_import

begin_comment
comment|/**  * Unit tests for {@link Futures#transformAsync(ListenableFuture, AsyncFunction, Executor)}.  *  * @author Nishant Thakkar  */
end_comment

begin_class
DECL|class|FuturesTransformAsyncTest
specifier|public
class|class
name|FuturesTransformAsyncTest
extends|extends
name|AbstractChainedListenableFutureTest
argument_list|<
name|String
argument_list|>
block|{
DECL|field|SLOW_OUTPUT_VALID_INPUT_DATA
specifier|protected
specifier|static
specifier|final
name|int
name|SLOW_OUTPUT_VALID_INPUT_DATA
init|=
literal|2
decl_stmt|;
DECL|field|SLOW_FUNC_VALID_INPUT_DATA
specifier|protected
specifier|static
specifier|final
name|int
name|SLOW_FUNC_VALID_INPUT_DATA
init|=
literal|3
decl_stmt|;
DECL|field|RESULT_DATA
specifier|private
specifier|static
specifier|final
name|String
name|RESULT_DATA
init|=
literal|"SUCCESS"
decl_stmt|;
DECL|field|outputFuture
specifier|private
name|SettableFuture
argument_list|<
name|String
argument_list|>
name|outputFuture
decl_stmt|;
comment|// Signals that the function is waiting to complete
DECL|field|funcIsWaitingLatch
specifier|private
name|CountDownLatch
name|funcIsWaitingLatch
decl_stmt|;
comment|// Signals the function so it will complete
DECL|field|funcCompletionLatch
specifier|private
name|CountDownLatch
name|funcCompletionLatch
decl_stmt|;
annotation|@
name|Override
DECL|method|buildChainingFuture (ListenableFuture<Integer> inputFuture)
specifier|protected
name|ListenableFuture
argument_list|<
name|String
argument_list|>
name|buildChainingFuture
parameter_list|(
name|ListenableFuture
argument_list|<
name|Integer
argument_list|>
name|inputFuture
parameter_list|)
block|{
name|outputFuture
operator|=
name|SettableFuture
operator|.
name|create
argument_list|()
expr_stmt|;
name|funcIsWaitingLatch
operator|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|funcCompletionLatch
operator|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
expr_stmt|;
return|return
name|transformAsync
argument_list|(
name|inputFuture
argument_list|,
operator|new
name|ChainingFunction
argument_list|()
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getSuccessfulResult ()
specifier|protected
name|String
name|getSuccessfulResult
parameter_list|()
block|{
return|return
name|RESULT_DATA
return|;
block|}
DECL|class|ChainingFunction
specifier|private
class|class
name|ChainingFunction
implements|implements
name|AsyncFunction
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
block|{
annotation|@
name|Override
DECL|method|apply (Integer input)
specifier|public
name|ListenableFuture
argument_list|<
name|String
argument_list|>
name|apply
parameter_list|(
name|Integer
name|input
parameter_list|)
block|{
switch|switch
condition|(
name|input
condition|)
block|{
case|case
name|VALID_INPUT_DATA
case|:
name|outputFuture
operator|.
name|set
argument_list|(
name|RESULT_DATA
argument_list|)
expr_stmt|;
break|break;
case|case
name|SLOW_OUTPUT_VALID_INPUT_DATA
case|:
break|break;
comment|// do nothing to the result
case|case
name|SLOW_FUNC_VALID_INPUT_DATA
case|:
name|funcIsWaitingLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|awaitUninterruptibly
argument_list|(
name|funcCompletionLatch
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|UndeclaredThrowableException
argument_list|(
name|EXCEPTION
argument_list|)
throw|;
block|}
return|return
name|outputFuture
return|;
block|}
block|}
DECL|method|testFutureGetThrowsFunctionException ()
specifier|public
name|void
name|testFutureGetThrowsFunctionException
parameter_list|()
throws|throws
name|Exception
block|{
name|inputFuture
operator|.
name|set
argument_list|(
name|EXCEPTION_DATA
argument_list|)
expr_stmt|;
name|listener
operator|.
name|assertException
argument_list|(
name|EXCEPTION
argument_list|)
expr_stmt|;
block|}
DECL|method|testFutureGetThrowsCancellationIfInputCancelled ()
specifier|public
name|void
name|testFutureGetThrowsCancellationIfInputCancelled
parameter_list|()
throws|throws
name|Exception
block|{
name|inputFuture
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// argument is ignored
try|try
block|{
name|resultFuture
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Result future must throw CancellationException"
operator|+
literal|" if input future is cancelled."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CancellationException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testFutureGetThrowsCancellationIfOutputCancelled ()
specifier|public
name|void
name|testFutureGetThrowsCancellationIfOutputCancelled
parameter_list|()
throws|throws
name|Exception
block|{
name|inputFuture
operator|.
name|set
argument_list|(
name|SLOW_OUTPUT_VALID_INPUT_DATA
argument_list|)
expr_stmt|;
name|outputFuture
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// argument is ignored
try|try
block|{
name|resultFuture
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Result future must throw CancellationException"
operator|+
literal|" if function output future is cancelled."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CancellationException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testFutureCancelBeforeInputCompletion ()
specifier|public
name|void
name|testFutureCancelBeforeInputCompletion
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|resultFuture
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultFuture
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|inputFuture
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|outputFuture
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|resultFuture
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Result future is cancelled and should have thrown a"
operator|+
literal|" CancellationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CancellationException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testFutureCancellableBeforeOutputCompletion ()
specifier|public
name|void
name|testFutureCancellableBeforeOutputCompletion
parameter_list|()
throws|throws
name|Exception
block|{
name|inputFuture
operator|.
name|set
argument_list|(
name|SLOW_OUTPUT_VALID_INPUT_DATA
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultFuture
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultFuture
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|inputFuture
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|outputFuture
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|resultFuture
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Result future is cancelled and should have thrown a"
operator|+
literal|" CancellationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CancellationException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testFutureCancellableBeforeFunctionCompletion ()
specifier|public
name|void
name|testFutureCancellableBeforeFunctionCompletion
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Set the result in a separate thread since this test runs the function
comment|// (which will block) in the same thread.
operator|new
name|Thread
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|inputFuture
operator|.
name|set
argument_list|(
name|SLOW_FUNC_VALID_INPUT_DATA
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|start
argument_list|()
expr_stmt|;
name|funcIsWaitingLatch
operator|.
name|await
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|resultFuture
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultFuture
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|inputFuture
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|outputFuture
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|resultFuture
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Result future is cancelled and should have thrown a"
operator|+
literal|" CancellationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CancellationException
name|expected
parameter_list|)
block|{     }
name|funcCompletionLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
comment|// allow the function to complete
try|try
block|{
name|outputFuture
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"The function output future is cancelled and should have thrown a"
operator|+
literal|" CancellationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CancellationException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testFutureCancelAfterCompletion ()
specifier|public
name|void
name|testFutureCancelAfterCompletion
parameter_list|()
throws|throws
name|Exception
block|{
name|inputFuture
operator|.
name|set
argument_list|(
name|VALID_INPUT_DATA
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|resultFuture
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|resultFuture
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|inputFuture
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|outputFuture
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RESULT_DATA
argument_list|,
name|resultFuture
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testFutureGetThrowsRuntimeException ()
specifier|public
name|void
name|testFutureGetThrowsRuntimeException
parameter_list|()
throws|throws
name|Exception
block|{
name|BadFuture
name|badInput
init|=
operator|new
name|BadFuture
argument_list|(
name|Futures
operator|.
name|immediateFuture
argument_list|(
literal|20
argument_list|)
argument_list|)
decl_stmt|;
name|ListenableFuture
argument_list|<
name|String
argument_list|>
name|chain
init|=
name|buildChainingFuture
argument_list|(
name|badInput
argument_list|)
decl_stmt|;
try|try
block|{
name|chain
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Future.get must throw an exception when the input future fails."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|assertSame
argument_list|(
name|RuntimeException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Proxy to throw a {@link RuntimeException} out of the {@link #get()} method. */
DECL|class|BadFuture
specifier|public
specifier|static
class|class
name|BadFuture
extends|extends
name|SimpleForwardingListenableFuture
argument_list|<
name|Integer
argument_list|>
block|{
DECL|method|BadFuture (ListenableFuture<Integer> delegate)
specifier|protected
name|BadFuture
parameter_list|(
name|ListenableFuture
argument_list|<
name|Integer
argument_list|>
name|delegate
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|Integer
name|get
parameter_list|()
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Oops"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

