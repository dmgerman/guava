begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|collect
operator|.
name|Multiset
operator|.
name|Entry
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A {@link Multiset} whose contents will never change, with many other important properties  * detailed at {@link ImmutableCollection}.  *  *<p><b>Grouped iteration.</b> In all current implementations, duplicate elements always appear  * consecutively when iterating. Elements iterate in order by the<i>first</i> appearance of  * that element when the multiset was created.  *  *<p>See the Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/ImmutableCollectionsExplained">  * immutable collections</a>.  *  * @author Jared Levy  * @author Louis Wasserman  * @since 2.0  */
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
comment|// we're overriding default serialization
comment|// TODO(user): write an efficient asList() implementation
DECL|class|ImmutableMultiset
specifier|public
specifier|abstract
class|class
name|ImmutableMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableCollection
argument_list|<
name|E
argument_list|>
implements|implements
name|Multiset
argument_list|<
name|E
argument_list|>
block|{
comment|/**    * Returns the empty immutable multiset.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// all supported methods are covariant
DECL|method|of ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|of
parameter_list|()
block|{
return|return
operator|(
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
operator|)
name|RegularImmutableMultiset
operator|.
name|EMPTY
return|;
block|}
comment|/**    * Returns an immutable multiset containing a single element.    *    * @throws NullPointerException if {@code element} is null    * @since 6.0 (source-compatible since 2.0)    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// generic array created but never written
DECL|method|of (E element)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|of
parameter_list|(
name|E
name|element
parameter_list|)
block|{
return|return
name|copyFromElements
argument_list|(
name|element
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable multiset containing the given elements, in order.    *    * @throws NullPointerException if any element is null    * @since 6.0 (source-compatible since 2.0)    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|//
DECL|method|of (E e1, E e2)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|of
parameter_list|(
name|E
name|e1
parameter_list|,
name|E
name|e2
parameter_list|)
block|{
return|return
name|copyFromElements
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable multiset containing the given elements, in the "grouped iteration order"    * described in the class documentation.    *    * @throws NullPointerException if any element is null    * @since 6.0 (source-compatible since 2.0)    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|//
DECL|method|of (E e1, E e2, E e3)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|of
parameter_list|(
name|E
name|e1
parameter_list|,
name|E
name|e2
parameter_list|,
name|E
name|e3
parameter_list|)
block|{
return|return
name|copyFromElements
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|,
name|e3
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable multiset containing the given elements, in the "grouped iteration order"    * described in the class documentation.    *    * @throws NullPointerException if any element is null    * @since 6.0 (source-compatible since 2.0)    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|//
DECL|method|of (E e1, E e2, E e3, E e4)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|of
parameter_list|(
name|E
name|e1
parameter_list|,
name|E
name|e2
parameter_list|,
name|E
name|e3
parameter_list|,
name|E
name|e4
parameter_list|)
block|{
return|return
name|copyFromElements
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|,
name|e3
argument_list|,
name|e4
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable multiset containing the given elements, in the "grouped iteration order"    * described in the class documentation.    *    * @throws NullPointerException if any element is null    * @since 6.0 (source-compatible since 2.0)    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|//
DECL|method|of (E e1, E e2, E e3, E e4, E e5)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|of
parameter_list|(
name|E
name|e1
parameter_list|,
name|E
name|e2
parameter_list|,
name|E
name|e3
parameter_list|,
name|E
name|e4
parameter_list|,
name|E
name|e5
parameter_list|)
block|{
return|return
name|copyFromElements
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|,
name|e3
argument_list|,
name|e4
argument_list|,
name|e5
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable multiset containing the given elements, in the "grouped iteration order"    * described in the class documentation.    *    * @throws NullPointerException if any element is null    * @since 6.0 (source-compatible since 2.0)    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|//
DECL|method|of (E e1, E e2, E e3, E e4, E e5, E e6, E... others)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|of
parameter_list|(
name|E
name|e1
parameter_list|,
name|E
name|e2
parameter_list|,
name|E
name|e3
parameter_list|,
name|E
name|e4
parameter_list|,
name|E
name|e5
parameter_list|,
name|E
name|e6
parameter_list|,
name|E
modifier|...
name|others
parameter_list|)
block|{
return|return
operator|new
name|Builder
argument_list|<
name|E
argument_list|>
argument_list|()
operator|.
name|add
argument_list|(
name|e1
argument_list|)
operator|.
name|add
argument_list|(
name|e2
argument_list|)
operator|.
name|add
argument_list|(
name|e3
argument_list|)
operator|.
name|add
argument_list|(
name|e4
argument_list|)
operator|.
name|add
argument_list|(
name|e5
argument_list|)
operator|.
name|add
argument_list|(
name|e6
argument_list|)
operator|.
name|add
argument_list|(
name|others
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**    * Returns an immutable multiset containing the given elements, in the "grouped iteration order"    * described in the class documentation.    *    * @throws NullPointerException if any of {@code elements} is null    * @since 6.0    */
DECL|method|copyOf (E[] elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|copyOf
parameter_list|(
name|E
index|[]
name|elements
parameter_list|)
block|{
return|return
name|copyFromElements
argument_list|(
name|elements
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable multiset containing the given elements, in the "grouped iteration order"    * described in the class documentation.    *    * @throws NullPointerException if any of {@code elements} is null    */
DECL|method|copyOf (Iterable<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|copyOf
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
parameter_list|)
block|{
if|if
condition|(
name|elements
operator|instanceof
name|ImmutableMultiset
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// all supported methods are covariant
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|result
init|=
operator|(
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
operator|)
name|elements
decl_stmt|;
if|if
condition|(
operator|!
name|result
operator|.
name|isPartialView
argument_list|()
condition|)
block|{
return|return
name|result
return|;
block|}
block|}
name|Multiset
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|multiset
init|=
operator|(
name|elements
operator|instanceof
name|Multiset
operator|)
condition|?
name|Multisets
operator|.
name|cast
argument_list|(
name|elements
argument_list|)
else|:
name|LinkedHashMultiset
operator|.
name|create
argument_list|(
name|elements
argument_list|)
decl_stmt|;
return|return
name|copyFromEntries
argument_list|(
name|multiset
operator|.
name|entrySet
argument_list|()
argument_list|)
return|;
block|}
DECL|method|copyFromElements (E... elements)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|copyFromElements
parameter_list|(
name|E
modifier|...
name|elements
parameter_list|)
block|{
name|Multiset
argument_list|<
name|E
argument_list|>
name|multiset
init|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|multiset
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|copyFromEntries
argument_list|(
name|multiset
operator|.
name|entrySet
argument_list|()
argument_list|)
return|;
block|}
DECL|method|copyFromEntries ( Collection<? extends Entry<? extends E>> entries)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|copyFromEntries
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Entry
argument_list|<
name|?
extends|extends
name|E
argument_list|>
argument_list|>
name|entries
parameter_list|)
block|{
if|if
condition|(
name|entries
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|of
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|RegularImmutableMultiset
argument_list|<
name|E
argument_list|>
argument_list|(
name|entries
argument_list|)
return|;
block|}
block|}
comment|/**    * Returns an immutable multiset containing the given elements, in the "grouped iteration order"    * described in the class documentation.    *    * @throws NullPointerException if any of {@code elements} is null    */
DECL|method|copyOf (Iterator<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|copyOf
parameter_list|(
name|Iterator
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
parameter_list|)
block|{
name|Multiset
argument_list|<
name|E
argument_list|>
name|multiset
init|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|Iterators
operator|.
name|addAll
argument_list|(
name|multiset
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|copyFromEntries
argument_list|(
name|multiset
operator|.
name|entrySet
argument_list|()
argument_list|)
return|;
block|}
DECL|method|ImmutableMultiset ()
name|ImmutableMultiset
parameter_list|()
block|{}
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
specifier|final
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entryIterator
init|=
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
operator|new
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
name|int
name|remaining
decl_stmt|;
name|E
name|element
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
operator|(
name|remaining
operator|>
literal|0
operator|)
operator|||
name|entryIterator
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
if|if
condition|(
name|remaining
operator|<=
literal|0
condition|)
block|{
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
init|=
name|entryIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|element
operator|=
name|entry
operator|.
name|getElement
argument_list|()
expr_stmt|;
name|remaining
operator|=
name|entry
operator|.
name|getCount
argument_list|()
expr_stmt|;
block|}
name|remaining
operator|--
expr_stmt|;
return|return
name|element
return|;
block|}
block|}
return|;
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
return|return
name|count
argument_list|(
name|object
argument_list|)
operator|>
literal|0
return|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|add (E element, int occurrences)
specifier|public
specifier|final
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
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|remove (Object element, int occurrences)
specifier|public
specifier|final
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
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|setCount (E element, int count)
specifier|public
specifier|final
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
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|setCount (E element, int oldCount, int newCount)
specifier|public
specifier|final
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
annotation|@
name|GwtIncompatible
argument_list|(
literal|"not present in emulated superclass"
argument_list|)
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
for|for
control|(
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
range|:
name|entrySet
argument_list|()
control|)
block|{
name|Arrays
operator|.
name|fill
argument_list|(
name|dst
argument_list|,
name|offset
argument_list|,
name|offset
operator|+
name|entry
operator|.
name|getCount
argument_list|()
argument_list|,
name|entry
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|offset
operator|+=
name|entry
operator|.
name|getCount
argument_list|()
expr_stmt|;
block|}
return|return
name|offset
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
return|return
name|Multisets
operator|.
name|equalsImpl
argument_list|(
name|this
argument_list|,
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Sets
operator|.
name|hashCodeImpl
argument_list|(
name|entrySet
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|entrySet
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|field|entrySet
specifier|private
specifier|transient
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entrySet
decl_stmt|;
annotation|@
name|Override
DECL|method|entrySet ()
specifier|public
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
name|ImmutableSet
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
operator|(
name|entrySet
operator|=
name|createEntrySet
argument_list|()
operator|)
else|:
name|es
return|;
block|}
DECL|method|createEntrySet ()
specifier|private
specifier|final
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|createEntrySet
parameter_list|()
block|{
return|return
name|isEmpty
argument_list|()
condition|?
name|ImmutableSet
operator|.
expr|<
name|Entry
argument_list|<
name|E
argument_list|>
operator|>
name|of
argument_list|()
else|:
operator|new
name|EntrySet
argument_list|()
return|;
block|}
DECL|method|getEntry (int index)
specifier|abstract
name|Entry
argument_list|<
name|E
argument_list|>
name|getEntry
parameter_list|(
name|int
name|index
parameter_list|)
function_decl|;
DECL|class|EntrySet
specifier|private
specifier|final
class|class
name|EntrySet
extends|extends
name|ImmutableSet
operator|.
name|Indexed
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
name|ImmutableMultiset
operator|.
name|this
operator|.
name|isPartialView
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|get (int index)
name|Entry
argument_list|<
name|E
argument_list|>
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|getEntry
argument_list|(
name|index
argument_list|)
return|;
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
name|elementSet
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|contains (Object o)
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
if|if
condition|(
name|entry
operator|.
name|getCount
argument_list|()
operator|<=
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|count
init|=
name|count
argument_list|(
name|entry
operator|.
name|getElement
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|count
operator|==
name|entry
operator|.
name|getCount
argument_list|()
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|ImmutableMultiset
operator|.
name|this
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|// We can't label this with @Override, because it doesn't override anything
comment|// in the GWT emulated version.
comment|// TODO(cpovirk): try making all copies of this method @GwtIncompatible instead
DECL|method|writeReplace ()
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|EntrySetSerializedForm
argument_list|<
name|E
argument_list|>
argument_list|(
name|ImmutableMultiset
operator|.
name|this
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
DECL|class|EntrySetSerializedForm
specifier|static
class|class
name|EntrySetSerializedForm
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Serializable
block|{
DECL|field|multiset
specifier|final
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|multiset
decl_stmt|;
DECL|method|EntrySetSerializedForm (ImmutableMultiset<E> multiset)
name|EntrySetSerializedForm
parameter_list|(
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|multiset
parameter_list|)
block|{
name|this
operator|.
name|multiset
operator|=
name|multiset
expr_stmt|;
block|}
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|multiset
operator|.
name|entrySet
argument_list|()
return|;
block|}
block|}
DECL|class|SerializedForm
specifier|private
specifier|static
class|class
name|SerializedForm
implements|implements
name|Serializable
block|{
DECL|field|elements
specifier|final
name|Object
index|[]
name|elements
decl_stmt|;
DECL|field|counts
specifier|final
name|int
index|[]
name|counts
decl_stmt|;
DECL|method|SerializedForm (Multiset<?> multiset)
name|SerializedForm
parameter_list|(
name|Multiset
argument_list|<
name|?
argument_list|>
name|multiset
parameter_list|)
block|{
name|int
name|distinct
init|=
name|multiset
operator|.
name|entrySet
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|elements
operator|=
operator|new
name|Object
index|[
name|distinct
index|]
expr_stmt|;
name|counts
operator|=
operator|new
name|int
index|[
name|distinct
index|]
expr_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|?
argument_list|>
name|entry
range|:
name|multiset
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|elements
index|[
name|i
index|]
operator|=
name|entry
operator|.
name|getElement
argument_list|()
expr_stmt|;
name|counts
index|[
name|i
index|]
operator|=
name|entry
operator|.
name|getCount
argument_list|()
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
block|}
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
name|LinkedHashMultiset
argument_list|<
name|Object
argument_list|>
name|multiset
init|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|(
name|elements
operator|.
name|length
argument_list|)
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
name|elements
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|multiset
operator|.
name|add
argument_list|(
name|elements
index|[
name|i
index|]
argument_list|,
name|counts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|ImmutableMultiset
operator|.
name|copyOf
argument_list|(
name|multiset
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
comment|// We can't label this with @Override, because it doesn't override anything
comment|// in the GWT emulated version.
DECL|method|writeReplace ()
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|SerializedForm
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**    * Returns a new builder. The generated builder is equivalent to the builder    * created by the {@link Builder} constructor.    */
DECL|method|builder ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Builder
argument_list|<
name|E
argument_list|>
name|builder
parameter_list|()
block|{
return|return
operator|new
name|Builder
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * A builder for creating immutable multiset instances, especially {@code    * public static final} multisets ("constant multisets"). Example:    *<pre> {@code    *    *   public static final ImmutableMultiset<Bean> BEANS =    *       new ImmutableMultiset.Builder<Bean>()    *           .addCopies(Bean.COCOA, 4)    *           .addCopies(Bean.GARDEN, 6)    *           .addCopies(Bean.RED, 8)    *           .addCopies(Bean.BLACK_EYED, 10)    *           .build();}</pre>    *    *<p>Builder instances can be reused; it is safe to call {@link #build} multiple    * times to build multiple multisets in series.    *    * @since 2.0    */
DECL|class|Builder
specifier|public
specifier|static
class|class
name|Builder
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableCollection
operator|.
name|Builder
argument_list|<
name|E
argument_list|>
block|{
DECL|field|contents
specifier|final
name|Multiset
argument_list|<
name|E
argument_list|>
name|contents
decl_stmt|;
comment|/**      * Creates a new builder. The returned builder is equivalent to the builder      * generated by {@link ImmutableMultiset#builder}.      */
DECL|method|Builder ()
specifier|public
name|Builder
parameter_list|()
block|{
name|this
argument_list|(
name|LinkedHashMultiset
operator|.
expr|<
name|E
operator|>
name|create
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|Builder (Multiset<E> contents)
name|Builder
parameter_list|(
name|Multiset
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
block|{
name|this
operator|.
name|contents
operator|=
name|contents
expr_stmt|;
block|}
comment|/**      * Adds {@code element} to the {@code ImmutableMultiset}.      *      * @param element the element to add      * @return this {@code Builder} object      * @throws NullPointerException if {@code element} is null      */
annotation|@
name|Override
DECL|method|add (E element)
specifier|public
name|Builder
argument_list|<
name|E
argument_list|>
name|add
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|contents
operator|.
name|add
argument_list|(
name|checkNotNull
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds a number of occurrences of an element to this {@code      * ImmutableMultiset}.      *      * @param element the element to add      * @param occurrences the number of occurrences of the element to add. May      *     be zero, in which case no change will be made.      * @return this {@code Builder} object      * @throws NullPointerException if {@code element} is null      * @throws IllegalArgumentException if {@code occurrences} is negative, or      *     if this operation would result in more than {@link Integer#MAX_VALUE}      *     occurrences of the element      */
DECL|method|addCopies (E element, int occurrences)
specifier|public
name|Builder
argument_list|<
name|E
argument_list|>
name|addCopies
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
name|contents
operator|.
name|add
argument_list|(
name|checkNotNull
argument_list|(
name|element
argument_list|)
argument_list|,
name|occurrences
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds or removes the necessary occurrences of an element such that the      * element attains the desired count.      *      * @param element the element to add or remove occurrences of      * @param count the desired count of the element in this multiset      * @return this {@code Builder} object      * @throws NullPointerException if {@code element} is null      * @throws IllegalArgumentException if {@code count} is negative      */
DECL|method|setCount (E element, int count)
specifier|public
name|Builder
argument_list|<
name|E
argument_list|>
name|setCount
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|contents
operator|.
name|setCount
argument_list|(
name|checkNotNull
argument_list|(
name|element
argument_list|)
argument_list|,
name|count
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds each element of {@code elements} to the {@code ImmutableMultiset}.      *      * @param elements the elements to add      * @return this {@code Builder} object      * @throws NullPointerException if {@code elements} is null or contains a      *     null element      */
annotation|@
name|Override
DECL|method|add (E... elements)
specifier|public
name|Builder
argument_list|<
name|E
argument_list|>
name|add
parameter_list|(
name|E
modifier|...
name|elements
parameter_list|)
block|{
name|super
operator|.
name|add
argument_list|(
name|elements
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds each element of {@code elements} to the {@code ImmutableMultiset}.      *      * @param elements the {@code Iterable} to add to the {@code      *     ImmutableMultiset}      * @return this {@code Builder} object      * @throws NullPointerException if {@code elements} is null or contains a      *     null element      */
annotation|@
name|Override
DECL|method|addAll (Iterable<? extends E> elements)
specifier|public
name|Builder
argument_list|<
name|E
argument_list|>
name|addAll
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|E
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
name|Multiset
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|multiset
init|=
name|Multisets
operator|.
name|cast
argument_list|(
name|elements
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|entry
range|:
name|multiset
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|addCopies
argument_list|(
name|entry
operator|.
name|getElement
argument_list|()
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|super
operator|.
name|addAll
argument_list|(
name|elements
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Adds each element of {@code elements} to the {@code ImmutableMultiset}.      *      * @param elements the elements to add to the {@code ImmutableMultiset}      * @return this {@code Builder} object      * @throws NullPointerException if {@code elements} is null or contains a      *     null element      */
annotation|@
name|Override
DECL|method|addAll (Iterator<? extends E> elements)
specifier|public
name|Builder
argument_list|<
name|E
argument_list|>
name|addAll
parameter_list|(
name|Iterator
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
parameter_list|)
block|{
name|super
operator|.
name|addAll
argument_list|(
name|elements
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Returns a newly-created {@code ImmutableMultiset} based on the contents      * of the {@code Builder}.      */
annotation|@
name|Override
DECL|method|build ()
specifier|public
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|build
parameter_list|()
block|{
return|return
name|copyOf
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

