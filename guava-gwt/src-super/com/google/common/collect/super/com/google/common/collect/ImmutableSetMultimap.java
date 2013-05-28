begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Objects
operator|.
name|firstNonNull
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
name|Collections
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
comment|/**  * An immutable {@link SetMultimap} with reliable user-specified key and value  * iteration order. Does not permit null keys or values.  *  *<p>Unlike {@link Multimaps#unmodifiableSetMultimap(SetMultimap)}, which is  * a<i>view</i> of a separate multimap which can still change, an instance of  * {@code ImmutableSetMultimap} contains its own data and will<i>never</i>  * change. {@code ImmutableSetMultimap} is convenient for  * {@code public static final} multimaps ("constant multimaps") and also lets  * you easily make a "defensive copy" of a multimap provided to your class by  * a caller.  *  *<p><b>Note:</b> Although this class is not final, it cannot be subclassed as  * it has no public or protected constructors. Thus, instances of this class  * are guaranteed to be immutable.  *  *<p>See the Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/ImmutableCollectionsExplained">  * immutable collections</a>.  *  * @author Mike Ward  * @since 2.0 (imported from Google Collections Library)  */
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
name|AbstractMapBasedMultimap
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
comment|/**    * A builder for creating immutable {@code SetMultimap} instances, especially    * {@code public static final} multimaps ("constant multimaps"). Example:    *<pre>   {@code    *    *   static final Multimap<String, Integer> STRING_TO_INTEGER_MULTIMAP =    *       new ImmutableSetMultimap.Builder<String, Integer>()    *           .put("one", 1)    *           .putAll("several", 1, 2, 3)    *           .putAll("many", 1, 2, 3, 4, 5)    *           .build();}</pre>    *    * Builder instances can be reused; it is safe to call {@link #build} multiple    * times to build multiple multimaps in series. Each multimap contains the    * key-value mappings in the previously created multimaps.    *    * @since 2.0 (imported from Google Collections Library)    */
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
comment|/**      * Creates a new builder. The returned builder is equivalent to the builder      * generated by {@link ImmutableSetMultimap#builder}.      */
DECL|method|Builder ()
specifier|public
name|Builder
parameter_list|()
block|{
name|builderMultimap
operator|=
operator|new
name|BuilderMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
expr_stmt|;
block|}
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
comment|/**      * Adds an entry to the built multimap if it is not already present.      *      * @since 11.0      */
DECL|method|put (Entry<? extends K, ? extends V> entry)
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
name|builderMultimap
operator|.
name|put
argument_list|(
name|checkNotNull
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
name|checkNotNull
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
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
name|this
operator|.
name|keyComparator
operator|=
name|checkNotNull
argument_list|(
name|keyComparator
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Specifies the ordering of the generated multimap's values for each key.      *      *<p>If this method is called, the sets returned by the {@code get()}      * method of the generated multimap and its {@link Multimap#asMap()} view      * are {@link ImmutableSortedSet} instances. However, serialization does not      * preserve that property, though it does maintain the key and value      * ordering.      *      * @since 8.0      */
comment|// TODO: Make serialization behavior consistent.
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
if|if
condition|(
name|keyComparator
operator|!=
literal|null
condition|)
block|{
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|sortedCopy
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
name|List
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
name|entries
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|builderMultimap
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|entries
argument_list|,
name|Ordering
operator|.
name|from
argument_list|(
name|keyComparator
argument_list|)
operator|.
operator|<
name|K
operator|>
name|onKeys
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|sortedCopy
operator|.
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
name|builderMultimap
operator|=
name|sortedCopy
expr_stmt|;
block|}
return|return
name|copyOf
argument_list|(
name|builderMultimap
argument_list|,
name|valueComparator
argument_list|)
return|;
block|}
block|}
comment|/**    * Returns an immutable set multimap containing the same mappings as    * {@code multimap}. The generated multimap's key and value orderings    * correspond to the iteration ordering of the {@code multimap.asMap()} view.    * Repeated occurrences of an entry in the multimap after the first are    * ignored.    *    *<p>Despite the method name, this method attempts to avoid actually copying    * the data when it is safe to do so. The exact circumstances under which a    * copy will or will not be performed are undocumented and subject to change.    *    * @throws NullPointerException if any key or value in {@code multimap} is    *     null    */
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
return|return
name|copyOf
argument_list|(
name|multimap
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|copyOf ( Multimap<? extends K, ? extends V> multimap, Comparator<? super V> valueComparator)
specifier|private
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
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
name|valueComparator
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
operator|&&
name|valueComparator
operator|==
literal|null
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
name|valueSet
argument_list|(
name|valueComparator
argument_list|,
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
argument_list|,
name|valueComparator
argument_list|)
return|;
block|}
comment|/**    * Returned by get() when a missing key is provided. Also holds the    * comparator, if any, used for values.    */
DECL|field|emptySet
specifier|private
specifier|final
specifier|transient
name|ImmutableSet
argument_list|<
name|V
argument_list|>
name|emptySet
decl_stmt|;
DECL|method|ImmutableSetMultimap (ImmutableMap<K, ImmutableSet<V>> map, int size, @Nullable Comparator<? super V> valueComparator)
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
parameter_list|,
annotation|@
name|Nullable
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
argument_list|(
name|map
argument_list|,
name|size
argument_list|)
expr_stmt|;
name|this
operator|.
name|emptySet
operator|=
name|emptySet
argument_list|(
name|valueComparator
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
name|firstNonNull
argument_list|(
name|set
argument_list|,
name|emptySet
argument_list|)
return|;
block|}
DECL|field|inverse
specifier|private
specifier|transient
name|ImmutableSetMultimap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
decl_stmt|;
comment|/**    * {@inheritDoc}    *    *<p>Because an inverse of a set multimap cannot contain multiple pairs with    * the same key and value, this method returns an {@code ImmutableSetMultimap}    * rather than the {@code ImmutableMultimap} specified in the {@code    * ImmutableMultimap} class.    *    * @since 11.0    */
DECL|method|inverse ()
specifier|public
name|ImmutableSetMultimap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
parameter_list|()
block|{
name|ImmutableSetMultimap
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
name|ImmutableSetMultimap
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
name|ImmutableSetMultimap
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
comment|/**    * Guaranteed to throw an exception and leave the multimap unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
DECL|method|replaceValues ( K key, Iterable<? extends V> values)
annotation|@
name|Deprecated
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
DECL|method|entries ()
annotation|@
name|Override
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
name|entries
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
name|EntrySet
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
DECL|class|EntrySet
specifier|private
specifier|static
specifier|final
class|class
name|EntrySet
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
block|{
DECL|field|multimap
specifier|private
specifier|transient
specifier|final
name|ImmutableSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|multimap
decl_stmt|;
DECL|method|EntrySet (ImmutableSetMultimap<K, V> multimap)
name|EntrySet
parameter_list|(
name|ImmutableSetMultimap
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
annotation|@
name|Override
DECL|method|contains (@ullable Object object)
specifier|public
name|boolean
name|contains
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
name|Entry
condition|)
block|{
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|entry
init|=
operator|(
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
annotation|@
name|Override
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
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|UnmodifiableIterator
argument_list|<
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
return|return
name|multimap
operator|.
name|entryIterator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|valueSet ( @ullable Comparator<? super V> valueComparator, Collection<? extends V> values)
specifier|private
specifier|static
parameter_list|<
name|V
parameter_list|>
name|ImmutableSet
argument_list|<
name|V
argument_list|>
name|valueSet
parameter_list|(
annotation|@
name|Nullable
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
name|valueComparator
parameter_list|,
name|Collection
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|values
parameter_list|)
block|{
return|return
operator|(
name|valueComparator
operator|==
literal|null
operator|)
condition|?
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|values
argument_list|)
else|:
name|ImmutableSortedSet
operator|.
name|copyOf
argument_list|(
name|valueComparator
argument_list|,
name|values
argument_list|)
return|;
block|}
DECL|method|emptySet ( @ullable Comparator<? super V> valueComparator)
specifier|private
specifier|static
parameter_list|<
name|V
parameter_list|>
name|ImmutableSet
argument_list|<
name|V
argument_list|>
name|emptySet
parameter_list|(
annotation|@
name|Nullable
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
name|valueComparator
parameter_list|)
block|{
return|return
operator|(
name|valueComparator
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
name|ImmutableSortedSet
operator|.
expr|<
name|V
operator|>
name|emptySet
argument_list|(
name|valueComparator
argument_list|)
return|;
block|}
DECL|method|valueComparator ()
annotation|@
name|Nullable
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
name|valueComparator
parameter_list|()
block|{
return|return
name|emptySet
operator|instanceof
name|ImmutableSortedSet
condition|?
operator|(
operator|(
name|ImmutableSortedSet
argument_list|<
name|V
argument_list|>
operator|)
name|emptySet
operator|)
operator|.
name|comparator
argument_list|()
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

