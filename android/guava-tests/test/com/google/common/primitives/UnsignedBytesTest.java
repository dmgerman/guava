begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Unit test for {@link UnsignedBytes}.  *  * @author Kevin Bourrillion  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|UnsignedBytesTest
specifier|public
class|class
name|UnsignedBytesTest
extends|extends
name|TestCase
block|{
DECL|field|LEAST
specifier|private
specifier|static
specifier|final
name|byte
name|LEAST
init|=
literal|0
decl_stmt|;
DECL|field|GREATEST
specifier|private
specifier|static
specifier|final
name|byte
name|GREATEST
init|=
operator|(
name|byte
operator|)
literal|255
decl_stmt|;
comment|// Only in this class, VALUES must be strictly ascending
DECL|field|VALUES
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|VALUES
init|=
block|{
name|LEAST
block|,
literal|127
block|,
operator|(
name|byte
operator|)
literal|128
block|,
operator|(
name|byte
operator|)
literal|129
block|,
name|GREATEST
block|}
decl_stmt|;
DECL|method|testToInt ()
specifier|public
name|void
name|testToInt
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|UnsignedBytes
operator|.
name|toInt
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|UnsignedBytes
operator|.
name|toInt
argument_list|(
operator|(
name|byte
operator|)
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|127
argument_list|,
name|UnsignedBytes
operator|.
name|toInt
argument_list|(
operator|(
name|byte
operator|)
literal|127
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|128
argument_list|,
name|UnsignedBytes
operator|.
name|toInt
argument_list|(
operator|(
name|byte
operator|)
operator|-
literal|128
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|129
argument_list|,
name|UnsignedBytes
operator|.
name|toInt
argument_list|(
operator|(
name|byte
operator|)
operator|-
literal|127
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|255
argument_list|,
name|UnsignedBytes
operator|.
name|toInt
argument_list|(
operator|(
name|byte
operator|)
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCheckedCast ()
specifier|public
name|void
name|testCheckedCast
parameter_list|()
block|{
for|for
control|(
name|byte
name|value
range|:
name|VALUES
control|)
block|{
name|assertEquals
argument_list|(
name|value
argument_list|,
name|UnsignedBytes
operator|.
name|checkedCast
argument_list|(
name|UnsignedBytes
operator|.
name|toInt
argument_list|(
name|value
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertCastFails
argument_list|(
literal|256L
argument_list|)
expr_stmt|;
name|assertCastFails
argument_list|(
operator|-
literal|1L
argument_list|)
expr_stmt|;
name|assertCastFails
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
name|assertCastFails
argument_list|(
name|Long
operator|.
name|MIN_VALUE
argument_list|)
expr_stmt|;
block|}
DECL|method|testSaturatedCast ()
specifier|public
name|void
name|testSaturatedCast
parameter_list|()
block|{
for|for
control|(
name|byte
name|value
range|:
name|VALUES
control|)
block|{
name|assertEquals
argument_list|(
name|value
argument_list|,
name|UnsignedBytes
operator|.
name|saturatedCast
argument_list|(
name|UnsignedBytes
operator|.
name|toInt
argument_list|(
name|value
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|GREATEST
argument_list|,
name|UnsignedBytes
operator|.
name|saturatedCast
argument_list|(
literal|256L
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LEAST
argument_list|,
name|UnsignedBytes
operator|.
name|saturatedCast
argument_list|(
operator|-
literal|1L
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GREATEST
argument_list|,
name|UnsignedBytes
operator|.
name|saturatedCast
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LEAST
argument_list|,
name|UnsignedBytes
operator|.
name|saturatedCast
argument_list|(
name|Long
operator|.
name|MIN_VALUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertCastFails (long value)
specifier|private
specifier|static
name|void
name|assertCastFails
parameter_list|(
name|long
name|value
parameter_list|)
block|{
try|try
block|{
name|UnsignedBytes
operator|.
name|checkedCast
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Cast to byte should have failed: "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|value
operator|+
literal|" not found in exception text: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
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
comment|// This is the only ordering for primitives that does not have a
comment|// corresponding Comparable wrapper in java.lang.
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|VALUES
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|VALUES
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|byte
name|x
init|=
name|VALUES
index|[
name|i
index|]
decl_stmt|;
name|byte
name|y
init|=
name|VALUES
index|[
name|j
index|]
decl_stmt|;
comment|// note: spec requires only that the sign is the same
name|assertEquals
argument_list|(
name|x
operator|+
literal|", "
operator|+
name|y
argument_list|,
name|Math
operator|.
name|signum
argument_list|(
name|UnsignedBytes
operator|.
name|compare
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
argument_list|)
argument_list|,
name|Math
operator|.
name|signum
argument_list|(
name|Ints
operator|.
name|compare
argument_list|(
name|i
argument_list|,
name|j
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testMax_noArgs ()
specifier|public
name|void
name|testMax_noArgs
parameter_list|()
block|{
try|try
block|{
name|UnsignedBytes
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
name|UnsignedBytes
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
name|UnsignedBytes
operator|.
name|max
argument_list|(
name|GREATEST
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|255
argument_list|,
name|UnsignedBytes
operator|.
name|max
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|,
operator|(
name|byte
operator|)
operator|-
literal|128
argument_list|,
operator|(
name|byte
operator|)
operator|-
literal|1
argument_list|,
operator|(
name|byte
operator|)
literal|127
argument_list|,
operator|(
name|byte
operator|)
literal|1
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
name|UnsignedBytes
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
name|UnsignedBytes
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
name|UnsignedBytes
operator|.
name|min
argument_list|(
name|GREATEST
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|,
name|UnsignedBytes
operator|.
name|min
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|,
operator|(
name|byte
operator|)
operator|-
literal|128
argument_list|,
operator|(
name|byte
operator|)
operator|-
literal|1
argument_list|,
operator|(
name|byte
operator|)
literal|127
argument_list|,
operator|(
name|byte
operator|)
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|,
name|UnsignedBytes
operator|.
name|min
argument_list|(
operator|(
name|byte
operator|)
operator|-
literal|1
argument_list|,
operator|(
name|byte
operator|)
literal|127
argument_list|,
operator|(
name|byte
operator|)
literal|1
argument_list|,
operator|(
name|byte
operator|)
operator|-
literal|128
argument_list|,
operator|(
name|byte
operator|)
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertParseFails (String value)
specifier|private
specifier|static
name|void
name|assertParseFails
parameter_list|(
name|String
name|value
parameter_list|)
block|{
try|try
block|{
name|UnsignedBytes
operator|.
name|parseUnsignedByte
argument_list|(
name|value
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
DECL|method|testParseUnsignedByte ()
specifier|public
name|void
name|testParseUnsignedByte
parameter_list|()
block|{
comment|// We can easily afford to test this exhaustively.
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<=
literal|0xff
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
name|i
argument_list|,
name|UnsignedBytes
operator|.
name|parseUnsignedByte
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertParseFails
argument_list|(
literal|"1000"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"-1"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"-128"
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
literal|"256"
argument_list|)
expr_stmt|;
block|}
DECL|method|testMaxValue ()
specifier|public
name|void
name|testMaxValue
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|UnsignedBytes
operator|.
name|compare
argument_list|(
name|UnsignedBytes
operator|.
name|MAX_VALUE
argument_list|,
call|(
name|byte
call|)
argument_list|(
name|UnsignedBytes
operator|.
name|MAX_VALUE
operator|+
literal|1
argument_list|)
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|assertParseFails (String value, int radix)
specifier|private
specifier|static
name|void
name|assertParseFails
parameter_list|(
name|String
name|value
parameter_list|,
name|int
name|radix
parameter_list|)
block|{
try|try
block|{
name|UnsignedBytes
operator|.
name|parseUnsignedByte
argument_list|(
name|value
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
block|{     }
block|}
DECL|method|testParseUnsignedByteWithRadix ()
specifier|public
name|void
name|testParseUnsignedByteWithRadix
parameter_list|()
throws|throws
name|NumberFormatException
block|{
comment|// We can easily afford to test this exhaustively.
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
name|i
init|=
literal|0
init|;
name|i
operator|<=
literal|0xff
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
name|i
argument_list|,
name|UnsignedBytes
operator|.
name|parseUnsignedByte
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|i
argument_list|,
name|radix
argument_list|)
argument_list|,
name|radix
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertParseFails
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
literal|1000
argument_list|,
name|radix
argument_list|)
argument_list|,
name|radix
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
operator|-
literal|1
argument_list|,
name|radix
argument_list|)
argument_list|,
name|radix
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
operator|-
literal|128
argument_list|,
name|radix
argument_list|)
argument_list|,
name|radix
argument_list|)
expr_stmt|;
name|assertParseFails
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
literal|256
argument_list|,
name|radix
argument_list|)
argument_list|,
name|radix
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testParseUnsignedByteThrowsExceptionForInvalidRadix ()
specifier|public
name|void
name|testParseUnsignedByteThrowsExceptionForInvalidRadix
parameter_list|()
block|{
comment|// Valid radix values are Character.MIN_RADIX to Character.MAX_RADIX,
comment|// inclusive.
try|try
block|{
name|UnsignedBytes
operator|.
name|parseUnsignedByte
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
name|nfe
parameter_list|)
block|{
comment|// expected
block|}
try|try
block|{
name|UnsignedBytes
operator|.
name|parseUnsignedByte
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
name|nfe
parameter_list|)
block|{
comment|// expected
block|}
comment|// The radix is used as an array index, so try a negative value.
try|try
block|{
name|UnsignedBytes
operator|.
name|parseUnsignedByte
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
name|nfe
parameter_list|)
block|{
comment|// expected
block|}
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
comment|// We can easily afford to test this exhaustively.
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<=
literal|0xff
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|i
argument_list|)
argument_list|,
name|UnsignedBytes
operator|.
name|toString
argument_list|(
operator|(
name|byte
operator|)
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testToStringWithRadix ()
specifier|public
name|void
name|testToStringWithRadix
parameter_list|()
block|{
comment|// We can easily afford to test this exhaustively.
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
name|i
init|=
literal|0
init|;
name|i
operator|<=
literal|0xff
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|i
argument_list|,
name|radix
argument_list|)
argument_list|,
name|UnsignedBytes
operator|.
name|toString
argument_list|(
operator|(
name|byte
operator|)
name|i
argument_list|,
name|radix
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
name|UnsignedBytes
operator|.
name|join
argument_list|(
literal|","
argument_list|,
operator|new
name|byte
index|[]
block|{}
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|UnsignedBytes
operator|.
name|join
argument_list|(
literal|","
argument_list|,
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|1
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1,2"
argument_list|,
name|UnsignedBytes
operator|.
name|join
argument_list|(
literal|","
argument_list|,
operator|(
name|byte
operator|)
literal|1
argument_list|,
operator|(
name|byte
operator|)
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|UnsignedBytes
operator|.
name|join
argument_list|(
literal|""
argument_list|,
operator|(
name|byte
operator|)
literal|1
argument_list|,
operator|(
name|byte
operator|)
literal|2
argument_list|,
operator|(
name|byte
operator|)
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"128,255"
argument_list|,
name|UnsignedBytes
operator|.
name|join
argument_list|(
literal|","
argument_list|,
operator|(
name|byte
operator|)
literal|128
argument_list|,
operator|(
name|byte
operator|)
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|unsafeComparatorClassName ()
specifier|private
specifier|static
name|String
name|unsafeComparatorClassName
parameter_list|()
block|{
return|return
name|UnsignedBytes
operator|.
name|LexicographicalComparatorHolder
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"$UnsafeComparator"
return|;
block|}
DECL|method|unsafeComparatorAvailable ()
specifier|private
specifier|static
name|boolean
name|unsafeComparatorAvailable
parameter_list|()
block|{
comment|// See Java Puzzler #44
comment|// Use reflection instead of catching NoClassDefFoundError
try|try
block|{
name|Class
operator|.
name|forName
argument_list|(
name|unsafeComparatorClassName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|Error
decl||
name|ClassNotFoundException
name|tolerable
parameter_list|)
block|{
comment|/*        * We're probably running on Android.        *        * A note on exception types:        *        * Android API level 10 throws ExceptionInInitializerError the first time and        * ClassNotFoundException thereafter.        *        * Android API level 26 and JVM8 both let our Error propagate directly the first time and        * throw NoClassDefFoundError thereafter. This is the proper behavior according to the spec.        * See https://docs.oracle.com/javase/specs/jls/se8/html/jls-12.html#jls-12.4.2 (steps #11 and        * #5).        *        * Note that that "first time" might happen in a test other than        * testLexicographicalComparatorChoice!        */
return|return
literal|false
return|;
block|}
block|}
DECL|method|testLexicographicalComparatorChoice ()
specifier|public
name|void
name|testLexicographicalComparatorChoice
parameter_list|()
throws|throws
name|Exception
block|{
name|Comparator
argument_list|<
name|byte
index|[]
argument_list|>
name|defaultComparator
init|=
name|UnsignedBytes
operator|.
name|lexicographicalComparator
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|defaultComparator
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|defaultComparator
argument_list|,
name|UnsignedBytes
operator|.
name|lexicographicalComparator
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|unsafeComparatorAvailable
argument_list|()
condition|)
block|{
name|assertSame
argument_list|(
name|defaultComparator
operator|.
name|getClass
argument_list|()
argument_list|,
name|Class
operator|.
name|forName
argument_list|(
name|unsafeComparatorClassName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertSame
argument_list|(
name|defaultComparator
argument_list|,
name|UnsignedBytes
operator|.
name|lexicographicalComparatorJavaImpl
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testLexicographicalComparator ()
specifier|public
name|void
name|testLexicographicalComparator
parameter_list|()
block|{
name|List
argument_list|<
name|byte
index|[]
argument_list|>
name|ordered
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|byte
index|[]
block|{}
argument_list|,
operator|new
name|byte
index|[]
block|{
name|LEAST
block|}
argument_list|,
operator|new
name|byte
index|[]
block|{
name|LEAST
block|,
name|LEAST
block|}
argument_list|,
operator|new
name|byte
index|[]
block|{
name|LEAST
block|,
operator|(
name|byte
operator|)
literal|1
block|}
argument_list|,
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|1
block|}
argument_list|,
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|1
block|,
name|LEAST
block|}
argument_list|,
operator|new
name|byte
index|[]
block|{
name|GREATEST
block|,
name|GREATEST
operator|-
operator|(
name|byte
operator|)
literal|1
block|}
argument_list|,
operator|new
name|byte
index|[]
block|{
name|GREATEST
block|,
name|GREATEST
block|}
argument_list|,
operator|new
name|byte
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
comment|// The Unsafe implementation if it's available. Otherwise, the Java implementation.
name|Comparator
argument_list|<
name|byte
index|[]
argument_list|>
name|comparator
init|=
name|UnsignedBytes
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
name|assertSame
argument_list|(
name|comparator
argument_list|,
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|comparator
argument_list|)
argument_list|)
expr_stmt|;
comment|// The Java implementation.
name|Comparator
argument_list|<
name|byte
index|[]
argument_list|>
name|javaImpl
init|=
name|UnsignedBytes
operator|.
name|lexicographicalComparatorJavaImpl
argument_list|()
decl_stmt|;
name|Helpers
operator|.
name|testComparator
argument_list|(
name|javaImpl
argument_list|,
name|ordered
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|javaImpl
argument_list|,
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|javaImpl
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testLexicographicalComparatorLongInputs ()
specifier|public
name|void
name|testLexicographicalComparatorLongInputs
parameter_list|()
block|{
name|Random
name|rnd
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
for|for
control|(
name|Comparator
argument_list|<
name|byte
index|[]
argument_list|>
name|comparator
range|:
name|Arrays
operator|.
name|asList
argument_list|(
name|UnsignedBytes
operator|.
name|lexicographicalComparator
argument_list|()
argument_list|,
name|UnsignedBytes
operator|.
name|lexicographicalComparatorJavaImpl
argument_list|()
argument_list|)
control|)
block|{
for|for
control|(
name|int
name|trials
init|=
literal|10
init|;
name|trials
operator|--
operator|>
literal|0
condition|;
control|)
block|{
name|byte
index|[]
name|left
init|=
operator|new
name|byte
index|[
literal|1
operator|+
name|rnd
operator|.
name|nextInt
argument_list|(
literal|32
argument_list|)
index|]
decl_stmt|;
name|rnd
operator|.
name|nextBytes
argument_list|(
name|left
argument_list|)
expr_stmt|;
name|byte
index|[]
name|right
init|=
name|left
operator|.
name|clone
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|comparator
operator|.
name|compare
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
operator|==
literal|0
argument_list|)
expr_stmt|;
name|int
name|i
init|=
name|rnd
operator|.
name|nextInt
argument_list|(
name|left
operator|.
name|length
argument_list|)
decl_stmt|;
name|left
index|[
name|i
index|]
operator|^=
call|(
name|byte
call|)
argument_list|(
literal|1
operator|+
name|rnd
operator|.
name|nextInt
argument_list|(
literal|255
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|comparator
operator|.
name|compare
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
operator|!=
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|comparator
operator|.
name|compare
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
operator|>
literal|0
argument_list|,
name|UnsignedBytes
operator|.
name|compare
argument_list|(
name|left
index|[
name|i
index|]
argument_list|,
name|right
index|[
name|i
index|]
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|UnsignedBytes
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

