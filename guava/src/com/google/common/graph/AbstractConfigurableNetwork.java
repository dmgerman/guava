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
name|GraphConstants
operator|.
name|DEFAULT_EDGE_COUNT
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
name|GraphConstants
operator|.
name|DEFAULT_NODE_COUNT
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
name|GraphConstants
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
name|GraphConstants
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
comment|/**  * Abstract configurable implementation of {@link Network} that supports the options supplied  * by {@link NetworkBuilder}.  *  *<p>This class maintains a map of nodes to {@link NodeConnections}. This class also maintains  * a map of edges to reference nodes. The reference node is defined to be the edge's source node  * on directed graphs, and an arbitrary endpoint of the edge on undirected graphs.  *  *<p>{@code Set}-returning accessors return unmodifiable views: the view returned will reflect  * changes to the graph (if the graph is mutable) but may not be modified by the user.  * The behavior of the returned view is undefined in the following cases:  *<ul>  *<li>Removing the element on which the accessor is called (e.g.:  *<pre>{@code  *     Set<N> adjacentNodes = adjacentNodes(node);  *     graph.removeNode(node);}</pre>  *     At this point, the contents of {@code adjacentNodes} are undefined.  *</ul>  *  *<p>The time complexity of all {@code Set}-returning accessors is O(1), since views are returned.  *  * @author James Sexton  * @author Joshua O'Madadhain  * @author Omar Darwish  * @param<N> Node parameter type  * @param<E> Edge parameter type  */
end_comment

begin_class
DECL|class|AbstractConfigurableNetwork
specifier|abstract
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
DECL|field|nodeOrder
specifier|private
specifier|final
name|ElementOrder
argument_list|<
name|?
super|super
name|N
argument_list|>
name|nodeOrder
decl_stmt|;
DECL|field|edgeOrder
specifier|private
specifier|final
name|ElementOrder
argument_list|<
name|?
super|super
name|E
argument_list|>
name|edgeOrder
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
comment|// We could make this a Map<E, Endpoints<N>>. Although it would make incidentNodes(edge)
comment|// slightly faster, it would also make Networks consume approximately 20% more memory.
DECL|field|edgeToReferenceNode
specifier|protected
specifier|final
name|Map
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|edgeToReferenceNode
decl_stmt|;
comment|// reference node == source on directed networks
comment|/**    * Constructs a graph with the properties specified in {@code builder}.    */
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
name|AbstractConfigurableNetwork
operator|.
expr|<
name|N
argument_list|,
name|E
operator|>
name|getNodeMapForBuilder
argument_list|(
name|builder
argument_list|)
argument_list|,
name|AbstractConfigurableNetwork
operator|.
expr|<
name|N
argument_list|,
name|E
operator|>
name|getEdgeMapForBuilder
argument_list|(
name|builder
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getNodeMapForBuilder ( NetworkBuilder<? super N, ? super E> builder)
specifier|private
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
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
name|getNodeMapForBuilder
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
name|int
name|expectedNodeSize
init|=
name|builder
operator|.
name|expectedNodeCount
operator|.
name|or
argument_list|(
name|DEFAULT_NODE_COUNT
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|builder
operator|.
name|nodeOrder
operator|.
name|type
argument_list|()
condition|)
block|{
case|case
name|UNORDERED
case|:
return|return
name|Maps
operator|.
name|newHashMapWithExpectedSize
argument_list|(
name|expectedNodeSize
argument_list|)
return|;
case|case
name|INSERTION
case|:
return|return
name|Maps
operator|.
name|newLinkedHashMapWithExpectedSize
argument_list|(
name|expectedNodeSize
argument_list|)
return|;
case|case
name|SORTED
case|:
return|return
name|Maps
operator|.
name|newTreeMap
argument_list|(
name|builder
operator|.
name|nodeOrder
operator|.
name|comparator
argument_list|()
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unrecognized node ElementOrder type"
argument_list|)
throw|;
block|}
block|}
DECL|method|getEdgeMapForBuilder ( NetworkBuilder<? super N, ? super E> builder)
specifier|private
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|Map
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|getEdgeMapForBuilder
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
name|int
name|expectedEdgeSize
init|=
name|builder
operator|.
name|expectedEdgeCount
operator|.
name|or
argument_list|(
name|DEFAULT_EDGE_COUNT
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|builder
operator|.
name|edgeOrder
operator|.
name|type
argument_list|()
condition|)
block|{
case|case
name|UNORDERED
case|:
return|return
name|Maps
operator|.
name|newHashMapWithExpectedSize
argument_list|(
name|expectedEdgeSize
argument_list|)
return|;
case|case
name|INSERTION
case|:
return|return
name|Maps
operator|.
name|newLinkedHashMapWithExpectedSize
argument_list|(
name|expectedEdgeSize
argument_list|)
return|;
case|case
name|SORTED
case|:
return|return
name|Maps
operator|.
name|newTreeMap
argument_list|(
name|builder
operator|.
name|edgeOrder
operator|.
name|comparator
argument_list|()
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unrecognized edge ElementOrder type"
argument_list|)
throw|;
block|}
block|}
comment|/**    * Constructs a graph with the properties specified in {@code builder}, initialized with    * the given node and edge maps.    */
DECL|method|AbstractConfigurableNetwork (NetworkBuilder<? super N, ? super E> builder, Map<N, NodeConnections<N, E>> nodeConnections, Map<E, N> edgeToReferenceNode)
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
name|N
argument_list|>
name|edgeToReferenceNode
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
name|nodeOrder
operator|=
name|builder
operator|.
name|nodeOrder
expr_stmt|;
name|this
operator|.
name|edgeOrder
operator|=
name|builder
operator|.
name|edgeOrder
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
name|edgeToReferenceNode
operator|=
name|checkNotNull
argument_list|(
name|edgeToReferenceNode
argument_list|)
expr_stmt|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>The order of iteration for this set is determined by the {@code ElementOrder<N>} provided    * to the {@code GraphBuilder} that was used to create this instance.    * By default, that order is the order in which the nodes were added to the graph.    */
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
comment|/**    * {@inheritDoc}    *    *<p>The order of iteration for this set is determined by the {@code ElementOrder<E>} provided    * to the {@code GraphBuilder} that was used to create this instance.    * By default, that order is the order in which the edges were added to the graph.    */
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
name|edgeToReferenceNode
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
DECL|method|nodeOrder ()
specifier|public
name|ElementOrder
argument_list|<
name|?
super|super
name|N
argument_list|>
name|nodeOrder
parameter_list|()
block|{
return|return
name|nodeOrder
return|;
block|}
annotation|@
name|Override
DECL|method|edgeOrder ()
specifier|public
name|ElementOrder
argument_list|<
name|?
super|super
name|E
argument_list|>
name|edgeOrder
parameter_list|()
block|{
return|return
name|edgeOrder
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
name|Endpoints
argument_list|<
name|N
argument_list|>
name|incidentNodes
parameter_list|(
name|Object
name|edge
parameter_list|)
block|{
name|N
name|nodeA
init|=
name|checkedReferenceNode
argument_list|(
name|edge
argument_list|)
decl_stmt|;
name|N
name|nodeB
init|=
name|nodeConnections
operator|.
name|get
argument_list|(
name|nodeA
argument_list|)
operator|.
name|oppositeNode
argument_list|(
name|edge
argument_list|)
decl_stmt|;
return|return
name|Endpoints
operator|.
name|of
argument_list|(
name|nodeA
argument_list|,
name|nodeB
argument_list|,
name|isDirected
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
name|Endpoints
argument_list|<
name|N
argument_list|>
name|endpoints
init|=
name|incidentNodes
argument_list|(
name|edge
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|E
argument_list|>
name|endpointsIncidentEdges
init|=
name|Sets
operator|.
name|union
argument_list|(
name|incidentEdges
argument_list|(
name|endpoints
operator|.
name|nodeA
argument_list|()
argument_list|)
argument_list|,
name|incidentEdges
argument_list|(
name|endpoints
operator|.
name|nodeB
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
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
annotation|@
name|Override
DECL|method|edgesConnecting (Object nodeA, Object nodeB)
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|edgesConnecting
parameter_list|(
name|Object
name|nodeA
parameter_list|,
name|Object
name|nodeB
parameter_list|)
block|{
name|NodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|connectionsA
init|=
name|checkedConnections
argument_list|(
name|nodeA
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|allowsSelfLoops
operator|&&
name|nodeA
operator|.
name|equals
argument_list|(
name|nodeB
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
name|checkArgument
argument_list|(
name|containsNode
argument_list|(
name|nodeB
argument_list|)
argument_list|,
name|NODE_NOT_IN_GRAPH
argument_list|,
name|nodeB
argument_list|)
expr_stmt|;
return|return
name|connectionsA
operator|.
name|edgesConnecting
argument_list|(
name|nodeB
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
DECL|method|checkedConnections (Object node)
specifier|protected
specifier|final
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
DECL|method|checkedReferenceNode (Object edge)
specifier|protected
specifier|final
name|N
name|checkedReferenceNode
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
name|N
name|referenceNode
init|=
name|edgeToReferenceNode
operator|.
name|get
argument_list|(
name|edge
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|referenceNode
operator|!=
literal|null
argument_list|,
name|EDGE_NOT_IN_GRAPH
argument_list|,
name|edge
argument_list|)
expr_stmt|;
return|return
name|referenceNode
return|;
block|}
DECL|method|containsNode (@ullable Object node)
specifier|protected
specifier|final
name|boolean
name|containsNode
parameter_list|(
annotation|@
name|Nullable
name|Object
name|node
parameter_list|)
block|{
return|return
name|nodeConnections
operator|.
name|containsKey
argument_list|(
name|node
argument_list|)
return|;
block|}
DECL|method|containsEdge (@ullable Object edge)
specifier|protected
specifier|final
name|boolean
name|containsEdge
parameter_list|(
annotation|@
name|Nullable
name|Object
name|edge
parameter_list|)
block|{
return|return
name|edgeToReferenceNode
operator|.
name|containsKey
argument_list|(
name|edge
argument_list|)
return|;
block|}
block|}
end_class

end_unit

