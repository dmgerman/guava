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
name|HashSet
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
comment|/**  * A high-performance, immutable {@code Set} with reliable, user-specified  * iteration order. Does not permit null elements.  *  *<p>Unlike {@link Collections#unmodifiableSet}, which is a<i>view</i> of a  * separate collection that can still change, an instance of this class contains  * its own private data and will<i>never</i> change. This class is convenient  * for {@code public static final} sets ("constant sets") and also lets you  * easily make a "defensive copy" of a set provided to your class by a caller.  *  *<p><b>Warning:</b> Like most sets, an {@code ImmutableSet} will not function  * correctly if an element is modified after being placed in the set. For this  * reason, and to avoid general confusion, it is strongly recommended to place  * only immutable objects into this collection.  *  *<p>This class has been observed to perform significantly better than {@link  * HashSet} for objects with very fast {@link Object#hashCode} implementations  * (as a well-behaved immutable object should). While this class's factory  * methods create hash-based instances, the {@link ImmutableSortedSet} subclass  * performs binary searches instead.  *  *<p><b>Note</b>: Although this class is not final, it cannot be subclassed  * outside its package as it has no public or protected constructors. Thus,  * instances of this type are guaranteed to be immutable.  *  * @see ImmutableList  * @see ImmutableMap  * @author Kevin Bourrillion  * @author Nick Kralevich  * @since 2010.01.04<b>stable</b> (imported from Google Collections Library)  */
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
comment|// we're overriding default serialization
DECL|class|ImmutableSet
specifier|public
specifier|abstract
class|class
name|ImmutableSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableCollection
argument_list|<
name|E
argument_list|>
implements|implements
name|Set
argument_list|<
name|E
argument_list|>
block|{
comment|/**    * Returns the empty immutable set. This set behaves and performs comparably    * to {@link Collections#emptySet}, and is preferable mainly for consistency    * and maintainability of your code.    */
comment|// Casting to any type is safe because the set will never hold any elements.
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
DECL|method|of ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSet
argument_list|<
name|E
argument_list|>
name|of
parameter_list|()
block|{
return|return
operator|(
name|ImmutableSet
argument_list|<
name|E
argument_list|>
operator|)
name|EmptyImmutableSet
operator|.
name|INSTANCE
return|;
block|}
comment|/**    * Returns an immutable set containing a single element. This set behaves and    * performs comparably to {@link Collections#singleton}, but will not accept    * a null element. It is preferable mainly for consistency and    * maintainability of your code.    */
DECL|method|of (E element)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSet
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
operator|new
name|SingletonImmutableSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|element
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable set containing the given elements, in order. Repeated    * occurrences of an element (according to {@link Object#equals}) after the    * first are ignored.    *    * @throws NullPointerException if any element is null    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|of (E e1, E e2)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSet
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
name|create
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable set containing the given elements, in order. Repeated    * occurrences of an element (according to {@link Object#equals}) after the    * first are ignored.    *    * @throws NullPointerException if any element is null    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|of (E e1, E e2, E e3)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSet
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
name|create
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|,
name|e3
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable set containing the given elements, in order. Repeated    * occurrences of an element (according to {@link Object#equals}) after the    * first are ignored.    *    * @throws NullPointerException if any element is null    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|of (E e1, E e2, E e3, E e4)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSet
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
name|create
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
comment|/**    * Returns an immutable set containing the given elements, in order. Repeated    * occurrences of an element (according to {@link Object#equals}) after the    * first are ignored.    *    * @throws NullPointerException if any element is null    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|of (E e1, E e2, E e3, E e4, E e5)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSet
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
name|create
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
comment|/**    * Returns an immutable set containing the given elements, in order. Repeated    * occurrences of an element (according to {@link Object#equals}) after the    * first are ignored (but too many of these may result in the set being    * sized inappropriately).    *    * @throws NullPointerException if any of {@code elements} is null    */
DECL|method|of (E... elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSet
argument_list|<
name|E
argument_list|>
name|of
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
switch|switch
condition|(
name|elements
operator|.
name|length
condition|)
block|{
case|case
literal|0
case|:
return|return
name|of
argument_list|()
return|;
case|case
literal|1
case|:
return|return
name|of
argument_list|(
name|elements
index|[
literal|0
index|]
argument_list|)
return|;
default|default:
return|return
name|create
argument_list|(
name|elements
argument_list|)
return|;
block|}
block|}
comment|/**    * Returns an immutable set containing the given elements, in order. Repeated    * occurrences of an element (according to {@link Object#equals}) after the    * first are ignored (but too many of these may result in the set being    * sized inappropriately). This method iterates over {@code elements} at most    * once.    *    *<p>Note that if {@code s} is a {@code Set<String>}, then {@code    * ImmutableSet.copyOf(s)} returns an {@code ImmutableSet<String>} containing    * each of the strings in {@code s}, while {@code ImmutableSet.of(s)} returns    * a {@code ImmutableSet<Set<String>>} containing one element (the given set    * itself).    *    *<p><b>Note:</b> Despite what the method name suggests, if {@code elements}    * is an {@code ImmutableSet} (but not an {@code ImmutableSortedSet}), no copy    * will actually be performed, and the given set itself will be returned.    *    * @throws NullPointerException if any of {@code elements} is null    */
DECL|method|copyOf (Iterable<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSet
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
name|ImmutableSet
operator|&&
operator|!
operator|(
name|elements
operator|instanceof
name|ImmutableSortedSet
operator|)
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// all supported methods are covariant
name|ImmutableSet
argument_list|<
name|E
argument_list|>
name|set
init|=
operator|(
name|ImmutableSet
argument_list|<
name|E
argument_list|>
operator|)
name|elements
decl_stmt|;
return|return
name|set
return|;
block|}
return|return
name|copyOfInternal
argument_list|(
name|Collections2
operator|.
name|toCollection
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable set containing the given elements, in order. Repeated    * occurrences of an element (according to {@link Object#equals}) after the    * first are ignored.    *    * @throws NullPointerException if any of {@code elements} is null    */
DECL|method|copyOf (Iterator<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSet
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
name|Collection
argument_list|<
name|E
argument_list|>
name|list
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|elements
argument_list|)
decl_stmt|;
return|return
name|copyOfInternal
argument_list|(
name|list
argument_list|)
return|;
block|}
DECL|method|copyOfInternal ( Collection<? extends E> collection)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSet
argument_list|<
name|E
argument_list|>
name|copyOfInternal
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
comment|// TODO: Support concurrent collections that change while this method is
comment|// running.
switch|switch
condition|(
name|collection
operator|.
name|size
argument_list|()
condition|)
block|{
case|case
literal|0
case|:
return|return
name|of
argument_list|()
return|;
case|case
literal|1
case|:
comment|// TODO: Remove "ImmutableSet.<E>" when eclipse bug is fixed.
return|return
name|ImmutableSet
operator|.
expr|<
name|E
operator|>
name|of
argument_list|(
name|collection
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
return|;
default|default:
return|return
name|create
argument_list|(
name|collection
argument_list|,
name|collection
operator|.
name|size
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|ImmutableSet ()
name|ImmutableSet
parameter_list|()
block|{}
comment|/** Returns {@code true} if the {@code hashCode()} method runs quickly. */
DECL|method|isHashCodeFast ()
name|boolean
name|isHashCodeFast
parameter_list|()
block|{
return|return
literal|false
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
name|object
operator|instanceof
name|ImmutableSet
operator|&&
name|isHashCodeFast
argument_list|()
operator|&&
operator|(
operator|(
name|ImmutableSet
argument_list|<
name|?
argument_list|>
operator|)
name|object
operator|)
operator|.
name|isHashCodeFast
argument_list|()
operator|&&
name|hashCode
argument_list|()
operator|!=
name|object
operator|.
name|hashCode
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|Collections2
operator|.
name|setEquals
argument_list|(
name|this
argument_list|,
name|object
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
name|int
name|hashCode
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|this
control|)
block|{
name|hashCode
operator|+=
name|o
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
name|hashCode
return|;
block|}
comment|// This declaration is needed to make Set.iterator() and
comment|// ImmutableCollection.iterator() consistent.
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
specifier|abstract
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
function_decl|;
DECL|method|create (E... elements)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSet
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|E
modifier|...
name|elements
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|,
name|elements
operator|.
name|length
argument_list|)
return|;
block|}
DECL|method|create ( Iterable<? extends E> iterable, int count)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSet
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|iterable
parameter_list|,
name|int
name|count
parameter_list|)
block|{
comment|// count is always the (nonzero) number of elements in the iterable
name|int
name|tableSize
init|=
name|Hashing
operator|.
name|chooseTableSize
argument_list|(
name|count
argument_list|)
decl_stmt|;
name|Object
index|[]
name|table
init|=
operator|new
name|Object
index|[
name|tableSize
index|]
decl_stmt|;
name|int
name|mask
init|=
name|tableSize
operator|-
literal|1
decl_stmt|;
name|List
argument_list|<
name|E
argument_list|>
name|elements
init|=
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|(
name|count
argument_list|)
decl_stmt|;
name|int
name|hashCode
init|=
literal|0
decl_stmt|;
for|for
control|(
name|E
name|element
range|:
name|iterable
control|)
block|{
name|checkNotNull
argument_list|(
name|element
argument_list|)
expr_stmt|;
comment|// for GWT
name|int
name|hash
init|=
name|element
operator|.
name|hashCode
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|Hashing
operator|.
name|smear
argument_list|(
name|hash
argument_list|)
init|;
literal|true
condition|;
name|i
operator|++
control|)
block|{
name|int
name|index
init|=
name|i
operator|&
name|mask
decl_stmt|;
name|Object
name|value
init|=
name|table
index|[
name|index
index|]
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
comment|// Came to an empty bucket. Put the element here.
name|table
index|[
name|index
index|]
operator|=
name|element
expr_stmt|;
name|elements
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|hashCode
operator|+=
name|hash
expr_stmt|;
break|break;
block|}
elseif|else
if|if
condition|(
name|value
operator|.
name|equals
argument_list|(
name|element
argument_list|)
condition|)
block|{
break|break;
comment|// Found a duplicate. Nothing to do.
block|}
block|}
block|}
if|if
condition|(
name|elements
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// The iterable contained only duplicates of the same element.
return|return
operator|new
name|SingletonImmutableSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|elements
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|hashCode
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|tableSize
operator|>
name|Hashing
operator|.
name|chooseTableSize
argument_list|(
name|elements
operator|.
name|size
argument_list|()
argument_list|)
condition|)
block|{
comment|// Resize the table when the iterable includes too many duplicates.
return|return
name|create
argument_list|(
name|elements
argument_list|,
name|elements
operator|.
name|size
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|RegularImmutableSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|elements
operator|.
name|toArray
argument_list|()
argument_list|,
name|hashCode
argument_list|,
name|table
argument_list|,
name|mask
argument_list|)
return|;
block|}
block|}
DECL|class|ArrayImmutableSet
specifier|abstract
specifier|static
class|class
name|ArrayImmutableSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableSet
argument_list|<
name|E
argument_list|>
block|{
comment|// the elements (two or more) in the desired order.
DECL|field|elements
specifier|final
specifier|transient
name|Object
index|[]
name|elements
decl_stmt|;
DECL|method|ArrayImmutableSet (Object[] elements)
name|ArrayImmutableSet
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
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|elements
operator|.
name|length
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
comment|/*      * The cast is safe because the only way to create an instance is via the      * create() method above, which only permits elements of type E.      */
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
literal|0
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
literal|0
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
if|if
condition|(
name|targets
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
name|targets
operator|instanceof
name|ArrayImmutableSet
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
if|if
condition|(
name|targets
operator|.
name|size
argument_list|()
operator|>
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
name|Object
name|target
range|:
operator|(
operator|(
name|ArrayImmutableSet
argument_list|<
name|?
argument_list|>
operator|)
name|targets
operator|)
operator|.
name|elements
control|)
block|{
if|if
condition|(
operator|!
name|contains
argument_list|(
name|target
argument_list|)
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
name|ImmutableAsList
argument_list|<
name|E
argument_list|>
argument_list|(
name|elements
argument_list|,
name|this
argument_list|)
return|;
block|}
block|}
comment|/** such as ImmutableMap.keySet() */
DECL|class|TransformedImmutableSet
specifier|abstract
specifier|static
class|class
name|TransformedImmutableSet
parameter_list|<
name|D
parameter_list|,
name|E
parameter_list|>
extends|extends
name|ImmutableSet
argument_list|<
name|E
argument_list|>
block|{
DECL|field|source
specifier|final
name|D
index|[]
name|source
decl_stmt|;
DECL|field|hashCode
specifier|final
name|int
name|hashCode
decl_stmt|;
DECL|method|TransformedImmutableSet (D[] source, int hashCode)
name|TransformedImmutableSet
parameter_list|(
name|D
index|[]
name|source
parameter_list|,
name|int
name|hashCode
parameter_list|)
block|{
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
name|this
operator|.
name|hashCode
operator|=
name|hashCode
expr_stmt|;
block|}
DECL|method|transform (D element)
specifier|abstract
name|E
name|transform
parameter_list|(
name|D
name|element
parameter_list|)
function_decl|;
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|source
operator|.
name|length
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
operator|new
name|AbstractIterator
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
name|int
name|index
init|=
literal|0
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|E
name|computeNext
parameter_list|()
block|{
return|return
name|index
operator|<
name|source
operator|.
name|length
condition|?
name|transform
argument_list|(
name|source
index|[
name|index
operator|++
index|]
argument_list|)
else|:
name|endOfData
argument_list|()
return|;
block|}
block|}
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
name|toArray
argument_list|(
operator|new
name|Object
index|[
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
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
comment|// Writes will produce ArrayStoreException when the toArray() doc requires.
name|Object
index|[]
name|objectArray
init|=
name|array
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
name|source
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|objectArray
index|[
name|i
index|]
operator|=
name|transform
argument_list|(
name|source
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|array
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
specifier|final
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|hashCode
return|;
block|}
DECL|method|isHashCodeFast ()
annotation|@
name|Override
name|boolean
name|isHashCodeFast
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
comment|/*    * This class is used to serialize all ImmutableSet instances, except for    * ImmutableEnumSet/ImmutableSortedSet, regardless of implementation type. It    * captures their "logical contents" and they are reconstructed using public    * static factories. This is necessary to ensure that the existence of a    * particular implementation type is an implementation detail.    */
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
DECL|method|SerializedForm (Object[] elements)
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
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|of
argument_list|(
name|elements
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
argument_list|(
name|toArray
argument_list|()
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
comment|/**    * A builder for creating immutable set instances, especially    * {@code public static final} sets ("constant sets").    *    *<p>Example:    *<pre>{@code    *   public static final ImmutableSet<Color> GOOGLE_COLORS    *       = new ImmutableSet.Builder<Color>()    *           .addAll(WEBSAFE_COLORS)    *           .add(new Color(0, 191, 255))    *           .build();}</pre>    *    *<p>Builder instances can be reused - it is safe to call {@link #build}    * multiple times to build multiple sets in series. Each set    * is a superset of the set created before it.    */
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
comment|// accessed directly by ImmutableSortedSet
DECL|field|contents
specifier|final
name|ArrayList
argument_list|<
name|E
argument_list|>
name|contents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
comment|/**      * Creates a new builder. The returned builder is equivalent to the builder      * generated by {@link ImmutableSet#builder}.      */
DECL|method|Builder ()
specifier|public
name|Builder
parameter_list|()
block|{}
comment|/**      * Adds {@code element} to the {@code ImmutableSet}.  If the {@code      * ImmutableSet} already contains {@code element}, then {@code add} has no      * effect (only the previously added element is retained).      *      * @param element the element to add      * @return this {@code Builder} object      * @throws NullPointerException if {@code element} is null      */
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
comment|/**      * Adds each element of {@code elements} to the {@code ImmutableSet},      * ignoring duplicate elements (only the first duplicate element is added).      *      * @param elements the elements to add      * @return this {@code Builder} object      * @throws NullPointerException if {@code elements} is null or contains a      *     null element      */
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
name|checkNotNull
argument_list|(
name|elements
argument_list|)
expr_stmt|;
comment|// for GWT
name|contents
operator|.
name|ensureCapacity
argument_list|(
name|contents
operator|.
name|size
argument_list|()
operator|+
name|elements
operator|.
name|length
argument_list|)
expr_stmt|;
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
comment|/**      * Adds each element of {@code elements} to the {@code ImmutableSet},      * ignoring duplicate elements (only the first duplicate element is added).      *      * @param elements the {@code Iterable} to add to the {@code ImmutableSet}      * @return this {@code Builder} object      * @throws NullPointerException if {@code elements} is null or contains a      *     null element      */
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
name|Collection
condition|)
block|{
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
init|=
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|elements
decl_stmt|;
name|contents
operator|.
name|ensureCapacity
argument_list|(
name|contents
operator|.
name|size
argument_list|()
operator|+
name|collection
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * Adds each element of {@code elements} to the {@code ImmutableSet},      * ignoring duplicate elements (only the first duplicate element is added).      *      * @param elements the elements to add to the {@code ImmutableSet}      * @return this {@code Builder} object      * @throws NullPointerException if {@code elements} is null or contains a      *     null element      */
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
comment|/**      * Returns a newly-created {@code ImmutableSet} based on the contents of      * the {@code Builder}.      */
DECL|method|build ()
annotation|@
name|Override
specifier|public
name|ImmutableSet
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

