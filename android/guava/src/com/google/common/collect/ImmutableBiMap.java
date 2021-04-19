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
name|collect
operator|.
name|CollectPreconditions
operator|.
name|checkEntryNotNull
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
name|Map
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
name|ImmutableMap
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
name|checkEntryNotNull
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
expr_stmt|;
return|return
operator|new
name|RegularImmutableBiMap
argument_list|<>
argument_list|(
operator|new
name|Object
index|[]
block|{
name|k1
block|,
name|v1
block|}
argument_list|,
literal|1
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
name|checkEntryNotNull
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
expr_stmt|;
name|checkEntryNotNull
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
expr_stmt|;
return|return
operator|new
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
operator|new
name|Object
index|[]
block|{
name|k1
block|,
name|v1
block|,
name|k2
block|,
name|v2
block|}
argument_list|,
literal|2
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
name|checkEntryNotNull
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
expr_stmt|;
name|checkEntryNotNull
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
expr_stmt|;
name|checkEntryNotNull
argument_list|(
name|k3
argument_list|,
name|v3
argument_list|)
expr_stmt|;
return|return
operator|new
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
operator|new
name|Object
index|[]
block|{
name|k1
block|,
name|v1
block|,
name|k2
block|,
name|v2
block|,
name|k3
block|,
name|v3
block|}
argument_list|,
literal|3
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
name|checkEntryNotNull
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
expr_stmt|;
name|checkEntryNotNull
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
expr_stmt|;
name|checkEntryNotNull
argument_list|(
name|k3
argument_list|,
name|v3
argument_list|)
expr_stmt|;
name|checkEntryNotNull
argument_list|(
name|k4
argument_list|,
name|v4
argument_list|)
expr_stmt|;
return|return
operator|new
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
operator|new
name|Object
index|[]
block|{
name|k1
block|,
name|v1
block|,
name|k2
block|,
name|v2
block|,
name|k3
block|,
name|v3
block|,
name|k4
block|,
name|v4
block|}
argument_list|,
literal|4
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
name|checkEntryNotNull
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
expr_stmt|;
name|checkEntryNotNull
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
expr_stmt|;
name|checkEntryNotNull
argument_list|(
name|k3
argument_list|,
name|v3
argument_list|)
expr_stmt|;
name|checkEntryNotNull
argument_list|(
name|k4
argument_list|,
name|v4
argument_list|)
expr_stmt|;
name|checkEntryNotNull
argument_list|(
name|k5
argument_list|,
name|v5
argument_list|)
expr_stmt|;
return|return
operator|new
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
operator|new
name|Object
index|[]
block|{
name|k1
block|,
name|v1
block|,
name|k2
block|,
name|v2
block|,
name|k3
block|,
name|v3
block|,
name|k4
block|,
name|v4
block|,
name|k5
block|,
name|v5
block|}
argument_list|,
literal|5
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
block|{
name|super
argument_list|()
expr_stmt|;
block|}
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
if|if
condition|(
name|size
operator|==
literal|0
condition|)
block|{
return|return
name|of
argument_list|()
return|;
block|}
name|sortEntries
argument_list|()
expr_stmt|;
name|entriesUsed
operator|=
literal|true
expr_stmt|;
return|return
operator|new
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|alternatingKeysAndValues
argument_list|,
name|size
argument_list|)
return|;
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
name|int
name|estimatedSize
init|=
operator|(
name|entries
operator|instanceof
name|Collection
operator|)
condition|?
operator|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|entries
operator|)
operator|.
name|size
argument_list|()
else|:
name|ImmutableCollection
operator|.
name|Builder
operator|.
name|DEFAULT_INITIAL_CAPACITY
decl_stmt|;
return|return
operator|new
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|estimatedSize
argument_list|)
operator|.
name|putAll
argument_list|(
name|entries
argument_list|)
operator|.
name|build
argument_list|()
return|;
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

