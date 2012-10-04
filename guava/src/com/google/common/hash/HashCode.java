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
name|common
operator|.
name|base
operator|.
name|Preconditions
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
name|primitives
operator|.
name|Ints
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|MessageDigest
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
comment|/**  * An immutable hash code of arbitrary bit length.  *  * @author Dimitris Andreou  * @since 11.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|HashCode
specifier|public
specifier|abstract
class|class
name|HashCode
block|{
DECL|method|HashCode ()
name|HashCode
parameter_list|()
block|{}
comment|/**    * Returns the first four bytes of {@linkplain #asBytes() this hashcode's bytes}, converted to    * an {@code int} value in little-endian order.    */
DECL|method|asInt ()
specifier|public
specifier|abstract
name|int
name|asInt
parameter_list|()
function_decl|;
comment|/**    * Returns the first eight bytes of {@linkplain #asBytes() this hashcode's bytes}, converted to    * a {@code long} value in little-endian order.    *    * @throws IllegalStateException if {@code bits()< 64}    */
DECL|method|asLong ()
specifier|public
specifier|abstract
name|long
name|asLong
parameter_list|()
function_decl|;
comment|/**    * If this hashcode has enough bits, returns {@code asLong()}, otherwise returns a {@code long}    * value with {@code asInt()} as the least-significant four bytes and {@code 0x00} as    * each of the most-significant four bytes.    *    * @since 14.0 (since 11.0 as {@code Hashing.padToLong(HashCode)})    */
DECL|method|padToLong ()
specifier|public
specifier|abstract
name|long
name|padToLong
parameter_list|()
function_decl|;
comment|/**    * Returns the value of this hash code as a byte array. The caller may modify the byte array;    * changes to it will<i>not</i> be reflected in this {@code HashCode} object or any other arrays    * returned by this method.    */
comment|// TODO(user): consider ByteString here, when that is available
DECL|method|asBytes ()
specifier|public
specifier|abstract
name|byte
index|[]
name|asBytes
parameter_list|()
function_decl|;
comment|/**    * Copies bytes from this hash code into {@code dest}.    *    * @param dest the byte array into which the hash code will be written    * @param offset the start offset in the data    * @param maxLength the maximum number of bytes to write    * @return the number of bytes written to {@code dest}    * @throws IndexOutOfBoundsException if there is not enough room in {@code dest}    */
DECL|method|writeBytesTo (byte[] dest, int offset, int maxLength)
specifier|public
name|int
name|writeBytesTo
parameter_list|(
name|byte
index|[]
name|dest
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|maxLength
parameter_list|)
block|{
name|byte
index|[]
name|hash
init|=
name|asBytes
argument_list|()
decl_stmt|;
name|maxLength
operator|=
name|Ints
operator|.
name|min
argument_list|(
name|maxLength
argument_list|,
name|hash
operator|.
name|length
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkPositionIndexes
argument_list|(
name|offset
argument_list|,
name|offset
operator|+
name|maxLength
argument_list|,
name|dest
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|hash
argument_list|,
literal|0
argument_list|,
name|dest
argument_list|,
name|offset
argument_list|,
name|maxLength
argument_list|)
expr_stmt|;
return|return
name|maxLength
return|;
block|}
comment|/**    * Returns the number of bits in this hash code; a positive multiple of 32.    */
DECL|method|bits ()
specifier|public
specifier|abstract
name|int
name|bits
parameter_list|()
function_decl|;
DECL|method|equals (@ullable Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|HashCode
condition|)
block|{
name|HashCode
name|that
init|=
operator|(
name|HashCode
operator|)
name|object
decl_stmt|;
comment|// Undocumented: this is a non-short-circuiting equals(), in case this is a cryptographic
comment|// hash code, in which case we don't want to leak timing information
return|return
name|MessageDigest
operator|.
name|isEqual
argument_list|(
name|this
operator|.
name|asBytes
argument_list|()
argument_list|,
name|that
operator|.
name|asBytes
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**    * Returns a "Java hash code" for this {@code HashCode} instance; this is well-defined    * (so, for example, you can safely put {@code HashCode} instances into a {@code    * HashSet}) but is otherwise probably not what you want to use.    */
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
comment|/*      * As long as the hash function that produced this isn't of horrible quality, this      * won't be of horrible quality either.      */
return|return
name|asInt
argument_list|()
return|;
block|}
comment|/**    * Returns a string containing each byte of {@link #asBytes}, in order, as a two-digit unsigned    * hexadecimal number in lower case.    *    *<p>Note that if the output is considered to be a single hexadecimal number, this hash code's    * bytes are the<i>big-endian</i> representation of that number. This may be surprising since    * everything else in the hashing API uniformly treats multibyte values as little-endian. But    * this format conveniently matches that of utilities such as the UNIX {@code md5sum} command.    */
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|byte
index|[]
name|bytes
init|=
name|asBytes
argument_list|()
decl_stmt|;
comment|// TODO(user): Use c.g.common.base.ByteArrays once it is open sourced.
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|2
operator|*
name|bytes
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|byte
name|b
range|:
name|bytes
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|hexDigits
index|[
operator|(
name|b
operator|>>
literal|4
operator|)
operator|&
literal|0xf
index|]
argument_list|)
operator|.
name|append
argument_list|(
name|hexDigits
index|[
name|b
operator|&
literal|0xf
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|field|hexDigits
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|hexDigits
init|=
literal|"0123456789abcdef"
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
block|}
end_class

end_unit

