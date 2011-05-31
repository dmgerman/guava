begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * An object that can receive a notification when an entry is evicted from a  * map.  *  *<p>An instance may be called concurrently by multiple threads to process  * different entries. Implementations of this interface should avoid performing  * blocking calls or synchronizing on shared resources.  *  * @param<K> the type of keys being evicted  * @param<V> the type of values being evicted  * @author Ben Manes  * @since Guava release 07  * @deprecated use {@link MapMaker.RemovalListener}<b>This class is scheduled for deletion in  *     Guava release 11.</b>  */
end_comment

begin_interface
annotation|@
name|Beta
annotation|@
name|Deprecated
specifier|public
DECL|interface|MapEvictionListener
interface|interface
name|MapEvictionListener
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
block|{
comment|/**    * Notifies the listener that an eviction has occurred. Eviction may be for    * reasons such as timed expiration, exceeding a maximum size, or due to    * garbage collection. Eviction notification does<i>not</i> occur due to    * manual removal.    *    * @param key the key of the entry that has already been evicted, or {@code    *     null} if its reference was collected    * @param value the value of the entry that has already been evicted, or    *     {@code null} if its reference was collected    */
DECL|method|onEviction (@ullable K key, @Nullable V value)
name|void
name|onEviction
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|,
annotation|@
name|Nullable
name|V
name|value
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

