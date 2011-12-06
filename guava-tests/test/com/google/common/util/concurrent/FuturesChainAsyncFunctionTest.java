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

begin_comment
comment|/**  * Unit tests for {@link Futures#transform(ListenableFuture, AsyncFunction)}.  *  * @author Nishant Thakkar  */
end_comment

begin_class
DECL|class|FuturesChainAsyncFunctionTest
specifier|public
class|class
name|FuturesChainAsyncFunctionTest
extends|extends
name|AbstractFuturesChainTest
block|{
DECL|method|chain ( ListenableFuture<Integer> inputFuture)
annotation|@
name|Override
name|ListenableFuture
argument_list|<
name|String
argument_list|>
name|chain
parameter_list|(
name|ListenableFuture
argument_list|<
name|Integer
argument_list|>
name|inputFuture
parameter_list|)
block|{
return|return
name|Futures
operator|.
name|transform
argument_list|(
name|inputFuture
argument_list|,
operator|new
name|AsyncFunctionFunction
argument_list|()
argument_list|)
return|;
block|}
DECL|class|AsyncFunctionFunction
specifier|private
class|class
name|AsyncFunctionFunction
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
return|return
name|FuturesChainAsyncFunctionTest
operator|.
name|this
operator|.
name|apply
argument_list|(
name|input
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

