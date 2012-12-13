begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.google
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
operator|.
name|google
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
name|checkNotNull
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
name|Sets
operator|.
name|newTreeSet
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
name|SampleElements
operator|.
name|Strings
operator|.
name|AFTER_LAST
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
name|SampleElements
operator|.
name|Strings
operator|.
name|AFTER_LAST_2
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
name|SampleElements
operator|.
name|Strings
operator|.
name|BEFORE_FIRST
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
name|SampleElements
operator|.
name|Strings
operator|.
name|BEFORE_FIRST_2
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|assertEquals
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
name|ContiguousSet
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
name|DiscreteDomain
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
name|ImmutableSet
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
name|ImmutableSortedSet
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Ordering
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
name|Range
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
name|TestCollectionGenerator
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
name|TestCollidingSetGenerator
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
name|TestIntegerSortedSetGenerator
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
name|TestSetGenerator
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
name|TestStringListGenerator
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
name|TestStringSetGenerator
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
name|TestStringSortedSetGenerator
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
name|TestUnhashableCollectionGenerator
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
name|UnhashableObject
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
comment|/**  * Generators of different types of sets and derived collections from sets.  *  * @author Kevin Bourrillion  * @author Jared Levy  * @author Hayward Chan  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|SetGenerators
specifier|public
class|class
name|SetGenerators
block|{
DECL|class|ImmutableSetCopyOfGenerator
specifier|public
specifier|static
class|class
name|ImmutableSetCopyOfGenerator
extends|extends
name|TestStringSetGenerator
block|{
DECL|method|create (String[] elements)
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|elements
argument_list|)
return|;
block|}
block|}
DECL|class|ImmutableSetWithBadHashesGenerator
specifier|public
specifier|static
class|class
name|ImmutableSetWithBadHashesGenerator
extends|extends
name|TestCollidingSetGenerator
comment|// Work around a GWT compiler bug.  Not explicitly listing this will
comment|// cause the createArray() method missing in the generated javascript.
comment|// TODO: Remove this once the GWT bug is fixed.
implements|implements
name|TestCollectionGenerator
argument_list|<
name|Object
argument_list|>
block|{
annotation|@
name|Override
DECL|method|create (Object... elements)
specifier|public
name|Set
argument_list|<
name|Object
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|elements
parameter_list|)
block|{
return|return
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|elements
argument_list|)
return|;
block|}
block|}
DECL|class|DegeneratedImmutableSetGenerator
specifier|public
specifier|static
class|class
name|DegeneratedImmutableSetGenerator
extends|extends
name|TestStringSetGenerator
block|{
comment|// Make sure we get what we think we're getting, or else this test
comment|// is pointless
annotation|@
name|SuppressWarnings
argument_list|(
literal|"cast"
argument_list|)
DECL|method|create (String[] elements)
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
operator|(
name|ImmutableSet
argument_list|<
name|String
argument_list|>
operator|)
name|ImmutableSet
operator|.
name|of
argument_list|(
name|elements
index|[
literal|0
index|]
argument_list|,
name|elements
index|[
literal|0
index|]
argument_list|)
return|;
block|}
block|}
DECL|class|ImmutableSortedSetCopyOfGenerator
specifier|public
specifier|static
class|class
name|ImmutableSortedSetCopyOfGenerator
extends|extends
name|TestStringSortedSetGenerator
block|{
DECL|method|create (String[] elements)
annotation|@
name|Override
specifier|protected
name|SortedSet
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
name|ImmutableSortedSet
operator|.
name|copyOf
argument_list|(
name|elements
argument_list|)
return|;
block|}
block|}
DECL|class|ImmutableSortedSetHeadsetGenerator
specifier|public
specifier|static
class|class
name|ImmutableSortedSetHeadsetGenerator
extends|extends
name|TestStringSortedSetGenerator
block|{
DECL|method|create (String[] elements)
annotation|@
name|Override
specifier|protected
name|SortedSet
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
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
name|elements
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"zzz"
argument_list|)
expr_stmt|;
return|return
name|ImmutableSortedSet
operator|.
name|copyOf
argument_list|(
name|list
argument_list|)
operator|.
name|headSet
argument_list|(
literal|"zzy"
argument_list|)
return|;
block|}
block|}
DECL|class|ImmutableSortedSetTailsetGenerator
specifier|public
specifier|static
class|class
name|ImmutableSortedSetTailsetGenerator
extends|extends
name|TestStringSortedSetGenerator
block|{
DECL|method|create (String[] elements)
annotation|@
name|Override
specifier|protected
name|SortedSet
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
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
name|elements
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"\0"
argument_list|)
expr_stmt|;
return|return
name|ImmutableSortedSet
operator|.
name|copyOf
argument_list|(
name|list
argument_list|)
operator|.
name|tailSet
argument_list|(
literal|"\0\0"
argument_list|)
return|;
block|}
block|}
DECL|class|ImmutableSortedSetSubsetGenerator
specifier|public
specifier|static
class|class
name|ImmutableSortedSetSubsetGenerator
extends|extends
name|TestStringSortedSetGenerator
block|{
DECL|method|create (String[] elements)
annotation|@
name|Override
specifier|protected
name|SortedSet
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
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
name|elements
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"\0"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"zzz"
argument_list|)
expr_stmt|;
return|return
name|ImmutableSortedSet
operator|.
name|copyOf
argument_list|(
name|list
argument_list|)
operator|.
name|subSet
argument_list|(
literal|"\0\0"
argument_list|,
literal|"zzy"
argument_list|)
return|;
block|}
block|}
DECL|class|ImmutableSortedSetExplicitComparator
specifier|public
specifier|static
class|class
name|ImmutableSortedSetExplicitComparator
extends|extends
name|TestStringSetGenerator
block|{
DECL|field|STRING_REVERSED
specifier|private
specifier|static
specifier|final
name|Comparator
argument_list|<
name|String
argument_list|>
name|STRING_REVERSED
init|=
name|Collections
operator|.
name|reverseOrder
argument_list|()
decl_stmt|;
DECL|method|create (String[] elements)
annotation|@
name|Override
specifier|protected
name|SortedSet
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
name|ImmutableSortedSet
operator|.
name|orderedBy
argument_list|(
name|STRING_REVERSED
argument_list|)
operator|.
name|add
argument_list|(
name|elements
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|order (List<String> insertionOrder)
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|insertionOrder
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|insertionOrder
argument_list|,
name|Collections
operator|.
name|reverseOrder
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|insertionOrder
return|;
block|}
block|}
DECL|class|ImmutableSortedSetExplicitSuperclassComparatorGenerator
specifier|public
specifier|static
class|class
name|ImmutableSortedSetExplicitSuperclassComparatorGenerator
extends|extends
name|TestStringSetGenerator
block|{
DECL|field|COMPARABLE_REVERSED
specifier|private
specifier|static
specifier|final
name|Comparator
argument_list|<
name|Comparable
argument_list|<
name|?
argument_list|>
argument_list|>
name|COMPARABLE_REVERSED
init|=
name|Collections
operator|.
name|reverseOrder
argument_list|()
decl_stmt|;
DECL|method|create (String[] elements)
annotation|@
name|Override
specifier|protected
name|SortedSet
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
operator|new
name|ImmutableSortedSet
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
argument_list|(
name|COMPARABLE_REVERSED
argument_list|)
operator|.
name|add
argument_list|(
name|elements
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|order (List<String> insertionOrder)
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|insertionOrder
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|insertionOrder
argument_list|,
name|Collections
operator|.
name|reverseOrder
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|insertionOrder
return|;
block|}
block|}
DECL|class|ImmutableSortedSetReversedOrderGenerator
specifier|public
specifier|static
class|class
name|ImmutableSortedSetReversedOrderGenerator
extends|extends
name|TestStringSetGenerator
block|{
DECL|method|create (String[] elements)
annotation|@
name|Override
specifier|protected
name|SortedSet
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
name|ImmutableSortedSet
operator|.
expr|<
name|String
operator|>
name|reverseOrder
argument_list|()
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
operator|.
name|iterator
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|order (List<String> insertionOrder)
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|insertionOrder
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|insertionOrder
argument_list|,
name|Collections
operator|.
name|reverseOrder
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|insertionOrder
return|;
block|}
block|}
DECL|class|ImmutableSortedSetUnhashableGenerator
specifier|public
specifier|static
class|class
name|ImmutableSortedSetUnhashableGenerator
extends|extends
name|TestUnhashableSetGenerator
block|{
DECL|method|create ( UnhashableObject[] elements)
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|UnhashableObject
argument_list|>
name|create
parameter_list|(
name|UnhashableObject
index|[]
name|elements
parameter_list|)
block|{
return|return
name|ImmutableSortedSet
operator|.
name|copyOf
argument_list|(
name|elements
argument_list|)
return|;
block|}
block|}
DECL|class|ImmutableSetAsListGenerator
specifier|public
specifier|static
class|class
name|ImmutableSetAsListGenerator
extends|extends
name|TestStringListGenerator
block|{
DECL|method|create (String[] elements)
annotation|@
name|Override
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|elements
argument_list|)
operator|.
name|asList
argument_list|()
return|;
block|}
block|}
DECL|class|ImmutableSortedSetAsListGenerator
specifier|public
specifier|static
class|class
name|ImmutableSortedSetAsListGenerator
extends|extends
name|TestStringListGenerator
block|{
DECL|method|create (String[] elements)
annotation|@
name|Override
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
name|Comparator
argument_list|<
name|String
argument_list|>
name|comparator
init|=
name|createExplicitComparator
argument_list|(
name|elements
argument_list|)
decl_stmt|;
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|set
init|=
name|ImmutableSortedSet
operator|.
name|copyOf
argument_list|(
name|comparator
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|set
operator|.
name|asList
argument_list|()
return|;
block|}
block|}
DECL|class|ImmutableSortedSetSubsetAsListGenerator
specifier|public
specifier|static
class|class
name|ImmutableSortedSetSubsetAsListGenerator
extends|extends
name|TestStringListGenerator
block|{
DECL|method|create (String[] elements)
annotation|@
name|Override
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
name|Comparator
argument_list|<
name|String
argument_list|>
name|comparator
init|=
name|createExplicitComparator
argument_list|(
name|elements
argument_list|)
decl_stmt|;
name|ImmutableSortedSet
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
name|builder
init|=
name|ImmutableSortedSet
operator|.
name|orderedBy
argument_list|(
name|comparator
argument_list|)
decl_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|BEFORE_FIRST
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|elements
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|AFTER_LAST
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
operator|.
name|subSet
argument_list|(
name|BEFORE_FIRST_2
argument_list|,
name|AFTER_LAST
argument_list|)
operator|.
name|asList
argument_list|()
return|;
block|}
block|}
DECL|class|ImmutableSortedSetAsListSubListGenerator
specifier|public
specifier|static
class|class
name|ImmutableSortedSetAsListSubListGenerator
extends|extends
name|TestStringListGenerator
block|{
DECL|method|create (String[] elements)
annotation|@
name|Override
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
name|Comparator
argument_list|<
name|String
argument_list|>
name|comparator
init|=
name|createExplicitComparator
argument_list|(
name|elements
argument_list|)
decl_stmt|;
name|ImmutableSortedSet
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
name|builder
init|=
name|ImmutableSortedSet
operator|.
name|orderedBy
argument_list|(
name|comparator
argument_list|)
decl_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|BEFORE_FIRST
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|elements
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|AFTER_LAST
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
operator|.
name|asList
argument_list|()
operator|.
name|subList
argument_list|(
literal|1
argument_list|,
name|elements
operator|.
name|length
operator|+
literal|1
argument_list|)
return|;
block|}
block|}
DECL|class|ImmutableSortedSetSubsetAsListSubListGenerator
specifier|public
specifier|static
class|class
name|ImmutableSortedSetSubsetAsListSubListGenerator
extends|extends
name|TestStringListGenerator
block|{
DECL|method|create (String[] elements)
annotation|@
name|Override
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
name|Comparator
argument_list|<
name|String
argument_list|>
name|comparator
init|=
name|createExplicitComparator
argument_list|(
name|elements
argument_list|)
decl_stmt|;
name|ImmutableSortedSet
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
name|builder
init|=
name|ImmutableSortedSet
operator|.
name|orderedBy
argument_list|(
name|comparator
argument_list|)
decl_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|BEFORE_FIRST
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|BEFORE_FIRST_2
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|elements
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|AFTER_LAST
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|AFTER_LAST_2
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
operator|.
name|subSet
argument_list|(
name|BEFORE_FIRST_2
argument_list|,
name|AFTER_LAST_2
argument_list|)
operator|.
name|asList
argument_list|()
operator|.
name|subList
argument_list|(
literal|1
argument_list|,
name|elements
operator|.
name|length
operator|+
literal|1
argument_list|)
return|;
block|}
block|}
DECL|class|TestUnhashableSetGenerator
specifier|public
specifier|abstract
specifier|static
class|class
name|TestUnhashableSetGenerator
extends|extends
name|TestUnhashableCollectionGenerator
argument_list|<
name|Set
argument_list|<
name|UnhashableObject
argument_list|>
argument_list|>
implements|implements
name|TestSetGenerator
argument_list|<
name|UnhashableObject
argument_list|>
block|{   }
DECL|method|createExplicitComparator ( String[] elements)
specifier|private
specifier|static
name|Ordering
argument_list|<
name|String
argument_list|>
name|createExplicitComparator
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
comment|// Collapse equal elements, which Ordering.explicit() doesn't support, while
comment|// maintaining the ordering by first occurrence.
name|Set
argument_list|<
name|String
argument_list|>
name|elementsPlus
init|=
name|Sets
operator|.
name|newLinkedHashSet
argument_list|()
decl_stmt|;
name|elementsPlus
operator|.
name|add
argument_list|(
name|BEFORE_FIRST
argument_list|)
expr_stmt|;
name|elementsPlus
operator|.
name|add
argument_list|(
name|BEFORE_FIRST_2
argument_list|)
expr_stmt|;
name|elementsPlus
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
expr_stmt|;
name|elementsPlus
operator|.
name|add
argument_list|(
name|AFTER_LAST
argument_list|)
expr_stmt|;
name|elementsPlus
operator|.
name|add
argument_list|(
name|AFTER_LAST_2
argument_list|)
expr_stmt|;
return|return
name|Ordering
operator|.
name|explicit
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
name|elementsPlus
argument_list|)
argument_list|)
return|;
block|}
comment|/*    * All the ContiguousSet generators below manually reject nulls here. In principle, we'd like to    * defer that to Range, since it's ContiguousSet.create() that's used to create the sets. However,    * that gets messy here, and we already have null tests for Range.    */
comment|/*    * These generators also rely on consecutive integer inputs (not necessarily in order, but no    * holes).    */
comment|// SetCreationTester has some tests that pass in duplicates. Dedup them.
DECL|method|nullCheckedTreeSet (E[] elements)
specifier|private
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|SortedSet
argument_list|<
name|E
argument_list|>
name|nullCheckedTreeSet
parameter_list|(
name|E
index|[]
name|elements
parameter_list|)
block|{
name|SortedSet
argument_list|<
name|E
argument_list|>
name|set
init|=
name|newTreeSet
argument_list|()
decl_stmt|;
for|for
control|(
name|E
name|element
range|:
name|elements
control|)
block|{
comment|// Explicit null check because TreeSet wrongly accepts add(null) when empty.
name|set
operator|.
name|add
argument_list|(
name|checkNotNull
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|set
return|;
block|}
DECL|class|ContiguousSetGenerator
specifier|public
specifier|static
class|class
name|ContiguousSetGenerator
extends|extends
name|AbstractContiguousSetGenerator
block|{
DECL|method|create (Integer[] elements)
annotation|@
name|Override
specifier|protected
name|SortedSet
argument_list|<
name|Integer
argument_list|>
name|create
parameter_list|(
name|Integer
index|[]
name|elements
parameter_list|)
block|{
return|return
name|checkedCreate
argument_list|(
name|nullCheckedTreeSet
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|class|ContiguousSetHeadsetGenerator
specifier|public
specifier|static
class|class
name|ContiguousSetHeadsetGenerator
extends|extends
name|AbstractContiguousSetGenerator
block|{
DECL|method|create (Integer[] elements)
annotation|@
name|Override
specifier|protected
name|SortedSet
argument_list|<
name|Integer
argument_list|>
name|create
parameter_list|(
name|Integer
index|[]
name|elements
parameter_list|)
block|{
name|SortedSet
argument_list|<
name|Integer
argument_list|>
name|set
init|=
name|nullCheckedTreeSet
argument_list|(
name|elements
argument_list|)
decl_stmt|;
name|int
name|tooHigh
init|=
operator|(
name|set
operator|.
name|isEmpty
argument_list|()
operator|)
condition|?
literal|0
else|:
name|set
operator|.
name|last
argument_list|()
operator|+
literal|1
decl_stmt|;
name|set
operator|.
name|add
argument_list|(
name|tooHigh
argument_list|)
expr_stmt|;
return|return
name|checkedCreate
argument_list|(
name|set
argument_list|)
operator|.
name|headSet
argument_list|(
name|tooHigh
argument_list|)
return|;
block|}
block|}
DECL|class|ContiguousSetTailsetGenerator
specifier|public
specifier|static
class|class
name|ContiguousSetTailsetGenerator
extends|extends
name|AbstractContiguousSetGenerator
block|{
DECL|method|create (Integer[] elements)
annotation|@
name|Override
specifier|protected
name|SortedSet
argument_list|<
name|Integer
argument_list|>
name|create
parameter_list|(
name|Integer
index|[]
name|elements
parameter_list|)
block|{
name|SortedSet
argument_list|<
name|Integer
argument_list|>
name|set
init|=
name|nullCheckedTreeSet
argument_list|(
name|elements
argument_list|)
decl_stmt|;
name|int
name|tooLow
init|=
operator|(
name|set
operator|.
name|isEmpty
argument_list|()
operator|)
condition|?
literal|0
else|:
name|set
operator|.
name|first
argument_list|()
operator|-
literal|1
decl_stmt|;
name|set
operator|.
name|add
argument_list|(
name|tooLow
argument_list|)
expr_stmt|;
return|return
name|checkedCreate
argument_list|(
name|set
argument_list|)
operator|.
name|tailSet
argument_list|(
name|tooLow
operator|+
literal|1
argument_list|)
return|;
block|}
block|}
DECL|class|ContiguousSetSubsetGenerator
specifier|public
specifier|static
class|class
name|ContiguousSetSubsetGenerator
extends|extends
name|AbstractContiguousSetGenerator
block|{
DECL|method|create (Integer[] elements)
annotation|@
name|Override
specifier|protected
name|SortedSet
argument_list|<
name|Integer
argument_list|>
name|create
parameter_list|(
name|Integer
index|[]
name|elements
parameter_list|)
block|{
name|SortedSet
argument_list|<
name|Integer
argument_list|>
name|set
init|=
name|nullCheckedTreeSet
argument_list|(
name|elements
argument_list|)
decl_stmt|;
if|if
condition|(
name|set
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|/*          * The (tooLow + 1, tooHigh) arguments below would be invalid because tooLow would be          * greater than tooHigh.          */
return|return
name|ContiguousSet
operator|.
name|create
argument_list|(
name|Range
operator|.
name|openClosed
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
argument_list|,
name|DiscreteDomain
operator|.
name|integers
argument_list|()
argument_list|)
operator|.
name|subSet
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
return|;
block|}
name|int
name|tooHigh
init|=
name|set
operator|.
name|last
argument_list|()
operator|+
literal|1
decl_stmt|;
name|int
name|tooLow
init|=
name|set
operator|.
name|first
argument_list|()
operator|-
literal|1
decl_stmt|;
name|set
operator|.
name|add
argument_list|(
name|tooHigh
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
name|tooLow
argument_list|)
expr_stmt|;
return|return
name|checkedCreate
argument_list|(
name|set
argument_list|)
operator|.
name|subSet
argument_list|(
name|tooLow
operator|+
literal|1
argument_list|,
name|tooHigh
argument_list|)
return|;
block|}
block|}
DECL|class|AbstractContiguousSetGenerator
specifier|private
specifier|abstract
specifier|static
class|class
name|AbstractContiguousSetGenerator
extends|extends
name|TestIntegerSortedSetGenerator
block|{
DECL|method|checkedCreate (SortedSet<Integer> elementsSet)
specifier|protected
specifier|final
name|ContiguousSet
argument_list|<
name|Integer
argument_list|>
name|checkedCreate
parameter_list|(
name|SortedSet
argument_list|<
name|Integer
argument_list|>
name|elementsSet
parameter_list|)
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|elements
init|=
name|newArrayList
argument_list|(
name|elementsSet
argument_list|)
decl_stmt|;
comment|/*        * A ContiguousSet can't have holes. If a test demands a hole, it should be changed so that it        * doesn't need one, or it should be suppressed for ContiguousSet.        */
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|elements
operator|.
name|size
argument_list|()
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|elements
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|+
literal|1
argument_list|,
operator|(
name|int
operator|)
name|elements
operator|.
name|get
argument_list|(
name|i
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Range
argument_list|<
name|Integer
argument_list|>
name|range
init|=
operator|(
name|elements
operator|.
name|isEmpty
argument_list|()
operator|)
condition|?
name|Range
operator|.
name|closedOpen
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
else|:
name|Range
operator|.
name|encloseAll
argument_list|(
name|elements
argument_list|)
decl_stmt|;
return|return
name|ContiguousSet
operator|.
name|create
argument_list|(
name|range
argument_list|,
name|DiscreteDomain
operator|.
name|integers
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

