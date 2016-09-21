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
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_comment
comment|/**  * A {@link PrimitiveSink} that can compute a hash code after reading the input. Each hasher should  * translate all multibyte values ({@link #putInt(int)}, {@link #putLong(long)}, etc) to bytes in  * little-endian order.  *  *<p><b>Warning:</b> The result of calling any methods after calling {@link #hash} is undefined.  *  *<p><b>Warning:</b> Using a specific character encoding when hashing a {@link CharSequence} with  * {@link #putString(CharSequence, Charset)} is generally only useful for cross-language  * compatibility (otherwise prefer {@link #putUnencodedChars}). However, the character encodings  * must be identical across languages. Also beware that {@link Charset} definitions may occasionally  * change between Java releases.  *  *<p><b>Warning:</b> Chunks of data that are put into the {@link Hasher} are not delimited. The  * resulting {@link HashCode} is dependent only on the bytes inserted, and the order in which they  * were inserted, not how those bytes were chunked into discrete put() operations. For example, the  * following three expressions all generate colliding hash codes:<pre>   {@code  *  *   newHasher().putByte(b1).putByte(b2).putByte(b3).hash()  *   newHasher().putByte(b1).putBytes(new byte[] { b2, b3 }).hash()  *   newHasher().putBytes(new byte[] { b1, b2, b3 }).hash()}</pre>  *  *<p>If you wish to avoid this, you should either prepend or append the size of each chunk. Keep in  * mind that when dealing with char sequences, the encoded form of two concatenated char sequences  * is not equivalent to the concatenation of their encoded form. Therefore,  * {@link #putString(CharSequence, Charset)} should only be used consistently with<i>complete</i>  * sequences and not broken into chunks.  *  * @author Kevin Bourrillion  * @since 11.0  */
end_comment

begin_interface
annotation|@
name|Beta
annotation|@
name|CanIgnoreReturnValue
DECL|interface|Hasher
specifier|public
interface|interface
name|Hasher
extends|extends
name|PrimitiveSink
block|{
annotation|@
name|Override
DECL|method|putByte (byte b)
name|Hasher
name|putByte
parameter_list|(
name|byte
name|b
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|putBytes (byte[] bytes)
name|Hasher
name|putBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|putBytes (byte[] bytes, int off, int len)
name|Hasher
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
annotation|@
name|Override
DECL|method|putShort (short s)
name|Hasher
name|putShort
parameter_list|(
name|short
name|s
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|putInt (int i)
name|Hasher
name|putInt
parameter_list|(
name|int
name|i
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|putLong (long l)
name|Hasher
name|putLong
parameter_list|(
name|long
name|l
parameter_list|)
function_decl|;
comment|/**    * Equivalent to {@code putInt(Float.floatToRawIntBits(f))}.    */
annotation|@
name|Override
DECL|method|putFloat (float f)
name|Hasher
name|putFloat
parameter_list|(
name|float
name|f
parameter_list|)
function_decl|;
comment|/**    * Equivalent to {@code putLong(Double.doubleToRawLongBits(d))}.    */
annotation|@
name|Override
DECL|method|putDouble (double d)
name|Hasher
name|putDouble
parameter_list|(
name|double
name|d
parameter_list|)
function_decl|;
comment|/**    * Equivalent to {@code putByte(b ? (byte) 1 : (byte) 0)}.    */
annotation|@
name|Override
DECL|method|putBoolean (boolean b)
name|Hasher
name|putBoolean
parameter_list|(
name|boolean
name|b
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|putChar (char c)
name|Hasher
name|putChar
parameter_list|(
name|char
name|c
parameter_list|)
function_decl|;
comment|/**    * Equivalent to processing each {@code char} value in the {@code CharSequence}, in order. In    * other words, no character encoding is performed; the low byte and high byte of each {@code    * char} are hashed directly (in that order). The input must not be updated while this method is    * in progress.    *    *<p><b>Warning:</b> This method will produce different output than most other languages do when    * running the same hash function on the equivalent input. For cross-language compatibility, use    * {@link #putString}, usually with a charset of UTF-8. For other use cases, use {@code    * putUnencodedChars}.    *    * @since 15.0 (since 11.0 as putString(CharSequence)).    */
annotation|@
name|Override
DECL|method|putUnencodedChars (CharSequence charSequence)
name|Hasher
name|putUnencodedChars
parameter_list|(
name|CharSequence
name|charSequence
parameter_list|)
function_decl|;
comment|/**    * Equivalent to {@code putBytes(charSequence.toString().getBytes(charset))}.    *    *<p><b>Warning:</b> This method, which reencodes the input before hashing it, is useful only for    * cross-language compatibility. For other use cases, prefer {@link #putUnencodedChars}, which is    * faster, produces the same output across Java releases, and hashes every {@code char} in the    * input, even if some are invalid.    */
annotation|@
name|Override
DECL|method|putString (CharSequence charSequence, Charset charset)
name|Hasher
name|putString
parameter_list|(
name|CharSequence
name|charSequence
parameter_list|,
name|Charset
name|charset
parameter_list|)
function_decl|;
comment|/**    * A simple convenience for {@code funnel.funnel(object, this)}.    */
DECL|method|putObject (T instance, Funnel<? super T> funnel)
parameter_list|<
name|T
parameter_list|>
name|Hasher
name|putObject
parameter_list|(
name|T
name|instance
parameter_list|,
name|Funnel
argument_list|<
name|?
super|super
name|T
argument_list|>
name|funnel
parameter_list|)
function_decl|;
comment|/**    * Computes a hash code based on the data that have been provided to this hasher. The result is    * unspecified if this method is called more than once on the same instance.    */
DECL|method|hash ()
name|HashCode
name|hash
parameter_list|()
function_decl|;
comment|/**    * {@inheritDoc}    *    * @deprecated This returns {@link Object#hashCode()}; you almost certainly mean to call    *     {@code hash().asInt()}.    */
annotation|@
name|Override
annotation|@
name|Deprecated
DECL|method|hashCode ()
name|int
name|hashCode
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

