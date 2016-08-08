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
name|graph
operator|.
name|GraphConstants
operator|.
name|DEFAULT_NODE_COUNT
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
name|NODE_NOT_IN_GRAPH
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
name|java
operator|.
name|util
operator|.
name|TreeMap
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
comment|/**  * Abstract configurable implementation of {@link Graph} that supports the options supplied  * by {@link GraphBuilder}.  *  *<p>This class maintains a map of nodes to {@link GraphConnections}.  *  *<p>{@code Set}-returning accessors return unmodifiable views: the view returned will reflect  * changes to the graph (if the graph is mutable) but may not be modified by the user.  * The behavior of the returned view is undefined in the following cases:  *<ul>  *<li>Removing the element on which the accessor is called (e.g.:  *<pre>{@code  *     Set<N> adjacentNodes = adjacentNodes(node);  *     graph.removeNode(node);}</pre>  *     At this point, the contents of {@code adjacentNodes} are undefined.  *</ul>  *  *<p>The time complexity of all {@code Set}-returning accessors is O(1), since views are returned.  *  * @author James Sexton  * @author Joshua O'Madadhain  * @author Omar Darwish  * @param<N> Node parameter type  */
end_comment

begin_class
DECL|class|AbstractConfigurableGraph
specifier|abstract
class|class
name|AbstractConfigurableGraph
parameter_list|<
name|N
parameter_list|>
extends|extends
name|AbstractGraph
argument_list|<
name|N
argument_list|>
block|{
DECL|field|isDirected
specifier|private
specifier|final
name|boolean
name|isDirected
decl_stmt|;
DECL|field|allowsSelfLoops
specifier|private
specifier|final
name|boolean
name|allowsSelfLoops
decl_stmt|;
DECL|field|nodeOrder
specifier|private
specifier|final
name|ElementOrder
argument_list|<
name|N
argument_list|>
name|nodeOrder
decl_stmt|;
DECL|field|nodeConnections
specifier|protected
specifier|final
name|MapIteratorCache
argument_list|<
name|N
argument_list|,
name|GraphConnections
argument_list|<
name|N
argument_list|>
argument_list|>
name|nodeConnections
decl_stmt|;
DECL|field|edgeCount
specifier|protected
name|long
name|edgeCount
decl_stmt|;
comment|// must be updated when edges are added or removed
comment|/**    * Constructs a graph with the properties specified in {@code builder}.    */
DECL|method|AbstractConfigurableGraph (GraphBuilder<? super N> builder)
name|AbstractConfigurableGraph
parameter_list|(
name|GraphBuilder
argument_list|<
name|?
super|super
name|N
argument_list|>
name|builder
parameter_list|)
block|{
name|this
argument_list|(
name|builder
argument_list|,
name|builder
operator|.
name|nodeOrder
operator|.
expr|<
name|N
argument_list|,
name|GraphConnections
argument_list|<
name|N
argument_list|>
operator|>
name|createMap
argument_list|(
name|builder
operator|.
name|expectedNodeCount
operator|.
name|or
argument_list|(
name|DEFAULT_NODE_COUNT
argument_list|)
argument_list|)
argument_list|,
literal|0L
comment|/* edgeCount */
argument_list|)
expr_stmt|;
block|}
comment|/**    * Constructs a graph with the properties specified in {@code builder}, initialized with    * the given node map.    */
DECL|method|AbstractConfigurableGraph (GraphBuilder<? super N> builder, Map<N, GraphConnections<N>> nodeConnections, long edgeCount)
name|AbstractConfigurableGraph
parameter_list|(
name|GraphBuilder
argument_list|<
name|?
super|super
name|N
argument_list|>
name|builder
parameter_list|,
name|Map
argument_list|<
name|N
argument_list|,
name|GraphConnections
argument_list|<
name|N
argument_list|>
argument_list|>
name|nodeConnections
parameter_list|,
name|long
name|edgeCount
parameter_list|)
block|{
name|this
operator|.
name|isDirected
operator|=
name|builder
operator|.
name|directed
expr_stmt|;
name|this
operator|.
name|allowsSelfLoops
operator|=
name|builder
operator|.
name|allowsSelfLoops
expr_stmt|;
name|this
operator|.
name|nodeOrder
operator|=
name|builder
operator|.
name|nodeOrder
operator|.
name|cast
argument_list|()
expr_stmt|;
comment|// Prefer the heavier "MapRetrievalCache" for nodes if lookup is expensive.
name|this
operator|.
name|nodeConnections
operator|=
operator|(
name|nodeConnections
operator|instanceof
name|TreeMap
operator|)
condition|?
operator|new
name|MapRetrievalCache
argument_list|<
name|N
argument_list|,
name|GraphConnections
argument_list|<
name|N
argument_list|>
argument_list|>
argument_list|(
name|nodeConnections
argument_list|)
else|:
operator|new
name|MapIteratorCache
argument_list|<
name|N
argument_list|,
name|GraphConnections
argument_list|<
name|N
argument_list|>
argument_list|>
argument_list|(
name|nodeConnections
argument_list|)
expr_stmt|;
name|this
operator|.
name|edgeCount
operator|=
name|checkNonNegative
argument_list|(
name|edgeCount
argument_list|)
expr_stmt|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>The order of iteration for this set is determined by the {@code ElementOrder<N>} provided    * to the {@code GraphBuilder} that was used to create this instance.    * By default, that order is the order in which the nodes were added to the graph.    */
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
name|nodeConnections
operator|.
name|unmodifiableKeySet
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
name|isDirected
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
name|allowsSelfLoops
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
name|nodeOrder
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
name|checkedConnections
argument_list|(
name|node
argument_list|)
operator|.
name|adjacentNodes
argument_list|()
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
name|checkedConnections
argument_list|(
name|node
argument_list|)
operator|.
name|predecessors
argument_list|()
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
name|checkedConnections
argument_list|(
name|node
argument_list|)
operator|.
name|successors
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|edgeCount ()
specifier|protected
name|long
name|edgeCount
parameter_list|()
block|{
return|return
name|edgeCount
return|;
block|}
DECL|method|checkedConnections (Object node)
specifier|protected
specifier|final
name|GraphConnections
argument_list|<
name|N
argument_list|>
name|checkedConnections
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|node
argument_list|,
literal|"node"
argument_list|)
expr_stmt|;
name|GraphConnections
argument_list|<
name|N
argument_list|>
name|connections
init|=
name|nodeConnections
operator|.
name|get
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|connections
operator|!=
literal|null
argument_list|,
name|NODE_NOT_IN_GRAPH
argument_list|,
name|node
argument_list|)
expr_stmt|;
return|return
name|connections
return|;
block|}
DECL|method|containsNode (@ullable Object node)
specifier|protected
specifier|final
name|boolean
name|containsNode
parameter_list|(
annotation|@
name|Nullable
name|Object
name|node
parameter_list|)
block|{
return|return
name|nodeConnections
operator|.
name|containsKey
argument_list|(
name|node
argument_list|)
return|;
block|}
block|}
end_class

end_unit

