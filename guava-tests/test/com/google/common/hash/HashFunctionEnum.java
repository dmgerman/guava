begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * An enum that contains all of the known hash functions.  *  * @author Kurt Alfred Kluever  */
end_comment

begin_enum
DECL|enum|HashFunctionEnum
enum|enum
name|HashFunctionEnum
block|{
DECL|enumConstant|ADLER32
DECL|enumConstant|Hashing.adler32
name|ADLER32
argument_list|(
name|Hashing
operator|.
name|adler32
argument_list|()
argument_list|)
block|,
DECL|enumConstant|CRC32
DECL|enumConstant|Hashing.crc32
name|CRC32
argument_list|(
name|Hashing
operator|.
name|crc32
argument_list|()
argument_list|)
block|,
DECL|enumConstant|GOOD_FAST_HASH_32
DECL|enumConstant|Hashing.goodFastHash
name|GOOD_FAST_HASH_32
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|32
argument_list|)
argument_list|)
block|,
DECL|enumConstant|GOOD_FAST_HASH_64
DECL|enumConstant|Hashing.goodFastHash
name|GOOD_FAST_HASH_64
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|64
argument_list|)
argument_list|)
block|,
DECL|enumConstant|GOOD_FAST_HASH_128
DECL|enumConstant|Hashing.goodFastHash
name|GOOD_FAST_HASH_128
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|128
argument_list|)
argument_list|)
block|,
DECL|enumConstant|GOOD_FAST_HASH_256
DECL|enumConstant|Hashing.goodFastHash
name|GOOD_FAST_HASH_256
argument_list|(
name|Hashing
operator|.
name|goodFastHash
argument_list|(
literal|256
argument_list|)
argument_list|)
block|,
DECL|enumConstant|MD5
DECL|enumConstant|Hashing.md5
name|MD5
argument_list|(
name|Hashing
operator|.
name|md5
argument_list|()
argument_list|)
block|,
DECL|enumConstant|MURMUR3_128
DECL|enumConstant|Hashing.murmur3_128
name|MURMUR3_128
argument_list|(
name|Hashing
operator|.
name|murmur3_128
argument_list|()
argument_list|)
block|,
DECL|enumConstant|MURMUR3_32
DECL|enumConstant|Hashing.murmur3_32
name|MURMUR3_32
argument_list|(
name|Hashing
operator|.
name|murmur3_32
argument_list|()
argument_list|)
block|,
DECL|enumConstant|SHA1
DECL|enumConstant|Hashing.sha1
name|SHA1
argument_list|(
name|Hashing
operator|.
name|sha1
argument_list|()
argument_list|)
block|,
DECL|enumConstant|SHA256
DECL|enumConstant|Hashing.sha256
name|SHA256
argument_list|(
name|Hashing
operator|.
name|sha256
argument_list|()
argument_list|)
block|,
DECL|enumConstant|SHA512
DECL|enumConstant|Hashing.sha512
name|SHA512
argument_list|(
name|Hashing
operator|.
name|sha512
argument_list|()
argument_list|)
block|,   ;
DECL|field|hashFunction
specifier|private
specifier|final
name|HashFunction
name|hashFunction
decl_stmt|;
DECL|method|HashFunctionEnum (HashFunction hashFunction)
specifier|private
name|HashFunctionEnum
parameter_list|(
name|HashFunction
name|hashFunction
parameter_list|)
block|{
name|this
operator|.
name|hashFunction
operator|=
name|hashFunction
expr_stmt|;
block|}
DECL|method|getHashFunction ()
name|HashFunction
name|getHashFunction
parameter_list|()
block|{
return|return
name|hashFunction
return|;
block|}
block|}
end_enum

end_unit

