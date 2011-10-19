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
name|cache
operator|.
name|LocalCacheAsMap
operator|.
name|Segment
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
comment|/**  * Exposes a {@link LocalCacheAsMap} as a {@code Cache}.  *  * @author Charles Fry  */
end_comment

begin_class
DECL|class|LocalCache
class|class
name|LocalCache
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|map
specifier|final
name|LocalCacheAsMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
DECL|method|LocalCache (CacheBuilder<? super K, ? super V> builder, Supplier<? extends StatsCounter> statsCounterSupplier, CacheLoader<? super K, V> loader)
name|LocalCache
parameter_list|(
name|CacheBuilder
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
super|super
name|V
argument_list|>
name|builder
parameter_list|,
name|Supplier
argument_list|<
name|?
extends|extends
name|StatsCounter
argument_list|>
name|statsCounterSupplier
parameter_list|,
name|CacheLoader
argument_list|<
name|?
super|super
name|K
argument_list|,
name|V
argument_list|>
name|loader
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
operator|new
name|LocalCacheAsMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|builder
argument_list|,
name|statsCounterSupplier
argument_list|,
name|loader
argument_list|)
expr_stmt|;
block|}
comment|// Cache methods
annotation|@
name|Override
DECL|method|get (K key)
specifier|public
name|V
name|get
parameter_list|(
name|K
name|key
parameter_list|)
throws|throws
name|ExecutionException
block|{
return|return
name|map
operator|.
name|getOrLoad
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|get (K key, final Callable<V> valueLoader)
specifier|public
name|V
name|get
parameter_list|(
name|K
name|key
parameter_list|,
specifier|final
name|Callable
argument_list|<
name|V
argument_list|>
name|valueLoader
parameter_list|)
throws|throws
name|ExecutionException
block|{
name|checkNotNull
argument_list|(
name|valueLoader
argument_list|)
expr_stmt|;
return|return
name|map
operator|.
name|getOrLoad
argument_list|(
name|key
argument_list|,
operator|new
name|CacheLoader
argument_list|<
name|Object
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
name|Object
name|key
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|valueLoader
operator|.
name|call
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|refresh (K key)
specifier|public
name|void
name|refresh
parameter_list|(
name|K
name|key
parameter_list|)
throws|throws
name|ExecutionException
block|{
name|map
operator|.
name|refresh
argument_list|(
name|key
argument_list|)
expr_stmt|;
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
name|checkNotNull
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|invalidateAll ()
specifier|public
name|void
name|invalidateAll
parameter_list|()
block|{
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|long
name|size
parameter_list|()
block|{
return|return
name|map
operator|.
name|longSize
argument_list|()
return|;
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
return|return
name|map
return|;
block|}
annotation|@
name|Override
DECL|method|stats ()
specifier|public
name|CacheStats
name|stats
parameter_list|()
block|{
name|SimpleStatsCounter
name|aggregator
init|=
operator|new
name|SimpleStatsCounter
argument_list|()
decl_stmt|;
for|for
control|(
name|Segment
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|segment
range|:
name|map
operator|.
name|segments
control|)
block|{
name|aggregator
operator|.
name|incrementBy
argument_list|(
name|segment
operator|.
name|statsCounter
argument_list|)
expr_stmt|;
block|}
return|return
name|aggregator
operator|.
name|snapshot
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|cleanUp ()
specifier|public
name|void
name|cleanUp
parameter_list|()
block|{
name|map
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
block|}
comment|// Serialization Support
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1
decl_stmt|;
DECL|method|writeReplace ()
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
name|map
operator|.
name|cacheSerializationProxy
argument_list|()
return|;
block|}
block|}
end_class

end_unit

