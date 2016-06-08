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
comment|/**  * A builder for constructing instances of {@link Network} with user-defined properties.  *  *<p>A graph built by this class will have the following properties by default:  *<ul>  *<li>does not allow parallel edges  *<li>allows self-loops  *<li>orders {@code nodes()} and {@code edges()} in the order in which the elements were added  *</ul>  *  * @author James Sexton  * @author Joshua O'Madadhain  * @since 20.0  */
end_comment

begin_comment
comment|// TODO(user): try creating an abstract superclass that this and GraphBuilder could derive from.
end_comment

begin_class
annotation|@
name|Beta
DECL|class|NetworkBuilder
specifier|public
specifier|final
class|class
name|NetworkBuilder
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
block|{
DECL|field|directed
specifier|final
name|boolean
name|directed
decl_stmt|;
DECL|field|allowsParallelEdges
name|boolean
name|allowsParallelEdges
init|=
literal|false
decl_stmt|;
DECL|field|allowsSelfLoops
name|boolean
name|allowsSelfLoops
init|=
literal|true
decl_stmt|;
DECL|field|nodeOrder
name|ElementOrder
argument_list|<
name|?
super|super
name|N
argument_list|>
name|nodeOrder
init|=
name|ElementOrder
operator|.
name|insertion
argument_list|()
decl_stmt|;
DECL|field|edgeOrder
name|ElementOrder
argument_list|<
name|?
super|super
name|E
argument_list|>
name|edgeOrder
init|=
name|ElementOrder
operator|.
name|insertion
argument_list|()
decl_stmt|;
DECL|field|expectedNodeCount
name|Optional
argument_list|<
name|Integer
argument_list|>
name|expectedNodeCount
init|=
name|Optional
operator|.
name|absent
argument_list|()
decl_stmt|;
DECL|field|expectedEdgeCount
name|Optional
argument_list|<
name|Integer
argument_list|>
name|expectedEdgeCount
init|=
name|Optional
operator|.
name|absent
argument_list|()
decl_stmt|;
comment|/**    * Creates a new instance with the specified edge directionality.    *    * @param directed if true, creates an instance for graphs whose edges are each directed;    *      if false, creates an instance for graphs whose edges are each undirected.    */
DECL|method|NetworkBuilder (boolean directed)
specifier|private
name|NetworkBuilder
parameter_list|(
name|boolean
name|directed
parameter_list|)
block|{
name|this
operator|.
name|directed
operator|=
name|directed
expr_stmt|;
block|}
comment|/**    * Returns a {@link NetworkBuilder} for building directed graphs.    */
DECL|method|directed ()
specifier|public
specifier|static
name|NetworkBuilder
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
name|NetworkBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|true
argument_list|)
return|;
block|}
comment|/**    * Returns a {@link NetworkBuilder} for building undirected graphs.    */
DECL|method|undirected ()
specifier|public
specifier|static
name|NetworkBuilder
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
name|NetworkBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|false
argument_list|)
return|;
block|}
comment|/**    * Returns a {@link NetworkBuilder} initialized with all properties queryable from {@code graph}.    *    *<p>The "queryable" properties are those that are exposed through the {@link Network} interface,    * such as {@link Network#isDirected()}. Other properties, such as    * {@link #expectedNodeCount(int)}, are not set in the new builder.    */
DECL|method|from (Network<N, E> graph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|NetworkBuilder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|from
parameter_list|(
name|Network
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|graph
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|graph
argument_list|)
expr_stmt|;
return|return
operator|new
name|NetworkBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|(
name|graph
operator|.
name|isDirected
argument_list|()
argument_list|)
operator|.
name|allowsParallelEdges
argument_list|(
name|graph
operator|.
name|allowsParallelEdges
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
name|orderNodes
argument_list|(
name|graph
operator|.
name|nodeOrder
argument_list|()
argument_list|)
operator|.
name|orderEdges
argument_list|(
name|graph
operator|.
name|edgeOrder
argument_list|()
argument_list|)
operator|.
name|cast
argument_list|()
return|;
block|}
comment|/**    * Specifies whether the graph will allow parallel edges. Attempting to add a parallel edge to    * a graph that does not allow them will throw an {@link UnsupportedOperationException}.    */
DECL|method|allowsParallelEdges (boolean allowsParallelEdges)
specifier|public
name|NetworkBuilder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|allowsParallelEdges
parameter_list|(
name|boolean
name|allowsParallelEdges
parameter_list|)
block|{
name|this
operator|.
name|allowsParallelEdges
operator|=
name|allowsParallelEdges
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Specifies whether the graph will allow self-loops (edges that connect a node to itself).    * Attempting to add a self-loop to a graph that does not allow them will throw an    * {@link UnsupportedOperationException}.    */
DECL|method|allowsSelfLoops (boolean allowsSelfLoops)
specifier|public
name|NetworkBuilder
argument_list|<
name|N
argument_list|,
name|E
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
name|NetworkBuilder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|expectedNodeCount
parameter_list|(
name|int
name|expectedNodeCount
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|expectedNodeCount
operator|>=
literal|0
argument_list|,
literal|"The expected number of nodes can't be negative: %s"
argument_list|,
name|expectedNodeCount
argument_list|)
expr_stmt|;
name|this
operator|.
name|expectedNodeCount
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|expectedNodeCount
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Specifies the expected number of edges in the graph.    *    * @throws IllegalArgumentException if {@code expectedEdgeCount} is negative    */
DECL|method|expectedEdgeCount (int expectedEdgeCount)
specifier|public
name|NetworkBuilder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|expectedEdgeCount
parameter_list|(
name|int
name|expectedEdgeCount
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|expectedEdgeCount
operator|>=
literal|0
argument_list|,
literal|"The expected number of edges can't be negative: %s"
argument_list|,
name|expectedEdgeCount
argument_list|)
expr_stmt|;
name|this
operator|.
name|expectedEdgeCount
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|expectedEdgeCount
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Specifies the order of iteration for the elements of {@link Network#nodes()}.    */
DECL|method|orderNodes (ElementOrder<N1> nodeOrder)
specifier|public
parameter_list|<
name|N1
extends|extends
name|N
parameter_list|>
name|NetworkBuilder
argument_list|<
name|N1
argument_list|,
name|E
argument_list|>
name|orderNodes
parameter_list|(
name|ElementOrder
argument_list|<
name|N1
argument_list|>
name|nodeOrder
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|nodeOrder
argument_list|)
expr_stmt|;
name|NetworkBuilder
argument_list|<
name|N1
argument_list|,
name|E
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
name|nodeOrder
expr_stmt|;
return|return
name|newBuilder
return|;
block|}
comment|/**    * Specifies the order of iteration for the elements of {@link Network#edges()}.    */
DECL|method|orderEdges (ElementOrder<E1> edgeOrder)
specifier|public
parameter_list|<
name|E1
extends|extends
name|E
parameter_list|>
name|NetworkBuilder
argument_list|<
name|N
argument_list|,
name|E1
argument_list|>
name|orderEdges
parameter_list|(
name|ElementOrder
argument_list|<
name|E1
argument_list|>
name|edgeOrder
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|edgeOrder
argument_list|)
expr_stmt|;
name|NetworkBuilder
argument_list|<
name|N
argument_list|,
name|E1
argument_list|>
name|newBuilder
init|=
name|cast
argument_list|()
decl_stmt|;
name|newBuilder
operator|.
name|edgeOrder
operator|=
name|edgeOrder
expr_stmt|;
return|return
name|newBuilder
return|;
block|}
comment|/**    * Returns an empty {@link MutableNetwork} with the properties of this {@link NetworkBuilder}.    */
DECL|method|build ()
specifier|public
parameter_list|<
name|N1
extends|extends
name|N
parameter_list|,
name|E1
extends|extends
name|E
parameter_list|>
name|MutableNetwork
argument_list|<
name|N1
argument_list|,
name|E1
argument_list|>
name|build
parameter_list|()
block|{
return|return
operator|new
name|ConfigurableMutableNetwork
argument_list|<
name|N1
argument_list|,
name|E1
argument_list|>
argument_list|(
name|this
argument_list|)
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
name|E1
extends|extends
name|E
parameter_list|>
name|NetworkBuilder
argument_list|<
name|N1
argument_list|,
name|E1
argument_list|>
name|cast
parameter_list|()
block|{
return|return
operator|(
name|NetworkBuilder
argument_list|<
name|N1
argument_list|,
name|E1
argument_list|>
operator|)
name|this
return|;
block|}
block|}
end_class

end_unit

