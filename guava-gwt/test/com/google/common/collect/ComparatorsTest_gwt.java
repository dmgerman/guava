begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
package|;
end_package

begin_class
DECL|class|ComparatorsTest_gwt
specifier|public
class|class
name|ComparatorsTest_gwt
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
literal|"com.google.common.collect.testModule"
return|;
block|}
DECL|method|testEmptiesFirst ()
specifier|public
name|void
name|testEmptiesFirst
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
name|collect
operator|.
name|ComparatorsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ComparatorsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEmptiesFirst
argument_list|()
expr_stmt|;
block|}
DECL|method|testEmptiesLast ()
specifier|public
name|void
name|testEmptiesLast
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
name|collect
operator|.
name|ComparatorsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ComparatorsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEmptiesLast
argument_list|()
expr_stmt|;
block|}
DECL|method|testGreatestCollector ()
specifier|public
name|void
name|testGreatestCollector
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
name|collect
operator|.
name|ComparatorsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ComparatorsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testGreatestCollector
argument_list|()
expr_stmt|;
block|}
DECL|method|testIsInOrder ()
specifier|public
name|void
name|testIsInOrder
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
name|collect
operator|.
name|ComparatorsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ComparatorsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testIsInOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testIsInStrictOrder ()
specifier|public
name|void
name|testIsInStrictOrder
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
name|collect
operator|.
name|ComparatorsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ComparatorsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testIsInStrictOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testLeastCollector ()
specifier|public
name|void
name|testLeastCollector
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
name|collect
operator|.
name|ComparatorsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ComparatorsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLeastCollector
argument_list|()
expr_stmt|;
block|}
DECL|method|testLexicographical ()
specifier|public
name|void
name|testLexicographical
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
name|collect
operator|.
name|ComparatorsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ComparatorsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLexicographical
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

