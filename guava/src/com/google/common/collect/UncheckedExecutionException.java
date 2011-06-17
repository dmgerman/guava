begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
package|;
end_package

begin_comment
comment|/**  * Unchecked version of {@link java.util.concurrent.ExecutionException}.  *  * @author fry@google.com (Charles Fry)  */
end_comment

begin_comment
comment|// TODO(user): move to common.util.concurrent
end_comment

begin_class
DECL|class|UncheckedExecutionException
specifier|public
class|class
name|UncheckedExecutionException
extends|extends
name|RuntimeException
block|{
comment|/**    * Creates a new instance with the given cause.    */
DECL|method|UncheckedExecutionException (Throwable cause)
specifier|public
name|UncheckedExecutionException
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates a new instance with the given cause.    */
DECL|method|UncheckedExecutionException (String message, Throwable cause)
specifier|public
name|UncheckedExecutionException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

