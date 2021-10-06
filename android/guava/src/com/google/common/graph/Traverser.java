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
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|collect
operator|.
name|AbstractIterator
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
name|errorprone
operator|.
name|annotations
operator|.
name|DoNotMock
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
name|Deque
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
name|CheckForNull
import|;
end_import

begin_comment
comment|/**  * An object that can traverse the nodes that are reachable from a specified (set of) start node(s)  * using a specified {@link SuccessorsFunction}.  *  *<p>There are two entry points for creating a {@code Traverser}: {@link  * #forTree(SuccessorsFunction)} and {@link #forGraph(SuccessorsFunction)}. You should choose one  * based on your answers to the following questions:  *  *<ol>  *<li>Is there only one path to any node that's reachable from any start node? (If so, the graph  *       to be traversed is a tree or forest even if it is a subgraph of a graph which is neither.)  *<li>Are the node objects' implementations of {@code equals()}/{@code hashCode()}<a  *       href="https://github.com/google/guava/wiki/GraphsExplained#non-recursiveness">recursive</a>?  *</ol>  *  *<p>If your answers are:  *  *<ul>  *<li>(1) "no" and (2) "no", use {@link #forGraph(SuccessorsFunction)}.  *<li>(1) "yes" and (2) "yes", use {@link #forTree(SuccessorsFunction)}.  *<li>(1) "yes" and (2) "no", you can use either, but {@code forTree()} will be more efficient.  *<li>(1) "no" and (2) "yes",<b><i>neither will work</i></b>, but if you transform your node  *       objects into a non-recursive form, you can use {@code forGraph()}.  *</ul>  *  * @author Jens Nyman  * @param<N> Node parameter type  * @since 23.1  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|DoNotMock
argument_list|(
literal|"Call forGraph or forTree, passing a lambda or a Graph with the desired edges (built with"
operator|+
literal|" GraphBuilder)"
argument_list|)
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|Traverser
specifier|public
specifier|abstract
class|class
name|Traverser
parameter_list|<
name|N
parameter_list|>
block|{
DECL|field|successorFunction
specifier|private
specifier|final
name|SuccessorsFunction
argument_list|<
name|N
argument_list|>
name|successorFunction
decl_stmt|;
DECL|method|Traverser (SuccessorsFunction<N> successorFunction)
specifier|private
name|Traverser
parameter_list|(
name|SuccessorsFunction
argument_list|<
name|N
argument_list|>
name|successorFunction
parameter_list|)
block|{
name|this
operator|.
name|successorFunction
operator|=
name|checkNotNull
argument_list|(
name|successorFunction
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates a new traverser for the given general {@code graph}.    *    *<p>Traversers created using this method are guaranteed to visit each node reachable from the    * start node(s) at most once.    *    *<p>If you know that no node in {@code graph} is reachable by more than one path from the start    * node(s), consider using {@link #forTree(SuccessorsFunction)} instead.    *    *<p><b>Performance notes</b>    *    *<ul>    *<li>Traversals require<i>O(n)</i> time (where<i>n</i> is the number of nodes reachable from    *       the start node), assuming that the node objects have<i>O(1)</i> {@code equals()} and    *       {@code hashCode()} implementations. (See the<a    *       href="https://github.com/google/guava/wiki/GraphsExplained#elements-must-be-useable-as-map-keys">    *       notes on element objects</a> for more information.)    *<li>While traversing, the traverser will use<i>O(n)</i> space (where<i>n</i> is the number    *       of nodes that have thus far been visited), plus<i>O(H)</i> space (where<i>H</i> is the    *       number of nodes that have been seen but not yet visited, that is, the "horizon").    *</ul>    *    * @param graph {@link SuccessorsFunction} representing a general graph that may have cycles.    */
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
return|return
operator|new
name|Traverser
argument_list|<
name|N
argument_list|>
argument_list|(
name|graph
argument_list|)
block|{
annotation|@
name|Override
name|Traversal
argument_list|<
name|N
argument_list|>
name|newTraversal
parameter_list|()
block|{
return|return
name|Traversal
operator|.
name|inGraph
argument_list|(
name|graph
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**    * Creates a new traverser for a directed acyclic graph that has at most one path from the start    * node(s) to any node reachable from the start node(s), and has no paths from any start node to    * any other start node, such as a tree or forest.    *    *<p>{@code forTree()} is especially useful (versus {@code forGraph()}) in cases where the data    * structure being traversed is, in addition to being a tree/forest, also defined<a    * href="https://github.com/google/guava/wiki/GraphsExplained#non-recursiveness">recursively</a>.    * This is because the {@code forTree()}-based implementations don't keep track of visited nodes,    * and therefore don't need to call `equals()` or `hashCode()` on the node objects; this saves    * both time and space versus traversing the same graph using {@code forGraph()}.    *    *<p>Providing a graph to be traversed for which there is more than one path from the start    * node(s) to any node may lead to:    *    *<ul>    *<li>Traversal not terminating (if the graph has cycles)    *<li>Nodes being visited multiple times (if multiple paths exist from any start node to any    *       node reachable from any start node)    *</ul>    *    *<p><b>Performance notes</b>    *    *<ul>    *<li>Traversals require<i>O(n)</i> time (where<i>n</i> is the number of nodes reachable from    *       the start node).    *<li>While traversing, the traverser will use<i>O(H)</i> space (where<i>H</i> is the number    *       of nodes that have been seen but not yet visited, that is, the "horizon").    *</ul>    *    *<p><b>Examples</b> (all edges are directed facing downwards)    *    *<p>The graph below would be valid input with start nodes of {@code a, f, c}. However, if {@code    * b} were<i>also</i> a start node, then there would be multiple paths to reach {@code e} and    * {@code h}.    *    *<pre>{@code    *    a     b      c    *   / \   / \     |    *  /   \ /   \    |    * d     e     f   g    *       |    *       |    *       h    * }</pre>    *    *<p>.    *    *<p>The graph below would be a valid input with start nodes of {@code a, f}. However, if {@code    * b} were a start node, there would be multiple paths to {@code f}.    *    *<pre>{@code    *    a     b    *   / \   / \    *  /   \ /   \    * c     d     e    *        \   /    *         \ /    *          f    * }</pre>    *    *<p><b>Note on binary trees</b>    *    *<p>This method can be used to traverse over a binary tree. Given methods {@code    * leftChild(node)} and {@code rightChild(node)}, this method can be called as    *    *<pre>{@code    * Traverser.forTree(node -> ImmutableList.of(leftChild(node), rightChild(node)));    * }</pre>    *    * @param tree {@link SuccessorsFunction} representing a directed acyclic graph that has at most    *     one path between any two nodes    */
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
if|if
condition|(
name|tree
operator|instanceof
name|BaseGraph
condition|)
block|{
name|checkArgument
argument_list|(
operator|(
operator|(
name|BaseGraph
argument_list|<
name|?
argument_list|>
operator|)
name|tree
operator|)
operator|.
name|isDirected
argument_list|()
argument_list|,
literal|"Undirected graphs can never be trees."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tree
operator|instanceof
name|Network
condition|)
block|{
name|checkArgument
argument_list|(
operator|(
operator|(
name|Network
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|tree
operator|)
operator|.
name|isDirected
argument_list|()
argument_list|,
literal|"Undirected networks can never be trees."
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|Traverser
argument_list|<
name|N
argument_list|>
argument_list|(
name|tree
argument_list|)
block|{
annotation|@
name|Override
name|Traversal
argument_list|<
name|N
argument_list|>
name|newTraversal
parameter_list|()
block|{
return|return
name|Traversal
operator|.
name|inTree
argument_list|(
name|tree
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**    * Returns an unmodifiable {@code Iterable} over the nodes reachable from {@code startNode}, in    * the order of a breadth-first traversal. That is, all the nodes of depth 0 are returned, then    * depth 1, then 2, and so on.    *    *<p><b>Example:</b> The following graph with {@code startNode} {@code a} would return nodes in    * the order {@code abcdef} (assuming successors are returned in alphabetical order).    *    *<pre>{@code    * b ---- a ---- d    * |      |    * |      |    * e ---- c ---- f    * }</pre>    *    *<p>The behavior of this method is undefined if the nodes, or the topology of the graph, change    * while iteration is in progress.    *    *<p>The returned {@code Iterable} can be iterated over multiple times. Every iterator will    * compute its next element on the fly. It is thus possible to limit the traversal to a certain    * number of nodes as follows:    *    *<pre>{@code    * Iterables.limit(Traverser.forGraph(graph).breadthFirst(node), maxNumberOfNodes);    * }</pre>    *    *<p>See<a href="https://en.wikipedia.org/wiki/Breadth-first_search">Wikipedia</a> for more    * info.    *    * @throws IllegalArgumentException if {@code startNode} is not an element of the graph    */
DECL|method|breadthFirst (N startNode)
specifier|public
specifier|final
name|Iterable
argument_list|<
name|N
argument_list|>
name|breadthFirst
parameter_list|(
name|N
name|startNode
parameter_list|)
block|{
return|return
name|breadthFirst
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|startNode
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns an unmodifiable {@code Iterable} over the nodes reachable from any of the {@code    * startNodes}, in the order of a breadth-first traversal. This is equivalent to a breadth-first    * traversal of a graph with an additional root node whose successors are the listed {@code    * startNodes}.    *    * @throws IllegalArgumentException if any of {@code startNodes} is not an element of the graph    * @see #breadthFirst(Object)    * @since 24.1    */
DECL|method|breadthFirst (Iterable<? extends N> startNodes)
specifier|public
specifier|final
name|Iterable
argument_list|<
name|N
argument_list|>
name|breadthFirst
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|N
argument_list|>
name|startNodes
parameter_list|)
block|{
name|ImmutableSet
argument_list|<
name|N
argument_list|>
name|validated
init|=
name|validate
argument_list|(
name|startNodes
argument_list|)
decl_stmt|;
return|return
operator|new
name|Iterable
argument_list|<
name|N
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|N
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|newTraversal
argument_list|()
operator|.
name|breadthFirst
argument_list|(
name|validated
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**    * Returns an unmodifiable {@code Iterable} over the nodes reachable from {@code startNode}, in    * the order of a depth-first pre-order traversal. "Pre-order" implies that nodes appear in the    * {@code Iterable} in the order in which they are first visited.    *    *<p><b>Example:</b> The following graph with {@code startNode} {@code a} would return nodes in    * the order {@code abecfd} (assuming successors are returned in alphabetical order).    *    *<pre>{@code    * b ---- a ---- d    * |      |    * |      |    * e ---- c ---- f    * }</pre>    *    *<p>The behavior of this method is undefined if the nodes, or the topology of the graph, change    * while iteration is in progress.    *    *<p>The returned {@code Iterable} can be iterated over multiple times. Every iterator will    * compute its next element on the fly. It is thus possible to limit the traversal to a certain    * number of nodes as follows:    *    *<pre>{@code    * Iterables.limit(    *     Traverser.forGraph(graph).depthFirstPreOrder(node), maxNumberOfNodes);    * }</pre>    *    *<p>See<a href="https://en.wikipedia.org/wiki/Depth-first_search">Wikipedia</a> for more info.    *    * @throws IllegalArgumentException if {@code startNode} is not an element of the graph    */
DECL|method|depthFirstPreOrder (N startNode)
specifier|public
specifier|final
name|Iterable
argument_list|<
name|N
argument_list|>
name|depthFirstPreOrder
parameter_list|(
name|N
name|startNode
parameter_list|)
block|{
return|return
name|depthFirstPreOrder
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|startNode
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns an unmodifiable {@code Iterable} over the nodes reachable from any of the {@code    * startNodes}, in the order of a depth-first pre-order traversal. This is equivalent to a    * depth-first pre-order traversal of a graph with an additional root node whose successors are    * the listed {@code startNodes}.    *    * @throws IllegalArgumentException if any of {@code startNodes} is not an element of the graph    * @see #depthFirstPreOrder(Object)    * @since 24.1    */
DECL|method|depthFirstPreOrder (Iterable<? extends N> startNodes)
specifier|public
specifier|final
name|Iterable
argument_list|<
name|N
argument_list|>
name|depthFirstPreOrder
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|N
argument_list|>
name|startNodes
parameter_list|)
block|{
name|ImmutableSet
argument_list|<
name|N
argument_list|>
name|validated
init|=
name|validate
argument_list|(
name|startNodes
argument_list|)
decl_stmt|;
return|return
operator|new
name|Iterable
argument_list|<
name|N
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|N
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|newTraversal
argument_list|()
operator|.
name|preOrder
argument_list|(
name|validated
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**    * Returns an unmodifiable {@code Iterable} over the nodes reachable from {@code startNode}, in    * the order of a depth-first post-order traversal. "Post-order" implies that nodes appear in the    * {@code Iterable} in the order in which they are visited for the last time.    *    *<p><b>Example:</b> The following graph with {@code startNode} {@code a} would return nodes in    * the order {@code fcebda} (assuming successors are returned in alphabetical order).    *    *<pre>{@code    * b ---- a ---- d    * |      |    * |      |    * e ---- c ---- f    * }</pre>    *    *<p>The behavior of this method is undefined if the nodes, or the topology of the graph, change    * while iteration is in progress.    *    *<p>The returned {@code Iterable} can be iterated over multiple times. Every iterator will    * compute its next element on the fly. It is thus possible to limit the traversal to a certain    * number of nodes as follows:    *    *<pre>{@code    * Iterables.limit(    *     Traverser.forGraph(graph).depthFirstPostOrder(node), maxNumberOfNodes);    * }</pre>    *    *<p>See<a href="https://en.wikipedia.org/wiki/Depth-first_search">Wikipedia</a> for more info.    *    * @throws IllegalArgumentException if {@code startNode} is not an element of the graph    */
DECL|method|depthFirstPostOrder (N startNode)
specifier|public
specifier|final
name|Iterable
argument_list|<
name|N
argument_list|>
name|depthFirstPostOrder
parameter_list|(
name|N
name|startNode
parameter_list|)
block|{
return|return
name|depthFirstPostOrder
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|startNode
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns an unmodifiable {@code Iterable} over the nodes reachable from any of the {@code    * startNodes}, in the order of a depth-first post-order traversal. This is equivalent to a    * depth-first post-order traversal of a graph with an additional root node whose successors are    * the listed {@code startNodes}.    *    * @throws IllegalArgumentException if any of {@code startNodes} is not an element of the graph    * @see #depthFirstPostOrder(Object)    * @since 24.1    */
DECL|method|depthFirstPostOrder (Iterable<? extends N> startNodes)
specifier|public
specifier|final
name|Iterable
argument_list|<
name|N
argument_list|>
name|depthFirstPostOrder
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|N
argument_list|>
name|startNodes
parameter_list|)
block|{
name|ImmutableSet
argument_list|<
name|N
argument_list|>
name|validated
init|=
name|validate
argument_list|(
name|startNodes
argument_list|)
decl_stmt|;
return|return
operator|new
name|Iterable
argument_list|<
name|N
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|N
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|newTraversal
argument_list|()
operator|.
name|postOrder
argument_list|(
name|validated
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|newTraversal ()
specifier|abstract
name|Traversal
argument_list|<
name|N
argument_list|>
name|newTraversal
parameter_list|()
function_decl|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"CheckReturnValue"
argument_list|)
DECL|method|validate (Iterable<? extends N> startNodes)
specifier|private
name|ImmutableSet
argument_list|<
name|N
argument_list|>
name|validate
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|N
argument_list|>
name|startNodes
parameter_list|)
block|{
name|ImmutableSet
argument_list|<
name|N
argument_list|>
name|copy
init|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|startNodes
argument_list|)
decl_stmt|;
for|for
control|(
name|N
name|node
range|:
name|copy
control|)
block|{
name|successorFunction
operator|.
name|successors
argument_list|(
name|node
argument_list|)
expr_stmt|;
comment|// Will throw if node doesn't exist
block|}
return|return
name|copy
return|;
block|}
comment|/**    * Abstracts away the difference between traversing a graph vs. a tree. For a tree, we just take    * the next element from the next non-empty iterator; for graph, we need to loop through the next    * non-empty iterator to find first unvisited node.    */
DECL|class|Traversal
specifier|private
specifier|abstract
specifier|static
class|class
name|Traversal
parameter_list|<
name|N
parameter_list|>
block|{
DECL|field|successorFunction
specifier|final
name|SuccessorsFunction
argument_list|<
name|N
argument_list|>
name|successorFunction
decl_stmt|;
DECL|method|Traversal (SuccessorsFunction<N> successorFunction)
name|Traversal
parameter_list|(
name|SuccessorsFunction
argument_list|<
name|N
argument_list|>
name|successorFunction
parameter_list|)
block|{
name|this
operator|.
name|successorFunction
operator|=
name|successorFunction
expr_stmt|;
block|}
DECL|method|inGraph (SuccessorsFunction<N> graph)
specifier|static
parameter_list|<
name|N
parameter_list|>
name|Traversal
argument_list|<
name|N
argument_list|>
name|inGraph
parameter_list|(
name|SuccessorsFunction
argument_list|<
name|N
argument_list|>
name|graph
parameter_list|)
block|{
name|Set
argument_list|<
name|N
argument_list|>
name|visited
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
return|return
operator|new
name|Traversal
argument_list|<
name|N
argument_list|>
argument_list|(
name|graph
argument_list|)
block|{
annotation|@
name|Override
annotation|@
name|CheckForNull
name|N
name|visitNext
parameter_list|(
name|Deque
argument_list|<
name|Iterator
argument_list|<
name|?
extends|extends
name|N
argument_list|>
argument_list|>
name|horizon
parameter_list|)
block|{
name|Iterator
argument_list|<
name|?
extends|extends
name|N
argument_list|>
name|top
init|=
name|horizon
operator|.
name|getFirst
argument_list|()
decl_stmt|;
while|while
condition|(
name|top
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|N
name|element
init|=
name|top
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// requireNonNull is safe because horizon contains only graph nodes.
comment|/*              * TODO(cpovirk): Replace these two statements with one (`N element =              * requireNonNull(top.next())`) once our checker supports it.              *              * (The problem is likely              * https://github.com/jspecify/nullness-checker-for-checker-framework/blob/61aafa4ae52594830cfc2d61c8b113009dbdb045/src/main/java/com/google/jspecify/nullness/NullSpecAnnotatedTypeFactory.java#L896)              */
name|requireNonNull
argument_list|(
name|element
argument_list|)
expr_stmt|;
if|if
condition|(
name|visited
operator|.
name|add
argument_list|(
name|element
argument_list|)
condition|)
block|{
return|return
name|element
return|;
block|}
block|}
name|horizon
operator|.
name|removeFirst
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
return|;
block|}
DECL|method|inTree (SuccessorsFunction<N> tree)
specifier|static
parameter_list|<
name|N
parameter_list|>
name|Traversal
argument_list|<
name|N
argument_list|>
name|inTree
parameter_list|(
name|SuccessorsFunction
argument_list|<
name|N
argument_list|>
name|tree
parameter_list|)
block|{
return|return
operator|new
name|Traversal
argument_list|<
name|N
argument_list|>
argument_list|(
name|tree
argument_list|)
block|{
annotation|@
name|CheckForNull
annotation|@
name|Override
name|N
name|visitNext
parameter_list|(
name|Deque
argument_list|<
name|Iterator
argument_list|<
name|?
extends|extends
name|N
argument_list|>
argument_list|>
name|horizon
parameter_list|)
block|{
name|Iterator
argument_list|<
name|?
extends|extends
name|N
argument_list|>
name|top
init|=
name|horizon
operator|.
name|getFirst
argument_list|()
decl_stmt|;
if|if
condition|(
name|top
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
name|checkNotNull
argument_list|(
name|top
operator|.
name|next
argument_list|()
argument_list|)
return|;
block|}
name|horizon
operator|.
name|removeFirst
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
return|;
block|}
DECL|method|breadthFirst (Iterator<? extends N> startNodes)
specifier|final
name|Iterator
argument_list|<
name|N
argument_list|>
name|breadthFirst
parameter_list|(
name|Iterator
argument_list|<
name|?
extends|extends
name|N
argument_list|>
name|startNodes
parameter_list|)
block|{
return|return
name|topDown
argument_list|(
name|startNodes
argument_list|,
name|InsertionOrder
operator|.
name|BACK
argument_list|)
return|;
block|}
DECL|method|preOrder (Iterator<? extends N> startNodes)
specifier|final
name|Iterator
argument_list|<
name|N
argument_list|>
name|preOrder
parameter_list|(
name|Iterator
argument_list|<
name|?
extends|extends
name|N
argument_list|>
name|startNodes
parameter_list|)
block|{
return|return
name|topDown
argument_list|(
name|startNodes
argument_list|,
name|InsertionOrder
operator|.
name|FRONT
argument_list|)
return|;
block|}
comment|/**      * In top-down traversal, an ancestor node is always traversed before any of its descendant      * nodes. The traversal order among descendant nodes (particularly aunts and nieces) are      * determined by the {@code InsertionOrder} parameter: nieces are placed at the FRONT before      * aunts for pre-order; while in BFS they are placed at the BACK after aunts.      */
DECL|method|topDown (Iterator<? extends N> startNodes, InsertionOrder order)
specifier|private
name|Iterator
argument_list|<
name|N
argument_list|>
name|topDown
parameter_list|(
name|Iterator
argument_list|<
name|?
extends|extends
name|N
argument_list|>
name|startNodes
parameter_list|,
name|InsertionOrder
name|order
parameter_list|)
block|{
name|Deque
argument_list|<
name|Iterator
argument_list|<
name|?
extends|extends
name|N
argument_list|>
argument_list|>
name|horizon
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
name|horizon
operator|.
name|add
argument_list|(
name|startNodes
argument_list|)
expr_stmt|;
return|return
operator|new
name|AbstractIterator
argument_list|<
name|N
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
annotation|@
name|CheckForNull
specifier|protected
name|N
name|computeNext
parameter_list|()
block|{
do|do
block|{
name|N
name|next
init|=
name|visitNext
argument_list|(
name|horizon
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|?
extends|extends
name|N
argument_list|>
name|successors
init|=
name|successorFunction
operator|.
name|successors
argument_list|(
name|next
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|successors
operator|.
name|hasNext
argument_list|()
condition|)
block|{
comment|// BFS: horizon.addLast(successors)
comment|// Pre-order: horizon.addFirst(successors)
name|order
operator|.
name|insertInto
argument_list|(
name|horizon
argument_list|,
name|successors
argument_list|)
expr_stmt|;
block|}
return|return
name|next
return|;
block|}
block|}
do|while
condition|(
operator|!
name|horizon
operator|.
name|isEmpty
argument_list|()
condition|)
do|;
return|return
name|endOfData
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|postOrder (Iterator<? extends N> startNodes)
specifier|final
name|Iterator
argument_list|<
name|N
argument_list|>
name|postOrder
parameter_list|(
name|Iterator
argument_list|<
name|?
extends|extends
name|N
argument_list|>
name|startNodes
parameter_list|)
block|{
name|Deque
argument_list|<
name|N
argument_list|>
name|ancestorStack
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
name|Deque
argument_list|<
name|Iterator
argument_list|<
name|?
extends|extends
name|N
argument_list|>
argument_list|>
name|horizon
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
name|horizon
operator|.
name|add
argument_list|(
name|startNodes
argument_list|)
expr_stmt|;
return|return
operator|new
name|AbstractIterator
argument_list|<
name|N
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
annotation|@
name|CheckForNull
specifier|protected
name|N
name|computeNext
parameter_list|()
block|{
for|for
control|(
name|N
name|next
init|=
name|visitNext
argument_list|(
name|horizon
argument_list|)
init|;
name|next
operator|!=
literal|null
condition|;
name|next
operator|=
name|visitNext
argument_list|(
name|horizon
argument_list|)
control|)
block|{
name|Iterator
argument_list|<
name|?
extends|extends
name|N
argument_list|>
name|successors
init|=
name|successorFunction
operator|.
name|successors
argument_list|(
name|next
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|successors
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
name|next
return|;
block|}
name|horizon
operator|.
name|addFirst
argument_list|(
name|successors
argument_list|)
expr_stmt|;
name|ancestorStack
operator|.
name|push
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
comment|// TODO(b/192579700): Use a ternary once it no longer confuses our nullness checker.
if|if
condition|(
operator|!
name|ancestorStack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|ancestorStack
operator|.
name|pop
argument_list|()
return|;
block|}
return|return
name|endOfData
argument_list|()
return|;
block|}
block|}
return|;
block|}
comment|/**      * Visits the next node from the top iterator of {@code horizon} and returns the visited node.      * Null is returned to indicate reaching the end of the top iterator.      *      *<p>For example, if horizon is {@code [[a, b], [c, d], [e]]}, {@code visitNext()} will return      * {@code [a, b, null, c, d, null, e, null]} sequentially, encoding the topological structure.      * (Note, however, that the callers of {@code visitNext()} often insert additional iterators      * into {@code horizon} between calls to {@code visitNext()}. This causes them to receive      * additional values interleaved with those shown above.)      */
annotation|@
name|CheckForNull
DECL|method|visitNext (Deque<Iterator<? extends N>> horizon)
specifier|abstract
name|N
name|visitNext
parameter_list|(
name|Deque
argument_list|<
name|Iterator
argument_list|<
name|?
extends|extends
name|N
argument_list|>
argument_list|>
name|horizon
parameter_list|)
function_decl|;
block|}
comment|/** Poor man's method reference for {@code Deque::addFirst} and {@code Deque::addLast}. */
DECL|enum|InsertionOrder
specifier|private
enum|enum
name|InsertionOrder
block|{
DECL|enumConstant|FRONT
name|FRONT
block|{
annotation|@
name|Override
argument_list|<
name|T
argument_list|>
name|void
name|insertInto
parameter_list|(
name|Deque
argument_list|<
name|T
argument_list|>
name|deque
parameter_list|,
name|T
name|value
parameter_list|)
block|{
name|deque
operator|.
name|addFirst
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|,
DECL|enumConstant|BACK
name|BACK
block|{
annotation|@
name|Override
argument_list|<
name|T
argument_list|>
name|void
name|insertInto
parameter_list|(
name|Deque
argument_list|<
name|T
argument_list|>
name|deque
parameter_list|,
name|T
name|value
parameter_list|)
block|{
name|deque
operator|.
name|addLast
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|;
DECL|method|insertInto (Deque<T> deque, T value)
specifier|abstract
parameter_list|<
name|T
parameter_list|>
name|void
name|insertInto
parameter_list|(
name|Deque
argument_list|<
name|T
argument_list|>
name|deque
parameter_list|,
name|T
name|value
parameter_list|)
function_decl|;
block|}
block|}
end_class

end_unit

