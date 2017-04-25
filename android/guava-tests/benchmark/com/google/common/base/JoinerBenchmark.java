begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Iterator
import|;
end_import

begin_comment
comment|/**  * Benchmarks {@link Joiner} against some common implementations of delimiter-based  * string joining.  *  * @author Adomas Paltanavicius  */
end_comment

begin_class
DECL|class|JoinerBenchmark
specifier|public
class|class
name|JoinerBenchmark
block|{
DECL|field|DELIMITER_STRING
specifier|private
specifier|static
specifier|final
name|String
name|DELIMITER_STRING
init|=
literal|","
decl_stmt|;
DECL|field|DELIMITER_CHARACTER
specifier|private
specifier|static
specifier|final
name|char
name|DELIMITER_CHARACTER
init|=
literal|','
decl_stmt|;
DECL|field|JOINER_ON_STRING
specifier|private
specifier|static
specifier|final
name|Joiner
name|JOINER_ON_STRING
init|=
name|Joiner
operator|.
name|on
argument_list|(
name|DELIMITER_STRING
argument_list|)
decl_stmt|;
DECL|field|JOINER_ON_CHARACTER
specifier|private
specifier|static
specifier|final
name|Joiner
name|JOINER_ON_CHARACTER
init|=
name|Joiner
operator|.
name|on
argument_list|(
name|DELIMITER_CHARACTER
argument_list|)
decl_stmt|;
DECL|field|count
annotation|@
name|Param
argument_list|(
block|{
literal|"3"
block|,
literal|"30"
block|,
literal|"300"
block|}
argument_list|)
name|int
name|count
decl_stmt|;
DECL|field|componentLength
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
literal|"32"
block|,
literal|"100"
block|}
argument_list|)
name|int
name|componentLength
decl_stmt|;
DECL|field|components
specifier|private
name|Iterable
argument_list|<
name|String
argument_list|>
name|components
decl_stmt|;
annotation|@
name|BeforeExperiment
DECL|method|setUp ()
name|void
name|setUp
parameter_list|()
block|{
name|String
name|component
init|=
name|Strings
operator|.
name|repeat
argument_list|(
literal|"a"
argument_list|,
name|componentLength
argument_list|)
decl_stmt|;
name|String
index|[]
name|raw
init|=
operator|new
name|String
index|[
name|count
index|]
decl_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|raw
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|components
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|raw
argument_list|)
expr_stmt|;
block|}
comment|/**    * {@link Joiner} with a string delimiter.    */
DECL|method|joinerWithStringDelimiter (int reps)
annotation|@
name|Benchmark
name|int
name|joinerWithStringDelimiter
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|dummy
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
name|dummy
operator|^=
name|JOINER_ON_STRING
operator|.
name|join
argument_list|(
name|components
argument_list|)
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
comment|/**    * {@link Joiner} with a character delimiter.    */
DECL|method|joinerWithCharacterDelimiter (int reps)
annotation|@
name|Benchmark
name|int
name|joinerWithCharacterDelimiter
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|dummy
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
name|dummy
operator|^=
name|JOINER_ON_CHARACTER
operator|.
name|join
argument_list|(
name|components
argument_list|)
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
comment|/**    * Mimics what the {@link Joiner} class does internally when no extra options like    * ignoring {@code null} values are used.    */
DECL|method|joinerInlined (int reps)
annotation|@
name|Benchmark
name|int
name|joinerInlined
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|dummy
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
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
init|=
name|components
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|iterator
operator|.
name|next
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|DELIMITER_STRING
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|iterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|dummy
operator|^=
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
comment|/**    * Only appends delimiter if the accumulated string is non-empty.    * Note: this isn't a candidate implementation for Joiner since it fails on leading    * empty components.    */
DECL|method|stringBuilderIsEmpty (int reps)
annotation|@
name|Benchmark
name|int
name|stringBuilderIsEmpty
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|dummy
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
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|comp
range|:
name|components
control|)
block|{
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|DELIMITER_STRING
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|comp
argument_list|)
expr_stmt|;
block|}
name|dummy
operator|^=
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
comment|/**    * Similar to the above, but keeps a boolean flag rather than checking for the string    * accumulated so far being empty. As a result, it does not have the above-mentioned bug.    */
DECL|method|booleanIfFirst (int reps)
annotation|@
name|Benchmark
name|int
name|booleanIfFirst
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|dummy
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
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|boolean
name|append
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|comp
range|:
name|components
control|)
block|{
if|if
condition|(
name|append
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|DELIMITER_STRING
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|comp
argument_list|)
expr_stmt|;
name|append
operator|=
literal|true
expr_stmt|;
block|}
name|dummy
operator|^=
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
comment|/**    * Starts with an empty delimiter and changes to the desired value at the end of the    * iteration.    */
DECL|method|assignDelimiter (int reps)
annotation|@
name|Benchmark
name|int
name|assignDelimiter
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|dummy
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
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
name|delim
init|=
literal|""
decl_stmt|;
for|for
control|(
name|String
name|comp
range|:
name|components
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|delim
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|comp
argument_list|)
expr_stmt|;
name|delim
operator|=
name|DELIMITER_STRING
expr_stmt|;
block|}
name|dummy
operator|^=
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
comment|/**    * Always append the delimiter after the component, and in the very end shortens the buffer    * to get rid of the extra trailing delimiter.    */
DECL|method|alwaysAppendThenBackUp (int reps)
annotation|@
name|Benchmark
name|int
name|alwaysAppendThenBackUp
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|dummy
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
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|comp
range|:
name|components
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|comp
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|DELIMITER_STRING
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|setLength
argument_list|(
name|sb
operator|.
name|length
argument_list|()
operator|-
name|DELIMITER_STRING
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|dummy
operator|^=
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
block|}
end_class

end_unit
