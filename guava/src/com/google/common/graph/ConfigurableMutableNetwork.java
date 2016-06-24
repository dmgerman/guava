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
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|GraphConstants
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
name|GraphConstants
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
name|ImmutableList
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

begin_comment
comment|/**  * Configurable implementation of {@link MutableNetwork} that supports both directed and undirected  * graphs. Instances of this class should be constructed with {@link NetworkBuilder}.  *  *<p>Time complexities for mutation methods are all O(1) except for {@code removeNode(N node)},  * which is in O(d_node) where d_node is the degree of {@code node}.  *  * @author James Sexton  * @author Joshua O'Madadhain  * @author Omar Darwish  * @param<N> Node parameter type  * @param<E> Edge parameter type  */
end_comment

begin_class
DECL|class|ConfigurableMutableNetwork
specifier|final
class|class
name|ConfigurableMutableNetwork
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
extends|extends
name|AbstractConfigurableNetwork
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
implements|implements
name|MutableNetwork
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
block|{
comment|/**    * Constructs a mutable graph with the properties specified in {@code builder}.    */
DECL|method|ConfigurableMutableNetwork (NetworkBuilder<? super N, ? super E> builder)
name|ConfigurableMutableNetwork
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
name|super
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
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
name|addNodeInternal
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/**    * Adds {@code node} to the graph and returns the associated {@link NodeConnections}.    *    * @throws IllegalStateException if {@code node} is already present    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addNodeInternal (N node)
specifier|private
name|NodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|addNodeInternal
parameter_list|(
name|N
name|node
parameter_list|)
block|{
name|NodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|connections
init|=
name|newNodeConnections
argument_list|()
decl_stmt|;
name|checkState
argument_list|(
name|nodeConnections
operator|.
name|put
argument_list|(
name|node
argument_list|,
name|connections
argument_list|)
operator|==
literal|null
argument_list|)
expr_stmt|;
return|return
name|connections
return|;
block|}
comment|/**    * Add nodes that are not elements of the graph, then add {@code edge} between them.    * Return {@code false} if {@code edge} already exists between {@code nodeA} and {@code nodeB},    * and in the same direction.    *    * @throws IllegalArgumentException if an edge (other than {@code edge}) already    *         exists from {@code nodeA} to {@code nodeB}, and this is not a multigraph.    *         Also, if self-loops are not allowed, and {@code nodeA} is equal to {@code nodeB}.    */
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
DECL|method|addEdge (E edge, N nodeA, N nodeB)
specifier|public
name|boolean
name|addEdge
parameter_list|(
name|E
name|edge
parameter_list|,
name|N
name|nodeA
parameter_list|,
name|N
name|nodeB
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
name|nodeA
argument_list|,
literal|"nodeA"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|nodeB
argument_list|,
literal|"nodeB"
argument_list|)
expr_stmt|;
if|if
condition|(
name|containsEdge
argument_list|(
name|edge
argument_list|)
condition|)
block|{
name|Endpoints
argument_list|<
name|N
argument_list|>
name|existingEndpoints
init|=
name|incidentNodes
argument_list|(
name|edge
argument_list|)
decl_stmt|;
name|Endpoints
argument_list|<
name|N
argument_list|>
name|newEndpoints
init|=
name|Endpoints
operator|.
name|of
argument_list|(
name|nodeA
argument_list|,
name|nodeB
argument_list|,
name|isDirected
argument_list|()
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|existingEndpoints
operator|.
name|equals
argument_list|(
name|newEndpoints
argument_list|)
argument_list|,
name|REUSING_EDGE
argument_list|,
name|edge
argument_list|,
name|existingEndpoints
argument_list|,
name|newEndpoints
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|NodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|connectionsA
init|=
name|nodeConnections
operator|.
name|get
argument_list|(
name|nodeA
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|allowsParallelEdges
argument_list|()
condition|)
block|{
name|checkArgument
argument_list|(
operator|!
operator|(
name|connectionsA
operator|!=
literal|null
operator|&&
name|connectionsA
operator|.
name|successors
argument_list|()
operator|.
name|contains
argument_list|(
name|nodeB
argument_list|)
operator|)
argument_list|,
name|ADDING_PARALLEL_EDGE
argument_list|,
name|nodeA
argument_list|,
name|nodeB
argument_list|)
expr_stmt|;
block|}
name|boolean
name|isSelfLoop
init|=
name|nodeA
operator|.
name|equals
argument_list|(
name|nodeB
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|allowsSelfLoops
argument_list|()
condition|)
block|{
name|checkArgument
argument_list|(
operator|!
name|isSelfLoop
argument_list|,
name|SELF_LOOPS_NOT_ALLOWED
argument_list|,
name|nodeA
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|connectionsA
operator|==
literal|null
condition|)
block|{
name|connectionsA
operator|=
name|addNodeInternal
argument_list|(
name|nodeA
argument_list|)
expr_stmt|;
block|}
name|connectionsA
operator|.
name|addOutEdge
argument_list|(
name|edge
argument_list|,
name|nodeB
argument_list|)
expr_stmt|;
name|NodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|connectionsB
init|=
name|isSelfLoop
condition|?
name|connectionsA
else|:
name|nodeConnections
operator|.
name|get
argument_list|(
name|nodeB
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectionsB
operator|==
literal|null
condition|)
block|{
name|connectionsB
operator|=
name|addNodeInternal
argument_list|(
name|nodeB
argument_list|)
expr_stmt|;
block|}
name|connectionsB
operator|.
name|addInEdge
argument_list|(
name|edge
argument_list|,
name|nodeA
argument_list|,
name|isSelfLoop
argument_list|)
expr_stmt|;
name|edgeToReferenceNode
operator|.
name|put
argument_list|(
name|edge
argument_list|,
name|nodeA
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
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
if|if
condition|(
name|connections
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Since views are returned, we need to copy the edges that will be removed.
comment|// Thus we avoid modifying the underlying view while iterating over it.
for|for
control|(
name|E
name|edge
range|:
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|connections
operator|.
name|incidentEdges
argument_list|()
argument_list|)
control|)
block|{
name|removeEdge
argument_list|(
name|edge
argument_list|)
expr_stmt|;
block|}
name|nodeConnections
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
annotation|@
name|CanIgnoreReturnValue
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
name|N
name|nodeA
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
name|nodeA
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|NodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|connectionsA
init|=
name|nodeConnections
operator|.
name|get
argument_list|(
name|nodeA
argument_list|)
decl_stmt|;
name|N
name|nodeB
init|=
name|connectionsA
operator|.
name|oppositeNode
argument_list|(
name|edge
argument_list|)
decl_stmt|;
name|boolean
name|isSelfLoop
init|=
name|allowsSelfLoops
argument_list|()
operator|&&
name|nodeA
operator|.
name|equals
argument_list|(
name|nodeB
argument_list|)
decl_stmt|;
name|NodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|connectionsB
init|=
name|isSelfLoop
condition|?
name|connectionsA
else|:
name|nodeConnections
operator|.
name|get
argument_list|(
name|nodeB
argument_list|)
decl_stmt|;
name|connectionsA
operator|.
name|removeOutEdge
argument_list|(
name|edge
argument_list|)
expr_stmt|;
name|connectionsB
operator|.
name|removeInEdge
argument_list|(
name|edge
argument_list|,
name|isSelfLoop
argument_list|)
expr_stmt|;
name|edgeToReferenceNode
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
DECL|method|newNodeConnections ()
specifier|private
name|NodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|newNodeConnections
parameter_list|()
block|{
return|return
name|isDirected
argument_list|()
condition|?
name|allowsParallelEdges
argument_list|()
condition|?
name|DirectedMultiNodeConnections
operator|.
expr|<
name|N
operator|,
name|E
operator|>
name|of
argument_list|()
operator|:
name|DirectedNodeConnections
operator|.
expr|<
name|N
operator|,
name|E
operator|>
name|of
argument_list|()
operator|:
name|allowsParallelEdges
argument_list|()
condition|?
name|UndirectedMultiNodeConnections
operator|.
expr|<
name|N
operator|,
name|E
operator|>
name|of
argument_list|()
operator|:
name|UndirectedNodeConnections
operator|.
expr|<
name|N
operator|,
name|E
operator|>
name|of
argument_list|()
return|;
block|}
block|}
end_class

end_unit

