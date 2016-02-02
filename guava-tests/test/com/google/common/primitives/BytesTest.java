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
name|List
import|;
end_import

begin_comment
comment|/**  * Unit test for {@link Bytes}.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|BytesTest
specifier|public
class|class
name|BytesTest
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
DECL|field|ARRAY234
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|ARRAY234
init|=
block|{
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
operator|)
literal|3
block|,
operator|(
name|byte
operator|)
literal|4
block|}
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
name|Byte
operator|.
name|MIN_VALUE
block|,
operator|-
literal|1
block|,
literal|0
block|,
literal|1
block|,
name|Byte
operator|.
name|MAX_VALUE
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
name|byte
name|value
range|:
name|VALUES
control|)
block|{
name|assertEquals
argument_list|(
operator|(
operator|(
name|Byte
operator|)
name|value
operator|)
operator|.
name|hashCode
argument_list|()
argument_list|,
name|Bytes
operator|.
name|hashCode
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
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
name|Bytes
operator|.
name|contains
argument_list|(
name|EMPTY
argument_list|,
operator|(
name|byte
operator|)
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Bytes
operator|.
name|contains
argument_list|(
name|ARRAY1
argument_list|,
operator|(
name|byte
operator|)
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Bytes
operator|.
name|contains
argument_list|(
name|ARRAY234
argument_list|,
operator|(
name|byte
operator|)
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Bytes
operator|.
name|contains
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
operator|-
literal|1
block|}
argument_list|,
operator|(
name|byte
operator|)
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Bytes
operator|.
name|contains
argument_list|(
name|ARRAY234
argument_list|,
operator|(
name|byte
operator|)
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Bytes
operator|.
name|contains
argument_list|(
name|ARRAY234
argument_list|,
operator|(
name|byte
operator|)
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Bytes
operator|.
name|contains
argument_list|(
name|ARRAY234
argument_list|,
operator|(
name|byte
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
name|Bytes
operator|.
name|indexOf
argument_list|(
name|EMPTY
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
operator|-
literal|1
argument_list|,
name|Bytes
operator|.
name|indexOf
argument_list|(
name|ARRAY1
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
operator|-
literal|1
argument_list|,
name|Bytes
operator|.
name|indexOf
argument_list|(
name|ARRAY234
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
literal|0
argument_list|,
name|Bytes
operator|.
name|indexOf
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
operator|-
literal|1
block|}
argument_list|,
operator|(
name|byte
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
name|Bytes
operator|.
name|indexOf
argument_list|(
name|ARRAY234
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
literal|1
argument_list|,
name|Bytes
operator|.
name|indexOf
argument_list|(
name|ARRAY234
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
literal|2
argument_list|,
name|Bytes
operator|.
name|indexOf
argument_list|(
name|ARRAY234
argument_list|,
operator|(
name|byte
operator|)
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Bytes
operator|.
name|indexOf
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
operator|)
literal|3
block|,
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
operator|)
literal|3
block|}
argument_list|,
operator|(
name|byte
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
name|Bytes
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
name|Bytes
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
name|Bytes
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
name|Bytes
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
name|Bytes
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
name|Bytes
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
name|Bytes
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
name|Bytes
operator|.
name|indexOf
argument_list|(
name|ARRAY234
argument_list|,
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
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
name|Bytes
operator|.
name|indexOf
argument_list|(
name|ARRAY234
argument_list|,
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|3
block|,
operator|(
name|byte
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
name|Bytes
operator|.
name|indexOf
argument_list|(
name|ARRAY234
argument_list|,
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
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
name|Bytes
operator|.
name|indexOf
argument_list|(
name|ARRAY234
argument_list|,
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
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
name|Bytes
operator|.
name|indexOf
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
operator|)
literal|3
block|,
operator|(
name|byte
operator|)
literal|3
block|,
operator|(
name|byte
operator|)
literal|3
block|,
operator|(
name|byte
operator|)
literal|3
block|}
argument_list|,
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
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
name|Bytes
operator|.
name|indexOf
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
operator|)
literal|3
block|,
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
operator|)
literal|3
block|,
operator|(
name|byte
operator|)
literal|4
block|,
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
operator|)
literal|3
block|}
argument_list|,
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
operator|)
literal|3
block|,
operator|(
name|byte
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
name|Bytes
operator|.
name|indexOf
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
operator|)
literal|3
block|,
operator|(
name|byte
operator|)
literal|4
block|,
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
operator|)
literal|3
block|,
operator|(
name|byte
operator|)
literal|4
block|}
argument_list|,
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
operator|)
literal|3
block|,
operator|(
name|byte
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
name|Bytes
operator|.
name|indexOf
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|4
block|,
operator|(
name|byte
operator|)
literal|3
block|,
operator|(
name|byte
operator|)
literal|2
block|}
argument_list|,
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
operator|)
literal|3
block|,
operator|(
name|byte
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
name|Bytes
operator|.
name|lastIndexOf
argument_list|(
name|EMPTY
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
operator|-
literal|1
argument_list|,
name|Bytes
operator|.
name|lastIndexOf
argument_list|(
name|ARRAY1
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
operator|-
literal|1
argument_list|,
name|Bytes
operator|.
name|lastIndexOf
argument_list|(
name|ARRAY234
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
literal|0
argument_list|,
name|Bytes
operator|.
name|lastIndexOf
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
operator|-
literal|1
block|}
argument_list|,
operator|(
name|byte
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
name|Bytes
operator|.
name|lastIndexOf
argument_list|(
name|ARRAY234
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
literal|1
argument_list|,
name|Bytes
operator|.
name|lastIndexOf
argument_list|(
name|ARRAY234
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
literal|2
argument_list|,
name|Bytes
operator|.
name|lastIndexOf
argument_list|(
name|ARRAY234
argument_list|,
operator|(
name|byte
operator|)
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|Bytes
operator|.
name|lastIndexOf
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
operator|)
literal|3
block|,
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
operator|)
literal|3
block|}
argument_list|,
operator|(
name|byte
operator|)
literal|3
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
name|Bytes
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
name|Bytes
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
name|Bytes
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
name|Bytes
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
name|Bytes
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
name|Bytes
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
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|1
block|,
operator|(
name|byte
operator|)
literal|1
block|,
operator|(
name|byte
operator|)
literal|1
block|}
argument_list|,
name|Bytes
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
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|1
block|,
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
operator|)
literal|3
block|,
operator|(
name|byte
operator|)
literal|4
block|}
argument_list|,
name|Bytes
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
name|Bytes
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
name|Bytes
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
name|Bytes
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
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|1
block|,
operator|(
name|byte
operator|)
literal|0
block|,
operator|(
name|byte
operator|)
literal|0
block|}
argument_list|,
name|Bytes
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
name|Bytes
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
name|Bytes
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
DECL|method|testToArray ()
specifier|public
name|void
name|testToArray
parameter_list|()
block|{
comment|// need explicit type parameter to avoid javac warning!?
name|List
argument_list|<
name|Byte
argument_list|>
name|none
init|=
name|Arrays
operator|.
expr|<
name|Byte
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
name|Bytes
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
name|Byte
argument_list|>
name|one
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|byte
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
name|Bytes
operator|.
name|toArray
argument_list|(
name|one
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|byte
index|[]
name|array
init|=
block|{
operator|(
name|byte
operator|)
literal|0
block|,
operator|(
name|byte
operator|)
literal|1
block|,
operator|(
name|byte
operator|)
literal|0x55
block|}
decl_stmt|;
name|List
argument_list|<
name|Byte
argument_list|>
name|three
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|,
operator|(
name|byte
operator|)
literal|1
argument_list|,
operator|(
name|byte
operator|)
literal|0x55
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
name|Bytes
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
name|Bytes
operator|.
name|toArray
argument_list|(
name|Bytes
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
name|Byte
argument_list|>
name|list
init|=
name|Bytes
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
name|Byte
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
name|byte
index|[]
name|arr
init|=
name|Bytes
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
name|Byte
argument_list|>
name|list
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|,
operator|(
name|byte
operator|)
literal|1
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|Bytes
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
DECL|method|testToArray_withConversion ()
specifier|public
name|void
name|testToArray_withConversion
parameter_list|()
block|{
name|byte
index|[]
name|array
init|=
block|{
operator|(
name|byte
operator|)
literal|0
block|,
operator|(
name|byte
operator|)
literal|1
block|,
operator|(
name|byte
operator|)
literal|2
block|}
decl_stmt|;
name|List
argument_list|<
name|Byte
argument_list|>
name|bytes
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|byte
operator|)
literal|0
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
decl_stmt|;
name|List
argument_list|<
name|Short
argument_list|>
name|shorts
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|short
operator|)
literal|0
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|,
operator|(
name|short
operator|)
literal|2
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|ints
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Float
argument_list|>
name|floats
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|float
operator|)
literal|0
argument_list|,
operator|(
name|float
operator|)
literal|1
argument_list|,
operator|(
name|float
operator|)
literal|2
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Long
argument_list|>
name|longs
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|long
operator|)
literal|0
argument_list|,
operator|(
name|long
operator|)
literal|1
argument_list|,
operator|(
name|long
operator|)
literal|2
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Double
argument_list|>
name|doubles
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|double
operator|)
literal|0
argument_list|,
operator|(
name|double
operator|)
literal|1
argument_list|,
operator|(
name|double
operator|)
literal|2
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
name|Bytes
operator|.
name|toArray
argument_list|(
name|bytes
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
name|Bytes
operator|.
name|toArray
argument_list|(
name|shorts
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
name|Bytes
operator|.
name|toArray
argument_list|(
name|ints
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
name|Bytes
operator|.
name|toArray
argument_list|(
name|floats
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
name|Bytes
operator|.
name|toArray
argument_list|(
name|longs
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
name|Bytes
operator|.
name|toArray
argument_list|(
name|doubles
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsList_isAView ()
specifier|public
name|void
name|testAsList_isAView
parameter_list|()
block|{
name|byte
index|[]
name|array
init|=
block|{
operator|(
name|byte
operator|)
literal|0
block|,
operator|(
name|byte
operator|)
literal|1
block|}
decl_stmt|;
name|List
argument_list|<
name|Byte
argument_list|>
name|list
init|=
name|Bytes
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
name|byte
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
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
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
name|byte
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
name|byte
operator|)
literal|2
argument_list|,
operator|(
name|byte
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
name|byte
index|[]
name|array
init|=
block|{
operator|(
name|byte
operator|)
literal|0
block|,
operator|(
name|byte
operator|)
literal|1
block|,
operator|(
name|byte
operator|)
literal|2
block|}
decl_stmt|;
name|List
argument_list|<
name|Byte
argument_list|>
name|list
init|=
name|Bytes
operator|.
name|asList
argument_list|(
name|array
argument_list|)
decl_stmt|;
name|byte
index|[]
name|newArray
init|=
name|Bytes
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
name|byte
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
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0
block|,
operator|(
name|byte
operator|)
literal|1
block|,
operator|(
name|byte
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
name|byte
operator|)
literal|5
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|1
argument_list|,
operator|(
name|byte
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
name|byte
index|[]
name|array
init|=
block|{
operator|(
name|byte
operator|)
literal|0
block|,
operator|(
name|byte
operator|)
literal|1
block|,
operator|(
name|byte
operator|)
literal|2
block|,
operator|(
name|byte
operator|)
literal|3
block|}
decl_stmt|;
name|List
argument_list|<
name|Byte
argument_list|>
name|list
init|=
name|Bytes
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
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|1
block|,
operator|(
name|byte
operator|)
literal|2
block|}
argument_list|,
name|Bytes
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
name|byte
index|[]
block|{}
argument_list|,
name|Bytes
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
name|Bytes
operator|.
name|asList
argument_list|(
name|EMPTY
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
name|Bytes
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

