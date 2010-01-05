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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
comment|/**  * Utility methods for working with {@link Closeable} objects.  *  * @author Michael Lancaster  * @since 2009.09.15<b>tentative</b>  */
end_comment

begin_class
DECL|class|Closeables
specifier|public
specifier|final
class|class
name|Closeables
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|Closeables
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|Closeables ()
specifier|private
name|Closeables
parameter_list|()
block|{}
comment|/**    * Closes a {@link Closeable}, with control over whether an    * {@code IOException} may be thrown. This is primarily useful in a    * finally block, where a thrown exception needs to be logged but not    * propagated (otherwise the original exception will be lost).    *    *<p>If {@code swallowIOException} is true then we never throw    * {@code IOException} but merely log it.    *    *<p>Example:    *    *<p><pre>public void useStreamNicely() throws IOException {    * SomeStream stream = new SomeStream("foo");    * boolean threw = true;    * try {    *   // Some code which does something with the Stream. May throw a    *   // Throwable.    *   threw = false; // No throwable thrown.    * } finally {    *   // Close the stream.    *   // If an exception occurs, only rethrow it if (threw==false).    *   Closeables.close(stream, threw);    * }    *</pre>    *    * @param closeable the {@code Closeable} object to be closed, or null,    *     in which case this method does nothing    * @param swallowIOException if true, don't propagate IO exceptions    *     thrown by the {@code close} methods    * @throws IOException if {@code swallowIOException} is false and    *     {@code close} throws an {@code IOException}.    */
DECL|method|close (@ullable Closeable closeable, boolean swallowIOException)
specifier|public
specifier|static
name|void
name|close
parameter_list|(
annotation|@
name|Nullable
name|Closeable
name|closeable
parameter_list|,
name|boolean
name|swallowIOException
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|closeable
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|closeable
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
if|if
condition|(
name|swallowIOException
condition|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"IOException thrown while closing Closeable."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
block|}
comment|/**    * Equivalent to calling {@code close(closeable, true)}, but with no    * IOException in the signature.    * @param closeable the {@code Closeable} object to be closed, or null, in    *      which case this method does nothing    */
DECL|method|closeQuietly (@ullable Closeable closeable)
specifier|public
specifier|static
name|void
name|closeQuietly
parameter_list|(
annotation|@
name|Nullable
name|Closeable
name|closeable
parameter_list|)
block|{
try|try
block|{
name|close
argument_list|(
name|closeable
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"IOException should not have been thrown."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

