begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.hash
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
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
name|ByteOrder
import|;
end_import

begin_comment
comment|/**  * A convenience base class for implementors of {@code Hasher}; handles accumulating data until an  * entire "chunk" (of implementation-dependent length) is ready to be hashed.  *  * @author Kevin Bourrillion  * @author Dimitris Andreou  */
end_comment

begin_comment
comment|// TODO(kevinb): this class still needs some design-and-document-for-inheritance love
end_comment

begin_class
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|AbstractStreamingHasher
specifier|abstract
class|class
name|AbstractStreamingHasher
extends|extends
name|AbstractHasher
block|{
comment|/** Buffer via which we pass data to the hash algorithm (the implementor) */
DECL|field|buffer
specifier|private
specifier|final
name|ByteBuffer
name|buffer
decl_stmt|;
comment|/** Number of bytes to be filled before process() invocation(s). */
DECL|field|bufferSize
specifier|private
specifier|final
name|int
name|bufferSize
decl_stmt|;
comment|/** Number of bytes processed per process() invocation. */
DECL|field|chunkSize
specifier|private
specifier|final
name|int
name|chunkSize
decl_stmt|;
comment|/**    * Constructor for use by subclasses. This hasher instance will process chunks of the specified    * size.    *    * @param chunkSize the number of bytes available per {@link #process(ByteBuffer)} invocation;    *     must be at least 4    */
DECL|method|AbstractStreamingHasher (int chunkSize)
specifier|protected
name|AbstractStreamingHasher
parameter_list|(
name|int
name|chunkSize
parameter_list|)
block|{
name|this
argument_list|(
name|chunkSize
argument_list|,
name|chunkSize
argument_list|)
expr_stmt|;
block|}
comment|/**    * Constructor for use by subclasses. This hasher instance will process chunks of the specified    * size, using an internal buffer of {@code bufferSize} size, which must be a multiple of {@code    * chunkSize}.    *    * @param chunkSize the number of bytes available per {@link #process(ByteBuffer)} invocation;    *     must be at least 4    * @param bufferSize the size of the internal buffer. Must be a multiple of chunkSize    */
DECL|method|AbstractStreamingHasher (int chunkSize, int bufferSize)
specifier|protected
name|AbstractStreamingHasher
parameter_list|(
name|int
name|chunkSize
parameter_list|,
name|int
name|bufferSize
parameter_list|)
block|{
comment|// TODO(kevinb): check more preconditions (as bufferSize>= chunkSize) if this is ever public
name|checkArgument
argument_list|(
name|bufferSize
operator|%
name|chunkSize
operator|==
literal|0
argument_list|)
expr_stmt|;
comment|// TODO(user): benchmark performance difference with longer buffer
comment|// always space for a single primitive
name|this
operator|.
name|buffer
operator|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
name|bufferSize
operator|+
literal|7
argument_list|)
operator|.
name|order
argument_list|(
name|ByteOrder
operator|.
name|LITTLE_ENDIAN
argument_list|)
expr_stmt|;
name|this
operator|.
name|bufferSize
operator|=
name|bufferSize
expr_stmt|;
name|this
operator|.
name|chunkSize
operator|=
name|chunkSize
expr_stmt|;
block|}
comment|/** Processes the available bytes of the buffer (at most {@code chunk} bytes). */
DECL|method|process (ByteBuffer bb)
specifier|protected
specifier|abstract
name|void
name|process
parameter_list|(
name|ByteBuffer
name|bb
parameter_list|)
function_decl|;
comment|/**    * This is invoked for the last bytes of the input, which are not enough to fill a whole chunk.    * The passed {@code ByteBuffer} is guaranteed to be non-empty.    *    *<p>This implementation simply pads with zeros and delegates to {@link #process(ByteBuffer)}.    */
DECL|method|processRemaining (ByteBuffer bb)
specifier|protected
name|void
name|processRemaining
parameter_list|(
name|ByteBuffer
name|bb
parameter_list|)
block|{
name|Java8Compatibility
operator|.
name|position
argument_list|(
name|bb
argument_list|,
name|bb
operator|.
name|limit
argument_list|()
argument_list|)
expr_stmt|;
comment|// move at the end
name|Java8Compatibility
operator|.
name|limit
argument_list|(
name|bb
argument_list|,
name|chunkSize
operator|+
literal|7
argument_list|)
expr_stmt|;
comment|// get ready to pad with longs
while|while
condition|(
name|bb
operator|.
name|position
argument_list|()
operator|<
name|chunkSize
condition|)
block|{
name|bb
operator|.
name|putLong
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|Java8Compatibility
operator|.
name|limit
argument_list|(
name|bb
argument_list|,
name|chunkSize
argument_list|)
expr_stmt|;
name|Java8Compatibility
operator|.
name|flip
argument_list|(
name|bb
argument_list|)
expr_stmt|;
name|process
argument_list|(
name|bb
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|putBytes (byte[] bytes, int off, int len)
specifier|public
specifier|final
name|Hasher
name|putBytes
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
return|return
name|putBytesInternal
argument_list|(
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|bytes
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
operator|.
name|order
argument_list|(
name|ByteOrder
operator|.
name|LITTLE_ENDIAN
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|putBytes (ByteBuffer readBuffer)
specifier|public
specifier|final
name|Hasher
name|putBytes
parameter_list|(
name|ByteBuffer
name|readBuffer
parameter_list|)
block|{
name|ByteOrder
name|order
init|=
name|readBuffer
operator|.
name|order
argument_list|()
decl_stmt|;
try|try
block|{
name|readBuffer
operator|.
name|order
argument_list|(
name|ByteOrder
operator|.
name|LITTLE_ENDIAN
argument_list|)
expr_stmt|;
return|return
name|putBytesInternal
argument_list|(
name|readBuffer
argument_list|)
return|;
block|}
finally|finally
block|{
name|readBuffer
operator|.
name|order
argument_list|(
name|order
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|putBytesInternal (ByteBuffer readBuffer)
specifier|private
name|Hasher
name|putBytesInternal
parameter_list|(
name|ByteBuffer
name|readBuffer
parameter_list|)
block|{
comment|// If we have room for all of it, this is easy
if|if
condition|(
name|readBuffer
operator|.
name|remaining
argument_list|()
operator|<=
name|buffer
operator|.
name|remaining
argument_list|()
condition|)
block|{
name|buffer
operator|.
name|put
argument_list|(
name|readBuffer
argument_list|)
expr_stmt|;
name|munchIfFull
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// First add just enough to fill buffer size, and munch that
name|int
name|bytesToCopy
init|=
name|bufferSize
operator|-
name|buffer
operator|.
name|position
argument_list|()
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
name|bytesToCopy
condition|;
name|i
operator|++
control|)
block|{
name|buffer
operator|.
name|put
argument_list|(
name|readBuffer
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|munch
argument_list|()
expr_stmt|;
comment|// buffer becomes empty here, since chunkSize divides bufferSize
comment|// Now process directly from the rest of the input buffer
while|while
condition|(
name|readBuffer
operator|.
name|remaining
argument_list|()
operator|>=
name|chunkSize
condition|)
block|{
name|process
argument_list|(
name|readBuffer
argument_list|)
expr_stmt|;
block|}
comment|// Finally stick the remainder back in our usual buffer
name|buffer
operator|.
name|put
argument_list|(
name|readBuffer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/*    * Note: hashString(CharSequence, Charset) is intentionally not overridden.    *    * While intuitively, using CharsetEncoder to encode the CharSequence directly to the buffer (or    * even to an intermediate buffer) should be considerably more efficient than potentially    * copying the CharSequence to a String and then calling getBytes(Charset) on that String, in    * reality there are optimizations that make the getBytes(Charset) approach considerably faster,    * at least for commonly used charsets like UTF-8.    */
annotation|@
name|Override
DECL|method|putByte (byte b)
specifier|public
specifier|final
name|Hasher
name|putByte
parameter_list|(
name|byte
name|b
parameter_list|)
block|{
name|buffer
operator|.
name|put
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|munchIfFull
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|putShort (short s)
specifier|public
specifier|final
name|Hasher
name|putShort
parameter_list|(
name|short
name|s
parameter_list|)
block|{
name|buffer
operator|.
name|putShort
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|munchIfFull
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|putChar (char c)
specifier|public
specifier|final
name|Hasher
name|putChar
parameter_list|(
name|char
name|c
parameter_list|)
block|{
name|buffer
operator|.
name|putChar
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|munchIfFull
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|putInt (int i)
specifier|public
specifier|final
name|Hasher
name|putInt
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|buffer
operator|.
name|putInt
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|munchIfFull
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|putLong (long l)
specifier|public
specifier|final
name|Hasher
name|putLong
parameter_list|(
name|long
name|l
parameter_list|)
block|{
name|buffer
operator|.
name|putLong
argument_list|(
name|l
argument_list|)
expr_stmt|;
name|munchIfFull
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|hash ()
specifier|public
specifier|final
name|HashCode
name|hash
parameter_list|()
block|{
name|munch
argument_list|()
expr_stmt|;
name|Java8Compatibility
operator|.
name|flip
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
if|if
condition|(
name|buffer
operator|.
name|remaining
argument_list|()
operator|>
literal|0
condition|)
block|{
name|processRemaining
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|Java8Compatibility
operator|.
name|position
argument_list|(
name|buffer
argument_list|,
name|buffer
operator|.
name|limit
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|makeHash
argument_list|()
return|;
block|}
comment|/**    * Computes a hash code based on the data that have been provided to this hasher. This is called    * after all chunks are handled with {@link #process} and any leftover bytes that did not make a    * complete chunk are handled with {@link #processRemaining}.    */
DECL|method|makeHash ()
specifier|protected
specifier|abstract
name|HashCode
name|makeHash
parameter_list|()
function_decl|;
comment|// Process pent-up data in chunks
DECL|method|munchIfFull ()
specifier|private
name|void
name|munchIfFull
parameter_list|()
block|{
if|if
condition|(
name|buffer
operator|.
name|remaining
argument_list|()
operator|<
literal|8
condition|)
block|{
comment|// buffer is full; not enough room for a primitive. We have at least one full chunk.
name|munch
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|munch ()
specifier|private
name|void
name|munch
parameter_list|()
block|{
name|Java8Compatibility
operator|.
name|flip
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
while|while
condition|(
name|buffer
operator|.
name|remaining
argument_list|()
operator|>=
name|chunkSize
condition|)
block|{
comment|// we could limit the buffer to ensure process() does not read more than
comment|// chunkSize number of bytes, but we trust the implementations
name|process
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|compact
argument_list|()
expr_stmt|;
comment|// preserve any remaining data that do not make a full chunk
block|}
block|}
end_class

end_unit

