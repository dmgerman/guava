begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|annotations
operator|.
name|Beta
import|;
end_import

begin_comment
comment|/**  * {@link Error} variant of {@link java.util.concurrent.ExecutionException}. As  * with {@code ExecutionException}, the error's {@linkplain #getCause() cause}  * comes from a failed task, possibly run in another thread. That cause should  * itself be an {@code Error}; if not, use {@code ExecutionException} or {@link  * UncheckedExecutionException}. This allows the client code to continue to  * distinguish between exceptions and errors, even when they come from other  * threads.  *  * @author cpovirk@google.com (Chris Povirk)  * @since Guava release 10  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|ExecutionError
specifier|public
class|class
name|ExecutionError
extends|extends
name|Error
block|{
comment|/**    * Creates a new instance with {@code null} as its detail message.    */
DECL|method|ExecutionError ()
specifier|protected
name|ExecutionError
parameter_list|()
block|{}
comment|/**    * Creates a new instance with the given detail message.    */
DECL|method|ExecutionError (String message)
specifier|protected
name|ExecutionError
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates a new instance with the given detail message and cause.    */
DECL|method|ExecutionError (String message, Error cause)
specifier|public
name|ExecutionError
parameter_list|(
name|String
name|message
parameter_list|,
name|Error
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
comment|/**    * Creates a new instance with the given cause.    */
DECL|method|ExecutionError (Error cause)
specifier|public
name|ExecutionError
parameter_list|(
name|Error
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
end_class

end_unit

