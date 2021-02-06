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
name|annotations
operator|.
name|GwtIncompatible
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
name|base
operator|.
name|Supplier
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
name|Futures
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
name|ListenableFuture
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
name|ListenableFutureTask
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
name|CheckReturnValue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|Executor
import|;
end_import

begin_comment
comment|/**  * Computes or retrieves values, based on a key, for use in populating a {@link LoadingCache}.  *  *<p>Most implementations will only need to implement {@link #load}. Other methods may be  * overridden as desired.  *  *<p>Usage example:  *  *<pre>{@code  * CacheLoader<Key, Graph> loader = new CacheLoader<Key, Graph>() {  *   public Graph load(Key key) throws AnyException {  *     return createExpensiveGraph(key);  *   }  * };  * LoadingCache<Key, Graph> cache = CacheBuilder.newBuilder().build(loader);  * }</pre>  *  *<p>Since this example doesn't support reloading or bulk loading, if you're able to use lambda  * expressions it can be specified even more easily:  *  *<pre>{@code  * CacheLoader<Key, Graph> loader = CacheLoader.from(key -> createExpensiveGraph(key));  * }</pre>  *  * @author Charles Fry  * @since 10.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|CacheLoader
specifier|public
specifier|abstract
class|class
name|CacheLoader
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|CacheLoader ()
specifier|protected
name|CacheLoader
parameter_list|()
block|{}
comment|/**    * Computes or retrieves the value corresponding to {@code key}.    *    * @param key the non-null key whose value should be loaded    * @return the value associated with {@code key};<b>must not be null</b>    * @throws Exception if unable to load the result    * @throws InterruptedException if this method is interrupted. {@code InterruptedException} is    *     treated like any other {@code Exception} in all respects except that, when it is caught,    *     the thread's interrupt status is set    */
DECL|method|load (K key)
specifier|public
specifier|abstract
name|V
name|load
parameter_list|(
name|K
name|key
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**    * Computes or retrieves a replacement value corresponding to an already-cached {@code key}. This    * method is called when an existing cache entry is refreshed by {@link    * CacheBuilder#refreshAfterWrite}, or through a call to {@link LoadingCache#refresh}.    *    *<p>This implementation synchronously delegates to {@link #load}. It is recommended that it be    * overridden with an asynchronous implementation when using {@link    * CacheBuilder#refreshAfterWrite}.    *    *<p><b>Note:</b><i>all exceptions thrown by this method will be logged and then swallowed</i>.    *    * @param key the non-null key whose value should be loaded    * @param oldValue the non-null old value corresponding to {@code key}    * @return the future new value associated with {@code key};<b>must not be null, must not return    *     null</b>    * @throws Exception if unable to reload the result    * @throws InterruptedException if this method is interrupted. {@code InterruptedException} is    *     treated like any other {@code Exception} in all respects except that, when it is caught,    *     the thread's interrupt status is set    * @since 11.0    */
annotation|@
name|GwtIncompatible
comment|// Futures
DECL|method|reload (K key, V oldValue)
specifier|public
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|reload
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|oldValue
parameter_list|)
throws|throws
name|Exception
block|{
name|checkNotNull
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|oldValue
argument_list|)
expr_stmt|;
return|return
name|Futures
operator|.
name|immediateFuture
argument_list|(
name|load
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Computes or retrieves the values corresponding to {@code keys}. This method is called by {@link    * LoadingCache#getAll}.    *    *<p>If the returned map doesn't contain all requested {@code keys} then the entries it does    * contain will be cached, but {@code getAll} will throw an exception. If the returned map    * contains extra keys not present in {@code keys} then all returned entries will be cached, but    * only the entries for {@code keys} will be returned from {@code getAll}.    *    *<p>This method should be overridden when bulk retrieval is significantly more efficient than    * many individual lookups. Note that {@link LoadingCache#getAll} will defer to individual calls    * to {@link LoadingCache#get} if this method is not overridden.    *    * @param keys the unique, non-null keys whose values should be loaded    * @return a map from each key in {@code keys} to the value associated with that key;<b>may not    *     contain null values</b>    * @throws Exception if unable to load the result    * @throws InterruptedException if this method is interrupted. {@code InterruptedException} is    *     treated like any other {@code Exception} in all respects except that, when it is caught,    *     the thread's interrupt status is set    * @since 11.0    */
DECL|method|loadAll (Iterable<? extends K> keys)
specifier|public
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|loadAll
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
name|Exception
block|{
comment|// This will be caught by getAll(), causing it to fall back to multiple calls to
comment|// LoadingCache.get
throw|throw
operator|new
name|UnsupportedLoadingOperationException
argument_list|()
throw|;
block|}
comment|/**    * Returns a cache loader that uses {@code function} to load keys, and without supporting either    * reloading or bulk loading. This is most useful when you can pass a lambda expression. Otherwise    * it is useful mostly when you already have an existing function instance.    *    * @param function the function to be used for loading values; must never return {@code null}    * @return a cache loader that loads values by passing each key to {@code function}    */
annotation|@
name|CheckReturnValue
DECL|method|from (Function<K, V> function)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|CacheLoader
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|from
parameter_list|(
name|Function
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|function
parameter_list|)
block|{
return|return
operator|new
name|FunctionToCacheLoader
argument_list|<>
argument_list|(
name|function
argument_list|)
return|;
block|}
comment|/**    * Returns a cache loader based on an<i>existing</i> supplier instance. Note that there's no need    * to create a<i>new</i> supplier just to pass it in here; just subclass {@code CacheLoader} and    * implement {@link #load load} instead.    *    * @param supplier the supplier to be used for loading values; must never return {@code null}    * @return a cache loader that loads values by calling {@link Supplier#get}, irrespective of the    *     key    */
annotation|@
name|CheckReturnValue
DECL|method|from (Supplier<V> supplier)
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|CacheLoader
argument_list|<
name|Object
argument_list|,
name|V
argument_list|>
name|from
parameter_list|(
name|Supplier
argument_list|<
name|V
argument_list|>
name|supplier
parameter_list|)
block|{
return|return
operator|new
name|SupplierToCacheLoader
argument_list|<
name|V
argument_list|>
argument_list|(
name|supplier
argument_list|)
return|;
block|}
DECL|class|FunctionToCacheLoader
specifier|private
specifier|static
specifier|final
class|class
name|FunctionToCacheLoader
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|CacheLoader
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|computingFunction
specifier|private
specifier|final
name|Function
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|computingFunction
decl_stmt|;
DECL|method|FunctionToCacheLoader (Function<K, V> computingFunction)
specifier|public
name|FunctionToCacheLoader
parameter_list|(
name|Function
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|computingFunction
parameter_list|)
block|{
name|this
operator|.
name|computingFunction
operator|=
name|checkNotNull
argument_list|(
name|computingFunction
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|load (K key)
specifier|public
name|V
name|load
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|computingFunction
operator|.
name|apply
argument_list|(
name|checkNotNull
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
comment|/**    * Returns a {@code CacheLoader} which wraps {@code loader}, executing calls to {@link    * CacheLoader#reload} using {@code executor}.    *    *<p>This method is useful only when {@code loader.reload} has a synchronous implementation, such    * as {@linkplain #reload the default implementation}.    *    * @since 17.0    */
annotation|@
name|CheckReturnValue
annotation|@
name|GwtIncompatible
comment|// Executor + Futures
DECL|method|asyncReloading ( final CacheLoader<K, V> loader, final Executor executor)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|CacheLoader
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|asyncReloading
parameter_list|(
specifier|final
name|CacheLoader
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|loader
parameter_list|,
specifier|final
name|Executor
name|executor
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|loader
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|executor
argument_list|)
expr_stmt|;
return|return
operator|new
name|CacheLoader
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|V
name|load
parameter_list|(
name|K
name|key
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|loader
operator|.
name|load
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|reload
parameter_list|(
specifier|final
name|K
name|key
parameter_list|,
specifier|final
name|V
name|oldValue
parameter_list|)
throws|throws
name|Exception
block|{
name|ListenableFutureTask
argument_list|<
name|V
argument_list|>
name|task
init|=
name|ListenableFutureTask
operator|.
name|create
argument_list|(
operator|new
name|Callable
argument_list|<
name|V
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|V
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|loader
operator|.
name|reload
argument_list|(
name|key
argument_list|,
name|oldValue
argument_list|)
operator|.
name|get
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|executor
operator|.
name|execute
argument_list|(
name|task
argument_list|)
expr_stmt|;
return|return
name|task
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|loadAll
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
name|Exception
block|{
return|return
name|loader
operator|.
name|loadAll
argument_list|(
name|keys
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|class|SupplierToCacheLoader
specifier|private
specifier|static
specifier|final
class|class
name|SupplierToCacheLoader
parameter_list|<
name|V
parameter_list|>
extends|extends
name|CacheLoader
argument_list|<
name|Object
argument_list|,
name|V
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|computingSupplier
specifier|private
specifier|final
name|Supplier
argument_list|<
name|V
argument_list|>
name|computingSupplier
decl_stmt|;
DECL|method|SupplierToCacheLoader (Supplier<V> computingSupplier)
specifier|public
name|SupplierToCacheLoader
parameter_list|(
name|Supplier
argument_list|<
name|V
argument_list|>
name|computingSupplier
parameter_list|)
block|{
name|this
operator|.
name|computingSupplier
operator|=
name|checkNotNull
argument_list|(
name|computingSupplier
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|load (Object key)
specifier|public
name|V
name|load
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
return|return
name|computingSupplier
operator|.
name|get
argument_list|()
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
comment|/**    * Exception thrown by {@code loadAll()} to indicate that it is not supported.    *    * @since 19.0    */
DECL|class|UnsupportedLoadingOperationException
specifier|public
specifier|static
specifier|final
class|class
name|UnsupportedLoadingOperationException
extends|extends
name|UnsupportedOperationException
block|{
comment|// Package-private because this should only be thrown by loadAll() when it is not overridden.
comment|// Cache implementors may want to catch it but should not need to be able to throw it.
DECL|method|UnsupportedLoadingOperationException ()
name|UnsupportedLoadingOperationException
parameter_list|()
block|{}
block|}
comment|/**    * Thrown to indicate that an invalid response was returned from a call to {@link CacheLoader}.    *    * @since 11.0    */
DECL|class|InvalidCacheLoadException
specifier|public
specifier|static
specifier|final
class|class
name|InvalidCacheLoadException
extends|extends
name|RuntimeException
block|{
DECL|method|InvalidCacheLoadException (String message)
specifier|public
name|InvalidCacheLoadException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

