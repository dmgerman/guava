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
name|Iterator
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
name|ConcurrentMap
import|;
end_import

begin_comment
comment|/**  * The reason why a cached entry was removed.  *  * @author fry@google.com (Charles Fry)  * @since Guava release 10  */
end_comment

begin_enum
annotation|@
name|Beta
DECL|enum|RemovalCause
specifier|public
enum|enum
name|RemovalCause
block|{
comment|/**    * The entry was manually removed by the user. This can result from the user invoking {@link    * Cache#invalidate}, {@link Map#remove}, {@link ConcurrentMap#remove}, or {@link    * Iterator#remove}.    */
DECL|enumConstant|EXPLICIT
name|EXPLICIT
block|{
annotation|@
name|Override
name|boolean
name|wasEvicted
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
block|,
comment|/**    * The entry itself was not actually removed, but its value was replaced by the user. This can    * result from the user invoking {@link Map#put}, {@link Map#putAll},    * {@link ConcurrentMap#replace(Object, Object)}, or    * {@link ConcurrentMap#replace(Object, Object, Object)}.    */
DECL|enumConstant|REPLACED
name|REPLACED
block|{
annotation|@
name|Override
name|boolean
name|wasEvicted
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
block|,
comment|/**    * The entry was removed automatically because its key or value was garbage-collected. This    * can occur when using {@link CacheBuilder#weakKeys}, {@link CacheBuilder#weakValues}, or    * {@link CacheBuilder#softValues}.    */
DECL|enumConstant|COLLECTED
name|COLLECTED
block|{
annotation|@
name|Override
name|boolean
name|wasEvicted
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
block|,
comment|/**    * The entry's expiration timestamp has passed. This can occur when using {@link    * CacheBuilder#expireAfterWrite} or {@link CacheBuilder#expireAfterAccess}.    */
DECL|enumConstant|EXPIRED
name|EXPIRED
block|{
annotation|@
name|Override
name|boolean
name|wasEvicted
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
block|,
comment|/**    * The entry was evicted due to size constraints. This can occur when using {@link    * CacheBuilder#maximumSize}.    */
DECL|enumConstant|SIZE
name|SIZE
block|{
annotation|@
name|Override
name|boolean
name|wasEvicted
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
block|;
comment|/**    * Returns {@code true} if there was an automatic removal due to eviction (the cause is neither    * {@link #EXPLICIT} nor {@link #REPLACED}).    */
DECL|method|wasEvicted ()
specifier|abstract
name|boolean
name|wasEvicted
parameter_list|()
function_decl|;
block|}
end_enum

end_unit

