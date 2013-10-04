begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|common
operator|.
name|annotations
operator|.
name|GwtCompatible
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
comment|/**  * Unit tests for {@link Utf8}.  *  * @author Jon Perlow  * @author Martin Buchholz  * @author ClÃ©ment Roux  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|Utf8Test
specifier|public
class|class
name|Utf8Test
extends|extends
name|TestCase
block|{
DECL|method|testUtf8Length_validStrings ()
specifier|public
name|void
name|testUtf8Length_validStrings
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Utf8
operator|.
name|utf8Length
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|Utf8
operator|.
name|utf8Length
argument_list|(
literal|"Hello world"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|Utf8
operator|.
name|utf8Length
argument_list|(
literal|"RÃ©sumÃ©"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|461
argument_list|,
name|Utf8
operator|.
name|utf8Length
argument_list|(
literal|"å¨å»Â·èå£«æ¯äºï¼William Shakespeareï¼"
operator|+
literal|"1564å¹´4æ26èâ1616å¹´4æ23è[1]ï¼ä¿é»è±åå°æ¼å¡ãåä½å®¶åè©©äººï¼"
operator|+
literal|"ææéä½¢ç°¡ç¨±èç¿ï¼ä¸­åæ¸æ«æ°ååæç¿»è­¯åèåæ¯æ¯ãæ²æ¯ç®è³ãç­æ¯æ¯è³ã"
operator|+
literal|"èåºæ¯åºå°ãç´¢å£«æ¯å°ãå¤åæèå°ãå¸åè¦ç®é¿ãå¶æ¯å£ãæ²åç®å°ã"
operator|+
literal|"ç¹æ¯ä¸ç¾ã[2]èå£«æ¯äºç·¨å¯«éå¥½å¤ä½åï¼ä½¢å°åä½é¿è¥¿æ´æå­¸å¥½æå½±é¿ï¼"
operator|+
literal|"åé½æäººç¿»è­¯åå¥½å¤è©±ã"
argument_list|)
argument_list|)
expr_stmt|;
comment|// A surrogate pair
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|Utf8
operator|.
name|utf8Length
argument_list|(
name|newString
argument_list|(
name|Character
operator|.
name|MIN_HIGH_SURROGATE
argument_list|,
name|Character
operator|.
name|MIN_LOW_SURROGATE
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUtf8Length_invalidStrings ()
specifier|public
name|void
name|testUtf8Length_invalidStrings
parameter_list|()
block|{
name|testUtf8LengthFails
argument_list|(
name|newString
argument_list|(
name|Character
operator|.
name|MIN_HIGH_SURROGATE
argument_list|)
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|testUtf8LengthFails
argument_list|(
literal|"foobar"
operator|+
name|newString
argument_list|(
name|Character
operator|.
name|MIN_HIGH_SURROGATE
argument_list|)
argument_list|,
literal|6
argument_list|)
expr_stmt|;
name|testUtf8LengthFails
argument_list|(
name|newString
argument_list|(
name|Character
operator|.
name|MIN_LOW_SURROGATE
argument_list|)
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|testUtf8LengthFails
argument_list|(
literal|"foobar"
operator|+
name|newString
argument_list|(
name|Character
operator|.
name|MIN_LOW_SURROGATE
argument_list|)
argument_list|,
literal|6
argument_list|)
expr_stmt|;
name|testUtf8LengthFails
argument_list|(
name|newString
argument_list|(
name|Character
operator|.
name|MIN_HIGH_SURROGATE
argument_list|,
name|Character
operator|.
name|MIN_HIGH_SURROGATE
argument_list|)
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|testUtf8LengthFails (String invalidString, int invalidCodePointIndex)
specifier|private
specifier|static
name|void
name|testUtf8LengthFails
parameter_list|(
name|String
name|invalidString
parameter_list|,
name|int
name|invalidCodePointIndex
parameter_list|)
block|{
try|try
block|{
name|Utf8
operator|.
name|utf8Length
argument_list|(
name|invalidString
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
comment|// Expected
name|String
name|message
init|=
name|ex
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|String
name|expected
init|=
literal|"Unpaired surrogate at "
operator|+
name|invalidCodePointIndex
operator|+
literal|" ("
operator|+
name|invalidString
operator|+
literal|")"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
block|}
comment|// 128 - [chars 0x0000 to 0x007f]
DECL|field|ONE_BYTE_ROUNDTRIPPABLE_CHARACTERS
specifier|private
specifier|static
specifier|final
name|long
name|ONE_BYTE_ROUNDTRIPPABLE_CHARACTERS
init|=
literal|0x007f
operator|-
literal|0x0000
operator|+
literal|1
decl_stmt|;
comment|// 128
DECL|field|EXPECTED_ONE_BYTE_ROUNDTRIPPABLE_COUNT
specifier|private
specifier|static
specifier|final
name|long
name|EXPECTED_ONE_BYTE_ROUNDTRIPPABLE_COUNT
init|=
name|ONE_BYTE_ROUNDTRIPPABLE_CHARACTERS
decl_stmt|;
comment|// 1920 [chars 0x0080 to 0x07FF]
DECL|field|TWO_BYTE_ROUNDTRIPPABLE_CHARACTERS
specifier|private
specifier|static
specifier|final
name|long
name|TWO_BYTE_ROUNDTRIPPABLE_CHARACTERS
init|=
literal|0x07FF
operator|-
literal|0x0080
operator|+
literal|1
decl_stmt|;
comment|// 18,304
DECL|field|EXPECTED_TWO_BYTE_ROUNDTRIPPABLE_COUNT
specifier|private
specifier|static
specifier|final
name|long
name|EXPECTED_TWO_BYTE_ROUNDTRIPPABLE_COUNT
init|=
comment|// Both bytes are one byte characters
operator|(
name|long
operator|)
name|Math
operator|.
name|pow
argument_list|(
name|EXPECTED_ONE_BYTE_ROUNDTRIPPABLE_COUNT
argument_list|,
literal|2
argument_list|)
operator|+
comment|// The possible number of two byte characters
name|TWO_BYTE_ROUNDTRIPPABLE_CHARACTERS
decl_stmt|;
comment|// 2048
DECL|field|THREE_BYTE_SURROGATES
specifier|private
specifier|static
specifier|final
name|long
name|THREE_BYTE_SURROGATES
init|=
literal|2
operator|*
literal|1024
decl_stmt|;
comment|// 61,440 [chars 0x0800 to 0xFFFF, minus surrogates]
DECL|field|THREE_BYTE_ROUNDTRIPPABLE_CHARACTERS
specifier|private
specifier|static
specifier|final
name|long
name|THREE_BYTE_ROUNDTRIPPABLE_CHARACTERS
init|=
literal|0xFFFF
operator|-
literal|0x0800
operator|+
literal|1
operator|-
name|THREE_BYTE_SURROGATES
decl_stmt|;
comment|// 2,650,112
DECL|field|EXPECTED_THREE_BYTE_ROUNDTRIPPABLE_COUNT
specifier|private
specifier|static
specifier|final
name|long
name|EXPECTED_THREE_BYTE_ROUNDTRIPPABLE_COUNT
init|=
comment|// All one byte characters
operator|(
name|long
operator|)
name|Math
operator|.
name|pow
argument_list|(
name|EXPECTED_ONE_BYTE_ROUNDTRIPPABLE_COUNT
argument_list|,
literal|3
argument_list|)
operator|+
comment|// One two byte character and a one byte character
literal|2
operator|*
name|TWO_BYTE_ROUNDTRIPPABLE_CHARACTERS
operator|*
name|ONE_BYTE_ROUNDTRIPPABLE_CHARACTERS
operator|+
comment|// Three byte characters
name|THREE_BYTE_ROUNDTRIPPABLE_CHARACTERS
decl_stmt|;
comment|// 1,048,576 [chars 0x10000L to 0x10FFFF]
DECL|field|FOUR_BYTE_ROUNDTRIPPABLE_CHARACTERS
specifier|private
specifier|static
specifier|final
name|long
name|FOUR_BYTE_ROUNDTRIPPABLE_CHARACTERS
init|=
literal|0x10FFFF
operator|-
literal|0x10000L
operator|+
literal|1
decl_stmt|;
comment|// 289,571,839
DECL|field|EXPECTED_FOUR_BYTE_ROUNDTRIPPABLE_COUNT
specifier|private
specifier|static
specifier|final
name|long
name|EXPECTED_FOUR_BYTE_ROUNDTRIPPABLE_COUNT
init|=
comment|// All one byte characters
operator|(
name|long
operator|)
name|Math
operator|.
name|pow
argument_list|(
name|EXPECTED_ONE_BYTE_ROUNDTRIPPABLE_COUNT
argument_list|,
literal|4
argument_list|)
operator|+
comment|// One and three byte characters
literal|2
operator|*
name|THREE_BYTE_ROUNDTRIPPABLE_CHARACTERS
operator|*
name|ONE_BYTE_ROUNDTRIPPABLE_CHARACTERS
operator|+
comment|// Two two byte characters
name|TWO_BYTE_ROUNDTRIPPABLE_CHARACTERS
operator|*
name|TWO_BYTE_ROUNDTRIPPABLE_CHARACTERS
operator|+
comment|// Permutations of one and two byte characters
literal|3
operator|*
name|TWO_BYTE_ROUNDTRIPPABLE_CHARACTERS
operator|*
name|ONE_BYTE_ROUNDTRIPPABLE_CHARACTERS
operator|*
name|ONE_BYTE_ROUNDTRIPPABLE_CHARACTERS
operator|+
comment|// Four byte characters
name|FOUR_BYTE_ROUNDTRIPPABLE_CHARACTERS
decl_stmt|;
comment|/**    * Tests that round tripping of a sample of four byte permutations work.    * All permutations are prohibitively expensive to test for automated runs.    * This method tests specific four-byte cases.    */
DECL|method|testIsValidUtf8_4BytesSamples ()
specifier|public
name|void
name|testIsValidUtf8_4BytesSamples
parameter_list|()
block|{
comment|// Valid 4 byte.
name|assertValidUtf8
argument_list|(
literal|0xF0
argument_list|,
literal|0xA4
argument_list|,
literal|0xAD
argument_list|,
literal|0xA2
argument_list|)
expr_stmt|;
comment|// Bad trailing bytes
name|assertInvalidUtf8
argument_list|(
literal|0xF0
argument_list|,
literal|0xA4
argument_list|,
literal|0xAD
argument_list|,
literal|0x7F
argument_list|)
expr_stmt|;
name|assertInvalidUtf8
argument_list|(
literal|0xF0
argument_list|,
literal|0xA4
argument_list|,
literal|0xAD
argument_list|,
literal|0xC0
argument_list|)
expr_stmt|;
comment|// Special cases for byte2
name|assertInvalidUtf8
argument_list|(
literal|0xF0
argument_list|,
literal|0x8F
argument_list|,
literal|0xAD
argument_list|,
literal|0xA2
argument_list|)
expr_stmt|;
name|assertInvalidUtf8
argument_list|(
literal|0xF4
argument_list|,
literal|0x90
argument_list|,
literal|0xAD
argument_list|,
literal|0xA2
argument_list|)
expr_stmt|;
block|}
comment|/** Tests some hard-coded test cases. */
DECL|method|testSomeSequences ()
specifier|public
name|void
name|testSomeSequences
parameter_list|()
block|{
comment|// Empty
name|assertValidUtf8
argument_list|()
expr_stmt|;
comment|// One-byte characters, including control characters
name|assertValidUtf8
argument_list|(
literal|0x00
argument_list|,
literal|0x61
argument_list|,
literal|0x62
argument_list|,
literal|0x63
argument_list|,
literal|0x7F
argument_list|)
expr_stmt|;
comment|// "\u0000abc\u007f"
comment|// Two-byte characters
name|assertValidUtf8
argument_list|(
literal|0xC2
argument_list|,
literal|0xA2
argument_list|,
literal|0xC2
argument_list|,
literal|0xA2
argument_list|)
expr_stmt|;
comment|// "\u00a2\u00a2"
comment|// Three-byte characters
name|assertValidUtf8
argument_list|(
literal|0xc8
argument_list|,
literal|0x8a
argument_list|,
literal|0x63
argument_list|,
literal|0xc8
argument_list|,
literal|0x8a
argument_list|,
literal|0x63
argument_list|)
expr_stmt|;
comment|// "\u020ac\u020ac"
comment|// Four-byte characters
comment|// "\u024B62\u024B62"
name|assertValidUtf8
argument_list|(
literal|0xc9
argument_list|,
literal|0x8b
argument_list|,
literal|0x36
argument_list|,
literal|0x32
argument_list|,
literal|0xc9
argument_list|,
literal|0x8b
argument_list|,
literal|0x36
argument_list|,
literal|0x32
argument_list|)
expr_stmt|;
comment|// Mixed string
comment|// "a\u020ac\u00a2b\\u024B62u020acc\u00a2de\u024B62"
name|assertValidUtf8
argument_list|(
literal|0x61
argument_list|,
literal|0xc8
argument_list|,
literal|0x8a
argument_list|,
literal|0x63
argument_list|,
literal|0xc2
argument_list|,
literal|0xa2
argument_list|,
literal|0x62
argument_list|,
literal|0x5c
argument_list|,
literal|0x75
argument_list|,
literal|0x30
argument_list|,
literal|0x32
argument_list|,
literal|0x34
argument_list|,
literal|0x42
argument_list|,
literal|0x36
argument_list|,
literal|0x32
argument_list|,
literal|0x75
argument_list|,
literal|0x30
argument_list|,
literal|0x32
argument_list|,
literal|0x30
argument_list|,
literal|0x61
argument_list|,
literal|0x63
argument_list|,
literal|0x63
argument_list|,
literal|0xc2
argument_list|,
literal|0xa2
argument_list|,
literal|0x64
argument_list|,
literal|0x65
argument_list|,
literal|0xc9
argument_list|,
literal|0x8b
argument_list|,
literal|0x36
argument_list|,
literal|0x32
argument_list|)
expr_stmt|;
comment|// Not a valid string
name|assertInvalidUtf8
argument_list|(
operator|-
literal|1
argument_list|,
literal|0
argument_list|,
operator|-
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|testShardsHaveExpectedRoundTrippables ()
specifier|public
name|void
name|testShardsHaveExpectedRoundTrippables
parameter_list|()
block|{
comment|// A sanity check.
name|long
name|actual
init|=
literal|0
decl_stmt|;
for|for
control|(
name|long
name|expected
range|:
name|generateFourByteShardsExpectedRunnables
argument_list|()
control|)
block|{
name|actual
operator|+=
name|expected
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|EXPECTED_FOUR_BYTE_ROUNDTRIPPABLE_COUNT
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
DECL|method|newString (char... chars)
specifier|private
name|String
name|newString
parameter_list|(
name|char
modifier|...
name|chars
parameter_list|)
block|{
return|return
operator|new
name|String
argument_list|(
name|chars
argument_list|)
return|;
block|}
DECL|method|toByteArray (int... bytes)
specifier|private
name|byte
index|[]
name|toByteArray
parameter_list|(
name|int
modifier|...
name|bytes
parameter_list|)
block|{
name|byte
index|[]
name|realBytes
init|=
operator|new
name|byte
index|[
name|bytes
operator|.
name|length
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
name|bytes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|realBytes
index|[
name|i
index|]
operator|=
operator|(
name|byte
operator|)
name|bytes
index|[
name|i
index|]
expr_stmt|;
block|}
return|return
name|realBytes
return|;
block|}
DECL|method|assertValidUtf8 (int... bytes)
specifier|private
name|void
name|assertValidUtf8
parameter_list|(
name|int
modifier|...
name|bytes
parameter_list|)
block|{
name|assertValidOrInvalidUtf8
argument_list|(
name|bytes
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|assertValidUtf8 (byte[] bytes)
specifier|private
name|void
name|assertValidUtf8
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|assertValidOrInvalidUtf8
argument_list|(
name|bytes
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|assertInvalidUtf8 (int... bytes)
specifier|private
name|void
name|assertInvalidUtf8
parameter_list|(
name|int
modifier|...
name|bytes
parameter_list|)
block|{
name|assertValidOrInvalidUtf8
argument_list|(
name|bytes
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|assertValidOrInvalidUtf8 (int[] bytes, boolean not)
specifier|private
name|void
name|assertValidOrInvalidUtf8
parameter_list|(
name|int
index|[]
name|bytes
parameter_list|,
name|boolean
name|not
parameter_list|)
block|{
name|assertValidOrInvalidUtf8
argument_list|(
name|toByteArray
argument_list|(
name|bytes
argument_list|)
argument_list|,
name|not
argument_list|)
expr_stmt|;
block|}
DECL|method|assertValidOrInvalidUtf8 (byte[] bytes, boolean not)
specifier|private
name|void
name|assertValidOrInvalidUtf8
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|boolean
name|not
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|not
operator|^
name|Utf8
operator|.
name|isValidUtf8
argument_list|(
name|bytes
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|not
operator|^
name|Utf8
operator|.
name|isValidUtf8
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|generateFourByteShardsExpectedRunnables ()
specifier|private
specifier|static
name|long
index|[]
name|generateFourByteShardsExpectedRunnables
parameter_list|()
block|{
name|long
index|[]
name|expected
init|=
operator|new
name|long
index|[
literal|128
index|]
decl_stmt|;
comment|// 0-63 are all 5300224
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<=
literal|63
condition|;
name|i
operator|++
control|)
block|{
name|expected
index|[
name|i
index|]
operator|=
literal|5300224
expr_stmt|;
block|}
comment|// 97-111 are all 2342912
for|for
control|(
name|int
name|i
init|=
literal|97
init|;
name|i
operator|<=
literal|111
condition|;
name|i
operator|++
control|)
block|{
name|expected
index|[
name|i
index|]
operator|=
literal|2342912
expr_stmt|;
block|}
comment|// 113-117 are all 1048576
for|for
control|(
name|int
name|i
init|=
literal|113
init|;
name|i
operator|<=
literal|117
condition|;
name|i
operator|++
control|)
block|{
name|expected
index|[
name|i
index|]
operator|=
literal|1048576
expr_stmt|;
block|}
comment|// One offs
name|expected
index|[
literal|112
index|]
operator|=
literal|786432
expr_stmt|;
name|expected
index|[
literal|118
index|]
operator|=
literal|786432
expr_stmt|;
name|expected
index|[
literal|119
index|]
operator|=
literal|1048576
expr_stmt|;
name|expected
index|[
literal|120
index|]
operator|=
literal|458752
expr_stmt|;
name|expected
index|[
literal|121
index|]
operator|=
literal|524288
expr_stmt|;
name|expected
index|[
literal|122
index|]
operator|=
literal|65536
expr_stmt|;
comment|// Anything not assigned was the default 0.
return|return
name|expected
return|;
block|}
block|}
end_class

end_unit

