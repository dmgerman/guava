begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Tests for {@code Multimap.asMap().get(Object)}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MultimapAsMapGetTester
specifier|public
class|class
name|MultimapAsMapGetTester
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
DECL|method|testPropagatesRemoveToMultimap ()
specifier|public
name|void
name|testPropagatesRemoveToMultimap
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
argument_list|()
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|()
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
argument_list|()
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|()
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
argument_list|()
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e2
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|result
init|=
name|multimap
argument_list|()
operator|.
name|asMap
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
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|remove
argument_list|(
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|()
argument_list|)
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
argument_list|()
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|()
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
DECL|method|testPropagatesRemoveLastElementToMultimap ()
specifier|public
name|void
name|testPropagatesRemoveLastElementToMultimap
parameter_list|()
block|{
name|Collection
argument_list|<
name|V
argument_list|>
name|result
init|=
name|multimap
argument_list|()
operator|.
name|asMap
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
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|remove
argument_list|(
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|()
argument_list|)
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testPropagatesClearToMultimap ()
specifier|public
name|void
name|testPropagatesClearToMultimap
parameter_list|()
block|{
name|Collection
argument_list|<
name|V
argument_list|>
name|result
init|=
name|multimap
argument_list|()
operator|.
name|asMap
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
decl_stmt|;
name|result
operator|.
name|clear
argument_list|()
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
name|assertThat
argument_list|(
name|result
argument_list|)
operator|.
name|isEmpty
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
block|{
name|SUPPORTS_PUT
block|,
name|ALLOWS_NULL_VALUES
block|}
argument_list|)
DECL|method|testAddNullValue ()
specifier|public
name|void
name|testAddNullValue
parameter_list|()
block|{
name|Collection
argument_list|<
name|V
argument_list|>
name|result
init|=
name|multimap
argument_list|()
operator|.
name|asMap
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
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|add
argument_list|(
literal|null
argument_list|)
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
argument_list|()
argument_list|,
literal|null
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
block|{
name|SUPPORTS_REMOVE
block|,
name|ALLOWS_NULL_VALUE_QUERIES
block|}
argument_list|)
DECL|method|testRemoveNullValue ()
specifier|public
name|void
name|testRemoveNullValue
parameter_list|()
block|{
name|Collection
argument_list|<
name|V
argument_list|>
name|result
init|=
name|multimap
argument_list|()
operator|.
name|asMap
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
decl_stmt|;
name|assertFalse
argument_list|(
name|result
operator|.
name|remove
argument_list|(
literal|null
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
name|value
operator|=
name|SUPPORTS_PUT
argument_list|,
name|absent
operator|=
name|ALLOWS_NULL_VALUES
argument_list|)
DECL|method|testAddNullValueUnsupported ()
specifier|public
name|void
name|testAddNullValueUnsupported
parameter_list|()
block|{
name|Collection
argument_list|<
name|V
argument_list|>
name|result
init|=
name|multimap
argument_list|()
operator|.
name|asMap
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
decl_stmt|;
try|try
block|{
name|result
operator|.
name|add
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
name|SUPPORTS_PUT
argument_list|)
DECL|method|testPropagatesAddToMultimap ()
specifier|public
name|void
name|testPropagatesAddToMultimap
parameter_list|()
block|{
name|Collection
argument_list|<
name|V
argument_list|>
name|result
init|=
name|multimap
argument_list|()
operator|.
name|asMap
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
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
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
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|()
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e3
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
name|SUPPORTS_PUT
block|}
argument_list|)
DECL|method|testPropagatesRemoveThenAddToMultimap ()
specifier|public
name|void
name|testPropagatesRemoveThenAddToMultimap
parameter_list|()
block|{
name|int
name|oldSize
init|=
name|getNumElements
argument_list|()
decl_stmt|;
name|K
name|k0
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
name|Collection
argument_list|<
name|V
argument_list|>
name|result
init|=
name|multimap
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|get
argument_list|(
name|k0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|remove
argument_list|(
name|v0
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|multimap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|k0
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|multimap
argument_list|()
operator|.
name|containsEntry
argument_list|(
name|k0
argument_list|,
name|v0
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|V
name|v1
init|=
name|sampleValues
argument_list|()
operator|.
name|e1
argument_list|()
decl_stmt|;
name|V
name|v2
init|=
name|sampleValues
argument_list|()
operator|.
name|e2
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|add
argument_list|(
name|v1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|add
argument_list|(
name|v2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|v1
argument_list|,
name|v2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|multimap
argument_list|()
operator|.
name|get
argument_list|(
name|k0
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|v1
argument_list|,
name|v2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|k0
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|multimap
argument_list|()
operator|.
name|containsEntry
argument_list|(
name|k0
argument_list|,
name|v0
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|containsEntry
argument_list|(
name|k0
argument_list|,
name|v2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|oldSize
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
DECL|method|testReflectsMultimapRemove ()
specifier|public
name|void
name|testReflectsMultimapRemove
parameter_list|()
block|{
name|Collection
argument_list|<
name|V
argument_list|>
name|result
init|=
name|multimap
argument_list|()
operator|.
name|asMap
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
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

