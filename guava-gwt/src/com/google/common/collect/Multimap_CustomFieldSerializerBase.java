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
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|SerializationException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|SerializationStreamReader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|SerializationStreamWriter
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

begin_comment
comment|/**  * This class contains static utility methods for writing {@code Multimap} GWT  * field serializers. Serializers should delegate to  * {@link #serialize(SerializationStreamWriter, Multimap)} and to either  * {@link #instantiate(SerializationStreamReader, ImmutableMultimap.Builder)} or  * {@link #populate(SerializationStreamReader, Multimap)}.  *  * @author Chris Povirk  */
end_comment

begin_class
DECL|class|Multimap_CustomFieldSerializerBase
specifier|public
specifier|final
class|class
name|Multimap_CustomFieldSerializerBase
block|{
DECL|method|instantiate ( SerializationStreamReader reader, ImmutableMultimap.Builder<Object, Object> builder)
specifier|static
name|ImmutableMultimap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|instantiate
parameter_list|(
name|SerializationStreamReader
name|reader
parameter_list|,
name|ImmutableMultimap
operator|.
name|Builder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
parameter_list|)
throws|throws
name|SerializationException
block|{
name|int
name|keyCount
init|=
name|reader
operator|.
name|readInt
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|keyCount
condition|;
operator|++
name|i
control|)
block|{
name|Object
name|key
init|=
name|reader
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|int
name|valueCount
init|=
name|reader
operator|.
name|readInt
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|valueCount
condition|;
operator|++
name|j
control|)
block|{
name|Object
name|value
init|=
name|reader
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
DECL|method|populate ( SerializationStreamReader reader, Multimap<Object, Object> multimap)
specifier|public
specifier|static
name|Multimap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|populate
parameter_list|(
name|SerializationStreamReader
name|reader
parameter_list|,
name|Multimap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|multimap
parameter_list|)
throws|throws
name|SerializationException
block|{
name|int
name|keyCount
init|=
name|reader
operator|.
name|readInt
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|keyCount
condition|;
operator|++
name|i
control|)
block|{
name|Object
name|key
init|=
name|reader
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|int
name|valueCount
init|=
name|reader
operator|.
name|readInt
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|valueCount
condition|;
operator|++
name|j
control|)
block|{
name|Object
name|value
init|=
name|reader
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|multimap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|multimap
return|;
block|}
DECL|method|serialize (SerializationStreamWriter writer, Multimap<?, ?> instance)
specifier|public
specifier|static
name|void
name|serialize
parameter_list|(
name|SerializationStreamWriter
name|writer
parameter_list|,
name|Multimap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|instance
parameter_list|)
throws|throws
name|SerializationException
block|{
name|writer
operator|.
name|writeInt
argument_list|(
name|instance
operator|.
name|asMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
extends|extends
name|Collection
argument_list|<
name|?
argument_list|>
argument_list|>
name|entry
range|:
name|instance
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|writer
operator|.
name|writeObject
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeInt
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|writer
operator|.
name|writeObject
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|Multimap_CustomFieldSerializerBase ()
specifier|private
name|Multimap_CustomFieldSerializerBase
parameter_list|()
block|{}
block|}
end_class

end_unit

