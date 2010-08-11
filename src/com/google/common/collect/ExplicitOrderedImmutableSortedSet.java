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
name|ObjectInputStream
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
name|Arrays
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
name|List
import|;
end_import

begin_comment
comment|/**  * An immutable sorted set consisting of, and ordered by, a list of provided  * elements.  *  * @author Jared Levy  */
end_comment

begin_comment
comment|// TODO(jlevy): Create superclass with code shared by this class and
end_comment

begin_comment
comment|// RegularImmutableSortedSet.
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
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
DECL|class|ExplicitOrderedImmutableSortedSet
specifier|final
class|class
name|ExplicitOrderedImmutableSortedSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
block|{
DECL|method|create (List<E> list)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|list
parameter_list|)
block|{
name|ExplicitOrdering
argument_list|<
name|E
argument_list|>
name|ordering
init|=
operator|new
name|ExplicitOrdering
argument_list|<
name|E
argument_list|>
argument_list|(
name|list
argument_list|)
decl_stmt|;
if|if
condition|(
name|ordering
operator|.
name|rankMap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|emptySet
argument_list|(
name|ordering
argument_list|)
return|;
block|}
comment|// Not using list.toArray() to avoid iterating across the input list twice.
name|Object
index|[]
name|elements
init|=
name|ordering
operator|.
name|rankMap
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|()
decl_stmt|;
return|return
operator|new
name|ExplicitOrderedImmutableSortedSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|elements
argument_list|,
name|ordering
argument_list|)
return|;
block|}
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
DECL|method|ExplicitOrderedImmutableSortedSet (Object[] elements, Comparator<? super E> comparator)
name|ExplicitOrderedImmutableSortedSet
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
name|this
argument_list|(
name|elements
argument_list|,
name|comparator
argument_list|,
literal|0
argument_list|,
name|elements
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
DECL|method|ExplicitOrderedImmutableSortedSet (Object[] elements, Comparator<? super E> comparator, int fromIndex, int toIndex)
name|ExplicitOrderedImmutableSortedSet
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
comment|// create() generates an ImmutableMap<E, Integer> rankMap.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|rankMap ()
specifier|private
name|ImmutableMap
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
name|rankMap
parameter_list|()
block|{
return|return
operator|(
operator|(
name|ExplicitOrdering
operator|)
name|comparator
argument_list|()
operator|)
operator|.
name|rankMap
return|;
block|}
comment|// create() ensures that every element is an E.
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
name|Integer
name|index
init|=
name|rankMap
argument_list|()
operator|.
name|get
argument_list|(
name|o
argument_list|)
decl_stmt|;
return|return
operator|(
name|index
operator|!=
literal|null
operator|&&
name|index
operator|>=
name|fromIndex
operator|&&
name|index
operator|<
name|toIndex
operator|)
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
comment|// TODO(jlevy): Move to ObjectArrays (same code in ImmutableList).
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
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
comment|// TODO(jlevy): Cache hash code?
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
comment|// TODO(jlevy): Override subSet to avoid redundant map lookups.
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
name|Integer
name|index
init|=
name|rankMap
argument_list|()
operator|.
name|get
argument_list|(
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|==
literal|null
condition|)
block|{
comment|// TODO(kevinb): Make Ordering.IncomparableValueException public, use it
throw|throw
operator|new
name|ClassCastException
argument_list|()
throw|;
block|}
if|if
condition|(
name|index
operator|<=
name|fromIndex
condition|)
block|{
return|return
name|fromIndex
return|;
block|}
elseif|else
if|if
condition|(
name|index
operator|>=
name|toIndex
condition|)
block|{
return|return
name|toIndex
return|;
block|}
else|else
block|{
return|return
name|index
return|;
block|}
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
name|ExplicitOrderedImmutableSortedSet
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
name|Integer
name|index
init|=
name|rankMap
argument_list|()
operator|.
name|get
argument_list|(
name|target
argument_list|)
decl_stmt|;
return|return
operator|(
name|index
operator|!=
literal|null
operator|&&
name|index
operator|>=
name|fromIndex
operator|&&
name|index
operator|<
name|toIndex
operator|)
condition|?
name|index
operator|-
name|fromIndex
else|:
operator|-
literal|1
return|;
block|}
comment|/*    * TODO(jlevy): Modify ImmutableSortedAsList.subList() so it creates a list    * based on an ExplicitOrderedImmutableSortedSet when the original list was    * constructed from one, for faster contains(), indexOf(), and lastIndexOf().    */
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
comment|/*    * Generates an ExplicitOrderedImmutableSortedSet when deserialized, for    * better performance.    */
DECL|class|SerializedForm
specifier|private
specifier|static
class|class
name|SerializedForm
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Serializable
block|{
DECL|field|elements
specifier|final
name|Object
index|[]
name|elements
decl_stmt|;
DECL|method|SerializedForm (Object[] elements)
specifier|public
name|SerializedForm
parameter_list|(
name|Object
index|[]
name|elements
parameter_list|)
block|{
name|this
operator|.
name|elements
operator|=
name|elements
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|ImmutableSortedSet
operator|.
name|withExplicitOrder
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
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
DECL|method|readObject (ObjectInputStream stream)
specifier|private
name|void
name|readObject
parameter_list|(
name|ObjectInputStream
name|stream
parameter_list|)
throws|throws
name|InvalidObjectException
block|{
throw|throw
operator|new
name|InvalidObjectException
argument_list|(
literal|"Use SerializedForm"
argument_list|)
throw|;
block|}
DECL|method|writeReplace ()
annotation|@
name|Override
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|SerializedForm
argument_list|<
name|E
argument_list|>
argument_list|(
name|toArray
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

