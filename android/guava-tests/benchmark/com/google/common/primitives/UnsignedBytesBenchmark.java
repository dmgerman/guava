begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.primitives
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
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
comment|/**  * Microbenchmark for {@link UnsignedBytes}.  *  * @author Hiroshi Yamauchi  */
end_comment

begin_class
DECL|class|UnsignedBytesBenchmark
specifier|public
class|class
name|UnsignedBytesBenchmark
block|{
DECL|field|ba1
specifier|private
name|byte
index|[]
name|ba1
decl_stmt|;
DECL|field|ba2
specifier|private
name|byte
index|[]
name|ba2
decl_stmt|;
DECL|field|ba3
specifier|private
name|byte
index|[]
name|ba3
decl_stmt|;
DECL|field|ba4
specifier|private
name|byte
index|[]
name|ba4
decl_stmt|;
DECL|field|javaImpl
specifier|private
name|Comparator
argument_list|<
name|byte
index|[]
argument_list|>
name|javaImpl
decl_stmt|;
DECL|field|unsafeImpl
specifier|private
name|Comparator
argument_list|<
name|byte
index|[]
argument_list|>
name|unsafeImpl
decl_stmt|;
comment|// 4, 8, 64, 1K, 1M, 1M (unaligned), 64M, 64M (unaligned)
comment|// @Param({"4", "8", "64", "1024", "1048576", "1048577", "6710884", "6710883"})
annotation|@
name|Param
argument_list|(
block|{
literal|"4"
block|,
literal|"8"
block|,
literal|"64"
block|,
literal|"1024"
block|}
argument_list|)
DECL|field|length
specifier|private
name|int
name|length
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
name|r
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|ba1
operator|=
operator|new
name|byte
index|[
name|length
index|]
expr_stmt|;
name|r
operator|.
name|nextBytes
argument_list|(
name|ba1
argument_list|)
expr_stmt|;
name|ba2
operator|=
name|Arrays
operator|.
name|copyOf
argument_list|(
name|ba1
argument_list|,
name|ba1
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// Differ at the last element
name|ba3
operator|=
name|Arrays
operator|.
name|copyOf
argument_list|(
name|ba1
argument_list|,
name|ba1
operator|.
name|length
argument_list|)
expr_stmt|;
name|ba4
operator|=
name|Arrays
operator|.
name|copyOf
argument_list|(
name|ba1
argument_list|,
name|ba1
operator|.
name|length
argument_list|)
expr_stmt|;
name|ba3
index|[
name|ba1
operator|.
name|length
operator|-
literal|1
index|]
operator|=
operator|(
name|byte
operator|)
literal|43
expr_stmt|;
name|ba4
index|[
name|ba1
operator|.
name|length
operator|-
literal|1
index|]
operator|=
operator|(
name|byte
operator|)
literal|42
expr_stmt|;
name|javaImpl
operator|=
name|UnsignedBytes
operator|.
name|lexicographicalComparatorJavaImpl
argument_list|()
expr_stmt|;
name|unsafeImpl
operator|=
name|UnsignedBytes
operator|.
name|LexicographicalComparatorHolder
operator|.
name|UnsafeComparator
operator|.
name|INSTANCE
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|longEqualJava (int reps)
name|void
name|longEqualJava
parameter_list|(
name|int
name|reps
parameter_list|)
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
name|reps
condition|;
operator|++
name|i
control|)
block|{
if|if
condition|(
name|javaImpl
operator|.
name|compare
argument_list|(
name|ba1
argument_list|,
name|ba2
argument_list|)
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|Error
argument_list|()
throw|;
comment|// deoptimization
block|}
block|}
block|}
annotation|@
name|Benchmark
DECL|method|longEqualUnsafe (int reps)
name|void
name|longEqualUnsafe
parameter_list|(
name|int
name|reps
parameter_list|)
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
name|reps
condition|;
operator|++
name|i
control|)
block|{
if|if
condition|(
name|unsafeImpl
operator|.
name|compare
argument_list|(
name|ba1
argument_list|,
name|ba2
argument_list|)
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|Error
argument_list|()
throw|;
comment|// deoptimization
block|}
block|}
block|}
annotation|@
name|Benchmark
DECL|method|diffLastJava (int reps)
name|void
name|diffLastJava
parameter_list|(
name|int
name|reps
parameter_list|)
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
name|reps
condition|;
operator|++
name|i
control|)
block|{
if|if
condition|(
name|javaImpl
operator|.
name|compare
argument_list|(
name|ba3
argument_list|,
name|ba4
argument_list|)
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|Error
argument_list|()
throw|;
comment|// deoptimization
block|}
block|}
block|}
annotation|@
name|Benchmark
DECL|method|diffLastUnsafe (int reps)
name|void
name|diffLastUnsafe
parameter_list|(
name|int
name|reps
parameter_list|)
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
name|reps
condition|;
operator|++
name|i
control|)
block|{
if|if
condition|(
name|unsafeImpl
operator|.
name|compare
argument_list|(
name|ba3
argument_list|,
name|ba4
argument_list|)
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|Error
argument_list|()
throw|;
comment|// deoptimization
block|}
block|}
block|}
comment|/*   try {     UnsignedBytesBenchmark bench = new UnsignedBytesBenchmark();     bench.length = 1024;     bench.setUp();     bench.timeUnsafe(100000);   } catch (Exception e) {   }*/
block|}
end_class

end_unit

