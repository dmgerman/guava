begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_comment
comment|/**  * Contains constant definitions for the six standard {@link Charset} instances, which are  * guaranteed to be supported by all Java platform implementations.  *  *<p>Assuming you're free to choose, note that<b>{@link #UTF_8} is widely preferred</b>.  *  *<p>See the Guava User Guide article on<a  * href="https://github.com/google/guava/wiki/StringsExplained#charsets">  * {@code Charsets}</a>.  *  * @author Mike Bostock  * @since 1.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|Charsets
specifier|public
specifier|final
class|class
name|Charsets
block|{
DECL|method|Charsets ()
specifier|private
name|Charsets
parameter_list|()
block|{}
comment|/**    * US-ASCII: seven-bit ASCII, the Basic Latin block of the Unicode character set (ISO646-US).    *    *<p><b>Note for Java 7 and later:</b> this constant should be treated as deprecated; use    * {@link java.nio.charset.StandardCharsets#US_ASCII} instead.    *    */
annotation|@
name|GwtIncompatible
comment|// Non-UTF-8 Charset
DECL|field|US_ASCII
specifier|public
specifier|static
specifier|final
name|Charset
name|US_ASCII
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"US-ASCII"
argument_list|)
decl_stmt|;
comment|/**    * ISO-8859-1: ISO Latin Alphabet Number 1 (ISO-LATIN-1).    *    *<p><b>Note for Java 7 and later:</b> this constant should be treated as deprecated; use    * {@link java.nio.charset.StandardCharsets#ISO_8859_1} instead.    *    */
annotation|@
name|GwtIncompatible
comment|// Non-UTF-8 Charset
DECL|field|ISO_8859_1
specifier|public
specifier|static
specifier|final
name|Charset
name|ISO_8859_1
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"ISO-8859-1"
argument_list|)
decl_stmt|;
comment|/**    * UTF-8: eight-bit UCS Transformation Format.    *    *<p><b>Note for Java 7 and later:</b> this constant should be treated as deprecated; use    * {@link java.nio.charset.StandardCharsets#UTF_8} instead.    *    */
DECL|field|UTF_8
specifier|public
specifier|static
specifier|final
name|Charset
name|UTF_8
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
comment|/**    * UTF-16BE: sixteen-bit UCS Transformation Format, big-endian byte order.    *    *<p><b>Note for Java 7 and later:</b> this constant should be treated as deprecated; use    * {@link java.nio.charset.StandardCharsets#UTF_16BE} instead.    *    */
annotation|@
name|GwtIncompatible
comment|// Non-UTF-8 Charset
DECL|field|UTF_16BE
specifier|public
specifier|static
specifier|final
name|Charset
name|UTF_16BE
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-16BE"
argument_list|)
decl_stmt|;
comment|/**    * UTF-16LE: sixteen-bit UCS Transformation Format, little-endian byte order.    *    *<p><b>Note for Java 7 and later:</b> this constant should be treated as deprecated; use    * {@link java.nio.charset.StandardCharsets#UTF_16LE} instead.    *    */
annotation|@
name|GwtIncompatible
comment|// Non-UTF-8 Charset
DECL|field|UTF_16LE
specifier|public
specifier|static
specifier|final
name|Charset
name|UTF_16LE
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-16LE"
argument_list|)
decl_stmt|;
comment|/**    * UTF-16: sixteen-bit UCS Transformation Format, byte order identified by an optional byte-order    * mark.    *    *<p><b>Note for Java 7 and later:</b> this constant should be treated as deprecated; use    * {@link java.nio.charset.StandardCharsets#UTF_16} instead.    *    */
annotation|@
name|GwtIncompatible
comment|// Non-UTF-8 Charset
DECL|field|UTF_16
specifier|public
specifier|static
specifier|final
name|Charset
name|UTF_16
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-16"
argument_list|)
decl_stmt|;
comment|/*    * Please do not add new Charset references to this class, unless those character encodings are    * part of the set required to be supported by all Java platform implementations! Any Charsets    * initialized here may cause unexpected delays when this class is loaded. See the Charset    * Javadocs for the list of built-in character encodings.    */
block|}
end_class

end_unit

