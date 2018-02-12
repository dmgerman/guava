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
DECL|class|HashBiMapTest_gwt
specifier|public
class|class
name|HashBiMapTest_gwt
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
DECL|method|testBashIt ()
specifier|public
name|void
name|testBashIt
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
name|HashBiMapTest
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
name|HashBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testBashIt
argument_list|()
expr_stmt|;
block|}
DECL|method|testBiMapEntrySetIteratorRemove ()
specifier|public
name|void
name|testBiMapEntrySetIteratorRemove
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
name|HashBiMapTest
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
name|HashBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testBiMapEntrySetIteratorRemove
argument_list|()
expr_stmt|;
block|}
DECL|method|testInsertionOrder ()
specifier|public
name|void
name|testInsertionOrder
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
name|HashBiMapTest
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
name|HashBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testInsertionOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testInsertionOrderAfterForcePut ()
specifier|public
name|void
name|testInsertionOrderAfterForcePut
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
name|HashBiMapTest
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
name|HashBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testInsertionOrderAfterForcePut
argument_list|()
expr_stmt|;
block|}
DECL|method|testInsertionOrderAfterInverseForcePut ()
specifier|public
name|void
name|testInsertionOrderAfterInverseForcePut
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
name|HashBiMapTest
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
name|HashBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testInsertionOrderAfterInverseForcePut
argument_list|()
expr_stmt|;
block|}
DECL|method|testInsertionOrderAfterRemoveFirst ()
specifier|public
name|void
name|testInsertionOrderAfterRemoveFirst
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
name|HashBiMapTest
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
name|HashBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testInsertionOrderAfterRemoveFirst
argument_list|()
expr_stmt|;
block|}
DECL|method|testInsertionOrderAfterRemoveLast ()
specifier|public
name|void
name|testInsertionOrderAfterRemoveLast
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
name|HashBiMapTest
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
name|HashBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testInsertionOrderAfterRemoveLast
argument_list|()
expr_stmt|;
block|}
DECL|method|testInsertionOrderAfterRemoveMiddle ()
specifier|public
name|void
name|testInsertionOrderAfterRemoveMiddle
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
name|HashBiMapTest
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
name|HashBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testInsertionOrderAfterRemoveMiddle
argument_list|()
expr_stmt|;
block|}
DECL|method|testInverseEntrySetValueNewKey ()
specifier|public
name|void
name|testInverseEntrySetValueNewKey
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
name|HashBiMapTest
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
name|HashBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testInverseEntrySetValueNewKey
argument_list|()
expr_stmt|;
block|}
DECL|method|testInverseInsertionOrderAfterInverse ()
specifier|public
name|void
name|testInverseInsertionOrderAfterInverse
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
name|HashBiMapTest
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
name|HashBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testInverseInsertionOrderAfterInverse
argument_list|()
expr_stmt|;
block|}
DECL|method|testInverseInsertionOrderAfterInverseForcePutPresentKey ()
specifier|public
name|void
name|testInverseInsertionOrderAfterInverseForcePutPresentKey
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
name|HashBiMapTest
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
name|HashBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testInverseInsertionOrderAfterInverseForcePutPresentKey
argument_list|()
expr_stmt|;
block|}
DECL|method|testMapConstructor ()
specifier|public
name|void
name|testMapConstructor
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
name|HashBiMapTest
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
name|HashBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testMapConstructor
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

