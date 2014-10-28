begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|Helpers
operator|.
name|copyToList
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
name|Helpers
operator|.
name|mapEntry
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
name|ListMultimap
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

begin_comment
comment|/**  * Testers for {@link ListMultimap#remove(Object, Object)}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ListMultimapRemoveTester
specifier|public
class|class
name|ListMultimapRemoveTester
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testMultimapRemoveDeletesFirstOccurrence ()
specifier|public
name|void
name|testMultimapRemoveDeletesFirstOccurrence
parameter_list|()
block|{
name|K
name|k
init|=
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|()
decl_stmt|;
name|V
name|v0
init|=
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|()
decl_stmt|;
name|V
name|v1
init|=
name|sampleValues
argument_list|()
operator|.
name|e2
argument_list|()
decl_stmt|;
name|resetContainer
argument_list|(
name|mapEntry
argument_list|(
name|k
argument_list|,
name|v0
argument_list|)
argument_list|,
name|mapEntry
argument_list|(
name|k
argument_list|,
name|v1
argument_list|)
argument_list|,
name|mapEntry
argument_list|(
name|k
argument_list|,
name|v0
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|V
argument_list|>
name|list
init|=
name|multimap
argument_list|()
operator|.
name|get
argument_list|(
name|k
argument_list|)
decl_stmt|;
name|multimap
argument_list|()
operator|.
name|remove
argument_list|(
name|k
argument_list|,
name|v0
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|list
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|v1
argument_list|,
name|v0
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testRemoveAtIndexFromGetPropagates ()
specifier|public
name|void
name|testRemoveAtIndexFromGetPropagates
parameter_list|()
block|{
name|K
name|k
init|=
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|()
decl_stmt|;
name|V
name|v0
init|=
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|()
decl_stmt|;
name|V
name|v1
init|=
name|sampleValues
argument_list|()
operator|.
name|e2
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|V
argument_list|>
name|values
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|v0
argument_list|,
name|v1
argument_list|,
name|v0
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|resetContainer
argument_list|(
name|mapEntry
argument_list|(
name|k
argument_list|,
name|v0
argument_list|)
argument_list|,
name|mapEntry
argument_list|(
name|k
argument_list|,
name|v1
argument_list|)
argument_list|,
name|mapEntry
argument_list|(
name|k
argument_list|,
name|v0
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|V
argument_list|>
name|expectedValues
init|=
name|copyToList
argument_list|(
name|values
argument_list|)
decl_stmt|;
name|multimap
argument_list|()
operator|.
name|get
argument_list|(
name|k
argument_list|)
operator|.
name|remove
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|expectedValues
operator|.
name|remove
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|assertGet
argument_list|(
name|k
argument_list|,
name|expectedValues
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testRemoveAtIndexFromAsMapPropagates ()
specifier|public
name|void
name|testRemoveAtIndexFromAsMapPropagates
parameter_list|()
block|{
name|K
name|k
init|=
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|()
decl_stmt|;
name|V
name|v0
init|=
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|()
decl_stmt|;
name|V
name|v1
init|=
name|sampleValues
argument_list|()
operator|.
name|e2
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|V
argument_list|>
name|values
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|v0
argument_list|,
name|v1
argument_list|,
name|v0
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|resetContainer
argument_list|(
name|mapEntry
argument_list|(
name|k
argument_list|,
name|v0
argument_list|)
argument_list|,
name|mapEntry
argument_list|(
name|k
argument_list|,
name|v1
argument_list|)
argument_list|,
name|mapEntry
argument_list|(
name|k
argument_list|,
name|v0
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|V
argument_list|>
name|expectedValues
init|=
name|copyToList
argument_list|(
name|values
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|V
argument_list|>
name|asMapValue
init|=
operator|(
name|List
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
name|k
argument_list|)
decl_stmt|;
name|asMapValue
operator|.
name|remove
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|expectedValues
operator|.
name|remove
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|assertGet
argument_list|(
name|k
argument_list|,
name|expectedValues
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testRemoveAtIndexFromAsMapEntrySetPropagates ()
specifier|public
name|void
name|testRemoveAtIndexFromAsMapEntrySetPropagates
parameter_list|()
block|{
name|K
name|k
init|=
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|()
decl_stmt|;
name|V
name|v0
init|=
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|()
decl_stmt|;
name|V
name|v1
init|=
name|sampleValues
argument_list|()
operator|.
name|e2
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|V
argument_list|>
name|values
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|v0
argument_list|,
name|v1
argument_list|,
name|v0
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|resetContainer
argument_list|(
name|mapEntry
argument_list|(
name|k
argument_list|,
name|v0
argument_list|)
argument_list|,
name|mapEntry
argument_list|(
name|k
argument_list|,
name|v1
argument_list|)
argument_list|,
name|mapEntry
argument_list|(
name|k
argument_list|,
name|v0
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|V
argument_list|>
name|expectedValues
init|=
name|copyToList
argument_list|(
name|values
argument_list|)
decl_stmt|;
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|asMapEntry
init|=
name|multimap
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|V
argument_list|>
name|asMapValue
init|=
operator|(
name|List
argument_list|<
name|V
argument_list|>
operator|)
name|asMapEntry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|asMapValue
operator|.
name|remove
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|expectedValues
operator|.
name|remove
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|assertGet
argument_list|(
name|k
argument_list|,
name|expectedValues
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

