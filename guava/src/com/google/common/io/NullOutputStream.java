begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2004 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_comment
comment|/**  * Implementation of {@link OutputStream} that simply discards written bytes.  *  * @author Spencer Kimball  * @since 1.0  * @deprecated Use {@link ByteStreams#nullOutputStream} instead. This class is  *     scheduled to be removed in Guava release 15.0.  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|Deprecated
DECL|class|NullOutputStream
specifier|public
specifier|final
class|class
name|NullOutputStream
extends|extends
name|OutputStream
block|{
comment|/** Discards the specified byte. */
DECL|method|write (int b)
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
block|{   }
comment|/** Discards the specified byte array. */
DECL|method|write (byte[] b, int off, int len)
annotation|@
name|Override
specifier|public
name|void
name|write
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
block|{   }
block|}
end_class

end_unit

