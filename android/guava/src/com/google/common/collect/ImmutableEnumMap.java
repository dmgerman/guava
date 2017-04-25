begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|checkArgument
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
name|GwtCompatible
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
operator|.
name|IteratorBasedImmutableMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumMap
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
comment|/**  * Implementation of {@link ImmutableMap} backed by a non-empty {@link  * java.util.EnumMap}.  *  * @author Louis Wasserman  */
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
comment|// we're overriding default serialization
DECL|class|ImmutableEnumMap
specifier|final
class|class
name|ImmutableEnumMap
parameter_list|<
name|K
extends|extends
name|Enum
parameter_list|<
name|K
parameter_list|>
parameter_list|,
name|V
parameter_list|>
extends|extends
name|IteratorBasedImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|asImmutable (EnumMap<K, V> map)
specifier|static
parameter_list|<
name|K
extends|extends
name|Enum
argument_list|<
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|asImmutable
parameter_list|(
name|EnumMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
switch|switch
condition|(
name|map
operator|.
name|size
argument_list|()
condition|)
block|{
case|case
literal|0
case|:
return|return
name|ImmutableMap
operator|.
name|of
argument_list|()
return|;
case|case
literal|1
case|:
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|map
operator|.
name|entrySet
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|ImmutableMap
operator|.
name|of
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
default|default:
return|return
operator|new
name|ImmutableEnumMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|map
argument_list|)
return|;
block|}
block|}
DECL|field|delegate
specifier|private
specifier|final
specifier|transient
name|EnumMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
decl_stmt|;
DECL|method|ImmutableEnumMap (EnumMap<K, V> delegate)
specifier|private
name|ImmutableEnumMap
parameter_list|(
name|EnumMap
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
name|delegate
expr_stmt|;
name|checkArgument
argument_list|(
operator|!
name|delegate
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|keyIterator ()
name|UnmodifiableIterator
argument_list|<
name|K
argument_list|>
name|keyIterator
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|unmodifiableIterator
argument_list|(
name|delegate
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
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
name|delegate
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|containsKey (@ullable Object key)
specifier|public
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
name|delegate
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
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
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object object)
specifier|public
name|boolean
name|equals
parameter_list|(
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
name|ImmutableEnumMap
condition|)
block|{
name|object
operator|=
operator|(
operator|(
name|ImmutableEnumMap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
operator|)
operator|.
name|delegate
expr_stmt|;
block|}
return|return
name|delegate
operator|.
name|equals
argument_list|(
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|entryIterator ()
name|UnmodifiableIterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entryIterator
parameter_list|()
block|{
return|return
name|Maps
operator|.
name|unmodifiableEntryIterator
argument_list|(
name|delegate
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|// All callers of the constructor are restricted to<K extends Enum<K>>.
annotation|@
name|Override
DECL|method|writeReplace ()
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|EnumSerializedForm
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|delegate
argument_list|)
return|;
block|}
comment|/*    * This class is used to serialize ImmutableEnumMap instances.    */
DECL|class|EnumSerializedForm
specifier|private
specifier|static
class|class
name|EnumSerializedForm
parameter_list|<
name|K
extends|extends
name|Enum
parameter_list|<
name|K
parameter_list|>
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Serializable
block|{
DECL|field|delegate
specifier|final
name|EnumMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
decl_stmt|;
DECL|method|EnumSerializedForm (EnumMap<K, V> delegate)
name|EnumSerializedForm
parameter_list|(
name|EnumMap
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
name|delegate
expr_stmt|;
block|}
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
return|return
operator|new
name|ImmutableEnumMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|delegate
argument_list|)
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
block|}
end_class

end_unit
