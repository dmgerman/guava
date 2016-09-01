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
name|checkPositive
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
comment|/**  * Configurable implementation of {@link MutableValueGraph} that supports both directed and  * undirected graphs. Instances of this class should be constructed with {@link ValueGraphBuilder}.  *  *<p>Time complexities for mutation methods are all O(1) except for {@code removeNode(N node)},  * which is in O(d_node) where d_node is the degree of {@code node}.  *  * @author James Sexton  * @author Joshua O'Madadhain  * @author Omar Darwish  * @param<N> Node parameter type  * @param<V> Value parameter type  */
end_comment

begin_class
DECL|class|ConfigurableMutableValueGraph
specifier|final
class|class
name|ConfigurableMutableValueGraph
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ConfigurableValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
implements|implements
name|MutableValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
block|{
comment|/**    * Constructs a mutable graph with the properties specified in {@code builder}.    */
DECL|method|ConfigurableMutableValueGraph (AbstractGraphBuilder<? super N> builder)
name|ConfigurableMutableValueGraph
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
comment|/**    * Adds {@code node} to the graph and returns the associated {@link GraphConnections}.    *    * @throws IllegalStateException if {@code node} is already present    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addNodeInternal (N node)
specifier|private
name|GraphConnections
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|addNodeInternal
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
name|newConnections
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
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
DECL|method|putEdgeValue (N nodeU, N nodeV, V value)
specifier|public
name|V
name|putEdgeValue
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|nodeU
argument_list|,
literal|"nodeU"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|nodeV
argument_list|,
literal|"nodeV"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|value
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
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
name|boolean
name|isSelfLoop
init|=
name|nodeU
operator|.
name|equals
argument_list|(
name|nodeV
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
name|nodeU
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|connectionsU
operator|==
literal|null
condition|)
block|{
name|connectionsU
operator|=
name|addNodeInternal
argument_list|(
name|nodeU
argument_list|)
expr_stmt|;
block|}
name|V
name|previousValue
init|=
name|connectionsU
operator|.
name|addSuccessor
argument_list|(
name|nodeV
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|GraphConnections
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|connectionsV
init|=
name|nodeConnections
operator|.
name|get
argument_list|(
name|nodeV
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectionsV
operator|==
literal|null
condition|)
block|{
name|connectionsV
operator|=
name|addNodeInternal
argument_list|(
name|nodeV
argument_list|)
expr_stmt|;
block|}
name|connectionsV
operator|.
name|addPredecessor
argument_list|(
name|nodeU
argument_list|,
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|previousValue
operator|==
literal|null
condition|)
block|{
name|checkPositive
argument_list|(
operator|++
name|edgeCount
argument_list|)
expr_stmt|;
block|}
return|return
name|previousValue
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
comment|// Remove self-loop (if any) first, so we don't get CME while removing incident edges.
if|if
condition|(
name|connections
operator|.
name|removeSuccessor
argument_list|(
name|node
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|connections
operator|.
name|removePredecessor
argument_list|(
name|node
argument_list|)
expr_stmt|;
operator|--
name|edgeCount
expr_stmt|;
block|}
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
name|getWithoutCaching
argument_list|(
name|successor
argument_list|)
operator|.
name|removePredecessor
argument_list|(
name|node
argument_list|)
expr_stmt|;
operator|--
name|edgeCount
expr_stmt|;
block|}
if|if
condition|(
name|isDirected
argument_list|()
condition|)
block|{
comment|// In undirected graphs, the successor and predecessor sets are equal.
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
name|checkState
argument_list|(
name|nodeConnections
operator|.
name|getWithoutCaching
argument_list|(
name|predecessor
argument_list|)
operator|.
name|removeSuccessor
argument_list|(
name|node
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
operator|--
name|edgeCount
expr_stmt|;
block|}
block|}
name|nodeConnections
operator|.
name|remove
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|checkNonNegative
argument_list|(
name|edgeCount
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
DECL|method|removeEdge (Object nodeU, Object nodeV)
specifier|public
name|V
name|removeEdge
parameter_list|(
name|Object
name|nodeU
parameter_list|,
name|Object
name|nodeV
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|nodeU
argument_list|,
literal|"nodeU"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|nodeV
argument_list|,
literal|"nodeV"
argument_list|)
expr_stmt|;
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
name|GraphConnections
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|connectionsV
init|=
name|nodeConnections
operator|.
name|get
argument_list|(
name|nodeV
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectionsU
operator|==
literal|null
operator|||
name|connectionsV
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|V
name|previousValue
init|=
name|connectionsU
operator|.
name|removeSuccessor
argument_list|(
name|nodeV
argument_list|)
decl_stmt|;
if|if
condition|(
name|previousValue
operator|!=
literal|null
condition|)
block|{
name|connectionsV
operator|.
name|removePredecessor
argument_list|(
name|nodeU
argument_list|)
expr_stmt|;
name|checkNonNegative
argument_list|(
operator|--
name|edgeCount
argument_list|)
expr_stmt|;
block|}
return|return
name|previousValue
return|;
block|}
DECL|method|newConnections ()
specifier|private
name|GraphConnections
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|newConnections
parameter_list|()
block|{
return|return
name|isDirected
argument_list|()
condition|?
name|DirectedGraphConnections
operator|.
expr|<
name|N
operator|,
name|V
operator|>
name|of
argument_list|()
operator|:
name|UndirectedGraphConnections
operator|.
expr|<
name|N
operator|,
name|V
operator|>
name|of
argument_list|()
return|;
block|}
block|}
end_class

end_unit

