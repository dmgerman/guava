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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
operator|.
name|GcFinalization
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|Arrays
import|;
end_import

begin_comment
comment|/**  * Unit tests for {@link FileBackedOutputStream}.  *  * @author Chris Nokleberg  */
end_comment

begin_class
DECL|class|FileBackedOutputStreamTest
specifier|public
class|class
name|FileBackedOutputStreamTest
extends|extends
name|IoTestCase
block|{
DECL|method|testThreshold ()
specifier|public
name|void
name|testThreshold
parameter_list|()
throws|throws
name|Exception
block|{
name|testThreshold
argument_list|(
literal|0
argument_list|,
literal|100
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|testThreshold
argument_list|(
literal|10
argument_list|,
literal|100
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|testThreshold
argument_list|(
literal|100
argument_list|,
literal|100
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|testThreshold
argument_list|(
literal|1000
argument_list|,
literal|100
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|testThreshold
argument_list|(
literal|0
argument_list|,
literal|100
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|testThreshold
argument_list|(
literal|10
argument_list|,
literal|100
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|testThreshold
argument_list|(
literal|100
argument_list|,
literal|100
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|testThreshold
argument_list|(
literal|1000
argument_list|,
literal|100
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testFinalizeDeletesFile ()
specifier|public
name|void
name|testFinalizeDeletesFile
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|data
init|=
name|newPreFilledByteArray
argument_list|(
literal|100
argument_list|)
decl_stmt|;
name|FileBackedOutputStream
name|out
init|=
operator|new
name|FileBackedOutputStream
argument_list|(
literal|0
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|write
argument_list|(
name|out
argument_list|,
name|data
argument_list|,
literal|0
argument_list|,
literal|100
argument_list|,
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|File
name|file
init|=
name|out
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|file
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Make sure that finalize deletes the file
name|out
operator|=
literal|null
expr_stmt|;
comment|// times out and throws RuntimeException on failure
name|GcFinalization
operator|.
name|awaitDone
argument_list|(
operator|new
name|GcFinalization
operator|.
name|FinalizationPredicate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isDone
parameter_list|()
block|{
return|return
operator|!
name|file
operator|.
name|exists
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|testThreshold_resetOnFinalize ()
specifier|public
name|void
name|testThreshold_resetOnFinalize
parameter_list|()
throws|throws
name|Exception
block|{
name|testThreshold
argument_list|(
literal|0
argument_list|,
literal|100
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|testThreshold
argument_list|(
literal|10
argument_list|,
literal|100
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|testThreshold
argument_list|(
literal|100
argument_list|,
literal|100
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|testThreshold
argument_list|(
literal|1000
argument_list|,
literal|100
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|testThreshold
argument_list|(
literal|0
argument_list|,
literal|100
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|testThreshold
argument_list|(
literal|10
argument_list|,
literal|100
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|testThreshold
argument_list|(
literal|100
argument_list|,
literal|100
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|testThreshold
argument_list|(
literal|1000
argument_list|,
literal|100
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testThreshold ( int fileThreshold, int dataSize, boolean singleByte, boolean resetOnFinalize)
specifier|private
name|void
name|testThreshold
parameter_list|(
name|int
name|fileThreshold
parameter_list|,
name|int
name|dataSize
parameter_list|,
name|boolean
name|singleByte
parameter_list|,
name|boolean
name|resetOnFinalize
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|data
init|=
name|newPreFilledByteArray
argument_list|(
name|dataSize
argument_list|)
decl_stmt|;
name|FileBackedOutputStream
name|out
init|=
operator|new
name|FileBackedOutputStream
argument_list|(
name|fileThreshold
argument_list|,
name|resetOnFinalize
argument_list|)
decl_stmt|;
name|ByteSource
name|source
init|=
name|out
operator|.
name|asByteSource
argument_list|()
decl_stmt|;
name|int
name|chunk1
init|=
name|Math
operator|.
name|min
argument_list|(
name|dataSize
argument_list|,
name|fileThreshold
argument_list|)
decl_stmt|;
name|int
name|chunk2
init|=
name|dataSize
operator|-
name|chunk1
decl_stmt|;
comment|// Write just enough to not trip the threshold
if|if
condition|(
name|chunk1
operator|>
literal|0
condition|)
block|{
name|write
argument_list|(
name|out
argument_list|,
name|data
argument_list|,
literal|0
argument_list|,
name|chunk1
argument_list|,
name|singleByte
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ByteSource
operator|.
name|wrap
argument_list|(
name|data
argument_list|)
operator|.
name|slice
argument_list|(
literal|0
argument_list|,
name|chunk1
argument_list|)
operator|.
name|contentEquals
argument_list|(
name|source
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|File
name|file
init|=
name|out
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|file
argument_list|)
expr_stmt|;
comment|// Write data to go over the threshold
if|if
condition|(
name|chunk2
operator|>
literal|0
condition|)
block|{
name|write
argument_list|(
name|out
argument_list|,
name|data
argument_list|,
name|chunk1
argument_list|,
name|chunk2
argument_list|,
name|singleByte
argument_list|)
expr_stmt|;
name|file
operator|=
name|out
operator|.
name|getFile
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|dataSize
argument_list|,
name|file
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Check that source returns the right data
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|data
argument_list|,
name|source
operator|.
name|read
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Make sure that reset deleted the file
name|out
operator|.
name|reset
argument_list|()
expr_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|assertFalse
argument_list|(
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|write (OutputStream out, byte[] b, int off, int len, boolean singleByte)
specifier|private
specifier|static
name|void
name|write
parameter_list|(
name|OutputStream
name|out
parameter_list|,
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|,
name|boolean
name|singleByte
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|singleByte
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
name|off
init|;
name|i
operator|<
name|off
operator|+
name|len
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|write
argument_list|(
name|b
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|out
operator|.
name|write
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
comment|// for coverage
block|}
comment|// TODO(chrisn): only works if we ensure we have crossed file threshold
DECL|method|testWriteErrorAfterClose ()
specifier|public
name|void
name|testWriteErrorAfterClose
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|data
init|=
name|newPreFilledByteArray
argument_list|(
literal|100
argument_list|)
decl_stmt|;
name|FileBackedOutputStream
name|out
init|=
operator|new
name|FileBackedOutputStream
argument_list|(
literal|50
argument_list|)
decl_stmt|;
name|ByteSource
name|source
init|=
name|out
operator|.
name|asByteSource
argument_list|()
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|data
argument_list|,
name|source
operator|.
name|read
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
try|try
block|{
name|out
operator|.
name|write
argument_list|(
literal|42
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
name|IOException
name|expected
parameter_list|)
block|{     }
comment|// Verify that write had no effect
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|data
argument_list|,
name|source
operator|.
name|read
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
DECL|method|testReset ()
specifier|public
name|void
name|testReset
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|data
init|=
name|newPreFilledByteArray
argument_list|(
literal|100
argument_list|)
decl_stmt|;
name|FileBackedOutputStream
name|out
init|=
operator|new
name|FileBackedOutputStream
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
decl_stmt|;
name|ByteSource
name|source
init|=
name|out
operator|.
name|asByteSource
argument_list|()
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|data
argument_list|,
name|source
operator|.
name|read
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|reset
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|,
name|source
operator|.
name|read
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|data
argument_list|,
name|source
operator|.
name|read
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

