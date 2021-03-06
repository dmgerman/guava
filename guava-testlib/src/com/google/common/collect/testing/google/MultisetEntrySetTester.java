begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.google
package|package
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
name|google
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
name|collect
operator|.
name|testing
operator|.
name|features
operator|.
name|CollectionFeature
operator|.
name|SUPPORTS_ADD
import|;
end_import

begin_import
import|import static
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
name|features
operator|.
name|CollectionFeature
operator|.
name|SUPPORTS_ITERATOR_REMOVE
import|;
end_import

begin_import
import|import static
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
name|features
operator|.
name|CollectionFeature
operator|.
name|SUPPORTS_REMOVE
import|;
end_import

begin_import
import|import static
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
name|features
operator|.
name|CollectionSize
operator|.
name|ONE
import|;
end_import

begin_import
import|import static
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
name|features
operator|.
name|CollectionSize
operator|.
name|SEVERAL
import|;
end_import

begin_import
import|import static
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
name|features
operator|.
name|CollectionSize
operator|.
name|ZERO
import|;
end_import

begin_import
import|import static
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
name|google
operator|.
name|MultisetFeature
operator|.
name|ENTRIES_ARE_VIEWS
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
name|collect
operator|.
name|Iterables
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
name|Multiset
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
name|Multisets
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
name|features
operator|.
name|CollectionFeature
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
name|features
operator|.
name|CollectionSize
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
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_comment
comment|/**  * Tests for {@code Multiset.entrySet}.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|Ignore
comment|// Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
DECL|class|MultisetEntrySetTester
specifier|public
class|class
name|MultisetEntrySetTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractMultisetTester
argument_list|<
name|E
argument_list|>
block|{
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testEntrySet_clear ()
specifier|public
name|void
name|testEntrySet_clear
parameter_list|()
block|{
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"multiset not empty after entrySet().clear()"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ITERATOR_REMOVE
argument_list|)
DECL|method|testEntrySet_iteratorRemovePropagates ()
specifier|public
name|void
name|testEntrySet_iteratorRemovePropagates
parameter_list|()
block|{
name|Iterator
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|iterator
init|=
name|getMultiset
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
literal|"non-empty multiset.entrySet() iterator.hasNext() returned false"
argument_list|,
name|iterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"multiset.entrySet() iterator.next() returned incorrect entry"
argument_list|,
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|1
argument_list|)
argument_list|,
name|iterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"size 1 multiset.entrySet() iterator.hasNext() returned true after next()"
argument_list|,
name|iterator
operator|.
name|hasNext
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
literal|"multiset isn't empty after multiset.entrySet() iterator.remove()"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testEntrySet_removePresent ()
specifier|public
name|void
name|testEntrySet_removePresent
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"multiset.entrySet.remove(presentEntry) returned false"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|remove
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"multiset contains element after removing its entry"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|contains
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testEntrySet_removeAbsent ()
specifier|public
name|void
name|testEntrySet_removeAbsent
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"multiset.entrySet.remove(missingEntry) returned true"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|remove
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"multiset didn't contain element after removing a missing entry"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|contains
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testEntrySet_removeAllPresent ()
specifier|public
name|void
name|testEntrySet_removeAllPresent
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"multiset.entrySet.removeAll(presentEntry) returned false"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|removeAll
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"multiset contains element after removing its entry"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|contains
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testEntrySet_removeAllAbsent ()
specifier|public
name|void
name|testEntrySet_removeAllAbsent
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"multiset.entrySet.remove(missingEntry) returned true"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|removeAll
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"multiset didn't contain element after removing a missing entry"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|contains
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testEntrySet_retainAllPresent ()
specifier|public
name|void
name|testEntrySet_retainAllPresent
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"multiset.entrySet.retainAll(presentEntry) returned false"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|retainAll
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"multiset doesn't contains element after retaining its entry"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|contains
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testEntrySet_retainAllAbsent ()
specifier|public
name|void
name|testEntrySet_retainAllAbsent
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"multiset.entrySet.retainAll(missingEntry) returned true"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|retainAll
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"multiset contains element after retaining a different entry"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|contains
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|MultisetFeature
operator|.
name|Require
argument_list|(
name|ENTRIES_ARE_VIEWS
argument_list|)
DECL|method|testEntryViewReflectsRemove ()
specifier|public
name|void
name|testEntryViewReflectsRemove
parameter_list|()
block|{
name|initThreeCopies
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|elementSet
argument_list|()
operator|.
name|remove
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ITERATOR_REMOVE
argument_list|)
annotation|@
name|MultisetFeature
operator|.
name|Require
argument_list|(
name|ENTRIES_ARE_VIEWS
argument_list|)
DECL|method|testEntryReflectsIteratorRemove ()
specifier|public
name|void
name|testEntryReflectsIteratorRemove
parameter_list|()
block|{
name|initThreeCopies
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|E
argument_list|>
name|itr
init|=
name|getMultiset
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|itr
operator|.
name|next
argument_list|()
expr_stmt|;
name|itr
operator|.
name|remove
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|itr
operator|.
name|next
argument_list|()
expr_stmt|;
name|itr
operator|.
name|remove
argument_list|()
expr_stmt|;
name|itr
operator|.
name|next
argument_list|()
expr_stmt|;
name|itr
operator|.
name|remove
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|MultisetFeature
operator|.
name|Require
argument_list|(
name|ENTRIES_ARE_VIEWS
argument_list|)
DECL|method|testEntryReflectsClear ()
specifier|public
name|void
name|testEntryReflectsClear
parameter_list|()
block|{
name|initThreeCopies
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|getMultiset
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|MultisetFeature
operator|.
name|Require
argument_list|(
name|ENTRIES_ARE_VIEWS
argument_list|)
DECL|method|testEntryReflectsEntrySetClear ()
specifier|public
name|void
name|testEntryReflectsEntrySetClear
parameter_list|()
block|{
name|initThreeCopies
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ITERATOR_REMOVE
argument_list|)
annotation|@
name|MultisetFeature
operator|.
name|Require
argument_list|(
name|ENTRIES_ARE_VIEWS
argument_list|)
DECL|method|testEntryReflectsEntrySetIteratorRemove ()
specifier|public
name|void
name|testEntryReflectsEntrySetIteratorRemove
parameter_list|()
block|{
name|initThreeCopies
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entryItr
init|=
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
init|=
name|entryItr
operator|.
name|next
argument_list|()
decl_stmt|;
name|entryItr
operator|.
name|remove
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|MultisetFeature
operator|.
name|Require
argument_list|(
name|ENTRIES_ARE_VIEWS
argument_list|)
DECL|method|testEntryReflectsElementSetClear ()
specifier|public
name|void
name|testEntryReflectsElementSetClear
parameter_list|()
block|{
name|initThreeCopies
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|getMultiset
argument_list|()
operator|.
name|elementSet
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ITERATOR_REMOVE
argument_list|)
annotation|@
name|MultisetFeature
operator|.
name|Require
argument_list|(
name|ENTRIES_ARE_VIEWS
argument_list|)
DECL|method|testEntryReflectsElementSetIteratorRemove ()
specifier|public
name|void
name|testEntryReflectsElementSetIteratorRemove
parameter_list|()
block|{
name|initThreeCopies
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|E
argument_list|>
name|elementItr
init|=
name|getMultiset
argument_list|()
operator|.
name|elementSet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|elementItr
operator|.
name|next
argument_list|()
expr_stmt|;
name|elementItr
operator|.
name|remove
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
block|{
name|SUPPORTS_REMOVE
block|,
name|SUPPORTS_ADD
block|}
argument_list|)
annotation|@
name|MultisetFeature
operator|.
name|Require
argument_list|(
name|ENTRIES_ARE_VIEWS
argument_list|)
DECL|method|testEntryReflectsRemoveThenAdd ()
specifier|public
name|void
name|testEntryReflectsRemoveThenAdd
parameter_list|()
block|{
name|initThreeCopies
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|elementSet
argument_list|()
operator|.
name|remove
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|getMultiset
argument_list|()
operator|.
name|add
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
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
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

