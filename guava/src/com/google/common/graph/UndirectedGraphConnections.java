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
name|INNER_CAPACITY
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
name|INNER_LOAD_FACTOR
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
name|ImmutableSet
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
name|HashSet
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
comment|/**  * A class representing an origin node's adjacent nodes in an undirected graph.  *  * @author James Sexton  * @param<N> Node parameter type  */
end_comment

begin_class
DECL|class|UndirectedGraphConnections
specifier|final
class|class
name|UndirectedGraphConnections
parameter_list|<
name|N
parameter_list|>
implements|implements
name|GraphConnections
argument_list|<
name|N
argument_list|>
block|{
DECL|field|adjacentNodes
specifier|private
specifier|final
name|Set
argument_list|<
name|N
argument_list|>
name|adjacentNodes
decl_stmt|;
DECL|method|UndirectedGraphConnections (Set<N> adjacentNodes)
specifier|private
name|UndirectedGraphConnections
parameter_list|(
name|Set
argument_list|<
name|N
argument_list|>
name|adjacentNodes
parameter_list|)
block|{
name|this
operator|.
name|adjacentNodes
operator|=
name|checkNotNull
argument_list|(
name|adjacentNodes
argument_list|,
literal|"adjacentNodes"
argument_list|)
expr_stmt|;
block|}
DECL|method|of ()
specifier|static
parameter_list|<
name|N
parameter_list|>
name|UndirectedGraphConnections
argument_list|<
name|N
argument_list|>
name|of
parameter_list|()
block|{
return|return
operator|new
name|UndirectedGraphConnections
argument_list|<
name|N
argument_list|>
argument_list|(
operator|new
name|HashSet
argument_list|<
name|N
argument_list|>
argument_list|(
name|INNER_CAPACITY
argument_list|,
name|INNER_LOAD_FACTOR
argument_list|)
argument_list|)
return|;
block|}
DECL|method|ofImmutable (Set<N> adjacentNodes)
specifier|static
parameter_list|<
name|N
parameter_list|>
name|UndirectedGraphConnections
argument_list|<
name|N
argument_list|>
name|ofImmutable
parameter_list|(
name|Set
argument_list|<
name|N
argument_list|>
name|adjacentNodes
parameter_list|)
block|{
return|return
operator|new
name|UndirectedGraphConnections
argument_list|<
name|N
argument_list|>
argument_list|(
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|adjacentNodes
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
name|adjacentNodes
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|predecessors ()
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|predecessors
parameter_list|()
block|{
return|return
name|adjacentNodes
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|successors ()
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|successors
parameter_list|()
block|{
return|return
name|adjacentNodes
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|removePredecessor (Object node)
specifier|public
name|void
name|removePredecessor
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
name|removeSuccessor
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|removeSuccessor (Object node)
specifier|public
name|void
name|removeSuccessor
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
name|adjacentNodes
operator|.
name|remove
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addPredecessor (N node)
specifier|public
name|void
name|addPredecessor
parameter_list|(
name|N
name|node
parameter_list|)
block|{
name|addSuccessor
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addSuccessor (N node)
specifier|public
name|void
name|addSuccessor
parameter_list|(
name|N
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
name|adjacentNodes
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
