begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|net
operator|.
name|URL
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

begin_comment
comment|/**  * Provides utility methods for working with resources in the classpath.  * Note that even though these methods use {@link URL} parameters, they  * are usually not appropriate for HTTP or other non-classpath resources.  *  *<p>All method parameters must be non-null unless documented otherwise.  *  * @author Chris Nokleberg  * @author Ben Yu  * @since 1  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|Resources
specifier|public
specifier|final
class|class
name|Resources
block|{
DECL|method|Resources ()
specifier|private
name|Resources
parameter_list|()
block|{}
comment|/**    * Returns a factory that will supply instances of {@link InputStream} that    * read from the given URL.    *    * @param url the URL to read from    * @return the factory    */
DECL|method|newInputStreamSupplier ( final URL url)
specifier|public
specifier|static
name|InputSupplier
argument_list|<
name|InputStream
argument_list|>
name|newInputStreamSupplier
parameter_list|(
specifier|final
name|URL
name|url
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|url
argument_list|)
expr_stmt|;
return|return
operator|new
name|InputSupplier
argument_list|<
name|InputStream
argument_list|>
argument_list|()
block|{
specifier|public
name|InputStream
name|getInput
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|url
operator|.
name|openStream
argument_list|()
return|;
block|}
block|}
return|;
block|}
comment|/**    * Returns a factory that will supply instances of    * {@link InputStreamReader} that read a URL using the given character set.    *    * @param url the URL to read from    * @param charset the character set used when reading the URL contents    * @return the factory    */
DECL|method|newReaderSupplier ( URL url, Charset charset)
specifier|public
specifier|static
name|InputSupplier
argument_list|<
name|InputStreamReader
argument_list|>
name|newReaderSupplier
parameter_list|(
name|URL
name|url
parameter_list|,
name|Charset
name|charset
parameter_list|)
block|{
return|return
name|CharStreams
operator|.
name|newReaderSupplier
argument_list|(
name|newInputStreamSupplier
argument_list|(
name|url
argument_list|)
argument_list|,
name|charset
argument_list|)
return|;
block|}
comment|/**    * Reads all bytes from a URL into a byte array.    *    * @param url the URL to read from    * @return a byte array containing all the bytes from the URL    * @throws IOException if an I/O error occurs    */
DECL|method|toByteArray (URL url)
specifier|public
specifier|static
name|byte
index|[]
name|toByteArray
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|ByteStreams
operator|.
name|toByteArray
argument_list|(
name|newInputStreamSupplier
argument_list|(
name|url
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Reads all characters from a URL into a {@link String}, using the given    * character set.    *    * @param url the URL to read from    * @param charset the character set used when reading the URL    * @return a string containing all the characters from the URL    * @throws IOException if an I/O error occurs.    */
DECL|method|toString (URL url, Charset charset)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|URL
name|url
parameter_list|,
name|Charset
name|charset
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|CharStreams
operator|.
name|toString
argument_list|(
name|newReaderSupplier
argument_list|(
name|url
argument_list|,
name|charset
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Streams lines from a URL, stopping when our callback returns false, or we    * have read all of the lines.    *    * @param url the URL to read from    * @param charset the character set used when reading the URL    * @param callback the LineProcessor to use to handle the lines    * @return the output of processing the lines    * @throws IOException if an I/O error occurs    */
DECL|method|readLines (URL url, Charset charset, LineProcessor<T> callback)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|readLines
parameter_list|(
name|URL
name|url
parameter_list|,
name|Charset
name|charset
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
return|return
name|CharStreams
operator|.
name|readLines
argument_list|(
name|newReaderSupplier
argument_list|(
name|url
argument_list|,
name|charset
argument_list|)
argument_list|,
name|callback
argument_list|)
return|;
block|}
comment|/**    * Reads all of the lines from a URL. The lines do not include    * line-termination characters, but do include other leading and trailing    * whitespace.    *    * @param url the URL to read from    * @param charset the character set used when writing the file    * @return a mutable {@link List} containing all the lines    * @throws IOException if an I/O error occurs    */
DECL|method|readLines (URL url, Charset charset)
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|readLines
parameter_list|(
name|URL
name|url
parameter_list|,
name|Charset
name|charset
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|CharStreams
operator|.
name|readLines
argument_list|(
name|newReaderSupplier
argument_list|(
name|url
argument_list|,
name|charset
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Copies all bytes from a URL to an output stream.    *    * @param from the URL to read from    * @param to the output stream    * @throws IOException if an I/O error occurs    */
DECL|method|copy (URL from, OutputStream to)
specifier|public
specifier|static
name|void
name|copy
parameter_list|(
name|URL
name|from
parameter_list|,
name|OutputStream
name|to
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteStreams
operator|.
name|copy
argument_list|(
name|newInputStreamSupplier
argument_list|(
name|from
argument_list|)
argument_list|,
name|to
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns a {@code URL} pointing to {@code resourceName} if the resource is    * found in the class path. {@code Resources.class.getClassLoader()} is used    * to locate the resource.    *     * @throws IllegalArgumentException if resource is not found    */
DECL|method|getResource (String resourceName)
specifier|public
specifier|static
name|URL
name|getResource
parameter_list|(
name|String
name|resourceName
parameter_list|)
block|{
name|URL
name|url
init|=
name|Resources
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|resourceName
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|url
operator|!=
literal|null
argument_list|,
literal|"resource %s not found."
argument_list|,
name|resourceName
argument_list|)
expr_stmt|;
return|return
name|url
return|;
block|}
comment|/**    * Returns a {@code URL} pointing to {@code resourceName} that is relative to    * {@code contextClass}, if the resource is found in the class path.     *     * @throws IllegalArgumentException if resource is not found    */
DECL|method|getResource (Class<?> contextClass, String resourceName)
specifier|public
specifier|static
name|URL
name|getResource
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|contextClass
parameter_list|,
name|String
name|resourceName
parameter_list|)
block|{
name|URL
name|url
init|=
name|contextClass
operator|.
name|getResource
argument_list|(
name|resourceName
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|url
operator|!=
literal|null
argument_list|,
literal|"resource %s relative to %s not found."
argument_list|,
name|resourceName
argument_list|,
name|contextClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|url
return|;
block|}
block|}
end_class

end_unit

