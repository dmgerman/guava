begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *   * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software distributed under the  * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  * express or implied. See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|OPEN
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
name|base
operator|.
name|Objects
import|;
end_import

begin_comment
comment|/**  * A generalized interval on any ordering, for internal use. Supports {@code null}. Unlike  * {@link Range}, this allows the use of an arbitrary comparator. This is designed for use in the  * implementation of subcollections of sorted collection types.  *   *<p>Whenever possible, use {@code Range} instead, which is better supported.  *   * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|class|GeneralRange
specifier|final
class|class
name|GeneralRange
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Serializable
block|{
comment|/**    * Converts a Range to a GeneralRange.    */
DECL|method|from (Range<T> range)
specifier|static
parameter_list|<
name|T
extends|extends
name|Comparable
parameter_list|>
name|GeneralRange
argument_list|<
name|T
argument_list|>
name|from
parameter_list|(
name|Range
argument_list|<
name|T
argument_list|>
name|range
parameter_list|)
block|{
annotation|@
name|Nullable
name|T
name|lowerEndpoint
init|=
name|range
operator|.
name|hasLowerBound
argument_list|()
condition|?
name|range
operator|.
name|lowerEndpoint
argument_list|()
else|:
literal|null
decl_stmt|;
name|BoundType
name|lowerBoundType
init|=
name|range
operator|.
name|hasLowerBound
argument_list|()
condition|?
name|range
operator|.
name|lowerBoundType
argument_list|()
else|:
name|OPEN
decl_stmt|;
annotation|@
name|Nullable
name|T
name|upperEndpoint
init|=
name|range
operator|.
name|hasUpperBound
argument_list|()
condition|?
name|range
operator|.
name|upperEndpoint
argument_list|()
else|:
literal|null
decl_stmt|;
name|BoundType
name|upperBoundType
init|=
name|range
operator|.
name|hasUpperBound
argument_list|()
condition|?
name|range
operator|.
name|upperBoundType
argument_list|()
else|:
name|OPEN
decl_stmt|;
return|return
operator|new
name|GeneralRange
argument_list|<
name|T
argument_list|>
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
name|range
operator|.
name|hasLowerBound
argument_list|()
argument_list|,
name|lowerEndpoint
argument_list|,
name|lowerBoundType
argument_list|,
name|range
operator|.
name|hasUpperBound
argument_list|()
argument_list|,
name|upperEndpoint
argument_list|,
name|upperBoundType
argument_list|)
return|;
block|}
comment|/**    * Returns the whole range relative to the specified comparator.    */
DECL|method|all (Comparator<? super T> comparator)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|GeneralRange
argument_list|<
name|T
argument_list|>
name|all
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|T
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
operator|new
name|GeneralRange
argument_list|<
name|T
argument_list|>
argument_list|(
name|comparator
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
name|OPEN
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
name|OPEN
argument_list|)
return|;
block|}
comment|/**    * Returns everything above the endpoint relative to the specified comparator, with the specified    * endpoint behavior.    */
DECL|method|downTo (Comparator<? super T> comparator, @Nullable T endpoint, BoundType boundType)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|GeneralRange
argument_list|<
name|T
argument_list|>
name|downTo
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|T
argument_list|>
name|comparator
parameter_list|,
annotation|@
name|Nullable
name|T
name|endpoint
parameter_list|,
name|BoundType
name|boundType
parameter_list|)
block|{
return|return
operator|new
name|GeneralRange
argument_list|<
name|T
argument_list|>
argument_list|(
name|comparator
argument_list|,
literal|true
argument_list|,
name|endpoint
argument_list|,
name|boundType
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
name|OPEN
argument_list|)
return|;
block|}
comment|/**    * Returns everything below the endpoint relative to the specified comparator, with the specified    * endpoint behavior.    */
DECL|method|upTo (Comparator<? super T> comparator, @Nullable T endpoint, BoundType boundType)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|GeneralRange
argument_list|<
name|T
argument_list|>
name|upTo
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|T
argument_list|>
name|comparator
parameter_list|,
annotation|@
name|Nullable
name|T
name|endpoint
parameter_list|,
name|BoundType
name|boundType
parameter_list|)
block|{
return|return
operator|new
name|GeneralRange
argument_list|<
name|T
argument_list|>
argument_list|(
name|comparator
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
name|OPEN
argument_list|,
literal|true
argument_list|,
name|endpoint
argument_list|,
name|boundType
argument_list|)
return|;
block|}
comment|/**    * Returns everything between the endpoints relative to the specified comparator, with the    * specified endpoint behavior.    */
DECL|method|range (Comparator<? super T> comparator, @Nullable T lower, BoundType lowerType, @Nullable T upper, BoundType upperType)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|GeneralRange
argument_list|<
name|T
argument_list|>
name|range
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|T
argument_list|>
name|comparator
parameter_list|,
annotation|@
name|Nullable
name|T
name|lower
parameter_list|,
name|BoundType
name|lowerType
parameter_list|,
annotation|@
name|Nullable
name|T
name|upper
parameter_list|,
name|BoundType
name|upperType
parameter_list|)
block|{
return|return
operator|new
name|GeneralRange
argument_list|<
name|T
argument_list|>
argument_list|(
name|comparator
argument_list|,
literal|true
argument_list|,
name|lower
argument_list|,
name|lowerType
argument_list|,
literal|true
argument_list|,
name|upper
argument_list|,
name|upperType
argument_list|)
return|;
block|}
DECL|field|comparator
specifier|private
specifier|final
name|Comparator
argument_list|<
name|?
super|super
name|T
argument_list|>
name|comparator
decl_stmt|;
DECL|field|hasLowerBound
specifier|private
specifier|final
name|boolean
name|hasLowerBound
decl_stmt|;
annotation|@
name|Nullable
DECL|field|lowerEndpoint
specifier|private
specifier|final
name|T
name|lowerEndpoint
decl_stmt|;
DECL|field|lowerBoundType
specifier|private
specifier|final
name|BoundType
name|lowerBoundType
decl_stmt|;
DECL|field|hasUpperBound
specifier|private
specifier|final
name|boolean
name|hasUpperBound
decl_stmt|;
annotation|@
name|Nullable
DECL|field|upperEndpoint
specifier|private
specifier|final
name|T
name|upperEndpoint
decl_stmt|;
DECL|field|upperBoundType
specifier|private
specifier|final
name|BoundType
name|upperBoundType
decl_stmt|;
DECL|method|GeneralRange (Comparator<? super T> comparator, boolean hasLowerBound, @Nullable T lowerEndpoint, BoundType lowerBoundType, boolean hasUpperBound, @Nullable T upperEndpoint, BoundType upperBoundType)
specifier|private
name|GeneralRange
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|T
argument_list|>
name|comparator
parameter_list|,
name|boolean
name|hasLowerBound
parameter_list|,
annotation|@
name|Nullable
name|T
name|lowerEndpoint
parameter_list|,
name|BoundType
name|lowerBoundType
parameter_list|,
name|boolean
name|hasUpperBound
parameter_list|,
annotation|@
name|Nullable
name|T
name|upperEndpoint
parameter_list|,
name|BoundType
name|upperBoundType
parameter_list|)
block|{
name|this
operator|.
name|comparator
operator|=
name|checkNotNull
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
name|this
operator|.
name|hasLowerBound
operator|=
name|hasLowerBound
expr_stmt|;
name|this
operator|.
name|hasUpperBound
operator|=
name|hasUpperBound
expr_stmt|;
name|this
operator|.
name|lowerEndpoint
operator|=
name|lowerEndpoint
expr_stmt|;
name|this
operator|.
name|lowerBoundType
operator|=
name|checkNotNull
argument_list|(
name|lowerBoundType
argument_list|)
expr_stmt|;
name|this
operator|.
name|upperEndpoint
operator|=
name|upperEndpoint
expr_stmt|;
name|this
operator|.
name|upperBoundType
operator|=
name|checkNotNull
argument_list|(
name|upperBoundType
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasLowerBound
condition|)
block|{
name|comparator
operator|.
name|compare
argument_list|(
name|lowerEndpoint
argument_list|,
name|lowerEndpoint
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|hasUpperBound
condition|)
block|{
name|comparator
operator|.
name|compare
argument_list|(
name|upperEndpoint
argument_list|,
name|upperEndpoint
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|hasLowerBound
operator|&&
name|hasUpperBound
condition|)
block|{
name|int
name|cmp
init|=
name|comparator
operator|.
name|compare
argument_list|(
name|lowerEndpoint
argument_list|,
name|upperEndpoint
argument_list|)
decl_stmt|;
comment|// be consistent with Range
name|checkArgument
argument_list|(
name|cmp
operator|<=
literal|0
argument_list|,
literal|"lowerEndpoint (%s)> upperEndpoint (%s)"
argument_list|,
name|lowerEndpoint
argument_list|,
name|upperEndpoint
argument_list|)
expr_stmt|;
if|if
condition|(
name|cmp
operator|==
literal|0
condition|)
block|{
name|checkArgument
argument_list|(
name|lowerBoundType
operator|!=
name|OPEN
operator||
name|upperBoundType
operator|!=
name|OPEN
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|comparator ()
name|Comparator
argument_list|<
name|?
super|super
name|T
argument_list|>
name|comparator
parameter_list|()
block|{
return|return
name|comparator
return|;
block|}
DECL|method|hasLowerBound ()
name|boolean
name|hasLowerBound
parameter_list|()
block|{
return|return
name|hasLowerBound
return|;
block|}
DECL|method|hasUpperBound ()
name|boolean
name|hasUpperBound
parameter_list|()
block|{
return|return
name|hasUpperBound
return|;
block|}
DECL|method|isEmpty ()
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
operator|(
name|hasUpperBound
argument_list|()
operator|&&
name|tooLow
argument_list|(
name|upperEndpoint
argument_list|)
operator|)
operator|||
operator|(
name|hasLowerBound
argument_list|()
operator|&&
name|tooHigh
argument_list|(
name|lowerEndpoint
argument_list|)
operator|)
return|;
block|}
DECL|method|tooLow (@ullable T t)
name|boolean
name|tooLow
parameter_list|(
annotation|@
name|Nullable
name|T
name|t
parameter_list|)
block|{
if|if
condition|(
operator|!
name|hasLowerBound
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|T
name|lbound
init|=
name|lowerEndpoint
decl_stmt|;
name|int
name|cmp
init|=
name|comparator
operator|.
name|compare
argument_list|(
name|t
argument_list|,
name|lbound
argument_list|)
decl_stmt|;
return|return
name|cmp
operator|<
literal|0
operator||
operator|(
name|cmp
operator|==
literal|0
operator|&
name|lowerBoundType
operator|==
name|OPEN
operator|)
return|;
block|}
DECL|method|tooHigh (@ullable T t)
name|boolean
name|tooHigh
parameter_list|(
annotation|@
name|Nullable
name|T
name|t
parameter_list|)
block|{
if|if
condition|(
operator|!
name|hasUpperBound
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|T
name|ubound
init|=
name|upperEndpoint
decl_stmt|;
name|int
name|cmp
init|=
name|comparator
operator|.
name|compare
argument_list|(
name|t
argument_list|,
name|ubound
argument_list|)
decl_stmt|;
return|return
name|cmp
operator|>
literal|0
operator||
operator|(
name|cmp
operator|==
literal|0
operator|&
name|upperBoundType
operator|==
name|OPEN
operator|)
return|;
block|}
DECL|method|contains (@ullable T t)
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|T
name|t
parameter_list|)
block|{
return|return
operator|!
name|tooLow
argument_list|(
name|t
argument_list|)
operator|&&
operator|!
name|tooHigh
argument_list|(
name|t
argument_list|)
return|;
block|}
comment|/**    * Returns the intersection of the two ranges, or an empty range if their intersection is empty.    */
DECL|method|intersect (GeneralRange<T> other)
name|GeneralRange
argument_list|<
name|T
argument_list|>
name|intersect
parameter_list|(
name|GeneralRange
argument_list|<
name|T
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
name|comparator
operator|.
name|equals
argument_list|(
name|other
operator|.
name|comparator
argument_list|)
argument_list|)
expr_stmt|;
name|boolean
name|hasLowBound
init|=
name|this
operator|.
name|hasLowerBound
decl_stmt|;
annotation|@
name|Nullable
name|T
name|lowEnd
init|=
name|lowerEndpoint
decl_stmt|;
name|BoundType
name|lowType
init|=
name|lowerBoundType
decl_stmt|;
if|if
condition|(
operator|!
name|hasLowerBound
argument_list|()
condition|)
block|{
name|hasLowBound
operator|=
name|other
operator|.
name|hasLowerBound
expr_stmt|;
name|lowEnd
operator|=
name|other
operator|.
name|lowerEndpoint
expr_stmt|;
name|lowType
operator|=
name|other
operator|.
name|lowerBoundType
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|other
operator|.
name|hasLowerBound
argument_list|()
condition|)
block|{
name|int
name|cmp
init|=
name|comparator
operator|.
name|compare
argument_list|(
name|lowerEndpoint
argument_list|,
name|other
operator|.
name|lowerEndpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmp
operator|<
literal|0
operator|||
operator|(
name|cmp
operator|==
literal|0
operator|&&
name|other
operator|.
name|lowerBoundType
operator|==
name|OPEN
operator|)
condition|)
block|{
name|lowEnd
operator|=
name|other
operator|.
name|lowerEndpoint
expr_stmt|;
name|lowType
operator|=
name|other
operator|.
name|lowerBoundType
expr_stmt|;
block|}
block|}
name|boolean
name|hasUpBound
init|=
name|this
operator|.
name|hasUpperBound
decl_stmt|;
annotation|@
name|Nullable
name|T
name|upEnd
init|=
name|upperEndpoint
decl_stmt|;
name|BoundType
name|upType
init|=
name|upperBoundType
decl_stmt|;
if|if
condition|(
operator|!
name|hasUpperBound
argument_list|()
condition|)
block|{
name|hasUpBound
operator|=
name|other
operator|.
name|hasUpperBound
expr_stmt|;
name|upEnd
operator|=
name|other
operator|.
name|upperEndpoint
expr_stmt|;
name|upType
operator|=
name|other
operator|.
name|upperBoundType
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|other
operator|.
name|hasUpperBound
argument_list|()
condition|)
block|{
name|int
name|cmp
init|=
name|comparator
operator|.
name|compare
argument_list|(
name|upperEndpoint
argument_list|,
name|other
operator|.
name|upperEndpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmp
operator|>
literal|0
operator|||
operator|(
name|cmp
operator|==
literal|0
operator|&&
name|other
operator|.
name|upperBoundType
operator|==
name|OPEN
operator|)
condition|)
block|{
name|upEnd
operator|=
name|other
operator|.
name|upperEndpoint
expr_stmt|;
name|upType
operator|=
name|other
operator|.
name|upperBoundType
expr_stmt|;
block|}
block|}
if|if
condition|(
name|hasLowBound
operator|&&
name|hasUpBound
condition|)
block|{
name|int
name|cmp
init|=
name|comparator
operator|.
name|compare
argument_list|(
name|lowEnd
argument_list|,
name|upEnd
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmp
operator|>
literal|0
operator|||
operator|(
name|cmp
operator|==
literal|0
operator|&&
name|lowType
operator|==
name|OPEN
operator|&&
name|upType
operator|==
name|OPEN
operator|)
condition|)
block|{
comment|// force allowed empty range
name|lowEnd
operator|=
name|upEnd
expr_stmt|;
name|lowType
operator|=
name|OPEN
expr_stmt|;
name|upType
operator|=
name|CLOSED
expr_stmt|;
block|}
block|}
return|return
operator|new
name|GeneralRange
argument_list|<
name|T
argument_list|>
argument_list|(
name|comparator
argument_list|,
name|hasLowBound
argument_list|,
name|lowEnd
argument_list|,
name|lowType
argument_list|,
name|hasUpBound
argument_list|,
name|upEnd
argument_list|,
name|upType
argument_list|)
return|;
block|}
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
name|GeneralRange
condition|)
block|{
name|GeneralRange
argument_list|<
name|?
argument_list|>
name|r
init|=
operator|(
name|GeneralRange
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|comparator
operator|.
name|equals
argument_list|(
name|r
operator|.
name|comparator
argument_list|)
operator|&&
name|hasLowerBound
operator|==
name|r
operator|.
name|hasLowerBound
operator|&&
name|hasUpperBound
operator|==
name|r
operator|.
name|hasUpperBound
operator|&&
name|lowerBoundType
operator|.
name|equals
argument_list|(
name|r
operator|.
name|lowerBoundType
argument_list|)
operator|&&
name|upperBoundType
operator|.
name|equals
argument_list|(
name|r
operator|.
name|upperBoundType
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|lowerEndpoint
argument_list|,
name|r
operator|.
name|lowerEndpoint
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|upperEndpoint
argument_list|,
name|r
operator|.
name|upperEndpoint
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
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hashCode
argument_list|(
name|comparator
argument_list|,
name|lowerEndpoint
argument_list|,
name|lowerBoundType
argument_list|,
name|upperEndpoint
argument_list|,
name|upperBoundType
argument_list|)
return|;
block|}
DECL|field|reverse
specifier|private
specifier|transient
name|GeneralRange
argument_list|<
name|T
argument_list|>
name|reverse
decl_stmt|;
comment|/**    * Returns the same range relative to the reversed comparator.    */
DECL|method|reverse ()
specifier|public
name|GeneralRange
argument_list|<
name|T
argument_list|>
name|reverse
parameter_list|()
block|{
name|GeneralRange
argument_list|<
name|T
argument_list|>
name|result
init|=
name|reverse
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
operator|new
name|GeneralRange
argument_list|<
name|T
argument_list|>
argument_list|(
name|Ordering
operator|.
name|from
argument_list|(
name|comparator
argument_list|)
operator|.
name|reverse
argument_list|()
argument_list|,
name|hasUpperBound
argument_list|,
name|upperEndpoint
argument_list|,
name|upperBoundType
argument_list|,
name|hasLowerBound
argument_list|,
name|lowerEndpoint
argument_list|,
name|lowerBoundType
argument_list|)
expr_stmt|;
name|result
operator|.
name|reverse
operator|=
name|this
expr_stmt|;
return|return
name|this
operator|.
name|reverse
operator|=
name|result
return|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
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
name|comparator
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|lowerBoundType
condition|)
block|{
case|case
name|CLOSED
case|:
name|builder
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
expr_stmt|;
break|break;
case|case
name|OPEN
case|:
name|builder
operator|.
name|append
argument_list|(
literal|'('
argument_list|)
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|hasLowerBound
argument_list|()
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|lowerEndpoint
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"-\u221e"
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasUpperBound
argument_list|()
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|upperEndpoint
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"\u221e"
argument_list|)
expr_stmt|;
block|}
switch|switch
condition|(
name|upperBoundType
condition|)
block|{
case|case
name|CLOSED
case|:
name|builder
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
break|break;
case|case
name|OPEN
case|:
name|builder
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
break|break;
block|}
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

