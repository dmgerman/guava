begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|InvalidObjectException
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
comment|/**  * An immutable {@link SetMultimap} with reliable user-specified key and value  * iteration order. Does not permit null keys or values.  *  *<p>Unlike {@link Multimaps#unmodifiableSetMultimap(SetMultimap)}, which is  * a<i>view</i> of a separate multimap which can still change, an instance of  * {@code ImmutableSetMultimap} contains its own data and will<i>never</i>  * change. {@code ImmutableSetMultimap} is convenient for  * {@code public static final} multimaps ("constant multimaps") and also lets  * you easily make a "defensive copy" of a multimap provided to your class by  * a caller.  *  *<p><b>Note</b>: Although this class is not final, it cannot be subclassed as  * it has no public or protected constructors. Thus, instances of this class  * are guaranteed to be immutable.  *  * @author Mike Ward  * @since 2 (imported from Google Collections Library)  */
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
DECL|class|ImmutableSetMultimap
specifier|public
class|class
name|ImmutableSetMultimap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/** Returns the empty multimap. */
comment|// Casting is safe because the multimap will never hold any elements.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|of ()
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|of
parameter_list|()
block|{
return|return
operator|(
name|ImmutableSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|EmptyImmutableSetMultimap
operator|.
name|INSTANCE
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
name|ImmutableSetMultimap
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
name|ImmutableSetMultimap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builder
init|=
name|ImmutableSetMultimap
operator|.
name|builder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**    * Returns an immutable multimap containing the given entries, in order.    * Repeated occurrences of an entry (according to {@link Object#equals}) after    * the first are ignored.    */
DECL|method|of (K k1, V v1, K k2, V v2)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableSetMultimap
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
name|ImmutableSetMultimap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builder
init|=
name|ImmutableSetMultimap
operator|.
name|builder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
expr_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**    * Returns an immutable multimap containing the given entries, in order.    * Repeated occurrences of an entry (according to {@link Object#equals}) after    * the first are ignored.    */
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableSetMultimap
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
name|ImmutableSetMultimap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builder
init|=
name|ImmutableSetMultimap
operator|.
name|builder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
expr_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
expr_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|k3
argument_list|,
name|v3
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**    * Returns an immutable multimap containing the given entries, in order.    * Repeated occurrences of an entry (according to {@link Object#equals}) after    * the first are ignored.    */
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableSetMultimap
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
name|ImmutableSetMultimap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builder
init|=
name|ImmutableSetMultimap
operator|.
name|builder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
expr_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
expr_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|k3
argument_list|,
name|v3
argument_list|)
expr_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|k4
argument_list|,
name|v4
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**    * Returns an immutable multimap containing the given entries, in order.    * Repeated occurrences of an entry (according to {@link Object#equals}) after    * the first are ignored.    */
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableSetMultimap
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
name|ImmutableSetMultimap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builder
init|=
name|ImmutableSetMultimap
operator|.
name|builder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
expr_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|k2
argument_list|,
name|v2
argument_list|)
expr_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|k3
argument_list|,
name|v3
argument_list|)
expr_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|k4
argument_list|,
name|v4
argument_list|)
expr_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|k5
argument_list|,
name|v5
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
comment|// looking for of() with> 5 entries? Use the builder instead.
comment|/**    * Returns a new {@link Builder}.    */
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
comment|/**    * Multimap for {@link ImmutableSetMultimap.Builder} that maintains key    * and value orderings and performs better than {@link LinkedHashMultimap}.    */
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
name|Sets
operator|.
name|newLinkedHashSet
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
comment|/**    * A builder for creating immutable {@code SetMultimap} instances, especially    * {@code public static final} multimaps ("constant multimaps"). Example:    *<pre>   {@code    *    *   static final Multimap<String, Integer> STRING_TO_INTEGER_MULTIMAP =    *       new ImmutableSetMultimap.Builder<String, Integer>()    *           .put("one", 1)    *           .putAll("several", 1, 2, 3)    *           .putAll("many", 1, 2, 3, 4, 5)    *           .build();}</pre>    *    *<p>Builder instances can be reused - it is safe to call {@link #build}    * multiple times to build multiple multimaps in series. Each multimap    * contains the key-value mappings in the previously created multimaps.    *    * @since 2 (imported from Google Collections Library)    */
DECL|class|Builder
specifier|public
specifier|static
specifier|final
class|class
name|Builder
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableMultimap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
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
comment|/**      * Creates a new builder. The returned builder is equivalent to the builder      * generated by {@link ImmutableSetMultimap#builder}.      */
DECL|method|Builder ()
specifier|public
name|Builder
parameter_list|()
block|{}
comment|/**      * Adds a key-value mapping to the built multimap if it is not already      * present.      */
DECL|method|put (K key, V value)
annotation|@
name|Override
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
annotation|@
name|Override
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
name|collection
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
name|collection
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
comment|/**      * Stores an array of values with the same key in the built multimap.      *      * @throws NullPointerException if the key or any value is null. The      *     builder is left in an invalid state.      */
DECL|method|putAll (K key, V... values)
annotation|@
name|Override
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
DECL|method|putAll ( Multimap<? extends K, ? extends V> multimap)
annotation|@
name|Override
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
comment|/**      * Returns a newly-created immutable set multimap.      */
DECL|method|build ()
annotation|@
name|Override
specifier|public
name|ImmutableSetMultimap
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
comment|/**    * Returns an immutable set multimap containing the same mappings as    * {@code multimap}. The generated multimap's key and value orderings    * correspond to the iteration ordering of the {@code multimap.asMap()} view.    * Repeated occurrences of an entry in the multimap after the first are    * ignored.    *    *<p><b>Note:</b> Despite what the method name suggests, if    * {@code multimap} is an {@code ImmutableSetMultimap}, no copy will actually    * be performed, and the given multimap itself will be returned.    *    * @throws NullPointerException if any key or value in {@code multimap} is    *     null    */
DECL|method|copyOf ( Multimap<? extends K, ? extends V> multimap)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableSetMultimap
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
name|checkNotNull
argument_list|(
name|multimap
argument_list|)
expr_stmt|;
comment|// eager for GWT
if|if
condition|(
name|multimap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|of
argument_list|()
return|;
block|}
if|if
condition|(
name|multimap
operator|instanceof
name|ImmutableSetMultimap
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// safe since multimap is not writable
name|ImmutableSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|kvMultimap
init|=
operator|(
name|ImmutableSetMultimap
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
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|ImmutableSet
argument_list|<
name|V
argument_list|>
argument_list|>
name|builder
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
name|int
name|size
init|=
literal|0
decl_stmt|;
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
name|K
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|values
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|ImmutableSet
argument_list|<
name|V
argument_list|>
name|set
init|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|values
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|set
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|builder
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|set
argument_list|)
expr_stmt|;
name|size
operator|+=
name|set
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ImmutableSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|,
name|size
argument_list|)
return|;
block|}
DECL|method|ImmutableSetMultimap (ImmutableMap<K, ImmutableSet<V>> map, int size)
name|ImmutableSetMultimap
parameter_list|(
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
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|super
argument_list|(
name|map
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
comment|// views
comment|/**    * Returns an immutable set of the values for the given key.  If no mappings    * in the multimap have the provided key, an empty immutable set is returned.    * The values are in the same order as the parameters used to build this    * multimap.    */
DECL|method|get (@ullable K key)
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
name|K
name|key
parameter_list|)
block|{
comment|// This cast is safe as its type is known in constructor.
name|ImmutableSet
argument_list|<
name|V
argument_list|>
name|set
init|=
operator|(
name|ImmutableSet
argument_list|<
name|V
argument_list|>
operator|)
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|set
operator|==
literal|null
operator|)
condition|?
name|ImmutableSet
operator|.
expr|<
name|V
operator|>
name|of
argument_list|()
else|:
name|set
return|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the multimap unmodified.    *    * @throws UnsupportedOperationException always    */
DECL|method|removeAll (Object key)
annotation|@
name|Override
specifier|public
name|ImmutableSet
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
DECL|method|replaceValues ( K key, Iterable<? extends V> values)
annotation|@
name|Override
specifier|public
name|ImmutableSet
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
DECL|field|entries
specifier|private
specifier|transient
name|ImmutableSet
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
comment|/**    * Returns an immutable collection of all key-value pairs in the multimap.    * Its iterator traverses the values for the first key, the values for the    * second key, and so on.    */
comment|// TODO(kevinb): Fix this so that two copies of the entries are not created.
DECL|method|entries ()
annotation|@
name|Override
specifier|public
name|ImmutableSet
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
name|ImmutableSet
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
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|super
operator|.
name|entries
argument_list|()
argument_list|)
operator|)
else|:
name|result
return|;
block|}
comment|/**    * @serialData number of distinct keys, and then for each distinct key: the    *     key, the number of values for that key, and the key's values    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.io.ObjectOutputStream"
argument_list|)
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
argument_list|(
literal|"java.io.ObjectInputStream"
argument_list|)
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
name|int
name|keyCount
init|=
name|stream
operator|.
name|readInt
argument_list|()
decl_stmt|;
if|if
condition|(
name|keyCount
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|InvalidObjectException
argument_list|(
literal|"Invalid key count "
operator|+
name|keyCount
argument_list|)
throw|;
block|}
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|Object
argument_list|,
name|ImmutableSet
argument_list|<
name|Object
argument_list|>
argument_list|>
name|builder
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
name|int
name|tmpSize
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|keyCount
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|key
init|=
name|stream
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|int
name|valueCount
init|=
name|stream
operator|.
name|readInt
argument_list|()
decl_stmt|;
if|if
condition|(
name|valueCount
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|InvalidObjectException
argument_list|(
literal|"Invalid value count "
operator|+
name|valueCount
argument_list|)
throw|;
block|}
name|Object
index|[]
name|array
init|=
operator|new
name|Object
index|[
name|valueCount
index|]
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|valueCount
condition|;
name|j
operator|++
control|)
block|{
name|array
index|[
name|j
index|]
operator|=
name|stream
operator|.
name|readObject
argument_list|()
expr_stmt|;
block|}
name|ImmutableSet
argument_list|<
name|Object
argument_list|>
name|valueSet
init|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|array
argument_list|)
decl_stmt|;
if|if
condition|(
name|valueSet
operator|.
name|size
argument_list|()
operator|!=
name|array
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|InvalidObjectException
argument_list|(
literal|"Duplicate key-value pairs exist for key "
operator|+
name|key
argument_list|)
throw|;
block|}
name|builder
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|valueSet
argument_list|)
expr_stmt|;
name|tmpSize
operator|+=
name|valueCount
expr_stmt|;
block|}
name|ImmutableMap
argument_list|<
name|Object
argument_list|,
name|ImmutableSet
argument_list|<
name|Object
argument_list|>
argument_list|>
name|tmpMap
decl_stmt|;
try|try
block|{
name|tmpMap
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|(
name|InvalidObjectException
operator|)
operator|new
name|InvalidObjectException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|FieldSettersHolder
operator|.
name|MAP_FIELD_SETTER
operator|.
name|set
argument_list|(
name|this
argument_list|,
name|tmpMap
argument_list|)
expr_stmt|;
name|FieldSettersHolder
operator|.
name|SIZE_FIELD_SETTER
operator|.
name|set
argument_list|(
name|this
argument_list|,
name|tmpSize
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"not needed in emulated source."
argument_list|)
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

