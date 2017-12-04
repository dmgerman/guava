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
name|collect
operator|.
name|BenchmarkHelpers
operator|.
name|SetImpl
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
name|collect
operator|.
name|CollectionBenchmarkSampleData
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * A microbenchmark that tests the performance of contains() on various Set implementations.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
DECL|class|SetContainsBenchmark
specifier|public
class|class
name|SetContainsBenchmark
block|{
comment|// Start at 4.88 then multiply by 2*2^phi<evil cackle> - The goal is be uniform
comment|// yet visit a variety of "values-relative-to-the-next-power-of-2"
annotation|@
name|Param
argument_list|(
block|{
literal|"5"
block|,
literal|"30"
block|,
literal|"180"
block|,
literal|"1100"
block|,
literal|"6900"
block|,
literal|"43000"
block|,
literal|"260000"
block|}
argument_list|)
comment|// "1600000", "9800000"
DECL|field|size
specifier|private
name|int
name|size
decl_stmt|;
comment|// TODO(kevinb): look at exact (==) hits vs. equals() hits?
annotation|@
name|Param
argument_list|(
block|{
literal|"0.2"
block|,
literal|"0.8"
block|}
argument_list|)
DECL|field|hitRate
specifier|private
name|double
name|hitRate
decl_stmt|;
annotation|@
name|Param
argument_list|(
literal|"true"
argument_list|)
DECL|field|isUserTypeFast
specifier|private
name|boolean
name|isUserTypeFast
decl_stmt|;
comment|// "" means no fixed seed
annotation|@
name|Param
argument_list|(
literal|""
argument_list|)
DECL|field|random
specifier|private
name|SpecialRandom
name|random
decl_stmt|;
annotation|@
name|Param
argument_list|(
block|{
literal|"HashSetImpl"
block|,
literal|"ImmutableSetImpl"
block|}
argument_list|)
DECL|field|impl
specifier|private
name|SetImpl
name|impl
decl_stmt|;
comment|// the following must be set during setUp
DECL|field|queries
specifier|private
name|Element
index|[]
name|queries
decl_stmt|;
DECL|field|setToTest
specifier|private
name|Set
argument_list|<
name|Element
argument_list|>
name|setToTest
decl_stmt|;
annotation|@
name|BeforeExperiment
DECL|method|setUp ()
name|void
name|setUp
parameter_list|()
block|{
name|CollectionBenchmarkSampleData
name|sampleData
init|=
operator|new
name|CollectionBenchmarkSampleData
argument_list|(
name|isUserTypeFast
argument_list|,
name|random
argument_list|,
name|hitRate
argument_list|,
name|size
argument_list|)
decl_stmt|;
name|this
operator|.
name|setToTest
operator|=
operator|(
name|Set
argument_list|<
name|Element
argument_list|>
operator|)
name|impl
operator|.
name|create
argument_list|(
name|sampleData
operator|.
name|getValuesInSet
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|queries
operator|=
name|sampleData
operator|.
name|getQueries
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|contains (int reps)
name|boolean
name|contains
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
comment|// Paranoia: acting on hearsay that accessing fields might be slow
comment|// Should write a benchmark to test that!
name|Set
argument_list|<
name|Element
argument_list|>
name|set
init|=
name|setToTest
decl_stmt|;
name|Element
index|[]
name|queries
init|=
name|this
operator|.
name|queries
decl_stmt|;
name|int
name|mask
init|=
name|queries
operator|.
name|length
operator|-
literal|1
decl_stmt|;
name|boolean
name|dummy
init|=
literal|false
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
name|dummy
operator|^=
name|set
operator|.
name|contains
argument_list|(
name|queries
index|[
name|i
operator|&
name|mask
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
block|}
end_class

end_unit

