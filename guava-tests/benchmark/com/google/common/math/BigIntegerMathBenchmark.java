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
name|java
operator|.
name|math
operator|.
name|RoundingMode
operator|.
name|CEILING
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
name|BigIntegerMath
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
name|IntMath
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

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

begin_comment
comment|/**  * Benchmarks for the non-rounding methods of {@code BigIntegerMath}.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|BigIntegerMathBenchmark
specifier|public
class|class
name|BigIntegerMathBenchmark
extends|extends
name|SimpleBenchmark
block|{
DECL|field|factorials
specifier|private
specifier|static
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
DECL|field|slowFactorials
specifier|private
specifier|static
specifier|final
name|int
index|[]
name|slowFactorials
init|=
operator|new
name|int
index|[
name|ARRAY_SIZE
index|]
decl_stmt|;
DECL|field|binomials
specifier|private
specifier|static
specifier|final
name|int
index|[]
name|binomials
init|=
operator|new
name|int
index|[
name|ARRAY_SIZE
index|]
decl_stmt|;
annotation|@
name|Param
argument_list|(
block|{
literal|"50"
block|,
literal|"1000"
block|,
literal|"10000"
block|}
argument_list|)
DECL|field|factorialBound
name|int
name|factorialBound
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
name|factorials
index|[
name|i
index|]
operator|=
name|RANDOM_SOURCE
operator|.
name|nextInt
argument_list|(
name|factorialBound
argument_list|)
expr_stmt|;
name|slowFactorials
index|[
name|i
index|]
operator|=
name|RANDOM_SOURCE
operator|.
name|nextInt
argument_list|(
name|factorialBound
argument_list|)
expr_stmt|;
name|binomials
index|[
name|i
index|]
operator|=
name|RANDOM_SOURCE
operator|.
name|nextInt
argument_list|(
name|factorials
index|[
name|i
index|]
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Previous version of BigIntegerMath.factorial, kept for timing purposes.    */
DECL|method|slowFactorial (int n)
specifier|private
specifier|static
name|BigInteger
name|slowFactorial
parameter_list|(
name|int
name|n
parameter_list|)
block|{
if|if
condition|(
name|n
operator|<=
literal|20
condition|)
block|{
return|return
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|LongMath
operator|.
name|factorial
argument_list|(
name|n
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
name|int
name|k
init|=
literal|20
decl_stmt|;
return|return
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|LongMath
operator|.
name|factorial
argument_list|(
name|k
argument_list|)
argument_list|)
operator|.
name|multiply
argument_list|(
name|slowFactorial
argument_list|(
name|k
argument_list|,
name|n
argument_list|)
argument_list|)
return|;
block|}
block|}
comment|/**    * Returns the product of {@code n1} exclusive through {@code n2} inclusive.    */
DECL|method|slowFactorial (int n1, int n2)
specifier|private
specifier|static
name|BigInteger
name|slowFactorial
parameter_list|(
name|int
name|n1
parameter_list|,
name|int
name|n2
parameter_list|)
block|{
assert|assert
name|n1
operator|<=
name|n2
assert|;
if|if
condition|(
name|IntMath
operator|.
name|log2
argument_list|(
name|n2
argument_list|,
name|CEILING
argument_list|)
operator|*
operator|(
name|n2
operator|-
name|n1
operator|)
operator|<
name|Long
operator|.
name|SIZE
operator|-
literal|1
condition|)
block|{
comment|// the result will definitely fit into a long
name|long
name|result
init|=
literal|1
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|n1
operator|+
literal|1
init|;
name|i
operator|<=
name|n2
condition|;
name|i
operator|++
control|)
block|{
name|result
operator|*=
name|i
expr_stmt|;
block|}
return|return
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|result
argument_list|)
return|;
block|}
comment|/*      * We want each multiplication to have both sides with approximately the same number of digits.      * Currently, we just divide the range in half.      */
name|int
name|mid
init|=
operator|(
name|n1
operator|+
name|n2
operator|)
operator|>>>
literal|1
decl_stmt|;
return|return
name|slowFactorial
argument_list|(
name|n1
argument_list|,
name|mid
argument_list|)
operator|.
name|multiply
argument_list|(
name|slowFactorial
argument_list|(
name|mid
argument_list|,
name|n2
argument_list|)
argument_list|)
return|;
block|}
DECL|method|timeSlowFactorial (int reps)
specifier|public
name|int
name|timeSlowFactorial
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
name|slowFactorial
argument_list|(
name|slowFactorials
index|[
name|j
index|]
argument_list|)
operator|.
name|intValue
argument_list|()
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
name|BigIntegerMath
operator|.
name|factorial
argument_list|(
name|factorials
index|[
name|j
index|]
argument_list|)
operator|.
name|intValue
argument_list|()
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
literal|0xffff
decl_stmt|;
name|tmp
operator|+=
name|BigIntegerMath
operator|.
name|binomial
argument_list|(
name|factorials
index|[
name|j
index|]
argument_list|,
name|binomials
index|[
name|j
index|]
argument_list|)
operator|.
name|intValue
argument_list|()
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
name|BigIntegerMathBenchmark
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

