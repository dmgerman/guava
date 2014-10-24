begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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

begin_comment
comment|/**  * Tests {@link SingletonImmutableTable}.  *  * @author Gregory Kick  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|SingletonImmutableTableTest
specifier|public
class|class
name|SingletonImmutableTableTest
extends|extends
name|AbstractImmutableTableTest
block|{
DECL|field|testTable
specifier|private
specifier|final
name|ImmutableTable
argument_list|<
name|Character
argument_list|,
name|Integer
argument_list|,
name|String
argument_list|>
name|testTable
init|=
operator|new
name|SingletonImmutableTable
argument_list|<
name|Character
argument_list|,
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|(
literal|'a'
argument_list|,
literal|1
argument_list|,
literal|"blah"
argument_list|)
decl_stmt|;
DECL|method|testHashCode ()
specifier|public
name|void
name|testHashCode
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Objects
operator|.
name|hashCode
argument_list|(
literal|'a'
argument_list|,
literal|1
argument_list|,
literal|"blah"
argument_list|)
argument_list|,
name|testTable
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCellSet ()
specifier|public
name|void
name|testCellSet
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|Tables
operator|.
name|immutableCell
argument_list|(
literal|'a'
argument_list|,
literal|1
argument_list|,
literal|"blah"
argument_list|)
argument_list|)
argument_list|,
name|testTable
operator|.
name|cellSet
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testColumn ()
specifier|public
name|void
name|testColumn
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|,
name|testTable
operator|.
name|column
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|'a'
argument_list|,
literal|"blah"
argument_list|)
argument_list|,
name|testTable
operator|.
name|column
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testColumnKeySet ()
specifier|public
name|void
name|testColumnKeySet
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|1
argument_list|)
argument_list|,
name|testTable
operator|.
name|columnKeySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testColumnMap ()
specifier|public
name|void
name|testColumnMap
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|1
argument_list|,
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|'a'
argument_list|,
literal|"blah"
argument_list|)
argument_list|)
argument_list|,
name|testTable
operator|.
name|columnMap
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
name|assertEquals
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|,
name|testTable
operator|.
name|row
argument_list|(
literal|'A'
argument_list|)
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
literal|"blah"
argument_list|)
argument_list|,
name|testTable
operator|.
name|row
argument_list|(
literal|'a'
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testRowKeySet ()
specifier|public
name|void
name|testRowKeySet
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|'a'
argument_list|)
argument_list|,
name|testTable
operator|.
name|rowKeySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRowMap ()
specifier|public
name|void
name|testRowMap
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|'a'
argument_list|,
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|"blah"
argument_list|)
argument_list|)
argument_list|,
name|testTable
operator|.
name|rowMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEqualsObject ()
specifier|public
name|void
name|testEqualsObject
parameter_list|()
block|{
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|testTable
argument_list|,
name|HashBasedTable
operator|.
name|create
argument_list|(
name|testTable
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|ImmutableTable
operator|.
name|of
argument_list|()
argument_list|,
name|HashBasedTable
operator|.
name|create
argument_list|()
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|HashBasedTable
operator|.
name|create
argument_list|(
name|ImmutableTable
operator|.
name|of
argument_list|(
literal|'A'
argument_list|,
literal|2
argument_list|,
literal|""
argument_list|)
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"{a={1=blah}}"
argument_list|,
name|testTable
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testContains ()
specifier|public
name|void
name|testContains
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|testTable
operator|.
name|contains
argument_list|(
literal|'a'
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|testTable
operator|.
name|contains
argument_list|(
literal|'a'
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|testTable
operator|.
name|contains
argument_list|(
literal|'A'
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|testTable
operator|.
name|contains
argument_list|(
literal|'A'
argument_list|,
literal|2
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
name|assertTrue
argument_list|(
name|testTable
operator|.
name|containsColumn
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|testTable
operator|.
name|containsColumn
argument_list|(
literal|2
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
name|assertTrue
argument_list|(
name|testTable
operator|.
name|containsRow
argument_list|(
literal|'a'
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|testTable
operator|.
name|containsRow
argument_list|(
literal|'A'
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
name|assertTrue
argument_list|(
name|testTable
operator|.
name|containsValue
argument_list|(
literal|"blah"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|testTable
operator|.
name|containsValue
argument_list|(
literal|""
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
name|assertEquals
argument_list|(
literal|"blah"
argument_list|,
name|testTable
operator|.
name|get
argument_list|(
literal|'a'
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|testTable
operator|.
name|get
argument_list|(
literal|'a'
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|testTable
operator|.
name|get
argument_list|(
literal|'A'
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|testTable
operator|.
name|get
argument_list|(
literal|'A'
argument_list|,
literal|2
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
name|assertFalse
argument_list|(
name|testTable
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
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|testTable
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testValues ()
specifier|public
name|void
name|testValues
parameter_list|()
block|{
name|assertThat
argument_list|(
name|testTable
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"blah"
argument_list|)
expr_stmt|;
block|}
DECL|method|getTestInstances ()
annotation|@
name|Override
name|Iterable
argument_list|<
name|ImmutableTable
argument_list|<
name|Character
argument_list|,
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|>
name|getTestInstances
parameter_list|()
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|(
name|testTable
argument_list|)
return|;
block|}
block|}
end_class

end_unit

