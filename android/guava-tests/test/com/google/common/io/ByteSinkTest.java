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
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
comment|/**  * Tests for the default implementations of {@code ByteSink} methods.  *  * @author Colin Decker  */
end_comment

begin_class
DECL|class|ByteSinkTest
specifier|public
class|class
name|ByteSinkTest
extends|extends
name|IoTestCase
block|{
DECL|field|bytes
specifier|private
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
DECL|field|sink
specifier|private
name|TestByteSink
name|sink
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
name|sink
operator|=
operator|new
name|TestByteSink
argument_list|()
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
name|OutputStream
name|out
init|=
name|sink
operator|.
name|openBufferedStream
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sink
operator|.
name|wasStreamOpened
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sink
operator|.
name|wasStreamClosed
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|sink
operator|.
name|wasStreamClosed
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|,
name|sink
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testWrite_bytes ()
specifier|public
name|void
name|testWrite_bytes
parameter_list|()
throws|throws
name|IOException
block|{
name|assertArrayEquals
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|,
name|sink
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|sink
operator|.
name|write
argument_list|(
name|bytes
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
DECL|method|testWriteFrom_inputStream ()
specifier|public
name|void
name|testWriteFrom_inputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|ByteArrayInputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|sink
operator|.
name|writeFrom
argument_list|(
name|in
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
DECL|method|testWriteFromStream_doesNotCloseThatStream ()
specifier|public
name|void
name|testWriteFromStream_doesNotCloseThatStream
parameter_list|()
throws|throws
name|IOException
block|{
name|TestInputStream
name|in
init|=
operator|new
name|TestInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
operator|new
name|byte
index|[
literal|10
index|]
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|in
operator|.
name|closed
argument_list|()
argument_list|)
expr_stmt|;
name|sink
operator|.
name|writeFrom
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|in
operator|.
name|closed
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testClosesOnErrors_copyingFromByteSourceThatThrows ()
specifier|public
name|void
name|testClosesOnErrors_copyingFromByteSourceThatThrows
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
name|READ_THROWS
argument_list|,
name|CLOSE_THROWS
argument_list|)
control|)
block|{
name|TestByteSource
name|failSource
init|=
operator|new
name|TestByteSource
argument_list|(
operator|new
name|byte
index|[
literal|10
index|]
argument_list|,
name|option
argument_list|)
decl_stmt|;
name|TestByteSink
name|okSink
init|=
operator|new
name|TestByteSink
argument_list|()
decl_stmt|;
try|try
block|{
name|failSource
operator|.
name|copyTo
argument_list|(
name|okSink
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
block|{}
comment|// ensure stream was closed IF it was opened (depends on implementation whether or not it's
comment|// opened at all if source.newInputStream() throws).
name|assertTrue
argument_list|(
literal|"stream not closed when copying from source with option: "
operator|+
name|option
argument_list|,
operator|!
name|okSink
operator|.
name|wasStreamOpened
argument_list|()
operator|||
name|okSink
operator|.
name|wasStreamClosed
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testClosesOnErrors_whenWriteThrows ()
specifier|public
name|void
name|testClosesOnErrors_whenWriteThrows
parameter_list|()
block|{
name|TestByteSink
name|failSink
init|=
operator|new
name|TestByteSink
argument_list|(
name|WRITE_THROWS
argument_list|)
decl_stmt|;
try|try
block|{
operator|new
name|TestByteSource
argument_list|(
operator|new
name|byte
index|[
literal|10
index|]
argument_list|)
operator|.
name|copyTo
argument_list|(
name|failSink
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
name|failSink
operator|.
name|wasStreamClosed
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testClosesOnErrors_writingFromInputStreamThatThrows ()
specifier|public
name|void
name|testClosesOnErrors_writingFromInputStreamThatThrows
parameter_list|()
block|{
name|TestByteSink
name|okSink
init|=
operator|new
name|TestByteSink
argument_list|()
decl_stmt|;
try|try
block|{
name|TestInputStream
name|in
init|=
operator|new
name|TestInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
operator|new
name|byte
index|[
literal|10
index|]
argument_list|)
argument_list|,
name|READ_THROWS
argument_list|)
decl_stmt|;
name|okSink
operator|.
name|writeFrom
argument_list|(
name|in
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
name|okSink
operator|.
name|wasStreamClosed
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
