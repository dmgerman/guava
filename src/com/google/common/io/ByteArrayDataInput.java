begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * An extension of {@code DataInput} for reading from in-memory byte arrays; its  * methods offer identical functionality but do not throw {@link IOException}.  * If any method encounters the end of the array prematurely, it throws {@link  * IllegalStateException}.  *  * @author Kevin Bourrillion  * @since 2009.09.15<b>tentative</b>  */
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
comment|/*@Override*/
name|void
name|readFully
parameter_list|(
name|byte
name|b
index|[]
parameter_list|)
function_decl|;
DECL|method|readFully (byte b[], int off, int len)
comment|/*@Override*/
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
comment|/*@Override*/
name|int
name|skipBytes
parameter_list|(
name|int
name|n
parameter_list|)
function_decl|;
DECL|method|readBoolean ()
comment|/*@Override*/
name|boolean
name|readBoolean
parameter_list|()
function_decl|;
DECL|method|readByte ()
comment|/*@Override*/
name|byte
name|readByte
parameter_list|()
function_decl|;
DECL|method|readUnsignedByte ()
comment|/*@Override*/
name|int
name|readUnsignedByte
parameter_list|()
function_decl|;
DECL|method|readShort ()
comment|/*@Override*/
name|short
name|readShort
parameter_list|()
function_decl|;
DECL|method|readUnsignedShort ()
comment|/*@Override*/
name|int
name|readUnsignedShort
parameter_list|()
function_decl|;
DECL|method|readChar ()
comment|/*@Override*/
name|char
name|readChar
parameter_list|()
function_decl|;
DECL|method|readInt ()
comment|/*@Override*/
name|int
name|readInt
parameter_list|()
function_decl|;
DECL|method|readLong ()
comment|/*@Override*/
name|long
name|readLong
parameter_list|()
function_decl|;
DECL|method|readFloat ()
comment|/*@Override*/
name|float
name|readFloat
parameter_list|()
function_decl|;
DECL|method|readDouble ()
comment|/*@Override*/
name|double
name|readDouble
parameter_list|()
function_decl|;
DECL|method|readLine ()
comment|/*@Override*/
name|String
name|readLine
parameter_list|()
function_decl|;
DECL|method|readUTF ()
comment|/*@Override*/
name|String
name|readUTF
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

