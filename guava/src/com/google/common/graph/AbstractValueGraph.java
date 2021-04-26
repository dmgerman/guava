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
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|Maps
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
name|CheckForNull
import|;
end_import

begin_comment
comment|/**  * This class provides a skeletal implementation of {@link ValueGraph}. It is recommended to extend  * this class rather than implement {@link ValueGraph} directly.  *  *<p>The methods implemented in this class should not be overridden unless the subclass admits a  * more efficient implementation.  *  * @author James Sexton  * @param<N> Node parameter type  * @param<V> Value parameter type  * @since 20.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|AbstractValueGraph
specifier|public
specifier|abstract
class|class
name|AbstractValueGraph
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractBaseGraph
argument_list|<
name|N
argument_list|>
implements|implements
name|ValueGraph
argument_list|<
name|N
argument_list|,
name|V
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
name|AbstractValueGraph
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
return|return
name|AbstractValueGraph
operator|.
name|this
operator|.
name|edges
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
name|AbstractValueGraph
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
name|AbstractValueGraph
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
name|ElementOrder
argument_list|<
name|N
argument_list|>
name|nodeOrder
parameter_list|()
block|{
return|return
name|AbstractValueGraph
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
name|ElementOrder
argument_list|<
name|N
argument_list|>
name|incidentEdgeOrder
parameter_list|()
block|{
return|return
name|AbstractValueGraph
operator|.
name|this
operator|.
name|incidentEdgeOrder
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
name|AbstractValueGraph
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
name|AbstractValueGraph
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
name|AbstractValueGraph
operator|.
name|this
operator|.
name|successors
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|degree
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|AbstractValueGraph
operator|.
name|this
operator|.
name|degree
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|inDegree
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|AbstractValueGraph
operator|.
name|this
operator|.
name|inDegree
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|outDegree
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|AbstractValueGraph
operator|.
name|this
operator|.
name|outDegree
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
DECL|method|edgeValue (N nodeU, N nodeV)
specifier|public
name|Optional
argument_list|<
name|V
argument_list|>
name|edgeValue
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|edgeValueOrDefault
argument_list|(
name|nodeU
argument_list|,
name|nodeV
argument_list|,
literal|null
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|edgeValue (EndpointPair<N> endpoints)
specifier|public
name|Optional
argument_list|<
name|V
argument_list|>
name|edgeValue
parameter_list|(
name|EndpointPair
argument_list|<
name|N
argument_list|>
name|endpoints
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|edgeValueOrDefault
argument_list|(
name|endpoints
argument_list|,
literal|null
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@heckForNull Object obj)
specifier|public
specifier|final
name|boolean
name|equals
parameter_list|(
annotation|@
name|CheckForNull
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
name|ValueGraph
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ValueGraph
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|other
init|=
operator|(
name|ValueGraph
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
name|edgeValueMap
argument_list|(
name|this
argument_list|)
operator|.
name|equals
argument_list|(
name|edgeValueMap
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
name|edgeValueMap
argument_list|(
name|this
argument_list|)
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/** Returns a string representation of this graph. */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"isDirected: "
operator|+
name|isDirected
argument_list|()
operator|+
literal|", allowsSelfLoops: "
operator|+
name|allowsSelfLoops
argument_list|()
operator|+
literal|", nodes: "
operator|+
name|nodes
argument_list|()
operator|+
literal|", edges: "
operator|+
name|edgeValueMap
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|edgeValueMap (final ValueGraph<N, V> graph)
specifier|private
specifier|static
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|EndpointPair
argument_list|<
name|N
argument_list|>
argument_list|,
name|V
argument_list|>
name|edgeValueMap
parameter_list|(
specifier|final
name|ValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|graph
parameter_list|)
block|{
name|Function
argument_list|<
name|EndpointPair
argument_list|<
name|N
argument_list|>
argument_list|,
name|V
argument_list|>
name|edgeToValueFn
init|=
operator|new
name|Function
argument_list|<
name|EndpointPair
argument_list|<
name|N
argument_list|>
argument_list|,
name|V
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|V
name|apply
parameter_list|(
name|EndpointPair
argument_list|<
name|N
argument_list|>
name|edge
parameter_list|)
block|{
comment|// requireNonNull is safe because the endpoint pair comes from the graph.
return|return
name|requireNonNull
argument_list|(
name|graph
operator|.
name|edgeValueOrDefault
argument_list|(
name|edge
operator|.
name|nodeU
argument_list|()
argument_list|,
name|edge
operator|.
name|nodeV
argument_list|()
argument_list|,
literal|null
argument_list|)
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
name|graph
operator|.
name|edges
argument_list|()
argument_list|,
name|edgeToValueFn
argument_list|)
return|;
block|}
block|}
end_class

end_unit

