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
name|GwtIncompatible
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
name|Optional
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|AbstractIterator
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
name|collect
operator|.
name|Lists
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
name|io
operator|.
name|BufferedReader
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
name|io
operator|.
name|Writer
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
comment|/**  * A readable source of characters, such as a text file. Unlike a {@link Reader}, a  * {@code CharSource} is not an open, stateful stream of characters that can be read and closed.  * Instead, it is an immutable<i>supplier</i> of {@code Reader} instances.  *  *<p>{@code CharSource} provides two kinds of methods:  *<ul>  *<li><b>Methods that return a reader:</b> These methods should return a<i>new</i>, independent  *     instance each time they are called. The caller is responsible for ensuring that the returned  *     reader is closed.  *<li><b>Convenience methods:</b> These are implementations of common operations that are typically  *     implemented by opening a reader using one of the methods in the first category, doing  *     something and finally closing the reader that was opened.  *</ul>  *  *<p>Several methods in this class, such as {@link #readLines()}, break the contents of the source  * into lines. Like {@link BufferedReader}, these methods break lines on any of {@code \n},  * {@code \r} or {@code \r\n}, do not include the line separator in each line and do not consider  * there to be an empty line at the end if the contents are terminated with a line separator.  *  *<p>Any {@link ByteSource} containing text encoded with a specific {@linkplain Charset character  * encoding} may be viewed as a {@code CharSource} using {@link ByteSource#asCharSource(Charset)}.  *  * @since 14.0  * @author Colin Decker  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|CharSource
specifier|public
specifier|abstract
class|class
name|CharSource
block|{
comment|/**    * Constructor for use by subclasses.    */
DECL|method|CharSource ()
specifier|protected
name|CharSource
parameter_list|()
block|{}
comment|/**    * Returns a {@link ByteSource} view of this char source that encodes chars read from this source    * as bytes using the given {@link Charset}.    *    *<p>If {@link ByteSource#asCharSource} is called on the returned source with the same charset,    * the default implementation of this method will ensure that the original {@code CharSource} is    * returned, rather than round-trip encoding. Subclasses that override this method should behave    * the same way.    *    * @since 20.0    */
annotation|@
name|Beta
DECL|method|asByteSource (Charset charset)
specifier|public
name|ByteSource
name|asByteSource
parameter_list|(
name|Charset
name|charset
parameter_list|)
block|{
return|return
operator|new
name|AsByteSource
argument_list|(
name|charset
argument_list|)
return|;
block|}
comment|/**    * Opens a new {@link Reader} for reading from this source. This method should return a new,    * independent reader each time it is called.    *    *<p>The caller is responsible for ensuring that the returned reader is closed.    *    * @throws IOException if an I/O error occurs in the process of opening the reader    */
DECL|method|openStream ()
specifier|public
specifier|abstract
name|Reader
name|openStream
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**    * Opens a new {@link BufferedReader} for reading from this source. This method should return a    * new, independent reader each time it is called.    *    *<p>The caller is responsible for ensuring that the returned reader is closed.    *    * @throws IOException if an I/O error occurs in the process of opening the reader    */
DECL|method|openBufferedStream ()
specifier|public
name|BufferedReader
name|openBufferedStream
parameter_list|()
throws|throws
name|IOException
block|{
name|Reader
name|reader
init|=
name|openStream
argument_list|()
decl_stmt|;
return|return
operator|(
name|reader
operator|instanceof
name|BufferedReader
operator|)
condition|?
operator|(
name|BufferedReader
operator|)
name|reader
else|:
operator|new
name|BufferedReader
argument_list|(
name|reader
argument_list|)
return|;
block|}
comment|/**    * Returns the size of this source in chars, if the size can be easily determined without actually    * opening the data stream.    *    *<p>The default implementation returns {@link Optional#absent}. Some sources, such as a    * {@code CharSequence}, may return a non-absent value. Note that in such cases, it is    *<i>possible</i> that this method will return a different number of chars than would be returned    * by reading all of the chars.    *    *<p>Additionally, for mutable sources such as {@code StringBuilder}s, a subsequent read may    * return a different number of chars if the contents are changed.    *    * @since 19.0    */
annotation|@
name|Beta
DECL|method|lengthIfKnown ()
specifier|public
name|Optional
argument_list|<
name|Long
argument_list|>
name|lengthIfKnown
parameter_list|()
block|{
return|return
name|Optional
operator|.
name|absent
argument_list|()
return|;
block|}
comment|/**    * Returns the length of this source in chars, even if doing so requires opening and traversing an    * entire stream. To avoid a potentially expensive operation, see {@link #lengthIfKnown}.    *    *<p>The default implementation calls {@link #lengthIfKnown} and returns the value if present. If    * absent, it will fall back to a heavyweight operation that will open a stream,    * {@link Reader#skip(long) skip} to the end of the stream, and return the total number of chars    * that were skipped.    *    *<p>Note that for sources that implement {@link #lengthIfKnown} to provide a more efficient    * implementation, it is<i>possible</i> that this method will return a different number of chars    * than would be returned by reading all of the chars.    *    *<p>In either case, for mutable sources such as files, a subsequent read may return a different    * number of chars if the contents are changed.    *    * @throws IOException if an I/O error occurs in the process of reading the length of this source    * @since 19.0    */
annotation|@
name|Beta
DECL|method|length ()
specifier|public
name|long
name|length
parameter_list|()
throws|throws
name|IOException
block|{
name|Optional
argument_list|<
name|Long
argument_list|>
name|lengthIfKnown
init|=
name|lengthIfKnown
argument_list|()
decl_stmt|;
if|if
condition|(
name|lengthIfKnown
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|lengthIfKnown
operator|.
name|get
argument_list|()
return|;
block|}
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
name|Reader
name|reader
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
name|reader
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
DECL|method|countBySkipping (Reader reader)
specifier|private
name|long
name|countBySkipping
parameter_list|(
name|Reader
name|reader
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
name|reader
operator|.
name|skip
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
operator|)
operator|!=
literal|0
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
comment|/**    * Appends the contents of this source to the given {@link Appendable} (such as a {@link Writer}).    * Does not close {@code appendable} if it is {@code Closeable}.    *    * @return the number of characters copied    * @throws IOException if an I/O error occurs in the process of reading from this source or    *     writing to {@code appendable}    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|copyTo (Appendable appendable)
specifier|public
name|long
name|copyTo
parameter_list|(
name|Appendable
name|appendable
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotNull
argument_list|(
name|appendable
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
name|Reader
name|reader
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
name|CharStreams
operator|.
name|copy
argument_list|(
name|reader
argument_list|,
name|appendable
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
comment|/**    * Copies the contents of this source to the given sink.    *    * @return the number of characters copied    * @throws IOException if an I/O error occurs in the process of reading from this source or    *     writing to {@code sink}    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|copyTo (CharSink sink)
specifier|public
name|long
name|copyTo
parameter_list|(
name|CharSink
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
name|Reader
name|reader
init|=
name|closer
operator|.
name|register
argument_list|(
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|Writer
name|writer
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
name|CharStreams
operator|.
name|copy
argument_list|(
name|reader
argument_list|,
name|writer
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
comment|/**    * Reads the contents of this source as a string.    *    * @throws IOException if an I/O error occurs in the process of reading from this source    */
DECL|method|read ()
specifier|public
name|String
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
name|Reader
name|reader
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
name|CharStreams
operator|.
name|toString
argument_list|(
name|reader
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
comment|/**    * Reads the first line of this source as a string. Returns {@code null} if this source is empty.    *    *<p>Like {@link BufferedReader}, this method breaks lines on any of {@code \n}, {@code \r} or    * {@code \r\n}, does not include the line separator in the returned line and does not consider    * there to be an extra empty line at the end if the content is terminated with a line separator.    *    * @throws IOException if an I/O error occurs in the process of reading from this source    */
annotation|@
name|Nullable
DECL|method|readFirstLine ()
specifier|public
name|String
name|readFirstLine
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
name|BufferedReader
name|reader
init|=
name|closer
operator|.
name|register
argument_list|(
name|openBufferedStream
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|reader
operator|.
name|readLine
argument_list|()
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
comment|/**    * Reads all the lines of this source as a list of strings. The returned list will be empty if    * this source is empty.    *    *<p>Like {@link BufferedReader}, this method breaks lines on any of {@code \n}, {@code \r} or    * {@code \r\n}, does not include the line separator in the returned lines and does not consider    * there to be an extra empty line at the end if the content is terminated with a line separator.    *    * @throws IOException if an I/O error occurs in the process of reading from this source    */
DECL|method|readLines ()
specifier|public
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|readLines
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
name|BufferedReader
name|reader
init|=
name|closer
operator|.
name|register
argument_list|(
name|openBufferedStream
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|result
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|String
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|result
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
comment|/**    * Reads lines of text from this source, processing each line as it is read using the given    * {@link LineProcessor processor}. Stops when all lines have been processed or the processor    * returns {@code false} and returns the result produced by the processor.    *    *<p>Like {@link BufferedReader}, this method breaks lines on any of {@code \n}, {@code \r} or    * {@code \r\n}, does not include the line separator in the lines passed to the {@code processor}    * and does not consider there to be an extra empty line at the end if the content is terminated    * with a line separator.    *    * @throws IOException if an I/O error occurs in the process of reading from this source or if    *     {@code processor} throws an {@code IOException}    * @since 16.0    */
annotation|@
name|Beta
annotation|@
name|CanIgnoreReturnValue
comment|// some processors won't return a useful result
DECL|method|readLines (LineProcessor<T> processor)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|readLines
parameter_list|(
name|LineProcessor
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
name|Reader
name|reader
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
name|CharStreams
operator|.
name|readLines
argument_list|(
name|reader
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
comment|/**    * Returns whether the source has zero chars. The default implementation returns true if    * {@link #lengthIfKnown} returns zero, falling back to opening a stream and checking for EOF if    * the length is not known.    *    *<p>Note that, in cases where {@code lengthIfKnown} returns zero, it is<i>possible</i> that    * chars are actually available for reading. This means that a source may return {@code true} from    * {@code isEmpty()} despite having readable content.    *    * @throws IOException if an I/O error occurs    * @since 15.0    */
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
throws|throws
name|IOException
block|{
name|Optional
argument_list|<
name|Long
argument_list|>
name|lengthIfKnown
init|=
name|lengthIfKnown
argument_list|()
decl_stmt|;
if|if
condition|(
name|lengthIfKnown
operator|.
name|isPresent
argument_list|()
operator|&&
name|lengthIfKnown
operator|.
name|get
argument_list|()
operator|==
literal|0L
condition|)
block|{
return|return
literal|true
return|;
block|}
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
name|Reader
name|reader
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
name|reader
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
comment|/**    * Concatenates multiple {@link CharSource} instances into a single source. Streams returned from    * the source will contain the concatenated data from the streams of the underlying sources.    *    *<p>Only one underlying stream will be open at a time. Closing the concatenated stream will    * close the open underlying stream.    *    * @param sources the sources to concatenate    * @return a {@code CharSource} containing the concatenated data    * @since 15.0    */
DECL|method|concat (Iterable<? extends CharSource> sources)
specifier|public
specifier|static
name|CharSource
name|concat
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|CharSource
argument_list|>
name|sources
parameter_list|)
block|{
return|return
operator|new
name|ConcatenatedCharSource
argument_list|(
name|sources
argument_list|)
return|;
block|}
comment|/**    * Concatenates multiple {@link CharSource} instances into a single source. Streams returned from    * the source will contain the concatenated data from the streams of the underlying sources.    *    *<p>Only one underlying stream will be open at a time. Closing the concatenated stream will    * close the open underlying stream.    *    *<p>Note: The input {@code Iterator} will be copied to an {@code ImmutableList} when this method    * is called. This will fail if the iterator is infinite and may cause problems if the iterator    * eagerly fetches data for each source when iterated (rather than producing sources that only    * load data through their streams). Prefer using the {@link #concat(Iterable)} overload if    * possible.    *    * @param sources the sources to concatenate    * @return a {@code CharSource} containing the concatenated data    * @throws NullPointerException if any of {@code sources} is {@code null}    * @since 15.0    */
DECL|method|concat (Iterator<? extends CharSource> sources)
specifier|public
specifier|static
name|CharSource
name|concat
parameter_list|(
name|Iterator
argument_list|<
name|?
extends|extends
name|CharSource
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
comment|/**    * Concatenates multiple {@link CharSource} instances into a single source. Streams returned from    * the source will contain the concatenated data from the streams of the underlying sources.    *    *<p>Only one underlying stream will be open at a time. Closing the concatenated stream will    * close the open underlying stream.    *    * @param sources the sources to concatenate    * @return a {@code CharSource} containing the concatenated data    * @throws NullPointerException if any of {@code sources} is {@code null}    * @since 15.0    */
DECL|method|concat (CharSource... sources)
specifier|public
specifier|static
name|CharSource
name|concat
parameter_list|(
name|CharSource
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
comment|/**    * Returns a view of the given character sequence as a {@link CharSource}. The behavior of the    * returned {@code CharSource} and any {@code Reader} instances created by it is unspecified if    * the {@code charSequence} is mutated while it is being read, so don't do that.    *    * @since 15.0 (since 14.0 as {@code CharStreams.asCharSource(String)})    */
DECL|method|wrap (CharSequence charSequence)
specifier|public
specifier|static
name|CharSource
name|wrap
parameter_list|(
name|CharSequence
name|charSequence
parameter_list|)
block|{
return|return
operator|new
name|CharSequenceCharSource
argument_list|(
name|charSequence
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable {@link CharSource} that contains no characters.    *    * @since 15.0    */
DECL|method|empty ()
specifier|public
specifier|static
name|CharSource
name|empty
parameter_list|()
block|{
return|return
name|EmptyCharSource
operator|.
name|INSTANCE
return|;
block|}
comment|/**    * A byte source that reads chars from this source and encodes them as bytes using a charset.    */
DECL|class|AsByteSource
specifier|private
specifier|final
class|class
name|AsByteSource
extends|extends
name|ByteSource
block|{
DECL|field|charset
specifier|final
name|Charset
name|charset
decl_stmt|;
DECL|method|AsByteSource (Charset charset)
name|AsByteSource
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
DECL|method|asCharSource (Charset charset)
specifier|public
name|CharSource
name|asCharSource
parameter_list|(
name|Charset
name|charset
parameter_list|)
block|{
if|if
condition|(
name|charset
operator|.
name|equals
argument_list|(
name|this
operator|.
name|charset
argument_list|)
condition|)
block|{
return|return
name|CharSource
operator|.
name|this
return|;
block|}
return|return
name|super
operator|.
name|asCharSource
argument_list|(
name|charset
argument_list|)
return|;
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
name|ReaderInputStream
argument_list|(
name|CharSource
operator|.
name|this
operator|.
name|openStream
argument_list|()
argument_list|,
name|charset
argument_list|,
literal|8192
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
name|CharSource
operator|.
name|this
operator|.
name|toString
argument_list|()
operator|+
literal|".asByteSource("
operator|+
name|charset
operator|+
literal|")"
return|;
block|}
block|}
DECL|class|CharSequenceCharSource
specifier|private
specifier|static
class|class
name|CharSequenceCharSource
extends|extends
name|CharSource
block|{
DECL|field|LINE_SPLITTER
specifier|private
specifier|static
specifier|final
name|Splitter
name|LINE_SPLITTER
init|=
name|Splitter
operator|.
name|onPattern
argument_list|(
literal|"\r\n|\n|\r"
argument_list|)
decl_stmt|;
DECL|field|seq
specifier|private
specifier|final
name|CharSequence
name|seq
decl_stmt|;
DECL|method|CharSequenceCharSource (CharSequence seq)
specifier|protected
name|CharSequenceCharSource
parameter_list|(
name|CharSequence
name|seq
parameter_list|)
block|{
name|this
operator|.
name|seq
operator|=
name|checkNotNull
argument_list|(
name|seq
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
block|{
return|return
operator|new
name|CharSequenceReader
argument_list|(
name|seq
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|read ()
specifier|public
name|String
name|read
parameter_list|()
block|{
return|return
name|seq
operator|.
name|toString
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
name|seq
operator|.
name|length
argument_list|()
operator|==
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|length ()
specifier|public
name|long
name|length
parameter_list|()
block|{
return|return
name|seq
operator|.
name|length
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|lengthIfKnown ()
specifier|public
name|Optional
argument_list|<
name|Long
argument_list|>
name|lengthIfKnown
parameter_list|()
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
operator|(
name|long
operator|)
name|seq
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns an iterable over the lines in the string. If the string ends in a newline, a final      * empty string is not included to match the behavior of BufferedReader/LineReader.readLine().      */
DECL|method|lines ()
specifier|private
name|Iterable
argument_list|<
name|String
argument_list|>
name|lines
parameter_list|()
block|{
return|return
operator|new
name|Iterable
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|AbstractIterator
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
name|Iterator
argument_list|<
name|String
argument_list|>
name|lines
init|=
name|LINE_SPLITTER
operator|.
name|split
argument_list|(
name|seq
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|String
name|computeNext
parameter_list|()
block|{
if|if
condition|(
name|lines
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|next
init|=
name|lines
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// skip last line if it's empty
if|if
condition|(
name|lines
operator|.
name|hasNext
argument_list|()
operator|||
operator|!
name|next
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|next
return|;
block|}
block|}
return|return
name|endOfData
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|readFirstLine ()
specifier|public
name|String
name|readFirstLine
parameter_list|()
block|{
name|Iterator
argument_list|<
name|String
argument_list|>
name|lines
init|=
name|lines
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
name|lines
operator|.
name|hasNext
argument_list|()
condition|?
name|lines
operator|.
name|next
argument_list|()
else|:
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|readLines ()
specifier|public
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|readLines
parameter_list|()
block|{
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|lines
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|readLines (LineProcessor<T> processor)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|readLines
parameter_list|(
name|LineProcessor
argument_list|<
name|T
argument_list|>
name|processor
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|String
name|line
range|:
name|lines
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|processor
operator|.
name|processLine
argument_list|(
name|line
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
return|return
name|processor
operator|.
name|getResult
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
literal|"CharSource.wrap("
operator|+
name|Ascii
operator|.
name|truncate
argument_list|(
name|seq
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
DECL|class|EmptyCharSource
specifier|private
specifier|static
specifier|final
class|class
name|EmptyCharSource
extends|extends
name|CharSequenceCharSource
block|{
DECL|field|INSTANCE
specifier|private
specifier|static
specifier|final
name|EmptyCharSource
name|INSTANCE
init|=
operator|new
name|EmptyCharSource
argument_list|()
decl_stmt|;
DECL|method|EmptyCharSource ()
specifier|private
name|EmptyCharSource
parameter_list|()
block|{
name|super
argument_list|(
literal|""
argument_list|)
expr_stmt|;
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
literal|"CharSource.empty()"
return|;
block|}
block|}
DECL|class|ConcatenatedCharSource
specifier|private
specifier|static
specifier|final
class|class
name|ConcatenatedCharSource
extends|extends
name|CharSource
block|{
DECL|field|sources
specifier|private
specifier|final
name|Iterable
argument_list|<
name|?
extends|extends
name|CharSource
argument_list|>
name|sources
decl_stmt|;
DECL|method|ConcatenatedCharSource (Iterable<? extends CharSource> sources)
name|ConcatenatedCharSource
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|CharSource
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
name|Reader
name|openStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|MultiReader
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
name|CharSource
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
DECL|method|lengthIfKnown ()
specifier|public
name|Optional
argument_list|<
name|Long
argument_list|>
name|lengthIfKnown
parameter_list|()
block|{
name|long
name|result
init|=
literal|0L
decl_stmt|;
for|for
control|(
name|CharSource
name|source
range|:
name|sources
control|)
block|{
name|Optional
argument_list|<
name|Long
argument_list|>
name|lengthIfKnown
init|=
name|source
operator|.
name|lengthIfKnown
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|lengthIfKnown
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|Optional
operator|.
name|absent
argument_list|()
return|;
block|}
name|result
operator|+=
name|lengthIfKnown
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
return|return
name|Optional
operator|.
name|of
argument_list|(
name|result
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|length ()
specifier|public
name|long
name|length
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
name|CharSource
name|source
range|:
name|sources
control|)
block|{
name|result
operator|+=
name|source
operator|.
name|length
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
literal|"CharSource.concat("
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

