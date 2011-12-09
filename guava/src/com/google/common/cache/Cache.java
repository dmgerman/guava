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
comment|/**  * A semi-persistent mapping from keys to values. Cache entries are manually added using  * {@link #get(K, Callable)} or {@link #put(K, V)}, and are stored in the cache until either  * evicted or manually invalidated.  *  *<p>Implementations of this interface are expected to be thread-safe, and can be safely accessed  * by multiple concurrent threads.  *  *<p>All methods other than {@link #getIfPresent} are optional.  *  * @author Charles Fry  * @since 10.0  */
end_comment

begin_interface
annotation|@
name|Beta
annotation|@
name|GwtCompatible
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
comment|/**    * Returns the value associated with {@code key} in this cache, or {@code null} if there is no    * cached value for {@code key}.    */
annotation|@
name|Nullable
DECL|method|getIfPresent (K key)
name|V
name|getIfPresent
parameter_list|(
name|K
name|key
parameter_list|)
function_decl|;
comment|/**    * Returns the value associated with {@code key} in this cache, obtaining that value from    * {@code valueLoader} if necessary. No observable state associated with this cache is modified    * until loading completes. This method provides a simple substitute for the conventional    * "if cached, return; otherwise create, cache and return" pattern.    *    *<p><b>Warning:</b> as with {@link CacheLoader#load}, {@code valueLoader}<b>must not</b> return    * {@code null}; it may either return a non-null value or throw an exception.    *    * @throws ExecutionException if a checked exception was thrown while loading the value    * @throws UncheckedExecutionException if an unchecked exception was thrown while loading the    *     value    * @throws ExecutionError if an error was thrown while loading the value    */
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
comment|/**    * Returns a map of the values associated with {@code keys} in this cache. The returned map will    * only contain entries which are already present in the cache.    */
DECL|method|getAllPresent (Iterable<? extends K> keys)
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
extends|extends
name|K
argument_list|>
name|keys
parameter_list|)
function_decl|;
comment|/**    * Associates {@code value} with {@code key} in this cache. If the cache previously contained a    * value associated with {@code key}, the old value is replaced by {@code value}.    *    *<p>Prefer {@link #get(K, Callable)} when using the conventional "if cached, return; otherwise    * create, cache and return" pattern.    */
DECL|method|put (K key, V value)
name|void
name|put
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
function_decl|;
comment|/**    * Discards any cached value for key {@code key}.    */
DECL|method|invalidate (Object key)
name|void
name|invalidate
parameter_list|(
name|Object
name|key
parameter_list|)
function_decl|;
comment|/**    * Discards any cached values for keys {@code keys}.    */
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
comment|/**    * Discards all entries in the cache.    */
DECL|method|invalidateAll ()
name|void
name|invalidateAll
parameter_list|()
function_decl|;
comment|/**    * Returns the approximate number of entries in this cache.    */
DECL|method|size ()
name|long
name|size
parameter_list|()
function_decl|;
comment|/**    * Returns a current snapshot of this cache's cumulative statistics. All stats are initialized    * to zero, and are monotonically increasing over the lifetime of the cache.    */
DECL|method|stats ()
name|CacheStats
name|stats
parameter_list|()
function_decl|;
comment|/**    * Returns a view of the entries stored in this cache as a thread-safe map. Modifications made to    * the map directly affect the cache.    */
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
comment|/**    * Returns the value associated with {@code key} in this cache, first loading that value if    * necessary. No observable state associated with this cache is modified until loading completes.    *    * @throws ExecutionException if a checked exception was thrown while loading the value    * @throws UncheckedExecutionException if an unchecked exception was thrown while loading the    *     value    * @throws ExecutionError if an error was thrown while loading the value    * @deprecated This method has been split out into the {@link LoadingCache} interface, and will be    * removed in Guava release 12.0.    */
DECL|method|get (K key)
annotation|@
name|Deprecated
name|V
name|get
parameter_list|(
name|K
name|key
parameter_list|)
throws|throws
name|ExecutionException
function_decl|;
comment|/**    * Returns the value associated with {@code key} in this cache, first loading that value if    * necessary. No observable state associated with this cache is modified until computation    * completes. Unlike {@link #get}, this method does not throw a checked exception, and thus should    * only be used in situations where checked exceptions are not thrown by the cache loader.    *    *<p><b>Warning:</b> this method silently converts checked exceptions to unchecked exceptions,    * and should not be used with cache loaders which throw checked exceptions.    *    * @throws UncheckedExecutionException if an exception was thrown while loading the value,    *     regardless of whether the exception was checked or unchecked    * @throws ExecutionError if an error was thrown while loading the value    * @deprecated This method has been split out into the {@link LoadingCache} interface, and will be    * removed in Guava release 12.0.    */
DECL|method|getUnchecked (K key)
annotation|@
name|Deprecated
name|V
name|getUnchecked
parameter_list|(
name|K
name|key
parameter_list|)
function_decl|;
comment|/**    * Discouraged. Provided to satisfy the {@code Function} interface; use {@link #get} or    * {@link #getUnchecked} instead.    *    * @throws UncheckedExecutionException if an exception was thrown while loading the value,    *     regardless of whether the exception was checked or unchecked    * @throws ExecutionError if an error was thrown while loading the value    * @deprecated This method has been split out into the {@link LoadingCache} interface, and will be    * removed in Guava release 12.0.    */
DECL|method|apply (K key)
annotation|@
name|Deprecated
name|V
name|apply
parameter_list|(
name|K
name|key
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

