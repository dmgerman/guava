begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|truth0
operator|.
name|Truth
operator|.
name|ASSERT
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
name|base
operator|.
name|Objects
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Test cases for {@link Table} read operations.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|AbstractTableReadTest
specifier|public
specifier|abstract
class|class
name|AbstractTableReadTest
extends|extends
name|TestCase
block|{
DECL|field|table
specifier|protected
name|Table
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
name|table
decl_stmt|;
comment|/**    * Creates a table with the specified data.    *    * @param data the table data, repeating the sequence row key, column key,    *     value once per mapping    * @throws IllegalArgumentException if the size of {@code data} isn't a    *     multiple of 3    * @throws ClassCastException if a data element has the wrong type    */
specifier|protected
specifier|abstract
name|Table
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
DECL|method|create (Object... data)
name|create
parameter_list|(
name|Object
modifier|...
name|data
parameter_list|)
function_decl|;
DECL|method|assertSize (int expectedSize)
specifier|protected
name|void
name|assertSize
parameter_list|(
name|int
name|expectedSize
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|expectedSize
argument_list|,
name|table
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setUp ()
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|table
operator|=
name|create
argument_list|()
expr_stmt|;
block|}
DECL|method|testContains ()
specifier|public
name|void
name|testContains
parameter_list|()
block|{
name|table
operator|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|,
literal|"foo"
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|table
operator|.
name|contains
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|table
operator|.
name|contains
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|table
operator|.
name|contains
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|table
operator|.
name|contains
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|table
operator|.
name|contains
argument_list|(
literal|"bar"
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|table
operator|.
name|contains
argument_list|(
literal|"cat"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|table
operator|.
name|contains
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|table
operator|.
name|contains
argument_list|(
literal|null
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|table
operator|.
name|contains
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testContainsRow ()
specifier|public
name|void
name|testContainsRow
parameter_list|()
block|{
name|table
operator|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|,
literal|"foo"
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|table
operator|.
name|containsRow
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|table
operator|.
name|containsRow
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|table
operator|.
name|containsRow
argument_list|(
literal|"cat"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|table
operator|.
name|containsRow
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testContainsColumn ()
specifier|public
name|void
name|testContainsColumn
parameter_list|()
block|{
name|table
operator|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|,
literal|"foo"
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|table
operator|.
name|containsColumn
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|table
operator|.
name|containsColumn
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|table
operator|.
name|containsColumn
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|table
operator|.
name|containsColumn
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testContainsValue ()
specifier|public
name|void
name|testContainsValue
parameter_list|()
block|{
name|table
operator|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|,
literal|"foo"
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|table
operator|.
name|containsValue
argument_list|(
literal|'a'
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|table
operator|.
name|containsValue
argument_list|(
literal|'b'
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|table
operator|.
name|containsValue
argument_list|(
literal|'c'
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|table
operator|.
name|containsValue
argument_list|(
literal|'x'
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|table
operator|.
name|containsValue
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGet ()
specifier|public
name|void
name|testGet
parameter_list|()
block|{
name|table
operator|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|,
literal|"foo"
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Character
operator|)
literal|'a'
argument_list|,
name|table
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Character
operator|)
literal|'b'
argument_list|,
name|table
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Character
operator|)
literal|'c'
argument_list|,
name|table
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|table
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|table
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|table
operator|.
name|get
argument_list|(
literal|"cat"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|table
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|table
operator|.
name|get
argument_list|(
literal|null
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|table
operator|.
name|get
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsEmpty ()
specifier|public
name|void
name|testIsEmpty
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|table
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|,
literal|"foo"
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|table
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSize ()
specifier|public
name|void
name|testSize
parameter_list|()
block|{
name|assertSize
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|table
operator|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|,
literal|"foo"
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|)
expr_stmt|;
name|assertSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
name|table
operator|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|,
literal|"foo"
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|)
expr_stmt|;
name|Table
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
name|hashCopy
init|=
name|HashBasedTable
operator|.
name|create
argument_list|(
name|table
argument_list|)
decl_stmt|;
name|Table
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
name|reordered
init|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|,
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|)
decl_stmt|;
name|Table
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
name|smaller
init|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|)
decl_stmt|;
name|Table
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
name|swapOuter
init|=
name|create
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|,
literal|"bar"
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|)
decl_stmt|;
name|Table
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
name|swapValues
init|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'c'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|,
literal|"foo"
argument_list|,
literal|3
argument_list|,
literal|'a'
argument_list|)
decl_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|table
argument_list|,
name|hashCopy
argument_list|,
name|reordered
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|smaller
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|swapOuter
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|swapValues
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testHashCode ()
specifier|public
name|void
name|testHashCode
parameter_list|()
block|{
name|table
operator|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|,
literal|"foo"
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|)
expr_stmt|;
name|int
name|expected
init|=
name|Objects
operator|.
name|hashCode
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|)
operator|+
name|Objects
operator|.
name|hashCode
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|)
operator|+
name|Objects
operator|.
name|hashCode
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|table
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToStringSize1 ()
specifier|public
name|void
name|testToStringSize1
parameter_list|()
block|{
name|table
operator|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{foo={1=a}}"
argument_list|,
name|table
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRow ()
specifier|public
name|void
name|testRow
parameter_list|()
block|{
name|table
operator|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|,
literal|"foo"
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|)
argument_list|,
name|table
operator|.
name|row
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// This test assumes that the implementation does not support null keys.
DECL|method|testRowNull ()
specifier|public
name|void
name|testRowNull
parameter_list|()
block|{
name|table
operator|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|,
literal|"foo"
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|)
expr_stmt|;
try|try
block|{
name|table
operator|.
name|row
argument_list|(
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
block|{}
block|}
DECL|method|testColumn ()
specifier|public
name|void
name|testColumn
parameter_list|()
block|{
name|table
operator|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|,
literal|"foo"
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|'b'
argument_list|)
argument_list|,
name|table
operator|.
name|column
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// This test assumes that the implementation does not support null keys.
DECL|method|testColumnNull ()
specifier|public
name|void
name|testColumnNull
parameter_list|()
block|{
name|table
operator|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|,
literal|"foo"
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|)
expr_stmt|;
try|try
block|{
name|table
operator|.
name|column
argument_list|(
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
block|{}
block|}
DECL|method|testColumnSetPartialOverlap ()
specifier|public
name|void
name|testColumnSetPartialOverlap
parameter_list|()
block|{
name|table
operator|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|,
literal|"foo"
argument_list|,
literal|2
argument_list|,
literal|'c'
argument_list|,
literal|"bar"
argument_list|,
literal|3
argument_list|,
literal|'d'
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|table
operator|.
name|columnKeySet
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|allOf
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

