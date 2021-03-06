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
name|checkArgument
import|;
end_import

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
name|checkState
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|UnsignedInts
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
name|Serializable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
import|;
end_import

begin_comment
comment|/**  * An immutable hash code of arbitrary bit length.  *  * @author Dimitris Andreou  * @author Kurt Alfred Kluever  * @since 11.0  */
end_comment

begin_class
annotation|@
name|ElementTypesAreNonnullByDefault
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
comment|/** Returns the number of bits in this hash code; a positive multiple of 8. */
DECL|method|bits ()
specifier|public
specifier|abstract
name|int
name|bits
parameter_list|()
function_decl|;
comment|/**    * Returns the first four bytes of {@linkplain #asBytes() this hashcode's bytes}, converted to an    * {@code int} value in little-endian order.    *    * @throws IllegalStateException if {@code bits()< 32}    */
DECL|method|asInt ()
specifier|public
specifier|abstract
name|int
name|asInt
parameter_list|()
function_decl|;
comment|/**    * Returns the first eight bytes of {@linkplain #asBytes() this hashcode's bytes}, converted to a    * {@code long} value in little-endian order.    *    * @throws IllegalStateException if {@code bits()< 64}    */
DECL|method|asLong ()
specifier|public
specifier|abstract
name|long
name|asLong
parameter_list|()
function_decl|;
comment|/**    * If this hashcode has enough bits, returns {@code asLong()}, otherwise returns a {@code long}    * value with {@code asBytes()} as the least-significant bytes and {@code 0x00} as the remaining    * most-significant bytes.    *    * @since 14.0 (since 11.0 as {@code Hashing.padToLong(HashCode)})    */
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
annotation|@
name|CanIgnoreReturnValue
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
name|maxLength
operator|=
name|Ints
operator|.
name|min
argument_list|(
name|maxLength
argument_list|,
name|bits
argument_list|()
operator|/
literal|8
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
name|writeBytesToImpl
argument_list|(
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
DECL|method|writeBytesToImpl (byte[] dest, int offset, int maxLength)
specifier|abstract
name|void
name|writeBytesToImpl
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
function_decl|;
comment|/**    * Returns a mutable view of the underlying bytes for the given {@code HashCode} if it is a    * byte-based hashcode. Otherwise it returns {@link HashCode#asBytes}. Do<i>not</i> mutate this    * array or else you will break the immutability contract of {@code HashCode}.    */
DECL|method|getBytesInternal ()
name|byte
index|[]
name|getBytesInternal
parameter_list|()
block|{
return|return
name|asBytes
argument_list|()
return|;
block|}
comment|/**    * Returns whether this {@code HashCode} and that {@code HashCode} have the same value, given that    * they have the same number of bits.    */
DECL|method|equalsSameBits (HashCode that)
specifier|abstract
name|boolean
name|equalsSameBits
parameter_list|(
name|HashCode
name|that
parameter_list|)
function_decl|;
comment|/**    * Creates a 32-bit {@code HashCode} representation of the given int value. The underlying bytes    * are interpreted in little endian order.    *    * @since 15.0 (since 12.0 in HashCodes)    */
DECL|method|fromInt (int hash)
specifier|public
specifier|static
name|HashCode
name|fromInt
parameter_list|(
name|int
name|hash
parameter_list|)
block|{
return|return
operator|new
name|IntHashCode
argument_list|(
name|hash
argument_list|)
return|;
block|}
DECL|class|IntHashCode
specifier|private
specifier|static
specifier|final
class|class
name|IntHashCode
extends|extends
name|HashCode
implements|implements
name|Serializable
block|{
DECL|field|hash
specifier|final
name|int
name|hash
decl_stmt|;
DECL|method|IntHashCode (int hash)
name|IntHashCode
parameter_list|(
name|int
name|hash
parameter_list|)
block|{
name|this
operator|.
name|hash
operator|=
name|hash
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|bits ()
specifier|public
name|int
name|bits
parameter_list|()
block|{
return|return
literal|32
return|;
block|}
annotation|@
name|Override
DECL|method|asBytes ()
specifier|public
name|byte
index|[]
name|asBytes
parameter_list|()
block|{
return|return
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
name|hash
block|,
call|(
name|byte
call|)
argument_list|(
name|hash
operator|>>
literal|8
argument_list|)
block|,
call|(
name|byte
call|)
argument_list|(
name|hash
operator|>>
literal|16
argument_list|)
block|,
call|(
name|byte
call|)
argument_list|(
name|hash
operator|>>
literal|24
argument_list|)
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|asInt ()
specifier|public
name|int
name|asInt
parameter_list|()
block|{
return|return
name|hash
return|;
block|}
annotation|@
name|Override
DECL|method|asLong ()
specifier|public
name|long
name|asLong
parameter_list|()
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"this HashCode only has 32 bits; cannot create a long"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|padToLong ()
specifier|public
name|long
name|padToLong
parameter_list|()
block|{
return|return
name|UnsignedInts
operator|.
name|toLong
argument_list|(
name|hash
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|writeBytesToImpl (byte[] dest, int offset, int maxLength)
name|void
name|writeBytesToImpl
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|maxLength
condition|;
name|i
operator|++
control|)
block|{
name|dest
index|[
name|offset
operator|+
name|i
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|hash
operator|>>
operator|(
name|i
operator|*
literal|8
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|equalsSameBits (HashCode that)
name|boolean
name|equalsSameBits
parameter_list|(
name|HashCode
name|that
parameter_list|)
block|{
return|return
name|hash
operator|==
name|that
operator|.
name|asInt
argument_list|()
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
comment|/**    * Creates a 64-bit {@code HashCode} representation of the given long value. The underlying bytes    * are interpreted in little endian order.    *    * @since 15.0 (since 12.0 in HashCodes)    */
DECL|method|fromLong (long hash)
specifier|public
specifier|static
name|HashCode
name|fromLong
parameter_list|(
name|long
name|hash
parameter_list|)
block|{
return|return
operator|new
name|LongHashCode
argument_list|(
name|hash
argument_list|)
return|;
block|}
DECL|class|LongHashCode
specifier|private
specifier|static
specifier|final
class|class
name|LongHashCode
extends|extends
name|HashCode
implements|implements
name|Serializable
block|{
DECL|field|hash
specifier|final
name|long
name|hash
decl_stmt|;
DECL|method|LongHashCode (long hash)
name|LongHashCode
parameter_list|(
name|long
name|hash
parameter_list|)
block|{
name|this
operator|.
name|hash
operator|=
name|hash
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|bits ()
specifier|public
name|int
name|bits
parameter_list|()
block|{
return|return
literal|64
return|;
block|}
annotation|@
name|Override
DECL|method|asBytes ()
specifier|public
name|byte
index|[]
name|asBytes
parameter_list|()
block|{
return|return
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
name|hash
block|,
call|(
name|byte
call|)
argument_list|(
name|hash
operator|>>
literal|8
argument_list|)
block|,
call|(
name|byte
call|)
argument_list|(
name|hash
operator|>>
literal|16
argument_list|)
block|,
call|(
name|byte
call|)
argument_list|(
name|hash
operator|>>
literal|24
argument_list|)
block|,
call|(
name|byte
call|)
argument_list|(
name|hash
operator|>>
literal|32
argument_list|)
block|,
call|(
name|byte
call|)
argument_list|(
name|hash
operator|>>
literal|40
argument_list|)
block|,
call|(
name|byte
call|)
argument_list|(
name|hash
operator|>>
literal|48
argument_list|)
block|,
call|(
name|byte
call|)
argument_list|(
name|hash
operator|>>
literal|56
argument_list|)
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|asInt ()
specifier|public
name|int
name|asInt
parameter_list|()
block|{
return|return
operator|(
name|int
operator|)
name|hash
return|;
block|}
annotation|@
name|Override
DECL|method|asLong ()
specifier|public
name|long
name|asLong
parameter_list|()
block|{
return|return
name|hash
return|;
block|}
annotation|@
name|Override
DECL|method|padToLong ()
specifier|public
name|long
name|padToLong
parameter_list|()
block|{
return|return
name|hash
return|;
block|}
annotation|@
name|Override
DECL|method|writeBytesToImpl (byte[] dest, int offset, int maxLength)
name|void
name|writeBytesToImpl
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|maxLength
condition|;
name|i
operator|++
control|)
block|{
name|dest
index|[
name|offset
operator|+
name|i
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|hash
operator|>>
operator|(
name|i
operator|*
literal|8
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|equalsSameBits (HashCode that)
name|boolean
name|equalsSameBits
parameter_list|(
name|HashCode
name|that
parameter_list|)
block|{
return|return
name|hash
operator|==
name|that
operator|.
name|asLong
argument_list|()
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
comment|/**    * Creates a {@code HashCode} from a byte array. The array is defensively copied to preserve the    * immutability contract of {@code HashCode}. The array cannot be empty.    *    * @since 15.0 (since 12.0 in HashCodes)    */
DECL|method|fromBytes (byte[] bytes)
specifier|public
specifier|static
name|HashCode
name|fromBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|bytes
operator|.
name|length
operator|>=
literal|1
argument_list|,
literal|"A HashCode must contain at least 1 byte."
argument_list|)
expr_stmt|;
return|return
name|fromBytesNoCopy
argument_list|(
name|bytes
operator|.
name|clone
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Creates a {@code HashCode} from a byte array. The array is<i>not</i> copied defensively, so it    * must be handed-off so as to preserve the immutability contract of {@code HashCode}.    */
DECL|method|fromBytesNoCopy (byte[] bytes)
specifier|static
name|HashCode
name|fromBytesNoCopy
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
operator|new
name|BytesHashCode
argument_list|(
name|bytes
argument_list|)
return|;
block|}
DECL|class|BytesHashCode
specifier|private
specifier|static
specifier|final
class|class
name|BytesHashCode
extends|extends
name|HashCode
implements|implements
name|Serializable
block|{
DECL|field|bytes
specifier|final
name|byte
index|[]
name|bytes
decl_stmt|;
DECL|method|BytesHashCode (byte[] bytes)
name|BytesHashCode
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|this
operator|.
name|bytes
operator|=
name|checkNotNull
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|bits ()
specifier|public
name|int
name|bits
parameter_list|()
block|{
return|return
name|bytes
operator|.
name|length
operator|*
literal|8
return|;
block|}
annotation|@
name|Override
DECL|method|asBytes ()
specifier|public
name|byte
index|[]
name|asBytes
parameter_list|()
block|{
return|return
name|bytes
operator|.
name|clone
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|asInt ()
specifier|public
name|int
name|asInt
parameter_list|()
block|{
name|checkState
argument_list|(
name|bytes
operator|.
name|length
operator|>=
literal|4
argument_list|,
literal|"HashCode#asInt() requires>= 4 bytes (it only has %s bytes)."
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
operator|(
name|bytes
index|[
literal|0
index|]
operator|&
literal|0xFF
operator|)
operator||
operator|(
operator|(
name|bytes
index|[
literal|1
index|]
operator|&
literal|0xFF
operator|)
operator|<<
literal|8
operator|)
operator||
operator|(
operator|(
name|bytes
index|[
literal|2
index|]
operator|&
literal|0xFF
operator|)
operator|<<
literal|16
operator|)
operator||
operator|(
operator|(
name|bytes
index|[
literal|3
index|]
operator|&
literal|0xFF
operator|)
operator|<<
literal|24
operator|)
return|;
block|}
annotation|@
name|Override
DECL|method|asLong ()
specifier|public
name|long
name|asLong
parameter_list|()
block|{
name|checkState
argument_list|(
name|bytes
operator|.
name|length
operator|>=
literal|8
argument_list|,
literal|"HashCode#asLong() requires>= 8 bytes (it only has %s bytes)."
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|padToLong
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|padToLong ()
specifier|public
name|long
name|padToLong
parameter_list|()
block|{
name|long
name|retVal
init|=
operator|(
name|bytes
index|[
literal|0
index|]
operator|&
literal|0xFF
operator|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|Math
operator|.
name|min
argument_list|(
name|bytes
operator|.
name|length
argument_list|,
literal|8
argument_list|)
condition|;
name|i
operator|++
control|)
block|{
name|retVal
operator||=
operator|(
name|bytes
index|[
name|i
index|]
operator|&
literal|0xFFL
operator|)
operator|<<
operator|(
name|i
operator|*
literal|8
operator|)
expr_stmt|;
block|}
return|return
name|retVal
return|;
block|}
annotation|@
name|Override
DECL|method|writeBytesToImpl (byte[] dest, int offset, int maxLength)
name|void
name|writeBytesToImpl
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
name|System
operator|.
name|arraycopy
argument_list|(
name|bytes
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
block|}
annotation|@
name|Override
DECL|method|getBytesInternal ()
name|byte
index|[]
name|getBytesInternal
parameter_list|()
block|{
return|return
name|bytes
return|;
block|}
annotation|@
name|Override
DECL|method|equalsSameBits (HashCode that)
name|boolean
name|equalsSameBits
parameter_list|(
name|HashCode
name|that
parameter_list|)
block|{
comment|// We don't use MessageDigest.isEqual() here because its contract does not guarantee
comment|// constant-time evaluation (no short-circuiting).
if|if
condition|(
name|this
operator|.
name|bytes
operator|.
name|length
operator|!=
name|that
operator|.
name|getBytesInternal
argument_list|()
operator|.
name|length
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|areEqual
init|=
literal|true
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|this
operator|.
name|bytes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|areEqual
operator|&=
operator|(
name|this
operator|.
name|bytes
index|[
name|i
index|]
operator|==
name|that
operator|.
name|getBytesInternal
argument_list|()
index|[
name|i
index|]
operator|)
expr_stmt|;
block|}
return|return
name|areEqual
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
comment|/**    * Creates a {@code HashCode} from a hexadecimal ({@code base 16}) encoded string. The string must    * be at least 2 characters long, and contain only valid, lower-cased hexadecimal characters.    *    *<p>This method accepts the exact format generated by {@link #toString}. If you require more    * lenient {@code base 16} decoding, please use {@link com.google.common.io.BaseEncoding#decode}    * (and pass the result to {@link #fromBytes}).    *    * @since 15.0    */
DECL|method|fromString (String string)
specifier|public
specifier|static
name|HashCode
name|fromString
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|string
operator|.
name|length
argument_list|()
operator|>=
literal|2
argument_list|,
literal|"input string (%s) must have at least 2 characters"
argument_list|,
name|string
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|string
operator|.
name|length
argument_list|()
operator|%
literal|2
operator|==
literal|0
argument_list|,
literal|"input string (%s) must have an even number of characters"
argument_list|,
name|string
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
name|string
operator|.
name|length
argument_list|()
operator|/
literal|2
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|string
operator|.
name|length
argument_list|()
condition|;
name|i
operator|+=
literal|2
control|)
block|{
name|int
name|ch1
init|=
name|decode
argument_list|(
name|string
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
operator|<<
literal|4
decl_stmt|;
name|int
name|ch2
init|=
name|decode
argument_list|(
name|string
operator|.
name|charAt
argument_list|(
name|i
operator|+
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|bytes
index|[
name|i
operator|/
literal|2
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|ch1
operator|+
name|ch2
argument_list|)
expr_stmt|;
block|}
return|return
name|fromBytesNoCopy
argument_list|(
name|bytes
argument_list|)
return|;
block|}
DECL|method|decode (char ch)
specifier|private
specifier|static
name|int
name|decode
parameter_list|(
name|char
name|ch
parameter_list|)
block|{
if|if
condition|(
name|ch
operator|>=
literal|'0'
operator|&&
name|ch
operator|<=
literal|'9'
condition|)
block|{
return|return
name|ch
operator|-
literal|'0'
return|;
block|}
if|if
condition|(
name|ch
operator|>=
literal|'a'
operator|&&
name|ch
operator|<=
literal|'f'
condition|)
block|{
return|return
name|ch
operator|-
literal|'a'
operator|+
literal|10
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Illegal hexadecimal character: "
operator|+
name|ch
argument_list|)
throw|;
block|}
comment|/**    * Returns {@code true} if {@code object} is a {@link HashCode} instance with the identical byte    * representation to this hash code.    *    *<p><b>Security note:</b> this method uses a constant-time (not short-circuiting) implementation    * to protect against<a href="http://en.wikipedia.org/wiki/Timing_attack">timing attacks</a>.    */
annotation|@
name|Override
DECL|method|equals (@heckForNull Object object)
specifier|public
specifier|final
name|boolean
name|equals
parameter_list|(
annotation|@
name|CheckForNull
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
return|return
name|bits
argument_list|()
operator|==
name|that
operator|.
name|bits
argument_list|()
operator|&&
name|equalsSameBits
argument_list|(
name|that
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**    * Returns a "Java hash code" for this {@code HashCode} instance; this is well-defined (so, for    * example, you can safely put {@code HashCode} instances into a {@code HashSet}) but is otherwise    * probably not what you want to use.    */
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
specifier|final
name|int
name|hashCode
parameter_list|()
block|{
comment|// If we have at least 4 bytes (32 bits), just take the first 4 bytes. Since this is
comment|// already a (presumably) high-quality hash code, any four bytes of it will do.
if|if
condition|(
name|bits
argument_list|()
operator|>=
literal|32
condition|)
block|{
return|return
name|asInt
argument_list|()
return|;
block|}
comment|// If we have less than 4 bytes, use them all.
name|byte
index|[]
name|bytes
init|=
name|getBytesInternal
argument_list|()
decl_stmt|;
name|int
name|val
init|=
operator|(
name|bytes
index|[
literal|0
index|]
operator|&
literal|0xFF
operator|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|bytes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|val
operator||=
operator|(
operator|(
name|bytes
index|[
name|i
index|]
operator|&
literal|0xFF
operator|)
operator|<<
operator|(
name|i
operator|*
literal|8
operator|)
operator|)
expr_stmt|;
block|}
return|return
name|val
return|;
block|}
comment|/**    * Returns a string containing each byte of {@link #asBytes}, in order, as a two-digit unsigned    * hexadecimal number in lower case.    *    *<p>Note that if the output is considered to be a single hexadecimal number, whether this string    * is big-endian or little-endian depends on the byte order of {@link #asBytes}. This may be    * surprising for implementations of {@code HashCode} that represent the number in big-endian    * since everything else in the hashing API uniformly treats multibyte values as little-endian.    *    *<p>To create a {@code HashCode} from its string representation, see {@link #fromString}.    */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
specifier|final
name|String
name|toString
parameter_list|()
block|{
name|byte
index|[]
name|bytes
init|=
name|getBytesInternal
argument_list|()
decl_stmt|;
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

