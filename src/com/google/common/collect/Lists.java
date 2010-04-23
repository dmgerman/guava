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
name|VisibleForTesting
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
name|Function
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
name|checkElementIndex
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
name|AbstractList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractSequentialList
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
name|LinkedList
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Static utility methods pertaining to {@link List} instances. Also see this  * class's counterparts {@link Sets} and {@link Maps}.  *  * @author Kevin Bourrillion  * @author Mike Bostock  * @since 2 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Lists
specifier|public
specifier|final
class|class
name|Lists
block|{
DECL|method|Lists ()
specifier|private
name|Lists
parameter_list|()
block|{}
comment|// ArrayList
comment|/**    * Creates a<i>mutable</i>, empty {@code ArrayList} instance.    *    *<p><b>Note:</b> if mutability is not required, use {@link    * ImmutableList#of()} instead.    *    * @return a new, empty {@code ArrayList}    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|newArrayList ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ArrayList
argument_list|<
name|E
argument_list|>
name|newArrayList
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Creates a<i>mutable</i> {@code ArrayList} instance containing the given    * elements.    *    *<p><b>Note:</b> if mutability is not required and the elements are    * non-null, use an overload of {@link ImmutableList#of()} (for varargs) or    * {@link ImmutableList#copyOf(Object[])} (for an array) instead.    *    * @param elements the elements that the list should contain, in order    * @return a new {@code ArrayList} containing those elements    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|newArrayList (E... elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ArrayList
argument_list|<
name|E
argument_list|>
name|newArrayList
parameter_list|(
name|E
modifier|...
name|elements
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|elements
argument_list|)
expr_stmt|;
comment|// for GWT
comment|// Avoid integer overflow when a large array is passed in
name|int
name|capacity
init|=
name|computeArrayListCapacity
argument_list|(
name|elements
operator|.
name|length
argument_list|)
decl_stmt|;
name|ArrayList
argument_list|<
name|E
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|(
name|capacity
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|list
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
DECL|method|computeArrayListCapacity (int arraySize)
annotation|@
name|VisibleForTesting
specifier|static
name|int
name|computeArrayListCapacity
parameter_list|(
name|int
name|arraySize
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|arraySize
operator|>=
literal|0
argument_list|)
expr_stmt|;
comment|// TODO: Figure out the right behavior, and document it
return|return
operator|(
name|int
operator|)
name|Math
operator|.
name|min
argument_list|(
literal|5L
operator|+
name|arraySize
operator|+
operator|(
name|arraySize
operator|/
literal|10
operator|)
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
return|;
block|}
comment|/**    * Creates a<i>mutable</i> {@code ArrayList} instance containing the given    * elements.    *    *<p><b>Note:</b> if mutability is not required and the elements are    * non-null, use {@link ImmutableList#copyOf(Iterator)} instead.    *    * @param elements the elements that the list should contain, in order    * @return a new {@code ArrayList} containing those elements    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|newArrayList (Iterable<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ArrayList
argument_list|<
name|E
argument_list|>
name|newArrayList
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
name|checkNotNull
argument_list|(
name|elements
argument_list|)
expr_stmt|;
comment|// for GWT
comment|// Let ArrayList's sizing logic work, if possible
if|if
condition|(
name|elements
operator|instanceof
name|Collection
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|collection
init|=
operator|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
operator|)
name|elements
decl_stmt|;
return|return
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|(
name|collection
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|newArrayList
argument_list|(
name|elements
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**    * Creates a<i>mutable</i> {@code ArrayList} instance containing the given    * elements.    *    *<p><b>Note:</b> if mutability is not required and the elements are    * non-null, use {@link ImmutableList#copyOf(Iterator)} instead.    *    * @param elements the elements that the list should contain, in order    * @return a new {@code ArrayList} containing those elements    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|newArrayList (Iterator<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ArrayList
argument_list|<
name|E
argument_list|>
name|newArrayList
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
name|checkNotNull
argument_list|(
name|elements
argument_list|)
expr_stmt|;
comment|// for GWT
name|ArrayList
argument_list|<
name|E
argument_list|>
name|list
init|=
name|newArrayList
argument_list|()
decl_stmt|;
while|while
condition|(
name|elements
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|elements
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
comment|/**    * Creates an {@code ArrayList} instance backed by an array of the    *<i>exact</i> size specified; equivalent to    * {@link ArrayList#ArrayList(int)}.    *    *<p><b>Note:</b> if you know the exact size your list will be, consider    * using a fixed-size list ({@link Arrays#asList(Object[])}) or an {@link    * ImmutableList} instead of a growable {@link ArrayList}.    *    *<p><b>Note:</b> If you have only an<i>estimate</i> of the eventual size of    * the list, consider padding this estimate by a suitable amount, or simply    * use {@link #newArrayListWithExpectedSize(int)} instead.    *    * @param initialArraySize the exact size of the initial backing array for    *     the returned array list ({@code ArrayList} documentation calls this    *     value the "capacity")    * @return a new, empty {@code ArrayList} which is guaranteed not to resize    *     itself unless its size reaches {@code initialArraySize + 1}    * @throws IllegalArgumentException if {@code initialArraySize} is negative    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|newArrayListWithCapacity ( int initialArraySize)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ArrayList
argument_list|<
name|E
argument_list|>
name|newArrayListWithCapacity
parameter_list|(
name|int
name|initialArraySize
parameter_list|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|(
name|initialArraySize
argument_list|)
return|;
block|}
comment|/**    * Creates an {@code ArrayList} instance sized appropriately to hold an    *<i>estimated</i> number of elements without resizing. A small amount of    * padding is added in case the estimate is low.    *    *<p><b>Note:</b> If you know the<i>exact</i> number of elements the list    * will hold, or prefer to calculate your own amount of padding, refer to    * {@link #newArrayListWithCapacity(int)}.    *    * @param estimatedSize an estimate of the eventual {@link List#size()} of    *     the new list    * @return a new, empty {@code ArrayList}, sized appropriately to hold the    *     estimated number of elements    * @throws IllegalArgumentException if {@code estimatedSize} is negative    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|newArrayListWithExpectedSize ( int estimatedSize)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ArrayList
argument_list|<
name|E
argument_list|>
name|newArrayListWithExpectedSize
parameter_list|(
name|int
name|estimatedSize
parameter_list|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|(
name|computeArrayListCapacity
argument_list|(
name|estimatedSize
argument_list|)
argument_list|)
return|;
block|}
comment|// LinkedList
comment|/**    * Creates an empty {@code LinkedList} instance.    *    *<p><b>Note:</b> if you need an immutable empty {@link List}, use    * {@link Collections#emptyList} instead.    *    * @return a new, empty {@code LinkedList}    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|newLinkedList ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|LinkedList
argument_list|<
name|E
argument_list|>
name|newLinkedList
parameter_list|()
block|{
return|return
operator|new
name|LinkedList
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Creates a {@code LinkedList} instance containing the given elements.    *    * @param elements the elements that the list should contain, in order    * @return a new {@code LinkedList} containing those elements    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|newLinkedList ( Iterable<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|LinkedList
argument_list|<
name|E
argument_list|>
name|newLinkedList
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
name|LinkedList
argument_list|<
name|E
argument_list|>
name|list
init|=
name|newLinkedList
argument_list|()
decl_stmt|;
for|for
control|(
name|E
name|element
range|:
name|elements
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
comment|/**    * Returns an unmodifiable list containing the specified first element and    * backed by the specified array of additional elements. Changes to the {@code    * rest} array will be reflected in the returned list. Unlike {@link    * Arrays#asList}, the returned list is unmodifiable.    *    *<p>This is useful when a varargs method needs to use a signature such as    * {@code (Foo firstFoo, Foo... moreFoos)}, in order to avoid overload    * ambiguity or to enforce a minimum argument count.    *    *<p>The returned list is serializable and implements {@link RandomAccess}.    *    * @param first the first element    * @param rest an array of additional elements, possibly empty    * @return an unmodifiable list containing the specified elements    */
DECL|method|asList (@ullable E first, E[] rest)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|List
argument_list|<
name|E
argument_list|>
name|asList
parameter_list|(
annotation|@
name|Nullable
name|E
name|first
parameter_list|,
name|E
index|[]
name|rest
parameter_list|)
block|{
return|return
operator|new
name|OnePlusArrayList
argument_list|<
name|E
argument_list|>
argument_list|(
name|first
argument_list|,
name|rest
argument_list|)
return|;
block|}
comment|/** @see Lists#asList(Object, Object[]) */
DECL|class|OnePlusArrayList
specifier|private
specifier|static
class|class
name|OnePlusArrayList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractList
argument_list|<
name|E
argument_list|>
implements|implements
name|Serializable
implements|,
name|RandomAccess
block|{
DECL|field|first
specifier|final
name|E
name|first
decl_stmt|;
DECL|field|rest
specifier|final
name|E
index|[]
name|rest
decl_stmt|;
DECL|method|OnePlusArrayList (@ullable E first, E[] rest)
name|OnePlusArrayList
parameter_list|(
annotation|@
name|Nullable
name|E
name|first
parameter_list|,
name|E
index|[]
name|rest
parameter_list|)
block|{
name|this
operator|.
name|first
operator|=
name|first
expr_stmt|;
name|this
operator|.
name|rest
operator|=
name|checkNotNull
argument_list|(
name|rest
argument_list|)
expr_stmt|;
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
name|rest
operator|.
name|length
operator|+
literal|1
return|;
block|}
DECL|method|get (int index)
annotation|@
name|Override
specifier|public
name|E
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
comment|// check explicitly so the IOOBE will have the right message
name|checkElementIndex
argument_list|(
name|index
argument_list|,
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|(
name|index
operator|==
literal|0
operator|)
condition|?
name|first
else|:
name|rest
index|[
name|index
operator|-
literal|1
index|]
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
comment|/**    * Returns an unmodifiable list containing the specified first and second    * element, and backed by the specified array of additional elements. Changes    * to the {@code rest} array will be reflected in the returned list. Unlike    * {@link Arrays#asList}, the returned list is unmodifiable.    *    *<p>This is useful when a varargs method needs to use a signature such as    * {@code (Foo firstFoo, Foo secondFoo, Foo... moreFoos)}, in order to avoid    * overload ambiguity or to enforce a minimum argument count.    *    *<p>The returned list is serializable and implements {@link RandomAccess}.    *    * @param first the first element    * @param second the second element    * @param rest an array of additional elements, possibly empty    * @return an unmodifiable list containing the specified elements    */
DECL|method|asList ( @ullable E first, @Nullable E second, E[] rest)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|List
argument_list|<
name|E
argument_list|>
name|asList
parameter_list|(
annotation|@
name|Nullable
name|E
name|first
parameter_list|,
annotation|@
name|Nullable
name|E
name|second
parameter_list|,
name|E
index|[]
name|rest
parameter_list|)
block|{
return|return
operator|new
name|TwoPlusArrayList
argument_list|<
name|E
argument_list|>
argument_list|(
name|first
argument_list|,
name|second
argument_list|,
name|rest
argument_list|)
return|;
block|}
comment|/** @see Lists#asList(Object, Object, Object[]) */
DECL|class|TwoPlusArrayList
specifier|private
specifier|static
class|class
name|TwoPlusArrayList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractList
argument_list|<
name|E
argument_list|>
implements|implements
name|Serializable
implements|,
name|RandomAccess
block|{
DECL|field|first
specifier|final
name|E
name|first
decl_stmt|;
DECL|field|second
specifier|final
name|E
name|second
decl_stmt|;
DECL|field|rest
specifier|final
name|E
index|[]
name|rest
decl_stmt|;
DECL|method|TwoPlusArrayList (@ullable E first, @Nullable E second, E[] rest)
name|TwoPlusArrayList
parameter_list|(
annotation|@
name|Nullable
name|E
name|first
parameter_list|,
annotation|@
name|Nullable
name|E
name|second
parameter_list|,
name|E
index|[]
name|rest
parameter_list|)
block|{
name|this
operator|.
name|first
operator|=
name|first
expr_stmt|;
name|this
operator|.
name|second
operator|=
name|second
expr_stmt|;
name|this
operator|.
name|rest
operator|=
name|checkNotNull
argument_list|(
name|rest
argument_list|)
expr_stmt|;
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
name|rest
operator|.
name|length
operator|+
literal|2
return|;
block|}
DECL|method|get (int index)
annotation|@
name|Override
specifier|public
name|E
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
switch|switch
condition|(
name|index
condition|)
block|{
case|case
literal|0
case|:
return|return
name|first
return|;
case|case
literal|1
case|:
return|return
name|second
return|;
default|default:
comment|// check explicitly so the IOOBE will have the right message
name|checkElementIndex
argument_list|(
name|index
argument_list|,
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|rest
index|[
name|index
operator|-
literal|2
index|]
return|;
block|}
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
comment|/**    * Returns a list that applies {@code function} to each element of {@code    * fromList}. The returned list is a transformed view of {@code fromList};    * changes to {@code fromList} will be reflected in the returned list and vice    * versa.    *    *<p>Since functions are not reversible, the transform is one-way and new    * items cannot be stored in the returned list. The {@code add},    * {@code addAll} and {@code set} methods are unsupported in the returned    * list.    *    *<p>The function is applied lazily, invoked when needed. This is necessary    * for the returned list to be a view, but it means that the function will be    * applied many times for bulk operations like {@link List#contains} and    * {@link List#hashCode}. For this to perform well, {@code function} should be    * fast. To avoid lazy evaluation when the returned list doesn't need to be a    * view, copy the returned list into a new list of your choosing.    *    *<p>If {@code fromList} implements {@link RandomAccess}, so will the    * returned list. The returned list always implements {@link Serializable},    * but serialization will succeed only when {@code fromList} and    * {@code function} are serializable. The returned list is threadsafe if the    * supplied list and function are.    */
DECL|method|transform ( List<F> fromList, Function<? super F, ? extends T> function)
specifier|public
specifier|static
parameter_list|<
name|F
parameter_list|,
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|transform
parameter_list|(
name|List
argument_list|<
name|F
argument_list|>
name|fromList
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|F
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|function
parameter_list|)
block|{
return|return
operator|(
name|fromList
operator|instanceof
name|RandomAccess
operator|)
condition|?
operator|new
name|TransformingRandomAccessList
argument_list|<
name|F
argument_list|,
name|T
argument_list|>
argument_list|(
name|fromList
argument_list|,
name|function
argument_list|)
else|:
operator|new
name|TransformingSequentialList
argument_list|<
name|F
argument_list|,
name|T
argument_list|>
argument_list|(
name|fromList
argument_list|,
name|function
argument_list|)
return|;
block|}
comment|/**    * Implementation of a sequential transforming list.    *    * @see Lists#transform    */
DECL|class|TransformingSequentialList
specifier|private
specifier|static
class|class
name|TransformingSequentialList
parameter_list|<
name|F
parameter_list|,
name|T
parameter_list|>
extends|extends
name|AbstractSequentialList
argument_list|<
name|T
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|fromList
specifier|final
name|List
argument_list|<
name|F
argument_list|>
name|fromList
decl_stmt|;
DECL|field|function
specifier|final
name|Function
argument_list|<
name|?
super|super
name|F
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|function
decl_stmt|;
DECL|method|TransformingSequentialList ( List<F> fromList, Function<? super F, ? extends T> function)
name|TransformingSequentialList
parameter_list|(
name|List
argument_list|<
name|F
argument_list|>
name|fromList
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|F
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|function
parameter_list|)
block|{
name|this
operator|.
name|fromList
operator|=
name|checkNotNull
argument_list|(
name|fromList
argument_list|)
expr_stmt|;
name|this
operator|.
name|function
operator|=
name|checkNotNull
argument_list|(
name|function
argument_list|)
expr_stmt|;
block|}
comment|/**      * The default implementation inherited is based on iteration and removal of      * each element which can be overkill. That's why we forward this call      * directly to the backing list.      */
DECL|method|clear ()
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|fromList
operator|.
name|clear
argument_list|()
expr_stmt|;
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
name|fromList
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|listIterator (final int index)
annotation|@
name|Override
specifier|public
name|ListIterator
argument_list|<
name|T
argument_list|>
name|listIterator
parameter_list|(
specifier|final
name|int
name|index
parameter_list|)
block|{
specifier|final
name|ListIterator
argument_list|<
name|F
argument_list|>
name|delegate
init|=
name|fromList
operator|.
name|listIterator
argument_list|(
name|index
argument_list|)
decl_stmt|;
return|return
operator|new
name|ListIterator
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|add
parameter_list|(
name|T
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|hasNext
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|hasPrevious
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|hasPrevious
argument_list|()
return|;
block|}
specifier|public
name|T
name|next
parameter_list|()
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|delegate
operator|.
name|next
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|int
name|nextIndex
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|nextIndex
argument_list|()
return|;
block|}
specifier|public
name|T
name|previous
parameter_list|()
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|delegate
operator|.
name|previous
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|int
name|previousIndex
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|previousIndex
argument_list|()
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|delegate
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|set
parameter_list|(
name|T
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not supported"
argument_list|)
throw|;
block|}
block|}
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
comment|/**    * Implementation of a transforming random access list. We try to make as many    * of these methods pass-through to the source list as possible so that the    * performance characteristics of the source list and transformed list are    * similar.    *    * @see Lists#transform    */
DECL|class|TransformingRandomAccessList
specifier|private
specifier|static
class|class
name|TransformingRandomAccessList
parameter_list|<
name|F
parameter_list|,
name|T
parameter_list|>
extends|extends
name|AbstractList
argument_list|<
name|T
argument_list|>
implements|implements
name|RandomAccess
implements|,
name|Serializable
block|{
DECL|field|fromList
specifier|final
name|List
argument_list|<
name|F
argument_list|>
name|fromList
decl_stmt|;
DECL|field|function
specifier|final
name|Function
argument_list|<
name|?
super|super
name|F
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|function
decl_stmt|;
DECL|method|TransformingRandomAccessList ( List<F> fromList, Function<? super F, ? extends T> function)
name|TransformingRandomAccessList
parameter_list|(
name|List
argument_list|<
name|F
argument_list|>
name|fromList
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|F
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|function
parameter_list|)
block|{
name|this
operator|.
name|fromList
operator|=
name|checkNotNull
argument_list|(
name|fromList
argument_list|)
expr_stmt|;
name|this
operator|.
name|function
operator|=
name|checkNotNull
argument_list|(
name|function
argument_list|)
expr_stmt|;
block|}
DECL|method|clear ()
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|fromList
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|get (int index)
annotation|@
name|Override
specifier|public
name|T
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|fromList
operator|.
name|get
argument_list|(
name|index
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
name|fromList
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|remove (int index)
annotation|@
name|Override
specifier|public
name|T
name|remove
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|fromList
operator|.
name|remove
argument_list|(
name|index
argument_list|)
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
name|fromList
operator|.
name|size
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
comment|/**    * Returns consecutive {@linkplain List#subList(int, int) sublists} of a list,    * each of the same size (the final list may be smaller). For example,    * partitioning a list containing {@code [a, b, c, d, e]} with a partition    * size of 3 yields {@code [[a, b, c], [d, e]]} -- an outer list containing    * two inner lists of three and two elements, all in the original order.    *    *<p>The outer list is unmodifiable, but reflects the latest state of the    * source list. The inner lists are sublist views of the original list,    * produced on demand using {@link List#subList(int, int)}, and are subject    * to all the usual caveats about modification as explained in that API.    *    * @param list the list to return consecutive sublists of    * @param size the desired size of each sublist (the last may be    *     smaller)    * @return a list of consecutive sublists    * @throws IllegalArgumentException if {@code partitionSize} is nonpositive    */
DECL|method|partition (List<T> list, int size)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|List
argument_list|<
name|T
argument_list|>
argument_list|>
name|partition
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|list
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|size
operator|>
literal|0
argument_list|)
expr_stmt|;
return|return
operator|(
name|list
operator|instanceof
name|RandomAccess
operator|)
condition|?
operator|new
name|RandomAccessPartition
argument_list|<
name|T
argument_list|>
argument_list|(
name|list
argument_list|,
name|size
argument_list|)
else|:
operator|new
name|Partition
argument_list|<
name|T
argument_list|>
argument_list|(
name|list
argument_list|,
name|size
argument_list|)
return|;
block|}
DECL|class|Partition
specifier|private
specifier|static
class|class
name|Partition
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AbstractList
argument_list|<
name|List
argument_list|<
name|T
argument_list|>
argument_list|>
block|{
DECL|field|list
specifier|final
name|List
argument_list|<
name|T
argument_list|>
name|list
decl_stmt|;
DECL|field|size
specifier|final
name|int
name|size
decl_stmt|;
DECL|method|Partition (List<T> list, int size)
name|Partition
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|list
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|this
operator|.
name|list
operator|=
name|list
expr_stmt|;
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
block|}
DECL|method|get (int index)
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|T
argument_list|>
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|int
name|listSize
init|=
name|size
argument_list|()
decl_stmt|;
name|checkElementIndex
argument_list|(
name|index
argument_list|,
name|listSize
argument_list|)
expr_stmt|;
name|int
name|start
init|=
name|index
operator|*
name|size
decl_stmt|;
name|int
name|end
init|=
name|Math
operator|.
name|min
argument_list|(
name|start
operator|+
name|size
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|list
operator|.
name|subList
argument_list|(
name|start
argument_list|,
name|end
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
operator|(
name|list
operator|.
name|size
argument_list|()
operator|+
name|size
operator|-
literal|1
operator|)
operator|/
name|size
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
name|list
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
DECL|class|RandomAccessPartition
specifier|private
specifier|static
class|class
name|RandomAccessPartition
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Partition
argument_list|<
name|T
argument_list|>
implements|implements
name|RandomAccess
block|{
DECL|method|RandomAccessPartition (List<T> list, int size)
name|RandomAccessPartition
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|list
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|super
argument_list|(
name|list
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

