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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
operator|.
name|Hashing
operator|.
name|murmur3_32
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
name|Charsets
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
name|HashFn
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
comment|/** Tests for {@link Murmur3_32HashFunction}. */
end_comment

begin_class
DECL|class|Murmur3Hash32Test
specifier|public
class|class
name|Murmur3Hash32Test
extends|extends
name|TestCase
block|{
DECL|method|testKnownIntegerInputs ()
specifier|public
name|void
name|testKnownIntegerInputs
parameter_list|()
block|{
name|assertHash
argument_list|(
literal|593689054
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashInt
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
operator|-
literal|189366624
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashInt
argument_list|(
operator|-
literal|42
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
operator|-
literal|1134849565
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashInt
argument_list|(
literal|42
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
operator|-
literal|1718298732
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashInt
argument_list|(
name|Integer
operator|.
name|MIN_VALUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
operator|-
literal|1653689534
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashInt
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testKnownLongInputs ()
specifier|public
name|void
name|testKnownLongInputs
parameter_list|()
block|{
name|assertHash
argument_list|(
literal|1669671676
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashLong
argument_list|(
literal|0L
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
operator|-
literal|846261623
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashLong
argument_list|(
operator|-
literal|42L
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
literal|1871679806
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashLong
argument_list|(
literal|42L
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
literal|1366273829
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashLong
argument_list|(
name|Long
operator|.
name|MIN_VALUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
operator|-
literal|2106506049
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashLong
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testKnownStringInputs ()
specifier|public
name|void
name|testKnownStringInputs
parameter_list|()
block|{
name|assertHash
argument_list|(
literal|0
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashUnencodedChars
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
literal|679745764
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashUnencodedChars
argument_list|(
literal|"k"
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
literal|1510782915
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashUnencodedChars
argument_list|(
literal|"hell"
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
operator|-
literal|675079799
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashUnencodedChars
argument_list|(
literal|"hello"
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
literal|1935035788
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashUnencodedChars
argument_list|(
literal|"http://www.google.com/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
operator|-
literal|528633700
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashUnencodedChars
argument_list|(
literal|"The quick brown fox jumps over the lazy dog"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testKnownUtf8StringInputs ()
specifier|public
name|void
name|testKnownUtf8StringInputs
parameter_list|()
block|{
name|assertHash
argument_list|(
literal|0
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashString
argument_list|(
literal|""
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
literal|0xcfbda5d1
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashString
argument_list|(
literal|"k"
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
literal|0xa167dbf3
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashString
argument_list|(
literal|"hell"
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
literal|0x248bfa47
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashString
argument_list|(
literal|"hello"
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
literal|0x3d41b97c
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashString
argument_list|(
literal|"http://www.google.com/"
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
literal|0x2e4ff723
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashString
argument_list|(
literal|"The quick brown fox jumps over the lazy dog"
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
literal|0xfc5ba834
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashString
argument_list|(
literal|"æ¯æï¼æ¥,æ¯é±æææ¥"
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|testSimpleStringUtf8 ()
specifier|public
name|void
name|testSimpleStringUtf8
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|murmur3_32
argument_list|()
operator|.
name|hashBytes
argument_list|(
literal|"ABCDefGHI\u0799"
operator|.
name|getBytes
argument_list|(
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashString
argument_list|(
literal|"ABCDefGHI\u0799"
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|testStringInputsUtf8 ()
specifier|public
name|void
name|testStringInputsUtf8
parameter_list|()
block|{
name|Random
name|rng
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
name|z
init|=
literal|0
init|;
name|z
operator|<
literal|100
condition|;
name|z
operator|++
control|)
block|{
name|String
name|str
decl_stmt|;
name|int
index|[]
name|codePoints
init|=
operator|new
name|int
index|[
name|rng
operator|.
name|nextInt
argument_list|(
literal|8
argument_list|)
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
name|codePoints
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
do|do
block|{
name|codePoints
index|[
name|i
index|]
operator|=
name|rng
operator|.
name|nextInt
argument_list|(
literal|0x800
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
operator|!
name|Character
operator|.
name|isValidCodePoint
argument_list|(
name|codePoints
index|[
name|i
index|]
argument_list|)
operator|||
operator|(
name|codePoints
index|[
name|i
index|]
operator|>=
name|Character
operator|.
name|MIN_SURROGATE
operator|&&
name|codePoints
index|[
name|i
index|]
operator|<=
name|Character
operator|.
name|MAX_SURROGATE
operator|)
condition|)
do|;
block|}
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
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
name|codePoints
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|builder
operator|.
name|appendCodePoint
argument_list|(
name|codePoints
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|str
operator|=
name|builder
operator|.
name|toString
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|murmur3_32
argument_list|()
operator|.
name|hashBytes
argument_list|(
name|str
operator|.
name|getBytes
argument_list|(
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashString
argument_list|(
name|str
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertHash (int expected, HashCode actual)
specifier|private
specifier|static
name|void
name|assertHash
parameter_list|(
name|int
name|expected
parameter_list|,
name|HashCode
name|actual
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|HashCode
operator|.
name|fromInt
argument_list|(
name|expected
argument_list|)
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
DECL|method|testParanoidHashBytes ()
specifier|public
name|void
name|testParanoidHashBytes
parameter_list|()
block|{
name|HashFn
name|hf
init|=
operator|new
name|HashFn
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|hash
parameter_list|(
name|byte
index|[]
name|input
parameter_list|,
name|int
name|seed
parameter_list|)
block|{
return|return
name|murmur3_32
argument_list|(
name|seed
argument_list|)
operator|.
name|hashBytes
argument_list|(
name|input
argument_list|)
operator|.
name|asBytes
argument_list|()
return|;
block|}
block|}
decl_stmt|;
comment|// Murmur3A, MurmurHash3 for x86, 32-bit (MurmurHash3_x86_32)
comment|// https://github.com/aappleby/smhasher/blob/master/src/main.cpp
name|HashTestUtils
operator|.
name|verifyHashFunction
argument_list|(
name|hf
argument_list|,
literal|32
argument_list|,
literal|0xB0F57EE3
argument_list|)
expr_stmt|;
block|}
DECL|method|testParanoid ()
specifier|public
name|void
name|testParanoid
parameter_list|()
block|{
name|HashFn
name|hf
init|=
operator|new
name|HashFn
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|hash
parameter_list|(
name|byte
index|[]
name|input
parameter_list|,
name|int
name|seed
parameter_list|)
block|{
name|Hasher
name|hasher
init|=
name|murmur3_32
argument_list|(
name|seed
argument_list|)
operator|.
name|newHasher
argument_list|()
decl_stmt|;
name|Funnels
operator|.
name|byteArrayFunnel
argument_list|()
operator|.
name|funnel
argument_list|(
name|input
argument_list|,
name|hasher
argument_list|)
expr_stmt|;
return|return
name|hasher
operator|.
name|hash
argument_list|()
operator|.
name|asBytes
argument_list|()
return|;
block|}
block|}
decl_stmt|;
comment|// Murmur3A, MurmurHash3 for x86, 32-bit (MurmurHash3_x86_32)
comment|// https://github.com/aappleby/smhasher/blob/master/src/main.cpp
name|HashTestUtils
operator|.
name|verifyHashFunction
argument_list|(
name|hf
argument_list|,
literal|32
argument_list|,
literal|0xB0F57EE3
argument_list|)
expr_stmt|;
block|}
DECL|method|testInvariants ()
specifier|public
name|void
name|testInvariants
parameter_list|()
block|{
name|HashTestUtils
operator|.
name|assertInvariants
argument_list|(
name|murmur3_32
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|testInvalidUnicodeHashString ()
specifier|public
name|void
name|testInvalidUnicodeHashString
parameter_list|()
block|{
name|String
name|str
init|=
operator|new
name|String
argument_list|(
operator|new
name|char
index|[]
block|{
literal|'a'
block|,
name|Character
operator|.
name|MIN_HIGH_SURROGATE
block|,
name|Character
operator|.
name|MIN_HIGH_SURROGATE
block|,
literal|'z'
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|murmur3_32
argument_list|()
operator|.
name|hashBytes
argument_list|(
name|str
operator|.
name|getBytes
argument_list|(
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|hashString
argument_list|(
name|str
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testInvalidUnicodeHasherPutString ()
specifier|public
name|void
name|testInvalidUnicodeHasherPutString
parameter_list|()
block|{
name|String
name|str
init|=
operator|new
name|String
argument_list|(
operator|new
name|char
index|[]
block|{
literal|'a'
block|,
name|Character
operator|.
name|MIN_HIGH_SURROGATE
block|,
name|Character
operator|.
name|MIN_HIGH_SURROGATE
block|,
literal|'z'
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|murmur3_32
argument_list|()
operator|.
name|hashBytes
argument_list|(
name|str
operator|.
name|getBytes
argument_list|(
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
argument_list|,
name|murmur3_32
argument_list|()
operator|.
name|newHasher
argument_list|()
operator|.
name|putString
argument_list|(
name|str
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
operator|.
name|hash
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

