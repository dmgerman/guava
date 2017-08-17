begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Static utility methods pertaining to {@link Escaper} instances.  *  * @author Sven Mawson  * @author David Beaumont  * @since 15.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|Escapers
specifier|public
specifier|final
class|class
name|Escapers
block|{
DECL|method|Escapers ()
specifier|private
name|Escapers
parameter_list|()
block|{}
comment|/**    * Returns an {@link Escaper} that does no escaping, passing all character data through unchanged.    */
DECL|method|nullEscaper ()
specifier|public
specifier|static
name|Escaper
name|nullEscaper
parameter_list|()
block|{
return|return
name|NULL_ESCAPER
return|;
block|}
comment|// An Escaper that efficiently performs no escaping.
comment|// Extending CharEscaper (instead of Escaper) makes Escapers.compose() easier.
DECL|field|NULL_ESCAPER
specifier|private
specifier|static
specifier|final
name|Escaper
name|NULL_ESCAPER
init|=
operator|new
name|CharEscaper
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|escape
parameter_list|(
name|String
name|string
parameter_list|)
block|{
return|return
name|checkNotNull
argument_list|(
name|string
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|char
index|[]
name|escape
parameter_list|(
name|char
name|c
parameter_list|)
block|{
comment|// TODO: Fix tests not to call this directly and make it throw an error.
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
comment|/**    * Returns a builder for creating simple, fast escapers. A builder instance can be reused and each    * escaper that is created will be a snapshot of the current builder state. Builders are not    * thread safe.    *    *<p>The initial state of the builder is such that:    *<ul>    *<li>There are no replacement mappings    *<li>{@code safeMin == Character.MIN_VALUE}    *<li>{@code safeMax == Character.MAX_VALUE}    *<li>{@code unsafeReplacement == null}    *</ul>    *<p>For performance reasons escapers created by this builder are not Unicode aware and will not    * validate the well-formedness of their input.    */
DECL|method|builder ()
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|Builder
argument_list|()
return|;
block|}
comment|/**    * A builder for simple, fast escapers.    *    *<p>Typically an escaper needs to deal with the escaping of high valued characters or code    * points. In these cases it is necessary to extend either {@link ArrayBasedCharEscaper} or    * {@link ArrayBasedUnicodeEscaper} to provide the desired behavior. However this builder is    * suitable for creating escapers that replace a relative small set of characters.    *    * @author David Beaumont    * @since 15.0    */
annotation|@
name|Beta
DECL|class|Builder
specifier|public
specifier|static
specifier|final
class|class
name|Builder
block|{
DECL|field|replacementMap
specifier|private
specifier|final
name|Map
argument_list|<
name|Character
argument_list|,
name|String
argument_list|>
name|replacementMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|safeMin
specifier|private
name|char
name|safeMin
init|=
name|Character
operator|.
name|MIN_VALUE
decl_stmt|;
DECL|field|safeMax
specifier|private
name|char
name|safeMax
init|=
name|Character
operator|.
name|MAX_VALUE
decl_stmt|;
DECL|field|unsafeReplacement
specifier|private
name|String
name|unsafeReplacement
init|=
literal|null
decl_stmt|;
comment|// The constructor is exposed via the builder() method above.
DECL|method|Builder ()
specifier|private
name|Builder
parameter_list|()
block|{}
comment|/**      * Sets the safe range of characters for the escaper. Characters in this range that have no      * explicit replacement are considered 'safe' and remain unescaped in the output. If      * {@code safeMax< safeMin} then the safe range is empty.      *      * @param safeMin the lowest 'safe' character      * @param safeMax the highest 'safe' character      * @return the builder instance      */
annotation|@
name|CanIgnoreReturnValue
DECL|method|setSafeRange (char safeMin, char safeMax)
specifier|public
name|Builder
name|setSafeRange
parameter_list|(
name|char
name|safeMin
parameter_list|,
name|char
name|safeMax
parameter_list|)
block|{
name|this
operator|.
name|safeMin
operator|=
name|safeMin
expr_stmt|;
name|this
operator|.
name|safeMax
operator|=
name|safeMax
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the replacement string for any characters outside the 'safe' range that have no explicit      * replacement. If {@code unsafeReplacement} is {@code null} then no replacement will occur, if      * it is {@code ""} then the unsafe characters are removed from the output.      *      * @param unsafeReplacement the string to replace unsafe characters      * @return the builder instance      */
annotation|@
name|CanIgnoreReturnValue
DECL|method|setUnsafeReplacement (@ullable String unsafeReplacement)
specifier|public
name|Builder
name|setUnsafeReplacement
parameter_list|(
annotation|@
name|Nullable
name|String
name|unsafeReplacement
parameter_list|)
block|{
name|this
operator|.
name|unsafeReplacement
operator|=
name|unsafeReplacement
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds a replacement string for the given input character. The specified character will be      * replaced by the given string whenever it occurs in the input, irrespective of whether it lies      * inside or outside the 'safe' range.      *      * @param c the character to be replaced      * @param replacement the string to replace the given character      * @return the builder instance      * @throws NullPointerException if {@code replacement} is null      */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addEscape (char c, String replacement)
specifier|public
name|Builder
name|addEscape
parameter_list|(
name|char
name|c
parameter_list|,
name|String
name|replacement
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|replacement
argument_list|)
expr_stmt|;
comment|// This can replace an existing character (the builder is re-usable).
name|replacementMap
operator|.
name|put
argument_list|(
name|c
argument_list|,
name|replacement
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Returns a new escaper based on the current state of the builder.      */
DECL|method|build ()
specifier|public
name|Escaper
name|build
parameter_list|()
block|{
return|return
operator|new
name|ArrayBasedCharEscaper
argument_list|(
name|replacementMap
argument_list|,
name|safeMin
argument_list|,
name|safeMax
argument_list|)
block|{
specifier|private
specifier|final
name|char
index|[]
name|replacementChars
init|=
name|unsafeReplacement
operator|!=
literal|null
condition|?
name|unsafeReplacement
operator|.
name|toCharArray
argument_list|()
else|:
literal|null
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
name|replacementChars
return|;
block|}
block|}
return|;
block|}
block|}
comment|/**    * Returns a {@link UnicodeEscaper} equivalent to the given escaper instance. If the escaper is    * already a UnicodeEscaper then it is simply returned, otherwise it is wrapped in a    * UnicodeEscaper.    *    *<p>When a {@link CharEscaper} escaper is wrapped by this method it acquires extra behavior with    * respect to the well-formedness of Unicode character sequences and will throw    * {@link IllegalArgumentException} when given bad input.    *    * @param escaper the instance to be wrapped    * @return a UnicodeEscaper with the same behavior as the given instance    * @throws NullPointerException if escaper is null    * @throws IllegalArgumentException if escaper is not a UnicodeEscaper or a CharEscaper    */
DECL|method|asUnicodeEscaper (Escaper escaper)
specifier|static
name|UnicodeEscaper
name|asUnicodeEscaper
parameter_list|(
name|Escaper
name|escaper
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|escaper
argument_list|)
expr_stmt|;
if|if
condition|(
name|escaper
operator|instanceof
name|UnicodeEscaper
condition|)
block|{
return|return
operator|(
name|UnicodeEscaper
operator|)
name|escaper
return|;
block|}
elseif|else
if|if
condition|(
name|escaper
operator|instanceof
name|CharEscaper
condition|)
block|{
return|return
name|wrap
argument_list|(
operator|(
name|CharEscaper
operator|)
name|escaper
argument_list|)
return|;
block|}
comment|// In practice this shouldn't happen because it would be very odd not to
comment|// extend either CharEscaper or UnicodeEscaper for non trivial cases.
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot create a UnicodeEscaper from: "
operator|+
name|escaper
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
comment|/**    * Returns a string that would replace the given character in the specified escaper, or    * {@code null} if no replacement should be made. This method is intended for use in tests through    * the {@code EscaperAsserts} class; production users of {@link CharEscaper} should limit    * themselves to its public interface.    *    * @param c the character to escape if necessary    * @return the replacement string, or {@code null} if no escaping was needed    */
DECL|method|computeReplacement (CharEscaper escaper, char c)
specifier|public
specifier|static
name|String
name|computeReplacement
parameter_list|(
name|CharEscaper
name|escaper
parameter_list|,
name|char
name|c
parameter_list|)
block|{
return|return
name|stringOrNull
argument_list|(
name|escaper
operator|.
name|escape
argument_list|(
name|c
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a string that would replace the given character in the specified escaper, or    * {@code null} if no replacement should be made. This method is intended for use in tests through    * the {@code EscaperAsserts} class; production users of {@link UnicodeEscaper} should limit    * themselves to its public interface.    *    * @param cp the Unicode code point to escape if necessary    * @return the replacement string, or {@code null} if no escaping was needed    */
DECL|method|computeReplacement (UnicodeEscaper escaper, int cp)
specifier|public
specifier|static
name|String
name|computeReplacement
parameter_list|(
name|UnicodeEscaper
name|escaper
parameter_list|,
name|int
name|cp
parameter_list|)
block|{
return|return
name|stringOrNull
argument_list|(
name|escaper
operator|.
name|escape
argument_list|(
name|cp
argument_list|)
argument_list|)
return|;
block|}
DECL|method|stringOrNull (char[] in)
specifier|private
specifier|static
name|String
name|stringOrNull
parameter_list|(
name|char
index|[]
name|in
parameter_list|)
block|{
return|return
operator|(
name|in
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
operator|new
name|String
argument_list|(
name|in
argument_list|)
return|;
block|}
comment|/** Private helper to wrap a CharEscaper as a UnicodeEscaper. */
DECL|method|wrap (final CharEscaper escaper)
specifier|private
specifier|static
name|UnicodeEscaper
name|wrap
parameter_list|(
specifier|final
name|CharEscaper
name|escaper
parameter_list|)
block|{
return|return
operator|new
name|UnicodeEscaper
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|char
index|[]
name|escape
parameter_list|(
name|int
name|cp
parameter_list|)
block|{
comment|// If a code point maps to a single character, just escape that.
if|if
condition|(
name|cp
operator|<
name|Character
operator|.
name|MIN_SUPPLEMENTARY_CODE_POINT
condition|)
block|{
return|return
name|escaper
operator|.
name|escape
argument_list|(
operator|(
name|char
operator|)
name|cp
argument_list|)
return|;
block|}
comment|// Convert the code point to a surrogate pair and escape them both.
comment|// Note: This code path is horribly slow and typically allocates 4 new
comment|// char[] each time it is invoked. However this avoids any
comment|// synchronization issues and makes the escaper thread safe.
name|char
index|[]
name|surrogateChars
init|=
operator|new
name|char
index|[
literal|2
index|]
decl_stmt|;
name|Character
operator|.
name|toChars
argument_list|(
name|cp
argument_list|,
name|surrogateChars
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|char
index|[]
name|hiChars
init|=
name|escaper
operator|.
name|escape
argument_list|(
name|surrogateChars
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|char
index|[]
name|loChars
init|=
name|escaper
operator|.
name|escape
argument_list|(
name|surrogateChars
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
comment|// If either hiChars or lowChars are non-null, the CharEscaper is trying
comment|// to escape the characters of a surrogate pair separately. This is
comment|// uncommon and applies only to escapers that assume UCS-2 rather than
comment|// UTF-16. See: http://en.wikipedia.org/wiki/UTF-16/UCS-2
if|if
condition|(
name|hiChars
operator|==
literal|null
operator|&&
name|loChars
operator|==
literal|null
condition|)
block|{
comment|// We expect this to be the common code path for most escapers.
return|return
literal|null
return|;
block|}
comment|// Combine the characters and/or escaped sequences into a single array.
name|int
name|hiCount
init|=
name|hiChars
operator|!=
literal|null
condition|?
name|hiChars
operator|.
name|length
else|:
literal|1
decl_stmt|;
name|int
name|loCount
init|=
name|loChars
operator|!=
literal|null
condition|?
name|loChars
operator|.
name|length
else|:
literal|1
decl_stmt|;
name|char
index|[]
name|output
init|=
operator|new
name|char
index|[
name|hiCount
operator|+
name|loCount
index|]
decl_stmt|;
if|if
condition|(
name|hiChars
operator|!=
literal|null
condition|)
block|{
comment|// TODO: Is this faster than System.arraycopy() for small arrays?
for|for
control|(
name|int
name|n
init|=
literal|0
init|;
name|n
operator|<
name|hiChars
operator|.
name|length
condition|;
operator|++
name|n
control|)
block|{
name|output
index|[
name|n
index|]
operator|=
name|hiChars
index|[
name|n
index|]
expr_stmt|;
block|}
block|}
else|else
block|{
name|output
index|[
literal|0
index|]
operator|=
name|surrogateChars
index|[
literal|0
index|]
expr_stmt|;
block|}
if|if
condition|(
name|loChars
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|n
init|=
literal|0
init|;
name|n
operator|<
name|loChars
operator|.
name|length
condition|;
operator|++
name|n
control|)
block|{
name|output
index|[
name|hiCount
operator|+
name|n
index|]
operator|=
name|loChars
index|[
name|n
index|]
expr_stmt|;
block|}
block|}
else|else
block|{
name|output
index|[
name|hiCount
index|]
operator|=
name|surrogateChars
index|[
literal|1
index|]
expr_stmt|;
block|}
return|return
name|output
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

