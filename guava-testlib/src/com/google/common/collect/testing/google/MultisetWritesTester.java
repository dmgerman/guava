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
name|SUPPORTS_CLEAR
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
name|CollectionFeature
operator|.
name|SUPPORTS_REMOVE_ALL
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
name|SUPPORTS_RETAIN_ALL
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
name|ZERO
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
name|WrongType
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

begin_comment
comment|/**  * A generic JUnit test which tests multiset-specific write operations.  * Can't be invoked directly; please see {@link MultisetTestSuiteBuilder}.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MultisetWritesTester
specifier|public
class|class
name|MultisetWritesTester
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
name|SUPPORTS_ADD
argument_list|)
DECL|method|testAddOccurrences ()
specifier|public
name|void
name|testAddOccurrences
parameter_list|()
block|{
name|int
name|oldCount
init|=
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"multiset.add(E, int) should return the old count"
argument_list|,
name|oldCount
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|add
argument_list|(
name|samples
operator|.
name|e0
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"multiset.count() incorrect after add(E, int)"
argument_list|,
name|oldCount
operator|+
literal|2
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_ADD
argument_list|)
DECL|method|testAddOccurrences_unsupported ()
specifier|public
name|void
name|testAddOccurrences_unsupported
parameter_list|()
block|{
try|try
block|{
name|getMultiset
argument_list|()
operator|.
name|add
argument_list|(
name|samples
operator|.
name|e0
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"unsupported multiset.add(E, int) didn't throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|required
parameter_list|)
block|{}
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
DECL|method|testRemove_occurrences_present ()
specifier|public
name|void
name|testRemove_occurrences_present
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"multiset.remove(present, 2) didn't return the old count"
argument_list|,
literal|1
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"multiset contains present after multiset.remove(present, 2)"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|contains
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRemove_occurrences_absent ()
specifier|public
name|void
name|testRemove_occurrences_absent
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"multiset.remove(absent, 0) didn't return 0"
argument_list|,
literal|0
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e3
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRemove_occurrences_unsupported_absent ()
specifier|public
name|void
name|testRemove_occurrences_unsupported_absent
parameter_list|()
block|{
comment|// notice: we don't care whether it succeeds, or fails with UOE
try|try
block|{
name|assertEquals
argument_list|(
literal|"multiset.remove(absent, 2) didn't return 0 or throw an exception"
argument_list|,
literal|0
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e3
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|ok
parameter_list|)
block|{}
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRemove_occurrences_0 ()
specifier|public
name|void
name|testRemove_occurrences_0
parameter_list|()
block|{
name|int
name|oldCount
init|=
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"multiset.remove(E, 0) didn't return the old count"
argument_list|,
name|oldCount
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRemove_occurrences_negative ()
specifier|public
name|void
name|testRemove_occurrences_negative
parameter_list|()
block|{
try|try
block|{
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"multiset.remove(E, -1) didn't throw an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|required
parameter_list|)
block|{}
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRemove_occurrences_wrongType ()
specifier|public
name|void
name|testRemove_occurrences_wrongType
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"multiset.remove(wrongType, 1) didn't return 0"
argument_list|,
literal|0
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
name|WrongType
operator|.
name|VALUE
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_CLEAR
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
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testEntrySet_iterator ()
specifier|public
name|void
name|testEntrySet_iterator
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
name|samples
operator|.
name|e0
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
literal|"size 1 multiset.entrySet() iterator.hasNext() returned true "
operator|+
literal|"after next()"
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
name|absent
operator|=
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testEntrySet_iterator_remove_unsupported ()
specifier|public
name|void
name|testEntrySet_iterator_remove_unsupported
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
try|try
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"multiset.entrySet() iterator.remove() didn't throw an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{}
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
DECL|method|testEntrySet_remove_present ()
specifier|public
name|void
name|testEntrySet_remove_present
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
name|samples
operator|.
name|e0
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
name|samples
operator|.
name|e0
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
DECL|method|testEntrySet_remove_missing ()
specifier|public
name|void
name|testEntrySet_remove_missing
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
name|samples
operator|.
name|e0
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
name|samples
operator|.
name|e0
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
name|SUPPORTS_REMOVE_ALL
argument_list|)
DECL|method|testEntrySet_removeAll_present ()
specifier|public
name|void
name|testEntrySet_removeAll_present
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
name|samples
operator|.
name|e0
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
name|samples
operator|.
name|e0
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
name|SUPPORTS_REMOVE_ALL
argument_list|)
DECL|method|testEntrySet_removeAll_missing ()
specifier|public
name|void
name|testEntrySet_removeAll_missing
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
name|samples
operator|.
name|e0
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
name|samples
operator|.
name|e0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE_ALL
argument_list|)
DECL|method|testEntrySet_removeAll_null ()
specifier|public
name|void
name|testEntrySet_removeAll_null
parameter_list|()
block|{
try|try
block|{
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|removeAll
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"multiset.entrySet.removeAll(null) didn't throw an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{}
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
name|SUPPORTS_RETAIN_ALL
argument_list|)
DECL|method|testEntrySet_retainAll_present ()
specifier|public
name|void
name|testEntrySet_retainAll_present
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
name|samples
operator|.
name|e0
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
name|samples
operator|.
name|e0
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
name|SUPPORTS_RETAIN_ALL
argument_list|)
DECL|method|testEntrySet_retainAll_missing ()
specifier|public
name|void
name|testEntrySet_retainAll_missing
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
name|samples
operator|.
name|e0
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
name|samples
operator|.
name|e0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_RETAIN_ALL
argument_list|)
DECL|method|testEntrySet_retainAll_null ()
specifier|public
name|void
name|testEntrySet_retainAll_null
parameter_list|()
block|{
try|try
block|{
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|retainAll
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// Returning successfully is not ideal, but tolerated.
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{}
block|}
block|}
end_class

end_unit

