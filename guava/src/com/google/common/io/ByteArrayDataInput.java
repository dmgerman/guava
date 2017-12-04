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
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|DataInput
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
comment|/**  * An extension of {@code DataInput} for reading from in-memory byte arrays; its methods offer  * identical functionality but do not throw {@link IOException}.  *  *<p><b>Warning:</b> The caller is responsible for not attempting to read past the end of the  * array. If any method encounters the end of the array prematurely, it throws {@link  * IllegalStateException} to signify<i>programmer error</i>. This behavior is a technical violation  * of the supertype's contract, which specifies a checked exception.  *  * @author Kevin Bourrillion  * @since 1.0  */
end_comment

begin_interface
annotation|@
name|GwtIncompatible
DECL|interface|ByteArrayDataInput
specifier|public
interface|interface
name|ByteArrayDataInput
extends|extends
name|DataInput
block|{
annotation|@
name|Override
DECL|method|readFully (byte b[])
name|void
name|readFully
parameter_list|(
name|byte
name|b
index|[]
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|readFully (byte b[], int off, int len)
name|void
name|readFully
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
comment|// not guaranteed to skip n bytes so result should NOT be ignored
comment|// use ByteStreams.skipFully or one of the read methods instead
annotation|@
name|Override
DECL|method|skipBytes (int n)
name|int
name|skipBytes
parameter_list|(
name|int
name|n
parameter_list|)
function_decl|;
annotation|@
name|CanIgnoreReturnValue
comment|// to skip a byte
annotation|@
name|Override
DECL|method|readBoolean ()
name|boolean
name|readBoolean
parameter_list|()
function_decl|;
annotation|@
name|CanIgnoreReturnValue
comment|// to skip a byte
annotation|@
name|Override
DECL|method|readByte ()
name|byte
name|readByte
parameter_list|()
function_decl|;
annotation|@
name|CanIgnoreReturnValue
comment|// to skip a byte
annotation|@
name|Override
DECL|method|readUnsignedByte ()
name|int
name|readUnsignedByte
parameter_list|()
function_decl|;
annotation|@
name|CanIgnoreReturnValue
comment|// to skip some bytes
annotation|@
name|Override
DECL|method|readShort ()
name|short
name|readShort
parameter_list|()
function_decl|;
annotation|@
name|CanIgnoreReturnValue
comment|// to skip some bytes
annotation|@
name|Override
DECL|method|readUnsignedShort ()
name|int
name|readUnsignedShort
parameter_list|()
function_decl|;
annotation|@
name|CanIgnoreReturnValue
comment|// to skip some bytes
annotation|@
name|Override
DECL|method|readChar ()
name|char
name|readChar
parameter_list|()
function_decl|;
annotation|@
name|CanIgnoreReturnValue
comment|// to skip some bytes
annotation|@
name|Override
DECL|method|readInt ()
name|int
name|readInt
parameter_list|()
function_decl|;
annotation|@
name|CanIgnoreReturnValue
comment|// to skip some bytes
annotation|@
name|Override
DECL|method|readLong ()
name|long
name|readLong
parameter_list|()
function_decl|;
annotation|@
name|CanIgnoreReturnValue
comment|// to skip some bytes
annotation|@
name|Override
DECL|method|readFloat ()
name|float
name|readFloat
parameter_list|()
function_decl|;
annotation|@
name|CanIgnoreReturnValue
comment|// to skip some bytes
annotation|@
name|Override
DECL|method|readDouble ()
name|double
name|readDouble
parameter_list|()
function_decl|;
annotation|@
name|CanIgnoreReturnValue
comment|// to skip a line
annotation|@
name|Override
DECL|method|readLine ()
name|String
name|readLine
parameter_list|()
function_decl|;
annotation|@
name|CanIgnoreReturnValue
comment|// to skip a field
annotation|@
name|Override
DECL|method|readUTF ()
name|String
name|readUTF
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

