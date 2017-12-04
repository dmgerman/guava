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
name|checkState
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
name|Equivalence
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
name|SortedMap
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentSkipListMap
import|;
end_import

begin_comment
comment|/**  * Helper classes for various benchmarks.  *  * @author Christopher Swenson  */
end_comment

begin_class
DECL|class|BenchmarkHelpers
specifier|final
class|class
name|BenchmarkHelpers
block|{
comment|/** So far, this is the best way to test various implementations of {@link Set} subclasses. */
DECL|interface|CollectionsImplEnum
specifier|public
interface|interface
name|CollectionsImplEnum
block|{
DECL|method|create (Collection<E> contents)
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
parameter_list|>
name|Collection
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
function_decl|;
DECL|method|name ()
name|String
name|name
parameter_list|()
function_decl|;
block|}
DECL|interface|MapsImplEnum
specifier|public
interface|interface
name|MapsImplEnum
block|{
DECL|method|create (Map<K, V> contents)
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|contents
parameter_list|)
function_decl|;
DECL|method|name ()
name|String
name|name
parameter_list|()
function_decl|;
block|}
DECL|interface|InternerImplEnum
specifier|public
interface|interface
name|InternerImplEnum
block|{
DECL|method|create (Collection<E> contents)
parameter_list|<
name|E
parameter_list|>
name|Interner
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
function_decl|;
DECL|method|name ()
name|String
name|name
parameter_list|()
function_decl|;
block|}
DECL|enum|SetImpl
specifier|public
enum|enum
name|SetImpl
implements|implements
name|CollectionsImplEnum
block|{
DECL|enumConstant|HashSetImpl
name|HashSetImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
block|{
return|return
operator|new
name|HashSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|LinkedHashSetImpl
name|LinkedHashSetImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
block|{
return|return
operator|new
name|LinkedHashSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|TreeSetImpl
name|TreeSetImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
block|{
return|return
operator|new
name|TreeSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|UnmodifiableSetImpl
name|UnmodifiableSetImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
operator|new
name|HashSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|contents
argument_list|)
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|SynchronizedSetImpl
name|SynchronizedSetImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|synchronizedSet
argument_list|(
operator|new
name|HashSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|contents
argument_list|)
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|ImmutableSetImpl
name|ImmutableSetImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|ImmutableSortedSetImpl
name|ImmutableSortedSetImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|ImmutableSortedSet
operator|.
name|copyOf
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|ContiguousSetImpl
name|ContiguousSetImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|ContiguousSet
operator|.
name|copyOf
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|,
comment|//    @GoogleInternal
comment|//    CompactHashSetImpl {
comment|//      @Override
comment|//      public<E extends Comparable<E>> Set<E> create(Collection<E> contents) {
comment|//        return CompactHashSet.create(contents);
comment|//      }
comment|//    },
comment|//    @GoogleInternal
comment|//    CompactLinkedHashSetImpl {
comment|//      @Override
comment|//      public<E extends Comparable<E>> Set<E> create(Collection<E> contents) {
comment|//        return CompactLinkedHashSet.create(contents);
comment|//      }
comment|//    },
block|;   }
DECL|enum|ListMultimapImpl
specifier|public
enum|enum
name|ListMultimapImpl
block|{
DECL|enumConstant|ArrayListMultimapImpl
name|ArrayListMultimapImpl
block|{
annotation|@
name|Override
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|ArrayListMultimap
operator|.
name|create
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|LinkedListMultimapImpl
name|LinkedListMultimapImpl
block|{
annotation|@
name|Override
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|LinkedListMultimap
operator|.
name|create
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|ImmutableListMultimapImpl
name|ImmutableListMultimapImpl
block|{
annotation|@
name|Override
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|ImmutableListMultimap
operator|.
name|copyOf
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|;
DECL|method|create (Multimap<K, V> contents)
specifier|abstract
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|contents
parameter_list|)
function_decl|;
block|}
DECL|enum|RangeSetImpl
specifier|public
enum|enum
name|RangeSetImpl
block|{
DECL|enumConstant|TreeRangeSetImpl
name|TreeRangeSetImpl
block|{
annotation|@
name|Override
argument_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
argument_list|>
name|RangeSet
argument_list|<
name|K
argument_list|>
name|create
parameter_list|(
name|RangeSet
argument_list|<
name|K
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|TreeRangeSet
operator|.
name|create
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|ImmutableRangeSetImpl
name|ImmutableRangeSetImpl
block|{
annotation|@
name|Override
argument_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
argument_list|>
name|RangeSet
argument_list|<
name|K
argument_list|>
name|create
parameter_list|(
name|RangeSet
argument_list|<
name|K
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|ImmutableRangeSet
operator|.
name|copyOf
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|;
DECL|method|create (RangeSet<K> contents)
specifier|abstract
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
parameter_list|>
name|RangeSet
argument_list|<
name|K
argument_list|>
name|create
parameter_list|(
name|RangeSet
argument_list|<
name|K
argument_list|>
name|contents
parameter_list|)
function_decl|;
block|}
DECL|enum|SetMultimapImpl
specifier|public
enum|enum
name|SetMultimapImpl
block|{
DECL|enumConstant|HashMultimapImpl
name|HashMultimapImpl
block|{
annotation|@
name|Override
argument_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
argument_list|,
name|V
extends|extends
name|Comparable
argument_list|<
name|V
argument_list|>
argument_list|>
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|HashMultimap
operator|.
name|create
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|LinkedHashMultimapImpl
name|LinkedHashMultimapImpl
block|{
annotation|@
name|Override
argument_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
argument_list|,
name|V
extends|extends
name|Comparable
argument_list|<
name|V
argument_list|>
argument_list|>
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|LinkedHashMultimap
operator|.
name|create
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|TreeMultimapImpl
name|TreeMultimapImpl
block|{
annotation|@
name|Override
argument_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
argument_list|,
name|V
extends|extends
name|Comparable
argument_list|<
name|V
argument_list|>
argument_list|>
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|TreeMultimap
operator|.
name|create
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|ImmutableSetMultimapImpl
name|ImmutableSetMultimapImpl
block|{
annotation|@
name|Override
argument_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
argument_list|,
name|V
extends|extends
name|Comparable
argument_list|<
name|V
argument_list|>
argument_list|>
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|ImmutableSetMultimap
operator|.
name|copyOf
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|;
DECL|method|create ( Multimap<K, V> contents)
specifier|abstract
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
parameter_list|,
name|V
extends|extends
name|Comparable
argument_list|<
name|V
argument_list|>
parameter_list|>
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|contents
parameter_list|)
function_decl|;
block|}
DECL|enum|MapImpl
specifier|public
enum|enum
name|MapImpl
implements|implements
name|MapsImplEnum
block|{
DECL|enumConstant|HashMapImpl
name|HashMapImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
return|return
name|Maps
operator|.
name|newHashMap
argument_list|(
name|map
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|LinkedHashMapImpl
name|LinkedHashMapImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
return|return
name|Maps
operator|.
name|newLinkedHashMap
argument_list|(
name|map
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|ConcurrentHashMapImpl
name|ConcurrentHashMapImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
return|return
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|(
name|map
argument_list|)
return|;
block|}
block|}
block|,
comment|//    @GoogleInternal
comment|//    CompactHashmapImpl {
comment|//      @Override
comment|//      public<K extends Comparable<K>, V> Map<K, V> create(Map<K, V> map) {
comment|//        Map<K, V> result = CompactHashMap.createWithExpectedSize(map.size());
comment|//        result.putAll(map);
comment|//        return result;
comment|//      }
comment|//    },
comment|//    @GoogleInternal
comment|//    CompactLinkedHashmapImpl {
comment|//      @Override
comment|//      public<K extends Comparable<K>, V> Map<K, V> create(Map<K, V> map) {
comment|//        Map<K, V> result = CompactLinkedHashMap.createWithExpectedSize(map.size());
comment|//        result.putAll(map);
comment|//        return result;
comment|//      }
comment|//    },
DECL|enumConstant|ImmutableMapImpl
name|ImmutableMapImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
return|return
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|map
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|MapMakerStrongKeysStrongValues
name|MapMakerStrongKeysStrongValues
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
comment|// We use a "custom" equivalence to force MapMaker to make a MapMakerInternalMap.
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newMap
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|keyEquivalence
argument_list|(
name|Equivalence
operator|.
name|equals
argument_list|()
argument_list|)
operator|.
name|makeMap
argument_list|()
decl_stmt|;
name|checkState
argument_list|(
name|newMap
operator|instanceof
name|MapMakerInternalMap
argument_list|)
expr_stmt|;
name|newMap
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
return|return
name|newMap
return|;
block|}
block|}
block|,
DECL|enumConstant|MapMakerStrongKeysWeakValues
name|MapMakerStrongKeysWeakValues
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newMap
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|weakValues
argument_list|()
operator|.
name|makeMap
argument_list|()
decl_stmt|;
name|checkState
argument_list|(
name|newMap
operator|instanceof
name|MapMakerInternalMap
argument_list|)
expr_stmt|;
name|newMap
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
return|return
name|newMap
return|;
block|}
block|}
block|,
DECL|enumConstant|MapMakerWeakKeysStrongValues
name|MapMakerWeakKeysStrongValues
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newMap
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|weakKeys
argument_list|()
operator|.
name|makeMap
argument_list|()
decl_stmt|;
name|checkState
argument_list|(
name|newMap
operator|instanceof
name|MapMakerInternalMap
argument_list|)
expr_stmt|;
name|newMap
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
return|return
name|newMap
return|;
block|}
block|}
block|,
DECL|enumConstant|MapMakerWeakKeysWeakValues
name|MapMakerWeakKeysWeakValues
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newMap
init|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|weakKeys
argument_list|()
operator|.
name|weakValues
argument_list|()
operator|.
name|makeMap
argument_list|()
decl_stmt|;
name|checkState
argument_list|(
name|newMap
operator|instanceof
name|MapMakerInternalMap
argument_list|)
expr_stmt|;
name|newMap
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
return|return
name|newMap
return|;
block|}
block|}
block|;   }
DECL|enum|SortedMapImpl
enum|enum
name|SortedMapImpl
implements|implements
name|MapsImplEnum
block|{
DECL|enumConstant|TreeMapImpl
name|TreeMapImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|result
init|=
name|Maps
operator|.
name|newTreeMap
argument_list|()
decl_stmt|;
name|result
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
block|,
DECL|enumConstant|ConcurrentSkipListImpl
name|ConcurrentSkipListImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
return|return
operator|new
name|ConcurrentSkipListMap
argument_list|<>
argument_list|(
name|map
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|ImmutableSortedMapImpl
name|ImmutableSortedMapImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
return|return
name|ImmutableSortedMap
operator|.
name|copyOf
argument_list|(
name|map
argument_list|)
return|;
block|}
block|}
block|;   }
DECL|enum|BiMapImpl
enum|enum
name|BiMapImpl
implements|implements
name|MapsImplEnum
block|{
DECL|enumConstant|HashBiMapImpl
name|HashBiMapImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|BiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
return|return
name|HashBiMap
operator|.
name|create
argument_list|(
name|map
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|ImmutableBiMapImpl
name|ImmutableBiMapImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|BiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
return|return
name|ImmutableBiMap
operator|.
name|copyOf
argument_list|(
name|map
argument_list|)
return|;
block|}
block|}
block|;
annotation|@
name|Override
DECL|method|create (Map<K, V> map)
specifier|public
specifier|abstract
parameter_list|<
name|K
extends|extends
name|Comparable
argument_list|<
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|BiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
function_decl|;
block|}
DECL|enum|MultisetImpl
enum|enum
name|MultisetImpl
implements|implements
name|CollectionsImplEnum
block|{
DECL|enumConstant|HashMultisetImpl
name|HashMultisetImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
parameter_list|>
name|Multiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|HashMultiset
operator|.
name|create
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|LinkedHashMultisetImpl
name|LinkedHashMultisetImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
parameter_list|>
name|Multiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|LinkedHashMultiset
operator|.
name|create
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|ConcurrentHashMultisetImpl
name|ConcurrentHashMultisetImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
parameter_list|>
name|Multiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|ConcurrentHashMultiset
operator|.
name|create
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|ImmutableMultisetImpl
name|ImmutableMultisetImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
parameter_list|>
name|Multiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|ImmutableMultiset
operator|.
name|copyOf
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|;   }
DECL|enum|SortedMultisetImpl
enum|enum
name|SortedMultisetImpl
implements|implements
name|CollectionsImplEnum
block|{
DECL|enumConstant|TreeMultisetImpl
name|TreeMultisetImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
parameter_list|>
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|TreeMultiset
operator|.
name|create
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|ImmutableSortedMultisetImpl
name|ImmutableSortedMultisetImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
parameter_list|>
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|ImmutableSortedMultiset
operator|.
name|copyOf
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|;   }
DECL|enum|QueueImpl
enum|enum
name|QueueImpl
implements|implements
name|CollectionsImplEnum
block|{
DECL|enumConstant|MinMaxPriorityQueueImpl
name|MinMaxPriorityQueueImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
parameter_list|>
name|Queue
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|MinMaxPriorityQueue
operator|.
name|create
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|;   }
DECL|enum|TableImpl
enum|enum
name|TableImpl
block|{
DECL|enumConstant|HashBasedTableImpl
name|HashBasedTableImpl
block|{
annotation|@
name|Override
argument_list|<
name|R
extends|extends
name|Comparable
argument_list|<
name|R
argument_list|>
argument_list|,
name|C
extends|extends
name|Comparable
argument_list|<
name|C
argument_list|>
argument_list|,
name|V
argument_list|>
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|HashBasedTable
operator|.
name|create
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|TreeBasedTableImpl
name|TreeBasedTableImpl
block|{
annotation|@
name|Override
argument_list|<
name|R
extends|extends
name|Comparable
argument_list|<
name|R
argument_list|>
argument_list|,
name|C
extends|extends
name|Comparable
argument_list|<
name|C
argument_list|>
argument_list|,
name|V
argument_list|>
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|contents
parameter_list|)
block|{
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|table
init|=
name|TreeBasedTable
operator|.
name|create
argument_list|()
decl_stmt|;
name|table
operator|.
name|putAll
argument_list|(
name|contents
argument_list|)
expr_stmt|;
return|return
name|table
return|;
block|}
block|}
block|,
DECL|enumConstant|ArrayTableImpl
name|ArrayTableImpl
block|{
annotation|@
name|Override
argument_list|<
name|R
extends|extends
name|Comparable
argument_list|<
name|R
argument_list|>
argument_list|,
name|C
extends|extends
name|Comparable
argument_list|<
name|C
argument_list|>
argument_list|,
name|V
argument_list|>
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|contents
parameter_list|)
block|{
if|if
condition|(
name|contents
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|ImmutableTable
operator|.
name|of
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|ArrayTable
operator|.
name|create
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|}
block|,
DECL|enumConstant|ImmutableTableImpl
name|ImmutableTableImpl
block|{
annotation|@
name|Override
argument_list|<
name|R
extends|extends
name|Comparable
argument_list|<
name|R
argument_list|>
argument_list|,
name|C
extends|extends
name|Comparable
argument_list|<
name|C
argument_list|>
argument_list|,
name|V
argument_list|>
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|contents
parameter_list|)
block|{
return|return
name|ImmutableTable
operator|.
name|copyOf
argument_list|(
name|contents
argument_list|)
return|;
block|}
block|}
block|;
DECL|method|create ( Table<R, C, V> contents)
specifier|abstract
parameter_list|<
name|R
extends|extends
name|Comparable
argument_list|<
name|R
argument_list|>
parameter_list|,
name|C
extends|extends
name|Comparable
argument_list|<
name|C
argument_list|>
parameter_list|,
name|V
parameter_list|>
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|contents
parameter_list|)
function_decl|;
block|}
DECL|enum|InternerImpl
specifier|public
enum|enum
name|InternerImpl
implements|implements
name|InternerImplEnum
block|{
DECL|enumConstant|WeakInternerImpl
name|WeakInternerImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
parameter_list|>
name|Interner
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
block|{
name|Interner
argument_list|<
name|E
argument_list|>
name|interner
init|=
name|Interners
operator|.
name|newWeakInterner
argument_list|()
decl_stmt|;
for|for
control|(
name|E
name|e
range|:
name|contents
control|)
block|{
name|interner
operator|.
name|intern
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|interner
return|;
block|}
block|}
block|,
DECL|enumConstant|StrongInternerImpl
name|StrongInternerImpl
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
parameter_list|>
name|Interner
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|contents
parameter_list|)
block|{
name|Interner
argument_list|<
name|E
argument_list|>
name|interner
init|=
name|Interners
operator|.
name|newStrongInterner
argument_list|()
decl_stmt|;
for|for
control|(
name|E
name|e
range|:
name|contents
control|)
block|{
name|interner
operator|.
name|intern
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|interner
return|;
block|}
block|}
block|;   }
DECL|enum|Value
specifier|public
enum|enum
name|Value
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;   }
DECL|enum|ListSizeDistribution
specifier|public
enum|enum
name|ListSizeDistribution
block|{
DECL|enumConstant|UNIFORM_0_TO_2
name|UNIFORM_0_TO_2
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|)
block|,
DECL|enumConstant|UNIFORM_0_TO_9
name|UNIFORM_0_TO_9
argument_list|(
literal|0
argument_list|,
literal|9
argument_list|)
block|,
DECL|enumConstant|ALWAYS_0
name|ALWAYS_0
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
block|,
DECL|enumConstant|ALWAYS_10
name|ALWAYS_10
argument_list|(
literal|10
argument_list|,
literal|10
argument_list|)
block|;
DECL|field|min
specifier|final
name|int
name|min
decl_stmt|;
DECL|field|max
specifier|final
name|int
name|max
decl_stmt|;
DECL|method|ListSizeDistribution (int min, int max)
specifier|private
name|ListSizeDistribution
parameter_list|(
name|int
name|min
parameter_list|,
name|int
name|max
parameter_list|)
block|{
name|this
operator|.
name|min
operator|=
name|min
expr_stmt|;
name|this
operator|.
name|max
operator|=
name|max
expr_stmt|;
block|}
DECL|method|chooseSize (Random random)
specifier|public
name|int
name|chooseSize
parameter_list|(
name|Random
name|random
parameter_list|)
block|{
return|return
name|random
operator|.
name|nextInt
argument_list|(
name|max
operator|-
name|min
operator|+
literal|1
argument_list|)
operator|+
name|min
return|;
block|}
block|}
block|}
end_class

end_unit

