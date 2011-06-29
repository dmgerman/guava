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

begin_comment
comment|/**  * An object that can receive a notification when an entry is removed from a cache. The removal  * resulting in notification could have occured to an entry being manually removed or replaced, or  * due to eviction resulting from timed expiration, exceeding a maximum size, or garbage  * collection.  *  *<p>An instance may be called concurrently by multiple threads to process different entries.  * Implementations of this interface should avoid performing blocking calls or synchronizing on  * shared resources.  *  * @param<K> the most general type of keys this listener can listen for; for  *     example {@code Object} if any key is acceptable  * @param<V> the most general type of values this listener can listen for; for  *     example {@code Object} if any key is acceptable  * @author fry@google.com (Charles Fry)  * @since Guava release 10  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|RemovalListener
specifier|public
interface|interface
name|RemovalListener
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
block|{
comment|/**    * Notifies the listener that a removal occurred at some point in the past.    */
comment|// Technically should accept RemovalNotification<? extends K, ? extends V>, but because
comment|// RemovalNotification is guaranteed covariant, let's make users' lives simpler.
DECL|method|onRemoval (RemovalNotification<K, V> notification)
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
function_decl|;
block|}
end_interface

end_unit

