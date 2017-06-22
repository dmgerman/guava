begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ERROR_ELEMENT_NOT_IN_GRAPH
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
name|assertWithMessage
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
name|AbstractPackageSanityTests
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|AssertionFailedError
import|;
end_import

begin_comment
comment|/**  * Covers basic sanity checks for the entire package.  *  * @author Kurt Alfred Kluever  */
end_comment

begin_class
DECL|class|PackageSanityTests
specifier|public
class|class
name|PackageSanityTests
extends|extends
name|AbstractPackageSanityTests
block|{
DECL|field|GRAPH_BUILDER_A
specifier|private
specifier|static
specifier|final
name|AbstractGraphBuilder
argument_list|<
name|?
argument_list|>
name|GRAPH_BUILDER_A
init|=
name|GraphBuilder
operator|.
name|directed
argument_list|()
operator|.
name|expectedNodeCount
argument_list|(
literal|10
argument_list|)
decl_stmt|;
DECL|field|GRAPH_BUILDER_B
specifier|private
specifier|static
specifier|final
name|AbstractGraphBuilder
argument_list|<
name|?
argument_list|>
name|GRAPH_BUILDER_B
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
name|expectedNodeCount
argument_list|(
literal|16
argument_list|)
decl_stmt|;
DECL|field|IMMUTABLE_GRAPH_A
specifier|private
specifier|static
specifier|final
name|ImmutableGraph
argument_list|<
name|String
argument_list|>
name|IMMUTABLE_GRAPH_A
init|=
name|graphWithNode
argument_list|(
literal|"A"
argument_list|)
decl_stmt|;
DECL|field|IMMUTABLE_GRAPH_B
specifier|private
specifier|static
specifier|final
name|ImmutableGraph
argument_list|<
name|String
argument_list|>
name|IMMUTABLE_GRAPH_B
init|=
name|graphWithNode
argument_list|(
literal|"B"
argument_list|)
decl_stmt|;
DECL|field|NETWORK_BUILDER_A
specifier|private
specifier|static
specifier|final
name|NetworkBuilder
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|NETWORK_BUILDER_A
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
name|expectedNodeCount
argument_list|(
literal|10
argument_list|)
decl_stmt|;
DECL|field|NETWORK_BUILDER_B
specifier|private
specifier|static
specifier|final
name|NetworkBuilder
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|NETWORK_BUILDER_B
init|=
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|allowsSelfLoops
argument_list|(
literal|true
argument_list|)
operator|.
name|expectedNodeCount
argument_list|(
literal|16
argument_list|)
decl_stmt|;
DECL|field|IMMUTABLE_NETWORK_A
specifier|private
specifier|static
specifier|final
name|ImmutableNetwork
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|IMMUTABLE_NETWORK_A
init|=
name|networkWithNode
argument_list|(
literal|"A"
argument_list|)
decl_stmt|;
DECL|field|IMMUTABLE_NETWORK_B
specifier|private
specifier|static
specifier|final
name|ImmutableNetwork
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|IMMUTABLE_NETWORK_B
init|=
name|networkWithNode
argument_list|(
literal|"B"
argument_list|)
decl_stmt|;
DECL|method|PackageSanityTests ()
specifier|public
name|PackageSanityTests
parameter_list|()
block|{
name|setDistinctValues
argument_list|(
name|AbstractGraphBuilder
operator|.
name|class
argument_list|,
name|GRAPH_BUILDER_A
argument_list|,
name|GRAPH_BUILDER_B
argument_list|)
expr_stmt|;
name|setDistinctValues
argument_list|(
name|Graph
operator|.
name|class
argument_list|,
name|IMMUTABLE_GRAPH_A
argument_list|,
name|IMMUTABLE_GRAPH_B
argument_list|)
expr_stmt|;
name|setDistinctValues
argument_list|(
name|NetworkBuilder
operator|.
name|class
argument_list|,
name|NETWORK_BUILDER_A
argument_list|,
name|NETWORK_BUILDER_B
argument_list|)
expr_stmt|;
name|setDistinctValues
argument_list|(
name|Network
operator|.
name|class
argument_list|,
name|IMMUTABLE_NETWORK_A
argument_list|,
name|IMMUTABLE_NETWORK_B
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|testNulls ()
specifier|public
name|void
name|testNulls
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|super
operator|.
name|testNulls
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|e
parameter_list|)
block|{
name|assertWithMessage
argument_list|(
literal|"Method did not throw null pointer OR element not in graph exception."
argument_list|)
operator|.
name|that
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|ERROR_ELEMENT_NOT_IN_GRAPH
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|graphWithNode (N node)
specifier|private
specifier|static
parameter_list|<
name|N
parameter_list|>
name|ImmutableGraph
argument_list|<
name|N
argument_list|>
name|graphWithNode
parameter_list|(
name|N
name|node
parameter_list|)
block|{
name|MutableGraph
argument_list|<
name|N
argument_list|>
name|graph
init|=
name|GraphBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|graph
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
name|ImmutableGraph
operator|.
name|copyOf
argument_list|(
name|graph
argument_list|)
return|;
block|}
DECL|method|networkWithNode (N node)
specifier|private
specifier|static
parameter_list|<
name|N
parameter_list|>
name|ImmutableNetwork
argument_list|<
name|N
argument_list|,
name|N
argument_list|>
name|networkWithNode
parameter_list|(
name|N
name|node
parameter_list|)
block|{
name|MutableNetwork
argument_list|<
name|N
argument_list|,
name|N
argument_list|>
name|network
init|=
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|network
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
name|ImmutableNetwork
operator|.
name|copyOf
argument_list|(
name|network
argument_list|)
return|;
block|}
block|}
end_class

end_unit

