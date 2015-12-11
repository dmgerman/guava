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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Abstract base class for testing mutable implementations of  * {@link DirectedGraph} interface.  *  *<p>This class is responsible for testing that an implementation of the interface  * {@code DirectedGraph} is correctly handling directed edges.  * Implementation-dependent test cases are left to subclasses. Test cases that  * do not require the graph to be directed are found in superclasses.  *  */
end_comment

begin_class
DECL|class|AbstractDirectedGraphTest
specifier|public
specifier|abstract
class|class
name|AbstractDirectedGraphTest
extends|extends
name|AbstractMutableGraphTest
block|{
DECL|field|directedGraph
name|DirectedGraph
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
decl_stmt|;
annotation|@
name|Override
DECL|method|createGraph ()
specifier|public
specifier|abstract
name|DirectedGraph
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|createGraph
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
block|{
name|graph
operator|=
name|directedGraph
operator|=
name|createGraph
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|edgesConnecting_oneEdge ()
specifier|public
name|void
name|edgesConnecting_oneEdge
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
name|directedGraph
operator|.
name|edgesConnecting
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|E12
argument_list|)
expr_stmt|;
comment|// Passed nodes should be in the correct edge direction, first is the
comment|// source node and the second is the target node
name|assertThat
argument_list|(
name|directedGraph
operator|.
name|edgesConnecting
argument_list|(
name|N2
argument_list|,
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
DECL|method|inEdges_oneEdge ()
specifier|public
name|void
name|inEdges_oneEdge
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
name|directedGraph
operator|.
name|inEdges
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
comment|// Edge direction handled correctly
name|assertThat
argument_list|(
name|directedGraph
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
DECL|method|outEdges_oneEdge ()
specifier|public
name|void
name|outEdges_oneEdge
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
name|directedGraph
operator|.
name|outEdges
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
comment|// Edge direction handled correctly
name|assertThat
argument_list|(
name|directedGraph
operator|.
name|outEdges
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
DECL|method|predecessors_oneEdge ()
specifier|public
name|void
name|predecessors_oneEdge
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
name|directedGraph
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
comment|// Edge direction handled correctly
name|assertThat
argument_list|(
name|directedGraph
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
DECL|method|successors_oneEdge ()
specifier|public
name|void
name|successors_oneEdge
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
name|directedGraph
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
comment|// Edge direction handled correctly
name|assertThat
argument_list|(
name|directedGraph
operator|.
name|successors
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
DECL|method|inDegree_oneEdge ()
specifier|public
name|void
name|inDegree_oneEdge
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
name|directedGraph
operator|.
name|inDegree
argument_list|(
name|N2
argument_list|)
argument_list|)
expr_stmt|;
comment|// Edge direction handled correctly
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|directedGraph
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
DECL|method|outDegree_oneEdge ()
specifier|public
name|void
name|outDegree_oneEdge
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
name|directedGraph
operator|.
name|outDegree
argument_list|(
name|N1
argument_list|)
argument_list|)
expr_stmt|;
comment|// Edge direction handled correctly
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|directedGraph
operator|.
name|outDegree
argument_list|(
name|N2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|source_oneEdge ()
specifier|public
name|void
name|source_oneEdge
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
name|N1
argument_list|,
name|directedGraph
operator|.
name|source
argument_list|(
name|E12
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"CheckReturnValue"
argument_list|)
DECL|method|source_edgeNotInGraph ()
specifier|public
name|void
name|source_edgeNotInGraph
parameter_list|()
block|{
try|try
block|{
name|directedGraph
operator|.
name|source
argument_list|(
name|EDGE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
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
DECL|method|target_oneEdge ()
specifier|public
name|void
name|target_oneEdge
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
name|N2
argument_list|,
name|directedGraph
operator|.
name|target
argument_list|(
name|E12
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"CheckReturnValue"
argument_list|)
DECL|method|target_edgeNotInGraph ()
specifier|public
name|void
name|target_edgeNotInGraph
parameter_list|()
block|{
try|try
block|{
name|directedGraph
operator|.
name|target
argument_list|(
name|EDGE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
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
comment|// Element Mutation
annotation|@
name|Test
DECL|method|addEdge_existingNodes ()
specifier|public
name|void
name|addEdge_existingNodes
parameter_list|()
block|{
comment|// Adding nodes initially for safety (insulating from possible future
comment|// modifications to proxy methods)
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
name|assertTrue
argument_list|(
name|addEdge
argument_list|(
name|E12
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|directedGraph
operator|.
name|edges
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|E12
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|directedGraph
operator|.
name|edgesConnecting
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|E12
argument_list|)
expr_stmt|;
comment|// Direction of the added edge is correctly handled
name|assertThat
argument_list|(
name|directedGraph
operator|.
name|edgesConnecting
argument_list|(
name|N2
argument_list|,
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
DECL|method|addEdge_existingEdgeBetweenSameNodes ()
specifier|public
name|void
name|addEdge_existingEdgeBetweenSameNodes
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
name|directedGraph
operator|.
name|edges
argument_list|()
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|addEdge
argument_list|(
name|E12
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|directedGraph
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
annotation|@
name|Test
DECL|method|addEdge_existingEdgeBetweenDifferentNodes ()
specifier|public
name|void
name|addEdge_existingEdgeBetweenDifferentNodes
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
try|try
block|{
comment|// Edge between totally different nodes
name|addEdge
argument_list|(
name|E12
argument_list|,
name|N4
argument_list|,
name|N5
argument_list|)
expr_stmt|;
name|fail
argument_list|(
name|ERROR_ADDED_EXISTING_EDGE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|ERROR_REUSE_EDGE
argument_list|)
expr_stmt|;
block|}
try|try
block|{
comment|// Edge between same nodes but in reverse direction
name|addEdge
argument_list|(
name|E12
argument_list|,
name|N2
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
name|ERROR_ADDED_EXISTING_EDGE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|ERROR_REUSE_EDGE
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|addEdge_parallelEdge ()
specifier|public
name|void
name|addEdge_parallelEdge
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
try|try
block|{
name|addEdge
argument_list|(
name|EDGE_NOT_IN_GRAPH
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|fail
argument_list|(
name|ERROR_ADDED_PARALLEL_EDGE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|ERROR_PARALLEL_EDGE
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
name|addEdge
argument_list|(
name|E12
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|directedGraph
operator|.
name|removeEdge
argument_list|(
name|E12
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|directedGraph
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
name|directedGraph
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
DECL|method|toString_emptyGraph ()
specifier|public
name|void
name|toString_emptyGraph
parameter_list|()
block|{
name|assertThat
argument_list|(
name|graph
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"config: %s, nodes: %s, edges: {}"
argument_list|,
name|graph
operator|.
name|config
argument_list|()
argument_list|,
name|graph
operator|.
name|nodes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|toString_noEdges ()
specifier|public
name|void
name|toString_noEdges
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
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"config: %s, nodes: %s, edges: {}"
argument_list|,
name|graph
operator|.
name|config
argument_list|()
argument_list|,
name|graph
operator|.
name|nodes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|toString_singleEdge ()
specifier|public
name|void
name|toString_singleEdge
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
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"config: %s, nodes: %s, edges: {%s=<%s -> %s>}"
argument_list|,
name|graph
operator|.
name|config
argument_list|()
argument_list|,
name|graph
operator|.
name|nodes
argument_list|()
argument_list|,
name|E12
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|toString_multipleNodesAndEdges ()
specifier|public
name|void
name|toString_multipleNodesAndEdges
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
name|assertThat
argument_list|(
name|graph
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"config: %s, nodes: %s, edges: {%s=<%s -> %s>, %s=<%s -> %s>}"
argument_list|,
name|graph
operator|.
name|config
argument_list|()
argument_list|,
name|graph
operator|.
name|nodes
argument_list|()
argument_list|,
name|E12
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|,
name|E13
argument_list|,
name|N1
argument_list|,
name|N3
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

