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
name|SUPPORTS_PUT
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
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_comment
comment|/**  * Testers for {@link ListMultimap#put(Object, Object)}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|Ignore
comment|// Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
DECL|class|ListMultimapPutTester
specifier|public
class|class
name|ListMultimapPutTester
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
comment|// MultimapPutTester tests non-duplicate values, but ignores ordering
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
DECL|method|testPutAddsValueAtEnd ()
specifier|public
name|void
name|testPutAddsValueAtEnd
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
for|for
control|(
name|V
name|value
range|:
name|sampleValues
argument_list|()
control|)
block|{
name|resetContainer
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|V
argument_list|>
name|values
init|=
name|multimap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|V
argument_list|>
name|expectedValues
init|=
name|Helpers
operator|.
name|copyToList
argument_list|(
name|values
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
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|expectedValues
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|assertGet
argument_list|(
name|key
argument_list|,
name|expectedValues
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
name|values
operator|.
name|get
argument_list|(
name|values
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
DECL|method|testPutDuplicateValue ()
specifier|public
name|void
name|testPutDuplicateValue
parameter_list|()
block|{
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entries
init|=
name|copyToList
argument_list|(
name|multimap
argument_list|()
operator|.
name|entries
argument_list|()
argument_list|)
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
name|entries
control|)
block|{
name|resetContainer
argument_list|()
expr_stmt|;
name|K
name|k
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|V
name|v
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|V
argument_list|>
name|values
init|=
name|multimap
argument_list|()
operator|.
name|get
argument_list|(
name|k
argument_list|)
decl_stmt|;
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
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|put
argument_list|(
name|k
argument_list|,
name|v
argument_list|)
argument_list|)
expr_stmt|;
name|expectedValues
operator|.
name|add
argument_list|(
name|v
argument_list|)
expr_stmt|;
name|assertGet
argument_list|(
name|k
argument_list|,
name|expectedValues
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|v
argument_list|,
name|values
operator|.
name|get
argument_list|(
name|values
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

