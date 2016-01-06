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

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|CheckReturnValue
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
comment|/**  * Static utility methods for {@link Graph} instances.  *  * @author Joshua O'Madadhain  * @see Graph  * @since 20.0  */
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
DECL|field|MULTIGRAPH
specifier|public
specifier|static
specifier|final
name|GraphConfig
name|MULTIGRAPH
init|=
name|config
argument_list|()
operator|.
name|multigraph
argument_list|()
decl_stmt|;
DECL|method|Graphs ()
specifier|private
name|Graphs
parameter_list|()
block|{}
comment|/**    * Returns the node at the other end of {@code e} from {@code n}.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|CheckReturnValue
DECL|method|oppositeNode ( UndirectedGraph<N, ?> undirectedGraph, Object edge, Object node)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|>
name|N
name|oppositeNode
parameter_list|(
name|UndirectedGraph
argument_list|<
name|N
argument_list|,
name|?
argument_list|>
name|undirectedGraph
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
name|edge
argument_list|,
literal|"edge"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|node
argument_list|,
literal|"node"
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|N
argument_list|>
name|incidentNodes
init|=
name|undirectedGraph
operator|.
name|incidentNodes
argument_list|(
name|edge
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|incidentNodes
operator|.
name|contains
argument_list|(
name|node
argument_list|)
argument_list|,
literal|"Edge %s is not incident to node %s"
argument_list|,
name|edge
argument_list|,
name|node
argument_list|)
expr_stmt|;
for|for
control|(
name|N
name|incidentNode
range|:
name|incidentNodes
control|)
block|{
if|if
condition|(
operator|!
name|incidentNode
operator|.
name|equals
argument_list|(
name|node
argument_list|)
condition|)
block|{
return|return
name|incidentNode
return|;
block|}
block|}
comment|// Reaching this point means that incidentNodes contains only one node,
comment|// which is node, hence edge is a self-loop for node, and node is its own opposite
return|return
operator|(
name|N
operator|)
name|node
return|;
block|}
comment|/**    * Adds {@code edge} to {@code graph} with the specified incident {@code nodes}, in the order    * returned by {@code nodes}' iterator.    */
DECL|method|addEdge (Graph<N, E> graph, E edge, Iterable<N> nodes)
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
name|Graph
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
name|Iterable
argument_list|<
name|N
argument_list|>
name|nodes
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
name|nodes
argument_list|,
literal|"nodes"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|edge
argument_list|,
literal|"edge"
argument_list|)
expr_stmt|;
if|if
condition|(
name|graph
operator|instanceof
name|Hypergraph
condition|)
block|{
return|return
operator|(
operator|(
name|Hypergraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
operator|)
name|graph
operator|)
operator|.
name|addEdge
argument_list|(
name|edge
argument_list|,
name|nodes
argument_list|)
return|;
block|}
name|Iterator
argument_list|<
name|N
argument_list|>
name|it
init|=
name|nodes
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|,
literal|"'graph' is not a Hypergraph, and 'nodes' has< 1 elements: %s"
argument_list|,
name|nodes
argument_list|)
expr_stmt|;
name|N
name|n1
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|N
name|n2
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
operator|!
name|it
operator|.
name|hasNext
argument_list|()
argument_list|,
literal|"'graph' is not a Hypergraph, and 'nodes' has> 2 elements: %s"
argument_list|,
name|nodes
argument_list|)
expr_stmt|;
return|return
name|graph
operator|.
name|addEdge
argument_list|(
name|edge
argument_list|,
name|n1
argument_list|,
name|n2
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|graph
operator|.
name|addEdge
argument_list|(
name|edge
argument_list|,
name|n1
argument_list|,
name|n1
argument_list|)
return|;
block|}
block|}
comment|/**    * Creates a mutable copy of {@code graph}, using the same node and edge elements.    */
annotation|@
name|CheckReturnValue
DECL|method|copyOf (DirectedGraph<N, E> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|DirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copyOf
parameter_list|(
name|DirectedGraph
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
name|DirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copy
init|=
name|createDirected
argument_list|(
name|graph
operator|.
name|config
argument_list|()
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
argument_list|)
decl_stmt|;
name|mergeNodesFrom
argument_list|(
name|graph
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|mergeEdgesFrom
argument_list|(
name|graph
argument_list|,
name|copy
argument_list|)
expr_stmt|;
return|return
name|copy
return|;
block|}
comment|/**    * Creates a mutable copy of {@code graph}, using all of its elements that satisfy    * {@code nodePredicate} and {@code edgePredicate}.    */
annotation|@
name|CheckReturnValue
DECL|method|copyOf ( DirectedGraph<N, E> graph, Predicate<? super N> nodePredicate, Predicate<? super E> edgePredicate)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|DirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copyOf
parameter_list|(
name|DirectedGraph
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
name|DirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copy
init|=
name|createDirected
argument_list|(
name|graph
operator|.
name|config
argument_list|()
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
argument_list|)
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
comment|// the edge's incident nodes if they are not present; we need to run them past nodePredicate.
if|if
condition|(
name|edgePredicate
operator|.
name|equals
argument_list|(
name|Predicates
operator|.
expr|<
name|E
operator|>
name|alwaysFalse
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|copy
return|;
comment|// no edges to add
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
if|if
condition|(
name|edgePredicate
operator|.
name|apply
argument_list|(
name|edge
argument_list|)
condition|)
block|{
name|N
name|source
init|=
name|graph
operator|.
name|source
argument_list|(
name|edge
argument_list|)
decl_stmt|;
name|N
name|target
init|=
name|graph
operator|.
name|target
argument_list|(
name|edge
argument_list|)
decl_stmt|;
if|if
condition|(
name|nodePredicate
operator|.
name|apply
argument_list|(
name|source
argument_list|)
operator|&&
name|nodePredicate
operator|.
name|apply
argument_list|(
name|target
argument_list|)
condition|)
block|{
name|copy
operator|.
name|addEdge
argument_list|(
name|edge
argument_list|,
name|source
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|copy
return|;
block|}
comment|/**    * Copies all nodes from {@code original} into {@code copy}.    */
DECL|method|mergeNodesFrom (Graph<N, E> original, Graph<N, E> copy)
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
argument_list|,
name|E
argument_list|>
name|original
parameter_list|,
name|Graph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copy
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
for|for
control|(
name|N
name|node
range|:
name|original
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
block|}
comment|/**    * Copies all nodes from {@code original} into {@code copy} that satisfy {@code nodePredicate}.    */
DECL|method|mergeNodesFrom ( Graph<N, E> original, Graph<N, E> copy, Predicate<? super N> nodePredicate)
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
argument_list|,
name|E
argument_list|>
name|original
parameter_list|,
name|Graph
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
if|if
condition|(
name|nodePredicate
operator|.
name|equals
argument_list|(
name|Predicates
operator|.
expr|<
name|N
operator|>
name|alwaysFalse
argument_list|()
argument_list|)
condition|)
block|{
return|return;
comment|// nothing to do
block|}
if|if
condition|(
name|nodePredicate
operator|.
name|equals
argument_list|(
name|Predicates
operator|.
expr|<
name|N
operator|>
name|alwaysTrue
argument_list|()
argument_list|)
condition|)
block|{
name|mergeNodesFrom
argument_list|(
name|original
argument_list|,
name|copy
argument_list|)
expr_stmt|;
comment|// optimization
block|}
else|else
block|{
for|for
control|(
name|N
name|node
range|:
name|original
operator|.
name|nodes
argument_list|()
control|)
block|{
if|if
condition|(
name|nodePredicate
operator|.
name|apply
argument_list|(
name|node
argument_list|)
condition|)
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
block|}
block|}
comment|/**    * Copies all edges from {@code original} into {@code copy}.  Also copies all nodes incident    * to these edges.    */
DECL|method|mergeEdgesFrom (DirectedGraph<N, E> original, DirectedGraph<N, E> copy)
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
name|DirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|original
parameter_list|,
name|DirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copy
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
for|for
control|(
name|E
name|edge
range|:
name|original
operator|.
name|edges
argument_list|()
control|)
block|{
name|copy
operator|.
name|addEdge
argument_list|(
name|edge
argument_list|,
name|original
operator|.
name|source
argument_list|(
name|edge
argument_list|)
argument_list|,
name|original
operator|.
name|target
argument_list|(
name|edge
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Copies all edges from {@code original} into {@code copy} that satisfy {@code edgePredicate}.    * Also copies all nodes incident to these edges.    */
DECL|method|mergeEdgesFrom ( DirectedGraph<N, E> original, DirectedGraph<N, E> copy, Predicate<? super E> edgePredicate)
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
name|DirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|original
parameter_list|,
name|DirectedGraph
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
if|if
condition|(
name|edgePredicate
operator|.
name|equals
argument_list|(
name|Predicates
operator|.
expr|<
name|E
operator|>
name|alwaysFalse
argument_list|()
argument_list|)
condition|)
block|{
return|return;
comment|// nothing to do
block|}
if|if
condition|(
name|edgePredicate
operator|.
name|equals
argument_list|(
name|Predicates
operator|.
expr|<
name|E
operator|>
name|alwaysTrue
argument_list|()
argument_list|)
condition|)
block|{
name|mergeEdgesFrom
argument_list|(
name|original
argument_list|,
name|copy
argument_list|)
expr_stmt|;
comment|// optimization
block|}
else|else
block|{
for|for
control|(
name|E
name|edge
range|:
name|original
operator|.
name|edges
argument_list|()
control|)
block|{
if|if
condition|(
name|edgePredicate
operator|.
name|apply
argument_list|(
name|edge
argument_list|)
condition|)
block|{
name|copy
operator|.
name|addEdge
argument_list|(
name|edge
argument_list|,
name|original
operator|.
name|source
argument_list|(
name|edge
argument_list|)
argument_list|,
name|original
operator|.
name|target
argument_list|(
name|edge
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**    * Creates a mutable copy of {@code graph}, using the same node and edge elements.    */
annotation|@
name|CheckReturnValue
DECL|method|copyOf (UndirectedGraph<N, E> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|UndirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copyOf
parameter_list|(
name|UndirectedGraph
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
name|UndirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copy
init|=
name|createUndirected
argument_list|(
name|graph
operator|.
name|config
argument_list|()
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
argument_list|)
decl_stmt|;
name|mergeNodesFrom
argument_list|(
name|graph
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|mergeEdgesFrom
argument_list|(
name|graph
argument_list|,
name|copy
argument_list|)
expr_stmt|;
return|return
name|copy
return|;
block|}
comment|/**    * Creates a mutable copy of {@code graph}, using all of its elements that satisfy    * {@code nodePredicate} and {@code edgePredicate}.    */
annotation|@
name|CheckReturnValue
DECL|method|copyOf ( UndirectedGraph<N, E> graph, Predicate<? super N> nodePredicate, Predicate<? super E> edgePredicate)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|UndirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copyOf
parameter_list|(
name|UndirectedGraph
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
name|UndirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copy
init|=
name|createUndirected
argument_list|(
name|graph
operator|.
name|config
argument_list|()
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
argument_list|)
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
comment|// the edge's incident nodes if they are not present; we need to run them past nodePredicate.
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
if|if
condition|(
name|edgePredicate
operator|.
name|apply
argument_list|(
name|edge
argument_list|)
condition|)
block|{
name|boolean
name|nodesOk
init|=
literal|true
decl_stmt|;
name|Set
argument_list|<
name|N
argument_list|>
name|incidentNodes
init|=
name|graph
operator|.
name|incidentNodes
argument_list|(
name|edge
argument_list|)
decl_stmt|;
for|for
control|(
name|N
name|node
range|:
name|incidentNodes
control|)
block|{
name|nodesOk
operator|&=
name|nodePredicate
operator|.
name|apply
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|nodesOk
condition|)
block|{
name|addEdge
argument_list|(
name|copy
argument_list|,
name|edge
argument_list|,
name|incidentNodes
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|copy
return|;
block|}
comment|/**    * Copies all edges from {@code original} into {@code copy}.  Also copies all nodes incident    * to these edges.    */
DECL|method|mergeEdgesFrom (Graph<N, E> original, Graph<N, E> copy)
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
name|Graph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|original
parameter_list|,
name|Graph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|copy
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
for|for
control|(
name|E
name|edge
range|:
name|original
operator|.
name|edges
argument_list|()
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
comment|/**    * Copies all edges from {@code original} into {@code copy} that satisfy {@code edgePredicate}.    * Also copies all nodes incident to these edges.    */
comment|// NOTE: this is identical to mergeEdgesFrom(DirectedGraph) except for the call to addEdge
DECL|method|mergeEdgesFrom (Graph<N, E> original, Graph<N, E> copy, Predicate<? super E> edgePredicate)
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
name|Graph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|original
parameter_list|,
name|Graph
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
if|if
condition|(
name|edgePredicate
operator|.
name|equals
argument_list|(
name|Predicates
operator|.
expr|<
name|E
operator|>
name|alwaysFalse
argument_list|()
argument_list|)
condition|)
block|{
return|return;
comment|// nothing to do
block|}
if|if
condition|(
name|edgePredicate
operator|.
name|equals
argument_list|(
name|Predicates
operator|.
expr|<
name|E
operator|>
name|alwaysTrue
argument_list|()
argument_list|)
condition|)
block|{
name|mergeEdgesFrom
argument_list|(
name|original
argument_list|,
name|copy
argument_list|)
expr_stmt|;
comment|// optimization
block|}
else|else
block|{
for|for
control|(
name|E
name|edge
range|:
name|original
operator|.
name|edges
argument_list|()
control|)
block|{
if|if
condition|(
name|edgePredicate
operator|.
name|apply
argument_list|(
name|edge
argument_list|)
condition|)
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
block|}
block|}
comment|/**    * Copies all nodes and edges from {@code original} to {@code copy} that satisfy    * {@code nodePredicate} and {@code edgePredicate}.    */
DECL|method|copyFrom ( Graph<N, E> original, Graph<N, E> copy, Predicate<? super N> nodePredicate, Predicate<? super E> edgePredicate)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|void
name|copyFrom
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|original
parameter_list|,
name|Graph
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
name|checkNotNull
argument_list|(
name|edgePredicate
argument_list|,
literal|"edgePredicate"
argument_list|)
expr_stmt|;
name|mergeNodesFrom
argument_list|(
name|original
argument_list|,
name|copy
argument_list|,
name|nodePredicate
argument_list|)
expr_stmt|;
name|mergeEdgesFrom
argument_list|(
name|original
argument_list|,
name|copy
argument_list|,
name|edgePredicate
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns a new default instance of {@code GraphConfig}.    *    * @see GraphConfig    */
annotation|@
name|CheckReturnValue
DECL|method|config ()
specifier|public
specifier|static
name|GraphConfig
name|config
parameter_list|()
block|{
return|return
operator|new
name|GraphConfig
argument_list|()
return|;
block|}
comment|/**    * Returns a new instance of {@link DirectedGraph} with the default    * graph configuration.    *    * @see GraphConfig    */
annotation|@
name|CheckReturnValue
DECL|method|createDirected ()
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|DirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|createDirected
parameter_list|()
block|{
return|return
operator|new
name|IncidenceSetDirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|(
name|config
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns a new instance of {@link DirectedGraph} with the graph    * configuration specified by {@code config}.    */
annotation|@
name|CheckReturnValue
DECL|method|createDirected (GraphConfig config)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|DirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|createDirected
parameter_list|(
name|GraphConfig
name|config
parameter_list|)
block|{
return|return
operator|new
name|IncidenceSetDirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|(
name|config
argument_list|)
return|;
block|}
comment|/**    * Returns a new instance of {@link UndirectedGraph} with the default    * graph configuration.    *    * @see GraphConfig    */
annotation|@
name|CheckReturnValue
DECL|method|createUndirected ()
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|UndirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|createUndirected
parameter_list|()
block|{
return|return
operator|new
name|IncidenceSetUndirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|(
name|config
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns a new instance of {@link UndirectedGraph} with the graph    * configuration specified by {@code config}.    */
annotation|@
name|CheckReturnValue
DECL|method|createUndirected (GraphConfig config)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|UndirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|createUndirected
parameter_list|(
name|GraphConfig
name|config
parameter_list|)
block|{
return|return
operator|new
name|IncidenceSetUndirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|(
name|config
argument_list|)
return|;
block|}
comment|/**    * Returns true iff {@code g1} and {@code g2} have the same node and edge sets and each edge    * has the same source and target in both graphs.    *    * @see Graph#equals(Object)    */
annotation|@
name|CheckReturnValue
DECL|method|equal ( @ullable DirectedGraph<?, ?> g1, @Nullable DirectedGraph<?, ?> g2)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|boolean
name|equal
parameter_list|(
annotation|@
name|Nullable
name|DirectedGraph
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|g1
parameter_list|,
annotation|@
name|Nullable
name|DirectedGraph
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|g2
parameter_list|)
block|{
if|if
condition|(
name|g1
operator|==
name|g2
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|g1
operator|==
literal|null
operator|||
name|g2
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
name|g1
operator|.
name|nodes
argument_list|()
operator|.
name|equals
argument_list|(
name|g2
operator|.
name|nodes
argument_list|()
argument_list|)
operator|||
operator|!
name|g1
operator|.
name|edges
argument_list|()
operator|.
name|equals
argument_list|(
name|g2
operator|.
name|edges
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
name|e
range|:
name|g1
operator|.
name|edges
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|g1
operator|.
name|source
argument_list|(
name|e
argument_list|)
operator|.
name|equals
argument_list|(
name|g2
operator|.
name|source
argument_list|(
name|e
argument_list|)
argument_list|)
operator|||
operator|!
name|g1
operator|.
name|target
argument_list|(
name|e
argument_list|)
operator|.
name|equals
argument_list|(
name|g2
operator|.
name|target
argument_list|(
name|e
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
comment|/**    * Returns true iff {@code g1} and {@code g2} have the same node and edge sets and each edge    * has the same incident node set in both graphs.    *    * @see Graph#equals(Object)    */
annotation|@
name|CheckReturnValue
DECL|method|equal (@ullable Graph<?, ?> g1, @Nullable Graph<?, ?> g2)
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
argument_list|,
name|?
argument_list|>
name|g1
parameter_list|,
annotation|@
name|Nullable
name|Graph
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|g2
parameter_list|)
block|{
if|if
condition|(
name|g1
operator|==
name|g2
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|g1
operator|==
literal|null
operator|||
name|g2
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
name|g1
operator|.
name|nodes
argument_list|()
operator|.
name|equals
argument_list|(
name|g2
operator|.
name|nodes
argument_list|()
argument_list|)
operator|||
operator|!
name|g1
operator|.
name|edges
argument_list|()
operator|.
name|equals
argument_list|(
name|g2
operator|.
name|edges
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
name|e
range|:
name|g1
operator|.
name|edges
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|g1
operator|.
name|incidentNodes
argument_list|(
name|e
argument_list|)
operator|.
name|equals
argument_list|(
name|g2
operator|.
name|incidentNodes
argument_list|(
name|e
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
comment|/**    * Returns a {@link Predicate} that returns {@code true} if the input edge is not a self-loop in    * {@code graph}. A self-loop is defined as an edge whose set of incident nodes has exactly one    * element. The predicate's {@code apply} method will throw a {@link IllegalStateException} if    * {@code graph} does not contain {@code edge}.    */
annotation|@
name|CheckReturnValue
DECL|method|noSelfLoopPredicate (final Graph<?, E> graph)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Predicate
argument_list|<
name|E
argument_list|>
name|noSelfLoopPredicate
parameter_list|(
specifier|final
name|Graph
argument_list|<
name|?
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
operator|new
name|Predicate
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|E
name|edge
parameter_list|)
block|{
name|checkState
argument_list|(
name|graph
operator|.
name|edges
argument_list|()
operator|.
name|contains
argument_list|(
name|edge
argument_list|)
argument_list|,
literal|"Graph does not contain edge %s"
argument_list|,
name|edge
argument_list|)
expr_stmt|;
return|return
name|graph
operator|.
name|incidentNodes
argument_list|(
name|edge
argument_list|)
operator|.
name|size
argument_list|()
operator|!=
literal|1
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit
