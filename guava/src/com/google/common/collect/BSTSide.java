begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the  * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  * express or implied. See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * A side of a binary search tree node, used to index its children.  *  * @author lowasser@google.com (Louis Wasserman)  */
end_comment

begin_enum
annotation|@
name|GwtCompatible
DECL|enum|BSTSide
enum|enum
name|BSTSide
block|{
DECL|enumConstant|LEFT
name|LEFT
block|{
annotation|@
name|Override
specifier|public
name|BSTSide
name|other
parameter_list|()
block|{
return|return
name|RIGHT
return|;
block|}
block|}
block|,
DECL|enumConstant|RIGHT
name|RIGHT
block|{
annotation|@
name|Override
specifier|public
name|BSTSide
name|other
parameter_list|()
block|{
return|return
name|LEFT
return|;
block|}
block|}
block|;
DECL|method|other ()
specifier|abstract
name|BSTSide
name|other
parameter_list|()
function_decl|;
block|}
end_enum

end_unit

