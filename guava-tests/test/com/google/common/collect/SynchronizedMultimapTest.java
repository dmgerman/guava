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
import|import static
name|org
operator|.
name|truth0
operator|.
name|Truth
operator|.
name|ASSERT
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
name|collect
operator|.
name|testing
operator|.
name|google
operator|.
name|SetMultimapTestSuiteBuilder
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
name|TestStringSetMultimapGenerator
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
name|io
operator|.
name|Serializable
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
name|RandomAccess
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Tests for {@code Synchronized#multimap}.  *  * @author Mike Bostock  */
end_comment

begin_class
DECL|class|SynchronizedMultimapTest
specifier|public
class|class
name|SynchronizedMultimapTest
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
name|SynchronizedMultimapTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|SetMultimapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringSetMultimapGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|SetMultimap
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
name|TestMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inner
init|=
operator|new
name|TestMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|SetMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outer
init|=
name|Synchronized
operator|.
name|setMultimap
argument_list|(
name|inner
argument_list|,
name|inner
operator|.
name|mutex
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
name|outer
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
name|outer
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"Synchronized.setMultimap"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|MapFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionFeature
operator|.
name|SUPPORTS_ITERATOR_REMOVE
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_NULL_KEYS
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUES
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
DECL|class|TestMultimap
specifier|private
specifier|static
specifier|final
class|class
name|TestMultimap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|delegate
specifier|final
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
init|=
name|HashMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
DECL|field|mutex
specifier|public
specifier|final
name|Object
name|mutex
init|=
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|// something Serializable
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|equals (@ullable Object o)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|equals
argument_list|(
name|o
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|hashCode
argument_list|()
return|;
block|}
DECL|method|size ()
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|isEmpty ()
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|containsKey (@ullable Object key)
annotation|@
name|Override
specifier|public
name|boolean
name|containsKey
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|containsValue (@ullable Object value)
annotation|@
name|Override
specifier|public
name|boolean
name|containsValue
parameter_list|(
annotation|@
name|Nullable
name|Object
name|value
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|containsValue
argument_list|(
name|value
argument_list|)
return|;
block|}
DECL|method|containsEntry (@ullable Object key, @Nullable Object value)
annotation|@
name|Override
specifier|public
name|boolean
name|containsEntry
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|,
annotation|@
name|Nullable
name|Object
name|value
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|containsEntry
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|get (@ullable K key)
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|V
argument_list|>
name|get
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
comment|/* TODO: verify that the Collection is also synchronized? */
return|return
name|super
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|put (K key, V value)
annotation|@
name|Override
specifier|public
name|boolean
name|put
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|putAll (@ullable K key, Iterable<? extends V> values)
annotation|@
name|Override
specifier|public
name|boolean
name|putAll
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|,
name|Iterable
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|values
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|putAll
argument_list|(
name|key
argument_list|,
name|values
argument_list|)
return|;
block|}
DECL|method|putAll (Multimap<? extends K, ? extends V> map)
annotation|@
name|Override
specifier|public
name|boolean
name|putAll
parameter_list|(
name|Multimap
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
return|;
block|}
DECL|method|replaceValues (@ullable K key, Iterable<? extends V> values)
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|V
argument_list|>
name|replaceValues
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|,
name|Iterable
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|values
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|replaceValues
argument_list|(
name|key
argument_list|,
name|values
argument_list|)
return|;
block|}
DECL|method|remove (@ullable Object key, @Nullable Object value)
annotation|@
name|Override
specifier|public
name|boolean
name|remove
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|,
annotation|@
name|Nullable
name|Object
name|value
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|remove
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|removeAll (@ullable Object key)
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|V
argument_list|>
name|removeAll
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|removeAll
argument_list|(
name|key
argument_list|)
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
name|super
operator|.
name|clear
argument_list|()
expr_stmt|;
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
comment|/* TODO: verify that the Set is also synchronized? */
return|return
name|super
operator|.
name|keySet
argument_list|()
return|;
block|}
DECL|method|keys ()
annotation|@
name|Override
specifier|public
name|Multiset
argument_list|<
name|K
argument_list|>
name|keys
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
comment|/* TODO: verify that the Set is also synchronized? */
return|return
name|super
operator|.
name|keys
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
comment|/* TODO: verify that the Collection is also synchronized? */
return|return
name|super
operator|.
name|values
argument_list|()
return|;
block|}
DECL|method|entries ()
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entries
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
comment|/* TODO: verify that the Collection is also synchronized? */
return|return
name|super
operator|.
name|entries
argument_list|()
return|;
block|}
DECL|method|asMap ()
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|asMap
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
comment|/* TODO: verify that the Map is also synchronized? */
return|return
name|super
operator|.
name|asMap
argument_list|()
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
DECL|method|testSynchronizedListMultimap ()
specifier|public
name|void
name|testSynchronizedListMultimap
parameter_list|()
block|{
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|Multimaps
operator|.
name|synchronizedListMultimap
argument_list|(
name|ArrayListMultimap
operator|.
expr|<
name|String
argument_list|,
name|Integer
operator|>
name|create
argument_list|()
argument_list|)
decl_stmt|;
name|multimap
operator|.
name|putAll
argument_list|(
literal|"foo"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|3
argument_list|,
operator|-
literal|1
argument_list|,
literal|2
argument_list|,
literal|4
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|putAll
argument_list|(
literal|"bar"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|multimap
operator|.
name|removeAll
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|3
argument_list|,
operator|-
literal|1
argument_list|,
literal|2
argument_list|,
literal|4
argument_list|,
literal|1
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|multimap
operator|.
name|containsKey
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|multimap
operator|.
name|replaceValues
argument_list|(
literal|"bar"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|6
argument_list|,
literal|5
argument_list|)
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|6
argument_list|,
literal|5
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testSynchronizedSortedSetMultimap ()
specifier|public
name|void
name|testSynchronizedSortedSetMultimap
parameter_list|()
block|{
name|SortedSetMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|Multimaps
operator|.
name|synchronizedSortedSetMultimap
argument_list|(
name|TreeMultimap
operator|.
expr|<
name|String
argument_list|,
name|Integer
operator|>
name|create
argument_list|()
argument_list|)
decl_stmt|;
name|multimap
operator|.
name|putAll
argument_list|(
literal|"foo"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|3
argument_list|,
operator|-
literal|1
argument_list|,
literal|2
argument_list|,
literal|4
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|putAll
argument_list|(
literal|"bar"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|multimap
operator|.
name|removeAll
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
operator|-
literal|1
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|multimap
operator|.
name|containsKey
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|multimap
operator|.
name|replaceValues
argument_list|(
literal|"bar"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|6
argument_list|,
literal|5
argument_list|)
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|5
argument_list|,
literal|6
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testSynchronizedArrayListMultimapRandomAccess ()
specifier|public
name|void
name|testSynchronizedArrayListMultimapRandomAccess
parameter_list|()
block|{
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|delegate
init|=
name|ArrayListMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
name|delegate
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|Multimaps
operator|.
name|synchronizedListMultimap
argument_list|(
name|delegate
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
block|}
DECL|method|testSynchronizedLinkedListMultimapRandomAccess ()
specifier|public
name|void
name|testSynchronizedLinkedListMultimapRandomAccess
parameter_list|()
block|{
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|delegate
init|=
name|LinkedListMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
name|delegate
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|Multimaps
operator|.
name|synchronizedListMultimap
argument_list|(
name|delegate
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

