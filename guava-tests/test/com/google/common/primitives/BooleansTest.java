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
comment|/**  * Unit test for {@link Booleans}.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|BooleansTest
specifier|public
class|class
name|BooleansTest
extends|extends
name|TestCase
block|{
DECL|field|EMPTY
specifier|private
specifier|static
specifier|final
name|boolean
index|[]
name|EMPTY
init|=
block|{}
decl_stmt|;
DECL|field|ARRAY_FALSE
specifier|private
specifier|static
specifier|final
name|boolean
index|[]
name|ARRAY_FALSE
init|=
block|{
literal|false
block|}
decl_stmt|;
DECL|field|ARRAY_TRUE
specifier|private
specifier|static
specifier|final
name|boolean
index|[]
name|ARRAY_TRUE
init|=
block|{
literal|true
block|}
decl_stmt|;
DECL|field|ARRAY_FALSE_FALSE
specifier|private
specifier|static
specifier|final
name|boolean
index|[]
name|ARRAY_FALSE_FALSE
init|=
block|{
literal|false
block|,
literal|false
block|}
decl_stmt|;
DECL|field|ARRAY_FALSE_TRUE
specifier|private
specifier|static
specifier|final
name|boolean
index|[]
name|ARRAY_FALSE_TRUE
init|=
block|{
literal|false
block|,
literal|true
block|}
decl_stmt|;
DECL|field|VALUES
specifier|private
specifier|static
specifier|final
name|boolean
index|[]
name|VALUES
init|=
block|{
literal|false
block|,
literal|true
block|}
decl_stmt|;
DECL|method|testHashCode ()
specifier|public
name|void
name|testHashCode
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
operator|.
name|hashCode
argument_list|()
argument_list|,
name|Booleans
operator|.
name|hashCode
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
operator|.
name|hashCode
argument_list|()
argument_list|,
name|Booleans
operator|.
name|hashCode
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCompare ()
specifier|public
name|void
name|testCompare
parameter_list|()
block|{
for|for
control|(
name|boolean
name|x
range|:
name|VALUES
control|)
block|{
for|for
control|(
name|boolean
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
name|Boolean
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
name|Booleans
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
name|Booleans
operator|.
name|contains
argument_list|(
name|EMPTY
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Booleans
operator|.
name|contains
argument_list|(
name|ARRAY_FALSE
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Booleans
operator|.
name|contains
argument_list|(
name|ARRAY_FALSE
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Booleans
operator|.
name|contains
argument_list|(
name|ARRAY_FALSE_TRUE
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Booleans
operator|.
name|contains
argument_list|(
name|ARRAY_FALSE_TRUE
argument_list|,
literal|true
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
name|Booleans
operator|.
name|indexOf
argument_list|(
name|EMPTY
argument_list|,
name|ARRAY_FALSE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Booleans
operator|.
name|indexOf
argument_list|(
name|ARRAY_FALSE
argument_list|,
name|ARRAY_FALSE_TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Booleans
operator|.
name|indexOf
argument_list|(
name|ARRAY_FALSE_FALSE
argument_list|,
name|ARRAY_FALSE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Booleans
operator|.
name|indexOf
argument_list|(
name|ARRAY_FALSE
argument_list|,
name|ARRAY_FALSE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Booleans
operator|.
name|indexOf
argument_list|(
name|ARRAY_FALSE_TRUE
argument_list|,
name|ARRAY_FALSE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Booleans
operator|.
name|indexOf
argument_list|(
name|ARRAY_FALSE_TRUE
argument_list|,
name|ARRAY_TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Booleans
operator|.
name|indexOf
argument_list|(
name|ARRAY_TRUE
argument_list|,
operator|new
name|boolean
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIndexOf_arrays ()
specifier|public
name|void
name|testIndexOf_arrays
parameter_list|()
block|{
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Booleans
operator|.
name|indexOf
argument_list|(
name|EMPTY
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Booleans
operator|.
name|indexOf
argument_list|(
name|ARRAY_FALSE
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Booleans
operator|.
name|indexOf
argument_list|(
name|ARRAY_FALSE_FALSE
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Booleans
operator|.
name|indexOf
argument_list|(
name|ARRAY_FALSE
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Booleans
operator|.
name|indexOf
argument_list|(
name|ARRAY_FALSE_TRUE
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Booleans
operator|.
name|indexOf
argument_list|(
name|ARRAY_FALSE_TRUE
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|Booleans
operator|.
name|indexOf
argument_list|(
operator|new
name|boolean
index|[]
block|{
literal|false
block|,
literal|false
block|,
literal|true
block|}
argument_list|,
literal|true
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
name|Booleans
operator|.
name|lastIndexOf
argument_list|(
name|EMPTY
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Booleans
operator|.
name|lastIndexOf
argument_list|(
name|ARRAY_FALSE
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Booleans
operator|.
name|lastIndexOf
argument_list|(
name|ARRAY_FALSE_FALSE
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Booleans
operator|.
name|lastIndexOf
argument_list|(
name|ARRAY_FALSE
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Booleans
operator|.
name|lastIndexOf
argument_list|(
name|ARRAY_FALSE_TRUE
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Booleans
operator|.
name|lastIndexOf
argument_list|(
name|ARRAY_FALSE_TRUE
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|Booleans
operator|.
name|lastIndexOf
argument_list|(
operator|new
name|boolean
index|[]
block|{
literal|false
block|,
literal|true
block|,
literal|true
block|}
argument_list|,
literal|true
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
name|Booleans
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
name|Booleans
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
name|Booleans
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
name|ARRAY_FALSE
argument_list|,
name|Booleans
operator|.
name|concat
argument_list|(
name|ARRAY_FALSE
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|ARRAY_FALSE
argument_list|,
name|Booleans
operator|.
name|concat
argument_list|(
name|ARRAY_FALSE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|ARRAY_FALSE
argument_list|,
name|Booleans
operator|.
name|concat
argument_list|(
name|EMPTY
argument_list|,
name|ARRAY_FALSE
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
name|boolean
index|[]
block|{
literal|false
block|,
literal|false
block|,
literal|false
block|}
argument_list|,
name|Booleans
operator|.
name|concat
argument_list|(
name|ARRAY_FALSE
argument_list|,
name|ARRAY_FALSE
argument_list|,
name|ARRAY_FALSE
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
name|boolean
index|[]
block|{
literal|false
block|,
literal|false
block|,
literal|true
block|}
argument_list|,
name|Booleans
operator|.
name|concat
argument_list|(
name|ARRAY_FALSE
argument_list|,
name|ARRAY_FALSE_TRUE
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
name|Booleans
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
name|ARRAY_FALSE
argument_list|,
name|Booleans
operator|.
name|ensureCapacity
argument_list|(
name|ARRAY_FALSE
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|ARRAY_FALSE
argument_list|,
name|Booleans
operator|.
name|ensureCapacity
argument_list|(
name|ARRAY_FALSE
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
name|boolean
index|[]
block|{
literal|true
block|,
literal|false
block|,
literal|false
block|}
argument_list|,
name|Booleans
operator|.
name|ensureCapacity
argument_list|(
operator|new
name|boolean
index|[]
block|{
literal|true
block|}
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
name|Booleans
operator|.
name|ensureCapacity
argument_list|(
name|ARRAY_FALSE
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
name|Booleans
operator|.
name|ensureCapacity
argument_list|(
name|ARRAY_FALSE
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
name|Booleans
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
literal|"false"
argument_list|,
name|Booleans
operator|.
name|join
argument_list|(
literal|","
argument_list|,
name|ARRAY_FALSE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"false,true"
argument_list|,
name|Booleans
operator|.
name|join
argument_list|(
literal|","
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"falsetruefalse"
argument_list|,
name|Booleans
operator|.
name|join
argument_list|(
literal|""
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|false
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
name|boolean
index|[]
argument_list|>
name|ordered
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|boolean
index|[]
block|{}
argument_list|,
operator|new
name|boolean
index|[]
block|{
literal|false
block|}
argument_list|,
operator|new
name|boolean
index|[]
block|{
literal|false
block|,
literal|false
block|}
argument_list|,
operator|new
name|boolean
index|[]
block|{
literal|false
block|,
literal|true
block|}
argument_list|,
operator|new
name|boolean
index|[]
block|{
literal|true
block|}
argument_list|,
operator|new
name|boolean
index|[]
block|{
literal|true
block|,
literal|false
block|}
argument_list|,
operator|new
name|boolean
index|[]
block|{
literal|true
block|,
literal|true
block|}
argument_list|,
operator|new
name|boolean
index|[]
block|{
literal|true
block|,
literal|true
block|,
literal|true
block|}
argument_list|)
decl_stmt|;
name|Comparator
argument_list|<
name|boolean
index|[]
argument_list|>
name|comparator
init|=
name|Booleans
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
name|boolean
index|[]
argument_list|>
name|comparator
init|=
name|Booleans
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
DECL|method|testToArray ()
specifier|public
name|void
name|testToArray
parameter_list|()
block|{
comment|// need explicit type parameter to avoid javac warning!?
name|List
argument_list|<
name|Boolean
argument_list|>
name|none
init|=
name|Arrays
operator|.
expr|<
name|Boolean
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
name|Booleans
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
name|Boolean
argument_list|>
name|one
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|ARRAY_FALSE
argument_list|,
name|Booleans
operator|.
name|toArray
argument_list|(
name|one
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|boolean
index|[]
name|array
init|=
block|{
literal|false
block|,
literal|false
block|,
literal|true
block|}
decl_stmt|;
name|List
argument_list|<
name|Boolean
argument_list|>
name|three
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
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
name|Booleans
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
name|Booleans
operator|.
name|toArray
argument_list|(
name|Booleans
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
comment|// Only for booleans, we lengthen VALUES
name|boolean
index|[]
name|VALUES
init|=
name|BooleansTest
operator|.
name|VALUES
decl_stmt|;
name|VALUES
operator|=
name|Booleans
operator|.
name|concat
argument_list|(
name|VALUES
argument_list|,
name|VALUES
argument_list|)
expr_stmt|;
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
name|Boolean
argument_list|>
name|list
init|=
name|Booleans
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
name|Boolean
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
name|boolean
index|[]
name|arr
init|=
name|Booleans
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
name|Boolean
argument_list|>
name|list
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|Booleans
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
DECL|method|testAsListIsEmpty ()
specifier|public
name|void
name|testAsListIsEmpty
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|EMPTY
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsListSize ()
specifier|public
name|void
name|testAsListSize
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|EMPTY
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE_TRUE
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsListIndexOf ()
specifier|public
name|void
name|testAsListIndexOf
parameter_list|()
block|{
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|EMPTY
argument_list|)
operator|.
name|indexOf
argument_list|(
literal|"wrong type"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|EMPTY
argument_list|)
operator|.
name|indexOf
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
operator|.
name|indexOf
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
operator|.
name|indexOf
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE_TRUE
argument_list|)
operator|.
name|indexOf
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsListLastIndexOf ()
specifier|public
name|void
name|testAsListLastIndexOf
parameter_list|()
block|{
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|EMPTY
argument_list|)
operator|.
name|indexOf
argument_list|(
literal|"wrong type"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|EMPTY
argument_list|)
operator|.
name|indexOf
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
operator|.
name|lastIndexOf
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE_TRUE
argument_list|)
operator|.
name|lastIndexOf
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE_FALSE
argument_list|)
operator|.
name|lastIndexOf
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsListContains ()
specifier|public
name|void
name|testAsListContains
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|EMPTY
argument_list|)
operator|.
name|contains
argument_list|(
literal|"wrong type"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|EMPTY
argument_list|)
operator|.
name|contains
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
operator|.
name|contains
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_TRUE
argument_list|)
operator|.
name|contains
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE_TRUE
argument_list|)
operator|.
name|contains
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE_TRUE
argument_list|)
operator|.
name|contains
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsListEquals ()
specifier|public
name|void
name|testAsListEquals
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|EMPTY
argument_list|)
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
operator|.
name|equals
argument_list|(
name|ARRAY_FALSE
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
operator|.
name|equals
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
operator|.
name|equals
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE_TRUE
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE_FALSE
argument_list|)
operator|.
name|equals
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE_TRUE
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE_TRUE
argument_list|)
operator|.
name|lastIndexOf
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Boolean
argument_list|>
name|reference
init|=
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
argument_list|,
name|reference
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|reference
argument_list|,
name|reference
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsListHashcode ()
specifier|public
name|void
name|testAsListHashcode
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|EMPTY
argument_list|)
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
operator|.
name|hashCode
argument_list|()
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Boolean
argument_list|>
name|reference
init|=
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
operator|.
name|hashCode
argument_list|()
argument_list|,
name|reference
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsListToString ()
specifier|public
name|void
name|testAsListToString
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"[false]"
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[false, true]"
argument_list|,
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE_TRUE
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsListSet ()
specifier|public
name|void
name|testAsListSet
parameter_list|()
block|{
name|List
argument_list|<
name|Boolean
argument_list|>
name|list
init|=
name|Booleans
operator|.
name|asList
argument_list|(
name|ARRAY_FALSE
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|list
operator|.
name|set
argument_list|(
literal|0
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|list
operator|.
name|set
argument_list|(
literal|0
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|list
operator|.
name|set
argument_list|(
literal|0
argument_list|,
literal|null
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
try|try
block|{
name|list
operator|.
name|set
argument_list|(
literal|1
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|expected
parameter_list|)
block|{     }
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
name|boolean
index|[]
operator|.
expr|class
argument_list|,
operator|new
name|boolean
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|tester
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|Booleans
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

