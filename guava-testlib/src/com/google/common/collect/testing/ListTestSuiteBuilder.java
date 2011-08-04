begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
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
package|;
end_package

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
name|Feature
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
name|testers
operator|.
name|ListAddAllAtIndexTester
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
name|testers
operator|.
name|ListAddAllTester
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
name|testers
operator|.
name|ListAddAtIndexTester
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
name|testers
operator|.
name|ListAddTester
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
name|testers
operator|.
name|ListCreationTester
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
name|testers
operator|.
name|ListEqualsTester
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
name|testers
operator|.
name|ListGetTester
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
name|testers
operator|.
name|ListHashCodeTester
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
name|testers
operator|.
name|ListIndexOfTester
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
name|testers
operator|.
name|ListLastIndexOfTester
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
name|testers
operator|.
name|ListListIteratorTester
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
name|testers
operator|.
name|ListRemoveAllTester
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
name|testers
operator|.
name|ListRemoveAtIndexTester
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
name|testers
operator|.
name|ListRemoveTester
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
name|testers
operator|.
name|ListRetainAllTester
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
name|testers
operator|.
name|ListSetTester
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
name|testers
operator|.
name|ListSubListTester
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
name|testers
operator|.
name|ListToArrayTester
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestSuite
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
comment|/**  * Creates, based on your criteria, a JUnit test suite that exhaustively tests  * a List implementation.  *  * @author George van den Driessche  */
end_comment

begin_class
DECL|class|ListTestSuiteBuilder
specifier|public
specifier|final
class|class
name|ListTestSuiteBuilder
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractCollectionTestSuiteBuilder
argument_list|<
name|ListTestSuiteBuilder
argument_list|<
name|E
argument_list|>
argument_list|,
name|E
argument_list|>
block|{
DECL|method|using ( TestListGenerator<E> generator)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ListTestSuiteBuilder
argument_list|<
name|E
argument_list|>
name|using
parameter_list|(
name|TestListGenerator
argument_list|<
name|E
argument_list|>
name|generator
parameter_list|)
block|{
return|return
operator|new
name|ListTestSuiteBuilder
argument_list|<
name|E
argument_list|>
argument_list|()
operator|.
name|usingGenerator
argument_list|(
name|generator
argument_list|)
return|;
block|}
DECL|method|getTesters ()
annotation|@
name|Override
specifier|protected
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|>
argument_list|>
name|getTesters
parameter_list|()
block|{
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|>
argument_list|>
name|testers
init|=
name|Helpers
operator|.
name|copyToList
argument_list|(
name|super
operator|.
name|getTesters
argument_list|()
argument_list|)
decl_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListAddAllAtIndexTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListAddAllTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListAddAtIndexTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListAddTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListCreationTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListEqualsTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListGetTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListHashCodeTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListIndexOfTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListLastIndexOfTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListListIteratorTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListRemoveAllTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListRemoveAtIndexTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListRemoveTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListRetainAllTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListSetTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListSubListTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListToArrayTester
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|testers
return|;
block|}
comment|/**    * Specifies {@link CollectionFeature#KNOWN_ORDER} for all list tests, since    * lists have an iteration ordering corresponding to the insertion order.    */
DECL|method|createTestSuite ()
annotation|@
name|Override
specifier|public
name|TestSuite
name|createTestSuite
parameter_list|()
block|{
if|if
condition|(
operator|!
name|getFeatures
argument_list|()
operator|.
name|contains
argument_list|(
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|features
init|=
name|Helpers
operator|.
name|copyToList
argument_list|(
name|getFeatures
argument_list|()
argument_list|)
decl_stmt|;
name|features
operator|.
name|add
argument_list|(
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|)
expr_stmt|;
name|withFeatures
argument_list|(
name|features
argument_list|)
expr_stmt|;
block|}
return|return
name|super
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
block|}
end_class

end_unit

