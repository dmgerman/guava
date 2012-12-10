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
name|GwtCompatible
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Unchecked variant of {@link java.util.concurrent.ExecutionException}. As with  * {@code ExecutionException}, the exception's {@linkplain #getCause() cause}  * comes from a failed task, possibly run in another thread.  *  *<p>{@code UncheckedExecutionException} is intended as an alternative to  * {@code ExecutionException} when the exception thrown by a task is an  * unchecked exception. However, it may also wrap a checked exception in some  * cases.  *  *<p>When wrapping an {@code Error} from another thread, prefer {@link  * ExecutionError}. When wrapping a checked exception, prefer {@code  * ExecutionException}.  *  * @author Charles Fry  * @since 10.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|UncheckedExecutionException
specifier|public
class|class
name|UncheckedExecutionException
extends|extends
name|RuntimeException
block|{
comment|/**    * Creates a new instance with {@code null} as its detail message.    */
DECL|method|UncheckedExecutionException ()
specifier|protected
name|UncheckedExecutionException
parameter_list|()
block|{}
comment|/**    * Creates a new instance with the given detail message.    */
DECL|method|UncheckedExecutionException (@ullable String message)
specifier|protected
name|UncheckedExecutionException
parameter_list|(
annotation|@
name|Nullable
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
DECL|method|UncheckedExecutionException (@ullable String message, @Nullable Throwable cause)
specifier|public
name|UncheckedExecutionException
parameter_list|(
annotation|@
name|Nullable
name|String
name|message
parameter_list|,
annotation|@
name|Nullable
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
comment|/**    * Creates a new instance with the given cause.    */
DECL|method|UncheckedExecutionException (@ullable Throwable cause)
specifier|public
name|UncheckedExecutionException
parameter_list|(
annotation|@
name|Nullable
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

