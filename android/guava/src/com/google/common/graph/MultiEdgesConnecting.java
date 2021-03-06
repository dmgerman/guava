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
name|AbstractIterator
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
name|Iterator
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
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
import|;
end_import

begin_comment
comment|/**  * A class to represent the set of edges connecting an (implicit) origin node to a target node.  *  *<p>The {@link #outEdgeToNode} map allows this class to work on networks with parallel edges. See  * {@link EdgesConnecting} for a class that is more efficient but forbids parallel edges.  *  * @author James Sexton  * @param<E> Edge parameter type  */
end_comment

begin_class
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|MultiEdgesConnecting
specifier|abstract
class|class
name|MultiEdgesConnecting
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractSet
argument_list|<
name|E
argument_list|>
block|{
DECL|field|outEdgeToNode
specifier|private
specifier|final
name|Map
argument_list|<
name|E
argument_list|,
name|?
argument_list|>
name|outEdgeToNode
decl_stmt|;
DECL|field|targetNode
specifier|private
specifier|final
name|Object
name|targetNode
decl_stmt|;
DECL|method|MultiEdgesConnecting (Map<E, ?> outEdgeToNode, Object targetNode)
name|MultiEdgesConnecting
parameter_list|(
name|Map
argument_list|<
name|E
argument_list|,
name|?
argument_list|>
name|outEdgeToNode
parameter_list|,
name|Object
name|targetNode
parameter_list|)
block|{
name|this
operator|.
name|outEdgeToNode
operator|=
name|checkNotNull
argument_list|(
name|outEdgeToNode
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
name|Iterator
argument_list|<
name|?
extends|extends
name|Entry
argument_list|<
name|E
argument_list|,
name|?
argument_list|>
argument_list|>
name|entries
init|=
name|outEdgeToNode
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
operator|new
name|AbstractIterator
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
annotation|@
name|CheckForNull
specifier|protected
name|E
name|computeNext
parameter_list|()
block|{
while|while
condition|(
name|entries
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Entry
argument_list|<
name|E
argument_list|,
name|?
argument_list|>
name|entry
init|=
name|entries
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|targetNode
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|entry
operator|.
name|getKey
argument_list|()
return|;
block|}
block|}
return|return
name|endOfData
argument_list|()
return|;
block|}
block|}
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
return|return
name|targetNode
operator|.
name|equals
argument_list|(
name|outEdgeToNode
operator|.
name|get
argument_list|(
name|edge
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

