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
name|base
operator|.
name|Objects
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
name|ArrayDeque
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
name|HashSet
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
name|Queue
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
comment|/**    * Returns true iff {@code graph} has at least one cycle. A cycle is defined as a non-empty    * subset of edges in a graph arranged to form a path (a sequence of adjacent outgoing edges)    * starting and ending with the same node.    *    *<p>This method will detect any non-empty cycle, including self-loops (a cycle of length 1).    */
DECL|method|hasCycle (Graph<?> graph)
specifier|public
specifier|static
name|boolean
name|hasCycle
parameter_list|(
name|Graph
argument_list|<
name|?
argument_list|>
name|graph
parameter_list|)
block|{
name|int
name|numEdges
init|=
name|graph
operator|.
name|edges
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|numEdges
operator|==
literal|0
condition|)
block|{
return|return
literal|false
return|;
comment|// An edge-free graph is acyclic by definition.
block|}
if|if
condition|(
operator|!
name|graph
operator|.
name|isDirected
argument_list|()
operator|&&
name|numEdges
operator|>=
name|graph
operator|.
name|nodes
argument_list|()
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
comment|// Optimization for the undirected case: at least one cycle must exist.
block|}
name|Map
argument_list|<
name|Object
argument_list|,
name|NodeVisitState
argument_list|>
name|visitedNodes
init|=
name|Maps
operator|.
name|newHashMapWithExpectedSize
argument_list|(
name|graph
operator|.
name|nodes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|node
range|:
name|graph
operator|.
name|nodes
argument_list|()
control|)
block|{
if|if
condition|(
name|subgraphHasCycle
argument_list|(
name|graph
argument_list|,
name|visitedNodes
argument_list|,
name|node
argument_list|,
literal|null
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**    * Returns true iff {@code network} has at least one cycle. A cycle is defined as a non-empty    * subset of edges in a graph arranged to form a path (a sequence of adjacent outgoing edges)    * starting and ending with the same node.    *    *<p>This method will detect any non-empty cycle, including self-loops (a cycle of length 1).    */
DECL|method|hasCycle (Network<?, ?> network)
specifier|public
specifier|static
name|boolean
name|hasCycle
parameter_list|(
name|Network
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|network
parameter_list|)
block|{
comment|// In a directed graph, parallel edges cannot introduce a cycle in an acyclic graph.
comment|// However, in an undirected graph, any parallel edge induces a cycle in the graph.
if|if
condition|(
operator|!
name|network
operator|.
name|isDirected
argument_list|()
operator|&&
name|network
operator|.
name|allowsParallelEdges
argument_list|()
operator|&&
name|network
operator|.
name|edges
argument_list|()
operator|.
name|size
argument_list|()
operator|>
name|network
operator|.
name|asGraph
argument_list|()
operator|.
name|edges
argument_list|()
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|hasCycle
argument_list|(
name|network
operator|.
name|asGraph
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Performs a traversal of the nodes reachable from {@code node}. If we ever reach a node we've    * already visited (following only outgoing edges and without reusing edges), we know there's a    * cycle in the graph.    */
DECL|method|subgraphHasCycle ( Graph<?> graph, Map<Object, NodeVisitState> visitedNodes, Object node, @Nullable Object previousNode)
specifier|private
specifier|static
name|boolean
name|subgraphHasCycle
parameter_list|(
name|Graph
argument_list|<
name|?
argument_list|>
name|graph
parameter_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|NodeVisitState
argument_list|>
name|visitedNodes
parameter_list|,
name|Object
name|node
parameter_list|,
annotation|@
name|Nullable
name|Object
name|previousNode
parameter_list|)
block|{
name|NodeVisitState
name|state
init|=
name|visitedNodes
operator|.
name|get
argument_list|(
name|node
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|==
name|NodeVisitState
operator|.
name|COMPLETE
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|state
operator|==
name|NodeVisitState
operator|.
name|PENDING
condition|)
block|{
return|return
literal|true
return|;
block|}
name|visitedNodes
operator|.
name|put
argument_list|(
name|node
argument_list|,
name|NodeVisitState
operator|.
name|PENDING
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|nextNode
range|:
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
control|)
block|{
if|if
condition|(
name|canTraverseWithoutReusingEdge
argument_list|(
name|graph
argument_list|,
name|nextNode
argument_list|,
name|previousNode
argument_list|)
operator|&&
name|subgraphHasCycle
argument_list|(
name|graph
argument_list|,
name|visitedNodes
argument_list|,
name|nextNode
argument_list|,
name|node
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
name|visitedNodes
operator|.
name|put
argument_list|(
name|node
argument_list|,
name|NodeVisitState
operator|.
name|COMPLETE
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|/**    * Determines whether an edge has already been used during traversal. In the directed case a cycle    * is always detected before reusing an edge, so no special logic is required. In the undirected    * case, we must take care not to "backtrack" over an edge (i.e. going from A to B and then going    * from B to A).    */
DECL|method|canTraverseWithoutReusingEdge ( Graph<?> graph, Object nextNode, @Nullable Object previousNode)
specifier|private
specifier|static
name|boolean
name|canTraverseWithoutReusingEdge
parameter_list|(
name|Graph
argument_list|<
name|?
argument_list|>
name|graph
parameter_list|,
name|Object
name|nextNode
parameter_list|,
annotation|@
name|Nullable
name|Object
name|previousNode
parameter_list|)
block|{
if|if
condition|(
name|graph
operator|.
name|isDirected
argument_list|()
operator|||
operator|!
name|Objects
operator|.
name|equal
argument_list|(
name|previousNode
argument_list|,
name|nextNode
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// This falls into the undirected A->B->A case. The Graph interface does not support parallel
comment|// edges, so this traversal would require reusing the undirected AB edge.
return|return
literal|false
return|;
block|}
comment|/**    * Returns the transitive closure of {@code graph}. The transitive closure of a graph is another    * graph with an edge connecting node A to node B iff node B is {@link #reachableNodes(Graph,    * Object) reachable} from node A.    *    *<p>This is a "snapshot" based on the current topology of {@code graph}, rather than a live    * view of the transitive closure of {@code graph}. In other words, the returned {@link Graph}    * will not be updated after modifications to {@code graph}.    */
DECL|method|transitiveClosure (Graph<N> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|>
name|Graph
argument_list|<
name|N
argument_list|>
name|transitiveClosure
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|>
name|graph
parameter_list|)
block|{
name|MutableGraph
argument_list|<
name|N
argument_list|>
name|transitiveClosure
init|=
name|GraphBuilder
operator|.
name|from
argument_list|(
name|graph
argument_list|)
operator|.
name|allowsSelfLoops
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
comment|// Every node is, at a minimum, reachable from itself. Since the resulting transitive closure
comment|// will have no isolated nodes, we can skip adding nodes explicitly and let putEdge() do it.
if|if
condition|(
name|graph
operator|.
name|isDirected
argument_list|()
condition|)
block|{
comment|// Note: works for both directed and undirected graphs, but we only use in the directed case.
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
for|for
control|(
name|N
name|reachableNode
range|:
name|reachableNodes
argument_list|(
name|graph
argument_list|,
name|node
argument_list|)
control|)
block|{
name|transitiveClosure
operator|.
name|putEdge
argument_list|(
name|node
argument_list|,
name|reachableNode
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// An optimization for the undirected case: for every node B reachable from node A,
comment|// node A and node B have the same reachability set.
name|Set
argument_list|<
name|N
argument_list|>
name|visitedNodes
init|=
operator|new
name|HashSet
argument_list|<
name|N
argument_list|>
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
if|if
condition|(
operator|!
name|visitedNodes
operator|.
name|contains
argument_list|(
name|node
argument_list|)
condition|)
block|{
name|ImmutableList
argument_list|<
name|N
argument_list|>
name|reachableNodes
init|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|reachableNodes
argument_list|(
name|graph
argument_list|,
name|node
argument_list|)
argument_list|)
decl_stmt|;
name|visitedNodes
operator|.
name|addAll
argument_list|(
name|reachableNodes
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|a
init|=
literal|0
init|;
name|a
operator|<
name|reachableNodes
operator|.
name|size
argument_list|()
condition|;
operator|++
name|a
control|)
block|{
name|N
name|nodeA
init|=
name|reachableNodes
operator|.
name|get
argument_list|(
name|a
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|b
init|=
name|a
init|;
name|b
operator|<
name|reachableNodes
operator|.
name|size
argument_list|()
condition|;
operator|++
name|b
control|)
block|{
name|N
name|nodeB
init|=
name|reachableNodes
operator|.
name|get
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|transitiveClosure
operator|.
name|putEdge
argument_list|(
name|nodeA
argument_list|,
name|nodeB
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|transitiveClosure
return|;
block|}
comment|/**    * Returns the set of nodes that are reachable from {@code node}. Node B is defined as reachable    * from node A if there exists a path (a sequence of adjacent outgoing edges) starting at node A    * and ending at node B. Note that a node is always reachable from itself via a zero-length path.    *    *<p>This is a "snapshot" based on the current topology of {@code graph}, rather than a live    * view of the set of nodes reachable from {@code node}. In other words, the returned {@link Set}    * will not be updated after modifications to {@code graph}.    *    * @throws IllegalArgumentException if {@code node} is not present in {@code graph}    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// Throws an exception if node is not an element of graph.
DECL|method|reachableNodes (Graph<N> graph, Object node)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|>
name|Set
argument_list|<
name|N
argument_list|>
name|reachableNodes
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|>
name|graph
parameter_list|,
name|Object
name|node
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|graph
operator|.
name|nodes
argument_list|()
operator|.
name|contains
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|N
argument_list|>
name|visitedNodes
init|=
operator|new
name|HashSet
argument_list|<
name|N
argument_list|>
argument_list|()
decl_stmt|;
name|Queue
argument_list|<
name|N
argument_list|>
name|queuedNodes
init|=
operator|new
name|ArrayDeque
argument_list|<
name|N
argument_list|>
argument_list|()
decl_stmt|;
name|visitedNodes
operator|.
name|add
argument_list|(
operator|(
name|N
operator|)
name|node
argument_list|)
expr_stmt|;
name|queuedNodes
operator|.
name|add
argument_list|(
operator|(
name|N
operator|)
name|node
argument_list|)
expr_stmt|;
comment|// Perform a breadth-first traversal rooted at the input node.
while|while
condition|(
operator|!
name|queuedNodes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|N
name|currentNode
init|=
name|queuedNodes
operator|.
name|remove
argument_list|()
decl_stmt|;
for|for
control|(
name|N
name|successor
range|:
name|graph
operator|.
name|successors
argument_list|(
name|currentNode
argument_list|)
control|)
block|{
if|if
condition|(
name|visitedNodes
operator|.
name|add
argument_list|(
name|successor
argument_list|)
condition|)
block|{
name|queuedNodes
operator|.
name|add
argument_list|(
name|successor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|visitedNodes
argument_list|)
return|;
block|}
comment|/**    * Returns an unmodifiable view of edges that are parallel to {@code edge}, i.e. the set of edges    * that connect the same nodes in the same direction (if any). An edge is not parallel to itself.    *    * @throws IllegalArgumentException if {@code edge} is not present in {@code graph}    */
DECL|method|parallelEdges (Network<?, E> graph, Object edge)
specifier|public
specifier|static
parameter_list|<
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
name|?
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
name|?
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
comment|/**    * Returns an unmodifiable view of the edges which have an {@link Network#incidentNodes(Object)    * incident node} in common with {@code edge}. An edge is not considered adjacent to itself.    *    * @throws IllegalArgumentException if {@code edge} is not present in {@code graph}    */
DECL|method|adjacentEdges (Network<?, E> graph, Object edge)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|adjacentEdges
parameter_list|(
name|Network
argument_list|<
name|?
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
name|?
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
name|graph
operator|.
name|incidentEdges
argument_list|(
name|endpoints
operator|.
name|nodeA
argument_list|()
argument_list|)
argument_list|,
name|graph
operator|.
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
comment|// Graph mutation methods
comment|// Graph transformation methods
comment|/**    * Returns an induced subgraph of {@code graph}. This subgraph is a new graph that contains    * all of the nodes in {@code nodes}, and all of the {@link Graph#edges() edges} from {@code    * graph} for which the endpoints are both contained by {@code nodes}.    *    * @throws IllegalArgumentException if any element in {@code nodes} is not a node in the graph    */
DECL|method|inducedSubgraph (Graph<N> graph, Iterable<? extends N> nodes)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|>
name|MutableGraph
argument_list|<
name|N
argument_list|>
name|inducedSubgraph
parameter_list|(
name|Graph
argument_list|<
name|N
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
name|MutableGraph
argument_list|<
name|N
argument_list|>
name|subgraph
init|=
name|GraphBuilder
operator|.
name|from
argument_list|(
name|graph
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
name|N
name|successorNode
range|:
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
control|)
block|{
if|if
condition|(
name|subgraph
operator|.
name|nodes
argument_list|()
operator|.
name|contains
argument_list|(
name|successorNode
argument_list|)
condition|)
block|{
name|subgraph
operator|.
name|putEdge
argument_list|(
name|node
argument_list|,
name|successorNode
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|subgraph
return|;
block|}
comment|/**    * Returns an induced subgraph of {@code graph}. This subgraph is a new graph that contains    * all of the nodes in {@code nodes}, and all of the {@link Graph#edges() edges} (and associated    * edge values) from {@code graph} for which the endpoints are both contained by {@code nodes}.    *    * @throws IllegalArgumentException if any element in {@code nodes} is not a node in the graph    */
DECL|method|inducedSubgraph (ValueGraph<N, V> graph, Iterable<? extends N> nodes)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
name|MutableValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|inducedSubgraph
parameter_list|(
name|ValueGraph
argument_list|<
name|N
argument_list|,
name|V
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
name|MutableValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|subgraph
init|=
name|ValueGraphBuilder
operator|.
name|from
argument_list|(
name|graph
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
name|N
name|successorNode
range|:
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
control|)
block|{
if|if
condition|(
name|subgraph
operator|.
name|nodes
argument_list|()
operator|.
name|contains
argument_list|(
name|successorNode
argument_list|)
condition|)
block|{
name|subgraph
operator|.
name|putEdgeValue
argument_list|(
name|node
argument_list|,
name|successorNode
argument_list|,
name|graph
operator|.
name|edgeValue
argument_list|(
name|node
argument_list|,
name|successorNode
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|subgraph
return|;
block|}
comment|/**    * Returns an induced subgraph of {@code graph}. This subgraph is a new graph that contains    * all of the nodes in {@code nodes}, and all of the {@link Network#edges() edges} from {@code    * graph} for which the endpoints are both contained by {@code nodes}.    *    * @throws IllegalArgumentException if any element in {@code nodes} is not a node in the graph    */
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
name|MutableNetwork
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|subgraph
init|=
name|NetworkBuilder
operator|.
name|from
argument_list|(
name|graph
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
name|successorNode
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
name|successorNode
argument_list|)
condition|)
block|{
name|subgraph
operator|.
name|addEdge
argument_list|(
name|node
argument_list|,
name|successorNode
argument_list|,
name|edge
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|subgraph
return|;
block|}
comment|/**    * Creates a mutable copy of {@code graph} with the same nodes and edges.    */
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
name|copy
operator|.
name|addNode
argument_list|(
name|node
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
name|graph
operator|.
name|edges
argument_list|()
control|)
block|{
name|copy
operator|.
name|putEdge
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
expr_stmt|;
block|}
return|return
name|copy
return|;
block|}
comment|/**    * Creates a mutable copy of {@code graph} with the same nodes, edges, and edge values.    */
DECL|method|copyOf (ValueGraph<N, V> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
name|MutableValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|copyOf
parameter_list|(
name|ValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|graph
parameter_list|)
block|{
name|MutableValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|copy
init|=
name|ValueGraphBuilder
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
name|copy
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Endpoints
argument_list|<
name|N
argument_list|>
name|edge
range|:
name|graph
operator|.
name|edges
argument_list|()
control|)
block|{
name|copy
operator|.
name|putEdgeValue
argument_list|(
name|edge
operator|.
name|nodeA
argument_list|()
argument_list|,
name|edge
operator|.
name|nodeB
argument_list|()
argument_list|,
name|graph
operator|.
name|edgeValue
argument_list|(
name|edge
operator|.
name|nodeA
argument_list|()
argument_list|,
name|edge
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
comment|/**    * Creates a mutable copy of {@code graph} with the same nodes and edges.    */
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
name|copy
operator|.
name|addNode
argument_list|(
name|node
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
argument_list|,
name|edge
argument_list|)
expr_stmt|;
block|}
return|return
name|copy
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
DECL|method|checkNonNegative (int value)
specifier|static
name|int
name|checkNonNegative
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|checkState
argument_list|(
name|value
operator|>=
literal|0
argument_list|,
literal|"Not true that %s is non-negative."
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
DECL|method|checkPositive (int value)
specifier|static
name|int
name|checkPositive
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|checkState
argument_list|(
name|value
operator|>
literal|0
argument_list|,
literal|"Not true that %s is positive."
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
DECL|method|checkNonNegative (long value)
specifier|static
name|long
name|checkNonNegative
parameter_list|(
name|long
name|value
parameter_list|)
block|{
name|checkState
argument_list|(
name|value
operator|>=
literal|0
argument_list|,
literal|"Not true that %s is non-negative."
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
DECL|method|checkPositive (long value)
specifier|static
name|long
name|checkPositive
parameter_list|(
name|long
name|value
parameter_list|)
block|{
name|checkState
argument_list|(
name|value
operator|>
literal|0
argument_list|,
literal|"Not true that %s is positive."
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
comment|/**    * An enum representing the state of a node during DFS. {@code PENDING} means that    * the node is on the stack of the DFS, while {@code COMPLETE} means that    * the node and all its successors have been already explored. Any node that    * has not been explored will not have a state at all.    */
DECL|enum|NodeVisitState
specifier|private
enum|enum
name|NodeVisitState
block|{
DECL|enumConstant|PENDING
name|PENDING
block|,
DECL|enumConstant|COMPLETE
name|COMPLETE
block|}
block|}
end_class

end_unit

