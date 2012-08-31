begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|junit
operator|.
name|framework
operator|.
name|AssertionFailedError
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
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_comment
comment|/**  * Tests for {@link AbstractFuture}.  *  * @author Brian Stoler  */
end_comment

begin_class
DECL|class|AbstractFutureTest
specifier|public
class|class
name|AbstractFutureTest
extends|extends
name|TestCase
block|{
DECL|method|testSuccess ()
specifier|public
name|void
name|testSuccess
parameter_list|()
throws|throws
name|ExecutionException
throws|,
name|InterruptedException
block|{
specifier|final
name|Object
name|value
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|value
argument_list|,
operator|new
name|AbstractFuture
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
block|{
name|set
parameter_list|(
name|value
parameter_list|)
constructor_decl|;
block|}
block|}
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testException ()
specifier|public
name|void
name|testException
parameter_list|()
throws|throws
name|InterruptedException
block|{
specifier|final
name|Throwable
name|failure
init|=
operator|new
name|Throwable
argument_list|()
decl_stmt|;
name|AbstractFuture
argument_list|<
name|String
argument_list|>
name|future
init|=
operator|new
name|AbstractFuture
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
block|{
name|setException
parameter_list|(
name|failure
parameter_list|)
constructor_decl|;
block|}
block|}
decl_stmt|;
name|ExecutionException
name|ee1
init|=
name|getExpectingExecutionException
argument_list|(
name|future
argument_list|)
decl_stmt|;
name|ExecutionException
name|ee2
init|=
name|getExpectingExecutionException
argument_list|(
name|future
argument_list|)
decl_stmt|;
comment|// Ensure we get a unique execution exception on each get
name|assertNotSame
argument_list|(
name|ee1
argument_list|,
name|ee2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|failure
argument_list|,
name|ee1
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|failure
argument_list|,
name|ee2
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|checkStackTrace
argument_list|(
name|ee1
argument_list|)
expr_stmt|;
name|checkStackTrace
argument_list|(
name|ee2
argument_list|)
expr_stmt|;
block|}
DECL|method|testCancel_notDoneNoInterrupt ()
specifier|public
name|void
name|testCancel_notDoneNoInterrupt
parameter_list|()
block|{
name|InterruptibleFuture
name|future
init|=
operator|new
name|InterruptibleFuture
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|future
operator|.
name|cancel
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|future
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|future
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|future
operator|.
name|wasInterrupted
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|future
operator|.
name|interruptTaskWasCalled
argument_list|)
expr_stmt|;
block|}
DECL|method|testCancel_notDoneInterrupt ()
specifier|public
name|void
name|testCancel_notDoneInterrupt
parameter_list|()
block|{
name|InterruptibleFuture
name|future
init|=
operator|new
name|InterruptibleFuture
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|future
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|future
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
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
name|future
operator|.
name|wasInterrupted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|future
operator|.
name|interruptTaskWasCalled
argument_list|)
expr_stmt|;
block|}
DECL|method|testCancel_done ()
specifier|public
name|void
name|testCancel_done
parameter_list|()
block|{
name|AbstractFuture
argument_list|<
name|String
argument_list|>
name|future
init|=
operator|new
name|AbstractFuture
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
block|{
name|set
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|assertFalse
argument_list|(
name|future
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|future
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|future
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCompletionFinishesWithDone ()
specifier|public
name|void
name|testCompletionFinishesWithDone
parameter_list|()
block|{
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|10
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
literal|50000
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|AbstractFuture
argument_list|<
name|String
argument_list|>
name|future
init|=
operator|new
name|AbstractFuture
argument_list|<
name|String
argument_list|>
argument_list|()
block|{}
decl_stmt|;
specifier|final
name|AtomicReference
argument_list|<
name|String
argument_list|>
name|errorMessage
init|=
operator|new
name|AtomicReference
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|executor
operator|.
name|execute
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
name|future
operator|.
name|set
argument_list|(
literal|"success"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|future
operator|.
name|isDone
argument_list|()
condition|)
block|{
name|errorMessage
operator|.
name|set
argument_list|(
literal|"Set call exited before future was complete."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|executor
operator|.
name|execute
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
name|future
operator|.
name|setException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"failure"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|future
operator|.
name|isDone
argument_list|()
condition|)
block|{
name|errorMessage
operator|.
name|set
argument_list|(
literal|"SetException call exited before future was complete."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|executor
operator|.
name|execute
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
name|future
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|future
operator|.
name|isDone
argument_list|()
condition|)
block|{
name|errorMessage
operator|.
name|set
argument_list|(
literal|"Cancel call exited before future was complete."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
try|try
block|{
name|future
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Ignore, we just wanted to block.
block|}
name|String
name|error
init|=
name|errorMessage
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|error
argument_list|,
name|error
argument_list|)
expr_stmt|;
block|}
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
DECL|method|checkStackTrace (ExecutionException e)
specifier|private
name|void
name|checkStackTrace
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
comment|// Our call site for get() should be in the trace.
name|int
name|index
init|=
name|findStackFrame
argument_list|(
name|e
argument_list|,
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"getExpectingExecutionException"
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|index
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// Above our method should be the call to get(). Don't assert on the class
comment|// because it could be some superclass.
name|ASSERT
operator|.
name|that
argument_list|(
name|e
operator|.
name|getStackTrace
argument_list|()
index|[
name|index
operator|-
literal|1
index|]
operator|.
name|getMethodName
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"get"
argument_list|)
expr_stmt|;
block|}
DECL|method|findStackFrame ( ExecutionException e, String clazz, String method)
specifier|private
specifier|static
name|int
name|findStackFrame
parameter_list|(
name|ExecutionException
name|e
parameter_list|,
name|String
name|clazz
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|StackTraceElement
index|[]
name|elements
init|=
name|e
operator|.
name|getStackTrace
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
name|elements
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|StackTraceElement
name|element
init|=
name|elements
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|element
operator|.
name|getClassName
argument_list|()
operator|.
name|equals
argument_list|(
name|clazz
argument_list|)
operator|&&
name|element
operator|.
name|getMethodName
argument_list|()
operator|.
name|equals
argument_list|(
name|method
argument_list|)
condition|)
block|{
return|return
name|i
return|;
block|}
block|}
name|AssertionFailedError
name|failure
init|=
operator|new
name|AssertionFailedError
argument_list|(
literal|"Expected element "
operator|+
name|clazz
operator|+
literal|"."
operator|+
name|method
operator|+
literal|" not found in stack trace"
argument_list|)
decl_stmt|;
name|failure
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|failure
throw|;
block|}
DECL|method|getExpectingExecutionException ( AbstractFuture<String> future)
specifier|private
name|ExecutionException
name|getExpectingExecutionException
parameter_list|(
name|AbstractFuture
argument_list|<
name|String
argument_list|>
name|future
parameter_list|)
throws|throws
name|InterruptedException
block|{
try|try
block|{
name|String
name|got
init|=
name|future
operator|.
name|get
argument_list|()
decl_stmt|;
name|fail
argument_list|(
literal|"Expected exception but got "
operator|+
name|got
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
return|return
name|e
return|;
block|}
comment|// unreachable, but compiler doesn't know that fail() always throws
return|return
literal|null
return|;
block|}
DECL|class|InterruptibleFuture
specifier|private
specifier|static
specifier|final
class|class
name|InterruptibleFuture
extends|extends
name|AbstractFuture
argument_list|<
name|String
argument_list|>
block|{
DECL|field|interruptTaskWasCalled
name|boolean
name|interruptTaskWasCalled
decl_stmt|;
DECL|method|interruptTask ()
annotation|@
name|Override
specifier|protected
name|void
name|interruptTask
parameter_list|()
block|{
name|interruptTaskWasCalled
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

