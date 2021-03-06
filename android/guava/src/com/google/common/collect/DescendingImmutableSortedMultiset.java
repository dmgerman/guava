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
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
import|;
end_import

begin_comment
comment|/**  * A descending wrapper around an {@code ImmutableSortedMultiset}  *  * @author Louis Wasserman  */
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
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|DescendingImmutableSortedMultiset
specifier|final
class|class
name|DescendingImmutableSortedMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableSortedMultiset
argument_list|<
name|E
argument_list|>
block|{
DECL|field|forward
specifier|private
specifier|final
specifier|transient
name|ImmutableSortedMultiset
argument_list|<
name|E
argument_list|>
name|forward
decl_stmt|;
DECL|method|DescendingImmutableSortedMultiset (ImmutableSortedMultiset<E> forward)
name|DescendingImmutableSortedMultiset
parameter_list|(
name|ImmutableSortedMultiset
argument_list|<
name|E
argument_list|>
name|forward
parameter_list|)
block|{
name|this
operator|.
name|forward
operator|=
name|forward
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|count (@heckForNull Object element)
specifier|public
name|int
name|count
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|element
parameter_list|)
block|{
return|return
name|forward
operator|.
name|count
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|CheckForNull
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
name|forward
operator|.
name|lastEntry
argument_list|()
return|;
block|}
annotation|@
name|Override
annotation|@
name|CheckForNull
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
name|forward
operator|.
name|firstEntry
argument_list|()
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
return|return
name|forward
operator|.
name|size
argument_list|()
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
name|forward
operator|.
name|elementSet
argument_list|()
operator|.
name|descendingSet
argument_list|()
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
name|forward
operator|.
name|entrySet
argument_list|()
operator|.
name|asList
argument_list|()
operator|.
name|reverse
argument_list|()
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|descendingMultiset ()
specifier|public
name|ImmutableSortedMultiset
argument_list|<
name|E
argument_list|>
name|descendingMultiset
parameter_list|()
block|{
return|return
name|forward
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
name|forward
operator|.
name|tailMultiset
argument_list|(
name|upperBound
argument_list|,
name|boundType
argument_list|)
operator|.
name|descendingMultiset
argument_list|()
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
name|forward
operator|.
name|headMultiset
argument_list|(
name|lowerBound
argument_list|,
name|boundType
argument_list|)
operator|.
name|descendingMultiset
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
name|forward
operator|.
name|isPartialView
argument_list|()
return|;
block|}
block|}
end_class

end_unit

