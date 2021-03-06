begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|math
operator|.
name|MathBenchmarking
operator|.
name|ARRAY_MASK
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
name|math
operator|.
name|MathBenchmarking
operator|.
name|ARRAY_SIZE
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
name|math
operator|.
name|MathBenchmarking
operator|.
name|RANDOM_SOURCE
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
name|math
operator|.
name|MathBenchmarking
operator|.
name|randomNonZeroBigInteger
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
name|math
operator|.
name|MathBenchmarking
operator|.
name|randomPositiveBigInteger
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
name|math
operator|.
name|RoundingMode
import|;
end_import

begin_comment
comment|/**  * Benchmarks for the rounding methods of {@code IntMath}.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|IntMathRoundingBenchmark
specifier|public
class|class
name|IntMathRoundingBenchmark
block|{
DECL|field|positive
specifier|private
specifier|static
specifier|final
name|int
index|[]
name|positive
init|=
operator|new
name|int
index|[
name|ARRAY_SIZE
index|]
decl_stmt|;
DECL|field|nonzero
specifier|private
specifier|static
specifier|final
name|int
index|[]
name|nonzero
init|=
operator|new
name|int
index|[
name|ARRAY_SIZE
index|]
decl_stmt|;
DECL|field|ints
specifier|private
specifier|static
specifier|final
name|int
index|[]
name|ints
init|=
operator|new
name|int
index|[
name|ARRAY_SIZE
index|]
decl_stmt|;
annotation|@
name|BeforeExperiment
DECL|method|setUp ()
name|void
name|setUp
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|ARRAY_SIZE
condition|;
name|i
operator|++
control|)
block|{
name|positive
index|[
name|i
index|]
operator|=
name|randomPositiveBigInteger
argument_list|(
name|Integer
operator|.
name|SIZE
operator|-
literal|2
argument_list|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
name|nonzero
index|[
name|i
index|]
operator|=
name|randomNonZeroBigInteger
argument_list|(
name|Integer
operator|.
name|SIZE
operator|-
literal|2
argument_list|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
name|ints
index|[
name|i
index|]
operator|=
name|RANDOM_SOURCE
operator|.
name|nextInt
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Param
argument_list|(
block|{
literal|"DOWN"
block|,
literal|"UP"
block|,
literal|"FLOOR"
block|,
literal|"CEILING"
block|,
literal|"HALF_EVEN"
block|,
literal|"HALF_UP"
block|,
literal|"HALF_DOWN"
block|}
argument_list|)
DECL|field|mode
name|RoundingMode
name|mode
decl_stmt|;
annotation|@
name|Benchmark
DECL|method|log2 (int reps)
name|int
name|log2
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|tmp
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
name|int
name|j
init|=
name|i
operator|&
name|ARRAY_MASK
decl_stmt|;
name|tmp
operator|+=
name|IntMath
operator|.
name|log2
argument_list|(
name|positive
index|[
name|j
index|]
argument_list|,
name|mode
argument_list|)
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
annotation|@
name|Benchmark
DECL|method|log10 (int reps)
name|int
name|log10
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|tmp
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
name|int
name|j
init|=
name|i
operator|&
name|ARRAY_MASK
decl_stmt|;
name|tmp
operator|+=
name|IntMath
operator|.
name|log10
argument_list|(
name|positive
index|[
name|j
index|]
argument_list|,
name|mode
argument_list|)
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
annotation|@
name|Benchmark
DECL|method|sqrt (int reps)
name|int
name|sqrt
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|tmp
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
name|int
name|j
init|=
name|i
operator|&
name|ARRAY_MASK
decl_stmt|;
name|tmp
operator|+=
name|IntMath
operator|.
name|sqrt
argument_list|(
name|positive
index|[
name|j
index|]
argument_list|,
name|mode
argument_list|)
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
annotation|@
name|Benchmark
DECL|method|divide (int reps)
name|int
name|divide
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|tmp
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
name|int
name|j
init|=
name|i
operator|&
name|ARRAY_MASK
decl_stmt|;
name|tmp
operator|+=
name|IntMath
operator|.
name|divide
argument_list|(
name|ints
index|[
name|j
index|]
argument_list|,
name|nonzero
index|[
name|j
index|]
argument_list|,
name|mode
argument_list|)
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
block|}
end_class

end_unit

