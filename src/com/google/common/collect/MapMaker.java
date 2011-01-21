begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Function
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
name|CustomConcurrentHashMap
operator|.
name|Strength
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
name|lang
operator|.
name|ref
operator|.
name|SoftReference
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_comment
comment|/**  *<p>A {@link ConcurrentMap} builder, providing any combination of these  * features: {@linkplain SoftReference soft} or {@linkplain WeakReference  * weak} keys, soft or weak values, size-based evicition, timed expiration, and  * on-demand computation of values. Usage example:<pre>   {@code  *  *   ConcurrentMap<Key, Graph> graphs = new MapMaker()  *       .concurrencyLevel(4)  *       .softKeys()  *       .weakValues()  *       .maximumSize(10000)  *       .expireAfterWrite(10, TimeUnit.MINUTES)  *       .makeComputingMap(  *           new Function<Key, Graph>() {  *             public Graph apply(Key key) {  *               return createExpensiveGraph(key);  *             }  *           });}</pre>  *  * These features are all optional; {@code new MapMaker().makeMap()}  * returns a valid concurrent map that behaves exactly like a  * {@link ConcurrentHashMap}.  *  * The returned map is implemented as a hash table with similar performance  * characteristics to {@link ConcurrentHashMap}. It supports all optional  * operations of the {@code ConcurrentMap} interface. It does not permit  * null keys or values.  *  *<p><b>Note:</b> by default, the returned map uses equality comparisons  * (the {@link Object#equals(Object) equals} method) to determine equality  * for keys or values. However, if {@link #weakKeys()} or {@link  * #softKeys()} was specified, the map uses identity ({@code ==})  * comparisons instead for keys. Likewise, if {@link #weakValues()} or  * {@link #softValues()} was specified, the map uses identity comparisons  * for values.  *  *<p>The returned map has<i>weakly consistent iteration</i>: an iterator  * over one of the map's view collections may reflect some, all or none of  * the changes made to the map after the iterator was created.  *  *<p>An entry whose key or value is reclaimed by the garbage collector  * immediately disappears from the map. (If the default settings of strong  * keys and strong values are used, this will never happen.) The client can  * never observe a partially-reclaimed entry. Any {@link java.util.Map.Entry}  * instance retrieved from the map's {@linkplain Map#entrySet() entry set}  * is a snapshot of that entry's state at the time of retrieval; such entries  * do, however, support {@link java.util.Map.Entry#setValue}.  *  *<p>The maps produced by {@code MapMaker} are serializable, and the  * deserialized maps retain all the configuration properties of the original  * map. If the map uses soft or weak references, the entries will be  * reconstructed as they were, but there is no guarantee that the entries won't  * be immediately reclaimed.  *  *<p>{@code new MapMaker().weakKeys().makeMap()} can almost always be  * used as a drop-in replacement for {@link java.util.WeakHashMap}, adding  * concurrency, asynchronous cleanup, identity-based equality for keys, and  * great flexibility.  *  * @author Bob Lee  * @author Kevin Bourrillion  * @since 2 (imported from Google Collections Library)  */
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
extends|extends
name|GenericMapMaker
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
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
literal|16
decl_stmt|;
DECL|field|DEFAULT_EXPIRATION_NANOS
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_EXPIRATION_NANOS
init|=
literal|0
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
DECL|field|maximumSize
name|int
name|maximumSize
init|=
name|UNSET_INT
decl_stmt|;
DECL|field|keyStrength
name|Strength
name|keyStrength
decl_stmt|;
DECL|field|valueStrength
name|Strength
name|valueStrength
decl_stmt|;
DECL|field|expireAfterWriteNanos
name|long
name|expireAfterWriteNanos
init|=
name|UNSET_INT
decl_stmt|;
DECL|field|expireAfterAccessNanos
name|long
name|expireAfterAccessNanos
init|=
name|UNSET_INT
decl_stmt|;
comment|// TODO(kevinb): dispense with this after benchmarking
DECL|field|useCustomMap
name|boolean
name|useCustomMap
decl_stmt|;
DECL|field|keyEquivalence
name|Equivalence
argument_list|<
name|Object
argument_list|>
name|keyEquivalence
decl_stmt|;
DECL|field|valueEquivalence
name|Equivalence
argument_list|<
name|Object
argument_list|>
name|valueEquivalence
decl_stmt|;
comment|/**    * Constructs a new {@code MapMaker} instance with default settings,    * including strong keys, strong values, and no automatic expiration.    */
DECL|method|MapMaker ()
specifier|public
name|MapMaker
parameter_list|()
block|{}
comment|// TODO(kevinb): undo this indirection if keyEquiv gets released
DECL|method|privateKeyEquivalence (Equivalence<Object> equivalence)
name|MapMaker
name|privateKeyEquivalence
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
name|Objects
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
comment|// TODO(kevinb): undo this indirection if valueEquiv gets released
DECL|method|privateValueEquivalence (Equivalence<Object> equivalence)
name|MapMaker
name|privateValueEquivalence
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
name|valueEquivalence
operator|==
literal|null
argument_list|,
literal|"value equivalence was already set to %s"
argument_list|,
name|valueEquivalence
argument_list|)
expr_stmt|;
name|this
operator|.
name|valueEquivalence
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
DECL|method|getValueEquivalence ()
name|Equivalence
argument_list|<
name|Object
argument_list|>
name|getValueEquivalence
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|firstNonNull
argument_list|(
name|valueEquivalence
argument_list|,
name|getValueStrength
argument_list|()
operator|.
name|defaultEquivalence
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Sets a custom initial capacity (defaults to 16). Resizing this or    * any other kind of hash table is a relatively slow operation, so,    * when possible, it is a good idea to provide estimates of expected    * table sizes.    *    * @throws IllegalArgumentException if {@code initialCapacity} is    *   negative    * @throws IllegalStateException if an initial capacity was already set    */
annotation|@
name|Override
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
comment|/**    * Specifies the maximum number of entries the map may contain. While the    * number of entries in the map is not guaranteed to grow to the maximum,    * the map will attempt to make the best use of memory without exceeding the    * maximum number of entries. As the map size grows close to the maximum,    * the map will evict entries that are less likely to be used again. For    * example, the map may evict an entry because it hasn't been used recently    * or very often.    *    * @param size the maximum size of the map    *    * @throws IllegalArgumentException if {@code size} is not greater than zero    * @throws IllegalStateException if a maximum size was already set    * @since 8    */
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
argument_list|(
literal|"To be supported"
argument_list|)
annotation|@
name|Override
DECL|method|maximumSize (int size)
specifier|public
name|MapMaker
name|maximumSize
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|checkState
argument_list|(
name|this
operator|.
name|maximumSize
operator|==
name|UNSET_INT
argument_list|,
literal|"maximum size was already set to %s"
argument_list|,
name|this
operator|.
name|maximumSize
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|size
operator|>
literal|0
argument_list|,
literal|"maximum size must be positive"
argument_list|)
expr_stmt|;
name|this
operator|.
name|maximumSize
operator|=
name|size
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
comment|/**    * Guides the allowed concurrency among update operations. Used as a    * hint for internal sizing. The table is internally partitioned to try    * to permit the indicated number of concurrent updates without    * contention.  Because placement in hash tables is essentially random,    * the actual concurrency will vary. Ideally, you should choose a value    * to accommodate as many threads as will ever concurrently modify the    * table. Using a significantly higher value than you need can waste    * space and time, and a significantly lower value can lead to thread    * contention. But overestimates and underestimates within an order of    * magnitude do not usually have much noticeable impact. A value of one    * is appropriate when it is known that only one thread will modify and    * all others will only read. Defaults to 16.    *    * @throws IllegalArgumentException if {@code concurrencyLevel} is    *     nonpositive    * @throws IllegalStateException if a concurrency level was already set    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.util.concurrent.ConcurrentHashMap concurrencyLevel"
argument_list|)
annotation|@
name|Override
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
comment|/**    * Specifies that each key (not value) stored in the map should be    * wrapped in a {@link WeakReference} (by default, strong references    * are used).    *    *<p><b>Note:</b> the map will use identity ({@code ==}) comparison    * to determine equality of weak keys, which may not behave as you expect.    * For example, storing a key in the map and then attempting a lookup    * using a different but {@link Object#equals(Object) equals}-equivalent    * key will always fail.    *    * @throws IllegalStateException if the key strength was already set    * @see WeakReference    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.lang.ref.WeakReference"
argument_list|)
annotation|@
name|Override
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
comment|/**    * Specifies that each key (not value) stored in the map should be    * wrapped in a {@link SoftReference} (by default, strong references    * are used).    *    *<p><b>Note:</b> the map will use identity ({@code ==}) comparison    * to determine equality of soft keys, which may not behave as you expect.    * For example, storing a key in the map and then attempting a lookup    * using a different but {@link Object#equals(Object) equals}-equivalent    * key will always fail.    *    * @throws IllegalStateException if the key strength was already set    * @see SoftReference    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.lang.ref.SoftReference"
argument_list|)
annotation|@
name|Override
DECL|method|softKeys ()
specifier|public
name|MapMaker
name|softKeys
parameter_list|()
block|{
return|return
name|setKeyStrength
argument_list|(
name|Strength
operator|.
name|SOFT
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
name|Objects
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
comment|/**    * Specifies that each value (not key) stored in the map should be    * wrapped in a {@link WeakReference} (by default, strong references    * are used).    *    *<p>Weak values will be garbage collected once they are weakly    * reachable. This makes them a poor candidate for caching; consider    * {@link #softValues()} instead.    *    *<p><b>Note:</b> the map will use identity ({@code ==}) comparison    * to determine equality of weak values. This will notably impact    * the behavior of {@link Map#containsValue(Object) containsValue},    * {@link ConcurrentMap#remove(Object, Object) remove(Object, Object)},    * and {@link ConcurrentMap#replace(Object, Object, Object) replace(K, V, V)}.    *    * @throws IllegalStateException if the value strength was already set    * @see WeakReference    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.lang.ref.WeakReference"
argument_list|)
annotation|@
name|Override
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
comment|/**    * Specifies that each value (not key) stored in the map should be    * wrapped in a {@link SoftReference} (by default, strong references    * are used).    *    *<p>Soft values will be garbage collected in response to memory    * demand, and in a least-recently-used manner. This makes them a    * good candidate for caching.    *    *<p><b>Note:</b> the map will use identity ({@code ==}) comparison    * to determine equality of soft values. This will notably impact    * the behavior of {@link Map#containsValue(Object) containsValue},    * {@link ConcurrentMap#remove(Object, Object) remove(Object, Object)},    * and {@link ConcurrentMap#replace(Object, Object, Object) replace(K, V, V)}.    *    * @throws IllegalStateException if the value strength was already set    * @see SoftReference    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.lang.ref.SoftReference"
argument_list|)
annotation|@
name|Override
DECL|method|softValues ()
specifier|public
name|MapMaker
name|softValues
parameter_list|()
block|{
return|return
name|setValueStrength
argument_list|(
name|Strength
operator|.
name|SOFT
argument_list|)
return|;
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
name|Objects
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
comment|/**    * Old name of {@link #expireAfterWrite}.    */
comment|// TODO(user): deprecate
annotation|@
name|Override
DECL|method|expiration (long duration, TimeUnit unit)
specifier|public
name|MapMaker
name|expiration
parameter_list|(
name|long
name|duration
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|expireAfterWrite
argument_list|(
name|duration
argument_list|,
name|unit
argument_list|)
return|;
block|}
comment|/**    * Specifies that each entry should be automatically removed from the    * map once a fixed duration has passed since the entry's creation.    * Note that changing the value of an entry will reset its expiration    * time.    *    * @param duration the length of time after an entry is created that it    *     should be automatically removed    * @param unit the unit that {@code duration} is expressed in    * @throws IllegalArgumentException if {@code duration} is not positive, or is    *     larger than one hundred years    * @throws IllegalStateException if the time to live or time to idle was    *     already set    * @since 8    */
annotation|@
name|Beta
annotation|@
name|Override
DECL|method|expireAfterWrite (long duration, TimeUnit unit)
specifier|public
name|MapMaker
name|expireAfterWrite
parameter_list|(
name|long
name|duration
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|checkExpiration
argument_list|(
name|duration
argument_list|,
name|unit
argument_list|)
expr_stmt|;
name|this
operator|.
name|expireAfterWriteNanos
operator|=
name|unit
operator|.
name|toNanos
argument_list|(
name|duration
argument_list|)
expr_stmt|;
name|useCustomMap
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|checkExpiration (long duration, TimeUnit unit)
specifier|private
name|void
name|checkExpiration
parameter_list|(
name|long
name|duration
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|checkState
argument_list|(
name|expireAfterWriteNanos
operator|==
name|UNSET_INT
argument_list|,
literal|"expireAfterWrite was already set to %s ns"
argument_list|,
name|expireAfterWriteNanos
argument_list|)
expr_stmt|;
name|checkState
argument_list|(
name|expireAfterAccessNanos
operator|==
name|UNSET_INT
argument_list|,
literal|"expireAfterAccess was already set to %s ns"
argument_list|,
name|expireAfterAccessNanos
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|duration
operator|>
literal|0
argument_list|,
literal|"duration must be positive: %s %s"
argument_list|,
name|duration
argument_list|,
name|unit
argument_list|)
expr_stmt|;
block|}
DECL|method|getExpireAfterWriteNanos ()
name|long
name|getExpireAfterWriteNanos
parameter_list|()
block|{
return|return
operator|(
name|expireAfterWriteNanos
operator|==
name|UNSET_INT
operator|)
condition|?
name|DEFAULT_EXPIRATION_NANOS
else|:
name|expireAfterWriteNanos
return|;
block|}
comment|/**    * Specifies that each entry should be automatically removed from the    * map once a fixed duration has passed since the entry's last access.    *    * @param duration the length of time after an entry is last accessed    *     that it should be automatically removed    * @param unit the unit that {@code duration} is expressed in    * @throws IllegalArgumentException if {@code duration} is not positive, or is    *     larger than one hundred years    * @throws IllegalStateException if the time to idle or time to live was    *     already set    * @since 8    */
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
argument_list|(
literal|"To be supported"
argument_list|)
annotation|@
name|Override
DECL|method|expireAfterAccess (long duration, TimeUnit unit)
specifier|public
name|MapMaker
name|expireAfterAccess
parameter_list|(
name|long
name|duration
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|checkExpiration
argument_list|(
name|duration
argument_list|,
name|unit
argument_list|)
expr_stmt|;
name|this
operator|.
name|expireAfterAccessNanos
operator|=
name|unit
operator|.
name|toNanos
argument_list|(
name|duration
argument_list|)
expr_stmt|;
name|useCustomMap
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getExpireAfterAccessNanos ()
name|long
name|getExpireAfterAccessNanos
parameter_list|()
block|{
return|return
operator|(
name|expireAfterAccessNanos
operator|==
name|UNSET_INT
operator|)
condition|?
name|DEFAULT_EXPIRATION_NANOS
else|:
name|expireAfterAccessNanos
return|;
block|}
comment|/**    * Specifies a listener instance, which all maps built using this {@code    * MapMaker} will notify each time an entry is evicted.    *    *<p>A map built by this map maker will invoke the supplied listener after it    * evicts an entry, whether it does so due to timed expiration, exceeding the    * maximum size, or discovering that the key or value has been reclaimed by    * the garbage collector. It will invoke the listener synchronously, during    * invocations of any of that map's public methods (even read-only methods).    * The listener will<i>not</i> be invoked on manual removal.    *    *<p><b>Important note:</b> Instead of returning<em>this</em> as a {@code    * MapMaker} instance, this method returns {@code GenericMapMaker<K, V>}.    * From this point on, either the original reference or the returned    * reference may be used to complete configuration and build the map, but only    * the "generic" one is type-safe. That is, it will properly prevent you from    * building maps whose key or value types are incompatible with the types    * accepted by the listener already provided; the {@code MapMaker} type cannot    * do this. For best results, simply use the standard method-chaining idiom,    * as illustrated in the documentation at top, configuring a {@code MapMaker}    * and building your {@link Map} all in a single statement.    *    *<p><b>Warning:</b> if you ignore the above advice, and use this {@code    * MapMaker} to build maps whose key or value types are incompatible with the    * listener, you will likely experience a {@link ClassCastException} at an    * undefined point in the future.    *    * @throws IllegalStateException if an eviction listener was already set    * @since 7    */
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
argument_list|(
literal|"To be supported"
argument_list|)
DECL|method|evictionListener ( MapEvictionListener<K, V> listener)
specifier|public
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|GenericMapMaker
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|evictionListener
parameter_list|(
name|MapEvictionListener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|listener
parameter_list|)
block|{
name|checkState
argument_list|(
name|this
operator|.
name|evictionListener
operator|==
literal|null
argument_list|)
expr_stmt|;
comment|// safely limiting the kinds of maps this can produce
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|GenericMapMaker
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|me
init|=
operator|(
name|GenericMapMaker
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|this
decl_stmt|;
name|me
operator|.
name|evictionListener
operator|=
name|checkNotNull
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|useCustomMap
operator|=
literal|true
expr_stmt|;
return|return
name|me
return|;
block|}
comment|/**    * Builds a map, without on-demand computation of values. This method    * does not alter the state of this {@code MapMaker} instance, so it can be    * invoked again to create multiple independent maps.    *    * @return a serializable concurrent map having the requested features    */
annotation|@
name|Override
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
return|return
name|useCustomMap
condition|?
operator|new
name|CustomConcurrentHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|this
argument_list|)
else|:
operator|new
name|ConcurrentHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
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
comment|/**    * Builds a caching function, which either returns an already-computed value    * for a given key or atomically computes it using the supplied function.    * If another thread is currently computing the value for this key, simply    * waits for that thread to finish and returns its computed value. Note that    * the function may be executed concurrently by multiple threads, but only for    * distinct keys.    *    *<p>The {@code Map} view of the {@code Cache}'s cache is only    * updated when function computation completes. In other words, an entry isn't    * visible until the value's computation completes. No methods on the {@code    * Map} will ever trigger computation.    *    *<p>{@link Cache#apply} in the returned function implementation may    * throw:    *    *<ul>    *<li>{@link NullPointerException} if the key is null or the    *     computing function returns null    *<li>{@link ComputationException} if an exception was thrown by the    *     computing function. If that exception is already of type {@link    *     ComputationException} it is propagated directly; otherwise it is    *     wrapped.    *</ul>    *    *<p>If {@link Map#put} is called before a computation completes, other    * threads waiting on the computation will wake up and return the stored    * value. When the computation completes, its result will be ignored.    *    *<p>This method does not alter the state of this {@code MapMaker} instance,    * so it can be invoked again to create multiple independent maps.    *    * @param computingFunction the function used to compute new values    * @return a serializable cache having the requested features    */
comment|// TODO(kevinb): figure out the Cache interface before making this public
DECL|method|makeCache ( Function<? super K, ? extends V> computingFunction)
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makeCache
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|computingFunction
parameter_list|)
block|{
return|return
operator|new
name|ComputingConcurrentHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|this
argument_list|,
name|computingFunction
argument_list|)
return|;
block|}
comment|/**    * Builds a map that supports atomic, on-demand computation of values. {@link    * Map#get} either returns an already-computed value for the given key,    * atomically computes it using the supplied function, or, if another thread    * is currently computing the value for this key, simply waits for that thread    * to finish and returns its computed value. Note that the function may be    * executed concurrently by multiple threads, but only for distinct keys.    *    *<p>If an entry's value has not finished computing yet, query methods    * besides {@code get} return immediately as if an entry doesn't exist. In    * other words, an entry isn't externally visible until the value's    * computation completes.    *    *<p>{@link Map#get} on the returned map will never return {@code null}. It    * may throw:    *    *<ul>    *<li>{@link NullPointerException} if the key is null or the computing    *     function returns null    *<li>{@link ComputationException} if an exception was thrown by the    *     computing function. If that exception is already of type {@link    *     ComputationException} it is propagated directly; otherwise it is    *     wrapped.    *</ul>    *    *<p><b>Note:</b> Callers of {@code get}<i>must</i> ensure that the key    * argument is of type {@code K}. The {@code get} method accepts {@code    * Object}, so the key type is not checked at compile time. Passing an object    * of a type other than {@code K} can result in that object being unsafely    * passed to the computing function as type {@code K}, and unsafely stored in    * the map.    *    *<p>If {@link Map#put} is called before a computation completes, other    * threads waiting on the computation will wake up and return the stored    * value.    *    *<p>This method does not alter the state of this {@code MapMaker} instance,    * so it can be invoked again to create multiple independent maps.    *    * @param computingFunction the function used to compute new values    * @return a serializable concurrent map having the requested features    */
annotation|@
name|Override
DECL|method|makeComputingMap ( Function<? super K, ? extends V> computingFunction)
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
name|makeComputingMap
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|computingFunction
parameter_list|)
block|{
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cache
init|=
name|makeCache
argument_list|(
name|computingFunction
argument_list|)
decl_stmt|;
return|return
operator|new
name|ComputingMapAdapter
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|cache
argument_list|)
return|;
block|}
comment|/**    * A function which caches the result of each application (computation). This    * interface does not specify the caching semantics, but does expose a {@code    * ConcurrentMap} view of cached entries.    */
DECL|interface|Cache
interface|interface
name|Cache
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|Function
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/**      * Returns a map view of the cached entries.      */
DECL|method|asMap ()
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|asMap
parameter_list|()
function_decl|;
block|}
comment|/**    * Overrides get() to compute on demand.    */
DECL|class|ComputingMapAdapter
specifier|static
class|class
name|ComputingMapAdapter
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
DECL|field|cache
specifier|final
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cache
decl_stmt|;
DECL|method|ComputingMapAdapter (Cache<K, V> cache)
name|ComputingMapAdapter
parameter_list|(
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|cache
operator|.
name|asMap
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// unsafe, which is why this is deprecated
DECL|method|get (Object key)
annotation|@
name|Override
specifier|public
name|V
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|cache
operator|.
name|apply
argument_list|(
operator|(
name|K
operator|)
name|key
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

