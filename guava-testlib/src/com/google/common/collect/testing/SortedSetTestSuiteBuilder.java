begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|SortedSetNavigationTester
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
comment|/**  * Creates, based on your criteria, a JUnit test suite that exhaustively tests  * a SortedSet implementation.  */
end_comment

begin_class
DECL|class|SortedSetTestSuiteBuilder
specifier|public
class|class
name|SortedSetTestSuiteBuilder
parameter_list|<
name|E
parameter_list|>
extends|extends
name|SetTestSuiteBuilder
argument_list|<
name|E
argument_list|>
block|{
DECL|method|using ( TestSetGenerator<E> generator)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|SortedSetTestSuiteBuilder
argument_list|<
name|E
argument_list|>
name|using
parameter_list|(
name|TestSetGenerator
argument_list|<
name|E
argument_list|>
name|generator
parameter_list|)
block|{
name|SortedSetTestSuiteBuilder
argument_list|<
name|E
argument_list|>
name|builder
init|=
operator|new
name|SortedSetTestSuiteBuilder
argument_list|<
name|E
argument_list|>
argument_list|()
decl_stmt|;
name|builder
operator|.
name|usingGenerator
argument_list|(
name|generator
argument_list|)
expr_stmt|;
return|return
name|builder
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
name|SortedSetNavigationTester
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|testers
return|;
block|}
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

