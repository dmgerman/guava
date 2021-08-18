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
name|features
operator|.
name|CollectionFeature
operator|.
name|SERIALIZABLE
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
name|CollectionFeature
operator|.
name|SERIALIZABLE_INCLUDING_VIEWS
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
name|CollectionSerializationEqualTester
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
name|SetAddAllTester
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
name|SetAddTester
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
name|SetEqualsTester
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
name|SetHashCodeTester
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
name|SetRemoveTester
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
name|ArrayList
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
comment|/**  * Creates, based on your criteria, a JUnit test suite that exhaustively tests a Set implementation.  *  * @author George van den Driessche  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|SetTestSuiteBuilder
specifier|public
class|class
name|SetTestSuiteBuilder
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractCollectionTestSuiteBuilder
argument_list|<
name|SetTestSuiteBuilder
argument_list|<
name|E
argument_list|>
argument_list|,
name|E
argument_list|>
block|{
DECL|method|using (TestSetGenerator<E> generator)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|SetTestSuiteBuilder
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
return|return
operator|new
name|SetTestSuiteBuilder
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
name|CollectionSerializationEqualTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|SetAddAllTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|SetAddTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|SetCreationTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|SetHashCodeTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|SetEqualsTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|testers
operator|.
name|add
argument_list|(
name|SetRemoveTester
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// SetRemoveAllTester doesn't exist because, Sets not permitting
comment|// duplicate elements, there are no tests for Set.removeAll() that aren't
comment|// covered by CollectionRemoveAllTester.
return|return
name|testers
return|;
block|}
annotation|@
name|Override
DECL|method|createDerivedSuites ( FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<E>, E>> parentBuilder)
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
name|Collection
argument_list|<
name|E
argument_list|>
argument_list|,
name|E
argument_list|>
argument_list|>
name|parentBuilder
parameter_list|)
block|{
name|List
argument_list|<
name|TestSuite
argument_list|>
name|derivedSuites
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|super
operator|.
name|createDerivedSuites
argument_list|(
name|parentBuilder
argument_list|)
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
name|SERIALIZABLE
argument_list|)
condition|)
block|{
name|derivedSuites
operator|.
name|add
argument_list|(
name|SetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|ReserializedSetGenerator
argument_list|<
name|E
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
name|getName
argument_list|()
operator|+
literal|" reserialized"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|computeReserializedCollectionFeatures
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
block|}
return|return
name|derivedSuites
return|;
block|}
DECL|class|ReserializedSetGenerator
specifier|static
class|class
name|ReserializedSetGenerator
parameter_list|<
name|E
parameter_list|>
implements|implements
name|TestSetGenerator
argument_list|<
name|E
argument_list|>
block|{
DECL|field|gen
specifier|final
name|OneSizeTestContainerGenerator
argument_list|<
name|Collection
argument_list|<
name|E
argument_list|>
argument_list|,
name|E
argument_list|>
name|gen
decl_stmt|;
DECL|method|ReserializedSetGenerator (OneSizeTestContainerGenerator<Collection<E>, E> gen)
specifier|private
name|ReserializedSetGenerator
parameter_list|(
name|OneSizeTestContainerGenerator
argument_list|<
name|Collection
argument_list|<
name|E
argument_list|>
argument_list|,
name|E
argument_list|>
name|gen
parameter_list|)
block|{
name|this
operator|.
name|gen
operator|=
name|gen
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|samples ()
specifier|public
name|SampleElements
argument_list|<
name|E
argument_list|>
name|samples
parameter_list|()
block|{
return|return
name|gen
operator|.
name|samples
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|create (Object... elements)
specifier|public
name|Set
argument_list|<
name|E
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
name|Set
argument_list|<
name|E
argument_list|>
operator|)
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|gen
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
DECL|method|createArray (int length)
specifier|public
name|E
index|[]
name|createArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
name|gen
operator|.
name|createArray
argument_list|(
name|length
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|order (List<E> insertionOrder)
specifier|public
name|Iterable
argument_list|<
name|E
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|insertionOrder
parameter_list|)
block|{
return|return
name|gen
operator|.
name|order
argument_list|(
name|insertionOrder
argument_list|)
return|;
block|}
block|}
DECL|method|computeReserializedCollectionFeatures (Set<Feature<?>> features)
specifier|private
specifier|static
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|computeReserializedCollectionFeatures
parameter_list|(
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|features
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
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|features
argument_list|)
decl_stmt|;
name|derivedFeatures
operator|.
name|remove
argument_list|(
name|SERIALIZABLE
argument_list|)
expr_stmt|;
name|derivedFeatures
operator|.
name|remove
argument_list|(
name|SERIALIZABLE_INCLUDING_VIEWS
argument_list|)
expr_stmt|;
return|return
name|derivedFeatures
return|;
block|}
block|}
end_class

end_unit

