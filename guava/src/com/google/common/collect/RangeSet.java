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
name|Beta
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
comment|/**  * A set comprising zero or more {@linkplain Range#isEmpty nonempty},  * {@linkplain Range#isConnected(Range) disconnected} ranges of type {@code C}.  *  *<p>Implementations that choose to support the {@link #add(Range)} operation are required to  * ignore empty ranges and coalesce connected ranges.  For example:<pre>   {@code  *  *   RangeSet<Integer> rangeSet = TreeRangeSet.create();  *   rangeSet.add(Range.closed(1, 10)); // {[1, 10]}  *   rangeSet.add(Range.closedOpen(11, 15)); // disconnected range; {[1, 10], [11, 15)}  *   rangeSet.add(Range.closedOpen(15, 20)); // connected range; {[1, 10], [11, 20)}  *   rangeSet.add(Range.openClosed(0, 0)); // empty range; {[1, 10], [11, 20)}  *   rangeSet.remove(Range.open(5, 10)); // splits [1, 10]; {[1, 5], [10, 10], [11, 20)}}</pre>  *  *<p>Note that the behavior of {@link Range#isEmpty()} and {@link Range#isConnected(Range)} may  * not be as expected on discrete ranges.  See the Javadoc of those methods for details.  *  *<p>For a {@link Set} whose contents are specified by a {@link Range}, see {@link ContiguousSet}.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#rangeset">  * RangeSets</a>.  *  * @author Kevin Bourrillion  * @author Louis Wasserman  * @since 14.0  */
end_comment

begin_interface
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
DECL|interface|RangeSet
specifier|public
interface|interface
name|RangeSet
parameter_list|<
name|C
extends|extends
name|Comparable
parameter_list|>
block|{
comment|// Query methods
comment|/**    * Determines whether any of this range set's member ranges contains {@code value}.    */
DECL|method|contains (C value)
name|boolean
name|contains
parameter_list|(
name|C
name|value
parameter_list|)
function_decl|;
comment|/**    * Returns the unique range from this range set that {@linkplain Range#contains contains}    * {@code value}, or {@code null} if this range set does not contain {@code value}.    */
DECL|method|rangeContaining (C value)
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
comment|/**    * Returns {@code true} if there exists a member range in this range set which    * {@linkplain Range#encloses encloses} the specified range.    */
DECL|method|encloses (Range<C> otherRange)
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
comment|/**    * Returns {@code true} if for each member range in {@code other} there exists a member range in    * this range set which {@linkplain Range#encloses encloses} it. It follows that    * {@code this.contains(value)} whenever {@code other.contains(value)}. Returns {@code true} if    * {@code other} is empty.    *    *<p>This is equivalent to checking if this range set {@link #encloses} each of the ranges in    * {@code other}.    */
DECL|method|enclosesAll (RangeSet<C> other)
name|boolean
name|enclosesAll
parameter_list|(
name|RangeSet
argument_list|<
name|C
argument_list|>
name|other
parameter_list|)
function_decl|;
comment|/**    * Returns {@code true} if this range set contains no ranges.    */
DECL|method|isEmpty ()
name|boolean
name|isEmpty
parameter_list|()
function_decl|;
comment|/**    * Returns the minimal range which {@linkplain Range#encloses(Range) encloses} all ranges    * in this range set.    *    * @throws NoSuchElementException if this range set is {@linkplain #isEmpty() empty}    */
DECL|method|span ()
name|Range
argument_list|<
name|C
argument_list|>
name|span
parameter_list|()
function_decl|;
comment|// Views
comment|/**    * Returns a view of the {@linkplain Range#isConnected disconnected} ranges that make up this    * range set.  The returned set may be empty. The iterators returned by its    * {@link Iterable#iterator} method return the ranges in increasing order of lower bound    * (equivalently, of upper bound).    */
DECL|method|asRanges ()
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
comment|/**    * Returns a descending view of the {@linkplain Range#isConnected disconnected} ranges that    * make up this range set. The returned set may be empty. The iterators returned by its    * {@link Iterable#iterator} method return the ranges in decreasing order of lower bound    * (equivalently, of upper bound).    *    * @since 19.0    */
DECL|method|asDescendingSetOfRanges ()
name|Set
argument_list|<
name|Range
argument_list|<
name|C
argument_list|>
argument_list|>
name|asDescendingSetOfRanges
parameter_list|()
function_decl|;
comment|/**    * Returns a view of the complement of this {@code RangeSet}.    *    *<p>The returned view supports the {@link #add} operation if this {@code RangeSet} supports    * {@link #remove}, and vice versa.    */
DECL|method|complement ()
name|RangeSet
argument_list|<
name|C
argument_list|>
name|complement
parameter_list|()
function_decl|;
comment|/**    * Returns a view of the intersection of this {@code RangeSet} with the specified range.    *    *<p>The returned view supports all optional operations supported by this {@code RangeSet}, with    * the caveat that an {@link IllegalArgumentException} is thrown on an attempt to    * {@linkplain #add(Range) add} any range not {@linkplain Range#encloses(Range) enclosed} by    * {@code view}.    */
DECL|method|subRangeSet (Range<C> view)
name|RangeSet
argument_list|<
name|C
argument_list|>
name|subRangeSet
parameter_list|(
name|Range
argument_list|<
name|C
argument_list|>
name|view
parameter_list|)
function_decl|;
comment|// Modification
comment|/**    * Adds the specified range to this {@code RangeSet} (optional operation). That is, for equal    * range sets a and b, the result of {@code a.add(range)} is that {@code a} will be the minimal    * range set for which both {@code a.enclosesAll(b)} and {@code a.encloses(range)}.    *    *<p>Note that {@code range} will be {@linkplain Range#span(Range) coalesced} with any ranges in    * the range set that are {@linkplain Range#isConnected(Range) connected} with it.  Moreover,    * if {@code range} is empty, this is a no-op.    *    * @throws UnsupportedOperationException if this range set does not support the {@code add}    *         operation    */
DECL|method|add (Range<C> range)
name|void
name|add
parameter_list|(
name|Range
argument_list|<
name|C
argument_list|>
name|range
parameter_list|)
function_decl|;
comment|/**    * Removes the specified range from this {@code RangeSet} (optional operation). After this    * operation, if {@code range.contains(c)}, {@code this.contains(c)} will return {@code false}.    *    *<p>If {@code range} is empty, this is a no-op.    *    * @throws UnsupportedOperationException if this range set does not support the {@code remove}    *         operation    */
DECL|method|remove (Range<C> range)
name|void
name|remove
parameter_list|(
name|Range
argument_list|<
name|C
argument_list|>
name|range
parameter_list|)
function_decl|;
comment|/**    * Removes all ranges from this {@code RangeSet} (optional operation).  After this operation,    * {@code this.contains(c)} will return false for all {@code c}.    *    *<p>This is equivalent to {@code remove(Range.all())}.    *    * @throws UnsupportedOperationException if this range set does not support the {@code clear}    *         operation    */
DECL|method|clear ()
name|void
name|clear
parameter_list|()
function_decl|;
comment|/**    * Adds all of the ranges from the specified range set to this range set (optional operation).    * After this operation, this range set is the minimal range set that    * {@linkplain #enclosesAll(RangeSet) encloses} both the original range set and {@code other}.    *    *<p>This is equivalent to calling {@link #add} on each of the ranges in {@code other} in turn.    *    * @throws UnsupportedOperationException if this range set does not support the {@code addAll}    *         operation    */
DECL|method|addAll (RangeSet<C> other)
name|void
name|addAll
parameter_list|(
name|RangeSet
argument_list|<
name|C
argument_list|>
name|other
parameter_list|)
function_decl|;
comment|/**    * Removes all of the ranges from the specified range set from this range set (optional    * operation). After this operation, if {@code other.contains(c)}, {@code this.contains(c)} will    * return {@code false}.    *    *<p>This is equivalent to calling {@link #remove} on each of the ranges in {@code other} in    * turn.    *    * @throws UnsupportedOperationException if this range set does not support the {@code removeAll}    *         operation    */
DECL|method|removeAll (RangeSet<C> other)
name|void
name|removeAll
parameter_list|(
name|RangeSet
argument_list|<
name|C
argument_list|>
name|other
parameter_list|)
function_decl|;
comment|// Object methods
comment|/**    * Returns {@code true} if {@code obj} is another {@code RangeSet} that contains the same ranges    * according to {@link Range#equals(Object)}.    */
annotation|@
name|Override
DECL|method|equals (@ullable Object obj)
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
function_decl|;
comment|/**    * Returns {@code asRanges().hashCode()}.    */
annotation|@
name|Override
DECL|method|hashCode ()
name|int
name|hashCode
parameter_list|()
function_decl|;
comment|/**    * Returns a readable string representation of this range set. For example, if this    * {@code RangeSet} consisted of {@code Range.closed(1, 3)} and {@code Range.greaterThan(4)},    * this might return {@code " [1â¥3](4â¥+â)}"}.    */
annotation|@
name|Override
DECL|method|toString ()
name|String
name|toString
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

