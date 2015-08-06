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
name|DESCENDING_VIEW
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
name|SUBSET_VIEW
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
name|Bound
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
name|SortedSetSubsetTestSetGenerator
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
name|NavigableSetNavigationTester
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
name|Collections
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
name|NavigableSet
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
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_comment
comment|/**  * Creates, based on your criteria, a JUnit test suite that exhaustively tests  * a NavigableSet implementation.  */
end_comment

begin_class
annotation|@
name|SuppressUnderAndroid
DECL|class|NavigableSetTestSuiteBuilder
specifier|public
specifier|final
class|class
name|NavigableSetTestSuiteBuilder
parameter_list|<
name|E
parameter_list|>
extends|extends
name|SortedSetTestSuiteBuilder
argument_list|<
name|E
argument_list|>
block|{
DECL|method|using ( TestSortedSetGenerator<E> generator)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|NavigableSetTestSuiteBuilder
argument_list|<
name|E
argument_list|>
name|using
parameter_list|(
name|TestSortedSetGenerator
argument_list|<
name|E
argument_list|>
name|generator
parameter_list|)
block|{
name|NavigableSetTestSuiteBuilder
argument_list|<
name|E
argument_list|>
name|builder
init|=
operator|new
name|NavigableSetTestSuiteBuilder
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
annotation|@
name|Override
DECL|method|createDerivedSuites (FeatureSpecificTestSuiteBuilder< ?, ? extends OneSizeTestContainerGenerator<Collection<E>, E>> parentBuilder)
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
argument_list|<
name|TestSuite
argument_list|>
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
operator|!
name|parentBuilder
operator|.
name|getFeatures
argument_list|()
operator|.
name|contains
argument_list|(
name|SUBSET_VIEW
argument_list|)
condition|)
block|{
comment|// Other combinations are inherited from SortedSetTestSuiteBuilder.
name|derivedSuites
operator|.
name|add
argument_list|(
name|createSubsetSuite
argument_list|(
name|parentBuilder
argument_list|,
name|Bound
operator|.
name|NO_BOUND
argument_list|,
name|Bound
operator|.
name|INCLUSIVE
argument_list|)
argument_list|)
expr_stmt|;
name|derivedSuites
operator|.
name|add
argument_list|(
name|createSubsetSuite
argument_list|(
name|parentBuilder
argument_list|,
name|Bound
operator|.
name|EXCLUSIVE
argument_list|,
name|Bound
operator|.
name|NO_BOUND
argument_list|)
argument_list|)
expr_stmt|;
name|derivedSuites
operator|.
name|add
argument_list|(
name|createSubsetSuite
argument_list|(
name|parentBuilder
argument_list|,
name|Bound
operator|.
name|EXCLUSIVE
argument_list|,
name|Bound
operator|.
name|EXCLUSIVE
argument_list|)
argument_list|)
expr_stmt|;
name|derivedSuites
operator|.
name|add
argument_list|(
name|createSubsetSuite
argument_list|(
name|parentBuilder
argument_list|,
name|Bound
operator|.
name|EXCLUSIVE
argument_list|,
name|Bound
operator|.
name|INCLUSIVE
argument_list|)
argument_list|)
expr_stmt|;
name|derivedSuites
operator|.
name|add
argument_list|(
name|createSubsetSuite
argument_list|(
name|parentBuilder
argument_list|,
name|Bound
operator|.
name|INCLUSIVE
argument_list|,
name|Bound
operator|.
name|INCLUSIVE
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|DESCENDING_VIEW
argument_list|)
condition|)
block|{
name|derivedSuites
operator|.
name|add
argument_list|(
name|createDescendingSuite
argument_list|(
name|parentBuilder
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|derivedSuites
return|;
block|}
DECL|class|NavigableSetSubsetTestSetGenerator
specifier|public
specifier|static
specifier|final
class|class
name|NavigableSetSubsetTestSetGenerator
parameter_list|<
name|E
parameter_list|>
extends|extends
name|SortedSetSubsetTestSetGenerator
argument_list|<
name|E
argument_list|>
block|{
DECL|method|NavigableSetSubsetTestSetGenerator ( TestSortedSetGenerator<E> delegate, Bound to, Bound from)
specifier|public
name|NavigableSetSubsetTestSetGenerator
parameter_list|(
name|TestSortedSetGenerator
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|,
name|Bound
name|to
parameter_list|,
name|Bound
name|from
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
argument_list|,
name|to
argument_list|,
name|from
argument_list|)
expr_stmt|;
block|}
DECL|method|createSubSet (SortedSet<E> sortedSet, E firstExclusive, E lastExclusive)
annotation|@
name|Override
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|createSubSet
parameter_list|(
name|SortedSet
argument_list|<
name|E
argument_list|>
name|sortedSet
parameter_list|,
name|E
name|firstExclusive
parameter_list|,
name|E
name|lastExclusive
parameter_list|)
block|{
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|set
init|=
operator|(
name|NavigableSet
argument_list|<
name|E
argument_list|>
operator|)
name|sortedSet
decl_stmt|;
if|if
condition|(
name|from
operator|==
name|Bound
operator|.
name|NO_BOUND
operator|&&
name|to
operator|==
name|Bound
operator|.
name|INCLUSIVE
condition|)
block|{
return|return
name|set
operator|.
name|headSet
argument_list|(
name|lastInclusive
argument_list|,
literal|true
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|from
operator|==
name|Bound
operator|.
name|EXCLUSIVE
operator|&&
name|to
operator|==
name|Bound
operator|.
name|NO_BOUND
condition|)
block|{
return|return
name|set
operator|.
name|tailSet
argument_list|(
name|firstExclusive
argument_list|,
literal|false
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|from
operator|==
name|Bound
operator|.
name|EXCLUSIVE
operator|&&
name|to
operator|==
name|Bound
operator|.
name|EXCLUSIVE
condition|)
block|{
return|return
name|set
operator|.
name|subSet
argument_list|(
name|firstExclusive
argument_list|,
literal|false
argument_list|,
name|lastExclusive
argument_list|,
literal|false
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|from
operator|==
name|Bound
operator|.
name|EXCLUSIVE
operator|&&
name|to
operator|==
name|Bound
operator|.
name|INCLUSIVE
condition|)
block|{
return|return
name|set
operator|.
name|subSet
argument_list|(
name|firstExclusive
argument_list|,
literal|false
argument_list|,
name|lastInclusive
argument_list|,
literal|true
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|from
operator|==
name|Bound
operator|.
name|INCLUSIVE
operator|&&
name|to
operator|==
name|Bound
operator|.
name|INCLUSIVE
condition|)
block|{
return|return
name|set
operator|.
name|subSet
argument_list|(
name|firstInclusive
argument_list|,
literal|true
argument_list|,
name|lastInclusive
argument_list|,
literal|true
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|(
name|NavigableSet
argument_list|<
name|E
argument_list|>
operator|)
name|super
operator|.
name|createSubSet
argument_list|(
name|set
argument_list|,
name|firstExclusive
argument_list|,
name|lastExclusive
argument_list|)
return|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|newBuilderUsing ( TestSortedSetGenerator<E> delegate, Bound to, Bound from)
specifier|public
name|NavigableSetTestSuiteBuilder
argument_list|<
name|E
argument_list|>
name|newBuilderUsing
parameter_list|(
name|TestSortedSetGenerator
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|,
name|Bound
name|to
parameter_list|,
name|Bound
name|from
parameter_list|)
block|{
return|return
name|using
argument_list|(
operator|new
name|NavigableSetSubsetTestSetGenerator
argument_list|<
name|E
argument_list|>
argument_list|(
name|delegate
argument_list|,
name|to
argument_list|,
name|from
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Create a suite whose maps are descending views of other maps.    */
DECL|method|createDescendingSuite (final FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<E>, E>> parentBuilder)
specifier|private
name|TestSuite
name|createDescendingSuite
parameter_list|(
specifier|final
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
specifier|final
name|TestSetGenerator
argument_list|<
name|E
argument_list|>
name|delegate
init|=
operator|(
name|TestSetGenerator
argument_list|<
name|E
argument_list|>
operator|)
name|parentBuilder
operator|.
name|getSubjectGenerator
argument_list|()
operator|.
name|getInnerGenerator
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|features
init|=
operator|new
name|ArrayList
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|features
operator|.
name|add
argument_list|(
name|DESCENDING_VIEW
argument_list|)
expr_stmt|;
name|features
operator|.
name|addAll
argument_list|(
name|parentBuilder
operator|.
name|getFeatures
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|NavigableSetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestSetGenerator
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|SampleElements
argument_list|<
name|E
argument_list|>
name|samples
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|samples
argument_list|()
return|;
block|}
annotation|@
name|Override
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
name|delegate
operator|.
name|createArray
argument_list|(
name|length
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|List
argument_list|<
name|E
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|E
name|e
range|:
name|delegate
operator|.
name|order
argument_list|(
name|insertionOrder
argument_list|)
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|reverse
argument_list|(
name|list
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
annotation|@
name|Override
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
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|navigableSet
init|=
operator|(
name|NavigableSet
argument_list|<
name|E
argument_list|>
operator|)
name|delegate
operator|.
name|create
argument_list|(
name|elements
argument_list|)
decl_stmt|;
return|return
name|navigableSet
operator|.
name|descendingSet
argument_list|()
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
name|parentBuilder
operator|.
name|getName
argument_list|()
operator|+
literal|" descending"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|features
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
name|NavigableSetNavigationTester
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|testers
return|;
block|}
block|}
end_class

end_unit

