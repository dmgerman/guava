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
comment|/**  * Tests for {@link ImmutableGraph} and {@link ImmutableBasicGraph}.  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4
operator|.
name|class
argument_list|)
DECL|class|ImmutableGraphTest
specifier|public
class|class
name|ImmutableGraphTest
block|{
annotation|@
name|Test
DECL|method|immutableGraph ()
specifier|public
name|void
name|immutableGraph
parameter_list|()
block|{
name|MutableGraph
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|mutableGraph
init|=
name|GraphBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|mutableGraph
operator|.
name|addNode
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|Graph
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|immutableGraph
init|=
name|ImmutableGraph
operator|.
name|copyOf
argument_list|(
name|mutableGraph
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|immutableGraph
argument_list|)
operator|.
name|isNotInstanceOf
argument_list|(
name|MutableGraph
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|immutableGraph
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|mutableGraph
argument_list|)
expr_stmt|;
name|mutableGraph
operator|.
name|addNode
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|immutableGraph
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|mutableGraph
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|immutableBasicGraph ()
specifier|public
name|void
name|immutableBasicGraph
parameter_list|()
block|{
name|MutableBasicGraph
argument_list|<
name|String
argument_list|>
name|mutableGraph
init|=
name|BasicGraphBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|mutableGraph
operator|.
name|addNode
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|BasicGraph
argument_list|<
name|String
argument_list|>
name|immutableGraph
init|=
name|ImmutableBasicGraph
operator|.
name|copyOf
argument_list|(
name|mutableGraph
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|immutableGraph
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
name|immutableGraph
argument_list|)
operator|.
name|isNotInstanceOf
argument_list|(
name|MutableBasicGraph
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|immutableGraph
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|mutableGraph
argument_list|)
expr_stmt|;
name|mutableGraph
operator|.
name|addNode
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|immutableGraph
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|mutableGraph
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|copyOfImmutableGraph_optimized ()
specifier|public
name|void
name|copyOfImmutableGraph_optimized
parameter_list|()
block|{
name|Graph
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|graph1
init|=
name|ImmutableGraph
operator|.
name|copyOf
argument_list|(
name|GraphBuilder
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
name|Graph
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|graph2
init|=
name|ImmutableBasicGraph
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
name|isSameAs
argument_list|(
name|graph1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|copyOfImmutableBasicGraph_optimized ()
specifier|public
name|void
name|copyOfImmutableBasicGraph_optimized
parameter_list|()
block|{
name|BasicGraph
argument_list|<
name|String
argument_list|>
name|graph1
init|=
name|ImmutableBasicGraph
operator|.
name|copyOf
argument_list|(
name|BasicGraphBuilder
operator|.
name|directed
argument_list|()
operator|.
operator|<
name|String
operator|>
name|build
argument_list|()
argument_list|)
decl_stmt|;
name|BasicGraph
argument_list|<
name|String
argument_list|>
name|graph2
init|=
name|ImmutableBasicGraph
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
name|isSameAs
argument_list|(
name|graph1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

