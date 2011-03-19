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
name|VisibleForTesting
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
name|HashMap
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
comment|/**  * Implementation of {@link Multimap} using hash tables.  *  *<p>The multimap does not store duplicate key-value pairs. Adding a new  * key-value pair equal to an existing key-value pair has no effect.  *  *<p>Keys and values may be null. All optional multimap methods are supported,  * and all returned views are modifiable.  *  *<p>This class is not threadsafe when any concurrent operations update the  * multimap. Concurrent read operations will work correctly. To allow concurrent  * update operations, wrap your multimap with a call to {@link  * Multimaps#synchronizedSetMultimap}.  *  * @author Jared Levy  * @since 2 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|,
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|HashMultimap
specifier|public
specifier|final
class|class
name|HashMultimap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|DEFAULT_VALUES_PER_KEY
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_VALUES_PER_KEY
init|=
literal|8
decl_stmt|;
annotation|@
name|VisibleForTesting
DECL|field|expectedValuesPerKey
specifier|transient
name|int
name|expectedValuesPerKey
init|=
name|DEFAULT_VALUES_PER_KEY
decl_stmt|;
comment|/**    * Creates a new, empty {@code HashMultimap} with the default initial    * capacities.    */
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|HashMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|HashMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Constructs an empty {@code HashMultimap} with enough capacity to hold the    * specified numbers of keys and values without rehashing.    *    * @param expectedKeys the expected number of distinct keys    * @param expectedValuesPerKey the expected average number of values per key    * @throws IllegalArgumentException if {@code expectedKeys} or {@code    *      expectedValuesPerKey} is negative    */
DECL|method|create ( int expectedKeys, int expectedValuesPerKey)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|HashMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|int
name|expectedKeys
parameter_list|,
name|int
name|expectedValuesPerKey
parameter_list|)
block|{
return|return
operator|new
name|HashMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|expectedKeys
argument_list|,
name|expectedValuesPerKey
argument_list|)
return|;
block|}
comment|/**    * Constructs a {@code HashMultimap} with the same mappings as the specified    * multimap. If a key-value mapping appears multiple times in the input    * multimap, it only appears once in the constructed multimap.    *    * @param multimap the multimap whose contents are copied to this multimap    */
DECL|method|create ( Multimap<? extends K, ? extends V> multimap)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|HashMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Multimap
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|multimap
parameter_list|)
block|{
return|return
operator|new
name|HashMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|multimap
argument_list|)
return|;
block|}
DECL|method|HashMultimap ()
specifier|private
name|HashMultimap
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|HashMap
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|HashMultimap (int expectedKeys, int expectedValuesPerKey)
specifier|private
name|HashMultimap
parameter_list|(
name|int
name|expectedKeys
parameter_list|,
name|int
name|expectedValuesPerKey
parameter_list|)
block|{
name|super
argument_list|(
name|Maps
operator|.
expr|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
operator|>
name|newHashMapWithExpectedSize
argument_list|(
name|expectedKeys
argument_list|)
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkArgument
argument_list|(
name|expectedValuesPerKey
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|this
operator|.
name|expectedValuesPerKey
operator|=
name|expectedValuesPerKey
expr_stmt|;
block|}
DECL|method|HashMultimap (Multimap<? extends K, ? extends V> multimap)
specifier|private
name|HashMultimap
parameter_list|(
name|Multimap
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|multimap
parameter_list|)
block|{
name|super
argument_list|(
name|Maps
operator|.
expr|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
operator|>
name|newHashMapWithExpectedSize
argument_list|(
name|multimap
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|putAll
argument_list|(
name|multimap
argument_list|)
expr_stmt|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>Creates an empty {@code HashSet} for a collection of values for one key.    *    * @return a new {@code HashSet} containing a collection of values for one key    */
DECL|method|createCollection ()
annotation|@
name|Override
name|Set
argument_list|<
name|V
argument_list|>
name|createCollection
parameter_list|()
block|{
return|return
name|Sets
operator|.
expr|<
name|V
operator|>
name|newHashSetWithExpectedSize
argument_list|(
name|expectedValuesPerKey
argument_list|)
return|;
block|}
block|}
end_class

end_unit

