begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
import|;
end_import

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
name|testing
operator|.
name|EqualsTester
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
name|Iterator
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|RandomAccess
import|;
end_import

begin_comment
comment|/**  * Tests for {@code LinkedListMultimap}.  *  * @author Mike Bostock  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|LinkedListMultimapTest
specifier|public
class|class
name|LinkedListMultimapTest
extends|extends
name|AbstractListMultimapTest
block|{
DECL|method|create ()
annotation|@
name|Override
specifier|protected
name|LinkedListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|create
parameter_list|()
block|{
return|return
name|LinkedListMultimap
operator|.
name|create
argument_list|()
return|;
block|}
comment|/**    * Confirm that get() returns a List that doesn't implement RandomAccess.    */
DECL|method|testGetRandomAccess ()
specifier|public
name|void
name|testGetRandomAccess
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|create
argument_list|()
decl_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
block|}
comment|/**    * Confirm that removeAll() returns a List that implements RandomAccess, even    * though get() doesn't.    */
DECL|method|testRemoveAllRandomAccess ()
specifier|public
name|void
name|testRemoveAllRandomAccess
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|create
argument_list|()
decl_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|removeAll
argument_list|(
literal|"foo"
argument_list|)
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|removeAll
argument_list|(
literal|"bar"
argument_list|)
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
block|}
comment|/**    * Confirm that replaceValues() returns a List that implements RandomAccess,    * even though get() doesn't.    */
DECL|method|testReplaceValuesRandomAccess ()
specifier|public
name|void
name|testReplaceValuesRandomAccess
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|create
argument_list|()
decl_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|replaceValues
argument_list|(
literal|"foo"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|2
argument_list|,
literal|4
argument_list|)
argument_list|)
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|replaceValues
argument_list|(
literal|"bar"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|2
argument_list|,
literal|4
argument_list|)
argument_list|)
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateFromMultimap ()
specifier|public
name|void
name|testCreateFromMultimap
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|createSample
argument_list|()
decl_stmt|;
name|LinkedListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|copy
init|=
name|LinkedListMultimap
operator|.
name|create
argument_list|(
name|multimap
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|multimap
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateFromSize ()
specifier|public
name|void
name|testCreateFromSize
parameter_list|()
block|{
name|LinkedListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|LinkedListMultimap
operator|.
name|create
argument_list|(
literal|20
argument_list|)
decl_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|)
argument_list|,
name|multimap
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateFromIllegalSize ()
specifier|public
name|void
name|testCreateFromIllegalSize
parameter_list|()
block|{
try|try
block|{
name|LinkedListMultimap
operator|.
name|create
argument_list|(
operator|-
literal|20
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
block|{}
block|}
comment|/* "Linked" prefix avoids collision with AbstractMultimapTest. */
DECL|method|testLinkedToString ()
specifier|public
name|void
name|testLinkedToString
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"{foo=[3, -1, 2, 4, 1], bar=[1, 2, 3, 1]}"
argument_list|,
name|createSample
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLinkedGetAdd ()
specifier|public
name|void
name|testLinkedGetAdd
parameter_list|()
block|{
name|LinkedListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|create
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Integer
argument_list|>
name|foos
init|=
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|foos
operator|.
name|add
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|foos
operator|.
name|add
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{bar=[1, 4], foo=[2, 3, 5]}"
argument_list|,
name|map
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[bar=1, foo=2, foo=3, bar=4, foo=5]"
argument_list|,
name|map
operator|.
name|entries
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLinkedGetInsert ()
specifier|public
name|void
name|testLinkedGetInsert
parameter_list|()
block|{
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|create
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|foos
init|=
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|foos
operator|.
name|add
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|foos
operator|.
name|add
argument_list|(
literal|0
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{bar=[1, 4], foo=[3, 2, 5]}"
argument_list|,
name|map
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[bar=1, foo=3, foo=2, bar=4, foo=5]"
argument_list|,
name|map
operator|.
name|entries
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLinkedPutInOrder ()
specifier|public
name|void
name|testLinkedPutInOrder
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|create
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{foo=[1], bar=[2, 3]}"
argument_list|,
name|map
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[foo=1, bar=2, bar=3]"
argument_list|,
name|map
operator|.
name|entries
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLinkedPutOutOfOrder ()
specifier|public
name|void
name|testLinkedPutOutOfOrder
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|create
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{bar=[1, 3], foo=[2]}"
argument_list|,
name|map
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[bar=1, foo=2, bar=3]"
argument_list|,
name|map
operator|.
name|entries
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLinkedPutAllMultimap ()
specifier|public
name|void
name|testLinkedPutAllMultimap
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|src
init|=
name|create
argument_list|()
decl_stmt|;
name|src
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|src
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|src
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|dst
init|=
name|create
argument_list|()
decl_stmt|;
name|dst
operator|.
name|putAll
argument_list|(
name|src
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{bar=[1, 3], foo=[2]}"
argument_list|,
name|dst
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[bar=1, foo=2, bar=3]"
argument_list|,
name|src
operator|.
name|entries
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLinkedReplaceValues ()
specifier|public
name|void
name|testLinkedReplaceValues
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|create
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{bar=[1, 3, 4], foo=[2]}"
argument_list|,
name|map
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|replaceValues
argument_list|(
literal|"bar"
argument_list|,
name|asList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[bar=1, foo=2, bar=2]"
argument_list|,
name|map
operator|.
name|entries
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{bar=[1, 2], foo=[2]}"
argument_list|,
name|map
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLinkedClear ()
specifier|public
name|void
name|testLinkedClear
parameter_list|()
block|{
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|create
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|foos
init|=
name|map
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|Integer
argument_list|>
name|values
init|=
name|map
operator|.
name|values
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|asList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|,
name|foos
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|values
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
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|,
name|foos
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|values
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[]"
argument_list|,
name|map
operator|.
name|entries
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{}"
argument_list|,
name|map
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLinkedKeySet ()
specifier|public
name|void
name|testLinkedKeySet
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|create
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[bar, foo]"
argument_list|,
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|remove
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{foo=[2]}"
argument_list|,
name|map
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLinkedKeys ()
specifier|public
name|void
name|testLinkedKeys
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|create
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[bar=1, foo=2, bar=3, bar=4]"
argument_list|,
name|map
operator|.
name|entries
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|map
operator|.
name|keys
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|allOf
argument_list|(
literal|"bar"
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|"bar"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|map
operator|.
name|keys
argument_list|()
operator|.
name|remove
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
comment|// bar is no longer the first key!
name|assertEquals
argument_list|(
literal|"{foo=[2], bar=[3, 4]}"
argument_list|,
name|map
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLinkedValues ()
specifier|public
name|void
name|testLinkedValues
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|create
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[1, 2, 3, 4]"
argument_list|,
name|map
operator|.
name|values
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|values
argument_list|()
operator|.
name|remove
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{bar=[1, 3, 4]}"
argument_list|,
name|map
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLinkedEntries ()
specifier|public
name|void
name|testLinkedEntries
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|create
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|entries
init|=
name|map
operator|.
name|entries
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|entry
init|=
name|entries
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
operator|(
name|int
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|entry
operator|=
name|entries
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
operator|(
name|int
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setValue
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|entry
operator|=
name|entries
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
operator|(
name|int
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entries
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|entries
operator|.
name|remove
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{bar=[1], foo=[4]}"
argument_list|,
name|map
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLinkedAsMapEntries ()
specifier|public
name|void
name|testLinkedAsMapEntries
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|create
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|Integer
argument_list|>
argument_list|>
argument_list|>
name|entries
init|=
name|map
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|entry
init|=
name|entries
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|entry
operator|.
name|getValue
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
literal|3
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
try|try
block|{
name|entry
operator|.
name|setValue
argument_list|(
name|Arrays
operator|.
expr|<
name|Integer
operator|>
name|asList
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"UnsupportedOperationException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{}
name|entries
operator|.
name|remove
argument_list|()
expr_stmt|;
comment|// clear
name|entry
operator|=
name|entries
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|item
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entries
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{foo=[2]}"
argument_list|,
name|map
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEntriesAfterMultimapUpdate ()
specifier|public
name|void
name|testEntriesAfterMultimapUpdate
parameter_list|()
block|{
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|create
argument_list|()
decl_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|entries
init|=
name|multimap
operator|.
name|entries
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|iterator
init|=
name|entries
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|entrya
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|entryb
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
operator|(
name|int
operator|)
name|multimap
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|set
argument_list|(
literal|0
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|multimap
operator|.
name|containsEntry
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|containsEntry
argument_list|(
literal|"foo"
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|containsEntry
argument_list|(
literal|"bar"
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
operator|(
name|int
operator|)
name|entrya
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
operator|(
name|int
operator|)
name|entryb
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|containsEntry
argument_list|(
literal|"foo"
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|containsEntry
argument_list|(
literal|"foo"
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|containsEntry
argument_list|(
literal|"bar"
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
operator|(
name|int
operator|)
name|entrya
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
operator|(
name|int
operator|)
name|entryb
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|LinkedListMultimap
operator|.
name|create
argument_list|()
argument_list|,
name|LinkedListMultimap
operator|.
name|create
argument_list|()
argument_list|,
name|LinkedListMultimap
operator|.
name|create
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

