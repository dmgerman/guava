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
name|ALLOWS_NULL_QUERIES
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

begin_comment
comment|/**  * Tests for {@link Multimap#removeAll(Object)}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MultimapRemoveAllTester
specifier|public
class|class
name|MultimapRemoveAllTester
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
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRemoveAllAbsentKey ()
specifier|public
name|void
name|testRemoveAllAbsentKey
parameter_list|()
block|{
name|ASSERT
operator|.
name|that
argument_list|(
name|multimap
argument_list|()
operator|.
name|removeAll
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e3
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|expectUnchanged
argument_list|()
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
DECL|method|testRemoveAllPresentKey ()
specifier|public
name|void
name|testRemoveAllPresentKey
parameter_list|()
block|{
name|ASSERT
operator|.
name|that
argument_list|(
name|multimap
argument_list|()
operator|.
name|removeAll
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|allOf
argument_list|(
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|expectMissing
argument_list|(
name|samples
operator|.
name|e0
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
DECL|method|testRemoveAllPropagatesToGet ()
specifier|public
name|void
name|testRemoveAllPropagatesToGet
parameter_list|()
block|{
name|Collection
argument_list|<
name|V
argument_list|>
name|getResult
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
argument_list|)
decl_stmt|;
name|multimap
argument_list|()
operator|.
name|removeAll
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|getResult
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|expectMissing
argument_list|(
name|samples
operator|.
name|e0
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
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRemoveAllMultipleValues ()
specifier|public
name|void
name|testRemoveAllMultipleValues
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
name|e1
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
name|e2
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|multimap
argument_list|()
operator|.
name|removeAll
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|allOf
argument_list|(
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e1
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e2
argument_list|)
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
block|{
name|SUPPORTS_REMOVE
block|,
name|ALLOWS_NULL_KEYS
block|}
argument_list|)
DECL|method|testRemoveAllNullKeyPresent ()
specifier|public
name|void
name|testRemoveAllNullKeyPresent
parameter_list|()
block|{
name|initMultimapWithNullKey
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|multimap
argument_list|()
operator|.
name|removeAll
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|allOf
argument_list|(
name|getValueForNullKey
argument_list|()
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|expectMissing
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
block|{
name|SUPPORTS_REMOVE
block|,
name|ALLOWS_NULL_QUERIES
block|}
argument_list|)
DECL|method|testRemoveAllNullKeyAbsent ()
specifier|public
name|void
name|testRemoveAllNullKeyAbsent
parameter_list|()
block|{
name|ASSERT
operator|.
name|that
argument_list|(
name|multimap
argument_list|()
operator|.
name|removeAll
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

