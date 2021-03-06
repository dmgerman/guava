begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2017 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.io
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
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
name|caliper
operator|.
name|api
operator|.
name|VmOptions
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|CharBuffer
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
comment|/**  * Benchmarks for {@link CharStreams#copy}.  *  *<p>{@link CharStreams#copy} has type specific optimizations for various common Appendable and  * Reader implementations, this compares the performance of the different options.  */
end_comment

begin_comment
comment|// These benchmarks allocate a lot of data so use a large heap
end_comment

begin_class
annotation|@
name|VmOptions
argument_list|(
block|{
literal|"-Xms12g"
block|,
literal|"-Xmx12g"
block|,
literal|"-d64"
block|}
argument_list|)
DECL|class|CharStreamsCopyBenchmark
specifier|public
class|class
name|CharStreamsCopyBenchmark
block|{
DECL|enum|CopyStrategy
enum|enum
name|CopyStrategy
block|{
DECL|enumConstant|OLD
name|OLD
block|{
annotation|@
name|Override
name|long
name|copy
parameter_list|(
name|Readable
name|from
parameter_list|,
name|Appendable
name|to
parameter_list|)
throws|throws
name|IOException
block|{
name|CharBuffer
name|buf
init|=
name|CharStreams
operator|.
name|createBuffer
argument_list|()
decl_stmt|;
name|long
name|total
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|from
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|buf
operator|.
name|flip
argument_list|()
expr_stmt|;
name|to
operator|.
name|append
argument_list|(
name|buf
argument_list|)
expr_stmt|;
name|total
operator|+=
name|buf
operator|.
name|remaining
argument_list|()
expr_stmt|;
name|buf
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
return|return
name|total
return|;
block|}
block|}
block|,
DECL|enumConstant|NEW
name|NEW
block|{
annotation|@
name|Override
name|long
name|copy
parameter_list|(
name|Readable
name|from
parameter_list|,
name|Appendable
name|to
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|CharStreams
operator|.
name|copy
argument_list|(
name|from
argument_list|,
name|to
argument_list|)
return|;
block|}
block|}
block|;
DECL|method|copy (Readable from, Appendable to)
specifier|abstract
name|long
name|copy
parameter_list|(
name|Readable
name|from
parameter_list|,
name|Appendable
name|to
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
DECL|enum|TargetSupplier
enum|enum
name|TargetSupplier
block|{
DECL|enumConstant|STRING_WRITER
name|STRING_WRITER
block|{
annotation|@
name|Override
name|Appendable
name|get
parameter_list|(
name|int
name|sz
parameter_list|)
block|{
return|return
operator|new
name|StringWriter
argument_list|(
name|sz
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|STRING_BUILDER
name|STRING_BUILDER
block|{
annotation|@
name|Override
name|Appendable
name|get
parameter_list|(
name|int
name|sz
parameter_list|)
block|{
return|return
operator|new
name|StringBuilder
argument_list|(
name|sz
argument_list|)
return|;
block|}
block|}
block|;
DECL|method|get (int sz)
specifier|abstract
name|Appendable
name|get
parameter_list|(
name|int
name|sz
parameter_list|)
function_decl|;
block|}
DECL|field|strategy
annotation|@
name|Param
name|CopyStrategy
name|strategy
decl_stmt|;
DECL|field|target
annotation|@
name|Param
name|TargetSupplier
name|target
decl_stmt|;
annotation|@
name|Param
argument_list|(
block|{
literal|"10"
block|,
literal|"1024"
block|,
literal|"1048576"
block|}
argument_list|)
DECL|field|size
name|int
name|size
decl_stmt|;
DECL|field|data
name|String
name|data
decl_stmt|;
annotation|@
name|BeforeExperiment
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
comment|// precalculate some random strings of ascii characters.
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Random
name|random
init|=
operator|new
name|Random
argument_list|(
literal|0xdeadbeef
argument_list|)
decl_stmt|;
comment|// for unpredictable but reproducible behavior
name|sb
operator|.
name|ensureCapacity
argument_list|(
name|size
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
name|size
condition|;
name|k
operator|++
control|)
block|{
comment|// [9-127) includes all ascii non-control characters
name|sb
operator|.
name|append
argument_list|(
call|(
name|char
call|)
argument_list|(
name|random
operator|.
name|nextInt
argument_list|(
literal|127
operator|-
literal|9
argument_list|)
operator|+
literal|9
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|data
operator|=
name|sb
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|timeCopy (int reps)
specifier|public
name|long
name|timeCopy
parameter_list|(
name|int
name|reps
parameter_list|)
throws|throws
name|IOException
block|{
name|long
name|r
init|=
literal|0
decl_stmt|;
specifier|final
name|String
name|localData
init|=
name|data
decl_stmt|;
specifier|final
name|TargetSupplier
name|localTarget
init|=
name|target
decl_stmt|;
specifier|final
name|CopyStrategy
name|localStrategy
init|=
name|strategy
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
name|Appendable
name|appendable
init|=
name|localTarget
operator|.
name|get
argument_list|(
name|localData
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|r
operator|+=
name|localStrategy
operator|.
name|copy
argument_list|(
operator|new
name|StringReader
argument_list|(
name|localData
argument_list|)
argument_list|,
name|appendable
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
block|}
end_class

end_unit

