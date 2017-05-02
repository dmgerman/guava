begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the  * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  * express or implied. See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|checkPositionIndexes
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
name|util
operator|.
name|Comparator
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
comment|/**  * An immutable sorted multiset with one or more distinct elements.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// uses writeReplace, not default serialization
annotation|@
name|GwtIncompatible
DECL|class|RegularImmutableSortedMultiset
specifier|final
class|class
name|RegularImmutableSortedMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableSortedMultiset
argument_list|<
name|E
argument_list|>
block|{
DECL|field|ZERO_CUMULATIVE_COUNTS
specifier|private
specifier|static
specifier|final
name|long
index|[]
name|ZERO_CUMULATIVE_COUNTS
init|=
block|{
literal|0
block|}
decl_stmt|;
DECL|field|NATURAL_EMPTY_MULTISET
specifier|static
specifier|final
name|ImmutableSortedMultiset
argument_list|<
name|Comparable
argument_list|>
name|NATURAL_EMPTY_MULTISET
init|=
operator|new
name|RegularImmutableSortedMultiset
argument_list|<
name|Comparable
argument_list|>
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|VisibleForTesting
DECL|field|elementSet
specifier|final
specifier|transient
name|RegularImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|elementSet
decl_stmt|;
DECL|field|cumulativeCounts
specifier|private
specifier|final
specifier|transient
name|long
index|[]
name|cumulativeCounts
decl_stmt|;
DECL|field|offset
specifier|private
specifier|final
specifier|transient
name|int
name|offset
decl_stmt|;
DECL|field|length
specifier|private
specifier|final
specifier|transient
name|int
name|length
decl_stmt|;
DECL|method|RegularImmutableSortedMultiset (Comparator<? super E> comparator)
name|RegularImmutableSortedMultiset
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
name|elementSet
operator|=
name|ImmutableSortedSet
operator|.
name|emptySet
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
name|this
operator|.
name|cumulativeCounts
operator|=
name|ZERO_CUMULATIVE_COUNTS
expr_stmt|;
name|this
operator|.
name|offset
operator|=
literal|0
expr_stmt|;
name|this
operator|.
name|length
operator|=
literal|0
expr_stmt|;
block|}
DECL|method|RegularImmutableSortedMultiset ( RegularImmutableSortedSet<E> elementSet, long[] cumulativeCounts, int offset, int length)
name|RegularImmutableSortedMultiset
parameter_list|(
name|RegularImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|elementSet
parameter_list|,
name|long
index|[]
name|cumulativeCounts
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|this
operator|.
name|elementSet
operator|=
name|elementSet
expr_stmt|;
name|this
operator|.
name|cumulativeCounts
operator|=
name|cumulativeCounts
expr_stmt|;
name|this
operator|.
name|offset
operator|=
name|offset
expr_stmt|;
name|this
operator|.
name|length
operator|=
name|length
expr_stmt|;
block|}
DECL|method|getCount (int index)
specifier|private
name|int
name|getCount
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
call|(
name|int
call|)
argument_list|(
name|cumulativeCounts
index|[
name|offset
operator|+
name|index
operator|+
literal|1
index|]
operator|-
name|cumulativeCounts
index|[
name|offset
operator|+
name|index
index|]
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getEntry (int index)
name|Entry
argument_list|<
name|E
argument_list|>
name|getEntry
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|elementSet
operator|.
name|asList
argument_list|()
operator|.
name|get
argument_list|(
name|index
argument_list|)
argument_list|,
name|getCount
argument_list|(
name|index
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|firstEntry ()
specifier|public
name|Entry
argument_list|<
name|E
argument_list|>
name|firstEntry
parameter_list|()
block|{
return|return
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|getEntry
argument_list|(
literal|0
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|lastEntry ()
specifier|public
name|Entry
argument_list|<
name|E
argument_list|>
name|lastEntry
parameter_list|()
block|{
return|return
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|getEntry
argument_list|(
name|length
operator|-
literal|1
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|int
name|index
init|=
name|elementSet
operator|.
name|indexOf
argument_list|(
name|element
argument_list|)
decl_stmt|;
return|return
operator|(
name|index
operator|>=
literal|0
operator|)
condition|?
name|getCount
argument_list|(
name|index
argument_list|)
else|:
literal|0
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
name|long
name|size
init|=
name|cumulativeCounts
index|[
name|offset
operator|+
name|length
index|]
operator|-
name|cumulativeCounts
index|[
name|offset
index|]
decl_stmt|;
return|return
name|Ints
operator|.
name|saturatedCast
argument_list|(
name|size
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|elementSet ()
specifier|public
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|elementSet
parameter_list|()
block|{
return|return
name|elementSet
return|;
block|}
annotation|@
name|Override
DECL|method|headMultiset (E upperBound, BoundType boundType)
specifier|public
name|ImmutableSortedMultiset
argument_list|<
name|E
argument_list|>
name|headMultiset
parameter_list|(
name|E
name|upperBound
parameter_list|,
name|BoundType
name|boundType
parameter_list|)
block|{
return|return
name|getSubMultiset
argument_list|(
literal|0
argument_list|,
name|elementSet
operator|.
name|headIndex
argument_list|(
name|upperBound
argument_list|,
name|checkNotNull
argument_list|(
name|boundType
argument_list|)
operator|==
name|CLOSED
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|tailMultiset (E lowerBound, BoundType boundType)
specifier|public
name|ImmutableSortedMultiset
argument_list|<
name|E
argument_list|>
name|tailMultiset
parameter_list|(
name|E
name|lowerBound
parameter_list|,
name|BoundType
name|boundType
parameter_list|)
block|{
return|return
name|getSubMultiset
argument_list|(
name|elementSet
operator|.
name|tailIndex
argument_list|(
name|lowerBound
argument_list|,
name|checkNotNull
argument_list|(
name|boundType
argument_list|)
operator|==
name|CLOSED
argument_list|)
argument_list|,
name|length
argument_list|)
return|;
block|}
DECL|method|getSubMultiset (int from, int to)
name|ImmutableSortedMultiset
argument_list|<
name|E
argument_list|>
name|getSubMultiset
parameter_list|(
name|int
name|from
parameter_list|,
name|int
name|to
parameter_list|)
block|{
name|checkPositionIndexes
argument_list|(
name|from
argument_list|,
name|to
argument_list|,
name|length
argument_list|)
expr_stmt|;
if|if
condition|(
name|from
operator|==
name|to
condition|)
block|{
return|return
name|emptyMultiset
argument_list|(
name|comparator
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|from
operator|==
literal|0
operator|&&
name|to
operator|==
name|length
condition|)
block|{
return|return
name|this
return|;
block|}
else|else
block|{
name|RegularImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|subElementSet
init|=
name|elementSet
operator|.
name|getSubSet
argument_list|(
name|from
argument_list|,
name|to
argument_list|)
decl_stmt|;
return|return
operator|new
name|RegularImmutableSortedMultiset
argument_list|<
name|E
argument_list|>
argument_list|(
name|subElementSet
argument_list|,
name|cumulativeCounts
argument_list|,
name|offset
operator|+
name|from
argument_list|,
name|to
operator|-
name|from
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
name|offset
operator|>
literal|0
operator|||
name|length
operator|<
name|cumulativeCounts
operator|.
name|length
operator|-
literal|1
return|;
block|}
block|}
end_class

end_unit

