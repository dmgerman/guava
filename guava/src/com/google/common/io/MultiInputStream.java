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
name|util
operator|.
name|Iterator
import|;
end_import

begin_comment
comment|/**  * An {@link InputStream} that concatenates multiple substreams. At most  * one stream will be open at a time.  *  * @author Chris Nokleberg  * @since 1.0  */
end_comment

begin_class
DECL|class|MultiInputStream
specifier|final
class|class
name|MultiInputStream
extends|extends
name|InputStream
block|{
DECL|field|it
specifier|private
name|Iterator
argument_list|<
name|?
extends|extends
name|InputSupplier
argument_list|<
name|?
extends|extends
name|InputStream
argument_list|>
argument_list|>
name|it
decl_stmt|;
DECL|field|in
specifier|private
name|InputStream
name|in
decl_stmt|;
comment|/**    * Creates a new instance.    *    * @param it an iterator of I/O suppliers that will provide each substream    */
DECL|method|MultiInputStream ( Iterator<? extends InputSupplier<? extends InputStream>> it)
specifier|public
name|MultiInputStream
parameter_list|(
name|Iterator
argument_list|<
name|?
extends|extends
name|InputSupplier
argument_list|<
name|?
extends|extends
name|InputStream
argument_list|>
argument_list|>
name|it
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|it
operator|=
name|it
expr_stmt|;
name|advance
argument_list|()
expr_stmt|;
block|}
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
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|in
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Closes the current input stream and opens the next one, if any.    */
DECL|method|advance ()
specifier|private
name|void
name|advance
parameter_list|()
throws|throws
name|IOException
block|{
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|in
operator|=
name|it
operator|.
name|next
argument_list|()
operator|.
name|getInput
argument_list|()
expr_stmt|;
block|}
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
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|in
operator|.
name|available
argument_list|()
return|;
block|}
DECL|method|markSupported ()
annotation|@
name|Override
specifier|public
name|boolean
name|markSupported
parameter_list|()
block|{
return|return
literal|false
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
if|if
condition|(
name|in
operator|==
literal|null
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
operator|==
operator|-
literal|1
condition|)
block|{
name|advance
argument_list|()
expr_stmt|;
return|return
name|read
argument_list|()
return|;
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
name|in
operator|==
literal|null
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
operator|==
operator|-
literal|1
condition|)
block|{
name|advance
argument_list|()
expr_stmt|;
return|return
name|read
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
return|;
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
if|if
condition|(
name|in
operator|==
literal|null
operator|||
name|n
operator|<=
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
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
if|if
condition|(
name|result
operator|!=
literal|0
condition|)
block|{
return|return
name|result
return|;
block|}
if|if
condition|(
name|read
argument_list|()
operator|==
operator|-
literal|1
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
literal|1
operator|+
name|in
operator|.
name|skip
argument_list|(
name|n
operator|-
literal|1
argument_list|)
return|;
block|}
block|}
end_class

end_unit

