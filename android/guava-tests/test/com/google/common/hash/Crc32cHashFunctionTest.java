begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.hash
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
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
name|Charsets
operator|.
name|UTF_8
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
comment|/**  * Unit tests for {@link Crc32c}. Known test values are from RFC 3720, Section B.4.  *  * @author Patrick Costello  * @author Kurt Alfred Kluever  */
end_comment

begin_class
DECL|class|Crc32cHashFunctionTest
specifier|public
class|class
name|Crc32cHashFunctionTest
extends|extends
name|TestCase
block|{
DECL|method|testEmpty ()
specifier|public
name|void
name|testEmpty
parameter_list|()
block|{
name|assertCrc
argument_list|(
literal|0
argument_list|,
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
DECL|method|testZeros ()
specifier|public
name|void
name|testZeros
parameter_list|()
block|{
comment|// Test 32 byte array of 0x00.
name|byte
index|[]
name|zeros
init|=
operator|new
name|byte
index|[
literal|32
index|]
decl_stmt|;
name|assertCrc
argument_list|(
literal|0x8a9136aa
argument_list|,
name|zeros
argument_list|)
expr_stmt|;
block|}
DECL|method|testZeros100 ()
specifier|public
name|void
name|testZeros100
parameter_list|()
block|{
comment|// Test 100 byte array of 0x00.
name|byte
index|[]
name|zeros
init|=
operator|new
name|byte
index|[
literal|100
index|]
decl_stmt|;
name|assertCrc
argument_list|(
literal|0x07cb9ff6
argument_list|,
name|zeros
argument_list|)
expr_stmt|;
block|}
DECL|method|testFull ()
specifier|public
name|void
name|testFull
parameter_list|()
block|{
comment|// Test 32 byte array of 0xFF.
name|byte
index|[]
name|fulls
init|=
operator|new
name|byte
index|[
literal|32
index|]
decl_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|fulls
argument_list|,
operator|(
name|byte
operator|)
literal|0xFF
argument_list|)
expr_stmt|;
name|assertCrc
argument_list|(
literal|0x62a8ab43
argument_list|,
name|fulls
argument_list|)
expr_stmt|;
block|}
DECL|method|testFull100 ()
specifier|public
name|void
name|testFull100
parameter_list|()
block|{
comment|// Test 100 byte array of 0xFF.
name|byte
index|[]
name|fulls
init|=
operator|new
name|byte
index|[
literal|100
index|]
decl_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|fulls
argument_list|,
operator|(
name|byte
operator|)
literal|0xFF
argument_list|)
expr_stmt|;
name|assertCrc
argument_list|(
literal|0xbc753add
argument_list|,
name|fulls
argument_list|)
expr_stmt|;
block|}
DECL|method|testAscending ()
specifier|public
name|void
name|testAscending
parameter_list|()
block|{
comment|// Test 32 byte arrays of ascending.
name|byte
index|[]
name|ascending
init|=
operator|new
name|byte
index|[
literal|32
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|32
condition|;
name|i
operator|++
control|)
block|{
name|ascending
index|[
name|i
index|]
operator|=
operator|(
name|byte
operator|)
name|i
expr_stmt|;
block|}
name|assertCrc
argument_list|(
literal|0x46dd794e
argument_list|,
name|ascending
argument_list|)
expr_stmt|;
block|}
DECL|method|testDescending ()
specifier|public
name|void
name|testDescending
parameter_list|()
block|{
comment|// Test 32 byte arrays of descending.
name|byte
index|[]
name|descending
init|=
operator|new
name|byte
index|[
literal|32
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|32
condition|;
name|i
operator|++
control|)
block|{
name|descending
index|[
name|i
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
literal|31
operator|-
name|i
argument_list|)
expr_stmt|;
block|}
name|assertCrc
argument_list|(
literal|0x113fdb5c
argument_list|,
name|descending
argument_list|)
expr_stmt|;
block|}
DECL|method|testDescending100 ()
specifier|public
name|void
name|testDescending100
parameter_list|()
block|{
comment|// Test 100 byte arrays of descending.
name|byte
index|[]
name|descending
init|=
operator|new
name|byte
index|[
literal|100
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|descending
index|[
name|i
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
literal|99
operator|-
name|i
argument_list|)
expr_stmt|;
block|}
name|assertCrc
argument_list|(
literal|0xd022db97
argument_list|,
name|descending
argument_list|)
expr_stmt|;
block|}
DECL|method|testScsiReadCommand ()
specifier|public
name|void
name|testScsiReadCommand
parameter_list|()
block|{
comment|// Test SCSI read command.
name|byte
index|[]
name|scsiReadCommand
init|=
operator|new
name|byte
index|[]
block|{
literal|0x01
block|,
operator|(
name|byte
operator|)
literal|0xc0
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x14
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x04
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x14
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x18
block|,
literal|0x28
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x02
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|,
literal|0x00
block|}
decl_stmt|;
name|assertCrc
argument_list|(
literal|0xd9963a56
argument_list|,
name|scsiReadCommand
argument_list|)
expr_stmt|;
block|}
comment|// Known values from http://www.evanjones.ca/crc32c.html
DECL|method|testSomeOtherKnownValues ()
specifier|public
name|void
name|testSomeOtherKnownValues
parameter_list|()
block|{
name|assertCrc
argument_list|(
literal|0x22620404
argument_list|,
literal|"The quick brown fox jumps over the lazy dog"
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|assertCrc
argument_list|(
literal|0xE3069283
argument_list|,
literal|"123456789"
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|assertCrc
argument_list|(
literal|0xf3dbd4fe
argument_list|,
literal|"1234567890"
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|assertCrc
argument_list|(
literal|0xBFE92A83
argument_list|,
literal|"23456789"
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Verifies that the crc of an array of byte data matches the expected value.    *    * @param expectedCrc the expected crc value.    * @param data the data to run the checksum on.    */
DECL|method|assertCrc (int expectedCrc, byte[] data)
specifier|private
specifier|static
name|void
name|assertCrc
parameter_list|(
name|int
name|expectedCrc
parameter_list|,
name|byte
index|[]
name|data
parameter_list|)
block|{
name|int
name|actualCrc
init|=
name|Hashing
operator|.
name|crc32c
argument_list|()
operator|.
name|hashBytes
argument_list|(
name|data
argument_list|)
operator|.
name|asInt
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"expected: %08x, actual: %08x"
argument_list|,
name|expectedCrc
argument_list|,
name|actualCrc
argument_list|)
argument_list|,
name|expectedCrc
argument_list|,
name|actualCrc
argument_list|)
expr_stmt|;
block|}
comment|// From RFC 3720, Section 12.1, the polynomial generator is 0x11EDC6F41.
comment|// We calculate the constant below by:
comment|//   1. Omitting the most significant bit (because it's always 1). => 0x1EDC6F41
comment|//   2. Flipping the bits of the constant so we can process a byte at a time. => 0x82F63B78
DECL|field|CRC32C_GENERATOR
specifier|private
specifier|static
specifier|final
name|int
name|CRC32C_GENERATOR
init|=
literal|0x1EDC6F41
decl_stmt|;
comment|// 0x11EDC6F41
DECL|field|CRC32C_GENERATOR_FLIPPED
specifier|private
specifier|static
specifier|final
name|int
name|CRC32C_GENERATOR_FLIPPED
init|=
name|Integer
operator|.
name|reverse
argument_list|(
name|CRC32C_GENERATOR
argument_list|)
decl_stmt|;
DECL|method|testCrc32cLookupTable ()
specifier|public
name|void
name|testCrc32cLookupTable
parameter_list|()
block|{
comment|// See Hacker's Delight 2nd Edition, Figure 14-7.
name|int
index|[]
name|expected
init|=
operator|new
name|int
index|[
literal|256
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expected
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|crc
init|=
name|i
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|7
init|;
name|j
operator|>=
literal|0
condition|;
name|j
operator|--
control|)
block|{
name|int
name|mask
init|=
operator|-
operator|(
name|crc
operator|&
literal|1
operator|)
decl_stmt|;
name|crc
operator|=
operator|(
operator|(
name|crc
operator|>>>
literal|1
operator|)
operator|^
operator|(
name|CRC32C_GENERATOR_FLIPPED
operator|&
name|mask
operator|)
operator|)
expr_stmt|;
block|}
name|expected
index|[
name|i
index|]
operator|=
name|crc
expr_stmt|;
block|}
name|int
index|[]
name|actual
init|=
name|Crc32cHashFunction
operator|.
name|Crc32cHasher
operator|.
name|CRC_TABLE
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Expected: \n"
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|expected
argument_list|)
operator|+
literal|"\nActual:\n"
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|actual
argument_list|)
argument_list|,
name|Arrays
operator|.
name|equals
argument_list|(
name|expected
argument_list|,
name|actual
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

