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
name|randomBigInteger
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

begin_comment
comment|/**  * Benchmarks against the Apache Commons Math utilities.  *  *<p>Note: the Apache benchmarks are not open sourced to avoid the extra dependency.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|ApacheBenchmark
specifier|public
class|class
name|ApacheBenchmark
block|{
DECL|enum|Impl
specifier|private
enum|enum
name|Impl
block|{
DECL|enumConstant|GUAVA
name|GUAVA
block|{
annotation|@
name|Override
specifier|public
name|double
name|factorialDouble
parameter_list|(
name|int
name|n
parameter_list|)
block|{
return|return
name|DoubleMath
operator|.
name|factorial
argument_list|(
name|n
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|gcdInt
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
block|{
return|return
name|IntMath
operator|.
name|gcd
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|gcdLong
parameter_list|(
name|long
name|a
parameter_list|,
name|long
name|b
parameter_list|)
block|{
return|return
name|LongMath
operator|.
name|gcd
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|binomialCoefficient
parameter_list|(
name|int
name|n
parameter_list|,
name|int
name|k
parameter_list|)
block|{
return|return
name|LongMath
operator|.
name|binomial
argument_list|(
name|n
argument_list|,
name|k
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|noAddOverflow
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
block|{
try|try
block|{
name|int
name|unused
init|=
name|IntMath
operator|.
name|checkedAdd
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
decl_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|noAddOverflow
parameter_list|(
name|long
name|a
parameter_list|,
name|long
name|b
parameter_list|)
block|{
try|try
block|{
name|long
name|unused
init|=
name|LongMath
operator|.
name|checkedAdd
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
decl_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|noMulOverflow
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
block|{
try|try
block|{
name|int
name|unused
init|=
name|IntMath
operator|.
name|checkedMultiply
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
decl_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|noMulOverflow
parameter_list|(
name|long
name|a
parameter_list|,
name|long
name|b
parameter_list|)
block|{
try|try
block|{
name|long
name|unused
init|=
name|LongMath
operator|.
name|checkedMultiply
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
decl_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
block|;
DECL|method|factorialDouble (int n)
specifier|public
specifier|abstract
name|double
name|factorialDouble
parameter_list|(
name|int
name|n
parameter_list|)
function_decl|;
DECL|method|binomialCoefficient (int n, int k)
specifier|public
specifier|abstract
name|long
name|binomialCoefficient
parameter_list|(
name|int
name|n
parameter_list|,
name|int
name|k
parameter_list|)
function_decl|;
DECL|method|gcdInt (int a, int b)
specifier|public
specifier|abstract
name|int
name|gcdInt
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
function_decl|;
DECL|method|gcdLong (long a, long b)
specifier|public
specifier|abstract
name|long
name|gcdLong
parameter_list|(
name|long
name|a
parameter_list|,
name|long
name|b
parameter_list|)
function_decl|;
DECL|method|noAddOverflow (int a, int b)
specifier|public
specifier|abstract
name|boolean
name|noAddOverflow
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
function_decl|;
DECL|method|noAddOverflow (long a, long b)
specifier|public
specifier|abstract
name|boolean
name|noAddOverflow
parameter_list|(
name|long
name|a
parameter_list|,
name|long
name|b
parameter_list|)
function_decl|;
DECL|method|noMulOverflow (int a, int b)
specifier|public
specifier|abstract
name|boolean
name|noMulOverflow
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
function_decl|;
DECL|method|noMulOverflow (long a, long b)
specifier|public
specifier|abstract
name|boolean
name|noMulOverflow
parameter_list|(
name|long
name|a
parameter_list|,
name|long
name|b
parameter_list|)
function_decl|;
block|}
DECL|field|factorials
specifier|private
specifier|final
name|int
index|[]
name|factorials
init|=
operator|new
name|int
index|[
name|ARRAY_SIZE
index|]
decl_stmt|;
DECL|field|binomials
specifier|private
specifier|final
name|int
index|[]
index|[]
name|binomials
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
DECL|field|nonnegInt
specifier|private
specifier|final
name|int
index|[]
index|[]
name|nonnegInt
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
DECL|field|nonnegLong
specifier|private
specifier|final
name|long
index|[]
index|[]
name|nonnegLong
init|=
operator|new
name|long
index|[
name|ARRAY_SIZE
index|]
index|[
literal|2
index|]
decl_stmt|;
DECL|field|intsToAdd
specifier|private
specifier|final
name|int
index|[]
index|[]
name|intsToAdd
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
DECL|field|intsToMul
specifier|private
specifier|final
name|int
index|[]
index|[]
name|intsToMul
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
DECL|field|longsToAdd
specifier|private
specifier|final
name|long
index|[]
index|[]
name|longsToAdd
init|=
operator|new
name|long
index|[
name|ARRAY_SIZE
index|]
index|[
literal|2
index|]
decl_stmt|;
DECL|field|longsToMul
specifier|private
specifier|final
name|long
index|[]
index|[]
name|longsToMul
init|=
operator|new
name|long
index|[
name|ARRAY_SIZE
index|]
index|[
literal|2
index|]
decl_stmt|;
annotation|@
name|Param
argument_list|(
block|{
literal|"APACHE"
block|,
literal|"GUAVA"
block|}
argument_list|)
DECL|field|impl
name|Impl
name|impl
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
name|factorials
index|[
name|i
index|]
operator|=
name|RANDOM_SOURCE
operator|.
name|nextInt
argument_list|(
literal|200
argument_list|)
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
literal|2
condition|;
name|j
operator|++
control|)
block|{
name|nonnegInt
index|[
name|i
index|]
index|[
name|j
index|]
operator|=
name|randomNonNegativeBigInteger
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
name|nonnegLong
index|[
name|i
index|]
index|[
name|j
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
block|}
do|do
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
literal|2
condition|;
name|j
operator|++
control|)
block|{
name|intsToAdd
index|[
name|i
index|]
index|[
name|j
index|]
operator|=
name|randomBigInteger
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
block|}
block|}
do|while
condition|(
operator|!
name|Impl
operator|.
name|GUAVA
operator|.
name|noAddOverflow
argument_list|(
name|intsToAdd
index|[
name|i
index|]
index|[
literal|0
index|]
argument_list|,
name|intsToAdd
index|[
name|i
index|]
index|[
literal|1
index|]
argument_list|)
condition|)
do|;
do|do
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
literal|2
condition|;
name|j
operator|++
control|)
block|{
name|longsToAdd
index|[
name|i
index|]
index|[
name|j
index|]
operator|=
name|randomBigInteger
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
block|}
block|}
do|while
condition|(
operator|!
name|Impl
operator|.
name|GUAVA
operator|.
name|noAddOverflow
argument_list|(
name|longsToAdd
index|[
name|i
index|]
index|[
literal|0
index|]
argument_list|,
name|longsToAdd
index|[
name|i
index|]
index|[
literal|1
index|]
argument_list|)
condition|)
do|;
do|do
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
literal|2
condition|;
name|j
operator|++
control|)
block|{
name|intsToMul
index|[
name|i
index|]
index|[
name|j
index|]
operator|=
name|randomBigInteger
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
block|}
block|}
do|while
condition|(
operator|!
name|Impl
operator|.
name|GUAVA
operator|.
name|noMulOverflow
argument_list|(
name|intsToMul
index|[
name|i
index|]
index|[
literal|0
index|]
argument_list|,
name|intsToMul
index|[
name|i
index|]
index|[
literal|1
index|]
argument_list|)
condition|)
do|;
do|do
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
literal|2
condition|;
name|j
operator|++
control|)
block|{
name|longsToMul
index|[
name|i
index|]
index|[
name|j
index|]
operator|=
name|randomBigInteger
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
block|}
block|}
do|while
condition|(
operator|!
name|Impl
operator|.
name|GUAVA
operator|.
name|noMulOverflow
argument_list|(
name|longsToMul
index|[
name|i
index|]
index|[
literal|0
index|]
argument_list|,
name|longsToMul
index|[
name|i
index|]
index|[
literal|1
index|]
argument_list|)
condition|)
do|;
name|int
name|k
init|=
name|binomials
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
decl_stmt|;
name|binomials
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
DECL|method|factorialDouble (int reps)
annotation|@
name|Benchmark
name|long
name|factorialDouble
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|long
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
name|Double
operator|.
name|doubleToRawLongBits
argument_list|(
name|impl
operator|.
name|factorialDouble
argument_list|(
name|factorials
index|[
name|j
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
DECL|method|intGCD (int reps)
annotation|@
name|Benchmark
name|int
name|intGCD
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
name|impl
operator|.
name|gcdInt
argument_list|(
name|nonnegInt
index|[
name|j
index|]
index|[
literal|0
index|]
argument_list|,
name|nonnegInt
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
DECL|method|longGCD (int reps)
annotation|@
name|Benchmark
name|long
name|longGCD
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|long
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
name|impl
operator|.
name|gcdLong
argument_list|(
name|nonnegLong
index|[
name|j
index|]
index|[
literal|0
index|]
argument_list|,
name|nonnegLong
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
DECL|method|binomialCoefficient (int reps)
annotation|@
name|Benchmark
name|long
name|binomialCoefficient
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|long
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
name|impl
operator|.
name|binomialCoefficient
argument_list|(
name|binomials
index|[
name|j
index|]
index|[
literal|0
index|]
argument_list|,
name|binomials
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
DECL|method|intAddOverflow (int reps)
annotation|@
name|Benchmark
name|int
name|intAddOverflow
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
name|impl
operator|.
name|noAddOverflow
argument_list|(
name|intsToAdd
index|[
name|j
index|]
index|[
literal|0
index|]
argument_list|,
name|intsToAdd
index|[
name|j
index|]
index|[
literal|1
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
DECL|method|longAddOverflow (int reps)
annotation|@
name|Benchmark
name|int
name|longAddOverflow
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
name|impl
operator|.
name|noAddOverflow
argument_list|(
name|longsToAdd
index|[
name|j
index|]
index|[
literal|0
index|]
argument_list|,
name|longsToAdd
index|[
name|j
index|]
index|[
literal|1
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
DECL|method|intMulOverflow (int reps)
annotation|@
name|Benchmark
name|int
name|intMulOverflow
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
name|impl
operator|.
name|noMulOverflow
argument_list|(
name|intsToMul
index|[
name|j
index|]
index|[
literal|0
index|]
argument_list|,
name|intsToMul
index|[
name|j
index|]
index|[
literal|1
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
DECL|method|longMulOverflow (int reps)
annotation|@
name|Benchmark
name|int
name|longMulOverflow
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
name|impl
operator|.
name|noMulOverflow
argument_list|(
name|longsToMul
index|[
name|j
index|]
index|[
literal|0
index|]
argument_list|,
name|longsToMul
index|[
name|j
index|]
index|[
literal|1
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

