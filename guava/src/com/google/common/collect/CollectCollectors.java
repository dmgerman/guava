begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|checkNotNull
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
name|Beta
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
name|base
operator|.
name|Preconditions
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
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BinaryOperator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|ToIntFunction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collector
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
import|;
end_import

begin_comment
comment|/** Collectors utilities for {@code common.collect} internals. */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|CollectCollectors
specifier|final
class|class
name|CollectCollectors
block|{
DECL|field|TO_IMMUTABLE_LIST
specifier|private
specifier|static
specifier|final
name|Collector
argument_list|<
name|Object
argument_list|,
name|?
argument_list|,
name|ImmutableList
argument_list|<
name|Object
argument_list|>
argument_list|>
name|TO_IMMUTABLE_LIST
init|=
name|Collector
operator|.
name|of
argument_list|(
name|ImmutableList
operator|::
name|builder
argument_list|,
name|ImmutableList
operator|.
name|Builder
operator|::
name|add
argument_list|,
name|ImmutableList
operator|.
name|Builder
operator|::
name|combine
argument_list|,
name|ImmutableList
operator|.
name|Builder
operator|::
name|build
argument_list|)
decl_stmt|;
DECL|field|TO_IMMUTABLE_SET
specifier|private
specifier|static
specifier|final
name|Collector
argument_list|<
name|Object
argument_list|,
name|?
argument_list|,
name|ImmutableSet
argument_list|<
name|Object
argument_list|>
argument_list|>
name|TO_IMMUTABLE_SET
init|=
name|Collector
operator|.
name|of
argument_list|(
name|ImmutableSet
operator|::
name|builder
argument_list|,
name|ImmutableSet
operator|.
name|Builder
operator|::
name|add
argument_list|,
name|ImmutableSet
operator|.
name|Builder
operator|::
name|combine
argument_list|,
name|ImmutableSet
operator|.
name|Builder
operator|::
name|build
argument_list|)
decl_stmt|;
annotation|@
name|GwtIncompatible
specifier|private
specifier|static
specifier|final
name|Collector
argument_list|<
name|Range
argument_list|<
name|Comparable
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|,
name|?
argument_list|,
name|ImmutableRangeSet
argument_list|<
name|Comparable
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
DECL|field|TO_IMMUTABLE_RANGE_SET
name|TO_IMMUTABLE_RANGE_SET
init|=
name|Collector
operator|.
name|of
argument_list|(
name|ImmutableRangeSet
operator|::
name|builder
argument_list|,
name|ImmutableRangeSet
operator|.
name|Builder
operator|::
name|add
argument_list|,
name|ImmutableRangeSet
operator|.
name|Builder
operator|::
name|combine
argument_list|,
name|ImmutableRangeSet
operator|.
name|Builder
operator|::
name|build
argument_list|)
decl_stmt|;
comment|// Lists
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|toImmutableList ()
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Collector
argument_list|<
name|E
argument_list|,
name|?
argument_list|,
name|ImmutableList
argument_list|<
name|E
argument_list|>
argument_list|>
name|toImmutableList
parameter_list|()
block|{
return|return
operator|(
name|Collector
operator|)
name|TO_IMMUTABLE_LIST
return|;
block|}
comment|// Sets
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|toImmutableSet ()
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Collector
argument_list|<
name|E
argument_list|,
name|?
argument_list|,
name|ImmutableSet
argument_list|<
name|E
argument_list|>
argument_list|>
name|toImmutableSet
parameter_list|()
block|{
return|return
operator|(
name|Collector
operator|)
name|TO_IMMUTABLE_SET
return|;
block|}
DECL|method|toImmutableSortedSet ( Comparator<? super E> comparator)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Collector
argument_list|<
name|E
argument_list|,
name|?
argument_list|,
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
argument_list|>
name|toImmutableSortedSet
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
return|return
name|Collector
operator|.
name|of
argument_list|(
parameter_list|()
lambda|->
operator|new
name|ImmutableSortedSet
operator|.
name|Builder
argument_list|<
name|E
argument_list|>
argument_list|(
name|comparator
argument_list|)
argument_list|,
name|ImmutableSortedSet
operator|.
name|Builder
operator|::
name|add
argument_list|,
name|ImmutableSortedSet
operator|.
name|Builder
operator|::
name|combine
argument_list|,
name|ImmutableSortedSet
operator|.
name|Builder
operator|::
name|build
argument_list|)
return|;
block|}
annotation|@
name|GwtIncompatible
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
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
DECL|method|toImmutableRangeSet ()
name|Collector
argument_list|<
name|Range
argument_list|<
name|E
argument_list|>
argument_list|,
name|?
argument_list|,
name|ImmutableRangeSet
argument_list|<
name|E
argument_list|>
argument_list|>
name|toImmutableRangeSet
parameter_list|()
block|{
return|return
operator|(
name|Collector
operator|)
name|TO_IMMUTABLE_RANGE_SET
return|;
block|}
comment|// Multisets
DECL|method|toImmutableMultiset ( Function<? super T, ? extends E> elementFunction, ToIntFunction<? super T> countFunction)
specifier|static
parameter_list|<
name|T
parameter_list|,
name|E
parameter_list|>
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
argument_list|>
name|toImmutableMultiset
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|E
argument_list|>
name|elementFunction
parameter_list|,
name|ToIntFunction
argument_list|<
name|?
super|super
name|T
argument_list|>
name|countFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|elementFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|countFunction
argument_list|)
expr_stmt|;
return|return
name|Collector
operator|.
name|of
argument_list|(
name|LinkedHashMultiset
operator|::
name|create
argument_list|,
parameter_list|(
name|multiset
parameter_list|,
name|t
parameter_list|)
lambda|->
name|multiset
operator|.
name|add
argument_list|(
name|checkNotNull
argument_list|(
name|elementFunction
operator|.
name|apply
argument_list|(
name|t
argument_list|)
argument_list|)
argument_list|,
name|countFunction
operator|.
name|applyAsInt
argument_list|(
name|t
argument_list|)
argument_list|)
argument_list|,
parameter_list|(
name|multiset1
parameter_list|,
name|multiset2
parameter_list|)
lambda|->
block|{
name|multiset1
operator|.
name|addAll
argument_list|(
name|multiset2
argument_list|)
argument_list|;           return
name|multiset1
argument_list|;
block|}
operator|,
parameter_list|(
name|Multiset
argument_list|<
name|E
argument_list|>
name|multiset
parameter_list|)
lambda|->
name|ImmutableMultiset
operator|.
name|copyFromEntries
argument_list|(
name|multiset
operator|.
name|entrySet
argument_list|()
argument_list|)
block|)
class|;
end_class

begin_function
unit|}    static
DECL|method|toMultiset ( Function<? super T, E> elementFunction, ToIntFunction<? super T> countFunction, Supplier<M> multisetSupplier)
parameter_list|<
name|T
parameter_list|,
name|E
parameter_list|,
name|M
extends|extends
name|Multiset
argument_list|<
name|E
argument_list|>
parameter_list|>
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|M
argument_list|>
name|toMultiset
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|E
argument_list|>
name|elementFunction
parameter_list|,
name|ToIntFunction
argument_list|<
name|?
super|super
name|T
argument_list|>
name|countFunction
parameter_list|,
name|Supplier
argument_list|<
name|M
argument_list|>
name|multisetSupplier
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|elementFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|countFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|multisetSupplier
argument_list|)
expr_stmt|;
return|return
name|Collector
operator|.
name|of
argument_list|(
name|multisetSupplier
argument_list|,
parameter_list|(
name|ms
parameter_list|,
name|t
parameter_list|)
lambda|->
name|ms
operator|.
name|add
argument_list|(
name|elementFunction
operator|.
name|apply
argument_list|(
name|t
argument_list|)
argument_list|,
name|countFunction
operator|.
name|applyAsInt
argument_list|(
name|t
argument_list|)
argument_list|)
argument_list|,
parameter_list|(
name|ms1
parameter_list|,
name|ms2
parameter_list|)
lambda|->
block|{
name|ms1
operator|.
name|addAll
argument_list|(
name|ms2
argument_list|)
argument_list|;           return
name|ms1
argument_list|;
block|}
end_function

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_comment
unit|}
comment|// Maps
end_comment

begin_function
DECL|method|toImmutableMap ( Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction)
unit|static
parameter_list|<
name|T
parameter_list|,
name|K
parameter_list|,
name|V
parameter_list|>
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableMap
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|K
argument_list|>
name|keyFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|keyFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|valueFunction
argument_list|)
expr_stmt|;
return|return
name|Collector
operator|.
name|of
argument_list|(
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|::
operator|new
argument_list|,
parameter_list|(
name|builder
parameter_list|,
name|input
parameter_list|)
lambda|->
name|builder
operator|.
name|put
argument_list|(
name|keyFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|,
name|valueFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|)
argument_list|,
name|ImmutableMap
operator|.
name|Builder
operator|::
name|combine
argument_list|,
name|ImmutableMap
operator|.
name|Builder
operator|::
name|build
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|toImmutableMap ( Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|,
name|K
parameter_list|,
name|V
parameter_list|>
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableMap
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|K
argument_list|>
name|keyFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
parameter_list|,
name|BinaryOperator
argument_list|<
name|V
argument_list|>
name|mergeFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|keyFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|valueFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|mergeFunction
argument_list|)
expr_stmt|;
return|return
name|Collectors
operator|.
name|collectingAndThen
argument_list|(
name|Collectors
operator|.
name|toMap
argument_list|(
name|keyFunction
argument_list|,
name|valueFunction
argument_list|,
name|mergeFunction
argument_list|,
name|LinkedHashMap
operator|::
operator|new
argument_list|)
argument_list|,
name|ImmutableMap
operator|::
name|copyOf
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|toImmutableSortedMap ( Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction)
specifier|static
parameter_list|<
name|T
parameter_list|,
name|K
parameter_list|,
name|V
parameter_list|>
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableSortedMap
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|K
argument_list|>
name|keyFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|keyFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|valueFunction
argument_list|)
expr_stmt|;
comment|/*      * We will always fail if there are duplicate keys, and the keys are always sorted by      * the Comparator, so the entries can come in in arbitrary order -- so we report UNORDERED.      */
return|return
name|Collector
operator|.
name|of
argument_list|(
parameter_list|()
lambda|->
operator|new
name|ImmutableSortedMap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|comparator
argument_list|)
argument_list|,
parameter_list|(
name|builder
parameter_list|,
name|input
parameter_list|)
lambda|->
name|builder
operator|.
name|put
argument_list|(
name|keyFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|,
name|valueFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|)
argument_list|,
name|ImmutableSortedMap
operator|.
name|Builder
operator|::
name|combine
argument_list|,
name|ImmutableSortedMap
operator|.
name|Builder
operator|::
name|build
argument_list|,
name|Collector
operator|.
name|Characteristics
operator|.
name|UNORDERED
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|toImmutableSortedMap ( Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction)
specifier|static
parameter_list|<
name|T
parameter_list|,
name|K
parameter_list|,
name|V
parameter_list|>
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableSortedMap
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|K
argument_list|>
name|keyFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
parameter_list|,
name|BinaryOperator
argument_list|<
name|V
argument_list|>
name|mergeFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|keyFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|valueFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|mergeFunction
argument_list|)
expr_stmt|;
return|return
name|Collectors
operator|.
name|collectingAndThen
argument_list|(
name|Collectors
operator|.
name|toMap
argument_list|(
name|keyFunction
argument_list|,
name|valueFunction
argument_list|,
name|mergeFunction
argument_list|,
parameter_list|()
lambda|->
operator|new
name|TreeMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|comparator
argument_list|)
argument_list|)
argument_list|,
name|ImmutableSortedMap
operator|::
name|copyOfSorted
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|toImmutableBiMap ( Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction)
specifier|static
parameter_list|<
name|T
parameter_list|,
name|K
parameter_list|,
name|V
parameter_list|>
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableBiMap
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|K
argument_list|>
name|keyFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|keyFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|valueFunction
argument_list|)
expr_stmt|;
return|return
name|Collector
operator|.
name|of
argument_list|(
name|ImmutableBiMap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|::
operator|new
argument_list|,
parameter_list|(
name|builder
parameter_list|,
name|input
parameter_list|)
lambda|->
name|builder
operator|.
name|put
argument_list|(
name|keyFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|,
name|valueFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|)
argument_list|,
name|ImmutableBiMap
operator|.
name|Builder
operator|::
name|combine
argument_list|,
name|ImmutableBiMap
operator|.
name|Builder
operator|::
name|build
argument_list|,
operator|new
name|Collector
operator|.
name|Characteristics
index|[
literal|0
index|]
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|GwtIncompatible
specifier|static
parameter_list|<
name|T
parameter_list|,
name|K
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|K
argument_list|>
parameter_list|,
name|V
parameter_list|>
DECL|method|toImmutableRangeMap ( Function<? super T, Range<K>> keyFunction, Function<? super T, ? extends V> valueFunction)
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableRangeMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableRangeMap
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|Range
argument_list|<
name|K
argument_list|>
argument_list|>
name|keyFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|keyFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|valueFunction
argument_list|)
expr_stmt|;
return|return
name|Collector
operator|.
name|of
argument_list|(
name|ImmutableRangeMap
operator|::
operator|<
name|K
argument_list|,
name|V
operator|>
name|builder
argument_list|,
parameter_list|(
name|builder
parameter_list|,
name|input
parameter_list|)
lambda|->
name|builder
operator|.
name|put
argument_list|(
name|keyFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|,
name|valueFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|)
argument_list|,
name|ImmutableRangeMap
operator|.
name|Builder
operator|::
name|combine
argument_list|,
name|ImmutableRangeMap
operator|.
name|Builder
operator|::
name|build
argument_list|)
return|;
block|}
end_function

begin_comment
comment|// Multimaps
end_comment

begin_function
DECL|method|toImmutableListMultimap ( Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction)
specifier|static
parameter_list|<
name|T
parameter_list|,
name|K
parameter_list|,
name|V
parameter_list|>
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableListMultimap
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|K
argument_list|>
name|keyFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|keyFunction
argument_list|,
literal|"keyFunction"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|valueFunction
argument_list|,
literal|"valueFunction"
argument_list|)
expr_stmt|;
return|return
name|Collector
operator|.
name|of
argument_list|(
name|ImmutableListMultimap
operator|::
operator|<
name|K
argument_list|,
name|V
operator|>
name|builder
argument_list|,
parameter_list|(
name|builder
parameter_list|,
name|t
parameter_list|)
lambda|->
name|builder
operator|.
name|put
argument_list|(
name|keyFunction
operator|.
name|apply
argument_list|(
name|t
argument_list|)
argument_list|,
name|valueFunction
operator|.
name|apply
argument_list|(
name|t
argument_list|)
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|Builder
operator|::
name|combine
argument_list|,
name|ImmutableListMultimap
operator|.
name|Builder
operator|::
name|build
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|flatteningToImmutableListMultimap ( Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valuesFunction)
specifier|static
parameter_list|<
name|T
parameter_list|,
name|K
parameter_list|,
name|V
parameter_list|>
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|flatteningToImmutableListMultimap
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|K
argument_list|>
name|keyFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|Stream
argument_list|<
name|?
extends|extends
name|V
argument_list|>
argument_list|>
name|valuesFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|keyFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|valuesFunction
argument_list|)
expr_stmt|;
return|return
name|Collectors
operator|.
name|collectingAndThen
argument_list|(
name|flatteningToMultimap
argument_list|(
name|input
lambda|->
name|checkNotNull
argument_list|(
name|keyFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|)
argument_list|,
name|input
lambda|->
name|valuesFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
operator|.
name|peek
argument_list|(
name|Preconditions
operator|::
name|checkNotNull
argument_list|)
argument_list|,
name|MultimapBuilder
operator|.
name|linkedHashKeys
argument_list|()
operator|.
name|arrayListValues
argument_list|()
operator|::
operator|<
name|K
argument_list|,
name|V
operator|>
name|build
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|::
name|copyOf
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|toImmutableSetMultimap ( Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction)
specifier|static
parameter_list|<
name|T
parameter_list|,
name|K
parameter_list|,
name|V
parameter_list|>
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableSetMultimap
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|K
argument_list|>
name|keyFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|keyFunction
argument_list|,
literal|"keyFunction"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|valueFunction
argument_list|,
literal|"valueFunction"
argument_list|)
expr_stmt|;
return|return
name|Collector
operator|.
name|of
argument_list|(
name|ImmutableSetMultimap
operator|::
operator|<
name|K
argument_list|,
name|V
operator|>
name|builder
argument_list|,
parameter_list|(
name|builder
parameter_list|,
name|t
parameter_list|)
lambda|->
name|builder
operator|.
name|put
argument_list|(
name|keyFunction
operator|.
name|apply
argument_list|(
name|t
argument_list|)
argument_list|,
name|valueFunction
operator|.
name|apply
argument_list|(
name|t
argument_list|)
argument_list|)
argument_list|,
name|ImmutableSetMultimap
operator|.
name|Builder
operator|::
name|combine
argument_list|,
name|ImmutableSetMultimap
operator|.
name|Builder
operator|::
name|build
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|flatteningToImmutableSetMultimap ( Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valuesFunction)
specifier|static
parameter_list|<
name|T
parameter_list|,
name|K
parameter_list|,
name|V
parameter_list|>
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|flatteningToImmutableSetMultimap
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|K
argument_list|>
name|keyFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|Stream
argument_list|<
name|?
extends|extends
name|V
argument_list|>
argument_list|>
name|valuesFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|keyFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|valuesFunction
argument_list|)
expr_stmt|;
return|return
name|Collectors
operator|.
name|collectingAndThen
argument_list|(
name|flatteningToMultimap
argument_list|(
name|input
lambda|->
name|checkNotNull
argument_list|(
name|keyFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|)
argument_list|,
name|input
lambda|->
name|valuesFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
operator|.
name|peek
argument_list|(
name|Preconditions
operator|::
name|checkNotNull
argument_list|)
argument_list|,
name|MultimapBuilder
operator|.
name|linkedHashKeys
argument_list|()
operator|.
name|linkedHashSetValues
argument_list|()
operator|::
operator|<
name|K
argument_list|,
name|V
operator|>
name|build
argument_list|)
argument_list|,
name|ImmutableSetMultimap
operator|::
name|copyOf
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|toMultimap ( Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, Supplier<M> multimapSupplier)
specifier|static
parameter_list|<
name|T
parameter_list|,
name|K
parameter_list|,
name|V
parameter_list|,
name|M
extends|extends
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
parameter_list|>
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|M
argument_list|>
name|toMultimap
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|K
argument_list|>
name|keyFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
parameter_list|,
name|Supplier
argument_list|<
name|M
argument_list|>
name|multimapSupplier
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|keyFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|valueFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|multimapSupplier
argument_list|)
expr_stmt|;
return|return
name|Collector
operator|.
name|of
argument_list|(
name|multimapSupplier
argument_list|,
parameter_list|(
name|multimap
parameter_list|,
name|input
parameter_list|)
lambda|->
name|multimap
operator|.
name|put
argument_list|(
name|keyFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|,
name|valueFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
argument_list|)
argument_list|,
parameter_list|(
name|multimap1
parameter_list|,
name|multimap2
parameter_list|)
lambda|->
block|{
name|multimap1
operator|.
name|putAll
argument_list|(
name|multimap2
argument_list|)
argument_list|;           return
name|multimap1
argument_list|;
block|}
end_function

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_function
unit|}    @
name|Beta
DECL|method|flatteningToMultimap ( Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valueFunction, Supplier<M> multimapSupplier)
specifier|static
argument_list|<
name|T
argument_list|,
name|K
argument_list|,
name|V
argument_list|,
name|M
extends|extends
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|M
argument_list|>
name|flatteningToMultimap
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|K
argument_list|>
name|keyFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|Stream
argument_list|<
name|?
extends|extends
name|V
argument_list|>
argument_list|>
name|valueFunction
parameter_list|,
name|Supplier
argument_list|<
name|M
argument_list|>
name|multimapSupplier
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|keyFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|valueFunction
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|multimapSupplier
argument_list|)
expr_stmt|;
return|return
name|Collector
operator|.
name|of
argument_list|(
name|multimapSupplier
argument_list|,
parameter_list|(
name|multimap
parameter_list|,
name|input
parameter_list|)
lambda|->
block|{
name|K
name|key
init|=
name|keyFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|valuesForKey
init|=
name|multimap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|valueFunction
operator|.
name|apply
argument_list|(
name|input
argument_list|)
operator|.
name|forEachOrdered
argument_list|(
name|valuesForKey
operator|::
name|add
argument_list|)
expr_stmt|;
block|}
argument_list|,
parameter_list|(
name|multimap1
parameter_list|,
name|multimap2
parameter_list|)
lambda|->
block|{
name|multimap1
operator|.
name|putAll
argument_list|(
name|multimap2
argument_list|)
expr_stmt|;
return|return
name|multimap1
return|;
block|}
argument_list|)
return|;
block|}
end_function

unit|}
end_unit

