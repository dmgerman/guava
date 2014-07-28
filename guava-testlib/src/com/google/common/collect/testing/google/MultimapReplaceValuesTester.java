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
name|ArrayList
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

begin_comment
comment|/**  * Tests for {@link Multimap#replaceValues(Object, Iterable)}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MultimapReplaceValuesTester
specifier|public
class|class
name|MultimapReplaceValuesTester
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
block|{
name|SUPPORTS_PUT
block|,
name|SUPPORTS_REMOVE
block|,
name|ALLOWS_NULL_VALUES
block|}
argument_list|)
DECL|method|testReplaceValuesWithNullValue ()
specifier|public
name|void
name|testReplaceValuesWithNullValue
parameter_list|()
block|{
name|int
name|size
init|=
name|multimap
argument_list|()
operator|.
name|size
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|,
literal|null
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
decl_stmt|;
name|multimap
argument_list|()
operator|.
name|replaceValues
argument_list|(
name|key
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|assertGet
argument_list|(
name|key
argument_list|,
name|values
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
name|SUPPORTS_REMOVE
block|,
name|ALLOWS_NULL_KEYS
block|}
argument_list|)
DECL|method|testReplaceValuesWithNullKey ()
specifier|public
name|void
name|testReplaceValuesWithNullKey
parameter_list|()
block|{
name|int
name|size
init|=
name|multimap
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|K
name|key
init|=
literal|null
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e2
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
decl_stmt|;
name|multimap
argument_list|()
operator|.
name|replaceValues
argument_list|(
name|key
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|assertGet
argument_list|(
name|key
argument_list|,
name|values
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
name|SUPPORTS_REMOVE
block|}
argument_list|)
DECL|method|testReplaceEmptyValues ()
specifier|public
name|void
name|testReplaceEmptyValues
parameter_list|()
block|{
name|int
name|size
init|=
name|multimap
argument_list|()
operator|.
name|size
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e2
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
decl_stmt|;
name|multimap
argument_list|()
operator|.
name|replaceValues
argument_list|(
name|key
argument_list|,
name|values
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
name|values
operator|.
name|size
argument_list|()
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
name|SUPPORTS_REMOVE
block|}
argument_list|)
DECL|method|testReplaceValuesWithEmpty ()
specifier|public
name|void
name|testReplaceValuesWithEmpty
parameter_list|()
block|{
name|int
name|size
init|=
name|multimap
argument_list|()
operator|.
name|size
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
name|List
argument_list|<
name|V
argument_list|>
name|oldValues
init|=
operator|new
name|ArrayList
argument_list|<
name|V
argument_list|>
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|V
argument_list|>
name|values
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|oldValues
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|V
argument_list|>
argument_list|(
name|multimap
argument_list|()
operator|.
name|replaceValues
argument_list|(
name|key
argument_list|,
name|values
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertGet
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|size
operator|-
name|oldValues
operator|.
name|size
argument_list|()
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
name|SUPPORTS_REMOVE
block|}
argument_list|)
DECL|method|testReplaceValuesWithDuplicates ()
specifier|public
name|void
name|testReplaceValuesWithDuplicates
parameter_list|()
block|{
name|int
name|size
init|=
name|multimap
argument_list|()
operator|.
name|size
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
name|List
argument_list|<
name|V
argument_list|>
name|oldValues
init|=
operator|new
name|ArrayList
argument_list|<
name|V
argument_list|>
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
name|e0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|oldValues
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|V
argument_list|>
argument_list|(
name|multimap
argument_list|()
operator|.
name|replaceValues
argument_list|(
name|key
argument_list|,
name|values
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|size
operator|-
name|oldValues
operator|.
name|size
argument_list|()
operator|+
name|multimap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|size
argument_list|()
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
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|containsAll
argument_list|(
name|values
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
name|SUPPORTS_PUT
block|,
name|SUPPORTS_REMOVE
block|}
argument_list|)
DECL|method|testReplaceNonEmptyValues ()
specifier|public
name|void
name|testReplaceNonEmptyValues
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e2
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
decl_stmt|;
for|for
control|(
name|K
name|k
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
name|multimap
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|oldKeyValues
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
name|k
argument_list|)
argument_list|)
decl_stmt|;
name|multimap
argument_list|()
operator|.
name|replaceValues
argument_list|(
name|k
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|assertGet
argument_list|(
name|k
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|size
operator|+
name|values
operator|.
name|size
argument_list|()
operator|-
name|oldKeyValues
operator|.
name|size
argument_list|()
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
block|{
name|SUPPORTS_PUT
block|,
name|SUPPORTS_REMOVE
block|}
argument_list|)
DECL|method|testReplaceValuesPropagatesToGet ()
specifier|public
name|void
name|testReplaceValuesPropagatesToGet
parameter_list|()
block|{
name|K
name|key
init|=
name|sampleKeys
argument_list|()
operator|.
name|e0
decl_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|getCollection
init|=
name|multimap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|sampleValues
argument_list|()
operator|.
name|e0
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e2
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
decl_stmt|;
name|multimap
argument_list|()
operator|.
name|replaceValues
argument_list|(
name|key
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getCollection
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
name|e2
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_REMOVE
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
DECL|method|testReplaceValuesRemoveNotSupported ()
specifier|public
name|void
name|testReplaceValuesRemoveNotSupported
parameter_list|()
block|{
name|List
argument_list|<
name|V
argument_list|>
name|values
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
decl_stmt|;
try|try
block|{
name|multimap
argument_list|()
operator|.
name|replaceValues
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected UnsupportedOperationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{
comment|// success
block|}
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_PUT
argument_list|)
DECL|method|testReplaceValuesPutNotSupported ()
specifier|public
name|void
name|testReplaceValuesPutNotSupported
parameter_list|()
block|{
name|List
argument_list|<
name|V
argument_list|>
name|values
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|sampleValues
argument_list|()
operator|.
name|e3
argument_list|)
decl_stmt|;
try|try
block|{
name|multimap
argument_list|()
operator|.
name|replaceValues
argument_list|(
name|sampleKeys
argument_list|()
operator|.
name|e0
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected UnsupportedOperationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{
comment|// success
block|}
block|}
block|}
end_class

end_unit

