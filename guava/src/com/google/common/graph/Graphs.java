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
name|collect
operator|.
name|AbstractIterator
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
name|Sets
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
name|primitives
operator|.
name|Ints
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Static utility methods for {@link Graph} instances.  *  * @author James Sexton  * @author Joshua O'Madadhain  * @since 20.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|Graphs
specifier|public
specifier|final
class|class
name|Graphs
block|{
DECL|method|Graphs ()
specifier|private
name|Graphs
parameter_list|()
block|{}
comment|// Graph query methods
DECL|method|endpointsInternal (final Graph<N> graph)
specifier|static
parameter_list|<
name|N
parameter_list|>
name|Set
argument_list|<
name|Endpoints
argument_list|<
name|N
argument_list|>
argument_list|>
name|endpointsInternal
parameter_list|(
specifier|final
name|Graph
argument_list|<
name|N
argument_list|>
name|graph
parameter_list|)
block|{
return|return
operator|new
name|AbstractSet
argument_list|<
name|Endpoints
argument_list|<
name|N
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|Endpoints
argument_list|<
name|N
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|graph
operator|.
name|isDirected
argument_list|()
condition|?
operator|new
name|DirectedEndpointsIterator
argument_list|<
name|N
argument_list|>
argument_list|(
name|graph
argument_list|)
else|:
operator|new
name|UndirectedEndpointsIterator
argument_list|<
name|N
argument_list|>
argument_list|(
name|graph
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
name|boolean
name|directed
init|=
name|graph
operator|.
name|isDirected
argument_list|()
decl_stmt|;
name|long
name|endpointsCount
init|=
literal|0L
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
name|Set
argument_list|<
name|N
argument_list|>
name|successors
init|=
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|endpointsCount
operator|+=
name|successors
operator|.
name|size
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|directed
operator|&&
name|successors
operator|.
name|contains
argument_list|(
name|node
argument_list|)
condition|)
block|{
name|endpointsCount
operator|++
expr_stmt|;
comment|// count self-loops twice in the undirected case
block|}
block|}
if|if
condition|(
operator|!
name|directed
condition|)
block|{
comment|// In undirected graphs, every pair of adjacent nodes has been counted twice.
name|checkState
argument_list|(
operator|(
name|endpointsCount
operator|&
literal|1
operator|)
operator|==
literal|0
argument_list|)
expr_stmt|;
name|endpointsCount
operator|>>>=
literal|1
expr_stmt|;
block|}
return|return
name|Ints
operator|.
name|saturatedCast
argument_list|(
name|endpointsCount
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|Endpoints
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|containsEndpoints
argument_list|(
name|graph
argument_list|,
operator|(
name|Endpoints
argument_list|<
name|?
argument_list|>
operator|)
name|obj
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|containsEndpoints (Graph<?> graph, Endpoints<?> endpoints)
specifier|private
specifier|static
name|boolean
name|containsEndpoints
parameter_list|(
name|Graph
argument_list|<
name|?
argument_list|>
name|graph
parameter_list|,
name|Endpoints
argument_list|<
name|?
argument_list|>
name|endpoints
parameter_list|)
block|{
return|return
name|graph
operator|.
name|isDirected
argument_list|()
operator|==
operator|(
name|endpoints
operator|instanceof
name|Endpoints
operator|.
name|Directed
operator|)
operator|&&
name|graph
operator|.
name|nodes
argument_list|()
operator|.
name|contains
argument_list|(
name|endpoints
operator|.
name|nodeA
argument_list|()
argument_list|)
operator|&&
name|graph
operator|.
name|successors
argument_list|(
name|endpoints
operator|.
name|nodeA
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|endpoints
operator|.
name|nodeB
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns an unmodifiable view of edges that are parallel to {@code edge}, i.e. the set of edges    * that connect the same nodes in the same direction (if any). An edge is not parallel to itself.    *    * @throws IllegalArgumentException if {@code edge} is not present in {@code graph}    */
DECL|method|parallelEdges (Network<N, E> graph, Object edge)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|parallelEdges
parameter_list|(
name|Network
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|graph
parameter_list|,
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
name|graph
operator|.
name|incidentNodes
argument_list|(
name|edge
argument_list|)
decl_stmt|;
comment|// Verifies that edge is in graph
if|if
condition|(
operator|!
name|graph
operator|.
name|allowsParallelEdges
argument_list|()
condition|)
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|()
return|;
block|}
return|return
name|Sets
operator|.
name|difference
argument_list|(
name|graph
operator|.
name|edgesConnecting
argument_list|(
name|endpoints
operator|.
name|nodeA
argument_list|()
argument_list|,
name|endpoints
operator|.
name|nodeB
argument_list|()
argument_list|)
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
name|edge
argument_list|)
argument_list|)
return|;
comment|// An edge is not parallel to itself.
block|}
comment|// Graph mutation methods
comment|// Graph transformation methods
comment|/**    * Returns an induced subgraph of {@code graph}. This subgraph is a new graph that contains    * all of the nodes in {@code nodes}, and all of the edges from {@code graph} for which the    * edge's incident nodes are both contained by {@code nodes}.    *    * @throws IllegalArgumentException if any element in {@code nodes} is not a node in the graph    */
DECL|method|inducedSubgraph (Network<N, E> graph, Iterable<? extends N> nodes)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|MutableNetwork
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|inducedSubgraph
parameter_list|(
name|Network
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|graph
parameter_list|,
name|Iterable
argument_list|<
name|?
extends|extends
name|N
argument_list|>
name|nodes
parameter_list|)
block|{
name|NetworkBuilder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|builder
init|=
name|NetworkBuilder
operator|.
name|from
argument_list|(
name|graph
argument_list|)
decl_stmt|;
if|if
condition|(
name|nodes
operator|instanceof
name|Collection
condition|)
block|{
name|builder
operator|=
name|builder
operator|.
name|expectedNodeCount
argument_list|(
operator|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|nodes
operator|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|MutableNetwork
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|subgraph
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
for|for
control|(
name|N
name|node
range|:
name|nodes
control|)
block|{
name|subgraph
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|N
name|node
range|:
name|subgraph
operator|.
name|nodes
argument_list|()
control|)
block|{
for|for
control|(
name|E
name|edge
range|:
name|graph
operator|.
name|outEdges
argument_list|(
name|node
argument_list|)
control|)
block|{
name|N
name|adjacentNode
init|=
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
decl_stmt|;
if|if
condition|(
name|subgraph
operator|.
name|nodes
argument_list|()
operator|.
name|contains
argument_list|(
name|adjacentNode
argument_list|)
condition|)
block|{
name|subgraph
operator|.
name|addEdge
argument_list|(
name|edge
argument_list|,
name|node
argument_list|,
name|adjacentNode
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|subgraph
return|;
block|}
comment|/**    * Creates a mutable copy of {@code graph}, using the same nodes and edges.    */
DECL|method|copyOf (Graph<N> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|>
name|MutableGraph
argument_list|<
name|N
argument_list|>
name|copyOf
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|>
name|graph
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|graph
argument_list|,
literal|"graph"
argument_list|)
expr_stmt|;
name|MutableGraph
argument_list|<
name|N
argument_list|>
name|copy
init|=
name|GraphBuilder
operator|.
name|from
argument_list|(
name|graph
argument_list|)
operator|.
name|expectedNodeCount
argument_list|(
name|graph
operator|.
name|nodes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|build
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
name|checkState
argument_list|(
name|copy
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Endpoints
argument_list|<
name|N
argument_list|>
name|endpoints
range|:
name|endpointsInternal
argument_list|(
name|graph
argument_list|)
control|)
block|{
name|checkState
argument_list|(
name|copy
operator|.
name|addEdge
argument_list|(
name|endpoints
operator|.
name|nodeA
argument_list|()
argument_list|,
name|endpoints
operator|.
name|nodeB
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|copy
return|;
block|}
comment|/**    * Creates a mutable copy of {@code graph}, using the same node and edge elements.    */
DECL|method|copyOf (Network<N, E> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|MutableNetwork
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
name|checkNotNull
argument_list|(
name|graph
argument_list|,
literal|"graph"
argument_list|)
expr_stmt|;
name|MutableNetwork
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copy
init|=
name|NetworkBuilder
operator|.
name|from
argument_list|(
name|graph
argument_list|)
operator|.
name|expectedNodeCount
argument_list|(
name|graph
operator|.
name|nodes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|expectedEdgeCount
argument_list|(
name|graph
operator|.
name|edges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|build
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
name|checkState
argument_list|(
name|copy
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|Endpoints
argument_list|<
name|N
argument_list|>
name|endpoints
init|=
name|graph
operator|.
name|incidentNodes
argument_list|(
name|edge
argument_list|)
decl_stmt|;
name|checkState
argument_list|(
name|copy
operator|.
name|addEdge
argument_list|(
name|edge
argument_list|,
name|endpoints
operator|.
name|nodeA
argument_list|()
argument_list|,
name|endpoints
operator|.
name|nodeB
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|copy
return|;
block|}
DECL|class|AbstractEndpointsIterator
specifier|private
specifier|abstract
specifier|static
class|class
name|AbstractEndpointsIterator
parameter_list|<
name|N
parameter_list|>
extends|extends
name|AbstractIterator
argument_list|<
name|Endpoints
argument_list|<
name|N
argument_list|>
argument_list|>
block|{
DECL|field|graph
specifier|private
specifier|final
name|Graph
argument_list|<
name|N
argument_list|>
name|graph
decl_stmt|;
DECL|field|nodeIterator
specifier|private
specifier|final
name|Iterator
argument_list|<
name|N
argument_list|>
name|nodeIterator
decl_stmt|;
DECL|field|node
name|N
name|node
init|=
literal|null
decl_stmt|;
comment|// null is safe as an initial value because graphs do not allow null nodes
DECL|field|successorIterator
name|Iterator
argument_list|<
name|N
argument_list|>
name|successorIterator
init|=
name|ImmutableSet
operator|.
expr|<
name|N
operator|>
name|of
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
DECL|method|AbstractEndpointsIterator (Graph<N> graph)
name|AbstractEndpointsIterator
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|>
name|graph
parameter_list|)
block|{
name|this
operator|.
name|graph
operator|=
name|graph
expr_stmt|;
name|this
operator|.
name|nodeIterator
operator|=
name|graph
operator|.
name|nodes
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
block|}
comment|/**      * Called after {@link #successorIterator} is exhausted. Advances {@link #node} to the next node      * and updates {@link #successorIterator} to iterate through the successors of {@link #node}.      */
DECL|method|advance ()
specifier|final
name|boolean
name|advance
parameter_list|()
block|{
name|checkState
argument_list|(
operator|!
name|successorIterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|nodeIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|node
operator|=
name|nodeIterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|successorIterator
operator|=
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
operator|.
name|iterator
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
comment|/**    * If the graph is directed, each ordered [source, target] pair will be visited once if there is    * one or more edge connecting them.    */
DECL|class|DirectedEndpointsIterator
specifier|private
specifier|static
specifier|final
class|class
name|DirectedEndpointsIterator
parameter_list|<
name|N
parameter_list|>
extends|extends
name|AbstractEndpointsIterator
argument_list|<
name|N
argument_list|>
block|{
DECL|method|DirectedEndpointsIterator (Graph<N> graph)
name|DirectedEndpointsIterator
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|>
name|graph
parameter_list|)
block|{
name|super
argument_list|(
name|graph
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|computeNext ()
specifier|protected
name|Endpoints
argument_list|<
name|N
argument_list|>
name|computeNext
parameter_list|()
block|{
while|while
condition|(
literal|true
condition|)
block|{
if|if
condition|(
name|successorIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
name|Endpoints
operator|.
name|ofDirected
argument_list|(
name|node
argument_list|,
name|successorIterator
operator|.
name|next
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
operator|!
name|advance
argument_list|()
condition|)
block|{
return|return
name|endOfData
argument_list|()
return|;
block|}
block|}
block|}
block|}
comment|/**    * If the graph is undirected, each unordered [node, otherNode] pair (except self-loops) will be    * visited twice if there is one or more edge connecting them. To avoid returning duplicate    * {@link Endpoints}, we keep track of the nodes that we have visited. When processing node pairs,    * we skip if the "other node" is in the visited set, as shown below:    *    * Nodes = {N1, N2, N3, N4}    *    N2           __    *   /  \         |  |    * N1----N3      N4__|    *    * Visited Nodes = {}    * Endpoints [N1, N2] - return    * Endpoints [N1, N3] - return    * Visited Nodes = {N1}    * Endpoints [N2, N1] - skip    * Endpoints [N2, N3] - return    * Visited Nodes = {N1, N2}    * Endpoints [N3, N1] - skip    * Endpoints [N3, N2] - skip    * Visited Nodes = {N1, N2, N3}    * Endpoints [N4, N4] - return    * Visited Nodes = {N1, N2, N3, N4}    */
DECL|class|UndirectedEndpointsIterator
specifier|private
specifier|static
specifier|final
class|class
name|UndirectedEndpointsIterator
parameter_list|<
name|N
parameter_list|>
extends|extends
name|AbstractEndpointsIterator
argument_list|<
name|N
argument_list|>
block|{
DECL|field|visitedNodes
specifier|private
name|Set
argument_list|<
name|N
argument_list|>
name|visitedNodes
decl_stmt|;
DECL|method|UndirectedEndpointsIterator (Graph<N> graph)
name|UndirectedEndpointsIterator
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|>
name|graph
parameter_list|)
block|{
name|super
argument_list|(
name|graph
argument_list|)
expr_stmt|;
name|this
operator|.
name|visitedNodes
operator|=
name|Sets
operator|.
name|newHashSetWithExpectedSize
argument_list|(
name|graph
operator|.
name|nodes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|computeNext ()
specifier|protected
name|Endpoints
argument_list|<
name|N
argument_list|>
name|computeNext
parameter_list|()
block|{
while|while
condition|(
literal|true
condition|)
block|{
while|while
condition|(
name|successorIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|N
name|otherNode
init|=
name|successorIterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|visitedNodes
operator|.
name|contains
argument_list|(
name|otherNode
argument_list|)
condition|)
block|{
return|return
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
name|node
argument_list|,
name|otherNode
argument_list|)
return|;
block|}
block|}
comment|// Add to visited set *after* processing neighbors so we still include self-loops.
name|visitedNodes
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|advance
argument_list|()
condition|)
block|{
name|visitedNodes
operator|=
literal|null
expr_stmt|;
return|return
name|endOfData
argument_list|()
return|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

