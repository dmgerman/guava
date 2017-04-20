begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.net
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|net
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
name|assertEscaping
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
name|net
operator|.
name|UrlEscapers
operator|.
name|urlFormParameterEscaper
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
name|net
operator|.
name|UrlEscapers
operator|.
name|urlFragmentEscaper
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
name|net
operator|.
name|UrlEscapers
operator|.
name|urlPathSegmentEscaper
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
comment|/**  * Tests for the {@link UrlEscapers} class.  *  * @author David Beaumont  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|UrlEscapersTest
specifier|public
class|class
name|UrlEscapersTest
extends|extends
name|TestCase
block|{
comment|/**    * Helper to assert common expected behaviour of uri escapers. You should call    * assertBasicUrlEscaper() unless the escaper explicitly does not escape '%'.    */
DECL|method|assertBasicUrlEscaperExceptPercent (UnicodeEscaper e)
specifier|static
name|void
name|assertBasicUrlEscaperExceptPercent
parameter_list|(
name|UnicodeEscaper
name|e
parameter_list|)
block|{
comment|// URL escapers should throw null pointer exceptions for null input
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
literal|"Escaping null string should throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|x
parameter_list|)
block|{
comment|// pass
block|}
comment|// All URL escapers should leave 0-9, A-Z, a-z unescaped
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
literal|'z'
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
literal|'Z'
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'0'
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'9'
argument_list|)
expr_stmt|;
comment|// Unreserved characters used in java.net.URLEncoder
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'-'
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'_'
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'.'
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'*'
argument_list|)
expr_stmt|;
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"%00"
argument_list|,
literal|'\u0000'
argument_list|)
expr_stmt|;
comment|// nul
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"%7F"
argument_list|,
literal|'\u007f'
argument_list|)
expr_stmt|;
comment|// del
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"%C2%80"
argument_list|,
literal|'\u0080'
argument_list|)
expr_stmt|;
comment|// xx-00010,x-000000
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"%DF%BF"
argument_list|,
literal|'\u07ff'
argument_list|)
expr_stmt|;
comment|// xx-11111,x-111111
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"%E0%A0%80"
argument_list|,
literal|'\u0800'
argument_list|)
expr_stmt|;
comment|// xxx-0000,x-100000,x-00,0000
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"%EF%BF%BF"
argument_list|,
literal|'\uffff'
argument_list|)
expr_stmt|;
comment|// xxx-1111,x-111111,x-11,1111
name|assertUnicodeEscaping
argument_list|(
name|e
argument_list|,
literal|"%F0%90%80%80"
argument_list|,
literal|'\uD800'
argument_list|,
literal|'\uDC00'
argument_list|)
expr_stmt|;
name|assertUnicodeEscaping
argument_list|(
name|e
argument_list|,
literal|"%F4%8F%BF%BF"
argument_list|,
literal|'\uDBFF'
argument_list|,
literal|'\uDFFF'
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|e
operator|.
name|escape
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"safestring"
argument_list|,
name|e
operator|.
name|escape
argument_list|(
literal|"safestring"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"embedded%00null"
argument_list|,
name|e
operator|.
name|escape
argument_list|(
literal|"embedded\0null"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"max%EF%BF%BFchar"
argument_list|,
name|e
operator|.
name|escape
argument_list|(
literal|"max\uffffchar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Helper to assert common expected behaviour of uri escapers.
DECL|method|assertBasicUrlEscaper (UnicodeEscaper e)
specifier|static
name|void
name|assertBasicUrlEscaper
parameter_list|(
name|UnicodeEscaper
name|e
parameter_list|)
block|{
name|assertBasicUrlEscaperExceptPercent
argument_list|(
name|e
argument_list|)
expr_stmt|;
comment|// The escape character must always be escaped
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"%25"
argument_list|,
literal|'%'
argument_list|)
expr_stmt|;
block|}
DECL|method|testUrlFormParameterEscaper ()
specifier|public
name|void
name|testUrlFormParameterEscaper
parameter_list|()
block|{
name|UnicodeEscaper
name|e
init|=
operator|(
name|UnicodeEscaper
operator|)
name|urlFormParameterEscaper
argument_list|()
decl_stmt|;
comment|// Verify that these are the same escaper (as documented)
name|assertSame
argument_list|(
name|e
argument_list|,
name|urlFormParameterEscaper
argument_list|()
argument_list|)
expr_stmt|;
name|assertBasicUrlEscaper
argument_list|(
name|e
argument_list|)
expr_stmt|;
comment|/*      * Specified as safe by RFC 2396 but not by java.net.URLEncoder. These tests will start failing      * when the escaper is made compliant with RFC 2396, but that's a good thing (just change them      * to assertUnescaped).      */
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"%21"
argument_list|,
literal|'!'
argument_list|)
expr_stmt|;
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"%28"
argument_list|,
literal|'('
argument_list|)
expr_stmt|;
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"%29"
argument_list|,
literal|')'
argument_list|)
expr_stmt|;
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"%7E"
argument_list|,
literal|'~'
argument_list|)
expr_stmt|;
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"%27"
argument_list|,
literal|'\''
argument_list|)
expr_stmt|;
comment|// Plus for spaces
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"+"
argument_list|,
literal|' '
argument_list|)
expr_stmt|;
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"%2B"
argument_list|,
literal|'+'
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"safe+with+spaces"
argument_list|,
name|e
operator|.
name|escape
argument_list|(
literal|"safe with spaces"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo%40bar.com"
argument_list|,
name|e
operator|.
name|escape
argument_list|(
literal|"foo@bar.com"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUrlPathSegmentEscaper ()
specifier|public
name|void
name|testUrlPathSegmentEscaper
parameter_list|()
block|{
name|UnicodeEscaper
name|e
init|=
operator|(
name|UnicodeEscaper
operator|)
name|urlPathSegmentEscaper
argument_list|()
decl_stmt|;
name|assertPathEscaper
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'+'
argument_list|)
expr_stmt|;
block|}
DECL|method|assertPathEscaper (UnicodeEscaper e)
specifier|static
name|void
name|assertPathEscaper
parameter_list|(
name|UnicodeEscaper
name|e
parameter_list|)
block|{
name|assertBasicUrlEscaper
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'!'
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'\''
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'('
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|')'
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'~'
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|':'
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'@'
argument_list|)
expr_stmt|;
comment|// Don't use plus for spaces
name|assertEscaping
argument_list|(
name|e
argument_list|,
literal|"%20"
argument_list|,
literal|' '
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"safe%20with%20spaces"
argument_list|,
name|e
operator|.
name|escape
argument_list|(
literal|"safe with spaces"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo@bar.com"
argument_list|,
name|e
operator|.
name|escape
argument_list|(
literal|"foo@bar.com"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUrlFragmentEscaper ()
specifier|public
name|void
name|testUrlFragmentEscaper
parameter_list|()
block|{
name|UnicodeEscaper
name|e
init|=
operator|(
name|UnicodeEscaper
operator|)
name|urlFragmentEscaper
argument_list|()
decl_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'+'
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
name|assertUnescaped
argument_list|(
name|e
argument_list|,
literal|'?'
argument_list|)
expr_stmt|;
name|assertPathEscaper
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

