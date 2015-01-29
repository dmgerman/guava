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
name|Preconditions
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
name|org
operator|.
name|mockito
operator|.
name|Mockito
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
name|CancellationException
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
name|Executor
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
name|TimeUnit
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Test for {@link FutureCallback}.  *  * @author Anthony Zana  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|FutureCallbackTest
specifier|public
class|class
name|FutureCallbackTest
extends|extends
name|TestCase
block|{
DECL|method|testSameThreadSuccess ()
specifier|public
name|void
name|testSameThreadSuccess
parameter_list|()
block|{
name|SettableFuture
argument_list|<
name|String
argument_list|>
name|f
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
name|MockCallback
name|callback
init|=
operator|new
name|MockCallback
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|Futures
operator|.
name|addCallback
argument_list|(
name|f
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|f
operator|.
name|set
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
block|}
DECL|method|testExecutorSuccess ()
specifier|public
name|void
name|testExecutorSuccess
parameter_list|()
block|{
name|CountingSameThreadExecutor
name|ex
init|=
operator|new
name|CountingSameThreadExecutor
argument_list|()
decl_stmt|;
name|SettableFuture
argument_list|<
name|String
argument_list|>
name|f
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
name|MockCallback
name|callback
init|=
operator|new
name|MockCallback
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|Futures
operator|.
name|addCallback
argument_list|(
name|f
argument_list|,
name|callback
argument_list|,
name|ex
argument_list|)
expr_stmt|;
name|f
operator|.
name|set
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ex
operator|.
name|runCount
argument_list|)
expr_stmt|;
block|}
comment|// Error cases
DECL|method|testSameThreadExecutionException ()
specifier|public
name|void
name|testSameThreadExecutionException
parameter_list|()
block|{
name|SettableFuture
argument_list|<
name|String
argument_list|>
name|f
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
name|Exception
name|e
init|=
operator|new
name|IllegalArgumentException
argument_list|(
literal|"foo not found"
argument_list|)
decl_stmt|;
name|MockCallback
name|callback
init|=
operator|new
name|MockCallback
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|Futures
operator|.
name|addCallback
argument_list|(
name|f
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|f
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
DECL|method|testCancel ()
specifier|public
name|void
name|testCancel
parameter_list|()
block|{
name|SettableFuture
argument_list|<
name|String
argument_list|>
name|f
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
name|FutureCallback
argument_list|<
name|String
argument_list|>
name|callback
init|=
operator|new
name|FutureCallback
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
specifier|private
name|boolean
name|called
init|=
literal|false
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|String
name|result
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Was not expecting onSuccess() to be called."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|onFailure
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|assertFalse
argument_list|(
name|called
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|t
operator|instanceof
name|CancellationException
argument_list|)
expr_stmt|;
name|called
operator|=
literal|true
expr_stmt|;
block|}
block|}
decl_stmt|;
name|Futures
operator|.
name|addCallback
argument_list|(
name|f
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|f
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testThrowErrorFromGet ()
specifier|public
name|void
name|testThrowErrorFromGet
parameter_list|()
block|{
name|Error
name|error
init|=
operator|new
name|AssertionError
argument_list|(
literal|"ASSERT!"
argument_list|)
decl_stmt|;
name|ListenableFuture
argument_list|<
name|String
argument_list|>
name|f
init|=
name|ThrowingFuture
operator|.
name|throwingError
argument_list|(
name|error
argument_list|)
decl_stmt|;
name|MockCallback
name|callback
init|=
operator|new
name|MockCallback
argument_list|(
name|error
argument_list|)
decl_stmt|;
name|Futures
operator|.
name|addCallback
argument_list|(
name|f
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
DECL|method|testRuntimeExeceptionFromGet ()
specifier|public
name|void
name|testRuntimeExeceptionFromGet
parameter_list|()
block|{
name|RuntimeException
name|e
init|=
operator|new
name|IllegalArgumentException
argument_list|(
literal|"foo not found"
argument_list|)
decl_stmt|;
name|ListenableFuture
argument_list|<
name|String
argument_list|>
name|f
init|=
name|ThrowingFuture
operator|.
name|throwingRuntimeException
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|MockCallback
name|callback
init|=
operator|new
name|MockCallback
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|Futures
operator|.
name|addCallback
argument_list|(
name|f
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"Mockito"
argument_list|)
DECL|method|testOnSuccessThrowsRuntimeException ()
specifier|public
name|void
name|testOnSuccessThrowsRuntimeException
parameter_list|()
throws|throws
name|Exception
block|{
name|RuntimeException
name|exception
init|=
operator|new
name|RuntimeException
argument_list|()
decl_stmt|;
name|String
name|result
init|=
literal|"result"
decl_stmt|;
name|SettableFuture
argument_list|<
name|String
argument_list|>
name|future
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// Safe for a mock
name|FutureCallback
argument_list|<
name|String
argument_list|>
name|callback
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|FutureCallback
operator|.
name|class
argument_list|)
decl_stmt|;
name|Futures
operator|.
name|addCallback
argument_list|(
name|future
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|doThrow
argument_list|(
name|exception
argument_list|)
operator|.
name|when
argument_list|(
name|callback
argument_list|)
operator|.
name|onSuccess
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|future
operator|.
name|set
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|result
argument_list|,
name|future
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|callback
argument_list|)
operator|.
name|onSuccess
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verifyNoMoreInteractions
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"Mockito"
argument_list|)
DECL|method|testOnSuccessThrowsError ()
specifier|public
name|void
name|testOnSuccessThrowsError
parameter_list|()
throws|throws
name|Exception
block|{
class|class
name|TestError
extends|extends
name|Error
block|{}
name|TestError
name|error
init|=
operator|new
name|TestError
argument_list|()
decl_stmt|;
name|String
name|result
init|=
literal|"result"
decl_stmt|;
name|SettableFuture
argument_list|<
name|String
argument_list|>
name|future
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// Safe for a mock
name|FutureCallback
argument_list|<
name|String
argument_list|>
name|callback
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|FutureCallback
operator|.
name|class
argument_list|)
decl_stmt|;
name|Futures
operator|.
name|addCallback
argument_list|(
name|future
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|doThrow
argument_list|(
name|error
argument_list|)
operator|.
name|when
argument_list|(
name|callback
argument_list|)
operator|.
name|onSuccess
argument_list|(
name|result
argument_list|)
expr_stmt|;
try|try
block|{
name|future
operator|.
name|set
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestError
name|e
parameter_list|)
block|{
name|assertSame
argument_list|(
name|error
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|result
argument_list|,
name|future
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|callback
argument_list|)
operator|.
name|onSuccess
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verifyNoMoreInteractions
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
DECL|method|testWildcardFuture ()
specifier|public
name|void
name|testWildcardFuture
parameter_list|()
block|{
name|SettableFuture
argument_list|<
name|String
argument_list|>
name|settable
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|f
init|=
name|settable
decl_stmt|;
name|FutureCallback
argument_list|<
name|Object
argument_list|>
name|callback
init|=
operator|new
name|FutureCallback
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|Object
name|result
parameter_list|)
block|{}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{}
block|}
decl_stmt|;
name|Futures
operator|.
name|addCallback
argument_list|(
name|f
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
DECL|class|CountingSameThreadExecutor
specifier|private
class|class
name|CountingSameThreadExecutor
implements|implements
name|Executor
block|{
DECL|field|runCount
name|int
name|runCount
init|=
literal|0
decl_stmt|;
annotation|@
name|Override
DECL|method|execute (Runnable command)
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|command
parameter_list|)
block|{
name|command
operator|.
name|run
argument_list|()
expr_stmt|;
name|runCount
operator|++
expr_stmt|;
block|}
block|}
comment|// TODO(user): Move to testing, unify with RuntimeExceptionThrowingFuture
comment|/**    * A {@link Future} implementation which always throws directly from calls to    * get() (i.e. not wrapped in ExecutionException.    * For just a normal Future failure, use {@link SettableFuture}).    *    *<p>Useful for testing the behavior of Future utilities against odd futures.    *    * @author Anthony Zana    */
DECL|class|ThrowingFuture
specifier|private
specifier|static
class|class
name|ThrowingFuture
parameter_list|<
name|V
parameter_list|>
implements|implements
name|ListenableFuture
argument_list|<
name|V
argument_list|>
block|{
DECL|field|error
specifier|private
specifier|final
name|Error
name|error
decl_stmt|;
DECL|field|runtime
specifier|private
specifier|final
name|RuntimeException
name|runtime
decl_stmt|;
DECL|method|throwingError (Error error)
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|throwingError
parameter_list|(
name|Error
name|error
parameter_list|)
block|{
return|return
operator|new
name|ThrowingFuture
argument_list|<
name|V
argument_list|>
argument_list|(
name|error
argument_list|)
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|ListenableFuture
argument_list|<
name|V
argument_list|>
DECL|method|throwingRuntimeException (RuntimeException e)
name|throwingRuntimeException
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
return|return
operator|new
name|ThrowingFuture
argument_list|<
name|V
argument_list|>
argument_list|(
name|e
argument_list|)
return|;
block|}
DECL|method|ThrowingFuture (Error error)
specifier|private
name|ThrowingFuture
parameter_list|(
name|Error
name|error
parameter_list|)
block|{
name|this
operator|.
name|error
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|error
argument_list|)
expr_stmt|;
name|this
operator|.
name|runtime
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|ThrowingFuture (RuntimeException e)
specifier|public
name|ThrowingFuture
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|this
operator|.
name|runtime
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|this
operator|.
name|error
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|cancel (boolean mayInterruptIfRunning)
specifier|public
name|boolean
name|cancel
parameter_list|(
name|boolean
name|mayInterruptIfRunning
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|isCancelled ()
specifier|public
name|boolean
name|isCancelled
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|isDone ()
specifier|public
name|boolean
name|isDone
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|V
name|get
parameter_list|()
block|{
name|throwOnGet
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"Unreachable"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|get (long timeout, TimeUnit unit)
specifier|public
name|V
name|get
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|throwOnGet
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"Unreachable"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|addListener (Runnable listener, Executor executor)
specifier|public
name|void
name|addListener
parameter_list|(
name|Runnable
name|listener
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|executor
operator|.
name|execute
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
DECL|method|throwOnGet ()
specifier|private
name|void
name|throwOnGet
parameter_list|()
block|{
if|if
condition|(
name|error
operator|!=
literal|null
condition|)
block|{
throw|throw
name|error
throw|;
block|}
else|else
block|{
throw|throw
name|runtime
throw|;
block|}
block|}
block|}
DECL|class|MockCallback
specifier|private
specifier|final
class|class
name|MockCallback
implements|implements
name|FutureCallback
argument_list|<
name|String
argument_list|>
block|{
DECL|field|value
annotation|@
name|Nullable
specifier|private
name|String
name|value
init|=
literal|null
decl_stmt|;
DECL|field|failure
annotation|@
name|Nullable
specifier|private
name|Throwable
name|failure
init|=
literal|null
decl_stmt|;
DECL|field|wasCalled
specifier|private
name|boolean
name|wasCalled
init|=
literal|false
decl_stmt|;
DECL|method|MockCallback (String expectedValue)
name|MockCallback
parameter_list|(
name|String
name|expectedValue
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|expectedValue
expr_stmt|;
block|}
DECL|method|MockCallback (Throwable expectedFailure)
specifier|public
name|MockCallback
parameter_list|(
name|Throwable
name|expectedFailure
parameter_list|)
block|{
name|this
operator|.
name|failure
operator|=
name|expectedFailure
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onSuccess (String result)
specifier|public
specifier|synchronized
name|void
name|onSuccess
parameter_list|(
name|String
name|result
parameter_list|)
block|{
name|assertFalse
argument_list|(
name|wasCalled
argument_list|)
expr_stmt|;
name|wasCalled
operator|=
literal|true
expr_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onFailure (Throwable t)
specifier|public
specifier|synchronized
name|void
name|onFailure
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|assertFalse
argument_list|(
name|wasCalled
argument_list|)
expr_stmt|;
name|wasCalled
operator|=
literal|true
expr_stmt|;
name|assertEquals
argument_list|(
name|failure
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

