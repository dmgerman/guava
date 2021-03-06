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
name|transform
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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|UndeclaredThrowableException
import|;
end_import

begin_comment
comment|/**  * Unit tests for {@link Futures#transform(ListenableFuture, Function, Executor)}.  *  * @author Nishant Thakkar  */
end_comment

begin_class
DECL|class|FuturesTransformTest
specifier|public
class|class
name|FuturesTransformTest
extends|extends
name|AbstractChainedListenableFutureTest
argument_list|<
name|String
argument_list|>
block|{
DECL|field|RESULT_DATA
specifier|private
specifier|static
specifier|final
name|String
name|RESULT_DATA
init|=
literal|"SUCCESS"
decl_stmt|;
DECL|field|WRAPPED_EXCEPTION
specifier|private
specifier|static
specifier|final
name|UndeclaredThrowableException
name|WRAPPED_EXCEPTION
init|=
operator|new
name|UndeclaredThrowableException
argument_list|(
name|EXCEPTION
argument_list|)
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
return|return
name|transform
argument_list|(
name|inputFuture
argument_list|,
operator|new
name|ComposeFunction
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
DECL|class|ComposeFunction
specifier|private
class|class
name|ComposeFunction
implements|implements
name|Function
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
name|String
name|apply
parameter_list|(
name|Integer
name|input
parameter_list|)
block|{
if|if
condition|(
name|input
operator|.
name|intValue
argument_list|()
operator|==
name|VALID_INPUT_DATA
condition|)
block|{
return|return
name|RESULT_DATA
return|;
block|}
else|else
block|{
throw|throw
name|WRAPPED_EXCEPTION
throw|;
block|}
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
name|WRAPPED_EXCEPTION
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

