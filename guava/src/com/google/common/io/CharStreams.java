begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Charsets
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
name|Preconditions
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
name|OutputStreamWriter
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
name|StringReader
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
name|ArrayList
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
name|List
import|;
end_import

begin_comment
comment|/**  * Provides utility methods for working with character streams.  *  *<p>All method parameters must be non-null unless documented otherwise.  *  *<p>Some of the methods in this class take arguments with a generic type of  * {@code Readable& Closeable}. A {@link java.io.Reader} implements both of  * those interfaces. Similarly for {@code Appendable& Closeable} and  * {@link java.io.Writer}.  *  * @author Chris Nokleberg  * @author Bin Zhu  * @since 1.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|CharStreams
specifier|public
specifier|final
class|class
name|CharStreams
block|{
DECL|field|BUF_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|BUF_SIZE
init|=
literal|0x800
decl_stmt|;
comment|// 2K chars (4K bytes)
DECL|method|CharStreams ()
specifier|private
name|CharStreams
parameter_list|()
block|{}
comment|/**    * Returns a factory that will supply instances of {@link StringReader} that    * read a string value.    *    * @param value the string to read    * @return the factory    */
DECL|method|newReaderSupplier ( final String value)
specifier|public
specifier|static
name|InputSupplier
argument_list|<
name|StringReader
argument_list|>
name|newReaderSupplier
parameter_list|(
specifier|final
name|String
name|value
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
operator|new
name|InputSupplier
argument_list|<
name|StringReader
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|StringReader
name|getInput
parameter_list|()
block|{
return|return
operator|new
name|StringReader
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**    * Returns a factory that will supply instances of {@link InputStreamReader},    * using the given {@link InputStream} factory and character set.    *    * @param in the factory that will be used to open input streams    * @param charset the charset used to decode the input stream; see {@link    *     Charsets} for helpful predefined constants    * @return the factory    */
DECL|method|newReaderSupplier ( final InputSupplier<? extends InputStream> in, final Charset charset)
specifier|public
specifier|static
name|InputSupplier
argument_list|<
name|InputStreamReader
argument_list|>
name|newReaderSupplier
parameter_list|(
specifier|final
name|InputSupplier
argument_list|<
name|?
extends|extends
name|InputStream
argument_list|>
name|in
parameter_list|,
specifier|final
name|Charset
name|charset
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|charset
argument_list|)
expr_stmt|;
return|return
operator|new
name|InputSupplier
argument_list|<
name|InputStreamReader
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|InputStreamReader
name|getInput
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|InputStreamReader
argument_list|(
name|in
operator|.
name|getInput
argument_list|()
argument_list|,
name|charset
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**    * Returns a factory that will supply instances of {@link OutputStreamWriter},    * using the given {@link OutputStream} factory and character set.    *    * @param out the factory that will be used to open output streams    * @param charset the charset used to encode the output stream; see {@link    *     Charsets} for helpful predefined constants    * @return the factory    */
DECL|method|newWriterSupplier ( final OutputSupplier<? extends OutputStream> out, final Charset charset)
specifier|public
specifier|static
name|OutputSupplier
argument_list|<
name|OutputStreamWriter
argument_list|>
name|newWriterSupplier
parameter_list|(
specifier|final
name|OutputSupplier
argument_list|<
name|?
extends|extends
name|OutputStream
argument_list|>
name|out
parameter_list|,
specifier|final
name|Charset
name|charset
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|charset
argument_list|)
expr_stmt|;
return|return
operator|new
name|OutputSupplier
argument_list|<
name|OutputStreamWriter
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|OutputStreamWriter
name|getOutput
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|OutputStreamWriter
argument_list|(
name|out
operator|.
name|getOutput
argument_list|()
argument_list|,
name|charset
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**    * Writes a character sequence (such as a string) to an appendable    * object from the given supplier.    *    * @param from the character sequence to write    * @param to the output supplier    * @throws IOException if an I/O error occurs    */
DECL|method|write (CharSequence from, OutputSupplier<W> to)
specifier|public
specifier|static
parameter_list|<
name|W
extends|extends
name|Appendable
operator|&
name|Closeable
parameter_list|>
name|void
name|write
parameter_list|(
name|CharSequence
name|from
parameter_list|,
name|OutputSupplier
argument_list|<
name|W
argument_list|>
name|to
parameter_list|)
throws|throws
name|IOException
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|from
argument_list|)
expr_stmt|;
name|boolean
name|threw
init|=
literal|true
decl_stmt|;
name|W
name|out
init|=
name|to
operator|.
name|getOutput
argument_list|()
decl_stmt|;
try|try
block|{
name|out
operator|.
name|append
argument_list|(
name|from
argument_list|)
expr_stmt|;
name|threw
operator|=
literal|false
expr_stmt|;
block|}
finally|finally
block|{
name|Closeables
operator|.
name|close
argument_list|(
name|out
argument_list|,
name|threw
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Opens {@link Readable} and {@link Appendable} objects from the    * given factories, copies all characters between the two, and closes    * them.    *    * @param from the input factory    * @param to the output factory    * @return the number of characters copied    * @throws IOException if an I/O error occurs    */
specifier|public
specifier|static
parameter_list|<
name|R
extends|extends
name|Readable
operator|&
name|Closeable
parameter_list|,
DECL|method|copy (InputSupplier<R> from, OutputSupplier<W> to)
name|W
extends|extends
name|Appendable
operator|&
name|Closeable
parameter_list|>
name|long
name|copy
parameter_list|(
name|InputSupplier
argument_list|<
name|R
argument_list|>
name|from
parameter_list|,
name|OutputSupplier
argument_list|<
name|W
argument_list|>
name|to
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|successfulOps
init|=
literal|0
decl_stmt|;
name|R
name|in
init|=
name|from
operator|.
name|getInput
argument_list|()
decl_stmt|;
try|try
block|{
name|W
name|out
init|=
name|to
operator|.
name|getOutput
argument_list|()
decl_stmt|;
try|try
block|{
name|long
name|count
init|=
name|copy
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
decl_stmt|;
name|successfulOps
operator|++
expr_stmt|;
return|return
name|count
return|;
block|}
finally|finally
block|{
name|Closeables
operator|.
name|close
argument_list|(
name|out
argument_list|,
name|successfulOps
operator|<
literal|1
argument_list|)
expr_stmt|;
name|successfulOps
operator|++
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|Closeables
operator|.
name|close
argument_list|(
name|in
argument_list|,
name|successfulOps
operator|<
literal|2
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Opens a {@link Readable} object from the supplier, copies all characters    * to the {@link Appendable} object, and closes the input. Does not close    * or flush the output.    *    * @param from the input factory    * @param to the object to write to    * @return the number of characters copied    * @throws IOException if an I/O error occurs    */
DECL|method|copy ( InputSupplier<R> from, Appendable to)
specifier|public
specifier|static
parameter_list|<
name|R
extends|extends
name|Readable
operator|&
name|Closeable
parameter_list|>
name|long
name|copy
parameter_list|(
name|InputSupplier
argument_list|<
name|R
argument_list|>
name|from
parameter_list|,
name|Appendable
name|to
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|threw
init|=
literal|true
decl_stmt|;
name|R
name|in
init|=
name|from
operator|.
name|getInput
argument_list|()
decl_stmt|;
try|try
block|{
name|long
name|count
init|=
name|copy
argument_list|(
name|in
argument_list|,
name|to
argument_list|)
decl_stmt|;
name|threw
operator|=
literal|false
expr_stmt|;
return|return
name|count
return|;
block|}
finally|finally
block|{
name|Closeables
operator|.
name|close
argument_list|(
name|in
argument_list|,
name|threw
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Copies all characters between the {@link Readable} and {@link Appendable}    * objects. Does not close or flush either object.    *    * @param from the object to read from    * @param to the object to write to    * @return the number of characters copied    * @throws IOException if an I/O error occurs    */
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
name|CharBuffer
name|buf
init|=
name|CharBuffer
operator|.
name|allocate
argument_list|(
name|BUF_SIZE
argument_list|)
decl_stmt|;
name|long
name|total
init|=
literal|0
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
comment|/**    * Reads all characters from a {@link Readable} object into a {@link String}.    * Does not close the {@code Readable}.    *    * @param r the object to read from    * @return a string containing all the characters    * @throws IOException if an I/O error occurs    */
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
comment|/**    * Returns the characters from a {@link Readable}& {@link Closeable} object    * supplied by a factory as a {@link String}.    *    * @param supplier the factory to read from    * @return a string containing all the characters    * @throws IOException if an I/O error occurs    */
DECL|method|toString ( InputSupplier<R> supplier)
specifier|public
specifier|static
parameter_list|<
name|R
extends|extends
name|Readable
operator|&
name|Closeable
parameter_list|>
name|String
name|toString
parameter_list|(
name|InputSupplier
argument_list|<
name|R
argument_list|>
name|supplier
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toStringBuilder
argument_list|(
name|supplier
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**    * Reads all characters from a {@link Readable} object into a new    * {@link StringBuilder} instance. Does not close the {@code Readable}.    *    * @param r the object to read from    * @return a {@link StringBuilder} containing all the characters    * @throws IOException if an I/O error occurs    */
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
name|copy
argument_list|(
name|r
argument_list|,
name|sb
argument_list|)
expr_stmt|;
return|return
name|sb
return|;
block|}
comment|/**    * Returns the characters from a {@link Readable}& {@link Closeable} object    * supplied by a factory as a new {@link StringBuilder} instance.    *    * @param supplier the factory to read from    * @throws IOException if an I/O error occurs    */
DECL|method|toStringBuilder ( InputSupplier<R> supplier)
specifier|private
specifier|static
parameter_list|<
name|R
extends|extends
name|Readable
operator|&
name|Closeable
parameter_list|>
name|StringBuilder
name|toStringBuilder
parameter_list|(
name|InputSupplier
argument_list|<
name|R
argument_list|>
name|supplier
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|threw
init|=
literal|true
decl_stmt|;
name|R
name|r
init|=
name|supplier
operator|.
name|getInput
argument_list|()
decl_stmt|;
try|try
block|{
name|StringBuilder
name|result
init|=
name|toStringBuilder
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|threw
operator|=
literal|false
expr_stmt|;
return|return
name|result
return|;
block|}
finally|finally
block|{
name|Closeables
operator|.
name|close
argument_list|(
name|r
argument_list|,
name|threw
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Reads the first line from a {@link Readable}& {@link Closeable} object    * supplied by a factory. The line does not include line-termination    * characters, but does include other leading and trailing whitespace.    *    * @param supplier the factory to read from    * @return the first line, or null if the reader is empty    * @throws IOException if an I/O error occurs    */
DECL|method|readFirstLine ( InputSupplier<R> supplier)
specifier|public
specifier|static
parameter_list|<
name|R
extends|extends
name|Readable
operator|&
name|Closeable
parameter_list|>
name|String
name|readFirstLine
parameter_list|(
name|InputSupplier
argument_list|<
name|R
argument_list|>
name|supplier
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|threw
init|=
literal|true
decl_stmt|;
name|R
name|r
init|=
name|supplier
operator|.
name|getInput
argument_list|()
decl_stmt|;
try|try
block|{
name|String
name|line
init|=
operator|new
name|LineReader
argument_list|(
name|r
argument_list|)
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|threw
operator|=
literal|false
expr_stmt|;
return|return
name|line
return|;
block|}
finally|finally
block|{
name|Closeables
operator|.
name|close
argument_list|(
name|r
argument_list|,
name|threw
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Reads all of the lines from a {@link Readable}& {@link Closeable} object    * supplied by a factory. The lines do not include line-termination    * characters, but do include other leading and trailing whitespace.    *    * @param supplier the factory to read from    * @return a mutable {@link List} containing all the lines    * @throws IOException if an I/O error occurs    */
DECL|method|readLines ( InputSupplier<R> supplier)
specifier|public
specifier|static
parameter_list|<
name|R
extends|extends
name|Readable
operator|&
name|Closeable
parameter_list|>
name|List
argument_list|<
name|String
argument_list|>
name|readLines
parameter_list|(
name|InputSupplier
argument_list|<
name|R
argument_list|>
name|supplier
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|threw
init|=
literal|true
decl_stmt|;
name|R
name|r
init|=
name|supplier
operator|.
name|getInput
argument_list|()
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|String
argument_list|>
name|result
init|=
name|readLines
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|threw
operator|=
literal|false
expr_stmt|;
return|return
name|result
return|;
block|}
finally|finally
block|{
name|Closeables
operator|.
name|close
argument_list|(
name|r
argument_list|,
name|threw
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Reads all of the lines from a {@link Readable} object. The lines do    * not include line-termination characters, but do include other    * leading and trailing whitespace.    *    *<p>Does not close the {@code Readable}. If reading files or resources you    * should use the {@link Files#readLines} and {@link Resources#readLines}    * methods.    *    * @param r the object to read from    * @return a mutable {@link List} containing all the lines    * @throws IOException if an I/O error occurs    */
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
argument_list|<
name|String
argument_list|>
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
comment|/**    * Streams lines from a {@link Readable} and {@link Closeable} object    * supplied by a factory, stopping when our callback returns false, or we    * have read all of the lines.    *    * @param supplier the factory to read from    * @param callback the LineProcessor to use to handle the lines    * @return the output of processing the lines    * @throws IOException if an I/O error occurs    */
DECL|method|readLines ( InputSupplier<R> supplier, LineProcessor<T> callback)
specifier|public
specifier|static
parameter_list|<
name|R
extends|extends
name|Readable
operator|&
name|Closeable
parameter_list|,
name|T
parameter_list|>
name|T
name|readLines
parameter_list|(
name|InputSupplier
argument_list|<
name|R
argument_list|>
name|supplier
parameter_list|,
name|LineProcessor
argument_list|<
name|T
argument_list|>
name|callback
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|threw
init|=
literal|true
decl_stmt|;
name|R
name|r
init|=
name|supplier
operator|.
name|getInput
argument_list|()
decl_stmt|;
try|try
block|{
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
if|if
condition|(
operator|!
name|callback
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
name|threw
operator|=
literal|false
expr_stmt|;
block|}
finally|finally
block|{
name|Closeables
operator|.
name|close
argument_list|(
name|r
argument_list|,
name|threw
argument_list|)
expr_stmt|;
block|}
return|return
name|callback
operator|.
name|getResult
argument_list|()
return|;
block|}
comment|/**    * Joins multiple {@link Reader} suppliers into a single supplier.    * Reader returned from the supplier will contain the concatenated data    * from the readers of the underlying suppliers.    *    *<p>Reading from the joined reader will throw a {@link NullPointerException}    * if any of the suppliers are null or return null.    *    *<p>Only one underlying reader will be open at a time. Closing the    * joined reader will close the open underlying reader.    *    * @param suppliers the suppliers to concatenate    * @return a supplier that will return a reader containing the concatenated    *     data    */
DECL|method|join ( final Iterable<? extends InputSupplier<? extends Reader>> suppliers)
specifier|public
specifier|static
name|InputSupplier
argument_list|<
name|Reader
argument_list|>
name|join
parameter_list|(
specifier|final
name|Iterable
argument_list|<
name|?
extends|extends
name|InputSupplier
argument_list|<
name|?
extends|extends
name|Reader
argument_list|>
argument_list|>
name|suppliers
parameter_list|)
block|{
return|return
operator|new
name|InputSupplier
argument_list|<
name|Reader
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Reader
name|getInput
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|MultiReader
argument_list|(
name|suppliers
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/** Varargs form of {@link #join(Iterable)}. */
DECL|method|join ( InputSupplier<? extends Reader>.... suppliers)
specifier|public
specifier|static
name|InputSupplier
argument_list|<
name|Reader
argument_list|>
name|join
parameter_list|(
name|InputSupplier
argument_list|<
name|?
extends|extends
name|Reader
argument_list|>
modifier|...
name|suppliers
parameter_list|)
block|{
return|return
name|join
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|suppliers
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Discards {@code n} characters of data from the reader. This method    * will block until the full amount has been skipped. Does not close the    * reader.    *    * @param reader the reader to read from    * @param n the number of characters to skip    * @throws EOFException if this stream reaches the end before skipping all    *     the bytes    * @throws IOException if an I/O error occurs    */
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
comment|// force a blocking read
if|if
condition|(
name|reader
operator|.
name|read
argument_list|()
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|EOFException
argument_list|()
throw|;
block|}
name|n
operator|--
expr_stmt|;
block|}
else|else
block|{
name|n
operator|-=
name|amt
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Returns a Writer that sends all output to the given {@link Appendable}    * target. Closing the writer will close the target if it is {@link    * Closeable}, and flushing the writer will flush the target if it is {@link    * java.io.Flushable}.    *    * @param target the object to which output will be sent    * @return a new Writer object, unless target is a Writer, in which case the    *     target is returned    */
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

