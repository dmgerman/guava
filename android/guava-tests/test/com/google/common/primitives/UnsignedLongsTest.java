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
import|import static
name|java
operator|.
name|math
operator|.
name|BigInteger
operator|.
name|ONE
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
name|testing
operator|.
name|Helpers
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
name|java
operator|.
name|math
operator|.
name|BigInteger
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
name|Comparator
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
comment|/**  * Tests for UnsignedLongs  *  * @author Brian Milch  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|UnsignedLongsTest
specifier|public
class|class
name|UnsignedLongsTest
extends|extends
name|TestCase
block|{
DECL|field|LEAST
specifier|private
specifier|static
specifier|final
name|long
name|LEAST
init|=
literal|0L
decl_stmt|;
DECL|field|GREATEST
specifier|private
specifier|static
specifier|final
name|long
name|GREATEST
init|=
literal|0xffffffffffffffffL
decl_stmt|;
DECL|method|testCompare ()
specifier|public
name|void
name|testCompare
parameter_list|()
block|{
comment|// max value
name|assertTrue
argument_list|(
name|UnsignedLongs
operator|.
name|compare
argument_list|(
literal|0
argument_list|,
literal|0xffffffffffffffffL
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|UnsignedLongs
operator|.
name|compare
argument_list|(
literal|0xffffffffffffffffL
argument_list|,
literal|0
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// both with high bit set
name|assertTrue
argument_list|(
name|UnsignedLongs
operator|.
name|compare
argument_list|(
literal|0xff1a618b7f65ea12L
argument_list|,
literal|0xffffffffffffffffL
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|UnsignedLongs
operator|.
name|compare
argument_list|(
literal|0xffffffffffffffffL
argument_list|,
literal|0xff1a618b7f65ea12L
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// one with high bit set
name|assertTrue
argument_list|(
name|UnsignedLongs
operator|.
name|compare
argument_list|(
literal|0x5a4316b8c153ac4dL
argument_list|,
literal|0xff1a618b7f65ea12L
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|UnsignedLongs
operator|.
name|compare
argument_list|(
literal|0xff1a618b7f65ea12L
argument_list|,
literal|0x5a4316b8c153ac4dL
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// neither with high bit set
name|assertTrue
argument_list|(
name|UnsignedLongs
operator|.
name|compare
argument_list|(
literal|0x5a4316b8c153ac4dL
argument_list|,
literal|0x6cf78a4b139a4e2aL
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|UnsignedLongs
operator|.
name|compare
argument_list|(
literal|0x6cf78a4b139a4e2aL
argument_list|,
literal|0x5a4316b8c153ac4dL
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// same value
name|assertTrue
argument_list|(
name|UnsignedLongs
operator|.
name|compare
argument_list|(
literal|0xff1a618b7f65ea12L
argument_list|,
literal|0xff1a618b7f65ea12L
argument_list|)
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|testMax_noArgs ()
specifier|public
name|void
name|testMax_noArgs
parameter_list|()
block|{
try|try
block|{
name|UnsignedLongs
operator|.
name|max
argument_list|()
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
DECL|method|testMax ()
specifier|public
name|void
name|testMax
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|LEAST
argument_list|,
name|UnsignedLongs
operator|.
name|max
argument_list|(
name|LEAST
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GREATEST
argument_list|,
name|UnsignedLongs
operator|.
name|max
argument_list|(
name|GREATEST
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0xff1a618b7f65ea12L
argument_list|,
name|UnsignedLongs
operator|.
name|max
argument_list|(
literal|0x5a4316b8c153ac4dL
argument_list|,
literal|8L
argument_list|,
literal|100L
argument_list|,
literal|0L
argument_list|,
literal|0x6cf78a4b139a4e2aL
argument_list|,
literal|0xff1a618b7f65ea12L
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testMin_noArgs ()
specifier|public
name|void
name|testMin_noArgs
parameter_list|()
block|{
try|try
block|{
name|UnsignedLongs
operator|.
name|min
argument_list|()
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
DECL|method|testMin ()
specifier|public
name|void
name|testMin
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|LEAST
argument_list|,
name|UnsignedLongs
operator|.
name|min
argument_list|(
name|LEAST
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GREATEST
argument_list|,
name|UnsignedLongs
operator|.
name|min
argument_list|(
name|GREATEST
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0L
argument_list|,
name|UnsignedLongs
operator|.
name|min
argument_list|(
literal|0x5a4316b8c153ac4dL
argument_list|,
literal|8L
argument_list|,
literal|100L
argument_list|,
literal|0L
argument_list|,
literal|0x6cf78a4b139a4e2aL
argument_list|,
literal|0xff1a618b7f65ea12L
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLexicographicalComparator ()
specifier|public
name|void
name|testLexicographicalComparator
parameter_list|()
block|{
name|List
argument_list|<
name|long
index|[]
argument_list|>
name|ordered
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|long
index|[]
block|{}
argument_list|,
operator|new
name|long
index|[]
block|{
name|LEAST
block|}
argument_list|,
operator|new
name|long
index|[]
block|{
name|LEAST
block|,
name|LEAST
block|}
argument_list|,
operator|new
name|long
index|[]
block|{
name|LEAST
block|,
operator|(
name|long
operator|)
literal|1
block|}
argument_list|,
operator|new
name|long
index|[]
block|{
operator|(
name|long
operator|)
literal|1
block|}
argument_list|,
operator|new
name|long
index|[]
block|{
operator|(
name|long
operator|)
literal|1
block|,
name|LEAST
block|}
argument_list|,
operator|new
name|long
index|[]
block|{
name|GREATEST
block|,
name|GREATEST
operator|-
operator|(
name|long
operator|)
literal|1
block|}
argument_list|,
operator|new
name|long
index|[]
block|{
name|GREATEST
block|,
name|GREATEST
block|}
argument_list|,
operator|new
name|long
index|[]
block|{
name|GREATEST
block|,
name|GREATEST
block|,
name|GREATEST
block|}
argument_list|)
decl_stmt|;
name|Comparator
argument_list|<
name|long
index|[]
argument_list|>
name|comparator
init|=
name|UnsignedLongs
operator|.
name|lexicographicalComparator
argument_list|()
decl_stmt|;
name|Helpers
operator|.
name|testComparator
argument_list|(
name|comparator
argument_list|,
name|ordered
argument_list|)
expr_stmt|;
block|}
DECL|method|testDivide ()
specifier|public
name|void
name|testDivide
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|UnsignedLongs
operator|.
name|divide
argument_list|(
literal|14
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|UnsignedLongs
operator|.
name|divide
argument_list|(
literal|0
argument_list|,
literal|50
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|UnsignedLongs
operator|.
name|divide
argument_list|(
literal|0xfffffffffffffffeL
argument_list|,
literal|0xfffffffffffffffdL
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|UnsignedLongs
operator|.
name|divide
argument_list|(
literal|0xfffffffffffffffdL
argument_list|,
literal|0xfffffffffffffffeL
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|281479271743488L
argument_list|,
name|UnsignedLongs
operator|.
name|divide
argument_list|(
literal|0xfffffffffffffffeL
argument_list|,
literal|65535
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x7fffffffffffffffL
argument_list|,
name|UnsignedLongs
operator|.
name|divide
argument_list|(
literal|0xfffffffffffffffeL
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3689348814741910322L
argument_list|,
name|UnsignedLongs
operator|.
name|divide
argument_list|(
literal|0xfffffffffffffffeL
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testRemainder ()
specifier|public
name|void
name|testRemainder
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|UnsignedLongs
operator|.
name|remainder
argument_list|(
literal|14
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|UnsignedLongs
operator|.
name|remainder
argument_list|(
literal|0
argument_list|,
literal|50
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|UnsignedLongs
operator|.
name|remainder
argument_list|(
literal|0xfffffffffffffffeL
argument_list|,
literal|0xfffffffffffffffdL
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0xfffffffffffffffdL
argument_list|,
name|UnsignedLongs
operator|.
name|remainder
argument_list|(
literal|0xfffffffffffffffdL
argument_list|,
literal|0xfffffffffffffffeL
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|65534L
argument_list|,
name|UnsignedLongs
operator|.
name|remainder
argument_list|(
literal|0xfffffffffffffffeL
argument_list|,
literal|65535
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|UnsignedLongs
operator|.
name|remainder
argument_list|(
literal|0xfffffffffffffffeL
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|UnsignedLongs
operator|.
name|remainder
argument_list|(
literal|0xfffffffffffffffeL
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// Too slow in GWT (~3min fully optimized)
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
name|long
name|dividend
init|=
name|r
operator|.
name|nextLong
argument_list|()
decl_stmt|;
name|long
name|divisor
init|=
name|r
operator|.
name|nextLong
argument_list|()
decl_stmt|;
comment|// Test that the Euclidean property is preserved:
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|dividend
operator|-
operator|(
name|divisor
operator|*
name|UnsignedLongs
operator|.
name|divide
argument_list|(
name|dividend
argument_list|,
name|divisor
argument_list|)
operator|+
name|UnsignedLongs
operator|.
name|remainder
argument_list|(
name|dividend
argument_list|,
name|divisor
argument_list|)
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testParseLong ()
specifier|public
name|void
name|testParseLong
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0xffffffffffffffffL
argument_list|,
name|UnsignedLongs
operator|.
name|parseUnsignedLong
argument_list|(
literal|"18446744073709551615"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x7fffffffffffffffL
argument_list|,
name|UnsignedLongs
operator|.
name|parseUnsignedLong
argument_list|(
literal|"9223372036854775807"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0xff1a618b7f65ea12L
argument_list|,
name|UnsignedLongs
operator|.
name|parseUnsignedLong
argument_list|(
literal|"18382112080831834642"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x5a4316b8c153ac4dL
argument_list|,
name|UnsignedLongs
operator|.
name|parseUnsignedLong
argument_list|(
literal|"6504067269626408013"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x6cf78a4b139a4e2aL
argument_list|,
name|UnsignedLongs
operator|.
name|parseUnsignedLong
argument_list|(
literal|"7851896530399809066"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseLongFails ()
specifier|public
name|void
name|testParseLongFails
parameter_list|()
block|{
try|try
block|{
comment|// One more than maximum value
name|UnsignedLongs
operator|.
name|parseUnsignedLong
argument_list|(
literal|"18446744073709551616"
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
block|{     }
block|}
DECL|method|testDecodeLong ()
specifier|public
name|void
name|testDecodeLong
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0xffffffffffffffffL
argument_list|,
name|UnsignedLongs
operator|.
name|decode
argument_list|(
literal|"0xffffffffffffffff"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|01234567
argument_list|,
name|UnsignedLongs
operator|.
name|decode
argument_list|(
literal|"01234567"
argument_list|)
argument_list|)
expr_stmt|;
comment|// octal
name|assertEquals
argument_list|(
literal|0x1234567890abcdefL
argument_list|,
name|UnsignedLongs
operator|.
name|decode
argument_list|(
literal|"#1234567890abcdef"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|987654321012345678L
argument_list|,
name|UnsignedLongs
operator|.
name|decode
argument_list|(
literal|"987654321012345678"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x135791357913579L
argument_list|,
name|UnsignedLongs
operator|.
name|decode
argument_list|(
literal|"0x135791357913579"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x135791357913579L
argument_list|,
name|UnsignedLongs
operator|.
name|decode
argument_list|(
literal|"0X135791357913579"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0L
argument_list|,
name|UnsignedLongs
operator|.
name|decode
argument_list|(
literal|"0"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testDecodeLongFails ()
specifier|public
name|void
name|testDecodeLongFails
parameter_list|()
block|{
try|try
block|{
comment|// One more than maximum value
name|UnsignedLongs
operator|.
name|decode
argument_list|(
literal|"0xfffffffffffffffff"
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
block|{     }
try|try
block|{
name|UnsignedLongs
operator|.
name|decode
argument_list|(
literal|"-5"
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
block|{     }
try|try
block|{
name|UnsignedLongs
operator|.
name|decode
argument_list|(
literal|"-0x5"
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
block|{     }
try|try
block|{
name|UnsignedLongs
operator|.
name|decode
argument_list|(
literal|"-05"
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
block|{     }
block|}
DECL|method|testParseLongWithRadix ()
specifier|public
name|void
name|testParseLongWithRadix
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0xffffffffffffffffL
argument_list|,
name|UnsignedLongs
operator|.
name|parseUnsignedLong
argument_list|(
literal|"ffffffffffffffff"
argument_list|,
literal|16
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x1234567890abcdefL
argument_list|,
name|UnsignedLongs
operator|.
name|parseUnsignedLong
argument_list|(
literal|"1234567890abcdef"
argument_list|,
literal|16
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseLongWithRadixLimits ()
specifier|public
name|void
name|testParseLongWithRadixLimits
parameter_list|()
block|{
name|BigInteger
name|max
init|=
name|BigInteger
operator|.
name|ZERO
operator|.
name|setBit
argument_list|(
literal|64
argument_list|)
operator|.
name|subtract
argument_list|(
name|ONE
argument_list|)
decl_stmt|;
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
name|max
operator|.
name|toString
argument_list|(
name|radix
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|max
operator|.
name|longValue
argument_list|()
argument_list|,
name|UnsignedLongs
operator|.
name|parseUnsignedLong
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
name|BigInteger
name|overflow
init|=
name|max
operator|.
name|add
argument_list|(
name|ONE
argument_list|)
decl_stmt|;
name|String
name|overflowAsString
init|=
name|overflow
operator|.
name|toString
argument_list|(
name|radix
argument_list|)
decl_stmt|;
name|UnsignedLongs
operator|.
name|parseUnsignedLong
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
block|{       }
block|}
try|try
block|{
name|UnsignedLongs
operator|.
name|parseUnsignedLong
argument_list|(
literal|"1234567890abcdef1"
argument_list|,
literal|16
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
block|{     }
block|}
DECL|method|testParseLongThrowsExceptionForInvalidRadix ()
specifier|public
name|void
name|testParseLongThrowsExceptionForInvalidRadix
parameter_list|()
block|{
comment|// Valid radix values are Character.MIN_RADIX to Character.MAX_RADIX, inclusive.
try|try
block|{
name|UnsignedLongs
operator|.
name|parseUnsignedLong
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
block|{     }
try|try
block|{
name|UnsignedLongs
operator|.
name|parseUnsignedLong
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
block|{     }
comment|// The radix is used as an array index, so try a negative value.
try|try
block|{
name|UnsignedLongs
operator|.
name|parseUnsignedLong
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
block|{     }
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|String
index|[]
name|tests
init|=
block|{
literal|"ffffffffffffffff"
block|,
literal|"7fffffffffffffff"
block|,
literal|"ff1a618b7f65ea12"
block|,
literal|"5a4316b8c153ac4d"
block|,
literal|"6cf78a4b139a4e2a"
block|}
decl_stmt|;
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
name|int
name|base
range|:
name|bases
control|)
block|{
for|for
control|(
name|String
name|x
range|:
name|tests
control|)
block|{
name|BigInteger
name|xValue
init|=
operator|new
name|BigInteger
argument_list|(
name|x
argument_list|,
literal|16
argument_list|)
decl_stmt|;
name|long
name|xLong
init|=
name|xValue
operator|.
name|longValue
argument_list|()
decl_stmt|;
comment|// signed
name|assertEquals
argument_list|(
name|xValue
operator|.
name|toString
argument_list|(
name|base
argument_list|)
argument_list|,
name|UnsignedLongs
operator|.
name|toString
argument_list|(
name|xLong
argument_list|,
name|base
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testJoin ()
specifier|public
name|void
name|testJoin
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|UnsignedLongs
operator|.
name|join
argument_list|(
literal|","
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|UnsignedLongs
operator|.
name|join
argument_list|(
literal|","
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1,2"
argument_list|,
name|UnsignedLongs
operator|.
name|join
argument_list|(
literal|","
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"18446744073709551615,9223372036854775808"
argument_list|,
name|UnsignedLongs
operator|.
name|join
argument_list|(
literal|","
argument_list|,
operator|-
literal|1
argument_list|,
name|Long
operator|.
name|MIN_VALUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|UnsignedLongs
operator|.
name|join
argument_list|(
literal|""
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"184467440737095516159223372036854775808"
argument_list|,
name|UnsignedLongs
operator|.
name|join
argument_list|(
literal|""
argument_list|,
operator|-
literal|1
argument_list|,
name|Long
operator|.
name|MIN_VALUE
argument_list|)
argument_list|)
expr_stmt|;
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
name|UnsignedLongs
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
