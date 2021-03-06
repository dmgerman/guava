begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Verify
operator|.
name|verify
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
name|Futures
operator|.
name|immediateFailedFuture
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
name|Futures
operator|.
name|immediateFuture
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
name|directExecutor
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
name|Executors
operator|.
name|newScheduledThreadPool
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
name|util
operator|.
name|concurrent
operator|.
name|ForwardingListenableFuture
operator|.
name|SimpleForwardingListenableFuture
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
name|ScheduledExecutorService
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
name|TimeoutException
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

begin_comment
comment|/**  * Tests for {@link FluentFuture}. The tests cover only the basics for the API. The actual logic is  * tested in {@link FuturesTest}.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|FluentFutureTest
specifier|public
class|class
name|FluentFutureTest
extends|extends
name|TestCase
block|{
DECL|method|testFromFluentFuture ()
specifier|public
name|void
name|testFromFluentFuture
parameter_list|()
block|{
name|FluentFuture
argument_list|<
name|String
argument_list|>
name|f
init|=
name|FluentFuture
operator|.
name|from
argument_list|(
name|SettableFuture
operator|.
expr|<
name|String
operator|>
name|create
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|FluentFuture
operator|.
name|from
argument_list|(
name|f
argument_list|)
argument_list|)
operator|.
name|isSameInstanceAs
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
DECL|method|testFromFluentFuturePassingAsNonFluent ()
specifier|public
name|void
name|testFromFluentFuturePassingAsNonFluent
parameter_list|()
block|{
name|ListenableFuture
argument_list|<
name|String
argument_list|>
name|f
init|=
name|FluentFuture
operator|.
name|from
argument_list|(
name|SettableFuture
operator|.
expr|<
name|String
operator|>
name|create
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|FluentFuture
operator|.
name|from
argument_list|(
name|f
argument_list|)
argument_list|)
operator|.
name|isSameInstanceAs
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
DECL|method|testFromNonFluentFuture ()
specifier|public
name|void
name|testFromNonFluentFuture
parameter_list|()
throws|throws
name|Exception
block|{
name|ListenableFuture
argument_list|<
name|String
argument_list|>
name|f
init|=
operator|new
name|SimpleForwardingListenableFuture
argument_list|<
name|String
argument_list|>
argument_list|(
name|immediateFuture
argument_list|(
literal|"a"
argument_list|)
argument_list|)
block|{}
decl_stmt|;
name|verify
argument_list|(
operator|!
operator|(
name|f
operator|instanceof
name|FluentFuture
operator|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|FluentFuture
operator|.
name|from
argument_list|(
name|f
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
comment|// TODO(cpovirk): Test forwarding more extensively.
block|}
DECL|method|testAddCallback ()
specifier|public
name|void
name|testAddCallback
parameter_list|()
block|{
name|FluentFuture
argument_list|<
name|String
argument_list|>
name|f
init|=
name|FluentFuture
operator|.
name|from
argument_list|(
name|immediateFuture
argument_list|(
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|boolean
index|[]
name|called
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
name|f
operator|.
name|addCallback
argument_list|(
operator|new
name|FutureCallback
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
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
name|called
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
block|}
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
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|called
index|[
literal|0
index|]
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
DECL|method|testCatching ()
specifier|public
name|void
name|testCatching
parameter_list|()
throws|throws
name|Exception
block|{
name|FluentFuture
argument_list|<
name|?
argument_list|>
name|f
init|=
name|FluentFuture
operator|.
name|from
argument_list|(
name|immediateFailedFuture
argument_list|(
operator|new
name|RuntimeException
argument_list|()
argument_list|)
argument_list|)
operator|.
name|catching
argument_list|(
name|Throwable
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|Throwable
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|Throwable
name|input
parameter_list|)
block|{
return|return
name|input
operator|.
name|getClass
argument_list|()
return|;
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|f
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|RuntimeException
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testCatchingAsync ()
specifier|public
name|void
name|testCatchingAsync
parameter_list|()
throws|throws
name|Exception
block|{
name|FluentFuture
argument_list|<
name|?
argument_list|>
name|f
init|=
name|FluentFuture
operator|.
name|from
argument_list|(
name|immediateFailedFuture
argument_list|(
operator|new
name|RuntimeException
argument_list|()
argument_list|)
argument_list|)
operator|.
name|catchingAsync
argument_list|(
name|Throwable
operator|.
name|class
argument_list|,
operator|new
name|AsyncFunction
argument_list|<
name|Throwable
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|apply
parameter_list|(
name|Throwable
name|input
parameter_list|)
block|{
return|return
name|Futures
operator|.
expr|<
name|Class
argument_list|<
name|?
argument_list|>
operator|>
name|immediateFuture
argument_list|(
name|input
operator|.
name|getClass
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|f
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|RuntimeException
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransform ()
specifier|public
name|void
name|testTransform
parameter_list|()
throws|throws
name|Exception
block|{
name|FluentFuture
argument_list|<
name|Integer
argument_list|>
name|f
init|=
name|FluentFuture
operator|.
name|from
argument_list|(
name|immediateFuture
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|transform
argument_list|(
operator|new
name|Function
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|apply
parameter_list|(
name|Integer
name|input
parameter_list|)
block|{
return|return
name|input
operator|+
literal|1
return|;
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|f
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransformAsync ()
specifier|public
name|void
name|testTransformAsync
parameter_list|()
throws|throws
name|Exception
block|{
name|FluentFuture
argument_list|<
name|Integer
argument_list|>
name|f
init|=
name|FluentFuture
operator|.
name|from
argument_list|(
name|immediateFuture
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|transformAsync
argument_list|(
operator|new
name|AsyncFunction
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|Integer
argument_list|>
name|apply
parameter_list|(
name|Integer
name|input
parameter_list|)
block|{
return|return
name|immediateFuture
argument_list|(
name|input
operator|+
literal|1
argument_list|)
return|;
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|f
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// withTimeout
DECL|method|testWithTimeout ()
specifier|public
name|void
name|testWithTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|ScheduledExecutorService
name|executor
init|=
name|newScheduledThreadPool
argument_list|(
literal|1
argument_list|)
decl_stmt|;
try|try
block|{
name|FluentFuture
argument_list|<
name|?
argument_list|>
name|f
init|=
name|FluentFuture
operator|.
name|from
argument_list|(
name|SettableFuture
operator|.
name|create
argument_list|()
argument_list|)
operator|.
name|withTimeout
argument_list|(
literal|0
argument_list|,
name|SECONDS
argument_list|,
name|executor
argument_list|)
decl_stmt|;
try|try
block|{
name|f
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|()
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
argument_list|)
operator|.
name|hasCauseThat
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|TimeoutException
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

