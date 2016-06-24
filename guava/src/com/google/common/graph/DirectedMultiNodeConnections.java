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
name|base
operator|.
name|Predicate
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
name|HashMultiset
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
name|collect
operator|.
name|Multiset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|Reference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|SoftReference
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
name|Map
operator|.
name|Entry
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
comment|/**  * An implementation of {@link NodeConnections} for directed networks with parallel edges.  *  * @author James Sexton  * @param<N> Node parameter type  * @param<E> Edge parameter type  */
end_comment

begin_class
DECL|class|DirectedMultiNodeConnections
specifier|final
class|class
name|DirectedMultiNodeConnections
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
extends|extends
name|AbstractDirectedNodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
block|{
DECL|method|DirectedMultiNodeConnections (Map<E, N> inEdges, Map<E, N> outEdges, int selfLoopCount)
specifier|private
name|DirectedMultiNodeConnections
parameter_list|(
name|Map
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|inEdges
parameter_list|,
name|Map
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|outEdges
parameter_list|,
name|int
name|selfLoopCount
parameter_list|)
block|{
name|super
argument_list|(
name|inEdges
argument_list|,
name|outEdges
argument_list|,
name|selfLoopCount
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
name|DirectedMultiNodeConnections
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
name|DirectedMultiNodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|(
name|Maps
operator|.
expr|<
name|E
argument_list|,
name|N
operator|>
name|newHashMapWithExpectedSize
argument_list|(
name|EXPECTED_DEGREE
argument_list|)
argument_list|,
name|Maps
operator|.
expr|<
name|E
argument_list|,
name|N
operator|>
name|newHashMapWithExpectedSize
argument_list|(
name|EXPECTED_DEGREE
argument_list|)
argument_list|,
literal|0
argument_list|)
return|;
block|}
DECL|method|ofImmutable ( Map<E, N> inEdges, Map<E, N> outEdges, int selfLoopCount)
specifier|static
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
name|DirectedMultiNodeConnections
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
name|inEdges
parameter_list|,
name|Map
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|outEdges
parameter_list|,
name|int
name|selfLoopCount
parameter_list|)
block|{
return|return
operator|new
name|DirectedMultiNodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
argument_list|(
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|inEdges
argument_list|)
argument_list|,
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|outEdges
argument_list|)
argument_list|,
name|selfLoopCount
argument_list|)
return|;
block|}
DECL|field|predecessorsReference
specifier|private
specifier|transient
name|Reference
argument_list|<
name|Multiset
argument_list|<
name|N
argument_list|>
argument_list|>
name|predecessorsReference
decl_stmt|;
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
name|Multiset
argument_list|<
name|N
argument_list|>
name|predecessors
init|=
name|getReference
argument_list|(
name|predecessorsReference
argument_list|)
decl_stmt|;
if|if
condition|(
name|predecessors
operator|==
literal|null
condition|)
block|{
name|predecessors
operator|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|inEdgeMap
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|predecessorsReference
operator|=
operator|new
name|SoftReference
argument_list|<
name|Multiset
argument_list|<
name|N
argument_list|>
argument_list|>
argument_list|(
name|predecessors
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|predecessors
operator|.
name|elementSet
argument_list|()
argument_list|)
return|;
block|}
DECL|field|successorsReference
specifier|private
specifier|transient
name|Reference
argument_list|<
name|Multiset
argument_list|<
name|N
argument_list|>
argument_list|>
name|successorsReference
decl_stmt|;
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
name|Multiset
argument_list|<
name|N
argument_list|>
name|successors
init|=
name|getReference
argument_list|(
name|successorsReference
argument_list|)
decl_stmt|;
if|if
condition|(
name|successors
operator|==
literal|null
condition|)
block|{
name|successors
operator|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|outEdgeMap
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|successorsReference
operator|=
operator|new
name|SoftReference
argument_list|<
name|Multiset
argument_list|<
name|N
argument_list|>
argument_list|>
argument_list|(
name|successors
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|successors
operator|.
name|elementSet
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|edgesConnecting (final Object node)
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|edgesConnecting
parameter_list|(
specifier|final
name|Object
name|node
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|Maps
operator|.
name|filterEntries
argument_list|(
name|outEdgeMap
argument_list|,
operator|new
name|Predicate
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|Entry
argument_list|<
name|E
argument_list|,
name|N
argument_list|>
name|entry
parameter_list|)
block|{
return|return
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|node
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|removeInEdge (Object edge, boolean isSelfLoop)
specifier|public
name|N
name|removeInEdge
parameter_list|(
name|Object
name|edge
parameter_list|,
name|boolean
name|isSelfLoop
parameter_list|)
block|{
name|N
name|node
init|=
name|super
operator|.
name|removeInEdge
argument_list|(
name|edge
argument_list|,
name|isSelfLoop
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|Multiset
argument_list|<
name|N
argument_list|>
name|predecessors
init|=
name|getReference
argument_list|(
name|predecessorsReference
argument_list|)
decl_stmt|;
if|if
condition|(
name|predecessors
operator|!=
literal|null
condition|)
block|{
name|checkState
argument_list|(
name|predecessors
operator|.
name|remove
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|node
return|;
block|}
annotation|@
name|Override
DECL|method|removeOutEdge (Object edge)
specifier|public
name|N
name|removeOutEdge
parameter_list|(
name|Object
name|edge
parameter_list|)
block|{
name|N
name|node
init|=
name|super
operator|.
name|removeOutEdge
argument_list|(
name|edge
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|Multiset
argument_list|<
name|N
argument_list|>
name|successors
init|=
name|getReference
argument_list|(
name|successorsReference
argument_list|)
decl_stmt|;
if|if
condition|(
name|successors
operator|!=
literal|null
condition|)
block|{
name|checkState
argument_list|(
name|successors
operator|.
name|remove
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|node
return|;
block|}
annotation|@
name|Override
DECL|method|addInEdge (E edge, N node, boolean isSelfLoop)
specifier|public
name|boolean
name|addInEdge
parameter_list|(
name|E
name|edge
parameter_list|,
name|N
name|node
parameter_list|,
name|boolean
name|isSelfLoop
parameter_list|)
block|{
if|if
condition|(
name|super
operator|.
name|addInEdge
argument_list|(
name|edge
argument_list|,
name|node
argument_list|,
name|isSelfLoop
argument_list|)
condition|)
block|{
name|Multiset
argument_list|<
name|N
argument_list|>
name|predecessors
init|=
name|getReference
argument_list|(
name|predecessorsReference
argument_list|)
decl_stmt|;
if|if
condition|(
name|predecessors
operator|!=
literal|null
condition|)
block|{
name|checkState
argument_list|(
name|predecessors
operator|.
name|add
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|addOutEdge (E edge, N node)
specifier|public
name|boolean
name|addOutEdge
parameter_list|(
name|E
name|edge
parameter_list|,
name|N
name|node
parameter_list|)
block|{
if|if
condition|(
name|super
operator|.
name|addOutEdge
argument_list|(
name|edge
argument_list|,
name|node
argument_list|)
condition|)
block|{
name|Multiset
argument_list|<
name|N
argument_list|>
name|successors
init|=
name|getReference
argument_list|(
name|successorsReference
argument_list|)
decl_stmt|;
if|if
condition|(
name|successors
operator|!=
literal|null
condition|)
block|{
name|checkState
argument_list|(
name|successors
operator|.
name|add
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|getReference (@ullable Reference<T> reference)
annotation|@
name|Nullable
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getReference
parameter_list|(
annotation|@
name|Nullable
name|Reference
argument_list|<
name|T
argument_list|>
name|reference
parameter_list|)
block|{
if|if
condition|(
name|reference
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|reference
operator|.
name|get
argument_list|()
return|;
comment|// can be null
block|}
block|}
end_class

end_unit

