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
name|escape
operator|.
name|Escapers
import|;
end_import

begin_comment
comment|/**  * {@code Escaper} instances suitable for strings to be included in HTML  * attribute values and<em>most</em> elements' text contents. When possible,  * avoid manual escaping by using templating systems and high-level APIs that  * provide autoescaping.  * One Google-authored templating system available for external use is<a  * href="https://developers.google.com/closure/templates/">Closure  * Templates</a>.  *  *<p>HTML escaping is particularly tricky: For example,<a  * href="http://goo.gl/5TgZb">some elements' text contents must not be HTML  * escaped</a>. As a result, it is impossible to escape an HTML document  * correctly without domain-specific knowledge beyond what {@code HtmlEscapers}  * provides. We strongly encourage the use of HTML templating systems.  *  * @author Sven Mawson  * @author David Beaumont  * @since 15.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|HtmlEscapers
specifier|public
specifier|final
class|class
name|HtmlEscapers
block|{
comment|/**    * Returns an {@link Escaper} instance that escapes HTML metacharacters as    * specified by<a href="http://www.w3.org/TR/html4/">HTML 4.01</a>. The    * resulting strings can be used both in attribute values and in<em>most</em>    * elements' text contents, provided that the HTML document's character    * encoding can encode any non-ASCII code points in the input (as UTF-8 and    * other Unicode encodings can).    *    *    *<p><b>Note:</b> This escaper only performs minimal escaping to make content    * structurally compatible with HTML. Specifically, it does not perform entity    * replacement (symbolic or numeric), so it does not replace non-ASCII code    * points with character references. This escaper escapes only the following    * five ASCII characters: {@code '"&<>}.    */
DECL|method|htmlEscaper ()
specifier|public
specifier|static
name|Escaper
name|htmlEscaper
parameter_list|()
block|{
return|return
name|HTML_ESCAPER
return|;
block|}
comment|// For each xxxEscaper() method, please add links to external reference pages
comment|// that are considered authoritative for the behavior of that escaper.
DECL|field|HTML_ESCAPER
specifier|private
specifier|static
specifier|final
name|Escaper
name|HTML_ESCAPER
init|=
name|Escapers
operator|.
name|builder
argument_list|()
operator|.
name|addEscape
argument_list|(
literal|'"'
argument_list|,
literal|"&quot;"
argument_list|)
comment|// Note: "&apos;" is not defined in HTML 4.01.
operator|.
name|addEscape
argument_list|(
literal|'\''
argument_list|,
literal|"&#39;"
argument_list|)
operator|.
name|addEscape
argument_list|(
literal|'&'
argument_list|,
literal|"&amp;"
argument_list|)
operator|.
name|addEscape
argument_list|(
literal|'<'
argument_list|,
literal|"&lt;"
argument_list|)
operator|.
name|addEscape
argument_list|(
literal|'>'
argument_list|,
literal|"&gt;"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
DECL|method|HtmlEscapers ()
specifier|private
name|HtmlEscapers
parameter_list|()
block|{}
block|}
end_class

end_unit

