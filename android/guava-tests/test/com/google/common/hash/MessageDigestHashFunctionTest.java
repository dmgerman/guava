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
name|collect
operator|.
name|ImmutableMap
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
name|collect
operator|.
name|ImmutableSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|MessageDigest
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
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
comment|/**  * Tests for the MessageDigestHashFunction.  *  * @author Kurt Alfred Kluever  */
end_comment

begin_class
DECL|class|MessageDigestHashFunctionTest
specifier|public
class|class
name|MessageDigestHashFunctionTest
extends|extends
name|TestCase
block|{
DECL|field|INPUTS
specifier|private
specifier|static
specifier|final
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|INPUTS
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|""
argument_list|,
literal|"Z"
argument_list|,
literal|"foobar"
argument_list|)
decl_stmt|;
comment|// From "How Provider Implementations Are Requested and Supplied" from
comment|// http://docs.oracle.com/javase/6/docs/technotes/guides/security/crypto/CryptoSpec.html
comment|//  - Some providers may choose to also include alias names.
comment|//  - For example, the "SHA-1" algorithm might be referred to as "SHA1".
comment|//  - The algorithm name is not case-sensitive.
DECL|field|ALGORITHMS
specifier|private
specifier|static
specifier|final
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|HashFunction
argument_list|>
name|ALGORITHMS
init|=
operator|new
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|String
argument_list|,
name|HashFunction
argument_list|>
argument_list|()
operator|.
name|put
argument_list|(
literal|"MD5"
argument_list|,
name|Hashing
operator|.
name|md5
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
literal|"SHA"
argument_list|,
name|Hashing
operator|.
name|sha1
argument_list|()
argument_list|)
comment|// Not the official name, but still works
operator|.
name|put
argument_list|(
literal|"SHA1"
argument_list|,
name|Hashing
operator|.
name|sha1
argument_list|()
argument_list|)
comment|// Not the official name, but still works
operator|.
name|put
argument_list|(
literal|"sHa-1"
argument_list|,
name|Hashing
operator|.
name|sha1
argument_list|()
argument_list|)
comment|// Not the official name, but still works
operator|.
name|put
argument_list|(
literal|"SHA-1"
argument_list|,
name|Hashing
operator|.
name|sha1
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
literal|"SHA-256"
argument_list|,
name|Hashing
operator|.
name|sha256
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
literal|"SHA-384"
argument_list|,
name|Hashing
operator|.
name|sha384
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
literal|"SHA-512"
argument_list|,
name|Hashing
operator|.
name|sha512
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
DECL|method|testHashing ()
specifier|public
name|void
name|testHashing
parameter_list|()
block|{
for|for
control|(
name|String
name|stringToTest
range|:
name|INPUTS
control|)
block|{
for|for
control|(
name|String
name|algorithmToTest
range|:
name|ALGORITHMS
operator|.
name|keySet
argument_list|()
control|)
block|{
name|assertMessageDigestHashing
argument_list|(
name|HashTestUtils
operator|.
name|ascii
argument_list|(
name|stringToTest
argument_list|)
argument_list|,
name|algorithmToTest
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testPutAfterHash ()
specifier|public
name|void
name|testPutAfterHash
parameter_list|()
block|{
name|Hasher
name|sha1
init|=
name|Hashing
operator|.
name|sha1
argument_list|()
operator|.
name|newHasher
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"2fd4e1c67a2d28fced849ee1bb76e7391b93eb12"
argument_list|,
name|sha1
operator|.
name|putString
argument_list|(
literal|"The quick brown fox jumps over the lazy dog"
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
operator|.
name|hash
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|sha1
operator|.
name|putInt
argument_list|(
literal|42
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testHashTwice ()
specifier|public
name|void
name|testHashTwice
parameter_list|()
block|{
name|Hasher
name|sha1
init|=
name|Hashing
operator|.
name|sha1
argument_list|()
operator|.
name|newHasher
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"2fd4e1c67a2d28fced849ee1bb76e7391b93eb12"
argument_list|,
name|sha1
operator|.
name|putString
argument_list|(
literal|"The quick brown fox jumps over the lazy dog"
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
operator|.
name|hash
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|sha1
operator|.
name|hash
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Hashing.md5()"
argument_list|,
name|Hashing
operator|.
name|md5
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hashing.sha1()"
argument_list|,
name|Hashing
operator|.
name|sha1
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hashing.sha256()"
argument_list|,
name|Hashing
operator|.
name|sha256
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hashing.sha512()"
argument_list|,
name|Hashing
operator|.
name|sha512
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertMessageDigestHashing (byte[] input, String algorithmName)
specifier|private
specifier|static
name|void
name|assertMessageDigestHashing
parameter_list|(
name|byte
index|[]
name|input
parameter_list|,
name|String
name|algorithmName
parameter_list|)
block|{
try|try
block|{
name|MessageDigest
name|digest
init|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
name|algorithmName
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HashCode
operator|.
name|fromBytes
argument_list|(
name|digest
operator|.
name|digest
argument_list|(
name|input
argument_list|)
argument_list|)
argument_list|,
name|ALGORITHMS
operator|.
name|get
argument_list|(
name|algorithmName
argument_list|)
operator|.
name|hashBytes
argument_list|(
name|input
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|bytes
init|=
literal|4
init|;
name|bytes
operator|<=
name|digest
operator|.
name|getDigestLength
argument_list|()
condition|;
name|bytes
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|HashCode
operator|.
name|fromBytes
argument_list|(
name|Arrays
operator|.
name|copyOf
argument_list|(
name|digest
operator|.
name|digest
argument_list|(
name|input
argument_list|)
argument_list|,
name|bytes
argument_list|)
argument_list|)
argument_list|,
operator|new
name|MessageDigestHashFunction
argument_list|(
name|algorithmName
argument_list|,
name|bytes
argument_list|,
name|algorithmName
argument_list|)
operator|.
name|hashBytes
argument_list|(
name|input
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|int
name|maxSize
init|=
name|digest
operator|.
name|getDigestLength
argument_list|()
decl_stmt|;
operator|new
name|MessageDigestHashFunction
argument_list|(
name|algorithmName
argument_list|,
name|maxSize
operator|+
literal|1
argument_list|,
name|algorithmName
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{       }
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|nsae
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|nsae
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

