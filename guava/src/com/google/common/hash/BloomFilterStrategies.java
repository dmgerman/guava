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
name|math
operator|.
name|LongMath
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
name|java
operator|.
name|math
operator|.
name|RoundingMode
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_comment
comment|/**  * Collections of strategies of generating the k * log(M) bits required for an element to  * be mapped to a BloomFilter of M bits and k hash functions. These  * strategies are part of the serialized form of the Bloom filters that use them, thus they must be  * preserved as is (no updates allowed, only introduction of new versions).  *  * Important: the order of the constants cannot change, and they cannot be deleted - we depend  * on their ordinal for BloomFilter serialization.  *  * @author Dimitris Andreou  * @author Kurt Alfred Kluever  */
end_comment

begin_enum
DECL|enum|BloomFilterStrategies
enum|enum
name|BloomFilterStrategies
implements|implements
name|BloomFilter
operator|.
name|Strategy
block|{
comment|/**    * See "Less Hashing, Same Performance: Building a Better Bloom Filter" by Adam Kirsch and    * Michael Mitzenmacher. The paper argues that this trick doesn't significantly deteriorate the    * performance of a Bloom filter (yet only needs two 32bit hash functions).    */
DECL|enumConstant|MURMUR128_MITZ_32
DECL|method|MURMUR128_MITZ_32 ()
name|MURMUR128_MITZ_32
parameter_list|()
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|boolean
name|put
parameter_list|(
name|T
name|object
parameter_list|,
name|Funnel
argument_list|<
name|?
super|super
name|T
argument_list|>
name|funnel
parameter_list|,
name|int
name|numHashFunctions
parameter_list|,
name|BitArray
name|bits
parameter_list|)
block|{
name|long
name|bitSize
init|=
name|bits
operator|.
name|bitSize
argument_list|()
decl_stmt|;
name|long
name|hash64
init|=
name|Hashing
operator|.
name|murmur3_128
argument_list|()
operator|.
name|hashObject
argument_list|(
name|object
argument_list|,
name|funnel
argument_list|)
operator|.
name|asLong
argument_list|()
decl_stmt|;
name|int
name|hash1
init|=
operator|(
name|int
operator|)
name|hash64
decl_stmt|;
name|int
name|hash2
init|=
call|(
name|int
call|)
argument_list|(
name|hash64
operator|>>>
literal|32
argument_list|)
decl_stmt|;
name|boolean
name|bitsChanged
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|numHashFunctions
condition|;
name|i
operator|++
control|)
block|{
name|int
name|combinedHash
init|=
name|hash1
operator|+
operator|(
name|i
operator|*
name|hash2
operator|)
decl_stmt|;
comment|// Flip all the bits if it's negative (guaranteed positive number)
if|if
condition|(
name|combinedHash
operator|<
literal|0
condition|)
block|{
name|combinedHash
operator|=
operator|~
name|combinedHash
expr_stmt|;
block|}
name|bitsChanged
operator||=
name|bits
operator|.
name|set
argument_list|(
name|combinedHash
operator|%
name|bitSize
argument_list|)
expr_stmt|;
block|}
return|return
name|bitsChanged
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|boolean
name|mightContain
parameter_list|(
name|T
name|object
parameter_list|,
name|Funnel
argument_list|<
name|?
super|super
name|T
argument_list|>
name|funnel
parameter_list|,
name|int
name|numHashFunctions
parameter_list|,
name|BitArray
name|bits
parameter_list|)
block|{
name|long
name|bitSize
init|=
name|bits
operator|.
name|bitSize
argument_list|()
decl_stmt|;
name|long
name|hash64
init|=
name|Hashing
operator|.
name|murmur3_128
argument_list|()
operator|.
name|hashObject
argument_list|(
name|object
argument_list|,
name|funnel
argument_list|)
operator|.
name|asLong
argument_list|()
decl_stmt|;
name|int
name|hash1
init|=
operator|(
name|int
operator|)
name|hash64
decl_stmt|;
name|int
name|hash2
init|=
call|(
name|int
call|)
argument_list|(
name|hash64
operator|>>>
literal|32
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|numHashFunctions
condition|;
name|i
operator|++
control|)
block|{
name|int
name|combinedHash
init|=
name|hash1
operator|+
operator|(
name|i
operator|*
name|hash2
operator|)
decl_stmt|;
comment|// Flip all the bits if it's negative (guaranteed positive number)
if|if
condition|(
name|combinedHash
operator|<
literal|0
condition|)
block|{
name|combinedHash
operator|=
operator|~
name|combinedHash
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|bits
operator|.
name|get
argument_list|(
name|combinedHash
operator|%
name|bitSize
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
block|,
comment|/**    * This strategy uses all 128 bits of {@link Hashing#murmur3_128} when hashing. It looks    * different than the implementation in MURMUR128_MITZ_32 because we're avoiding the    * multiplication in the loop and doing a (much simpler) += hash2. We're also changing the    * index to a positive number by AND'ing with Long.MAX_VALUE instead of flipping the bits.    */
DECL|enumConstant|MURMUR128_MITZ_64
DECL|method|MURMUR128_MITZ_64 ()
name|MURMUR128_MITZ_64
parameter_list|()
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|boolean
name|put
parameter_list|(
name|T
name|object
parameter_list|,
name|Funnel
argument_list|<
name|?
super|super
name|T
argument_list|>
name|funnel
parameter_list|,
name|int
name|numHashFunctions
parameter_list|,
name|BitArray
name|bits
parameter_list|)
block|{
name|long
name|bitSize
init|=
name|bits
operator|.
name|bitSize
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|Hashing
operator|.
name|murmur3_128
argument_list|()
operator|.
name|hashObject
argument_list|(
name|object
argument_list|,
name|funnel
argument_list|)
operator|.
name|getBytesInternal
argument_list|()
decl_stmt|;
name|long
name|hash1
init|=
name|lowerEight
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|long
name|hash2
init|=
name|upperEight
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|boolean
name|bitsChanged
init|=
literal|false
decl_stmt|;
name|long
name|combinedHash
init|=
name|hash1
operator|+
name|hash2
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
name|numHashFunctions
condition|;
name|i
operator|++
control|)
block|{
comment|// Make the combined hash positive and indexable
name|bitsChanged
operator||=
name|bits
operator|.
name|set
argument_list|(
operator|(
name|combinedHash
operator|&
name|Long
operator|.
name|MAX_VALUE
operator|)
operator|%
name|bitSize
argument_list|)
expr_stmt|;
name|combinedHash
operator|+=
name|hash2
expr_stmt|;
block|}
return|return
name|bitsChanged
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|boolean
name|mightContain
parameter_list|(
name|T
name|object
parameter_list|,
name|Funnel
argument_list|<
name|?
super|super
name|T
argument_list|>
name|funnel
parameter_list|,
name|int
name|numHashFunctions
parameter_list|,
name|BitArray
name|bits
parameter_list|)
block|{
name|long
name|bitSize
init|=
name|bits
operator|.
name|bitSize
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|Hashing
operator|.
name|murmur3_128
argument_list|()
operator|.
name|hashObject
argument_list|(
name|object
argument_list|,
name|funnel
argument_list|)
operator|.
name|getBytesInternal
argument_list|()
decl_stmt|;
name|long
name|hash1
init|=
name|lowerEight
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|long
name|hash2
init|=
name|upperEight
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|long
name|combinedHash
init|=
name|hash1
operator|+
name|hash2
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
name|numHashFunctions
condition|;
name|i
operator|++
control|)
block|{
comment|// Make the combined hash positive and indexable
if|if
condition|(
operator|!
name|bits
operator|.
name|get
argument_list|(
operator|(
name|combinedHash
operator|&
name|Long
operator|.
name|MAX_VALUE
operator|)
operator|%
name|bitSize
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|combinedHash
operator|+=
name|hash2
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
comment|/* static */
name|long
name|lowerEight
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|Longs
operator|.
name|fromBytes
argument_list|(
name|bytes
index|[
literal|7
index|]
argument_list|,
name|bytes
index|[
literal|6
index|]
argument_list|,
name|bytes
index|[
literal|5
index|]
argument_list|,
name|bytes
index|[
literal|4
index|]
argument_list|,
name|bytes
index|[
literal|3
index|]
argument_list|,
name|bytes
index|[
literal|2
index|]
argument_list|,
name|bytes
index|[
literal|1
index|]
argument_list|,
name|bytes
index|[
literal|0
index|]
argument_list|)
return|;
block|}
specifier|private
comment|/* static */
name|long
name|upperEight
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|Longs
operator|.
name|fromBytes
argument_list|(
name|bytes
index|[
literal|15
index|]
argument_list|,
name|bytes
index|[
literal|14
index|]
argument_list|,
name|bytes
index|[
literal|13
index|]
argument_list|,
name|bytes
index|[
literal|12
index|]
argument_list|,
name|bytes
index|[
literal|11
index|]
argument_list|,
name|bytes
index|[
literal|10
index|]
argument_list|,
name|bytes
index|[
literal|9
index|]
argument_list|,
name|bytes
index|[
literal|8
index|]
argument_list|)
return|;
block|}
block|}
block|;
comment|// Note: We use this instead of java.util.BitSet because we need access to the long[] data field
DECL|class|BitArray
specifier|static
specifier|final
class|class
name|BitArray
block|{
DECL|field|data
specifier|final
name|long
index|[]
name|data
decl_stmt|;
DECL|field|bitCount
name|long
name|bitCount
decl_stmt|;
DECL|method|BitArray (long bits)
name|BitArray
parameter_list|(
name|long
name|bits
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|long
index|[
name|Ints
operator|.
name|checkedCast
argument_list|(
name|LongMath
operator|.
name|divide
argument_list|(
name|bits
argument_list|,
literal|64
argument_list|,
name|RoundingMode
operator|.
name|CEILING
argument_list|)
argument_list|)
index|]
argument_list|)
expr_stmt|;
block|}
comment|// Used by serialization
DECL|method|BitArray (long[] data)
name|BitArray
parameter_list|(
name|long
index|[]
name|data
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|data
operator|.
name|length
operator|>
literal|0
argument_list|,
literal|"data length is zero!"
argument_list|)
expr_stmt|;
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
name|long
name|bitCount
init|=
literal|0
decl_stmt|;
for|for
control|(
name|long
name|value
range|:
name|data
control|)
block|{
name|bitCount
operator|+=
name|Long
operator|.
name|bitCount
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|bitCount
operator|=
name|bitCount
expr_stmt|;
block|}
comment|/** Returns true if the bit changed value. */
DECL|method|set (long index)
name|boolean
name|set
parameter_list|(
name|long
name|index
parameter_list|)
block|{
if|if
condition|(
operator|!
name|get
argument_list|(
name|index
argument_list|)
condition|)
block|{
name|data
index|[
call|(
name|int
call|)
argument_list|(
name|index
operator|>>>
literal|6
argument_list|)
index|]
operator||=
operator|(
literal|1L
operator|<<
name|index
operator|)
expr_stmt|;
name|bitCount
operator|++
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|get (long index)
name|boolean
name|get
parameter_list|(
name|long
name|index
parameter_list|)
block|{
return|return
operator|(
name|data
index|[
call|(
name|int
call|)
argument_list|(
name|index
operator|>>>
literal|6
argument_list|)
index|]
operator|&
operator|(
literal|1L
operator|<<
name|index
operator|)
operator|)
operator|!=
literal|0
return|;
block|}
comment|/** Number of bits */
DECL|method|bitSize ()
name|long
name|bitSize
parameter_list|()
block|{
return|return
operator|(
name|long
operator|)
name|data
operator|.
name|length
operator|*
name|Long
operator|.
name|SIZE
return|;
block|}
comment|/** Number of set bits (1s) */
DECL|method|bitCount ()
name|long
name|bitCount
parameter_list|()
block|{
return|return
name|bitCount
return|;
block|}
DECL|method|copy ()
name|BitArray
name|copy
parameter_list|()
block|{
return|return
operator|new
name|BitArray
argument_list|(
name|data
operator|.
name|clone
argument_list|()
argument_list|)
return|;
block|}
comment|/** Combines the two BitArrays using bitwise OR. */
DECL|method|putAll (BitArray array)
name|void
name|putAll
parameter_list|(
name|BitArray
name|array
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|data
operator|.
name|length
operator|==
name|array
operator|.
name|data
operator|.
name|length
argument_list|,
literal|"BitArrays must be of equal length (%s != %s)"
argument_list|,
name|data
operator|.
name|length
argument_list|,
name|array
operator|.
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
name|bitCount
operator|=
literal|0
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
name|data
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|data
index|[
name|i
index|]
operator||=
name|array
operator|.
name|data
index|[
name|i
index|]
expr_stmt|;
name|bitCount
operator|+=
name|Long
operator|.
name|bitCount
argument_list|(
name|data
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|equals (Object o)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|BitArray
condition|)
block|{
name|BitArray
name|bitArray
init|=
operator|(
name|BitArray
operator|)
name|o
decl_stmt|;
return|return
name|Arrays
operator|.
name|equals
argument_list|(
name|data
argument_list|,
name|bitArray
operator|.
name|data
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|hashCode
argument_list|(
name|data
argument_list|)
return|;
block|}
block|}
block|}
end_enum

end_unit

