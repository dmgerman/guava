begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Maps
operator|.
name|immutableEntry
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
name|NavigableMapTestSuiteBuilder
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
name|TestStringSortedMapGenerator
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
name|Iterator
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
name|SortedMap
import|;
end_import

begin_comment
comment|/**  * Tests for {@code ForwardingNavigableMap}.  *  * @author Robert Konigsberg  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|ForwardingNavigableMapTest
specifier|public
class|class
name|ForwardingNavigableMapTest
extends|extends
name|ForwardingSortedMapTest
block|{
DECL|class|StandardImplForwardingNavigableMap
specifier|static
class|class
name|StandardImplForwardingNavigableMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingNavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|backingMap
specifier|private
specifier|final
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|backingMap
decl_stmt|;
DECL|method|StandardImplForwardingNavigableMap (NavigableMap<K, V> backingMap)
name|StandardImplForwardingNavigableMap
parameter_list|(
name|NavigableMap
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
name|NavigableMap
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
comment|/*        * We can't use StandardKeySet, as NavigableMapTestSuiteBuilder assumes that our keySet is a        * NavigableSet. We test StandardKeySet in the superclass, so it's still covered.        */
return|return
name|navigableKeySet
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
annotation|@
name|Override
DECL|method|lowerEntry (K key)
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|lowerEntry
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|standardLowerEntry
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|lowerKey (K key)
specifier|public
name|K
name|lowerKey
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|standardLowerKey
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|floorEntry (K key)
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|floorEntry
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|standardFloorEntry
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|floorKey (K key)
specifier|public
name|K
name|floorKey
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|standardFloorKey
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|ceilingEntry (K key)
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|ceilingEntry
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|standardCeilingEntry
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|ceilingKey (K key)
specifier|public
name|K
name|ceilingKey
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|standardCeilingKey
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|higherEntry (K key)
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|higherEntry
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|standardHigherEntry
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|higherKey (K key)
specifier|public
name|K
name|higherKey
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|standardHigherKey
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|firstEntry ()
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|firstEntry
parameter_list|()
block|{
return|return
name|standardFirstEntry
argument_list|()
return|;
block|}
comment|/*      * We can't override lastEntry to delegate to standardLastEntry, as it would create an infinite      * loop. Instead, we test standardLastEntry manually below.      */
annotation|@
name|Override
DECL|method|pollFirstEntry ()
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|pollFirstEntry
parameter_list|()
block|{
return|return
name|standardPollFirstEntry
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|pollLastEntry ()
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|pollLastEntry
parameter_list|()
block|{
return|return
name|standardPollLastEntry
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|descendingMap ()
specifier|public
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|descendingMap
parameter_list|()
block|{
return|return
operator|new
name|StandardDescendingMap
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|navigableKeySet ()
specifier|public
name|NavigableSet
argument_list|<
name|K
argument_list|>
name|navigableKeySet
parameter_list|()
block|{
return|return
operator|new
name|StandardNavigableKeySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|descendingKeySet ()
specifier|public
name|NavigableSet
argument_list|<
name|K
argument_list|>
name|descendingKeySet
parameter_list|()
block|{
return|return
name|standardDescendingKeySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|firstKey ()
specifier|public
name|K
name|firstKey
parameter_list|()
block|{
return|return
name|standardFirstKey
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|headMap (K toKey)
specifier|public
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|headMap
parameter_list|(
name|K
name|toKey
parameter_list|)
block|{
return|return
name|standardHeadMap
argument_list|(
name|toKey
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|lastKey ()
specifier|public
name|K
name|lastKey
parameter_list|()
block|{
return|return
name|standardLastKey
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|tailMap (K fromKey)
specifier|public
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|tailMap
parameter_list|(
name|K
name|fromKey
parameter_list|)
block|{
return|return
name|standardTailMap
argument_list|(
name|fromKey
argument_list|)
return|;
block|}
block|}
DECL|class|StandardLastEntryForwardingNavigableMap
specifier|static
class|class
name|StandardLastEntryForwardingNavigableMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingNavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|backingMap
specifier|private
specifier|final
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|backingMap
decl_stmt|;
DECL|method|StandardLastEntryForwardingNavigableMap (NavigableMap<K, V> backingMap)
name|StandardLastEntryForwardingNavigableMap
parameter_list|(
name|NavigableMap
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
name|NavigableMap
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
annotation|@
name|Override
DECL|method|lastEntry ()
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|lastEntry
parameter_list|()
block|{
return|return
name|standardLastEntry
argument_list|()
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
name|ForwardingNavigableMapTest
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
name|SortedMap
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
name|StandardImplForwardingNavigableMap
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
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"ForwardingNavigableMap[SafeTreeMap] with no comparator and standard "
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
name|CollectionFeature
operator|.
name|SUPPORTS_ITERATOR_REMOVE
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
comment|// TODO(user): add forwarding-to-ImmutableSortedMap test
return|return
name|suite
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
name|NavigableMap
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|sortedMap
init|=
name|createProxyInstance
argument_list|(
name|NavigableMap
operator|.
name|class
argument_list|)
decl_stmt|;
name|forward
operator|=
operator|new
name|ForwardingNavigableMap
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
name|NavigableMap
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
DECL|method|testStandardLastEntry ()
specifier|public
name|void
name|testStandardLastEntry
parameter_list|()
block|{
name|NavigableMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|forwarding
init|=
operator|new
name|StandardLastEntryForwardingNavigableMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|(
operator|new
name|SafeTreeMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|forwarding
operator|.
name|lastEntry
argument_list|()
argument_list|)
expr_stmt|;
name|forwarding
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|immutableEntry
argument_list|(
literal|"b"
argument_list|,
literal|2
argument_list|)
argument_list|,
name|forwarding
operator|.
name|lastEntry
argument_list|()
argument_list|)
expr_stmt|;
name|forwarding
operator|.
name|put
argument_list|(
literal|"c"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|immutableEntry
argument_list|(
literal|"c"
argument_list|,
literal|3
argument_list|)
argument_list|,
name|forwarding
operator|.
name|lastEntry
argument_list|()
argument_list|)
expr_stmt|;
name|forwarding
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|immutableEntry
argument_list|(
literal|"c"
argument_list|,
literal|3
argument_list|)
argument_list|,
name|forwarding
operator|.
name|lastEntry
argument_list|()
argument_list|)
expr_stmt|;
name|forwarding
operator|.
name|remove
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|immutableEntry
argument_list|(
literal|"b"
argument_list|,
literal|2
argument_list|)
argument_list|,
name|forwarding
operator|.
name|lastEntry
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLowerEntry ()
specifier|public
name|void
name|testLowerEntry
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|lowerEntry
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[lowerEntry(Object)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLowerKey ()
specifier|public
name|void
name|testLowerKey
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|lowerKey
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[lowerKey(Object)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testFloorEntry ()
specifier|public
name|void
name|testFloorEntry
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|floorEntry
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[floorEntry(Object)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testFloorKey ()
specifier|public
name|void
name|testFloorKey
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|floorKey
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[floorKey(Object)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCeilingEntry ()
specifier|public
name|void
name|testCeilingEntry
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|ceilingEntry
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[ceilingEntry(Object)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCeilingKey ()
specifier|public
name|void
name|testCeilingKey
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|ceilingKey
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[ceilingKey(Object)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testHigherEntry ()
specifier|public
name|void
name|testHigherEntry
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|higherEntry
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[higherEntry(Object)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testHigherKey ()
specifier|public
name|void
name|testHigherKey
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|higherKey
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[higherKey(Object)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testPollFirstEntry ()
specifier|public
name|void
name|testPollFirstEntry
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|pollFirstEntry
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[pollFirstEntry]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testPollLastEntry ()
specifier|public
name|void
name|testPollLastEntry
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|pollLastEntry
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[pollLastEntry]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testFirstEntry ()
specifier|public
name|void
name|testFirstEntry
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|firstEntry
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[firstEntry]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLastEntry ()
specifier|public
name|void
name|testLastEntry
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|lastEntry
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[lastEntry]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDescendingMap ()
specifier|public
name|void
name|testDescendingMap
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|descendingMap
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[descendingMap]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testNavigableKeySet ()
specifier|public
name|void
name|testNavigableKeySet
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|navigableKeySet
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[navigableKeySet]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDescendingKeySet ()
specifier|public
name|void
name|testDescendingKeySet
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|descendingKeySet
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[descendingKeySet]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSubMap_K_Bool_K_Bool ()
specifier|public
name|void
name|testSubMap_K_Bool_K_Bool
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|subMap
argument_list|(
literal|"a"
argument_list|,
literal|false
argument_list|,
literal|"b"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[subMap(Object,boolean,Object,boolean)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testHeadMap_K_Bool ()
specifier|public
name|void
name|testHeadMap_K_Bool
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|headMap
argument_list|(
literal|"a"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[headMap(Object,boolean)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTailMap_K_Bool ()
specifier|public
name|void
name|testTailMap_K_Bool
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|tailMap
argument_list|(
literal|"a"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[tailMap(Object,boolean)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|forward ()
annotation|@
name|Override
name|NavigableMap
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
name|NavigableMap
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

