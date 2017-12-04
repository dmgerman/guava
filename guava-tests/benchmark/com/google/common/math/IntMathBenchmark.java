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
name|randomExponent
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
name|randomNonNegativeBigInteger
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

begin_comment
comment|/**  * Benchmarks for the non-rounding methods of {@code IntMath}.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|IntMathBenchmark
specifier|public
class|class
name|IntMathBenchmark
block|{
DECL|field|exponent
specifier|private
specifier|static
name|int
index|[]
name|exponent
init|=
operator|new
name|int
index|[
name|ARRAY_SIZE
index|]
decl_stmt|;
DECL|field|factorial
specifier|private
specifier|static
name|int
index|[]
name|factorial
init|=
operator|new
name|int
index|[
name|ARRAY_SIZE
index|]
decl_stmt|;
DECL|field|binomial
specifier|private
specifier|static
name|int
index|[]
name|binomial
init|=
operator|new
name|int
index|[
name|ARRAY_SIZE
index|]
decl_stmt|;
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
DECL|field|nonnegative
specifier|private
specifier|static
specifier|final
name|int
index|[]
name|nonnegative
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
name|exponent
index|[
name|i
index|]
operator|=
name|randomExponent
argument_list|()
expr_stmt|;
name|factorial
index|[
name|i
index|]
operator|=
name|RANDOM_SOURCE
operator|.
name|nextInt
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|binomial
index|[
name|i
index|]
operator|=
name|RANDOM_SOURCE
operator|.
name|nextInt
argument_list|(
name|factorial
index|[
name|i
index|]
operator|+
literal|1
argument_list|)
expr_stmt|;
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
literal|1
argument_list|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
name|nonnegative
index|[
name|i
index|]
operator|=
name|randomNonNegativeBigInteger
argument_list|(
name|Integer
operator|.
name|SIZE
operator|-
literal|1
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
name|Benchmark
DECL|method|pow (int reps)
name|int
name|pow
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
name|pow
argument_list|(
name|positive
index|[
name|j
index|]
argument_list|,
name|exponent
index|[
name|j
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
annotation|@
name|Benchmark
DECL|method|mod (int reps)
name|int
name|mod
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
name|mod
argument_list|(
name|ints
index|[
name|j
index|]
argument_list|,
name|positive
index|[
name|j
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
annotation|@
name|Benchmark
DECL|method|gCD (int reps)
name|int
name|gCD
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
name|gcd
argument_list|(
name|nonnegative
index|[
name|j
index|]
argument_list|,
name|positive
index|[
name|j
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
annotation|@
name|Benchmark
DECL|method|factorial (int reps)
name|int
name|factorial
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
name|factorial
argument_list|(
name|factorial
index|[
name|j
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
annotation|@
name|Benchmark
DECL|method|binomial (int reps)
name|int
name|binomial
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
name|binomial
argument_list|(
name|factorial
index|[
name|j
index|]
argument_list|,
name|binomial
index|[
name|j
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
annotation|@
name|Benchmark
DECL|method|isPrime (int reps)
name|int
name|isPrime
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
if|if
condition|(
name|IntMath
operator|.
name|isPrime
argument_list|(
name|positive
index|[
name|j
index|]
argument_list|)
condition|)
block|{
name|tmp
operator|++
expr_stmt|;
block|}
block|}
return|return
name|tmp
return|;
block|}
block|}
end_class

end_unit

