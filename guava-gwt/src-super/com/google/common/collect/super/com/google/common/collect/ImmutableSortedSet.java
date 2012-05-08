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
name|List
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
comment|/**  * GWT emulation of {@link ImmutableSortedSet}.  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|ImmutableSortedSet
specifier|public
specifier|abstract
class|class
name|ImmutableSortedSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingImmutableSet
argument_list|<
name|E
argument_list|>
implements|implements
name|SortedSet
argument_list|<
name|E
argument_list|>
implements|,
name|SortedIterable
argument_list|<
name|E
argument_list|>
block|{
comment|// TODO(cpovirk): split into ImmutableSortedSet/ForwardingImmutableSortedSet?
comment|// In the non-emulated source, this is in ImmutableSortedSetFauxverideShim,
comment|// which overrides ImmutableSet& which ImmutableSortedSet extends.
comment|// It is necessary here because otherwise the builder() method
comment|// would be inherited from the emulated ImmutableSet.
DECL|method|builder ()
annotation|@
name|Deprecated
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSortedSet
operator|.
name|Builder
argument_list|<
name|E
argument_list|>
name|builder
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|// TODO: Can we find a way to remove this @SuppressWarnings even for eclipse?
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
name|NATURAL_ORDER
init|=
name|Ordering
operator|.
name|natural
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|field|NATURAL_EMPTY_SET
specifier|private
specifier|static
specifier|final
name|ImmutableSortedSet
argument_list|<
name|Object
argument_list|>
name|NATURAL_EMPTY_SET
init|=
operator|new
name|EmptyImmutableSortedSet
argument_list|<
name|Object
argument_list|>
argument_list|(
name|NATURAL_ORDER
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|emptySet ()
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|emptySet
parameter_list|()
block|{
return|return
operator|(
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
operator|)
name|NATURAL_EMPTY_SET
return|;
block|}
DECL|method|emptySet ( Comparator<? super E> comparator)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|emptySet
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
name|checkNotNull
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
if|if
condition|(
name|NATURAL_ORDER
operator|.
name|equals
argument_list|(
name|comparator
argument_list|)
condition|)
block|{
return|return
name|emptySet
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|EmptyImmutableSortedSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|comparator
argument_list|)
return|;
block|}
block|}
DECL|method|of ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|of
parameter_list|()
block|{
return|return
name|emptySet
argument_list|()
return|;
block|}
DECL|method|of ( E element)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|ImmutableSortedSet
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
name|ofInternal
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
name|element
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|of ( E e1, E e2)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|ImmutableSortedSet
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
name|ofInternal
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
name|e1
argument_list|,
name|e2
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|of ( E e1, E e2, E e3)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|ImmutableSortedSet
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
name|ofInternal
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
name|e1
argument_list|,
name|e2
argument_list|,
name|e3
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|of ( E e1, E e2, E e3, E e4)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|ImmutableSortedSet
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
name|ofInternal
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|of ( E e1, E e2, E e3, E e4, E e5)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|ImmutableSortedSet
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
name|ofInternal
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|of ( E e1, E e2, E e3, E e4, E e5, E e6, E... remaining)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|ImmutableSortedSet
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
name|remaining
parameter_list|)
block|{
name|int
name|size
init|=
name|remaining
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
name|remaining
argument_list|)
expr_stmt|;
comment|// This is messed up. See TODO at top of file.
return|return
name|ofInternal
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
operator|(
name|E
index|[]
operator|)
name|all
operator|.
name|toArray
argument_list|(
operator|new
name|Comparable
index|[
literal|0
index|]
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
specifier|public
DECL|method|of ( E[] elements)
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|ImmutableSortedSet
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
DECL|method|ofInternal ( Comparator<? super E> comparator, E... elements)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|ofInternal
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|,
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
name|emptySet
argument_list|(
name|comparator
argument_list|)
return|;
default|default:
name|SortedSet
argument_list|<
name|E
argument_list|>
name|delegate
init|=
operator|new
name|TreeSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|comparator
argument_list|)
decl_stmt|;
for|for
control|(
name|E
name|element
range|:
name|elements
control|)
block|{
name|checkNotNull
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
return|return
operator|new
name|RegularImmutableSortedSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|delegate
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
DECL|method|copyOf ( Collection<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|ImmutableSortedSet
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
return|return
name|copyOfInternal
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
name|elements
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|copyOf ( Iterable<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|ImmutableSortedSet
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
name|copyOfInternal
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
name|elements
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|copyOf ( Iterator<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|ImmutableSortedSet
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
name|copyOfInternal
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
name|elements
argument_list|)
return|;
block|}
DECL|method|copyOf ( E[] elements)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|ImmutableSortedSet
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
name|ofInternal
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
name|elements
argument_list|)
return|;
block|}
DECL|method|copyOf ( Comparator<? super E> comparator, Iterable<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|copyOf
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|,
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
name|comparator
argument_list|)
expr_stmt|;
return|return
name|copyOfInternal
argument_list|(
name|comparator
argument_list|,
name|elements
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|copyOf ( Comparator<? super E> comparator, Collection<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|copyOf
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
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
name|checkNotNull
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
return|return
name|copyOfInternal
argument_list|(
name|comparator
argument_list|,
name|elements
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|copyOf ( Comparator<? super E> comparator, Iterator<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|copyOf
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|,
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
name|comparator
argument_list|)
expr_stmt|;
return|return
name|copyOfInternal
argument_list|(
name|comparator
argument_list|,
name|elements
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|copyOfSorted (SortedSet<E> sortedSet)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|copyOfSorted
parameter_list|(
name|SortedSet
argument_list|<
name|E
argument_list|>
name|sortedSet
parameter_list|)
block|{
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
init|=
name|sortedSet
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
name|NATURAL_ORDER
expr_stmt|;
block|}
return|return
name|copyOfInternal
argument_list|(
name|comparator
argument_list|,
name|sortedSet
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
DECL|method|copyOfInternal ( Comparator<? super E> comparator, Iterable<? extends E> elements, boolean fromSortedSet)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|copyOfInternal
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|,
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
parameter_list|,
name|boolean
name|fromSortedSet
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
name|boolean
name|hasSameComparator
init|=
name|fromSortedSet
operator|||
name|hasSameComparator
argument_list|(
name|elements
argument_list|,
name|comparator
argument_list|)
decl_stmt|;
if|if
condition|(
name|hasSameComparator
operator|&&
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
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|result
init|=
operator|(
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
operator|)
name|elements
decl_stmt|;
name|boolean
name|isSubset
init|=
operator|(
name|result
operator|instanceof
name|RegularImmutableSortedSet
operator|)
operator|&&
operator|(
operator|(
name|RegularImmutableSortedSet
operator|)
name|result
operator|)
operator|.
name|isSubset
decl_stmt|;
if|if
condition|(
operator|!
name|isSubset
condition|)
block|{
comment|// Only return the original copy if this immutable sorted set isn't
comment|// a subset of another, to avoid memory leak.
return|return
name|result
return|;
block|}
block|}
return|return
name|copyOfInternal
argument_list|(
name|comparator
argument_list|,
name|elements
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
DECL|method|copyOfInternal ( Comparator<? super E> comparator, Iterator<? extends E> elements)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|copyOfInternal
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|,
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
name|comparator
argument_list|)
expr_stmt|;
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
name|emptySet
argument_list|(
name|comparator
argument_list|)
return|;
block|}
name|SortedSet
argument_list|<
name|E
argument_list|>
name|delegate
init|=
operator|new
name|TreeSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|comparator
argument_list|)
decl_stmt|;
while|while
condition|(
name|elements
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|E
name|element
init|=
name|elements
operator|.
name|next
argument_list|()
decl_stmt|;
name|checkNotNull
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
return|return
operator|new
name|RegularImmutableSortedSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|delegate
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|hasSameComparator ( Iterable<?> elements, Comparator<?> comparator)
specifier|private
specifier|static
name|boolean
name|hasSameComparator
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|elements
parameter_list|,
name|Comparator
argument_list|<
name|?
argument_list|>
name|comparator
parameter_list|)
block|{
if|if
condition|(
name|elements
operator|instanceof
name|SortedSet
condition|)
block|{
name|SortedSet
argument_list|<
name|?
argument_list|>
name|sortedSet
init|=
operator|(
name|SortedSet
argument_list|<
name|?
argument_list|>
operator|)
name|elements
decl_stmt|;
name|Comparator
argument_list|<
name|?
argument_list|>
name|comparator2
init|=
name|sortedSet
operator|.
name|comparator
argument_list|()
decl_stmt|;
return|return
operator|(
name|comparator2
operator|==
literal|null
operator|)
condition|?
name|comparator
operator|==
name|Ordering
operator|.
name|natural
argument_list|()
else|:
name|comparator
operator|.
name|equals
argument_list|(
name|comparator2
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
comment|// Assumes that delegate doesn't have null elements and comparator.
DECL|method|unsafeDelegateSortedSet ( SortedSet<E> delegate, boolean isSubset)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|unsafeDelegateSortedSet
parameter_list|(
name|SortedSet
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|,
name|boolean
name|isSubset
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|isEmpty
argument_list|()
condition|?
name|emptySet
argument_list|(
name|delegate
operator|.
name|comparator
argument_list|()
argument_list|)
else|:
operator|new
name|RegularImmutableSortedSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|delegate
argument_list|,
name|isSubset
argument_list|)
return|;
block|}
comment|// This reference is only used by GWT compiler to infer the elements of the
comment|// set that needs to be serialized.
DECL|field|unusedComparatorForSerialization
specifier|private
name|Comparator
argument_list|<
name|E
argument_list|>
name|unusedComparatorForSerialization
decl_stmt|;
DECL|field|unusedElementForSerialization
specifier|private
name|E
name|unusedElementForSerialization
decl_stmt|;
DECL|field|sortedDelegate
specifier|private
specifier|transient
specifier|final
name|SortedSet
argument_list|<
name|E
argument_list|>
name|sortedDelegate
decl_stmt|;
comment|/**    * Scary constructor for ContiguousSet. This constructor (in this file, the    * GWT emulation of ImmutableSortedSet) creates an empty sortedDelegate,    * which, in a vacuum, sets this object's contents to empty.  By contrast,    * the non-GWT constructor with the same signature uses the comparator only    * as a comparator. It does NOT assume empty contents. (It requires an    * implementation of iterator() to define its contents, and methods like    * contains() are implemented in terms of that method (though they will    * likely be overridden by subclasses for performance reasons).) This means    * that a call to this method have can different behavior in GWT and non-GWT    * environments UNLESS subclasses are careful to always override all methods    * implemented in terms of sortedDelegate (except comparator()).    */
DECL|method|ImmutableSortedSet (Comparator<? super E> comparator)
name|ImmutableSortedSet
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
name|Sets
operator|.
name|newTreeSet
argument_list|(
name|comparator
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|ImmutableSortedSet (SortedSet<E> sortedDelegate)
name|ImmutableSortedSet
parameter_list|(
name|SortedSet
argument_list|<
name|E
argument_list|>
name|sortedDelegate
parameter_list|)
block|{
name|super
argument_list|(
name|sortedDelegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|sortedDelegate
operator|=
name|Collections
operator|.
name|unmodifiableSortedSet
argument_list|(
name|sortedDelegate
argument_list|)
expr_stmt|;
block|}
DECL|method|comparator ()
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
return|return
name|sortedDelegate
operator|.
name|comparator
argument_list|()
return|;
block|}
annotation|@
name|Override
comment|// needed to unify SortedIterable and Collection iterator() methods
DECL|method|iterator ()
specifier|public
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|super
operator|.
name|iterator
argument_list|()
return|;
block|}
DECL|method|contains (@ullable Object object)
annotation|@
name|Override
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
try|try
block|{
comment|// This set never contains null.  We need to explicitly check here
comment|// because some comparator might throw NPE (e.g. the natural ordering).
return|return
name|object
operator|!=
literal|null
operator|&&
name|sortedDelegate
operator|.
name|contains
argument_list|(
name|object
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
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
for|for
control|(
name|Object
name|target
range|:
name|targets
control|)
block|{
if|if
condition|(
name|target
operator|==
literal|null
condition|)
block|{
comment|// This set never contains null.  We need to explicitly check here
comment|// because some comparator might throw NPE (e.g. the natural ordering).
return|return
literal|false
return|;
block|}
block|}
try|try
block|{
return|return
name|sortedDelegate
operator|.
name|containsAll
argument_list|(
name|targets
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|first ()
specifier|public
name|E
name|first
parameter_list|()
block|{
return|return
name|sortedDelegate
operator|.
name|first
argument_list|()
return|;
block|}
DECL|method|headSet (E toElement)
specifier|public
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|headSet
parameter_list|(
name|E
name|toElement
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|toElement
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|unsafeDelegateSortedSet
argument_list|(
name|sortedDelegate
operator|.
name|headSet
argument_list|(
name|toElement
argument_list|)
argument_list|,
literal|true
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
return|return
name|emptySet
argument_list|(
name|comparator
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|higher (E e)
name|E
name|higher
parameter_list|(
name|E
name|e
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
init|=
name|tailSet
argument_list|(
name|e
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|E
name|higher
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|comparator
argument_list|()
operator|.
name|compare
argument_list|(
name|e
argument_list|,
name|higher
argument_list|)
operator|<
literal|0
condition|)
block|{
return|return
name|higher
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|headSet (E toElement, boolean inclusive)
name|ImmutableSortedSet
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
name|checkNotNull
argument_list|(
name|toElement
argument_list|)
expr_stmt|;
if|if
condition|(
name|inclusive
condition|)
block|{
name|E
name|tmp
init|=
name|higher
argument_list|(
name|toElement
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmp
operator|==
literal|null
condition|)
block|{
return|return
name|this
return|;
block|}
name|toElement
operator|=
name|tmp
expr_stmt|;
block|}
return|return
name|headSet
argument_list|(
name|toElement
argument_list|)
return|;
block|}
DECL|method|last ()
specifier|public
name|E
name|last
parameter_list|()
block|{
return|return
name|sortedDelegate
operator|.
name|last
argument_list|()
return|;
block|}
DECL|method|subSet (E fromElement, E toElement)
specifier|public
name|ImmutableSortedSet
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
DECL|method|subSet (E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
name|ImmutableSortedSet
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
name|checkNotNull
argument_list|(
name|fromElement
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|toElement
argument_list|)
expr_stmt|;
name|int
name|cmp
init|=
name|comparator
argument_list|()
operator|.
name|compare
argument_list|(
name|fromElement
argument_list|,
name|toElement
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|cmp
operator|<=
literal|0
argument_list|,
literal|"fromElement (%s) is less than toElement (%s)"
argument_list|,
name|fromElement
argument_list|,
name|toElement
argument_list|)
expr_stmt|;
if|if
condition|(
name|cmp
operator|==
literal|0
operator|&&
operator|!
operator|(
name|fromInclusive
operator|&&
name|toInclusive
operator|)
condition|)
block|{
return|return
name|emptySet
argument_list|(
name|comparator
argument_list|()
argument_list|)
return|;
block|}
return|return
name|tailSet
argument_list|(
name|fromElement
argument_list|,
name|fromInclusive
argument_list|)
operator|.
name|headSet
argument_list|(
name|toElement
argument_list|,
name|toInclusive
argument_list|)
return|;
block|}
DECL|method|tailSet (E fromElement)
specifier|public
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|tailSet
parameter_list|(
name|E
name|fromElement
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|fromElement
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|unsafeDelegateSortedSet
argument_list|(
name|sortedDelegate
operator|.
name|tailSet
argument_list|(
name|fromElement
argument_list|)
argument_list|,
literal|true
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
return|return
name|emptySet
argument_list|(
name|comparator
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|tailSet (E fromElement, boolean inclusive)
name|ImmutableSortedSet
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
name|checkNotNull
argument_list|(
name|fromElement
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|inclusive
condition|)
block|{
name|E
name|tmp
init|=
name|higher
argument_list|(
name|fromElement
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmp
operator|==
literal|null
condition|)
block|{
return|return
name|emptySet
argument_list|(
name|comparator
argument_list|()
argument_list|)
return|;
block|}
name|fromElement
operator|=
name|tmp
expr_stmt|;
block|}
return|return
name|tailSet
argument_list|(
name|fromElement
argument_list|)
return|;
block|}
DECL|method|orderedBy (Comparator<E> comparator)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Builder
argument_list|<
name|E
argument_list|>
name|orderedBy
parameter_list|(
name|Comparator
argument_list|<
name|E
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
operator|new
name|Builder
argument_list|<
name|E
argument_list|>
argument_list|(
name|comparator
argument_list|)
return|;
block|}
DECL|method|reverseOrder ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
parameter_list|>
name|Builder
argument_list|<
name|E
argument_list|>
name|reverseOrder
parameter_list|()
block|{
return|return
operator|new
name|Builder
argument_list|<
name|E
argument_list|>
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|reverse
argument_list|()
argument_list|)
return|;
block|}
DECL|method|naturalOrder ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
parameter_list|>
name|Builder
argument_list|<
name|E
argument_list|>
name|naturalOrder
parameter_list|()
block|{
return|return
operator|new
name|Builder
argument_list|<
name|E
argument_list|>
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
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
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|E
argument_list|>
block|{
DECL|field|comparator
specifier|private
specifier|final
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
decl_stmt|;
DECL|method|Builder (Comparator<? super E> comparator)
specifier|public
name|Builder
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
operator|.
name|comparator
operator|=
name|checkNotNull
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
block|}
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
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|build
parameter_list|()
block|{
return|return
name|copyOfInternal
argument_list|(
name|comparator
argument_list|,
name|contents
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

