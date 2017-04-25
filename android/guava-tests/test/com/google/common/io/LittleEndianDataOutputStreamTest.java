begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|DataInputStream
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
comment|/**  * Test class for {@link LittleEndianDataOutputStream}.  *  * @author Keith Bottner  */
end_comment

begin_class
DECL|class|LittleEndianDataOutputStreamTest
specifier|public
class|class
name|LittleEndianDataOutputStreamTest
extends|extends
name|TestCase
block|{
DECL|field|baos
specifier|private
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
DECL|field|out
specifier|private
name|LittleEndianDataOutputStream
name|out
init|=
operator|new
name|LittleEndianDataOutputStream
argument_list|(
name|baos
argument_list|)
decl_stmt|;
DECL|method|testWriteLittleEndian ()
specifier|public
name|void
name|testWriteLittleEndian
parameter_list|()
throws|throws
name|IOException
block|{
comment|/* Write out various test values in LITTLE ENDIAN FORMAT */
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
name|byte
index|[]
name|data
init|=
name|baos
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
comment|/* Setup input streams */
name|DataInput
name|in
init|=
operator|new
name|DataInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
argument_list|)
decl_stmt|;
comment|/* Read in various values NORMALLY */
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
comment|// testing a deprecated method
DECL|method|testWriteBytes ()
specifier|public
name|void
name|testWriteBytes
parameter_list|()
throws|throws
name|IOException
block|{
comment|/* Write out various test values in LITTLE ENDIAN FORMAT */
name|out
operator|.
name|writeBytes
argument_list|(
literal|"r\u00C9sum\u00C9"
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
comment|/* Setup input streams */
name|DataInput
name|in
init|=
operator|new
name|DataInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
argument_list|)
decl_stmt|;
comment|/* Read in various values NORMALLY */
name|byte
index|[]
name|b
init|=
operator|new
name|byte
index|[
literal|6
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
literal|"r\u00C9sum\u00C9"
operator|.
name|getBytes
argument_list|(
name|Charsets
operator|.
name|ISO_8859_1
argument_list|)
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
comment|// testing a deprecated method
DECL|method|testWriteBytes_discardHighOrderBytes ()
specifier|public
name|void
name|testWriteBytes_discardHighOrderBytes
parameter_list|()
throws|throws
name|IOException
block|{
comment|/* Write out various test values in LITTLE ENDIAN FORMAT */
name|out
operator|.
name|writeBytes
argument_list|(
literal|"\uAAAA\uAABB\uAACC"
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
comment|/* Setup input streams */
name|DataInput
name|in
init|=
operator|new
name|DataInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
argument_list|)
decl_stmt|;
comment|/* Read in various values NORMALLY */
name|byte
index|[]
name|b
init|=
operator|new
name|byte
index|[
literal|3
index|]
decl_stmt|;
name|in
operator|.
name|readFully
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|byte
index|[]
name|expected
init|=
block|{
operator|(
name|byte
operator|)
literal|0xAA
block|,
operator|(
name|byte
operator|)
literal|0xBB
block|,
operator|(
name|byte
operator|)
literal|0xCC
block|}
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
DECL|method|testWriteChars ()
specifier|public
name|void
name|testWriteChars
parameter_list|()
throws|throws
name|IOException
block|{
comment|/* Write out various test values in LITTLE ENDIAN FORMAT */
name|out
operator|.
name|writeChars
argument_list|(
literal|"r\u00C9sum\u00C9"
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
comment|/* Setup input streams */
name|DataInput
name|in
init|=
operator|new
name|DataInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
argument_list|)
decl_stmt|;
comment|/* Read in various values NORMALLY */
name|byte
index|[]
name|actual
init|=
operator|new
name|byte
index|[
literal|12
index|]
decl_stmt|;
name|in
operator|.
name|readFully
argument_list|(
name|actual
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'r'
argument_list|,
name|actual
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|actual
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|0xC9
argument_list|,
name|actual
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|actual
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'s'
argument_list|,
name|actual
index|[
literal|4
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|actual
index|[
literal|5
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'u'
argument_list|,
name|actual
index|[
literal|6
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|actual
index|[
literal|7
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'m'
argument_list|,
name|actual
index|[
literal|8
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|actual
index|[
literal|9
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|0xC9
argument_list|,
name|actual
index|[
literal|10
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|actual
index|[
literal|11
index|]
argument_list|)
expr_stmt|;
block|}
DECL|method|assertEquals (byte[] expected, byte[] actual)
specifier|private
specifier|static
name|void
name|assertEquals
parameter_list|(
name|byte
index|[]
name|expected
parameter_list|,
name|byte
index|[]
name|actual
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|Bytes
operator|.
name|asList
argument_list|(
name|expected
argument_list|)
argument_list|,
name|Bytes
operator|.
name|asList
argument_list|(
name|actual
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
