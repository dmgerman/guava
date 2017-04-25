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
name|java
operator|.
name|security
operator|.
name|MessageDigest
import|;
end_import

begin_comment
comment|/**  * Benchmarks for comparing instance creation of {@link MessageDigest}s.  *  * @author Kurt Alfred Kluever  */
end_comment

begin_class
DECL|class|MessageDigestCreationBenchmark
specifier|public
class|class
name|MessageDigestCreationBenchmark
block|{
annotation|@
name|Param
argument_list|(
block|{
literal|"MD5"
block|,
literal|"SHA-1"
block|,
literal|"SHA-256"
block|,
literal|"SHA-384"
block|,
literal|"SHA-512"
block|}
argument_list|)
DECL|field|algorithm
specifier|private
name|String
name|algorithm
decl_stmt|;
DECL|field|md
specifier|private
name|MessageDigest
name|md
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|BeforeExperiment
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|md
operator|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
name|algorithm
argument_list|)
expr_stmt|;
block|}
DECL|method|getInstance (int reps)
annotation|@
name|Benchmark
name|int
name|getInstance
parameter_list|(
name|int
name|reps
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|retValue
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
name|retValue
operator|^=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
name|algorithm
argument_list|)
operator|.
name|getDigestLength
argument_list|()
expr_stmt|;
block|}
return|return
name|retValue
return|;
block|}
DECL|method|clone (int reps)
annotation|@
name|Benchmark
name|int
name|clone
parameter_list|(
name|int
name|reps
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|retValue
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
name|retValue
operator|^=
operator|(
operator|(
name|MessageDigest
operator|)
name|md
operator|.
name|clone
argument_list|()
operator|)
operator|.
name|getDigestLength
argument_list|()
expr_stmt|;
block|}
return|return
name|retValue
return|;
block|}
block|}
end_class

end_unit
