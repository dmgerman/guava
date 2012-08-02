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
name|collect
operator|.
name|MapMakerInternalMap
operator|.
name|DRAIN_THRESHOLD
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
name|MapMakerInternalMapTest
operator|.
name|SMALL_MAX_SIZE
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
name|MapMakerInternalMapTest
operator|.
name|allEvictingMakers
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
name|MapMakerInternalMapTest
operator|.
name|assertNotified
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
name|MapMakerInternalMapTest
operator|.
name|checkAndDrainRecencyQueue
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
name|MapMakerInternalMapTest
operator|.
name|checkEvictionQueues
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
name|MapMakerInternalMapTest
operator|.
name|checkExpirationTimes
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|MapMaker
operator|.
name|ComputingMapAdapter
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
name|MapMaker
operator|.
name|RemovalCause
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
name|MapMakerInternalMap
operator|.
name|ReferenceEntry
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
name|MapMakerInternalMap
operator|.
name|Segment
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
name|MapMakerInternalMapTest
operator|.
name|DummyEntry
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
name|MapMakerInternalMapTest
operator|.
name|DummyValueReference
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
name|MapMakerInternalMapTest
operator|.
name|QueuingRemovalListener
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
name|testing
operator|.
name|NullPointerTester
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|Random
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
name|CountDownLatch
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
name|TimeUnit
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
name|atomic
operator|.
name|AtomicInteger
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
name|atomic
operator|.
name|AtomicReferenceArray
import|;
end_import

begin_comment
comment|/**  * @author Charles Fry  */
end_comment

begin_class
DECL|class|ComputingConcurrentHashMapTest
specifier|public
class|class
name|ComputingConcurrentHashMapTest
extends|extends
name|TestCase
block|{
DECL|method|makeComputingMap ( MapMaker maker, Function<? super K, ? extends V> computingFunction)
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ComputingConcurrentHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makeComputingMap
parameter_list|(
name|MapMaker
name|maker
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|computingFunction
parameter_list|)
block|{
return|return
operator|new
name|ComputingConcurrentHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|maker
argument_list|,
name|computingFunction
argument_list|)
return|;
block|}
DECL|method|makeAdaptedMap ( MapMaker maker, Function<? super K, ? extends V> computingFunction)
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ComputingMapAdapter
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makeAdaptedMap
parameter_list|(
name|MapMaker
name|maker
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|computingFunction
parameter_list|)
block|{
return|return
operator|new
name|ComputingMapAdapter
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|maker
argument_list|,
name|computingFunction
argument_list|)
return|;
block|}
DECL|method|createMapMaker ()
specifier|private
name|MapMaker
name|createMapMaker
parameter_list|()
block|{
name|MapMaker
name|maker
init|=
operator|new
name|MapMaker
argument_list|()
decl_stmt|;
name|maker
operator|.
name|useCustomMap
operator|=
literal|true
expr_stmt|;
return|return
name|maker
return|;
block|}
comment|// constructor tests
DECL|method|testComputingFunction ()
specifier|public
name|void
name|testComputingFunction
parameter_list|()
block|{
name|Function
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|computingFunction
init|=
operator|new
name|Function
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|apply
parameter_list|(
name|Object
name|from
parameter_list|)
block|{
return|return
name|from
return|;
block|}
block|}
decl_stmt|;
name|ComputingConcurrentHashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|makeComputingMap
argument_list|(
name|createMapMaker
argument_list|()
argument_list|,
name|computingFunction
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|computingFunction
argument_list|,
name|map
operator|.
name|computingFunction
argument_list|)
expr_stmt|;
block|}
comment|// computation tests
DECL|method|testCompute ()
specifier|public
name|void
name|testCompute
parameter_list|()
throws|throws
name|ExecutionException
block|{
name|CountingFunction
name|computingFunction
init|=
operator|new
name|CountingFunction
argument_list|()
decl_stmt|;
name|ComputingConcurrentHashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|makeComputingMap
argument_list|(
name|createMapMaker
argument_list|()
argument_list|,
name|computingFunction
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|computingFunction
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|key
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|map
operator|.
name|getOrCompute
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|computingFunction
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
name|map
operator|.
name|getOrCompute
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|computingFunction
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testComputeNull ()
specifier|public
name|void
name|testComputeNull
parameter_list|()
block|{
name|Function
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|computingFunction
init|=
operator|new
name|ConstantLoader
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|ComputingMapAdapter
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|makeAdaptedMap
argument_list|(
name|createMapMaker
argument_list|()
argument_list|,
name|computingFunction
argument_list|)
decl_stmt|;
try|try
block|{
name|map
operator|.
name|get
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testRecordReadOnCompute ()
specifier|public
name|void
name|testRecordReadOnCompute
parameter_list|()
throws|throws
name|ExecutionException
block|{
name|CountingFunction
name|computingFunction
init|=
operator|new
name|CountingFunction
argument_list|()
decl_stmt|;
for|for
control|(
name|MapMaker
name|maker
range|:
name|allEvictingMakers
argument_list|()
control|)
block|{
name|ComputingConcurrentHashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|makeComputingMap
argument_list|(
name|maker
operator|.
name|concurrencyLevel
argument_list|(
literal|1
argument_list|)
argument_list|,
name|computingFunction
argument_list|)
decl_stmt|;
name|Segment
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|segment
init|=
name|map
operator|.
name|segments
index|[
literal|0
index|]
decl_stmt|;
name|List
argument_list|<
name|ReferenceEntry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|writeOrder
init|=
name|Lists
operator|.
name|newLinkedList
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ReferenceEntry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|readOrder
init|=
name|Lists
operator|.
name|newLinkedList
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
name|SMALL_MAX_SIZE
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|key
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|int
name|hash
init|=
name|map
operator|.
name|hash
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|map
operator|.
name|getOrCompute
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|ReferenceEntry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
init|=
name|segment
operator|.
name|getEntry
argument_list|(
name|key
argument_list|,
name|hash
argument_list|)
decl_stmt|;
name|writeOrder
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|readOrder
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
name|checkEvictionQueues
argument_list|(
name|map
argument_list|,
name|segment
argument_list|,
name|readOrder
argument_list|,
name|writeOrder
argument_list|)
expr_stmt|;
name|checkExpirationTimes
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|segment
operator|.
name|recencyQueue
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// access some of the elements
name|Random
name|random
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ReferenceEntry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|reads
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|ReferenceEntry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|i
init|=
name|readOrder
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ReferenceEntry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
init|=
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|random
operator|.
name|nextBoolean
argument_list|()
condition|)
block|{
name|map
operator|.
name|getOrCompute
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|reads
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|i
operator|.
name|remove
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|segment
operator|.
name|recencyQueue
operator|.
name|size
argument_list|()
operator|<=
name|DRAIN_THRESHOLD
argument_list|)
expr_stmt|;
block|}
block|}
name|int
name|undrainedIndex
init|=
name|reads
operator|.
name|size
argument_list|()
operator|-
name|segment
operator|.
name|recencyQueue
operator|.
name|size
argument_list|()
decl_stmt|;
name|checkAndDrainRecencyQueue
argument_list|(
name|map
argument_list|,
name|segment
argument_list|,
name|reads
operator|.
name|subList
argument_list|(
name|undrainedIndex
argument_list|,
name|reads
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|readOrder
operator|.
name|addAll
argument_list|(
name|reads
argument_list|)
expr_stmt|;
name|checkEvictionQueues
argument_list|(
name|map
argument_list|,
name|segment
argument_list|,
name|readOrder
argument_list|,
name|writeOrder
argument_list|)
expr_stmt|;
name|checkExpirationTimes
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testComputeExistingEntry ()
specifier|public
name|void
name|testComputeExistingEntry
parameter_list|()
throws|throws
name|ExecutionException
block|{
name|CountingFunction
name|computingFunction
init|=
operator|new
name|CountingFunction
argument_list|()
decl_stmt|;
name|ComputingConcurrentHashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|makeComputingMap
argument_list|(
name|createMapMaker
argument_list|()
argument_list|,
name|computingFunction
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|computingFunction
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|key
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
name|map
operator|.
name|getOrCompute
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|computingFunction
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testComputePartiallyCollectedKey ()
specifier|public
name|void
name|testComputePartiallyCollectedKey
parameter_list|()
throws|throws
name|ExecutionException
block|{
name|MapMaker
name|maker
init|=
name|createMapMaker
argument_list|()
operator|.
name|concurrencyLevel
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|CountingFunction
name|computingFunction
init|=
operator|new
name|CountingFunction
argument_list|()
decl_stmt|;
name|ComputingConcurrentHashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|makeComputingMap
argument_list|(
name|maker
argument_list|,
name|computingFunction
argument_list|)
decl_stmt|;
name|Segment
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|segment
init|=
name|map
operator|.
name|segments
index|[
literal|0
index|]
decl_stmt|;
name|AtomicReferenceArray
argument_list|<
name|ReferenceEntry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|table
init|=
name|segment
operator|.
name|table
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|computingFunction
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|key
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|int
name|hash
init|=
name|map
operator|.
name|hash
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|int
name|index
init|=
name|hash
operator|&
operator|(
name|table
operator|.
name|length
argument_list|()
operator|-
literal|1
operator|)
decl_stmt|;
name|DummyEntry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
init|=
name|DummyEntry
operator|.
name|create
argument_list|(
name|key
argument_list|,
name|hash
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|DummyValueReference
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|valueRef
init|=
name|DummyValueReference
operator|.
name|create
argument_list|(
name|value
argument_list|,
name|entry
argument_list|)
decl_stmt|;
name|entry
operator|.
name|setValueReference
argument_list|(
name|valueRef
argument_list|)
expr_stmt|;
name|table
operator|.
name|set
argument_list|(
name|index
argument_list|,
name|entry
argument_list|)
expr_stmt|;
name|segment
operator|.
name|count
operator|++
expr_stmt|;
name|assertSame
argument_list|(
name|value
argument_list|,
name|map
operator|.
name|getOrCompute
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|computingFunction
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|segment
operator|.
name|count
argument_list|)
expr_stmt|;
name|entry
operator|.
name|clearKey
argument_list|()
expr_stmt|;
name|assertNotSame
argument_list|(
name|value
argument_list|,
name|map
operator|.
name|getOrCompute
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|computingFunction
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|segment
operator|.
name|count
argument_list|)
expr_stmt|;
block|}
DECL|method|testComputePartiallyCollectedValue ()
specifier|public
name|void
name|testComputePartiallyCollectedValue
parameter_list|()
throws|throws
name|ExecutionException
block|{
name|MapMaker
name|maker
init|=
name|createMapMaker
argument_list|()
operator|.
name|concurrencyLevel
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|CountingFunction
name|computingFunction
init|=
operator|new
name|CountingFunction
argument_list|()
decl_stmt|;
name|ComputingConcurrentHashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|makeComputingMap
argument_list|(
name|maker
argument_list|,
name|computingFunction
argument_list|)
decl_stmt|;
name|Segment
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|segment
init|=
name|map
operator|.
name|segments
index|[
literal|0
index|]
decl_stmt|;
name|AtomicReferenceArray
argument_list|<
name|ReferenceEntry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|table
init|=
name|segment
operator|.
name|table
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|computingFunction
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|key
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|int
name|hash
init|=
name|map
operator|.
name|hash
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|int
name|index
init|=
name|hash
operator|&
operator|(
name|table
operator|.
name|length
argument_list|()
operator|-
literal|1
operator|)
decl_stmt|;
name|DummyEntry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
init|=
name|DummyEntry
operator|.
name|create
argument_list|(
name|key
argument_list|,
name|hash
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|DummyValueReference
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|valueRef
init|=
name|DummyValueReference
operator|.
name|create
argument_list|(
name|value
argument_list|,
name|entry
argument_list|)
decl_stmt|;
name|entry
operator|.
name|setValueReference
argument_list|(
name|valueRef
argument_list|)
expr_stmt|;
name|table
operator|.
name|set
argument_list|(
name|index
argument_list|,
name|entry
argument_list|)
expr_stmt|;
name|segment
operator|.
name|count
operator|++
expr_stmt|;
name|assertSame
argument_list|(
name|value
argument_list|,
name|map
operator|.
name|getOrCompute
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|computingFunction
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|segment
operator|.
name|count
argument_list|)
expr_stmt|;
name|valueRef
operator|.
name|clear
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|value
argument_list|,
name|map
operator|.
name|getOrCompute
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|computingFunction
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|segment
operator|.
name|count
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
comment|// test of deprecated method
DECL|method|testComputeExpiredEntry ()
specifier|public
name|void
name|testComputeExpiredEntry
parameter_list|()
throws|throws
name|ExecutionException
block|{
name|MapMaker
name|maker
init|=
name|createMapMaker
argument_list|()
operator|.
name|expireAfterWrite
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
decl_stmt|;
name|CountingFunction
name|computingFunction
init|=
operator|new
name|CountingFunction
argument_list|()
decl_stmt|;
name|ComputingConcurrentHashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|makeComputingMap
argument_list|(
name|maker
argument_list|,
name|computingFunction
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|computingFunction
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|key
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Object
name|one
init|=
name|map
operator|.
name|getOrCompute
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|computingFunction
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|two
init|=
name|map
operator|.
name|getOrCompute
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|one
argument_list|,
name|two
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|computingFunction
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRemovalListener_replaced ()
specifier|public
name|void
name|testRemovalListener_replaced
parameter_list|()
block|{
comment|// TODO(user): May be a good candidate to play with the MultithreadedTestCase
specifier|final
name|CountDownLatch
name|startSignal
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|computingSignal
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|doneSignal
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|computedObject
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Function
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|computingFunction
init|=
operator|new
name|Function
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|apply
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|computingSignal
operator|.
name|countDown
argument_list|()
expr_stmt|;
try|try
block|{
name|startSignal
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|computedObject
return|;
block|}
block|}
decl_stmt|;
name|QueuingRemovalListener
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|listener
init|=
operator|new
name|QueuingRemovalListener
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|MapMaker
name|maker
init|=
operator|(
name|MapMaker
operator|)
name|createMapMaker
argument_list|()
operator|.
name|removalListener
argument_list|(
name|listener
argument_list|)
decl_stmt|;
specifier|final
name|ComputingConcurrentHashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|makeComputingMap
argument_list|(
name|maker
argument_list|,
name|computingFunction
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|listener
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Object
name|one
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
specifier|final
name|Object
name|two
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
specifier|final
name|Object
name|three
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
operator|new
name|Thread
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|map
operator|.
name|getOrCompute
argument_list|(
name|one
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|doneSignal
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|computingSignal
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|map
operator|.
name|put
argument_list|(
name|one
argument_list|,
name|two
argument_list|)
expr_stmt|;
name|startSignal
operator|.
name|countDown
argument_list|()
expr_stmt|;
try|try
block|{
name|doneSignal
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|assertNotNull
argument_list|(
name|map
operator|.
name|putIfAbsent
argument_list|(
name|one
argument_list|,
name|three
argument_list|)
argument_list|)
expr_stmt|;
comment|// force notifications
name|assertNotified
argument_list|(
name|listener
argument_list|,
name|one
argument_list|,
name|computedObject
argument_list|,
name|RemovalCause
operator|.
name|REPLACED
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|listener
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// computing functions
DECL|class|CountingFunction
specifier|private
specifier|static
class|class
name|CountingFunction
implements|implements
name|Function
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
block|{
DECL|field|count
specifier|private
specifier|final
name|AtomicInteger
name|count
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|apply (Object from)
specifier|public
name|Object
name|apply
parameter_list|(
name|Object
name|from
parameter_list|)
block|{
name|count
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
return|return
operator|new
name|Object
argument_list|()
return|;
block|}
DECL|method|getCount ()
specifier|public
name|int
name|getCount
parameter_list|()
block|{
return|return
name|count
operator|.
name|get
argument_list|()
return|;
block|}
block|}
DECL|method|testNullParameters ()
specifier|public
name|void
name|testNullParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|NullPointerTester
name|tester
init|=
operator|new
name|NullPointerTester
argument_list|()
decl_stmt|;
name|Function
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|computingFunction
init|=
operator|new
name|IdentityLoader
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|tester
operator|.
name|testAllPublicInstanceMethods
argument_list|(
name|makeComputingMap
argument_list|(
name|createMapMaker
argument_list|()
argument_list|,
name|computingFunction
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|ConstantLoader
specifier|static
specifier|final
class|class
name|ConstantLoader
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Function
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|constant
specifier|private
specifier|final
name|V
name|constant
decl_stmt|;
DECL|method|ConstantLoader (V constant)
specifier|public
name|ConstantLoader
parameter_list|(
name|V
name|constant
parameter_list|)
block|{
name|this
operator|.
name|constant
operator|=
name|constant
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (K key)
specifier|public
name|V
name|apply
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|constant
return|;
block|}
block|}
DECL|class|IdentityLoader
specifier|static
specifier|final
class|class
name|IdentityLoader
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Function
argument_list|<
name|T
argument_list|,
name|T
argument_list|>
block|{
annotation|@
name|Override
DECL|method|apply (T key)
specifier|public
name|T
name|apply
parameter_list|(
name|T
name|key
parameter_list|)
block|{
return|return
name|key
return|;
block|}
block|}
block|}
end_class

end_unit

