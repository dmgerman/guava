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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Platform
operator|.
name|checkGwtRpcEnabled
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

begin_comment
comment|/**  * This class implements the GWT serialization of {@link ArrayListMultimap}.  *  * @author Chris Povirk  */
end_comment

begin_class
DECL|class|ArrayListMultimap_CustomFieldSerializer
specifier|public
class|class
name|ArrayListMultimap_CustomFieldSerializer
block|{
DECL|method|deserialize (SerializationStreamReader in, ArrayListMultimap<?, ?> out)
specifier|public
specifier|static
name|void
name|deserialize
parameter_list|(
name|SerializationStreamReader
name|in
parameter_list|,
name|ArrayListMultimap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|out
parameter_list|)
block|{}
DECL|method|instantiate (SerializationStreamReader in)
specifier|public
specifier|static
name|ArrayListMultimap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|instantiate
parameter_list|(
name|SerializationStreamReader
name|in
parameter_list|)
throws|throws
name|SerializationException
block|{
name|checkGwtRpcEnabled
argument_list|()
expr_stmt|;
return|return
operator|(
name|ArrayListMultimap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
operator|)
name|Multimap_CustomFieldSerializerBase
operator|.
name|populate
argument_list|(
name|in
argument_list|,
name|ArrayListMultimap
operator|.
name|create
argument_list|()
argument_list|)
return|;
block|}
DECL|method|serialize (SerializationStreamWriter out, ArrayListMultimap<?, ?> multimap)
specifier|public
specifier|static
name|void
name|serialize
parameter_list|(
name|SerializationStreamWriter
name|out
parameter_list|,
name|ArrayListMultimap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|multimap
parameter_list|)
throws|throws
name|SerializationException
block|{
name|checkGwtRpcEnabled
argument_list|()
expr_stmt|;
name|Multimap_CustomFieldSerializerBase
operator|.
name|serialize
argument_list|(
name|out
argument_list|,
name|multimap
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

