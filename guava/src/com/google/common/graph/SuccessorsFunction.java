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

begin_comment
comment|/**  * A functional interface for<a  * href="https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)">graph</a>-structured data.  *  *<p>This interface is meant to be used as the type of a parameter to graph algorithms (such as  * breadth first traversal) that only need a way of accessing the successors of a node in a graph.  *  *<h3>Usage</h3>  *  * Given an algorithm, for example:  *  *<pre>{@code  *   public<N> someGraphAlgorithm(N startNode, SuccessorsFunction<N> successorsFunction);  * }</pre>  *  * you will invoke it depending on the graph representation you're using.  *  *<p>If you have an instance of one of the primary {@code common.graph} types ({@link Graph},  * {@link ValueGraph}, and {@link Network}):  *  *<pre>{@code  *   someGraphAlgorithm(startNode, graph);  * }</pre>  *  * This works because those types each implement {@code SuccessorsFunction}. It will also work with  * any other implementation of this interface.  *  *<p>If you have your own graph implementation based around a custom node type {@code MyNode},  * which has a method {@code getChildren()} that retrieves its successors in a graph:  *  *<pre>{@code  *   someGraphAlgorithm(startNode, MyNode::getChildren);  * }</pre>  *  *<p>If you have some other mechanism for returning the successors of a node, or one that doesn't  * return an {@code Iterable<? extends N>}, then you can use a lambda to perform a more general  * transformation:  *  *<pre>{@code  *   someGraphAlgorithm(startNode, node -> ImmutableList.of(node.leftChild(), node.rightChild()));  * }</pre>  *  *<p>Graph algorithms that need additional capabilities (accessing both predecessors and  * successors, iterating over the edges, etc.) should declare their input to be of a type that  * provides those capabilities, such as {@link Graph}, {@link ValueGraph}, or {@link Network}.  *  *<h3>Additional documentation</h3>  *  *<p>See the Guava User Guide for the {@code common.graph} package (<a  * href="https://github.com/google/guava/wiki/GraphsExplained">"Graphs Explained"</a>) for  * additional documentation, including<a  * href="https://github.com/google/guava/wiki/GraphsExplained#notes-for-implementors">notes for  * implementors</a>  *  * @author Joshua O'Madadhain  * @author Jens Nyman  * @param<N> Node parameter type  * @since 23.0  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|SuccessorsFunction
specifier|public
interface|interface
name|SuccessorsFunction
parameter_list|<
name|N
parameter_list|>
block|{
comment|/**    * Returns all nodes in this graph adjacent to {@code node} which can be reached by traversing    * {@code node}'s outgoing edges in the direction (if any) of the edge.    *    *<p>This is<i>not</i> the same as "all nodes reachable from {@code node} by following outgoing    * edges". For that functionality, see {@link Graphs#reachableNodes(Graph, Object)}.    *    *<p>Some algorithms that operate on a {@code SuccessorsFunction} may produce undesired results    * if the returned {@link Iterable} contains duplicate elements. Implementations of such    * algorithms should document their behavior in the presence of duplicates.    *    *<p>The returned {@code iterable} and its elements, must each be non-null.    *    * @throws IllegalArgumentException if {@code node} is not an element of this graph    */
DECL|method|successors (N node)
name|Iterable
argument_list|<
name|?
extends|extends
name|N
argument_list|>
name|successors
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

