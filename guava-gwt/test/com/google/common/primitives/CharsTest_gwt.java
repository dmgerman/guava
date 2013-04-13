begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.primitives
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
package|;
end_package

begin_class
DECL|class|CharsTest_gwt
specifier|public
class|class
name|CharsTest_gwt
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
literal|"com.google.common.primitives.testModule"
return|;
block|}
DECL|method|testAsListEmpty ()
specifier|public
name|void
name|testAsListEmpty
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testAsListEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testAsList_isAView ()
specifier|public
name|void
name|testAsList_isAView
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testAsList_isAView
argument_list|()
expr_stmt|;
block|}
DECL|method|testAsList_subList_toArray_roundTrip ()
specifier|public
name|void
name|testAsList_subList_toArray_roundTrip
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testAsList_subList_toArray_roundTrip
argument_list|()
expr_stmt|;
block|}
DECL|method|testAsList_toArray_roundTrip ()
specifier|public
name|void
name|testAsList_toArray_roundTrip
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testAsList_toArray_roundTrip
argument_list|()
expr_stmt|;
block|}
DECL|method|testCheckedCast ()
specifier|public
name|void
name|testCheckedCast
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCheckedCast
argument_list|()
expr_stmt|;
block|}
DECL|method|testCompare ()
specifier|public
name|void
name|testCompare
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCompare
argument_list|()
expr_stmt|;
block|}
DECL|method|testConcat ()
specifier|public
name|void
name|testConcat
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testConcat
argument_list|()
expr_stmt|;
block|}
DECL|method|testContains ()
specifier|public
name|void
name|testContains
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testContains
argument_list|()
expr_stmt|;
block|}
DECL|method|testEnsureCapacity ()
specifier|public
name|void
name|testEnsureCapacity
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEnsureCapacity
argument_list|()
expr_stmt|;
block|}
DECL|method|testEnsureCapacity_fail ()
specifier|public
name|void
name|testEnsureCapacity_fail
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEnsureCapacity_fail
argument_list|()
expr_stmt|;
block|}
DECL|method|testHashCode ()
specifier|public
name|void
name|testHashCode
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testHashCode
argument_list|()
expr_stmt|;
block|}
DECL|method|testIndexOf ()
specifier|public
name|void
name|testIndexOf
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testIndexOf
argument_list|()
expr_stmt|;
block|}
DECL|method|testIndexOf_arrayTarget ()
specifier|public
name|void
name|testIndexOf_arrayTarget
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testIndexOf_arrayTarget
argument_list|()
expr_stmt|;
block|}
DECL|method|testJoin ()
specifier|public
name|void
name|testJoin
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testJoin
argument_list|()
expr_stmt|;
block|}
DECL|method|testLastIndexOf ()
specifier|public
name|void
name|testLastIndexOf
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLastIndexOf
argument_list|()
expr_stmt|;
block|}
DECL|method|testLexicographicalComparator ()
specifier|public
name|void
name|testLexicographicalComparator
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLexicographicalComparator
argument_list|()
expr_stmt|;
block|}
DECL|method|testMax ()
specifier|public
name|void
name|testMax
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testMax
argument_list|()
expr_stmt|;
block|}
DECL|method|testMax_noArgs ()
specifier|public
name|void
name|testMax_noArgs
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testMax_noArgs
argument_list|()
expr_stmt|;
block|}
DECL|method|testMin ()
specifier|public
name|void
name|testMin
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testMin
argument_list|()
expr_stmt|;
block|}
DECL|method|testMin_noArgs ()
specifier|public
name|void
name|testMin_noArgs
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testMin_noArgs
argument_list|()
expr_stmt|;
block|}
DECL|method|testSaturatedCast ()
specifier|public
name|void
name|testSaturatedCast
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testSaturatedCast
argument_list|()
expr_stmt|;
block|}
DECL|method|testToArray ()
specifier|public
name|void
name|testToArray
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testToArray
argument_list|()
expr_stmt|;
block|}
DECL|method|testToArray_threadSafe ()
specifier|public
name|void
name|testToArray_threadSafe
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testToArray_threadSafe
argument_list|()
expr_stmt|;
block|}
DECL|method|testToArray_withNull ()
specifier|public
name|void
name|testToArray_withNull
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
name|primitives
operator|.
name|CharsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|CharsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testToArray_withNull
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

