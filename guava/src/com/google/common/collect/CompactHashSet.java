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
name|AbstractSet
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
name|Collections
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
name|NoSuchElementException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Spliterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Spliterators
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
name|Consumer
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
name|MonotonicNonNullDecl
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
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * CompactHashSet is an implementation of a Set. All optional operations (adding and removing) are  * supported. The elements can be any objects.  *  *<p>{@code contains(x)}, {@code add(x)} and {@code remove(x)}, are all (expected and amortized)  * constant time operations. Expected in the hashtable sense (depends on the hash function doing a  * good job of distributing the elements to the buckets to a distribution not far from uniform), and  * amortized since some operations can trigger a hash table resize.  *  *<p>Unlike {@code java.util.HashSet}, iteration is only proportional to the actual {@code size()},  * which is optimal, and<i>not</i> the size of the internal hashtable, which could be much larger  * than {@code size()}. Furthermore, this structure only depends on a fixed number of arrays; {@code  * add(x)} operations<i>do not</i> create objects for the garbage collector to deal with, and for  * every element added, the garbage collector will have to traverse {@code 1.5} references on  * average, in the marking phase, not {@code 5.0} as in {@code java.util.HashSet}.  *  *<p>If there are no removals, then {@link #iterator iteration} order is the same as insertion  * order. Any removal invalidates any ordering guarantees.  *  *<p>This class should not be assumed to be universally superior to {@code java.util.HashSet}.  * Generally speaking, this class reduces object allocation and memory consumption at the price of  * moderately increased constant factors of CPU.  Only use this class when there is a specific  * reason to prioritize memory over CPU.  *  * @author Dimitris Andreou  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
comment|// not worth using in GWT for now
DECL|class|CompactHashSet
class|class
name|CompactHashSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractSet
argument_list|<
name|E
argument_list|>
implements|implements
name|Serializable
block|{
comment|// TODO(user): cache all field accesses in local vars
comment|/** Creates an empty {@code CompactHashSet} instance. */
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|CompactHashSet
argument_list|<
name|E
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|CompactHashSet
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Creates a<i>mutable</i> {@code CompactHashSet} instance containing the elements of the given    * collection in unspecified order.    *    * @param collection the elements that the set should contain    * @return a new {@code CompactHashSet} containing those elements (minus duplicates)    */
DECL|method|create (Collection<? extends E> collection)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|CompactHashSet
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|collection
parameter_list|)
block|{
name|CompactHashSet
argument_list|<
name|E
argument_list|>
name|set
init|=
name|createWithExpectedSize
argument_list|(
name|collection
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|set
operator|.
name|addAll
argument_list|(
name|collection
argument_list|)
expr_stmt|;
return|return
name|set
return|;
block|}
comment|/**    * Creates a<i>mutable</i> {@code CompactHashSet} instance containing the given elements in    * unspecified order.    *    * @param elements the elements that the set should contain    * @return a new {@code CompactHashSet} containing those elements (minus duplicates)    */
DECL|method|create (E... elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|CompactHashSet
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|E
modifier|...
name|elements
parameter_list|)
block|{
name|CompactHashSet
argument_list|<
name|E
argument_list|>
name|set
init|=
name|createWithExpectedSize
argument_list|(
name|elements
operator|.
name|length
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|set
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|set
return|;
block|}
comment|/**    * Creates a {@code CompactHashSet} instance, with a high enough "initial capacity" that it    *<i>should</i> hold {@code expectedSize} elements without growth.    *    * @param expectedSize the number of elements you expect to add to the returned set    * @return a new, empty {@code CompactHashSet} with enough capacity to hold {@code expectedSize}    *     elements without resizing    * @throws IllegalArgumentException if {@code expectedSize} is negative    */
DECL|method|createWithExpectedSize (int expectedSize)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|CompactHashSet
argument_list|<
name|E
argument_list|>
name|createWithExpectedSize
parameter_list|(
name|int
name|expectedSize
parameter_list|)
block|{
return|return
operator|new
name|CompactHashSet
argument_list|<
name|E
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
comment|// TODO(user): decide, and inline, load factor. 0.75?
DECL|field|DEFAULT_LOAD_FACTOR
specifier|private
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
comment|// TODO(user): decide default size
DECL|field|DEFAULT_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_SIZE
init|=
literal|3
decl_stmt|;
DECL|field|UNSET
specifier|static
specifier|final
name|int
name|UNSET
init|=
operator|-
literal|1
decl_stmt|;
comment|/**    * The hashtable. Its values are indexes to both the elements and entries arrays.    *    *<p>Currently, the UNSET value means "null pointer", and any non negative value x is the actual    * index.    *    *<p>Its size must be a power of two.    */
DECL|field|table
annotation|@
name|MonotonicNonNullDecl
specifier|private
specifier|transient
name|int
index|[]
name|table
decl_stmt|;
comment|/**    * Contains the logical entries, in the range of [0, size()). The high 32 bits of each long is the    * smeared hash of the element, whereas the low 32 bits is the "next" pointer (pointing to the    * next entry in the bucket chain). The pointers in [size(), entries.length) are all "null"    * (UNSET).    */
DECL|field|entries
annotation|@
name|MonotonicNonNullDecl
specifier|private
specifier|transient
name|long
index|[]
name|entries
decl_stmt|;
comment|/** The elements contained in the set, in the range of [0, size()). */
DECL|field|elements
annotation|@
name|MonotonicNonNullDecl
specifier|transient
name|Object
index|[]
name|elements
decl_stmt|;
comment|/** The load factor. */
DECL|field|loadFactor
specifier|transient
name|float
name|loadFactor
decl_stmt|;
comment|/**    * Keeps track of modifications of this set, to make it possible to throw    * ConcurrentModificationException in the iterator. Note that we choose not to make this volatile,    * so we do less of a "best effort" to track such errors, for better performance.    */
DECL|field|modCount
specifier|transient
name|int
name|modCount
decl_stmt|;
comment|/** When we have this many elements, resize the hashtable. */
DECL|field|threshold
specifier|private
specifier|transient
name|int
name|threshold
decl_stmt|;
comment|/** The number of elements contained in the set. */
DECL|field|size
specifier|private
specifier|transient
name|int
name|size
decl_stmt|;
comment|/** Constructs a new empty instance of {@code CompactHashSet}. */
DECL|method|CompactHashSet ()
name|CompactHashSet
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
comment|/**    * Constructs a new instance of {@code CompactHashSet} with the specified capacity.    *    * @param expectedSize the initial capacity of this {@code CompactHashSet}.    */
DECL|method|CompactHashSet (int expectedSize)
name|CompactHashSet
parameter_list|(
name|int
name|expectedSize
parameter_list|)
block|{
name|init
argument_list|(
name|expectedSize
argument_list|,
name|DEFAULT_LOAD_FACTOR
argument_list|)
expr_stmt|;
block|}
comment|/** Pseudoconstructor for serialization support. */
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
name|elements
operator|=
operator|new
name|Object
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
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|add (@ullable E object)
specifier|public
name|boolean
name|add
parameter_list|(
annotation|@
name|Nullable
name|E
name|object
parameter_list|)
block|{
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
name|elements
init|=
name|this
operator|.
name|elements
decl_stmt|;
name|int
name|hash
init|=
name|smearedHash
argument_list|(
name|object
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
comment|// uninitialized bucket
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
name|object
argument_list|,
name|elements
index|[
name|next
index|]
argument_list|)
condition|)
block|{
return|return
literal|false
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
name|object
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
literal|true
return|;
block|}
comment|/**    * Creates a fresh entry with the specified object at the specified position in the entry arrays.    */
DECL|method|insertEntry (int entryIndex, E object, int hash)
name|void
name|insertEntry
parameter_list|(
name|int
name|entryIndex
parameter_list|,
name|E
name|object
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
name|elements
index|[
name|entryIndex
index|]
operator|=
name|object
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
name|elements
operator|=
name|Arrays
operator|.
name|copyOf
argument_list|(
name|elements
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
name|oldSize
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
name|oldSize
condition|)
block|{
name|Arrays
operator|.
name|fill
argument_list|(
name|entries
argument_list|,
name|oldSize
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
annotation|@
name|Override
DECL|method|contains (@ullable Object object)
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
name|int
name|hash
init|=
name|smearedHash
argument_list|(
name|object
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
name|object
argument_list|,
name|elements
index|[
name|next
index|]
argument_list|)
condition|)
block|{
return|return
literal|true
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
literal|false
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|remove (@ullable Object object)
specifier|public
name|boolean
name|remove
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
name|remove
argument_list|(
name|object
argument_list|,
name|smearedHash
argument_list|(
name|object
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
DECL|method|remove (Object object, int hash)
specifier|private
name|boolean
name|remove
parameter_list|(
name|Object
name|object
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
return|return
literal|false
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
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|object
argument_list|,
name|elements
index|[
name|next
index|]
argument_list|)
condition|)
block|{
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
name|moveEntry
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
literal|true
return|;
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
literal|false
return|;
block|}
comment|/**    * Moves the last entry in the entry array into {@code dstIndex}, and nulls out its old position.    */
DECL|method|moveEntry (int dstIndex)
name|void
name|moveEntry
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
name|elements
index|[
name|dstIndex
index|]
operator|=
name|elements
index|[
name|srcIndex
index|]
expr_stmt|;
name|elements
index|[
name|srcIndex
index|]
operator|=
literal|null
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
name|elements
index|[
name|dstIndex
index|]
operator|=
literal|null
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
DECL|method|firstEntryIndex ()
name|int
name|firstEntryIndex
parameter_list|()
block|{
return|return
name|isEmpty
argument_list|()
condition|?
operator|-
literal|1
else|:
literal|0
return|;
block|}
DECL|method|getSuccessor (int entryIndex)
name|int
name|getSuccessor
parameter_list|(
name|int
name|entryIndex
parameter_list|)
block|{
return|return
operator|(
name|entryIndex
operator|+
literal|1
operator|<
name|size
operator|)
condition|?
name|entryIndex
operator|+
literal|1
else|:
operator|-
literal|1
return|;
block|}
comment|/**    * Updates the index an iterator is pointing to after a call to remove: returns the index of the    * entry that should be looked at after a removal on indexRemoved, with indexBeforeRemove as the    * index that *was* the next entry that would be looked at.    */
DECL|method|adjustAfterRemove (int indexBeforeRemove, @SuppressWarnings(R) int indexRemoved)
name|int
name|adjustAfterRemove
parameter_list|(
name|int
name|indexBeforeRemove
parameter_list|,
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|int
name|indexRemoved
parameter_list|)
block|{
return|return
name|indexBeforeRemove
operator|-
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|Iterator
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
name|int
name|expectedModCount
init|=
name|modCount
decl_stmt|;
name|int
name|index
init|=
name|firstEntryIndex
argument_list|()
decl_stmt|;
name|int
name|indexToRemove
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|index
operator|>=
literal|0
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|E
name|next
parameter_list|()
block|{
name|checkForConcurrentModification
argument_list|()
expr_stmt|;
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
name|indexToRemove
operator|=
name|index
expr_stmt|;
name|E
name|result
init|=
operator|(
name|E
operator|)
name|elements
index|[
name|index
index|]
decl_stmt|;
name|index
operator|=
name|getSuccessor
argument_list|(
name|index
argument_list|)
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
name|checkForConcurrentModification
argument_list|()
expr_stmt|;
name|checkRemove
argument_list|(
name|indexToRemove
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|expectedModCount
operator|++
expr_stmt|;
name|CompactHashSet
operator|.
name|this
operator|.
name|remove
argument_list|(
name|elements
index|[
name|indexToRemove
index|]
argument_list|,
name|getHash
argument_list|(
name|entries
index|[
name|indexToRemove
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|index
operator|=
name|adjustAfterRemove
argument_list|(
name|index
argument_list|,
name|indexToRemove
argument_list|)
expr_stmt|;
name|indexToRemove
operator|=
operator|-
literal|1
expr_stmt|;
block|}
specifier|private
name|void
name|checkForConcurrentModification
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
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|spliterator ()
specifier|public
name|Spliterator
argument_list|<
name|E
argument_list|>
name|spliterator
parameter_list|()
block|{
return|return
name|Spliterators
operator|.
name|spliterator
argument_list|(
name|elements
argument_list|,
literal|0
argument_list|,
name|size
argument_list|,
name|Spliterator
operator|.
name|DISTINCT
operator||
name|Spliterator
operator|.
name|ORDERED
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|forEach (Consumer<? super E> action)
specifier|public
name|void
name|forEach
parameter_list|(
name|Consumer
argument_list|<
name|?
super|super
name|E
argument_list|>
name|action
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|action
argument_list|)
expr_stmt|;
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
name|action
operator|.
name|accept
argument_list|(
operator|(
name|E
operator|)
name|elements
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
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
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|size
operator|==
literal|0
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
name|Arrays
operator|.
name|copyOf
argument_list|(
name|elements
argument_list|,
name|size
argument_list|)
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|toArray (T[] a)
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
name|a
parameter_list|)
block|{
return|return
name|ObjectArrays
operator|.
name|toArrayImpl
argument_list|(
name|elements
argument_list|,
literal|0
argument_list|,
name|size
argument_list|,
name|a
argument_list|)
return|;
block|}
comment|/**    * Ensures that this {@code CompactHashSet} has the smallest representation in memory, given its    * current size.    */
DECL|method|trimToSize ()
specifier|public
name|void
name|trimToSize
parameter_list|()
block|{
name|int
name|size
init|=
name|this
operator|.
name|size
decl_stmt|;
if|if
condition|(
name|size
operator|<
name|entries
operator|.
name|length
condition|)
block|{
name|resizeEntries
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
comment|// size / loadFactor gives the table size of the appropriate load factor,
comment|// but that may not be a power of two. We floor it to a power of two by
comment|// keeping its highest bit. But the smaller table may have a load factor
comment|// larger than what we want; then we want to go to the next power of 2 if we can
name|int
name|minimumTableSize
init|=
name|Math
operator|.
name|max
argument_list|(
literal|1
argument_list|,
name|Integer
operator|.
name|highestOneBit
argument_list|(
call|(
name|int
call|)
argument_list|(
name|size
operator|/
name|loadFactor
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|minimumTableSize
operator|<
name|MAXIMUM_CAPACITY
condition|)
block|{
name|double
name|load
init|=
operator|(
name|double
operator|)
name|size
operator|/
name|minimumTableSize
decl_stmt|;
if|if
condition|(
name|load
operator|>
name|loadFactor
condition|)
block|{
name|minimumTableSize
operator|<<=
literal|1
expr_stmt|;
comment|// increase to next power if possible
block|}
block|}
if|if
condition|(
name|minimumTableSize
operator|<
name|table
operator|.
name|length
condition|)
block|{
name|resizeTable
argument_list|(
name|minimumTableSize
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
name|modCount
operator|++
expr_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|elements
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
comment|/**    * The serial form currently mimics Android's java.util.HashSet version, e.g. see    * http://omapzoom.org/?p=platform/libcore.git;a=blob;f=luni/src/main/java/java/util/HashSet.java    */
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
name|size
argument_list|)
expr_stmt|;
for|for
control|(
name|E
name|e
range|:
name|this
control|)
block|{
name|stream
operator|.
name|writeObject
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
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
name|init
argument_list|(
name|DEFAULT_SIZE
argument_list|,
name|DEFAULT_LOAD_FACTOR
argument_list|)
expr_stmt|;
name|int
name|elementCount
init|=
name|stream
operator|.
name|readInt
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|elementCount
init|;
operator|--
name|i
operator|>=
literal|0
condition|;
control|)
block|{
name|E
name|element
init|=
operator|(
name|E
operator|)
name|stream
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

