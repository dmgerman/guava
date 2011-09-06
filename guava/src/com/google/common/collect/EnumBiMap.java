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
name|annotations
operator|.
name|GwtIncompatible
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
name|Map
import|;
end_import

begin_comment
comment|/**  * A {@code BiMap} backed by two {@code EnumMap} instances. Null keys and values  * are not permitted. An {@code EnumBiMap} and its inverse are both  * serializable.  *  * @author Mike Bostock  * @since 2.0 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|EnumBiMap
specifier|public
specifier|final
class|class
name|EnumBiMap
parameter_list|<
name|K
extends|extends
name|Enum
parameter_list|<
name|K
parameter_list|>
parameter_list|,
name|V
extends|extends
name|Enum
parameter_list|<
name|V
parameter_list|>
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
DECL|field|valueType
specifier|private
specifier|transient
name|Class
argument_list|<
name|V
argument_list|>
name|valueType
decl_stmt|;
comment|/**    * Returns a new, empty {@code EnumBiMap} using the specified key and value    * types.    *    * @param keyType the key type    * @param valueType the value type    */
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
extends|extends
name|Enum
argument_list|<
name|V
argument_list|>
parameter_list|>
name|EnumBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
DECL|method|create (Class<K> keyType, Class<V> valueType)
name|create
parameter_list|(
name|Class
argument_list|<
name|K
argument_list|>
name|keyType
parameter_list|,
name|Class
argument_list|<
name|V
argument_list|>
name|valueType
parameter_list|)
block|{
return|return
operator|new
name|EnumBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|keyType
argument_list|,
name|valueType
argument_list|)
return|;
block|}
comment|/**    * Returns a new bimap with the same mappings as the specified map. If the    * specified map is an {@code EnumBiMap}, the new bimap has the same types as    * the provided map. Otherwise, the specified map must contain at least one    * mapping, in order to determine the key and value types.    *    * @param map the map whose mappings are to be placed in this map    * @throws IllegalArgumentException if map is not an {@code EnumBiMap}    *     instance and contains no mappings    */
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
extends|extends
name|Enum
argument_list|<
name|V
argument_list|>
parameter_list|>
name|EnumBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
DECL|method|create (Map<K, V> map)
name|create
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
name|EnumBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|bimap
init|=
name|create
argument_list|(
name|inferKeyType
argument_list|(
name|map
argument_list|)
argument_list|,
name|inferValueType
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
DECL|method|EnumBiMap (Class<K> keyType, Class<V> valueType)
specifier|private
name|EnumBiMap
parameter_list|(
name|Class
argument_list|<
name|K
argument_list|>
name|keyType
parameter_list|,
name|Class
argument_list|<
name|V
argument_list|>
name|valueType
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
name|WellBehavedMap
operator|.
name|wrap
argument_list|(
operator|new
name|EnumMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
argument_list|(
name|valueType
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|keyType
operator|=
name|keyType
expr_stmt|;
name|this
operator|.
name|valueType
operator|=
name|valueType
expr_stmt|;
block|}
DECL|method|inferKeyType (Map<K, ?> map)
specifier|static
parameter_list|<
name|K
extends|extends
name|Enum
argument_list|<
name|K
argument_list|>
parameter_list|>
name|Class
argument_list|<
name|K
argument_list|>
name|inferKeyType
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
name|map
operator|instanceof
name|EnumBiMap
condition|)
block|{
return|return
operator|(
operator|(
name|EnumBiMap
argument_list|<
name|K
argument_list|,
name|?
argument_list|>
operator|)
name|map
operator|)
operator|.
name|keyType
argument_list|()
return|;
block|}
if|if
condition|(
name|map
operator|instanceof
name|EnumHashBiMap
condition|)
block|{
return|return
operator|(
operator|(
name|EnumHashBiMap
argument_list|<
name|K
argument_list|,
name|?
argument_list|>
operator|)
name|map
operator|)
operator|.
name|keyType
argument_list|()
return|;
block|}
name|checkArgument
argument_list|(
operator|!
name|map
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getDeclaringClass
argument_list|()
return|;
block|}
DECL|method|inferValueType (Map<?, V> map)
specifier|private
specifier|static
parameter_list|<
name|V
extends|extends
name|Enum
argument_list|<
name|V
argument_list|>
parameter_list|>
name|Class
argument_list|<
name|V
argument_list|>
name|inferValueType
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
name|map
operator|instanceof
name|EnumBiMap
condition|)
block|{
return|return
operator|(
operator|(
name|EnumBiMap
argument_list|<
name|?
argument_list|,
name|V
argument_list|>
operator|)
name|map
operator|)
operator|.
name|valueType
return|;
block|}
name|checkArgument
argument_list|(
operator|!
name|map
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|map
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getDeclaringClass
argument_list|()
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
comment|/** Returns the associated value type. */
DECL|method|valueType ()
specifier|public
name|Class
argument_list|<
name|V
argument_list|>
name|valueType
parameter_list|()
block|{
return|return
name|valueType
return|;
block|}
comment|/**    * @serialData the key class, value class, number of entries, first key, first    *     value, second key, second value, and so on.    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.io.ObjectOutputStream"
argument_list|)
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
name|stream
operator|.
name|writeObject
argument_list|(
name|valueType
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
comment|// reading fields populated by writeObject
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.io.ObjectInputStream"
argument_list|)
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
name|valueType
operator|=
operator|(
name|Class
argument_list|<
name|V
argument_list|>
operator|)
name|stream
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|setDelegates
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
name|WellBehavedMap
operator|.
name|wrap
argument_list|(
operator|new
name|EnumMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
argument_list|(
name|valueType
argument_list|)
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
argument_list|(
literal|"not needed in emulated source."
argument_list|)
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

