begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A semi-persistent mapping from keys to values. Values are automatically created by the cache as  * a function of the keys, and are stored in the cache until either evicted or manually invalidated.  *  *<p>All methods other than {@link #get} are optional.  *  *<p>When evaluated as a {@link Function}, a cache yields the same result as invoking {@link #get}.  *  * @author Charles Fry  * @since Guava release 10  */
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
comment|/**    * Returns the value associated with the given key, creating or retrieving that value if    * necessary. No state associated with this cache is modified until computation completes.    *    *<p>The implementation may support {@code null} as a valid cached value, or may return {@code    * null} without caching it, or may not permit null results at all.    *    *<p>This method is identical to {@link #getChecked} except that exceptions which occur during    * cache loading are unchecked.    *    * @throws NullPointerException if the specified key is null and this cache does not permit null    *     keys (optional)    * @throws ComputationException wraps errors which occur while loading the response    */
DECL|method|get (@ullable K key)
annotation|@
name|Nullable
name|V
name|get
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|)
function_decl|;
comment|/**    * Returns the value associated with the given key, creating or retrieving that value if    * necessary. No state associated with this cache is modified until computation completes.    *    *<p>The implementation may support {@code null} as a valid cached value, or may return {@code    * null} without caching it, or may not permit null results at all.    *    *<p>This method is identical to {@link #get} except that exceptions which occur during cache    * loading are checked.    *    * @throws NullPointerException if the specified key is null and this cache does not permit null    *     keys (optional)    * @throws ExecutionException wraps errors which occur while loading the response    */
DECL|method|getChecked (@ullable K key)
annotation|@
name|Nullable
name|V
name|getChecked
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|)
throws|throws
name|ExecutionException
function_decl|;
comment|/**    * Provided to satisfy the {@code Function} interface; use {@link #get} instead.    *    * @deprecated Use {@link #get} instead.    */
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|apply (@ullable K key)
annotation|@
name|Nullable
name|V
name|apply
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|)
function_decl|;
comment|// TODO(user): add bulk operations
comment|/**    * Discards the cached value for key {@code key}, if it exists, so that the next invocation of    * {@code get(key)} will result in a cache miss and re-creation.    *    * @throws UnsupportedOperationException if this operation is not supported by the cache    *     implementation    * @throws NullPointerException if the specified key is null and this cache does not permit null    *     keys (optional)    */
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
comment|/**    * Returns the approximate number of entries in this cache. If the cache contains more than {@code    * Integer.MAX_VALUE} elements, returns {@code Integer.MAX_VALUE}.    *    * @throws UnsupportedOperationException if this operation is not supported by the cache    *     implementation    */
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
comment|/**    * Returns a current snapshot of this cache's cumulative statistics. All stats are initialized    * to zero, and are monotonically increasing over the lifetime of the cache.    *    * @throws UnsupportedOperationException if this operation is not supported by the cache    *     implementation    */
DECL|method|stats ()
name|CacheStats
name|stats
parameter_list|()
function_decl|;
comment|/**    * Returns a list of immutable copies of this cache's most active entries, approximately ordered    * from least likely to be evicted to most likely to be evicted.    *    * @param limit the maximum number of entries to return    * @throws UnsupportedOperationException if this operation is not supported by the cache    *     implementation    */
DECL|method|activeEntries (int limit)
name|ImmutableList
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|activeEntries
parameter_list|(
name|int
name|limit
parameter_list|)
function_decl|;
comment|/**    * Returns a view of the entries stored in this cache as a thread-safe map. Assume that none of    * the returned map's optional operations will be implemented, unless specified otherwise.    *    *<p>Operations on the returned map will never trigger a computation. So, unlike {@link    * Cache#get}, this map's {@link Map#get get} method will just return {@code null} immediately for    * a key that is not already cached.    *    * @throws UnsupportedOperationException if this operation is not supported by the cache    *     implementation    */
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
end_interface

end_unit

