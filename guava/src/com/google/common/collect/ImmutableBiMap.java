begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|checkState
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
name|CollectPreconditions
operator|.
name|checkNonnegative
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|VisibleForTesting
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
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|DoNotCall
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|Map
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_comment
comment|/**  * A {@link BiMap} whose contents will never change, with many other important properties detailed  * at {@link ImmutableCollection}.  *  * @author Jared Levy  * @since 2.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|,
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|ImmutableBiMap
specifier|public
specifier|abstract
class|class
name|ImmutableBiMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableBiMapFauxverideShim
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|BiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/**    * Returns a {@link Collector} that accumulates elements into an {@code ImmutableBiMap} whose keys    * and values are the result of applying the provided mapping functions to the input elements.    * Entries appear in the result {@code ImmutableBiMap} in encounter order.    *    *<p>If the mapped keys or values contain duplicates (according to {@link Object#equals(Object)},    * an {@code IllegalArgumentException} is thrown when the collection operation is performed. (This    * differs from the {@code Collector} returned by {@link Collectors#toMap(Function, Function)},    * which throws an {@code IllegalStateException}.)    *    * @since 21.0    */
DECL|method|toImmutableBiMap ( Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|,
name|K
parameter_list|,
name|V
parameter_list|>
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableBiMap
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|K
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
name|toImmutableBiMap
argument_list|(
name|keyFunction
argument_list|,
name|valueFunction
argument_list|)
return|;
block|}
comment|/**    * Returns the empty bimap.    *    *<p><b>Performance note:</b> the instance returned is a singleton.    */
comment|// Casting to any type is safe because the set will never hold any elements.
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
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
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
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|RegularImmutableBiMap
operator|.
name|EMPTY
return|;
block|}
comment|/** Returns an immutable bimap containing a single entry. */
DECL|method|of (K k1, V v1)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|(
name|K
name|k1
parameter_list|,
name|V
name|v1
parameter_list|)
block|{
return|return
operator|new
name|SingletonImmutableBiMap
argument_list|<>
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable map containing the given entries, in order.    *    * @throws IllegalArgumentException if duplicate keys or values are added    */
DECL|method|of (K k1, V v1, K k2, V v2)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|(
name|K
name|k1
parameter_list|,
name|V
name|v1
parameter_list|,
name|K
name|k2
parameter_list|,
name|V
name|v2
parameter_list|)
block|{
return|return
name|RegularImmutableBiMap
operator|.
name|fromEntries
argument_list|(
name|entryOf
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable map containing the given entries, in order.    *    * @throws IllegalArgumentException if duplicate keys or values are added    */
DECL|method|of (K k1, V v1, K k2, V v2, K k3, V v3)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|(
name|K
name|k1
parameter_list|,
name|V
name|v1
parameter_list|,
name|K
name|k2
parameter_list|,
name|V
name|v2
parameter_list|,
name|K
name|k3
parameter_list|,
name|V
name|v3
parameter_list|)
block|{
return|return
name|RegularImmutableBiMap
operator|.
name|fromEntries
argument_list|(
name|entryOf
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k3
argument_list|,
name|v3
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable map containing the given entries, in order.    *    * @throws IllegalArgumentException if duplicate keys or values are added    */
DECL|method|of (K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|(
name|K
name|k1
parameter_list|,
name|V
name|v1
parameter_list|,
name|K
name|k2
parameter_list|,
name|V
name|v2
parameter_list|,
name|K
name|k3
parameter_list|,
name|V
name|v3
parameter_list|,
name|K
name|k4
parameter_list|,
name|V
name|v4
parameter_list|)
block|{
return|return
name|RegularImmutableBiMap
operator|.
name|fromEntries
argument_list|(
name|entryOf
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k3
argument_list|,
name|v3
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k4
argument_list|,
name|v4
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable map containing the given entries, in order.    *    * @throws IllegalArgumentException if duplicate keys or values are added    */
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|(
name|K
name|k1
parameter_list|,
name|V
name|v1
parameter_list|,
name|K
name|k2
parameter_list|,
name|V
name|v2
parameter_list|,
name|K
name|k3
parameter_list|,
name|V
name|v3
parameter_list|,
name|K
name|k4
parameter_list|,
name|V
name|v4
parameter_list|,
name|K
name|k5
parameter_list|,
name|V
name|v5
parameter_list|)
block|{
return|return
name|RegularImmutableBiMap
operator|.
name|fromEntries
argument_list|(
name|entryOf
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k3
argument_list|,
name|v3
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k4
argument_list|,
name|v4
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k5
argument_list|,
name|v5
argument_list|)
argument_list|)
return|;
block|}
comment|// looking for of() with> 5 entries? Use the builder instead.
comment|/**    * Returns a new builder. The generated builder is equivalent to the builder created by the {@link    * Builder} constructor.    */
DECL|method|builder ()
specifier|public
specifier|static
parameter_list|<
name|K
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
comment|/**    * Returns a new builder, expecting the specified number of entries to be added.    *    *<p>If {@code expectedSize} is exactly the number of entries added to the builder before {@link    * Builder#build} is called, the builder is likely to perform better than an unsized {@link    * #builder()} would have.    *    *<p>It is not specified if any performance benefits apply if {@code expectedSize} is close to,    * but not exactly, the number of entries added to the builder.    *    * @since 23.1    */
annotation|@
name|Beta
DECL|method|builderWithExpectedSize (int expectedSize)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builderWithExpectedSize
parameter_list|(
name|int
name|expectedSize
parameter_list|)
block|{
name|checkNonnegative
argument_list|(
name|expectedSize
argument_list|,
literal|"expectedSize"
argument_list|)
expr_stmt|;
return|return
operator|new
name|Builder
argument_list|<>
argument_list|(
name|expectedSize
argument_list|)
return|;
block|}
comment|/**    * A builder for creating immutable bimap instances, especially {@code public static final} bimaps    * ("constant bimaps"). Example:    *    *<pre>{@code    * static final ImmutableBiMap<String, Integer> WORD_TO_INT =    *     new ImmutableBiMap.Builder<String, Integer>()    *         .put("one", 1)    *         .put("two", 2)    *         .put("three", 3)    *         .build();    * }</pre>    *    *<p>For<i>small</i> immutable bimaps, the {@code ImmutableBiMap.of()} methods are even more    * convenient.    *    *<p>By default, a {@code Builder} will generate bimaps that iterate over entries in the order    * they were inserted into the builder. For example, in the above example, {@code    * WORD_TO_INT.entrySet()} is guaranteed to iterate over the entries in the order {@code "one"=1,    * "two"=2, "three"=3}, and {@code keySet()} and {@code values()} respect the same order. If you    * want a different order, consider using {@link #orderEntriesByValue(Comparator)}, which changes    * this builder to sort entries by value.    *    *<p>Builder instances can be reused - it is safe to call {@link #build} multiple times to build    * multiple bimaps in series. Each bimap is a superset of the bimaps created before it.    *    * @since 2.0    */
DECL|class|Builder
specifier|public
specifier|static
specifier|final
class|class
name|Builder
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/**      * Creates a new builder. The returned builder is equivalent to the builder generated by {@link      * ImmutableBiMap#builder}.      */
DECL|method|Builder ()
specifier|public
name|Builder
parameter_list|()
block|{}
DECL|method|Builder (int size)
name|Builder
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|super
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
comment|/**      * Associates {@code key} with {@code value} in the built bimap. Duplicate keys or values are      * not allowed, and will cause {@link #build} to fail.      */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|put (K key, V value)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|put
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|super
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds the given {@code entry} to the bimap. Duplicate keys or values are not allowed, and will      * cause {@link #build} to fail.      *      * @since 19.0      */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|put (Entry<? extends K, ? extends V> entry)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|put
parameter_list|(
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|entry
parameter_list|)
block|{
name|super
operator|.
name|put
argument_list|(
name|entry
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Associates all of the given map's keys and values in the built bimap. Duplicate keys or      * values are not allowed, and will cause {@link #build} to fail.      *      * @throws NullPointerException if any key or value in {@code map} is null      */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|putAll (Map<? extends K, ? extends V> map)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|map
parameter_list|)
block|{
name|super
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds all of the given entries to the built bimap. Duplicate keys or values are not allowed,      * and will cause {@link #build} to fail.      *      * @throws NullPointerException if any key, value, or entry is null      * @since 19.0      */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Beta
annotation|@
name|Override
DECL|method|putAll (Iterable<? extends Entry<? extends K, ? extends V>> entries)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|putAll
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
argument_list|>
name|entries
parameter_list|)
block|{
name|super
operator|.
name|putAll
argument_list|(
name|entries
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures this {@code Builder} to order entries by value according to the specified      * comparator.      *      *<p>The sort order is stable, that is, if two entries have values that compare as equivalent,      * the entry that was inserted first will be first in the built map's iteration order.      *      * @throws IllegalStateException if this method was already called      * @since 19.0      */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Beta
annotation|@
name|Override
DECL|method|orderEntriesByValue (Comparator<? super V> valueComparator)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|orderEntriesByValue
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
name|valueComparator
parameter_list|)
block|{
name|super
operator|.
name|orderEntriesByValue
argument_list|(
name|valueComparator
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
DECL|method|combine (ImmutableMap.Builder<K, V> builder)
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|combine
parameter_list|(
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builder
parameter_list|)
block|{
name|super
operator|.
name|combine
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Returns a newly-created immutable bimap. The iteration order of the returned bimap is the      * order in which entries were inserted into the builder, unless {@link #orderEntriesByValue}      * was called, in which case entries are sorted by value.      *      * @throws IllegalArgumentException if duplicate keys or values were added      */
annotation|@
name|Override
DECL|method|build ()
specifier|public
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
block|{
switch|switch
condition|(
name|size
condition|)
block|{
case|case
literal|0
case|:
return|return
name|of
argument_list|()
return|;
case|case
literal|1
case|:
return|return
name|of
argument_list|(
name|entries
index|[
literal|0
index|]
operator|.
name|getKey
argument_list|()
argument_list|,
name|entries
index|[
literal|0
index|]
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
default|default:
comment|/*            * If entries is full, or if hash flooding is detected, then this implementation may end            * up using the entries array directly and writing over the entry objects with            * non-terminal entries, but this is safe; if this Builder is used further, it will grow            * the entries array (so it can't affect the original array), and future build() calls            * will always copy any entry objects that cannot be safely reused.            */
if|if
condition|(
name|valueComparator
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|entriesUsed
condition|)
block|{
name|entries
operator|=
name|Arrays
operator|.
name|copyOf
argument_list|(
name|entries
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
name|Arrays
operator|.
name|sort
argument_list|(
name|entries
argument_list|,
literal|0
argument_list|,
name|size
argument_list|,
name|Ordering
operator|.
name|from
argument_list|(
name|valueComparator
argument_list|)
operator|.
name|onResultOf
argument_list|(
name|Maps
operator|.
expr|<
name|V
operator|>
name|valueFunction
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|entriesUsed
operator|=
literal|true
expr_stmt|;
return|return
name|RegularImmutableBiMap
operator|.
name|fromEntryArray
argument_list|(
name|size
argument_list|,
name|entries
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|VisibleForTesting
DECL|method|buildJdkBacked ()
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|buildJdkBacked
parameter_list|()
block|{
name|checkState
argument_list|(
name|valueComparator
operator|==
literal|null
argument_list|,
literal|"buildJdkBacked is for tests only, doesn't support orderEntriesByValue"
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|size
condition|)
block|{
case|case
literal|0
case|:
return|return
name|of
argument_list|()
return|;
case|case
literal|1
case|:
return|return
name|of
argument_list|(
name|entries
index|[
literal|0
index|]
operator|.
name|getKey
argument_list|()
argument_list|,
name|entries
index|[
literal|0
index|]
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
default|default:
name|entriesUsed
operator|=
literal|true
expr_stmt|;
return|return
name|RegularImmutableBiMap
operator|.
name|fromEntryArray
argument_list|(
name|size
argument_list|,
name|entries
argument_list|)
return|;
block|}
block|}
block|}
comment|/**    * Returns an immutable bimap containing the same entries as {@code map}. If {@code map} somehow    * contains entries with duplicate keys (for example, if it is a {@code SortedMap} whose    * comparator is not<i>consistent with equals</i>), the results of this method are undefined.    *    *<p>The returned {@code BiMap} iterates over entries in the same order as the {@code entrySet}    * of the original map.    *    *<p>Despite the method name, this method attempts to avoid actually copying the data when it is    * safe to do so. The exact circumstances under which a copy will or will not be performed are    * undocumented and subject to change.    *    * @throws IllegalArgumentException if two keys have the same value or two values have the same    *     key    * @throws NullPointerException if any key or value in {@code map} is null    */
DECL|method|copyOf (Map<? extends K, ? extends V> map)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|copyOf
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
name|map
operator|instanceof
name|ImmutableBiMap
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// safe since map is not writable
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|bimap
init|=
operator|(
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|map
decl_stmt|;
comment|// TODO(lowasser): if we need to make a copy of a BiMap because the
comment|// forward map is a view, don't make a copy of the non-view delegate map
if|if
condition|(
operator|!
name|bimap
operator|.
name|isPartialView
argument_list|()
condition|)
block|{
return|return
name|bimap
return|;
block|}
block|}
return|return
name|copyOf
argument_list|(
name|map
operator|.
name|entrySet
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable bimap containing the given entries. The returned bimap iterates over    * entries in the same order as the original iterable.    *    * @throws IllegalArgumentException if two keys have the same value or two values have the same    *     key    * @throws NullPointerException if any key, value, or entry is null    * @since 19.0    */
annotation|@
name|Beta
DECL|method|copyOf ( Iterable<? extends Entry<? extends K, ? extends V>> entries)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|copyOf
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
argument_list|>
name|entries
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// we'll only be using getKey and getValue, which are covariant
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|entryArray
init|=
operator|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
operator|)
name|Iterables
operator|.
name|toArray
argument_list|(
name|entries
argument_list|,
name|EMPTY_ENTRY_ARRAY
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|entryArray
operator|.
name|length
condition|)
block|{
case|case
literal|0
case|:
return|return
name|of
argument_list|()
return|;
case|case
literal|1
case|:
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|entryArray
index|[
literal|0
index|]
decl_stmt|;
return|return
name|of
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
return|;
default|default:
comment|/*          * The current implementation will end up using entryArray directly, though it will write          * over the (arbitrary, potentially mutable) Entry objects actually stored in entryArray.          */
return|return
name|RegularImmutableBiMap
operator|.
name|fromEntries
argument_list|(
name|entryArray
argument_list|)
return|;
block|}
block|}
DECL|method|ImmutableBiMap ()
name|ImmutableBiMap
parameter_list|()
block|{}
comment|/**    * {@inheritDoc}    *    *<p>The inverse of an {@code ImmutableBiMap} is another {@code ImmutableBiMap}.    */
annotation|@
name|Override
DECL|method|inverse ()
specifier|public
specifier|abstract
name|ImmutableBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
parameter_list|()
function_decl|;
comment|/**    * Returns an immutable set of the values in this map, in the same order they appear in {@link    * #entrySet}.    */
annotation|@
name|Override
DECL|method|values ()
specifier|public
name|ImmutableSet
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
return|return
name|inverse
argument_list|()
operator|.
name|keySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createValues ()
specifier|final
name|ImmutableSet
argument_list|<
name|V
argument_list|>
name|createValues
parameter_list|()
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"should never be called"
argument_list|)
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the bimap unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Deprecated
annotation|@
name|Override
annotation|@
name|DoNotCall
argument_list|(
literal|"Always throws UnsupportedOperationException"
argument_list|)
DECL|method|forcePut (K key, V value)
specifier|public
specifier|final
name|V
name|forcePut
parameter_list|(
name|K
name|key
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
comment|/**    * Serialized type for all ImmutableBiMap instances. It captures the logical contents and they are    * reconstructed using public factory methods. This ensures that the implementation types remain    * as implementation details.    *    *<p>Since the bimap is immutable, ImmutableBiMap doesn't require special logic for keeping the    * bimap and its inverse in sync during serialization, the way AbstractBiMap does.    */
DECL|class|SerializedForm
specifier|private
specifier|static
class|class
name|SerializedForm
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableMap
operator|.
name|SerializedForm
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|SerializedForm (ImmutableBiMap<K, V> bimap)
name|SerializedForm
parameter_list|(
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|bimap
parameter_list|)
block|{
name|super
argument_list|(
name|bimap
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|makeBuilder (int size)
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makeBuilder
parameter_list|(
name|int
name|size
parameter_list|)
block|{
return|return
operator|new
name|Builder
argument_list|<>
argument_list|(
name|size
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
annotation|@
name|Override
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
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

