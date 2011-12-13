begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.cache
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
package|;
end_package

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
name|Function
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
name|Objects
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
name|Optional
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
operator|.
name|LocalCache
operator|.
name|Strength
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
name|Iterables
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
name|Sets
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
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Helper class for creating {@link CacheBuilder} instances with all combinations of several sets of  * parameters.  *  * @author mike nonemacher  */
end_comment

begin_class
DECL|class|CacheBuilderFactory
class|class
name|CacheBuilderFactory
block|{
comment|// Default values contain only 'null', which means don't call the CacheBuilder method (just give
comment|// the CacheBuilder default).
DECL|field|concurrencyLevels
specifier|private
name|Set
argument_list|<
name|Integer
argument_list|>
name|concurrencyLevels
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
operator|(
name|Integer
operator|)
literal|null
argument_list|)
decl_stmt|;
DECL|field|initialCapacities
specifier|private
name|Set
argument_list|<
name|Integer
argument_list|>
name|initialCapacities
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
operator|(
name|Integer
operator|)
literal|null
argument_list|)
decl_stmt|;
DECL|field|maximumSizes
specifier|private
name|Set
argument_list|<
name|Integer
argument_list|>
name|maximumSizes
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
operator|(
name|Integer
operator|)
literal|null
argument_list|)
decl_stmt|;
DECL|field|expireAfterWrites
specifier|private
name|Set
argument_list|<
name|DurationSpec
argument_list|>
name|expireAfterWrites
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
operator|(
name|DurationSpec
operator|)
literal|null
argument_list|)
decl_stmt|;
DECL|field|expireAfterAccesses
specifier|private
name|Set
argument_list|<
name|DurationSpec
argument_list|>
name|expireAfterAccesses
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
operator|(
name|DurationSpec
operator|)
literal|null
argument_list|)
decl_stmt|;
DECL|field|refreshes
specifier|private
name|Set
argument_list|<
name|DurationSpec
argument_list|>
name|refreshes
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
operator|(
name|DurationSpec
operator|)
literal|null
argument_list|)
decl_stmt|;
DECL|field|keyStrengths
specifier|private
name|Set
argument_list|<
name|Strength
argument_list|>
name|keyStrengths
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
operator|(
name|Strength
operator|)
literal|null
argument_list|)
decl_stmt|;
DECL|field|valueStrengths
specifier|private
name|Set
argument_list|<
name|Strength
argument_list|>
name|valueStrengths
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
operator|(
name|Strength
operator|)
literal|null
argument_list|)
decl_stmt|;
DECL|method|withConcurrencyLevels (Set<Integer> concurrencyLevels)
name|CacheBuilderFactory
name|withConcurrencyLevels
parameter_list|(
name|Set
argument_list|<
name|Integer
argument_list|>
name|concurrencyLevels
parameter_list|)
block|{
name|this
operator|.
name|concurrencyLevels
operator|=
name|Sets
operator|.
name|newLinkedHashSet
argument_list|(
name|concurrencyLevels
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withInitialCapacities (Set<Integer> initialCapacities)
name|CacheBuilderFactory
name|withInitialCapacities
parameter_list|(
name|Set
argument_list|<
name|Integer
argument_list|>
name|initialCapacities
parameter_list|)
block|{
name|this
operator|.
name|initialCapacities
operator|=
name|Sets
operator|.
name|newLinkedHashSet
argument_list|(
name|initialCapacities
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withMaximumSizes (Set<Integer> maximumSizes)
name|CacheBuilderFactory
name|withMaximumSizes
parameter_list|(
name|Set
argument_list|<
name|Integer
argument_list|>
name|maximumSizes
parameter_list|)
block|{
name|this
operator|.
name|maximumSizes
operator|=
name|Sets
operator|.
name|newLinkedHashSet
argument_list|(
name|maximumSizes
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withExpireAfterWrites (Set<DurationSpec> durations)
name|CacheBuilderFactory
name|withExpireAfterWrites
parameter_list|(
name|Set
argument_list|<
name|DurationSpec
argument_list|>
name|durations
parameter_list|)
block|{
name|this
operator|.
name|expireAfterWrites
operator|=
name|Sets
operator|.
name|newLinkedHashSet
argument_list|(
name|durations
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withExpireAfterAccesses (Set<DurationSpec> durations)
name|CacheBuilderFactory
name|withExpireAfterAccesses
parameter_list|(
name|Set
argument_list|<
name|DurationSpec
argument_list|>
name|durations
parameter_list|)
block|{
name|this
operator|.
name|expireAfterAccesses
operator|=
name|Sets
operator|.
name|newLinkedHashSet
argument_list|(
name|durations
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withRefreshes (Set<DurationSpec> durations)
name|CacheBuilderFactory
name|withRefreshes
parameter_list|(
name|Set
argument_list|<
name|DurationSpec
argument_list|>
name|durations
parameter_list|)
block|{
name|this
operator|.
name|refreshes
operator|=
name|Sets
operator|.
name|newLinkedHashSet
argument_list|(
name|durations
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withKeyStrengths (Set<Strength> keyStrengths)
name|CacheBuilderFactory
name|withKeyStrengths
parameter_list|(
name|Set
argument_list|<
name|Strength
argument_list|>
name|keyStrengths
parameter_list|)
block|{
name|this
operator|.
name|keyStrengths
operator|=
name|Sets
operator|.
name|newLinkedHashSet
argument_list|(
name|keyStrengths
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkArgument
argument_list|(
operator|!
name|this
operator|.
name|keyStrengths
operator|.
name|contains
argument_list|(
name|Strength
operator|.
name|SOFT
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withValueStrengths (Set<Strength> valueStrengths)
name|CacheBuilderFactory
name|withValueStrengths
parameter_list|(
name|Set
argument_list|<
name|Strength
argument_list|>
name|valueStrengths
parameter_list|)
block|{
name|this
operator|.
name|valueStrengths
operator|=
name|Sets
operator|.
name|newLinkedHashSet
argument_list|(
name|valueStrengths
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|buildAllPermutations ()
name|Iterable
argument_list|<
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|buildAllPermutations
parameter_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Iterable
argument_list|<
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|combinations
init|=
name|buildCartesianProduct
argument_list|(
name|concurrencyLevels
argument_list|,
name|initialCapacities
argument_list|,
name|maximumSizes
argument_list|,
name|expireAfterWrites
argument_list|,
name|expireAfterAccesses
argument_list|,
name|refreshes
argument_list|,
name|keyStrengths
argument_list|,
name|valueStrengths
argument_list|)
decl_stmt|;
return|return
name|Iterables
operator|.
name|transform
argument_list|(
name|combinations
argument_list|,
operator|new
name|Function
argument_list|<
name|List
argument_list|<
name|Object
argument_list|>
argument_list|,
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|apply
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|combination
parameter_list|)
block|{
return|return
name|createCacheBuilder
argument_list|(
operator|(
name|Integer
operator|)
name|combination
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
operator|(
name|Integer
operator|)
name|combination
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
operator|(
name|Integer
operator|)
name|combination
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|,
operator|(
name|DurationSpec
operator|)
name|combination
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|,
operator|(
name|DurationSpec
operator|)
name|combination
operator|.
name|get
argument_list|(
literal|4
argument_list|)
argument_list|,
operator|(
name|DurationSpec
operator|)
name|combination
operator|.
name|get
argument_list|(
literal|5
argument_list|)
argument_list|,
operator|(
name|Strength
operator|)
name|combination
operator|.
name|get
argument_list|(
literal|6
argument_list|)
argument_list|,
operator|(
name|Strength
operator|)
name|combination
operator|.
name|get
argument_list|(
literal|7
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|field|NULLABLE_TO_OPTIONAL
specifier|private
specifier|static
specifier|final
name|Function
argument_list|<
name|Object
argument_list|,
name|Optional
argument_list|<
name|?
argument_list|>
argument_list|>
name|NULLABLE_TO_OPTIONAL
init|=
operator|new
name|Function
argument_list|<
name|Object
argument_list|,
name|Optional
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Optional
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|fromNullable
argument_list|(
name|obj
argument_list|)
return|;
block|}
block|}
decl_stmt|;
DECL|field|OPTIONAL_TO_NULLABLE
specifier|private
specifier|static
specifier|final
name|Function
argument_list|<
name|Optional
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|OPTIONAL_TO_NULLABLE
init|=
operator|new
name|Function
argument_list|<
name|Optional
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|apply
parameter_list|(
name|Optional
argument_list|<
name|?
argument_list|>
name|optional
parameter_list|)
block|{
return|return
name|optional
operator|.
name|orNull
argument_list|()
return|;
block|}
block|}
empty_stmt|;
comment|/**    * Sets.cartesianProduct doesn't allow sets that contain null, but we want null to mean    * "don't call the associated CacheBuilder method" - that is, get the default CacheBuilder    * behavior. This method wraps the elements in the input sets (which may contain null) as    * Optionals, calls Sets.cartesianProduct with those, then transforms the result to unwrap    * the Optionals.     */
DECL|method|buildCartesianProduct (Set<?>.... sets)
specifier|private
name|Iterable
argument_list|<
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|buildCartesianProduct
parameter_list|(
name|Set
argument_list|<
name|?
argument_list|>
modifier|...
name|sets
parameter_list|)
block|{
name|List
argument_list|<
name|Set
argument_list|<
name|Optional
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|optionalSets
init|=
name|Lists
operator|.
name|newArrayListWithExpectedSize
argument_list|(
name|sets
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|Set
argument_list|<
name|?
argument_list|>
name|set
range|:
name|sets
control|)
block|{
name|Set
argument_list|<
name|Optional
argument_list|<
name|?
argument_list|>
argument_list|>
name|optionalSet
init|=
name|Sets
operator|.
name|newLinkedHashSet
argument_list|(
name|Iterables
operator|.
name|transform
argument_list|(
name|set
argument_list|,
name|NULLABLE_TO_OPTIONAL
argument_list|)
argument_list|)
decl_stmt|;
name|optionalSets
operator|.
name|add
argument_list|(
name|optionalSet
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|List
argument_list|<
name|Optional
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|cartesianProduct
init|=
name|Sets
operator|.
name|cartesianProduct
argument_list|(
name|optionalSets
argument_list|)
decl_stmt|;
return|return
name|Iterables
operator|.
name|transform
argument_list|(
name|cartesianProduct
argument_list|,
operator|new
name|Function
argument_list|<
name|List
argument_list|<
name|Optional
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|apply
parameter_list|(
name|List
argument_list|<
name|Optional
argument_list|<
name|?
argument_list|>
argument_list|>
name|objs
parameter_list|)
block|{
return|return
name|Lists
operator|.
name|transform
argument_list|(
name|objs
argument_list|,
name|OPTIONAL_TO_NULLABLE
argument_list|)
return|;
block|}
block|}
block|)
function|;
block|}
end_class

begin_function
DECL|method|createCacheBuilder ( Integer concurrencyLevel, Integer initialCapacity, Integer maximumSize, DurationSpec expireAfterWrite, DurationSpec expireAfterAccess, DurationSpec refresh, Strength keyStrength, Strength valueStrength)
specifier|private
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|createCacheBuilder
parameter_list|(
name|Integer
name|concurrencyLevel
parameter_list|,
name|Integer
name|initialCapacity
parameter_list|,
name|Integer
name|maximumSize
parameter_list|,
name|DurationSpec
name|expireAfterWrite
parameter_list|,
name|DurationSpec
name|expireAfterAccess
parameter_list|,
name|DurationSpec
name|refresh
parameter_list|,
name|Strength
name|keyStrength
parameter_list|,
name|Strength
name|valueStrength
parameter_list|)
block|{
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|concurrencyLevel
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|concurrencyLevel
argument_list|(
name|concurrencyLevel
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|initialCapacity
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|initialCapacity
argument_list|(
name|initialCapacity
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|maximumSize
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|maximumSize
argument_list|(
name|maximumSize
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|expireAfterWrite
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|expireAfterWrite
argument_list|(
name|expireAfterWrite
operator|.
name|duration
argument_list|,
name|expireAfterWrite
operator|.
name|unit
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|expireAfterAccess
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|expireAfterAccess
argument_list|(
name|expireAfterAccess
operator|.
name|duration
argument_list|,
name|expireAfterAccess
operator|.
name|unit
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|refresh
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|refreshAfterWrite
argument_list|(
name|refresh
operator|.
name|duration
argument_list|,
name|refresh
operator|.
name|unit
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|keyStrength
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setKeyStrength
argument_list|(
name|keyStrength
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|valueStrength
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setValueStrength
argument_list|(
name|valueStrength
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
return|;
block|}
end_function

begin_class
DECL|class|DurationSpec
specifier|static
class|class
name|DurationSpec
block|{
DECL|field|duration
specifier|private
specifier|final
name|long
name|duration
decl_stmt|;
DECL|field|unit
specifier|private
specifier|final
name|TimeUnit
name|unit
decl_stmt|;
DECL|method|DurationSpec (long duration, TimeUnit unit)
specifier|private
name|DurationSpec
parameter_list|(
name|long
name|duration
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|this
operator|.
name|duration
operator|=
name|duration
expr_stmt|;
name|this
operator|.
name|unit
operator|=
name|unit
expr_stmt|;
block|}
DECL|method|of (long duration, TimeUnit unit)
specifier|public
specifier|static
name|DurationSpec
name|of
parameter_list|(
name|long
name|duration
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
operator|new
name|DurationSpec
argument_list|(
name|duration
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hashCode
argument_list|(
name|duration
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|DurationSpec
condition|)
block|{
name|DurationSpec
name|that
init|=
operator|(
name|DurationSpec
operator|)
name|o
decl_stmt|;
return|return
name|unit
operator|.
name|toNanos
argument_list|(
name|duration
argument_list|)
operator|==
name|that
operator|.
name|unit
operator|.
name|toNanos
argument_list|(
name|that
operator|.
name|duration
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|toStringHelper
argument_list|(
name|this
argument_list|)
operator|.
name|add
argument_list|(
literal|"duration"
argument_list|,
name|duration
argument_list|)
operator|.
name|add
argument_list|(
literal|"unit"
argument_list|,
name|unit
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

unit|}
end_unit

