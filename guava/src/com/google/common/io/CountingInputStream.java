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
comment|/**  * An {@link InputStream} that counts the number of bytes read.  *  * @author Chris Nokleberg  * @since 1.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|CountingInputStream
specifier|public
specifier|final
class|class
name|CountingInputStream
extends|extends
name|FilterInputStream
block|{
DECL|field|count
specifier|private
name|long
name|count
decl_stmt|;
DECL|field|mark
specifier|private
name|long
name|mark
init|=
operator|-
literal|1
decl_stmt|;
comment|/**    * Wraps another input stream, counting the number of bytes read.    *    * @param in the input stream to be wrapped    */
DECL|method|CountingInputStream (InputStream in)
specifier|public
name|CountingInputStream
parameter_list|(
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
block|}
comment|/** Returns the number of bytes read. */
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
DECL|method|read ()
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|result
init|=
name|in
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|!=
operator|-
literal|1
condition|)
block|{
name|count
operator|++
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|read (byte[] b, int off, int len)
annotation|@
name|Override
specifier|public
name|int
name|read
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
name|int
name|result
init|=
name|in
operator|.
name|read
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|!=
operator|-
literal|1
condition|)
block|{
name|count
operator|+=
name|result
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|skip (long n)
annotation|@
name|Override
specifier|public
name|long
name|skip
parameter_list|(
name|long
name|n
parameter_list|)
throws|throws
name|IOException
block|{
name|long
name|result
init|=
name|in
operator|.
name|skip
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|count
operator|+=
name|result
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|mark (int readlimit)
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|mark
parameter_list|(
name|int
name|readlimit
parameter_list|)
block|{
name|in
operator|.
name|mark
argument_list|(
name|readlimit
argument_list|)
expr_stmt|;
name|mark
operator|=
name|count
expr_stmt|;
comment|// it's okay to mark even if mark isn't supported, as reset won't work
block|}
DECL|method|reset ()
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|reset
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|in
operator|.
name|markSupported
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Mark not supported"
argument_list|)
throw|;
block|}
if|if
condition|(
name|mark
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Mark not set"
argument_list|)
throw|;
block|}
name|in
operator|.
name|reset
argument_list|()
expr_stmt|;
name|count
operator|=
name|mark
expr_stmt|;
block|}
block|}
end_class

end_unit

