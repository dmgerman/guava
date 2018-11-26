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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|primitives
operator|.
name|Bytes
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
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|DataInput
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|DataOutputStream
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Test class for {@link LittleEndianDataInputStream}.  *  * @author Chris Nokleberg  */
end_comment

begin_class
DECL|class|LittleEndianDataInputStreamTest
specifier|public
class|class
name|LittleEndianDataInputStreamTest
extends|extends
name|TestCase
block|{
DECL|field|data
specifier|private
name|byte
index|[]
name|data
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|DataOutputStream
name|out
init|=
operator|new
name|DataOutputStream
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|initializeData
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|data
operator|=
name|baos
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
block|}
DECL|method|initializeData (DataOutputStream out)
specifier|private
name|void
name|initializeData
parameter_list|(
name|DataOutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
comment|/* Write out various test values NORMALLY */
name|out
operator|.
name|write
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|-
literal|100
block|,
literal|100
block|}
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeBoolean
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeBoolean
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeByte
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeByte
argument_list|(
operator|-
literal|100
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeByte
argument_list|(
operator|(
name|byte
operator|)
literal|200
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeChar
argument_list|(
literal|'a'
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeShort
argument_list|(
operator|(
name|short
operator|)
operator|-
literal|30000
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeShort
argument_list|(
operator|(
name|short
operator|)
literal|50000
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeInt
argument_list|(
literal|0xCAFEBABE
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeLong
argument_list|(
literal|0xDEADBEEFCAFEBABEL
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeUTF
argument_list|(
literal|"Herby Derby"
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeFloat
argument_list|(
name|Float
operator|.
name|intBitsToFloat
argument_list|(
literal|0xCAFEBABE
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeDouble
argument_list|(
name|Double
operator|.
name|longBitsToDouble
argument_list|(
literal|0xDEADBEEFCAFEBABEL
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testReadFully ()
specifier|public
name|void
name|testReadFully
parameter_list|()
throws|throws
name|IOException
block|{
name|DataInput
name|in
init|=
operator|new
name|LittleEndianDataInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
argument_list|)
decl_stmt|;
name|byte
index|[]
name|b
init|=
operator|new
name|byte
index|[
name|data
operator|.
name|length
index|]
decl_stmt|;
name|in
operator|.
name|readFully
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Bytes
operator|.
name|asList
argument_list|(
name|data
argument_list|)
argument_list|,
name|Bytes
operator|.
name|asList
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testReadUnsignedByte_eof ()
specifier|public
name|void
name|testReadUnsignedByte_eof
parameter_list|()
throws|throws
name|IOException
block|{
name|DataInput
name|in
init|=
operator|new
name|LittleEndianDataInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|in
operator|.
name|readUnsignedByte
argument_list|()
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
DECL|method|testReadUnsignedShort_eof ()
specifier|public
name|void
name|testReadUnsignedShort_eof
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|buf
init|=
block|{
literal|23
block|}
decl_stmt|;
name|DataInput
name|in
init|=
operator|new
name|LittleEndianDataInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|buf
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|in
operator|.
name|readUnsignedShort
argument_list|()
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
DECL|method|testReadLine ()
specifier|public
name|void
name|testReadLine
parameter_list|()
throws|throws
name|IOException
block|{
name|DataInput
name|in
init|=
operator|new
name|LittleEndianDataInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|in
operator|.
name|readLine
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{
name|assertThat
argument_list|(
name|expected
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"readLine is not supported"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testReadLittleEndian ()
specifier|public
name|void
name|testReadLittleEndian
parameter_list|()
throws|throws
name|IOException
block|{
name|DataInput
name|in
init|=
operator|new
name|LittleEndianDataInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
argument_list|)
decl_stmt|;
comment|/* Read in various values in LITTLE ENDIAN FORMAT */
name|byte
index|[]
name|b
init|=
operator|new
name|byte
index|[
literal|2
index|]
decl_stmt|;
name|in
operator|.
name|readFully
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|100
argument_list|,
name|b
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|b
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|in
operator|.
name|readBoolean
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|in
operator|.
name|readBoolean
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|in
operator|.
name|readByte
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|100
argument_list|,
name|in
operator|.
name|readByte
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|in
operator|.
name|readUnsignedByte
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'\u6100'
argument_list|,
name|in
operator|.
name|readChar
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|12150
argument_list|,
name|in
operator|.
name|readShort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20675
argument_list|,
name|in
operator|.
name|readUnsignedShort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0xBEBAFECA
argument_list|,
name|in
operator|.
name|readInt
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0xBEBAFECAEFBEADDEL
argument_list|,
name|in
operator|.
name|readLong
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Herby Derby"
argument_list|,
name|in
operator|.
name|readUTF
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0xBEBAFECA
argument_list|,
name|Float
operator|.
name|floatToIntBits
argument_list|(
name|in
operator|.
name|readFloat
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0xBEBAFECAEFBEADDEL
argument_list|,
name|Double
operator|.
name|doubleToLongBits
argument_list|(
name|in
operator|.
name|readDouble
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSkipBytes ()
specifier|public
name|void
name|testSkipBytes
parameter_list|()
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|DataOutputStream
name|out
init|=
operator|new
name|DataOutputStream
argument_list|(
name|baos
argument_list|)
decl_stmt|;
comment|/* Write out various test values NORMALLY */
name|out
operator|.
name|write
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
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|,
literal|9
block|}
argument_list|)
expr_stmt|;
comment|// 10 bytes of junk to skip
name|initializeData
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|byte
index|[]
name|data
init|=
name|baos
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|DataInput
name|in
init|=
operator|new
name|LittleEndianDataInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|bytesSkipped
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|bytesSkipped
operator|<
literal|10
condition|)
block|{
name|bytesSkipped
operator|+=
name|in
operator|.
name|skipBytes
argument_list|(
literal|10
operator|-
name|bytesSkipped
argument_list|)
expr_stmt|;
block|}
comment|/* Read in various values in LITTLE ENDIAN FORMAT */
name|byte
index|[]
name|b
init|=
operator|new
name|byte
index|[
literal|2
index|]
decl_stmt|;
name|in
operator|.
name|readFully
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|100
argument_list|,
name|b
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|b
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|in
operator|.
name|readBoolean
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|in
operator|.
name|readBoolean
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

