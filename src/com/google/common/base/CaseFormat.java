begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2006 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * Utility class for converting between various case formats.  *  * @author Mike Bostock  * @since 9.09.15<b>tentative</b>  */
end_comment

begin_enum
DECL|enum|CaseFormat
specifier|public
enum|enum
name|CaseFormat
block|{
comment|/**    * Hyphenated variable naming convention, e.g., "lower-hyphen".    */
DECL|enumConstant|LOWER_HYPHEN
DECL|enumConstant|Pattern.compile
name|LOWER_HYPHEN
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
literal|"[-]"
argument_list|)
argument_list|,
literal|"-"
argument_list|)
block|,
comment|/**    * C++ variable naming convention, e.g., "lower_underscore".    */
DECL|enumConstant|LOWER_UNDERSCORE
DECL|enumConstant|Pattern.compile
name|LOWER_UNDERSCORE
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
literal|"[_]"
argument_list|)
argument_list|,
literal|"_"
argument_list|)
block|,
comment|/**    * Java variable naming convention, e.g., "lowerCamel".    */
DECL|enumConstant|LOWER_CAMEL
DECL|enumConstant|Pattern.compile
name|LOWER_CAMEL
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
literal|"[A-Z]"
argument_list|)
argument_list|,
literal|""
argument_list|)
block|,
comment|/**    * Java and C++ class naming convention, e.g., "UpperCamel".    */
DECL|enumConstant|UPPER_CAMEL
DECL|enumConstant|Pattern.compile
name|UPPER_CAMEL
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
literal|"[A-Z]"
argument_list|)
argument_list|,
literal|""
argument_list|)
block|,
comment|/**    * Java and C++ constant naming convention, e.g., "UPPER_UNDERSCORE".    */
DECL|enumConstant|UPPER_UNDERSCORE
DECL|enumConstant|Pattern.compile
name|UPPER_UNDERSCORE
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
literal|"[_]"
argument_list|)
argument_list|,
literal|"_"
argument_list|)
block|;
DECL|field|wordBoundary
specifier|private
specifier|final
name|Pattern
name|wordBoundary
decl_stmt|;
DECL|field|wordSeparator
specifier|private
specifier|final
name|String
name|wordSeparator
decl_stmt|;
DECL|method|CaseFormat (Pattern wordBoundary, String wordSeparator)
specifier|private
name|CaseFormat
parameter_list|(
name|Pattern
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
comment|/**    * Converts the specified {@code String s} from this format to the specified    * {@code format}. A "best effort" approach is taken; if {@code s} does not    * conform to the assumed format, then the behavior of this method is    * undefined but we make a reasonable effort at converting anyway.    */
DECL|method|to (CaseFormat format, String s)
specifier|public
name|String
name|to
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
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
comment|/* optimize case where no conversion is required */
if|if
condition|(
name|format
operator|==
name|this
condition|)
block|{
return|return
name|s
return|;
block|}
comment|/* optimize cases where no camel conversion is required */
switch|switch
condition|(
name|this
condition|)
block|{
case|case
name|LOWER_HYPHEN
case|:
switch|switch
condition|(
name|format
condition|)
block|{
case|case
name|LOWER_UNDERSCORE
case|:
return|return
name|s
operator|.
name|replace
argument_list|(
literal|"-"
argument_list|,
literal|"_"
argument_list|)
return|;
case|case
name|UPPER_UNDERSCORE
case|:
return|return
name|s
operator|.
name|replace
argument_list|(
literal|"-"
argument_list|,
literal|"_"
argument_list|)
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
return|;
block|}
break|break;
case|case
name|LOWER_UNDERSCORE
case|:
switch|switch
condition|(
name|format
condition|)
block|{
case|case
name|LOWER_HYPHEN
case|:
return|return
name|s
operator|.
name|replace
argument_list|(
literal|"_"
argument_list|,
literal|"-"
argument_list|)
return|;
case|case
name|UPPER_UNDERSCORE
case|:
return|return
name|s
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
return|;
block|}
break|break;
case|case
name|UPPER_UNDERSCORE
case|:
switch|switch
condition|(
name|format
condition|)
block|{
case|case
name|LOWER_HYPHEN
case|:
return|return
name|s
operator|.
name|replace
argument_list|(
literal|"_"
argument_list|,
literal|"-"
argument_list|)
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
return|;
case|case
name|LOWER_UNDERSCORE
case|:
return|return
name|s
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
return|;
block|}
break|break;
block|}
comment|/* otherwise, deal with camel conversion */
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
for|for
control|(
name|Matcher
name|matcher
init|=
name|wordBoundary
operator|.
name|matcher
argument_list|(
name|s
argument_list|)
init|;
name|matcher
operator|.
name|find
argument_list|()
condition|;
control|)
block|{
name|int
name|j
init|=
name|matcher
operator|.
name|start
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
comment|/* include some extra space for separators */
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
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
return|return
name|format
operator|.
name|normalizeFirstWord
argument_list|(
name|s
argument_list|)
return|;
block|}
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
expr_stmt|;
return|return
name|out
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|normalizeFirstWord (String word)
specifier|private
name|String
name|normalizeFirstWord
parameter_list|(
name|String
name|word
parameter_list|)
block|{
switch|switch
condition|(
name|this
condition|)
block|{
case|case
name|LOWER_CAMEL
case|:
return|return
name|word
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
return|;
default|default:
return|return
name|normalizeWord
argument_list|(
name|word
argument_list|)
return|;
block|}
block|}
DECL|method|normalizeWord (String word)
specifier|private
name|String
name|normalizeWord
parameter_list|(
name|String
name|word
parameter_list|)
block|{
switch|switch
condition|(
name|this
condition|)
block|{
case|case
name|LOWER_HYPHEN
case|:
return|return
name|word
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
return|;
case|case
name|LOWER_UNDERSCORE
case|:
return|return
name|word
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
return|;
case|case
name|LOWER_CAMEL
case|:
return|return
name|toTitleCase
argument_list|(
name|word
argument_list|)
return|;
case|case
name|UPPER_CAMEL
case|:
return|return
name|toTitleCase
argument_list|(
name|word
argument_list|)
return|;
case|case
name|UPPER_UNDERSCORE
case|:
return|return
name|word
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
return|;
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"unknown case: "
operator|+
name|this
argument_list|)
throw|;
block|}
DECL|method|toTitleCase (String word)
specifier|private
specifier|static
name|String
name|toTitleCase
parameter_list|(
name|String
name|word
parameter_list|)
block|{
return|return
operator|(
name|word
operator|.
name|length
argument_list|()
operator|<
literal|2
operator|)
condition|?
name|word
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
else|:
operator|(
name|Character
operator|.
name|toTitleCase
argument_list|(
name|word
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|+
name|word
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|)
return|;
block|}
block|}
end_enum

end_unit

