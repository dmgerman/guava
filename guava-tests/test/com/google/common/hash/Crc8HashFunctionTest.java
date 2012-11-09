begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * This tests CRC8 module using externally calculated checksums from  * http://www.zorc.breitbandkatze.de/crc.html  * with the ATM HEC paramters:  * CRC-8, Polynomial 0x07, Initial value 0x00, Final XOR value 0x55  * (direct, don't reverse data bytes, don't reverse CRC before final XOR)  *  * @author Nicholas Yu  */
end_comment

begin_class
DECL|class|Crc8HashFunctionTest
specifier|public
class|class
name|Crc8HashFunctionTest
extends|extends
name|TestCase
block|{
DECL|method|testGenerateKnownValues ()
specifier|public
name|void
name|testGenerateKnownValues
parameter_list|()
block|{
name|assertCrc8
argument_list|(
literal|"Google"
argument_list|,
operator|(
name|byte
operator|)
literal|1
argument_list|)
expr_stmt|;
name|assertCrc8
argument_list|(
literal|"GOOGLE"
argument_list|,
operator|(
name|byte
operator|)
operator|-
literal|90
argument_list|)
expr_stmt|;
name|assertCrc8
argument_list|(
literal|"My CRC 8!"
argument_list|,
operator|(
name|byte
operator|)
operator|-
literal|36
argument_list|)
expr_stmt|;
name|assertCrc8
argument_list|(
literal|"Z"
argument_list|,
operator|(
name|byte
operator|)
operator|-
literal|44
argument_list|)
expr_stmt|;
name|assertCrc8
argument_list|(
literal|""
argument_list|,
operator|(
name|byte
operator|)
literal|85
argument_list|)
expr_stmt|;
block|}
DECL|method|assertCrc8 (String value, byte expectedCrc)
specifier|private
specifier|static
name|void
name|assertCrc8
parameter_list|(
name|String
name|value
parameter_list|,
name|byte
name|expectedCrc
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|expectedCrc
argument_list|,
name|Hashing
operator|.
name|crc8
argument_list|()
operator|.
name|hashString
argument_list|(
name|value
argument_list|,
name|Charsets
operator|.
name|US_ASCII
argument_list|)
operator|.
name|asBytes
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

