begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|EntryPoint
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
name|List
import|;
end_import

begin_comment
comment|/**  * A dummy entry point that accesses all GWT classes in  * {@code com.google.common.collect}.  *  * @author Hayward Chan  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|class|TestModuleEntryPoint
specifier|public
class|class
name|TestModuleEntryPoint
implements|implements
name|EntryPoint
block|{
annotation|@
name|Override
DECL|method|onModuleLoad ()
specifier|public
name|void
name|onModuleLoad
parameter_list|()
block|{
comment|// TODO: Auto generate this list.
comment|// Files covered by GWT_SRCS
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|allClasses
init|=
name|Arrays
operator|.
expr|<
name|Class
argument_list|<
name|?
argument_list|>
operator|>
name|asList
argument_list|(
name|AbstractIndexedListIterator
operator|.
name|class
argument_list|,
name|AbstractIterator
operator|.
name|class
argument_list|,
name|AbstractListMultimap
operator|.
name|class
argument_list|,
name|AbstractMapEntry
operator|.
name|class
argument_list|,
name|AbstractMultimap
operator|.
name|class
argument_list|,
name|AbstractMultiset
operator|.
name|class
argument_list|,
name|AbstractSetMultimap
operator|.
name|class
argument_list|,
name|AbstractSortedSetMultimap
operator|.
name|class
argument_list|,
name|BiMap
operator|.
name|class
argument_list|,
name|ByFunctionOrdering
operator|.
name|class
argument_list|,
name|ClassToInstanceMap
operator|.
name|class
argument_list|,
name|Collections2
operator|.
name|class
argument_list|,
name|ComparisonChain
operator|.
name|class
argument_list|,
name|ComparatorOrdering
operator|.
name|class
argument_list|,
name|CompoundOrdering
operator|.
name|class
argument_list|,
name|ComputationException
operator|.
name|class
argument_list|,
name|Constraint
operator|.
name|class
argument_list|,
name|Constraints
operator|.
name|class
argument_list|,
name|EmptyImmutableListMultimap
operator|.
name|class
argument_list|,
name|EmptyImmutableMultiset
operator|.
name|class
argument_list|,
name|EmptyImmutableSetMultimap
operator|.
name|class
argument_list|,
name|ExplicitOrdering
operator|.
name|class
argument_list|,
name|ForwardingCollection
operator|.
name|class
argument_list|,
name|ForwardingConcurrentMap
operator|.
name|class
argument_list|,
name|ForwardingIterator
operator|.
name|class
argument_list|,
name|ForwardingList
operator|.
name|class
argument_list|,
name|ForwardingListIterator
operator|.
name|class
argument_list|,
name|ForwardingListMultimap
operator|.
name|class
argument_list|,
name|ForwardingMap
operator|.
name|class
argument_list|,
name|ForwardingMapEntry
operator|.
name|class
argument_list|,
name|ForwardingMultimap
operator|.
name|class
argument_list|,
name|ForwardingMultiset
operator|.
name|class
argument_list|,
name|ForwardingObject
operator|.
name|class
argument_list|,
name|ForwardingQueue
operator|.
name|class
argument_list|,
name|ForwardingSet
operator|.
name|class
argument_list|,
name|ForwardingSetMultimap
operator|.
name|class
argument_list|,
name|ForwardingSortedMap
operator|.
name|class
argument_list|,
name|ForwardingSortedSet
operator|.
name|class
argument_list|,
name|ForwardingSortedSetMultimap
operator|.
name|class
argument_list|,
name|GenericMapMaker
operator|.
name|class
argument_list|,
name|Hashing
operator|.
name|class
argument_list|,
name|ImmutableEntry
operator|.
name|class
argument_list|,
name|ImmutableListMultimap
operator|.
name|class
argument_list|,
name|LexicographicalOrdering
operator|.
name|class
argument_list|,
name|ListMultimap
operator|.
name|class
argument_list|,
name|Lists
operator|.
name|class
argument_list|,
name|MapConstraint
operator|.
name|class
argument_list|,
name|MapConstraints
operator|.
name|class
argument_list|,
name|MapDifference
operator|.
name|class
argument_list|,
name|Maps
operator|.
name|class
argument_list|,
name|Multimap
operator|.
name|class
argument_list|,
name|Multiset
operator|.
name|class
argument_list|,
name|Multisets
operator|.
name|class
argument_list|,
name|NaturalOrdering
operator|.
name|class
argument_list|,
name|NullsFirstOrdering
operator|.
name|class
argument_list|,
name|NullsLastOrdering
operator|.
name|class
argument_list|,
name|ObjectArrays
operator|.
name|class
argument_list|,
name|Ordering
operator|.
name|class
argument_list|,
name|PeekingIterator
operator|.
name|class
argument_list|,
name|ReverseNaturalOrdering
operator|.
name|class
argument_list|,
name|ReverseOrdering
operator|.
name|class
argument_list|,
name|SetMultimap
operator|.
name|class
argument_list|,
name|SortedSetMultimap
operator|.
name|class
argument_list|,
name|UnmodifiableIterator
operator|.
name|class
argument_list|,
name|UsingToStringOrdering
operator|.
name|class
argument_list|,
comment|// Classes covered by :generated_supersource
name|AbstractBiMap
operator|.
name|class
argument_list|,
name|AbstractMapBasedMultiset
operator|.
name|class
argument_list|,
name|ArrayListMultimap
operator|.
name|class
argument_list|,
name|EnumBiMap
operator|.
name|class
argument_list|,
name|EnumHashBiMap
operator|.
name|class
argument_list|,
name|EnumMultiset
operator|.
name|class
argument_list|,
name|HashBiMap
operator|.
name|class
argument_list|,
name|HashMultimap
operator|.
name|class
argument_list|,
name|HashMultiset
operator|.
name|class
argument_list|,
name|ImmutableListMultimap
operator|.
name|class
argument_list|,
name|ImmutableMultimap
operator|.
name|class
argument_list|,
name|ImmutableMultiset
operator|.
name|class
argument_list|,
name|ImmutableSetMultimap
operator|.
name|class
argument_list|,
name|Iterables
operator|.
name|class
argument_list|,
name|Iterators
operator|.
name|class
argument_list|,
name|LinkedHashMultimap
operator|.
name|class
argument_list|,
name|LinkedHashMultiset
operator|.
name|class
argument_list|,
name|LinkedListMultimap
operator|.
name|class
argument_list|,
name|Multimaps
operator|.
name|class
argument_list|,
name|ObjectArrays
operator|.
name|class
argument_list|,
name|Sets
operator|.
name|class
argument_list|,
name|Synchronized
operator|.
name|class
argument_list|,
name|TreeMultimap
operator|.
name|class
argument_list|,
name|TreeMultiset
operator|.
name|class
argument_list|)
decl_stmt|;
block|}
comment|// Reference that make sure an RPC interface is referenced.
block|}
end_class

end_unit

