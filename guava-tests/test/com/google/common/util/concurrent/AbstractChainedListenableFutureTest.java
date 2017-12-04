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
name|testing
operator|.
name|MockFutureListener
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
comment|/**  * Unit tests for any listenable future that chains other listenable futures. Unit tests need only  * override buildChainingFuture and getSuccessfulResult, but they can add custom tests as needed.  *  * @author Nishant Thakkar  */
end_comment

begin_class
DECL|class|AbstractChainedListenableFutureTest
specifier|public
specifier|abstract
class|class
name|AbstractChainedListenableFutureTest
parameter_list|<
name|T
parameter_list|>
extends|extends
name|TestCase
block|{
DECL|field|EXCEPTION_DATA
specifier|protected
specifier|static
specifier|final
name|int
name|EXCEPTION_DATA
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|VALID_INPUT_DATA
specifier|protected
specifier|static
specifier|final
name|int
name|VALID_INPUT_DATA
init|=
literal|1
decl_stmt|;
DECL|field|EXCEPTION
specifier|protected
specifier|static
specifier|final
name|Exception
name|EXCEPTION
init|=
operator|new
name|Exception
argument_list|(
literal|"Test exception"
argument_list|)
decl_stmt|;
DECL|field|inputFuture
specifier|protected
name|SettableFuture
argument_list|<
name|Integer
argument_list|>
name|inputFuture
decl_stmt|;
DECL|field|resultFuture
specifier|protected
name|ListenableFuture
argument_list|<
name|T
argument_list|>
name|resultFuture
decl_stmt|;
DECL|field|listener
specifier|protected
name|MockFutureListener
name|listener
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|inputFuture
operator|=
name|SettableFuture
operator|.
name|create
argument_list|()
expr_stmt|;
name|resultFuture
operator|=
name|buildChainingFuture
argument_list|(
name|inputFuture
argument_list|)
expr_stmt|;
name|listener
operator|=
operator|new
name|MockFutureListener
argument_list|(
name|resultFuture
argument_list|)
expr_stmt|;
block|}
DECL|method|testFutureGetBeforeCallback ()
specifier|public
name|void
name|testFutureGetBeforeCallback
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Verify that get throws a timeout exception before the callback is called.
try|try
block|{
name|resultFuture
operator|.
name|get
argument_list|(
literal|1L
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"The data is not yet ready, so a TimeoutException is expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testFutureGetThrowsWrappedException ()
specifier|public
name|void
name|testFutureGetThrowsWrappedException
parameter_list|()
throws|throws
name|Exception
block|{
name|inputFuture
operator|.
name|setException
argument_list|(
name|EXCEPTION
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
DECL|method|testFutureGetThrowsWrappedError ()
specifier|public
name|void
name|testFutureGetThrowsWrappedError
parameter_list|()
throws|throws
name|Exception
block|{
name|Error
name|error
init|=
operator|new
name|Error
argument_list|()
decl_stmt|;
name|inputFuture
operator|.
name|setException
argument_list|(
name|error
argument_list|)
expr_stmt|;
comment|// Verify that get throws an ExecutionException, caused by an Error, when
comment|// the callback is called.
name|listener
operator|.
name|assertException
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
DECL|method|testAddListenerAfterCallback ()
specifier|public
name|void
name|testAddListenerAfterCallback
parameter_list|()
throws|throws
name|Throwable
block|{
name|inputFuture
operator|.
name|set
argument_list|(
name|VALID_INPUT_DATA
argument_list|)
expr_stmt|;
name|listener
operator|.
name|assertSuccess
argument_list|(
name|getSuccessfulResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testFutureBeforeCallback ()
specifier|public
name|void
name|testFutureBeforeCallback
parameter_list|()
throws|throws
name|Throwable
block|{
name|inputFuture
operator|.
name|set
argument_list|(
name|VALID_INPUT_DATA
argument_list|)
expr_stmt|;
name|listener
operator|.
name|assertSuccess
argument_list|(
name|getSuccessfulResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Override to return a chaining listenableFuture that returns the result of getSuccessfulResult()    * when inputFuture returns VALID_INPUT_DATA, and sets the exception to EXCEPTION in all other    * cases.    */
DECL|method|buildChainingFuture (ListenableFuture<Integer> inputFuture)
specifier|protected
specifier|abstract
name|ListenableFuture
argument_list|<
name|T
argument_list|>
name|buildChainingFuture
parameter_list|(
name|ListenableFuture
argument_list|<
name|Integer
argument_list|>
name|inputFuture
parameter_list|)
function_decl|;
comment|/**    * Override to return the result when VALID_INPUT_DATA is passed in to the chaining    * listenableFuture    */
DECL|method|getSuccessfulResult ()
specifier|protected
specifier|abstract
name|T
name|getSuccessfulResult
parameter_list|()
function_decl|;
block|}
end_class

end_unit

