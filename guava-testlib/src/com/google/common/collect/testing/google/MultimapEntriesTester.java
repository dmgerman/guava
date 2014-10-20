begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|MapFeature
operator|.
name|ALLOWS_NULL_KEYS
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
name|MapFeature
operator|.
name|ALLOWS_NULL_KEY_QUERIES
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
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUES
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
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUE_QUERIES
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
name|MapFeature
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
name|collect
operator|.
name|Multimap
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
name|MapFeature
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

begin_comment
comment|/**  * Tester for {@code Multimap.entries}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MultimapEntriesTester
specifier|public
class|class
name|MultimapEntriesTester
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractMultimapTester
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
block|{
DECL|method|testEntries ()
specifier|public
name|void
name|testEntries
parameter_list|()
block|{
name|assertThat
argument_list|(
name|multimap
argument_list|()
operator|.
name|entries
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactlyAs
argument_list|(
name|getSampleElements
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_KEYS
argument_list|)
DECL|method|testContainsEntryWithNullKeyPresent ()
specifier|public
name|void
name|testContainsEntryWithNullKeyPresent
parameter_list|()
block|{
name|initMultimapWithNullKey
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|multimap
argument_list|()
operator|.
name|entries
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
operator|(
name|K
operator|)
literal|null
argument_list|,
name|getValueForNullKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_KEY_QUERIES
argument_list|)
DECL|method|testContainsEntryWithNullKeyAbsent ()
specifier|public
name|void
name|testContainsEntryWithNullKeyAbsent
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|multimap
argument_list|()
operator|.
name|entries
argument_list|()
operator|.
name|contains
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
literal|null
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|)
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_VALUES
argument_list|)
DECL|method|testContainsEntryWithNullValuePresent ()
specifier|public
name|void
name|testContainsEntryWithNullValuePresent
parameter_list|()
block|{
name|initMultimapWithNullValue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|multimap
argument_list|()
operator|.
name|entries
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|getKeyForNullValue
argument_list|()
argument_list|,
operator|(
name|V
operator|)
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_VALUE_QUERIES
argument_list|)
DECL|method|testContainsEntryWithNullValueAbsent ()
specifier|public
name|void
name|testContainsEntryWithNullValueAbsent
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|multimap
argument_list|()
operator|.
name|entries
argument_list|()
operator|.
name|contains
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|,
literal|null
argument_list|)
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRemovePropagatesToMultimap ()
specifier|public
name|void
name|testRemovePropagatesToMultimap
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|entries
argument_list|()
operator|.
name|remove
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|expectMissing
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getNumElements
argument_list|()
operator|-
literal|1
argument_list|,
name|multimap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|multimap
argument_list|()
operator|.
name|containsEntry
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|,
name|sampleValues
argument_list|()
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRemoveAllPropagatesToMultimap ()
specifier|public
name|void
name|testRemoveAllPropagatesToMultimap
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|entries
argument_list|()
operator|.
name|removeAll
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|expectMissing
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getNumElements
argument_list|()
operator|-
literal|1
argument_list|,
name|multimap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|multimap
argument_list|()
operator|.
name|containsEntry
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|,
name|sampleValues
argument_list|()
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRetainAllPropagatesToMultimap ()
specifier|public
name|void
name|testRetainAllPropagatesToMultimap
parameter_list|()
block|{
name|multimap
argument_list|()
operator|.
name|entries
argument_list|()
operator|.
name|retainAll
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|)
argument_list|)
argument_list|,
name|multimap
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|multimap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|containsEntry
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|,
name|sampleValues
argument_list|()
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
name|SUPPORTS_ITERATOR_REMOVE
argument_list|)
DECL|method|testIteratorRemovePropagatesToMultimap ()
specifier|public
name|void
name|testIteratorRemovePropagatesToMultimap
parameter_list|()
block|{
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|iterator
init|=
name|multimap
argument_list|()
operator|.
name|entries
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e0
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
name|assertTrue
argument_list|(
name|multimap
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testEntriesRemainValidAfterRemove ()
specifier|public
name|void
name|testEntriesRemainValidAfterRemove
parameter_list|()
block|{
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|iterator
init|=
name|multimap
argument_list|()
operator|.
name|entries
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|K
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|V
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|multimap
argument_list|()
operator|.
name|removeAll
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|key
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

