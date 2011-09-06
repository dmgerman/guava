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
comment|/**  * A collection of common removal listeners.  *  * @author Charles Fry  * @since 10.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|RemovalListeners
specifier|public
specifier|final
class|class
name|RemovalListeners
block|{
DECL|method|RemovalListeners ()
specifier|private
name|RemovalListeners
parameter_list|()
block|{}
comment|/**    * Returns an asynchronous {@code RemovalListener} which processes all    * eviction notifications asynchronously, using {@code executor}.    *    * @param listener the backing listener    * @param executor the executor with which removal notifications are    *     asynchronously executed    */
DECL|method|asynchronous ( final RemovalListener<K, V> listener, final Executor executor)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|RemovalListener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|asynchronous
parameter_list|(
specifier|final
name|RemovalListener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|listener
parameter_list|,
specifier|final
name|Executor
name|executor
parameter_list|)
block|{
return|return
operator|new
name|RemovalListener
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
name|void
name|onRemoval
parameter_list|(
specifier|final
name|RemovalNotification
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|notification
parameter_list|)
block|{
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|listener
operator|.
name|onRemoval
argument_list|(
name|notification
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

