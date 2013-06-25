begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
import|;
end_import

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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractCollection
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
name|ListIterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|RandomAccess
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

begin_comment
comment|/**  * Tests for {@code Constraints}.  *  * @author Mike Bostock  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|ConstraintsTest
specifier|public
class|class
name|ConstraintsTest
extends|extends
name|TestCase
block|{
DECL|field|TEST_ELEMENT
specifier|private
specifier|static
specifier|final
name|String
name|TEST_ELEMENT
init|=
literal|"test"
decl_stmt|;
DECL|class|TestElementException
specifier|private
specifier|static
specifier|final
class|class
name|TestElementException
extends|extends
name|IllegalArgumentException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
DECL|field|TEST_CONSTRAINT
specifier|private
specifier|static
specifier|final
name|Constraint
argument_list|<
name|String
argument_list|>
name|TEST_CONSTRAINT
init|=
operator|new
name|Constraint
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|checkElement
parameter_list|(
name|String
name|element
parameter_list|)
block|{
if|if
condition|(
name|TEST_ELEMENT
operator|.
name|equals
argument_list|(
name|element
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|TestElementException
argument_list|()
throw|;
block|}
return|return
name|element
return|;
block|}
block|}
decl_stmt|;
DECL|method|testNotNull ()
specifier|public
name|void
name|testNotNull
parameter_list|()
block|{
name|Constraint
argument_list|<
name|?
super|super
name|String
argument_list|>
name|constraint
init|=
name|Constraints
operator|.
name|notNull
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|TEST_ELEMENT
argument_list|,
name|constraint
operator|.
name|checkElement
argument_list|(
name|TEST_ELEMENT
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|constraint
operator|.
name|checkElement
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"NullPointerException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{}
name|assertEquals
argument_list|(
literal|"Not null"
argument_list|,
name|constraint
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testConstrainedCollectionLegal ()
specifier|public
name|void
name|testConstrainedCollectionLegal
parameter_list|()
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|collection
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|constrained
init|=
name|Constraints
operator|.
name|constrainedCollection
argument_list|(
name|collection
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
name|collection
operator|.
name|add
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|constrained
operator|.
name|add
argument_list|(
literal|"qux"
argument_list|)
expr_stmt|;
name|constrained
operator|.
name|addAll
argument_list|(
name|asList
argument_list|(
literal|"cat"
argument_list|,
literal|"dog"
argument_list|)
argument_list|)
expr_stmt|;
comment|/* equals and hashCode aren't defined for Collection */
name|ASSERT
operator|.
name|that
argument_list|(
name|collection
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
name|TEST_ELEMENT
argument_list|,
literal|"qux"
argument_list|,
literal|"cat"
argument_list|,
literal|"dog"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|constrained
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
name|TEST_ELEMENT
argument_list|,
literal|"qux"
argument_list|,
literal|"cat"
argument_list|,
literal|"dog"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testConstrainedCollectionIllegal ()
specifier|public
name|void
name|testConstrainedCollectionIllegal
parameter_list|()
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|collection
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|constrained
init|=
name|Constraints
operator|.
name|constrainedCollection
argument_list|(
name|collection
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
try|try
block|{
name|constrained
operator|.
name|add
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|addAll
argument_list|(
name|asList
argument_list|(
literal|"baz"
argument_list|,
name|TEST_ELEMENT
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
name|ASSERT
operator|.
name|that
argument_list|(
name|constrained
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|collection
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testConstrainedSetLegal ()
specifier|public
name|void
name|testConstrainedSetLegal
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
name|Sets
operator|.
name|newLinkedHashSet
argument_list|(
name|asList
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|constrained
init|=
name|Constraints
operator|.
name|constrainedSet
argument_list|(
name|set
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
name|set
operator|.
name|add
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|constrained
operator|.
name|add
argument_list|(
literal|"qux"
argument_list|)
expr_stmt|;
name|constrained
operator|.
name|addAll
argument_list|(
name|asList
argument_list|(
literal|"cat"
argument_list|,
literal|"dog"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|set
operator|.
name|equals
argument_list|(
name|constrained
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|constrained
operator|.
name|equals
argument_list|(
name|set
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|set
operator|.
name|toString
argument_list|()
argument_list|,
name|constrained
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|set
operator|.
name|hashCode
argument_list|()
argument_list|,
name|constrained
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|set
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
name|TEST_ELEMENT
argument_list|,
literal|"qux"
argument_list|,
literal|"cat"
argument_list|,
literal|"dog"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|constrained
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
name|TEST_ELEMENT
argument_list|,
literal|"qux"
argument_list|,
literal|"cat"
argument_list|,
literal|"dog"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testConstrainedSetIllegal ()
specifier|public
name|void
name|testConstrainedSetIllegal
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
name|Sets
operator|.
name|newLinkedHashSet
argument_list|(
name|asList
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|constrained
init|=
name|Constraints
operator|.
name|constrainedSet
argument_list|(
name|set
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
try|try
block|{
name|constrained
operator|.
name|add
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|addAll
argument_list|(
name|asList
argument_list|(
literal|"baz"
argument_list|,
name|TEST_ELEMENT
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
name|ASSERT
operator|.
name|that
argument_list|(
name|constrained
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|set
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testConstrainedSortedSetLegal ()
specifier|public
name|void
name|testConstrainedSortedSetLegal
parameter_list|()
block|{
name|SortedSet
argument_list|<
name|String
argument_list|>
name|sortedSet
init|=
name|Sets
operator|.
name|newTreeSet
argument_list|(
name|asList
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
decl_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|constrained
init|=
name|Constraints
operator|.
name|constrainedSortedSet
argument_list|(
name|sortedSet
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
name|sortedSet
operator|.
name|add
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|constrained
operator|.
name|add
argument_list|(
literal|"qux"
argument_list|)
expr_stmt|;
name|constrained
operator|.
name|addAll
argument_list|(
name|asList
argument_list|(
literal|"cat"
argument_list|,
literal|"dog"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sortedSet
operator|.
name|equals
argument_list|(
name|constrained
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|constrained
operator|.
name|equals
argument_list|(
name|sortedSet
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|sortedSet
operator|.
name|toString
argument_list|()
argument_list|,
name|constrained
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|sortedSet
operator|.
name|hashCode
argument_list|()
argument_list|,
name|constrained
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|sortedSet
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"bar"
argument_list|,
literal|"cat"
argument_list|,
literal|"dog"
argument_list|,
literal|"foo"
argument_list|,
literal|"qux"
argument_list|,
name|TEST_ELEMENT
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|constrained
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"bar"
argument_list|,
literal|"cat"
argument_list|,
literal|"dog"
argument_list|,
literal|"foo"
argument_list|,
literal|"qux"
argument_list|,
name|TEST_ELEMENT
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|constrained
operator|.
name|comparator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|constrained
operator|.
name|first
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_ELEMENT
argument_list|,
name|constrained
operator|.
name|last
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testConstrainedSortedSetIllegal ()
specifier|public
name|void
name|testConstrainedSortedSetIllegal
parameter_list|()
block|{
name|SortedSet
argument_list|<
name|String
argument_list|>
name|sortedSet
init|=
name|Sets
operator|.
name|newTreeSet
argument_list|(
name|asList
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
decl_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|constrained
init|=
name|Constraints
operator|.
name|constrainedSortedSet
argument_list|(
name|sortedSet
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
try|try
block|{
name|constrained
operator|.
name|add
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|subSet
argument_list|(
literal|"bar"
argument_list|,
literal|"foo"
argument_list|)
operator|.
name|add
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|headSet
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|add
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|tailSet
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|add
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|addAll
argument_list|(
name|asList
argument_list|(
literal|"baz"
argument_list|,
name|TEST_ELEMENT
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
name|ASSERT
operator|.
name|that
argument_list|(
name|constrained
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"bar"
argument_list|,
literal|"foo"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|sortedSet
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"bar"
argument_list|,
literal|"foo"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testConstrainedListLegal ()
specifier|public
name|void
name|testConstrainedListLegal
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
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|constrained
init|=
name|Constraints
operator|.
name|constrainedList
argument_list|(
name|list
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|constrained
operator|.
name|add
argument_list|(
literal|"qux"
argument_list|)
expr_stmt|;
name|constrained
operator|.
name|addAll
argument_list|(
name|asList
argument_list|(
literal|"cat"
argument_list|,
literal|"dog"
argument_list|)
argument_list|)
expr_stmt|;
name|constrained
operator|.
name|add
argument_list|(
literal|1
argument_list|,
literal|"cow"
argument_list|)
expr_stmt|;
name|constrained
operator|.
name|addAll
argument_list|(
literal|4
argument_list|,
name|asList
argument_list|(
literal|"box"
argument_list|,
literal|"fan"
argument_list|)
argument_list|)
expr_stmt|;
name|constrained
operator|.
name|set
argument_list|(
literal|2
argument_list|,
literal|"baz"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|list
operator|.
name|equals
argument_list|(
name|constrained
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|constrained
operator|.
name|equals
argument_list|(
name|list
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
operator|.
name|toString
argument_list|()
argument_list|,
name|constrained
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
operator|.
name|hashCode
argument_list|()
argument_list|,
name|constrained
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|list
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"foo"
argument_list|,
literal|"cow"
argument_list|,
literal|"baz"
argument_list|,
name|TEST_ELEMENT
argument_list|,
literal|"box"
argument_list|,
literal|"fan"
argument_list|,
literal|"qux"
argument_list|,
literal|"cat"
argument_list|,
literal|"dog"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|constrained
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"foo"
argument_list|,
literal|"cow"
argument_list|,
literal|"baz"
argument_list|,
name|TEST_ELEMENT
argument_list|,
literal|"box"
argument_list|,
literal|"fan"
argument_list|,
literal|"qux"
argument_list|,
literal|"cat"
argument_list|,
literal|"dog"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ListIterator
argument_list|<
name|String
argument_list|>
name|iterator
init|=
name|constrained
operator|.
name|listIterator
argument_list|()
decl_stmt|;
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|iterator
operator|.
name|set
argument_list|(
literal|"sun"
argument_list|)
expr_stmt|;
name|constrained
operator|.
name|listIterator
argument_list|(
literal|2
argument_list|)
operator|.
name|add
argument_list|(
literal|"sky"
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|list
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"sun"
argument_list|,
literal|"cow"
argument_list|,
literal|"sky"
argument_list|,
literal|"baz"
argument_list|,
name|TEST_ELEMENT
argument_list|,
literal|"box"
argument_list|,
literal|"fan"
argument_list|,
literal|"qux"
argument_list|,
literal|"cat"
argument_list|,
literal|"dog"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|constrained
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"sun"
argument_list|,
literal|"cow"
argument_list|,
literal|"sky"
argument_list|,
literal|"baz"
argument_list|,
name|TEST_ELEMENT
argument_list|,
literal|"box"
argument_list|,
literal|"fan"
argument_list|,
literal|"qux"
argument_list|,
literal|"cat"
argument_list|,
literal|"dog"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|constrained
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
block|}
DECL|method|testConstrainedListRandomAccessFalse ()
specifier|public
name|void
name|testConstrainedListRandomAccessFalse
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
name|newLinkedList
argument_list|(
name|asList
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|constrained
init|=
name|Constraints
operator|.
name|constrainedList
argument_list|(
name|list
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|constrained
operator|.
name|add
argument_list|(
literal|"qux"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|constrained
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
block|}
DECL|method|testConstrainedListIllegal ()
specifier|public
name|void
name|testConstrainedListIllegal
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
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|constrained
init|=
name|Constraints
operator|.
name|constrainedList
argument_list|(
name|list
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
try|try
block|{
name|constrained
operator|.
name|add
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|listIterator
argument_list|()
operator|.
name|add
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|listIterator
argument_list|(
literal|1
argument_list|)
operator|.
name|add
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|listIterator
argument_list|()
operator|.
name|set
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|listIterator
argument_list|(
literal|1
argument_list|)
operator|.
name|set
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|.
name|add
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|add
argument_list|(
literal|1
argument_list|,
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|set
argument_list|(
literal|1
argument_list|,
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|addAll
argument_list|(
name|asList
argument_list|(
literal|"baz"
argument_list|,
name|TEST_ELEMENT
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|addAll
argument_list|(
literal|1
argument_list|,
name|asList
argument_list|(
literal|"baz"
argument_list|,
name|TEST_ELEMENT
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
name|ASSERT
operator|.
name|that
argument_list|(
name|constrained
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|list
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testConstrainedMultisetLegal ()
specifier|public
name|void
name|testConstrainedMultisetLegal
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|asList
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|constrained
init|=
name|Constraints
operator|.
name|constrainedMultiset
argument_list|(
name|multiset
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
name|multiset
operator|.
name|add
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|constrained
operator|.
name|add
argument_list|(
literal|"qux"
argument_list|)
expr_stmt|;
name|constrained
operator|.
name|addAll
argument_list|(
name|asList
argument_list|(
literal|"cat"
argument_list|,
literal|"dog"
argument_list|)
argument_list|)
expr_stmt|;
name|constrained
operator|.
name|add
argument_list|(
literal|"cow"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multiset
operator|.
name|equals
argument_list|(
name|constrained
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|constrained
operator|.
name|equals
argument_list|(
name|multiset
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|multiset
operator|.
name|toString
argument_list|()
argument_list|,
name|constrained
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|multiset
operator|.
name|hashCode
argument_list|()
argument_list|,
name|constrained
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|multiset
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
name|TEST_ELEMENT
argument_list|,
literal|"qux"
argument_list|,
literal|"cat"
argument_list|,
literal|"dog"
argument_list|,
literal|"cow"
argument_list|,
literal|"cow"
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|constrained
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
name|TEST_ELEMENT
argument_list|,
literal|"qux"
argument_list|,
literal|"cat"
argument_list|,
literal|"dog"
argument_list|,
literal|"cow"
argument_list|,
literal|"cow"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|constrained
operator|.
name|count
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|constrained
operator|.
name|remove
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|constrained
operator|.
name|setCount
argument_list|(
literal|"cow"
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|multiset
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"bar"
argument_list|,
name|TEST_ELEMENT
argument_list|,
literal|"qux"
argument_list|,
literal|"cat"
argument_list|,
literal|"dog"
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|constrained
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"bar"
argument_list|,
name|TEST_ELEMENT
argument_list|,
literal|"qux"
argument_list|,
literal|"cat"
argument_list|,
literal|"dog"
argument_list|)
expr_stmt|;
block|}
DECL|method|testConstrainedMultisetIllegal ()
specifier|public
name|void
name|testConstrainedMultisetIllegal
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|asList
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|constrained
init|=
name|Constraints
operator|.
name|constrainedMultiset
argument_list|(
name|multiset
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
try|try
block|{
name|constrained
operator|.
name|add
argument_list|(
name|TEST_ELEMENT
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|add
argument_list|(
name|TEST_ELEMENT
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|addAll
argument_list|(
name|asList
argument_list|(
literal|"baz"
argument_list|,
name|TEST_ELEMENT
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestElementException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestElementException
name|expected
parameter_list|)
block|{}
name|ASSERT
operator|.
name|that
argument_list|(
name|constrained
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|multiset
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
block|}
DECL|method|testNefariousAddAll ()
specifier|public
name|void
name|testNefariousAddAll
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
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|constrained
init|=
name|Constraints
operator|.
name|constrainedList
argument_list|(
name|list
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|onceIterable
init|=
name|onceIterableCollection
argument_list|(
literal|"baz"
argument_list|)
decl_stmt|;
name|constrained
operator|.
name|addAll
argument_list|(
name|onceIterable
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|constrained
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|"baz"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|list
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|"baz"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
comment|/**    * Returns a "nefarious" collection, which permits only one call to    * iterator(). This verifies that the constrained collection uses a defensive    * copy instead of potentially checking the elements in one snapshot and    * adding the elements from another.    *    * @param element the element to be contained in the collection    */
DECL|method|onceIterableCollection (final E element)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Collection
argument_list|<
name|E
argument_list|>
name|onceIterableCollection
parameter_list|(
specifier|final
name|E
name|element
parameter_list|)
block|{
return|return
operator|new
name|AbstractCollection
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
name|boolean
name|iteratorCalled
decl_stmt|;
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
comment|/*          * We could make the collection empty, but that seems more likely to          * trigger special cases (so maybe we should test both empty and          * nonempty...).          */
return|return
literal|1
return|;
block|}
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"Expected only one call to iterator()"
argument_list|,
name|iteratorCalled
argument_list|)
expr_stmt|;
name|iteratorCalled
operator|=
literal|true
expr_stmt|;
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|element
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

