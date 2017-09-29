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
name|CollectPreconditions
operator|.
name|checkNonnegative
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
name|annotations
operator|.
name|VisibleForTesting
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
name|ArrayList
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
name|List
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

begin_comment
comment|/**  * Implementation of {@code Multimap} that uses an {@code ArrayList} to store  * the values for a given key. A {@link HashMap} associates each key with an  * {@link ArrayList} of values.  *  *<p>When iterating through the collections supplied by this class, the  * ordering of values for a given key agrees with the order in which the values  * were added.  *  *<p>This multimap allows duplicate key-value pairs. After adding a new  * key-value pair equal to an existing key-value pair, the {@code  * ArrayListMultimap} will contain entries for both the new value and the old  * value.  *  *<p>Keys and values may be null. All optional multimap methods are supported,  * and all returned views are modifiable.  *  *<p>The lists returned by {@link #get}, {@link #removeAll}, and {@link  * #replaceValues} all implement {@link java.util.RandomAccess}.  *  *<p>This class is not threadsafe when any concurrent operations update the  * multimap. Concurrent read operations will work correctly. To allow concurrent  * update operations, wrap your multimap with a call to {@link  * Multimaps#synchronizedListMultimap}.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#multimap">  * {@code Multimap}</a>.  *  * @author Jared Levy  * @since 2.0  */
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
DECL|class|ArrayListMultimap
specifier|public
specifier|final
class|class
name|ArrayListMultimap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ArrayListMultimapGwtSerializationDependencies
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|// Default from ArrayList
DECL|field|DEFAULT_VALUES_PER_KEY
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_VALUES_PER_KEY
init|=
literal|3
decl_stmt|;
DECL|field|expectedValuesPerKey
annotation|@
name|VisibleForTesting
specifier|transient
name|int
name|expectedValuesPerKey
decl_stmt|;
comment|/**    * Creates a new, empty {@code ArrayListMultimap} with the default initial capacities.    *    *<p>This method will soon be deprecated in favor of {@code    * MultimapBuilder.hashKeys().arrayListValues().build()}.    */
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ArrayListMultimap
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
name|ArrayListMultimap
argument_list|<>
argument_list|()
return|;
block|}
comment|/**    * Constructs an empty {@code ArrayListMultimap} with enough capacity to hold the specified    * numbers of keys and values without resizing.    *    *<p>This method will soon be deprecated in favor of {@code    * MultimapBuilder.hashKeys(expectedKeys).arrayListValues(expectedValuesPerKey).build()}.    *    * @param expectedKeys the expected number of distinct keys    * @param expectedValuesPerKey the expected average number of values per key    * @throws IllegalArgumentException if {@code expectedKeys} or {@code expectedValuesPerKey} is    *     negative    */
DECL|method|create (int expectedKeys, int expectedValuesPerKey)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ArrayListMultimap
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
name|ArrayListMultimap
argument_list|<>
argument_list|(
name|expectedKeys
argument_list|,
name|expectedValuesPerKey
argument_list|)
return|;
block|}
comment|/**    * Constructs an {@code ArrayListMultimap} with the same mappings as the specified multimap.    *    *<p>This method will soon be deprecated in favor of {@code    * MultimapBuilder.hashKeys().arrayListValues().build(multimap)}.    *    * @param multimap the multimap whose contents are copied to this multimap    */
DECL|method|create (Multimap<? extends K, ? extends V> multimap)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ArrayListMultimap
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
name|ArrayListMultimap
argument_list|<>
argument_list|(
name|multimap
argument_list|)
return|;
block|}
DECL|method|ArrayListMultimap ()
specifier|private
name|ArrayListMultimap
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
name|expectedValuesPerKey
operator|=
name|DEFAULT_VALUES_PER_KEY
expr_stmt|;
block|}
DECL|method|ArrayListMultimap (int expectedKeys, int expectedValuesPerKey)
specifier|private
name|ArrayListMultimap
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
name|checkNonnegative
argument_list|(
name|expectedValuesPerKey
argument_list|,
literal|"expectedValuesPerKey"
argument_list|)
expr_stmt|;
name|this
operator|.
name|expectedValuesPerKey
operator|=
name|expectedValuesPerKey
expr_stmt|;
block|}
DECL|method|ArrayListMultimap (Multimap<? extends K, ? extends V> multimap)
specifier|private
name|ArrayListMultimap
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
name|this
argument_list|(
name|multimap
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
operator|(
name|multimap
operator|instanceof
name|ArrayListMultimap
operator|)
condition|?
operator|(
operator|(
name|ArrayListMultimap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|multimap
operator|)
operator|.
name|expectedValuesPerKey
else|:
name|DEFAULT_VALUES_PER_KEY
argument_list|)
expr_stmt|;
name|putAll
argument_list|(
name|multimap
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates a new, empty {@code ArrayList} to hold the collection of values for    * an arbitrary key.    */
annotation|@
name|Override
DECL|method|createCollection ()
name|List
argument_list|<
name|V
argument_list|>
name|createCollection
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|V
argument_list|>
argument_list|(
name|expectedValuesPerKey
argument_list|)
return|;
block|}
comment|/**    * Reduces the memory used by this {@code ArrayListMultimap}, if feasible.    *    * @deprecated For a {@link ListMultimap} that automatically trims to size, use {@link    *     ImmutableListMultimap}. If you need a mutable collection, remove the {@code trimToSize}    *     call, or switch to a {@code HashMap<K, ArrayList<V>>}.    */
annotation|@
name|Deprecated
DECL|method|trimToSize ()
specifier|public
name|void
name|trimToSize
parameter_list|()
block|{
for|for
control|(
name|Collection
argument_list|<
name|V
argument_list|>
name|collection
range|:
name|backingMap
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
name|ArrayList
argument_list|<
name|V
argument_list|>
name|arrayList
init|=
operator|(
name|ArrayList
argument_list|<
name|V
argument_list|>
operator|)
name|collection
decl_stmt|;
name|arrayList
operator|.
name|trimToSize
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * @serialData expectedValuesPerKey, number of distinct keys, and then for    *     each distinct key: the key, number of values for that key, and the    *     key's values    */
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
annotation|@
name|GwtIncompatible
comment|// java.io.ObjectOutputStream
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
name|Maps
operator|.
name|newHashMap
argument_list|()
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
annotation|@
name|GwtIncompatible
comment|// Not needed in emulated source.
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
end_class

end_unit

