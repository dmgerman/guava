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
comment|/**  * A semi-persistent mapping from keys to values. Values are automatically loaded by the cache,  * and are stored in the cache until either evicted or manually invalidated.  *  *<p>Implementations of this interface are expected to be thread-safe, and can be safely accessed  * by multiple concurrent threads.  *  *<p>When evaluated as a {@link Function}, a cache yields the same result as invoking  * {@link #getUnchecked}.  *  * @author Charles Fry  * @since 11.0  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|LoadingCache
specifier|public
interface|interface
name|LoadingCache
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
extends|,
name|Function
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/**    * Returns the value associated with {@code key} in this cache, first loading that value if    * necessary. No observable state associated with this cache is modified until loading completes.    *    *<p>If another call to {@link #get} or {@link #getUnchecked} is currently loading the value for    * {@code key}, simply waits for that thread to finish and returns its loaded value. Note that    * multiple threads can concurrently load values for distinct keys.    *    *<p>Caches loaded by a {@link CacheLoader} will call {@link CacheLoader#load} to load new values    * into the cache. Newly loaded values are added to the cache using    * {@code Cache.asMap().putIfAbsent} after loading has completed; if another value was associated    * with {@code key} while the new value was loading then a removal notification will be sent for    * the new value.    *    *<p>If the cache loader associated with this cache is known not to throw checked    * exceptions, then prefer {@link #getUnchecked} over this method.    *    * @throws ExecutionException if a checked exception was thrown while loading the value. ({@code    *     ExecutionException} is thrown<a    *     href="http://code.google.com/p/guava-libraries/wiki/CachesExplained#Interruption">even if    *     computation was interrupted by an {@code InterruptedException}</a>.)    * @throws UncheckedExecutionException if an unchecked exception was thrown while loading the    *     value    * @throws ExecutionError if an error was thrown while loading the value    */
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
comment|/**    * Returns the value associated with {@code key} in this cache, first loading that value if    * necessary. No observable state associated with this cache is modified until loading    * completes. Unlike {@link #get}, this method does not throw a checked exception, and thus should    * only be used in situations where checked exceptions are not thrown by the cache loader.    *    *<p>If another call to {@link #get} or {@link #getUnchecked} is currently loading the value for    * {@code key}, simply waits for that thread to finish and returns its loaded value. Note that    * multiple threads can concurrently load values for distinct keys.    *    *<p>Caches loaded by a {@link CacheLoader} will call {@link CacheLoader#load} to load new values    * into the cache. Newly loaded values are added to the cache using    * {@code Cache.asMap().putIfAbsent} after loading has completed; if another value was associated    * with {@code key} while the new value was loading then a removal notification will be sent for    * the new value.    *    *<p><b>Warning:</b> this method silently converts checked exceptions to unchecked exceptions,    * and should not be used with cache loaders which throw checked exceptions. In such cases use    * {@link #get} instead.    *    * @throws UncheckedExecutionException if an exception was thrown while loading the value. (As    *     explained in the last paragraph above, this should be an unchecked exception only.)    * @throws ExecutionError if an error was thrown while loading the value    */
DECL|method|getUnchecked (K key)
name|V
name|getUnchecked
parameter_list|(
name|K
name|key
parameter_list|)
function_decl|;
comment|/**    * Returns a map of the values associated with {@code keys}, creating or retrieving those values    * if necessary. The returned map contains entries that were already cached, combined with newly    * loaded entries; it will never contain null keys or values.    *    *<p>Caches loaded by a {@link CacheLoader} will issue a single request to    * {@link CacheLoader#loadAll} for all keys which are not already present in the cache. All    * entries returned by {@link CacheLoader#loadAll} will be stored in the cache, over-writing    * any previously cached values. This method will throw an exception if    * {@link CacheLoader#loadAll} returns {@code null}, returns a map containing null keys or values,    * or fails to return an entry for each requested key.    *    *<p>Note that duplicate elements in {@code keys}, as determined by {@link Object#equals}, will    * be ignored.    *    * @throws ExecutionException if a checked exception was thrown while loading the value. ({@code    *     ExecutionException} is thrown<a    *     href="http://code.google.com/p/guava-libraries/wiki/CachesExplained#Interruption">even if    *     computation was interrupted by an {@code InterruptedException}</a>.)    * @throws UncheckedExecutionException if an unchecked exception was thrown while loading the    *     values    * @throws ExecutionError if an error was thrown while loading the values    * @since 11.0    */
DECL|method|getAll (Iterable<? extends K> keys)
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
function_decl|;
comment|/**    * @deprecated Provided to satisfy the {@code Function} interface; use {@link #get} or    *     {@link #getUnchecked} instead.    * @throws UncheckedExecutionException if an exception was thrown while loading the value. (As    *     described in the documentation for {@link #getUnchecked}, {@code LoadingCache} should be    *     used as a {@code Function} only with cache loaders that throw only unchecked exceptions.)    */
annotation|@
name|Deprecated
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
comment|/**    * Loads a new value for key {@code key}, possibly asynchronously. While the new value is loading    * the previous value (if any) will continue to be returned by {@code get(key)} unless it is    * evicted. If the new value is loaded successfully it will replace the previous value in the    * cache; if an exception is thrown while refreshing the previous value will remain,<i>and the    * exception will be logged (using {@link java.util.logging.Logger}) and swallowed</i>.    *    *<p>Caches loaded by a {@link CacheLoader} will call {@link CacheLoader#reload} if the    * cache currently contains a value for {@code key}, and {@link CacheLoader#load} otherwise.    * Loading is asynchronous only if {@link CacheLoader#reload} was overridden with an    * asynchronous implementation.    *    *<p>Returns without doing anything if another thread is currently loading the value for    * {@code key}. If the cache loader associated with this cache performs refresh asynchronously    * then this method may return before refresh completes.    *    * @since 11.0    */
DECL|method|refresh (K key)
name|void
name|refresh
parameter_list|(
name|K
name|key
parameter_list|)
function_decl|;
comment|/**    * {@inheritDoc}    *    *<p><b>Note that although the view<i>is</i> modifiable, no method on the returned map will ever    * cause entries to be automatically loaded.</b>    */
annotation|@
name|Override
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

