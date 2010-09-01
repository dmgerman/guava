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
name|Preconditions
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
name|ArrayList
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
name|List
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
comment|/**  * A high-performance, immutable, random-access {@code List} implementation.  * Does not permit null elements.  *  *<p>Unlike {@link Collections#unmodifiableList}, which is a<i>view</i> of a  * separate collection that can still change, an instance of {@code  * ImmutableList} contains its own private data and will<i>never</i> change.  * {@code ImmutableList} is convenient for {@code public static final} lists  * ("constant lists") and also lets you easily make a "defensive copy" of a list  * provided to your class by a caller.  *  *<p><b>Note</b>: Although this class is not final, it cannot be subclassed as  * it has no public or protected constructors. Thus, instances of this type are  * guaranteed to be immutable.  *  * @see ImmutableMap  * @see ImmutableSet  * @author Kevin Bourrillion  * @since 2 (imported from Google Collections Library)  */
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
DECL|class|ImmutableList
specifier|public
specifier|abstract
class|class
name|ImmutableList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableCollection
argument_list|<
name|E
argument_list|>
implements|implements
name|List
argument_list|<
name|E
argument_list|>
implements|,
name|RandomAccess
block|{
comment|/**    * Returns the empty immutable list. This set behaves and performs comparably    * to {@link Collections#emptyList}, and is preferable mainly for consistency    * and maintainability of your code.    */
comment|// Casting to any type is safe because the list will never hold any elements.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|of ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|of
parameter_list|()
block|{
return|return
operator|(
name|ImmutableList
argument_list|<
name|E
argument_list|>
operator|)
name|EmptyImmutableList
operator|.
name|INSTANCE
return|;
block|}
comment|/**    * Returns an immutable list containing a single element. This list behaves    * and performs comparably to {@link Collections#singleton}, but will not    * accept a null element. It is preferable mainly for consistency and    * maintainability of your code.    *    * @throws NullPointerException if {@code element} is null    */
DECL|method|of (E element)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
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
name|SingletonImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|element
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable list containing the given elements, in order.    *    * @throws NullPointerException if any element is null    */
DECL|method|of (E e1, E e2)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
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
name|e1
argument_list|,
name|e2
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable list containing the given elements, in order.    *    * @throws NullPointerException if any element is null    */
DECL|method|of (E e1, E e2, E e3)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
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
name|e1
argument_list|,
name|e2
argument_list|,
name|e3
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable list containing the given elements, in order.    *    * @throws NullPointerException if any element is null    */
DECL|method|of (E e1, E e2, E e3, E e4)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
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
comment|/**    * Returns an immutable list containing the given elements, in order.    *    * @throws NullPointerException if any element is null    */
DECL|method|of (E e1, E e2, E e3, E e4, E e5)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
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
comment|/**    * Returns an immutable list containing the given elements, in order.    *    * @throws NullPointerException if any element is null    */
DECL|method|of (E e1, E e2, E e3, E e4, E e5, E e6)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
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
parameter_list|)
block|{
return|return
name|construct
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
argument_list|,
name|e6
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable list containing the given elements, in order.    *    * @throws NullPointerException if any element is null    */
DECL|method|of ( E e1, E e2, E e3, E e4, E e5, E e6, E e7)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
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
name|e7
parameter_list|)
block|{
return|return
name|construct
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
argument_list|,
name|e6
argument_list|,
name|e7
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable list containing the given elements, in order.    *    * @throws NullPointerException if any element is null    */
DECL|method|of ( E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
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
name|e7
parameter_list|,
name|E
name|e8
parameter_list|)
block|{
return|return
name|construct
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
argument_list|,
name|e6
argument_list|,
name|e7
argument_list|,
name|e8
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable list containing the given elements, in order.    *    * @throws NullPointerException if any element is null    */
DECL|method|of ( E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
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
name|e7
parameter_list|,
name|E
name|e8
parameter_list|,
name|E
name|e9
parameter_list|)
block|{
return|return
name|construct
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
argument_list|,
name|e6
argument_list|,
name|e7
argument_list|,
name|e8
argument_list|,
name|e9
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable list containing the given elements, in order.    *    * @throws NullPointerException if any element is null    */
DECL|method|of ( E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
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
name|e7
parameter_list|,
name|E
name|e8
parameter_list|,
name|E
name|e9
parameter_list|,
name|E
name|e10
parameter_list|)
block|{
return|return
name|construct
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
argument_list|,
name|e6
argument_list|,
name|e7
argument_list|,
name|e8
argument_list|,
name|e9
argument_list|,
name|e10
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable list containing the given elements, in order.    *    * @throws NullPointerException if any element is null    */
DECL|method|of ( E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
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
name|e7
parameter_list|,
name|E
name|e8
parameter_list|,
name|E
name|e9
parameter_list|,
name|E
name|e10
parameter_list|,
name|E
name|e11
parameter_list|)
block|{
return|return
name|construct
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
argument_list|,
name|e6
argument_list|,
name|e7
argument_list|,
name|e8
argument_list|,
name|e9
argument_list|,
name|e10
argument_list|,
name|e11
argument_list|)
return|;
block|}
comment|// These go up to eleven. After that, you just get the varargs form, and
comment|// whatever warnings might come along with it. :(
comment|/**    * Returns an immutable list containing the given elements, in order.    *    * @throws NullPointerException if any element is null    * @since 3 (source-compatible since release 2)    */
DECL|method|of ( E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11, E e12, E... others)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
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
name|e7
parameter_list|,
name|E
name|e8
parameter_list|,
name|E
name|e9
parameter_list|,
name|E
name|e10
parameter_list|,
name|E
name|e11
parameter_list|,
name|E
name|e12
parameter_list|,
name|E
modifier|...
name|others
parameter_list|)
block|{
name|Object
index|[]
name|array
init|=
operator|new
name|Object
index|[
literal|12
operator|+
name|others
operator|.
name|length
index|]
decl_stmt|;
name|array
index|[
literal|0
index|]
operator|=
name|e1
expr_stmt|;
name|array
index|[
literal|1
index|]
operator|=
name|e2
expr_stmt|;
name|array
index|[
literal|2
index|]
operator|=
name|e3
expr_stmt|;
name|array
index|[
literal|3
index|]
operator|=
name|e4
expr_stmt|;
name|array
index|[
literal|4
index|]
operator|=
name|e5
expr_stmt|;
name|array
index|[
literal|5
index|]
operator|=
name|e6
expr_stmt|;
name|array
index|[
literal|6
index|]
operator|=
name|e7
expr_stmt|;
name|array
index|[
literal|7
index|]
operator|=
name|e8
expr_stmt|;
name|array
index|[
literal|8
index|]
operator|=
name|e9
expr_stmt|;
name|array
index|[
literal|9
index|]
operator|=
name|e10
expr_stmt|;
name|array
index|[
literal|10
index|]
operator|=
name|e11
expr_stmt|;
name|array
index|[
literal|11
index|]
operator|=
name|e12
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|others
argument_list|,
literal|0
argument_list|,
name|array
argument_list|,
literal|12
argument_list|,
name|others
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|construct
argument_list|(
name|array
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable list containing the given elements, in order.    *    * @deprecated use {@link #copyOf(Object[])}    * @throws NullPointerException if any of {@code elements} is null    * @since 2 (changed from varargs in release 3)    */
annotation|@
name|Deprecated
DECL|method|of (E[] elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
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
name|elements
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable list containing the given elements, in order. If    * {@code elements} is a {@link Collection}, this method behaves exactly as    * {@link #copyOf(Collection)}; otherwise, it behaves exactly as {@code    * copyOf(elements.iterator()}.    *    * @throws NullPointerException if any of {@code elements} is null    */
DECL|method|copyOf (Iterable<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
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
name|checkNotNull
argument_list|(
name|elements
argument_list|)
expr_stmt|;
comment|// TODO(kevinb): is this here only for GWT?
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
comment|/**    * Returns an immutable list containing the given elements, in order.    *    *<p>Despite the method name, this method attempts to avoid actually copying    * the data when it is safe to do so. The exact circumstances under which a    * copy will or will not be performed are undocumented and subject to change.    *    *<p>Note that if {@code list} is a {@code List<String>}, then {@code    * ImmutableList.copyOf(list)} returns an {@code ImmutableList<String>}    * containing each of the strings in {@code list}, while    * ImmutableList.of(list)} returns an {@code ImmutableList<List<String>>}    * containing one element (the given list itself).    *    *<p>This method is safe to use even when {@code elements} is a synchronized    * or concurrent collection that is currently being modified by another    * thread.    *    * @throws NullPointerException if any of {@code elements} is null    * @since 2 (Iterable overload existed previously)    */
DECL|method|copyOf (Collection<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
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
if|if
condition|(
name|elements
operator|instanceof
name|ImmutableCollection
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// all supported methods are covariant
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|list
init|=
operator|(
operator|(
name|ImmutableCollection
argument_list|<
name|E
argument_list|>
operator|)
name|elements
operator|)
operator|.
name|asList
argument_list|()
decl_stmt|;
return|return
name|list
operator|.
name|isPartialView
argument_list|()
condition|?
name|copyFromCollection
argument_list|(
name|list
argument_list|)
else|:
name|list
return|;
block|}
return|return
name|copyFromCollection
argument_list|(
name|elements
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable list containing the given elements, in order.    *    * @throws NullPointerException if any of {@code elements} is null    */
DECL|method|copyOf (Iterator<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
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
return|return
name|copyFromCollection
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable list containing the given elements, in order.    *    * @throws NullPointerException if any of {@code elements} is null    * @since 3    */
DECL|method|copyOf (E[] elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
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
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
case|case
literal|1
case|:
return|return
operator|new
name|SingletonImmutableList
argument_list|<
name|E
argument_list|>
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
name|clone
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|copyFromCollection ( Collection<? extends E> collection)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|copyFromCollection
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
name|Object
index|[]
name|elements
init|=
name|collection
operator|.
name|toArray
argument_list|()
decl_stmt|;
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// collection had only Es in it
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|list
init|=
operator|new
name|SingletonImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
operator|(
name|E
operator|)
name|elements
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
return|return
name|list
return|;
default|default:
comment|// safe to use the array without copying it
comment|// as specified by Collection.toArray().
return|return
name|construct
argument_list|(
name|elements
argument_list|)
return|;
block|}
block|}
comment|/** {@code elements} has to be internally created array. */
DECL|method|construct (Object... elements)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|construct
parameter_list|(
name|Object
modifier|...
name|elements
parameter_list|)
block|{
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
name|checkElementNotNull
argument_list|(
name|elements
index|[
name|i
index|]
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|RegularImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|elements
argument_list|)
return|;
block|}
comment|// We do this instead of Preconditions.checkNotNull to save boxing and array
comment|// creation cost.
DECL|method|checkElementNotNull (Object element, int index)
specifier|private
specifier|static
name|Object
name|checkElementNotNull
parameter_list|(
name|Object
name|element
parameter_list|,
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|element
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"at index "
operator|+
name|index
argument_list|)
throw|;
block|}
return|return
name|element
return|;
block|}
DECL|method|ImmutableList ()
name|ImmutableList
parameter_list|()
block|{}
comment|// This declaration is needed to make List.iterator() and
comment|// ImmutableCollection.iterator() consistent.
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
name|listIterator
argument_list|()
return|;
block|}
DECL|method|listIterator ()
annotation|@
name|Override
specifier|public
name|UnmodifiableListIterator
argument_list|<
name|E
argument_list|>
name|listIterator
parameter_list|()
block|{
return|return
name|listIterator
argument_list|(
literal|0
argument_list|)
return|;
block|}
DECL|method|listIterator (int index)
annotation|@
name|Override
specifier|public
specifier|abstract
name|UnmodifiableListIterator
argument_list|<
name|E
argument_list|>
name|listIterator
parameter_list|(
name|int
name|index
parameter_list|)
function_decl|;
comment|// Mark these two methods with @Nullable
DECL|method|indexOf (@ullable Object object)
specifier|public
specifier|abstract
name|int
name|indexOf
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
function_decl|;
DECL|method|lastIndexOf (@ullable Object object)
specifier|public
specifier|abstract
name|int
name|lastIndexOf
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
function_decl|;
comment|// constrain the return type to ImmutableList<E>
comment|/**    * Returns an immutable list of the elements between the specified {@code    * fromIndex}, inclusive, and {@code toIndex}, exclusive. (If {@code    * fromIndex} and {@code toIndex} are equal, the empty immutable list is    * returned.)    */
DECL|method|subList (int fromIndex, int toIndex)
specifier|public
specifier|abstract
name|ImmutableList
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
function_decl|;
comment|/**    * Guaranteed to throw an exception and leave the list unmodified.    *    * @throws UnsupportedOperationException always    */
DECL|method|addAll (int index, Collection<? extends E> newElements)
specifier|public
specifier|final
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
name|newElements
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the list unmodified.    *    * @throws UnsupportedOperationException always    */
DECL|method|set (int index, E element)
specifier|public
specifier|final
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the list unmodified.    *    * @throws UnsupportedOperationException always    */
DECL|method|add (int index, E element)
specifier|public
specifier|final
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the list unmodified.    *    * @throws UnsupportedOperationException always    */
DECL|method|remove (int index)
specifier|public
specifier|final
name|E
name|remove
parameter_list|(
name|int
name|index
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Returns this list instance.    *    * @since 2    */
DECL|method|asList ()
annotation|@
name|Override
specifier|public
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|asList
parameter_list|()
block|{
return|return
name|this
return|;
block|}
comment|/*    * Serializes ImmutableLists as their logical contents. This ensures that    * implementation types do not leak into the serialized representation.    */
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
comment|/**    * A builder for creating immutable list instances, especially {@code public    * static final} lists ("constant lists"). Example:<pre>   {@code    *    *   public static final ImmutableList<Color> GOOGLE_COLORS    *       = new ImmutableList.Builder<Color>()    *           .addAll(WEBSAFE_COLORS)    *           .add(new Color(0, 191, 255))    *           .build();}</pre>    *    * Builder instances can be reused; it is safe to call {@link #build} multiple    * times to build multiple lists in series. Each new list contains all the    * elements of the ones created before it.    *    * @since 2 (imported from Google Collections Library)    */
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
comment|/**      * Creates a new builder. The returned builder is equivalent to the builder      * generated by {@link ImmutableList#builder}.      */
DECL|method|Builder ()
specifier|public
name|Builder
parameter_list|()
block|{}
comment|/**      * Adds {@code element} to the {@code ImmutableList}.      *      * @param element the element to add      * @return this {@code Builder} object      * @throws NullPointerException if {@code element} is null      */
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
comment|/**      * Adds each element of {@code elements} to the {@code ImmutableList}.      *      * @param elements the {@code Iterable} to add to the {@code ImmutableList}      * @return this {@code Builder} object      * @throws NullPointerException if {@code elements} is null or contains a      *     null element      */
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
comment|/**      * Adds each element of {@code elements} to the {@code ImmutableList}.      *      * @param elements the {@code Iterable} to add to the {@code ImmutableList}      * @return this {@code Builder} object      * @throws NullPointerException if {@code elements} is null or contains a      *     null element      */
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
comment|/**      * Adds each element of {@code elements} to the {@code ImmutableList}.      *      * @param elements the {@code Iterable} to add to the {@code ImmutableList}      * @return this {@code Builder} object      * @throws NullPointerException if {@code elements} is null or contains a      *     null element      */
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
comment|/**      * Returns a newly-created {@code ImmutableList} based on the contents of      * the {@code Builder}.      */
DECL|method|build ()
annotation|@
name|Override
specifier|public
name|ImmutableList
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

