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
comment|/** Tests for a directed {@link ConfigurableMutableGraph} allowing self-loops. */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4
operator|.
name|class
argument_list|)
DECL|class|ConfigurableDirectedGraphTest
specifier|public
class|class
name|ConfigurableDirectedGraphTest
extends|extends
name|ConfigurableSimpleDirectedGraphTest
block|{
annotation|@
name|Override
DECL|method|createGraph ()
specifier|public
name|MutableGraph
argument_list|<
name|Integer
argument_list|>
name|createGraph
parameter_list|()
block|{
return|return
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
return|;
block|}
annotation|@
name|Test
DECL|method|adjacentNodes_selfLoop ()
specifier|public
name|void
name|adjacentNodes_selfLoop
parameter_list|()
block|{
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|)
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
DECL|method|predecessors_selfLoop ()
specifier|public
name|void
name|predecessors_selfLoop
parameter_list|()
block|{
name|putEdge
argument_list|(
name|N1
argument_list|,
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
name|containsExactly
argument_list|(
name|N1
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
name|graph
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
name|putEdge
argument_list|(
name|N1
argument_list|,
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
name|containsExactly
argument_list|(
name|N1
argument_list|)
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
name|N1
argument_list|,
name|N2
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
name|putEdge
argument_list|(
name|N1
argument_list|,
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
name|containsExactly
argument_list|(
name|EndpointPair
operator|.
name|ordered
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|)
argument_list|)
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
name|incidentEdges
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|EndpointPair
operator|.
name|ordered
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|)
argument_list|,
name|EndpointPair
operator|.
name|ordered
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
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
name|putEdge
argument_list|(
name|N1
argument_list|,
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
literal|2
argument_list|)
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
name|putEdge
argument_list|(
name|N1
argument_list|,
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
literal|1
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
name|putEdge
argument_list|(
name|N1
argument_list|,
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
literal|1
argument_list|)
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
name|assertThat
argument_list|(
name|putEdge
argument_list|(
name|N1
argument_list|,
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
name|containsExactly
argument_list|(
name|N1
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
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeNode_existingNodeWithSelfLoopEdge ()
specifier|public
name|void
name|removeNode_existingNodeWithSelfLoopEdge
parameter_list|()
block|{
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|putEdge
argument_list|(
name|N1
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
name|isEmpty
argument_list|()
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
name|putEdge
argument_list|(
name|N1
argument_list|,
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
block|}
end_class

end_unit

