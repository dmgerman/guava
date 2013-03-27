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
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|SKIP_THROWS
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertArrayEquals
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
name|Charsets
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
name|hash
operator|.
name|Hashing
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
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
comment|/**  * Tests for the default implementations of {@code ByteSource} methods.  *  * @author Colin Decker  */
end_comment

begin_class
DECL|class|ByteSourceTest
specifier|public
class|class
name|ByteSourceTest
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
name|ByteSourceTester
operator|.
name|tests
argument_list|(
literal|"ByteSource.wrap[byte[]]"
argument_list|,
name|SourceSinkFactories
operator|.
name|byteArraySourceFactory
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTestSuite
argument_list|(
name|ByteSourceTest
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|field|bytes
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|bytes
init|=
name|newPreFilledByteArray
argument_list|(
literal|10000
argument_list|)
decl_stmt|;
DECL|field|source
specifier|private
name|TestByteSource
name|source
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|source
operator|=
operator|new
name|TestByteSource
argument_list|(
name|bytes
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
name|BufferedInputStream
name|in
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
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ByteStreams
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|out
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
name|assertArrayEquals
argument_list|(
name|bytes
argument_list|,
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSize ()
specifier|public
name|void
name|testSize
parameter_list|()
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
name|bytes
operator|.
name|length
argument_list|,
name|source
operator|.
name|size
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
comment|// test that we can get the size even if skip() isn't supported
name|assertEquals
argument_list|(
name|bytes
operator|.
name|length
argument_list|,
operator|new
name|TestByteSource
argument_list|(
name|bytes
argument_list|,
name|SKIP_THROWS
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyTo_outputStream ()
specifier|public
name|void
name|testCopyTo_outputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|bytes
operator|.
name|length
argument_list|,
name|source
operator|.
name|copyTo
argument_list|(
name|out
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
name|assertArrayEquals
argument_list|(
name|bytes
argument_list|,
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyTo_byteSink ()
specifier|public
name|void
name|testCopyTo_byteSink
parameter_list|()
throws|throws
name|IOException
block|{
name|TestByteSink
name|sink
init|=
operator|new
name|TestByteSink
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
name|bytes
operator|.
name|length
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
name|assertArrayEquals
argument_list|(
name|bytes
argument_list|,
name|sink
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRead_toArray ()
specifier|public
name|void
name|testRead_toArray
parameter_list|()
throws|throws
name|IOException
block|{
name|assertArrayEquals
argument_list|(
name|bytes
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
DECL|method|testHash ()
specifier|public
name|void
name|testHash
parameter_list|()
throws|throws
name|IOException
block|{
name|ByteSource
name|byteSource
init|=
operator|new
name|TestByteSource
argument_list|(
literal|"hamburger\n"
operator|.
name|getBytes
argument_list|(
name|Charsets
operator|.
name|US_ASCII
argument_list|)
argument_list|)
decl_stmt|;
comment|// Pasted this expected string from `echo hamburger | md5sum`
name|assertEquals
argument_list|(
literal|"cfa0c5002275c90508338a5cdb2a9781"
argument_list|,
name|byteSource
operator|.
name|hash
argument_list|(
name|Hashing
operator|.
name|md5
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testContentEquals ()
specifier|public
name|void
name|testContentEquals
parameter_list|()
throws|throws
name|IOException
block|{
name|assertTrue
argument_list|(
name|source
operator|.
name|contentEquals
argument_list|(
name|source
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
name|ByteSource
name|equalSource
init|=
operator|new
name|TestByteSource
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|source
operator|.
name|contentEquals
argument_list|(
name|equalSource
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|TestByteSource
argument_list|(
name|bytes
argument_list|)
operator|.
name|contentEquals
argument_list|(
name|source
argument_list|)
argument_list|)
expr_stmt|;
name|ByteSource
name|fewerBytes
init|=
operator|new
name|TestByteSource
argument_list|(
name|newPreFilledByteArray
argument_list|(
name|bytes
operator|.
name|length
operator|/
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|source
operator|.
name|contentEquals
argument_list|(
name|fewerBytes
argument_list|)
argument_list|)
expr_stmt|;
name|byte
index|[]
name|copy
init|=
name|bytes
operator|.
name|clone
argument_list|()
decl_stmt|;
name|copy
index|[
literal|9876
index|]
operator|=
literal|1
expr_stmt|;
name|ByteSource
name|oneByteOff
init|=
operator|new
name|TestByteSource
argument_list|(
name|copy
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|source
operator|.
name|contentEquals
argument_list|(
name|oneByteOff
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSlice ()
specifier|public
name|void
name|testSlice
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Test preconditions
try|try
block|{
name|source
operator|.
name|slice
argument_list|(
operator|-
literal|1
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
try|try
block|{
name|source
operator|.
name|slice
argument_list|(
literal|0
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
name|assertCorrectSlice
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertCorrectSlice
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertCorrectSlice
argument_list|(
literal|100
argument_list|,
literal|0
argument_list|,
literal|10
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|assertCorrectSlice
argument_list|(
literal|100
argument_list|,
literal|0
argument_list|,
literal|100
argument_list|,
literal|100
argument_list|)
expr_stmt|;
name|assertCorrectSlice
argument_list|(
literal|100
argument_list|,
literal|5
argument_list|,
literal|10
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|assertCorrectSlice
argument_list|(
literal|100
argument_list|,
literal|5
argument_list|,
literal|100
argument_list|,
literal|95
argument_list|)
expr_stmt|;
name|assertCorrectSlice
argument_list|(
literal|100
argument_list|,
literal|100
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertCorrectSlice
argument_list|(
literal|100
argument_list|,
literal|100
argument_list|,
literal|10
argument_list|,
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|assertCorrectSlice
argument_list|(
literal|100
argument_list|,
literal|101
argument_list|,
literal|10
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|EOFException
name|expected
parameter_list|)
block|{     }
block|}
comment|/**    * @param input      the size of the input source    * @param offset     the first argument to {@link ByteStreams#slice}    * @param length     the second argument to {@link ByteStreams#slice}    * @param expectRead the number of bytes we expect to read    */
DECL|method|assertCorrectSlice ( int input, int offset, long length, int expectRead)
specifier|private
specifier|static
name|void
name|assertCorrectSlice
parameter_list|(
name|int
name|input
parameter_list|,
name|int
name|offset
parameter_list|,
name|long
name|length
parameter_list|,
name|int
name|expectRead
parameter_list|)
throws|throws
name|IOException
block|{
name|checkArgument
argument_list|(
name|expectRead
operator|==
operator|(
name|int
operator|)
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|Math
operator|.
name|min
argument_list|(
name|input
argument_list|,
name|offset
operator|+
name|length
argument_list|)
operator|-
name|offset
argument_list|)
argument_list|)
expr_stmt|;
name|byte
index|[]
name|expected
init|=
name|newPreFilledByteArray
argument_list|(
name|offset
argument_list|,
name|expectRead
argument_list|)
decl_stmt|;
name|ByteSource
name|source
init|=
operator|new
name|TestByteSource
argument_list|(
name|newPreFilledByteArray
argument_list|(
name|input
argument_list|)
argument_list|)
decl_stmt|;
name|ByteSource
name|slice
init|=
name|source
operator|.
name|slice
argument_list|(
name|offset
argument_list|,
name|length
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
name|expected
argument_list|,
name|slice
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyToStream_doesNotCloseThatStream ()
specifier|public
name|void
name|testCopyToStream_doesNotCloseThatStream
parameter_list|()
throws|throws
name|IOException
block|{
name|TestOutputStream
name|out
init|=
operator|new
name|TestOutputStream
argument_list|(
name|ByteStreams
operator|.
name|nullOutputStream
argument_list|()
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|out
operator|.
name|closed
argument_list|()
argument_list|)
expr_stmt|;
name|source
operator|.
name|copyTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|out
operator|.
name|closed
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testClosesOnErrors_copyingToByteSinkThatThrows ()
specifier|public
name|void
name|testClosesOnErrors_copyingToByteSinkThatThrows
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
name|TestByteSource
name|okSource
init|=
operator|new
name|TestByteSource
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
try|try
block|{
name|okSource
operator|.
name|copyTo
argument_list|(
operator|new
name|TestByteSink
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
comment|// ensure stream was closed IF it was opened (depends on implementation whether or not it's
comment|// opened at all if sink.newOutputStream() throws).
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
name|TestByteSource
name|failSource
init|=
operator|new
name|TestByteSource
argument_list|(
name|bytes
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
name|TestByteSink
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
DECL|method|testClosesOnErrors_copyingToOutputStreamThatThrows ()
specifier|public
name|void
name|testClosesOnErrors_copyingToOutputStreamThatThrows
parameter_list|()
block|{
name|TestByteSource
name|okSource
init|=
operator|new
name|TestByteSource
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
try|try
block|{
name|OutputStream
name|out
init|=
operator|new
name|TestOutputStream
argument_list|(
name|ByteStreams
operator|.
name|nullOutputStream
argument_list|()
argument_list|,
name|WRITE_THROWS
argument_list|)
decl_stmt|;
name|okSource
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
name|ByteSource
name|b1
init|=
name|ByteSource
operator|.
name|wrap
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|0
block|,
literal|1
block|,
literal|2
block|,
literal|3
block|}
argument_list|)
decl_stmt|;
name|ByteSource
name|b2
init|=
name|ByteSource
operator|.
name|wrap
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|ByteSource
name|b3
init|=
name|ByteSource
operator|.
name|wrap
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|4
block|,
literal|5
block|}
argument_list|)
decl_stmt|;
name|byte
index|[]
name|expected
init|=
block|{
literal|0
block|,
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|}
decl_stmt|;
name|assertArrayEquals
argument_list|(
name|expected
argument_list|,
name|ByteSource
operator|.
name|concat
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|b1
argument_list|,
name|b2
argument_list|,
name|b3
argument_list|)
argument_list|)
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|expected
argument_list|,
name|ByteSource
operator|.
name|concat
argument_list|(
name|b1
argument_list|,
name|b2
argument_list|,
name|b3
argument_list|)
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|expected
argument_list|,
name|ByteSource
operator|.
name|concat
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|b1
argument_list|,
name|b2
argument_list|,
name|b3
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
block|}
block|}
end_class

end_unit

