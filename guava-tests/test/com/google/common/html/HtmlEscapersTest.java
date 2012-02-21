begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.html
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|html
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
name|escape
operator|.
name|testing
operator|.
name|EscaperAsserts
operator|.
name|assertUnescaped
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|escape
operator|.
name|testing
operator|.
name|EscaperAsserts
operator|.
name|assertUnicodeEscaping
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|escape
operator|.
name|testing
operator|.
name|EscaperAsserts
operator|.
name|assertEscaping
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
name|escape
operator|.
name|UnicodeEscaper
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
comment|/**  * Tests for the {@link HtmlEscapers} class.  *  * @author David Beaumont  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|HtmlEscapersTest
specifier|public
class|class
name|HtmlEscapersTest
extends|extends
name|TestCase
block|{
DECL|method|testHtmlEscaper ()
specifier|public
name|void
name|testHtmlEscaper
parameter_list|()
throws|throws
name|Exception
block|{
name|UnicodeEscaper
name|e
init|=
name|HtmlEscapers
operator|.
name|htmlEscaper
argument_list|()
decl_stmt|;
comment|// Test a sampling of 'safe' chars.
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|' '
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'a'
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'A'
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'1'
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'~'
argument_list|)
expr_stmt|;
comment|// Test basic special chars.
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"&quot;"
argument_list|,
literal|'"'
argument_list|)
expr_stmt|;
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"&#39;"
argument_list|,
literal|'\''
argument_list|)
expr_stmt|;
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"&amp;"
argument_list|,
literal|'&'
argument_list|)
expr_stmt|;
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"&lt;"
argument_list|,
literal|'<'
argument_list|)
expr_stmt|;
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"&gt;"
argument_list|,
literal|'>'
argument_list|)
expr_stmt|;
comment|// Extended special chars (a sampling).
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"&nbsp;"
argument_list|,
literal|'\u00A0'
argument_list|)
expr_stmt|;
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"&pound;"
argument_list|,
literal|'\u00A3'
argument_list|)
expr_stmt|;
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"&raquo;"
argument_list|,
literal|'\u00BB'
argument_list|)
expr_stmt|;
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"&laquo;"
argument_list|,
literal|'\u00AB'
argument_list|)
expr_stmt|;
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"&diams;"
argument_list|,
literal|'\u2666'
argument_list|)
expr_stmt|;
comment|// Test decimal escaping conversion (3, 4& 5 length decimals).
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"&#127;"
argument_list|,
literal|'\u007F'
argument_list|)
expr_stmt|;
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"&#256;"
argument_list|,
literal|'\u0100'
argument_list|)
expr_stmt|;
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"&#9831;"
argument_list|,
literal|'\u2667'
argument_list|)
expr_stmt|;
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"&#65535;"
argument_list|,
literal|'\uFFFF'
argument_list|)
expr_stmt|;
comment|// Test escaping for surrogate characters (5, 6& 7 length decimals).
name|assertUnicodeEscaping
argument_list|(
name|e
argument_list|,
literal|"&#65536;"
argument_list|,
comment|// 0x10000
name|Character
operator|.
name|MIN_HIGH_SURROGATE
argument_list|,
name|Character
operator|.
name|MIN_LOW_SURROGATE
argument_list|)
expr_stmt|;
name|assertUnicodeEscaping
argument_list|(
name|e
argument_list|,
literal|"&#100000;"
argument_list|,
literal|'\uD821'
argument_list|,
literal|'\uDEA0'
argument_list|)
expr_stmt|;
name|assertUnicodeEscaping
argument_list|(
name|e
argument_list|,
literal|"&#1114111;"
argument_list|,
comment|// U+10FFFF
name|Character
operator|.
name|MAX_HIGH_SURROGATE
argument_list|,
name|Character
operator|.
name|MAX_LOW_SURROGATE
argument_list|)
expr_stmt|;
block|}
DECL|method|testHtmlContentEscaper ()
specifier|public
name|void
name|testHtmlContentEscaper
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"xxx"
argument_list|,
name|HtmlEscapers
operator|.
name|htmlContentEscaper
argument_list|()
operator|.
name|escape
argument_list|(
literal|"xxx"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"&quot;test&quot;"
argument_list|,
name|HtmlEscapers
operator|.
name|htmlContentEscaper
argument_list|()
operator|.
name|escape
argument_list|(
literal|"\"test\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"&#39;test&#39;"
argument_list|,
name|HtmlEscapers
operator|.
name|htmlContentEscaper
argument_list|()
operator|.
name|escape
argument_list|(
literal|"\'test'"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test&amp; test&amp; test"
argument_list|,
name|HtmlEscapers
operator|.
name|htmlContentEscaper
argument_list|()
operator|.
name|escape
argument_list|(
literal|"test& test& test"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test&lt;&lt; 1"
argument_list|,
name|HtmlEscapers
operator|.
name|htmlContentEscaper
argument_list|()
operator|.
name|escape
argument_list|(
literal|"test<< 1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test&gt;&gt; 1"
argument_list|,
name|HtmlEscapers
operator|.
name|htmlContentEscaper
argument_list|()
operator|.
name|escape
argument_list|(
literal|"test>> 1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"&lt;tab&gt;"
argument_list|,
name|HtmlEscapers
operator|.
name|htmlContentEscaper
argument_list|()
operator|.
name|escape
argument_list|(
literal|"<tab>"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test simple escape of '&'.
name|assertEquals
argument_list|(
literal|"foo&amp;bar"
argument_list|,
name|HtmlEscapers
operator|.
name|htmlContentEscaper
argument_list|()
operator|.
name|escape
argument_list|(
literal|"foo&bar"
argument_list|)
argument_list|)
expr_stmt|;
comment|// If the string contains no escapes, it should return the arg.
comment|// Note: assert<b>Same</b> for this implementation.
name|String
name|s
init|=
literal|"blah blah farhvergnugen"
decl_stmt|;
name|assertSame
argument_list|(
name|s
argument_list|,
name|HtmlEscapers
operator|.
name|htmlContentEscaper
argument_list|()
operator|.
name|escape
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
comment|// Tests escapes at begin and end of string.
name|assertEquals
argument_list|(
literal|"&lt;p&gt;"
argument_list|,
name|HtmlEscapers
operator|.
name|htmlContentEscaper
argument_list|()
operator|.
name|escape
argument_list|(
literal|"<p>"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test all escapes.
name|assertEquals
argument_list|(
literal|"a&quot;b&lt;c&gt;d&amp;"
argument_list|,
name|HtmlEscapers
operator|.
name|htmlContentEscaper
argument_list|()
operator|.
name|escape
argument_list|(
literal|"a\"b<c>d&"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test two escapes in a row.
name|assertEquals
argument_list|(
literal|"foo&amp;&amp;bar"
argument_list|,
name|HtmlEscapers
operator|.
name|htmlContentEscaper
argument_list|()
operator|.
name|escape
argument_list|(
literal|"foo&&bar"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test many non-escaped characters.
name|s
operator|=
literal|"!@#$%^*()_+=-/?\\|]}[{,.;:"
operator|+
literal|"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
operator|+
literal|"1234567890"
expr_stmt|;
name|assertSame
argument_list|(
name|s
argument_list|,
name|HtmlEscapers
operator|.
name|htmlContentEscaper
argument_list|()
operator|.
name|escape
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

