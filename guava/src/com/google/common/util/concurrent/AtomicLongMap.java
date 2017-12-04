begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.util.concurrent
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
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
name|atomic
operator|.
name|AtomicBoolean
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
name|atomic
operator|.
name|AtomicLong
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
name|LongBinaryOperator
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
name|LongUnaryOperator
import|;
end_import

begin_comment
comment|/**  * A map containing {@code long} values that can be atomically updated. While writes to a  * traditional {@code Map} rely on {@code put(K, V)}, the typical mechanism for writing to this map  * is {@code addAndGet(K, long)}, which adds a {@code long} to the value currently associated with  * {@code K}. If a key has not yet been associated with a value, its implicit value is zero.  *  *<p>Most methods in this class treat absent values and zero values identically, as individually  * documented. Exceptions to this are {@link #containsKey}, {@link #size}, {@link #isEmpty}, {@link  * #asMap}, and {@link #toString}.  *  *<p>Instances of this class may be used by multiple threads concurrently. All operations are  * atomic unless otherwise noted.  *  *<p><b>Note:</b> If your values are always positive and less than 2^31, you may wish to use a  * {@link com.google.common.collect.Multiset} such as {@link  * com.google.common.collect.ConcurrentHashMultiset} instead.  *  *<p><b>Warning:</b> Unlike {@code Multiset}, entries whose values are zero are not automatically  * removed from the map. Instead they must be removed manually with {@link #removeAllZeros}.  *  * @author Charles Fry  * @since 11.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AtomicLongMap
specifier|public
specifier|final
class|class
name|AtomicLongMap
parameter_list|<
name|K
parameter_list|>
implements|implements
name|Serializable
block|{
DECL|field|map
specifier|private
specifier|final
name|ConcurrentHashMap
argument_list|<
name|K
argument_list|,
name|Long
argument_list|>
name|map
decl_stmt|;
DECL|method|AtomicLongMap (ConcurrentHashMap<K, Long> map)
specifier|private
name|AtomicLongMap
parameter_list|(
name|ConcurrentHashMap
argument_list|<
name|K
argument_list|,
name|Long
argument_list|>
name|map
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|checkNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
comment|/** Creates an {@code AtomicLongMap}. */
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|>
name|AtomicLongMap
argument_list|<
name|K
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|AtomicLongMap
argument_list|<
name|K
argument_list|>
argument_list|(
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
argument_list|)
return|;
block|}
comment|/** Creates an {@code AtomicLongMap} with the same mappings as the specified {@code Map}. */
DECL|method|create (Map<? extends K, ? extends Long> m)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|>
name|AtomicLongMap
argument_list|<
name|K
argument_list|>
name|create
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|Long
argument_list|>
name|m
parameter_list|)
block|{
name|AtomicLongMap
argument_list|<
name|K
argument_list|>
name|result
init|=
name|create
argument_list|()
decl_stmt|;
name|result
operator|.
name|putAll
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**    * Returns the value associated with {@code key}, or zero if there is no value associated with    * {@code key}.    */
DECL|method|get (K key)
specifier|public
name|long
name|get
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|map
operator|.
name|getOrDefault
argument_list|(
name|key
argument_list|,
literal|0L
argument_list|)
return|;
block|}
comment|/**    * Increments by one the value currently associated with {@code key}, and returns the new value.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|incrementAndGet (K key)
specifier|public
name|long
name|incrementAndGet
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|addAndGet
argument_list|(
name|key
argument_list|,
literal|1
argument_list|)
return|;
block|}
comment|/**    * Decrements by one the value currently associated with {@code key}, and returns the new value.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|decrementAndGet (K key)
specifier|public
name|long
name|decrementAndGet
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|addAndGet
argument_list|(
name|key
argument_list|,
operator|-
literal|1
argument_list|)
return|;
block|}
comment|/**    * Adds {@code delta} to the value currently associated with {@code key}, and returns the new    * value.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addAndGet (K key, long delta)
specifier|public
name|long
name|addAndGet
parameter_list|(
name|K
name|key
parameter_list|,
name|long
name|delta
parameter_list|)
block|{
return|return
name|accumulateAndGet
argument_list|(
name|key
argument_list|,
name|delta
argument_list|,
name|Long
operator|::
name|sum
argument_list|)
return|;
block|}
comment|/**    * Increments by one the value currently associated with {@code key}, and returns the old value.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|getAndIncrement (K key)
specifier|public
name|long
name|getAndIncrement
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|getAndAdd
argument_list|(
name|key
argument_list|,
literal|1
argument_list|)
return|;
block|}
comment|/**    * Decrements by one the value currently associated with {@code key}, and returns the old value.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|getAndDecrement (K key)
specifier|public
name|long
name|getAndDecrement
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|getAndAdd
argument_list|(
name|key
argument_list|,
operator|-
literal|1
argument_list|)
return|;
block|}
comment|/**    * Adds {@code delta} to the value currently associated with {@code key}, and returns the old    * value.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|getAndAdd (K key, long delta)
specifier|public
name|long
name|getAndAdd
parameter_list|(
name|K
name|key
parameter_list|,
name|long
name|delta
parameter_list|)
block|{
return|return
name|getAndAccumulate
argument_list|(
name|key
argument_list|,
name|delta
argument_list|,
name|Long
operator|::
name|sum
argument_list|)
return|;
block|}
comment|/**    * Updates the value currently associated with {@code key} with the specified function, and    * returns the new value. If there is not currently a value associated with {@code key}, the    * function is applied to {@code 0L}.    *    * @since 21.0    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|updateAndGet (K key, LongUnaryOperator updaterFunction)
specifier|public
name|long
name|updateAndGet
parameter_list|(
name|K
name|key
parameter_list|,
name|LongUnaryOperator
name|updaterFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|updaterFunction
argument_list|)
expr_stmt|;
return|return
name|map
operator|.
name|compute
argument_list|(
name|key
argument_list|,
parameter_list|(
name|k
parameter_list|,
name|value
parameter_list|)
lambda|->
name|updaterFunction
operator|.
name|applyAsLong
argument_list|(
operator|(
name|value
operator|==
literal|null
operator|)
condition|?
literal|0L
else|:
name|value
operator|.
name|longValue
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Updates the value currently associated with {@code key} with the specified function, and    * returns the old value. If there is not currently a value associated with {@code key}, the    * function is applied to {@code 0L}.    *    * @since 21.0    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|getAndUpdate (K key, LongUnaryOperator updaterFunction)
specifier|public
name|long
name|getAndUpdate
parameter_list|(
name|K
name|key
parameter_list|,
name|LongUnaryOperator
name|updaterFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|updaterFunction
argument_list|)
expr_stmt|;
name|AtomicLong
name|holder
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
name|map
operator|.
name|compute
argument_list|(
name|key
argument_list|,
parameter_list|(
name|k
parameter_list|,
name|value
parameter_list|)
lambda|->
block|{
name|long
name|oldValue
init|=
operator|(
name|value
operator|==
literal|null
operator|)
condition|?
literal|0L
else|:
name|value
operator|.
name|longValue
argument_list|()
decl_stmt|;
name|holder
operator|.
name|set
argument_list|(
name|oldValue
argument_list|)
expr_stmt|;
return|return
name|updaterFunction
operator|.
name|applyAsLong
argument_list|(
name|oldValue
argument_list|)
return|;
block|}
argument_list|)
expr_stmt|;
return|return
name|holder
operator|.
name|get
argument_list|()
return|;
block|}
comment|/**    * Updates the value currently associated with {@code key} by combining it with {@code x} via the    * specified accumulator function, returning the new value. The previous value associated with    * {@code key} (or zero, if there is none) is passed as the first argument to {@code    * accumulatorFunction}, and {@code x} is passed as the second argument.    *    * @since 21.0    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|accumulateAndGet (K key, long x, LongBinaryOperator accumulatorFunction)
specifier|public
name|long
name|accumulateAndGet
parameter_list|(
name|K
name|key
parameter_list|,
name|long
name|x
parameter_list|,
name|LongBinaryOperator
name|accumulatorFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|accumulatorFunction
argument_list|)
expr_stmt|;
return|return
name|updateAndGet
argument_list|(
name|key
argument_list|,
name|oldValue
lambda|->
name|accumulatorFunction
operator|.
name|applyAsLong
argument_list|(
name|oldValue
argument_list|,
name|x
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Updates the value currently associated with {@code key} by combining it with {@code x} via the    * specified accumulator function, returning the old value. The previous value associated with    * {@code key} (or zero, if there is none) is passed as the first argument to {@code    * accumulatorFunction}, and {@code x} is passed as the second argument.    *    * @since 21.0    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|getAndAccumulate (K key, long x, LongBinaryOperator accumulatorFunction)
specifier|public
name|long
name|getAndAccumulate
parameter_list|(
name|K
name|key
parameter_list|,
name|long
name|x
parameter_list|,
name|LongBinaryOperator
name|accumulatorFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|accumulatorFunction
argument_list|)
expr_stmt|;
return|return
name|getAndUpdate
argument_list|(
name|key
argument_list|,
name|oldValue
lambda|->
name|accumulatorFunction
operator|.
name|applyAsLong
argument_list|(
name|oldValue
argument_list|,
name|x
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Associates {@code newValue} with {@code key} in this map, and returns the value previously    * associated with {@code key}, or zero if there was no such value.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|put (K key, long newValue)
specifier|public
name|long
name|put
parameter_list|(
name|K
name|key
parameter_list|,
name|long
name|newValue
parameter_list|)
block|{
return|return
name|getAndUpdate
argument_list|(
name|key
argument_list|,
name|x
lambda|->
name|newValue
argument_list|)
return|;
block|}
comment|/**    * Copies all of the mappings from the specified map to this map. The effect of this call is    * equivalent to that of calling {@code put(k, v)} on this map once for each mapping from key    * {@code k} to value {@code v} in the specified map. The behavior of this operation is undefined    * if the specified map is modified while the operation is in progress.    */
DECL|method|putAll (Map<? extends K, ? extends Long> m)
specifier|public
name|void
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
name|Long
argument_list|>
name|m
parameter_list|)
block|{
name|m
operator|.
name|forEach
argument_list|(
name|this
operator|::
name|put
argument_list|)
expr_stmt|;
block|}
comment|/**    * Removes and returns the value associated with {@code key}. If {@code key} is not in the map,    * this method has no effect and returns zero.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|remove (K key)
specifier|public
name|long
name|remove
parameter_list|(
name|K
name|key
parameter_list|)
block|{
name|Long
name|result
init|=
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
literal|0L
else|:
name|result
operator|.
name|longValue
argument_list|()
return|;
block|}
comment|/**    * Atomically remove {@code key} from the map iff its associated value is 0.    *    * @since 20.0    */
annotation|@
name|Beta
annotation|@
name|CanIgnoreReturnValue
DECL|method|removeIfZero (K key)
specifier|public
name|boolean
name|removeIfZero
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|remove
argument_list|(
name|key
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**    * Removes all mappings from this map whose values are zero.    *    *<p>This method is not atomic: the map may be visible in intermediate states, where some of the    * zero values have been removed and others have not.    */
DECL|method|removeAllZeros ()
specifier|public
name|void
name|removeAllZeros
parameter_list|()
block|{
name|map
operator|.
name|values
argument_list|()
operator|.
name|removeIf
argument_list|(
name|x
lambda|->
name|x
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns the sum of all values in this map.    *    *<p>This method is not atomic: the sum may or may not include other concurrent operations.    */
DECL|method|sum ()
specifier|public
name|long
name|sum
parameter_list|()
block|{
return|return
name|map
operator|.
name|values
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|mapToLong
argument_list|(
name|Long
operator|::
name|longValue
argument_list|)
operator|.
name|sum
argument_list|()
return|;
block|}
DECL|field|asMap
specifier|private
specifier|transient
name|Map
argument_list|<
name|K
argument_list|,
name|Long
argument_list|>
name|asMap
decl_stmt|;
comment|/** Returns a live, read-only view of the map backing this {@code AtomicLongMap}. */
DECL|method|asMap ()
specifier|public
name|Map
argument_list|<
name|K
argument_list|,
name|Long
argument_list|>
name|asMap
parameter_list|()
block|{
name|Map
argument_list|<
name|K
argument_list|,
name|Long
argument_list|>
name|result
init|=
name|asMap
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|asMap
operator|=
name|createAsMap
argument_list|()
else|:
name|result
return|;
block|}
DECL|method|createAsMap ()
specifier|private
name|Map
argument_list|<
name|K
argument_list|,
name|Long
argument_list|>
name|createAsMap
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|map
argument_list|)
return|;
block|}
comment|/** Returns true if this map contains a mapping for the specified key. */
DECL|method|containsKey (Object key)
specifier|public
name|boolean
name|containsKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|map
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**    * Returns the number of key-value mappings in this map. If the map contains more than {@code    * Integer.MAX_VALUE} elements, returns {@code Integer.MAX_VALUE}.    */
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|map
operator|.
name|size
argument_list|()
return|;
block|}
comment|/** Returns {@code true} if this map contains no key-value mappings. */
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|map
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/**    * Removes all of the mappings from this map. The map will be empty after this call returns.    *    *<p>This method is not atomic: the map may not be empty after returning if there were concurrent    * writes.    */
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
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
name|map
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**    * If {@code key} is not already associated with a value or if {@code key} is associated with    * zero, associate it with {@code newValue}. Returns the previous value associated with {@code    * key}, or zero if there was no mapping for {@code key}.    */
DECL|method|putIfAbsent (K key, long newValue)
name|long
name|putIfAbsent
parameter_list|(
name|K
name|key
parameter_list|,
name|long
name|newValue
parameter_list|)
block|{
name|AtomicBoolean
name|noValue
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|Long
name|result
init|=
name|map
operator|.
name|compute
argument_list|(
name|key
argument_list|,
parameter_list|(
name|k
parameter_list|,
name|oldValue
parameter_list|)
lambda|->
block|{
if|if
condition|(
name|oldValue
operator|==
literal|null
operator|||
name|oldValue
operator|==
literal|0
condition|)
block|{
name|noValue
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|newValue
return|;
block|}
else|else
block|{
return|return
name|oldValue
return|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|noValue
operator|.
name|get
argument_list|()
condition|?
literal|0L
else|:
name|result
operator|.
name|longValue
argument_list|()
return|;
block|}
comment|/**    * If {@code (key, expectedOldValue)} is currently in the map, this method replaces {@code    * expectedOldValue} with {@code newValue} and returns true; otherwise, this method returns false.    *    *<p>If {@code expectedOldValue} is zero, this method will succeed if {@code (key, zero)} is    * currently in the map, or if {@code key} is not in the map at all.    */
DECL|method|replace (K key, long expectedOldValue, long newValue)
name|boolean
name|replace
parameter_list|(
name|K
name|key
parameter_list|,
name|long
name|expectedOldValue
parameter_list|,
name|long
name|newValue
parameter_list|)
block|{
if|if
condition|(
name|expectedOldValue
operator|==
literal|0L
condition|)
block|{
return|return
name|putIfAbsent
argument_list|(
name|key
argument_list|,
name|newValue
argument_list|)
operator|==
literal|0L
return|;
block|}
else|else
block|{
return|return
name|map
operator|.
name|replace
argument_list|(
name|key
argument_list|,
name|expectedOldValue
argument_list|,
name|newValue
argument_list|)
return|;
block|}
block|}
comment|/**    * If {@code (key, value)} is currently in the map, this method removes it and returns true;    * otherwise, this method returns false.    */
DECL|method|remove (K key, long value)
name|boolean
name|remove
parameter_list|(
name|K
name|key
parameter_list|,
name|long
name|value
parameter_list|)
block|{
return|return
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

