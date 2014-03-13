begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
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
comment|/**  * Exception thrown upon the failure of a  *<a href="http://code.google.com/p/guava-libraries/wiki/ConditionalFailuresExplained">verification  * check</a>, including those performed by the convenience methods of the {@link Verify} class.  *  * @since 17.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|VerifyException
specifier|public
class|class
name|VerifyException
extends|extends
name|RuntimeException
block|{
comment|/** Constructs a {@code VerifyException} with no message. */
DECL|method|VerifyException ()
specifier|public
name|VerifyException
parameter_list|()
block|{}
comment|/** Constructs a {@code VerifyException} with the message {@code message}. */
DECL|method|VerifyException (@ullable String message)
specifier|public
name|VerifyException
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
block|}
end_class

end_unit

