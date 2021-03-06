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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
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
name|Map
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
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Implementation of {@link Multimap} using hash tables.  *  *<p>The multimap does not store duplicate key-value pairs. Adding a new key-value pair equal to an  * existing key-value pair has no effect.  *  *<p>Keys and values may be null. All optional multimap methods are supported, and all returned  * views are modifiable.  *  *<p>This class is not threadsafe when any concurrent operations update the multimap. Concurrent  * read operations will work correctly if the last write<i>happens-before</i> any reads. To allow  * concurrent update operations, wrap your multimap with a call to {@link  * Multimaps#synchronizedSetMultimap}.  *  *<p><b>Warning:</b> Do not modify either a key<i>or a value</i> of a {@code HashMultimap} in a  * way that affects its {@link Object#equals} behavior. Undefined behavior and bugs will result.  *  * @author Jared Levy  * @since 2.0  */
end_comment

begin_annotation
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
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|class|HashMultimap
specifier|public
name|final
name|class
name|HashMultimap
operator|<
name|K
expr|extends @
name|Nullable
name|Object
operator|,
name|V
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|HashMultimapGwtSerializationDependencies
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|DEFAULT_VALUES_PER_KEY
specifier|private
specifier|static
name|final
name|int
name|DEFAULT_VALUES_PER_KEY
operator|=
literal|2
block|;    @
DECL|field|expectedValuesPerKey
name|VisibleForTesting
specifier|transient
name|int
name|expectedValuesPerKey
operator|=
name|DEFAULT_VALUES_PER_KEY
block|;
comment|/**    * Creates a new, empty {@code HashMultimap} with the default initial capacities.    *    *<p>This method will soon be deprecated in favor of {@code    * MultimapBuilder.hashKeys().hashSetValues().build()}.    */
specifier|public
specifier|static
operator|<
name|K
expr|extends @
name|Nullable
name|Object
block|,
name|V
expr|extends @
name|Nullable
name|Object
operator|>
DECL|method|create ()
name|HashMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
argument_list|()
block|{
return|return
operator|new
name|HashMultimap
argument_list|<>
argument_list|()
return|;
block|}
comment|/**    * Constructs an empty {@code HashMultimap} with enough capacity to hold the specified numbers of    * keys and values without rehashing.    *    *<p>This method will soon be deprecated in favor of {@code    * MultimapBuilder.hashKeys(expectedKeys).hashSetValues(expectedValuesPerKey).build()}.    *    * @param expectedKeys the expected number of distinct keys    * @param expectedValuesPerKey the expected average number of values per key    * @throws IllegalArgumentException if {@code expectedKeys} or {@code expectedValuesPerKey} is    *     negative    */
DECL|method|create ( int expectedKeys, int expectedValuesPerKey)
specifier|public
specifier|static
operator|<
name|K
expr|extends @
name|Nullable
name|Object
operator|,
name|V
expr|extends @
name|Nullable
name|Object
operator|>
name|HashMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
argument_list|(
name|int
name|expectedKeys
argument_list|,
name|int
name|expectedValuesPerKey
argument_list|)
block|{
return|return
operator|new
name|HashMultimap
argument_list|<>
argument_list|(
name|expectedKeys
argument_list|,
name|expectedValuesPerKey
argument_list|)
return|;
block|}
end_expr_stmt

begin_comment
comment|/**    * Constructs a {@code HashMultimap} with the same mappings as the specified multimap. If a    * key-value mapping appears multiple times in the input multimap, it only appears once in the    * constructed multimap.    *    *<p>This method will soon be deprecated in favor of {@code    * MultimapBuilder.hashKeys().hashSetValues().build(multimap)}.    *    * @param multimap the multimap whose contents are copied to this multimap    */
end_comment

begin_expr_stmt
DECL|method|create ( Multimap<? extends K, ? extends V> multimap)
specifier|public
specifier|static
operator|<
name|K
expr|extends @
name|Nullable
name|Object
operator|,
name|V
expr|extends @
name|Nullable
name|Object
operator|>
name|HashMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
argument_list|(
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
argument_list|)
block|{
return|return
operator|new
name|HashMultimap
argument_list|<>
argument_list|(
name|multimap
argument_list|)
return|;
block|}
end_expr_stmt

begin_constructor
DECL|method|HashMultimap ()
specifier|private
name|HashMultimap
parameter_list|()
block|{
name|this
argument_list|(
literal|12
argument_list|,
name|DEFAULT_VALUES_PER_KEY
argument_list|)
expr_stmt|;
block|}
end_constructor

begin_constructor
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
name|Platform
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
end_constructor

begin_constructor
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
name|Platform
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
end_constructor

begin_comment
comment|/**    * {@inheritDoc}    *    *<p>Creates an empty {@code HashSet} for a collection of values for one key.    *    * @return a new {@code HashSet} containing a collection of values for one key    */
end_comment

begin_function
annotation|@
name|Override
DECL|method|createCollection ()
name|Set
argument_list|<
name|V
argument_list|>
name|createCollection
parameter_list|()
block|{
return|return
name|Platform
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
end_function

begin_comment
comment|/**    * @serialData expectedValuesPerKey, number of distinct keys, and then for each distinct key: the    *     key, number of values for that key, and the key's values    */
end_comment

begin_function
annotation|@
name|GwtIncompatible
comment|// java.io.ObjectOutputStream
DECL|method|writeObject (ObjectOutputStream stream)
specifier|private
name|void
name|writeObject
parameter_list|(
name|ObjectOutputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|stream
operator|.
name|defaultWriteObject
argument_list|()
expr_stmt|;
name|Serialization
operator|.
name|writeMultimap
argument_list|(
name|this
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|GwtIncompatible
comment|// java.io.ObjectInputStream
DECL|method|readObject (ObjectInputStream stream)
specifier|private
name|void
name|readObject
parameter_list|(
name|ObjectInputStream
name|stream
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|stream
operator|.
name|defaultReadObject
argument_list|()
expr_stmt|;
name|expectedValuesPerKey
operator|=
name|DEFAULT_VALUES_PER_KEY
expr_stmt|;
name|int
name|distinctKeys
init|=
name|Serialization
operator|.
name|readCount
argument_list|(
name|stream
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|map
init|=
name|Platform
operator|.
name|newHashMapWithExpectedSize
argument_list|(
literal|12
argument_list|)
decl_stmt|;
name|setMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|Serialization
operator|.
name|populateMultimap
argument_list|(
name|this
argument_list|,
name|stream
argument_list|,
name|distinctKeys
argument_list|)
expr_stmt|;
block|}
end_function

begin_decl_stmt
annotation|@
name|GwtIncompatible
comment|// Not needed in emulated source
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
end_decl_stmt

unit|}
end_unit

