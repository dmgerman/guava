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
comment|/**  * A factory for copying nodes in binary search trees with different children.  *  *<p>Typically, nodes will carry more information than the fields in the {@link BSTNode} class,  * often some kind of value or some aggregate data for the subtree. This factory is responsible for  * copying this additional data between nodes.  *  * @author Louis Wasserman  * @param<N> The type of the tree nodes constructed with this {@code BSTNodeFactory}.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|BSTNodeFactory
specifier|abstract
class|class
name|BSTNodeFactory
parameter_list|<
name|K
parameter_list|,
name|N
extends|extends
name|BSTNode
parameter_list|<
name|K
parameter_list|,
name|N
parameter_list|>
parameter_list|>
block|{
comment|/**    * Returns a new {@code N} with the key and value data from {@code source}, with left child    * {@code left}, and right child {@code right}. If {@code left} or {@code right} is null, the    * returned node will not have a child on the corresponding side.    */
DECL|method|createNode (N source, @Nullable N left, @Nullable N right)
specifier|public
specifier|abstract
name|N
name|createNode
parameter_list|(
name|N
name|source
parameter_list|,
annotation|@
name|Nullable
name|N
name|left
parameter_list|,
annotation|@
name|Nullable
name|N
name|right
parameter_list|)
function_decl|;
comment|/**    * Returns a new {@code N} with the key and value data from {@code source} that is a leaf.    */
DECL|method|createLeaf (N source)
specifier|public
specifier|final
name|N
name|createLeaf
parameter_list|(
name|N
name|source
parameter_list|)
block|{
return|return
name|createNode
argument_list|(
name|source
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
end_class

end_unit

