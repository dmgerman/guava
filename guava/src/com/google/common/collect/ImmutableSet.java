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
name|collect
operator|.
name|ObjectArrays
operator|.
name|checkElementNotNull
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
name|primitives
operator|.
name|Ints
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
name|EnumSet
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
comment|/**  * A high-performance, immutable {@code Set} with reliable, user-specified  * iteration order. Does not permit null elements.  *  *<p>Unlike {@link Collections#unmodifiableSet}, which is a<i>view</i> of a  * separate collection that can still change, an instance of this class contains  * its own private data and will<i>never</i> change. This class is convenient  * for {@code public static final} sets ("constant sets") and also lets you  * easily make a "defensive copy" of a set provided to your class by a caller.  *  *<p><b>Warning:</b> Like most sets, an {@code ImmutableSet} will not function  * correctly if an element is modified after being placed in the set. For this  * reason, and to avoid general confusion, it is strongly recommended to place  * only immutable objects into this collection.  *  *<p>This class has been observed to perform significantly better than {@link  * HashSet} for objects with very fast {@link Object#hashCode} implementations  * (as a well-behaved immutable object should). While this class's factory  * methods create hash-based instances, the {@link ImmutableSortedSet} subclass  * performs binary searches instead.  *  *<p><b>Note:</b> Although this class is not final, it cannot be subclassed  * outside its package as it has no public or protected constructors. Thus,  * instances of this type are guaranteed to be immutable.  *  *<p>See the Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/ImmutableCollectionsExplained">  * immutable collections</a>.  *  * @see ImmutableList  * @see ImmutableMap  * @author Kevin Bourrillion  * @author Nick Kralevich  * @since 2.0 (imported from Google Collections Library)  */
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
name|construct
argument_list|(
literal|2
argument_list|,
name|e1
argument_list|,
name|e2
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable set containing the given elements, in order. Repeated    * occurrences of an element (according to {@link Object#equals}) after the    * first are ignored.    *    * @throws NullPointerException if any element is null    */
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
name|construct
argument_list|(
literal|3
argument_list|,
name|e1
argument_list|,
name|e2
argument_list|,
name|e3
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable set containing the given elements, in order. Repeated    * occurrences of an element (according to {@link Object#equals}) after the    * first are ignored.    *    * @throws NullPointerException if any element is null    */
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
name|construct
argument_list|(
literal|4
argument_list|,
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
name|construct
argument_list|(
literal|5
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
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable set containing the given elements, in order. Repeated    * occurrences of an element (according to {@link Object#equals}) after the    * first are ignored.    *    * @throws NullPointerException if any element is null    * @since 3.0 (source-compatible since 2.0)    */
DECL|method|of (E e1, E e2, E e3, E e4, E e5, E e6, E... others)
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
parameter_list|,
name|E
name|e6
parameter_list|,
name|E
modifier|...
name|others
parameter_list|)
block|{
specifier|final
name|int
name|paramCount
init|=
literal|6
decl_stmt|;
name|Object
index|[]
name|elements
init|=
operator|new
name|Object
index|[
name|paramCount
operator|+
name|others
operator|.
name|length
index|]
decl_stmt|;
name|elements
index|[
literal|0
index|]
operator|=
name|e1
expr_stmt|;
name|elements
index|[
literal|1
index|]
operator|=
name|e2
expr_stmt|;
name|elements
index|[
literal|2
index|]
operator|=
name|e3
expr_stmt|;
name|elements
index|[
literal|3
index|]
operator|=
name|e4
expr_stmt|;
name|elements
index|[
literal|4
index|]
operator|=
name|e5
expr_stmt|;
name|elements
index|[
literal|5
index|]
operator|=
name|e6
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|others
argument_list|,
literal|0
argument_list|,
name|elements
argument_list|,
name|paramCount
argument_list|,
name|others
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|construct
argument_list|(
name|elements
operator|.
name|length
argument_list|,
name|elements
argument_list|)
return|;
block|}
comment|/**    * Constructs an {@code ImmutableSet} from the first {@code n} elements of the specified array.    * If {@code k} is the size of the returned {@code ImmutableSet}, then the unique elements of    * {@code elements} will be in the first {@code k} positions, and {@code elements[i] == null} for    * {@code k<= i< n}.    *    *<p>This may modify {@code elements}.  Additionally, if {@code n == elements.length} and    * {@code elements} contains no duplicates, {@code elements} may be used without copying in the    * returned {@code ImmutableSet}, in which case it may no longer be modified.    *    *<p>{@code elements} may contain only values of type {@code E}.    *    * @throws NullPointerException if any of the first {@code n} elements of {@code elements} is    *          null    */
DECL|method|construct (int n, Object... elements)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSet
argument_list|<
name|E
argument_list|>
name|construct
parameter_list|(
name|int
name|n
parameter_list|,
name|Object
modifier|...
name|elements
parameter_list|)
block|{
switch|switch
condition|(
name|n
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// safe; elements contains only E's
name|E
name|elem
init|=
operator|(
name|E
operator|)
name|elements
index|[
literal|0
index|]
decl_stmt|;
return|return
name|of
argument_list|(
name|elem
argument_list|)
return|;
default|default:
comment|// continue below to handle the general case
block|}
name|int
name|tableSize
init|=
name|chooseTableSize
argument_list|(
name|n
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
name|int
name|hashCode
init|=
literal|0
decl_stmt|;
name|int
name|uniques
init|=
literal|0
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
name|n
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|element
init|=
name|checkElementNotNull
argument_list|(
name|elements
index|[
name|i
index|]
argument_list|,
name|i
argument_list|)
decl_stmt|;
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
name|j
init|=
name|Hashing
operator|.
name|smear
argument_list|(
name|hash
argument_list|)
init|;
condition|;
name|j
operator|++
control|)
block|{
name|int
name|index
init|=
name|j
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
comment|// Came to an empty slot. Put the element here.
name|elements
index|[
name|uniques
operator|++
index|]
operator|=
name|element
expr_stmt|;
name|table
index|[
name|index
index|]
operator|=
name|element
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
block|}
block|}
block|}
name|Arrays
operator|.
name|fill
argument_list|(
name|elements
argument_list|,
name|uniques
argument_list|,
name|n
argument_list|,
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|uniques
operator|==
literal|1
condition|)
block|{
comment|// There is only one element or elements are all duplicates
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// we are careful to only pass in E
name|E
name|element
init|=
operator|(
name|E
operator|)
name|elements
index|[
literal|0
index|]
decl_stmt|;
return|return
operator|new
name|SingletonImmutableSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|element
argument_list|,
name|hashCode
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|tableSize
operator|!=
name|chooseTableSize
argument_list|(
name|uniques
argument_list|)
condition|)
block|{
comment|// Resize the table when the array includes too many duplicates.
comment|// when this happens, we have already made a copy
return|return
name|construct
argument_list|(
name|uniques
argument_list|,
name|elements
argument_list|)
return|;
block|}
else|else
block|{
name|Object
index|[]
name|uniqueElements
init|=
operator|(
name|uniques
operator|<
name|elements
operator|.
name|length
operator|)
condition|?
name|ObjectArrays
operator|.
name|arraysCopyOf
argument_list|(
name|elements
argument_list|,
name|uniques
argument_list|)
else|:
name|elements
decl_stmt|;
return|return
operator|new
name|RegularImmutableSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|uniqueElements
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
comment|// We use power-of-2 tables, and this is the highest int that's a power of 2
DECL|field|MAX_TABLE_SIZE
specifier|static
specifier|final
name|int
name|MAX_TABLE_SIZE
init|=
name|Ints
operator|.
name|MAX_POWER_OF_TWO
decl_stmt|;
comment|// Represents how tightly we can pack things, as a maximum.
DECL|field|DESIRED_LOAD_FACTOR
specifier|private
specifier|static
specifier|final
name|double
name|DESIRED_LOAD_FACTOR
init|=
literal|0.7
decl_stmt|;
comment|// If the set has this many elements, it will "max out" the table size
DECL|field|CUTOFF
specifier|private
specifier|static
specifier|final
name|int
name|CUTOFF
init|=
call|(
name|int
call|)
argument_list|(
name|MAX_TABLE_SIZE
operator|*
name|DESIRED_LOAD_FACTOR
argument_list|)
decl_stmt|;
comment|/**    * Returns an array size suitable for the backing array of a hash table that    * uses open addressing with linear probing in its implementation.  The    * returned size is the smallest power of two that can hold setSize elements    * with the desired load factor.    *    *<p>Do not call this method with setSize< 2.    */
DECL|method|chooseTableSize (int setSize)
annotation|@
name|VisibleForTesting
specifier|static
name|int
name|chooseTableSize
parameter_list|(
name|int
name|setSize
parameter_list|)
block|{
comment|// Correct the size for open addressing to match desired load factor.
if|if
condition|(
name|setSize
operator|<
name|CUTOFF
condition|)
block|{
comment|// Round up to the next highest power of 2.
name|int
name|tableSize
init|=
name|Integer
operator|.
name|highestOneBit
argument_list|(
name|setSize
operator|-
literal|1
argument_list|)
operator|<<
literal|1
decl_stmt|;
while|while
condition|(
name|tableSize
operator|*
name|DESIRED_LOAD_FACTOR
operator|<
name|setSize
condition|)
block|{
name|tableSize
operator|<<=
literal|1
expr_stmt|;
block|}
return|return
name|tableSize
return|;
block|}
comment|// The table can't be completely full or we'll get infinite reprobes
name|checkArgument
argument_list|(
name|setSize
operator|<
name|MAX_TABLE_SIZE
argument_list|,
literal|"collection too large"
argument_list|)
expr_stmt|;
return|return
name|MAX_TABLE_SIZE
return|;
block|}
comment|/**    * Returns an immutable set containing the given elements, in order. Repeated    * occurrences of an element (according to {@link Object#equals}) after the    * first are ignored.    *    * @throws NullPointerException if any of {@code elements} is null    * @since 3.0    */
DECL|method|copyOf (E[] elements)
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
name|E
index|[]
name|elements
parameter_list|)
block|{
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
name|construct
argument_list|(
name|elements
operator|.
name|length
argument_list|,
name|elements
operator|.
name|clone
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**    * Returns an immutable set containing the given elements, in order. Repeated    * occurrences of an element (according to {@link Object#equals}) after the    * first are ignored. This method iterates over {@code elements} at most once.    *    *<p>Note that if {@code s} is a {@code Set<String>}, then {@code    * ImmutableSet.copyOf(s)} returns an {@code ImmutableSet<String>} containing    * each of the strings in {@code s}, while {@code ImmutableSet.of(s)} returns    * a {@code ImmutableSet<Set<String>>} containing one element (the given set    * itself).    *    *<p>Despite the method name, this method attempts to avoid actually copying    * the data when it is safe to do so. The exact circumstances under which a    * copy will or will not be performed are undocumented and subject to change.    *    * @throws NullPointerException if any of {@code elements} is null    */
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
return|return
operator|(
name|elements
operator|instanceof
name|Collection
operator|)
condition|?
name|copyOf
argument_list|(
name|Collections2
operator|.
name|cast
argument_list|(
name|elements
argument_list|)
argument_list|)
else|:
name|copyOf
argument_list|(
name|elements
operator|.
name|iterator
argument_list|()
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
comment|// We special-case for 0 or 1 elements, but anything further is madness.
if|if
condition|(
operator|!
name|elements
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
name|of
argument_list|()
return|;
block|}
name|E
name|first
init|=
name|elements
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|elements
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
name|of
argument_list|(
name|first
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|E
argument_list|>
argument_list|()
operator|.
name|add
argument_list|(
name|first
argument_list|)
operator|.
name|addAll
argument_list|(
name|elements
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
comment|/**    * Returns an immutable set containing the given elements, in order. Repeated    * occurrences of an element (according to {@link Object#equals}) after the    * first are ignored. This method iterates over {@code elements} at most    * once.    *    *<p>Note that if {@code s} is a {@code Set<String>}, then {@code    * ImmutableSet.copyOf(s)} returns an {@code ImmutableSet<String>} containing    * each of the strings in {@code s}, while {@code ImmutableSet.of(s)} returns    * a {@code ImmutableSet<Set<String>>} containing one element (the given set    * itself).    *    *<p><b>Note:</b> Despite what the method name suggests, {@code copyOf} will    * return constant-space views, rather than linear-space copies, of some    * inputs known to be immutable. For some other immutable inputs, such as key    * sets of an {@code ImmutableMap}, it still performs a copy in order to avoid    * holding references to the values of the map. The heuristics used in this    * decision are undocumented and subject to change except that:    *<ul>    *<li>A full copy will be done of any {@code ImmutableSortedSet}.</li>    *<li>{@code ImmutableSet.copyOf()} is idempotent with respect to pointer    * equality.</li>    *</ul>    *    *<p>This method is safe to use even when {@code elements} is a synchronized    * or concurrent collection that is currently being modified by another    * thread.    *    * @throws NullPointerException if any of {@code elements} is null    * @since 7.0 (source-compatible since 2.0)    */
DECL|method|copyOf (Collection<? extends E> elements)
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
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
parameter_list|)
block|{
comment|/*      * TODO(user): consider checking for ImmutableAsList here      * TODO(user): consider checking for Multiset here      */
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
if|if
condition|(
operator|!
name|set
operator|.
name|isPartialView
argument_list|()
condition|)
block|{
return|return
name|set
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|elements
operator|instanceof
name|EnumSet
condition|)
block|{
return|return
name|copyOfEnumSet
argument_list|(
operator|(
name|EnumSet
operator|)
name|elements
argument_list|)
return|;
block|}
name|Object
index|[]
name|array
init|=
name|elements
operator|.
name|toArray
argument_list|()
decl_stmt|;
return|return
name|construct
argument_list|(
name|array
operator|.
name|length
argument_list|,
name|array
argument_list|)
return|;
block|}
DECL|method|copyOfEnumSet ( EnumSet<E> enumSet)
specifier|private
specifier|static
parameter_list|<
name|E
extends|extends
name|Enum
argument_list|<
name|E
argument_list|>
parameter_list|>
name|ImmutableSet
argument_list|<
name|E
argument_list|>
name|copyOfEnumSet
parameter_list|(
name|EnumSet
argument_list|<
name|E
argument_list|>
name|enumSet
parameter_list|)
block|{
return|return
name|ImmutableEnumSet
operator|.
name|asImmutable
argument_list|(
name|EnumSet
operator|.
name|copyOf
argument_list|(
name|enumSet
argument_list|)
argument_list|)
return|;
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
elseif|else
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
name|Sets
operator|.
name|equalsImpl
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
return|return
name|Sets
operator|.
name|hashCodeImpl
argument_list|(
name|this
argument_list|)
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
name|copyOf
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
comment|/**    * A builder for creating immutable set instances, especially {@code public    * static final} sets ("constant sets"). Example:<pre>   {@code    *    *   public static final ImmutableSet<Color> GOOGLE_COLORS =    *       new ImmutableSet.Builder<Color>()    *           .addAll(WEBSAFE_COLORS)    *           .add(new Color(0, 191, 255))    *           .build();}</pre>    *    *<p>Builder instances can be reused; it is safe to call {@link #build} multiple    * times to build multiple sets in series. Each set is a superset of the set    * created before it.    *    * @since 2.0 (imported from Google Collections Library)    */
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
name|ArrayBasedBuilder
argument_list|<
name|E
argument_list|>
block|{
comment|/**      * Creates a new builder. The returned builder is equivalent to the builder      * generated by {@link ImmutableSet#builder}.      */
DECL|method|Builder ()
specifier|public
name|Builder
parameter_list|()
block|{
name|this
argument_list|(
name|DEFAULT_INITIAL_CAPACITY
argument_list|)
expr_stmt|;
block|}
DECL|method|Builder (int capacity)
name|Builder
parameter_list|(
name|int
name|capacity
parameter_list|)
block|{
name|super
argument_list|(
name|capacity
argument_list|)
expr_stmt|;
block|}
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
name|super
operator|.
name|add
argument_list|(
name|element
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
name|ImmutableSet
argument_list|<
name|E
argument_list|>
name|result
init|=
name|construct
argument_list|(
name|size
argument_list|,
name|contents
argument_list|)
decl_stmt|;
comment|// construct has the side effect of deduping contents, so we update size
comment|// accordingly.
name|size
operator|=
name|result
operator|.
name|size
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
block|}
end_class

end_unit

