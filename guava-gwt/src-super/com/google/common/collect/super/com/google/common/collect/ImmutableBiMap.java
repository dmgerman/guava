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
name|collect
operator|.
name|CollectPreconditions
operator|.
name|checkEntryNotNull
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

begin_comment
comment|/**  * GWT emulation of {@link com.google.common.collect.ImmutableBiMap}.  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|ImmutableBiMap
specifier|public
specifier|abstract
class|class
name|ImmutableBiMap
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
name|BiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|toImmutableBiMap ( Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction)
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
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableBiMap
parameter_list|(
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
name|toImmutableBiMap
argument_list|(
name|keyFunction
argument_list|,
name|valueFunction
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
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|()
block|{
return|return
operator|(
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|RegularImmutableBiMap
operator|.
name|EMPTY
return|;
block|}
DECL|method|of (K k1, V v1)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
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
name|checkEntryNotNull
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
expr_stmt|;
return|return
operator|new
name|SingletonImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
return|;
block|}
DECL|method|of (K k1, V v1, K k2, V v2)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
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
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|,
name|k2
argument_list|,
name|v2
argument_list|)
argument_list|)
return|;
block|}
DECL|method|of (K k1, V v1, K k2, V v2, K k3, V v3)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
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
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|,
name|k2
argument_list|,
name|v2
argument_list|,
name|k3
argument_list|,
name|v3
argument_list|)
argument_list|)
return|;
block|}
DECL|method|of (K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
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
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|,
name|k2
argument_list|,
name|v2
argument_list|,
name|k3
argument_list|,
name|v3
argument_list|,
name|k4
argument_list|,
name|v4
argument_list|)
argument_list|)
return|;
block|}
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
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
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|,
name|k2
argument_list|,
name|v2
argument_list|,
name|k3
argument_list|,
name|v3
argument_list|,
name|k4
argument_list|,
name|v4
argument_list|,
name|k5
argument_list|,
name|v5
argument_list|)
argument_list|)
return|;
block|}
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
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
parameter_list|,
name|K
name|k6
parameter_list|,
name|V
name|v6
parameter_list|)
block|{
return|return
operator|new
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|,
name|k2
argument_list|,
name|v2
argument_list|,
name|k3
argument_list|,
name|v3
argument_list|,
name|k4
argument_list|,
name|v4
argument_list|,
name|k5
argument_list|,
name|v5
argument_list|,
name|k6
argument_list|,
name|v6
argument_list|)
argument_list|)
return|;
block|}
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
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
parameter_list|,
name|K
name|k6
parameter_list|,
name|V
name|v6
parameter_list|,
name|K
name|k7
parameter_list|,
name|V
name|v7
parameter_list|)
block|{
return|return
operator|new
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|,
name|k2
argument_list|,
name|v2
argument_list|,
name|k3
argument_list|,
name|v3
argument_list|,
name|k4
argument_list|,
name|v4
argument_list|,
name|k5
argument_list|,
name|v5
argument_list|,
name|k6
argument_list|,
name|v6
argument_list|,
name|k7
argument_list|,
name|v7
argument_list|)
argument_list|)
return|;
block|}
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
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
parameter_list|,
name|K
name|k6
parameter_list|,
name|V
name|v6
parameter_list|,
name|K
name|k7
parameter_list|,
name|V
name|v7
parameter_list|,
name|K
name|k8
parameter_list|,
name|V
name|v8
parameter_list|)
block|{
return|return
operator|new
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|,
name|k2
argument_list|,
name|v2
argument_list|,
name|k3
argument_list|,
name|v3
argument_list|,
name|k4
argument_list|,
name|v4
argument_list|,
name|k5
argument_list|,
name|v5
argument_list|,
name|k6
argument_list|,
name|v6
argument_list|,
name|k7
argument_list|,
name|v7
argument_list|,
name|k8
argument_list|,
name|v8
argument_list|)
argument_list|)
return|;
block|}
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
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
parameter_list|,
name|K
name|k6
parameter_list|,
name|V
name|v6
parameter_list|,
name|K
name|k7
parameter_list|,
name|V
name|v7
parameter_list|,
name|K
name|k8
parameter_list|,
name|V
name|v8
parameter_list|,
name|K
name|k9
parameter_list|,
name|V
name|v9
parameter_list|)
block|{
return|return
operator|new
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|,
name|k2
argument_list|,
name|v2
argument_list|,
name|k3
argument_list|,
name|v3
argument_list|,
name|k4
argument_list|,
name|v4
argument_list|,
name|k5
argument_list|,
name|v5
argument_list|,
name|k6
argument_list|,
name|v6
argument_list|,
name|k7
argument_list|,
name|v7
argument_list|,
name|k8
argument_list|,
name|v8
argument_list|,
name|k9
argument_list|,
name|v9
argument_list|)
argument_list|)
return|;
block|}
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
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
parameter_list|,
name|K
name|k6
parameter_list|,
name|V
name|v6
parameter_list|,
name|K
name|k7
parameter_list|,
name|V
name|v7
parameter_list|,
name|K
name|k8
parameter_list|,
name|V
name|v8
parameter_list|,
name|K
name|k9
parameter_list|,
name|V
name|v9
parameter_list|,
name|K
name|k10
parameter_list|,
name|V
name|v10
parameter_list|)
block|{
return|return
operator|new
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|,
name|k2
argument_list|,
name|v2
argument_list|,
name|k3
argument_list|,
name|v3
argument_list|,
name|k4
argument_list|,
name|v4
argument_list|,
name|k5
argument_list|,
name|v5
argument_list|,
name|k6
argument_list|,
name|v6
argument_list|,
name|k7
argument_list|,
name|v7
argument_list|,
name|k8
argument_list|,
name|v8
argument_list|,
name|k9
argument_list|,
name|v9
argument_list|,
name|k10
argument_list|,
name|v10
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|SafeVarargs
DECL|method|ofEntries (Entry<? extends K, ? extends V>.... entries)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|ofEntries
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
modifier|...
name|entries
parameter_list|)
block|{
return|return
operator|new
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|ImmutableMap
operator|.
name|ofEntries
argument_list|(
name|entries
argument_list|)
argument_list|)
return|;
block|}
DECL|method|builder ()
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
name|builder
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
argument_list|()
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
DECL|method|Builder ()
specifier|public
name|Builder
parameter_list|()
block|{}
DECL|method|Builder (int initCapacity)
name|Builder
parameter_list|(
name|int
name|initCapacity
parameter_list|)
block|{
name|super
argument_list|(
name|initCapacity
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
name|super
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
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
name|super
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
return|return
name|this
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
name|super
operator|.
name|putAll
argument_list|(
name|entries
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
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
name|super
operator|.
name|orderEntriesByValue
argument_list|(
name|valueComparator
argument_list|)
expr_stmt|;
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
DECL|method|build ()
specifier|public
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
block|{
return|return
name|buildOrThrow
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|buildOrThrow ()
specifier|public
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|buildOrThrow
parameter_list|()
block|{
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
init|=
name|super
operator|.
name|buildOrThrow
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|of
argument_list|()
return|;
block|}
return|return
operator|new
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|super
operator|.
name|buildOrThrow
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|buildJdkBacked ()
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|buildJdkBacked
parameter_list|()
block|{
return|return
name|build
argument_list|()
return|;
block|}
block|}
DECL|method|copyOf (Map<? extends K, ? extends V> map)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
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
if|if
condition|(
name|map
operator|instanceof
name|ImmutableBiMap
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// safe since map is not writable
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|bimap
init|=
operator|(
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|map
decl_stmt|;
return|return
name|bimap
return|;
block|}
if|if
condition|(
name|map
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|of
argument_list|()
return|;
block|}
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|immutableMap
init|=
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|map
argument_list|)
decl_stmt|;
return|return
operator|new
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|immutableMap
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
name|ImmutableBiMap
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
argument_list|()
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
DECL|method|ImmutableBiMap (Map<K, V> delegate)
name|ImmutableBiMap
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
DECL|method|inverse ()
specifier|public
specifier|abstract
name|ImmutableBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|values ()
specifier|public
name|ImmutableSet
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
return|return
name|inverse
argument_list|()
operator|.
name|keySet
argument_list|()
return|;
block|}
DECL|method|forcePut (K key, V value)
specifier|public
specifier|final
name|V
name|forcePut
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

