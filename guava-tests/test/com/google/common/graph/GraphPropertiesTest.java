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
name|GraphProperties
operator|.
name|isCyclic
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
name|graph
operator|.
name|GraphProperties
operator|.
name|roots
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
comment|/**  * Tests for {@link GraphProperties}.  */
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
DECL|field|E11
specifier|private
specifier|static
specifier|final
name|String
name|E11
init|=
literal|"1-1"
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
DECL|field|E13
specifier|private
specifier|static
specifier|final
name|String
name|E13
init|=
literal|"1-3"
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
DECL|field|E23
specifier|private
specifier|static
specifier|final
name|String
name|E23
init|=
literal|"2-3"
decl_stmt|;
DECL|field|E31
specifier|private
specifier|static
specifier|final
name|String
name|E31
init|=
literal|"3-1"
decl_stmt|;
annotation|@
name|Test
DECL|method|isCyclic_emptyGraph ()
specifier|public
name|void
name|isCyclic_emptyGraph
parameter_list|()
block|{
name|Network
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
init|=
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|isCyclic
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|isCyclic_isolatedNodes ()
specifier|public
name|void
name|isCyclic_isolatedNodes
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
init|=
name|NetworkBuilder
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
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isCyclic
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|directedGraph
operator|.
name|addNode
argument_list|(
name|N2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isCyclic
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|isCyclic_oneEdge ()
specifier|public
name|void
name|isCyclic_oneEdge
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
init|=
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|directedGraph
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
name|assertThat
argument_list|(
name|isCyclic
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|isCyclic_selfLoopEdge ()
specifier|public
name|void
name|isCyclic_selfLoopEdge
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
init|=
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|directedGraph
operator|.
name|addEdge
argument_list|(
name|E11
argument_list|,
name|N1
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isCyclic
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|isCyclic_twoParallelEdges ()
specifier|public
name|void
name|isCyclic_twoParallelEdges
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
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
name|build
argument_list|()
decl_stmt|;
name|directedGraph
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
name|directedGraph
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
name|assertThat
argument_list|(
name|isCyclic
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|isCyclic_twoAcyclicEdges ()
specifier|public
name|void
name|isCyclic_twoAcyclicEdges
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
init|=
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|directedGraph
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
name|directedGraph
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
name|assertThat
argument_list|(
name|isCyclic
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|isCyclic_twoCyclicEdges ()
specifier|public
name|void
name|isCyclic_twoCyclicEdges
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
init|=
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|directedGraph
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
name|directedGraph
operator|.
name|addEdge
argument_list|(
name|E21
argument_list|,
name|N2
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isCyclic
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|isCyclic_threeAcyclicEdges ()
specifier|public
name|void
name|isCyclic_threeAcyclicEdges
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
init|=
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|directedGraph
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
name|directedGraph
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
name|directedGraph
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
name|assertThat
argument_list|(
name|isCyclic
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|isCyclic_threeCyclicEdges ()
specifier|public
name|void
name|isCyclic_threeCyclicEdges
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
init|=
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|directedGraph
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
name|directedGraph
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
name|directedGraph
operator|.
name|addEdge
argument_list|(
name|E31
argument_list|,
name|N3
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isCyclic
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|isCyclic_disconnectedCyclicGraph ()
specifier|public
name|void
name|isCyclic_disconnectedCyclicGraph
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
init|=
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|directedGraph
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
name|directedGraph
operator|.
name|addEdge
argument_list|(
name|E21
argument_list|,
name|N2
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|directedGraph
operator|.
name|addNode
argument_list|(
name|N3
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isCyclic
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|isCyclic_cyclicMultigraph ()
specifier|public
name|void
name|isCyclic_cyclicMultigraph
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
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
name|build
argument_list|()
decl_stmt|;
name|directedGraph
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
name|directedGraph
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
name|directedGraph
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
name|directedGraph
operator|.
name|addEdge
argument_list|(
name|E31
argument_list|,
name|N3
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isCyclic
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|isCyclic_multipleCycles ()
specifier|public
name|void
name|isCyclic_multipleCycles
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
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
name|build
argument_list|()
decl_stmt|;
name|directedGraph
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
name|directedGraph
operator|.
name|addEdge
argument_list|(
name|E21
argument_list|,
name|N2
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|directedGraph
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
name|directedGraph
operator|.
name|addEdge
argument_list|(
name|E31
argument_list|,
name|N3
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|isCyclic
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|roots_emptyGraph ()
specifier|public
name|void
name|roots_emptyGraph
parameter_list|()
block|{
name|Network
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
init|=
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|roots
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|roots_trivialGraph ()
specifier|public
name|void
name|roots_trivialGraph
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
init|=
name|NetworkBuilder
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
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|roots
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|N1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|roots_nodeWithSelfLoop ()
specifier|public
name|void
name|roots_nodeWithSelfLoop
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
init|=
name|NetworkBuilder
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
name|N1
argument_list|)
expr_stmt|;
name|directedGraph
operator|.
name|addEdge
argument_list|(
name|E11
argument_list|,
name|N1
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|roots
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|roots_nodeWithChildren ()
specifier|public
name|void
name|roots_nodeWithChildren
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
init|=
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|directedGraph
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
name|directedGraph
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
name|assertThat
argument_list|(
name|roots
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|N1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|roots_cycle ()
specifier|public
name|void
name|roots_cycle
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
init|=
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|directedGraph
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
name|directedGraph
operator|.
name|addEdge
argument_list|(
name|E21
argument_list|,
name|N2
argument_list|,
name|N1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|roots
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|roots_multipleRoots ()
specifier|public
name|void
name|roots_multipleRoots
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|directedGraph
init|=
name|NetworkBuilder
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
name|N1
argument_list|)
expr_stmt|;
name|directedGraph
operator|.
name|addNode
argument_list|(
name|N2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|roots
argument_list|(
name|directedGraph
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

