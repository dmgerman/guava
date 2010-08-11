begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|base
operator|.
name|Objects
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
name|Iterator
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
comment|/**  * Provides static utility methods for creating and working with {@link  * Multiset} instances.  *  * @author Kevin Bourrillion  * @author Mike Bostock  * @since 2 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Multisets
specifier|public
specifier|final
class|class
name|Multisets
block|{
DECL|method|Multisets ()
specifier|private
name|Multisets
parameter_list|()
block|{}
comment|/**    * Returns an unmodifiable view of the specified multiset. Query operations on    * the returned multiset "read through" to the specified multiset, and    * attempts to modify the returned multiset result in an    * {@link UnsupportedOperationException}.    *    *<p>The returned multiset will be serializable if the specified multiset is    * serializable.    *    * @param multiset the multiset for which an unmodifiable view is to be    *     generated    * @return an unmodifiable view of the multiset    */
DECL|method|unmodifiableMultiset ( Multiset<? extends E> multiset)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Multiset
argument_list|<
name|E
argument_list|>
name|unmodifiableMultiset
parameter_list|(
name|Multiset
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|multiset
parameter_list|)
block|{
return|return
operator|new
name|UnmodifiableMultiset
argument_list|<
name|E
argument_list|>
argument_list|(
name|checkNotNull
argument_list|(
name|multiset
argument_list|)
argument_list|)
return|;
block|}
DECL|class|UnmodifiableMultiset
specifier|private
specifier|static
class|class
name|UnmodifiableMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingMultiset
argument_list|<
name|E
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|delegate
specifier|final
name|Multiset
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|delegate
decl_stmt|;
DECL|method|UnmodifiableMultiset (Multiset<? extends E> delegate)
name|UnmodifiableMultiset
parameter_list|(
name|Multiset
argument_list|<
name|?
extends|extends
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
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
block|{
comment|// This is safe because all non-covariant methods are overriden
return|return
operator|(
name|Multiset
operator|)
name|delegate
return|;
block|}
DECL|field|elementSet
specifier|transient
name|Set
argument_list|<
name|E
argument_list|>
name|elementSet
decl_stmt|;
DECL|method|elementSet ()
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|elementSet
parameter_list|()
block|{
name|Set
argument_list|<
name|E
argument_list|>
name|es
init|=
name|elementSet
decl_stmt|;
return|return
operator|(
name|es
operator|==
literal|null
operator|)
condition|?
name|elementSet
operator|=
name|Collections
operator|.
expr|<
name|E
operator|>
name|unmodifiableSet
argument_list|(
name|delegate
operator|.
name|elementSet
argument_list|()
argument_list|)
else|:
name|es
return|;
block|}
DECL|field|entrySet
specifier|transient
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
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|entrySet ()
annotation|@
name|Override
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
name|Set
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|es
init|=
name|entrySet
decl_stmt|;
return|return
operator|(
name|es
operator|==
literal|null
operator|)
comment|// Safe because the returned set is made unmodifiable and Entry
comment|// itself is readonly
condition|?
name|entrySet
operator|=
operator|(
name|Set
operator|)
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|delegate
operator|.
name|entrySet
argument_list|()
argument_list|)
else|:
name|es
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
comment|// Safe because the returned Iterator is made unmodifiable
return|return
operator|(
name|Iterator
operator|)
name|Iterators
operator|.
name|unmodifiableIterator
argument_list|(
name|delegate
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|add (E element, int occurences)
annotation|@
name|Override
specifier|public
name|int
name|add
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|occurences
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|addAll (Collection<? extends E> elementsToAdd)
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
name|elementsToAdd
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|remove (Object element)
annotation|@
name|Override
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|element
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|remove (Object element, int occurrences)
annotation|@
name|Override
specifier|public
name|int
name|remove
parameter_list|(
name|Object
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|removeAll (Collection<?> elementsToRemove)
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
name|elementsToRemove
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|retainAll (Collection<?> elementsToRetain)
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
name|elementsToRetain
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|clear ()
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|setCount (E element, int count)
annotation|@
name|Override
specifier|public
name|int
name|setCount
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|setCount (E element, int oldCount, int newCount)
annotation|@
name|Override
specifier|public
name|boolean
name|setCount
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|oldCount
parameter_list|,
name|int
name|newCount
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
comment|/**    * Returns an immutable multiset entry with the specified element and count.    *    * @param e the element to be associated with the returned entry    * @param n the count to be associated with the returned entry    * @throws IllegalArgumentException if {@code n} is negative    */
DECL|method|immutableEntry ( @ullable final E e, final int n)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
name|immutableEntry
parameter_list|(
annotation|@
name|Nullable
specifier|final
name|E
name|e
parameter_list|,
specifier|final
name|int
name|n
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|n
operator|>=
literal|0
argument_list|)
expr_stmt|;
return|return
operator|new
name|AbstractEntry
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|E
name|getElement
parameter_list|()
block|{
return|return
name|e
return|;
block|}
specifier|public
name|int
name|getCount
parameter_list|()
block|{
return|return
name|n
return|;
block|}
block|}
return|;
block|}
comment|/**    * Returns a multiset view of the specified set. The multiset is backed by the    * set, so changes to the set are reflected in the multiset, and vice versa.    * If the set is modified while an iteration over the multiset is in progress    * (except through the iterator's own {@code remove} operation) the results of    * the iteration are undefined.    *    *<p>The multiset supports element removal, which removes the corresponding    * element from the set. It does not support the {@code add} or {@code addAll}    * operations, nor does it support the use of {@code setCount} to add    * elements.    *    *<p>The returned multiset will be serializable if the specified set is    * serializable. The multiset is threadsafe if the set is threadsafe.    *    * @param set the backing set for the returned multiset view    */
DECL|method|forSet (Set<E> set)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Multiset
argument_list|<
name|E
argument_list|>
name|forSet
parameter_list|(
name|Set
argument_list|<
name|E
argument_list|>
name|set
parameter_list|)
block|{
return|return
operator|new
name|SetMultiset
argument_list|<
name|E
argument_list|>
argument_list|(
name|set
argument_list|)
return|;
block|}
comment|/** @see Multisets#forSet */
DECL|class|SetMultiset
specifier|private
specifier|static
class|class
name|SetMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingCollection
argument_list|<
name|E
argument_list|>
implements|implements
name|Multiset
argument_list|<
name|E
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|delegate
specifier|final
name|Set
argument_list|<
name|E
argument_list|>
name|delegate
decl_stmt|;
DECL|method|SetMultiset (Set<E> set)
name|SetMultiset
parameter_list|(
name|Set
argument_list|<
name|E
argument_list|>
name|set
parameter_list|)
block|{
name|delegate
operator|=
name|checkNotNull
argument_list|(
name|set
argument_list|)
expr_stmt|;
block|}
DECL|method|delegate ()
annotation|@
name|Override
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
DECL|method|count (Object element)
specifier|public
name|int
name|count
parameter_list|(
name|Object
name|element
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|contains
argument_list|(
name|element
argument_list|)
condition|?
literal|1
else|:
literal|0
return|;
block|}
DECL|method|add (E element, int occurrences)
specifier|public
name|int
name|add
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|remove (Object element, int occurrences)
specifier|public
name|int
name|remove
parameter_list|(
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
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|remove
argument_list|(
name|element
argument_list|)
condition|?
literal|1
else|:
literal|0
return|;
block|}
DECL|field|elementSet
specifier|transient
name|Set
argument_list|<
name|E
argument_list|>
name|elementSet
decl_stmt|;
DECL|method|elementSet ()
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|elementSet
parameter_list|()
block|{
name|Set
argument_list|<
name|E
argument_list|>
name|es
init|=
name|elementSet
decl_stmt|;
return|return
operator|(
name|es
operator|==
literal|null
operator|)
condition|?
name|elementSet
operator|=
operator|new
name|ElementSet
argument_list|()
else|:
name|es
return|;
block|}
DECL|field|entrySet
specifier|transient
name|Set
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entrySet
decl_stmt|;
DECL|method|entrySet ()
specifier|public
name|Set
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
name|Set
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|es
init|=
name|entrySet
decl_stmt|;
return|return
operator|(
name|es
operator|==
literal|null
operator|)
condition|?
name|entrySet
operator|=
operator|new
name|EntrySet
argument_list|()
else|:
name|es
return|;
block|}
DECL|method|add (E o)
annotation|@
name|Override
specifier|public
name|boolean
name|add
parameter_list|(
name|E
name|o
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|addAll (Collection<? extends E> c)
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
name|c
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|setCount (E element, int count)
specifier|public
name|int
name|setCount
parameter_list|(
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
if|if
condition|(
name|count
operator|==
name|count
argument_list|(
name|element
argument_list|)
condition|)
block|{
return|return
name|count
return|;
block|}
elseif|else
if|if
condition|(
name|count
operator|==
literal|0
condition|)
block|{
name|remove
argument_list|(
name|element
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
DECL|method|setCount (E element, int oldCount, int newCount)
specifier|public
name|boolean
name|setCount
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|oldCount
parameter_list|,
name|int
name|newCount
parameter_list|)
block|{
return|return
name|setCountImpl
argument_list|(
name|this
argument_list|,
name|element
argument_list|,
name|oldCount
argument_list|,
name|newCount
argument_list|)
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
operator|instanceof
name|Multiset
condition|)
block|{
name|Multiset
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|Multiset
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|this
operator|.
name|size
argument_list|()
operator|==
name|that
operator|.
name|size
argument_list|()
operator|&&
name|delegate
operator|.
name|equals
argument_list|(
name|that
operator|.
name|elementSet
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
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
name|int
name|sum
init|=
literal|0
decl_stmt|;
for|for
control|(
name|E
name|e
range|:
name|this
control|)
block|{
name|sum
operator|+=
operator|(
operator|(
name|e
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|e
operator|.
name|hashCode
argument_list|()
operator|)
operator|^
literal|1
expr_stmt|;
block|}
return|return
name|sum
return|;
block|}
comment|/** @see SetMultiset#elementSet */
DECL|class|ElementSet
class|class
name|ElementSet
extends|extends
name|ForwardingSet
argument_list|<
name|E
argument_list|>
block|{
DECL|method|delegate ()
annotation|@
name|Override
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
DECL|method|add (E o)
annotation|@
name|Override
specifier|public
name|boolean
name|add
parameter_list|(
name|E
name|o
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|addAll (Collection<? extends E> c)
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
name|c
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
comment|/** @see SetMultiset#entrySet */
DECL|class|EntrySet
class|class
name|EntrySet
extends|extends
name|AbstractSet
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
block|{
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
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|final
name|Iterator
argument_list|<
name|E
argument_list|>
name|elements
init|=
name|delegate
operator|.
name|iterator
argument_list|()
decl_stmt|;
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|elements
operator|.
name|hasNext
argument_list|()
return|;
block|}
specifier|public
name|Entry
argument_list|<
name|E
argument_list|>
name|next
parameter_list|()
block|{
return|return
name|immutableEntry
argument_list|(
name|elements
operator|.
name|next
argument_list|()
argument_list|,
literal|1
argument_list|)
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|elements
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
comment|// TODO(kevinb): faster contains, remove
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
comment|/**    * Returns the expected number of distinct elements given the specified    * elements. The number of distinct elements is only computed if {@code    * elements} is an instance of {@code Multiset}; otherwise the default value    * of 11 is returned.    */
DECL|method|inferDistinctElements (Iterable<?> elements)
specifier|static
name|int
name|inferDistinctElements
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|elements
parameter_list|)
block|{
if|if
condition|(
name|elements
operator|instanceof
name|Multiset
condition|)
block|{
return|return
operator|(
operator|(
name|Multiset
argument_list|<
name|?
argument_list|>
operator|)
name|elements
operator|)
operator|.
name|elementSet
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
return|return
literal|11
return|;
comment|// initial capacity will be rounded up to 16
block|}
comment|/**    * Returns an unmodifiable<b>view</b> of the intersection of two multisets.    * An element's count in the multiset is the smaller of its counts in the two    * backing multisets. The iteration order of the returned multiset matches the    * element set of {@code multiset1}, with repeated occurrences of the same    * element appearing consecutively.    *    *<p>Results are undefined if {@code multiset1} and {@code multiset2} are    * based on different equivalence relations (as {@code HashMultiset} and    * {@code TreeMultiset} are).    *    * @since 2    */
DECL|method|intersection ( final Multiset<E> multiset1, final Multiset<?> multiset2)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Multiset
argument_list|<
name|E
argument_list|>
name|intersection
parameter_list|(
specifier|final
name|Multiset
argument_list|<
name|E
argument_list|>
name|multiset1
parameter_list|,
specifier|final
name|Multiset
argument_list|<
name|?
argument_list|>
name|multiset2
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|multiset1
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|multiset2
argument_list|)
expr_stmt|;
return|return
operator|new
name|AbstractMultiset
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|count
parameter_list|(
name|Object
name|element
parameter_list|)
block|{
name|int
name|count1
init|=
name|multiset1
operator|.
name|count
argument_list|(
name|element
argument_list|)
decl_stmt|;
return|return
operator|(
name|count1
operator|==
literal|0
operator|)
condition|?
literal|0
else|:
name|Math
operator|.
name|min
argument_list|(
name|count1
argument_list|,
name|multiset2
operator|.
name|count
argument_list|(
name|element
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
name|Set
argument_list|<
name|E
argument_list|>
name|createElementSet
parameter_list|()
block|{
return|return
name|Sets
operator|.
name|intersection
argument_list|(
name|multiset1
operator|.
name|elementSet
argument_list|()
argument_list|,
name|multiset2
operator|.
name|elementSet
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
name|entrySet
return|;
block|}
specifier|final
name|Set
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entrySet
init|=
operator|new
name|AbstractSet
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
specifier|final
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|iterator1
init|=
name|multiset1
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
operator|new
name|AbstractIterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Entry
argument_list|<
name|E
argument_list|>
name|computeNext
parameter_list|()
block|{
while|while
condition|(
name|iterator1
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Entry
argument_list|<
name|E
argument_list|>
name|entry1
init|=
name|iterator1
operator|.
name|next
argument_list|()
decl_stmt|;
name|E
name|element
init|=
name|entry1
operator|.
name|getElement
argument_list|()
decl_stmt|;
name|int
name|count
init|=
name|Math
operator|.
name|min
argument_list|(
name|entry1
operator|.
name|getCount
argument_list|()
argument_list|,
name|multiset2
operator|.
name|count
argument_list|(
name|element
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|count
operator|>
literal|0
condition|)
block|{
return|return
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
return|;
block|}
block|}
return|return
name|endOfData
argument_list|()
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|elementSet
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
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
operator|instanceof
name|Entry
condition|)
block|{
name|Entry
argument_list|<
name|?
argument_list|>
name|entry
init|=
operator|(
name|Entry
argument_list|<
name|?
argument_list|>
operator|)
name|o
decl_stmt|;
name|int
name|entryCount
init|=
name|entry
operator|.
name|getCount
argument_list|()
decl_stmt|;
return|return
operator|(
name|entryCount
operator|>
literal|0
operator|)
operator|&&
operator|(
name|count
argument_list|(
name|entry
operator|.
name|getElement
argument_list|()
argument_list|)
operator|==
name|entryCount
operator|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|elementSet
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
decl_stmt|;
block|}
return|;
block|}
comment|/**    * Implementation of the {@code equals}, {@code hashCode}, and    * {@code toString} methods of {@link Multiset.Entry}.    */
DECL|class|AbstractEntry
specifier|abstract
specifier|static
class|class
name|AbstractEntry
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
block|{
comment|/**      * Indicates whether an object equals this entry, following the behavior      * specified in {@link Multiset.Entry#equals}.      */
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
operator|instanceof
name|Multiset
operator|.
name|Entry
condition|)
block|{
name|Multiset
operator|.
name|Entry
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|Multiset
operator|.
name|Entry
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|this
operator|.
name|getCount
argument_list|()
operator|==
name|that
operator|.
name|getCount
argument_list|()
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|this
operator|.
name|getElement
argument_list|()
argument_list|,
name|that
operator|.
name|getElement
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Return this entry's hash code, following the behavior specified in      * {@link Multiset.Entry#hashCode}.      */
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|E
name|e
init|=
name|getElement
argument_list|()
decl_stmt|;
return|return
operator|(
operator|(
name|e
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|e
operator|.
name|hashCode
argument_list|()
operator|)
operator|^
name|getCount
argument_list|()
return|;
block|}
comment|/**      * Returns a string representation of this multiset entry. The string      * representation consists of the associated element if the associated count      * is one, and otherwise the associated element followed by the characters      * " x " (space, x and space) followed by the count. Elements and counts are      * converted to strings as by {@code String.valueOf}.      */
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|text
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|getElement
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|n
init|=
name|getCount
argument_list|()
decl_stmt|;
return|return
operator|(
name|n
operator|==
literal|1
operator|)
condition|?
name|text
else|:
operator|(
name|text
operator|+
literal|" x "
operator|+
name|n
operator|)
return|;
block|}
block|}
DECL|method|setCountImpl (Multiset<E> self, E element, int count)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|int
name|setCountImpl
parameter_list|(
name|Multiset
argument_list|<
name|E
argument_list|>
name|self
parameter_list|,
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
name|int
name|oldCount
init|=
name|self
operator|.
name|count
argument_list|(
name|element
argument_list|)
decl_stmt|;
name|int
name|delta
init|=
name|count
operator|-
name|oldCount
decl_stmt|;
if|if
condition|(
name|delta
operator|>
literal|0
condition|)
block|{
name|self
operator|.
name|add
argument_list|(
name|element
argument_list|,
name|delta
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|delta
operator|<
literal|0
condition|)
block|{
name|self
operator|.
name|remove
argument_list|(
name|element
argument_list|,
operator|-
name|delta
argument_list|)
expr_stmt|;
block|}
return|return
name|oldCount
return|;
block|}
DECL|method|setCountImpl ( Multiset<E> self, E element, int oldCount, int newCount)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|boolean
name|setCountImpl
parameter_list|(
name|Multiset
argument_list|<
name|E
argument_list|>
name|self
parameter_list|,
name|E
name|element
parameter_list|,
name|int
name|oldCount
parameter_list|,
name|int
name|newCount
parameter_list|)
block|{
name|checkNonnegative
argument_list|(
name|oldCount
argument_list|,
literal|"oldCount"
argument_list|)
expr_stmt|;
name|checkNonnegative
argument_list|(
name|newCount
argument_list|,
literal|"newCount"
argument_list|)
expr_stmt|;
if|if
condition|(
name|self
operator|.
name|count
argument_list|(
name|element
argument_list|)
operator|==
name|oldCount
condition|)
block|{
name|self
operator|.
name|setCount
argument_list|(
name|element
argument_list|,
name|newCount
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|checkNonnegative (int count, String name)
specifier|static
name|void
name|checkNonnegative
parameter_list|(
name|int
name|count
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|count
operator|>=
literal|0
argument_list|,
literal|"%s cannot be negative: %s"
argument_list|,
name|name
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

