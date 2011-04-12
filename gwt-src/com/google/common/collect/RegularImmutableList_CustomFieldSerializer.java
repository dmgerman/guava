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
name|core
operator|.
name|java
operator|.
name|util
operator|.
name|Collection_CustomFieldSerializerBase
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * This class implements the GWT serialization of {@link  * RegularImmutableList}.  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|RegularImmutableList_CustomFieldSerializer
specifier|public
class|class
name|RegularImmutableList_CustomFieldSerializer
block|{
DECL|method|deserialize (SerializationStreamReader reader, RegularImmutableList<?> instance)
specifier|public
specifier|static
name|void
name|deserialize
parameter_list|(
name|SerializationStreamReader
name|reader
parameter_list|,
name|RegularImmutableList
argument_list|<
name|?
argument_list|>
name|instance
parameter_list|)
block|{   }
DECL|method|instantiate ( SerializationStreamReader reader)
specifier|public
specifier|static
name|RegularImmutableList
argument_list|<
name|Object
argument_list|>
name|instantiate
parameter_list|(
name|SerializationStreamReader
name|reader
parameter_list|)
throws|throws
name|SerializationException
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|elements
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Collection_CustomFieldSerializerBase
operator|.
name|deserialize
argument_list|(
name|reader
argument_list|,
name|elements
argument_list|)
expr_stmt|;
comment|/*      * For this custom field serializer to be invoked, the list must have been      * RegularImmutableList before it's serialized.  Since RegularImmutableList      * always have one or more elements, ImmutableList.copyOf always return      * a RegularImmutableList back.      */
return|return
operator|(
name|RegularImmutableList
argument_list|<
name|Object
argument_list|>
operator|)
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|elements
argument_list|)
return|;
block|}
DECL|method|serialize (SerializationStreamWriter writer, RegularImmutableList<?> instance)
specifier|public
specifier|static
name|void
name|serialize
parameter_list|(
name|SerializationStreamWriter
name|writer
parameter_list|,
name|RegularImmutableList
argument_list|<
name|?
argument_list|>
name|instance
parameter_list|)
throws|throws
name|SerializationException
block|{
name|Collection_CustomFieldSerializerBase
operator|.
name|serialize
argument_list|(
name|writer
argument_list|,
name|instance
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

