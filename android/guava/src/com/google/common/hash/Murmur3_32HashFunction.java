begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_comment
comment|/*  * MurmurHash3 was written by Austin Appleby, and is placed in the public  * domain. The author hereby disclaims copyright to this source code.  */
end_comment

begin_comment
comment|/*  * Source:  * http://code.google.com/p/smhasher/source/browse/trunk/MurmurHash3.cpp  * (Modified to adapt to Guava coding conventions and to use the HashFunction interface)  */
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
name|checkPositionIndexes
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|UnsignedBytes
operator|.
name|toInt
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
name|Charsets
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
name|Chars
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
name|Longs
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
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|Immutable
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
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteOrder
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
comment|/**  * See MurmurHash3_x86_32 in<a  * href="https://github.com/aappleby/smhasher/blob/master/src/MurmurHash3.cpp">the C++  * implementation</a>.  *  * @author Austin Appleby  * @author Dimitris Andreou  * @author Kurt Alfred Kluever  */
end_comment

begin_class
annotation|@
name|Immutable
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|Murmur3_32HashFunction
specifier|final
class|class
name|Murmur3_32HashFunction
extends|extends
name|AbstractHashFunction
implements|implements
name|Serializable
block|{
DECL|field|MURMUR3_32
specifier|static
specifier|final
name|HashFunction
name|MURMUR3_32
init|=
operator|new
name|Murmur3_32HashFunction
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|field|GOOD_FAST_HASH_32
specifier|static
specifier|final
name|HashFunction
name|GOOD_FAST_HASH_32
init|=
operator|new
name|Murmur3_32HashFunction
argument_list|(
name|Hashing
operator|.
name|GOOD_FAST_HASH_SEED
argument_list|)
decl_stmt|;
DECL|field|CHUNK_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|CHUNK_SIZE
init|=
literal|4
decl_stmt|;
DECL|field|C1
specifier|private
specifier|static
specifier|final
name|int
name|C1
init|=
literal|0xcc9e2d51
decl_stmt|;
DECL|field|C2
specifier|private
specifier|static
specifier|final
name|int
name|C2
init|=
literal|0x1b873593
decl_stmt|;
DECL|field|seed
specifier|private
specifier|final
name|int
name|seed
decl_stmt|;
DECL|method|Murmur3_32HashFunction (int seed)
name|Murmur3_32HashFunction
parameter_list|(
name|int
name|seed
parameter_list|)
block|{
name|this
operator|.
name|seed
operator|=
name|seed
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
DECL|method|newHasher ()
specifier|public
name|Hasher
name|newHasher
parameter_list|()
block|{
return|return
operator|new
name|Murmur3_32Hasher
argument_list|(
name|seed
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Hashing.murmur3_32("
operator|+
name|seed
operator|+
literal|")"
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@heckForNull Object object)
specifier|public
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
name|Murmur3_32HashFunction
condition|)
block|{
name|Murmur3_32HashFunction
name|other
init|=
operator|(
name|Murmur3_32HashFunction
operator|)
name|object
decl_stmt|;
return|return
name|seed
operator|==
name|other
operator|.
name|seed
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|getClass
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|^
name|seed
return|;
block|}
annotation|@
name|Override
DECL|method|hashInt (int input)
specifier|public
name|HashCode
name|hashInt
parameter_list|(
name|int
name|input
parameter_list|)
block|{
name|int
name|k1
init|=
name|mixK1
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|int
name|h1
init|=
name|mixH1
argument_list|(
name|seed
argument_list|,
name|k1
argument_list|)
decl_stmt|;
return|return
name|fmix
argument_list|(
name|h1
argument_list|,
name|Ints
operator|.
name|BYTES
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashLong (long input)
specifier|public
name|HashCode
name|hashLong
parameter_list|(
name|long
name|input
parameter_list|)
block|{
name|int
name|low
init|=
operator|(
name|int
operator|)
name|input
decl_stmt|;
name|int
name|high
init|=
call|(
name|int
call|)
argument_list|(
name|input
operator|>>>
literal|32
argument_list|)
decl_stmt|;
name|int
name|k1
init|=
name|mixK1
argument_list|(
name|low
argument_list|)
decl_stmt|;
name|int
name|h1
init|=
name|mixH1
argument_list|(
name|seed
argument_list|,
name|k1
argument_list|)
decl_stmt|;
name|k1
operator|=
name|mixK1
argument_list|(
name|high
argument_list|)
expr_stmt|;
name|h1
operator|=
name|mixH1
argument_list|(
name|h1
argument_list|,
name|k1
argument_list|)
expr_stmt|;
return|return
name|fmix
argument_list|(
name|h1
argument_list|,
name|Longs
operator|.
name|BYTES
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashUnencodedChars (CharSequence input)
specifier|public
name|HashCode
name|hashUnencodedChars
parameter_list|(
name|CharSequence
name|input
parameter_list|)
block|{
name|int
name|h1
init|=
name|seed
decl_stmt|;
comment|// step through the CharSequence 2 chars at a time
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|input
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
name|k1
init|=
name|input
operator|.
name|charAt
argument_list|(
name|i
operator|-
literal|1
argument_list|)
operator||
operator|(
name|input
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|<<
literal|16
operator|)
decl_stmt|;
name|k1
operator|=
name|mixK1
argument_list|(
name|k1
argument_list|)
expr_stmt|;
name|h1
operator|=
name|mixH1
argument_list|(
name|h1
argument_list|,
name|k1
argument_list|)
expr_stmt|;
block|}
comment|// deal with any remaining characters
if|if
condition|(
operator|(
name|input
operator|.
name|length
argument_list|()
operator|&
literal|1
operator|)
operator|==
literal|1
condition|)
block|{
name|int
name|k1
init|=
name|input
operator|.
name|charAt
argument_list|(
name|input
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|k1
operator|=
name|mixK1
argument_list|(
name|k1
argument_list|)
expr_stmt|;
name|h1
operator|^=
name|k1
expr_stmt|;
block|}
return|return
name|fmix
argument_list|(
name|h1
argument_list|,
name|Chars
operator|.
name|BYTES
operator|*
name|input
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
comment|// need to use Charsets for Android tests to pass
annotation|@
name|Override
DECL|method|hashString (CharSequence input, Charset charset)
specifier|public
name|HashCode
name|hashString
parameter_list|(
name|CharSequence
name|input
parameter_list|,
name|Charset
name|charset
parameter_list|)
block|{
if|if
condition|(
name|Charsets
operator|.
name|UTF_8
operator|.
name|equals
argument_list|(
name|charset
argument_list|)
condition|)
block|{
name|int
name|utf16Length
init|=
name|input
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|h1
init|=
name|seed
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
name|int
name|len
init|=
literal|0
decl_stmt|;
comment|// This loop optimizes for pure ASCII.
while|while
condition|(
name|i
operator|+
literal|4
operator|<=
name|utf16Length
condition|)
block|{
name|char
name|c0
init|=
name|input
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|char
name|c1
init|=
name|input
operator|.
name|charAt
argument_list|(
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
name|char
name|c2
init|=
name|input
operator|.
name|charAt
argument_list|(
name|i
operator|+
literal|2
argument_list|)
decl_stmt|;
name|char
name|c3
init|=
name|input
operator|.
name|charAt
argument_list|(
name|i
operator|+
literal|3
argument_list|)
decl_stmt|;
if|if
condition|(
name|c0
operator|<
literal|0x80
operator|&&
name|c1
operator|<
literal|0x80
operator|&&
name|c2
operator|<
literal|0x80
operator|&&
name|c3
operator|<
literal|0x80
condition|)
block|{
name|int
name|k1
init|=
name|c0
operator||
operator|(
name|c1
operator|<<
literal|8
operator|)
operator||
operator|(
name|c2
operator|<<
literal|16
operator|)
operator||
operator|(
name|c3
operator|<<
literal|24
operator|)
decl_stmt|;
name|k1
operator|=
name|mixK1
argument_list|(
name|k1
argument_list|)
expr_stmt|;
name|h1
operator|=
name|mixH1
argument_list|(
name|h1
argument_list|,
name|k1
argument_list|)
expr_stmt|;
name|i
operator|+=
literal|4
expr_stmt|;
name|len
operator|+=
literal|4
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
name|long
name|buffer
init|=
literal|0
decl_stmt|;
name|int
name|shift
init|=
literal|0
decl_stmt|;
for|for
control|(
init|;
name|i
operator|<
name|utf16Length
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|input
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|<
literal|0x80
condition|)
block|{
name|buffer
operator||=
operator|(
name|long
operator|)
name|c
operator|<<
name|shift
expr_stmt|;
name|shift
operator|+=
literal|8
expr_stmt|;
name|len
operator|++
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|<
literal|0x800
condition|)
block|{
name|buffer
operator||=
name|charToTwoUtf8Bytes
argument_list|(
name|c
argument_list|)
operator|<<
name|shift
expr_stmt|;
name|shift
operator|+=
literal|16
expr_stmt|;
name|len
operator|+=
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
argument_list|<
name|Character
operator|.
name|MIN_SURROGATE
operator|||
name|c
argument_list|>
name|Character
operator|.
name|MAX_SURROGATE
condition|)
block|{
name|buffer
operator||=
name|charToThreeUtf8Bytes
argument_list|(
name|c
argument_list|)
operator|<<
name|shift
expr_stmt|;
name|shift
operator|+=
literal|24
expr_stmt|;
name|len
operator|+=
literal|3
expr_stmt|;
block|}
else|else
block|{
name|int
name|codePoint
init|=
name|Character
operator|.
name|codePointAt
argument_list|(
name|input
argument_list|,
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|codePoint
operator|==
name|c
condition|)
block|{
comment|// not a valid code point; let the JDK handle invalid Unicode
return|return
name|hashBytes
argument_list|(
name|input
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|charset
argument_list|)
argument_list|)
return|;
block|}
name|i
operator|++
expr_stmt|;
name|buffer
operator||=
name|codePointToFourUtf8Bytes
argument_list|(
name|codePoint
argument_list|)
operator|<<
name|shift
expr_stmt|;
name|len
operator|+=
literal|4
expr_stmt|;
block|}
if|if
condition|(
name|shift
operator|>=
literal|32
condition|)
block|{
name|int
name|k1
init|=
name|mixK1
argument_list|(
operator|(
name|int
operator|)
name|buffer
argument_list|)
decl_stmt|;
name|h1
operator|=
name|mixH1
argument_list|(
name|h1
argument_list|,
name|k1
argument_list|)
expr_stmt|;
name|buffer
operator|=
name|buffer
operator|>>>
literal|32
expr_stmt|;
name|shift
operator|-=
literal|32
expr_stmt|;
block|}
block|}
name|int
name|k1
init|=
name|mixK1
argument_list|(
operator|(
name|int
operator|)
name|buffer
argument_list|)
decl_stmt|;
name|h1
operator|^=
name|k1
expr_stmt|;
return|return
name|fmix
argument_list|(
name|h1
argument_list|,
name|len
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|hashBytes
argument_list|(
name|input
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|charset
argument_list|)
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|hashBytes (byte[] input, int off, int len)
specifier|public
name|HashCode
name|hashBytes
parameter_list|(
name|byte
index|[]
name|input
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
name|checkPositionIndexes
argument_list|(
name|off
argument_list|,
name|off
operator|+
name|len
argument_list|,
name|input
operator|.
name|length
argument_list|)
expr_stmt|;
name|int
name|h1
init|=
name|seed
decl_stmt|;
name|int
name|i
decl_stmt|;
for|for
control|(
name|i
operator|=
literal|0
init|;
name|i
operator|+
name|CHUNK_SIZE
operator|<=
name|len
condition|;
name|i
operator|+=
name|CHUNK_SIZE
control|)
block|{
name|int
name|k1
init|=
name|mixK1
argument_list|(
name|getIntLittleEndian
argument_list|(
name|input
argument_list|,
name|off
operator|+
name|i
argument_list|)
argument_list|)
decl_stmt|;
name|h1
operator|=
name|mixH1
argument_list|(
name|h1
argument_list|,
name|k1
argument_list|)
expr_stmt|;
block|}
name|int
name|k1
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|shift
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
operator|,
name|shift
operator|+=
literal|8
control|)
block|{
name|k1
operator|^=
name|toInt
argument_list|(
name|input
index|[
name|off
operator|+
name|i
index|]
argument_list|)
operator|<<
name|shift
expr_stmt|;
block|}
name|h1
operator|^=
name|mixK1
argument_list|(
name|k1
argument_list|)
expr_stmt|;
return|return
name|fmix
argument_list|(
name|h1
argument_list|,
name|len
argument_list|)
return|;
block|}
DECL|method|getIntLittleEndian (byte[] input, int offset)
specifier|private
specifier|static
name|int
name|getIntLittleEndian
parameter_list|(
name|byte
index|[]
name|input
parameter_list|,
name|int
name|offset
parameter_list|)
block|{
return|return
name|Ints
operator|.
name|fromBytes
argument_list|(
name|input
index|[
name|offset
operator|+
literal|3
index|]
argument_list|,
name|input
index|[
name|offset
operator|+
literal|2
index|]
argument_list|,
name|input
index|[
name|offset
operator|+
literal|1
index|]
argument_list|,
name|input
index|[
name|offset
index|]
argument_list|)
return|;
block|}
DECL|method|mixK1 (int k1)
specifier|private
specifier|static
name|int
name|mixK1
parameter_list|(
name|int
name|k1
parameter_list|)
block|{
name|k1
operator|*=
name|C1
expr_stmt|;
name|k1
operator|=
name|Integer
operator|.
name|rotateLeft
argument_list|(
name|k1
argument_list|,
literal|15
argument_list|)
expr_stmt|;
name|k1
operator|*=
name|C2
expr_stmt|;
return|return
name|k1
return|;
block|}
DECL|method|mixH1 (int h1, int k1)
specifier|private
specifier|static
name|int
name|mixH1
parameter_list|(
name|int
name|h1
parameter_list|,
name|int
name|k1
parameter_list|)
block|{
name|h1
operator|^=
name|k1
expr_stmt|;
name|h1
operator|=
name|Integer
operator|.
name|rotateLeft
argument_list|(
name|h1
argument_list|,
literal|13
argument_list|)
expr_stmt|;
name|h1
operator|=
name|h1
operator|*
literal|5
operator|+
literal|0xe6546b64
expr_stmt|;
return|return
name|h1
return|;
block|}
comment|// Finalization mix - force all bits of a hash block to avalanche
DECL|method|fmix (int h1, int length)
specifier|private
specifier|static
name|HashCode
name|fmix
parameter_list|(
name|int
name|h1
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|h1
operator|^=
name|length
expr_stmt|;
name|h1
operator|^=
name|h1
operator|>>>
literal|16
expr_stmt|;
name|h1
operator|*=
literal|0x85ebca6b
expr_stmt|;
name|h1
operator|^=
name|h1
operator|>>>
literal|13
expr_stmt|;
name|h1
operator|*=
literal|0xc2b2ae35
expr_stmt|;
name|h1
operator|^=
name|h1
operator|>>>
literal|16
expr_stmt|;
return|return
name|HashCode
operator|.
name|fromInt
argument_list|(
name|h1
argument_list|)
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
DECL|class|Murmur3_32Hasher
specifier|private
specifier|static
specifier|final
class|class
name|Murmur3_32Hasher
extends|extends
name|AbstractHasher
block|{
DECL|field|h1
specifier|private
name|int
name|h1
decl_stmt|;
DECL|field|buffer
specifier|private
name|long
name|buffer
decl_stmt|;
DECL|field|shift
specifier|private
name|int
name|shift
decl_stmt|;
DECL|field|length
specifier|private
name|int
name|length
decl_stmt|;
DECL|field|isDone
specifier|private
name|boolean
name|isDone
decl_stmt|;
DECL|method|Murmur3_32Hasher (int seed)
name|Murmur3_32Hasher
parameter_list|(
name|int
name|seed
parameter_list|)
block|{
name|this
operator|.
name|h1
operator|=
name|seed
expr_stmt|;
name|this
operator|.
name|length
operator|=
literal|0
expr_stmt|;
name|isDone
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|update (int nBytes, long update)
specifier|private
name|void
name|update
parameter_list|(
name|int
name|nBytes
parameter_list|,
name|long
name|update
parameter_list|)
block|{
comment|// 1<= nBytes<= 4
name|buffer
operator||=
operator|(
name|update
operator|&
literal|0xFFFFFFFFL
operator|)
operator|<<
name|shift
expr_stmt|;
name|shift
operator|+=
name|nBytes
operator|*
literal|8
expr_stmt|;
name|length
operator|+=
name|nBytes
expr_stmt|;
if|if
condition|(
name|shift
operator|>=
literal|32
condition|)
block|{
name|h1
operator|=
name|mixH1
argument_list|(
name|h1
argument_list|,
name|mixK1
argument_list|(
operator|(
name|int
operator|)
name|buffer
argument_list|)
argument_list|)
expr_stmt|;
name|buffer
operator|>>>=
literal|32
expr_stmt|;
name|shift
operator|-=
literal|32
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|putByte (byte b)
specifier|public
name|Hasher
name|putByte
parameter_list|(
name|byte
name|b
parameter_list|)
block|{
name|update
argument_list|(
literal|1
argument_list|,
name|b
operator|&
literal|0xFF
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|putBytes (byte[] bytes, int off, int len)
specifier|public
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
block|{
name|checkPositionIndexes
argument_list|(
name|off
argument_list|,
name|off
operator|+
name|len
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|int
name|i
decl_stmt|;
for|for
control|(
name|i
operator|=
literal|0
init|;
name|i
operator|+
literal|4
operator|<=
name|len
condition|;
name|i
operator|+=
literal|4
control|)
block|{
name|update
argument_list|(
literal|4
argument_list|,
name|getIntLittleEndian
argument_list|(
name|bytes
argument_list|,
name|off
operator|+
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|putByte
argument_list|(
name|bytes
index|[
name|off
operator|+
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|putBytes (ByteBuffer buffer)
specifier|public
name|Hasher
name|putBytes
parameter_list|(
name|ByteBuffer
name|buffer
parameter_list|)
block|{
name|ByteOrder
name|bo
init|=
name|buffer
operator|.
name|order
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|order
argument_list|(
name|ByteOrder
operator|.
name|LITTLE_ENDIAN
argument_list|)
expr_stmt|;
while|while
condition|(
name|buffer
operator|.
name|remaining
argument_list|()
operator|>=
literal|4
condition|)
block|{
name|putInt
argument_list|(
name|buffer
operator|.
name|getInt
argument_list|()
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|buffer
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
name|putByte
argument_list|(
name|buffer
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|order
argument_list|(
name|bo
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|putInt (int i)
specifier|public
name|Hasher
name|putInt
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|update
argument_list|(
literal|4
argument_list|,
name|i
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|putLong (long l)
specifier|public
name|Hasher
name|putLong
parameter_list|(
name|long
name|l
parameter_list|)
block|{
name|update
argument_list|(
literal|4
argument_list|,
operator|(
name|int
operator|)
name|l
argument_list|)
expr_stmt|;
name|update
argument_list|(
literal|4
argument_list|,
name|l
operator|>>>
literal|32
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|putChar (char c)
specifier|public
name|Hasher
name|putChar
parameter_list|(
name|char
name|c
parameter_list|)
block|{
name|update
argument_list|(
literal|2
argument_list|,
name|c
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
comment|// need to use Charsets for Android tests to pass
annotation|@
name|Override
DECL|method|putString (CharSequence input, Charset charset)
specifier|public
name|Hasher
name|putString
parameter_list|(
name|CharSequence
name|input
parameter_list|,
name|Charset
name|charset
parameter_list|)
block|{
if|if
condition|(
name|Charsets
operator|.
name|UTF_8
operator|.
name|equals
argument_list|(
name|charset
argument_list|)
condition|)
block|{
name|int
name|utf16Length
init|=
name|input
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
comment|// This loop optimizes for pure ASCII.
while|while
condition|(
name|i
operator|+
literal|4
operator|<=
name|utf16Length
condition|)
block|{
name|char
name|c0
init|=
name|input
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|char
name|c1
init|=
name|input
operator|.
name|charAt
argument_list|(
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
name|char
name|c2
init|=
name|input
operator|.
name|charAt
argument_list|(
name|i
operator|+
literal|2
argument_list|)
decl_stmt|;
name|char
name|c3
init|=
name|input
operator|.
name|charAt
argument_list|(
name|i
operator|+
literal|3
argument_list|)
decl_stmt|;
if|if
condition|(
name|c0
operator|<
literal|0x80
operator|&&
name|c1
operator|<
literal|0x80
operator|&&
name|c2
operator|<
literal|0x80
operator|&&
name|c3
operator|<
literal|0x80
condition|)
block|{
name|update
argument_list|(
literal|4
argument_list|,
name|c0
operator||
operator|(
name|c1
operator|<<
literal|8
operator|)
operator||
operator|(
name|c2
operator|<<
literal|16
operator|)
operator||
operator|(
name|c3
operator|<<
literal|24
operator|)
argument_list|)
expr_stmt|;
name|i
operator|+=
literal|4
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
for|for
control|(
init|;
name|i
operator|<
name|utf16Length
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|input
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|<
literal|0x80
condition|)
block|{
name|update
argument_list|(
literal|1
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|<
literal|0x800
condition|)
block|{
name|update
argument_list|(
literal|2
argument_list|,
name|charToTwoUtf8Bytes
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
argument_list|<
name|Character
operator|.
name|MIN_SURROGATE
operator|||
name|c
argument_list|>
name|Character
operator|.
name|MAX_SURROGATE
condition|)
block|{
name|update
argument_list|(
literal|3
argument_list|,
name|charToThreeUtf8Bytes
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|codePoint
init|=
name|Character
operator|.
name|codePointAt
argument_list|(
name|input
argument_list|,
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|codePoint
operator|==
name|c
condition|)
block|{
comment|// fall back to JDK getBytes instead of trying to handle invalid surrogates ourselves
name|putBytes
argument_list|(
name|input
operator|.
name|subSequence
argument_list|(
name|i
argument_list|,
name|utf16Length
argument_list|)
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|charset
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
name|i
operator|++
expr_stmt|;
name|update
argument_list|(
literal|4
argument_list|,
name|codePointToFourUtf8Bytes
argument_list|(
name|codePoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|this
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|putString
argument_list|(
name|input
argument_list|,
name|charset
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|hash ()
specifier|public
name|HashCode
name|hash
parameter_list|()
block|{
name|checkState
argument_list|(
operator|!
name|isDone
argument_list|)
expr_stmt|;
name|isDone
operator|=
literal|true
expr_stmt|;
name|h1
operator|^=
name|mixK1
argument_list|(
operator|(
name|int
operator|)
name|buffer
argument_list|)
expr_stmt|;
return|return
name|fmix
argument_list|(
name|h1
argument_list|,
name|length
argument_list|)
return|;
block|}
block|}
DECL|method|codePointToFourUtf8Bytes (int codePoint)
specifier|private
specifier|static
name|long
name|codePointToFourUtf8Bytes
parameter_list|(
name|int
name|codePoint
parameter_list|)
block|{
comment|// codePoint has at most 21 bits
return|return
operator|(
operator|(
literal|0xFL
operator|<<
literal|4
operator|)
operator||
operator|(
name|codePoint
operator|>>>
literal|18
operator|)
operator|)
operator||
operator|(
operator|(
literal|0x80L
operator||
operator|(
literal|0x3F
operator|&
operator|(
name|codePoint
operator|>>>
literal|12
operator|)
operator|)
operator|)
operator|<<
literal|8
operator|)
operator||
operator|(
operator|(
literal|0x80L
operator||
operator|(
literal|0x3F
operator|&
operator|(
name|codePoint
operator|>>>
literal|6
operator|)
operator|)
operator|)
operator|<<
literal|16
operator|)
operator||
operator|(
operator|(
literal|0x80L
operator||
operator|(
literal|0x3F
operator|&
name|codePoint
operator|)
operator|)
operator|<<
literal|24
operator|)
return|;
block|}
DECL|method|charToThreeUtf8Bytes (char c)
specifier|private
specifier|static
name|long
name|charToThreeUtf8Bytes
parameter_list|(
name|char
name|c
parameter_list|)
block|{
return|return
operator|(
operator|(
literal|0x7L
operator|<<
literal|5
operator|)
operator||
operator|(
name|c
operator|>>>
literal|12
operator|)
operator|)
operator||
operator|(
operator|(
literal|0x80
operator||
operator|(
literal|0x3F
operator|&
operator|(
name|c
operator|>>>
literal|6
operator|)
operator|)
operator|)
operator|<<
literal|8
operator|)
operator||
operator|(
operator|(
literal|0x80
operator||
operator|(
literal|0x3F
operator|&
name|c
operator|)
operator|)
operator|<<
literal|16
operator|)
return|;
block|}
DECL|method|charToTwoUtf8Bytes (char c)
specifier|private
specifier|static
name|long
name|charToTwoUtf8Bytes
parameter_list|(
name|char
name|c
parameter_list|)
block|{
comment|// c has at most 11 bits
return|return
operator|(
operator|(
literal|0x3L
operator|<<
literal|6
operator|)
operator||
operator|(
name|c
operator|>>>
literal|6
operator|)
operator|)
operator||
operator|(
operator|(
literal|0x80
operator||
operator|(
literal|0x3F
operator|&
name|c
operator|)
operator|)
operator|<<
literal|8
operator|)
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0L
decl_stmt|;
block|}
end_class

end_unit

