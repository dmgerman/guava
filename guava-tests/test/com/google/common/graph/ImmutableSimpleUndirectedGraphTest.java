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
comment|/**  * Tests for {@link ImmutableUndirectedGraph}, creating a simple undirected graph (parallel and  * self-loop edges are not allowed)  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4
operator|.
name|class
argument_list|)
DECL|class|ImmutableSimpleUndirectedGraphTest
specifier|public
class|class
name|ImmutableSimpleUndirectedGraphTest
extends|extends
name|AbstractImmutableUndirectedGraphTest
block|{
DECL|field|builder
specifier|protected
name|ImmutableUndirectedGraph
operator|.
name|Builder
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|builder
decl_stmt|;
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
DECL|method|addNode (Integer n)
specifier|final
name|boolean
name|addNode
parameter_list|(
name|Integer
name|n
parameter_list|)
block|{
name|UndirectedGraph
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|oldGraph
init|=
name|Graphs
operator|.
name|copyOf
argument_list|(
name|undirectedGraph
argument_list|)
decl_stmt|;
name|graph
operator|=
name|undirectedGraph
operator|=
name|builder
operator|.
name|addNode
argument_list|(
name|n
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
return|return
operator|!
name|graph
operator|.
name|equals
argument_list|(
name|oldGraph
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
DECL|method|addEdge (String e, Integer n1, Integer n2)
specifier|final
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
name|UndirectedGraph
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|oldGraph
init|=
name|Graphs
operator|.
name|copyOf
argument_list|(
name|undirectedGraph
argument_list|)
decl_stmt|;
name|graph
operator|=
name|undirectedGraph
operator|=
name|builder
operator|.
name|addEdge
argument_list|(
name|e
argument_list|,
name|n1
argument_list|,
name|n2
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
return|return
operator|!
name|graph
operator|.
name|equals
argument_list|(
name|oldGraph
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createGraph ()
specifier|public
name|ImmutableUndirectedGraph
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
name|ImmutableUndirectedGraph
operator|.
name|builder
argument_list|(
name|Graphs
operator|.
name|config
argument_list|()
operator|.
name|noSelfLoops
argument_list|()
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
DECL|method|addEdge_selfLoop ()
specifier|public
name|void
name|addEdge_selfLoop
parameter_list|()
block|{
try|try
block|{
name|addEdge
argument_list|(
name|E11
argument_list|,
name|N1
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
name|ERROR_ADDED_SELF_LOOP
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
name|ERROR_SELF_LOOP
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * This test checks an implementation dependent feature. It tests that    * the method {@code addEdge} will silently add the missing nodes to the builder,    * then add the edge connecting them. We are not using the proxy methods here    * as we want to test {@code addEdge} when the end-points are not elements    * of the graph.    */
annotation|@
name|Test
DECL|method|addEdge_nodesNotInGraph ()
specifier|public
name|void
name|addEdge_nodesNotInGraph
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|addEdge
argument_list|(
name|E15
argument_list|,
name|N1
argument_list|,
name|N5
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|addEdge
argument_list|(
name|E41
argument_list|,
name|N4
argument_list|,
name|N1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|addEdge
argument_list|(
name|E23
argument_list|,
name|N2
argument_list|,
name|N3
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|undirectedGraph
operator|.
name|nodes
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|N1
argument_list|,
name|N5
argument_list|,
name|N4
argument_list|,
name|N2
argument_list|,
name|N3
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|undirectedGraph
operator|.
name|edges
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|E15
argument_list|,
name|E41
argument_list|,
name|E23
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|undirectedGraph
operator|.
name|edgesConnecting
argument_list|(
name|N1
argument_list|,
name|N5
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|E15
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|undirectedGraph
operator|.
name|edgesConnecting
argument_list|(
name|N4
argument_list|,
name|N1
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|E41
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|undirectedGraph
operator|.
name|edgesConnecting
argument_list|(
name|N2
argument_list|,
name|N3
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|E23
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|undirectedGraph
operator|.
name|edgesConnecting
argument_list|(
name|N3
argument_list|,
name|N2
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|E23
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|copyOf_nullArgument ()
specifier|public
name|void
name|copyOf_nullArgument
parameter_list|()
block|{
try|try
block|{
name|ImmutableUndirectedGraph
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|unused
init|=
name|ImmutableUndirectedGraph
operator|.
name|copyOf
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|fail
argument_list|(
literal|"Should have rejected a null graph"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
block|}
annotation|@
name|Test
DECL|method|copyOf ()
specifier|public
name|void
name|copyOf
parameter_list|()
block|{
name|UndirectedGraph
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|graph
init|=
name|Graphs
operator|.
name|createUndirected
argument_list|(
name|undirectedGraph
operator|.
name|config
argument_list|()
argument_list|)
decl_stmt|;
name|populateInputGraph
argument_list|(
name|graph
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ImmutableUndirectedGraph
operator|.
name|copyOf
argument_list|(
name|graph
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|graph
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addGraph_incompatibleMultigraphConfig ()
specifier|public
name|void
name|addGraph_incompatibleMultigraphConfig
parameter_list|()
block|{
try|try
block|{
name|UndirectedGraph
name|multigraph
init|=
name|Graphs
operator|.
name|createUndirected
argument_list|(
name|Graphs
operator|.
name|MULTIGRAPH
argument_list|)
decl_stmt|;
name|ImmutableUndirectedGraph
operator|.
name|Builder
name|immutableGraphBuilder
init|=
name|ImmutableUndirectedGraph
operator|.
name|builder
argument_list|()
decl_stmt|;
name|immutableGraphBuilder
operator|.
name|addGraph
argument_list|(
name|multigraph
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have rejected a graph with an incompatible multigraph configuration"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
annotation|@
name|Test
DECL|method|addGraph_incompatibleSelfLoopConfig ()
specifier|public
name|void
name|addGraph_incompatibleSelfLoopConfig
parameter_list|()
block|{
try|try
block|{
name|UndirectedGraph
name|graph
init|=
name|Graphs
operator|.
name|createUndirected
argument_list|(
name|Graphs
operator|.
name|config
argument_list|()
operator|.
name|noSelfLoops
argument_list|()
argument_list|)
decl_stmt|;
name|ImmutableUndirectedGraph
operator|.
name|Builder
name|immutableGraphBuilder
init|=
name|ImmutableUndirectedGraph
operator|.
name|builder
argument_list|()
decl_stmt|;
name|immutableGraphBuilder
operator|.
name|addGraph
argument_list|(
name|graph
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have rejected a graph with an incompatible self-loop configuration"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
annotation|@
name|Test
DECL|method|addGraph ()
specifier|public
name|void
name|addGraph
parameter_list|()
block|{
name|UndirectedGraph
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|graph
init|=
name|Graphs
operator|.
name|createUndirected
argument_list|(
name|undirectedGraph
operator|.
name|config
argument_list|()
argument_list|)
decl_stmt|;
name|populateInputGraph
argument_list|(
name|graph
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|builder
operator|.
name|addGraph
argument_list|(
name|graph
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|graph
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addGraph_overlap ()
specifier|public
name|void
name|addGraph_overlap
parameter_list|()
block|{
name|UndirectedGraph
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|graph
init|=
name|Graphs
operator|.
name|createUndirected
argument_list|(
name|undirectedGraph
operator|.
name|config
argument_list|()
argument_list|)
decl_stmt|;
name|populateInputGraph
argument_list|(
name|graph
argument_list|)
expr_stmt|;
comment|// Add an edge that is in 'graph' (overlap)
name|builder
operator|.
name|addEdge
argument_list|(
name|E12
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addGraph
argument_list|(
name|graph
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|graph
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addGraph_inconsistentEdges ()
specifier|public
name|void
name|addGraph_inconsistentEdges
parameter_list|()
block|{
name|UndirectedGraph
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|graph
init|=
name|Graphs
operator|.
name|createUndirected
argument_list|(
name|undirectedGraph
operator|.
name|config
argument_list|()
argument_list|)
decl_stmt|;
name|populateInputGraph
argument_list|(
name|graph
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addEdge
argument_list|(
name|E12
argument_list|,
name|N5
argument_list|,
name|N1
argument_list|)
expr_stmt|;
try|try
block|{
name|builder
operator|.
name|addGraph
argument_list|(
name|graph
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have rejected a graph whose edge definitions were inconsistent with existing"
operator|+
literal|"builder state"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|populateInputGraph (UndirectedGraph<Integer, String> graph)
specifier|protected
name|void
name|populateInputGraph
parameter_list|(
name|UndirectedGraph
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|graph
parameter_list|)
block|{
name|graph
operator|.
name|addEdge
argument_list|(
name|E12
argument_list|,
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|graph
operator|.
name|addEdge
argument_list|(
name|E23
argument_list|,
name|N2
argument_list|,
name|N3
argument_list|)
expr_stmt|;
name|graph
operator|.
name|addNode
argument_list|(
name|N5
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

