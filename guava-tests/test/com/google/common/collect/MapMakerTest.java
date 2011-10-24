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
name|RemovalNotification
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
name|Map
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
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_comment
comment|/**  * @author Charles Fry  */
end_comment

begin_class
DECL|class|MapMakerTest
specifier|public
class|class
name|MapMakerTest
extends|extends
name|TestCase
block|{
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
name|tester
operator|.
name|testAllPublicInstanceMethods
argument_list|(
operator|new
name|MapMaker
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRemovalNotification_clear ()
specifier|public
name|void
name|testRemovalNotification_clear
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// If a clear() happens while a computation is pending, we should not get a removal
comment|// notification.
specifier|final
name|CountDownLatch
name|computingLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|computingFunction
init|=
operator|new
name|Function
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|apply
parameter_list|(
name|String
name|key
parameter_list|)
block|{
try|try
block|{
name|computingLatch
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
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
return|return
name|key
return|;
block|}
block|}
decl_stmt|;
name|QueuingRemovalListener
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|listener
init|=
operator|new
name|QueuingRemovalListener
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
comment|// test of deprecated code
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|String
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
name|removalListener
argument_list|(
name|listener
argument_list|)
operator|.
name|makeComputingMap
argument_list|(
name|computingFunction
argument_list|)
decl_stmt|;
comment|// seed the map, so its segment's count> 0
name|map
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
specifier|final
name|CountDownLatch
name|computationStarted
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|computationComplete
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|computationStarted
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|map
operator|.
name|get
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|computationComplete
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// wait for the computingEntry to be created
name|computationStarted
operator|.
name|await
argument_list|()
expr_stmt|;
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// let the computation proceed
name|computingLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
comment|// don't check map.size() until we know the get("b") call is complete
name|computationComplete
operator|.
name|await
argument_list|()
expr_stmt|;
comment|// At this point, the listener should be holding the seed value (a -> a), and the map should
comment|// contain the computed value (b -> b), since the clear() happened before the computation
comment|// completed.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|listener
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|RemovalNotification
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|notification
init|=
name|listener
operator|.
name|remove
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|notification
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|notification
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// "Basher tests", where we throw a bunch of stuff at a Cache and check basic invariants.
comment|/**    * This is a less carefully-controlled version of {@link #testRemovalNotification_clear} - this is    * a black-box test that tries to create lots of different thread-interleavings, and asserts that    * each computation is affected by a call to {@code clear()} (and therefore gets passed to the    * removal listener), or else is not affected by the {@code clear()} (and therefore exists in the    * map afterward).    */
DECL|method|testRemovalNotification_clear_basher ()
specifier|public
name|void
name|testRemovalNotification_clear_basher
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// If a clear() happens close to the end of computation, one of two things should happen:
comment|// - computation ends first: the removal listener is called, and the map does not contain the
comment|//   key/value pair
comment|// - clear() happens first: the removal listener is not called, and the map contains the pair
name|CountDownLatch
name|computationLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|QueuingRemovalListener
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|listener
init|=
operator|new
name|QueuingRemovalListener
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
comment|// test of deprecated code
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|removalListener
argument_list|(
name|listener
argument_list|)
operator|.
name|concurrencyLevel
argument_list|(
literal|20
argument_list|)
operator|.
name|makeComputingMap
argument_list|(
operator|new
name|DelayingIdentityLoader
argument_list|<
name|String
argument_list|>
argument_list|(
name|computationLatch
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|nThreads
init|=
literal|100
decl_stmt|;
name|int
name|nTasks
init|=
literal|1000
decl_stmt|;
name|int
name|nSeededEntries
init|=
literal|100
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|expectedKeys
init|=
name|Sets
operator|.
name|newHashSetWithExpectedSize
argument_list|(
name|nTasks
operator|+
name|nSeededEntries
argument_list|)
decl_stmt|;
comment|// seed the map, so its segments have a count>0; otherwise, clear() won't visit the in-progress
comment|// entries
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nSeededEntries
condition|;
name|i
operator|++
control|)
block|{
name|String
name|s
init|=
literal|"b"
operator|+
name|i
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|s
argument_list|,
name|s
argument_list|)
expr_stmt|;
name|expectedKeys
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
specifier|final
name|AtomicInteger
name|computedCount
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|ExecutorService
name|threadPool
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|nThreads
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|tasksFinished
init|=
operator|new
name|CountDownLatch
argument_list|(
name|nTasks
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
name|nTasks
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|String
name|s
init|=
literal|"a"
operator|+
name|i
decl_stmt|;
name|threadPool
operator|.
name|submit
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|map
operator|.
name|get
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|computedCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|tasksFinished
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|expectedKeys
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|computationLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
comment|// let some computations complete
while|while
condition|(
name|computedCount
operator|.
name|get
argument_list|()
operator|<
name|nThreads
condition|)
block|{
name|Thread
operator|.
name|yield
argument_list|()
expr_stmt|;
block|}
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
name|tasksFinished
operator|.
name|await
argument_list|()
expr_stmt|;
comment|// Check all of the removal notifications we received: they should have had correctly-associated
comment|// keys and values. (An earlier bug saw removal notifications for in-progress computations,
comment|// which had real keys with null values.)
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|removalNotifications
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|RemovalNotification
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|notification
range|:
name|listener
control|)
block|{
name|removalNotifications
operator|.
name|put
argument_list|(
name|notification
operator|.
name|getKey
argument_list|()
argument_list|,
name|notification
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected key/value pair passed to removalListener"
argument_list|,
name|notification
operator|.
name|getKey
argument_list|()
argument_list|,
name|notification
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// All of the seed values should have been visible, so we should have gotten removal
comment|// notifications for all of them.
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nSeededEntries
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|"b"
operator|+
name|i
argument_list|,
name|removalNotifications
operator|.
name|get
argument_list|(
literal|"b"
operator|+
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Each of the values added to the map should either still be there, or have seen a removal
comment|// notification.
name|assertEquals
argument_list|(
name|expectedKeys
argument_list|,
name|Sets
operator|.
name|union
argument_list|(
name|map
operator|.
name|keySet
argument_list|()
argument_list|,
name|removalNotifications
operator|.
name|keySet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Sets
operator|.
name|intersection
argument_list|(
name|map
operator|.
name|keySet
argument_list|()
argument_list|,
name|removalNotifications
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|DelayingIdentityLoader
specifier|static
specifier|final
class|class
name|DelayingIdentityLoader
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
DECL|field|delayLatch
specifier|private
specifier|final
name|CountDownLatch
name|delayLatch
decl_stmt|;
DECL|method|DelayingIdentityLoader (CountDownLatch delayLatch)
name|DelayingIdentityLoader
parameter_list|(
name|CountDownLatch
name|delayLatch
parameter_list|)
block|{
name|this
operator|.
name|delayLatch
operator|=
name|delayLatch
expr_stmt|;
block|}
DECL|method|apply (T key)
annotation|@
name|Override
specifier|public
name|T
name|apply
parameter_list|(
name|T
name|key
parameter_list|)
block|{
name|boolean
name|interrupted
init|=
literal|false
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
name|delayLatch
operator|.
name|await
argument_list|()
expr_stmt|;
break|break;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|interrupted
operator|=
literal|true
expr_stmt|;
block|}
block|}
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
return|return
name|key
return|;
block|}
block|}
block|}
end_class

end_unit

