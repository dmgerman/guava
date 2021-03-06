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
name|SortedSetMultimap
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
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
comment|/**  * Testers for {@link SortedSetMultimap#asMap}.  *  * @author Louis Wasserman  * @param<K> The key type of the tested multimap.  * @param<V> The value type of the tested multimap.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|Ignore
comment|// Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
DECL|class|SortedSetMultimapAsMapTester
specifier|public
class|class
name|SortedSetMultimapAsMapTester
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
name|SortedSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
block|{
DECL|method|testAsMapValuesImplementSortedSet ()
specifier|public
name|void
name|testAsMapValuesImplementSortedSet
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
name|SortedSet
argument_list|<
name|V
argument_list|>
name|valueSet
init|=
operator|(
name|SortedSet
argument_list|<
name|V
argument_list|>
operator|)
name|valueCollection
decl_stmt|;
name|assertEquals
argument_list|(
name|multimap
argument_list|()
operator|.
name|valueComparator
argument_list|()
argument_list|,
name|valueSet
operator|.
name|comparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testAsMapGetImplementsSortedSet ()
specifier|public
name|void
name|testAsMapGetImplementsSortedSet
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
name|SortedSet
argument_list|<
name|V
argument_list|>
name|valueSet
init|=
operator|(
name|SortedSet
argument_list|<
name|V
argument_list|>
operator|)
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
name|assertEquals
argument_list|(
name|multimap
argument_list|()
operator|.
name|valueComparator
argument_list|()
argument_list|,
name|valueSet
operator|.
name|comparator
argument_list|()
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
DECL|method|testAsMapRemoveImplementsSortedSet ()
specifier|public
name|void
name|testAsMapRemoveImplementsSortedSet
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
name|SortedSet
argument_list|<
name|V
argument_list|>
name|valueSet
init|=
operator|(
name|SortedSet
argument_list|<
name|V
argument_list|>
operator|)
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
decl_stmt|;
name|assertEquals
argument_list|(
name|multimap
argument_list|()
operator|.
name|valueComparator
argument_list|()
argument_list|,
name|valueSet
operator|.
name|comparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

