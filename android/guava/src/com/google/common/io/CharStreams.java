begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|GwtIncompatible
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
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|EOFException
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
name|CharBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_comment
comment|/**  * Provides utility methods for working with character streams.  *  *<p>All method parameters must be non-null unless documented otherwise.  *  *<p>Some of the methods in this class take arguments with a generic type of {@code Readable&  * Closeable}. A {@link java.io.Reader} implements both of those interfaces. Similarly for {@code  * Appendable& Closeable} and {@link java.io.Writer}.  *  * @author Chris Nokleberg  * @author Bin Zhu  * @author Colin Decker  * @since 1.0  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|CharStreams
specifier|public
specifier|final
class|class
name|CharStreams
block|{
comment|// 2K chars (4K bytes)
DECL|field|DEFAULT_BUF_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_BUF_SIZE
init|=
literal|0x800
decl_stmt|;
comment|/** Creates a new {@code CharBuffer} for buffering reads or writes. */
DECL|method|createBuffer ()
specifier|static
name|CharBuffer
name|createBuffer
parameter_list|()
block|{
return|return
name|CharBuffer
operator|.
name|allocate
argument_list|(
name|DEFAULT_BUF_SIZE
argument_list|)
return|;
block|}
DECL|method|CharStreams ()
specifier|private
name|CharStreams
parameter_list|()
block|{}
comment|/**    * Copies all characters between the {@link Readable} and {@link Appendable} objects. Does not    * close or flush either object.    *    * @param from the object to read from    * @param to the object to write to    * @return the number of characters copied    * @throws IOException if an I/O error occurs    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|copy (Readable from, Appendable to)
specifier|public
specifier|static
name|long
name|copy
parameter_list|(
name|Readable
name|from
parameter_list|,
name|Appendable
name|to
parameter_list|)
throws|throws
name|IOException
block|{
comment|// The most common case is that from is a Reader (like InputStreamReader or StringReader) so
comment|// take advantage of that.
if|if
condition|(
name|from
operator|instanceof
name|Reader
condition|)
block|{
comment|// optimize for common output types which are optimized to deal with char[]
if|if
condition|(
name|to
operator|instanceof
name|StringBuilder
condition|)
block|{
return|return
name|copyReaderToBuilder
argument_list|(
operator|(
name|Reader
operator|)
name|from
argument_list|,
operator|(
name|StringBuilder
operator|)
name|to
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|copyReaderToWriter
argument_list|(
operator|(
name|Reader
operator|)
name|from
argument_list|,
name|asWriter
argument_list|(
name|to
argument_list|)
argument_list|)
return|;
block|}
block|}
else|else
block|{
name|checkNotNull
argument_list|(
name|from
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|to
argument_list|)
expr_stmt|;
name|long
name|total
init|=
literal|0
decl_stmt|;
name|CharBuffer
name|buf
init|=
name|createBuffer
argument_list|()
decl_stmt|;
while|while
condition|(
name|from
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|buf
operator|.
name|flip
argument_list|()
expr_stmt|;
name|to
operator|.
name|append
argument_list|(
name|buf
argument_list|)
expr_stmt|;
name|total
operator|+=
name|buf
operator|.
name|remaining
argument_list|()
expr_stmt|;
name|buf
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
return|return
name|total
return|;
block|}
block|}
comment|// TODO(lukes): consider allowing callers to pass in a buffer to use, some callers would be able
comment|// to reuse buffers, others would be able to size them more appropriately than the constant
comment|// defaults
comment|/**    * Copies all characters between the {@link Reader} and {@link StringBuilder} objects. Does not    * close or flush the reader.    *    *<p>This is identical to {@link #copy(Readable, Appendable)} but optimized for these specific    * types. CharBuffer has poor performance when being written into or read out of so round tripping    * all the bytes through the buffer takes a long time. With these specialized types we can just    * use a char array.    *    * @param from the object to read from    * @param to the object to write to    * @return the number of characters copied    * @throws IOException if an I/O error occurs    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|copyReaderToBuilder (Reader from, StringBuilder to)
specifier|static
name|long
name|copyReaderToBuilder
parameter_list|(
name|Reader
name|from
parameter_list|,
name|StringBuilder
name|to
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotNull
argument_list|(
name|from
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|to
argument_list|)
expr_stmt|;
name|char
index|[]
name|buf
init|=
operator|new
name|char
index|[
name|DEFAULT_BUF_SIZE
index|]
decl_stmt|;
name|int
name|nRead
decl_stmt|;
name|long
name|total
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|nRead
operator|=
name|from
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|to
operator|.
name|append
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|nRead
argument_list|)
expr_stmt|;
name|total
operator|+=
name|nRead
expr_stmt|;
block|}
return|return
name|total
return|;
block|}
comment|/**    * Copies all characters between the {@link Reader} and {@link Writer} objects. Does not close or    * flush the reader or writer.    *    *<p>This is identical to {@link #copy(Readable, Appendable)} but optimized for these specific    * types. CharBuffer has poor performance when being written into or read out of so round tripping    * all the bytes through the buffer takes a long time. With these specialized types we can just    * use a char array.    *    * @param from the object to read from    * @param to the object to write to    * @return the number of characters copied    * @throws IOException if an I/O error occurs    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|copyReaderToWriter (Reader from, Writer to)
specifier|static
name|long
name|copyReaderToWriter
parameter_list|(
name|Reader
name|from
parameter_list|,
name|Writer
name|to
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotNull
argument_list|(
name|from
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|to
argument_list|)
expr_stmt|;
name|char
index|[]
name|buf
init|=
operator|new
name|char
index|[
name|DEFAULT_BUF_SIZE
index|]
decl_stmt|;
name|int
name|nRead
decl_stmt|;
name|long
name|total
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|nRead
operator|=
name|from
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|to
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|nRead
argument_list|)
expr_stmt|;
name|total
operator|+=
name|nRead
expr_stmt|;
block|}
return|return
name|total
return|;
block|}
comment|/**    * Reads all characters from a {@link Readable} object into a {@link String}. Does not close the    * {@code Readable}.    *    * @param r the object to read from    * @return a string containing all the characters    * @throws IOException if an I/O error occurs    */
DECL|method|toString (Readable r)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Readable
name|r
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toStringBuilder
argument_list|(
name|r
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**    * Reads all characters from a {@link Readable} object into a new {@link StringBuilder} instance.    * Does not close the {@code Readable}.    *    * @param r the object to read from    * @return a {@link StringBuilder} containing all the characters    * @throws IOException if an I/O error occurs    */
DECL|method|toStringBuilder (Readable r)
specifier|private
specifier|static
name|StringBuilder
name|toStringBuilder
parameter_list|(
name|Readable
name|r
parameter_list|)
throws|throws
name|IOException
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|r
operator|instanceof
name|Reader
condition|)
block|{
name|copyReaderToBuilder
argument_list|(
operator|(
name|Reader
operator|)
name|r
argument_list|,
name|sb
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|copy
argument_list|(
name|r
argument_list|,
name|sb
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
return|;
block|}
comment|/**    * Reads all of the lines from a {@link Readable} object. The lines do not include    * line-termination characters, but do include other leading and trailing whitespace.    *    *<p>Does not close the {@code Readable}. If reading files or resources you should use the {@link    * Files#readLines} and {@link Resources#readLines} methods.    *    * @param r the object to read from    * @return a mutable {@link List} containing all the lines    * @throws IOException if an I/O error occurs    */
annotation|@
name|Beta
DECL|method|readLines (Readable r)
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|readLines
parameter_list|(
name|Readable
name|r
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|LineReader
name|lineReader
init|=
operator|new
name|LineReader
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|String
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|lineReader
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
name|result
return|;
block|}
comment|/**    * Streams lines from a {@link Readable} object, stopping when the processor returns {@code false}    * or all lines have been read and returning the result produced by the processor. Does not close    * {@code readable}. Note that this method may not fully consume the contents of {@code readable}    * if the processor stops processing early.    *    * @throws IOException if an I/O error occurs    * @since 14.0    */
annotation|@
name|Beta
annotation|@
name|CanIgnoreReturnValue
comment|// some processors won't return a useful result
DECL|method|readLines (Readable readable, LineProcessor<T> processor)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|readLines
parameter_list|(
name|Readable
name|readable
parameter_list|,
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
name|readable
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|LineReader
name|lineReader
init|=
operator|new
name|LineReader
argument_list|(
name|readable
argument_list|)
decl_stmt|;
name|String
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|lineReader
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
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
comment|/**    * Reads and discards data from the given {@code Readable} until the end of the stream is reached.    * Returns the total number of chars read. Does not close the stream.    *    * @since 20.0    */
annotation|@
name|Beta
annotation|@
name|CanIgnoreReturnValue
DECL|method|exhaust (Readable readable)
specifier|public
specifier|static
name|long
name|exhaust
parameter_list|(
name|Readable
name|readable
parameter_list|)
throws|throws
name|IOException
block|{
name|long
name|total
init|=
literal|0
decl_stmt|;
name|long
name|read
decl_stmt|;
name|CharBuffer
name|buf
init|=
name|createBuffer
argument_list|()
decl_stmt|;
while|while
condition|(
operator|(
name|read
operator|=
name|readable
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|total
operator|+=
name|read
expr_stmt|;
name|buf
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
return|return
name|total
return|;
block|}
comment|/**    * Discards {@code n} characters of data from the reader. This method will block until the full    * amount has been skipped. Does not close the reader.    *    * @param reader the reader to read from    * @param n the number of characters to skip    * @throws EOFException if this stream reaches the end before skipping all the characters    * @throws IOException if an I/O error occurs    */
annotation|@
name|Beta
DECL|method|skipFully (Reader reader, long n)
specifier|public
specifier|static
name|void
name|skipFully
parameter_list|(
name|Reader
name|reader
parameter_list|,
name|long
name|n
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotNull
argument_list|(
name|reader
argument_list|)
expr_stmt|;
while|while
condition|(
name|n
operator|>
literal|0
condition|)
block|{
name|long
name|amt
init|=
name|reader
operator|.
name|skip
argument_list|(
name|n
argument_list|)
decl_stmt|;
if|if
condition|(
name|amt
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|EOFException
argument_list|()
throw|;
block|}
name|n
operator|-=
name|amt
expr_stmt|;
block|}
block|}
comment|/**    * Returns a {@link Writer} that simply discards written chars.    *    * @since 15.0    */
annotation|@
name|Beta
DECL|method|nullWriter ()
specifier|public
specifier|static
name|Writer
name|nullWriter
parameter_list|()
block|{
return|return
name|NullWriter
operator|.
name|INSTANCE
return|;
block|}
DECL|class|NullWriter
specifier|private
specifier|static
specifier|final
class|class
name|NullWriter
extends|extends
name|Writer
block|{
DECL|field|INSTANCE
specifier|private
specifier|static
specifier|final
name|NullWriter
name|INSTANCE
init|=
operator|new
name|NullWriter
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|write (int c)
specifier|public
name|void
name|write
parameter_list|(
name|int
name|c
parameter_list|)
block|{}
annotation|@
name|Override
DECL|method|write (char[] cbuf)
specifier|public
name|void
name|write
parameter_list|(
name|char
index|[]
name|cbuf
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|cbuf
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|write (char[] cbuf, int off, int len)
specifier|public
name|void
name|write
parameter_list|(
name|char
index|[]
name|cbuf
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
name|checkPositionIndexes
argument_list|(
name|off
argument_list|,
name|off
operator|+
name|len
argument_list|,
name|cbuf
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|write (String str)
specifier|public
name|void
name|write
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|write (String str, int off, int len)
specifier|public
name|void
name|write
parameter_list|(
name|String
name|str
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
name|checkPositionIndexes
argument_list|(
name|off
argument_list|,
name|off
operator|+
name|len
argument_list|,
name|str
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|append (@ullableDecl CharSequence csq)
specifier|public
name|Writer
name|append
parameter_list|(
annotation|@
name|NullableDecl
name|CharSequence
name|csq
parameter_list|)
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|append (@ullableDecl CharSequence csq, int start, int end)
specifier|public
name|Writer
name|append
parameter_list|(
annotation|@
name|NullableDecl
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
name|checkPositionIndexes
argument_list|(
name|start
argument_list|,
name|end
argument_list|,
name|csq
operator|==
literal|null
condition|?
literal|"null"
operator|.
name|length
argument_list|()
else|:
name|csq
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|append (char c)
specifier|public
name|Writer
name|append
parameter_list|(
name|char
name|c
parameter_list|)
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|flush ()
specifier|public
name|void
name|flush
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"CharStreams.nullWriter()"
return|;
block|}
block|}
comment|/**    * Returns a Writer that sends all output to the given {@link Appendable} target. Closing the    * writer will close the target if it is {@link Closeable}, and flushing the writer will flush the    * target if it is {@link java.io.Flushable}.    *    * @param target the object to which output will be sent    * @return a new Writer object, unless target is a Writer, in which case the target is returned    */
annotation|@
name|Beta
DECL|method|asWriter (Appendable target)
specifier|public
specifier|static
name|Writer
name|asWriter
parameter_list|(
name|Appendable
name|target
parameter_list|)
block|{
if|if
condition|(
name|target
operator|instanceof
name|Writer
condition|)
block|{
return|return
operator|(
name|Writer
operator|)
name|target
return|;
block|}
return|return
operator|new
name|AppendableWriter
argument_list|(
name|target
argument_list|)
return|;
block|}
block|}
end_class

end_unit

