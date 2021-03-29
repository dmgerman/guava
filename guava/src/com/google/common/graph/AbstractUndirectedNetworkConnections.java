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
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|base
operator|.
name|Preconditions
operator|.
name|checkState
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

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
comment|/**  * A base implementation of {@link NetworkConnections} for undirected networks.  *  * @author James Sexton  * @param<N> Node parameter type  * @param<E> Edge parameter type  */
end_comment

begin_class
DECL|class|AbstractUndirectedNetworkConnections
specifier|abstract
class|class
name|AbstractUndirectedNetworkConnections
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
implements|implements
name|NetworkConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
block|{
comment|/** Keys are edges incident to the origin node, values are the node at the other end. */
DECL|field|incidentEdgeMap
specifier|final
name|Map
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|incidentEdgeMap
decl_stmt|;
DECL|method|AbstractUndirectedNetworkConnections (Map<E, N> incidentEdgeMap)
name|AbstractUndirectedNetworkConnections
parameter_list|(
name|Map
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|incidentEdgeMap
parameter_list|)
block|{
name|this
operator|.
name|incidentEdgeMap
operator|=
name|checkNotNull
argument_list|(
name|incidentEdgeMap
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|predecessors ()
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|predecessors
parameter_list|()
block|{
return|return
name|adjacentNodes
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|successors ()
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|successors
parameter_list|()
block|{
return|return
name|adjacentNodes
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|incidentEdges ()
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|incidentEdges
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|incidentEdgeMap
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|inEdges ()
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|inEdges
parameter_list|()
block|{
return|return
name|incidentEdges
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|outEdges ()
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|outEdges
parameter_list|()
block|{
return|return
name|incidentEdges
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|adjacentNode (E edge)
specifier|public
name|N
name|adjacentNode
parameter_list|(
name|E
name|edge
parameter_list|)
block|{
return|return
name|checkNotNull
argument_list|(
name|incidentEdgeMap
operator|.
name|get
argument_list|(
name|edge
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|removeInEdge (E edge, boolean isSelfLoop)
specifier|public
name|N
name|removeInEdge
parameter_list|(
name|E
name|edge
parameter_list|,
name|boolean
name|isSelfLoop
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSelfLoop
condition|)
block|{
return|return
name|removeOutEdge
argument_list|(
name|edge
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|removeOutEdge (E edge)
specifier|public
name|N
name|removeOutEdge
parameter_list|(
name|E
name|edge
parameter_list|)
block|{
name|N
name|previousNode
init|=
name|incidentEdgeMap
operator|.
name|remove
argument_list|(
name|edge
argument_list|)
decl_stmt|;
return|return
name|checkNotNull
argument_list|(
name|previousNode
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addInEdge (E edge, N node, boolean isSelfLoop)
specifier|public
name|void
name|addInEdge
parameter_list|(
name|E
name|edge
parameter_list|,
name|N
name|node
parameter_list|,
name|boolean
name|isSelfLoop
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSelfLoop
condition|)
block|{
name|addOutEdge
argument_list|(
name|edge
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|addOutEdge (E edge, N node)
specifier|public
name|void
name|addOutEdge
parameter_list|(
name|E
name|edge
parameter_list|,
name|N
name|node
parameter_list|)
block|{
name|N
name|previousNode
init|=
name|incidentEdgeMap
operator|.
name|put
argument_list|(
name|edge
argument_list|,
name|node
argument_list|)
decl_stmt|;
name|checkState
argument_list|(
name|previousNode
operator|==
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

