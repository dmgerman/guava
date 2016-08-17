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
name|NOT_AVAILABLE_ON_UNDIRECTED
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
name|Objects
import|;
end_import

begin_comment
comment|/**  * An immutable pair representing the two (possibly equal, in the case of a self-loop) endpoints  * of an edge in a graph. The {@link Endpoints} of a directed edge are an ordered pair of nodes  * (source and target). The {@link Endpoints} of an undirected edge are an unordered pair of nodes.  *  * @author James Sexton  * @since 20.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|Endpoints
specifier|public
specifier|abstract
class|class
name|Endpoints
parameter_list|<
name|N
parameter_list|>
block|{
DECL|field|nodeA
specifier|private
specifier|final
name|N
name|nodeA
decl_stmt|;
DECL|field|nodeB
specifier|private
specifier|final
name|N
name|nodeB
decl_stmt|;
DECL|method|Endpoints (N nodeA, N nodeB)
specifier|private
name|Endpoints
parameter_list|(
name|N
name|nodeA
parameter_list|,
name|N
name|nodeB
parameter_list|)
block|{
name|this
operator|.
name|nodeA
operator|=
name|checkNotNull
argument_list|(
name|nodeA
argument_list|)
expr_stmt|;
name|this
operator|.
name|nodeB
operator|=
name|checkNotNull
argument_list|(
name|nodeB
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns {@link Endpoints} representing the endpoints of an edge in {@code graph}.    */
DECL|method|of (Graph<?, ?> graph, N nodeA, N nodeB)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|>
name|Endpoints
argument_list|<
name|N
argument_list|>
name|of
parameter_list|(
name|Graph
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|graph
parameter_list|,
name|N
name|nodeA
parameter_list|,
name|N
name|nodeB
parameter_list|)
block|{
return|return
name|graph
operator|.
name|isDirected
argument_list|()
condition|?
name|ofDirected
argument_list|(
name|nodeA
argument_list|,
name|nodeB
argument_list|)
else|:
name|ofUndirected
argument_list|(
name|nodeA
argument_list|,
name|nodeB
argument_list|)
return|;
block|}
comment|/**    * Returns {@link Endpoints} representing the endpoints of an edge in {@code network}.    */
DECL|method|of (Network<?, ?> network, N nodeA, N nodeB)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|>
name|Endpoints
argument_list|<
name|N
argument_list|>
name|of
parameter_list|(
name|Network
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|network
parameter_list|,
name|N
name|nodeA
parameter_list|,
name|N
name|nodeB
parameter_list|)
block|{
return|return
name|network
operator|.
name|isDirected
argument_list|()
condition|?
name|ofDirected
argument_list|(
name|nodeA
argument_list|,
name|nodeB
argument_list|)
else|:
name|ofUndirected
argument_list|(
name|nodeA
argument_list|,
name|nodeB
argument_list|)
return|;
block|}
comment|/**    * Returns {@link Endpoints} representing the endpoints of a directed edge.    */
DECL|method|ofDirected (N source, N target)
specifier|static
parameter_list|<
name|N
parameter_list|>
name|Endpoints
operator|.
name|Directed
argument_list|<
name|N
argument_list|>
name|ofDirected
parameter_list|(
name|N
name|source
parameter_list|,
name|N
name|target
parameter_list|)
block|{
return|return
operator|new
name|Directed
argument_list|<
name|N
argument_list|>
argument_list|(
name|source
argument_list|,
name|target
argument_list|)
return|;
block|}
comment|/**    * Returns {@link Endpoints} representing the endpoints of an undirected edge.    */
DECL|method|ofUndirected (N nodeA, N nodeB)
specifier|static
parameter_list|<
name|N
parameter_list|>
name|Endpoints
operator|.
name|Undirected
argument_list|<
name|N
argument_list|>
name|ofUndirected
parameter_list|(
name|N
name|nodeA
parameter_list|,
name|N
name|nodeB
parameter_list|)
block|{
return|return
operator|new
name|Undirected
argument_list|<
name|N
argument_list|>
argument_list|(
name|nodeA
argument_list|,
name|nodeB
argument_list|)
return|;
block|}
comment|/**    * If these are the {@link Endpoints} of a directed edge, returns the node which is the source of    * that edge.    *    * @throws UnsupportedOperationException if these are the {@link Endpoints} of a undirected edge    */
DECL|method|source ()
specifier|public
specifier|abstract
name|N
name|source
parameter_list|()
function_decl|;
comment|/**    * If these are the {@link Endpoints} of a directed edge, returns the node which is the target of    * that edge.    *    * @throws UnsupportedOperationException if these are the {@link Endpoints} of a undirected edge    */
DECL|method|target ()
specifier|public
specifier|abstract
name|N
name|target
parameter_list|()
function_decl|;
comment|/**    * If these are the {@link Endpoints} of a directed edge, returns the {@link #source()};    * otherwise, returns an arbitrary (but consistent) endpoint of the origin edge.    */
DECL|method|nodeA ()
specifier|public
specifier|final
name|N
name|nodeA
parameter_list|()
block|{
return|return
name|nodeA
return|;
block|}
comment|/**    * Returns the node {@link #adjacentNode(Object) adjacent} to {@link #nodeA()} along the origin    * edge. If these are the {@link Endpoints} of a directed edge, it is equal to {@link #target()}.    */
DECL|method|nodeB ()
specifier|public
specifier|final
name|N
name|nodeB
parameter_list|()
block|{
return|return
name|nodeB
return|;
block|}
comment|/**    * Returns the node that is adjacent to {@code node} along the origin edge.    *    * @throws IllegalArgumentException if this instance does not contain {@code node}, that is, the    *     origin edge is not incident to {@code}    */
DECL|method|adjacentNode (Object node)
specifier|public
specifier|final
name|N
name|adjacentNode
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
if|if
condition|(
name|node
operator|.
name|equals
argument_list|(
name|nodeA
argument_list|)
condition|)
block|{
return|return
name|nodeB
return|;
block|}
elseif|else
if|if
condition|(
name|node
operator|.
name|equals
argument_list|(
name|nodeB
argument_list|)
condition|)
block|{
return|return
name|nodeA
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Endpoints %s does not contain node %s"
argument_list|,
name|this
argument_list|,
name|node
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|isDirected ()
specifier|abstract
name|boolean
name|isDirected
parameter_list|()
function_decl|;
comment|/**    * The {@link Endpoints} of two directed edges are equal if their {@link #source()} and    * {@link #target()} are equal. The {@link Endpoints} of two undirected edges are equal if they    * contain the same nodes. The {@link Endpoints} of a directed edge are never equal to the    * {@link Endpoints} of an undirected edge.    */
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
specifier|abstract
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
function_decl|;
comment|/**    * The hashcode of the {@link Endpoints} of a directed edge is equal to    * {@code Objects.hashCode(source(), target())}. The hashcode of the {@link Endpoints}    * of an undirected edge is equal to {@code nodeA().hashCode() ^ nodeB().hashCode()}.    */
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
specifier|abstract
name|int
name|hashCode
parameter_list|()
function_decl|;
comment|/**    * The {@link Endpoints} of a directed edge.    */
DECL|class|Directed
specifier|private
specifier|static
specifier|final
class|class
name|Directed
parameter_list|<
name|N
parameter_list|>
extends|extends
name|Endpoints
argument_list|<
name|N
argument_list|>
block|{
DECL|method|Directed (N source, N target)
specifier|private
name|Directed
parameter_list|(
name|N
name|source
parameter_list|,
name|N
name|target
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|source ()
specifier|public
name|N
name|source
parameter_list|()
block|{
return|return
name|nodeA
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|target ()
specifier|public
name|N
name|target
parameter_list|()
block|{
return|return
name|nodeB
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isDirected ()
name|boolean
name|isDirected
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|Endpoints
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Endpoints
argument_list|<
name|?
argument_list|>
name|other
init|=
operator|(
name|Endpoints
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|isDirected
argument_list|()
operator|!=
name|other
operator|.
name|isDirected
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|source
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|source
argument_list|()
argument_list|)
operator|&&
name|target
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|target
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hashCode
argument_list|(
name|source
argument_list|()
argument_list|,
name|target
argument_list|()
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
name|String
operator|.
name|format
argument_list|(
literal|"<%s -> %s>"
argument_list|,
name|source
argument_list|()
argument_list|,
name|target
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**    * The {@link Endpoints} of an undirected edge.    */
DECL|class|Undirected
specifier|private
specifier|static
specifier|final
class|class
name|Undirected
parameter_list|<
name|N
parameter_list|>
extends|extends
name|Endpoints
argument_list|<
name|N
argument_list|>
block|{
DECL|method|Undirected (N nodeA, N nodeB)
specifier|private
name|Undirected
parameter_list|(
name|N
name|nodeA
parameter_list|,
name|N
name|nodeB
parameter_list|)
block|{
name|super
argument_list|(
name|nodeA
argument_list|,
name|nodeB
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|source ()
specifier|public
name|N
name|source
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
name|NOT_AVAILABLE_ON_UNDIRECTED
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|target ()
specifier|public
name|N
name|target
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
name|NOT_AVAILABLE_ON_UNDIRECTED
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|isDirected ()
name|boolean
name|isDirected
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|Endpoints
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Endpoints
argument_list|<
name|?
argument_list|>
name|other
init|=
operator|(
name|Endpoints
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|isDirected
argument_list|()
operator|!=
name|other
operator|.
name|isDirected
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Equivalent to the following simple implementation:
comment|// boolean condition1 = nodeA().equals(other.nodeA())&& nodeB().equals(other.nodeB());
comment|// boolean condition2 = nodeA().equals(other.nodeB())&& nodeB().equals(other.nodeA());
comment|// return condition1 || condition2;
if|if
condition|(
name|nodeA
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|nodeA
argument_list|()
argument_list|)
condition|)
block|{
comment|// check condition1
comment|// Here's the tricky bit. We don't have to explicitly check for condition2 in this case.
comment|// Why? The second half of condition2 requires that nodeB equals other.nodeA.
comment|// We already know that nodeA equals other.nodeA. Combined with the earlier statement,
comment|// and the transitive property of equality, this implies that nodeA equals nodeB.
comment|// If nodeA equals nodeB, condition1 == condition2, so checking condition1 is sufficient.
return|return
name|nodeB
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|nodeB
argument_list|()
argument_list|)
return|;
block|}
return|return
name|nodeA
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|nodeB
argument_list|()
argument_list|)
operator|&&
name|nodeB
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|nodeA
argument_list|()
argument_list|)
return|;
comment|// check condition2
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|nodeA
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|^
name|nodeB
argument_list|()
operator|.
name|hashCode
argument_list|()
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
name|String
operator|.
name|format
argument_list|(
literal|"[%s, %s]"
argument_list|,
name|nodeA
argument_list|()
argument_list|,
name|nodeB
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

