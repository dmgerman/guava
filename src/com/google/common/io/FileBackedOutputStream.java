begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|annotations
operator|.
name|VisibleForTesting
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
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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

begin_comment
comment|/**  * An {@link OutputStream} that starts buffering to a byte array, but  * switches to file buffering once the data reaches a configurable size.  *  *<p>This class is thread-safe.  *  * @author Chris Nokleberg  * @since Guava release 01  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|FileBackedOutputStream
specifier|public
specifier|final
class|class
name|FileBackedOutputStream
extends|extends
name|OutputStream
block|{
DECL|field|fileThreshold
specifier|private
specifier|final
name|int
name|fileThreshold
decl_stmt|;
DECL|field|resetOnFinalize
specifier|private
specifier|final
name|boolean
name|resetOnFinalize
decl_stmt|;
DECL|field|supplier
specifier|private
specifier|final
name|InputSupplier
argument_list|<
name|InputStream
argument_list|>
name|supplier
decl_stmt|;
DECL|field|out
specifier|private
name|OutputStream
name|out
decl_stmt|;
DECL|field|memory
specifier|private
name|MemoryOutput
name|memory
decl_stmt|;
DECL|field|file
specifier|private
name|File
name|file
decl_stmt|;
comment|/** ByteArrayOutputStream that exposes its internals. */
DECL|class|MemoryOutput
specifier|private
specifier|static
class|class
name|MemoryOutput
extends|extends
name|ByteArrayOutputStream
block|{
DECL|method|getBuffer ()
name|byte
index|[]
name|getBuffer
parameter_list|()
block|{
return|return
name|buf
return|;
block|}
DECL|method|getCount ()
name|int
name|getCount
parameter_list|()
block|{
return|return
name|count
return|;
block|}
block|}
comment|/** Returns the file holding the data (possibly null). */
DECL|method|getFile ()
annotation|@
name|VisibleForTesting
specifier|synchronized
name|File
name|getFile
parameter_list|()
block|{
return|return
name|file
return|;
block|}
comment|/**    * Creates a new instance that uses the given file threshold.    * Equivalent to {@code ThresholdOutputStream(fileThreshold, false)}.    *    * @param fileThreshold the number of bytes before the stream should    *     switch to buffering to a file    */
DECL|method|FileBackedOutputStream (int fileThreshold)
specifier|public
name|FileBackedOutputStream
parameter_list|(
name|int
name|fileThreshold
parameter_list|)
block|{
name|this
argument_list|(
name|fileThreshold
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates a new instance that uses the given file threshold, and    * optionally resets the data when the {@link InputSupplier} returned    * by {@link #getSupplier} is finalized.    *    * @param fileThreshold the number of bytes before the stream should    *     switch to buffering to a file    * @param resetOnFinalize if true, the {@link #reset} method will    *     be called when the {@link InputSupplier} returned by {@link    *     #getSupplier} is finalized    */
DECL|method|FileBackedOutputStream (int fileThreshold, boolean resetOnFinalize)
specifier|public
name|FileBackedOutputStream
parameter_list|(
name|int
name|fileThreshold
parameter_list|,
name|boolean
name|resetOnFinalize
parameter_list|)
block|{
name|this
operator|.
name|fileThreshold
operator|=
name|fileThreshold
expr_stmt|;
name|this
operator|.
name|resetOnFinalize
operator|=
name|resetOnFinalize
expr_stmt|;
name|memory
operator|=
operator|new
name|MemoryOutput
argument_list|()
expr_stmt|;
name|out
operator|=
name|memory
expr_stmt|;
if|if
condition|(
name|resetOnFinalize
condition|)
block|{
name|supplier
operator|=
operator|new
name|InputSupplier
argument_list|<
name|InputStream
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|InputStream
name|getInput
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
specifier|protected
name|void
name|finalize
parameter_list|()
block|{
try|try
block|{
name|reset
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
block|}
block|}
block|}
expr_stmt|;
block|}
else|else
block|{
name|supplier
operator|=
operator|new
name|InputSupplier
argument_list|<
name|InputStream
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|InputStream
name|getInput
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|openStream
argument_list|()
return|;
block|}
block|}
expr_stmt|;
block|}
block|}
comment|/**    * Returns a supplier that may be used to retrieve the data buffered    * by this stream.    */
DECL|method|getSupplier ()
specifier|public
name|InputSupplier
argument_list|<
name|InputStream
argument_list|>
name|getSupplier
parameter_list|()
block|{
return|return
name|supplier
return|;
block|}
DECL|method|openStream ()
specifier|private
specifier|synchronized
name|InputStream
name|openStream
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|memory
operator|.
name|getBuffer
argument_list|()
argument_list|,
literal|0
argument_list|,
name|memory
operator|.
name|getCount
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**    * Calls {@link #close} if not already closed, and then resets this    * object back to its initial state, for reuse. If data was buffered    * to a file, it will be deleted.    *    * @throws IOException if an I/O error occurred while deleting the file buffer    */
DECL|method|reset ()
specifier|public
specifier|synchronized
name|void
name|reset
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|memory
operator|==
literal|null
condition|)
block|{
name|memory
operator|=
operator|new
name|MemoryOutput
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|memory
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
name|out
operator|=
name|memory
expr_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|File
name|deleteMe
init|=
name|file
decl_stmt|;
name|file
operator|=
literal|null
expr_stmt|;
if|if
condition|(
operator|!
name|deleteMe
operator|.
name|delete
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not delete: "
operator|+
name|deleteMe
argument_list|)
throw|;
block|}
block|}
block|}
block|}
DECL|method|write (int b)
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|update
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
DECL|method|write (byte[] b)
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|write
argument_list|(
name|b
argument_list|,
literal|0
argument_list|,
name|b
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
DECL|method|write (byte[] b, int off, int len)
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|update
argument_list|(
name|len
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
DECL|method|close ()
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|flush ()
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|flush
parameter_list|()
throws|throws
name|IOException
block|{
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
comment|/**    * Checks if writing {@code len} bytes would go over threshold, and    * switches to file buffering if so.    */
DECL|method|update (int len)
specifier|private
name|void
name|update
parameter_list|(
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|file
operator|==
literal|null
operator|&&
operator|(
name|memory
operator|.
name|getCount
argument_list|()
operator|+
name|len
operator|>
name|fileThreshold
operator|)
condition|)
block|{
name|File
name|temp
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"FileBackedOutputStream"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|resetOnFinalize
condition|)
block|{
comment|// Finalizers are not guaranteed to be called on system shutdown;
comment|// this is insurance.
name|temp
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
block|}
name|FileOutputStream
name|transfer
init|=
operator|new
name|FileOutputStream
argument_list|(
name|temp
argument_list|)
decl_stmt|;
name|transfer
operator|.
name|write
argument_list|(
name|memory
operator|.
name|getBuffer
argument_list|()
argument_list|,
literal|0
argument_list|,
name|memory
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|transfer
operator|.
name|flush
argument_list|()
expr_stmt|;
comment|// We've successfully transferred the data; switch to writing to file
name|out
operator|=
name|transfer
expr_stmt|;
name|file
operator|=
name|temp
expr_stmt|;
name|memory
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

