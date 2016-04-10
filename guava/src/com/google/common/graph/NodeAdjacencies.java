begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.graph
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|graph
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * An interface for representing an origin node's adjacent nodes in a network.  *  * @author James Sexton  * @param<N> Node parameter type  */
end_comment

begin_interface
DECL|interface|NodeAdjacencies
interface|interface
name|NodeAdjacencies
parameter_list|<
name|N
parameter_list|>
block|{
DECL|method|adjacentNodes ()
name|Set
argument_list|<
name|N
argument_list|>
name|adjacentNodes
parameter_list|()
function_decl|;
DECL|method|predecessors ()
name|Set
argument_list|<
name|N
argument_list|>
name|predecessors
parameter_list|()
function_decl|;
DECL|method|successors ()
name|Set
argument_list|<
name|N
argument_list|>
name|successors
parameter_list|()
function_decl|;
comment|/**    * Remove {@code node} from the set of predecessors.    *    * @return true iff the adjacency relationships changed    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|removePredecessor (Object node)
name|boolean
name|removePredecessor
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * Remove {@code node} from the set of successors.    *    * @return true iff the adjacency relationships changed    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|removeSuccessor (Object node)
name|boolean
name|removeSuccessor
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * Add {@code node} as a predecessor to the origin node.    * In the case of an undirected graph, it also becomes a successor.    *    * @return true iff the adjacency relationships changed    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addPredecessor (N node)
name|boolean
name|addPredecessor
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
comment|/**    * Add {@code node} as a successor to the origin node.    * In the case of an undirected graph, it also becomes a predecessor.    *    * @return true iff the adjacency relationships changed    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addSuccessor (N node)
name|boolean
name|addSuccessor
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

