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
comment|/**  * A readable source of characters, such as a text file. Unlike a {@link Reader}, a  * {@code CharSource} is not an open, stateful stream of characters that can be read and closed.  * Instead, it is an immutable<i>supplier</i> of {@code InputStream} instances.  *  *<p>{@code CharSource} provides two kinds of methods:  *<ul>  *<li><b>Methods that return a reader:</b> These methods should return a<i>new</i>, independent  *   instance each time they are called. The caller is responsible for ensuring that the returned  *   reader is closed.  *<li><b>Convenience methods:</b> These are implementations of common operations that are  *   typically implemented by opening a reader using one of the methods in the first category,  *   doing something and finally closing the reader that was opened.  *</ul>  *  *<p>Several methods in this class, such as {@link #readLines()}, break the contents of the  * source into lines. Like {@link BufferedReader}, these methods break lines on any of {@code \n},  * {@code \r} or {@code \r\n}, do not include the line separator in each line and do not consider  * there to be an empty line at the end if the contents are terminated with a line separator.  *  *<p>Any {@link ByteSource} containing text encoded with a specific {@linkplain Charset character  * encoding} may be viewed as a {@code CharSource} using {@link ByteSource#asCharSource(Charset)}.  *  * @since 14.0  * @author Colin Decker  */
end_comment

begin_class
DECL|class|CharSource
specifier|public
specifier|abstract
class|class
name|CharSource
block|{
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
comment|/**    * Appends the contents of this source to the given {@link Appendable} (such as a {@link Writer}).    * Does not close {@code appendable} if it is {@code Closeable}.    *    * @throws IOException if an I/O error occurs in the process of reading from this source or    *     writing to {@code appendable}    */
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
name|add
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
argument_list|,
name|IOException
operator|.
name|class
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
comment|/**    * Copies the contents of this source to the given sink.    *    * @throws IOException if an I/O error occurs in the process of reading from this source or    *     writing to {@code sink}    */
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
name|add
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
name|add
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
argument_list|,
name|IOException
operator|.
name|class
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
name|add
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
argument_list|,
name|IOException
operator|.
name|class
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
comment|/**    * Reads the first link of this source as a string. Returns {@code null} if this source is empty.    *    *<p>Like {@link BufferedReader}, this method breaks lines on any of {@code \n}, {@code \r} or    * {@code \r\n}, does not include the line separator in the returned line and does not consider    * there to be an extra empty line at the end if the content is terminated with a line separator.    *    * @throws IOException if an I/O error occurs in the process of reading from this source    */
DECL|method|readFirstLine ()
specifier|public
annotation|@
name|Nullable
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
name|add
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
argument_list|,
name|IOException
operator|.
name|class
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
name|add
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
argument_list|,
name|IOException
operator|.
name|class
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

