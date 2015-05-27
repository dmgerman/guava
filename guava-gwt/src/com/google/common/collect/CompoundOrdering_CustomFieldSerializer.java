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
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_comment
comment|/**  * This class implements the GWT serialization of {@link CompoundOrdering}.  *  * @author Chris Povirk  */
end_comment

begin_class
DECL|class|CompoundOrdering_CustomFieldSerializer
specifier|public
class|class
name|CompoundOrdering_CustomFieldSerializer
block|{
DECL|method|deserialize (SerializationStreamReader reader, CompoundOrdering<?> instance)
specifier|public
specifier|static
name|void
name|deserialize
parameter_list|(
name|SerializationStreamReader
name|reader
parameter_list|,
name|CompoundOrdering
argument_list|<
name|?
argument_list|>
name|instance
parameter_list|)
block|{}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// deserialization is unsafe
DECL|method|instantiate (SerializationStreamReader reader)
specifier|public
specifier|static
name|CompoundOrdering
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
return|return
operator|new
name|CompoundOrdering
argument_list|<
name|Object
argument_list|>
argument_list|(
operator|(
name|ImmutableList
argument_list|<
name|Comparator
argument_list|<
name|Object
argument_list|>
argument_list|>
operator|)
name|reader
operator|.
name|readObject
argument_list|()
argument_list|)
return|;
block|}
DECL|method|serialize (SerializationStreamWriter writer, CompoundOrdering<?> instance)
specifier|public
specifier|static
name|void
name|serialize
parameter_list|(
name|SerializationStreamWriter
name|writer
parameter_list|,
name|CompoundOrdering
argument_list|<
name|?
argument_list|>
name|instance
parameter_list|)
throws|throws
name|SerializationException
block|{
name|writer
operator|.
name|writeObject
argument_list|(
name|instance
operator|.
name|comparators
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

