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
name|MapNavigationTester
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
name|NavigableMap
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
comment|/**  * Creates, based on your criteria, a JUnit test suite that exhaustively tests  * a NavigableMap implementation.  */
end_comment

begin_class
DECL|class|NavigableMapTestSuiteBuilder
specifier|public
class|class
name|NavigableMapTestSuiteBuilder
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|MapTestSuiteBuilder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|using ( TestMapGenerator<K, V> generator)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|NavigableMapTestSuiteBuilder
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
name|NavigableMapTestSuiteBuilder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|result
init|=
operator|new
name|NavigableMapTestSuiteBuilder
argument_list|<
name|K
argument_list|,
name|V
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
name|MapNavigationTester
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
DECL|method|createDerivedSuites (FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<K, V>, Entry<K, V>>> parentBuilder)
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
name|SUBMAP
argument_list|)
condition|)
block|{
name|derivedSuites
operator|.
name|add
argument_list|(
name|createSubmapSuite
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
name|createSubmapSuite
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
name|createSubmapSuite
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
name|createSubmapSuite
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
name|createSubmapSuite
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
name|createSubmapSuite
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
name|createSubmapSuite
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
name|createSubmapSuite
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
DECL|method|createDerivedKeySetSuite ( TestSetGenerator<K> keySetGenerator)
annotation|@
name|Override
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
name|NavigableSetTestSuiteBuilder
operator|.
name|using
argument_list|(
name|keySetGenerator
argument_list|)
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
DECL|enumConstant|SUBMAP
name|SUBMAP
block|,
DECL|enumConstant|DESCENDING
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
comment|/**    * Two bounds (from and to) define how to build a subMap.    */
DECL|enum|Bound
enum|enum
name|Bound
block|{
DECL|enumConstant|INCLUSIVE
name|INCLUSIVE
block|,
DECL|enumConstant|EXCLUSIVE
name|EXCLUSIVE
block|,
DECL|enumConstant|NO_BOUND
name|NO_BOUND
block|;   }
comment|/**    * Creates a suite whose map has some elements filtered out of view.    *    *<p>Because the map may be ascending or descending, this test must derive    * the relative order of these extreme values rather than relying on their    * regular sort ordering.    */
DECL|method|createSubmapSuite (final FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<K, V>, Entry<K, V>>> parentBuilder, final Bound from, final Bound to)
specifier|private
name|TestSuite
name|createSubmapSuite
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
name|TestMapGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
init|=
operator|(
name|TestMapGenerator
argument_list|<
name|K
argument_list|,
name|V
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
name|NoRecurse
operator|.
name|SUBMAP
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
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|emptyMap
init|=
operator|(
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
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
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entryComparator
init|=
name|Helpers
operator|.
name|entryComparator
argument_list|(
name|emptyMap
operator|.
name|comparator
argument_list|()
argument_list|)
decl_stmt|;
comment|// derive values for inclusive filtering from the input samples
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
comment|// no elements are inserted into the array
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
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
argument_list|,
name|samples
operator|.
name|e1
argument_list|,
name|samples
operator|.
name|e2
argument_list|,
name|samples
operator|.
name|e3
argument_list|,
name|samples
operator|.
name|e4
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|samplesList
argument_list|,
name|entryComparator
argument_list|)
expr_stmt|;
specifier|final
name|K
name|firstInclusive
init|=
name|samplesList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getKey
argument_list|()
decl_stmt|;
specifier|final
name|K
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
operator|.
name|getKey
argument_list|()
decl_stmt|;
return|return
name|NavigableMapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|ForwardingTestMapGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|delegate
argument_list|)
block|{
annotation|@
name|Override
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
name|entries
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// we dangerously assume K and V are both strings
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
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
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
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
name|entryComparator
argument_list|)
expr_stmt|;
name|K
name|firstExclusive
init|=
name|extremeValues
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|K
name|lastExclusive
init|=
name|extremeValues
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getKey
argument_list|()
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
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|allEntries
init|=
operator|new
name|ArrayList
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
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
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
init|=
operator|(
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|delegate
operator|.
name|create
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|allEntries
operator|.
name|toArray
argument_list|(
operator|new
name|Entry
index|[
name|allEntries
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
decl_stmt|;
comment|// call the smallest subMap overload that filters out the extreme values
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
name|EXCLUSIVE
condition|)
block|{
return|return
name|map
operator|.
name|headMap
argument_list|(
name|lastExclusive
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
name|map
operator|.
name|headMap
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
name|map
operator|.
name|tailMap
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
name|map
operator|.
name|subMap
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
name|map
operator|.
name|subMap
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
name|NO_BOUND
condition|)
block|{
return|return
name|map
operator|.
name|tailMap
argument_list|(
name|firstInclusive
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
name|EXCLUSIVE
condition|)
block|{
return|return
name|map
operator|.
name|subMap
argument_list|(
name|firstInclusive
argument_list|,
name|lastExclusive
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
name|map
operator|.
name|subMap
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
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
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
literal|" subMap "
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
comment|/**    * Returns an array of four bogus elements that will always be too high or    * too low for the display. This includes two values for each extreme.    *    *<p>This method (dangerously) assume that the strings {@code "!! a"} and    * {@code "~~ z"} will work for this purpose, which may cause problems for    * navigable maps with non-string or unicode generators.    */
DECL|method|getExtremeValues ()
specifier|private
name|List
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|getExtremeValues
parameter_list|()
block|{
name|List
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
literal|"!! a"
argument_list|,
literal|"below view"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
literal|"!! b"
argument_list|,
literal|"below view"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
literal|"~~ y"
argument_list|,
literal|"above view"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
literal|"~~ z"
argument_list|,
literal|"above view"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**    * Create a suite whose maps are descending views of other maps.    */
DECL|method|createDescendingSuite (final FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<K, V>, Entry<K, V>>> parentBuilder)
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
specifier|final
name|TestMapGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
init|=
operator|(
name|TestMapGenerator
argument_list|<
name|K
argument_list|,
name|V
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
return|return
name|NavigableMapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|ForwardingTestMapGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|delegate
argument_list|)
block|{
annotation|@
name|Override
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
name|entries
parameter_list|)
block|{
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
init|=
operator|(
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|delegate
operator|.
name|create
argument_list|(
name|entries
argument_list|)
decl_stmt|;
return|return
name|map
operator|.
name|descendingMap
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
DECL|class|ForwardingTestMapGenerator
specifier|static
class|class
name|ForwardingTestMapGenerator
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
DECL|field|delegate
specifier|private
name|TestMapGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
decl_stmt|;
DECL|method|ForwardingTestMapGenerator (TestMapGenerator<K, V> delegate)
name|ForwardingTestMapGenerator
parameter_list|(
name|TestMapGenerator
argument_list|<
name|K
argument_list|,
name|V
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
name|delegate
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
name|delegate
operator|.
name|createValueArray
argument_list|(
name|length
argument_list|)
return|;
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
name|delegate
operator|.
name|samples
argument_list|()
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
name|delegate
operator|.
name|create
argument_list|(
name|elements
argument_list|)
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
name|delegate
operator|.
name|createArray
argument_list|(
name|length
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

