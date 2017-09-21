begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2017 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Provides methods for traversing a graph.  *  * @author Jens Nyman  * @param<N> Node parameter type  * @since 24.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|Traverser
specifier|public
specifier|abstract
class|class
name|Traverser
parameter_list|<
name|N
parameter_list|>
block|{
comment|/**    * Creates a new traverser for the given general {@code graph}.    *    *<p>If {@code graph} is known to be tree-shaped, consider using {@link    * #forTree(SuccessorsFunction)} instead.    *    *<p><b>Performance notes</b>    *    *<ul>    *<li>Traversals require<i>O(n)</i> time (where<i>n</i> is the number of nodes reachable from    *       the start node), assuming that the node objects have<i>O(1)</i> {@code equals()} and    *       {@code hashCode()} implementations.    *<li>While traversing, the traverser will use<i>O(n)</i> space (where<i>n</i> is the number    *       of nodes that have thus far been visited), plus<i>O(H)</i> space (where<i>H</i> is the    *       number of nodes that have been seen but not yet visited, that is, the "horizon").    *</ul>    *    * @param graph {@link SuccessorsFunction} representing a general graph that may have cycles.    */
DECL|method|forGraph (SuccessorsFunction<N> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|>
name|Traverser
argument_list|<
name|N
argument_list|>
name|forGraph
parameter_list|(
name|SuccessorsFunction
argument_list|<
name|N
argument_list|>
name|graph
parameter_list|)
block|{
comment|// TODO(b/27898002): Implement
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not yet implemented"
argument_list|)
throw|;
block|}
comment|/**    * Creates a new traverser for a directed acyclic graph that has at most one path from the start    * node to any node reachable from the start node, such as a tree.    *    *<p>Providing graphs that don't conform to the above description may lead to:    *    *<ul>    *<li>Traversal not terminating (if the graph has cycles)    *<li>Nodes being visited multiple times (if multiple paths exist from the start node to any    *       node reachable from it)    *</ul>    *    * In these cases, use {@link #forGraph(SuccessorsFunction)} instead.    *    *<p><b>Performance notes</b>    *    *<ul>    *<li>Traversals require<i>O(n)</i> time (where<i>n</i> is the number of nodes reachable from    *       the start node).    *<li>While traversing, the traverser will use<i>O(H)</i> space (where<i>H</i> is the number    *       of nodes that have been seen but not yet visited, that is, the "horizon").    *</ul>    *    *<p><b>Examples</b>    *    *<p>This is a valid input graph (all edges are directed facing downwards):    *    *<pre>{@code    *    a     b      c    *   / \   / \     |    *  /   \ /   \    |    * d     e     f   g    *       |    *       |    *       h    * }</pre>    *    *<p>This is<b>not</b> a valid input graph (all edges are directed facing downwards):    *    *<pre>{@code    *    a     b    *   / \   / \    *  /   \ /   \    * c     d     e    *        \   /    *         \ /    *          f    * }</pre>    *    *<p>because there are two paths from {@code b} to {@code f} ({@code b->d->f} and {@code    * b->e->f}).    *    *<p><b>Note on binary trees</b>    *    *<p>This method can be used to traverse over a binary tree. Given methods {@code    * leftChild(node)} and {@code rightChild(node)}, this method can be called as    *    *<pre>{@code    * Traverser.forTree(node -> ImmutableList.of(leftChild(node), rightChild(node)));    * }</pre>    *    * @param tree {@link SuccessorsFunction} representing a directed acyclic graph that has at most    *     one path between any two nodes    */
DECL|method|forTree (SuccessorsFunction<N> tree)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|>
name|Traverser
argument_list|<
name|N
argument_list|>
name|forTree
parameter_list|(
name|SuccessorsFunction
argument_list|<
name|N
argument_list|>
name|tree
parameter_list|)
block|{
comment|// TODO(b/27898002): Implement
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not yet implemented"
argument_list|)
throw|;
block|}
comment|/**    * Returns an unmodifiable iterable over the nodes in the graph, using breadth-first traversal.    * That is, all the nodes of depth 0 are returned, then depth 1, then 2, and so on.    *    *<p><b>Example:</b> The following graph with {@code startNode} {@code a} would return nodes in    * the order {@code abcdef} (assuming successors are returned in alphabetical order).    *    *<pre>{@code    * b ---- a ---- d    * |      |    * |      |    * e ---- c ---- f    * }</pre>    *    *<p>The behavior of this method is undefined if the nodes, or the topology of the graph, change    * while iteration is in progress.    *    *<p>The returned iterable can be iterated over multiple times. Every iterator will compute its    * next element on the fly. It is thus possible to limit the traversal to a certain number of    * nodes as follows:    *    *<pre>{@code    * Iterables.limit(Traverser.forGraph(graph).breadthFirst(node), maxNumberOfNodes);    * }</pre>    *    *<p>See<a href="https://en.wikipedia.org/wiki/Breadth-first_search">Wikipedia</a> for more    * info.    */
DECL|method|breadthFirst (N startNode)
specifier|public
specifier|abstract
name|Iterable
argument_list|<
name|N
argument_list|>
name|breadthFirst
parameter_list|(
name|N
name|startNode
parameter_list|)
function_decl|;
comment|/**    * Returns an unmodifiable iterable over the nodes in the graph, using depth-first pre-order    * traversal. That is, the nodes are returned in the order they are visited for the first time.    *    *<p><b>Example:</b> The following graph with {@code startNode} {@code a} would return nodes in    * the order {@code abcdef} (assuming successors are returned in alphabetical order).    *    *<pre>{@code    * b ---- a ---- f    * |      |    * |      |    * c ---- d ---- e    * }</pre>    *    *<p>The behavior of this method is undefined if the nodes, or the topology of the graph, change    * while iteration is in progress.    *    *<p>The returned iterable can be iterated over multiple times. Every iterator will compute its    * next element on the fly. It is thus possible to limit the traversal to a certain number of    * nodes as follows:    *    *<pre>{@code    * Iterables.limit(    *     Traverser.forGraph(graph).depthFirstPreOrder(node), maxNumberOfNodes);    * }</pre>    *    *<p>See<a href="https://en.wikipedia.org/wiki/Depth-first_search">Wikipedia</a> for more info.    */
DECL|method|depthFirstPreOrder (N startNode)
specifier|public
specifier|abstract
name|Iterable
argument_list|<
name|N
argument_list|>
name|depthFirstPreOrder
parameter_list|(
name|N
name|startNode
parameter_list|)
function_decl|;
comment|/**    * Returns an unmodifiable iterable over the nodes in the graph, using depth-first post-order    * traversal. That is, the nodes are returned in the order they are visited for the last time.    *    *<p><b>Example:</b> The following graph with {@code startNode} {@code a} would return nodes in    * the order {@code edcbfa} (assuming successors are returned in alphabetical order).    *    *<pre>{@code    * b ---- a ---- f    * |      |    * |      |    * c ---- d ---- e    * }</pre>    *    *<p>The behavior of this method is undefined if the nodes, or the topology of the graph, change    * while iteration is in progress.    *    *<p>The returned iterable can be iterated over multiple times. Every iterator will compute its    * next element on the fly. It is thus possible to limit the traversal to a certain number of    * nodes as follows:    *    *<pre>{@code    * Iterables.limit(    *     Traverser.forGraph(graph).depthFirstPostOrder(node), maxNumberOfNodes);    * }</pre>    *    *<p>See<a href="https://en.wikipedia.org/wiki/Depth-first_search">Wikipedia</a> for more info.    */
DECL|method|depthFirstPostOrder (N startNode)
specifier|public
specifier|abstract
name|Iterable
argument_list|<
name|N
argument_list|>
name|depthFirstPostOrder
parameter_list|(
name|N
name|startNode
parameter_list|)
function_decl|;
block|}
end_class

end_unit

