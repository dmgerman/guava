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
name|ImmutableSortedMap
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
name|Ordering
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
name|Sets
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
name|Map
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

begin_class
DECL|class|SafeTreeSetTest
specifier|public
class|class
name|SafeTreeSetTest
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
name|addTestSuite
argument_list|(
name|SafeTreeSetTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|NavigableSetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringSetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Set
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
operator|new
name|SafeTreeSet
argument_list|<>
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
name|Lists
operator|.
name|newArrayList
argument_list|(
name|Sets
operator|.
name|newTreeSet
argument_list|(
name|insertionOrder
argument_list|)
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
argument_list|)
operator|.
name|named
argument_list|(
literal|"SafeTreeSet with natural comparator"
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
name|SetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringSetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Set
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
name|NavigableSet
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|new
name|SafeTreeSet
argument_list|<>
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|nullsFirst
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|set
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|set
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
name|Lists
operator|.
name|newArrayList
argument_list|(
name|Sets
operator|.
name|newTreeSet
argument_list|(
name|insertionOrder
argument_list|)
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
name|ALLOWS_NULL_VALUES
argument_list|)
operator|.
name|named
argument_list|(
literal|"SafeTreeSet with null-friendly comparator"
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
annotation|@
name|GwtIncompatible
comment|// SerializableTester
DECL|method|testViewSerialization ()
specifier|public
name|void
name|testViewSerialization
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|ImmutableSortedMap
operator|.
name|of
argument_list|(
literal|"one"
argument_list|,
literal|1
argument_list|,
literal|"two"
argument_list|,
literal|2
argument_list|,
literal|"three"
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|map
operator|.
name|entrySet
argument_list|()
argument_list|)
expr_stmt|;
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|map
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
name|map
operator|.
name|values
argument_list|()
argument_list|)
argument_list|,
name|Lists
operator|.
name|newArrayList
argument_list|(
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|map
operator|.
name|values
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// SerializableTester
DECL|method|testEmpty_serialization ()
specifier|public
name|void
name|testEmpty_serialization
parameter_list|()
block|{
name|SortedSet
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|new
name|SafeTreeSet
argument_list|<>
argument_list|()
decl_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|copy
init|=
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|set
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|set
operator|.
name|comparator
argument_list|()
argument_list|,
name|copy
operator|.
name|comparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// SerializableTester
DECL|method|testSingle_serialization ()
specifier|public
name|void
name|testSingle_serialization
parameter_list|()
block|{
name|SortedSet
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|new
name|SafeTreeSet
argument_list|<>
argument_list|()
decl_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|"e"
argument_list|)
expr_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|copy
init|=
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|set
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|set
operator|.
name|comparator
argument_list|()
argument_list|,
name|copy
operator|.
name|comparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// SerializableTester
DECL|method|testSeveral_serialization ()
specifier|public
name|void
name|testSeveral_serialization
parameter_list|()
block|{
name|SortedSet
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|new
name|SafeTreeSet
argument_list|<>
argument_list|()
decl_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|copy
init|=
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|set
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|set
operator|.
name|comparator
argument_list|()
argument_list|,
name|copy
operator|.
name|comparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

