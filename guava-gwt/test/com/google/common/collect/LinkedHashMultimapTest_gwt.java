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
DECL|class|LinkedHashMultimapTest_gwt
specifier|public
class|class
name|LinkedHashMultimapTest_gwt
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
DECL|method|testAsMapEntriesEquals ()
specifier|public
name|void
name|testAsMapEntriesEquals
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testAsMapEntriesEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testAsMapEquals ()
specifier|public
name|void
name|testAsMapEquals
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testAsMapEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testAsMapValues ()
specifier|public
name|void
name|testAsMapValues
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testAsMapValues
argument_list|()
expr_stmt|;
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testCreate
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreateFromIllegalSizes ()
specifier|public
name|void
name|testCreateFromIllegalSizes
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testCreateFromIllegalSizes
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreateFromMultimap ()
specifier|public
name|void
name|testCreateFromMultimap
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testCreateFromMultimap
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreateFromSizes ()
specifier|public
name|void
name|testCreateFromSizes
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testCreateFromSizes
argument_list|()
expr_stmt|;
block|}
DECL|method|testDuplicates ()
specifier|public
name|void
name|testDuplicates
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testDuplicates
argument_list|()
expr_stmt|;
block|}
DECL|method|testKeysToString_ordering ()
specifier|public
name|void
name|testKeysToString_ordering
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testKeysToString_ordering
argument_list|()
expr_stmt|;
block|}
DECL|method|testOrderingReadOnly ()
specifier|public
name|void
name|testOrderingReadOnly
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testOrderingReadOnly
argument_list|()
expr_stmt|;
block|}
DECL|method|testOrderingSynchronized ()
specifier|public
name|void
name|testOrderingSynchronized
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testOrderingSynchronized
argument_list|()
expr_stmt|;
block|}
DECL|method|testOrderingUnmodifiable ()
specifier|public
name|void
name|testOrderingUnmodifiable
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testOrderingUnmodifiable
argument_list|()
expr_stmt|;
block|}
DECL|method|testOrderingUpdates ()
specifier|public
name|void
name|testOrderingUpdates
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testOrderingUpdates
argument_list|()
expr_stmt|;
block|}
DECL|method|testPutAllReturn_existingElements ()
specifier|public
name|void
name|testPutAllReturn_existingElements
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testPutAllReturn_existingElements
argument_list|()
expr_stmt|;
block|}
DECL|method|testPutMultimapOrdered ()
specifier|public
name|void
name|testPutMultimapOrdered
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testPutMultimapOrdered
argument_list|()
expr_stmt|;
block|}
DECL|method|testPutReturn ()
specifier|public
name|void
name|testPutReturn
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testPutReturn
argument_list|()
expr_stmt|;
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testToString
argument_list|()
expr_stmt|;
block|}
DECL|method|testToStringNullExact ()
specifier|public
name|void
name|testToStringNullExact
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testToStringNullExact
argument_list|()
expr_stmt|;
block|}
DECL|method|testValueSetHashTableExpansion ()
specifier|public
name|void
name|testValueSetHashTableExpansion
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testValueSetHashTableExpansion
argument_list|()
expr_stmt|;
block|}
DECL|method|testValues ()
specifier|public
name|void
name|testValues
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testValues
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
name|LinkedHashMultimapTest
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
name|LinkedHashMultimapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testValuesIteratorRemove
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

