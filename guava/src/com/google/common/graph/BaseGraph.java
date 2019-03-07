begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2017 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A non-public interface for the methods shared between {@link Graph} and {@link ValueGraph}.  *  * @author James Sexton  * @param<N> Node parameter type  */
end_comment

begin_interface
DECL|interface|BaseGraph
interface|interface
name|BaseGraph
parameter_list|<
name|N
parameter_list|>
extends|extends
name|SuccessorsFunction
argument_list|<
name|N
argument_list|>
extends|,
name|PredecessorsFunction
argument_list|<
name|N
argument_list|>
block|{
comment|//
comment|// Graph-level accessors
comment|//
comment|/** Returns all nodes in this graph, in the order specified by {@link #nodeOrder()}. */
DECL|method|nodes ()
name|Set
argument_list|<
name|N
argument_list|>
name|nodes
parameter_list|()
function_decl|;
comment|/** Returns all edges in this graph. */
DECL|method|edges ()
name|Set
argument_list|<
name|EndpointPair
argument_list|<
name|N
argument_list|>
argument_list|>
name|edges
parameter_list|()
function_decl|;
comment|//
comment|// Graph properties
comment|//
comment|/**    * Returns true if the edges in this graph are directed. Directed edges connect a {@link    * EndpointPair#source() source node} to a {@link EndpointPair#target() target node}, while    * undirected edges connect a pair of nodes to each other.    */
DECL|method|isDirected ()
name|boolean
name|isDirected
parameter_list|()
function_decl|;
comment|/**    * Returns true if this graph allows self-loops (edges that connect a node to itself). Attempting    * to add a self-loop to a graph that does not allow them will throw an {@link    * IllegalArgumentException}.    */
DECL|method|allowsSelfLoops ()
name|boolean
name|allowsSelfLoops
parameter_list|()
function_decl|;
comment|/** Returns the order of iteration for the elements of {@link #nodes()}. */
DECL|method|nodeOrder ()
name|ElementOrder
argument_list|<
name|N
argument_list|>
name|nodeOrder
parameter_list|()
function_decl|;
comment|//
comment|// Element-level accessors
comment|//
comment|/**    * Returns the nodes which have an incident edge in common with {@code node} in this graph.    *    * @throws IllegalArgumentException if {@code node} is not an element of this graph    */
DECL|method|adjacentNodes (N node)
name|Set
argument_list|<
name|N
argument_list|>
name|adjacentNodes
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
comment|/**    * Returns all nodes in this graph adjacent to {@code node} which can be reached by traversing    * {@code node}'s incoming edges<i>against</i> the direction (if any) of the edge.    *    *<p>In an undirected graph, this is equivalent to {@link #adjacentNodes(Object)}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this graph    */
annotation|@
name|Override
DECL|method|predecessors (N node)
name|Set
argument_list|<
name|N
argument_list|>
name|predecessors
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
comment|/**    * Returns all nodes in this graph adjacent to {@code node} which can be reached by traversing    * {@code node}'s outgoing edges in the direction (if any) of the edge.    *    *<p>In an undirected graph, this is equivalent to {@link #adjacentNodes(Object)}.    *    *<p>This is<i>not</i> the same as "all nodes reachable from {@code node} by following outgoing    * edges". For that functionality, see {@link Graphs#reachableNodes(Graph, Object)}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this graph    */
annotation|@
name|Override
DECL|method|successors (N node)
name|Set
argument_list|<
name|N
argument_list|>
name|successors
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
comment|/**    * Returns the edges in this graph whose endpoints include {@code node}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this graph    * @since 24.0    */
DECL|method|incidentEdges (N node)
name|Set
argument_list|<
name|EndpointPair
argument_list|<
name|N
argument_list|>
argument_list|>
name|incidentEdges
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
comment|/**    * Returns the count of {@code node}'s incident edges, counting self-loops twice (equivalently,    * the number of times an edge touches {@code node}).    *    *<p>For directed graphs, this is equal to {@code inDegree(node) + outDegree(node)}.    *    *<p>For undirected graphs, this is equal to {@code incidentEdges(node).size()} + (number of    * self-loops incident to {@code node}).    *    *<p>If the count is greater than {@code Integer.MAX_VALUE}, returns {@code Integer.MAX_VALUE}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this graph    */
DECL|method|degree (N node)
name|int
name|degree
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
comment|/**    * Returns the count of {@code node}'s incoming edges (equal to {@code predecessors(node).size()})    * in a directed graph. In an undirected graph, returns the {@link #degree(Object)}.    *    *<p>If the count is greater than {@code Integer.MAX_VALUE}, returns {@code Integer.MAX_VALUE}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this graph    */
DECL|method|inDegree (N node)
name|int
name|inDegree
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
comment|/**    * Returns the count of {@code node}'s outgoing edges (equal to {@code successors(node).size()})    * in a directed graph. In an undirected graph, returns the {@link #degree(Object)}.    *    *<p>If the count is greater than {@code Integer.MAX_VALUE}, returns {@code Integer.MAX_VALUE}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this graph    */
DECL|method|outDegree (N node)
name|int
name|outDegree
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
comment|/**    * Returns true if there is an edge that directly connects {@code nodeU} to {@code nodeV}. This is    * equivalent to {@code nodes().contains(nodeU)&& successors(nodeU).contains(nodeV)}.    *    *<p>In an undirected graph, this is equal to {@code hasEdgeConnecting(nodeV, nodeU)}.    *    * @since 23.0    */
DECL|method|hasEdgeConnecting (N nodeU, N nodeV)
name|boolean
name|hasEdgeConnecting
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
function_decl|;
comment|/**    * Returns true if there is an edge that directly connects {@code endpoints} (in the order, if    * any, specified by {@code endpoints}). This is equivalent to {@code    * edges().contains(endpoints)}.    *    *<p>Unlike the other {@code EndpointPair}-accepting methods, this method does not throw if the    * endpoints are unordered; it simply returns false. This is for consistency with the behavior of    * {@link Collection#contains(Object)} (which does not generally throw if the object cannot be    * present in the collection), and the desire to have this method's behavior be compatible with    * {@code edges().contains(endpoints)}.    *    * @since 27.1    */
DECL|method|hasEdgeConnecting (EndpointPair<N> endpoints)
name|boolean
name|hasEdgeConnecting
parameter_list|(
name|EndpointPair
argument_list|<
name|N
argument_list|>
name|endpoints
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

