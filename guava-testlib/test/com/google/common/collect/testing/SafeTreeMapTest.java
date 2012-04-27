begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|testing
operator|.
name|Helpers
operator|.
name|NullsBeforeTwo
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
name|SortedMap
import|;
end_import

begin_comment
comment|/**  * Tests for SafeTreeMap.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|SafeTreeMapTest
specifier|public
class|class
name|SafeTreeMapTest
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
name|SafeTreeMapTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|NavigableMapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringSortedMapGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|create
parameter_list|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
index|[]
name|entries
parameter_list|)
block|{
name|NavigableMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|SafeTreeMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|map
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
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|,
name|MapFeature
operator|.
name|GENERAL_PURPOSE
argument_list|)
operator|.
name|named
argument_list|(
literal|"SafeTreeMap with natural comparator"
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
name|NavigableMapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringSortedMapGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|create
parameter_list|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
index|[]
name|entries
parameter_list|)
block|{
name|NavigableMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|SafeTreeMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|NullsBeforeTwo
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
annotation|@
name|Override
specifier|public
name|Iterable
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|insertionOrder
parameter_list|)
block|{
name|sort
argument_list|(
name|insertionOrder
argument_list|,
name|Helpers
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|entryComparator
argument_list|(
name|NullsBeforeTwo
operator|.
name|INSTANCE
argument_list|)
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
name|MapFeature
operator|.
name|ALLOWS_NULL_KEYS
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|,
name|MapFeature
operator|.
name|GENERAL_PURPOSE
argument_list|)
operator|.
name|named
argument_list|(
literal|"SafeTreeMap with null-friendly comparator"
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
argument_list|(
literal|"SerializableTester"
argument_list|)
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
argument_list|(
literal|"SerializableTester"
argument_list|)
DECL|class|ReserializedMapTests
specifier|public
specifier|static
class|class
name|ReserializedMapTests
extends|extends
name|SortedMapInterfaceTest
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
block|{
DECL|method|ReserializedMapTests ()
name|ReserializedMapTests
parameter_list|()
block|{
name|super
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|makePopulatedMap ()
annotation|@
name|Override
specifier|protected
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|makePopulatedMap
parameter_list|()
block|{
name|NavigableMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|SafeTreeMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"one"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"two"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"three"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
return|return
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|map
argument_list|)
return|;
block|}
DECL|method|makeEmptyMap ()
annotation|@
name|Override
specifier|protected
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|makeEmptyMap
parameter_list|()
throws|throws
name|UnsupportedOperationException
block|{
name|NavigableMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|SafeTreeMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
return|return
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|map
argument_list|)
return|;
block|}
DECL|method|getKeyNotInPopulatedMap ()
annotation|@
name|Override
specifier|protected
name|String
name|getKeyNotInPopulatedMap
parameter_list|()
block|{
return|return
literal|"minus one"
return|;
block|}
DECL|method|getValueNotInPopulatedMap ()
annotation|@
name|Override
specifier|protected
name|Integer
name|getValueNotInPopulatedMap
parameter_list|()
block|{
return|return
operator|-
literal|1
return|;
block|}
block|}
block|}
end_class

end_unit

