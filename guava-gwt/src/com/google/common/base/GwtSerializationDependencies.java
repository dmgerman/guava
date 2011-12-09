begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS-IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Contains dummy collection implementations to convince GWT that part of  * serializing a collection is serializing its elements.  *  *<p>See {@linkplain com.google.common.collect.GwtSerializationDependencies the  * com.google.common.collect version} for more details.  *  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|GwtCompatible
comment|// None of these classes are instantiated, let alone serialized:
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
DECL|class|GwtSerializationDependencies
specifier|final
class|class
name|GwtSerializationDependencies
block|{
DECL|method|GwtSerializationDependencies ()
specifier|private
name|GwtSerializationDependencies
parameter_list|()
block|{}
block|}
end_class

end_unit

