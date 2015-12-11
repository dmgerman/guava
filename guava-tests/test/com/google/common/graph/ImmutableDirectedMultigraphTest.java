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
name|assertTrue
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
comment|/**  * Tests for {@link ImmutableDirectedGraph} allowing parallel edges.  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4
operator|.
name|class
argument_list|)
DECL|class|ImmutableDirectedMultigraphTest
specifier|public
class|class
name|ImmutableDirectedMultigraphTest
extends|extends
name|ImmutableDirectedGraphTest
block|{
annotation|@
name|Override
DECL|method|createGraph ()
specifier|public
name|ImmutableDirectedGraph
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|createGraph
parameter_list|()
block|{
name|builder
operator|=
name|ImmutableDirectedGraph
operator|.
name|builder
argument_list|(
name|Graphs
operator|.
name|MULTIGRAPH
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Test
DECL|method|edgesConnecting_parallelEdges ()
specifier|public
name|void
name|edgesConnecting_parallelEdges
parameter_list|()
block|{
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
name|assertTrue
argument_list|(
name|addEdge
argument_list|(
name|E12_A
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|immutableGraph
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
argument_list|,
name|E12_A
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
comment|// Passed nodes should be in the correct edge direction, first is the
comment|// source node and the second is the target node
name|assertThat
argument_list|(
name|immutableGraph
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
DECL|method|edgesConnecting_parallelSelfLoopEdges ()
specifier|public
name|void
name|edgesConnecting_parallelSelfLoopEdges
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|addEdge
argument_list|(
name|E11
argument_list|,
name|N1
argument_list|,
name|N1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|addEdge
argument_list|(
name|E11_A
argument_list|,
name|N1
argument_list|,
name|N1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|immutableGraph
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
argument_list|,
name|E11_A
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addEdge_builder_parallelEdge ()
specifier|public
name|void
name|addEdge_builder_parallelEdge
parameter_list|()
block|{
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
name|assertTrue
argument_list|(
name|addEdge
argument_list|(
name|E12_A
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|immutableGraph
operator|.
name|edges
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|E12
argument_list|,
name|E12_A
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addEdge_builder_parallelSelfLoopEdge ()
specifier|public
name|void
name|addEdge_builder_parallelSelfLoopEdge
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|addEdge
argument_list|(
name|E11
argument_list|,
name|N1
argument_list|,
name|N1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|addEdge
argument_list|(
name|E11_A
argument_list|,
name|N1
argument_list|,
name|N1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|immutableGraph
operator|.
name|edges
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|E11
argument_list|,
name|E11_A
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|toString_parallelEdges ()
specifier|public
name|void
name|toString_parallelEdges
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
name|E12_A
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|addEdge
argument_list|(
name|E11
argument_list|,
name|N1
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|addEdge
argument_list|(
name|E11_A
argument_list|,
name|N1
argument_list|,
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
literal|"config: %s, nodes: %s, "
operator|+
literal|"edges: {%s=<%s -> %s>, %s=<%s -> %s>, %s=<%s -> %s>, %s=<%s -> %s>}"
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
name|E12_A
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|,
name|E11
argument_list|,
name|N1
argument_list|,
name|N1
argument_list|,
name|E11_A
argument_list|,
name|N1
argument_list|,
name|N1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|populateInputGraph (DirectedGraph<Integer, String> graph)
specifier|protected
name|void
name|populateInputGraph
parameter_list|(
name|DirectedGraph
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|graph
parameter_list|)
block|{
name|super
operator|.
name|populateInputGraph
argument_list|(
name|graph
argument_list|)
expr_stmt|;
comment|// Add some parallel edges
name|graph
operator|.
name|addEdge
argument_list|(
name|E11_A
argument_list|,
name|N1
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|graph
operator|.
name|addEdge
argument_list|(
name|E12_A
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

