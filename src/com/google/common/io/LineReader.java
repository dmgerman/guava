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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|CharBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
import|;
end_import

begin_comment
comment|/**  * A class for reading lines of text. Provides the same functionality  * as {@link java.io.BufferedReader#readLine()} but for all {@link Readable}  * objects, not just instances of {@link Reader}.  *  * @author Chris Nokleberg  * @since 1  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|LineReader
specifier|public
specifier|final
class|class
name|LineReader
block|{
DECL|field|readable
specifier|private
specifier|final
name|Readable
name|readable
decl_stmt|;
DECL|field|reader
specifier|private
specifier|final
name|Reader
name|reader
decl_stmt|;
DECL|field|buf
specifier|private
specifier|final
name|char
index|[]
name|buf
init|=
operator|new
name|char
index|[
literal|0x1000
index|]
decl_stmt|;
comment|// 4K
DECL|field|cbuf
specifier|private
specifier|final
name|CharBuffer
name|cbuf
init|=
name|CharBuffer
operator|.
name|wrap
argument_list|(
name|buf
argument_list|)
decl_stmt|;
DECL|field|lines
specifier|private
specifier|final
name|Queue
argument_list|<
name|String
argument_list|>
name|lines
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|lineBuf
specifier|private
specifier|final
name|LineBuffer
name|lineBuf
init|=
operator|new
name|LineBuffer
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|handleLine
parameter_list|(
name|String
name|line
parameter_list|,
name|String
name|end
parameter_list|)
block|{
name|lines
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|/**    * Creates a new instance that will read lines from the given    * {@code Readable} object.    */
DECL|method|LineReader (Readable readable)
specifier|public
name|LineReader
parameter_list|(
name|Readable
name|readable
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|readable
argument_list|)
expr_stmt|;
name|this
operator|.
name|readable
operator|=
name|readable
expr_stmt|;
name|this
operator|.
name|reader
operator|=
operator|(
name|readable
operator|instanceof
name|Reader
operator|)
condition|?
operator|(
name|Reader
operator|)
name|readable
else|:
literal|null
expr_stmt|;
block|}
comment|/**    * Reads a line of text. A line is considered to be terminated by any    * one of a line feed ({@code '\n'}), a carriage return    * ({@code '\r'}), or a carriage return followed immediately by a linefeed    * ({@code "\r\n"}).    *    * @return a {@code String} containing the contents of the line, not    *     including any line-termination characters, or {@code null} if the    *     end of the stream has been reached.    * @throws IOException if an I/O error occurs    */
DECL|method|readLine ()
specifier|public
name|String
name|readLine
parameter_list|()
throws|throws
name|IOException
block|{
while|while
condition|(
name|lines
operator|.
name|peek
argument_list|()
operator|==
literal|null
condition|)
block|{
name|cbuf
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// The default implementation of Reader#read(CharBuffer) allocates a
comment|// temporary char[], so we call Reader#read(char[], int, int) instead.
name|int
name|read
init|=
operator|(
name|reader
operator|!=
literal|null
operator|)
condition|?
name|reader
operator|.
name|read
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|buf
operator|.
name|length
argument_list|)
else|:
name|readable
operator|.
name|read
argument_list|(
name|cbuf
argument_list|)
decl_stmt|;
if|if
condition|(
name|read
operator|==
operator|-
literal|1
condition|)
block|{
name|lineBuf
operator|.
name|finish
argument_list|()
expr_stmt|;
break|break;
block|}
name|lineBuf
operator|.
name|add
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
return|return
name|lines
operator|.
name|poll
argument_list|()
return|;
block|}
block|}
end_class

end_unit

