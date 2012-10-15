begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Iterables
operator|.
name|getOnlyElement
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
name|ArrayList
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
name|HashMap
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
comment|/**  * An immutable, hash-based {@link Map} with reliable user-specified iteration  * order. Does not permit null keys or values.  *  *<p>Unlike {@link Collections#unmodifiableMap}, which is a<i>view</i> of a  * separate map which can still change, an instance of {@code ImmutableMap}  * contains its own data and will<i>never</i> change. {@code ImmutableMap} is  * convenient for {@code public static final} maps ("constant maps") and also  * lets you easily make a "defensive copy" of a map provided to your class by a  * caller.  *  *<p><i>Performance notes:</i> unlike {@link HashMap}, {@code ImmutableMap} is  * not optimized for element types that have slow {@link Object#equals} or  * {@link Object#hashCode} implementations. You can get better performance by  * having your element type cache its own hash codes, and by making use of the  * cached values to short-circuit a slow {@code equals} algorithm.  *  *<p>See the Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/ImmutableCollectionsExplained">  * immutable collections</a>.  *  * @author Jesse Wilson  * @author Kevin Bourrillion  * @since 2.0 (imported from Google Collections Library)  */
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// we're overriding default serialization
DECL|class|ImmutableMap
specifier|public
specifier|abstract
class|class
name|ImmutableMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|,
name|Serializable
block|{
comment|/**    * Returns the empty map. This map behaves and performs comparably to    * {@link Collections#emptyMap}, and is preferable mainly for consistency    * and maintainability of your code.    */
DECL|method|of ()
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|()
block|{
return|return
name|ImmutableBiMap
operator|.
name|of
argument_list|()
return|;
block|}
comment|/**    * Returns an immutable map containing a single entry. This map behaves and    * performs comparably to {@link Collections#singletonMap} but will not accept    * a null key or value. It is preferable mainly for consistency and    * maintainability of your code.    */
DECL|method|of (K k1, V v1)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMap
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
name|ImmutableBiMap
operator|.
name|of
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable map containing the given entries, in order.    *    * @throws IllegalArgumentException if duplicate keys are provided    */
DECL|method|of (K k1, V v1, K k2, V v2)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMap
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
operator|new
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|entryOf
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable map containing the given entries, in order.    *    * @throws IllegalArgumentException if duplicate keys are provided    */
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMap
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
operator|new
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|entryOf
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k3
argument_list|,
name|v3
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable map containing the given entries, in order.    *    * @throws IllegalArgumentException if duplicate keys are provided    */
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMap
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
operator|new
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|entryOf
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k3
argument_list|,
name|v3
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k4
argument_list|,
name|v4
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable map containing the given entries, in order.    *    * @throws IllegalArgumentException if duplicate keys are provided    */
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMap
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
operator|new
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|entryOf
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k3
argument_list|,
name|v3
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k4
argument_list|,
name|v4
argument_list|)
argument_list|,
name|entryOf
argument_list|(
name|k5
argument_list|,
name|v5
argument_list|)
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
comment|/**    * Verifies that {@code key} and {@code value} are non-null, and returns a new    * immutable entry with those values.    *    *<p>A call to {@link Map.Entry#setValue} on the returned entry will always    * throw {@link UnsupportedOperationException}.    */
DECL|method|entryOf (K key, V value)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entryOf
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|key
argument_list|,
literal|"null key in entry: null=%s"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|value
argument_list|,
literal|"null value in entry: %s=null"
argument_list|,
name|key
argument_list|)
expr_stmt|;
return|return
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**    * A builder for creating immutable map instances, especially {@code public    * static final} maps ("constant maps"). Example:<pre>   {@code    *    *   static final ImmutableMap<String, Integer> WORD_TO_INT =    *       new ImmutableMap.Builder<String, Integer>()    *           .put("one", 1)    *           .put("two", 2)    *           .put("three", 3)    *           .build();}</pre>    *    * For<i>small</i> immutable maps, the {@code ImmutableMap.of()} methods are    * even more convenient.    *    *<p>Builder instances can be reused - it is safe to call {@link #build}    * multiple times to build multiple maps in series. Each map is a superset of    * the maps created before it.    *    * @since 2.0 (imported from Google Collections Library)    */
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
DECL|field|entries
specifier|final
name|ArrayList
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entries
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
comment|/**      * Creates a new builder. The returned builder is equivalent to the builder      * generated by {@link ImmutableMap#builder}.      */
DECL|method|Builder ()
specifier|public
name|Builder
parameter_list|()
block|{}
comment|/**      * Associates {@code key} with {@code value} in the built map. Duplicate      * keys are not allowed, and will cause {@link #build} to fail.      */
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
name|entries
operator|.
name|add
argument_list|(
name|entryOf
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds the given {@code entry} to the map, making it immutable if      * necessary. Duplicate keys are not allowed, and will cause {@link #build}      * to fail.      *      * @since 11.0      */
DECL|method|put (Entry<? extends K, ? extends V> entry)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|put
parameter_list|(
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|entry
parameter_list|)
block|{
name|K
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|V
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|instanceof
name|ImmutableEntry
condition|)
block|{
name|checkNotNull
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// all supported methods are covariant
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|immutableEntry
init|=
operator|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|entry
decl_stmt|;
name|entries
operator|.
name|add
argument_list|(
name|immutableEntry
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Directly calling entryOf(entry.getKey(), entry.getValue()) can cause
comment|// compilation error in Eclipse.
name|entries
operator|.
name|add
argument_list|(
name|entryOf
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Associates all of the given map's keys and values in the built map.      * Duplicate keys are not allowed, and will cause {@link #build} to fail.      *      * @throws NullPointerException if any key or value in {@code map} is null      */
DECL|method|putAll (Map<? extends K, ? extends V> map)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|map
parameter_list|)
block|{
name|entries
operator|.
name|ensureCapacity
argument_list|(
name|entries
operator|.
name|size
argument_list|()
operator|+
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|put
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
comment|/*      * TODO(kevinb): Should build() and the ImmutableBiMap& ImmutableSortedMap      * versions throw an IllegalStateException instead?      */
comment|/**      * Returns a newly-created immutable map.      *      * @throws IllegalArgumentException if duplicate keys were added      */
DECL|method|build ()
specifier|public
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
block|{
return|return
name|fromEntryList
argument_list|(
name|entries
argument_list|)
return|;
block|}
DECL|method|fromEntryList ( List<Entry<K, V>> entries)
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|fromEntryList
parameter_list|(
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entries
parameter_list|)
block|{
name|int
name|size
init|=
name|entries
operator|.
name|size
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|size
condition|)
block|{
case|case
literal|0
case|:
return|return
name|of
argument_list|()
return|;
case|case
literal|1
case|:
return|return
operator|new
name|SingletonImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|getOnlyElement
argument_list|(
name|entries
argument_list|)
argument_list|)
return|;
default|default:
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
index|[]
name|entryArray
init|=
name|entries
operator|.
name|toArray
argument_list|(
operator|new
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
index|[
name|entries
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
return|return
operator|new
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|entryArray
argument_list|)
return|;
block|}
block|}
block|}
comment|/**    * Returns an immutable map containing the same entries as {@code map}. If    * {@code map} somehow contains entries with duplicate keys (for example, if    * it is a {@code SortedMap} whose comparator is not<i>consistent with    * equals</i>), the results of this method are undefined.    *    *<p>Despite the method name, this method attempts to avoid actually copying    * the data when it is safe to do so. The exact circumstances under which a    * copy will or will not be performed are undocumented and subject to change.    *    * @throws NullPointerException if any key or value in {@code map} is null    */
DECL|method|copyOf ( Map<? extends K, ? extends V> map)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|copyOf
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
operator|(
name|map
operator|instanceof
name|ImmutableMap
operator|)
operator|&&
operator|!
operator|(
name|map
operator|instanceof
name|ImmutableSortedMap
operator|)
condition|)
block|{
comment|// TODO(user): Make ImmutableMap.copyOf(immutableBiMap) call copyOf()
comment|// on the ImmutableMap delegate(), rather than the bimap itself
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// safe since map is not writable
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|kvMap
init|=
operator|(
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|map
decl_stmt|;
if|if
condition|(
operator|!
name|kvMap
operator|.
name|isPartialView
argument_list|()
condition|)
block|{
return|return
name|kvMap
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// we won't write to this array
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|entries
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|Entry
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|entries
operator|.
name|length
condition|)
block|{
case|case
literal|0
case|:
return|return
name|of
argument_list|()
return|;
case|case
literal|1
case|:
return|return
operator|new
name|SingletonImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|entryOf
argument_list|(
name|entries
index|[
literal|0
index|]
operator|.
name|getKey
argument_list|()
argument_list|,
name|entries
index|[
literal|0
index|]
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
return|;
default|default:
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|entries
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|K
name|k
init|=
name|entries
index|[
name|i
index|]
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|V
name|v
init|=
name|entries
index|[
name|i
index|]
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|entries
index|[
name|i
index|]
operator|=
name|entryOf
argument_list|(
name|k
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|entries
argument_list|)
return|;
block|}
block|}
DECL|method|ImmutableMap ()
name|ImmutableMap
parameter_list|()
block|{}
comment|/**    * Guaranteed to throw an exception and leave the map unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|put (K k, V v)
specifier|public
specifier|final
name|V
name|put
parameter_list|(
name|K
name|k
parameter_list|,
name|V
name|v
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the map unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|remove (Object o)
specifier|public
specifier|final
name|V
name|remove
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the map unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|putAll (Map<? extends K, ? extends V> map)
specifier|public
specifier|final
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|map
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the map unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|clear ()
specifier|public
specifier|final
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
annotation|@
name|Override
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|size
argument_list|()
operator|==
literal|0
return|;
block|}
annotation|@
name|Override
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
name|get
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
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
return|return
name|value
operator|!=
literal|null
operator|&&
name|Maps
operator|.
name|containsValueImpl
argument_list|(
name|this
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|// Overriding to mark it Nullable
annotation|@
name|Override
DECL|method|get (@ullable Object key)
specifier|public
specifier|abstract
name|V
name|get
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
function_decl|;
DECL|field|entrySet
specifier|private
specifier|transient
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entrySet
decl_stmt|;
comment|/**    * Returns an immutable set of the mappings in this map. The entries are in    * the same order as the parameters used to build this map.    */
annotation|@
name|Override
DECL|method|entrySet ()
specifier|public
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|result
init|=
name|entrySet
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|entrySet
operator|=
name|createEntrySet
argument_list|()
else|:
name|result
return|;
block|}
DECL|method|createEntrySet ()
specifier|abstract
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|createEntrySet
parameter_list|()
function_decl|;
DECL|field|keySet
specifier|private
specifier|transient
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|keySet
decl_stmt|;
comment|/**    * Returns an immutable set of the keys in this map. These keys are in    * the same order as the parameters used to build this map.    */
annotation|@
name|Override
DECL|method|keySet ()
specifier|public
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|result
init|=
name|keySet
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|keySet
operator|=
name|createKeySet
argument_list|()
else|:
name|result
return|;
block|}
DECL|method|createKeySet ()
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|createKeySet
parameter_list|()
block|{
return|return
operator|new
name|ImmutableMapKeySet
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|()
block|{
return|return
name|ImmutableMap
operator|.
name|this
return|;
block|}
block|}
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
comment|/**    * Returns an immutable collection of the values in this map. The values are    * in the same order as the parameters used to build this map.    */
annotation|@
name|Override
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
name|values
operator|=
name|createValues
argument_list|()
else|:
name|result
return|;
block|}
DECL|method|createValues ()
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|createValues
parameter_list|()
block|{
return|return
operator|new
name|ImmutableMapValues
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|()
block|{
return|return
name|ImmutableMap
operator|.
name|this
return|;
block|}
block|}
return|;
block|}
comment|// cached so that this.multimapView().inverse() only computes inverse once
DECL|field|multimapView
specifier|private
specifier|transient
name|ImmutableSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|multimapView
decl_stmt|;
comment|/**    * Returns a multimap view of the map.    *    * @since 14.0    */
annotation|@
name|Beta
DECL|method|asMultimap ()
specifier|public
name|ImmutableSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|asMultimap
parameter_list|()
block|{
name|ImmutableSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|result
init|=
name|multimapView
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
operator|(
name|multimapView
operator|=
name|createMultimapView
argument_list|()
operator|)
else|:
name|result
return|;
block|}
DECL|method|createMultimapView ()
specifier|private
name|ImmutableSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|createMultimapView
parameter_list|()
block|{
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|ImmutableSet
argument_list|<
name|V
argument_list|>
argument_list|>
name|map
init|=
name|viewMapValuesAsSingletonSets
argument_list|()
decl_stmt|;
return|return
operator|new
name|ImmutableSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|map
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|viewMapValuesAsSingletonSets ()
specifier|private
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|ImmutableSet
argument_list|<
name|V
argument_list|>
argument_list|>
name|viewMapValuesAsSingletonSets
parameter_list|()
block|{
class|class
name|MapViewOfValuesAsSingletonSets
extends|extends
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|ImmutableSet
argument_list|<
name|V
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|ImmutableMap
operator|.
name|this
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
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
name|ImmutableMap
operator|.
name|this
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ImmutableSet
argument_list|<
name|V
argument_list|>
name|get
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
block|{
name|V
name|outerValue
init|=
name|ImmutableMap
operator|.
name|this
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|outerValue
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|ImmutableSet
operator|.
name|of
argument_list|(
name|outerValue
argument_list|)
return|;
block|}
annotation|@
name|Override
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|ImmutableSet
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
name|createEntrySet
parameter_list|()
block|{
return|return
operator|new
name|ImmutableMapEntrySet
argument_list|<
name|K
argument_list|,
name|ImmutableSet
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|ImmutableSet
argument_list|<
name|V
argument_list|>
argument_list|>
name|map
parameter_list|()
block|{
return|return
name|MapViewOfValuesAsSingletonSets
operator|.
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|UnmodifiableIterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|ImmutableSet
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
specifier|final
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|backingIterator
init|=
operator|(
operator|(
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|ImmutableMap
operator|.
name|this
operator|)
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
name|Entry
argument_list|<
name|K
argument_list|,
name|ImmutableSet
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|backingIterator
operator|.
name|hasNext
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|ImmutableSet
argument_list|<
name|V
argument_list|>
argument_list|>
name|next
parameter_list|()
block|{
specifier|final
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|backingEntry
init|=
name|backingIterator
operator|.
name|next
argument_list|()
decl_stmt|;
return|return
operator|new
name|AbstractMapEntry
argument_list|<
name|K
argument_list|,
name|ImmutableSet
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|K
name|getKey
parameter_list|()
block|{
return|return
name|backingEntry
operator|.
name|getKey
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ImmutableSet
argument_list|<
name|V
argument_list|>
name|getValue
parameter_list|()
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|(
name|backingEntry
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
return|;
block|}
block|}
return|;
block|}
block|}
return|return
operator|new
name|MapViewOfValuesAsSingletonSets
argument_list|()
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
return|return
name|Maps
operator|.
name|equalsImpl
argument_list|(
name|this
argument_list|,
name|object
argument_list|)
return|;
block|}
DECL|method|isPartialView ()
specifier|abstract
name|boolean
name|isPartialView
parameter_list|()
function_decl|;
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
comment|// not caching hash code since it could change if map values are mutable
comment|// in a way that modifies their hash codes
return|return
name|entrySet
argument_list|()
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
name|Maps
operator|.
name|toStringImpl
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**    * Serialized type for all ImmutableMap instances. It captures the logical    * contents and they are reconstructed using public factory methods. This    * ensures that the implementation types remain as implementation details.    */
DECL|class|SerializedForm
specifier|static
class|class
name|SerializedForm
implements|implements
name|Serializable
block|{
DECL|field|keys
specifier|private
specifier|final
name|Object
index|[]
name|keys
decl_stmt|;
DECL|field|values
specifier|private
specifier|final
name|Object
index|[]
name|values
decl_stmt|;
DECL|method|SerializedForm (ImmutableMap<?, ?> map)
name|SerializedForm
parameter_list|(
name|ImmutableMap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
name|keys
operator|=
operator|new
name|Object
index|[
name|map
operator|.
name|size
argument_list|()
index|]
expr_stmt|;
name|values
operator|=
operator|new
name|Object
index|[
name|map
operator|.
name|size
argument_list|()
index|]
expr_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|keys
index|[
name|i
index|]
operator|=
name|entry
operator|.
name|getKey
argument_list|()
expr_stmt|;
name|values
index|[
name|i
index|]
operator|=
name|entry
operator|.
name|getValue
argument_list|()
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
block|}
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
name|Builder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
operator|new
name|Builder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
return|return
name|createMap
argument_list|(
name|builder
argument_list|)
return|;
block|}
DECL|method|createMap (Builder<Object, Object> builder)
name|Object
name|createMap
parameter_list|(
name|Builder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|keys
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|builder
operator|.
name|put
argument_list|(
name|keys
index|[
name|i
index|]
argument_list|,
name|values
index|[
name|i
index|]
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
DECL|method|writeReplace ()
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|SerializedForm
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

