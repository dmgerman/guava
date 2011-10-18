begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 Google Inc.  *   * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software distributed under the  * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  * express or implied. See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Beta
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractSet
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
name|NoSuchElementException
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
comment|/**  * A set of values of type {@code C} made up of zero or more<i>disjoint</i> {@linkplain Range  * ranges}.  *   *<p>It is guaranteed that {@linkplain Range#isConnected connected} ranges will be  * {@linkplain Range#span coalesced} together, and that {@linkplain Range#isEmpty empty} ranges  * will never be held in a {@code RangeSet}.  *  *<p>For a {@link Set} whose contents are specified by a {@link Range}, see {@link ContiguousSet}.  *   * @author Kevin Bourrillion  * @author Louis Wasserman  * @since 11.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|RangeSet
specifier|public
specifier|abstract
class|class
name|RangeSet
parameter_list|<
name|C
extends|extends
name|Comparable
parameter_list|>
block|{
DECL|method|RangeSet ()
name|RangeSet
parameter_list|()
block|{}
comment|/**    * Determines whether any of this range set's member ranges contains {@code value}.    */
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
comment|/**    * Returns the unique range from this range set that {@linkplain Range#contains contains}    * {@code value}, or {@code null} if this range set does not contain {@code value}.    */
DECL|method|rangeContaining (C value)
specifier|public
name|Range
argument_list|<
name|C
argument_list|>
name|rangeContaining
parameter_list|(
name|C
name|value
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
for|for
control|(
name|Range
argument_list|<
name|C
argument_list|>
name|range
range|:
name|asRanges
argument_list|()
control|)
block|{
if|if
condition|(
name|range
operator|.
name|contains
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|range
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**    * Returns a view of the {@linkplain Range#isConnected disconnected} ranges that make up this    * range set.  The returned set may be empty. The iterators returned by its     * {@link Iterable#iterator} method return the ranges in increasing order of lower bound     * (equivalently, of upper bound).    */
DECL|method|asRanges ()
specifier|public
specifier|abstract
name|Set
argument_list|<
name|Range
argument_list|<
name|C
argument_list|>
argument_list|>
name|asRanges
parameter_list|()
function_decl|;
comment|/**    * Returns {@code true} if this range set contains no ranges.    */
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
comment|/**    * Returns the range with the lowest lower bound in this range set.    *     * @throws NoSuchElementException if this range set is empty    */
DECL|method|firstRange ()
specifier|public
name|Range
argument_list|<
name|C
argument_list|>
name|firstRange
parameter_list|()
block|{
return|return
name|asRanges
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
comment|/**    * Returns the range with the greatest upper bound in this range set.    *     * @throws NoSuchElementException if this range set is empty    */
DECL|method|lastRange ()
specifier|public
name|Range
argument_list|<
name|C
argument_list|>
name|lastRange
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|getLast
argument_list|(
name|asRanges
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns the minimal range that {@linkplain Range#encloses encloses} every one of the disjoint    * ranges in this range set.    *     * @throws NoSuchElementException if this range set is empty    */
DECL|method|span ()
specifier|public
name|Range
argument_list|<
name|C
argument_list|>
name|span
parameter_list|()
block|{
name|Range
argument_list|<
name|C
argument_list|>
name|firstRange
init|=
name|firstRange
argument_list|()
decl_stmt|;
name|Range
argument_list|<
name|C
argument_list|>
name|lastRange
init|=
name|lastRange
argument_list|()
decl_stmt|;
return|return
operator|new
name|Range
argument_list|<
name|C
argument_list|>
argument_list|(
name|firstRange
operator|.
name|lowerBound
argument_list|,
name|lastRange
operator|.
name|upperBound
argument_list|)
return|;
block|}
comment|/**    * Returns a view of the complement of this {@code RangeSet}.    *     *<p>The returned view supports the {@link #add} operation if this {@code RangeSet} supports    * {@link #remove}, and vice versa.    */
DECL|method|complement ()
specifier|public
specifier|abstract
name|RangeSet
argument_list|<
name|C
argument_list|>
name|complement
parameter_list|()
function_decl|;
comment|/**    * A basic, simple implementation of {@link #complement}. This is not efficient on all methods;    * for example, {@link #rangeContaining} and {@link #encloses} are linear-time.    */
DECL|class|StandardComplement
specifier|static
class|class
name|StandardComplement
parameter_list|<
name|C
extends|extends
name|Comparable
parameter_list|>
extends|extends
name|RangeSet
argument_list|<
name|C
argument_list|>
block|{
DECL|field|positive
specifier|final
name|RangeSet
argument_list|<
name|C
argument_list|>
name|positive
decl_stmt|;
DECL|method|StandardComplement (RangeSet<C> positive)
specifier|public
name|StandardComplement
parameter_list|(
name|RangeSet
argument_list|<
name|C
argument_list|>
name|positive
parameter_list|)
block|{
name|this
operator|.
name|positive
operator|=
name|positive
expr_stmt|;
block|}
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
operator|!
name|positive
operator|.
name|contains
argument_list|(
name|value
argument_list|)
return|;
block|}
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
name|positive
operator|.
name|remove
argument_list|(
name|range
argument_list|)
expr_stmt|;
block|}
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
name|positive
operator|.
name|add
argument_list|(
name|range
argument_list|)
expr_stmt|;
block|}
DECL|field|asRanges
specifier|private
specifier|transient
name|Set
argument_list|<
name|Range
argument_list|<
name|C
argument_list|>
argument_list|>
name|asRanges
decl_stmt|;
annotation|@
name|Override
DECL|method|asRanges ()
specifier|public
specifier|final
name|Set
argument_list|<
name|Range
argument_list|<
name|C
argument_list|>
argument_list|>
name|asRanges
parameter_list|()
block|{
name|Set
argument_list|<
name|Range
argument_list|<
name|C
argument_list|>
argument_list|>
name|result
init|=
name|asRanges
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|asRanges
operator|=
name|createAsRanges
argument_list|()
else|:
name|result
return|;
block|}
DECL|method|createAsRanges ()
name|Set
argument_list|<
name|Range
argument_list|<
name|C
argument_list|>
argument_list|>
name|createAsRanges
parameter_list|()
block|{
return|return
operator|new
name|AbstractSet
argument_list|<
name|Range
argument_list|<
name|C
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|Range
argument_list|<
name|C
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
specifier|final
name|Iterator
argument_list|<
name|Range
argument_list|<
name|C
argument_list|>
argument_list|>
name|positiveIterator
init|=
name|positive
operator|.
name|asRanges
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
operator|new
name|AbstractIterator
argument_list|<
name|Range
argument_list|<
name|C
argument_list|>
argument_list|>
argument_list|()
block|{
name|Cut
argument_list|<
name|C
argument_list|>
name|prevCut
init|=
name|Cut
operator|.
name|belowAll
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|Range
argument_list|<
name|C
argument_list|>
name|computeNext
parameter_list|()
block|{
while|while
condition|(
name|positiveIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Cut
argument_list|<
name|C
argument_list|>
name|oldCut
init|=
name|prevCut
decl_stmt|;
name|Range
argument_list|<
name|C
argument_list|>
name|positiveRange
init|=
name|positiveIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|prevCut
operator|=
name|positiveRange
operator|.
name|upperBound
expr_stmt|;
if|if
condition|(
name|oldCut
operator|.
name|compareTo
argument_list|(
name|positiveRange
operator|.
name|lowerBound
argument_list|)
operator|<
literal|0
condition|)
block|{
return|return
operator|new
name|Range
argument_list|<
name|C
argument_list|>
argument_list|(
name|oldCut
argument_list|,
name|positiveRange
operator|.
name|lowerBound
argument_list|)
return|;
block|}
block|}
name|Cut
argument_list|<
name|C
argument_list|>
name|posInfinity
init|=
name|Cut
operator|.
name|aboveAll
argument_list|()
decl_stmt|;
if|if
condition|(
name|prevCut
operator|.
name|compareTo
argument_list|(
name|posInfinity
argument_list|)
operator|<
literal|0
condition|)
block|{
name|Range
argument_list|<
name|C
argument_list|>
name|result
init|=
operator|new
name|Range
argument_list|<
name|C
argument_list|>
argument_list|(
name|prevCut
argument_list|,
name|posInfinity
argument_list|)
decl_stmt|;
name|prevCut
operator|=
name|posInfinity
expr_stmt|;
return|return
name|result
return|;
block|}
return|return
name|endOfData
argument_list|()
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|size
argument_list|(
name|iterator
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|complement ()
specifier|public
name|RangeSet
argument_list|<
name|C
argument_list|>
name|complement
parameter_list|()
block|{
return|return
name|positive
return|;
block|}
block|}
comment|/**    * Adds the specified range to this {@code RangeSet} (optional operation). That is, for equal    * range sets a and b, the result of {@code a.add(range)} is that {@code a} will be the minimal    * range set for which both {@code a.enclosesAll(b)} and {@code a.encloses(range)}.    *     *<p>Note that {@code range} will be {@linkplain Range#span(Range) coalesced} with any ranges in    * the range set that are {@linkplain Range#isConnected(Range) connected} with it.  Moreover,    * if {@code range} is empty, this is a no-op.    *     * @throws UnsupportedOperationException if this range set does not support the {@code add}    *         operation    */
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
comment|/**    * Removes the specified range from this {@code RangeSet} (optional operation). After this    * operation, if {@code range.contains(c)}, {@code this.contains(c)} will return {@code false}.    *     *<p>If {@code range} is empty, this is a no-op.    *     * @throws UnsupportedOperationException if this range set does not support the {@code remove}    *         operation    */
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
comment|/**    * Returns {@code true} if there exists a member range in this range set which    * {@linkplain Range#encloses encloses} the specified range.    */
DECL|method|encloses (Range<C> otherRange)
specifier|public
name|boolean
name|encloses
parameter_list|(
name|Range
argument_list|<
name|C
argument_list|>
name|otherRange
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
name|asRanges
argument_list|()
control|)
block|{
if|if
condition|(
name|range
operator|.
name|encloses
argument_list|(
name|otherRange
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**    * Returns {@code true} if for each member range in {@code other} there exists a member range in    * this range set which {@linkplain Range#encloses encloses} it. It follows that    * {@code this.contains(value)} whenever {@code other.contains(value)}. Returns {@code true} if    * {@code other} is empty.    *     *<p>    * This is equivalent to checking if this range set {@link #encloses} each of the ranges in    * {@code other}.    */
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
for|for
control|(
name|Range
argument_list|<
name|C
argument_list|>
name|range
range|:
name|other
operator|.
name|asRanges
argument_list|()
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
comment|/**    * Adds all of the ranges from the specified range set to this range set (optional operation).    * After this operation, this range set is the minimal range set that    * {@linkplain #enclosesAll(RangeSet) encloses} both the original range set and {@code other}.    *     *<p>    * This is equivalent to calling {@link #add} on each of the ranges in {@code other} in turn.    *     * @throws UnsupportedOperationException if this range set does not support the {@code addAll}    *         operation    */
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
for|for
control|(
name|Range
argument_list|<
name|C
argument_list|>
name|range
range|:
name|other
operator|.
name|asRanges
argument_list|()
control|)
block|{
name|this
operator|.
name|add
argument_list|(
name|range
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Removes all of the ranges from the specified range set from this range set (optional    * operation). After this operation, if {@code other.contains(c)}, {@code this.contains(c)} will    * return {@code false}.    *     *<p>    * This is equivalent to calling {@link #remove} on each of the ranges in {@code other} in turn.    *     * @throws UnsupportedOperationException if this range set does not support the {@code removeAll}    *         operation    */
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
for|for
control|(
name|Range
argument_list|<
name|C
argument_list|>
name|range
range|:
name|other
operator|.
name|asRanges
argument_list|()
control|)
block|{
name|this
operator|.
name|remove
argument_list|(
name|range
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Returns {@code true} if {@code obj} is another {@code RangeSet} that contains the same ranges    * according to {@link Range#equals(Object)}.    */
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
comment|/**    * Returns a readable string representation of this range set. For example, if this    * {@code RangeSet} consisted of {@code Ranges.closed(1, 3)} and {@code Ranges.greaterThan(4)},    * this might return {@code " [1â¥3](4â¥+â)}"}.    */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
specifier|final
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
expr_stmt|;
for|for
control|(
name|Range
argument_list|<
name|C
argument_list|>
name|range
range|:
name|asRanges
argument_list|()
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|range
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

