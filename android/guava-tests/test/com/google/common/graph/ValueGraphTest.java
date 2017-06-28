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
name|graph
operator|.
name|TestUtil
operator|.
name|assertStronglyEquivalent
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
comment|/** Tests for {@link ConfigurableMutableValueGraph} and related functionality. */
end_comment

begin_comment
comment|// TODO(user): Expand coverage and move to proper test suite.
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4
operator|.
name|class
argument_list|)
DECL|class|ValueGraphTest
specifier|public
specifier|final
class|class
name|ValueGraphTest
block|{
DECL|field|graph
name|MutableValueGraph
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|graph
decl_stmt|;
annotation|@
name|After
DECL|method|validateGraphState ()
specifier|public
name|void
name|validateGraphState
parameter_list|()
block|{
name|assertStronglyEquivalent
argument_list|(
name|graph
argument_list|,
name|Graphs
operator|.
name|copyOf
argument_list|(
name|graph
argument_list|)
argument_list|)
expr_stmt|;
name|assertStronglyEquivalent
argument_list|(
name|graph
argument_list|,
name|ImmutableValueGraph
operator|.
name|copyOf
argument_list|(
name|graph
argument_list|)
argument_list|)
expr_stmt|;
name|Graph
argument_list|<
name|Integer
argument_list|>
name|asGraph
init|=
name|graph
operator|.
name|asGraph
argument_list|()
decl_stmt|;
name|AbstractGraphTest
operator|.
name|validateGraph
argument_list|(
name|asGraph
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
name|isEqualTo
argument_list|(
name|asGraph
operator|.
name|nodes
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edges
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|asGraph
operator|.
name|edges
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|nodeOrder
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|asGraph
operator|.
name|nodeOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|isDirected
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|asGraph
operator|.
name|isDirected
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|allowsSelfLoops
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|asGraph
operator|.
name|allowsSelfLoops
argument_list|()
argument_list|)
expr_stmt|;
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
name|assertThat
argument_list|(
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|asGraph
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|asGraph
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|asGraph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|degree
argument_list|(
name|node
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|asGraph
operator|.
name|degree
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|inDegree
argument_list|(
name|node
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|asGraph
operator|.
name|inDegree
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|outDegree
argument_list|(
name|node
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|asGraph
operator|.
name|outDegree
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Integer
name|otherNode
range|:
name|graph
operator|.
name|nodes
argument_list|()
control|)
block|{
name|boolean
name|hasEdge
init|=
name|graph
operator|.
name|hasEdgeConnecting
argument_list|(
name|node
argument_list|,
name|otherNode
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|hasEdge
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|asGraph
operator|.
name|hasEdgeConnecting
argument_list|(
name|node
argument_list|,
name|otherNode
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgeValueOrDefault
argument_list|(
name|node
argument_list|,
name|otherNode
argument_list|,
literal|null
argument_list|)
operator|!=
literal|null
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|hasEdge
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|directedGraph ()
specifier|public
name|void
name|directedGraph
parameter_list|()
block|{
name|graph
operator|=
name|ValueGraphBuilder
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
expr_stmt|;
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"valueA"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"valueB"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|,
literal|"valueC"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|4
argument_list|,
literal|4
argument_list|,
literal|"valueD"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgeValue
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueA"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgeValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueB"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgeValue
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueC"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgeValue
argument_list|(
literal|4
argument_list|,
literal|4
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueD"
argument_list|)
expr_stmt|;
name|String
name|toString
init|=
name|graph
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|toString
argument_list|)
operator|.
name|contains
argument_list|(
literal|"valueA"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|toString
argument_list|)
operator|.
name|contains
argument_list|(
literal|"valueB"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|toString
argument_list|)
operator|.
name|contains
argument_list|(
literal|"valueC"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|toString
argument_list|)
operator|.
name|contains
argument_list|(
literal|"valueD"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|undirectedGraph ()
specifier|public
name|void
name|undirectedGraph
parameter_list|()
block|{
name|graph
operator|=
name|ValueGraphBuilder
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
expr_stmt|;
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"valueA"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"valueB"
argument_list|)
expr_stmt|;
comment|// overwrites valueA in undirected case
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|,
literal|"valueC"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|4
argument_list|,
literal|4
argument_list|,
literal|"valueD"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgeValue
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueB"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgeValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueB"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgeValue
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueC"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgeValue
argument_list|(
literal|4
argument_list|,
literal|4
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueD"
argument_list|)
expr_stmt|;
name|String
name|toString
init|=
name|graph
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|toString
argument_list|)
operator|.
name|doesNotContain
argument_list|(
literal|"valueA"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|toString
argument_list|)
operator|.
name|contains
argument_list|(
literal|"valueB"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|toString
argument_list|)
operator|.
name|contains
argument_list|(
literal|"valueC"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|toString
argument_list|)
operator|.
name|contains
argument_list|(
literal|"valueD"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|putEdgeValue_directed ()
specifier|public
name|void
name|putEdgeValue_directed
parameter_list|()
block|{
name|graph
operator|=
name|ValueGraphBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"valueA"
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"valueB"
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"valueC"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueA"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"valueD"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueB"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|putEdgeValue_undirected ()
specifier|public
name|void
name|putEdgeValue_undirected
parameter_list|()
block|{
name|graph
operator|=
name|ValueGraphBuilder
operator|.
name|undirected
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"valueA"
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"valueB"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueA"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"valueC"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueB"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"valueD"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueC"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeEdge_directed ()
specifier|public
name|void
name|removeEdge_directed
parameter_list|()
block|{
name|graph
operator|=
name|ValueGraphBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"valueA"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"valueB"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|,
literal|"valueC"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|removeEdge
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueA"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|removeEdge
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|removeEdge
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueB"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|removeEdge
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|removeEdge
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueC"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|removeEdge
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeEdge_undirected ()
specifier|public
name|void
name|removeEdge_undirected
parameter_list|()
block|{
name|graph
operator|=
name|ValueGraphBuilder
operator|.
name|undirected
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"valueA"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"valueB"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|,
literal|"valueC"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|removeEdge
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueB"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|removeEdge
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|removeEdge
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|removeEdge
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueC"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|removeEdge
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|edgeValue_edgeNotPresent ()
specifier|public
name|void
name|edgeValue_edgeNotPresent
parameter_list|()
block|{
name|graph
operator|=
name|ValueGraphBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
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
try|try
block|{
name|graph
operator|.
name|edgeValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have rejected edgeValue() if edge not present in graph."
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
argument_list|)
operator|.
name|hasMessage
argument_list|(
literal|"Edge connecting 2 to 1 is not present in this graph."
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|edgeValue_nodeNotPresent ()
specifier|public
name|void
name|edgeValue_nodeNotPresent
parameter_list|()
block|{
name|graph
operator|=
name|ValueGraphBuilder
operator|.
name|undirected
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
try|try
block|{
name|graph
operator|.
name|edgeValue
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have rejected edgeValue() if node not present in graph."
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
argument_list|)
operator|.
name|hasMessage
argument_list|(
literal|"Node 3 is not an element of this graph."
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|edgeValueOrDefault ()
specifier|public
name|void
name|edgeValueOrDefault
parameter_list|()
block|{
name|graph
operator|=
name|ValueGraphBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgeValueOrDefault
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"default"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgeValueOrDefault
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"default"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"valueA"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"valueB"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgeValueOrDefault
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"default"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueA"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgeValueOrDefault
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"default"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueB"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|removeEdge
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"valueC"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgeValueOrDefault
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"default"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgeValueOrDefault
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"default"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"valueC"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|equivalence_considersEdgeValue ()
specifier|public
name|void
name|equivalence_considersEdgeValue
parameter_list|()
block|{
name|graph
operator|=
name|ValueGraphBuilder
operator|.
name|undirected
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
name|graph
operator|.
name|putEdgeValue
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"valueA"
argument_list|)
expr_stmt|;
name|MutableValueGraph
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|otherGraph
init|=
name|ValueGraphBuilder
operator|.
name|undirected
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|otherGraph
operator|.
name|putEdgeValue
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"valueA"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|otherGraph
argument_list|)
expr_stmt|;
name|otherGraph
operator|.
name|putEdgeValue
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"valueB"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|otherGraph
argument_list|)
expr_stmt|;
comment|// values differ
block|}
block|}
end_class

end_unit

