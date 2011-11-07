begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|InputStream
import|;
end_import

begin_comment
comment|/**  * Unit tests for {@link CountingInputStream}.  *  * @author Chris Nokleberg  */
end_comment

begin_class
DECL|class|CountingInputStreamTest
specifier|public
class|class
name|CountingInputStreamTest
extends|extends
name|IoTestCase
block|{
DECL|field|counter
specifier|private
name|CountingInputStream
name|counter
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|counter
operator|=
operator|new
name|CountingInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
operator|new
name|byte
index|[
literal|20
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testReadSingleByte ()
specifier|public
name|void
name|testReadSingleByte
parameter_list|()
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|counter
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testReadArray ()
specifier|public
name|void
name|testReadArray
parameter_list|()
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|counter
operator|.
name|read
argument_list|(
operator|new
name|byte
index|[
literal|10
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testReadArrayRange ()
specifier|public
name|void
name|testReadArrayRange
parameter_list|()
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|counter
operator|.
name|read
argument_list|(
operator|new
name|byte
index|[
literal|10
index|]
argument_list|,
literal|1
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSkip ()
specifier|public
name|void
name|testSkip
parameter_list|()
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|counter
operator|.
name|skip
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSkipEOF ()
specifier|public
name|void
name|testSkipEOF
parameter_list|()
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|counter
operator|.
name|skip
argument_list|(
literal|30
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|counter
operator|.
name|skip
argument_list|(
literal|20
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test reading a single byte while we're in the right state
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|counter
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testReadArrayEOF ()
specifier|public
name|void
name|testReadArrayEOF
parameter_list|()
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|counter
operator|.
name|read
argument_list|(
operator|new
name|byte
index|[
literal|30
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|counter
operator|.
name|read
argument_list|(
operator|new
name|byte
index|[
literal|30
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testMark ()
specifier|public
name|void
name|testMark
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|counter
operator|.
name|markSupported
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|counter
operator|.
name|read
argument_list|(
operator|new
name|byte
index|[
literal|10
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|counter
operator|.
name|mark
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|counter
operator|.
name|read
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|counter
operator|.
name|reset
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|counter
operator|.
name|skip
argument_list|(
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testMarkNotSet ()
specifier|public
name|void
name|testMarkNotSet
parameter_list|()
block|{
try|try
block|{
name|counter
operator|.
name|reset
argument_list|()
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
name|assertEquals
argument_list|(
literal|"Mark not set"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testMarkNotSupported ()
specifier|public
name|void
name|testMarkNotSupported
parameter_list|()
block|{
name|counter
operator|=
operator|new
name|CountingInputStream
argument_list|(
operator|new
name|UnmarkableInputStream
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|counter
operator|.
name|reset
argument_list|()
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
name|assertEquals
argument_list|(
literal|"Mark not supported"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|UnmarkableInputStream
specifier|private
specifier|static
class|class
name|UnmarkableInputStream
extends|extends
name|InputStream
block|{
annotation|@
name|Override
DECL|method|read ()
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|markSupported ()
specifier|public
name|boolean
name|markSupported
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

