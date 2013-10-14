begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|checkPositionIndexes
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
comment|/**  * Low-level, high-performance utility methods related to the {@linkplain Charsets#UTF_8 UTF-8}  * character encoding. UTF-8 is defined in section D92 of  *<a href="http://www.unicode.org/versions/Unicode6.2.0/ch03.pdf">The Unicode Standard Core  * Specification, Chapter 3</a>.  *  *<p>The variant of UTF-8 implemented by this class is the restricted definition of UTF-8  * introduced in Unicode 3.1. One implication of this is that it rejects  *<a href="http://www.unicode.org/versions/corrigendum1.html">"non-shortest form"</a> byte  * sequences, even though the JDK decoder may accept them.  *  * @author Martin Buchholz  * @author ClÃ©ment Roux  * @since 16.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|Utf8
specifier|public
specifier|final
class|class
name|Utf8
block|{
comment|/**    * Returns the number of bytes in the UTF-8-encoded form of {@code sequence}. For a string,    * this method is equivalent to {@code string.getBytes(UTF_8).length}, but is more efficient in    * both time and space.    *    * @throws IllegalArgumentException if {@code sequence} contains ill-formed UTF-16 (unpaired    *     surrogates)    */
DECL|method|encodedLength (CharSequence sequence)
specifier|public
specifier|static
name|int
name|encodedLength
parameter_list|(
name|CharSequence
name|sequence
parameter_list|)
block|{
name|long
name|utf8Length
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|charIndex
init|=
literal|0
init|;
name|charIndex
operator|<
name|sequence
operator|.
name|length
argument_list|()
condition|;
name|charIndex
operator|++
control|)
block|{
name|char
name|c
init|=
name|sequence
operator|.
name|charAt
argument_list|(
name|charIndex
argument_list|)
decl_stmt|;
comment|// From http://en.wikipedia.org/wiki/UTF-8#Description
if|if
condition|(
name|c
operator|<
literal|0x80
condition|)
block|{
name|utf8Length
operator|+=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|<
literal|0x800
condition|)
block|{
name|utf8Length
operator|+=
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|<
name|Character
operator|.
name|MIN_SURROGATE
operator|||
name|Character
operator|.
name|MAX_SURROGATE
operator|<
name|c
condition|)
block|{
name|utf8Length
operator|+=
literal|3
expr_stmt|;
block|}
else|else
block|{
comment|// Expect a surrogate pair.
name|int
name|cp
init|=
name|Character
operator|.
name|codePointAt
argument_list|(
name|sequence
argument_list|,
name|charIndex
argument_list|)
decl_stmt|;
if|if
condition|(
name|cp
operator|<
name|Character
operator|.
name|MIN_SUPPLEMENTARY_CODE_POINT
condition|)
block|{
comment|// The pair starts with a low surrogate, or no character follows the high surrogate.
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unpaired surrogate at index "
operator|+
name|charIndex
argument_list|)
throw|;
block|}
name|utf8Length
operator|+=
literal|4
expr_stmt|;
name|charIndex
operator|++
expr_stmt|;
block|}
block|}
comment|// return Ints.checkedCast(utf8Length);
name|int
name|result
init|=
operator|(
name|int
operator|)
name|utf8Length
decl_stmt|;
if|if
condition|(
name|result
operator|!=
name|utf8Length
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"UTF-8 length does not fit in int: "
operator|+
name|utf8Length
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
comment|/**    * Returns {@code true} if {@code bytes} is a<i>well-formed</i> UTF-8 byte sequence according to    * Unicode 6.0. Note that this is a stronger criterion than simply whether the bytes can be    * decoded. For example, some versions of the JDK decoder will accept "non-shortest form" byte    * sequences, but encoding never reproduces these. Such byte sequences are<i>not</i> considered    * well-formed.    *    *<p>This method returns {@code true} if and only if {@code Arrays.equals(bytes, new    * String(bytes, UTF_8).getBytes(UTF_8))} does, but is more efficient in both time and space.    */
DECL|method|isWellFormed (byte[] bytes)
specifier|public
specifier|static
name|boolean
name|isWellFormed
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|isWellFormed
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
return|;
block|}
comment|/**    * Returns whether the given byte array slice is a well-formed UTF-8 byte sequence, as defined by    * {@link #isWellFormed(byte[]). Note that this can be false even when {@code isWellFormed(bytes)}    * is true.    *    * @param bytes the input buffer    * @param off the offset in the buffer of the first byte to read    * @param len the number of bytes to read from the buffer    */
DECL|method|isWellFormed (byte[] bytes, int off, int len)
specifier|public
specifier|static
name|boolean
name|isWellFormed
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
name|int
name|end
init|=
name|off
operator|+
name|len
decl_stmt|;
name|checkPositionIndexes
argument_list|(
name|off
argument_list|,
name|end
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// Look for the first non-ASCII character.
for|for
control|(
name|int
name|i
init|=
name|off
init|;
name|i
operator|<
name|end
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|bytes
index|[
name|i
index|]
operator|<
literal|0
condition|)
block|{
return|return
name|isWellFormedSlowPath
argument_list|(
name|bytes
argument_list|,
name|i
argument_list|,
name|end
argument_list|)
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|isWellFormedSlowPath (byte[] bytes, int off, int end)
specifier|private
specifier|static
name|boolean
name|isWellFormedSlowPath
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|end
parameter_list|)
block|{
name|int
name|index
init|=
name|off
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|byte1
decl_stmt|;
comment|// Optimize for interior runs of ASCII bytes.
do|do
block|{
if|if
condition|(
name|index
operator|>=
name|end
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
do|while
condition|(
operator|(
name|byte1
operator|=
name|bytes
index|[
name|index
operator|++
index|]
operator|)
operator|>=
literal|0
condition|)
do|;
if|if
condition|(
name|byte1
operator|<
operator|(
name|byte
operator|)
literal|0xE0
condition|)
block|{
comment|// Two-byte form.
if|if
condition|(
name|index
operator|==
name|end
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Simultaneously check for illegal trailing-byte in leading position
comment|// and overlong 2-byte form.
if|if
condition|(
name|byte1
argument_list|<
operator|(
name|byte
operator|)
literal|0xC2
operator|||
name|bytes
index|[
name|index
operator|++
index|]
argument_list|>
argument_list|(
name|byte
argument_list|)
literal|0xBF
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|byte1
operator|<
operator|(
name|byte
operator|)
literal|0xF0
condition|)
block|{
comment|// Three-byte form.
if|if
condition|(
name|index
operator|+
literal|1
operator|>=
name|end
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|byte2
init|=
name|bytes
index|[
name|index
operator|++
index|]
decl_stmt|;
if|if
condition|(
name|byte2
operator|>
operator|(
name|byte
operator|)
literal|0xBF
comment|// Overlong? 5 most significant bits must not all be zero.
operator|||
operator|(
name|byte1
operator|==
operator|(
name|byte
operator|)
literal|0xE0
operator|&&
name|byte2
operator|<
operator|(
name|byte
operator|)
literal|0xA0
operator|)
comment|// Check for illegal surrogate codepoints.
operator|||
operator|(
name|byte1
operator|==
operator|(
name|byte
operator|)
literal|0xED
operator|&&
operator|(
name|byte
operator|)
literal|0xA0
operator|<=
name|byte2
operator|)
comment|// Third byte trailing-byte test.
operator|||
name|bytes
index|[
name|index
operator|++
index|]
operator|>
operator|(
name|byte
operator|)
literal|0xBF
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
comment|// Four-byte form.
if|if
condition|(
name|index
operator|+
literal|2
operator|>=
name|end
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|byte2
init|=
name|bytes
index|[
name|index
operator|++
index|]
decl_stmt|;
if|if
condition|(
name|byte2
operator|>
operator|(
name|byte
operator|)
literal|0xBF
comment|// Check that 1<= plane<= 16. Tricky optimized form of:
comment|// if (byte1> (byte) 0xF4
comment|//     || byte1 == (byte) 0xF0&& byte2< (byte) 0x90
comment|//     || byte1 == (byte) 0xF4&& byte2> (byte) 0x8F)
operator|||
operator|(
operator|(
operator|(
name|byte1
operator|<<
literal|28
operator|)
operator|+
operator|(
name|byte2
operator|-
operator|(
name|byte
operator|)
literal|0x90
operator|)
operator|)
operator|>>
literal|30
operator|)
operator|!=
literal|0
comment|// Third byte trailing-byte test
operator|||
name|bytes
index|[
name|index
operator|++
index|]
operator|>
operator|(
name|byte
operator|)
literal|0xBF
comment|// Fourth byte trailing-byte test
operator|||
name|bytes
index|[
name|index
operator|++
index|]
operator|>
operator|(
name|byte
operator|)
literal|0xBF
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
DECL|method|Utf8 ()
specifier|private
name|Utf8
parameter_list|()
block|{}
block|}
end_class

end_unit

