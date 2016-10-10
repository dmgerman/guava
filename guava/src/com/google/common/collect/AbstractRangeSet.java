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
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A skeletal implementation of {@code RangeSet}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|AbstractRangeSet
specifier|abstract
class|class
name|AbstractRangeSet
parameter_list|<
name|C
extends|extends
name|Comparable
parameter_list|>
implements|implements
name|RangeSet
argument_list|<
name|C
argument_list|>
block|{
DECL|method|AbstractRangeSet ()
name|AbstractRangeSet
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|contains (C value)
specifier|public
name|boolean
name|contains
parameter_list|(
name|C
name|value
parameter_list|)
block|{
return|return
name|rangeContaining
argument_list|(
name|value
argument_list|)
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|rangeContaining (C value)
specifier|public
specifier|abstract
name|Range
argument_list|<
name|C
argument_list|>
name|rangeContaining
parameter_list|(
name|C
name|value
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|asRanges
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|add (Range<C> range)
specifier|public
name|void
name|add
parameter_list|(
name|Range
argument_list|<
name|C
argument_list|>
name|range
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|remove (Range<C> range)
specifier|public
name|void
name|remove
parameter_list|(
name|Range
argument_list|<
name|C
argument_list|>
name|range
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|remove
argument_list|(
name|Range
operator|.
expr|<
name|C
operator|>
name|all
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|enclosesAll (RangeSet<C> other)
specifier|public
name|boolean
name|enclosesAll
parameter_list|(
name|RangeSet
argument_list|<
name|C
argument_list|>
name|other
parameter_list|)
block|{
return|return
name|enclosesAll
argument_list|(
name|other
operator|.
name|asRanges
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|enclosesAll (Iterable<Range<C>> ranges)
specifier|public
name|boolean
name|enclosesAll
parameter_list|(
name|Iterable
argument_list|<
name|Range
argument_list|<
name|C
argument_list|>
argument_list|>
name|ranges
parameter_list|)
block|{
for|for
control|(
name|Range
argument_list|<
name|C
argument_list|>
name|range
range|:
name|ranges
control|)
block|{
if|if
condition|(
operator|!
name|encloses
argument_list|(
name|range
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
annotation|@
name|Override
DECL|method|addAll (RangeSet<C> other)
specifier|public
name|void
name|addAll
parameter_list|(
name|RangeSet
argument_list|<
name|C
argument_list|>
name|other
parameter_list|)
block|{
name|addAll
argument_list|(
name|other
operator|.
name|asRanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addAll (Iterable<Range<C>> ranges)
specifier|public
name|void
name|addAll
parameter_list|(
name|Iterable
argument_list|<
name|Range
argument_list|<
name|C
argument_list|>
argument_list|>
name|ranges
parameter_list|)
block|{
for|for
control|(
name|Range
argument_list|<
name|C
argument_list|>
name|range
range|:
name|ranges
control|)
block|{
name|add
argument_list|(
name|range
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|removeAll (RangeSet<C> other)
specifier|public
name|void
name|removeAll
parameter_list|(
name|RangeSet
argument_list|<
name|C
argument_list|>
name|other
parameter_list|)
block|{
name|removeAll
argument_list|(
name|other
operator|.
name|asRanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|removeAll (Iterable<Range<C>> ranges)
specifier|public
name|void
name|removeAll
parameter_list|(
name|Iterable
argument_list|<
name|Range
argument_list|<
name|C
argument_list|>
argument_list|>
name|ranges
parameter_list|)
block|{
for|for
control|(
name|Range
argument_list|<
name|C
argument_list|>
name|range
range|:
name|ranges
control|)
block|{
name|remove
argument_list|(
name|range
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|intersects (Range<C> otherRange)
specifier|public
name|boolean
name|intersects
parameter_list|(
name|Range
argument_list|<
name|C
argument_list|>
name|otherRange
parameter_list|)
block|{
return|return
operator|!
name|subRangeSet
argument_list|(
name|otherRange
argument_list|)
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|encloses (Range<C> otherRange)
specifier|public
specifier|abstract
name|boolean
name|encloses
parameter_list|(
name|Range
argument_list|<
name|C
argument_list|>
name|otherRange
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|equals (@ullable Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
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
name|obj
operator|instanceof
name|RangeSet
condition|)
block|{
name|RangeSet
argument_list|<
name|?
argument_list|>
name|other
init|=
operator|(
name|RangeSet
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|this
operator|.
name|asRanges
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|asRanges
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
specifier|final
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|asRanges
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
specifier|final
name|String
name|toString
parameter_list|()
block|{
return|return
name|asRanges
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

