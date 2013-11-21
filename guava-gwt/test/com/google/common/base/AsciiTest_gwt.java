begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
package|;
end_package

begin_class
DECL|class|AsciiTest_gwt
specifier|public
class|class
name|AsciiTest_gwt
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
literal|"com.google.common.base.testModule"
return|;
block|}
DECL|method|testCharsIgnored ()
specifier|public
name|void
name|testCharsIgnored
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
name|base
operator|.
name|AsciiTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|AsciiTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCharsIgnored
argument_list|()
expr_stmt|;
block|}
DECL|method|testCharsLower ()
specifier|public
name|void
name|testCharsLower
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
name|base
operator|.
name|AsciiTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|AsciiTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCharsLower
argument_list|()
expr_stmt|;
block|}
DECL|method|testCharsUpper ()
specifier|public
name|void
name|testCharsUpper
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
name|base
operator|.
name|AsciiTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|AsciiTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCharsUpper
argument_list|()
expr_stmt|;
block|}
DECL|method|testToLowerCase ()
specifier|public
name|void
name|testToLowerCase
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
name|base
operator|.
name|AsciiTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|AsciiTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testToLowerCase
argument_list|()
expr_stmt|;
block|}
DECL|method|testToUpperCase ()
specifier|public
name|void
name|testToUpperCase
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
name|base
operator|.
name|AsciiTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|AsciiTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testToUpperCase
argument_list|()
expr_stmt|;
block|}
DECL|method|testTruncate ()
specifier|public
name|void
name|testTruncate
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
name|base
operator|.
name|AsciiTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|AsciiTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testTruncate
argument_list|()
expr_stmt|;
block|}
DECL|method|testTruncateIllegalArguments ()
specifier|public
name|void
name|testTruncateIllegalArguments
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
name|base
operator|.
name|AsciiTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|AsciiTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testTruncateIllegalArguments
argument_list|()
expr_stmt|;
block|}
DECL|method|testTruncateWithCustomTruncationIndicator ()
specifier|public
name|void
name|testTruncateWithCustomTruncationIndicator
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
name|base
operator|.
name|AsciiTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|AsciiTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testTruncateWithCustomTruncationIndicator
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

