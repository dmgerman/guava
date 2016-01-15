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
name|ADDING_PARALLEL_EDGE
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
name|REUSING_EDGE
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
name|SELF_LOOPS_NOT_ALLOWED
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
name|Iterables
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
name|Maps
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
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
comment|/**  * Adjacency-set-based implementation of a directed graph consisting of nodes  * of type N and edges of type E.  *  *<p>{@link Graphs#createDirected()} should be used to get an instance of this class.  *  *<p>This class maintains the following for representing the directed graph data  *    structure:  *<ul>  *<li>For each node: sets of incoming and outgoing edges.  *<li>For each edge: references to the source and target nodes.  *</ul>  *  *<p>Some invariants/assumptions are maintained in this implementation:  *<ul>  *<li>An edge has exactly two end-points (source node and target node), which  *     may or may not be distinct.  *<li>By default, this is not a multigraph, that is, parallel edges (multiple  *     edges directed from n1 to n2) are not allowed.  If you want a multigraph,  *     create the graph with the 'multigraph' option:  *<pre>Graphs.createDirected(Graphs.config().multigraph());</pre>  *<li>Anti-parallel edges (same incident nodes but in opposite direction,  *     e.g. (n1, n2) and (n2, n1)) are always allowed.  *<li>By default, self-loop edges are allowed. If you want to disallow them,  *     create the graph without the option of self-loops:  *<pre>Graphs.createDirected(Graphs.config().noSelfLoops());</pre>  *<li>Edges are not adjacent to themselves by definition. In the case of a  *     self-loop, a node can be adjacent to itself, but an edge will never be.  *</ul>  *  *<p>{@code Set}-returning accessors return unmodifiable views: the view returned  * will reflect changes to the graph, but may not be modified by the user.  * The behavior of the returned view is undefined in the following cases:  *<ul>  *<li>Removing the element on which the accessor is called (e.g.:  *<pre>{@code  *     Set<N> preds = predecessors(n);  *     graph.removeNode(n);}</pre>  *     At this point, the contents of {@code preds} are undefined.  *</ul>  *  *<p>The time complexity of all {@code Set}-returning accessors is O(1), since we  * are returning views. It should be noted that for the following methods:  *<ul>  *<li>{@code incidentEdges}.  *<li>Methods that ask for adjacent nodes (e.g. {@code predecessors}).  *<li>{@code adjacentEdges}.  *<li>{@code edgesConnecting}.  *</ul>  * the view is calculated lazily and the backing set is<b>not</b> cached, so every time the user  * accesses the returned view, the backing set will be reconstructed again. If the user wants  * to avoid this, they should either use {@code ImmutableDirectedGraph}  * (if their input is not changing) or make a copy of the return value.  *  *<p>All other accessors have a time complexity of O(1), except for  * {@code degree(n)}, whose time complexity is linear in the minimum of  * the out-degree and in-degree of {@code n}, in case of allowing self-loop edges.  * This is due to a call to {@code edgesConnecting}.  *  *<p>Time complexities for mutation methods:  *<ul>  *<li>{@code addNode}: O(1).  *<li>{@code removeEdge}: O(1).  *<li>{@code addEdge(E e, N n1, N n2)}: O(1), unless this graph is not a multigraph  * (does not support parallel edges). In such case, this method may call  * {@code edgesConnecting(n1, n2)}.  *<li>{@code removeNode(n)}: O(d), where d is the degree of the node {@code n}.  *</ul>  *  * @author Joshua O'Madadhain  * @param<N> Node parameter type  * @param<E> Edge parameter type  * @see IncidenceSetUndirectedGraph  * @see Graphs  */
end_comment

begin_class
DECL|class|IncidenceSetDirectedGraph
specifier|final
class|class
name|IncidenceSetDirectedGraph
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
implements|implements
name|DirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
block|{
comment|// TODO(b/24620028): Enable this class to support sorted nodes/edges.
DECL|field|nodeToIncidentEdges
specifier|private
specifier|final
name|Map
argument_list|<
name|N
argument_list|,
name|DirectedIncidentEdges
argument_list|<
name|E
argument_list|>
argument_list|>
name|nodeToIncidentEdges
decl_stmt|;
DECL|field|edgeToIncidentNodes
specifier|private
specifier|final
name|Map
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
DECL|method|IncidenceSetDirectedGraph (GraphConfig config)
name|IncidenceSetDirectedGraph
parameter_list|(
name|GraphConfig
name|config
parameter_list|)
block|{
comment|// The default of 11 is rather arbitrary, but roughly matches the sizing of just new HashMap()
name|this
operator|.
name|nodeToIncidentEdges
operator|=
name|Maps
operator|.
name|newLinkedHashMapWithExpectedSize
argument_list|(
name|config
operator|.
name|getExpectedNodeCount
argument_list|()
operator|.
name|or
argument_list|(
literal|11
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|edgeToIncidentNodes
operator|=
name|Maps
operator|.
name|newLinkedHashMapWithExpectedSize
argument_list|(
name|config
operator|.
name|getExpectedEdgeCount
argument_list|()
operator|.
name|or
argument_list|(
literal|11
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
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
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|nodeToIncidentEdges
operator|.
name|keySet
argument_list|()
argument_list|)
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
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|edgeToIncidentNodes
operator|.
name|keySet
argument_list|()
argument_list|)
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
name|checkNotNull
argument_list|(
name|node
argument_list|,
literal|"node"
argument_list|)
expr_stmt|;
name|DirectedIncidentEdges
argument_list|<
name|E
argument_list|>
name|incidentEdges
init|=
name|nodeToIncidentEdges
operator|.
name|get
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|incidentEdges
operator|!=
literal|null
argument_list|,
name|NODE_NOT_IN_GRAPH
argument_list|,
name|node
argument_list|)
expr_stmt|;
return|return
name|Sets
operator|.
name|union
argument_list|(
name|incidentEdges
operator|.
name|inEdges
argument_list|()
argument_list|,
name|incidentEdges
operator|.
name|outEdges
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|incidentNodes (final Object edge)
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|incidentNodes
parameter_list|(
specifier|final
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
comment|// Returning an immutable set here as the edge's endpoints will not change anyway.
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
name|Sets
operator|.
name|difference
argument_list|(
name|Sets
operator|.
name|union
argument_list|(
name|incidentEdges
argument_list|(
name|endpoints
operator|.
name|target
argument_list|()
argument_list|)
argument_list|,
name|incidentEdges
argument_list|(
name|endpoints
operator|.
name|source
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
name|edge
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns the intersection of these two sets, using {@code Sets.intersection}:    *<ol>    *<li>Outgoing edges of {@code node1}.    *<li>Incoming edges of {@code node2}.    *</ol>    * The first argument passed to {@code Sets.intersection} is the smaller of    * the two sets.    *    * @see Sets#intersection    */
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
name|checkNotNull
argument_list|(
name|node1
argument_list|,
literal|"node1"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|node2
argument_list|,
literal|"node2"
argument_list|)
expr_stmt|;
name|DirectedIncidentEdges
argument_list|<
name|E
argument_list|>
name|incidentEdgesN1
init|=
name|nodeToIncidentEdges
operator|.
name|get
argument_list|(
name|node1
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|incidentEdgesN1
operator|!=
literal|null
argument_list|,
name|NODE_NOT_IN_GRAPH
argument_list|,
name|node1
argument_list|)
expr_stmt|;
name|DirectedIncidentEdges
argument_list|<
name|E
argument_list|>
name|incidentEdgesN2
init|=
name|nodeToIncidentEdges
operator|.
name|get
argument_list|(
name|node2
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|incidentEdgesN2
operator|!=
literal|null
argument_list|,
name|NODE_NOT_IN_GRAPH
argument_list|,
name|node2
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|E
argument_list|>
name|outEdges
init|=
name|incidentEdgesN1
operator|.
name|outEdges
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|E
argument_list|>
name|inEdges
init|=
name|incidentEdgesN2
operator|.
name|inEdges
argument_list|()
decl_stmt|;
return|return
name|outEdges
operator|.
name|size
argument_list|()
operator|<=
name|inEdges
operator|.
name|size
argument_list|()
condition|?
name|Sets
operator|.
name|intersection
argument_list|(
name|outEdges
argument_list|,
name|inEdges
argument_list|)
else|:
name|Sets
operator|.
name|intersection
argument_list|(
name|inEdges
argument_list|,
name|outEdges
argument_list|)
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
name|checkNotNull
argument_list|(
name|node
argument_list|,
literal|"node"
argument_list|)
expr_stmt|;
name|DirectedIncidentEdges
argument_list|<
name|E
argument_list|>
name|incidentEdges
init|=
name|nodeToIncidentEdges
operator|.
name|get
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|incidentEdges
operator|!=
literal|null
argument_list|,
name|NODE_NOT_IN_GRAPH
argument_list|,
name|node
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|incidentEdges
operator|.
name|inEdges
argument_list|()
argument_list|)
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
name|checkNotNull
argument_list|(
name|node
argument_list|,
literal|"node"
argument_list|)
expr_stmt|;
name|DirectedIncidentEdges
argument_list|<
name|E
argument_list|>
name|incidentEdges
init|=
name|nodeToIncidentEdges
operator|.
name|get
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|incidentEdges
operator|!=
literal|null
argument_list|,
name|NODE_NOT_IN_GRAPH
argument_list|,
name|node
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|incidentEdges
operator|.
name|outEdges
argument_list|()
argument_list|)
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
name|checkNotNull
argument_list|(
name|node
argument_list|,
literal|"node"
argument_list|)
expr_stmt|;
name|DirectedIncidentEdges
argument_list|<
name|E
argument_list|>
name|incidentEdges
init|=
name|nodeToIncidentEdges
operator|.
name|get
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|incidentEdges
operator|!=
literal|null
argument_list|,
name|NODE_NOT_IN_GRAPH
argument_list|,
name|node
argument_list|)
expr_stmt|;
specifier|final
name|Set
argument_list|<
name|E
argument_list|>
name|inEdges
init|=
name|incidentEdges
operator|.
name|inEdges
argument_list|()
decl_stmt|;
return|return
operator|new
name|SetView
argument_list|<
name|N
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|inEdges
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
name|Set
argument_list|<
name|N
argument_list|>
name|elements
parameter_list|()
block|{
name|Set
argument_list|<
name|N
argument_list|>
name|nodes
init|=
name|Sets
operator|.
name|newLinkedHashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|E
name|edge
range|:
name|inEdges
control|)
block|{
name|nodes
operator|.
name|add
argument_list|(
name|source
argument_list|(
name|edge
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|nodes
return|;
block|}
block|}
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
name|checkNotNull
argument_list|(
name|node
argument_list|,
literal|"node"
argument_list|)
expr_stmt|;
name|DirectedIncidentEdges
argument_list|<
name|E
argument_list|>
name|incidentEdges
init|=
name|nodeToIncidentEdges
operator|.
name|get
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|incidentEdges
operator|!=
literal|null
argument_list|,
name|NODE_NOT_IN_GRAPH
argument_list|,
name|node
argument_list|)
expr_stmt|;
specifier|final
name|Set
argument_list|<
name|E
argument_list|>
name|outEdges
init|=
name|incidentEdges
operator|.
name|outEdges
argument_list|()
decl_stmt|;
return|return
operator|new
name|SetView
argument_list|<
name|N
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|outEdges
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
name|Set
argument_list|<
name|N
argument_list|>
name|elements
parameter_list|()
block|{
name|Set
argument_list|<
name|N
argument_list|>
name|nodes
init|=
name|Sets
operator|.
name|newLinkedHashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|E
name|edge
range|:
name|outEdges
control|)
block|{
name|nodes
operator|.
name|add
argument_list|(
name|target
argument_list|(
name|edge
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|nodes
return|;
block|}
block|}
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
name|checkNotNull
argument_list|(
name|node
argument_list|,
literal|"node"
argument_list|)
expr_stmt|;
return|return
name|config
operator|.
name|isSelfLoopsAllowed
argument_list|()
condition|?
name|inDegree
argument_list|(
name|node
argument_list|)
operator|+
name|outDegree
argument_list|(
name|node
argument_list|)
operator|-
name|edgesConnecting
argument_list|(
name|node
argument_list|,
name|node
argument_list|)
operator|.
name|size
argument_list|()
else|:
name|inDegree
argument_list|(
name|node
argument_list|)
operator|+
name|outDegree
argument_list|(
name|node
argument_list|)
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
name|checkNotNull
argument_list|(
name|node
argument_list|,
literal|"node"
argument_list|)
expr_stmt|;
name|DirectedIncidentEdges
argument_list|<
name|E
argument_list|>
name|incidentEdges
init|=
name|nodeToIncidentEdges
operator|.
name|get
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|incidentEdges
operator|!=
literal|null
argument_list|,
name|NODE_NOT_IN_GRAPH
argument_list|,
name|node
argument_list|)
expr_stmt|;
return|return
name|incidentEdges
operator|.
name|inEdges
argument_list|()
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
name|checkNotNull
argument_list|(
name|node
argument_list|,
literal|"node"
argument_list|)
expr_stmt|;
name|DirectedIncidentEdges
argument_list|<
name|E
argument_list|>
name|incidentEdges
init|=
name|nodeToIncidentEdges
operator|.
name|get
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|incidentEdges
operator|!=
literal|null
argument_list|,
name|NODE_NOT_IN_GRAPH
argument_list|,
name|node
argument_list|)
expr_stmt|;
return|return
name|incidentEdges
operator|.
name|outEdges
argument_list|()
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
operator|.
name|target
argument_list|()
return|;
block|}
comment|// Element Mutation
annotation|@
name|Override
DECL|method|addNode (N node)
specifier|public
name|boolean
name|addNode
parameter_list|(
name|N
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
if|if
condition|(
name|containsNode
argument_list|(
name|node
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// TODO(user): Enable users to specify the expected number of neighbors
comment|// of a new node.
name|nodeToIncidentEdges
operator|.
name|put
argument_list|(
name|node
argument_list|,
name|DirectedIncidentEdges
operator|.
expr|<
name|E
operator|>
name|of
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/**    * Add nodes that are not elements of the graph, then add {@code edge} between them.    * Return {@code false} if {@code edge} already exists between {@code node1} and {@code node2},    * and in the same direction.    *    *<p>If this graph is not a multigraph (does not support parallel edges), this method may call    * {@code edgesConnecting(node1, node2)} to discover whether node1 and node2 are already    * connected.    *    * @throws IllegalArgumentException if an edge (other than {@code edge}) already    *         exists from {@code node1} to {@code node2}, and this is not a multigraph.    *         Also, if self-loops are not allowed, and {@code node1} is equal to {@code node2}.    */
annotation|@
name|Override
DECL|method|addEdge (E edge, N node1, N node2)
specifier|public
name|boolean
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
name|checkNotNull
argument_list|(
name|edge
argument_list|,
literal|"edge"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|node1
argument_list|,
literal|"node1"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|node2
argument_list|,
literal|"node2"
argument_list|)
expr_stmt|;
name|DirectedIncidentNodes
argument_list|<
name|N
argument_list|>
name|endpoints
init|=
name|DirectedIncidentNodes
operator|.
name|of
argument_list|(
name|node1
argument_list|,
name|node2
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|config
operator|.
name|isSelfLoopsAllowed
argument_list|()
operator|||
operator|!
name|endpoints
operator|.
name|isSelfLoop
argument_list|()
argument_list|,
name|SELF_LOOPS_NOT_ALLOWED
argument_list|,
name|node1
argument_list|)
expr_stmt|;
name|DirectedIncidentNodes
argument_list|<
name|N
argument_list|>
name|previousEndpoints
init|=
name|edgeToIncidentNodes
operator|.
name|get
argument_list|(
name|edge
argument_list|)
decl_stmt|;
if|if
condition|(
name|previousEndpoints
operator|!=
literal|null
condition|)
block|{
name|checkArgument
argument_list|(
name|previousEndpoints
operator|.
name|equals
argument_list|(
name|endpoints
argument_list|)
argument_list|,
name|REUSING_EDGE
argument_list|,
name|edge
argument_list|,
name|previousEndpoints
argument_list|,
name|endpoints
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|config
operator|.
name|isMultigraph
argument_list|()
operator|&&
name|containsNode
argument_list|(
name|node1
argument_list|)
operator|&&
name|containsNode
argument_list|(
name|node2
argument_list|)
condition|)
block|{
name|E
name|edgeConnecting
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|edgesConnecting
argument_list|(
name|node1
argument_list|,
name|node2
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|edgeConnecting
operator|==
literal|null
argument_list|,
name|ADDING_PARALLEL_EDGE
argument_list|,
name|node1
argument_list|,
name|node2
argument_list|,
name|edgeConnecting
argument_list|)
expr_stmt|;
block|}
name|addNode
argument_list|(
name|node1
argument_list|)
expr_stmt|;
name|nodeToIncidentEdges
operator|.
name|get
argument_list|(
name|node1
argument_list|)
operator|.
name|outEdges
argument_list|()
operator|.
name|add
argument_list|(
name|edge
argument_list|)
expr_stmt|;
name|addNode
argument_list|(
name|node2
argument_list|)
expr_stmt|;
name|nodeToIncidentEdges
operator|.
name|get
argument_list|(
name|node2
argument_list|)
operator|.
name|inEdges
argument_list|()
operator|.
name|add
argument_list|(
name|edge
argument_list|)
expr_stmt|;
name|edgeToIncidentNodes
operator|.
name|put
argument_list|(
name|edge
argument_list|,
name|endpoints
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|removeNode (Object node)
specifier|public
name|boolean
name|removeNode
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
comment|// Return false if the node doesn't exist in the graph
if|if
condition|(
operator|!
name|containsNode
argument_list|(
name|node
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Since views are returned, we need to copy the set of incident edges
comment|// to an equivalent collection to avoid removing the edges we are looping on.
for|for
control|(
name|Object
name|edge
range|:
name|incidentEdges
argument_list|(
name|node
argument_list|)
operator|.
name|toArray
argument_list|()
control|)
block|{
name|removeEdge
argument_list|(
name|edge
argument_list|)
expr_stmt|;
block|}
name|nodeToIncidentEdges
operator|.
name|remove
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|removeEdge (Object edge)
specifier|public
name|boolean
name|removeEdge
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
comment|// Return false if the edge doesn't exist in the graph
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
if|if
condition|(
name|endpoints
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|nodeToIncidentEdges
operator|.
name|get
argument_list|(
name|endpoints
operator|.
name|source
argument_list|()
argument_list|)
operator|.
name|outEdges
argument_list|()
operator|.
name|remove
argument_list|(
name|edge
argument_list|)
expr_stmt|;
name|nodeToIncidentEdges
operator|.
name|get
argument_list|(
name|endpoints
operator|.
name|target
argument_list|()
argument_list|)
operator|.
name|inEdges
argument_list|()
operator|.
name|remove
argument_list|(
name|edge
argument_list|)
expr_stmt|;
name|edgeToIncidentNodes
operator|.
name|remove
argument_list|(
name|edge
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object other)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|other
parameter_list|)
block|{
return|return
operator|(
name|other
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
operator|)
name|other
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
comment|// This map encapsulates all of the structural relationships of this graph, so its hash code
comment|// is consistent with the above definition of equals().
return|return
name|nodeToIncidentEdges
operator|.
name|hashCode
argument_list|()
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
name|nodeToIncidentEdges
operator|.
name|keySet
argument_list|()
argument_list|,
name|edgeToIncidentNodes
argument_list|)
return|;
block|}
DECL|method|containsNode (Object node)
specifier|private
name|boolean
name|containsNode
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
return|return
name|nodeToIncidentEdges
operator|.
name|containsKey
argument_list|(
name|node
argument_list|)
return|;
block|}
block|}
end_class

end_unit

