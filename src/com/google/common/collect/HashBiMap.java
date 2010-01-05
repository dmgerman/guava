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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A {@link BiMap} backed by two {@link HashMap} instances. This implementation  * allows null keys and values. A {@code HashBiMap} and its inverse are both  * serializable.  *  * @author Mike Bostock  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|HashBiMap
specifier|public
specifier|final
class|class
name|HashBiMap
parameter_list|<
name|K
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
comment|/**    * Returns a new, empty {@code HashBiMap} with the default initial capacity    * (16).    */
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|HashBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|HashBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Constructs a new, empty bimap with the specified expected size.    *    * @param expectedSize the expected number of entries    * @throws IllegalArgumentException if the specified expected size is    *     negative    */
DECL|method|create (int expectedSize)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|HashBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|int
name|expectedSize
parameter_list|)
block|{
return|return
operator|new
name|HashBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|expectedSize
argument_list|)
return|;
block|}
comment|/**    * Constructs a new bimap containing initial values from {@code map}. The    * bimap is created with an initial capacity sufficient to hold the mappings    * in the specified map.    */
DECL|method|create ( Map<? extends K, ? extends V> map)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|HashBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
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
name|HashBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|bimap
init|=
name|create
argument_list|(
name|map
operator|.
name|size
argument_list|()
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
DECL|method|HashBiMap ()
specifier|private
name|HashBiMap
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|HashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
argument_list|,
operator|new
name|HashMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|HashBiMap (int expectedSize)
specifier|private
name|HashBiMap
parameter_list|(
name|int
name|expectedSize
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|HashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|Maps
operator|.
name|capacity
argument_list|(
name|expectedSize
argument_list|)
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
name|Maps
operator|.
name|capacity
argument_list|(
name|expectedSize
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Override these two methods to show that keys and values may be null
DECL|method|put (@ullable K key, @Nullable V value)
annotation|@
name|Override
specifier|public
name|V
name|put
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
DECL|method|forcePut (@ullable K key, @Nullable V value)
annotation|@
name|Override
specifier|public
name|V
name|forcePut
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
comment|/**    * @serialData the number of entries, first key, first value, second key,    *     second value, and so on.    */
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
name|int
name|size
init|=
name|Serialization
operator|.
name|readCount
argument_list|(
name|stream
argument_list|)
decl_stmt|;
name|setDelegates
argument_list|(
name|Maps
operator|.
expr|<
name|K
argument_list|,
name|V
operator|>
name|newHashMapWithExpectedSize
argument_list|(
name|size
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
name|size
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
argument_list|,
name|size
argument_list|)
expr_stmt|;
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

