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
DECL|class|TreeMultisetTest_gwt
specifier|public
class|class
name|TreeMultisetTest_gwt
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
DECL|method|testAddOne ()
specifier|public
name|void
name|testAddOne
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testAddOne
argument_list|()
expr_stmt|;
block|}
DECL|method|testAddSeveralTimes ()
specifier|public
name|void
name|testAddSeveralTimes
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testAddSeveralTimes
argument_list|()
expr_stmt|;
block|}
DECL|method|testClear ()
specifier|public
name|void
name|testClear
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testClear
argument_list|()
expr_stmt|;
block|}
DECL|method|testClearNothing ()
specifier|public
name|void
name|testClearNothing
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testClearNothing
argument_list|()
expr_stmt|;
block|}
DECL|method|testContainsAfterRemoval ()
specifier|public
name|void
name|testContainsAfterRemoval
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testContainsAfterRemoval
argument_list|()
expr_stmt|;
block|}
DECL|method|testContainsAllVacuous ()
specifier|public
name|void
name|testContainsAllVacuous
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testContainsAllVacuous
argument_list|()
expr_stmt|;
block|}
DECL|method|testContainsNo ()
specifier|public
name|void
name|testContainsNo
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testContainsNo
argument_list|()
expr_stmt|;
block|}
DECL|method|testContainsOne ()
specifier|public
name|void
name|testContainsOne
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testContainsOne
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
name|TreeMultisetTest
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
name|TreeMultisetTest
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
DECL|method|testCreateFromIterable ()
specifier|public
name|void
name|testCreateFromIterable
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testCreateFromIterable
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreateWithComparator ()
specifier|public
name|void
name|testCreateWithComparator
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testCreateWithComparator
argument_list|()
expr_stmt|;
block|}
DECL|method|testCustomComparator ()
specifier|public
name|void
name|testCustomComparator
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testCustomComparator
argument_list|()
expr_stmt|;
block|}
DECL|method|testDegenerateComparator ()
specifier|public
name|void
name|testDegenerateComparator
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testDegenerateComparator
argument_list|()
expr_stmt|;
block|}
DECL|method|testElementSetSortedSetMethods ()
specifier|public
name|void
name|testElementSetSortedSetMethods
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testElementSetSortedSetMethods
argument_list|()
expr_stmt|;
block|}
DECL|method|testElementSetSubsetClear ()
specifier|public
name|void
name|testElementSetSubsetClear
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testElementSetSubsetClear
argument_list|()
expr_stmt|;
block|}
DECL|method|testElementSetSubsetRemove ()
specifier|public
name|void
name|testElementSetSubsetRemove
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testElementSetSubsetRemove
argument_list|()
expr_stmt|;
block|}
DECL|method|testElementSetSubsetRemoveAll ()
specifier|public
name|void
name|testElementSetSubsetRemoveAll
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testElementSetSubsetRemoveAll
argument_list|()
expr_stmt|;
block|}
DECL|method|testElementSetSubsetRetainAll ()
specifier|public
name|void
name|testElementSetSubsetRetainAll
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testElementSetSubsetRetainAll
argument_list|()
expr_stmt|;
block|}
DECL|method|testEqualsDifferentTypes ()
specifier|public
name|void
name|testEqualsDifferentTypes
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testEqualsDifferentTypes
argument_list|()
expr_stmt|;
block|}
DECL|method|testEqualsNo ()
specifier|public
name|void
name|testEqualsNo
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testEqualsNo
argument_list|()
expr_stmt|;
block|}
DECL|method|testEqualsPartial ()
specifier|public
name|void
name|testEqualsPartial
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testEqualsPartial
argument_list|()
expr_stmt|;
block|}
DECL|method|testEqualsSelf ()
specifier|public
name|void
name|testEqualsSelf
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testEqualsSelf
argument_list|()
expr_stmt|;
block|}
DECL|method|testEqualsTricky ()
specifier|public
name|void
name|testEqualsTricky
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testEqualsTricky
argument_list|()
expr_stmt|;
block|}
DECL|method|testEqualsYes ()
specifier|public
name|void
name|testEqualsYes
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testEqualsYes
argument_list|()
expr_stmt|;
block|}
DECL|method|testIsEmptyNo ()
specifier|public
name|void
name|testIsEmptyNo
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testIsEmptyNo
argument_list|()
expr_stmt|;
block|}
DECL|method|testIsEmptyYes ()
specifier|public
name|void
name|testIsEmptyYes
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testIsEmptyYes
argument_list|()
expr_stmt|;
block|}
DECL|method|testNullAcceptingComparator ()
specifier|public
name|void
name|testNullAcceptingComparator
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testNullAcceptingComparator
argument_list|()
expr_stmt|;
block|}
DECL|method|testReallyBig ()
specifier|public
name|void
name|testReallyBig
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testReallyBig
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemoveAllVacuous ()
specifier|public
name|void
name|testRemoveAllVacuous
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testRemoveAllVacuous
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemoveOneFromNoneStandard ()
specifier|public
name|void
name|testRemoveOneFromNoneStandard
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testRemoveOneFromNoneStandard
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemoveOneFromOneStandard ()
specifier|public
name|void
name|testRemoveOneFromOneStandard
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testRemoveOneFromOneStandard
argument_list|()
expr_stmt|;
block|}
DECL|method|testRetainAllOfNothing ()
specifier|public
name|void
name|testRetainAllOfNothing
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testRetainAllOfNothing
argument_list|()
expr_stmt|;
block|}
DECL|method|testRetainAllVacuous ()
specifier|public
name|void
name|testRetainAllVacuous
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testRetainAllVacuous
argument_list|()
expr_stmt|;
block|}
DECL|method|testSubMultisetSize ()
specifier|public
name|void
name|testSubMultisetSize
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testSubMultisetSize
argument_list|()
expr_stmt|;
block|}
DECL|method|testToArrayOne ()
specifier|public
name|void
name|testToArrayOne
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testToArrayOne
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
name|TreeMultisetTest
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
name|TreeMultisetTest
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
DECL|method|testToStringNull ()
specifier|public
name|void
name|testToStringNull
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testToStringNull
argument_list|()
expr_stmt|;
block|}
DECL|method|testUnmodifiableMultiset ()
specifier|public
name|void
name|testUnmodifiableMultiset
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
name|TreeMultisetTest
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
name|TreeMultisetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testUnmodifiableMultiset
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

