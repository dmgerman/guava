begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|hash
operator|.
name|HashTestUtils
operator|.
name|ascii
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
name|hash
operator|.
name|HashTestUtils
operator|.
name|toBytes
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
name|hash
operator|.
name|Hashing
operator|.
name|murmur3_128
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|ByteOrder
operator|.
name|LITTLE_ENDIAN
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
name|Funnels
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
name|HashTestUtils
operator|.
name|HashFn
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
comment|/**  * Tests for Murmur3Hash128.  */
end_comment

begin_class
DECL|class|Murmur3Hash128Test
specifier|public
class|class
name|Murmur3Hash128Test
extends|extends
name|TestCase
block|{
DECL|method|testCompatibilityWithCPlusPlus ()
specifier|public
name|void
name|testCompatibilityWithCPlusPlus
parameter_list|()
block|{
name|assertHash
argument_list|(
literal|0
argument_list|,
name|toBytes
argument_list|(
name|LITTLE_ENDIAN
argument_list|,
literal|0x629942693e10f867L
argument_list|,
literal|0x92db0b82baeb5347L
argument_list|)
argument_list|,
name|ascii
argument_list|(
literal|"hell"
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
literal|1
argument_list|,
name|toBytes
argument_list|(
name|LITTLE_ENDIAN
argument_list|,
literal|0xa78ddff5adae8d10L
argument_list|,
literal|0x128900ef20900135L
argument_list|)
argument_list|,
name|ascii
argument_list|(
literal|"hello"
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
literal|2
argument_list|,
name|toBytes
argument_list|(
name|LITTLE_ENDIAN
argument_list|,
literal|0x8a486b23f422e826L
argument_list|,
literal|0xf962a2c58947765fL
argument_list|)
argument_list|,
name|ascii
argument_list|(
literal|"hello "
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
literal|3
argument_list|,
name|toBytes
argument_list|(
name|LITTLE_ENDIAN
argument_list|,
literal|0x2ea59f466f6bed8cL
argument_list|,
literal|0xc610990acc428a17L
argument_list|)
argument_list|,
name|ascii
argument_list|(
literal|"hello w"
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
literal|4
argument_list|,
name|toBytes
argument_list|(
name|LITTLE_ENDIAN
argument_list|,
literal|0x79f6305a386c572cL
argument_list|,
literal|0x46305aed3483b94eL
argument_list|)
argument_list|,
name|ascii
argument_list|(
literal|"hello wo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
literal|5
argument_list|,
name|toBytes
argument_list|(
name|LITTLE_ENDIAN
argument_list|,
literal|0xc2219d213ec1f1b5L
argument_list|,
literal|0xa1d8e2e0a52785bdL
argument_list|)
argument_list|,
name|ascii
argument_list|(
literal|"hello wor"
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
literal|0
argument_list|,
name|toBytes
argument_list|(
name|LITTLE_ENDIAN
argument_list|,
literal|0xe34bbc7bbc071b6cL
argument_list|,
literal|0x7a433ca9c49a9347L
argument_list|)
argument_list|,
name|ascii
argument_list|(
literal|"The quick brown fox jumps over the lazy dog"
argument_list|)
argument_list|)
expr_stmt|;
name|assertHash
argument_list|(
literal|0
argument_list|,
name|toBytes
argument_list|(
name|LITTLE_ENDIAN
argument_list|,
literal|0x658ca970ff85269aL
argument_list|,
literal|0x43fee3eaa68e5c3eL
argument_list|)
argument_list|,
name|ascii
argument_list|(
literal|"The quick brown fox jumps over the lazy cog"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertHash (int seed, byte[] expectedHash, byte[] input)
specifier|private
specifier|static
name|void
name|assertHash
parameter_list|(
name|int
name|seed
parameter_list|,
name|byte
index|[]
name|expectedHash
parameter_list|,
name|byte
index|[]
name|input
parameter_list|)
block|{
name|byte
index|[]
name|hash
init|=
name|murmur3_128
argument_list|(
name|seed
argument_list|)
operator|.
name|newHasher
argument_list|()
operator|.
name|putBytes
argument_list|(
name|input
argument_list|)
operator|.
name|hash
argument_list|()
operator|.
name|asBytes
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|expectedHash
argument_list|,
name|hash
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testParanoid ()
specifier|public
name|void
name|testParanoid
parameter_list|()
block|{
name|HashFn
name|hf
init|=
operator|new
name|HashFn
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|hash
parameter_list|(
name|byte
index|[]
name|input
parameter_list|,
name|int
name|seed
parameter_list|)
block|{
name|Hasher
name|hasher
init|=
name|murmur3_128
argument_list|(
name|seed
argument_list|)
operator|.
name|newHasher
argument_list|()
decl_stmt|;
name|Funnels
operator|.
name|byteArrayFunnel
argument_list|()
operator|.
name|funnel
argument_list|(
name|input
argument_list|,
name|hasher
argument_list|)
expr_stmt|;
return|return
name|hasher
operator|.
name|hash
argument_list|()
operator|.
name|asBytes
argument_list|()
return|;
block|}
block|}
decl_stmt|;
comment|// the magic number comes from:
comment|// http://code.google.com/p/smhasher/source/browse/trunk/main.cpp, #74
name|HashTestUtils
operator|.
name|verifyHashFunction
argument_list|(
name|hf
argument_list|,
literal|128
argument_list|,
literal|0x6384BA69
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

