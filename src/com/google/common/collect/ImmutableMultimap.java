begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|Iterator
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
name|Map
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
comment|/**  * An immutable {@link Multimap}. Does not permit null keys or values.  *  *<p>Unlike {@link Multimaps#unmodifiableMultimap(Multimap)}, which is  * a<i>view</i> of a separate multimap which can still change, an instance of  * {@code ImmutableMultimap} contains its own data and will<i>never</i>  * change. {@code ImmutableMultimap} is convenient for  * {@code public static final} multimaps ("constant multimaps") and also lets  * you easily make a "defensive copy" of a multimap provided to your class by  * a caller.  *  *<p><b>Note</b>: Although this class is not final, it cannot be subclassed as  * it has no public or protected constructors. Thus, instances of this class  * are guaranteed to be immutable.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ImmutableMultimap
specifier|public
specifier|abstract
class|class
name|ImmutableMultimap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|,
name|Serializable
block|{
comment|/** Returns an empty multimap. */
DECL|method|of ()
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|()
block|{
return|return
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
return|;
block|}
comment|/**    * Returns an immutable multimap containing a single entry.    */
DECL|method|of (K k1, V v1)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|(
name|K
name|k1
parameter_list|,
name|V
name|v1
parameter_list|)
block|{
return|return
name|ImmutableListMultimap
operator|.
name|of
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable multimap containing the given entries, in order.    */
DECL|method|of (K k1, V v1, K k2, V v2)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|(
name|K
name|k1
parameter_list|,
name|V
name|v1
parameter_list|,
name|K
name|k2
parameter_list|,
name|V
name|v2
parameter_list|)
block|{
return|return
name|ImmutableListMultimap
operator|.
name|of
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|,
name|k2
argument_list|,
name|v2
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable multimap containing the given entries, in order.    */
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|(
name|K
name|k1
parameter_list|,
name|V
name|v1
parameter_list|,
name|K
name|k2
parameter_list|,
name|V
name|v2
parameter_list|,
name|K
name|k3
parameter_list|,
name|V
name|v3
parameter_list|)
block|{
return|return
name|ImmutableListMultimap
operator|.
name|of
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|,
name|k2
argument_list|,
name|v2
argument_list|,
name|k3
argument_list|,
name|v3
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable multimap containing the given entries, in order.    */
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|(
name|K
name|k1
parameter_list|,
name|V
name|v1
parameter_list|,
name|K
name|k2
parameter_list|,
name|V
name|v2
parameter_list|,
name|K
name|k3
parameter_list|,
name|V
name|v3
parameter_list|,
name|K
name|k4
parameter_list|,
name|V
name|v4
parameter_list|)
block|{
return|return
name|ImmutableListMultimap
operator|.
name|of
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|,
name|k2
argument_list|,
name|v2
argument_list|,
name|k3
argument_list|,
name|v3
argument_list|,
name|k4
argument_list|,
name|v4
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable multimap containing the given entries, in order.    */
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|(
name|K
name|k1
parameter_list|,
name|V
name|v1
parameter_list|,
name|K
name|k2
parameter_list|,
name|V
name|v2
parameter_list|,
name|K
name|k3
parameter_list|,
name|V
name|v3
parameter_list|,
name|K
name|k4
parameter_list|,
name|V
name|v4
parameter_list|,
name|K
name|k5
parameter_list|,
name|V
name|v5
parameter_list|)
block|{
return|return
name|ImmutableListMultimap
operator|.
name|of
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|,
name|k2
argument_list|,
name|v2
argument_list|,
name|k3
argument_list|,
name|v3
argument_list|,
name|k4
argument_list|,
name|v4
argument_list|,
name|k5
argument_list|,
name|v5
argument_list|)
return|;
block|}
comment|// looking for of() with> 5 entries? Use the builder instead.
comment|/**    * Returns a new builder. The generated builder is equivalent to the builder    * created by the {@link Builder} constructor.    */
DECL|method|builder ()
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builder
parameter_list|()
block|{
return|return
operator|new
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Multimap for {@link ImmutableMultimap.Builder} that maintains key and    * value orderings, allows duplicate values, and performs better than    * {@link LinkedListMultimap}.    */
DECL|class|BuilderMultimap
specifier|private
specifier|static
class|class
name|BuilderMultimap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|BuilderMultimap ()
name|BuilderMultimap
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|LinkedHashMap
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
DECL|method|createCollection ()
annotation|@
name|Override
name|Collection
argument_list|<
name|V
argument_list|>
name|createCollection
parameter_list|()
block|{
return|return
name|Lists
operator|.
name|newArrayList
argument_list|()
return|;
block|}
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
comment|/**    * A builder for creating immutable multimap instances, especially    * {@code public static final} multimaps ("constant multimaps"). Example:    *<pre>   {@code    *    *   static final Multimap<String, Integer> STRING_TO_INTEGER_MULTIMAP =    *       new ImmutableMultimap.Builder<String, Integer>()    *           .put("one", 1)    *           .putAll("several", 1, 2, 3)    *           .putAll("many", 1, 2, 3, 4, 5)    *           .build();}</pre>    *    *<p>Builder instances can be reused - it is safe to call {@link #build}    * multiple times to build multiple multimaps in series. Each multimap    * contains the key-value mappings in the previously created multimaps.    */
DECL|class|Builder
specifier|public
specifier|static
class|class
name|Builder
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
block|{
DECL|field|builderMultimap
specifier|private
specifier|final
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builderMultimap
init|=
operator|new
name|BuilderMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Creates a new builder. The returned builder is equivalent to the builder      * generated by {@link ImmutableMultimap#builder}.      */
DECL|method|Builder ()
specifier|public
name|Builder
parameter_list|()
block|{}
comment|/**      * Adds a key-value mapping to the built multimap.      */
DECL|method|put (K key, V value)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|put
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|builderMultimap
operator|.
name|put
argument_list|(
name|checkNotNull
argument_list|(
name|key
argument_list|)
argument_list|,
name|checkNotNull
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Stores a collection of values with the same key in the built multimap.      *      * @throws NullPointerException if {@code key}, {@code values}, or any      *     element in {@code values} is null. The builder is left in an invalid      *     state.      */
DECL|method|putAll (K key, Iterable<? extends V> values)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|putAll
parameter_list|(
name|K
name|key
parameter_list|,
name|Iterable
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|values
parameter_list|)
block|{
name|Collection
argument_list|<
name|V
argument_list|>
name|valueList
init|=
name|builderMultimap
operator|.
name|get
argument_list|(
name|checkNotNull
argument_list|(
name|key
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|V
name|value
range|:
name|values
control|)
block|{
name|valueList
operator|.
name|add
argument_list|(
name|checkNotNull
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Stores an array of values with the same key in the built multimap.      *      * @throws NullPointerException if the key or any value is null. The builder      *     is left in an invalid state.      */
DECL|method|putAll (K key, V... values)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|putAll
parameter_list|(
name|K
name|key
parameter_list|,
name|V
modifier|...
name|values
parameter_list|)
block|{
return|return
name|putAll
argument_list|(
name|key
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|values
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Stores another multimap's entries in the built multimap. The generated      * multimap's key and value orderings correspond to the iteration ordering      * of the {@code multimap.asMap()} view, with new keys and values following      * any existing keys and values.      *      * @throws NullPointerException if any key or value in {@code multimap} is      *     null. The builder is left in an invalid state.      */
DECL|method|putAll (Multimap<? extends K, ? extends V> multimap)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|putAll
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
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|Collection
argument_list|<
name|?
extends|extends
name|V
argument_list|>
argument_list|>
name|entry
range|:
name|multimap
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|putAll
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Returns a newly-created immutable multimap.      */
DECL|method|build ()
specifier|public
name|ImmutableMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
block|{
return|return
name|copyOf
argument_list|(
name|builderMultimap
argument_list|)
return|;
block|}
block|}
comment|/**    * Returns an immutable multimap containing the same mappings as    * {@code multimap}. The generated multimap's key and value orderings    * correspond to the iteration ordering of the {@code multimap.asMap()} view.    *    *<p><b>Note:</b> Despite what the method name suggests, if    * {@code multimap} is an {@code ImmutableMultimap}, no copy will actually be    * performed, and the given multimap itself will be returned.    *    * @throws NullPointerException if any key or value in {@code multimap} is    *     null    */
DECL|method|copyOf ( Multimap<? extends K, ? extends V> multimap)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|copyOf
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
if|if
condition|(
name|multimap
operator|instanceof
name|ImmutableMultimap
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// safe since multimap is not writable
name|ImmutableMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|kvMultimap
init|=
operator|(
name|ImmutableMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|multimap
decl_stmt|;
return|return
name|kvMultimap
return|;
block|}
else|else
block|{
return|return
name|ImmutableListMultimap
operator|.
name|copyOf
argument_list|(
name|multimap
argument_list|)
return|;
block|}
block|}
DECL|field|map
specifier|final
specifier|transient
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|?
extends|extends
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
argument_list|>
name|map
decl_stmt|;
DECL|field|size
specifier|final
specifier|transient
name|int
name|size
decl_stmt|;
comment|// These constants allow the deserialization code to set final fields. This
comment|// holder class makes sure they are not initialized unless an instance is
comment|// deserialized.
DECL|class|FieldSettersHolder
specifier|static
class|class
name|FieldSettersHolder
block|{
comment|// Eclipse doesn't like the raw ImmutableMultimap
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|static
specifier|final
name|Serialization
operator|.
name|FieldSetter
argument_list|<
name|ImmutableMultimap
argument_list|>
DECL|field|MAP_FIELD_SETTER
name|MAP_FIELD_SETTER
init|=
name|Serialization
operator|.
name|getFieldSetter
argument_list|(
name|ImmutableMultimap
operator|.
name|class
argument_list|,
literal|"map"
argument_list|)
decl_stmt|;
comment|// Eclipse doesn't like the raw ImmutableMultimap
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|static
specifier|final
name|Serialization
operator|.
name|FieldSetter
argument_list|<
name|ImmutableMultimap
argument_list|>
DECL|field|SIZE_FIELD_SETTER
name|SIZE_FIELD_SETTER
init|=
name|Serialization
operator|.
name|getFieldSetter
argument_list|(
name|ImmutableMultimap
operator|.
name|class
argument_list|,
literal|"size"
argument_list|)
decl_stmt|;
block|}
DECL|method|ImmutableMultimap (ImmutableMap<K, ? extends ImmutableCollection<V>> map, int size)
name|ImmutableMultimap
parameter_list|(
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|?
extends|extends
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
argument_list|>
name|map
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
block|}
comment|// mutators (not supported)
comment|/**    * Guaranteed to throw an exception and leave the multimap unmodified.    *    * @throws UnsupportedOperationException always    */
DECL|method|removeAll (Object key)
specifier|public
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|removeAll
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the multimap unmodified.    *    * @throws UnsupportedOperationException always    */
DECL|method|replaceValues (K key, Iterable<? extends V> values)
specifier|public
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|replaceValues
parameter_list|(
name|K
name|key
parameter_list|,
name|Iterable
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|values
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the multimap unmodified.    *    * @throws UnsupportedOperationException always    */
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Returns an immutable collection of the values for the given key.  If no    * mappings in the multimap have the provided key, an empty immutable    * collection is returned. The values are in the same order as the parameters    * used to build this multimap.    */
DECL|method|get (K key)
specifier|public
specifier|abstract
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|get
parameter_list|(
name|K
name|key
parameter_list|)
function_decl|;
comment|/**    * Guaranteed to throw an exception and leave the multimap unmodified.    *    * @throws UnsupportedOperationException always    */
DECL|method|put (K key, V value)
specifier|public
name|boolean
name|put
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the multimap unmodified.    *    * @throws UnsupportedOperationException always    */
DECL|method|putAll (K key, Iterable<? extends V> values)
specifier|public
name|boolean
name|putAll
parameter_list|(
name|K
name|key
parameter_list|,
name|Iterable
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|values
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the multimap unmodified.    *    * @throws UnsupportedOperationException always    */
DECL|method|putAll (Multimap<? extends K, ? extends V> multimap)
specifier|public
name|boolean
name|putAll
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the multimap unmodified.    *    * @throws UnsupportedOperationException always    */
DECL|method|remove (Object key, Object value)
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|// accessors
DECL|method|containsEntry (@ullable Object key, @Nullable Object value)
specifier|public
name|boolean
name|containsEntry
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|,
annotation|@
name|Nullable
name|Object
name|value
parameter_list|)
block|{
name|Collection
argument_list|<
name|V
argument_list|>
name|values
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|values
operator|!=
literal|null
operator|&&
name|values
operator|.
name|contains
argument_list|(
name|value
argument_list|)
return|;
block|}
DECL|method|containsKey (@ullable Object key)
specifier|public
name|boolean
name|containsKey
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
block|{
return|return
name|map
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|containsValue (@ullable Object value)
specifier|public
name|boolean
name|containsValue
parameter_list|(
annotation|@
name|Nullable
name|Object
name|value
parameter_list|)
block|{
for|for
control|(
name|Collection
argument_list|<
name|V
argument_list|>
name|valueCollection
range|:
name|map
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|valueCollection
operator|.
name|contains
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|size
operator|==
literal|0
return|;
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|size
return|;
block|}
DECL|method|equals (@ullable Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|Multimap
condition|)
block|{
name|Multimap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|that
init|=
operator|(
name|Multimap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|this
operator|.
name|map
operator|.
name|equals
argument_list|(
name|that
operator|.
name|asMap
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|map
operator|.
name|hashCode
argument_list|()
return|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|map
operator|.
name|toString
argument_list|()
return|;
block|}
comment|// views
comment|/**    * Returns an immutable set of the distinct keys in this multimap. These keys    * are ordered according to when they first appeared during the construction    * of this multimap.    */
DECL|method|keySet ()
specifier|public
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
name|map
operator|.
name|keySet
argument_list|()
return|;
block|}
comment|/**    * Returns an immutable map that associates each key with its corresponding    * values in the multimap.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// a widening cast
DECL|method|asMap ()
specifier|public
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|asMap
parameter_list|()
block|{
return|return
operator|(
name|ImmutableMap
operator|)
name|map
return|;
block|}
DECL|field|entries
specifier|private
specifier|transient
name|ImmutableCollection
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entries
decl_stmt|;
comment|/**    * Returns an immutable collection of all key-value pairs in the multimap. Its    * iterator traverses the values for the first key, the values for the second    * key, and so on.    */
DECL|method|entries ()
specifier|public
name|ImmutableCollection
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entries
parameter_list|()
block|{
name|ImmutableCollection
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|result
init|=
name|entries
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
operator|(
name|entries
operator|=
operator|new
name|EntryCollection
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|this
argument_list|)
operator|)
else|:
name|result
return|;
block|}
DECL|class|EntryCollection
specifier|private
specifier|static
class|class
name|EntryCollection
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableCollection
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
block|{
DECL|field|multimap
specifier|final
name|ImmutableMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|multimap
decl_stmt|;
DECL|method|EntryCollection (ImmutableMultimap<K, V> multimap)
name|EntryCollection
parameter_list|(
name|ImmutableMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|multimap
parameter_list|)
block|{
name|this
operator|.
name|multimap
operator|=
name|multimap
expr_stmt|;
block|}
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
name|UnmodifiableIterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
specifier|final
name|Iterator
argument_list|<
name|?
extends|extends
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|?
extends|extends
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
name|mapIterator
init|=
name|this
operator|.
name|multimap
operator|.
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
operator|new
name|UnmodifiableIterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|()
block|{
name|K
name|key
decl_stmt|;
name|Iterator
argument_list|<
name|V
argument_list|>
name|valueIterator
decl_stmt|;
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
operator|(
name|key
operator|!=
literal|null
operator|&&
name|valueIterator
operator|.
name|hasNext
argument_list|()
operator|)
operator|||
name|mapIterator
operator|.
name|hasNext
argument_list|()
return|;
block|}
specifier|public
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|next
parameter_list|()
block|{
if|if
condition|(
name|key
operator|==
literal|null
operator|||
operator|!
name|valueIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|?
extends|extends
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
argument_list|>
name|entry
init|=
name|mapIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|key
operator|=
name|entry
operator|.
name|getKey
argument_list|()
expr_stmt|;
name|valueIterator
operator|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
block|}
return|return
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|key
argument_list|,
name|valueIterator
operator|.
name|next
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|multimap
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|contains (Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|Map
operator|.
name|Entry
condition|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|multimap
operator|.
name|containsEntry
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
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
DECL|field|keys
specifier|private
specifier|transient
name|ImmutableMultiset
argument_list|<
name|K
argument_list|>
name|keys
decl_stmt|;
comment|/**    * Returns a collection, which may contain duplicates, of all keys. The number    * of times a key appears in the returned multiset equals the number of    * mappings the key has in the multimap. Duplicate keys appear consecutively    * in the multiset's iteration order.    */
DECL|method|keys ()
specifier|public
name|ImmutableMultiset
argument_list|<
name|K
argument_list|>
name|keys
parameter_list|()
block|{
name|ImmutableMultiset
argument_list|<
name|K
argument_list|>
name|result
init|=
name|keys
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
operator|(
name|keys
operator|=
name|createKeys
argument_list|()
operator|)
else|:
name|result
return|;
block|}
DECL|method|createKeys ()
specifier|private
name|ImmutableMultiset
argument_list|<
name|K
argument_list|>
name|createKeys
parameter_list|()
block|{
name|ImmutableMultiset
operator|.
name|Builder
argument_list|<
name|K
argument_list|>
name|builder
init|=
name|ImmutableMultiset
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|?
extends|extends
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|builder
operator|.
name|addCopies
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
DECL|field|values
specifier|private
specifier|transient
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|values
decl_stmt|;
comment|/**    * Returns an immutable collection of the values in this multimap. Its    * iterator traverses the values for the first key, the values for the second    * key, and so on.    */
DECL|method|values ()
specifier|public
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|result
init|=
name|values
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
operator|(
name|values
operator|=
operator|new
name|Values
argument_list|<
name|V
argument_list|>
argument_list|(
name|this
argument_list|)
operator|)
else|:
name|result
return|;
block|}
DECL|class|Values
specifier|private
specifier|static
class|class
name|Values
parameter_list|<
name|V
parameter_list|>
extends|extends
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
block|{
DECL|field|multimap
specifier|final
name|Multimap
argument_list|<
name|?
argument_list|,
name|V
argument_list|>
name|multimap
decl_stmt|;
DECL|method|Values (Multimap<?, V> multimap)
name|Values
parameter_list|(
name|Multimap
argument_list|<
name|?
argument_list|,
name|V
argument_list|>
name|multimap
parameter_list|)
block|{
name|this
operator|.
name|multimap
operator|=
name|multimap
expr_stmt|;
block|}
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
name|UnmodifiableIterator
argument_list|<
name|V
argument_list|>
name|iterator
parameter_list|()
block|{
specifier|final
name|Iterator
argument_list|<
name|?
extends|extends
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|V
argument_list|>
argument_list|>
name|entryIterator
init|=
name|multimap
operator|.
name|entries
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
operator|new
name|UnmodifiableIterator
argument_list|<
name|V
argument_list|>
argument_list|()
block|{
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|entryIterator
operator|.
name|hasNext
argument_list|()
return|;
block|}
specifier|public
name|V
name|next
parameter_list|()
block|{
return|return
name|entryIterator
operator|.
name|next
argument_list|()
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|multimap
operator|.
name|size
argument_list|()
return|;
block|}
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

