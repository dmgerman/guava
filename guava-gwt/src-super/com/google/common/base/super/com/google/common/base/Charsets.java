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
comment|/*    * Please do not add new Charset references to this class, unless those character encodings are    * part of the set required to be supported by all Java platform implementations! Any Charsets    * initialized here may cause unexpected delays when this class is loaded. See the Charset    * Javadocs for the list of built-in character encodings.    */
block|}
end_class

end_unit

