begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|getDone
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
name|immediateCancelledFuture
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
name|ExecutionException
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
comment|/**  * Unit tests for {@link Futures#getDone}.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|FuturesGetDoneTest
specifier|public
class|class
name|FuturesGetDoneTest
extends|extends
name|TestCase
block|{
DECL|method|testSuccessful ()
specifier|public
name|void
name|testSuccessful
parameter_list|()
throws|throws
name|ExecutionException
block|{
name|assertThat
argument_list|(
name|getDone
argument_list|(
name|immediateFuture
argument_list|(
literal|"a"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
block|}
DECL|method|testSuccessfulNull ()
specifier|public
name|void
name|testSuccessfulNull
parameter_list|()
throws|throws
name|ExecutionException
block|{
name|assertThat
argument_list|(
name|getDone
argument_list|(
name|immediateFuture
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|testFailed ()
specifier|public
name|void
name|testFailed
parameter_list|()
block|{
name|Exception
name|failureCause
init|=
operator|new
name|Exception
argument_list|()
decl_stmt|;
try|try
block|{
name|getDone
argument_list|(
name|immediateFailedFuture
argument_list|(
name|failureCause
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|expected
parameter_list|)
block|{
name|assertThat
argument_list|(
name|expected
operator|.
name|getCause
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|failureCause
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCancelled ()
specifier|public
name|void
name|testCancelled
parameter_list|()
throws|throws
name|ExecutionException
block|{
try|try
block|{
name|getDone
argument_list|(
name|immediateCancelledFuture
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CancellationException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testPending ()
specifier|public
name|void
name|testPending
parameter_list|()
throws|throws
name|ExecutionException
block|{
try|try
block|{
name|getDone
argument_list|(
name|SettableFuture
operator|.
name|create
argument_list|()
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
block|{     }
block|}
block|}
end_class

end_unit
