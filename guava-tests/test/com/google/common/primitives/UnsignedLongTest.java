begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the  * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  * express or implied. See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.primitives
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
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
name|GwtCompatible
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
name|GwtIncompatible
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
name|ImmutableSet
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
name|java
operator|.
name|math
operator|.
name|BigInteger
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

begin_comment
comment|/**  * Tests for {@code UnsignedLong}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|UnsignedLongTest
specifier|public
class|class
name|UnsignedLongTest
extends|extends
name|TestCase
block|{
DECL|field|TEST_LONGS
specifier|private
specifier|static
specifier|final
name|ImmutableSet
argument_list|<
name|Long
argument_list|>
name|TEST_LONGS
decl_stmt|;
DECL|field|TEST_BIG_INTEGERS
specifier|private
specifier|static
specifier|final
name|ImmutableSet
argument_list|<
name|BigInteger
argument_list|>
name|TEST_BIG_INTEGERS
decl_stmt|;
static|static
block|{
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|Long
argument_list|>
name|testLongsBuilder
init|=
name|ImmutableSet
operator|.
name|builder
argument_list|()
decl_stmt|;
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|BigInteger
argument_list|>
name|testBigIntegersBuilder
init|=
name|ImmutableSet
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|long
name|i
init|=
operator|-
literal|3
init|;
name|i
operator|<=
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|testLongsBuilder
operator|.
name|add
argument_list|(
name|i
argument_list|)
operator|.
name|add
argument_list|(
name|Long
operator|.
name|MAX_VALUE
operator|+
name|i
argument_list|)
operator|.
name|add
argument_list|(
name|Long
operator|.
name|MIN_VALUE
operator|+
name|i
argument_list|)
operator|.
name|add
argument_list|(
name|Integer
operator|.
name|MIN_VALUE
operator|+
name|i
argument_list|)
operator|.
name|add
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
operator|+
name|i
argument_list|)
expr_stmt|;
name|BigInteger
name|bigI
init|=
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|testBigIntegersBuilder
operator|.
name|add
argument_list|(
name|bigI
argument_list|)
operator|.
name|add
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
operator|.
name|add
argument_list|(
name|bigI
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|Long
operator|.
name|MIN_VALUE
argument_list|)
operator|.
name|add
argument_list|(
name|bigI
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
operator|.
name|add
argument_list|(
name|bigI
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|Integer
operator|.
name|MIN_VALUE
argument_list|)
operator|.
name|add
argument_list|(
name|bigI
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|BigInteger
operator|.
name|ONE
operator|.
name|shiftLeft
argument_list|(
literal|63
argument_list|)
operator|.
name|add
argument_list|(
name|bigI
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|BigInteger
operator|.
name|ONE
operator|.
name|shiftLeft
argument_list|(
literal|64
argument_list|)
operator|.
name|add
argument_list|(
name|bigI
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|TEST_LONGS
operator|=
name|testLongsBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
name|TEST_BIG_INTEGERS
operator|=
name|testBigIntegersBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
DECL|method|testAsUnsignedAndLongValueAreInverses ()
specifier|public
name|void
name|testAsUnsignedAndLongValueAreInverses
parameter_list|()
block|{
for|for
control|(
name|long
name|value
range|:
name|TEST_LONGS
control|)
block|{
name|assertEquals
argument_list|(
name|UnsignedLongs
operator|.
name|toString
argument_list|(
name|value
argument_list|)
argument_list|,
name|value
argument_list|,
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|value
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testAsUnsignedBigIntegerValue ()
specifier|public
name|void
name|testAsUnsignedBigIntegerValue
parameter_list|()
block|{
for|for
control|(
name|long
name|value
range|:
name|TEST_LONGS
control|)
block|{
name|BigInteger
name|expected
init|=
operator|(
name|value
operator|>=
literal|0
operator|)
condition|?
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
else|:
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
operator|.
name|add
argument_list|(
name|BigInteger
operator|.
name|ZERO
operator|.
name|setBit
argument_list|(
literal|64
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|UnsignedLongs
operator|.
name|toString
argument_list|(
name|value
argument_list|)
argument_list|,
name|expected
argument_list|,
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|value
argument_list|)
operator|.
name|bigIntegerValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testValueOfLong ()
specifier|public
name|void
name|testValueOfLong
parameter_list|()
block|{
for|for
control|(
name|long
name|value
range|:
name|TEST_LONGS
control|)
block|{
name|boolean
name|expectSuccess
init|=
name|value
operator|>=
literal|0
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
name|value
argument_list|,
name|UnsignedLong
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|expectSuccess
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertFalse
argument_list|(
name|expectSuccess
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testValueOfBigInteger ()
specifier|public
name|void
name|testValueOfBigInteger
parameter_list|()
block|{
name|BigInteger
name|min
init|=
name|BigInteger
operator|.
name|ZERO
decl_stmt|;
name|BigInteger
name|max
init|=
name|UnsignedLong
operator|.
name|MAX_VALUE
operator|.
name|bigIntegerValue
argument_list|()
decl_stmt|;
for|for
control|(
name|BigInteger
name|big
range|:
name|TEST_BIG_INTEGERS
control|)
block|{
name|boolean
name|expectSuccess
init|=
name|big
operator|.
name|compareTo
argument_list|(
name|min
argument_list|)
operator|>=
literal|0
operator|&&
name|big
operator|.
name|compareTo
argument_list|(
name|max
argument_list|)
operator|<=
literal|0
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
name|big
argument_list|,
name|UnsignedLong
operator|.
name|valueOf
argument_list|(
name|big
argument_list|)
operator|.
name|bigIntegerValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|expectSuccess
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertFalse
argument_list|(
name|expectSuccess
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
for|for
control|(
name|long
name|value
range|:
name|TEST_LONGS
control|)
block|{
name|UnsignedLong
name|unsignedValue
init|=
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|unsignedValue
operator|.
name|bigIntegerValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|unsignedValue
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|GwtIncompatible
comment|// too slow
DECL|method|testToStringRadix ()
specifier|public
name|void
name|testToStringRadix
parameter_list|()
block|{
for|for
control|(
name|int
name|radix
init|=
name|Character
operator|.
name|MIN_RADIX
init|;
name|radix
operator|<=
name|Character
operator|.
name|MAX_RADIX
condition|;
name|radix
operator|++
control|)
block|{
for|for
control|(
name|long
name|l
range|:
name|TEST_LONGS
control|)
block|{
name|UnsignedLong
name|value
init|=
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|l
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|value
operator|.
name|bigIntegerValue
argument_list|()
operator|.
name|toString
argument_list|(
name|radix
argument_list|)
argument_list|,
name|value
operator|.
name|toString
argument_list|(
name|radix
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testToStringRadixQuick ()
specifier|public
name|void
name|testToStringRadixQuick
parameter_list|()
block|{
name|int
index|[]
name|radices
init|=
block|{
literal|2
block|,
literal|3
block|,
literal|5
block|,
literal|7
block|,
literal|10
block|,
literal|12
block|,
literal|16
block|,
literal|21
block|,
literal|31
block|,
literal|36
block|}
decl_stmt|;
for|for
control|(
name|int
name|radix
range|:
name|radices
control|)
block|{
for|for
control|(
name|long
name|l
range|:
name|TEST_LONGS
control|)
block|{
name|UnsignedLong
name|value
init|=
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|l
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|value
operator|.
name|bigIntegerValue
argument_list|()
operator|.
name|toString
argument_list|(
name|radix
argument_list|)
argument_list|,
name|value
operator|.
name|toString
argument_list|(
name|radix
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testFloatValue ()
specifier|public
name|void
name|testFloatValue
parameter_list|()
block|{
for|for
control|(
name|long
name|value
range|:
name|TEST_LONGS
control|)
block|{
name|UnsignedLong
name|unsignedValue
init|=
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|unsignedValue
operator|.
name|bigIntegerValue
argument_list|()
operator|.
name|floatValue
argument_list|()
argument_list|,
name|unsignedValue
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testDoubleValue ()
specifier|public
name|void
name|testDoubleValue
parameter_list|()
block|{
for|for
control|(
name|long
name|value
range|:
name|TEST_LONGS
control|)
block|{
name|UnsignedLong
name|unsignedValue
init|=
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|unsignedValue
operator|.
name|bigIntegerValue
argument_list|()
operator|.
name|doubleValue
argument_list|()
argument_list|,
name|unsignedValue
operator|.
name|doubleValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testPlus ()
specifier|public
name|void
name|testPlus
parameter_list|()
block|{
for|for
control|(
name|long
name|a
range|:
name|TEST_LONGS
control|)
block|{
for|for
control|(
name|long
name|b
range|:
name|TEST_LONGS
control|)
block|{
name|UnsignedLong
name|aUnsigned
init|=
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|UnsignedLong
name|bUnsigned
init|=
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|long
name|expected
init|=
name|aUnsigned
operator|.
name|bigIntegerValue
argument_list|()
operator|.
name|add
argument_list|(
name|bUnsigned
operator|.
name|bigIntegerValue
argument_list|()
argument_list|)
operator|.
name|longValue
argument_list|()
decl_stmt|;
name|UnsignedLong
name|unsignedSum
init|=
name|aUnsigned
operator|.
name|plus
argument_list|(
name|bUnsigned
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|unsignedSum
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testMinus ()
specifier|public
name|void
name|testMinus
parameter_list|()
block|{
for|for
control|(
name|long
name|a
range|:
name|TEST_LONGS
control|)
block|{
for|for
control|(
name|long
name|b
range|:
name|TEST_LONGS
control|)
block|{
name|UnsignedLong
name|aUnsigned
init|=
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|UnsignedLong
name|bUnsigned
init|=
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|long
name|expected
init|=
name|aUnsigned
operator|.
name|bigIntegerValue
argument_list|()
operator|.
name|subtract
argument_list|(
name|bUnsigned
operator|.
name|bigIntegerValue
argument_list|()
argument_list|)
operator|.
name|longValue
argument_list|()
decl_stmt|;
name|UnsignedLong
name|unsignedSub
init|=
name|aUnsigned
operator|.
name|minus
argument_list|(
name|bUnsigned
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|unsignedSub
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testTimes ()
specifier|public
name|void
name|testTimes
parameter_list|()
block|{
for|for
control|(
name|long
name|a
range|:
name|TEST_LONGS
control|)
block|{
for|for
control|(
name|long
name|b
range|:
name|TEST_LONGS
control|)
block|{
name|UnsignedLong
name|aUnsigned
init|=
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|UnsignedLong
name|bUnsigned
init|=
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|long
name|expected
init|=
name|aUnsigned
operator|.
name|bigIntegerValue
argument_list|()
operator|.
name|multiply
argument_list|(
name|bUnsigned
operator|.
name|bigIntegerValue
argument_list|()
argument_list|)
operator|.
name|longValue
argument_list|()
decl_stmt|;
name|UnsignedLong
name|unsignedMul
init|=
name|aUnsigned
operator|.
name|times
argument_list|(
name|bUnsigned
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|unsignedMul
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testDividedBy ()
specifier|public
name|void
name|testDividedBy
parameter_list|()
block|{
for|for
control|(
name|long
name|a
range|:
name|TEST_LONGS
control|)
block|{
for|for
control|(
name|long
name|b
range|:
name|TEST_LONGS
control|)
block|{
if|if
condition|(
name|b
operator|!=
literal|0
condition|)
block|{
name|UnsignedLong
name|aUnsigned
init|=
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|UnsignedLong
name|bUnsigned
init|=
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|long
name|expected
init|=
name|aUnsigned
operator|.
name|bigIntegerValue
argument_list|()
operator|.
name|divide
argument_list|(
name|bUnsigned
operator|.
name|bigIntegerValue
argument_list|()
argument_list|)
operator|.
name|longValue
argument_list|()
decl_stmt|;
name|UnsignedLong
name|unsignedDiv
init|=
name|aUnsigned
operator|.
name|dividedBy
argument_list|(
name|bUnsigned
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|unsignedDiv
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|testDivideByZeroThrows ()
specifier|public
name|void
name|testDivideByZeroThrows
parameter_list|()
block|{
for|for
control|(
name|long
name|a
range|:
name|TEST_LONGS
control|)
block|{
try|try
block|{
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|a
argument_list|)
operator|.
name|dividedBy
argument_list|(
name|UnsignedLong
operator|.
name|ZERO
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ArithmeticException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|expected
parameter_list|)
block|{       }
block|}
block|}
DECL|method|testMod ()
specifier|public
name|void
name|testMod
parameter_list|()
block|{
for|for
control|(
name|long
name|a
range|:
name|TEST_LONGS
control|)
block|{
for|for
control|(
name|long
name|b
range|:
name|TEST_LONGS
control|)
block|{
if|if
condition|(
name|b
operator|!=
literal|0
condition|)
block|{
name|UnsignedLong
name|aUnsigned
init|=
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|UnsignedLong
name|bUnsigned
init|=
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|long
name|expected
init|=
name|aUnsigned
operator|.
name|bigIntegerValue
argument_list|()
operator|.
name|remainder
argument_list|(
name|bUnsigned
operator|.
name|bigIntegerValue
argument_list|()
argument_list|)
operator|.
name|longValue
argument_list|()
decl_stmt|;
name|UnsignedLong
name|unsignedRem
init|=
name|aUnsigned
operator|.
name|mod
argument_list|(
name|bUnsigned
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|unsignedRem
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|testModByZero ()
specifier|public
name|void
name|testModByZero
parameter_list|()
block|{
for|for
control|(
name|long
name|a
range|:
name|TEST_LONGS
control|)
block|{
try|try
block|{
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|a
argument_list|)
operator|.
name|mod
argument_list|(
name|UnsignedLong
operator|.
name|ZERO
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ArithmeticException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|expected
parameter_list|)
block|{       }
block|}
block|}
DECL|method|testCompare ()
specifier|public
name|void
name|testCompare
parameter_list|()
block|{
for|for
control|(
name|long
name|a
range|:
name|TEST_LONGS
control|)
block|{
for|for
control|(
name|long
name|b
range|:
name|TEST_LONGS
control|)
block|{
name|UnsignedLong
name|aUnsigned
init|=
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|UnsignedLong
name|bUnsigned
init|=
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|aUnsigned
operator|.
name|bigIntegerValue
argument_list|()
operator|.
name|compareTo
argument_list|(
name|bUnsigned
operator|.
name|bigIntegerValue
argument_list|()
argument_list|)
argument_list|,
name|aUnsigned
operator|.
name|compareTo
argument_list|(
name|bUnsigned
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|GwtIncompatible
comment|// too slow
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
name|EqualsTester
name|equalsTester
init|=
operator|new
name|EqualsTester
argument_list|()
decl_stmt|;
for|for
control|(
name|long
name|a
range|:
name|TEST_LONGS
control|)
block|{
name|BigInteger
name|big
init|=
operator|(
name|a
operator|>=
literal|0
operator|)
condition|?
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|a
argument_list|)
else|:
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|a
argument_list|)
operator|.
name|add
argument_list|(
name|BigInteger
operator|.
name|ZERO
operator|.
name|setBit
argument_list|(
literal|64
argument_list|)
argument_list|)
decl_stmt|;
name|equalsTester
operator|.
name|addEqualityGroup
argument_list|(
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|a
argument_list|)
argument_list|,
name|UnsignedLong
operator|.
name|valueOf
argument_list|(
name|big
argument_list|)
argument_list|,
name|UnsignedLong
operator|.
name|valueOf
argument_list|(
name|big
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|UnsignedLong
operator|.
name|valueOf
argument_list|(
name|big
operator|.
name|toString
argument_list|(
literal|16
argument_list|)
argument_list|,
literal|16
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|equalsTester
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testIntValue ()
specifier|public
name|void
name|testIntValue
parameter_list|()
block|{
for|for
control|(
name|long
name|a
range|:
name|TEST_LONGS
control|)
block|{
name|UnsignedLong
name|aUnsigned
init|=
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|int
name|intValue
init|=
name|aUnsigned
operator|.
name|bigIntegerValue
argument_list|()
operator|.
name|intValue
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|intValue
argument_list|,
name|aUnsigned
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|GwtIncompatible
comment|// serialization
DECL|method|testSerialization ()
specifier|public
name|void
name|testSerialization
parameter_list|()
block|{
for|for
control|(
name|long
name|a
range|:
name|TEST_LONGS
control|)
block|{
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|UnsignedLong
operator|.
name|fromLongBits
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|GwtIncompatible
comment|// NullPointerTester
DECL|method|testNulls ()
specifier|public
name|void
name|testNulls
parameter_list|()
block|{
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|UnsignedLong
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

