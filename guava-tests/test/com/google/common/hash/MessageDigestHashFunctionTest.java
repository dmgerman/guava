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
DECL|method|testMd5Hashing ()
specifier|public
name|void
name|testMd5Hashing
parameter_list|()
throws|throws
name|Exception
block|{
name|assertMessageDigestHashing
argument_list|(
name|HashTestUtils
operator|.
name|ascii
argument_list|(
literal|""
argument_list|)
argument_list|,
literal|"MD5"
argument_list|)
expr_stmt|;
name|assertMessageDigestHashing
argument_list|(
name|HashTestUtils
operator|.
name|ascii
argument_list|(
literal|"Z"
argument_list|)
argument_list|,
literal|"MD5"
argument_list|)
expr_stmt|;
name|assertMessageDigestHashing
argument_list|(
name|HashTestUtils
operator|.
name|ascii
argument_list|(
literal|"foobar"
argument_list|)
argument_list|,
literal|"MD5"
argument_list|)
expr_stmt|;
block|}
DECL|method|testSha1Hashing ()
specifier|public
name|void
name|testSha1Hashing
parameter_list|()
throws|throws
name|Exception
block|{
name|assertMessageDigestHashing
argument_list|(
name|HashTestUtils
operator|.
name|ascii
argument_list|(
literal|""
argument_list|)
argument_list|,
literal|"SHA1"
argument_list|)
expr_stmt|;
name|assertMessageDigestHashing
argument_list|(
name|HashTestUtils
operator|.
name|ascii
argument_list|(
literal|"Z"
argument_list|)
argument_list|,
literal|"SHA1"
argument_list|)
expr_stmt|;
name|assertMessageDigestHashing
argument_list|(
name|HashTestUtils
operator|.
name|ascii
argument_list|(
literal|"foobar"
argument_list|)
argument_list|,
literal|"SHA1"
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
throws|throws
name|NoSuchAlgorithmException
block|{
name|assertEquals
argument_list|(
name|HashCodes
operator|.
name|fromBytes
argument_list|(
name|MessageDigest
operator|.
name|getInstance
argument_list|(
name|algorithmName
argument_list|)
operator|.
name|digest
argument_list|(
name|input
argument_list|)
argument_list|)
argument_list|,
operator|new
name|MessageDigestHashFunction
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
block|}
block|}
end_class

end_unit

