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
name|MapFeature
operator|.
name|SUPPORTS_REMOVE
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
name|Lists
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
name|Maps
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
name|Sets
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
comment|/**  * Testers for {@link com.google.common.collect.ListMultimap#asMap}.  *  * @author Louis Wasserman  * @param<K> The key type of the tested multimap.  * @param<V> The value type of the tested multimap.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ListMultimapAsMapTester
specifier|public
class|class
name|ListMultimapAsMapTester
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractListMultimapTester
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|testAsMapValuesImplementList ()
specifier|public
name|void
name|testAsMapValuesImplementList
parameter_list|()
block|{
for|for
control|(
name|Collection
argument_list|<
name|V
argument_list|>
name|valueCollection
range|:
name|multimap
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
name|assertTrue
argument_list|(
name|valueCollection
operator|instanceof
name|List
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testAsMapGetImplementsList ()
specifier|public
name|void
name|testAsMapGetImplementsList
parameter_list|()
block|{
for|for
control|(
name|K
name|key
range|:
name|multimap
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|assertTrue
argument_list|(
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
operator|instanceof
name|List
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testAsMapRemoveImplementsList ()
specifier|public
name|void
name|testAsMapRemoveImplementsList
parameter_list|()
block|{
name|List
argument_list|<
name|K
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|multimap
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|K
name|key
range|:
name|keys
control|)
block|{
name|resetCollection
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|remove
argument_list|(
name|key
argument_list|)
operator|instanceof
name|List
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
name|resetContainer
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k1
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|expected
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|expected
operator|.
name|put
argument_list|(
name|k0
argument_list|()
argument_list|,
name|Lists
operator|.
name|newArrayList
argument_list|(
name|v0
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|expected
operator|.
name|put
argument_list|(
name|k1
argument_list|()
argument_list|,
name|Lists
operator|.
name|newArrayList
argument_list|(
name|v0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|expected
argument_list|,
name|multimap
argument_list|()
operator|.
name|asMap
argument_list|()
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testEntrySetEquals ()
specifier|public
name|void
name|testEntrySetEquals
parameter_list|()
block|{
name|resetContainer
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k1
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v3
argument_list|()
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
name|expected
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k0
argument_list|()
argument_list|,
operator|(
name|Collection
argument_list|<
name|V
argument_list|>
operator|)
name|Lists
operator|.
name|newArrayList
argument_list|(
name|v0
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k1
argument_list|()
argument_list|,
operator|(
name|Collection
argument_list|<
name|V
argument_list|>
operator|)
name|Lists
operator|.
name|newArrayList
argument_list|(
name|v0
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|expected
argument_list|,
name|multimap
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
operator|.
name|testEquals
argument_list|()
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
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testValuesRemove ()
specifier|public
name|void
name|testValuesRemove
parameter_list|()
block|{
name|resetContainer
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k1
argument_list|()
argument_list|,
name|v0
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|k0
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|remove
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|v0
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|multimap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
name|k0
argument_list|()
argument_list|,
name|Lists
operator|.
name|newArrayList
argument_list|(
name|v0
argument_list|()
argument_list|,
name|v3
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|multimap
argument_list|()
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

