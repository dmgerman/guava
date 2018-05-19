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
name|checkNonnegative
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
name|primitives
operator|.
name|Ints
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
name|InvalidObjectException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectStreamException
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
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|ObjIntConsumer
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
name|MonotonicNonNull
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
comment|/**  * Basic implementation of {@code Multiset<E>} backed by an instance of {@code Map<E, Count>}.  *  *<p>For serialization to work, the subclass must specify explicit {@code readObject} and {@code  * writeObject} methods.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|AbstractMapBasedMultiset
specifier|abstract
class|class
name|AbstractMapBasedMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractMultiset
argument_list|<
name|E
argument_list|>
implements|implements
name|Serializable
block|{
comment|// TODO(lowasser): consider overhauling this back to Map<E, Integer>
DECL|field|backingMap
specifier|private
specifier|transient
name|Map
argument_list|<
name|E
argument_list|,
name|Count
argument_list|>
name|backingMap
decl_stmt|;
comment|/*    * Cache the size for efficiency. Using a long lets us avoid the need for    * overflow checking and ensures that size() will function correctly even if    * the multiset had once been larger than Integer.MAX_VALUE.    */
DECL|field|size
specifier|private
specifier|transient
name|long
name|size
decl_stmt|;
comment|/** Standard constructor. */
DECL|method|AbstractMapBasedMultiset (Map<E, Count> backingMap)
specifier|protected
name|AbstractMapBasedMultiset
parameter_list|(
name|Map
argument_list|<
name|E
argument_list|,
name|Count
argument_list|>
name|backingMap
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|backingMap
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|backingMap
operator|=
name|backingMap
expr_stmt|;
block|}
comment|/** Used during deserialization only. The backing map must be empty. */
DECL|method|setBackingMap (Map<E, Count> backingMap)
name|void
name|setBackingMap
parameter_list|(
name|Map
argument_list|<
name|E
argument_list|,
name|Count
argument_list|>
name|backingMap
parameter_list|)
block|{
name|this
operator|.
name|backingMap
operator|=
name|backingMap
expr_stmt|;
block|}
comment|// Required Implementations
comment|/**    * {@inheritDoc}    *    *<p>Invoking {@link Multiset.Entry#getCount} on an entry in the returned set always returns the    * current count of that element in the multiset, as opposed to the count at the time the entry    * was retrieved.    */
annotation|@
name|Override
DECL|method|entrySet ()
specifier|public
name|Set
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
name|super
operator|.
name|entrySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|elementIterator ()
name|Iterator
argument_list|<
name|E
argument_list|>
name|elementIterator
parameter_list|()
block|{
specifier|final
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|E
argument_list|,
name|Count
argument_list|>
argument_list|>
name|backingEntries
init|=
name|backingMap
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
operator|new
name|Iterator
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
name|Map
operator|.
expr|@
name|Nullable
name|Entry
argument_list|<
name|E
argument_list|,
name|Count
argument_list|>
name|toRemove
expr_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|backingEntries
operator|.
name|hasNext
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|E
name|next
parameter_list|()
block|{
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|E
argument_list|,
name|Count
argument_list|>
name|mapEntry
init|=
name|backingEntries
operator|.
name|next
argument_list|()
decl_stmt|;
name|toRemove
operator|=
name|mapEntry
expr_stmt|;
return|return
name|mapEntry
operator|.
name|getKey
argument_list|()
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
name|toRemove
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|size
operator|-=
name|toRemove
operator|.
name|getValue
argument_list|()
operator|.
name|getAndSet
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|backingEntries
operator|.
name|remove
argument_list|()
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
DECL|method|entryIterator ()
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entryIterator
parameter_list|()
block|{
specifier|final
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|E
argument_list|,
name|Count
argument_list|>
argument_list|>
name|backingEntries
init|=
name|backingMap
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
operator|new
name|Iterator
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
block|{
name|Map
operator|.
expr|@
name|Nullable
name|Entry
argument_list|<
name|E
argument_list|,
name|Count
argument_list|>
name|toRemove
expr_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|backingEntries
operator|.
name|hasNext
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
name|next
parameter_list|()
block|{
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|E
argument_list|,
name|Count
argument_list|>
name|mapEntry
init|=
name|backingEntries
operator|.
name|next
argument_list|()
decl_stmt|;
name|toRemove
operator|=
name|mapEntry
expr_stmt|;
return|return
operator|new
name|Multisets
operator|.
name|AbstractEntry
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|E
name|getElement
parameter_list|()
block|{
return|return
name|mapEntry
operator|.
name|getKey
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getCount
parameter_list|()
block|{
name|Count
name|count
init|=
name|mapEntry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|count
operator|==
literal|null
operator|||
name|count
operator|.
name|get
argument_list|()
operator|==
literal|0
condition|)
block|{
name|Count
name|frequency
init|=
name|backingMap
operator|.
name|get
argument_list|(
name|getElement
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|frequency
operator|!=
literal|null
condition|)
block|{
return|return
name|frequency
operator|.
name|get
argument_list|()
return|;
block|}
block|}
return|return
operator|(
name|count
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|count
operator|.
name|get
argument_list|()
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
name|toRemove
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|size
operator|-=
name|toRemove
operator|.
name|getValue
argument_list|()
operator|.
name|getAndSet
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|backingEntries
operator|.
name|remove
argument_list|()
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
DECL|method|forEachEntry (ObjIntConsumer<? super E> action)
specifier|public
name|void
name|forEachEntry
parameter_list|(
name|ObjIntConsumer
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
name|backingMap
operator|.
name|forEach
argument_list|(
parameter_list|(
name|element
parameter_list|,
name|count
parameter_list|)
lambda|->
name|action
operator|.
name|accept
argument_list|(
name|element
argument_list|,
name|count
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
for|for
control|(
name|Count
name|frequency
range|:
name|backingMap
operator|.
name|values
argument_list|()
control|)
block|{
name|frequency
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|backingMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|size
operator|=
literal|0L
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|distinctElements ()
name|int
name|distinctElements
parameter_list|()
block|{
return|return
name|backingMap
operator|.
name|size
argument_list|()
return|;
block|}
comment|// Optimizations - Query Operations
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|Ints
operator|.
name|saturatedCast
argument_list|(
name|size
argument_list|)
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
name|MapBasedMultisetIterator
argument_list|()
return|;
block|}
comment|/*    * Not subclassing AbstractMultiset$MultisetIterator because next() needs to    * retrieve the Map.Entry<E, Count> entry, which can then be used for    * a more efficient remove() call.    */
DECL|class|MapBasedMultisetIterator
specifier|private
class|class
name|MapBasedMultisetIterator
implements|implements
name|Iterator
argument_list|<
name|E
argument_list|>
block|{
DECL|field|entryIterator
specifier|final
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|E
argument_list|,
name|Count
argument_list|>
argument_list|>
name|entryIterator
decl_stmt|;
DECL|field|currentEntry
name|Map
operator|.
expr|@
name|MonotonicNonNull
name|Entry
argument_list|<
name|E
argument_list|,
name|Count
argument_list|>
name|currentEntry
expr_stmt|;
DECL|field|occurrencesLeft
name|int
name|occurrencesLeft
decl_stmt|;
DECL|field|canRemove
name|boolean
name|canRemove
decl_stmt|;
DECL|method|MapBasedMultisetIterator ()
name|MapBasedMultisetIterator
parameter_list|()
block|{
name|this
operator|.
name|entryIterator
operator|=
name|backingMap
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|occurrencesLeft
operator|>
literal|0
operator|||
name|entryIterator
operator|.
name|hasNext
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|E
name|next
parameter_list|()
block|{
if|if
condition|(
name|occurrencesLeft
operator|==
literal|0
condition|)
block|{
name|currentEntry
operator|=
name|entryIterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|occurrencesLeft
operator|=
name|currentEntry
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
name|occurrencesLeft
operator|--
expr_stmt|;
name|canRemove
operator|=
literal|true
expr_stmt|;
return|return
name|currentEntry
operator|.
name|getKey
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|checkRemove
argument_list|(
name|canRemove
argument_list|)
expr_stmt|;
name|int
name|frequency
init|=
name|currentEntry
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|frequency
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|ConcurrentModificationException
argument_list|()
throw|;
block|}
if|if
condition|(
name|currentEntry
operator|.
name|getValue
argument_list|()
operator|.
name|addAndGet
argument_list|(
operator|-
literal|1
argument_list|)
operator|==
literal|0
condition|)
block|{
name|entryIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
name|size
operator|--
expr_stmt|;
name|canRemove
operator|=
literal|false
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|count (@ullable Object element)
specifier|public
name|int
name|count
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|)
block|{
name|Count
name|frequency
init|=
name|Maps
operator|.
name|safeGet
argument_list|(
name|backingMap
argument_list|,
name|element
argument_list|)
decl_stmt|;
return|return
operator|(
name|frequency
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|frequency
operator|.
name|get
argument_list|()
return|;
block|}
comment|// Optional Operations - Modification Operations
comment|/**    * {@inheritDoc}    *    * @throws IllegalArgumentException if the call would result in more than {@link    *     Integer#MAX_VALUE} occurrences of {@code element} in this multiset.    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|add (@ullable E element, int occurrences)
specifier|public
name|int
name|add
parameter_list|(
annotation|@
name|Nullable
name|E
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
if|if
condition|(
name|occurrences
operator|==
literal|0
condition|)
block|{
return|return
name|count
argument_list|(
name|element
argument_list|)
return|;
block|}
name|checkArgument
argument_list|(
name|occurrences
operator|>
literal|0
argument_list|,
literal|"occurrences cannot be negative: %s"
argument_list|,
name|occurrences
argument_list|)
expr_stmt|;
name|Count
name|frequency
init|=
name|backingMap
operator|.
name|get
argument_list|(
name|element
argument_list|)
decl_stmt|;
name|int
name|oldCount
decl_stmt|;
if|if
condition|(
name|frequency
operator|==
literal|null
condition|)
block|{
name|oldCount
operator|=
literal|0
expr_stmt|;
name|backingMap
operator|.
name|put
argument_list|(
name|element
argument_list|,
operator|new
name|Count
argument_list|(
name|occurrences
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|oldCount
operator|=
name|frequency
operator|.
name|get
argument_list|()
expr_stmt|;
name|long
name|newCount
init|=
operator|(
name|long
operator|)
name|oldCount
operator|+
operator|(
name|long
operator|)
name|occurrences
decl_stmt|;
name|checkArgument
argument_list|(
name|newCount
operator|<=
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
literal|"too many occurrences: %s"
argument_list|,
name|newCount
argument_list|)
expr_stmt|;
name|frequency
operator|.
name|add
argument_list|(
name|occurrences
argument_list|)
expr_stmt|;
block|}
name|size
operator|+=
name|occurrences
expr_stmt|;
return|return
name|oldCount
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|remove (@ullable Object element, int occurrences)
specifier|public
name|int
name|remove
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
if|if
condition|(
name|occurrences
operator|==
literal|0
condition|)
block|{
return|return
name|count
argument_list|(
name|element
argument_list|)
return|;
block|}
name|checkArgument
argument_list|(
name|occurrences
operator|>
literal|0
argument_list|,
literal|"occurrences cannot be negative: %s"
argument_list|,
name|occurrences
argument_list|)
expr_stmt|;
name|Count
name|frequency
init|=
name|backingMap
operator|.
name|get
argument_list|(
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|frequency
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
name|int
name|oldCount
init|=
name|frequency
operator|.
name|get
argument_list|()
decl_stmt|;
name|int
name|numberRemoved
decl_stmt|;
if|if
condition|(
name|oldCount
operator|>
name|occurrences
condition|)
block|{
name|numberRemoved
operator|=
name|occurrences
expr_stmt|;
block|}
else|else
block|{
name|numberRemoved
operator|=
name|oldCount
expr_stmt|;
name|backingMap
operator|.
name|remove
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
name|frequency
operator|.
name|add
argument_list|(
operator|-
name|numberRemoved
argument_list|)
expr_stmt|;
name|size
operator|-=
name|numberRemoved
expr_stmt|;
return|return
name|oldCount
return|;
block|}
comment|// Roughly a 33% performance improvement over AbstractMultiset.setCount().
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|setCount (@ullable E element, int count)
specifier|public
name|int
name|setCount
parameter_list|(
annotation|@
name|Nullable
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|checkNonnegative
argument_list|(
name|count
argument_list|,
literal|"count"
argument_list|)
expr_stmt|;
name|Count
name|existingCounter
decl_stmt|;
name|int
name|oldCount
decl_stmt|;
if|if
condition|(
name|count
operator|==
literal|0
condition|)
block|{
name|existingCounter
operator|=
name|backingMap
operator|.
name|remove
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|oldCount
operator|=
name|getAndSet
argument_list|(
name|existingCounter
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|existingCounter
operator|=
name|backingMap
operator|.
name|get
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|oldCount
operator|=
name|getAndSet
argument_list|(
name|existingCounter
argument_list|,
name|count
argument_list|)
expr_stmt|;
if|if
condition|(
name|existingCounter
operator|==
literal|null
condition|)
block|{
name|backingMap
operator|.
name|put
argument_list|(
name|element
argument_list|,
operator|new
name|Count
argument_list|(
name|count
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|size
operator|+=
operator|(
name|count
operator|-
name|oldCount
operator|)
expr_stmt|;
return|return
name|oldCount
return|;
block|}
DECL|method|getAndSet (@ullable Count i, int count)
specifier|private
specifier|static
name|int
name|getAndSet
parameter_list|(
annotation|@
name|Nullable
name|Count
name|i
parameter_list|,
name|int
name|count
parameter_list|)
block|{
if|if
condition|(
name|i
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|i
operator|.
name|getAndSet
argument_list|(
name|count
argument_list|)
return|;
block|}
comment|// Don't allow default serialization.
annotation|@
name|GwtIncompatible
comment|// java.io.ObjectStreamException
DECL|method|readObjectNoData ()
specifier|private
name|void
name|readObjectNoData
parameter_list|()
throws|throws
name|ObjectStreamException
block|{
throw|throw
operator|new
name|InvalidObjectException
argument_list|(
literal|"Stream data required"
argument_list|)
throw|;
block|}
annotation|@
name|GwtIncompatible
comment|// not needed in emulated source.
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|2250766705698539974L
decl_stmt|;
block|}
end_class

end_unit

