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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|graph
operator|.
name|GraphConstants
operator|.
name|Presence
import|;
end_import

begin_comment
comment|/**  * Configurable implementation of {@link MutableGraph} that supports both directed and  * undirected graphs. Instances of this class should be constructed with {@link GraphBuilder}.  *  *<p>Time complexities for mutation methods are all O(1) except for {@code removeNode(N node)},  * which is in O(d_node) where d_node is the degree of {@code node}.  *  * @author James Sexton  * @param<N> Node parameter type  */
end_comment

begin_class
DECL|class|ConfigurableMutableGraph
specifier|final
class|class
name|ConfigurableMutableGraph
parameter_list|<
name|N
parameter_list|>
extends|extends
name|ForwardingGraph
argument_list|<
name|N
argument_list|>
implements|implements
name|MutableGraph
argument_list|<
name|N
argument_list|>
block|{
DECL|field|backingValueGraph
specifier|private
specifier|final
name|MutableValueGraph
argument_list|<
name|N
argument_list|,
name|Presence
argument_list|>
name|backingValueGraph
decl_stmt|;
comment|/**    * Constructs a {@link MutableGraph} with the properties specified in {@code builder}.    */
DECL|method|ConfigurableMutableGraph (AbstractGraphBuilder<? super N> builder)
name|ConfigurableMutableGraph
parameter_list|(
name|AbstractGraphBuilder
argument_list|<
name|?
super|super
name|N
argument_list|>
name|builder
parameter_list|)
block|{
name|this
operator|.
name|backingValueGraph
operator|=
operator|new
name|ConfigurableMutableValueGraph
argument_list|<
name|N
argument_list|,
name|Presence
argument_list|>
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Graph
argument_list|<
name|N
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|backingValueGraph
return|;
block|}
annotation|@
name|Override
DECL|method|addNode (N node)
specifier|public
name|boolean
name|addNode
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|backingValueGraph
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|putEdge (N nodeU, N nodeV)
specifier|public
name|boolean
name|putEdge
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
block|{
return|return
name|backingValueGraph
operator|.
name|putEdgeValue
argument_list|(
name|nodeU
argument_list|,
name|nodeV
argument_list|,
name|Presence
operator|.
name|EDGE_EXISTS
argument_list|)
operator|==
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|removeNode (Object node)
specifier|public
name|boolean
name|removeNode
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
return|return
name|backingValueGraph
operator|.
name|removeNode
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|removeEdge (Object nodeU, Object nodeV)
specifier|public
name|boolean
name|removeEdge
parameter_list|(
name|Object
name|nodeU
parameter_list|,
name|Object
name|nodeV
parameter_list|)
block|{
return|return
name|backingValueGraph
operator|.
name|removeEdge
argument_list|(
name|nodeU
argument_list|,
name|nodeV
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

