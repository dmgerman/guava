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
name|ImmutableList
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
name|common
operator|.
name|testing
operator|.
name|EqualsTester
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
comment|/**  * Tests for {@link Endpoints} and {@link Graph#edges()}.  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4
operator|.
name|class
argument_list|)
DECL|class|EndpointsTest
specifier|public
specifier|final
class|class
name|EndpointsTest
block|{
DECL|field|N0
specifier|private
specifier|static
specifier|final
name|Integer
name|N0
init|=
literal|0
decl_stmt|;
DECL|field|N1
specifier|private
specifier|static
specifier|final
name|Integer
name|N1
init|=
literal|1
decl_stmt|;
DECL|field|N2
specifier|private
specifier|static
specifier|final
name|Integer
name|N2
init|=
literal|2
decl_stmt|;
DECL|field|N3
specifier|private
specifier|static
specifier|final
name|Integer
name|N3
init|=
literal|3
decl_stmt|;
DECL|field|N4
specifier|private
specifier|static
specifier|final
name|Integer
name|N4
init|=
literal|4
decl_stmt|;
DECL|field|E12
specifier|private
specifier|static
specifier|final
name|String
name|E12
init|=
literal|"1-2"
decl_stmt|;
DECL|field|E12_A
specifier|private
specifier|static
specifier|final
name|String
name|E12_A
init|=
literal|"1-2a"
decl_stmt|;
DECL|field|E21
specifier|private
specifier|static
specifier|final
name|String
name|E21
init|=
literal|"2-1"
decl_stmt|;
DECL|field|E13
specifier|private
specifier|static
specifier|final
name|String
name|E13
init|=
literal|"1-3"
decl_stmt|;
DECL|field|E44
specifier|private
specifier|static
specifier|final
name|String
name|E44
init|=
literal|"4-4"
decl_stmt|;
comment|// Test for Endpoints class
annotation|@
name|Test
DECL|method|testDirectedEndpoints ()
specifier|public
name|void
name|testDirectedEndpoints
parameter_list|()
block|{
name|Endpoints
argument_list|<
name|String
argument_list|>
name|directed
init|=
name|Endpoints
operator|.
name|ofDirected
argument_list|(
literal|"source"
argument_list|,
literal|"target"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|directed
operator|.
name|source
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"source"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|directed
operator|.
name|target
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"target"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|directed
operator|.
name|nodeA
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"source"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|directed
operator|.
name|nodeB
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"target"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|directed
operator|.
name|adjacentNode
argument_list|(
literal|"source"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"target"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|directed
operator|.
name|adjacentNode
argument_list|(
literal|"target"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"source"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|directed
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"<source -> target>"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUndirectedEndpoints ()
specifier|public
name|void
name|testUndirectedEndpoints
parameter_list|()
block|{
name|Endpoints
argument_list|<
name|String
argument_list|>
name|undirected
init|=
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
literal|"chicken"
argument_list|,
literal|"egg"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|undirected
operator|.
name|nodeA
argument_list|()
argument_list|,
name|undirected
operator|.
name|nodeB
argument_list|()
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"chicken"
argument_list|,
literal|"egg"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|undirected
operator|.
name|adjacentNode
argument_list|(
name|undirected
operator|.
name|nodeA
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|undirected
operator|.
name|nodeB
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|undirected
operator|.
name|adjacentNode
argument_list|(
name|undirected
operator|.
name|nodeB
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|undirected
operator|.
name|nodeA
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|undirected
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"chicken"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|undirected
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"egg"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSelfLoop ()
specifier|public
name|void
name|testSelfLoop
parameter_list|()
block|{
name|Endpoints
argument_list|<
name|String
argument_list|>
name|undirected
init|=
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
literal|"node"
argument_list|,
literal|"node"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|undirected
operator|.
name|nodeA
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"node"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|undirected
operator|.
name|nodeB
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"node"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|undirected
operator|.
name|adjacentNode
argument_list|(
literal|"node"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"node"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|undirected
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"[node, node]"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAdjacentNode_nodeNotIncident ()
specifier|public
name|void
name|testAdjacentNode_nodeNotIncident
parameter_list|()
block|{
name|List
argument_list|<
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|>
name|testGraphs
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
operator|<
name|Integer
argument_list|,
name|String
operator|>
name|build
argument_list|()
argument_list|,
name|NetworkBuilder
operator|.
name|undirected
argument_list|()
operator|.
operator|<
name|Integer
argument_list|,
name|String
operator|>
name|build
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|graph
range|:
name|testGraphs
control|)
block|{
name|graph
operator|.
name|addEdge
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"1-2"
argument_list|)
expr_stmt|;
name|Endpoints
argument_list|<
name|Integer
argument_list|>
name|endpoints
init|=
name|graph
operator|.
name|incidentNodes
argument_list|(
literal|"1-2"
argument_list|)
decl_stmt|;
try|try
block|{
name|endpoints
operator|.
name|adjacentNode
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have rejected adjacentNode() called with a node not incident to edge."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{       }
block|}
block|}
annotation|@
name|Test
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
name|Endpoints
argument_list|<
name|String
argument_list|>
name|directed
init|=
name|Endpoints
operator|.
name|ofDirected
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|Endpoints
argument_list|<
name|String
argument_list|>
name|directedMirror
init|=
name|Endpoints
operator|.
name|ofDirected
argument_list|(
literal|"b"
argument_list|,
literal|"a"
argument_list|)
decl_stmt|;
name|Endpoints
argument_list|<
name|String
argument_list|>
name|undirected
init|=
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|Endpoints
argument_list|<
name|String
argument_list|>
name|undirectedMirror
init|=
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
literal|"b"
argument_list|,
literal|"a"
argument_list|)
decl_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|directed
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|directedMirror
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|undirected
argument_list|,
name|undirectedMirror
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
comment|// Tests for Graph.edges() and Network.asGraph().edges() methods
comment|// TODO(user): Move these to a more appropiate location in the test suite.
annotation|@
name|Test
DECL|method|edges_directedGraph ()
specifier|public
name|void
name|edges_directedGraph
parameter_list|()
block|{
name|MutableBasicGraph
argument_list|<
name|Integer
argument_list|>
name|directedGraph
init|=
name|BasicGraphBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|directedGraph
operator|.
name|addNode
argument_list|(
name|N0
argument_list|)
expr_stmt|;
name|directedGraph
operator|.
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|directedGraph
operator|.
name|putEdge
argument_list|(
name|N2
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|directedGraph
operator|.
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N3
argument_list|)
expr_stmt|;
name|directedGraph
operator|.
name|putEdge
argument_list|(
name|N4
argument_list|,
name|N4
argument_list|)
expr_stmt|;
name|containsExactlySanityCheck
argument_list|(
name|directedGraph
operator|.
name|edges
argument_list|()
argument_list|,
name|Endpoints
operator|.
name|ofDirected
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|,
name|Endpoints
operator|.
name|ofDirected
argument_list|(
name|N2
argument_list|,
name|N1
argument_list|)
argument_list|,
name|Endpoints
operator|.
name|ofDirected
argument_list|(
name|N1
argument_list|,
name|N3
argument_list|)
argument_list|,
name|Endpoints
operator|.
name|ofDirected
argument_list|(
name|N4
argument_list|,
name|N4
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|edges_undirectedGraph ()
specifier|public
name|void
name|edges_undirectedGraph
parameter_list|()
block|{
name|MutableBasicGraph
argument_list|<
name|Integer
argument_list|>
name|undirectedGraph
init|=
name|BasicGraphBuilder
operator|.
name|undirected
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|undirectedGraph
operator|.
name|addNode
argument_list|(
name|N0
argument_list|)
expr_stmt|;
name|undirectedGraph
operator|.
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|undirectedGraph
operator|.
name|putEdge
argument_list|(
name|N2
argument_list|,
name|N1
argument_list|)
expr_stmt|;
comment|// does nothing
name|undirectedGraph
operator|.
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N3
argument_list|)
expr_stmt|;
name|undirectedGraph
operator|.
name|putEdge
argument_list|(
name|N4
argument_list|,
name|N4
argument_list|)
expr_stmt|;
name|containsExactlySanityCheck
argument_list|(
name|undirectedGraph
operator|.
name|edges
argument_list|()
argument_list|,
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|,
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
name|N1
argument_list|,
name|N3
argument_list|)
argument_list|,
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
name|N4
argument_list|,
name|N4
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|edges_directedNetwork ()
specifier|public
name|void
name|edges_directedNetwork
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedNetwork
init|=
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|directedNetwork
operator|.
name|addNode
argument_list|(
name|N0
argument_list|)
expr_stmt|;
name|directedNetwork
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
argument_list|)
expr_stmt|;
name|directedNetwork
operator|.
name|addEdge
argument_list|(
name|N2
argument_list|,
name|N1
argument_list|,
name|E21
argument_list|)
expr_stmt|;
name|directedNetwork
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N3
argument_list|,
name|E13
argument_list|)
expr_stmt|;
name|directedNetwork
operator|.
name|addEdge
argument_list|(
name|N4
argument_list|,
name|N4
argument_list|,
name|E44
argument_list|)
expr_stmt|;
name|containsExactlySanityCheck
argument_list|(
name|directedNetwork
operator|.
name|asGraph
argument_list|()
operator|.
name|edges
argument_list|()
argument_list|,
name|Endpoints
operator|.
name|ofDirected
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|,
name|Endpoints
operator|.
name|ofDirected
argument_list|(
name|N2
argument_list|,
name|N1
argument_list|)
argument_list|,
name|Endpoints
operator|.
name|ofDirected
argument_list|(
name|N1
argument_list|,
name|N3
argument_list|)
argument_list|,
name|Endpoints
operator|.
name|ofDirected
argument_list|(
name|N4
argument_list|,
name|N4
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|edges_undirectedNetwork ()
specifier|public
name|void
name|edges_undirectedNetwork
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|undirectedNetwork
init|=
name|NetworkBuilder
operator|.
name|undirected
argument_list|()
operator|.
name|allowsParallelEdges
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|undirectedNetwork
operator|.
name|addNode
argument_list|(
name|N0
argument_list|)
expr_stmt|;
name|undirectedNetwork
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
argument_list|)
expr_stmt|;
name|undirectedNetwork
operator|.
name|addEdge
argument_list|(
name|N2
argument_list|,
name|N1
argument_list|,
name|E12_A
argument_list|)
expr_stmt|;
comment|// adds parallel edge, won't be in Graph edges
name|undirectedNetwork
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N3
argument_list|,
name|E13
argument_list|)
expr_stmt|;
name|undirectedNetwork
operator|.
name|addEdge
argument_list|(
name|N4
argument_list|,
name|N4
argument_list|,
name|E44
argument_list|)
expr_stmt|;
name|containsExactlySanityCheck
argument_list|(
name|undirectedNetwork
operator|.
name|asGraph
argument_list|()
operator|.
name|edges
argument_list|()
argument_list|,
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|,
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
name|N1
argument_list|,
name|N3
argument_list|)
argument_list|,
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
name|N4
argument_list|,
name|N4
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|edges_unmodifiableView ()
specifier|public
name|void
name|edges_unmodifiableView
parameter_list|()
block|{
name|MutableBasicGraph
argument_list|<
name|Integer
argument_list|>
name|directedGraph
init|=
name|BasicGraphBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Endpoints
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|edges
init|=
name|directedGraph
operator|.
name|edges
argument_list|()
decl_stmt|;
name|directedGraph
operator|.
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|containsExactlySanityCheck
argument_list|(
name|edges
argument_list|,
name|Endpoints
operator|.
name|ofDirected
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|)
expr_stmt|;
name|directedGraph
operator|.
name|putEdge
argument_list|(
name|N2
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|containsExactlySanityCheck
argument_list|(
name|edges
argument_list|,
name|Endpoints
operator|.
name|ofDirected
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|,
name|Endpoints
operator|.
name|ofDirected
argument_list|(
name|N2
argument_list|,
name|N1
argument_list|)
argument_list|)
expr_stmt|;
name|directedGraph
operator|.
name|removeEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|directedGraph
operator|.
name|removeEdge
argument_list|(
name|N2
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|containsExactlySanityCheck
argument_list|(
name|edges
argument_list|)
expr_stmt|;
try|try
block|{
name|edges
operator|.
name|add
argument_list|(
name|Endpoints
operator|.
name|ofDirected
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Set returned by edges() should be unmodifiable"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
block|}
annotation|@
name|Test
DECL|method|edges_containment ()
specifier|public
name|void
name|edges_containment
parameter_list|()
block|{
name|MutableBasicGraph
argument_list|<
name|Integer
argument_list|>
name|undirectedGraph
init|=
name|BasicGraphBuilder
operator|.
name|undirected
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|undirectedGraph
operator|.
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|undirectedGraph
operator|.
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Endpoints
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|edges
init|=
name|undirectedGraph
operator|.
name|edges
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|edges
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|edges
argument_list|)
operator|.
name|contains
argument_list|(
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|edges
argument_list|)
operator|.
name|contains
argument_list|(
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|edges
argument_list|)
operator|.
name|contains
argument_list|(
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
name|N2
argument_list|,
name|N1
argument_list|)
argument_list|)
expr_stmt|;
comment|// equal to ofUndirected(N1, N2)
name|assertThat
argument_list|(
name|edges
argument_list|)
operator|.
name|doesNotContain
argument_list|(
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
name|N2
argument_list|,
name|N2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|edges
argument_list|)
operator|.
name|doesNotContain
argument_list|(
name|Endpoints
operator|.
name|ofDirected
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|)
expr_stmt|;
comment|// graph not directed
name|assertThat
argument_list|(
name|edges
argument_list|)
operator|.
name|doesNotContain
argument_list|(
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
name|N3
argument_list|,
name|N4
argument_list|)
argument_list|)
expr_stmt|;
comment|// nodes not in graph
block|}
DECL|method|containsExactlySanityCheck (Collection<?> collection, Object... varargs)
specifier|private
specifier|static
name|void
name|containsExactlySanityCheck
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|,
name|Object
modifier|...
name|varargs
parameter_list|)
block|{
name|assertThat
argument_list|(
name|collection
argument_list|)
operator|.
name|hasSize
argument_list|(
name|varargs
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|varargs
control|)
block|{
name|assertThat
argument_list|(
name|collection
argument_list|)
operator|.
name|contains
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|collection
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|varargs
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

