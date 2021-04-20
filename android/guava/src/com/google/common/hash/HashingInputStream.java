begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|FilterInputStream
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

begin_comment
comment|/**  * An {@link InputStream} that maintains a hash of the data read from it.  *  * @author Qian Huang  * @since 16.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|HashingInputStream
specifier|public
specifier|final
class|class
name|HashingInputStream
extends|extends
name|FilterInputStream
block|{
DECL|field|hasher
specifier|private
specifier|final
name|Hasher
name|hasher
decl_stmt|;
comment|/**    * Creates an input stream that hashes using the given {@link HashFunction} and delegates all data    * read from it to the underlying {@link InputStream}.    *    *<p>The {@link InputStream} should not be read from before or after the hand-off.    */
DECL|method|HashingInputStream (HashFunction hashFunction, InputStream in)
specifier|public
name|HashingInputStream
parameter_list|(
name|HashFunction
name|hashFunction
parameter_list|,
name|InputStream
name|in
parameter_list|)
block|{
name|super
argument_list|(
name|checkNotNull
argument_list|(
name|in
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
comment|/**    * Reads the next byte of data from the underlying input stream and updates the hasher with the    * byte read.    */
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
DECL|method|read ()
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|b
init|=
name|in
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|b
operator|!=
operator|-
literal|1
condition|)
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
block|}
return|return
name|b
return|;
block|}
comment|/**    * Reads the specified bytes of data from the underlying input stream and updates the hasher with    * the bytes read.    */
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
DECL|method|read (byte[] bytes, int off, int len)
specifier|public
name|int
name|read
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
name|int
name|numOfBytesRead
init|=
name|in
operator|.
name|read
argument_list|(
name|bytes
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
decl_stmt|;
if|if
condition|(
name|numOfBytesRead
operator|!=
operator|-
literal|1
condition|)
block|{
name|hasher
operator|.
name|putBytes
argument_list|(
name|bytes
argument_list|,
name|off
argument_list|,
name|numOfBytesRead
argument_list|)
expr_stmt|;
block|}
return|return
name|numOfBytesRead
return|;
block|}
comment|/**    * mark() is not supported for HashingInputStream    *    * @return {@code false} always    */
annotation|@
name|Override
DECL|method|markSupported ()
specifier|public
name|boolean
name|markSupported
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/** mark() is not supported for HashingInputStream */
annotation|@
name|Override
DECL|method|mark (int readlimit)
specifier|public
name|void
name|mark
parameter_list|(
name|int
name|readlimit
parameter_list|)
block|{}
comment|/**    * reset() is not supported for HashingInputStream.    *    * @throws IOException this operation is not supported    */
annotation|@
name|Override
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"reset not supported"
argument_list|)
throw|;
block|}
comment|/**    * Returns the {@link HashCode} based on the data read from this stream. The result is unspecified    * if this method is called more than once on the same instance.    */
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
block|}
end_class

end_unit

