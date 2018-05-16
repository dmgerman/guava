begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|BitSet
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
comment|/** Benchmark for the {@link CharMatcher#whitespace} implementation. */
end_comment

begin_class
DECL|class|WhitespaceMatcherBenchmark
specifier|public
class|class
name|WhitespaceMatcherBenchmark
block|{
DECL|field|STRING_LENGTH
specifier|private
specifier|static
specifier|final
name|int
name|STRING_LENGTH
init|=
literal|10000
decl_stmt|;
DECL|field|OLD_WHITESPACE_TABLE
specifier|private
specifier|static
specifier|final
name|String
name|OLD_WHITESPACE_TABLE
init|=
literal|"\u0001\u0000\u00a0\u0000\u0000\u0000\u0000\u0000"
operator|+
literal|"\u0000\u0009\n\u000b\u000c\r\u0000\u0000\u2028\u2029\u0000\u0000\u0000\u0000\u0000"
operator|+
literal|"\u202f\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0020\u0000\u0000\u0000\u0000"
operator|+
literal|"\u0000\u0000\u0000\u0000\u0000\u0000\u3000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
operator|+
literal|"\u0000\u0000\u0000\u0085\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009"
operator|+
literal|"\u200a\u0000\u0000\u0000\u0000\u0000\u205f\u1680\u0000\u0000\u180e\u0000\u0000\u0000"
decl_stmt|;
DECL|field|OLD_WHITESPACE
specifier|public
specifier|static
specifier|final
name|CharMatcher
name|OLD_WHITESPACE
init|=
operator|new
name|CharMatcher
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
name|char
name|c
parameter_list|)
block|{
return|return
name|OLD_WHITESPACE_TABLE
operator|.
name|charAt
argument_list|(
name|c
operator|%
literal|79
argument_list|)
operator|==
name|c
return|;
block|}
block|}
decl_stmt|;
DECL|field|useNew
annotation|@
name|Param
specifier|private
name|boolean
name|useNew
decl_stmt|;
annotation|@
name|Param
argument_list|(
block|{
literal|"20"
block|,
literal|"50"
block|,
literal|"80"
block|}
argument_list|)
DECL|field|percentMatching
specifier|private
name|int
name|percentMatching
decl_stmt|;
DECL|field|teststring
specifier|private
name|String
name|teststring
decl_stmt|;
DECL|field|matcher
specifier|private
name|CharMatcher
name|matcher
decl_stmt|;
annotation|@
name|BeforeExperiment
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
block|{
name|BitSet
name|bitSet
init|=
operator|new
name|BitSet
argument_list|()
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
name|OLD_WHITESPACE_TABLE
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|bitSet
operator|.
name|set
argument_list|(
name|OLD_WHITESPACE_TABLE
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|bitSet
operator|.
name|clear
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|bitSet
operator|.
name|clear
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|matcher
operator|=
name|useNew
condition|?
name|CharMatcher
operator|.
name|whitespace
argument_list|()
else|:
name|OLD_WHITESPACE
expr_stmt|;
name|teststring
operator|=
name|newTestString
argument_list|(
operator|new
name|Random
argument_list|(
literal|1
argument_list|)
argument_list|,
name|bitSet
argument_list|,
name|percentMatching
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|countIn (int reps)
specifier|public
name|int
name|countIn
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|result
init|=
literal|0
decl_stmt|;
name|CharMatcher
name|matcher
init|=
name|this
operator|.
name|matcher
decl_stmt|;
name|String
name|teststring
init|=
name|this
operator|.
name|teststring
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
operator|+=
name|matcher
operator|.
name|countIn
argument_list|(
name|teststring
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Benchmark
DECL|method|collapseFrom (int reps)
specifier|public
name|int
name|collapseFrom
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|result
init|=
literal|0
decl_stmt|;
name|CharMatcher
name|matcher
init|=
name|this
operator|.
name|matcher
decl_stmt|;
name|String
name|teststring
init|=
name|this
operator|.
name|teststring
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
operator|+=
name|System
operator|.
name|identityHashCode
argument_list|(
name|matcher
operator|.
name|collapseFrom
argument_list|(
name|teststring
argument_list|,
literal|' '
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|allMatchingChars (BitSet bitSet)
specifier|private
specifier|static
name|String
name|allMatchingChars
parameter_list|(
name|BitSet
name|bitSet
parameter_list|)
block|{
specifier|final
name|char
index|[]
name|result
init|=
operator|new
name|char
index|[
name|bitSet
operator|.
name|cardinality
argument_list|()
index|]
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|,
name|c
init|=
name|bitSet
operator|.
name|nextSetBit
argument_list|(
literal|0
argument_list|)
init|;
name|j
operator|<
name|result
operator|.
name|length
condition|;
operator|++
name|j
control|)
block|{
name|result
index|[
name|j
index|]
operator|=
operator|(
name|char
operator|)
name|c
expr_stmt|;
name|c
operator|=
name|bitSet
operator|.
name|nextSetBit
argument_list|(
name|c
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|String
argument_list|(
name|result
argument_list|)
return|;
block|}
DECL|method|newTestString (Random random, BitSet bitSet, int percentMatching)
specifier|private
specifier|static
name|String
name|newTestString
parameter_list|(
name|Random
name|random
parameter_list|,
name|BitSet
name|bitSet
parameter_list|,
name|int
name|percentMatching
parameter_list|)
block|{
specifier|final
name|String
name|allMatchingChars
init|=
name|allMatchingChars
argument_list|(
name|bitSet
argument_list|)
decl_stmt|;
specifier|final
name|char
index|[]
name|result
init|=
operator|new
name|char
index|[
name|STRING_LENGTH
index|]
decl_stmt|;
comment|// Fill with matching chars.
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|result
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|result
index|[
name|i
index|]
operator|=
name|allMatchingChars
operator|.
name|charAt
argument_list|(
name|random
operator|.
name|nextInt
argument_list|(
name|allMatchingChars
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Replace some of chars by non-matching.
name|int
name|remaining
init|=
call|(
name|int
call|)
argument_list|(
operator|(
literal|100
operator|-
name|percentMatching
operator|)
operator|*
name|result
operator|.
name|length
operator|/
literal|100.0
operator|+
literal|0.5
argument_list|)
decl_stmt|;
while|while
condition|(
name|remaining
operator|>
literal|0
condition|)
block|{
specifier|final
name|char
name|c
init|=
operator|(
name|char
operator|)
name|random
operator|.
name|nextInt
argument_list|()
decl_stmt|;
if|if
condition|(
name|bitSet
operator|.
name|get
argument_list|(
name|c
argument_list|)
condition|)
block|{
specifier|final
name|int
name|pos
init|=
name|random
operator|.
name|nextInt
argument_list|(
name|result
operator|.
name|length
argument_list|)
decl_stmt|;
if|if
condition|(
name|bitSet
operator|.
name|get
argument_list|(
name|result
index|[
name|pos
index|]
argument_list|)
condition|)
block|{
name|result
index|[
name|pos
index|]
operator|=
name|c
expr_stmt|;
name|remaining
operator|--
expr_stmt|;
block|}
block|}
block|}
return|return
operator|new
name|String
argument_list|(
name|result
argument_list|)
return|;
block|}
block|}
end_class

end_unit

