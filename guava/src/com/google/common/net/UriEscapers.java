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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|Beta
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
name|Escaper
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
name|net
operator|.
name|PercentEscaper
import|;
end_import

begin_comment
comment|/**  * A factory for {@code Escaper} instances suitable for escaping strings so they  * can be safely included in URIs or particular sections of URIs.  *  *<p>If the resulting URIs are inserted into an HTML or XML document, they will  * require additional escaping with {@link com.google.common.html.HtmlEscapers}  * or {@link com.google.common.xml.XmlEscapers}.  *  *<p>For more information on URI escaping, see  *<a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>.  *  * @author David Beaumont  * @since 11.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|UriEscapers
specifier|public
specifier|final
class|class
name|UriEscapers
block|{
DECL|method|UriEscapers ()
specifier|private
name|UriEscapers
parameter_list|()
block|{ }
comment|// For each xxxEscaper() method, please add links to external reference pages
comment|// that are considered authoritative for the behavior of that escaper.
comment|// TODO(user): Remove the 'plusForSpace' boolean in favor of an enum.
comment|// As 'plusForSpace' is mostly not the right thing to use, we should consider
comment|// having an enum to give it an explicit name and associated documentation.
comment|/**    * Returns an {@link Escaper} instance that escapes Java chars so they can be    * safely included in URIs. For details on escaping URIs, see section 2.4 of    *<a href="http://www.ietf.org/rfc/rfc2396.txt">RFC 2396</a>.    *    *<p>When encoding a String, the following rules apply:    *<ul>    *<li>The alphanumeric characters "a" through "z", "A" through "Z" and "0"    *     through "9" remain the same.    *<li>The special characters ".", "-", "*", and "_" remain the same.    *<li>The space character " " is converted into a plus sign "+".    *<li>All other characters are converted into one or more bytes using UTF-8    *     encoding and each byte is then represented by the 3-character string    *     "%XY", where "XY" is the two-digit, uppercase, hexadecimal    *     representation of the byte value.    *<ul>    *    *<p><b>Note</b>: Unlike other escapers, URI escapers produce uppercase    * hexadecimal sequences. From<a href="http://www.ietf.org/rfc/rfc3986.txt">    * RFC 3986</a>:<br>    *<i>"URI producers and normalizers should use uppercase hexadecimal digits    * for all percent-encodings."</i>    *    *    *<p>This method is equivalent to {@code uriEscaper(true)}.    */
DECL|method|uriEscaper ()
specifier|public
specifier|static
name|Escaper
name|uriEscaper
parameter_list|()
block|{
return|return
name|uriEscaper
argument_list|(
literal|true
argument_list|)
return|;
block|}
comment|/**    * Returns a {@link Escaper} instance that escapes Java characters so they can    * be safely included in URIs. For details on escaping URIs, see section 2.4    * of<a href="http://www.ietf.org/rfc/rfc2396.txt">RFC 2396</a>.    *    *<p>When encoding a String, the following rules apply:    *<ul>    *<li>The alphanumeric characters "a" through "z", "A" through "Z" and "0"    *     through "9" remain the same.    *<li>The special characters ".", "-", "*", and "_" remain the same.    *<li>If {@code plusForSpace} was specified, the space character " " is    *     converted into a plus sign "+". Otherwise it is converted into "%20".    *<li>All other characters are converted into one or more bytes using UTF-8    *     encoding and each byte is then represented by the 3-character string    *     "%XY", where "XY" is the two-digit, uppercase, hexadecimal    *     representation of the byte value.    *</ul>    *    *<p>The need to use {@code plusForSpace} is limited to a small set of use    * cases relating to URL encoded forms and should be avoided otherwise. For    * more information on when it may be appropriate to use {@code plusForSpace},    * see<a href="http://www.w3.org/TR/html401/interact/forms.html#h-17.13.4.1">    * "Forms in HTML documents".</a>    *    *<p><b>Note</b>: Unlike other escapers, URI escapers produce uppercase    * hexadecimal sequences. From<a href="http://www.ietf.org/rfc/rfc3986.txt">    * RFC 3986</a>:<br>    *<i>"URI producers and normalizers should use uppercase hexadecimal digits    * for all percent-encodings."</i>    *    * @param plusForSpace if {@code true} space is escaped to {@code +} otherwise    *        it is escaped to {@code %20}. Although common, the escaping of    *        spaces as plus signs has a very ambiguous status in the relevant    *        specifications. You should prefer {@code %20} unless you are doing    *        exact character-by-character comparisons of URLs and backwards    *        compatibility requires you to use plus signs.    *    * @see #uriEscaper()    */
DECL|method|uriEscaper (boolean plusForSpace)
specifier|public
specifier|static
name|Escaper
name|uriEscaper
parameter_list|(
name|boolean
name|plusForSpace
parameter_list|)
block|{
return|return
name|plusForSpace
condition|?
name|URI_ESCAPER
else|:
name|URI_ESCAPER_NO_PLUS
return|;
block|}
comment|/**    * A string of safe characters that mimics the behavior of    * {@link java.net.URLEncoder}.    */
DECL|field|URI_SAFECHARS_JAVA
specifier|public
specifier|static
specifier|final
name|String
name|URI_SAFECHARS_JAVA
init|=
literal|"-_.*"
decl_stmt|;
DECL|field|URI_ESCAPER
specifier|private
specifier|static
specifier|final
name|Escaper
name|URI_ESCAPER
init|=
operator|new
name|PercentEscaper
argument_list|(
name|URI_SAFECHARS_JAVA
argument_list|,
literal|true
argument_list|)
decl_stmt|;
DECL|field|URI_ESCAPER_NO_PLUS
specifier|private
specifier|static
specifier|final
name|Escaper
name|URI_ESCAPER_NO_PLUS
init|=
operator|new
name|PercentEscaper
argument_list|(
name|URI_SAFECHARS_JAVA
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|/**    * Returns an {@link Escaper} instance that escapes Java chars so they can be    * safely included in URI path segments. For details on escaping URIs, see    * section 2.4 of<a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>.    *    *<p>When encoding a String, the following rules apply:    *<ul>    *<li>The alphanumeric characters "a" through "z", "A" through "Z" and "0"    *     through "9" remain the same.    *<li>The unreserved characters ".", "-", "~", and "_" remain the same.    *<li>The general delimiters "@" and ":" remain the same.    *<li>The subdelimiters "!", "$", "&amp;", "'", "(", ")", "*", ",", ";",    *     and "=" remain the same.    *<li>The space character " " is converted into %20.    *<li>All other characters are converted into one or more bytes using UTF-8    *     encoding and each byte is then represented by the 3-character string    *     "%XY", where "XY" is the two-digit, uppercase, hexadecimal    *     representation of the byte value.    *</ul>    *    *<p><b>Note</b>: Unlike other escapers, URI escapers produce uppercase    * hexadecimal sequences. From<a href="http://www.ietf.org/rfc/rfc3986.txt">    * RFC 3986</a>:<br>    *<i>"URI producers and normalizers should use uppercase hexadecimal digits    * for all percent-encodings."</i>    *    *<p>This method differs from {@link #uriPathEscaperRfcCompliant} by    * escaping "+" characters into "%2D".    */
DECL|method|uriPathEscaper ()
specifier|public
specifier|static
name|Escaper
name|uriPathEscaper
parameter_list|()
block|{
return|return
name|URI_PATH_ESCAPER
return|;
block|}
comment|/**    * Returns an {@link Escaper} instance that escapes Java chars so they can be    * safely included in URI path segments. For details on escaping URIs, see    * section 2.4 of<a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>.    *    *<p>When encoding a String, the following rules apply:    *<ul>    *<li>The alphanumeric characters "a" through "z", "A" through "Z" and "0"    *     through "9" remain the same.    *<li>The unreserved characters ".", "-", "~", and "_" remain the same.    *<li>The general delimiters "@" and ":" remain the same.    *<li>The subdelimiters "!", "$", "&amp;", "'", "(", ")", "*", "+", ",", ";",    *     and "=" remain the same.    *<li>If {@code escapePlus} was specified, the plus character "+" is    *     converted into "%2B", otherwise it remains the same.    *<li>The space character " " is converted into %20.    *<li>All other characters are converted into one or more bytes using UTF-8    *     encoding and each byte is then represented by the 3-character string    *     "%XY", where "XY" is the two-digit, uppercase, hexadecimal    *     representation of the byte value.    *</ul>    *    *<p><b>Note</b>: Unlike other escapers, URI escapers produce uppercase    * hexadecimal sequences. From<a href="http://www.ietf.org/rfc/rfc3986.txt">    * RFC 3986</a>:<br>    *<i>"URI producers and normalizers should use uppercase hexadecimal digits    * for all percent-encodings."</i>    *    *<p>This method differs from {@link #uriPathEscaper} by not escaping    * "+" characters, treating them the same as the other sub-delimiters    * described by RFC 3986. Note that the version that does escape "+" characters    * is in common use and therefore the use of this method should be done    * carefully to avoid compatibility problems, especially when comparing URLs.    */
DECL|method|uriPathEscaperRfcCompliant ()
specifier|public
specifier|static
name|Escaper
name|uriPathEscaperRfcCompliant
parameter_list|()
block|{
return|return
name|URI_PATH_ESCAPER_RFC_COMPLIANT
return|;
block|}
comment|/**    * The set of path characters as defined by RFC 3986 excluding the plus    * character {@code '+'}. This set of characters is used as the basis for    * most of the 'safe' strings for URIs.    */
DECL|field|URI_SAFECHARS_PATH
specifier|public
specifier|static
specifier|final
name|String
name|URI_SAFECHARS_PATH
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
DECL|field|URI_PATH_ESCAPER
specifier|private
specifier|static
specifier|final
name|Escaper
name|URI_PATH_ESCAPER
init|=
operator|new
name|PercentEscaper
argument_list|(
name|URI_SAFECHARS_PATH
argument_list|,
literal|false
argument_list|)
decl_stmt|;
DECL|field|URI_PATH_ESCAPER_RFC_COMPLIANT
specifier|private
specifier|static
specifier|final
name|Escaper
name|URI_PATH_ESCAPER_RFC_COMPLIANT
init|=
operator|new
name|PercentEscaper
argument_list|(
name|URI_SAFECHARS_PATH
operator|+
literal|"+"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|/**    * Returns an {@link Escaper} instance that escapes Java chars so they can be    * safely included in URI query string segments. When the query string    * consists of a sequence of name=value pairs separated by&amp;, the names    * and values should be individually encoded. If you escape an entire query    * string in one pass with this escaper, then the "=" and "&amp;" characters    * used as separators will also be escaped.    *    *<p>This escaper is also suitable for escaping fragment identifiers.    *    *<p>For details on escaping URIs, see    * section 2.4 of<a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>.    *    *<p>When encoding a String, the following rules apply:    *<ul>    *<li>The alphanumeric characters "a" through "z", "A" through "Z" and "0"    *     through "9" remain the same.    *<li>The unreserved characters ".", "-", "~", and "_" remain the same.    *<li>The general delimiters "@" and ":" remain the same.    *<li>The path delimiters "/" and "?" remain the same.    *<li>The subdelimiters "!", "$", "'", "(", ")", "*", ",", and ";",    *     remain the same.    *<li>The space character " " is converted into %20.    *<li>The equals sign "=" is converted into %3D.    *<li>The ampersand "&amp;" is converted into %26.    *<li>All other characters are converted into one or more bytes using UTF-8    *     encoding and each byte is then represented by the 3-character string    *     "%XY", where "XY" is the two-digit, uppercase, hexadecimal    *     representation of the byte value.    *</ul>    *    *<p>The need to use {@code plusForSpace} is limited to a small set of use    * cases relating to URL encoded forms and should be avoided otherwise. For    * more information on when it may be appropriate to use {@code plusForSpace},    * see<a href="http://www.w3.org/TR/html401/interact/forms.html#h-17.13.4.1">    * "Forms in HTML documents".</a>    *    *<p><b>Note</b>: Unlike other escapers, URI escapers produce uppercase    * hexadecimal sequences. From<a href="http://www.ietf.org/rfc/rfc3986.txt">    * RFC 3986</a>:<br>    *<i>"URI producers and normalizers should use uppercase hexadecimal digits    * for all percent-encodings."</i>    *    * @param plusForSpace if {@code true} space is escaped to {@code +} otherwise    *        it is escaped to {@code %20}. Although common, the escaping of    *        spaces as plus signs has a very ambiguous status in the relevant    *        specifications. You should prefer {@code %20} unless you are doing    *        exact character-by-character comparisons of URLs and backwards    *        compatibility requires you to use plus signs.    */
DECL|method|uriQueryStringEscaper (boolean plusForSpace)
specifier|public
specifier|static
name|Escaper
name|uriQueryStringEscaper
parameter_list|(
name|boolean
name|plusForSpace
parameter_list|)
block|{
return|return
name|plusForSpace
condition|?
name|QUERY_STRING_ESCAPER
else|:
name|QUERY_STRING_ESCAPER_NO_PLUS
return|;
block|}
comment|/**    * A string of characters that do not need to be escaped when used in the    * key/value arguments that make up the query parameters of an HTTP URL. Note    * that some of these characters do need to be escaped when used in other    * parts of the URI.    */
DECL|field|URI_SAFECHARS_QUERY_STRING
specifier|public
specifier|static
specifier|final
name|String
name|URI_SAFECHARS_QUERY_STRING
init|=
literal|"-._~"
operator|+
comment|// Unreserved characters
literal|"!$'()*,;"
operator|+
comment|// The subdelim characters (excluding '+', '&' and '=').
literal|"@:/?"
decl_stmt|;
comment|// The gendelim characters permitted in query parameters.
DECL|field|QUERY_STRING_ESCAPER
specifier|private
specifier|static
specifier|final
name|Escaper
name|QUERY_STRING_ESCAPER
init|=
operator|new
name|PercentEscaper
argument_list|(
name|URI_SAFECHARS_QUERY_STRING
argument_list|,
literal|true
argument_list|)
decl_stmt|;
DECL|field|QUERY_STRING_ESCAPER_NO_PLUS
specifier|private
specifier|static
specifier|final
name|Escaper
name|QUERY_STRING_ESCAPER_NO_PLUS
init|=
operator|new
name|PercentEscaper
argument_list|(
name|URI_SAFECHARS_QUERY_STRING
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|/**    * Returns an {@link Escaper} instance which ensures that any invalid Unicode    * code points in a URI are correctly percent escaped. This escaper is    * suitable for applying additional escaping to partially escaped URIs and    *<em>will not</em> correctly escape data during the creation of a URI.    *    *<p>This escaper is special in that it<em>does not</em> escape the    * {@code %} character and as such does not have a well defined inverse.    * It assumes that a minimal amount of percent-escaping has already been    * applied to the input, at least ensuring that {@code %} itself is escaped.    *    *<p>For details on escaping URIs, see    * section 2.4 of<a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>.    *    *<p>When encoding a String, the following rules apply:    *<ul>    *<li>The alphanumeric characters "a" through "z", "A" through "Z" and "0"    *     through "9" remain the same.    *<li>The unreserved characters ".", "-", "~", and "_" remain the same.    *<li>The general delimiters ":", "/", "?", "#", "[", "]" and "@" remain the    *     same.    *<li>The subdelimiters "!", "$", "&", "'", "(", ")", "*", "+", ",", ";", and    *     "=" remain the same.    *<li><em>The percent character "%" remains the same.</em>    *<li>All other characters are converted into one or more bytes using UTF-8    *     encoding and each byte is then represented by the 3-character string    *     "%XY", where "XY" is the two-digit, uppercase, hexadecimal    *     representation of the byte value.    *</ul>    *    *<p>This escaper is useful in cases where we have a partially escaped (but    * not ambiguous) URI such as:    *<pre>{@code http://example.com/foo|bar%26baz}</pre>    * If we re-escape the string representation of the complete URI we get:    *<pre>{@code http://example.com/foo%7Cbar%26baz}</pre>    *    *<p>Note that this escaper only canonicalizes with respect to percent    * escaping and in general<em>will not</em> make the string representations    * of URIs comparable in a meaningful way.    *    *<p>This escaper is idempotent and escaping an already validly escaped URI    * will have no effect.    */
DECL|method|canonicalizingEscaper ()
specifier|public
specifier|static
name|Escaper
name|canonicalizingEscaper
parameter_list|()
block|{
return|return
name|CANONICALIZING_ESCAPER
return|;
block|}
DECL|field|ALL_VALID_URI_CHARS
specifier|private
specifier|static
specifier|final
name|String
name|ALL_VALID_URI_CHARS
init|=
literal|"-._~"
operator|+
comment|// Unreserved characters.
literal|":/?#[]@"
operator|+
comment|// Gendelim chars.
literal|"!$&'()*+,;="
operator|+
comment|// Subdelim chars.
literal|"%"
decl_stmt|;
comment|// The percent character itself!
DECL|field|CANONICALIZING_ESCAPER
specifier|private
specifier|static
specifier|final
name|Escaper
name|CANONICALIZING_ESCAPER
init|=
operator|new
name|PercentEscaper
argument_list|(
name|ALL_VALID_URI_CHARS
argument_list|,
literal|false
argument_list|)
decl_stmt|;
block|}
end_class

end_unit

