begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2017 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|checkArgument
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
name|ENDPOINTS_MISMATCH
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
name|base
operator|.
name|Function
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
name|ImmutableSet
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|Ints
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
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_comment
comment|/**  * This class provides a skeletal implementation of {@link BaseGraph}.  *  *<p>The methods implemented in this class should not be overridden unless the subclass admits a  * more efficient implementation.  *  * @author James Sexton  * @param<N> Node parameter type  */
end_comment

begin_class
DECL|class|AbstractBaseGraph
specifier|abstract
class|class
name|AbstractBaseGraph
parameter_list|<
name|N
parameter_list|>
implements|implements
name|BaseGraph
argument_list|<
name|N
argument_list|>
block|{
comment|/**    * Returns the number of edges in this graph; used to calculate the size of {@link #edges()}. This    * implementation requires O(|N|) time. Classes extending this one may manually keep track of the    * number of edges as the graph is updated, and override this method for better performance.    */
DECL|method|edgeCount ()
specifier|protected
name|long
name|edgeCount
parameter_list|()
block|{
name|long
name|degreeSum
init|=
literal|0L
decl_stmt|;
for|for
control|(
name|N
name|node
range|:
name|nodes
argument_list|()
control|)
block|{
name|degreeSum
operator|+=
name|degree
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
comment|// According to the degree sum formula, this is equal to twice the number of edges.
name|checkState
argument_list|(
operator|(
name|degreeSum
operator|&
literal|1
operator|)
operator|==
literal|0
argument_list|)
expr_stmt|;
return|return
name|degreeSum
operator|>>>
literal|1
return|;
block|}
comment|/**    * An implementation of {@link BaseGraph#edges()} defined in terms of {@link #nodes()} and {@link    * #successors(Object)}.    */
annotation|@
name|Override
DECL|method|edges ()
specifier|public
name|Set
argument_list|<
name|EndpointPair
argument_list|<
name|N
argument_list|>
argument_list|>
name|edges
parameter_list|()
block|{
return|return
operator|new
name|AbstractSet
argument_list|<
name|EndpointPair
argument_list|<
name|N
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|UnmodifiableIterator
argument_list|<
name|EndpointPair
argument_list|<
name|N
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|EndpointPairIterator
operator|.
name|of
argument_list|(
name|AbstractBaseGraph
operator|.
name|this
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
name|Ints
operator|.
name|saturatedCast
argument_list|(
name|edgeCount
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|// Mostly safe: We check contains(u) before calling successors(u), so we perform unsafe
comment|// operations only in weird cases like checking for an EndpointPair<ArrayList> in a
comment|// Graph<LinkedList>.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|EndpointPair
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|EndpointPair
argument_list|<
name|?
argument_list|>
name|endpointPair
init|=
operator|(
name|EndpointPair
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|isOrderingCompatible
argument_list|(
name|endpointPair
argument_list|)
operator|&&
name|nodes
argument_list|()
operator|.
name|contains
argument_list|(
name|endpointPair
operator|.
name|nodeU
argument_list|()
argument_list|)
operator|&&
name|successors
argument_list|(
operator|(
name|N
operator|)
name|endpointPair
operator|.
name|nodeU
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|endpointPair
operator|.
name|nodeV
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|incidentEdges (N node)
specifier|public
name|Set
argument_list|<
name|EndpointPair
argument_list|<
name|N
argument_list|>
argument_list|>
name|incidentEdges
parameter_list|(
name|N
name|node
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|nodes
argument_list|()
operator|.
name|contains
argument_list|(
name|node
argument_list|)
argument_list|,
literal|"Node %s is not an element of this graph."
argument_list|,
name|node
argument_list|)
expr_stmt|;
return|return
operator|new
name|IncidentEdgeSet
argument_list|<
name|N
argument_list|>
argument_list|(
name|this
argument_list|,
name|node
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|UnmodifiableIterator
argument_list|<
name|EndpointPair
argument_list|<
name|N
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
if|if
condition|(
name|graph
operator|.
name|isDirected
argument_list|()
condition|)
block|{
return|return
name|Iterators
operator|.
name|unmodifiableIterator
argument_list|(
name|Iterators
operator|.
name|concat
argument_list|(
name|Iterators
operator|.
name|transform
argument_list|(
name|graph
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
operator|.
name|iterator
argument_list|()
argument_list|,
operator|new
name|Function
argument_list|<
name|N
argument_list|,
name|EndpointPair
argument_list|<
name|N
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|EndpointPair
argument_list|<
name|N
argument_list|>
name|apply
parameter_list|(
name|N
name|predecessor
parameter_list|)
block|{
return|return
name|EndpointPair
operator|.
name|ordered
argument_list|(
name|predecessor
argument_list|,
name|node
argument_list|)
return|;
block|}
block|}
argument_list|)
argument_list|,
name|Iterators
operator|.
name|transform
argument_list|(
comment|// filter out 'node' from successors (already covered by predecessors, above)
name|Sets
operator|.
name|difference
argument_list|(
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
name|node
argument_list|)
argument_list|)
operator|.
name|iterator
argument_list|()
argument_list|,
operator|new
name|Function
argument_list|<
name|N
argument_list|,
name|EndpointPair
argument_list|<
name|N
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|EndpointPair
argument_list|<
name|N
argument_list|>
name|apply
parameter_list|(
name|N
name|successor
parameter_list|)
block|{
return|return
name|EndpointPair
operator|.
name|ordered
argument_list|(
name|node
argument_list|,
name|successor
argument_list|)
return|;
block|}
block|}
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Iterators
operator|.
name|unmodifiableIterator
argument_list|(
name|Iterators
operator|.
name|transform
argument_list|(
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
operator|.
name|iterator
argument_list|()
argument_list|,
operator|new
name|Function
argument_list|<
name|N
argument_list|,
name|EndpointPair
argument_list|<
name|N
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|EndpointPair
argument_list|<
name|N
argument_list|>
name|apply
parameter_list|(
name|N
name|adjacentNode
parameter_list|)
block|{
return|return
name|EndpointPair
operator|.
name|unordered
argument_list|(
name|node
argument_list|,
name|adjacentNode
argument_list|)
return|;
block|}
block|}
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|degree (N node)
specifier|public
name|int
name|degree
parameter_list|(
name|N
name|node
parameter_list|)
block|{
if|if
condition|(
name|isDirected
argument_list|()
condition|)
block|{
return|return
name|IntMath
operator|.
name|saturatedAdd
argument_list|(
name|predecessors
argument_list|(
name|node
argument_list|)
operator|.
name|size
argument_list|()
argument_list|,
name|successors
argument_list|(
name|node
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
name|Set
argument_list|<
name|N
argument_list|>
name|neighbors
init|=
name|adjacentNodes
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|int
name|selfLoopCount
init|=
operator|(
name|allowsSelfLoops
argument_list|()
operator|&&
name|neighbors
operator|.
name|contains
argument_list|(
name|node
argument_list|)
operator|)
condition|?
literal|1
else|:
literal|0
decl_stmt|;
return|return
name|IntMath
operator|.
name|saturatedAdd
argument_list|(
name|neighbors
operator|.
name|size
argument_list|()
argument_list|,
name|selfLoopCount
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|inDegree (N node)
specifier|public
name|int
name|inDegree
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|isDirected
argument_list|()
condition|?
name|predecessors
argument_list|(
name|node
argument_list|)
operator|.
name|size
argument_list|()
else|:
name|degree
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|outDegree (N node)
specifier|public
name|int
name|outDegree
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|isDirected
argument_list|()
condition|?
name|successors
argument_list|(
name|node
argument_list|)
operator|.
name|size
argument_list|()
else|:
name|degree
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hasEdgeConnecting (N nodeU, N nodeV)
specifier|public
name|boolean
name|hasEdgeConnecting
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|nodeU
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|nodeV
argument_list|)
expr_stmt|;
return|return
name|nodes
argument_list|()
operator|.
name|contains
argument_list|(
name|nodeU
argument_list|)
operator|&&
name|successors
argument_list|(
name|nodeU
argument_list|)
operator|.
name|contains
argument_list|(
name|nodeV
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hasEdgeConnecting (EndpointPair<N> endpoints)
specifier|public
name|boolean
name|hasEdgeConnecting
parameter_list|(
name|EndpointPair
argument_list|<
name|N
argument_list|>
name|endpoints
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|endpoints
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isOrderingCompatible
argument_list|(
name|endpoints
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|N
name|nodeU
init|=
name|endpoints
operator|.
name|nodeU
argument_list|()
decl_stmt|;
name|N
name|nodeV
init|=
name|endpoints
operator|.
name|nodeV
argument_list|()
decl_stmt|;
return|return
name|nodes
argument_list|()
operator|.
name|contains
argument_list|(
name|nodeU
argument_list|)
operator|&&
name|successors
argument_list|(
name|nodeU
argument_list|)
operator|.
name|contains
argument_list|(
name|nodeV
argument_list|)
return|;
block|}
comment|/**    * Throws {@code IllegalArgumentException} if the ordering of {@code endpoints} is not compatible    * with the directionality of this graph.    */
DECL|method|validateEndpoints (EndpointPair<?> endpoints)
specifier|protected
specifier|final
name|void
name|validateEndpoints
parameter_list|(
name|EndpointPair
argument_list|<
name|?
argument_list|>
name|endpoints
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|endpoints
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|isOrderingCompatible
argument_list|(
name|endpoints
argument_list|)
argument_list|,
name|ENDPOINTS_MISMATCH
argument_list|)
expr_stmt|;
block|}
DECL|method|isOrderingCompatible (EndpointPair<?> endpoints)
specifier|protected
specifier|final
name|boolean
name|isOrderingCompatible
parameter_list|(
name|EndpointPair
argument_list|<
name|?
argument_list|>
name|endpoints
parameter_list|)
block|{
return|return
name|endpoints
operator|.
name|isOrdered
argument_list|()
operator|||
operator|!
name|this
operator|.
name|isDirected
argument_list|()
return|;
block|}
block|}
end_class

end_unit

