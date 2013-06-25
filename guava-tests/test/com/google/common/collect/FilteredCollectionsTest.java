begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|truth0
operator|.
name|Truth
operator|.
name|ASSERT
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
name|Predicate
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
name|Predicates
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
name|testing
operator|.
name|EqualsTester
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
name|NavigableSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_comment
comment|/**  * Tests for filtered collection views.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|FilteredCollectionsTest
specifier|public
class|class
name|FilteredCollectionsTest
extends|extends
name|TestCase
block|{
DECL|field|EVEN
specifier|private
specifier|static
specifier|final
name|Predicate
argument_list|<
name|Integer
argument_list|>
name|EVEN
init|=
operator|new
name|Predicate
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|Integer
name|input
parameter_list|)
block|{
return|return
name|input
operator|%
literal|2
operator|==
literal|0
return|;
block|}
block|}
decl_stmt|;
DECL|field|PRIME_DIGIT
specifier|private
specifier|static
specifier|final
name|Predicate
argument_list|<
name|Integer
argument_list|>
name|PRIME_DIGIT
init|=
name|Predicates
operator|.
name|in
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|,
literal|5
argument_list|,
literal|7
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|SAMPLE_INPUTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|?
extends|extends
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|SAMPLE_INPUTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|ImmutableList
operator|.
expr|<
name|Integer
operator|>
name|of
argument_list|()
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|1
argument_list|)
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|2
argument_list|)
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|3
argument_list|,
literal|5
argument_list|)
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|2
argument_list|,
literal|4
argument_list|)
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|5
argument_list|,
literal|6
argument_list|,
literal|8
argument_list|,
literal|9
argument_list|)
argument_list|)
decl_stmt|;
comment|/*    * We have a whole series of abstract test classes that "stack", so e.g. the tests for filtered    * NavigableSets inherit the tests for filtered Iterables, Collections, Sets, and SortedSets. The    * actual implementation tests are further down.    */
DECL|class|AbstractFilteredIterableTest
specifier|public
specifier|static
specifier|abstract
class|class
name|AbstractFilteredIterableTest
parameter_list|<
name|C
extends|extends
name|Iterable
parameter_list|<
name|Integer
parameter_list|>
parameter_list|>
extends|extends
name|TestCase
block|{
DECL|method|createUnfiltered (Iterable<Integer> contents)
specifier|abstract
name|C
name|createUnfiltered
parameter_list|(
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|contents
parameter_list|)
function_decl|;
DECL|method|filter (C elements, Predicate<? super Integer> predicate)
specifier|abstract
name|C
name|filter
parameter_list|(
name|C
name|elements
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|Integer
argument_list|>
name|predicate
parameter_list|)
function_decl|;
DECL|method|testIterationOrderPreserved ()
specifier|public
name|void
name|testIterationOrderPreserved
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
name|C
name|unfiltered
init|=
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
decl_stmt|;
name|C
name|filtered
init|=
name|filter
argument_list|(
name|unfiltered
argument_list|,
name|EVEN
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|Integer
argument_list|>
name|filteredItr
init|=
name|filtered
operator|.
name|iterator
argument_list|()
decl_stmt|;
for|for
control|(
name|Integer
name|i
range|:
name|unfiltered
control|)
block|{
if|if
condition|(
name|EVEN
operator|.
name|apply
argument_list|(
name|i
argument_list|)
condition|)
block|{
name|assertTrue
argument_list|(
name|filteredItr
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|i
argument_list|,
name|filteredItr
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertFalse
argument_list|(
name|filteredItr
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|AbstractFilteredCollectionTest
specifier|public
specifier|static
specifier|abstract
class|class
name|AbstractFilteredCollectionTest
parameter_list|<
name|C
extends|extends
name|Collection
parameter_list|<
name|Integer
parameter_list|>
parameter_list|>
extends|extends
name|AbstractFilteredIterableTest
argument_list|<
name|C
argument_list|>
block|{
DECL|method|testReadsThroughAdd ()
specifier|public
name|void
name|testReadsThroughAdd
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
name|C
name|unfiltered
init|=
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
decl_stmt|;
name|C
name|filterThenAdd
init|=
name|filter
argument_list|(
name|unfiltered
argument_list|,
name|EVEN
argument_list|)
decl_stmt|;
name|unfiltered
operator|.
name|add
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|target
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|contents
argument_list|)
decl_stmt|;
name|target
operator|.
name|add
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|C
name|addThenFilter
init|=
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|target
argument_list|)
argument_list|,
name|EVEN
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|filterThenAdd
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactlyAs
argument_list|(
name|addThenFilter
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testAdd ()
specifier|public
name|void
name|testAdd
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
for|for
control|(
name|int
name|toAdd
init|=
literal|0
init|;
name|toAdd
operator|<
literal|10
condition|;
name|toAdd
operator|++
control|)
block|{
name|boolean
name|expectedResult
init|=
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
operator|.
name|add
argument_list|(
name|toAdd
argument_list|)
decl_stmt|;
name|C
name|filtered
init|=
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
name|expectedResult
argument_list|,
name|filtered
operator|.
name|add
argument_list|(
name|toAdd
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|EVEN
operator|.
name|apply
argument_list|(
name|toAdd
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertFalse
argument_list|(
name|EVEN
operator|.
name|apply
argument_list|(
name|toAdd
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|testRemove ()
specifier|public
name|void
name|testRemove
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
for|for
control|(
name|int
name|toRemove
init|=
literal|0
init|;
name|toRemove
operator|<
literal|10
condition|;
name|toRemove
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|contents
operator|.
name|contains
argument_list|(
name|toRemove
argument_list|)
operator|&&
name|EVEN
operator|.
name|apply
argument_list|(
name|toRemove
argument_list|)
argument_list|,
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
operator|.
name|remove
argument_list|(
name|toRemove
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testContains ()
specifier|public
name|void
name|testContains
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|EVEN
operator|.
name|apply
argument_list|(
name|i
argument_list|)
operator|&&
name|contents
operator|.
name|contains
argument_list|(
name|i
argument_list|)
argument_list|,
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
operator|.
name|contains
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testContainsOnDifferentType ()
specifier|public
name|void
name|testContainsOnDifferentType
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
name|assertFalse
argument_list|(
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testAddAllFailsAtomically ()
specifier|public
name|void
name|testAddAllFailsAtomically
parameter_list|()
block|{
name|ImmutableList
argument_list|<
name|Integer
argument_list|>
name|toAdd
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|2
argument_list|,
literal|4
argument_list|,
literal|3
argument_list|)
decl_stmt|;
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
name|C
name|filtered
init|=
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
decl_stmt|;
name|C
name|filteredToModify
init|=
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
decl_stmt|;
try|try
block|{
name|filteredToModify
operator|.
name|addAll
argument_list|(
name|toAdd
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
block|{         }
name|ASSERT
operator|.
name|that
argument_list|(
name|filteredToModify
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactlyAs
argument_list|(
name|filtered
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testAddToFilterFiltered ()
specifier|public
name|void
name|testAddToFilterFiltered
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
name|C
name|unfiltered
init|=
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
decl_stmt|;
name|C
name|filtered1
init|=
name|filter
argument_list|(
name|unfiltered
argument_list|,
name|EVEN
argument_list|)
decl_stmt|;
name|C
name|filtered2
init|=
name|filter
argument_list|(
name|filtered1
argument_list|,
name|PRIME_DIGIT
argument_list|)
decl_stmt|;
try|try
block|{
name|filtered2
operator|.
name|add
argument_list|(
literal|4
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
block|{}
try|try
block|{
name|filtered2
operator|.
name|add
argument_list|(
literal|3
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
block|{}
name|filtered2
operator|.
name|add
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testClearFilterFiltered ()
specifier|public
name|void
name|testClearFilterFiltered
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
name|C
name|unfiltered
init|=
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
decl_stmt|;
name|C
name|filtered1
init|=
name|filter
argument_list|(
name|unfiltered
argument_list|,
name|EVEN
argument_list|)
decl_stmt|;
name|C
name|filtered2
init|=
name|filter
argument_list|(
name|filtered1
argument_list|,
name|PRIME_DIGIT
argument_list|)
decl_stmt|;
name|C
name|inverseFiltered
init|=
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|Predicates
operator|.
name|not
argument_list|(
name|Predicates
operator|.
name|and
argument_list|(
name|EVEN
argument_list|,
name|PRIME_DIGIT
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|filtered2
operator|.
name|clear
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|unfiltered
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactlyAs
argument_list|(
name|inverseFiltered
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|AbstractFilteredSetTest
specifier|public
specifier|static
specifier|abstract
class|class
name|AbstractFilteredSetTest
parameter_list|<
name|C
extends|extends
name|Set
parameter_list|<
name|Integer
parameter_list|>
parameter_list|>
extends|extends
name|AbstractFilteredCollectionTest
argument_list|<
name|C
argument_list|>
block|{
DECL|method|testEqualsAndHashCode ()
specifier|public
name|void
name|testEqualsAndHashCode
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
name|Set
argument_list|<
name|Integer
argument_list|>
name|expected
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Integer
name|i
range|:
name|contents
control|)
block|{
if|if
condition|(
name|EVEN
operator|.
name|apply
argument_list|(
name|i
argument_list|)
condition|)
block|{
name|expected
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|expected
argument_list|,
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|class|AbstractFilteredSortedSetTest
specifier|public
specifier|static
specifier|abstract
class|class
name|AbstractFilteredSortedSetTest
parameter_list|<
name|C
extends|extends
name|SortedSet
parameter_list|<
name|Integer
parameter_list|>
parameter_list|>
extends|extends
name|AbstractFilteredSetTest
argument_list|<
name|C
argument_list|>
block|{
DECL|method|testFirst ()
specifier|public
name|void
name|testFirst
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
name|C
name|filtered
init|=
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
decl_stmt|;
try|try
block|{
name|Integer
name|first
init|=
name|filtered
operator|.
name|first
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|filtered
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|min
argument_list|(
name|filtered
argument_list|)
argument_list|,
name|first
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|filtered
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testLast ()
specifier|public
name|void
name|testLast
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
name|C
name|filtered
init|=
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
decl_stmt|;
try|try
block|{
name|Integer
name|first
init|=
name|filtered
operator|.
name|last
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|filtered
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|max
argument_list|(
name|filtered
argument_list|)
argument_list|,
name|first
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|filtered
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testHeadSet ()
specifier|public
name|void
name|testHeadSet
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|filter
argument_list|(
operator|(
name|C
operator|)
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
operator|.
name|headSet
argument_list|(
name|i
argument_list|)
argument_list|,
name|EVEN
argument_list|)
argument_list|,
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
operator|.
name|headSet
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testTailSet ()
specifier|public
name|void
name|testTailSet
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|filter
argument_list|(
operator|(
name|C
operator|)
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
operator|.
name|tailSet
argument_list|(
name|i
argument_list|)
argument_list|,
name|EVEN
argument_list|)
argument_list|,
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
operator|.
name|tailSet
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testSubSet ()
specifier|public
name|void
name|testSubSet
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
for|for
control|(
name|int
name|j
init|=
name|i
init|;
name|j
operator|<
literal|10
condition|;
name|j
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|filter
argument_list|(
operator|(
name|C
operator|)
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
operator|.
name|subSet
argument_list|(
name|i
argument_list|,
name|j
argument_list|)
argument_list|,
name|EVEN
argument_list|)
argument_list|,
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
operator|.
name|subSet
argument_list|(
name|i
argument_list|,
name|j
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
DECL|class|AbstractFilteredNavigableSetTest
specifier|public
specifier|static
specifier|abstract
class|class
name|AbstractFilteredNavigableSetTest
extends|extends
name|AbstractFilteredSortedSetTest
argument_list|<
name|NavigableSet
argument_list|<
name|Integer
argument_list|>
argument_list|>
block|{
DECL|method|testNavigableHeadSet ()
specifier|public
name|void
name|testNavigableHeadSet
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
for|for
control|(
name|boolean
name|inclusive
range|:
name|ImmutableList
operator|.
name|of
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
control|)
block|{
name|assertEquals
argument_list|(
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
operator|.
name|headSet
argument_list|(
name|i
argument_list|,
name|inclusive
argument_list|)
argument_list|,
name|EVEN
argument_list|)
argument_list|,
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
operator|.
name|headSet
argument_list|(
name|i
argument_list|,
name|inclusive
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|testNavigableTailSet ()
specifier|public
name|void
name|testNavigableTailSet
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
for|for
control|(
name|boolean
name|inclusive
range|:
name|ImmutableList
operator|.
name|of
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
control|)
block|{
name|assertEquals
argument_list|(
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
operator|.
name|tailSet
argument_list|(
name|i
argument_list|,
name|inclusive
argument_list|)
argument_list|,
name|EVEN
argument_list|)
argument_list|,
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
operator|.
name|tailSet
argument_list|(
name|i
argument_list|,
name|inclusive
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|testNavigableSubSet ()
specifier|public
name|void
name|testNavigableSubSet
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
for|for
control|(
name|int
name|j
init|=
name|i
operator|+
literal|1
init|;
name|j
operator|<
literal|10
condition|;
name|j
operator|++
control|)
block|{
for|for
control|(
name|boolean
name|fromInclusive
range|:
name|ImmutableList
operator|.
name|of
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
control|)
block|{
for|for
control|(
name|boolean
name|toInclusive
range|:
name|ImmutableList
operator|.
name|of
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
control|)
block|{
name|NavigableSet
argument_list|<
name|Integer
argument_list|>
name|filterSubset
init|=
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
operator|.
name|subSet
argument_list|(
name|i
argument_list|,
name|fromInclusive
argument_list|,
name|j
argument_list|,
name|toInclusive
argument_list|)
argument_list|,
name|EVEN
argument_list|)
decl_stmt|;
name|NavigableSet
argument_list|<
name|Integer
argument_list|>
name|subsetFilter
init|=
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
operator|.
name|subSet
argument_list|(
name|i
argument_list|,
name|fromInclusive
argument_list|,
name|j
argument_list|,
name|toInclusive
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|filterSubset
argument_list|,
name|subsetFilter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
DECL|method|testDescendingSet ()
specifier|public
name|void
name|testDescendingSet
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
name|NavigableSet
argument_list|<
name|Integer
argument_list|>
name|filtered
init|=
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
decl_stmt|;
name|NavigableSet
argument_list|<
name|Integer
argument_list|>
name|unfiltered
init|=
name|createUnfiltered
argument_list|(
name|filtered
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|filtered
operator|.
name|descendingSet
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactlyAs
argument_list|(
name|unfiltered
operator|.
name|descendingSet
argument_list|()
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testPollFirst ()
specifier|public
name|void
name|testPollFirst
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
name|NavigableSet
argument_list|<
name|Integer
argument_list|>
name|filtered
init|=
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
decl_stmt|;
name|NavigableSet
argument_list|<
name|Integer
argument_list|>
name|unfiltered
init|=
name|createUnfiltered
argument_list|(
name|filtered
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|unfiltered
operator|.
name|pollFirst
argument_list|()
argument_list|,
name|filtered
operator|.
name|pollFirst
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|unfiltered
argument_list|,
name|filtered
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testPollLast ()
specifier|public
name|void
name|testPollLast
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
name|NavigableSet
argument_list|<
name|Integer
argument_list|>
name|filtered
init|=
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
decl_stmt|;
name|NavigableSet
argument_list|<
name|Integer
argument_list|>
name|unfiltered
init|=
name|createUnfiltered
argument_list|(
name|filtered
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|unfiltered
operator|.
name|pollLast
argument_list|()
argument_list|,
name|filtered
operator|.
name|pollLast
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|unfiltered
argument_list|,
name|filtered
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testNavigation ()
specifier|public
name|void
name|testNavigation
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|contents
range|:
name|SAMPLE_INPUTS
control|)
block|{
name|NavigableSet
argument_list|<
name|Integer
argument_list|>
name|filtered
init|=
name|filter
argument_list|(
name|createUnfiltered
argument_list|(
name|contents
argument_list|)
argument_list|,
name|EVEN
argument_list|)
decl_stmt|;
name|NavigableSet
argument_list|<
name|Integer
argument_list|>
name|unfiltered
init|=
name|createUnfiltered
argument_list|(
name|filtered
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|unfiltered
operator|.
name|lower
argument_list|(
name|i
argument_list|)
argument_list|,
name|filtered
operator|.
name|lower
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|unfiltered
operator|.
name|floor
argument_list|(
name|i
argument_list|)
argument_list|,
name|filtered
operator|.
name|floor
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|unfiltered
operator|.
name|ceiling
argument_list|(
name|i
argument_list|)
argument_list|,
name|filtered
operator|.
name|ceiling
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|unfiltered
operator|.
name|higher
argument_list|(
name|i
argument_list|)
argument_list|,
name|filtered
operator|.
name|higher
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// implementation tests
DECL|class|IterablesFilterArrayListTest
specifier|public
specifier|static
specifier|final
class|class
name|IterablesFilterArrayListTest
extends|extends
name|AbstractFilteredIterableTest
argument_list|<
name|Iterable
argument_list|<
name|Integer
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|createUnfiltered (Iterable<Integer> contents)
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|createUnfiltered
parameter_list|(
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|Lists
operator|.
name|newArrayList
argument_list|(
name|contents
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|filter (Iterable<Integer> elements, Predicate<? super Integer> predicate)
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|filter
parameter_list|(
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|elements
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|Integer
argument_list|>
name|predicate
parameter_list|)
block|{
return|return
name|Iterables
operator|.
name|filter
argument_list|(
name|elements
argument_list|,
name|predicate
argument_list|)
return|;
block|}
block|}
DECL|class|Collections2FilterArrayListTest
specifier|public
specifier|static
specifier|final
class|class
name|Collections2FilterArrayListTest
extends|extends
name|AbstractFilteredCollectionTest
argument_list|<
name|Collection
argument_list|<
name|Integer
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|createUnfiltered (Iterable<Integer> contents)
name|Collection
argument_list|<
name|Integer
argument_list|>
name|createUnfiltered
parameter_list|(
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|Lists
operator|.
name|newArrayList
argument_list|(
name|contents
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|filter (Collection<Integer> elements, Predicate<? super Integer> predicate)
name|Collection
argument_list|<
name|Integer
argument_list|>
name|filter
parameter_list|(
name|Collection
argument_list|<
name|Integer
argument_list|>
name|elements
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|Integer
argument_list|>
name|predicate
parameter_list|)
block|{
return|return
name|Collections2
operator|.
name|filter
argument_list|(
name|elements
argument_list|,
name|predicate
argument_list|)
return|;
block|}
block|}
DECL|class|SetsFilterHashSetTest
specifier|public
specifier|static
specifier|final
class|class
name|SetsFilterHashSetTest
extends|extends
name|AbstractFilteredSetTest
argument_list|<
name|Set
argument_list|<
name|Integer
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|createUnfiltered (Iterable<Integer> contents)
name|Set
argument_list|<
name|Integer
argument_list|>
name|createUnfiltered
parameter_list|(
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|Sets
operator|.
name|newHashSet
argument_list|(
name|contents
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|filter (Set<Integer> elements, Predicate<? super Integer> predicate)
name|Set
argument_list|<
name|Integer
argument_list|>
name|filter
parameter_list|(
name|Set
argument_list|<
name|Integer
argument_list|>
name|elements
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|Integer
argument_list|>
name|predicate
parameter_list|)
block|{
return|return
name|Sets
operator|.
name|filter
argument_list|(
name|elements
argument_list|,
name|predicate
argument_list|)
return|;
block|}
block|}
DECL|class|SetsFilterSortedSetTest
specifier|public
specifier|static
specifier|final
class|class
name|SetsFilterSortedSetTest
extends|extends
name|AbstractFilteredSortedSetTest
argument_list|<
name|SortedSet
argument_list|<
name|Integer
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|createUnfiltered (Iterable<Integer> contents)
name|SortedSet
argument_list|<
name|Integer
argument_list|>
name|createUnfiltered
parameter_list|(
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|contents
parameter_list|)
block|{
specifier|final
name|TreeSet
argument_list|<
name|Integer
argument_list|>
name|result
init|=
name|Sets
operator|.
name|newTreeSet
argument_list|(
name|contents
argument_list|)
decl_stmt|;
comment|// we have to make the result not Navigable
return|return
operator|new
name|ForwardingSortedSet
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|SortedSet
argument_list|<
name|Integer
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|result
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|filter (SortedSet<Integer> elements, Predicate<? super Integer> predicate)
name|SortedSet
argument_list|<
name|Integer
argument_list|>
name|filter
parameter_list|(
name|SortedSet
argument_list|<
name|Integer
argument_list|>
name|elements
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|Integer
argument_list|>
name|predicate
parameter_list|)
block|{
return|return
name|Sets
operator|.
name|filter
argument_list|(
name|elements
argument_list|,
name|predicate
argument_list|)
return|;
block|}
block|}
DECL|class|SetsFilterNavigableSetTest
specifier|public
specifier|static
specifier|final
class|class
name|SetsFilterNavigableSetTest
extends|extends
name|AbstractFilteredNavigableSetTest
block|{
annotation|@
name|Override
DECL|method|createUnfiltered (Iterable<Integer> contents)
name|NavigableSet
argument_list|<
name|Integer
argument_list|>
name|createUnfiltered
parameter_list|(
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|Sets
operator|.
name|newTreeSet
argument_list|(
name|contents
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|filter ( NavigableSet<Integer> elements, Predicate<? super Integer> predicate)
name|NavigableSet
argument_list|<
name|Integer
argument_list|>
name|filter
parameter_list|(
name|NavigableSet
argument_list|<
name|Integer
argument_list|>
name|elements
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|Integer
argument_list|>
name|predicate
parameter_list|)
block|{
return|return
name|Sets
operator|.
name|filter
argument_list|(
name|elements
argument_list|,
name|predicate
argument_list|)
return|;
block|}
block|}
comment|/** No-op test so that the class has at least one method, making Maven's test runner happy. */
DECL|method|testNoop ()
specifier|public
name|void
name|testNoop
parameter_list|()
block|{   }
block|}
end_class

end_unit

