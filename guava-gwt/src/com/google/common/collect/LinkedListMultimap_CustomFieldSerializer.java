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
name|Map
operator|.
name|Entry
import|;
end_import

begin_comment
comment|/**  * This class implements the GWT serialization of {@link LinkedListMultimap}.  *  * @author Chris Povirk  */
end_comment

begin_class
DECL|class|LinkedListMultimap_CustomFieldSerializer
specifier|public
class|class
name|LinkedListMultimap_CustomFieldSerializer
block|{
DECL|method|deserialize (SerializationStreamReader in, LinkedListMultimap<?, ?> out)
specifier|public
specifier|static
name|void
name|deserialize
parameter_list|(
name|SerializationStreamReader
name|in
parameter_list|,
name|LinkedListMultimap
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
name|LinkedListMultimap
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
name|LinkedListMultimap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|multimap
init|=
name|LinkedListMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
name|int
name|size
init|=
name|in
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
name|size
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|key
init|=
name|in
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|in
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
return|return
name|multimap
return|;
block|}
DECL|method|serialize (SerializationStreamWriter out, LinkedListMultimap<?, ?> multimap)
specifier|public
specifier|static
name|void
name|serialize
parameter_list|(
name|SerializationStreamWriter
name|out
parameter_list|,
name|LinkedListMultimap
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
name|out
operator|.
name|writeInt
argument_list|(
name|multimap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|entry
range|:
name|multimap
operator|.
name|entries
argument_list|()
control|)
block|{
name|out
operator|.
name|writeObject
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

