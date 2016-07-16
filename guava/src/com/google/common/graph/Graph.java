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
name|ConcurrentModificationException
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
comment|/**  * An interface for<a href="https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)">graph</a>  * data structures. A graph is composed of a set of nodes (sometimes called vertices) and a set of  * edges connecting pairs of nodes. Graphs are useful for modeling many kinds of relations. If the  * relation to be modeled is symmetric (such as "distance between cities"), that can be represented  * with an undirected graph, where an edge that connects node A to node B also connects node B to  * node A. If the relation to be modeled is asymmetric (such as "employees managed"), that can be  * represented with a directed graph, where edges are strictly one-way.  *  *<p>There are three main interfaces provided to represent graphs. In order of increasing  * complexity they are: {@link BasicGraph}, {@link Graph}, and {@link Network}. You should generally  * prefer the simplest interface that satisfies your use case.  *  *<p>To choose the right interface, answer these questions:  *  *<ol>  *<li>Do you have data (objects) that you wish to associate with edges?  *<p>Yes: Go to question 2. No: Use {@link BasicGraph}.  *<li>Are the objects you wish to associate with edges unique within the scope of a graph? That is,  *     no two objects would be {@link Object#equals(Object)} to each other. A common example where  *     this would<i>not</i> be the case is with weighted graphs.  *<p>Yes: Go to question 3. No: Use {@link Graph}.  *<li>Do you need to be able to query the graph for an edge associated with a particular object  *     (not just the edge connecting a given pair of nodes)?  *<p>Yes: Use {@link Network}. No: Go to question 4.  *<li>Do you need explicit support for parallel edges? Do you need to be able to remove one edge  *     connecting a pair of nodes while leaving other edges connecting those same nodes?  *<p>Yes: Use {@link Network}. No: Use {@link Graph}.  *</ol>  *  *<p>Although {@link Graph}s and {@link Network}s both require users to provide objects when adding  * edges to the graph, the differentiating factor is that in {@link Graph}s, these objects can be  * any arbitrary data. Like the values in a {@link Map}, they do not have to be unique, and can be  * mutated while in the graph. In a {@link Network}, these objects serve as keys into internal data  * structures. Like the keys in a {@link Map}, they must be unique, and cannot be mutated in a way  * that affects their equals/hashcode or the data structure will become corrupted.  *  *<p>In all three interfaces, nodes have all the same requirements as keys in a {@link Map}.  *  *<p>All mutation methods live on the subinterface {@link MutableGraph}. If you do not need to  * mutate a graph (e.g. if you write a method than runs a read-only algorithm on the graph), you  * should prefer the non-mutating {@link Graph} interface.  *  *<p>We provide an efficient implementation of this interface via {@link GraphBuilder}. When using  * the implementation provided, all {@link Set}-returning methods provide live, unmodifiable views  * of the graph. In other words, you cannot add an element to the {@link Set}, but if an element is  * added to the {@link Graph} that would affect the result of that set, it will be updated  * automatically. This also means that you cannot modify a {@link Graph} in a way that would affect  * a {#link Set} while iterating over that set. For example, you cannot remove the nodes from a  * {@link Graph} while iterating over {@link #nodes} (unless you first make a copy of the nodes),  * just as you could not remove the keys from a {@Map} while iterating over its {@link  * Map#keySet()}. This will either throw a {@link ConcurrentModificationException} or risk undefined  * behavior.  *  *<p>Example of use:  *  *<pre><code>  * MutableGraph<String, Double> synonymGraph = GraphBuilder.undirected().build();  * synonymGraph.putEdgeValue("large", "big", 0.9);  * synonymGraph.putEdgeValue("large", "huge", 0.9);  * synonymGraph.putEdgeValue("large", "grand", 0.6);  * synonymGraph.putEdgeValue("large", "cold", 0.0);  * synonymGraph.putEdgeValue("large", "small", -1.0);  * for (String word : synonymGraph.adjacentNodes("large")) {  *   if (synonymGraph.edgeValue(word, "large")> 0.5) {  *     System.out.println(word + " is a synonym for large");  *   }  * }  *</code></pre>  *  * @author James Sexton  * @param<N> Node parameter type  * @param<V> Value parameter type  * @since 20.0  */
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
comment|/** Returns all nodes in this graph, in the order specified by {@link #nodeOrder()}. */
DECL|method|nodes ()
name|Set
argument_list|<
name|N
argument_list|>
name|nodes
parameter_list|()
function_decl|;
comment|/** Returns all edges in this graph. */
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
comment|/** Returns true if the edges in this graph have a direction associated with them. */
DECL|method|isDirected ()
name|boolean
name|isDirected
parameter_list|()
function_decl|;
comment|/**    * Returns true if this graph allows self-loops (edges that connect a node to itself). Attempting    * to add a self-loop to a graph that does not allow them will throw an {@link    * UnsupportedOperationException}.    */
DECL|method|allowsSelfLoops ()
name|boolean
name|allowsSelfLoops
parameter_list|()
function_decl|;
comment|/** Returns the order of iteration for the elements of {@link #nodes()}. */
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
comment|/**    * Returns the count of {@code node}'s incident edges, counting self-loops twice (equivalently,    * the number of times an edge touches {@code node}).    *    *<p>For directed graphs, this is equivalent to {@code inDegree(node) + outDegree(node)}.    *    *<p>For undirected graphs, this is equivalent to {@code adjacentNodes(node).size()} + (1 if    * {@code node} has an incident self-loop, 0 otherwise).    *    *<p>If the count is greater than {@code Integer.MAX_VALUE}, returns {@code Integer.MAX_VALUE}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this graph    */
DECL|method|degree (Object node)
name|int
name|degree
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * Returns the count of {@code node}'s incoming edges (equal to {@code predecessors(node).size()})    * in a directed graph. In an undirected graph, returns the {@link #degree(Object)}.    *    *<p>If the count is greater than {@code Integer.MAX_VALUE}, returns {@code Integer.MAX_VALUE}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this graph    */
DECL|method|inDegree (Object node)
name|int
name|inDegree
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * Returns the count of {@code node}'s outgoing edges (equal to {@code successors(node).size()})    * in a directed graph. In an undirected graph, returns the {@link #degree(Object)}.    *    *<p>If the count is greater than {@code Integer.MAX_VALUE}, returns {@code Integer.MAX_VALUE}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this graph    */
DECL|method|outDegree (Object node)
name|int
name|outDegree
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
comment|/**    * If there is an edge connecting {@code nodeA} to {@code nodeB}, returns the non-null value    * associated with that edge; otherwise, returns {@code defaultValue}.    *    * @throws IllegalArgumentException if {@code nodeA} or {@code nodeB} is not an element of this    *     graph    */
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
comment|/**    * Returns {@code true} iff {@code object} is a {@link Graph} that has the same elements and the    * same structural relationships as those in this graph.    *    *<p>Thus, two graphs A and B are equal if<b>all</b> of the following are true:    *    *<ul>    *<li>A and B have equal {@link #isDirected() directedness}.    *<li>A and B have equal {@link #nodes() node sets}.    *<li>A and B have equal {@link #edges() edge sets}.    *<li>Every edge in A and B are associated with equal {@link #edgeValue(Object, Object) values}.    *</ul>    *    *<p>Graph properties besides {@link #isDirected() directedness} do<b>not</b> affect equality.    * For example, two graphs may be considered equal even if one allows self-loops and the other    * doesn't. Additionally, the order in which nodes or edges are added to the graph, and the order    * in which they are iterated over, are irrelevant.    *    *<p>A reference implementation of this is provided by {@link AbstractGraph#equals(Object)}.    */
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
comment|/**    * Returns the hash code for this graph. The hash code of a graph is defined as the hash code of a    * map from each of its {@link #edges() edges} to the associated {@link #edgeValue(Object, Object)    * edge value}.    *    *<p>A reference implementation of this is provided by {@link AbstractGraph#hashCode()}.    */
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

