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
DECL|class|EnumsTest_gwt
specifier|public
class|class
name|EnumsTest_gwt
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
DECL|method|testGetIfPresent ()
specifier|public
name|void
name|testGetIfPresent
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
name|EnumsTest
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
name|EnumsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testGetIfPresent
argument_list|()
expr_stmt|;
block|}
DECL|method|testGetIfPresent_caseSensitive ()
specifier|public
name|void
name|testGetIfPresent_caseSensitive
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
name|EnumsTest
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
name|EnumsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testGetIfPresent_caseSensitive
argument_list|()
expr_stmt|;
block|}
DECL|method|testGetIfPresent_whenNoMatchingConstant ()
specifier|public
name|void
name|testGetIfPresent_whenNoMatchingConstant
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
name|EnumsTest
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
name|EnumsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testGetIfPresent_whenNoMatchingConstant
argument_list|()
expr_stmt|;
block|}
DECL|method|testStringConverter_convert ()
specifier|public
name|void
name|testStringConverter_convert
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
name|EnumsTest
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
name|EnumsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testStringConverter_convert
argument_list|()
expr_stmt|;
block|}
DECL|method|testStringConverter_convertError ()
specifier|public
name|void
name|testStringConverter_convertError
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
name|EnumsTest
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
name|EnumsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testStringConverter_convertError
argument_list|()
expr_stmt|;
block|}
DECL|method|testStringConverter_nullConversions ()
specifier|public
name|void
name|testStringConverter_nullConversions
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
name|EnumsTest
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
name|EnumsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testStringConverter_nullConversions
argument_list|()
expr_stmt|;
block|}
DECL|method|testStringConverter_reverse ()
specifier|public
name|void
name|testStringConverter_reverse
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
name|EnumsTest
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
name|EnumsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testStringConverter_reverse
argument_list|()
expr_stmt|;
block|}
DECL|method|testStringConverter_serialization ()
specifier|public
name|void
name|testStringConverter_serialization
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
name|EnumsTest
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
name|EnumsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testStringConverter_serialization
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

