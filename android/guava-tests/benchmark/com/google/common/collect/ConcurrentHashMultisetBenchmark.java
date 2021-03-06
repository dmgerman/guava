begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|collect
operator|.
name|CollectPreconditions
operator|.
name|checkNonnegative
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
name|annotations
operator|.
name|VisibleForTesting
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
name|primitives
operator|.
name|Ints
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
name|util
operator|.
name|concurrent
operator|.
name|ThreadFactoryBuilder
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
name|Random
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
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
name|ConcurrentHashMap
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
name|ConcurrentMap
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
name|ExecutionException
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
name|ExecutorService
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
name|Executors
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
name|Future
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
comment|/**  * Benchmarks for {@link ConcurrentHashMultiset}.  *  * @author mike nonemacher  */
end_comment

begin_class
DECL|class|ConcurrentHashMultisetBenchmark
specifier|public
class|class
name|ConcurrentHashMultisetBenchmark
block|{
annotation|@
name|Param
argument_list|(
block|{
literal|"1"
block|,
literal|"2"
block|,
literal|"4"
block|,
literal|"8"
block|}
argument_list|)
DECL|field|threads
name|int
name|threads
decl_stmt|;
annotation|@
name|Param
argument_list|(
block|{
literal|"3"
block|,
literal|"30"
block|,
literal|"300"
block|}
argument_list|)
DECL|field|size
name|int
name|size
decl_stmt|;
DECL|field|implSupplier
annotation|@
name|Param
name|MultisetSupplier
name|implSupplier
decl_stmt|;
DECL|field|multiset
specifier|private
name|Multiset
argument_list|<
name|Integer
argument_list|>
name|multiset
decl_stmt|;
DECL|field|keys
specifier|private
name|ImmutableList
argument_list|<
name|Integer
argument_list|>
name|keys
decl_stmt|;
DECL|field|threadPool
specifier|private
name|ExecutorService
name|threadPool
decl_stmt|;
annotation|@
name|BeforeExperiment
DECL|method|setUp ()
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|multiset
operator|=
name|implSupplier
operator|.
name|get
argument_list|()
expr_stmt|;
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|Integer
argument_list|>
name|builder
init|=
name|ImmutableList
operator|.
name|builder
argument_list|()
decl_stmt|;
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
name|builder
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|keys
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
name|threadPool
operator|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|threads
argument_list|,
operator|new
name|ThreadFactoryBuilder
argument_list|()
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|add (final int reps)
name|long
name|add
parameter_list|(
specifier|final
name|int
name|reps
parameter_list|)
throws|throws
name|ExecutionException
throws|,
name|InterruptedException
block|{
return|return
name|doMultithreadedLoop
argument_list|(
operator|new
name|Callable
argument_list|<
name|Long
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Long
name|call
parameter_list|()
block|{
return|return
name|runAddSingleThread
argument_list|(
name|reps
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
annotation|@
name|Benchmark
DECL|method|addRemove (final int reps)
name|long
name|addRemove
parameter_list|(
specifier|final
name|int
name|reps
parameter_list|)
throws|throws
name|ExecutionException
throws|,
name|InterruptedException
block|{
return|return
name|doMultithreadedLoop
argument_list|(
operator|new
name|Callable
argument_list|<
name|Long
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Long
name|call
parameter_list|()
block|{
return|return
name|runAddRemoveSingleThread
argument_list|(
name|reps
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|doMultithreadedLoop (Callable<Long> task)
specifier|private
name|long
name|doMultithreadedLoop
parameter_list|(
name|Callable
argument_list|<
name|Long
argument_list|>
name|task
parameter_list|)
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
name|List
argument_list|<
name|Future
argument_list|<
name|Long
argument_list|>
argument_list|>
name|futures
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|threads
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|threads
condition|;
name|i
operator|++
control|)
block|{
name|futures
operator|.
name|add
argument_list|(
name|threadPool
operator|.
name|submit
argument_list|(
name|task
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|long
name|total
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Future
argument_list|<
name|Long
argument_list|>
name|future
range|:
name|futures
control|)
block|{
name|total
operator|+=
name|future
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
return|return
name|total
return|;
block|}
DECL|method|runAddSingleThread (int reps)
specifier|private
name|long
name|runAddSingleThread
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|Random
name|random
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|int
name|nKeys
init|=
name|keys
operator|.
name|size
argument_list|()
decl_stmt|;
name|long
name|blah
init|=
literal|0
decl_stmt|;
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
name|Integer
name|key
init|=
name|keys
operator|.
name|get
argument_list|(
name|random
operator|.
name|nextInt
argument_list|(
name|nKeys
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|delta
init|=
name|random
operator|.
name|nextInt
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|blah
operator|+=
name|delta
expr_stmt|;
name|multiset
operator|.
name|add
argument_list|(
name|key
argument_list|,
name|delta
argument_list|)
expr_stmt|;
block|}
return|return
name|blah
return|;
block|}
DECL|method|runAddRemoveSingleThread (int reps)
specifier|private
name|long
name|runAddRemoveSingleThread
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|Random
name|random
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|int
name|nKeys
init|=
name|keys
operator|.
name|size
argument_list|()
decl_stmt|;
name|long
name|blah
init|=
literal|0
decl_stmt|;
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
name|Integer
name|key
init|=
name|keys
operator|.
name|get
argument_list|(
name|random
operator|.
name|nextInt
argument_list|(
name|nKeys
argument_list|)
argument_list|)
decl_stmt|;
comment|// This range is [-5, 4] - slight negative bias so we often hit zero, which brings the
comment|// auto-removal of zeroes into play.
name|int
name|delta
init|=
name|random
operator|.
name|nextInt
argument_list|(
literal|10
argument_list|)
operator|-
literal|5
decl_stmt|;
name|blah
operator|+=
name|delta
expr_stmt|;
if|if
condition|(
name|delta
operator|>=
literal|0
condition|)
block|{
name|multiset
operator|.
name|add
argument_list|(
name|key
argument_list|,
name|delta
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|multiset
operator|.
name|remove
argument_list|(
name|key
argument_list|,
operator|-
name|delta
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|blah
return|;
block|}
DECL|enum|MultisetSupplier
specifier|private
enum|enum
name|MultisetSupplier
block|{
DECL|enumConstant|CONCURRENT_HASH_MULTISET
DECL|method|CONCURRENT_HASH_MULTISET ()
name|CONCURRENT_HASH_MULTISET
parameter_list|()
block|{
annotation|@
name|Override
name|Multiset
argument_list|<
name|Integer
argument_list|>
name|get
parameter_list|()
block|{
return|return
name|ConcurrentHashMultiset
operator|.
name|create
argument_list|()
return|;
block|}
block|}
block|,
DECL|enumConstant|BOXED_ATOMIC_REPLACE
DECL|method|BOXED_ATOMIC_REPLACE ()
name|BOXED_ATOMIC_REPLACE
parameter_list|()
block|{
annotation|@
name|Override
name|Multiset
argument_list|<
name|Integer
argument_list|>
name|get
parameter_list|()
block|{
return|return
name|OldConcurrentHashMultiset
operator|.
name|create
argument_list|()
return|;
block|}
block|}
block|,
DECL|enumConstant|SYNCHRONIZED_MULTISET
DECL|method|SYNCHRONIZED_MULTISET ()
name|SYNCHRONIZED_MULTISET
parameter_list|()
block|{
annotation|@
name|Override
name|Multiset
argument_list|<
name|Integer
argument_list|>
name|get
parameter_list|()
block|{
return|return
name|Synchronized
operator|.
name|multiset
argument_list|(
name|HashMultiset
operator|.
expr|<
name|Integer
operator|>
name|create
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
block|,     ;
DECL|method|get ()
specifier|abstract
name|Multiset
argument_list|<
name|Integer
argument_list|>
name|get
parameter_list|()
function_decl|;
block|}
comment|/**    * Duplication of the old version of ConcurrentHashMultiset (with some unused stuff removed, like    * serialization code) which used a map with boxed integers for the values.    */
DECL|class|OldConcurrentHashMultiset
specifier|private
specifier|static
specifier|final
class|class
name|OldConcurrentHashMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractMultiset
argument_list|<
name|E
argument_list|>
block|{
comment|/** The number of occurrences of each element. */
DECL|field|countMap
specifier|private
specifier|final
specifier|transient
name|ConcurrentMap
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
name|countMap
decl_stmt|;
comment|/**      * Creates a new, empty {@code OldConcurrentHashMultiset} using the default initial capacity,      * load factor, and concurrency settings.      */
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|OldConcurrentHashMultiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|OldConcurrentHashMultiset
argument_list|<
name|E
argument_list|>
argument_list|(
operator|new
name|ConcurrentHashMap
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|OldConcurrentHashMultiset (ConcurrentMap<E, Integer> countMap)
name|OldConcurrentHashMultiset
parameter_list|(
name|ConcurrentMap
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
name|countMap
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|countMap
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|countMap
operator|=
name|countMap
expr_stmt|;
block|}
comment|// Query Operations
comment|/**      * Returns the number of occurrences of {@code element} in this multiset.      *      * @param element the element to look for      * @return the nonnegative number of occurrences of the element      */
annotation|@
name|Override
DECL|method|count (@heckForNull Object element)
specifier|public
name|int
name|count
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|element
parameter_list|)
block|{
try|try
block|{
return|return
name|unbox
argument_list|(
name|countMap
operator|.
name|get
argument_list|(
name|element
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
decl||
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
block|}
comment|/**      * {@inheritDoc}      *      *<p>If the data in the multiset is modified by any other threads during this method, it is      * undefined which (if any) of these modifications will be reflected in the result.      */
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
name|long
name|sum
init|=
literal|0L
decl_stmt|;
for|for
control|(
name|Integer
name|value
range|:
name|countMap
operator|.
name|values
argument_list|()
control|)
block|{
name|sum
operator|+=
name|value
expr_stmt|;
block|}
return|return
name|Ints
operator|.
name|saturatedCast
argument_list|(
name|sum
argument_list|)
return|;
block|}
comment|/*      * Note: the superclass toArray() methods assume that size() gives a correct      * answer, which ours does not.      */
annotation|@
name|Override
DECL|method|toArray ()
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|snapshot
argument_list|()
operator|.
name|toArray
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toArray (T[] array)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|toArray
parameter_list|(
name|T
index|[]
name|array
parameter_list|)
block|{
return|return
name|snapshot
argument_list|()
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
return|;
block|}
comment|/*      * We'd love to use 'new ArrayList(this)' or 'list.addAll(this)', but      * either of these would recurse back to us again!      */
DECL|method|snapshot ()
specifier|private
name|List
argument_list|<
name|E
argument_list|>
name|snapshot
parameter_list|()
block|{
name|List
argument_list|<
name|E
argument_list|>
name|list
init|=
name|Lists
operator|.
name|newArrayListWithExpectedSize
argument_list|(
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
range|:
name|entrySet
argument_list|()
control|)
block|{
name|E
name|element
init|=
name|entry
operator|.
name|getElement
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|entry
operator|.
name|getCount
argument_list|()
init|;
name|i
operator|>
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|list
return|;
block|}
comment|// Modification Operations
comment|/**      * Adds a number of occurrences of the specified element to this multiset.      *      * @param element the element to add      * @param occurrences the number of occurrences to add      * @return the previous count of the element before the operation; possibly zero      * @throws IllegalArgumentException if {@code occurrences} is negative, or if the resulting      *     amount would exceed {@link Integer#MAX_VALUE}      */
annotation|@
name|Override
DECL|method|add (E element, int occurrences)
specifier|public
name|int
name|add
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
if|if
condition|(
name|occurrences
operator|==
literal|0
condition|)
block|{
return|return
name|count
argument_list|(
name|element
argument_list|)
return|;
block|}
name|checkArgument
argument_list|(
name|occurrences
operator|>
literal|0
argument_list|,
literal|"Invalid occurrences: %s"
argument_list|,
name|occurrences
argument_list|)
expr_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|current
init|=
name|count
argument_list|(
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|current
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|countMap
operator|.
name|putIfAbsent
argument_list|(
name|element
argument_list|,
name|occurrences
argument_list|)
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
block|}
else|else
block|{
name|checkArgument
argument_list|(
name|occurrences
operator|<=
name|Integer
operator|.
name|MAX_VALUE
operator|-
name|current
argument_list|,
literal|"Overflow adding %s occurrences to a count of %s"
argument_list|,
name|occurrences
argument_list|,
name|current
argument_list|)
expr_stmt|;
name|int
name|next
init|=
name|current
operator|+
name|occurrences
decl_stmt|;
if|if
condition|(
name|countMap
operator|.
name|replace
argument_list|(
name|element
argument_list|,
name|current
argument_list|,
name|next
argument_list|)
condition|)
block|{
return|return
name|current
return|;
block|}
block|}
comment|// If we're still here, there was a race, so just try again.
block|}
block|}
comment|/**      * Removes a number of occurrences of the specified element from this multiset. If the multiset      * contains fewer than this number of occurrences to begin with, all occurrences will be      * removed.      *      * @param element the element whose occurrences should be removed      * @param occurrences the number of occurrences of the element to remove      * @return the count of the element before the operation; possibly zero      * @throws IllegalArgumentException if {@code occurrences} is negative      */
annotation|@
name|Override
DECL|method|remove (@heckForNull Object element, int occurrences)
specifier|public
name|int
name|remove
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
if|if
condition|(
name|occurrences
operator|==
literal|0
condition|)
block|{
return|return
name|count
argument_list|(
name|element
argument_list|)
return|;
block|}
name|checkArgument
argument_list|(
name|occurrences
operator|>
literal|0
argument_list|,
literal|"Invalid occurrences: %s"
argument_list|,
name|occurrences
argument_list|)
expr_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|current
init|=
name|count
argument_list|(
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|current
operator|==
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
if|if
condition|(
name|occurrences
operator|>=
name|current
condition|)
block|{
if|if
condition|(
name|countMap
operator|.
name|remove
argument_list|(
name|element
argument_list|,
name|current
argument_list|)
condition|)
block|{
return|return
name|current
return|;
block|}
block|}
else|else
block|{
comment|// We know it's an "E" because it already exists in the map.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|E
name|casted
init|=
operator|(
name|E
operator|)
name|element
decl_stmt|;
if|if
condition|(
name|countMap
operator|.
name|replace
argument_list|(
name|casted
argument_list|,
name|current
argument_list|,
name|current
operator|-
name|occurrences
argument_list|)
condition|)
block|{
return|return
name|current
return|;
block|}
block|}
comment|// If we're still here, there was a race, so just try again.
block|}
block|}
comment|/**      * Removes<b>all</b> occurrences of the specified element from this multiset. This method      * complements {@link Multiset#remove(Object)}, which removes only one occurrence at a time.      *      * @param element the element whose occurrences should all be removed      * @return the number of occurrences successfully removed, possibly zero      */
DECL|method|removeAllOccurrences (@heckForNull Object element)
specifier|private
name|int
name|removeAllOccurrences
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|element
parameter_list|)
block|{
try|try
block|{
return|return
name|unbox
argument_list|(
name|countMap
operator|.
name|remove
argument_list|(
name|element
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
decl||
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
block|}
comment|/**      * Removes exactly the specified number of occurrences of {@code element}, or makes no change if      * this is not possible.      *      *<p>This method, in contrast to {@link #remove(Object, int)}, has no effect when the element      * count is smaller than {@code occurrences}.      *      * @param element the element to remove      * @param occurrences the number of occurrences of {@code element} to remove      * @return {@code true} if the removal was possible (including if {@code occurrences} is zero)      */
DECL|method|removeExactly (@heckForNull Object element, int occurrences)
specifier|public
name|boolean
name|removeExactly
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
if|if
condition|(
name|occurrences
operator|==
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
name|checkArgument
argument_list|(
name|occurrences
operator|>
literal|0
argument_list|,
literal|"Invalid occurrences: %s"
argument_list|,
name|occurrences
argument_list|)
expr_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|current
init|=
name|count
argument_list|(
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|occurrences
operator|>
name|current
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|occurrences
operator|==
name|current
condition|)
block|{
if|if
condition|(
name|countMap
operator|.
name|remove
argument_list|(
name|element
argument_list|,
name|occurrences
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
else|else
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// it's in the map, must be an "E"
name|E
name|casted
init|=
operator|(
name|E
operator|)
name|element
decl_stmt|;
if|if
condition|(
name|countMap
operator|.
name|replace
argument_list|(
name|casted
argument_list|,
name|current
argument_list|,
name|current
operator|-
name|occurrences
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
comment|// If we're still here, there was a race, so just try again.
block|}
block|}
comment|/**      * Adds or removes occurrences of {@code element} such that the {@link #count} of the element      * becomes {@code count}.      *      * @return the count of {@code element} in the multiset before this call      * @throws IllegalArgumentException if {@code count} is negative      */
annotation|@
name|Override
DECL|method|setCount (E element, int count)
specifier|public
name|int
name|setCount
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|checkNonnegative
argument_list|(
name|count
argument_list|,
literal|"count"
argument_list|)
expr_stmt|;
return|return
operator|(
name|count
operator|==
literal|0
operator|)
condition|?
name|removeAllOccurrences
argument_list|(
name|element
argument_list|)
else|:
name|unbox
argument_list|(
name|countMap
operator|.
name|put
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Sets the number of occurrences of {@code element} to {@code newCount}, but only if the count      * is currently {@code oldCount}. If {@code element} does not appear in the multiset exactly      * {@code oldCount} times, no changes will be made.      *      * @return {@code true} if the change was successful. This usually indicates that the multiset      *     has been modified, but not always: in the case that {@code oldCount == newCount}, the      *     method will return {@code true} if the condition was met.      * @throws IllegalArgumentException if {@code oldCount} or {@code newCount} is negative      */
annotation|@
name|Override
DECL|method|setCount (E element, int oldCount, int newCount)
specifier|public
name|boolean
name|setCount
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|oldCount
parameter_list|,
name|int
name|newCount
parameter_list|)
block|{
name|checkNonnegative
argument_list|(
name|oldCount
argument_list|,
literal|"oldCount"
argument_list|)
expr_stmt|;
name|checkNonnegative
argument_list|(
name|newCount
argument_list|,
literal|"newCount"
argument_list|)
expr_stmt|;
if|if
condition|(
name|newCount
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|oldCount
operator|==
literal|0
condition|)
block|{
comment|// No change to make, but must return true if the element is not present
return|return
operator|!
name|countMap
operator|.
name|containsKey
argument_list|(
name|element
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|countMap
operator|.
name|remove
argument_list|(
name|element
argument_list|,
name|oldCount
argument_list|)
return|;
block|}
block|}
if|if
condition|(
name|oldCount
operator|==
literal|0
condition|)
block|{
return|return
name|countMap
operator|.
name|putIfAbsent
argument_list|(
name|element
argument_list|,
name|newCount
argument_list|)
operator|==
literal|null
return|;
block|}
return|return
name|countMap
operator|.
name|replace
argument_list|(
name|element
argument_list|,
name|oldCount
argument_list|,
name|newCount
argument_list|)
return|;
block|}
comment|// Views
annotation|@
name|Override
DECL|method|createElementSet ()
name|Set
argument_list|<
name|E
argument_list|>
name|createElementSet
parameter_list|()
block|{
specifier|final
name|Set
argument_list|<
name|E
argument_list|>
name|delegate
init|=
name|countMap
operator|.
name|keySet
argument_list|()
decl_stmt|;
return|return
operator|new
name|ForwardingSet
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Set
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
annotation|@
name|Override
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
try|try
block|{
return|return
name|delegate
operator|.
name|remove
argument_list|(
name|object
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
decl||
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|elementIterator ()
name|Iterator
argument_list|<
name|E
argument_list|>
name|elementIterator
parameter_list|()
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"should never be called"
argument_list|)
throw|;
block|}
DECL|field|entrySet
specifier|private
specifier|transient
name|EntrySet
name|entrySet
decl_stmt|;
annotation|@
name|Override
DECL|method|entrySet ()
specifier|public
name|Set
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
name|EntrySet
name|result
init|=
name|entrySet
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|entrySet
operator|=
name|result
operator|=
operator|new
name|EntrySet
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|distinctElements ()
name|int
name|distinctElements
parameter_list|()
block|{
return|return
name|countMap
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|countMap
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|entryIterator ()
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entryIterator
parameter_list|()
block|{
specifier|final
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|backingIterator
init|=
name|countMap
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
operator|new
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|backingIterator
operator|.
name|hasNext
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
name|next
parameter_list|()
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
name|backingEntry
init|=
name|backingIterator
operator|.
name|next
argument_list|()
decl_stmt|;
return|return
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|backingEntry
operator|.
name|getKey
argument_list|()
argument_list|,
name|backingEntry
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|backingIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Multisets
operator|.
name|iteratorImpl
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|countMap
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|class|EntrySet
specifier|private
class|class
name|EntrySet
extends|extends
name|AbstractMultiset
argument_list|<
name|E
argument_list|>
operator|.
name|EntrySet
block|{
annotation|@
name|Override
DECL|method|multiset ()
name|Multiset
argument_list|<
name|E
argument_list|>
name|multiset
parameter_list|()
block|{
return|return
name|OldConcurrentHashMultiset
operator|.
name|this
return|;
block|}
comment|/*        * Note: the superclass toArray() methods assume that size() gives a correct        * answer, which ours does not.        */
annotation|@
name|Override
DECL|method|toArray ()
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|snapshot
argument_list|()
operator|.
name|toArray
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toArray (T[] array)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|toArray
parameter_list|(
name|T
index|[]
name|array
parameter_list|)
block|{
return|return
name|snapshot
argument_list|()
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
return|;
block|}
DECL|method|snapshot ()
specifier|private
name|List
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|snapshot
parameter_list|()
block|{
name|List
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|list
init|=
name|Lists
operator|.
name|newArrayListWithExpectedSize
argument_list|(
name|size
argument_list|()
argument_list|)
decl_stmt|;
comment|// not Iterables.addAll(list, this), because that'll forward back here
name|Iterators
operator|.
name|addAll
argument_list|(
name|list
argument_list|,
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|list
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
if|if
condition|(
name|object
operator|instanceof
name|Multiset
operator|.
name|Entry
condition|)
block|{
name|Multiset
operator|.
name|Entry
argument_list|<
name|?
argument_list|>
name|entry
init|=
operator|(
name|Multiset
operator|.
name|Entry
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
name|Object
name|element
init|=
name|entry
operator|.
name|getElement
argument_list|()
decl_stmt|;
name|int
name|entryCount
init|=
name|entry
operator|.
name|getCount
argument_list|()
decl_stmt|;
return|return
name|countMap
operator|.
name|remove
argument_list|(
name|element
argument_list|,
name|entryCount
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/** The hash code is the same as countMap's, though the objects aren't equal. */
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|countMap
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
comment|/** We use a special form of unboxing that treats null as zero. */
DECL|method|unbox (@heckForNull Integer i)
specifier|private
specifier|static
name|int
name|unbox
parameter_list|(
annotation|@
name|CheckForNull
name|Integer
name|i
parameter_list|)
block|{
return|return
operator|(
name|i
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|i
return|;
block|}
block|}
block|}
end_class

end_unit

