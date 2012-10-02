begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.io
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
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
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilterInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
comment|/** Returns a random portion of the requested bytes on each call. */
end_comment

begin_class
DECL|class|RandomAmountInputStream
class|class
name|RandomAmountInputStream
extends|extends
name|FilterInputStream
block|{
DECL|field|random
specifier|private
specifier|final
name|Random
name|random
decl_stmt|;
DECL|method|RandomAmountInputStream (@ullable InputStream in, Random random)
specifier|public
name|RandomAmountInputStream
parameter_list|(
annotation|@
name|Nullable
name|InputStream
name|in
parameter_list|,
name|Random
name|random
parameter_list|)
block|{
name|super
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|this
operator|.
name|random
operator|=
name|checkNotNull
argument_list|(
name|random
argument_list|)
expr_stmt|;
block|}
DECL|method|read (byte[] b, int off, int len)
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|super
operator|.
name|read
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|random
operator|.
name|nextInt
argument_list|(
name|len
argument_list|)
operator|+
literal|1
argument_list|)
return|;
block|}
block|}
end_class

end_unit

