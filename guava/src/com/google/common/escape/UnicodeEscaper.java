begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
comment|/**  * An {@link Escaper} that converts literal text into a format safe for inclusion in a particular  * context (such as an XML document). Typically (but not always), the inverse process of  * "unescaping" the text is performed automatically by the relevant parser.  *  *<p>For example, an XML escaper would convert the literal string {@code "Foo<Bar>"} into {@code  * "Foo&lt;Bar&gt;"} to prevent {@code "<Bar>"} from being confused with an XML tag. When the  * resulting XML document is parsed, the parser API will return this text as the original literal  * string {@code "Foo<Bar>"}.  *  *<p><b>Note:</b> This class is similar to {@link CharEscaper} but with one very important  * difference. A CharEscaper can only process Java<a  * href="http://en.wikipedia.org/wiki/UTF-16">UTF16</a> characters in isolation and may not cope  * when it encounters surrogate pairs. This class facilitates the correct escaping of all Unicode  * characters.  *  *<p>As there are important reasons, including potential security issues, to handle Unicode  * correctly if you are considering implementing a new escaper you should favor using UnicodeEscaper  * wherever possible.  *  *<p>A {@code UnicodeEscaper} instance is required to be stateless, and safe when used concurrently  * by multiple threads.  *  *<p>Popular escapers are defined as constants in classes like {@link  * com.google.common.html.HtmlEscapers} and {@link com.google.common.xml.XmlEscapers}. To create  * your own escapers extend this class and implement the {@link #escape(int)} method.  *  * @author David Beaumont  * @since 15.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|UnicodeEscaper
specifier|public
specifier|abstract
class|class
name|UnicodeEscaper
extends|extends
name|Escaper
block|{
comment|/** The amount of padding (chars) to use when growing the escape buffer. */
DECL|field|DEST_PAD
specifier|private
specifier|static
specifier|final
name|int
name|DEST_PAD
init|=
literal|32
decl_stmt|;
comment|/** Constructor for use by subclasses. */
DECL|method|UnicodeEscaper ()
specifier|protected
name|UnicodeEscaper
parameter_list|()
block|{}
comment|/**    * Returns the escaped form of the given Unicode code point, or {@code null} if this code point    * does not need to be escaped. When called as part of an escaping operation, the given code point    * is guaranteed to be in the range {@code 0<= cp<= Character#MAX_CODE_POINT}.    *    *<p>If an empty array is returned, this effectively strips the input character from the    * resulting text.    *    *<p>If the character does not need to be escaped, this method should return {@code null}, rather    * than an array containing the character representation of the code point. This enables the    * escaping algorithm to perform more efficiently.    *    *<p>If the implementation of this method cannot correctly handle a particular code point then it    * should either throw an appropriate runtime exception or return a suitable replacement    * character. It must never silently discard invalid input as this may constitute a security risk.    *    * @param cp the Unicode code point to escape if necessary    * @return the replacement characters, or {@code null} if no escaping was needed    */
annotation|@
name|CheckForNull
DECL|method|escape (int cp)
specifier|protected
specifier|abstract
name|char
index|[]
name|escape
parameter_list|(
name|int
name|cp
parameter_list|)
function_decl|;
comment|/**    * Returns the escaped form of a given literal string.    *    *<p>If you are escaping input in arbitrary successive chunks, then it is not generally safe to    * use this method. If an input string ends with an unmatched high surrogate character, then this    * method will throw {@link IllegalArgumentException}. You should ensure your input is valid<a    * href="http://en.wikipedia.org/wiki/UTF-16">UTF-16</a> before calling this method.    *    *<p><b>Note:</b> When implementing an escaper it is a good idea to override this method for    * efficiency by inlining the implementation of {@link #nextEscapeIndex(CharSequence, int, int)}    * directly. Doing this for {@link com.google.common.net.PercentEscaper} more than doubled the    * performance for unescaped strings (as measured by {@code CharEscapersBenchmark}).    *    * @param string the literal string to be escaped    * @return the escaped form of {@code string}    * @throws NullPointerException if {@code string} is null    * @throws IllegalArgumentException if invalid surrogate characters are encountered    */
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
name|int
name|end
init|=
name|string
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|index
init|=
name|nextEscapeIndex
argument_list|(
name|string
argument_list|,
literal|0
argument_list|,
name|end
argument_list|)
decl_stmt|;
return|return
name|index
operator|==
name|end
condition|?
name|string
else|:
name|escapeSlow
argument_list|(
name|string
argument_list|,
name|index
argument_list|)
return|;
block|}
comment|/**    * Scans a sub-sequence of characters from a given {@link CharSequence}, returning the index of    * the next character that requires escaping.    *    *<p><b>Note:</b> When implementing an escaper, it is a good idea to override this method for    * efficiency. The base class implementation determines successive Unicode code points and invokes    * {@link #escape(int)} for each of them. If the semantics of your escaper are such that code    * points in the supplementary range are either all escaped or all unescaped, this method can be    * implemented more efficiently using {@link CharSequence#charAt(int)}.    *    *<p>Note however that if your escaper does not escape characters in the supplementary range, you    * should either continue to validate the correctness of any surrogate characters encountered or    * provide a clear warning to users that your escaper does not validate its input.    *    *<p>See {@link com.google.common.net.PercentEscaper} for an example.    *    * @param csq a sequence of characters    * @param start the index of the first character to be scanned    * @param end the index immediately after the last character to be scanned    * @throws IllegalArgumentException if the scanned sub-sequence of {@code csq} contains invalid    *     surrogate pairs    */
DECL|method|nextEscapeIndex (CharSequence csq, int start, int end)
specifier|protected
name|int
name|nextEscapeIndex
parameter_list|(
name|CharSequence
name|csq
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
block|{
name|int
name|index
init|=
name|start
decl_stmt|;
while|while
condition|(
name|index
operator|<
name|end
condition|)
block|{
name|int
name|cp
init|=
name|codePointAt
argument_list|(
name|csq
argument_list|,
name|index
argument_list|,
name|end
argument_list|)
decl_stmt|;
if|if
condition|(
name|cp
operator|<
literal|0
operator|||
name|escape
argument_list|(
name|cp
argument_list|)
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
name|index
operator|+=
name|Character
operator|.
name|isSupplementaryCodePoint
argument_list|(
name|cp
argument_list|)
condition|?
literal|2
else|:
literal|1
expr_stmt|;
block|}
return|return
name|index
return|;
block|}
comment|/**    * Returns the escaped form of a given literal string, starting at the given index. This method is    * called by the {@link #escape(String)} method when it discovers that escaping is required. It is    * protected to allow subclasses to override the fastpath escaping function to inline their    * escaping test. See {@link CharEscaperBuilder} for an example usage.    *    *<p>This method is not reentrant and may only be invoked by the top level {@link    * #escape(String)} method.    *    * @param s the literal string to be escaped    * @param index the index to start escaping from    * @return the escaped form of {@code string}    * @throws NullPointerException if {@code string} is null    * @throws IllegalArgumentException if invalid surrogate characters are encountered    */
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
name|end
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
name|destIndex
init|=
literal|0
decl_stmt|;
name|int
name|unescapedChunkStart
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|index
operator|<
name|end
condition|)
block|{
name|int
name|cp
init|=
name|codePointAt
argument_list|(
name|s
argument_list|,
name|index
argument_list|,
name|end
argument_list|)
decl_stmt|;
if|if
condition|(
name|cp
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Trailing high surrogate at end of input"
argument_list|)
throw|;
block|}
comment|// It is possible for this to return null because nextEscapeIndex() may
comment|// (for performance reasons) yield some false positives but it must never
comment|// give false negatives.
name|char
index|[]
name|escaped
init|=
name|escape
argument_list|(
name|cp
argument_list|)
decl_stmt|;
name|int
name|nextIndex
init|=
name|index
operator|+
operator|(
name|Character
operator|.
name|isSupplementaryCodePoint
argument_list|(
name|cp
argument_list|)
condition|?
literal|2
else|:
literal|1
operator|)
decl_stmt|;
if|if
condition|(
name|escaped
operator|!=
literal|null
condition|)
block|{
name|int
name|charsSkipped
init|=
name|index
operator|-
name|unescapedChunkStart
decl_stmt|;
comment|// This is the size needed to add the replacement, not the full
comment|// size needed by the string. We only regrow when we absolutely must.
name|int
name|sizeNeeded
init|=
name|destIndex
operator|+
name|charsSkipped
operator|+
name|escaped
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|dest
operator|.
name|length
operator|<
name|sizeNeeded
condition|)
block|{
name|int
name|destLength
init|=
name|sizeNeeded
operator|+
operator|(
name|end
operator|-
name|index
operator|)
operator|+
name|DEST_PAD
decl_stmt|;
name|dest
operator|=
name|growBuffer
argument_list|(
name|dest
argument_list|,
name|destIndex
argument_list|,
name|destLength
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
name|unescapedChunkStart
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
if|if
condition|(
name|escaped
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|escaped
argument_list|,
literal|0
argument_list|,
name|dest
argument_list|,
name|destIndex
argument_list|,
name|escaped
operator|.
name|length
argument_list|)
expr_stmt|;
name|destIndex
operator|+=
name|escaped
operator|.
name|length
expr_stmt|;
block|}
comment|// If we dealt with an escaped character, reset the unescaped range.
name|unescapedChunkStart
operator|=
name|nextIndex
expr_stmt|;
block|}
name|index
operator|=
name|nextEscapeIndex
argument_list|(
name|s
argument_list|,
name|nextIndex
argument_list|,
name|end
argument_list|)
expr_stmt|;
block|}
comment|// Process trailing unescaped characters - no need to account for escaped
comment|// length or padding the allocation.
name|int
name|charsSkipped
init|=
name|end
operator|-
name|unescapedChunkStart
decl_stmt|;
if|if
condition|(
name|charsSkipped
operator|>
literal|0
condition|)
block|{
name|int
name|endIndex
init|=
name|destIndex
operator|+
name|charsSkipped
decl_stmt|;
if|if
condition|(
name|dest
operator|.
name|length
operator|<
name|endIndex
condition|)
block|{
name|dest
operator|=
name|growBuffer
argument_list|(
name|dest
argument_list|,
name|destIndex
argument_list|,
name|endIndex
argument_list|)
expr_stmt|;
block|}
name|s
operator|.
name|getChars
argument_list|(
name|unescapedChunkStart
argument_list|,
name|end
argument_list|,
name|dest
argument_list|,
name|destIndex
argument_list|)
expr_stmt|;
name|destIndex
operator|=
name|endIndex
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
comment|/**    * Returns the Unicode code point of the character at the given index.    *    *<p>Unlike {@link Character#codePointAt(CharSequence, int)} or {@link String#codePointAt(int)}    * this method will never fail silently when encountering an invalid surrogate pair.    *    *<p>The behaviour of this method is as follows:    *    *<ol>    *<li>If {@code index>= end}, {@link IndexOutOfBoundsException} is thrown.    *<li><b>If the character at the specified index is not a surrogate, it is returned.</b>    *<li>If the first character was a high surrogate value, then an attempt is made to read the    *       next character.    *<ol>    *<li><b>If the end of the sequence was reached, the negated value of the trailing high    *             surrogate is returned.</b>    *<li><b>If the next character was a valid low surrogate, the code point value of the    *             high/low surrogate pair is returned.</b>    *<li>If the next character was not a low surrogate value, then {@link    *             IllegalArgumentException} is thrown.    *</ol>    *<li>If the first character was a low surrogate value, {@link IllegalArgumentException} is    *       thrown.    *</ol>    *    * @param seq the sequence of characters from which to decode the code point    * @param index the index of the first character to decode    * @param end the index beyond the last valid character to decode    * @return the Unicode code point for the given index or the negated value of the trailing high    *     surrogate character at the end of the sequence    */
DECL|method|codePointAt (CharSequence seq, int index, int end)
specifier|protected
specifier|static
name|int
name|codePointAt
parameter_list|(
name|CharSequence
name|seq
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|end
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|seq
argument_list|)
expr_stmt|;
if|if
condition|(
name|index
operator|<
name|end
condition|)
block|{
name|char
name|c1
init|=
name|seq
operator|.
name|charAt
argument_list|(
name|index
operator|++
argument_list|)
decl_stmt|;
if|if
condition|(
name|c1
argument_list|<
name|Character
operator|.
name|MIN_HIGH_SURROGATE
operator|||
name|c1
argument_list|>
name|Character
operator|.
name|MAX_LOW_SURROGATE
condition|)
block|{
comment|// Fast path (first test is probably all we need to do)
return|return
name|c1
return|;
block|}
elseif|else
if|if
condition|(
name|c1
operator|<=
name|Character
operator|.
name|MAX_HIGH_SURROGATE
condition|)
block|{
comment|// If the high surrogate was the last character, return its inverse
if|if
condition|(
name|index
operator|==
name|end
condition|)
block|{
return|return
operator|-
name|c1
return|;
block|}
comment|// Otherwise look for the low surrogate following it
name|char
name|c2
init|=
name|seq
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|Character
operator|.
name|isLowSurrogate
argument_list|(
name|c2
argument_list|)
condition|)
block|{
return|return
name|Character
operator|.
name|toCodePoint
argument_list|(
name|c1
argument_list|,
name|c2
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected low surrogate but got char '"
operator|+
name|c2
operator|+
literal|"' with value "
operator|+
operator|(
name|int
operator|)
name|c2
operator|+
literal|" at index "
operator|+
name|index
operator|+
literal|" in '"
operator|+
name|seq
operator|+
literal|"'"
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unexpected low surrogate character '"
operator|+
name|c1
operator|+
literal|"' with value "
operator|+
operator|(
name|int
operator|)
name|c1
operator|+
literal|" at index "
operator|+
operator|(
name|index
operator|-
literal|1
operator|)
operator|+
literal|" in '"
operator|+
name|seq
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
literal|"Index exceeds specified range"
argument_list|)
throw|;
block|}
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
block|}
end_class

end_unit

