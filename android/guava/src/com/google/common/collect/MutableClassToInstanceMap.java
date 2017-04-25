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
name|GwtIncompatible
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
name|primitives
operator|.
name|Primitives
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
name|Serializable
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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

begin_comment
comment|/**  * A mutable class-to-instance map backed by an arbitrary user-provided map. See also {@link  * ImmutableClassToInstanceMap}.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#classtoinstancemap"> {@code  * ClassToInstanceMap}</a>.  *  * @author Kevin Bourrillion  * @since 2.0  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// using writeReplace instead of standard serialization
DECL|class|MutableClassToInstanceMap
specifier|public
specifier|final
class|class
name|MutableClassToInstanceMap
parameter_list|<
name|B
parameter_list|>
extends|extends
name|ForwardingMap
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
implements|implements
name|ClassToInstanceMap
argument_list|<
name|B
argument_list|>
implements|,
name|Serializable
block|{
comment|/**    * Returns a new {@code MutableClassToInstanceMap} instance backed by a {@link    * HashMap} using the default initial capacity and load factor.    */
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|B
parameter_list|>
name|MutableClassToInstanceMap
argument_list|<
name|B
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|MutableClassToInstanceMap
argument_list|<
name|B
argument_list|>
argument_list|(
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns a new {@code MutableClassToInstanceMap} instance backed by a given    * empty {@code backingMap}. The caller surrenders control of the backing map,    * and thus should not allow any direct references to it to remain accessible.    */
DECL|method|create (Map<Class<? extends B>, B> backingMap)
specifier|public
specifier|static
parameter_list|<
name|B
parameter_list|>
name|MutableClassToInstanceMap
argument_list|<
name|B
argument_list|>
name|create
parameter_list|(
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|backingMap
parameter_list|)
block|{
return|return
operator|new
name|MutableClassToInstanceMap
argument_list|<
name|B
argument_list|>
argument_list|(
name|backingMap
argument_list|)
return|;
block|}
DECL|field|delegate
specifier|private
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|delegate
decl_stmt|;
DECL|method|MutableClassToInstanceMap (Map<Class<? extends B>, B> delegate)
specifier|private
name|MutableClassToInstanceMap
parameter_list|(
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
DECL|method|checkedEntry (final Entry<Class<? extends B>, B> entry)
specifier|static
parameter_list|<
name|B
parameter_list|>
name|Entry
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|checkedEntry
parameter_list|(
specifier|final
name|Entry
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|entry
parameter_list|)
block|{
return|return
operator|new
name|ForwardingMapEntry
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Entry
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|entry
return|;
block|}
annotation|@
name|Override
specifier|public
name|B
name|setValue
parameter_list|(
name|B
name|value
parameter_list|)
block|{
return|return
name|super
operator|.
name|setValue
argument_list|(
name|cast
argument_list|(
name|getKey
argument_list|()
argument_list|,
name|value
argument_list|)
argument_list|)
return|;
block|}
block|}
empty_stmt|;
block|}
annotation|@
name|Override
DECL|method|entrySet ()
specifier|public
name|Set
argument_list|<
name|Entry
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
operator|new
name|ForwardingSet
argument_list|<
name|Entry
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
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
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|MutableClassToInstanceMap
operator|.
name|this
operator|.
name|delegate
argument_list|()
operator|.
name|entrySet
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|TransformedIterator
argument_list|<
name|Entry
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
argument_list|,
name|Entry
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
argument_list|>
argument_list|(
name|delegate
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
name|Entry
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|transform
parameter_list|(
name|Entry
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|from
parameter_list|)
block|{
return|return
name|checkedEntry
argument_list|(
name|from
argument_list|)
return|;
block|}
block|}
empty_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|standardToArray
argument_list|()
return|;
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
return|return
name|standardToArray
argument_list|(
name|array
argument_list|)
return|;
block|}
block|}
empty_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
DECL|method|put (Class<? extends B> key, B value)
specifier|public
name|B
name|put
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
name|key
parameter_list|,
name|B
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
name|cast
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|putAll (Map<? extends Class<? extends B>, ? extends B> map)
specifier|public
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|?
extends|extends
name|B
argument_list|>
name|map
parameter_list|)
block|{
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|copy
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
argument_list|(
name|map
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|?
extends|extends
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|entry
range|:
name|copy
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|cast
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
expr_stmt|;
block|}
name|super
operator|.
name|putAll
argument_list|(
name|copy
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|putInstance (Class<T> type, T value)
specifier|public
parameter_list|<
name|T
extends|extends
name|B
parameter_list|>
name|T
name|putInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|T
name|value
parameter_list|)
block|{
return|return
name|cast
argument_list|(
name|type
argument_list|,
name|put
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getInstance (Class<T> type)
specifier|public
parameter_list|<
name|T
extends|extends
name|B
parameter_list|>
name|T
name|getInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|cast
argument_list|(
name|type
argument_list|,
name|get
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
DECL|method|cast (Class<T> type, B value)
specifier|private
specifier|static
parameter_list|<
name|B
parameter_list|,
name|T
extends|extends
name|B
parameter_list|>
name|T
name|cast
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|B
name|value
parameter_list|)
block|{
return|return
name|Primitives
operator|.
name|wrap
argument_list|(
name|type
argument_list|)
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
DECL|method|writeReplace ()
specifier|private
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|SerializedForm
argument_list|(
name|delegate
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Serialized form of the map, to avoid serializing the constraint.    */
DECL|class|SerializedForm
specifier|private
specifier|static
specifier|final
class|class
name|SerializedForm
parameter_list|<
name|B
parameter_list|>
implements|implements
name|Serializable
block|{
DECL|field|backingMap
specifier|private
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|backingMap
decl_stmt|;
DECL|method|SerializedForm (Map<Class<? extends B>, B> backingMap)
name|SerializedForm
parameter_list|(
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|backingMap
parameter_list|)
block|{
name|this
operator|.
name|backingMap
operator|=
name|backingMap
expr_stmt|;
block|}
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|create
argument_list|(
name|backingMap
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
