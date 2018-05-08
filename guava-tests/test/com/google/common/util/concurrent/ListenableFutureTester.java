begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|fail
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

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Used to test listenable future implementations.  *  * @author Sven Mawson  */
end_comment

begin_class
DECL|class|ListenableFutureTester
specifier|public
class|class
name|ListenableFutureTester
block|{
DECL|field|exec
specifier|private
specifier|final
name|ExecutorService
name|exec
decl_stmt|;
DECL|field|future
specifier|private
specifier|final
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|future
decl_stmt|;
DECL|field|latch
specifier|private
specifier|final
name|CountDownLatch
name|latch
decl_stmt|;
DECL|method|ListenableFutureTester (ListenableFuture<?> future)
specifier|public
name|ListenableFutureTester
parameter_list|(
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|future
parameter_list|)
block|{
name|this
operator|.
name|exec
operator|=
name|Executors
operator|.
name|newCachedThreadPool
argument_list|()
expr_stmt|;
name|this
operator|.
name|future
operator|=
name|checkNotNull
argument_list|(
name|future
argument_list|)
expr_stmt|;
name|this
operator|.
name|latch
operator|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|future
operator|.
name|addListener
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
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|,
name|exec
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|latch
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
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
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|exec
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
DECL|method|testCompletedFuture (@ullable Object expectedValue)
specifier|public
name|void
name|testCompletedFuture
parameter_list|(
annotation|@
name|Nullable
name|Object
name|expectedValue
parameter_list|)
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
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
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
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
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedValue
argument_list|,
name|future
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCancelledFuture ()
specifier|public
name|void
name|testCancelledFuture
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
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
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
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
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|future
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Future should throw CancellationException on cancel."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CancellationException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testFailedFuture (@ullable String message)
specifier|public
name|void
name|testFailedFuture
parameter_list|(
annotation|@
name|Nullable
name|String
name|message
parameter_list|)
throws|throws
name|InterruptedException
block|{
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
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
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
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|future
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Future should rethrow the exception."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
operator|.
name|hasMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

