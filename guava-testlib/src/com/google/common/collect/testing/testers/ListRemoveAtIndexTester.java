begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.testers
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
operator|.
name|testers
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
operator|.
name|features
operator|.
name|CollectionFeature
operator|.
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
operator|.
name|features
operator|.
name|CollectionSize
operator|.
name|ONE
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
operator|.
name|features
operator|.
name|CollectionSize
operator|.
name|ZERO
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
operator|.
name|features
operator|.
name|ListFeature
operator|.
name|SUPPORTS_REMOVE_WITH_INDEX
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|GwtCompatible
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
operator|.
name|Helpers
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
operator|.
name|features
operator|.
name|CollectionFeature
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
operator|.
name|features
operator|.
name|CollectionSize
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
operator|.
name|features
operator|.
name|ListFeature
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ConcurrentModificationException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_comment
comment|/**  * A generic JUnit test which tests {@code remove(int)} operations on a list. Can't be invoked  * directly; please see {@link com.google.common.collect.testing.ListTestSuiteBuilder}.  *  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|Ignore
comment|// Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
DECL|class|ListRemoveAtIndexTester
specifier|public
class|class
name|ListRemoveAtIndexTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractListTester
argument_list|<
name|E
argument_list|>
block|{
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_REMOVE_WITH_INDEX
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testRemoveAtIndex_unsupported ()
specifier|public
name|void
name|testRemoveAtIndex_unsupported
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"remove(i) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE_WITH_INDEX
argument_list|)
DECL|method|testRemoveAtIndex_negative ()
specifier|public
name|void
name|testRemoveAtIndex_negative
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|remove
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"remove(-1) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|expected
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE_WITH_INDEX
argument_list|)
DECL|method|testRemoveAtIndex_tooLarge ()
specifier|public
name|void
name|testRemoveAtIndex_tooLarge
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|remove
argument_list|(
name|getNumElements
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"remove(size) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|expected
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE_WITH_INDEX
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testRemoveAtIndex_first ()
specifier|public
name|void
name|testRemoveAtIndex_first
parameter_list|()
block|{
name|runRemoveTest
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE_WITH_INDEX
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
block|{
name|ZERO
block|,
name|ONE
block|}
argument_list|)
DECL|method|testRemoveAtIndex_middle ()
specifier|public
name|void
name|testRemoveAtIndex_middle
parameter_list|()
block|{
name|runRemoveTest
argument_list|(
name|getNumElements
argument_list|()
operator|/
literal|2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
argument_list|)
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE_WITH_INDEX
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testRemoveAtIndexConcurrentWithIteration ()
specifier|public
name|void
name|testRemoveAtIndexConcurrentWithIteration
parameter_list|()
block|{
try|try
block|{
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
init|=
name|collection
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|getList
argument_list|()
operator|.
name|remove
argument_list|(
name|getNumElements
argument_list|()
operator|/
literal|2
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ConcurrentModificationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConcurrentModificationException
name|expected
parameter_list|)
block|{
comment|// success
block|}
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE_WITH_INDEX
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testRemoveAtIndex_last ()
specifier|public
name|void
name|testRemoveAtIndex_last
parameter_list|()
block|{
name|runRemoveTest
argument_list|(
name|getNumElements
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|runRemoveTest (int index)
specifier|private
name|void
name|runRemoveTest
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|Platform
operator|.
name|format
argument_list|(
literal|"remove(%d) should return the element at index %d"
argument_list|,
name|index
argument_list|,
name|index
argument_list|)
argument_list|,
name|getList
argument_list|()
operator|.
name|get
argument_list|(
name|index
argument_list|)
argument_list|,
name|getList
argument_list|()
operator|.
name|remove
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|E
argument_list|>
name|expected
init|=
name|Helpers
operator|.
name|copyToList
argument_list|(
name|createSamplesArray
argument_list|()
argument_list|)
decl_stmt|;
name|expected
operator|.
name|remove
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

