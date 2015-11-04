begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|UnsignedBytes
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|Buffer
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
name|nio
operator|.
name|CharBuffer
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

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|CharsetEncoder
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
name|CoderResult
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
name|CodingErrorAction
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

begin_comment
comment|/**  * An {@link InputStream} that converts characters from a {@link Reader} into bytes using an  * arbitrary Charset.  *  *<p>This is an alternative to copying the data to an {@code OutputStream} via a {@code Writer},  * which is necessarily blocking. By implementing an {@code InputStream} it allows consumers to  * "pull" as much data as they can handle, which is more convenient when dealing with flow  * controlled, async APIs.  *  * @author Chris Nokleberg  */
end_comment

begin_class
DECL|class|ReaderInputStream
specifier|final
class|class
name|ReaderInputStream
extends|extends
name|InputStream
block|{
DECL|field|reader
specifier|private
specifier|final
name|Reader
name|reader
decl_stmt|;
DECL|field|encoder
specifier|private
specifier|final
name|CharsetEncoder
name|encoder
decl_stmt|;
DECL|field|singleByte
specifier|private
specifier|final
name|byte
index|[]
name|singleByte
init|=
operator|new
name|byte
index|[
literal|1
index|]
decl_stmt|;
comment|/**    * charBuffer holds characters that have been read from the Reader but not encoded yet. The buffer    * is perpetually "flipped" (unencoded characters between position and limit).    */
DECL|field|charBuffer
specifier|private
name|CharBuffer
name|charBuffer
decl_stmt|;
comment|/**    * byteBuffer holds encoded characters that have not yet been sent to the caller of the input    * stream. When encoding it is "unflipped" (encoded bytes between 0 and position) and when    * draining it is flipped (undrained bytes between position and limit).    */
DECL|field|byteBuffer
specifier|private
name|ByteBuffer
name|byteBuffer
decl_stmt|;
comment|/** Whether we've finished reading the reader. */
DECL|field|endOfInput
specifier|private
name|boolean
name|endOfInput
decl_stmt|;
comment|/** Whether we're copying encoded bytes to the caller's buffer. */
DECL|field|draining
specifier|private
name|boolean
name|draining
decl_stmt|;
comment|/** Whether we've successfully flushed the encoder. */
DECL|field|doneFlushing
specifier|private
name|boolean
name|doneFlushing
decl_stmt|;
comment|/**    * Creates a new input stream that will encode the characters from {@code reader} into bytes using    * the given character set. Malformed input and unmappable characters will be replaced.    *    * @param reader input source    * @param charset character set used for encoding chars to bytes    * @param bufferSize size of internal input and output buffers    * @throws IllegalArgumentException if bufferSize is non-positive    */
DECL|method|ReaderInputStream (Reader reader, Charset charset, int bufferSize)
name|ReaderInputStream
parameter_list|(
name|Reader
name|reader
parameter_list|,
name|Charset
name|charset
parameter_list|,
name|int
name|bufferSize
parameter_list|)
block|{
name|this
argument_list|(
name|reader
argument_list|,
name|charset
operator|.
name|newEncoder
argument_list|()
operator|.
name|onMalformedInput
argument_list|(
name|CodingErrorAction
operator|.
name|REPLACE
argument_list|)
operator|.
name|onUnmappableCharacter
argument_list|(
name|CodingErrorAction
operator|.
name|REPLACE
argument_list|)
argument_list|,
name|bufferSize
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates a new input stream that will encode the characters from {@code reader} into bytes using    * the given character set encoder.    *    * @param reader input source    * @param encoder character set encoder used for encoding chars to bytes    * @param bufferSize size of internal input and output buffers    * @throws IllegalArgumentException if bufferSize is non-positive    */
DECL|method|ReaderInputStream (Reader reader, CharsetEncoder encoder, int bufferSize)
name|ReaderInputStream
parameter_list|(
name|Reader
name|reader
parameter_list|,
name|CharsetEncoder
name|encoder
parameter_list|,
name|int
name|bufferSize
parameter_list|)
block|{
name|this
operator|.
name|reader
operator|=
name|checkNotNull
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|this
operator|.
name|encoder
operator|=
name|checkNotNull
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|bufferSize
operator|>
literal|0
argument_list|,
literal|"bufferSize must be positive: %s"
argument_list|,
name|bufferSize
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|reset
argument_list|()
expr_stmt|;
name|charBuffer
operator|=
name|CharBuffer
operator|.
name|allocate
argument_list|(
name|bufferSize
argument_list|)
expr_stmt|;
name|charBuffer
operator|.
name|flip
argument_list|()
expr_stmt|;
name|byteBuffer
operator|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
name|bufferSize
argument_list|)
expr_stmt|;
block|}
DECL|method|close ()
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|read ()
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|read
argument_list|(
name|singleByte
argument_list|)
operator|==
literal|1
operator|)
condition|?
name|UnsignedBytes
operator|.
name|toInt
argument_list|(
name|singleByte
index|[
literal|0
index|]
argument_list|)
else|:
operator|-
literal|1
return|;
block|}
comment|// TODO(chrisn): Consider trying to encode/flush directly to the argument byte
comment|// buffer when possible.
DECL|method|read (byte[] b, int off, int len)
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Obey InputStream contract.
name|checkPositionIndexes
argument_list|(
name|off
argument_list|,
name|off
operator|+
name|len
argument_list|,
name|b
operator|.
name|length
argument_list|)
expr_stmt|;
if|if
condition|(
name|len
operator|==
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
comment|// The rest of this method implements the process described by the CharsetEncoder javadoc.
name|int
name|totalBytesRead
init|=
literal|0
decl_stmt|;
name|boolean
name|doneEncoding
init|=
name|endOfInput
decl_stmt|;
name|DRAINING
label|:
while|while
condition|(
literal|true
condition|)
block|{
comment|// We stay in draining mode until there are no bytes left in the output buffer.  Then we go
comment|// back to encoding/flushing.
if|if
condition|(
name|draining
condition|)
block|{
name|totalBytesRead
operator|+=
name|drain
argument_list|(
name|b
argument_list|,
name|off
operator|+
name|totalBytesRead
argument_list|,
name|len
operator|-
name|totalBytesRead
argument_list|)
expr_stmt|;
if|if
condition|(
name|totalBytesRead
operator|==
name|len
operator|||
name|doneFlushing
condition|)
block|{
return|return
operator|(
name|totalBytesRead
operator|>
literal|0
operator|)
condition|?
name|totalBytesRead
else|:
operator|-
literal|1
return|;
block|}
name|draining
operator|=
literal|false
expr_stmt|;
name|byteBuffer
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
while|while
condition|(
literal|true
condition|)
block|{
comment|// We call encode until there is no more input. The last call to encode will have endOfInput
comment|// == true. Then there is a final call to flush.
name|CoderResult
name|result
decl_stmt|;
if|if
condition|(
name|doneFlushing
condition|)
block|{
name|result
operator|=
name|CoderResult
operator|.
name|UNDERFLOW
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|doneEncoding
condition|)
block|{
name|result
operator|=
name|encoder
operator|.
name|flush
argument_list|(
name|byteBuffer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|encoder
operator|.
name|encode
argument_list|(
name|charBuffer
argument_list|,
name|byteBuffer
argument_list|,
name|endOfInput
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|.
name|isOverflow
argument_list|()
condition|)
block|{
comment|// Not enough room in output buffer--drain it, creating a bigger buffer if necessary.
name|startDraining
argument_list|(
literal|true
argument_list|)
expr_stmt|;
continue|continue
name|DRAINING
continue|;
block|}
elseif|else
if|if
condition|(
name|result
operator|.
name|isUnderflow
argument_list|()
condition|)
block|{
comment|// If encoder underflows, it means either:
comment|// a) the final flush() succeeded; next drain (then done)
comment|// b) we encoded all of the input; next flush
comment|// c) we ran of out input to encode; next read more input
if|if
condition|(
name|doneEncoding
condition|)
block|{
comment|// (a)
name|doneFlushing
operator|=
literal|true
expr_stmt|;
name|startDraining
argument_list|(
literal|false
argument_list|)
expr_stmt|;
continue|continue
name|DRAINING
continue|;
block|}
elseif|else
if|if
condition|(
name|endOfInput
condition|)
block|{
comment|// (b)
name|doneEncoding
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
comment|// (c)
name|readMoreChars
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|result
operator|.
name|isError
argument_list|()
condition|)
block|{
comment|// Only reach here if a CharsetEncoder with non-REPLACE settings is used.
name|result
operator|.
name|throwException
argument_list|()
expr_stmt|;
return|return
literal|0
return|;
comment|// Not called.
block|}
block|}
block|}
block|}
comment|/** Returns a new CharBuffer identical to buf, except twice the capacity. */
DECL|method|grow (CharBuffer buf)
specifier|private
specifier|static
name|CharBuffer
name|grow
parameter_list|(
name|CharBuffer
name|buf
parameter_list|)
block|{
name|char
index|[]
name|copy
init|=
name|Arrays
operator|.
name|copyOf
argument_list|(
name|buf
operator|.
name|array
argument_list|()
argument_list|,
name|buf
operator|.
name|capacity
argument_list|()
operator|*
literal|2
argument_list|)
decl_stmt|;
name|CharBuffer
name|bigger
init|=
name|CharBuffer
operator|.
name|wrap
argument_list|(
name|copy
argument_list|)
decl_stmt|;
name|bigger
operator|.
name|position
argument_list|(
name|buf
operator|.
name|position
argument_list|()
argument_list|)
expr_stmt|;
name|bigger
operator|.
name|limit
argument_list|(
name|buf
operator|.
name|limit
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|bigger
return|;
block|}
comment|/** Handle the case of underflow caused by needing more input characters. */
DECL|method|readMoreChars ()
specifier|private
name|void
name|readMoreChars
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Possibilities:
comment|// 1) array has space available on right hand side (between limit and capacity)
comment|// 2) array has space available on left hand side (before position)
comment|// 3) array has no space available
comment|//
comment|// In case 2 we shift the existing chars to the left, and in case 3 we create a bigger
comment|// array, then they both become case 1.
if|if
condition|(
name|availableCapacity
argument_list|(
name|charBuffer
argument_list|)
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|charBuffer
operator|.
name|position
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// (2) There is room in the buffer. Move existing bytes to the beginning.
name|charBuffer
operator|.
name|compact
argument_list|()
operator|.
name|flip
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// (3) Entire buffer is full, need bigger buffer.
name|charBuffer
operator|=
name|grow
argument_list|(
name|charBuffer
argument_list|)
expr_stmt|;
block|}
block|}
comment|// (1) Read more characters into free space at end of array.
name|int
name|limit
init|=
name|charBuffer
operator|.
name|limit
argument_list|()
decl_stmt|;
name|int
name|numChars
init|=
name|reader
operator|.
name|read
argument_list|(
name|charBuffer
operator|.
name|array
argument_list|()
argument_list|,
name|limit
argument_list|,
name|availableCapacity
argument_list|(
name|charBuffer
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|numChars
operator|==
operator|-
literal|1
condition|)
block|{
name|endOfInput
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|charBuffer
operator|.
name|limit
argument_list|(
name|limit
operator|+
name|numChars
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Returns the number of elements between the limit and capacity. */
DECL|method|availableCapacity (Buffer buffer)
specifier|private
specifier|static
name|int
name|availableCapacity
parameter_list|(
name|Buffer
name|buffer
parameter_list|)
block|{
return|return
name|buffer
operator|.
name|capacity
argument_list|()
operator|-
name|buffer
operator|.
name|limit
argument_list|()
return|;
block|}
comment|/**    * Flips the buffer output buffer so we can start reading bytes from it.  If we are starting to    * drain because there was overflow, and there aren't actually any characters to drain, then the    * overflow must be due to a small output buffer.    */
DECL|method|startDraining (boolean overflow)
specifier|private
name|void
name|startDraining
parameter_list|(
name|boolean
name|overflow
parameter_list|)
block|{
name|byteBuffer
operator|.
name|flip
argument_list|()
expr_stmt|;
if|if
condition|(
name|overflow
operator|&&
name|byteBuffer
operator|.
name|remaining
argument_list|()
operator|==
literal|0
condition|)
block|{
name|byteBuffer
operator|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
name|byteBuffer
operator|.
name|capacity
argument_list|()
operator|*
literal|2
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|draining
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|/**    * Copy as much of the byte buffer into the output array as possible, returning the (positive)    * number of characters copied.    */
DECL|method|drain (byte[] b, int off, int len)
specifier|private
name|int
name|drain
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
name|int
name|remaining
init|=
name|Math
operator|.
name|min
argument_list|(
name|len
argument_list|,
name|byteBuffer
operator|.
name|remaining
argument_list|()
argument_list|)
decl_stmt|;
name|byteBuffer
operator|.
name|get
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|remaining
argument_list|)
expr_stmt|;
return|return
name|remaining
return|;
block|}
block|}
end_class

end_unit

