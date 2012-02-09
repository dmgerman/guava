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
name|primitives
operator|.
name|UnsignedBytes
operator|.
name|toInt
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteOrder
import|;
end_import

begin_comment
comment|/**  * See http://smhasher.googlecode.com/svn/trunk/MurmurHash3.cpp  * MurmurHash3_x64_128  *  * @author aappleby@google.com (Austin Appleby)  * @author andreou@google.com (Dimitris Andreou)  */
end_comment

begin_class
DECL|class|Murmur3_128HashFunction
specifier|final
class|class
name|Murmur3_128HashFunction
extends|extends
name|AbstractStreamingHashFunction
implements|implements
name|Serializable
block|{
comment|// TODO(user): when the shortcuts are implemented, update BloomFilterStrategies
DECL|field|seed
specifier|private
specifier|final
name|int
name|seed
decl_stmt|;
DECL|method|Murmur3_128HashFunction (int seed)
name|Murmur3_128HashFunction
parameter_list|(
name|int
name|seed
parameter_list|)
block|{
name|this
operator|.
name|seed
operator|=
name|seed
expr_stmt|;
block|}
DECL|method|bits ()
annotation|@
name|Override
specifier|public
name|int
name|bits
parameter_list|()
block|{
return|return
literal|128
return|;
block|}
DECL|method|newHasher ()
annotation|@
name|Override
specifier|public
name|Hasher
name|newHasher
parameter_list|()
block|{
return|return
operator|new
name|Murmur3_128Hasher
argument_list|(
name|seed
argument_list|)
return|;
block|}
DECL|class|Murmur3_128Hasher
specifier|private
specifier|static
specifier|final
class|class
name|Murmur3_128Hasher
extends|extends
name|AbstractStreamingHasher
block|{
DECL|field|h1
name|long
name|h1
decl_stmt|;
DECL|field|h2
name|long
name|h2
decl_stmt|;
DECL|field|c1
name|long
name|c1
init|=
literal|0x87c37b91114253d5L
decl_stmt|;
DECL|field|c2
name|long
name|c2
init|=
literal|0x4cf5ad432745937fL
decl_stmt|;
DECL|field|len
name|int
name|len
decl_stmt|;
DECL|method|Murmur3_128Hasher (int seed)
name|Murmur3_128Hasher
parameter_list|(
name|int
name|seed
parameter_list|)
block|{
name|super
argument_list|(
literal|16
argument_list|)
expr_stmt|;
name|h1
operator|=
name|seed
expr_stmt|;
name|h2
operator|=
name|seed
expr_stmt|;
block|}
DECL|method|process (ByteBuffer bb)
annotation|@
name|Override
specifier|protected
name|void
name|process
parameter_list|(
name|ByteBuffer
name|bb
parameter_list|)
block|{
name|long
name|k1
init|=
name|bb
operator|.
name|getLong
argument_list|()
decl_stmt|;
name|long
name|k2
init|=
name|bb
operator|.
name|getLong
argument_list|()
decl_stmt|;
name|len
operator|+=
literal|16
expr_stmt|;
name|bmix64
argument_list|(
name|k1
argument_list|,
name|k2
argument_list|)
expr_stmt|;
block|}
DECL|method|bmix64 (long k1, long k2)
specifier|private
name|void
name|bmix64
parameter_list|(
name|long
name|k1
parameter_list|,
name|long
name|k2
parameter_list|)
block|{
name|k1
operator|*=
name|c1
expr_stmt|;
name|k1
operator|=
name|Long
operator|.
name|rotateLeft
argument_list|(
name|k1
argument_list|,
literal|31
argument_list|)
expr_stmt|;
name|k1
operator|*=
name|c2
expr_stmt|;
name|h1
operator|^=
name|k1
expr_stmt|;
name|h1
operator|=
name|Long
operator|.
name|rotateLeft
argument_list|(
name|h1
argument_list|,
literal|27
argument_list|)
expr_stmt|;
name|h1
operator|+=
name|h2
expr_stmt|;
name|h1
operator|=
name|h1
operator|*
literal|5
operator|+
literal|0x52dce729
expr_stmt|;
name|k2
operator|*=
name|c2
expr_stmt|;
name|k2
operator|=
name|Long
operator|.
name|rotateLeft
argument_list|(
name|k2
argument_list|,
literal|33
argument_list|)
expr_stmt|;
name|k2
operator|*=
name|c1
expr_stmt|;
name|h2
operator|^=
name|k2
expr_stmt|;
name|h2
operator|=
name|Long
operator|.
name|rotateLeft
argument_list|(
name|h2
argument_list|,
literal|31
argument_list|)
expr_stmt|;
name|h2
operator|+=
name|h1
expr_stmt|;
name|h2
operator|=
name|h2
operator|*
literal|5
operator|+
literal|0x38495ab5
expr_stmt|;
block|}
DECL|method|processRemaining (ByteBuffer bb)
annotation|@
name|Override
specifier|protected
name|void
name|processRemaining
parameter_list|(
name|ByteBuffer
name|bb
parameter_list|)
block|{
name|long
name|k1
init|=
literal|0
decl_stmt|;
name|long
name|k2
init|=
literal|0
decl_stmt|;
name|len
operator|+=
name|bb
operator|.
name|remaining
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|bb
operator|.
name|remaining
argument_list|()
condition|)
block|{
case|case
literal|15
case|:
name|k2
operator|^=
operator|(
name|long
operator|)
name|toInt
argument_list|(
name|bb
operator|.
name|get
argument_list|(
literal|14
argument_list|)
argument_list|)
operator|<<
literal|48
expr_stmt|;
comment|// fall through
case|case
literal|14
case|:
name|k2
operator|^=
operator|(
name|long
operator|)
name|toInt
argument_list|(
name|bb
operator|.
name|get
argument_list|(
literal|13
argument_list|)
argument_list|)
operator|<<
literal|40
expr_stmt|;
comment|// fall through
case|case
literal|13
case|:
name|k2
operator|^=
operator|(
name|long
operator|)
name|toInt
argument_list|(
name|bb
operator|.
name|get
argument_list|(
literal|12
argument_list|)
argument_list|)
operator|<<
literal|32
expr_stmt|;
comment|// fall through
case|case
literal|12
case|:
name|k2
operator|^=
operator|(
name|long
operator|)
name|toInt
argument_list|(
name|bb
operator|.
name|get
argument_list|(
literal|11
argument_list|)
argument_list|)
operator|<<
literal|24
expr_stmt|;
comment|// fall through
case|case
literal|11
case|:
name|k2
operator|^=
operator|(
name|long
operator|)
name|toInt
argument_list|(
name|bb
operator|.
name|get
argument_list|(
literal|10
argument_list|)
argument_list|)
operator|<<
literal|16
expr_stmt|;
comment|// fall through
case|case
literal|10
case|:
name|k2
operator|^=
operator|(
name|long
operator|)
name|toInt
argument_list|(
name|bb
operator|.
name|get
argument_list|(
literal|9
argument_list|)
argument_list|)
operator|<<
literal|8
expr_stmt|;
comment|// fall through
case|case
literal|9
case|:
name|k2
operator|^=
operator|(
name|long
operator|)
name|toInt
argument_list|(
name|bb
operator|.
name|get
argument_list|(
literal|8
argument_list|)
argument_list|)
operator|<<
literal|0
expr_stmt|;
name|k2
operator|*=
name|c2
expr_stmt|;
name|k2
operator|=
name|Long
operator|.
name|rotateLeft
argument_list|(
name|k2
argument_list|,
literal|33
argument_list|)
expr_stmt|;
name|k2
operator|*=
name|c1
expr_stmt|;
name|h2
operator|^=
name|k2
expr_stmt|;
comment|// fall through
case|case
literal|8
case|:
name|k1
operator|^=
operator|(
name|long
operator|)
name|toInt
argument_list|(
name|bb
operator|.
name|get
argument_list|(
literal|7
argument_list|)
argument_list|)
operator|<<
literal|56
expr_stmt|;
comment|// fall through
case|case
literal|7
case|:
name|k1
operator|^=
operator|(
name|long
operator|)
name|toInt
argument_list|(
name|bb
operator|.
name|get
argument_list|(
literal|6
argument_list|)
argument_list|)
operator|<<
literal|48
expr_stmt|;
comment|// fall through
case|case
literal|6
case|:
name|k1
operator|^=
operator|(
name|long
operator|)
name|toInt
argument_list|(
name|bb
operator|.
name|get
argument_list|(
literal|5
argument_list|)
argument_list|)
operator|<<
literal|40
expr_stmt|;
comment|// fall through
case|case
literal|5
case|:
name|k1
operator|^=
operator|(
name|long
operator|)
name|toInt
argument_list|(
name|bb
operator|.
name|get
argument_list|(
literal|4
argument_list|)
argument_list|)
operator|<<
literal|32
expr_stmt|;
comment|// fall through
case|case
literal|4
case|:
name|k1
operator|^=
operator|(
name|long
operator|)
name|toInt
argument_list|(
name|bb
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
operator|<<
literal|24
expr_stmt|;
comment|// fall through
case|case
literal|3
case|:
name|k1
operator|^=
operator|(
name|long
operator|)
name|toInt
argument_list|(
name|bb
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|<<
literal|16
expr_stmt|;
comment|// fall through
case|case
literal|2
case|:
name|k1
operator|^=
operator|(
name|long
operator|)
name|toInt
argument_list|(
name|bb
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|<<
literal|8
expr_stmt|;
comment|// fall through
case|case
literal|1
case|:
name|k1
operator|^=
operator|(
name|long
operator|)
name|toInt
argument_list|(
name|bb
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|<<
literal|0
expr_stmt|;
name|k1
operator|*=
name|c1
expr_stmt|;
name|k1
operator|=
name|Long
operator|.
name|rotateLeft
argument_list|(
name|k1
argument_list|,
literal|31
argument_list|)
expr_stmt|;
name|k1
operator|*=
name|c2
expr_stmt|;
name|h1
operator|^=
name|k1
expr_stmt|;
comment|// fall through
default|default:
block|}
block|}
DECL|method|makeHash ()
annotation|@
name|Override
specifier|public
name|HashCode
name|makeHash
parameter_list|()
block|{
name|h1
operator|^=
name|len
expr_stmt|;
name|h2
operator|^=
name|len
expr_stmt|;
name|h1
operator|+=
name|h2
expr_stmt|;
name|h2
operator|+=
name|h1
expr_stmt|;
name|h1
operator|=
name|fmix64
argument_list|(
name|h1
argument_list|)
expr_stmt|;
name|h2
operator|=
name|fmix64
argument_list|(
name|h2
argument_list|)
expr_stmt|;
name|h1
operator|+=
name|h2
expr_stmt|;
name|h2
operator|+=
name|h1
expr_stmt|;
name|ByteBuffer
name|bb
init|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
operator|new
name|byte
index|[
literal|16
index|]
argument_list|)
operator|.
name|order
argument_list|(
name|ByteOrder
operator|.
name|LITTLE_ENDIAN
argument_list|)
decl_stmt|;
name|bb
operator|.
name|putLong
argument_list|(
name|h1
argument_list|)
expr_stmt|;
name|bb
operator|.
name|putLong
argument_list|(
name|h2
argument_list|)
expr_stmt|;
return|return
name|HashCodes
operator|.
name|fromBytes
argument_list|(
name|bb
operator|.
name|array
argument_list|()
argument_list|)
return|;
block|}
DECL|method|fmix64 (long k)
specifier|private
name|long
name|fmix64
parameter_list|(
name|long
name|k
parameter_list|)
block|{
name|k
operator|^=
name|k
operator|>>>
literal|33
expr_stmt|;
name|k
operator|*=
literal|0xff51afd7ed558ccdL
expr_stmt|;
name|k
operator|^=
name|k
operator|>>>
literal|33
expr_stmt|;
name|k
operator|*=
literal|0xc4ceb9fe1a85ec53L
expr_stmt|;
name|k
operator|^=
name|k
operator|>>>
literal|33
expr_stmt|;
return|return
name|k
return|;
block|}
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0L
decl_stmt|;
block|}
end_class

end_unit

