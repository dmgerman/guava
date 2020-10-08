begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|GwtIncompatible
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
name|Preconditions
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
name|CanIgnoreReturnValue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayDeque
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
name|Deque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|PriorityQueue
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ArrayBlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|BlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentLinkedQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|LinkedBlockingDeque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|LinkedBlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|PriorityBlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|SynchronousQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_comment
comment|/**  * Static utility methods pertaining to {@link Queue} and {@link Deque} instances. Also see this  * class's counterparts {@link Lists}, {@link Sets}, and {@link Maps}.  *  * @author Kurt Alfred Kluever  * @since 11.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|Queues
specifier|public
specifier|final
class|class
name|Queues
block|{
DECL|method|Queues ()
specifier|private
name|Queues
parameter_list|()
block|{}
comment|// ArrayBlockingQueue
comment|/**    * Creates an empty {@code ArrayBlockingQueue} with the given (fixed) capacity and nonfair access    * policy.    */
annotation|@
name|GwtIncompatible
comment|// ArrayBlockingQueue
DECL|method|newArrayBlockingQueue (int capacity)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ArrayBlockingQueue
argument_list|<
name|E
argument_list|>
name|newArrayBlockingQueue
parameter_list|(
name|int
name|capacity
parameter_list|)
block|{
return|return
operator|new
name|ArrayBlockingQueue
argument_list|<
name|E
argument_list|>
argument_list|(
name|capacity
argument_list|)
return|;
block|}
comment|// ArrayDeque
comment|/**    * Creates an empty {@code ArrayDeque}.    *    * @since 12.0    */
DECL|method|newArrayDeque ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ArrayDeque
argument_list|<
name|E
argument_list|>
name|newArrayDeque
parameter_list|()
block|{
return|return
operator|new
name|ArrayDeque
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Creates an {@code ArrayDeque} containing the elements of the specified iterable, in the order    * they are returned by the iterable's iterator.    *    * @since 12.0    */
DECL|method|newArrayDeque (Iterable<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ArrayDeque
argument_list|<
name|E
argument_list|>
name|newArrayDeque
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
parameter_list|)
block|{
if|if
condition|(
name|elements
operator|instanceof
name|Collection
condition|)
block|{
return|return
operator|new
name|ArrayDeque
argument_list|<
name|E
argument_list|>
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
operator|)
name|elements
argument_list|)
return|;
block|}
name|ArrayDeque
argument_list|<
name|E
argument_list|>
name|deque
init|=
operator|new
name|ArrayDeque
argument_list|<
name|E
argument_list|>
argument_list|()
decl_stmt|;
name|Iterables
operator|.
name|addAll
argument_list|(
name|deque
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|deque
return|;
block|}
comment|// ConcurrentLinkedQueue
comment|/** Creates an empty {@code ConcurrentLinkedQueue}. */
annotation|@
name|GwtIncompatible
comment|// ConcurrentLinkedQueue
DECL|method|newConcurrentLinkedQueue ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ConcurrentLinkedQueue
argument_list|<
name|E
argument_list|>
name|newConcurrentLinkedQueue
parameter_list|()
block|{
return|return
operator|new
name|ConcurrentLinkedQueue
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Creates a {@code ConcurrentLinkedQueue} containing the elements of the specified iterable, in    * the order they are returned by the iterable's iterator.    */
annotation|@
name|GwtIncompatible
comment|// ConcurrentLinkedQueue
DECL|method|newConcurrentLinkedQueue ( Iterable<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ConcurrentLinkedQueue
argument_list|<
name|E
argument_list|>
name|newConcurrentLinkedQueue
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
parameter_list|)
block|{
if|if
condition|(
name|elements
operator|instanceof
name|Collection
condition|)
block|{
return|return
operator|new
name|ConcurrentLinkedQueue
argument_list|<
name|E
argument_list|>
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
operator|)
name|elements
argument_list|)
return|;
block|}
name|ConcurrentLinkedQueue
argument_list|<
name|E
argument_list|>
name|queue
init|=
operator|new
name|ConcurrentLinkedQueue
argument_list|<
name|E
argument_list|>
argument_list|()
decl_stmt|;
name|Iterables
operator|.
name|addAll
argument_list|(
name|queue
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|queue
return|;
block|}
comment|// LinkedBlockingDeque
comment|/**    * Creates an empty {@code LinkedBlockingDeque} with a capacity of {@link Integer#MAX_VALUE}.    *    * @since 12.0    */
annotation|@
name|GwtIncompatible
comment|// LinkedBlockingDeque
DECL|method|newLinkedBlockingDeque ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|LinkedBlockingDeque
argument_list|<
name|E
argument_list|>
name|newLinkedBlockingDeque
parameter_list|()
block|{
return|return
operator|new
name|LinkedBlockingDeque
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Creates an empty {@code LinkedBlockingDeque} with the given (fixed) capacity.    *    * @throws IllegalArgumentException if {@code capacity} is less than 1    * @since 12.0    */
annotation|@
name|GwtIncompatible
comment|// LinkedBlockingDeque
DECL|method|newLinkedBlockingDeque (int capacity)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|LinkedBlockingDeque
argument_list|<
name|E
argument_list|>
name|newLinkedBlockingDeque
parameter_list|(
name|int
name|capacity
parameter_list|)
block|{
return|return
operator|new
name|LinkedBlockingDeque
argument_list|<
name|E
argument_list|>
argument_list|(
name|capacity
argument_list|)
return|;
block|}
comment|/**    * Creates a {@code LinkedBlockingDeque} with a capacity of {@link Integer#MAX_VALUE}, containing    * the elements of the specified iterable, in the order they are returned by the iterable's    * iterator.    *    * @since 12.0    */
annotation|@
name|GwtIncompatible
comment|// LinkedBlockingDeque
DECL|method|newLinkedBlockingDeque (Iterable<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|LinkedBlockingDeque
argument_list|<
name|E
argument_list|>
name|newLinkedBlockingDeque
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
parameter_list|)
block|{
if|if
condition|(
name|elements
operator|instanceof
name|Collection
condition|)
block|{
return|return
operator|new
name|LinkedBlockingDeque
argument_list|<
name|E
argument_list|>
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
operator|)
name|elements
argument_list|)
return|;
block|}
name|LinkedBlockingDeque
argument_list|<
name|E
argument_list|>
name|deque
init|=
operator|new
name|LinkedBlockingDeque
argument_list|<
name|E
argument_list|>
argument_list|()
decl_stmt|;
name|Iterables
operator|.
name|addAll
argument_list|(
name|deque
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|deque
return|;
block|}
comment|// LinkedBlockingQueue
comment|/** Creates an empty {@code LinkedBlockingQueue} with a capacity of {@link Integer#MAX_VALUE}. */
annotation|@
name|GwtIncompatible
comment|// LinkedBlockingQueue
DECL|method|newLinkedBlockingQueue ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|LinkedBlockingQueue
argument_list|<
name|E
argument_list|>
name|newLinkedBlockingQueue
parameter_list|()
block|{
return|return
operator|new
name|LinkedBlockingQueue
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Creates an empty {@code LinkedBlockingQueue} with the given (fixed) capacity.    *    * @throws IllegalArgumentException if {@code capacity} is less than 1    */
annotation|@
name|GwtIncompatible
comment|// LinkedBlockingQueue
DECL|method|newLinkedBlockingQueue (int capacity)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|LinkedBlockingQueue
argument_list|<
name|E
argument_list|>
name|newLinkedBlockingQueue
parameter_list|(
name|int
name|capacity
parameter_list|)
block|{
return|return
operator|new
name|LinkedBlockingQueue
argument_list|<
name|E
argument_list|>
argument_list|(
name|capacity
argument_list|)
return|;
block|}
comment|/**    * Creates a {@code LinkedBlockingQueue} with a capacity of {@link Integer#MAX_VALUE}, containing    * the elements of the specified iterable, in the order they are returned by the iterable's    * iterator.    *    * @param elements the elements that the queue should contain, in order    * @return a new {@code LinkedBlockingQueue} containing those elements    */
annotation|@
name|GwtIncompatible
comment|// LinkedBlockingQueue
DECL|method|newLinkedBlockingQueue (Iterable<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|LinkedBlockingQueue
argument_list|<
name|E
argument_list|>
name|newLinkedBlockingQueue
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
parameter_list|)
block|{
if|if
condition|(
name|elements
operator|instanceof
name|Collection
condition|)
block|{
return|return
operator|new
name|LinkedBlockingQueue
argument_list|<
name|E
argument_list|>
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
operator|)
name|elements
argument_list|)
return|;
block|}
name|LinkedBlockingQueue
argument_list|<
name|E
argument_list|>
name|queue
init|=
operator|new
name|LinkedBlockingQueue
argument_list|<
name|E
argument_list|>
argument_list|()
decl_stmt|;
name|Iterables
operator|.
name|addAll
argument_list|(
name|queue
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|queue
return|;
block|}
comment|// LinkedList: see {@link com.google.common.collect.Lists}
comment|// PriorityBlockingQueue
comment|/**    * Creates an empty {@code PriorityBlockingQueue} with the ordering given by its elements' natural    * ordering.    *    * @since 11.0 (requires that {@code E} be {@code Comparable} since 15.0).    */
annotation|@
name|GwtIncompatible
comment|// PriorityBlockingQueue
DECL|method|newPriorityBlockingQueue ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
parameter_list|>
name|PriorityBlockingQueue
argument_list|<
name|E
argument_list|>
name|newPriorityBlockingQueue
parameter_list|()
block|{
return|return
operator|new
name|PriorityBlockingQueue
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Creates a {@code PriorityBlockingQueue} containing the given elements.    *    *<p><b>Note:</b> If the specified iterable is a {@code SortedSet} or a {@code PriorityQueue},    * this priority queue will be ordered according to the same ordering.    *    * @since 11.0 (requires that {@code E} be {@code Comparable} since 15.0).    */
annotation|@
name|GwtIncompatible
comment|// PriorityBlockingQueue
DECL|method|newPriorityBlockingQueue ( Iterable<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
parameter_list|>
name|PriorityBlockingQueue
argument_list|<
name|E
argument_list|>
name|newPriorityBlockingQueue
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
parameter_list|)
block|{
if|if
condition|(
name|elements
operator|instanceof
name|Collection
condition|)
block|{
return|return
operator|new
name|PriorityBlockingQueue
argument_list|<
name|E
argument_list|>
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
operator|)
name|elements
argument_list|)
return|;
block|}
name|PriorityBlockingQueue
argument_list|<
name|E
argument_list|>
name|queue
init|=
operator|new
name|PriorityBlockingQueue
argument_list|<
name|E
argument_list|>
argument_list|()
decl_stmt|;
name|Iterables
operator|.
name|addAll
argument_list|(
name|queue
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|queue
return|;
block|}
comment|// PriorityQueue
comment|/**    * Creates an empty {@code PriorityQueue} with the ordering given by its elements' natural    * ordering.    *    * @since 11.0 (requires that {@code E} be {@code Comparable} since 15.0).    */
DECL|method|newPriorityQueue ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
parameter_list|>
name|PriorityQueue
argument_list|<
name|E
argument_list|>
name|newPriorityQueue
parameter_list|()
block|{
return|return
operator|new
name|PriorityQueue
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Creates a {@code PriorityQueue} containing the given elements.    *    *<p><b>Note:</b> If the specified iterable is a {@code SortedSet} or a {@code PriorityQueue},    * this priority queue will be ordered according to the same ordering.    *    * @since 11.0 (requires that {@code E} be {@code Comparable} since 15.0).    */
DECL|method|newPriorityQueue ( Iterable<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
parameter_list|>
name|PriorityQueue
argument_list|<
name|E
argument_list|>
name|newPriorityQueue
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
parameter_list|)
block|{
if|if
condition|(
name|elements
operator|instanceof
name|Collection
condition|)
block|{
return|return
operator|new
name|PriorityQueue
argument_list|<
name|E
argument_list|>
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
operator|)
name|elements
argument_list|)
return|;
block|}
name|PriorityQueue
argument_list|<
name|E
argument_list|>
name|queue
init|=
operator|new
name|PriorityQueue
argument_list|<
name|E
argument_list|>
argument_list|()
decl_stmt|;
name|Iterables
operator|.
name|addAll
argument_list|(
name|queue
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|queue
return|;
block|}
comment|// SynchronousQueue
comment|/** Creates an empty {@code SynchronousQueue} with nonfair access policy. */
annotation|@
name|GwtIncompatible
comment|// SynchronousQueue
DECL|method|newSynchronousQueue ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|SynchronousQueue
argument_list|<
name|E
argument_list|>
name|newSynchronousQueue
parameter_list|()
block|{
return|return
operator|new
name|SynchronousQueue
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Drains the queue as {@link BlockingQueue#drainTo(Collection, int)}, but if the requested {@code    * numElements} elements are not available, it will wait for them up to the specified timeout.    *    * @param q the blocking queue to be drained    * @param buffer where to add the transferred elements    * @param numElements the number of elements to be waited for    * @param timeout how long to wait before giving up    * @return the number of elements transferred    * @throws InterruptedException if interrupted while waiting    * @since 28.0    */
annotation|@
name|Beta
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|GwtIncompatible
comment|// BlockingQueue
DECL|method|drain ( BlockingQueue<E> q, Collection<? super E> buffer, int numElements, java.time.Duration timeout)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|int
name|drain
parameter_list|(
name|BlockingQueue
argument_list|<
name|E
argument_list|>
name|q
parameter_list|,
name|Collection
argument_list|<
name|?
super|super
name|E
argument_list|>
name|buffer
parameter_list|,
name|int
name|numElements
parameter_list|,
name|java
operator|.
name|time
operator|.
name|Duration
name|timeout
parameter_list|)
throws|throws
name|InterruptedException
block|{
comment|// TODO(b/126049426): Consider using saturateToNanos(timeout) instead.
return|return
name|drain
argument_list|(
name|q
argument_list|,
name|buffer
argument_list|,
name|numElements
argument_list|,
name|timeout
operator|.
name|toNanos
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
return|;
block|}
comment|/**    * Drains the queue as {@link BlockingQueue#drainTo(Collection, int)}, but if the requested {@code    * numElements} elements are not available, it will wait for them up to the specified timeout.    *    * @param q the blocking queue to be drained    * @param buffer where to add the transferred elements    * @param numElements the number of elements to be waited for    * @param timeout how long to wait before giving up, in units of {@code unit}    * @param unit a {@code TimeUnit} determining how to interpret the timeout parameter    * @return the number of elements transferred    * @throws InterruptedException if interrupted while waiting    */
annotation|@
name|Beta
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|GwtIncompatible
comment|// BlockingQueue
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should accept a java.time.Duration
DECL|method|drain ( BlockingQueue<E> q, Collection<? super E> buffer, int numElements, long timeout, TimeUnit unit)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|int
name|drain
parameter_list|(
name|BlockingQueue
argument_list|<
name|E
argument_list|>
name|q
parameter_list|,
name|Collection
argument_list|<
name|?
super|super
name|E
argument_list|>
name|buffer
parameter_list|,
name|int
name|numElements
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
comment|/*      * This code performs one System.nanoTime() more than necessary, and in return, the time to      * execute Queue#drainTo is not added *on top* of waiting for the timeout (which could make      * the timeout arbitrarily inaccurate, given a queue that is slow to drain).      */
name|long
name|deadline
init|=
name|System
operator|.
name|nanoTime
argument_list|()
operator|+
name|unit
operator|.
name|toNanos
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
name|int
name|added
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|added
operator|<
name|numElements
condition|)
block|{
comment|// we could rely solely on #poll, but #drainTo might be more efficient when there are multiple
comment|// elements already available (e.g. LinkedBlockingQueue#drainTo locks only once)
name|added
operator|+=
name|q
operator|.
name|drainTo
argument_list|(
name|buffer
argument_list|,
name|numElements
operator|-
name|added
argument_list|)
expr_stmt|;
if|if
condition|(
name|added
operator|<
name|numElements
condition|)
block|{
comment|// not enough elements immediately available; will have to poll
name|E
name|e
init|=
name|q
operator|.
name|poll
argument_list|(
name|deadline
operator|-
name|System
operator|.
name|nanoTime
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
break|break;
comment|// we already waited enough, and there are no more elements in sight
block|}
name|buffer
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|added
operator|++
expr_stmt|;
block|}
block|}
return|return
name|added
return|;
block|}
comment|/**    * Drains the queue as {@linkplain #drain(BlockingQueue, Collection, int, Duration)}, but with a    * different behavior in case it is interrupted while waiting. In that case, the operation will    * continue as usual, and in the end the thread's interruption status will be set (no {@code    * InterruptedException} is thrown).    *    * @param q the blocking queue to be drained    * @param buffer where to add the transferred elements    * @param numElements the number of elements to be waited for    * @param timeout how long to wait before giving up    * @return the number of elements transferred    * @since 28.0    */
annotation|@
name|Beta
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|GwtIncompatible
comment|// BlockingQueue
DECL|method|drainUninterruptibly ( BlockingQueue<E> q, Collection<? super E> buffer, int numElements, java.time.Duration timeout)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|int
name|drainUninterruptibly
parameter_list|(
name|BlockingQueue
argument_list|<
name|E
argument_list|>
name|q
parameter_list|,
name|Collection
argument_list|<
name|?
super|super
name|E
argument_list|>
name|buffer
parameter_list|,
name|int
name|numElements
parameter_list|,
name|java
operator|.
name|time
operator|.
name|Duration
name|timeout
parameter_list|)
block|{
comment|// TODO(b/126049426): Consider using saturateToNanos(timeout) instead.
return|return
name|drainUninterruptibly
argument_list|(
name|q
argument_list|,
name|buffer
argument_list|,
name|numElements
argument_list|,
name|timeout
operator|.
name|toNanos
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
return|;
block|}
comment|/**    * Drains the queue as {@linkplain #drain(BlockingQueue, Collection, int, long, TimeUnit)}, but    * with a different behavior in case it is interrupted while waiting. In that case, the operation    * will continue as usual, and in the end the thread's interruption status will be set (no {@code    * InterruptedException} is thrown).    *    * @param q the blocking queue to be drained    * @param buffer where to add the transferred elements    * @param numElements the number of elements to be waited for    * @param timeout how long to wait before giving up, in units of {@code unit}    * @param unit a {@code TimeUnit} determining how to interpret the timeout parameter    * @return the number of elements transferred    */
annotation|@
name|Beta
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|GwtIncompatible
comment|// BlockingQueue
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should accept a java.time.Duration
DECL|method|drainUninterruptibly ( BlockingQueue<E> q, Collection<? super E> buffer, int numElements, long timeout, TimeUnit unit)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|int
name|drainUninterruptibly
parameter_list|(
name|BlockingQueue
argument_list|<
name|E
argument_list|>
name|q
parameter_list|,
name|Collection
argument_list|<
name|?
super|super
name|E
argument_list|>
name|buffer
parameter_list|,
name|int
name|numElements
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|long
name|deadline
init|=
name|System
operator|.
name|nanoTime
argument_list|()
operator|+
name|unit
operator|.
name|toNanos
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
name|int
name|added
init|=
literal|0
decl_stmt|;
name|boolean
name|interrupted
init|=
literal|false
decl_stmt|;
try|try
block|{
while|while
condition|(
name|added
operator|<
name|numElements
condition|)
block|{
comment|// we could rely solely on #poll, but #drainTo might be more efficient when there are
comment|// multiple elements already available (e.g. LinkedBlockingQueue#drainTo locks only once)
name|added
operator|+=
name|q
operator|.
name|drainTo
argument_list|(
name|buffer
argument_list|,
name|numElements
operator|-
name|added
argument_list|)
expr_stmt|;
if|if
condition|(
name|added
operator|<
name|numElements
condition|)
block|{
comment|// not enough elements immediately available; will have to poll
name|E
name|e
decl_stmt|;
comment|// written exactly once, by a successful (uninterrupted) invocation of #poll
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
name|e
operator|=
name|q
operator|.
name|poll
argument_list|(
name|deadline
operator|-
name|System
operator|.
name|nanoTime
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
expr_stmt|;
break|break;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
name|interrupted
operator|=
literal|true
expr_stmt|;
comment|// note interruption and retry
block|}
block|}
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
break|break;
comment|// we already waited enough, and there are no more elements in sight
block|}
name|buffer
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|added
operator|++
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|interrupted
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|added
return|;
block|}
comment|/**    * Returns a synchronized (thread-safe) queue backed by the specified queue. In order to guarantee    * serial access, it is critical that<b>all</b> access to the backing queue is accomplished    * through the returned queue.    *    *<p>It is imperative that the user manually synchronize on the returned queue when accessing the    * queue's iterator:    *    *<pre>{@code    * Queue<E> queue = Queues.synchronizedQueue(MinMaxPriorityQueue.<E>create());    * ...    * queue.add(element);  // Needn't be in synchronized block    * ...    * synchronized (queue) {  // Must synchronize on queue!    *   Iterator<E> i = queue.iterator(); // Must be in synchronized block    *   while (i.hasNext()) {    *     foo(i.next());    *   }    * }    * }</pre>    *    *<p>Failure to follow this advice may result in non-deterministic behavior.    *    *<p>The returned queue will be serializable if the specified queue is serializable.    *    * @param queue the queue to be wrapped in a synchronized view    * @return a synchronized view of the specified queue    * @since 14.0    */
DECL|method|synchronizedQueue (Queue<E> queue)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Queue
argument_list|<
name|E
argument_list|>
name|synchronizedQueue
parameter_list|(
name|Queue
argument_list|<
name|E
argument_list|>
name|queue
parameter_list|)
block|{
return|return
name|Synchronized
operator|.
name|queue
argument_list|(
name|queue
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**    * Returns a synchronized (thread-safe) deque backed by the specified deque. In order to guarantee    * serial access, it is critical that<b>all</b> access to the backing deque is accomplished    * through the returned deque.    *    *<p>It is imperative that the user manually synchronize on the returned deque when accessing any    * of the deque's iterators:    *    *<pre>{@code    * Deque<E> deque = Queues.synchronizedDeque(Queues.<E>newArrayDeque());    * ...    * deque.add(element);  // Needn't be in synchronized block    * ...    * synchronized (deque) {  // Must synchronize on deque!    *   Iterator<E> i = deque.iterator(); // Must be in synchronized block    *   while (i.hasNext()) {    *     foo(i.next());    *   }    * }    * }</pre>    *    *<p>Failure to follow this advice may result in non-deterministic behavior.    *    *<p>The returned deque will be serializable if the specified deque is serializable.    *    * @param deque the deque to be wrapped in a synchronized view    * @return a synchronized view of the specified deque    * @since 15.0    */
DECL|method|synchronizedDeque (Deque<E> deque)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Deque
argument_list|<
name|E
argument_list|>
name|synchronizedDeque
parameter_list|(
name|Deque
argument_list|<
name|E
argument_list|>
name|deque
parameter_list|)
block|{
return|return
name|Synchronized
operator|.
name|deque
argument_list|(
name|deque
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
end_class

end_unit

