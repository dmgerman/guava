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
name|EXPECTED_DEGREE
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
name|BiMap
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
name|HashBiMap
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
name|ImmutableBiMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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

begin_comment
comment|/**  * An implementation of {@link NetworkConnections} for undirected networks.  *  * @author James Sexton  * @param<N> Node parameter type  * @param<E> Edge parameter type  */
end_comment

begin_class
DECL|class|UndirectedNetworkConnections
specifier|final
class|class
name|UndirectedNetworkConnections
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
extends|extends
name|AbstractUndirectedNetworkConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
block|{
DECL|method|UndirectedNetworkConnections (Map<E, N> incidentEdgeMap)
specifier|protected
name|UndirectedNetworkConnections
parameter_list|(
name|Map
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|incidentEdgeMap
parameter_list|)
block|{
name|super
argument_list|(
name|incidentEdgeMap
argument_list|)
expr_stmt|;
block|}
DECL|method|of ()
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|UndirectedNetworkConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|of
parameter_list|()
block|{
return|return
operator|new
name|UndirectedNetworkConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|(
name|HashBiMap
operator|.
expr|<
name|E
argument_list|,
name|N
operator|>
name|create
argument_list|(
name|EXPECTED_DEGREE
argument_list|)
argument_list|)
return|;
block|}
DECL|method|ofImmutable (Map<E, N> incidentEdges)
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|UndirectedNetworkConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|ofImmutable
parameter_list|(
name|Map
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|incidentEdges
parameter_list|)
block|{
return|return
operator|new
name|UndirectedNetworkConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|(
name|ImmutableBiMap
operator|.
name|copyOf
argument_list|(
name|incidentEdges
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|adjacentNodes ()
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|adjacentNodes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
operator|(
operator|(
name|BiMap
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
operator|)
name|incidentEdgeMap
operator|)
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|edgesConnecting (N node)
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|edgesConnecting
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
operator|new
name|EdgesConnecting
argument_list|<
name|E
argument_list|>
argument_list|(
operator|(
operator|(
name|BiMap
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
operator|)
name|incidentEdgeMap
operator|)
operator|.
name|inverse
argument_list|()
argument_list|,
name|node
argument_list|)
return|;
block|}
block|}
end_class

end_unit

