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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
operator|.
name|SerializableTester
operator|.
name|reserializeAndAssert
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|contrib
operator|.
name|truth
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
name|DerivedComparable
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
name|NullPointerTester
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
name|HashSet
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

begin_comment
comment|/**  * Tests for {@link Multisets}.  *  * @author Mike Bostock  * @author Jared Levy  */
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
DECL|method|testForSet ()
specifier|public
name|void
name|testForSet
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
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
name|multiset
operator|.
name|addAll
argument_list|(
name|set
argument_list|)
expr_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|multisetView
init|=
name|Multisets
operator|.
name|forSet
argument_list|(
name|set
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|multiset
operator|.
name|equals
argument_list|(
name|multisetView
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multisetView
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
name|multisetView
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
name|multisetView
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|multiset
operator|.
name|size
argument_list|()
argument_list|,
name|multisetView
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multisetView
operator|.
name|contains
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|set
argument_list|,
name|multisetView
operator|.
name|elementSet
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|multisetView
operator|.
name|elementSet
argument_list|()
argument_list|,
name|set
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|multiset
operator|.
name|elementSet
argument_list|()
argument_list|,
name|multisetView
operator|.
name|elementSet
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|multisetView
operator|.
name|elementSet
argument_list|()
argument_list|,
name|multiset
operator|.
name|elementSet
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|multisetView
operator|.
name|add
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"UnsupportedOperationException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|multisetView
operator|.
name|addAll
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
literal|"baz"
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"UnsupportedOperationException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|multisetView
operator|.
name|elementSet
argument_list|()
operator|.
name|add
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"UnsupportedOperationException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|multisetView
operator|.
name|elementSet
argument_list|()
operator|.
name|addAll
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
literal|"baz"
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"UnsupportedOperationException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{}
name|multisetView
operator|.
name|remove
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|multisetView
operator|.
name|contains
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|set
operator|.
name|contains
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|set
argument_list|,
name|multisetView
operator|.
name|elementSet
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|multisetView
operator|.
name|elementSet
argument_list|()
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|multisetView
operator|.
name|entrySet
argument_list|()
argument_list|)
operator|.
name|hasContentsAnyOrder
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
argument_list|,
name|Multisets
operator|.
name|immutableEntry
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|multisetView
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|multisetView
operator|.
name|contains
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|set
operator|.
name|contains
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|set
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multisetView
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|multiset
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|multiset
operator|.
name|toString
argument_list|()
argument_list|,
name|multisetView
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
name|multisetView
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|multiset
operator|.
name|size
argument_list|()
argument_list|,
name|multisetView
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"SerializableTester"
argument_list|)
DECL|method|testForSetSerialization ()
specifier|public
name|void
name|testForSetSerialization
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
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
name|multiset
operator|.
name|addAll
argument_list|(
name|set
argument_list|)
expr_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|multisetView
init|=
name|Multisets
operator|.
name|forSet
argument_list|(
name|set
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|multiset
operator|.
name|equals
argument_list|(
name|multisetView
argument_list|)
argument_list|)
expr_stmt|;
name|reserializeAndAssert
argument_list|(
name|multisetView
argument_list|)
expr_stmt|;
block|}
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
name|hasContentsInOrder
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
name|hasContentsInOrder
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
name|hasContentsInOrder
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
name|hasContentsInOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemoveOccurrencesEmpty ()
specifier|public
name|void
name|testRemoveOccurrencesEmpty
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
name|hasContentsInOrder
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
name|hasContentsInOrder
argument_list|()
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
name|hasContentsInOrder
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRemoveEmptyOccurrences ()
specifier|public
name|void
name|testRemoveEmptyOccurrences
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
name|hasContentsInOrder
argument_list|(
literal|"a"
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRemoveOccurrences ()
specifier|public
name|void
name|testRemoveOccurrences
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
name|hasContentsInOrder
argument_list|(
literal|"a"
argument_list|,
literal|"c"
argument_list|)
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
annotation|@
name|GwtIncompatible
argument_list|(
literal|"NullPointerTester"
argument_list|)
DECL|method|testNullPointers ()
specifier|public
name|void
name|testNullPointers
parameter_list|()
throws|throws
name|Exception
block|{
name|NullPointerTester
name|tester
init|=
operator|new
name|NullPointerTester
argument_list|()
decl_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|Multiset
operator|.
name|class
argument_list|,
name|ImmutableMultiset
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
name|tester
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|Multisets
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

