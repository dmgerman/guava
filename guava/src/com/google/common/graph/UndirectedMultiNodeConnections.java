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
import|import static
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
name|INNER_CAPACITY
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
name|graph
operator|.
name|GraphConstants
operator|.
name|INNER_LOAD_FACTOR
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
name|HashMultiset
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
name|ImmutableMap
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
name|Multiset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|Reference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|SoftReference
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
name|HashMap
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

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * An implementation of {@link NodeConnections} for undirected networks with parallel edges.  *  * @author James Sexton  * @param<N> Node parameter type  * @param<E> Edge parameter type  */
end_comment

begin_class
DECL|class|UndirectedMultiNodeConnections
specifier|final
class|class
name|UndirectedMultiNodeConnections
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
extends|extends
name|AbstractUndirectedNodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
block|{
DECL|method|UndirectedMultiNodeConnections (Map<E, N> incidentEdges)
specifier|private
name|UndirectedMultiNodeConnections
parameter_list|(
name|Map
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|incidentEdges
parameter_list|)
block|{
name|super
argument_list|(
name|incidentEdges
argument_list|)
expr_stmt|;
block|}
DECL|method|of ()
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|UndirectedMultiNodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|of
parameter_list|()
block|{
return|return
operator|new
name|UndirectedMultiNodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|(
operator|new
name|HashMap
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
argument_list|(
name|INNER_CAPACITY
argument_list|,
name|INNER_LOAD_FACTOR
argument_list|)
argument_list|)
return|;
block|}
DECL|method|ofImmutable (Map<E, N> incidentEdges)
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|UndirectedMultiNodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|ofImmutable
parameter_list|(
name|Map
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|incidentEdges
parameter_list|)
block|{
return|return
operator|new
name|UndirectedMultiNodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|(
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|incidentEdges
argument_list|)
argument_list|)
return|;
block|}
DECL|field|adjacentNodesReference
specifier|private
specifier|transient
name|Reference
argument_list|<
name|Multiset
argument_list|<
name|N
argument_list|>
argument_list|>
name|adjacentNodesReference
decl_stmt|;
annotation|@
name|Override
DECL|method|adjacentNodes ()
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|adjacentNodes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|adjacentNodesMultiset
argument_list|()
operator|.
name|elementSet
argument_list|()
argument_list|)
return|;
block|}
DECL|method|adjacentNodesMultiset ()
specifier|private
name|Multiset
argument_list|<
name|N
argument_list|>
name|adjacentNodesMultiset
parameter_list|()
block|{
name|Multiset
argument_list|<
name|N
argument_list|>
name|adjacentNodes
init|=
name|getReference
argument_list|(
name|adjacentNodesReference
argument_list|)
decl_stmt|;
if|if
condition|(
name|adjacentNodes
operator|==
literal|null
condition|)
block|{
name|adjacentNodes
operator|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|incidentEdgeMap
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|adjacentNodesReference
operator|=
operator|new
name|SoftReference
argument_list|<
name|Multiset
argument_list|<
name|N
argument_list|>
argument_list|>
argument_list|(
name|adjacentNodes
argument_list|)
expr_stmt|;
block|}
return|return
name|adjacentNodes
return|;
block|}
annotation|@
name|Override
DECL|method|edgesConnecting (final Object node)
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|edgesConnecting
parameter_list|(
specifier|final
name|Object
name|node
parameter_list|)
block|{
return|return
operator|new
name|MultiEdgesConnecting
argument_list|<
name|E
argument_list|>
argument_list|(
name|incidentEdgeMap
argument_list|,
name|node
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|adjacentNodesMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|node
argument_list|)
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|removeInEdge (Object edge, boolean isSelfLoop)
specifier|public
name|N
name|removeInEdge
parameter_list|(
name|Object
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
DECL|method|removeOutEdge (Object edge)
specifier|public
name|N
name|removeOutEdge
parameter_list|(
name|Object
name|edge
parameter_list|)
block|{
name|N
name|node
init|=
name|checkNotNull
argument_list|(
name|super
operator|.
name|removeOutEdge
argument_list|(
name|edge
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|N
argument_list|>
name|adjacentNodes
init|=
name|getReference
argument_list|(
name|adjacentNodesReference
argument_list|)
decl_stmt|;
if|if
condition|(
name|adjacentNodes
operator|!=
literal|null
condition|)
block|{
name|checkState
argument_list|(
name|adjacentNodes
operator|.
name|remove
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|node
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
name|super
operator|.
name|addOutEdge
argument_list|(
name|edge
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|Multiset
argument_list|<
name|N
argument_list|>
name|adjacentNodes
init|=
name|getReference
argument_list|(
name|adjacentNodesReference
argument_list|)
decl_stmt|;
if|if
condition|(
name|adjacentNodes
operator|!=
literal|null
condition|)
block|{
name|checkState
argument_list|(
name|adjacentNodes
operator|.
name|add
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getReference (@ullable Reference<T> reference)
annotation|@
name|Nullable
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getReference
parameter_list|(
annotation|@
name|Nullable
name|Reference
argument_list|<
name|T
argument_list|>
name|reference
parameter_list|)
block|{
return|return
operator|(
name|reference
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|reference
operator|.
name|get
argument_list|()
return|;
block|}
block|}
end_class

end_unit

