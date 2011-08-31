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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentLinkedQueue
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

begin_comment
comment|/**  * Utility {@link RemovalListener} implementations intended for use in testing.  *  * @author mike nonemacher  */
end_comment

begin_class
DECL|class|TestingRemovalListeners
class|class
name|TestingRemovalListeners
block|{
comment|/**    * Returns a new no-op {@code RemovalListener}.    */
DECL|method|nullRemovalListener ()
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|NullRemovalListener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nullRemovalListener
parameter_list|()
block|{
return|return
operator|new
name|NullRemovalListener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Type-inferring factory method for creating a {@link QueuingRemovalListener}.    */
DECL|method|queuingRemovalListener ()
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|QueuingRemovalListener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|queuingRemovalListener
parameter_list|()
block|{
return|return
operator|new
name|QueuingRemovalListener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Type-inferring factory method for creating a {@link CountingRemovalListener}.    */
DECL|method|countingRemovalListener ()
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|CountingRemovalListener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|countingRemovalListener
parameter_list|()
block|{
return|return
operator|new
name|CountingRemovalListener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * {@link RemovalListener} that adds all {@link RemovalNotification} objects to a queue.    */
DECL|class|QueuingRemovalListener
specifier|static
class|class
name|QueuingRemovalListener
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ConcurrentLinkedQueue
argument_list|<
name|RemovalNotification
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
implements|implements
name|RemovalListener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|Override
DECL|method|onRemoval (RemovalNotification<K, V> notification)
specifier|public
name|void
name|onRemoval
parameter_list|(
name|RemovalNotification
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|notification
parameter_list|)
block|{
name|add
argument_list|(
name|notification
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * {@link RemovalListener} that counts each {@link RemovalNotification} it receives, and provides    * access to the most-recently received one.    */
DECL|class|CountingRemovalListener
specifier|static
class|class
name|CountingRemovalListener
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|RemovalListener
argument_list|<
name|K
argument_list|,
name|V
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
DECL|field|lastNotification
specifier|private
specifier|volatile
name|RemovalNotification
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|lastNotification
decl_stmt|;
annotation|@
name|Override
DECL|method|onRemoval (RemovalNotification<K, V> notification)
specifier|public
name|void
name|onRemoval
parameter_list|(
name|RemovalNotification
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|notification
parameter_list|)
block|{
name|count
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|lastNotification
operator|=
name|notification
expr_stmt|;
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
DECL|method|getLastEvictedKey ()
specifier|public
name|K
name|getLastEvictedKey
parameter_list|()
block|{
return|return
name|lastNotification
operator|.
name|getKey
argument_list|()
return|;
block|}
DECL|method|getLastEvictedValue ()
specifier|public
name|V
name|getLastEvictedValue
parameter_list|()
block|{
return|return
name|lastNotification
operator|.
name|getValue
argument_list|()
return|;
block|}
DECL|method|getLastNotification ()
specifier|public
name|RemovalNotification
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getLastNotification
parameter_list|()
block|{
return|return
name|lastNotification
return|;
block|}
block|}
comment|/**    * No-op {@link RemovalListener}.    */
DECL|class|NullRemovalListener
specifier|static
class|class
name|NullRemovalListener
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|RemovalListener
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|Override
DECL|method|onRemoval (RemovalNotification<K, V> notification)
specifier|public
name|void
name|onRemoval
parameter_list|(
name|RemovalNotification
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|notification
parameter_list|)
block|{}
block|}
block|}
end_class

end_unit

