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
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Test
import|;
end_import

begin_comment
comment|/**  * Abstract base class for testing implementations of {@link Network} interface.  *  *<p>This class is responsible for testing that a directed implementation of {@link Network}  * is correctly handling directed edges. Implementation-dependent test cases are left to  * subclasses. Test cases that do not require the graph to be directed are found in superclasses.  *  */
end_comment

begin_class
DECL|class|AbstractDirectedNetworkTest
specifier|public
specifier|abstract
class|class
name|AbstractDirectedNetworkTest
extends|extends
name|AbstractNetworkTest
block|{
annotation|@
name|After
DECL|method|validateSourceAndTarget ()
specifier|public
name|void
name|validateSourceAndTarget
parameter_list|()
block|{
for|for
control|(
name|Integer
name|node
range|:
name|network
operator|.
name|nodes
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|inEdge
range|:
name|network
operator|.
name|inEdges
argument_list|(
name|node
argument_list|)
control|)
block|{
name|Endpoints
argument_list|<
name|Integer
argument_list|>
name|endpoints
init|=
name|network
operator|.
name|incidentNodes
argument_list|(
name|inEdge
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|endpoints
operator|.
name|source
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|endpoints
operator|.
name|adjacentNode
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoints
operator|.
name|target
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|outEdge
range|:
name|network
operator|.
name|outEdges
argument_list|(
name|node
argument_list|)
control|)
block|{
name|Endpoints
argument_list|<
name|Integer
argument_list|>
name|endpoints
init|=
name|network
operator|.
name|incidentNodes
argument_list|(
name|outEdge
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|endpoints
operator|.
name|source
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoints
operator|.
name|target
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|endpoints
operator|.
name|adjacentNode
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Integer
name|adjacentNode
range|:
name|network
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
control|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|edges
init|=
name|network
operator|.
name|edgesConnecting
argument_list|(
name|node
argument_list|,
name|adjacentNode
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|antiParallelEdges
init|=
name|network
operator|.
name|edgesConnecting
argument_list|(
name|adjacentNode
argument_list|,
name|node
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|node
operator|.
name|equals
argument_list|(
name|adjacentNode
argument_list|)
operator|||
name|Collections
operator|.
name|disjoint
argument_list|(
name|edges
argument_list|,
name|antiParallelEdges
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
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
name|network
operator|.
name|incidentNodes
argument_list|(
name|E12
argument_list|)
operator|.
name|source
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
operator|.
name|incidentNodes
argument_list|(
name|E12
argument_list|)
operator|.
name|target
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|N2
argument_list|)
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
name|network
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
name|network
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
name|network
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
name|network
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
name|network
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
name|network
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
name|network
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
name|network
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
name|network
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
name|network
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
name|network
operator|.
name|incidentNodes
argument_list|(
name|E12
argument_list|)
operator|.
name|source
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|source_edgeNotInGraph ()
specifier|public
name|void
name|source_edgeNotInGraph
parameter_list|()
block|{
try|try
block|{
name|network
operator|.
name|incidentNodes
argument_list|(
name|EDGE_NOT_IN_GRAPH
argument_list|)
operator|.
name|source
argument_list|()
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
name|network
operator|.
name|incidentNodes
argument_list|(
name|E12
argument_list|)
operator|.
name|target
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|target_edgeNotInGraph ()
specifier|public
name|void
name|target_edgeNotInGraph
parameter_list|()
block|{
try|try
block|{
name|network
operator|.
name|incidentNodes
argument_list|(
name|EDGE_NOT_IN_GRAPH
argument_list|)
operator|.
name|target
argument_list|()
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
name|network
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
name|network
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
name|network
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
name|network
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
name|network
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
block|}
end_class

end_unit

