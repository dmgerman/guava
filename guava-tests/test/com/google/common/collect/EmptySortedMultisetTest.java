begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *   * Licensed under the Apache License, Version 2.0 (the "License"); you may not  * use this file except in compliance with the License. You may obtain a copy of  * the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the  * License for the specific language governing permissions and limitations under  * the License.  */
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Test cases for {@link EmptySortedMultiset}.  *   * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|EmptySortedMultisetTest
specifier|public
class|class
name|EmptySortedMultisetTest
extends|extends
name|TestCase
block|{
DECL|method|testContainsNull ()
specifier|public
name|void
name|testContainsNull
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|EmptySortedMultiset
operator|.
name|natural
argument_list|()
operator|.
name|contains
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCountNull ()
specifier|public
name|void
name|testCountNull
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|EmptySortedMultiset
operator|.
name|natural
argument_list|()
operator|.
name|count
argument_list|(
literal|null
argument_list|)
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|testRemoveNull ()
specifier|public
name|void
name|testRemoveNull
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|EmptySortedMultiset
operator|.
name|natural
argument_list|()
operator|.
name|remove
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAdd ()
specifier|public
name|void
name|testAdd
parameter_list|()
block|{
try|try
block|{
name|EmptySortedMultiset
operator|.
name|natural
argument_list|()
operator|.
name|add
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testAddNull ()
specifier|public
name|void
name|testAddNull
parameter_list|()
block|{
try|try
block|{
name|EmptySortedMultiset
operator|.
name|natural
argument_list|()
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected NullPointerException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testRemove ()
specifier|public
name|void
name|testRemove
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|EmptySortedMultiset
operator|.
name|natural
argument_list|()
operator|.
name|remove
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsEmpty ()
specifier|public
name|void
name|testIsEmpty
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|EmptySortedMultiset
operator|.
name|natural
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|EmptySortedMultiset
operator|.
name|natural
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|EmptySortedMultiset
operator|.
name|natural
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|EmptySortedMultiset
operator|.
name|natural
argument_list|()
operator|.
name|elementSet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testElementSetAdd ()
specifier|public
name|void
name|testElementSetAdd
parameter_list|()
block|{
try|try
block|{
name|EmptySortedMultiset
operator|.
name|natural
argument_list|()
operator|.
name|elementSet
argument_list|()
operator|.
name|add
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected UnsupportedOperationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
block|}
block|}
end_class

end_unit

