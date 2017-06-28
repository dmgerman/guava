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
name|graph
operator|.
name|GraphConstants
operator|.
name|GRAPH_STRING_FORMAT
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
name|MULTIPLE_EDGES_CONNECTING
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Collections
operator|.
name|unmodifiableSet
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
name|annotations
operator|.
name|Beta
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
name|base
operator|.
name|Predicate
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
name|Maps
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
name|Iterator
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
name|Optional
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
comment|/**  * This class provides a skeletal implementation of {@link Network}. It is recommended to extend  * this class rather than implement {@link Network} directly.  *  *<p>The methods implemented in this class should not be overridden unless the subclass admits a  * more efficient implementation.  *  * @author James Sexton  * @param<N> Node parameter type  * @param<E> Edge parameter type  * @since 20.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|AbstractNetwork
specifier|public
specifier|abstract
class|class
name|AbstractNetwork
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
implements|implements
name|Network
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
block|{
annotation|@
name|Override
DECL|method|asGraph ()
specifier|public
name|Graph
argument_list|<
name|N
argument_list|>
name|asGraph
parameter_list|()
block|{
return|return
operator|new
name|AbstractGraph
argument_list|<
name|N
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|nodes
parameter_list|()
block|{
return|return
name|AbstractNetwork
operator|.
name|this
operator|.
name|nodes
argument_list|()
return|;
block|}
annotation|@
name|Override
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
if|if
condition|(
name|allowsParallelEdges
argument_list|()
condition|)
block|{
return|return
name|super
operator|.
name|edges
argument_list|()
return|;
comment|// Defer to AbstractGraph implementation.
block|}
comment|// Optimized implementation assumes no parallel edges (1:1 edge to EndpointPair mapping).
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
name|Iterator
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
name|Iterators
operator|.
name|transform
argument_list|(
name|AbstractNetwork
operator|.
name|this
operator|.
name|edges
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|,
operator|new
name|Function
argument_list|<
name|E
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
name|E
name|edge
parameter_list|)
block|{
return|return
name|incidentNodes
argument_list|(
name|edge
argument_list|)
return|;
block|}
block|}
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
name|AbstractNetwork
operator|.
name|this
operator|.
name|edges
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
comment|// Mostly safe: We check contains(u) before calling successors(u), so we perform unsafe
comment|// operations only in weird cases like checking for an EndpointPair<ArrayList> in a
comment|// Network<LinkedList>.
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
name|Nullable
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
name|isDirected
argument_list|()
operator|==
name|endpointPair
operator|.
name|isOrdered
argument_list|()
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
specifier|public
name|ElementOrder
argument_list|<
name|N
argument_list|>
name|nodeOrder
parameter_list|()
block|{
return|return
name|AbstractNetwork
operator|.
name|this
operator|.
name|nodeOrder
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isDirected
parameter_list|()
block|{
return|return
name|AbstractNetwork
operator|.
name|this
operator|.
name|isDirected
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|allowsSelfLoops
parameter_list|()
block|{
return|return
name|AbstractNetwork
operator|.
name|this
operator|.
name|allowsSelfLoops
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|adjacentNodes
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|AbstractNetwork
operator|.
name|this
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|predecessors
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|AbstractNetwork
operator|.
name|this
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|successors
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|AbstractNetwork
operator|.
name|this
operator|.
name|successors
argument_list|(
name|node
argument_list|)
return|;
block|}
comment|// DO NOT override the AbstractGraph *degree() implementations.
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
name|inEdges
argument_list|(
name|node
argument_list|)
operator|.
name|size
argument_list|()
argument_list|,
name|outEdges
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
return|return
name|IntMath
operator|.
name|saturatedAdd
argument_list|(
name|incidentEdges
argument_list|(
name|node
argument_list|)
operator|.
name|size
argument_list|()
argument_list|,
name|edgesConnecting
argument_list|(
name|node
argument_list|,
name|node
argument_list|)
operator|.
name|size
argument_list|()
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
name|inEdges
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
name|outEdges
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
DECL|method|adjacentEdges (E edge)
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|adjacentEdges
parameter_list|(
name|E
name|edge
parameter_list|)
block|{
name|EndpointPair
argument_list|<
name|N
argument_list|>
name|endpointPair
init|=
name|incidentNodes
argument_list|(
name|edge
argument_list|)
decl_stmt|;
comment|// Verifies that edge is in this network.
name|Set
argument_list|<
name|E
argument_list|>
name|endpointPairIncidentEdges
init|=
name|Sets
operator|.
name|union
argument_list|(
name|incidentEdges
argument_list|(
name|endpointPair
operator|.
name|nodeU
argument_list|()
argument_list|)
argument_list|,
name|incidentEdges
argument_list|(
name|endpointPair
operator|.
name|nodeV
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|Sets
operator|.
name|difference
argument_list|(
name|endpointPairIncidentEdges
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
name|edge
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|edgesConnecting (N nodeU, N nodeV)
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|edgesConnecting
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
block|{
name|Set
argument_list|<
name|E
argument_list|>
name|outEdgesU
init|=
name|outEdges
argument_list|(
name|nodeU
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|E
argument_list|>
name|inEdgesV
init|=
name|inEdges
argument_list|(
name|nodeV
argument_list|)
decl_stmt|;
return|return
name|outEdgesU
operator|.
name|size
argument_list|()
operator|<=
name|inEdgesV
operator|.
name|size
argument_list|()
condition|?
name|unmodifiableSet
argument_list|(
name|Sets
operator|.
name|filter
argument_list|(
name|outEdgesU
argument_list|,
name|connectedPredicate
argument_list|(
name|nodeU
argument_list|,
name|nodeV
argument_list|)
argument_list|)
argument_list|)
else|:
name|unmodifiableSet
argument_list|(
name|Sets
operator|.
name|filter
argument_list|(
name|inEdgesV
argument_list|,
name|connectedPredicate
argument_list|(
name|nodeV
argument_list|,
name|nodeU
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|connectedPredicate (final N nodePresent, final N nodeToCheck)
specifier|private
name|Predicate
argument_list|<
name|E
argument_list|>
name|connectedPredicate
parameter_list|(
specifier|final
name|N
name|nodePresent
parameter_list|,
specifier|final
name|N
name|nodeToCheck
parameter_list|)
block|{
return|return
operator|new
name|Predicate
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|E
name|edge
parameter_list|)
block|{
return|return
name|incidentNodes
argument_list|(
name|edge
argument_list|)
operator|.
name|adjacentNode
argument_list|(
name|nodePresent
argument_list|)
operator|.
name|equals
argument_list|(
name|nodeToCheck
argument_list|)
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|edgeConnecting (N nodeU, N nodeV)
specifier|public
name|Optional
argument_list|<
name|E
argument_list|>
name|edgeConnecting
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
block|{
name|Set
argument_list|<
name|E
argument_list|>
name|edgesConnecting
init|=
name|edgesConnecting
argument_list|(
name|nodeU
argument_list|,
name|nodeV
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|edgesConnecting
operator|.
name|size
argument_list|()
condition|)
block|{
case|case
literal|0
case|:
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
case|case
literal|1
case|:
return|return
name|Optional
operator|.
name|of
argument_list|(
name|edgesConnecting
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|MULTIPLE_EDGES_CONNECTING
argument_list|,
name|nodeU
argument_list|,
name|nodeV
argument_list|)
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|edgeConnectingOrNull (N nodeU, N nodeV)
specifier|public
name|E
name|edgeConnectingOrNull
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
block|{
return|return
name|edgeConnecting
argument_list|(
name|nodeU
argument_list|,
name|nodeV
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
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
return|return
operator|!
name|edgesConnecting
argument_list|(
name|nodeU
argument_list|,
name|nodeV
argument_list|)
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object obj)
specifier|public
specifier|final
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|Network
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Network
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|other
init|=
operator|(
name|Network
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|isDirected
argument_list|()
operator|==
name|other
operator|.
name|isDirected
argument_list|()
operator|&&
name|nodes
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|nodes
argument_list|()
argument_list|)
operator|&&
name|edgeIncidentNodesMap
argument_list|(
name|this
argument_list|)
operator|.
name|equals
argument_list|(
name|edgeIncidentNodesMap
argument_list|(
name|other
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
specifier|final
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|edgeIncidentNodesMap
argument_list|(
name|this
argument_list|)
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/** Returns a string representation of this network. */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|propertiesString
init|=
name|String
operator|.
name|format
argument_list|(
literal|"isDirected: %s, allowsParallelEdges: %s, allowsSelfLoops: %s"
argument_list|,
name|isDirected
argument_list|()
argument_list|,
name|allowsParallelEdges
argument_list|()
argument_list|,
name|allowsSelfLoops
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|String
operator|.
name|format
argument_list|(
name|GRAPH_STRING_FORMAT
argument_list|,
name|propertiesString
argument_list|,
name|nodes
argument_list|()
argument_list|,
name|edgeIncidentNodesMap
argument_list|(
name|this
argument_list|)
argument_list|)
return|;
block|}
DECL|method|edgeIncidentNodesMap (final Network<N, E> network)
specifier|private
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|Map
argument_list|<
name|E
argument_list|,
name|EndpointPair
argument_list|<
name|N
argument_list|>
argument_list|>
name|edgeIncidentNodesMap
parameter_list|(
specifier|final
name|Network
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|network
parameter_list|)
block|{
name|Function
argument_list|<
name|E
argument_list|,
name|EndpointPair
argument_list|<
name|N
argument_list|>
argument_list|>
name|edgeToIncidentNodesFn
init|=
operator|new
name|Function
argument_list|<
name|E
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
name|E
name|edge
parameter_list|)
block|{
return|return
name|network
operator|.
name|incidentNodes
argument_list|(
name|edge
argument_list|)
return|;
block|}
block|}
decl_stmt|;
return|return
name|Maps
operator|.
name|asMap
argument_list|(
name|network
operator|.
name|edges
argument_list|()
argument_list|,
name|edgeToIncidentNodesFn
argument_list|)
return|;
block|}
block|}
end_class

end_unit

