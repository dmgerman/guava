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
name|collect
operator|.
name|ImmutableSet
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
name|java
operator|.
name|util
operator|.
name|AbstractSet
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
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
import|;
end_import

begin_comment
comment|/**  * A class to represent the set of edges connecting an (implicit) origin node to a target node.  *  *<p>The {@link #nodeToOutEdge} map means this class only works on networks without parallel edges.  * See {@link MultiEdgesConnecting} for a class that works with parallel edges.  *  * @author James Sexton  * @param<E> Edge parameter type  */
end_comment

begin_class
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|EdgesConnecting
specifier|final
class|class
name|EdgesConnecting
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractSet
argument_list|<
name|E
argument_list|>
block|{
DECL|field|nodeToOutEdge
specifier|private
specifier|final
name|Map
argument_list|<
name|?
argument_list|,
name|E
argument_list|>
name|nodeToOutEdge
decl_stmt|;
DECL|field|targetNode
specifier|private
specifier|final
name|Object
name|targetNode
decl_stmt|;
DECL|method|EdgesConnecting (Map<?, E> nodeToEdgeMap, Object targetNode)
name|EdgesConnecting
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|E
argument_list|>
name|nodeToEdgeMap
parameter_list|,
name|Object
name|targetNode
parameter_list|)
block|{
name|this
operator|.
name|nodeToOutEdge
operator|=
name|checkNotNull
argument_list|(
name|nodeToEdgeMap
argument_list|)
expr_stmt|;
name|this
operator|.
name|targetNode
operator|=
name|checkNotNull
argument_list|(
name|targetNode
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
name|E
name|connectingEdge
init|=
name|getConnectingEdge
argument_list|()
decl_stmt|;
return|return
operator|(
name|connectingEdge
operator|==
literal|null
operator|)
condition|?
name|ImmutableSet
operator|.
expr|<
name|E
operator|>
name|of
argument_list|()
operator|.
name|iterator
argument_list|()
else|:
name|Iterators
operator|.
name|singletonIterator
argument_list|(
name|connectingEdge
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|getConnectingEdge
argument_list|()
operator|==
literal|null
condition|?
literal|0
else|:
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|contains (@heckForNull Object edge)
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|edge
parameter_list|)
block|{
name|E
name|connectingEdge
init|=
name|getConnectingEdge
argument_list|()
decl_stmt|;
return|return
operator|(
name|connectingEdge
operator|!=
literal|null
operator|&&
name|connectingEdge
operator|.
name|equals
argument_list|(
name|edge
argument_list|)
operator|)
return|;
block|}
annotation|@
name|CheckForNull
DECL|method|getConnectingEdge ()
specifier|private
name|E
name|getConnectingEdge
parameter_list|()
block|{
return|return
name|nodeToOutEdge
operator|.
name|get
argument_list|(
name|targetNode
argument_list|)
return|;
block|}
block|}
end_class

end_unit

