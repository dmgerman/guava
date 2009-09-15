begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2006 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Flushable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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

begin_comment
comment|/**  * Writer that places all output on an {@link Appendable} target. If the target  * is {@link Flushable} or {@link Closeable}, flush()es and close()s will also  * be delegated to the target.  *  * @author Alan Green  * @author Sebastian Kanthak  * @since 9.09.15<b>tentative</b>  */
end_comment

begin_class
DECL|class|AppendableWriter
class|class
name|AppendableWriter
extends|extends
name|Writer
block|{
DECL|field|target
specifier|private
specifier|final
name|Appendable
name|target
decl_stmt|;
DECL|field|closed
specifier|private
name|boolean
name|closed
decl_stmt|;
comment|/**    * Creates a new writer that appends everything it writes to {@code target}.    *    * @param target target to which to append output    */
DECL|method|AppendableWriter (Appendable target)
name|AppendableWriter
parameter_list|(
name|Appendable
name|target
parameter_list|)
block|{
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
block|}
comment|/*    * Abstract methods from Writer    */
DECL|method|write (char cbuf[], int off, int len)
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|char
name|cbuf
index|[]
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
name|checkNotClosed
argument_list|()
expr_stmt|;
comment|// It turns out that creating a new String is usually as fast, or faster
comment|// than wrapping cbuf in a light-weight CharSequence.
name|target
operator|.
name|append
argument_list|(
operator|new
name|String
argument_list|(
name|cbuf
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|flush ()
annotation|@
name|Override
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|IOException
block|{
name|checkNotClosed
argument_list|()
expr_stmt|;
if|if
condition|(
name|target
operator|instanceof
name|Flushable
condition|)
block|{
operator|(
operator|(
name|Flushable
operator|)
name|target
operator|)
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
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
name|this
operator|.
name|closed
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|target
operator|instanceof
name|Closeable
condition|)
block|{
operator|(
operator|(
name|Closeable
operator|)
name|target
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/*    * Override a few functions for performance reasons to avoid creating    * unnecessary strings.    */
DECL|method|write (int c)
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|int
name|c
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotClosed
argument_list|()
expr_stmt|;
name|target
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
DECL|method|write (String str)
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|String
name|str
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotClosed
argument_list|()
expr_stmt|;
name|target
operator|.
name|append
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
DECL|method|write (String str, int off, int len)
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|String
name|str
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
name|checkNotClosed
argument_list|()
expr_stmt|;
comment|// tricky: append takes start, end pair...
name|target
operator|.
name|append
argument_list|(
name|str
argument_list|,
name|off
argument_list|,
name|off
operator|+
name|len
argument_list|)
expr_stmt|;
block|}
DECL|method|append (char c)
annotation|@
name|Override
specifier|public
name|Writer
name|append
parameter_list|(
name|char
name|c
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotClosed
argument_list|()
expr_stmt|;
name|target
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|append (CharSequence charSeq)
annotation|@
name|Override
specifier|public
name|Writer
name|append
parameter_list|(
name|CharSequence
name|charSeq
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotClosed
argument_list|()
expr_stmt|;
name|target
operator|.
name|append
argument_list|(
name|charSeq
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|append (CharSequence charSeq, int start, int end)
annotation|@
name|Override
specifier|public
name|Writer
name|append
parameter_list|(
name|CharSequence
name|charSeq
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotClosed
argument_list|()
expr_stmt|;
name|target
operator|.
name|append
argument_list|(
name|charSeq
argument_list|,
name|start
argument_list|,
name|end
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|checkNotClosed ()
specifier|private
name|void
name|checkNotClosed
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|closed
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot write to a closed writer."
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

