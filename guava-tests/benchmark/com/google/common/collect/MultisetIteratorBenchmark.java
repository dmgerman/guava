begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
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
comment|/**  * Tests the speed of iteration of different iteration methods for collections.  *  * @author David Richter  */
end_comment

begin_class
DECL|class|MultisetIteratorBenchmark
specifier|public
class|class
name|MultisetIteratorBenchmark
block|{
DECL|field|size
annotation|@
name|Param
argument_list|(
block|{
literal|"0"
block|,
literal|"1"
block|,
literal|"16"
block|,
literal|"256"
block|,
literal|"4096"
block|,
literal|"65536"
block|}
argument_list|)
name|int
name|size
decl_stmt|;
DECL|field|linkedHashMultiset
name|LinkedHashMultiset
argument_list|<
name|Object
argument_list|>
name|linkedHashMultiset
decl_stmt|;
DECL|field|hashMultiset
name|HashMultiset
argument_list|<
name|Object
argument_list|>
name|hashMultiset
decl_stmt|;
comment|// TreeMultiset requires a Comparable element.
DECL|field|treeMultiset
name|TreeMultiset
argument_list|<
name|Integer
argument_list|>
name|treeMultiset
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|BeforeExperiment
name|void
name|setUp
parameter_list|()
block|{
name|hashMultiset
operator|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|size
argument_list|)
expr_stmt|;
name|linkedHashMultiset
operator|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|(
name|size
argument_list|)
expr_stmt|;
name|treeMultiset
operator|=
name|TreeMultiset
operator|.
name|create
argument_list|()
expr_stmt|;
name|Random
name|random
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|int
name|sizeRemaining
init|=
name|size
decl_stmt|;
comment|// TODO(kevinb): generate better test contents for multisets
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|sizeRemaining
operator|>
literal|0
condition|;
name|i
operator|++
control|)
block|{
comment|// The JVM will return interned values for small ints.
name|Integer
name|value
init|=
name|random
operator|.
name|nextInt
argument_list|(
literal|1000
argument_list|)
operator|+
literal|128
decl_stmt|;
name|int
name|count
init|=
name|Math
operator|.
name|min
argument_list|(
name|random
operator|.
name|nextInt
argument_list|(
literal|10
argument_list|)
operator|+
literal|1
argument_list|,
name|sizeRemaining
argument_list|)
decl_stmt|;
name|sizeRemaining
operator|-=
name|count
expr_stmt|;
name|hashMultiset
operator|.
name|add
argument_list|(
name|value
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|linkedHashMultiset
operator|.
name|add
argument_list|(
name|value
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|treeMultiset
operator|.
name|add
argument_list|(
name|value
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
comment|//TODO(kevinb): convert to assert once benchmark tests enable asserts by default
name|Preconditions
operator|.
name|checkState
argument_list|(
name|hashMultiset
operator|.
name|size
argument_list|()
operator|==
name|size
argument_list|)
expr_stmt|;
block|}
DECL|method|hashMultiset (int reps)
annotation|@
name|Benchmark
name|int
name|hashMultiset
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|sum
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
for|for
control|(
name|Object
name|value
range|:
name|hashMultiset
control|)
block|{
name|sum
operator|+=
name|value
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|sum
return|;
block|}
DECL|method|linkedHashMultiset (int reps)
annotation|@
name|Benchmark
name|int
name|linkedHashMultiset
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|sum
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
for|for
control|(
name|Object
name|value
range|:
name|linkedHashMultiset
control|)
block|{
name|sum
operator|+=
name|value
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|sum
return|;
block|}
DECL|method|treeMultiset (int reps)
annotation|@
name|Benchmark
name|int
name|treeMultiset
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|sum
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
for|for
control|(
name|Object
name|value
range|:
name|treeMultiset
control|)
block|{
name|sum
operator|+=
name|value
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|sum
return|;
block|}
block|}
end_class

end_unit

