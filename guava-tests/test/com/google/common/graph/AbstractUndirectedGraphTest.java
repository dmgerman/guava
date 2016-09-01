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
comment|/**  * Abstract base class for testing undirected implementations of the {@link Graph} interface.  *  *<p>This class is responsible for testing that an undirected implementation of {@link Graph}  * is correctly handling undirected edges.  Implementation-dependent test cases are left to  * subclasses. Test cases that do not require the graph to be undirected are found in superclasses.  */
end_comment

begin_class
DECL|class|AbstractUndirectedGraphTest
specifier|public
specifier|abstract
class|class
name|AbstractUndirectedGraphTest
extends|extends
name|AbstractGraphTest
block|{
annotation|@
name|After
DECL|method|validateUndirectedEdges ()
specifier|public
name|void
name|validateUndirectedEdges
parameter_list|()
block|{
for|for
control|(
name|Integer
name|node
range|:
name|graph
operator|.
name|nodes
argument_list|()
control|)
block|{
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|graph
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
argument_list|,
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
argument_list|,
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|predecessors_oneEdge ()
specifier|public
name|void
name|predecessors_oneEdge
parameter_list|()
block|{
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
name|N2
argument_list|)
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
name|N2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|successors
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
block|}
annotation|@
name|Test
DECL|method|inDegree_oneEdge ()
specifier|public
name|void
name|inDegree_oneEdge
parameter_list|()
block|{
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
name|inDegree
argument_list|(
name|N2
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
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
block|}
annotation|@
name|Test
DECL|method|outDegree_oneEdge ()
specifier|public
name|void
name|outDegree_oneEdge
parameter_list|()
block|{
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
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|outDegree
argument_list|(
name|N2
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
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
name|assertThat
argument_list|(
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|)
operator|.
name|isTrue
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
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|putEdge
argument_list|(
name|N2
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
DECL|method|removeEdge_antiparallelEdges ()
specifier|public
name|void
name|removeEdge_antiparallelEdges
parameter_list|()
block|{
name|putEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
expr_stmt|;
name|putEdge
argument_list|(
name|N2
argument_list|,
name|N1
argument_list|)
expr_stmt|;
comment|// no-op
name|assertThat
argument_list|(
name|graph
operator|.
name|removeEdge
argument_list|(
name|N1
argument_list|,
name|N2
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
name|adjacentNodes
argument_list|(
name|N1
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edges
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|removeEdge
argument_list|(
name|N2
argument_list|,
name|N1
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

