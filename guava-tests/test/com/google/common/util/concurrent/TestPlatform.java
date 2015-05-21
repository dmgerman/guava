begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|concurrent
operator|.
name|FuturesTest
operator|.
name|failureWithCause
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
name|FuturesTest
operator|.
name|pseudoTimedGetUninterruptibly
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
name|Uninterruptibles
operator|.
name|getUninterruptibly
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
name|MILLISECONDS
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
name|fail
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
name|TimeoutException
import|;
end_import

begin_comment
comment|/**  * Methods factored out so that they can be emulated differently in GWT.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|TestPlatform
specifier|final
class|class
name|TestPlatform
block|{
DECL|method|verifyGetOnPendingFuture (Future<?> future)
specifier|static
name|void
name|verifyGetOnPendingFuture
parameter_list|(
name|Future
argument_list|<
name|?
argument_list|>
name|future
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|future
argument_list|)
expr_stmt|;
try|try
block|{
name|pseudoTimedGetUninterruptibly
argument_list|(
name|future
argument_list|,
literal|10
argument_list|,
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|expected
parameter_list|)
block|{     }
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
throw|throw
name|failureWithCause
argument_list|(
name|e
argument_list|,
literal|""
argument_list|)
throw|;
block|}
block|}
DECL|method|verifyTimedGetOnPendingFuture (Future<?> future)
specifier|static
name|void
name|verifyTimedGetOnPendingFuture
parameter_list|(
name|Future
argument_list|<
name|?
argument_list|>
name|future
parameter_list|)
block|{
try|try
block|{
name|getUninterruptibly
argument_list|(
name|future
argument_list|,
literal|0
argument_list|,
name|SECONDS
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|expected
parameter_list|)
block|{     }
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
throw|throw
name|failureWithCause
argument_list|(
name|e
argument_list|,
literal|""
argument_list|)
throw|;
block|}
block|}
DECL|method|verifyThreadWasNotInterrupted ()
specifier|static
name|void
name|verifyThreadWasNotInterrupted
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|isInterrupted
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|clearInterrupt ()
specifier|static
name|void
name|clearInterrupt
parameter_list|()
block|{
name|Thread
operator|.
name|interrupted
argument_list|()
expr_stmt|;
block|}
DECL|method|TestPlatform ()
specifier|private
name|TestPlatform
parameter_list|()
block|{}
block|}
end_class

end_unit

