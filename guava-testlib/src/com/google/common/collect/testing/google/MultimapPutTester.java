begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|SUPPORTS_PUT
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
name|collect
operator|.
name|ImmutableList
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

begin_comment
comment|/**  * Tester for {@link Multimap#put}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MultimapPutTester
specifier|public
class|class
name|MultimapPutTester
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
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
DECL|method|testPutEmpty ()
specifier|public
name|void
name|testPutEmpty
parameter_list|()
block|{
name|int
name|size
init|=
name|getNumElements
argument_list|()
decl_stmt|;
name|K
name|key
init|=
name|sampleKeys
argument_list|()
operator|.
name|e3
decl_stmt|;
name|V
name|value
init|=
name|sampleValues
argument_list|()
operator|.
name|e3
decl_stmt|;
name|assertGet
argument_list|(
name|key
argument_list|,
name|ImmutableList
operator|.
expr|<
name|V
operator|>
name|of
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|assertGet
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|size
operator|+
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testPutPresent ()
specifier|public
name|void
name|testPutPresent
parameter_list|()
block|{
name|int
name|size
init|=
name|getNumElements
argument_list|()
decl_stmt|;
name|K
name|key
init|=
name|sampleKeys
argument_list|()
operator|.
name|e0
decl_stmt|;
name|V
name|oldValue
init|=
name|sampleValues
argument_list|()
operator|.
name|e0
decl_stmt|;
name|V
name|newValue
init|=
name|sampleValues
argument_list|()
operator|.
name|e3
decl_stmt|;
name|assertGet
argument_list|(
name|key
argument_list|,
name|oldValue
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|newValue
argument_list|)
argument_list|)
expr_stmt|;
name|assertGet
argument_list|(
name|key
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|size
operator|+
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
DECL|method|testPutTwoElements ()
specifier|public
name|void
name|testPutTwoElements
parameter_list|()
block|{
name|int
name|size
init|=
name|getNumElements
argument_list|()
decl_stmt|;
name|K
name|key
init|=
name|sampleKeys
argument_list|()
operator|.
name|e0
decl_stmt|;
name|V
name|v1
init|=
name|sampleValues
argument_list|()
operator|.
name|e3
decl_stmt|;
name|V
name|v2
init|=
name|sampleValues
argument_list|()
operator|.
name|e4
decl_stmt|;
name|List
argument_list|<
name|V
argument_list|>
name|values
init|=
name|Helpers
operator|.
name|copyToList
argument_list|(
name|multimap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|v1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|v2
argument_list|)
argument_list|)
expr_stmt|;
name|values
operator|.
name|add
argument_list|(
name|v1
argument_list|)
expr_stmt|;
name|values
operator|.
name|add
argument_list|(
name|v2
argument_list|)
expr_stmt|;
name|assertGet
argument_list|(
name|key
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|size
operator|+
literal|2
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
name|MapFeature
operator|.
name|Require
argument_list|(
block|{
name|SUPPORTS_PUT
block|,
name|ALLOWS_NULL_VALUES
block|}
argument_list|)
DECL|method|testPutNullValue ()
specifier|public
name|void
name|testPutNullValue
parameter_list|()
block|{
name|int
name|size
init|=
name|getNumElements
argument_list|()
decl_stmt|;
name|multimap
argument_list|()
operator|.
name|put
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e3
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertGet
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e3
argument_list|,
name|Lists
operator|.
name|newArrayList
argument_list|(
operator|(
name|V
operator|)
literal|null
argument_list|)
argument_list|)
expr_stmt|;
comment|// ImmutableList.of can't take null.
name|assertEquals
argument_list|(
name|size
operator|+
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
name|MapFeature
operator|.
name|Require
argument_list|(
block|{
name|SUPPORTS_PUT
block|,
name|ALLOWS_NULL_KEYS
block|}
argument_list|)
DECL|method|testPutNullKey ()
specifier|public
name|void
name|testPutNullKey
parameter_list|()
block|{
name|int
name|size
init|=
name|getNumElements
argument_list|()
decl_stmt|;
name|multimap
argument_list|()
operator|.
name|put
argument_list|(
literal|null
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
expr_stmt|;
name|assertGet
argument_list|(
literal|null
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|size
operator|+
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
DECL|method|testPutNotPresentKeyPropagatesToGet ()
specifier|public
name|void
name|testPutNotPresentKeyPropagatesToGet
parameter_list|()
block|{
name|int
name|size
init|=
name|getNumElements
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|collection
init|=
name|multimap
argument_list|()
operator|.
name|get
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e3
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|collection
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|multimap
argument_list|()
operator|.
name|put
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e3
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|collection
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|item
argument_list|(
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|size
operator|+
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testPutPresentKeyPropagatesToGet ()
specifier|public
name|void
name|testPutPresentKeyPropagatesToGet
parameter_list|()
block|{
name|List
argument_list|<
name|K
argument_list|>
name|keys
init|=
name|Helpers
operator|.
name|copyToList
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
name|resetContainer
argument_list|()
expr_stmt|;
name|int
name|size
init|=
name|getNumElements
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|collection
init|=
name|multimap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|expectedCollection
init|=
name|Helpers
operator|.
name|copyToList
argument_list|(
name|collection
argument_list|)
decl_stmt|;
name|multimap
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
expr_stmt|;
name|expectedCollection
operator|.
name|add
argument_list|(
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|collection
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|allFrom
argument_list|(
name|expectedCollection
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|size
operator|+
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
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testPutPresentKeyPropagatesToAsMapGet ()
specifier|public
name|void
name|testPutPresentKeyPropagatesToAsMapGet
parameter_list|()
block|{
name|List
argument_list|<
name|K
argument_list|>
name|keys
init|=
name|Helpers
operator|.
name|copyToList
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
name|resetContainer
argument_list|()
expr_stmt|;
name|int
name|size
init|=
name|getNumElements
argument_list|()
decl_stmt|;
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
name|assertNotNull
argument_list|(
name|collection
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|expectedCollection
init|=
name|Helpers
operator|.
name|copyToList
argument_list|(
name|collection
argument_list|)
decl_stmt|;
name|multimap
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
expr_stmt|;
name|expectedCollection
operator|.
name|add
argument_list|(
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|collection
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|allFrom
argument_list|(
name|expectedCollection
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|size
operator|+
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
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testPutPresentKeyPropagatesToAsMapEntrySet ()
specifier|public
name|void
name|testPutPresentKeyPropagatesToAsMapEntrySet
parameter_list|()
block|{
name|List
argument_list|<
name|K
argument_list|>
name|keys
init|=
name|Helpers
operator|.
name|copyToList
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
name|resetContainer
argument_list|()
expr_stmt|;
name|int
name|size
init|=
name|getNumElements
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
name|asMapItr
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
decl_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|collection
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|asMapItr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
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
name|asMapItr
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|asMapEntry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|collection
operator|=
name|asMapEntry
operator|.
name|getValue
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
name|assertNotNull
argument_list|(
name|collection
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|expectedCollection
init|=
name|Helpers
operator|.
name|copyToList
argument_list|(
name|collection
argument_list|)
decl_stmt|;
name|multimap
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
expr_stmt|;
name|expectedCollection
operator|.
name|add
argument_list|(
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|collection
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|allFrom
argument_list|(
name|expectedCollection
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|size
operator|+
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
block|}
block|}
end_class

end_unit

