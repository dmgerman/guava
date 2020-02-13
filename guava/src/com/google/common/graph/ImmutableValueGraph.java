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
comment|/**  * A {@link ValueGraph} whose elements and structural relationships will never change. Instances of  * this class may be obtained with {@link #copyOf(ValueGraph)}.  *  *<p>See the Guava User's Guide's<a  * href="https://github.com/google/guava/wiki/GraphsExplained#immutable-implementations">discussion  * of the {@code Immutable*} types</a> for more information on the properties and guarantees  * provided by this class.  *  * @author James Sexton  * @author Jens Nyman  * @param<N> Node parameter type  * @param<V> Value parameter type  * @since 20.0  */
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
block|,
literal|"V"
block|}
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"Immutable"
argument_list|)
comment|// Extends ConfigurableValueGraph but uses ImmutableMaps.
DECL|class|ImmutableValueGraph
specifier|public
specifier|final
class|class
name|ImmutableValueGraph
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
extends|extends
name|StandardValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
block|{
DECL|method|ImmutableValueGraph (ValueGraph<N, V> graph)
specifier|private
name|ImmutableValueGraph
parameter_list|(
name|ValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|graph
parameter_list|)
block|{
name|super
argument_list|(
name|ValueGraphBuilder
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
expr_stmt|;
block|}
comment|/** Returns an immutable copy of {@code graph}. */
DECL|method|copyOf (ValueGraph<N, V> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
name|ImmutableValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|copyOf
parameter_list|(
name|ValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|graph
parameter_list|)
block|{
return|return
operator|(
name|graph
operator|instanceof
name|ImmutableValueGraph
operator|)
condition|?
operator|(
name|ImmutableValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
operator|)
name|graph
else|:
operator|new
name|ImmutableValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
argument_list|(
name|graph
argument_list|)
return|;
block|}
comment|/**    * Simply returns its argument.    *    * @deprecated no need to use this    */
annotation|@
name|Deprecated
DECL|method|copyOf (ImmutableValueGraph<N, V> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
name|ImmutableValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|copyOf
parameter_list|(
name|ImmutableValueGraph
argument_list|<
name|N
argument_list|,
name|V
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
annotation|@
name|Override
DECL|method|asGraph ()
specifier|public
name|ImmutableGraph
argument_list|<
name|N
argument_list|>
name|asGraph
parameter_list|()
block|{
return|return
operator|new
name|ImmutableGraph
argument_list|<
name|N
argument_list|>
argument_list|(
name|this
argument_list|)
return|;
comment|// safe because the view is effectively immutable
block|}
DECL|method|getNodeConnections ( ValueGraph<N, V> graph)
specifier|private
specifier|static
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
name|ImmutableMap
argument_list|<
name|N
argument_list|,
name|GraphConnections
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
argument_list|>
name|getNodeConnections
parameter_list|(
name|ValueGraph
argument_list|<
name|N
argument_list|,
name|V
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
name|V
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
DECL|method|connectionsOf ( final ValueGraph<N, V> graph, final N node)
specifier|private
specifier|static
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
name|GraphConnections
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|connectionsOf
parameter_list|(
specifier|final
name|ValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|graph
parameter_list|,
specifier|final
name|N
name|node
parameter_list|)
block|{
name|Function
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|successorNodeToValueFn
init|=
operator|new
name|Function
argument_list|<
name|N
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
name|N
name|successorNode
parameter_list|)
block|{
return|return
name|graph
operator|.
name|edgeValueOrDefault
argument_list|(
name|node
argument_list|,
name|successorNode
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
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
name|successorNodeToValueFn
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
name|successorNodeToValueFn
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * A builder for creating {@link ImmutableValueGraph} instances, especially {@code static final}    * graphs. Example:    *    *<pre>{@code    * static final ImmutableValueGraph<City, Distance> CITY_ROAD_DISTANCE_GRAPH =    *     ValueGraphBuilder.undirected()    *         .<City, Distance>immutable()    *         .putEdgeValue(PARIS, BERLIN, kilometers(1060))    *         .putEdgeValue(PARIS, BRUSSELS, kilometers(317))    *         .putEdgeValue(BERLIN, BRUSSELS, kilometers(764))    *         .addNode(REYKJAVIK)    *         .build();    * }</pre>    *    *<p>Builder instances can be reused; it is safe to call {@link #build} multiple times to build    * multiple graphs in series. Each new graph contains all the elements of the ones created before    * it.    *    * @since 28.0    */
DECL|class|Builder
specifier|public
specifier|static
class|class
name|Builder
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
block|{
DECL|field|mutableValueGraph
specifier|private
specifier|final
name|MutableValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|mutableValueGraph
decl_stmt|;
DECL|method|Builder (ValueGraphBuilder<N, V> graphBuilder)
name|Builder
parameter_list|(
name|ValueGraphBuilder
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|graphBuilder
parameter_list|)
block|{
comment|// The incidentEdgeOrder for immutable graphs is always stable. However, we don't want to
comment|// modify this builder, so we make a copy instead.
name|this
operator|.
name|mutableValueGraph
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
name|ImmutableValueGraph
operator|.
name|Builder
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|addNode
parameter_list|(
name|N
name|node
parameter_list|)
block|{
name|mutableValueGraph
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
comment|/**      * Adds an edge connecting {@code nodeU} to {@code nodeV} if one is not already present, and      * sets a value for that edge to {@code value} (overwriting the existing value, if any).      *      *<p>If the graph is directed, the resultant edge will be directed; otherwise, it will be      * undirected.      *      *<p>Values do not have to be unique. However, values must be non-null.      *      *<p>If {@code nodeU} and {@code nodeV} are not already present in this graph, this method will      * silently {@link #addNode(Object) add} {@code nodeU} and {@code nodeV} to the graph.      *      * @return this {@code Builder} object      * @throws IllegalArgumentException if the introduction of the edge would violate {@link      *     #allowsSelfLoops()}      */
annotation|@
name|CanIgnoreReturnValue
DECL|method|putEdgeValue (N nodeU, N nodeV, V value)
specifier|public
name|ImmutableValueGraph
operator|.
name|Builder
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|putEdgeValue
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|mutableValueGraph
operator|.
name|putEdgeValue
argument_list|(
name|nodeU
argument_list|,
name|nodeV
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds an edge connecting {@code endpoints} if one is not already present, and sets a value for      * that edge to {@code value} (overwriting the existing value, if any).      *      *<p>If the graph is directed, the resultant edge will be directed; otherwise, it will be      * undirected.      *      *<p>If this graph is directed, {@code endpoints} must be ordered.      *      *<p>Values do not have to be unique. However, values must be non-null.      *      *<p>If either or both endpoints are not already present in this graph, this method will      * silently {@link #addNode(Object) add} each missing endpoint to the graph.      *      * @return this {@code Builder} object      * @throws IllegalArgumentException if the introduction of the edge would violate {@link      *     #allowsSelfLoops()}      * @throws IllegalArgumentException if the endpoints are unordered and the graph is directed      */
annotation|@
name|CanIgnoreReturnValue
DECL|method|putEdgeValue (EndpointPair<N> endpoints, V value)
specifier|public
name|ImmutableValueGraph
operator|.
name|Builder
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|putEdgeValue
parameter_list|(
name|EndpointPair
argument_list|<
name|N
argument_list|>
name|endpoints
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|mutableValueGraph
operator|.
name|putEdgeValue
argument_list|(
name|endpoints
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Returns a newly-created {@code ImmutableValueGraph} based on the contents of this {@code      * Builder}.      */
DECL|method|build ()
specifier|public
name|ImmutableValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
block|{
return|return
name|ImmutableValueGraph
operator|.
name|copyOf
argument_list|(
name|mutableValueGraph
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

