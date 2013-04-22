begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.escape
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|escape
package|;
end_package

begin_class
DECL|class|ArrayBasedUnicodeEscaperTest_gwt
specifier|public
class|class
name|ArrayBasedUnicodeEscaperTest_gwt
extends|extends
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|junit
operator|.
name|client
operator|.
name|GWTTestCase
block|{
DECL|method|getModuleName ()
annotation|@
name|Override
specifier|public
name|String
name|getModuleName
parameter_list|()
block|{
return|return
literal|"com.google.common.escape.testModule"
return|;
block|}
DECL|method|testCodePointsFromSurrogatePairs ()
specifier|public
name|void
name|testCodePointsFromSurrogatePairs
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|escape
operator|.
name|ArrayBasedUnicodeEscaperTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|escape
operator|.
name|ArrayBasedUnicodeEscaperTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCodePointsFromSurrogatePairs
argument_list|()
expr_stmt|;
block|}
DECL|method|testDeleteUnsafeChars ()
specifier|public
name|void
name|testDeleteUnsafeChars
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|escape
operator|.
name|ArrayBasedUnicodeEscaperTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|escape
operator|.
name|ArrayBasedUnicodeEscaperTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testDeleteUnsafeChars
argument_list|()
expr_stmt|;
block|}
DECL|method|testReplacementPriority ()
specifier|public
name|void
name|testReplacementPriority
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|escape
operator|.
name|ArrayBasedUnicodeEscaperTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|escape
operator|.
name|ArrayBasedUnicodeEscaperTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testReplacementPriority
argument_list|()
expr_stmt|;
block|}
DECL|method|testReplacements ()
specifier|public
name|void
name|testReplacements
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|escape
operator|.
name|ArrayBasedUnicodeEscaperTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|escape
operator|.
name|ArrayBasedUnicodeEscaperTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testReplacements
argument_list|()
expr_stmt|;
block|}
DECL|method|testSafeRange ()
specifier|public
name|void
name|testSafeRange
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|escape
operator|.
name|ArrayBasedUnicodeEscaperTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|escape
operator|.
name|ArrayBasedUnicodeEscaperTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testSafeRange
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

