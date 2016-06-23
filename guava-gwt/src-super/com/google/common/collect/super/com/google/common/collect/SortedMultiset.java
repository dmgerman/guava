begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not  * use this file except in compliance with the License. You may obtain a copy of  * the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the  * License for the specific language governing permissions and limitations under  * the License.  */
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
name|SortedSet
import|;
end_import

begin_comment
comment|/**  * GWT emulation of {@code SortedMultiset}, with {@code elementSet} reduced  * to returning a {@code SortedSet} for GWT compatibility.  *  * @author Louis Wasserman  * @since 11.0  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|SortedMultiset
specifier|public
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
extends|,
name|SortedIterable
argument_list|<
name|E
argument_list|>
block|{
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
DECL|method|firstEntry ()
name|Entry
argument_list|<
name|E
argument_list|>
name|firstEntry
parameter_list|()
function_decl|;
DECL|method|lastEntry ()
name|Entry
argument_list|<
name|E
argument_list|>
name|lastEntry
parameter_list|()
function_decl|;
DECL|method|pollFirstEntry ()
name|Entry
argument_list|<
name|E
argument_list|>
name|pollFirstEntry
parameter_list|()
function_decl|;
DECL|method|pollLastEntry ()
name|Entry
argument_list|<
name|E
argument_list|>
name|pollLastEntry
parameter_list|()
function_decl|;
comment|/**    * Returns a {@link SortedSet} view of the distinct elements in this multiset.    * (Outside GWT, this returns a {@code NavigableSet}.)    */
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
DECL|method|descendingMultiset ()
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|descendingMultiset
parameter_list|()
function_decl|;
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

