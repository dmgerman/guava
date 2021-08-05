begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|DoNotMock
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
name|BiFunction
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
comment|/**  * A mapping from disjoint nonempty ranges to non-null values. Queries look up the value associated  * with the range (if any) that contains a specified key.  *  *<p>In contrast to {@link RangeSet}, no "coalescing" is done of {@linkplain  * Range#isConnected(Range) connected} ranges, even if they are mapped to the same value.  *  * @author Louis Wasserman  * @since 14.0  */
end_comment

begin_interface
annotation|@
name|Beta
annotation|@
name|DoNotMock
argument_list|(
literal|"Use ImmutableRangeMap or TreeRangeMap"
argument_list|)
annotation|@
name|GwtIncompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|interface|RangeMap
specifier|public
interface|interface
name|RangeMap
parameter_list|<
name|K
extends|extends
name|Comparable
parameter_list|,
name|V
parameter_list|>
block|{
comment|/*    * TODO(cpovirk): These docs sometimes say "map" and sometimes say "range map." Pick one, or at    * least decide on a policy for when to use which.    */
comment|/**    * Returns the value associated with the specified key, or {@code null} if there is no such value.    *    *<p>Specifically, if any range in this range map contains the specified key, the value    * associated with that range is returned.    */
annotation|@
name|CheckForNull
DECL|method|get (K key)
name|V
name|get
parameter_list|(
name|K
name|key
parameter_list|)
function_decl|;
comment|/**    * Returns the range containing this key and its associated value, if such a range is present in    * the range map, or {@code null} otherwise.    */
annotation|@
name|CheckForNull
DECL|method|getEntry (K key)
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
function_decl|;
comment|/**    * Returns the minimal range {@linkplain Range#encloses(Range) enclosing} the ranges in this    * {@code RangeMap}.    *    * @throws NoSuchElementException if this range map is empty    */
DECL|method|span ()
name|Range
argument_list|<
name|K
argument_list|>
name|span
parameter_list|()
function_decl|;
comment|/**    * Maps a range to a specified value (optional operation).    *    *<p>Specifically, after a call to {@code put(range, value)}, if {@link    * Range#contains(Comparable) range.contains(k)}, then {@link #get(Comparable) get(k)} will return    * {@code value}.    *    *<p>If {@code range} {@linkplain Range#isEmpty() is empty}, then this is a no-op.    */
DECL|method|put (Range<K> range, V value)
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
function_decl|;
comment|/**    * Maps a range to a specified value, coalescing this range with any existing ranges with the same    * value that are {@linkplain Range#isConnected connected} to this range.    *    *<p>The behavior of {@link #get(Comparable) get(k)} after calling this method is identical to    * the behavior described in {@link #put(Range, Object) put(range, value)}, however the ranges    * returned from {@link #asMapOfRanges} will be different if there were existing entries which    * connect to the given range and value.    *    *<p>Even if the input range is empty, if it is connected on both sides by ranges mapped to the    * same value those two ranges will be coalesced.    *    *<p><b>Note:</b> coalescing requires calling {@code .equals()} on any connected values, which    * may be expensive depending on the value type. Using this method on range maps with large values    * such as {@link Collection} types is discouraged.    *    * @since 22.0    */
DECL|method|putCoalescing (Range<K> range, V value)
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
function_decl|;
comment|/** Puts all the associations from {@code rangeMap} into this range map (optional operation). */
DECL|method|putAll (RangeMap<K, V> rangeMap)
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
function_decl|;
comment|/** Removes all associations from this range map (optional operation). */
DECL|method|clear ()
name|void
name|clear
parameter_list|()
function_decl|;
comment|/**    * Removes all associations from this range map in the specified range (optional operation).    *    *<p>If {@code !range.contains(k)}, {@link #get(Comparable) get(k)} will return the same result    * before and after a call to {@code remove(range)}. If {@code range.contains(k)}, then after a    * call to {@code remove(range)}, {@code get(k)} will return {@code null}.    */
DECL|method|remove (Range<K> range)
name|void
name|remove
parameter_list|(
name|Range
argument_list|<
name|K
argument_list|>
name|range
parameter_list|)
function_decl|;
comment|/**    * Merges a value into a part of the map by applying a remapping function.    *    *<p>If any parts of the range are already present in this map, those parts are mapped to new    * values by applying the remapping function. The remapping function accepts the map's existing    * value for that part of the range and the given value. It returns the value to be associated    * with that part of the map, or it returns {@code null} to clear that part of the map.    *    *<p>Any parts of the range not already present in this map are mapped to the specified value,    * unless the value is {@code null}.    *    *<p>Any existing entry spanning either range boundary may be split at the boundary, even if the    * merge does not affect its value. For example, if {@code rangeMap} had one entry {@code [1, 5]    * => 3} then {@code rangeMap.merge(Range.closed(0,2), 3, Math::max)} could yield a map with the    * entries {@code [0, 1) => 3, [1, 2] => 3, (2, 5] => 3}.    *    * @since 28.1    */
DECL|method|merge ( Range<K> range, @CheckForNull V value, BiFunction<? super V, ? super @Nullable V, ? extends @Nullable V> remappingFunction)
name|void
name|merge
argument_list|(
name|Range
argument_list|<
name|K
argument_list|>
name|range
argument_list|,
annotation|@
name|CheckForNull
name|V
name|value
argument_list|,
name|BiFunction
operator|<
condition|?
name|super
name|V
argument_list|,
operator|?
name|super
expr|@
name|Nullable
name|V
argument_list|,
operator|?
expr|extends @
name|Nullable
name|V
operator|>
name|remappingFunction
argument_list|)
decl_stmt|;
comment|/**    * Returns a view of this range map as an unmodifiable {@code Map<Range<K>, V>}. Modifications to    * this range map are guaranteed to read through to the returned {@code Map}.    *    *<p>The returned {@code Map} iterates over entries in ascending order of the bounds of the    * {@code Range} entries.    *    *<p>It is guaranteed that no empty ranges will be in the returned {@code Map}.    */
DECL|method|asMapOfRanges ()
name|Map
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
function_decl|;
comment|/**    * Returns a view of this range map as an unmodifiable {@code Map<Range<K>, V>}. Modifications to    * this range map are guaranteed to read through to the returned {@code Map}.    *    *<p>The returned {@code Map} iterates over entries in descending order of the bounds of the    * {@code Range} entries.    *    *<p>It is guaranteed that no empty ranges will be in the returned {@code Map}.    *    * @since 19.0    */
DECL|method|asDescendingMapOfRanges ()
name|Map
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
function_decl|;
comment|/**    * Returns a view of the part of this range map that intersects with {@code range}.    *    *<p>For example, if {@code rangeMap} had the entries {@code [1, 5] => "foo", (6, 8) => "bar",    * (10, â) => "baz"} then {@code rangeMap.subRangeMap(Range.open(3, 12))} would return a range map    * with the entries {@code (3, 5] => "foo", (6, 8) => "bar", (10, 12) => "baz"}.    *    *<p>The returned range map supports all optional operations that this range map supports, except    * for {@code asMapOfRanges().iterator().remove()}.    *    *<p>The returned range map will throw an {@link IllegalArgumentException} on an attempt to    * insert a range not {@linkplain Range#encloses(Range) enclosed} by {@code range}.    */
comment|// TODO(cpovirk): Consider documenting that IAE on the various methods that can throw it.
DECL|method|subRangeMap (Range<K> range)
name|RangeMap
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
name|range
parameter_list|)
function_decl|;
comment|/**    * Returns {@code true} if {@code obj} is another {@code RangeMap} that has an equivalent {@link    * #asMapOfRanges()}.    */
annotation|@
name|Override
DECL|method|equals (@heckForNull Object o)
name|boolean
name|equals
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|o
parameter_list|)
function_decl|;
comment|/** Returns {@code asMapOfRanges().hashCode()}. */
annotation|@
name|Override
DECL|method|hashCode ()
name|int
name|hashCode
parameter_list|()
function_decl|;
comment|/** Returns a readable string representation of this range map. */
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

