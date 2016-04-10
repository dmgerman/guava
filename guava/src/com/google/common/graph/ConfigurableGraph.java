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
comment|/**  * Configurable implementation of {@link Graph} that supports both directed and undirected graphs.  * Instances of this class should be constructed with {@link GraphBuilder}.  *  *<p>Time complexities for mutation methods are all O(1) except for {@code removeNode(N node)},  * which is in O(d_node) where d_node is the degree of {@code node}.  *  * @author James Sexton  * @author Joshua O'Madadhain  * @author Omar Darwish  * @param<N> Node parameter type  */
end_comment

begin_comment
comment|// TODO(b/24620028): Enable this class to support sorted nodes/edges.
end_comment

begin_class
DECL|class|ConfigurableGraph
class|class
name|ConfigurableGraph
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
DECL|method|ConfigurableGraph (GraphBuilder<? super N> builder)
name|ConfigurableGraph
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
comment|/**    * Constructs a graph with the properties specified in {@code builder}, initialized with    * the given node maps. May be used for either mutable or immutable graphs.    */
DECL|method|ConfigurableGraph (GraphBuilder<? super N> builder, Map<N, NodeAdjacencies<N>> nodeConnections)
name|ConfigurableGraph
parameter_list|(
name|GraphBuilder
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
name|NodeAdjacencies
argument_list|<
name|N
argument_list|>
argument_list|>
name|nodeConnections
parameter_list|)
block|{
name|super
argument_list|(
name|builder
argument_list|,
name|nodeConnections
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
name|nodes
argument_list|()
operator|.
name|contains
argument_list|(
name|node
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|nodeConnections
operator|.
name|put
argument_list|(
name|node
argument_list|,
name|newNodeConnections
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/**    * Add an edge between {@code node1} and {@code node2}; if these nodes are not already    * present in this graph, then add them.    * Return {@code false} if an edge already exists between {@code node1} and {@code node2},    * and in the same direction.    *    * @throws IllegalArgumentException if self-loops are not allowed, and {@code node1} is equal to    *     {@code node2}.    */
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
DECL|method|addEdge (N node1, N node2)
specifier|public
name|boolean
name|addEdge
parameter_list|(
name|N
name|node1
parameter_list|,
name|N
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
name|checkArgument
argument_list|(
name|allowsSelfLoops
argument_list|()
operator|||
operator|!
name|node1
operator|.
name|equals
argument_list|(
name|node2
argument_list|)
argument_list|,
name|SELF_LOOPS_NOT_ALLOWED
argument_list|,
name|node1
argument_list|)
expr_stmt|;
name|boolean
name|containsN1
init|=
name|nodes
argument_list|()
operator|.
name|contains
argument_list|(
name|node1
argument_list|)
decl_stmt|;
name|boolean
name|containsN2
init|=
name|nodes
argument_list|()
operator|.
name|contains
argument_list|(
name|node2
argument_list|)
decl_stmt|;
comment|// TODO(user): does not support parallel edges
if|if
condition|(
name|containsN1
operator|&&
name|containsN2
operator|&&
name|nodeConnections
operator|.
name|get
argument_list|(
name|node1
argument_list|)
operator|.
name|successors
argument_list|()
operator|.
name|contains
argument_list|(
name|node2
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|containsN1
condition|)
block|{
name|addNode
argument_list|(
name|node1
argument_list|)
expr_stmt|;
block|}
name|NodeAdjacencies
argument_list|<
name|N
argument_list|>
name|connectionsN1
init|=
name|nodeConnections
operator|.
name|get
argument_list|(
name|node1
argument_list|)
decl_stmt|;
name|connectionsN1
operator|.
name|addSuccessor
argument_list|(
name|node2
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|containsN2
condition|)
block|{
name|addNode
argument_list|(
name|node2
argument_list|)
expr_stmt|;
block|}
name|NodeAdjacencies
argument_list|<
name|N
argument_list|>
name|connectionsN2
init|=
name|nodeConnections
operator|.
name|get
argument_list|(
name|node2
argument_list|)
decl_stmt|;
name|connectionsN2
operator|.
name|addPredecessor
argument_list|(
name|node1
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
if|if
condition|(
operator|!
name|nodes
argument_list|()
operator|.
name|contains
argument_list|(
name|node
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|N
name|successor
range|:
name|nodeConnections
operator|.
name|get
argument_list|(
name|node
argument_list|)
operator|.
name|successors
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|node
operator|.
name|equals
argument_list|(
name|successor
argument_list|)
condition|)
block|{
comment|// don't remove the successor if it's the input node (=> CME); will be removed below
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
block|}
for|for
control|(
name|N
name|predecessor
range|:
name|nodeConnections
operator|.
name|get
argument_list|(
name|node
argument_list|)
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
DECL|method|removeEdge (Object node1, Object node2)
specifier|public
name|boolean
name|removeEdge
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
name|NodeAdjacencies
argument_list|<
name|N
argument_list|>
name|connectionsN1
init|=
name|nodeConnections
operator|.
name|get
argument_list|(
name|node1
argument_list|)
decl_stmt|;
name|NodeAdjacencies
argument_list|<
name|N
argument_list|>
name|connectionsN2
init|=
name|nodeConnections
operator|.
name|get
argument_list|(
name|node2
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectionsN1
operator|==
literal|null
operator|||
name|connectionsN2
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|result
init|=
name|connectionsN1
operator|.
name|removeSuccessor
argument_list|(
name|node2
argument_list|)
decl_stmt|;
name|connectionsN2
operator|.
name|removePredecessor
argument_list|(
name|node1
argument_list|)
expr_stmt|;
return|return
name|result
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

