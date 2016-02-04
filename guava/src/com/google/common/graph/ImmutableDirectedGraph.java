begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2014 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|graph
operator|.
name|GraphErrorMessageUtils
operator|.
name|EDGE_NOT_IN_GRAPH
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|graph
operator|.
name|GraphErrorMessageUtils
operator|.
name|NODE_NOT_IN_GRAPH
import|;
end_import

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
name|Beta
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableMap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Sets
import|;
end_import

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
comment|/**  * Implementation of an immutable directed graph consisting of nodes  * of type N and edges of type E.  *  *<p>This class maintains the following data structures:  *<ul>  *<li>For each node: sets of incoming and outgoing edges.  *<li>For each edge: references to the source and target nodes.  *</ul>  *  *<p>Some invariants/assumptions are maintained in this implementation:  *<ul>  *<li>An edge has exactly two end-points (source node and target node), which  *     may or may not be distinct.  *<li>By default, this is not a multigraph, that is, parallel edges (multiple  *     edges directed from node1 to node2) are not allowed.  If you want a multigraph,  *     build the graph with the 'multigraph' option:  *<pre>ImmutableDirectedGraph.builder(Graphs.config().multigraph()).build();</pre>  *<li>Anti-parallel edges (same incident nodes but in opposite direction,  *     e.g. (node1, node2) and (node2, node1)) are always allowed.  *<li>Edges are not adjacent to themselves by definition. In the case of a  *     self-loop, a node can be adjacent to itself, but an edge will never be.  *</ul>  *  *<p>The time complexity of all {@code Set}-returning accessors is O(1), since we  * are returning views. An exception to this is {@code edgesConnecting(node1, node2)},  * which is O(min(outD_node1, inD_node2)).  *  *<p>All other accessors have a time complexity of O(1), except for {@code degree(node)},  * whose time complexity is O(outD_node).  *  * @author Joshua O'Madadhain  * @param<N> Node parameter type  * @param<E> Edge parameter type  * @see Graphs  * @since 20.0  */
end_comment

begin_comment
comment|// TODO(user): Add support for sorted nodes/edges and/or hypergraphs.
end_comment

begin_class
annotation|@
name|Beta
DECL|class|ImmutableDirectedGraph
specifier|public
specifier|final
class|class
name|ImmutableDirectedGraph
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
extends|extends
name|AbstractImmutableGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
implements|implements
name|DirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
block|{
comment|// All nodes in the graph exist in this map
DECL|field|nodeConnections
specifier|private
specifier|final
name|ImmutableMap
argument_list|<
name|N
argument_list|,
name|NodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|>
name|nodeConnections
decl_stmt|;
comment|// All edges in the graph exist in this map
DECL|field|edgeToIncidentNodes
specifier|private
specifier|final
name|ImmutableMap
argument_list|<
name|E
argument_list|,
name|DirectedIncidentNodes
argument_list|<
name|N
argument_list|>
argument_list|>
name|edgeToIncidentNodes
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|GraphConfig
name|config
decl_stmt|;
DECL|method|ImmutableDirectedGraph (Builder<N, E> builder)
specifier|private
name|ImmutableDirectedGraph
parameter_list|(
name|Builder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|builder
parameter_list|)
block|{
name|DirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|directedGraph
init|=
name|builder
operator|.
name|directedGraph
decl_stmt|;
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|N
argument_list|,
name|NodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|>
name|nodeConnectionsBuilder
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|N
name|node
range|:
name|directedGraph
operator|.
name|nodes
argument_list|()
control|)
block|{
name|NodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|connections
init|=
name|DirectedNodeConnections
operator|.
name|ofImmutable
argument_list|(
name|directedGraph
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
argument_list|,
name|directedGraph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
argument_list|,
name|directedGraph
operator|.
name|inEdges
argument_list|(
name|node
argument_list|)
argument_list|,
name|directedGraph
operator|.
name|outEdges
argument_list|(
name|node
argument_list|)
argument_list|)
decl_stmt|;
name|nodeConnectionsBuilder
operator|.
name|put
argument_list|(
name|node
argument_list|,
name|connections
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|nodeConnections
operator|=
name|nodeConnectionsBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|E
argument_list|,
name|DirectedIncidentNodes
argument_list|<
name|N
argument_list|>
argument_list|>
name|edgeToIncidentNodesBuilder
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|E
name|edge
range|:
name|directedGraph
operator|.
name|edges
argument_list|()
control|)
block|{
name|DirectedIncidentNodes
argument_list|<
name|N
argument_list|>
name|incidentNodes
init|=
name|DirectedIncidentNodes
operator|.
name|of
argument_list|(
name|directedGraph
operator|.
name|source
argument_list|(
name|edge
argument_list|)
argument_list|,
name|directedGraph
operator|.
name|target
argument_list|(
name|edge
argument_list|)
argument_list|)
decl_stmt|;
name|edgeToIncidentNodesBuilder
operator|.
name|put
argument_list|(
name|edge
argument_list|,
name|incidentNodes
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|edgeToIncidentNodes
operator|=
name|edgeToIncidentNodesBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|directedGraph
operator|.
name|config
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|nodes ()
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|nodes
parameter_list|()
block|{
return|return
name|nodeConnections
operator|.
name|keySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|edges ()
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|edges
parameter_list|()
block|{
return|return
name|edgeToIncidentNodes
operator|.
name|keySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|config ()
specifier|public
name|GraphConfig
name|config
parameter_list|()
block|{
return|return
name|config
return|;
block|}
annotation|@
name|Override
DECL|method|incidentEdges (Object node)
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|incidentEdges
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
return|return
name|Sets
operator|.
name|union
argument_list|(
name|inEdges
argument_list|(
name|node
argument_list|)
argument_list|,
name|outEdges
argument_list|(
name|node
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|incidentNodes (Object edge)
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|incidentNodes
parameter_list|(
name|Object
name|edge
parameter_list|)
block|{
return|return
name|checkedEndpoints
argument_list|(
name|edge
argument_list|)
operator|.
name|asImmutableSet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|adjacentNodes (Object node)
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|adjacentNodes
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
return|return
name|Sets
operator|.
name|union
argument_list|(
name|predecessors
argument_list|(
name|node
argument_list|)
argument_list|,
name|successors
argument_list|(
name|node
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|adjacentEdges (Object edge)
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|adjacentEdges
parameter_list|(
name|Object
name|edge
parameter_list|)
block|{
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|E
argument_list|>
name|adjacentEdges
init|=
name|ImmutableSet
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|N
name|endpoint
range|:
name|incidentNodes
argument_list|(
name|edge
argument_list|)
control|)
block|{
for|for
control|(
name|E
name|adjacentEdge
range|:
name|incidentEdges
argument_list|(
name|endpoint
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|edge
operator|.
name|equals
argument_list|(
name|adjacentEdge
argument_list|)
condition|)
block|{
comment|// Edges are not adjacent to themselves by definition.
name|adjacentEdges
operator|.
name|add
argument_list|(
name|adjacentEdge
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|adjacentEdges
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**    * Returns the intersection of these two sets, using {@link Sets#intersection}:    *<ol>    *<li>Outgoing edges of {@code node1}.    *<li>Incoming edges of {@code node2}.    *</ol>    */
annotation|@
name|Override
DECL|method|edgesConnecting (Object node1, Object node2)
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|edgesConnecting
parameter_list|(
name|Object
name|node1
parameter_list|,
name|Object
name|node2
parameter_list|)
block|{
name|Set
argument_list|<
name|E
argument_list|>
name|sourceOutEdges
init|=
name|outEdges
argument_list|(
name|node1
argument_list|)
decl_stmt|;
comment|// Verifies that node1 is in graph
if|if
condition|(
operator|!
name|config
operator|.
name|isSelfLoopsAllowed
argument_list|()
operator|&&
name|node1
operator|.
name|equals
argument_list|(
name|node2
argument_list|)
condition|)
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|()
return|;
block|}
name|Set
argument_list|<
name|E
argument_list|>
name|targetInEdges
init|=
name|inEdges
argument_list|(
name|node2
argument_list|)
decl_stmt|;
return|return
operator|(
name|sourceOutEdges
operator|.
name|size
argument_list|()
operator|<=
name|targetInEdges
operator|.
name|size
argument_list|()
operator|)
condition|?
name|Sets
operator|.
name|intersection
argument_list|(
name|sourceOutEdges
argument_list|,
name|targetInEdges
argument_list|)
operator|.
name|immutableCopy
argument_list|()
else|:
name|Sets
operator|.
name|intersection
argument_list|(
name|targetInEdges
argument_list|,
name|sourceOutEdges
argument_list|)
operator|.
name|immutableCopy
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|inEdges (Object node)
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|inEdges
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
return|return
name|checkedConnections
argument_list|(
name|node
argument_list|)
operator|.
name|inEdges
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|outEdges (Object node)
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|outEdges
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
return|return
name|checkedConnections
argument_list|(
name|node
argument_list|)
operator|.
name|outEdges
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|predecessors (Object node)
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|predecessors
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
return|return
name|checkedConnections
argument_list|(
name|node
argument_list|)
operator|.
name|predecessors
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|successors (Object node)
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|successors
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
return|return
name|checkedConnections
argument_list|(
name|node
argument_list|)
operator|.
name|successors
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|degree (Object node)
specifier|public
name|long
name|degree
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
return|return
name|incidentEdges
argument_list|(
name|node
argument_list|)
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|inDegree (Object node)
specifier|public
name|long
name|inDegree
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
return|return
name|inEdges
argument_list|(
name|node
argument_list|)
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|outDegree (Object node)
specifier|public
name|long
name|outDegree
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
return|return
name|outEdges
argument_list|(
name|node
argument_list|)
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|source (Object edge)
specifier|public
name|N
name|source
parameter_list|(
name|Object
name|edge
parameter_list|)
block|{
return|return
name|checkedEndpoints
argument_list|(
name|edge
argument_list|)
operator|.
name|source
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|target (Object edge)
specifier|public
name|N
name|target
parameter_list|(
name|Object
name|edge
parameter_list|)
block|{
return|return
name|checkedEndpoints
argument_list|(
name|edge
argument_list|)
operator|.
name|target
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object object)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
operator|(
name|object
operator|instanceof
name|DirectedGraph
operator|)
operator|&&
name|Graphs
operator|.
name|equal
argument_list|(
name|this
argument_list|,
operator|(
name|DirectedGraph
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
comment|// The node set is included in the hash to differentiate between graphs with isolated nodes.
return|return
name|Objects
operator|.
name|hashCode
argument_list|(
name|nodes
argument_list|()
argument_list|,
name|edgeToIncidentNodes
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"config: %s, nodes: %s, edges: %s"
argument_list|,
name|config
argument_list|,
name|nodes
argument_list|()
argument_list|,
name|edgeToIncidentNodes
argument_list|)
return|;
block|}
DECL|method|checkedConnections (Object node)
specifier|private
name|NodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|checkedConnections
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|node
argument_list|,
literal|"node"
argument_list|)
expr_stmt|;
name|NodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|connections
init|=
name|nodeConnections
operator|.
name|get
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|connections
operator|!=
literal|null
argument_list|,
name|NODE_NOT_IN_GRAPH
argument_list|,
name|node
argument_list|)
expr_stmt|;
return|return
name|connections
return|;
block|}
DECL|method|checkedEndpoints (Object edge)
specifier|private
name|DirectedIncidentNodes
argument_list|<
name|N
argument_list|>
name|checkedEndpoints
parameter_list|(
name|Object
name|edge
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|edge
argument_list|,
literal|"edge"
argument_list|)
expr_stmt|;
name|DirectedIncidentNodes
argument_list|<
name|N
argument_list|>
name|endpoints
init|=
name|edgeToIncidentNodes
operator|.
name|get
argument_list|(
name|edge
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|endpoints
operator|!=
literal|null
argument_list|,
name|EDGE_NOT_IN_GRAPH
argument_list|,
name|edge
argument_list|)
expr_stmt|;
return|return
name|endpoints
return|;
block|}
comment|/**    * Returns a new builder. The generated builder is equivalent to the builder    * created by the {@code Builder} constructor.    */
DECL|method|builder ()
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|Builder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|builder
parameter_list|()
block|{
return|return
operator|new
name|Builder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Returns a new builder. The generated builder is equivalent to the builder    * created by the {@code Builder} constructor.    *    * @param config an instance of {@code GraphConfig} with the intended    *        graph configuration.    */
DECL|method|builder (GraphConfig config)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|Builder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|builder
parameter_list|(
name|GraphConfig
name|config
parameter_list|)
block|{
return|return
operator|new
name|Builder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|(
name|config
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable copy of the input graph.    */
DECL|method|copyOf (DirectedGraph<N, E> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|ImmutableDirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copyOf
parameter_list|(
name|DirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|graph
parameter_list|)
block|{
return|return
operator|new
name|Builder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|(
name|graph
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**    * A builder for creating immutable directed graph instances.    *    * @param<N> Node parameter type    * @param<E> edge parameter type    * @see GraphConfig    */
DECL|class|Builder
specifier|public
specifier|static
specifier|final
class|class
name|Builder
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
implements|implements
name|AbstractImmutableGraph
operator|.
name|Builder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
block|{
DECL|field|directedGraph
specifier|private
specifier|final
name|DirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|directedGraph
decl_stmt|;
comment|/**      * Creates a new builder with the default graph configuration.      */
DECL|method|Builder ()
specifier|public
name|Builder
parameter_list|()
block|{
name|this
argument_list|(
name|Graphs
operator|.
expr|<
name|N
argument_list|,
name|E
operator|>
name|createDirected
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new builder with the specified graph configuration.      */
DECL|method|Builder (GraphConfig config)
specifier|public
name|Builder
parameter_list|(
name|GraphConfig
name|config
parameter_list|)
block|{
name|this
argument_list|(
name|Graphs
operator|.
expr|<
name|N
argument_list|,
name|E
operator|>
name|createDirected
argument_list|(
name|config
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new builder whose internal state is that of {@code graph}.      *      *<p>NOTE: This constructor should only be used in the case where it will be immediately      * followed by a call to {@code build}, to ensure that the input graph will not be modified.      * Currently the only such context is {@code Immutable*Graph.copyOf()}, which use these      * constructors to avoid making an extra copy of the graph state.      * @see ImmutableDirectedGraph#copyOf(DirectedGraph)      */
DECL|method|Builder (DirectedGraph<N, E> graph)
specifier|private
name|Builder
parameter_list|(
name|DirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|graph
parameter_list|)
block|{
name|this
operator|.
name|directedGraph
operator|=
name|checkNotNull
argument_list|(
name|graph
argument_list|,
literal|"graph"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
DECL|method|addNode (N node)
specifier|public
name|Builder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|addNode
parameter_list|(
name|N
name|node
parameter_list|)
block|{
name|directedGraph
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
DECL|method|addEdge (E edge, N node1, N node2)
specifier|public
name|Builder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|addEdge
parameter_list|(
name|E
name|edge
parameter_list|,
name|N
name|node1
parameter_list|,
name|N
name|node2
parameter_list|)
block|{
name|directedGraph
operator|.
name|addEdge
argument_list|(
name|edge
argument_list|,
name|node1
argument_list|,
name|node2
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds all elements of {@code graph} to the graph being built.      *      * @throws IllegalArgumentException under either of two conditions:      *     (1) the {@code GraphConfig} objects held by the graph being built and by {@code graph}      *     are not compatible      *     (2) calling {@code Graph.addEdge(e, n1, n2)} on the graph being built throws IAE      * @see Graph#addEdge(e, n1, n2)      */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addGraph (DirectedGraph<N, E> graph)
specifier|public
name|Builder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|addGraph
parameter_list|(
name|DirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|graph
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|directedGraph
operator|.
name|config
argument_list|()
operator|.
name|compatibleWith
argument_list|(
name|graph
operator|.
name|config
argument_list|()
argument_list|)
argument_list|,
literal|"GraphConfigs for input and for graph being built are not compatible: input: %s, "
operator|+
literal|"this graph: %s"
argument_list|,
name|graph
operator|.
name|config
argument_list|()
argument_list|,
name|directedGraph
operator|.
name|config
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|N
name|node
range|:
name|graph
operator|.
name|nodes
argument_list|()
control|)
block|{
name|directedGraph
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|E
name|edge
range|:
name|graph
operator|.
name|edges
argument_list|()
control|)
block|{
name|directedGraph
operator|.
name|addEdge
argument_list|(
name|edge
argument_list|,
name|graph
operator|.
name|source
argument_list|(
name|edge
argument_list|)
argument_list|,
name|graph
operator|.
name|target
argument_list|(
name|edge
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|build ()
specifier|public
name|ImmutableDirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|build
parameter_list|()
block|{
return|return
operator|new
name|ImmutableDirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

