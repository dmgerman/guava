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
comment|/**  * An interface for<a  * href="https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)">graph</a>-structured data,  * whose edges are anonymous entities with no identity or information of their own.  *  *<p>A graph is composed of a set of nodes and a set of edges connecting pairs of nodes.  *  *<p>There are three primary interfaces provided to represent graphs. In order of increasing  * complexity they are: {@link Graph}, {@link ValueGraph}, and {@link Network}. You should generally  * prefer the simplest interface that satisfies your use case. See the<a  * href="https://github.com/google/guava/wiki/GraphsExplained#choosing-the-right-graph-type">  * "Choosing the right graph type"</a> section of the Guava User Guide for more details.  *  *<h3>Capabilities</h3>  *  *<p>{@code Graph} supports the following use cases (<a  * href="https://github.com/google/guava/wiki/GraphsExplained#definitions">definitions of  * terms</a>):  *  *<ul>  *<li>directed graphs  *<li>undirected graphs  *<li>graphs that do/don't allow self-loops  *<li>graphs whose nodes/edges are insertion-ordered, sorted, or unordered  *</ul>  *  *<p>{@code Graph} explicitly does not support parallel edges, and forbids implementations or  * extensions with parallel edges. If you need parallel edges, use {@link Network}.  *  *<h3>Building a {@code Graph}</h3>  *  *<p>The implementation classes that `common.graph` provides are not public, by design. To create  * an instance of one of the built-in implementations of {@code Graph}, use the {@link GraphBuilder}  * class:  *  *<pre>{@code  *   MutableGraph<Integer> graph = GraphBuilder.undirected().build();  * }</pre>  *  *<p>{@link GraphBuilder#build()} returns an instance of {@link MutableGraph}, which is a subtype  * of {@code Graph} that provides methods for adding and removing nodes and edges. If you do not  * need to mutate a graph (e.g. if you write a method than runs a read-only algorithm on the graph),  * you should use the non-mutating {@link Graph} interface, or an {@link ImmutableGraph}.  *  *<p>You can create an immutable copy of an existing {@code Graph} using {@link  * ImmutableGraph#copyOf(Graph)}:  *  *<pre>{@code  *   ImmutableGraph<Integer> immutableGraph = ImmutableGraph.copyOf(graph);  * }</pre>  *  *<p>Instances of {@link ImmutableGraph} do not implement {@link MutableGraph} (obviously!) and are  * contractually guaranteed to be unmodifiable and thread-safe.  *  *<p>The Guava User Guide has<a  * href="https://github.com/google/guava/wiki/GraphsExplained#building-graph-instances">more  * information on (and examples of) building graphs</a>.  *  *<h3>Additional documentation</h3>  *  *<p>See the Guava User Guide for the {@code common.graph} package (<a  * href="https://github.com/google/guava/wiki/GraphsExplained">"Graphs Explained"</a>) for  * additional documentation, including:  *  *<ul>  *<li><a  *       href="https://github.com/google/guava/wiki/GraphsExplained#equals-hashcode-and-graph-equivalence">  *       {@code equals()}, {@code hashCode()}, and graph equivalence</a>  *<li><a href="https://github.com/google/guava/wiki/GraphsExplained#synchronization">  *       Synchronization policy</a>  *<li><a href="https://github.com/google/guava/wiki/GraphsExplained#notes-for-implementors">Notes  *       for implementors</a>  *</ul>  *  * @author James Sexton  * @author Joshua O'Madadhain  * @param<N> Node parameter type  * @since 20.0  */
end_comment

begin_comment
comment|// TODO(b/35456940): Update the documentation to reflect the new interfaces
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
parameter_list|>
extends|extends
name|BaseGraph
argument_list|<
name|N
argument_list|>
block|{
comment|//
comment|// Graph-level accessors
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
comment|//
comment|// Graph properties
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
comment|/** {@inheritDoc} */
annotation|@
name|Override
DECL|method|hasEdge (N nodeU, N nodeV)
name|boolean
name|hasEdge
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
function_decl|;
comment|//
comment|// Graph identity
comment|//
comment|/**    * Returns {@code true} iff {@code object} is a {@link Graph} that has the same elements and the    * same structural relationships as those in this graph.    *    *<p>Thus, two graphs A and B are equal if<b>all</b> of the following are true:    *    *<ul>    *<li>A and B have equal {@link #isDirected() directedness}.    *<li>A and B have equal {@link #nodes() node sets}.    *<li>A and B have equal {@link #edges() edge sets}.    *</ul>    *    *<p>Graph properties besides {@link #isDirected() directedness} do<b>not</b> affect equality.    * For example, two graphs may be considered equal even if one allows self-loops and the other    * doesn't. Additionally, the order in which nodes or edges are added to the graph, and the order    * in which they are iterated over, are irrelevant.    *    *<p>A reference implementation of this is provided by {@link AbstractGraph#equals(Object)}.    */
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
comment|/**    * Returns the hash code for this graph. The hash code of a graph is defined as the hash code of    * the set returned by {@link #edges()}.    *    *<p>A reference implementation of this is provided by {@link AbstractGraph#hashCode()}.    */
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
