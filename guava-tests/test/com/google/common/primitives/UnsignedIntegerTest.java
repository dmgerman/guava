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
name|math
operator|.
name|BigInteger
import|;
end_import

begin_comment
comment|/**  * Tests for {@code UnsignedInteger}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|UnsignedIntegerTest
specifier|public
class|class
name|UnsignedIntegerTest
extends|extends
name|TestCase
block|{
DECL|field|TEST_INTS
specifier|private
specifier|static
specifier|final
name|ImmutableSet
argument_list|<
name|Integer
argument_list|>
name|TEST_INTS
decl_stmt|;
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
DECL|method|force32 (int value)
specifier|private
specifier|static
name|int
name|force32
parameter_list|(
name|int
name|value
parameter_list|)
block|{
comment|// GWT doesn't consistently overflow values to make them 32-bit, so we need to force it.
return|return
name|value
operator|&
literal|0xffffffff
return|;
block|}
static|static
block|{
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|Integer
argument_list|>
name|testIntsBuilder
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
name|Long
argument_list|>
name|testLongsBuilder
init|=
name|ImmutableSet
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
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
name|testIntsBuilder
operator|.
name|add
argument_list|(
name|i
argument_list|)
operator|.
name|add
argument_list|(
name|force32
argument_list|(
name|Integer
operator|.
name|MIN_VALUE
operator|+
name|i
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|force32
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
operator|+
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|testLongsBuilder
operator|.
name|add
argument_list|(
operator|(
name|long
operator|)
name|i
argument_list|)
operator|.
name|add
argument_list|(
operator|(
name|long
operator|)
name|Integer
operator|.
name|MIN_VALUE
operator|+
name|i
argument_list|)
operator|.
name|add
argument_list|(
operator|(
name|long
operator|)
name|Integer
operator|.
name|MAX_VALUE
operator|+
name|i
argument_list|)
operator|.
name|add
argument_list|(
operator|(
literal|1L
operator|<<
literal|32
operator|)
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|TEST_INTS
operator|=
name|testIntsBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
name|TEST_LONGS
operator|=
name|testLongsBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
DECL|method|testFromIntBitsAndIntValueAreInverses ()
specifier|public
name|void
name|testFromIntBitsAndIntValueAreInverses
parameter_list|()
block|{
for|for
control|(
name|int
name|value
range|:
name|TEST_INTS
control|)
block|{
name|assertEquals
argument_list|(
name|UnsignedInts
operator|.
name|toString
argument_list|(
name|value
argument_list|)
argument_list|,
name|value
argument_list|,
name|UnsignedInteger
operator|.
name|fromIntBits
argument_list|(
name|value
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testFromIntBitsLongValue ()
specifier|public
name|void
name|testFromIntBitsLongValue
parameter_list|()
block|{
for|for
control|(
name|int
name|value
range|:
name|TEST_INTS
control|)
block|{
name|long
name|expected
init|=
name|value
operator|&
literal|0xffffffffL
decl_stmt|;
name|assertEquals
argument_list|(
name|UnsignedInts
operator|.
name|toString
argument_list|(
name|value
argument_list|)
argument_list|,
name|expected
argument_list|,
name|UnsignedInteger
operator|.
name|fromIntBits
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
DECL|method|testValueOfLong ()
specifier|public
name|void
name|testValueOfLong
parameter_list|()
block|{
name|long
name|min
init|=
literal|0
decl_stmt|;
name|long
name|max
init|=
operator|(
literal|1L
operator|<<
literal|32
operator|)
operator|-
literal|1
decl_stmt|;
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
name|min
operator|&&
name|value
operator|<=
name|max
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
name|value
argument_list|,
name|UnsignedInteger
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
name|long
name|min
init|=
literal|0
decl_stmt|;
name|long
name|max
init|=
operator|(
literal|1L
operator|<<
literal|32
operator|)
operator|-
literal|1
decl_stmt|;
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
name|min
operator|&&
name|value
operator|<=
name|max
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
name|value
argument_list|,
name|UnsignedInteger
operator|.
name|valueOf
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
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
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
for|for
control|(
name|int
name|value
range|:
name|TEST_INTS
control|)
block|{
name|UnsignedInteger
name|unsignedValue
init|=
name|UnsignedInteger
operator|.
name|fromIntBits
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
argument_list|(
literal|"too slow"
argument_list|)
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
name|int
name|l
range|:
name|TEST_INTS
control|)
block|{
name|UnsignedInteger
name|value
init|=
name|UnsignedInteger
operator|.
name|fromIntBits
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
name|int
name|l
range|:
name|TEST_INTS
control|)
block|{
name|UnsignedInteger
name|value
init|=
name|UnsignedInteger
operator|.
name|fromIntBits
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
name|int
name|value
range|:
name|TEST_INTS
control|)
block|{
name|UnsignedInteger
name|unsignedValue
init|=
name|UnsignedInteger
operator|.
name|fromIntBits
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
name|int
name|value
range|:
name|TEST_INTS
control|)
block|{
name|UnsignedInteger
name|unsignedValue
init|=
name|UnsignedInteger
operator|.
name|fromIntBits
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
name|int
name|a
range|:
name|TEST_INTS
control|)
block|{
for|for
control|(
name|int
name|b
range|:
name|TEST_INTS
control|)
block|{
name|UnsignedInteger
name|aUnsigned
init|=
name|UnsignedInteger
operator|.
name|fromIntBits
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|UnsignedInteger
name|bUnsigned
init|=
name|UnsignedInteger
operator|.
name|fromIntBits
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|int
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
name|intValue
argument_list|()
decl_stmt|;
name|UnsignedInteger
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
name|intValue
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
name|int
name|a
range|:
name|TEST_INTS
control|)
block|{
for|for
control|(
name|int
name|b
range|:
name|TEST_INTS
control|)
block|{
name|UnsignedInteger
name|aUnsigned
init|=
name|UnsignedInteger
operator|.
name|fromIntBits
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|UnsignedInteger
name|bUnsigned
init|=
name|UnsignedInteger
operator|.
name|fromIntBits
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|int
name|expected
init|=
name|force32
argument_list|(
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
name|intValue
argument_list|()
argument_list|)
decl_stmt|;
name|UnsignedInteger
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
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"multiply"
argument_list|)
DECL|method|testTimes ()
specifier|public
name|void
name|testTimes
parameter_list|()
block|{
for|for
control|(
name|int
name|a
range|:
name|TEST_INTS
control|)
block|{
for|for
control|(
name|int
name|b
range|:
name|TEST_INTS
control|)
block|{
name|UnsignedInteger
name|aUnsigned
init|=
name|UnsignedInteger
operator|.
name|fromIntBits
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|UnsignedInteger
name|bUnsigned
init|=
name|UnsignedInteger
operator|.
name|fromIntBits
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|int
name|expected
init|=
name|force32
argument_list|(
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
name|intValue
argument_list|()
argument_list|)
decl_stmt|;
name|UnsignedInteger
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
name|aUnsigned
operator|+
literal|" * "
operator|+
name|bUnsigned
argument_list|,
name|expected
argument_list|,
name|unsignedMul
operator|.
name|intValue
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
name|int
name|a
range|:
name|TEST_INTS
control|)
block|{
for|for
control|(
name|int
name|b
range|:
name|TEST_INTS
control|)
block|{
if|if
condition|(
name|b
operator|!=
literal|0
condition|)
block|{
name|UnsignedInteger
name|aUnsigned
init|=
name|UnsignedInteger
operator|.
name|fromIntBits
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|UnsignedInteger
name|bUnsigned
init|=
name|UnsignedInteger
operator|.
name|fromIntBits
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|int
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
name|intValue
argument_list|()
decl_stmt|;
name|UnsignedInteger
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
name|intValue
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
name|int
name|a
range|:
name|TEST_INTS
control|)
block|{
try|try
block|{
name|UnsignedInteger
operator|.
name|fromIntBits
argument_list|(
name|a
argument_list|)
operator|.
name|divide
argument_list|(
name|UnsignedInteger
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
block|{}
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
name|int
name|a
range|:
name|TEST_INTS
control|)
block|{
for|for
control|(
name|int
name|b
range|:
name|TEST_INTS
control|)
block|{
if|if
condition|(
name|b
operator|!=
literal|0
condition|)
block|{
name|UnsignedInteger
name|aUnsigned
init|=
name|UnsignedInteger
operator|.
name|fromIntBits
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|UnsignedInteger
name|bUnsigned
init|=
name|UnsignedInteger
operator|.
name|fromIntBits
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|int
name|expected
init|=
name|aUnsigned
operator|.
name|bigIntegerValue
argument_list|()
operator|.
name|mod
argument_list|(
name|bUnsigned
operator|.
name|bigIntegerValue
argument_list|()
argument_list|)
operator|.
name|intValue
argument_list|()
decl_stmt|;
name|UnsignedInteger
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
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"ReturnValueIgnored"
argument_list|)
DECL|method|testModByZero ()
specifier|public
name|void
name|testModByZero
parameter_list|()
block|{
for|for
control|(
name|int
name|a
range|:
name|TEST_INTS
control|)
block|{
try|try
block|{
name|UnsignedInteger
operator|.
name|fromIntBits
argument_list|(
name|a
argument_list|)
operator|.
name|mod
argument_list|(
name|UnsignedInteger
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
block|{}
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
name|int
name|a
range|:
name|TEST_INTS
control|)
block|{
for|for
control|(
name|int
name|b
range|:
name|TEST_INTS
control|)
block|{
name|UnsignedInteger
name|aUnsigned
init|=
name|UnsignedInteger
operator|.
name|fromIntBits
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|UnsignedInteger
name|bUnsigned
init|=
name|UnsignedInteger
operator|.
name|fromIntBits
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
argument_list|(
literal|"too slow"
argument_list|)
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
name|int
name|a
range|:
name|TEST_INTS
control|)
block|{
name|long
name|value
init|=
name|a
operator|&
literal|0xffffffffL
decl_stmt|;
name|equalsTester
operator|.
name|addEqualityGroup
argument_list|(
name|UnsignedInteger
operator|.
name|fromIntBits
argument_list|(
name|a
argument_list|)
argument_list|,
name|UnsignedInteger
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|,
name|UnsignedInteger
operator|.
name|valueOf
argument_list|(
name|Long
operator|.
name|toString
argument_list|(
name|value
argument_list|)
argument_list|)
argument_list|,
name|UnsignedInteger
operator|.
name|valueOf
argument_list|(
name|Long
operator|.
name|toString
argument_list|(
name|value
argument_list|,
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
name|int
name|a
range|:
name|TEST_INTS
control|)
block|{
name|UnsignedInteger
name|aUnsigned
init|=
name|UnsignedInteger
operator|.
name|fromIntBits
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
argument_list|(
literal|"serialization"
argument_list|)
DECL|method|testSerialization ()
specifier|public
name|void
name|testSerialization
parameter_list|()
block|{
for|for
control|(
name|int
name|a
range|:
name|TEST_INTS
control|)
block|{
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|UnsignedInteger
operator|.
name|fromIntBits
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"NullPointerTester"
argument_list|)
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
name|UnsignedInteger
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

