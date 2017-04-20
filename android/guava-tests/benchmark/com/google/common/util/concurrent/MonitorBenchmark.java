begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
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
name|BlockingQueue
import|;
end_import

begin_comment
comment|/**  * Benchmarks for {@link Monitor}.  *  * @author Justin T. Sampson  */
end_comment

begin_class
DECL|class|MonitorBenchmark
specifier|public
class|class
name|MonitorBenchmark
block|{
DECL|field|capacity
annotation|@
name|Param
argument_list|(
block|{
literal|"10"
block|,
literal|"100"
block|,
literal|"1000"
block|}
argument_list|)
name|int
name|capacity
decl_stmt|;
DECL|field|queueType
annotation|@
name|Param
argument_list|(
block|{
literal|"Array"
block|,
literal|"Priority"
block|}
argument_list|)
name|String
name|queueType
decl_stmt|;
DECL|field|useMonitor
annotation|@
name|Param
name|boolean
name|useMonitor
decl_stmt|;
DECL|field|queue
specifier|private
name|BlockingQueue
argument_list|<
name|String
argument_list|>
name|queue
decl_stmt|;
DECL|field|strings
specifier|private
name|String
index|[]
name|strings
decl_stmt|;
annotation|@
name|BeforeExperiment
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|setUp ()
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|prefix
init|=
operator|(
name|useMonitor
condition|?
literal|"com.google.common.util.concurrent.MonitorBased"
else|:
literal|"java.util.concurrent."
operator|)
decl_stmt|;
name|String
name|className
init|=
name|prefix
operator|+
name|queueType
operator|+
literal|"BlockingQueue"
decl_stmt|;
name|Constructor
argument_list|<
name|?
argument_list|>
name|constructor
init|=
name|Class
operator|.
name|forName
argument_list|(
name|className
argument_list|)
operator|.
name|getConstructor
argument_list|(
name|int
operator|.
name|class
argument_list|)
decl_stmt|;
name|queue
operator|=
operator|(
name|BlockingQueue
argument_list|<
name|String
argument_list|>
operator|)
name|constructor
operator|.
name|newInstance
argument_list|(
name|capacity
argument_list|)
expr_stmt|;
name|strings
operator|=
operator|new
name|String
index|[
name|capacity
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|capacity
condition|;
name|i
operator|++
control|)
block|{
name|strings
index|[
name|i
index|]
operator|=
name|String
operator|.
name|valueOf
argument_list|(
name|Math
operator|.
name|random
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addsAndRemoves (int reps)
annotation|@
name|Benchmark
name|void
name|addsAndRemoves
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|capacity
init|=
name|this
operator|.
name|capacity
decl_stmt|;
name|BlockingQueue
argument_list|<
name|String
argument_list|>
name|queue
init|=
name|this
operator|.
name|queue
decl_stmt|;
name|String
index|[]
name|strings
init|=
name|this
operator|.
name|strings
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
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|capacity
condition|;
name|j
operator|++
control|)
block|{
name|queue
operator|.
name|add
argument_list|(
name|strings
index|[
name|j
index|]
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|capacity
condition|;
name|j
operator|++
control|)
block|{
name|queue
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

