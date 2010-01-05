begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|GwtCompatible
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
comment|/**  * A map which forwards all its method calls to another map. Subclasses should  * override one or more methods to modify the behavior of the backing map as  * desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  * @see ForwardingObject  * @author Kevin Bourrillion  * @author Jared Levy  * @since 2010.01.04<b>stable</b> (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ForwardingMap
specifier|public
specifier|abstract
class|class
name|ForwardingMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingObject
implements|implements
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
specifier|abstract
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|()
function_decl|;
DECL|method|size ()
specifier|public
name|int
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
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|remove (Object object)
specifier|public
name|V
name|remove
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|remove
argument_list|(
name|object
argument_list|)
return|;
block|}
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|delegate
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|containsKey (Object key)
specifier|public
name|boolean
name|containsKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|containsValue (Object value)
specifier|public
name|boolean
name|containsValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|containsValue
argument_list|(
name|value
argument_list|)
return|;
block|}
DECL|method|get (Object key)
specifier|public
name|V
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
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
DECL|method|put (K key, V value)
specifier|public
name|V
name|put
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|putAll (Map<? extends K, ? extends V> map)
specifier|public
name|void
name|putAll
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
name|map
parameter_list|)
block|{
name|delegate
argument_list|()
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
DECL|method|keySet ()
specifier|public
name|Set
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|keySet
argument_list|()
return|;
block|}
DECL|method|values ()
specifier|public
name|Collection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|values
argument_list|()
return|;
block|}
DECL|method|entrySet ()
specifier|public
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|entrySet
argument_list|()
return|;
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
return|return
name|object
operator|==
name|this
operator|||
name|delegate
argument_list|()
operator|.
name|equals
argument_list|(
name|object
argument_list|)
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
return|return
name|delegate
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
end_class

end_unit

