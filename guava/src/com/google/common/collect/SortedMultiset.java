begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *   * Licensed under the Apache License, Version 2.0 (the "License"); you may not  * use this file except in compliance with the License. You may obtain a copy of  * the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the  * License for the specific language governing permissions and limitations under  * the License.  */
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
name|collect
operator|.
name|Range
operator|.
name|BoundType
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
name|SortedSet
import|;
end_import

begin_comment
comment|/**  * A {@link Multiset} which maintains the ordering of its elements, according to  * either their natural order or an explicit {@link Comparator}. In all cases,  * this implementation uses {@link Comparable#compareTo} or  * {@link Comparator#compare} instead of {@link Object#equals} to determine  * equivalence of instances.  *   *<p>  *<b>Warning:</b> The comparison must be<i>consistent with equals</i> as  * explained by the {@link Comparable} class specification. Otherwise, the  * resulting multiset will violate the {@link Collection} contract, which it is  * specified in terms of {@link Object#equals}.  *   * @author Louis Wasserman  */
end_comment

begin_interface
DECL|interface|SortedMultiset
annotation|@
name|Beta
interface|interface
name|SortedMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|Multiset
argument_list|<
name|E
argument_list|>
block|{
comment|/**    * Returns the comparator that orders this multiset, or    * {@link Ordering#natural()} if the natural ordering of the elements is used.    */
DECL|method|comparator ()
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|()
function_decl|;
comment|/**    * Returns the entry of the first element in this multiset, or {@code null} if    * this multiset is empty.    */
DECL|method|firstEntry ()
name|Entry
argument_list|<
name|E
argument_list|>
name|firstEntry
parameter_list|()
function_decl|;
comment|/**    * Returns the entry of the last element in this multiset, or {@code null} if    * this multiset is empty.    */
DECL|method|lastEntry ()
name|Entry
argument_list|<
name|E
argument_list|>
name|lastEntry
parameter_list|()
function_decl|;
comment|/**    * Returns and removes the entry associated with the lowest element in this    * multiset, or returns {@code null} if this multiset is empty.    */
DECL|method|pollFirstEntry ()
name|Entry
argument_list|<
name|E
argument_list|>
name|pollFirstEntry
parameter_list|()
function_decl|;
comment|/**    * Returns and removes the entry associated with the greatest element in this    * multiset, or returns {@code null} if this multiset is empty.    */
DECL|method|pollLastEntry ()
name|Entry
argument_list|<
name|E
argument_list|>
name|pollLastEntry
parameter_list|()
function_decl|;
comment|/**    * Returns a {@link SortedSet} view of the distinct elements in this multiset.    */
DECL|method|elementSet ()
annotation|@
name|Override
name|SortedSet
argument_list|<
name|E
argument_list|>
name|elementSet
parameter_list|()
function_decl|;
comment|/**    * {@inheritDoc}    *     *<p>    * The iterator returns the elements in ascending order according to this    * multiset's comparator.    */
DECL|method|iterator ()
annotation|@
name|Override
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
function_decl|;
comment|/**    * Returns a descending view of this multiset. Modifications made to either    * map will be reflected in the other.    */
DECL|method|descendingMultiset ()
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|descendingMultiset
parameter_list|()
function_decl|;
comment|/**    * Returns a view of this multiset restricted to the elements less than    * {@code upperBound}, optionally including {@code upperBound} itself. The    * returned multiset is a view of this multiset, so changes to one will be    * reflected in the other. The returned multiset supports all operations that    * this multiset supports.    *     *<p>The returned multiset will throw an {@link IllegalArgumentException} on    * attempts to add elements outside its range.    */
DECL|method|headMultiset (E upperBound, BoundType boundType)
name|SortedMultiset
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
function_decl|;
comment|/**    * Returns a view of this multiset restricted to the range between    * {@code lowerBound} and {@code upperBound}. The returned multiset is a view    * of this multiset, so changes to one will be reflected in the other. The    * returned multiset supports all operations that this multiset supports.    *     *<p>The returned multiset will throw an {@link IllegalArgumentException} on    * attempts to add elements outside its range.    *     *<p>This method is equivalent to    * {@code tailMultiset(lowerBound, lowerBoundType).headMultiset(upperBound,    * upperBoundType)}.    */
DECL|method|subMultiset (E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType)
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|subMultiset
parameter_list|(
name|E
name|lowerBound
parameter_list|,
name|BoundType
name|lowerBoundType
parameter_list|,
name|E
name|upperBound
parameter_list|,
name|BoundType
name|upperBoundType
parameter_list|)
function_decl|;
comment|/**    * Returns a view of this multiset restricted to the elements greater than    * {@code lowerBound}, optionally including {@code lowerBound} itself. The    * returned multiset is a view of this multiset, so changes to one will be    * reflected in the other. The returned multiset supports all operations that    * this multiset supports.    *     *<p>The returned multiset will throw an {@link IllegalArgumentException} on    * attempts to add elements outside its range.    */
DECL|method|tailMultiset (E lowerBound, BoundType boundType)
name|SortedMultiset
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
function_decl|;
block|}
end_interface

end_unit

