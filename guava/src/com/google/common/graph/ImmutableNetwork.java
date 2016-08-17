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
name|Function
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
name|Maps
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

begin_comment
comment|/**  * A {@link Network} whose relationships are constant. Instances of this class may be obtained  * with {@link #copyOf(Network)}.  *  * @author James Sexton  * @author Joshua O'Madadhain  * @author Omar Darwish  * @param<N> Node parameter type  * @param<E> Edge parameter type  * @since 20.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|ImmutableNetwork
specifier|public
class|class
name|ImmutableNetwork
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
extends|extends
name|ConfigurableNetwork
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
block|{
DECL|method|ImmutableNetwork (Network<N, E> graph)
specifier|private
name|ImmutableNetwork
parameter_list|(
name|Network
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
name|NetworkBuilder
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
name|getEdgeToReferenceNode
argument_list|(
name|graph
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns an immutable copy of {@code graph}.    */
DECL|method|copyOf (Network<N, E> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|ImmutableNetwork
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copyOf
parameter_list|(
name|Network
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
name|ImmutableNetwork
operator|)
condition|?
operator|(
name|ImmutableNetwork
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
operator|)
name|graph
else|:
operator|new
name|ImmutableNetwork
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
DECL|method|copyOf (ImmutableNetwork<N, E> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|ImmutableNetwork
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copyOf
parameter_list|(
name|ImmutableNetwork
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
DECL|method|getNodeConnections (Network<N, E> graph)
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
name|NetworkConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|>
name|getNodeConnections
parameter_list|(
name|Network
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|graph
parameter_list|)
block|{
comment|// ImmutableMap.Builder maintains the order of the elements as inserted, so the map will have
comment|// whatever ordering the graph's nodes do, so ImmutableSortedMap is unnecessary even if the
comment|// input nodes are sorted.
name|ImmutableMap
operator|.
name|Builder
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
name|connectionsOf
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
DECL|method|getEdgeToReferenceNode (Network<N, E> graph)
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
name|getEdgeToReferenceNode
parameter_list|(
name|Network
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|graph
parameter_list|)
block|{
comment|// ImmutableMap.Builder maintains the order of the elements as inserted, so the map will
comment|// have whatever ordering the graph's edges do, so ImmutableSortedMap is unnecessary even if the
comment|// input edges are sorted.
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|edgeToReferenceNode
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
name|edgeToReferenceNode
operator|.
name|put
argument_list|(
name|edge
argument_list|,
name|graph
operator|.
name|incidentNodes
argument_list|(
name|edge
argument_list|)
operator|.
name|nodeA
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|edgeToReferenceNode
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|connectionsOf (Network<N, E> graph, N node)
specifier|private
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|NetworkConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|connectionsOf
parameter_list|(
name|Network
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
if|if
condition|(
name|graph
operator|.
name|isDirected
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|inEdgeMap
init|=
name|Maps
operator|.
name|asMap
argument_list|(
name|graph
operator|.
name|inEdges
argument_list|(
name|node
argument_list|)
argument_list|,
name|sourceNodeFn
argument_list|(
name|graph
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|outEdgeMap
init|=
name|Maps
operator|.
name|asMap
argument_list|(
name|graph
operator|.
name|outEdges
argument_list|(
name|node
argument_list|)
argument_list|,
name|targetNodeFn
argument_list|(
name|graph
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|selfLoopCount
init|=
name|graph
operator|.
name|edgesConnecting
argument_list|(
name|node
argument_list|,
name|node
argument_list|)
operator|.
name|size
argument_list|()
decl_stmt|;
return|return
name|graph
operator|.
name|allowsParallelEdges
argument_list|()
condition|?
name|DirectedMultiNetworkConnections
operator|.
name|ofImmutable
argument_list|(
name|inEdgeMap
argument_list|,
name|outEdgeMap
argument_list|,
name|selfLoopCount
argument_list|)
else|:
name|DirectedNetworkConnections
operator|.
name|ofImmutable
argument_list|(
name|inEdgeMap
argument_list|,
name|outEdgeMap
argument_list|,
name|selfLoopCount
argument_list|)
return|;
block|}
else|else
block|{
name|Map
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|incidentEdgeMap
init|=
name|Maps
operator|.
name|asMap
argument_list|(
name|graph
operator|.
name|incidentEdges
argument_list|(
name|node
argument_list|)
argument_list|,
name|adjacentNodeFn
argument_list|(
name|graph
argument_list|,
name|node
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|graph
operator|.
name|allowsParallelEdges
argument_list|()
condition|?
name|UndirectedMultiNetworkConnections
operator|.
name|ofImmutable
argument_list|(
name|incidentEdgeMap
argument_list|)
else|:
name|UndirectedNetworkConnections
operator|.
name|ofImmutable
argument_list|(
name|incidentEdgeMap
argument_list|)
return|;
block|}
block|}
DECL|method|sourceNodeFn (final Network<N, E> graph)
specifier|private
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|Function
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|sourceNodeFn
parameter_list|(
specifier|final
name|Network
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
name|Function
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|N
name|apply
parameter_list|(
name|E
name|edge
parameter_list|)
block|{
return|return
name|graph
operator|.
name|incidentNodes
argument_list|(
name|edge
argument_list|)
operator|.
name|source
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|targetNodeFn (final Network<N, E> graph)
specifier|private
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|Function
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|targetNodeFn
parameter_list|(
specifier|final
name|Network
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
name|Function
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|N
name|apply
parameter_list|(
name|E
name|edge
parameter_list|)
block|{
return|return
name|graph
operator|.
name|incidentNodes
argument_list|(
name|edge
argument_list|)
operator|.
name|target
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|adjacentNodeFn (final Network<N, E> graph, final N node)
specifier|private
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|Function
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|adjacentNodeFn
parameter_list|(
specifier|final
name|Network
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|graph
parameter_list|,
specifier|final
name|N
name|node
parameter_list|)
block|{
return|return
operator|new
name|Function
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|N
name|apply
parameter_list|(
name|E
name|edge
parameter_list|)
block|{
return|return
name|graph
operator|.
name|incidentNodes
argument_list|(
name|edge
argument_list|)
operator|.
name|adjacentNode
argument_list|(
name|node
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

