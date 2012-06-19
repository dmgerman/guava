begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
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
name|Runner
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
name|SimpleBenchmark
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
name|Strings
import|;
end_import

begin_comment
comment|/**  * Microbenchmark for {@link Strings#repeat}  *  * @author Mike Cripps  */
end_comment

begin_class
DECL|class|StringsRepeatBenchmark
specifier|public
class|class
name|StringsRepeatBenchmark
extends|extends
name|SimpleBenchmark
block|{
DECL|field|count
annotation|@
name|Param
argument_list|(
block|{
literal|"1"
block|,
literal|"5"
block|,
literal|"25"
block|,
literal|"125"
block|}
argument_list|)
name|int
name|count
decl_stmt|;
DECL|field|length
annotation|@
name|Param
argument_list|(
block|{
literal|"1"
block|,
literal|"10"
block|}
argument_list|)
name|int
name|length
decl_stmt|;
DECL|field|originalString
specifier|private
name|String
name|originalString
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
block|{
name|originalString
operator|=
name|Strings
operator|.
name|repeat
argument_list|(
literal|"x"
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
DECL|method|timeOldRepeat (int reps)
specifier|public
name|void
name|timeOldRepeat
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
name|i
operator|++
control|)
block|{
name|String
name|x
init|=
name|oldRepeat
argument_list|(
name|originalString
argument_list|,
name|count
argument_list|)
decl_stmt|;
if|if
condition|(
name|x
operator|.
name|length
argument_list|()
operator|!=
operator|(
name|originalString
operator|.
name|length
argument_list|()
operator|*
name|count
operator|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Wrong length: "
operator|+
name|x
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|timeMikeRepeat (int reps)
specifier|public
name|void
name|timeMikeRepeat
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
name|i
operator|++
control|)
block|{
name|String
name|x
init|=
name|mikeRepeat
argument_list|(
name|originalString
argument_list|,
name|count
argument_list|)
decl_stmt|;
if|if
condition|(
name|x
operator|.
name|length
argument_list|()
operator|!=
operator|(
name|originalString
operator|.
name|length
argument_list|()
operator|*
name|count
operator|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Wrong length: "
operator|+
name|x
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|timeMartinRepeat (int reps)
specifier|public
name|void
name|timeMartinRepeat
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
name|i
operator|++
control|)
block|{
name|String
name|x
init|=
name|martinRepeat
argument_list|(
name|originalString
argument_list|,
name|count
argument_list|)
decl_stmt|;
if|if
condition|(
name|x
operator|.
name|length
argument_list|()
operator|!=
operator|(
name|originalString
operator|.
name|length
argument_list|()
operator|*
name|count
operator|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Wrong length: "
operator|+
name|x
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|mikeRepeat (String string, int count)
specifier|private
specifier|static
specifier|final
name|String
name|mikeRepeat
parameter_list|(
name|String
name|string
parameter_list|,
name|int
name|count
parameter_list|)
block|{
specifier|final
name|int
name|len
init|=
name|string
operator|.
name|length
argument_list|()
decl_stmt|;
name|char
index|[]
name|strCopy
init|=
operator|new
name|char
index|[
name|len
operator|*
name|Integer
operator|.
name|highestOneBit
argument_list|(
name|count
argument_list|)
index|]
decl_stmt|;
name|string
operator|.
name|getChars
argument_list|(
literal|0
argument_list|,
name|len
argument_list|,
name|strCopy
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|char
index|[]
name|array
init|=
operator|new
name|char
index|[
name|len
operator|*
name|count
index|]
decl_stmt|;
name|int
name|strCopyLen
init|=
name|len
decl_stmt|;
name|int
name|pos
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|count
operator|!=
literal|0
condition|)
block|{
if|if
condition|(
operator|(
name|count
operator|&
literal|1
operator|)
operator|!=
literal|0
condition|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|strCopy
argument_list|,
literal|0
argument_list|,
name|array
argument_list|,
name|pos
argument_list|,
name|strCopyLen
argument_list|)
expr_stmt|;
name|pos
operator|+=
name|strCopyLen
expr_stmt|;
block|}
name|count
operator|>>=
literal|1
expr_stmt|;
if|if
condition|(
name|count
operator|!=
literal|0
condition|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|strCopy
argument_list|,
literal|0
argument_list|,
name|strCopy
argument_list|,
name|strCopyLen
argument_list|,
name|strCopyLen
argument_list|)
expr_stmt|;
name|strCopyLen
operator|<<=
literal|1
expr_stmt|;
block|}
block|}
return|return
operator|new
name|String
argument_list|(
name|array
argument_list|)
return|;
block|}
DECL|method|oldRepeat (String string, int count)
specifier|private
specifier|static
specifier|final
name|String
name|oldRepeat
parameter_list|(
name|String
name|string
parameter_list|,
name|int
name|count
parameter_list|)
block|{
comment|// If this multiplication overflows, a NegativeArraySizeException or
comment|// OutOfMemoryError is not far behind
specifier|final
name|int
name|len
init|=
name|string
operator|.
name|length
argument_list|()
decl_stmt|;
specifier|final
name|int
name|size
init|=
name|len
operator|*
name|count
decl_stmt|;
name|char
index|[]
name|array
init|=
operator|new
name|char
index|[
name|size
index|]
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
name|size
condition|;
name|i
operator|+=
name|len
control|)
block|{
name|string
operator|.
name|getChars
argument_list|(
literal|0
argument_list|,
name|len
argument_list|,
name|array
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|String
argument_list|(
name|array
argument_list|)
return|;
block|}
DECL|method|martinRepeat (String string, int count)
specifier|private
specifier|static
specifier|final
name|String
name|martinRepeat
parameter_list|(
name|String
name|string
parameter_list|,
name|int
name|count
parameter_list|)
block|{
specifier|final
name|int
name|len
init|=
name|string
operator|.
name|length
argument_list|()
decl_stmt|;
specifier|final
name|int
name|size
init|=
name|len
operator|*
name|count
decl_stmt|;
specifier|final
name|char
index|[]
name|array
init|=
operator|new
name|char
index|[
name|size
index|]
decl_stmt|;
name|string
operator|.
name|getChars
argument_list|(
literal|0
argument_list|,
name|len
argument_list|,
name|array
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|int
name|n
decl_stmt|;
for|for
control|(
name|n
operator|=
name|len
init|;
name|n
operator|<
name|size
operator|-
name|n
condition|;
name|n
operator|<<=
literal|1
control|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|array
argument_list|,
literal|0
argument_list|,
name|array
argument_list|,
name|n
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|arraycopy
argument_list|(
name|array
argument_list|,
literal|0
argument_list|,
name|array
argument_list|,
name|n
argument_list|,
name|size
operator|-
name|n
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
argument_list|(
name|array
argument_list|)
return|;
block|}
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|Runner
operator|.
name|main
argument_list|(
name|StringsRepeatBenchmark
operator|.
name|class
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

