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
name|NOT_AVAILABLE_ON_UNDIRECTED
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
name|Iterator
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

begin_comment
comment|/**  * Abstract configurable implementation of {@link Network} that supports the options supplied  * by {@link NetworkBuilder}.  *  *<p>This class maintains a map of {@link NodeConnections} for every node  * and {@link IncidentNodes} for every edge.  *  *<p>{@code Set}-returning accessors return unmodifiable views: the view returned will reflect  * changes to the graph (if the graph is mutable) but may not be modified by the user.  * The behavior of the returned view is undefined in the following cases:  *<ul>  *<li>Removing the element on which the accessor is called (e.g.:  *<pre>{@code  *     Set<N> adjacentNodes = adjacentNodes(node);  *     graph.removeNode(node);}</pre>  *     At this point, the contents of {@code adjacentNodes} are undefined.  *</ul>  *  *<p>The time complexity of all {@code Set}-returning accessors is O(1), since views are returned.  *  * @author James Sexton  * @author Joshua O'Madadhain  * @author Omar Darwish  * @param<N> Node parameter type  * @param<E> Edge parameter type  */
end_comment

begin_comment
comment|// TODO(b/24620028): Enable this class to support sorted nodes/edges.
end_comment

begin_class
DECL|class|AbstractConfigurableNetwork
class|class
name|AbstractConfigurableNetwork
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
extends|extends
name|AbstractNetwork
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
block|{
comment|// The default of 11 is rather arbitrary, but roughly matches the sizing of just new HashMap()
DECL|field|DEFAULT_MAP_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_MAP_SIZE
init|=
literal|11
decl_stmt|;
DECL|field|isDirected
specifier|private
specifier|final
name|boolean
name|isDirected
decl_stmt|;
DECL|field|allowsParallelEdges
specifier|private
specifier|final
name|boolean
name|allowsParallelEdges
decl_stmt|;
DECL|field|allowsSelfLoops
specifier|private
specifier|final
name|boolean
name|allowsSelfLoops
decl_stmt|;
DECL|field|nodeConnections
specifier|protected
specifier|final
name|Map
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
DECL|field|edgeToIncidentNodes
specifier|protected
specifier|final
name|Map
argument_list|<
name|E
argument_list|,
name|IncidentNodes
argument_list|<
name|N
argument_list|>
argument_list|>
name|edgeToIncidentNodes
decl_stmt|;
comment|/**    * Constructs a mutable graph with the properties specified in {@code builder}.    */
DECL|method|AbstractConfigurableNetwork (NetworkBuilder<? super N, ? super E> builder)
name|AbstractConfigurableNetwork
parameter_list|(
name|NetworkBuilder
argument_list|<
name|?
super|super
name|N
argument_list|,
name|?
super|super
name|E
argument_list|>
name|builder
parameter_list|)
block|{
name|this
argument_list|(
name|builder
argument_list|,
name|Maps
operator|.
expr|<
name|N
argument_list|,
name|NodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
operator|>
name|newLinkedHashMapWithExpectedSize
argument_list|(
name|builder
operator|.
name|expectedNodeCount
operator|.
name|or
argument_list|(
name|DEFAULT_MAP_SIZE
argument_list|)
argument_list|)
argument_list|,
name|Maps
operator|.
expr|<
name|E
argument_list|,
name|IncidentNodes
argument_list|<
name|N
argument_list|>
operator|>
name|newLinkedHashMapWithExpectedSize
argument_list|(
name|builder
operator|.
name|expectedEdgeCount
operator|.
name|or
argument_list|(
name|DEFAULT_MAP_SIZE
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Constructs a graph with the properties specified in {@code builder}, initialized with    * the given node and edge maps. May be used for either mutable or immutable graphs.    */
DECL|method|AbstractConfigurableNetwork (NetworkBuilder<? super N, ? super E> builder, Map<N, NodeConnections<N, E>> nodeConnections, Map<E, IncidentNodes<N>> edgeToIncidentNodes)
name|AbstractConfigurableNetwork
parameter_list|(
name|NetworkBuilder
argument_list|<
name|?
super|super
name|N
argument_list|,
name|?
super|super
name|E
argument_list|>
name|builder
parameter_list|,
name|Map
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
parameter_list|,
name|Map
argument_list|<
name|E
argument_list|,
name|IncidentNodes
argument_list|<
name|N
argument_list|>
argument_list|>
name|edgeToIncidentNodes
parameter_list|)
block|{
name|this
operator|.
name|isDirected
operator|=
name|builder
operator|.
name|directed
expr_stmt|;
name|this
operator|.
name|allowsParallelEdges
operator|=
name|builder
operator|.
name|allowsParallelEdges
expr_stmt|;
name|this
operator|.
name|allowsSelfLoops
operator|=
name|builder
operator|.
name|allowsSelfLoops
expr_stmt|;
name|this
operator|.
name|nodeConnections
operator|=
name|checkNotNull
argument_list|(
name|nodeConnections
argument_list|)
expr_stmt|;
name|this
operator|.
name|edgeToIncidentNodes
operator|=
name|checkNotNull
argument_list|(
name|edgeToIncidentNodes
argument_list|)
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
name|nodeConnections
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
DECL|method|isDirected ()
specifier|public
name|boolean
name|isDirected
parameter_list|()
block|{
return|return
name|isDirected
return|;
block|}
annotation|@
name|Override
DECL|method|allowsParallelEdges ()
specifier|public
name|boolean
name|allowsParallelEdges
parameter_list|()
block|{
return|return
name|allowsParallelEdges
return|;
block|}
annotation|@
name|Override
DECL|method|allowsSelfLoops ()
specifier|public
name|boolean
name|allowsSelfLoops
parameter_list|()
block|{
return|return
name|allowsSelfLoops
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
name|checkedConnections
argument_list|(
name|node
argument_list|)
operator|.
name|incidentEdges
argument_list|()
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
name|checkedIncidentNodes
argument_list|(
name|edge
argument_list|)
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
name|checkedConnections
argument_list|(
name|node
argument_list|)
operator|.
name|adjacentNodes
argument_list|()
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
name|Iterator
argument_list|<
name|N
argument_list|>
name|incidentNodesIterator
init|=
name|incidentNodes
argument_list|(
name|edge
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|E
argument_list|>
name|endpointsIncidentEdges
init|=
name|incidentEdges
argument_list|(
name|incidentNodesIterator
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|incidentNodesIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|endpointsIncidentEdges
operator|=
name|Sets
operator|.
name|union
argument_list|(
name|incidentEdges
argument_list|(
name|incidentNodesIterator
operator|.
name|next
argument_list|()
argument_list|)
argument_list|,
name|endpointsIncidentEdges
argument_list|)
expr_stmt|;
block|}
return|return
name|Sets
operator|.
name|difference
argument_list|(
name|endpointsIncidentEdges
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
comment|/**    * If {@code node1} is equal to {@code node2}, the set of self-loop edges is returned.    * Otherwise, returns the intersection of these two sets, using {@link Sets#intersection}:    *<ol>    *<li>Outgoing edges of {@code node1}.    *<li>Incoming edges of {@code node2}.    *</ol>    */
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
name|outEdgesN1
init|=
name|outEdges
argument_list|(
name|node1
argument_list|)
decl_stmt|;
comment|// Verifies that node1 is in graph
if|if
condition|(
name|node1
operator|.
name|equals
argument_list|(
name|node2
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|allowsSelfLoops
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
name|selfLoopEdges
init|=
name|Sets
operator|.
name|filter
argument_list|(
name|outEdgesN1
argument_list|,
name|Graphs
operator|.
name|selfLoopPredicate
argument_list|(
name|this
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|selfLoopEdges
argument_list|)
return|;
block|}
name|Set
argument_list|<
name|E
argument_list|>
name|inEdgesN2
init|=
name|inEdges
argument_list|(
name|node2
argument_list|)
decl_stmt|;
return|return
operator|(
name|outEdgesN1
operator|.
name|size
argument_list|()
operator|<=
name|inEdgesN2
operator|.
name|size
argument_list|()
operator|)
condition|?
name|Sets
operator|.
name|intersection
argument_list|(
name|outEdgesN1
argument_list|,
name|inEdgesN2
argument_list|)
else|:
name|Sets
operator|.
name|intersection
argument_list|(
name|inEdgesN2
argument_list|,
name|outEdgesN1
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
DECL|method|source (Object edge)
specifier|public
name|N
name|source
parameter_list|(
name|Object
name|edge
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isDirected
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
name|NOT_AVAILABLE_ON_UNDIRECTED
argument_list|)
throw|;
block|}
return|return
name|checkedIncidentNodes
argument_list|(
name|edge
argument_list|)
operator|.
name|node1
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
if|if
condition|(
operator|!
name|isDirected
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
name|NOT_AVAILABLE_ON_UNDIRECTED
argument_list|)
throw|;
block|}
return|return
name|checkedIncidentNodes
argument_list|(
name|edge
argument_list|)
operator|.
name|node2
argument_list|()
return|;
block|}
DECL|method|checkedConnections (Object node)
specifier|protected
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
DECL|method|checkedIncidentNodes (Object edge)
specifier|protected
name|IncidentNodes
argument_list|<
name|N
argument_list|>
name|checkedIncidentNodes
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
name|IncidentNodes
argument_list|<
name|N
argument_list|>
name|incidentNodes
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
name|incidentNodes
operator|!=
literal|null
argument_list|,
name|EDGE_NOT_IN_GRAPH
argument_list|,
name|edge
argument_list|)
expr_stmt|;
return|return
name|incidentNodes
return|;
block|}
block|}
end_class

end_unit

