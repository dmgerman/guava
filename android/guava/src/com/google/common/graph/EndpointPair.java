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
name|Iterators
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
name|UnmodifiableIterator
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
name|CheckForNull
import|;
end_import

begin_comment
comment|/**  * An immutable pair representing the two endpoints of an edge in a graph. The {@link EndpointPair}  * of a directed edge is an ordered pair of nodes ({@link #source()} and {@link #target()}). The  * {@link EndpointPair} of an undirected edge is an unordered pair of nodes ({@link #nodeU()} and  * {@link #nodeV()}).  *  *<p>The edge is a self-loop if, and only if, the two endpoints are equal.  *  * @author James Sexton  * @since 20.0  */
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
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|EndpointPair
specifier|public
specifier|abstract
class|class
name|EndpointPair
parameter_list|<
name|N
parameter_list|>
implements|implements
name|Iterable
argument_list|<
name|N
argument_list|>
block|{
DECL|field|nodeU
specifier|private
specifier|final
name|N
name|nodeU
decl_stmt|;
DECL|field|nodeV
specifier|private
specifier|final
name|N
name|nodeV
decl_stmt|;
DECL|method|EndpointPair (N nodeU, N nodeV)
specifier|private
name|EndpointPair
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
block|{
name|this
operator|.
name|nodeU
operator|=
name|checkNotNull
argument_list|(
name|nodeU
argument_list|)
expr_stmt|;
name|this
operator|.
name|nodeV
operator|=
name|checkNotNull
argument_list|(
name|nodeV
argument_list|)
expr_stmt|;
block|}
comment|/** Returns an {@link EndpointPair} representing the endpoints of a directed edge. */
DECL|method|ordered (N source, N target)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|>
name|EndpointPair
argument_list|<
name|N
argument_list|>
name|ordered
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
name|Ordered
argument_list|<>
argument_list|(
name|source
argument_list|,
name|target
argument_list|)
return|;
block|}
comment|/** Returns an {@link EndpointPair} representing the endpoints of an undirected edge. */
DECL|method|unordered (N nodeU, N nodeV)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|>
name|EndpointPair
argument_list|<
name|N
argument_list|>
name|unordered
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
block|{
comment|// Swap nodes on purpose to prevent callers from relying on the "ordering" of an unordered pair.
return|return
operator|new
name|Unordered
argument_list|<>
argument_list|(
name|nodeV
argument_list|,
name|nodeU
argument_list|)
return|;
block|}
comment|/** Returns an {@link EndpointPair} representing the endpoints of an edge in {@code graph}. */
DECL|method|of (Graph<?> graph, N nodeU, N nodeV)
specifier|static
parameter_list|<
name|N
parameter_list|>
name|EndpointPair
argument_list|<
name|N
argument_list|>
name|of
parameter_list|(
name|Graph
argument_list|<
name|?
argument_list|>
name|graph
parameter_list|,
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
block|{
return|return
name|graph
operator|.
name|isDirected
argument_list|()
condition|?
name|ordered
argument_list|(
name|nodeU
argument_list|,
name|nodeV
argument_list|)
else|:
name|unordered
argument_list|(
name|nodeU
argument_list|,
name|nodeV
argument_list|)
return|;
block|}
comment|/** Returns an {@link EndpointPair} representing the endpoints of an edge in {@code network}. */
DECL|method|of (Network<?, ?> network, N nodeU, N nodeV)
specifier|static
parameter_list|<
name|N
parameter_list|>
name|EndpointPair
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
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
block|{
return|return
name|network
operator|.
name|isDirected
argument_list|()
condition|?
name|ordered
argument_list|(
name|nodeU
argument_list|,
name|nodeV
argument_list|)
else|:
name|unordered
argument_list|(
name|nodeU
argument_list|,
name|nodeV
argument_list|)
return|;
block|}
comment|/**    * If this {@link EndpointPair} {@link #isOrdered()}, returns the node which is the source.    *    * @throws UnsupportedOperationException if this {@link EndpointPair} is not ordered    */
DECL|method|source ()
specifier|public
specifier|abstract
name|N
name|source
parameter_list|()
function_decl|;
comment|/**    * If this {@link EndpointPair} {@link #isOrdered()}, returns the node which is the target.    *    * @throws UnsupportedOperationException if this {@link EndpointPair} is not ordered    */
DECL|method|target ()
specifier|public
specifier|abstract
name|N
name|target
parameter_list|()
function_decl|;
comment|/**    * If this {@link EndpointPair} {@link #isOrdered()} returns the {@link #source()}; otherwise,    * returns an arbitrary (but consistent) endpoint of the origin edge.    */
DECL|method|nodeU ()
specifier|public
specifier|final
name|N
name|nodeU
parameter_list|()
block|{
return|return
name|nodeU
return|;
block|}
comment|/**    * Returns the node {@link #adjacentNode(Object) adjacent} to {@link #nodeU()} along the origin    * edge. If this {@link EndpointPair} {@link #isOrdered()}, this is equal to {@link #target()}.    */
DECL|method|nodeV ()
specifier|public
specifier|final
name|N
name|nodeV
parameter_list|()
block|{
return|return
name|nodeV
return|;
block|}
comment|/**    * Returns the node that is adjacent to {@code node} along the origin edge.    *    * @throws IllegalArgumentException if this {@link EndpointPair} does not contain {@code node}    * @since 20.0 (but the argument type was changed from {@code Object} to {@code N} in 31.0)    */
DECL|method|adjacentNode (N node)
specifier|public
specifier|final
name|N
name|adjacentNode
parameter_list|(
name|N
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|.
name|equals
argument_list|(
name|nodeU
argument_list|)
condition|)
block|{
return|return
name|nodeV
return|;
block|}
elseif|else
if|if
condition|(
name|node
operator|.
name|equals
argument_list|(
name|nodeV
argument_list|)
condition|)
block|{
return|return
name|nodeU
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"EndpointPair "
operator|+
name|this
operator|+
literal|" does not contain node "
operator|+
name|node
argument_list|)
throw|;
block|}
block|}
comment|/**    * Returns {@code true} if this {@link EndpointPair} is an ordered pair (i.e. represents the    * endpoints of a directed edge).    */
DECL|method|isOrdered ()
specifier|public
specifier|abstract
name|boolean
name|isOrdered
parameter_list|()
function_decl|;
comment|/** Iterates in the order {@link #nodeU()}, {@link #nodeV()}. */
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
specifier|final
name|UnmodifiableIterator
argument_list|<
name|N
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|forArray
argument_list|(
name|nodeU
argument_list|,
name|nodeV
argument_list|)
return|;
block|}
comment|/**    * Two ordered {@link EndpointPair}s are equal if their {@link #source()} and {@link #target()}    * are equal. Two unordered {@link EndpointPair}s are equal if they contain the same nodes. An    * ordered {@link EndpointPair} is never equal to an unordered {@link EndpointPair}.    */
annotation|@
name|Override
DECL|method|equals (@heckForNull Object obj)
specifier|public
specifier|abstract
name|boolean
name|equals
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|obj
parameter_list|)
function_decl|;
comment|/**    * The hashcode of an ordered {@link EndpointPair} is equal to {@code Objects.hashCode(source(),    * target())}. The hashcode of an unordered {@link EndpointPair} is equal to {@code    * nodeU().hashCode() + nodeV().hashCode()}.    */
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
specifier|abstract
name|int
name|hashCode
parameter_list|()
function_decl|;
DECL|class|Ordered
specifier|private
specifier|static
specifier|final
class|class
name|Ordered
parameter_list|<
name|N
parameter_list|>
extends|extends
name|EndpointPair
argument_list|<
name|N
argument_list|>
block|{
DECL|method|Ordered (N source, N target)
specifier|private
name|Ordered
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
name|nodeU
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
name|nodeV
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isOrdered ()
specifier|public
name|boolean
name|isOrdered
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@heckForNull Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|CheckForNull
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
name|EndpointPair
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|EndpointPair
argument_list|<
name|?
argument_list|>
name|other
init|=
operator|(
name|EndpointPair
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|isOrdered
argument_list|()
operator|!=
name|other
operator|.
name|isOrdered
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
literal|"<"
operator|+
name|source
argument_list|()
operator|+
literal|" -> "
operator|+
name|target
argument_list|()
operator|+
literal|">"
return|;
block|}
block|}
DECL|class|Unordered
specifier|private
specifier|static
specifier|final
class|class
name|Unordered
parameter_list|<
name|N
parameter_list|>
extends|extends
name|EndpointPair
argument_list|<
name|N
argument_list|>
block|{
DECL|method|Unordered (N nodeU, N nodeV)
specifier|private
name|Unordered
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
block|{
name|super
argument_list|(
name|nodeU
argument_list|,
name|nodeV
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
DECL|method|isOrdered ()
specifier|public
name|boolean
name|isOrdered
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@heckForNull Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|CheckForNull
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
name|EndpointPair
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|EndpointPair
argument_list|<
name|?
argument_list|>
name|other
init|=
operator|(
name|EndpointPair
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|isOrdered
argument_list|()
operator|!=
name|other
operator|.
name|isOrdered
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Equivalent to the following simple implementation:
comment|// boolean condition1 = nodeU().equals(other.nodeU())&& nodeV().equals(other.nodeV());
comment|// boolean condition2 = nodeU().equals(other.nodeV())&& nodeV().equals(other.nodeU());
comment|// return condition1 || condition2;
if|if
condition|(
name|nodeU
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|nodeU
argument_list|()
argument_list|)
condition|)
block|{
comment|// check condition1
comment|// Here's the tricky bit. We don't have to explicitly check for condition2 in this case.
comment|// Why? The second half of condition2 requires that nodeV equals other.nodeU.
comment|// We already know that nodeU equals other.nodeU. Combined with the earlier statement,
comment|// and the transitive property of equality, this implies that nodeU equals nodeV.
comment|// If nodeU equals nodeV, condition1 == condition2, so checking condition1 is sufficient.
return|return
name|nodeV
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|nodeV
argument_list|()
argument_list|)
return|;
block|}
return|return
name|nodeU
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|nodeV
argument_list|()
argument_list|)
operator|&&
name|nodeV
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|nodeU
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
name|nodeU
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|+
name|nodeV
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
literal|"["
operator|+
name|nodeU
argument_list|()
operator|+
literal|", "
operator|+
name|nodeV
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
block|}
end_class

end_unit

