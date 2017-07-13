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
name|base
operator|.
name|Strings
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
name|collect
operator|.
name|ImmutableList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|EOFException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilterReader
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
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|List
import|;
end_import

begin_comment
comment|/**  * Unit test for {@link CharStreams}.  *  * @author Chris Nokleberg  */
end_comment

begin_class
DECL|class|CharStreamsTest
specifier|public
class|class
name|CharStreamsTest
extends|extends
name|IoTestCase
block|{
DECL|field|TEXT
specifier|private
specifier|static
specifier|final
name|String
name|TEXT
init|=
literal|"The quick brown fox jumped over the lazy dog."
decl_stmt|;
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
name|TEXT
argument_list|,
name|CharStreams
operator|.
name|toString
argument_list|(
operator|new
name|StringReader
argument_list|(
name|TEXT
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testReadLines ()
specifier|public
name|void
name|testReadLines
parameter_list|()
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|lines
init|=
name|CharStreams
operator|.
name|readLines
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|"a\nb\nc"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
argument_list|,
name|lines
argument_list|)
expr_stmt|;
block|}
DECL|method|testReadLines_withLineProcessor ()
specifier|public
name|void
name|testReadLines_withLineProcessor
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|text
init|=
literal|"a\nb\nc"
decl_stmt|;
comment|// Test a LineProcessor that always returns false.
name|Reader
name|r
init|=
operator|new
name|StringReader
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|LineProcessor
argument_list|<
name|Integer
argument_list|>
name|alwaysFalse
init|=
operator|new
name|LineProcessor
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
name|int
name|seen
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|processLine
parameter_list|(
name|String
name|line
parameter_list|)
block|{
name|seen
operator|++
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|Integer
name|getResult
parameter_list|()
block|{
return|return
name|seen
return|;
block|}
block|}
decl_stmt|;
name|assertEquals
argument_list|(
literal|"processLine was called more than once"
argument_list|,
literal|1
argument_list|,
name|CharStreams
operator|.
name|readLines
argument_list|(
name|r
argument_list|,
name|alwaysFalse
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test a LineProcessor that always returns true.
name|r
operator|=
operator|new
name|StringReader
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|LineProcessor
argument_list|<
name|Integer
argument_list|>
name|alwaysTrue
init|=
operator|new
name|LineProcessor
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
name|int
name|seen
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|processLine
parameter_list|(
name|String
name|line
parameter_list|)
block|{
name|seen
operator|++
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|Integer
name|getResult
parameter_list|()
block|{
return|return
name|seen
return|;
block|}
block|}
decl_stmt|;
name|assertEquals
argument_list|(
literal|"processLine was not called for all the lines"
argument_list|,
literal|3
argument_list|,
name|CharStreams
operator|.
name|readLines
argument_list|(
name|r
argument_list|,
name|alwaysTrue
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test a LineProcessor that is conditional.
name|r
operator|=
operator|new
name|StringReader
argument_list|(
name|text
argument_list|)
expr_stmt|;
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|LineProcessor
argument_list|<
name|Integer
argument_list|>
name|conditional
init|=
operator|new
name|LineProcessor
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
name|int
name|seen
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|processLine
parameter_list|(
name|String
name|line
parameter_list|)
block|{
name|seen
operator|++
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|line
argument_list|)
expr_stmt|;
return|return
name|seen
operator|<
literal|2
return|;
block|}
annotation|@
name|Override
specifier|public
name|Integer
name|getResult
parameter_list|()
block|{
return|return
name|seen
return|;
block|}
block|}
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|CharStreams
operator|.
name|readLines
argument_list|(
name|r
argument_list|,
name|conditional
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ab"
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSkipFully_EOF ()
specifier|public
name|void
name|testSkipFully_EOF
parameter_list|()
throws|throws
name|IOException
block|{
name|Reader
name|reader
init|=
operator|new
name|StringReader
argument_list|(
literal|"abcde"
argument_list|)
decl_stmt|;
try|try
block|{
name|CharStreams
operator|.
name|skipFully
argument_list|(
name|reader
argument_list|,
literal|6
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected EOFException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|EOFException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
DECL|method|testSkipFully ()
specifier|public
name|void
name|testSkipFully
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|testString
init|=
literal|"abcdef"
decl_stmt|;
name|Reader
name|reader
init|=
operator|new
name|StringReader
argument_list|(
name|testString
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|testString
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|,
name|reader
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|CharStreams
operator|.
name|skipFully
argument_list|(
name|reader
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|testString
operator|.
name|charAt
argument_list|(
literal|2
argument_list|)
argument_list|,
name|reader
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|CharStreams
operator|.
name|skipFully
argument_list|(
name|reader
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|testString
operator|.
name|charAt
argument_list|(
literal|5
argument_list|)
argument_list|,
name|reader
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|reader
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsWriter ()
specifier|public
name|void
name|testAsWriter
parameter_list|()
block|{
comment|// Should wrap Appendable in a new object
name|Appendable
name|plainAppendable
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Writer
name|result
init|=
name|CharStreams
operator|.
name|asWriter
argument_list|(
name|plainAppendable
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|plainAppendable
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
comment|// A Writer should not be wrapped
name|Appendable
name|secretlyAWriter
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|result
operator|=
name|CharStreams
operator|.
name|asWriter
argument_list|(
name|secretlyAWriter
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|secretlyAWriter
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
comment|// CharStreams.copy has type specific optimizations for Readers,StringBuilders and Writers
DECL|method|testCopy ()
specifier|public
name|void
name|testCopy
parameter_list|()
throws|throws
name|IOException
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|long
name|copied
init|=
name|CharStreams
operator|.
name|copy
argument_list|(
name|wrapAsGenericReadable
argument_list|(
operator|new
name|StringReader
argument_list|(
name|ASCII
argument_list|)
argument_list|)
argument_list|,
name|wrapAsGenericAppendable
argument_list|(
name|builder
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ASCII
argument_list|,
name|builder
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ASCII
operator|.
name|length
argument_list|()
argument_list|,
name|copied
argument_list|)
expr_stmt|;
name|StringBuilder
name|builder2
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|copied
operator|=
name|CharStreams
operator|.
name|copy
argument_list|(
name|wrapAsGenericReadable
argument_list|(
operator|new
name|StringReader
argument_list|(
name|I18N
argument_list|)
argument_list|)
argument_list|,
name|wrapAsGenericAppendable
argument_list|(
name|builder2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|I18N
argument_list|,
name|builder2
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|I18N
operator|.
name|length
argument_list|()
argument_list|,
name|copied
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopy_toStringBuilder_fromReader ()
specifier|public
name|void
name|testCopy_toStringBuilder_fromReader
parameter_list|()
throws|throws
name|IOException
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|long
name|copied
init|=
name|CharStreams
operator|.
name|copy
argument_list|(
operator|new
name|StringReader
argument_list|(
name|ASCII
argument_list|)
argument_list|,
name|builder
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ASCII
argument_list|,
name|builder
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ASCII
operator|.
name|length
argument_list|()
argument_list|,
name|copied
argument_list|)
expr_stmt|;
name|StringBuilder
name|builder2
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|copied
operator|=
name|CharStreams
operator|.
name|copy
argument_list|(
operator|new
name|StringReader
argument_list|(
name|I18N
argument_list|)
argument_list|,
name|builder2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|I18N
argument_list|,
name|builder2
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|I18N
operator|.
name|length
argument_list|()
argument_list|,
name|copied
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopy_toStringBuilder_fromReadable ()
specifier|public
name|void
name|testCopy_toStringBuilder_fromReadable
parameter_list|()
throws|throws
name|IOException
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|long
name|copied
init|=
name|CharStreams
operator|.
name|copy
argument_list|(
name|wrapAsGenericReadable
argument_list|(
operator|new
name|StringReader
argument_list|(
name|ASCII
argument_list|)
argument_list|)
argument_list|,
name|builder
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ASCII
argument_list|,
name|builder
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ASCII
operator|.
name|length
argument_list|()
argument_list|,
name|copied
argument_list|)
expr_stmt|;
name|StringBuilder
name|builder2
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|copied
operator|=
name|CharStreams
operator|.
name|copy
argument_list|(
name|wrapAsGenericReadable
argument_list|(
operator|new
name|StringReader
argument_list|(
name|I18N
argument_list|)
argument_list|)
argument_list|,
name|builder2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|I18N
argument_list|,
name|builder2
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|I18N
operator|.
name|length
argument_list|()
argument_list|,
name|copied
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopy_toWriter_fromReader ()
specifier|public
name|void
name|testCopy_toWriter_fromReader
parameter_list|()
throws|throws
name|IOException
block|{
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|long
name|copied
init|=
name|CharStreams
operator|.
name|copy
argument_list|(
operator|new
name|StringReader
argument_list|(
name|ASCII
argument_list|)
argument_list|,
name|writer
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ASCII
argument_list|,
name|writer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ASCII
operator|.
name|length
argument_list|()
argument_list|,
name|copied
argument_list|)
expr_stmt|;
name|StringWriter
name|writer2
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|copied
operator|=
name|CharStreams
operator|.
name|copy
argument_list|(
operator|new
name|StringReader
argument_list|(
name|I18N
argument_list|)
argument_list|,
name|writer2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|I18N
argument_list|,
name|writer2
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|I18N
operator|.
name|length
argument_list|()
argument_list|,
name|copied
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopy_toWriter_fromReadable ()
specifier|public
name|void
name|testCopy_toWriter_fromReadable
parameter_list|()
throws|throws
name|IOException
block|{
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|long
name|copied
init|=
name|CharStreams
operator|.
name|copy
argument_list|(
name|wrapAsGenericReadable
argument_list|(
operator|new
name|StringReader
argument_list|(
name|ASCII
argument_list|)
argument_list|)
argument_list|,
name|writer
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ASCII
argument_list|,
name|writer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ASCII
operator|.
name|length
argument_list|()
argument_list|,
name|copied
argument_list|)
expr_stmt|;
name|StringWriter
name|writer2
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|copied
operator|=
name|CharStreams
operator|.
name|copy
argument_list|(
name|wrapAsGenericReadable
argument_list|(
operator|new
name|StringReader
argument_list|(
name|I18N
argument_list|)
argument_list|)
argument_list|,
name|writer2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|I18N
argument_list|,
name|writer2
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|I18N
operator|.
name|length
argument_list|()
argument_list|,
name|copied
argument_list|)
expr_stmt|;
block|}
comment|/**    * Test for Guava issue 1061: http://code.google.com/p/guava-libraries/issues/detail?id=1061    *    *<p>CharStreams.copy was failing to clear its CharBuffer after each read call, which effectively    * reduced the available size of the buffer each time a call to read didn't fill up the available    * space in the buffer completely. In general this is a performance problem since the buffer size    * is permanently reduced, but with certain Reader implementations it could also cause the buffer    * size to reach 0, causing an infinite loop.    */
DECL|method|testCopyWithReaderThatDoesNotFillBuffer ()
specifier|public
name|void
name|testCopyWithReaderThatDoesNotFillBuffer
parameter_list|()
throws|throws
name|IOException
block|{
comment|// need a long enough string for the buffer to hit 0 remaining before the copy completes
name|String
name|string
init|=
name|Strings
operator|.
name|repeat
argument_list|(
literal|"0123456789"
argument_list|,
literal|100
argument_list|)
decl_stmt|;
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// the main assertion of this test is here... the copy will fail if the buffer size goes down
comment|// each time it is not filled completely
name|long
name|copied
init|=
name|CharStreams
operator|.
name|copy
argument_list|(
name|newNonBufferFillingReader
argument_list|(
operator|new
name|StringReader
argument_list|(
name|string
argument_list|)
argument_list|)
argument_list|,
name|b
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|string
argument_list|,
name|b
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|string
operator|.
name|length
argument_list|()
argument_list|,
name|copied
argument_list|)
expr_stmt|;
block|}
DECL|method|testExhaust_reader ()
specifier|public
name|void
name|testExhaust_reader
parameter_list|()
throws|throws
name|IOException
block|{
name|Reader
name|reader
init|=
operator|new
name|StringReader
argument_list|(
name|ASCII
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ASCII
operator|.
name|length
argument_list|()
argument_list|,
name|CharStreams
operator|.
name|exhaust
argument_list|(
name|reader
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|reader
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|CharStreams
operator|.
name|exhaust
argument_list|(
name|reader
argument_list|)
argument_list|)
expr_stmt|;
name|Reader
name|empty
init|=
operator|new
name|StringReader
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|CharStreams
operator|.
name|exhaust
argument_list|(
name|empty
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|empty
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testExhaust_readable ()
specifier|public
name|void
name|testExhaust_readable
parameter_list|()
throws|throws
name|IOException
block|{
name|CharBuffer
name|buf
init|=
name|CharBuffer
operator|.
name|wrap
argument_list|(
name|ASCII
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ASCII
operator|.
name|length
argument_list|()
argument_list|,
name|CharStreams
operator|.
name|exhaust
argument_list|(
name|buf
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|buf
operator|.
name|remaining
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|CharStreams
operator|.
name|exhaust
argument_list|(
name|buf
argument_list|)
argument_list|)
expr_stmt|;
name|CharBuffer
name|empty
init|=
name|CharBuffer
operator|.
name|wrap
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|CharStreams
operator|.
name|exhaust
argument_list|(
name|empty
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|empty
operator|.
name|remaining
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testNullWriter ()
specifier|public
name|void
name|testNullWriter
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create a null writer
name|Writer
name|nullWriter
init|=
name|CharStreams
operator|.
name|nullWriter
argument_list|()
decl_stmt|;
comment|// write to the writer
name|nullWriter
operator|.
name|write
argument_list|(
literal|'n'
argument_list|)
expr_stmt|;
name|String
name|test
init|=
literal|"Test string for NullWriter"
decl_stmt|;
name|nullWriter
operator|.
name|write
argument_list|(
name|test
argument_list|)
expr_stmt|;
name|nullWriter
operator|.
name|write
argument_list|(
name|test
argument_list|,
literal|2
argument_list|,
literal|10
argument_list|)
expr_stmt|;
comment|// nothing really to assert?
name|assertSame
argument_list|(
name|CharStreams
operator|.
name|nullWriter
argument_list|()
argument_list|,
name|CharStreams
operator|.
name|nullWriter
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns a reader wrapping the given reader that only reads half of the maximum number of    * characters that it could read in read(char[], int, int).    */
DECL|method|newNonBufferFillingReader (Reader reader)
specifier|private
specifier|static
name|Reader
name|newNonBufferFillingReader
parameter_list|(
name|Reader
name|reader
parameter_list|)
block|{
return|return
operator|new
name|FilterReader
argument_list|(
name|reader
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|int
name|read
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
comment|// if a buffer isn't being cleared correctly, this method will eventually start being called
comment|// with a len of 0 forever
if|if
condition|(
name|len
operator|<=
literal|0
condition|)
block|{
name|fail
argument_list|(
literal|"read called with a len of "
operator|+
name|len
argument_list|)
expr_stmt|;
block|}
comment|// read fewer than the max number of chars to read
comment|// shouldn't be a problem unless the buffer is shrinking each call
return|return
name|in
operator|.
name|read
argument_list|(
name|cbuf
argument_list|,
name|off
argument_list|,
name|Math
operator|.
name|max
argument_list|(
name|len
operator|-
literal|1024
argument_list|,
literal|0
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**    * Wrap an appendable in an appendable to defeat any type specific optimizations.    */
DECL|method|wrapAsGenericAppendable (final Appendable a)
specifier|private
specifier|static
name|Appendable
name|wrapAsGenericAppendable
parameter_list|(
specifier|final
name|Appendable
name|a
parameter_list|)
block|{
return|return
operator|new
name|Appendable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Appendable
name|append
parameter_list|(
name|CharSequence
name|csq
parameter_list|)
throws|throws
name|IOException
block|{
name|a
operator|.
name|append
argument_list|(
name|csq
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Appendable
name|append
parameter_list|(
name|CharSequence
name|csq
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
name|a
operator|.
name|append
argument_list|(
name|csq
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
annotation|@
name|Override
specifier|public
name|Appendable
name|append
parameter_list|(
name|char
name|c
parameter_list|)
throws|throws
name|IOException
block|{
name|a
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
block|}
return|;
block|}
comment|/**    * Wrap a readable in a readable to defeat any type specific optimizations.    */
DECL|method|wrapAsGenericReadable (final Readable a)
specifier|private
specifier|static
name|Readable
name|wrapAsGenericReadable
parameter_list|(
specifier|final
name|Readable
name|a
parameter_list|)
block|{
return|return
operator|new
name|Readable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|(
name|CharBuffer
name|cb
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|a
operator|.
name|read
argument_list|(
name|cb
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

