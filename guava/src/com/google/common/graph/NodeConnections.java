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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * An interface for representing an origin node's adjacent nodes and incident edges in a network.  *  * @author James Sexton  * @param<N> Node parameter type  * @param<E> Edge parameter type  */
end_comment

begin_interface
DECL|interface|NodeConnections
interface|interface
name|NodeConnections
parameter_list|<
name|N
parameter_list|,
name|E
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
DECL|method|incidentEdges ()
name|Set
argument_list|<
name|E
argument_list|>
name|incidentEdges
parameter_list|()
function_decl|;
DECL|method|inEdges ()
name|Set
argument_list|<
name|E
argument_list|>
name|inEdges
parameter_list|()
function_decl|;
DECL|method|outEdges ()
name|Set
argument_list|<
name|E
argument_list|>
name|outEdges
parameter_list|()
function_decl|;
comment|/**    * Remove {@code edge} from the sets of in edges. If this is known to be the last remaining edge    * between two (formerly) connected nodes, {@link #removePredecessor} must also be called.    */
DECL|method|removeInEdge (Object edge)
name|void
name|removeInEdge
parameter_list|(
name|Object
name|edge
parameter_list|)
function_decl|;
comment|/**    * Remove {@code edge} from the sets of out edges. If this is known to be the last remaining edge    * between two (formerly) connected nodes, {@link #removeSuccessor} must also be called.    */
DECL|method|removeOutEdge (Object edge)
name|void
name|removeOutEdge
parameter_list|(
name|Object
name|edge
parameter_list|)
function_decl|;
comment|/**    * Remove {@code node} from the set of predecessors.    */
DECL|method|removePredecessor (Object node)
name|void
name|removePredecessor
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * Remove {@code node} from the set of successors.    */
DECL|method|removeSuccessor (Object node)
name|void
name|removeSuccessor
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * Add {@code node} as a predecessor to the origin node, connected with {@code edge}.    * In the case of an undirected graph, it also becomes a successor.    */
DECL|method|addPredecessor (N node, E edge)
name|void
name|addPredecessor
parameter_list|(
name|N
name|node
parameter_list|,
name|E
name|edge
parameter_list|)
function_decl|;
comment|/**    * Add {@code node} as a successor to the origin node, connected with {@code edge}.    * In the case of an undirected graph, it also becomes a predecessor.    */
DECL|method|addSuccessor (N node, E edge)
name|void
name|addSuccessor
parameter_list|(
name|N
name|node
parameter_list|,
name|E
name|edge
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

