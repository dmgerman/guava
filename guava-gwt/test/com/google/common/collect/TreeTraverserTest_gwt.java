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
DECL|class|TreeTraverserTest_gwt
specifier|public
class|class
name|TreeTraverserTest_gwt
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
DECL|method|testBreadthOrder ()
specifier|public
name|void
name|testBreadthOrder
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
name|TreeTraverserTest
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
name|TreeTraverserTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testBreadthOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testInOrder ()
specifier|public
name|void
name|testInOrder
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
name|TreeTraverserTest
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
name|TreeTraverserTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testInOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testPostOrder ()
specifier|public
name|void
name|testPostOrder
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
name|TreeTraverserTest
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
name|TreeTraverserTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testPostOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testPreOrder ()
specifier|public
name|void
name|testPreOrder
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
name|TreeTraverserTest
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
name|TreeTraverserTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testPreOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testUsing ()
specifier|public
name|void
name|testUsing
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
name|TreeTraverserTest
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
name|TreeTraverserTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testUsing
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

