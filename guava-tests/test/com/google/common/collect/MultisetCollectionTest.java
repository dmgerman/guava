begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
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
name|google
operator|.
name|AbstractMultisetSetCountTester
operator|.
name|getSetCountDuplicateInitializingMethods
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
name|google
operator|.
name|MultisetIteratorTester
operator|.
name|getIteratorDuplicateInitializingMethods
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
name|google
operator|.
name|MultisetReadsTester
operator|.
name|getReadsDuplicateInitializingMethods
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Collections
operator|.
name|sort
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
name|AnEnum
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
operator|.
name|NullsBeforeB
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
name|google
operator|.
name|MultisetTestSuiteBuilder
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
name|SortedMultisetTestSuiteBuilder
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
name|TestEnumMultisetGenerator
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
name|TestStringMultisetGenerator
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|List
import|;
end_import

begin_comment
comment|/**  * Collection tests for {@link Multiset} implementations.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
argument_list|(
literal|"suite"
argument_list|)
comment|// TODO(cpovirk): set up collect/gwt/suites version
DECL|class|MultisetCollectionTest
specifier|public
class|class
name|MultisetCollectionTest
extends|extends
name|TestCase
block|{
DECL|method|suite ()
specifier|public
specifier|static
name|Test
name|suite
parameter_list|()
block|{
name|TestSuite
name|suite
init|=
operator|new
name|TestSuite
argument_list|()
decl_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|MultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
name|hashMultisetGenerator
argument_list|()
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
argument_list|)
operator|.
name|named
argument_list|(
literal|"HashMultiset"
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|MultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
name|unmodifiableMultisetGenerator
argument_list|()
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_QUERIES
argument_list|)
operator|.
name|named
argument_list|(
literal|"UnmodifiableTreeMultiset"
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|SortedMultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringMultisetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
name|TreeMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|insertionOrder
parameter_list|)
block|{
return|return
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|sortedCopy
argument_list|(
name|insertionOrder
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|,
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_QUERIES
argument_list|)
operator|.
name|named
argument_list|(
literal|"TreeMultiset, Ordering.natural"
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|SortedMultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringMultisetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|result
init|=
name|TreeMultiset
operator|.
name|create
argument_list|(
name|NullsBeforeB
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
name|result
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|insertionOrder
parameter_list|)
block|{
name|sort
argument_list|(
name|insertionOrder
argument_list|,
name|NullsBeforeB
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
return|return
name|insertionOrder
return|;
block|}
block|}
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|,
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|)
operator|.
name|named
argument_list|(
literal|"TreeMultiset, NullsBeforeB"
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|MultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
name|forSetGenerator
argument_list|()
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionFeature
operator|.
name|SUPPORTS_REMOVE
argument_list|)
operator|.
name|suppressing
argument_list|(
name|getReadsDuplicateInitializingMethods
argument_list|()
argument_list|)
operator|.
name|suppressing
argument_list|(
name|getSetCountDuplicateInitializingMethods
argument_list|()
argument_list|)
operator|.
name|suppressing
argument_list|(
name|getIteratorDuplicateInitializingMethods
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"ForSetMultiset"
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|MultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
name|concurrentMultisetGenerator
argument_list|()
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_QUERIES
argument_list|)
operator|.
name|named
argument_list|(
literal|"ConcurrentHashMultiset"
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|MultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
name|enumMultisetGenerator
argument_list|()
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|,
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_QUERIES
argument_list|)
operator|.
name|named
argument_list|(
literal|"EnumMultiset"
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|MultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
name|unionGenerator
argument_list|()
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|)
operator|.
name|named
argument_list|(
literal|"UnionMultiset"
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|MultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
name|intersectionGenerator
argument_list|()
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|)
operator|.
name|named
argument_list|(
literal|"IntersectionMultiset"
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|MultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
name|sumGenerator
argument_list|()
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|)
operator|.
name|named
argument_list|(
literal|"SumMultiset"
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|MultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
name|differenceGenerator
argument_list|()
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|)
operator|.
name|named
argument_list|(
literal|"DifferenceMultiset"
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|SortedMultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
name|unmodifiableSortedMultisetGenerator
argument_list|()
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_QUERIES
argument_list|)
operator|.
name|named
argument_list|(
literal|"UnmodifiableSortedTreeMultiset"
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|method|hashMultisetGenerator ()
specifier|private
specifier|static
name|TestStringMultisetGenerator
name|hashMultisetGenerator
parameter_list|()
block|{
return|return
operator|new
name|TestStringMultisetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
name|HashMultiset
operator|.
name|create
argument_list|(
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|unmodifiableMultisetGenerator ()
specifier|private
specifier|static
name|TestStringMultisetGenerator
name|unmodifiableMultisetGenerator
parameter_list|()
block|{
return|return
operator|new
name|TestStringMultisetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
name|Multisets
operator|.
name|unmodifiableMultiset
argument_list|(
name|TreeMultiset
operator|.
name|create
argument_list|(
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|insertionOrder
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|insertionOrder
argument_list|)
expr_stmt|;
return|return
name|insertionOrder
return|;
block|}
block|}
return|;
block|}
DECL|method|unmodifiableSortedMultisetGenerator ()
specifier|private
specifier|static
name|TestStringMultisetGenerator
name|unmodifiableSortedMultisetGenerator
parameter_list|()
block|{
return|return
operator|new
name|TestStringMultisetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
name|Multisets
operator|.
name|unmodifiableSortedMultiset
argument_list|(
name|TreeMultiset
operator|.
name|create
argument_list|(
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|insertionOrder
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|insertionOrder
argument_list|)
expr_stmt|;
return|return
name|insertionOrder
return|;
block|}
block|}
return|;
block|}
DECL|method|forSetGenerator ()
specifier|private
specifier|static
name|TestStringMultisetGenerator
name|forSetGenerator
parameter_list|()
block|{
return|return
operator|new
name|TestStringMultisetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
name|Multisets
operator|.
name|forSet
argument_list|(
name|Sets
operator|.
name|newHashSet
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|concurrentMultisetGenerator ()
specifier|private
specifier|static
name|TestStringMultisetGenerator
name|concurrentMultisetGenerator
parameter_list|()
block|{
return|return
operator|new
name|TestStringMultisetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
name|ConcurrentHashMultiset
operator|.
name|create
argument_list|(
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|enumMultisetGenerator ()
specifier|private
specifier|static
name|TestEnumMultisetGenerator
name|enumMultisetGenerator
parameter_list|()
block|{
return|return
operator|new
name|TestEnumMultisetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|AnEnum
argument_list|>
name|create
parameter_list|(
name|AnEnum
index|[]
name|elements
parameter_list|)
block|{
return|return
operator|(
name|elements
operator|.
name|length
operator|==
literal|0
operator|)
condition|?
name|EnumMultiset
operator|.
name|create
argument_list|(
name|AnEnum
operator|.
name|class
argument_list|)
else|:
name|EnumMultiset
operator|.
name|create
argument_list|(
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|unionGenerator ()
specifier|private
specifier|static
name|TestStringMultisetGenerator
name|unionGenerator
parameter_list|()
block|{
return|return
operator|new
name|TestStringMultisetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset1
init|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset2
init|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|elements
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|element
init|=
name|elements
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|multiset1
operator|.
name|contains
argument_list|(
name|element
argument_list|)
operator|||
name|multiset2
operator|.
name|contains
argument_list|(
name|element
argument_list|)
condition|)
block|{
comment|// add to both; the one already containing it will have more
name|multiset1
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|multiset2
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|i
operator|%
literal|2
operator|==
literal|0
condition|)
block|{
name|multiset1
operator|.
name|add
argument_list|(
name|elements
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|multiset2
operator|.
name|add
argument_list|(
name|elements
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Multisets
operator|.
name|union
argument_list|(
name|multiset1
argument_list|,
name|multiset2
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|intersectionGenerator ()
specifier|private
specifier|static
name|TestStringMultisetGenerator
name|intersectionGenerator
parameter_list|()
block|{
return|return
operator|new
name|TestStringMultisetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset1
init|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset2
init|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|multiset1
operator|.
name|add
argument_list|(
literal|"only1"
argument_list|)
expr_stmt|;
name|multiset2
operator|.
name|add
argument_list|(
literal|"only2"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|elements
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|multiset1
operator|.
name|add
argument_list|(
name|elements
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|multiset2
operator|.
name|add
argument_list|(
name|elements
index|[
name|elements
operator|.
name|length
operator|-
literal|1
operator|-
name|i
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|elements
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|multiset1
operator|.
name|add
argument_list|(
name|elements
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|elements
operator|.
name|length
operator|>
literal|1
condition|)
block|{
comment|/*            * When a test requests a multiset with duplicates, our plan of            * "add an extra item 0 to A and an extra item 1 to B" really means            * "add an extra item 0 to A and B," which isn't what we want.            */
if|if
condition|(
operator|!
name|elements
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|elements
index|[
literal|1
index|]
argument_list|)
condition|)
block|{
name|multiset2
operator|.
name|add
argument_list|(
name|elements
index|[
literal|1
index|]
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Multisets
operator|.
name|intersection
argument_list|(
name|multiset1
argument_list|,
name|multiset2
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|sumGenerator ()
specifier|private
specifier|static
name|TestStringMultisetGenerator
name|sumGenerator
parameter_list|()
block|{
return|return
operator|new
name|TestStringMultisetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset1
init|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset2
init|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|elements
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
comment|// add to either; sum should contain all
if|if
condition|(
name|i
operator|%
literal|2
operator|==
literal|0
condition|)
block|{
name|multiset1
operator|.
name|add
argument_list|(
name|elements
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|multiset2
operator|.
name|add
argument_list|(
name|elements
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Multisets
operator|.
name|sum
argument_list|(
name|multiset1
argument_list|,
name|multiset2
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|differenceGenerator ()
specifier|private
specifier|static
name|TestStringMultisetGenerator
name|differenceGenerator
parameter_list|()
block|{
return|return
operator|new
name|TestStringMultisetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset1
init|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset2
init|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|multiset1
operator|.
name|add
argument_list|(
literal|"equalIn1"
argument_list|)
expr_stmt|;
name|multiset1
operator|.
name|add
argument_list|(
literal|"fewerIn1"
argument_list|)
expr_stmt|;
name|multiset2
operator|.
name|add
argument_list|(
literal|"equalIn1"
argument_list|)
expr_stmt|;
name|multiset2
operator|.
name|add
argument_list|(
literal|"fewerIn1"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|multiset2
operator|.
name|add
argument_list|(
literal|"onlyIn2"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|elements
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
comment|// add 1 more copy of each element to multiset1 than multiset2
name|multiset1
operator|.
name|add
argument_list|(
name|elements
index|[
name|i
index|]
argument_list|,
name|i
operator|+
literal|2
argument_list|)
expr_stmt|;
name|multiset2
operator|.
name|add
argument_list|(
name|elements
index|[
name|i
index|]
argument_list|,
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|Multisets
operator|.
name|difference
argument_list|(
name|multiset1
argument_list|,
name|multiset2
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

