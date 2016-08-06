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
comment|/**  * Tests for {@link ImmutableNetwork}.  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4
operator|.
name|class
argument_list|)
DECL|class|ImmutableNetworkTest
specifier|public
class|class
name|ImmutableNetworkTest
block|{
annotation|@
name|Test
DECL|method|copyOfImmutableNetwork_optimized ()
specifier|public
name|void
name|copyOfImmutableNetwork_optimized
parameter_list|()
block|{
name|Network
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|graph1
init|=
name|ImmutableNetwork
operator|.
name|copyOf
argument_list|(
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
operator|<
name|String
argument_list|,
name|String
operator|>
name|build
argument_list|()
argument_list|)
decl_stmt|;
name|Network
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|graph2
init|=
name|ImmutableNetwork
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
DECL|method|edgesConnecting_directed ()
specifier|public
name|void
name|edgesConnecting_directed
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mutableGraph
init|=
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|mutableGraph
operator|.
name|addEdgeV2
argument_list|(
literal|"A"
argument_list|,
literal|"A"
argument_list|,
literal|"AA"
argument_list|)
expr_stmt|;
name|mutableGraph
operator|.
name|addEdgeV2
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|"AB"
argument_list|)
expr_stmt|;
name|Network
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|graph
init|=
name|ImmutableNetwork
operator|.
name|copyOf
argument_list|(
name|mutableGraph
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgesConnecting
argument_list|(
literal|"A"
argument_list|,
literal|"A"
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"AA"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgesConnecting
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"AB"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgesConnecting
argument_list|(
literal|"B"
argument_list|,
literal|"A"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|edgesConnecting_undirected ()
specifier|public
name|void
name|edgesConnecting_undirected
parameter_list|()
block|{
name|MutableNetwork
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mutableGraph
init|=
name|NetworkBuilder
operator|.
name|undirected
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|mutableGraph
operator|.
name|addEdgeV2
argument_list|(
literal|"A"
argument_list|,
literal|"A"
argument_list|,
literal|"AA"
argument_list|)
expr_stmt|;
name|mutableGraph
operator|.
name|addEdgeV2
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|"AB"
argument_list|)
expr_stmt|;
name|Network
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|graph
init|=
name|ImmutableNetwork
operator|.
name|copyOf
argument_list|(
name|mutableGraph
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgesConnecting
argument_list|(
literal|"A"
argument_list|,
literal|"A"
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"AA"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgesConnecting
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"AB"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|graph
operator|.
name|edgesConnecting
argument_list|(
literal|"B"
argument_list|,
literal|"A"
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"AB"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

