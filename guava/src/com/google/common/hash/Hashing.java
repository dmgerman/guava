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
name|annotations
operator|.
name|VisibleForTesting
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
name|Supplier
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
name|security
operator|.
name|MessageDigest
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|Adler32
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|CRC32
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|Checksum
import|;
end_import

begin_comment
comment|/**  * Static methods to obtain {@link HashFunction} instances, and other static hashing-related  * utilities.  *  * @author Kevin Bourrillion  * @author Dimitris Andreou  * @author Kurt Alfred Kluever  * @since 11.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|Hashing
specifier|public
specifier|final
class|class
name|Hashing
block|{
DECL|method|Hashing ()
specifier|private
name|Hashing
parameter_list|()
block|{}
comment|/**    * Used to randomize {@link #goodFastHash} instances, so that programs which persist anything    * dependent on hashcodes of those, will fail sooner than later.    */
DECL|field|GOOD_FAST_HASH_SEED
specifier|private
specifier|static
specifier|final
name|int
name|GOOD_FAST_HASH_SEED
init|=
operator|(
name|int
operator|)
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
comment|// Used by goodFastHash when minimumBits == 32.
DECL|field|GOOD_FAST_HASH_FUNCTION_32
specifier|private
specifier|static
specifier|final
name|HashFunction
name|GOOD_FAST_HASH_FUNCTION_32
init|=
name|murmur3_32
argument_list|(
name|GOOD_FAST_HASH_SEED
argument_list|)
decl_stmt|;
comment|// Used by goodFastHash when 32< minimumBits<= 128.
DECL|field|GOOD_FAST_HASH_FUNCTION_128
specifier|private
specifier|static
specifier|final
name|HashFunction
name|GOOD_FAST_HASH_FUNCTION_128
init|=
name|murmur3_128
argument_list|(
name|GOOD_FAST_HASH_SEED
argument_list|)
decl_stmt|;
comment|/**    * Returns a general-purpose,<b>non-cryptographic-strength</b>, streaming hash function that    * produces hash codes of length at least {@code minimumBits}. Users without specific    * compatibility requirements and who do not persist the hash codes are encouraged to    * choose this hash function.    *    *<p>Repeated calls to {@link #goodFastHash} with the same {@code minimumBits} value will    * return {@link HashFunction} instances with identical behavior (but not necessarily the    * same instance) for the duration of the current virtual machine.    *    *<p><b>Warning: the implementation is unspecified and is subject to change.</b>    *    * @throws IllegalArgumentException if {@code minimumBits} is not positive    */
DECL|method|goodFastHash (int minimumBits)
specifier|public
specifier|static
name|HashFunction
name|goodFastHash
parameter_list|(
name|int
name|minimumBits
parameter_list|)
block|{
name|int
name|bits
init|=
name|checkPositiveAndMakeMultipleOf32
argument_list|(
name|minimumBits
argument_list|)
decl_stmt|;
if|if
condition|(
name|bits
operator|==
literal|32
condition|)
block|{
return|return
name|GOOD_FAST_HASH_FUNCTION_32
return|;
block|}
if|if
condition|(
name|bits
operator|<=
literal|128
condition|)
block|{
return|return
name|GOOD_FAST_HASH_FUNCTION_128
return|;
block|}
comment|// Otherwise, join together some 128-bit murmur3s
name|int
name|hashFunctionsNeeded
init|=
operator|(
name|bits
operator|+
literal|127
operator|)
operator|/
literal|128
decl_stmt|;
name|HashFunction
index|[]
name|hashFunctions
init|=
operator|new
name|HashFunction
index|[
name|hashFunctionsNeeded
index|]
decl_stmt|;
name|hashFunctions
index|[
literal|0
index|]
operator|=
name|GOOD_FAST_HASH_FUNCTION_128
expr_stmt|;
name|int
name|seed
init|=
name|GOOD_FAST_HASH_SEED
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
name|hashFunctionsNeeded
condition|;
name|i
operator|++
control|)
block|{
name|seed
operator|+=
literal|1500450271
expr_stmt|;
comment|// a prime; shouldn't matter
name|hashFunctions
index|[
name|i
index|]
operator|=
name|murmur3_128
argument_list|(
name|seed
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ConcatenatedHashFunction
argument_list|(
name|hashFunctions
argument_list|)
return|;
block|}
comment|/**    * Returns a hash function implementing the    *<a href="http://smhasher.googlecode.com/svn/trunk/MurmurHash3.cpp">    * 32-bit murmur3 algorithm, x86 variant</a> (little-endian variant),    * using the given seed value.    *    *<p>The exact C++ equivalent is the MurmurHash3_x86_32 function (Murmur3A).    */
DECL|method|murmur3_32 (int seed)
specifier|public
specifier|static
name|HashFunction
name|murmur3_32
parameter_list|(
name|int
name|seed
parameter_list|)
block|{
return|return
operator|new
name|Murmur3_32HashFunction
argument_list|(
name|seed
argument_list|)
return|;
block|}
comment|/**    * Returns a hash function implementing the    *<a href="http://smhasher.googlecode.com/svn/trunk/MurmurHash3.cpp">    * 32-bit murmur3 algorithm, x86 variant</a> (little-endian variant),    * using a seed value of zero.    *    *<p>The exact C++ equivalent is the MurmurHash3_x86_32 function (Murmur3A).    */
DECL|method|murmur3_32 ()
specifier|public
specifier|static
name|HashFunction
name|murmur3_32
parameter_list|()
block|{
return|return
name|MURMUR3_32
return|;
block|}
DECL|field|MURMUR3_32
specifier|private
specifier|static
specifier|final
name|Murmur3_32HashFunction
name|MURMUR3_32
init|=
operator|new
name|Murmur3_32HashFunction
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|/**    * Returns a hash function implementing the    *<a href="http://smhasher.googlecode.com/svn/trunk/MurmurHash3.cpp">    * 128-bit murmur3 algorithm, x64 variant</a> (little-endian variant),    * using the given seed value.    *    *<p>The exact C++ equivalent is the MurmurHash3_x64_128 function (Murmur3F).    */
DECL|method|murmur3_128 (int seed)
specifier|public
specifier|static
name|HashFunction
name|murmur3_128
parameter_list|(
name|int
name|seed
parameter_list|)
block|{
return|return
operator|new
name|Murmur3_128HashFunction
argument_list|(
name|seed
argument_list|)
return|;
block|}
comment|/**    * Returns a hash function implementing the    *<a href="http://smhasher.googlecode.com/svn/trunk/MurmurHash3.cpp">    * 128-bit murmur3 algorithm, x64 variant</a> (little-endian variant),    * using a seed value of zero.    *    *<p>The exact C++ equivalent is the MurmurHash3_x64_128 function (Murmur3F).    */
DECL|method|murmur3_128 ()
specifier|public
specifier|static
name|HashFunction
name|murmur3_128
parameter_list|()
block|{
return|return
name|MURMUR3_128
return|;
block|}
DECL|field|MURMUR3_128
specifier|private
specifier|static
specifier|final
name|Murmur3_128HashFunction
name|MURMUR3_128
init|=
operator|new
name|Murmur3_128HashFunction
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|/**    * Returns a hash function implementing the MD5 hash algorithm (128 hash bits) by delegating to    * the MD5 {@link MessageDigest}.    */
DECL|method|md5 ()
specifier|public
specifier|static
name|HashFunction
name|md5
parameter_list|()
block|{
return|return
name|MD5
return|;
block|}
DECL|field|MD5
specifier|private
specifier|static
specifier|final
name|HashFunction
name|MD5
init|=
operator|new
name|MessageDigestHashFunction
argument_list|(
literal|"MD5"
argument_list|,
literal|"Hashing.md5()"
argument_list|)
decl_stmt|;
comment|/**    * Returns a hash function implementing the SHA-1 algorithm (160 hash bits) by delegating to the    * SHA-1 {@link MessageDigest}.    */
DECL|method|sha1 ()
specifier|public
specifier|static
name|HashFunction
name|sha1
parameter_list|()
block|{
return|return
name|SHA_1
return|;
block|}
DECL|field|SHA_1
specifier|private
specifier|static
specifier|final
name|HashFunction
name|SHA_1
init|=
operator|new
name|MessageDigestHashFunction
argument_list|(
literal|"SHA-1"
argument_list|,
literal|"Hashing.sha1()"
argument_list|)
decl_stmt|;
comment|/**    * Returns a hash function implementing the SHA-256 algorithm (256 hash bits) by delegating to    * the SHA-256 {@link MessageDigest}.    */
DECL|method|sha256 ()
specifier|public
specifier|static
name|HashFunction
name|sha256
parameter_list|()
block|{
return|return
name|SHA_256
return|;
block|}
DECL|field|SHA_256
specifier|private
specifier|static
specifier|final
name|HashFunction
name|SHA_256
init|=
operator|new
name|MessageDigestHashFunction
argument_list|(
literal|"SHA-256"
argument_list|,
literal|"Hashing.sha256()"
argument_list|)
decl_stmt|;
comment|/**    * Returns a hash function implementing the SHA-512 algorithm (512 hash bits) by delegating to the    * SHA-512 {@link MessageDigest}.    */
DECL|method|sha512 ()
specifier|public
specifier|static
name|HashFunction
name|sha512
parameter_list|()
block|{
return|return
name|SHA_512
return|;
block|}
DECL|field|SHA_512
specifier|private
specifier|static
specifier|final
name|HashFunction
name|SHA_512
init|=
operator|new
name|MessageDigestHashFunction
argument_list|(
literal|"SHA-512"
argument_list|,
literal|"Hashing.sha512()"
argument_list|)
decl_stmt|;
comment|/**    * Returns a hash function implementing the CRC-32 checksum algorithm (32 hash bits) by delegating    * to the {@link CRC32} {@link Checksum}.    *    *<p>To get the {@code long} value equivalent to {@link Checksum#getValue()} for a    * {@code HashCode} produced by this function, use {@link HashCode#padToLong()}.    *    * @since 14.0    */
DECL|method|crc32 ()
specifier|public
specifier|static
name|HashFunction
name|crc32
parameter_list|()
block|{
return|return
name|CRC_32
return|;
block|}
DECL|field|CRC_32
specifier|private
specifier|static
specifier|final
name|HashFunction
name|CRC_32
init|=
name|checksumHashFunction
argument_list|(
name|ChecksumType
operator|.
name|CRC_32
argument_list|,
literal|"Hashing.crc32()"
argument_list|)
decl_stmt|;
comment|/**    * Returns a hash function implementing the Adler-32 checksum algorithm (32 hash bits) by    * delegating to the {@link Adler32} {@link Checksum}.    *    *<p>To get the {@code long} value equivalent to {@link Checksum#getValue()} for a    * {@code HashCode} produced by this function, use {@link HashCode#padToLong()}.    *    * @since 14.0    */
DECL|method|adler32 ()
specifier|public
specifier|static
name|HashFunction
name|adler32
parameter_list|()
block|{
return|return
name|ADLER_32
return|;
block|}
DECL|field|ADLER_32
specifier|private
specifier|static
specifier|final
name|HashFunction
name|ADLER_32
init|=
name|checksumHashFunction
argument_list|(
name|ChecksumType
operator|.
name|ADLER_32
argument_list|,
literal|"Hashing.adler32()"
argument_list|)
decl_stmt|;
DECL|method|checksumHashFunction (ChecksumType type, String toString)
specifier|private
specifier|static
name|HashFunction
name|checksumHashFunction
parameter_list|(
name|ChecksumType
name|type
parameter_list|,
name|String
name|toString
parameter_list|)
block|{
return|return
operator|new
name|ChecksumHashFunction
argument_list|(
name|type
argument_list|,
name|type
operator|.
name|bits
argument_list|,
name|toString
argument_list|)
return|;
block|}
DECL|enum|ChecksumType
enum|enum
name|ChecksumType
implements|implements
name|Supplier
argument_list|<
name|Checksum
argument_list|>
block|{
DECL|enumConstant|CRC_32
name|CRC_32
argument_list|(
literal|32
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Checksum
name|get
parameter_list|()
block|{
return|return
operator|new
name|CRC32
argument_list|()
return|;
block|}
block|}
block|,
DECL|enumConstant|ADLER_32
name|ADLER_32
argument_list|(
literal|32
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Checksum
name|get
parameter_list|()
block|{
return|return
operator|new
name|Adler32
argument_list|()
return|;
block|}
block|}
block|;
DECL|field|bits
specifier|private
specifier|final
name|int
name|bits
decl_stmt|;
DECL|method|ChecksumType (int bits)
name|ChecksumType
parameter_list|(
name|int
name|bits
parameter_list|)
block|{
name|this
operator|.
name|bits
operator|=
name|bits
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
specifier|abstract
name|Checksum
name|get
parameter_list|()
function_decl|;
block|}
comment|// Lazy initialization holder class idiom.
comment|/**    * If {@code hashCode} has enough bits, returns {@code hashCode.asLong()}, otherwise    * returns a {@code long} value with {@code hashCode.asInt()} as the least-significant    * four bytes and {@code 0x00} as each of the most-significant four bytes.    *    * @deprecated Use {@code HashCode.padToLong()} instead. This method is scheduled to be    *     removed in Guava 15.0.    */
annotation|@
name|Deprecated
DECL|method|padToLong (HashCode hashCode)
specifier|public
specifier|static
name|long
name|padToLong
parameter_list|(
name|HashCode
name|hashCode
parameter_list|)
block|{
return|return
name|hashCode
operator|.
name|padToLong
argument_list|()
return|;
block|}
comment|/**    * Assigns to {@code hashCode} a "bucket" in the range {@code [0, buckets)}, in a uniform    * manner that minimizes the need for remapping as {@code buckets} grows. That is,    * {@code consistentHash(h, n)} equals:    *    *<ul>    *<li>{@code n - 1}, with approximate probability {@code 1/n}    *<li>{@code consistentHash(h, n - 1)}, otherwise (probability {@code 1 - 1/n})    *</ul>    *    *<p>See the<a href="http://en.wikipedia.org/wiki/Consistent_hashing">wikipedia    * article on consistent hashing</a> for more information.    *<p>    * If you might want to have weights for the buckets in the future, take a look at    * {@code weightedConsistentHash}.    */
DECL|method|consistentHash (HashCode hashCode, int buckets)
specifier|public
specifier|static
name|int
name|consistentHash
parameter_list|(
name|HashCode
name|hashCode
parameter_list|,
name|int
name|buckets
parameter_list|)
block|{
return|return
name|consistentHash
argument_list|(
name|hashCode
operator|.
name|padToLong
argument_list|()
argument_list|,
name|buckets
argument_list|)
return|;
block|}
comment|/**    * Assigns to {@code input} a "bucket" in the range {@code [0, buckets)}, in a uniform    * manner that minimizes the need for remapping as {@code buckets} grows. That is,    * {@code consistentHash(h, n)} equals:    *    *<ul>    *<li>{@code n - 1}, with approximate probability {@code 1/n}    *<li>{@code consistentHash(h, n - 1)}, otherwise (probability {@code 1 - 1/n})    *</ul>    *    *<p>See the<a href="http://en.wikipedia.org/wiki/Consistent_hashing">wikipedia    * article on consistent hashing</a> for more information.    *<p>    * If you might want to have weights for the buckets in the future, take a look at    * {@code weightedConsistentHash}.    */
DECL|method|consistentHash (long input, int buckets)
specifier|public
specifier|static
name|int
name|consistentHash
parameter_list|(
name|long
name|input
parameter_list|,
name|int
name|buckets
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|buckets
operator|>
literal|0
argument_list|,
literal|"buckets must be positive: %s"
argument_list|,
name|buckets
argument_list|)
expr_stmt|;
name|LinearCongruentialGenerator
name|generator
init|=
operator|new
name|LinearCongruentialGenerator
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|int
name|candidate
init|=
literal|0
decl_stmt|;
name|int
name|next
decl_stmt|;
comment|// Jump from bucket to bucket until we go out of range
while|while
condition|(
literal|true
condition|)
block|{
name|next
operator|=
call|(
name|int
call|)
argument_list|(
operator|(
name|candidate
operator|+
literal|1
operator|)
operator|/
name|generator
operator|.
name|nextDouble
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|next
operator|>=
literal|0
operator|&&
name|next
operator|<
name|buckets
condition|)
block|{
name|candidate
operator|=
name|next
expr_stmt|;
block|}
else|else
block|{
return|return
name|candidate
return|;
block|}
block|}
block|}
comment|/**    * Returns a hash code, having the same bit length as each of the input hash codes,    * that combines the information of these hash codes in an ordered fashion. That    * is, whenever two equal hash codes are produced by two calls to this method, it    * is<i>as likely as possible</i> that each was computed from the<i>same</i>    * input hash codes in the<i>same</i> order.    *    * @throws IllegalArgumentException if {@code hashCodes} is empty, or the hash codes    *     do not all have the same bit length    */
DECL|method|combineOrdered (Iterable<HashCode> hashCodes)
specifier|public
specifier|static
name|HashCode
name|combineOrdered
parameter_list|(
name|Iterable
argument_list|<
name|HashCode
argument_list|>
name|hashCodes
parameter_list|)
block|{
name|Iterator
argument_list|<
name|HashCode
argument_list|>
name|iterator
init|=
name|hashCodes
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|iterator
operator|.
name|hasNext
argument_list|()
argument_list|,
literal|"Must be at least 1 hash code to combine."
argument_list|)
expr_stmt|;
name|int
name|bits
init|=
name|iterator
operator|.
name|next
argument_list|()
operator|.
name|bits
argument_list|()
decl_stmt|;
name|byte
index|[]
name|resultBytes
init|=
operator|new
name|byte
index|[
name|bits
operator|/
literal|8
index|]
decl_stmt|;
for|for
control|(
name|HashCode
name|hashCode
range|:
name|hashCodes
control|)
block|{
name|byte
index|[]
name|nextBytes
init|=
name|hashCode
operator|.
name|asBytes
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|nextBytes
operator|.
name|length
operator|==
name|resultBytes
operator|.
name|length
argument_list|,
literal|"All hashcodes must have the same bit length."
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nextBytes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|resultBytes
index|[
name|i
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|resultBytes
index|[
name|i
index|]
operator|*
literal|37
operator|^
name|nextBytes
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|HashCodes
operator|.
name|fromBytesNoCopy
argument_list|(
name|resultBytes
argument_list|)
return|;
block|}
comment|/**    * Returns a hash code, having the same bit length as each of the input hash codes,    * that combines the information of these hash codes in an unordered fashion. That    * is, whenever two equal hash codes are produced by two calls to this method, it    * is<i>as likely as possible</i> that each was computed from the<i>same</i>    * input hash codes in<i>some</i> order.    *    * @throws IllegalArgumentException if {@code hashCodes} is empty, or the hash codes    *     do not all have the same bit length    */
DECL|method|combineUnordered (Iterable<HashCode> hashCodes)
specifier|public
specifier|static
name|HashCode
name|combineUnordered
parameter_list|(
name|Iterable
argument_list|<
name|HashCode
argument_list|>
name|hashCodes
parameter_list|)
block|{
name|Iterator
argument_list|<
name|HashCode
argument_list|>
name|iterator
init|=
name|hashCodes
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|iterator
operator|.
name|hasNext
argument_list|()
argument_list|,
literal|"Must be at least 1 hash code to combine."
argument_list|)
expr_stmt|;
name|byte
index|[]
name|resultBytes
init|=
operator|new
name|byte
index|[
name|iterator
operator|.
name|next
argument_list|()
operator|.
name|bits
argument_list|()
operator|/
literal|8
index|]
decl_stmt|;
for|for
control|(
name|HashCode
name|hashCode
range|:
name|hashCodes
control|)
block|{
name|byte
index|[]
name|nextBytes
init|=
name|hashCode
operator|.
name|asBytes
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|nextBytes
operator|.
name|length
operator|==
name|resultBytes
operator|.
name|length
argument_list|,
literal|"All hashcodes must have the same bit length."
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nextBytes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|resultBytes
index|[
name|i
index|]
operator|+=
name|nextBytes
index|[
name|i
index|]
expr_stmt|;
block|}
block|}
return|return
name|HashCodes
operator|.
name|fromBytesNoCopy
argument_list|(
name|resultBytes
argument_list|)
return|;
block|}
comment|/**    * Checks that the passed argument is positive, and ceils it to a multiple of 32.    */
DECL|method|checkPositiveAndMakeMultipleOf32 (int bits)
specifier|static
name|int
name|checkPositiveAndMakeMultipleOf32
parameter_list|(
name|int
name|bits
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|bits
operator|>
literal|0
argument_list|,
literal|"Number of bits must be positive"
argument_list|)
expr_stmt|;
return|return
operator|(
name|bits
operator|+
literal|31
operator|)
operator|&
operator|~
literal|31
return|;
block|}
comment|// TODO(kevinb): Maybe expose this class via a static Hashing method?
annotation|@
name|VisibleForTesting
DECL|class|ConcatenatedHashFunction
specifier|static
specifier|final
class|class
name|ConcatenatedHashFunction
extends|extends
name|AbstractCompositeHashFunction
block|{
DECL|field|bits
specifier|private
specifier|final
name|int
name|bits
decl_stmt|;
DECL|method|ConcatenatedHashFunction (HashFunction... functions)
name|ConcatenatedHashFunction
parameter_list|(
name|HashFunction
modifier|...
name|functions
parameter_list|)
block|{
name|super
argument_list|(
name|functions
argument_list|)
expr_stmt|;
name|int
name|bitSum
init|=
literal|0
decl_stmt|;
for|for
control|(
name|HashFunction
name|function
range|:
name|functions
control|)
block|{
name|bitSum
operator|+=
name|function
operator|.
name|bits
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|bits
operator|=
name|bitSum
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|makeHash (Hasher[] hashers)
name|HashCode
name|makeHash
parameter_list|(
name|Hasher
index|[]
name|hashers
parameter_list|)
block|{
comment|// TODO(user): Get rid of the ByteBuffer here?
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
name|bits
operator|/
literal|8
index|]
decl_stmt|;
name|ByteBuffer
name|buffer
init|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
for|for
control|(
name|Hasher
name|hasher
range|:
name|hashers
control|)
block|{
name|buffer
operator|.
name|put
argument_list|(
name|hasher
operator|.
name|hash
argument_list|()
operator|.
name|asBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|HashCodes
operator|.
name|fromBytesNoCopy
argument_list|(
name|bytes
argument_list|)
return|;
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
name|bits
return|;
block|}
block|}
DECL|class|LinearCongruentialGenerator
specifier|private
specifier|static
specifier|final
class|class
name|LinearCongruentialGenerator
block|{
DECL|field|state
specifier|private
name|long
name|state
decl_stmt|;
DECL|method|LinearCongruentialGenerator (long seed)
specifier|public
name|LinearCongruentialGenerator
parameter_list|(
name|long
name|seed
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|seed
expr_stmt|;
block|}
DECL|method|nextDouble ()
specifier|public
name|double
name|nextDouble
parameter_list|()
block|{
name|state
operator|=
literal|2862933555777941757L
operator|*
name|state
operator|+
literal|1
expr_stmt|;
return|return
operator|(
call|(
name|double
call|)
argument_list|(
call|(
name|int
call|)
argument_list|(
name|state
operator|>>>
literal|33
argument_list|)
operator|+
literal|1
argument_list|)
operator|)
operator|/
operator|(
literal|0x1
literal|.0p31
operator|)
return|;
block|}
block|}
block|}
end_class

end_unit

