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
name|collect
operator|.
name|testing
operator|.
name|Helpers
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
name|Collection
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
comment|/**  * Unit test for {@link Chars}.  *  * @author Kevin Bourrillion  */
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
DECL|class|CharsTest
specifier|public
class|class
name|CharsTest
extends|extends
name|TestCase
block|{
DECL|field|EMPTY
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|EMPTY
init|=
block|{}
decl_stmt|;
DECL|field|ARRAY1
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|ARRAY1
init|=
block|{
operator|(
name|char
operator|)
literal|1
block|}
decl_stmt|;
DECL|field|ARRAY234
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|ARRAY234
init|=
block|{
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|3
block|,
operator|(
name|char
operator|)
literal|4
block|}
decl_stmt|;
DECL|field|LEAST
specifier|private
specifier|static
specifier|final
name|char
name|LEAST
init|=
name|Character
operator|.
name|MIN_VALUE
decl_stmt|;
DECL|field|GREATEST
specifier|private
specifier|static
specifier|final
name|char
name|GREATEST
init|=
name|Character
operator|.
name|MAX_VALUE
decl_stmt|;
DECL|field|VALUES
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|VALUES
init|=
block|{
name|LEAST
block|,
literal|'a'
block|,
literal|'\u00e0'
block|,
literal|'\udcaa'
block|,
name|GREATEST
block|}
decl_stmt|;
DECL|method|testHashCode ()
specifier|public
name|void
name|testHashCode
parameter_list|()
block|{
for|for
control|(
name|char
name|value
range|:
name|VALUES
control|)
block|{
name|assertEquals
argument_list|(
operator|(
operator|(
name|Character
operator|)
name|value
operator|)
operator|.
name|hashCode
argument_list|()
argument_list|,
name|Chars
operator|.
name|hashCode
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCheckedCast ()
specifier|public
name|void
name|testCheckedCast
parameter_list|()
block|{
for|for
control|(
name|char
name|value
range|:
name|VALUES
control|)
block|{
name|assertEquals
argument_list|(
name|value
argument_list|,
name|Chars
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
name|char
name|value
range|:
name|VALUES
control|)
block|{
name|assertEquals
argument_list|(
name|value
argument_list|,
name|Chars
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
name|Chars
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
name|Chars
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
name|Chars
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
name|Chars
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
name|void
name|assertCastFails
parameter_list|(
name|long
name|value
parameter_list|)
block|{
try|try
block|{
name|Chars
operator|.
name|checkedCast
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Cast to char should have failed: "
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
name|char
name|x
range|:
name|VALUES
control|)
block|{
for|for
control|(
name|char
name|y
range|:
name|VALUES
control|)
block|{
comment|// note: spec requires only that the sign is the same
name|assertEquals
argument_list|(
name|x
operator|+
literal|", "
operator|+
name|y
argument_list|,
name|Character
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
argument_list|,
name|Chars
operator|.
name|compare
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testContains ()
specifier|public
name|void
name|testContains
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|Chars
operator|.
name|contains
argument_list|(
name|EMPTY
argument_list|,
operator|(
name|char
operator|)
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Chars
operator|.
name|contains
argument_list|(
name|ARRAY1
argument_list|,
operator|(
name|char
operator|)
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Chars
operator|.
name|contains
argument_list|(
name|ARRAY234
argument_list|,
operator|(
name|char
operator|)
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Chars
operator|.
name|contains
argument_list|(
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
operator|-
literal|1
block|}
argument_list|,
operator|(
name|char
operator|)
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Chars
operator|.
name|contains
argument_list|(
name|ARRAY234
argument_list|,
operator|(
name|char
operator|)
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Chars
operator|.
name|contains
argument_list|(
name|ARRAY234
argument_list|,
operator|(
name|char
operator|)
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Chars
operator|.
name|contains
argument_list|(
name|ARRAY234
argument_list|,
operator|(
name|char
operator|)
literal|4
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIndexOf ()
specifier|public
name|void
name|testIndexOf
parameter_list|()
block|{
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
name|EMPTY
argument_list|,
operator|(
name|char
operator|)
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
name|ARRAY1
argument_list|,
operator|(
name|char
operator|)
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
name|ARRAY234
argument_list|,
operator|(
name|char
operator|)
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
operator|-
literal|1
block|}
argument_list|,
operator|(
name|char
operator|)
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
name|ARRAY234
argument_list|,
operator|(
name|char
operator|)
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
name|ARRAY234
argument_list|,
operator|(
name|char
operator|)
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
name|ARRAY234
argument_list|,
operator|(
name|char
operator|)
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|3
block|,
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|3
block|}
argument_list|,
operator|(
name|char
operator|)
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIndexOf_arrayTarget ()
specifier|public
name|void
name|testIndexOf_arrayTarget
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
name|EMPTY
argument_list|,
name|EMPTY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
name|ARRAY234
argument_list|,
name|EMPTY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
name|EMPTY
argument_list|,
name|ARRAY234
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
name|ARRAY234
argument_list|,
name|ARRAY1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
name|ARRAY1
argument_list|,
name|ARRAY234
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
name|ARRAY1
argument_list|,
name|ARRAY1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
name|ARRAY234
argument_list|,
name|ARRAY234
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
name|ARRAY234
argument_list|,
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|3
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
name|ARRAY234
argument_list|,
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|3
block|,
operator|(
name|char
operator|)
literal|4
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
name|ARRAY234
argument_list|,
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|3
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
name|ARRAY234
argument_list|,
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|4
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|3
block|,
operator|(
name|char
operator|)
literal|3
block|,
operator|(
name|char
operator|)
literal|3
block|,
operator|(
name|char
operator|)
literal|3
block|}
argument_list|,
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|3
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|3
block|,
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|3
block|,
operator|(
name|char
operator|)
literal|4
block|,
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|3
block|}
argument_list|,
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|3
block|,
operator|(
name|char
operator|)
literal|4
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|3
block|,
operator|(
name|char
operator|)
literal|4
block|,
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|3
block|,
operator|(
name|char
operator|)
literal|4
block|}
argument_list|,
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|3
block|,
operator|(
name|char
operator|)
literal|4
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Chars
operator|.
name|indexOf
argument_list|(
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|4
block|,
operator|(
name|char
operator|)
literal|3
block|,
operator|(
name|char
operator|)
literal|2
block|}
argument_list|,
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|3
block|,
operator|(
name|char
operator|)
literal|4
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testLastIndexOf ()
specifier|public
name|void
name|testLastIndexOf
parameter_list|()
block|{
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Chars
operator|.
name|lastIndexOf
argument_list|(
name|EMPTY
argument_list|,
operator|(
name|char
operator|)
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Chars
operator|.
name|lastIndexOf
argument_list|(
name|ARRAY1
argument_list|,
operator|(
name|char
operator|)
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Chars
operator|.
name|lastIndexOf
argument_list|(
name|ARRAY234
argument_list|,
operator|(
name|char
operator|)
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Chars
operator|.
name|lastIndexOf
argument_list|(
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
operator|-
literal|1
block|}
argument_list|,
operator|(
name|char
operator|)
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Chars
operator|.
name|lastIndexOf
argument_list|(
name|ARRAY234
argument_list|,
operator|(
name|char
operator|)
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Chars
operator|.
name|lastIndexOf
argument_list|(
name|ARRAY234
argument_list|,
operator|(
name|char
operator|)
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|Chars
operator|.
name|lastIndexOf
argument_list|(
name|ARRAY234
argument_list|,
operator|(
name|char
operator|)
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|Chars
operator|.
name|lastIndexOf
argument_list|(
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|3
block|,
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|3
block|}
argument_list|,
operator|(
name|char
operator|)
literal|3
argument_list|)
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
name|Chars
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
name|Chars
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
name|Chars
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
name|char
operator|)
literal|9
argument_list|,
name|Chars
operator|.
name|max
argument_list|(
operator|(
name|char
operator|)
literal|8
argument_list|,
operator|(
name|char
operator|)
literal|6
argument_list|,
operator|(
name|char
operator|)
literal|7
argument_list|,
operator|(
name|char
operator|)
literal|5
argument_list|,
operator|(
name|char
operator|)
literal|3
argument_list|,
operator|(
name|char
operator|)
literal|0
argument_list|,
operator|(
name|char
operator|)
literal|9
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
name|Chars
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
name|Chars
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
name|Chars
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
name|char
operator|)
literal|0
argument_list|,
name|Chars
operator|.
name|min
argument_list|(
operator|(
name|char
operator|)
literal|8
argument_list|,
operator|(
name|char
operator|)
literal|6
argument_list|,
operator|(
name|char
operator|)
literal|7
argument_list|,
operator|(
name|char
operator|)
literal|5
argument_list|,
operator|(
name|char
operator|)
literal|3
argument_list|,
operator|(
name|char
operator|)
literal|0
argument_list|,
operator|(
name|char
operator|)
literal|9
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testConcat ()
specifier|public
name|void
name|testConcat
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|EMPTY
argument_list|,
name|Chars
operator|.
name|concat
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|EMPTY
argument_list|,
name|Chars
operator|.
name|concat
argument_list|(
name|EMPTY
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|EMPTY
argument_list|,
name|Chars
operator|.
name|concat
argument_list|(
name|EMPTY
argument_list|,
name|EMPTY
argument_list|,
name|EMPTY
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|ARRAY1
argument_list|,
name|Chars
operator|.
name|concat
argument_list|(
name|ARRAY1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|ARRAY1
argument_list|,
name|Chars
operator|.
name|concat
argument_list|(
name|ARRAY1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|ARRAY1
argument_list|,
name|Chars
operator|.
name|concat
argument_list|(
name|EMPTY
argument_list|,
name|ARRAY1
argument_list|,
name|EMPTY
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|1
block|,
operator|(
name|char
operator|)
literal|1
block|,
operator|(
name|char
operator|)
literal|1
block|}
argument_list|,
name|Chars
operator|.
name|concat
argument_list|(
name|ARRAY1
argument_list|,
name|ARRAY1
argument_list|,
name|ARRAY1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|1
block|,
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|3
block|,
operator|(
name|char
operator|)
literal|4
block|}
argument_list|,
name|Chars
operator|.
name|concat
argument_list|(
name|ARRAY1
argument_list|,
name|ARRAY234
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEnsureCapacity ()
specifier|public
name|void
name|testEnsureCapacity
parameter_list|()
block|{
name|assertSame
argument_list|(
name|EMPTY
argument_list|,
name|Chars
operator|.
name|ensureCapacity
argument_list|(
name|EMPTY
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|ARRAY1
argument_list|,
name|Chars
operator|.
name|ensureCapacity
argument_list|(
name|ARRAY1
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|ARRAY1
argument_list|,
name|Chars
operator|.
name|ensureCapacity
argument_list|(
name|ARRAY1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|1
block|,
operator|(
name|char
operator|)
literal|0
block|,
operator|(
name|char
operator|)
literal|0
block|}
argument_list|,
name|Chars
operator|.
name|ensureCapacity
argument_list|(
name|ARRAY1
argument_list|,
literal|2
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEnsureCapacity_fail ()
specifier|public
name|void
name|testEnsureCapacity_fail
parameter_list|()
block|{
try|try
block|{
name|Chars
operator|.
name|ensureCapacity
argument_list|(
name|ARRAY1
argument_list|,
operator|-
literal|1
argument_list|,
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
block|{     }
try|try
block|{
comment|// notice that this should even fail when no growth was needed
name|Chars
operator|.
name|ensureCapacity
argument_list|(
name|ARRAY1
argument_list|,
literal|1
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
block|{     }
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
name|Chars
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
name|Chars
operator|.
name|join
argument_list|(
literal|","
argument_list|,
literal|'1'
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1,2"
argument_list|,
name|Chars
operator|.
name|join
argument_list|(
literal|","
argument_list|,
literal|'1'
argument_list|,
literal|'2'
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|Chars
operator|.
name|join
argument_list|(
literal|""
argument_list|,
literal|'1'
argument_list|,
literal|'2'
argument_list|,
literal|'3'
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
name|char
index|[]
argument_list|>
name|ordered
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|char
index|[]
block|{}
argument_list|,
operator|new
name|char
index|[]
block|{
name|LEAST
block|}
argument_list|,
operator|new
name|char
index|[]
block|{
name|LEAST
block|,
name|LEAST
block|}
argument_list|,
operator|new
name|char
index|[]
block|{
name|LEAST
block|,
operator|(
name|char
operator|)
literal|1
block|}
argument_list|,
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|1
block|}
argument_list|,
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|1
block|,
name|LEAST
block|}
argument_list|,
operator|new
name|char
index|[]
block|{
name|GREATEST
block|,
name|GREATEST
operator|-
operator|(
name|char
operator|)
literal|1
block|}
argument_list|,
operator|new
name|char
index|[]
block|{
name|GREATEST
block|,
name|GREATEST
block|}
argument_list|,
operator|new
name|char
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
name|char
index|[]
argument_list|>
name|comparator
init|=
name|Chars
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
DECL|method|testToArray ()
specifier|public
name|void
name|testToArray
parameter_list|()
block|{
comment|// need explicit type parameter to avoid javac warning!?
name|List
argument_list|<
name|Character
argument_list|>
name|none
init|=
name|Arrays
operator|.
expr|<
name|Character
operator|>
name|asList
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|EMPTY
argument_list|,
name|Chars
operator|.
name|toArray
argument_list|(
name|none
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Character
argument_list|>
name|one
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|char
operator|)
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|ARRAY1
argument_list|,
name|Chars
operator|.
name|toArray
argument_list|(
name|one
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|char
index|[]
name|array
init|=
block|{
operator|(
name|char
operator|)
literal|0
block|,
operator|(
name|char
operator|)
literal|1
block|,
literal|'A'
block|}
decl_stmt|;
name|List
argument_list|<
name|Character
argument_list|>
name|three
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|char
operator|)
literal|0
argument_list|,
operator|(
name|char
operator|)
literal|1
argument_list|,
literal|'A'
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|array
argument_list|,
name|Chars
operator|.
name|toArray
argument_list|(
name|three
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|array
argument_list|,
name|Chars
operator|.
name|toArray
argument_list|(
name|Chars
operator|.
name|asList
argument_list|(
name|array
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToArray_threadSafe ()
specifier|public
name|void
name|testToArray_threadSafe
parameter_list|()
block|{
for|for
control|(
name|int
name|delta
range|:
operator|new
name|int
index|[]
block|{
operator|+
literal|1
block|,
literal|0
block|,
operator|-
literal|1
block|}
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
operator|<
name|VALUES
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|List
argument_list|<
name|Character
argument_list|>
name|list
init|=
name|Chars
operator|.
name|asList
argument_list|(
name|VALUES
argument_list|)
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|Character
argument_list|>
name|misleadingSize
init|=
name|Helpers
operator|.
name|misleadingSizeCollection
argument_list|(
name|delta
argument_list|)
decl_stmt|;
name|misleadingSize
operator|.
name|addAll
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|char
index|[]
name|arr
init|=
name|Chars
operator|.
name|toArray
argument_list|(
name|misleadingSize
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|i
argument_list|,
name|arr
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|i
condition|;
name|j
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|VALUES
index|[
name|j
index|]
argument_list|,
name|arr
index|[
name|j
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|testToArray_withNull ()
specifier|public
name|void
name|testToArray_withNull
parameter_list|()
block|{
name|List
argument_list|<
name|Character
argument_list|>
name|list
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|char
operator|)
literal|0
argument_list|,
operator|(
name|char
operator|)
literal|1
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|Chars
operator|.
name|toArray
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testAsList_isAView ()
specifier|public
name|void
name|testAsList_isAView
parameter_list|()
block|{
name|char
index|[]
name|array
init|=
block|{
operator|(
name|char
operator|)
literal|0
block|,
operator|(
name|char
operator|)
literal|1
block|}
decl_stmt|;
name|List
argument_list|<
name|Character
argument_list|>
name|list
init|=
name|Chars
operator|.
name|asList
argument_list|(
name|array
argument_list|)
decl_stmt|;
name|list
operator|.
name|set
argument_list|(
literal|0
argument_list|,
operator|(
name|char
operator|)
literal|2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|1
block|}
argument_list|,
name|array
argument_list|)
argument_list|)
expr_stmt|;
name|array
index|[
literal|1
index|]
operator|=
operator|(
name|char
operator|)
literal|3
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|char
operator|)
literal|2
argument_list|,
operator|(
name|char
operator|)
literal|3
argument_list|)
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsList_toArray_roundTrip ()
specifier|public
name|void
name|testAsList_toArray_roundTrip
parameter_list|()
block|{
name|char
index|[]
name|array
init|=
block|{
operator|(
name|char
operator|)
literal|0
block|,
operator|(
name|char
operator|)
literal|1
block|,
operator|(
name|char
operator|)
literal|2
block|}
decl_stmt|;
name|List
argument_list|<
name|Character
argument_list|>
name|list
init|=
name|Chars
operator|.
name|asList
argument_list|(
name|array
argument_list|)
decl_stmt|;
name|char
index|[]
name|newArray
init|=
name|Chars
operator|.
name|toArray
argument_list|(
name|list
argument_list|)
decl_stmt|;
comment|// Make sure it returned a copy
name|list
operator|.
name|set
argument_list|(
literal|0
argument_list|,
operator|(
name|char
operator|)
literal|4
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|0
block|,
operator|(
name|char
operator|)
literal|1
block|,
operator|(
name|char
operator|)
literal|2
block|}
argument_list|,
name|newArray
argument_list|)
argument_list|)
expr_stmt|;
name|newArray
index|[
literal|1
index|]
operator|=
operator|(
name|char
operator|)
literal|5
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|char
operator|)
literal|1
argument_list|,
operator|(
name|char
operator|)
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// This test stems from a real bug found by andrewk
DECL|method|testAsList_subList_toArray_roundTrip ()
specifier|public
name|void
name|testAsList_subList_toArray_roundTrip
parameter_list|()
block|{
name|char
index|[]
name|array
init|=
block|{
operator|(
name|char
operator|)
literal|0
block|,
operator|(
name|char
operator|)
literal|1
block|,
operator|(
name|char
operator|)
literal|2
block|,
operator|(
name|char
operator|)
literal|3
block|}
decl_stmt|;
name|List
argument_list|<
name|Character
argument_list|>
name|list
init|=
name|Chars
operator|.
name|asList
argument_list|(
name|array
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
operator|new
name|char
index|[]
block|{
operator|(
name|char
operator|)
literal|1
block|,
operator|(
name|char
operator|)
literal|2
block|}
argument_list|,
name|Chars
operator|.
name|toArray
argument_list|(
name|list
operator|.
name|subList
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
operator|new
name|char
index|[]
block|{}
argument_list|,
name|Chars
operator|.
name|toArray
argument_list|(
name|list
operator|.
name|subList
argument_list|(
literal|2
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsListEmpty ()
specifier|public
name|void
name|testAsListEmpty
parameter_list|()
block|{
name|assertSame
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|,
name|Chars
operator|.
name|asList
argument_list|(
name|EMPTY
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

