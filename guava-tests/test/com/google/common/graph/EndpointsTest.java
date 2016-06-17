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
comment|/**  * Tests for {@link Endpoints}.  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4
operator|.
name|class
argument_list|)
DECL|class|EndpointsTest
specifier|public
specifier|final
class|class
name|EndpointsTest
block|{
annotation|@
name|Test
DECL|method|testDirectedEndpoints ()
specifier|public
name|void
name|testDirectedEndpoints
parameter_list|()
block|{
name|Endpoints
argument_list|<
name|String
argument_list|>
name|directed
init|=
name|Endpoints
operator|.
name|ofDirected
argument_list|(
literal|"source"
argument_list|,
literal|"target"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|directed
operator|.
name|source
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"source"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|directed
operator|.
name|target
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"target"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|directed
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"source"
argument_list|,
literal|"target"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|directed
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"<source -> target>"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUndirectedEndpoints ()
specifier|public
name|void
name|testUndirectedEndpoints
parameter_list|()
block|{
name|Endpoints
argument_list|<
name|String
argument_list|>
name|undirected
init|=
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
literal|"chicken"
argument_list|,
literal|"egg"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|undirected
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"chicken"
argument_list|,
literal|"egg"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|undirected
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"chicken"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|undirected
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"egg"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSelfLoop ()
specifier|public
name|void
name|testSelfLoop
parameter_list|()
block|{
name|Endpoints
argument_list|<
name|String
argument_list|>
name|undirected
init|=
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
literal|"node"
argument_list|,
literal|"node"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|undirected
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|undirected
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"[node, node]"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
name|Endpoints
argument_list|<
name|String
argument_list|>
name|directed
init|=
name|Endpoints
operator|.
name|ofDirected
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|Endpoints
argument_list|<
name|String
argument_list|>
name|directedMirror
init|=
name|Endpoints
operator|.
name|ofDirected
argument_list|(
literal|"b"
argument_list|,
literal|"a"
argument_list|)
decl_stmt|;
name|Endpoints
argument_list|<
name|String
argument_list|>
name|undirected
init|=
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|Endpoints
argument_list|<
name|String
argument_list|>
name|undirectedMirror
init|=
name|Endpoints
operator|.
name|ofUndirected
argument_list|(
literal|"b"
argument_list|,
literal|"a"
argument_list|)
decl_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|directed
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|directedMirror
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|undirected
argument_list|,
name|undirectedMirror
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

