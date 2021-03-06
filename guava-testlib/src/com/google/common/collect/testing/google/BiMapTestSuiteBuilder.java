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
name|BiMap
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
name|MapTestSuiteBuilder
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
name|PerCollectionSizeTestSuiteBuilder
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
name|SetTestSuiteBuilder
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
name|MapFeature
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
name|google
operator|.
name|DerivedGoogleCollectionGenerators
operator|.
name|BiMapValueSetGenerator
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
name|google
operator|.
name|DerivedGoogleCollectionGenerators
operator|.
name|InverseBiMapGenerator
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
name|google
operator|.
name|DerivedGoogleCollectionGenerators
operator|.
name|MapGenerator
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
name|SetCreationTester
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
comment|/**  * Creates, based on your criteria, a JUnit test suite that exhaustively tests a {@code BiMap}  * implementation.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|BiMapTestSuiteBuilder
specifier|public
class|class
name|BiMapTestSuiteBuilder
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|PerCollectionSizeTestSuiteBuilder
argument_list|<
name|BiMapTestSuiteBuilder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|,
name|TestBiMapGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|,
name|BiMap
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
block|{
DECL|method|using (TestBiMapGenerator<K, V> generator)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|BiMapTestSuiteBuilder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|using
parameter_list|(
name|TestBiMapGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|generator
parameter_list|)
block|{
return|return
operator|new
name|BiMapTestSuiteBuilder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
operator|.
name|usingGenerator
argument_list|(
name|generator
argument_list|)
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
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|BiMapEntrySetTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|BiMapPutTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|BiMapInverseTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|BiMapRemoveTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|BiMapClearTester
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|testers
return|;
block|}
DECL|enum|NoRecurse
enum|enum
name|NoRecurse
implements|implements
name|Feature
argument_list|<
name|Void
argument_list|>
block|{
DECL|enumConstant|INVERSE
name|INVERSE
block|;
annotation|@
name|Override
DECL|method|getImpliedFeatures ()
specifier|public
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
super|super
name|Void
argument_list|>
argument_list|>
name|getImpliedFeatures
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|createDerivedSuites ( FeatureSpecificTestSuiteBuilder< ?, ? extends OneSizeTestContainerGenerator<BiMap<K, V>, Entry<K, V>>> parentBuilder)
specifier|protected
name|List
argument_list|<
name|TestSuite
argument_list|>
name|createDerivedSuites
parameter_list|(
name|FeatureSpecificTestSuiteBuilder
argument_list|<
name|?
argument_list|,
name|?
extends|extends
name|OneSizeTestContainerGenerator
argument_list|<
name|BiMap
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
name|List
argument_list|<
name|TestSuite
argument_list|>
name|derived
init|=
name|super
operator|.
name|createDerivedSuites
argument_list|(
name|parentBuilder
argument_list|)
decl_stmt|;
comment|// TODO(cpovirk): consider using this approach (derived suites instead of extension) in
comment|// ListTestSuiteBuilder, etc.?
name|derived
operator|.
name|add
argument_list|(
name|MapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|MapGenerator
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
name|parentBuilder
operator|.
name|getFeatures
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
name|parentBuilder
operator|.
name|getName
argument_list|()
operator|+
literal|" [Map]"
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
name|suppressing
argument_list|(
name|SetCreationTester
operator|.
name|class
operator|.
name|getMethods
argument_list|()
argument_list|)
comment|// BiMap.entrySet() duplicate-handling behavior is too confusing for SetCreationTester
operator|.
name|withSetUp
argument_list|(
name|parentBuilder
operator|.
name|getSetUp
argument_list|()
argument_list|)
operator|.
name|withTearDown
argument_list|(
name|parentBuilder
operator|.
name|getTearDown
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
comment|/*      * TODO(cpovirk): the Map tests duplicate most of this effort by using a      * CollectionTestSuiteBuilder on values(). It would be nice to avoid that      */
name|derived
operator|.
name|add
argument_list|(
name|SetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|BiMapValueSetGenerator
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
name|computeValuesSetFeatures
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
literal|" values [Set]"
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
name|suppressing
argument_list|(
name|SetCreationTester
operator|.
name|class
operator|.
name|getMethods
argument_list|()
argument_list|)
comment|// BiMap.values() duplicate-handling behavior is too confusing for SetCreationTester
operator|.
name|withSetUp
argument_list|(
name|parentBuilder
operator|.
name|getSetUp
argument_list|()
argument_list|)
operator|.
name|withTearDown
argument_list|(
name|parentBuilder
operator|.
name|getTearDown
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|parentBuilder
operator|.
name|getFeatures
argument_list|()
operator|.
name|contains
argument_list|(
name|NoRecurse
operator|.
name|INVERSE
argument_list|)
condition|)
block|{
name|derived
operator|.
name|add
argument_list|(
name|BiMapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|InverseBiMapGenerator
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
name|computeInverseFeatures
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
literal|" inverse"
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
name|withSetUp
argument_list|(
name|parentBuilder
operator|.
name|getSetUp
argument_list|()
argument_list|)
operator|.
name|withTearDown
argument_list|(
name|parentBuilder
operator|.
name|getTearDown
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|derived
return|;
block|}
DECL|method|computeInverseFeatures (Set<Feature<?>> mapFeatures)
specifier|private
specifier|static
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|computeInverseFeatures
parameter_list|(
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|mapFeatures
parameter_list|)
block|{
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|inverseFeatures
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|mapFeatures
argument_list|)
decl_stmt|;
name|boolean
name|nullKeys
init|=
name|inverseFeatures
operator|.
name|remove
argument_list|(
name|MapFeature
operator|.
name|ALLOWS_NULL_KEYS
argument_list|)
decl_stmt|;
name|boolean
name|nullValues
init|=
name|inverseFeatures
operator|.
name|remove
argument_list|(
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|)
decl_stmt|;
if|if
condition|(
name|nullKeys
condition|)
block|{
name|inverseFeatures
operator|.
name|add
argument_list|(
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|nullValues
condition|)
block|{
name|inverseFeatures
operator|.
name|add
argument_list|(
name|MapFeature
operator|.
name|ALLOWS_NULL_KEYS
argument_list|)
expr_stmt|;
block|}
name|inverseFeatures
operator|.
name|add
argument_list|(
name|NoRecurse
operator|.
name|INVERSE
argument_list|)
expr_stmt|;
name|inverseFeatures
operator|.
name|remove
argument_list|(
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|)
expr_stmt|;
name|inverseFeatures
operator|.
name|add
argument_list|(
name|MapFeature
operator|.
name|REJECTS_DUPLICATES_AT_CREATION
argument_list|)
expr_stmt|;
return|return
name|inverseFeatures
return|;
block|}
comment|// TODO(lowasser): can we eliminate the duplication from MapTestSuiteBuilder here?
DECL|method|computeValuesSetFeatures (Set<Feature<?>> mapFeatures)
specifier|private
specifier|static
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|computeValuesSetFeatures
parameter_list|(
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|mapFeatures
parameter_list|)
block|{
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|valuesCollectionFeatures
init|=
name|computeCommonDerivedCollectionFeatures
argument_list|(
name|mapFeatures
argument_list|)
decl_stmt|;
name|valuesCollectionFeatures
operator|.
name|add
argument_list|(
name|CollectionFeature
operator|.
name|ALLOWS_NULL_QUERIES
argument_list|)
expr_stmt|;
if|if
condition|(
name|mapFeatures
operator|.
name|contains
argument_list|(
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|)
condition|)
block|{
name|valuesCollectionFeatures
operator|.
name|add
argument_list|(
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|)
expr_stmt|;
block|}
name|valuesCollectionFeatures
operator|.
name|add
argument_list|(
name|CollectionFeature
operator|.
name|REJECTS_DUPLICATES_AT_CREATION
argument_list|)
expr_stmt|;
return|return
name|valuesCollectionFeatures
return|;
block|}
DECL|method|computeCommonDerivedCollectionFeatures ( Set<Feature<?>> mapFeatures)
specifier|private
specifier|static
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|computeCommonDerivedCollectionFeatures
parameter_list|(
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|mapFeatures
parameter_list|)
block|{
return|return
name|MapTestSuiteBuilder
operator|.
name|computeCommonDerivedCollectionFeatures
argument_list|(
name|mapFeatures
argument_list|)
return|;
block|}
block|}
end_class

end_unit

