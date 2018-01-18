begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/** Emulation for InterruptibleTask in GWT. */
end_comment

begin_class
DECL|class|InterruptibleTask
specifier|abstract
class|class
name|InterruptibleTask
parameter_list|<
name|V
parameter_list|>
implements|implements
name|Runnable
block|{
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|V
name|result
init|=
literal|null
decl_stmt|;
name|Throwable
name|error
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isDone
argument_list|()
condition|)
block|{
return|return;
block|}
try|try
block|{
name|result
operator|=
name|runInterruptibly
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|error
operator|=
name|t
expr_stmt|;
block|}
name|afterRanInterruptibly
argument_list|(
name|result
argument_list|,
name|error
argument_list|)
expr_stmt|;
block|}
DECL|method|isDone ()
specifier|abstract
name|boolean
name|isDone
parameter_list|()
function_decl|;
DECL|method|runInterruptibly ()
specifier|abstract
name|V
name|runInterruptibly
parameter_list|()
throws|throws
name|Exception
function_decl|;
DECL|method|afterRanInterruptibly (V result, Throwable error)
specifier|abstract
name|void
name|afterRanInterruptibly
parameter_list|(
name|V
name|result
parameter_list|,
name|Throwable
name|error
parameter_list|)
function_decl|;
DECL|method|interruptTask ()
specifier|final
name|void
name|interruptTask
parameter_list|()
block|{}
DECL|method|toPendingString ()
specifier|abstract
name|String
name|toPendingString
parameter_list|()
function_decl|;
block|}
end_class

end_unit

