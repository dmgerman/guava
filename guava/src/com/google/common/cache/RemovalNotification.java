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
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
comment|/**  * A notification of the removal of a single entry. The key and/or value may be null if they were  * already garbage collected.  *  *<p>Like other {@code Map.Entry} instances associated with {@code CacheBuilder}, this class holds  * strong references to the key and value, regardless of the type of references the cache may be  * using.  *  * @author fry@google.com (Charles Fry)  * @since Guava release 10  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|RemovalNotification
specifier|public
specifier|final
class|class
name|RemovalNotification
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|key
annotation|@
name|Nullable
specifier|private
specifier|final
name|K
name|key
decl_stmt|;
DECL|field|value
annotation|@
name|Nullable
specifier|private
specifier|final
name|V
name|value
decl_stmt|;
DECL|field|cause
specifier|private
specifier|final
name|RemovalCause
name|cause
decl_stmt|;
DECL|method|RemovalNotification (@ullable K key, @Nullable V value, RemovalCause cause)
name|RemovalNotification
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
parameter_list|,
name|RemovalCause
name|cause
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|this
operator|.
name|cause
operator|=
name|checkNotNull
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns the cause for which the entry was removed.    */
DECL|method|getCause ()
specifier|public
name|RemovalCause
name|getCause
parameter_list|()
block|{
return|return
name|cause
return|;
block|}
comment|/**    * Returns {@code true} if there was an automatic removal due to eviction (the cause is neither    * {@link RemovalCause#EXPLICIT} nor {@link RemovalCause#REPLACED}).    */
DECL|method|wasEvicted ()
specifier|public
name|boolean
name|wasEvicted
parameter_list|()
block|{
return|return
name|cause
operator|.
name|wasEvicted
argument_list|()
return|;
block|}
DECL|method|getKey ()
annotation|@
name|Nullable
annotation|@
name|Override
specifier|public
name|K
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|getValue ()
annotation|@
name|Nullable
annotation|@
name|Override
specifier|public
name|V
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|setValue (V value)
annotation|@
name|Override
specifier|public
specifier|final
name|V
name|setValue
parameter_list|(
name|V
name|value
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|equals (@ullable Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|Entry
condition|)
block|{
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|that
init|=
operator|(
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|Objects
operator|.
name|equal
argument_list|(
name|this
operator|.
name|getKey
argument_list|()
argument_list|,
name|that
operator|.
name|getKey
argument_list|()
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|this
operator|.
name|getValue
argument_list|()
argument_list|,
name|that
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|K
name|k
init|=
name|getKey
argument_list|()
decl_stmt|;
name|V
name|v
init|=
name|getValue
argument_list|()
decl_stmt|;
return|return
operator|(
operator|(
name|k
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|k
operator|.
name|hashCode
argument_list|()
operator|)
operator|^
operator|(
operator|(
name|v
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|v
operator|.
name|hashCode
argument_list|()
operator|)
return|;
block|}
comment|/**    * Returns a string representation of the form<code>{key}={value}</code>.    */
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getKey
argument_list|()
operator|+
literal|"="
operator|+
name|getValue
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
end_class

end_unit

