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
name|Graphs
operator|.
name|checkNonNegative
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
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_comment
comment|/**  * Configurable implementation of {@link ValueGraph} that supports the options supplied by {@link  * AbstractGraphBuilder}.  *  *<p>This class maintains a map of nodes to {@link GraphConnections}.  *  *<p>Collection-returning accessors return unmodifiable views: the view returned will reflect  * changes to the graph (if the graph is mutable) but may not be modified by the user.  *  *<p>The time complexity of all collection-returning accessors is O(1), since views are returned.  *  * @author James Sexton  * @author Joshua O'Madadhain  * @author Omar Darwish  * @param<N> Node parameter type  * @param<V> Value parameter type  */
end_comment

begin_class
DECL|class|ConfigurableValueGraph
class|class
name|ConfigurableValueGraph
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
block|{
DECL|field|isDirected
specifier|private
specifier|final
name|boolean
name|isDirected
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
DECL|field|nodeConnections
specifier|protected
specifier|final
name|MapIteratorCache
argument_list|<
name|N
argument_list|,
name|GraphConnections
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
argument_list|>
name|nodeConnections
decl_stmt|;
DECL|field|edgeCount
specifier|protected
name|long
name|edgeCount
decl_stmt|;
comment|// must be updated when edges are added or removed
comment|/** Constructs a graph with the properties specified in {@code builder}. */
DECL|method|ConfigurableValueGraph (AbstractGraphBuilder<? super N> builder)
name|ConfigurableValueGraph
parameter_list|(
name|AbstractGraphBuilder
argument_list|<
name|?
super|super
name|N
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
name|GraphConnections
argument_list|<
name|N
argument_list|,
name|V
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
literal|0L
argument_list|)
expr_stmt|;
block|}
comment|/**    * Constructs a graph with the properties specified in {@code builder}, initialized with the given    * node map.    */
DECL|method|ConfigurableValueGraph ( AbstractGraphBuilder<? super N> builder, Map<N, GraphConnections<N, V>> nodeConnections, long edgeCount)
name|ConfigurableValueGraph
parameter_list|(
name|AbstractGraphBuilder
argument_list|<
name|?
super|super
name|N
argument_list|>
name|builder
parameter_list|,
name|Map
argument_list|<
name|N
argument_list|,
name|GraphConnections
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
argument_list|>
name|nodeConnections
parameter_list|,
name|long
name|edgeCount
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
comment|// Prefer the heavier "MapRetrievalCache" for nodes if lookup is expensive.
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
name|GraphConnections
argument_list|<
name|N
argument_list|,
name|V
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
name|GraphConnections
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|(
name|nodeConnections
argument_list|)
expr_stmt|;
name|this
operator|.
name|edgeCount
operator|=
name|checkNonNegative
argument_list|(
name|edgeCount
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
annotation|@
name|Override
DECL|method|hasEdgeConnecting (N nodeU, N nodeV)
specifier|public
name|boolean
name|hasEdgeConnecting
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
block|{
return|return
name|hasEdgeConnecting_internal
argument_list|(
name|checkNotNull
argument_list|(
name|nodeU
argument_list|)
argument_list|,
name|checkNotNull
argument_list|(
name|nodeV
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hasEdgeConnecting (EndpointPair<N> endpoints)
specifier|public
name|boolean
name|hasEdgeConnecting
parameter_list|(
name|EndpointPair
argument_list|<
name|N
argument_list|>
name|endpoints
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|endpoints
argument_list|)
expr_stmt|;
return|return
name|isOrderingCompatible
argument_list|(
name|endpoints
argument_list|)
operator|&&
name|hasEdgeConnecting_internal
argument_list|(
name|endpoints
operator|.
name|nodeU
argument_list|()
argument_list|,
name|endpoints
operator|.
name|nodeV
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|NullableDecl
DECL|method|edgeValueOrDefault (N nodeU, N nodeV, @NullableDecl V defaultValue)
specifier|public
name|V
name|edgeValueOrDefault
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|,
annotation|@
name|NullableDecl
name|V
name|defaultValue
parameter_list|)
block|{
return|return
name|edgeValueOrDefault_internal
argument_list|(
name|checkNotNull
argument_list|(
name|nodeU
argument_list|)
argument_list|,
name|checkNotNull
argument_list|(
name|nodeV
argument_list|)
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|NullableDecl
DECL|method|edgeValueOrDefault (EndpointPair<N> endpoints, @NullableDecl V defaultValue)
specifier|public
name|V
name|edgeValueOrDefault
parameter_list|(
name|EndpointPair
argument_list|<
name|N
argument_list|>
name|endpoints
parameter_list|,
annotation|@
name|NullableDecl
name|V
name|defaultValue
parameter_list|)
block|{
name|validateEndpoints
argument_list|(
name|endpoints
argument_list|)
expr_stmt|;
return|return
name|edgeValueOrDefault_internal
argument_list|(
name|endpoints
operator|.
name|nodeU
argument_list|()
argument_list|,
name|endpoints
operator|.
name|nodeV
argument_list|()
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|edgeCount ()
specifier|protected
name|long
name|edgeCount
parameter_list|()
block|{
return|return
name|edgeCount
return|;
block|}
DECL|method|checkedConnections (N node)
specifier|protected
specifier|final
name|GraphConnections
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|checkedConnections
parameter_list|(
name|N
name|node
parameter_list|)
block|{
name|GraphConnections
argument_list|<
name|N
argument_list|,
name|V
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
literal|"Node "
operator|+
name|node
operator|+
literal|" is not an element of this graph."
argument_list|)
throw|;
block|}
return|return
name|connections
return|;
block|}
DECL|method|containsNode (@ullableDecl N node)
specifier|protected
specifier|final
name|boolean
name|containsNode
parameter_list|(
annotation|@
name|NullableDecl
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
DECL|method|hasEdgeConnecting_internal (N nodeU, N nodeV)
specifier|protected
specifier|final
name|boolean
name|hasEdgeConnecting_internal
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
block|{
name|GraphConnections
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|connectionsU
init|=
name|nodeConnections
operator|.
name|get
argument_list|(
name|nodeU
argument_list|)
decl_stmt|;
return|return
operator|(
name|connectionsU
operator|!=
literal|null
operator|)
operator|&&
name|connectionsU
operator|.
name|successors
argument_list|()
operator|.
name|contains
argument_list|(
name|nodeV
argument_list|)
return|;
block|}
DECL|method|edgeValueOrDefault_internal (N nodeU, N nodeV, V defaultValue)
specifier|protected
specifier|final
name|V
name|edgeValueOrDefault_internal
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|,
name|V
name|defaultValue
parameter_list|)
block|{
name|GraphConnections
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|connectionsU
init|=
name|nodeConnections
operator|.
name|get
argument_list|(
name|nodeU
argument_list|)
decl_stmt|;
name|V
name|value
init|=
operator|(
name|connectionsU
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|connectionsU
operator|.
name|value
argument_list|(
name|nodeV
argument_list|)
decl_stmt|;
return|return
name|value
operator|==
literal|null
condition|?
name|defaultValue
else|:
name|value
return|;
block|}
block|}
end_class

end_unit

