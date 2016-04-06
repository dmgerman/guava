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
name|util
operator|.
name|concurrent
operator|.
name|Uninterruptibles
operator|.
name|awaitUninterruptibly
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|HOURS
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
name|CountDownLatch
import|;
end_import

begin_comment
comment|/**  * @author Charles Fry  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|MapMakerTest
specifier|public
class|class
name|MapMakerTest
extends|extends
name|TestCase
block|{
annotation|@
name|GwtIncompatible
comment|// NullPointerTester
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
annotation|@
name|GwtIncompatible
comment|// threads
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
name|DelayingIdentityLoader
argument_list|<
name|String
argument_list|>
argument_list|(
name|computingLatch
argument_list|)
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
annotation|@
name|GwtIncompatible
comment|// threads
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
name|awaitUninterruptibly
argument_list|(
name|delayLatch
argument_list|)
expr_stmt|;
return|return
name|key
return|;
block|}
block|}
comment|/*    * TODO(cpovirk): eliminate duplication between these tests and those in LegacyMapMakerTests and    * anywhere else    */
comment|/** Tests for the builder. */
DECL|class|MakerTest
specifier|public
specifier|static
class|class
name|MakerTest
extends|extends
name|TestCase
block|{
DECL|method|testInitialCapacity_negative ()
specifier|public
name|void
name|testInitialCapacity_negative
parameter_list|()
block|{
name|MapMaker
name|maker
init|=
operator|new
name|MapMaker
argument_list|()
decl_stmt|;
try|try
block|{
name|maker
operator|.
name|initialCapacity
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{       }
block|}
comment|// TODO(cpovirk): enable when ready
DECL|method|xtestInitialCapacity_setTwice ()
specifier|public
name|void
name|xtestInitialCapacity_setTwice
parameter_list|()
block|{
name|MapMaker
name|maker
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|initialCapacity
argument_list|(
literal|16
argument_list|)
decl_stmt|;
try|try
block|{
comment|// even to the same value is not allowed
name|maker
operator|.
name|initialCapacity
argument_list|(
literal|16
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{       }
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
comment|// test of deprecated method
DECL|method|testExpiration_setTwice ()
specifier|public
name|void
name|testExpiration_setTwice
parameter_list|()
block|{
name|MapMaker
name|maker
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|expireAfterWrite
argument_list|(
literal|1
argument_list|,
name|HOURS
argument_list|)
decl_stmt|;
try|try
block|{
comment|// even to the same value is not allowed
name|maker
operator|.
name|expireAfterWrite
argument_list|(
literal|1
argument_list|,
name|HOURS
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|expected
parameter_list|)
block|{       }
block|}
DECL|method|testMaximumSize_setTwice ()
specifier|public
name|void
name|testMaximumSize_setTwice
parameter_list|()
block|{
name|MapMaker
name|maker
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|maximumSize
argument_list|(
literal|16
argument_list|)
decl_stmt|;
try|try
block|{
comment|// even to the same value is not allowed
name|maker
operator|.
name|maximumSize
argument_list|(
literal|16
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|expected
parameter_list|)
block|{       }
block|}
DECL|method|testReturnsPlainConcurrentHashMapWhenPossible ()
specifier|public
name|void
name|testReturnsPlainConcurrentHashMapWhenPossible
parameter_list|()
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|initialCapacity
argument_list|(
literal|5
argument_list|)
operator|.
name|makeMap
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|map
operator|instanceof
name|ConcurrentHashMap
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Tests of the built map with maximumSize. */
DECL|class|MaximumSizeTest
specifier|public
specifier|static
class|class
name|MaximumSizeTest
extends|extends
name|TestCase
block|{
DECL|method|testPut_sizeIsZero ()
specifier|public
name|void
name|testPut_sizeIsZero
parameter_list|()
block|{
name|ConcurrentMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|maximumSize
argument_list|(
literal|0
argument_list|)
operator|.
name|makeMap
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSizeBasedEviction ()
specifier|public
name|void
name|testSizeBasedEviction
parameter_list|()
block|{
name|int
name|numKeys
init|=
literal|10
decl_stmt|;
name|int
name|mapSize
init|=
literal|5
decl_stmt|;
name|ConcurrentMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|maximumSize
argument_list|(
name|mapSize
argument_list|)
operator|.
name|makeMap
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
name|numKeys
condition|;
name|i
operator|++
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|i
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|mapSize
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|numKeys
operator|-
name|mapSize
init|;
name|i
operator|<
name|mapSize
condition|;
name|i
operator|++
control|)
block|{
name|assertTrue
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/** Tests for recursive computation. */
DECL|class|RecursiveComputationTest
specifier|public
specifier|static
class|class
name|RecursiveComputationTest
extends|extends
name|TestCase
block|{
DECL|field|recursiveComputer
name|Function
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|recursiveComputer
init|=
operator|new
name|Function
argument_list|<
name|Integer
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
name|Integer
name|key
parameter_list|)
block|{
if|if
condition|(
name|key
operator|>
literal|0
condition|)
block|{
return|return
name|key
operator|+
literal|", "
operator|+
name|recursiveMap
operator|.
name|get
argument_list|(
name|key
operator|-
literal|1
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|"0"
return|;
block|}
block|}
block|}
decl_stmt|;
DECL|field|recursiveMap
name|ConcurrentMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|recursiveMap
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|makeComputingMap
argument_list|(
name|recursiveComputer
argument_list|)
decl_stmt|;
DECL|method|testRecursiveComputation ()
specifier|public
name|void
name|testRecursiveComputation
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"3, 2, 1, 0"
argument_list|,
name|recursiveMap
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Tests for computing functionality.    */
DECL|class|ComputingTest
specifier|public
specifier|static
class|class
name|ComputingTest
extends|extends
name|TestCase
block|{
DECL|method|testComputerThatReturnsNull ()
specifier|public
name|void
name|testComputerThatReturnsNull
parameter_list|()
block|{
name|ConcurrentMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|makeComputingMap
argument_list|(
operator|new
name|Function
argument_list|<
name|Integer
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
name|Integer
name|key
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
argument_list|)
decl_stmt|;
try|try
block|{
name|map
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
comment|/* expected */
block|}
block|}
DECL|method|testRuntimeException ()
specifier|public
name|void
name|testRuntimeException
parameter_list|()
block|{
specifier|final
name|RuntimeException
name|e
init|=
operator|new
name|RuntimeException
argument_list|()
decl_stmt|;
name|ConcurrentMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|makeComputingMap
argument_list|(
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
throw|throw
name|e
throw|;
block|}
block|}
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
name|ComputationException
name|ce
parameter_list|)
block|{
name|assertSame
argument_list|(
name|e
argument_list|,
name|ce
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

