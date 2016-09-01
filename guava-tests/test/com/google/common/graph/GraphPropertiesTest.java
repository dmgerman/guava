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
name|Graphs
operator|.
name|hasCycle
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
comment|/**  * Tests for {@link Graphs#hasCycle(Graph)} and {@link Graphs#hasCycle(Network)}.  */
end_comment

begin_comment
comment|// TODO(user): Consider moving this to GraphsTest.
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4
operator|.
name|class
argument_list|)
DECL|class|GraphPropertiesTest
specifier|public
class|class
name|GraphPropertiesTest
block|{
DECL|field|graphsToTest
name|ImmutableList
argument_list|<
name|MutableGraph
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|graphsToTest
decl_stmt|;
DECL|field|directedGraph
name|Graph
argument_list|<
name|Integer
argument_list|>
name|directedGraph
decl_stmt|;
DECL|field|undirectedGraph
name|Graph
argument_list|<
name|Integer
argument_list|>
name|undirectedGraph
decl_stmt|;
DECL|field|networksToTest
name|ImmutableList
argument_list|<
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|>
name|networksToTest
decl_stmt|;
DECL|field|directedNetwork
name|Network
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedNetwork
decl_stmt|;
DECL|field|undirectedNetwork
name|Network
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|undirectedNetwork
decl_stmt|;
annotation|@
name|Before
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
block|{
name|MutableGraph
argument_list|<
name|Integer
argument_list|>
name|mutableDirectedGraph
init|=
name|GraphBuilder
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
decl_stmt|;
name|MutableGraph
argument_list|<
name|Integer
argument_list|>
name|mutableUndirectedGraph
init|=
name|GraphBuilder
operator|.
name|undirected
argument_list|()
operator|.
name|allowsSelfLoops
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|graphsToTest
operator|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|mutableDirectedGraph
argument_list|,
name|mutableUndirectedGraph
argument_list|)
expr_stmt|;
name|directedGraph
operator|=
name|mutableDirectedGraph
expr_stmt|;
name|undirectedGraph
operator|=
name|mutableUndirectedGraph
expr_stmt|;
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|mutableDirectedNetwork
init|=
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|allowsParallelEdges
argument_list|(
literal|true
argument_list|)
operator|.
name|allowsSelfLoops
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|mutableUndirectedNetwork
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
name|allowsSelfLoops
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|networksToTest
operator|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|mutableDirectedNetwork
argument_list|,
name|mutableUndirectedNetwork
argument_list|)
expr_stmt|;
name|directedNetwork
operator|=
name|mutableDirectedNetwork
expr_stmt|;
name|undirectedNetwork
operator|=
name|mutableUndirectedNetwork
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|hasCycle_emptyGraph ()
specifier|public
name|void
name|hasCycle_emptyGraph
parameter_list|()
block|{
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|undirectedGraph
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|hasCycle_isolatedNodes ()
specifier|public
name|void
name|hasCycle_isolatedNodes
parameter_list|()
block|{
for|for
control|(
name|MutableGraph
argument_list|<
name|Integer
argument_list|>
name|graph
range|:
name|graphsToTest
control|)
block|{
name|graph
operator|.
name|addNode
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|graph
operator|.
name|addNode
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|undirectedGraph
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|hasCycle_oneEdge ()
specifier|public
name|void
name|hasCycle_oneEdge
parameter_list|()
block|{
for|for
control|(
name|MutableGraph
argument_list|<
name|Integer
argument_list|>
name|graph
range|:
name|graphsToTest
control|)
block|{
name|graph
operator|.
name|putEdge
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|undirectedGraph
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|hasCycle_selfLoopEdge ()
specifier|public
name|void
name|hasCycle_selfLoopEdge
parameter_list|()
block|{
for|for
control|(
name|MutableGraph
argument_list|<
name|Integer
argument_list|>
name|graph
range|:
name|graphsToTest
control|)
block|{
name|graph
operator|.
name|putEdge
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|undirectedGraph
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|hasCycle_twoAcyclicEdges ()
specifier|public
name|void
name|hasCycle_twoAcyclicEdges
parameter_list|()
block|{
for|for
control|(
name|MutableGraph
argument_list|<
name|Integer
argument_list|>
name|graph
range|:
name|graphsToTest
control|)
block|{
name|graph
operator|.
name|putEdge
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdge
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|undirectedGraph
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|hasCycle_twoCyclicEdges ()
specifier|public
name|void
name|hasCycle_twoCyclicEdges
parameter_list|()
block|{
for|for
control|(
name|MutableGraph
argument_list|<
name|Integer
argument_list|>
name|graph
range|:
name|graphsToTest
control|)
block|{
name|graph
operator|.
name|putEdge
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdge
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
expr_stmt|;
comment|// no-op in undirected case
block|}
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|undirectedGraph
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|hasCycle_threeAcyclicEdges ()
specifier|public
name|void
name|hasCycle_threeAcyclicEdges
parameter_list|()
block|{
for|for
control|(
name|MutableGraph
argument_list|<
name|Integer
argument_list|>
name|graph
range|:
name|graphsToTest
control|)
block|{
name|graph
operator|.
name|putEdge
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdge
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdge
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|undirectedGraph
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
comment|// cyclic in undirected case
block|}
annotation|@
name|Test
DECL|method|hasCycle_threeCyclicEdges ()
specifier|public
name|void
name|hasCycle_threeCyclicEdges
parameter_list|()
block|{
for|for
control|(
name|MutableGraph
argument_list|<
name|Integer
argument_list|>
name|graph
range|:
name|graphsToTest
control|)
block|{
name|graph
operator|.
name|putEdge
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdge
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdge
argument_list|(
literal|3
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|undirectedGraph
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|hasCycle_disconnectedCyclicGraph ()
specifier|public
name|void
name|hasCycle_disconnectedCyclicGraph
parameter_list|()
block|{
for|for
control|(
name|MutableGraph
argument_list|<
name|Integer
argument_list|>
name|graph
range|:
name|graphsToTest
control|)
block|{
name|graph
operator|.
name|putEdge
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdge
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
expr_stmt|;
comment|// no-op in undirected case
name|graph
operator|.
name|addNode
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|undirectedGraph
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|hasCycle_multipleCycles ()
specifier|public
name|void
name|hasCycle_multipleCycles
parameter_list|()
block|{
for|for
control|(
name|MutableGraph
argument_list|<
name|Integer
argument_list|>
name|graph
range|:
name|graphsToTest
control|)
block|{
name|graph
operator|.
name|putEdge
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdge
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdge
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdge
argument_list|(
literal|3
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|undirectedGraph
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|hasCycle_twoParallelEdges ()
specifier|public
name|void
name|hasCycle_twoParallelEdges
parameter_list|()
block|{
for|for
control|(
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|network
range|:
name|networksToTest
control|)
block|{
name|network
operator|.
name|addEdge
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"1-2a"
argument_list|)
expr_stmt|;
name|network
operator|.
name|addEdge
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"1-2b"
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|directedNetwork
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|undirectedNetwork
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
comment|// cyclic in undirected case
block|}
annotation|@
name|Test
DECL|method|hasCycle_cyclicMultigraph ()
specifier|public
name|void
name|hasCycle_cyclicMultigraph
parameter_list|()
block|{
for|for
control|(
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|network
range|:
name|networksToTest
control|)
block|{
name|network
operator|.
name|addEdge
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"1-2a"
argument_list|)
expr_stmt|;
name|network
operator|.
name|addEdge
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"1-2b"
argument_list|)
expr_stmt|;
name|network
operator|.
name|addEdge
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|,
literal|"2-3"
argument_list|)
expr_stmt|;
name|network
operator|.
name|addEdge
argument_list|(
literal|3
argument_list|,
literal|1
argument_list|,
literal|"3-1"
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|directedNetwork
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hasCycle
argument_list|(
name|undirectedNetwork
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

