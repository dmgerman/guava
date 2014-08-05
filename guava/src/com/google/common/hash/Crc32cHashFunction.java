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

begin_comment
comment|/**  * This class generates a CRC32C checksum, defined by RFC 3720, Section 12.1.  * The generator polynomial for this checksum is {@code 0x11EDC6F41}.  *  * @author Kurt Alfred Kluever  */
end_comment

begin_class
DECL|class|Crc32cHashFunction
specifier|final
class|class
name|Crc32cHashFunction
extends|extends
name|AbstractStreamingHashFunction
block|{
annotation|@
name|Override
DECL|method|bits ()
specifier|public
name|int
name|bits
parameter_list|()
block|{
return|return
literal|32
return|;
block|}
annotation|@
name|Override
DECL|method|newHasher ()
specifier|public
name|Hasher
name|newHasher
parameter_list|()
block|{
return|return
operator|new
name|Crc32cHasher
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Hashing.crc32c()"
return|;
block|}
DECL|class|Crc32cHasher
specifier|static
specifier|final
class|class
name|Crc32cHasher
extends|extends
name|AbstractByteHasher
block|{
comment|// The CRC table, generated from the polynomial 0x11EDC6F41.
DECL|field|CRC_TABLE
specifier|static
specifier|final
name|int
index|[]
name|CRC_TABLE
init|=
block|{
literal|0x00000000
block|,
literal|0xf26b8303
block|,
literal|0xe13b70f7
block|,
literal|0x1350f3f4
block|,
literal|0xc79a971f
block|,
literal|0x35f1141c
block|,
literal|0x26a1e7e8
block|,
literal|0xd4ca64eb
block|,
literal|0x8ad958cf
block|,
literal|0x78b2dbcc
block|,
literal|0x6be22838
block|,
literal|0x9989ab3b
block|,
literal|0x4d43cfd0
block|,
literal|0xbf284cd3
block|,
literal|0xac78bf27
block|,
literal|0x5e133c24
block|,
literal|0x105ec76f
block|,
literal|0xe235446c
block|,
literal|0xf165b798
block|,
literal|0x030e349b
block|,
literal|0xd7c45070
block|,
literal|0x25afd373
block|,
literal|0x36ff2087
block|,
literal|0xc494a384
block|,
literal|0x9a879fa0
block|,
literal|0x68ec1ca3
block|,
literal|0x7bbcef57
block|,
literal|0x89d76c54
block|,
literal|0x5d1d08bf
block|,
literal|0xaf768bbc
block|,
literal|0xbc267848
block|,
literal|0x4e4dfb4b
block|,
literal|0x20bd8ede
block|,
literal|0xd2d60ddd
block|,
literal|0xc186fe29
block|,
literal|0x33ed7d2a
block|,
literal|0xe72719c1
block|,
literal|0x154c9ac2
block|,
literal|0x061c6936
block|,
literal|0xf477ea35
block|,
literal|0xaa64d611
block|,
literal|0x580f5512
block|,
literal|0x4b5fa6e6
block|,
literal|0xb93425e5
block|,
literal|0x6dfe410e
block|,
literal|0x9f95c20d
block|,
literal|0x8cc531f9
block|,
literal|0x7eaeb2fa
block|,
literal|0x30e349b1
block|,
literal|0xc288cab2
block|,
literal|0xd1d83946
block|,
literal|0x23b3ba45
block|,
literal|0xf779deae
block|,
literal|0x05125dad
block|,
literal|0x1642ae59
block|,
literal|0xe4292d5a
block|,
literal|0xba3a117e
block|,
literal|0x4851927d
block|,
literal|0x5b016189
block|,
literal|0xa96ae28a
block|,
literal|0x7da08661
block|,
literal|0x8fcb0562
block|,
literal|0x9c9bf696
block|,
literal|0x6ef07595
block|,
literal|0x417b1dbc
block|,
literal|0xb3109ebf
block|,
literal|0xa0406d4b
block|,
literal|0x522bee48
block|,
literal|0x86e18aa3
block|,
literal|0x748a09a0
block|,
literal|0x67dafa54
block|,
literal|0x95b17957
block|,
literal|0xcba24573
block|,
literal|0x39c9c670
block|,
literal|0x2a993584
block|,
literal|0xd8f2b687
block|,
literal|0x0c38d26c
block|,
literal|0xfe53516f
block|,
literal|0xed03a29b
block|,
literal|0x1f682198
block|,
literal|0x5125dad3
block|,
literal|0xa34e59d0
block|,
literal|0xb01eaa24
block|,
literal|0x42752927
block|,
literal|0x96bf4dcc
block|,
literal|0x64d4cecf
block|,
literal|0x77843d3b
block|,
literal|0x85efbe38
block|,
literal|0xdbfc821c
block|,
literal|0x2997011f
block|,
literal|0x3ac7f2eb
block|,
literal|0xc8ac71e8
block|,
literal|0x1c661503
block|,
literal|0xee0d9600
block|,
literal|0xfd5d65f4
block|,
literal|0x0f36e6f7
block|,
literal|0x61c69362
block|,
literal|0x93ad1061
block|,
literal|0x80fde395
block|,
literal|0x72966096
block|,
literal|0xa65c047d
block|,
literal|0x5437877e
block|,
literal|0x4767748a
block|,
literal|0xb50cf789
block|,
literal|0xeb1fcbad
block|,
literal|0x197448ae
block|,
literal|0x0a24bb5a
block|,
literal|0xf84f3859
block|,
literal|0x2c855cb2
block|,
literal|0xdeeedfb1
block|,
literal|0xcdbe2c45
block|,
literal|0x3fd5af46
block|,
literal|0x7198540d
block|,
literal|0x83f3d70e
block|,
literal|0x90a324fa
block|,
literal|0x62c8a7f9
block|,
literal|0xb602c312
block|,
literal|0x44694011
block|,
literal|0x5739b3e5
block|,
literal|0xa55230e6
block|,
literal|0xfb410cc2
block|,
literal|0x092a8fc1
block|,
literal|0x1a7a7c35
block|,
literal|0xe811ff36
block|,
literal|0x3cdb9bdd
block|,
literal|0xceb018de
block|,
literal|0xdde0eb2a
block|,
literal|0x2f8b6829
block|,
literal|0x82f63b78
block|,
literal|0x709db87b
block|,
literal|0x63cd4b8f
block|,
literal|0x91a6c88c
block|,
literal|0x456cac67
block|,
literal|0xb7072f64
block|,
literal|0xa457dc90
block|,
literal|0x563c5f93
block|,
literal|0x082f63b7
block|,
literal|0xfa44e0b4
block|,
literal|0xe9141340
block|,
literal|0x1b7f9043
block|,
literal|0xcfb5f4a8
block|,
literal|0x3dde77ab
block|,
literal|0x2e8e845f
block|,
literal|0xdce5075c
block|,
literal|0x92a8fc17
block|,
literal|0x60c37f14
block|,
literal|0x73938ce0
block|,
literal|0x81f80fe3
block|,
literal|0x55326b08
block|,
literal|0xa759e80b
block|,
literal|0xb4091bff
block|,
literal|0x466298fc
block|,
literal|0x1871a4d8
block|,
literal|0xea1a27db
block|,
literal|0xf94ad42f
block|,
literal|0x0b21572c
block|,
literal|0xdfeb33c7
block|,
literal|0x2d80b0c4
block|,
literal|0x3ed04330
block|,
literal|0xccbbc033
block|,
literal|0xa24bb5a6
block|,
literal|0x502036a5
block|,
literal|0x4370c551
block|,
literal|0xb11b4652
block|,
literal|0x65d122b9
block|,
literal|0x97baa1ba
block|,
literal|0x84ea524e
block|,
literal|0x7681d14d
block|,
literal|0x2892ed69
block|,
literal|0xdaf96e6a
block|,
literal|0xc9a99d9e
block|,
literal|0x3bc21e9d
block|,
literal|0xef087a76
block|,
literal|0x1d63f975
block|,
literal|0x0e330a81
block|,
literal|0xfc588982
block|,
literal|0xb21572c9
block|,
literal|0x407ef1ca
block|,
literal|0x532e023e
block|,
literal|0xa145813d
block|,
literal|0x758fe5d6
block|,
literal|0x87e466d5
block|,
literal|0x94b49521
block|,
literal|0x66df1622
block|,
literal|0x38cc2a06
block|,
literal|0xcaa7a905
block|,
literal|0xd9f75af1
block|,
literal|0x2b9cd9f2
block|,
literal|0xff56bd19
block|,
literal|0x0d3d3e1a
block|,
literal|0x1e6dcdee
block|,
literal|0xec064eed
block|,
literal|0xc38d26c4
block|,
literal|0x31e6a5c7
block|,
literal|0x22b65633
block|,
literal|0xd0ddd530
block|,
literal|0x0417b1db
block|,
literal|0xf67c32d8
block|,
literal|0xe52cc12c
block|,
literal|0x1747422f
block|,
literal|0x49547e0b
block|,
literal|0xbb3ffd08
block|,
literal|0xa86f0efc
block|,
literal|0x5a048dff
block|,
literal|0x8ecee914
block|,
literal|0x7ca56a17
block|,
literal|0x6ff599e3
block|,
literal|0x9d9e1ae0
block|,
literal|0xd3d3e1ab
block|,
literal|0x21b862a8
block|,
literal|0x32e8915c
block|,
literal|0xc083125f
block|,
literal|0x144976b4
block|,
literal|0xe622f5b7
block|,
literal|0xf5720643
block|,
literal|0x07198540
block|,
literal|0x590ab964
block|,
literal|0xab613a67
block|,
literal|0xb831c993
block|,
literal|0x4a5a4a90
block|,
literal|0x9e902e7b
block|,
literal|0x6cfbad78
block|,
literal|0x7fab5e8c
block|,
literal|0x8dc0dd8f
block|,
literal|0xe330a81a
block|,
literal|0x115b2b19
block|,
literal|0x020bd8ed
block|,
literal|0xf0605bee
block|,
literal|0x24aa3f05
block|,
literal|0xd6c1bc06
block|,
literal|0xc5914ff2
block|,
literal|0x37faccf1
block|,
literal|0x69e9f0d5
block|,
literal|0x9b8273d6
block|,
literal|0x88d28022
block|,
literal|0x7ab90321
block|,
literal|0xae7367ca
block|,
literal|0x5c18e4c9
block|,
literal|0x4f48173d
block|,
literal|0xbd23943e
block|,
literal|0xf36e6f75
block|,
literal|0x0105ec76
block|,
literal|0x12551f82
block|,
literal|0xe03e9c81
block|,
literal|0x34f4f86a
block|,
literal|0xc69f7b69
block|,
literal|0xd5cf889d
block|,
literal|0x27a40b9e
block|,
literal|0x79b737ba
block|,
literal|0x8bdcb4b9
block|,
literal|0x988c474d
block|,
literal|0x6ae7c44e
block|,
literal|0xbe2da0a5
block|,
literal|0x4c4623a6
block|,
literal|0x5f16d052
block|,
literal|0xad7d5351
block|}
decl_stmt|;
DECL|field|crc
specifier|private
name|int
name|crc
init|=
literal|0
decl_stmt|;
annotation|@
name|Override
DECL|method|update (byte b)
specifier|public
name|void
name|update
parameter_list|(
name|byte
name|b
parameter_list|)
block|{
name|crc
operator|^=
literal|0xFFFFFFFF
expr_stmt|;
comment|// See Hacker's Delight 2nd Edition, Figure 14-7.
name|crc
operator|=
operator|~
operator|(
operator|(
name|crc
operator|>>>
literal|8
operator|)
operator|^
name|CRC_TABLE
index|[
operator|(
name|crc
operator|^
name|b
operator|)
operator|&
literal|0xFF
index|]
operator|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hash ()
specifier|public
name|HashCode
name|hash
parameter_list|()
block|{
return|return
name|HashCode
operator|.
name|fromInt
argument_list|(
name|crc
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

