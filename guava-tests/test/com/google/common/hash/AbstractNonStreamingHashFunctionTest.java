begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|HashTestUtils
operator|.
name|RandomHasherAction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|List
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

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Tests for AbstractNonStreamingHashFunction.  */
end_comment

begin_class
DECL|class|AbstractNonStreamingHashFunctionTest
specifier|public
class|class
name|AbstractNonStreamingHashFunctionTest
extends|extends
name|TestCase
block|{
comment|/**    * Constructs two trivial HashFunctions (output := input), one streaming and one non-streaming,    * and checks that their results are identical, no matter which newHasher version we used.    */
DECL|method|testExhaustive ()
specifier|public
name|void
name|testExhaustive
parameter_list|()
block|{
name|List
argument_list|<
name|Hasher
argument_list|>
name|hashers
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|StreamingVersion
argument_list|()
operator|.
name|newHasher
argument_list|()
argument_list|,
operator|new
name|StreamingVersion
argument_list|()
operator|.
name|newHasher
argument_list|(
literal|52
argument_list|)
argument_list|,
operator|new
name|NonStreamingVersion
argument_list|()
operator|.
name|newHasher
argument_list|()
argument_list|,
operator|new
name|NonStreamingVersion
argument_list|()
operator|.
name|newHasher
argument_list|(
literal|123
argument_list|)
argument_list|)
decl_stmt|;
name|Random
name|random
init|=
operator|new
name|Random
argument_list|(
literal|0
argument_list|)
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
literal|200
condition|;
name|i
operator|++
control|)
block|{
name|RandomHasherAction
operator|.
name|pickAtRandom
argument_list|(
name|random
argument_list|)
operator|.
name|performAction
argument_list|(
name|random
argument_list|,
name|hashers
argument_list|)
expr_stmt|;
block|}
name|HashCode
index|[]
name|codes
init|=
operator|new
name|HashCode
index|[
name|hashers
operator|.
name|size
argument_list|()
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
name|hashers
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|codes
index|[
name|i
index|]
operator|=
name|hashers
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|hash
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|codes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|codes
index|[
name|i
operator|-
literal|1
index|]
argument_list|,
name|codes
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testPutStringWithLowSurrogate ()
specifier|public
name|void
name|testPutStringWithLowSurrogate
parameter_list|()
block|{
comment|// we pad because the dummy hash function we use to test this, merely copies the input into
comment|// the output, so the input must be at least 32 bits, since the output has to be that long
name|assertPutString
argument_list|(
operator|new
name|char
index|[]
block|{
literal|'p'
block|,
name|HashTestUtils
operator|.
name|randomLowSurrogate
argument_list|(
operator|new
name|Random
argument_list|()
argument_list|)
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|testPutStringWithHighSurrogate ()
specifier|public
name|void
name|testPutStringWithHighSurrogate
parameter_list|()
block|{
comment|// we pad because the dummy hash function we use to test this, merely copies the input into
comment|// the output, so the input must be at least 32 bits, since the output has to be that long
name|assertPutString
argument_list|(
operator|new
name|char
index|[]
block|{
literal|'p'
block|,
name|HashTestUtils
operator|.
name|randomHighSurrogate
argument_list|(
operator|new
name|Random
argument_list|()
argument_list|)
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|testPutStringWithLowHighSurrogate ()
specifier|public
name|void
name|testPutStringWithLowHighSurrogate
parameter_list|()
block|{
name|assertPutString
argument_list|(
operator|new
name|char
index|[]
block|{
name|HashTestUtils
operator|.
name|randomLowSurrogate
argument_list|(
operator|new
name|Random
argument_list|()
argument_list|)
block|,
name|HashTestUtils
operator|.
name|randomHighSurrogate
argument_list|(
operator|new
name|Random
argument_list|()
argument_list|)
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|testPutStringWithHighLowSurrogate ()
specifier|public
name|void
name|testPutStringWithHighLowSurrogate
parameter_list|()
block|{
name|assertPutString
argument_list|(
operator|new
name|char
index|[]
block|{
name|HashTestUtils
operator|.
name|randomHighSurrogate
argument_list|(
operator|new
name|Random
argument_list|()
argument_list|)
block|,
name|HashTestUtils
operator|.
name|randomLowSurrogate
argument_list|(
operator|new
name|Random
argument_list|()
argument_list|)
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|assertPutString (char[] chars)
specifier|private
specifier|static
name|void
name|assertPutString
parameter_list|(
name|char
index|[]
name|chars
parameter_list|)
block|{
name|Hasher
name|h1
init|=
operator|new
name|NonStreamingVersion
argument_list|()
operator|.
name|newHasher
argument_list|()
decl_stmt|;
name|Hasher
name|h2
init|=
operator|new
name|NonStreamingVersion
argument_list|()
operator|.
name|newHasher
argument_list|()
decl_stmt|;
name|String
name|s
init|=
operator|new
name|String
argument_list|(
name|chars
argument_list|)
decl_stmt|;
comment|// this is the correct implementation of the spec
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|s
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|h1
operator|.
name|putChar
argument_list|(
name|s
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|h2
operator|.
name|putUnencodedChars
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|h1
operator|.
name|hash
argument_list|()
argument_list|,
name|h2
operator|.
name|hash
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|StreamingVersion
specifier|static
class|class
name|StreamingVersion
extends|extends
name|AbstractStreamingHashFunction
block|{
annotation|@
name|Override
DECL|method|bits ()
specifier|public
name|int
name|bits
parameter_list|()
block|{
return|return
literal|32
return|;
block|}
annotation|@
name|Override
DECL|method|newHasher ()
specifier|public
name|Hasher
name|newHasher
parameter_list|()
block|{
return|return
operator|new
name|AbstractStreamingHasher
argument_list|(
literal|4
argument_list|,
literal|4
argument_list|)
block|{
specifier|final
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
annotation|@
name|Override
name|HashCode
name|makeHash
parameter_list|()
block|{
return|return
name|HashCode
operator|.
name|fromBytes
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|process
parameter_list|(
name|ByteBuffer
name|bb
parameter_list|)
block|{
while|while
condition|(
name|bb
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|bb
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|processRemaining
parameter_list|(
name|ByteBuffer
name|bb
parameter_list|)
block|{
while|while
condition|(
name|bb
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|bb
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|;
block|}
block|}
DECL|class|NonStreamingVersion
specifier|static
class|class
name|NonStreamingVersion
extends|extends
name|AbstractNonStreamingHashFunction
block|{
annotation|@
name|Override
DECL|method|bits ()
specifier|public
name|int
name|bits
parameter_list|()
block|{
return|return
literal|32
return|;
block|}
annotation|@
name|Override
DECL|method|hashBytes (byte[] input)
specifier|public
name|HashCode
name|hashBytes
parameter_list|(
name|byte
index|[]
name|input
parameter_list|)
block|{
return|return
name|HashCode
operator|.
name|fromBytes
argument_list|(
name|input
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashBytes (byte[] input, int off, int len)
specifier|public
name|HashCode
name|hashBytes
parameter_list|(
name|byte
index|[]
name|input
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
return|return
name|HashCode
operator|.
name|fromBytes
argument_list|(
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|input
argument_list|,
name|off
argument_list|,
name|off
operator|+
name|len
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashString (CharSequence input, Charset charset)
specifier|public
name|HashCode
name|hashString
parameter_list|(
name|CharSequence
name|input
parameter_list|,
name|Charset
name|charset
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|hashLong (long input)
specifier|public
name|HashCode
name|hashLong
parameter_list|(
name|long
name|input
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|hashInt (int input)
specifier|public
name|HashCode
name|hashInt
parameter_list|(
name|int
name|input
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

