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
name|Graphs
operator|.
name|checkNonNegative
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
name|Optional
import|;
end_import

begin_comment
comment|/**  * A builder for constructing instances of {@link MutableValueGraph} or {@link ImmutableValueGraph}  * with user-defined properties.  *  *<p>A graph built by this class will have the following properties by default:  *  *<ul>  *<li>does not allow self-loops  *<li>orders {@link Graph#nodes()} in the order in which the elements were added  *</ul>  *  *<p>Examples of use:  *  *<pre>{@code  * // Building a mutable value graph  * MutableValueGraph<String, Double> graph =  *     ValueGraphBuilder.undirected().allowsSelfLoops(true).build();  * graph.putEdgeValue("San Francisco", "San Francisco", 0.0);  * graph.putEdgeValue("San Jose", "San Jose", 0.0);  * graph.putEdgeValue("San Francisco", "San Jose", 48.4);  *  * // Building an immutable value graph  * ImmutableValueGraph<String, Double> immutableGraph =  *     ValueGraphBuilder.undirected()  *         .allowsSelfLoops(true)  *         .<String, Double>immutable()  *         .putEdgeValue("San Francisco", "San Francisco", 0.0)  *         .putEdgeValue("San Jose", "San Jose", 0.0)  *         .putEdgeValue("San Francisco", "San Jose", 48.4)  *         .build();  * }</pre>  *  * @author James Sexton  * @author Joshua O'Madadhain  * @param<N> The most general node type this builder will support. This is normally {@code Object}  *     unless it is constrained by using a method like {@link #nodeOrder}, or the builder is  *     constructed based on an existing {@code ValueGraph} using {@link #from(ValueGraph)}.  * @param<V> The most general value type this builder will support. This is normally {@code Object}  *     unless the builder is constructed based on an existing {@code Graph} using {@link  *     #from(ValueGraph)}.  * @since 20.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|ValueGraphBuilder
specifier|public
specifier|final
class|class
name|ValueGraphBuilder
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractGraphBuilder
argument_list|<
name|N
argument_list|>
block|{
comment|/** Creates a new instance with the specified edge directionality. */
DECL|method|ValueGraphBuilder (boolean directed)
specifier|private
name|ValueGraphBuilder
parameter_list|(
name|boolean
name|directed
parameter_list|)
block|{
name|super
argument_list|(
name|directed
argument_list|)
expr_stmt|;
block|}
comment|/** Returns a {@link ValueGraphBuilder} for building directed graphs. */
DECL|method|directed ()
specifier|public
specifier|static
name|ValueGraphBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|directed
parameter_list|()
block|{
return|return
operator|new
name|ValueGraphBuilder
argument_list|<>
argument_list|(
literal|true
argument_list|)
return|;
block|}
comment|/** Returns a {@link ValueGraphBuilder} for building undirected graphs. */
DECL|method|undirected ()
specifier|public
specifier|static
name|ValueGraphBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|undirected
parameter_list|()
block|{
return|return
operator|new
name|ValueGraphBuilder
argument_list|<>
argument_list|(
literal|false
argument_list|)
return|;
block|}
comment|/**    * Returns a {@link ValueGraphBuilder} initialized with all properties queryable from {@code    * graph}.    *    *<p>The "queryable" properties are those that are exposed through the {@link ValueGraph}    * interface, such as {@link ValueGraph#isDirected()}. Other properties, such as {@link    * #expectedNodeCount(int)}, are not set in the new builder.    */
DECL|method|from (ValueGraph<N, V> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
name|ValueGraphBuilder
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|from
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
operator|new
name|ValueGraphBuilder
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
argument_list|(
name|graph
operator|.
name|isDirected
argument_list|()
argument_list|)
operator|.
name|allowsSelfLoops
argument_list|(
name|graph
operator|.
name|allowsSelfLoops
argument_list|()
argument_list|)
operator|.
name|nodeOrder
argument_list|(
name|graph
operator|.
name|nodeOrder
argument_list|()
argument_list|)
return|;
comment|// TODO(b/142723300): Add incidentEdgeOrder
block|}
comment|/**    * Returns an {@link ImmutableValueGraph.Builder} with the properties of this {@link    * ValueGraphBuilder}.    *    *<p>The returned builder can be used for populating an {@link ImmutableValueGraph}.    *    *<p>Note that the returned builder will always have {@link #incidentEdgeOrder} set to {@link    * ElementOrder#stable()}, regardless of the value that was set in this builder.    *    * @since 28.0    */
DECL|method|immutable ()
specifier|public
parameter_list|<
name|N1
extends|extends
name|N
parameter_list|,
name|V1
extends|extends
name|V
parameter_list|>
name|ImmutableValueGraph
operator|.
name|Builder
argument_list|<
name|N1
argument_list|,
name|V1
argument_list|>
name|immutable
parameter_list|()
block|{
name|ValueGraphBuilder
argument_list|<
name|N1
argument_list|,
name|V1
argument_list|>
name|castBuilder
init|=
name|cast
argument_list|()
decl_stmt|;
return|return
operator|new
name|ImmutableValueGraph
operator|.
name|Builder
argument_list|<>
argument_list|(
name|castBuilder
argument_list|)
return|;
block|}
comment|/**    * Specifies whether the graph will allow self-loops (edges that connect a node to itself).    * Attempting to add a self-loop to a graph that does not allow them will throw an {@link    * UnsupportedOperationException}.    *    *<p>The default value is {@code false}.    */
DECL|method|allowsSelfLoops (boolean allowsSelfLoops)
specifier|public
name|ValueGraphBuilder
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|allowsSelfLoops
parameter_list|(
name|boolean
name|allowsSelfLoops
parameter_list|)
block|{
name|this
operator|.
name|allowsSelfLoops
operator|=
name|allowsSelfLoops
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Specifies the expected number of nodes in the graph.    *    * @throws IllegalArgumentException if {@code expectedNodeCount} is negative    */
DECL|method|expectedNodeCount (int expectedNodeCount)
specifier|public
name|ValueGraphBuilder
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|expectedNodeCount
parameter_list|(
name|int
name|expectedNodeCount
parameter_list|)
block|{
name|this
operator|.
name|expectedNodeCount
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|checkNonNegative
argument_list|(
name|expectedNodeCount
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Specifies the order of iteration for the elements of {@link Graph#nodes()}.    *    *<p>The default value is {@link ElementOrder#insertion() insertion order}.    */
DECL|method|nodeOrder (ElementOrder<N1> nodeOrder)
specifier|public
parameter_list|<
name|N1
extends|extends
name|N
parameter_list|>
name|ValueGraphBuilder
argument_list|<
name|N1
argument_list|,
name|V
argument_list|>
name|nodeOrder
parameter_list|(
name|ElementOrder
argument_list|<
name|N1
argument_list|>
name|nodeOrder
parameter_list|)
block|{
name|ValueGraphBuilder
argument_list|<
name|N1
argument_list|,
name|V
argument_list|>
name|newBuilder
init|=
name|cast
argument_list|()
decl_stmt|;
name|newBuilder
operator|.
name|nodeOrder
operator|=
name|checkNotNull
argument_list|(
name|nodeOrder
argument_list|)
expr_stmt|;
return|return
name|newBuilder
return|;
block|}
comment|/**    * Specifies the order of iteration for the elements of {@link ValueGraph#edges()}, {@link    * ValueGraph#adjacentNodes(Object)}, {@link ValueGraph#predecessors(Object)}, {@link    * ValueGraph#successors(Object)} and {@link ValueGraph#incidentEdges(Object)}.    *    *<p>The default value is {@link ElementOrder#unordered() unordered} for mutable graphs. For    * immutable graphs, this value is ignored; they always have a {@link ElementOrder#stable()    * stable} order.    *    * @throws IllegalArgumentException if {@code incidentEdgeOrder} is not either {@code    *     ElementOrder.unordered()} or {@code ElementOrder.stable()}.    */
comment|// TODO(b/142723300): Make this method public
DECL|method|incidentEdgeOrder (ElementOrder<N1> incidentEdgeOrder)
parameter_list|<
name|N1
extends|extends
name|N
parameter_list|>
name|ValueGraphBuilder
argument_list|<
name|N1
argument_list|,
name|V
argument_list|>
name|incidentEdgeOrder
parameter_list|(
name|ElementOrder
argument_list|<
name|N1
argument_list|>
name|incidentEdgeOrder
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|incidentEdgeOrder
operator|.
name|type
argument_list|()
operator|==
name|ElementOrder
operator|.
name|Type
operator|.
name|UNORDERED
operator|||
name|incidentEdgeOrder
operator|.
name|type
argument_list|()
operator|==
name|ElementOrder
operator|.
name|Type
operator|.
name|STABLE
argument_list|,
literal|"The given elementOrder (%s) is unsupported. incidentEdgeOrder() only supports"
operator|+
literal|" ElementOrder.unordered() and ElementOrder.stable()."
argument_list|,
name|incidentEdgeOrder
argument_list|)
expr_stmt|;
name|ValueGraphBuilder
argument_list|<
name|N1
argument_list|,
name|V
argument_list|>
name|newBuilder
init|=
name|cast
argument_list|()
decl_stmt|;
name|newBuilder
operator|.
name|incidentEdgeOrder
operator|=
name|checkNotNull
argument_list|(
name|incidentEdgeOrder
argument_list|)
expr_stmt|;
return|return
name|newBuilder
return|;
block|}
comment|/**    * Returns an empty {@link MutableValueGraph} with the properties of this {@link    * ValueGraphBuilder}.    */
DECL|method|build ()
specifier|public
parameter_list|<
name|N1
extends|extends
name|N
parameter_list|,
name|V1
extends|extends
name|V
parameter_list|>
name|MutableValueGraph
argument_list|<
name|N1
argument_list|,
name|V1
argument_list|>
name|build
parameter_list|()
block|{
return|return
operator|new
name|ConfigurableMutableValueGraph
argument_list|<>
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|copy ()
name|ValueGraphBuilder
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|copy
parameter_list|()
block|{
name|ValueGraphBuilder
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|newBuilder
init|=
operator|new
name|ValueGraphBuilder
argument_list|<>
argument_list|(
name|directed
argument_list|)
decl_stmt|;
name|newBuilder
operator|.
name|allowsSelfLoops
operator|=
name|allowsSelfLoops
expr_stmt|;
name|newBuilder
operator|.
name|nodeOrder
operator|=
name|nodeOrder
expr_stmt|;
name|newBuilder
operator|.
name|expectedNodeCount
operator|=
name|expectedNodeCount
expr_stmt|;
name|newBuilder
operator|.
name|incidentEdgeOrder
operator|=
name|incidentEdgeOrder
expr_stmt|;
return|return
name|newBuilder
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|cast ()
specifier|private
parameter_list|<
name|N1
extends|extends
name|N
parameter_list|,
name|V1
extends|extends
name|V
parameter_list|>
name|ValueGraphBuilder
argument_list|<
name|N1
argument_list|,
name|V1
argument_list|>
name|cast
parameter_list|()
block|{
return|return
operator|(
name|ValueGraphBuilder
argument_list|<
name|N1
argument_list|,
name|V1
argument_list|>
operator|)
name|this
return|;
block|}
block|}
end_class

end_unit

