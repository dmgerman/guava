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
name|ImmutableMap
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
comment|/**  * A cache which forwards all its method calls to another cache. Subclasses should override one or  * more methods to modify the behavior of the backing cache as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p>Note that {@link #get}, {@link #getUnchecked}, and {@link #apply} all expose the same  * underlying functionality, so should probably be overridden as a group.  *  * @author Charles Fry  * @since 11.0  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|ForwardingLoadingCache
specifier|public
specifier|abstract
class|class
name|ForwardingLoadingCache
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
implements|implements
name|LoadingCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingLoadingCache ()
specifier|protected
name|ForwardingLoadingCache
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|LoadingCache
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
DECL|method|getUnchecked (K key)
specifier|public
name|V
name|getUnchecked
parameter_list|(
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
name|Override
DECL|method|getAll (Iterable<? extends K> keys)
specifier|public
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
block|{
return|return
name|delegate
argument_list|()
operator|.
name|getAll
argument_list|(
name|keys
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|apply (K key)
specifier|public
name|V
name|apply
parameter_list|(
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
DECL|method|refresh (K key)
specifier|public
name|void
name|refresh
parameter_list|(
name|K
name|key
parameter_list|)
block|{
name|delegate
argument_list|()
operator|.
name|refresh
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
comment|/**    * A simplified version of {@link ForwardingLoadingCache} where subclasses can pass in an already    * constructed {@link LoadingCache} as the delegate.    *    * @since 10.0    */
DECL|class|SimpleForwardingLoadingCache
specifier|public
specifier|abstract
specifier|static
class|class
name|SimpleForwardingLoadingCache
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingLoadingCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|delegate
specifier|private
specifier|final
name|LoadingCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
decl_stmt|;
DECL|method|SimpleForwardingLoadingCache (LoadingCache<K, V> delegate)
specifier|protected
name|SimpleForwardingLoadingCache
parameter_list|(
name|LoadingCache
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
name|LoadingCache
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

