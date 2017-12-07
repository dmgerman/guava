begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2014 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|math
operator|.
name|IntMath
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|RoundingMode
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|Comparator
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
name|List
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
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_comment
comment|/**  * An accumulator that selects the "top" {@code k} elements added to it, relative to a provided  * comparator. "Top" can mean the greatest or the lowest elements, specified in the factory used to  * create the {@code TopKSelector} instance.  *  *<p>If your input data is available as an {@link Iterable} or {@link Iterator}, prefer {@link  * Ordering#leastOf(Iterable, int)}, which provides the same implementation with an interface  * tailored to that use case.  *  *<p>This uses the same efficient implementation as {@link Ordering#leastOf(Iterable, int)},  * offering expected O(n + k log k) performance (worst case O(n log k)) for n calls to {@link  * #offer} and a call to {@link #topK}, with O(k) memory. In comparison, quickselect has the same  * asymptotics but requires O(n) memory, and a {@code PriorityQueue} implementation takes O(n log  * k). In benchmarks, this implementation performs at least as well as either implementation, and  * degrades more gracefully for worst-case input.  *  *<p>The implementation does not necessarily use a<i>stable</i> sorting algorithm; when multiple  * equivalent elements are added to it, it is undefined which will come first in the output.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|TopKSelector
annotation|@
name|GwtCompatible
specifier|final
class|class
name|TopKSelector
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**    * Returns a {@code TopKSelector} that collects the lowest {@code k} elements added to it,    * relative to the natural ordering of the elements, and returns them via {@link #topK} in    * ascending order.    *    * @throws IllegalArgumentException if {@code k< 0}    */
DECL|method|least (int k)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|T
argument_list|>
parameter_list|>
name|TopKSelector
argument_list|<
name|T
argument_list|>
name|least
parameter_list|(
name|int
name|k
parameter_list|)
block|{
return|return
name|least
argument_list|(
name|k
argument_list|,
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns a {@code TopKSelector} that collects the greatest {@code k} elements added to it,    * relative to the natural ordering of the elements, and returns them via {@link #topK} in    * descending order.    *    * @throws IllegalArgumentException if {@code k< 0}    */
DECL|method|greatest (int k)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|T
argument_list|>
parameter_list|>
name|TopKSelector
argument_list|<
name|T
argument_list|>
name|greatest
parameter_list|(
name|int
name|k
parameter_list|)
block|{
return|return
name|greatest
argument_list|(
name|k
argument_list|,
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns a {@code TopKSelector} that collects the lowest {@code k} elements added to it,    * relative to the specified comparator, and returns them via {@link #topK} in ascending order.    *    * @throws IllegalArgumentException if {@code k< 0}    */
DECL|method|least (int k, Comparator<? super T> comparator)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|TopKSelector
argument_list|<
name|T
argument_list|>
name|least
parameter_list|(
name|int
name|k
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|T
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
operator|new
name|TopKSelector
argument_list|<
name|T
argument_list|>
argument_list|(
name|comparator
argument_list|,
name|k
argument_list|)
return|;
block|}
comment|/**    * Returns a {@code TopKSelector} that collects the greatest {@code k} elements added to it,    * relative to the specified comparator, and returns them via {@link #topK} in descending order.    *    * @throws IllegalArgumentException if {@code k< 0}    */
DECL|method|greatest (int k, Comparator<? super T> comparator)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|TopKSelector
argument_list|<
name|T
argument_list|>
name|greatest
parameter_list|(
name|int
name|k
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|T
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
operator|new
name|TopKSelector
argument_list|<
name|T
argument_list|>
argument_list|(
name|Ordering
operator|.
name|from
argument_list|(
name|comparator
argument_list|)
operator|.
name|reverse
argument_list|()
argument_list|,
name|k
argument_list|)
return|;
block|}
DECL|field|k
specifier|private
specifier|final
name|int
name|k
decl_stmt|;
DECL|field|comparator
specifier|private
specifier|final
name|Comparator
argument_list|<
name|?
super|super
name|T
argument_list|>
name|comparator
decl_stmt|;
comment|/*    * We are currently considering the elements in buffer in the range [0, bufferSize) as candidates    * for the top k elements. Whenever the buffer is filled, we quickselect the top k elements to the    * range [0, k) and ignore the remaining elements.    */
DECL|field|buffer
specifier|private
specifier|final
name|T
index|[]
name|buffer
decl_stmt|;
DECL|field|bufferSize
specifier|private
name|int
name|bufferSize
decl_stmt|;
comment|/**    * The largest of the lowest k elements we've seen so far relative to this comparator. If    * bufferSize â¥ k, then we can ignore any elements greater than this value.    */
DECL|field|threshold
specifier|private
name|T
name|threshold
decl_stmt|;
DECL|method|TopKSelector (Comparator<? super T> comparator, int k)
specifier|private
name|TopKSelector
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|T
argument_list|>
name|comparator
parameter_list|,
name|int
name|k
parameter_list|)
block|{
name|this
operator|.
name|comparator
operator|=
name|checkNotNull
argument_list|(
name|comparator
argument_list|,
literal|"comparator"
argument_list|)
expr_stmt|;
name|this
operator|.
name|k
operator|=
name|k
expr_stmt|;
name|checkArgument
argument_list|(
name|k
operator|>=
literal|0
argument_list|,
literal|"k must be nonnegative, was %s"
argument_list|,
name|k
argument_list|)
expr_stmt|;
name|this
operator|.
name|buffer
operator|=
operator|(
name|T
index|[]
operator|)
operator|new
name|Object
index|[
name|k
operator|*
literal|2
index|]
expr_stmt|;
name|this
operator|.
name|bufferSize
operator|=
literal|0
expr_stmt|;
name|this
operator|.
name|threshold
operator|=
literal|null
expr_stmt|;
block|}
comment|/**    * Adds {@code elem} as a candidate for the top {@code k} elements. This operation takes amortized    * O(1) time.    */
DECL|method|offer (@ullableDecl T elem)
specifier|public
name|void
name|offer
parameter_list|(
annotation|@
name|NullableDecl
name|T
name|elem
parameter_list|)
block|{
if|if
condition|(
name|k
operator|==
literal|0
condition|)
block|{
return|return;
block|}
elseif|else
if|if
condition|(
name|bufferSize
operator|==
literal|0
condition|)
block|{
name|buffer
index|[
literal|0
index|]
operator|=
name|elem
expr_stmt|;
name|threshold
operator|=
name|elem
expr_stmt|;
name|bufferSize
operator|=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|bufferSize
operator|<
name|k
condition|)
block|{
name|buffer
index|[
name|bufferSize
operator|++
index|]
operator|=
name|elem
expr_stmt|;
if|if
condition|(
name|comparator
operator|.
name|compare
argument_list|(
name|elem
argument_list|,
name|threshold
argument_list|)
operator|>
literal|0
condition|)
block|{
name|threshold
operator|=
name|elem
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|comparator
operator|.
name|compare
argument_list|(
name|elem
argument_list|,
name|threshold
argument_list|)
operator|<
literal|0
condition|)
block|{
comment|// Otherwise, we can ignore elem; we've seen k better elements.
name|buffer
index|[
name|bufferSize
operator|++
index|]
operator|=
name|elem
expr_stmt|;
if|if
condition|(
name|bufferSize
operator|==
literal|2
operator|*
name|k
condition|)
block|{
name|trim
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Quickselects the top k elements from the 2k elements in the buffer. O(k) expected time, O(k log    * k) worst case.    */
DECL|method|trim ()
specifier|private
name|void
name|trim
parameter_list|()
block|{
name|int
name|left
init|=
literal|0
decl_stmt|;
name|int
name|right
init|=
literal|2
operator|*
name|k
operator|-
literal|1
decl_stmt|;
name|int
name|minThresholdPosition
init|=
literal|0
decl_stmt|;
comment|// The leftmost position at which the greatest of the k lower elements
comment|// -- the new value of threshold -- might be found.
name|int
name|iterations
init|=
literal|0
decl_stmt|;
name|int
name|maxIterations
init|=
name|IntMath
operator|.
name|log2
argument_list|(
name|right
operator|-
name|left
argument_list|,
name|RoundingMode
operator|.
name|CEILING
argument_list|)
operator|*
literal|3
decl_stmt|;
while|while
condition|(
name|left
operator|<
name|right
condition|)
block|{
name|int
name|pivotIndex
init|=
operator|(
name|left
operator|+
name|right
operator|+
literal|1
operator|)
operator|>>>
literal|1
decl_stmt|;
name|int
name|pivotNewIndex
init|=
name|partition
argument_list|(
name|left
argument_list|,
name|right
argument_list|,
name|pivotIndex
argument_list|)
decl_stmt|;
if|if
condition|(
name|pivotNewIndex
operator|>
name|k
condition|)
block|{
name|right
operator|=
name|pivotNewIndex
operator|-
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|pivotNewIndex
operator|<
name|k
condition|)
block|{
name|left
operator|=
name|Math
operator|.
name|max
argument_list|(
name|pivotNewIndex
argument_list|,
name|left
operator|+
literal|1
argument_list|)
expr_stmt|;
name|minThresholdPosition
operator|=
name|pivotNewIndex
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
name|iterations
operator|++
expr_stmt|;
if|if
condition|(
name|iterations
operator|>=
name|maxIterations
condition|)
block|{
comment|// We've already taken O(k log k), let's make sure we don't take longer than O(k log k).
name|Arrays
operator|.
name|sort
argument_list|(
name|buffer
argument_list|,
name|left
argument_list|,
name|right
argument_list|,
name|comparator
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
name|bufferSize
operator|=
name|k
expr_stmt|;
name|threshold
operator|=
name|buffer
index|[
name|minThresholdPosition
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|minThresholdPosition
operator|+
literal|1
init|;
name|i
operator|<
name|k
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|comparator
operator|.
name|compare
argument_list|(
name|buffer
index|[
name|i
index|]
argument_list|,
name|threshold
argument_list|)
operator|>
literal|0
condition|)
block|{
name|threshold
operator|=
name|buffer
index|[
name|i
index|]
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Partitions the contents of buffer in the range [left, right] around the pivot element    * previously stored in buffer[pivotValue]. Returns the new index of the pivot element,    * pivotNewIndex, so that everything in [left, pivotNewIndex] is â¤ pivotValue and everything in    * (pivotNewIndex, right] is greater than pivotValue.    */
DECL|method|partition (int left, int right, int pivotIndex)
specifier|private
name|int
name|partition
parameter_list|(
name|int
name|left
parameter_list|,
name|int
name|right
parameter_list|,
name|int
name|pivotIndex
parameter_list|)
block|{
name|T
name|pivotValue
init|=
name|buffer
index|[
name|pivotIndex
index|]
decl_stmt|;
name|buffer
index|[
name|pivotIndex
index|]
operator|=
name|buffer
index|[
name|right
index|]
expr_stmt|;
name|int
name|pivotNewIndex
init|=
name|left
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|left
init|;
name|i
operator|<
name|right
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|comparator
operator|.
name|compare
argument_list|(
name|buffer
index|[
name|i
index|]
argument_list|,
name|pivotValue
argument_list|)
operator|<
literal|0
condition|)
block|{
name|swap
argument_list|(
name|pivotNewIndex
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|pivotNewIndex
operator|++
expr_stmt|;
block|}
block|}
name|buffer
index|[
name|right
index|]
operator|=
name|buffer
index|[
name|pivotNewIndex
index|]
expr_stmt|;
name|buffer
index|[
name|pivotNewIndex
index|]
operator|=
name|pivotValue
expr_stmt|;
return|return
name|pivotNewIndex
return|;
block|}
DECL|method|swap (int i, int j)
specifier|private
name|void
name|swap
parameter_list|(
name|int
name|i
parameter_list|,
name|int
name|j
parameter_list|)
block|{
name|T
name|tmp
init|=
name|buffer
index|[
name|i
index|]
decl_stmt|;
name|buffer
index|[
name|i
index|]
operator|=
name|buffer
index|[
name|j
index|]
expr_stmt|;
name|buffer
index|[
name|j
index|]
operator|=
name|tmp
expr_stmt|;
block|}
comment|/**    * Adds each member of {@code elements} as a candidate for the top {@code k} elements. This    * operation takes amortized linear time in the length of {@code elements}.    *    *<p>If all input data to this {@code TopKSelector} is in a single {@code Iterable}, prefer    * {@link Ordering#leastOf(Iterable, int)}, which provides a simpler API for that use case.    */
DECL|method|offerAll (Iterable<? extends T> elements)
specifier|public
name|void
name|offerAll
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|elements
parameter_list|)
block|{
name|offerAll
argument_list|(
name|elements
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Adds each member of {@code elements} as a candidate for the top {@code k} elements. This    * operation takes amortized linear time in the length of {@code elements}. The iterator is    * consumed after this operation completes.    *    *<p>If all input data to this {@code TopKSelector} is in a single {@code Iterator}, prefer    * {@link Ordering#leastOf(Iterator, int)}, which provides a simpler API for that use case.    */
DECL|method|offerAll (Iterator<? extends T> elements)
specifier|public
name|void
name|offerAll
parameter_list|(
name|Iterator
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|elements
parameter_list|)
block|{
while|while
condition|(
name|elements
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|offer
argument_list|(
name|elements
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Returns the top {@code k} elements offered to this {@code TopKSelector}, or all elements if    * fewer than {@code k} have been offered, in the order specified by the factory used to create    * this {@code TopKSelector}.    *    *<p>The returned list is an unmodifiable copy and will not be affected by further changes to    * this {@code TopKSelector}. This method returns in O(k log k) time.    */
DECL|method|topK ()
specifier|public
name|List
argument_list|<
name|T
argument_list|>
name|topK
parameter_list|()
block|{
name|Arrays
operator|.
name|sort
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|bufferSize
argument_list|,
name|comparator
argument_list|)
expr_stmt|;
if|if
condition|(
name|bufferSize
operator|>
name|k
condition|)
block|{
name|Arrays
operator|.
name|fill
argument_list|(
name|buffer
argument_list|,
name|k
argument_list|,
name|buffer
operator|.
name|length
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|bufferSize
operator|=
name|k
expr_stmt|;
name|threshold
operator|=
name|buffer
index|[
name|k
operator|-
literal|1
index|]
expr_stmt|;
block|}
comment|// we have to support null elements, so no ImmutableList for us
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Arrays
operator|.
name|copyOf
argument_list|(
name|buffer
argument_list|,
name|bufferSize
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

