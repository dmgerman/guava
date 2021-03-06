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
name|collect
operator|.
name|Maps
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
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Utility {@link CacheLoader} implementations intended for use in testing.  *  * @author mike nonemacher  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|TestingCacheLoaders
class|class
name|TestingCacheLoaders
block|{
comment|/**    * Returns a {@link CacheLoader} that implements a naive {@link CacheLoader#loadAll}, delegating    * {@link CacheLoader#load} calls to {@code loader}.    */
DECL|method|bulkLoader (final CacheLoader<K, V> loader)
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
name|bulkLoader
parameter_list|(
specifier|final
name|CacheLoader
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|loader
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|loader
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
name|newHashMap
argument_list|()
decl_stmt|;
comment|// allow nulls
for|for
control|(
name|K
name|key
range|:
name|keys
control|)
block|{
name|result
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|load
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
return|;
block|}
comment|/** Returns a {@link CacheLoader} that returns the given {@code constant} for every request. */
DECL|method|constantLoader (@ullable V constant)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ConstantLoader
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|constantLoader
parameter_list|(
annotation|@
name|Nullable
name|V
name|constant
parameter_list|)
block|{
return|return
operator|new
name|ConstantLoader
argument_list|<>
argument_list|(
name|constant
argument_list|)
return|;
block|}
comment|/** Returns a {@link CacheLoader} that returns the given {@code constant} for every request. */
DECL|method|incrementingLoader ()
specifier|static
name|IncrementingLoader
name|incrementingLoader
parameter_list|()
block|{
return|return
operator|new
name|IncrementingLoader
argument_list|()
return|;
block|}
comment|/** Returns a {@link CacheLoader} that throws the given error for every request. */
DECL|method|errorLoader (final Error e)
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
name|errorLoader
parameter_list|(
specifier|final
name|Error
name|e
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|e
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
block|{
throw|throw
name|e
throw|;
block|}
block|}
return|;
block|}
comment|/** Returns a {@link CacheLoader} that throws the given exception for every request. */
DECL|method|exceptionLoader (final Exception e)
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
name|exceptionLoader
parameter_list|(
specifier|final
name|Exception
name|e
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|e
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
throw|throw
name|e
throw|;
block|}
block|}
return|;
block|}
comment|/** Returns a {@link CacheLoader} that returns the key for every request. */
DECL|method|identityLoader ()
specifier|static
parameter_list|<
name|T
parameter_list|>
name|IdentityLoader
argument_list|<
name|T
argument_list|>
name|identityLoader
parameter_list|()
block|{
return|return
operator|new
name|IdentityLoader
argument_list|<
name|T
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Returns a {@code new Object()} for every request, and increments a counter for every request.    * The count is accessible via {@link #getCount}.    */
DECL|class|CountingLoader
specifier|static
class|class
name|CountingLoader
extends|extends
name|CacheLoader
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
block|{
DECL|field|count
specifier|private
specifier|final
name|AtomicInteger
name|count
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|load (Object from)
specifier|public
name|Object
name|load
parameter_list|(
name|Object
name|from
parameter_list|)
block|{
name|count
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
return|return
operator|new
name|Object
argument_list|()
return|;
block|}
DECL|method|getCount ()
specifier|public
name|int
name|getCount
parameter_list|()
block|{
return|return
name|count
operator|.
name|get
argument_list|()
return|;
block|}
block|}
DECL|class|ConstantLoader
specifier|static
specifier|final
class|class
name|ConstantLoader
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
block|{
DECL|field|constant
specifier|private
specifier|final
name|V
name|constant
decl_stmt|;
DECL|method|ConstantLoader (V constant)
name|ConstantLoader
parameter_list|(
name|V
name|constant
parameter_list|)
block|{
name|this
operator|.
name|constant
operator|=
name|constant
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
name|constant
return|;
block|}
block|}
comment|/**    * Returns a {@code new Object()} for every request, and increments a counter for every request.    * An {@code Integer} loader that returns the key for {@code load} requests, and increments the    * old value on {@code reload} requests. The load counts are accessible via {@link #getLoadCount}    * and {@link #getReloadCount}.    */
DECL|class|IncrementingLoader
specifier|static
class|class
name|IncrementingLoader
extends|extends
name|CacheLoader
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
block|{
DECL|field|countLoad
specifier|private
specifier|final
name|AtomicInteger
name|countLoad
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|countReload
specifier|private
specifier|final
name|AtomicInteger
name|countReload
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|load (Integer key)
specifier|public
name|Integer
name|load
parameter_list|(
name|Integer
name|key
parameter_list|)
block|{
name|countLoad
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
return|return
name|key
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// reload
annotation|@
name|Override
DECL|method|reload (Integer key, Integer oldValue)
specifier|public
name|ListenableFuture
argument_list|<
name|Integer
argument_list|>
name|reload
parameter_list|(
name|Integer
name|key
parameter_list|,
name|Integer
name|oldValue
parameter_list|)
block|{
name|countReload
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
return|return
name|Futures
operator|.
name|immediateFuture
argument_list|(
name|oldValue
operator|+
literal|1
argument_list|)
return|;
block|}
DECL|method|getLoadCount ()
specifier|public
name|int
name|getLoadCount
parameter_list|()
block|{
return|return
name|countLoad
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|getReloadCount ()
specifier|public
name|int
name|getReloadCount
parameter_list|()
block|{
return|return
name|countReload
operator|.
name|get
argument_list|()
return|;
block|}
block|}
DECL|class|IdentityLoader
specifier|static
specifier|final
class|class
name|IdentityLoader
parameter_list|<
name|T
parameter_list|>
extends|extends
name|CacheLoader
argument_list|<
name|T
argument_list|,
name|T
argument_list|>
block|{
annotation|@
name|Override
DECL|method|load (T key)
specifier|public
name|T
name|load
parameter_list|(
name|T
name|key
parameter_list|)
block|{
return|return
name|key
return|;
block|}
block|}
block|}
end_class

end_unit

