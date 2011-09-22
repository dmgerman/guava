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
name|util
operator|.
name|concurrent
operator|.
name|UncheckedExecutionException
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
name|atomic
operator|.
name|AtomicLong
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
comment|/**  * This class provides a skeletal implementation of the {@code Cache} interface to minimize the  * effort required to implement this interface.  *  *<p>To implement a cache, the programmer needs only to extend this class and provide an  * implementation for the {@code get} method. This implementation throws an  * {@link UnsupportedOperationException} on calls to {@link #size}, {@link #invalidate},  * {@link #invalidateAll}, {@link #stats}, and {@link #asMap}. The methods  * {@link #getUnchecked} and {@link #apply} are implemented in terms of {@link #get}. The method  * {@link #cleanUp} is a no-op.  *  * @author Charles Fry  * @since 10.0  */
end_comment

begin_class
annotation|@
name|Beta
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
annotation|@
name|Override
annotation|@
name|Nullable
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
annotation|@
name|Nullable
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
DECL|method|invalidate (@ullable Object key)
specifier|public
name|void
name|invalidate
parameter_list|(
annotation|@
name|Nullable
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
comment|/**      * Records a single hit. This should be called when a cache request returns a cached value.      */
DECL|method|recordHit ()
specifier|public
name|void
name|recordHit
parameter_list|()
function_decl|;
comment|/**      * Records the successful load of a new entry. This should be called when a cache request      * causes an entry to be loaded, and the loading completes succesfully. In contrast to      * {@link #recordConcurrentMiss}, this method should only be called by the loading thread.      *      * @param loadTime the number of nanoseconds the cache spent computing or retrieving the new      *     value      */
DECL|method|recordLoadSuccess (long loadTime)
specifier|public
name|void
name|recordLoadSuccess
parameter_list|(
name|long
name|loadTime
parameter_list|)
function_decl|;
comment|/**      * Records the failed load of a new entry. This should be called when a cache request causes      * an entry to be loaded, but an exception is thrown while loading the entry. In contrast to      * {@link #recordConcurrentMiss}, this method should only be called by the loading thread.      *      * @param loadTime the number of nanoseconds the cache spent computing or retrieving the new      *     value prior to an exception being thrown      */
DECL|method|recordLoadException (long loadTime)
specifier|public
name|void
name|recordLoadException
parameter_list|(
name|long
name|loadTime
parameter_list|)
function_decl|;
comment|/**      * Records a single concurrent miss. This should be called when a cache request returns a      * value which was loaded by a different thread. In contrast to {@link #recordLoadSuccess}      * and {@link #recordLoadException}, this method should never be called by the loading      * thread. Multiple concurrent calls to {@link Cache} lookup methods with the same key on an      * absent value should result in a single call to either {@code recordLoadSuccess} or      * {@code recordLoadException} and multiple calls to this method, despite all being served by      * the results of a single load operation.      */
DECL|method|recordConcurrentMiss ()
specifier|public
name|void
name|recordConcurrentMiss
parameter_list|()
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
name|AtomicLong
name|hitCount
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|missCount
specifier|private
specifier|final
name|AtomicLong
name|missCount
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|loadSuccessCount
specifier|private
specifier|final
name|AtomicLong
name|loadSuccessCount
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|loadExceptionCount
specifier|private
specifier|final
name|AtomicLong
name|loadExceptionCount
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|totalLoadTime
specifier|private
specifier|final
name|AtomicLong
name|totalLoadTime
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|evictionCount
specifier|private
specifier|final
name|AtomicLong
name|evictionCount
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|recordHit ()
specifier|public
name|void
name|recordHit
parameter_list|()
block|{
name|hitCount
operator|.
name|incrementAndGet
argument_list|()
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
name|missCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|loadSuccessCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|totalLoadTime
operator|.
name|addAndGet
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
name|missCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|loadExceptionCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|totalLoadTime
operator|.
name|addAndGet
argument_list|(
name|loadTime
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|recordConcurrentMiss ()
specifier|public
name|void
name|recordConcurrentMiss
parameter_list|()
block|{
name|missCount
operator|.
name|incrementAndGet
argument_list|()
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
name|incrementAndGet
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
name|get
argument_list|()
argument_list|,
name|missCount
operator|.
name|get
argument_list|()
argument_list|,
name|loadSuccessCount
operator|.
name|get
argument_list|()
argument_list|,
name|loadExceptionCount
operator|.
name|get
argument_list|()
argument_list|,
name|totalLoadTime
operator|.
name|get
argument_list|()
argument_list|,
name|evictionCount
operator|.
name|get
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
name|addAndGet
argument_list|(
name|otherStats
operator|.
name|hitCount
argument_list|()
argument_list|)
expr_stmt|;
name|missCount
operator|.
name|addAndGet
argument_list|(
name|otherStats
operator|.
name|missCount
argument_list|()
argument_list|)
expr_stmt|;
name|loadSuccessCount
operator|.
name|addAndGet
argument_list|(
name|otherStats
operator|.
name|loadSuccessCount
argument_list|()
argument_list|)
expr_stmt|;
name|loadExceptionCount
operator|.
name|addAndGet
argument_list|(
name|otherStats
operator|.
name|loadExceptionCount
argument_list|()
argument_list|)
expr_stmt|;
name|totalLoadTime
operator|.
name|addAndGet
argument_list|(
name|otherStats
operator|.
name|totalLoadTime
argument_list|()
argument_list|)
expr_stmt|;
name|evictionCount
operator|.
name|addAndGet
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

