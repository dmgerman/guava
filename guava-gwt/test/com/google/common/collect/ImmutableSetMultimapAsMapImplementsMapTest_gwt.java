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
DECL|class|ImmutableSetMultimapAsMapImplementsMapTest_gwt
specifier|public
class|class
name|ImmutableSetMultimapAsMapImplementsMapTest_gwt
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testClear
argument_list|()
expr_stmt|;
block|}
DECL|method|testContainsKey ()
specifier|public
name|void
name|testContainsKey
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testContainsKey
argument_list|()
expr_stmt|;
block|}
DECL|method|testContainsValue ()
specifier|public
name|void
name|testContainsValue
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testContainsValue
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySet
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySetAddAndAddAll ()
specifier|public
name|void
name|testEntrySetAddAndAddAll
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySetAddAndAddAll
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySetClear ()
specifier|public
name|void
name|testEntrySetClear
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySetClear
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySetContainsEntryIncompatibleKey ()
specifier|public
name|void
name|testEntrySetContainsEntryIncompatibleKey
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySetContainsEntryIncompatibleKey
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySetContainsEntryNullKeyMissing ()
specifier|public
name|void
name|testEntrySetContainsEntryNullKeyMissing
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySetContainsEntryNullKeyMissing
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySetContainsEntryNullKeyPresent ()
specifier|public
name|void
name|testEntrySetContainsEntryNullKeyPresent
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySetContainsEntryNullKeyPresent
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySetForEmptyMap ()
specifier|public
name|void
name|testEntrySetForEmptyMap
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySetForEmptyMap
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySetIteratorRemove ()
specifier|public
name|void
name|testEntrySetIteratorRemove
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySetIteratorRemove
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySetRemove ()
specifier|public
name|void
name|testEntrySetRemove
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySetRemove
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySetRemoveAll ()
specifier|public
name|void
name|testEntrySetRemoveAll
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySetRemoveAll
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySetRemoveAllNullFromEmpty ()
specifier|public
name|void
name|testEntrySetRemoveAllNullFromEmpty
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySetRemoveAllNullFromEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySetRemoveDifferentValue ()
specifier|public
name|void
name|testEntrySetRemoveDifferentValue
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySetRemoveDifferentValue
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySetRemoveMissingKey ()
specifier|public
name|void
name|testEntrySetRemoveMissingKey
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySetRemoveMissingKey
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySetRemoveNullKeyMissing ()
specifier|public
name|void
name|testEntrySetRemoveNullKeyMissing
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySetRemoveNullKeyMissing
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySetRemoveNullKeyPresent ()
specifier|public
name|void
name|testEntrySetRemoveNullKeyPresent
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySetRemoveNullKeyPresent
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySetRetainAll ()
specifier|public
name|void
name|testEntrySetRetainAll
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySetRetainAll
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySetRetainAllNullFromEmpty ()
specifier|public
name|void
name|testEntrySetRetainAllNullFromEmpty
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySetRetainAllNullFromEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySetSetValue ()
specifier|public
name|void
name|testEntrySetSetValue
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySetSetValue
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySetSetValueSameValue ()
specifier|public
name|void
name|testEntrySetSetValueSameValue
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEntrySetSetValueSameValue
argument_list|()
expr_stmt|;
block|}
DECL|method|testEqualsForEmptyMap ()
specifier|public
name|void
name|testEqualsForEmptyMap
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEqualsForEmptyMap
argument_list|()
expr_stmt|;
block|}
DECL|method|testEqualsForEqualMap ()
specifier|public
name|void
name|testEqualsForEqualMap
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEqualsForEqualMap
argument_list|()
expr_stmt|;
block|}
DECL|method|testEqualsForLargerMap ()
specifier|public
name|void
name|testEqualsForLargerMap
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEqualsForLargerMap
argument_list|()
expr_stmt|;
block|}
DECL|method|testEqualsForSmallerMap ()
specifier|public
name|void
name|testEqualsForSmallerMap
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testEqualsForSmallerMap
argument_list|()
expr_stmt|;
block|}
DECL|method|testGet ()
specifier|public
name|void
name|testGet
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testGet
argument_list|()
expr_stmt|;
block|}
DECL|method|testGetForEmptyMap ()
specifier|public
name|void
name|testGetForEmptyMap
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testGetForEmptyMap
argument_list|()
expr_stmt|;
block|}
DECL|method|testGetNull ()
specifier|public
name|void
name|testGetNull
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testGetNull
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
name|collect
operator|.
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testHashCode
argument_list|()
expr_stmt|;
block|}
DECL|method|testHashCodeForEmptyMap ()
specifier|public
name|void
name|testHashCodeForEmptyMap
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testHashCodeForEmptyMap
argument_list|()
expr_stmt|;
block|}
DECL|method|testKeySetClear ()
specifier|public
name|void
name|testKeySetClear
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testKeySetClear
argument_list|()
expr_stmt|;
block|}
DECL|method|testKeySetRemove ()
specifier|public
name|void
name|testKeySetRemove
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testKeySetRemove
argument_list|()
expr_stmt|;
block|}
DECL|method|testKeySetRemoveAll ()
specifier|public
name|void
name|testKeySetRemoveAll
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testKeySetRemoveAll
argument_list|()
expr_stmt|;
block|}
DECL|method|testKeySetRemoveAllNullFromEmpty ()
specifier|public
name|void
name|testKeySetRemoveAllNullFromEmpty
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testKeySetRemoveAllNullFromEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testKeySetRetainAll ()
specifier|public
name|void
name|testKeySetRetainAll
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testKeySetRetainAll
argument_list|()
expr_stmt|;
block|}
DECL|method|testKeySetRetainAllNullFromEmpty ()
specifier|public
name|void
name|testKeySetRetainAllNullFromEmpty
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testKeySetRetainAllNullFromEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testPutAllExistingKey ()
specifier|public
name|void
name|testPutAllExistingKey
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testPutAllExistingKey
argument_list|()
expr_stmt|;
block|}
DECL|method|testPutAllNewKey ()
specifier|public
name|void
name|testPutAllNewKey
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testPutAllNewKey
argument_list|()
expr_stmt|;
block|}
DECL|method|testPutExistingKey ()
specifier|public
name|void
name|testPutExistingKey
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testPutExistingKey
argument_list|()
expr_stmt|;
block|}
DECL|method|testPutNewKey ()
specifier|public
name|void
name|testPutNewKey
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testPutNewKey
argument_list|()
expr_stmt|;
block|}
DECL|method|testPutNullKey ()
specifier|public
name|void
name|testPutNullKey
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testPutNullKey
argument_list|()
expr_stmt|;
block|}
DECL|method|testPutNullValue ()
specifier|public
name|void
name|testPutNullValue
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testPutNullValue
argument_list|()
expr_stmt|;
block|}
DECL|method|testPutNullValueForExistingKey ()
specifier|public
name|void
name|testPutNullValueForExistingKey
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testPutNullValueForExistingKey
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemove ()
specifier|public
name|void
name|testRemove
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testRemove
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemoveMissingKey ()
specifier|public
name|void
name|testRemoveMissingKey
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testRemoveMissingKey
argument_list|()
expr_stmt|;
block|}
DECL|method|testSize ()
specifier|public
name|void
name|testSize
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testSize
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testValues
argument_list|()
expr_stmt|;
block|}
DECL|method|testValuesClear ()
specifier|public
name|void
name|testValuesClear
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testValuesClear
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testValuesIteratorRemove
argument_list|()
expr_stmt|;
block|}
DECL|method|testValuesRemove ()
specifier|public
name|void
name|testValuesRemove
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testValuesRemove
argument_list|()
expr_stmt|;
block|}
DECL|method|testValuesRemoveAll ()
specifier|public
name|void
name|testValuesRemoveAll
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testValuesRemoveAll
argument_list|()
expr_stmt|;
block|}
DECL|method|testValuesRemoveAllNullFromEmpty ()
specifier|public
name|void
name|testValuesRemoveAllNullFromEmpty
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testValuesRemoveAllNullFromEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testValuesRemoveMissing ()
specifier|public
name|void
name|testValuesRemoveMissing
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testValuesRemoveMissing
argument_list|()
expr_stmt|;
block|}
DECL|method|testValuesRetainAll ()
specifier|public
name|void
name|testValuesRetainAll
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testValuesRetainAll
argument_list|()
expr_stmt|;
block|}
DECL|method|testValuesRetainAllNullFromEmpty ()
specifier|public
name|void
name|testValuesRetainAllNullFromEmpty
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
name|ImmutableSetMultimapAsMapImplementsMapTest
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
name|ImmutableSetMultimapAsMapImplementsMapTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testValuesRetainAllNullFromEmpty
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

