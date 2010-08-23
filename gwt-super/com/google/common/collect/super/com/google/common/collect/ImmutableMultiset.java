begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|primitives
operator|.
name|Ints
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|List
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * An immutable hash-based multiset. Does not permit null elements.  *  *<p>Its iterator orders elements according to the first appearance of the  * element among the items passed to the factory method or builder. When the  * multiset contains multiple instances of an element, those instances are  * consecutive in the iteration order.  *  * @author Jared Levy  * @since 2 (imported from Google Collections Library)  */
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
DECL|class|ImmutableMultiset
specifier|public
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
name|EmptyImmutableMultiset
operator|.
name|INSTANCE
return|;
block|}
comment|/**    * Returns an immutable multiset containing a single element.    *    * @throws NullPointerException if {@code element} is null    * @since 6 (source-compatible since release 2)    */
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
name|copyOfInternal
argument_list|(
name|element
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable multiset containing the given elements, in order.    *    * @throws NullPointerException if any element is null    * @since 6 (source-compatible since release 2)    */
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
name|copyOfInternal
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable multiset containing the given elements, in order.    *    * @throws NullPointerException if any element is null    * @since 6 (source-compatible since release 2)    */
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
name|copyOfInternal
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|,
name|e3
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable multiset containing the given elements, in order.    *    * @throws NullPointerException if any element is null    * @since 6 (source-compatible since release 2)    */
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
name|copyOfInternal
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
comment|/**    * Returns an immutable multiset containing the given elements, in order.    *    * @throws NullPointerException if any element is null    * @since 6 (source-compatible since release 2)    */
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
name|copyOfInternal
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
comment|/**    * Returns an immutable multiset containing the given elements, in order.    *    * @throws NullPointerException if any element is null    * @since 6 (source-compatible since release 2)    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|//
DECL|method|of ( E e1, E e2, E e3, E e4, E e5, E e6, E... others)
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
name|int
name|size
init|=
name|others
operator|.
name|length
operator|+
literal|6
decl_stmt|;
name|List
argument_list|<
name|E
argument_list|>
name|all
init|=
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|(
name|size
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|all
argument_list|,
name|e1
argument_list|,
name|e2
argument_list|,
name|e3
argument_list|,
name|e4
argument_list|,
name|e5
argument_list|,
name|e6
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|all
argument_list|,
name|others
argument_list|)
expr_stmt|;
return|return
name|copyOf
argument_list|(
name|all
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable multiset containing the given elements.    *    *<p>The multiset is ordered by the first occurrence of each element. For    * example, {@code ImmutableMultiset.of(2, 3, 1, 3)} yields a multiset with    * elements in the order {@code 2, 3, 3, 1}.    *    * @throws NullPointerException if any of {@code elements} is null    * @deprecated use {@link #copyOf(Object[])}.    * @since 2 (changed from varargs in release 6)    */
annotation|@
name|Deprecated
DECL|method|of (E[] elements)
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
index|[]
name|elements
parameter_list|)
block|{
return|return
name|copyOf
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
comment|/**    * Returns an immutable multiset containing the given elements.    *    *<p>The multiset is ordered by the first occurrence of each element. For    * example, {@code ImmutableMultiset.copyOf([2, 3, 1, 3])} yields a multiset    * with elements in the order {@code 2, 3, 3, 1}.    *    * @throws NullPointerException if any of {@code elements} is null    * @since 6    */
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
name|copyOf
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
comment|/**    * Returns an immutable multiset containing the given elements.    *    *<p>The multiset is ordered by the first occurrence of each element. For    * example, {@code ImmutableMultiset.copyOf(Arrays.asList(2, 3, 1, 3))} yields    * a multiset with elements in the order {@code 2, 3, 3, 1}.    *    *<p>Note that if {@code c} is a {@code Collection<String>}, then {@code    * ImmutableMultiset.copyOf(c)} returns an {@code ImmutableMultiset<String>}    * containing each of the strings in {@code c}, while    * {@code ImmutableMultiset.of(c)} returns an    * {@code ImmutableMultiset<Collection<String>>} containing one element (the    * given collection itself).    *    *<p><b>Note:</b> Despite what the method name suggests, if {@code elements}    * is an {@code ImmutableMultiset}, no copy will actually be performed, and    * the given multiset itself will be returned.    *    * @throws NullPointerException if any of {@code elements} is null    */
DECL|method|copyOf ( Iterable<? extends E> elements)
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
return|return
name|result
return|;
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
name|copyOfInternal
argument_list|(
name|multiset
argument_list|)
return|;
block|}
DECL|method|copyOfInternal (E... elements)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|copyOfInternal
parameter_list|(
name|E
modifier|...
name|elements
parameter_list|)
block|{
return|return
name|copyOf
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
DECL|method|copyOfInternal ( Multiset<? extends E> multiset)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|copyOfInternal
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
name|long
name|size
init|=
literal|0
decl_stmt|;
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
name|builder
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
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
name|int
name|count
init|=
name|entry
operator|.
name|getCount
argument_list|()
decl_stmt|;
if|if
condition|(
name|count
operator|>
literal|0
condition|)
block|{
comment|// Since ImmutableMap.Builder throws an NPE if an element is null, no
comment|// other null checks are needed.
name|builder
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getElement
argument_list|()
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|size
operator|+=
name|count
expr_stmt|;
block|}
block|}
if|if
condition|(
name|size
operator|==
literal|0
condition|)
block|{
return|return
name|of
argument_list|()
return|;
block|}
return|return
operator|new
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|,
name|Ints
operator|.
name|saturatedCast
argument_list|(
name|size
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable multiset containing the given elements.    *    *<p>The multiset is ordered by the first occurrence of each element. For    * example,    * {@code ImmutableMultiset.copyOf(Arrays.asList(2, 3, 1, 3).iterator())}    * yields a multiset with elements in the order {@code 2, 3, 3, 1}.    *    * @throws NullPointerException if any of {@code elements} is null    */
DECL|method|copyOf ( Iterator<? extends E> elements)
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
name|copyOfInternal
argument_list|(
name|multiset
argument_list|)
return|;
block|}
DECL|field|map
specifier|private
specifier|final
specifier|transient
name|ImmutableMap
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
name|map
decl_stmt|;
DECL|field|size
specifier|private
specifier|final
specifier|transient
name|int
name|size
decl_stmt|;
comment|// These constants allow the deserialization code to set final fields. This
comment|// holder class makes sure they are not initialized unless an instance is
comment|// deserialized.
DECL|method|ImmutableMultiset (ImmutableMap<E, Integer> map, int size)
name|ImmutableMultiset
parameter_list|(
name|ImmutableMap
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
name|map
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
block|}
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
name|Integer
name|value
init|=
name|map
operator|.
name|get
argument_list|(
name|element
argument_list|)
decl_stmt|;
return|return
operator|(
name|value
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|value
return|;
block|}
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
specifier|final
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|mapIterator
init|=
name|map
operator|.
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
name|mapIterator
operator|.
name|hasNext
argument_list|()
return|;
block|}
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
name|Map
operator|.
name|Entry
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
name|entry
init|=
name|mapIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|element
operator|=
name|entry
operator|.
name|getKey
argument_list|()
expr_stmt|;
name|remaining
operator|=
name|entry
operator|.
name|getValue
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
DECL|method|contains (@ullable Object element)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|)
block|{
return|return
name|map
operator|.
name|containsKey
argument_list|(
name|element
argument_list|)
return|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    */
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
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    */
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    */
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    */
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
if|if
condition|(
name|this
operator|.
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
for|for
control|(
name|Entry
argument_list|<
name|?
argument_list|>
name|entry
range|:
name|that
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|count
argument_list|(
name|entry
operator|.
name|getElement
argument_list|()
argument_list|)
operator|!=
name|entry
operator|.
name|getCount
argument_list|()
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
comment|// could cache this, but not considered worthwhile to do so
return|return
name|map
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
name|entrySet
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|elementSet ()
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|elementSet
parameter_list|()
block|{
return|return
name|map
operator|.
name|keySet
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
operator|new
name|EntrySet
argument_list|<
name|E
argument_list|>
argument_list|(
name|this
argument_list|)
operator|)
else|:
name|es
return|;
block|}
DECL|class|EntrySet
specifier|private
specifier|static
class|class
name|EntrySet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
block|{
DECL|field|multiset
specifier|final
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|multiset
decl_stmt|;
DECL|method|EntrySet (ImmutableMultiset<E> multiset)
specifier|public
name|EntrySet
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
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
name|UnmodifiableIterator
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
name|Map
operator|.
name|Entry
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|mapIterator
init|=
name|multiset
operator|.
name|map
operator|.
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
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|mapIterator
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
name|Map
operator|.
name|Entry
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
name|mapEntry
init|=
name|mapIterator
operator|.
name|next
argument_list|()
decl_stmt|;
return|return
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|mapEntry
operator|.
name|getKey
argument_list|()
argument_list|,
name|mapEntry
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|multiset
operator|.
name|map
operator|.
name|size
argument_list|()
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
name|multiset
operator|.
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
comment|// TODO(hhchan): Revert once this class is emulated in GWT.
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
name|newArray
init|=
operator|new
name|Object
index|[
name|size
argument_list|()
index|]
decl_stmt|;
return|return
name|toArray
argument_list|(
name|newArray
argument_list|)
return|;
block|}
comment|// TODO(hhchan): Revert once this class is emulated in GWT.
DECL|method|toArray (T[] other)
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
name|other
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
name|other
operator|.
name|length
operator|<
name|size
condition|)
block|{
name|other
operator|=
name|ObjectArrays
operator|.
name|newArray
argument_list|(
name|other
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|other
operator|.
name|length
operator|>
name|size
condition|)
block|{
name|other
index|[
name|size
index|]
operator|=
literal|null
expr_stmt|;
block|}
comment|// Writes will produce ArrayStoreException when the toArray() doc requires
name|Object
index|[]
name|otherAsObjectArray
init|=
name|other
decl_stmt|;
name|int
name|index
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|?
argument_list|>
name|element
range|:
name|this
control|)
block|{
name|otherAsObjectArray
index|[
name|index
operator|++
index|]
operator|=
name|element
expr_stmt|;
block|}
return|return
name|other
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
name|multiset
operator|.
name|map
operator|.
name|hashCode
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
literal|0
decl_stmt|;
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
comment|/**    * A builder for creating immutable multiset instances, especially    * {@code public static final} multisets ("constant multisets").    *    *<p>Example:    *<pre>   {@code    *   public static final ImmutableMultiset<Bean> BEANS    *       = new ImmutableMultiset.Builder<Bean>()    *           .addCopies(Bean.COCOA, 4)    *           .addCopies(Bean.GARDEN, 6)    *           .addCopies(Bean.RED, 8)    *           .addCopies(Bean.BLACK_EYED, 10)    *           .build();}</pre>    *    *<p>Builder instances can be reused - it is safe to call {@link #build}    * multiple times to build multiple multisets in series. Each multiset    * is a superset of the multiset created before it.    *    * @since 2 (imported from Google Collections Library)    */
DECL|class|Builder
specifier|public
specifier|static
specifier|final
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
specifier|private
specifier|final
name|Multiset
argument_list|<
name|E
argument_list|>
name|contents
init|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
comment|/**      * Creates a new builder. The returned builder is equivalent to the builder      * generated by {@link ImmutableMultiset#builder}.      */
DECL|method|Builder ()
specifier|public
name|Builder
parameter_list|()
block|{}
comment|/**      * Adds {@code element} to the {@code ImmutableMultiset}.      *      * @param element the element to add      * @return this {@code Builder} object      * @throws NullPointerException if {@code element} is null      */
DECL|method|add (E element)
annotation|@
name|Override
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
DECL|method|add (E... elements)
annotation|@
name|Override
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
DECL|method|addAll (Iterable<? extends E> elements)
annotation|@
name|Override
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
DECL|method|addAll (Iterator<? extends E> elements)
annotation|@
name|Override
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
DECL|method|build ()
annotation|@
name|Override
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

