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
name|Comparator
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
name|Set
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
name|function
operator|.
name|Consumer
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
comment|/**  * An immutable sorted set with one or more elements. TODO(jlevy): Consider  * separate class for a single-element sorted set.  *  * @author Jared Levy  * @author Louis Wasserman  */
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
DECL|class|RegularImmutableSortedSet
specifier|final
class|class
name|RegularImmutableSortedSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
block|{
DECL|field|NATURAL_EMPTY_SET
specifier|static
specifier|final
name|RegularImmutableSortedSet
argument_list|<
name|Comparable
argument_list|>
name|NATURAL_EMPTY_SET
init|=
operator|new
name|RegularImmutableSortedSet
argument_list|<
name|Comparable
argument_list|>
argument_list|(
name|ImmutableList
operator|.
expr|<
name|Comparable
operator|>
name|of
argument_list|()
argument_list|,
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|elements
annotation|@
name|VisibleForTesting
specifier|final
specifier|transient
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|elements
decl_stmt|;
DECL|method|RegularImmutableSortedSet (ImmutableList<E> elements, Comparator<? super E> comparator)
name|RegularImmutableSortedSet
parameter_list|(
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|elements
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|)
block|{
name|super
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
name|this
operator|.
name|elements
operator|=
name|elements
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|elements
operator|.
name|iterator
argument_list|()
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// NavigableSet
annotation|@
name|Override
DECL|method|descendingIterator ()
specifier|public
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
name|descendingIterator
parameter_list|()
block|{
return|return
name|elements
operator|.
name|reverse
argument_list|()
operator|.
name|iterator
argument_list|()
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
name|asList
argument_list|()
operator|.
name|spliterator
argument_list|()
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
name|elements
operator|.
name|forEach
argument_list|(
name|action
argument_list|)
expr_stmt|;
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
name|elements
operator|.
name|size
argument_list|()
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
try|try
block|{
return|return
name|o
operator|!=
literal|null
operator|&&
name|unsafeBinarySearch
argument_list|(
name|o
argument_list|)
operator|>=
literal|0
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|containsAll (Collection<?> targets)
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|targets
parameter_list|)
block|{
comment|// TODO(jlevy): For optimal performance, use a binary search when
comment|// targets.size()< size() / log(size())
comment|// TODO(kevinb): see if we can share code with OrderedIterator after it
comment|// graduates from labs.
if|if
condition|(
name|targets
operator|instanceof
name|Multiset
condition|)
block|{
name|targets
operator|=
operator|(
operator|(
name|Multiset
argument_list|<
name|?
argument_list|>
operator|)
name|targets
operator|)
operator|.
name|elementSet
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|SortedIterables
operator|.
name|hasSameComparator
argument_list|(
name|comparator
argument_list|()
argument_list|,
name|targets
argument_list|)
operator|||
operator|(
name|targets
operator|.
name|size
argument_list|()
operator|<=
literal|1
operator|)
condition|)
block|{
return|return
name|super
operator|.
name|containsAll
argument_list|(
name|targets
argument_list|)
return|;
block|}
comment|/*      * If targets is a sorted set with the same comparator, containsAll can run      * in O(n) time stepping through the two collections.      */
name|Iterator
argument_list|<
name|E
argument_list|>
name|thisIterator
init|=
name|iterator
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|thatIterator
init|=
name|targets
operator|.
name|iterator
argument_list|()
decl_stmt|;
comment|// known nonempty since we checked targets.size()> 1
if|if
condition|(
operator|!
name|thisIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Object
name|target
init|=
name|thatIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|E
name|current
init|=
name|thisIterator
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|cmp
init|=
name|unsafeCompare
argument_list|(
name|current
argument_list|,
name|target
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmp
operator|<
literal|0
condition|)
block|{
if|if
condition|(
operator|!
name|thisIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|current
operator|=
name|thisIterator
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cmp
operator|==
literal|0
condition|)
block|{
if|if
condition|(
operator|!
name|thatIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|target
operator|=
name|thatIterator
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cmp
operator|>
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|unsafeBinarySearch (Object key)
specifier|private
name|int
name|unsafeBinarySearch
parameter_list|(
name|Object
name|key
parameter_list|)
throws|throws
name|ClassCastException
block|{
return|return
name|Collections
operator|.
name|binarySearch
argument_list|(
name|elements
argument_list|,
name|key
argument_list|,
name|unsafeComparator
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
name|elements
operator|.
name|isPartialView
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|copyIntoArray (Object[] dst, int offset)
name|int
name|copyIntoArray
parameter_list|(
name|Object
index|[]
name|dst
parameter_list|,
name|int
name|offset
parameter_list|)
block|{
return|return
name|elements
operator|.
name|copyIntoArray
argument_list|(
name|dst
argument_list|,
name|offset
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object object)
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
if|if
condition|(
name|object
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|Set
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Set
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|Set
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|size
argument_list|()
operator|!=
name|that
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|SortedIterables
operator|.
name|hasSameComparator
argument_list|(
name|comparator
argument_list|,
name|that
argument_list|)
condition|)
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|otherIterator
init|=
name|that
operator|.
name|iterator
argument_list|()
decl_stmt|;
try|try
block|{
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
init|=
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
name|Object
name|element
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Object
name|otherElement
init|=
name|otherIterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|otherElement
operator|==
literal|null
operator|||
name|unsafeCompare
argument_list|(
name|element
argument_list|,
name|otherElement
argument_list|)
operator|!=
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
comment|// concurrent change to other set
block|}
block|}
return|return
name|containsAll
argument_list|(
name|that
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|first ()
specifier|public
name|E
name|first
parameter_list|()
block|{
if|if
condition|(
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
return|return
name|elements
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|last ()
specifier|public
name|E
name|last
parameter_list|()
block|{
if|if
condition|(
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
return|return
name|elements
operator|.
name|get
argument_list|(
name|size
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|lower (E element)
specifier|public
name|E
name|lower
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|int
name|index
init|=
name|headIndex
argument_list|(
name|element
argument_list|,
literal|false
argument_list|)
operator|-
literal|1
decl_stmt|;
return|return
operator|(
name|index
operator|==
operator|-
literal|1
operator|)
condition|?
literal|null
else|:
name|elements
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|floor (E element)
specifier|public
name|E
name|floor
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|int
name|index
init|=
name|headIndex
argument_list|(
name|element
argument_list|,
literal|true
argument_list|)
operator|-
literal|1
decl_stmt|;
return|return
operator|(
name|index
operator|==
operator|-
literal|1
operator|)
condition|?
literal|null
else|:
name|elements
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|ceiling (E element)
specifier|public
name|E
name|ceiling
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|int
name|index
init|=
name|tailIndex
argument_list|(
name|element
argument_list|,
literal|true
argument_list|)
decl_stmt|;
return|return
operator|(
name|index
operator|==
name|size
argument_list|()
operator|)
condition|?
literal|null
else|:
name|elements
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|higher (E element)
specifier|public
name|E
name|higher
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|int
name|index
init|=
name|tailIndex
argument_list|(
name|element
argument_list|,
literal|false
argument_list|)
decl_stmt|;
return|return
operator|(
name|index
operator|==
name|size
argument_list|()
operator|)
condition|?
literal|null
else|:
name|elements
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|headSetImpl (E toElement, boolean inclusive)
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|headSetImpl
parameter_list|(
name|E
name|toElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
return|return
name|getSubSet
argument_list|(
literal|0
argument_list|,
name|headIndex
argument_list|(
name|toElement
argument_list|,
name|inclusive
argument_list|)
argument_list|)
return|;
block|}
DECL|method|headIndex (E toElement, boolean inclusive)
name|int
name|headIndex
parameter_list|(
name|E
name|toElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
name|int
name|index
init|=
name|Collections
operator|.
name|binarySearch
argument_list|(
name|elements
argument_list|,
name|checkNotNull
argument_list|(
name|toElement
argument_list|)
argument_list|,
name|comparator
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>=
literal|0
condition|)
block|{
return|return
name|inclusive
condition|?
name|index
operator|+
literal|1
else|:
name|index
return|;
block|}
else|else
block|{
return|return
operator|~
name|index
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|subSetImpl ( E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|subSetImpl
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|boolean
name|fromInclusive
parameter_list|,
name|E
name|toElement
parameter_list|,
name|boolean
name|toInclusive
parameter_list|)
block|{
return|return
name|tailSetImpl
argument_list|(
name|fromElement
argument_list|,
name|fromInclusive
argument_list|)
operator|.
name|headSetImpl
argument_list|(
name|toElement
argument_list|,
name|toInclusive
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|tailSetImpl (E fromElement, boolean inclusive)
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|tailSetImpl
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
return|return
name|getSubSet
argument_list|(
name|tailIndex
argument_list|(
name|fromElement
argument_list|,
name|inclusive
argument_list|)
argument_list|,
name|size
argument_list|()
argument_list|)
return|;
block|}
DECL|method|tailIndex (E fromElement, boolean inclusive)
name|int
name|tailIndex
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
name|int
name|index
init|=
name|Collections
operator|.
name|binarySearch
argument_list|(
name|elements
argument_list|,
name|checkNotNull
argument_list|(
name|fromElement
argument_list|)
argument_list|,
name|comparator
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>=
literal|0
condition|)
block|{
return|return
name|inclusive
condition|?
name|index
else|:
name|index
operator|+
literal|1
return|;
block|}
else|else
block|{
return|return
operator|~
name|index
return|;
block|}
block|}
comment|// Pretend the comparator can compare anything. If it turns out it can't
comment|// compare two elements, it'll throw a CCE. Only methods that are specified to
comment|// throw CCE should call this.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|unsafeComparator ()
name|Comparator
argument_list|<
name|Object
argument_list|>
name|unsafeComparator
parameter_list|()
block|{
return|return
operator|(
name|Comparator
argument_list|<
name|Object
argument_list|>
operator|)
name|comparator
return|;
block|}
DECL|method|getSubSet (int newFromIndex, int newToIndex)
name|RegularImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|getSubSet
parameter_list|(
name|int
name|newFromIndex
parameter_list|,
name|int
name|newToIndex
parameter_list|)
block|{
if|if
condition|(
name|newFromIndex
operator|==
literal|0
operator|&&
name|newToIndex
operator|==
name|size
argument_list|()
condition|)
block|{
return|return
name|this
return|;
block|}
elseif|else
if|if
condition|(
name|newFromIndex
operator|<
name|newToIndex
condition|)
block|{
return|return
operator|new
name|RegularImmutableSortedSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|elements
operator|.
name|subList
argument_list|(
name|newFromIndex
argument_list|,
name|newToIndex
argument_list|)
argument_list|,
name|comparator
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|emptySet
argument_list|(
name|comparator
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|indexOf (@ullable Object target)
name|int
name|indexOf
parameter_list|(
annotation|@
name|Nullable
name|Object
name|target
parameter_list|)
block|{
if|if
condition|(
name|target
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|int
name|position
decl_stmt|;
try|try
block|{
name|position
operator|=
name|Collections
operator|.
name|binarySearch
argument_list|(
name|elements
argument_list|,
name|target
argument_list|,
name|unsafeComparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
operator|(
name|position
operator|>=
literal|0
operator|)
condition|?
name|position
else|:
operator|-
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|createAsList ()
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|createAsList
parameter_list|()
block|{
return|return
operator|(
name|size
argument_list|()
operator|<=
literal|1
operator|)
condition|?
name|elements
else|:
operator|new
name|ImmutableSortedAsList
argument_list|<
name|E
argument_list|>
argument_list|(
name|this
argument_list|,
name|elements
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createDescendingSet ()
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|createDescendingSet
parameter_list|()
block|{
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|reversedOrder
init|=
name|Collections
operator|.
name|reverseOrder
argument_list|(
name|comparator
argument_list|)
decl_stmt|;
return|return
name|isEmpty
argument_list|()
condition|?
name|emptySet
argument_list|(
name|reversedOrder
argument_list|)
else|:
operator|new
name|RegularImmutableSortedSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|elements
operator|.
name|reverse
argument_list|()
argument_list|,
name|reversedOrder
argument_list|)
return|;
block|}
block|}
end_class

end_unit

