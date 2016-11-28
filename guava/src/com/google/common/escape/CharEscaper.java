begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2006 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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

begin_comment
comment|/**  * An object that converts literal text into a format safe for inclusion in a particular context  * (such as an XML document). Typically (but not always), the inverse process of "unescaping" the  * text is performed automatically by the relevant parser.  *  *<p>For example, an XML escaper would convert the literal string {@code "Foo<Bar>"} into {@code  * "Foo&lt;Bar&gt;"} to prevent {@code "<Bar>"} from being confused with an XML tag. When the  * resulting XML document is parsed, the parser API will return this text as the original literal  * string {@code "Foo<Bar>"}.  *  *<p>A {@code CharEscaper} instance is required to be stateless, and safe when used concurrently by  * multiple threads.  *  *<p>Popular escapers are defined as constants in classes like  * {@link com.google.common.html.HtmlEscapers} and {@link com.google.common.xml.XmlEscapers}. To  * create your own escapers extend this class and implement the {@link #escape(char)} method.  *  * @author Sven Mawson  * @since 15.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|CharEscaper
specifier|public
specifier|abstract
class|class
name|CharEscaper
extends|extends
name|Escaper
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|CharEscaper ()
specifier|protected
name|CharEscaper
parameter_list|()
block|{}
comment|/**    * Returns the escaped form of a given literal string.    *    * @param string the literal string to be escaped    * @return the escaped form of {@code string}    * @throws NullPointerException if {@code string} is null    */
annotation|@
name|Override
DECL|method|escape (String string)
specifier|public
name|String
name|escape
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|string
argument_list|)
expr_stmt|;
comment|// GWT specific check (do not optimize)
comment|// Inlineable fast-path loop which hands off to escapeSlow() only if needed
name|int
name|length
init|=
name|string
operator|.
name|length
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|length
condition|;
name|index
operator|++
control|)
block|{
if|if
condition|(
name|escape
argument_list|(
name|string
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|escapeSlow
argument_list|(
name|string
argument_list|,
name|index
argument_list|)
return|;
block|}
block|}
return|return
name|string
return|;
block|}
comment|/**    * Returns the escaped form of a given literal string, starting at the given index. This method is    * called by the {@link #escape(String)} method when it discovers that escaping is required. It is    * protected to allow subclasses to override the fastpath escaping function to inline their    * escaping test. See {@link CharEscaperBuilder} for an example usage.    *    * @param s the literal string to be escaped    * @param index the index to start escaping from    * @return the escaped form of {@code string}    * @throws NullPointerException if {@code string} is null    */
DECL|method|escapeSlow (String s, int index)
specifier|protected
specifier|final
name|String
name|escapeSlow
parameter_list|(
name|String
name|s
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|int
name|slen
init|=
name|s
operator|.
name|length
argument_list|()
decl_stmt|;
comment|// Get a destination buffer and setup some loop variables.
name|char
index|[]
name|dest
init|=
name|Platform
operator|.
name|charBufferFromThreadLocal
argument_list|()
decl_stmt|;
name|int
name|destSize
init|=
name|dest
operator|.
name|length
decl_stmt|;
name|int
name|destIndex
init|=
literal|0
decl_stmt|;
name|int
name|lastEscape
init|=
literal|0
decl_stmt|;
comment|// Loop through the rest of the string, replacing when needed into the
comment|// destination buffer, which gets grown as needed as well.
for|for
control|(
init|;
name|index
operator|<
name|slen
condition|;
name|index
operator|++
control|)
block|{
comment|// Get a replacement for the current character.
name|char
index|[]
name|r
init|=
name|escape
argument_list|(
name|s
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
argument_list|)
decl_stmt|;
comment|// If no replacement is needed, just continue.
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|int
name|rlen
init|=
name|r
operator|.
name|length
decl_stmt|;
name|int
name|charsSkipped
init|=
name|index
operator|-
name|lastEscape
decl_stmt|;
comment|// This is the size needed to add the replacement, not the full size
comment|// needed by the string. We only regrow when we absolutely must, and
comment|// when we do grow, grow enough to avoid excessive growing. Grow.
name|int
name|sizeNeeded
init|=
name|destIndex
operator|+
name|charsSkipped
operator|+
name|rlen
decl_stmt|;
if|if
condition|(
name|destSize
operator|<
name|sizeNeeded
condition|)
block|{
name|destSize
operator|=
name|sizeNeeded
operator|+
name|DEST_PAD_MULTIPLIER
operator|*
operator|(
name|slen
operator|-
name|index
operator|)
expr_stmt|;
name|dest
operator|=
name|growBuffer
argument_list|(
name|dest
argument_list|,
name|destIndex
argument_list|,
name|destSize
argument_list|)
expr_stmt|;
block|}
comment|// If we have skipped any characters, we need to copy them now.
if|if
condition|(
name|charsSkipped
operator|>
literal|0
condition|)
block|{
name|s
operator|.
name|getChars
argument_list|(
name|lastEscape
argument_list|,
name|index
argument_list|,
name|dest
argument_list|,
name|destIndex
argument_list|)
expr_stmt|;
name|destIndex
operator|+=
name|charsSkipped
expr_stmt|;
block|}
comment|// Copy the replacement string into the dest buffer as needed.
if|if
condition|(
name|rlen
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|r
argument_list|,
literal|0
argument_list|,
name|dest
argument_list|,
name|destIndex
argument_list|,
name|rlen
argument_list|)
expr_stmt|;
name|destIndex
operator|+=
name|rlen
expr_stmt|;
block|}
name|lastEscape
operator|=
name|index
operator|+
literal|1
expr_stmt|;
block|}
comment|// Copy leftover characters if there are any.
name|int
name|charsLeft
init|=
name|slen
operator|-
name|lastEscape
decl_stmt|;
if|if
condition|(
name|charsLeft
operator|>
literal|0
condition|)
block|{
name|int
name|sizeNeeded
init|=
name|destIndex
operator|+
name|charsLeft
decl_stmt|;
if|if
condition|(
name|destSize
operator|<
name|sizeNeeded
condition|)
block|{
comment|// Regrow and copy, expensive! No padding as this is the final copy.
name|dest
operator|=
name|growBuffer
argument_list|(
name|dest
argument_list|,
name|destIndex
argument_list|,
name|sizeNeeded
argument_list|)
expr_stmt|;
block|}
name|s
operator|.
name|getChars
argument_list|(
name|lastEscape
argument_list|,
name|slen
argument_list|,
name|dest
argument_list|,
name|destIndex
argument_list|)
expr_stmt|;
name|destIndex
operator|=
name|sizeNeeded
expr_stmt|;
block|}
return|return
operator|new
name|String
argument_list|(
name|dest
argument_list|,
literal|0
argument_list|,
name|destIndex
argument_list|)
return|;
block|}
comment|/**    * Returns the escaped form of the given character, or {@code null} if this character does not    * need to be escaped. If an empty array is returned, this effectively strips the input character    * from the resulting text.    *    *<p>If the character does not need to be escaped, this method should return {@code null}, rather    * than a one-character array containing the character itself. This enables the escaping algorithm    * to perform more efficiently.    *    *<p>An escaper is expected to be able to deal with any {@code char} value, so this method should    * not throw any exceptions.    *    * @param c the character to escape if necessary    * @return the replacement characters, or {@code null} if no escaping was needed    */
DECL|method|escape (char c)
specifier|protected
specifier|abstract
name|char
index|[]
name|escape
parameter_list|(
name|char
name|c
parameter_list|)
function_decl|;
comment|/**    * Helper method to grow the character buffer as needed, this only happens once in a while so it's    * ok if it's in a method call. If the index passed in is 0 then no copying will be done.    */
DECL|method|growBuffer (char[] dest, int index, int size)
specifier|private
specifier|static
name|char
index|[]
name|growBuffer
parameter_list|(
name|char
index|[]
name|dest
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|size
parameter_list|)
block|{
if|if
condition|(
name|size
operator|<
literal|0
condition|)
block|{
comment|// overflow - should be OutOfMemoryError but GWT/j2cl don't support it
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"Cannot increase internal buffer any further"
argument_list|)
throw|;
block|}
name|char
index|[]
name|copy
init|=
operator|new
name|char
index|[
name|size
index|]
decl_stmt|;
if|if
condition|(
name|index
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|dest
argument_list|,
literal|0
argument_list|,
name|copy
argument_list|,
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
return|return
name|copy
return|;
block|}
comment|/**    * The multiplier for padding to use when growing the escape buffer.    */
DECL|field|DEST_PAD_MULTIPLIER
specifier|private
specifier|static
specifier|final
name|int
name|DEST_PAD_MULTIPLIER
init|=
literal|2
decl_stmt|;
block|}
end_class

end_unit

