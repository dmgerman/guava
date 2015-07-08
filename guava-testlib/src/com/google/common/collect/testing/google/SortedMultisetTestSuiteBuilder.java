begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not  * use this file except in compliance with the License. You may obtain a copy of  * the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the  * License for the specific language governing permissions and limitations under  * the License.  */
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
name|features
operator|.
name|CollectionFeature
operator|.
name|KNOWN_ORDER
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
name|RESTRICTS_ELEMENTS
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
name|collect
operator|.
name|BoundType
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
name|ImmutableList
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
name|Lists
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
name|Multiset
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
name|SortedMultiset
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
name|SampleElements
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
name|testing
operator|.
name|SerializableTester
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
name|Arrays
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
name|Comparator
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

begin_comment
comment|/**  * Creates, based on your criteria, a JUnit test suite that exhaustively tests a  * {@code SortedMultiset} implementation.  *  *<p><b>Warning:</b> expects that {@code E} is a String.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|SortedMultisetTestSuiteBuilder
specifier|public
class|class
name|SortedMultisetTestSuiteBuilder
parameter_list|<
name|E
parameter_list|>
extends|extends
name|MultisetTestSuiteBuilder
argument_list|<
name|E
argument_list|>
block|{
DECL|method|using ( TestMultisetGenerator<E> generator)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|SortedMultisetTestSuiteBuilder
argument_list|<
name|E
argument_list|>
name|using
parameter_list|(
name|TestMultisetGenerator
argument_list|<
name|E
argument_list|>
name|generator
parameter_list|)
block|{
name|SortedMultisetTestSuiteBuilder
argument_list|<
name|E
argument_list|>
name|result
init|=
operator|new
name|SortedMultisetTestSuiteBuilder
argument_list|<
name|E
argument_list|>
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
DECL|method|createTestSuite ()
specifier|public
name|TestSuite
name|createTestSuite
parameter_list|()
block|{
name|withFeatures
argument_list|(
name|KNOWN_ORDER
argument_list|)
expr_stmt|;
name|TestSuite
name|suite
init|=
name|super
operator|.
name|createTestSuite
argument_list|()
decl_stmt|;
for|for
control|(
name|TestSuite
name|subSuite
range|:
name|createDerivedSuites
argument_list|(
name|this
argument_list|)
control|)
block|{
name|suite
operator|.
name|addTest
argument_list|(
name|subSuite
argument_list|)
expr_stmt|;
block|}
return|return
name|suite
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
name|MultisetNavigationTester
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
DECL|method|createElementSetTestSuite (FeatureSpecificTestSuiteBuilder< ?, ? extends OneSizeTestContainerGenerator<Collection<E>, E>> parentBuilder)
name|TestSuite
name|createElementSetTestSuite
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
comment|// TODO(lowasser): make a SortedElementSetGenerator
return|return
name|SetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|ElementSetGenerator
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
literal|".elementSet"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|computeElementSetFeatures
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
name|createTestSuite
argument_list|()
return|;
block|}
comment|/**    * To avoid infinite recursion, test suites with these marker features won't    * have derived suites created for them.    */
DECL|enum|NoRecurse
enum|enum
name|NoRecurse
implements|implements
name|Feature
argument_list|<
name|Void
argument_list|>
block|{
DECL|enumConstant|SUBMULTISET
DECL|enumConstant|DESCENDING
name|SUBMULTISET
block|,
name|DESCENDING
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
comment|/**    * Two bounds (from and to) define how to build a subMultiset.    */
DECL|enum|Bound
enum|enum
name|Bound
block|{
DECL|enumConstant|INCLUSIVE
DECL|enumConstant|EXCLUSIVE
DECL|enumConstant|NO_BOUND
name|INCLUSIVE
block|,
name|EXCLUSIVE
block|,
name|NO_BOUND
block|;   }
DECL|method|createDerivedSuites ( SortedMultisetTestSuiteBuilder<E> parentBuilder)
name|List
argument_list|<
name|TestSuite
argument_list|>
name|createDerivedSuites
parameter_list|(
name|SortedMultisetTestSuiteBuilder
argument_list|<
name|E
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
name|Lists
operator|.
name|newArrayList
argument_list|()
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
name|NoRecurse
operator|.
name|DESCENDING
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
name|createReserializedSuite
argument_list|(
name|parentBuilder
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
name|NoRecurse
operator|.
name|SUBMULTISET
argument_list|)
condition|)
block|{
name|derivedSuites
operator|.
name|add
argument_list|(
name|createSubMultisetSuite
argument_list|(
name|parentBuilder
argument_list|,
name|Bound
operator|.
name|NO_BOUND
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
name|createSubMultisetSuite
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
name|createSubMultisetSuite
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
name|createSubMultisetSuite
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
name|createSubMultisetSuite
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
name|createSubMultisetSuite
argument_list|(
name|parentBuilder
argument_list|,
name|Bound
operator|.
name|INCLUSIVE
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
name|createSubMultisetSuite
argument_list|(
name|parentBuilder
argument_list|,
name|Bound
operator|.
name|INCLUSIVE
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
name|createSubMultisetSuite
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
return|return
name|derivedSuites
return|;
block|}
DECL|method|createSubMultisetSuite ( SortedMultisetTestSuiteBuilder<E> parentBuilder, final Bound from, final Bound to)
specifier|private
name|TestSuite
name|createSubMultisetSuite
parameter_list|(
name|SortedMultisetTestSuiteBuilder
argument_list|<
name|E
argument_list|>
name|parentBuilder
parameter_list|,
specifier|final
name|Bound
name|from
parameter_list|,
specifier|final
name|Bound
name|to
parameter_list|)
block|{
specifier|final
name|TestMultisetGenerator
argument_list|<
name|E
argument_list|>
name|delegate
init|=
operator|(
name|TestMultisetGenerator
argument_list|<
name|E
argument_list|>
operator|)
name|parentBuilder
operator|.
name|getSubjectGenerator
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|features
init|=
operator|new
name|HashSet
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
name|NoRecurse
operator|.
name|SUBMULTISET
argument_list|)
expr_stmt|;
name|features
operator|.
name|add
argument_list|(
name|RESTRICTS_ELEMENTS
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
if|if
condition|(
operator|!
name|features
operator|.
name|remove
argument_list|(
name|SERIALIZABLE_INCLUDING_VIEWS
argument_list|)
condition|)
block|{
name|features
operator|.
name|remove
argument_list|(
name|SERIALIZABLE
argument_list|)
expr_stmt|;
block|}
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|emptyMultiset
init|=
operator|(
name|SortedMultiset
argument_list|<
name|E
argument_list|>
operator|)
name|delegate
operator|.
name|create
argument_list|()
decl_stmt|;
specifier|final
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
init|=
name|emptyMultiset
operator|.
name|comparator
argument_list|()
decl_stmt|;
name|SampleElements
argument_list|<
name|E
argument_list|>
name|samples
init|=
name|delegate
operator|.
name|samples
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|E
argument_list|>
name|samplesList
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|samples
operator|.
name|e0
argument_list|()
argument_list|,
name|samples
operator|.
name|e1
argument_list|()
argument_list|,
name|samples
operator|.
name|e2
argument_list|()
argument_list|,
name|samples
operator|.
name|e3
argument_list|()
argument_list|,
name|samples
operator|.
name|e4
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|samplesList
argument_list|,
name|comparator
argument_list|)
expr_stmt|;
specifier|final
name|E
name|firstInclusive
init|=
name|samplesList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|final
name|E
name|lastInclusive
init|=
name|samplesList
operator|.
name|get
argument_list|(
name|samplesList
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
return|return
name|SortedMultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|ForwardingTestMultisetGenerator
argument_list|<
name|E
argument_list|>
argument_list|(
name|delegate
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|entries
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// we dangerously assume E is a string
name|List
argument_list|<
name|E
argument_list|>
name|extremeValues
init|=
operator|(
name|List
operator|)
name|getExtremeValues
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// map generators must past entry objects
name|List
argument_list|<
name|E
argument_list|>
name|normalValues
init|=
operator|(
name|List
operator|)
name|Arrays
operator|.
name|asList
argument_list|(
name|entries
argument_list|)
decl_stmt|;
comment|// prepare extreme values to be filtered out of view
name|Collections
operator|.
name|sort
argument_list|(
name|extremeValues
argument_list|,
name|comparator
argument_list|)
expr_stmt|;
name|E
name|firstExclusive
init|=
name|extremeValues
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|E
name|lastExclusive
init|=
name|extremeValues
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|from
operator|==
name|Bound
operator|.
name|NO_BOUND
condition|)
block|{
name|extremeValues
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|extremeValues
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|to
operator|==
name|Bound
operator|.
name|NO_BOUND
condition|)
block|{
name|extremeValues
operator|.
name|remove
argument_list|(
name|extremeValues
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|extremeValues
operator|.
name|remove
argument_list|(
name|extremeValues
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// the regular values should be visible after filtering
name|List
argument_list|<
name|E
argument_list|>
name|allEntries
init|=
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|()
decl_stmt|;
name|allEntries
operator|.
name|addAll
argument_list|(
name|extremeValues
argument_list|)
expr_stmt|;
name|allEntries
operator|.
name|addAll
argument_list|(
name|normalValues
argument_list|)
expr_stmt|;
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|multiset
init|=
operator|(
name|SortedMultiset
argument_list|<
name|E
argument_list|>
operator|)
name|delegate
operator|.
name|create
argument_list|(
name|allEntries
operator|.
name|toArray
argument_list|()
argument_list|)
decl_stmt|;
comment|// call the smallest subMap overload that filters out the extreme
comment|// values
if|if
condition|(
name|from
operator|==
name|Bound
operator|.
name|INCLUSIVE
condition|)
block|{
name|multiset
operator|=
name|multiset
operator|.
name|tailMultiset
argument_list|(
name|firstInclusive
argument_list|,
name|BoundType
operator|.
name|CLOSED
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|from
operator|==
name|Bound
operator|.
name|EXCLUSIVE
condition|)
block|{
name|multiset
operator|=
name|multiset
operator|.
name|tailMultiset
argument_list|(
name|firstExclusive
argument_list|,
name|BoundType
operator|.
name|OPEN
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|to
operator|==
name|Bound
operator|.
name|INCLUSIVE
condition|)
block|{
name|multiset
operator|=
name|multiset
operator|.
name|headMultiset
argument_list|(
name|lastInclusive
argument_list|,
name|BoundType
operator|.
name|CLOSED
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|to
operator|==
name|Bound
operator|.
name|EXCLUSIVE
condition|)
block|{
name|multiset
operator|=
name|multiset
operator|.
name|headMultiset
argument_list|(
name|lastExclusive
argument_list|,
name|BoundType
operator|.
name|OPEN
argument_list|)
expr_stmt|;
block|}
return|return
name|multiset
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
literal|" subMultiset "
operator|+
name|from
operator|+
literal|"-"
operator|+
name|to
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
comment|/**    * Returns an array of four bogus elements that will always be too high or too    * low for the display. This includes two values for each extreme.    *    *<p>    * This method (dangerously) assume that the strings {@code "!! a"} and    * {@code "~~ z"} will work for this purpose, which may cause problems for    * navigable maps with non-string or unicode generators.    */
DECL|method|getExtremeValues ()
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getExtremeValues
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
literal|"!! a"
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
literal|"!! b"
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
literal|"~~ y"
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
literal|"~~ z"
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|createDescendingSuite ( SortedMultisetTestSuiteBuilder<E> parentBuilder)
specifier|private
name|TestSuite
name|createDescendingSuite
parameter_list|(
name|SortedMultisetTestSuiteBuilder
argument_list|<
name|E
argument_list|>
name|parentBuilder
parameter_list|)
block|{
specifier|final
name|TestMultisetGenerator
argument_list|<
name|E
argument_list|>
name|delegate
init|=
operator|(
name|TestMultisetGenerator
argument_list|<
name|E
argument_list|>
operator|)
name|parentBuilder
operator|.
name|getSubjectGenerator
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|features
init|=
operator|new
name|HashSet
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
name|NoRecurse
operator|.
name|DESCENDING
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
if|if
condition|(
operator|!
name|features
operator|.
name|remove
argument_list|(
name|SERIALIZABLE_INCLUDING_VIEWS
argument_list|)
condition|)
block|{
name|features
operator|.
name|remove
argument_list|(
name|SERIALIZABLE
argument_list|)
expr_stmt|;
block|}
return|return
name|SortedMultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|ForwardingTestMultisetGenerator
argument_list|<
name|E
argument_list|>
argument_list|(
name|delegate
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|entries
parameter_list|)
block|{
return|return
operator|(
operator|(
name|SortedMultiset
argument_list|<
name|E
argument_list|>
operator|)
name|super
operator|.
name|create
argument_list|(
name|entries
argument_list|)
operator|)
operator|.
name|descendingMultiset
argument_list|()
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
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|super
operator|.
name|order
argument_list|(
name|insertionOrder
argument_list|)
argument_list|)
operator|.
name|reverse
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
DECL|method|createReserializedSuite ( SortedMultisetTestSuiteBuilder<E> parentBuilder)
specifier|private
name|TestSuite
name|createReserializedSuite
parameter_list|(
name|SortedMultisetTestSuiteBuilder
argument_list|<
name|E
argument_list|>
name|parentBuilder
parameter_list|)
block|{
specifier|final
name|TestMultisetGenerator
argument_list|<
name|E
argument_list|>
name|delegate
init|=
operator|(
name|TestMultisetGenerator
argument_list|<
name|E
argument_list|>
operator|)
name|parentBuilder
operator|.
name|getSubjectGenerator
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|features
init|=
operator|new
name|HashSet
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
name|addAll
argument_list|(
name|parentBuilder
operator|.
name|getFeatures
argument_list|()
argument_list|)
expr_stmt|;
name|features
operator|.
name|remove
argument_list|(
name|SERIALIZABLE
argument_list|)
expr_stmt|;
name|features
operator|.
name|remove
argument_list|(
name|SERIALIZABLE_INCLUDING_VIEWS
argument_list|)
expr_stmt|;
return|return
name|SortedMultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|ForwardingTestMultisetGenerator
argument_list|<
name|E
argument_list|>
argument_list|(
name|delegate
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|entries
parameter_list|)
block|{
return|return
name|SerializableTester
operator|.
name|reserialize
argument_list|(
operator|(
operator|(
name|SortedMultiset
argument_list|<
name|E
argument_list|>
operator|)
name|super
operator|.
name|create
argument_list|(
name|entries
argument_list|)
operator|)
argument_list|)
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
literal|" reserialized"
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
DECL|class|ForwardingTestMultisetGenerator
specifier|private
specifier|static
class|class
name|ForwardingTestMultisetGenerator
parameter_list|<
name|E
parameter_list|>
implements|implements
name|TestMultisetGenerator
argument_list|<
name|E
argument_list|>
block|{
DECL|field|delegate
specifier|private
specifier|final
name|TestMultisetGenerator
argument_list|<
name|E
argument_list|>
name|delegate
decl_stmt|;
DECL|method|ForwardingTestMultisetGenerator (TestMultisetGenerator<E> delegate)
name|ForwardingTestMultisetGenerator
parameter_list|(
name|TestMultisetGenerator
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
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
name|delegate
operator|.
name|samples
argument_list|()
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
name|delegate
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
name|Multiset
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
name|delegate
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

