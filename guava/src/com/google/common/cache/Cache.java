begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|errorprone
operator|.
name|annotations
operator|.
name|CompatibleWith
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
comment|/**  * A semi-persistent mapping from keys to values. Cache entries are manually added using {@link  * #get(Object, Callable)} or {@link #put(Object, Object)}, and are stored in the cache until either  * evicted or manually invalidated. The common way to build instances is using {@link CacheBuilder}.  *  *<p>Implementations of this interface are expected to be thread-safe, and can be safely accessed  * by multiple concurrent threads.  *  * @author Charles Fry  * @since 10.0  */
end_comment

begin_interface
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
block|{
comment|/**    * Returns the value associated with {@code key} in this cache, or {@code null} if there is no    * cached value for {@code key}.    *    * @since 11.0    */
annotation|@
name|Nullable
DECL|method|getIfPresent (@ompatibleWithR) Object key)
name|V
name|getIfPresent
parameter_list|(
annotation|@
name|CompatibleWith
argument_list|(
literal|"K"
argument_list|)
name|Object
name|key
parameter_list|)
function_decl|;
comment|/**    * Returns the value associated with {@code key} in this cache, obtaining that value from {@code    * loader} if necessary. The method improves upon the conventional "if cached, return; otherwise    * create, cache and return" pattern. For further improvements, use {@link LoadingCache} and its    * {@link LoadingCache#get(Object) get(K)} method instead of this one.    *    *<p>Among the improvements that this method and {@code LoadingCache.get(K)} both provide are:    *    *<ul>    *<li>{@linkplain LoadingCache#get(Object) awaiting the result of a pending load} rather than    *       starting a redundant one    *<li>eliminating the error-prone caching boilerplate    *<li>tracking load {@linkplain #stats statistics}    *</ul>    *    *<p>Among the further improvements that {@code LoadingCache} can provide but this method cannot:    *    *<ul>    *<li>consolidation of the loader logic to {@linkplain CacheBuilder#build(CacheLoader) a single    *       authoritative location}    *<li>{@linkplain LoadingCache#refresh refreshing of entries}, including {@linkplain    *       CacheBuilder#refreshAfterWrite automated refreshing}    *<li>{@linkplain LoadingCache#getAll bulk loading requests}, including {@linkplain    *       CacheLoader#loadAll bulk loading implementations}    *</ul>    *    *<p><b>Warning:</b> For any given key, every {@code loader} used with it should compute the same    * value. Otherwise, a call that passes one {@code loader} may return the result of another call    * with a differently behaving {@code loader}. For example, a call that requests a short timeout    * for an RPC may wait for a similar call that requests a long timeout, or a call by an    * unprivileged user may return a resource accessible only to a privileged user making a similar    * call. To prevent this problem, create a key object that includes all values that affect the    * result of the query. Or use {@code LoadingCache.get(K)}, which lacks the ability to refer to    * state other than that in the key.    *    *<p><b>Warning:</b> as with {@link CacheLoader#load}, {@code loader}<b>must not</b> return    * {@code null}; it may either return a non-null value or throw an exception.    *    *<p>No observable state associated with this cache is modified until loading completes.    *    * @throws ExecutionException if a checked exception was thrown while loading the value    * @throws UncheckedExecutionException if an unchecked exception was thrown while loading the    *     value    * @throws ExecutionError if an error was thrown while loading the value    * @since 11.0    */
DECL|method|get (K key, Callable<? extends V> loader)
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
name|loader
parameter_list|)
throws|throws
name|ExecutionException
function_decl|;
comment|/**    * Returns a map of the values associated with {@code keys} in this cache. The returned map will    * only contain entries which are already present in the cache.    *    * @since 11.0    */
DECL|method|getAllPresent (Iterable<?> keys)
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
function_decl|;
comment|/**    * Associates {@code value} with {@code key} in this cache. If the cache previously contained a    * value associated with {@code key}, the old value is replaced by {@code value}.    *    *<p>Prefer {@link #get(Object, Callable)} when using the conventional "if cached, return;    * otherwise create, cache and return" pattern.    *    * @since 11.0    */
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
comment|/**    * Copies all of the mappings from the specified map to the cache. The effect of this call is    * equivalent to that of calling {@code put(k, v)} on this map once for each mapping from key    * {@code k} to value {@code v} in the specified map. The behavior of this operation is undefined    * if the specified map is modified while the operation is in progress.    *    * @since 12.0    */
DECL|method|putAll (Map<? extends K, ? extends V> m)
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
function_decl|;
comment|/** Discards any cached value for key {@code key}. */
DECL|method|invalidate (@ompatibleWithR) Object key)
name|void
name|invalidate
parameter_list|(
annotation|@
name|CompatibleWith
argument_list|(
literal|"K"
argument_list|)
name|Object
name|key
parameter_list|)
function_decl|;
comment|/**    * Discards any cached values for keys {@code keys}.    *    * @since 11.0    */
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
comment|/** Discards all entries in the cache. */
DECL|method|invalidateAll ()
name|void
name|invalidateAll
parameter_list|()
function_decl|;
comment|/** Returns the approximate number of entries in this cache. */
DECL|method|size ()
name|long
name|size
parameter_list|()
function_decl|;
comment|/**    * Returns a current snapshot of this cache's cumulative statistics, or a set of default values if    * the cache is not recording statistics. All statistics begin at zero and never decrease over the    * lifetime of the cache.    *    *<p><b>Warning:</b> this cache may not be recording statistical data. For example, a cache    * created using {@link CacheBuilder} only does so if the {@link CacheBuilder#recordStats} method    * was called. If statistics are not being recorded, a {@code CacheStats} instance with zero for    * all values is returned.    *    */
DECL|method|stats ()
name|CacheStats
name|stats
parameter_list|()
function_decl|;
comment|/**    * Returns a view of the entries stored in this cache as a thread-safe map. Modifications made to    * the map directly affect the cache.    *    *<p>Iterators from the returned map are at least<i>weakly consistent</i>: they are safe for    * concurrent use, but if the cache is modified (including by eviction) after the iterator is    * created, it is undefined which of the changes (if any) will be reflected in that iterator.    */
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

