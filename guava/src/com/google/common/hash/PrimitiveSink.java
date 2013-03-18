begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.hash
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
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
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_comment
comment|/**  * An object which can receive a stream of primitive values.  *   * @author Kevin Bourrillion  * @since 12.0 (in 11.0 as {@code Sink})  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|PrimitiveSink
specifier|public
interface|interface
name|PrimitiveSink
block|{
comment|/**    * Puts a byte into this sink.    *    * @param b a byte    * @return this instance    */
DECL|method|putByte (byte b)
name|PrimitiveSink
name|putByte
parameter_list|(
name|byte
name|b
parameter_list|)
function_decl|;
comment|/**    * Puts an array of bytes into this sink.    *    * @param bytes a byte array    * @return this instance    */
DECL|method|putBytes (byte[] bytes)
name|PrimitiveSink
name|putBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
function_decl|;
comment|/**    * Puts a chunk of an array of bytes into this sink. {@code bytes[off]} is the first byte written,    * {@code bytes[off + len - 1]} is the last.     *     * @param bytes a byte array    * @param off the start offset in the array    * @param len the number of bytes to write    * @return this instance     * @throws IndexOutOfBoundsException if {@code off< 0} or {@code off + len> bytes.length} or    *   {@code len< 0}    */
DECL|method|putBytes (byte[] bytes, int off, int len)
name|PrimitiveSink
name|putBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
function_decl|;
comment|/**    * Puts a short into this sink.    */
DECL|method|putShort (short s)
name|PrimitiveSink
name|putShort
parameter_list|(
name|short
name|s
parameter_list|)
function_decl|;
comment|/**    * Puts an int into this sink.    */
DECL|method|putInt (int i)
name|PrimitiveSink
name|putInt
parameter_list|(
name|int
name|i
parameter_list|)
function_decl|;
comment|/**    * Puts a long into this sink.    */
DECL|method|putLong (long l)
name|PrimitiveSink
name|putLong
parameter_list|(
name|long
name|l
parameter_list|)
function_decl|;
comment|/**    * Puts a float into this sink.    */
DECL|method|putFloat (float f)
name|PrimitiveSink
name|putFloat
parameter_list|(
name|float
name|f
parameter_list|)
function_decl|;
comment|/**    * Puts a double into this sink.    */
DECL|method|putDouble (double d)
name|PrimitiveSink
name|putDouble
parameter_list|(
name|double
name|d
parameter_list|)
function_decl|;
comment|/**    * Puts a boolean into this sink.    */
DECL|method|putBoolean (boolean b)
name|PrimitiveSink
name|putBoolean
parameter_list|(
name|boolean
name|b
parameter_list|)
function_decl|;
comment|/**    * Puts a character into this sink.    */
DECL|method|putChar (char c)
name|PrimitiveSink
name|putChar
parameter_list|(
name|char
name|c
parameter_list|)
function_decl|;
comment|/**    * Puts a string into this sink.    *    * @deprecated Use {PrimitiveSink#putUnencodedChars} instead.    */
annotation|@
name|Deprecated
DECL|method|putString (CharSequence charSequence)
name|PrimitiveSink
name|putString
parameter_list|(
name|CharSequence
name|charSequence
parameter_list|)
function_decl|;
comment|/**    * Puts each 16-bit code unit from the {@link CharSequence} into this sink.    *    * @since 15.0 (since 11.0 as putString(CharSequence))    */
DECL|method|putUnencodedChars (CharSequence charSequence)
name|PrimitiveSink
name|putUnencodedChars
parameter_list|(
name|CharSequence
name|charSequence
parameter_list|)
function_decl|;
comment|/**    * Puts a string into this sink using the given charset.    */
DECL|method|putString (CharSequence charSequence, Charset charset)
name|PrimitiveSink
name|putString
parameter_list|(
name|CharSequence
name|charSequence
parameter_list|,
name|Charset
name|charset
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

