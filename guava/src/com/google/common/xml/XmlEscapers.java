begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.xml
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|xml
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
name|CharEscaper
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
comment|/**  * A factory class for obtaining escaper instances suitable for working with  * XML.  *  *<p><b>Note</b>: Currently the escapers provided by this class do not escape  * any characters outside the ASCII character range. Unlike HTML escaping the  * XML escapers will not escape non-ASCII characters to their numeric entity  * replacements. These XML escapers provide the minimal level of escaping to  * ensure that the output can be safely included in a Unicode XML document.  *  *  *<p>For details on the behavior of the escapers in this class, see sections  *<a href="http://www.w3.org/TR/2008/REC-xml-20081126/#charsets">2.2</a> and  *<a href="http://www.w3.org/TR/2008/REC-xml-20081126/#syntax">2.4</a> of the  * XML specification.  *  * @author Alex Matevossian  * @author David Beaumont  * @since 11.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|XmlEscapers
specifier|public
class|class
name|XmlEscapers
block|{
DECL|method|XmlEscapers ()
specifier|private
name|XmlEscapers
parameter_list|()
block|{ }
DECL|field|MIN_ASCII_CONTROL_CHAR
specifier|private
specifier|static
specifier|final
name|char
name|MIN_ASCII_CONTROL_CHAR
init|=
literal|0x00
decl_stmt|;
DECL|field|MAX_ASCII_CONTROL_CHAR
specifier|private
specifier|static
specifier|final
name|char
name|MAX_ASCII_CONTROL_CHAR
init|=
literal|0x1F
decl_stmt|;
comment|// For each xxxEscaper() method, please add links to external reference pages
comment|// that are considered authoritative for the behavior of that escaper.
comment|// TODO(user): When this escaper strips \uFFFE& \uFFFF, add this doc.
comment|//<p>This escaper also silently removes non-whitespace control characters and
comment|// the character values {@code 0xFFFE} and {@code 0xFFFF} which are not
comment|// permitted in XML. For more detail see section
comment|//<a href="http://www.w3.org/TR/2008/REC-xml-20081126/#charsets">2.2</a> of
comment|// the XML specification.
comment|/**    * Returns an {@link Escaper} instance that escapes special characters in a    * string so it can safely be included in an XML document as element content.    * See section    *<a href="http://www.w3.org/TR/2008/REC-xml-20081126/#syntax">2.4</a> of the    * XML specification.    *    *<p><b>Note</b>: Double and single quotes are not escaped, so it is<b>not    * safe</b> to use this escaper to escape attribute values. Use    * {@link #xmlContentEscaper} if the output can appear in element content or    * {@link #xmlAttributeEscaper} in attribute values.    *    *<p>This escaper does not escape non-ASCII characters to their numeric    * character references (NCR). Any non-ASCII characters appearing in the input    * will be preserved in the output. Specifically "\r" (carriage return) is    * preserved in the output, which may result in it being silently converted to    * "\n" when the XML is parsed.    *    *<p>This escaper does not treat surrogate pairs specially and does not    * perform Unicode validation on its input.    */
DECL|method|xmlContentEscaper ()
specifier|public
specifier|static
name|CharEscaper
name|xmlContentEscaper
parameter_list|()
block|{
comment|// TODO(user): Update callers and return Escaper (remove cast below).
return|return
name|XML_CONTENT_ESCAPER
return|;
block|}
comment|/**    * Returns an {@link Escaper} instance that escapes special characters in a    * string so it can safely be included in XML document as an attribute value.    * See section    *<a href="http://www.w3.org/TR/2008/REC-xml-20081126/#AVNormalize">3.3.3</a>    * of the XML specification.    *    *<p>This escaper does not escape non-ASCII characters to their numeric    * character references (NCR). However, horizontal tab {@code '\t'}, line feed    * {@code '\n'} and carriage return {@code '\r'} are escaped to a    * corresponding NCR {@code "&#x9;"}, {@code "&#xA;"}, and {@code "&#xD;"}    * respectively. Any other non-ASCII characters appearing in the input will    * be preserved in the output.    *    *<p>This escaper does not treat surrogate pairs specially and does not    * perform Unicode validation on its input.    */
DECL|method|xmlAttributeEscaper ()
specifier|public
specifier|static
name|Escaper
name|xmlAttributeEscaper
parameter_list|()
block|{
return|return
name|XML_ATTRIBUTE_ESCAPER
return|;
block|}
DECL|field|XML_ESCAPER
specifier|private
specifier|static
specifier|final
name|CharEscaper
name|XML_ESCAPER
decl_stmt|;
DECL|field|XML_CONTENT_ESCAPER
specifier|private
specifier|static
specifier|final
name|CharEscaper
name|XML_CONTENT_ESCAPER
decl_stmt|;
DECL|field|XML_ATTRIBUTE_ESCAPER
specifier|private
specifier|static
specifier|final
name|Escaper
name|XML_ATTRIBUTE_ESCAPER
decl_stmt|;
static|static
block|{
name|Escapers
operator|.
name|Builder
name|builder
init|=
name|Escapers
operator|.
name|builder
argument_list|()
decl_stmt|;
comment|// The char values \uFFFE and \uFFFF are explicitly not allowed in XML
comment|// (Unicode code points above \uFFFF are represented via surrogate pairs
comment|// which means they are treated as pairs of safe characters).
comment|// TODO(user): When refactoring done change the \uFFFF below to \uFFFD
name|builder
operator|.
name|setSafeRange
argument_list|(
name|Character
operator|.
name|MIN_VALUE
argument_list|,
literal|'\uFFFF'
argument_list|)
expr_stmt|;
comment|// Unsafe characters are removed.
name|builder
operator|.
name|setUnsafeReplacement
argument_list|(
literal|""
argument_list|)
expr_stmt|;
comment|// Except for '\n', '\t' and '\r' we remove all ASCII control characters.
comment|// An alternative to this would be to make a map that simply replaces the
comment|// allowed ASCII whitespace characters with themselves and set the minimum
comment|// safe character to 0x20. However this would slow down the escaping of
comment|// simple strings that contain '\t','\n' or '\r'.
for|for
control|(
name|char
name|c
init|=
name|MIN_ASCII_CONTROL_CHAR
init|;
name|c
operator|<=
name|MAX_ASCII_CONTROL_CHAR
condition|;
name|c
operator|++
control|)
block|{
if|if
condition|(
name|c
operator|!=
literal|'\t'
operator|&&
name|c
operator|!=
literal|'\n'
operator|&&
name|c
operator|!=
literal|'\r'
condition|)
block|{
name|builder
operator|.
name|addEscape
argument_list|(
name|c
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Build the content escaper first and then add quote escaping for the
comment|// general escaper.
name|builder
operator|.
name|addEscape
argument_list|(
literal|'&'
argument_list|,
literal|"&amp;"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addEscape
argument_list|(
literal|'<'
argument_list|,
literal|"&lt;"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addEscape
argument_list|(
literal|'>'
argument_list|,
literal|"&gt;"
argument_list|)
expr_stmt|;
name|XML_CONTENT_ESCAPER
operator|=
operator|(
name|CharEscaper
operator|)
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
name|builder
operator|.
name|addEscape
argument_list|(
literal|'\''
argument_list|,
literal|"&apos;"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addEscape
argument_list|(
literal|'"'
argument_list|,
literal|"&quot;"
argument_list|)
expr_stmt|;
name|XML_ESCAPER
operator|=
operator|(
name|CharEscaper
operator|)
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
name|builder
operator|.
name|addEscape
argument_list|(
literal|'\t'
argument_list|,
literal|"&#x9;"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addEscape
argument_list|(
literal|'\n'
argument_list|,
literal|"&#xA;"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addEscape
argument_list|(
literal|'\r'
argument_list|,
literal|"&#xD;"
argument_list|)
expr_stmt|;
name|XML_ATTRIBUTE_ESCAPER
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

