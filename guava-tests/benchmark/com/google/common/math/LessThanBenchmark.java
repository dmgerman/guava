begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_comment
comment|/**  * Benchmarks for various ways of writing the expression {@code foo + ((bar< baz) ? 1 : 0)}.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|LessThanBenchmark
specifier|public
class|class
name|LessThanBenchmark
block|{
DECL|field|SAMPLE_SIZE
specifier|static
specifier|final
name|int
name|SAMPLE_SIZE
init|=
literal|0x1000
decl_stmt|;
DECL|field|SAMPLE_MASK
specifier|static
specifier|final
name|int
name|SAMPLE_MASK
init|=
literal|0x0FFF
decl_stmt|;
annotation|@
name|Param
argument_list|(
literal|"1234"
argument_list|)
DECL|field|randomSeed
name|int
name|randomSeed
decl_stmt|;
DECL|field|xInts
name|int
index|[]
name|xInts
decl_stmt|;
DECL|field|yInts
name|int
index|[]
name|yInts
decl_stmt|;
DECL|field|xLongs
name|long
index|[]
name|xLongs
decl_stmt|;
DECL|field|yLongs
name|long
index|[]
name|yLongs
decl_stmt|;
DECL|field|constant
name|int
index|[]
name|constant
decl_stmt|;
DECL|field|NONNEGATIVE_LONG_MASK
specifier|private
specifier|static
specifier|final
name|long
name|NONNEGATIVE_LONG_MASK
init|=
literal|0x7FFFFFFFFFFFFFFFL
decl_stmt|;
annotation|@
name|BeforeExperiment
DECL|method|setUp ()
name|void
name|setUp
parameter_list|()
block|{
name|Random
name|random
init|=
operator|new
name|Random
argument_list|(
name|randomSeed
argument_list|)
decl_stmt|;
name|xInts
operator|=
operator|new
name|int
index|[
name|SAMPLE_SIZE
index|]
expr_stmt|;
name|yInts
operator|=
operator|new
name|int
index|[
name|SAMPLE_SIZE
index|]
expr_stmt|;
name|xLongs
operator|=
operator|new
name|long
index|[
name|SAMPLE_SIZE
index|]
expr_stmt|;
name|yLongs
operator|=
operator|new
name|long
index|[
name|SAMPLE_SIZE
index|]
expr_stmt|;
name|constant
operator|=
operator|new
name|int
index|[
name|SAMPLE_SIZE
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
name|SAMPLE_SIZE
condition|;
name|i
operator|++
control|)
block|{
name|xInts
index|[
name|i
index|]
operator|=
name|random
operator|.
name|nextInt
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
name|yInts
index|[
name|i
index|]
operator|=
name|random
operator|.
name|nextInt
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
name|xLongs
index|[
name|i
index|]
operator|=
name|random
operator|.
name|nextLong
argument_list|()
operator|&
name|NONNEGATIVE_LONG_MASK
expr_stmt|;
name|yLongs
index|[
name|i
index|]
operator|=
name|random
operator|.
name|nextLong
argument_list|()
operator|&
name|NONNEGATIVE_LONG_MASK
expr_stmt|;
name|constant
index|[
name|i
index|]
operator|=
name|random
operator|.
name|nextInt
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Benchmark
DECL|method|branchFreeLtIntInlined (int reps)
name|int
name|branchFreeLtIntInlined
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
name|SAMPLE_MASK
decl_stmt|;
name|int
name|x
init|=
name|xInts
index|[
name|j
index|]
decl_stmt|;
name|int
name|y
init|=
name|yInts
index|[
name|j
index|]
decl_stmt|;
name|int
name|z
init|=
name|constant
index|[
name|j
index|]
decl_stmt|;
name|tmp
operator|+=
name|z
operator|+
operator|(
operator|(
name|x
operator|-
name|y
operator|)
operator|>>>
operator|(
name|Integer
operator|.
name|SIZE
operator|-
literal|1
operator|)
operator|)
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
annotation|@
name|Benchmark
DECL|method|branchFreeLtInt (int reps)
name|int
name|branchFreeLtInt
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
name|SAMPLE_MASK
decl_stmt|;
name|int
name|x
init|=
name|xInts
index|[
name|j
index|]
decl_stmt|;
name|int
name|y
init|=
name|yInts
index|[
name|j
index|]
decl_stmt|;
name|int
name|z
init|=
name|constant
index|[
name|j
index|]
decl_stmt|;
name|tmp
operator|+=
name|z
operator|+
name|IntMath
operator|.
name|lessThanBranchFree
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
annotation|@
name|Benchmark
DECL|method|ternaryLtIntAddOutsideTernary (int reps)
name|int
name|ternaryLtIntAddOutsideTernary
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
name|SAMPLE_MASK
decl_stmt|;
name|int
name|x
init|=
name|xInts
index|[
name|j
index|]
decl_stmt|;
name|int
name|y
init|=
name|yInts
index|[
name|j
index|]
decl_stmt|;
name|int
name|z
init|=
name|constant
index|[
name|j
index|]
decl_stmt|;
name|tmp
operator|+=
name|z
operator|+
operator|(
operator|(
name|x
operator|<
name|y
operator|)
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
annotation|@
name|Benchmark
DECL|method|ternaryLtIntAddInsideTernary (int reps)
name|int
name|ternaryLtIntAddInsideTernary
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
name|SAMPLE_MASK
decl_stmt|;
name|int
name|x
init|=
name|xInts
index|[
name|j
index|]
decl_stmt|;
name|int
name|y
init|=
name|yInts
index|[
name|j
index|]
decl_stmt|;
name|int
name|z
init|=
name|constant
index|[
name|j
index|]
decl_stmt|;
name|tmp
operator|+=
operator|(
name|x
operator|<
name|y
operator|)
condition|?
name|z
operator|+
literal|1
else|:
name|z
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
annotation|@
name|Benchmark
DECL|method|branchFreeLtLongInlined (int reps)
name|int
name|branchFreeLtLongInlined
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
name|SAMPLE_MASK
decl_stmt|;
name|long
name|x
init|=
name|xLongs
index|[
name|j
index|]
decl_stmt|;
name|long
name|y
init|=
name|yLongs
index|[
name|j
index|]
decl_stmt|;
name|int
name|z
init|=
name|constant
index|[
name|j
index|]
decl_stmt|;
name|tmp
operator|+=
name|z
operator|+
call|(
name|int
call|)
argument_list|(
operator|(
name|x
operator|-
name|y
operator|)
operator|>>>
operator|(
name|Long
operator|.
name|SIZE
operator|-
literal|1
operator|)
argument_list|)
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
annotation|@
name|Benchmark
DECL|method|branchFreeLtLong (int reps)
name|int
name|branchFreeLtLong
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
name|SAMPLE_MASK
decl_stmt|;
name|long
name|x
init|=
name|xLongs
index|[
name|j
index|]
decl_stmt|;
name|long
name|y
init|=
name|yLongs
index|[
name|j
index|]
decl_stmt|;
name|int
name|z
init|=
name|constant
index|[
name|j
index|]
decl_stmt|;
name|tmp
operator|+=
name|z
operator|+
name|LongMath
operator|.
name|lessThanBranchFree
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
annotation|@
name|Benchmark
DECL|method|ternaryLtLongAddOutsideTernary (int reps)
name|int
name|ternaryLtLongAddOutsideTernary
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
name|SAMPLE_MASK
decl_stmt|;
name|long
name|x
init|=
name|xLongs
index|[
name|j
index|]
decl_stmt|;
name|long
name|y
init|=
name|yLongs
index|[
name|j
index|]
decl_stmt|;
name|int
name|z
init|=
name|constant
index|[
name|j
index|]
decl_stmt|;
name|tmp
operator|+=
name|z
operator|+
operator|(
operator|(
name|x
operator|<
name|y
operator|)
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
annotation|@
name|Benchmark
DECL|method|ternaryLtLongAddInsideTernary (int reps)
name|int
name|ternaryLtLongAddInsideTernary
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
name|SAMPLE_MASK
decl_stmt|;
name|long
name|x
init|=
name|xLongs
index|[
name|j
index|]
decl_stmt|;
name|long
name|y
init|=
name|yLongs
index|[
name|j
index|]
decl_stmt|;
name|int
name|z
init|=
name|constant
index|[
name|j
index|]
decl_stmt|;
name|tmp
operator|+=
operator|(
name|x
operator|<
name|y
operator|)
condition|?
name|z
operator|+
literal|1
else|:
name|z
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
block|}
end_class

end_unit

