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
DECL|class|ContiguousSetTest_gwt
specifier|public
class|class
name|ContiguousSetTest_gwt
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
DECL|method|testAsList ()
specifier|public
name|void
name|testAsList
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testAsList
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
name|collect
operator|.
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testContains
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testContainsAll
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreate_empty ()
specifier|public
name|void
name|testCreate_empty
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCreate_empty
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreate_noMax ()
specifier|public
name|void
name|testCreate_noMax
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCreate_noMax
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreate_noMin ()
specifier|public
name|void
name|testCreate_noMin
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCreate_noMin
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testFirst ()
specifier|public
name|void
name|testFirst
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testFirst
argument_list|()
expr_stmt|;
block|}
DECL|method|testHeadSet ()
specifier|public
name|void
name|testHeadSet
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testHeadSet
argument_list|()
expr_stmt|;
block|}
DECL|method|testHeadSet_tooSmall ()
specifier|public
name|void
name|testHeadSet_tooSmall
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testHeadSet_tooSmall
argument_list|()
expr_stmt|;
block|}
DECL|method|testIntersection ()
specifier|public
name|void
name|testIntersection
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testIntersection
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testIntersection_empty
argument_list|()
expr_stmt|;
block|}
DECL|method|testLast ()
specifier|public
name|void
name|testLast
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testLast
argument_list|()
expr_stmt|;
block|}
DECL|method|testRange ()
specifier|public
name|void
name|testRange
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testRange
argument_list|()
expr_stmt|;
block|}
DECL|method|testRange_unboundedRange ()
specifier|public
name|void
name|testRange_unboundedRange
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testRange_unboundedRange
argument_list|()
expr_stmt|;
block|}
DECL|method|testSubSet ()
specifier|public
name|void
name|testSubSet
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testSubSet
argument_list|()
expr_stmt|;
block|}
DECL|method|testSubSet_outOfOrder ()
specifier|public
name|void
name|testSubSet_outOfOrder
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testSubSet_outOfOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testSubSet_tooLarge ()
specifier|public
name|void
name|testSubSet_tooLarge
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testSubSet_tooLarge
argument_list|()
expr_stmt|;
block|}
DECL|method|testSubSet_tooSmall ()
specifier|public
name|void
name|testSubSet_tooSmall
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testSubSet_tooSmall
argument_list|()
expr_stmt|;
block|}
DECL|method|testTailSet ()
specifier|public
name|void
name|testTailSet
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testTailSet
argument_list|()
expr_stmt|;
block|}
DECL|method|testTailSet_tooLarge ()
specifier|public
name|void
name|testTailSet_tooLarge
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
name|ContiguousSetTest
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
name|ContiguousSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testTailSet_tooLarge
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

