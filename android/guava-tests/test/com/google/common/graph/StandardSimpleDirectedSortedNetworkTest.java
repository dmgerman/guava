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
name|ElementOrder
operator|.
name|sorted
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
name|Ordering
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
comment|/**  * Tests for a directed {@link StandardMutableNetwork}, creating a simple directed sorted graph  * (parallel and self-loop edges are not allowed).  *  *<p>The main purpose of this class is to run the inherited {@link #concurrentIteration} test  * against a sorted graph so as to cover {@link MapRetrievalCache}.  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4
operator|.
name|class
argument_list|)
DECL|class|StandardSimpleDirectedSortedNetworkTest
specifier|public
class|class
name|StandardSimpleDirectedSortedNetworkTest
extends|extends
name|AbstractStandardDirectedNetworkTest
block|{
annotation|@
name|Override
DECL|method|createGraph ()
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|createGraph
parameter_list|()
block|{
return|return
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|allowsParallelEdges
argument_list|(
literal|false
argument_list|)
operator|.
name|allowsSelfLoops
argument_list|(
literal|false
argument_list|)
operator|.
name|edgeOrder
argument_list|(
name|sorted
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
argument_list|)
operator|.
name|nodeOrder
argument_list|(
name|sorted
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|addNode (Integer n)
name|void
name|addNode
parameter_list|(
name|Integer
name|n
parameter_list|)
block|{
name|networkAsMutableNetwork
operator|.
name|addNode
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addEdge (Integer n1, Integer n2, String e)
name|void
name|addEdge
parameter_list|(
name|Integer
name|n1
parameter_list|,
name|Integer
name|n2
parameter_list|,
name|String
name|e
parameter_list|)
block|{
name|networkAsMutableNetwork
operator|.
name|addEdge
argument_list|(
name|n1
argument_list|,
name|n2
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addEdge_nodesNotInGraph ()
specifier|public
name|void
name|addEdge_nodesNotInGraph
parameter_list|()
block|{
comment|/*      * Skip this test because the expected ordering is different here than in the superclass because      * of sorting.      *      * TODO(cpovirk): Implement this to check for the proper order.      */
block|}
block|}
end_class

end_unit
