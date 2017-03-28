begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * This is meant to be used with {@code --measureMemory} to measure the memory  * usage of various {@code Set} implementations.  *  * @author Christopher Swenson  */
end_comment

begin_class
DECL|class|SetCreationBenchmark
specifier|public
class|class
name|SetCreationBenchmark
block|{
annotation|@
name|Param
argument_list|(
block|{
literal|"3"
block|,
literal|"6"
block|,
literal|"11"
block|,
literal|"23"
block|,
literal|"45"
block|,
literal|"91"
block|,
literal|"181"
block|,
literal|"362"
block|,
literal|"724"
block|,
literal|"1448"
block|,
literal|"2896"
block|,
literal|"5793"
block|,
literal|"11585"
block|,
literal|"23170"
block|,
literal|"46341"
block|,
literal|"92682"
block|,
literal|"185364"
block|,
literal|"370728"
block|,
literal|"741455"
block|,
literal|"1482910"
block|,
literal|"2965821"
block|,
literal|"5931642"
block|}
argument_list|)
DECL|field|size
specifier|private
name|int
name|size
decl_stmt|;
comment|// "" means no fixed seed
annotation|@
name|Param
argument_list|(
literal|"1234"
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
literal|"ImmutableSetImpl"
block|,
literal|"HashSetImpl"
block|}
argument_list|)
DECL|field|impl
specifier|private
name|SetImpl
name|impl
decl_stmt|;
comment|// the following must be set during setUp
DECL|field|sampleData
specifier|private
name|CollectionBenchmarkSampleData
name|sampleData
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|BeforeExperiment
name|void
name|setUp
parameter_list|()
block|{
name|sampleData
operator|=
operator|new
name|CollectionBenchmarkSampleData
argument_list|(
literal|true
argument_list|,
name|random
argument_list|,
literal|0.8
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
DECL|method|creation (int reps)
annotation|@
name|Benchmark
name|int
name|creation
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|x
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
name|x
operator|^=
name|System
operator|.
name|identityHashCode
argument_list|(
name|impl
operator|.
name|create
argument_list|(
name|sampleData
operator|.
name|getValuesInSet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|x
return|;
block|}
block|}
end_class

end_unit

