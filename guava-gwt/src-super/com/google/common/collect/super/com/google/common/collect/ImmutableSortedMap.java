begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|Maps
operator|.
name|newTreeMap
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
name|singletonMap
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
name|unmodifiableSortedMap
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
name|Map
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BinaryOperator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collector
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_comment
comment|/**  * GWT emulated version of {@link com.google.common.collect.ImmutableSortedMap}. It's a thin wrapper  * around a {@link java.util.TreeMap}.  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|ImmutableSortedMap
specifier|public
specifier|final
class|class
name|ImmutableSortedMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|field|NATURAL_ORDER
specifier|static
specifier|final
name|Comparator
name|NATURAL_ORDER
init|=
name|Ordering
operator|.
name|natural
argument_list|()
decl_stmt|;
comment|// This reference is only used by GWT compiler to infer the keys and values
comment|// of the map that needs to be serialized.
DECL|field|unusedComparatorForSerialization
specifier|private
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|unusedComparatorForSerialization
decl_stmt|;
DECL|field|unusedKeyForSerialization
specifier|private
name|K
name|unusedKeyForSerialization
decl_stmt|;
DECL|field|unusedValueForSerialization
specifier|private
name|V
name|unusedValueForSerialization
decl_stmt|;
DECL|field|sortedDelegate
specifier|private
specifier|final
specifier|transient
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|sortedDelegate
decl_stmt|;
comment|// The comparator used by this map.  It's the same as that of sortedDelegate,
comment|// except that when sortedDelegate's comparator is null, it points to a
comment|// non-null instance of Ordering.natural().
comment|// (cpovirk: Is sortedDelegate's comparator really ever null?)
comment|// The comparator will likely also differ because of our nullAccepting hack.
comment|// See the bottom of the file for more information about it.
DECL|field|comparator
specifier|private
specifier|final
specifier|transient
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
decl_stmt|;
DECL|method|ImmutableSortedMap (SortedMap<K, V> delegate, Comparator<? super K> comparator)
name|ImmutableSortedMap
parameter_list|(
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|comparator
operator|=
name|comparator
expr_stmt|;
name|this
operator|.
name|sortedDelegate
operator|=
name|delegate
expr_stmt|;
block|}
DECL|method|toImmutableSortedMap ( Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|,
name|K
parameter_list|,
name|V
parameter_list|>
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableSortedMap
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|K
argument_list|>
name|keyFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
parameter_list|)
block|{
return|return
name|CollectCollectors
operator|.
name|toImmutableSortedMap
argument_list|(
name|comparator
argument_list|,
name|keyFunction
argument_list|,
name|valueFunction
argument_list|)
return|;
block|}
DECL|method|toImmutableSortedMap ( Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|,
name|K
parameter_list|,
name|V
parameter_list|>
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableSortedMap
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|K
argument_list|>
name|keyFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
parameter_list|,
name|BinaryOperator
argument_list|<
name|V
argument_list|>
name|mergeFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|keyFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|valueFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|mergeFunction
argument_list|)
expr_stmt|;
return|return
name|Collectors
operator|.
name|collectingAndThen
argument_list|(
name|Collectors
operator|.
name|toMap
argument_list|(
name|keyFunction
argument_list|,
name|valueFunction
argument_list|,
name|mergeFunction
argument_list|,
parameter_list|()
lambda|->
operator|new
name|TreeMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|comparator
argument_list|)
argument_list|)
argument_list|,
name|ImmutableSortedMap
operator|::
name|copyOfSorted
argument_list|)
return|;
block|}
comment|// Casting to any type is safe because the set will never hold any elements.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|of ()
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|()
block|{
return|return
operator|new
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|NATURAL_ORDER
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|of (K k1, V v1)
specifier|public
specifier|static
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|(
name|K
name|k1
parameter_list|,
name|V
name|v1
parameter_list|)
block|{
return|return
name|copyOf
argument_list|(
name|singletonMap
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
argument_list|)
return|;
block|}
DECL|method|of ( K k1, V v1, K k2, V v2)
specifier|public
specifier|static
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|(
name|K
name|k1
parameter_list|,
name|V
name|v1
parameter_list|,
name|K
name|k2
parameter_list|,
name|V
name|v2
parameter_list|)
block|{
return|return
operator|new
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
operator|.
name|put
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3)
specifier|public
specifier|static
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|(
name|K
name|k1
parameter_list|,
name|V
name|v1
parameter_list|,
name|K
name|k2
parameter_list|,
name|V
name|v2
parameter_list|,
name|K
name|k3
parameter_list|,
name|V
name|v3
parameter_list|)
block|{
return|return
operator|new
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
operator|.
name|put
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
operator|.
name|put
argument_list|(
name|k3
argument_list|,
name|v3
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4)
specifier|public
specifier|static
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|(
name|K
name|k1
parameter_list|,
name|V
name|v1
parameter_list|,
name|K
name|k2
parameter_list|,
name|V
name|v2
parameter_list|,
name|K
name|k3
parameter_list|,
name|V
name|v3
parameter_list|,
name|K
name|k4
parameter_list|,
name|V
name|v4
parameter_list|)
block|{
return|return
operator|new
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
operator|.
name|put
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
operator|.
name|put
argument_list|(
name|k3
argument_list|,
name|v3
argument_list|)
operator|.
name|put
argument_list|(
name|k4
argument_list|,
name|v4
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5)
specifier|public
specifier|static
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|(
name|K
name|k1
parameter_list|,
name|V
name|v1
parameter_list|,
name|K
name|k2
parameter_list|,
name|V
name|v2
parameter_list|,
name|K
name|k3
parameter_list|,
name|V
name|v3
parameter_list|,
name|K
name|k4
parameter_list|,
name|V
name|v4
parameter_list|,
name|K
name|k5
parameter_list|,
name|V
name|v5
parameter_list|)
block|{
return|return
operator|new
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
operator|.
name|put
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
operator|.
name|put
argument_list|(
name|k3
argument_list|,
name|v3
argument_list|)
operator|.
name|put
argument_list|(
name|k4
argument_list|,
name|v4
argument_list|)
operator|.
name|put
argument_list|(
name|k5
argument_list|,
name|v5
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|copyOf (Map<? extends K, ? extends V> map)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|copyOf
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
return|return
name|copyOfInternal
argument_list|(
operator|(
name|Map
operator|)
name|map
argument_list|,
operator|(
name|Ordering
argument_list|<
name|K
argument_list|>
operator|)
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
return|;
block|}
DECL|method|copyOf ( Map<? extends K, ? extends V> map, Comparator<? super K> comparator)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|copyOf
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
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
name|copyOfInternal
argument_list|(
name|map
argument_list|,
name|checkNotNull
argument_list|(
name|comparator
argument_list|)
argument_list|)
return|;
block|}
DECL|method|copyOf ( Iterable<? extends Entry<? extends K, ? extends V>> entries)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|copyOf
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
argument_list|>
name|entries
parameter_list|)
block|{
return|return
operator|new
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|NATURAL_ORDER
argument_list|)
operator|.
name|putAll
argument_list|(
name|entries
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|copyOf ( Iterable<? extends Entry<? extends K, ? extends V>> entries, Comparator<? super K> comparator)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|copyOf
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
argument_list|>
name|entries
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
operator|new
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|comparator
argument_list|)
operator|.
name|putAll
argument_list|(
name|entries
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|copyOfSorted (SortedMap<K, ? extends V> map)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|copyOfSorted
parameter_list|(
name|SortedMap
argument_list|<
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|map
parameter_list|)
block|{
comment|// If map has a null comparator, the keys should have a natural ordering,
comment|// even though K doesn't explicitly implement Comparable.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
init|=
operator|(
name|map
operator|.
name|comparator
argument_list|()
operator|==
literal|null
operator|)
condition|?
name|NATURAL_ORDER
else|:
name|map
operator|.
name|comparator
argument_list|()
decl_stmt|;
return|return
name|copyOfInternal
argument_list|(
name|map
argument_list|,
name|comparator
argument_list|)
return|;
block|}
DECL|method|copyOfInternal ( Map<? extends K, ? extends V> map, Comparator<? super K> comparator)
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|copyOfInternal
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
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
parameter_list|)
block|{
if|if
condition|(
name|map
operator|instanceof
name|ImmutableSortedMap
condition|)
block|{
comment|// TODO: Prove that this cast is safe, even though
comment|// Collections.unmodifiableSortedMap requires the same key type.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|kvMap
init|=
operator|(
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|map
decl_stmt|;
name|Comparator
argument_list|<
name|?
argument_list|>
name|comparator2
init|=
name|kvMap
operator|.
name|comparator
argument_list|()
decl_stmt|;
name|boolean
name|sameComparator
init|=
operator|(
name|comparator2
operator|==
literal|null
operator|)
condition|?
name|comparator
operator|==
name|NATURAL_ORDER
else|:
name|comparator
operator|.
name|equals
argument_list|(
name|comparator2
argument_list|)
decl_stmt|;
if|if
condition|(
name|sameComparator
condition|)
block|{
return|return
name|kvMap
return|;
block|}
block|}
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
init|=
name|newModifiableDelegate
argument_list|(
name|comparator
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|putEntryWithChecks
argument_list|(
name|delegate
argument_list|,
name|entry
argument_list|)
expr_stmt|;
block|}
return|return
name|newView
argument_list|(
name|unmodifiableSortedMap
argument_list|(
name|delegate
argument_list|)
argument_list|,
name|comparator
argument_list|)
return|;
block|}
DECL|method|putEntryWithChecks ( SortedMap<K, V> map, Entry<? extends K, ? extends V> entry)
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|void
name|putEntryWithChecks
parameter_list|(
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|,
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|entry
parameter_list|)
block|{
name|K
name|key
init|=
name|checkNotNull
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|V
name|value
init|=
name|checkNotNull
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
comment|// When a collision happens, the colliding entry is the first entry
comment|// of the tail map.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|previousEntry
init|=
name|map
operator|.
name|tailMap
argument_list|(
name|key
argument_list|)
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Duplicate keys in mappings "
operator|+
name|previousEntry
operator|.
name|getKey
argument_list|()
operator|+
literal|"="
operator|+
name|previousEntry
operator|.
name|getValue
argument_list|()
operator|+
literal|" and "
operator|+
name|key
operator|+
literal|"="
operator|+
name|value
argument_list|)
throw|;
block|}
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|naturalOrder ()
specifier|public
specifier|static
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|naturalOrder
parameter_list|()
block|{
return|return
operator|new
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
return|;
block|}
DECL|method|orderedBy (Comparator<K> comparator)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|orderedBy
parameter_list|(
name|Comparator
argument_list|<
name|K
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
operator|new
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|comparator
argument_list|)
return|;
block|}
DECL|method|reverseOrder ()
specifier|public
specifier|static
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|reverseOrder
parameter_list|()
block|{
return|return
operator|new
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|reverse
argument_list|()
argument_list|)
return|;
block|}
DECL|class|Builder
specifier|public
specifier|static
specifier|final
class|class
name|Builder
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|comparator
specifier|private
specifier|final
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
decl_stmt|;
DECL|method|Builder (Comparator<? super K> comparator)
specifier|public
name|Builder
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
parameter_list|)
block|{
name|this
operator|.
name|comparator
operator|=
name|checkNotNull
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|put (K key, V value)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|put
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|entries
operator|.
name|add
argument_list|(
name|entryOf
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|put (Entry<? extends K, ? extends V> entry)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|put
parameter_list|(
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|entry
parameter_list|)
block|{
name|super
operator|.
name|put
argument_list|(
name|entry
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|putAll (Map<? extends K, ? extends V> map)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
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
return|return
name|putAll
argument_list|(
name|map
operator|.
name|entrySet
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|putAll (Iterable<? extends Entry<? extends K, ? extends V>> entries)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|putAll
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
argument_list|>
name|entries
parameter_list|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|put
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|combine (Builder<K, V> other)
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|combine
parameter_list|(
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|other
parameter_list|)
block|{
name|super
operator|.
name|combine
argument_list|(
name|other
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|orderEntriesByValue (Comparator<? super V> valueComparator)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|orderEntriesByValue
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
name|valueComparator
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not available on ImmutableSortedMap.Builder"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|build ()
specifier|public
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
block|{
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
init|=
name|newModifiableDelegate
argument_list|(
name|comparator
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|putEntryWithChecks
argument_list|(
name|delegate
argument_list|,
name|entry
argument_list|)
expr_stmt|;
block|}
return|return
name|newView
argument_list|(
name|unmodifiableSortedMap
argument_list|(
name|delegate
argument_list|)
argument_list|,
name|comparator
argument_list|)
return|;
block|}
block|}
DECL|field|keySet
specifier|private
specifier|transient
name|ImmutableSortedSet
argument_list|<
name|K
argument_list|>
name|keySet
decl_stmt|;
annotation|@
name|Override
DECL|method|keySet ()
specifier|public
name|ImmutableSortedSet
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
name|ImmutableSortedSet
argument_list|<
name|K
argument_list|>
name|ks
init|=
name|keySet
decl_stmt|;
return|return
operator|(
name|ks
operator|==
literal|null
operator|)
condition|?
operator|(
name|keySet
operator|=
name|createKeySet
argument_list|()
operator|)
else|:
name|ks
return|;
block|}
annotation|@
name|Override
DECL|method|createKeySet ()
name|ImmutableSortedSet
argument_list|<
name|K
argument_list|>
name|createKeySet
parameter_list|()
block|{
comment|// the keySet() of the delegate is only a Set and TreeMap.navigatableKeySet
comment|// is not available in GWT yet.  To keep the code simple and code size more,
comment|// we make a copy here, instead of creating a view of it.
comment|//
comment|// TODO: revisit if it's unbearably slow or when GWT supports
comment|// TreeMap.navigatbleKeySet().
return|return
name|ImmutableSortedSet
operator|.
name|copyOf
argument_list|(
name|comparator
argument_list|,
name|sortedDelegate
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
DECL|method|comparator ()
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
parameter_list|()
block|{
return|return
name|comparator
return|;
block|}
DECL|method|firstKey ()
specifier|public
name|K
name|firstKey
parameter_list|()
block|{
return|return
name|sortedDelegate
operator|.
name|firstKey
argument_list|()
return|;
block|}
DECL|method|lastKey ()
specifier|public
name|K
name|lastKey
parameter_list|()
block|{
return|return
name|sortedDelegate
operator|.
name|lastKey
argument_list|()
return|;
block|}
DECL|method|higher (K k)
name|K
name|higher
parameter_list|(
name|K
name|k
parameter_list|)
block|{
name|Iterator
argument_list|<
name|K
argument_list|>
name|iterator
init|=
name|keySet
argument_list|()
operator|.
name|tailSet
argument_list|(
name|k
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|K
name|tmp
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|comparator
argument_list|()
operator|.
name|compare
argument_list|(
name|k
argument_list|,
name|tmp
argument_list|)
operator|<
literal|0
condition|)
block|{
return|return
name|tmp
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|headMap (K toKey)
specifier|public
name|ImmutableSortedMap
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
name|checkNotNull
argument_list|(
name|toKey
argument_list|)
expr_stmt|;
return|return
name|newView
argument_list|(
name|sortedDelegate
operator|.
name|headMap
argument_list|(
name|toKey
argument_list|)
argument_list|)
return|;
block|}
DECL|method|headMap (K toKey, boolean inclusive)
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|headMap
parameter_list|(
name|K
name|toKey
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|toKey
argument_list|)
expr_stmt|;
if|if
condition|(
name|inclusive
condition|)
block|{
name|K
name|tmp
init|=
name|higher
argument_list|(
name|toKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmp
operator|==
literal|null
condition|)
block|{
return|return
name|this
return|;
block|}
name|toKey
operator|=
name|tmp
expr_stmt|;
block|}
return|return
name|headMap
argument_list|(
name|toKey
argument_list|)
return|;
block|}
DECL|method|subMap (K fromKey, K toKey)
specifier|public
name|ImmutableSortedMap
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
name|checkNotNull
argument_list|(
name|fromKey
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|toKey
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|comparator
operator|.
name|compare
argument_list|(
name|fromKey
argument_list|,
name|toKey
argument_list|)
operator|<=
literal|0
argument_list|)
expr_stmt|;
return|return
name|newView
argument_list|(
name|sortedDelegate
operator|.
name|subMap
argument_list|(
name|fromKey
argument_list|,
name|toKey
argument_list|)
argument_list|)
return|;
block|}
DECL|method|subMap (K fromKey, boolean fromInclusive, K toKey, boolean toInclusive)
name|ImmutableSortedMap
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
name|boolean
name|fromInclusive
parameter_list|,
name|K
name|toKey
parameter_list|,
name|boolean
name|toInclusive
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|fromKey
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|toKey
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|comparator
operator|.
name|compare
argument_list|(
name|fromKey
argument_list|,
name|toKey
argument_list|)
operator|<=
literal|0
argument_list|)
expr_stmt|;
return|return
name|tailMap
argument_list|(
name|fromKey
argument_list|,
name|fromInclusive
argument_list|)
operator|.
name|headMap
argument_list|(
name|toKey
argument_list|,
name|toInclusive
argument_list|)
return|;
block|}
DECL|method|tailMap (K fromKey)
specifier|public
name|ImmutableSortedMap
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
name|checkNotNull
argument_list|(
name|fromKey
argument_list|)
expr_stmt|;
return|return
name|newView
argument_list|(
name|sortedDelegate
operator|.
name|tailMap
argument_list|(
name|fromKey
argument_list|)
argument_list|)
return|;
block|}
DECL|method|tailMap (K fromKey, boolean inclusive)
specifier|public
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|tailMap
parameter_list|(
name|K
name|fromKey
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|fromKey
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|inclusive
condition|)
block|{
name|fromKey
operator|=
name|higher
argument_list|(
name|fromKey
argument_list|)
expr_stmt|;
if|if
condition|(
name|fromKey
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|this
operator|.
name|comparator
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
return|return
name|tailMap
argument_list|(
name|fromKey
argument_list|)
return|;
block|}
DECL|method|newView (SortedMap<K, V> delegate)
specifier|private
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newView
parameter_list|(
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|)
block|{
return|return
name|newView
argument_list|(
name|delegate
argument_list|,
name|comparator
argument_list|)
return|;
block|}
DECL|method|newView ( SortedMap<K, V> delegate, Comparator<? super K> comparator)
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newView
parameter_list|(
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
operator|new
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|delegate
argument_list|,
name|comparator
argument_list|)
return|;
block|}
comment|/*    * We don't permit nulls, but we wrap every comparator with nullsFirst().    * Why? We want for queries like containsKey(null) to return false, but the    * GWT SortedMap implementation that we delegate to throws    * NullPointerException if the comparator does. Since our construction    * methods ensure that null is never present in the map, it's OK for the    * comparator to look for it wherever it wants.    *    * Note that we do NOT touch the comparator returned by comparator(), which    * should be identical to the one the user passed in. We touch only the    * "secret" comparator used by the delegate implementation.    */
DECL|method|newModifiableDelegate (Comparator<? super K> comparator)
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newModifiableDelegate
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
name|newTreeMap
argument_list|(
name|nullAccepting
argument_list|(
name|comparator
argument_list|)
argument_list|)
return|;
block|}
DECL|method|nullAccepting (Comparator<E> comparator)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Comparator
argument_list|<
name|E
argument_list|>
name|nullAccepting
parameter_list|(
name|Comparator
argument_list|<
name|E
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
name|Ordering
operator|.
name|from
argument_list|(
name|comparator
argument_list|)
operator|.
name|nullsFirst
argument_list|()
return|;
block|}
block|}
end_class

end_unit

