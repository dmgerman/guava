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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|GwtIncompatible
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
name|AbstractTester
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
name|FeatureSpecificTestSuiteBuilder
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
name|ListTestSuiteBuilder
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
name|OneSizeTestContainerGenerator
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
name|TestListGenerator
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
name|features
operator|.
name|ListFeature
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
name|EnumSet
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
name|java
operator|.
name|util
operator|.
name|Set
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

begin_comment
comment|/**  * Creates, based on your criteria, a JUnit test suite that exhaustively tests  * a {@code ListMultimap} implementation.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|ListMultimapTestSuiteBuilder
specifier|public
class|class
name|ListMultimapTestSuiteBuilder
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|MultimapTestSuiteBuilder
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
block|{
DECL|method|using ( TestListMultimapGenerator<K, V> generator)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ListMultimapTestSuiteBuilder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|using
parameter_list|(
name|TestListMultimapGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|generator
parameter_list|)
block|{
name|ListMultimapTestSuiteBuilder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|result
init|=
operator|new
name|ListMultimapTestSuiteBuilder
argument_list|<>
argument_list|()
decl_stmt|;
name|result
operator|.
name|usingGenerator
argument_list|(
name|generator
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|getTesters ()
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
name|ListMultimapAsMapTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListMultimapEqualsTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListMultimapPutTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListMultimapPutAllTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListMultimapRemoveTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|ListMultimapReplaceValuesTester
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|testers
return|;
block|}
annotation|@
name|Override
DECL|method|computeMultimapGetTestSuite ( FeatureSpecificTestSuiteBuilder< ?, ? extends OneSizeTestContainerGenerator<ListMultimap<K, V>, Entry<K, V>>> parentBuilder)
name|TestSuite
name|computeMultimapGetTestSuite
parameter_list|(
name|FeatureSpecificTestSuiteBuilder
argument_list|<
name|?
argument_list|,
name|?
extends|extends
name|OneSizeTestContainerGenerator
argument_list|<
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|,
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|>
name|parentBuilder
parameter_list|)
block|{
return|return
name|ListTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|MultimapGetGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|parentBuilder
operator|.
name|getSubjectGenerator
argument_list|()
argument_list|)
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|computeMultimapGetFeatures
argument_list|(
name|parentBuilder
operator|.
name|getFeatures
argument_list|()
argument_list|)
argument_list|)
operator|.
name|named
argument_list|(
name|parentBuilder
operator|.
name|getName
argument_list|()
operator|+
literal|".get[key]"
argument_list|)
operator|.
name|suppressing
argument_list|(
name|parentBuilder
operator|.
name|getSuppressedTests
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|computeMultimapAsMapGetTestSuite ( FeatureSpecificTestSuiteBuilder< ?, ? extends OneSizeTestContainerGenerator<ListMultimap<K, V>, Entry<K, V>>> parentBuilder)
name|TestSuite
name|computeMultimapAsMapGetTestSuite
parameter_list|(
name|FeatureSpecificTestSuiteBuilder
argument_list|<
name|?
argument_list|,
name|?
extends|extends
name|OneSizeTestContainerGenerator
argument_list|<
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|,
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|>
name|parentBuilder
parameter_list|)
block|{
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|features
init|=
name|computeMultimapAsMapGetFeatures
argument_list|(
name|parentBuilder
operator|.
name|getFeatures
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|Collections
operator|.
name|disjoint
argument_list|(
name|features
argument_list|,
name|EnumSet
operator|.
name|allOf
argument_list|(
name|CollectionSize
operator|.
name|class
argument_list|)
argument_list|)
condition|)
block|{
return|return
operator|new
name|TestSuite
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|ListTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|MultimapAsMapGetGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|parentBuilder
operator|.
name|getSubjectGenerator
argument_list|()
argument_list|)
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|features
argument_list|)
operator|.
name|named
argument_list|(
name|parentBuilder
operator|.
name|getName
argument_list|()
operator|+
literal|".asMap[].get[key]"
argument_list|)
operator|.
name|suppressing
argument_list|(
name|parentBuilder
operator|.
name|getSuppressedTests
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|computeMultimapGetFeatures (Set<Feature<?>> multimapFeatures)
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|computeMultimapGetFeatures
parameter_list|(
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|multimapFeatures
parameter_list|)
block|{
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|derivedFeatures
init|=
name|super
operator|.
name|computeMultimapGetFeatures
argument_list|(
name|multimapFeatures
argument_list|)
decl_stmt|;
if|if
condition|(
name|derivedFeatures
operator|.
name|contains
argument_list|(
name|CollectionFeature
operator|.
name|SUPPORTS_ADD
argument_list|)
condition|)
block|{
name|derivedFeatures
operator|.
name|add
argument_list|(
name|ListFeature
operator|.
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|derivedFeatures
operator|.
name|contains
argument_list|(
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
argument_list|)
condition|)
block|{
name|derivedFeatures
operator|.
name|add
argument_list|(
name|ListFeature
operator|.
name|GENERAL_PURPOSE
argument_list|)
expr_stmt|;
block|}
return|return
name|derivedFeatures
return|;
block|}
DECL|class|MultimapGetGenerator
specifier|private
specifier|static
class|class
name|MultimapGetGenerator
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|MultimapTestSuiteBuilder
operator|.
name|MultimapGetGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
implements|implements
name|TestListGenerator
argument_list|<
name|V
argument_list|>
block|{
DECL|method|MultimapGetGenerator ( OneSizeTestContainerGenerator<ListMultimap<K, V>, Entry<K, V>> multimapGenerator)
specifier|public
name|MultimapGetGenerator
parameter_list|(
name|OneSizeTestContainerGenerator
argument_list|<
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|,
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|multimapGenerator
parameter_list|)
block|{
name|super
argument_list|(
name|multimapGenerator
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|create (Object... elements)
specifier|public
name|List
argument_list|<
name|V
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|elements
parameter_list|)
block|{
return|return
operator|(
name|List
argument_list|<
name|V
argument_list|>
operator|)
name|super
operator|.
name|create
argument_list|(
name|elements
argument_list|)
return|;
block|}
block|}
DECL|class|MultimapAsMapGetGenerator
specifier|private
specifier|static
class|class
name|MultimapAsMapGetGenerator
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|MultimapTestSuiteBuilder
operator|.
name|MultimapAsMapGetGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
implements|implements
name|TestListGenerator
argument_list|<
name|V
argument_list|>
block|{
DECL|method|MultimapAsMapGetGenerator ( OneSizeTestContainerGenerator<ListMultimap<K, V>, Entry<K, V>> multimapGenerator)
specifier|public
name|MultimapAsMapGetGenerator
parameter_list|(
name|OneSizeTestContainerGenerator
argument_list|<
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|,
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|multimapGenerator
parameter_list|)
block|{
name|super
argument_list|(
name|multimapGenerator
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|create (Object... elements)
specifier|public
name|List
argument_list|<
name|V
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|elements
parameter_list|)
block|{
return|return
operator|(
name|List
argument_list|<
name|V
argument_list|>
operator|)
name|super
operator|.
name|create
argument_list|(
name|elements
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

