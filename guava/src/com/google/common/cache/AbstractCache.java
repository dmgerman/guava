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
name|Callable
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

begin_comment
comment|/**  * This class provides a skeletal implementation of the {@code Cache} interface to minimize the  * effort required to implement this interface.  *  *<p>To implement a cache, the programmer needs only to extend this class and provide an  * implementation for the {@link #put} and {@link #getIfPresent} methods. {@link #getAllPresent} is  * implemented in terms of {@link #getIfPresent}; {@link #putAll} is implemented in terms of  * {@link #put}, {@link #invalidateAll(Iterable)} is implemented in terms of {@link #invalidate}.  * The method {@link #cleanUp} is a no-op. All other methods throw an  * {@link UnsupportedOperationException}.  *  * @author Charles Fry  * @since 10.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|AbstractCache
specifier|public
specifier|abstract
class|class
name|AbstractCache
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|AbstractCache ()
specifier|protected
name|AbstractCache
parameter_list|()
block|{}
comment|/**    * @since 11.0    */
annotation|@
name|Override
DECL|method|get (K key, Callable<? extends V> valueLoader)
specifier|public
name|V
name|get
parameter_list|(
name|K
name|key
parameter_list|,
name|Callable
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|valueLoader
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
comment|/**    * This implementation of {@code getAllPresent} lacks any insight into the internal cache data    * structure, and is thus forced to return the query keys instead of the cached keys. This is only    * possible with an unsafe cast which requires {@code keys} to actually be of type {@code K}.    *    * {@inheritDoc}    *    * @since 11.0    */
annotation|@
name|Override
DECL|method|getAllPresent (Iterable<?> keys)
specifier|public
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getAllPresent
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|keys
parameter_list|)
block|{
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|result
init|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|keys
control|)
block|{
if|if
condition|(
operator|!
name|result
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|K
name|castKey
init|=
operator|(
name|K
operator|)
name|key
decl_stmt|;
name|result
operator|.
name|put
argument_list|(
name|castKey
argument_list|,
name|getIfPresent
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|result
argument_list|)
return|;
block|}
comment|/**    * @since 11.0    */
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * @since 12.0    */
annotation|@
name|Override
DECL|method|putAll (Map<? extends K, ? extends V> m)
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
name|V
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
name|V
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
annotation|@
name|Override
DECL|method|cleanUp ()
specifier|public
name|void
name|cleanUp
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|long
name|size
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * @since 11.0    */
annotation|@
name|Override
DECL|method|invalidateAll (Iterable<?> keys)
specifier|public
name|void
name|invalidateAll
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|keys
parameter_list|)
block|{
for|for
control|(
name|Object
name|key
range|:
name|keys
control|)
block|{
name|invalidate
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|invalidateAll ()
specifier|public
name|void
name|invalidateAll
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|stats ()
specifier|public
name|CacheStats
name|stats
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Accumulates statistics during the operation of a {@link Cache} for presentation by {@link    * Cache#stats}. This is solely intended for consumption by {@code Cache} implementors.    *    * @since 10.0    */
annotation|@
name|Beta
DECL|interface|StatsCounter
specifier|public
interface|interface
name|StatsCounter
block|{
comment|/**      * Records cache hits. This should be called when a cache request returns a cached value.      *      * @param count the number of hits to record      * @since 11.0      */
DECL|method|recordHits (int count)
specifier|public
name|void
name|recordHits
parameter_list|(
name|int
name|count
parameter_list|)
function_decl|;
comment|/**      * Records cache misses. This should be called when a cache request returns a value that was      * not found in the cache. This method should be called by the loading thread, as well as by      * threads blocking on the load. Multiple concurrent calls to {@link Cache} lookup methods with      * the same key on an absent value should result in a single call to either      * {@code recordLoadSuccess} or {@code recordLoadException} and multiple calls to this method,      * despite all being served by the results of a single load operation.      *      * @param count the number of misses to record      * @since 11.0      */
DECL|method|recordMisses (int count)
specifier|public
name|void
name|recordMisses
parameter_list|(
name|int
name|count
parameter_list|)
function_decl|;
comment|/**      * Records the successful load of a new entry. This should be called when a cache request      * causes an entry to be loaded, and the loading completes successfully. In contrast to      * {@link #recordMisses}, this method should only be called by the loading thread.      *      * @param loadTime the number of nanoseconds the cache spent computing or retrieving the new      *     value      */
DECL|method|recordLoadSuccess (long loadTime)
specifier|public
name|void
name|recordLoadSuccess
parameter_list|(
name|long
name|loadTime
parameter_list|)
function_decl|;
comment|/**      * Records the failed load of a new entry. This should be called when a cache request causes      * an entry to be loaded, but an exception is thrown while loading the entry. In contrast to      * {@link #recordMisses}, this method should only be called by the loading thread.      *      * @param loadTime the number of nanoseconds the cache spent computing or retrieving the new      *     value prior to an exception being thrown      */
DECL|method|recordLoadException (long loadTime)
specifier|public
name|void
name|recordLoadException
parameter_list|(
name|long
name|loadTime
parameter_list|)
function_decl|;
comment|/**      * Records the eviction of an entry from the cache. This should only been called when an entry      * is evicted due to the cache's eviction strategy, and not as a result of manual {@linkplain      * Cache#invalidate invalidations}.      */
DECL|method|recordEviction ()
specifier|public
name|void
name|recordEviction
parameter_list|()
function_decl|;
comment|/**      * Returns a snapshot of this counter's values. Note that this may be an inconsistent view, as      * it may be interleaved with update operations.      */
DECL|method|snapshot ()
specifier|public
name|CacheStats
name|snapshot
parameter_list|()
function_decl|;
block|}
comment|/**    * A thread-safe {@link StatsCounter} implementation for use by {@link Cache} implementors.    *    * @since 10.0    */
annotation|@
name|Beta
DECL|class|SimpleStatsCounter
specifier|public
specifier|static
class|class
name|SimpleStatsCounter
implements|implements
name|StatsCounter
block|{
DECL|field|hitCount
specifier|private
specifier|final
name|LongAdder
name|hitCount
init|=
operator|new
name|LongAdder
argument_list|()
decl_stmt|;
DECL|field|missCount
specifier|private
specifier|final
name|LongAdder
name|missCount
init|=
operator|new
name|LongAdder
argument_list|()
decl_stmt|;
DECL|field|loadSuccessCount
specifier|private
specifier|final
name|LongAdder
name|loadSuccessCount
init|=
operator|new
name|LongAdder
argument_list|()
decl_stmt|;
DECL|field|loadExceptionCount
specifier|private
specifier|final
name|LongAdder
name|loadExceptionCount
init|=
operator|new
name|LongAdder
argument_list|()
decl_stmt|;
DECL|field|totalLoadTime
specifier|private
specifier|final
name|LongAdder
name|totalLoadTime
init|=
operator|new
name|LongAdder
argument_list|()
decl_stmt|;
DECL|field|evictionCount
specifier|private
specifier|final
name|LongAdder
name|evictionCount
init|=
operator|new
name|LongAdder
argument_list|()
decl_stmt|;
comment|/**      * @since 11.0      */
annotation|@
name|Override
DECL|method|recordHits (int count)
specifier|public
name|void
name|recordHits
parameter_list|(
name|int
name|count
parameter_list|)
block|{
name|hitCount
operator|.
name|add
argument_list|(
name|count
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 11.0      */
annotation|@
name|Override
DECL|method|recordMisses (int count)
specifier|public
name|void
name|recordMisses
parameter_list|(
name|int
name|count
parameter_list|)
block|{
name|missCount
operator|.
name|add
argument_list|(
name|count
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|recordLoadSuccess (long loadTime)
specifier|public
name|void
name|recordLoadSuccess
parameter_list|(
name|long
name|loadTime
parameter_list|)
block|{
name|loadSuccessCount
operator|.
name|increment
argument_list|()
expr_stmt|;
name|totalLoadTime
operator|.
name|add
argument_list|(
name|loadTime
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|recordLoadException (long loadTime)
specifier|public
name|void
name|recordLoadException
parameter_list|(
name|long
name|loadTime
parameter_list|)
block|{
name|loadExceptionCount
operator|.
name|increment
argument_list|()
expr_stmt|;
name|totalLoadTime
operator|.
name|add
argument_list|(
name|loadTime
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|recordEviction ()
specifier|public
name|void
name|recordEviction
parameter_list|()
block|{
name|evictionCount
operator|.
name|increment
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|snapshot ()
specifier|public
name|CacheStats
name|snapshot
parameter_list|()
block|{
return|return
operator|new
name|CacheStats
argument_list|(
name|hitCount
operator|.
name|sum
argument_list|()
argument_list|,
name|missCount
operator|.
name|sum
argument_list|()
argument_list|,
name|loadSuccessCount
operator|.
name|sum
argument_list|()
argument_list|,
name|loadExceptionCount
operator|.
name|sum
argument_list|()
argument_list|,
name|totalLoadTime
operator|.
name|sum
argument_list|()
argument_list|,
name|evictionCount
operator|.
name|sum
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Increments all counters by the values in {@code other}.      */
DECL|method|incrementBy (StatsCounter other)
specifier|public
name|void
name|incrementBy
parameter_list|(
name|StatsCounter
name|other
parameter_list|)
block|{
name|CacheStats
name|otherStats
init|=
name|other
operator|.
name|snapshot
argument_list|()
decl_stmt|;
name|hitCount
operator|.
name|add
argument_list|(
name|otherStats
operator|.
name|hitCount
argument_list|()
argument_list|)
expr_stmt|;
name|missCount
operator|.
name|add
argument_list|(
name|otherStats
operator|.
name|missCount
argument_list|()
argument_list|)
expr_stmt|;
name|loadSuccessCount
operator|.
name|add
argument_list|(
name|otherStats
operator|.
name|loadSuccessCount
argument_list|()
argument_list|)
expr_stmt|;
name|loadExceptionCount
operator|.
name|add
argument_list|(
name|otherStats
operator|.
name|loadExceptionCount
argument_list|()
argument_list|)
expr_stmt|;
name|totalLoadTime
operator|.
name|add
argument_list|(
name|otherStats
operator|.
name|totalLoadTime
argument_list|()
argument_list|)
expr_stmt|;
name|evictionCount
operator|.
name|add
argument_list|(
name|otherStats
operator|.
name|evictionCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

