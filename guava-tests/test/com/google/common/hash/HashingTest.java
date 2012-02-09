begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|hash
operator|.
name|Hashing
operator|.
name|ConcatenatedHashFunction
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
name|collect
operator|.
name|ImmutableList
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
name|collect
operator|.
name|Lists
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
name|util
operator|.
name|concurrent
operator|.
name|AtomicLongMap
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
name|testing
operator|.
name|NullPointerTester
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_comment
comment|/**  * Unit tests for {@link Hashing}.  *  * @author andreou@google.com (Dimitris Andreou)  * @author kak@google.com (Kurt Alfred Kluever)  */
end_comment

begin_class
DECL|class|HashingTest
specifier|public
class|class
name|HashingTest
extends|extends
name|TestCase
block|{
DECL|method|testMd5 ()
specifier|public
name|void
name|testMd5
parameter_list|()
block|{
name|HashTestUtils
operator|.
name|checkAvalanche
argument_list|(
name|Hashing
operator|.
name|md5
argument_list|()
argument_list|,
literal|100
argument_list|,
literal|0.4
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkNo2BitCharacteristics
argument_list|(
name|Hashing
operator|.
name|md5
argument_list|()
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkNoFunnels
argument_list|(
name|Hashing
operator|.
name|md5
argument_list|()
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|assertInvariants
argument_list|(
name|Hashing
operator|.
name|md5
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSha1 ()
specifier|public
name|void
name|testSha1
parameter_list|()
block|{
name|HashTestUtils
operator|.
name|checkAvalanche
argument_list|(
name|Hashing
operator|.
name|sha1
argument_list|()
argument_list|,
literal|100
argument_list|,
literal|0.4
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkNo2BitCharacteristics
argument_list|(
name|Hashing
operator|.
name|sha1
argument_list|()
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkNoFunnels
argument_list|(
name|Hashing
operator|.
name|sha1
argument_list|()
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|assertInvariants
argument_list|(
name|Hashing
operator|.
name|sha1
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSha256 ()
specifier|public
name|void
name|testSha256
parameter_list|()
block|{
name|HashTestUtils
operator|.
name|checkAvalanche
argument_list|(
name|Hashing
operator|.
name|sha256
argument_list|()
argument_list|,
literal|100
argument_list|,
literal|0.4
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkNo2BitCharacteristics
argument_list|(
name|Hashing
operator|.
name|sha256
argument_list|()
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkNoFunnels
argument_list|(
name|Hashing
operator|.
name|sha256
argument_list|()
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|assertInvariants
argument_list|(
name|Hashing
operator|.
name|sha256
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSha512 ()
specifier|public
name|void
name|testSha512
parameter_list|()
block|{
name|HashTestUtils
operator|.
name|checkAvalanche
argument_list|(
name|Hashing
operator|.
name|sha512
argument_list|()
argument_list|,
literal|100
argument_list|,
literal|0.4
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkNo2BitCharacteristics
argument_list|(
name|Hashing
operator|.
name|sha512
argument_list|()
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkNoFunnels
argument_list|(
name|Hashing
operator|.
name|sha512
argument_list|()
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|assertInvariants
argument_list|(
name|Hashing
operator|.
name|sha512
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testMurmur3_128 ()
specifier|public
name|void
name|testMurmur3_128
parameter_list|()
block|{
name|HashTestUtils
operator|.
name|check2BitAvalanche
argument_list|(
name|Hashing
operator|.
name|murmur3_128
argument_list|()
argument_list|,
literal|250
argument_list|,
literal|0.20
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkAvalanche
argument_list|(
name|Hashing
operator|.
name|murmur3_128
argument_list|()
argument_list|,
literal|250
argument_list|,
literal|0.17
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkNo2BitCharacteristics
argument_list|(
name|Hashing
operator|.
name|murmur3_128
argument_list|()
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkNoFunnels
argument_list|(
name|Hashing
operator|.
name|murmur3_128
argument_list|()
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|assertInvariants
argument_list|(
name|Hashing
operator|.
name|murmur3_128
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testMurmur3_32 ()
specifier|public
name|void
name|testMurmur3_32
parameter_list|()
block|{
name|HashTestUtils
operator|.
name|check2BitAvalanche
argument_list|(
name|Hashing
operator|.
name|murmur3_32
argument_list|()
argument_list|,
literal|250
argument_list|,
literal|0.20
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkAvalanche
argument_list|(
name|Hashing
operator|.
name|murmur3_32
argument_list|()
argument_list|,
literal|250
argument_list|,
literal|0.17
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkNo2BitCharacteristics
argument_list|(
name|Hashing
operator|.
name|murmur3_32
argument_list|()
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkNoFunnels
argument_list|(
name|Hashing
operator|.
name|murmur3_32
argument_list|()
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|assertInvariants
argument_list|(
name|Hashing
operator|.
name|murmur3_32
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGoodFastHash ()
specifier|public
name|void
name|testGoodFastHash
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
literal|200
condition|;
name|i
operator|+=
literal|17
control|)
block|{
name|HashFunction
name|hasher
init|=
name|Hashing
operator|.
name|goodFastHash
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|hasher
operator|.
name|bits
argument_list|()
operator|>=
name|i
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|assertInvariants
argument_list|(
name|hasher
argument_list|)
expr_stmt|;
block|}
block|}
comment|// goodFastHash(32) uses Murmur3_32. Use the same epsilon bounds.
DECL|method|testGoodFastHash32 ()
specifier|public
name|void
name|testGoodFastHash32
parameter_list|()
block|{
name|HashTestUtils
operator|.
name|check2BitAvalanche
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|32
argument_list|)
argument_list|,
literal|250
argument_list|,
literal|0.20
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkAvalanche
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|32
argument_list|)
argument_list|,
literal|250
argument_list|,
literal|0.17
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkNo2BitCharacteristics
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|32
argument_list|)
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkNoFunnels
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|32
argument_list|)
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|assertInvariants
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|32
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// goodFastHash(128) uses Murmur3_128. Use the same epsilon bounds.
DECL|method|testGoodFastHash128 ()
specifier|public
name|void
name|testGoodFastHash128
parameter_list|()
block|{
name|HashTestUtils
operator|.
name|check2BitAvalanche
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|128
argument_list|)
argument_list|,
literal|250
argument_list|,
literal|0.20
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkAvalanche
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|128
argument_list|)
argument_list|,
literal|250
argument_list|,
literal|0.17
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkNo2BitCharacteristics
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|128
argument_list|)
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkNoFunnels
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|128
argument_list|)
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|assertInvariants
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|128
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// goodFastHash(256) uses Murmur3_128. Use the same epsilon bounds.
DECL|method|testGoodFastHash256 ()
specifier|public
name|void
name|testGoodFastHash256
parameter_list|()
block|{
name|HashTestUtils
operator|.
name|check2BitAvalanche
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|256
argument_list|)
argument_list|,
literal|250
argument_list|,
literal|0.20
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkAvalanche
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|256
argument_list|)
argument_list|,
literal|250
argument_list|,
literal|0.17
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkNo2BitCharacteristics
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|256
argument_list|)
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|checkNoFunnels
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|256
argument_list|)
argument_list|)
expr_stmt|;
name|HashTestUtils
operator|.
name|assertInvariants
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|256
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testPadToLong ()
specifier|public
name|void
name|testPadToLong
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0x1111111111111111L
argument_list|,
name|Hashing
operator|.
name|padToLong
argument_list|(
name|HashCodes
operator|.
name|fromLong
argument_list|(
literal|0x1111111111111111L
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x9999999999999999L
argument_list|,
name|Hashing
operator|.
name|padToLong
argument_list|(
name|HashCodes
operator|.
name|fromLong
argument_list|(
literal|0x9999999999999999L
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x0000000011111111L
argument_list|,
name|Hashing
operator|.
name|padToLong
argument_list|(
name|HashCodes
operator|.
name|fromInt
argument_list|(
literal|0x11111111
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x0000000099999999L
argument_list|,
name|Hashing
operator|.
name|padToLong
argument_list|(
name|HashCodes
operator|.
name|fromInt
argument_list|(
literal|0x99999999
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testConsistentHash_correctness ()
specifier|public
name|void
name|testConsistentHash_correctness
parameter_list|()
block|{
name|long
index|[]
name|interestingValues
init|=
block|{
operator|-
literal|1
block|,
literal|0
block|,
literal|1
block|,
literal|2
block|,
name|Long
operator|.
name|MAX_VALUE
block|,
name|Long
operator|.
name|MIN_VALUE
block|}
decl_stmt|;
for|for
control|(
name|long
name|h
range|:
name|interestingValues
control|)
block|{
name|checkConsistentHashCorrectness
argument_list|(
name|h
argument_list|)
expr_stmt|;
block|}
name|Random
name|r
init|=
operator|new
name|Random
argument_list|(
literal|7
argument_list|)
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
literal|20
condition|;
name|i
operator|++
control|)
block|{
name|checkConsistentHashCorrectness
argument_list|(
name|r
operator|.
name|nextLong
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|checkConsistentHashCorrectness (long hashCode)
specifier|private
name|void
name|checkConsistentHashCorrectness
parameter_list|(
name|long
name|hashCode
parameter_list|)
block|{
name|int
name|last
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|shards
init|=
literal|1
init|;
name|shards
operator|<=
literal|100000
condition|;
name|shards
operator|++
control|)
block|{
name|int
name|b
init|=
name|Hashing
operator|.
name|consistentHash
argument_list|(
name|hashCode
argument_list|,
name|shards
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
operator|!=
name|last
condition|)
block|{
name|assertEquals
argument_list|(
name|shards
operator|-
literal|1
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|last
operator|=
name|b
expr_stmt|;
block|}
block|}
block|}
DECL|method|testConsistentHash_probabilities ()
specifier|public
name|void
name|testConsistentHash_probabilities
parameter_list|()
block|{
name|AtomicLongMap
argument_list|<
name|Integer
argument_list|>
name|map
init|=
name|AtomicLongMap
operator|.
name|create
argument_list|()
decl_stmt|;
name|Random
name|r
init|=
operator|new
name|Random
argument_list|(
literal|9
argument_list|)
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
name|ITERS
condition|;
name|i
operator|++
control|)
block|{
name|countRemaps
argument_list|(
name|r
operator|.
name|nextLong
argument_list|()
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|shard
init|=
literal|2
init|;
name|shard
operator|<=
name|MAX_SHARDS
condition|;
name|shard
operator|++
control|)
block|{
comment|// Rough: don't exceed 1.2x the expected number of remaps by more than 20
name|assertTrue
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|shard
argument_list|)
operator|<=
literal|1.2
operator|*
name|ITERS
operator|/
name|shard
operator|+
literal|20
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|countRemaps (long h, AtomicLongMap<Integer> map)
specifier|private
name|void
name|countRemaps
parameter_list|(
name|long
name|h
parameter_list|,
name|AtomicLongMap
argument_list|<
name|Integer
argument_list|>
name|map
parameter_list|)
block|{
name|int
name|last
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|shards
init|=
literal|2
init|;
name|shards
operator|<=
name|MAX_SHARDS
condition|;
name|shards
operator|++
control|)
block|{
name|int
name|chosen
init|=
name|Hashing
operator|.
name|consistentHash
argument_list|(
name|h
argument_list|,
name|shards
argument_list|)
decl_stmt|;
if|if
condition|(
name|chosen
operator|!=
name|last
condition|)
block|{
name|map
operator|.
name|incrementAndGet
argument_list|(
name|shards
argument_list|)
expr_stmt|;
name|last
operator|=
name|chosen
expr_stmt|;
block|}
block|}
block|}
DECL|field|ITERS
specifier|private
specifier|static
specifier|final
name|int
name|ITERS
init|=
literal|10000
decl_stmt|;
DECL|field|MAX_SHARDS
specifier|private
specifier|static
specifier|final
name|int
name|MAX_SHARDS
init|=
literal|500
decl_stmt|;
DECL|method|testConsistentHash_outOfRange ()
specifier|public
name|void
name|testConsistentHash_outOfRange
parameter_list|()
block|{
try|try
block|{
name|Hashing
operator|.
name|consistentHash
argument_list|(
literal|5L
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testConsistentHash_ofHashCode ()
specifier|public
name|void
name|testConsistentHash_ofHashCode
parameter_list|()
block|{
name|checkSameResult
argument_list|(
name|HashCodes
operator|.
name|fromLong
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|checkSameResult
argument_list|(
name|HashCodes
operator|.
name|fromLong
argument_list|(
literal|0x9999999999999999L
argument_list|)
argument_list|,
literal|0x9999999999999999L
argument_list|)
expr_stmt|;
name|checkSameResult
argument_list|(
name|HashCodes
operator|.
name|fromInt
argument_list|(
literal|0x99999999
argument_list|)
argument_list|,
literal|0x0000000099999999L
argument_list|)
expr_stmt|;
block|}
DECL|method|checkSameResult (HashCode hashCode, long equivLong)
specifier|public
name|void
name|checkSameResult
parameter_list|(
name|HashCode
name|hashCode
parameter_list|,
name|long
name|equivLong
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|Hashing
operator|.
name|consistentHash
argument_list|(
name|equivLong
argument_list|,
literal|5555
argument_list|)
argument_list|,
name|Hashing
operator|.
name|consistentHash
argument_list|(
name|hashCode
argument_list|,
literal|5555
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCombineOrdered_empty ()
specifier|public
name|void
name|testCombineOrdered_empty
parameter_list|()
block|{
try|try
block|{
name|Hashing
operator|.
name|combineOrdered
argument_list|(
name|Collections
operator|.
expr|<
name|HashCode
operator|>
name|emptySet
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testCombineOrdered_differentBitLengths ()
specifier|public
name|void
name|testCombineOrdered_differentBitLengths
parameter_list|()
block|{
try|try
block|{
name|Hashing
operator|.
name|combineOrdered
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|HashCodes
operator|.
name|fromInt
argument_list|(
literal|32
argument_list|)
argument_list|,
name|HashCodes
operator|.
name|fromLong
argument_list|(
literal|32L
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testCombineOrdered ()
specifier|public
name|void
name|testCombineOrdered
parameter_list|()
block|{
name|HashCode
name|hash31
init|=
name|HashCodes
operator|.
name|fromInt
argument_list|(
literal|31
argument_list|)
decl_stmt|;
name|HashCode
name|hash32
init|=
name|HashCodes
operator|.
name|fromInt
argument_list|(
literal|32
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|hash32
argument_list|,
name|Hashing
operator|.
name|combineOrdered
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|hash32
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HashCodes
operator|.
name|fromBytes
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0x80
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|}
argument_list|)
argument_list|,
name|Hashing
operator|.
name|combineOrdered
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|hash32
argument_list|,
name|hash32
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HashCodes
operator|.
name|fromBytes
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0xa0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|}
argument_list|)
argument_list|,
name|Hashing
operator|.
name|combineOrdered
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|hash32
argument_list|,
name|hash32
argument_list|,
name|hash32
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Hashing
operator|.
name|combineOrdered
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|hash31
argument_list|,
name|hash32
argument_list|)
argument_list|)
operator|.
name|equals
argument_list|(
name|Hashing
operator|.
name|combineOrdered
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|hash32
argument_list|,
name|hash31
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCombineOrdered_randomHashCodes ()
specifier|public
name|void
name|testCombineOrdered_randomHashCodes
parameter_list|()
block|{
name|Random
name|random
init|=
operator|new
name|Random
argument_list|(
literal|7
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|HashCode
argument_list|>
name|hashCodes
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
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
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|hashCodes
operator|.
name|add
argument_list|(
name|HashCodes
operator|.
name|fromLong
argument_list|(
name|random
operator|.
name|nextLong
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|HashCode
name|hashCode1
init|=
name|Hashing
operator|.
name|combineOrdered
argument_list|(
name|hashCodes
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|shuffle
argument_list|(
name|hashCodes
argument_list|,
name|random
argument_list|)
expr_stmt|;
name|HashCode
name|hashCode2
init|=
name|Hashing
operator|.
name|combineOrdered
argument_list|(
name|hashCodes
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|hashCode1
operator|.
name|equals
argument_list|(
name|hashCode2
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCombineUnordered_empty ()
specifier|public
name|void
name|testCombineUnordered_empty
parameter_list|()
block|{
try|try
block|{
name|Hashing
operator|.
name|combineUnordered
argument_list|(
name|Collections
operator|.
expr|<
name|HashCode
operator|>
name|emptySet
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testCombineUnordered_differentBitLengths ()
specifier|public
name|void
name|testCombineUnordered_differentBitLengths
parameter_list|()
block|{
try|try
block|{
name|Hashing
operator|.
name|combineUnordered
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|HashCodes
operator|.
name|fromInt
argument_list|(
literal|32
argument_list|)
argument_list|,
name|HashCodes
operator|.
name|fromLong
argument_list|(
literal|32L
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testCombineUnordered ()
specifier|public
name|void
name|testCombineUnordered
parameter_list|()
block|{
name|HashCode
name|hash31
init|=
name|HashCodes
operator|.
name|fromInt
argument_list|(
literal|31
argument_list|)
decl_stmt|;
name|HashCode
name|hash32
init|=
name|HashCodes
operator|.
name|fromInt
argument_list|(
literal|32
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|hash32
argument_list|,
name|Hashing
operator|.
name|combineUnordered
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|hash32
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HashCodes
operator|.
name|fromInt
argument_list|(
literal|64
argument_list|)
argument_list|,
name|Hashing
operator|.
name|combineUnordered
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|hash32
argument_list|,
name|hash32
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HashCodes
operator|.
name|fromInt
argument_list|(
literal|96
argument_list|)
argument_list|,
name|Hashing
operator|.
name|combineUnordered
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|hash32
argument_list|,
name|hash32
argument_list|,
name|hash32
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Hashing
operator|.
name|combineUnordered
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|hash31
argument_list|,
name|hash32
argument_list|)
argument_list|)
argument_list|,
name|Hashing
operator|.
name|combineUnordered
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|hash32
argument_list|,
name|hash31
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCombineUnordered_randomHashCodes ()
specifier|public
name|void
name|testCombineUnordered_randomHashCodes
parameter_list|()
block|{
name|Random
name|random
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|HashCode
argument_list|>
name|hashCodes
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
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
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|hashCodes
operator|.
name|add
argument_list|(
name|HashCodes
operator|.
name|fromLong
argument_list|(
name|random
operator|.
name|nextLong
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|HashCode
name|hashCode1
init|=
name|Hashing
operator|.
name|combineUnordered
argument_list|(
name|hashCodes
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|shuffle
argument_list|(
name|hashCodes
argument_list|)
expr_stmt|;
name|HashCode
name|hashCode2
init|=
name|Hashing
operator|.
name|combineUnordered
argument_list|(
name|hashCodes
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|hashCode1
argument_list|,
name|hashCode2
argument_list|)
expr_stmt|;
block|}
DECL|method|testConcatenatedHashFunction_bits ()
specifier|public
name|void
name|testConcatenatedHashFunction_bits
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Hashing
operator|.
name|md5
argument_list|()
operator|.
name|bits
argument_list|()
argument_list|,
operator|new
name|ConcatenatedHashFunction
argument_list|(
name|Hashing
operator|.
name|md5
argument_list|()
argument_list|)
operator|.
name|bits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Hashing
operator|.
name|md5
argument_list|()
operator|.
name|bits
argument_list|()
operator|+
name|Hashing
operator|.
name|murmur3_32
argument_list|()
operator|.
name|bits
argument_list|()
argument_list|,
operator|new
name|ConcatenatedHashFunction
argument_list|(
name|Hashing
operator|.
name|md5
argument_list|()
argument_list|,
name|Hashing
operator|.
name|murmur3_32
argument_list|()
argument_list|)
operator|.
name|bits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Hashing
operator|.
name|md5
argument_list|()
operator|.
name|bits
argument_list|()
operator|+
name|Hashing
operator|.
name|murmur3_32
argument_list|()
operator|.
name|bits
argument_list|()
operator|+
name|Hashing
operator|.
name|murmur3_128
argument_list|()
operator|.
name|bits
argument_list|()
argument_list|,
operator|new
name|ConcatenatedHashFunction
argument_list|(
name|Hashing
operator|.
name|md5
argument_list|()
argument_list|,
name|Hashing
operator|.
name|murmur3_32
argument_list|()
argument_list|,
name|Hashing
operator|.
name|murmur3_128
argument_list|()
argument_list|)
operator|.
name|bits
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testConcatenatedHashFunction_makeHash ()
specifier|public
name|void
name|testConcatenatedHashFunction_makeHash
parameter_list|()
block|{
name|byte
index|[]
name|md5Hash
init|=
name|Hashing
operator|.
name|md5
argument_list|()
operator|.
name|hashLong
argument_list|(
literal|42L
argument_list|)
operator|.
name|asBytes
argument_list|()
decl_stmt|;
name|byte
index|[]
name|murmur3Hash
init|=
name|Hashing
operator|.
name|murmur3_32
argument_list|()
operator|.
name|hashLong
argument_list|(
literal|42L
argument_list|)
operator|.
name|asBytes
argument_list|()
decl_stmt|;
name|byte
index|[]
name|combined
init|=
operator|new
name|byte
index|[
name|md5Hash
operator|.
name|length
operator|+
name|murmur3Hash
operator|.
name|length
index|]
decl_stmt|;
name|ByteBuffer
name|buffer
init|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|combined
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|put
argument_list|(
name|md5Hash
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|put
argument_list|(
name|murmur3Hash
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HashCodes
operator|.
name|fromBytes
argument_list|(
name|combined
argument_list|)
argument_list|,
operator|new
name|ConcatenatedHashFunction
argument_list|(
name|Hashing
operator|.
name|md5
argument_list|()
argument_list|,
name|Hashing
operator|.
name|murmur3_32
argument_list|()
argument_list|)
operator|.
name|hashLong
argument_list|(
literal|42L
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testNullPointers ()
specifier|public
name|void
name|testNullPointers
parameter_list|()
block|{
name|NullPointerTester
name|tester
init|=
operator|new
name|NullPointerTester
argument_list|()
decl_stmt|;
name|tester
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|Hashing
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

