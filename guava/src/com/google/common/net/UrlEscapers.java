begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|Escaper
import|;
end_import

begin_comment
comment|/**  * {@code Escaper} instances suitable for strings to be included in particular sections of URLs.  *  *<p>If the resulting URLs are inserted into an HTML or XML document, they will require additional  * escaping with {@link com.google.common.html.HtmlEscapers} or  * {@link com.google.common.xml.XmlEscapers}.  *  *  * @author David Beaumont  * @author Chris Povirk  * @since 15.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|UrlEscapers
specifier|public
specifier|final
class|class
name|UrlEscapers
block|{
DECL|method|UrlEscapers ()
specifier|private
name|UrlEscapers
parameter_list|()
block|{}
comment|// For each xxxEscaper() method, please add links to external reference pages
comment|// that are considered authoritative for the behavior of that escaper.
DECL|field|URL_FORM_PARAMETER_OTHER_SAFE_CHARS
specifier|static
specifier|final
name|String
name|URL_FORM_PARAMETER_OTHER_SAFE_CHARS
init|=
literal|"-_.*"
decl_stmt|;
DECL|field|URL_PATH_OTHER_SAFE_CHARS_LACKING_PLUS
specifier|static
specifier|final
name|String
name|URL_PATH_OTHER_SAFE_CHARS_LACKING_PLUS
init|=
literal|"-._~"
operator|+
comment|// Unreserved characters.
literal|"!$'()*,;&="
operator|+
comment|// The subdelim characters (excluding '+').
literal|"@:"
decl_stmt|;
comment|// The gendelim characters permitted in paths.
comment|/**    * Returns an {@link Escaper} instance that escapes strings so they can be safely included in    *<a href="https://goo.gl/MplK6I">URL form parameter names and values</a>. Escaping is performed    * with the UTF-8 character encoding. The caller is responsible for    *<a href="https://goo.gl/9EfkM1">replacing any unpaired carriage return or line feed characters    * with a CR+LF pair</a> on any non-file inputs before escaping them with this escaper.    *    *<p>When escaping a String, the following rules apply:    *<ul>    *<li>The alphanumeric characters "a" through "z", "A" through "Z" and "0" through "9" remain the    *     same.    *<li>The special characters ".", "-", "*", and "_" remain the same.    *<li>The space character " " is converted into a plus sign "+".    *<li>All other characters are converted into one or more bytes using UTF-8 encoding and each    *     byte is then represented by the 3-character string "%XY", where "XY" is the two-digit,    *     uppercase, hexadecimal representation of the byte value.    *</ul>    *    *<p>This escaper is suitable for escaping parameter names and values even when    *<a href="https://goo.gl/utn6M">using the non-standard semicolon</a>, rather than the ampersand,    * as a parameter delimiter. Nevertheless, we recommend using the ampersand unless you must    * interoperate with systems that require semicolons.    *    *<p><b>Note:</b> Unlike other escapers, URL escapers produce<a    * href="https://url.spec.whatwg.org/#percent-encode">uppercase</a> hexadecimal sequences.    *    */
DECL|method|urlFormParameterEscaper ()
specifier|public
specifier|static
name|Escaper
name|urlFormParameterEscaper
parameter_list|()
block|{
return|return
name|URL_FORM_PARAMETER_ESCAPER
return|;
block|}
DECL|field|URL_FORM_PARAMETER_ESCAPER
specifier|private
specifier|static
specifier|final
name|Escaper
name|URL_FORM_PARAMETER_ESCAPER
init|=
operator|new
name|PercentEscaper
argument_list|(
name|URL_FORM_PARAMETER_OTHER_SAFE_CHARS
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|/**    * Returns an {@link Escaper} instance that escapes strings so they can be safely included in    *<a href="https://goo.gl/m2MIf0">URL path segments</a>. The returned escaper escapes all    * non-ASCII characters, even though<a href="https://goo.gl/e7E0In">many of these are accepted in    * modern URLs</a>. (<a href="https://goo.gl/jfVxXW">If the escaper were to leave these characters    * unescaped, they would be escaped by the consumer at parse time, anyway.</a>) Additionally, the    * escaper escapes the slash character ("/"). While slashes are acceptable in URL paths, they are    * considered by the specification to be separators between "path segments." This implies that, if    * you wish for your path to contain slashes, you must escape each segment separately and then    * join them.    *    *<p>When escaping a String, the following rules apply:    *<ul>    *<li>The alphanumeric characters "a" through "z", "A" through "Z" and "0" through "9" remain the    *     same.    *<li>The unreserved characters ".", "-", "~", and "_" remain the same.    *<li>The general delimiters "@" and ":" remain the same.    *<li>The subdelimiters "!", "$", "&amp;", "'", "(", ")", "*", "+", ",", ";", and "=" remain the    *     same.    *<li>The space character " " is converted into %20.    *<li>All other characters are converted into one or more bytes using UTF-8 encoding and each    *     byte is then represented by the 3-character string "%XY", where "XY" is the two-digit,    *     uppercase, hexadecimal representation of the byte value.    *</ul>    *    *<p><b>Note:</b> Unlike other escapers, URL escapers produce<a    * href="https://url.spec.whatwg.org/#percent-encode">uppercase</a> hexadecimal sequences.    */
DECL|method|urlPathSegmentEscaper ()
specifier|public
specifier|static
name|Escaper
name|urlPathSegmentEscaper
parameter_list|()
block|{
return|return
name|URL_PATH_SEGMENT_ESCAPER
return|;
block|}
DECL|field|URL_PATH_SEGMENT_ESCAPER
specifier|private
specifier|static
specifier|final
name|Escaper
name|URL_PATH_SEGMENT_ESCAPER
init|=
operator|new
name|PercentEscaper
argument_list|(
name|URL_PATH_OTHER_SAFE_CHARS_LACKING_PLUS
operator|+
literal|"+"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|/**    * Returns an {@link Escaper} instance that escapes strings so they can be safely included in a    *<a href="https://goo.gl/xXEq4p">URL fragment</a>. The returned escaper escapes all non-ASCII    * characters, even though<a href="https://goo.gl/e7E0In">many of these are accepted in modern    * URLs</a>.    *    *<p>When escaping a String, the following rules apply:    *<ul>    *<li>The alphanumeric characters "a" through "z", "A" through "Z" and "0" through "9" remain the    *     same.    *<li>The unreserved characters ".", "-", "~", and "_" remain the same.    *<li>The general delimiters "@" and ":" remain the same.    *<li>The subdelimiters "!", "$", "&amp;", "'", "(", ")", "*", "+", ",", ";", and "=" remain the    *     same.    *<li>The space character " " is converted into %20.    *<li>Fragments allow unescaped "/" and "?", so they remain the same.    *<li>All other characters are converted into one or more bytes using UTF-8 encoding and each    *     byte is then represented by the 3-character string "%XY", where "XY" is the two-digit,    *     uppercase, hexadecimal representation of the byte value.    *</ul>    *    *<p><b>Note:</b> Unlike other escapers, URL escapers produce<a    * href="https://url.spec.whatwg.org/#percent-encode">uppercase</a> hexadecimal sequences.    */
DECL|method|urlFragmentEscaper ()
specifier|public
specifier|static
name|Escaper
name|urlFragmentEscaper
parameter_list|()
block|{
return|return
name|URL_FRAGMENT_ESCAPER
return|;
block|}
DECL|field|URL_FRAGMENT_ESCAPER
specifier|private
specifier|static
specifier|final
name|Escaper
name|URL_FRAGMENT_ESCAPER
init|=
operator|new
name|PercentEscaper
argument_list|(
name|URL_PATH_OTHER_SAFE_CHARS_LACKING_PLUS
operator|+
literal|"+/?"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
block|}
end_class

end_unit

