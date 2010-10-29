begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|annotations
operator|.
name|VisibleForTesting
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
name|Preconditions
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
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
comment|/**  * Implementation of {@code Multimap} that does not allow duplicate key-value  * entries and that returns collections whose iterators follow the ordering in  * which the data was added to the multimap.  *  *<p>The collections returned by {@code keySet}, {@code keys}, and {@code  * asMap} iterate through the keys in the order they were first added to the  * multimap. Similarly, {@code get}, {@code removeAll}, and {@code  * replaceValues} return collections that iterate through the values in the  * order they were added. The collections generated by {@code entries} and  * {@code values} iterate across the key-value mappings in the order they were  * added to the multimap.  *  *<p>The iteration ordering of the collections generated by {@code keySet},  * {@code keys}, and {@code asMap} has a few subtleties. As long as the set of  * keys remains unchanged, adding or removing mappings does not affect the key  * iteration order. However, if you remove all values associated with a key and  * then add the key back to the multimap, that key will come last in the key  * iteration order.  *  *<p>The multimap does not store duplicate key-value pairs. Adding a new  * key-value pair equal to an existing key-value pair has no effect.  *  *<p>Keys and values may be null. All optional multimap methods are supported,  * and all returned views are modifiable.  *  *<p>This class is not threadsafe when any concurrent operations update the  * multimap. Concurrent read operations will work correctly. To allow concurrent  * update operations, wrap your multimap with a call to {@link  * Multimaps#synchronizedSetMultimap}.  *  * @author Jared Levy  * @since 2 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|,
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|LinkedHashMultimap
specifier|public
specifier|final
class|class
name|LinkedHashMultimap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|DEFAULT_VALUES_PER_KEY
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_VALUES_PER_KEY
init|=
literal|8
decl_stmt|;
annotation|@
name|VisibleForTesting
DECL|field|expectedValuesPerKey
specifier|transient
name|int
name|expectedValuesPerKey
init|=
name|DEFAULT_VALUES_PER_KEY
decl_stmt|;
comment|/**    * Map entries with an iteration order corresponding to the order in which the    * key-value pairs were added to the multimap.    */
comment|// package-private for GWT deserialization
DECL|field|linkedEntries
specifier|transient
name|Collection
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
name|linkedEntries
decl_stmt|;
comment|/**    * Creates a new, empty {@code LinkedHashMultimap} with the default initial    * capacities.    */
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|LinkedHashMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|LinkedHashMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Constructs an empty {@code LinkedHashMultimap} with enough capacity to hold    * the specified numbers of keys and values without rehashing.    *    * @param expectedKeys the expected number of distinct keys    * @param expectedValuesPerKey the expected average number of values per key    * @throws IllegalArgumentException if {@code expectedKeys} or {@code    *      expectedValuesPerKey} is negative    */
DECL|method|create ( int expectedKeys, int expectedValuesPerKey)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|LinkedHashMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|int
name|expectedKeys
parameter_list|,
name|int
name|expectedValuesPerKey
parameter_list|)
block|{
return|return
operator|new
name|LinkedHashMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|expectedKeys
argument_list|,
name|expectedValuesPerKey
argument_list|)
return|;
block|}
comment|/**    * Constructs a {@code LinkedHashMultimap} with the same mappings as the    * specified multimap. If a key-value mapping appears multiple times in the    * input multimap, it only appears once in the constructed multimap. The new    * multimap has the same {@link Multimap#entries()} iteration order as the    * input multimap, except for excluding duplicate mappings.    *    * @param multimap the multimap whose contents are copied to this multimap    */
DECL|method|create ( Multimap<? extends K, ? extends V> multimap)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|LinkedHashMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
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
return|return
operator|new
name|LinkedHashMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|multimap
argument_list|)
return|;
block|}
DECL|method|LinkedHashMultimap ()
specifier|private
name|LinkedHashMultimap
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|LinkedHashMap
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|linkedEntries
operator|=
name|Sets
operator|.
name|newLinkedHashSet
argument_list|()
expr_stmt|;
block|}
DECL|method|LinkedHashMultimap (int expectedKeys, int expectedValuesPerKey)
specifier|private
name|LinkedHashMultimap
parameter_list|(
name|int
name|expectedKeys
parameter_list|,
name|int
name|expectedValuesPerKey
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|LinkedHashMap
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|(
name|expectedKeys
argument_list|)
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkArgument
argument_list|(
name|expectedValuesPerKey
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|this
operator|.
name|expectedValuesPerKey
operator|=
name|expectedValuesPerKey
expr_stmt|;
name|linkedEntries
operator|=
operator|new
name|LinkedHashSet
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
argument_list|(
operator|(
name|int
operator|)
name|Math
operator|.
name|min
argument_list|(
literal|1
operator|<<
literal|30
argument_list|,
operator|(
operator|(
name|long
operator|)
name|expectedKeys
operator|)
operator|*
name|expectedValuesPerKey
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|LinkedHashMultimap (Multimap<? extends K, ? extends V> multimap)
specifier|private
name|LinkedHashMultimap
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
name|super
argument_list|(
operator|new
name|LinkedHashMap
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|(
name|Maps
operator|.
name|capacity
argument_list|(
name|multimap
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|linkedEntries
operator|=
operator|new
name|LinkedHashSet
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
argument_list|(
name|Maps
operator|.
name|capacity
argument_list|(
name|multimap
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|putAll
argument_list|(
name|multimap
argument_list|)
expr_stmt|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>Creates an empty {@code LinkedHashSet} for a collection of values for    * one key.    *    * @return a new {@code LinkedHashSet} containing a collection of values for    *     one key    */
DECL|method|createCollection ()
annotation|@
name|Override
name|Set
argument_list|<
name|V
argument_list|>
name|createCollection
parameter_list|()
block|{
return|return
operator|new
name|LinkedHashSet
argument_list|<
name|V
argument_list|>
argument_list|(
name|Maps
operator|.
name|capacity
argument_list|(
name|expectedValuesPerKey
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>Creates a decorated {@code LinkedHashSet} that also keeps track of the    * order in which key-value pairs are added to the multimap.    *    * @param key key to associate with values in the collection    * @return a new decorated {@code LinkedHashSet} containing a collection of    *     values for one key    */
DECL|method|createCollection (@ullable K key)
annotation|@
name|Override
name|Collection
argument_list|<
name|V
argument_list|>
name|createCollection
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|)
block|{
return|return
operator|new
name|SetDecorator
argument_list|(
name|key
argument_list|,
name|createCollection
argument_list|()
argument_list|)
return|;
block|}
DECL|class|SetDecorator
specifier|private
class|class
name|SetDecorator
extends|extends
name|ForwardingSet
argument_list|<
name|V
argument_list|>
block|{
DECL|field|delegate
specifier|final
name|Set
argument_list|<
name|V
argument_list|>
name|delegate
decl_stmt|;
DECL|field|key
specifier|final
name|K
name|key
decl_stmt|;
DECL|method|SetDecorator (@ullable K key, Set<V> delegate)
name|SetDecorator
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|,
name|Set
argument_list|<
name|V
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|V
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
DECL|method|createEntry (@ullable E value)
parameter_list|<
name|E
parameter_list|>
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|E
argument_list|>
name|createEntry
parameter_list|(
annotation|@
name|Nullable
name|E
name|value
parameter_list|)
block|{
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
DECL|method|createEntries (Collection<E> values)
parameter_list|<
name|E
parameter_list|>
name|Collection
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|E
argument_list|>
argument_list|>
name|createEntries
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|values
parameter_list|)
block|{
comment|// converts a collection of values into a list of key/value map entries
name|Collection
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|E
argument_list|>
argument_list|>
name|entries
init|=
name|Lists
operator|.
name|newArrayListWithExpectedSize
argument_list|(
name|values
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|E
name|value
range|:
name|values
control|)
block|{
name|entries
operator|.
name|add
argument_list|(
name|createEntry
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|entries
return|;
block|}
DECL|method|add (@ullable V value)
annotation|@
name|Override
specifier|public
name|boolean
name|add
parameter_list|(
annotation|@
name|Nullable
name|V
name|value
parameter_list|)
block|{
name|boolean
name|changed
init|=
name|delegate
operator|.
name|add
argument_list|(
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|changed
condition|)
block|{
name|linkedEntries
operator|.
name|add
argument_list|(
name|createEntry
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|changed
return|;
block|}
DECL|method|addAll (Collection<? extends V> values)
annotation|@
name|Override
specifier|public
name|boolean
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|values
parameter_list|)
block|{
name|boolean
name|changed
init|=
name|delegate
operator|.
name|addAll
argument_list|(
name|values
argument_list|)
decl_stmt|;
if|if
condition|(
name|changed
condition|)
block|{
name|linkedEntries
operator|.
name|addAll
argument_list|(
name|createEntries
argument_list|(
name|delegate
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|changed
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
for|for
control|(
name|V
name|value
range|:
name|delegate
control|)
block|{
name|linkedEntries
operator|.
name|remove
argument_list|(
name|createEntry
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|delegate
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
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
specifier|final
name|Iterator
argument_list|<
name|V
argument_list|>
name|delegateIterator
init|=
name|delegate
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
operator|new
name|Iterator
argument_list|<
name|V
argument_list|>
argument_list|()
block|{
name|V
name|value
decl_stmt|;
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|delegateIterator
operator|.
name|hasNext
argument_list|()
return|;
block|}
specifier|public
name|V
name|next
parameter_list|()
block|{
name|value
operator|=
name|delegateIterator
operator|.
name|next
argument_list|()
expr_stmt|;
return|return
name|value
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|delegateIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|linkedEntries
operator|.
name|remove
argument_list|(
name|createEntry
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|remove (@ullable Object value)
annotation|@
name|Override
specifier|public
name|boolean
name|remove
parameter_list|(
annotation|@
name|Nullable
name|Object
name|value
parameter_list|)
block|{
name|boolean
name|changed
init|=
name|delegate
operator|.
name|remove
argument_list|(
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|changed
condition|)
block|{
comment|/*          * linkedEntries.remove() will return false when this method is called          * by entries().iterator().remove()          */
name|linkedEntries
operator|.
name|remove
argument_list|(
name|createEntry
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|changed
return|;
block|}
DECL|method|removeAll (Collection<?> values)
annotation|@
name|Override
specifier|public
name|boolean
name|removeAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|values
parameter_list|)
block|{
name|boolean
name|changed
init|=
name|delegate
operator|.
name|removeAll
argument_list|(
name|values
argument_list|)
decl_stmt|;
if|if
condition|(
name|changed
condition|)
block|{
name|linkedEntries
operator|.
name|removeAll
argument_list|(
name|createEntries
argument_list|(
name|values
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|changed
return|;
block|}
DECL|method|retainAll (Collection<?> values)
annotation|@
name|Override
specifier|public
name|boolean
name|retainAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|values
parameter_list|)
block|{
comment|/*        * Calling linkedEntries.retainAll() would incorrectly remove values        * with other keys.        */
name|boolean
name|changed
init|=
literal|false
decl_stmt|;
name|Iterator
argument_list|<
name|V
argument_list|>
name|iterator
init|=
name|delegate
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
name|V
name|value
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|values
operator|.
name|contains
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|linkedEntries
operator|.
name|remove
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|changed
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|changed
return|;
block|}
block|}
comment|/**    * {@inheritDoc}    *    *<p>Generates an iterator across map entries that follows the ordering in    * which the key-value pairs were added to the multimap.    *    * @return a key-value iterator with the correct ordering    */
DECL|method|createEntryIterator ()
annotation|@
name|Override
name|Iterator
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
name|createEntryIterator
parameter_list|()
block|{
specifier|final
name|Iterator
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
name|delegateIterator
init|=
name|linkedEntries
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
operator|new
name|Iterator
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
argument_list|()
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
decl_stmt|;
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|delegateIterator
operator|.
name|hasNext
argument_list|()
return|;
block|}
specifier|public
name|Map
operator|.
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
name|delegateIterator
operator|.
name|next
argument_list|()
expr_stmt|;
return|return
name|entry
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
comment|// Remove from iterator first to keep iterator valid.
name|delegateIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|LinkedHashMultimap
operator|.
name|this
operator|.
name|remove
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
return|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>If {@code values} is not empty and the multimap already contains a    * mapping for {@code key}, the {@code keySet()} ordering is unchanged.    * However, the provided values always come last in the {@link #entries()} and    * {@link #values()} iteration orderings.    */
DECL|method|replaceValues ( @ullable K key, Iterable<? extends V> values)
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
comment|/**    * Returns a set of all key-value pairs. Changes to the returned set will    * update the underlying multimap, and vice versa. The entries set does not    * support the {@code add} or {@code addAll} operations.    *    *<p>The iterator generated by the returned set traverses the entries in the    * order they were added to the multimap.    *    *<p>Each entry is an immutable snapshot of a key-value mapping in the    * multimap, taken at the time the entry is returned by a method call to the    * collection or its iterator.    */
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
return|return
name|super
operator|.
name|entries
argument_list|()
return|;
block|}
comment|/**    * Returns a collection of all values in the multimap. Changes to the returned    * collection will update the underlying multimap, and vice versa.    *    *<p>The iterator generated by the returned collection traverses the values    * in the order they were added to the multimap.    */
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
name|super
operator|.
name|values
argument_list|()
return|;
block|}
comment|// Unfortunately, the entries() ordering does not determine the key ordering;
comment|// see the example in the LinkedListMultimap class Javadoc.
comment|/**    * @serialData the number of distinct keys, and then for each distinct key:    *     the first key, the number of values for that key, and the key's values,    *     followed by successive keys and values from the entries() ordering    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.io.ObjectOutputStream"
argument_list|)
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
name|writeInt
argument_list|(
name|expectedValuesPerKey
argument_list|)
expr_stmt|;
name|Serialization
operator|.
name|writeMultimap
argument_list|(
name|this
argument_list|,
name|stream
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
range|:
name|linkedEntries
control|)
block|{
name|stream
operator|.
name|writeObject
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|stream
operator|.
name|writeObject
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.io.ObjectInputStream"
argument_list|)
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
name|expectedValuesPerKey
operator|=
name|stream
operator|.
name|readInt
argument_list|()
expr_stmt|;
name|int
name|distinctKeys
init|=
name|Serialization
operator|.
name|readCount
argument_list|(
name|stream
argument_list|)
decl_stmt|;
name|setMap
argument_list|(
operator|new
name|LinkedHashMap
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|(
name|Maps
operator|.
name|capacity
argument_list|(
name|distinctKeys
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|linkedEntries
operator|=
operator|new
name|LinkedHashSet
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
argument_list|(
name|distinctKeys
operator|*
name|expectedValuesPerKey
argument_list|)
expr_stmt|;
name|Serialization
operator|.
name|populateMultimap
argument_list|(
name|this
argument_list|,
name|stream
argument_list|,
name|distinctKeys
argument_list|)
expr_stmt|;
name|linkedEntries
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// will clear and repopulate entries
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// reading data stored by writeObject
name|K
name|key
init|=
operator|(
name|K
operator|)
name|stream
operator|.
name|readObject
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// reading data stored by writeObject
name|V
name|value
init|=
operator|(
name|V
operator|)
name|stream
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|linkedEntries
operator|.
name|add
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java serialization not supported"
argument_list|)
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

