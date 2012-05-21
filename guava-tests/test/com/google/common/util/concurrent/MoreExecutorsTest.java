begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/*  * Portions of this file are modified versions of  * http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/test/tck/AbstractExecutorServiceTest.java?revision=1.30  * which contained the following notice:  *  * Written by Doug Lea with assistance from members of JCP JSR-166  * Expert Group and released to the public domain, as explained at  * http://creativecommons.org/publicdomain/zero/1.0/  * Other contributors include Andrew Wright, Jeffrey Hayes,  * Pat Fisher, Mike Judd.  */
end_comment

begin_package
DECL|package|com.google.common.util.concurrent
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
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
name|Iterables
operator|.
name|getOnlyElement
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
name|util
operator|.
name|concurrent
operator|.
name|MoreExecutors
operator|.
name|invokeAnyImpl
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
name|util
operator|.
name|concurrent
operator|.
name|MoreExecutors
operator|.
name|listeningDecorator
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
name|util
operator|.
name|concurrent
operator|.
name|MoreExecutors
operator|.
name|sameThreadExecutor
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
name|SECONDS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|contrib
operator|.
name|truth
operator|.
name|Truth
operator|.
name|ASSERT
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
name|Throwables
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
name|ImmutableList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|CyclicBarrier
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
name|Future
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
name|RejectedExecutionException
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
name|AtomicReference
import|;
end_import

begin_comment
comment|/**  * Tests for MoreExecutors.  *  * @author Kyle Littlefield (klittle)  */
end_comment

begin_class
DECL|class|MoreExecutorsTest
specifier|public
class|class
name|MoreExecutorsTest
extends|extends
name|JSR166TestCase
block|{
DECL|method|testSameThreadExecutorServiceInThreadExecution ()
specifier|public
name|void
name|testSameThreadExecutorServiceInThreadExecution
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|ListeningExecutorService
name|executor
init|=
name|MoreExecutors
operator|.
name|sameThreadExecutor
argument_list|()
decl_stmt|;
specifier|final
name|ThreadLocal
argument_list|<
name|Integer
argument_list|>
name|threadLocalCount
init|=
operator|new
name|ThreadLocal
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Integer
name|initialValue
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
block|}
decl_stmt|;
specifier|final
name|AtomicReference
argument_list|<
name|Throwable
argument_list|>
name|throwableFromOtherThread
init|=
operator|new
name|AtomicReference
argument_list|<
name|Throwable
argument_list|>
argument_list|(
literal|null
argument_list|)
decl_stmt|;
specifier|final
name|Runnable
name|incrementTask
init|=
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
name|threadLocalCount
operator|.
name|set
argument_list|(
name|threadLocalCount
operator|.
name|get
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|Thread
name|otherThread
init|=
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
try|try
block|{
name|Future
argument_list|<
name|?
argument_list|>
name|future
init|=
name|executor
operator|.
name|submit
argument_list|(
name|incrementTask
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|future
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|threadLocalCount
operator|.
name|get
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|Throwable
parameter_list|)
block|{
name|throwableFromOtherThread
operator|.
name|set
argument_list|(
name|Throwable
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|otherThread
operator|.
name|start
argument_list|()
expr_stmt|;
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|future
init|=
name|executor
operator|.
name|submit
argument_list|(
name|incrementTask
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|future
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertListenerRunImmediately
argument_list|(
name|future
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|threadLocalCount
operator|.
name|get
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|otherThread
operator|.
name|join
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Thread
operator|.
name|State
operator|.
name|TERMINATED
argument_list|,
name|otherThread
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|Throwable
name|throwable
init|=
name|throwableFromOtherThread
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
literal|"Throwable from other thread: "
operator|+
operator|(
name|throwable
operator|==
literal|null
condition|?
literal|null
else|:
name|Throwables
operator|.
name|getStackTraceAsString
argument_list|(
name|throwable
argument_list|)
operator|)
argument_list|,
name|throwableFromOtherThread
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSameThreadExecutorInvokeAll ()
specifier|public
name|void
name|testSameThreadExecutorInvokeAll
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|ExecutorService
name|executor
init|=
name|MoreExecutors
operator|.
name|sameThreadExecutor
argument_list|()
decl_stmt|;
specifier|final
name|ThreadLocal
argument_list|<
name|Integer
argument_list|>
name|threadLocalCount
init|=
operator|new
name|ThreadLocal
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Integer
name|initialValue
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
block|}
decl_stmt|;
specifier|final
name|Callable
argument_list|<
name|Integer
argument_list|>
name|incrementTask
init|=
operator|new
name|Callable
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|call
parameter_list|()
block|{
name|int
name|i
init|=
name|threadLocalCount
operator|.
name|get
argument_list|()
decl_stmt|;
name|threadLocalCount
operator|.
name|set
argument_list|(
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
return|return
name|i
return|;
block|}
block|}
decl_stmt|;
name|List
argument_list|<
name|Future
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|futures
init|=
name|executor
operator|.
name|invokeAll
argument_list|(
name|Collections
operator|.
name|nCopies
argument_list|(
literal|10
argument_list|,
name|incrementTask
argument_list|)
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
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|Future
argument_list|<
name|Integer
argument_list|>
name|future
init|=
name|futures
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Task should have been run before being returned"
argument_list|,
name|future
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|i
argument_list|,
name|future
operator|.
name|get
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|threadLocalCount
operator|.
name|get
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSameThreadExecutorServiceTermination ()
specifier|public
name|void
name|testSameThreadExecutorServiceTermination
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|ExecutorService
name|executor
init|=
name|MoreExecutors
operator|.
name|sameThreadExecutor
argument_list|()
decl_stmt|;
specifier|final
name|CyclicBarrier
name|barrier
init|=
operator|new
name|CyclicBarrier
argument_list|(
literal|2
argument_list|)
decl_stmt|;
specifier|final
name|AtomicReference
argument_list|<
name|Throwable
argument_list|>
name|throwableFromOtherThread
init|=
operator|new
name|AtomicReference
argument_list|<
name|Throwable
argument_list|>
argument_list|(
literal|null
argument_list|)
decl_stmt|;
specifier|final
name|Runnable
name|doNothingRunnable
init|=
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
block|{         }
block|}
decl_stmt|;
name|Thread
name|otherThread
init|=
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
try|try
block|{
name|Future
argument_list|<
name|?
argument_list|>
name|future
init|=
name|executor
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
comment|// WAIT #1
name|barrier
operator|.
name|await
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
comment|// WAIT #2
name|barrier
operator|.
name|await
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|executor
operator|.
name|isTerminated
argument_list|()
argument_list|)
expr_stmt|;
comment|// WAIT #3
name|barrier
operator|.
name|await
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|future
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|executor
operator|.
name|isTerminated
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|Throwable
parameter_list|)
block|{
name|throwableFromOtherThread
operator|.
name|set
argument_list|(
name|Throwable
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|otherThread
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// WAIT #1
name|barrier
operator|.
name|await
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|executor
operator|.
name|isTerminated
argument_list|()
argument_list|)
expr_stmt|;
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|executor
operator|.
name|submit
argument_list|(
name|doNothingRunnable
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have encountered RejectedExecutionException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RejectedExecutionException
name|ex
parameter_list|)
block|{
comment|// good to go
block|}
name|assertFalse
argument_list|(
name|executor
operator|.
name|isTerminated
argument_list|()
argument_list|)
expr_stmt|;
comment|// WAIT #2
name|barrier
operator|.
name|await
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|executor
operator|.
name|awaitTermination
argument_list|(
literal|20
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
comment|// WAIT #3
name|barrier
operator|.
name|await
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|executor
operator|.
name|awaitTermination
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|executor
operator|.
name|awaitTermination
argument_list|(
literal|0
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|executor
operator|.
name|submit
argument_list|(
name|doNothingRunnable
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have encountered RejectedExecutionException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RejectedExecutionException
name|ex
parameter_list|)
block|{
comment|// good to go
block|}
name|assertTrue
argument_list|(
name|executor
operator|.
name|isTerminated
argument_list|()
argument_list|)
expr_stmt|;
name|otherThread
operator|.
name|join
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Thread
operator|.
name|State
operator|.
name|TERMINATED
argument_list|,
name|otherThread
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|Throwable
name|throwable
init|=
name|throwableFromOtherThread
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
literal|"Throwable from other thread: "
operator|+
operator|(
name|throwable
operator|==
literal|null
condition|?
literal|null
else|:
name|Throwables
operator|.
name|getStackTraceAsString
argument_list|(
name|throwable
argument_list|)
operator|)
argument_list|,
name|throwableFromOtherThread
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testListeningDecorator ()
specifier|public
name|void
name|testListeningDecorator
parameter_list|()
throws|throws
name|Exception
block|{
name|ListeningExecutorService
name|service
init|=
name|listeningDecorator
argument_list|(
name|MoreExecutors
operator|.
name|sameThreadExecutor
argument_list|()
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|service
argument_list|,
name|listeningDecorator
argument_list|(
name|service
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|>
name|callables
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|Callables
operator|.
name|returning
argument_list|(
literal|"x"
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Future
argument_list|<
name|String
argument_list|>
argument_list|>
name|results
decl_stmt|;
name|results
operator|=
name|service
operator|.
name|invokeAll
argument_list|(
name|callables
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|getOnlyElement
argument_list|(
name|results
argument_list|)
argument_list|)
operator|.
name|isA
argument_list|(
name|ListenableFutureTask
operator|.
name|class
argument_list|)
expr_stmt|;
name|results
operator|=
name|service
operator|.
name|invokeAll
argument_list|(
name|callables
argument_list|,
literal|1
argument_list|,
name|SECONDS
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|getOnlyElement
argument_list|(
name|results
argument_list|)
argument_list|)
operator|.
name|isA
argument_list|(
name|ListenableFutureTask
operator|.
name|class
argument_list|)
expr_stmt|;
comment|/*      * TODO(cpovirk): move ForwardingTestCase somewhere common, and use it to      * test the forwarded methods      */
block|}
comment|/**    * invokeAny(null) throws NPE    */
DECL|method|testInvokeAnyImpl_nullTasks ()
specifier|public
name|void
name|testInvokeAnyImpl_nullTasks
parameter_list|()
throws|throws
name|Exception
block|{
name|ListeningExecutorService
name|e
init|=
name|sameThreadExecutor
argument_list|()
decl_stmt|;
try|try
block|{
name|invokeAnyImpl
argument_list|(
name|e
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|shouldThrow
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|success
parameter_list|)
block|{     }
finally|finally
block|{
name|joinPool
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * invokeAny(empty collection) throws IAE    */
DECL|method|testInvokeAnyImpl_emptyTasks ()
specifier|public
name|void
name|testInvokeAnyImpl_emptyTasks
parameter_list|()
throws|throws
name|Exception
block|{
name|ListeningExecutorService
name|e
init|=
name|sameThreadExecutor
argument_list|()
decl_stmt|;
try|try
block|{
name|invokeAnyImpl
argument_list|(
name|e
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|shouldThrow
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|success
parameter_list|)
block|{     }
finally|finally
block|{
name|joinPool
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * invokeAny(c) throws NPE if c has null elements    */
DECL|method|testInvokeAnyImpl_nullElement ()
specifier|public
name|void
name|testInvokeAnyImpl_nullElement
parameter_list|()
throws|throws
name|Exception
block|{
name|ListeningExecutorService
name|e
init|=
name|sameThreadExecutor
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Callable
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<
name|Callable
argument_list|<
name|Integer
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|l
operator|.
name|add
argument_list|(
operator|new
name|Callable
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|call
parameter_list|()
block|{
throw|throw
operator|new
name|ArithmeticException
argument_list|(
literal|"/ by zero"
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
name|l
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
try|try
block|{
name|invokeAnyImpl
argument_list|(
name|e
argument_list|,
name|l
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|shouldThrow
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|success
parameter_list|)
block|{     }
finally|finally
block|{
name|joinPool
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * invokeAny(c) throws ExecutionException if no task in c completes    */
DECL|method|testInvokeAnyImpl_noTaskCompletes ()
specifier|public
name|void
name|testInvokeAnyImpl_noTaskCompletes
parameter_list|()
throws|throws
name|Exception
block|{
name|ListeningExecutorService
name|e
init|=
name|sameThreadExecutor
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|l
operator|.
name|add
argument_list|(
operator|new
name|NPETask
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|invokeAnyImpl
argument_list|(
name|e
argument_list|,
name|l
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|shouldThrow
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|success
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|success
operator|.
name|getCause
argument_list|()
operator|instanceof
name|NullPointerException
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|joinPool
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * invokeAny(c) returns result of some task in c if at least one completes    */
DECL|method|testInvokeAnyImpl ()
specifier|public
name|void
name|testInvokeAnyImpl
parameter_list|()
throws|throws
name|Exception
block|{
name|ListeningExecutorService
name|e
init|=
name|sameThreadExecutor
argument_list|()
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|l
operator|.
name|add
argument_list|(
operator|new
name|StringTask
argument_list|()
argument_list|)
expr_stmt|;
name|l
operator|.
name|add
argument_list|(
operator|new
name|StringTask
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|invokeAnyImpl
argument_list|(
name|e
argument_list|,
name|l
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|TEST_STRING
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|joinPool
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertListenerRunImmediately (ListenableFuture<?> future)
specifier|private
specifier|static
name|void
name|assertListenerRunImmediately
parameter_list|(
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|future
parameter_list|)
block|{
name|CountingRunnable
name|listener
init|=
operator|new
name|CountingRunnable
argument_list|()
decl_stmt|;
name|future
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|sameThreadExecutor
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|listener
operator|.
name|count
argument_list|)
expr_stmt|;
block|}
DECL|class|CountingRunnable
specifier|private
specifier|static
specifier|final
class|class
name|CountingRunnable
implements|implements
name|Runnable
block|{
DECL|field|count
name|int
name|count
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|count
operator|++
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

