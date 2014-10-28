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
import|;
end_import

begin_comment
comment|/**  * Tester for {@code Multimap.keySet}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MultimapKeySetTester
specifier|public
class|class
name|MultimapKeySetTester
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
DECL|method|testKeySet ()
specifier|public
name|void
name|testKeySet
parameter_list|()
block|{
for|for
control|(
name|Map
operator|.
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
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|contains
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
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
DECL|method|testKeySetContainsNullKeyPresent ()
specifier|public
name|void
name|testKeySetContainsNullKeyPresent
parameter_list|()
block|{
name|initMultimapWithNullKey
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|contains
argument_list|(
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
name|ALLOWS_NULL_KEY_QUERIES
argument_list|)
DECL|method|testKeySetContainsNullKeyAbsent ()
specifier|public
name|void
name|testKeySetContainsNullKeyAbsent
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|multimap
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|contains
argument_list|(
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
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testKeySetRemovePropagatesToMultimap ()
specifier|public
name|void
name|testKeySetRemovePropagatesToMultimap
parameter_list|()
block|{
name|int
name|key0Count
init|=
name|multimap
argument_list|()
operator|.
name|get
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|()
argument_list|)
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|key0Count
operator|>
literal|0
argument_list|,
name|multimap
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|remove
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getNumElements
argument_list|()
operator|-
name|key0Count
argument_list|,
name|multimap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertGet
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
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
name|SUPPORTS_ITERATOR_REMOVE
argument_list|)
DECL|method|testKeySetIteratorRemove ()
specifier|public
name|void
name|testKeySetIteratorRemove
parameter_list|()
block|{
name|int
name|key0Count
init|=
name|multimap
argument_list|()
operator|.
name|get
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|()
argument_list|)
operator|.
name|size
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|K
argument_list|>
name|keyItr
init|=
name|multimap
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|keyItr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
name|keyItr
operator|.
name|next
argument_list|()
operator|.
name|equals
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|()
argument_list|)
condition|)
block|{
name|keyItr
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
name|assertEquals
argument_list|(
name|getNumElements
argument_list|()
operator|-
name|key0Count
argument_list|,
name|multimap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertGet
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

