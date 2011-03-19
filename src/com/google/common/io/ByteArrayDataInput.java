begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * An extension of {@code DataInput} for reading from in-memory byte arrays; its  * methods offer identical functionality but do not throw {@link IOException}.  * If any method encounters the end of the array prematurely, it throws {@link  * IllegalStateException}.  *  * @author Kevin Bourrillion  * @since 1  */
end_comment

begin_interface
DECL|interface|ByteArrayDataInput
specifier|public
interface|interface
name|ByteArrayDataInput
extends|extends
name|DataInput
block|{
DECL|method|readFully (byte b[])
annotation|@
name|Override
name|void
name|readFully
parameter_list|(
name|byte
name|b
index|[]
parameter_list|)
function_decl|;
DECL|method|readFully (byte b[], int off, int len)
annotation|@
name|Override
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
DECL|method|skipBytes (int n)
annotation|@
name|Override
name|int
name|skipBytes
parameter_list|(
name|int
name|n
parameter_list|)
function_decl|;
DECL|method|readBoolean ()
annotation|@
name|Override
name|boolean
name|readBoolean
parameter_list|()
function_decl|;
DECL|method|readByte ()
annotation|@
name|Override
name|byte
name|readByte
parameter_list|()
function_decl|;
DECL|method|readUnsignedByte ()
annotation|@
name|Override
name|int
name|readUnsignedByte
parameter_list|()
function_decl|;
DECL|method|readShort ()
annotation|@
name|Override
name|short
name|readShort
parameter_list|()
function_decl|;
DECL|method|readUnsignedShort ()
annotation|@
name|Override
name|int
name|readUnsignedShort
parameter_list|()
function_decl|;
DECL|method|readChar ()
annotation|@
name|Override
name|char
name|readChar
parameter_list|()
function_decl|;
DECL|method|readInt ()
annotation|@
name|Override
name|int
name|readInt
parameter_list|()
function_decl|;
DECL|method|readLong ()
annotation|@
name|Override
name|long
name|readLong
parameter_list|()
function_decl|;
DECL|method|readFloat ()
annotation|@
name|Override
name|float
name|readFloat
parameter_list|()
function_decl|;
DECL|method|readDouble ()
annotation|@
name|Override
name|double
name|readDouble
parameter_list|()
function_decl|;
DECL|method|readLine ()
annotation|@
name|Override
name|String
name|readLine
parameter_list|()
function_decl|;
DECL|method|readUTF ()
annotation|@
name|Override
name|String
name|readUTF
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

