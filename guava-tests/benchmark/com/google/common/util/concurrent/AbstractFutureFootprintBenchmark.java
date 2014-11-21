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
name|Footprint
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
name|SkipThisScenarioException
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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

begin_comment
comment|/**  * Measures the size of AbstractFuture implementations.  */
end_comment

begin_class
DECL|class|AbstractFutureFootprintBenchmark
specifier|public
class|class
name|AbstractFutureFootprintBenchmark
block|{
DECL|enum|State
DECL|enumConstant|NOT_DONE
DECL|enumConstant|FINISHED
DECL|enumConstant|CANCELLED
DECL|enumConstant|FAILED
enum|enum
name|State
block|{
name|NOT_DONE
block|,
name|FINISHED
block|,
name|CANCELLED
block|,
name|FAILED
block|}
DECL|field|state
annotation|@
name|Param
name|State
name|state
decl_stmt|;
DECL|field|impl
annotation|@
name|Param
name|Impl
name|impl
decl_stmt|;
DECL|field|numListeners
annotation|@
name|Param
argument_list|(
block|{
literal|"0"
block|,
literal|"1"
block|,
literal|"5"
block|,
literal|"10"
block|}
argument_list|)
name|int
name|numListeners
decl_stmt|;
DECL|field|numThreads
annotation|@
name|Param
argument_list|(
block|{
literal|"0"
block|,
literal|"1"
block|,
literal|"5"
block|,
literal|"10"
block|}
argument_list|)
name|int
name|numThreads
decl_stmt|;
DECL|field|blockedThreads
specifier|private
specifier|final
name|Set
argument_list|<
name|Thread
argument_list|>
name|blockedThreads
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|BeforeExperiment
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|state
operator|!=
name|State
operator|.
name|NOT_DONE
operator|&&
operator|(
name|numListeners
operator|!=
literal|0
operator|||
name|numThreads
operator|!=
literal|0
operator|)
condition|)
block|{
throw|throw
operator|new
name|SkipThisScenarioException
argument_list|()
throw|;
block|}
block|}
comment|// This exclusion doesn't exclude the TOMBSTONE objects we set. So 'done' NEW futures will look
comment|// larger than they are.
annotation|@
name|Footprint
argument_list|(
name|exclude
operator|=
block|{
name|Runnable
operator|.
name|class
block|,
name|Executor
operator|.
name|class
block|,
name|Thread
operator|.
name|class
block|,
name|Exception
operator|.
name|class
block|}
argument_list|)
DECL|method|measureSize ()
specifier|public
name|Object
name|measureSize
parameter_list|()
block|{
for|for
control|(
name|Thread
name|thread
range|:
name|blockedThreads
control|)
block|{
name|thread
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
name|blockedThreads
operator|.
name|clear
argument_list|()
expr_stmt|;
specifier|final
name|Facade
argument_list|<
name|Object
argument_list|>
name|f
init|=
name|impl
operator|.
name|newFacade
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
name|numThreads
condition|;
name|i
operator|++
control|)
block|{
name|Thread
name|thread
init|=
operator|new
name|Thread
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
name|f
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|expected
parameter_list|)
block|{}
block|}
block|}
decl_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
name|blockedThreads
operator|.
name|add
argument_list|(
name|thread
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
name|numListeners
condition|;
name|i
operator|++
control|)
block|{
name|f
operator|.
name|addListener
argument_list|(
name|Runnables
operator|.
name|doNothing
argument_list|()
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Thread
name|thread
range|:
name|blockedThreads
control|)
block|{
name|AbstractFutureBenchmarks
operator|.
name|awaitWaiting
argument_list|(
name|thread
argument_list|)
expr_stmt|;
block|}
switch|switch
condition|(
name|state
condition|)
block|{
case|case
name|NOT_DONE
case|:
break|break;
case|case
name|FINISHED
case|:
name|f
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
break|break;
case|case
name|CANCELLED
case|:
name|f
operator|.
name|cancel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
break|break;
case|case
name|FAILED
case|:
name|f
operator|.
name|setException
argument_list|(
operator|new
name|Exception
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
return|return
name|f
return|;
block|}
block|}
end_class

end_unit

