begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|GwtIncompatible
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|DataOutput
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

begin_comment
comment|/**  * An extension of {@code DataOutput} for writing to in-memory byte arrays; its methods offer  * identical functionality but do not throw {@link IOException}.  *  * @author Jayaprabhakar Kadarkarai  * @since 1.0  */
end_comment

begin_interface
annotation|@
name|GwtIncompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|interface|ByteArrayDataOutput
specifier|public
interface|interface
name|ByteArrayDataOutput
extends|extends
name|DataOutput
block|{
annotation|@
name|Override
DECL|method|write (int b)
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|write (byte b[])
name|void
name|write
parameter_list|(
name|byte
name|b
index|[]
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|write (byte b[], int off, int len)
name|void
name|write
parameter_list|(
name|byte
name|b
index|[]
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|writeBoolean (boolean v)
name|void
name|writeBoolean
parameter_list|(
name|boolean
name|v
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|writeByte (int v)
name|void
name|writeByte
parameter_list|(
name|int
name|v
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|writeShort (int v)
name|void
name|writeShort
parameter_list|(
name|int
name|v
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|writeChar (int v)
name|void
name|writeChar
parameter_list|(
name|int
name|v
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|writeInt (int v)
name|void
name|writeInt
parameter_list|(
name|int
name|v
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|writeLong (long v)
name|void
name|writeLong
parameter_list|(
name|long
name|v
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|writeFloat (float v)
name|void
name|writeFloat
parameter_list|(
name|float
name|v
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|writeDouble (double v)
name|void
name|writeDouble
parameter_list|(
name|double
name|v
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|writeChars (String s)
name|void
name|writeChars
parameter_list|(
name|String
name|s
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|writeUTF (String s)
name|void
name|writeUTF
parameter_list|(
name|String
name|s
parameter_list|)
function_decl|;
comment|/**    * @deprecated This method is dangerous as it discards the high byte of every character. For    *     UTF-8, use {@code write(s.getBytes(StandardCharsets.UTF_8))}.    */
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|writeBytes (String s)
name|void
name|writeBytes
parameter_list|(
name|String
name|s
parameter_list|)
function_decl|;
comment|/** Returns the contents that have been written to this instance, as a byte array. */
DECL|method|toByteArray ()
name|byte
index|[]
name|toByteArray
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

