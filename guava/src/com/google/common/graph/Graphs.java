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
name|ENDPOINTS_GRAPH_DIRECTEDNESS
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
name|base
operator|.
name|Joiner
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
name|Predicate
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
name|Predicates
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Static utility methods for {@link Graph} instances.  *  * @author Joshua O'Madadhain  * @since 20.0  */
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
DECL|field|GRAPH_FORMAT
specifier|private
specifier|static
specifier|final
name|String
name|GRAPH_FORMAT
init|=
literal|"%s, nodes: %s, edges: %s"
decl_stmt|;
DECL|field|DIRECTED_FORMAT
specifier|private
specifier|static
specifier|final
name|String
name|DIRECTED_FORMAT
init|=
literal|"<%s -> %s>"
decl_stmt|;
DECL|field|UNDIRECTED_FORMAT
specifier|private
specifier|static
specifier|final
name|String
name|UNDIRECTED_FORMAT
init|=
literal|"[%s, %s]"
decl_stmt|;
DECL|method|Graphs ()
specifier|private
name|Graphs
parameter_list|()
block|{}
comment|/**    * Returns the node at the other end of {@code edge} from {@code node}.    *    * @throws IllegalArgumentException if {@code edge} is not incident to {@code node}    */
DECL|method|oppositeNode (Network<N, ?> graph, Object edge, Object node)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|>
name|N
name|oppositeNode
parameter_list|(
name|Network
argument_list|<
name|N
argument_list|,
name|?
argument_list|>
name|graph
parameter_list|,
name|Object
name|edge
parameter_list|,
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
if|if
condition|(
name|node
operator|.
name|equals
argument_list|(
name|endpoints
operator|.
name|nodeA
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|endpoints
operator|.
name|nodeB
argument_list|()
return|;
block|}
else|else
block|{
name|checkArgument
argument_list|(
name|node
operator|.
name|equals
argument_list|(
name|endpoints
operator|.
name|nodeB
argument_list|()
argument_list|)
argument_list|,
literal|"Edge %s is not incident to node %s"
argument_list|,
name|edge
argument_list|,
name|node
argument_list|)
expr_stmt|;
return|return
name|endpoints
operator|.
name|nodeA
argument_list|()
return|;
block|}
block|}
comment|/**    * Returns the subset of nodes in {@code graph} that have no predecessors.    *    *<p>Note that in an undirected graph, this is equivalent to all isolated nodes.    */
DECL|method|roots (final Graph<N> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|>
name|Set
argument_list|<
name|N
argument_list|>
name|roots
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
name|Sets
operator|.
name|filter
argument_list|(
name|graph
operator|.
name|nodes
argument_list|()
argument_list|,
operator|new
name|Predicate
argument_list|<
name|N
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|graph
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
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
comment|/**    * Adds {@code edge} to {@code graph} with the specified {@code endpoints}.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addEdge (MutableNetwork<N, E> graph, E edge, Endpoints<N> endpoints)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|boolean
name|addEdge
parameter_list|(
name|MutableNetwork
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|graph
parameter_list|,
name|E
name|edge
parameter_list|,
name|Endpoints
argument_list|<
name|N
argument_list|>
name|endpoints
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|graph
argument_list|,
literal|"graph"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|edge
argument_list|,
literal|"edge"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|endpoints
argument_list|,
literal|"endpoints"
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|endpoints
operator|.
name|isDirected
argument_list|()
operator|==
name|graph
operator|.
name|isDirected
argument_list|()
argument_list|,
name|ENDPOINTS_GRAPH_DIRECTEDNESS
argument_list|,
name|endpoints
operator|.
name|isDirected
argument_list|()
argument_list|,
name|graph
operator|.
name|isDirected
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|graph
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
return|;
block|}
comment|/**    * Creates a mutable copy of {@code graph}, using the same nodes.    */
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
return|return
name|copyOf
argument_list|(
name|graph
argument_list|,
name|Predicates
operator|.
name|alwaysTrue
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Creates a mutable copy of {@code graph}, using all of its elements that satisfy    * {@code nodePredicate} and {@code edgePredicate}.    */
DECL|method|copyOf (Graph<N> graph, Predicate<? super N> nodePredicate)
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
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|N
argument_list|>
name|nodePredicate
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|graph
argument_list|,
literal|"graph"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|nodePredicate
argument_list|,
literal|"nodePredicate"
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
name|Sets
operator|.
name|filter
argument_list|(
name|graph
operator|.
name|nodes
argument_list|()
argument_list|,
name|nodePredicate
argument_list|)
control|)
block|{
name|copy
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
for|for
control|(
name|N
name|successor
range|:
name|Sets
operator|.
name|filter
argument_list|(
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
argument_list|,
name|nodePredicate
argument_list|)
control|)
block|{
name|copy
operator|.
name|addEdge
argument_list|(
name|node
argument_list|,
name|successor
argument_list|)
expr_stmt|;
block|}
comment|// TODO(b/28087289): update this when parallel edges are permitted to ensure that the correct
comment|// multiplicity is preserved.
block|}
return|return
name|copy
return|;
block|}
comment|/**    * Copies all nodes from {@code original} into {@code copy}.    */
DECL|method|mergeNodesFrom (Graph<N> original, MutableGraph<N> copy)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|>
name|void
name|mergeNodesFrom
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|>
name|original
parameter_list|,
name|MutableGraph
argument_list|<
name|N
argument_list|>
name|copy
parameter_list|)
block|{
name|mergeNodesFrom
argument_list|(
name|original
argument_list|,
name|copy
argument_list|,
name|Predicates
operator|.
name|alwaysTrue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Copies all nodes from {@code original} into {@code copy} that satisfy {@code nodePredicate}.    */
DECL|method|mergeNodesFrom ( Graph<N> original, MutableGraph<N> copy, Predicate<? super N> nodePredicate)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|void
name|mergeNodesFrom
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|>
name|original
parameter_list|,
name|MutableGraph
argument_list|<
name|N
argument_list|>
name|copy
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|N
argument_list|>
name|nodePredicate
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|original
argument_list|,
literal|"original"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|copy
argument_list|,
literal|"copy"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|nodePredicate
argument_list|,
literal|"nodePredicate"
argument_list|)
expr_stmt|;
for|for
control|(
name|N
name|node
range|:
name|Sets
operator|.
name|filter
argument_list|(
name|original
operator|.
name|nodes
argument_list|()
argument_list|,
name|nodePredicate
argument_list|)
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
return|return
name|copyOf
argument_list|(
name|graph
argument_list|,
name|Predicates
operator|.
name|alwaysTrue
argument_list|()
argument_list|,
name|Predicates
operator|.
name|alwaysTrue
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Creates a mutable copy of {@code graph}, using all of its elements that satisfy    * {@code nodePredicate} and {@code edgePredicate}.    */
DECL|method|copyOf ( Network<N, E> graph, Predicate<? super N> nodePredicate, Predicate<? super E> edgePredicate)
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
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|N
argument_list|>
name|nodePredicate
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|E
argument_list|>
name|edgePredicate
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|graph
argument_list|,
literal|"graph"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|nodePredicate
argument_list|,
literal|"nodePredicate"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|edgePredicate
argument_list|,
literal|"edgePredicate"
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
name|mergeNodesFrom
argument_list|(
name|graph
argument_list|,
name|copy
argument_list|,
name|nodePredicate
argument_list|)
expr_stmt|;
comment|// We can't just call mergeEdgesFrom(graph, copy, edgePredicate) because addEdge() can add
comment|// the edge's incident nodes if they are not present.
for|for
control|(
name|E
name|edge
range|:
name|Sets
operator|.
name|filter
argument_list|(
name|graph
operator|.
name|edges
argument_list|()
argument_list|,
name|edgePredicate
argument_list|)
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
if|if
condition|(
name|copy
operator|.
name|nodes
argument_list|()
operator|.
name|containsAll
argument_list|(
name|endpoints
argument_list|)
condition|)
block|{
name|addEdge
argument_list|(
name|copy
argument_list|,
name|edge
argument_list|,
name|endpoints
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|copy
return|;
block|}
comment|/**    * Copies all nodes from {@code original} into {@code copy}.    */
DECL|method|mergeNodesFrom (Graph<N> original, MutableNetwork<N, ?> copy)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|>
name|void
name|mergeNodesFrom
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|>
name|original
parameter_list|,
name|MutableNetwork
argument_list|<
name|N
argument_list|,
name|?
argument_list|>
name|copy
parameter_list|)
block|{
name|mergeNodesFrom
argument_list|(
name|original
argument_list|,
name|copy
argument_list|,
name|Predicates
operator|.
name|alwaysTrue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Copies all nodes from {@code original} into {@code copy} that satisfy {@code nodePredicate}.    */
DECL|method|mergeNodesFrom ( Graph<N> original, MutableNetwork<N, ?> copy, Predicate<? super N> nodePredicate)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|void
name|mergeNodesFrom
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|>
name|original
parameter_list|,
name|MutableNetwork
argument_list|<
name|N
argument_list|,
name|?
argument_list|>
name|copy
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|N
argument_list|>
name|nodePredicate
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|original
argument_list|,
literal|"original"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|copy
argument_list|,
literal|"copy"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|nodePredicate
argument_list|,
literal|"nodePredicate"
argument_list|)
expr_stmt|;
for|for
control|(
name|N
name|node
range|:
name|Sets
operator|.
name|filter
argument_list|(
name|original
operator|.
name|nodes
argument_list|()
argument_list|,
name|nodePredicate
argument_list|)
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
block|}
comment|/**    * Copies all edges from {@code original} into {@code copy}. Also copies all nodes incident    * to these edges.    */
DECL|method|mergeEdgesFrom (Network<N, E> original, MutableNetwork<N, E> copy)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|void
name|mergeEdgesFrom
parameter_list|(
name|Network
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|original
parameter_list|,
name|MutableNetwork
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copy
parameter_list|)
block|{
name|mergeEdgesFrom
argument_list|(
name|original
argument_list|,
name|copy
argument_list|,
name|Predicates
operator|.
name|alwaysTrue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Copies all edges from {@code original} into {@code copy} that satisfy {@code edgePredicate}.    * Also copies all nodes incident to these edges.    */
DECL|method|mergeEdgesFrom ( Network<N, E> original, MutableNetwork<N, E> copy, Predicate<? super E> edgePredicate)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|void
name|mergeEdgesFrom
parameter_list|(
name|Network
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|original
parameter_list|,
name|MutableNetwork
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copy
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|E
argument_list|>
name|edgePredicate
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|original
argument_list|,
literal|"original"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|copy
argument_list|,
literal|"copy"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|edgePredicate
argument_list|,
literal|"edgePredicate"
argument_list|)
expr_stmt|;
for|for
control|(
name|E
name|edge
range|:
name|Sets
operator|.
name|filter
argument_list|(
name|original
operator|.
name|edges
argument_list|()
argument_list|,
name|edgePredicate
argument_list|)
control|)
block|{
name|addEdge
argument_list|(
name|copy
argument_list|,
name|edge
argument_list|,
name|original
operator|.
name|incidentNodes
argument_list|(
name|edge
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Returns true iff {@code graph1} and {@code graph2} have the same node connections.    *    *<p>Note: {@link Network} instances can only be equal to other {@link Network} instances.    * In particular, {@link Graph}s that are not also {@link Network}s cannot be equal    * to {@link Network}s.    *    * @see Network#equals(Object)    */
DECL|method|equal (@ullable Graph<?> graph1, @Nullable Graph<?> graph2)
specifier|public
specifier|static
name|boolean
name|equal
parameter_list|(
annotation|@
name|Nullable
name|Graph
argument_list|<
name|?
argument_list|>
name|graph1
parameter_list|,
annotation|@
name|Nullable
name|Graph
argument_list|<
name|?
argument_list|>
name|graph2
parameter_list|)
block|{
comment|// If both graphs are Network instances, use equal(Network, Network) instead
if|if
condition|(
name|graph1
operator|instanceof
name|Network
operator|&&
name|graph2
operator|instanceof
name|Network
condition|)
block|{
return|return
name|equal
argument_list|(
operator|(
name|Network
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|graph1
argument_list|,
operator|(
name|Network
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|graph2
argument_list|)
return|;
block|}
comment|// Otherwise, if either graph is a Network (but not both), they can't be equal.
if|if
condition|(
name|graph1
operator|instanceof
name|Network
operator|||
name|graph2
operator|instanceof
name|Network
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|graph1
operator|==
name|graph2
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|graph1
operator|==
literal|null
operator|||
name|graph2
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
operator|!
name|graph1
operator|.
name|nodes
argument_list|()
operator|.
name|equals
argument_list|(
name|graph2
operator|.
name|nodes
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|Object
name|node
range|:
name|graph1
operator|.
name|nodes
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|graph1
operator|.
name|successors
argument_list|(
name|node
argument_list|)
operator|.
name|equals
argument_list|(
name|graph2
operator|.
name|successors
argument_list|(
name|node
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|bothUndirected
init|=
operator|!
name|graph1
operator|.
name|isDirected
argument_list|()
operator|&&
operator|!
name|graph2
operator|.
name|isDirected
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|bothUndirected
operator|&&
operator|!
name|graph1
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
operator|.
name|equals
argument_list|(
name|graph2
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**    * Returns true iff {@code graph1} and {@code graph2} have the same node/edge relationships.    *    * @see Network#equals(Object)    */
DECL|method|equal (@ullable Network<?, ?> graph1, @Nullable Network<?, ?> graph2)
specifier|public
specifier|static
name|boolean
name|equal
parameter_list|(
annotation|@
name|Nullable
name|Network
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|graph1
parameter_list|,
annotation|@
name|Nullable
name|Network
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|graph2
parameter_list|)
block|{
if|if
condition|(
name|graph1
operator|==
name|graph2
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|graph1
operator|==
literal|null
operator|||
name|graph2
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
name|graph1
operator|.
name|edges
argument_list|()
operator|.
name|size
argument_list|()
operator|!=
name|graph2
operator|.
name|edges
argument_list|()
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|graph1
operator|.
name|nodes
argument_list|()
operator|.
name|equals
argument_list|(
name|graph2
operator|.
name|nodes
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|Object
name|node
range|:
name|graph1
operator|.
name|nodes
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|graph1
operator|.
name|inEdges
argument_list|(
name|node
argument_list|)
operator|.
name|equals
argument_list|(
name|graph2
operator|.
name|inEdges
argument_list|(
name|node
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|bothUndirected
init|=
operator|!
name|graph1
operator|.
name|isDirected
argument_list|()
operator|&&
operator|!
name|graph2
operator|.
name|isDirected
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|bothUndirected
operator|&&
operator|!
name|graph1
operator|.
name|outEdges
argument_list|(
name|node
argument_list|)
operator|.
name|equals
argument_list|(
name|graph2
operator|.
name|outEdges
argument_list|(
name|node
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**    * Returns the hash code of {@code graph}.    *    * @see Graph#hashCode()    */
DECL|method|hashCode (Graph<?> graph)
specifier|public
specifier|static
name|int
name|hashCode
parameter_list|(
name|Graph
argument_list|<
name|?
argument_list|>
name|graph
parameter_list|)
block|{
if|if
condition|(
name|graph
operator|instanceof
name|Network
condition|)
block|{
return|return
name|hashCode
argument_list|(
operator|(
name|Network
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|graph
argument_list|)
return|;
block|}
return|return
name|nodeToAdjacentNodes
argument_list|(
name|graph
argument_list|)
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/**    * Returns the hash code of {@code graph}.    *    * @see Network#hashCode()    */
DECL|method|hashCode (Network<?, ?> graph)
specifier|public
specifier|static
name|int
name|hashCode
parameter_list|(
name|Network
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|graph
parameter_list|)
block|{
return|return
name|nodeToIncidentEdges
argument_list|(
name|graph
argument_list|)
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/**    * Returns a string representation of {@code graph}. Encodes edge direction if {@code graph}    * is directed.    */
DECL|method|toString (Graph<?> graph)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Graph
argument_list|<
name|?
argument_list|>
name|graph
parameter_list|)
block|{
if|if
condition|(
name|graph
operator|instanceof
name|Network
condition|)
block|{
return|return
name|toString
argument_list|(
operator|(
name|Network
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|graph
argument_list|)
return|;
block|}
return|return
name|String
operator|.
name|format
argument_list|(
name|GRAPH_FORMAT
argument_list|,
name|getPropertiesString
argument_list|(
name|graph
argument_list|)
argument_list|,
name|graph
operator|.
name|nodes
argument_list|()
argument_list|,
name|adjacentNodesString
argument_list|(
name|graph
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a string representation of {@code graph}. Encodes edge direction if {@code graph}    * is directed.    */
DECL|method|toString (Network<?, ?> graph)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Network
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|graph
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
name|GRAPH_FORMAT
argument_list|,
name|getPropertiesString
argument_list|(
name|graph
argument_list|)
argument_list|,
name|graph
operator|.
name|nodes
argument_list|()
argument_list|,
name|Maps
operator|.
name|asMap
argument_list|(
name|graph
operator|.
name|edges
argument_list|()
argument_list|,
name|edgeToIncidentNodesString
argument_list|(
name|graph
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a String of the adjacent node relationships for {@code graph}.    */
DECL|method|adjacentNodesString (final Graph<N> graph)
specifier|private
specifier|static
parameter_list|<
name|N
parameter_list|>
name|String
name|adjacentNodesString
parameter_list|(
specifier|final
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
name|List
argument_list|<
name|String
argument_list|>
name|adjacencies
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// This will list each undirected edge twice (once as [n1, n2] and once as [n2, n1]); this is OK
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
name|successor
range|:
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
control|)
block|{
name|adjacencies
operator|.
name|add
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|graph
operator|.
name|isDirected
argument_list|()
condition|?
name|DIRECTED_FORMAT
else|:
name|UNDIRECTED_FORMAT
argument_list|,
name|node
argument_list|,
name|successor
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|String
operator|.
name|format
argument_list|(
literal|"{%s}"
argument_list|,
name|Joiner
operator|.
name|on
argument_list|(
literal|", "
argument_list|)
operator|.
name|join
argument_list|(
name|adjacencies
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a map that is a live view of {@code graph}, with nodes as keys    * and the set of incident edges as values.    */
DECL|method|nodeToIncidentEdges (final Network<N, E> graph)
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
name|Set
argument_list|<
name|E
argument_list|>
argument_list|>
name|nodeToIncidentEdges
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
name|checkNotNull
argument_list|(
name|graph
argument_list|,
literal|"graph"
argument_list|)
expr_stmt|;
return|return
name|Maps
operator|.
name|asMap
argument_list|(
name|graph
operator|.
name|nodes
argument_list|()
argument_list|,
operator|new
name|Function
argument_list|<
name|N
argument_list|,
name|Set
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|apply
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|graph
operator|.
name|incidentEdges
argument_list|(
name|node
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**    * Returns a map that is a live view of {@code graph}, with nodes as keys    * and the set of adjacent nodes as values.    */
DECL|method|nodeToAdjacentNodes (final Graph<N> graph)
specifier|private
specifier|static
parameter_list|<
name|N
parameter_list|>
name|Map
argument_list|<
name|N
argument_list|,
name|Set
argument_list|<
name|N
argument_list|>
argument_list|>
name|nodeToAdjacentNodes
parameter_list|(
specifier|final
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
return|return
name|Maps
operator|.
name|asMap
argument_list|(
name|graph
operator|.
name|nodes
argument_list|()
argument_list|,
operator|new
name|Function
argument_list|<
name|N
argument_list|,
name|Set
argument_list|<
name|N
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|apply
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**    * Returns a function that transforms an edge into a string representation of its incident nodes    * in {@code graph}. The function's {@code apply} method will throw an    * {@link IllegalArgumentException} if {@code graph} does not contain {@code edge}.    */
DECL|method|edgeToIncidentNodesString (final Network<?, ?> graph)
specifier|private
specifier|static
name|Function
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
name|edgeToIncidentNodesString
parameter_list|(
specifier|final
name|Network
argument_list|<
name|?
argument_list|,
name|?
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
return|return
operator|new
name|Function
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|apply
parameter_list|(
name|Object
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
name|toString
argument_list|()
return|;
block|}
block|}
return|;
block|}
comment|/**    * Returns a string representation of the properties of {@code graph}.    */
comment|// TODO(b/28087289): add allowsParallelEdges() once that's supported
DECL|method|getPropertiesString (Graph<?> graph)
specifier|private
specifier|static
name|String
name|getPropertiesString
parameter_list|(
name|Graph
argument_list|<
name|?
argument_list|>
name|graph
parameter_list|)
block|{
if|if
condition|(
name|graph
operator|instanceof
name|Network
condition|)
block|{
return|return
name|getPropertiesString
argument_list|(
operator|(
name|Network
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|graph
argument_list|)
return|;
block|}
return|return
name|String
operator|.
name|format
argument_list|(
literal|"isDirected: %s, allowsSelfLoops: %s"
argument_list|,
name|graph
operator|.
name|isDirected
argument_list|()
argument_list|,
name|graph
operator|.
name|allowsSelfLoops
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns a string representation of the properties of {@code graph}.    */
DECL|method|getPropertiesString (Network<?, ?> graph)
specifier|private
specifier|static
name|String
name|getPropertiesString
parameter_list|(
name|Network
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|graph
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"isDirected: %s, allowsParallelEdges: %s, allowsSelfLoops: %s"
argument_list|,
name|graph
operator|.
name|isDirected
argument_list|()
argument_list|,
name|graph
operator|.
name|allowsParallelEdges
argument_list|()
argument_list|,
name|graph
operator|.
name|allowsSelfLoops
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

