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
name|java
operator|.
name|io
operator|.
name|Flushable
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

begin_comment
comment|/**  * Utility methods for working with {@link Flushable} objects.  *  * @author Michael Lancaster  * @since 1  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|Flushables
specifier|public
specifier|final
class|class
name|Flushables
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
name|Flushables
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|Flushables ()
specifier|private
name|Flushables
parameter_list|()
block|{}
comment|/**    * Flush a {@link Flushable}, with control over whether an    * {@code IOException} may be thrown.    *    *<p>If {@code swallowIOException} is true, then we don't rethrow    * {@code IOException}, but merely log it.    *    * @param flushable the {@code Flushable} object to be flushed.    * @param swallowIOException if true, don't propagate IO exceptions    *     thrown by the {@code flush} method    * @throws IOException if {@code swallowIOException} is false and    *     {@link Flushable#flush} throws an {@code IOException}.    * @see Closeables#close    */
DECL|method|flush (Flushable flushable, boolean swallowIOException)
specifier|public
specifier|static
name|void
name|flush
parameter_list|(
name|Flushable
name|flushable
parameter_list|,
name|boolean
name|swallowIOException
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|flushable
operator|.
name|flush
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
literal|"IOException thrown while flushing Flushable."
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
comment|/**    * Equivalent to calling {@code flush(flushable, true)}, but with no    * {@code IOException} in the signature.    *    * @param flushable the {@code Flushable} object to be flushed.    */
DECL|method|flushQuietly (Flushable flushable)
specifier|public
specifier|static
name|void
name|flushQuietly
parameter_list|(
name|Flushable
name|flushable
parameter_list|)
block|{
try|try
block|{
name|flush
argument_list|(
name|flushable
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

