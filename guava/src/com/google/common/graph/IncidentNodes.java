begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2014 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * An immutable set representing the nodes incident to an edge in a graph.  *  * @author James Sexton  * @param<N> Node parameter type  */
end_comment

begin_class
DECL|class|IncidentNodes
specifier|abstract
class|class
name|IncidentNodes
parameter_list|<
name|N
parameter_list|>
extends|extends
name|AbstractSet
argument_list|<
name|N
argument_list|>
block|{
DECL|method|of (N node1, N node2)
specifier|static
parameter_list|<
name|N
parameter_list|>
name|IncidentNodes
argument_list|<
name|N
argument_list|>
name|of
parameter_list|(
name|N
name|node1
parameter_list|,
name|N
name|node2
parameter_list|)
block|{
if|if
condition|(
name|node1
operator|.
name|equals
argument_list|(
name|node2
argument_list|)
condition|)
block|{
return|return
operator|new
name|OneNode
argument_list|<
name|N
argument_list|>
argument_list|(
name|node1
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|TwoNodes
argument_list|<
name|N
argument_list|>
argument_list|(
name|node1
argument_list|,
name|node2
argument_list|)
return|;
block|}
block|}
DECL|method|of (Set<N> nodes)
specifier|static
parameter_list|<
name|N
parameter_list|>
name|IncidentNodes
argument_list|<
name|N
argument_list|>
name|of
parameter_list|(
name|Set
argument_list|<
name|N
argument_list|>
name|nodes
parameter_list|)
block|{
name|Iterator
argument_list|<
name|N
argument_list|>
name|nodesIterator
init|=
name|nodes
operator|.
name|iterator
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|nodes
operator|.
name|size
argument_list|()
condition|)
block|{
case|case
literal|1
case|:
return|return
operator|new
name|OneNode
argument_list|<
name|N
argument_list|>
argument_list|(
name|nodesIterator
operator|.
name|next
argument_list|()
argument_list|)
return|;
case|case
literal|2
case|:
return|return
operator|new
name|TwoNodes
argument_list|<
name|N
argument_list|>
argument_list|(
name|nodesIterator
operator|.
name|next
argument_list|()
argument_list|,
name|nodesIterator
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
literal|"Hypergraphs are not currently supported. An edge in a"
operator|+
literal|" non-hypergraph cannot be incident to "
operator|+
name|nodes
operator|.
name|size
argument_list|()
operator|+
literal|" nodes: "
operator|+
name|nodes
argument_list|)
throw|;
block|}
block|}
DECL|method|isSelfLoop ()
name|boolean
name|isSelfLoop
parameter_list|()
block|{
return|return
name|size
argument_list|()
operator|==
literal|1
return|;
block|}
comment|/**    * In the case of a directed graph, returns the source node of the incident edge. In the case of    * an undirected graph, returns an arbitrary (but consistent) endpoint of the incident edge.    */
DECL|method|node1 ()
specifier|abstract
name|N
name|node1
parameter_list|()
function_decl|;
comment|/**    * Returns the node opposite to {@link #node1} along the incident edge. In the case of a directed    * graph, this will always be the target node of the incident edge.    */
DECL|method|node2 ()
specifier|abstract
name|N
name|node2
parameter_list|()
function_decl|;
DECL|class|OneNode
specifier|private
specifier|static
specifier|final
class|class
name|OneNode
parameter_list|<
name|N
parameter_list|>
extends|extends
name|IncidentNodes
argument_list|<
name|N
argument_list|>
block|{
DECL|field|node
specifier|private
specifier|final
name|N
name|node
decl_stmt|;
DECL|method|OneNode (N node)
specifier|private
name|OneNode
parameter_list|(
name|N
name|node
parameter_list|)
block|{
name|this
operator|.
name|node
operator|=
name|checkNotNull
argument_list|(
name|node
argument_list|,
literal|"node"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|N
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|singletonIterator
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|contains (@ullable Object object)
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
name|node
operator|.
name|equals
argument_list|(
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|node1 ()
name|N
name|node1
parameter_list|()
block|{
return|return
name|node
return|;
block|}
annotation|@
name|Override
DECL|method|node2 ()
name|N
name|node2
parameter_list|()
block|{
return|return
name|node
return|;
block|}
block|}
DECL|class|TwoNodes
specifier|private
specifier|static
specifier|final
class|class
name|TwoNodes
parameter_list|<
name|N
parameter_list|>
extends|extends
name|IncidentNodes
argument_list|<
name|N
argument_list|>
block|{
DECL|field|node1
specifier|private
specifier|final
name|N
name|node1
decl_stmt|;
DECL|field|node2
specifier|private
specifier|final
name|N
name|node2
decl_stmt|;
comment|/**      * An immutable set with two non-equal nodes. Iterates as {@code node1}, {@code node2}.      */
DECL|method|TwoNodes (N node1, N node2)
specifier|private
name|TwoNodes
parameter_list|(
name|N
name|node1
parameter_list|,
name|N
name|node2
parameter_list|)
block|{
name|this
operator|.
name|node1
operator|=
name|checkNotNull
argument_list|(
name|node1
argument_list|,
literal|"node1"
argument_list|)
expr_stmt|;
name|this
operator|.
name|node2
operator|=
name|checkNotNull
argument_list|(
name|node2
argument_list|,
literal|"node2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|N
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|forArray
argument_list|(
name|node1
argument_list|,
name|node2
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
literal|2
return|;
block|}
annotation|@
name|Override
DECL|method|contains (@ullable Object object)
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
name|node1
operator|.
name|equals
argument_list|(
name|object
argument_list|)
operator|||
name|node2
operator|.
name|equals
argument_list|(
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|node1 ()
name|N
name|node1
parameter_list|()
block|{
return|return
name|node1
return|;
block|}
annotation|@
name|Override
DECL|method|node2 ()
name|N
name|node2
parameter_list|()
block|{
return|return
name|node2
return|;
block|}
block|}
block|}
end_class

end_unit

