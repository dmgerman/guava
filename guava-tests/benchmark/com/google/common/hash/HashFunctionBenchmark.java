begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.hash
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
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
name|hash
operator|.
name|HashFunction
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
comment|/**  * Benchmarks for comparing the various {@link HashFunction functions} that we provide.  *  *<p>Parameters for the benchmark are:  *<ul>  *<li>size: The length of the byte array to hash.  *<li>hashFunctionEnum: The {@link HashFunction} to use for hashing.  *</ul>  *  * @author Kurt Alfred Kluever  */
end_comment

begin_class
DECL|class|HashFunctionBenchmark
specifier|public
class|class
name|HashFunctionBenchmark
block|{
comment|// Use a statically configured random instance for all of the benchmarks
DECL|field|random
specifier|private
specifier|static
specifier|final
name|Random
name|random
init|=
operator|new
name|Random
argument_list|(
literal|42
argument_list|)
decl_stmt|;
annotation|@
name|Param
argument_list|(
block|{
literal|"10"
block|,
literal|"1000"
block|,
literal|"100000"
block|,
literal|"1000000"
block|}
argument_list|)
DECL|field|size
specifier|private
name|int
name|size
decl_stmt|;
DECL|field|hashFunctionEnum
annotation|@
name|Param
name|HashFunctionEnum
name|hashFunctionEnum
decl_stmt|;
DECL|field|testBytes
specifier|private
name|byte
index|[]
name|testBytes
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|BeforeExperiment
name|void
name|setUp
parameter_list|()
block|{
name|testBytes
operator|=
operator|new
name|byte
index|[
name|size
index|]
expr_stmt|;
name|random
operator|.
name|nextBytes
argument_list|(
name|testBytes
argument_list|)
expr_stmt|;
block|}
DECL|method|hashFunction (int reps)
annotation|@
name|Benchmark
name|int
name|hashFunction
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|HashFunction
name|hashFunction
init|=
name|hashFunctionEnum
operator|.
name|getHashFunction
argument_list|()
decl_stmt|;
name|int
name|result
init|=
literal|37
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
name|result
operator|^=
name|hashFunction
operator|.
name|hashBytes
argument_list|(
name|testBytes
argument_list|)
operator|.
name|asBytes
argument_list|()
index|[
literal|0
index|]
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

