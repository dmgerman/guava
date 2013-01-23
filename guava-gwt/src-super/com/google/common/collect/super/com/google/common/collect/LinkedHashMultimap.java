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
name|Objects
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
name|ConcurrentModificationException
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
name|NoSuchElementException
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
comment|/**  * Implementation of {@code Multimap} that does not allow duplicate key-value  * entries and that returns collections whose iterators follow the ordering in  * which the data was added to the multimap.  *  *<p>The collections returned by {@code keySet}, {@code keys}, and {@code  * asMap} iterate through the keys in the order they were first added to the  * multimap. Similarly, {@code get}, {@code removeAll}, and {@code  * replaceValues} return collections that iterate through the values in the  * order they were added. The collections generated by {@code entries} and  * {@code values} iterate across the key-value mappings in the order they were  * added to the multimap.  *  *<p>The iteration ordering of the collections generated by {@code keySet},  * {@code keys}, and {@code asMap} has a few subtleties. As long as the set of  * keys remains unchanged, adding or removing mappings does not affect the key  * iteration order. However, if you remove all values associated with a key and  * then add the key back to the multimap, that key will come last in the key  * iteration order.  *  *<p>The multimap does not store duplicate key-value pairs. Adding a new  * key-value pair equal to an existing key-value pair has no effect.  *  *<p>Keys and values may be null. All optional multimap methods are supported,  * and all returned views are modifiable.  *  *<p>This class is not threadsafe when any concurrent operations update the  * multimap. Concurrent read operations will work correctly. To allow concurrent  * update operations, wrap your multimap with a call to {@link  * Multimaps#synchronizedSetMultimap}.  *  *<p>See the Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#Multimap">  * {@code Multimap}</a>.  *  * @author Jared Levy  * @author Louis Wasserman  * @since 2.0 (imported from Google Collections Library)  */
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
argument_list|(
name|DEFAULT_KEY_CAPACITY
argument_list|,
name|DEFAULT_VALUE_SET_CAPACITY
argument_list|)
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
name|Maps
operator|.
name|capacity
argument_list|(
name|expectedKeys
argument_list|)
argument_list|,
name|Maps
operator|.
name|capacity
argument_list|(
name|expectedValuesPerKey
argument_list|)
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
name|LinkedHashMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|result
init|=
name|create
argument_list|(
name|multimap
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|DEFAULT_VALUE_SET_CAPACITY
argument_list|)
decl_stmt|;
name|result
operator|.
name|putAll
argument_list|(
name|multimap
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|interface|ValueSetLink
specifier|private
interface|interface
name|ValueSetLink
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
block|{
DECL|method|getPredecessorInValueSet ()
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getPredecessorInValueSet
parameter_list|()
function_decl|;
DECL|method|getSuccessorInValueSet ()
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getSuccessorInValueSet
parameter_list|()
function_decl|;
DECL|method|setPredecessorInValueSet (ValueSetLink<K, V> entry)
name|void
name|setPredecessorInValueSet
parameter_list|(
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|)
function_decl|;
DECL|method|setSuccessorInValueSet (ValueSetLink<K, V> entry)
name|void
name|setSuccessorInValueSet
parameter_list|(
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|)
function_decl|;
block|}
DECL|method|succeedsInValueSet (ValueSetLink<K, V> pred, ValueSetLink<K, V> succ)
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|void
name|succeedsInValueSet
parameter_list|(
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|pred
parameter_list|,
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|succ
parameter_list|)
block|{
name|pred
operator|.
name|setSuccessorInValueSet
argument_list|(
name|succ
argument_list|)
expr_stmt|;
name|succ
operator|.
name|setPredecessorInValueSet
argument_list|(
name|pred
argument_list|)
expr_stmt|;
block|}
DECL|method|succeedsInMultimap ( ValueEntry<K, V> pred, ValueEntry<K, V> succ)
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|void
name|succeedsInMultimap
parameter_list|(
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|pred
parameter_list|,
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|succ
parameter_list|)
block|{
name|pred
operator|.
name|setSuccessorInMultimap
argument_list|(
name|succ
argument_list|)
expr_stmt|;
name|succ
operator|.
name|setPredecessorInMultimap
argument_list|(
name|pred
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteFromValueSet (ValueSetLink<K, V> entry)
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|void
name|deleteFromValueSet
parameter_list|(
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|)
block|{
name|succeedsInValueSet
argument_list|(
name|entry
operator|.
name|getPredecessorInValueSet
argument_list|()
argument_list|,
name|entry
operator|.
name|getSuccessorInValueSet
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteFromMultimap (ValueEntry<K, V> entry)
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|void
name|deleteFromMultimap
parameter_list|(
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|)
block|{
name|succeedsInMultimap
argument_list|(
name|entry
operator|.
name|getPredecessorInMultimap
argument_list|()
argument_list|,
name|entry
operator|.
name|getSuccessorInMultimap
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * LinkedHashMultimap entries are in no less than three coexisting linked lists:    * a bucket in the hash table for a Set<V> associated with a key, the linked list    * of insertion-ordered entries in that Set<V>, and the linked list of entries    * in the LinkedHashMultimap as a whole.    */
annotation|@
name|VisibleForTesting
DECL|class|ValueEntry
specifier|static
specifier|final
class|class
name|ValueEntry
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|smearedValueHash
specifier|final
name|int
name|smearedValueHash
decl_stmt|;
DECL|field|nextInValueBucket
annotation|@
name|Nullable
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nextInValueBucket
decl_stmt|;
DECL|field|predecessorInValueSet
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|predecessorInValueSet
decl_stmt|;
DECL|field|successorInValueSet
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|successorInValueSet
decl_stmt|;
DECL|field|predecessorInMultimap
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|predecessorInMultimap
decl_stmt|;
DECL|field|successorInMultimap
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|successorInMultimap
decl_stmt|;
DECL|method|ValueEntry (@ullable K key, @Nullable V value, int smearedValueHash, @Nullable ValueEntry<K, V> nextInValueBucket)
name|ValueEntry
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
name|int
name|smearedValueHash
parameter_list|,
annotation|@
name|Nullable
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nextInValueBucket
parameter_list|)
block|{
name|super
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|this
operator|.
name|smearedValueHash
operator|=
name|smearedValueHash
expr_stmt|;
name|this
operator|.
name|nextInValueBucket
operator|=
name|nextInValueBucket
expr_stmt|;
block|}
DECL|method|matchesValue (@ullable Object v, int smearedVHash)
name|boolean
name|matchesValue
parameter_list|(
annotation|@
name|Nullable
name|Object
name|v
parameter_list|,
name|int
name|smearedVHash
parameter_list|)
block|{
return|return
name|smearedValueHash
operator|==
name|smearedVHash
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|getValue
argument_list|()
argument_list|,
name|v
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getPredecessorInValueSet ()
specifier|public
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getPredecessorInValueSet
parameter_list|()
block|{
return|return
name|predecessorInValueSet
return|;
block|}
annotation|@
name|Override
DECL|method|getSuccessorInValueSet ()
specifier|public
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getSuccessorInValueSet
parameter_list|()
block|{
return|return
name|successorInValueSet
return|;
block|}
annotation|@
name|Override
DECL|method|setPredecessorInValueSet (ValueSetLink<K, V> entry)
specifier|public
name|void
name|setPredecessorInValueSet
parameter_list|(
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|)
block|{
name|predecessorInValueSet
operator|=
name|entry
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setSuccessorInValueSet (ValueSetLink<K, V> entry)
specifier|public
name|void
name|setSuccessorInValueSet
parameter_list|(
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|)
block|{
name|successorInValueSet
operator|=
name|entry
expr_stmt|;
block|}
DECL|method|getPredecessorInMultimap ()
specifier|public
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getPredecessorInMultimap
parameter_list|()
block|{
return|return
name|predecessorInMultimap
return|;
block|}
DECL|method|getSuccessorInMultimap ()
specifier|public
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getSuccessorInMultimap
parameter_list|()
block|{
return|return
name|successorInMultimap
return|;
block|}
DECL|method|setSuccessorInMultimap (ValueEntry<K, V> multimapSuccessor)
specifier|public
name|void
name|setSuccessorInMultimap
parameter_list|(
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|multimapSuccessor
parameter_list|)
block|{
name|this
operator|.
name|successorInMultimap
operator|=
name|multimapSuccessor
expr_stmt|;
block|}
DECL|method|setPredecessorInMultimap (ValueEntry<K, V> multimapPredecessor)
specifier|public
name|void
name|setPredecessorInMultimap
parameter_list|(
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|multimapPredecessor
parameter_list|)
block|{
name|this
operator|.
name|predecessorInMultimap
operator|=
name|multimapPredecessor
expr_stmt|;
block|}
block|}
DECL|field|DEFAULT_KEY_CAPACITY
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_KEY_CAPACITY
init|=
literal|16
decl_stmt|;
DECL|field|DEFAULT_VALUE_SET_CAPACITY
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_VALUE_SET_CAPACITY
init|=
literal|2
decl_stmt|;
DECL|field|VALUE_SET_LOAD_FACTOR
annotation|@
name|VisibleForTesting
specifier|static
specifier|final
name|double
name|VALUE_SET_LOAD_FACTOR
init|=
literal|1.0
decl_stmt|;
DECL|field|valueSetCapacity
annotation|@
name|VisibleForTesting
specifier|transient
name|int
name|valueSetCapacity
init|=
name|DEFAULT_VALUE_SET_CAPACITY
decl_stmt|;
DECL|field|multimapHeaderEntry
specifier|private
specifier|transient
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|multimapHeaderEntry
decl_stmt|;
DECL|method|LinkedHashMultimap (int keyCapacity, int valueSetCapacity)
specifier|private
name|LinkedHashMultimap
parameter_list|(
name|int
name|keyCapacity
parameter_list|,
name|int
name|valueSetCapacity
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
name|keyCapacity
argument_list|)
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|valueSetCapacity
operator|>=
literal|0
argument_list|,
literal|"expectedValuesPerKey must be>= 0 but was %s"
argument_list|,
name|valueSetCapacity
argument_list|)
expr_stmt|;
name|this
operator|.
name|valueSetCapacity
operator|=
name|valueSetCapacity
expr_stmt|;
name|this
operator|.
name|multimapHeaderEntry
operator|=
operator|new
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|succeedsInMultimap
argument_list|(
name|multimapHeaderEntry
argument_list|,
name|multimapHeaderEntry
argument_list|)
expr_stmt|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>Creates an empty {@code LinkedHashSet} for a collection of values for    * one key.    *    * @return a new {@code LinkedHashSet} containing a collection of values for    *     one key    */
annotation|@
name|Override
DECL|method|createCollection ()
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
name|valueSetCapacity
argument_list|)
return|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>Creates a decorated insertion-ordered set that also keeps track of the    * order in which key-value pairs are added to the multimap.    *    * @param key key to associate with values in the collection    * @return a new decorated set containing a collection of values for one key    */
annotation|@
name|Override
DECL|method|createCollection (K key)
name|Collection
argument_list|<
name|V
argument_list|>
name|createCollection
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
operator|new
name|ValueSet
argument_list|(
name|key
argument_list|,
name|valueSetCapacity
argument_list|)
return|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>If {@code values} is not empty and the multimap already contains a    * mapping for {@code key}, the {@code keySet()} ordering is unchanged.    * However, the provided values always come last in the {@link #entries()} and    * {@link #values()} iteration orderings.    */
annotation|@
name|Override
DECL|method|replaceValues (@ullable K key, Iterable<? extends V> values)
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
annotation|@
name|VisibleForTesting
DECL|class|ValueSet
specifier|final
class|class
name|ValueSet
extends|extends
name|Sets
operator|.
name|ImprovedAbstractSet
argument_list|<
name|V
argument_list|>
implements|implements
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/*      * We currently use a fixed load factor of 1.0, a bit higher than normal to reduce memory      * consumption.      */
DECL|field|key
specifier|private
specifier|final
name|K
name|key
decl_stmt|;
DECL|field|hashTable
annotation|@
name|VisibleForTesting
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|hashTable
decl_stmt|;
DECL|field|size
specifier|private
name|int
name|size
init|=
literal|0
decl_stmt|;
DECL|field|modCount
specifier|private
name|int
name|modCount
init|=
literal|0
decl_stmt|;
comment|// We use the set object itself as the end of the linked list, avoiding an unnecessary
comment|// entry object per key.
DECL|field|firstEntry
specifier|private
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|firstEntry
decl_stmt|;
DECL|field|lastEntry
specifier|private
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|lastEntry
decl_stmt|;
DECL|method|ValueSet (K key, int expectedValues)
name|ValueSet
parameter_list|(
name|K
name|key
parameter_list|,
name|int
name|expectedValues
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|firstEntry
operator|=
name|this
expr_stmt|;
name|this
operator|.
name|lastEntry
operator|=
name|this
expr_stmt|;
comment|// Round expected values up to a power of 2 to get the table size.
name|int
name|tableSize
init|=
name|Hashing
operator|.
name|closedTableSize
argument_list|(
name|expectedValues
argument_list|,
name|VALUE_SET_LOAD_FACTOR
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|hashTable
init|=
operator|new
name|ValueEntry
index|[
name|tableSize
index|]
decl_stmt|;
name|this
operator|.
name|hashTable
operator|=
name|hashTable
expr_stmt|;
block|}
DECL|method|mask ()
specifier|private
name|int
name|mask
parameter_list|()
block|{
return|return
name|hashTable
operator|.
name|length
operator|-
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|getPredecessorInValueSet ()
specifier|public
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getPredecessorInValueSet
parameter_list|()
block|{
return|return
name|lastEntry
return|;
block|}
annotation|@
name|Override
DECL|method|getSuccessorInValueSet ()
specifier|public
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getSuccessorInValueSet
parameter_list|()
block|{
return|return
name|firstEntry
return|;
block|}
annotation|@
name|Override
DECL|method|setPredecessorInValueSet (ValueSetLink<K, V> entry)
specifier|public
name|void
name|setPredecessorInValueSet
parameter_list|(
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|)
block|{
name|lastEntry
operator|=
name|entry
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setSuccessorInValueSet (ValueSetLink<K, V> entry)
specifier|public
name|void
name|setSuccessorInValueSet
parameter_list|(
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|)
block|{
name|firstEntry
operator|=
name|entry
expr_stmt|;
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
operator|new
name|Iterator
argument_list|<
name|V
argument_list|>
argument_list|()
block|{
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nextEntry
init|=
name|firstEntry
decl_stmt|;
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|toRemove
decl_stmt|;
name|int
name|expectedModCount
init|=
name|modCount
decl_stmt|;
specifier|private
name|void
name|checkForComodification
parameter_list|()
block|{
if|if
condition|(
name|modCount
operator|!=
name|expectedModCount
condition|)
block|{
throw|throw
operator|new
name|ConcurrentModificationException
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
name|checkForComodification
argument_list|()
expr_stmt|;
return|return
name|nextEntry
operator|!=
name|ValueSet
operator|.
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|V
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
operator|(
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|nextEntry
decl_stmt|;
name|V
name|result
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|toRemove
operator|=
name|entry
expr_stmt|;
name|nextEntry
operator|=
name|entry
operator|.
name|getSuccessorInValueSet
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|checkForComodification
argument_list|()
expr_stmt|;
name|Iterators
operator|.
name|checkRemove
argument_list|(
name|toRemove
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|ValueSet
operator|.
name|this
operator|.
name|remove
argument_list|(
name|toRemove
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|expectedModCount
operator|=
name|modCount
expr_stmt|;
name|toRemove
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|size
return|;
block|}
annotation|@
name|Override
DECL|method|contains (@ullable Object o)
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
name|int
name|smearedHash
init|=
name|Hashing
operator|.
name|smearedHash
argument_list|(
name|o
argument_list|)
decl_stmt|;
for|for
control|(
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|hashTable
index|[
name|smearedHash
operator|&
name|mask
argument_list|()
index|]
init|;
name|entry
operator|!=
literal|null
condition|;
name|entry
operator|=
name|entry
operator|.
name|nextInValueBucket
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|matchesValue
argument_list|(
name|o
argument_list|,
name|smearedHash
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
DECL|method|add (@ullable V value)
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
name|int
name|smearedHash
init|=
name|Hashing
operator|.
name|smearedHash
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|int
name|bucket
init|=
name|smearedHash
operator|&
name|mask
argument_list|()
decl_stmt|;
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|rowHead
init|=
name|hashTable
index|[
name|bucket
index|]
decl_stmt|;
for|for
control|(
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|rowHead
init|;
name|entry
operator|!=
literal|null
condition|;
name|entry
operator|=
name|entry
operator|.
name|nextInValueBucket
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|matchesValue
argument_list|(
name|value
argument_list|,
name|smearedHash
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newEntry
init|=
operator|new
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|smearedHash
argument_list|,
name|rowHead
argument_list|)
decl_stmt|;
name|succeedsInValueSet
argument_list|(
name|lastEntry
argument_list|,
name|newEntry
argument_list|)
expr_stmt|;
name|succeedsInValueSet
argument_list|(
name|newEntry
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|succeedsInMultimap
argument_list|(
name|multimapHeaderEntry
operator|.
name|getPredecessorInMultimap
argument_list|()
argument_list|,
name|newEntry
argument_list|)
expr_stmt|;
name|succeedsInMultimap
argument_list|(
name|newEntry
argument_list|,
name|multimapHeaderEntry
argument_list|)
expr_stmt|;
name|hashTable
index|[
name|bucket
index|]
operator|=
name|newEntry
expr_stmt|;
name|size
operator|++
expr_stmt|;
name|modCount
operator|++
expr_stmt|;
name|rehashIfNecessary
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|rehashIfNecessary ()
specifier|private
name|void
name|rehashIfNecessary
parameter_list|()
block|{
if|if
condition|(
name|Hashing
operator|.
name|needsResizing
argument_list|(
name|size
argument_list|,
name|hashTable
operator|.
name|length
argument_list|,
name|VALUE_SET_LOAD_FACTOR
argument_list|)
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|hashTable
init|=
operator|new
name|ValueEntry
index|[
name|this
operator|.
name|hashTable
operator|.
name|length
operator|*
literal|2
index|]
decl_stmt|;
name|this
operator|.
name|hashTable
operator|=
name|hashTable
expr_stmt|;
name|int
name|mask
init|=
name|hashTable
operator|.
name|length
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|firstEntry
init|;
name|entry
operator|!=
name|this
condition|;
name|entry
operator|=
name|entry
operator|.
name|getSuccessorInValueSet
argument_list|()
control|)
block|{
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|valueEntry
init|=
operator|(
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|entry
decl_stmt|;
name|int
name|bucket
init|=
name|valueEntry
operator|.
name|smearedValueHash
operator|&
name|mask
decl_stmt|;
name|valueEntry
operator|.
name|nextInValueBucket
operator|=
name|hashTable
index|[
name|bucket
index|]
expr_stmt|;
name|hashTable
index|[
name|bucket
index|]
operator|=
name|valueEntry
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|remove (@ullable Object o)
specifier|public
name|boolean
name|remove
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
name|int
name|smearedHash
init|=
name|Hashing
operator|.
name|smearedHash
argument_list|(
name|o
argument_list|)
decl_stmt|;
name|int
name|bucket
init|=
name|smearedHash
operator|&
name|mask
argument_list|()
decl_stmt|;
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|prev
init|=
literal|null
decl_stmt|;
for|for
control|(
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|hashTable
index|[
name|bucket
index|]
init|;
name|entry
operator|!=
literal|null
condition|;
name|prev
operator|=
name|entry
operator|,
name|entry
operator|=
name|entry
operator|.
name|nextInValueBucket
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|matchesValue
argument_list|(
name|o
argument_list|,
name|smearedHash
argument_list|)
condition|)
block|{
if|if
condition|(
name|prev
operator|==
literal|null
condition|)
block|{
comment|// first entry in the bucket
name|hashTable
index|[
name|bucket
index|]
operator|=
name|entry
operator|.
name|nextInValueBucket
expr_stmt|;
block|}
else|else
block|{
name|prev
operator|.
name|nextInValueBucket
operator|=
name|entry
operator|.
name|nextInValueBucket
expr_stmt|;
block|}
name|deleteFromValueSet
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|deleteFromMultimap
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|size
operator|--
expr_stmt|;
name|modCount
operator|++
expr_stmt|;
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
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|Arrays
operator|.
name|fill
argument_list|(
name|hashTable
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|size
operator|=
literal|0
expr_stmt|;
for|for
control|(
name|ValueSetLink
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|firstEntry
init|;
name|entry
operator|!=
name|this
condition|;
name|entry
operator|=
name|entry
operator|.
name|getSuccessorInValueSet
argument_list|()
control|)
block|{
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|valueEntry
init|=
operator|(
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|entry
decl_stmt|;
name|deleteFromMultimap
argument_list|(
name|valueEntry
argument_list|)
expr_stmt|;
block|}
name|succeedsInValueSet
argument_list|(
name|this
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|modCount
operator|++
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|entryIterator ()
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
name|entryIterator
parameter_list|()
block|{
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
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nextEntry
init|=
name|multimapHeaderEntry
operator|.
name|successorInMultimap
decl_stmt|;
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|toRemove
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|nextEntry
operator|!=
name|multimapHeaderEntry
return|;
block|}
annotation|@
name|Override
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
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
name|ValueEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|result
init|=
name|nextEntry
decl_stmt|;
name|toRemove
operator|=
name|result
expr_stmt|;
name|nextEntry
operator|=
name|nextEntry
operator|.
name|successorInMultimap
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|Iterators
operator|.
name|checkRemove
argument_list|(
name|toRemove
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|LinkedHashMultimap
operator|.
name|this
operator|.
name|remove
argument_list|(
name|toRemove
operator|.
name|getKey
argument_list|()
argument_list|,
name|toRemove
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|toRemove
operator|=
literal|null
expr_stmt|;
block|}
block|}
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
name|super
operator|.
name|clear
argument_list|()
expr_stmt|;
name|succeedsInMultimap
argument_list|(
name|multimapHeaderEntry
argument_list|,
name|multimapHeaderEntry
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

