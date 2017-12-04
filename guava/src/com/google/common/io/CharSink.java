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
name|BufferedWriter
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
name|stream
operator|.
name|Stream
import|;
end_import

begin_comment
comment|/**  * A destination to which characters can be written, such as a text file. Unlike a {@link Writer}, a  * {@code CharSink} is not an open, stateful stream that can be written to and closed. Instead, it  * is an immutable<i>supplier</i> of {@code Writer} instances.  *  *<p>{@code CharSink} provides two kinds of methods:  *  *<ul>  *<li><b>Methods that return a writer:</b> These methods should return a<i>new</i>, independent  *       instance each time they are called. The caller is responsible for ensuring that the  *       returned writer is closed.  *<li><b>Convenience methods:</b> These are implementations of common operations that are  *       typically implemented by opening a writer using one of the methods in the first category,  *       doing something and finally closing the writer that was opened.  *</ul>  *  *<p>Any {@link ByteSink} may be viewed as a {@code CharSink} with a specific {@linkplain Charset  * character encoding} using {@link ByteSink#asCharSink(Charset)}. Characters written to the  * resulting {@code CharSink} will written to the {@code ByteSink} as encoded bytes.  *  * @since 14.0  * @author Colin Decker  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|CharSink
specifier|public
specifier|abstract
class|class
name|CharSink
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|CharSink ()
specifier|protected
name|CharSink
parameter_list|()
block|{}
comment|/**    * Opens a new {@link Writer} for writing to this sink. This method returns a new, independent    * writer each time it is called.    *    *<p>The caller is responsible for ensuring that the returned writer is closed.    *    * @throws IOException if an I/O error occurs while opening the writer    */
DECL|method|openStream ()
specifier|public
specifier|abstract
name|Writer
name|openStream
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**    * Opens a new buffered {@link Writer} for writing to this sink. The returned stream is not    * required to be a {@link BufferedWriter} in order to allow implementations to simply delegate to    * {@link #openStream()} when the stream returned by that method does not benefit from additional    * buffering. This method returns a new, independent writer each time it is called.    *    *<p>The caller is responsible for ensuring that the returned writer is closed.    *    * @throws IOException if an I/O error occurs while opening the writer    * @since 15.0 (in 14.0 with return type {@link BufferedWriter})    */
DECL|method|openBufferedStream ()
specifier|public
name|Writer
name|openBufferedStream
parameter_list|()
throws|throws
name|IOException
block|{
name|Writer
name|writer
init|=
name|openStream
argument_list|()
decl_stmt|;
return|return
operator|(
name|writer
operator|instanceof
name|BufferedWriter
operator|)
condition|?
operator|(
name|BufferedWriter
operator|)
name|writer
else|:
operator|new
name|BufferedWriter
argument_list|(
name|writer
argument_list|)
return|;
block|}
comment|/**    * Writes the given character sequence to this sink.    *    * @throws IOException if an I/O error while writing to this sink    */
DECL|method|write (CharSequence charSequence)
specifier|public
name|void
name|write
parameter_list|(
name|CharSequence
name|charSequence
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotNull
argument_list|(
name|charSequence
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
name|Writer
name|out
init|=
name|closer
operator|.
name|register
argument_list|(
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|out
operator|.
name|append
argument_list|(
name|charSequence
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
comment|// https://code.google.com/p/guava-libraries/issues/detail?id=1330
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
comment|/**    * Writes the given lines of text to this sink with each line (including the last) terminated with    * the operating system's default line separator. This method is equivalent to {@code    * writeLines(lines, System.getProperty("line.separator"))}.    *    * @throws IOException if an I/O error occurs while writing to this sink    */
DECL|method|writeLines (Iterable<? extends CharSequence> lines)
specifier|public
name|void
name|writeLines
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|CharSequence
argument_list|>
name|lines
parameter_list|)
throws|throws
name|IOException
block|{
name|writeLines
argument_list|(
name|lines
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Writes the given lines of text to this sink with each line (including the last) terminated with    * the given line separator.    *    * @throws IOException if an I/O error occurs while writing to this sink    */
DECL|method|writeLines (Iterable<? extends CharSequence> lines, String lineSeparator)
specifier|public
name|void
name|writeLines
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|CharSequence
argument_list|>
name|lines
parameter_list|,
name|String
name|lineSeparator
parameter_list|)
throws|throws
name|IOException
block|{
name|writeLines
argument_list|(
name|lines
operator|.
name|iterator
argument_list|()
argument_list|,
name|lineSeparator
argument_list|)
expr_stmt|;
block|}
comment|/**    * Writes the given lines of text to this sink with each line (including the last) terminated with    * the operating system's default line separator. This method is equivalent to {@code    * writeLines(lines, System.getProperty("line.separator"))}.    *    * @throws IOException if an I/O error occurs while writing to this sink    * @since 22.0    */
annotation|@
name|Beta
DECL|method|writeLines (Stream<? extends CharSequence> lines)
specifier|public
name|void
name|writeLines
parameter_list|(
name|Stream
argument_list|<
name|?
extends|extends
name|CharSequence
argument_list|>
name|lines
parameter_list|)
throws|throws
name|IOException
block|{
name|writeLines
argument_list|(
name|lines
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Writes the given lines of text to this sink with each line (including the last) terminated with    * the given line separator.    *    * @throws IOException if an I/O error occurs while writing to this sink    * @since 22.0    */
annotation|@
name|Beta
DECL|method|writeLines (Stream<? extends CharSequence> lines, String lineSeparator)
specifier|public
name|void
name|writeLines
parameter_list|(
name|Stream
argument_list|<
name|?
extends|extends
name|CharSequence
argument_list|>
name|lines
parameter_list|,
name|String
name|lineSeparator
parameter_list|)
throws|throws
name|IOException
block|{
name|writeLines
argument_list|(
name|lines
operator|.
name|iterator
argument_list|()
argument_list|,
name|lineSeparator
argument_list|)
expr_stmt|;
block|}
DECL|method|writeLines (Iterator<? extends CharSequence> lines, String lineSeparator)
specifier|private
name|void
name|writeLines
parameter_list|(
name|Iterator
argument_list|<
name|?
extends|extends
name|CharSequence
argument_list|>
name|lines
parameter_list|,
name|String
name|lineSeparator
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotNull
argument_list|(
name|lineSeparator
argument_list|)
expr_stmt|;
try|try
init|(
name|Writer
name|out
init|=
name|openBufferedStream
argument_list|()
init|)
block|{
while|while
condition|(
name|lines
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
name|lines
operator|.
name|next
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|lineSeparator
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Writes all the text from the given {@link Readable} (such as a {@link Reader}) to this sink.    * Does not close {@code readable} if it is {@code Closeable}.    *    * @return the number of characters written    * @throws IOException if an I/O error occurs while reading from {@code readable} or writing to    *     this sink    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|writeFrom (Readable readable)
specifier|public
name|long
name|writeFrom
parameter_list|(
name|Readable
name|readable
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotNull
argument_list|(
name|readable
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
name|Writer
name|out
init|=
name|closer
operator|.
name|register
argument_list|(
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|long
name|written
init|=
name|CharStreams
operator|.
name|copy
argument_list|(
name|readable
argument_list|,
name|out
argument_list|)
decl_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
comment|// https://code.google.com/p/guava-libraries/issues/detail?id=1330
return|return
name|written
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
block|}
end_class

end_unit

