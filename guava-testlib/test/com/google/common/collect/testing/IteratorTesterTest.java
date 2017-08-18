begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
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
name|Lists
operator|.
name|newArrayList
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
name|IteratorFeature
operator|.
name|MODIFIABLE
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Collections
operator|.
name|emptyList
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
name|Lists
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
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
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
comment|/**  * Unit test for IteratorTester.  *  * @author Mick Killianey  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// No serialization is used in this test
DECL|class|IteratorTesterTest
specifier|public
class|class
name|IteratorTesterTest
extends|extends
name|TestCase
block|{
DECL|method|testCanCatchDifferentLengthOfIteration ()
specifier|public
name|void
name|testCanCatchDifferentLengthOfIteration
parameter_list|()
block|{
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
name|tester
init|=
operator|new
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|4
argument_list|,
name|MODIFIABLE
argument_list|,
name|newArrayList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
return|return
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|assertFailure
argument_list|(
name|tester
argument_list|)
expr_stmt|;
block|}
DECL|method|testCanCatchDifferentContents ()
specifier|public
name|void
name|testCanCatchDifferentContents
parameter_list|()
block|{
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
name|tester
init|=
operator|new
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|3
argument_list|,
name|MODIFIABLE
argument_list|,
name|newArrayList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
return|return
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|,
literal|2
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|assertFailure
argument_list|(
name|tester
argument_list|)
expr_stmt|;
block|}
DECL|method|testCanCatchDifferentRemoveBehaviour ()
specifier|public
name|void
name|testCanCatchDifferentRemoveBehaviour
parameter_list|()
block|{
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
name|tester
init|=
operator|new
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|3
argument_list|,
name|MODIFIABLE
argument_list|,
name|newArrayList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|assertFailure
argument_list|(
name|tester
argument_list|)
expr_stmt|;
block|}
DECL|method|testUnknownOrder ()
specifier|public
name|void
name|testUnknownOrder
parameter_list|()
block|{
operator|new
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|3
argument_list|,
name|MODIFIABLE
argument_list|,
name|newArrayList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|UNKNOWN_ORDER
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
return|return
name|newArrayList
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testUnknownOrderUnrecognizedElement ()
specifier|public
name|void
name|testUnknownOrderUnrecognizedElement
parameter_list|()
block|{
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
name|tester
init|=
operator|new
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|3
argument_list|,
name|MODIFIABLE
argument_list|,
name|newArrayList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|50
argument_list|)
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|UNKNOWN_ORDER
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
return|return
name|newArrayList
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|3
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|assertFailure
argument_list|(
name|tester
argument_list|)
expr_stmt|;
block|}
comment|/**    * This Iterator wraps another iterator and gives it a bug found    * in JDK6.    *    *<p>This bug is this: if you create an iterator from a TreeSet    * and call next() on that iterator when hasNext() is false, so    * that next() throws a NoSuchElementException, then subsequent    * calls to remove() will incorrectly throw an IllegalStateException,    * instead of removing the last element returned.    *    *<p>See    *<a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6529795">    * Sun bug 6529795</a>    */
DECL|class|IteratorWithSunJavaBug6529795
specifier|static
class|class
name|IteratorWithSunJavaBug6529795
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Iterator
argument_list|<
name|T
argument_list|>
block|{
DECL|field|iterator
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
decl_stmt|;
DECL|field|nextThrewException
name|boolean
name|nextThrewException
decl_stmt|;
DECL|method|IteratorWithSunJavaBug6529795 (Iterator<T> iterator)
name|IteratorWithSunJavaBug6529795
parameter_list|(
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|)
block|{
name|this
operator|.
name|iterator
operator|=
name|iterator
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|iterator
operator|.
name|hasNext
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|T
name|next
parameter_list|()
block|{
try|try
block|{
return|return
name|iterator
operator|.
name|next
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|e
parameter_list|)
block|{
name|nextThrewException
operator|=
literal|true
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
if|if
condition|(
name|nextThrewException
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testCanCatchSunJavaBug6529795InTargetIterator ()
specifier|public
name|void
name|testCanCatchSunJavaBug6529795InTargetIterator
parameter_list|()
block|{
try|try
block|{
comment|/* Choose 4 steps to get sequence [next, next, next, remove] */
operator|new
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|4
argument_list|,
name|MODIFIABLE
argument_list|,
name|newArrayList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|iterator
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
operator|new
name|IteratorWithSunJavaBug6529795
argument_list|<>
argument_list|(
name|iterator
argument_list|)
return|;
block|}
block|}
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|e
parameter_list|)
block|{
return|return;
block|}
name|fail
argument_list|(
literal|"Should have caught jdk6 bug in target iterator"
argument_list|)
expr_stmt|;
block|}
DECL|field|STEPS
specifier|private
specifier|static
specifier|final
name|int
name|STEPS
init|=
literal|3
decl_stmt|;
DECL|class|TesterThatCountsCalls
specifier|static
class|class
name|TesterThatCountsCalls
extends|extends
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
block|{
DECL|method|TesterThatCountsCalls ()
name|TesterThatCountsCalls
parameter_list|()
block|{
name|super
argument_list|(
name|STEPS
argument_list|,
name|MODIFIABLE
argument_list|,
name|newArrayList
argument_list|(
literal|1
argument_list|)
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|)
expr_stmt|;
block|}
DECL|field|numCallsToNewTargetIterator
name|int
name|numCallsToNewTargetIterator
decl_stmt|;
DECL|field|numCallsToVerify
name|int
name|numCallsToVerify
decl_stmt|;
DECL|method|newTargetIterator ()
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
name|numCallsToNewTargetIterator
operator|++
expr_stmt|;
return|return
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|1
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
DECL|method|verify (List<Integer> elements)
annotation|@
name|Override
specifier|protected
name|void
name|verify
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|elements
parameter_list|)
block|{
name|numCallsToVerify
operator|++
expr_stmt|;
name|super
operator|.
name|verify
argument_list|(
name|elements
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testVerifyGetsCalled ()
specifier|public
name|void
name|testVerifyGetsCalled
parameter_list|()
block|{
name|TesterThatCountsCalls
name|tester
init|=
operator|new
name|TesterThatCountsCalls
argument_list|()
decl_stmt|;
name|tester
operator|.
name|test
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should have verified once per stimulus executed"
argument_list|,
name|tester
operator|.
name|numCallsToVerify
argument_list|,
name|tester
operator|.
name|numCallsToNewTargetIterator
operator|*
name|STEPS
argument_list|)
expr_stmt|;
block|}
DECL|method|testVerifyCanThrowAssertionThatFailsTest ()
specifier|public
name|void
name|testVerifyCanThrowAssertionThatFailsTest
parameter_list|()
block|{
specifier|final
name|String
name|message
init|=
literal|"Important info about why verify failed"
decl_stmt|;
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
name|tester
init|=
operator|new
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|1
argument_list|,
name|MODIFIABLE
argument_list|,
name|newArrayList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
return|return
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|verify
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|elements
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionFailedError
argument_list|(
name|message
argument_list|)
throw|;
block|}
block|}
decl_stmt|;
name|AssertionFailedError
name|actual
init|=
literal|null
decl_stmt|;
try|try
block|{
name|tester
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|e
parameter_list|)
block|{
name|actual
operator|=
name|e
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
literal|"verify() should be able to cause test failure"
argument_list|,
name|actual
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"AssertionFailedError should have info about why test failed"
argument_list|,
name|actual
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testMissingException ()
specifier|public
name|void
name|testMissingException
parameter_list|()
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|emptyList
init|=
name|newArrayList
argument_list|()
decl_stmt|;
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
name|tester
init|=
operator|new
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|1
argument_list|,
name|MODIFIABLE
argument_list|,
name|emptyList
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
return|return
operator|new
name|Iterator
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
comment|// We should throw here, but we won't!
block|}
annotation|@
name|Override
specifier|public
name|Integer
name|next
parameter_list|()
block|{
comment|// We should throw here, but we won't!
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
return|;
block|}
block|}
decl_stmt|;
name|assertFailure
argument_list|(
name|tester
argument_list|)
expr_stmt|;
block|}
DECL|method|testUnexpectedException ()
specifier|public
name|void
name|testUnexpectedException
parameter_list|()
block|{
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
name|tester
init|=
operator|new
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|1
argument_list|,
name|MODIFIABLE
argument_list|,
name|newArrayList
argument_list|(
literal|1
argument_list|)
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
return|return
operator|new
name|ThrowingIterator
argument_list|<>
argument_list|(
operator|new
name|IllegalStateException
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|assertFailure
argument_list|(
name|tester
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimilarException ()
specifier|public
name|void
name|testSimilarException
parameter_list|()
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|emptyList
init|=
name|emptyList
argument_list|()
decl_stmt|;
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
name|tester
init|=
operator|new
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|1
argument_list|,
name|MODIFIABLE
argument_list|,
name|emptyList
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
return|return
operator|new
name|Iterator
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|()
block|{
comment|/* subclass */
block|}
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Integer
name|next
parameter_list|()
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
block|{
comment|/* subclass */
block|}
throw|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
return|;
block|}
block|}
decl_stmt|;
name|tester
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testMismatchedException ()
specifier|public
name|void
name|testMismatchedException
parameter_list|()
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|emptyList
init|=
name|emptyList
argument_list|()
decl_stmt|;
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
name|tester
init|=
operator|new
name|IteratorTester
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|1
argument_list|,
name|MODIFIABLE
argument_list|,
name|emptyList
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
return|return
operator|new
name|Iterator
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
comment|// Wrong exception type.
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Integer
name|next
parameter_list|()
block|{
comment|// Wrong exception type.
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
return|;
block|}
block|}
decl_stmt|;
name|assertFailure
argument_list|(
name|tester
argument_list|)
expr_stmt|;
block|}
DECL|method|assertFailure (IteratorTester<?> tester)
specifier|private
specifier|static
name|void
name|assertFailure
parameter_list|(
name|IteratorTester
argument_list|<
name|?
argument_list|>
name|tester
parameter_list|)
block|{
try|try
block|{
name|tester
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
return|return;
block|}
name|fail
argument_list|()
expr_stmt|;
block|}
DECL|class|ThrowingIterator
specifier|private
specifier|static
specifier|final
class|class
name|ThrowingIterator
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Iterator
argument_list|<
name|E
argument_list|>
block|{
DECL|field|ex
specifier|private
specifier|final
name|RuntimeException
name|ex
decl_stmt|;
DECL|method|ThrowingIterator (RuntimeException ex)
specifier|private
name|ThrowingIterator
parameter_list|(
name|RuntimeException
name|ex
parameter_list|)
block|{
name|this
operator|.
name|ex
operator|=
name|ex
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
comment|// IteratorTester doesn't expect exceptions for hasNext().
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|E
name|next
parameter_list|()
block|{
name|ex
operator|.
name|fillInStackTrace
argument_list|()
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|ex
operator|.
name|fillInStackTrace
argument_list|()
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
block|}
end_class

end_unit

