begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.hash
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
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
comment|/**  * An {@link OutputStream} that maintains a hash of the data written to it.  *  * @author Nick Piepmeier  * @since 16.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|HashingOutputStream
specifier|public
specifier|final
class|class
name|HashingOutputStream
extends|extends
name|FilterOutputStream
block|{
DECL|field|hasher
specifier|private
specifier|final
name|Hasher
name|hasher
decl_stmt|;
comment|/**    * Creates an output stream that hashes using the given {@link HashFunction}, and forwards all    * data written to it to the underlying {@link OutputStream}.    *    *<p>The {@link OutputStream} should not be written to before or after the hand-off.    */
comment|// TODO(user): Evaluate whether it makes sense to always piggyback the computation of a
comment|// HashCode on an existing OutputStream, compared to creating a separate OutputStream that could
comment|// be (optionally) be combined with another if needed (with something like
comment|// MultiplexingOutputStream).
DECL|method|HashingOutputStream (HashFunction hashFunction, OutputStream out)
specifier|public
name|HashingOutputStream
parameter_list|(
name|HashFunction
name|hashFunction
parameter_list|,
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
name|this
operator|.
name|hasher
operator|=
name|checkNotNull
argument_list|(
name|hashFunction
operator|.
name|newHasher
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|write (int b)
annotation|@
name|Override
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
name|hasher
operator|.
name|putByte
argument_list|(
operator|(
name|byte
operator|)
name|b
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
DECL|method|write (byte[] bytes, int off, int len)
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|bytes
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
name|hasher
operator|.
name|putBytes
argument_list|(
name|bytes
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|bytes
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns the {@link HashCode} based on the data written to this stream. The result is    * unspecified if this method is called more than once on the same instance.    */
DECL|method|hash ()
specifier|public
name|HashCode
name|hash
parameter_list|()
block|{
return|return
name|hasher
operator|.
name|hash
argument_list|()
return|;
block|}
comment|// Overriding close() because FilterOutputStream's close() method pre-JDK8 has bad behavior:
comment|// it silently ignores any exception thrown by flush(). Instead, just close the delegate stream.
comment|// It should flush itself if necessary.
DECL|method|close ()
annotation|@
name|Override
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

