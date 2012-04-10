begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|collect
operator|.
name|Lists
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
comment|/**  * GWT emulated version of {@link ImmutableList}.  * TODO(cpovirk): more doc  *  * @author Hayward Chan  */
end_comment

begin_class
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
DECL|method|ImmutableList ()
name|ImmutableList
parameter_list|()
block|{}
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
operator|new
name|RegularImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|ImmutableList
operator|.
expr|<
name|E
operator|>
name|nullCheckedList
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|)
argument_list|)
return|;
block|}
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
operator|new
name|RegularImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|ImmutableList
operator|.
expr|<
name|E
operator|>
name|nullCheckedList
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|,
name|e3
argument_list|)
argument_list|)
return|;
block|}
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
operator|new
name|RegularImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|ImmutableList
operator|.
expr|<
name|E
operator|>
name|nullCheckedList
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|,
name|e3
argument_list|,
name|e4
argument_list|)
argument_list|)
return|;
block|}
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
operator|new
name|RegularImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|ImmutableList
operator|.
expr|<
name|E
operator|>
name|nullCheckedList
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
argument_list|)
return|;
block|}
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
operator|new
name|RegularImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|ImmutableList
operator|.
expr|<
name|E
operator|>
name|nullCheckedList
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
argument_list|)
return|;
block|}
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
operator|new
name|RegularImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|ImmutableList
operator|.
expr|<
name|E
operator|>
name|nullCheckedList
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
argument_list|)
return|;
block|}
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
operator|new
name|RegularImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|ImmutableList
operator|.
expr|<
name|E
operator|>
name|nullCheckedList
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
argument_list|)
return|;
block|}
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
operator|new
name|RegularImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|ImmutableList
operator|.
expr|<
name|E
operator|>
name|nullCheckedList
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
argument_list|)
return|;
block|}
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
operator|new
name|RegularImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|ImmutableList
operator|.
expr|<
name|E
operator|>
name|nullCheckedList
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
argument_list|)
return|;
block|}
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
operator|new
name|RegularImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|ImmutableList
operator|.
expr|<
name|E
operator|>
name|nullCheckedList
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
argument_list|)
return|;
block|}
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
specifier|final
name|int
name|paramCount
init|=
literal|12
decl_stmt|;
name|Object
index|[]
name|array
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
name|arrayCopy
argument_list|(
name|array
argument_list|,
literal|0
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
argument_list|,
name|e12
argument_list|)
expr_stmt|;
name|arrayCopy
argument_list|(
name|array
argument_list|,
name|paramCount
argument_list|,
name|others
argument_list|)
expr_stmt|;
return|return
operator|new
name|RegularImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|ImmutableList
operator|.
expr|<
name|E
operator|>
name|nullCheckedList
argument_list|(
name|array
argument_list|)
argument_list|)
return|;
block|}
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
operator|new
name|RegularImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|ImmutableList
operator|.
expr|<
name|E
operator|>
name|nullCheckedList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|method|arrayCopy (Object[] dest, int pos, Object... source)
specifier|private
specifier|static
name|void
name|arrayCopy
parameter_list|(
name|Object
index|[]
name|dest
parameter_list|,
name|int
name|pos
parameter_list|,
name|Object
modifier|...
name|source
parameter_list|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|source
argument_list|,
literal|0
argument_list|,
name|dest
argument_list|,
name|pos
argument_list|,
name|source
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
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
comment|// for GWT
return|return
operator|(
name|elements
operator|instanceof
name|Collection
operator|)
condition|?
name|copyOf
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
operator|)
name|elements
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
comment|/*        * TODO: When given an ImmutableList that's a sublist, copy the referenced        * portion of the array into a new array to save space?        */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// all supported methods are covariant
name|ImmutableCollection
argument_list|<
name|E
argument_list|>
name|list
init|=
operator|(
name|ImmutableCollection
argument_list|<
name|E
argument_list|>
operator|)
name|elements
decl_stmt|;
return|return
name|list
operator|.
name|asList
argument_list|()
return|;
block|}
return|return
name|copyFromCollection
argument_list|(
name|elements
argument_list|)
return|;
block|}
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
name|checkNotNull
argument_list|(
name|elements
argument_list|)
expr_stmt|;
comment|// eager for GWT
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
return|return
operator|new
name|RegularImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|ImmutableList
operator|.
expr|<
name|E
operator|>
name|nullCheckedList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
block|}
comment|// Factory method that skips the null checks.  Used only when the elements
comment|// are guaranteed to be non-null.
DECL|method|unsafeDelegateList (List<? extends E> list)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|unsafeDelegateList
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|list
parameter_list|)
block|{
switch|switch
condition|(
name|list
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
return|return
operator|new
name|SingletonImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|list
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
return|;
default|default:
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|E
argument_list|>
name|castedList
init|=
operator|(
name|List
argument_list|<
name|E
argument_list|>
operator|)
name|list
decl_stmt|;
return|return
operator|new
name|RegularImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|castedList
argument_list|)
return|;
block|}
block|}
comment|/**    * Views the array as an immutable list.  The array must have only {@code E} elements.    *    *<p>The array must be internally created.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// caller is reponsible for getting this right
DECL|method|asImmutableList (Object[] elements)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|asImmutableList
parameter_list|(
name|Object
index|[]
name|elements
parameter_list|)
block|{
return|return
name|unsafeDelegateList
argument_list|(
operator|(
name|List
operator|)
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
DECL|method|nullCheckedList (Object... array)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|List
argument_list|<
name|E
argument_list|>
name|nullCheckedList
parameter_list|(
name|Object
modifier|...
name|array
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|len
init|=
name|array
operator|.
name|length
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|array
index|[
name|i
index|]
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
name|i
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|E
index|[]
name|castedArray
init|=
operator|(
name|E
index|[]
operator|)
name|array
decl_stmt|;
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|castedArray
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|indexOf (@ullable Object object)
specifier|public
name|int
name|indexOf
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
operator|(
name|object
operator|==
literal|null
operator|)
condition|?
operator|-
literal|1
else|:
name|Lists
operator|.
name|indexOfImpl
argument_list|(
name|this
argument_list|,
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|lastIndexOf (@ullable Object object)
specifier|public
name|int
name|lastIndexOf
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
operator|(
name|object
operator|==
literal|null
operator|)
condition|?
operator|-
literal|1
else|:
name|Lists
operator|.
name|lastIndexOfImpl
argument_list|(
name|this
argument_list|,
name|object
argument_list|)
return|;
block|}
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
DECL|method|subList (int fromIndex, int toIndex)
annotation|@
name|Override
specifier|public
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
block|{
return|return
name|unsafeDelegateList
argument_list|(
name|Lists
operator|.
name|subListImpl
argument_list|(
name|this
argument_list|,
name|fromIndex
argument_list|,
name|toIndex
argument_list|)
argument_list|)
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
name|UnmodifiableListIterator
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
operator|new
name|AbstractIndexedListIterator
argument_list|<
name|E
argument_list|>
argument_list|(
name|size
argument_list|()
argument_list|,
name|index
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|E
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|ImmutableList
operator|.
name|this
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
block|}
return|;
block|}
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
DECL|method|reverse ()
specifier|public
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|reverse
parameter_list|()
block|{
name|List
argument_list|<
name|E
argument_list|>
name|list
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|reverse
argument_list|(
name|list
argument_list|)
expr_stmt|;
return|return
name|unsafeDelegateList
argument_list|(
name|list
argument_list|)
return|;
block|}
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
DECL|method|Builder ()
specifier|public
name|Builder
parameter_list|()
block|{}
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

