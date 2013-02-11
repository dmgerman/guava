begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|BoundType
operator|.
name|CLOSED
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
comment|/**  * An implementation of {@link ContiguousSet} that contains one or more elements.  *  * @author Gregory Kick  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// allow ungenerified Comparable types
DECL|class|RegularContiguousSet
specifier|final
class|class
name|RegularContiguousSet
parameter_list|<
name|C
extends|extends
name|Comparable
parameter_list|>
extends|extends
name|ContiguousSet
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
DECL|method|RegularContiguousSet (Range<C> range, DiscreteDomain<C> domain)
name|RegularContiguousSet
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
name|domain
argument_list|)
expr_stmt|;
name|this
operator|.
name|range
operator|=
name|range
expr_stmt|;
block|}
DECL|method|intersectionInCurrentDomain (Range<C> other)
specifier|private
name|ContiguousSet
argument_list|<
name|C
argument_list|>
name|intersectionInCurrentDomain
parameter_list|(
name|Range
argument_list|<
name|C
argument_list|>
name|other
parameter_list|)
block|{
return|return
operator|(
name|range
operator|.
name|isConnected
argument_list|(
name|other
argument_list|)
operator|)
condition|?
name|ContiguousSet
operator|.
name|create
argument_list|(
name|range
operator|.
name|intersection
argument_list|(
name|other
argument_list|)
argument_list|,
name|domain
argument_list|)
else|:
operator|new
name|EmptyContiguousSet
argument_list|<
name|C
argument_list|>
argument_list|(
name|domain
argument_list|)
return|;
block|}
DECL|method|headSetImpl (C toElement, boolean inclusive)
annotation|@
name|Override
name|ContiguousSet
argument_list|<
name|C
argument_list|>
name|headSetImpl
parameter_list|(
name|C
name|toElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
return|return
name|intersectionInCurrentDomain
argument_list|(
name|Range
operator|.
name|upTo
argument_list|(
name|toElement
argument_list|,
name|BoundType
operator|.
name|forBoolean
argument_list|(
name|inclusive
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|subSetImpl (C fromElement, boolean fromInclusive, C toElement, boolean toInclusive)
annotation|@
name|Override
name|ContiguousSet
argument_list|<
name|C
argument_list|>
name|subSetImpl
parameter_list|(
name|C
name|fromElement
parameter_list|,
name|boolean
name|fromInclusive
parameter_list|,
name|C
name|toElement
parameter_list|,
name|boolean
name|toInclusive
parameter_list|)
block|{
if|if
condition|(
name|fromElement
operator|.
name|compareTo
argument_list|(
name|toElement
argument_list|)
operator|==
literal|0
operator|&&
operator|!
name|fromInclusive
operator|&&
operator|!
name|toInclusive
condition|)
block|{
comment|// Range would reject our attempt to create (x, x).
return|return
operator|new
name|EmptyContiguousSet
argument_list|<
name|C
argument_list|>
argument_list|(
name|domain
argument_list|)
return|;
block|}
return|return
name|intersectionInCurrentDomain
argument_list|(
name|Range
operator|.
name|range
argument_list|(
name|fromElement
argument_list|,
name|BoundType
operator|.
name|forBoolean
argument_list|(
name|fromInclusive
argument_list|)
argument_list|,
name|toElement
argument_list|,
name|BoundType
operator|.
name|forBoolean
argument_list|(
name|toInclusive
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|tailSetImpl (C fromElement, boolean inclusive)
annotation|@
name|Override
name|ContiguousSet
argument_list|<
name|C
argument_list|>
name|tailSetImpl
parameter_list|(
name|C
name|fromElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
return|return
name|intersectionInCurrentDomain
argument_list|(
name|Range
operator|.
name|downTo
argument_list|(
name|fromElement
argument_list|,
name|BoundType
operator|.
name|forBoolean
argument_list|(
name|inclusive
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"not used by GWT emulation"
argument_list|)
DECL|method|indexOf (Object target)
annotation|@
name|Override
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
name|AbstractSequentialIterator
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
annotation|@
name|GwtIncompatible
argument_list|(
literal|"NavigableSet"
argument_list|)
DECL|method|descendingIterator ()
annotation|@
name|Override
specifier|public
name|UnmodifiableIterator
argument_list|<
name|C
argument_list|>
name|descendingIterator
parameter_list|()
block|{
return|return
operator|new
name|AbstractSequentialIterator
argument_list|<
name|C
argument_list|>
argument_list|(
name|last
argument_list|()
argument_list|)
block|{
specifier|final
name|C
name|first
init|=
name|first
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
name|first
argument_list|)
condition|?
literal|null
else|:
name|domain
operator|.
name|previous
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
name|Range
operator|.
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
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
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
return|return
name|Collections2
operator|.
name|containsAllImpl
argument_list|(
name|this
argument_list|,
name|targets
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
literal|false
return|;
block|}
DECL|method|intersection (ContiguousSet<C> other)
annotation|@
name|Override
specifier|public
name|ContiguousSet
argument_list|<
name|C
argument_list|>
name|intersection
parameter_list|(
name|ContiguousSet
argument_list|<
name|C
argument_list|>
name|other
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|other
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|this
operator|.
name|domain
operator|.
name|equals
argument_list|(
name|other
operator|.
name|domain
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|other
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|other
return|;
block|}
else|else
block|{
name|C
name|lowerEndpoint
init|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|max
argument_list|(
name|this
operator|.
name|first
argument_list|()
argument_list|,
name|other
operator|.
name|first
argument_list|()
argument_list|)
decl_stmt|;
name|C
name|upperEndpoint
init|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|min
argument_list|(
name|this
operator|.
name|last
argument_list|()
argument_list|,
name|other
operator|.
name|last
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|(
name|lowerEndpoint
operator|.
name|compareTo
argument_list|(
name|upperEndpoint
argument_list|)
operator|<
literal|0
operator|)
condition|?
name|ContiguousSet
operator|.
name|create
argument_list|(
name|Range
operator|.
name|closed
argument_list|(
name|lowerEndpoint
argument_list|,
name|upperEndpoint
argument_list|)
argument_list|,
name|domain
argument_list|)
else|:
operator|new
name|EmptyContiguousSet
argument_list|<
name|C
argument_list|>
argument_list|(
name|domain
argument_list|)
return|;
block|}
block|}
DECL|method|range ()
annotation|@
name|Override
specifier|public
name|Range
argument_list|<
name|C
argument_list|>
name|range
parameter_list|()
block|{
return|return
name|range
argument_list|(
name|CLOSED
argument_list|,
name|CLOSED
argument_list|)
return|;
block|}
DECL|method|range (BoundType lowerBoundType, BoundType upperBoundType)
annotation|@
name|Override
specifier|public
name|Range
argument_list|<
name|C
argument_list|>
name|range
parameter_list|(
name|BoundType
name|lowerBoundType
parameter_list|,
name|BoundType
name|upperBoundType
parameter_list|)
block|{
return|return
name|Range
operator|.
name|create
argument_list|(
name|range
operator|.
name|lowerBound
operator|.
name|withLowerBoundType
argument_list|(
name|lowerBoundType
argument_list|,
name|domain
argument_list|)
argument_list|,
name|range
operator|.
name|upperBound
operator|.
name|withUpperBoundType
argument_list|(
name|upperBoundType
argument_list|,
name|domain
argument_list|)
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
name|RegularContiguousSet
condition|)
block|{
name|RegularContiguousSet
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|RegularContiguousSet
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
annotation|@
name|GwtIncompatible
argument_list|(
literal|"serialization"
argument_list|)
DECL|class|SerializedForm
specifier|private
specifier|static
specifier|final
class|class
name|SerializedForm
parameter_list|<
name|C
extends|extends
name|Comparable
parameter_list|>
implements|implements
name|Serializable
block|{
DECL|field|range
specifier|final
name|Range
argument_list|<
name|C
argument_list|>
name|range
decl_stmt|;
DECL|field|domain
specifier|final
name|DiscreteDomain
argument_list|<
name|C
argument_list|>
name|domain
decl_stmt|;
DECL|method|SerializedForm (Range<C> range, DiscreteDomain<C> domain)
specifier|private
name|SerializedForm
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
DECL|method|readResolve ()
specifier|private
name|Object
name|readResolve
parameter_list|()
block|{
return|return
operator|new
name|RegularContiguousSet
argument_list|<
name|C
argument_list|>
argument_list|(
name|range
argument_list|,
name|domain
argument_list|)
return|;
block|}
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"serialization"
argument_list|)
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
argument_list|<
name|C
argument_list|>
argument_list|(
name|range
argument_list|,
name|domain
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
end_class

end_unit

