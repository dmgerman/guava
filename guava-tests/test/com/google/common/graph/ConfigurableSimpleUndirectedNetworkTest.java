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
comment|/**  * Tests for an undirected {@link ConfigurableMutableNetwork}, creating a simple undirected graph  * (parallel and self-loop edges are not allowed).  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4
operator|.
name|class
argument_list|)
DECL|class|ConfigurableSimpleUndirectedNetworkTest
specifier|public
class|class
name|ConfigurableSimpleUndirectedNetworkTest
extends|extends
name|AbstractUndirectedNetworkTest
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
name|undirected
argument_list|()
operator|.
name|allowsParallelEdges
argument_list|(
literal|false
argument_list|)
operator|.
name|allowsSelfLoops
argument_list|(
literal|false
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|nodes_checkReturnedSetMutability ()
specifier|public
name|void
name|nodes_checkReturnedSetMutability
parameter_list|()
block|{
name|Set
argument_list|<
name|Integer
argument_list|>
name|nodes
init|=
name|network
operator|.
name|nodes
argument_list|()
decl_stmt|;
try|try
block|{
name|nodes
operator|.
name|add
argument_list|(
name|N2
argument_list|)
expr_stmt|;
name|fail
argument_list|(
name|ERROR_MODIFIABLE_COLLECTION
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
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
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|edges_checkReturnedSetMutability ()
specifier|public
name|void
name|edges_checkReturnedSetMutability
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|edges
init|=
name|network
operator|.
name|edges
argument_list|()
decl_stmt|;
try|try
block|{
name|edges
operator|.
name|add
argument_list|(
name|E12
argument_list|)
expr_stmt|;
name|fail
argument_list|(
name|ERROR_MODIFIABLE_COLLECTION
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
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
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|incidentEdges_checkReturnedSetMutability ()
specifier|public
name|void
name|incidentEdges_checkReturnedSetMutability
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|incidentEdges
init|=
name|network
operator|.
name|incidentEdges
argument_list|(
name|N1
argument_list|)
decl_stmt|;
try|try
block|{
name|incidentEdges
operator|.
name|add
argument_list|(
name|E12
argument_list|)
expr_stmt|;
name|fail
argument_list|(
name|ERROR_MODIFIABLE_COLLECTION
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
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
name|incidentEdges
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|incidentEdges
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|adjacentNodes_checkReturnedSetMutability ()
specifier|public
name|void
name|adjacentNodes_checkReturnedSetMutability
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Integer
argument_list|>
name|adjacentNodes
init|=
name|network
operator|.
name|adjacentNodes
argument_list|(
name|N1
argument_list|)
decl_stmt|;
try|try
block|{
name|adjacentNodes
operator|.
name|add
argument_list|(
name|N2
argument_list|)
expr_stmt|;
name|fail
argument_list|(
name|ERROR_MODIFIABLE_COLLECTION
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
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
name|containsExactlyElementsIn
argument_list|(
name|adjacentNodes
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|adjacentEdges_checkReturnedSetMutability ()
specifier|public
name|void
name|adjacentEdges_checkReturnedSetMutability
parameter_list|()
block|{
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|adjacentEdges
init|=
name|network
operator|.
name|adjacentEdges
argument_list|(
name|E12
argument_list|)
decl_stmt|;
try|try
block|{
name|adjacentEdges
operator|.
name|add
argument_list|(
name|E23
argument_list|)
expr_stmt|;
name|fail
argument_list|(
name|ERROR_MODIFIABLE_COLLECTION
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
name|addEdge
argument_list|(
name|N2
argument_list|,
name|N3
argument_list|,
name|E23
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
operator|.
name|adjacentEdges
argument_list|(
name|E12
argument_list|)
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|adjacentEdges
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|edgesConnecting_checkReturnedSetMutability ()
specifier|public
name|void
name|edgesConnecting_checkReturnedSetMutability
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
name|Set
argument_list|<
name|String
argument_list|>
name|edgesConnecting
init|=
name|network
operator|.
name|edgesConnecting
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
decl_stmt|;
try|try
block|{
name|edgesConnecting
operator|.
name|add
argument_list|(
name|E23
argument_list|)
expr_stmt|;
name|fail
argument_list|(
name|ERROR_MODIFIABLE_COLLECTION
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
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
name|containsExactlyElementsIn
argument_list|(
name|edgesConnecting
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|inEdges_checkReturnedSetMutability ()
specifier|public
name|void
name|inEdges_checkReturnedSetMutability
parameter_list|()
block|{
name|addNode
argument_list|(
name|N2
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|inEdges
init|=
name|network
operator|.
name|inEdges
argument_list|(
name|N2
argument_list|)
decl_stmt|;
try|try
block|{
name|inEdges
operator|.
name|add
argument_list|(
name|E12
argument_list|)
expr_stmt|;
name|fail
argument_list|(
name|ERROR_MODIFIABLE_COLLECTION
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
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
name|inEdges
argument_list|(
name|N2
argument_list|)
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|inEdges
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|outEdges_checkReturnedSetMutability ()
specifier|public
name|void
name|outEdges_checkReturnedSetMutability
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|outEdges
init|=
name|network
operator|.
name|outEdges
argument_list|(
name|N1
argument_list|)
decl_stmt|;
try|try
block|{
name|outEdges
operator|.
name|add
argument_list|(
name|E12
argument_list|)
expr_stmt|;
name|fail
argument_list|(
name|ERROR_MODIFIABLE_COLLECTION
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
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
name|containsExactlyElementsIn
argument_list|(
name|outEdges
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|predecessors_checkReturnedSetMutability ()
specifier|public
name|void
name|predecessors_checkReturnedSetMutability
parameter_list|()
block|{
name|addNode
argument_list|(
name|N2
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Integer
argument_list|>
name|predecessors
init|=
name|network
operator|.
name|predecessors
argument_list|(
name|N2
argument_list|)
decl_stmt|;
try|try
block|{
name|predecessors
operator|.
name|add
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
name|ERROR_MODIFIABLE_COLLECTION
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
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
name|predecessors
argument_list|(
name|N2
argument_list|)
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|predecessors
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|successors_checkReturnedSetMutability ()
specifier|public
name|void
name|successors_checkReturnedSetMutability
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Integer
argument_list|>
name|successors
init|=
name|network
operator|.
name|successors
argument_list|(
name|N1
argument_list|)
decl_stmt|;
try|try
block|{
name|successors
operator|.
name|add
argument_list|(
name|N2
argument_list|)
expr_stmt|;
name|fail
argument_list|(
name|ERROR_MODIFIABLE_COLLECTION
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
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
name|containsExactlyElementsIn
argument_list|(
name|successors
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Element Mutation
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
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
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
comment|/**    * This test checks an implementation dependent feature. It tests that    * the method {@code addEdge} will silently add the missing nodes to the graph,    * then add the edge connecting them. We are not using the proxy methods here    * as we want to test {@code addEdge} when the end-points are not elements    * of the graph.    */
annotation|@
name|Test
DECL|method|addEdge_nodesNotInGraph ()
specifier|public
name|void
name|addEdge_nodesNotInGraph
parameter_list|()
block|{
name|network
operator|.
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|network
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N5
argument_list|,
name|E15
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|network
operator|.
name|addEdge
argument_list|(
name|N4
argument_list|,
name|N1
argument_list|,
name|E41
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|network
operator|.
name|addEdge
argument_list|(
name|N2
argument_list|,
name|N3
argument_list|,
name|E23
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
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
name|network
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
name|network
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
name|network
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
name|network
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
name|network
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
block|}
end_class

end_unit

