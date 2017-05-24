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
comment|/**  * An interface for representing and manipulating an origin node's adjacent nodes and incident edges  * in a {@link Network}.  *  * @author James Sexton  * @param<N> Node parameter type  * @param<E> Edge parameter type  */
end_comment

begin_interface
DECL|interface|NetworkConnections
interface|interface
name|NetworkConnections
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
comment|/**    * Returns the set of edges connecting the origin node to {@code node}. For networks without    * parallel edges, this set cannot be of size greater than one.    */
DECL|method|edgesConnecting (N node)
name|Set
argument_list|<
name|E
argument_list|>
name|edgesConnecting
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
comment|/**    * Returns the node that is adjacent to the origin node along {@code edge}.    *    *<p>In the directed case, {@code edge} is assumed to be an outgoing edge.    */
DECL|method|adjacentNode (E edge)
name|N
name|adjacentNode
parameter_list|(
name|E
name|edge
parameter_list|)
function_decl|;
comment|/**    * Remove {@code edge} from the set of incoming edges. Returns the former predecessor node.    *    *<p>In the undirected case, returns {@code null} if {@code isSelfLoop} is true.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|removeInEdge (E edge, boolean isSelfLoop)
name|N
name|removeInEdge
parameter_list|(
name|E
name|edge
parameter_list|,
name|boolean
name|isSelfLoop
parameter_list|)
function_decl|;
comment|/** Remove {@code edge} from the set of outgoing edges. Returns the former successor node. */
annotation|@
name|CanIgnoreReturnValue
DECL|method|removeOutEdge (E edge)
name|N
name|removeOutEdge
parameter_list|(
name|E
name|edge
parameter_list|)
function_decl|;
comment|/**    * Add {@code edge} to the set of incoming edges. Implicitly adds {@code node} as a predecessor.    */
DECL|method|addInEdge (E edge, N node, boolean isSelfLoop)
name|void
name|addInEdge
parameter_list|(
name|E
name|edge
parameter_list|,
name|N
name|node
parameter_list|,
name|boolean
name|isSelfLoop
parameter_list|)
function_decl|;
comment|/** Add {@code edge} to the set of outgoing edges. Implicitly adds {@code node} as a successor. */
DECL|method|addOutEdge (E edge, N node)
name|void
name|addOutEdge
parameter_list|(
name|E
name|edge
parameter_list|,
name|N
name|node
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

