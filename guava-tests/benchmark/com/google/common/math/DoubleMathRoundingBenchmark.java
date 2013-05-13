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
name|randomDouble
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
name|randomPositiveDouble
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
name|legacy
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
name|runner
operator|.
name|CaliperMain
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
name|DoubleMath
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
comment|/**  * Benchmarks for the rounding methods of {@code DoubleMath}.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|DoubleMathRoundingBenchmark
specifier|public
class|class
name|DoubleMathRoundingBenchmark
extends|extends
name|Benchmark
block|{
DECL|field|doubleInIntRange
specifier|private
specifier|static
specifier|final
name|double
index|[]
name|doubleInIntRange
init|=
operator|new
name|double
index|[
name|ARRAY_SIZE
index|]
decl_stmt|;
DECL|field|doubleInLongRange
specifier|private
specifier|static
specifier|final
name|double
index|[]
name|doubleInLongRange
init|=
operator|new
name|double
index|[
name|ARRAY_SIZE
index|]
decl_stmt|;
DECL|field|positiveDoubles
specifier|private
specifier|static
specifier|final
name|double
index|[]
name|positiveDoubles
init|=
operator|new
name|double
index|[
name|ARRAY_SIZE
index|]
decl_stmt|;
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
name|doubleInIntRange
index|[
name|i
index|]
operator|=
name|randomDouble
argument_list|(
name|Integer
operator|.
name|SIZE
operator|-
literal|2
argument_list|)
expr_stmt|;
name|doubleInLongRange
index|[
name|i
index|]
operator|=
name|randomDouble
argument_list|(
name|Long
operator|.
name|SIZE
operator|-
literal|2
argument_list|)
expr_stmt|;
name|positiveDoubles
index|[
name|i
index|]
operator|=
name|randomPositiveDouble
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|timeRoundToInt (int reps)
specifier|public
name|int
name|timeRoundToInt
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
name|DoubleMath
operator|.
name|roundToInt
argument_list|(
name|doubleInIntRange
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
DECL|method|timeRoundToLong (int reps)
specifier|public
name|long
name|timeRoundToLong
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
name|DoubleMath
operator|.
name|roundToLong
argument_list|(
name|doubleInLongRange
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
DECL|method|timeRoundToBigInteger (int reps)
specifier|public
name|int
name|timeRoundToBigInteger
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
name|DoubleMath
operator|.
name|roundToBigInteger
argument_list|(
name|positiveDoubles
index|[
name|j
index|]
argument_list|,
name|mode
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
DECL|method|timeLog2Round (int reps)
specifier|public
name|int
name|timeLog2Round
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
name|DoubleMath
operator|.
name|log2
argument_list|(
name|positiveDoubles
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
name|CaliperMain
operator|.
name|main
argument_list|(
name|DoubleMathRoundingBenchmark
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

