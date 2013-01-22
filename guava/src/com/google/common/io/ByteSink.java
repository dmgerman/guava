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
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
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

begin_comment
comment|/**  * A destination to which bytes can be written, such as a file. Unlike an {@link OutputStream}, a  * {@code ByteSink} is not an open, stateful stream that can be written to and closed. Instead, it  * is an immutable<i>supplier</i> of {@code OutputStream} instances.  *  *<p>{@code ByteSink} provides two kinds of methods:  *<ul>  *<li><b>Methods that return a stream:</b> These methods should return a<i>new</i>, independent  *   instance each time they are called. The caller is responsible for ensuring that the returned  *   stream is closed.  *<li><b>Convenience methods:</b> These are implementations of common operations that are  *   typically implemented by opening a stream using one of the methods in the first category, doing  *   something and finally closing the stream or channel that was opened.  *</ul>  *  * @since 14.0  * @author Colin Decker  */
end_comment

begin_class
DECL|class|ByteSink
specifier|public
specifier|abstract
class|class
name|ByteSink
implements|implements
name|OutputSupplier
argument_list|<
name|OutputStream
argument_list|>
block|{
comment|/**    * Returns a {@link CharSink} view of this {@code ByteSink} that writes characters to this sink    * as bytes encoded with the given {@link Charset charset}.    */
DECL|method|asCharSink (Charset charset)
specifier|public
name|CharSink
name|asCharSink
parameter_list|(
name|Charset
name|charset
parameter_list|)
block|{
return|return
operator|new
name|AsCharSink
argument_list|(
name|charset
argument_list|)
return|;
block|}
comment|/**    * Opens a new {@link OutputStream} for writing to this sink. This method should return a new,    * independent stream each time it is called.    *    *<p>The caller is responsible for ensuring that the returned stream is closed.    *    * @throws IOException if an I/O error occurs in the process of opening the stream    */
DECL|method|openStream ()
specifier|public
specifier|abstract
name|OutputStream
name|openStream
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**    * This method is a temporary method provided for easing migration from suppliers to sources and    * sinks.    *    * @since 15.0    * @deprecated This method is only provided for temporary compatibility with the    *     {@link OutputSupplier} interface and should not be called directly. Use {@link #openStream}    *     instead.    */
annotation|@
name|Override
annotation|@
name|Deprecated
DECL|method|getOutput ()
specifier|public
specifier|final
name|OutputStream
name|getOutput
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|openStream
argument_list|()
return|;
block|}
comment|/**    * Opens a new {@link BufferedOutputStream} for writing to this sink. This method should return a    * new, independent stream each time it is called.    *    *<p>The caller is responsible for ensuring that the returned stream is closed.    *    * @throws IOException if an I/O error occurs in the process of opening the stream    */
DECL|method|openBufferedStream ()
specifier|public
name|BufferedOutputStream
name|openBufferedStream
parameter_list|()
throws|throws
name|IOException
block|{
name|OutputStream
name|out
init|=
name|openStream
argument_list|()
decl_stmt|;
return|return
operator|(
name|out
operator|instanceof
name|BufferedOutputStream
operator|)
condition|?
operator|(
name|BufferedOutputStream
operator|)
name|out
else|:
operator|new
name|BufferedOutputStream
argument_list|(
name|out
argument_list|)
return|;
block|}
comment|/**    * Writes all the given bytes to this sink.    *    * @throws IOException if an I/O occurs in the process of writing to this sink    */
DECL|method|write (byte[] bytes)
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotNull
argument_list|(
name|bytes
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
name|OutputStream
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
name|write
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
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
comment|/**    * Writes all the bytes from the given {@code InputStream} to this sink. Does not close    * {@code input}.    *    * @throws IOException if an I/O occurs in the process of reading from {@code input} or writing to    *     this sink    */
DECL|method|writeFrom (InputStream input)
specifier|public
name|long
name|writeFrom
parameter_list|(
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotNull
argument_list|(
name|input
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
name|OutputStream
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
return|return
name|ByteStreams
operator|.
name|copy
argument_list|(
name|input
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
comment|/**    * A char sink that encodes written characters with a charset and writes resulting bytes to this    * byte sink.    */
DECL|class|AsCharSink
specifier|private
specifier|final
class|class
name|AsCharSink
extends|extends
name|CharSink
block|{
DECL|field|charset
specifier|private
specifier|final
name|Charset
name|charset
decl_stmt|;
DECL|method|AsCharSink (Charset charset)
specifier|private
name|AsCharSink
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
name|Writer
name|openStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|OutputStreamWriter
argument_list|(
name|ByteSink
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
name|ByteSink
operator|.
name|this
operator|.
name|toString
argument_list|()
operator|+
literal|".asCharSink("
operator|+
name|charset
operator|+
literal|")"
return|;
block|}
block|}
block|}
end_class

end_unit

