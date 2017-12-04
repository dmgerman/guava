begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|caliper
operator|.
name|BeforeExperiment
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|Benchmark
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|Param
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
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
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
name|Random
import|;
end_import

begin_comment
comment|/**  * Benchmarks to compare performance of MinMaxPriorityQueue and PriorityQueue.  *  * @author Sverre Sundsdal  */
end_comment

begin_class
DECL|class|MinMaxPriorityQueueBenchmark
specifier|public
class|class
name|MinMaxPriorityQueueBenchmark
block|{
DECL|field|comparator
annotation|@
name|Param
specifier|private
name|ComparatorType
name|comparator
decl_stmt|;
comment|// TODO(kevinb): add 1000000 back when we have the ability to throw
comment|// NotApplicableException in the expensive comparator case.
annotation|@
name|Param
argument_list|(
block|{
literal|"100"
block|,
literal|"10000"
block|}
argument_list|)
DECL|field|size
specifier|private
name|int
name|size
decl_stmt|;
DECL|field|heap
annotation|@
name|Param
specifier|private
name|HeapType
name|heap
decl_stmt|;
DECL|field|queue
specifier|private
name|Queue
argument_list|<
name|Integer
argument_list|>
name|queue
decl_stmt|;
DECL|field|random
specifier|private
specifier|final
name|Random
name|random
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
annotation|@
name|BeforeExperiment
DECL|method|setUp ()
name|void
name|setUp
parameter_list|()
block|{
name|queue
operator|=
name|heap
operator|.
name|create
argument_list|(
name|comparator
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|queue
operator|.
name|add
argument_list|(
name|random
operator|.
name|nextInt
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Benchmark
DECL|method|pollAndAdd (int reps)
name|void
name|pollAndAdd
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|reps
condition|;
name|i
operator|++
control|)
block|{
comment|// TODO(kevinb): precompute random #s?
name|queue
operator|.
name|add
argument_list|(
name|queue
operator|.
name|poll
argument_list|()
operator|^
name|random
operator|.
name|nextInt
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Benchmark
DECL|method|populate (int reps)
name|void
name|populate
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|reps
condition|;
name|i
operator|++
control|)
block|{
name|queue
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|size
condition|;
name|j
operator|++
control|)
block|{
comment|// TODO(kevinb): precompute random #s?
name|queue
operator|.
name|add
argument_list|(
name|random
operator|.
name|nextInt
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Implementation of the InvertedMinMaxPriorityQueue which forwards all calls to a    * MinMaxPriorityQueue, except poll, which is forwarded to pollMax. That way we can benchmark    * pollMax using the same code that benchmarks poll.    */
DECL|class|InvertedMinMaxPriorityQueue
specifier|static
specifier|final
class|class
name|InvertedMinMaxPriorityQueue
parameter_list|<
name|T
parameter_list|>
extends|extends
name|ForwardingQueue
argument_list|<
name|T
argument_list|>
block|{
DECL|field|mmHeap
name|MinMaxPriorityQueue
argument_list|<
name|T
argument_list|>
name|mmHeap
decl_stmt|;
DECL|method|InvertedMinMaxPriorityQueue (Comparator<T> comparator)
specifier|public
name|InvertedMinMaxPriorityQueue
parameter_list|(
name|Comparator
argument_list|<
name|T
argument_list|>
name|comparator
parameter_list|)
block|{
name|mmHeap
operator|=
name|MinMaxPriorityQueue
operator|.
name|orderedBy
argument_list|(
name|comparator
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Queue
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|mmHeap
return|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|public
name|T
name|poll
parameter_list|()
block|{
return|return
name|mmHeap
operator|.
name|pollLast
argument_list|()
return|;
block|}
block|}
DECL|enum|HeapType
specifier|public
enum|enum
name|HeapType
block|{
DECL|enumConstant|MIN_MAX
name|MIN_MAX
block|{
annotation|@
name|Override
specifier|public
name|Queue
argument_list|<
name|Integer
argument_list|>
name|create
parameter_list|(
name|Comparator
argument_list|<
name|Integer
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
name|MinMaxPriorityQueue
operator|.
name|orderedBy
argument_list|(
name|comparator
argument_list|)
operator|.
name|create
argument_list|()
return|;
block|}
block|}
block|,
DECL|enumConstant|PRIORITY_QUEUE
name|PRIORITY_QUEUE
block|{
annotation|@
name|Override
specifier|public
name|Queue
argument_list|<
name|Integer
argument_list|>
name|create
parameter_list|(
name|Comparator
argument_list|<
name|Integer
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
operator|new
name|PriorityQueue
argument_list|<>
argument_list|(
literal|11
argument_list|,
name|comparator
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|INVERTED_MIN_MAX
name|INVERTED_MIN_MAX
block|{
annotation|@
name|Override
specifier|public
name|Queue
argument_list|<
name|Integer
argument_list|>
name|create
parameter_list|(
name|Comparator
argument_list|<
name|Integer
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
operator|new
name|InvertedMinMaxPriorityQueue
argument_list|<>
argument_list|(
name|comparator
argument_list|)
return|;
block|}
block|}
block|;
DECL|method|create (Comparator<Integer> comparator)
specifier|public
specifier|abstract
name|Queue
argument_list|<
name|Integer
argument_list|>
name|create
parameter_list|(
name|Comparator
argument_list|<
name|Integer
argument_list|>
name|comparator
parameter_list|)
function_decl|;
block|}
comment|/**    * Does a CPU intensive operation on Integer and returns a BigInteger Used to implement an    * ordering that spends a lot of cpu.    */
DECL|class|ExpensiveComputation
specifier|static
class|class
name|ExpensiveComputation
implements|implements
name|Function
argument_list|<
name|Integer
argument_list|,
name|BigInteger
argument_list|>
block|{
annotation|@
name|Override
DECL|method|apply (Integer from)
specifier|public
name|BigInteger
name|apply
parameter_list|(
name|Integer
name|from
parameter_list|)
block|{
name|BigInteger
name|v
init|=
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|from
argument_list|)
decl_stmt|;
comment|// Math.sin is very slow for values outside 4*pi
comment|// Need to take absolute value to avoid inverting the value.
for|for
control|(
name|double
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|100
condition|;
name|i
operator|+=
literal|20
control|)
block|{
name|v
operator|=
name|v
operator|.
name|add
argument_list|(
name|v
operator|.
name|multiply
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
operator|(
operator|(
name|Double
operator|)
name|Math
operator|.
name|abs
argument_list|(
name|Math
operator|.
name|sin
argument_list|(
name|i
argument_list|)
operator|*
literal|10.0
argument_list|)
operator|)
operator|.
name|longValue
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|v
return|;
block|}
block|}
DECL|enum|ComparatorType
specifier|public
enum|enum
name|ComparatorType
block|{
DECL|enumConstant|CHEAP
name|CHEAP
block|{
annotation|@
name|Override
specifier|public
name|Comparator
argument_list|<
name|Integer
argument_list|>
name|get
parameter_list|()
block|{
return|return
name|Ordering
operator|.
name|natural
argument_list|()
return|;
block|}
block|}
block|,
DECL|enumConstant|EXPENSIVE
name|EXPENSIVE
block|{
annotation|@
name|Override
specifier|public
name|Comparator
argument_list|<
name|Integer
argument_list|>
name|get
parameter_list|()
block|{
return|return
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|onResultOf
argument_list|(
operator|new
name|ExpensiveComputation
argument_list|()
argument_list|)
return|;
block|}
block|}
block|;
DECL|method|get ()
specifier|public
specifier|abstract
name|Comparator
argument_list|<
name|Integer
argument_list|>
name|get
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

