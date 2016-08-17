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
comment|/**  * An interface to represent a graph data structure. Graphs can be either directed or undirected  * (but cannot have both directed edges and undirected edges). Every edge is associated with an  * arbitrary user-provided value. Parallel edges are not supported (although the Value type may be,  * for example, a collection).  *  *<p>Nodes in a graph are analogous to keys in a Map - they must be unique within a graph.  * Values in a graph are analogous to values in a Map - they may be any arbitrary object.  *  * TODO(b/30133524): Rewrite the top-level javadoc from scratch.  *  * TODO(jasexton): Rename interface (and various other classes) to "Graph".  *  * @author James Sexton  * @param<N> Node parameter type  * @param<V> Value parameter type  * @since 20.0  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|Graph
specifier|public
interface|interface
name|Graph
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
block|{
comment|//
comment|// Graph-level accessors
comment|//
comment|/**    * Returns all nodes in this graph, in the order specified by {@link #nodeOrder()}.    */
DECL|method|nodes ()
name|Set
argument_list|<
name|N
argument_list|>
name|nodes
parameter_list|()
function_decl|;
comment|/**    * Returns all edges in this graph.    */
DECL|method|edges ()
name|Set
argument_list|<
name|Endpoints
argument_list|<
name|N
argument_list|>
argument_list|>
name|edges
parameter_list|()
function_decl|;
comment|//
comment|// Graph properties
comment|//
comment|/**    * Returns true if the edges in this graph have a direction associated with them.    */
DECL|method|isDirected ()
name|boolean
name|isDirected
parameter_list|()
function_decl|;
comment|/**    * Returns true if this graph allows self-loops (edges that connect a node to itself).    * Attempting to add a self-loop to a graph that does not allow them will throw an    * {@link UnsupportedOperationException}.    */
DECL|method|allowsSelfLoops ()
name|boolean
name|allowsSelfLoops
parameter_list|()
function_decl|;
comment|/**    * Returns the order of iteration for the elements of {@link #nodes()}.    */
DECL|method|nodeOrder ()
name|ElementOrder
argument_list|<
name|N
argument_list|>
name|nodeOrder
parameter_list|()
function_decl|;
comment|//
comment|// Element-level accessors
comment|//
comment|/**    * Returns the nodes which have an incident edge in common with {@code node} in this graph.    *    * @throws IllegalArgumentException if {@code node} is not an element of this graph    */
DECL|method|adjacentNodes (Object node)
name|Set
argument_list|<
name|N
argument_list|>
name|adjacentNodes
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * Returns all nodes in this graph adjacent to {@code node} which can be reached by traversing    * {@code node}'s incoming edges<i>against</i> the direction (if any) of the edge.    *    * @throws IllegalArgumentException if {@code node} is not an element of this graph    */
DECL|method|predecessors (Object node)
name|Set
argument_list|<
name|N
argument_list|>
name|predecessors
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * Returns all nodes in this graph adjacent to {@code node} which can be reached by traversing    * {@code node}'s outgoing edges in the direction (if any) of the edge.    *    *<p>This is<i>not</i> the same as "all nodes reachable from {@code node} by following outgoing    * edges". For that functionality, see {@link Graphs#reachableNodes(Graph, Object)} and {@link    * Graphs#transitiveClosure(Graph)}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this graph    */
DECL|method|successors (Object node)
name|Set
argument_list|<
name|N
argument_list|>
name|successors
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * If there is an edge connecting {@code nodeA} to {@code nodeB}, returns the non-null value    * associated with that edge.    *    * @throws IllegalArgumentException if there is no edge connecting {@code nodeA} to {@code nodeB}    */
DECL|method|edgeValue (Object nodeA, Object nodeB)
name|V
name|edgeValue
parameter_list|(
name|Object
name|nodeA
parameter_list|,
name|Object
name|nodeB
parameter_list|)
function_decl|;
comment|/**    * If there is an edge connecting {@code nodeA} to {@code nodeB}, returns the non-null value    * associated with that edge; otherwise, returns {@code defaultValue}.    *    * @throws IllegalArgumentException if {@code nodeA} or {@code nodeB} is not an element of    *     this graph    */
DECL|method|edgeValueOrDefault (Object nodeA, Object nodeB, @Nullable V defaultValue)
name|V
name|edgeValueOrDefault
parameter_list|(
name|Object
name|nodeA
parameter_list|,
name|Object
name|nodeB
parameter_list|,
annotation|@
name|Nullable
name|V
name|defaultValue
parameter_list|)
function_decl|;
comment|//
comment|// Graph identity
comment|//
comment|/**    * Returns {@code true} iff {@code object} is a {@link Graph} that has the same structural    * relationships as those in this graph.    *    *<p>Thus, two graphs A and B are equal if<b>all</b> of the following are true:    *<ul>    *<li>A and B have equal {@link #isDirected() directedness}.    *<li>A and B have equal {@link #nodes() node sets}.    *<li>A and B have equal {@link #edges() edge sets}.    *<li>Every edge in A and B are associated with equal {@link #edgeValue(Object, Object) values}.    *</ul>    *    *<p>Graph properties besides {@link #isDirected() directedness} do<b>not</b> affect equality.    * For example, two graphs may be considered equal even if one allows self-loops and the other    * doesn't. Additionally, the order in which nodes or edges are added to the graph, and the order    * in which they are iterated over, are irrelevant.    *    *<p>A reference implementation of this is provided by {@link AbstractGraph#equals(Object)}.    */
annotation|@
name|Override
DECL|method|equals (@ullable Object object)
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
function_decl|;
comment|/**    * Returns the hash code for this graph. The hash code of a graph is defined as the hash code    * of a map from each of its {@link #edges() edges} to the associated {@link #edgeValue(Object,    * Object) edge value}.    *    *<p>A reference implementation of this is provided by {@link AbstractGraph#hashCode()}.    */
annotation|@
name|Override
DECL|method|hashCode ()
name|int
name|hashCode
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

