begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|base
operator|.
name|Ascii
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
name|Equivalence
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
name|MoreObjects
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
name|MapMakerInternalMap
operator|.
name|Strength
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
name|lang
operator|.
name|ref
operator|.
name|WeakReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ConcurrentModificationException
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
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
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
name|MonotonicNonNull
import|;
end_import

begin_comment
comment|/**  * A builder of {@link ConcurrentMap} instances that can have keys or values automatically wrapped  * in {@linkplain WeakReference weak} references.  *  *<p>Usage example:  *  *<pre>{@code  * ConcurrentMap<Request, Stopwatch> timers = new MapMaker()  *     .concurrencyLevel(4)  *     .weakKeys()  *     .makeMap();  * }</pre>  *  *<p>These features are all optional; {@code new MapMaker().makeMap()} returns a valid concurrent  * map that behaves similarly to a {@link ConcurrentHashMap}.  *  *<p>The returned map is implemented as a hash table with similar performance characteristics to  * {@link ConcurrentHashMap}. It supports all optional operations of the {@code ConcurrentMap}  * interface. It does not permit null keys or values.  *  *<p><b>Note:</b> by default, the returned map uses equality comparisons (the {@link Object#equals  * equals} method) to determine equality for keys or values. However, if {@link #weakKeys} was  * specified, the map uses identity ({@code ==}) comparisons instead for keys. Likewise, if {@link  * #weakValues} was specified, the map uses identity comparisons for values.  *  *<p>The view collections of the returned map have<i>weakly consistent iterators</i>. This means  * that they are safe for concurrent use, but if other threads modify the map after the iterator is  * created, it is undefined which of these changes, if any, are reflected in that iterator. These  * iterators never throw {@link ConcurrentModificationException}.  *  *<p>If {@link #weakKeys} or {@link #weakValues} are requested, it is possible for a key or value  * present in the map to be reclaimed by the garbage collector. Entries with reclaimed keys or  * values may be removed from the map on each map modification or on occasional map accesses; such  * entries may be counted by {@link Map#size}, but will never be visible to read or write  * operations. A partially-reclaimed entry is never exposed to the user. Any {@link java.util.Entry}  * instance retrieved from the map's {@linkplain Map#entrySet entry set} is a snapshot of that  * entry's state at the time of retrieval; such entries do, however, support {@link  * java.util.Entry#setValue}, which simply calls {@link Map#put} on the entry's key.  *  *<p>The maps produced by {@code MapMaker} are serializable, and the deserialized maps retain all  * the configuration properties of the original map. During deserialization, if the original map had  * used weak references, the entries are reconstructed as they were, but it's not unlikely they'll  * be quickly garbage-collected before they are ever accessed.  *  *<p>{@code new MapMaker().weakKeys().makeMap()} is a recommended replacement for {@link  * java.util.WeakHashMap}, but note that it compares keys using object identity whereas {@code  * WeakHashMap} uses {@link Object#equals}.  *  * @author Bob Lee  * @author Charles Fry  * @author Kevin Bourrillion  * @since 2.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|MapMaker
specifier|public
specifier|final
class|class
name|MapMaker
block|{
DECL|field|DEFAULT_INITIAL_CAPACITY
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_INITIAL_CAPACITY
init|=
literal|16
decl_stmt|;
DECL|field|DEFAULT_CONCURRENCY_LEVEL
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_CONCURRENCY_LEVEL
init|=
literal|4
decl_stmt|;
DECL|field|UNSET_INT
specifier|static
specifier|final
name|int
name|UNSET_INT
init|=
operator|-
literal|1
decl_stmt|;
comment|// TODO(kevinb): dispense with this after benchmarking
DECL|field|useCustomMap
name|boolean
name|useCustomMap
decl_stmt|;
DECL|field|initialCapacity
name|int
name|initialCapacity
init|=
name|UNSET_INT
decl_stmt|;
DECL|field|concurrencyLevel
name|int
name|concurrencyLevel
init|=
name|UNSET_INT
decl_stmt|;
DECL|field|keyStrength
annotation|@
name|MonotonicNonNull
name|Strength
name|keyStrength
decl_stmt|;
DECL|field|valueStrength
annotation|@
name|MonotonicNonNull
name|Strength
name|valueStrength
decl_stmt|;
DECL|field|keyEquivalence
annotation|@
name|MonotonicNonNull
name|Equivalence
argument_list|<
name|Object
argument_list|>
name|keyEquivalence
decl_stmt|;
comment|/**    * Constructs a new {@code MapMaker} instance with default settings, including strong keys, strong    * values, and no automatic eviction of any kind.    */
DECL|method|MapMaker ()
specifier|public
name|MapMaker
parameter_list|()
block|{}
comment|/**    * Sets a custom {@code Equivalence} strategy for comparing keys.    *    *<p>By default, the map uses {@link Equivalence#identity} to determine key equality when {@link    * #weakKeys} is specified, and {@link Equivalence#equals()} otherwise. The only place this is    * used is in {@link Interners.WeakInterner}.    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|GwtIncompatible
comment|// To be supported
DECL|method|keyEquivalence (Equivalence<Object> equivalence)
name|MapMaker
name|keyEquivalence
parameter_list|(
name|Equivalence
argument_list|<
name|Object
argument_list|>
name|equivalence
parameter_list|)
block|{
name|checkState
argument_list|(
name|keyEquivalence
operator|==
literal|null
argument_list|,
literal|"key equivalence was already set to %s"
argument_list|,
name|keyEquivalence
argument_list|)
expr_stmt|;
name|keyEquivalence
operator|=
name|checkNotNull
argument_list|(
name|equivalence
argument_list|)
expr_stmt|;
name|this
operator|.
name|useCustomMap
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getKeyEquivalence ()
name|Equivalence
argument_list|<
name|Object
argument_list|>
name|getKeyEquivalence
parameter_list|()
block|{
return|return
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|keyEquivalence
argument_list|,
name|getKeyStrength
argument_list|()
operator|.
name|defaultEquivalence
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Sets the minimum total size for the internal hash tables. For example, if the initial capacity    * is {@code 60}, and the concurrency level is {@code 8}, then eight segments are created, each    * having a hash table of size eight. Providing a large enough estimate at construction time    * avoids the need for expensive resizing operations later, but setting this value unnecessarily    * high wastes memory.    *    * @throws IllegalArgumentException if {@code initialCapacity} is negative    * @throws IllegalStateException if an initial capacity was already set    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|initialCapacity (int initialCapacity)
specifier|public
name|MapMaker
name|initialCapacity
parameter_list|(
name|int
name|initialCapacity
parameter_list|)
block|{
name|checkState
argument_list|(
name|this
operator|.
name|initialCapacity
operator|==
name|UNSET_INT
argument_list|,
literal|"initial capacity was already set to %s"
argument_list|,
name|this
operator|.
name|initialCapacity
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|initialCapacity
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|this
operator|.
name|initialCapacity
operator|=
name|initialCapacity
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getInitialCapacity ()
name|int
name|getInitialCapacity
parameter_list|()
block|{
return|return
operator|(
name|initialCapacity
operator|==
name|UNSET_INT
operator|)
condition|?
name|DEFAULT_INITIAL_CAPACITY
else|:
name|initialCapacity
return|;
block|}
comment|/**    * Guides the allowed concurrency among update operations. Used as a hint for internal sizing. The    * table is internally partitioned to try to permit the indicated number of concurrent updates    * without contention. Because assignment of entries to these partitions is not necessarily    * uniform, the actual concurrency observed may vary. Ideally, you should choose a value to    * accommodate as many threads as will ever concurrently modify the table. Using a significantly    * higher value than you need can waste space and time, and a significantly lower value can lead    * to thread contention. But overestimates and underestimates within an order of magnitude do not    * usually have much noticeable impact. A value of one permits only one thread to modify the map    * at a time, but since read operations can proceed concurrently, this still yields higher    * concurrency than full synchronization. Defaults to 4.    *    *<p><b>Note:</b> Prior to Guava release 9.0, the default was 16. It is possible the default will    * change again in the future. If you care about this value, you should always choose it    * explicitly.    *    * @throws IllegalArgumentException if {@code concurrencyLevel} is nonpositive    * @throws IllegalStateException if a concurrency level was already set    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|concurrencyLevel (int concurrencyLevel)
specifier|public
name|MapMaker
name|concurrencyLevel
parameter_list|(
name|int
name|concurrencyLevel
parameter_list|)
block|{
name|checkState
argument_list|(
name|this
operator|.
name|concurrencyLevel
operator|==
name|UNSET_INT
argument_list|,
literal|"concurrency level was already set to %s"
argument_list|,
name|this
operator|.
name|concurrencyLevel
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|concurrencyLevel
operator|>
literal|0
argument_list|)
expr_stmt|;
name|this
operator|.
name|concurrencyLevel
operator|=
name|concurrencyLevel
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getConcurrencyLevel ()
name|int
name|getConcurrencyLevel
parameter_list|()
block|{
return|return
operator|(
name|concurrencyLevel
operator|==
name|UNSET_INT
operator|)
condition|?
name|DEFAULT_CONCURRENCY_LEVEL
else|:
name|concurrencyLevel
return|;
block|}
comment|/**    * Specifies that each key (not value) stored in the map should be wrapped in a {@link    * WeakReference} (by default, strong references are used).    *    *<p><b>Warning:</b> when this method is used, the resulting map will use identity ({@code ==})    * comparison to determine equality of keys, which is a technical violation of the {@link Map}    * specification, and may not be what you expect.    *    * @throws IllegalStateException if the key strength was already set    * @see WeakReference    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|GwtIncompatible
comment|// java.lang.ref.WeakReference
DECL|method|weakKeys ()
specifier|public
name|MapMaker
name|weakKeys
parameter_list|()
block|{
return|return
name|setKeyStrength
argument_list|(
name|Strength
operator|.
name|WEAK
argument_list|)
return|;
block|}
DECL|method|setKeyStrength (Strength strength)
name|MapMaker
name|setKeyStrength
parameter_list|(
name|Strength
name|strength
parameter_list|)
block|{
name|checkState
argument_list|(
name|keyStrength
operator|==
literal|null
argument_list|,
literal|"Key strength was already set to %s"
argument_list|,
name|keyStrength
argument_list|)
expr_stmt|;
name|keyStrength
operator|=
name|checkNotNull
argument_list|(
name|strength
argument_list|)
expr_stmt|;
if|if
condition|(
name|strength
operator|!=
name|Strength
operator|.
name|STRONG
condition|)
block|{
comment|// STRONG could be used during deserialization.
name|useCustomMap
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|getKeyStrength ()
name|Strength
name|getKeyStrength
parameter_list|()
block|{
return|return
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|keyStrength
argument_list|,
name|Strength
operator|.
name|STRONG
argument_list|)
return|;
block|}
comment|/**    * Specifies that each value (not key) stored in the map should be wrapped in a {@link    * WeakReference} (by default, strong references are used).    *    *<p>Weak values will be garbage collected once they are weakly reachable. This makes them a poor    * candidate for caching.    *    *<p><b>Warning:</b> when this method is used, the resulting map will use identity ({@code ==})    * comparison to determine equality of values. This technically violates the specifications of the    * methods {@link Map#containsValue containsValue}, {@link ConcurrentMap#remove(Object, Object)    * remove(Object, Object)} and {@link ConcurrentMap#replace(Object, Object, Object) replace(K, V,    * V)}, and may not be what you expect.    *    * @throws IllegalStateException if the value strength was already set    * @see WeakReference    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|GwtIncompatible
comment|// java.lang.ref.WeakReference
DECL|method|weakValues ()
specifier|public
name|MapMaker
name|weakValues
parameter_list|()
block|{
return|return
name|setValueStrength
argument_list|(
name|Strength
operator|.
name|WEAK
argument_list|)
return|;
block|}
comment|/**    * A dummy singleton value type used by {@link Interners}.    *    *<p>{@link MapMakerInternalMap} can optimize for memory usage in this case; see {@link    * MapMakerInternalMap#createWithDummyValues}.    */
DECL|enum|Dummy
enum|enum
name|Dummy
block|{
DECL|enumConstant|VALUE
name|VALUE
block|}
DECL|method|setValueStrength (Strength strength)
name|MapMaker
name|setValueStrength
parameter_list|(
name|Strength
name|strength
parameter_list|)
block|{
name|checkState
argument_list|(
name|valueStrength
operator|==
literal|null
argument_list|,
literal|"Value strength was already set to %s"
argument_list|,
name|valueStrength
argument_list|)
expr_stmt|;
name|valueStrength
operator|=
name|checkNotNull
argument_list|(
name|strength
argument_list|)
expr_stmt|;
if|if
condition|(
name|strength
operator|!=
name|Strength
operator|.
name|STRONG
condition|)
block|{
comment|// STRONG could be used during deserialization.
name|useCustomMap
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|getValueStrength ()
name|Strength
name|getValueStrength
parameter_list|()
block|{
return|return
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|valueStrength
argument_list|,
name|Strength
operator|.
name|STRONG
argument_list|)
return|;
block|}
comment|/**    * Builds a thread-safe map. This method does not alter the state of this {@code MapMaker}    * instance, so it can be invoked again to create multiple independent maps.    *    *<p>The bulk operations {@code putAll}, {@code equals}, and {@code clear} are not guaranteed to    * be performed atomically on the returned map. Additionally, {@code size} and {@code    * containsValue} are implemented as bulk read operations, and thus may fail to observe concurrent    * writes.    *    * @return a serializable concurrent map having the requested features    */
DECL|method|makeMap ()
specifier|public
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makeMap
parameter_list|()
block|{
if|if
condition|(
operator|!
name|useCustomMap
condition|)
block|{
return|return
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|(
name|getInitialCapacity
argument_list|()
argument_list|,
literal|0.75f
argument_list|,
name|getConcurrencyLevel
argument_list|()
argument_list|)
return|;
block|}
return|return
name|MapMakerInternalMap
operator|.
name|create
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**    * Returns a string representation for this MapMaker instance. The exact form of the returned    * string is not specified.    */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|MoreObjects
operator|.
name|ToStringHelper
name|s
init|=
name|MoreObjects
operator|.
name|toStringHelper
argument_list|(
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|initialCapacity
operator|!=
name|UNSET_INT
condition|)
block|{
name|s
operator|.
name|add
argument_list|(
literal|"initialCapacity"
argument_list|,
name|initialCapacity
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|concurrencyLevel
operator|!=
name|UNSET_INT
condition|)
block|{
name|s
operator|.
name|add
argument_list|(
literal|"concurrencyLevel"
argument_list|,
name|concurrencyLevel
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|keyStrength
operator|!=
literal|null
condition|)
block|{
name|s
operator|.
name|add
argument_list|(
literal|"keyStrength"
argument_list|,
name|Ascii
operator|.
name|toLowerCase
argument_list|(
name|keyStrength
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|valueStrength
operator|!=
literal|null
condition|)
block|{
name|s
operator|.
name|add
argument_list|(
literal|"valueStrength"
argument_list|,
name|Ascii
operator|.
name|toLowerCase
argument_list|(
name|valueStrength
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|keyEquivalence
operator|!=
literal|null
condition|)
block|{
name|s
operator|.
name|addValue
argument_list|(
literal|"keyEquivalence"
argument_list|)
expr_stmt|;
block|}
return|return
name|s
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

