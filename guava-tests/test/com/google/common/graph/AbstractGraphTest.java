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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
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
name|CanIgnoreReturnValue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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

begin_comment
comment|/**  * Abstract base class for testing implementations of {@link Graph} interface. Graph  * instances created for testing should have Integer node and String edge objects.  *  *<p>Tests assume the following about the graph implementation:  *<ul>  *<li>Parallel edges are not allowed.  *</ul>  *  *<p>Test cases that should be handled similarly in any graph implementation are  * included in this class. For example, testing that {@code nodes()} method returns  * the set of the nodes in the graph. The following test cases are left for the subclasses  * to handle:  *<ul>  *<li>Test cases related to whether the graph is directed, undirected, mutable,  *     or immutable.  *<li>Test cases related to the specific implementation of the {@link Graph} interface.  *</ul>  *  * TODO(user): Make this class generic (using<N, E>) for all node and edge types.  * TODO(user): Differentiate between directed and undirected edge strings.  */
end_comment

begin_class
DECL|class|AbstractGraphTest
specifier|public
specifier|abstract
class|class
name|AbstractGraphTest
block|{
DECL|field|graph
name|Graph
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|graph
decl_stmt|;
DECL|field|N1
specifier|static
specifier|final
name|Integer
name|N1
init|=
literal|1
decl_stmt|;
DECL|field|N2
specifier|static
specifier|final
name|Integer
name|N2
init|=
literal|2
decl_stmt|;
DECL|field|N3
specifier|static
specifier|final
name|Integer
name|N3
init|=
literal|3
decl_stmt|;
DECL|field|N4
specifier|static
specifier|final
name|Integer
name|N4
init|=
literal|4
decl_stmt|;
DECL|field|N5
specifier|static
specifier|final
name|Integer
name|N5
init|=
literal|5
decl_stmt|;
DECL|field|NODE_NOT_IN_GRAPH
specifier|static
specifier|final
name|Integer
name|NODE_NOT_IN_GRAPH
init|=
literal|1000
decl_stmt|;
DECL|field|E11
specifier|static
specifier|final
name|String
name|E11
init|=
literal|"1-1"
decl_stmt|;
DECL|field|E11_A
specifier|static
specifier|final
name|String
name|E11_A
init|=
literal|"1-1a"
decl_stmt|;
DECL|field|E12
specifier|static
specifier|final
name|String
name|E12
init|=
literal|"1-2"
decl_stmt|;
DECL|field|E12_A
specifier|static
specifier|final
name|String
name|E12_A
init|=
literal|"1-2a"
decl_stmt|;
DECL|field|E21
specifier|static
specifier|final
name|String
name|E21
init|=
literal|"2-1"
decl_stmt|;
DECL|field|E13
specifier|static
specifier|final
name|String
name|E13
init|=
literal|"1-3"
decl_stmt|;
DECL|field|E14
specifier|static
specifier|final
name|String
name|E14
init|=
literal|"1-4"
decl_stmt|;
DECL|field|E23
specifier|static
specifier|final
name|String
name|E23
init|=
literal|"2-3"
decl_stmt|;
DECL|field|E41
specifier|static
specifier|final
name|String
name|E41
init|=
literal|"4-1"
decl_stmt|;
DECL|field|E15
specifier|static
specifier|final
name|String
name|E15
init|=
literal|"1-5"
decl_stmt|;
DECL|field|EDGE_NOT_IN_GRAPH
specifier|static
specifier|final
name|String
name|EDGE_NOT_IN_GRAPH
init|=
literal|"edgeNotInGraph"
decl_stmt|;
comment|// TODO(user): Consider separating Strings that we've defined here to capture
comment|// identifiable substrings of expected error messages, from Strings that we've defined
comment|// here to provide error messages.
comment|// TODO(user): Some Strings used in the subclasses can be added as static Strings
comment|// here too.
DECL|field|ERROR_ELEMENT_NOT_IN_GRAPH
specifier|static
specifier|final
name|String
name|ERROR_ELEMENT_NOT_IN_GRAPH
init|=
literal|"not an element of this graph"
decl_stmt|;
DECL|field|NODE_STRING
specifier|static
specifier|final
name|String
name|NODE_STRING
init|=
literal|"Node"
decl_stmt|;
DECL|field|EDGE_STRING
specifier|static
specifier|final
name|String
name|EDGE_STRING
init|=
literal|"Edge"
decl_stmt|;
DECL|field|ERROR_PARALLEL_EDGE
specifier|static
specifier|final
name|String
name|ERROR_PARALLEL_EDGE
init|=
literal|"connected by a different edge"
decl_stmt|;
DECL|field|ERROR_REUSE_EDGE
specifier|static
specifier|final
name|String
name|ERROR_REUSE_EDGE
init|=
literal|"it can't be reused to connect"
decl_stmt|;
DECL|field|ERROR_MODIFIABLE_SET
specifier|static
specifier|final
name|String
name|ERROR_MODIFIABLE_SET
init|=
literal|"Set returned is unexpectedly modifiable"
decl_stmt|;
DECL|field|ERROR_SELF_LOOP
specifier|static
specifier|final
name|String
name|ERROR_SELF_LOOP
init|=
literal|"self-loops are not allowed"
decl_stmt|;
DECL|field|ERROR_NODE_NOT_IN_GRAPH
specifier|static
specifier|final
name|String
name|ERROR_NODE_NOT_IN_GRAPH
init|=
literal|"Should not be allowed to pass a node that is not an element of the graph."
decl_stmt|;
DECL|field|ERROR_EDGE_NOT_IN_GRAPH
specifier|static
specifier|final
name|String
name|ERROR_EDGE_NOT_IN_GRAPH
init|=
literal|"Should not be allowed to pass an edge that is not an element of the graph."
decl_stmt|;
DECL|field|ERROR_ADDED_SELF_LOOP
specifier|static
specifier|final
name|String
name|ERROR_ADDED_SELF_LOOP
init|=
literal|"Should not be allowed to add a self-loop edge."
decl_stmt|;
DECL|field|ERROR_ADDED_PARALLEL_EDGE
specifier|static
specifier|final
name|String
name|ERROR_ADDED_PARALLEL_EDGE
init|=
literal|"Should not be allowed to add a parallel edge."
decl_stmt|;
DECL|field|ERROR_ADDED_EXISTING_EDGE
specifier|static
specifier|final
name|String
name|ERROR_ADDED_EXISTING_EDGE
init|=
literal|"Reusing an existing edge to connect different nodes succeeded"
decl_stmt|;
comment|/**    * Creates and returns an instance of the graph to be tested.    */
DECL|method|createGraph ()
specifier|public
specifier|abstract
name|Graph
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|createGraph
parameter_list|()
function_decl|;
comment|/**    * A proxy method that adds the node {@code n} to the graph being tested.    * In case of Immutable graph implementations, this method should add {@code n} to the graph    * builder and build a new graph with the current builder state.    *    * @return {@code true} iff the graph was modified as a result of this call    * TODO(user): Consider changing access modifier to be protected.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addNode (Integer n)
name|boolean
name|addNode
parameter_list|(
name|Integer
name|n
parameter_list|)
block|{
return|return
name|graph
operator|.
name|addNode
argument_list|(
name|n
argument_list|)
return|;
block|}
comment|/**    * A proxy method that adds the edge {@code e} to the graph    * being tested. In case of Immutable graph implementations, this method    * should add {@code e} to the graph builder and build a new graph with the current    * builder state.    *    *<p>This method should be used in tests of specific implementations if you want to    * ensure uniform behavior (including side effects) with how edges are added elsewhere    * in the tests.  For example, the existing implementations of this method explicitly    * add the supplied nodes to the graph, and then call {@code graph.addEdge()} to connect    * the edge to the nodes; this is not part of the contract of {@code graph.addEdge()}    * and is done for convenience.  In cases where you want to avoid such side effects    * (e.g., if you're testing what happens in your implementation if you add an edge    * whose end-points don't already exist in the graph), you should<b>not</b> use this    * method.    *    * @return {@code true} iff the graph was modified as a result of this call    * TODO(user): Consider changing access modifier to be protected.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addEdge (String e, Integer n1, Integer n2)
name|boolean
name|addEdge
parameter_list|(
name|String
name|e
parameter_list|,
name|Integer
name|n1
parameter_list|,
name|Integer
name|n2
parameter_list|)
block|{
name|graph
operator|.
name|addNode
argument_list|(
name|n1
argument_list|)
expr_stmt|;
name|graph
operator|.
name|addNode
argument_list|(
name|n2
argument_list|)
expr_stmt|;
return|return
name|graph
operator|.
name|addEdge
argument_list|(
name|e
argument_list|,
name|n1
argument_list|,
name|n2
argument_list|)
return|;
block|}
annotation|@
name|Before
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
block|{
name|graph
operator|=
name|createGraph
argument_list|()
expr_stmt|;
block|}
comment|/**    * Verifies that the {@code Set} returned by {@code nodes} has the expected mutability property    * (see the {@code Graph} documentation for more information).    */
annotation|@
name|Test
DECL|method|nodes_checkReturnedSetMutability ()
specifier|public
specifier|abstract
name|void
name|nodes_checkReturnedSetMutability
parameter_list|()
function_decl|;
comment|/**    * Verifies that the {@code Set} returned by {@code edges} has the expected mutability property    * (see the {@code Graph} documentation for more information).    */
annotation|@
name|Test
DECL|method|edges_checkReturnedSetMutability ()
specifier|public
specifier|abstract
name|void
name|edges_checkReturnedSetMutability
parameter_list|()
function_decl|;
comment|/**    * Verifies that the {@code Set} returned by {@code incidentEdges} has the expected    * mutability property (see the {@code Graph} documentation for more information).    */
annotation|@
name|Test
DECL|method|incidentEdges_checkReturnedSetMutability ()
specifier|public
specifier|abstract
name|void
name|incidentEdges_checkReturnedSetMutability
parameter_list|()
function_decl|;
comment|/**    * Verifies that the {@code Set} returned by {@code incidentNodes} has the expected    * mutability property (see the {@code Graph} documentation for more information).    */
annotation|@
name|Test
DECL|method|incidentNodes_checkReturnedSetMutability ()
specifier|public
specifier|abstract
name|void
name|incidentNodes_checkReturnedSetMutability
parameter_list|()
function_decl|;
comment|/**    * Verifies that the {@code Set} returned by {@code adjacentNodes} has the expected    * mutability property (see the {@code Graph} documentation for more information).    */
annotation|@
name|Test
DECL|method|adjacentNodes_checkReturnedSetMutability ()
specifier|public
specifier|abstract
name|void
name|adjacentNodes_checkReturnedSetMutability
parameter_list|()
function_decl|;
comment|/**    * Verifies that the {@code Set} returned by {@code adjacentEdges} has the expected    * mutability property (see the {@code Graph} documentation for more information).    */
annotation|@
name|Test
DECL|method|adjacentEdges_checkReturnedSetMutability ()
specifier|public
specifier|abstract
name|void
name|adjacentEdges_checkReturnedSetMutability
parameter_list|()
function_decl|;
comment|/**    * Verifies that the {@code Set} returned by {@code edgesConnecting} has the expected    * mutability property (see the {@code Graph} documentation for more information).    */
annotation|@
name|Test
DECL|method|edgesConnecting_checkReturnedSetMutability ()
specifier|public
specifier|abstract
name|void
name|edgesConnecting_checkReturnedSetMutability
parameter_list|()
function_decl|;
comment|/**    * Verifies that the {@code Set} returned by {@code inEdges} has the expected    * mutability property (see the {@code Graph} documentation for more information).    */
annotation|@
name|Test
DECL|method|inEdges_checkReturnedSetMutability ()
specifier|public
specifier|abstract
name|void
name|inEdges_checkReturnedSetMutability
parameter_list|()
function_decl|;
comment|/**    * Verifies that the {@code Set} returned by {@code outEdges} has the expected    * mutability property (see the {@code Graph} documentation for more information).    */
annotation|@
name|Test
DECL|method|outEdges_checkReturnedSetMutability ()
specifier|public
specifier|abstract
name|void
name|outEdges_checkReturnedSetMutability
parameter_list|()
function_decl|;
comment|/**    * Verifies that the {@code Set} returned by {@code predecessors} has the expected    * mutability property (see the {@code Graph} documentation for more information).    */
annotation|@
name|Test
DECL|method|predecessors_checkReturnedSetMutability ()
specifier|public
specifier|abstract
name|void
name|predecessors_checkReturnedSetMutability
parameter_list|()
function_decl|;
comment|/**    * Verifies that the {@code Set} returned by {@code successors} has the expected    * mutability property (see the {@code Graph} documentation for more information).    */
annotation|@
name|Test
DECL|method|successors_checkReturnedSetMutability ()
specifier|public
specifier|abstract
name|void
name|successors_checkReturnedSetMutability
parameter_list|()
function_decl|;
annotation|@
name|Test
DECL|method|nodes_oneNode ()
specifier|public
name|void
name|nodes_oneNode
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|nodes
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|N1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|nodes_noNodes ()
specifier|public
name|void
name|nodes_noNodes
parameter_list|()
block|{
name|assertThat
argument_list|(
name|graph
operator|.
name|nodes
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|edges_oneEdge ()
specifier|public
name|void
name|edges_oneEdge
parameter_list|()
block|{
name|addEdge
argument_list|(
name|E12
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edges
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|E12
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|edges_noEdges ()
specifier|public
name|void
name|edges_noEdges
parameter_list|()
block|{
name|assertThat
argument_list|(
name|graph
operator|.
name|edges
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
comment|// Graph with no edges, given disconnected nodes
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|addNode
argument_list|(
name|N2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edges
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|incidentEdges_oneEdge ()
specifier|public
name|void
name|incidentEdges_oneEdge
parameter_list|()
block|{
name|addEdge
argument_list|(
name|E12
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|incidentEdges
argument_list|(
name|N2
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|E12
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|incidentEdges
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|E12
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|incidentEdges_isolatedNode ()
specifier|public
name|void
name|incidentEdges_isolatedNode
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|incidentEdges
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|incidentEdges_nodeNotInGraph ()
specifier|public
name|void
name|incidentEdges_nodeNotInGraph
parameter_list|()
block|{
try|try
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|unused
init|=
name|graph
operator|.
name|incidentEdges
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|)
decl_stmt|;
name|fail
argument_list|(
name|ERROR_NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertNodeNotInGraphErrorMessage
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|incidentNodes_oneEdge ()
specifier|public
name|void
name|incidentNodes_oneEdge
parameter_list|()
block|{
name|addEdge
argument_list|(
name|E12
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|incidentNodes
argument_list|(
name|E12
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|incidentNodes_edgeNotInGraph ()
specifier|public
name|void
name|incidentNodes_edgeNotInGraph
parameter_list|()
block|{
try|try
block|{
name|Set
argument_list|<
name|Integer
argument_list|>
name|unused
init|=
name|graph
operator|.
name|incidentNodes
argument_list|(
name|EDGE_NOT_IN_GRAPH
argument_list|)
decl_stmt|;
name|fail
argument_list|(
name|ERROR_EDGE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertEdgeNotInGraphErrorMessage
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|adjacentNodes_oneEdge ()
specifier|public
name|void
name|adjacentNodes_oneEdge
parameter_list|()
block|{
name|addEdge
argument_list|(
name|E12
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|N2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|N2
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|N1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|adjacentNodes_noAdjacentNodes ()
specifier|public
name|void
name|adjacentNodes_noAdjacentNodes
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|adjacentNodes_nodeNotInGraph ()
specifier|public
name|void
name|adjacentNodes_nodeNotInGraph
parameter_list|()
block|{
try|try
block|{
name|Set
argument_list|<
name|Integer
argument_list|>
name|unused
init|=
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|)
decl_stmt|;
name|fail
argument_list|(
name|ERROR_NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertNodeNotInGraphErrorMessage
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|adjacentEdges_addEdges ()
specifier|public
name|void
name|adjacentEdges_addEdges
parameter_list|()
block|{
name|addEdge
argument_list|(
name|E12
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|addEdge
argument_list|(
name|E13
argument_list|,
name|N1
argument_list|,
name|N3
argument_list|)
expr_stmt|;
name|addEdge
argument_list|(
name|E23
argument_list|,
name|N2
argument_list|,
name|N3
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|adjacentEdges
argument_list|(
name|E12
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|E13
argument_list|,
name|E23
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|adjacentEdges_noAdjacentEdges ()
specifier|public
name|void
name|adjacentEdges_noAdjacentEdges
parameter_list|()
block|{
name|addEdge
argument_list|(
name|E12
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|adjacentEdges
argument_list|(
name|E12
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|adjacentEdges_nodeNotInGraph ()
specifier|public
name|void
name|adjacentEdges_nodeNotInGraph
parameter_list|()
block|{
try|try
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|unused
init|=
name|graph
operator|.
name|adjacentEdges
argument_list|(
name|EDGE_NOT_IN_GRAPH
argument_list|)
decl_stmt|;
name|fail
argument_list|(
name|ERROR_EDGE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertEdgeNotInGraphErrorMessage
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|edgesConnecting_disconnectedNodes ()
specifier|public
name|void
name|edgesConnecting_disconnectedNodes
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|addNode
argument_list|(
name|N2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgesConnecting
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|edgesConnecting_nodesNotInGraph ()
specifier|public
name|void
name|edgesConnecting_nodesNotInGraph
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|addNode
argument_list|(
name|N2
argument_list|)
expr_stmt|;
try|try
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|unused
init|=
name|graph
operator|.
name|edgesConnecting
argument_list|(
name|N1
argument_list|,
name|NODE_NOT_IN_GRAPH
argument_list|)
decl_stmt|;
name|fail
argument_list|(
name|ERROR_NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertNodeNotInGraphErrorMessage
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|unused
init|=
name|graph
operator|.
name|edgesConnecting
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|,
name|N2
argument_list|)
decl_stmt|;
name|fail
argument_list|(
name|ERROR_NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertNodeNotInGraphErrorMessage
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|unused
init|=
name|graph
operator|.
name|edgesConnecting
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|,
name|NODE_NOT_IN_GRAPH
argument_list|)
decl_stmt|;
name|fail
argument_list|(
name|ERROR_NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertNodeNotInGraphErrorMessage
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|inEdges_noInEdges ()
specifier|public
name|void
name|inEdges_noInEdges
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|inEdges
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|inEdges_nodeNotInGraph ()
specifier|public
name|void
name|inEdges_nodeNotInGraph
parameter_list|()
block|{
try|try
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|unused
init|=
name|graph
operator|.
name|inEdges
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|)
decl_stmt|;
name|fail
argument_list|(
name|ERROR_NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertNodeNotInGraphErrorMessage
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|outEdges_noOutEdges ()
specifier|public
name|void
name|outEdges_noOutEdges
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|outEdges
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|outEdges_nodeNotInGraph ()
specifier|public
name|void
name|outEdges_nodeNotInGraph
parameter_list|()
block|{
try|try
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|unused
init|=
name|graph
operator|.
name|outEdges
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|)
decl_stmt|;
name|fail
argument_list|(
name|ERROR_NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertNodeNotInGraphErrorMessage
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|predecessors_noPredecessors ()
specifier|public
name|void
name|predecessors_noPredecessors
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|predecessors
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|predecessors_nodeNotInGraph ()
specifier|public
name|void
name|predecessors_nodeNotInGraph
parameter_list|()
block|{
try|try
block|{
name|Set
argument_list|<
name|Integer
argument_list|>
name|unused
init|=
name|graph
operator|.
name|predecessors
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|)
decl_stmt|;
name|fail
argument_list|(
name|ERROR_NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertNodeNotInGraphErrorMessage
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|successors_noSuccessors ()
specifier|public
name|void
name|successors_noSuccessors
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|successors
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|successors_nodeNotInGraph ()
specifier|public
name|void
name|successors_nodeNotInGraph
parameter_list|()
block|{
try|try
block|{
name|Set
argument_list|<
name|Integer
argument_list|>
name|unused
init|=
name|graph
operator|.
name|successors
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|)
decl_stmt|;
name|fail
argument_list|(
name|ERROR_NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertNodeNotInGraphErrorMessage
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|degree_oneEdge ()
specifier|public
name|void
name|degree_oneEdge
parameter_list|()
block|{
name|addEdge
argument_list|(
name|E12
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|graph
operator|.
name|degree
argument_list|(
name|N1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|graph
operator|.
name|degree
argument_list|(
name|N2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|degree_isolatedNode ()
specifier|public
name|void
name|degree_isolatedNode
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|graph
operator|.
name|degree
argument_list|(
name|N1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|degree_nodeNotInGraph ()
specifier|public
name|void
name|degree_nodeNotInGraph
parameter_list|()
block|{
try|try
block|{
name|long
name|unused
init|=
name|graph
operator|.
name|degree
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|)
decl_stmt|;
name|fail
argument_list|(
name|ERROR_NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertNodeNotInGraphErrorMessage
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|inDegree_isolatedNode ()
specifier|public
name|void
name|inDegree_isolatedNode
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|graph
operator|.
name|inDegree
argument_list|(
name|N1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|inDegree_nodeNotInGraph ()
specifier|public
name|void
name|inDegree_nodeNotInGraph
parameter_list|()
block|{
try|try
block|{
name|long
name|unused
init|=
name|graph
operator|.
name|inDegree
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|)
decl_stmt|;
name|fail
argument_list|(
name|ERROR_NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertNodeNotInGraphErrorMessage
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|outDegree_isolatedNode ()
specifier|public
name|void
name|outDegree_isolatedNode
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|graph
operator|.
name|outDegree
argument_list|(
name|N1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|outDegree_nodeNotInGraph ()
specifier|public
name|void
name|outDegree_nodeNotInGraph
parameter_list|()
block|{
try|try
block|{
name|long
name|unused
init|=
name|graph
operator|.
name|outDegree
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|)
decl_stmt|;
name|fail
argument_list|(
name|ERROR_NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertNodeNotInGraphErrorMessage
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|addNode_newNode ()
specifier|public
name|void
name|addNode_newNode
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|addNode
argument_list|(
name|N1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|nodes
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|N1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addNode_existingNode ()
specifier|public
name|void
name|addNode_existingNode
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|ImmutableSet
argument_list|<
name|Integer
argument_list|>
name|nodes
init|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|graph
operator|.
name|nodes
argument_list|()
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|addNode
argument_list|(
name|N1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|nodes
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|nodes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeNode_existingNode ()
specifier|public
name|void
name|removeNode_existingNode
parameter_list|()
block|{
name|addEdge
argument_list|(
name|E12
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|addEdge
argument_list|(
name|E41
argument_list|,
name|N4
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|graph
operator|.
name|removeNode
argument_list|(
name|N1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|nodes
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|N2
argument_list|,
name|N4
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edges
argument_list|()
argument_list|)
operator|.
name|doesNotContain
argument_list|(
name|E12
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edges
argument_list|()
argument_list|)
operator|.
name|doesNotContain
argument_list|(
name|E41
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeNode_invalidArgument ()
specifier|public
name|void
name|removeNode_invalidArgument
parameter_list|()
block|{
name|ImmutableSet
argument_list|<
name|Integer
argument_list|>
name|nodes
init|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|graph
operator|.
name|nodes
argument_list|()
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|graph
operator|.
name|removeNode
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|nodes
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|nodes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeEdge_invalidArgument ()
specifier|public
name|void
name|removeEdge_invalidArgument
parameter_list|()
block|{
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|edges
init|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|graph
operator|.
name|edges
argument_list|()
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|graph
operator|.
name|removeEdge
argument_list|(
name|EDGE_NOT_IN_GRAPH
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edges
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|edges
argument_list|)
expr_stmt|;
block|}
DECL|method|assertNodeNotInGraphErrorMessage (Throwable throwable)
specifier|static
name|void
name|assertNodeNotInGraphErrorMessage
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|assertThat
argument_list|(
name|throwable
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|startsWith
argument_list|(
name|NODE_STRING
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|throwable
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|ERROR_ELEMENT_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
block|}
DECL|method|assertEdgeNotInGraphErrorMessage (Throwable throwable)
specifier|static
name|void
name|assertEdgeNotInGraphErrorMessage
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|assertThat
argument_list|(
name|throwable
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|startsWith
argument_list|(
name|EDGE_STRING
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|throwable
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|ERROR_ELEMENT_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

