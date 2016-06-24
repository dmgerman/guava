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
name|SELF_LOOPS_NOT_ALLOWED
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
comment|/**  * Configurable implementation of {@link MutableGraph} that supports both directed and undirected  * graphs. Instances of this class should be constructed with {@link GraphBuilder}.  *  *<p>Time complexities for mutation methods are all O(1) except for {@code removeNode(N node)},  * which is in O(d_node) where d_node is the degree of {@code node}.  *  * @author James Sexton  * @author Joshua O'Madadhain  * @author Omar Darwish  * @param<N> Node parameter type  */
end_comment

begin_class
DECL|class|ConfigurableMutableGraph
specifier|final
class|class
name|ConfigurableMutableGraph
parameter_list|<
name|N
parameter_list|>
extends|extends
name|AbstractConfigurableGraph
argument_list|<
name|N
argument_list|>
implements|implements
name|MutableGraph
argument_list|<
name|N
argument_list|>
block|{
comment|/**    * Constructs a mutable graph with the properties specified in {@code builder}.    */
DECL|method|ConfigurableMutableGraph (GraphBuilder<? super N> builder)
name|ConfigurableMutableGraph
parameter_list|(
name|GraphBuilder
argument_list|<
name|?
super|super
name|N
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
comment|/**    * Adds {@code node} to the graph and returns the associated {@link NodeAdjacencies}.    *    * @throws IllegalStateException if {@code node} is already present    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addNodeInternal (N node)
specifier|private
name|NodeAdjacencies
argument_list|<
name|N
argument_list|>
name|addNodeInternal
parameter_list|(
name|N
name|node
parameter_list|)
block|{
name|NodeAdjacencies
argument_list|<
name|N
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
comment|/**    * Add an edge between {@code nodeA} and {@code nodeB}; if these nodes are not already    * present in this graph, then add them.    * Return {@code false} if an edge already exists between {@code nodeA} and {@code nodeB},    * and in the same direction.    *    * @throws IllegalArgumentException if self-loops are not allowed, and {@code nodeA} is equal to    *     {@code nodeB}.    */
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
DECL|method|addEdge (N nodeA, N nodeB)
specifier|public
name|boolean
name|addEdge
parameter_list|(
name|N
name|nodeA
parameter_list|,
name|N
name|nodeB
parameter_list|)
block|{
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
name|NodeAdjacencies
argument_list|<
name|N
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
comment|// TODO(b/28087289): does not support parallel edges
if|if
condition|(
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
condition|)
block|{
return|return
literal|false
return|;
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
name|addSuccessor
argument_list|(
name|nodeB
argument_list|)
expr_stmt|;
name|NodeAdjacencies
argument_list|<
name|N
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
name|addPredecessor
argument_list|(
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
name|NodeAdjacencies
argument_list|<
name|N
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
if|if
condition|(
name|allowsSelfLoops
argument_list|()
condition|)
block|{
comment|// Remove any potential self-loop first so we won't get CME while removing incident edges.
name|connections
operator|.
name|removeSuccessor
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|connections
operator|.
name|removePredecessor
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|N
name|successor
range|:
name|connections
operator|.
name|successors
argument_list|()
control|)
block|{
name|nodeConnections
operator|.
name|get
argument_list|(
name|successor
argument_list|)
operator|.
name|removePredecessor
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|N
name|predecessor
range|:
name|connections
operator|.
name|predecessors
argument_list|()
control|)
block|{
name|nodeConnections
operator|.
name|get
argument_list|(
name|predecessor
argument_list|)
operator|.
name|removeSuccessor
argument_list|(
name|node
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
DECL|method|removeEdge (Object nodeA, Object nodeB)
specifier|public
name|boolean
name|removeEdge
parameter_list|(
name|Object
name|nodeA
parameter_list|,
name|Object
name|nodeB
parameter_list|)
block|{
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
name|NodeAdjacencies
argument_list|<
name|N
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
name|connectionsA
operator|==
literal|null
operator|||
operator|!
name|connectionsA
operator|.
name|successors
argument_list|()
operator|.
name|contains
argument_list|(
name|nodeB
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|NodeAdjacencies
argument_list|<
name|N
argument_list|>
name|connectionsB
init|=
name|nodeConnections
operator|.
name|get
argument_list|(
name|nodeB
argument_list|)
decl_stmt|;
name|connectionsA
operator|.
name|removeSuccessor
argument_list|(
name|nodeB
argument_list|)
expr_stmt|;
name|connectionsB
operator|.
name|removePredecessor
argument_list|(
name|nodeA
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|newNodeConnections ()
specifier|private
name|NodeAdjacencies
argument_list|<
name|N
argument_list|>
name|newNodeConnections
parameter_list|()
block|{
return|return
name|isDirected
argument_list|()
condition|?
name|DirectedNodeAdjacencies
operator|.
expr|<
name|N
operator|>
name|of
argument_list|()
else|:
name|UndirectedNodeAdjacencies
operator|.
expr|<
name|N
operator|>
name|of
argument_list|()
return|;
block|}
block|}
end_class

end_unit

