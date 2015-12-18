begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
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
name|EnumSet
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
name|TimeUnit
import|;
end_import

begin_comment
comment|/**  * Unit test for {@link FakeTicker}.  *  * @author Jige Yu  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|FakeTickerTest
specifier|public
class|class
name|FakeTickerTest
extends|extends
name|TestCase
block|{
annotation|@
name|GwtIncompatible
argument_list|(
literal|"NullPointerTester"
argument_list|)
DECL|method|testNullPointerExceptions ()
specifier|public
name|void
name|testNullPointerExceptions
parameter_list|()
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
name|FakeTicker
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAdvance ()
specifier|public
name|void
name|testAdvance
parameter_list|()
block|{
name|FakeTicker
name|ticker
init|=
operator|new
name|FakeTicker
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ticker
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|ticker
argument_list|,
name|ticker
operator|.
name|advance
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|ticker
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1000010L
argument_list|,
name|ticker
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAutoIncrementStep_returnsSameInstance ()
specifier|public
name|void
name|testAutoIncrementStep_returnsSameInstance
parameter_list|()
block|{
name|FakeTicker
name|ticker
init|=
operator|new
name|FakeTicker
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|ticker
argument_list|,
name|ticker
operator|.
name|setAutoIncrementStep
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAutoIncrementStep_nanos ()
specifier|public
name|void
name|testAutoIncrementStep_nanos
parameter_list|()
block|{
name|FakeTicker
name|ticker
init|=
operator|new
name|FakeTicker
argument_list|()
operator|.
name|setAutoIncrementStep
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ticker
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|ticker
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|ticker
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAutoIncrementStep_millis ()
specifier|public
name|void
name|testAutoIncrementStep_millis
parameter_list|()
block|{
name|FakeTicker
name|ticker
init|=
operator|new
name|FakeTicker
argument_list|()
operator|.
name|setAutoIncrementStep
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ticker
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1000000
argument_list|,
name|ticker
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2000000
argument_list|,
name|ticker
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAutoIncrementStep_seconds ()
specifier|public
name|void
name|testAutoIncrementStep_seconds
parameter_list|()
block|{
name|FakeTicker
name|ticker
init|=
operator|new
name|FakeTicker
argument_list|()
operator|.
name|setAutoIncrementStep
argument_list|(
literal|3
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ticker
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3000000000L
argument_list|,
name|ticker
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|6000000000L
argument_list|,
name|ticker
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAutoIncrementStep_resetToZero ()
specifier|public
name|void
name|testAutoIncrementStep_resetToZero
parameter_list|()
block|{
name|FakeTicker
name|ticker
init|=
operator|new
name|FakeTicker
argument_list|()
operator|.
name|setAutoIncrementStep
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ticker
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|ticker
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|ticker
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|TimeUnit
name|timeUnit
range|:
name|EnumSet
operator|.
name|allOf
argument_list|(
name|TimeUnit
operator|.
name|class
argument_list|)
control|)
block|{
name|ticker
operator|.
name|setAutoIncrementStep
argument_list|(
literal|0
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected no auto-increment when setting autoIncrementStep to 0 "
operator|+
name|timeUnit
argument_list|,
literal|30
argument_list|,
name|ticker
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testAutoIncrement_negative ()
specifier|public
name|void
name|testAutoIncrement_negative
parameter_list|()
block|{
name|FakeTicker
name|ticker
init|=
operator|new
name|FakeTicker
argument_list|()
decl_stmt|;
try|try
block|{
name|ticker
operator|.
name|setAutoIncrementStep
argument_list|(
operator|-
literal|1
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"concurrency"
argument_list|)
DECL|method|testConcurrentAdvance ()
specifier|public
name|void
name|testConcurrentAdvance
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|FakeTicker
name|ticker
init|=
operator|new
name|FakeTicker
argument_list|()
decl_stmt|;
name|int
name|numberOfThreads
init|=
literal|64
decl_stmt|;
name|runConcurrentTest
argument_list|(
name|numberOfThreads
argument_list|,
operator|new
name|Callable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|()
throws|throws
name|Exception
block|{
comment|// adds two nanoseconds to the ticker
name|ticker
operator|.
name|advance
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|numberOfThreads
operator|*
literal|2
argument_list|,
name|ticker
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"concurrency"
argument_list|)
DECL|method|testConcurrentAutoIncrementStep ()
specifier|public
name|void
name|testConcurrentAutoIncrementStep
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|incrementByNanos
init|=
literal|3
decl_stmt|;
specifier|final
name|FakeTicker
name|ticker
init|=
operator|new
name|FakeTicker
argument_list|()
operator|.
name|setAutoIncrementStep
argument_list|(
name|incrementByNanos
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
decl_stmt|;
name|int
name|numberOfThreads
init|=
literal|64
decl_stmt|;
name|runConcurrentTest
argument_list|(
name|numberOfThreads
argument_list|,
operator|new
name|Callable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|ticker
operator|.
name|read
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|incrementByNanos
operator|*
name|numberOfThreads
argument_list|,
name|ticker
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Runs {@code callable} concurrently {@code numberOfThreads} times.    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"concurrency"
argument_list|)
DECL|method|runConcurrentTest (int numberOfThreads, final Callable<Void> callable)
specifier|private
name|void
name|runConcurrentTest
parameter_list|(
name|int
name|numberOfThreads
parameter_list|,
specifier|final
name|Callable
argument_list|<
name|Void
argument_list|>
name|callable
parameter_list|)
throws|throws
name|Exception
block|{
name|ExecutorService
name|executorService
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|numberOfThreads
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|startLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|numberOfThreads
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|doneLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|numberOfThreads
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|numberOfThreads
init|;
name|i
operator|>
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|executorService
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|startLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|startLatch
operator|.
name|await
argument_list|()
expr_stmt|;
name|callable
operator|.
name|call
argument_list|()
expr_stmt|;
name|doneLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|doneLatch
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

