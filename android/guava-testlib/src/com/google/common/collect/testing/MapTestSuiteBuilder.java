begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|DerivedCollectionGenerators
operator|.
name|keySetGenerator
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
name|testing
operator|.
name|DerivedCollectionGenerators
operator|.
name|MapEntrySetGenerator
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
name|DerivedCollectionGenerators
operator|.
name|MapValueCollectionGenerator
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
name|testers
operator|.
name|MapClearTester
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
name|MapContainsKeyTester
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
name|MapContainsValueTester
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
name|MapCreationTester
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
name|MapEntrySetTester
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
name|MapEqualsTester
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
name|MapGetTester
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
name|MapHashCodeTester
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
name|MapIsEmptyTester
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
name|MapPutAllTester
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
name|MapPutTester
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
name|MapRemoveTester
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
name|MapSerializationTester
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
name|MapSizeTester
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
name|MapToStringTester
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
name|testing
operator|.
name|SerializableTester
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
comment|/**  * Creates, based on your criteria, a JUnit test suite that exhaustively tests a Map implementation.  *  * @author George van den Driessche  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|MapTestSuiteBuilder
specifier|public
class|class
name|MapTestSuiteBuilder
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|PerCollectionSizeTestSuiteBuilder
argument_list|<
name|MapTestSuiteBuilder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|,
name|TestMapGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|,
name|Map
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
DECL|method|using (TestMapGenerator<K, V> generator)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|MapTestSuiteBuilder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|using
parameter_list|(
name|TestMapGenerator
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
name|MapTestSuiteBuilder
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
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// Class parameters must be raw.
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
return|return
name|Arrays
operator|.
expr|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|>
operator|>
name|asList
argument_list|(
name|MapClearTester
operator|.
name|class
argument_list|,
name|MapContainsKeyTester
operator|.
name|class
argument_list|,
name|MapContainsValueTester
operator|.
name|class
argument_list|,
name|MapCreationTester
operator|.
name|class
argument_list|,
name|MapEntrySetTester
operator|.
name|class
argument_list|,
name|MapEqualsTester
operator|.
name|class
argument_list|,
name|MapGetTester
operator|.
name|class
argument_list|,
name|MapHashCodeTester
operator|.
name|class
argument_list|,
name|MapIsEmptyTester
operator|.
name|class
argument_list|,
name|MapPutTester
operator|.
name|class
argument_list|,
name|MapPutAllTester
operator|.
name|class
argument_list|,
name|MapRemoveTester
operator|.
name|class
argument_list|,
name|MapSerializationTester
operator|.
name|class
argument_list|,
name|MapSizeTester
operator|.
name|class
argument_list|,
name|MapToStringTester
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createDerivedSuites ( FeatureSpecificTestSuiteBuilder< ?, ? extends OneSizeTestContainerGenerator<Map<K, V>, Entry<K, V>>> parentBuilder)
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
name|Map
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
comment|// TODO: Once invariant support is added, supply invariants to each of the
comment|// derived suites, to check that mutations to the derived collections are
comment|// reflected in the underlying map.
name|List
argument_list|<
name|TestSuite
argument_list|>
name|derivedSuites
init|=
name|super
operator|.
name|createDerivedSuites
argument_list|(
name|parentBuilder
argument_list|)
decl_stmt|;
if|if
condition|(
name|parentBuilder
operator|.
name|getFeatures
argument_list|()
operator|.
name|contains
argument_list|(
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|)
condition|)
block|{
name|derivedSuites
operator|.
name|add
argument_list|(
name|MapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|ReserializedMapGenerator
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
name|computeReserializedMapFeatures
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
literal|" reserialized"
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
name|derivedSuites
operator|.
name|add
argument_list|(
name|createDerivedEntrySetSuite
argument_list|(
operator|new
name|MapEntrySetGenerator
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
name|computeEntrySetFeatures
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
literal|" entrySet"
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
name|derivedSuites
operator|.
name|add
argument_list|(
name|createDerivedKeySetSuite
argument_list|(
name|keySetGenerator
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
name|computeKeySetFeatures
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
literal|" keys"
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
name|derivedSuites
operator|.
name|add
argument_list|(
name|createDerivedValueCollectionSuite
argument_list|(
operator|new
name|MapValueCollectionGenerator
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
name|named
argument_list|(
name|parentBuilder
operator|.
name|getName
argument_list|()
operator|+
literal|" values"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|computeValuesCollectionFeatures
argument_list|(
name|parentBuilder
operator|.
name|getFeatures
argument_list|()
argument_list|)
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
return|return
name|derivedSuites
return|;
block|}
DECL|method|createDerivedEntrySetSuite ( TestSetGenerator<Entry<K, V>> entrySetGenerator)
specifier|protected
name|SetTestSuiteBuilder
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|createDerivedEntrySetSuite
parameter_list|(
name|TestSetGenerator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entrySetGenerator
parameter_list|)
block|{
return|return
name|SetTestSuiteBuilder
operator|.
name|using
argument_list|(
name|entrySetGenerator
argument_list|)
return|;
block|}
DECL|method|createDerivedKeySetSuite (TestSetGenerator<K> keySetGenerator)
specifier|protected
name|SetTestSuiteBuilder
argument_list|<
name|K
argument_list|>
name|createDerivedKeySetSuite
parameter_list|(
name|TestSetGenerator
argument_list|<
name|K
argument_list|>
name|keySetGenerator
parameter_list|)
block|{
return|return
name|SetTestSuiteBuilder
operator|.
name|using
argument_list|(
name|keySetGenerator
argument_list|)
return|;
block|}
DECL|method|createDerivedValueCollectionSuite ( TestCollectionGenerator<V> valueCollectionGenerator)
specifier|protected
name|CollectionTestSuiteBuilder
argument_list|<
name|V
argument_list|>
name|createDerivedValueCollectionSuite
parameter_list|(
name|TestCollectionGenerator
argument_list|<
name|V
argument_list|>
name|valueCollectionGenerator
parameter_list|)
block|{
return|return
name|CollectionTestSuiteBuilder
operator|.
name|using
argument_list|(
name|valueCollectionGenerator
argument_list|)
return|;
block|}
DECL|method|computeReserializedMapFeatures (Set<Feature<?>> mapFeatures)
specifier|private
specifier|static
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|computeReserializedMapFeatures
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
name|derivedFeatures
init|=
name|Helpers
operator|.
name|copyToSet
argument_list|(
name|mapFeatures
argument_list|)
decl_stmt|;
name|derivedFeatures
operator|.
name|remove
argument_list|(
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|)
expr_stmt|;
name|derivedFeatures
operator|.
name|remove
argument_list|(
name|CollectionFeature
operator|.
name|SERIALIZABLE_INCLUDING_VIEWS
argument_list|)
expr_stmt|;
return|return
name|derivedFeatures
return|;
block|}
DECL|method|computeEntrySetFeatures (Set<Feature<?>> mapFeatures)
specifier|private
specifier|static
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|computeEntrySetFeatures
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
name|entrySetFeatures
init|=
name|computeCommonDerivedCollectionFeatures
argument_list|(
name|mapFeatures
argument_list|)
decl_stmt|;
if|if
condition|(
name|mapFeatures
operator|.
name|contains
argument_list|(
name|MapFeature
operator|.
name|ALLOWS_NULL_ENTRY_QUERIES
argument_list|)
condition|)
block|{
name|entrySetFeatures
operator|.
name|add
argument_list|(
name|CollectionFeature
operator|.
name|ALLOWS_NULL_QUERIES
argument_list|)
expr_stmt|;
block|}
return|return
name|entrySetFeatures
return|;
block|}
DECL|method|computeKeySetFeatures (Set<Feature<?>> mapFeatures)
specifier|private
specifier|static
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|computeKeySetFeatures
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
name|keySetFeatures
init|=
name|computeCommonDerivedCollectionFeatures
argument_list|(
name|mapFeatures
argument_list|)
decl_stmt|;
comment|// TODO(lowasser): make this trigger only if the map is a submap
comment|// currently, the KeySetGenerator won't work properly for a subset of a keyset of a submap
name|keySetFeatures
operator|.
name|add
argument_list|(
name|CollectionFeature
operator|.
name|SUBSET_VIEW
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
name|ALLOWS_NULL_KEYS
argument_list|)
condition|)
block|{
name|keySetFeatures
operator|.
name|add
argument_list|(
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mapFeatures
operator|.
name|contains
argument_list|(
name|MapFeature
operator|.
name|ALLOWS_NULL_KEY_QUERIES
argument_list|)
condition|)
block|{
name|keySetFeatures
operator|.
name|add
argument_list|(
name|CollectionFeature
operator|.
name|ALLOWS_NULL_QUERIES
argument_list|)
expr_stmt|;
block|}
return|return
name|keySetFeatures
return|;
block|}
DECL|method|computeValuesCollectionFeatures (Set<Feature<?>> mapFeatures)
specifier|private
specifier|static
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|computeValuesCollectionFeatures
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
if|if
condition|(
name|mapFeatures
operator|.
name|contains
argument_list|(
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUE_QUERIES
argument_list|)
condition|)
block|{
name|valuesCollectionFeatures
operator|.
name|add
argument_list|(
name|CollectionFeature
operator|.
name|ALLOWS_NULL_QUERIES
argument_list|)
expr_stmt|;
block|}
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
return|return
name|valuesCollectionFeatures
return|;
block|}
DECL|method|computeCommonDerivedCollectionFeatures ( Set<Feature<?>> mapFeatures)
specifier|public
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
name|mapFeatures
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|mapFeatures
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|derivedFeatures
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|mapFeatures
operator|.
name|remove
argument_list|(
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|)
expr_stmt|;
if|if
condition|(
name|mapFeatures
operator|.
name|remove
argument_list|(
name|CollectionFeature
operator|.
name|SERIALIZABLE_INCLUDING_VIEWS
argument_list|)
condition|)
block|{
name|derivedFeatures
operator|.
name|add
argument_list|(
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mapFeatures
operator|.
name|contains
argument_list|(
name|MapFeature
operator|.
name|SUPPORTS_REMOVE
argument_list|)
condition|)
block|{
name|derivedFeatures
operator|.
name|add
argument_list|(
name|CollectionFeature
operator|.
name|SUPPORTS_REMOVE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mapFeatures
operator|.
name|contains
argument_list|(
name|MapFeature
operator|.
name|REJECTS_DUPLICATES_AT_CREATION
argument_list|)
condition|)
block|{
name|derivedFeatures
operator|.
name|add
argument_list|(
name|CollectionFeature
operator|.
name|REJECTS_DUPLICATES_AT_CREATION
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mapFeatures
operator|.
name|contains
argument_list|(
name|MapFeature
operator|.
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
argument_list|)
condition|)
block|{
name|derivedFeatures
operator|.
name|add
argument_list|(
name|CollectionFeature
operator|.
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
argument_list|)
expr_stmt|;
block|}
comment|// add the intersection of CollectionFeature.values() and mapFeatures
for|for
control|(
name|CollectionFeature
name|feature
range|:
name|CollectionFeature
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|mapFeatures
operator|.
name|contains
argument_list|(
name|feature
argument_list|)
condition|)
block|{
name|derivedFeatures
operator|.
name|add
argument_list|(
name|feature
argument_list|)
expr_stmt|;
block|}
block|}
comment|// add the intersection of CollectionSize.values() and mapFeatures
for|for
control|(
name|CollectionSize
name|size
range|:
name|CollectionSize
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|mapFeatures
operator|.
name|contains
argument_list|(
name|size
argument_list|)
condition|)
block|{
name|derivedFeatures
operator|.
name|add
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|derivedFeatures
return|;
block|}
DECL|class|ReserializedMapGenerator
specifier|private
specifier|static
class|class
name|ReserializedMapGenerator
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|TestMapGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|mapGenerator
specifier|private
specifier|final
name|OneSizeTestContainerGenerator
argument_list|<
name|Map
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
name|mapGenerator
decl_stmt|;
DECL|method|ReserializedMapGenerator ( OneSizeTestContainerGenerator<Map<K, V>, Entry<K, V>> mapGenerator)
specifier|public
name|ReserializedMapGenerator
parameter_list|(
name|OneSizeTestContainerGenerator
argument_list|<
name|Map
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
name|mapGenerator
parameter_list|)
block|{
name|this
operator|.
name|mapGenerator
operator|=
name|mapGenerator
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|samples ()
specifier|public
name|SampleElements
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|samples
parameter_list|()
block|{
return|return
name|mapGenerator
operator|.
name|samples
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createArray (int length)
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|createArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
name|mapGenerator
operator|.
name|createArray
argument_list|(
name|length
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|order (List<Entry<K, V>> insertionOrder)
specifier|public
name|Iterable
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|insertionOrder
parameter_list|)
block|{
return|return
name|mapGenerator
operator|.
name|order
argument_list|(
name|insertionOrder
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|create (Object... elements)
specifier|public
name|Map
argument_list|<
name|K
argument_list|,
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
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|mapGenerator
operator|.
name|create
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createKeyArray (int length)
specifier|public
name|K
index|[]
name|createKeyArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|(
operator|(
name|TestMapGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|mapGenerator
operator|.
name|getInnerGenerator
argument_list|()
operator|)
operator|.
name|createKeyArray
argument_list|(
name|length
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createValueArray (int length)
specifier|public
name|V
index|[]
name|createValueArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|(
operator|(
name|TestMapGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|mapGenerator
operator|.
name|getInnerGenerator
argument_list|()
operator|)
operator|.
name|createValueArray
argument_list|(
name|length
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

