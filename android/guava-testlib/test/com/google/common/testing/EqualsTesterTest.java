begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Preconditions
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
name|ImmutableList
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
name|Sets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
comment|/**  * Unit tests for {@link EqualsTester}.  *  * @author Jim McMaster  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|SuppressWarnings
argument_list|(
literal|"MissingTestCall"
argument_list|)
DECL|class|EqualsTesterTest
specifier|public
class|class
name|EqualsTesterTest
extends|extends
name|TestCase
block|{
DECL|field|reference
specifier|private
name|ValidTestObject
name|reference
decl_stmt|;
DECL|field|equalsTester
specifier|private
name|EqualsTester
name|equalsTester
decl_stmt|;
DECL|field|equalObject1
specifier|private
name|ValidTestObject
name|equalObject1
decl_stmt|;
DECL|field|equalObject2
specifier|private
name|ValidTestObject
name|equalObject2
decl_stmt|;
DECL|field|notEqualObject1
specifier|private
name|ValidTestObject
name|notEqualObject1
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
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
name|reference
operator|=
operator|new
name|ValidTestObject
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|equalsTester
operator|=
operator|new
name|EqualsTester
argument_list|()
expr_stmt|;
name|equalObject1
operator|=
operator|new
name|ValidTestObject
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|equalObject2
operator|=
operator|new
name|ValidTestObject
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|notEqualObject1
operator|=
operator|new
name|ValidTestObject
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
comment|/** Test null reference yields error */
DECL|method|testAddNullReference ()
specifier|public
name|void
name|testAddNullReference
parameter_list|()
block|{
try|try
block|{
name|equalsTester
operator|.
name|addEqualityGroup
argument_list|(
operator|(
name|Object
operator|)
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
name|e
parameter_list|)
block|{     }
block|}
comment|/** Test equalObjects after adding multiple instances at once with a null */
DECL|method|testAddTwoEqualObjectsAtOnceWithNull ()
specifier|public
name|void
name|testAddTwoEqualObjectsAtOnceWithNull
parameter_list|()
block|{
try|try
block|{
name|equalsTester
operator|.
name|addEqualityGroup
argument_list|(
name|reference
argument_list|,
name|equalObject1
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should fail on null equal object"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{     }
block|}
comment|/** Test adding null equal object yields error */
DECL|method|testAddNullEqualObject ()
specifier|public
name|void
name|testAddNullEqualObject
parameter_list|()
block|{
try|try
block|{
name|equalsTester
operator|.
name|addEqualityGroup
argument_list|(
name|reference
argument_list|,
operator|(
name|Object
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should fail on null equal object"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{     }
block|}
comment|/**    * Test adding objects only by addEqualityGroup, with no reference object specified in the    * constructor.    */
DECL|method|testAddEqualObjectWithOArgConstructor ()
specifier|public
name|void
name|testAddEqualObjectWithOArgConstructor
parameter_list|()
block|{
name|equalsTester
operator|.
name|addEqualityGroup
argument_list|(
name|equalObject1
argument_list|,
name|notEqualObject1
argument_list|)
expr_stmt|;
try|try
block|{
name|equalsTester
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|e
parameter_list|)
block|{
name|assertErrorMessage
argument_list|(
name|e
argument_list|,
name|equalObject1
operator|+
literal|" [group 1, item 1] must be Object#equals to "
operator|+
name|notEqualObject1
operator|+
literal|" [group 1, item 2]"
argument_list|)
expr_stmt|;
return|return;
block|}
name|fail
argument_list|(
literal|"Should get not equal to equal object error"
argument_list|)
expr_stmt|;
block|}
comment|/**    * Test EqualsTester with no equals or not equals objects. This checks proper handling of null,    * incompatible class and reflexive tests    */
DECL|method|testTestEqualsEmptyLists ()
specifier|public
name|void
name|testTestEqualsEmptyLists
parameter_list|()
block|{
name|equalsTester
operator|.
name|addEqualityGroup
argument_list|(
name|reference
argument_list|)
expr_stmt|;
name|equalsTester
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
comment|/**    * Test EqualsTester after populating equalObjects. This checks proper handling of equality and    * verifies hashCode for valid objects    */
DECL|method|testTestEqualsEqualsObjects ()
specifier|public
name|void
name|testTestEqualsEqualsObjects
parameter_list|()
block|{
name|equalsTester
operator|.
name|addEqualityGroup
argument_list|(
name|reference
argument_list|,
name|equalObject1
argument_list|,
name|equalObject2
argument_list|)
expr_stmt|;
name|equalsTester
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
comment|/** Test proper handling of case where an object is not equal to itself */
DECL|method|testNonreflexiveEquals ()
specifier|public
name|void
name|testNonreflexiveEquals
parameter_list|()
block|{
name|Object
name|obj
init|=
operator|new
name|NonReflexiveObject
argument_list|()
decl_stmt|;
name|equalsTester
operator|.
name|addEqualityGroup
argument_list|(
name|obj
argument_list|)
expr_stmt|;
try|try
block|{
name|equalsTester
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|e
parameter_list|)
block|{
name|assertErrorMessage
argument_list|(
name|e
argument_list|,
name|obj
operator|+
literal|" must be Object#equals to itself"
argument_list|)
expr_stmt|;
return|return;
block|}
name|fail
argument_list|(
literal|"Should get non-reflexive error"
argument_list|)
expr_stmt|;
block|}
comment|/** Test proper handling where an object tests equal to null */
DECL|method|testInvalidEqualsNull ()
specifier|public
name|void
name|testInvalidEqualsNull
parameter_list|()
block|{
name|Object
name|obj
init|=
operator|new
name|InvalidEqualsNullObject
argument_list|()
decl_stmt|;
name|equalsTester
operator|.
name|addEqualityGroup
argument_list|(
name|obj
argument_list|)
expr_stmt|;
try|try
block|{
name|equalsTester
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|e
parameter_list|)
block|{
name|assertErrorMessage
argument_list|(
name|e
argument_list|,
name|obj
operator|+
literal|" must not be Object#equals to null"
argument_list|)
expr_stmt|;
return|return;
block|}
name|fail
argument_list|(
literal|"Should get equal to null error"
argument_list|)
expr_stmt|;
block|}
comment|/** Test proper handling where an object incorrectly tests for an incompatible class */
DECL|method|testInvalidEqualsIncompatibleClass ()
specifier|public
name|void
name|testInvalidEqualsIncompatibleClass
parameter_list|()
block|{
name|Object
name|obj
init|=
operator|new
name|InvalidEqualsIncompatibleClassObject
argument_list|()
decl_stmt|;
name|equalsTester
operator|.
name|addEqualityGroup
argument_list|(
name|obj
argument_list|)
expr_stmt|;
try|try
block|{
name|equalsTester
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|e
parameter_list|)
block|{
name|assertErrorMessage
argument_list|(
name|e
argument_list|,
name|obj
operator|+
literal|" must not be Object#equals to an arbitrary object of another class"
argument_list|)
expr_stmt|;
return|return;
block|}
name|fail
argument_list|(
literal|"Should get equal to incompatible class error"
argument_list|)
expr_stmt|;
block|}
comment|/** Test proper handling where an object is not equal to one the user has said should be equal */
DECL|method|testInvalidNotEqualsEqualObject ()
specifier|public
name|void
name|testInvalidNotEqualsEqualObject
parameter_list|()
block|{
name|equalsTester
operator|.
name|addEqualityGroup
argument_list|(
name|reference
argument_list|,
name|notEqualObject1
argument_list|)
expr_stmt|;
try|try
block|{
name|equalsTester
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|e
parameter_list|)
block|{
name|assertErrorMessage
argument_list|(
name|e
argument_list|,
name|reference
operator|+
literal|" [group 1, item 1]"
argument_list|)
expr_stmt|;
name|assertErrorMessage
argument_list|(
name|e
argument_list|,
name|notEqualObject1
operator|+
literal|" [group 1, item 2]"
argument_list|)
expr_stmt|;
return|return;
block|}
name|fail
argument_list|(
literal|"Should get not equal to equal object error"
argument_list|)
expr_stmt|;
block|}
comment|/**    * Test for an invalid hashCode method, i.e., one that returns different value for objects that    * are equal according to the equals method    */
DECL|method|testInvalidHashCode ()
specifier|public
name|void
name|testInvalidHashCode
parameter_list|()
block|{
name|Object
name|a
init|=
operator|new
name|InvalidHashCodeObject
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|Object
name|b
init|=
operator|new
name|InvalidHashCodeObject
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|equalsTester
operator|.
name|addEqualityGroup
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
expr_stmt|;
try|try
block|{
name|equalsTester
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|e
parameter_list|)
block|{
name|assertErrorMessage
argument_list|(
name|e
argument_list|,
literal|"the Object#hashCode ("
operator|+
name|a
operator|.
name|hashCode
argument_list|()
operator|+
literal|") of "
operator|+
name|a
operator|+
literal|" [group 1, item 1] must be equal to the Object#hashCode ("
operator|+
name|b
operator|.
name|hashCode
argument_list|()
operator|+
literal|") of "
operator|+
name|b
argument_list|)
expr_stmt|;
return|return;
block|}
name|fail
argument_list|(
literal|"Should get invalid hashCode error"
argument_list|)
expr_stmt|;
block|}
DECL|method|testNullEqualityGroup ()
specifier|public
name|void
name|testNullEqualityGroup
parameter_list|()
block|{
name|EqualsTester
name|tester
init|=
operator|new
name|EqualsTester
argument_list|()
decl_stmt|;
try|try
block|{
name|tester
operator|.
name|addEqualityGroup
argument_list|(
operator|(
name|Object
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{     }
block|}
DECL|method|testNullObjectInEqualityGroup ()
specifier|public
name|void
name|testNullObjectInEqualityGroup
parameter_list|()
block|{
name|EqualsTester
name|tester
init|=
operator|new
name|EqualsTester
argument_list|()
decl_stmt|;
try|try
block|{
name|tester
operator|.
name|addEqualityGroup
argument_list|(
literal|1
argument_list|,
literal|null
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
name|assertErrorMessage
argument_list|(
name|e
argument_list|,
literal|"at index 1"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testSymmetryBroken ()
specifier|public
name|void
name|testSymmetryBroken
parameter_list|()
block|{
name|EqualsTester
name|tester
init|=
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|named
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|addPeers
argument_list|(
literal|"bar"
argument_list|)
argument_list|,
name|named
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|tester
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|e
parameter_list|)
block|{
name|assertErrorMessage
argument_list|(
name|e
argument_list|,
literal|"bar [group 1, item 2] must be Object#equals to foo [group 1, item 1]"
argument_list|)
expr_stmt|;
return|return;
block|}
name|fail
argument_list|(
literal|"should failed because symmetry is broken"
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransitivityBrokenInEqualityGroup ()
specifier|public
name|void
name|testTransitivityBrokenInEqualityGroup
parameter_list|()
block|{
name|EqualsTester
name|tester
init|=
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|named
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|addPeers
argument_list|(
literal|"bar"
argument_list|,
literal|"baz"
argument_list|)
argument_list|,
name|named
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|addPeers
argument_list|(
literal|"foo"
argument_list|)
argument_list|,
name|named
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|addPeers
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|tester
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|e
parameter_list|)
block|{
name|assertErrorMessage
argument_list|(
name|e
argument_list|,
literal|"bar [group 1, item 2] must be Object#equals to baz [group 1, item 3]"
argument_list|)
expr_stmt|;
return|return;
block|}
name|fail
argument_list|(
literal|"should failed because transitivity is broken"
argument_list|)
expr_stmt|;
block|}
DECL|method|testUnequalObjectsInEqualityGroup ()
specifier|public
name|void
name|testUnequalObjectsInEqualityGroup
parameter_list|()
block|{
name|EqualsTester
name|tester
init|=
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|named
argument_list|(
literal|"foo"
argument_list|)
argument_list|,
name|named
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|tester
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|e
parameter_list|)
block|{
name|assertErrorMessage
argument_list|(
name|e
argument_list|,
literal|"foo [group 1, item 1] must be Object#equals to bar [group 1, item 2]"
argument_list|)
expr_stmt|;
return|return;
block|}
name|fail
argument_list|(
literal|"should failed because of unequal objects in the same equality group"
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransitivityBrokenAcrossEqualityGroups ()
specifier|public
name|void
name|testTransitivityBrokenAcrossEqualityGroups
parameter_list|()
block|{
name|EqualsTester
name|tester
init|=
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|named
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|addPeers
argument_list|(
literal|"bar"
argument_list|)
argument_list|,
name|named
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|addPeers
argument_list|(
literal|"foo"
argument_list|,
literal|"x"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|named
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|addPeers
argument_list|(
literal|"x"
argument_list|)
argument_list|,
name|named
argument_list|(
literal|"x"
argument_list|)
operator|.
name|addPeers
argument_list|(
literal|"baz"
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|tester
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|e
parameter_list|)
block|{
name|assertErrorMessage
argument_list|(
name|e
argument_list|,
literal|"bar [group 1, item 2] must not be Object#equals to x [group 2, item 2]"
argument_list|)
expr_stmt|;
return|return;
block|}
name|fail
argument_list|(
literal|"should failed because transitivity is broken"
argument_list|)
expr_stmt|;
block|}
DECL|method|testEqualityGroups ()
specifier|public
name|void
name|testEqualityGroups
parameter_list|()
block|{
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|named
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|addPeers
argument_list|(
literal|"bar"
argument_list|)
argument_list|,
name|named
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|addPeers
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|named
argument_list|(
literal|"baz"
argument_list|)
argument_list|,
name|named
argument_list|(
literal|"baz"
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testEqualityBasedOnToString ()
specifier|public
name|void
name|testEqualityBasedOnToString
parameter_list|()
block|{
try|try
block|{
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
operator|new
name|EqualsBasedOnToString
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"toString representation"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertErrorMessage (Throwable e, String message)
specifier|private
specifier|static
name|void
name|assertErrorMessage
parameter_list|(
name|Throwable
name|e
parameter_list|,
name|String
name|message
parameter_list|)
block|{
comment|// TODO(kevinb): use a Truth assertion here
if|if
condition|(
operator|!
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|fail
argument_list|(
literal|"expected<"
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|"> to contain<"
operator|+
name|message
operator|+
literal|">"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Test class with valid equals and hashCode methods. Testers created with instances of this class    * should always pass.    */
DECL|class|ValidTestObject
specifier|private
specifier|static
class|class
name|ValidTestObject
block|{
DECL|field|aspect1
specifier|private
name|int
name|aspect1
decl_stmt|;
DECL|field|aspect2
specifier|private
name|int
name|aspect2
decl_stmt|;
DECL|method|ValidTestObject (int aspect1, int aspect2)
name|ValidTestObject
parameter_list|(
name|int
name|aspect1
parameter_list|,
name|int
name|aspect2
parameter_list|)
block|{
name|this
operator|.
name|aspect1
operator|=
name|aspect1
expr_stmt|;
name|this
operator|.
name|aspect2
operator|=
name|aspect2
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|ValidTestObject
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ValidTestObject
name|other
init|=
operator|(
name|ValidTestObject
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|aspect1
operator|!=
name|other
operator|.
name|aspect1
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|aspect2
operator|!=
name|other
operator|.
name|aspect2
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
literal|17
decl_stmt|;
name|result
operator|=
literal|37
operator|*
name|result
operator|+
name|aspect1
expr_stmt|;
name|result
operator|=
literal|37
operator|*
name|result
operator|+
name|aspect2
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
comment|/** Test class with invalid hashCode method. */
DECL|class|InvalidHashCodeObject
specifier|private
specifier|static
class|class
name|InvalidHashCodeObject
block|{
DECL|field|aspect1
specifier|private
name|int
name|aspect1
decl_stmt|;
DECL|field|aspect2
specifier|private
name|int
name|aspect2
decl_stmt|;
DECL|method|InvalidHashCodeObject (int aspect1, int aspect2)
name|InvalidHashCodeObject
parameter_list|(
name|int
name|aspect1
parameter_list|,
name|int
name|aspect2
parameter_list|)
block|{
name|this
operator|.
name|aspect1
operator|=
name|aspect1
expr_stmt|;
name|this
operator|.
name|aspect2
operator|=
name|aspect2
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"EqualsHashCode"
argument_list|)
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|InvalidHashCodeObject
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|InvalidHashCodeObject
name|other
init|=
operator|(
name|InvalidHashCodeObject
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|aspect1
operator|!=
name|other
operator|.
name|aspect1
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|aspect2
operator|!=
name|other
operator|.
name|aspect2
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
comment|/** Test class that violates reflexivity. It is not equal to itself */
DECL|class|NonReflexiveObject
specifier|private
specifier|static
class|class
name|NonReflexiveObject
block|{
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|super
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
comment|/** Test class that returns true if the test object is null */
DECL|class|InvalidEqualsNullObject
specifier|private
specifier|static
class|class
name|InvalidEqualsNullObject
block|{
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|==
name|this
operator|||
name|o
operator|==
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
block|}
comment|/** Test class that returns true even if the test object is of the wrong class */
DECL|class|InvalidEqualsIncompatibleClassObject
specifier|private
specifier|static
class|class
name|InvalidEqualsIncompatibleClassObject
block|{
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
block|}
DECL|method|named (String name)
specifier|private
specifier|static
name|NamedObject
name|named
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|NamedObject
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|class|NamedObject
specifier|private
specifier|static
class|class
name|NamedObject
block|{
DECL|field|peerNames
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|peerNames
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|method|NamedObject (String name)
name|NamedObject
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|addPeers (String... names)
name|NamedObject
name|addPeers
parameter_list|(
name|String
modifier|...
name|names
parameter_list|)
block|{
name|peerNames
operator|.
name|addAll
argument_list|(
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|names
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|NamedObject
condition|)
block|{
name|NamedObject
name|that
init|=
operator|(
name|NamedObject
operator|)
name|obj
decl_stmt|;
return|return
name|name
operator|.
name|equals
argument_list|(
name|that
operator|.
name|name
argument_list|)
operator|||
name|peerNames
operator|.
name|contains
argument_list|(
name|that
operator|.
name|name
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
DECL|class|EqualsBasedOnToString
specifier|private
specifier|static
specifier|final
class|class
name|EqualsBasedOnToString
block|{
DECL|field|s
specifier|private
specifier|final
name|String
name|s
decl_stmt|;
DECL|method|EqualsBasedOnToString (String s)
specifier|private
name|EqualsBasedOnToString
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|this
operator|.
name|s
operator|=
name|s
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|obj
operator|!=
literal|null
operator|&&
name|obj
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|toString
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|s
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|s
return|;
block|}
block|}
block|}
end_class

end_unit

