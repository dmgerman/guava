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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLongArray
import|;
end_import

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Collections of strategies of generating the k * log(M) bits required for an element to be mapped  * to a BloomFilter of M bits and k hash functions. These strategies are part of the serialized form  * of the Bloom filters that use them, thus they must be preserved as is (no updates allowed, only  * introduction of new versions).  *  *<p>Important: the order of the constants cannot change, and they cannot be deleted - we depend on  * their ordinal for BloomFilter serialization.  *  * @author Dimitris Andreou  * @author Kurt Alfred Kluever  */
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
comment|/**    * See "Less Hashing, Same Performance: Building a Better Bloom Filter" by Adam Kirsch and Michael    * Mitzenmacher. The paper argues that this trick doesn't significantly deteriorate the    * performance of a Bloom filter (yet only needs two 32bit hash functions).    */
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
name|LockFreeBitArray
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
name|LockFreeBitArray
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
comment|/**    * This strategy uses all 128 bits of {@link Hashing#murmur3_128} when hashing. It looks different    * than the implementation in MURMUR128_MITZ_32 because we're avoiding the multiplication in the    * loop and doing a (much simpler) += hash2. We're also changing the index to a positive number by    * AND'ing with Long.MAX_VALUE instead of flipping the bits.    */
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
name|LockFreeBitArray
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
name|LockFreeBitArray
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
comment|/**    * Models a lock-free array of bits.    *    *<p>We use this instead of java.util.BitSet because we need access to the array of longs and we    * need compare-and-swap.    */
DECL|class|LockFreeBitArray
specifier|static
specifier|final
class|class
name|LockFreeBitArray
block|{
DECL|field|LONG_ADDRESSABLE_BITS
specifier|private
specifier|static
specifier|final
name|int
name|LONG_ADDRESSABLE_BITS
init|=
literal|6
decl_stmt|;
DECL|field|data
specifier|final
name|AtomicLongArray
name|data
decl_stmt|;
DECL|field|bitCount
specifier|private
specifier|final
name|LongAddable
name|bitCount
decl_stmt|;
DECL|method|LockFreeBitArray (long bits)
name|LockFreeBitArray
parameter_list|(
name|long
name|bits
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|bits
operator|>
literal|0
argument_list|,
literal|"data length is zero!"
argument_list|)
expr_stmt|;
comment|// Avoid delegating to this(long[]), since AtomicLongArray(long[]) will clone its input and
comment|// thus double memory usage.
name|this
operator|.
name|data
operator|=
operator|new
name|AtomicLongArray
argument_list|(
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
argument_list|)
expr_stmt|;
name|this
operator|.
name|bitCount
operator|=
name|LongAddables
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
comment|// Used by serialization
DECL|method|LockFreeBitArray (long[] data)
name|LockFreeBitArray
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
operator|new
name|AtomicLongArray
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|this
operator|.
name|bitCount
operator|=
name|LongAddables
operator|.
name|create
argument_list|()
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
operator|.
name|add
argument_list|(
name|bitCount
argument_list|)
expr_stmt|;
block|}
comment|/** Returns true if the bit changed value. */
DECL|method|set (long bitIndex)
name|boolean
name|set
parameter_list|(
name|long
name|bitIndex
parameter_list|)
block|{
if|if
condition|(
name|get
argument_list|(
name|bitIndex
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|longIndex
init|=
call|(
name|int
call|)
argument_list|(
name|bitIndex
operator|>>>
name|LONG_ADDRESSABLE_BITS
argument_list|)
decl_stmt|;
name|long
name|mask
init|=
literal|1L
operator|<<
name|bitIndex
decl_stmt|;
comment|// only cares about low 6 bits of bitIndex
name|long
name|oldValue
decl_stmt|;
name|long
name|newValue
decl_stmt|;
do|do
block|{
name|oldValue
operator|=
name|data
operator|.
name|get
argument_list|(
name|longIndex
argument_list|)
expr_stmt|;
name|newValue
operator|=
name|oldValue
operator||
name|mask
expr_stmt|;
if|if
condition|(
name|oldValue
operator|==
name|newValue
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
do|while
condition|(
operator|!
name|data
operator|.
name|compareAndSet
argument_list|(
name|longIndex
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
condition|)
do|;
comment|// We turned the bit on, so increment bitCount.
name|bitCount
operator|.
name|increment
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|get (long bitIndex)
name|boolean
name|get
parameter_list|(
name|long
name|bitIndex
parameter_list|)
block|{
return|return
operator|(
name|data
operator|.
name|get
argument_list|(
call|(
name|int
call|)
argument_list|(
name|bitIndex
operator|>>>
name|LONG_ADDRESSABLE_BITS
argument_list|)
argument_list|)
operator|&
operator|(
literal|1L
operator|<<
name|bitIndex
operator|)
operator|)
operator|!=
literal|0
return|;
block|}
comment|/**      * Careful here: if threads are mutating the atomicLongArray while this method is executing, the      * final long[] will be a "rolling snapshot" of the state of the bit array. This is usually good      * enough, but should be kept in mind.      */
DECL|method|toPlainArray (AtomicLongArray atomicLongArray)
specifier|public
specifier|static
name|long
index|[]
name|toPlainArray
parameter_list|(
name|AtomicLongArray
name|atomicLongArray
parameter_list|)
block|{
name|long
index|[]
name|array
init|=
operator|new
name|long
index|[
name|atomicLongArray
operator|.
name|length
argument_list|()
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
name|array
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
name|array
index|[
name|i
index|]
operator|=
name|atomicLongArray
operator|.
name|get
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
return|return
name|array
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
argument_list|()
operator|*
name|Long
operator|.
name|SIZE
return|;
block|}
comment|/**      * Number of set bits (1s).      *      *<p>Note that because of concurrent set calls and uses of atomics, this bitCount is a (very)      * close *estimate* of the actual number of bits set. It's not possible to do better than an      * estimate without locking. Note that the number, if not exactly accurate, is *always*      * underestimating, never overestimating.      */
DECL|method|bitCount ()
name|long
name|bitCount
parameter_list|()
block|{
return|return
name|bitCount
operator|.
name|sum
argument_list|()
return|;
block|}
DECL|method|copy ()
name|LockFreeBitArray
name|copy
parameter_list|()
block|{
return|return
operator|new
name|LockFreeBitArray
argument_list|(
name|toPlainArray
argument_list|(
name|data
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Combines the two BitArrays using bitwise OR.      *      *<p>NOTE: Because of the use of atomics, if the other LockFreeBitArray is being mutated while      * this operation is executing, not all of those new 1's may be set in the final state of this      * LockFreeBitArray. The ONLY guarantee provided is that all the bits that were set in the other      * LockFreeBitArray at the start of this method will be set in this LockFreeBitArray at the end      * of this method.      */
DECL|method|putAll (LockFreeBitArray other)
name|void
name|putAll
parameter_list|(
name|LockFreeBitArray
name|other
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|data
operator|.
name|length
argument_list|()
operator|==
name|other
operator|.
name|data
operator|.
name|length
argument_list|()
argument_list|,
literal|"BitArrays must be of equal length (%s != %s)"
argument_list|,
name|data
operator|.
name|length
argument_list|()
argument_list|,
name|other
operator|.
name|data
operator|.
name|length
argument_list|()
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
name|data
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|long
name|otherLong
init|=
name|other
operator|.
name|data
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|long
name|ourLongOld
decl_stmt|;
name|long
name|ourLongNew
decl_stmt|;
name|boolean
name|changedAnyBits
init|=
literal|true
decl_stmt|;
do|do
block|{
name|ourLongOld
operator|=
name|data
operator|.
name|get
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|ourLongNew
operator|=
name|ourLongOld
operator||
name|otherLong
expr_stmt|;
if|if
condition|(
name|ourLongOld
operator|==
name|ourLongNew
condition|)
block|{
name|changedAnyBits
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
do|while
condition|(
operator|!
name|data
operator|.
name|compareAndSet
argument_list|(
name|i
argument_list|,
name|ourLongOld
argument_list|,
name|ourLongNew
argument_list|)
condition|)
do|;
if|if
condition|(
name|changedAnyBits
condition|)
block|{
name|int
name|bitsAdded
init|=
name|Long
operator|.
name|bitCount
argument_list|(
name|ourLongNew
argument_list|)
operator|-
name|Long
operator|.
name|bitCount
argument_list|(
name|ourLongOld
argument_list|)
decl_stmt|;
name|bitCount
operator|.
name|add
argument_list|(
name|bitsAdded
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|LockFreeBitArray
condition|)
block|{
name|LockFreeBitArray
name|lockFreeBitArray
init|=
operator|(
name|LockFreeBitArray
operator|)
name|o
decl_stmt|;
comment|// TODO(lowasser): avoid allocation here
return|return
name|Arrays
operator|.
name|equals
argument_list|(
name|toPlainArray
argument_list|(
name|data
argument_list|)
argument_list|,
name|toPlainArray
argument_list|(
name|lockFreeBitArray
operator|.
name|data
argument_list|)
argument_list|)
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
comment|// TODO(lowasser): avoid allocation here
return|return
name|Arrays
operator|.
name|hashCode
argument_list|(
name|toPlainArray
argument_list|(
name|data
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_enum

end_unit

