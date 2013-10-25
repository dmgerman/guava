begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
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
comment|/**  * Custom GWT serializer for {@link Absent}.  *  *<p>GWT can serialize an absent {@code Optional} on its own, but the resulting object is a  * different instance than the singleton {@code Absent.INSTANCE}, which breaks equality. We  * implement a custom serializer to maintain the singleton property.  *  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Absent_CustomFieldSerializer
specifier|public
class|class
name|Absent_CustomFieldSerializer
block|{
DECL|method|deserialize (SerializationStreamReader reader, Absent<?> instance)
specifier|public
specifier|static
name|void
name|deserialize
parameter_list|(
name|SerializationStreamReader
name|reader
parameter_list|,
name|Absent
argument_list|<
name|?
argument_list|>
name|instance
parameter_list|)
block|{}
DECL|method|instantiate (SerializationStreamReader reader)
specifier|public
specifier|static
name|Absent
argument_list|<
name|?
argument_list|>
name|instantiate
parameter_list|(
name|SerializationStreamReader
name|reader
parameter_list|)
block|{
return|return
name|Absent
operator|.
name|INSTANCE
return|;
block|}
DECL|method|serialize (SerializationStreamWriter writer, Absent<?> instance)
specifier|public
specifier|static
name|void
name|serialize
parameter_list|(
name|SerializationStreamWriter
name|writer
parameter_list|,
name|Absent
argument_list|<
name|?
argument_list|>
name|instance
parameter_list|)
block|{}
block|}
end_class

end_unit

