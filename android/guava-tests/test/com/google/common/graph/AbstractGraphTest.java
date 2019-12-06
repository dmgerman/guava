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
name|graph
operator|.
name|TestUtil
operator|.
name|ERROR_NODE_NOT_IN_GRAPH
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
name|graph
operator|.
name|TestUtil
operator|.
name|assertNodeNotInGraphErrorMessage
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
name|graph
operator|.
name|TestUtil
operator|.
name|assertStronglyEquivalent
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
name|graph
operator|.
name|TestUtil
operator|.
name|sanityCheckSet
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|truth
operator|.
name|TruthJUnit
operator|.
name|assume
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
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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

begin_comment
comment|/**  * Abstract base class for testing implementations of {@link Graph} interface. Graph instances  * created for testing should have Integer node and String edge objects.  *  *<p>Test cases that should be handled similarly in any graph implementation are included in this  * class. For example, testing that {@code nodes()} method returns the set of the nodes in the  * graph. The following test cases are left for the subclasses to handle:  *  *<ul>  *<li>Test cases related to whether the graph is directed or undirected.  *<li>Test cases related to the specific implementation of the {@link Graph} interface.  *</ul>  *  * TODO(user): Make this class generic (using<N, E>) for all node and edge types.  * TODO(user): Differentiate between directed and undirected edge strings.  */
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
argument_list|>
name|graph
decl_stmt|;
comment|/**    * The same reference as {@link #graph}, except as a mutable graph. This field is null in case    * {@link #createGraph()} didn't return a mutable graph.    */
DECL|field|graphAsMutableGraph
name|MutableGraph
argument_list|<
name|Integer
argument_list|>
name|graphAsMutableGraph
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
comment|// TODO(user): Consider separating Strings that we've defined here to capture
comment|// identifiable substrings of expected error messages, from Strings that we've defined
comment|// here to provide error messages.
comment|// TODO(user): Some Strings used in the subclasses can be added as static Strings
comment|// here too.
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
DECL|field|ERROR_ADDED_SELF_LOOP
specifier|static
specifier|final
name|String
name|ERROR_ADDED_SELF_LOOP
init|=
literal|"Should not be allowed to add a self-loop edge."
decl_stmt|;
comment|/** Creates and returns an instance of the graph to be tested. */
DECL|method|createGraph ()
specifier|abstract
name|Graph
argument_list|<
name|Integer
argument_list|>
name|createGraph
parameter_list|()
function_decl|;
comment|/**    * A proxy method that adds the node {@code n} to the graph being tested. In case of Immutable    * graph implementations, this method should replace {@link #graph} with a new graph that includes    * this node.    */
DECL|method|addNode (Integer n)
specifier|abstract
name|void
name|addNode
parameter_list|(
name|Integer
name|n
parameter_list|)
function_decl|;
comment|/**    * A proxy method that adds the edge {@code e} to the graph being tested. In case of Immutable    * graph implementations, this method should replace {@link #graph} with a new graph that includes    * this edge.    */
DECL|method|putEdge (Integer n1, Integer n2)
specifier|abstract
name|void
name|putEdge
parameter_list|(
name|Integer
name|n1
parameter_list|,
name|Integer
name|n2
parameter_list|)
function_decl|;
DECL|method|putEdge (EndpointPair<Integer> endpoints)
specifier|final
name|void
name|putEdge
parameter_list|(
name|EndpointPair
argument_list|<
name|Integer
argument_list|>
name|endpoints
parameter_list|)
block|{
name|putEdge
argument_list|(
name|endpoints
operator|.
name|nodeU
argument_list|()
argument_list|,
name|endpoints
operator|.
name|nodeV
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|graphIsMutable ()
specifier|final
name|boolean
name|graphIsMutable
parameter_list|()
block|{
return|return
name|graphAsMutableGraph
operator|!=
literal|null
return|;
block|}
annotation|@
name|Before
DECL|method|init ()
specifier|public
specifier|final
name|void
name|init
parameter_list|()
block|{
name|graph
operator|=
name|createGraph
argument_list|()
expr_stmt|;
if|if
condition|(
name|graph
operator|instanceof
name|MutableGraph
condition|)
block|{
name|graphAsMutableGraph
operator|=
operator|(
name|MutableGraph
argument_list|<
name|Integer
argument_list|>
operator|)
name|graph
expr_stmt|;
block|}
block|}
annotation|@
name|After
DECL|method|validateGraphState ()
specifier|public
specifier|final
name|void
name|validateGraphState
parameter_list|()
block|{
name|validateGraph
argument_list|(
name|graph
argument_list|)
expr_stmt|;
block|}
DECL|method|validateGraph (Graph<N> graph)
specifier|static
parameter_list|<
name|N
parameter_list|>
name|void
name|validateGraph
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|>
name|graph
parameter_list|)
block|{
name|assertStronglyEquivalent
argument_list|(
name|graph
argument_list|,
name|Graphs
operator|.
name|copyOf
argument_list|(
name|graph
argument_list|)
argument_list|)
expr_stmt|;
name|assertStronglyEquivalent
argument_list|(
name|graph
argument_list|,
name|ImmutableGraph
operator|.
name|copyOf
argument_list|(
name|graph
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|graphString
init|=
name|graph
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|graphString
argument_list|)
operator|.
name|contains
argument_list|(
literal|"isDirected: "
operator|+
name|graph
operator|.
name|isDirected
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graphString
argument_list|)
operator|.
name|contains
argument_list|(
literal|"allowsSelfLoops: "
operator|+
name|graph
operator|.
name|allowsSelfLoops
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|nodeStart
init|=
name|graphString
operator|.
name|indexOf
argument_list|(
literal|"nodes:"
argument_list|)
decl_stmt|;
name|int
name|edgeStart
init|=
name|graphString
operator|.
name|indexOf
argument_list|(
literal|"edges:"
argument_list|)
decl_stmt|;
name|String
name|nodeString
init|=
name|graphString
operator|.
name|substring
argument_list|(
name|nodeStart
argument_list|,
name|edgeStart
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|EndpointPair
argument_list|<
name|N
argument_list|>
argument_list|>
name|allEndpointPairs
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|N
name|node
range|:
name|sanityCheckSet
argument_list|(
name|graph
operator|.
name|nodes
argument_list|()
argument_list|)
control|)
block|{
name|assertThat
argument_list|(
name|nodeString
argument_list|)
operator|.
name|contains
argument_list|(
name|node
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|graph
operator|.
name|isDirected
argument_list|()
condition|)
block|{
name|assertThat
argument_list|(
name|graph
operator|.
name|degree
argument_list|(
name|node
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|graph
operator|.
name|inDegree
argument_list|(
name|node
argument_list|)
operator|+
name|graph
operator|.
name|outDegree
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
argument_list|)
operator|.
name|hasSize
argument_list|(
name|graph
operator|.
name|inDegree
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
argument_list|)
operator|.
name|hasSize
argument_list|(
name|graph
operator|.
name|outDegree
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|selfLoopCount
init|=
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
operator|.
name|contains
argument_list|(
name|node
argument_list|)
condition|?
literal|1
else|:
literal|0
decl_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|degree
argument_list|(
name|node
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
operator|.
name|size
argument_list|()
operator|+
name|selfLoopCount
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|inDegree
argument_list|(
name|node
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|graph
operator|.
name|degree
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|outDegree
argument_list|(
name|node
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|graph
operator|.
name|degree
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|N
name|adjacentNode
range|:
name|sanityCheckSet
argument_list|(
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|graph
operator|.
name|allowsSelfLoops
argument_list|()
condition|)
block|{
name|assertThat
argument_list|(
name|node
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|adjacentNode
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|graph
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
operator|.
name|contains
argument_list|(
name|adjacentNode
argument_list|)
operator|||
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
operator|.
name|contains
argument_list|(
name|adjacentNode
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|N
name|predecessor
range|:
name|sanityCheckSet
argument_list|(
name|graph
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
argument_list|)
control|)
block|{
name|assertThat
argument_list|(
name|graph
operator|.
name|successors
argument_list|(
name|predecessor
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|hasEdgeConnecting
argument_list|(
name|predecessor
argument_list|,
name|node
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|incidentEdges
argument_list|(
name|node
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
name|EndpointPair
operator|.
name|of
argument_list|(
name|graph
argument_list|,
name|predecessor
argument_list|,
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|N
name|successor
range|:
name|sanityCheckSet
argument_list|(
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
argument_list|)
control|)
block|{
name|allEndpointPairs
operator|.
name|add
argument_list|(
name|EndpointPair
operator|.
name|of
argument_list|(
name|graph
argument_list|,
name|node
argument_list|,
name|successor
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|predecessors
argument_list|(
name|successor
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|hasEdgeConnecting
argument_list|(
name|node
argument_list|,
name|successor
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|incidentEdges
argument_list|(
name|node
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
name|EndpointPair
operator|.
name|of
argument_list|(
name|graph
argument_list|,
name|node
argument_list|,
name|successor
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|EndpointPair
argument_list|<
name|N
argument_list|>
name|endpoints
range|:
name|sanityCheckSet
argument_list|(
name|graph
operator|.
name|incidentEdges
argument_list|(
name|node
argument_list|)
argument_list|)
control|)
block|{
if|if
condition|(
name|graph
operator|.
name|isDirected
argument_list|()
condition|)
block|{
name|assertThat
argument_list|(
name|graph
operator|.
name|hasEdgeConnecting
argument_list|(
name|endpoints
operator|.
name|source
argument_list|()
argument_list|,
name|endpoints
operator|.
name|target
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|assertThat
argument_list|(
name|graph
operator|.
name|hasEdgeConnecting
argument_list|(
name|endpoints
operator|.
name|nodeU
argument_list|()
argument_list|,
name|endpoints
operator|.
name|nodeV
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|sanityCheckSet
argument_list|(
name|graph
operator|.
name|edges
argument_list|()
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
name|EndpointPair
operator|.
name|of
argument_list|(
name|graph
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|,
operator|new
name|Object
argument_list|()
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
name|isEqualTo
argument_list|(
name|allEndpointPairs
argument_list|)
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
comment|/**    * Verifies that the {@code Set} returned by {@code adjacentNodes} has the expected mutability    * property (see the {@code Graph} documentation for more information).    */
annotation|@
name|Test
DECL|method|adjacentNodes_checkReturnedSetMutability ()
specifier|public
specifier|abstract
name|void
name|adjacentNodes_checkReturnedSetMutability
parameter_list|()
function_decl|;
comment|/**    * Verifies that the {@code Set} returned by {@code predecessors} has the expected mutability    * property (see the {@code Graph} documentation for more information).    */
annotation|@
name|Test
DECL|method|predecessors_checkReturnedSetMutability ()
specifier|public
specifier|abstract
name|void
name|predecessors_checkReturnedSetMutability
parameter_list|()
function_decl|;
comment|/**    * Verifies that the {@code Set} returned by {@code successors} has the expected mutability    * property (see the {@code Graph} documentation for more information).    */
annotation|@
name|Test
DECL|method|successors_checkReturnedSetMutability ()
specifier|public
specifier|abstract
name|void
name|successors_checkReturnedSetMutability
parameter_list|()
function_decl|;
comment|/**    * Verifies that the {@code Set} returned by {@code incidentEdges} has the expected mutability    * property (see the {@code Graph} documentation for more information).    */
annotation|@
name|Test
DECL|method|incidentEdges_checkReturnedSetMutability ()
specifier|public
specifier|abstract
name|void
name|incidentEdges_checkReturnedSetMutability
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
DECL|method|adjacentNodes_oneEdge ()
specifier|public
name|void
name|adjacentNodes_oneEdge
parameter_list|()
block|{
name|putEdge
argument_list|(
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
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
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
name|graph
operator|.
name|predecessors
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
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
name|graph
operator|.
name|successors
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
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
DECL|method|incidentEdges_noIncidentEdges ()
specifier|public
name|void
name|incidentEdges_noIncidentEdges
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
name|graph
operator|.
name|incidentEdges
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
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
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|degree
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|degree
argument_list|(
name|N2
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
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
name|assertThat
argument_list|(
name|graph
operator|.
name|degree
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
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
name|graph
operator|.
name|degree
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
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
name|assertThat
argument_list|(
name|graph
operator|.
name|inDegree
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
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
name|graph
operator|.
name|inDegree
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
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
name|assertThat
argument_list|(
name|graph
operator|.
name|outDegree
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
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
name|graph
operator|.
name|outDegree
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
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
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|graphIsMutable
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|graphAsMutableGraph
operator|.
name|addNode
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
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
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|graphIsMutable
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
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
name|assertThat
argument_list|(
name|graphAsMutableGraph
operator|.
name|addNode
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
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
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|graphIsMutable
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|putEdge
argument_list|(
name|N4
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graphAsMutableGraph
operator|.
name|removeNode
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|graphAsMutableGraph
operator|.
name|removeNode
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
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
name|adjacentNodes
argument_list|(
name|N2
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|N4
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeNode_antiparallelEdges ()
specifier|public
name|void
name|removeNode_antiparallelEdges
parameter_list|()
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|graphIsMutable
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|putEdge
argument_list|(
name|N2
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graphAsMutableGraph
operator|.
name|removeNode
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
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
name|assertThat
argument_list|(
name|graphAsMutableGraph
operator|.
name|removeNode
argument_list|(
name|N2
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
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
DECL|method|removeNode_nodeNotPresent ()
specifier|public
name|void
name|removeNode_nodeNotPresent
parameter_list|()
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|graphIsMutable
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
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
name|assertThat
argument_list|(
name|graphAsMutableGraph
operator|.
name|removeNode
argument_list|(
name|NODE_NOT_IN_GRAPH
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
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
DECL|method|removeNode_queryAfterRemoval ()
specifier|public
name|void
name|removeNode_queryAfterRemoval
parameter_list|()
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|graphIsMutable
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
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
name|N1
argument_list|)
decl_stmt|;
comment|// ensure cache (if any) is populated
name|assertThat
argument_list|(
name|graphAsMutableGraph
operator|.
name|removeNode
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
try|try
block|{
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|N1
argument_list|)
expr_stmt|;
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
DECL|method|removeEdge_existingEdge ()
specifier|public
name|void
name|removeEdge_existingEdge
parameter_list|()
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|graphIsMutable
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N2
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
name|containsExactly
argument_list|(
name|N2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|predecessors
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
name|assertThat
argument_list|(
name|graphAsMutableGraph
operator|.
name|removeEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|graphAsMutableGraph
operator|.
name|removeEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
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
name|assertThat
argument_list|(
name|graph
operator|.
name|predecessors
argument_list|(
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
DECL|method|removeEdge_oneOfMany ()
specifier|public
name|void
name|removeEdge_oneOfMany
parameter_list|()
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|graphIsMutable
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N3
argument_list|)
expr_stmt|;
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N4
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graphAsMutableGraph
operator|.
name|removeEdge
argument_list|(
name|N1
argument_list|,
name|N3
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
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
argument_list|,
name|N4
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeEdge_nodeNotPresent ()
specifier|public
name|void
name|removeEdge_nodeNotPresent
parameter_list|()
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|graphIsMutable
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graphAsMutableGraph
operator|.
name|removeEdge
argument_list|(
name|N1
argument_list|,
name|NODE_NOT_IN_GRAPH
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
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
name|contains
argument_list|(
name|N2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeEdge_edgeNotPresent ()
specifier|public
name|void
name|removeEdge_edgeNotPresent
parameter_list|()
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|graphIsMutable
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|addNode
argument_list|(
name|N3
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graphAsMutableGraph
operator|.
name|removeEdge
argument_list|(
name|N1
argument_list|,
name|N3
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
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
name|contains
argument_list|(
name|N2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

