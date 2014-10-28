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
name|List
import|;
end_import

begin_comment
comment|/**  * Testers for {@link ListMultimap#putAll(Object, Iterable)}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ListMultimapPutAllTester
specifier|public
class|class
name|ListMultimapPutAllTester
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
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_PUT
argument_list|)
DECL|method|testPutAllAddsAtEndInOrder ()
specifier|public
name|void
name|testPutAllAddsAtEndInOrder
parameter_list|()
block|{
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
name|e3
argument_list|()
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e1
argument_list|()
argument_list|,
name|sampleValues
argument_list|()
operator|.
name|e4
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|K
name|k
range|:
name|sampleKeys
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
name|expectedValues
init|=
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
name|assertTrue
argument_list|(
name|multimap
argument_list|()
operator|.
name|putAll
argument_list|(
name|k
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
name|expectedValues
operator|.
name|addAll
argument_list|(
name|values
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

