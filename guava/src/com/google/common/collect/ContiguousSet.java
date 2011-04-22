begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Collection
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
comment|/**  * A sorted set of contiguous values in a given {@link DiscreteDomain}.  *  * @author gak@google.com (Gregory Kick)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// allow ungenerified Comparable types
DECL|class|ContiguousSet
specifier|final
class|class
name|ContiguousSet
parameter_list|<
name|C
extends|extends
name|Comparable
parameter_list|>
extends|extends
name|ImmutableSortedSet
argument_list|<
name|C
argument_list|>
block|{
DECL|field|range
specifier|private
specifier|final
name|Range
argument_list|<
name|C
argument_list|>
name|range
decl_stmt|;
DECL|field|domain
specifier|private
specifier|final
name|DiscreteDomain
argument_list|<
name|C
argument_list|>
name|domain
decl_stmt|;
DECL|method|ContiguousSet (Range<C> range, DiscreteDomain<C> domain)
name|ContiguousSet
parameter_list|(
name|Range
argument_list|<
name|C
argument_list|>
name|range
parameter_list|,
name|DiscreteDomain
argument_list|<
name|C
argument_list|>
name|domain
parameter_list|)
block|{
name|super
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|range
operator|=
name|range
expr_stmt|;
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
block|}
DECL|method|headSet (C toElement)
annotation|@
name|Override
specifier|public
name|ImmutableSortedSet
argument_list|<
name|C
argument_list|>
name|headSet
parameter_list|(
name|C
name|toElement
parameter_list|)
block|{
return|return
name|headSetImpl
argument_list|(
name|checkNotNull
argument_list|(
name|toElement
argument_list|)
argument_list|)
return|;
block|}
comment|// Abstract method doesn't exist in GWT emulation
DECL|method|headSetImpl (C toElement)
comment|/* @Override */
name|ImmutableSortedSet
argument_list|<
name|C
argument_list|>
name|headSetImpl
parameter_list|(
name|C
name|toElement
parameter_list|)
block|{
return|return
name|range
operator|.
name|intersection
argument_list|(
name|Ranges
operator|.
name|lessThan
argument_list|(
name|toElement
argument_list|)
argument_list|)
operator|.
name|asSet
argument_list|(
name|domain
argument_list|)
return|;
block|}
comment|// Abstract method doesn't exist in GWT emulation
DECL|method|indexOf (Object target)
comment|/* @Override */
name|int
name|indexOf
parameter_list|(
name|Object
name|target
parameter_list|)
block|{
return|return
name|contains
argument_list|(
name|target
argument_list|)
condition|?
operator|(
name|int
operator|)
name|domain
operator|.
name|distance
argument_list|(
name|first
argument_list|()
argument_list|,
operator|(
name|C
operator|)
name|target
argument_list|)
else|:
operator|-
literal|1
return|;
block|}
DECL|method|subSet (C fromElement, C toElement)
annotation|@
name|Override
specifier|public
name|ImmutableSortedSet
argument_list|<
name|C
argument_list|>
name|subSet
parameter_list|(
name|C
name|fromElement
parameter_list|,
name|C
name|toElement
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
name|checkArgument
argument_list|(
name|comparator
argument_list|()
operator|.
name|compare
argument_list|(
name|fromElement
argument_list|,
name|toElement
argument_list|)
operator|<=
literal|0
argument_list|)
expr_stmt|;
return|return
name|subSetImpl
argument_list|(
name|fromElement
argument_list|,
name|toElement
argument_list|)
return|;
block|}
comment|// Abstract method doesn't exist in GWT emulation
DECL|method|subSetImpl (C fromElement, C toElement)
comment|/* @Override */
name|ImmutableSortedSet
argument_list|<
name|C
argument_list|>
name|subSetImpl
parameter_list|(
name|C
name|fromElement
parameter_list|,
name|C
name|toElement
parameter_list|)
block|{
return|return
name|range
operator|.
name|intersection
argument_list|(
name|Ranges
operator|.
name|closedOpen
argument_list|(
name|fromElement
argument_list|,
name|toElement
argument_list|)
argument_list|)
operator|.
name|asSet
argument_list|(
name|domain
argument_list|)
return|;
block|}
DECL|method|tailSet (C fromElement)
annotation|@
name|Override
specifier|public
name|ImmutableSortedSet
argument_list|<
name|C
argument_list|>
name|tailSet
parameter_list|(
name|C
name|fromElement
parameter_list|)
block|{
return|return
name|tailSetImpl
argument_list|(
name|checkNotNull
argument_list|(
name|fromElement
argument_list|)
argument_list|)
return|;
block|}
comment|// Abstract method doesn't exist in GWT emulation
DECL|method|tailSetImpl (C fromElement)
comment|/* @Override */
name|ImmutableSortedSet
argument_list|<
name|C
argument_list|>
name|tailSetImpl
parameter_list|(
name|C
name|fromElement
parameter_list|)
block|{
return|return
name|range
operator|.
name|intersection
argument_list|(
name|Ranges
operator|.
name|atLeast
argument_list|(
name|fromElement
argument_list|)
argument_list|)
operator|.
name|asSet
argument_list|(
name|domain
argument_list|)
return|;
block|}
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
name|UnmodifiableIterator
argument_list|<
name|C
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|AbstractLinkedIterator
argument_list|<
name|C
argument_list|>
argument_list|(
name|first
argument_list|()
argument_list|)
block|{
specifier|final
name|C
name|last
init|=
name|last
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|C
name|computeNext
parameter_list|(
name|C
name|previous
parameter_list|)
block|{
return|return
name|equalsOrThrow
argument_list|(
name|previous
argument_list|,
name|last
argument_list|)
condition|?
literal|null
else|:
name|domain
operator|.
name|next
argument_list|(
name|previous
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|equalsOrThrow (Comparable<?> left, @Nullable Comparable<?> right)
specifier|private
specifier|static
name|boolean
name|equalsOrThrow
parameter_list|(
name|Comparable
argument_list|<
name|?
argument_list|>
name|left
parameter_list|,
annotation|@
name|Nullable
name|Comparable
argument_list|<
name|?
argument_list|>
name|right
parameter_list|)
block|{
return|return
name|right
operator|!=
literal|null
operator|&&
name|compareOrThrow
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
operator|==
literal|0
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// this method may throw CCE
DECL|method|compareOrThrow (Comparable left, Comparable right)
specifier|private
specifier|static
name|int
name|compareOrThrow
parameter_list|(
name|Comparable
name|left
parameter_list|,
name|Comparable
name|right
parameter_list|)
block|{
return|return
name|left
operator|.
name|compareTo
argument_list|(
name|right
argument_list|)
return|;
block|}
DECL|method|isPartialView ()
annotation|@
name|Override
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|first ()
annotation|@
name|Override
specifier|public
name|C
name|first
parameter_list|()
block|{
return|return
name|range
operator|.
name|lowerBound
operator|.
name|leastValueAbove
argument_list|(
name|domain
argument_list|)
return|;
block|}
DECL|method|last ()
annotation|@
name|Override
specifier|public
name|C
name|last
parameter_list|()
block|{
return|return
name|range
operator|.
name|upperBound
operator|.
name|greatestValueBelow
argument_list|(
name|domain
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
name|long
name|distance
init|=
name|domain
operator|.
name|distance
argument_list|(
name|first
argument_list|()
argument_list|,
name|last
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|(
name|distance
operator|>=
name|Integer
operator|.
name|MAX_VALUE
operator|)
condition|?
name|Integer
operator|.
name|MAX_VALUE
else|:
operator|(
name|int
operator|)
name|distance
operator|+
literal|1
return|;
block|}
DECL|method|contains (Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
try|try
block|{
return|return
name|range
operator|.
name|contains
argument_list|(
operator|(
name|C
operator|)
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
try|try
block|{
return|return
name|range
operator|.
name|containsAll
argument_list|(
operator|(
name|Iterable
argument_list|<
name|?
extends|extends
name|C
argument_list|>
operator|)
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
comment|// copied to make sure not to use the GWT-emulated version
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
name|ObjectArrays
operator|.
name|toArrayImpl
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|// copied to make sure not to use the GWT-emulated version
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
return|return
name|ObjectArrays
operator|.
name|toArrayImpl
argument_list|(
name|this
argument_list|,
name|other
argument_list|)
return|;
block|}
DECL|method|equals (Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
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
name|ContiguousSet
condition|)
block|{
name|ContiguousSet
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|ContiguousSet
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
name|domain
operator|.
name|equals
argument_list|(
name|that
operator|.
name|domain
argument_list|)
condition|)
block|{
return|return
name|this
operator|.
name|first
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|first
argument_list|()
argument_list|)
operator|&&
name|this
operator|.
name|last
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|last
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
name|super
operator|.
name|equals
argument_list|(
name|object
argument_list|)
return|;
block|}
comment|// copied to make sure not to use the GWT-emulated version
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
comment|/**    * Returns a short-hand representation of the contents such as    * {@code "[1â¥100]}"}.    */
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|Ranges
operator|.
name|closed
argument_list|(
name|first
argument_list|()
argument_list|,
name|last
argument_list|()
argument_list|)
operator|.
name|toString
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
end_class

end_unit

