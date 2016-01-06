begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2014 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.math
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|math
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
name|common
operator|.
name|collect
operator|.
name|ContiguousSet
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
name|DiscreteDomain
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
name|ImmutableSet
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
name|Range
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_comment
comment|/**  * Benchmarks some algorithms providing the same functionality as {@link Quantiles}.  */
end_comment

begin_class
DECL|class|QuantilesBenchmark
specifier|public
class|class
name|QuantilesBenchmark
block|{
DECL|field|ALL_DECILE_INDEXES
specifier|private
specifier|static
specifier|final
name|ContiguousSet
argument_list|<
name|Integer
argument_list|>
name|ALL_DECILE_INDEXES
init|=
name|ContiguousSet
operator|.
name|create
argument_list|(
name|Range
operator|.
name|closed
argument_list|(
literal|0
argument_list|,
literal|10
argument_list|)
argument_list|,
name|DiscreteDomain
operator|.
name|integers
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Param
argument_list|(
block|{
literal|"10"
block|,
literal|"100"
block|,
literal|"1000"
block|,
literal|"10000"
block|,
literal|"100000"
block|}
argument_list|)
DECL|field|datasetSize
name|int
name|datasetSize
decl_stmt|;
annotation|@
name|Param
DECL|field|algorithm
name|QuantilesAlgorithm
name|algorithm
decl_stmt|;
DECL|field|datasets
specifier|private
name|double
index|[]
index|[]
name|datasets
init|=
operator|new
name|double
index|[
literal|0x100
index|]
index|[]
decl_stmt|;
annotation|@
name|BeforeExperiment
DECL|method|setUp ()
name|void
name|setUp
parameter_list|()
block|{
name|Random
name|rng
init|=
operator|new
name|Random
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
literal|0x100
condition|;
name|i
operator|++
control|)
block|{
name|datasets
index|[
name|i
index|]
operator|=
operator|new
name|double
index|[
name|datasetSize
index|]
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|datasetSize
condition|;
name|j
operator|++
control|)
block|{
name|datasets
index|[
name|i
index|]
index|[
name|j
index|]
operator|=
name|rng
operator|.
name|nextDouble
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|dataset (int i)
specifier|private
name|double
index|[]
name|dataset
parameter_list|(
name|int
name|i
parameter_list|)
block|{
comment|// We must test on a fresh clone of the dataset each time. Doing sorts and quickselects on an
comment|// dataset which is already sorted or partially sorted is cheating.
return|return
name|datasets
index|[
name|i
operator|&
literal|0xFF
index|]
operator|.
name|clone
argument_list|()
return|;
block|}
DECL|method|median (int reps)
annotation|@
name|Benchmark
name|double
name|median
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|double
name|dummy
init|=
literal|0.0
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
name|dummy
operator|+=
name|algorithm
operator|.
name|singleQuantile
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
name|dataset
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
DECL|method|percentile90 (int reps)
annotation|@
name|Benchmark
name|double
name|percentile90
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|double
name|dummy
init|=
literal|0.0
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
name|dummy
operator|+=
name|algorithm
operator|.
name|singleQuantile
argument_list|(
literal|90
argument_list|,
literal|100
argument_list|,
name|dataset
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
DECL|method|percentile99 (int reps)
annotation|@
name|Benchmark
name|double
name|percentile99
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|double
name|dummy
init|=
literal|0.0
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
name|dummy
operator|+=
name|algorithm
operator|.
name|singleQuantile
argument_list|(
literal|99
argument_list|,
literal|100
argument_list|,
name|dataset
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
DECL|method|percentiles90And99 (int reps)
annotation|@
name|Benchmark
name|double
name|percentiles90And99
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|double
name|dummy
init|=
literal|0.0
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
name|dummy
operator|+=
name|algorithm
operator|.
name|multipleQuantiles
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|90
argument_list|,
literal|99
argument_list|)
argument_list|,
literal|100
argument_list|,
name|dataset
argument_list|(
name|i
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|90
argument_list|)
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
DECL|method|threePercentiles (int reps)
annotation|@
name|Benchmark
name|double
name|threePercentiles
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|double
name|dummy
init|=
literal|0.0
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
name|dummy
operator|+=
name|algorithm
operator|.
name|multipleQuantiles
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|90
argument_list|,
literal|95
argument_list|,
literal|99
argument_list|)
argument_list|,
literal|100
argument_list|,
name|dataset
argument_list|(
name|i
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|90
argument_list|)
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
DECL|method|allDeciles (int reps)
annotation|@
name|Benchmark
name|double
name|allDeciles
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|double
name|dummy
init|=
literal|0.0
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
name|dummy
operator|+=
name|algorithm
operator|.
name|multipleQuantiles
argument_list|(
name|ALL_DECILE_INDEXES
argument_list|,
literal|10
argument_list|,
name|dataset
argument_list|(
name|i
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|9
argument_list|)
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
block|}
end_class

end_unit
