begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
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
name|checkArgument
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
name|annotations
operator|.
name|GwtCompatible
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
name|VisibleForTesting
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
import|;
end_import

begin_comment
comment|/**  * A non-blocking queue which automatically evicts elements from the head of the queue when  * attempting to add new elements onto the queue and it is full. This data structure is logically  * equivalent to a circular buffer (i.e., cyclic buffer or ring buffer).  *  *<p>An evicting queue must be configured with a maximum size. Each time an element is added  * to a full queue, the queue automatically removes its head element. This is different from  * conventional bounded queues, which either block or reject new elements when full.  *  *<p>This class is not thread-safe, and does not accept null elements.  *  * @author Kurt Alfred Kluever  * @since 15.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|EvictingQueue
specifier|public
specifier|final
class|class
name|EvictingQueue
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingQueue
argument_list|<
name|E
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|delegate
specifier|private
specifier|final
name|Queue
argument_list|<
name|E
argument_list|>
name|delegate
decl_stmt|;
DECL|field|maxSize
annotation|@
name|VisibleForTesting
specifier|final
name|int
name|maxSize
decl_stmt|;
DECL|method|EvictingQueue (int maxSize)
specifier|private
name|EvictingQueue
parameter_list|(
name|int
name|maxSize
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|maxSize
operator|>=
literal|0
argument_list|,
literal|"maxSize (%s) must>= 0"
argument_list|,
name|maxSize
argument_list|)
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|Platform
operator|.
name|newFastestQueue
argument_list|(
name|maxSize
argument_list|)
expr_stmt|;
name|this
operator|.
name|maxSize
operator|=
name|maxSize
expr_stmt|;
block|}
comment|/**    * Creates and returns a new evicting queue that will hold up to {@code maxSize} elements.    *    *<p>When {@code maxSize} is zero, elements will be evicted immediately after being added to the    * queue.    */
DECL|method|create (int maxSize)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|EvictingQueue
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|int
name|maxSize
parameter_list|)
block|{
return|return
operator|new
name|EvictingQueue
argument_list|<
name|E
argument_list|>
argument_list|(
name|maxSize
argument_list|)
return|;
block|}
comment|/**    * Returns the number of additional elements that this queue can accept without evicting;    * zero if the queue is currently full.    *    * @since 16.0    */
DECL|method|remainingCapacity ()
specifier|public
name|int
name|remainingCapacity
parameter_list|()
block|{
return|return
name|maxSize
operator|-
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Queue
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
comment|/**    * Adds the given element to this queue. If the queue is currently full, the element at the head    * of the queue is evicted to make room.    *    * @return {@code true} always    */
annotation|@
name|Override
DECL|method|offer (E e)
specifier|public
name|boolean
name|offer
parameter_list|(
name|E
name|e
parameter_list|)
block|{
return|return
name|add
argument_list|(
name|e
argument_list|)
return|;
block|}
comment|/**    * Adds the given element to this queue. If the queue is currently full, the element at the head    * of the queue is evicted to make room.    *    * @return {@code true} always    */
annotation|@
name|Override
DECL|method|add (E e)
specifier|public
name|boolean
name|add
parameter_list|(
name|E
name|e
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
comment|// check before removing
if|if
condition|(
name|maxSize
operator|==
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|size
argument_list|()
operator|==
name|maxSize
condition|)
block|{
name|delegate
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
name|delegate
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|addAll (Collection<? extends E> collection)
specifier|public
name|boolean
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|standardAddAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|contains (Object object)
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|contains
argument_list|(
name|checkNotNull
argument_list|(
name|object
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|remove (Object object)
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|remove
argument_list|(
name|checkNotNull
argument_list|(
name|object
argument_list|)
argument_list|)
return|;
block|}
comment|// TODO(kak): Do we want to checkNotNull each element in containsAll, removeAll, and retainAll?
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0L
decl_stmt|;
block|}
end_class

end_unit

