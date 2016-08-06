begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.graph.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|graph
operator|.
name|testing
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|graph
operator|.
name|ImmutableNetwork
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
name|graph
operator|.
name|MutableNetwork
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
name|graph
operator|.
name|NetworkBuilder
import|;
end_import

begin_comment
comment|/**  * This class is useful for fluently building an immutable graph in tests. Example usage:  *<pre><code>  * // Constructs the following graph: (A)    (B)--->(C)  * private static final ImmutableNetwork<String, String> GRAPH =  *     TestNetworkBuilder.<String, String>init(NetworkBuilder.directed())  *         .addNode("A")  *         .addNode("B")  *         .addNode("C")  *         .addEdge("B->C", "B", "C")  *         .toImmutableNetwork();  *</code></pre>  */
end_comment

begin_class
DECL|class|TestNetworkBuilder
specifier|public
specifier|final
class|class
name|TestNetworkBuilder
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
block|{
DECL|field|graph
specifier|private
specifier|final
name|MutableNetwork
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|graph
decl_stmt|;
DECL|method|TestNetworkBuilder (MutableNetwork<N, E> graph)
specifier|private
name|TestNetworkBuilder
parameter_list|(
name|MutableNetwork
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|graph
parameter_list|)
block|{
name|this
operator|.
name|graph
operator|=
name|graph
expr_stmt|;
block|}
DECL|method|init (NetworkBuilder<? super N, ? super E> builder)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|TestNetworkBuilder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|init
parameter_list|(
name|NetworkBuilder
argument_list|<
name|?
super|super
name|N
argument_list|,
name|?
super|super
name|E
argument_list|>
name|builder
parameter_list|)
block|{
return|return
operator|new
name|TestNetworkBuilder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|(
name|builder
operator|.
expr|<
name|N
argument_list|,
name|E
operator|>
name|build
argument_list|()
argument_list|)
return|;
block|}
DECL|method|addNode (N node)
specifier|public
name|TestNetworkBuilder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|addNode
parameter_list|(
name|N
name|node
parameter_list|)
block|{
name|graph
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|addEdge (E edge, N nodeA, N nodeB)
specifier|public
name|TestNetworkBuilder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|addEdge
parameter_list|(
name|E
name|edge
parameter_list|,
name|N
name|nodeA
parameter_list|,
name|N
name|nodeB
parameter_list|)
block|{
name|graph
operator|.
name|addEdgeV2
argument_list|(
name|nodeA
argument_list|,
name|nodeB
argument_list|,
name|edge
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|toImmutableNetwork ()
specifier|public
name|ImmutableNetwork
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|toImmutableNetwork
parameter_list|()
block|{
return|return
name|ImmutableNetwork
operator|.
name|copyOf
argument_list|(
name|graph
argument_list|)
return|;
block|}
block|}
end_class

end_unit

