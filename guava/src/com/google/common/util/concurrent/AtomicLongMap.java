begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2011 Google Inc. All Rights Reserved.
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
name|collect
operator|.
name|Maps
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
name|AtomicLong
import|;
end_import

begin_comment
comment|/**  * A map containing {@code long} values that can be atomically updated. While writes to a  * traditional {@code Map} rely on {@code put(K, V)}, the typical mechanism for writing to this map  * is {@code addAndGet(K, long)}, which adds a {@code long} to the value currently associated with  * {@code K}. If a key has not yet been associated with a value, its implicit value is zero.  *  *<p>Most methods in this class treat absent values and zero values identically, as individually  * documented. Exceptions to this are {@link #containsKey}, {@link #size}, {@link #isEmpty},  * {@link #asMap}, and {@link #toString}.  *  *<p>Instances of this class may be used by multiple threads concurrently. All operations are  * atomic unless otherwise noted.  *  *<p>Unlike {@link com.google.common.collect.Multiset}, values may be negative, and zeroes are  * not automatically removed.  *  * @author Charles Fry  * @since 11.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|AtomicLongMap
specifier|public
specifier|final
class|class
name|AtomicLongMap
parameter_list|<
name|K
parameter_list|>
block|{
DECL|field|map
specifier|private
specifier|final
name|ConcurrentHashMap
argument_list|<
name|K
argument_list|,
name|AtomicLong
argument_list|>
name|map
decl_stmt|;
DECL|method|AtomicLongMap (ConcurrentHashMap<K, AtomicLong> map)
specifier|private
name|AtomicLongMap
parameter_list|(
name|ConcurrentHashMap
argument_list|<
name|K
argument_list|,
name|AtomicLong
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
comment|/**    * Creates an {@code AtomicLongMap}.    */
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
argument_list|<
name|K
argument_list|,
name|AtomicLong
argument_list|>
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Creates an {@code AtomicLongMap} with the same mappings as the specified {@code Map}.    */
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
name|AtomicLong
name|atomic
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|atomic
operator|==
literal|null
condition|?
literal|0L
else|:
name|atomic
operator|.
name|get
argument_list|()
return|;
block|}
comment|/**    * Increments by one the value currently associated with {@code key}, and returns the new value.    */
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
name|outer
label|:
for|for
control|(
init|;
condition|;
control|)
block|{
name|AtomicLong
name|atomic
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|atomic
operator|==
literal|null
condition|)
block|{
name|atomic
operator|=
name|map
operator|.
name|putIfAbsent
argument_list|(
name|key
argument_list|,
operator|new
name|AtomicLong
argument_list|(
name|delta
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|atomic
operator|==
literal|null
condition|)
block|{
return|return
name|delta
return|;
block|}
comment|// atomic is now non-null; fall through
block|}
for|for
control|(
init|;
condition|;
control|)
block|{
name|long
name|oldValue
init|=
name|atomic
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldValue
operator|==
literal|0L
condition|)
block|{
comment|// don't compareAndSet a zero
if|if
condition|(
name|map
operator|.
name|replace
argument_list|(
name|key
argument_list|,
name|atomic
argument_list|,
operator|new
name|AtomicLong
argument_list|(
name|delta
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|delta
return|;
block|}
comment|// atomic replaced
continue|continue
name|outer
continue|;
block|}
name|long
name|newValue
init|=
name|oldValue
operator|+
name|delta
decl_stmt|;
if|if
condition|(
name|atomic
operator|.
name|compareAndSet
argument_list|(
name|oldValue
argument_list|,
name|newValue
argument_list|)
condition|)
block|{
return|return
name|newValue
return|;
block|}
comment|// value changed
block|}
block|}
block|}
comment|/**    * Increments by one the value currently associated with {@code key}, and returns the old value.    */
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
name|outer
label|:
for|for
control|(
init|;
condition|;
control|)
block|{
name|AtomicLong
name|atomic
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|atomic
operator|==
literal|null
condition|)
block|{
name|atomic
operator|=
name|map
operator|.
name|putIfAbsent
argument_list|(
name|key
argument_list|,
operator|new
name|AtomicLong
argument_list|(
name|delta
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|atomic
operator|==
literal|null
condition|)
block|{
return|return
literal|0L
return|;
block|}
comment|// atomic is now non-null; fall through
block|}
for|for
control|(
init|;
condition|;
control|)
block|{
name|long
name|oldValue
init|=
name|atomic
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldValue
operator|==
literal|0L
condition|)
block|{
comment|// don't compareAndSet a zero
if|if
condition|(
name|map
operator|.
name|replace
argument_list|(
name|key
argument_list|,
name|atomic
argument_list|,
operator|new
name|AtomicLong
argument_list|(
name|delta
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|0L
return|;
block|}
comment|// atomic replaced
continue|continue
name|outer
continue|;
block|}
name|long
name|newValue
init|=
name|oldValue
operator|+
name|delta
decl_stmt|;
if|if
condition|(
name|atomic
operator|.
name|compareAndSet
argument_list|(
name|oldValue
argument_list|,
name|newValue
argument_list|)
condition|)
block|{
return|return
name|oldValue
return|;
block|}
comment|// value changed
block|}
block|}
block|}
comment|/**    * Associates {@code newValue} with {@code key} in this map, and returns the value previously    * associated with {@code key}, or zero if there was no such value.    */
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
name|outer
label|:
for|for
control|(
init|;
condition|;
control|)
block|{
name|AtomicLong
name|atomic
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|atomic
operator|==
literal|null
condition|)
block|{
name|atomic
operator|=
name|map
operator|.
name|putIfAbsent
argument_list|(
name|key
argument_list|,
operator|new
name|AtomicLong
argument_list|(
name|newValue
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|atomic
operator|==
literal|null
condition|)
block|{
return|return
literal|0L
return|;
block|}
comment|// atomic is now non-null; fall through
block|}
for|for
control|(
init|;
condition|;
control|)
block|{
name|long
name|oldValue
init|=
name|atomic
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldValue
operator|==
literal|0L
condition|)
block|{
comment|// don't compareAndSet a zero
if|if
condition|(
name|map
operator|.
name|replace
argument_list|(
name|key
argument_list|,
name|atomic
argument_list|,
operator|new
name|AtomicLong
argument_list|(
name|newValue
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|0L
return|;
block|}
comment|// atomic replaced
continue|continue
name|outer
continue|;
block|}
if|if
condition|(
name|atomic
operator|.
name|compareAndSet
argument_list|(
name|oldValue
argument_list|,
name|newValue
argument_list|)
condition|)
block|{
return|return
name|oldValue
return|;
block|}
comment|// value changed
block|}
block|}
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
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|Long
argument_list|>
name|entry
range|:
name|m
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
block|}
comment|/**    * Removes and returns the value associated with {@code key}. If {@code key} is not    * in the map, this method has no effect and returns zero.    */
DECL|method|remove (K key)
specifier|public
name|long
name|remove
parameter_list|(
name|K
name|key
parameter_list|)
block|{
name|AtomicLong
name|atomic
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|atomic
operator|==
literal|null
condition|)
block|{
return|return
literal|0L
return|;
block|}
for|for
control|(
init|;
condition|;
control|)
block|{
name|long
name|oldValue
init|=
name|atomic
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldValue
operator|==
literal|0L
operator|||
name|atomic
operator|.
name|compareAndSet
argument_list|(
name|oldValue
argument_list|,
literal|0L
argument_list|)
condition|)
block|{
comment|// only remove after setting to zero, to avoid concurrent updates
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|,
name|atomic
argument_list|)
expr_stmt|;
comment|// succeed even if the remove fails, since the value was already adjusted
return|return
name|oldValue
return|;
block|}
block|}
block|}
comment|/**    * Removes all mappings from this map whose values are zero.    *    *<p>This method is not atomic: the map may be visible in intermediate states, where some    * of the zero values have been removed and others have not.    */
DECL|method|removeAllZeros ()
specifier|public
name|void
name|removeAllZeros
parameter_list|()
block|{
for|for
control|(
name|K
name|key
range|:
name|map
operator|.
name|keySet
argument_list|()
control|)
block|{
name|AtomicLong
name|atomic
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|atomic
operator|!=
literal|null
operator|&&
name|atomic
operator|.
name|get
argument_list|()
operator|==
literal|0L
condition|)
block|{
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|,
name|atomic
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Returns the sum of all values in this map.    *    *<p>This method is not atomic: the sum may or may not include other concurrent operations.    */
DECL|method|sum ()
specifier|public
name|long
name|sum
parameter_list|()
block|{
name|long
name|sum
init|=
literal|0L
decl_stmt|;
for|for
control|(
name|AtomicLong
name|value
range|:
name|map
operator|.
name|values
argument_list|()
control|)
block|{
name|sum
operator|=
name|sum
operator|+
name|value
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
return|return
name|sum
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
comment|/**    * Returns a live, read-only view of the map backing this {@code AtomicLongMap}.    */
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
name|Maps
operator|.
name|transformValues
argument_list|(
name|map
argument_list|,
operator|new
name|Function
argument_list|<
name|AtomicLong
argument_list|,
name|Long
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Long
name|apply
parameter_list|(
name|AtomicLong
name|atomic
parameter_list|)
block|{
return|return
name|atomic
operator|.
name|get
argument_list|()
return|;
block|}
block|}
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns true if this map contains a mapping for the specified key.    */
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
comment|/**    * Returns the number of key-value mappings in this map. If the map contains more than    * {@code Integer.MAX_VALUE} elements, returns {@code Integer.MAX_VALUE}.    */
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
comment|/**    * Returns {@code true} if this map contains no key-value mappings.    */
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
comment|/*    * ConcurrentMap operations which we may eventually add.    *    * The problem with these is that remove(K, long) has to be done in two phases by definition ---    * first decrementing to zero, and then removing. putIfAbsent or replace could observe the    * intermediate zero-state. Ways we could deal with this are:    *    * - Don't define any of the ConcurrentMap operations. This is the current state of affairs.    *    * - Define putIfAbsent and replace as treating zero and absent identically (as currently    *   implemented below). This is a bit surprising with putIfAbsent, which really becomes    *   putIfZero.    *    * - Allow putIfAbsent and replace to distinguish between zero and absent, but don't implement    *   remove(K, long). Without any two-phase operations it becomes feasible for all remaining    *   operations to distinguish between zero and absent. If we do this, then perhaps we should add    *   replace(key, long).    *    * - Introduce a special-value private static final AtomicLong that would have the meaning of    *   removal-in-progress, and rework all operations to properly distinguish between zero and    *   absent.    */
comment|/**    * If {@code key} is not already associated with a value or if {@code key} is associated with    * zero, associate it with {@code newValue}. Returns the previous value associated with    * {@code key}, or zero if there was no mapping for {@code key}.    */
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
for|for
control|(
init|;
condition|;
control|)
block|{
name|AtomicLong
name|atomic
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|atomic
operator|==
literal|null
condition|)
block|{
name|atomic
operator|=
name|map
operator|.
name|putIfAbsent
argument_list|(
name|key
argument_list|,
operator|new
name|AtomicLong
argument_list|(
name|newValue
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|atomic
operator|==
literal|null
condition|)
block|{
return|return
literal|0L
return|;
block|}
comment|// atomic is now non-null; fall through
block|}
name|long
name|oldValue
init|=
name|atomic
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldValue
operator|==
literal|0L
condition|)
block|{
comment|// don't compareAndSet a zero
if|if
condition|(
name|map
operator|.
name|replace
argument_list|(
name|key
argument_list|,
name|atomic
argument_list|,
operator|new
name|AtomicLong
argument_list|(
name|newValue
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|0L
return|;
block|}
comment|// atomic replaced
continue|continue;
block|}
return|return
name|oldValue
return|;
block|}
block|}
comment|/**    * If {@code (key, expectedOldValue)} is currently in the map, this method replaces    * {@code expectedOldValue} with {@code newValue} and returns true; otherwise, this method    * returns false.    *    *<p>If {@code expectedOldValue} is zero, this method will succeed if {@code (key, zero)}    * is currently in the map, or if {@code key} is not in the map at all.    */
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
name|AtomicLong
name|atomic
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|atomic
operator|==
literal|null
operator|)
condition|?
literal|false
else|:
name|atomic
operator|.
name|compareAndSet
argument_list|(
name|expectedOldValue
argument_list|,
name|newValue
argument_list|)
return|;
block|}
block|}
comment|/**    * If {@code (key, value)} is currently in the map, this method removes it and returns    * true; otherwise, this method returns false.    */
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
name|AtomicLong
name|atomic
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|atomic
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|long
name|oldValue
init|=
name|atomic
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldValue
operator|!=
name|value
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|oldValue
operator|==
literal|0L
operator|||
name|atomic
operator|.
name|compareAndSet
argument_list|(
name|oldValue
argument_list|,
literal|0L
argument_list|)
condition|)
block|{
comment|// only remove after setting to zero, to avoid concurrent updates
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|,
name|atomic
argument_list|)
expr_stmt|;
comment|// succeed even if the remove fails, since the value was already adjusted
return|return
literal|true
return|;
block|}
comment|// value changed
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

