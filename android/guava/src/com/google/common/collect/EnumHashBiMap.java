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
name|annotations
operator|.
name|GwtIncompatible
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
name|CanIgnoreReturnValue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
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
name|HashMap
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
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_comment
comment|/**  * A {@code BiMap} backed by an {@code EnumMap} instance for keys-to-values, and a {@code HashMap}  * instance for values-to-keys. Null keys are not permitted, but null values are. An {@code  * EnumHashBiMap} and its inverse are both serializable.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#bimap"> {@code BiMap}</a>.  *  * @author Mike Bostock  * @since 2.0  */
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
DECL|method|create (Class<K> keyType)
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
argument_list|<>
argument_list|(
name|keyType
argument_list|)
return|;
block|}
comment|/**    * Constructs a new bimap with the same mappings as the specified map. If the specified map is an    * {@code EnumHashBiMap} or an {@link EnumBiMap}, the new bimap has the same key type as the input    * bimap. Otherwise, the specified map must contain at least one mapping, in order to determine    * the key type.    *    * @param map the map whose mappings are to be placed in this map    * @throws IllegalArgumentException if map is not an {@code EnumBiMap} or an {@code EnumHashBiMap}    *     instance and contains no mappings    */
DECL|method|create (Map<K, ? extends V> map)
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
comment|// Overriding these 3 methods to show that values may be null (but not keys)
annotation|@
name|Override
DECL|method|checkKey (K key)
name|K
name|checkKey
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|checkNotNull
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|put (K key, @NullableDecl V value)
specifier|public
name|V
name|put
parameter_list|(
name|K
name|key
parameter_list|,
annotation|@
name|NullableDecl
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
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|forcePut (K key, @NullableDecl V value)
specifier|public
name|V
name|forcePut
parameter_list|(
name|K
name|key
parameter_list|,
annotation|@
name|NullableDecl
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
comment|/**    * @serialData the key class, number of entries, first key, first value, second key, second value,    *     and so on.    */
annotation|@
name|GwtIncompatible
comment|// java.io.ObjectOutputStream
DECL|method|writeObject (ObjectOutputStream stream)
specifier|private
name|void
name|writeObject
parameter_list|(
name|ObjectOutputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|stream
operator|.
name|defaultWriteObject
argument_list|()
expr_stmt|;
name|stream
operator|.
name|writeObject
argument_list|(
name|keyType
argument_list|)
expr_stmt|;
name|Serialization
operator|.
name|writeMap
argument_list|(
name|this
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// reading field populated by writeObject
annotation|@
name|GwtIncompatible
comment|// java.io.ObjectInputStream
DECL|method|readObject (ObjectInputStream stream)
specifier|private
name|void
name|readObject
parameter_list|(
name|ObjectInputStream
name|stream
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|stream
operator|.
name|defaultReadObject
argument_list|()
expr_stmt|;
name|keyType
operator|=
operator|(
name|Class
argument_list|<
name|K
argument_list|>
operator|)
name|stream
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|setDelegates
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
argument_list|,
operator|new
name|HashMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
argument_list|(
name|keyType
operator|.
name|getEnumConstants
argument_list|()
operator|.
name|length
operator|*
literal|3
operator|/
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|Serialization
operator|.
name|populateMap
argument_list|(
name|this
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// only needed in emulated source.
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

