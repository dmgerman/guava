begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * An immutable sorted set with one or more elements.  * TODO: Consider creating a separate class for a single-element sorted set.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
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
DECL|field|elements
specifier|private
specifier|final
name|Object
index|[]
name|elements
decl_stmt|;
comment|/**    * The index of the first element that's in the sorted set (inclusive    * index).    */
DECL|field|fromIndex
specifier|private
specifier|final
name|int
name|fromIndex
decl_stmt|;
comment|/**    * The index after the last element that's in the sorted set (exclusive    * index).    */
DECL|field|toIndex
specifier|private
specifier|final
name|int
name|toIndex
decl_stmt|;
DECL|method|RegularImmutableSortedSet (Object[] elements, Comparator<? super E> comparator)
name|RegularImmutableSortedSet
parameter_list|(
name|Object
index|[]
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
name|this
operator|.
name|fromIndex
operator|=
literal|0
expr_stmt|;
name|this
operator|.
name|toIndex
operator|=
name|elements
operator|.
name|length
expr_stmt|;
block|}
DECL|method|RegularImmutableSortedSet (Object[] elements, Comparator<? super E> comparator, int fromIndex, int toIndex)
name|RegularImmutableSortedSet
parameter_list|(
name|Object
index|[]
name|elements
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|,
name|int
name|fromIndex
parameter_list|,
name|int
name|toIndex
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
name|this
operator|.
name|fromIndex
operator|=
name|fromIndex
expr_stmt|;
name|this
operator|.
name|toIndex
operator|=
name|toIndex
expr_stmt|;
block|}
comment|// The factory methods ensure that every element is an E.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|(
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
operator|)
name|Iterators
operator|.
name|forArray
argument_list|(
name|elements
argument_list|,
name|fromIndex
argument_list|,
name|size
argument_list|()
argument_list|)
return|;
block|}
DECL|method|isEmpty ()
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|toIndex
operator|-
name|fromIndex
return|;
block|}
DECL|method|contains (Object o)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
try|try
block|{
return|return
name|binarySearch
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
DECL|method|containsAll (Collection<?> targets)
annotation|@
name|Override
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
comment|// TODO: For optimal performance, use a binary search when
comment|// targets.size()< size() / log(size())
if|if
condition|(
operator|!
name|hasSameComparator
argument_list|(
name|targets
argument_list|,
name|comparator
argument_list|()
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
comment|/*      * If targets is a sorted set with the same comparator, containsAll can      * run in O(n) time stepping through the two collections.      */
name|int
name|i
init|=
name|fromIndex
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|iterator
init|=
name|targets
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Object
name|target
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
if|if
condition|(
name|i
operator|>=
name|toIndex
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|cmp
init|=
name|unsafeCompare
argument_list|(
name|elements
index|[
name|i
index|]
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
name|i
operator|++
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
name|iterator
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
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|i
operator|++
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
DECL|method|binarySearch (Object key)
specifier|private
name|int
name|binarySearch
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|int
name|lower
init|=
name|fromIndex
decl_stmt|;
name|int
name|upper
init|=
name|toIndex
operator|-
literal|1
decl_stmt|;
while|while
condition|(
name|lower
operator|<=
name|upper
condition|)
block|{
name|int
name|middle
init|=
name|lower
operator|+
operator|(
name|upper
operator|-
name|lower
operator|)
operator|/
literal|2
decl_stmt|;
name|int
name|c
init|=
name|unsafeCompare
argument_list|(
name|key
argument_list|,
name|elements
index|[
name|middle
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|<
literal|0
condition|)
block|{
name|upper
operator|=
name|middle
operator|-
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|>
literal|0
condition|)
block|{
name|lower
operator|=
name|middle
operator|+
literal|1
expr_stmt|;
block|}
else|else
block|{
return|return
name|middle
return|;
block|}
block|}
return|return
operator|-
name|lower
operator|-
literal|1
return|;
block|}
DECL|method|toArray ()
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
name|Object
index|[]
name|array
init|=
operator|new
name|Object
index|[
name|size
argument_list|()
index|]
decl_stmt|;
name|Platform
operator|.
name|unsafeArrayCopy
argument_list|(
name|elements
argument_list|,
name|fromIndex
argument_list|,
name|array
argument_list|,
literal|0
argument_list|,
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|array
return|;
block|}
comment|// TODO: Move to ObjectArrays (same code in ImmutableList).
DECL|method|toArray (T[] array)
annotation|@
name|Override
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
name|int
name|size
init|=
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|array
operator|.
name|length
operator|<
name|size
condition|)
block|{
name|array
operator|=
name|ObjectArrays
operator|.
name|newArray
argument_list|(
name|array
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|array
operator|.
name|length
operator|>
name|size
condition|)
block|{
name|array
index|[
name|size
index|]
operator|=
literal|null
expr_stmt|;
block|}
name|Platform
operator|.
name|unsafeArrayCopy
argument_list|(
name|elements
argument_list|,
name|fromIndex
argument_list|,
name|array
argument_list|,
literal|0
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
name|array
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
if|if
condition|(
name|hasSameComparator
argument_list|(
name|that
argument_list|,
name|comparator
argument_list|)
condition|)
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|iterator
init|=
name|that
operator|.
name|iterator
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|int
name|i
init|=
name|fromIndex
init|;
name|i
operator|<
name|toIndex
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|otherElement
init|=
name|iterator
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
name|elements
index|[
name|i
index|]
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
name|this
operator|.
name|containsAll
argument_list|(
name|that
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
comment|// not caching hash code since it could change if the elements are mutable
comment|// in a way that modifies their hash codes
name|int
name|hash
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|fromIndex
init|;
name|i
operator|<
name|toIndex
condition|;
name|i
operator|++
control|)
block|{
name|hash
operator|+=
name|elements
index|[
name|i
index|]
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
name|hash
return|;
block|}
comment|// The factory methods ensure that every element is an E.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|first ()
specifier|public
name|E
name|first
parameter_list|()
block|{
return|return
operator|(
name|E
operator|)
name|elements
index|[
name|fromIndex
index|]
return|;
block|}
comment|// The factory methods ensure that every element is an E.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|last ()
specifier|public
name|E
name|last
parameter_list|()
block|{
return|return
operator|(
name|E
operator|)
name|elements
index|[
name|toIndex
operator|-
literal|1
index|]
return|;
block|}
DECL|method|headSetImpl (E toElement)
annotation|@
name|Override
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|headSetImpl
parameter_list|(
name|E
name|toElement
parameter_list|)
block|{
return|return
name|createSubset
argument_list|(
name|fromIndex
argument_list|,
name|findSubsetIndex
argument_list|(
name|toElement
argument_list|)
argument_list|)
return|;
block|}
DECL|method|subSetImpl (E fromElement, E toElement)
annotation|@
name|Override
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|subSetImpl
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|E
name|toElement
parameter_list|)
block|{
return|return
name|createSubset
argument_list|(
name|findSubsetIndex
argument_list|(
name|fromElement
argument_list|)
argument_list|,
name|findSubsetIndex
argument_list|(
name|toElement
argument_list|)
argument_list|)
return|;
block|}
DECL|method|tailSetImpl (E fromElement)
annotation|@
name|Override
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|tailSetImpl
parameter_list|(
name|E
name|fromElement
parameter_list|)
block|{
return|return
name|createSubset
argument_list|(
name|findSubsetIndex
argument_list|(
name|fromElement
argument_list|)
argument_list|,
name|toIndex
argument_list|)
return|;
block|}
DECL|method|findSubsetIndex (E element)
specifier|private
name|int
name|findSubsetIndex
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|int
name|index
init|=
name|binarySearch
argument_list|(
name|element
argument_list|)
decl_stmt|;
return|return
operator|(
name|index
operator|>=
literal|0
operator|)
condition|?
name|index
else|:
operator|(
operator|-
name|index
operator|-
literal|1
operator|)
return|;
block|}
DECL|method|createSubset ( int newFromIndex, int newToIndex)
specifier|private
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|createSubset
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
argument_list|,
name|comparator
argument_list|,
name|newFromIndex
argument_list|,
name|newToIndex
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
DECL|method|hasPartialArray ()
annotation|@
name|Override
name|boolean
name|hasPartialArray
parameter_list|()
block|{
return|return
operator|(
name|fromIndex
operator|!=
literal|0
operator|)
operator|||
operator|(
name|toIndex
operator|!=
name|elements
operator|.
name|length
operator|)
return|;
block|}
DECL|method|indexOf (Object target)
annotation|@
name|Override
name|int
name|indexOf
parameter_list|(
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
name|binarySearch
argument_list|(
name|target
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
comment|// The equals() check is needed when the comparator isn't compatible with
comment|// equals().
return|return
operator|(
name|position
operator|>=
literal|0
operator|&&
name|elements
index|[
name|position
index|]
operator|.
name|equals
argument_list|(
name|target
argument_list|)
operator|)
condition|?
name|position
operator|-
name|fromIndex
else|:
operator|-
literal|1
return|;
block|}
DECL|method|createAsList ()
annotation|@
name|Override
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|createAsList
parameter_list|()
block|{
return|return
operator|new
name|ImmutableSortedAsList
argument_list|<
name|E
argument_list|>
argument_list|(
name|elements
argument_list|,
name|fromIndex
argument_list|,
name|size
argument_list|()
argument_list|,
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

