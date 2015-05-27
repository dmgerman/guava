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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ListIterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|RandomAccess
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
name|SortedSet
import|;
end_import

begin_comment
comment|/**  * Factories and utilities pertaining to the {@link Constraint} interface.  *  * @author Mike Bostock  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Constraints
specifier|final
class|class
name|Constraints
block|{
DECL|method|Constraints ()
specifier|private
name|Constraints
parameter_list|()
block|{}
comment|/**    * Returns a constrained view of the specified collection, using the specified    * constraint. Any operations that add new elements to the collection will    * call the provided constraint. However, this method does not verify that    * existing elements satisfy the constraint.    *    *<p>The returned collection is not serializable.    *    * @param collection the collection to constrain    * @param constraint the constraint that validates added elements    * @return a constrained view of the collection    */
DECL|method|constrainedCollection ( Collection<E> collection, Constraint<? super E> constraint)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Collection
argument_list|<
name|E
argument_list|>
name|constrainedCollection
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|collection
parameter_list|,
name|Constraint
argument_list|<
name|?
super|super
name|E
argument_list|>
name|constraint
parameter_list|)
block|{
return|return
operator|new
name|ConstrainedCollection
argument_list|<
name|E
argument_list|>
argument_list|(
name|collection
argument_list|,
name|constraint
argument_list|)
return|;
block|}
comment|/** @see Constraints#constrainedCollection */
DECL|class|ConstrainedCollection
specifier|static
class|class
name|ConstrainedCollection
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingCollection
argument_list|<
name|E
argument_list|>
block|{
DECL|field|delegate
specifier|private
specifier|final
name|Collection
argument_list|<
name|E
argument_list|>
name|delegate
decl_stmt|;
DECL|field|constraint
specifier|private
specifier|final
name|Constraint
argument_list|<
name|?
super|super
name|E
argument_list|>
name|constraint
decl_stmt|;
DECL|method|ConstrainedCollection (Collection<E> delegate, Constraint<? super E> constraint)
specifier|public
name|ConstrainedCollection
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|,
name|Constraint
argument_list|<
name|?
super|super
name|E
argument_list|>
name|constraint
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|constraint
operator|=
name|checkNotNull
argument_list|(
name|constraint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Collection
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
annotation|@
name|Override
DECL|method|add (E element)
specifier|public
name|boolean
name|add
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|constraint
operator|.
name|checkElement
argument_list|(
name|element
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|add
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addAll (Collection<? extends E> elements)
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
name|elements
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|addAll
argument_list|(
name|checkElements
argument_list|(
name|elements
argument_list|,
name|constraint
argument_list|)
argument_list|)
return|;
block|}
block|}
comment|/**    * Returns a constrained view of the specified set, using the specified    * constraint. Any operations that add new elements to the set will call the    * provided constraint. However, this method does not verify that existing    * elements satisfy the constraint.    *    *<p>The returned set is not serializable.    *    * @param set the set to constrain    * @param constraint the constraint that validates added elements    * @return a constrained view of the set    */
DECL|method|constrainedSet (Set<E> set, Constraint<? super E> constraint)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|constrainedSet
parameter_list|(
name|Set
argument_list|<
name|E
argument_list|>
name|set
parameter_list|,
name|Constraint
argument_list|<
name|?
super|super
name|E
argument_list|>
name|constraint
parameter_list|)
block|{
return|return
operator|new
name|ConstrainedSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|set
argument_list|,
name|constraint
argument_list|)
return|;
block|}
comment|/** @see Constraints#constrainedSet */
DECL|class|ConstrainedSet
specifier|static
class|class
name|ConstrainedSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingSet
argument_list|<
name|E
argument_list|>
block|{
DECL|field|delegate
specifier|private
specifier|final
name|Set
argument_list|<
name|E
argument_list|>
name|delegate
decl_stmt|;
DECL|field|constraint
specifier|private
specifier|final
name|Constraint
argument_list|<
name|?
super|super
name|E
argument_list|>
name|constraint
decl_stmt|;
DECL|method|ConstrainedSet (Set<E> delegate, Constraint<? super E> constraint)
specifier|public
name|ConstrainedSet
parameter_list|(
name|Set
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|,
name|Constraint
argument_list|<
name|?
super|super
name|E
argument_list|>
name|constraint
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|constraint
operator|=
name|checkNotNull
argument_list|(
name|constraint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Set
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
annotation|@
name|Override
DECL|method|add (E element)
specifier|public
name|boolean
name|add
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|constraint
operator|.
name|checkElement
argument_list|(
name|element
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|add
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addAll (Collection<? extends E> elements)
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
name|elements
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|addAll
argument_list|(
name|checkElements
argument_list|(
name|elements
argument_list|,
name|constraint
argument_list|)
argument_list|)
return|;
block|}
block|}
comment|/**    * Returns a constrained view of the specified sorted set, using the specified    * constraint. Any operations that add new elements to the sorted set will    * call the provided constraint. However, this method does not verify that    * existing elements satisfy the constraint.    *    *<p>The returned set is not serializable.    *    * @param sortedSet the sorted set to constrain    * @param constraint the constraint that validates added elements    * @return a constrained view of the sorted set    */
DECL|method|constrainedSortedSet ( SortedSet<E> sortedSet, Constraint<? super E> constraint)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|SortedSet
argument_list|<
name|E
argument_list|>
name|constrainedSortedSet
parameter_list|(
name|SortedSet
argument_list|<
name|E
argument_list|>
name|sortedSet
parameter_list|,
name|Constraint
argument_list|<
name|?
super|super
name|E
argument_list|>
name|constraint
parameter_list|)
block|{
return|return
operator|new
name|ConstrainedSortedSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|sortedSet
argument_list|,
name|constraint
argument_list|)
return|;
block|}
comment|/** @see Constraints#constrainedSortedSet */
DECL|class|ConstrainedSortedSet
specifier|private
specifier|static
class|class
name|ConstrainedSortedSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingSortedSet
argument_list|<
name|E
argument_list|>
block|{
DECL|field|delegate
specifier|final
name|SortedSet
argument_list|<
name|E
argument_list|>
name|delegate
decl_stmt|;
DECL|field|constraint
specifier|final
name|Constraint
argument_list|<
name|?
super|super
name|E
argument_list|>
name|constraint
decl_stmt|;
DECL|method|ConstrainedSortedSet (SortedSet<E> delegate, Constraint<? super E> constraint)
name|ConstrainedSortedSet
parameter_list|(
name|SortedSet
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|,
name|Constraint
argument_list|<
name|?
super|super
name|E
argument_list|>
name|constraint
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|constraint
operator|=
name|checkNotNull
argument_list|(
name|constraint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|SortedSet
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
annotation|@
name|Override
DECL|method|headSet (E toElement)
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
name|constrainedSortedSet
argument_list|(
name|delegate
operator|.
name|headSet
argument_list|(
name|toElement
argument_list|)
argument_list|,
name|constraint
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|subSet (E fromElement, E toElement)
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
name|constrainedSortedSet
argument_list|(
name|delegate
operator|.
name|subSet
argument_list|(
name|fromElement
argument_list|,
name|toElement
argument_list|)
argument_list|,
name|constraint
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|tailSet (E fromElement)
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
name|constrainedSortedSet
argument_list|(
name|delegate
operator|.
name|tailSet
argument_list|(
name|fromElement
argument_list|)
argument_list|,
name|constraint
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|add (E element)
specifier|public
name|boolean
name|add
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|constraint
operator|.
name|checkElement
argument_list|(
name|element
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|add
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addAll (Collection<? extends E> elements)
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
name|elements
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|addAll
argument_list|(
name|checkElements
argument_list|(
name|elements
argument_list|,
name|constraint
argument_list|)
argument_list|)
return|;
block|}
block|}
comment|/**    * Returns a constrained view of the specified list, using the specified    * constraint. Any operations that add new elements to the list will call the    * provided constraint. However, this method does not verify that existing    * elements satisfy the constraint.    *    *<p>If {@code list} implements {@link RandomAccess}, so will the returned    * list. The returned list is not serializable.    *    * @param list the list to constrain    * @param constraint the constraint that validates added elements    * @return a constrained view of the list    */
DECL|method|constrainedList (List<E> list, Constraint<? super E> constraint)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|List
argument_list|<
name|E
argument_list|>
name|constrainedList
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|list
parameter_list|,
name|Constraint
argument_list|<
name|?
super|super
name|E
argument_list|>
name|constraint
parameter_list|)
block|{
return|return
operator|(
name|list
operator|instanceof
name|RandomAccess
operator|)
condition|?
operator|new
name|ConstrainedRandomAccessList
argument_list|<
name|E
argument_list|>
argument_list|(
name|list
argument_list|,
name|constraint
argument_list|)
else|:
operator|new
name|ConstrainedList
argument_list|<
name|E
argument_list|>
argument_list|(
name|list
argument_list|,
name|constraint
argument_list|)
return|;
block|}
comment|/** @see Constraints#constrainedList */
annotation|@
name|GwtCompatible
DECL|class|ConstrainedList
specifier|private
specifier|static
class|class
name|ConstrainedList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingList
argument_list|<
name|E
argument_list|>
block|{
DECL|field|delegate
specifier|final
name|List
argument_list|<
name|E
argument_list|>
name|delegate
decl_stmt|;
DECL|field|constraint
specifier|final
name|Constraint
argument_list|<
name|?
super|super
name|E
argument_list|>
name|constraint
decl_stmt|;
DECL|method|ConstrainedList (List<E> delegate, Constraint<? super E> constraint)
name|ConstrainedList
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|,
name|Constraint
argument_list|<
name|?
super|super
name|E
argument_list|>
name|constraint
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|constraint
operator|=
name|checkNotNull
argument_list|(
name|constraint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|List
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
annotation|@
name|Override
DECL|method|add (E element)
specifier|public
name|boolean
name|add
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|constraint
operator|.
name|checkElement
argument_list|(
name|element
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|add
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|add (int index, E element)
specifier|public
name|void
name|add
parameter_list|(
name|int
name|index
parameter_list|,
name|E
name|element
parameter_list|)
block|{
name|constraint
operator|.
name|checkElement
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|add
argument_list|(
name|index
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addAll (Collection<? extends E> elements)
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
name|elements
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|addAll
argument_list|(
name|checkElements
argument_list|(
name|elements
argument_list|,
name|constraint
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addAll (int index, Collection<? extends E> elements)
specifier|public
name|boolean
name|addAll
parameter_list|(
name|int
name|index
parameter_list|,
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|addAll
argument_list|(
name|index
argument_list|,
name|checkElements
argument_list|(
name|elements
argument_list|,
name|constraint
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|listIterator ()
specifier|public
name|ListIterator
argument_list|<
name|E
argument_list|>
name|listIterator
parameter_list|()
block|{
return|return
name|constrainedListIterator
argument_list|(
name|delegate
operator|.
name|listIterator
argument_list|()
argument_list|,
name|constraint
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|listIterator (int index)
specifier|public
name|ListIterator
argument_list|<
name|E
argument_list|>
name|listIterator
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|constrainedListIterator
argument_list|(
name|delegate
operator|.
name|listIterator
argument_list|(
name|index
argument_list|)
argument_list|,
name|constraint
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|set (int index, E element)
specifier|public
name|E
name|set
parameter_list|(
name|int
name|index
parameter_list|,
name|E
name|element
parameter_list|)
block|{
name|constraint
operator|.
name|checkElement
argument_list|(
name|element
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|set
argument_list|(
name|index
argument_list|,
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|subList (int fromIndex, int toIndex)
specifier|public
name|List
argument_list|<
name|E
argument_list|>
name|subList
parameter_list|(
name|int
name|fromIndex
parameter_list|,
name|int
name|toIndex
parameter_list|)
block|{
return|return
name|constrainedList
argument_list|(
name|delegate
operator|.
name|subList
argument_list|(
name|fromIndex
argument_list|,
name|toIndex
argument_list|)
argument_list|,
name|constraint
argument_list|)
return|;
block|}
block|}
comment|/** @see Constraints#constrainedList */
DECL|class|ConstrainedRandomAccessList
specifier|static
class|class
name|ConstrainedRandomAccessList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ConstrainedList
argument_list|<
name|E
argument_list|>
implements|implements
name|RandomAccess
block|{
DECL|method|ConstrainedRandomAccessList (List<E> delegate, Constraint<? super E> constraint)
name|ConstrainedRandomAccessList
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|,
name|Constraint
argument_list|<
name|?
super|super
name|E
argument_list|>
name|constraint
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
argument_list|,
name|constraint
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Returns a constrained view of the specified list iterator, using the    * specified constraint. Any operations that would add new elements to the    * underlying list will be verified by the constraint.    *    * @param listIterator the iterator for which to return a constrained view    * @param constraint the constraint for elements in the list    * @return a constrained view of the specified iterator    */
DECL|method|constrainedListIterator ( ListIterator<E> listIterator, Constraint<? super E> constraint)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ListIterator
argument_list|<
name|E
argument_list|>
name|constrainedListIterator
parameter_list|(
name|ListIterator
argument_list|<
name|E
argument_list|>
name|listIterator
parameter_list|,
name|Constraint
argument_list|<
name|?
super|super
name|E
argument_list|>
name|constraint
parameter_list|)
block|{
return|return
operator|new
name|ConstrainedListIterator
argument_list|<
name|E
argument_list|>
argument_list|(
name|listIterator
argument_list|,
name|constraint
argument_list|)
return|;
block|}
comment|/** @see Constraints#constrainedListIterator */
DECL|class|ConstrainedListIterator
specifier|static
class|class
name|ConstrainedListIterator
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingListIterator
argument_list|<
name|E
argument_list|>
block|{
DECL|field|delegate
specifier|private
specifier|final
name|ListIterator
argument_list|<
name|E
argument_list|>
name|delegate
decl_stmt|;
DECL|field|constraint
specifier|private
specifier|final
name|Constraint
argument_list|<
name|?
super|super
name|E
argument_list|>
name|constraint
decl_stmt|;
DECL|method|ConstrainedListIterator (ListIterator<E> delegate, Constraint<? super E> constraint)
specifier|public
name|ConstrainedListIterator
parameter_list|(
name|ListIterator
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|,
name|Constraint
argument_list|<
name|?
super|super
name|E
argument_list|>
name|constraint
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
name|constraint
operator|=
name|constraint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|ListIterator
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
annotation|@
name|Override
DECL|method|add (E element)
specifier|public
name|void
name|add
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|constraint
operator|.
name|checkElement
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|set (E element)
specifier|public
name|void
name|set
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|constraint
operator|.
name|checkElement
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|set
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|constrainedTypePreservingCollection ( Collection<E> collection, Constraint<E> constraint)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Collection
argument_list|<
name|E
argument_list|>
name|constrainedTypePreservingCollection
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|collection
parameter_list|,
name|Constraint
argument_list|<
name|E
argument_list|>
name|constraint
parameter_list|)
block|{
if|if
condition|(
name|collection
operator|instanceof
name|SortedSet
condition|)
block|{
return|return
name|constrainedSortedSet
argument_list|(
operator|(
name|SortedSet
argument_list|<
name|E
argument_list|>
operator|)
name|collection
argument_list|,
name|constraint
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|collection
operator|instanceof
name|Set
condition|)
block|{
return|return
name|constrainedSet
argument_list|(
operator|(
name|Set
argument_list|<
name|E
argument_list|>
operator|)
name|collection
argument_list|,
name|constraint
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|collection
operator|instanceof
name|List
condition|)
block|{
return|return
name|constrainedList
argument_list|(
operator|(
name|List
argument_list|<
name|E
argument_list|>
operator|)
name|collection
argument_list|,
name|constraint
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|constrainedCollection
argument_list|(
name|collection
argument_list|,
name|constraint
argument_list|)
return|;
block|}
block|}
comment|/*    * TODO(kevinb): For better performance, avoid making a copy of the elements    * by having addAll() call add() repeatedly instead.    */
DECL|method|checkElements ( Collection<E> elements, Constraint<? super E> constraint)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Collection
argument_list|<
name|E
argument_list|>
name|checkElements
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|elements
parameter_list|,
name|Constraint
argument_list|<
name|?
super|super
name|E
argument_list|>
name|constraint
parameter_list|)
block|{
name|Collection
argument_list|<
name|E
argument_list|>
name|copy
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|elements
argument_list|)
decl_stmt|;
for|for
control|(
name|E
name|element
range|:
name|copy
control|)
block|{
name|constraint
operator|.
name|checkElement
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
return|return
name|copy
return|;
block|}
block|}
end_class

end_unit

