begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Set
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
comment|/**  * GWT implementation of {@link ImmutableMap} that forwards to another map.  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|ForwardingImmutableMap
specifier|public
specifier|abstract
class|class
name|ForwardingImmutableMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|delegate
specifier|final
specifier|transient
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
decl_stmt|;
DECL|method|ForwardingImmutableMap (Map<? extends K, ? extends V> delegate)
name|ForwardingImmutableMap
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|ForwardingImmutableMap (Entry<? extends K, ? extends V>.... entries)
name|ForwardingImmutableMap
parameter_list|(
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
modifier|...
name|entries
parameter_list|)
block|{
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
init|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|K
name|key
init|=
name|checkNotNull
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|V
name|previous
init|=
name|delegate
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|checkNotNull
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|previous
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"duplicate key: "
operator|+
name|key
argument_list|)
throw|;
block|}
block|}
name|this
operator|.
name|delegate
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|isEmpty ()
specifier|public
specifier|final
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|containsKey (@ullable Object key)
specifier|public
specifier|final
name|boolean
name|containsKey
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
block|{
return|return
name|Maps
operator|.
name|safeContainsKey
argument_list|(
name|delegate
argument_list|,
name|key
argument_list|)
return|;
block|}
DECL|method|containsValue (@ullable Object value)
specifier|public
specifier|final
name|boolean
name|containsValue
parameter_list|(
annotation|@
name|Nullable
name|Object
name|value
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|containsValue
argument_list|(
name|value
argument_list|)
return|;
block|}
DECL|method|get (@ullable Object key)
specifier|public
name|V
name|get
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
block|{
return|return
operator|(
name|key
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|Maps
operator|.
name|safeGet
argument_list|(
name|delegate
argument_list|,
name|key
argument_list|)
return|;
block|}
DECL|method|createEntrySet ()
annotation|@
name|Override
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|createEntrySet
parameter_list|()
block|{
return|return
name|ImmutableSet
operator|.
name|unsafeDelegate
argument_list|(
operator|new
name|ForwardingSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|entrySet
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|&&
operator|(
operator|(
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
operator|)
operator|.
name|getKey
argument_list|()
operator|==
literal|null
argument_list|)
block|{
return|return
literal|false
return|;
block|}
try|try
block|{
return|return
name|super
operator|.
name|contains
argument_list|(
name|object
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|toArray
parameter_list|(
name|T
index|[]
name|array
parameter_list|)
block|{
name|T
index|[]
name|result
init|=
name|super
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
decl_stmt|;
if|if
condition|(
name|size
argument_list|()
operator|<
name|result
operator|.
name|length
condition|)
block|{
comment|// It works around a GWT bug where elements after last is not
comment|// properly null'ed.
name|result
index|[
name|size
argument_list|()
index|]
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_function
unit|}    @
DECL|method|createKeySet ()
name|Override
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|createKeySet
parameter_list|()
block|{
return|return
name|ImmutableSet
operator|.
name|unsafeDelegate
argument_list|(
name|delegate
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|createValues ()
annotation|@
name|Override
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|createValues
parameter_list|()
block|{
return|return
name|ImmutableCollection
operator|.
name|unsafeDelegate
argument_list|(
name|delegate
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|size ()
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|size
argument_list|()
return|;
block|}
end_function

begin_function
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
return|return
name|delegate
operator|.
name|equals
argument_list|(
name|object
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|hashCode
argument_list|()
return|;
block|}
end_function

begin_function
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|toString
argument_list|()
return|;
block|}
end_function

unit|}
end_unit

