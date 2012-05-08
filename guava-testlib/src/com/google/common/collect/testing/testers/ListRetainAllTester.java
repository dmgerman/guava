begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.testers
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
name|testers
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
name|collect
operator|.
name|testing
operator|.
name|features
operator|.
name|CollectionSize
operator|.
name|ONE
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
name|CollectionSize
operator|.
name|ZERO
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
name|MinimalCollection
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

begin_comment
comment|/**  * A generic JUnit test which tests {@code retainAll} operations on a list.  * Can't be invoked directly; please see  * {@link com.google.common.collect.testing.ListTestSuiteBuilder}.  *  *<p>This class is GWT compatible.  *  * @author Chris Povirk  */
end_comment

begin_class
DECL|class|ListRetainAllTester
specifier|public
class|class
name|ListRetainAllTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractListTester
argument_list|<
name|E
argument_list|>
block|{
annotation|@
name|CollectionFeature
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
name|absent
operator|=
block|{
name|ZERO
block|,
name|ONE
block|}
argument_list|)
DECL|method|testRetainAll_duplicatesKept ()
specifier|public
name|void
name|testRetainAll_duplicatesKept
parameter_list|()
block|{
name|E
index|[]
name|array
init|=
name|createSamplesArray
argument_list|()
decl_stmt|;
name|array
index|[
literal|1
index|]
operator|=
name|samples
operator|.
name|e0
expr_stmt|;
name|collection
operator|=
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|array
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"containsDuplicates.retainAll(superset) should return false"
argument_list|,
name|collection
operator|.
name|retainAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|createSamplesArray
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|CollectionFeature
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
DECL|method|testRetainAll_duplicatesRemoved ()
specifier|public
name|void
name|testRetainAll_duplicatesRemoved
parameter_list|()
block|{
name|E
index|[]
name|array
init|=
name|createSamplesArray
argument_list|()
decl_stmt|;
name|array
index|[
literal|1
index|]
operator|=
name|samples
operator|.
name|e0
expr_stmt|;
name|collection
operator|=
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|array
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"containsDuplicates.retainAll(subset) should return true"
argument_list|,
name|collection
operator|.
name|retainAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|samples
operator|.
name|e2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|samples
operator|.
name|e2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

