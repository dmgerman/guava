begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
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
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|base
operator|.
name|Equivalence
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
name|base
operator|.
name|Objects
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
name|ImmutableMap
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
name|ImmutableTable
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|AssertionFailedError
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
comment|/**  * Tests for {@link EquivalenceTester}.  *  * @author Gregory Kick  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|EquivalenceTesterTest
specifier|public
class|class
name|EquivalenceTesterTest
extends|extends
name|TestCase
block|{
DECL|field|tester
specifier|private
name|EquivalenceTester
argument_list|<
name|Object
argument_list|>
name|tester
decl_stmt|;
DECL|field|equivalenceMock
specifier|private
name|MockEquivalence
name|equivalenceMock
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|this
operator|.
name|equivalenceMock
operator|=
operator|new
name|MockEquivalence
argument_list|()
expr_stmt|;
name|this
operator|.
name|tester
operator|=
name|EquivalenceTester
operator|.
name|of
argument_list|(
name|equivalenceMock
argument_list|)
expr_stmt|;
block|}
comment|/** Test null reference yields error */
DECL|method|testOf_NullPointerException ()
specifier|public
name|void
name|testOf_NullPointerException
parameter_list|()
block|{
try|try
block|{
name|EquivalenceTester
operator|.
name|of
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should fail on null reference"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testTest_NoData ()
specifier|public
name|void
name|testTest_NoData
parameter_list|()
block|{
name|tester
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testTest ()
specifier|public
name|void
name|testTest
parameter_list|()
block|{
name|Object
name|group1Item1
init|=
operator|new
name|TestObject
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Object
name|group1Item2
init|=
operator|new
name|TestObject
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|Object
name|group2Item1
init|=
operator|new
name|TestObject
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Object
name|group2Item2
init|=
operator|new
name|TestObject
argument_list|(
literal|2
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|equivalenceMock
operator|.
name|expectEquivalent
argument_list|(
name|group1Item1
argument_list|,
name|group1Item2
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectDistinct
argument_list|(
name|group1Item1
argument_list|,
name|group2Item1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectDistinct
argument_list|(
name|group1Item1
argument_list|,
name|group2Item2
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectEquivalent
argument_list|(
name|group1Item2
argument_list|,
name|group1Item1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectDistinct
argument_list|(
name|group1Item2
argument_list|,
name|group2Item1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectDistinct
argument_list|(
name|group1Item2
argument_list|,
name|group2Item2
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectDistinct
argument_list|(
name|group2Item1
argument_list|,
name|group1Item1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectDistinct
argument_list|(
name|group2Item1
argument_list|,
name|group1Item2
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectEquivalent
argument_list|(
name|group2Item1
argument_list|,
name|group2Item2
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectDistinct
argument_list|(
name|group2Item2
argument_list|,
name|group1Item1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectDistinct
argument_list|(
name|group2Item2
argument_list|,
name|group1Item2
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectEquivalent
argument_list|(
name|group2Item2
argument_list|,
name|group2Item1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectHash
argument_list|(
name|group1Item1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectHash
argument_list|(
name|group1Item2
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectHash
argument_list|(
name|group2Item1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectHash
argument_list|(
name|group2Item2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|replay
argument_list|()
expr_stmt|;
name|tester
operator|.
name|addEquivalenceGroup
argument_list|(
name|group1Item1
argument_list|,
name|group1Item2
argument_list|)
operator|.
name|addEquivalenceGroup
argument_list|(
name|group2Item1
argument_list|,
name|group2Item2
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testTest_symmetric ()
specifier|public
name|void
name|testTest_symmetric
parameter_list|()
block|{
name|Object
name|group1Item1
init|=
operator|new
name|TestObject
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Object
name|group1Item2
init|=
operator|new
name|TestObject
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|equivalenceMock
operator|.
name|expectEquivalent
argument_list|(
name|group1Item1
argument_list|,
name|group1Item2
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectDistinct
argument_list|(
name|group1Item2
argument_list|,
name|group1Item1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectHash
argument_list|(
name|group1Item1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectHash
argument_list|(
name|group1Item2
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|replay
argument_list|()
expr_stmt|;
try|try
block|{
name|tester
operator|.
name|addEquivalenceGroup
argument_list|(
name|group1Item1
argument_list|,
name|group1Item2
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|expected
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"TestObject{group=1, item=2} [group 1, item 2] must be equivalent to "
operator|+
literal|"TestObject{group=1, item=1} [group 1, item 1]"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|fail
argument_list|()
expr_stmt|;
block|}
DECL|method|testTest_trasitive ()
specifier|public
name|void
name|testTest_trasitive
parameter_list|()
block|{
name|Object
name|group1Item1
init|=
operator|new
name|TestObject
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Object
name|group1Item2
init|=
operator|new
name|TestObject
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|Object
name|group1Item3
init|=
operator|new
name|TestObject
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|equivalenceMock
operator|.
name|expectEquivalent
argument_list|(
name|group1Item1
argument_list|,
name|group1Item2
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectEquivalent
argument_list|(
name|group1Item1
argument_list|,
name|group1Item3
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectEquivalent
argument_list|(
name|group1Item2
argument_list|,
name|group1Item1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectDistinct
argument_list|(
name|group1Item2
argument_list|,
name|group1Item3
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectEquivalent
argument_list|(
name|group1Item3
argument_list|,
name|group1Item1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectEquivalent
argument_list|(
name|group1Item3
argument_list|,
name|group1Item2
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectHash
argument_list|(
name|group1Item1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectHash
argument_list|(
name|group1Item2
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectHash
argument_list|(
name|group1Item3
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|replay
argument_list|()
expr_stmt|;
try|try
block|{
name|tester
operator|.
name|addEquivalenceGroup
argument_list|(
name|group1Item1
argument_list|,
name|group1Item2
argument_list|,
name|group1Item3
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|expected
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"TestObject{group=1, item=2} [group 1, item 2] must be equivalent to "
operator|+
literal|"TestObject{group=1, item=3} [group 1, item 3]"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|fail
argument_list|()
expr_stmt|;
block|}
DECL|method|testTest_inequivalence ()
specifier|public
name|void
name|testTest_inequivalence
parameter_list|()
block|{
name|Object
name|group1Item1
init|=
operator|new
name|TestObject
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Object
name|group2Item1
init|=
operator|new
name|TestObject
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|equivalenceMock
operator|.
name|expectEquivalent
argument_list|(
name|group1Item1
argument_list|,
name|group2Item1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectDistinct
argument_list|(
name|group2Item1
argument_list|,
name|group1Item1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectHash
argument_list|(
name|group1Item1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectHash
argument_list|(
name|group2Item1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|replay
argument_list|()
expr_stmt|;
try|try
block|{
name|tester
operator|.
name|addEquivalenceGroup
argument_list|(
name|group1Item1
argument_list|)
operator|.
name|addEquivalenceGroup
argument_list|(
name|group2Item1
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|expected
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"TestObject{group=1, item=1} [group 1, item 1] must be inequivalent to "
operator|+
literal|"TestObject{group=2, item=1} [group 2, item 1]"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|fail
argument_list|()
expr_stmt|;
block|}
DECL|method|testTest_hash ()
specifier|public
name|void
name|testTest_hash
parameter_list|()
block|{
name|Object
name|group1Item1
init|=
operator|new
name|TestObject
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Object
name|group1Item2
init|=
operator|new
name|TestObject
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|equivalenceMock
operator|.
name|expectEquivalent
argument_list|(
name|group1Item1
argument_list|,
name|group1Item2
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectEquivalent
argument_list|(
name|group1Item2
argument_list|,
name|group1Item1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectHash
argument_list|(
name|group1Item1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|expectHash
argument_list|(
name|group1Item2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|equivalenceMock
operator|.
name|replay
argument_list|()
expr_stmt|;
try|try
block|{
name|tester
operator|.
name|addEquivalenceGroup
argument_list|(
name|group1Item1
argument_list|,
name|group1Item2
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|expected
parameter_list|)
block|{
name|String
name|expectedMessage
init|=
literal|"the hash (1) of TestObject{group=1, item=1} [group 1, item 1] must be "
operator|+
literal|"equal to the hash (2) of TestObject{group=1, item=2} [group 1, item 2]"
decl_stmt|;
if|if
condition|(
operator|!
name|expected
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
name|expectedMessage
argument_list|)
condition|)
block|{
name|fail
argument_list|(
literal|"<"
operator|+
name|expected
operator|.
name|getMessage
argument_list|()
operator|+
literal|"> expected to contain<"
operator|+
name|expectedMessage
operator|+
literal|">"
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
name|fail
argument_list|()
expr_stmt|;
block|}
comment|/** An object with a friendly {@link #toString()}. */
DECL|class|TestObject
specifier|private
specifier|static
specifier|final
class|class
name|TestObject
block|{
DECL|field|group
specifier|final
name|int
name|group
decl_stmt|;
DECL|field|item
specifier|final
name|int
name|item
decl_stmt|;
DECL|method|TestObject (int group , int item)
name|TestObject
parameter_list|(
name|int
name|group
parameter_list|,
name|int
name|item
parameter_list|)
block|{
name|this
operator|.
name|group
operator|=
name|group
expr_stmt|;
name|this
operator|.
name|item
operator|=
name|item
expr_stmt|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|toStringHelper
argument_list|(
literal|"TestObject"
argument_list|)
operator|.
name|add
argument_list|(
literal|"group"
argument_list|,
name|group
argument_list|)
operator|.
name|add
argument_list|(
literal|"item"
argument_list|,
name|item
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|class|MockEquivalence
specifier|private
specifier|static
specifier|final
class|class
name|MockEquivalence
extends|extends
name|Equivalence
argument_list|<
name|Object
argument_list|>
block|{
DECL|field|equivalentExpectationsBuilder
specifier|final
name|ImmutableTable
operator|.
name|Builder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|,
name|Boolean
argument_list|>
name|equivalentExpectationsBuilder
init|=
name|ImmutableTable
operator|.
name|builder
argument_list|()
decl_stmt|;
DECL|field|hashExpectationsBuilder
specifier|final
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|Object
argument_list|,
name|Integer
argument_list|>
name|hashExpectationsBuilder
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
DECL|field|equivalentExpectations
name|ImmutableTable
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|,
name|Boolean
argument_list|>
name|equivalentExpectations
decl_stmt|;
DECL|field|hashExpectations
name|ImmutableMap
argument_list|<
name|Object
argument_list|,
name|Integer
argument_list|>
name|hashExpectations
decl_stmt|;
DECL|method|expectEquivalent (Object a, Object b)
name|void
name|expectEquivalent
parameter_list|(
name|Object
name|a
parameter_list|,
name|Object
name|b
parameter_list|)
block|{
name|checkRecording
argument_list|()
expr_stmt|;
name|equivalentExpectationsBuilder
operator|.
name|put
argument_list|(
name|a
argument_list|,
name|b
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|expectDistinct (Object a, Object b)
name|void
name|expectDistinct
parameter_list|(
name|Object
name|a
parameter_list|,
name|Object
name|b
parameter_list|)
block|{
name|checkRecording
argument_list|()
expr_stmt|;
name|equivalentExpectationsBuilder
operator|.
name|put
argument_list|(
name|a
argument_list|,
name|b
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|expectHash (Object object, int hash)
name|void
name|expectHash
parameter_list|(
name|Object
name|object
parameter_list|,
name|int
name|hash
parameter_list|)
block|{
name|checkRecording
argument_list|()
expr_stmt|;
name|hashExpectationsBuilder
operator|.
name|put
argument_list|(
name|object
argument_list|,
name|hash
argument_list|)
expr_stmt|;
block|}
DECL|method|replay ()
name|void
name|replay
parameter_list|()
block|{
name|checkRecording
argument_list|()
expr_stmt|;
name|equivalentExpectations
operator|=
name|equivalentExpectationsBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
name|hashExpectations
operator|=
name|hashExpectationsBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
DECL|method|doEquivalent (Object a, Object b)
annotation|@
name|Override
specifier|protected
name|boolean
name|doEquivalent
parameter_list|(
name|Object
name|a
parameter_list|,
name|Object
name|b
parameter_list|)
block|{
return|return
name|equivalentExpectations
operator|.
name|get
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
return|;
block|}
DECL|method|doHash (Object object)
annotation|@
name|Override
specifier|protected
name|int
name|doHash
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|hashExpectations
operator|.
name|get
argument_list|(
name|object
argument_list|)
return|;
block|}
DECL|method|checkRecording ()
name|void
name|checkRecording
parameter_list|()
block|{
name|checkState
argument_list|(
name|equivalentExpectations
operator|==
literal|null
operator|&&
name|hashExpectations
operator|==
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

