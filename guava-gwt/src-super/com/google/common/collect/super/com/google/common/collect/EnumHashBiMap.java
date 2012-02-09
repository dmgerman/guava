begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|EnumMap
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
comment|/**  * A {@code BiMap} backed by an {@code EnumMap} instance for keys-to-values, and  * a {@code HashMap} instance for values-to-keys. Null keys are not permitted,  * but null values are. An {@code EnumHashBiMap} and its inverse are both  * serializable.  *   *<p>See the Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#BiMap">  * {@code BiMap}</a>.  *  * @author Mike Bostock  * @since 2.0 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|EnumHashBiMap
specifier|public
specifier|final
class|class
name|EnumHashBiMap
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
name|AbstractBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|keyType
specifier|private
specifier|transient
name|Class
argument_list|<
name|K
argument_list|>
name|keyType
decl_stmt|;
comment|/**    * Returns a new, empty {@code EnumHashBiMap} using the specified key type.    *    * @param keyType the key type    */
specifier|public
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
name|EnumHashBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
DECL|method|create (Class<K> keyType)
name|create
parameter_list|(
name|Class
argument_list|<
name|K
argument_list|>
name|keyType
parameter_list|)
block|{
return|return
operator|new
name|EnumHashBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|keyType
argument_list|)
return|;
block|}
comment|/**    * Constructs a new bimap with the same mappings as the specified map. If the    * specified map is an {@code EnumHashBiMap} or an {@link EnumBiMap}, the new    * bimap has the same key type as the input bimap. Otherwise, the specified    * map must contain at least one mapping, in order to determine the key type.    *    * @param map the map whose mappings are to be placed in this map    * @throws IllegalArgumentException if map is not an {@code EnumBiMap} or an    *     {@code EnumHashBiMap} instance and contains no mappings    */
specifier|public
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
name|EnumHashBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
DECL|method|create (Map<K, ? extends V> map)
name|create
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|map
parameter_list|)
block|{
name|EnumHashBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|bimap
init|=
name|create
argument_list|(
name|EnumBiMap
operator|.
name|inferKeyType
argument_list|(
name|map
argument_list|)
argument_list|)
decl_stmt|;
name|bimap
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
return|return
name|bimap
return|;
block|}
DECL|method|EnumHashBiMap (Class<K> keyType)
specifier|private
name|EnumHashBiMap
parameter_list|(
name|Class
argument_list|<
name|K
argument_list|>
name|keyType
parameter_list|)
block|{
name|super
argument_list|(
name|WellBehavedMap
operator|.
name|wrap
argument_list|(
operator|new
name|EnumMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|keyType
argument_list|)
argument_list|)
argument_list|,
name|Maps
operator|.
expr|<
name|V
argument_list|,
name|K
operator|>
name|newHashMapWithExpectedSize
argument_list|(
name|keyType
operator|.
name|getEnumConstants
argument_list|()
operator|.
name|length
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|keyType
operator|=
name|keyType
expr_stmt|;
block|}
comment|// Overriding these two methods to show that values may be null (but not keys)
DECL|method|put (K key, @Nullable V value)
annotation|@
name|Override
specifier|public
name|V
name|put
parameter_list|(
name|K
name|key
parameter_list|,
annotation|@
name|Nullable
name|V
name|value
parameter_list|)
block|{
return|return
name|super
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|forcePut (K key, @Nullable V value)
annotation|@
name|Override
specifier|public
name|V
name|forcePut
parameter_list|(
name|K
name|key
parameter_list|,
annotation|@
name|Nullable
name|V
name|value
parameter_list|)
block|{
return|return
name|super
operator|.
name|forcePut
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/** Returns the associated key type. */
DECL|method|keyType ()
specifier|public
name|Class
argument_list|<
name|K
argument_list|>
name|keyType
parameter_list|()
block|{
return|return
name|keyType
return|;
block|}
block|}
end_class

end_unit

