begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/** Holder for extra methods of {@code Objects} only in web. */
end_comment

begin_class
DECL|class|ExtraObjectsMethodsForWeb
specifier|abstract
class|class
name|ExtraObjectsMethodsForWeb
block|{
DECL|method|equal (@ullable String a, @Nullable String b)
specifier|public
specifier|static
name|boolean
name|equal
parameter_list|(
annotation|@
name|Nullable
name|String
name|a
parameter_list|,
annotation|@
name|Nullable
name|String
name|b
parameter_list|)
block|{
return|return
name|a
operator|==
name|b
return|;
block|}
block|}
end_class

end_unit

