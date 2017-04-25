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
name|ByteArrayOutputStream
import|;
end_import

begin_comment
comment|/**  * Unit tests for {@link CountingOutputStream}.  *  * @author Chris Nokleberg  */
end_comment

begin_class
DECL|class|CountingOutputStreamTest
specifier|public
class|class
name|CountingOutputStreamTest
extends|extends
name|IoTestCase
block|{
DECL|method|testCount ()
specifier|public
name|void
name|testCount
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|written
init|=
literal|0
decl_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|CountingOutputStream
name|counter
init|=
operator|new
name|CountingOutputStream
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|written
argument_list|,
name|out
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|written
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|counter
operator|.
name|write
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|written
operator|+=
literal|1
expr_stmt|;
name|assertEquals
argument_list|(
name|written
argument_list|,
name|out
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|written
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|data
init|=
operator|new
name|byte
index|[
literal|10
index|]
decl_stmt|;
name|counter
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|written
operator|+=
literal|10
expr_stmt|;
name|assertEquals
argument_list|(
name|written
argument_list|,
name|out
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|written
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|counter
operator|.
name|write
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|written
operator|+=
literal|5
expr_stmt|;
name|assertEquals
argument_list|(
name|written
argument_list|,
name|out
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|written
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|counter
operator|.
name|write
argument_list|(
name|data
argument_list|,
literal|2
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|written
operator|+=
literal|5
expr_stmt|;
name|assertEquals
argument_list|(
name|written
argument_list|,
name|out
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|written
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test that illegal arguments do not affect count
try|try
block|{
name|counter
operator|.
name|write
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
name|data
operator|.
name|length
operator|+
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|expected
parameter_list|)
block|{     }
name|assertEquals
argument_list|(
name|written
argument_list|,
name|out
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|written
argument_list|,
name|counter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
