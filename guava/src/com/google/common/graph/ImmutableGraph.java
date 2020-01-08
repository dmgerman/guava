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
name|Functions
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
name|graph
operator|.
name|GraphConstants
operator|.
name|Presence
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|Immutable
import|;
end_import

begin_comment
comment|/**  * A {@link Graph} whose elements and structural relationships will never change. Instances of this  * class may be obtained with {@link #copyOf(Graph)}.  *  *<p>See the Guava User's Guide's<a  * href="https://github.com/google/guava/wiki/GraphsExplained#immutable-implementations">discussion  * of the {@code Immutable*} types</a> for more information on the properties and guarantees  * provided by this class.  *  * @author James Sexton  * @author Joshua O'Madadhain  * @author Omar Darwish  * @author Jens Nyman  * @param<N> Node parameter type  * @since 20.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|Immutable
argument_list|(
name|containerOf
operator|=
block|{
literal|"N"
block|}
argument_list|)
DECL|class|ImmutableGraph
specifier|public
class|class
name|ImmutableGraph
parameter_list|<
name|N
parameter_list|>
extends|extends
name|ForwardingGraph
argument_list|<
name|N
argument_list|>
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"Immutable"
argument_list|)
comment|// The backing graph must be immutable.
DECL|field|backingGraph
specifier|private
specifier|final
name|BaseGraph
argument_list|<
name|N
argument_list|>
name|backingGraph
decl_stmt|;
DECL|method|ImmutableGraph (BaseGraph<N> backingGraph)
name|ImmutableGraph
parameter_list|(
name|BaseGraph
argument_list|<
name|N
argument_list|>
name|backingGraph
parameter_list|)
block|{
name|this
operator|.
name|backingGraph
operator|=
name|backingGraph
expr_stmt|;
block|}
comment|/** Returns an immutable copy of {@code graph}. */
DECL|method|copyOf (Graph<N> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|>
name|ImmutableGraph
argument_list|<
name|N
argument_list|>
name|copyOf
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|>
name|graph
parameter_list|)
block|{
return|return
operator|(
name|graph
operator|instanceof
name|ImmutableGraph
operator|)
condition|?
operator|(
name|ImmutableGraph
argument_list|<
name|N
argument_list|>
operator|)
name|graph
else|:
operator|new
name|ImmutableGraph
argument_list|<
name|N
argument_list|>
argument_list|(
operator|new
name|ConfigurableValueGraph
argument_list|<
name|N
argument_list|,
name|Presence
argument_list|>
argument_list|(
name|GraphBuilder
operator|.
name|from
argument_list|(
name|graph
argument_list|)
argument_list|,
name|getNodeConnections
argument_list|(
name|graph
argument_list|)
argument_list|,
name|graph
operator|.
name|edges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Simply returns its argument.    *    * @deprecated no need to use this    */
annotation|@
name|Deprecated
DECL|method|copyOf (ImmutableGraph<N> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|>
name|ImmutableGraph
argument_list|<
name|N
argument_list|>
name|copyOf
parameter_list|(
name|ImmutableGraph
argument_list|<
name|N
argument_list|>
name|graph
parameter_list|)
block|{
return|return
name|checkNotNull
argument_list|(
name|graph
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|incidentEdgeOrder ()
specifier|public
name|ElementOrder
argument_list|<
name|N
argument_list|>
name|incidentEdgeOrder
parameter_list|()
block|{
return|return
name|ElementOrder
operator|.
name|stable
argument_list|()
return|;
block|}
DECL|method|getNodeConnections ( Graph<N> graph)
specifier|private
specifier|static
parameter_list|<
name|N
parameter_list|>
name|ImmutableMap
argument_list|<
name|N
argument_list|,
name|GraphConnections
argument_list|<
name|N
argument_list|,
name|Presence
argument_list|>
argument_list|>
name|getNodeConnections
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|>
name|graph
parameter_list|)
block|{
comment|// ImmutableMap.Builder maintains the order of the elements as inserted, so the map will have
comment|// whatever ordering the graph's nodes do, so ImmutableSortedMap is unnecessary even if the
comment|// input nodes are sorted.
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|N
argument_list|,
name|GraphConnections
argument_list|<
name|N
argument_list|,
name|Presence
argument_list|>
argument_list|>
name|nodeConnections
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|N
name|node
range|:
name|graph
operator|.
name|nodes
argument_list|()
control|)
block|{
name|nodeConnections
operator|.
name|put
argument_list|(
name|node
argument_list|,
name|connectionsOf
argument_list|(
name|graph
argument_list|,
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|nodeConnections
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|connectionsOf (Graph<N> graph, N node)
specifier|private
specifier|static
parameter_list|<
name|N
parameter_list|>
name|GraphConnections
argument_list|<
name|N
argument_list|,
name|Presence
argument_list|>
name|connectionsOf
parameter_list|(
name|Graph
argument_list|<
name|N
argument_list|>
name|graph
parameter_list|,
name|N
name|node
parameter_list|)
block|{
name|Function
argument_list|<
name|N
argument_list|,
name|Presence
argument_list|>
name|edgeValueFn
init|=
operator|(
name|Function
argument_list|<
name|N
argument_list|,
name|Presence
argument_list|>
operator|)
name|Functions
operator|.
name|constant
argument_list|(
name|Presence
operator|.
name|EDGE_EXISTS
argument_list|)
decl_stmt|;
return|return
name|graph
operator|.
name|isDirected
argument_list|()
condition|?
name|DirectedGraphConnections
operator|.
name|ofImmutable
argument_list|(
name|node
argument_list|,
name|graph
operator|.
name|incidentEdges
argument_list|(
name|node
argument_list|)
argument_list|,
name|edgeValueFn
argument_list|)
else|:
name|UndirectedGraphConnections
operator|.
name|ofImmutable
argument_list|(
name|Maps
operator|.
name|asMap
argument_list|(
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
argument_list|,
name|edgeValueFn
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|BaseGraph
argument_list|<
name|N
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|backingGraph
return|;
block|}
comment|/**    * A builder for creating {@link ImmutableGraph} instances, especially {@code static final}    * graphs. Example:    *    *<pre>{@code    * static final ImmutableGraph<Country> COUNTRY_ADJACENCY_GRAPH =    *     GraphBuilder.undirected()    *         .<Country>immutable()    *         .putEdge(FRANCE, GERMANY)    *         .putEdge(FRANCE, BELGIUM)    *         .putEdge(GERMANY, BELGIUM)    *         .addNode(ICELAND)    *         .build();    * }</pre>    *    *<p>Builder instances can be reused; it is safe to call {@link #build} multiple times to build    * multiple graphs in series. Each new graph contains all the elements of the ones created before    * it.    *    * @since 28.0    */
DECL|class|Builder
specifier|public
specifier|static
class|class
name|Builder
parameter_list|<
name|N
parameter_list|>
block|{
DECL|field|mutableGraph
specifier|private
specifier|final
name|MutableGraph
argument_list|<
name|N
argument_list|>
name|mutableGraph
decl_stmt|;
DECL|method|Builder (GraphBuilder<N> graphBuilder)
name|Builder
parameter_list|(
name|GraphBuilder
argument_list|<
name|N
argument_list|>
name|graphBuilder
parameter_list|)
block|{
comment|// The incidentEdgeOrder for immutable graphs is always stable. However, we don't want to
comment|// modify this builder, so we make a copy instead.
name|this
operator|.
name|mutableGraph
operator|=
name|graphBuilder
operator|.
name|copy
argument_list|()
operator|.
name|incidentEdgeOrder
argument_list|(
name|ElementOrder
operator|.
expr|<
name|N
operator|>
name|stable
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
comment|/**      * Adds {@code node} if it is not already present.      *      *<p><b>Nodes must be unique</b>, just as {@code Map} keys must be. They must also be non-null.      *      * @return this {@code Builder} object      */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addNode (N node)
specifier|public
name|Builder
argument_list|<
name|N
argument_list|>
name|addNode
parameter_list|(
name|N
name|node
parameter_list|)
block|{
name|mutableGraph
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds an edge connecting {@code nodeU} to {@code nodeV} if one is not already present.      *      *<p>If the graph is directed, the resultant edge will be directed; otherwise, it will be      * undirected.      *      *<p>If {@code nodeU} and {@code nodeV} are not already present in this graph, this method will      * silently {@link #addNode(Object) add} {@code nodeU} and {@code nodeV} to the graph.      *      * @return this {@code Builder} object      * @throws IllegalArgumentException if the introduction of the edge would violate {@link      *     #allowsSelfLoops()}      */
annotation|@
name|CanIgnoreReturnValue
DECL|method|putEdge (N nodeU, N nodeV)
specifier|public
name|Builder
argument_list|<
name|N
argument_list|>
name|putEdge
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
block|{
name|mutableGraph
operator|.
name|putEdge
argument_list|(
name|nodeU
argument_list|,
name|nodeV
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds an edge connecting {@code endpoints} (in the order, if any, specified by {@code      * endpoints}) if one is not already present.      *      *<p>If this graph is directed, {@code endpoints} must be ordered and the added edge will be      * directed; if it is undirected, the added edge will be undirected.      *      *<p>If this graph is directed, {@code endpoints} must be ordered.      *      *<p>If either or both endpoints are not already present in this graph, this method will      * silently {@link #addNode(Object) add} each missing endpoint to the graph.      *      * @return this {@code Builder} object      * @throws IllegalArgumentException if the introduction of the edge would violate {@link      *     #allowsSelfLoops()}      * @throws IllegalArgumentException if the endpoints are unordered and the graph is directed      */
annotation|@
name|CanIgnoreReturnValue
DECL|method|putEdge (EndpointPair<N> endpoints)
specifier|public
name|Builder
argument_list|<
name|N
argument_list|>
name|putEdge
parameter_list|(
name|EndpointPair
argument_list|<
name|N
argument_list|>
name|endpoints
parameter_list|)
block|{
name|mutableGraph
operator|.
name|putEdge
argument_list|(
name|endpoints
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Returns a newly-created {@code ImmutableGraph} based on the contents of this {@code Builder}.      */
DECL|method|build ()
specifier|public
name|ImmutableGraph
argument_list|<
name|N
argument_list|>
name|build
parameter_list|()
block|{
return|return
name|ImmutableGraph
operator|.
name|copyOf
argument_list|(
name|mutableGraph
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

