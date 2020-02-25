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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|JUnit4
import|;
end_import

begin_comment
comment|/** Tests for a directed {@link ConfigurableMutableNetwork} allowing self-loops. */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4
operator|.
name|class
argument_list|)
DECL|class|ConfigurableDirectedNetworkTest
specifier|public
class|class
name|ConfigurableDirectedNetworkTest
extends|extends
name|ConfigurableSimpleDirectedNetworkTest
block|{
annotation|@
name|Override
DECL|method|createGraph ()
specifier|public
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|createGraph
parameter_list|()
block|{
return|return
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|allowsSelfLoops
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|addNode (Integer n)
name|void
name|addNode
parameter_list|(
name|Integer
name|n
parameter_list|)
block|{
name|networkAsMutableNetwork
operator|.
name|addNode
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addEdge (Integer n1, Integer n2, String e)
name|void
name|addEdge
parameter_list|(
name|Integer
name|n1
parameter_list|,
name|Integer
name|n2
parameter_list|,
name|String
name|e
parameter_list|)
block|{
name|networkAsMutableNetwork
operator|.
name|addEdge
argument_list|(
name|n1
argument_list|,
name|n2
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|edges_selfLoop ()
specifier|public
name|void
name|edges_selfLoop
parameter_list|()
block|{
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
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
name|containsExactly
argument_list|(
name|E11
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|incidentEdges_selfLoop ()
specifier|public
name|void
name|incidentEdges_selfLoop
parameter_list|()
block|{
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
operator|.
name|incidentEdges
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|E11
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|incidentNodes_selfLoop ()
specifier|public
name|void
name|incidentNodes_selfLoop
parameter_list|()
block|{
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
operator|.
name|incidentNodes
argument_list|(
name|E11
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
name|E11
argument_list|)
operator|.
name|target
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|N1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|adjacentNodes_selfLoop ()
specifier|public
name|void
name|adjacentNodes_selfLoop
parameter_list|()
block|{
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
expr_stmt|;
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
operator|.
name|adjacentNodes
argument_list|(
name|N1
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
DECL|method|adjacentEdges_selfLoop ()
specifier|public
name|void
name|adjacentEdges_selfLoop
parameter_list|()
block|{
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
expr_stmt|;
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
operator|.
name|adjacentEdges
argument_list|(
name|E11
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
DECL|method|edgesConnecting_selfLoop ()
specifier|public
name|void
name|edgesConnecting_selfLoop
parameter_list|()
block|{
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
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
name|N1
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|E11
argument_list|)
expr_stmt|;
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
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
name|assertThat
argument_list|(
name|network
operator|.
name|edgesConnecting
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|E11
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|inEdges_selfLoop ()
specifier|public
name|void
name|inEdges_selfLoop
parameter_list|()
block|{
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
expr_stmt|;
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
name|containsExactly
argument_list|(
name|E11
argument_list|)
expr_stmt|;
name|addEdge
argument_list|(
name|N4
argument_list|,
name|N1
argument_list|,
name|E41
argument_list|)
expr_stmt|;
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
name|containsExactly
argument_list|(
name|E11
argument_list|,
name|E41
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|outEdges_selfLoop ()
specifier|public
name|void
name|outEdges_selfLoop
parameter_list|()
block|{
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
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
name|E11
argument_list|)
expr_stmt|;
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
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
name|E11
argument_list|,
name|E12
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|predecessors_selfLoop ()
specifier|public
name|void
name|predecessors_selfLoop
parameter_list|()
block|{
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
expr_stmt|;
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
name|containsExactly
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|addEdge
argument_list|(
name|N4
argument_list|,
name|N1
argument_list|,
name|E41
argument_list|)
expr_stmt|;
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
name|containsExactly
argument_list|(
name|N1
argument_list|,
name|N4
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|successors_selfLoop ()
specifier|public
name|void
name|successors_selfLoop
parameter_list|()
block|{
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
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
name|N1
argument_list|)
expr_stmt|;
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
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
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|source_selfLoop ()
specifier|public
name|void
name|source_selfLoop
parameter_list|()
block|{
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
operator|.
name|incidentNodes
argument_list|(
name|E11
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
block|}
annotation|@
name|Test
DECL|method|target_selfLoop ()
specifier|public
name|void
name|target_selfLoop
parameter_list|()
block|{
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
operator|.
name|incidentNodes
argument_list|(
name|E11
argument_list|)
operator|.
name|target
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|N1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|degree_selfLoop ()
specifier|public
name|void
name|degree_selfLoop
parameter_list|()
block|{
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
operator|.
name|degree
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
operator|.
name|degree
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|inDegree_selfLoop ()
specifier|public
name|void
name|inDegree_selfLoop
parameter_list|()
block|{
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
operator|.
name|inDegree
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
name|addEdge
argument_list|(
name|N4
argument_list|,
name|N1
argument_list|,
name|E41
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
operator|.
name|inDegree
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|outDegree_selfLoop ()
specifier|public
name|void
name|outDegree_selfLoop
parameter_list|()
block|{
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
operator|.
name|outDegree
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
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
operator|.
name|outDegree
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|addEdge_selfLoop ()
specifier|public
name|void
name|addEdge_selfLoop
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
name|networkAsMutableNetwork
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
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
name|E11
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
name|N1
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|E11
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addEdge_existingSelfLoopEdgeBetweenSameNodes ()
specifier|public
name|void
name|addEdge_existingSelfLoopEdgeBetweenSameNodes
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
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
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
name|assertThat
argument_list|(
name|networkAsMutableNetwork
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
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
DECL|method|addEdge_existingEdgeBetweenDifferentNodes_selfLoops ()
specifier|public
name|void
name|addEdge_existingEdgeBetweenDifferentNodes_selfLoops
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
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
expr_stmt|;
try|try
block|{
name|networkAsMutableNetwork
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E11
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Reusing an existing self-loop edge to connect different nodes succeeded"
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
name|networkAsMutableNetwork
operator|.
name|addEdge
argument_list|(
name|N2
argument_list|,
name|N2
argument_list|,
name|E11
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Reusing an existing self-loop edge to make a different self-loop edge succeeded"
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
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
argument_list|)
expr_stmt|;
try|try
block|{
name|networkAsMutableNetwork
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E12
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Reusing an existing edge to add a self-loop edge between different nodes succeeded"
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
DECL|method|addEdge_parallelSelfLoopEdge ()
specifier|public
name|void
name|addEdge_parallelSelfLoopEdge
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
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
expr_stmt|;
try|try
block|{
name|networkAsMutableNetwork
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|EDGE_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Adding a parallel self-loop edge succeeded"
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
DECL|method|removeNode_existingNodeWithSelfLoopEdge ()
specifier|public
name|void
name|removeNode_existingNodeWithSelfLoopEdge
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
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|networkAsMutableNetwork
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
name|network
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
name|network
operator|.
name|edges
argument_list|()
argument_list|)
operator|.
name|doesNotContain
argument_list|(
name|E11
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeEdge_existingSelfLoopEdge ()
specifier|public
name|void
name|removeEdge_existingSelfLoopEdge
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
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|networkAsMutableNetwork
operator|.
name|removeEdge
argument_list|(
name|E11
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|network
operator|.
name|edges
argument_list|()
argument_list|)
operator|.
name|doesNotContain
argument_list|(
name|E11
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
name|N1
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

