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
comment|/**  * An immutable {@link BiMap} with reliable user-specified iteration order. Does  * not permit null keys or values. An {@code ImmutableBiMap} and its inverse  * have the same iteration ordering.  *  *<p>An instance of {@code ImmutableBiMap} contains its own data and will  *<i>never</i> change. {@code ImmutableBiMap} is convenient for  * {@code public static final} maps ("constant maps") and also lets you easily  * make a "defensive copy" of a bimap provided to your class by a caller.  *  *<p><b>Note</b>: Although this class is not final, it cannot be subclassed as  * it has no public or protected constructors. Thus, instances of this class are  * guaranteed to be immutable.  *  * @author Jared Levy  * @since 2 (imported from Google Collections Library)  */
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
DECL|class|ImmutableBiMap
specifier|public
specifier|abstract
class|class
name|ImmutableBiMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|BiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|EMPTY_IMMUTABLE_BIMAP
specifier|private
specifier|static
specifier|final
name|ImmutableBiMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|EMPTY_IMMUTABLE_BIMAP
init|=
operator|new
name|EmptyBiMap
argument_list|()
decl_stmt|;
comment|/**    * Returns the empty bimap.    */
comment|// Casting to any type is safe because the set will never hold any elements.
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
name|ImmutableBiMap
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
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|EMPTY_IMMUTABLE_BIMAP
return|;
block|}
comment|/**    * Returns an immutable bimap containing a single entry.    */
DECL|method|of (K k1, V v1)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
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
operator|new
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|k1
argument_list|,
name|v1
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable map containing the given entries, in order.    *    * @throws IllegalArgumentException if duplicate keys or values are added    */
DECL|method|of (K k1, V v1, K k2, V v2)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
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
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|ImmutableMap
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
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable map containing the given entries, in order.    *    * @throws IllegalArgumentException if duplicate keys or values are added    */
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
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
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|ImmutableMap
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
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable map containing the given entries, in order.    *    * @throws IllegalArgumentException if duplicate keys or values are added    */
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
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
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|ImmutableMap
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
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable map containing the given entries, in order.    *    * @throws IllegalArgumentException if duplicate keys or values are added    */
DECL|method|of ( K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
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
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|ImmutableMap
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
comment|/**    * A builder for creating immutable bimap instances, especially {@code public    * static final} bimaps ("constant bimaps"). Example:<pre>   {@code    *    *   static final ImmutableBiMap<String, Integer> WORD_TO_INT =    *       new ImmutableBiMap.Builder<String, Integer>()    *           .put("one", 1)    *           .put("two", 2)    *           .put("three", 3)    *           .build();}</pre>    *    * For<i>small</i> immutable bimaps, the {@code ImmutableBiMap.of()} methods    * are even more convenient.    *    *<p>Builder instances can be reused - it is safe to call {@link #build}    * multiple times to build multiple bimaps in series. Each bimap is a superset    * of the bimaps created before it.    *    * @since 2 (imported from Google Collections Library)    */
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
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/**      * Creates a new builder. The returned builder is equivalent to the builder      * generated by {@link ImmutableBiMap#builder}.      */
DECL|method|Builder ()
specifier|public
name|Builder
parameter_list|()
block|{}
comment|/**      * Associates {@code key} with {@code value} in the built bimap. Duplicate      * keys or values are not allowed, and will cause {@link #build} to fail.      */
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
comment|/**      * Associates all of the given map's keys and values in the built bimap.      * Duplicate keys or values are not allowed, and will cause {@link #build}      * to fail.      *      * @throws NullPointerException if any key or value in {@code map} is null      */
DECL|method|putAll (Map<? extends K, ? extends V> map)
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
name|super
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Returns a newly-created immutable bimap.      *      * @throws IllegalArgumentException if duplicate keys or values were added      */
DECL|method|build ()
annotation|@
name|Override
specifier|public
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
block|{
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
init|=
name|super
operator|.
name|build
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
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
return|return
operator|new
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|map
argument_list|)
return|;
block|}
block|}
comment|/**    * Returns an immutable bimap containing the same entries as {@code map}. If    * {@code map} somehow contains entries with duplicate keys (for example, if    * it is a {@code SortedMap} whose comparator is not<i>consistent with    * equals</i>), the results of this method are undefined.    *    *<p>Despite the method name, this method attempts to avoid actually copying    * the data when it is safe to do so. The exact circumstances under which a    * copy will or will not be performed are undocumented and subject to change.    *    * @throws IllegalArgumentException if two keys have the same value    * @throws NullPointerException if any key or value in {@code map} is null    */
DECL|method|copyOf ( Map<? extends K, ? extends V> map)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
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
name|map
operator|instanceof
name|ImmutableBiMap
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// safe since map is not writable
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|bimap
init|=
operator|(
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|map
decl_stmt|;
comment|// TODO(user): if we need to make a copy of a BiMap because the
comment|// forward map is a view, don't make a copy of the non-view delegate map
if|if
condition|(
operator|!
name|bimap
operator|.
name|isPartialView
argument_list|()
condition|)
block|{
return|return
name|bimap
return|;
block|}
block|}
if|if
condition|(
name|map
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
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|immutableMap
init|=
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|map
argument_list|)
decl_stmt|;
return|return
operator|new
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|immutableMap
argument_list|)
return|;
block|}
DECL|method|ImmutableBiMap ()
name|ImmutableBiMap
parameter_list|()
block|{}
DECL|method|delegate ()
specifier|abstract
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|()
function_decl|;
comment|/**    * {@inheritDoc}    *    *<p>The inverse of an {@code ImmutableBiMap} is another    * {@code ImmutableBiMap}.    */
annotation|@
name|Override
DECL|method|inverse ()
specifier|public
specifier|abstract
name|ImmutableBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
parameter_list|()
function_decl|;
DECL|method|containsKey (@ullable Object key)
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
name|delegate
argument_list|()
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|containsValue (@ullable Object value)
annotation|@
name|Override
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
name|inverse
argument_list|()
operator|.
name|containsKey
argument_list|(
name|value
argument_list|)
return|;
block|}
DECL|method|entrySet ()
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
name|entrySet
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|entrySet
argument_list|()
return|;
block|}
DECL|method|get (@ullable Object key)
annotation|@
name|Override
specifier|public
name|V
name|get
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|keySet ()
annotation|@
name|Override
specifier|public
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|keySet
argument_list|()
return|;
block|}
comment|/**    * Returns an immutable set of the values in this map. The values are in the    * same order as the parameters used to build this map.    */
DECL|method|values ()
annotation|@
name|Override
specifier|public
name|ImmutableSet
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
return|return
name|inverse
argument_list|()
operator|.
name|keySet
argument_list|()
return|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the bimap unmodified.    *    * @throws UnsupportedOperationException always    */
annotation|@
name|Override
DECL|method|forcePut (K key, V value)
specifier|public
name|V
name|forcePut
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
DECL|method|isEmpty ()
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|isEmpty
argument_list|()
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
name|delegate
argument_list|()
operator|.
name|size
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
name|object
operator|==
name|this
operator|||
name|delegate
argument_list|()
operator|.
name|equals
argument_list|(
name|object
argument_list|)
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
name|delegate
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
name|delegate
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/** Bimap with no mappings. */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// uses writeReplace(), not default serialization
DECL|class|EmptyBiMap
specifier|static
class|class
name|EmptyBiMap
extends|extends
name|ImmutableBiMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
block|{
DECL|method|delegate ()
annotation|@
name|Override
name|ImmutableMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|ImmutableMap
operator|.
name|of
argument_list|()
return|;
block|}
DECL|method|inverse ()
annotation|@
name|Override
specifier|public
name|ImmutableBiMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|inverse
parameter_list|()
block|{
return|return
name|this
return|;
block|}
DECL|method|isPartialView ()
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
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|EMPTY_IMMUTABLE_BIMAP
return|;
comment|// preserve singleton property
block|}
block|}
comment|/**    * Serialized type for all ImmutableBiMap instances. It captures the logical    * contents and they are reconstructed using public factory methods. This    * ensures that the implementation types remain as implementation details.    *    * Since the bimap is immutable, ImmutableBiMap doesn't require special logic    * for keeping the bimap and its inverse in sync during serialization, the way    * AbstractBiMap does.    */
DECL|class|SerializedForm
specifier|private
specifier|static
class|class
name|SerializedForm
extends|extends
name|ImmutableMap
operator|.
name|SerializedForm
block|{
DECL|method|SerializedForm (ImmutableBiMap<?, ?> bimap)
name|SerializedForm
parameter_list|(
name|ImmutableBiMap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|bimap
parameter_list|)
block|{
name|super
argument_list|(
name|bimap
argument_list|)
expr_stmt|;
block|}
DECL|method|readResolve ()
annotation|@
name|Override
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
annotation|@
name|Override
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

