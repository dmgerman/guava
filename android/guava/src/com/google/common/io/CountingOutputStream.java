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
name|java
operator|.
name|io
operator|.
name|FilterOutputStream
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
name|OutputStream
import|;
end_import

begin_comment
comment|/**  * An OutputStream that counts the number of bytes written.  *  * @author Chris Nokleberg  * @since 1.0  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|CountingOutputStream
specifier|public
specifier|final
class|class
name|CountingOutputStream
extends|extends
name|FilterOutputStream
block|{
DECL|field|count
specifier|private
name|long
name|count
decl_stmt|;
comment|/**    * Wraps another output stream, counting the number of bytes written.    *    * @param out the output stream to be wrapped    */
DECL|method|CountingOutputStream (OutputStream out)
specifier|public
name|CountingOutputStream
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
name|super
argument_list|(
name|checkNotNull
argument_list|(
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** Returns the number of bytes written. */
DECL|method|getCount ()
specifier|public
name|long
name|getCount
parameter_list|()
block|{
return|return
name|count
return|;
block|}
annotation|@
name|Override
DECL|method|write (byte[] b, int off, int len)
specifier|public
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
name|count
operator|+=
name|len
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|write (int b)
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|out
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
comment|// Overriding close() because FilterOutputStream's close() method pre-JDK8 has bad behavior:
comment|// it silently ignores any exception thrown by flush(). Instead, just close the delegate stream.
comment|// It should flush itself if necessary.
annotation|@
name|Override
DECL|method|close ()
specifier|public
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
block|}
end_class

end_unit

