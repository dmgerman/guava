begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|checkElementIndex
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
name|collect
operator|.
name|SortedLists
operator|.
name|KeyAbsentBehavior
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
name|SortedLists
operator|.
name|KeyPresentBehavior
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A {@link RangeMap} whose contents will never change, with many other important properties  * detailed at {@link ImmutableCollection}.  *  * @author Louis Wasserman  * @since 14.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
comment|// NavigableMap
DECL|class|ImmutableRangeMap
specifier|public
class|class
name|ImmutableRangeMap
parameter_list|<
name|K
extends|extends
name|Comparable
parameter_list|<
name|?
parameter_list|>
parameter_list|,
name|V
parameter_list|>
implements|implements
name|RangeMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|EMPTY
specifier|private
specifier|static
specifier|final
name|ImmutableRangeMap
argument_list|<
name|Comparable
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|EMPTY
init|=
operator|new
name|ImmutableRangeMap
argument_list|<>
argument_list|(
name|ImmutableList
operator|.
expr|<
name|Range
argument_list|<
name|Comparable
argument_list|<
name|?
argument_list|>
argument_list|>
operator|>
name|of
argument_list|()
condition|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|)
decl_stmt|;
comment|/**    * Returns a {@code Collector} that accumulates the input elements into a new {@code    * ImmutableRangeMap}. As in {@link Builder}, overlapping ranges are not permitted.    *    * @since 23.1    */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|,
name|K
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
DECL|method|toImmutableRangeMap ( Function<? super T, Range<K>> keyFunction, Function<? super T, ? extends V> valueFunction)
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableRangeMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableRangeMap
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|Range
argument_list|<
name|K
argument_list|>
argument_list|>
name|keyFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
parameter_list|)
block|{
return|return
name|CollectCollectors
operator|.
name|toImmutableRangeMap
argument_list|(
name|keyFunction
argument_list|,
name|valueFunction
argument_list|)
return|;
block|}
comment|/** Returns an empty immutable range map. */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|of ()
specifier|public
specifier|static
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|ImmutableRangeMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|()
block|{
return|return
operator|(
name|ImmutableRangeMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|EMPTY
return|;
block|}
comment|/** Returns an immutable range map mapping a single range to a single value. */
DECL|method|of (Range<K> range, V value)
specifier|public
specifier|static
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|ImmutableRangeMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|(
name|Range
argument_list|<
name|K
argument_list|>
name|range
parameter_list|,
name|V
name|value
parameter_list|)
block|{
return|return
operator|new
name|ImmutableRangeMap
argument_list|<>
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|range
argument_list|)
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|copyOf ( RangeMap<K, ? extends V> rangeMap)
specifier|public
specifier|static
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|ImmutableRangeMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|copyOf
parameter_list|(
name|RangeMap
argument_list|<
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|rangeMap
parameter_list|)
block|{
if|if
condition|(
name|rangeMap
operator|instanceof
name|ImmutableRangeMap
condition|)
block|{
return|return
operator|(
name|ImmutableRangeMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|rangeMap
return|;
block|}
name|Map
argument_list|<
name|Range
argument_list|<
name|K
argument_list|>
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|map
init|=
name|rangeMap
operator|.
name|asMapOfRanges
argument_list|()
decl_stmt|;
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|Range
argument_list|<
name|K
argument_list|>
argument_list|>
name|rangesBuilder
init|=
operator|new
name|ImmutableList
operator|.
name|Builder
argument_list|<>
argument_list|(
name|map
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|V
argument_list|>
name|valuesBuilder
init|=
operator|new
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|V
argument_list|>
argument_list|(
name|map
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|Range
argument_list|<
name|K
argument_list|>
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|rangesBuilder
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|valuesBuilder
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ImmutableRangeMap
argument_list|<>
argument_list|(
name|rangesBuilder
operator|.
name|build
argument_list|()
argument_list|,
name|valuesBuilder
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
comment|/** Returns a new builder for an immutable range map. */
DECL|method|builder ()
specifier|public
specifier|static
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|?
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builder
parameter_list|()
block|{
return|return
operator|new
name|Builder
argument_list|<>
argument_list|()
return|;
block|}
comment|/**    * A builder for immutable range maps. Overlapping ranges are prohibited.    *    * @since 14.0    */
DECL|class|Builder
specifier|public
specifier|static
specifier|final
class|class
name|Builder
parameter_list|<
name|K
extends|extends
name|Comparable
parameter_list|<
name|?
parameter_list|>
parameter_list|,
name|V
parameter_list|>
block|{
DECL|field|entries
specifier|private
specifier|final
name|List
argument_list|<
name|Entry
argument_list|<
name|Range
argument_list|<
name|K
argument_list|>
argument_list|,
name|V
argument_list|>
argument_list|>
name|entries
decl_stmt|;
DECL|method|Builder ()
specifier|public
name|Builder
parameter_list|()
block|{
name|this
operator|.
name|entries
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|()
expr_stmt|;
block|}
comment|/**      * Associates the specified range with the specified value.      *      * @throws IllegalArgumentException if {@code range} is empty      */
annotation|@
name|CanIgnoreReturnValue
DECL|method|put (Range<K> range, V value)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|put
parameter_list|(
name|Range
argument_list|<
name|K
argument_list|>
name|range
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|range
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
operator|!
name|range
operator|.
name|isEmpty
argument_list|()
argument_list|,
literal|"Range must not be empty, but was %s"
argument_list|,
name|range
argument_list|)
expr_stmt|;
name|entries
operator|.
name|add
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|range
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** Copies all associations from the specified range map into this builder. */
annotation|@
name|CanIgnoreReturnValue
DECL|method|putAll (RangeMap<K, ? extends V> rangeMap)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|putAll
parameter_list|(
name|RangeMap
argument_list|<
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|rangeMap
parameter_list|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|Range
argument_list|<
name|K
argument_list|>
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|entry
range|:
name|rangeMap
operator|.
name|asMapOfRanges
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
DECL|method|combine (Builder<K, V> builder)
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|combine
parameter_list|(
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builder
parameter_list|)
block|{
name|entries
operator|.
name|addAll
argument_list|(
name|builder
operator|.
name|entries
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Returns an {@code ImmutableRangeMap} containing the associations previously added to this      * builder.      *      * @throws IllegalArgumentException if any two ranges inserted into this builder overlap      */
DECL|method|build ()
specifier|public
name|ImmutableRangeMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|entries
argument_list|,
name|Range
operator|.
expr|<
name|K
operator|>
name|rangeLexOrdering
argument_list|()
operator|.
name|onKeys
argument_list|()
argument_list|)
expr_stmt|;
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|Range
argument_list|<
name|K
argument_list|>
argument_list|>
name|rangesBuilder
init|=
operator|new
name|ImmutableList
operator|.
name|Builder
argument_list|<>
argument_list|(
name|entries
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|V
argument_list|>
name|valuesBuilder
init|=
operator|new
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|V
argument_list|>
argument_list|(
name|entries
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|entries
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Range
argument_list|<
name|K
argument_list|>
name|range
init|=
name|entries
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|Range
argument_list|<
name|K
argument_list|>
name|prevRange
init|=
name|entries
operator|.
name|get
argument_list|(
name|i
operator|-
literal|1
argument_list|)
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|range
operator|.
name|isConnected
argument_list|(
name|prevRange
argument_list|)
operator|&&
operator|!
name|range
operator|.
name|intersection
argument_list|(
name|prevRange
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Overlapping ranges: range "
operator|+
name|prevRange
operator|+
literal|" overlaps with entry "
operator|+
name|range
argument_list|)
throw|;
block|}
block|}
name|rangesBuilder
operator|.
name|add
argument_list|(
name|range
argument_list|)
expr_stmt|;
name|valuesBuilder
operator|.
name|add
argument_list|(
name|entries
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ImmutableRangeMap
argument_list|<>
argument_list|(
name|rangesBuilder
operator|.
name|build
argument_list|()
argument_list|,
name|valuesBuilder
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|field|ranges
specifier|private
specifier|final
specifier|transient
name|ImmutableList
argument_list|<
name|Range
argument_list|<
name|K
argument_list|>
argument_list|>
name|ranges
decl_stmt|;
DECL|field|values
specifier|private
specifier|final
specifier|transient
name|ImmutableList
argument_list|<
name|V
argument_list|>
name|values
decl_stmt|;
DECL|method|ImmutableRangeMap (ImmutableList<Range<K>> ranges, ImmutableList<V> values)
name|ImmutableRangeMap
parameter_list|(
name|ImmutableList
argument_list|<
name|Range
argument_list|<
name|K
argument_list|>
argument_list|>
name|ranges
parameter_list|,
name|ImmutableList
argument_list|<
name|V
argument_list|>
name|values
parameter_list|)
block|{
name|this
operator|.
name|ranges
operator|=
name|ranges
expr_stmt|;
name|this
operator|.
name|values
operator|=
name|values
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get (K key)
specifier|public
annotation|@
name|Nullable
name|V
name|get
parameter_list|(
name|K
name|key
parameter_list|)
block|{
name|int
name|index
init|=
name|SortedLists
operator|.
name|binarySearch
argument_list|(
name|ranges
argument_list|,
name|Range
operator|.
expr|<
name|K
operator|>
name|lowerBoundFn
argument_list|()
argument_list|,
name|Cut
operator|.
name|belowValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|KeyPresentBehavior
operator|.
name|ANY_PRESENT
argument_list|,
name|KeyAbsentBehavior
operator|.
name|NEXT_LOWER
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|==
operator|-
literal|1
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
name|Range
argument_list|<
name|K
argument_list|>
name|range
init|=
name|ranges
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
return|return
name|range
operator|.
name|contains
argument_list|(
name|key
argument_list|)
condition|?
name|values
operator|.
name|get
argument_list|(
name|index
argument_list|)
else|:
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getEntry (K key)
specifier|public
annotation|@
name|Nullable
name|Entry
argument_list|<
name|Range
argument_list|<
name|K
argument_list|>
argument_list|,
name|V
argument_list|>
name|getEntry
parameter_list|(
name|K
name|key
parameter_list|)
block|{
name|int
name|index
init|=
name|SortedLists
operator|.
name|binarySearch
argument_list|(
name|ranges
argument_list|,
name|Range
operator|.
expr|<
name|K
operator|>
name|lowerBoundFn
argument_list|()
argument_list|,
name|Cut
operator|.
name|belowValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|KeyPresentBehavior
operator|.
name|ANY_PRESENT
argument_list|,
name|KeyAbsentBehavior
operator|.
name|NEXT_LOWER
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|==
operator|-
literal|1
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
name|Range
argument_list|<
name|K
argument_list|>
name|range
init|=
name|ranges
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
return|return
name|range
operator|.
name|contains
argument_list|(
name|key
argument_list|)
condition|?
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|range
argument_list|,
name|values
operator|.
name|get
argument_list|(
name|index
argument_list|)
argument_list|)
else|:
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|span ()
specifier|public
name|Range
argument_list|<
name|K
argument_list|>
name|span
parameter_list|()
block|{
if|if
condition|(
name|ranges
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
name|Range
argument_list|<
name|K
argument_list|>
name|firstRange
init|=
name|ranges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Range
argument_list|<
name|K
argument_list|>
name|lastRange
init|=
name|ranges
operator|.
name|get
argument_list|(
name|ranges
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
return|return
name|Range
operator|.
name|create
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
comment|/**    * Guaranteed to throw an exception and leave the {@code RangeMap} unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|put (Range<K> range, V value)
specifier|public
name|void
name|put
parameter_list|(
name|Range
argument_list|<
name|K
argument_list|>
name|range
parameter_list|,
name|V
name|value
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the {@code RangeMap} unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|putCoalescing (Range<K> range, V value)
specifier|public
name|void
name|putCoalescing
parameter_list|(
name|Range
argument_list|<
name|K
argument_list|>
name|range
parameter_list|,
name|V
name|value
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the {@code RangeMap} unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|putAll (RangeMap<K, V> rangeMap)
specifier|public
name|void
name|putAll
parameter_list|(
name|RangeMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|rangeMap
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the {@code RangeMap} unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the {@code RangeMap} unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|remove (Range<K> range)
specifier|public
name|void
name|remove
parameter_list|(
name|Range
argument_list|<
name|K
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
DECL|method|asMapOfRanges ()
specifier|public
name|ImmutableMap
argument_list|<
name|Range
argument_list|<
name|K
argument_list|>
argument_list|,
name|V
argument_list|>
name|asMapOfRanges
parameter_list|()
block|{
if|if
condition|(
name|ranges
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|ImmutableMap
operator|.
name|of
argument_list|()
return|;
block|}
name|RegularImmutableSortedSet
argument_list|<
name|Range
argument_list|<
name|K
argument_list|>
argument_list|>
name|rangeSet
init|=
operator|new
name|RegularImmutableSortedSet
argument_list|<>
argument_list|(
name|ranges
argument_list|,
name|Range
operator|.
expr|<
name|K
operator|>
name|rangeLexOrdering
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|ImmutableSortedMap
argument_list|<>
argument_list|(
name|rangeSet
argument_list|,
name|values
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|asDescendingMapOfRanges ()
specifier|public
name|ImmutableMap
argument_list|<
name|Range
argument_list|<
name|K
argument_list|>
argument_list|,
name|V
argument_list|>
name|asDescendingMapOfRanges
parameter_list|()
block|{
if|if
condition|(
name|ranges
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|ImmutableMap
operator|.
name|of
argument_list|()
return|;
block|}
name|RegularImmutableSortedSet
argument_list|<
name|Range
argument_list|<
name|K
argument_list|>
argument_list|>
name|rangeSet
init|=
operator|new
name|RegularImmutableSortedSet
argument_list|<>
argument_list|(
name|ranges
operator|.
name|reverse
argument_list|()
argument_list|,
name|Range
operator|.
expr|<
name|K
operator|>
name|rangeLexOrdering
argument_list|()
operator|.
name|reverse
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|ImmutableSortedMap
argument_list|<>
argument_list|(
name|rangeSet
argument_list|,
name|values
operator|.
name|reverse
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|subRangeMap (final Range<K> range)
specifier|public
name|ImmutableRangeMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|subRangeMap
parameter_list|(
specifier|final
name|Range
argument_list|<
name|K
argument_list|>
name|range
parameter_list|)
block|{
if|if
condition|(
name|checkNotNull
argument_list|(
name|range
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|ImmutableRangeMap
operator|.
name|of
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ranges
operator|.
name|isEmpty
argument_list|()
operator|||
name|range
operator|.
name|encloses
argument_list|(
name|span
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|this
return|;
block|}
name|int
name|lowerIndex
init|=
name|SortedLists
operator|.
name|binarySearch
argument_list|(
name|ranges
argument_list|,
name|Range
operator|.
expr|<
name|K
operator|>
name|upperBoundFn
argument_list|()
argument_list|,
name|range
operator|.
name|lowerBound
argument_list|,
name|KeyPresentBehavior
operator|.
name|FIRST_AFTER
argument_list|,
name|KeyAbsentBehavior
operator|.
name|NEXT_HIGHER
argument_list|)
decl_stmt|;
name|int
name|upperIndex
init|=
name|SortedLists
operator|.
name|binarySearch
argument_list|(
name|ranges
argument_list|,
name|Range
operator|.
expr|<
name|K
operator|>
name|lowerBoundFn
argument_list|()
argument_list|,
name|range
operator|.
name|upperBound
argument_list|,
name|KeyPresentBehavior
operator|.
name|ANY_PRESENT
argument_list|,
name|KeyAbsentBehavior
operator|.
name|NEXT_HIGHER
argument_list|)
decl_stmt|;
if|if
condition|(
name|lowerIndex
operator|>=
name|upperIndex
condition|)
block|{
return|return
name|ImmutableRangeMap
operator|.
name|of
argument_list|()
return|;
block|}
specifier|final
name|int
name|off
init|=
name|lowerIndex
decl_stmt|;
specifier|final
name|int
name|len
init|=
name|upperIndex
operator|-
name|lowerIndex
decl_stmt|;
name|ImmutableList
argument_list|<
name|Range
argument_list|<
name|K
argument_list|>
argument_list|>
name|subRanges
init|=
operator|new
name|ImmutableList
argument_list|<
name|Range
argument_list|<
name|K
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|len
return|;
block|}
annotation|@
name|Override
specifier|public
name|Range
argument_list|<
name|K
argument_list|>
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|checkElementIndex
argument_list|(
name|index
argument_list|,
name|len
argument_list|)
expr_stmt|;
if|if
condition|(
name|index
operator|==
literal|0
operator|||
name|index
operator|==
name|len
operator|-
literal|1
condition|)
block|{
return|return
name|ranges
operator|.
name|get
argument_list|(
name|index
operator|+
name|off
argument_list|)
operator|.
name|intersection
argument_list|(
name|range
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|ranges
operator|.
name|get
argument_list|(
name|index
operator|+
name|off
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
specifier|final
name|ImmutableRangeMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|outer
init|=
name|this
decl_stmt|;
return|return
operator|new
name|ImmutableRangeMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|subRanges
argument_list|,
name|values
operator|.
name|subList
argument_list|(
name|lowerIndex
argument_list|,
name|upperIndex
argument_list|)
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|ImmutableRangeMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|subRangeMap
parameter_list|(
name|Range
argument_list|<
name|K
argument_list|>
name|subRange
parameter_list|)
block|{
if|if
condition|(
name|range
operator|.
name|isConnected
argument_list|(
name|subRange
argument_list|)
condition|)
block|{
return|return
name|outer
operator|.
name|subRangeMap
argument_list|(
name|subRange
operator|.
name|intersection
argument_list|(
name|range
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|ImmutableRangeMap
operator|.
name|of
argument_list|()
return|;
block|}
block|}
block|}
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
name|asMapOfRanges
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|RangeMap
condition|)
block|{
name|RangeMap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|rangeMap
init|=
operator|(
name|RangeMap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|o
decl_stmt|;
return|return
name|asMapOfRanges
argument_list|()
operator|.
name|equals
argument_list|(
name|rangeMap
operator|.
name|asMapOfRanges
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
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|asMapOfRanges
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**    * This class is used to serialize ImmutableRangeMap instances. Serializes the {@link    * #asMapOfRanges()} form.    */
DECL|class|SerializedForm
specifier|private
specifier|static
class|class
name|SerializedForm
parameter_list|<
name|K
extends|extends
name|Comparable
parameter_list|<
name|?
parameter_list|>
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Serializable
block|{
DECL|field|mapOfRanges
specifier|private
specifier|final
name|ImmutableMap
argument_list|<
name|Range
argument_list|<
name|K
argument_list|>
argument_list|,
name|V
argument_list|>
name|mapOfRanges
decl_stmt|;
DECL|method|SerializedForm (ImmutableMap<Range<K>, V> mapOfRanges)
name|SerializedForm
parameter_list|(
name|ImmutableMap
argument_list|<
name|Range
argument_list|<
name|K
argument_list|>
argument_list|,
name|V
argument_list|>
name|mapOfRanges
parameter_list|)
block|{
name|this
operator|.
name|mapOfRanges
operator|=
name|mapOfRanges
expr_stmt|;
block|}
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
if|if
condition|(
name|mapOfRanges
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|of
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|createRangeMap
argument_list|()
return|;
block|}
block|}
DECL|method|createRangeMap ()
name|Object
name|createRangeMap
parameter_list|()
block|{
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builder
init|=
operator|new
name|Builder
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|Range
argument_list|<
name|K
argument_list|>
argument_list|,
name|V
argument_list|>
name|entry
range|:
name|mapOfRanges
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|builder
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
DECL|method|writeReplace ()
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|SerializedForm
argument_list|<>
argument_list|(
name|asMapOfRanges
argument_list|()
argument_list|)
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
end_class

end_unit

