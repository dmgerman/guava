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
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|MoreObjects
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
name|MoreObjects
operator|.
name|ToStringHelper
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
name|Ordering
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
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Used to represent the order of elements in a data structure that supports different options for  * iteration order guarantees.  *  *<p>Example usage:  *  *<pre>{@code  * MutableGraph<Integer> graph =  *     GraphBuilder.directed().nodeOrder(ElementOrder.<Integer>natural()).build();  * }</pre>  *  * @author Joshua O'Madadhain  * @since 20.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|Immutable
DECL|class|ElementOrder
specifier|public
specifier|final
class|class
name|ElementOrder
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|type
specifier|private
specifier|final
name|Type
name|type
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"Immutable"
argument_list|)
comment|// Hopefully the comparator provided is immutable!
DECL|field|comparator
specifier|private
specifier|final
annotation|@
name|Nullable
name|Comparator
argument_list|<
name|T
argument_list|>
name|comparator
decl_stmt|;
comment|/**    * The type of ordering that this object specifies.    *    *<ul>    *<li>UNORDERED: no order is guaranteed.    *<li>STABLE: ordering is guaranteed to follow a pattern that won't change between releases.    *       Some methods may have stronger guarantees.    *<li>INSERTION: insertion ordering is guaranteed.    *<li>SORTED: ordering according to a supplied comparator is guaranteed.    *</ul>    */
DECL|enum|Type
specifier|public
enum|enum
name|Type
block|{
DECL|enumConstant|UNORDERED
name|UNORDERED
block|,
DECL|enumConstant|STABLE
name|STABLE
block|,
DECL|enumConstant|INSERTION
name|INSERTION
block|,
DECL|enumConstant|SORTED
name|SORTED
block|}
DECL|method|ElementOrder (Type type, @Nullable Comparator<T> comparator)
specifier|private
name|ElementOrder
parameter_list|(
name|Type
name|type
parameter_list|,
annotation|@
name|Nullable
name|Comparator
argument_list|<
name|T
argument_list|>
name|comparator
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|checkNotNull
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|this
operator|.
name|comparator
operator|=
name|comparator
expr_stmt|;
name|checkState
argument_list|(
operator|(
name|type
operator|==
name|Type
operator|.
name|SORTED
operator|)
operator|==
operator|(
name|comparator
operator|!=
literal|null
operator|)
argument_list|)
expr_stmt|;
block|}
comment|/** Returns an instance which specifies that no ordering is guaranteed. */
DECL|method|unordered ()
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|>
name|ElementOrder
argument_list|<
name|S
argument_list|>
name|unordered
parameter_list|()
block|{
return|return
operator|new
name|ElementOrder
argument_list|<
name|S
argument_list|>
argument_list|(
name|Type
operator|.
name|UNORDERED
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**    * Returns an instance which specifies that ordering is guaranteed to be always be the same across    * iterations, and across releases. Some methods may have stronger guarantees.    *    *<p>This instance is only useful in combination with {@code incidentEdgeOrder}, e.g. {@code    * graphBuilder.incidentEdgeOrder(ElementOrder.stable())}.    *    *<h3>In combination with {@code incidentEdgeOrder}</h3>    *    *<p>{@code incidentEdgeOrder(ElementOrder.stable())} guarantees the ordering of the returned    * collections of the following methods:    *    *<ul>    *<li>For {@link Graph} and {@link ValueGraph}:    *<ul>    *<li>{@code edges()}: Stable order    *<li>{@code adjacentNodes(node)}: Connecting edge insertion order    *<li>{@code predecessors(node)}: Connecting edge insertion order    *<li>{@code successors(node)}: Connecting edge insertion order    *<li>{@code incidentEdges(node)}: Edge insertion order    *</ul>    *<li>For {@link Network}:    *<ul>    *<li>{@code adjacentNodes(node)}: Stable order    *<li>{@code predecessors(node)}: Connecting edge insertion order    *<li>{@code successors(node)}: Connecting edge insertion order    *<li>{@code incidentEdges(node)}: Stable order    *<li>{@code inEdges(node)}: Edge insertion order    *<li>{@code outEdges(node)}: Edge insertion order    *<li>{@code adjacentEdges(edge)}: Stable order    *<li>{@code edgesConnecting(nodeU, nodeV)}: Edge insertion order    *</ul>    *</ul>    */
DECL|method|stable ()
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|>
name|ElementOrder
argument_list|<
name|S
argument_list|>
name|stable
parameter_list|()
block|{
return|return
operator|new
name|ElementOrder
argument_list|<
name|S
argument_list|>
argument_list|(
name|Type
operator|.
name|STABLE
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/** Returns an instance which specifies that insertion ordering is guaranteed. */
DECL|method|insertion ()
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|>
name|ElementOrder
argument_list|<
name|S
argument_list|>
name|insertion
parameter_list|()
block|{
return|return
operator|new
name|ElementOrder
argument_list|<
name|S
argument_list|>
argument_list|(
name|Type
operator|.
name|INSERTION
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**    * Returns an instance which specifies that the natural ordering of the elements is guaranteed.    */
DECL|method|natural ()
specifier|public
specifier|static
parameter_list|<
name|S
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|S
argument_list|>
parameter_list|>
name|ElementOrder
argument_list|<
name|S
argument_list|>
name|natural
parameter_list|()
block|{
return|return
operator|new
name|ElementOrder
argument_list|<
name|S
argument_list|>
argument_list|(
name|Type
operator|.
name|SORTED
argument_list|,
name|Ordering
operator|.
expr|<
name|S
operator|>
name|natural
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns an instance which specifies that the ordering of the elements is guaranteed to be    * determined by {@code comparator}.    */
DECL|method|sorted (Comparator<S> comparator)
specifier|public
specifier|static
parameter_list|<
name|S
parameter_list|>
name|ElementOrder
argument_list|<
name|S
argument_list|>
name|sorted
parameter_list|(
name|Comparator
argument_list|<
name|S
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
operator|new
name|ElementOrder
argument_list|<
name|S
argument_list|>
argument_list|(
name|Type
operator|.
name|SORTED
argument_list|,
name|comparator
argument_list|)
return|;
block|}
comment|/** Returns the type of ordering used. */
DECL|method|type ()
specifier|public
name|Type
name|type
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**    * Returns the {@link Comparator} used.    *    * @throws UnsupportedOperationException if comparator is not defined    */
DECL|method|comparator ()
specifier|public
name|Comparator
argument_list|<
name|T
argument_list|>
name|comparator
parameter_list|()
block|{
if|if
condition|(
name|comparator
operator|!=
literal|null
condition|)
block|{
return|return
name|comparator
return|;
block|}
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"This ordering does not define a comparator."
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
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
name|ElementOrder
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ElementOrder
argument_list|<
name|?
argument_list|>
name|other
init|=
operator|(
name|ElementOrder
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
operator|(
name|type
operator|==
name|other
operator|.
name|type
operator|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|comparator
argument_list|,
name|other
operator|.
name|comparator
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
name|type
argument_list|,
name|comparator
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
name|ToStringHelper
name|helper
init|=
name|MoreObjects
operator|.
name|toStringHelper
argument_list|(
name|this
argument_list|)
operator|.
name|add
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|comparator
operator|!=
literal|null
condition|)
block|{
name|helper
operator|.
name|add
argument_list|(
literal|"comparator"
argument_list|,
name|comparator
argument_list|)
expr_stmt|;
block|}
return|return
name|helper
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/** Returns an empty mutable map whose keys will respect this {@link ElementOrder}. */
DECL|method|createMap (int expectedSize)
parameter_list|<
name|K
extends|extends
name|T
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|createMap
parameter_list|(
name|int
name|expectedSize
parameter_list|)
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|UNORDERED
case|:
return|return
name|Maps
operator|.
name|newHashMapWithExpectedSize
argument_list|(
name|expectedSize
argument_list|)
return|;
case|case
name|INSERTION
case|:
case|case
name|STABLE
case|:
return|return
name|Maps
operator|.
name|newLinkedHashMapWithExpectedSize
argument_list|(
name|expectedSize
argument_list|)
return|;
case|case
name|SORTED
case|:
return|return
name|Maps
operator|.
name|newTreeMap
argument_list|(
name|comparator
argument_list|()
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|cast ()
parameter_list|<
name|T1
extends|extends
name|T
parameter_list|>
name|ElementOrder
argument_list|<
name|T1
argument_list|>
name|cast
parameter_list|()
block|{
return|return
operator|(
name|ElementOrder
argument_list|<
name|T1
argument_list|>
operator|)
name|this
return|;
block|}
block|}
end_class

end_unit

