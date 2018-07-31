begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.net
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|net
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
name|common
operator|.
name|escape
operator|.
name|UnicodeEscaper
import|;
end_import

begin_comment
comment|/**  * A {@code UnicodeEscaper} that escapes some set of Java characters using a UTF-8 based percent  * encoding scheme. The set of safe characters (those which remain unescaped) can be specified on  * construction.  *  *<p>This class is primarily used for creating URI escapers in {@link UrlEscapers} but can be used  * directly if required. While URI escapers impose specific semantics on which characters are  * considered 'safe', this class has a minimal set of restrictions.  *  *<p>When escaping a String, the following rules apply:  *  *<ul>  *<li>All specified safe characters remain unchanged.  *<li>If {@code plusForSpace} was specified, the space character " " is converted into a plus  *       sign {@code "+"}.  *<li>All other characters are converted into one or more bytes using UTF-8 encoding and each  *       byte is then represented by the 3-character string "%XX", where "XX" is the two-digit,  *       uppercase, hexadecimal representation of the byte value.  *</ul>  *  *<p>For performance reasons the only currently supported character encoding of this class is  * UTF-8.  *  *<p><b>Note:</b> This escaper produces<a  * href="https://url.spec.whatwg.org/#percent-encode">uppercase</a> hexadecimal sequences.  *  * @author David Beaumont  * @since 15.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|PercentEscaper
specifier|public
specifier|final
class|class
name|PercentEscaper
extends|extends
name|UnicodeEscaper
block|{
comment|// In some escapers spaces are escaped to '+'
DECL|field|PLUS_SIGN
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|PLUS_SIGN
init|=
block|{
literal|'+'
block|}
decl_stmt|;
comment|// Percent escapers output upper case hex digits (uri escapers require this).
DECL|field|UPPER_HEX_DIGITS
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|UPPER_HEX_DIGITS
init|=
literal|"0123456789ABCDEF"
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
comment|/** If true we should convert space to the {@code +} character. */
DECL|field|plusForSpace
specifier|private
specifier|final
name|boolean
name|plusForSpace
decl_stmt|;
comment|/**    * An array of flags where for any {@code char c} if {@code safeOctets[c]} is true then {@code c}    * should remain unmodified in the output. If {@code c> safeOctets.length} then it should be    * escaped.    */
DECL|field|safeOctets
specifier|private
specifier|final
name|boolean
index|[]
name|safeOctets
decl_stmt|;
comment|/**    * Constructs a percent escaper with the specified safe characters and optional handling of the    * space character.    *    *<p>Not that it is allowed, but not necessarily desirable to specify {@code %} as a safe    * character. This has the effect of creating an escaper which has no well defined inverse but it    * can be useful when escaping additional characters.    *    * @param safeChars a non null string specifying additional safe characters for this escaper (the    *     ranges 0..9, a..z and A..Z are always safe and should not be specified here)    * @param plusForSpace true if ASCII space should be escaped to {@code +} rather than {@code %20}    * @throws IllegalArgumentException if any of the parameters were invalid    */
DECL|method|PercentEscaper (String safeChars, boolean plusForSpace)
specifier|public
name|PercentEscaper
parameter_list|(
name|String
name|safeChars
parameter_list|,
name|boolean
name|plusForSpace
parameter_list|)
block|{
comment|// TODO(dbeaumont): Switch to static factory methods for creation now that class is final.
comment|// TODO(dbeaumont): Support escapers where alphanumeric chars are not safe.
name|checkNotNull
argument_list|(
name|safeChars
argument_list|)
expr_stmt|;
comment|// eager for GWT.
comment|// Avoid any misunderstandings about the behavior of this escaper
if|if
condition|(
name|safeChars
operator|.
name|matches
argument_list|(
literal|".*[0-9A-Za-z].*"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Alphanumeric characters are always 'safe' and should not be explicitly specified"
argument_list|)
throw|;
block|}
name|safeChars
operator|+=
literal|"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
expr_stmt|;
comment|// Avoid ambiguous parameters. Safe characters are never modified so if
comment|// space is a safe character then setting plusForSpace is meaningless.
if|if
condition|(
name|plusForSpace
operator|&&
name|safeChars
operator|.
name|contains
argument_list|(
literal|" "
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"plusForSpace cannot be specified when space is a 'safe' character"
argument_list|)
throw|;
block|}
name|this
operator|.
name|plusForSpace
operator|=
name|plusForSpace
expr_stmt|;
name|this
operator|.
name|safeOctets
operator|=
name|createSafeOctets
argument_list|(
name|safeChars
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates a boolean array with entries corresponding to the character values specified in    * safeChars set to true. The array is as small as is required to hold the given character    * information.    */
DECL|method|createSafeOctets (String safeChars)
specifier|private
specifier|static
name|boolean
index|[]
name|createSafeOctets
parameter_list|(
name|String
name|safeChars
parameter_list|)
block|{
name|int
name|maxChar
init|=
operator|-
literal|1
decl_stmt|;
name|char
index|[]
name|safeCharArray
init|=
name|safeChars
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
for|for
control|(
name|char
name|c
range|:
name|safeCharArray
control|)
block|{
name|maxChar
operator|=
name|Math
operator|.
name|max
argument_list|(
name|c
argument_list|,
name|maxChar
argument_list|)
expr_stmt|;
block|}
name|boolean
index|[]
name|octets
init|=
operator|new
name|boolean
index|[
name|maxChar
operator|+
literal|1
index|]
decl_stmt|;
for|for
control|(
name|char
name|c
range|:
name|safeCharArray
control|)
block|{
name|octets
index|[
name|c
index|]
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|octets
return|;
block|}
comment|/*    * Overridden for performance. For unescaped strings this improved the performance of the uri    * escaper from ~760ns to ~400ns as measured by {@link CharEscapersBenchmark}.    */
annotation|@
name|Override
DECL|method|nextEscapeIndex (CharSequence csq, int index, int end)
specifier|protected
name|int
name|nextEscapeIndex
parameter_list|(
name|CharSequence
name|csq
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
name|csq
argument_list|)
expr_stmt|;
for|for
control|(
init|;
name|index
operator|<
name|end
condition|;
name|index
operator|++
control|)
block|{
name|char
name|c
init|=
name|csq
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|>=
name|safeOctets
operator|.
name|length
operator|||
operator|!
name|safeOctets
index|[
name|c
index|]
condition|)
block|{
break|break;
block|}
block|}
return|return
name|index
return|;
block|}
comment|/*    * Overridden for performance. For unescaped strings this improved the performance of the uri    * escaper from ~400ns to ~170ns as measured by {@link CharEscapersBenchmark}.    */
annotation|@
name|Override
DECL|method|escape (String s)
specifier|public
name|String
name|escape
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|int
name|slen
init|=
name|s
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
name|slen
condition|;
name|index
operator|++
control|)
block|{
name|char
name|c
init|=
name|s
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|>=
name|safeOctets
operator|.
name|length
operator|||
operator|!
name|safeOctets
index|[
name|c
index|]
condition|)
block|{
return|return
name|escapeSlow
argument_list|(
name|s
argument_list|,
name|index
argument_list|)
return|;
block|}
block|}
return|return
name|s
return|;
block|}
comment|/** Escapes the given Unicode code point in UTF-8. */
annotation|@
name|Override
DECL|method|escape (int cp)
specifier|protected
name|char
index|[]
name|escape
parameter_list|(
name|int
name|cp
parameter_list|)
block|{
comment|// We should never get negative values here but if we do it will throw an
comment|// IndexOutOfBoundsException, so at least it will get spotted.
if|if
condition|(
name|cp
operator|<
name|safeOctets
operator|.
name|length
operator|&&
name|safeOctets
index|[
name|cp
index|]
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|cp
operator|==
literal|' '
operator|&&
name|plusForSpace
condition|)
block|{
return|return
name|PLUS_SIGN
return|;
block|}
elseif|else
if|if
condition|(
name|cp
operator|<=
literal|0x7F
condition|)
block|{
comment|// Single byte UTF-8 characters
comment|// Start with "%--" and fill in the blanks
name|char
index|[]
name|dest
init|=
operator|new
name|char
index|[
literal|3
index|]
decl_stmt|;
name|dest
index|[
literal|0
index|]
operator|=
literal|'%'
expr_stmt|;
name|dest
index|[
literal|2
index|]
operator|=
name|UPPER_HEX_DIGITS
index|[
name|cp
operator|&
literal|0xF
index|]
expr_stmt|;
name|dest
index|[
literal|1
index|]
operator|=
name|UPPER_HEX_DIGITS
index|[
name|cp
operator|>>>
literal|4
index|]
expr_stmt|;
return|return
name|dest
return|;
block|}
elseif|else
if|if
condition|(
name|cp
operator|<=
literal|0x7ff
condition|)
block|{
comment|// Two byte UTF-8 characters [cp>= 0x80&& cp<= 0x7ff]
comment|// Start with "%--%--" and fill in the blanks
name|char
index|[]
name|dest
init|=
operator|new
name|char
index|[
literal|6
index|]
decl_stmt|;
name|dest
index|[
literal|0
index|]
operator|=
literal|'%'
expr_stmt|;
name|dest
index|[
literal|3
index|]
operator|=
literal|'%'
expr_stmt|;
name|dest
index|[
literal|5
index|]
operator|=
name|UPPER_HEX_DIGITS
index|[
name|cp
operator|&
literal|0xF
index|]
expr_stmt|;
name|cp
operator|>>>=
literal|4
expr_stmt|;
name|dest
index|[
literal|4
index|]
operator|=
name|UPPER_HEX_DIGITS
index|[
literal|0x8
operator||
operator|(
name|cp
operator|&
literal|0x3
operator|)
index|]
expr_stmt|;
name|cp
operator|>>>=
literal|2
expr_stmt|;
name|dest
index|[
literal|2
index|]
operator|=
name|UPPER_HEX_DIGITS
index|[
name|cp
operator|&
literal|0xF
index|]
expr_stmt|;
name|cp
operator|>>>=
literal|4
expr_stmt|;
name|dest
index|[
literal|1
index|]
operator|=
name|UPPER_HEX_DIGITS
index|[
literal|0xC
operator||
name|cp
index|]
expr_stmt|;
return|return
name|dest
return|;
block|}
elseif|else
if|if
condition|(
name|cp
operator|<=
literal|0xffff
condition|)
block|{
comment|// Three byte UTF-8 characters [cp>= 0x800&& cp<= 0xffff]
comment|// Start with "%E-%--%--" and fill in the blanks
name|char
index|[]
name|dest
init|=
operator|new
name|char
index|[
literal|9
index|]
decl_stmt|;
name|dest
index|[
literal|0
index|]
operator|=
literal|'%'
expr_stmt|;
name|dest
index|[
literal|1
index|]
operator|=
literal|'E'
expr_stmt|;
name|dest
index|[
literal|3
index|]
operator|=
literal|'%'
expr_stmt|;
name|dest
index|[
literal|6
index|]
operator|=
literal|'%'
expr_stmt|;
name|dest
index|[
literal|8
index|]
operator|=
name|UPPER_HEX_DIGITS
index|[
name|cp
operator|&
literal|0xF
index|]
expr_stmt|;
name|cp
operator|>>>=
literal|4
expr_stmt|;
name|dest
index|[
literal|7
index|]
operator|=
name|UPPER_HEX_DIGITS
index|[
literal|0x8
operator||
operator|(
name|cp
operator|&
literal|0x3
operator|)
index|]
expr_stmt|;
name|cp
operator|>>>=
literal|2
expr_stmt|;
name|dest
index|[
literal|5
index|]
operator|=
name|UPPER_HEX_DIGITS
index|[
name|cp
operator|&
literal|0xF
index|]
expr_stmt|;
name|cp
operator|>>>=
literal|4
expr_stmt|;
name|dest
index|[
literal|4
index|]
operator|=
name|UPPER_HEX_DIGITS
index|[
literal|0x8
operator||
operator|(
name|cp
operator|&
literal|0x3
operator|)
index|]
expr_stmt|;
name|cp
operator|>>>=
literal|2
expr_stmt|;
name|dest
index|[
literal|2
index|]
operator|=
name|UPPER_HEX_DIGITS
index|[
name|cp
index|]
expr_stmt|;
return|return
name|dest
return|;
block|}
elseif|else
if|if
condition|(
name|cp
operator|<=
literal|0x10ffff
condition|)
block|{
name|char
index|[]
name|dest
init|=
operator|new
name|char
index|[
literal|12
index|]
decl_stmt|;
comment|// Four byte UTF-8 characters [cp>= 0xffff&& cp<= 0x10ffff]
comment|// Start with "%F-%--%--%--" and fill in the blanks
name|dest
index|[
literal|0
index|]
operator|=
literal|'%'
expr_stmt|;
name|dest
index|[
literal|1
index|]
operator|=
literal|'F'
expr_stmt|;
name|dest
index|[
literal|3
index|]
operator|=
literal|'%'
expr_stmt|;
name|dest
index|[
literal|6
index|]
operator|=
literal|'%'
expr_stmt|;
name|dest
index|[
literal|9
index|]
operator|=
literal|'%'
expr_stmt|;
name|dest
index|[
literal|11
index|]
operator|=
name|UPPER_HEX_DIGITS
index|[
name|cp
operator|&
literal|0xF
index|]
expr_stmt|;
name|cp
operator|>>>=
literal|4
expr_stmt|;
name|dest
index|[
literal|10
index|]
operator|=
name|UPPER_HEX_DIGITS
index|[
literal|0x8
operator||
operator|(
name|cp
operator|&
literal|0x3
operator|)
index|]
expr_stmt|;
name|cp
operator|>>>=
literal|2
expr_stmt|;
name|dest
index|[
literal|8
index|]
operator|=
name|UPPER_HEX_DIGITS
index|[
name|cp
operator|&
literal|0xF
index|]
expr_stmt|;
name|cp
operator|>>>=
literal|4
expr_stmt|;
name|dest
index|[
literal|7
index|]
operator|=
name|UPPER_HEX_DIGITS
index|[
literal|0x8
operator||
operator|(
name|cp
operator|&
literal|0x3
operator|)
index|]
expr_stmt|;
name|cp
operator|>>>=
literal|2
expr_stmt|;
name|dest
index|[
literal|5
index|]
operator|=
name|UPPER_HEX_DIGITS
index|[
name|cp
operator|&
literal|0xF
index|]
expr_stmt|;
name|cp
operator|>>>=
literal|4
expr_stmt|;
name|dest
index|[
literal|4
index|]
operator|=
name|UPPER_HEX_DIGITS
index|[
literal|0x8
operator||
operator|(
name|cp
operator|&
literal|0x3
operator|)
index|]
expr_stmt|;
name|cp
operator|>>>=
literal|2
expr_stmt|;
name|dest
index|[
literal|2
index|]
operator|=
name|UPPER_HEX_DIGITS
index|[
name|cp
operator|&
literal|0x7
index|]
expr_stmt|;
return|return
name|dest
return|;
block|}
else|else
block|{
comment|// If this ever happens it is due to bug in UnicodeEscaper, not bad input.
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid unicode character value "
operator|+
name|cp
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

