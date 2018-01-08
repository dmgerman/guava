begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2017 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|checkElementIndex
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
name|checkPositive
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
name|Hashing
operator|.
name|smearedHash
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Multiset
operator|.
name|Entry
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
name|Multisets
operator|.
name|AbstractEntry
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
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_comment
comment|/**  * ObjectCountHashMap is an implementation of {@code AbstractObjectCountMap} that uses arrays to  * store key objects and count values. Comparing to using a traditional {@code HashMap}  * implementation which stores keys and count values as map entries, {@code ObjectCountHashMap}  * minimizes object allocation and reduces memory footprint.  *  *<p>In the absence of element deletions, this will iterate over elements in insertion order.  */
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
DECL|class|ObjectCountHashMap
class|class
name|ObjectCountHashMap
parameter_list|<
name|K
parameter_list|>
block|{
comment|/** Creates an empty {@code ObjectCountHashMap} instance. */
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|>
name|ObjectCountHashMap
argument_list|<
name|K
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|ObjectCountHashMap
argument_list|<
name|K
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Creates a {@code ObjectCountHashMap} instance, with a high enough "initial capacity" that it    *<i>should</i> hold {@code expectedSize} elements without growth.    *    * @param expectedSize the number of elements you expect to add to the returned set    * @return a new, empty {@code ObjectCountHashMap} with enough capacity to hold {@code    *     expectedSize} elements without resizing    * @throws IllegalArgumentException if {@code expectedSize} is negative    */
DECL|method|createWithExpectedSize (int expectedSize)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|>
name|ObjectCountHashMap
argument_list|<
name|K
argument_list|>
name|createWithExpectedSize
parameter_list|(
name|int
name|expectedSize
parameter_list|)
block|{
return|return
operator|new
name|ObjectCountHashMap
argument_list|<
name|K
argument_list|>
argument_list|(
name|expectedSize
argument_list|)
return|;
block|}
DECL|field|MAXIMUM_CAPACITY
specifier|private
specifier|static
specifier|final
name|int
name|MAXIMUM_CAPACITY
init|=
literal|1
operator|<<
literal|30
decl_stmt|;
DECL|field|DEFAULT_LOAD_FACTOR
specifier|static
specifier|final
name|float
name|DEFAULT_LOAD_FACTOR
init|=
literal|1.0f
decl_stmt|;
comment|/** Bitmask that selects the low 32 bits. */
DECL|field|NEXT_MASK
specifier|private
specifier|static
specifier|final
name|long
name|NEXT_MASK
init|=
operator|(
literal|1L
operator|<<
literal|32
operator|)
operator|-
literal|1
decl_stmt|;
comment|/** Bitmask that selects the high 32 bits. */
DECL|field|HASH_MASK
specifier|private
specifier|static
specifier|final
name|long
name|HASH_MASK
init|=
operator|~
name|NEXT_MASK
decl_stmt|;
DECL|field|DEFAULT_SIZE
specifier|static
specifier|final
name|int
name|DEFAULT_SIZE
init|=
literal|3
decl_stmt|;
comment|// used to indicate blank table entries
DECL|field|UNSET
specifier|static
specifier|final
name|int
name|UNSET
init|=
operator|-
literal|1
decl_stmt|;
comment|/** The keys of the entries in the map. */
DECL|field|keys
specifier|transient
name|Object
index|[]
name|keys
decl_stmt|;
comment|/** The values of the entries in the map. */
DECL|field|values
specifier|transient
name|int
index|[]
name|values
decl_stmt|;
DECL|field|size
specifier|transient
name|int
name|size
decl_stmt|;
DECL|field|modCount
specifier|transient
name|int
name|modCount
decl_stmt|;
comment|/**    * The hashtable. Its values are indexes to the keys, values, and entries arrays.    *    *<p>Currently, the UNSET value means "null pointer", and any non negative value x is the actual    * index.    *    *<p>Its size must be a power of two.    */
DECL|field|table
specifier|private
specifier|transient
name|int
index|[]
name|table
decl_stmt|;
comment|/**    * Contains the logical entries, in the range of [0, size()). The high 32 bits of each long is the    * smeared hash of the element, whereas the low 32 bits is the "next" pointer (pointing to the    * next entry in the bucket chain). The pointers in [size(), entries.length) are all "null"    * (UNSET).    */
DECL|field|entries
annotation|@
name|VisibleForTesting
specifier|transient
name|long
index|[]
name|entries
decl_stmt|;
comment|/** The load factor. */
DECL|field|loadFactor
specifier|private
specifier|transient
name|float
name|loadFactor
decl_stmt|;
comment|/** When we have this many elements, resize the hashtable. */
DECL|field|threshold
specifier|private
specifier|transient
name|int
name|threshold
decl_stmt|;
comment|/** Constructs a new empty instance of {@code ObjectCountHashMap}. */
DECL|method|ObjectCountHashMap ()
name|ObjectCountHashMap
parameter_list|()
block|{
name|init
argument_list|(
name|DEFAULT_SIZE
argument_list|,
name|DEFAULT_LOAD_FACTOR
argument_list|)
expr_stmt|;
block|}
DECL|method|ObjectCountHashMap (ObjectCountHashMap<K> map)
name|ObjectCountHashMap
parameter_list|(
name|ObjectCountHashMap
argument_list|<
name|K
argument_list|>
name|map
parameter_list|)
block|{
name|init
argument_list|(
name|map
operator|.
name|size
argument_list|()
argument_list|,
name|DEFAULT_LOAD_FACTOR
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|map
operator|.
name|firstIndex
argument_list|()
init|;
name|i
operator|!=
operator|-
literal|1
condition|;
name|i
operator|=
name|map
operator|.
name|nextIndex
argument_list|(
name|i
argument_list|)
control|)
block|{
name|put
argument_list|(
name|map
operator|.
name|getKey
argument_list|(
name|i
argument_list|)
argument_list|,
name|map
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Constructs a new instance of {@code ObjectCountHashMap} with the specified capacity.    *    * @param capacity the initial capacity of this {@code ObjectCountHashMap}.    */
DECL|method|ObjectCountHashMap (int capacity)
name|ObjectCountHashMap
parameter_list|(
name|int
name|capacity
parameter_list|)
block|{
name|this
argument_list|(
name|capacity
argument_list|,
name|DEFAULT_LOAD_FACTOR
argument_list|)
expr_stmt|;
block|}
DECL|method|ObjectCountHashMap (int expectedSize, float loadFactor)
name|ObjectCountHashMap
parameter_list|(
name|int
name|expectedSize
parameter_list|,
name|float
name|loadFactor
parameter_list|)
block|{
name|init
argument_list|(
name|expectedSize
argument_list|,
name|loadFactor
argument_list|)
expr_stmt|;
block|}
DECL|method|init (int expectedSize, float loadFactor)
name|void
name|init
parameter_list|(
name|int
name|expectedSize
parameter_list|,
name|float
name|loadFactor
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkArgument
argument_list|(
name|expectedSize
operator|>=
literal|0
argument_list|,
literal|"Initial capacity must be non-negative"
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkArgument
argument_list|(
name|loadFactor
operator|>
literal|0
argument_list|,
literal|"Illegal load factor"
argument_list|)
expr_stmt|;
name|int
name|buckets
init|=
name|Hashing
operator|.
name|closedTableSize
argument_list|(
name|expectedSize
argument_list|,
name|loadFactor
argument_list|)
decl_stmt|;
name|this
operator|.
name|table
operator|=
name|newTable
argument_list|(
name|buckets
argument_list|)
expr_stmt|;
name|this
operator|.
name|loadFactor
operator|=
name|loadFactor
expr_stmt|;
name|this
operator|.
name|keys
operator|=
operator|new
name|Object
index|[
name|expectedSize
index|]
expr_stmt|;
name|this
operator|.
name|values
operator|=
operator|new
name|int
index|[
name|expectedSize
index|]
expr_stmt|;
name|this
operator|.
name|entries
operator|=
name|newEntries
argument_list|(
name|expectedSize
argument_list|)
expr_stmt|;
name|this
operator|.
name|threshold
operator|=
name|Math
operator|.
name|max
argument_list|(
literal|1
argument_list|,
call|(
name|int
call|)
argument_list|(
name|buckets
operator|*
name|loadFactor
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|newTable (int size)
specifier|private
specifier|static
name|int
index|[]
name|newTable
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|int
index|[]
name|array
init|=
operator|new
name|int
index|[
name|size
index|]
decl_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|array
argument_list|,
name|UNSET
argument_list|)
expr_stmt|;
return|return
name|array
return|;
block|}
DECL|method|newEntries (int size)
specifier|private
specifier|static
name|long
index|[]
name|newEntries
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|long
index|[]
name|array
init|=
operator|new
name|long
index|[
name|size
index|]
decl_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|array
argument_list|,
name|UNSET
argument_list|)
expr_stmt|;
return|return
name|array
return|;
block|}
DECL|method|hashTableMask ()
specifier|private
name|int
name|hashTableMask
parameter_list|()
block|{
return|return
name|table
operator|.
name|length
operator|-
literal|1
return|;
block|}
DECL|method|firstIndex ()
name|int
name|firstIndex
parameter_list|()
block|{
return|return
operator|(
name|size
operator|==
literal|0
operator|)
condition|?
operator|-
literal|1
else|:
literal|0
return|;
block|}
DECL|method|nextIndex (int index)
name|int
name|nextIndex
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
operator|(
name|index
operator|+
literal|1
operator|<
name|size
operator|)
condition|?
name|index
operator|+
literal|1
else|:
operator|-
literal|1
return|;
block|}
DECL|method|nextIndexAfterRemove (int oldNextIndex, @SuppressWarnings(R) int removedIndex)
name|int
name|nextIndexAfterRemove
parameter_list|(
name|int
name|oldNextIndex
parameter_list|,
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|int
name|removedIndex
parameter_list|)
block|{
return|return
name|oldNextIndex
operator|-
literal|1
return|;
block|}
DECL|method|size ()
name|int
name|size
parameter_list|()
block|{
return|return
name|size
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getKey (int index)
name|K
name|getKey
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|checkElementIndex
argument_list|(
name|index
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
operator|(
name|K
operator|)
name|keys
index|[
name|index
index|]
return|;
block|}
DECL|method|getValue (int index)
name|int
name|getValue
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|checkElementIndex
argument_list|(
name|index
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
name|values
index|[
name|index
index|]
return|;
block|}
DECL|method|setValue (int index, int newValue)
name|void
name|setValue
parameter_list|(
name|int
name|index
parameter_list|,
name|int
name|newValue
parameter_list|)
block|{
name|checkElementIndex
argument_list|(
name|index
argument_list|,
name|size
argument_list|)
expr_stmt|;
name|values
index|[
name|index
index|]
operator|=
name|newValue
expr_stmt|;
block|}
DECL|method|getEntry (int index)
name|Entry
argument_list|<
name|K
argument_list|>
name|getEntry
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|checkElementIndex
argument_list|(
name|index
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
operator|new
name|MapEntry
argument_list|(
name|index
argument_list|)
return|;
block|}
DECL|class|MapEntry
class|class
name|MapEntry
extends|extends
name|AbstractEntry
argument_list|<
name|K
argument_list|>
block|{
DECL|field|key
annotation|@
name|NullableDecl
specifier|final
name|K
name|key
decl_stmt|;
DECL|field|lastKnownIndex
name|int
name|lastKnownIndex
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// keys only contains Ks
DECL|method|MapEntry (int index)
name|MapEntry
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
operator|(
name|K
operator|)
name|keys
index|[
name|index
index|]
expr_stmt|;
name|this
operator|.
name|lastKnownIndex
operator|=
name|index
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getElement ()
specifier|public
name|K
name|getElement
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|updateLastKnownIndex ()
name|void
name|updateLastKnownIndex
parameter_list|()
block|{
if|if
condition|(
name|lastKnownIndex
operator|==
operator|-
literal|1
operator|||
name|lastKnownIndex
operator|>=
name|size
argument_list|()
operator|||
operator|!
name|Objects
operator|.
name|equal
argument_list|(
name|key
argument_list|,
name|keys
index|[
name|lastKnownIndex
index|]
argument_list|)
condition|)
block|{
name|lastKnownIndex
operator|=
name|indexOf
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// values only contains Vs
annotation|@
name|Override
DECL|method|getCount ()
specifier|public
name|int
name|getCount
parameter_list|()
block|{
name|updateLastKnownIndex
argument_list|()
expr_stmt|;
return|return
operator|(
name|lastKnownIndex
operator|==
operator|-
literal|1
operator|)
condition|?
literal|0
else|:
name|values
index|[
name|lastKnownIndex
index|]
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// values only contains Vs
annotation|@
name|CanIgnoreReturnValue
DECL|method|setCount (int count)
specifier|public
name|int
name|setCount
parameter_list|(
name|int
name|count
parameter_list|)
block|{
name|updateLastKnownIndex
argument_list|()
expr_stmt|;
if|if
condition|(
name|lastKnownIndex
operator|==
operator|-
literal|1
condition|)
block|{
name|put
argument_list|(
name|key
argument_list|,
name|count
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
else|else
block|{
name|int
name|old
init|=
name|values
index|[
name|lastKnownIndex
index|]
decl_stmt|;
name|values
index|[
name|lastKnownIndex
index|]
operator|=
name|count
expr_stmt|;
return|return
name|old
return|;
block|}
block|}
block|}
DECL|method|getHash (long entry)
specifier|private
specifier|static
name|int
name|getHash
parameter_list|(
name|long
name|entry
parameter_list|)
block|{
return|return
call|(
name|int
call|)
argument_list|(
name|entry
operator|>>>
literal|32
argument_list|)
return|;
block|}
comment|/** Returns the index, or UNSET if the pointer is "null" */
DECL|method|getNext (long entry)
specifier|private
specifier|static
name|int
name|getNext
parameter_list|(
name|long
name|entry
parameter_list|)
block|{
return|return
operator|(
name|int
operator|)
name|entry
return|;
block|}
comment|/** Returns a new entry value by changing the "next" index of an existing entry */
DECL|method|swapNext (long entry, int newNext)
specifier|private
specifier|static
name|long
name|swapNext
parameter_list|(
name|long
name|entry
parameter_list|,
name|int
name|newNext
parameter_list|)
block|{
return|return
operator|(
name|HASH_MASK
operator|&
name|entry
operator|)
operator||
operator|(
name|NEXT_MASK
operator|&
name|newNext
operator|)
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
DECL|method|put (@ullableDecl K key, int value)
specifier|public
name|int
name|put
parameter_list|(
annotation|@
name|NullableDecl
name|K
name|key
parameter_list|,
name|int
name|value
parameter_list|)
block|{
name|checkPositive
argument_list|(
name|value
argument_list|,
literal|"count"
argument_list|)
expr_stmt|;
name|long
index|[]
name|entries
init|=
name|this
operator|.
name|entries
decl_stmt|;
name|Object
index|[]
name|keys
init|=
name|this
operator|.
name|keys
decl_stmt|;
name|int
index|[]
name|values
init|=
name|this
operator|.
name|values
decl_stmt|;
name|int
name|hash
init|=
name|smearedHash
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|int
name|tableIndex
init|=
name|hash
operator|&
name|hashTableMask
argument_list|()
decl_stmt|;
name|int
name|newEntryIndex
init|=
name|this
operator|.
name|size
decl_stmt|;
comment|// current size, and pointer to the entry to be appended
name|int
name|next
init|=
name|table
index|[
name|tableIndex
index|]
decl_stmt|;
if|if
condition|(
name|next
operator|==
name|UNSET
condition|)
block|{
name|table
index|[
name|tableIndex
index|]
operator|=
name|newEntryIndex
expr_stmt|;
block|}
else|else
block|{
name|int
name|last
decl_stmt|;
name|long
name|entry
decl_stmt|;
do|do
block|{
name|last
operator|=
name|next
expr_stmt|;
name|entry
operator|=
name|entries
index|[
name|next
index|]
expr_stmt|;
if|if
condition|(
name|getHash
argument_list|(
name|entry
argument_list|)
operator|==
name|hash
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|key
argument_list|,
name|keys
index|[
name|next
index|]
argument_list|)
condition|)
block|{
name|int
name|oldValue
init|=
name|values
index|[
name|next
index|]
decl_stmt|;
name|values
index|[
name|next
index|]
operator|=
name|value
expr_stmt|;
return|return
name|oldValue
return|;
block|}
name|next
operator|=
name|getNext
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|next
operator|!=
name|UNSET
condition|)
do|;
name|entries
index|[
name|last
index|]
operator|=
name|swapNext
argument_list|(
name|entry
argument_list|,
name|newEntryIndex
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|newEntryIndex
operator|==
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot contain more than Integer.MAX_VALUE elements!"
argument_list|)
throw|;
block|}
name|int
name|newSize
init|=
name|newEntryIndex
operator|+
literal|1
decl_stmt|;
name|resizeMeMaybe
argument_list|(
name|newSize
argument_list|)
expr_stmt|;
name|insertEntry
argument_list|(
name|newEntryIndex
argument_list|,
name|key
argument_list|,
name|value
argument_list|,
name|hash
argument_list|)
expr_stmt|;
name|this
operator|.
name|size
operator|=
name|newSize
expr_stmt|;
if|if
condition|(
name|newEntryIndex
operator|>=
name|threshold
condition|)
block|{
name|resizeTable
argument_list|(
literal|2
operator|*
name|table
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
name|modCount
operator|++
expr_stmt|;
return|return
literal|0
return|;
block|}
comment|/**    * Creates a fresh entry with the specified object at the specified position in the entry array.    */
DECL|method|insertEntry (int entryIndex, @NullableDecl K key, int value, int hash)
name|void
name|insertEntry
parameter_list|(
name|int
name|entryIndex
parameter_list|,
annotation|@
name|NullableDecl
name|K
name|key
parameter_list|,
name|int
name|value
parameter_list|,
name|int
name|hash
parameter_list|)
block|{
name|this
operator|.
name|entries
index|[
name|entryIndex
index|]
operator|=
operator|(
operator|(
name|long
operator|)
name|hash
operator|<<
literal|32
operator|)
operator||
operator|(
name|NEXT_MASK
operator|&
name|UNSET
operator|)
expr_stmt|;
name|this
operator|.
name|keys
index|[
name|entryIndex
index|]
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|values
index|[
name|entryIndex
index|]
operator|=
name|value
expr_stmt|;
block|}
comment|/** Returns currentSize + 1, after resizing the entries storage if necessary. */
DECL|method|resizeMeMaybe (int newSize)
specifier|private
name|void
name|resizeMeMaybe
parameter_list|(
name|int
name|newSize
parameter_list|)
block|{
name|int
name|entriesSize
init|=
name|entries
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|newSize
operator|>
name|entriesSize
condition|)
block|{
name|int
name|newCapacity
init|=
name|entriesSize
operator|+
name|Math
operator|.
name|max
argument_list|(
literal|1
argument_list|,
name|entriesSize
operator|>>>
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|newCapacity
operator|<
literal|0
condition|)
block|{
name|newCapacity
operator|=
name|Integer
operator|.
name|MAX_VALUE
expr_stmt|;
block|}
if|if
condition|(
name|newCapacity
operator|!=
name|entriesSize
condition|)
block|{
name|resizeEntries
argument_list|(
name|newCapacity
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Resizes the internal entries array to the specified capacity, which may be greater or less than    * the current capacity.    */
DECL|method|resizeEntries (int newCapacity)
name|void
name|resizeEntries
parameter_list|(
name|int
name|newCapacity
parameter_list|)
block|{
name|this
operator|.
name|keys
operator|=
name|Arrays
operator|.
name|copyOf
argument_list|(
name|keys
argument_list|,
name|newCapacity
argument_list|)
expr_stmt|;
name|this
operator|.
name|values
operator|=
name|Arrays
operator|.
name|copyOf
argument_list|(
name|values
argument_list|,
name|newCapacity
argument_list|)
expr_stmt|;
name|long
index|[]
name|entries
init|=
name|this
operator|.
name|entries
decl_stmt|;
name|int
name|oldCapacity
init|=
name|entries
operator|.
name|length
decl_stmt|;
name|entries
operator|=
name|Arrays
operator|.
name|copyOf
argument_list|(
name|entries
argument_list|,
name|newCapacity
argument_list|)
expr_stmt|;
if|if
condition|(
name|newCapacity
operator|>
name|oldCapacity
condition|)
block|{
name|Arrays
operator|.
name|fill
argument_list|(
name|entries
argument_list|,
name|oldCapacity
argument_list|,
name|newCapacity
argument_list|,
name|UNSET
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|entries
operator|=
name|entries
expr_stmt|;
block|}
DECL|method|resizeTable (int newCapacity)
specifier|private
name|void
name|resizeTable
parameter_list|(
name|int
name|newCapacity
parameter_list|)
block|{
comment|// newCapacity always a power of two
name|int
index|[]
name|oldTable
init|=
name|table
decl_stmt|;
name|int
name|oldCapacity
init|=
name|oldTable
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|oldCapacity
operator|>=
name|MAXIMUM_CAPACITY
condition|)
block|{
name|threshold
operator|=
name|Integer
operator|.
name|MAX_VALUE
expr_stmt|;
return|return;
block|}
name|int
name|newThreshold
init|=
literal|1
operator|+
call|(
name|int
call|)
argument_list|(
name|newCapacity
operator|*
name|loadFactor
argument_list|)
decl_stmt|;
name|int
index|[]
name|newTable
init|=
name|newTable
argument_list|(
name|newCapacity
argument_list|)
decl_stmt|;
name|long
index|[]
name|entries
init|=
name|this
operator|.
name|entries
decl_stmt|;
name|int
name|mask
init|=
name|newTable
operator|.
name|length
operator|-
literal|1
decl_stmt|;
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
condition|;
name|i
operator|++
control|)
block|{
name|long
name|oldEntry
init|=
name|entries
index|[
name|i
index|]
decl_stmt|;
name|int
name|hash
init|=
name|getHash
argument_list|(
name|oldEntry
argument_list|)
decl_stmt|;
name|int
name|tableIndex
init|=
name|hash
operator|&
name|mask
decl_stmt|;
name|int
name|next
init|=
name|newTable
index|[
name|tableIndex
index|]
decl_stmt|;
name|newTable
index|[
name|tableIndex
index|]
operator|=
name|i
expr_stmt|;
name|entries
index|[
name|i
index|]
operator|=
operator|(
operator|(
name|long
operator|)
name|hash
operator|<<
literal|32
operator|)
operator||
operator|(
name|NEXT_MASK
operator|&
name|next
operator|)
expr_stmt|;
block|}
name|this
operator|.
name|threshold
operator|=
name|newThreshold
expr_stmt|;
name|this
operator|.
name|table
operator|=
name|newTable
expr_stmt|;
block|}
DECL|method|indexOf (@ullableDecl Object key)
name|int
name|indexOf
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|key
parameter_list|)
block|{
name|int
name|hash
init|=
name|smearedHash
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|int
name|next
init|=
name|table
index|[
name|hash
operator|&
name|hashTableMask
argument_list|()
index|]
decl_stmt|;
while|while
condition|(
name|next
operator|!=
name|UNSET
condition|)
block|{
name|long
name|entry
init|=
name|entries
index|[
name|next
index|]
decl_stmt|;
if|if
condition|(
name|getHash
argument_list|(
name|entry
argument_list|)
operator|==
name|hash
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|key
argument_list|,
name|keys
index|[
name|next
index|]
argument_list|)
condition|)
block|{
return|return
name|next
return|;
block|}
name|next
operator|=
name|getNext
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
return|return
operator|-
literal|1
return|;
block|}
DECL|method|containsKey (@ullableDecl Object key)
specifier|public
name|boolean
name|containsKey
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|key
parameter_list|)
block|{
return|return
name|indexOf
argument_list|(
name|key
argument_list|)
operator|!=
operator|-
literal|1
return|;
block|}
DECL|method|get (@ullableDecl Object key)
specifier|public
name|int
name|get
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|key
parameter_list|)
block|{
name|int
name|index
init|=
name|indexOf
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|index
operator|==
operator|-
literal|1
operator|)
condition|?
literal|0
else|:
name|values
index|[
name|index
index|]
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
DECL|method|remove (@ullableDecl Object key)
specifier|public
name|int
name|remove
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|key
parameter_list|)
block|{
return|return
name|remove
argument_list|(
name|key
argument_list|,
name|smearedHash
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
DECL|method|removeEntry (int entryIndex)
name|int
name|removeEntry
parameter_list|(
name|int
name|entryIndex
parameter_list|)
block|{
return|return
name|remove
argument_list|(
name|keys
index|[
name|entryIndex
index|]
argument_list|,
name|getHash
argument_list|(
name|entries
index|[
name|entryIndex
index|]
argument_list|)
argument_list|)
return|;
block|}
DECL|method|remove (@ullableDecl Object key, int hash)
specifier|private
name|int
name|remove
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|key
parameter_list|,
name|int
name|hash
parameter_list|)
block|{
name|int
name|tableIndex
init|=
name|hash
operator|&
name|hashTableMask
argument_list|()
decl_stmt|;
name|int
name|next
init|=
name|table
index|[
name|tableIndex
index|]
decl_stmt|;
if|if
condition|(
name|next
operator|==
name|UNSET
condition|)
block|{
comment|// empty bucket
return|return
literal|0
return|;
block|}
name|int
name|last
init|=
name|UNSET
decl_stmt|;
do|do
block|{
if|if
condition|(
name|getHash
argument_list|(
name|entries
index|[
name|next
index|]
argument_list|)
operator|==
name|hash
condition|)
block|{
if|if
condition|(
name|Objects
operator|.
name|equal
argument_list|(
name|key
argument_list|,
name|keys
index|[
name|next
index|]
argument_list|)
condition|)
block|{
name|int
name|oldValue
init|=
name|values
index|[
name|next
index|]
decl_stmt|;
if|if
condition|(
name|last
operator|==
name|UNSET
condition|)
block|{
comment|// we need to update the root link from table[]
name|table
index|[
name|tableIndex
index|]
operator|=
name|getNext
argument_list|(
name|entries
index|[
name|next
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// we need to update the link from the chain
name|entries
index|[
name|last
index|]
operator|=
name|swapNext
argument_list|(
name|entries
index|[
name|last
index|]
argument_list|,
name|getNext
argument_list|(
name|entries
index|[
name|next
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|moveLastEntry
argument_list|(
name|next
argument_list|)
expr_stmt|;
name|size
operator|--
expr_stmt|;
name|modCount
operator|++
expr_stmt|;
return|return
name|oldValue
return|;
block|}
block|}
name|last
operator|=
name|next
expr_stmt|;
name|next
operator|=
name|getNext
argument_list|(
name|entries
index|[
name|next
index|]
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|next
operator|!=
name|UNSET
condition|)
do|;
return|return
literal|0
return|;
block|}
comment|/**    * Moves the last entry in the entry array into {@code dstIndex}, and nulls out its old position.    */
DECL|method|moveLastEntry (int dstIndex)
name|void
name|moveLastEntry
parameter_list|(
name|int
name|dstIndex
parameter_list|)
block|{
name|int
name|srcIndex
init|=
name|size
argument_list|()
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|dstIndex
operator|<
name|srcIndex
condition|)
block|{
comment|// move last entry to deleted spot
name|keys
index|[
name|dstIndex
index|]
operator|=
name|keys
index|[
name|srcIndex
index|]
expr_stmt|;
name|values
index|[
name|dstIndex
index|]
operator|=
name|values
index|[
name|srcIndex
index|]
expr_stmt|;
name|keys
index|[
name|srcIndex
index|]
operator|=
literal|null
expr_stmt|;
name|values
index|[
name|srcIndex
index|]
operator|=
literal|0
expr_stmt|;
comment|// move the last entry to the removed spot, just like we moved the element
name|long
name|lastEntry
init|=
name|entries
index|[
name|srcIndex
index|]
decl_stmt|;
name|entries
index|[
name|dstIndex
index|]
operator|=
name|lastEntry
expr_stmt|;
name|entries
index|[
name|srcIndex
index|]
operator|=
name|UNSET
expr_stmt|;
comment|// also need to update whoever's "next" pointer was pointing to the last entry place
comment|// reusing "tableIndex" and "next"; these variables were no longer needed
name|int
name|tableIndex
init|=
name|getHash
argument_list|(
name|lastEntry
argument_list|)
operator|&
name|hashTableMask
argument_list|()
decl_stmt|;
name|int
name|lastNext
init|=
name|table
index|[
name|tableIndex
index|]
decl_stmt|;
if|if
condition|(
name|lastNext
operator|==
name|srcIndex
condition|)
block|{
comment|// we need to update the root pointer
name|table
index|[
name|tableIndex
index|]
operator|=
name|dstIndex
expr_stmt|;
block|}
else|else
block|{
comment|// we need to update a pointer in an entry
name|int
name|previous
decl_stmt|;
name|long
name|entry
decl_stmt|;
do|do
block|{
name|previous
operator|=
name|lastNext
expr_stmt|;
name|lastNext
operator|=
name|getNext
argument_list|(
name|entry
operator|=
name|entries
index|[
name|lastNext
index|]
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|lastNext
operator|!=
name|srcIndex
condition|)
do|;
comment|// here, entries[previous] points to the old entry location; update it
name|entries
index|[
name|previous
index|]
operator|=
name|swapNext
argument_list|(
name|entry
argument_list|,
name|dstIndex
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|keys
index|[
name|dstIndex
index|]
operator|=
literal|null
expr_stmt|;
name|values
index|[
name|dstIndex
index|]
operator|=
literal|0
expr_stmt|;
name|entries
index|[
name|dstIndex
index|]
operator|=
name|UNSET
expr_stmt|;
block|}
block|}
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|modCount
operator|++
expr_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|keys
argument_list|,
literal|0
argument_list|,
name|size
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|values
argument_list|,
literal|0
argument_list|,
name|size
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|table
argument_list|,
name|UNSET
argument_list|)
expr_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|entries
argument_list|,
name|UNSET
argument_list|)
expr_stmt|;
name|this
operator|.
name|size
operator|=
literal|0
expr_stmt|;
block|}
block|}
end_class

end_unit

