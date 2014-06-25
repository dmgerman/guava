begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|collect
operator|.
name|ImmutableList
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
name|hash
operator|.
name|Funnels
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
name|hash
operator|.
name|HashCode
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
name|hash
operator|.
name|HashFunction
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
name|hash
operator|.
name|Hasher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|charset
operator|.
name|Charset
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
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_comment
comment|/**  * A readable source of bytes, such as a file. Unlike an {@link InputStream}, a  * {@code ByteSource} is not an open, stateful stream for input that can be read and closed.  * Instead, it is an immutable<i>supplier</i> of {@code InputStream} instances.  *  *<p>{@code ByteSource} provides two kinds of methods:  *<ul>  *<li><b>Methods that return a stream:</b> These methods should return a<i>new</i>, independent  *   instance each time they are called. The caller is responsible for ensuring that the returned  *   stream is closed.  *<li><b>Convenience methods:</b> These are implementations of common operations that are  *   typically implemented by opening a stream using one of the methods in the first category, doing  *   something and finally closing the stream that was opened.  *</ul>  *  * @since 14.0  * @author Colin Decker  */
end_comment

begin_class
DECL|class|ByteSource
specifier|public
specifier|abstract
class|class
name|ByteSource
block|{
DECL|field|BUF_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|BUF_SIZE
init|=
literal|0x1000
decl_stmt|;
comment|// 4K
comment|/**    * Constructor for use by subclasses.    */
DECL|method|ByteSource ()
specifier|protected
name|ByteSource
parameter_list|()
block|{}
comment|/**    * Returns a {@link CharSource} view of this byte source that decodes bytes read from this source    * as characters using the given {@link Charset}.    */
DECL|method|asCharSource (Charset charset)
specifier|public
name|CharSource
name|asCharSource
parameter_list|(
name|Charset
name|charset
parameter_list|)
block|{
return|return
operator|new
name|AsCharSource
argument_list|(
name|charset
argument_list|)
return|;
block|}
comment|/**    * Opens a new {@link InputStream} for reading from this source. This method should return a new,    * independent stream each time it is called.    *    *<p>The caller is responsible for ensuring that the returned stream is closed.    *    * @throws IOException if an I/O error occurs in the process of opening the stream    */
DECL|method|openStream ()
specifier|public
specifier|abstract
name|InputStream
name|openStream
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**    * Opens a new buffered {@link InputStream} for reading from this source. The returned stream is    * not required to be a {@link BufferedInputStream} in order to allow implementations to simply    * delegate to {@link #openStream()} when the stream returned by that method does not benefit    * from additional buffering (for example, a {@code ByteArrayInputStream}). This method should    * return a new, independent stream each time it is called.    *    *<p>The caller is responsible for ensuring that the returned stream is closed.    *    * @throws IOException if an I/O error occurs in the process of opening the stream    * @since 15.0 (in 14.0 with return type {@link BufferedInputStream})    */
DECL|method|openBufferedStream ()
specifier|public
name|InputStream
name|openBufferedStream
parameter_list|()
throws|throws
name|IOException
block|{
name|InputStream
name|in
init|=
name|openStream
argument_list|()
decl_stmt|;
return|return
operator|(
name|in
operator|instanceof
name|BufferedInputStream
operator|)
condition|?
operator|(
name|BufferedInputStream
operator|)
name|in
else|:
operator|new
name|BufferedInputStream
argument_list|(
name|in
argument_list|)
return|;
block|}
comment|/**    * Returns a view of a slice of this byte source that is at most {@code length} bytes long    * starting at the given {@code offset}.    *    * @throws IllegalArgumentException if {@code offset} or {@code length} is negative    */
DECL|method|slice (long offset, long length)
specifier|public
name|ByteSource
name|slice
parameter_list|(
name|long
name|offset
parameter_list|,
name|long
name|length
parameter_list|)
block|{
return|return
operator|new
name|SlicedByteSource
argument_list|(
name|offset
argument_list|,
name|length
argument_list|)
return|;
block|}
comment|/**    * Returns whether the source has zero bytes. The default implementation is to open a stream and    * check for EOF.    *    * @throws IOException if an I/O error occurs    * @since 15.0    */
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
throws|throws
name|IOException
block|{
name|Closer
name|closer
init|=
name|Closer
operator|.
name|create
argument_list|()
decl_stmt|;
try|try
block|{
name|InputStream
name|in
init|=
name|closer
operator|.
name|register
argument_list|(
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|in
operator|.
name|read
argument_list|()
operator|==
operator|-
literal|1
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
name|closer
operator|.
name|rethrow
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|closer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Returns the size of this source in bytes. For most implementations, this is a heavyweight    * operation that will open a stream, read (or {@link InputStream#skip(long) skip}, if possible)    * to the end of the stream and return the total number of bytes that were read.    *    *<p>For some sources, such as a file, this method may use a more efficient implementation. Note    * that in such cases, it is<i>possible</i> that this method will return a different number of    * bytes than would be returned by reading all of the bytes (for example, some special files may    * return a size of 0 despite actually having content when read).    *    *<p>In either case, if this is a mutable source such as a file, the size it returns may not be    * the same number of bytes a subsequent read would return.    *    * @throws IOException if an I/O error occurs in the process of reading the size of this source    */
DECL|method|size ()
specifier|public
name|long
name|size
parameter_list|()
throws|throws
name|IOException
block|{
name|Closer
name|closer
init|=
name|Closer
operator|.
name|create
argument_list|()
decl_stmt|;
try|try
block|{
name|InputStream
name|in
init|=
name|closer
operator|.
name|register
argument_list|(
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|countBySkipping
argument_list|(
name|in
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// skip may not be supported... at any rate, try reading
block|}
finally|finally
block|{
name|closer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|closer
operator|=
name|Closer
operator|.
name|create
argument_list|()
expr_stmt|;
try|try
block|{
name|InputStream
name|in
init|=
name|closer
operator|.
name|register
argument_list|(
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|countByReading
argument_list|(
name|in
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
name|closer
operator|.
name|rethrow
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|closer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Counts the bytes in the given input stream using skip if possible. Returns SKIP_FAILED if the    * first call to skip threw, in which case skip may just not be supported.    */
DECL|method|countBySkipping (InputStream in)
specifier|private
name|long
name|countBySkipping
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|long
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
comment|// don't try to skip more than available()
comment|// things may work really wrong with FileInputStream otherwise
name|long
name|skipped
init|=
name|in
operator|.
name|skip
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|in
operator|.
name|available
argument_list|()
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|skipped
operator|<=
literal|0
condition|)
block|{
if|if
condition|(
name|in
operator|.
name|read
argument_list|()
operator|==
operator|-
literal|1
condition|)
block|{
return|return
name|count
return|;
block|}
elseif|else
if|if
condition|(
name|count
operator|==
literal|0
operator|&&
name|in
operator|.
name|available
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// if available is still zero after reading a single byte, it
comment|// will probably always be zero, so we should countByReading
throw|throw
operator|new
name|IOException
argument_list|()
throw|;
block|}
name|count
operator|++
expr_stmt|;
block|}
else|else
block|{
name|count
operator|+=
name|skipped
expr_stmt|;
block|}
block|}
block|}
DECL|field|countBuffer
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|countBuffer
init|=
operator|new
name|byte
index|[
name|BUF_SIZE
index|]
decl_stmt|;
DECL|method|countByReading (InputStream in)
specifier|private
name|long
name|countByReading
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|long
name|count
init|=
literal|0
decl_stmt|;
name|long
name|read
decl_stmt|;
while|while
condition|(
operator|(
name|read
operator|=
name|in
operator|.
name|read
argument_list|(
name|countBuffer
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|count
operator|+=
name|read
expr_stmt|;
block|}
return|return
name|count
return|;
block|}
comment|/**    * Copies the contents of this byte source to the given {@code OutputStream}. Does not close    * {@code output}.    *    * @throws IOException if an I/O error occurs in the process of reading from this source or    *     writing to {@code output}    */
DECL|method|copyTo (OutputStream output)
specifier|public
name|long
name|copyTo
parameter_list|(
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|Closer
name|closer
init|=
name|Closer
operator|.
name|create
argument_list|()
decl_stmt|;
try|try
block|{
name|InputStream
name|in
init|=
name|closer
operator|.
name|register
argument_list|(
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|ByteStreams
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|output
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
name|closer
operator|.
name|rethrow
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|closer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Copies the contents of this byte source to the given {@code ByteSink}.    *    * @throws IOException if an I/O error occurs in the process of reading from this source or    *     writing to {@code sink}    */
DECL|method|copyTo (ByteSink sink)
specifier|public
name|long
name|copyTo
parameter_list|(
name|ByteSink
name|sink
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotNull
argument_list|(
name|sink
argument_list|)
expr_stmt|;
name|Closer
name|closer
init|=
name|Closer
operator|.
name|create
argument_list|()
decl_stmt|;
try|try
block|{
name|InputStream
name|in
init|=
name|closer
operator|.
name|register
argument_list|(
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|OutputStream
name|out
init|=
name|closer
operator|.
name|register
argument_list|(
name|sink
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|ByteStreams
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
name|closer
operator|.
name|rethrow
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|closer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Reads the full contents of this byte source as a byte array.    *    * @throws IOException if an I/O error occurs in the process of reading from this source    */
DECL|method|read ()
specifier|public
name|byte
index|[]
name|read
parameter_list|()
throws|throws
name|IOException
block|{
name|Closer
name|closer
init|=
name|Closer
operator|.
name|create
argument_list|()
decl_stmt|;
try|try
block|{
name|InputStream
name|in
init|=
name|closer
operator|.
name|register
argument_list|(
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|ByteStreams
operator|.
name|toByteArray
argument_list|(
name|in
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
name|closer
operator|.
name|rethrow
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|closer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Reads the contents of this byte source using the given {@code processor} to process bytes as    * they are read. Stops when all bytes have been read or the consumer returns {@code false}.    * Returns the result produced by the processor.    *    * @throws IOException if an I/O error occurs in the process of reading from this source or if    *     {@code processor} throws an {@code IOException}    * @since 16.0    */
annotation|@
name|Beta
DECL|method|read (ByteProcessor<T> processor)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|read
parameter_list|(
name|ByteProcessor
argument_list|<
name|T
argument_list|>
name|processor
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotNull
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|Closer
name|closer
init|=
name|Closer
operator|.
name|create
argument_list|()
decl_stmt|;
try|try
block|{
name|InputStream
name|in
init|=
name|closer
operator|.
name|register
argument_list|(
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|ByteStreams
operator|.
name|readBytes
argument_list|(
name|in
argument_list|,
name|processor
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
name|closer
operator|.
name|rethrow
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|closer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Hashes the contents of this byte source using the given hash function.    *    * @throws IOException if an I/O error occurs in the process of reading from this source    */
DECL|method|hash (HashFunction hashFunction)
specifier|public
name|HashCode
name|hash
parameter_list|(
name|HashFunction
name|hashFunction
parameter_list|)
throws|throws
name|IOException
block|{
name|Hasher
name|hasher
init|=
name|hashFunction
operator|.
name|newHasher
argument_list|()
decl_stmt|;
name|copyTo
argument_list|(
name|Funnels
operator|.
name|asOutputStream
argument_list|(
name|hasher
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|hasher
operator|.
name|hash
argument_list|()
return|;
block|}
comment|/**    * Checks that the contents of this byte source are equal to the contents of the given byte    * source.    *    * @throws IOException if an I/O error occurs in the process of reading from this source or    *     {@code other}    */
DECL|method|contentEquals (ByteSource other)
specifier|public
name|boolean
name|contentEquals
parameter_list|(
name|ByteSource
name|other
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotNull
argument_list|(
name|other
argument_list|)
expr_stmt|;
name|byte
index|[]
name|buf1
init|=
operator|new
name|byte
index|[
name|BUF_SIZE
index|]
decl_stmt|;
name|byte
index|[]
name|buf2
init|=
operator|new
name|byte
index|[
name|BUF_SIZE
index|]
decl_stmt|;
name|Closer
name|closer
init|=
name|Closer
operator|.
name|create
argument_list|()
decl_stmt|;
try|try
block|{
name|InputStream
name|in1
init|=
name|closer
operator|.
name|register
argument_list|(
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|InputStream
name|in2
init|=
name|closer
operator|.
name|register
argument_list|(
name|other
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|read1
init|=
name|ByteStreams
operator|.
name|read
argument_list|(
name|in1
argument_list|,
name|buf1
argument_list|,
literal|0
argument_list|,
name|BUF_SIZE
argument_list|)
decl_stmt|;
name|int
name|read2
init|=
name|ByteStreams
operator|.
name|read
argument_list|(
name|in2
argument_list|,
name|buf2
argument_list|,
literal|0
argument_list|,
name|BUF_SIZE
argument_list|)
decl_stmt|;
if|if
condition|(
name|read1
operator|!=
name|read2
operator|||
operator|!
name|Arrays
operator|.
name|equals
argument_list|(
name|buf1
argument_list|,
name|buf2
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|read1
operator|!=
name|BUF_SIZE
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
name|closer
operator|.
name|rethrow
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|closer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Concatenates multiple {@link ByteSource} instances into a single source. Streams returned from    * the source will contain the concatenated data from the streams of the underlying sources.    *    *<p>Only one underlying stream will be open at a time. Closing the concatenated stream will    * close the open underlying stream.    *    * @param sources the sources to concatenate    * @return a {@code ByteSource} containing the concatenated data    * @since 15.0    */
DECL|method|concat (Iterable<? extends ByteSource> sources)
specifier|public
specifier|static
name|ByteSource
name|concat
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|ByteSource
argument_list|>
name|sources
parameter_list|)
block|{
return|return
operator|new
name|ConcatenatedByteSource
argument_list|(
name|sources
argument_list|)
return|;
block|}
comment|/**    * Concatenates multiple {@link ByteSource} instances into a single source. Streams returned from    * the source will contain the concatenated data from the streams of the underlying sources.    *    *<p>Only one underlying stream will be open at a time. Closing the concatenated stream will    * close the open underlying stream.    *    *<p>Note: The input {@code Iterator} will be copied to an {@code ImmutableList} when this    * method is called. This will fail if the iterator is infinite and may cause problems if the    * iterator eagerly fetches data for each source when iterated (rather than producing sources    * that only load data through their streams). Prefer using the {@link #concat(Iterable)}    * overload if possible.    *    * @param sources the sources to concatenate    * @return a {@code ByteSource} containing the concatenated data    * @throws NullPointerException if any of {@code sources} is {@code null}    * @since 15.0    */
DECL|method|concat (Iterator<? extends ByteSource> sources)
specifier|public
specifier|static
name|ByteSource
name|concat
parameter_list|(
name|Iterator
argument_list|<
name|?
extends|extends
name|ByteSource
argument_list|>
name|sources
parameter_list|)
block|{
return|return
name|concat
argument_list|(
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|sources
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Concatenates multiple {@link ByteSource} instances into a single source. Streams returned from    * the source will contain the concatenated data from the streams of the underlying sources.    *    *<p>Only one underlying stream will be open at a time. Closing the concatenated stream will    * close the open underlying stream.    *    * @param sources the sources to concatenate    * @return a {@code ByteSource} containing the concatenated data    * @throws NullPointerException if any of {@code sources} is {@code null}    * @since 15.0    */
DECL|method|concat (ByteSource... sources)
specifier|public
specifier|static
name|ByteSource
name|concat
parameter_list|(
name|ByteSource
modifier|...
name|sources
parameter_list|)
block|{
return|return
name|concat
argument_list|(
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|sources
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a view of the given byte array as a {@link ByteSource}. To view only a specific range    * in the array, use {@code ByteSource.wrap(b).slice(offset, length)}.    *    * @since 15.0 (since 14.0 as {@code ByteStreams.asByteSource(byte[])}).    */
DECL|method|wrap (byte[] b)
specifier|public
specifier|static
name|ByteSource
name|wrap
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
block|{
return|return
operator|new
name|ByteArrayByteSource
argument_list|(
name|b
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable {@link ByteSource} that contains no bytes.    *    * @since 15.0    */
DECL|method|empty ()
specifier|public
specifier|static
name|ByteSource
name|empty
parameter_list|()
block|{
return|return
name|EmptyByteSource
operator|.
name|INSTANCE
return|;
block|}
comment|/**    * A char source that reads bytes from this source and decodes them as characters using a    * charset.    */
DECL|class|AsCharSource
specifier|private
specifier|final
class|class
name|AsCharSource
extends|extends
name|CharSource
block|{
DECL|field|charset
specifier|private
specifier|final
name|Charset
name|charset
decl_stmt|;
DECL|method|AsCharSource (Charset charset)
specifier|private
name|AsCharSource
parameter_list|(
name|Charset
name|charset
parameter_list|)
block|{
name|this
operator|.
name|charset
operator|=
name|checkNotNull
argument_list|(
name|charset
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|openStream ()
specifier|public
name|Reader
name|openStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|InputStreamReader
argument_list|(
name|ByteSource
operator|.
name|this
operator|.
name|openStream
argument_list|()
argument_list|,
name|charset
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|ByteSource
operator|.
name|this
operator|.
name|toString
argument_list|()
operator|+
literal|".asCharSource("
operator|+
name|charset
operator|+
literal|")"
return|;
block|}
block|}
comment|/**    * A view of a subsection of the containing byte source.    */
DECL|class|SlicedByteSource
specifier|private
specifier|final
class|class
name|SlicedByteSource
extends|extends
name|ByteSource
block|{
DECL|field|offset
specifier|private
specifier|final
name|long
name|offset
decl_stmt|;
DECL|field|length
specifier|private
specifier|final
name|long
name|length
decl_stmt|;
DECL|method|SlicedByteSource (long offset, long length)
specifier|private
name|SlicedByteSource
parameter_list|(
name|long
name|offset
parameter_list|,
name|long
name|length
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|offset
operator|>=
literal|0
argument_list|,
literal|"offset (%s) may not be negative"
argument_list|,
name|offset
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|length
operator|>=
literal|0
argument_list|,
literal|"length (%s) may not be negative"
argument_list|,
name|length
argument_list|)
expr_stmt|;
name|this
operator|.
name|offset
operator|=
name|offset
expr_stmt|;
name|this
operator|.
name|length
operator|=
name|length
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|openStream ()
specifier|public
name|InputStream
name|openStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|sliceStream
argument_list|(
name|ByteSource
operator|.
name|this
operator|.
name|openStream
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|openBufferedStream ()
specifier|public
name|InputStream
name|openBufferedStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|sliceStream
argument_list|(
name|ByteSource
operator|.
name|this
operator|.
name|openBufferedStream
argument_list|()
argument_list|)
return|;
block|}
DECL|method|sliceStream (InputStream in)
specifier|private
name|InputStream
name|sliceStream
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|offset
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|ByteStreams
operator|.
name|skipFully
argument_list|(
name|in
argument_list|,
name|offset
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|Closer
name|closer
init|=
name|Closer
operator|.
name|create
argument_list|()
decl_stmt|;
name|closer
operator|.
name|register
argument_list|(
name|in
argument_list|)
expr_stmt|;
try|try
block|{
throw|throw
name|closer
operator|.
name|rethrow
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|closer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|ByteStreams
operator|.
name|limit
argument_list|(
name|in
argument_list|,
name|length
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|slice (long offset, long length)
specifier|public
name|ByteSource
name|slice
parameter_list|(
name|long
name|offset
parameter_list|,
name|long
name|length
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|offset
operator|>=
literal|0
argument_list|,
literal|"offset (%s) may not be negative"
argument_list|,
name|offset
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|length
operator|>=
literal|0
argument_list|,
literal|"length (%s) may not be negative"
argument_list|,
name|length
argument_list|)
expr_stmt|;
name|long
name|maxLength
init|=
name|this
operator|.
name|length
operator|-
name|offset
decl_stmt|;
return|return
name|ByteSource
operator|.
name|this
operator|.
name|slice
argument_list|(
name|this
operator|.
name|offset
operator|+
name|offset
argument_list|,
name|Math
operator|.
name|min
argument_list|(
name|length
argument_list|,
name|maxLength
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|length
operator|==
literal|0
operator|||
name|super
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|ByteSource
operator|.
name|this
operator|.
name|toString
argument_list|()
operator|+
literal|".slice("
operator|+
name|offset
operator|+
literal|", "
operator|+
name|length
operator|+
literal|")"
return|;
block|}
block|}
DECL|class|ByteArrayByteSource
specifier|private
specifier|static
class|class
name|ByteArrayByteSource
extends|extends
name|ByteSource
block|{
DECL|field|bytes
specifier|protected
specifier|final
name|byte
index|[]
name|bytes
decl_stmt|;
DECL|method|ByteArrayByteSource (byte[] bytes)
specifier|protected
name|ByteArrayByteSource
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|this
operator|.
name|bytes
operator|=
name|checkNotNull
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|openStream ()
specifier|public
name|InputStream
name|openStream
parameter_list|()
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|openBufferedStream ()
specifier|public
name|InputStream
name|openBufferedStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|openStream
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|bytes
operator|.
name|length
operator|==
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|long
name|size
parameter_list|()
block|{
return|return
name|bytes
operator|.
name|length
return|;
block|}
annotation|@
name|Override
DECL|method|read ()
specifier|public
name|byte
index|[]
name|read
parameter_list|()
block|{
return|return
name|bytes
operator|.
name|clone
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|copyTo (OutputStream output)
specifier|public
name|long
name|copyTo
parameter_list|(
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
name|output
operator|.
name|write
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
return|return
name|bytes
operator|.
name|length
return|;
block|}
annotation|@
name|Override
DECL|method|read (ByteProcessor<T> processor)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|read
parameter_list|(
name|ByteProcessor
argument_list|<
name|T
argument_list|>
name|processor
parameter_list|)
throws|throws
name|IOException
block|{
name|processor
operator|.
name|processBytes
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|processor
operator|.
name|getResult
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|hash (HashFunction hashFunction)
specifier|public
name|HashCode
name|hash
parameter_list|(
name|HashFunction
name|hashFunction
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|hashFunction
operator|.
name|hashBytes
argument_list|(
name|bytes
argument_list|)
return|;
block|}
comment|// TODO(user): Possibly override slice()
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ByteSource.wrap("
operator|+
name|Ascii
operator|.
name|truncate
argument_list|(
name|BaseEncoding
operator|.
name|base16
argument_list|()
operator|.
name|encode
argument_list|(
name|bytes
argument_list|)
argument_list|,
literal|30
argument_list|,
literal|"..."
argument_list|)
operator|+
literal|")"
return|;
block|}
block|}
DECL|class|EmptyByteSource
specifier|private
specifier|static
specifier|final
class|class
name|EmptyByteSource
extends|extends
name|ByteArrayByteSource
block|{
DECL|field|INSTANCE
specifier|private
specifier|static
specifier|final
name|EmptyByteSource
name|INSTANCE
init|=
operator|new
name|EmptyByteSource
argument_list|()
decl_stmt|;
DECL|method|EmptyByteSource ()
specifier|private
name|EmptyByteSource
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|asCharSource (Charset charset)
specifier|public
name|CharSource
name|asCharSource
parameter_list|(
name|Charset
name|charset
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|charset
argument_list|)
expr_stmt|;
return|return
name|CharSource
operator|.
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|read ()
specifier|public
name|byte
index|[]
name|read
parameter_list|()
block|{
return|return
name|bytes
return|;
comment|// length is 0, no need to clone
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ByteSource.empty()"
return|;
block|}
block|}
DECL|class|ConcatenatedByteSource
specifier|private
specifier|static
specifier|final
class|class
name|ConcatenatedByteSource
extends|extends
name|ByteSource
block|{
DECL|field|sources
specifier|private
specifier|final
name|Iterable
argument_list|<
name|?
extends|extends
name|ByteSource
argument_list|>
name|sources
decl_stmt|;
DECL|method|ConcatenatedByteSource (Iterable<? extends ByteSource> sources)
name|ConcatenatedByteSource
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|ByteSource
argument_list|>
name|sources
parameter_list|)
block|{
name|this
operator|.
name|sources
operator|=
name|checkNotNull
argument_list|(
name|sources
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|openStream ()
specifier|public
name|InputStream
name|openStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|MultiInputStream
argument_list|(
name|sources
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
throws|throws
name|IOException
block|{
for|for
control|(
name|ByteSource
name|source
range|:
name|sources
control|)
block|{
if|if
condition|(
operator|!
name|source
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|long
name|size
parameter_list|()
throws|throws
name|IOException
block|{
name|long
name|result
init|=
literal|0L
decl_stmt|;
for|for
control|(
name|ByteSource
name|source
range|:
name|sources
control|)
block|{
name|result
operator|+=
name|source
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ByteSource.concat("
operator|+
name|sources
operator|+
literal|")"
return|;
block|}
block|}
block|}
end_class

end_unit

