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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
operator|.
name|newArrayList
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
name|collect
operator|.
name|CollectionBenchmarkSampleData
operator|.
name|Element
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
name|Collections
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
name|Map
operator|.
name|Entry
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
name|ConcurrentSkipListMap
import|;
end_import

begin_comment
comment|/**  * A microbenchmark that tests the performance of get() and iteration on various map  * implementations.  Forked from {@link SetContainsBenchmark}.  *  * @author Nicholaus Shupe  */
end_comment

begin_class
DECL|class|MapBenchmark
specifier|public
class|class
name|MapBenchmark
block|{
annotation|@
name|Param
argument_list|(
block|{
literal|"Hash"
block|,
literal|"LinkedHM"
block|,
literal|"MapMaker1"
block|,
literal|"Immutable"
block|}
argument_list|)
DECL|field|impl
specifier|private
name|Impl
name|impl
decl_stmt|;
DECL|enum|Impl
specifier|public
enum|enum
name|Impl
block|{
DECL|enumConstant|Hash
name|Hash
block|{
annotation|@
name|Override
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|Element
argument_list|>
name|keys
parameter_list|)
block|{
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|map
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Element
name|element
range|:
name|keys
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|element
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
block|}
block|,
DECL|enumConstant|LinkedHM
name|LinkedHM
block|{
annotation|@
name|Override
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|Element
argument_list|>
name|keys
parameter_list|)
block|{
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|map
init|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Element
name|element
range|:
name|keys
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|element
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
block|}
block|,
DECL|enumConstant|UnmodHM
name|UnmodHM
block|{
annotation|@
name|Override
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|Element
argument_list|>
name|keys
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|Hash
operator|.
name|create
argument_list|(
name|keys
argument_list|)
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|SyncHM
name|SyncHM
block|{
annotation|@
name|Override
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|Element
argument_list|>
name|keys
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|synchronizedMap
argument_list|(
name|Hash
operator|.
name|create
argument_list|(
name|keys
argument_list|)
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|Tree
name|Tree
block|{
annotation|@
name|Override
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|Element
argument_list|>
name|keys
parameter_list|)
block|{
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|map
init|=
name|Maps
operator|.
name|newTreeMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Element
name|element
range|:
name|keys
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|element
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
block|}
block|,
DECL|enumConstant|SkipList
name|SkipList
block|{
annotation|@
name|Override
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|Element
argument_list|>
name|keys
parameter_list|)
block|{
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|map
init|=
operator|new
name|ConcurrentSkipListMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Element
name|element
range|:
name|keys
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|element
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
block|}
block|,
DECL|enumConstant|ConcurrentHM1
name|ConcurrentHM1
block|{
annotation|@
name|Override
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|Element
argument_list|>
name|keys
parameter_list|)
block|{
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|map
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|(
name|keys
operator|.
name|size
argument_list|()
argument_list|,
literal|0.75f
argument_list|,
literal|1
argument_list|)
decl_stmt|;
for|for
control|(
name|Element
name|element
range|:
name|keys
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|element
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
block|}
block|,
DECL|enumConstant|ConcurrentHM16
name|ConcurrentHM16
block|{
annotation|@
name|Override
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|Element
argument_list|>
name|keys
parameter_list|)
block|{
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|map
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|(
name|keys
operator|.
name|size
argument_list|()
argument_list|,
literal|0.75f
argument_list|,
literal|16
argument_list|)
decl_stmt|;
for|for
control|(
name|Element
name|element
range|:
name|keys
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|element
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
block|}
block|,
DECL|enumConstant|MapMaker1
name|MapMaker1
block|{
annotation|@
name|Override
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|Element
argument_list|>
name|keys
parameter_list|)
block|{
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|map
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|concurrencyLevel
argument_list|(
literal|1
argument_list|)
operator|.
name|makeMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Element
name|element
range|:
name|keys
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|element
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
block|}
block|,
DECL|enumConstant|MapMaker16
name|MapMaker16
block|{
annotation|@
name|Override
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|Element
argument_list|>
name|keys
parameter_list|)
block|{
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|map
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|concurrencyLevel
argument_list|(
literal|16
argument_list|)
operator|.
name|makeMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Element
name|element
range|:
name|keys
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|element
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
block|}
block|,
DECL|enumConstant|Immutable
name|Immutable
block|{
annotation|@
name|Override
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|Element
argument_list|>
name|keys
parameter_list|)
block|{
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|builder
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Element
name|element
range|:
name|keys
control|)
block|{
name|builder
operator|.
name|put
argument_list|(
name|element
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
block|}
block|,
DECL|enumConstant|ImmutableSorted
name|ImmutableSorted
block|{
annotation|@
name|Override
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|Element
argument_list|>
name|keys
parameter_list|)
block|{
name|ImmutableSortedMap
operator|.
name|Builder
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|builder
init|=
name|ImmutableSortedMap
operator|.
name|naturalOrder
argument_list|()
decl_stmt|;
for|for
control|(
name|Element
name|element
range|:
name|keys
control|)
block|{
name|builder
operator|.
name|put
argument_list|(
name|element
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
block|}
block|;
DECL|method|create (Collection<Element> contents)
specifier|abstract
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|Element
argument_list|>
name|contents
parameter_list|)
function_decl|;
block|}
annotation|@
name|Param
argument_list|(
block|{
literal|"5"
block|,
literal|"50"
block|,
literal|"500"
block|,
literal|"5000"
block|,
literal|"50000"
block|}
argument_list|)
DECL|field|size
specifier|private
name|int
name|size
decl_stmt|;
comment|// TODO: look at exact (==) hits vs. equals() hits?
annotation|@
name|Param
argument_list|(
literal|"0.9"
argument_list|)
DECL|field|hitRate
specifier|private
name|double
name|hitRate
decl_stmt|;
annotation|@
name|Param
argument_list|(
literal|"true"
argument_list|)
DECL|field|isUserTypeFast
specifier|private
name|boolean
name|isUserTypeFast
decl_stmt|;
comment|// "" means no fixed seed
annotation|@
name|Param
argument_list|(
literal|""
argument_list|)
DECL|field|random
specifier|private
name|SpecialRandom
name|random
decl_stmt|;
annotation|@
name|Param
argument_list|(
literal|"false"
argument_list|)
DECL|field|sortedData
specifier|private
name|boolean
name|sortedData
decl_stmt|;
comment|// the following must be set during setUp
DECL|field|queries
specifier|private
name|Element
index|[]
name|queries
decl_stmt|;
DECL|field|mapToTest
specifier|private
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|mapToTest
decl_stmt|;
DECL|field|values
specifier|private
name|Collection
argument_list|<
name|Element
argument_list|>
name|values
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|BeforeExperiment
name|void
name|setUp
parameter_list|()
block|{
name|CollectionBenchmarkSampleData
name|sampleData
init|=
operator|new
name|CollectionBenchmarkSampleData
argument_list|(
name|isUserTypeFast
argument_list|,
name|random
argument_list|,
name|hitRate
argument_list|,
name|size
argument_list|)
decl_stmt|;
if|if
condition|(
name|sortedData
condition|)
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|valueList
init|=
name|newArrayList
argument_list|(
name|sampleData
operator|.
name|getValuesInSet
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|valueList
argument_list|)
expr_stmt|;
name|values
operator|=
name|valueList
expr_stmt|;
block|}
else|else
block|{
name|values
operator|=
name|sampleData
operator|.
name|getValuesInSet
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|mapToTest
operator|=
name|impl
operator|.
name|create
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|this
operator|.
name|queries
operator|=
name|sampleData
operator|.
name|getQueries
argument_list|()
expr_stmt|;
block|}
DECL|method|get (int reps)
annotation|@
name|Benchmark
name|boolean
name|get
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
comment|// Paranoia: acting on hearsay that accessing fields might be slow
comment|// Should write a benchmark to test that!
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|map
init|=
name|mapToTest
decl_stmt|;
name|Element
index|[]
name|queries
init|=
name|this
operator|.
name|queries
decl_stmt|;
comment|// Allows us to use& instead of %, acting on hearsay that division
comment|// operators (/%) are disproportionately expensive; should test this too!
name|int
name|mask
init|=
name|queries
operator|.
name|length
operator|-
literal|1
decl_stmt|;
name|boolean
name|dummy
init|=
literal|false
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
name|dummy
operator|^=
name|map
operator|.
name|get
argument_list|(
name|queries
index|[
name|i
operator|&
name|mask
index|]
argument_list|)
operator|!=
literal|null
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
DECL|method|createAndPopulate (int reps)
annotation|@
name|Benchmark
name|int
name|createAndPopulate
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|dummy
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
name|dummy
operator|+=
name|impl
operator|.
name|create
argument_list|(
name|values
argument_list|)
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
DECL|method|iterateWithEntrySet (int reps)
annotation|@
name|Benchmark
name|boolean
name|iterateWithEntrySet
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|map
init|=
name|mapToTest
decl_stmt|;
name|boolean
name|dummy
init|=
literal|false
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
for|for
control|(
name|Entry
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|dummy
operator|^=
name|entry
operator|.
name|getKey
argument_list|()
operator|!=
name|entry
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|dummy
return|;
block|}
DECL|method|iterateWithKeySetAndGet (int reps)
annotation|@
name|Benchmark
name|boolean
name|iterateWithKeySetAndGet
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|map
init|=
name|mapToTest
decl_stmt|;
name|boolean
name|dummy
init|=
literal|false
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
for|for
control|(
name|Element
name|key
range|:
name|map
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Element
name|value
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|dummy
operator|^=
name|key
operator|!=
name|value
expr_stmt|;
block|}
block|}
return|return
name|dummy
return|;
block|}
DECL|method|iterateValuesAndGet (int reps)
annotation|@
name|Benchmark
name|boolean
name|iterateValuesAndGet
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|map
init|=
name|mapToTest
decl_stmt|;
name|boolean
name|dummy
init|=
literal|false
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
for|for
control|(
name|Element
name|key
range|:
name|map
operator|.
name|values
argument_list|()
control|)
block|{
comment|// This normally wouldn't make sense, but because our keys are our values it kind of does
name|Element
name|value
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|dummy
operator|^=
name|key
operator|!=
name|value
expr_stmt|;
block|}
block|}
return|return
name|dummy
return|;
block|}
block|}
end_class

end_unit

