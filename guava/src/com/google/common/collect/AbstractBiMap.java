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
name|checkState
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
name|checkRemove
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
name|GwtCompatible
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
name|base
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|j2objc
operator|.
name|annotations
operator|.
name|WeakOuter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
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
comment|/**  * A general-purpose bimap implementation using any two backing {@code Map}  * instances.  *  *<p>Note that this class contains {@code equals()} calls that keep it from  * supporting {@code IdentityHashMap} backing maps.  *  * @author Kevin Bourrillion  * @author Mike Bostock  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|AbstractBiMap
specifier|abstract
class|class
name|AbstractBiMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingMap
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
implements|,
name|Serializable
block|{
DECL|field|delegate
specifier|private
specifier|transient
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
decl_stmt|;
DECL|field|inverse
specifier|transient
name|AbstractBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
decl_stmt|;
comment|/** Package-private constructor for creating a map-backed bimap. */
DECL|method|AbstractBiMap (Map<K, V> forward, Map<V, K> backward)
name|AbstractBiMap
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|forward
parameter_list|,
name|Map
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|backward
parameter_list|)
block|{
name|setDelegates
argument_list|(
name|forward
argument_list|,
name|backward
argument_list|)
expr_stmt|;
block|}
comment|/** Private constructor for inverse bimap. */
DECL|method|AbstractBiMap (Map<K, V> backward, AbstractBiMap<V, K> forward)
specifier|private
name|AbstractBiMap
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|backward
parameter_list|,
name|AbstractBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|forward
parameter_list|)
block|{
name|delegate
operator|=
name|backward
expr_stmt|;
name|inverse
operator|=
name|forward
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Map
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
comment|/**    * Returns its input, or throws an exception if this is not a valid key.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|checkKey (@ullable K key)
name|K
name|checkKey
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|)
block|{
return|return
name|key
return|;
block|}
comment|/**    * Returns its input, or throws an exception if this is not a valid value.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|checkValue (@ullable V value)
name|V
name|checkValue
parameter_list|(
annotation|@
name|Nullable
name|V
name|value
parameter_list|)
block|{
return|return
name|value
return|;
block|}
comment|/**    * Specifies the delegate maps going in each direction. Called by the    * constructor and by subclasses during deserialization.    */
DECL|method|setDelegates (Map<K, V> forward, Map<V, K> backward)
name|void
name|setDelegates
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|forward
parameter_list|,
name|Map
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|backward
parameter_list|)
block|{
name|checkState
argument_list|(
name|delegate
operator|==
literal|null
argument_list|)
expr_stmt|;
name|checkState
argument_list|(
name|inverse
operator|==
literal|null
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|forward
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|backward
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|forward
operator|!=
name|backward
argument_list|)
expr_stmt|;
name|delegate
operator|=
name|forward
expr_stmt|;
name|inverse
operator|=
operator|new
name|Inverse
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
argument_list|(
name|backward
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|setInverse (AbstractBiMap<V, K> inverse)
name|void
name|setInverse
parameter_list|(
name|AbstractBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
parameter_list|)
block|{
name|this
operator|.
name|inverse
operator|=
name|inverse
expr_stmt|;
block|}
comment|// Query Operations (optimizations)
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
name|inverse
operator|.
name|containsKey
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|// Modification Operations
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|put (@ullable K key, @Nullable V value)
specifier|public
name|V
name|put
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|,
annotation|@
name|Nullable
name|V
name|value
parameter_list|)
block|{
return|return
name|putInBothMaps
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|forcePut (@ullable K key, @Nullable V value)
specifier|public
name|V
name|forcePut
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|,
annotation|@
name|Nullable
name|V
name|value
parameter_list|)
block|{
return|return
name|putInBothMaps
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|putInBothMaps (@ullable K key, @Nullable V value, boolean force)
specifier|private
name|V
name|putInBothMaps
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|,
annotation|@
name|Nullable
name|V
name|value
parameter_list|,
name|boolean
name|force
parameter_list|)
block|{
name|checkKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|checkValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|boolean
name|containedKey
init|=
name|containsKey
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|containedKey
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|value
argument_list|,
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|value
return|;
block|}
if|if
condition|(
name|force
condition|)
block|{
name|inverse
argument_list|()
operator|.
name|remove
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|checkArgument
argument_list|(
operator|!
name|containsValue
argument_list|(
name|value
argument_list|)
argument_list|,
literal|"value already present: %s"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|V
name|oldValue
init|=
name|delegate
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|updateInverseMap
argument_list|(
name|key
argument_list|,
name|containedKey
argument_list|,
name|oldValue
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|oldValue
return|;
block|}
DECL|method|updateInverseMap (K key, boolean containedKey, V oldValue, V newValue)
specifier|private
name|void
name|updateInverseMap
parameter_list|(
name|K
name|key
parameter_list|,
name|boolean
name|containedKey
parameter_list|,
name|V
name|oldValue
parameter_list|,
name|V
name|newValue
parameter_list|)
block|{
if|if
condition|(
name|containedKey
condition|)
block|{
name|removeFromInverseMap
argument_list|(
name|oldValue
argument_list|)
expr_stmt|;
block|}
name|inverse
operator|.
name|delegate
operator|.
name|put
argument_list|(
name|newValue
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|remove (@ullable Object key)
specifier|public
name|V
name|remove
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
block|{
return|return
name|containsKey
argument_list|(
name|key
argument_list|)
condition|?
name|removeFromBothMaps
argument_list|(
name|key
argument_list|)
else|:
literal|null
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
DECL|method|removeFromBothMaps (Object key)
specifier|private
name|V
name|removeFromBothMaps
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|V
name|oldValue
init|=
name|delegate
operator|.
name|remove
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|removeFromInverseMap
argument_list|(
name|oldValue
argument_list|)
expr_stmt|;
return|return
name|oldValue
return|;
block|}
DECL|method|removeFromInverseMap (V oldValue)
specifier|private
name|void
name|removeFromInverseMap
parameter_list|(
name|V
name|oldValue
parameter_list|)
block|{
name|inverse
operator|.
name|delegate
operator|.
name|remove
argument_list|(
name|oldValue
argument_list|)
expr_stmt|;
block|}
comment|// Bulk Operations
annotation|@
name|Override
DECL|method|putAll (Map<? extends K, ? extends V> map)
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
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|delegate
operator|.
name|clear
argument_list|()
expr_stmt|;
name|inverse
operator|.
name|delegate
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|// Views
annotation|@
name|Override
DECL|method|inverse ()
specifier|public
name|BiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
parameter_list|()
block|{
return|return
name|inverse
return|;
block|}
DECL|field|keySet
specifier|private
specifier|transient
name|Set
argument_list|<
name|K
argument_list|>
name|keySet
decl_stmt|;
annotation|@
name|Override
DECL|method|keySet ()
specifier|public
name|Set
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
name|Set
argument_list|<
name|K
argument_list|>
name|result
init|=
name|keySet
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|keySet
operator|=
operator|new
name|KeySet
argument_list|()
else|:
name|result
return|;
block|}
annotation|@
name|WeakOuter
DECL|class|KeySet
specifier|private
class|class
name|KeySet
extends|extends
name|ForwardingSet
argument_list|<
name|K
argument_list|>
block|{
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Set
argument_list|<
name|K
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|keySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|AbstractBiMap
operator|.
name|this
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|remove (Object key)
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
if|if
condition|(
operator|!
name|contains
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|removeFromBothMaps
argument_list|(
name|key
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|removeAll (Collection<?> keysToRemove)
specifier|public
name|boolean
name|removeAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|keysToRemove
parameter_list|)
block|{
return|return
name|standardRemoveAll
argument_list|(
name|keysToRemove
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|retainAll (Collection<?> keysToRetain)
specifier|public
name|boolean
name|retainAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|keysToRetain
parameter_list|)
block|{
return|return
name|standardRetainAll
argument_list|(
name|keysToRetain
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|K
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Maps
operator|.
name|keyIterator
argument_list|(
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|field|valueSet
specifier|private
specifier|transient
name|Set
argument_list|<
name|V
argument_list|>
name|valueSet
decl_stmt|;
annotation|@
name|Override
DECL|method|values ()
specifier|public
name|Set
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
comment|/*      * We can almost reuse the inverse's keySet, except we have to fix the      * iteration order so that it is consistent with the forward map.      */
name|Set
argument_list|<
name|V
argument_list|>
name|result
init|=
name|valueSet
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|valueSet
operator|=
operator|new
name|ValueSet
argument_list|()
else|:
name|result
return|;
block|}
annotation|@
name|WeakOuter
DECL|class|ValueSet
specifier|private
class|class
name|ValueSet
extends|extends
name|ForwardingSet
argument_list|<
name|V
argument_list|>
block|{
DECL|field|valuesDelegate
specifier|final
name|Set
argument_list|<
name|V
argument_list|>
name|valuesDelegate
init|=
name|inverse
operator|.
name|keySet
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Set
argument_list|<
name|V
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|valuesDelegate
return|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|V
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Maps
operator|.
name|valueIterator
argument_list|(
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toArray ()
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|standardToArray
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toArray (T[] array)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|toArray
parameter_list|(
name|T
index|[]
name|array
parameter_list|)
block|{
return|return
name|standardToArray
argument_list|(
name|array
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
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
block|}
DECL|field|entrySet
specifier|private
specifier|transient
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
decl_stmt|;
annotation|@
name|Override
DECL|method|entrySet ()
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
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|result
init|=
name|entrySet
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|entrySet
operator|=
operator|new
name|EntrySet
argument_list|()
else|:
name|result
return|;
block|}
annotation|@
name|WeakOuter
DECL|class|EntrySet
specifier|private
class|class
name|EntrySet
extends|extends
name|ForwardingSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
block|{
DECL|field|esDelegate
specifier|final
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|esDelegate
init|=
name|delegate
operator|.
name|entrySet
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|esDelegate
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|AbstractBiMap
operator|.
name|this
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|remove (Object object)
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
operator|!
name|esDelegate
operator|.
name|contains
argument_list|(
name|object
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// safe because esDelgate.contains(object).
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|entry
init|=
operator|(
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
name|inverse
operator|.
name|delegate
operator|.
name|remove
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
comment|/*        * Remove the mapping in inverse before removing from esDelegate because        * if entry is part of esDelegate, entry might be invalidated after the        * mapping is removed from esDelegate.        */
name|esDelegate
operator|.
name|remove
argument_list|(
name|entry
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
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
name|iterator
init|=
name|esDelegate
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
operator|new
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|()
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|iterator
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
name|V
argument_list|>
name|next
parameter_list|()
block|{
name|entry
operator|=
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
specifier|final
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|finalEntry
init|=
name|entry
decl_stmt|;
return|return
operator|new
name|ForwardingMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|finalEntry
return|;
block|}
annotation|@
name|Override
specifier|public
name|V
name|setValue
parameter_list|(
name|V
name|value
parameter_list|)
block|{
comment|// Preconditions keep the map and inverse consistent.
name|checkState
argument_list|(
name|contains
argument_list|(
name|this
argument_list|)
argument_list|,
literal|"entry no longer in map"
argument_list|)
expr_stmt|;
comment|// similar to putInBothMaps, but set via entry
if|if
condition|(
name|Objects
operator|.
name|equal
argument_list|(
name|value
argument_list|,
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|value
return|;
block|}
name|checkArgument
argument_list|(
operator|!
name|containsValue
argument_list|(
name|value
argument_list|)
argument_list|,
literal|"value already present: %s"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|V
name|oldValue
init|=
name|finalEntry
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|checkState
argument_list|(
name|Objects
operator|.
name|equal
argument_list|(
name|value
argument_list|,
name|get
argument_list|(
name|getKey
argument_list|()
argument_list|)
argument_list|)
argument_list|,
literal|"entry no longer in map"
argument_list|)
expr_stmt|;
name|updateInverseMap
argument_list|(
name|getKey
argument_list|()
argument_list|,
literal|true
argument_list|,
name|oldValue
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|oldValue
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|checkRemove
argument_list|(
name|entry
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|V
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|removeFromInverseMap
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|// See java.util.Collections.CheckedEntrySet for details on attacks.
annotation|@
name|Override
DECL|method|toArray ()
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|standardToArray
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toArray (T[] array)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|toArray
parameter_list|(
name|T
index|[]
name|array
parameter_list|)
block|{
return|return
name|standardToArray
argument_list|(
name|array
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|contains (Object o)
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|Maps
operator|.
name|containsEntryImpl
argument_list|(
name|delegate
argument_list|()
argument_list|,
name|o
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|containsAll (Collection<?> c)
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
return|return
name|standardContainsAll
argument_list|(
name|c
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|removeAll (Collection<?> c)
specifier|public
name|boolean
name|removeAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
return|return
name|standardRemoveAll
argument_list|(
name|c
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|retainAll (Collection<?> c)
specifier|public
name|boolean
name|retainAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
return|return
name|standardRetainAll
argument_list|(
name|c
argument_list|)
return|;
block|}
block|}
comment|/** The inverse of any other {@code AbstractBiMap} subclass. */
DECL|class|Inverse
specifier|private
specifier|static
class|class
name|Inverse
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|Inverse (Map<K, V> backward, AbstractBiMap<V, K> forward)
specifier|private
name|Inverse
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|backward
parameter_list|,
name|AbstractBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|forward
parameter_list|)
block|{
name|super
argument_list|(
name|backward
argument_list|,
name|forward
argument_list|)
expr_stmt|;
block|}
comment|/*      * Serialization stores the forward bimap, the inverse of this inverse.      * Deserialization calls inverse() on the forward bimap and returns that      * inverse.      *      * If a bimap and its inverse are serialized together, the deserialized      * instances have inverse() methods that return the other.      */
annotation|@
name|Override
DECL|method|checkKey (K key)
name|K
name|checkKey
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|inverse
operator|.
name|checkValue
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|checkValue (V value)
name|V
name|checkValue
parameter_list|(
name|V
name|value
parameter_list|)
block|{
return|return
name|inverse
operator|.
name|checkKey
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**      * @serialData the forward bimap      */
annotation|@
name|GwtIncompatible
comment|// java.io.ObjectOuputStream
DECL|method|writeObject (ObjectOutputStream stream)
specifier|private
name|void
name|writeObject
parameter_list|(
name|ObjectOutputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|stream
operator|.
name|defaultWriteObject
argument_list|()
expr_stmt|;
name|stream
operator|.
name|writeObject
argument_list|(
name|inverse
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// java.io.ObjectInputStream
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// reading data stored by writeObject
DECL|method|readObject (ObjectInputStream stream)
specifier|private
name|void
name|readObject
parameter_list|(
name|ObjectInputStream
name|stream
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|stream
operator|.
name|defaultReadObject
argument_list|()
expr_stmt|;
name|setInverse
argument_list|(
operator|(
name|AbstractBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
operator|)
name|stream
operator|.
name|readObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// Not needed in the emulated source.
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|inverse
argument_list|()
operator|.
name|inverse
argument_list|()
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// Not needed in emulated source.
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
annotation|@
name|GwtIncompatible
comment|// Not needed in emulated source.
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
end_class

end_unit

