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
name|testing
operator|.
name|EqualsTester
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
operator|.
name|SerializableTester
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
name|util
operator|.
name|Random
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
comment|/**  * Tests for SimpleGenericBloomFilter and derived BloomFilter views.  *  * @author Dimitris Andreou  */
end_comment

begin_class
DECL|class|BloomFilterTest
specifier|public
class|class
name|BloomFilterTest
extends|extends
name|TestCase
block|{
comment|/**    * Sanity checking with many combinations of false positive rates and expected insertions    */
DECL|method|testBasic ()
specifier|public
name|void
name|testBasic
parameter_list|()
block|{
for|for
control|(
name|double
name|fpr
init|=
literal|0.0000001
init|;
name|fpr
operator|<
literal|0.1
condition|;
name|fpr
operator|*=
literal|10
control|)
block|{
for|for
control|(
name|int
name|expectedInsertions
init|=
literal|1
init|;
name|expectedInsertions
operator|<=
literal|10000
condition|;
name|expectedInsertions
operator|*=
literal|10
control|)
block|{
name|checkSanity
argument_list|(
name|BloomFilter
operator|.
name|create
argument_list|(
name|HashTestUtils
operator|.
name|BAD_FUNNEL
argument_list|,
name|expectedInsertions
argument_list|,
name|fpr
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testPreconditions ()
specifier|public
name|void
name|testPreconditions
parameter_list|()
block|{
try|try
block|{
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|stringFunnel
argument_list|()
argument_list|,
operator|-
literal|1
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
block|{}
try|try
block|{
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|stringFunnel
argument_list|()
argument_list|,
operator|-
literal|1
argument_list|,
literal|0.03
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
block|{}
try|try
block|{
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|stringFunnel
argument_list|()
argument_list|,
literal|1
argument_list|,
literal|0.0
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
block|{}
try|try
block|{
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|stringFunnel
argument_list|()
argument_list|,
literal|1
argument_list|,
literal|1.0
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
block|{}
block|}
DECL|method|testFailureWhenMoreThan255HashFunctionsAreNeeded ()
specifier|public
name|void
name|testFailureWhenMoreThan255HashFunctionsAreNeeded
parameter_list|()
block|{
try|try
block|{
name|int
name|n
init|=
literal|1000
decl_stmt|;
name|double
name|p
init|=
literal|0.00000000000000000000000000000000000000000000000000000000000000000000000000000001
decl_stmt|;
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|stringFunnel
argument_list|()
argument_list|,
name|n
argument_list|,
name|p
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
block|{}
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
name|testAllPublicInstanceMethods
argument_list|(
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|stringFunnel
argument_list|()
argument_list|,
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|tester
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|BloomFilter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**    * Tests that we never get an optimal hashes number of zero.    */
DECL|method|testOptimalHashes ()
specifier|public
name|void
name|testOptimalHashes
parameter_list|()
block|{
for|for
control|(
name|int
name|n
init|=
literal|1
init|;
name|n
operator|<
literal|1000
condition|;
name|n
operator|++
control|)
block|{
for|for
control|(
name|int
name|m
init|=
literal|0
init|;
name|m
operator|<
literal|1000
condition|;
name|m
operator|++
control|)
block|{
name|assertTrue
argument_list|(
name|BloomFilter
operator|.
name|optimalNumOfHashFunctions
argument_list|(
name|n
argument_list|,
name|m
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Tests that we always get a non-negative optimal size.    */
DECL|method|testOptimalSize ()
specifier|public
name|void
name|testOptimalSize
parameter_list|()
block|{
for|for
control|(
name|int
name|n
init|=
literal|1
init|;
name|n
operator|<
literal|1000
condition|;
name|n
operator|++
control|)
block|{
for|for
control|(
name|double
name|fpp
init|=
name|Double
operator|.
name|MIN_VALUE
init|;
name|fpp
operator|<
literal|1.0
condition|;
name|fpp
operator|+=
literal|0.001
control|)
block|{
name|assertTrue
argument_list|(
name|BloomFilter
operator|.
name|optimalNumOfBits
argument_list|(
name|n
argument_list|,
name|fpp
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
comment|// some random values
name|Random
name|random
init|=
operator|new
name|Random
argument_list|(
literal|0
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|repeats
init|=
literal|0
init|;
name|repeats
operator|<
literal|10000
condition|;
name|repeats
operator|++
control|)
block|{
name|assertTrue
argument_list|(
name|BloomFilter
operator|.
name|optimalNumOfBits
argument_list|(
name|random
operator|.
name|nextInt
argument_list|(
literal|1
operator|<<
literal|16
argument_list|)
argument_list|,
name|random
operator|.
name|nextDouble
argument_list|()
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
block|}
comment|// and some crazy values (this used to be capped to Integer.MAX_VALUE, now it can go bigger
name|assertEquals
argument_list|(
literal|3327428144502L
argument_list|,
name|BloomFilter
operator|.
name|optimalNumOfBits
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|Double
operator|.
name|MIN_VALUE
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|BloomFilter
operator|.
name|create
argument_list|(
name|HashTestUtils
operator|.
name|BAD_FUNNEL
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|Double
operator|.
name|MIN_VALUE
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"we can't represent such a large BF!"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Could not create BloomFilter of 3327428144502 bits"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|checkSanity (BloomFilter<Object> bf)
specifier|private
name|void
name|checkSanity
parameter_list|(
name|BloomFilter
argument_list|<
name|Object
argument_list|>
name|bf
parameter_list|)
block|{
name|assertFalse
argument_list|(
name|bf
operator|.
name|mightContain
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
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
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|o
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|bf
operator|.
name|put
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bf
operator|.
name|mightContain
argument_list|(
name|o
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCopy ()
specifier|public
name|void
name|testCopy
parameter_list|()
block|{
name|BloomFilter
argument_list|<
name|CharSequence
argument_list|>
name|original
init|=
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|stringFunnel
argument_list|()
argument_list|,
literal|100
argument_list|)
decl_stmt|;
name|BloomFilter
argument_list|<
name|CharSequence
argument_list|>
name|copy
init|=
name|original
operator|.
name|copy
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|original
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|original
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
DECL|method|testExpectedFpp ()
specifier|public
name|void
name|testExpectedFpp
parameter_list|()
block|{
name|BloomFilter
argument_list|<
name|Object
argument_list|>
name|bf
init|=
name|BloomFilter
operator|.
name|create
argument_list|(
name|HashTestUtils
operator|.
name|BAD_FUNNEL
argument_list|,
literal|10
argument_list|,
literal|0.03
argument_list|)
decl_stmt|;
name|double
name|fpp
init|=
name|bf
operator|.
name|expectedFpp
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0.0
argument_list|,
name|fpp
argument_list|)
expr_stmt|;
comment|// usually completed in less than 200 iterations
while|while
condition|(
name|fpp
operator|!=
literal|1.0
condition|)
block|{
name|boolean
name|changed
init|=
name|bf
operator|.
name|put
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
decl_stmt|;
name|double
name|newFpp
init|=
name|bf
operator|.
name|expectedFpp
argument_list|()
decl_stmt|;
comment|// if changed, the new fpp is strictly higher, otherwise it is the same
name|assertTrue
argument_list|(
name|changed
condition|?
name|newFpp
operator|>
name|fpp
else|:
name|newFpp
operator|==
name|fpp
argument_list|)
expr_stmt|;
name|fpp
operator|=
name|newFpp
expr_stmt|;
block|}
block|}
DECL|method|testEquals_empty ()
specifier|public
name|void
name|testEquals_empty
parameter_list|()
block|{
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|byteArrayFunnel
argument_list|()
argument_list|,
literal|100
argument_list|,
literal|0.01
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|byteArrayFunnel
argument_list|()
argument_list|,
literal|100
argument_list|,
literal|0.02
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|byteArrayFunnel
argument_list|()
argument_list|,
literal|200
argument_list|,
literal|0.01
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|byteArrayFunnel
argument_list|()
argument_list|,
literal|200
argument_list|,
literal|0.02
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|stringFunnel
argument_list|()
argument_list|,
literal|100
argument_list|,
literal|0.01
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|stringFunnel
argument_list|()
argument_list|,
literal|100
argument_list|,
literal|0.02
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|stringFunnel
argument_list|()
argument_list|,
literal|200
argument_list|,
literal|0.01
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|stringFunnel
argument_list|()
argument_list|,
literal|200
argument_list|,
literal|0.02
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
name|BloomFilter
argument_list|<
name|CharSequence
argument_list|>
name|bf1
init|=
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|stringFunnel
argument_list|()
argument_list|,
literal|100
argument_list|)
decl_stmt|;
name|bf1
operator|.
name|put
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|bf1
operator|.
name|put
argument_list|(
literal|"2"
argument_list|)
expr_stmt|;
name|BloomFilter
argument_list|<
name|CharSequence
argument_list|>
name|bf2
init|=
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|stringFunnel
argument_list|()
argument_list|,
literal|100
argument_list|)
decl_stmt|;
name|bf2
operator|.
name|put
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|bf2
operator|.
name|put
argument_list|(
literal|"2"
argument_list|)
expr_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|bf1
argument_list|,
name|bf2
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
name|bf2
operator|.
name|put
argument_list|(
literal|"3"
argument_list|)
expr_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|bf1
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|bf2
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testEqualsWithCustomFunnel ()
specifier|public
name|void
name|testEqualsWithCustomFunnel
parameter_list|()
block|{
name|BloomFilter
argument_list|<
name|Long
argument_list|>
name|bf1
init|=
name|BloomFilter
operator|.
name|create
argument_list|(
operator|new
name|CustomFunnel
argument_list|()
argument_list|,
literal|100
argument_list|)
decl_stmt|;
name|BloomFilter
argument_list|<
name|Long
argument_list|>
name|bf2
init|=
name|BloomFilter
operator|.
name|create
argument_list|(
operator|new
name|CustomFunnel
argument_list|()
argument_list|,
literal|100
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|bf1
argument_list|,
name|bf2
argument_list|)
expr_stmt|;
block|}
DECL|method|testSerializationWithCustomFunnel ()
specifier|public
name|void
name|testSerializationWithCustomFunnel
parameter_list|()
block|{
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|BloomFilter
operator|.
name|create
argument_list|(
operator|new
name|CustomFunnel
argument_list|()
argument_list|,
literal|100
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|CustomFunnel
specifier|private
specifier|static
specifier|final
class|class
name|CustomFunnel
implements|implements
name|Funnel
argument_list|<
name|Long
argument_list|>
block|{
annotation|@
name|Override
DECL|method|funnel (Long value, PrimitiveSink into)
specifier|public
name|void
name|funnel
parameter_list|(
name|Long
name|value
parameter_list|,
name|PrimitiveSink
name|into
parameter_list|)
block|{
name|into
operator|.
name|putLong
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object object)
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
return|return
operator|(
name|object
operator|instanceof
name|CustomFunnel
operator|)
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
literal|42
return|;
block|}
block|}
DECL|method|testPutReturnValue ()
specifier|public
name|void
name|testPutReturnValue
parameter_list|()
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
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|BloomFilter
argument_list|<
name|CharSequence
argument_list|>
name|bf
init|=
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|stringFunnel
argument_list|()
argument_list|,
literal|100
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
literal|10
condition|;
name|j
operator|++
control|)
block|{
name|String
name|value
init|=
operator|new
name|Object
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|boolean
name|mightContain
init|=
name|bf
operator|.
name|mightContain
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|boolean
name|put
init|=
name|bf
operator|.
name|put
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|mightContain
operator|!=
name|put
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testJavaSerialization ()
specifier|public
name|void
name|testJavaSerialization
parameter_list|()
block|{
name|BloomFilter
argument_list|<
name|byte
index|[]
argument_list|>
name|bf
init|=
name|BloomFilter
operator|.
name|create
argument_list|(
name|Funnels
operator|.
name|byteArrayFunnel
argument_list|()
argument_list|,
literal|100
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
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|bf
operator|.
name|put
argument_list|(
name|Ints
operator|.
name|toByteArray
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|BloomFilter
argument_list|<
name|byte
index|[]
argument_list|>
name|copy
init|=
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|bf
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
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|assertTrue
argument_list|(
name|copy
operator|.
name|mightContain
argument_list|(
name|Ints
operator|.
name|toByteArray
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|bf
operator|.
name|expectedFpp
argument_list|()
argument_list|,
name|copy
operator|.
name|expectedFpp
argument_list|()
argument_list|)
expr_stmt|;
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|bf
argument_list|)
expr_stmt|;
block|}
comment|/**    * This test will fail whenever someone updates/reorders the BloomFilterStrategies constants.    * Only appending a new constant is allowed.    */
DECL|method|testBloomFilterStrategies ()
specifier|public
name|void
name|testBloomFilterStrategies
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|BloomFilterStrategies
operator|.
name|values
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|BloomFilterStrategies
operator|.
name|MURMUR128_MITZ_32
argument_list|,
name|BloomFilterStrategies
operator|.
name|values
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

