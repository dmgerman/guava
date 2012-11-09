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
name|Runner
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
name|SimpleBenchmark
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
name|math
operator|.
name|LongMath
import|;
end_import

begin_comment
comment|/**  * Benchmarks for the non-rounding methods of {@code LongMath}.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|LongMathBenchmark
specifier|public
class|class
name|LongMathBenchmark
extends|extends
name|SimpleBenchmark
block|{
DECL|field|exponents
specifier|private
specifier|static
specifier|final
name|int
index|[]
name|exponents
init|=
operator|new
name|int
index|[
name|ARRAY_SIZE
index|]
decl_stmt|;
DECL|field|factorialArguments
specifier|private
specifier|static
specifier|final
name|int
index|[]
name|factorialArguments
init|=
operator|new
name|int
index|[
name|ARRAY_SIZE
index|]
decl_stmt|;
DECL|field|binomialArguments
specifier|private
specifier|static
specifier|final
name|int
index|[]
index|[]
name|binomialArguments
init|=
operator|new
name|int
index|[
name|ARRAY_SIZE
index|]
index|[
literal|2
index|]
decl_stmt|;
DECL|field|positive
specifier|private
specifier|static
specifier|final
name|long
index|[]
name|positive
init|=
operator|new
name|long
index|[
name|ARRAY_SIZE
index|]
decl_stmt|;
DECL|field|nonnegative
specifier|private
specifier|static
specifier|final
name|long
index|[]
name|nonnegative
init|=
operator|new
name|long
index|[
name|ARRAY_SIZE
index|]
decl_stmt|;
DECL|field|longs
specifier|private
specifier|static
specifier|final
name|long
index|[]
name|longs
init|=
operator|new
name|long
index|[
name|ARRAY_SIZE
index|]
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
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
name|exponents
index|[
name|i
index|]
operator|=
name|randomExponent
argument_list|()
expr_stmt|;
name|positive
index|[
name|i
index|]
operator|=
name|randomPositiveBigInteger
argument_list|(
name|Long
operator|.
name|SIZE
operator|-
literal|2
argument_list|)
operator|.
name|longValue
argument_list|()
expr_stmt|;
name|nonnegative
index|[
name|i
index|]
operator|=
name|randomNonNegativeBigInteger
argument_list|(
name|Long
operator|.
name|SIZE
operator|-
literal|2
argument_list|)
operator|.
name|longValue
argument_list|()
expr_stmt|;
name|longs
index|[
name|i
index|]
operator|=
name|RANDOM_SOURCE
operator|.
name|nextLong
argument_list|()
expr_stmt|;
name|factorialArguments
index|[
name|i
index|]
operator|=
name|RANDOM_SOURCE
operator|.
name|nextInt
argument_list|(
literal|30
argument_list|)
expr_stmt|;
name|binomialArguments
index|[
name|i
index|]
index|[
literal|1
index|]
operator|=
name|RANDOM_SOURCE
operator|.
name|nextInt
argument_list|(
name|MathBenchmarking
operator|.
name|biggestBinomials
operator|.
name|length
argument_list|)
expr_stmt|;
name|int
name|k
init|=
name|binomialArguments
index|[
name|i
index|]
index|[
literal|1
index|]
decl_stmt|;
name|binomialArguments
index|[
name|i
index|]
index|[
literal|0
index|]
operator|=
name|RANDOM_SOURCE
operator|.
name|nextInt
argument_list|(
name|MathBenchmarking
operator|.
name|biggestBinomials
index|[
name|k
index|]
operator|-
name|k
argument_list|)
operator|+
name|k
expr_stmt|;
block|}
block|}
DECL|method|timePow (int reps)
specifier|public
name|int
name|timePow
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
name|LongMath
operator|.
name|pow
argument_list|(
name|positive
index|[
name|j
index|]
argument_list|,
name|exponents
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
DECL|method|timeMod (int reps)
specifier|public
name|int
name|timeMod
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
name|LongMath
operator|.
name|mod
argument_list|(
name|longs
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
DECL|method|timeGCD (int reps)
specifier|public
name|int
name|timeGCD
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
name|LongMath
operator|.
name|mod
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
DECL|method|timeFactorial (int reps)
specifier|public
name|int
name|timeFactorial
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
name|LongMath
operator|.
name|factorial
argument_list|(
name|factorialArguments
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
DECL|method|timeBinomial (int reps)
specifier|public
name|int
name|timeBinomial
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
name|LongMath
operator|.
name|binomial
argument_list|(
name|binomialArguments
index|[
name|j
index|]
index|[
literal|0
index|]
argument_list|,
name|binomialArguments
index|[
name|j
index|]
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|Runner
operator|.
name|main
argument_list|(
name|LongMathBenchmark
operator|.
name|class
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

