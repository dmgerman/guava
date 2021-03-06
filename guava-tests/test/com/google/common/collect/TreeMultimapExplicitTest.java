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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|testing
operator|.
name|SerializableTester
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
name|Collection
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
name|Map
operator|.
name|Entry
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Unit tests for {@code TreeMultimap} with explicit comparators.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|TreeMultimapExplicitTest
specifier|public
class|class
name|TreeMultimapExplicitTest
extends|extends
name|TestCase
block|{
comment|/**    * Compare strings lengths, and if the lengths are equal compare the strings. A {@code null} is    * less than any non-null value.    */
DECL|enum|StringLength
specifier|private
enum|enum
name|StringLength
implements|implements
name|Comparator
argument_list|<
name|String
argument_list|>
block|{
DECL|enumConstant|COMPARATOR
name|COMPARATOR
block|;
annotation|@
name|Override
DECL|method|compare (String first, String second)
specifier|public
name|int
name|compare
parameter_list|(
name|String
name|first
parameter_list|,
name|String
name|second
parameter_list|)
block|{
if|if
condition|(
name|first
operator|==
name|second
condition|)
block|{
return|return
literal|0
return|;
block|}
elseif|else
if|if
condition|(
name|first
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
elseif|else
if|if
condition|(
name|second
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
elseif|else
if|if
condition|(
name|first
operator|.
name|length
argument_list|()
operator|!=
name|second
operator|.
name|length
argument_list|()
condition|)
block|{
return|return
name|first
operator|.
name|length
argument_list|()
operator|-
name|second
operator|.
name|length
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|first
operator|.
name|compareTo
argument_list|(
name|second
argument_list|)
return|;
block|}
block|}
block|}
comment|/** Decreasing integer values. A {@code null} comes before any non-null value. */
DECL|field|DECREASING_INT_COMPARATOR
specifier|private
specifier|static
specifier|final
name|Comparator
argument_list|<
name|Integer
argument_list|>
name|DECREASING_INT_COMPARATOR
init|=
name|Ordering
operator|.
expr|<
name|Integer
operator|>
name|natural
argument_list|()
operator|.
name|reverse
argument_list|()
operator|.
name|nullsFirst
argument_list|()
decl_stmt|;
DECL|method|create ()
specifier|private
name|SetMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|create
parameter_list|()
block|{
return|return
name|TreeMultimap
operator|.
name|create
argument_list|(
name|StringLength
operator|.
name|COMPARATOR
argument_list|,
name|DECREASING_INT_COMPARATOR
argument_list|)
return|;
block|}
comment|/** Create and populate a {@code TreeMultimap} with explicit comparators. */
DECL|method|createPopulate ()
specifier|private
name|TreeMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|createPopulate
parameter_list|()
block|{
name|TreeMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|TreeMultimap
operator|.
name|create
argument_list|(
name|StringLength
operator|.
name|COMPARATOR
argument_list|,
name|DECREASING_INT_COMPARATOR
argument_list|)
decl_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"google"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"google"
argument_list|,
literal|6
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|null
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|null
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|null
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"tree"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"tree"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|multimap
return|;
block|}
comment|/** Test that a TreeMultimap created from another uses the natural ordering. */
DECL|method|testMultimapCreateFromTreeMultimap ()
specifier|public
name|void
name|testMultimapCreateFromTreeMultimap
parameter_list|()
block|{
name|TreeMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|tree
init|=
name|TreeMultimap
operator|.
name|create
argument_list|(
name|StringLength
operator|.
name|COMPARATOR
argument_list|,
name|DECREASING_INT_COMPARATOR
argument_list|)
decl_stmt|;
name|tree
operator|.
name|put
argument_list|(
literal|"google"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|tree
operator|.
name|put
argument_list|(
literal|"google"
argument_list|,
literal|6
argument_list|)
expr_stmt|;
name|tree
operator|.
name|put
argument_list|(
literal|"tree"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|tree
operator|.
name|put
argument_list|(
literal|"tree"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|tree
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"tree"
argument_list|,
literal|"google"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|tree
operator|.
name|get
argument_list|(
literal|"google"
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|6
argument_list|,
literal|2
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|TreeMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|copy
init|=
name|TreeMultimap
operator|.
name|create
argument_list|(
name|tree
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|tree
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|copy
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"google"
argument_list|,
literal|"tree"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|copy
operator|.
name|get
argument_list|(
literal|"google"
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|2
argument_list|,
literal|6
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
name|copy
operator|.
name|keyComparator
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
name|copy
operator|.
name|valueComparator
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
name|copy
operator|.
name|get
argument_list|(
literal|"google"
argument_list|)
operator|.
name|comparator
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
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|create
argument_list|()
decl_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|putAll
argument_list|(
literal|"foo"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|-
literal|1
argument_list|,
literal|2
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|putAll
argument_list|(
literal|"bar"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{bar=[3, 2, 1], foo=[4, 3, 2, 1, -1]}"
argument_list|,
name|multimap
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetComparator ()
specifier|public
name|void
name|testGetComparator
parameter_list|()
block|{
name|TreeMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|createPopulate
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|StringLength
operator|.
name|COMPARATOR
argument_list|,
name|multimap
operator|.
name|keyComparator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DECREASING_INT_COMPARATOR
argument_list|,
name|multimap
operator|.
name|valueComparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOrderedGet ()
specifier|public
name|void
name|testOrderedGet
parameter_list|()
block|{
name|TreeMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|createPopulate
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|7
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
literal|"google"
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|6
argument_list|,
literal|2
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
literal|"tree"
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testOrderedKeySet ()
specifier|public
name|void
name|testOrderedKeySet
parameter_list|()
block|{
name|TreeMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|createPopulate
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|multimap
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|null
argument_list|,
literal|"tree"
argument_list|,
literal|"google"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testOrderedAsMapEntries ()
specifier|public
name|void
name|testOrderedAsMapEntries
parameter_list|()
block|{
name|TreeMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|createPopulate
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|Integer
argument_list|>
argument_list|>
argument_list|>
name|iterator
init|=
name|multimap
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|entry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|7
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|entry
operator|=
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tree"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|entry
operator|=
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"google"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|6
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
DECL|method|testOrderedEntries ()
specifier|public
name|void
name|testOrderedEntries
parameter_list|()
block|{
name|TreeMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|createPopulate
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|multimap
operator|.
name|entries
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|,
literal|7
argument_list|)
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|,
literal|3
argument_list|)
argument_list|,
name|Maps
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
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|"tree"
argument_list|,
operator|(
name|Integer
operator|)
literal|null
argument_list|)
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|"tree"
argument_list|,
literal|0
argument_list|)
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|"google"
argument_list|,
literal|6
argument_list|)
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|"google"
argument_list|,
literal|2
argument_list|)
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testOrderedValues ()
specifier|public
name|void
name|testOrderedValues
parameter_list|()
block|{
name|TreeMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|createPopulate
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|multimap
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|7
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|,
literal|null
argument_list|,
literal|0
argument_list|,
literal|6
argument_list|,
literal|2
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testComparator ()
specifier|public
name|void
name|testComparator
parameter_list|()
block|{
name|TreeMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|createPopulate
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|DECREASING_INT_COMPARATOR
argument_list|,
name|multimap
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|comparator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DECREASING_INT_COMPARATOR
argument_list|,
name|multimap
operator|.
name|get
argument_list|(
literal|"missing"
argument_list|)
operator|.
name|comparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testMultimapComparators ()
specifier|public
name|void
name|testMultimapComparators
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|create
argument_list|()
decl_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|putAll
argument_list|(
literal|"foo"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|-
literal|1
argument_list|,
literal|2
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|putAll
argument_list|(
literal|"bar"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|TreeMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|copy
init|=
name|TreeMultimap
operator|.
name|create
argument_list|(
name|StringLength
operator|.
name|COMPARATOR
argument_list|,
name|DECREASING_INT_COMPARATOR
argument_list|)
decl_stmt|;
name|copy
operator|.
name|putAll
argument_list|(
name|multimap
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|multimap
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|StringLength
operator|.
name|COMPARATOR
argument_list|,
name|copy
operator|.
name|keyComparator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DECREASING_INT_COMPARATOR
argument_list|,
name|copy
operator|.
name|valueComparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSortedKeySet ()
specifier|public
name|void
name|testSortedKeySet
parameter_list|()
block|{
name|TreeMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|createPopulate
argument_list|()
decl_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|keySet
init|=
name|multimap
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|keySet
operator|.
name|first
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"google"
argument_list|,
name|keySet
operator|.
name|last
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|StringLength
operator|.
name|COMPARATOR
argument_list|,
name|keySet
operator|.
name|comparator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|null
argument_list|,
literal|"tree"
argument_list|)
argument_list|,
name|keySet
operator|.
name|headSet
argument_list|(
literal|"yahoo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"google"
argument_list|)
argument_list|,
name|keySet
operator|.
name|tailSet
argument_list|(
literal|"yahoo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"tree"
argument_list|)
argument_list|,
name|keySet
operator|.
name|subSet
argument_list|(
literal|"ask"
argument_list|,
literal|"yahoo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// SerializableTester
DECL|method|testExplicitComparatorSerialization ()
specifier|public
name|void
name|testExplicitComparatorSerialization
parameter_list|()
block|{
name|TreeMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|createPopulate
argument_list|()
decl_stmt|;
name|TreeMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|copy
init|=
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|multimap
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|copy
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|7
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|,
literal|null
argument_list|,
literal|0
argument_list|,
literal|6
argument_list|,
literal|2
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|copy
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|null
argument_list|,
literal|"tree"
argument_list|,
literal|"google"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|multimap
operator|.
name|keyComparator
argument_list|()
argument_list|,
name|copy
operator|.
name|keyComparator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|multimap
operator|.
name|valueComparator
argument_list|()
argument_list|,
name|copy
operator|.
name|valueComparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

