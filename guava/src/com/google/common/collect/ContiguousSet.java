begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|GwtCompatible
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

begin_comment
comment|/**  * A sorted set of contiguous values in a given {@link DiscreteDomain}.  *  * @author Gregory Kick  * @since 10.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// allow ungenerified Comparable types
DECL|class|ContiguousSet
specifier|public
specifier|abstract
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
DECL|field|domain
specifier|final
name|DiscreteDomain
argument_list|<
name|C
argument_list|>
name|domain
decl_stmt|;
DECL|method|ContiguousSet (DiscreteDomain<C> domain)
name|ContiguousSet
parameter_list|(
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
name|domain
operator|=
name|domain
expr_stmt|;
block|}
DECL|method|headSet (C toElement)
annotation|@
name|Override
specifier|public
name|ContiguousSet
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
name|headSet
argument_list|(
name|checkNotNull
argument_list|(
name|toElement
argument_list|)
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|headSet (C toElement, boolean inclusive)
annotation|@
name|Override
name|ContiguousSet
argument_list|<
name|C
argument_list|>
name|headSet
parameter_list|(
name|C
name|toElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
return|return
name|headSetImpl
argument_list|(
name|checkNotNull
argument_list|(
name|toElement
argument_list|)
argument_list|,
name|inclusive
argument_list|)
return|;
block|}
DECL|method|subSet (C fromElement, C toElement)
annotation|@
name|Override
specifier|public
name|ContiguousSet
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
DECL|method|subSet (C fromElement, boolean fromInclusive, C toElement, boolean toInclusive)
annotation|@
name|Override
name|ContiguousSet
argument_list|<
name|C
argument_list|>
name|subSet
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
name|fromInclusive
argument_list|,
name|toElement
argument_list|,
name|toInclusive
argument_list|)
return|;
block|}
DECL|method|tailSet (C fromElement)
annotation|@
name|Override
specifier|public
name|ContiguousSet
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
name|tailSet
argument_list|(
name|checkNotNull
argument_list|(
name|fromElement
argument_list|)
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|tailSet (C fromElement, boolean inclusive)
annotation|@
name|Override
name|ContiguousSet
argument_list|<
name|C
argument_list|>
name|tailSet
parameter_list|(
name|C
name|fromElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
return|return
name|tailSetImpl
argument_list|(
name|checkNotNull
argument_list|(
name|fromElement
argument_list|)
argument_list|,
name|inclusive
argument_list|)
return|;
block|}
comment|/*    * These methods perform most headSet, subSet, and tailSet logic, besides parameter validation.    */
DECL|method|headSetImpl (C toElement, boolean inclusive)
comment|/*@Override*/
specifier|abstract
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
function_decl|;
DECL|method|subSetImpl (C fromElement, boolean fromInclusive, C toElement, boolean toInclusive)
comment|/*@Override*/
specifier|abstract
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
function_decl|;
DECL|method|tailSetImpl (C fromElement, boolean inclusive)
comment|/*@Override*/
specifier|abstract
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
function_decl|;
comment|/**    * Returns the set of values that are contained in both this set and the other.    *    *<p>This method should always be used instead of    * {@link Sets#intersection} for {@link ContiguousSet} instances.    */
DECL|method|intersection (ContiguousSet<C> other)
specifier|public
specifier|abstract
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
function_decl|;
comment|/**    * Returns a range, closed on both ends, whose endpoints are the minimum and maximum values    * contained in this set.  This is equivalent to {@code range(CLOSED, CLOSED)}.    *    * @throws NoSuchElementException if this set is empty    */
DECL|method|range ()
specifier|public
specifier|abstract
name|Range
argument_list|<
name|C
argument_list|>
name|range
parameter_list|()
function_decl|;
comment|/**    * Returns the minimal range with the given boundary types for which all values in this set are    * {@linkplain Range#contains(Comparable) contained} within the range.    *    *<p>Note that this method will return ranges with unbounded endpoints if {@link BoundType#OPEN}    * is requested for a domain minimum or maximum.  For example, if {@code set} was created from the    * range {@code [1â¥Integer.MAX_VALUE]} then {@code set.range(CLOSED, OPEN)} must return    * {@code [1â¥â)}.    *    * @throws NoSuchElementException if this set is empty    */
DECL|method|range (BoundType lowerBoundType, BoundType upperBoundType)
specifier|public
specifier|abstract
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
function_decl|;
comment|/** Returns a short-hand representation of the contents such as {@code "[1â¥100]"}. */
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|range
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

