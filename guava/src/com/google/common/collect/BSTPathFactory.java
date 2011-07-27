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
comment|/**  * A factory for extending paths in a binary search tree.  *  * @author Louis Wasserman  * @param<K> The key type of nodes of type {@code N}.  * @param<N> The type of binary search tree nodes used in the paths generated by this {@code  *        BSTPathFactory}.  * @param<P> The type of paths constructed by this {@code BSTPathFactory}.  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|BSTPathFactory
interface|interface
name|BSTPathFactory
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
parameter_list|,
name|P
extends|extends
name|BSTPath
parameter_list|<
name|K
parameter_list|,
name|N
parameter_list|,
name|P
parameter_list|>
parameter_list|>
block|{
comment|/**    * Returns this path extended by one node to the specified {@code side}.    */
DECL|method|extension (P path, BSTSide side)
name|P
name|extension
parameter_list|(
name|P
name|path
parameter_list|,
name|BSTSide
name|side
parameter_list|)
function_decl|;
comment|/**    * Returns the trivial path that starts at {@code root} and goes no further.    */
DECL|method|initialPath (N root)
name|P
name|initialPath
parameter_list|(
name|N
name|root
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

