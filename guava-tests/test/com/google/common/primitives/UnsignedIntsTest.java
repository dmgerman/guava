begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *   * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software distributed under the  * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  * express or implied. See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|Random
import|;
end_import

begin_comment
comment|/**  * Tests for UnsignedInts  *   * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|UnsignedIntsTest
specifier|public
class|class
name|UnsignedIntsTest
extends|extends
name|TestCase
block|{
DECL|field|UNSIGNED_INTS
specifier|private
specifier|static
specifier|final
name|long
index|[]
name|UNSIGNED_INTS
init|=
block|{
literal|0L
block|,
literal|1L
block|,
literal|2L
block|,
literal|3L
block|,
literal|0x12345678L
block|,
literal|0x5a4316b8L
block|,
literal|0x6cf78a4bL
block|,
literal|0xff1a618bL
block|,
literal|0xfffffffdL
block|,
literal|0xfffffffeL
block|,
literal|0xffffffffL
block|}
decl_stmt|;
DECL|method|testToLong ()
specifier|public
name|void
name|testToLong
parameter_list|()
block|{
for|for
control|(
name|long
name|a
range|:
name|UNSIGNED_INTS
control|)
block|{
name|assertEquals
argument_list|(
name|a
argument_list|,
name|UnsignedInts
operator|.
name|toLong
argument_list|(
operator|(
name|int
operator|)
name|a
argument_list|)
argument_list|)
expr_stmt|;
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
name|UNSIGNED_INTS
control|)
block|{
for|for
control|(
name|long
name|b
range|:
name|UNSIGNED_INTS
control|)
block|{
name|int
name|cmpAsLongs
init|=
name|Longs
operator|.
name|compare
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
decl_stmt|;
name|int
name|cmpAsUInt
init|=
name|UnsignedInts
operator|.
name|compare
argument_list|(
operator|(
name|int
operator|)
name|a
argument_list|,
operator|(
name|int
operator|)
name|b
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|signum
argument_list|(
name|cmpAsLongs
argument_list|)
argument_list|,
name|Integer
operator|.
name|signum
argument_list|(
name|cmpAsUInt
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testDivide ()
specifier|public
name|void
name|testDivide
parameter_list|()
block|{
for|for
control|(
name|long
name|a
range|:
name|UNSIGNED_INTS
control|)
block|{
for|for
control|(
name|long
name|b
range|:
name|UNSIGNED_INTS
control|)
block|{
try|try
block|{
name|assertEquals
argument_list|(
call|(
name|int
call|)
argument_list|(
name|a
operator|/
name|b
argument_list|)
argument_list|,
name|UnsignedInts
operator|.
name|divide
argument_list|(
operator|(
name|int
operator|)
name|a
argument_list|,
operator|(
name|int
operator|)
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|b
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|testRemainder ()
specifier|public
name|void
name|testRemainder
parameter_list|()
block|{
for|for
control|(
name|long
name|a
range|:
name|UNSIGNED_INTS
control|)
block|{
for|for
control|(
name|long
name|b
range|:
name|UNSIGNED_INTS
control|)
block|{
try|try
block|{
name|assertEquals
argument_list|(
call|(
name|int
call|)
argument_list|(
name|a
operator|%
name|b
argument_list|)
argument_list|,
name|UnsignedInts
operator|.
name|remainder
argument_list|(
operator|(
name|int
operator|)
name|a
argument_list|,
operator|(
name|int
operator|)
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|b
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"Too slow in GWT (~3min fully optimized)"
argument_list|)
DECL|method|testDivideRemainderEuclideanProperty ()
specifier|public
name|void
name|testDivideRemainderEuclideanProperty
parameter_list|()
block|{
comment|// Use a seed so that the test is deterministic:
name|Random
name|r
init|=
operator|new
name|Random
argument_list|(
literal|0L
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
literal|1000000
condition|;
name|i
operator|++
control|)
block|{
name|int
name|dividend
init|=
name|r
operator|.
name|nextInt
argument_list|()
decl_stmt|;
name|int
name|divisor
init|=
name|r
operator|.
name|nextInt
argument_list|()
decl_stmt|;
comment|// Test that the Euclidean property is preserved:
name|assertTrue
argument_list|(
name|dividend
operator|-
operator|(
name|divisor
operator|*
name|UnsignedInts
operator|.
name|divide
argument_list|(
name|dividend
argument_list|,
name|divisor
argument_list|)
operator|+
name|UnsignedInts
operator|.
name|remainder
argument_list|(
name|dividend
argument_list|,
name|divisor
argument_list|)
operator|)
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testParseInt ()
specifier|public
name|void
name|testParseInt
parameter_list|()
block|{
try|try
block|{
for|for
control|(
name|long
name|a
range|:
name|UNSIGNED_INTS
control|)
block|{
name|assertEquals
argument_list|(
operator|(
name|int
operator|)
name|a
argument_list|,
name|UnsignedInts
operator|.
name|parseUnsignedInt
argument_list|(
name|Long
operator|.
name|toString
argument_list|(
name|a
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|UnsignedInts
operator|.
name|parseUnsignedInt
argument_list|(
name|Long
operator|.
name|toString
argument_list|(
literal|1L
operator|<<
literal|32
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected NumberFormatException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testParseLongWithRadix ()
specifier|public
name|void
name|testParseLongWithRadix
parameter_list|()
throws|throws
name|NumberFormatException
block|{
for|for
control|(
name|long
name|a
range|:
name|UNSIGNED_INTS
control|)
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
name|assertEquals
argument_list|(
operator|(
name|int
operator|)
name|a
argument_list|,
name|UnsignedInts
operator|.
name|parseUnsignedInt
argument_list|(
name|Long
operator|.
name|toString
argument_list|(
name|a
argument_list|,
name|radix
argument_list|)
argument_list|,
name|radix
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// loops through all legal radix values.
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
comment|// tests can successfully parse a number string with this radix.
name|String
name|maxAsString
init|=
name|Long
operator|.
name|toString
argument_list|(
operator|(
literal|1L
operator|<<
literal|32
operator|)
operator|-
literal|1
argument_list|,
name|radix
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|UnsignedInts
operator|.
name|parseUnsignedInt
argument_list|(
name|maxAsString
argument_list|,
name|radix
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
comment|// tests that we get exception whre an overflow would occur.
name|long
name|overflow
init|=
literal|1L
operator|<<
literal|32
decl_stmt|;
name|String
name|overflowAsString
init|=
name|Long
operator|.
name|toString
argument_list|(
name|overflow
argument_list|,
name|radix
argument_list|)
decl_stmt|;
name|UnsignedInts
operator|.
name|parseUnsignedInt
argument_list|(
name|overflowAsString
argument_list|,
name|radix
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|expected
parameter_list|)
block|{}
block|}
block|}
DECL|method|testParseLongThrowsExceptionForInvalidRadix ()
specifier|public
name|void
name|testParseLongThrowsExceptionForInvalidRadix
parameter_list|()
block|{
comment|// Valid radix values are Character.MIN_RADIX to Character.MAX_RADIX,
comment|// inclusive.
try|try
block|{
name|UnsignedInts
operator|.
name|parseUnsignedInt
argument_list|(
literal|"0"
argument_list|,
name|Character
operator|.
name|MIN_RADIX
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
name|NumberFormatException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|UnsignedInts
operator|.
name|parseUnsignedInt
argument_list|(
literal|"0"
argument_list|,
name|Character
operator|.
name|MAX_RADIX
operator|+
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|expected
parameter_list|)
block|{}
comment|// The radix is used as an array index, so try a negative value.
try|try
block|{
name|UnsignedInts
operator|.
name|parseUnsignedInt
argument_list|(
literal|"0"
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
name|NumberFormatException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|int
index|[]
name|bases
init|=
block|{
literal|2
block|,
literal|5
block|,
literal|7
block|,
literal|8
block|,
literal|10
block|,
literal|16
block|}
decl_stmt|;
for|for
control|(
name|long
name|a
range|:
name|UNSIGNED_INTS
control|)
block|{
for|for
control|(
name|int
name|base
range|:
name|bases
control|)
block|{
name|assertEquals
argument_list|(
name|UnsignedInts
operator|.
name|toString
argument_list|(
operator|(
name|int
operator|)
name|a
argument_list|,
name|base
argument_list|)
argument_list|,
name|Long
operator|.
name|toString
argument_list|(
name|a
argument_list|,
name|base
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
throws|throws
name|Exception
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
name|setDefault
argument_list|(
name|int
index|[]
operator|.
expr|class
argument_list|,
operator|new
name|int
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|tester
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|UnsignedInts
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

