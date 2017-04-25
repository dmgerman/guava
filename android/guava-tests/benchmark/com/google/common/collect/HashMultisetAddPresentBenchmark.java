begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the  * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  * express or implied. See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
comment|/**  * Benchmark for HashMultiset.add for an already-present element.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|HashMultisetAddPresentBenchmark
specifier|public
class|class
name|HashMultisetAddPresentBenchmark
block|{
DECL|field|ARRAY_MASK
specifier|private
specifier|static
specifier|final
name|int
name|ARRAY_MASK
init|=
literal|0x0ffff
decl_stmt|;
DECL|field|ARRAY_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|ARRAY_SIZE
init|=
literal|0x10000
decl_stmt|;
DECL|field|multisets
name|List
argument_list|<
name|Multiset
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|multisets
init|=
operator|new
name|ArrayList
argument_list|<
name|Multiset
argument_list|<
name|Integer
argument_list|>
argument_list|>
argument_list|(
literal|0x10000
argument_list|)
decl_stmt|;
DECL|field|queries
name|int
index|[]
name|queries
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
name|Random
name|random
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|multisets
operator|.
name|clear
argument_list|()
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
name|ARRAY_SIZE
condition|;
name|i
operator|++
control|)
block|{
name|HashMultiset
argument_list|<
name|Integer
argument_list|>
name|multiset
init|=
name|HashMultiset
operator|.
expr|<
name|Integer
operator|>
name|create
argument_list|()
decl_stmt|;
name|multisets
operator|.
name|add
argument_list|(
name|multiset
argument_list|)
expr_stmt|;
name|queries
index|[
name|i
index|]
operator|=
name|random
operator|.
name|nextInt
argument_list|()
expr_stmt|;
name|multiset
operator|.
name|add
argument_list|(
name|queries
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|add (int reps)
annotation|@
name|Benchmark
name|int
name|add
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
name|multisets
operator|.
name|get
argument_list|(
name|j
argument_list|)
operator|.
name|add
argument_list|(
name|queries
index|[
name|j
index|]
argument_list|,
literal|4
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
