begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
comment|/**  * A benchmark to determine the overhead of sorting with {@link Ordering#from(Comparator)}, or with  * {@link Ordering#natural()}, as opposed to using the inlined {@link Arrays#sort(Object[])}  * implementation, which uses {@link Comparable#compareTo} directly.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|ComparatorDelegationOverheadBenchmark
specifier|public
class|class
name|ComparatorDelegationOverheadBenchmark
block|{
DECL|field|inputArrays
specifier|private
specifier|final
name|Integer
index|[]
index|[]
name|inputArrays
init|=
operator|new
name|Integer
index|[
literal|0x100
index|]
index|[]
decl_stmt|;
annotation|@
name|Param
argument_list|(
block|{
literal|"10000"
block|}
argument_list|)
DECL|field|n
name|int
name|n
decl_stmt|;
annotation|@
name|BeforeExperiment
DECL|method|setUp ()
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
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
name|Integer
index|[]
name|array
init|=
operator|new
name|Integer
index|[
name|n
index|]
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|n
condition|;
name|j
operator|++
control|)
block|{
name|array
index|[
name|j
index|]
operator|=
name|rng
operator|.
name|nextInt
argument_list|()
expr_stmt|;
block|}
name|inputArrays
index|[
name|i
index|]
operator|=
name|array
expr_stmt|;
block|}
block|}
annotation|@
name|Benchmark
DECL|method|arraysSortNoComparator (int reps)
name|int
name|arraysSortNoComparator
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
name|Integer
index|[]
name|copy
init|=
name|inputArrays
index|[
name|i
operator|&
literal|0xFF
index|]
operator|.
name|clone
argument_list|()
decl_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|copy
argument_list|)
expr_stmt|;
name|tmp
operator|+=
name|copy
index|[
literal|0
index|]
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
annotation|@
name|Benchmark
DECL|method|arraysSortOrderingNatural (int reps)
name|int
name|arraysSortOrderingNatural
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
name|Integer
index|[]
name|copy
init|=
name|inputArrays
index|[
name|i
operator|&
literal|0xFF
index|]
operator|.
name|clone
argument_list|()
decl_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|copy
argument_list|,
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
expr_stmt|;
name|tmp
operator|+=
name|copy
index|[
literal|0
index|]
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
DECL|field|NATURAL_INTEGER
specifier|private
specifier|static
specifier|final
name|Comparator
argument_list|<
name|Integer
argument_list|>
name|NATURAL_INTEGER
init|=
operator|new
name|Comparator
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Integer
name|o1
parameter_list|,
name|Integer
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|compareTo
argument_list|(
name|o2
argument_list|)
return|;
block|}
block|}
decl_stmt|;
annotation|@
name|Benchmark
DECL|method|arraysSortOrderingFromNatural (int reps)
name|int
name|arraysSortOrderingFromNatural
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
name|Integer
index|[]
name|copy
init|=
name|inputArrays
index|[
name|i
operator|&
literal|0xFF
index|]
operator|.
name|clone
argument_list|()
decl_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|copy
argument_list|,
name|Ordering
operator|.
name|from
argument_list|(
name|NATURAL_INTEGER
argument_list|)
argument_list|)
expr_stmt|;
name|tmp
operator|+=
name|copy
index|[
literal|0
index|]
expr_stmt|;
block|}
return|return
name|tmp
return|;
block|}
block|}
end_class

end_unit

