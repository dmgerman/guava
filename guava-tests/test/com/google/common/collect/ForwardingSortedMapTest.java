begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|SafeTreeMap
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
name|TestStringMapGenerator
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
name|TestSuite
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
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_comment
comment|/**  * Tests for {@code ForwardingSortedMap}.  *  * @author Robert Konigsberg  */
end_comment

begin_class
DECL|class|ForwardingSortedMapTest
specifier|public
class|class
name|ForwardingSortedMapTest
extends|extends
name|ForwardingMapTest
block|{
DECL|class|StandardImplForwardingSortedMap
specifier|static
class|class
name|StandardImplForwardingSortedMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|backingMap
specifier|private
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|backingMap
decl_stmt|;
DECL|method|StandardImplForwardingSortedMap (SortedMap<K, V> backingMap)
name|StandardImplForwardingSortedMap
parameter_list|(
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|backingMap
parameter_list|)
block|{
name|this
operator|.
name|backingMap
operator|=
name|backingMap
expr_stmt|;
block|}
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|backingMap
return|;
block|}
DECL|method|containsKey (Object key)
annotation|@
name|Override
specifier|public
name|boolean
name|containsKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|standardContainsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|containsValue (Object value)
annotation|@
name|Override
specifier|public
name|boolean
name|containsValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|standardContainsValue
argument_list|(
name|value
argument_list|)
return|;
block|}
DECL|method|putAll (Map<? extends K, ? extends V> map)
annotation|@
name|Override
specifier|public
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|map
parameter_list|)
block|{
name|standardPutAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
DECL|method|remove (Object object)
annotation|@
name|Override
specifier|public
name|V
name|remove
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|standardRemove
argument_list|(
name|object
argument_list|)
return|;
block|}
DECL|method|equals (Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|standardEquals
argument_list|(
name|object
argument_list|)
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|standardHashCode
argument_list|()
return|;
block|}
DECL|method|keySet ()
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
operator|new
name|StandardKeySet
argument_list|()
return|;
block|}
DECL|method|values ()
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
return|return
operator|new
name|StandardValues
argument_list|()
return|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|standardToString
argument_list|()
return|;
block|}
DECL|method|entrySet ()
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
operator|new
name|StandardEntrySet
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|backingMap
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|clear ()
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|standardClear
argument_list|()
expr_stmt|;
block|}
DECL|method|isEmpty ()
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|standardIsEmpty
argument_list|()
return|;
block|}
DECL|method|subMap (K fromKey, K toKey)
annotation|@
name|Override
specifier|public
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|subMap
parameter_list|(
name|K
name|fromKey
parameter_list|,
name|K
name|toKey
parameter_list|)
block|{
return|return
name|standardSubMap
argument_list|(
name|fromKey
argument_list|,
name|toKey
argument_list|)
return|;
block|}
block|}
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
name|ForwardingSortedMapTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|MapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringMapGenerator
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
name|SortedMap
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
argument_list|()
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
operator|new
name|StandardImplForwardingSortedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|map
argument_list|)
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
return|return
name|sort
argument_list|(
name|insertionOrder
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"ForwardingSortedMap[SafeTreeMap] with no comparator and standard "
operator|+
literal|"implementations"
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
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|MapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringMapGenerator
argument_list|()
block|{
specifier|private
specifier|final
name|Comparator
argument_list|<
name|String
argument_list|>
name|comparator
init|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|nullsFirst
argument_list|()
decl_stmt|;
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
name|SortedMap
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
name|comparator
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
operator|new
name|StandardImplForwardingSortedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|map
argument_list|)
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
return|return
name|sort
argument_list|(
name|insertionOrder
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"ForwardingSortedMap[SafeTreeMap] with natural comparator and "
operator|+
literal|"standard implementations"
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
name|ALLOWS_NULL_KEYS
argument_list|,
name|MapFeature
operator|.
name|GENERAL_PURPOSE
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
name|MapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringMapGenerator
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
name|ImmutableSortedMap
operator|.
name|Builder
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|builder
init|=
name|ImmutableSortedMap
operator|.
name|naturalOrder
argument_list|()
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
name|builder
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
operator|new
name|StandardImplForwardingSortedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|)
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
return|return
name|sort
argument_list|(
name|insertionOrder
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"ForwardingSortedMap[ImmutableSortedMap] with standard "
operator|+
literal|"implementations"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|MapFeature
operator|.
name|REJECTS_DUPLICATES_AT_CREATION
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_NULL_QUERIES
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
DECL|method|sort ( List<Entry<String, String>> entries)
specifier|private
specifier|static
name|Iterable
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|sort
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
name|entries
parameter_list|)
block|{
name|SortedMap
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
operator|.
name|nullsFirst
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
operator|.
name|entrySet
argument_list|()
return|;
block|}
DECL|method|setUp ()
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
comment|/*      * Class parameters must be raw, so we can't create a proxy with generic      * type arguments. The created proxy only records calls and returns null, so      * the type is irrelevant at runtime.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|sortedMap
init|=
name|createProxyInstance
argument_list|(
name|SortedMap
operator|.
name|class
argument_list|)
decl_stmt|;
name|forward
operator|=
operator|new
name|ForwardingSortedMap
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|sortedMap
return|;
block|}
block|}
expr_stmt|;
block|}
DECL|method|testComparator ()
specifier|public
name|void
name|testComparator
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|comparator
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[comparator]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testFirstKey ()
specifier|public
name|void
name|testFirstKey
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|firstKey
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[firstKey]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testHeadMap_K ()
specifier|public
name|void
name|testHeadMap_K
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|headMap
argument_list|(
literal|"asdf"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[headMap(Object)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLastKey ()
specifier|public
name|void
name|testLastKey
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|lastKey
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[lastKey]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSubMap_K_K ()
specifier|public
name|void
name|testSubMap_K_K
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|subMap
argument_list|(
literal|"first"
argument_list|,
literal|"last"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[subMap(Object,Object)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTailMap_K ()
specifier|public
name|void
name|testTailMap_K
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|tailMap
argument_list|(
literal|"last"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[tailMap(Object)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|forward ()
annotation|@
name|Override
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|forward
parameter_list|()
block|{
return|return
operator|(
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
operator|)
name|super
operator|.
name|forward
argument_list|()
return|;
block|}
block|}
end_class

end_unit

