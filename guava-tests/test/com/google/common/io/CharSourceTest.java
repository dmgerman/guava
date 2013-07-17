begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|TestOption
operator|.
name|CLOSE_THROWS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|TestOption
operator|.
name|OPEN_THROWS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|TestOption
operator|.
name|READ_THROWS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|TestOption
operator|.
name|WRITE_THROWS
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableSet
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
name|testing
operator|.
name|TestLogHandler
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestSuite
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|util
operator|.
name|EnumSet
import|;
end_import

begin_comment
comment|/**  * Tests for the default implementations of {@code CharSource} methods.  *  * @author Colin Decker  */
end_comment

begin_class
DECL|class|CharSourceTest
specifier|public
class|class
name|CharSourceTest
extends|extends
name|IoTestCase
block|{
DECL|method|suite ()
specifier|public
specifier|static
name|TestSuite
name|suite
parameter_list|()
block|{
name|TestSuite
name|suite
init|=
operator|new
name|TestSuite
argument_list|()
decl_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|CharSourceTester
operator|.
name|tests
argument_list|(
literal|"CharSource.wrap[CharSequence]"
argument_list|,
name|SourceSinkFactories
operator|.
name|stringCharSourceFactory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|CharSourceTester
operator|.
name|tests
argument_list|(
literal|"CharSource.empty[]"
argument_list|,
name|SourceSinkFactories
operator|.
name|emptyCharSourceFactory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTestSuite
argument_list|(
name|CharStreamsTest
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|field|STRING
specifier|private
specifier|static
specifier|final
name|String
name|STRING
init|=
name|ASCII
operator|+
name|I18N
decl_stmt|;
DECL|field|LINES
specifier|private
specifier|static
specifier|final
name|String
name|LINES
init|=
literal|"foo\nbar\r\nbaz\rsomething"
decl_stmt|;
DECL|field|source
specifier|private
name|TestCharSource
name|source
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|source
operator|=
operator|new
name|TestCharSource
argument_list|(
name|STRING
argument_list|)
expr_stmt|;
block|}
DECL|method|testOpenBufferedStream ()
specifier|public
name|void
name|testOpenBufferedStream
parameter_list|()
throws|throws
name|IOException
block|{
name|BufferedReader
name|reader
init|=
name|source
operator|.
name|openBufferedStream
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|source
operator|.
name|wasStreamOpened
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|source
operator|.
name|wasStreamClosed
argument_list|()
argument_list|)
expr_stmt|;
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|char
index|[]
name|buf
init|=
operator|new
name|char
index|[
literal|64
index|]
decl_stmt|;
name|int
name|read
decl_stmt|;
while|while
condition|(
operator|(
name|read
operator|=
name|reader
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|writer
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|source
operator|.
name|wasStreamClosed
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|STRING
argument_list|,
name|writer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyTo_appendable ()
specifier|public
name|void
name|testCopyTo_appendable
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
name|assertEquals
argument_list|(
name|STRING
operator|.
name|length
argument_list|()
argument_list|,
name|source
operator|.
name|copyTo
argument_list|(
name|builder
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|source
operator|.
name|wasStreamOpened
argument_list|()
operator|&&
name|source
operator|.
name|wasStreamClosed
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|STRING
argument_list|,
name|builder
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyTo_charSink ()
specifier|public
name|void
name|testCopyTo_charSink
parameter_list|()
throws|throws
name|IOException
block|{
name|TestCharSink
name|sink
init|=
operator|new
name|TestCharSink
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|sink
operator|.
name|wasStreamOpened
argument_list|()
operator|||
name|sink
operator|.
name|wasStreamClosed
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|STRING
operator|.
name|length
argument_list|()
argument_list|,
name|source
operator|.
name|copyTo
argument_list|(
name|sink
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|source
operator|.
name|wasStreamOpened
argument_list|()
operator|&&
name|source
operator|.
name|wasStreamClosed
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sink
operator|.
name|wasStreamOpened
argument_list|()
operator|&&
name|sink
operator|.
name|wasStreamClosed
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|STRING
argument_list|,
name|sink
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRead_toString ()
specifier|public
name|void
name|testRead_toString
parameter_list|()
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
name|STRING
argument_list|,
name|source
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|source
operator|.
name|wasStreamOpened
argument_list|()
operator|&&
name|source
operator|.
name|wasStreamClosed
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testReadFirstLine ()
specifier|public
name|void
name|testReadFirstLine
parameter_list|()
throws|throws
name|IOException
block|{
name|TestCharSource
name|lines
init|=
operator|new
name|TestCharSource
argument_list|(
name|LINES
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|lines
operator|.
name|readFirstLine
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|lines
operator|.
name|wasStreamOpened
argument_list|()
operator|&&
name|lines
operator|.
name|wasStreamClosed
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testReadLines_toList ()
specifier|public
name|void
name|testReadLines_toList
parameter_list|()
throws|throws
name|IOException
block|{
name|TestCharSource
name|lines
init|=
operator|new
name|TestCharSource
argument_list|(
name|LINES
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|"baz"
argument_list|,
literal|"something"
argument_list|)
argument_list|,
name|lines
operator|.
name|readLines
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|lines
operator|.
name|wasStreamOpened
argument_list|()
operator|&&
name|lines
operator|.
name|wasStreamClosed
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyToAppendable_doesNotCloseIfWriter ()
specifier|public
name|void
name|testCopyToAppendable_doesNotCloseIfWriter
parameter_list|()
throws|throws
name|IOException
block|{
name|TestWriter
name|writer
init|=
operator|new
name|TestWriter
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|writer
operator|.
name|closed
argument_list|()
argument_list|)
expr_stmt|;
name|source
operator|.
name|copyTo
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|writer
operator|.
name|closed
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testClosesOnErrors_copyingToCharSinkThatThrows ()
specifier|public
name|void
name|testClosesOnErrors_copyingToCharSinkThatThrows
parameter_list|()
block|{
for|for
control|(
name|TestOption
name|option
range|:
name|EnumSet
operator|.
name|of
argument_list|(
name|OPEN_THROWS
argument_list|,
name|WRITE_THROWS
argument_list|,
name|CLOSE_THROWS
argument_list|)
control|)
block|{
name|TestCharSource
name|okSource
init|=
operator|new
name|TestCharSource
argument_list|(
name|STRING
argument_list|)
decl_stmt|;
try|try
block|{
name|okSource
operator|.
name|copyTo
argument_list|(
operator|new
name|TestCharSink
argument_list|(
name|option
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|expected
parameter_list|)
block|{       }
comment|// ensure reader was closed IF it was opened (depends on implementation whether or not it's
comment|// opened at all if sink.newWriter() throws).
name|assertTrue
argument_list|(
literal|"stream not closed when copying to sink with option: "
operator|+
name|option
argument_list|,
operator|!
name|okSource
operator|.
name|wasStreamOpened
argument_list|()
operator|||
name|okSource
operator|.
name|wasStreamClosed
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testClosesOnErrors_whenReadThrows ()
specifier|public
name|void
name|testClosesOnErrors_whenReadThrows
parameter_list|()
block|{
name|TestCharSource
name|failSource
init|=
operator|new
name|TestCharSource
argument_list|(
name|STRING
argument_list|,
name|READ_THROWS
argument_list|)
decl_stmt|;
try|try
block|{
name|failSource
operator|.
name|copyTo
argument_list|(
operator|new
name|TestCharSink
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|expected
parameter_list|)
block|{     }
name|assertTrue
argument_list|(
name|failSource
operator|.
name|wasStreamClosed
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testClosesOnErrors_copyingToWriterThatThrows ()
specifier|public
name|void
name|testClosesOnErrors_copyingToWriterThatThrows
parameter_list|()
block|{
name|TestCharSource
name|okSource
init|=
operator|new
name|TestCharSource
argument_list|(
name|STRING
argument_list|)
decl_stmt|;
try|try
block|{
name|okSource
operator|.
name|copyTo
argument_list|(
operator|new
name|TestWriter
argument_list|(
name|WRITE_THROWS
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|expected
parameter_list|)
block|{     }
name|assertTrue
argument_list|(
name|okSource
operator|.
name|wasStreamClosed
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testConcat ()
specifier|public
name|void
name|testConcat
parameter_list|()
throws|throws
name|IOException
block|{
name|CharSource
name|c1
init|=
name|CharSource
operator|.
name|wrap
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|CharSource
name|c2
init|=
name|CharSource
operator|.
name|wrap
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|CharSource
name|c3
init|=
name|CharSource
operator|.
name|wrap
argument_list|(
literal|"de"
argument_list|)
decl_stmt|;
name|String
name|expected
init|=
literal|"abcde"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|CharSource
operator|.
name|concat
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|c1
argument_list|,
name|c2
argument_list|,
name|c3
argument_list|)
argument_list|)
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|CharSource
operator|.
name|concat
argument_list|(
name|c1
argument_list|,
name|c2
argument_list|,
name|c3
argument_list|)
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|CharSource
operator|.
name|concat
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|c1
argument_list|,
name|c2
argument_list|,
name|c3
argument_list|)
operator|.
name|iterator
argument_list|()
argument_list|)
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|CharSource
operator|.
name|concat
argument_list|(
name|c1
argument_list|,
name|c2
argument_list|,
name|c3
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|CharSource
name|emptyConcat
init|=
name|CharSource
operator|.
name|concat
argument_list|(
name|CharSource
operator|.
name|empty
argument_list|()
argument_list|,
name|CharSource
operator|.
name|empty
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|emptyConcat
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|field|BROKEN_READ_SOURCE
specifier|static
specifier|final
name|CharSource
name|BROKEN_READ_SOURCE
init|=
operator|new
name|TestCharSource
argument_list|(
literal|"ABC"
argument_list|,
name|READ_THROWS
argument_list|)
decl_stmt|;
DECL|field|BROKEN_CLOSE_SOURCE
specifier|static
specifier|final
name|CharSource
name|BROKEN_CLOSE_SOURCE
init|=
operator|new
name|TestCharSource
argument_list|(
literal|"ABC"
argument_list|,
name|CLOSE_THROWS
argument_list|)
decl_stmt|;
DECL|field|BROKEN_OPEN_SOURCE
specifier|static
specifier|final
name|CharSource
name|BROKEN_OPEN_SOURCE
init|=
operator|new
name|TestCharSource
argument_list|(
literal|"ABC"
argument_list|,
name|OPEN_THROWS
argument_list|)
decl_stmt|;
DECL|field|BROKEN_WRITE_SINK
specifier|static
specifier|final
name|CharSink
name|BROKEN_WRITE_SINK
init|=
operator|new
name|TestCharSink
argument_list|(
name|WRITE_THROWS
argument_list|)
decl_stmt|;
DECL|field|BROKEN_CLOSE_SINK
specifier|static
specifier|final
name|CharSink
name|BROKEN_CLOSE_SINK
init|=
operator|new
name|TestCharSink
argument_list|(
name|CLOSE_THROWS
argument_list|)
decl_stmt|;
DECL|field|BROKEN_OPEN_SINK
specifier|static
specifier|final
name|CharSink
name|BROKEN_OPEN_SINK
init|=
operator|new
name|TestCharSink
argument_list|(
name|OPEN_THROWS
argument_list|)
decl_stmt|;
DECL|field|BROKEN_SOURCES
specifier|private
specifier|static
specifier|final
name|ImmutableSet
argument_list|<
name|CharSource
argument_list|>
name|BROKEN_SOURCES
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
name|BROKEN_CLOSE_SOURCE
argument_list|,
name|BROKEN_OPEN_SOURCE
argument_list|,
name|BROKEN_READ_SOURCE
argument_list|)
decl_stmt|;
DECL|field|BROKEN_SINKS
specifier|private
specifier|static
specifier|final
name|ImmutableSet
argument_list|<
name|CharSink
argument_list|>
name|BROKEN_SINKS
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
name|BROKEN_CLOSE_SINK
argument_list|,
name|BROKEN_OPEN_SINK
argument_list|,
name|BROKEN_WRITE_SINK
argument_list|)
decl_stmt|;
DECL|method|testCopyExceptions ()
specifier|public
name|void
name|testCopyExceptions
parameter_list|()
block|{
if|if
condition|(
operator|!
name|Closer
operator|.
name|SuppressingSuppressor
operator|.
name|isAvailable
argument_list|()
condition|)
block|{
comment|// test that exceptions are logged
name|TestLogHandler
name|logHandler
init|=
operator|new
name|TestLogHandler
argument_list|()
decl_stmt|;
name|Closeables
operator|.
name|logger
operator|.
name|addHandler
argument_list|(
name|logHandler
argument_list|)
expr_stmt|;
try|try
block|{
for|for
control|(
name|CharSource
name|in
range|:
name|BROKEN_SOURCES
control|)
block|{
name|runFailureTest
argument_list|(
name|in
argument_list|,
name|newNormalCharSink
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|logHandler
operator|.
name|getStoredLogRecords
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|runFailureTest
argument_list|(
name|in
argument_list|,
name|BROKEN_CLOSE_SINK
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|in
operator|==
name|BROKEN_OPEN_SOURCE
operator|)
condition|?
literal|0
else|:
literal|1
argument_list|,
name|getAndResetRecords
argument_list|(
name|logHandler
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|CharSink
name|out
range|:
name|BROKEN_SINKS
control|)
block|{
name|runFailureTest
argument_list|(
name|newNormalCharSource
argument_list|()
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|logHandler
operator|.
name|getStoredLogRecords
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|runFailureTest
argument_list|(
name|BROKEN_CLOSE_SOURCE
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getAndResetRecords
argument_list|(
name|logHandler
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|CharSource
name|in
range|:
name|BROKEN_SOURCES
control|)
block|{
for|for
control|(
name|CharSink
name|out
range|:
name|BROKEN_SINKS
control|)
block|{
name|runFailureTest
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getAndResetRecords
argument_list|(
name|logHandler
argument_list|)
operator|<=
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|Closeables
operator|.
name|logger
operator|.
name|removeHandler
argument_list|(
name|logHandler
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// test that exceptions are suppressed
for|for
control|(
name|CharSource
name|in
range|:
name|BROKEN_SOURCES
control|)
block|{
name|int
name|suppressed
init|=
name|runSuppressionFailureTest
argument_list|(
name|in
argument_list|,
name|newNormalCharSink
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|suppressed
argument_list|)
expr_stmt|;
name|suppressed
operator|=
name|runSuppressionFailureTest
argument_list|(
name|in
argument_list|,
name|BROKEN_CLOSE_SINK
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|in
operator|==
name|BROKEN_OPEN_SOURCE
operator|)
condition|?
literal|0
else|:
literal|1
argument_list|,
name|suppressed
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|CharSink
name|out
range|:
name|BROKEN_SINKS
control|)
block|{
name|int
name|suppressed
init|=
name|runSuppressionFailureTest
argument_list|(
name|newNormalCharSource
argument_list|()
argument_list|,
name|out
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|suppressed
argument_list|)
expr_stmt|;
name|suppressed
operator|=
name|runSuppressionFailureTest
argument_list|(
name|BROKEN_CLOSE_SOURCE
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|suppressed
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|CharSource
name|in
range|:
name|BROKEN_SOURCES
control|)
block|{
for|for
control|(
name|CharSink
name|out
range|:
name|BROKEN_SINKS
control|)
block|{
name|int
name|suppressed
init|=
name|runSuppressionFailureTest
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|suppressed
operator|<=
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|getAndResetRecords (TestLogHandler logHandler)
specifier|private
specifier|static
name|int
name|getAndResetRecords
parameter_list|(
name|TestLogHandler
name|logHandler
parameter_list|)
block|{
name|int
name|records
init|=
name|logHandler
operator|.
name|getStoredLogRecords
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|logHandler
operator|.
name|clear
argument_list|()
expr_stmt|;
return|return
name|records
return|;
block|}
DECL|method|runFailureTest (CharSource in, CharSink out)
specifier|private
specifier|static
name|void
name|runFailureTest
parameter_list|(
name|CharSource
name|in
parameter_list|,
name|CharSink
name|out
parameter_list|)
block|{
try|try
block|{
name|in
operator|.
name|copyTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|expected
parameter_list|)
block|{     }
block|}
comment|/**    * @return the number of exceptions that were suppressed on the expected thrown exception    */
DECL|method|runSuppressionFailureTest (CharSource in, CharSink out)
specifier|private
specifier|static
name|int
name|runSuppressionFailureTest
parameter_list|(
name|CharSource
name|in
parameter_list|,
name|CharSink
name|out
parameter_list|)
block|{
try|try
block|{
name|in
operator|.
name|copyTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|expected
parameter_list|)
block|{
return|return
name|CloserTest
operator|.
name|getSuppressed
argument_list|(
name|expected
argument_list|)
operator|.
name|length
return|;
block|}
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
comment|// can't happen
block|}
DECL|method|newNormalCharSource ()
specifier|private
specifier|static
name|CharSource
name|newNormalCharSource
parameter_list|()
block|{
return|return
name|CharSource
operator|.
name|wrap
argument_list|(
literal|"ABC"
argument_list|)
return|;
block|}
DECL|method|newNormalCharSink ()
specifier|private
specifier|static
name|CharSink
name|newNormalCharSink
parameter_list|()
block|{
return|return
operator|new
name|CharSink
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Writer
name|openStream
parameter_list|()
block|{
return|return
operator|new
name|StringWriter
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

