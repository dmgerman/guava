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
name|Graphs
operator|.
name|checkNonNegative
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
name|Graphs
operator|.
name|checkPositive
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
name|Iterables
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
name|Iterators
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
name|Sets
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
name|UnmodifiableIterator
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
name|math
operator|.
name|IntMath
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractSet
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
comment|/**  * A base implementation of {@link NetworkConnections} for directed networks.  *  * @author James Sexton  * @param<N> Node parameter type  * @param<E> Edge parameter type  */
end_comment

begin_class
DECL|class|AbstractDirectedNetworkConnections
specifier|abstract
class|class
name|AbstractDirectedNetworkConnections
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
comment|/**    * Keys are edges incoming to the origin node, values are the source node.    */
DECL|field|inEdgeMap
specifier|protected
specifier|final
name|Map
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|inEdgeMap
decl_stmt|;
comment|/**    * Keys are edges outgoing from the origin node, values are the target node.    */
DECL|field|outEdgeMap
specifier|protected
specifier|final
name|Map
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|outEdgeMap
decl_stmt|;
DECL|field|selfLoopCount
specifier|private
name|int
name|selfLoopCount
decl_stmt|;
DECL|method|AbstractDirectedNetworkConnections (Map<E, N> inEdgeMap, Map<E, N> outEdgeMap, int selfLoopCount)
specifier|protected
name|AbstractDirectedNetworkConnections
parameter_list|(
name|Map
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|inEdgeMap
parameter_list|,
name|Map
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|outEdgeMap
parameter_list|,
name|int
name|selfLoopCount
parameter_list|)
block|{
name|this
operator|.
name|inEdgeMap
operator|=
name|checkNotNull
argument_list|(
name|inEdgeMap
argument_list|,
literal|"inEdgeMap"
argument_list|)
expr_stmt|;
name|this
operator|.
name|outEdgeMap
operator|=
name|checkNotNull
argument_list|(
name|outEdgeMap
argument_list|,
literal|"outEdgeMap"
argument_list|)
expr_stmt|;
name|this
operator|.
name|selfLoopCount
operator|=
name|checkNonNegative
argument_list|(
name|selfLoopCount
argument_list|)
expr_stmt|;
name|checkState
argument_list|(
name|selfLoopCount
operator|<=
name|inEdgeMap
operator|.
name|size
argument_list|()
operator|&&
name|selfLoopCount
operator|<=
name|outEdgeMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|Sets
operator|.
name|union
argument_list|(
name|predecessors
argument_list|()
argument_list|,
name|successors
argument_list|()
argument_list|)
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
operator|new
name|AbstractSet
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
name|Iterable
argument_list|<
name|E
argument_list|>
name|incidentEdges
init|=
operator|(
name|selfLoopCount
operator|==
literal|0
operator|)
condition|?
name|Iterables
operator|.
name|concat
argument_list|(
name|inEdgeMap
operator|.
name|keySet
argument_list|()
argument_list|,
name|outEdgeMap
operator|.
name|keySet
argument_list|()
argument_list|)
else|:
name|Sets
operator|.
name|union
argument_list|(
name|inEdgeMap
operator|.
name|keySet
argument_list|()
argument_list|,
name|outEdgeMap
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|Iterators
operator|.
name|unmodifiableIterator
argument_list|(
name|incidentEdges
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|IntMath
operator|.
name|saturatedAdd
argument_list|(
name|inEdgeMap
operator|.
name|size
argument_list|()
operator|-
name|selfLoopCount
argument_list|,
name|outEdgeMap
operator|.
name|size
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|inEdgeMap
operator|.
name|containsKey
argument_list|(
name|obj
argument_list|)
operator|||
name|outEdgeMap
operator|.
name|containsKey
argument_list|(
name|obj
argument_list|)
return|;
block|}
block|}
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
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|inEdgeMap
operator|.
name|keySet
argument_list|()
argument_list|)
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
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|outEdgeMap
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|oppositeNode (Object edge)
specifier|public
name|N
name|oppositeNode
parameter_list|(
name|Object
name|edge
parameter_list|)
block|{
comment|// Since the reference node is defined to be 'source' for directed graphs,
comment|// we can assume this edge lives in the set of outgoing edges.
return|return
name|checkNotNull
argument_list|(
name|outEdgeMap
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
name|isSelfLoop
condition|)
block|{
name|checkNonNegative
argument_list|(
operator|--
name|selfLoopCount
argument_list|)
expr_stmt|;
block|}
name|N
name|previousNode
init|=
name|inEdgeMap
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
name|previousNode
init|=
name|outEdgeMap
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
name|isSelfLoop
condition|)
block|{
name|checkPositive
argument_list|(
operator|++
name|selfLoopCount
argument_list|)
expr_stmt|;
block|}
name|N
name|previousNode
init|=
name|inEdgeMap
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
name|outEdgeMap
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

