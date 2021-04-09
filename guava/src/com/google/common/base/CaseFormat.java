begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2006 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
import|;
end_import

begin_comment
comment|/**  * Utility class for converting between various ASCII case formats. Behavior is undefined for  * non-ASCII input.  *  * @author Mike Bostock  * @since 1.0  */
end_comment

begin_enum
annotation|@
name|GwtCompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|enum|CaseFormat
specifier|public
enum|enum
name|CaseFormat
block|{
comment|/** Hyphenated variable naming convention, e.g., "lower-hyphen". */
DECL|enumConstant|LOWER_HYPHEN
DECL|enumConstant|CharMatcher.is
name|LOWER_HYPHEN
argument_list|(
name|CharMatcher
operator|.
name|is
argument_list|(
literal|'-'
argument_list|)
argument_list|,
literal|"-"
argument_list|)
block|{
annotation|@
name|Override
name|String
name|normalizeWord
parameter_list|(
name|String
name|word
parameter_list|)
block|{
return|return
name|Ascii
operator|.
name|toLowerCase
argument_list|(
name|word
argument_list|)
return|;
block|}
annotation|@
name|Override
name|String
name|convert
parameter_list|(
name|CaseFormat
name|format
parameter_list|,
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
name|format
operator|==
name|LOWER_UNDERSCORE
condition|)
block|{
return|return
name|s
operator|.
name|replace
argument_list|(
literal|'-'
argument_list|,
literal|'_'
argument_list|)
return|;
block|}
if|if
condition|(
name|format
operator|==
name|UPPER_UNDERSCORE
condition|)
block|{
return|return
name|Ascii
operator|.
name|toUpperCase
argument_list|(
name|s
operator|.
name|replace
argument_list|(
literal|'-'
argument_list|,
literal|'_'
argument_list|)
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|convert
argument_list|(
name|format
argument_list|,
name|s
argument_list|)
return|;
block|}
block|}
block|,
comment|/** C++ variable naming convention, e.g., "lower_underscore". */
DECL|enumConstant|LOWER_UNDERSCORE
DECL|enumConstant|CharMatcher.is
name|LOWER_UNDERSCORE
argument_list|(
name|CharMatcher
operator|.
name|is
argument_list|(
literal|'_'
argument_list|)
argument_list|,
literal|"_"
argument_list|)
block|{
annotation|@
name|Override
name|String
name|normalizeWord
parameter_list|(
name|String
name|word
parameter_list|)
block|{
return|return
name|Ascii
operator|.
name|toLowerCase
argument_list|(
name|word
argument_list|)
return|;
block|}
annotation|@
name|Override
name|String
name|convert
parameter_list|(
name|CaseFormat
name|format
parameter_list|,
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
name|format
operator|==
name|LOWER_HYPHEN
condition|)
block|{
return|return
name|s
operator|.
name|replace
argument_list|(
literal|'_'
argument_list|,
literal|'-'
argument_list|)
return|;
block|}
if|if
condition|(
name|format
operator|==
name|UPPER_UNDERSCORE
condition|)
block|{
return|return
name|Ascii
operator|.
name|toUpperCase
argument_list|(
name|s
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|convert
argument_list|(
name|format
argument_list|,
name|s
argument_list|)
return|;
block|}
block|}
block|,
comment|/** Java variable naming convention, e.g., "lowerCamel". */
DECL|enumConstant|LOWER_CAMEL
DECL|enumConstant|CharMatcher.inRange
name|LOWER_CAMEL
argument_list|(
name|CharMatcher
operator|.
name|inRange
argument_list|(
literal|'A'
argument_list|,
literal|'Z'
argument_list|)
argument_list|,
literal|""
argument_list|)
block|{
annotation|@
name|Override
name|String
name|normalizeWord
parameter_list|(
name|String
name|word
parameter_list|)
block|{
return|return
name|firstCharOnlyToUpper
argument_list|(
name|word
argument_list|)
return|;
block|}
annotation|@
name|Override
name|String
name|normalizeFirstWord
parameter_list|(
name|String
name|word
parameter_list|)
block|{
return|return
name|Ascii
operator|.
name|toLowerCase
argument_list|(
name|word
argument_list|)
return|;
block|}
block|}
block|,
comment|/** Java and C++ class naming convention, e.g., "UpperCamel". */
DECL|enumConstant|UPPER_CAMEL
DECL|enumConstant|CharMatcher.inRange
name|UPPER_CAMEL
argument_list|(
name|CharMatcher
operator|.
name|inRange
argument_list|(
literal|'A'
argument_list|,
literal|'Z'
argument_list|)
argument_list|,
literal|""
argument_list|)
block|{
annotation|@
name|Override
name|String
name|normalizeWord
parameter_list|(
name|String
name|word
parameter_list|)
block|{
return|return
name|firstCharOnlyToUpper
argument_list|(
name|word
argument_list|)
return|;
block|}
block|}
block|,
comment|/** Java and C++ constant naming convention, e.g., "UPPER_UNDERSCORE". */
DECL|enumConstant|UPPER_UNDERSCORE
DECL|enumConstant|CharMatcher.is
name|UPPER_UNDERSCORE
argument_list|(
name|CharMatcher
operator|.
name|is
argument_list|(
literal|'_'
argument_list|)
argument_list|,
literal|"_"
argument_list|)
block|{
annotation|@
name|Override
name|String
name|normalizeWord
parameter_list|(
name|String
name|word
parameter_list|)
block|{
return|return
name|Ascii
operator|.
name|toUpperCase
argument_list|(
name|word
argument_list|)
return|;
block|}
annotation|@
name|Override
name|String
name|convert
parameter_list|(
name|CaseFormat
name|format
parameter_list|,
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
name|format
operator|==
name|LOWER_HYPHEN
condition|)
block|{
return|return
name|Ascii
operator|.
name|toLowerCase
argument_list|(
name|s
operator|.
name|replace
argument_list|(
literal|'_'
argument_list|,
literal|'-'
argument_list|)
argument_list|)
return|;
block|}
if|if
condition|(
name|format
operator|==
name|LOWER_UNDERSCORE
condition|)
block|{
return|return
name|Ascii
operator|.
name|toLowerCase
argument_list|(
name|s
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|convert
argument_list|(
name|format
argument_list|,
name|s
argument_list|)
return|;
block|}
block|}
block|;
DECL|field|wordBoundary
specifier|private
specifier|final
name|CharMatcher
name|wordBoundary
decl_stmt|;
DECL|field|wordSeparator
specifier|private
specifier|final
name|String
name|wordSeparator
decl_stmt|;
DECL|method|CaseFormat (CharMatcher wordBoundary, String wordSeparator)
name|CaseFormat
parameter_list|(
name|CharMatcher
name|wordBoundary
parameter_list|,
name|String
name|wordSeparator
parameter_list|)
block|{
name|this
operator|.
name|wordBoundary
operator|=
name|wordBoundary
expr_stmt|;
name|this
operator|.
name|wordSeparator
operator|=
name|wordSeparator
expr_stmt|;
block|}
comment|/**    * Converts the specified {@code String str} from this format to the specified {@code format}. A    * "best effort" approach is taken; if {@code str} does not conform to the assumed format, then    * the behavior of this method is undefined but we make a reasonable effort at converting anyway.    */
DECL|method|to (CaseFormat format, String str)
specifier|public
specifier|final
name|String
name|to
parameter_list|(
name|CaseFormat
name|format
parameter_list|,
name|String
name|str
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|format
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|str
argument_list|)
expr_stmt|;
return|return
operator|(
name|format
operator|==
name|this
operator|)
condition|?
name|str
else|:
name|convert
argument_list|(
name|format
argument_list|,
name|str
argument_list|)
return|;
block|}
comment|/** Enum values can override for performance reasons. */
DECL|method|convert (CaseFormat format, String s)
name|String
name|convert
parameter_list|(
name|CaseFormat
name|format
parameter_list|,
name|String
name|s
parameter_list|)
block|{
comment|// deal with camel conversion
name|StringBuilder
name|out
init|=
literal|null
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
name|int
name|j
init|=
operator|-
literal|1
decl_stmt|;
while|while
condition|(
operator|(
name|j
operator|=
name|wordBoundary
operator|.
name|indexIn
argument_list|(
name|s
argument_list|,
operator|++
name|j
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
comment|// include some extra space for separators
name|out
operator|=
operator|new
name|StringBuilder
argument_list|(
name|s
operator|.
name|length
argument_list|()
operator|+
literal|4
operator|*
name|format
operator|.
name|wordSeparator
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|format
operator|.
name|normalizeFirstWord
argument_list|(
name|s
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|j
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|append
argument_list|(
name|format
operator|.
name|normalizeWord
argument_list|(
name|s
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|j
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|append
argument_list|(
name|format
operator|.
name|wordSeparator
argument_list|)
expr_stmt|;
name|i
operator|=
name|j
operator|+
name|wordSeparator
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
return|return
operator|(
name|i
operator|==
literal|0
operator|)
condition|?
name|format
operator|.
name|normalizeFirstWord
argument_list|(
name|s
argument_list|)
else|:
name|out
operator|.
name|append
argument_list|(
name|format
operator|.
name|normalizeWord
argument_list|(
name|s
operator|.
name|substring
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**    * Returns a {@code Converter} that converts strings from this format to {@code targetFormat}.    *    * @since 16.0    */
DECL|method|converterTo (CaseFormat targetFormat)
specifier|public
name|Converter
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|converterTo
parameter_list|(
name|CaseFormat
name|targetFormat
parameter_list|)
block|{
return|return
operator|new
name|StringConverter
argument_list|(
name|this
argument_list|,
name|targetFormat
argument_list|)
return|;
block|}
DECL|class|StringConverter
specifier|private
specifier|static
specifier|final
class|class
name|StringConverter
extends|extends
name|Converter
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|sourceFormat
specifier|private
specifier|final
name|CaseFormat
name|sourceFormat
decl_stmt|;
DECL|field|targetFormat
specifier|private
specifier|final
name|CaseFormat
name|targetFormat
decl_stmt|;
DECL|method|StringConverter (CaseFormat sourceFormat, CaseFormat targetFormat)
name|StringConverter
parameter_list|(
name|CaseFormat
name|sourceFormat
parameter_list|,
name|CaseFormat
name|targetFormat
parameter_list|)
block|{
name|this
operator|.
name|sourceFormat
operator|=
name|checkNotNull
argument_list|(
name|sourceFormat
argument_list|)
expr_stmt|;
name|this
operator|.
name|targetFormat
operator|=
name|checkNotNull
argument_list|(
name|targetFormat
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doForward (String s)
specifier|protected
name|String
name|doForward
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|sourceFormat
operator|.
name|to
argument_list|(
name|targetFormat
argument_list|,
name|s
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doBackward (String s)
specifier|protected
name|String
name|doBackward
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|targetFormat
operator|.
name|to
argument_list|(
name|sourceFormat
argument_list|,
name|s
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@heckForNull Object object)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|StringConverter
condition|)
block|{
name|StringConverter
name|that
init|=
operator|(
name|StringConverter
operator|)
name|object
decl_stmt|;
return|return
name|sourceFormat
operator|.
name|equals
argument_list|(
name|that
operator|.
name|sourceFormat
argument_list|)
operator|&&
name|targetFormat
operator|.
name|equals
argument_list|(
name|that
operator|.
name|targetFormat
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|sourceFormat
operator|.
name|hashCode
argument_list|()
operator|^
name|targetFormat
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|sourceFormat
operator|+
literal|".converterTo("
operator|+
name|targetFormat
operator|+
literal|")"
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0L
decl_stmt|;
block|}
DECL|method|normalizeWord (String word)
specifier|abstract
name|String
name|normalizeWord
parameter_list|(
name|String
name|word
parameter_list|)
function_decl|;
DECL|method|normalizeFirstWord (String word)
name|String
name|normalizeFirstWord
parameter_list|(
name|String
name|word
parameter_list|)
block|{
return|return
name|normalizeWord
argument_list|(
name|word
argument_list|)
return|;
block|}
DECL|method|firstCharOnlyToUpper (String word)
specifier|private
specifier|static
name|String
name|firstCharOnlyToUpper
parameter_list|(
name|String
name|word
parameter_list|)
block|{
return|return
name|word
operator|.
name|isEmpty
argument_list|()
condition|?
name|word
else|:
name|Ascii
operator|.
name|toUpperCase
argument_list|(
name|word
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|+
name|Ascii
operator|.
name|toLowerCase
argument_list|(
name|word
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
return|;
block|}
block|}
end_enum

end_unit

