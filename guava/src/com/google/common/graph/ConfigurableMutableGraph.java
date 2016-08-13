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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Configurable implementation of {@link MutableGraph} that supports both directed and undirected  * graphs. Instances of this class should be constructed with {@link GraphBuilder}.  *  *<p>Time complexities for mutation methods are all O(1) except for {@code removeNode(N node)},  * which is in O(d_node) where d_node is the degree of {@code node}.  *  * @author James Sexton  * @param<N> Node parameter type  */
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
name|AbstractGraph
argument_list|<
name|N
argument_list|>
implements|implements
name|MutableGraph
argument_list|<
name|N
argument_list|>
block|{
DECL|field|DUMMY_EDGE_VALUE
specifier|private
specifier|static
specifier|final
name|Object
name|DUMMY_EDGE_VALUE
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
DECL|field|backingGraph
specifier|private
specifier|final
name|MutableValueGraph
argument_list|<
name|N
argument_list|,
name|Object
argument_list|>
name|backingGraph
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
name|backingGraph
operator|=
operator|new
name|ConfigurableMutableValueGraph
argument_list|<
name|N
argument_list|,
name|Object
argument_list|>
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|nodes ()
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|nodes
parameter_list|()
block|{
return|return
name|backingGraph
operator|.
name|nodes
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|edges ()
specifier|public
name|Set
argument_list|<
name|Endpoints
argument_list|<
name|N
argument_list|>
argument_list|>
name|edges
parameter_list|()
block|{
return|return
name|backingGraph
operator|.
name|edges
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|nodeOrder ()
specifier|public
name|ElementOrder
argument_list|<
name|N
argument_list|>
name|nodeOrder
parameter_list|()
block|{
return|return
name|backingGraph
operator|.
name|nodeOrder
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isDirected ()
specifier|public
name|boolean
name|isDirected
parameter_list|()
block|{
return|return
name|backingGraph
operator|.
name|isDirected
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|allowsSelfLoops ()
specifier|public
name|boolean
name|allowsSelfLoops
parameter_list|()
block|{
return|return
name|backingGraph
operator|.
name|allowsSelfLoops
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|adjacentNodes (Object node)
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|adjacentNodes
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
return|return
name|backingGraph
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|predecessors (Object node)
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|predecessors
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
return|return
name|backingGraph
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|successors (Object node)
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|successors
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
return|return
name|backingGraph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
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
name|backingGraph
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|putEdge (N nodeA, N nodeB)
specifier|public
name|boolean
name|putEdge
parameter_list|(
name|N
name|nodeA
parameter_list|,
name|N
name|nodeB
parameter_list|)
block|{
return|return
name|backingGraph
operator|.
name|putEdgeValue
argument_list|(
name|nodeA
argument_list|,
name|nodeB
argument_list|,
name|DUMMY_EDGE_VALUE
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
name|backingGraph
operator|.
name|removeNode
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|removeEdge (Object nodeA, Object nodeB)
specifier|public
name|boolean
name|removeEdge
parameter_list|(
name|Object
name|nodeA
parameter_list|,
name|Object
name|nodeB
parameter_list|)
block|{
return|return
name|backingGraph
operator|.
name|removeEdge
argument_list|(
name|nodeA
argument_list|,
name|nodeB
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

