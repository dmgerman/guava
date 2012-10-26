begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.io
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
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
name|checkArgument
import|;
end_import

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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|math
operator|.
name|IntMath
operator|.
name|divide
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|math
operator|.
name|IntMath
operator|.
name|log2
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|math
operator|.
name|RoundingMode
operator|.
name|CEILING
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|math
operator|.
name|RoundingMode
operator|.
name|UNNECESSARY
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
name|base
operator|.
name|Ascii
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
name|base
operator|.
name|CharMatcher
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
name|base
operator|.
name|Joiner
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
name|base
operator|.
name|Splitter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|CheckReturnValue
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
comment|/**  * A binary encoding scheme for translating between byte sequences and ASCII strings. This class  * includes several constants for encoding schemes specified by<a  * href="http://tools.ietf.org/html/rfc4648">RFC 4648</a>.  *  *<p>All instances of this class are immutable, so they may be stored safely as static constants.  *  * @author Louis Wasserman  * @since 14.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|BaseEncoding
specifier|public
specifier|abstract
class|class
name|BaseEncoding
block|{
DECL|method|BaseEncoding ()
name|BaseEncoding
parameter_list|()
block|{}
comment|/**    * Encodes the specified byte array, and returns the encoded {@code String}.    */
DECL|method|encode (byte[] bytes)
specifier|public
name|String
name|encode
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|encode
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
comment|/**    * Encodes the specified range of the specified byte array, and returns the encoded    * {@code String}.    */
DECL|method|encode (byte[] bytes, int off, int len)
specifier|public
specifier|abstract
name|String
name|encode
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
function_decl|;
comment|// TODO(user): document the extent of leniency, probably after adding ignore(CharMatcher)
comment|/**    * Decodes the specified character sequence, and returns the resulting {@code byte[]}.    * This is the inverse operation to {@link #encode(byte[])}.    *    * @throws IllegalArgumentException if the input is not a valid encoded string according to this    *         encoding.    */
DECL|method|decode (CharSequence chars)
specifier|public
specifier|abstract
name|byte
index|[]
name|decode
parameter_list|(
name|CharSequence
name|chars
parameter_list|)
function_decl|;
comment|/**    * Returns an encoding that behaves equivalently to this encoding, but omits any padding    * characters as specified by<a href="http://tools.ietf.org/html/rfc4648#section-3.2">RFC 4648    * section 3.2</a>, Padding of Encoded Data.    */
annotation|@
name|CheckReturnValue
DECL|method|omitPadding ()
specifier|public
specifier|abstract
name|BaseEncoding
name|omitPadding
parameter_list|()
function_decl|;
comment|/**    * Returns an encoding that behaves equivalently to this encoding, but uses an alternate character    * for padding.    *    * @throws IllegalArgumentException if this padding character is already used in the alphabet or a    *         separator    */
annotation|@
name|CheckReturnValue
DECL|method|withPadChar (char padChar)
specifier|public
specifier|abstract
name|BaseEncoding
name|withPadChar
parameter_list|(
name|char
name|padChar
parameter_list|)
function_decl|;
comment|/**    * Returns an encoding that behaves equivalently to this encoding, but adds a separator string    * after every {@code n} characters. Any occurrences of any characters that occur in the separator    * are skipped over in decoding.    *    * @throws IllegalArgumentException if any alphabet or padding characters appear in the separator    *         string, or if {@code n<= 0}    * @throws UnsupportedOperationException if this encoding already uses a separator    */
annotation|@
name|CheckReturnValue
DECL|method|withSeparator (String separator, int n)
specifier|public
specifier|abstract
name|BaseEncoding
name|withSeparator
parameter_list|(
name|String
name|separator
parameter_list|,
name|int
name|n
parameter_list|)
function_decl|;
comment|/**    * Returns an encoding that behaves equivalently to this encoding, but encodes and decodes with    * uppercase letters. Padding and separator characters remain in their original case.    *    * @throws IllegalStateException if the alphabet used by this encoding contains mixed upper- and    *         lower-case characters    */
annotation|@
name|CheckReturnValue
DECL|method|upperCase ()
specifier|public
specifier|abstract
name|BaseEncoding
name|upperCase
parameter_list|()
function_decl|;
comment|/**    * Returns an encoding that behaves equivalently to this encoding, but encodes and decodes with    * lowercase letters. Padding and separator characters remain in their original case.    *    * @throws IllegalStateException if the alphabet used by this encoding contains mixed upper- and    *         lower-case characters    */
annotation|@
name|CheckReturnValue
DECL|method|lowerCase ()
specifier|public
specifier|abstract
name|BaseEncoding
name|lowerCase
parameter_list|()
function_decl|;
DECL|field|BASE64
specifier|private
specifier|static
specifier|final
name|BaseEncoding
name|BASE64
init|=
operator|new
name|StandardBaseEncoding
argument_list|(
literal|"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
argument_list|,
literal|'='
argument_list|)
decl_stmt|;
comment|/**    * The "base64" base encoding specified by<a    * href="http://tools.ietf.org/html/rfc4648#section-4">RFC 4648 section 4</a>, Base 64 Encoding.    * (This is the same as the base 64 encoding from<a    * href="http://tools.ietf.org/html/rfc3548#section-3">RFC 3548</a>.)    *    *<p>The character {@code '='} is used for padding, but can be {@linkplain #omitPadding()    * omitted} or {@linkplain #withPadChar(char) replaced}.    *    *<p>No line feeds are added by default, as per<a    * href="http://tools.ietf.org/html/rfc4648#section-3.1"> RFC 4648 section 3.1</a>, Line Feeds in    * Encoded Data. Line feeds may be added using {@link #withSeparator(String, int)}.    */
DECL|method|base64 ()
specifier|public
specifier|static
name|BaseEncoding
name|base64
parameter_list|()
block|{
return|return
name|BASE64
return|;
block|}
DECL|field|BASE64_URL
specifier|private
specifier|static
specifier|final
name|BaseEncoding
name|BASE64_URL
init|=
operator|new
name|StandardBaseEncoding
argument_list|(
literal|"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"
argument_list|,
literal|'='
argument_list|)
decl_stmt|;
comment|/**    * The "base64url" encoding specified by<a    * href="http://tools.ietf.org/html/rfc4648#section-5">RFC 4648 section 5</a>, Base 64 Encoding    * with URL and Filename Safe Alphabet, also sometimes referred to as the "web safe Base64."    * (This is the same as the base 64 encoding with URL and filename safe alphabet from<a    * href="http://tools.ietf.org/html/rfc3548#section-4">RFC 3548</a>.)    *    *<p>The character {@code '='} is used for padding, but can be {@linkplain #omitPadding()    * omitted} or {@linkplain #withPadChar(char) replaced}.    *    *<p>No line feeds are added by default, as per<a    * href="http://tools.ietf.org/html/rfc4648#section-3.1"> RFC 4648 section 3.1</a>, Line Feeds in    * Encoded Data. Line feeds may be added using {@link #withSeparator(String, int)}.    */
DECL|method|base64Url ()
specifier|public
specifier|static
name|BaseEncoding
name|base64Url
parameter_list|()
block|{
return|return
name|BASE64_URL
return|;
block|}
DECL|field|BASE32
specifier|private
specifier|static
specifier|final
name|BaseEncoding
name|BASE32
init|=
operator|new
name|StandardBaseEncoding
argument_list|(
literal|"ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
argument_list|,
literal|'='
argument_list|)
decl_stmt|;
comment|/**    * The "base32" encoding specified by<a    * href="http://tools.ietf.org/html/rfc4648#section-6">RFC 4648 section 6</a>, Base 32 Encoding.    * (This is the same as the base 32 encoding from<a    * href="http://tools.ietf.org/html/rfc3548#section-5">RFC 3548</a>.)    *    *<p>The character {@code '='} is used for padding, but can be {@linkplain #omitPadding()    * omitted} or {@linkplain #withPadChar(char) replaced}.    *    *<p>No line feeds are added by default, as per<a    * href="http://tools.ietf.org/html/rfc4648#section-3.1"> RFC 4648 section 3.1</a>, Line Feeds in    * Encoded Data. Line feeds may be added using {@link #withSeparator(String, int)}.    */
DECL|method|base32 ()
specifier|public
specifier|static
name|BaseEncoding
name|base32
parameter_list|()
block|{
return|return
name|BASE32
return|;
block|}
DECL|field|BASE32_HEX
specifier|private
specifier|static
specifier|final
name|BaseEncoding
name|BASE32_HEX
init|=
operator|new
name|StandardBaseEncoding
argument_list|(
literal|"0123456789ABCDEFGHIJKLMNOPQRSTUV"
argument_list|,
literal|'='
argument_list|)
decl_stmt|;
comment|/**    * The "base32hex" encoding specified by<a    * href="http://tools.ietf.org/html/rfc4648#section-7">RFC 4648 section 7</a>, Base 32 Encoding    * with Extended Hex Alphabet.  There is no corresponding encoding in RFC 3548.    *    *<p>The character {@code '='} is used for padding, but can be {@linkplain #omitPadding()    * omitted} or {@linkplain #withPadChar(char) replaced}.    *    *<p>No line feeds are added by default, as per<a    * href="http://tools.ietf.org/html/rfc4648#section-3.1"> RFC 4648 section 3.1</a>, Line Feeds in    * Encoded Data. Line feeds may be added using {@link #withSeparator(String, int)}.    */
DECL|method|base32Hex ()
specifier|public
specifier|static
name|BaseEncoding
name|base32Hex
parameter_list|()
block|{
return|return
name|BASE32_HEX
return|;
block|}
DECL|field|BASE16
specifier|private
specifier|static
specifier|final
name|BaseEncoding
name|BASE16
init|=
operator|new
name|StandardBaseEncoding
argument_list|(
literal|"0123456789ABCDEF"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|/**    * The "base16" encoding specified by<a    * href="http://tools.ietf.org/html/rfc4648#section-8">RFC 4648 section 8</a>, Base 16 Encoding.    * (This is the same as the base 16 encoding from<a    * href="http://tools.ietf.org/html/rfc3548#section-6">RFC 3548</a>.)    *    *<p>No padding is necessary in base 16, so {@link #withPadChar(char)} and    * {@link #omitPadding()} have no effect.    *    *<p>No line feeds are added by default, as per<a    * href="http://tools.ietf.org/html/rfc4648#section-3.1"> RFC 4648 section 3.1</a>, Line Feeds in    * Encoded Data. Line feeds may be added using {@link #withSeparator(String, int)}.    */
DECL|method|base16 ()
specifier|public
specifier|static
name|BaseEncoding
name|base16
parameter_list|()
block|{
return|return
name|BASE16
return|;
block|}
DECL|class|StandardBaseEncoding
specifier|static
specifier|final
class|class
name|StandardBaseEncoding
extends|extends
name|BaseEncoding
block|{
DECL|field|alphabet
specifier|private
specifier|final
name|String
name|alphabet
decl_stmt|;
DECL|field|alphabetMask
specifier|private
specifier|final
name|int
name|alphabetMask
decl_stmt|;
DECL|field|bitsPerChar
specifier|private
specifier|final
name|int
name|bitsPerChar
decl_stmt|;
DECL|field|charsPerChunk
specifier|private
specifier|final
name|int
name|charsPerChunk
decl_stmt|;
DECL|field|bytesPerChunk
specifier|private
specifier|final
name|int
name|bytesPerChunk
decl_stmt|;
annotation|@
name|Nullable
DECL|field|paddingChar
specifier|private
specifier|final
name|Character
name|paddingChar
decl_stmt|;
DECL|field|decodabet
specifier|private
specifier|final
name|byte
index|[]
name|decodabet
decl_stmt|;
comment|// The lengths mod charsPerChunk that a non-padded encoded string might possibly have.
DECL|field|validPadding
specifier|private
specifier|final
name|boolean
index|[]
name|validPadding
decl_stmt|;
DECL|method|StandardBaseEncoding (String alphabet, @Nullable Character paddingChar)
name|StandardBaseEncoding
parameter_list|(
name|String
name|alphabet
parameter_list|,
annotation|@
name|Nullable
name|Character
name|paddingChar
parameter_list|)
block|{
name|this
operator|.
name|alphabet
operator|=
name|checkNotNull
argument_list|(
name|alphabet
argument_list|)
expr_stmt|;
try|try
block|{
name|this
operator|.
name|bitsPerChar
operator|=
name|log2
argument_list|(
name|alphabet
operator|.
name|length
argument_list|()
argument_list|,
name|UNNECESSARY
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Illegal alphabet length "
operator|+
name|alphabet
operator|.
name|length
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|/*        * e.g. for base64, bitsPerChar == 6, charsPerChunk == 4, and bytesPerChunk == 3. This makes        * for the smallest chunk size that still has charsPerChunk * bitsPerChar be a multiple of 8.        */
name|int
name|gcd
init|=
name|Math
operator|.
name|min
argument_list|(
literal|8
argument_list|,
name|Integer
operator|.
name|lowestOneBit
argument_list|(
name|bitsPerChar
argument_list|)
argument_list|)
decl_stmt|;
name|this
operator|.
name|charsPerChunk
operator|=
literal|8
operator|/
name|gcd
expr_stmt|;
name|this
operator|.
name|bytesPerChunk
operator|=
name|bitsPerChar
operator|/
name|gcd
expr_stmt|;
name|this
operator|.
name|alphabetMask
operator|=
name|alphabet
operator|.
name|length
argument_list|()
operator|-
literal|1
expr_stmt|;
name|this
operator|.
name|paddingChar
operator|=
name|paddingChar
expr_stmt|;
name|checkArgument
argument_list|(
name|paddingChar
operator|==
literal|null
operator|||
name|alphabet
operator|.
name|indexOf
argument_list|(
name|paddingChar
operator|.
name|charValue
argument_list|()
argument_list|)
operator|==
operator|-
literal|1
argument_list|,
literal|"Padding character must not appear in alphabet"
argument_list|)
expr_stmt|;
name|byte
index|[]
name|decodabet
init|=
operator|new
name|byte
index|[
literal|128
index|]
decl_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|decodabet
argument_list|,
operator|(
name|byte
operator|)
operator|-
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|alphabet
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|alphabet
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|decodabet
index|[
name|c
index|]
operator|=
operator|(
name|byte
operator|)
name|i
expr_stmt|;
block|}
name|this
operator|.
name|decodabet
operator|=
name|decodabet
expr_stmt|;
name|boolean
index|[]
name|validPadding
init|=
operator|new
name|boolean
index|[
name|charsPerChunk
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|bytesPerChunk
condition|;
name|i
operator|++
control|)
block|{
name|int
name|chars
init|=
name|divide
argument_list|(
name|i
operator|*
literal|8
argument_list|,
name|bitsPerChar
argument_list|,
name|CEILING
argument_list|)
decl_stmt|;
name|validPadding
index|[
name|chars
index|]
operator|=
literal|true
expr_stmt|;
block|}
name|this
operator|.
name|validPadding
operator|=
name|validPadding
expr_stmt|;
block|}
DECL|method|paddingMatcher ()
specifier|private
name|CharMatcher
name|paddingMatcher
parameter_list|()
block|{
return|return
operator|(
name|paddingChar
operator|==
literal|null
operator|)
condition|?
name|CharMatcher
operator|.
name|NONE
else|:
name|CharMatcher
operator|.
name|is
argument_list|(
name|paddingChar
operator|.
name|charValue
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|encode (byte[] bytes, int off, int len)
specifier|public
name|String
name|encode
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
name|checkNotNull
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|checkPositionIndexes
argument_list|(
name|off
argument_list|,
name|off
operator|+
name|len
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|int
name|outputLength
init|=
name|charsPerChunk
operator|*
name|divide
argument_list|(
name|len
argument_list|,
name|bytesPerChunk
argument_list|,
name|CEILING
argument_list|)
decl_stmt|;
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
name|outputLength
argument_list|)
decl_stmt|;
name|int
name|bitBuffer
init|=
literal|0
decl_stmt|;
name|int
name|bitBufferLength
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|off
init|;
name|i
operator|<
name|off
operator|+
name|len
condition|;
name|i
operator|++
control|)
block|{
name|bitBuffer
operator|<<=
literal|8
expr_stmt|;
name|bitBuffer
operator||=
name|bytes
index|[
name|i
index|]
operator|&
literal|0xFF
expr_stmt|;
name|bitBufferLength
operator|+=
literal|8
expr_stmt|;
while|while
condition|(
name|bitBufferLength
operator|>=
name|bitsPerChar
condition|)
block|{
name|int
name|charIndex
init|=
operator|(
name|bitBuffer
operator|>>>
operator|(
name|bitBufferLength
operator|-
name|bitsPerChar
operator|)
operator|)
operator|&
name|alphabetMask
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|alphabet
operator|.
name|charAt
argument_list|(
name|charIndex
argument_list|)
argument_list|)
expr_stmt|;
name|bitBufferLength
operator|-=
name|bitsPerChar
expr_stmt|;
block|}
block|}
if|if
condition|(
name|bitBufferLength
operator|>
literal|0
condition|)
block|{
name|int
name|charIndex
init|=
operator|(
name|bitBuffer
operator|<<
operator|(
name|bitsPerChar
operator|-
name|bitBufferLength
operator|)
operator|)
operator|&
name|alphabetMask
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|alphabet
operator|.
name|charAt
argument_list|(
name|charIndex
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|paddingChar
operator|!=
literal|null
condition|)
block|{
while|while
condition|(
name|builder
operator|.
name|length
argument_list|()
operator|%
name|charsPerChunk
operator|!=
literal|0
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|paddingChar
operator|.
name|charValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|decode (CharSequence chars)
specifier|public
name|byte
index|[]
name|decode
parameter_list|(
name|CharSequence
name|chars
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|chars
argument_list|)
expr_stmt|;
name|int
name|outputLength
init|=
name|bytesPerChunk
operator|*
name|divide
argument_list|(
name|chars
operator|.
name|length
argument_list|()
argument_list|,
name|charsPerChunk
argument_list|,
name|CEILING
argument_list|)
decl_stmt|;
name|ByteBuffer
name|outputBuffer
init|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
name|outputLength
argument_list|)
decl_stmt|;
name|int
name|bitBuffer
init|=
literal|0
decl_stmt|;
name|int
name|bitBufferLength
init|=
literal|0
decl_stmt|;
name|CharMatcher
name|paddingMatcher
init|=
name|paddingMatcher
argument_list|()
decl_stmt|;
name|boolean
name|hitPadding
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|chars
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|chars
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|paddingMatcher
operator|.
name|matches
argument_list|(
name|c
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|hitPadding
operator|&&
operator|(
name|i
operator|<=
literal|0
operator|||
operator|!
name|validPadding
index|[
name|i
operator|%
name|charsPerChunk
index|]
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Padding cannot start at index "
operator|+
name|i
argument_list|)
throw|;
block|}
name|hitPadding
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|hitPadding
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected padding character but found '"
operator|+
name|c
operator|+
literal|"' at index "
operator|+
name|i
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|CharMatcher
operator|.
name|ASCII
operator|.
name|matches
argument_list|(
name|c
argument_list|)
operator|||
name|decodabet
index|[
name|c
index|]
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unrecognized character: "
operator|+
name|c
argument_list|)
throw|;
block|}
else|else
block|{
name|bitBuffer
operator|<<=
name|bitsPerChar
expr_stmt|;
name|bitBuffer
operator||=
name|decodabet
index|[
name|c
index|]
operator|&
name|alphabetMask
expr_stmt|;
name|bitBufferLength
operator|+=
name|bitsPerChar
expr_stmt|;
if|if
condition|(
name|bitBufferLength
operator|>=
literal|8
condition|)
block|{
name|outputBuffer
operator|.
name|put
argument_list|(
call|(
name|byte
call|)
argument_list|(
name|bitBuffer
operator|>>
operator|(
name|bitBufferLength
operator|-
literal|8
operator|)
argument_list|)
argument_list|)
expr_stmt|;
name|bitBufferLength
operator|-=
literal|8
expr_stmt|;
block|}
block|}
block|}
name|int
name|charsOverChunk
init|=
name|chars
operator|.
name|length
argument_list|()
operator|%
name|charsPerChunk
decl_stmt|;
if|if
condition|(
operator|!
name|hitPadding
operator|&&
operator|!
name|validPadding
index|[
name|charsOverChunk
index|]
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Bad trailing characters \""
operator|+
name|chars
operator|.
name|subSequence
argument_list|(
name|chars
operator|.
name|length
argument_list|()
operator|-
name|charsOverChunk
argument_list|,
name|chars
operator|.
name|length
argument_list|()
argument_list|)
operator|+
literal|"\""
argument_list|)
throw|;
block|}
name|outputBuffer
operator|.
name|flip
argument_list|()
expr_stmt|;
return|return
name|extractBytes
argument_list|(
name|outputBuffer
argument_list|)
return|;
block|}
DECL|method|extractBytes (ByteBuffer buf)
specifier|private
specifier|static
name|byte
index|[]
name|extractBytes
parameter_list|(
name|ByteBuffer
name|buf
parameter_list|)
block|{
if|if
condition|(
name|buf
operator|.
name|hasArray
argument_list|()
operator|&&
name|buf
operator|.
name|arrayOffset
argument_list|()
operator|==
literal|0
operator|&&
name|buf
operator|.
name|position
argument_list|()
operator|==
literal|0
operator|&&
name|buf
operator|.
name|limit
argument_list|()
operator|==
name|buf
operator|.
name|capacity
argument_list|()
condition|)
block|{
return|return
name|buf
operator|.
name|array
argument_list|()
return|;
block|}
else|else
block|{
name|byte
index|[]
name|result
init|=
operator|new
name|byte
index|[
name|buf
operator|.
name|remaining
argument_list|()
index|]
decl_stmt|;
name|buf
operator|.
name|get
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|buf
operator|.
name|position
argument_list|(
name|buf
operator|.
name|position
argument_list|()
operator|-
name|result
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|omitPadding ()
specifier|public
name|BaseEncoding
name|omitPadding
parameter_list|()
block|{
return|return
operator|(
name|paddingChar
operator|==
literal|null
operator|)
condition|?
name|this
else|:
operator|new
name|StandardBaseEncoding
argument_list|(
name|alphabet
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|withPadChar (char padChar)
specifier|public
name|BaseEncoding
name|withPadChar
parameter_list|(
name|char
name|padChar
parameter_list|)
block|{
if|if
condition|(
literal|8
operator|%
name|bitsPerChar
operator|==
literal|0
operator|||
operator|(
name|paddingChar
operator|!=
literal|null
operator|&&
name|paddingChar
operator|.
name|charValue
argument_list|()
operator|==
name|padChar
operator|)
condition|)
block|{
return|return
name|this
return|;
block|}
elseif|else
if|if
condition|(
name|alphabet
operator|.
name|indexOf
argument_list|(
name|padChar
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Padding character '"
operator|+
name|padChar
operator|+
literal|"'appears in alphabet"
argument_list|)
throw|;
block|}
else|else
block|{
return|return
operator|new
name|StandardBaseEncoding
argument_list|(
name|alphabet
argument_list|,
name|padChar
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|withSeparator (String separator, int afterEveryChars)
specifier|public
name|BaseEncoding
name|withSeparator
parameter_list|(
name|String
name|separator
parameter_list|,
name|int
name|afterEveryChars
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|separator
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|paddingMatcher
argument_list|()
operator|.
name|or
argument_list|(
name|CharMatcher
operator|.
name|anyOf
argument_list|(
name|alphabet
argument_list|)
argument_list|)
operator|.
name|matchesNoneOf
argument_list|(
name|separator
argument_list|)
argument_list|,
literal|"Separator cannot contain alphabet or padding characters"
argument_list|)
expr_stmt|;
return|return
operator|new
name|SeparatedBaseEncoding
argument_list|(
name|this
argument_list|,
name|separator
argument_list|,
name|afterEveryChars
argument_list|)
return|;
block|}
DECL|field|ASCII_UPPER_CASE
specifier|private
specifier|static
specifier|final
name|CharMatcher
name|ASCII_UPPER_CASE
init|=
name|CharMatcher
operator|.
name|inRange
argument_list|(
literal|'A'
argument_list|,
literal|'Z'
argument_list|)
decl_stmt|;
DECL|field|ASCII_NOT_UPPER_CASE
specifier|private
specifier|static
specifier|final
name|CharMatcher
name|ASCII_NOT_UPPER_CASE
init|=
name|CharMatcher
operator|.
name|ASCII
operator|.
name|and
argument_list|(
name|ASCII_UPPER_CASE
operator|.
name|negate
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|ASCII_LOWER_CASE
specifier|private
specifier|static
specifier|final
name|CharMatcher
name|ASCII_LOWER_CASE
init|=
name|CharMatcher
operator|.
name|inRange
argument_list|(
literal|'a'
argument_list|,
literal|'z'
argument_list|)
decl_stmt|;
DECL|field|ASCII_NOT_LOWER_CASE
specifier|private
specifier|static
specifier|final
name|CharMatcher
name|ASCII_NOT_LOWER_CASE
init|=
name|CharMatcher
operator|.
name|ASCII
operator|.
name|and
argument_list|(
name|ASCII_LOWER_CASE
operator|.
name|negate
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|upperCase ()
specifier|public
name|BaseEncoding
name|upperCase
parameter_list|()
block|{
if|if
condition|(
name|ASCII_UPPER_CASE
operator|.
name|matchesAnyOf
argument_list|(
name|alphabet
argument_list|)
operator|&&
name|ASCII_LOWER_CASE
operator|.
name|matchesAnyOf
argument_list|(
name|alphabet
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"alphabet uses both upper and lower case characters"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|ASCII_NOT_LOWER_CASE
operator|.
name|matchesAllOf
argument_list|(
name|alphabet
argument_list|)
condition|)
block|{
comment|// already all uppercase or caseless
return|return
name|this
return|;
block|}
else|else
block|{
return|return
operator|new
name|StandardBaseEncoding
argument_list|(
name|Ascii
operator|.
name|toUpperCase
argument_list|(
name|alphabet
argument_list|)
argument_list|,
name|paddingChar
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|lowerCase ()
specifier|public
name|BaseEncoding
name|lowerCase
parameter_list|()
block|{
if|if
condition|(
name|ASCII_UPPER_CASE
operator|.
name|matchesAnyOf
argument_list|(
name|alphabet
argument_list|)
operator|&&
name|ASCII_LOWER_CASE
operator|.
name|matchesAnyOf
argument_list|(
name|alphabet
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"alphabet uses both upper and lower case characters"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|ASCII_NOT_UPPER_CASE
operator|.
name|matchesAllOf
argument_list|(
name|alphabet
argument_list|)
condition|)
block|{
comment|// already all lowercase or caseless
return|return
name|this
return|;
block|}
else|else
block|{
return|return
operator|new
name|StandardBaseEncoding
argument_list|(
name|Ascii
operator|.
name|toLowerCase
argument_list|(
name|alphabet
argument_list|)
argument_list|,
name|paddingChar
argument_list|)
return|;
block|}
block|}
block|}
DECL|class|SeparatedBaseEncoding
specifier|static
specifier|final
class|class
name|SeparatedBaseEncoding
extends|extends
name|BaseEncoding
block|{
DECL|field|delegate
specifier|private
specifier|final
name|BaseEncoding
name|delegate
decl_stmt|;
DECL|field|separator
specifier|private
specifier|final
name|String
name|separator
decl_stmt|;
DECL|field|afterEveryChars
specifier|private
specifier|final
name|int
name|afterEveryChars
decl_stmt|;
DECL|field|separatorChars
specifier|private
specifier|final
name|CharMatcher
name|separatorChars
decl_stmt|;
DECL|method|SeparatedBaseEncoding (BaseEncoding delegate, String separator, int afterEveryChars)
name|SeparatedBaseEncoding
parameter_list|(
name|BaseEncoding
name|delegate
parameter_list|,
name|String
name|separator
parameter_list|,
name|int
name|afterEveryChars
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|separator
operator|=
name|checkNotNull
argument_list|(
name|separator
argument_list|)
expr_stmt|;
name|this
operator|.
name|afterEveryChars
operator|=
name|afterEveryChars
expr_stmt|;
name|checkArgument
argument_list|(
name|afterEveryChars
operator|>
literal|0
argument_list|,
literal|"Cannot add a separator after every %s chars"
argument_list|,
name|afterEveryChars
argument_list|)
expr_stmt|;
name|this
operator|.
name|separatorChars
operator|=
name|CharMatcher
operator|.
name|anyOf
argument_list|(
name|separator
argument_list|)
operator|.
name|precomputed
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|encode (byte[] bytes, int off, int len)
specifier|public
name|String
name|encode
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
comment|// TODO(user): delegate to the streaming implementation when we add it
name|String
name|unseparated
init|=
name|delegate
operator|.
name|encode
argument_list|(
name|bytes
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
decl_stmt|;
return|return
name|Joiner
operator|.
name|on
argument_list|(
name|separator
argument_list|)
operator|.
name|join
argument_list|(
name|Splitter
operator|.
name|fixedLength
argument_list|(
name|afterEveryChars
argument_list|)
operator|.
name|split
argument_list|(
name|unseparated
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|decode (CharSequence chars)
specifier|public
name|byte
index|[]
name|decode
parameter_list|(
name|CharSequence
name|chars
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|decode
argument_list|(
name|separatorChars
operator|.
name|removeFrom
argument_list|(
name|chars
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|omitPadding ()
specifier|public
name|BaseEncoding
name|omitPadding
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|omitPadding
argument_list|()
operator|.
name|withSeparator
argument_list|(
name|separator
argument_list|,
name|afterEveryChars
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|withPadChar (char padChar)
specifier|public
name|BaseEncoding
name|withPadChar
parameter_list|(
name|char
name|padChar
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|withPadChar
argument_list|(
name|padChar
argument_list|)
operator|.
name|withSeparator
argument_list|(
name|separator
argument_list|,
name|afterEveryChars
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|withSeparator (String separator, int afterEveryChars)
specifier|public
name|BaseEncoding
name|withSeparator
parameter_list|(
name|String
name|separator
parameter_list|,
name|int
name|afterEveryChars
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Already have a separator"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|upperCase ()
specifier|public
name|BaseEncoding
name|upperCase
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|upperCase
argument_list|()
operator|.
name|withSeparator
argument_list|(
name|separator
argument_list|,
name|afterEveryChars
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|lowerCase ()
specifier|public
name|BaseEncoding
name|lowerCase
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|lowerCase
argument_list|()
operator|.
name|withSeparator
argument_list|(
name|separator
argument_list|,
name|afterEveryChars
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

