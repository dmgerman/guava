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
name|annotations
operator|.
name|GwtIncompatible
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
name|java
operator|.
name|util
operator|.
name|TreeMap
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
comment|/**  * Configurable implementation of {@link Network} that supports the options supplied by {@link  * NetworkBuilder}.  *  *<p>This class maintains a map of nodes to {@link NetworkConnections}. This class also maintains a  * map of edges to reference nodes. The reference node is defined to be the edge's source node on  * directed graphs, and an arbitrary endpoint of the edge on undirected graphs.  *  *<p>Collection-returning accessors return unmodifiable views: the view returned will reflect  * changes to the graph (if the graph is mutable) but may not be modified by the user.  *  *<p>The time complexity of all collection-returning accessors is O(1), since views are returned.  *  * @author James Sexton  * @author Joshua O'Madadhain  * @author Omar Darwish  * @param<N> Node parameter type  * @param<E> Edge parameter type  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|ConfigurableNetwork
class|class
name|ConfigurableNetwork
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
name|N
argument_list|>
name|nodeOrder
decl_stmt|;
DECL|field|edgeOrder
specifier|private
specifier|final
name|ElementOrder
argument_list|<
name|E
argument_list|>
name|edgeOrder
decl_stmt|;
DECL|field|nodeConnections
specifier|protected
specifier|final
name|MapIteratorCache
argument_list|<
name|N
argument_list|,
name|NetworkConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|>
name|nodeConnections
decl_stmt|;
comment|// We could make this a Map<E, EndpointPair<N>>. It would make incidentNodes(edge) slightly
comment|// faster, but also make Networks consume 5 to 20+% (increasing with average degree) more memory.
DECL|field|edgeToReferenceNode
specifier|protected
specifier|final
name|MapIteratorCache
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|edgeToReferenceNode
decl_stmt|;
comment|// referenceNode == source if directed
comment|/** Constructs a graph with the properties specified in {@code builder}. */
DECL|method|ConfigurableNetwork (NetworkBuilder<? super N, ? super E> builder)
name|ConfigurableNetwork
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
name|builder
operator|.
name|nodeOrder
operator|.
expr|<
name|N
argument_list|,
name|NetworkConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
operator|>
name|createMap
argument_list|(
name|builder
operator|.
name|expectedNodeCount
operator|.
name|or
argument_list|(
name|DEFAULT_NODE_COUNT
argument_list|)
argument_list|)
argument_list|,
name|builder
operator|.
name|edgeOrder
operator|.
expr|<
name|E
argument_list|,
name|N
operator|>
name|createMap
argument_list|(
name|builder
operator|.
name|expectedEdgeCount
operator|.
name|or
argument_list|(
name|DEFAULT_EDGE_COUNT
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Constructs a graph with the properties specified in {@code builder}, initialized with the given    * node and edge maps.    */
DECL|method|ConfigurableNetwork ( NetworkBuilder<? super N, ? super E> builder, Map<N, NetworkConnections<N, E>> nodeConnections, Map<E, N> edgeToReferenceNode)
name|ConfigurableNetwork
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
name|NetworkConnections
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
operator|.
name|cast
argument_list|()
expr_stmt|;
name|this
operator|.
name|edgeOrder
operator|=
name|builder
operator|.
name|edgeOrder
operator|.
name|cast
argument_list|()
expr_stmt|;
comment|// Prefer the heavier "MapRetrievalCache" for nodes if lookup is expensive. This optimizes
comment|// methods that access the same node(s) repeatedly, such as Graphs.removeEdgesConnecting().
name|this
operator|.
name|nodeConnections
operator|=
operator|(
name|nodeConnections
operator|instanceof
name|TreeMap
operator|)
condition|?
operator|new
name|MapRetrievalCache
argument_list|<
name|N
argument_list|,
name|NetworkConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|>
argument_list|(
name|nodeConnections
argument_list|)
else|:
operator|new
name|MapIteratorCache
argument_list|<
name|N
argument_list|,
name|NetworkConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|>
argument_list|(
name|nodeConnections
argument_list|)
expr_stmt|;
name|this
operator|.
name|edgeToReferenceNode
operator|=
operator|new
name|MapIteratorCache
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
argument_list|(
name|edgeToReferenceNode
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
name|nodeConnections
operator|.
name|unmodifiableKeySet
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
name|edgeToReferenceNode
operator|.
name|unmodifiableKeySet
argument_list|()
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
DECL|method|incidentEdges (N node)
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|incidentEdges
parameter_list|(
name|N
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
DECL|method|incidentNodes (E edge)
specifier|public
name|EndpointPair
argument_list|<
name|N
argument_list|>
name|incidentNodes
parameter_list|(
name|E
name|edge
parameter_list|)
block|{
name|N
name|nodeU
init|=
name|checkedReferenceNode
argument_list|(
name|edge
argument_list|)
decl_stmt|;
name|N
name|nodeV
init|=
name|nodeConnections
operator|.
name|get
argument_list|(
name|nodeU
argument_list|)
operator|.
name|adjacentNode
argument_list|(
name|edge
argument_list|)
decl_stmt|;
return|return
name|EndpointPair
operator|.
name|of
argument_list|(
name|this
argument_list|,
name|nodeU
argument_list|,
name|nodeV
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|adjacentNodes (N node)
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|adjacentNodes
parameter_list|(
name|N
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
DECL|method|edgesConnecting (N nodeU, N nodeV)
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|edgesConnecting
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
block|{
name|NetworkConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|connectionsU
init|=
name|checkedConnections
argument_list|(
name|nodeU
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|allowsSelfLoops
operator|&&
name|nodeU
operator|==
name|nodeV
condition|)
block|{
comment|// just an optimization, only check reference equality
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
name|nodeV
argument_list|)
argument_list|,
name|NODE_NOT_IN_GRAPH
argument_list|,
name|nodeV
argument_list|)
expr_stmt|;
return|return
name|connectionsU
operator|.
name|edgesConnecting
argument_list|(
name|nodeV
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|inEdges (N node)
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|inEdges
parameter_list|(
name|N
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
DECL|method|outEdges (N node)
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|outEdges
parameter_list|(
name|N
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
DECL|method|predecessors (N node)
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|predecessors
parameter_list|(
name|N
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
DECL|method|successors (N node)
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|successors
parameter_list|(
name|N
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
DECL|method|checkedConnections (N node)
specifier|protected
specifier|final
name|NetworkConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|checkedConnections
parameter_list|(
name|N
name|node
parameter_list|)
block|{
name|NetworkConnections
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
if|if
condition|(
name|connections
operator|==
literal|null
condition|)
block|{
name|checkNotNull
argument_list|(
name|node
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|,
name|node
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|connections
return|;
block|}
DECL|method|checkedReferenceNode (E edge)
specifier|protected
specifier|final
name|N
name|checkedReferenceNode
parameter_list|(
name|E
name|edge
parameter_list|)
block|{
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
if|if
condition|(
name|referenceNode
operator|==
literal|null
condition|)
block|{
name|checkNotNull
argument_list|(
name|edge
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|EDGE_NOT_IN_GRAPH
argument_list|,
name|edge
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|referenceNode
return|;
block|}
DECL|method|containsNode (@ullable N node)
specifier|protected
specifier|final
name|boolean
name|containsNode
parameter_list|(
annotation|@
name|Nullable
name|N
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
DECL|method|containsEdge (@ullable E edge)
specifier|protected
specifier|final
name|boolean
name|containsEdge
parameter_list|(
annotation|@
name|Nullable
name|E
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

