begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.escape
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|escape
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
name|collect
operator|.
name|ImmutableMap
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
name|testing
operator|.
name|EscaperAsserts
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * @author David Beaumont  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ArrayBasedCharEscaperTest
specifier|public
class|class
name|ArrayBasedCharEscaperTest
extends|extends
name|TestCase
block|{
DECL|field|NO_REPLACEMENTS
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Character
argument_list|,
name|String
argument_list|>
name|NO_REPLACEMENTS
init|=
name|ImmutableMap
operator|.
name|of
argument_list|()
decl_stmt|;
DECL|field|SIMPLE_REPLACEMENTS
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Character
argument_list|,
name|String
argument_list|>
name|SIMPLE_REPLACEMENTS
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|'\n'
argument_list|,
literal|"<newline>"
argument_list|,
literal|'\t'
argument_list|,
literal|"<tab>"
argument_list|,
literal|'&'
argument_list|,
literal|"<and>"
argument_list|)
decl_stmt|;
DECL|method|testSafeRange ()
specifier|public
name|void
name|testSafeRange
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Basic escaping of unsafe chars (wrap them in {,}'s)
name|CharEscaper
name|wrappingEscaper
init|=
operator|new
name|ArrayBasedCharEscaper
argument_list|(
name|NO_REPLACEMENTS
argument_list|,
literal|'A'
argument_list|,
literal|'Z'
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|char
index|[]
name|escapeUnsafe
parameter_list|(
name|char
name|c
parameter_list|)
block|{
return|return
operator|(
literal|"{"
operator|+
name|c
operator|+
literal|"}"
operator|)
operator|.
name|toCharArray
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|EscaperAsserts
operator|.
name|assertBasic
argument_list|(
name|wrappingEscaper
argument_list|)
expr_stmt|;
comment|// '[' and '@' lie either side of [A-Z].
name|assertEquals
argument_list|(
literal|"{[}FOO{@}BAR{]}"
argument_list|,
name|wrappingEscaper
operator|.
name|escape
argument_list|(
literal|"[FOO@BAR]"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testDeleteUnsafeChars ()
specifier|public
name|void
name|testDeleteUnsafeChars
parameter_list|()
throws|throws
name|IOException
block|{
name|CharEscaper
name|deletingEscaper
init|=
operator|new
name|ArrayBasedCharEscaper
argument_list|(
name|NO_REPLACEMENTS
argument_list|,
literal|' '
argument_list|,
literal|'~'
argument_list|)
block|{
specifier|private
specifier|final
name|char
index|[]
name|noChars
init|=
operator|new
name|char
index|[
literal|0
index|]
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|char
index|[]
name|escapeUnsafe
parameter_list|(
name|char
name|c
parameter_list|)
block|{
return|return
name|noChars
return|;
block|}
block|}
decl_stmt|;
name|EscaperAsserts
operator|.
name|assertBasic
argument_list|(
name|deletingEscaper
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Everything outside the printable ASCII range is deleted."
argument_list|,
name|deletingEscaper
operator|.
name|escape
argument_list|(
literal|"\tEverything\0 outside the\uD800\uDC00 "
operator|+
literal|"printable ASCII \uFFFFrange is \u007Fdeleted.\n"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testReplacementPriority ()
specifier|public
name|void
name|testReplacementPriority
parameter_list|()
throws|throws
name|IOException
block|{
name|CharEscaper
name|replacingEscaper
init|=
operator|new
name|ArrayBasedCharEscaper
argument_list|(
name|SIMPLE_REPLACEMENTS
argument_list|,
literal|' '
argument_list|,
literal|'~'
argument_list|)
block|{
specifier|private
specifier|final
name|char
index|[]
name|unknown
init|=
operator|new
name|char
index|[]
block|{
literal|'?'
block|}
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|char
index|[]
name|escapeUnsafe
parameter_list|(
name|char
name|c
parameter_list|)
block|{
return|return
name|unknown
return|;
block|}
block|}
decl_stmt|;
name|EscaperAsserts
operator|.
name|assertBasic
argument_list|(
name|replacingEscaper
argument_list|)
expr_stmt|;
comment|// Replacements are applied first regardless of whether the character is in
comment|// the safe range or not ('&' is a safe char while '\t' and '\n' are not).
name|assertEquals
argument_list|(
literal|"<tab>Fish<and>? Chips?<newline>"
argument_list|,
name|replacingEscaper
operator|.
name|escape
argument_list|(
literal|"\tFish&\0 Chips\r\n"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

