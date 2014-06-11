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
name|DerivedComparable
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
name|Arrays
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
name|List
import|;
end_import

begin_comment
comment|/**  * Tests for {@link Multisets}.  *  * @author Mike Bostock  * @author Jared Levy  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|MultisetsTest
specifier|public
class|class
name|MultisetsTest
extends|extends
name|TestCase
block|{
comment|/* See MultisetsImmutableEntryTest for immutableEntry() tests. */
DECL|method|testNewTreeMultisetDerived ()
specifier|public
name|void
name|testNewTreeMultisetDerived
parameter_list|()
block|{
name|TreeMultiset
argument_list|<
name|DerivedComparable
argument_list|>
name|set
init|=
name|TreeMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|set
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
operator|new
name|DerivedComparable
argument_list|(
literal|"foo"
argument_list|)
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
operator|new
name|DerivedComparable
argument_list|(
literal|"bar"
argument_list|)
argument_list|,
literal|3
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
operator|new
name|DerivedComparable
argument_list|(
literal|"bar"
argument_list|)
argument_list|,
operator|new
name|DerivedComparable
argument_list|(
literal|"bar"
argument_list|)
argument_list|,
operator|new
name|DerivedComparable
argument_list|(
literal|"bar"
argument_list|)
argument_list|,
operator|new
name|DerivedComparable
argument_list|(
literal|"foo"
argument_list|)
argument_list|,
operator|new
name|DerivedComparable
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testNewTreeMultisetNonGeneric ()
specifier|public
name|void
name|testNewTreeMultisetNonGeneric
parameter_list|()
block|{
name|TreeMultiset
argument_list|<
name|LegacyComparable
argument_list|>
name|set
init|=
name|TreeMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|set
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
operator|new
name|LegacyComparable
argument_list|(
literal|"foo"
argument_list|)
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
operator|new
name|LegacyComparable
argument_list|(
literal|"bar"
argument_list|)
argument_list|,
literal|3
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
operator|new
name|LegacyComparable
argument_list|(
literal|"bar"
argument_list|)
argument_list|,
operator|new
name|LegacyComparable
argument_list|(
literal|"bar"
argument_list|)
argument_list|,
operator|new
name|LegacyComparable
argument_list|(
literal|"bar"
argument_list|)
argument_list|,
operator|new
name|LegacyComparable
argument_list|(
literal|"foo"
argument_list|)
argument_list|,
operator|new
name|LegacyComparable
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testNewTreeMultisetComparator ()
specifier|public
name|void
name|testNewTreeMultisetComparator
parameter_list|()
block|{
name|TreeMultiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
name|TreeMultiset
operator|.
name|create
argument_list|(
name|Collections
operator|.
name|reverseOrder
argument_list|()
argument_list|)
decl_stmt|;
name|multiset
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|multiset
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|,
literal|2
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
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|"bar"
argument_list|,
literal|"bar"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testRetainOccurrencesEmpty ()
specifier|public
name|void
name|testRetainOccurrencesEmpty
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
argument_list|()
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|toRetain
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|Multisets
operator|.
name|retainOccurrences
argument_list|(
name|multiset
argument_list|,
name|toRetain
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
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemoveOccurrencesIterableEmpty ()
specifier|public
name|void
name|testRemoveOccurrencesIterableEmpty
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
argument_list|()
decl_stmt|;
name|Iterable
argument_list|<
name|String
argument_list|>
name|toRemove
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|Multisets
operator|.
name|removeOccurrences
argument_list|(
name|multiset
argument_list|,
name|toRemove
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multiset
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRemoveOccurrencesMultisetEmpty ()
specifier|public
name|void
name|testRemoveOccurrencesMultisetEmpty
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
argument_list|()
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|toRemove
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|Multisets
operator|.
name|retainOccurrences
argument_list|(
name|multiset
argument_list|,
name|toRemove
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multiset
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testUnion ()
specifier|public
name|void
name|testUnion
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms1
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms2
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|Multisets
operator|.
name|union
argument_list|(
name|ms1
argument_list|,
name|ms2
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"a"
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
expr_stmt|;
block|}
DECL|method|testUnionEqualMultisets ()
specifier|public
name|void
name|testUnionEqualMultisets
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms1
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms2
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ms1
argument_list|,
name|Multisets
operator|.
name|union
argument_list|(
name|ms1
argument_list|,
name|ms2
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUnionEmptyNonempty ()
specifier|public
name|void
name|testUnionEmptyNonempty
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms1
init|=
name|HashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms2
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ms2
argument_list|,
name|Multisets
operator|.
name|union
argument_list|(
name|ms1
argument_list|,
name|ms2
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUnionNonemptyEmpty ()
specifier|public
name|void
name|testUnionNonemptyEmpty
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms1
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms2
init|=
name|HashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|ms1
argument_list|,
name|Multisets
operator|.
name|union
argument_list|(
name|ms1
argument_list|,
name|ms2
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIntersectEmptyNonempty ()
specifier|public
name|void
name|testIntersectEmptyNonempty
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms1
init|=
name|HashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms2
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|Multisets
operator|.
name|intersection
argument_list|(
name|ms1
argument_list|,
name|ms2
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testIntersectNonemptyEmpty ()
specifier|public
name|void
name|testIntersectNonemptyEmpty
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms1
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms2
init|=
name|HashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|Multisets
operator|.
name|intersection
argument_list|(
name|ms1
argument_list|,
name|ms2
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testSum ()
specifier|public
name|void
name|testSum
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms1
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms2
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"b"
argument_list|,
literal|"c"
argument_list|)
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|Multisets
operator|.
name|sum
argument_list|(
name|ms1
argument_list|,
name|ms2
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"a"
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
expr_stmt|;
block|}
DECL|method|testSumEmptyNonempty ()
specifier|public
name|void
name|testSumEmptyNonempty
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms1
init|=
name|HashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms2
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|Multisets
operator|.
name|sum
argument_list|(
name|ms1
argument_list|,
name|ms2
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
block|}
DECL|method|testSumNonemptyEmpty ()
specifier|public
name|void
name|testSumNonemptyEmpty
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms1
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms2
init|=
name|HashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|Multisets
operator|.
name|sum
argument_list|(
name|ms1
argument_list|,
name|ms2
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
block|}
DECL|method|testDifferenceWithNoRemovedElements ()
specifier|public
name|void
name|testDifferenceWithNoRemovedElements
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms1
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms2
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|Multisets
operator|.
name|difference
argument_list|(
name|ms1
argument_list|,
name|ms2
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
block|}
DECL|method|testDifferenceWithRemovedElement ()
specifier|public
name|void
name|testDifferenceWithRemovedElement
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms1
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms2
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"b"
argument_list|)
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|Multisets
operator|.
name|difference
argument_list|(
name|ms1
argument_list|,
name|ms2
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"a"
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
block|}
DECL|method|testDifferenceWithMoreElementsInSecondMultiset ()
specifier|public
name|void
name|testDifferenceWithMoreElementsInSecondMultiset
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms1
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms2
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"b"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|diff
init|=
name|Multisets
operator|.
name|difference
argument_list|(
name|ms1
argument_list|,
name|ms2
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|diff
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|item
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|diff
operator|.
name|count
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|diff
operator|.
name|count
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|diff
operator|.
name|contains
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|diff
operator|.
name|contains
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testDifferenceEmptyNonempty ()
specifier|public
name|void
name|testDifferenceEmptyNonempty
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms1
init|=
name|HashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms2
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ms1
argument_list|,
name|Multisets
operator|.
name|difference
argument_list|(
name|ms1
argument_list|,
name|ms2
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testDifferenceNonemptyEmpty ()
specifier|public
name|void
name|testDifferenceNonemptyEmpty
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms1
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms2
init|=
name|HashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|ms1
argument_list|,
name|Multisets
operator|.
name|difference
argument_list|(
name|ms1
argument_list|,
name|ms2
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testContainsOccurrencesEmpty ()
specifier|public
name|void
name|testContainsOccurrencesEmpty
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|superMultiset
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|subMultiset
init|=
name|HashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|Multisets
operator|.
name|containsOccurrences
argument_list|(
name|superMultiset
argument_list|,
name|subMultiset
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Multisets
operator|.
name|containsOccurrences
argument_list|(
name|subMultiset
argument_list|,
name|superMultiset
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testContainsOccurrences ()
specifier|public
name|void
name|testContainsOccurrences
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|superMultiset
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|subMultiset
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|Multisets
operator|.
name|containsOccurrences
argument_list|(
name|superMultiset
argument_list|,
name|subMultiset
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Multisets
operator|.
name|containsOccurrences
argument_list|(
name|subMultiset
argument_list|,
name|superMultiset
argument_list|)
argument_list|)
expr_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|diffMultiset
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|Multisets
operator|.
name|containsOccurrences
argument_list|(
name|superMultiset
argument_list|,
name|diffMultiset
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Multisets
operator|.
name|containsOccurrences
argument_list|(
name|diffMultiset
argument_list|,
name|subMultiset
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testRetainEmptyOccurrences ()
specifier|public
name|void
name|testRetainEmptyOccurrences
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
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|toRetain
init|=
name|HashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|Multisets
operator|.
name|retainOccurrences
argument_list|(
name|multiset
argument_list|,
name|toRetain
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multiset
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRetainOccurrences ()
specifier|public
name|void
name|testRetainOccurrences
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
name|TreeMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|,
literal|"c"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|toRetain
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|Multisets
operator|.
name|retainOccurrences
argument_list|(
name|multiset
argument_list|,
name|toRetain
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
literal|"a"
argument_list|,
literal|"b"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemoveEmptyOccurrencesMultiset ()
specifier|public
name|void
name|testRemoveEmptyOccurrencesMultiset
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
name|TreeMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|toRemove
init|=
name|HashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|Multisets
operator|.
name|removeOccurrences
argument_list|(
name|multiset
argument_list|,
name|toRemove
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
literal|"a"
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemoveOccurrencesMultiset ()
specifier|public
name|void
name|testRemoveOccurrencesMultiset
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
name|TreeMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|,
literal|"c"
argument_list|)
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|toRemove
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|Multisets
operator|.
name|removeOccurrences
argument_list|(
name|multiset
argument_list|,
name|toRemove
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
literal|"a"
argument_list|,
literal|"c"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemoveEmptyOccurrencesIterable ()
specifier|public
name|void
name|testRemoveEmptyOccurrencesIterable
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
name|TreeMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|Iterable
argument_list|<
name|String
argument_list|>
name|toRemove
init|=
name|ImmutableList
operator|.
name|of
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|Multisets
operator|.
name|removeOccurrences
argument_list|(
name|multiset
argument_list|,
name|toRemove
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
literal|"a"
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemoveOccurrencesMultisetIterable ()
specifier|public
name|void
name|testRemoveOccurrencesMultisetIterable
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
name|TreeMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|,
literal|"c"
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|toRemove
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|Multisets
operator|.
name|removeOccurrences
argument_list|(
name|multiset
argument_list|,
name|toRemove
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
literal|"a"
argument_list|,
literal|"c"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|testUnmodifiableMultisetShortCircuit ()
specifier|public
name|void
name|testUnmodifiableMultisetShortCircuit
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|mod
init|=
name|HashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|unmod
init|=
name|Multisets
operator|.
name|unmodifiableMultiset
argument_list|(
name|mod
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|mod
argument_list|,
name|unmod
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|unmod
argument_list|,
name|Multisets
operator|.
name|unmodifiableMultiset
argument_list|(
name|unmod
argument_list|)
argument_list|)
expr_stmt|;
name|ImmutableMultiset
argument_list|<
name|String
argument_list|>
name|immutable
init|=
name|ImmutableMultiset
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|immutable
argument_list|,
name|Multisets
operator|.
name|unmodifiableMultiset
argument_list|(
name|immutable
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|immutable
argument_list|,
name|Multisets
operator|.
name|unmodifiableMultiset
argument_list|(
operator|(
name|Multiset
argument_list|<
name|String
argument_list|>
operator|)
name|immutable
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testHighestCountFirst ()
specifier|public
name|void
name|testHighestCountFirst
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
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"a"
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|,
literal|"c"
argument_list|)
argument_list|)
decl_stmt|;
name|ImmutableMultiset
argument_list|<
name|String
argument_list|>
name|sortedMultiset
init|=
name|Multisets
operator|.
name|copyHighestCountFirst
argument_list|(
name|multiset
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|sortedMultiset
operator|.
name|entrySet
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
argument_list|,
name|Multisets
operator|.
name|immutableEntry
argument_list|(
literal|"c"
argument_list|,
literal|2
argument_list|)
argument_list|,
name|Multisets
operator|.
name|immutableEntry
argument_list|(
literal|"b"
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|sortedMultiset
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|"a"
argument_list|,
literal|"a"
argument_list|,
literal|"a"
argument_list|,
literal|"c"
argument_list|,
literal|"c"
argument_list|,
literal|"b"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|Multisets
operator|.
name|copyHighestCountFirst
argument_list|(
name|ImmutableMultiset
operator|.
name|of
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

