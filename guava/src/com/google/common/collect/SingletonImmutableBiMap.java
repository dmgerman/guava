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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|CollectPreconditions
operator|.
name|checkEntryNotNull
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
name|errorprone
operator|.
name|annotations
operator|.
name|concurrent
operator|.
name|LazyInit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|j2objc
operator|.
name|annotations
operator|.
name|RetainedWith
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BiConsumer
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
DECL|class|SingletonImmutableBiMap
specifier|final
class|class
name|SingletonImmutableBiMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableBiMap
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
DECL|method|SingletonImmutableBiMap (K singleKey, V singleValue)
name|SingletonImmutableBiMap
parameter_list|(
name|K
name|singleKey
parameter_list|,
name|V
name|singleValue
parameter_list|)
block|{
name|checkEntryNotNull
argument_list|(
name|singleKey
argument_list|,
name|singleValue
argument_list|)
expr_stmt|;
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
DECL|method|SingletonImmutableBiMap (K singleKey, V singleValue, ImmutableBiMap<V, K> inverse)
specifier|private
name|SingletonImmutableBiMap
parameter_list|(
name|K
name|singleKey
parameter_list|,
name|V
name|singleValue
parameter_list|,
name|ImmutableBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
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
name|this
operator|.
name|inverse
operator|=
name|inverse
expr_stmt|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
DECL|method|forEach (BiConsumer<? super K, ? super V> action)
specifier|public
name|void
name|forEach
parameter_list|(
name|BiConsumer
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
super|super
name|V
argument_list|>
name|action
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|action
argument_list|)
operator|.
name|accept
argument_list|(
name|singleKey
argument_list|,
name|singleValue
argument_list|)
expr_stmt|;
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
name|singleKey
operator|.
name|equals
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|containsValue (@ullable Object value)
specifier|public
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
name|singleValue
operator|.
name|equals
argument_list|(
name|value
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
annotation|@
name|Override
DECL|method|createEntrySet ()
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
name|of
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|singleKey
argument_list|,
name|singleValue
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createKeySet ()
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
name|of
argument_list|(
name|singleKey
argument_list|)
return|;
block|}
annotation|@
name|LazyInit
annotation|@
name|RetainedWith
DECL|field|inverse
specifier|transient
name|ImmutableBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
decl_stmt|;
annotation|@
name|Override
DECL|method|inverse ()
specifier|public
name|ImmutableBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
parameter_list|()
block|{
comment|// racy single-check idiom
name|ImmutableBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|result
init|=
name|inverse
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
return|return
name|inverse
operator|=
operator|new
name|SingletonImmutableBiMap
argument_list|<>
argument_list|(
name|singleValue
argument_list|,
name|singleKey
argument_list|,
name|this
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|result
return|;
block|}
block|}
block|}
end_class

end_unit

