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
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Tests for {@code Multimap} implementations. Caution: when subclassing avoid  * accidental naming collisions with tests in this class!  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|AbstractMultimapTest
specifier|public
specifier|abstract
class|class
name|AbstractMultimapTest
extends|extends
name|TestCase
block|{
DECL|field|multimap
specifier|private
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
decl_stmt|;
DECL|method|create ()
specifier|protected
specifier|abstract
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|create
parameter_list|()
function_decl|;
DECL|method|createSample ()
specifier|protected
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|createSample
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|sample
init|=
name|create
argument_list|()
decl_stmt|;
name|sample
operator|.
name|putAll
argument_list|(
literal|"foo"
argument_list|,
name|asList
argument_list|(
literal|3
argument_list|,
operator|-
literal|1
argument_list|,
literal|2
argument_list|,
literal|4
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|sample
operator|.
name|putAll
argument_list|(
literal|"bar"
argument_list|,
name|asList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|sample
return|;
block|}
comment|// public for GWT
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
name|multimap
operator|=
name|create
argument_list|()
expr_stmt|;
block|}
DECL|method|getMultimap ()
specifier|protected
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|getMultimap
parameter_list|()
block|{
return|return
name|multimap
return|;
block|}
comment|/**    * Returns the key to use as a null placeholder in tests. The default    * implementation returns {@code null}, but tests for multimaps that don't    * support null keys should override it.    */
DECL|method|nullKey ()
specifier|protected
name|String
name|nullKey
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**    * Returns the value to use as a null placeholder in tests. The default    * implementation returns {@code null}, but tests for multimaps that don't    * support null values should override it.    */
DECL|method|nullValue ()
specifier|protected
name|Integer
name|nullValue
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**    * Validate multimap size by calling {@code size()} and also by iterating    * through the entries. This tests cases where the {@code entries()} list is    * stored separately, such as the {@link LinkedHashMultimap}. It also    * verifies that the multimap contains every multimap entry.    */
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
name|multimap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|size
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|entry
range|:
name|multimap
operator|.
name|entries
argument_list|()
control|)
block|{
name|assertTrue
argument_list|(
name|multimap
operator|.
name|containsEntry
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|size
operator|++
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|expectedSize
argument_list|,
name|size
argument_list|)
expr_stmt|;
name|int
name|size2
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|entry2
range|:
name|multimap
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|size2
operator|+=
name|entry2
operator|.
name|getValue
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|expectedSize
argument_list|,
name|size2
argument_list|)
expr_stmt|;
block|}
DECL|method|removedCollectionsAreModifiable ()
specifier|protected
name|boolean
name|removedCollectionsAreModifiable
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|testKeySet ()
specifier|public
name|void
name|testKeySet
parameter_list|()
block|{
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
name|nullValue
argument_list|()
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
name|nullKey
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|multimap
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|keys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|keys
operator|.
name|contains
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|keys
operator|.
name|contains
argument_list|(
name|nullKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|keys
operator|.
name|containsAll
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|"foo"
argument_list|,
name|nullKey
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|keys
operator|.
name|containsAll
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testValues ()
specifier|public
name|void
name|testValues
parameter_list|()
block|{
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
name|nullValue
argument_list|()
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
name|nullKey
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Integer
argument_list|>
name|values
init|=
name|multimap
operator|.
name|values
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|values
operator|.
name|contains
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|values
operator|.
name|contains
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|values
operator|.
name|contains
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|values
operator|.
name|contains
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// the entries collection is more thoroughly tested in MultimapCollectionTest
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// varargs
DECL|method|testEntries ()
specifier|public
name|void
name|testEntries
parameter_list|()
block|{
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
name|nullValue
argument_list|()
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
name|nullKey
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
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
name|ASSERT
operator|.
name|that
argument_list|(
name|entries
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|allOf
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|"foo"
argument_list|,
name|nullValue
argument_list|()
argument_list|)
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|nullKey
argument_list|()
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testKeys ()
specifier|public
name|void
name|testKeys
parameter_list|()
block|{
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
literal|5
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
name|nullValue
argument_list|()
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
name|nullKey
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
name|multimap
operator|.
name|keys
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|multiset
operator|.
name|count
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|multiset
operator|.
name|count
argument_list|(
name|nullKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|multiset
operator|.
name|elementSet
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|allOf
argument_list|(
literal|"foo"
argument_list|,
name|nullKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|multiset
operator|.
name|entrySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|multiset
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|String
argument_list|>
argument_list|>
name|entries
init|=
name|multimap
operator|.
name|keys
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|entries
operator|.
name|contains
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entries
operator|.
name|contains
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entries
operator|.
name|contains
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|foo3null1
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|asList
argument_list|(
literal|"foo"
argument_list|,
literal|"foo"
argument_list|,
name|nullKey
argument_list|()
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|foo3null1
argument_list|,
name|multiset
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|multiset
argument_list|,
name|foo3null1
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|multiset
operator|.
name|equals
argument_list|(
name|HashMultiset
operator|.
name|create
argument_list|(
name|asList
argument_list|(
literal|"foo"
argument_list|,
literal|"foo"
argument_list|,
name|nullKey
argument_list|()
argument_list|,
name|nullKey
argument_list|()
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|foo3null1
operator|.
name|hashCode
argument_list|()
argument_list|,
name|multiset
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|foo3null1
operator|.
name|entrySet
argument_list|()
argument_list|,
name|multiset
operator|.
name|entrySet
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|multiset
operator|.
name|entrySet
argument_list|()
argument_list|,
name|foo3null1
operator|.
name|entrySet
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|foo3null1
operator|.
name|entrySet
argument_list|()
operator|.
name|hashCode
argument_list|()
argument_list|,
name|multiset
operator|.
name|entrySet
argument_list|()
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|multiset
operator|.
name|remove
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|multiset
operator|.
name|remove
argument_list|(
name|nullKey
argument_list|()
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|multimap
operator|.
name|containsKey
argument_list|(
name|nullKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|entries
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|multiset
operator|.
name|remove
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|containsKey
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|multiset
operator|.
name|setCount
argument_list|(
literal|"foo"
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|multiset
operator|.
name|setCount
argument_list|(
literal|"bar"
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testKeysEntrySetIterator ()
specifier|public
name|void
name|testKeysEntrySetIterator
parameter_list|()
block|{
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|8
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|String
argument_list|>
argument_list|>
name|iterator
init|=
name|multimap
operator|.
name|keys
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|iterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
argument_list|,
name|iterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|iterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertSize
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|testKeysEntrySetRemove ()
specifier|public
name|void
name|testKeysEntrySetRemove
parameter_list|()
block|{
name|multimap
operator|.
name|putAll
argument_list|(
literal|"foo"
argument_list|,
name|asList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|putAll
argument_list|(
literal|"bar"
argument_list|,
name|asList
argument_list|(
literal|4
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|String
argument_list|>
argument_list|>
name|entries
init|=
name|multimap
operator|.
name|keys
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|entries
operator|.
name|remove
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
literal|"bar"
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[foo x 3]"
argument_list|,
name|multimap
operator|.
name|keys
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// doesn't exist in entries, should have no effect
name|assertFalse
argument_list|(
name|entries
operator|.
name|remove
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[foo x 3]"
argument_list|,
name|multimap
operator|.
name|keys
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Multimap size after keys().entrySet().remove(entry)"
argument_list|,
literal|3
argument_list|,
name|multimap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testKeySetRemove ()
specifier|public
name|void
name|testKeySetRemove
parameter_list|()
block|{
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
name|nullValue
argument_list|()
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
name|nullKey
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|multimap
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|keys
operator|.
name|remove
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|keys
operator|.
name|remove
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|multimap
operator|.
name|containsKey
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|containsEntry
argument_list|(
name|nullKey
argument_list|()
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testKeySetIterator ()
specifier|public
name|void
name|testKeySetIterator
parameter_list|()
block|{
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
name|nullValue
argument_list|()
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
name|nullKey
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
init|=
name|multimap
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|key
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"foo"
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
name|assertSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|multimap
operator|.
name|containsKey
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|containsEntry
argument_list|(
name|nullKey
argument_list|()
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|iterator
operator|=
name|multimap
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|nullKey
argument_list|()
argument_list|,
name|iterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertSize
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|testValuesIteratorRemove ()
specifier|public
name|void
name|testValuesIteratorRemove
parameter_list|()
block|{
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
literal|2
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
name|nullKey
argument_list|()
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|iterator
init|=
name|multimap
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|int
name|value
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|value
operator|%
literal|2
operator|)
operator|==
literal|0
condition|)
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
name|assertSize
argument_list|(
literal|1
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
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"SerializableTester"
argument_list|)
DECL|method|testSerializable ()
specifier|public
name|void
name|testSerializable
parameter_list|()
block|{
name|multimap
operator|=
name|createSample
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|multimap
argument_list|,
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|multimap
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

