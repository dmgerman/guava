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
name|Immutable
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
comment|/**  * A {@link ValueGraph} whose elements and structural relationships will never change. Instances of  * this class may be obtained with {@link #copyOf(ValueGraph)}.  *  *<p>See the Guava User's Guide's<a  * href="https://github.com/google/guava/wiki/GraphsExplained#immutable-implementations">discussion  * of the {@code Immutable*} types</a> for more information on the properties and guarantees  * provided by this class.  *  * @author James Sexton  * @param<N> Node parameter type  * @param<V> Value parameter type  * @since 20.0  */
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
name|ImmutableGraph
operator|.
name|ValueBackedImpl
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
implements|implements
name|ValueGraph
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
name|edgeValue
argument_list|(
name|node
argument_list|,
name|successorNode
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
name|graph
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
argument_list|,
name|Maps
operator|.
name|asMap
argument_list|(
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
argument_list|,
name|successorNodeToValueFn
argument_list|)
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
annotation|@
name|Override
DECL|method|edgeValue (Object nodeU, Object nodeV)
specifier|public
name|V
name|edgeValue
parameter_list|(
name|Object
name|nodeU
parameter_list|,
name|Object
name|nodeV
parameter_list|)
block|{
return|return
name|backingValueGraph
operator|.
name|edgeValue
argument_list|(
name|nodeU
argument_list|,
name|nodeV
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|edgeValueOrDefault (Object nodeU, Object nodeV, @Nullable V defaultValue)
specifier|public
name|V
name|edgeValueOrDefault
parameter_list|(
name|Object
name|nodeU
parameter_list|,
name|Object
name|nodeV
parameter_list|,
annotation|@
name|Nullable
name|V
name|defaultValue
parameter_list|)
block|{
return|return
name|backingValueGraph
operator|.
name|edgeValueOrDefault
argument_list|(
name|nodeU
argument_list|,
name|nodeV
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|backingValueGraph
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

