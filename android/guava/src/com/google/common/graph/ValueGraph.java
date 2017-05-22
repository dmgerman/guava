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
comment|/**  * An interface for<a  * href="https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)">graph</a>-structured data,  * whose edges have associated non-unique values.  *  *<p>A graph is composed of a set of nodes and a set of edges connecting pairs of nodes.  *  *<p>There are three primary interfaces provided to represent graphs. In order of increasing  * complexity they are: {@link Graph}, {@link ValueGraph}, and {@link Network}. You should generally  * prefer the simplest interface that satisfies your use case. See the<a  * href="https://github.com/google/guava/wiki/GraphsExplained#choosing-the-right-graph-type">  * "Choosing the right graph type"</a> section of the Guava User Guide for more details.  *  *<h3>Capabilities</h3>  *  *<p>{@code ValueGraph} supports the following use cases (<a  * href="https://github.com/google/guava/wiki/GraphsExplained#definitions">definitions of  * terms</a>):  *  *<ul>  *<li>directed graphs  *<li>undirected graphs  *<li>graphs that do/don't allow self-loops  *<li>graphs whose nodes/edges are insertion-ordered, sorted, or unordered  *<li>graphs whose edges have associated values  *</ul>  *  *<p>{@code ValueGraph}, as a subtype of {@code Graph}, explicitly does not support parallel edges,  * and forbids implementations or extensions with parallel edges. If you need parallel edges, use  * {@link Network}. (You can use a positive {@code Integer} edge value as a loose representation of  * edge multiplicity, but the {@code *degree()} and mutation methods will not reflect your  * interpretation of the edge value as its multiplicity.)  *  *<h3>Building a {@code ValueGraph}</h3>  *  *<p>The implementation classes that `common.graph` provides are not public, by design. To create  * an instance of one of the built-in implementations of {@code ValueGraph}, use the {@link  * ValueGraphBuilder} class:  *  *<pre>{@code  *   MutableValueGraph<Integer, Double> graph = ValueGraphBuilder.directed().build();  * }</pre>  *  *<p>{@link ValueGraphBuilder#build()} returns an instance of {@link MutableValueGraph}, which is a  * subtype of {@code ValueGraph} that provides methods for adding and removing nodes and edges. If  * you do not need to mutate a graph (e.g. if you write a method than runs a read-only algorithm on  * the graph), you should use the non-mutating {@link ValueGraph} interface, or an {@link  * ImmutableValueGraph}.  *  *<p>You can create an immutable copy of an existing {@code ValueGraph} using {@link  * ImmutableValueGraph#copyOf(ValueGraph)}:  *  *<pre>{@code  *   ImmutableValueGraph<Integer, Double> immutableGraph = ImmutableValueGraph.copyOf(graph);  * }</pre>  *  *<p>Instances of {@link ImmutableValueGraph} do not implement {@link MutableValueGraph}  * (obviously!) and are contractually guaranteed to be unmodifiable and thread-safe.  *  *<p>The Guava User Guide has<a  * href="https://github.com/google/guava/wiki/GraphsExplained#building-graph-instances">more  * information on (and examples of) building graphs</a>.  *  *<h3>Additional documentation</h3>  *  *<p>See the Guava User Guide for the {@code common.graph} package (<a  * href="https://github.com/google/guava/wiki/GraphsExplained">"Graphs Explained"</a>) for  * additional documentation, including:  *  *<ul>  *<li><a  *       href="https://github.com/google/guava/wiki/GraphsExplained#equals-hashcode-and-graph-equivalence">  *       {@code equals()}, {@code hashCode()}, and graph equivalence</a>  *<li><a href="https://github.com/google/guava/wiki/GraphsExplained#synchronization">  *       Synchronization policy</a>  *<li><a href="https://github.com/google/guava/wiki/GraphsExplained#notes-for-implementors">Notes  *       for implementors</a>  *</ul>  *  * @author James Sexton  * @author Joshua O'Madadhain  * @param<N> Node parameter type  * @param<V> Value parameter type  * @since 20.0  */
end_comment

begin_comment
comment|// TODO(b/35456940): Update the documentation to reflect the new interfaces
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|ValueGraph
specifier|public
interface|interface
name|ValueGraph
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
extends|extends
name|BaseGraph
argument_list|<
name|N
argument_list|>
block|{
comment|//
comment|// ValueGraph-level accessors
comment|//
comment|/** {@inheritDoc} */
annotation|@
name|Override
DECL|method|nodes ()
name|Set
argument_list|<
name|N
argument_list|>
name|nodes
parameter_list|()
function_decl|;
comment|/** {@inheritDoc} */
annotation|@
name|Override
DECL|method|edges ()
name|Set
argument_list|<
name|EndpointPair
argument_list|<
name|N
argument_list|>
argument_list|>
name|edges
parameter_list|()
function_decl|;
comment|/**    * Returns a live view of this graph as a {@link Graph}. The resulting {@link Graph} will have an    * edge connecting node A to node B if this {@link ValueGraph} has an edge connecting A to B.    */
DECL|method|asGraph ()
name|Graph
argument_list|<
name|N
argument_list|>
name|asGraph
parameter_list|()
function_decl|;
comment|//
comment|// ValueGraph properties
comment|//
comment|/** {@inheritDoc} */
annotation|@
name|Override
DECL|method|isDirected ()
name|boolean
name|isDirected
parameter_list|()
function_decl|;
comment|/** {@inheritDoc} */
annotation|@
name|Override
DECL|method|allowsSelfLoops ()
name|boolean
name|allowsSelfLoops
parameter_list|()
function_decl|;
comment|/** {@inheritDoc} */
annotation|@
name|Override
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
comment|/** {@inheritDoc} */
annotation|@
name|Override
DECL|method|adjacentNodes (N node)
name|Set
argument_list|<
name|N
argument_list|>
name|adjacentNodes
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
comment|/** {@inheritDoc} */
annotation|@
name|Override
DECL|method|predecessors (N node)
name|Set
argument_list|<
name|N
argument_list|>
name|predecessors
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
comment|/** {@inheritDoc} */
annotation|@
name|Override
DECL|method|successors (N node)
name|Set
argument_list|<
name|N
argument_list|>
name|successors
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
comment|/** {@inheritDoc} */
annotation|@
name|Override
DECL|method|degree (N node)
name|int
name|degree
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
comment|/** {@inheritDoc} */
annotation|@
name|Override
DECL|method|inDegree (N node)
name|int
name|inDegree
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
comment|/** {@inheritDoc} */
annotation|@
name|Override
DECL|method|outDegree (N node)
name|int
name|outDegree
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
comment|/**    * If there is an edge connecting {@code nodeU} to {@code nodeV}, returns the non-null value    * associated with that edge.    *    *<p>In an undirected graph, this is equal to {@code edgeValue(nodeV, nodeU)}.    *    * @throws IllegalArgumentException if there is no edge connecting {@code nodeU} to {@code nodeV}.    */
DECL|method|edgeValue (N nodeU, N nodeV)
name|V
name|edgeValue
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
function_decl|;
comment|/**    * If there is an edge connecting {@code nodeU} to {@code nodeV}, returns the non-null value    * associated with that edge; otherwise, returns {@code defaultValue}.    *    *<p>In an undirected graph, this is equal to {@code edgeValueOrDefault(nodeV, nodeU,    * defaultValue)}.    */
DECL|method|edgeValueOrDefault (N nodeU, N nodeV, @Nullable V defaultValue)
name|V
name|edgeValueOrDefault
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|,
annotation|@
name|Nullable
name|V
name|defaultValue
parameter_list|)
function_decl|;
comment|//
comment|// ValueGraph identity
comment|//
comment|/**    * Returns {@code true} iff {@code object} is a {@link ValueGraph} that has the same elements and    * the same structural relationships as those in this graph.    *    *<p>Thus, two value graphs A and B are equal if<b>all</b> of the following are true:    *    *<ul>    *<li>A and B have equal {@link #isDirected() directedness}.    *<li>A and B have equal {@link #nodes() node sets}.    *<li>A and B have equal {@link #edges() edge sets}.    *<li>Every edge in A and B are associated with equal {@link #edgeValue(Object, Object) values}.    *</ul>    *    *<p>Graph properties besides {@link #isDirected() directedness} do<b>not</b> affect equality.    * For example, two graphs may be considered equal even if one allows self-loops and the other    * doesn't. Additionally, the order in which nodes or edges are added to the graph, and the order    * in which they are iterated over, are irrelevant.    *    *<p>A reference implementation of this is provided by {@link AbstractValueGraph#equals(Object)}.    */
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
comment|/**    * Returns the hash code for this graph. The hash code of a graph is defined as the hash code of a    * map from each of its {@link #edges() edges} to the associated {@link #edgeValue(Object, Object)    * edge value}.    *    *<p>A reference implementation of this is provided by {@link AbstractValueGraph#hashCode()}.    */
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

