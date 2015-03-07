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
comment|/**  * Emulation for InterruptibleTask in GWT.  */
end_comment

begin_class
DECL|class|InterruptibleTask
specifier|abstract
class|class
name|InterruptibleTask
implements|implements
name|Runnable
block|{
DECL|method|run ()
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|runInterruptibly
argument_list|()
expr_stmt|;
block|}
DECL|method|runInterruptibly ()
specifier|abstract
name|void
name|runInterruptibly
parameter_list|()
function_decl|;
DECL|method|wasInterrupted ()
specifier|abstract
name|boolean
name|wasInterrupted
parameter_list|()
function_decl|;
DECL|method|interruptTask ()
specifier|final
name|void
name|interruptTask
parameter_list|()
block|{}
block|}
end_class

end_unit

