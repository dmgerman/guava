begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|AbstractCollection
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
comment|/**  * A skeleton {@code Multimap} implementation, not necessarily in terms of a {@code Map}.  *   * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AbstractMultimap
specifier|abstract
class|class
name|AbstractMultimap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
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
for|for
control|(
name|Collection
argument_list|<
name|V
argument_list|>
name|collection
range|:
name|asMap
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|collection
operator|.
name|contains
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|containsEntry (@ullable Object key, @Nullable Object value)
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
name|Collection
argument_list|<
name|V
argument_list|>
name|collection
init|=
name|asMap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|collection
operator|!=
literal|null
operator|&&
name|collection
operator|.
name|contains
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|remove (@ullable Object key, @Nullable Object value)
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
name|Collection
argument_list|<
name|V
argument_list|>
name|collection
init|=
name|asMap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|collection
operator|!=
literal|null
operator|&&
name|collection
operator|.
name|remove
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|put (@ullable K key, @Nullable V value)
specifier|public
name|boolean
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
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|add
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|putAll (@ullable K key, Iterable<? extends V> values)
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
name|checkNotNull
argument_list|(
name|values
argument_list|)
expr_stmt|;
comment|// make sure we only call values.iterator() once
comment|// and we only call get(key) if values is nonempty
if|if
condition|(
name|values
operator|instanceof
name|Collection
condition|)
block|{
name|Collection
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|valueCollection
init|=
operator|(
name|Collection
argument_list|<
name|?
extends|extends
name|V
argument_list|>
operator|)
name|values
decl_stmt|;
return|return
operator|!
name|valueCollection
operator|.
name|isEmpty
argument_list|()
operator|&&
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|addAll
argument_list|(
name|valueCollection
argument_list|)
return|;
block|}
else|else
block|{
name|Iterator
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|valueItr
init|=
name|values
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
name|valueItr
operator|.
name|hasNext
argument_list|()
operator|&&
name|Iterators
operator|.
name|addAll
argument_list|(
name|get
argument_list|(
name|key
argument_list|)
argument_list|,
name|valueItr
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|putAll (Multimap<? extends K, ? extends V> multimap)
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
name|multimap
parameter_list|)
block|{
name|boolean
name|changed
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Map
operator|.
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
name|multimap
operator|.
name|entries
argument_list|()
control|)
block|{
name|changed
operator||=
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
name|changed
return|;
block|}
annotation|@
name|Override
DECL|method|replaceValues (@ullable K key, Iterable<? extends V> values)
specifier|public
name|Collection
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
name|checkNotNull
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|result
init|=
name|removeAll
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|putAll
argument_list|(
name|key
argument_list|,
name|values
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|field|entries
specifier|private
specifier|transient
name|Collection
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
annotation|@
name|Override
DECL|method|entries ()
specifier|public
name|Collection
argument_list|<
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
name|Collection
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
name|entries
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|entries
operator|=
name|createEntries
argument_list|()
else|:
name|result
return|;
block|}
DECL|method|createEntries ()
name|Collection
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|createEntries
parameter_list|()
block|{
if|if
condition|(
name|this
operator|instanceof
name|SetMultimap
condition|)
block|{
return|return
operator|new
name|EntrySet
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|Entries
argument_list|()
return|;
block|}
block|}
DECL|class|Entries
specifier|private
class|class
name|Entries
extends|extends
name|Multimaps
operator|.
name|Entries
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|Override
DECL|method|multimap ()
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|multimap
parameter_list|()
block|{
return|return
name|AbstractMultimap
operator|.
name|this
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
return|return
name|entryIterator
argument_list|()
return|;
block|}
block|}
DECL|class|EntrySet
specifier|private
class|class
name|EntrySet
extends|extends
name|Entries
implements|implements
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Sets
operator|.
name|hashCodeImpl
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
block|{
return|return
name|Sets
operator|.
name|equalsImpl
argument_list|(
name|this
argument_list|,
name|obj
argument_list|)
return|;
block|}
block|}
DECL|method|entryIterator ()
specifier|abstract
name|Iterator
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
name|createKeySet
argument_list|()
else|:
name|result
return|;
block|}
DECL|method|createKeySet ()
name|Set
argument_list|<
name|K
argument_list|>
name|createKeySet
parameter_list|()
block|{
return|return
operator|new
name|Maps
operator|.
name|KeySet
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|(
name|asMap
argument_list|()
argument_list|)
return|;
block|}
DECL|field|keys
specifier|private
specifier|transient
name|Multiset
argument_list|<
name|K
argument_list|>
name|keys
decl_stmt|;
annotation|@
name|Override
DECL|method|keys ()
specifier|public
name|Multiset
argument_list|<
name|K
argument_list|>
name|keys
parameter_list|()
block|{
name|Multiset
argument_list|<
name|K
argument_list|>
name|result
init|=
name|keys
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|keys
operator|=
name|createKeys
argument_list|()
else|:
name|result
return|;
block|}
DECL|method|createKeys ()
name|Multiset
argument_list|<
name|K
argument_list|>
name|createKeys
parameter_list|()
block|{
return|return
operator|new
name|Multimaps
operator|.
name|Keys
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
DECL|field|values
specifier|private
specifier|transient
name|Collection
argument_list|<
name|V
argument_list|>
name|values
decl_stmt|;
annotation|@
name|Override
DECL|method|values ()
specifier|public
name|Collection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
name|Collection
argument_list|<
name|V
argument_list|>
name|result
init|=
name|values
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|values
operator|=
name|createValues
argument_list|()
else|:
name|result
return|;
block|}
DECL|method|createValues ()
name|Collection
argument_list|<
name|V
argument_list|>
name|createValues
parameter_list|()
block|{
return|return
operator|new
name|Values
argument_list|()
return|;
block|}
DECL|class|Values
class|class
name|Values
extends|extends
name|AbstractCollection
argument_list|<
name|V
argument_list|>
block|{
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|V
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|valueIterator
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
return|return
name|AbstractMultimap
operator|.
name|this
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|contains (@ullable Object o)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
return|return
name|AbstractMultimap
operator|.
name|this
operator|.
name|containsValue
argument_list|(
name|o
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
name|AbstractMultimap
operator|.
name|this
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|valueIterator ()
name|Iterator
argument_list|<
name|V
argument_list|>
name|valueIterator
parameter_list|()
block|{
return|return
name|Maps
operator|.
name|valueIterator
argument_list|(
name|entries
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
DECL|field|asMap
specifier|private
specifier|transient
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
decl_stmt|;
annotation|@
name|Override
DECL|method|asMap ()
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
name|Map
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|result
init|=
name|asMap
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|asMap
operator|=
name|createAsMap
argument_list|()
else|:
name|result
return|;
block|}
DECL|method|createAsMap ()
specifier|abstract
name|Map
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|createAsMap
parameter_list|()
function_decl|;
comment|// Comparison and hashing
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
name|Multimaps
operator|.
name|equalsImpl
argument_list|(
name|this
argument_list|,
name|object
argument_list|)
return|;
block|}
comment|/**    * Returns the hash code for this multimap.    *    *<p>The hash code of a multimap is defined as the hash code of the map view,    * as returned by {@link Multimap#asMap}.    *    * @see Map#hashCode    */
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|asMap
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/**    * Returns a string representation of the multimap, generated by calling    * {@code toString} on the map returned by {@link Multimap#asMap}.    *    * @return a string representation of the multimap    */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|asMap
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

