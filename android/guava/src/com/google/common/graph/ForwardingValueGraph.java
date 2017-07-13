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
comment|/**  * A class to allow {@link ValueGraph} implementations to be backed by a provided delegate. This is  * not currently planned to be released as a general-purpose forwarding class.  *  * @author James Sexton  * @author Joshua O'Madadhain  */
end_comment

begin_class
DECL|class|ForwardingValueGraph
specifier|abstract
class|class
name|ForwardingValueGraph
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
block|{
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|ValueGraph
argument_list|<
name|N
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|()
function_decl|;
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
name|delegate
argument_list|()
operator|.
name|nodes
argument_list|()
return|;
block|}
comment|/**    * Defer to {@link AbstractValueGraph#edges()} (based on {@link #successors(Object)}) for full    * edges() implementation.    */
annotation|@
name|Override
DECL|method|edgeCount ()
specifier|protected
name|long
name|edgeCount
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|edges
argument_list|()
operator|.
name|size
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
name|delegate
argument_list|()
operator|.
name|isDirected
argument_list|()
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
name|delegate
argument_list|()
operator|.
name|allowsSelfLoops
argument_list|()
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
name|delegate
argument_list|()
operator|.
name|nodeOrder
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|adjacentNodes (N node)
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|adjacentNodes
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|predecessors (N node)
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|predecessors
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|successors (N node)
specifier|public
name|Set
argument_list|<
name|N
argument_list|>
name|successors
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|successors
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|degree (N node)
specifier|public
name|int
name|degree
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|degree
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|inDegree (N node)
specifier|public
name|int
name|inDegree
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|inDegree
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|outDegree (N node)
specifier|public
name|int
name|outDegree
parameter_list|(
name|N
name|node
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|outDegree
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hasEdgeConnecting (N nodeU, N nodeV)
specifier|public
name|boolean
name|hasEdgeConnecting
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|hasEdgeConnecting
argument_list|(
name|nodeU
argument_list|,
name|nodeV
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|edgeValueOrDefault (N nodeU, N nodeV, @Nullable V defaultValue)
specifier|public
name|V
name|edgeValueOrDefault
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|,
annotation|@
name|Nullable
name|V
name|defaultValue
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
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
block|}
end_class

end_unit

