begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.escape
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|escape
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
comment|/**  * Tests for {@link UnicodeEscaper}.  *  * @author David Beaumont  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|UnicodeEscaperTest
specifier|public
class|class
name|UnicodeEscaperTest
extends|extends
name|TestCase
block|{
DECL|field|SMALLEST_SURROGATE
specifier|private
specifier|static
specifier|final
name|String
name|SMALLEST_SURROGATE
init|=
literal|""
operator|+
name|Character
operator|.
name|MIN_HIGH_SURROGATE
operator|+
name|Character
operator|.
name|MIN_LOW_SURROGATE
decl_stmt|;
DECL|field|LARGEST_SURROGATE
specifier|private
specifier|static
specifier|final
name|String
name|LARGEST_SURROGATE
init|=
literal|""
operator|+
name|Character
operator|.
name|MAX_HIGH_SURROGATE
operator|+
name|Character
operator|.
name|MAX_LOW_SURROGATE
decl_stmt|;
DECL|field|TEST_STRING
specifier|private
specifier|static
specifier|final
name|String
name|TEST_STRING
init|=
literal|"\0abyz\u0080\u0100\u0800\u1000ABYZ\uffff"
operator|+
name|SMALLEST_SURROGATE
operator|+
literal|"0189"
operator|+
name|LARGEST_SURROGATE
decl_stmt|;
comment|// Escapes nothing
DECL|field|NOP_ESCAPER
specifier|private
specifier|static
specifier|final
name|UnicodeEscaper
name|NOP_ESCAPER
init|=
operator|new
name|UnicodeEscaper
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|char
index|[]
name|escape
parameter_list|(
name|int
name|c
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
comment|// Escapes everything except [a-zA-Z0-9]
DECL|field|SIMPLE_ESCAPER
specifier|private
specifier|static
specifier|final
name|UnicodeEscaper
name|SIMPLE_ESCAPER
init|=
operator|new
name|UnicodeEscaper
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|char
index|[]
name|escape
parameter_list|(
name|int
name|cp
parameter_list|)
block|{
return|return
operator|(
literal|'a'
operator|<=
name|cp
operator|&&
name|cp
operator|<=
literal|'z'
operator|)
operator|||
operator|(
literal|'A'
operator|<=
name|cp
operator|&&
name|cp
operator|<=
literal|'Z'
operator|)
operator|||
operator|(
literal|'0'
operator|<=
name|cp
operator|&&
name|cp
operator|<=
literal|'9'
operator|)
condition|?
literal|null
else|:
operator|(
literal|"["
operator|+
name|String
operator|.
name|valueOf
argument_list|(
name|cp
argument_list|)
operator|+
literal|"]"
operator|)
operator|.
name|toCharArray
argument_list|()
return|;
block|}
block|}
decl_stmt|;
DECL|method|testNopEscaper ()
specifier|public
name|void
name|testNopEscaper
parameter_list|()
block|{
name|UnicodeEscaper
name|e
init|=
name|NOP_ESCAPER
decl_stmt|;
name|assertEquals
argument_list|(
name|TEST_STRING
argument_list|,
name|escapeAsString
argument_list|(
name|e
argument_list|,
name|TEST_STRING
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleEscaper ()
specifier|public
name|void
name|testSimpleEscaper
parameter_list|()
block|{
name|UnicodeEscaper
name|e
init|=
name|SIMPLE_ESCAPER
decl_stmt|;
name|String
name|expected
init|=
literal|"[0]abyz[128][256][2048][4096]ABYZ[65535]"
operator|+
literal|"["
operator|+
name|Character
operator|.
name|MIN_SUPPLEMENTARY_CODE_POINT
operator|+
literal|"]"
operator|+
literal|"0189["
operator|+
name|Character
operator|.
name|MAX_CODE_POINT
operator|+
literal|"]"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|escapeAsString
argument_list|(
name|e
argument_list|,
name|TEST_STRING
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGrowBuffer ()
specifier|public
name|void
name|testGrowBuffer
parameter_list|()
block|{
comment|// need to grow past an initial 1024 byte buffer
name|StringBuilder
name|input
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|StringBuilder
name|expected
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
literal|256
init|;
name|i
operator|<
literal|1024
condition|;
name|i
operator|++
control|)
block|{
name|input
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|i
argument_list|)
expr_stmt|;
name|expected
operator|.
name|append
argument_list|(
literal|"["
operator|+
name|i
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|expected
operator|.
name|toString
argument_list|()
argument_list|,
name|SIMPLE_ESCAPER
operator|.
name|escape
argument_list|(
name|input
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSurrogatePairs ()
specifier|public
name|void
name|testSurrogatePairs
parameter_list|()
block|{
name|UnicodeEscaper
name|e
init|=
name|SIMPLE_ESCAPER
decl_stmt|;
comment|// Build up a range of surrogate pair characters to test
specifier|final
name|int
name|min
init|=
name|Character
operator|.
name|MIN_SUPPLEMENTARY_CODE_POINT
decl_stmt|;
specifier|final
name|int
name|max
init|=
name|Character
operator|.
name|MAX_CODE_POINT
decl_stmt|;
specifier|final
name|int
name|range
init|=
name|max
operator|-
name|min
decl_stmt|;
specifier|final
name|int
name|s1
init|=
name|min
operator|+
operator|(
literal|1
operator|*
name|range
operator|)
operator|/
literal|4
decl_stmt|;
specifier|final
name|int
name|s2
init|=
name|min
operator|+
operator|(
literal|2
operator|*
name|range
operator|)
operator|/
literal|4
decl_stmt|;
specifier|final
name|int
name|s3
init|=
name|min
operator|+
operator|(
literal|3
operator|*
name|range
operator|)
operator|/
literal|4
decl_stmt|;
specifier|final
name|char
index|[]
name|dst
init|=
operator|new
name|char
index|[
literal|12
index|]
decl_stmt|;
comment|// Put surrogate pairs at odd indices so they can be split easily
name|dst
index|[
literal|0
index|]
operator|=
literal|'x'
expr_stmt|;
name|Character
operator|.
name|toChars
argument_list|(
name|min
argument_list|,
name|dst
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|Character
operator|.
name|toChars
argument_list|(
name|s1
argument_list|,
name|dst
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Character
operator|.
name|toChars
argument_list|(
name|s2
argument_list|,
name|dst
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|Character
operator|.
name|toChars
argument_list|(
name|s3
argument_list|,
name|dst
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|Character
operator|.
name|toChars
argument_list|(
name|max
argument_list|,
name|dst
argument_list|,
literal|9
argument_list|)
expr_stmt|;
name|dst
index|[
literal|11
index|]
operator|=
literal|'x'
expr_stmt|;
name|String
name|test
init|=
operator|new
name|String
argument_list|(
name|dst
argument_list|)
decl_stmt|;
comment|// Get the expected result string
name|String
name|expected
init|=
literal|"x["
operator|+
name|min
operator|+
literal|"]["
operator|+
name|s1
operator|+
literal|"]["
operator|+
name|s2
operator|+
literal|"]["
operator|+
name|s3
operator|+
literal|"]["
operator|+
name|max
operator|+
literal|"]x"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|escapeAsString
argument_list|(
name|e
argument_list|,
name|test
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTrailingHighSurrogate ()
specifier|public
name|void
name|testTrailingHighSurrogate
parameter_list|()
block|{
name|String
name|test
init|=
literal|"abc"
operator|+
name|Character
operator|.
name|MIN_HIGH_SURROGATE
decl_stmt|;
try|try
block|{
name|escapeAsString
argument_list|(
name|NOP_ESCAPER
argument_list|,
name|test
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Trailing high surrogate should cause exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
comment|// Pass
block|}
try|try
block|{
name|escapeAsString
argument_list|(
name|SIMPLE_ESCAPER
argument_list|,
name|test
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Trailing high surrogate should cause exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
comment|// Pass
block|}
block|}
DECL|method|testNullInput ()
specifier|public
name|void
name|testNullInput
parameter_list|()
block|{
name|UnicodeEscaper
name|e
init|=
name|SIMPLE_ESCAPER
decl_stmt|;
try|try
block|{
name|e
operator|.
name|escape
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Null string should cause exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{
comment|// Pass
block|}
block|}
DECL|method|testBadStrings ()
specifier|public
name|void
name|testBadStrings
parameter_list|()
block|{
name|UnicodeEscaper
name|e
init|=
name|SIMPLE_ESCAPER
decl_stmt|;
name|String
index|[]
name|BAD_STRINGS
init|=
block|{
name|String
operator|.
name|valueOf
argument_list|(
name|Character
operator|.
name|MIN_LOW_SURROGATE
argument_list|)
block|,
name|Character
operator|.
name|MIN_LOW_SURROGATE
operator|+
literal|"xyz"
block|,
literal|"abc"
operator|+
name|Character
operator|.
name|MIN_LOW_SURROGATE
block|,
literal|"abc"
operator|+
name|Character
operator|.
name|MIN_LOW_SURROGATE
operator|+
literal|"xyz"
block|,
name|String
operator|.
name|valueOf
argument_list|(
name|Character
operator|.
name|MAX_LOW_SURROGATE
argument_list|)
block|,
name|Character
operator|.
name|MAX_LOW_SURROGATE
operator|+
literal|"xyz"
block|,
literal|"abc"
operator|+
name|Character
operator|.
name|MAX_LOW_SURROGATE
block|,
literal|"abc"
operator|+
name|Character
operator|.
name|MAX_LOW_SURROGATE
operator|+
literal|"xyz"
block|,     }
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|BAD_STRINGS
control|)
block|{
try|try
block|{
name|escapeAsString
argument_list|(
name|e
argument_list|,
name|s
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Isolated low surrogate should cause exception ["
operator|+
name|s
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
comment|// Pass
block|}
block|}
block|}
DECL|method|testFalsePositivesForNextEscapedIndex ()
specifier|public
name|void
name|testFalsePositivesForNextEscapedIndex
parameter_list|()
block|{
name|UnicodeEscaper
name|e
init|=
operator|new
name|UnicodeEscaper
argument_list|()
block|{
comment|// Canonical escaper method that only escapes lower case ASCII letters.
annotation|@
name|Override
specifier|protected
name|char
index|[]
name|escape
parameter_list|(
name|int
name|cp
parameter_list|)
block|{
return|return
operator|(
literal|'a'
operator|<=
name|cp
operator|&&
name|cp
operator|<=
literal|'z'
operator|)
condition|?
operator|new
name|char
index|[]
block|{
name|Character
operator|.
name|toUpperCase
argument_list|(
operator|(
name|char
operator|)
name|cp
argument_list|)
block|}
else|:
literal|null
return|;
block|}
comment|// Inefficient implementation that defines all letters as escapable.
annotation|@
name|Override
specifier|protected
name|int
name|nextEscapeIndex
parameter_list|(
name|CharSequence
name|csq
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|end
parameter_list|)
block|{
while|while
condition|(
name|index
operator|<
name|end
operator|&&
operator|!
name|Character
operator|.
name|isLetter
argument_list|(
name|csq
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
argument_list|)
condition|)
block|{
name|index
operator|++
expr_stmt|;
block|}
return|return
name|index
return|;
block|}
block|}
decl_stmt|;
name|assertEquals
argument_list|(
literal|"\0HELLO \uD800\uDC00 WORLD!\n"
argument_list|,
name|e
operator|.
name|escape
argument_list|(
literal|"\0HeLLo \uD800\uDC00 WorlD!\n"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCodePointAt_IndexOutOfBoundsException ()
specifier|public
name|void
name|testCodePointAt_IndexOutOfBoundsException
parameter_list|()
block|{
try|try
block|{
name|UnicodeEscaper
operator|.
name|codePointAt
argument_list|(
literal|"Testing..."
argument_list|,
literal|4
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|escapeAsString (Escaper e, String s)
specifier|private
specifier|static
name|String
name|escapeAsString
parameter_list|(
name|Escaper
name|e
parameter_list|,
name|String
name|s
parameter_list|)
block|{
return|return
name|e
operator|.
name|escape
argument_list|(
name|s
argument_list|)
return|;
block|}
block|}
end_class

end_unit

