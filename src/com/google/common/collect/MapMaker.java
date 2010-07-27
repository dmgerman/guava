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
comment|/**  *<p>A {@link ConcurrentMap} builder, providing any combination of these  * features: {@linkplain SoftReference soft} or {@linkplain WeakReference  * weak} keys, soft or weak values, timed expiration, and on-demand  * computation of values. Usage example:<pre> {@code  *  *   ConcurrentMap<Key, Graph> graphs = new MapMaker()  *       .concurrencyLevel(32)  *       .softKeys()  *       .weakValues()  *       .expiration(30, TimeUnit.MINUTES)  *       .makeComputingMap(  *           new Function<Key, Graph>() {  *             public Graph apply(Key key) {  *               return createExpensiveGraph(key);  *             }  *           });}</pre>  *  * These features are all optional; {@code new MapMaker().makeMap()}  * returns a valid concurrent map that behaves exactly like a  * {@link ConcurrentHashMap}.  *  * The returned map is implemented as a hash table with similar performance  * characteristics to {@link ConcurrentHashMap}. It supports all optional  * operations of the {@code ConcurrentMap} interface. It does not permit  * null keys or values. It is serializable; however, serializing a map that  * uses soft or weak references can give unpredictable results.  *  *<p><b>Note:</b> by default, the returned map uses equality comparisons  * (the {@link Object#equals(Object) equals} method) to determine equality  * for keys or values. However, if {@link #weakKeys()} or {@link  * #softKeys()} was specified, the map uses identity ({@code ==})  * comparisons instead for keys. Likewise, if {@link #weakValues()} or  * {@link #softValues()} was specified, the map uses identity comparisons  * for values.  *  *<p>The returned map has<i>weakly consistent iteration</i>: an iterator  * over one of the map's view collections may reflect some, all or none of  * the changes made to the map after the iterator was created.  *  *<p>An entry whose key or value is reclaimed by the garbage collector  * immediately disappears from the map. (If the default settings of strong  * keys and strong values are used, this will never happen.) The client can  * never observe a partially-reclaimed entry. Any {@link java.util.Map.Entry}  * instance retrieved from the map's {@linkplain Map#entrySet() entry set}  * is a snapshot of that entry's state at the time of retrieval; such entries  * do, however, support {@link java.util.Map.Entry#setValue}.  *  *<p>{@code new MapMaker().weakKeys().makeMap()} can almost always be  * used as a drop-in replacement for {@link java.util.WeakHashMap}, adding  * concurrency, asynchronous cleanup, identity-based equality for keys, and  * great flexibility.  *  * @author Bob Lee  * @author Kevin Bourrillion  * @since 2 (imported from Google Collections Library)  */
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
DECL|field|UNSET_INITIAL_CAPACITY
specifier|private
specifier|static
specifier|final
name|int
name|UNSET_INITIAL_CAPACITY
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|UNSET_CONCURRENCY_LEVEL
specifier|private
specifier|static
specifier|final
name|int
name|UNSET_CONCURRENCY_LEVEL
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|UNSET_EXPIRATION_NANOS
specifier|static
specifier|final
name|int
name|UNSET_EXPIRATION_NANOS
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|UNSET_MAXIMUM_SIZE
specifier|static
specifier|final
name|int
name|UNSET_MAXIMUM_SIZE
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|initialCapacity
name|int
name|initialCapacity
init|=
name|UNSET_INITIAL_CAPACITY
decl_stmt|;
DECL|field|concurrencyLevel
name|int
name|concurrencyLevel
init|=
name|UNSET_CONCURRENCY_LEVEL
decl_stmt|;
DECL|field|maximumSize
name|int
name|maximumSize
init|=
name|UNSET_MAXIMUM_SIZE
decl_stmt|;
DECL|field|keyStrength
name|Strength
name|keyStrength
decl_stmt|;
DECL|field|valueStrength
name|Strength
name|valueStrength
decl_stmt|;
DECL|field|expirationNanos
name|long
name|expirationNanos
init|=
name|UNSET_EXPIRATION_NANOS
decl_stmt|;
DECL|field|useCustomMap
specifier|private
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
comment|// TODO: undo this indirection if keyEquiv gets released
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
literal|"key equivalence was already set to "
operator|+
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
comment|// TODO: undo this indirection if valueEquiv gets released
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
literal|"value equivalence was already set to "
operator|+
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
name|UNSET_INITIAL_CAPACITY
argument_list|,
literal|"initial capacity was already set to "
operator|+
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
name|UNSET_INITIAL_CAPACITY
operator|)
condition|?
name|DEFAULT_INITIAL_CAPACITY
else|:
name|initialCapacity
return|;
block|}
comment|/**    * Specifies the maximum number of entries the map may contain. While the    * number of entries in the map is not guaranteed to grow to the maximum,    * the map will attempt to make the best use of memory without exceeding the    * maximum number of entries. As the map size grows close to the maximum,    * the map will evict entries that are less likely to be used again. For    * example, the map may evict an entry because it hasn't been used recently    * or very often.    *    * @throws IllegalArgumentException if {@code size} is negative    * @throws IllegalStateException if a maximum size was already set    */
comment|// TODO: Implement and make public.
DECL|method|maximumSize (int size)
name|MapMaker
name|maximumSize
parameter_list|(
name|int
name|size
parameter_list|)
block|{
comment|// TODO: Should we disallow maximumSize< concurrencyLevel? If we allow it,
comment|// should we return a dummy map that doesn't actually retain any
comment|// entries?
name|checkState
argument_list|(
name|this
operator|.
name|maximumSize
operator|==
name|UNSET_MAXIMUM_SIZE
argument_list|,
literal|"maximum size was already set to "
operator|+
name|this
operator|.
name|maximumSize
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
name|UNSET_CONCURRENCY_LEVEL
argument_list|,
literal|"concurrency level was already set to "
operator|+
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
name|UNSET_CONCURRENCY_LEVEL
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
literal|"Key strength was already set to "
operator|+
name|keyStrength
operator|+
literal|"."
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
comment|/**    * Specifies that each value (not key) stored in the map should be    * wrapped in a {@link WeakReference} (by default, strong references    * are used).    *    *<p>Weak values will be garbage collected once they are weakly    * reachable. This makes them a poor candidate for caching; consider    * {@link #softValues()} instead.    *    *<p><b>Note:</b> the map will use identity ({@code ==}) comparison    * to determine equality of weak values. This will notably impact    * the behavior of {@link Map#containsValue(Object) containsValue},    * {@link ConcurrentMap#remove(Object, Object) remove(Object, Object)},    * and {@link ConcurrentMap#replace(Object, Object, Object) replace(K, V, V)}.    *    * @throws IllegalStateException if the key strength was already set    * @see WeakReference    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.lang.ref.WeakReference"
argument_list|)
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
literal|"Value strength was already set to "
operator|+
name|valueStrength
operator|+
literal|"."
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
comment|/**    * Specifies that each entry should be automatically removed from the    * map once a fixed duration has passed since the entry's creation.    * Note that changing the value of an entry will reset its expiration    * time.    *    * @param duration the length of time after an entry is created that it    *     should be automatically removed    * @param unit the unit that {@code duration} is expressed in    * @throws IllegalArgumentException if {@code duration} is not positive    * @throws IllegalStateException if the expiration time was already set    */
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
name|checkState
argument_list|(
name|expirationNanos
operator|==
name|UNSET_EXPIRATION_NANOS
argument_list|,
literal|"expiration time of "
operator|+
name|expirationNanos
operator|+
literal|" ns was already set"
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|duration
operator|>
literal|0
argument_list|,
literal|"invalid duration: "
operator|+
name|duration
argument_list|)
expr_stmt|;
name|this
operator|.
name|expirationNanos
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
DECL|method|getExpirationNanos ()
name|long
name|getExpirationNanos
parameter_list|()
block|{
return|return
operator|(
name|expirationNanos
operator|==
name|UNSET_EXPIRATION_NANOS
operator|)
condition|?
name|DEFAULT_EXPIRATION_NANOS
else|:
name|expirationNanos
return|;
block|}
comment|/**    * Builds a map, without on-demand computation of values. This method    * does not alter the state of this {@code MapMaker} instance, so it can be    * invoked again to create multiple independent maps.    *    * @return a serializable concurrent map having the requested features    */
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
argument_list|,
literal|null
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
comment|/**    * Builds a map, without on-demand computation of values. This method    * does not alter the state of this {@code MapMaker} instance, so it can be    * invoked again to create multiple independent maps.    *    *<p>The returned map will invoke the supplied listener each time it evicts    * an entry, whether it does so due to timed expiration, exceeding the    * maximum size, or discovering that the key or value has been reclaimed by    * the garbage collector. The returned map will invoke this listener    * synchronously, during invocations of any of that map's public methods    * (even read-only methods). The listener will<i>not</i> be invoked on manual    * removal.    *    * As the listener will be invoked on a caller's thread,    * operations that are expensive or may throw exceptions should be performed    * asynchronously.    *    * @param listener the listener to be notified of eviction events    * @return a serializable concurrent map having the requested features    */
comment|// TODO: do generics magic to set the eviction listener outside of make
annotation|@
name|Beta
DECL|method|makeMap ( MapEvictionListener<K, V> listener)
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makeMap
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
return|return
operator|new
name|CustomConcurrentHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|this
argument_list|,
name|listener
argument_list|)
return|;
block|}
comment|/**    * Builds a caching function, which either returns an already-computed value    * for a given key or atomically computes it using the supplied function.    * If another thread is currently computing the value for this key, simply    * waits for that thread to finish and returns its computed value. Note that    * the function may be executed concurrently by multiple threads, but only for    * distinct keys.    *    *<p>The {@code Map} view of the {@code Cache}'s cache is only    * updated when function computation completes. In other words, an entry isn't    * visible until the value's computation completes. No methods on the {@code    * Map} will ever trigger computation.    *    *<p>{@link Cache#apply} in the returned function implementation may    * throw:    *    *<ul>    *<li>{@link NullPointerException} if the key is null or the    *     computing function returns null    *<li>{@link ComputationException} if an exception was thrown by the    *     computing function. If that exception is already of type {@link    *     ComputationException} it is propagated directly; otherwise it is    *     wrapped.    *</ul>    *    *<p>If {@link Map#put} is called on the underlying map before a computation    * completes, other threads waiting on the computation will wake up and return    * the stored value. When the computation completes, its new result will    * overwrite the value that was put in the map manually.    *    *<p>This method does not alter the state of this {@code MapMaker} instance,    * so it can be invoked again to create multiple independent maps.    *    * @param computingFunction the function used to compute new values    * @return a serializable cache having the requested features    */
comment|// TODO: figure out the Cache interface before making this public
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
literal|null
argument_list|,
name|computingFunction
argument_list|)
return|;
block|}
comment|/**    * Builds a caching function, which either returns an already-computed value    * for a given key or atomically computes it using the supplied function.    * If another thread is currently computing the value for this key, simply    * waits for that thread to finish and returns its computed value. Note that    * the function may be executed concurrently by multiple threads, but only for    * distinct keys.    *    *<p>The {@code Map} view of the {@code Cache}'s cache is only    * updated when function computation completes. In other words, an entry isn't    * visible until the value's computation completes. No methods on the {@code    * Map} will ever trigger computation.    *    *<p>{@link Cache#apply} in the returned function implementation may    * throw:    *    *<ul>    *<li>{@link NullPointerException} if the key is null or the    *     computing function returns null    *<li>{@link ComputationException} if an exception was thrown by the    *     computing function. If that exception is already of type {@link    *     ComputationException} it is propagated directly; otherwise it is    *     wrapped.    *</ul>    *    *<p>If {@link Map#put} is called on the underlying map before a computation    * completes, other threads waiting on the computation will wake up and return    * the stored value. When the computation completes, its new result will    * overwrite the value that was put in the map manually.    *    *<p>The returned map will invoke the supplied listener each time it evicts    * an entry, whether it does so due to timed expiration, exceeding the    * maximum size, or discovering that the key or value has been reclaimed by    * the garbage collector. The returned map will invoke this listener    * synchronously, during invocations of any of that map's public methods    * (even read-only methods). The listener will<i>not</i> be invoked on manual    * removal.    *    *<p>This method does not alter the state of this {@code MapMaker} instance,    * so it can be invoked again to create multiple independent maps.    *    * @param computingFunction the function used to compute new values    * @param listener the listener to be notified of eviction events    * @return a serializable cache having the requested features    */
comment|// TODO: figure out the Cache interface before making this public
annotation|@
name|Beta
DECL|method|makeCache ( Function<? super K, ? extends V> computingFunction, MapEvictionListener<K, V> listener)
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
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
parameter_list|,
name|MapEvictionListener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|listener
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
name|listener
argument_list|,
name|computingFunction
argument_list|)
return|;
block|}
comment|/**    * Builds a map that supports atomic, on-demand computation of values. {@link    * Map#get} either returns an already-computed value for the given key,    * atomically computes it using the supplied function, or, if another thread    * is currently computing the value for this key, simply waits for that thread    * to finish and returns its computed value. Note that the function may be    * executed concurrently by multiple threads, but only for distinct keys.    *    *<p>If an entry's value has not finished computing yet, query methods    * besides {@code get} return immediately as if an entry doesn't exist. In    * other words, an entry isn't externally visible until the value's    * computation completes.    *    *<p>{@link Map#get} on the returned map will never return {@code null}. It    * may throw:    *    *<ul>    *<li>{@link NullPointerException} if the key is null or the computing    *     function returns null    *<li>{@link ComputationException} if an exception was thrown by the    *     computing function. If that exception is already of type {@link    *     ComputationException} it is propagated directly; otherwise it is    *     wrapped.    *</ul>    *    *<p><b>Note:</b> Callers of {@code get}<i>must</i> ensure that the key    * argument is of type {@code K}. The {@code get} method accepts {@code    * Object}, so the key type is not checked at compile time. Passing an object    * of a type other than {@code K} can result in that object being unsafely    * passed to the computing function as type {@code K}, and unsafely stored in    * the map.    *    *<p>If {@link Map#put} is called before a computation completes, other    * threads waiting on the computation will wake up and return the stored    * value. When the computation completes, its new result will overwrite the    * value that was put in the map manually.    *    *<p>This method does not alter the state of this {@code MapMaker} instance,    * so it can be invoked again to create multiple independent maps.    *    * @param computingFunction the function used to compute new values    * @return a serializable concurrent map having the requested features    */
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
argument_list|,
literal|null
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
comment|/**    * A function which caches the result of each application (computation). This    * interface does not specify the caching semantics, but does expose a {@code    * ConcurrentMap} view of cached entries.    *    * @author Bob Lee    */
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

