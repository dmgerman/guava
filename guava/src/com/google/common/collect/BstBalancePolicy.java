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
comment|/**  * A local balancing policy for modified nodes in binary search trees.  *  * @author Louis Wasserman  * @param<N> The type of the nodes in the trees that this {@code BstRebalancePolicy} can  *        rebalance.  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|BstBalancePolicy
interface|interface
name|BstBalancePolicy
parameter_list|<
name|N
extends|extends
name|BstNode
parameter_list|<
name|?
parameter_list|,
name|N
parameter_list|>
parameter_list|>
block|{
comment|/**    * Constructs a locally balanced tree around the key and value data in {@code source}, and the    * subtrees {@code left} and {@code right}. It is guaranteed that the resulting tree will have    * the same inorder traversal order as the subtree {@code left}, then the entry {@code source},    * then the subtree {@code right}.    */
DECL|method|balance (BstNodeFactory<N> nodeFactory, N source, @Nullable N left, @Nullable N right)
name|N
name|balance
parameter_list|(
name|BstNodeFactory
argument_list|<
name|N
argument_list|>
name|nodeFactory
parameter_list|,
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
comment|/**    * Constructs a locally balanced tree around the subtrees {@code left} and {@code right}. It is    * guaranteed that the resulting tree will have the same inorder traversal order as the subtree    * {@code left}, then the subtree {@code right}.    */
annotation|@
name|Nullable
DECL|method|combine (BstNodeFactory<N> nodeFactory, @Nullable N left, @Nullable N right)
name|N
name|combine
parameter_list|(
name|BstNodeFactory
argument_list|<
name|N
argument_list|>
name|nodeFactory
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
block|}
end_interface

end_unit

