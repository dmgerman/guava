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
comment|/**  * This class provides a skeletal implementation of {@link Graph}. It is recommended to extend this  * class rather than implement {@link Graph} directly, to ensure consistent {@link #equals(Object)}  * and {@link #hashCode()} results across different graph implementations.  *  * @author James Sexton  * @param<N> Node parameter type  * @since 20.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|AbstractGraph
specifier|public
specifier|abstract
class|class
name|AbstractGraph
parameter_list|<
name|N
parameter_list|>
implements|implements
name|Graph
argument_list|<
name|N
argument_list|>
block|{
comment|/**    * Returns the number of edges in this graph; used to calculate the size of {@link #edges()}.    * The default implementation is O(|N|). You can manually keep track of the number of edges and    * override this method for better performance.    */
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
name|this
argument_list|,
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
comment|/**    * A reasonable default implementation of {@link Graph#edges()} defined in terms of    * {@link #nodes()} and {@link #successors(Object)}.    */
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
name|EndpointsIterator
operator|.
name|of
argument_list|(
name|AbstractGraph
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
name|nodes
argument_list|()
operator|.
name|contains
argument_list|(
name|endpoints
operator|.
name|nodeA
argument_list|()
argument_list|)
operator|&&
name|successors
argument_list|(
name|endpoints
operator|.
name|nodeA
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|endpoints
operator|.
name|nodeB
argument_list|()
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
name|Graph
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Graph
argument_list|<
name|?
argument_list|>
name|other
init|=
operator|(
name|Graph
argument_list|<
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
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|N
name|node
range|:
name|nodes
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|successors
argument_list|(
name|node
argument_list|)
operator|.
name|equals
argument_list|(
name|other
operator|.
name|successors
argument_list|(
name|node
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
name|N
argument_list|>
argument_list|>
name|nodeToSuccessors
init|=
operator|new
name|Function
argument_list|<
name|N
argument_list|,
name|Set
argument_list|<
name|N
argument_list|>
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
name|apply
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|successors
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
name|nodeToSuccessors
argument_list|)
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/**    * Returns a string representation of this graph.    */
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
literal|"isDirected: %s, allowsSelfLoops: %s"
argument_list|,
name|isDirected
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
name|edges
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns the number of times an edge touches {@code node} in {@code graph}. This is equivalent    * to the number of edges incident to {@code node} in the graph, with self-loops counting twice.    *    *<p>If this number is greater than {@code Integer.MAX_VALUE}, returns {@code Integer.MAX_VALUE}.    *    * @throws IllegalArgumentException if {@code node} is not an element of this graph    */
comment|// TODO(b/30649235): What to do with this? Move to Graphs or interfaces? Provide in/outDegree?
DECL|method|degree (Graph<?> graph, Object node)
specifier|private
specifier|static
name|int
name|degree
parameter_list|(
name|Graph
argument_list|<
name|?
argument_list|>
name|graph
parameter_list|,
name|Object
name|node
parameter_list|)
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
name|IntMath
operator|.
name|saturatedAdd
argument_list|(
name|graph
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
operator|.
name|size
argument_list|()
argument_list|,
name|graph
operator|.
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
name|int
name|selfLoops
init|=
operator|(
name|graph
operator|.
name|allowsSelfLoops
argument_list|()
operator|&&
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
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
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
operator|.
name|size
argument_list|()
argument_list|,
name|selfLoops
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

