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
name|Preconditions
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
name|ForwardingObject
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
comment|/**  * A cache which forwards all its method calls to another cache. Subclasses should override one or  * more methods to modify the behavior of the backing cache as desired per the  *<a href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p>Note that {@link #get}, {@link #getUnchecked}, and {@link #apply} all expose the same  * underlying functionality, so should probably be overridden as a group.  *  * @author Charles Fry  * @since 10.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|ForwardingCache
specifier|public
specifier|abstract
class|class
name|ForwardingCache
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingObject
implements|implements
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingCache ()
specifier|protected
name|ForwardingCache
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|()
function_decl|;
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|get (@ullable K key)
specifier|public
name|V
name|get
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|)
throws|throws
name|ExecutionException
block|{
return|return
name|delegate
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|getUnchecked (@ullable K key)
specifier|public
name|V
name|getUnchecked
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|getUnchecked
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|apply (@ullable K key)
specifier|public
name|V
name|apply
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|apply
argument_list|(
name|key
argument_list|)
return|;
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
name|delegate
argument_list|()
operator|.
name|invalidate
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
name|delegate
argument_list|()
operator|.
name|invalidateAll
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
name|delegate
argument_list|()
operator|.
name|size
argument_list|()
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
return|return
name|delegate
argument_list|()
operator|.
name|stats
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
name|delegate
argument_list|()
operator|.
name|asMap
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
name|delegate
argument_list|()
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
block|}
comment|/**    * A simplified version of {@link ForwardingCache} where subclasses can pass in an already    * constructed {@link Cache} as the delegete.    *    * @since 10.0    */
annotation|@
name|Beta
DECL|class|SimpleForwardingCache
specifier|public
specifier|abstract
specifier|static
class|class
name|SimpleForwardingCache
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|delegate
specifier|private
specifier|final
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
decl_stmt|;
DECL|method|SimpleForwardingCache (Cache<K, V> delegate)
specifier|protected
name|SimpleForwardingCache
parameter_list|(
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|final
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
block|}
block|}
end_class

end_unit

