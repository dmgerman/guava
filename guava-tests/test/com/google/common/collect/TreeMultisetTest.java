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
name|collect
operator|.
name|BoundType
operator|.
name|CLOSED
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
name|IteratorTester
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
name|Comparator
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
comment|/**  * Unit test for {@link TreeMultiset}.  *  * @author Neal Kanodia  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|TreeMultisetTest
specifier|public
class|class
name|TreeMultisetTest
extends|extends
name|AbstractMultisetTest
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|create ()
annotation|@
name|Override
specifier|protected
parameter_list|<
name|E
parameter_list|>
name|Multiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|(
name|Multiset
operator|)
name|TreeMultiset
operator|.
name|create
argument_list|()
return|;
block|}
DECL|method|testCreate ()
specifier|public
name|void
name|testCreate
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
argument_list|()
decl_stmt|;
name|multiset
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|multiset
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|multiset
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|multiset
operator|.
name|count
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
name|multiset
operator|.
name|comparator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[bar, foo x 2]"
argument_list|,
name|multiset
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateWithComparator ()
specifier|public
name|void
name|testCreateWithComparator
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
literal|"foo"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|multiset
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|multiset
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|multiset
operator|.
name|count
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[foo x 2, bar]"
argument_list|,
name|multiset
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateFromIterable ()
specifier|public
name|void
name|testCreateFromIterable
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
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|multiset
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|multiset
operator|.
name|count
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[bar, foo x 2]"
argument_list|,
name|multiset
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"c"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"b"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[a x 3, b x 2, c]"
argument_list|,
name|ms
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"unreasonable slow"
argument_list|)
DECL|method|testIteratorBashing ()
specifier|public
name|void
name|testIteratorBashing
parameter_list|()
block|{
name|IteratorTester
argument_list|<
name|String
argument_list|>
name|tester
init|=
operator|new
name|IteratorTester
argument_list|<
name|String
argument_list|>
argument_list|(
name|createSample
argument_list|()
operator|.
name|size
argument_list|()
operator|+
literal|2
argument_list|,
name|MODIFIABLE
argument_list|,
name|newArrayList
argument_list|(
name|createSample
argument_list|()
argument_list|)
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|)
block|{
specifier|private
name|Multiset
argument_list|<
name|String
argument_list|>
name|targetMultiset
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|String
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
name|targetMultiset
operator|=
name|createSample
argument_list|()
expr_stmt|;
return|return
name|targetMultiset
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
name|String
argument_list|>
name|elements
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|elements
argument_list|,
name|Lists
operator|.
name|newArrayList
argument_list|(
name|targetMultiset
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|/* This next line added as a stopgap until JDK6 bug is fixed. */
name|tester
operator|.
name|ignoreSunJavaBug6529795
argument_list|()
expr_stmt|;
name|tester
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"slow (~30s)"
argument_list|)
DECL|method|testElementSetIteratorBashing ()
specifier|public
name|void
name|testElementSetIteratorBashing
parameter_list|()
block|{
name|IteratorTester
argument_list|<
name|String
argument_list|>
name|tester
init|=
operator|new
name|IteratorTester
argument_list|<
name|String
argument_list|>
argument_list|(
literal|5
argument_list|,
name|MODIFIABLE
argument_list|,
name|newArrayList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|)
block|{
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|targetSet
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|String
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
name|create
argument_list|()
decl_stmt|;
name|multiset
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|multiset
operator|.
name|add
argument_list|(
literal|"c"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|multiset
operator|.
name|add
argument_list|(
literal|"b"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|targetSet
operator|=
name|multiset
operator|.
name|elementSet
argument_list|()
expr_stmt|;
return|return
name|targetSet
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
name|String
argument_list|>
name|elements
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|elements
argument_list|,
name|Lists
operator|.
name|newArrayList
argument_list|(
name|targetSet
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|/* This next line added as a stopgap until JDK6 bug is fixed. */
name|tester
operator|.
name|ignoreSunJavaBug6529795
argument_list|()
expr_stmt|;
name|tester
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testElementSetSortedSetMethods ()
specifier|public
name|void
name|testElementSetSortedSetMethods
parameter_list|()
block|{
name|TreeMultiset
argument_list|<
name|String
argument_list|>
name|ms
init|=
name|TreeMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"c"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"b"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|elementSet
init|=
name|ms
operator|.
name|elementSet
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|elementSet
operator|.
name|first
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c"
argument_list|,
name|elementSet
operator|.
name|last
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
name|elementSet
operator|.
name|comparator
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|elementSet
operator|.
name|headSet
argument_list|(
literal|"b"
argument_list|)
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|elementSet
operator|.
name|tailSet
argument_list|(
literal|"b"
argument_list|)
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"b"
argument_list|,
literal|"c"
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|elementSet
operator|.
name|subSet
argument_list|(
literal|"a"
argument_list|,
literal|"c"
argument_list|)
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
DECL|method|testElementSetSubsetRemove ()
specifier|public
name|void
name|testElementSetSubsetRemove
parameter_list|()
block|{
name|TreeMultiset
argument_list|<
name|String
argument_list|>
name|ms
init|=
name|TreeMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"b"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"c"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"d"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"e"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"f"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|elementSet
init|=
name|ms
operator|.
name|elementSet
argument_list|()
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|elementSet
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|,
literal|"d"
argument_list|,
literal|"e"
argument_list|,
literal|"f"
argument_list|)
expr_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|subset
init|=
name|elementSet
operator|.
name|subSet
argument_list|(
literal|"b"
argument_list|,
literal|"f"
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|subset
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"b"
argument_list|,
literal|"c"
argument_list|,
literal|"d"
argument_list|,
literal|"e"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|subset
operator|.
name|remove
argument_list|(
literal|"c"
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|elementSet
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"d"
argument_list|,
literal|"e"
argument_list|,
literal|"f"
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|subset
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"b"
argument_list|,
literal|"d"
argument_list|,
literal|"e"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|ms
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|subset
operator|.
name|remove
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|elementSet
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"d"
argument_list|,
literal|"e"
argument_list|,
literal|"f"
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|subset
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"b"
argument_list|,
literal|"d"
argument_list|,
literal|"e"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|ms
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testElementSetSubsetRemoveAll ()
specifier|public
name|void
name|testElementSetSubsetRemoveAll
parameter_list|()
block|{
name|TreeMultiset
argument_list|<
name|String
argument_list|>
name|ms
init|=
name|TreeMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"b"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"c"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"d"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"e"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"f"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|elementSet
init|=
name|ms
operator|.
name|elementSet
argument_list|()
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|elementSet
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|,
literal|"d"
argument_list|,
literal|"e"
argument_list|,
literal|"f"
argument_list|)
expr_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|subset
init|=
name|elementSet
operator|.
name|subSet
argument_list|(
literal|"b"
argument_list|,
literal|"f"
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|subset
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"b"
argument_list|,
literal|"c"
argument_list|,
literal|"d"
argument_list|,
literal|"e"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|subset
operator|.
name|removeAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"c"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|elementSet
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"d"
argument_list|,
literal|"e"
argument_list|,
literal|"f"
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|subset
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"b"
argument_list|,
literal|"d"
argument_list|,
literal|"e"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|ms
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testElementSetSubsetRetainAll ()
specifier|public
name|void
name|testElementSetSubsetRetainAll
parameter_list|()
block|{
name|TreeMultiset
argument_list|<
name|String
argument_list|>
name|ms
init|=
name|TreeMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"b"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"c"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"d"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"e"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"f"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|elementSet
init|=
name|ms
operator|.
name|elementSet
argument_list|()
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|elementSet
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|,
literal|"d"
argument_list|,
literal|"e"
argument_list|,
literal|"f"
argument_list|)
expr_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|subset
init|=
name|elementSet
operator|.
name|subSet
argument_list|(
literal|"b"
argument_list|,
literal|"f"
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|subset
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"b"
argument_list|,
literal|"c"
argument_list|,
literal|"d"
argument_list|,
literal|"e"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|subset
operator|.
name|retainAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"c"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|elementSet
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"a"
argument_list|,
literal|"c"
argument_list|,
literal|"f"
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|subset
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|ms
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testElementSetSubsetClear ()
specifier|public
name|void
name|testElementSetSubsetClear
parameter_list|()
block|{
name|TreeMultiset
argument_list|<
name|String
argument_list|>
name|ms
init|=
name|TreeMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"b"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"c"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"d"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"e"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"f"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|elementSet
init|=
name|ms
operator|.
name|elementSet
argument_list|()
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|elementSet
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|,
literal|"d"
argument_list|,
literal|"e"
argument_list|,
literal|"f"
argument_list|)
expr_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|subset
init|=
name|elementSet
operator|.
name|subSet
argument_list|(
literal|"b"
argument_list|,
literal|"f"
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|subset
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"b"
argument_list|,
literal|"c"
argument_list|,
literal|"d"
argument_list|,
literal|"e"
argument_list|)
expr_stmt|;
name|subset
operator|.
name|clear
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|elementSet
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"a"
argument_list|,
literal|"f"
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|subset
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|ms
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCustomComparator ()
specifier|public
name|void
name|testCustomComparator
parameter_list|()
throws|throws
name|Exception
block|{
name|Comparator
argument_list|<
name|String
argument_list|>
name|comparator
init|=
operator|new
name|Comparator
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|String
name|o1
parameter_list|,
name|String
name|o2
parameter_list|)
block|{
return|return
name|o2
operator|.
name|compareTo
argument_list|(
name|o1
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|TreeMultiset
argument_list|<
name|String
argument_list|>
name|ms
init|=
name|TreeMultiset
operator|.
name|create
argument_list|(
name|comparator
argument_list|)
decl_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"d"
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|ms
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"d"
argument_list|,
literal|"c"
argument_list|,
literal|"b"
argument_list|,
literal|"b"
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|elementSet
init|=
name|ms
operator|.
name|elementSet
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"d"
argument_list|,
name|elementSet
operator|.
name|first
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|elementSet
operator|.
name|last
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|comparator
argument_list|,
name|elementSet
operator|.
name|comparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testNullAcceptingComparator ()
specifier|public
name|void
name|testNullAcceptingComparator
parameter_list|()
throws|throws
name|Exception
block|{
name|Comparator
argument_list|<
name|String
argument_list|>
name|comparator
init|=
name|Ordering
operator|.
expr|<
name|String
operator|>
name|natural
argument_list|()
operator|.
name|nullsFirst
argument_list|()
decl_stmt|;
name|TreeMultiset
argument_list|<
name|String
argument_list|>
name|ms
init|=
name|TreeMultiset
operator|.
name|create
argument_list|(
name|comparator
argument_list|)
decl_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|null
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|ms
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|ms
operator|.
name|count
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|elementSet
init|=
name|ms
operator|.
name|elementSet
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|elementSet
operator|.
name|first
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|elementSet
operator|.
name|last
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|comparator
argument_list|,
name|elementSet
operator|.
name|comparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|field|DEGENERATE_COMPARATOR
specifier|private
specifier|static
specifier|final
name|Comparator
argument_list|<
name|String
argument_list|>
name|DEGENERATE_COMPARATOR
init|=
operator|new
name|Comparator
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|String
name|o1
parameter_list|,
name|String
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|length
argument_list|()
operator|-
name|o2
operator|.
name|length
argument_list|()
return|;
block|}
block|}
decl_stmt|;
comment|/**    * Test a TreeMultiset with a comparator that can return 0 when comparing    * unequal values.    */
DECL|method|testDegenerateComparator ()
specifier|public
name|void
name|testDegenerateComparator
parameter_list|()
throws|throws
name|Exception
block|{
name|TreeMultiset
argument_list|<
name|String
argument_list|>
name|ms
init|=
name|TreeMultiset
operator|.
name|create
argument_list|(
name|DEGENERATE_COMPARATOR
argument_list|)
decl_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|ms
operator|.
name|count
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|ms
operator|.
name|count
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|Multiset
argument_list|<
name|String
argument_list|>
name|ms2
init|=
name|TreeMultiset
operator|.
name|create
argument_list|(
name|DEGENERATE_COMPARATOR
argument_list|)
decl_stmt|;
name|ms2
operator|.
name|add
argument_list|(
literal|"cat"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|ms2
operator|.
name|add
argument_list|(
literal|"x"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms
argument_list|,
name|ms2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ms2
argument_list|,
name|ms
argument_list|)
expr_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|elementSet
init|=
name|ms
operator|.
name|elementSet
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|elementSet
operator|.
name|first
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|elementSet
operator|.
name|last
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DEGENERATE_COMPARATOR
argument_list|,
name|elementSet
operator|.
name|comparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSubMultisetSize ()
specifier|public
name|void
name|testSubMultisetSize
parameter_list|()
block|{
name|TreeMultiset
argument_list|<
name|String
argument_list|>
name|ms
init|=
name|TreeMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"a"
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"b"
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
name|ms
operator|.
name|add
argument_list|(
literal|"c"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|ms
operator|.
name|count
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|ms
operator|.
name|count
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|ms
operator|.
name|count
argument_list|(
literal|"c"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|ms
operator|.
name|headMultiset
argument_list|(
literal|"c"
argument_list|,
name|CLOSED
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|ms
operator|.
name|headMultiset
argument_list|(
literal|"b"
argument_list|,
name|CLOSED
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|ms
operator|.
name|headMultiset
argument_list|(
literal|"a"
argument_list|,
name|CLOSED
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|ms
operator|.
name|tailMultiset
argument_list|(
literal|"c"
argument_list|,
name|CLOSED
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|ms
operator|.
name|tailMultiset
argument_list|(
literal|"b"
argument_list|,
name|CLOSED
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|ms
operator|.
name|tailMultiset
argument_list|(
literal|"a"
argument_list|,
name|CLOSED
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToStringNull ()
annotation|@
name|Override
specifier|public
name|void
name|testToStringNull
parameter_list|()
block|{
name|c
operator|=
name|ms
operator|=
name|TreeMultiset
operator|.
name|create
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|nullsFirst
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|testToStringNull
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

