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
comment|/**  * A semi-persistent mapping from keys to values. Values are automatically loaded by the cache,  * and are stored in the cache until either evicted or manually invalidated.  *  *<p>All methods other than {@link #get} and {@link #getUnchecked} are optional.  *  *<p>When evaluated as a {@link Function}, a cache yields the same result as invoking {@link  * #getUnchecked}.  *  * @author Charles Fry  * @since 10.0  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|Cache
specifier|public
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
comment|/**    * Returns the value associated with {@code key} in this cache, first loading that value if    * necessary. No observable state associated with this cache is modified until loading completes.    *    * @throws ExecutionException if a checked exception was thrown while loading the value    * @throws UncheckedExecutionException if an unchecked exception was thrown while loading the    *     value    * @throws ExecutionError if an error was thrown while loading the value    */
DECL|method|get (K key)
name|V
name|get
parameter_list|(
name|K
name|key
parameter_list|)
throws|throws
name|ExecutionException
function_decl|;
comment|/**    * Returns the value associated with {@code key} in this cache, first loading that value if    * necessary. No observable state associated with this cache is modified until computation    * completes. Unlike {@link #get}, this method does not throw a checked exception, and thus should    * only be used in situations where checked exceptions are not thrown by the cache loader.    *    *<p><b>Warning:</b> this method silently converts checked exceptions to unchecked exceptions,    * and should not be used with cache loaders which throw checked exceptions.    *    * @throws UncheckedExecutionException if an exception was thrown while loading the value,    *     regardless of whether the exception was checked or unchecked    * @throws ExecutionError if an error was thrown while loading the value    */
DECL|method|getUnchecked (K key)
name|V
name|getUnchecked
parameter_list|(
name|K
name|key
parameter_list|)
function_decl|;
comment|/**    * Returns the value associated with {@code key} in this cache, obtaining that value from    * {@code valueLoader} if necessary. No observable state associated with this cache is modified    * until loading completes.    *    *<p>This method functions identically to {@link #get(Object)}, simply using a different    * mechanism to load the value if missing. This method provides a simple substitute for the    * conventional "if cached, return; otherwise create, cache and return" pattern.    *    *<p><b>Warning:</b> as with {@link CacheLoader#load}, {@code valueLoader}<b>must not</b> return    * {@code null}; it may either return a non-null value or throw an exception.    *    * @throws ExecutionException if a checked exception was thrown while loading the value    * @throws UncheckedExecutionException if an unchecked exception was thrown while loading the    *     value    * @throws ExecutionError if an error was thrown while loading the value    * @throws UnsupportedOperationException if this operation is not supported by the cache    *     implementation    */
DECL|method|get (K key, Callable<V> valueLoader)
name|V
name|get
parameter_list|(
name|K
name|key
parameter_list|,
name|Callable
argument_list|<
name|V
argument_list|>
name|valueLoader
parameter_list|)
throws|throws
name|ExecutionException
function_decl|;
comment|/**    * Returns a map of the values associated with {@code keys}, creating or retrieving those values    * if necessary. The returned map contains entries that were already cached, combined with newly    * loaded entries; it will never contain null keys or values.    *    *<p>Caches loaded by a {@link CacheLoader} will issue a single request to    * {@link CacheLoader#loadAll} for all keys which are not already present in the cache. All    * entries returned by {@link CacheLoader#loadAll} will be stored in the cache, over-writing    * any previously cached values. This method will throw an exception if    * {@link CacheLoader#loadAll} returns {@code null}, returns a map containing null keys or values,    * or fails to return an entry for each requested key.    *    *<p>Note that duplicate elements in {@code keys}, as determined by {@link Object#equals}, will    * be ignored.    *    * @throws ExecutionException if a checked exception was thrown while loading the values    * @throws UncheckedExecutionException if an unchecked exception was thrown while loading the    *     values    * @throws ExecutionError if an error was thrown while loading the values    * @since 11.0    */
DECL|method|getAll (Iterable<? extends K> keys)
name|Map
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
function_decl|;
comment|/**    * Discouraged. Provided to satisfy the {@code Function} interface; use {@link #get} or    * {@link #getUnchecked} instead.    *    * @throws UncheckedExecutionException if an exception was thrown while loading the value,    *     regardless of whether the exception was checked or unchecked    * @throws ExecutionError if an error was thrown while loading the value    */
annotation|@
name|Override
DECL|method|apply (K key)
name|V
name|apply
parameter_list|(
name|K
name|key
parameter_list|)
function_decl|;
comment|/**    * Loads a new value for key {@code key}. While the new value is loading the previous value (if    * any) will continue to be returned by {@code get(key)} unless it is evicted. If the new    * value is loaded succesfully it will replace the previous value in the cache; if an exception is    * thrown while refreshing the previous value will remain.    *    * @throws UnsupportedOperationException if this operation is not supported by the cache    *     implementation    * @throws ExecutionException if a checked exception was thrown while refreshing the entry    * @throws UncheckedExecutionException if an unchecked exception was thrown while refreshing the    *     entry    * @throws ExecutionError if an error was thrown while refreshing the entry    * @since 11.0    */
DECL|method|refresh (K key)
name|void
name|refresh
parameter_list|(
name|K
name|key
parameter_list|)
throws|throws
name|ExecutionException
function_decl|;
comment|/**    * Discards any cached value for key {@code key}, possibly asynchronously, so that a future    * invocation of {@code get(key)} will result in a cache miss and reload.    *    * @throws UnsupportedOperationException if this operation is not supported by the cache    *     implementation    */
DECL|method|invalidate (Object key)
name|void
name|invalidate
parameter_list|(
name|Object
name|key
parameter_list|)
function_decl|;
comment|/**    * Discards any cached values for keys {@code keys}, possibly asynchronously, so that a future    * invocation of {@code get(key)} for any of those keys will result in a cache miss and reload.    *    * @throws UnsupportedOperationException if this operation is not supported by the cache    *     implementation    */
DECL|method|invalidateAll (Iterable<?> keys)
name|void
name|invalidateAll
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|keys
parameter_list|)
function_decl|;
comment|/**    * Discards all entries in the cache, possibly asynchronously.    *    * @throws UnsupportedOperationException if this operation is not supported by the cache    *     implementation    */
DECL|method|invalidateAll ()
name|void
name|invalidateAll
parameter_list|()
function_decl|;
comment|/**    * Returns the approximate number of entries in this cache.    *    * @throws UnsupportedOperationException if this operation is not supported by the cache    *     implementation    */
DECL|method|size ()
name|long
name|size
parameter_list|()
function_decl|;
comment|/**    * Returns a current snapshot of this cache's cumulative statistics. All stats are initialized    * to zero, and are monotonically increasing over the lifetime of the cache.    *    * @throws UnsupportedOperationException if this operation is not supported by the cache    *     implementation    */
DECL|method|stats ()
name|CacheStats
name|stats
parameter_list|()
function_decl|;
comment|/**    * Returns a view of the entries stored in this cache as a thread-safe map. Assume that none of    * the returned map's optional operations will be implemented, unless otherwise specified.    *    *<p>Operations on the returned map will never cause new values to be loaded into the cache. So,    * unlike {@link #get} and {@link #getUnchecked}, this map's {@link Map#get get} method will    * always return {@code null} for a key that is not already cached.    *    * @throws UnsupportedOperationException if this operation is not supported by the cache    *     implementation    */
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
comment|/**    * Performs any pending maintenance operations needed by the cache. Exactly which activities are    * performed -- if any -- is implementation-dependent.    */
DECL|method|cleanUp ()
name|void
name|cleanUp
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

