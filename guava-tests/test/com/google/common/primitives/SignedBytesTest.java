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

begin_comment
comment|/**  * Unit test for {@link SignedBytes}.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"cast"
argument_list|)
comment|// redundant casts are intentional and harmless
DECL|class|SignedBytesTest
specifier|public
class|class
name|SignedBytesTest
extends|extends
name|TestCase
block|{
DECL|field|EMPTY
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|EMPTY
init|=
block|{}
decl_stmt|;
DECL|field|ARRAY1
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|ARRAY1
init|=
block|{
operator|(
name|byte
operator|)
literal|1
block|}
decl_stmt|;
DECL|field|LEAST
specifier|private
specifier|static
specifier|final
name|byte
name|LEAST
init|=
name|Byte
operator|.
name|MIN_VALUE
decl_stmt|;
DECL|field|GREATEST
specifier|private
specifier|static
specifier|final
name|byte
name|GREATEST
init|=
name|Byte
operator|.
name|MAX_VALUE
decl_stmt|;
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
operator|-
literal|1
block|,
literal|0
block|,
literal|1
block|,
name|GREATEST
block|}
decl_stmt|;
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
name|SignedBytes
operator|.
name|checkedCast
argument_list|(
operator|(
name|long
operator|)
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertCastFails
argument_list|(
name|GREATEST
operator|+
literal|1L
argument_list|)
expr_stmt|;
name|assertCastFails
argument_list|(
name|LEAST
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
name|SignedBytes
operator|.
name|saturatedCast
argument_list|(
operator|(
name|long
operator|)
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|GREATEST
argument_list|,
name|SignedBytes
operator|.
name|saturatedCast
argument_list|(
name|GREATEST
operator|+
literal|1L
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LEAST
argument_list|,
name|SignedBytes
operator|.
name|saturatedCast
argument_list|(
name|LEAST
operator|-
literal|1L
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GREATEST
argument_list|,
name|SignedBytes
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
name|SignedBytes
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
name|SignedBytes
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
for|for
control|(
name|byte
name|x
range|:
name|VALUES
control|)
block|{
for|for
control|(
name|byte
name|y
range|:
name|VALUES
control|)
block|{
comment|// Only compare the sign of the result of compareTo().
name|int
name|expected
init|=
name|Byte
operator|.
name|valueOf
argument_list|(
name|x
argument_list|)
operator|.
name|compareTo
argument_list|(
name|y
argument_list|)
decl_stmt|;
name|int
name|actual
init|=
name|SignedBytes
operator|.
name|compare
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
decl_stmt|;
if|if
condition|(
name|expected
operator|==
literal|0
condition|)
block|{
name|assertEquals
argument_list|(
name|x
operator|+
literal|", "
operator|+
name|y
argument_list|,
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|expected
operator|<
literal|0
condition|)
block|{
name|assertTrue
argument_list|(
name|x
operator|+
literal|", "
operator|+
name|y
operator|+
literal|" (expected: "
operator|+
name|expected
operator|+
literal|", actual"
operator|+
name|actual
operator|+
literal|")"
argument_list|,
name|actual
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertTrue
argument_list|(
name|x
operator|+
literal|", "
operator|+
name|y
operator|+
literal|" (expected: "
operator|+
name|expected
operator|+
literal|", actual"
operator|+
name|actual
operator|+
literal|")"
argument_list|,
name|actual
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
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
name|SignedBytes
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
name|SignedBytes
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
name|SignedBytes
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
literal|127
argument_list|,
name|SignedBytes
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
name|SignedBytes
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
name|SignedBytes
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
name|SignedBytes
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
operator|-
literal|128
argument_list|,
name|SignedBytes
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
name|SignedBytes
operator|.
name|join
argument_list|(
literal|","
argument_list|,
name|EMPTY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|SignedBytes
operator|.
name|join
argument_list|(
literal|","
argument_list|,
name|ARRAY1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1,2"
argument_list|,
name|SignedBytes
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
name|SignedBytes
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
literal|"-128,-1"
argument_list|,
name|SignedBytes
operator|.
name|join
argument_list|(
literal|","
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
name|Comparator
argument_list|<
name|byte
index|[]
argument_list|>
name|comparator
init|=
name|SignedBytes
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
annotation|@
name|GwtIncompatible
argument_list|(
literal|"SerializableTester"
argument_list|)
DECL|method|testLexicographicalComparatorSerializable ()
specifier|public
name|void
name|testLexicographicalComparatorSerializable
parameter_list|()
block|{
name|Comparator
argument_list|<
name|byte
index|[]
argument_list|>
name|comparator
init|=
name|SignedBytes
operator|.
name|lexicographicalComparator
argument_list|()
decl_stmt|;
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
name|SignedBytes
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

