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
name|REJECTS_DUPLICATES_AT_CREATION
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
comment|/**  * A generic JUnit test which tests {@code indexOf()} operations on a list.  * Can't be invoked directly; please see  * {@link com.google.common.collect.testing.ListTestSuiteBuilder}.  *  *<p>This class is GWT compatible.  *  * @author Chris Povirk  */
end_comment

begin_class
DECL|class|ListIndexOfTester
specifier|public
class|class
name|ListIndexOfTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractListIndexOfTester
argument_list|<
name|E
argument_list|>
block|{
DECL|method|find (Object o)
annotation|@
name|Override
specifier|protected
name|int
name|find
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|getList
argument_list|()
operator|.
name|indexOf
argument_list|(
name|o
argument_list|)
return|;
block|}
DECL|method|getMethodName ()
annotation|@
name|Override
specifier|protected
name|String
name|getMethodName
parameter_list|()
block|{
return|return
literal|"indexOf"
return|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|REJECTS_DUPLICATES_AT_CREATION
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
DECL|method|testIndexOf_duplicate ()
specifier|public
name|void
name|testIndexOf_duplicate
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
name|getNumElements
argument_list|()
operator|/
literal|2
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
name|assertEquals
argument_list|(
literal|"indexOf(duplicate) should return index of first occurrence"
argument_list|,
literal|0
argument_list|,
name|getList
argument_list|()
operator|.
name|indexOf
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

