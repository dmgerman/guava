begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2014 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|caliper
operator|.
name|BeforeExperiment
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|Benchmark
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|Param
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|api
operator|.
name|VmOptions
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
name|AbstractFutureBenchmarks
operator|.
name|Facade
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
name|AbstractFutureBenchmarks
operator|.
name|Impl
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
name|TimeoutException
import|;
end_import

begin_comment
comment|/** A benchmark that times how long it takes to add a given number of */
end_comment

begin_class
annotation|@
name|VmOptions
argument_list|(
block|{
literal|"-Xms8g"
block|,
literal|"-Xmx8g"
block|}
argument_list|)
DECL|class|SingleThreadAbstractFutureBenchmark
specifier|public
class|class
name|SingleThreadAbstractFutureBenchmark
block|{
DECL|field|impl
annotation|@
name|Param
name|Impl
name|impl
decl_stmt|;
DECL|field|exception
specifier|private
specifier|final
name|Exception
name|exception
init|=
operator|new
name|Exception
argument_list|()
decl_stmt|;
DECL|field|notDoneFuture
specifier|private
name|Facade
argument_list|<
name|?
argument_list|>
name|notDoneFuture
decl_stmt|;
annotation|@
name|BeforeExperiment
DECL|method|setUp ()
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|notDoneFuture
operator|=
name|impl
operator|.
name|newFacade
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|timeComplete_Normal (int reps)
specifier|public
name|long
name|timeComplete_Normal
parameter_list|(
name|int
name|reps
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|r
init|=
literal|0
decl_stmt|;
name|List
argument_list|<
name|Facade
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|reps
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
name|reps
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|Facade
argument_list|<
name|Integer
argument_list|>
name|localFuture
init|=
name|impl
operator|.
name|newFacade
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|localFuture
argument_list|)
expr_stmt|;
name|localFuture
operator|.
name|set
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|reps
condition|;
name|i
operator|++
control|)
block|{
name|r
operator|+=
name|list
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
annotation|@
name|Benchmark
DECL|method|timeComplete_Failure (int reps)
specifier|public
name|long
name|timeComplete_Failure
parameter_list|(
name|int
name|reps
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|r
init|=
literal|0
decl_stmt|;
name|List
argument_list|<
name|Facade
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|reps
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
name|reps
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|Facade
argument_list|<
name|Integer
argument_list|>
name|localFuture
init|=
name|impl
operator|.
name|newFacade
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|localFuture
argument_list|)
expr_stmt|;
name|localFuture
operator|.
name|setException
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|reps
condition|;
name|i
operator|++
control|)
block|{
name|Facade
argument_list|<
name|Integer
argument_list|>
name|facade
init|=
name|list
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
try|try
block|{
name|facade
operator|.
name|get
argument_list|()
expr_stmt|;
name|r
operator|++
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|r
operator|+=
literal|2
expr_stmt|;
block|}
block|}
return|return
name|r
return|;
block|}
annotation|@
name|Benchmark
DECL|method|timeComplete_Cancel (int reps)
specifier|public
name|long
name|timeComplete_Cancel
parameter_list|(
name|int
name|reps
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|r
init|=
literal|0
decl_stmt|;
name|List
argument_list|<
name|Facade
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|reps
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
name|reps
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|Facade
argument_list|<
name|Integer
argument_list|>
name|localFuture
init|=
name|impl
operator|.
name|newFacade
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|localFuture
argument_list|)
expr_stmt|;
name|localFuture
operator|.
name|cancel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|reps
condition|;
name|i
operator|++
control|)
block|{
name|Facade
argument_list|<
name|Integer
argument_list|>
name|facade
init|=
name|list
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
try|try
block|{
name|facade
operator|.
name|get
argument_list|()
expr_stmt|;
name|r
operator|++
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CancellationException
name|e
parameter_list|)
block|{
name|r
operator|+=
literal|2
expr_stmt|;
block|}
block|}
return|return
name|r
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// b/130759882
annotation|@
name|Benchmark
DECL|method|timeGetWith0Timeout (long reps)
specifier|public
name|long
name|timeGetWith0Timeout
parameter_list|(
name|long
name|reps
parameter_list|)
throws|throws
name|Exception
block|{
name|Facade
argument_list|<
name|?
argument_list|>
name|f
init|=
name|notDoneFuture
decl_stmt|;
name|long
name|r
init|=
literal|0
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
name|reps
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|f
operator|.
name|get
argument_list|(
literal|0
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|r
operator|+=
literal|1
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
name|r
operator|+=
literal|2
expr_stmt|;
block|}
block|}
return|return
name|r
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// b/130759882
annotation|@
name|Benchmark
DECL|method|timeGetWithSmallTimeout (long reps)
specifier|public
name|long
name|timeGetWithSmallTimeout
parameter_list|(
name|long
name|reps
parameter_list|)
throws|throws
name|Exception
block|{
name|Facade
argument_list|<
name|?
argument_list|>
name|f
init|=
name|notDoneFuture
decl_stmt|;
name|long
name|r
init|=
literal|0
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
name|reps
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|f
operator|.
name|get
argument_list|(
literal|500
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
expr_stmt|;
name|r
operator|+=
literal|1
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
name|r
operator|+=
literal|2
expr_stmt|;
block|}
block|}
return|return
name|r
return|;
block|}
block|}
end_class

end_unit

