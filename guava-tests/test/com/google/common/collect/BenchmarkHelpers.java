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
name|Set
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
comment|/**  * Helper classes for various benchmarks.  *  * @author Christopher Swenson  */
end_comment

begin_class
DECL|class|BenchmarkHelpers
specifier|final
class|class
name|BenchmarkHelpers
block|{
comment|/**    * So far, this is the best way to test various implementations of {@link Set} subclasses.    */
DECL|enum|SetImpl
specifier|public
enum|enum
name|SetImpl
block|{
DECL|enumConstant|Hash
name|Hash
block|{
annotation|@
name|Override
argument_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
argument_list|>
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
DECL|enumConstant|LinkedHash
name|LinkedHash
block|{
annotation|@
name|Override
argument_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
argument_list|>
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
DECL|enumConstant|Tree
name|Tree
block|{
annotation|@
name|Override
argument_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
argument_list|>
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
DECL|enumConstant|Unmodifiable
name|Unmodifiable
block|{
annotation|@
name|Override
argument_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
argument_list|>
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
DECL|enumConstant|Synchronized
name|Synchronized
block|{
annotation|@
name|Override
argument_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
argument_list|>
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
DECL|enumConstant|Immutable
name|Immutable
block|{
annotation|@
name|Override
argument_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
argument_list|>
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
DECL|enumConstant|ImmutableSorted
name|ImmutableSorted
block|{
annotation|@
name|Override
argument_list|<
name|E
extends|extends
name|Comparable
argument_list|<
name|E
argument_list|>
argument_list|>
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
block|,     ;
DECL|method|create (Collection<E> contents)
specifier|abstract
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
function_decl|;
block|}
DECL|enum|ListMultimapImpl
specifier|public
enum|enum
name|ListMultimapImpl
block|{
DECL|enumConstant|ArrayList
name|ArrayList
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
DECL|enumConstant|LinkedList
name|LinkedList
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
DECL|enumConstant|ImmutableList
name|ImmutableList
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
DECL|enum|SetMultimapImpl
specifier|public
enum|enum
name|SetMultimapImpl
block|{
DECL|enumConstant|Hash
name|Hash
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
DECL|enumConstant|LinkedHash
name|LinkedHash
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
DECL|enumConstant|Tree
name|Tree
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
DECL|enumConstant|ImmutableSet
name|ImmutableSet
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
DECL|enum|Value
specifier|public
enum|enum
name|Value
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;   }
block|}
end_class

end_unit

