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
DECL|class|MultisetsTest_gwt
specifier|public
class|class
name|MultisetsTest_gwt
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
DECL|method|testContainsOccurrences ()
specifier|public
name|void
name|testContainsOccurrences
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testContainsOccurrences
argument_list|()
expr_stmt|;
block|}
DECL|method|testContainsOccurrencesEmpty ()
specifier|public
name|void
name|testContainsOccurrencesEmpty
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testContainsOccurrencesEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testDifferenceEmptyNonempty ()
specifier|public
name|void
name|testDifferenceEmptyNonempty
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testDifferenceEmptyNonempty
argument_list|()
expr_stmt|;
block|}
DECL|method|testDifferenceNonemptyEmpty ()
specifier|public
name|void
name|testDifferenceNonemptyEmpty
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testDifferenceNonemptyEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testDifferenceWithMoreElementsInSecondMultiset ()
specifier|public
name|void
name|testDifferenceWithMoreElementsInSecondMultiset
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testDifferenceWithMoreElementsInSecondMultiset
argument_list|()
expr_stmt|;
block|}
DECL|method|testDifferenceWithNoRemovedElements ()
specifier|public
name|void
name|testDifferenceWithNoRemovedElements
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testDifferenceWithNoRemovedElements
argument_list|()
expr_stmt|;
block|}
DECL|method|testDifferenceWithRemovedElement ()
specifier|public
name|void
name|testDifferenceWithRemovedElement
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testDifferenceWithRemovedElement
argument_list|()
expr_stmt|;
block|}
DECL|method|testHighestCountFirst ()
specifier|public
name|void
name|testHighestCountFirst
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testHighestCountFirst
argument_list|()
expr_stmt|;
block|}
DECL|method|testIntersectEmptyNonempty ()
specifier|public
name|void
name|testIntersectEmptyNonempty
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testIntersectEmptyNonempty
argument_list|()
expr_stmt|;
block|}
DECL|method|testIntersectNonemptyEmpty ()
specifier|public
name|void
name|testIntersectNonemptyEmpty
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testIntersectNonemptyEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testNewTreeMultisetComparator ()
specifier|public
name|void
name|testNewTreeMultisetComparator
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testNewTreeMultisetComparator
argument_list|()
expr_stmt|;
block|}
DECL|method|testNewTreeMultisetDerived ()
specifier|public
name|void
name|testNewTreeMultisetDerived
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testNewTreeMultisetDerived
argument_list|()
expr_stmt|;
block|}
DECL|method|testNewTreeMultisetNonGeneric ()
specifier|public
name|void
name|testNewTreeMultisetNonGeneric
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testNewTreeMultisetNonGeneric
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemoveEmptyOccurrencesIterable ()
specifier|public
name|void
name|testRemoveEmptyOccurrencesIterable
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testRemoveEmptyOccurrencesIterable
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemoveEmptyOccurrencesMultiset ()
specifier|public
name|void
name|testRemoveEmptyOccurrencesMultiset
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testRemoveEmptyOccurrencesMultiset
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemoveOccurrencesIterableEmpty ()
specifier|public
name|void
name|testRemoveOccurrencesIterableEmpty
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testRemoveOccurrencesIterableEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemoveOccurrencesMultiset ()
specifier|public
name|void
name|testRemoveOccurrencesMultiset
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testRemoveOccurrencesMultiset
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemoveOccurrencesMultisetEmpty ()
specifier|public
name|void
name|testRemoveOccurrencesMultisetEmpty
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testRemoveOccurrencesMultisetEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemoveOccurrencesMultisetIterable ()
specifier|public
name|void
name|testRemoveOccurrencesMultisetIterable
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testRemoveOccurrencesMultisetIterable
argument_list|()
expr_stmt|;
block|}
DECL|method|testRetainEmptyOccurrences ()
specifier|public
name|void
name|testRetainEmptyOccurrences
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testRetainEmptyOccurrences
argument_list|()
expr_stmt|;
block|}
DECL|method|testRetainOccurrences ()
specifier|public
name|void
name|testRetainOccurrences
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testRetainOccurrences
argument_list|()
expr_stmt|;
block|}
DECL|method|testRetainOccurrencesEmpty ()
specifier|public
name|void
name|testRetainOccurrencesEmpty
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testRetainOccurrencesEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testSum ()
specifier|public
name|void
name|testSum
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testSum
argument_list|()
expr_stmt|;
block|}
DECL|method|testSumEmptyNonempty ()
specifier|public
name|void
name|testSumEmptyNonempty
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testSumEmptyNonempty
argument_list|()
expr_stmt|;
block|}
DECL|method|testSumNonemptyEmpty ()
specifier|public
name|void
name|testSumNonemptyEmpty
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testSumNonemptyEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testUnion ()
specifier|public
name|void
name|testUnion
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testUnion
argument_list|()
expr_stmt|;
block|}
DECL|method|testUnionEmptyNonempty ()
specifier|public
name|void
name|testUnionEmptyNonempty
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testUnionEmptyNonempty
argument_list|()
expr_stmt|;
block|}
DECL|method|testUnionEqualMultisets ()
specifier|public
name|void
name|testUnionEqualMultisets
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testUnionEqualMultisets
argument_list|()
expr_stmt|;
block|}
DECL|method|testUnionNonemptyEmpty ()
specifier|public
name|void
name|testUnionNonemptyEmpty
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testUnionNonemptyEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testUnmodifiableMultisetShortCircuit ()
specifier|public
name|void
name|testUnmodifiableMultisetShortCircuit
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
name|MultisetsTest
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
name|MultisetsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testUnmodifiableMultisetShortCircuit
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

