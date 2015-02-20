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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|GwtIncompatible
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
name|HashMap
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
DECL|method|testEncodedLength_validStrings ()
specifier|public
name|void
name|testEncodedLength_validStrings
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Utf8
operator|.
name|encodedLength
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
name|encodedLength
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
name|encodedLength
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
name|encodedLength
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
name|encodedLength
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
DECL|method|testEncodedLength_validStrings2 ()
specifier|public
name|void
name|testEncodedLength_validStrings2
parameter_list|()
block|{
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|utf8Lengths
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
name|utf8Lengths
operator|.
name|put
argument_list|(
literal|0x00
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|utf8Lengths
operator|.
name|put
argument_list|(
literal|0x7f
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|utf8Lengths
operator|.
name|put
argument_list|(
literal|0x80
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|utf8Lengths
operator|.
name|put
argument_list|(
literal|0x7ff
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|utf8Lengths
operator|.
name|put
argument_list|(
literal|0x800
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|utf8Lengths
operator|.
name|put
argument_list|(
name|Character
operator|.
name|MIN_SUPPLEMENTARY_CODE_POINT
operator|-
literal|1
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|utf8Lengths
operator|.
name|put
argument_list|(
name|Character
operator|.
name|MIN_SUPPLEMENTARY_CODE_POINT
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|utf8Lengths
operator|.
name|put
argument_list|(
name|Character
operator|.
name|MAX_CODE_POINT
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|Integer
index|[]
name|codePoints
init|=
name|utf8Lengths
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|Integer
index|[]
block|{}
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Random
name|rnd
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|trial
init|=
literal|0
init|;
name|trial
operator|<
literal|100
condition|;
name|trial
operator|++
control|)
block|{
name|sb
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|int
name|utf8Length
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
literal|6
condition|;
name|i
operator|++
control|)
block|{
name|Integer
name|randomCodePoint
init|=
name|codePoints
index|[
name|rnd
operator|.
name|nextInt
argument_list|(
name|codePoints
operator|.
name|length
argument_list|)
index|]
decl_stmt|;
name|sb
operator|.
name|appendCodePoint
argument_list|(
name|randomCodePoint
argument_list|)
expr_stmt|;
name|utf8Length
operator|+=
name|utf8Lengths
operator|.
name|get
argument_list|(
name|randomCodePoint
argument_list|)
expr_stmt|;
if|if
condition|(
name|utf8Length
operator|!=
name|Utf8
operator|.
name|encodedLength
argument_list|(
name|sb
argument_list|)
condition|)
block|{
name|StringBuilder
name|repro
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|sb
operator|.
name|length
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|repro
operator|.
name|append
argument_list|(
literal|" "
operator|+
operator|(
name|int
operator|)
name|sb
operator|.
name|charAt
argument_list|(
name|j
argument_list|)
argument_list|)
expr_stmt|;
comment|// GWT compatible
block|}
name|assertEquals
argument_list|(
name|repro
operator|.
name|toString
argument_list|()
argument_list|,
name|utf8Length
argument_list|,
name|Utf8
operator|.
name|encodedLength
argument_list|(
name|sb
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|testEncodedLength_invalidStrings ()
specifier|public
name|void
name|testEncodedLength_invalidStrings
parameter_list|()
block|{
name|testEncodedLengthFails
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
name|testEncodedLengthFails
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
name|testEncodedLengthFails
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
name|testEncodedLengthFails
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
name|testEncodedLengthFails
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"CheckReturnValue"
argument_list|)
DECL|method|testEncodedLengthFails (String invalidString, int invalidCodePointIndex)
specifier|private
specifier|static
name|void
name|testEncodedLengthFails
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
name|encodedLength
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
name|expected
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Unpaired surrogate at index "
operator|+
name|invalidCodePointIndex
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
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
comment|/** Tests that round tripping of all two byte permutations work. */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.nio.charset.Charset"
argument_list|)
DECL|method|testIsWellFormed_1Byte ()
specifier|public
name|void
name|testIsWellFormed_1Byte
parameter_list|()
block|{
name|testBytes
argument_list|(
literal|1
argument_list|,
name|EXPECTED_ONE_BYTE_ROUNDTRIPPABLE_COUNT
argument_list|)
expr_stmt|;
block|}
comment|/** Tests that round tripping of all two byte permutations work. */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.nio.charset.Charset"
argument_list|)
DECL|method|testIsWellFormed_2Bytes ()
specifier|public
name|void
name|testIsWellFormed_2Bytes
parameter_list|()
block|{
name|testBytes
argument_list|(
literal|2
argument_list|,
name|EXPECTED_TWO_BYTE_ROUNDTRIPPABLE_COUNT
argument_list|)
expr_stmt|;
block|}
comment|/** Tests that round tripping of all three byte permutations work. */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.nio.charset.Charset"
argument_list|)
DECL|method|testIsWellFormed_3Bytes ()
specifier|public
name|void
name|testIsWellFormed_3Bytes
parameter_list|()
block|{
name|testBytes
argument_list|(
literal|3
argument_list|,
name|EXPECTED_THREE_BYTE_ROUNDTRIPPABLE_COUNT
argument_list|)
expr_stmt|;
block|}
comment|/**    * Tests that round tripping of a sample of four byte permutations work.    * All permutations are prohibitively expensive to test for automated runs.    * This method tests specific four-byte cases.    */
DECL|method|testIsWellFormed_4BytesSamples ()
specifier|public
name|void
name|testIsWellFormed_4BytesSamples
parameter_list|()
block|{
comment|// Valid 4 byte.
name|assertWellFormed
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
name|assertNotWellFormed
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
name|assertNotWellFormed
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
name|assertNotWellFormed
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
name|assertNotWellFormed
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
name|assertWellFormed
argument_list|()
expr_stmt|;
comment|// One-byte characters, including control characters
name|assertWellFormed
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
name|assertWellFormed
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
name|assertWellFormed
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
name|assertWellFormed
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
name|assertWellFormed
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
name|assertNotWellFormed
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
DECL|method|assertWellFormed (int... bytes)
specifier|private
name|void
name|assertWellFormed
parameter_list|(
name|int
modifier|...
name|bytes
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Utf8
operator|.
name|isWellFormed
argument_list|(
name|toByteArray
argument_list|(
name|bytes
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertNotWellFormed (int... bytes)
specifier|private
name|void
name|assertNotWellFormed
parameter_list|(
name|int
modifier|...
name|bytes
parameter_list|)
block|{
name|assertFalse
argument_list|(
name|Utf8
operator|.
name|isWellFormed
argument_list|(
name|toByteArray
argument_list|(
name|bytes
argument_list|)
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
comment|/**    * Helper to run the loop to test all the permutations for the number of bytes    * specified.    *    * @param numBytes the number of bytes in the byte array    * @param expectedCount the expected number of roundtrippable permutations    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.nio.charset.Charset"
argument_list|)
DECL|method|testBytes (int numBytes, long expectedCount)
specifier|private
specifier|static
name|void
name|testBytes
parameter_list|(
name|int
name|numBytes
parameter_list|,
name|long
name|expectedCount
parameter_list|)
block|{
name|testBytes
argument_list|(
name|numBytes
argument_list|,
name|expectedCount
argument_list|,
literal|0
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
comment|/**    * Helper to run the loop to test all the permutations for the number of bytes    * specified. This overload is useful for debugging to get the loop to start    * at a certain character.    *    * @param numBytes the number of bytes in the byte array    * @param expectedCount the expected number of roundtrippable permutations    * @param start the starting bytes encoded as a long as big-endian    * @param lim the limit of bytes to process encoded as a long as big-endian,    *     or -1 to mean the max limit for numBytes    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.nio.charset.Charset"
argument_list|)
DECL|method|testBytes (int numBytes, long expectedCount, long start, long lim)
specifier|private
specifier|static
name|void
name|testBytes
parameter_list|(
name|int
name|numBytes
parameter_list|,
name|long
name|expectedCount
parameter_list|,
name|long
name|start
parameter_list|,
name|long
name|lim
parameter_list|)
block|{
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
name|numBytes
index|]
decl_stmt|;
if|if
condition|(
name|lim
operator|==
operator|-
literal|1
condition|)
block|{
name|lim
operator|=
literal|1L
operator|<<
operator|(
name|numBytes
operator|*
literal|8
operator|)
expr_stmt|;
block|}
name|long
name|countRoundTripped
init|=
literal|0
decl_stmt|;
for|for
control|(
name|long
name|byteChar
init|=
name|start
init|;
name|byteChar
operator|<
name|lim
condition|;
name|byteChar
operator|++
control|)
block|{
name|long
name|tmpByteChar
init|=
name|byteChar
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
name|numBytes
condition|;
name|i
operator|++
control|)
block|{
name|bytes
index|[
name|bytes
operator|.
name|length
operator|-
name|i
operator|-
literal|1
index|]
operator|=
operator|(
name|byte
operator|)
name|tmpByteChar
expr_stmt|;
name|tmpByteChar
operator|=
name|tmpByteChar
operator|>>
literal|8
expr_stmt|;
block|}
name|boolean
name|isRoundTrippable
init|=
name|Utf8
operator|.
name|isWellFormed
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|isRoundTrippable
argument_list|,
name|Utf8
operator|.
name|isWellFormed
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|numBytes
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|s
init|=
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|byte
index|[]
name|bytesReencoded
init|=
name|s
operator|.
name|getBytes
argument_list|(
name|Charsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|boolean
name|bytesEqual
init|=
name|Arrays
operator|.
name|equals
argument_list|(
name|bytes
argument_list|,
name|bytesReencoded
argument_list|)
decl_stmt|;
if|if
condition|(
name|bytesEqual
operator|!=
name|isRoundTrippable
condition|)
block|{
name|fail
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|isRoundTrippable
condition|)
block|{
name|countRoundTripped
operator|++
expr_stmt|;
block|}
block|}
name|assertEquals
argument_list|(
name|expectedCount
argument_list|,
name|countRoundTripped
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

