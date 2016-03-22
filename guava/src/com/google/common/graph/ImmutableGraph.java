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
name|checkNotNull
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
comment|/**  * A {@link Graph} whose contents will never change. Instances of this class should be obtained  * with {@link #copyOf(Graph)}.  *  *<p>The time complexity of {@code edgesConnecting(node1, node2)} is O(min(outD_node1, inD_node2)).  *  * @author James Sexton  * @author Joshua O'Madadhain  * @author Omar Darwish  * @param<N> Node parameter type  * @param<E> Edge parameter type  */
end_comment

begin_class
DECL|class|ImmutableGraph
specifier|public
specifier|final
class|class
name|ImmutableGraph
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
extends|extends
name|ConfigurableGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
block|{
DECL|method|ImmutableGraph (Graph<N, E> graph)
specifier|private
name|ImmutableGraph
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|graph
parameter_list|)
block|{
name|super
argument_list|(
name|GraphBuilder
operator|.
name|from
argument_list|(
name|graph
argument_list|)
argument_list|,
name|getNodeConnections
argument_list|(
name|graph
argument_list|)
argument_list|,
name|getEdgeToIncidentNodes
argument_list|(
name|graph
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns an immutable copy of {@code graph}.    */
DECL|method|copyOf (Graph<N, E> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|ImmutableGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copyOf
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|graph
parameter_list|)
block|{
return|return
operator|(
name|graph
operator|instanceof
name|ImmutableGraph
operator|)
condition|?
operator|(
name|ImmutableGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
operator|)
name|graph
else|:
operator|new
name|ImmutableGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|(
name|graph
argument_list|)
return|;
block|}
comment|/**    * Simply returns its argument.    *    * @deprecated no need to use this    */
annotation|@
name|Deprecated
DECL|method|copyOf (ImmutableGraph<N, E> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|ImmutableGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copyOf
parameter_list|(
name|ImmutableGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|graph
parameter_list|)
block|{
return|return
name|checkNotNull
argument_list|(
name|graph
argument_list|)
return|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the graph unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|addNode (N node)
specifier|public
specifier|final
name|boolean
name|addNode
parameter_list|(
name|N
name|node
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the graph unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|addEdge (E edge, N node1, N node2)
specifier|public
specifier|final
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the graph unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|removeNode (Object node)
specifier|public
specifier|final
name|boolean
name|removeNode
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the graph unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|removeEdge (Object edge)
specifier|public
specifier|final
name|boolean
name|removeEdge
parameter_list|(
name|Object
name|edge
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
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
comment|// This set is calculated as the intersection of two sets, and is likely to be small.
comment|// As an optimization, copy it to an ImmutableSet so re-iterating is fast.
return|return
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|super
operator|.
name|edgesConnecting
argument_list|(
name|node1
argument_list|,
name|node2
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getNodeConnections (Graph<N, E> graph)
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
name|getNodeConnections
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|graph
parameter_list|)
block|{
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
name|nodeConnections
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
name|graph
operator|.
name|nodes
argument_list|()
control|)
block|{
name|nodeConnections
operator|.
name|put
argument_list|(
name|node
argument_list|,
name|nodeConnectionsOf
argument_list|(
name|graph
argument_list|,
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|nodeConnections
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|getEdgeToIncidentNodes (Graph<N, E> graph)
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
name|IncidentNodes
argument_list|<
name|N
argument_list|>
argument_list|>
name|getEdgeToIncidentNodes
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|graph
parameter_list|)
block|{
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|E
argument_list|,
name|IncidentNodes
argument_list|<
name|N
argument_list|>
argument_list|>
name|edgeToIncidentNodes
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
name|graph
operator|.
name|edges
argument_list|()
control|)
block|{
name|edgeToIncidentNodes
operator|.
name|put
argument_list|(
name|edge
argument_list|,
name|IncidentNodes
operator|.
name|of
argument_list|(
name|graph
operator|.
name|incidentNodes
argument_list|(
name|edge
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|edgeToIncidentNodes
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|nodeConnectionsOf (Graph<N, E> graph, N node)
specifier|private
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|NodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|nodeConnectionsOf
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|graph
parameter_list|,
name|N
name|node
parameter_list|)
block|{
return|return
name|graph
operator|.
name|isDirected
argument_list|()
condition|?
name|DirectedNodeConnections
operator|.
name|ofImmutable
argument_list|(
name|graph
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
argument_list|,
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
argument_list|,
name|graph
operator|.
name|inEdges
argument_list|(
name|node
argument_list|)
argument_list|,
name|graph
operator|.
name|outEdges
argument_list|(
name|node
argument_list|)
argument_list|)
else|:
name|UndirectedNodeConnections
operator|.
name|ofImmutable
argument_list|(
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
argument_list|,
name|graph
operator|.
name|incidentEdges
argument_list|(
name|node
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit
