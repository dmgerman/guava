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
comment|/**  * An interface for<a href="https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)">graph</a>  * data structures. A graph is composed of a set of nodes (sometimes called vertices) and a set of  * edges connecting pairs of nodes. Graphs are useful for modeling many kinds of relations. If the  * relation to be modeled is symmetric (such as "distance between cities"), that can be represented  * with an undirected graph, where an edge that connects node U to node V also connects node V to  * node U. If the relation to be modeled is asymmetric (such as "employees managed"), that can be  * represented with a directed graph, where edges are strictly one-way.  *  *<p>There are three main interfaces provided to represent graphs. In order of increasing  * complexity they are: {@link Graph}, {@link ValueGraph}, and {@link Network}. You should generally  * prefer the simplest interface that satisfies your use case.  *  *<ol>  *<li>Do you have data (objects) that you wish to associate with edges?  *<p>Yes: Go to question 2. No: Use {@link Graph}.  *<li>Are the objects you wish to associate with edges unique within the scope of a graph? That is,  *     no two objects would be {@link Object#equals(Object) equal} to each other. A common example  *     where this would<i>not</i> be the case is with weighted graphs.  *<p>Yes: Go to question 3. No: Use {@link ValueGraph}.  *<li>Do you need to be able to query the graph for an edge associated with a particular object?  *     For example, do you need to query what nodes an edge associated with a particular object  *     connects, or whether an edge associated with that object exists in the graph?  *<p>Yes: Use {@link Network}. No: Go to question 4.  *<li>Do you need explicit support for parallel edges? For example, do you need to remove one edge  *     connecting a pair of nodes while leaving other edges connecting those same nodes intact?  *<p>Yes: Use {@link Network}. No: Use {@link ValueGraph}.  *</ol>  *  *<p>Although {@link MutableValueGraph} and {@link MutableNetwork} both require users to provide  * objects to associate with edges when adding them, the differentiating factor is that in {@link  * ValueGraph}s, these objects can be any arbitrary data. Like the values in a {@link Map}, they do  * not have to be unique, and can be mutated while in the graph. In a {@link Network}, these objects  * serve as keys into the data structure. Like the keys in a {@link Map}, they must be unique, and  * cannot be mutated in a way that affects their equals/hashcode or the data structure will become  * corrupted.  *  *<p>In all three interfaces, nodes have all the same requirements as keys in a {@link Map}.  *  *<p>All mutation methods live on the subinterface {@link MutableNetwork}. If you do not need to  * mutate a network (e.g. if you write a method than runs a read-only algorithm on the network), you  * should prefer the non-mutating {@link Network} interface.  *  *<p>We provide an efficient implementation of this interface via {@link NetworkBuilder}. When  * using the implementation provided, all collection-returning methods provide live, unmodifiable  * views of the network. In other words, you cannot add an element to the collection, but if an  * element is added to the {@link Network} that would affect the collection, the collection will be  * updated automatically. This also means that you cannot mutate a {@link Network} in a way that  * would affect a collection while iterating over that collection. For example, you cannot remove  * either {@code foo} or any successors of {@code foo} from the network while iterating over {@code  * successors(foo)} (unless you first make a copy of the successors), just as you could not remove  * keys from a {@link Map} while iterating over its {@link Map#keySet()}. Behavior in such a case is  * undefined, and may result in {@link ConcurrentModificationException}.  *  *<p>Example of use:  *  *<pre><code>  * MutableNetwork<String, String> roadNetwork = NetworkBuilder.undirected().build();  * roadNetwork.addEdge("Springfield", "Shelbyville", "Monorail");  * roadNetwork.addEdge("New York", "New New York", "Applied Cryogenics");  * roadNetwork.addEdge("Springfield", "New New York", "Secret Wormhole");  * String roadToQuery = "Secret Wormhole";  * if (roadNetwork.edges().contains(roadToQuery)) {  *   EndpointPair<String> cities = roadNetwork.incidentNodes(roadToQuery);  *   System.out.format("%s and %s connected via %s", cities.nodeU(), cities.nodeV(), roadToQuery);  * }  *</code></pre>  *  * @author James Sexton  * @author Joshua O'Madadhain  * @param<N> Node parameter type  * @param<E> Edge parameter type  * @since 20.0  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|Network
specifier|public
interface|interface
name|Network
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
block|{
comment|//
comment|// Network-level accessors
comment|//
comment|/** Returns all nodes in this network, in the order specified by {@link #nodeOrder()}. */
DECL|method|nodes ()
name|Set
argument_list|<
name|N
argument_list|>
name|nodes
parameter_list|()
function_decl|;
comment|/** Returns all edges in this network, in the order specified by {@link #edgeOrder()}. */
DECL|method|edges ()
name|Set
argument_list|<
name|E
argument_list|>
name|edges
parameter_list|()
function_decl|;
comment|/**    * Returns a live view of this network as a {@link Graph}. The resulting {@link Graph} will have    * an edge connecting node A to node B iff this {@link Network} has an edge connecting A to B.    *    *<p>If this network {@link #allowsParallelEdges() allows parallel edges}, parallel edges will be    * treated as if collapsed into a single edge. For example, the {@link #degree(Object)} of a node    * in the {@link Graph} view may be less than the degree of the same node in this {@link Network}.    */
DECL|method|asGraph ()
name|Graph
argument_list|<
name|N
argument_list|>
name|asGraph
parameter_list|()
function_decl|;
comment|//
comment|// Network properties
comment|//
comment|/**    * Returns true if the edges in this network are directed. Directed edges connect a {@link    * EndpointPair#source() source node} to a {@link EndpointPair#target() target node}, while    * undirected edges connect a pair of nodes to each other.    */
DECL|method|isDirected ()
name|boolean
name|isDirected
parameter_list|()
function_decl|;
comment|/**    * Returns true if this network allows parallel edges. Attempting to add a parallel edge to a    * network that does not allow them will throw an {@link UnsupportedOperationException}.    */
DECL|method|allowsParallelEdges ()
name|boolean
name|allowsParallelEdges
parameter_list|()
function_decl|;
comment|/**    * Returns true if this network allows self-loops (edges that connect a node to itself).    * Attempting to add a self-loop to a network that does not allow them will throw an {@link    * UnsupportedOperationException}.    */
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
comment|/** Returns the order of iteration for the elements of {@link #edges()}. */
DECL|method|edgeOrder ()
name|ElementOrder
argument_list|<
name|E
argument_list|>
name|edgeOrder
parameter_list|()
function_decl|;
comment|//
comment|// Element-level accessors
comment|//
comment|/**    * Returns the nodes which have an incident edge in common with {@code node} in this network.    *    * @throws IllegalArgumentException if {@code node} is not an element of this network    */
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
comment|/**    * Returns all nodes in this network adjacent to {@code node} which can be reached by traversing    * {@code node}'s incoming edges<i>against</i> the direction (if any) of the edge.    *    *<p>In an undirected network, this is equivalent to {@link #adjacentNodes(Object)}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this network    */
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
comment|/**    * Returns all nodes in this network adjacent to {@code node} which can be reached by traversing    * {@code node}'s outgoing edges in the direction (if any) of the edge.    *    *<p>In an undirected network, this is equivalent to {@link #adjacentNodes(Object)}.    *    *<p>This is<i>not</i> the same as "all nodes reachable from {@code node} by following outgoing    * edges". For that functionality, see {@link Graphs#reachableNodes(Graph, Object)}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this network    */
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
comment|/**    * Returns the edges whose {@link #incidentNodes(Object) incident nodes} in this network include    * {@code node}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this network    */
DECL|method|incidentEdges (Object node)
name|Set
argument_list|<
name|E
argument_list|>
name|incidentEdges
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * Returns all edges in this network which can be traversed in the direction (if any) of the edge    * to end at {@code node}.    *    *<p>In a directed network, an incoming edge's {@link EndpointPair#target()} equals {@code node}.    *    *<p>In an undirected network, this is equivalent to {@link #incidentEdges(Object)}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this network    */
DECL|method|inEdges (Object node)
name|Set
argument_list|<
name|E
argument_list|>
name|inEdges
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * Returns all edges in this network which can be traversed in the direction (if any) of the edge    * starting from {@code node}.    *    *<p>In a directed network, an outgoing edge's {@link EndpointPair#source()} equals {@code node}.    *    *<p>In an undirected network, this is equivalent to {@link #incidentEdges(Object)}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this network    */
DECL|method|outEdges (Object node)
name|Set
argument_list|<
name|E
argument_list|>
name|outEdges
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * Returns the count of {@code node}'s {@link #incidentEdges(Object) incident edges}, counting    * self-loops twice (equivalently, the number of times an edge touches {@code node}).    *    *<p>For directed networks, this is equal to {@code inDegree(node) + outDegree(node)}.    *    *<p>For undirected networks, this is equal to {@code incidentEdges(node).size()} + (number of    * self-loops incident to {@code node}).    *    *<p>If the count is greater than {@code Integer.MAX_VALUE}, returns {@code Integer.MAX_VALUE}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this network    */
DECL|method|degree (Object node)
name|int
name|degree
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * Returns the count of {@code node}'s {@link #inEdges(Object) incoming edges} in a directed    * network. In an undirected network, returns the {@link #degree(Object)}.    *    *<p>If the count is greater than {@code Integer.MAX_VALUE}, returns {@code Integer.MAX_VALUE}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this network    */
DECL|method|inDegree (Object node)
name|int
name|inDegree
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * Returns the count of {@code node}'s {@link #outEdges(Object) outgoing edges} in a directed    * network. In an undirected network, returns the {@link #degree(Object)}.    *    *<p>If the count is greater than {@code Integer.MAX_VALUE}, returns {@code Integer.MAX_VALUE}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this network    */
DECL|method|outDegree (Object node)
name|int
name|outDegree
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * Returns the nodes which are the endpoints of {@code edge} in this network.    *    * @throws IllegalArgumentException if {@code edge} is not an element of this network    */
DECL|method|incidentNodes (Object edge)
name|EndpointPair
argument_list|<
name|N
argument_list|>
name|incidentNodes
parameter_list|(
name|Object
name|edge
parameter_list|)
function_decl|;
comment|/**    * Returns the edges which have an {@link #incidentNodes(Object) incident node} in common with    * {@code edge}. An edge is not considered adjacent to itself.    *    * @throws IllegalArgumentException if {@code edge} is not an element of this network    */
DECL|method|adjacentEdges (Object edge)
name|Set
argument_list|<
name|E
argument_list|>
name|adjacentEdges
parameter_list|(
name|Object
name|edge
parameter_list|)
function_decl|;
comment|/**    * Returns the set of edges directly connecting {@code nodeU} to {@code nodeV}.    *    *<p>In an undirected network, this is equal to {@code edgesConnecting(nodeV, nodeU)}.    *    *<p>The resulting set of edges will be parallel (i.e. have equal {@link #incidentNodes(Object)}.    * If this network does not {@link #allowsParallelEdges() allow parallel edges}, the resulting set    * will contain at most one edge.    *    * @throws IllegalArgumentException if {@code nodeU} or {@code nodeV} is not an element of this    *     network    */
DECL|method|edgesConnecting (Object nodeU, Object nodeV)
name|Set
argument_list|<
name|E
argument_list|>
name|edgesConnecting
parameter_list|(
name|Object
name|nodeU
parameter_list|,
name|Object
name|nodeV
parameter_list|)
function_decl|;
comment|//
comment|// Network identity
comment|//
comment|/**    * For the default {@link Network} implementations, returns true iff {@code this == object}    * (reference equality). External implementations are free to define this method as they see fit,    * as long as they satisfy the {@link Object#equals(Object)} contract.    *    *<p>To compare two {@link Network}s based on their contents rather than their references, see    * {@link Graphs#equivalent(Network, Network)}.    */
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
comment|/**    * For the default {@link Network} implementations, returns {@code System.identityHashCode(this)}.    * External implementations are free to define this method as they see fit, as long as they    * satisfy the {@link Object#hashCode()} contract.    */
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

