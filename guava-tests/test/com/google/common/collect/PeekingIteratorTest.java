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
name|Iterators
operator|.
name|peekingIterator
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
name|UNMODIFIABLE
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
name|annotations
operator|.
name|GwtIncompatible
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
name|IteratorTester
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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

begin_comment
comment|/**   * Unit test for {@link PeekingIterator}.   *   * @author Mick Killianey   */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// No serialization is used in this test
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|PeekingIteratorTest
specifier|public
class|class
name|PeekingIteratorTest
extends|extends
name|TestCase
block|{
comment|/**    * Version of {@link IteratorTester} that compares an iterator over    * a given collection of elements (used as the reference iterator)    * against a {@code PeekingIterator} that *wraps* such an iterator    * (used as the target iterator).    *    *<p>This IteratorTester makes copies of the master so that it can    * later verify that {@link PeekingIterator#remove()} removes the    * same elements as the reference's iterator {@code #remove()}.    */
DECL|class|PeekingIteratorTester
specifier|private
specifier|static
class|class
name|PeekingIteratorTester
parameter_list|<
name|T
parameter_list|>
extends|extends
name|IteratorTester
argument_list|<
name|T
argument_list|>
block|{
DECL|field|master
specifier|private
name|Iterable
argument_list|<
name|T
argument_list|>
name|master
decl_stmt|;
DECL|field|targetList
specifier|private
name|List
argument_list|<
name|T
argument_list|>
name|targetList
decl_stmt|;
DECL|method|PeekingIteratorTester (Collection<T> master)
specifier|public
name|PeekingIteratorTester
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|master
parameter_list|)
block|{
name|super
argument_list|(
name|master
operator|.
name|size
argument_list|()
operator|+
literal|3
argument_list|,
name|MODIFIABLE
argument_list|,
name|master
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|)
expr_stmt|;
name|this
operator|.
name|master
operator|=
name|master
expr_stmt|;
block|}
DECL|method|newTargetIterator ()
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|T
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
comment|// make copy from master to verify later
name|targetList
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|master
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
init|=
name|targetList
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
name|Iterators
operator|.
name|peekingIterator
argument_list|(
name|iterator
argument_list|)
return|;
block|}
DECL|method|verify (List<T> elements)
annotation|@
name|Override
specifier|protected
name|void
name|verify
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|elements
parameter_list|)
block|{
comment|// verify same objects were removed from reference and target
name|assertEquals
argument_list|(
name|elements
argument_list|,
name|targetList
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|actsLikeIteratorHelper (final List<T> list)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|void
name|actsLikeIteratorHelper
parameter_list|(
specifier|final
name|List
argument_list|<
name|T
argument_list|>
name|list
parameter_list|)
block|{
comment|// Check with modifiable copies of the list
operator|new
name|PeekingIteratorTester
argument_list|<
name|T
argument_list|>
argument_list|(
name|list
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
comment|// Check with unmodifiable lists
operator|new
name|IteratorTester
argument_list|<
name|T
argument_list|>
argument_list|(
name|list
operator|.
name|size
argument_list|()
operator|*
literal|2
operator|+
literal|2
argument_list|,
name|UNMODIFIABLE
argument_list|,
name|list
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
name|T
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
init|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|list
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
name|Iterators
operator|.
name|peekingIterator
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
DECL|method|testPeekingIteratorBehavesLikeIteratorOnEmptyIterable ()
specifier|public
name|void
name|testPeekingIteratorBehavesLikeIteratorOnEmptyIterable
parameter_list|()
block|{
name|actsLikeIteratorHelper
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testPeekingIteratorBehavesLikeIteratorOnSingletonIterable ()
specifier|public
name|void
name|testPeekingIteratorBehavesLikeIteratorOnSingletonIterable
parameter_list|()
block|{
name|actsLikeIteratorHelper
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// TODO(cpovirk): instead of skipping, use a smaller number of steps
annotation|@
name|GwtIncompatible
argument_list|(
literal|"works but takes 5 minutes to run"
argument_list|)
DECL|method|testPeekingIteratorBehavesLikeIteratorOnThreeElementIterable ()
specifier|public
name|void
name|testPeekingIteratorBehavesLikeIteratorOnThreeElementIterable
parameter_list|()
block|{
name|actsLikeIteratorHelper
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|"C"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"works but takes 5 minutes to run"
argument_list|)
DECL|method|testPeekingIteratorAcceptsNullElements ()
specifier|public
name|void
name|testPeekingIteratorAcceptsNullElements
parameter_list|()
block|{
name|actsLikeIteratorHelper
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|null
argument_list|,
literal|"A"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testPeekOnEmptyList ()
specifier|public
name|void
name|testPeekOnEmptyList
parameter_list|()
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|iterator
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|PeekingIterator
argument_list|<
name|?
argument_list|>
name|peekingIterator
init|=
name|Iterators
operator|.
name|peekingIterator
argument_list|(
name|iterator
argument_list|)
decl_stmt|;
try|try
block|{
name|peekingIterator
operator|.
name|peek
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw NoSuchElementException if nothing to peek()"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|e
parameter_list|)
block|{
comment|/* expected */
block|}
block|}
DECL|method|testPeekDoesntChangeIteration ()
specifier|public
name|void
name|testPeekDoesntChangeIteration
parameter_list|()
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|"C"
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|iterator
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|PeekingIterator
argument_list|<
name|?
argument_list|>
name|peekingIterator
init|=
name|Iterators
operator|.
name|peekingIterator
argument_list|(
name|iterator
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should be able to peek() at first element"
argument_list|,
literal|"A"
argument_list|,
name|peekingIterator
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should be able to peek() first element multiple times"
argument_list|,
literal|"A"
argument_list|,
name|peekingIterator
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"next() should still return first element after peeking"
argument_list|,
literal|"A"
argument_list|,
name|peekingIterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should be able to peek() at middle element"
argument_list|,
literal|"B"
argument_list|,
name|peekingIterator
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should be able to peek() middle element multiple times"
argument_list|,
literal|"B"
argument_list|,
name|peekingIterator
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"next() should still return middle element after peeking"
argument_list|,
literal|"B"
argument_list|,
name|peekingIterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should be able to peek() at last element"
argument_list|,
literal|"C"
argument_list|,
name|peekingIterator
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should be able to peek() last element multiple times"
argument_list|,
literal|"C"
argument_list|,
name|peekingIterator
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"next() should still return last element after peeking"
argument_list|,
literal|"C"
argument_list|,
name|peekingIterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|peekingIterator
operator|.
name|peek
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw exception if no next to peek()"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|e
parameter_list|)
block|{
comment|/* expected */
block|}
try|try
block|{
name|peekingIterator
operator|.
name|peek
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should continue to throw exception if no next to peek()"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|e
parameter_list|)
block|{
comment|/* expected */
block|}
try|try
block|{
name|peekingIterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"next() should still throw exception after the end of iteration"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|e
parameter_list|)
block|{
comment|/* expected */
block|}
block|}
DECL|method|testCantRemoveAfterPeek ()
specifier|public
name|void
name|testCantRemoveAfterPeek
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|"C"
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|PeekingIterator
argument_list|<
name|?
argument_list|>
name|peekingIterator
init|=
name|Iterators
operator|.
name|peekingIterator
argument_list|(
name|iterator
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|peekingIterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|peekingIterator
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
comment|/* Should complain on attempt to remove() after peek(). */
try|try
block|{
name|peekingIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"remove() should throw IllegalStateException after a peek()"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|/* expected */
block|}
name|assertEquals
argument_list|(
literal|"After remove() throws exception, peek should still be ok"
argument_list|,
literal|"B"
argument_list|,
name|peekingIterator
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
comment|/* Should recover to be able to remove() after next(). */
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|peekingIterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|peekingIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should have removed an element"
argument_list|,
literal|2
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Second element should be gone"
argument_list|,
name|list
operator|.
name|contains
argument_list|(
literal|"B"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|ThrowsAtEndException
specifier|static
class|class
name|ThrowsAtEndException
extends|extends
name|RuntimeException
block|{
comment|/* nothing */
block|}
comment|/**     * This Iterator claims to have more elements than the underlying     * iterable, but when you try to fetch the extra elements, it throws     * an unchecked exception.     */
DECL|class|ThrowsAtEndIterator
specifier|static
class|class
name|ThrowsAtEndIterator
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Iterator
argument_list|<
name|E
argument_list|>
block|{
DECL|field|iterator
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
decl_stmt|;
DECL|method|ThrowsAtEndIterator (Iterable<E> iterable)
specifier|public
name|ThrowsAtEndIterator
parameter_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|iterable
parameter_list|)
block|{
name|this
operator|.
name|iterator
operator|=
name|iterable
operator|.
name|iterator
argument_list|()
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
literal|true
return|;
comment|// pretend that you have more...
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|E
name|next
parameter_list|()
block|{
comment|// ...but throw an unchecked exception when you ask for it.
if|if
condition|(
operator|!
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ThrowsAtEndException
argument_list|()
throw|;
block|}
return|return
name|iterator
operator|.
name|next
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testPeekingIteratorDoesntAdvancePrematurely ()
specifier|public
name|void
name|testPeekingIteratorDoesntAdvancePrematurely
parameter_list|()
throws|throws
name|Exception
block|{
comment|/*      * This test will catch problems where the underlying iterator      * throws a RuntimeException when retrieving the nth element.      *      * If the PeekingIterator is caching elements too aggressively,      * it may throw the exception on the (n-1)th element (oops!).      */
comment|/* Checks the case where the first element throws an exception. */
name|List
argument_list|<
name|Integer
argument_list|>
name|list
init|=
name|emptyList
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|iterator
init|=
name|peekingIterator
argument_list|(
operator|new
name|ThrowsAtEndIterator
argument_list|<
name|Integer
argument_list|>
argument_list|(
name|list
argument_list|)
argument_list|)
decl_stmt|;
name|assertNextThrows
argument_list|(
name|iterator
argument_list|)
expr_stmt|;
comment|/* Checks the case where a later element throws an exception. */
name|list
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|iterator
operator|=
name|peekingIterator
argument_list|(
operator|new
name|ThrowsAtEndIterator
argument_list|<
name|Integer
argument_list|>
argument_list|(
name|list
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|iterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|iterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertNextThrows
argument_list|(
name|iterator
argument_list|)
expr_stmt|;
block|}
DECL|method|assertNextThrows (Iterator<?> iterator)
specifier|private
name|void
name|assertNextThrows
parameter_list|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|iterator
parameter_list|)
block|{
try|try
block|{
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ThrowsAtEndException
name|expected
parameter_list|)
block|{     }
block|}
block|}
end_class

end_unit

