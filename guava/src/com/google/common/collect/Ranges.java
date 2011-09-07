begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Static methods pertaining to {@link Range} instances.  Each of the  * {@link Range nine types of ranges} can be constructed with a corresponding  * factory method:  *  *<dl>  *<dt>{@code (a..b)}  *<dd>{@link #open}  *<dt>{@code [a..b]}  *<dd>{@link #closed}  *<dt>{@code [a..b)}  *<dd>{@link #closedOpen}  *<dt>{@code (a..b]}  *<dd>{@link #openClosed}  *<dt>{@code (a..+â)}  *<dd>{@link #greaterThan}  *<dt>{@code [a..+â)}  *<dd>{@link #atLeast}  *<dt>{@code (-â..b)}  *<dd>{@link #lessThan}  *<dt>{@code (-â..b]}  *<dd>{@link #atMost}  *<dt>{@code (-â..+â)}  *<dd>{@link #all}  *</dl>  *  *<p>Additionally, {@link Range} instances can be constructed by passing the  * {@link BoundType bound types} explicitly.  *  *<dl>  *<dt>Bounded on both ends  *<dd>{@link #range}  *<dt>Unbounded on top ({@code (a..+â)} or {@code (a..+â)})  *<dd>{@link #downTo}  *<dt>Unbounded on bottom ({@code (-â..b)} or {@code (-â..b]})  *<dd>{@link #upTo}  *</dl>  *  * @author Kevin Bourrillion  * @author Gregory Kick  * @since 10.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|Beta
DECL|class|Ranges
specifier|public
specifier|final
class|class
name|Ranges
block|{
DECL|method|Ranges ()
specifier|private
name|Ranges
parameter_list|()
block|{}
DECL|method|create ( Cut<C> lowerBound, Cut<C> upperBound)
specifier|static
parameter_list|<
name|C
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|>
name|Range
argument_list|<
name|C
argument_list|>
name|create
parameter_list|(
name|Cut
argument_list|<
name|C
argument_list|>
name|lowerBound
parameter_list|,
name|Cut
argument_list|<
name|C
argument_list|>
name|upperBound
parameter_list|)
block|{
return|return
operator|new
name|Range
argument_list|<
name|C
argument_list|>
argument_list|(
name|lowerBound
argument_list|,
name|upperBound
argument_list|)
return|;
block|}
comment|/**    * Returns a range that contains all values strictly greater than {@code    * lower} and strictly less than {@code upper}.    *    * @throws IllegalArgumentException if {@code lower} is greater than<i>or    *     equal to</i> {@code upper}    */
DECL|method|open (C lower, C upper)
specifier|public
specifier|static
parameter_list|<
name|C
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|>
name|Range
argument_list|<
name|C
argument_list|>
name|open
parameter_list|(
name|C
name|lower
parameter_list|,
name|C
name|upper
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|Cut
operator|.
name|aboveValue
argument_list|(
name|lower
argument_list|)
argument_list|,
name|Cut
operator|.
name|belowValue
argument_list|(
name|upper
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a range that contains all values greater than or equal to    * {@code lower} and less than or equal to {@code upper}.    *    * @throws IllegalArgumentException if {@code lower} is greater than {@code    *     upper}    */
DECL|method|closed (C lower, C upper)
specifier|public
specifier|static
parameter_list|<
name|C
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|>
name|Range
argument_list|<
name|C
argument_list|>
name|closed
parameter_list|(
name|C
name|lower
parameter_list|,
name|C
name|upper
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|Cut
operator|.
name|belowValue
argument_list|(
name|lower
argument_list|)
argument_list|,
name|Cut
operator|.
name|aboveValue
argument_list|(
name|upper
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a range that contains all values greater than or equal to    * {@code lower} and strictly less than {@code upper}.    *    * @throws IllegalArgumentException if {@code lower} is greater than {@code    *     upper}    */
DECL|method|closedOpen ( C lower, C upper)
specifier|public
specifier|static
parameter_list|<
name|C
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|>
name|Range
argument_list|<
name|C
argument_list|>
name|closedOpen
parameter_list|(
name|C
name|lower
parameter_list|,
name|C
name|upper
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|Cut
operator|.
name|belowValue
argument_list|(
name|lower
argument_list|)
argument_list|,
name|Cut
operator|.
name|belowValue
argument_list|(
name|upper
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a range that contains all values strictly greater than {@code    * lower} and less than or equal to {@code upper}.    *    * @throws IllegalArgumentException if {@code lower} is greater than {@code    *     upper}    */
DECL|method|openClosed ( C lower, C upper)
specifier|public
specifier|static
parameter_list|<
name|C
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|>
name|Range
argument_list|<
name|C
argument_list|>
name|openClosed
parameter_list|(
name|C
name|lower
parameter_list|,
name|C
name|upper
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|Cut
operator|.
name|aboveValue
argument_list|(
name|lower
argument_list|)
argument_list|,
name|Cut
operator|.
name|aboveValue
argument_list|(
name|upper
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a range that contains any value from {@code lower} to {@code    * upper}, where each endpoint may be either inclusive (closed) or exclusive    * (open).    *    * @throws IllegalArgumentException if {@code lower} is greater than {@code    *     upper}    */
DECL|method|range ( C lower, BoundType lowerType, C upper, BoundType upperType)
specifier|public
specifier|static
parameter_list|<
name|C
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|>
name|Range
argument_list|<
name|C
argument_list|>
name|range
parameter_list|(
name|C
name|lower
parameter_list|,
name|BoundType
name|lowerType
parameter_list|,
name|C
name|upper
parameter_list|,
name|BoundType
name|upperType
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|lowerType
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|upperType
argument_list|)
expr_stmt|;
name|Cut
argument_list|<
name|C
argument_list|>
name|lowerBound
init|=
operator|(
name|lowerType
operator|==
name|BoundType
operator|.
name|OPEN
operator|)
condition|?
name|Cut
operator|.
name|aboveValue
argument_list|(
name|lower
argument_list|)
else|:
name|Cut
operator|.
name|belowValue
argument_list|(
name|lower
argument_list|)
decl_stmt|;
name|Cut
argument_list|<
name|C
argument_list|>
name|upperBound
init|=
operator|(
name|upperType
operator|==
name|BoundType
operator|.
name|OPEN
operator|)
condition|?
name|Cut
operator|.
name|belowValue
argument_list|(
name|upper
argument_list|)
else|:
name|Cut
operator|.
name|aboveValue
argument_list|(
name|upper
argument_list|)
decl_stmt|;
return|return
name|create
argument_list|(
name|lowerBound
argument_list|,
name|upperBound
argument_list|)
return|;
block|}
comment|/**    * Returns a range that contains all values strictly less than {@code    * endpoint}.    */
DECL|method|lessThan (C endpoint)
specifier|public
specifier|static
parameter_list|<
name|C
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|>
name|Range
argument_list|<
name|C
argument_list|>
name|lessThan
parameter_list|(
name|C
name|endpoint
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|Cut
operator|.
expr|<
name|C
operator|>
name|belowAll
argument_list|()
argument_list|,
name|Cut
operator|.
name|belowValue
argument_list|(
name|endpoint
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a range that contains all values less than or equal to    * {@code endpoint}.    */
DECL|method|atMost (C endpoint)
specifier|public
specifier|static
parameter_list|<
name|C
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|>
name|Range
argument_list|<
name|C
argument_list|>
name|atMost
parameter_list|(
name|C
name|endpoint
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|Cut
operator|.
expr|<
name|C
operator|>
name|belowAll
argument_list|()
argument_list|,
name|Cut
operator|.
name|aboveValue
argument_list|(
name|endpoint
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a range with no lower bound up to the given endpoint, which may be    * either inclusive (closed) or exclusive (open).    */
DECL|method|upTo ( C endpoint, BoundType boundType)
specifier|public
specifier|static
parameter_list|<
name|C
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|>
name|Range
argument_list|<
name|C
argument_list|>
name|upTo
parameter_list|(
name|C
name|endpoint
parameter_list|,
name|BoundType
name|boundType
parameter_list|)
block|{
switch|switch
condition|(
name|boundType
condition|)
block|{
case|case
name|OPEN
case|:
return|return
name|lessThan
argument_list|(
name|endpoint
argument_list|)
return|;
case|case
name|CLOSED
case|:
return|return
name|atMost
argument_list|(
name|endpoint
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
comment|/**    * Returns a range that contains all values strictly greater than {@code    * endpoint}.    */
DECL|method|greaterThan (C endpoint)
specifier|public
specifier|static
parameter_list|<
name|C
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|>
name|Range
argument_list|<
name|C
argument_list|>
name|greaterThan
parameter_list|(
name|C
name|endpoint
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|Cut
operator|.
name|aboveValue
argument_list|(
name|endpoint
argument_list|)
argument_list|,
name|Cut
operator|.
expr|<
name|C
operator|>
name|aboveAll
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns a range that contains all values greater than or equal to    * {@code endpoint}.    */
DECL|method|atLeast (C endpoint)
specifier|public
specifier|static
parameter_list|<
name|C
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|>
name|Range
argument_list|<
name|C
argument_list|>
name|atLeast
parameter_list|(
name|C
name|endpoint
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|Cut
operator|.
name|belowValue
argument_list|(
name|endpoint
argument_list|)
argument_list|,
name|Cut
operator|.
expr|<
name|C
operator|>
name|aboveAll
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns a range from the given endpoint, which may be either inclusive    * (closed) or exclusive (open), with no upper bound.    */
DECL|method|downTo ( C endpoint, BoundType boundType)
specifier|public
specifier|static
parameter_list|<
name|C
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|>
name|Range
argument_list|<
name|C
argument_list|>
name|downTo
parameter_list|(
name|C
name|endpoint
parameter_list|,
name|BoundType
name|boundType
parameter_list|)
block|{
switch|switch
condition|(
name|boundType
condition|)
block|{
case|case
name|OPEN
case|:
return|return
name|greaterThan
argument_list|(
name|endpoint
argument_list|)
return|;
case|case
name|CLOSED
case|:
return|return
name|atLeast
argument_list|(
name|endpoint
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
comment|/** Returns a range that contains every value of type {@code C}. */
DECL|method|all ()
specifier|public
specifier|static
parameter_list|<
name|C
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|>
name|Range
argument_list|<
name|C
argument_list|>
name|all
parameter_list|()
block|{
return|return
name|create
argument_list|(
name|Cut
operator|.
expr|<
name|C
operator|>
name|belowAll
argument_list|()
argument_list|,
name|Cut
operator|.
expr|<
name|C
operator|>
name|aboveAll
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns a range that {@linkplain Range#contains(Comparable) contains} only    * the given value. The returned range is {@linkplain BoundType#CLOSED closed}    * on both ends.    */
DECL|method|singleton (C value)
specifier|public
specifier|static
parameter_list|<
name|C
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|>
name|Range
argument_list|<
name|C
argument_list|>
name|singleton
parameter_list|(
name|C
name|value
parameter_list|)
block|{
return|return
name|closed
argument_list|(
name|value
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**    * Returns the minimal range that    * {@linkplain Range#contains(Comparable) contains} all of the given values.    * The returned range is {@linkplain BoundType#CLOSED closed} on both ends.    *    * @throws ClassCastException if the parameters are not<i>mutually    *     comparable</i>    * @throws NoSuchElementException if {@code values} is empty    * @throws NullPointerException if any of {@code values} is null    */
DECL|method|encloseAll ( Iterable<C> values)
specifier|public
specifier|static
parameter_list|<
name|C
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|>
name|Range
argument_list|<
name|C
argument_list|>
name|encloseAll
parameter_list|(
name|Iterable
argument_list|<
name|C
argument_list|>
name|values
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|values
argument_list|)
expr_stmt|;
if|if
condition|(
name|values
operator|instanceof
name|ContiguousSet
condition|)
block|{
return|return
operator|(
operator|(
name|ContiguousSet
argument_list|<
name|C
argument_list|>
operator|)
name|values
operator|)
operator|.
name|range
argument_list|()
return|;
block|}
name|Iterator
argument_list|<
name|C
argument_list|>
name|valueIterator
init|=
name|values
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|C
name|min
init|=
name|checkNotNull
argument_list|(
name|valueIterator
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|C
name|max
init|=
name|min
decl_stmt|;
while|while
condition|(
name|valueIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|C
name|value
init|=
name|checkNotNull
argument_list|(
name|valueIterator
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|min
operator|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|min
argument_list|(
name|min
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|max
operator|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|max
argument_list|(
name|max
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|closed
argument_list|(
name|min
argument_list|,
name|max
argument_list|)
return|;
block|}
block|}
end_class

end_unit

