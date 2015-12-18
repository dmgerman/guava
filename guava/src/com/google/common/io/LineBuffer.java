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
name|GwtIncompatible
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
comment|/**  * Package-protected abstract class that implements the line reading  * algorithm used by {@link LineReader}. Line separators are per {@link  * java.io.BufferedReader}: line feed, carriage return, or carriage  * return followed immediately by a linefeed.  *  *<p>Subclasses must implement {@link #handleLine}, call {@link #add}  * to pass character data, and call {@link #finish} at the end of stream.  *  * @author Chris Nokleberg  * @since 1.0  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|LineBuffer
specifier|abstract
class|class
name|LineBuffer
block|{
comment|/** Holds partial line contents. */
DECL|field|line
specifier|private
name|StringBuilder
name|line
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|/** Whether a line ending with a CR is pending processing. */
DECL|field|sawReturn
specifier|private
name|boolean
name|sawReturn
decl_stmt|;
comment|/**    * Process additional characters from the stream. When a line separator    * is found the contents of the line and the line separator itself    * are passed to the abstract {@link #handleLine} method.    *    * @param cbuf the character buffer to process    * @param off the offset into the buffer    * @param len the number of characters to process    * @throws IOException if an I/O error occurs    * @see #finish    */
DECL|method|add (char[] cbuf, int off, int len)
specifier|protected
name|void
name|add
parameter_list|(
name|char
index|[]
name|cbuf
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
name|pos
init|=
name|off
decl_stmt|;
if|if
condition|(
name|sawReturn
operator|&&
name|len
operator|>
literal|0
condition|)
block|{
comment|// Last call to add ended with a CR; we can handle the line now.
if|if
condition|(
name|finishLine
argument_list|(
name|cbuf
index|[
name|pos
index|]
operator|==
literal|'\n'
argument_list|)
condition|)
block|{
name|pos
operator|++
expr_stmt|;
block|}
block|}
name|int
name|start
init|=
name|pos
decl_stmt|;
for|for
control|(
name|int
name|end
init|=
name|off
operator|+
name|len
init|;
name|pos
operator|<
name|end
condition|;
name|pos
operator|++
control|)
block|{
switch|switch
condition|(
name|cbuf
index|[
name|pos
index|]
condition|)
block|{
case|case
literal|'\r'
case|:
name|line
operator|.
name|append
argument_list|(
name|cbuf
argument_list|,
name|start
argument_list|,
name|pos
operator|-
name|start
argument_list|)
expr_stmt|;
name|sawReturn
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|pos
operator|+
literal|1
operator|<
name|end
condition|)
block|{
if|if
condition|(
name|finishLine
argument_list|(
name|cbuf
index|[
name|pos
operator|+
literal|1
index|]
operator|==
literal|'\n'
argument_list|)
condition|)
block|{
name|pos
operator|++
expr_stmt|;
block|}
block|}
name|start
operator|=
name|pos
operator|+
literal|1
expr_stmt|;
break|break;
case|case
literal|'\n'
case|:
name|line
operator|.
name|append
argument_list|(
name|cbuf
argument_list|,
name|start
argument_list|,
name|pos
operator|-
name|start
argument_list|)
expr_stmt|;
name|finishLine
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|start
operator|=
name|pos
operator|+
literal|1
expr_stmt|;
break|break;
default|default:
comment|// do nothing
block|}
block|}
name|line
operator|.
name|append
argument_list|(
name|cbuf
argument_list|,
name|start
argument_list|,
name|off
operator|+
name|len
operator|-
name|start
argument_list|)
expr_stmt|;
block|}
comment|/** Called when a line is complete. */
DECL|method|finishLine (boolean sawNewline)
specifier|private
name|boolean
name|finishLine
parameter_list|(
name|boolean
name|sawNewline
parameter_list|)
throws|throws
name|IOException
block|{
name|handleLine
argument_list|(
name|line
operator|.
name|toString
argument_list|()
argument_list|,
name|sawReturn
condition|?
operator|(
name|sawNewline
condition|?
literal|"\r\n"
else|:
literal|"\r"
operator|)
else|:
operator|(
name|sawNewline
condition|?
literal|"\n"
else|:
literal|""
operator|)
argument_list|)
expr_stmt|;
name|line
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
name|sawReturn
operator|=
literal|false
expr_stmt|;
return|return
name|sawNewline
return|;
block|}
comment|/**    * Subclasses must call this method after finishing character processing,    * in order to ensure that any unterminated line in the buffer is    * passed to {@link #handleLine}.    *    * @throws IOException if an I/O error occurs    */
DECL|method|finish ()
specifier|protected
name|void
name|finish
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|sawReturn
operator|||
name|line
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|finishLine
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Called for each line found in the character data passed to    * {@link #add}.    *    * @param line a line of text (possibly empty), without any line separators    * @param end the line separator; one of {@code "\r"}, {@code "\n"},    *     {@code "\r\n"}, or {@code ""}    * @throws IOException if an I/O error occurs    */
DECL|method|handleLine (String line, String end)
specifier|protected
specifier|abstract
name|void
name|handleLine
parameter_list|(
name|String
name|line
parameter_list|,
name|String
name|end
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_class

end_unit

