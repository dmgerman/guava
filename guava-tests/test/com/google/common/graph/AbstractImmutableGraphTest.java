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
name|Rule
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
name|rules
operator|.
name|ExpectedException
import|;
end_import

begin_comment
comment|/**  * Abstract base class for testing immutable implementations of the {@link Graph}  * interface.  *  *<p>This class is testing that all mutation methods called directly  * on the immutable graph will throw {@code UnsupportedOperationException}. Also,  * it tests the builder mutation methods {@code addNode} and {@code addEdge}.  * Any other test cases should be either included in the superclasses or subclasses.  *  */
end_comment

begin_class
DECL|class|AbstractImmutableGraphTest
specifier|public
specifier|abstract
class|class
name|AbstractImmutableGraphTest
extends|extends
name|AbstractGraphTest
block|{
DECL|field|expectedException
annotation|@
name|Rule
specifier|public
specifier|final
name|ExpectedException
name|expectedException
init|=
name|ExpectedException
operator|.
name|none
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|nodes_checkReturnedSetMutability ()
specifier|public
specifier|final
name|void
name|nodes_checkReturnedSetMutability
parameter_list|()
block|{
name|expectedException
operator|.
name|expect
argument_list|(
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
expr_stmt|;
name|graph
operator|.
name|nodes
argument_list|()
operator|.
name|add
argument_list|(
name|N2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|edges_checkReturnedSetMutability ()
specifier|public
specifier|final
name|void
name|edges_checkReturnedSetMutability
parameter_list|()
block|{
name|expectedException
operator|.
name|expect
argument_list|(
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
expr_stmt|;
name|graph
operator|.
name|edges
argument_list|()
operator|.
name|add
argument_list|(
name|E12
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|incidentEdges_checkReturnedSetMutability ()
specifier|public
specifier|final
name|void
name|incidentEdges_checkReturnedSetMutability
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|expectedException
operator|.
name|expect
argument_list|(
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
expr_stmt|;
name|graph
operator|.
name|incidentEdges
argument_list|(
name|N1
argument_list|)
operator|.
name|add
argument_list|(
name|E12
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|incidentNodes_checkReturnedSetMutability ()
specifier|public
specifier|final
name|void
name|incidentNodes_checkReturnedSetMutability
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
name|expectedException
operator|.
name|expect
argument_list|(
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
expr_stmt|;
name|graph
operator|.
name|incidentNodes
argument_list|(
name|E12
argument_list|)
operator|.
name|add
argument_list|(
name|N2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|adjacentNodes_checkReturnedSetMutability ()
specifier|public
specifier|final
name|void
name|adjacentNodes_checkReturnedSetMutability
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|expectedException
operator|.
name|expect
argument_list|(
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
expr_stmt|;
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|N1
argument_list|)
operator|.
name|add
argument_list|(
name|N2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|adjacentEdges_checkReturnedSetMutability ()
specifier|public
specifier|final
name|void
name|adjacentEdges_checkReturnedSetMutability
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
name|expectedException
operator|.
name|expect
argument_list|(
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
expr_stmt|;
name|graph
operator|.
name|adjacentEdges
argument_list|(
name|E12
argument_list|)
operator|.
name|add
argument_list|(
name|E23
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|edgesConnecting_checkReturnedSetMutability ()
specifier|public
specifier|final
name|void
name|edgesConnecting_checkReturnedSetMutability
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
name|expectedException
operator|.
name|expect
argument_list|(
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
expr_stmt|;
name|graph
operator|.
name|edgesConnecting
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
operator|.
name|add
argument_list|(
name|E23
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|inEdges_checkReturnedSetMutability ()
specifier|public
specifier|final
name|void
name|inEdges_checkReturnedSetMutability
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
name|expectedException
operator|.
name|expect
argument_list|(
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
expr_stmt|;
name|graph
operator|.
name|inEdges
argument_list|(
name|N2
argument_list|)
operator|.
name|add
argument_list|(
name|E23
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|outEdges_checkReturnedSetMutability ()
specifier|public
specifier|final
name|void
name|outEdges_checkReturnedSetMutability
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
name|expectedException
operator|.
name|expect
argument_list|(
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
expr_stmt|;
name|graph
operator|.
name|outEdges
argument_list|(
name|N1
argument_list|)
operator|.
name|add
argument_list|(
name|E23
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|predecessors_checkReturnedSetMutability ()
specifier|public
specifier|final
name|void
name|predecessors_checkReturnedSetMutability
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
name|expectedException
operator|.
name|expect
argument_list|(
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
expr_stmt|;
name|graph
operator|.
name|predecessors
argument_list|(
name|N2
argument_list|)
operator|.
name|add
argument_list|(
name|N1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|successors_checkReturnedSetMutability ()
specifier|public
specifier|final
name|void
name|successors_checkReturnedSetMutability
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
name|expectedException
operator|.
name|expect
argument_list|(
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
expr_stmt|;
name|graph
operator|.
name|successors
argument_list|(
name|N1
argument_list|)
operator|.
name|add
argument_list|(
name|N2
argument_list|)
expr_stmt|;
block|}
comment|// We want to test calling the mutation methods directly on the graph,
comment|// hence the proxy methods are not needed. In case of immutable graphs,
comment|// proxy methods add nodes/edges to the builder then build a new graph.
annotation|@
name|Test
DECL|method|addNode ()
specifier|public
name|void
name|addNode
parameter_list|()
block|{
name|expectedException
operator|.
name|expect
argument_list|(
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
expr_stmt|;
name|graph
operator|.
name|addNode
argument_list|(
name|N3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addEdge ()
specifier|public
name|void
name|addEdge
parameter_list|()
block|{
name|expectedException
operator|.
name|expect
argument_list|(
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
expr_stmt|;
name|graph
operator|.
name|addEdge
argument_list|(
name|E13
argument_list|,
name|N1
argument_list|,
name|N3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeNode ()
specifier|public
name|void
name|removeNode
parameter_list|()
block|{
name|expectedException
operator|.
name|expect
argument_list|(
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
expr_stmt|;
name|graph
operator|.
name|removeNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeEdge ()
specifier|public
name|void
name|removeEdge
parameter_list|()
block|{
name|expectedException
operator|.
name|expect
argument_list|(
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
expr_stmt|;
name|graph
operator|.
name|removeEdge
argument_list|(
name|E12
argument_list|)
expr_stmt|;
block|}
comment|// Builder mutation methods
annotation|@
name|Test
DECL|method|addNode_builder_newNode ()
specifier|public
name|void
name|addNode_builder_newNode
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
block|}
end_class

end_unit
