begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Map
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
comment|/**  * Implementation of {@link ImmutableMap} with exactly one entry.  *  * @author Jesse Wilson  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|,
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// uses writeReplace(), not default serialization
DECL|class|SingletonImmutableMap
specifier|final
class|class
name|SingletonImmutableMap
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
DECL|field|singleKey
specifier|final
specifier|transient
name|K
name|singleKey
decl_stmt|;
DECL|field|singleValue
specifier|final
specifier|transient
name|V
name|singleValue
decl_stmt|;
DECL|field|entry
specifier|private
specifier|transient
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
decl_stmt|;
DECL|method|SingletonImmutableMap (K singleKey, V singleValue)
name|SingletonImmutableMap
parameter_list|(
name|K
name|singleKey
parameter_list|,
name|V
name|singleValue
parameter_list|)
block|{
name|this
operator|.
name|singleKey
operator|=
name|singleKey
expr_stmt|;
name|this
operator|.
name|singleValue
operator|=
name|singleValue
expr_stmt|;
block|}
DECL|method|SingletonImmutableMap (Entry<K, V> entry)
name|SingletonImmutableMap
parameter_list|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|)
block|{
name|this
operator|.
name|entry
operator|=
name|entry
expr_stmt|;
name|this
operator|.
name|singleKey
operator|=
name|entry
operator|.
name|getKey
argument_list|()
expr_stmt|;
name|this
operator|.
name|singleValue
operator|=
name|entry
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
DECL|method|entry ()
specifier|private
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|()
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|e
init|=
name|entry
decl_stmt|;
return|return
operator|(
name|e
operator|==
literal|null
operator|)
condition|?
operator|(
name|entry
operator|=
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|singleKey
argument_list|,
name|singleValue
argument_list|)
operator|)
else|:
name|e
return|;
block|}
DECL|method|get (Object key)
annotation|@
name|Override
specifier|public
name|V
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|singleKey
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|?
name|singleValue
else|:
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
DECL|method|isEmpty ()
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|containsKey (Object key)
annotation|@
name|Override
specifier|public
name|boolean
name|containsKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|singleKey
operator|.
name|equals
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|containsValue (Object value)
annotation|@
name|Override
specifier|public
name|boolean
name|containsValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|singleValue
operator|.
name|equals
argument_list|(
name|value
argument_list|)
return|;
block|}
DECL|method|isPartialView ()
annotation|@
name|Override
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|field|entrySet
specifier|private
specifier|transient
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entrySet
decl_stmt|;
DECL|method|entrySet ()
annotation|@
name|Override
specifier|public
name|ImmutableSet
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
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|es
init|=
name|entrySet
decl_stmt|;
return|return
operator|(
name|es
operator|==
literal|null
operator|)
condition|?
operator|(
name|entrySet
operator|=
name|ImmutableSet
operator|.
name|of
argument_list|(
name|entry
argument_list|()
argument_list|)
operator|)
else|:
name|es
return|;
block|}
DECL|field|keySet
specifier|private
specifier|transient
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|keySet
decl_stmt|;
DECL|method|keySet ()
annotation|@
name|Override
specifier|public
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|ks
init|=
name|keySet
decl_stmt|;
return|return
operator|(
name|ks
operator|==
literal|null
operator|)
condition|?
operator|(
name|keySet
operator|=
name|ImmutableSet
operator|.
name|of
argument_list|(
name|singleKey
argument_list|)
operator|)
else|:
name|ks
return|;
block|}
DECL|field|values
specifier|private
specifier|transient
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|values
decl_stmt|;
DECL|method|values ()
annotation|@
name|Override
specifier|public
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|v
init|=
name|values
decl_stmt|;
return|return
operator|(
name|v
operator|==
literal|null
operator|)
condition|?
operator|(
name|values
operator|=
operator|new
name|Values
argument_list|<
name|V
argument_list|>
argument_list|(
name|singleValue
argument_list|)
operator|)
else|:
name|v
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// uses writeReplace(), not default serialization
DECL|class|Values
specifier|private
specifier|static
class|class
name|Values
parameter_list|<
name|V
parameter_list|>
extends|extends
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
block|{
DECL|field|singleValue
specifier|final
name|V
name|singleValue
decl_stmt|;
DECL|method|Values (V singleValue)
name|Values
parameter_list|(
name|V
name|singleValue
parameter_list|)
block|{
name|this
operator|.
name|singleValue
operator|=
name|singleValue
expr_stmt|;
block|}
DECL|method|contains (Object object)
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
return|return
name|singleValue
operator|.
name|equals
argument_list|(
name|object
argument_list|)
return|;
block|}
DECL|method|isEmpty ()
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
name|UnmodifiableIterator
argument_list|<
name|V
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|singletonIterator
argument_list|(
name|singleValue
argument_list|)
return|;
block|}
DECL|method|isPartialView ()
annotation|@
name|Override
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
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
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|object
operator|instanceof
name|Map
condition|)
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|that
init|=
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|that
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|entry
init|=
name|that
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
return|return
name|singleKey
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|&&
name|singleValue
operator|.
name|equals
argument_list|(
name|entry
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
return|return
name|singleKey
operator|.
name|hashCode
argument_list|()
operator|^
name|singleValue
operator|.
name|hashCode
argument_list|()
return|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
operator|.
name|append
argument_list|(
name|singleKey
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|singleValue
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

