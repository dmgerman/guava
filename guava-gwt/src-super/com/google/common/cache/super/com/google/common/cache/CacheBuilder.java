begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.cache
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
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
name|cache
operator|.
name|CacheLoader
operator|.
name|InvalidCacheLoadException
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
name|ImmutableMap
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
name|util
operator|.
name|concurrent
operator|.
name|ExecutionError
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
name|util
operator|.
name|concurrent
operator|.
name|UncheckedExecutionException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|Timer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|ExecutionException
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

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * CacheBuilder emulation.  *  * @author Charles Fry  */
end_comment

begin_comment
comment|// TODO(fry): eventually we should emmulate LocalCache instead of CacheBuilder
end_comment

begin_class
DECL|class|CacheBuilder
specifier|public
class|class
name|CacheBuilder
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
block|{
DECL|field|UNSET_INT
specifier|private
specifier|static
specifier|final
name|int
name|UNSET_INT
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|DEFAULT_INITIAL_CAPACITY
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_INITIAL_CAPACITY
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
DECL|field|initialCapacity
specifier|private
name|int
name|initialCapacity
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|concurrencyLevel
specifier|private
name|int
name|concurrencyLevel
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|expirationMillis
specifier|private
name|long
name|expirationMillis
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|maximumSize
specifier|private
name|int
name|maximumSize
init|=
operator|-
literal|1
decl_stmt|;
DECL|method|CacheBuilder ()
name|CacheBuilder
parameter_list|()
block|{}
DECL|method|newBuilder ()
specifier|public
specifier|static
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|newBuilder
parameter_list|()
block|{
return|return
operator|new
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
return|;
block|}
DECL|method|initialCapacity (int initialCapacity)
specifier|public
name|CacheBuilder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
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
specifier|private
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
DECL|method|concurrencyLevel (int concurrencyLevel)
specifier|public
name|CacheBuilder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
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
comment|// GWT technically only supports concurrencyLevel == 1, but we silently
comment|// ignore other positive values.
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
DECL|method|expireAfterWrite (long duration, TimeUnit unit)
specifier|public
name|CacheBuilder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|expireAfterWrite
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
name|expirationMillis
operator|==
name|UNSET_INT
argument_list|,
literal|"expireAfterWrite was already set to %s ms"
argument_list|,
name|expirationMillis
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|duration
operator|>=
literal|0
argument_list|,
literal|"duration cannot be negative: %s %s"
argument_list|,
name|duration
argument_list|,
name|unit
argument_list|)
expr_stmt|;
name|this
operator|.
name|expirationMillis
operator|=
name|unit
operator|.
name|toMillis
argument_list|(
name|duration
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getExpirationMillis ()
specifier|private
name|long
name|getExpirationMillis
parameter_list|()
block|{
return|return
operator|(
name|expirationMillis
operator|==
name|UNSET_INT
operator|)
condition|?
name|DEFAULT_EXPIRATION_NANOS
else|:
name|expirationMillis
return|;
block|}
DECL|method|maximumSize (int maximumSize)
specifier|public
name|CacheBuilder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|maximumSize
parameter_list|(
name|int
name|maximumSize
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|maximumSize
operator|!=
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"maximum size of "
operator|+
name|maximumSize
operator|+
literal|" was already set"
argument_list|)
throw|;
block|}
if|if
condition|(
name|maximumSize
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid maximum size: "
operator|+
name|maximumSize
argument_list|)
throw|;
block|}
name|this
operator|.
name|maximumSize
operator|=
name|maximumSize
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|build ()
specifier|public
parameter_list|<
name|K1
extends|extends
name|K
parameter_list|,
name|V1
extends|extends
name|V
parameter_list|>
name|Cache
argument_list|<
name|K1
argument_list|,
name|V1
argument_list|>
name|build
parameter_list|()
block|{
return|return
operator|new
name|LocalManualCache
argument_list|<
name|K1
argument_list|,
name|V1
argument_list|>
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|build ( CacheLoader<? super K1, V1> loader)
specifier|public
parameter_list|<
name|K1
extends|extends
name|K
parameter_list|,
name|V1
extends|extends
name|V
parameter_list|>
name|LoadingCache
argument_list|<
name|K1
argument_list|,
name|V1
argument_list|>
name|build
parameter_list|(
name|CacheLoader
argument_list|<
name|?
super|super
name|K1
argument_list|,
name|V1
argument_list|>
name|loader
parameter_list|)
block|{
return|return
operator|new
name|LocalLoadingCache
argument_list|<
name|K1
argument_list|,
name|V1
argument_list|>
argument_list|(
name|this
argument_list|,
name|loader
argument_list|)
return|;
block|}
DECL|class|LocalManualCache
specifier|private
specifier|static
class|class
name|LocalManualCache
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|localCache
specifier|final
name|LocalCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|localCache
decl_stmt|;
DECL|method|LocalManualCache (CacheBuilder<? super K, ? super V> builder)
name|LocalManualCache
parameter_list|(
name|CacheBuilder
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
super|super
name|V
argument_list|>
name|builder
parameter_list|)
block|{
name|this
argument_list|(
name|builder
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|LocalManualCache (CacheBuilder<? super K, ? super V> builder, CacheLoader<? super K, V> loader)
specifier|protected
name|LocalManualCache
parameter_list|(
name|CacheBuilder
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
super|super
name|V
argument_list|>
name|builder
parameter_list|,
name|CacheLoader
argument_list|<
name|?
super|super
name|K
argument_list|,
name|V
argument_list|>
name|loader
parameter_list|)
block|{
name|this
operator|.
name|localCache
operator|=
operator|new
name|LocalCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|builder
argument_list|,
name|loader
argument_list|)
expr_stmt|;
block|}
comment|// Cache methods
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|getIfPresent (K key)
specifier|public
name|V
name|getIfPresent
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|localCache
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|put (K key, V value)
specifier|public
name|void
name|put
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|localCache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|invalidate (Object key)
specifier|public
name|void
name|invalidate
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|localCache
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|invalidateAll ()
specifier|public
name|void
name|invalidateAll
parameter_list|()
block|{
name|localCache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|long
name|size
parameter_list|()
block|{
return|return
name|localCache
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|asMap ()
specifier|public
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|asMap
parameter_list|()
block|{
return|return
name|localCache
return|;
block|}
block|}
DECL|class|LocalLoadingCache
specifier|private
specifier|static
class|class
name|LocalLoadingCache
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|LocalManualCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|LoadingCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|LocalLoadingCache (CacheBuilder<? super K, ? super V> builder, CacheLoader<? super K, V> loader)
name|LocalLoadingCache
parameter_list|(
name|CacheBuilder
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
super|super
name|V
argument_list|>
name|builder
parameter_list|,
name|CacheLoader
argument_list|<
name|?
super|super
name|K
argument_list|,
name|V
argument_list|>
name|loader
parameter_list|)
block|{
name|super
argument_list|(
name|builder
argument_list|,
name|checkNotNull
argument_list|(
name|loader
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Cache methods
annotation|@
name|Override
DECL|method|get (K key)
specifier|public
name|V
name|get
parameter_list|(
name|K
name|key
parameter_list|)
throws|throws
name|ExecutionException
block|{
return|return
name|localCache
operator|.
name|getOrLoad
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getUnchecked (K key)
specifier|public
name|V
name|getUnchecked
parameter_list|(
name|K
name|key
parameter_list|)
block|{
try|try
block|{
return|return
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UncheckedExecutionException
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|apply (K key)
specifier|public
specifier|final
name|V
name|apply
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|getUnchecked
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getAll (Iterable<? extends K> keys)
specifier|public
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getAll
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|K
argument_list|>
name|keys
parameter_list|)
throws|throws
name|ExecutionException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|refresh (K key)
specifier|public
name|void
name|refresh
parameter_list|(
name|K
name|key
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
comment|// TODO(fry,user): ConcurrentHashMap never throws a CME when mutating the map during iteration, but
comment|// this implementation (based on a LHM) does. This will all be replaced soon anyways, so leaving
comment|// it as is for now.
DECL|class|LocalCache
specifier|private
specifier|static
class|class
name|LocalCache
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|LinkedHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|loader
specifier|private
specifier|final
name|CacheLoader
argument_list|<
name|?
super|super
name|K
argument_list|,
name|V
argument_list|>
name|loader
decl_stmt|;
DECL|field|expirationMillis
specifier|private
specifier|final
name|long
name|expirationMillis
decl_stmt|;
DECL|field|maximumSize
specifier|private
specifier|final
name|int
name|maximumSize
decl_stmt|;
DECL|method|LocalCache (CacheBuilder<? super K, ? super V> builder, CacheLoader<? super K, V> loader)
name|LocalCache
parameter_list|(
name|CacheBuilder
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
super|super
name|V
argument_list|>
name|builder
parameter_list|,
name|CacheLoader
argument_list|<
name|?
super|super
name|K
argument_list|,
name|V
argument_list|>
name|loader
parameter_list|)
block|{
name|super
argument_list|(
name|builder
operator|.
name|getInitialCapacity
argument_list|()
argument_list|,
literal|0.75f
argument_list|,
operator|(
name|builder
operator|.
name|maximumSize
operator|!=
name|UNSET_INT
operator|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|loader
operator|=
name|loader
expr_stmt|;
name|this
operator|.
name|expirationMillis
operator|=
name|builder
operator|.
name|getExpirationMillis
argument_list|()
expr_stmt|;
name|this
operator|.
name|maximumSize
operator|=
name|builder
operator|.
name|maximumSize
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|put (K key, V value)
specifier|public
name|V
name|put
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|V
name|result
init|=
name|super
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|expirationMillis
operator|>
literal|0
condition|)
block|{
name|scheduleRemoval
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|removeEldestEntry (Map.Entry<K, V> ignored)
specifier|protected
name|boolean
name|removeEldestEntry
parameter_list|(
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|ignored
parameter_list|)
block|{
return|return
operator|(
name|maximumSize
operator|==
operator|-
literal|1
operator|)
condition|?
literal|false
else|:
name|size
argument_list|()
operator|>
name|maximumSize
return|;
block|}
annotation|@
name|Override
DECL|method|putIfAbsent (K key, V value)
specifier|public
name|V
name|putIfAbsent
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|remove (Object key, Object value)
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|containsKey
argument_list|(
name|key
argument_list|)
operator|&&
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|replace (K key, V oldValue, V newValue)
specifier|public
name|boolean
name|replace
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|oldValue
parameter_list|,
name|V
name|newValue
parameter_list|)
block|{
if|if
condition|(
name|containsKey
argument_list|(
name|key
argument_list|)
operator|&&
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|equals
argument_list|(
name|oldValue
argument_list|)
condition|)
block|{
name|put
argument_list|(
name|key
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|replace (K key, V value)
specifier|public
name|V
name|replace
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
return|return
name|containsKey
argument_list|(
name|key
argument_list|)
condition|?
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|scheduleRemoval (final K key, final V value)
specifier|private
name|void
name|scheduleRemoval
parameter_list|(
specifier|final
name|K
name|key
parameter_list|,
specifier|final
name|V
name|value
parameter_list|)
block|{
comment|/*        * TODO: Keep weak reference to map, too. Build a priority queue out of the entries themselves        * instead of creating a task per entry. Then, we could have one recurring task per map (which        * would clean the entire map and then reschedule itself depending upon when the next        * expiration comes). We also want to avoid removing an entry prematurely if the entry was set        * to the same value again.        */
name|Timer
name|timer
init|=
operator|new
name|Timer
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|remove
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|timer
operator|.
name|schedule
argument_list|(
operator|(
name|int
operator|)
name|expirationMillis
argument_list|)
expr_stmt|;
block|}
DECL|method|getOrLoad (Object k)
specifier|public
name|V
name|getOrLoad
parameter_list|(
name|Object
name|k
parameter_list|)
throws|throws
name|ExecutionException
block|{
comment|// from CustomConcurrentHashMap
name|V
name|result
init|=
name|super
operator|.
name|get
argument_list|(
name|k
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
comment|/*          * This cast isn't safe, but we can rely on the fact that K is almost always passed to          * Map.get(), and tools like IDEs and Findbugs can catch situations where this isn't the          * case.          *          * The alternative is to add an overloaded method, but the chances of a user calling get()          * instead of the new API and the risks inherent in adding a new API outweigh this little          * hole.          */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|K
name|key
init|=
operator|(
name|K
operator|)
name|k
decl_stmt|;
name|result
operator|=
name|compute
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|compute (K key)
specifier|private
name|V
name|compute
parameter_list|(
name|K
name|key
parameter_list|)
throws|throws
name|ExecutionException
block|{
name|V
name|value
decl_stmt|;
try|try
block|{
name|value
operator|=
name|loader
operator|.
name|load
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UncheckedExecutionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ExecutionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ExecutionError
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|String
name|message
init|=
name|loader
operator|+
literal|" returned null for key "
operator|+
name|key
operator|+
literal|"."
decl_stmt|;
throw|throw
operator|new
name|InvalidCacheLoadException
argument_list|(
name|message
argument_list|)
throw|;
block|}
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
block|}
block|}
end_class

end_unit

