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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
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
comment|/**    * Returns the value associated with the given key, creating or retrieving that value if    * necessary, and throwing an execution exception on failure. No state associated with this cache    * is modified until loading completes.    *    *<p>The implementation may support {@code null} as a valid cached value, or may return {@code    * null} without caching it, or may not permit null results at all.    *    * @throws ExecutionException if a checked exception was thrown while loading the response    * @throws UncheckedExecutionException if an unchecked exception was thrown while loading the    *     response    * @throws ExecutionError if an error was thrown while loading the response    */
DECL|method|get (K key)
annotation|@
name|Nullable
name|V
name|get
parameter_list|(
name|K
name|key
parameter_list|)
throws|throws
name|ExecutionException
function_decl|;
comment|/**    * Returns the value associated with the given key, loading that value if necessary. No state    * associated with this cache is modified until computation completes. Unlike {@link #get}, this    * method does not throw a checked exception, and thus should only be used in situations where    * checked exceptions are not thrown by the cache loader.    *    *<p><b>Warning:</b> this method silently converts checked exceptions to unchecked exceptions.    * The {@link #get} method should be preferred for cache loaders which throw checked exceptions.    *    *<p>The implementation may support {@code null} as a valid cached value, or may return {@code    * null} without caching it, or may not permit null results at all.    *    * @throws UncheckedExecutionException if an exception was thrown while loading the response,    *     regardless of whether the exception was checked or unchecked    * @throws ExecutionError if an error was thrown while loading the response    */
DECL|method|getUnchecked (K key)
annotation|@
name|Nullable
name|V
name|getUnchecked
parameter_list|(
name|K
name|key
parameter_list|)
function_decl|;
comment|/**    * Discouraged. Provided to satisfy the {@code Function} interface; use {@link #get} or    * {@link #getUnchecked} instead.    *    * @throws UncheckedExecutionException if an exception was thrown while loading the response,    *     regardless of whether the exception was checked or unchecked    * @throws ExecutionError if an error was thrown while loading the response    */
annotation|@
name|Override
DECL|method|apply (K key)
annotation|@
name|Nullable
name|V
name|apply
parameter_list|(
name|K
name|key
parameter_list|)
function_decl|;
comment|// TODO(fry): add bulk operations
comment|/**    * Discards any cached value for key {@code key}, possibly asynchronously, so that a future    * invocation of {@code get(key)} will result in a cache miss and reload.    *    * @throws UnsupportedOperationException if this operation is not supported by the cache    *     implementation    */
DECL|method|invalidate (@ullable Object key)
name|void
name|invalidate
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
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

