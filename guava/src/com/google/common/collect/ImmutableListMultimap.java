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
name|Map
operator|.
name|Entry
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
comment|/**  * An immutable {@link ListMultimap} with reliable user-specified key and value  * iteration order. Does not permit null keys or values.  *  *<p>Unlike {@link Multimaps#unmodifiableListMultimap(ListMultimap)}, which is  * a<i>view</i> of a separate multimap which can still change, an instance of  * {@code ImmutableListMultimap} contains its own data and will<i>never</i>  * change. {@code ImmutableListMultimap} is convenient for  * {@code public static final} multimaps ("constant multimaps") and also lets  * you easily make a "defensive copy" of a multimap provided to your class by  * a caller.  *  *<p><b>Note:</b> Although this class is not final, it cannot be subclassed as  * it has no public or protected constructors. Thus, instances of this class  * are guaranteed to be immutable.  *  *<p>See the Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/ImmutableCollectionsExplained">  * immutable collections</a>.  *  * @author Jared Levy  * @since 2.0 (imported from Google Collections Library)  */
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
DECL|class|ImmutableListMultimap
specifier|public
class|class
name|ImmutableListMultimap
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
name|ListMultimap
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
name|ImmutableListMultimap
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
name|ImmutableListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|EmptyImmutableListMultimap
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
name|ImmutableListMultimap
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
name|ImmutableListMultimap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builder
init|=
name|ImmutableListMultimap
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
comment|/**    * Returns an immutable multimap containing the given entries, in order.    */
DECL|method|of (K k1, V v1, K k2, V v2)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableListMultimap
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
name|ImmutableListMultimap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builder
init|=
name|ImmutableListMultimap
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
comment|/**    * Returns an immutable multimap containing the given entries, in order.    */
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableListMultimap
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
name|ImmutableListMultimap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builder
init|=
name|ImmutableListMultimap
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
comment|/**    * Returns an immutable multimap containing the given entries, in order.    */
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableListMultimap
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
name|ImmutableListMultimap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builder
init|=
name|ImmutableListMultimap
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
comment|/**    * Returns an immutable multimap containing the given entries, in order.    */
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableListMultimap
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
name|ImmutableListMultimap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|builder
init|=
name|ImmutableListMultimap
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
comment|/**    * A builder for creating immutable {@code ListMultimap} instances, especially    * {@code public static final} multimaps ("constant multimaps"). Example:    *<pre>   {@code    *    *   static final Multimap<String, Integer> STRING_TO_INTEGER_MULTIMAP =    *       new ImmutableListMultimap.Builder<String, Integer>()    *           .put("one", 1)    *           .putAll("several", 1, 2, 3)    *           .putAll("many", 1, 2, 3, 4, 5)    *           .build();}</pre>    *    *<p>Builder instances can be reused; it is safe to call {@link #build} multiple    * times to build multiple multimaps in series. Each multimap contains the    * key-value mappings in the previously created multimaps.    *    * @since 2.0 (imported from Google Collections Library)    */
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
comment|/**      * Creates a new builder. The returned builder is equivalent to the builder      * generated by {@link ImmutableListMultimap#builder}.      */
DECL|method|Builder ()
specifier|public
name|Builder
parameter_list|()
block|{}
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
name|super
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * {@inheritDoc}      *      * @since 11.0      */
DECL|method|put ( Entry<? extends K, ? extends V> entry)
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
name|super
operator|.
name|put
argument_list|(
name|entry
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
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
name|super
operator|.
name|putAll
argument_list|(
name|key
argument_list|,
name|values
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
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
name|super
operator|.
name|putAll
argument_list|(
name|key
argument_list|,
name|values
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
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
name|super
operator|.
name|putAll
argument_list|(
name|multimap
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * {@inheritDoc}      *      * @since 8.0      */
annotation|@
name|Override
DECL|method|orderKeysBy (Comparator<? super K> keyComparator)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|orderKeysBy
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|keyComparator
parameter_list|)
block|{
name|super
operator|.
name|orderKeysBy
argument_list|(
name|keyComparator
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * {@inheritDoc}      *      * @since 8.0      */
annotation|@
name|Override
DECL|method|orderValuesBy (Comparator<? super V> valueComparator)
specifier|public
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|orderValuesBy
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
name|valueComparator
parameter_list|)
block|{
name|super
operator|.
name|orderValuesBy
argument_list|(
name|valueComparator
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Returns a newly-created immutable list multimap.      */
DECL|method|build ()
annotation|@
name|Override
specifier|public
name|ImmutableListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
block|{
return|return
operator|(
name|ImmutableListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|super
operator|.
name|build
argument_list|()
return|;
block|}
block|}
comment|/**    * Returns an immutable multimap containing the same mappings as {@code    * multimap}. The generated multimap's key and value orderings correspond to    * the iteration ordering of the {@code multimap.asMap()} view.    *    *<p>Despite the method name, this method attempts to avoid actually copying    * the data when it is safe to do so. The exact circumstances under which a    * copy will or will not be performed are undocumented and subject to change.    *    * @throws NullPointerException if any key or value in {@code multimap} is    *         null    */
DECL|method|copyOf ( Multimap<? extends K, ? extends V> multimap)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableListMultimap
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
comment|// TODO(user): copy ImmutableSetMultimap by using asList() on the sets
if|if
condition|(
name|multimap
operator|instanceof
name|ImmutableListMultimap
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// safe since multimap is not writable
name|ImmutableListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|kvMultimap
init|=
operator|(
name|ImmutableListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|multimap
decl_stmt|;
if|if
condition|(
operator|!
name|kvMultimap
operator|.
name|isPartialView
argument_list|()
condition|)
block|{
return|return
name|kvMultimap
return|;
block|}
block|}
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|ImmutableList
argument_list|<
name|V
argument_list|>
argument_list|>
name|builder
init|=
operator|new
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|ImmutableList
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|(
name|multimap
operator|.
name|asMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|size
init|=
literal|0
decl_stmt|;
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
name|ImmutableList
argument_list|<
name|V
argument_list|>
name|list
init|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|builder
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|list
argument_list|)
expr_stmt|;
name|size
operator|+=
name|list
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ImmutableListMultimap
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
DECL|method|ImmutableListMultimap (ImmutableMap<K, ImmutableList<V>> map, int size)
name|ImmutableListMultimap
parameter_list|(
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|ImmutableList
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
comment|/**    * Returns an immutable list of the values for the given key.  If no mappings    * in the multimap have the provided key, an empty immutable list is    * returned. The values are in the same order as the parameters used to build    * this multimap.    */
DECL|method|get (@ullable K key)
annotation|@
name|Override
specifier|public
name|ImmutableList
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
name|ImmutableList
argument_list|<
name|V
argument_list|>
name|list
init|=
operator|(
name|ImmutableList
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
name|list
operator|==
literal|null
operator|)
condition|?
name|ImmutableList
operator|.
expr|<
name|V
operator|>
name|of
argument_list|()
else|:
name|list
return|;
block|}
DECL|field|inverse
specifier|private
specifier|transient
name|ImmutableListMultimap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
decl_stmt|;
comment|/**    * {@inheritDoc}    *    *<p>Because an inverse of a list multimap can contain multiple pairs with    * the same key and value, this method returns an {@code    * ImmutableListMultimap} rather than the {@code ImmutableMultimap} specified    * in the {@code ImmutableMultimap} class.    *    * @since 11.0    */
annotation|@
name|Override
DECL|method|inverse ()
specifier|public
name|ImmutableListMultimap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
parameter_list|()
block|{
name|ImmutableListMultimap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|result
init|=
name|inverse
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
operator|(
name|inverse
operator|=
name|invert
argument_list|()
operator|)
else|:
name|result
return|;
block|}
DECL|method|invert ()
specifier|private
name|ImmutableListMultimap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|invert
parameter_list|()
block|{
name|Builder
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|builder
init|=
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
range|:
name|entries
argument_list|()
control|)
block|{
name|builder
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ImmutableListMultimap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|invertedMultimap
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|invertedMultimap
operator|.
name|inverse
operator|=
name|this
expr_stmt|;
return|return
name|invertedMultimap
return|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the multimap unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
DECL|method|removeAll (Object key)
annotation|@
name|Deprecated
annotation|@
name|Override
specifier|public
name|ImmutableList
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
comment|/**    * Guaranteed to throw an exception and leave the multimap unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
DECL|method|replaceValues ( K key, Iterable<? extends V> values)
annotation|@
name|Deprecated
annotation|@
name|Override
specifier|public
name|ImmutableList
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
name|ImmutableList
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
name|builder
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|array
argument_list|)
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
name|ImmutableList
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
literal|"Not needed in emulated source"
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

