begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2019 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/** Tests for {@link ImmutableValueGraph} . */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4
operator|.
name|class
argument_list|)
DECL|class|ImmutableValueGraphTest
specifier|public
class|class
name|ImmutableValueGraphTest
block|{
annotation|@
name|Test
DECL|method|immutableValueGraph ()
specifier|public
name|void
name|immutableValueGraph
parameter_list|()
block|{
name|MutableValueGraph
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|mutableValueGraph
init|=
name|ValueGraphBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|mutableValueGraph
operator|.
name|addNode
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|ImmutableValueGraph
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|immutableValueGraph
init|=
name|ImmutableValueGraph
operator|.
name|copyOf
argument_list|(
name|mutableValueGraph
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|immutableValueGraph
operator|.
name|asGraph
argument_list|()
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|ImmutableGraph
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|immutableValueGraph
argument_list|)
operator|.
name|isNotInstanceOf
argument_list|(
name|MutableValueGraph
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|immutableValueGraph
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|mutableValueGraph
argument_list|)
expr_stmt|;
name|mutableValueGraph
operator|.
name|addNode
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|immutableValueGraph
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|mutableValueGraph
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|copyOfImmutableValueGraph_optimized ()
specifier|public
name|void
name|copyOfImmutableValueGraph_optimized
parameter_list|()
block|{
name|ValueGraph
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|graph1
init|=
name|ImmutableValueGraph
operator|.
name|copyOf
argument_list|(
name|ValueGraphBuilder
operator|.
name|directed
argument_list|()
operator|.
operator|<
name|String
argument_list|,
name|Integer
operator|>
name|build
argument_list|()
argument_list|)
decl_stmt|;
name|ValueGraph
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|graph2
init|=
name|ImmutableValueGraph
operator|.
name|copyOf
argument_list|(
name|graph1
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|graph2
argument_list|)
operator|.
name|isSameInstanceAs
argument_list|(
name|graph1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|incidentEdgeOrder_stable ()
specifier|public
name|void
name|incidentEdgeOrder_stable
parameter_list|()
block|{
name|ImmutableValueGraph
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|immutableValueGraph
init|=
name|ImmutableValueGraph
operator|.
name|copyOf
argument_list|(
name|ValueGraphBuilder
operator|.
name|directed
argument_list|()
operator|.
operator|<
name|String
argument_list|,
name|Integer
operator|>
name|build
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|immutableValueGraph
operator|.
name|incidentEdgeOrder
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ElementOrder
operator|.
name|stable
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|incidentEdgeOrder_fromUnorderedGraph_stable ()
specifier|public
name|void
name|incidentEdgeOrder_fromUnorderedGraph_stable
parameter_list|()
block|{
name|ImmutableValueGraph
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|immutableValueGraph
init|=
name|ImmutableValueGraph
operator|.
name|copyOf
argument_list|(
name|ValueGraphBuilder
operator|.
name|directed
argument_list|()
operator|.
name|incidentEdgeOrder
argument_list|(
name|ElementOrder
operator|.
name|unordered
argument_list|()
argument_list|)
operator|.
operator|<
name|String
argument_list|,
name|Integer
operator|>
name|build
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|immutableValueGraph
operator|.
name|incidentEdgeOrder
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ElementOrder
operator|.
name|stable
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|immutableValueGraphBuilder_appliesGraphBuilderConfig ()
specifier|public
name|void
name|immutableValueGraphBuilder_appliesGraphBuilderConfig
parameter_list|()
block|{
name|ImmutableValueGraph
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|emptyGraph
init|=
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
name|nodeOrder
argument_list|(
name|ElementOrder
operator|.
expr|<
name|String
operator|>
name|natural
argument_list|()
argument_list|)
operator|.
operator|<
name|String
decl_stmt|,
name|Integer
decl|>
name|immutable
argument_list|()
decl|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|emptyGraph
operator|.
name|isDirected
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|emptyGraph
operator|.
name|allowsSelfLoops
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|emptyGraph
operator|.
name|nodeOrder
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ElementOrder
operator|.
expr|<
name|String
operator|>
name|natural
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Tests that the ImmutableValueGraph.Builder doesn't change when the creating ValueGraphBuilder    * changes.    */
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"CheckReturnValue"
argument_list|)
DECL|method|immutableValueGraphBuilder_copiesGraphBuilder ()
specifier|public
name|void
name|immutableValueGraphBuilder_copiesGraphBuilder
parameter_list|()
block|{
name|ValueGraphBuilder
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|graphBuilder
init|=
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
operator|<
name|String
operator|>
name|nodeOrder
argument_list|(
name|ElementOrder
operator|.
expr|<
name|String
operator|>
name|natural
argument_list|()
argument_list|)
decl_stmt|;
name|ImmutableValueGraph
operator|.
name|Builder
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|immutableValueGraphBuilder
init|=
name|graphBuilder
operator|.
expr|<
name|String
decl_stmt|,
name|Integer
decl|>
name|immutable
argument_list|()
decl_stmt|;
comment|// Update ValueGraphBuilder, but this shouldn't impact immutableValueGraphBuilder
name|graphBuilder
operator|.
name|allowsSelfLoops
argument_list|(
literal|false
argument_list|)
operator|.
name|nodeOrder
argument_list|(
name|ElementOrder
operator|.
expr|<
name|String
operator|>
name|unordered
argument_list|()
argument_list|)
expr_stmt|;
name|ImmutableValueGraph
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|emptyGraph
init|=
name|immutableValueGraphBuilder
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|emptyGraph
operator|.
name|isDirected
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|emptyGraph
operator|.
name|allowsSelfLoops
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|emptyGraph
operator|.
name|nodeOrder
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ElementOrder
operator|.
expr|<
name|String
operator|>
name|natural
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|immutableValueGraphBuilder_addNode ()
specifier|public
name|void
name|immutableValueGraphBuilder_addNode
parameter_list|()
block|{
name|ImmutableValueGraph
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|graph
init|=
name|ValueGraphBuilder
operator|.
name|directed
argument_list|()
operator|.
operator|<
name|String
decl_stmt|,
name|Integer
decl|>
name|immutable
argument_list|()
decl|.
name|addNode
argument_list|(
literal|"A"
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
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
literal|"A"
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
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|immutableValueGraphBuilder_putEdgeFromNodes ()
specifier|public
name|void
name|immutableValueGraphBuilder_putEdgeFromNodes
parameter_list|()
block|{
name|ImmutableValueGraph
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|graph
init|=
name|ValueGraphBuilder
operator|.
name|directed
argument_list|()
operator|.
operator|<
name|String
decl_stmt|,
name|Integer
decl|>
name|immutable
argument_list|()
decl|.
name|putEdgeValue
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|10
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
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
literal|"A"
argument_list|,
literal|"B"
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
name|containsExactly
argument_list|(
name|EndpointPair
operator|.
name|ordered
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgeValueOrDefault
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|null
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|10
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|immutableValueGraphBuilder_putEdgeFromEndpointPair ()
specifier|public
name|void
name|immutableValueGraphBuilder_putEdgeFromEndpointPair
parameter_list|()
block|{
name|ImmutableValueGraph
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|graph
init|=
name|ValueGraphBuilder
operator|.
name|directed
argument_list|()
operator|.
operator|<
name|String
decl_stmt|,
name|Integer
decl|>
name|immutable
argument_list|()
decl|.
name|putEdgeValue
argument_list|(
name|EndpointPair
operator|.
name|ordered
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|)
argument_list|,
literal|10
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
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
literal|"A"
argument_list|,
literal|"B"
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
name|containsExactly
argument_list|(
name|EndpointPair
operator|.
name|ordered
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgeValueOrDefault
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|null
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|10
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|immutableValueGraphBuilder_incidentEdges_preservesIncidentEdgesOrder ()
specifier|public
name|void
name|immutableValueGraphBuilder_incidentEdges_preservesIncidentEdgesOrder
parameter_list|()
block|{
name|ImmutableValueGraph
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|graph
init|=
name|ValueGraphBuilder
operator|.
name|directed
argument_list|()
operator|.
operator|<
name|Integer
decl_stmt|,
name|String
decl|>
name|immutable
argument_list|()
decl|.
name|putEdgeValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"2-1"
argument_list|)
decl|.
name|putEdgeValue
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|,
literal|"2-3"
argument_list|)
decl|.
name|putEdgeValue
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|"1-2"
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|incidentEdges
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|EndpointPair
operator|.
name|ordered
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
argument_list|,
name|EndpointPair
operator|.
name|ordered
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|,
name|EndpointPair
operator|.
name|ordered
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|immutableValueGraphBuilder_incidentEdgeOrder_stable ()
specifier|public
name|void
name|immutableValueGraphBuilder_incidentEdgeOrder_stable
parameter_list|()
block|{
name|ImmutableValueGraph
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|graph
init|=
name|ValueGraphBuilder
operator|.
name|directed
argument_list|()
operator|.
operator|<
name|Integer
decl_stmt|,
name|String
decl|>
name|immutable
argument_list|()
decl|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|incidentEdgeOrder
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ElementOrder
operator|.
name|stable
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|immutableValueGraphBuilder_fromUnorderedBuilder_incidentEdgeOrder_stable ()
specifier|public
name|void
name|immutableValueGraphBuilder_fromUnorderedBuilder_incidentEdgeOrder_stable
parameter_list|()
block|{
name|ImmutableValueGraph
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|graph
init|=
name|ValueGraphBuilder
operator|.
name|directed
argument_list|()
operator|.
name|incidentEdgeOrder
argument_list|(
name|ElementOrder
operator|.
name|unordered
argument_list|()
argument_list|)
operator|.
operator|<
name|Integer
decl_stmt|,
name|String
decl|>
name|immutable
argument_list|()
decl|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|incidentEdgeOrder
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ElementOrder
operator|.
name|stable
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

