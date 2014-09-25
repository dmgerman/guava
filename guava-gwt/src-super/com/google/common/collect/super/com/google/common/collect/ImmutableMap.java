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
name|CollectPreconditions
operator|.
name|checkEntryNotNull
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
name|Iterables
operator|.
name|getOnlyElement
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumMap
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * GWT emulation of {@link ImmutableMap}.  For non sorted maps, it is a thin  * wrapper around {@link java.util.Collections#emptyMap()}, {@link  * Collections#singletonMap(Object, Object)} and {@link java.util.LinkedHashMap}  * for empty, singleton and regular maps respectively.  For sorted maps, it's  * a thin wrapper around {@link java.util.TreeMap}.  *  * @see ImmutableSortedMap  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|ImmutableMap
specifier|public
specifier|abstract
class|class
name|ImmutableMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|,
name|Serializable
block|{
DECL|class|IteratorBasedImmutableMap
specifier|abstract
specifier|static
class|class
name|IteratorBasedImmutableMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|entryIterator ()
specifier|abstract
name|UnmodifiableIterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entryIterator
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|createEntrySet ()
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|createEntrySet
parameter_list|()
block|{
return|return
operator|new
name|ImmutableMapEntrySet
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|()
block|{
return|return
name|IteratorBasedImmutableMap
operator|.
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|UnmodifiableIterator
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
name|entryIterator
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
DECL|method|ImmutableMap ()
name|ImmutableMap
parameter_list|()
block|{}
DECL|method|of ()
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|()
block|{
return|return
name|ImmutableBiMap
operator|.
name|of
argument_list|()
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
name|ImmutableMap
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
name|ImmutableBiMap
operator|.
name|of
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
name|ImmutableMap
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
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|entryOf
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
argument_list|)
return|;
block|}
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMap
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
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|entryOf
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k3
argument_list|,
name|v3
argument_list|)
argument_list|)
return|;
block|}
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMap
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
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|entryOf
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k3
argument_list|,
name|v3
argument_list|)
argument_list|,
name|entryOf
argument_list|(
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
name|ImmutableMap
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
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|entryOf
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k3
argument_list|,
name|v3
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k4
argument_list|,
name|v4
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k5
argument_list|,
name|v5
argument_list|)
argument_list|)
return|;
block|}
comment|// looking for of() with> 5 entries? Use the builder instead.
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
DECL|method|entryOf (K key, V value)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entryOf
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|checkEntryNotNull
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|class|Builder
specifier|public
specifier|static
class|class
name|Builder
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
block|{
DECL|field|entries
specifier|final
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entries
decl_stmt|;
DECL|method|Builder ()
specifier|public
name|Builder
parameter_list|()
block|{
name|this
operator|.
name|entries
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|()
expr_stmt|;
block|}
DECL|method|Builder (int initCapacity)
name|Builder
parameter_list|(
name|int
name|initCapacity
parameter_list|)
block|{
name|this
operator|.
name|entries
operator|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|initCapacity
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|entry
operator|instanceof
name|ImmutableEntry
condition|)
block|{
name|checkNotNull
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// all supported methods are covariant
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|immutableEntry
init|=
operator|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|entry
decl_stmt|;
name|entries
operator|.
name|add
argument_list|(
name|immutableEntry
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|entries
operator|.
name|add
argument_list|(
name|entryOf
argument_list|(
operator|(
name|K
operator|)
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
operator|(
name|V
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
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
name|this
return|;
block|}
DECL|method|build ()
specifier|public
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
block|{
return|return
name|fromEntryList
argument_list|(
name|entries
argument_list|)
return|;
block|}
DECL|method|fromEntryList ( List<Entry<K, V>> entries)
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|fromEntryList
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
name|entries
parameter_list|)
block|{
name|int
name|size
init|=
name|entries
operator|.
name|size
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|size
condition|)
block|{
case|case
literal|0
case|:
return|return
name|of
argument_list|()
return|;
case|case
literal|1
case|:
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|getOnlyElement
argument_list|(
name|entries
argument_list|)
decl_stmt|;
return|return
name|of
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
return|;
default|default:
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|entryArray
init|=
name|entries
operator|.
name|toArray
argument_list|(
operator|new
name|Entry
index|[
name|entries
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
return|return
operator|new
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|entryArray
argument_list|)
return|;
block|}
block|}
block|}
DECL|method|copyOf ( Map<? extends K, ? extends V> map)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMap
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
operator|(
name|map
operator|instanceof
name|ImmutableMap
operator|)
operator|&&
operator|!
operator|(
name|map
operator|instanceof
name|ImmutableSortedMap
operator|)
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// safe since map is not writable
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|kvMap
init|=
operator|(
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|map
decl_stmt|;
return|return
name|kvMap
return|;
block|}
elseif|else
if|if
condition|(
name|map
operator|instanceof
name|EnumMap
condition|)
block|{
name|EnumMap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|enumMap
init|=
operator|(
name|EnumMap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|map
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|entry
range|:
name|enumMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|checkNotNull
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// immutable collections are safe for covariant casts
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|result
init|=
name|ImmutableEnumMap
operator|.
name|asImmutable
argument_list|(
operator|new
name|EnumMap
argument_list|(
name|enumMap
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|result
return|;
block|}
name|int
name|size
init|=
name|map
operator|.
name|size
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|size
condition|)
block|{
case|case
literal|0
case|:
return|return
name|of
argument_list|()
return|;
case|case
literal|1
case|:
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
init|=
name|getOnlyElement
argument_list|(
name|map
operator|.
name|entrySet
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|ImmutableMap
operator|.
expr|<
name|K
operator|,
name|V
operator|>
name|of
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
return|;
default|default:
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|orderPreservingCopy
init|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
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
name|e
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|orderPreservingCopy
operator|.
name|put
argument_list|(
name|checkNotNull
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
name|checkNotNull
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|orderPreservingCopy
argument_list|)
return|;
block|}
block|}
DECL|method|isPartialView ()
specifier|abstract
name|boolean
name|isPartialView
parameter_list|()
function_decl|;
DECL|method|put (K k, V v)
specifier|public
specifier|final
name|V
name|put
parameter_list|(
name|K
name|k
parameter_list|,
name|V
name|v
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|remove (Object o)
specifier|public
specifier|final
name|V
name|remove
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|putAll (Map<? extends K, ? extends V> map)
specifier|public
specifier|final
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|clear ()
specifier|public
specifier|final
name|void
name|clear
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|size
argument_list|()
operator|==
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|containsKey (@ullable Object key)
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
return|return
name|get
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|containsValue (@ullable Object value)
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
return|return
name|values
argument_list|()
operator|.
name|contains
argument_list|(
name|value
argument_list|)
return|;
block|}
DECL|field|cachedEntrySet
specifier|private
specifier|transient
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|cachedEntrySet
init|=
literal|null
decl_stmt|;
DECL|method|entrySet ()
specifier|public
specifier|final
name|ImmutableSet
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
if|if
condition|(
name|cachedEntrySet
operator|!=
literal|null
condition|)
block|{
return|return
name|cachedEntrySet
return|;
block|}
return|return
name|cachedEntrySet
operator|=
name|createEntrySet
argument_list|()
return|;
block|}
DECL|method|createEntrySet ()
specifier|abstract
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|createEntrySet
parameter_list|()
function_decl|;
DECL|field|cachedKeySet
specifier|private
specifier|transient
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|cachedKeySet
init|=
literal|null
decl_stmt|;
DECL|method|keySet ()
specifier|public
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
if|if
condition|(
name|cachedKeySet
operator|!=
literal|null
condition|)
block|{
return|return
name|cachedKeySet
return|;
block|}
return|return
name|cachedKeySet
operator|=
name|createKeySet
argument_list|()
return|;
block|}
DECL|method|createKeySet ()
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|createKeySet
parameter_list|()
block|{
return|return
operator|new
name|ImmutableMapKeySet
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|field|cachedValues
specifier|private
specifier|transient
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|cachedValues
init|=
literal|null
decl_stmt|;
DECL|method|values ()
specifier|public
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
if|if
condition|(
name|cachedValues
operator|!=
literal|null
condition|)
block|{
return|return
name|cachedValues
return|;
block|}
return|return
name|cachedValues
operator|=
name|createValues
argument_list|()
return|;
block|}
comment|// esnickell is editing here
comment|// cached so that this.multimapView().inverse() only computes inverse once
DECL|field|multimapView
specifier|private
specifier|transient
name|ImmutableSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|multimapView
decl_stmt|;
DECL|method|asMultimap ()
specifier|public
name|ImmutableSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|asMultimap
parameter_list|()
block|{
name|ImmutableSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|result
init|=
name|multimapView
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
operator|(
name|multimapView
operator|=
operator|new
name|ImmutableSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
operator|new
name|MapViewOfValuesAsSingletonSets
argument_list|()
argument_list|,
name|size
argument_list|()
argument_list|,
literal|null
argument_list|)
operator|)
else|:
name|result
return|;
block|}
DECL|class|MapViewOfValuesAsSingletonSets
specifier|final
class|class
name|MapViewOfValuesAsSingletonSets
extends|extends
name|IteratorBasedImmutableMap
argument_list|<
name|K
argument_list|,
name|ImmutableSet
argument_list|<
name|V
argument_list|>
argument_list|>
block|{
DECL|method|size ()
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|ImmutableMap
operator|.
name|this
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|keySet ()
annotation|@
name|Override
specifier|public
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
name|ImmutableMap
operator|.
name|this
operator|.
name|keySet
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
return|return
name|ImmutableMap
operator|.
name|this
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|get (@ullable Object key)
annotation|@
name|Override
specifier|public
name|ImmutableSet
argument_list|<
name|V
argument_list|>
name|get
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
block|{
name|V
name|outerValue
init|=
name|ImmutableMap
operator|.
name|this
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|outerValue
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|ImmutableSet
operator|.
name|of
argument_list|(
name|outerValue
argument_list|)
return|;
block|}
DECL|method|isPartialView ()
annotation|@
name|Override
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
name|ImmutableMap
operator|.
name|this
operator|.
name|isPartialView
argument_list|()
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
comment|// ImmutableSet.of(value).hashCode() == value.hashCode(), so the hashes are the same
return|return
name|ImmutableMap
operator|.
name|this
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|entryIterator ()
name|UnmodifiableIterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|ImmutableSet
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
name|entryIterator
parameter_list|()
block|{
specifier|final
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|backingIterator
init|=
name|ImmutableMap
operator|.
name|this
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
operator|new
name|UnmodifiableIterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|ImmutableSet
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|backingIterator
operator|.
name|hasNext
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|ImmutableSet
argument_list|<
name|V
argument_list|>
argument_list|>
name|next
parameter_list|()
block|{
specifier|final
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|backingEntry
init|=
name|backingIterator
operator|.
name|next
argument_list|()
decl_stmt|;
return|return
operator|new
name|AbstractMapEntry
argument_list|<
name|K
argument_list|,
name|ImmutableSet
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|K
name|getKey
parameter_list|()
block|{
return|return
name|backingEntry
operator|.
name|getKey
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ImmutableSet
argument_list|<
name|V
argument_list|>
name|getValue
parameter_list|()
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|(
name|backingEntry
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
return|;
block|}
block|}
DECL|method|createValues ()
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|createValues
parameter_list|()
block|{
return|return
operator|new
name|ImmutableMapValues
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|equals (@ullable Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
name|Maps
operator|.
name|equalsImpl
argument_list|(
name|this
argument_list|,
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
comment|// not caching hash code since it could change if map values are mutable
comment|// in a way that modifies their hash codes
return|return
name|entrySet
argument_list|()
operator|.
name|hashCode
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
name|Maps
operator|.
name|toStringImpl
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

