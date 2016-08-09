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
comment|/**  * This class provides a skeletal implementation of {@link Network}. It is recommended to extend  * this class rather than implement {@link Network} directly, to ensure consistent  * {@link #equals(Object)} and {@link #hashCode()} results across different network implementations.  *  * @author James Sexton  * @param<N> Node parameter type  * @param<E> Edge parameter type  * @since 20.0  */
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
name|Endpoints
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
comment|// Optimized implementation assumes no parallel edges (1:1 edge to Endpoints mapping).
return|return
operator|new
name|AbstractSet
argument_list|<
name|Endpoints
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
name|Endpoints
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
name|Endpoints
argument_list|<
name|N
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Endpoints
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
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|Endpoints
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Endpoints
argument_list|<
name|?
argument_list|>
name|endpoints
init|=
operator|(
name|Endpoints
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
name|endpoints
operator|.
name|isDirected
argument_list|()
operator|&&
operator|!
name|edgesConnecting
argument_list|(
name|endpoints
operator|.
name|nodeA
argument_list|()
argument_list|,
name|endpoints
operator|.
name|nodeB
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
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
name|Object
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
name|Object
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
name|Object
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
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object obj)
specifier|public
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
if|if
condition|(
name|isDirected
argument_list|()
operator|!=
name|other
operator|.
name|isDirected
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
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
operator|||
operator|!
name|edges
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|edges
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|E
name|edge
range|:
name|edges
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|incidentNodes
argument_list|(
name|edge
argument_list|)
operator|.
name|equals
argument_list|(
name|other
operator|.
name|incidentNodes
argument_list|(
name|edge
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|Function
argument_list|<
name|N
argument_list|,
name|Set
argument_list|<
name|E
argument_list|>
argument_list|>
name|nodeToOutEdges
init|=
operator|new
name|Function
argument_list|<
name|N
argument_list|,
name|Set
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|apply
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|outEdges
argument_list|(
name|node
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
name|nodes
argument_list|()
argument_list|,
name|nodeToOutEdges
argument_list|)
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/**    * Returns a string representation of this network.    */
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
name|Function
argument_list|<
name|E
argument_list|,
name|Endpoints
argument_list|<
name|N
argument_list|>
argument_list|>
name|edgeToEndpointsFn
init|=
operator|new
name|Function
argument_list|<
name|E
argument_list|,
name|Endpoints
argument_list|<
name|N
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Endpoints
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
name|Maps
operator|.
name|asMap
argument_list|(
name|edges
argument_list|()
argument_list|,
name|edgeToEndpointsFn
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

