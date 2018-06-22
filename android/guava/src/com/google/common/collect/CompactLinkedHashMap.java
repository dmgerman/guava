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
name|MonotonicNonNullDecl
import|;
end_import

begin_comment
comment|/**  * CompactLinkedHashMap is an implementation of a Map with insertion or LRU iteration order,  * maintained with a doubly linked list through the entries. All optional operations (put and  * remove) are supported. Null keys and values are supported.  *  *<p>{@code containsKey(k)}, {@code put(k, v)} and {@code remove(k)} are all (expected and  * amortized) constant time operations. Expected in the hashtable sense (depends on the hash  * function doing a good job of distributing the elements to the buckets to a distribution not far  * from uniform), and amortized since some operations can trigger a hash table resize.  *  *<p>As compared with {@link java.util.LinkedHashMap}, this structure places significantly reduced  * load on the garbage collector by only using a constant number of internal objects.  *  *<p>This class should not be assumed to be universally superior to {@code  * java.util.LinkedHashMap}. Generally speaking, this class reduces object allocation and memory  * consumption at the price of moderately increased constant factors of CPU. Only use this class  * when there is a specific reason to prioritize memory over CPU.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
comment|// not worth using in GWT for now
DECL|class|CompactLinkedHashMap
class|class
name|CompactLinkedHashMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|CompactHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|// TODO(lowasser): implement removeEldestEntry so this can be used as a drop-in replacement
comment|/**    * Creates an empty {@code CompactLinkedHashMap} instance.    */
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|CompactLinkedHashMap
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
name|CompactLinkedHashMap
argument_list|<>
argument_list|()
return|;
block|}
comment|/**    * Creates a {@code CompactLinkedHashMap} instance, with a high enough "initial capacity"    * that it<i>should</i> hold {@code expectedSize} elements without growth.    *    * @param expectedSize the number of elements you expect to add to the returned set    * @return a new, empty {@code CompactLinkedHashMap} with enough capacity to hold {@code    *         expectedSize} elements without resizing    * @throws IllegalArgumentException if {@code expectedSize} is negative    */
DECL|method|createWithExpectedSize (int expectedSize)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|CompactLinkedHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|createWithExpectedSize
parameter_list|(
name|int
name|expectedSize
parameter_list|)
block|{
return|return
operator|new
name|CompactLinkedHashMap
argument_list|<>
argument_list|(
name|expectedSize
argument_list|)
return|;
block|}
DECL|field|ENDPOINT
specifier|private
specifier|static
specifier|final
name|int
name|ENDPOINT
init|=
operator|-
literal|2
decl_stmt|;
comment|/**    * Contains the link pointers corresponding with the entries, in the range of [0, size()). The    * high 32 bits of each long is the "prev" pointer, whereas the low 32 bits is the "succ" pointer    * (pointing to the next entry in the linked list). The pointers in [size(), entries.length) are    * all "null" (UNSET).    *    *<p>A node with "prev" pointer equal to {@code ENDPOINT} is the first node in the linked list,    * and a node with "next" pointer equal to {@code ENDPOINT} is the last node.    */
DECL|field|links
annotation|@
name|VisibleForTesting
annotation|@
name|MonotonicNonNullDecl
specifier|transient
name|long
index|[]
name|links
decl_stmt|;
comment|/** Pointer to the first node in the linked list, or {@code ENDPOINT} if there are no entries. */
DECL|field|firstEntry
specifier|private
specifier|transient
name|int
name|firstEntry
decl_stmt|;
comment|/**    * Pointer to the last node in the linked list, or {@code ENDPOINT} if there are no entries.    */
DECL|field|lastEntry
specifier|private
specifier|transient
name|int
name|lastEntry
decl_stmt|;
DECL|field|accessOrder
specifier|private
specifier|final
name|boolean
name|accessOrder
decl_stmt|;
DECL|method|CompactLinkedHashMap ()
name|CompactLinkedHashMap
parameter_list|()
block|{
name|this
argument_list|(
name|DEFAULT_SIZE
argument_list|)
expr_stmt|;
block|}
DECL|method|CompactLinkedHashMap (int expectedSize)
name|CompactLinkedHashMap
parameter_list|(
name|int
name|expectedSize
parameter_list|)
block|{
name|this
argument_list|(
name|expectedSize
argument_list|,
name|DEFAULT_LOAD_FACTOR
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|CompactLinkedHashMap (int expectedSize, float loadFactor, boolean accessOrder)
name|CompactLinkedHashMap
parameter_list|(
name|int
name|expectedSize
parameter_list|,
name|float
name|loadFactor
parameter_list|,
name|boolean
name|accessOrder
parameter_list|)
block|{
name|super
argument_list|(
name|expectedSize
argument_list|,
name|loadFactor
argument_list|)
expr_stmt|;
name|this
operator|.
name|accessOrder
operator|=
name|accessOrder
expr_stmt|;
block|}
annotation|@
name|Override
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
name|super
operator|.
name|init
argument_list|(
name|expectedSize
argument_list|,
name|loadFactor
argument_list|)
expr_stmt|;
name|firstEntry
operator|=
name|ENDPOINT
expr_stmt|;
name|lastEntry
operator|=
name|ENDPOINT
expr_stmt|;
name|links
operator|=
operator|new
name|long
index|[
name|expectedSize
index|]
expr_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|links
argument_list|,
name|UNSET
argument_list|)
expr_stmt|;
block|}
DECL|method|getPredecessor (int entry)
specifier|private
name|int
name|getPredecessor
parameter_list|(
name|int
name|entry
parameter_list|)
block|{
return|return
call|(
name|int
call|)
argument_list|(
name|links
index|[
name|entry
index|]
operator|>>>
literal|32
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getSuccessor (int entry)
name|int
name|getSuccessor
parameter_list|(
name|int
name|entry
parameter_list|)
block|{
return|return
operator|(
name|int
operator|)
name|links
index|[
name|entry
index|]
return|;
block|}
DECL|method|setSuccessor (int entry, int succ)
specifier|private
name|void
name|setSuccessor
parameter_list|(
name|int
name|entry
parameter_list|,
name|int
name|succ
parameter_list|)
block|{
name|long
name|succMask
init|=
operator|(
operator|~
literal|0L
operator|)
operator|>>>
literal|32
decl_stmt|;
name|links
index|[
name|entry
index|]
operator|=
operator|(
name|links
index|[
name|entry
index|]
operator|&
operator|~
name|succMask
operator|)
operator||
operator|(
name|succ
operator|&
name|succMask
operator|)
expr_stmt|;
block|}
DECL|method|setPredecessor (int entry, int pred)
specifier|private
name|void
name|setPredecessor
parameter_list|(
name|int
name|entry
parameter_list|,
name|int
name|pred
parameter_list|)
block|{
name|long
name|predMask
init|=
operator|~
literal|0L
operator|<<
literal|32
decl_stmt|;
name|links
index|[
name|entry
index|]
operator|=
operator|(
name|links
index|[
name|entry
index|]
operator|&
operator|~
name|predMask
operator|)
operator||
operator|(
operator|(
name|long
operator|)
name|pred
operator|<<
literal|32
operator|)
expr_stmt|;
block|}
DECL|method|setSucceeds (int pred, int succ)
specifier|private
name|void
name|setSucceeds
parameter_list|(
name|int
name|pred
parameter_list|,
name|int
name|succ
parameter_list|)
block|{
if|if
condition|(
name|pred
operator|==
name|ENDPOINT
condition|)
block|{
name|firstEntry
operator|=
name|succ
expr_stmt|;
block|}
else|else
block|{
name|setSuccessor
argument_list|(
name|pred
argument_list|,
name|succ
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|succ
operator|==
name|ENDPOINT
condition|)
block|{
name|lastEntry
operator|=
name|pred
expr_stmt|;
block|}
else|else
block|{
name|setPredecessor
argument_list|(
name|succ
argument_list|,
name|pred
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|insertEntry (int entryIndex, K key, V value, int hash)
name|void
name|insertEntry
parameter_list|(
name|int
name|entryIndex
parameter_list|,
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|,
name|int
name|hash
parameter_list|)
block|{
name|super
operator|.
name|insertEntry
argument_list|(
name|entryIndex
argument_list|,
name|key
argument_list|,
name|value
argument_list|,
name|hash
argument_list|)
expr_stmt|;
name|setSucceeds
argument_list|(
name|lastEntry
argument_list|,
name|entryIndex
argument_list|)
expr_stmt|;
name|setSucceeds
argument_list|(
name|entryIndex
argument_list|,
name|ENDPOINT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|accessEntry (int index)
name|void
name|accessEntry
parameter_list|(
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|accessOrder
condition|)
block|{
comment|// delete from previous position...
name|setSucceeds
argument_list|(
name|getPredecessor
argument_list|(
name|index
argument_list|)
argument_list|,
name|getSuccessor
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
comment|// ...and insert at the end.
name|setSucceeds
argument_list|(
name|lastEntry
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|setSucceeds
argument_list|(
name|index
argument_list|,
name|ENDPOINT
argument_list|)
expr_stmt|;
name|modCount
operator|++
expr_stmt|;
block|}
block|}
annotation|@
name|Override
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
name|setSucceeds
argument_list|(
name|getPredecessor
argument_list|(
name|dstIndex
argument_list|)
argument_list|,
name|getSuccessor
argument_list|(
name|dstIndex
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|dstIndex
operator|<
name|srcIndex
condition|)
block|{
name|setSucceeds
argument_list|(
name|getPredecessor
argument_list|(
name|srcIndex
argument_list|)
argument_list|,
name|dstIndex
argument_list|)
expr_stmt|;
name|setSucceeds
argument_list|(
name|dstIndex
argument_list|,
name|getSuccessor
argument_list|(
name|srcIndex
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|moveLastEntry
argument_list|(
name|dstIndex
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resizeEntries (int newCapacity)
name|void
name|resizeEntries
parameter_list|(
name|int
name|newCapacity
parameter_list|)
block|{
name|super
operator|.
name|resizeEntries
argument_list|(
name|newCapacity
argument_list|)
expr_stmt|;
name|links
operator|=
name|Arrays
operator|.
name|copyOf
argument_list|(
name|links
argument_list|,
name|newCapacity
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|firstEntryIndex ()
name|int
name|firstEntryIndex
parameter_list|()
block|{
return|return
name|firstEntry
return|;
block|}
annotation|@
name|Override
DECL|method|adjustAfterRemove (int indexBeforeRemove, int indexRemoved)
name|int
name|adjustAfterRemove
parameter_list|(
name|int
name|indexBeforeRemove
parameter_list|,
name|int
name|indexRemoved
parameter_list|)
block|{
return|return
operator|(
name|indexBeforeRemove
operator|>=
name|size
argument_list|()
operator|)
condition|?
name|indexRemoved
else|:
name|indexBeforeRemove
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
name|this
operator|.
name|firstEntry
operator|=
name|ENDPOINT
expr_stmt|;
name|this
operator|.
name|lastEntry
operator|=
name|ENDPOINT
expr_stmt|;
block|}
block|}
end_class

end_unit

