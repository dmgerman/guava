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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
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
comment|/**  * An InputStream that limits the number of bytes which can be read.  *  * @author Charles Fry  * @since Guava release 01  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|LimitInputStream
specifier|public
specifier|final
class|class
name|LimitInputStream
extends|extends
name|FilterInputStream
block|{
DECL|field|left
specifier|private
name|long
name|left
decl_stmt|;
DECL|field|mark
specifier|private
name|long
name|mark
init|=
operator|-
literal|1
decl_stmt|;
comment|/**    * Wraps another input stream, limiting the number of bytes which can be read.    *    * @param in the input stream to be wrapped    * @param limit the maximum number of bytes to be read    */
DECL|method|LimitInputStream (InputStream in, long limit)
specifier|public
name|LimitInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|long
name|limit
parameter_list|)
block|{
name|super
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkArgument
argument_list|(
name|limit
operator|>=
literal|0
argument_list|,
literal|"limit must be non-negative"
argument_list|)
expr_stmt|;
name|left
operator|=
name|limit
expr_stmt|;
block|}
DECL|method|available ()
annotation|@
name|Override
specifier|public
name|int
name|available
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|int
operator|)
name|Math
operator|.
name|min
argument_list|(
name|in
operator|.
name|available
argument_list|()
argument_list|,
name|left
argument_list|)
return|;
block|}
DECL|method|mark (int readlimit)
annotation|@
name|Override
specifier|public
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
name|left
expr_stmt|;
comment|// it's okay to mark even if mark isn't supported, as reset won't work
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
if|if
condition|(
name|left
operator|==
literal|0
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
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
operator|--
name|left
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
if|if
condition|(
name|left
operator|==
literal|0
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|len
operator|=
operator|(
name|int
operator|)
name|Math
operator|.
name|min
argument_list|(
name|len
argument_list|,
name|left
argument_list|)
expr_stmt|;
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
name|left
operator|-=
name|result
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|reset ()
annotation|@
name|Override
specifier|public
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
name|left
operator|=
name|mark
expr_stmt|;
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
name|n
operator|=
name|Math
operator|.
name|min
argument_list|(
name|n
argument_list|,
name|left
argument_list|)
expr_stmt|;
name|long
name|skipped
init|=
name|in
operator|.
name|skip
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|left
operator|-=
name|skipped
expr_stmt|;
return|return
name|skipped
return|;
block|}
block|}
end_class

end_unit

