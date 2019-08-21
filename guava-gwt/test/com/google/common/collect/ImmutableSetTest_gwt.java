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
DECL|class|ImmutableSetTest_gwt
specifier|public
class|class
name|ImmutableSetTest_gwt
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
DECL|method|testBuilderAddAll ()
specifier|public
name|void
name|testBuilderAddAll
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testBuilderAddAll
argument_list|()
expr_stmt|;
block|}
DECL|method|testBuilderAddAllHandlesNullsCorrectly ()
specifier|public
name|void
name|testBuilderAddAllHandlesNullsCorrectly
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testBuilderAddAllHandlesNullsCorrectly
argument_list|()
expr_stmt|;
block|}
DECL|method|testBuilderAddHandlesNullsCorrectly ()
specifier|public
name|void
name|testBuilderAddHandlesNullsCorrectly
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testBuilderAddHandlesNullsCorrectly
argument_list|()
expr_stmt|;
block|}
DECL|method|testBuilderWithDuplicateElements ()
specifier|public
name|void
name|testBuilderWithDuplicateElements
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testBuilderWithDuplicateElements
argument_list|()
expr_stmt|;
block|}
DECL|method|testBuilderWithNonDuplicateElements ()
specifier|public
name|void
name|testBuilderWithNonDuplicateElements
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testBuilderWithNonDuplicateElements
argument_list|()
expr_stmt|;
block|}
DECL|method|testComplexBuilder ()
specifier|public
name|void
name|testComplexBuilder
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testComplexBuilder
argument_list|()
expr_stmt|;
block|}
DECL|method|testContainsAll_sameType ()
specifier|public
name|void
name|testContainsAll_sameType
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testContainsAll_sameType
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_arrayContainingOnlyNull ()
specifier|public
name|void
name|testCopyOf_arrayContainingOnlyNull
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_arrayContainingOnlyNull
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_arrayOfOneElement ()
specifier|public
name|void
name|testCopyOf_arrayOfOneElement
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_arrayOfOneElement
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_collectionContainingNull ()
specifier|public
name|void
name|testCopyOf_collectionContainingNull
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_collectionContainingNull
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_collection_empty ()
specifier|public
name|void
name|testCopyOf_collection_empty
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_collection_empty
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_collection_enumSet ()
specifier|public
name|void
name|testCopyOf_collection_enumSet
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_collection_enumSet
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_collection_general ()
specifier|public
name|void
name|testCopyOf_collection_general
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_collection_general
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_collection_oneElement ()
specifier|public
name|void
name|testCopyOf_collection_oneElement
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_collection_oneElement
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_collection_oneElementRepeated ()
specifier|public
name|void
name|testCopyOf_collection_oneElementRepeated
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_collection_oneElementRepeated
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_copiesImmutableSortedSet ()
specifier|public
name|void
name|testCopyOf_copiesImmutableSortedSet
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_copiesImmutableSortedSet
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_emptyArray ()
specifier|public
name|void
name|testCopyOf_emptyArray
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_emptyArray
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_iteratorContainingNull ()
specifier|public
name|void
name|testCopyOf_iteratorContainingNull
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_iteratorContainingNull
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_iterator_empty ()
specifier|public
name|void
name|testCopyOf_iterator_empty
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_iterator_empty
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_iterator_general ()
specifier|public
name|void
name|testCopyOf_iterator_general
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_iterator_general
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_iterator_oneElement ()
specifier|public
name|void
name|testCopyOf_iterator_oneElement
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_iterator_oneElement
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_iterator_oneElementRepeated ()
specifier|public
name|void
name|testCopyOf_iterator_oneElementRepeated
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_iterator_oneElementRepeated
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_nullArray ()
specifier|public
name|void
name|testCopyOf_nullArray
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_nullArray
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_plainIterable ()
specifier|public
name|void
name|testCopyOf_plainIterable
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_plainIterable
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_plainIterable_iteratesOnce ()
specifier|public
name|void
name|testCopyOf_plainIterable_iteratesOnce
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_plainIterable_iteratesOnce
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_shortcut_empty ()
specifier|public
name|void
name|testCopyOf_shortcut_empty
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_shortcut_empty
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_shortcut_sameType ()
specifier|public
name|void
name|testCopyOf_shortcut_sameType
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_shortcut_sameType
argument_list|()
expr_stmt|;
block|}
DECL|method|testCopyOf_shortcut_singleton ()
specifier|public
name|void
name|testCopyOf_shortcut_singleton
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCopyOf_shortcut_singleton
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreation_allDuplicates ()
specifier|public
name|void
name|testCreation_allDuplicates
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCreation_allDuplicates
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreation_arrayOfArray ()
specifier|public
name|void
name|testCreation_arrayOfArray
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCreation_arrayOfArray
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreation_eightElements ()
specifier|public
name|void
name|testCreation_eightElements
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCreation_eightElements
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreation_fiveElements ()
specifier|public
name|void
name|testCreation_fiveElements
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCreation_fiveElements
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreation_fourElements ()
specifier|public
name|void
name|testCreation_fourElements
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCreation_fourElements
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreation_manyDuplicates ()
specifier|public
name|void
name|testCreation_manyDuplicates
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCreation_manyDuplicates
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreation_noArgs ()
specifier|public
name|void
name|testCreation_noArgs
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCreation_noArgs
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreation_oneDuplicate ()
specifier|public
name|void
name|testCreation_oneDuplicate
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCreation_oneDuplicate
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreation_oneElement ()
specifier|public
name|void
name|testCreation_oneElement
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCreation_oneElement
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreation_sevenElements ()
specifier|public
name|void
name|testCreation_sevenElements
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCreation_sevenElements
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreation_sixElements ()
specifier|public
name|void
name|testCreation_sixElements
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCreation_sixElements
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreation_threeElements ()
specifier|public
name|void
name|testCreation_threeElements
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCreation_threeElements
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreation_twoElements ()
specifier|public
name|void
name|testCreation_twoElements
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCreation_twoElements
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testEquals_sameType ()
specifier|public
name|void
name|testEquals_sameType
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEquals_sameType
argument_list|()
expr_stmt|;
block|}
DECL|method|testReuseBuilderReducingHashTableSizeWithPowerOfTwoTotalElements ()
specifier|public
name|void
name|testReuseBuilderReducingHashTableSizeWithPowerOfTwoTotalElements
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testReuseBuilderReducingHashTableSizeWithPowerOfTwoTotalElements
argument_list|()
expr_stmt|;
block|}
DECL|method|testReuseBuilderWithDuplicateElements ()
specifier|public
name|void
name|testReuseBuilderWithDuplicateElements
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testReuseBuilderWithDuplicateElements
argument_list|()
expr_stmt|;
block|}
DECL|method|testReuseBuilderWithNonDuplicateElements ()
specifier|public
name|void
name|testReuseBuilderWithNonDuplicateElements
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testReuseBuilderWithNonDuplicateElements
argument_list|()
expr_stmt|;
block|}
DECL|method|testToImmutableSet ()
specifier|public
name|void
name|testToImmutableSet
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testToImmutableSet
argument_list|()
expr_stmt|;
block|}
DECL|method|testToImmutableSet_duplicates ()
specifier|public
name|void
name|testToImmutableSet_duplicates
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testToImmutableSet_duplicates
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
name|ImmutableSetTest
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
name|ImmutableSetTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testToString
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

