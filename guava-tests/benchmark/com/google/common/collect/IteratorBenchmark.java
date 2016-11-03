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
name|LinkedList
import|;
end_import

begin_comment
comment|/**  * Tests the speed of iteration of different iteration methods for collections.  *  * @author David Richter  */
end_comment

begin_class
DECL|class|IteratorBenchmark
specifier|public
class|class
name|IteratorBenchmark
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
comment|// use concrete classes to remove any possible polymorphic overhead?
DECL|field|array
name|Object
index|[]
name|array
decl_stmt|;
DECL|field|arrayList
name|ArrayList
argument_list|<
name|Object
argument_list|>
name|arrayList
decl_stmt|;
DECL|field|linkedList
name|LinkedList
argument_list|<
name|Object
argument_list|>
name|linkedList
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|BeforeExperiment
name|void
name|setUp
parameter_list|()
block|{
name|array
operator|=
operator|new
name|Object
index|[
name|size
index|]
expr_stmt|;
name|arrayList
operator|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|size
argument_list|)
expr_stmt|;
name|linkedList
operator|=
name|Lists
operator|.
name|newLinkedList
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
name|size
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|value
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|array
index|[
name|i
index|]
operator|=
name|value
expr_stmt|;
name|arrayList
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|linkedList
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|arrayIndexed (int reps)
annotation|@
name|Benchmark
name|int
name|arrayIndexed
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
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|size
condition|;
name|index
operator|++
control|)
block|{
name|sum
operator|+=
name|array
index|[
name|index
index|]
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
DECL|method|arrayIndexedLength (int reps)
annotation|@
name|Benchmark
name|int
name|arrayIndexedLength
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
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|array
operator|.
name|length
condition|;
name|index
operator|++
control|)
block|{
name|sum
operator|+=
name|array
index|[
name|index
index|]
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
DECL|method|arrayFor (int reps)
annotation|@
name|Benchmark
name|int
name|arrayFor
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
name|array
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
DECL|method|arrayListIndexed (int reps)
annotation|@
name|Benchmark
name|int
name|arrayListIndexed
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
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|size
condition|;
name|index
operator|++
control|)
block|{
name|sum
operator|+=
name|arrayList
operator|.
name|get
argument_list|(
name|index
argument_list|)
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
DECL|method|arrayListIndexedLength (int reps)
annotation|@
name|Benchmark
name|int
name|arrayListIndexedLength
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
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|arrayList
operator|.
name|size
argument_list|()
condition|;
name|index
operator|++
control|)
block|{
name|sum
operator|+=
name|arrayList
operator|.
name|get
argument_list|(
name|index
argument_list|)
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
DECL|method|arrayListFor (int reps)
annotation|@
name|Benchmark
name|int
name|arrayListFor
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
name|arrayList
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
annotation|@
name|Benchmark
DECL|method|arrayListForWithHolder (int reps)
name|int
name|arrayListForWithHolder
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
index|[]
name|sumHolder
init|=
block|{
literal|0
block|}
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
name|arrayList
control|)
block|{
name|sumHolder
index|[
literal|0
index|]
operator|+=
name|value
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|sumHolder
index|[
literal|0
index|]
return|;
block|}
annotation|@
name|Benchmark
DECL|method|arrayListForEachWithHolder (int reps)
name|int
name|arrayListForEachWithHolder
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
index|[]
name|sumHolder
init|=
block|{
literal|0
block|}
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
name|arrayList
operator|.
name|forEach
argument_list|(
name|value
lambda|->
name|sumHolder
index|[
literal|0
index|]
operator|+=
name|value
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sumHolder
index|[
literal|0
index|]
return|;
block|}
DECL|method|arrayListToArrayFor (int reps)
annotation|@
name|Benchmark
name|int
name|arrayListToArrayFor
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
name|arrayList
operator|.
name|toArray
argument_list|()
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
DECL|method|linkedListFor (int reps)
annotation|@
name|Benchmark
name|int
name|linkedListFor
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
name|linkedList
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
annotation|@
name|Benchmark
DECL|method|linkedListForEach (int reps)
name|int
name|linkedListForEach
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
index|[]
name|sumHolder
init|=
block|{
literal|0
block|}
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
name|linkedList
operator|.
name|forEach
argument_list|(
name|value
lambda|->
name|sumHolder
index|[
literal|0
index|]
operator|+=
name|value
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sumHolder
index|[
literal|0
index|]
return|;
block|}
DECL|method|linkedListToArrayFor (int reps)
annotation|@
name|Benchmark
name|int
name|linkedListToArrayFor
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
name|linkedList
operator|.
name|toArray
argument_list|()
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

