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
name|SUPPORTS_PUT
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
name|ArrayList
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
comment|/**  * Tests for {@link Multimap#asMap}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MultimapAsMapTester
specifier|public
class|class
name|MultimapAsMapTester
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
DECL|method|testAsMapGet ()
specifier|public
name|void
name|testAsMapGet
parameter_list|()
block|{
for|for
control|(
name|K
name|key
range|:
name|sampleKeys
argument_list|()
control|)
block|{
name|List
argument_list|<
name|V
argument_list|>
name|expectedValues
init|=
operator|new
name|ArrayList
argument_list|<
name|V
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
range|:
name|getSampleElements
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|expectedValues
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Collection
argument_list|<
name|V
argument_list|>
name|collection
init|=
name|multimap
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|expectedValues
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|assertThat
argument_list|(
name|collection
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|assertThat
argument_list|(
name|collection
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|expectedValues
argument_list|)
expr_stmt|;
block|}
block|}
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
DECL|method|testAsMapGetNullKeyPresent ()
specifier|public
name|void
name|testAsMapGetNullKeyPresent
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
name|asMap
argument_list|()
operator|.
name|get
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|getValueForNullKey
argument_list|()
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
DECL|method|testAsMapGetNullKeyAbsent ()
specifier|public
name|void
name|testAsMapGetNullKeyAbsent
parameter_list|()
block|{
name|assertThat
argument_list|(
name|multimap
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|get
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ALLOWS_NULL_KEY_QUERIES
argument_list|)
DECL|method|testAsMapGetNullKeyUnsupported ()
specifier|public
name|void
name|testAsMapGetNullKeyUnsupported
parameter_list|()
block|{
try|try
block|{
name|multimap
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|get
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected NullPointerException"
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
DECL|method|testAsMapRemove ()
specifier|public
name|void
name|testAsMapRemove
parameter_list|()
block|{
name|assertThat
argument_list|(
name|multimap
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|remove
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|)
argument_list|)
operator|.
name|iteratesAs
argument_list|(
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|)
expr_stmt|;
name|assertGet
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
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
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
DECL|method|testAsMapEntrySetReflectsPutSameKey ()
specifier|public
name|void
name|testAsMapEntrySetReflectsPutSameKey
parameter_list|()
block|{
name|resetContainer
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
name|e3
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
name|asMapEntrySet
init|=
name|multimap
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|valueCollection
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|asMapEntrySet
argument_list|)
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|valueCollection
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|put
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e4
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|valueCollection
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e4
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
DECL|method|testAsMapEntrySetReflectsPutDifferentKey ()
specifier|public
name|void
name|testAsMapEntrySetReflectsPutDifferentKey
parameter_list|()
block|{
name|resetContainer
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
name|e3
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
name|asMapEntrySet
init|=
name|multimap
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|put
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e1
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e4
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|asMapEntrySet
operator|.
name|size
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
name|MapFeature
operator|.
name|Require
argument_list|(
block|{
name|SUPPORTS_PUT
block|,
name|SUPPORTS_REMOVE
block|}
argument_list|)
DECL|method|testAsMapEntrySetRemovePropagatesToMultimap ()
specifier|public
name|void
name|testAsMapEntrySetRemovePropagatesToMultimap
parameter_list|()
block|{
name|resetContainer
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
name|e3
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
name|asMapEntrySet
init|=
name|multimap
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|asMapEntry0
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|asMapEntrySet
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|put
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e1
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e4
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|asMapEntrySet
operator|.
name|remove
argument_list|(
name|asMapEntry0
argument_list|)
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
name|assertThat
argument_list|(
name|multimap
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|iteratesAs
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e1
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
DECL|method|testAsMapEntrySetIteratorRemovePropagatesToMultimap ()
specifier|public
name|void
name|testAsMapEntrySetIteratorRemovePropagatesToMultimap
parameter_list|()
block|{
name|resetContainer
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
name|e3
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
name|asMapEntrySet
init|=
name|multimap
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
name|asMapEntryItr
init|=
name|asMapEntrySet
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|asMapEntryItr
operator|.
name|next
argument_list|()
expr_stmt|;
name|asMapEntryItr
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
block|}
end_class

end_unit

