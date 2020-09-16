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
DECL|class|RangeTest_gwt
specifier|public
class|class
name|RangeTest_gwt
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
DECL|method|testAll ()
specifier|public
name|void
name|testAll
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testAll
argument_list|()
expr_stmt|;
block|}
DECL|method|testApply ()
specifier|public
name|void
name|testApply
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testApply
argument_list|()
expr_stmt|;
block|}
DECL|method|testAtLeast ()
specifier|public
name|void
name|testAtLeast
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testAtLeast
argument_list|()
expr_stmt|;
block|}
DECL|method|testAtMost ()
specifier|public
name|void
name|testAtMost
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testAtMost
argument_list|()
expr_stmt|;
block|}
DECL|method|testCanonical ()
specifier|public
name|void
name|testCanonical
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCanonical
argument_list|()
expr_stmt|;
block|}
DECL|method|testCanonical_unboundedDomain ()
specifier|public
name|void
name|testCanonical_unboundedDomain
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCanonical_unboundedDomain
argument_list|()
expr_stmt|;
block|}
DECL|method|testClosed ()
specifier|public
name|void
name|testClosed
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testClosed
argument_list|()
expr_stmt|;
block|}
DECL|method|testClosedOpen ()
specifier|public
name|void
name|testClosedOpen
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testClosedOpen
argument_list|()
expr_stmt|;
block|}
DECL|method|testClosed_invalid ()
specifier|public
name|void
name|testClosed_invalid
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testClosed_invalid
argument_list|()
expr_stmt|;
block|}
DECL|method|testContainsAll ()
specifier|public
name|void
name|testContainsAll
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testContainsAll
argument_list|()
expr_stmt|;
block|}
DECL|method|testEmpty1 ()
specifier|public
name|void
name|testEmpty1
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEmpty1
argument_list|()
expr_stmt|;
block|}
DECL|method|testEmpty2 ()
specifier|public
name|void
name|testEmpty2
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEmpty2
argument_list|()
expr_stmt|;
block|}
DECL|method|testEncloseAll ()
specifier|public
name|void
name|testEncloseAll
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEncloseAll
argument_list|()
expr_stmt|;
block|}
DECL|method|testEncloseAll_empty ()
specifier|public
name|void
name|testEncloseAll_empty
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEncloseAll_empty
argument_list|()
expr_stmt|;
block|}
DECL|method|testEncloseAll_nullValue ()
specifier|public
name|void
name|testEncloseAll_nullValue
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEncloseAll_nullValue
argument_list|()
expr_stmt|;
block|}
DECL|method|testEncloses_closed ()
specifier|public
name|void
name|testEncloses_closed
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEncloses_closed
argument_list|()
expr_stmt|;
block|}
DECL|method|testEncloses_open ()
specifier|public
name|void
name|testEncloses_open
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEncloses_open
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testEquivalentFactories ()
specifier|public
name|void
name|testEquivalentFactories
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEquivalentFactories
argument_list|()
expr_stmt|;
block|}
DECL|method|testGap_connectedAdjacentYieldsEmpty ()
specifier|public
name|void
name|testGap_connectedAdjacentYieldsEmpty
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testGap_connectedAdjacentYieldsEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testGap_general ()
specifier|public
name|void
name|testGap_general
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testGap_general
argument_list|()
expr_stmt|;
block|}
DECL|method|testGap_invalidRangesWithInfinity ()
specifier|public
name|void
name|testGap_invalidRangesWithInfinity
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testGap_invalidRangesWithInfinity
argument_list|()
expr_stmt|;
block|}
DECL|method|testGap_overlapping ()
specifier|public
name|void
name|testGap_overlapping
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testGap_overlapping
argument_list|()
expr_stmt|;
block|}
DECL|method|testGreaterThan ()
specifier|public
name|void
name|testGreaterThan
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testGreaterThan
argument_list|()
expr_stmt|;
block|}
DECL|method|testIntersection_deFactoEmpty ()
specifier|public
name|void
name|testIntersection_deFactoEmpty
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testIntersection_deFactoEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testIntersection_empty ()
specifier|public
name|void
name|testIntersection_empty
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testIntersection_empty
argument_list|()
expr_stmt|;
block|}
DECL|method|testIntersection_general ()
specifier|public
name|void
name|testIntersection_general
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testIntersection_general
argument_list|()
expr_stmt|;
block|}
DECL|method|testIntersection_singleton ()
specifier|public
name|void
name|testIntersection_singleton
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testIntersection_singleton
argument_list|()
expr_stmt|;
block|}
DECL|method|testIsConnected ()
specifier|public
name|void
name|testIsConnected
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testIsConnected
argument_list|()
expr_stmt|;
block|}
DECL|method|testLessThan ()
specifier|public
name|void
name|testLessThan
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLessThan
argument_list|()
expr_stmt|;
block|}
DECL|method|testOpen ()
specifier|public
name|void
name|testOpen
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testOpen
argument_list|()
expr_stmt|;
block|}
DECL|method|testOpenClosed ()
specifier|public
name|void
name|testOpenClosed
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testOpenClosed
argument_list|()
expr_stmt|;
block|}
DECL|method|testOpen_invalid ()
specifier|public
name|void
name|testOpen_invalid
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testOpen_invalid
argument_list|()
expr_stmt|;
block|}
DECL|method|testOrderingCuts ()
specifier|public
name|void
name|testOrderingCuts
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testOrderingCuts
argument_list|()
expr_stmt|;
block|}
DECL|method|testSingleton ()
specifier|public
name|void
name|testSingleton
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testSingleton
argument_list|()
expr_stmt|;
block|}
DECL|method|testSpan_general ()
specifier|public
name|void
name|testSpan_general
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
name|RangeTest
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
name|RangeTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testSpan_general
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

