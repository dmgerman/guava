begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
package|;
end_package

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
name|NavigableSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_comment
comment|/**  * A wrapper around {@code TreeSet} that aggressively checks to see if elements  * are mutually comparable. This implementation passes the navigable set test  * suites.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|SafeTreeSet
specifier|public
specifier|final
class|class
name|SafeTreeSet
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Serializable
implements|,
name|NavigableSet
argument_list|<
name|E
argument_list|>
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|field|NATURAL_ORDER
specifier|private
specifier|static
specifier|final
name|Comparator
argument_list|<
name|Object
argument_list|>
name|NATURAL_ORDER
init|=
operator|new
name|Comparator
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
return|return
operator|(
operator|(
name|Comparable
argument_list|<
name|Object
argument_list|>
operator|)
name|o1
operator|)
operator|.
name|compareTo
argument_list|(
name|o2
argument_list|)
return|;
block|}
block|}
decl_stmt|;
DECL|field|delegate
specifier|private
specifier|final
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|delegate
decl_stmt|;
DECL|method|SafeTreeSet ()
specifier|public
name|SafeTreeSet
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|TreeSet
argument_list|<
name|E
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|SafeTreeSet (Collection<? extends E> collection)
specifier|public
name|SafeTreeSet
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
name|this
argument_list|(
operator|new
name|TreeSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|collection
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|SafeTreeSet (Comparator<? super E> comparator)
specifier|public
name|SafeTreeSet
parameter_list|(
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
operator|new
name|TreeSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|comparator
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|SafeTreeSet (SortedSet<E> set)
specifier|public
name|SafeTreeSet
parameter_list|(
name|SortedSet
argument_list|<
name|E
argument_list|>
name|set
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|TreeSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|set
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|SafeTreeSet (NavigableSet<E> delegate)
specifier|private
name|SafeTreeSet
parameter_list|(
name|NavigableSet
argument_list|<
name|E
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
for|for
control|(
name|E
name|e
range|:
name|this
control|)
block|{
name|checkValid
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|add (E element)
annotation|@
name|Override
specifier|public
name|boolean
name|add
parameter_list|(
name|E
name|element
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|add
argument_list|(
name|checkValid
argument_list|(
name|element
argument_list|)
argument_list|)
return|;
block|}
DECL|method|addAll (Collection<? extends E> collection)
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
name|E
argument_list|>
name|collection
parameter_list|)
block|{
for|for
control|(
name|E
name|e
range|:
name|collection
control|)
block|{
name|checkValid
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|delegate
operator|.
name|addAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
DECL|method|ceiling (E e)
annotation|@
name|Override
specifier|public
name|E
name|ceiling
parameter_list|(
name|E
name|e
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|ceiling
argument_list|(
name|checkValid
argument_list|(
name|e
argument_list|)
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
name|delegate
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|comparator ()
annotation|@
name|Override
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|()
block|{
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
init|=
name|delegate
operator|.
name|comparator
argument_list|()
decl_stmt|;
if|if
condition|(
name|comparator
operator|==
literal|null
condition|)
block|{
name|comparator
operator|=
operator|(
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
operator|)
name|NATURAL_ORDER
expr_stmt|;
block|}
return|return
name|comparator
return|;
block|}
DECL|method|contains (Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|contains
argument_list|(
name|checkValid
argument_list|(
name|object
argument_list|)
argument_list|)
return|;
block|}
DECL|method|containsAll (Collection<?> c)
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
name|c
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|containsAll
argument_list|(
name|c
argument_list|)
return|;
block|}
DECL|method|descendingIterator ()
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|descendingIterator
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|descendingIterator
argument_list|()
return|;
block|}
DECL|method|descendingSet ()
annotation|@
name|Override
specifier|public
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|descendingSet
parameter_list|()
block|{
return|return
operator|new
name|SafeTreeSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|delegate
operator|.
name|descendingSet
argument_list|()
argument_list|)
return|;
block|}
DECL|method|first ()
annotation|@
name|Override
specifier|public
name|E
name|first
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|first
argument_list|()
return|;
block|}
DECL|method|floor (E e)
annotation|@
name|Override
specifier|public
name|E
name|floor
parameter_list|(
name|E
name|e
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|floor
argument_list|(
name|checkValid
argument_list|(
name|e
argument_list|)
argument_list|)
return|;
block|}
DECL|method|headSet (E toElement)
annotation|@
name|Override
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|headSet
parameter_list|(
name|E
name|toElement
parameter_list|)
block|{
return|return
name|headSet
argument_list|(
name|toElement
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|headSet (E toElement, boolean inclusive)
annotation|@
name|Override
specifier|public
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|headSet
parameter_list|(
name|E
name|toElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
return|return
operator|new
name|SafeTreeSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|delegate
operator|.
name|headSet
argument_list|(
name|checkValid
argument_list|(
name|toElement
argument_list|)
argument_list|,
name|inclusive
argument_list|)
argument_list|)
return|;
block|}
DECL|method|higher (E e)
annotation|@
name|Override
specifier|public
name|E
name|higher
parameter_list|(
name|E
name|e
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|higher
argument_list|(
name|checkValid
argument_list|(
name|e
argument_list|)
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
name|delegate
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|iterator
argument_list|()
return|;
block|}
DECL|method|last ()
annotation|@
name|Override
specifier|public
name|E
name|last
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|last
argument_list|()
return|;
block|}
DECL|method|lower (E e)
annotation|@
name|Override
specifier|public
name|E
name|lower
parameter_list|(
name|E
name|e
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|lower
argument_list|(
name|checkValid
argument_list|(
name|e
argument_list|)
argument_list|)
return|;
block|}
DECL|method|pollFirst ()
annotation|@
name|Override
specifier|public
name|E
name|pollFirst
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|pollFirst
argument_list|()
return|;
block|}
DECL|method|pollLast ()
annotation|@
name|Override
specifier|public
name|E
name|pollLast
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|pollLast
argument_list|()
return|;
block|}
DECL|method|remove (Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|remove
argument_list|(
name|checkValid
argument_list|(
name|object
argument_list|)
argument_list|)
return|;
block|}
DECL|method|removeAll (Collection<?> c)
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
name|c
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|removeAll
argument_list|(
name|c
argument_list|)
return|;
block|}
DECL|method|retainAll (Collection<?> c)
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
name|c
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|retainAll
argument_list|(
name|c
argument_list|)
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
name|delegate
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|subSet ( E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
annotation|@
name|Override
specifier|public
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|subSet
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
operator|new
name|SafeTreeSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|delegate
operator|.
name|subSet
argument_list|(
name|checkValid
argument_list|(
name|fromElement
argument_list|)
argument_list|,
name|fromInclusive
argument_list|,
name|checkValid
argument_list|(
name|toElement
argument_list|)
argument_list|,
name|toInclusive
argument_list|)
argument_list|)
return|;
block|}
DECL|method|subSet (E fromElement, E toElement)
annotation|@
name|Override
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|subSet
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|E
name|toElement
parameter_list|)
block|{
return|return
name|subSet
argument_list|(
name|fromElement
argument_list|,
literal|true
argument_list|,
name|toElement
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|tailSet (E fromElement)
annotation|@
name|Override
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|tailSet
parameter_list|(
name|E
name|fromElement
parameter_list|)
block|{
return|return
name|tailSet
argument_list|(
name|fromElement
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|tailSet (E fromElement, boolean inclusive)
annotation|@
name|Override
specifier|public
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|tailSet
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
return|return
operator|new
name|SafeTreeSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|delegate
operator|.
name|tailSet
argument_list|(
name|checkValid
argument_list|(
name|fromElement
argument_list|)
argument_list|,
name|inclusive
argument_list|)
argument_list|)
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
return|return
name|delegate
operator|.
name|toArray
argument_list|()
return|;
block|}
DECL|method|toArray (T[] a)
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
name|a
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|toArray
argument_list|(
name|a
argument_list|)
return|;
block|}
DECL|method|checkValid (T t)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|checkValid
parameter_list|(
name|T
name|t
parameter_list|)
block|{
comment|// a ClassCastException is what's supposed to happen!
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|E
name|e
init|=
operator|(
name|E
operator|)
name|t
decl_stmt|;
name|comparator
argument_list|()
operator|.
name|compare
argument_list|(
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|t
return|;
block|}
DECL|method|equals (Object obj)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|equals
argument_list|(
name|obj
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
return|return
name|delegate
operator|.
name|hashCode
argument_list|()
return|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0L
decl_stmt|;
block|}
end_class

end_unit

