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
DECL|class|EnumBiMapTest_gwt
specifier|public
class|class
name|EnumBiMapTest_gwt
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
DECL|method|testCreate ()
specifier|public
name|void
name|testCreate
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
name|EnumBiMapTest
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
name|EnumBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCreate
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreateFromMap ()
specifier|public
name|void
name|testCreateFromMap
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
name|EnumBiMapTest
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
name|EnumBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCreateFromMap
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySet ()
specifier|public
name|void
name|testEntrySet
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
name|EnumBiMapTest
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
name|EnumBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySet
argument_list|()
expr_stmt|;
block|}
DECL|method|testEnumBiMapConstructor ()
specifier|public
name|void
name|testEnumBiMapConstructor
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
name|EnumBiMapTest
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
name|EnumBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEnumBiMapConstructor
argument_list|()
expr_stmt|;
block|}
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
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
name|EnumBiMapTest
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
name|EnumBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testIterationOrder ()
specifier|public
name|void
name|testIterationOrder
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
name|EnumBiMapTest
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
name|EnumBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testIterationOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testKeySetIteratorRemove ()
specifier|public
name|void
name|testKeySetIteratorRemove
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
name|EnumBiMapTest
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
name|EnumBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testKeySetIteratorRemove
argument_list|()
expr_stmt|;
block|}
DECL|method|testKeyType ()
specifier|public
name|void
name|testKeyType
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
name|EnumBiMapTest
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
name|EnumBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testKeyType
argument_list|()
expr_stmt|;
block|}
DECL|method|testValueType ()
specifier|public
name|void
name|testValueType
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
name|EnumBiMapTest
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
name|EnumBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testValueType
argument_list|()
expr_stmt|;
block|}
DECL|method|testValuesIteratorRemove ()
specifier|public
name|void
name|testValuesIteratorRemove
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
name|EnumBiMapTest
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
name|EnumBiMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testValuesIteratorRemove
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

