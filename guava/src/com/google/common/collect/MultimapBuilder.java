begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Supplier
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
name|EnumMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
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
name|HashSet
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
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_comment
comment|/**  * A builder for a multimap implementation that allows customization of the backing map and value  * collection implementations used in a particular multimap.  *  *<p>This can be used to easily configure multimap data structure implementations not provided  * explicitly in {@code com.google.common.collect}, for example:  *  *<pre>   {@code  *   ListMultimap<String, Integer> treeListMultimap =  *       MultimapBuilder.treeKeys().arrayListValues().build();  *   SetMultimap<Integer, MyEnum> hashEnumMultimap =  *       MultimapBuilder.hashKeys().enumSetValues(MyEnum.class).build();}</pre>  *  *<p>{@code MultimapBuilder} instances are immutable.  Invoking a configuration method has no  * effect on the receiving instance; you must store and use the new builder instance it returns  * instead.  *  *<p>The generated multimaps are serializable if the key and value types are serializable,  * unless stated otherwise in one of the configuration methods.  *  * @author Louis Wasserman  * @param<K0> An upper bound on the key type of the generated multimap.  * @param<V0> An upper bound on the value type of the generated multimap.  * @since 16.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|MultimapBuilder
specifier|public
specifier|abstract
class|class
name|MultimapBuilder
parameter_list|<
name|K0
parameter_list|,
name|V0
parameter_list|>
block|{
comment|/*    * Leaving K and V as upper bounds rather than the actual key and value types allows type    * parameters to be left implicit more often. CacheBuilder uses the same technique.    */
DECL|method|MultimapBuilder ()
specifier|private
name|MultimapBuilder
parameter_list|()
block|{}
DECL|field|DEFAULT_EXPECTED_KEYS
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_EXPECTED_KEYS
init|=
literal|8
decl_stmt|;
comment|/**    * Uses a {@link HashMap} to map keys to value collections.    */
DECL|method|hashKeys ()
specifier|public
specifier|static
name|MultimapBuilderWithKeys
argument_list|<
name|Object
argument_list|>
name|hashKeys
parameter_list|()
block|{
return|return
name|hashKeys
argument_list|(
name|DEFAULT_EXPECTED_KEYS
argument_list|)
return|;
block|}
comment|/**    * Uses a {@link HashMap} to map keys to value collections, initialized to expect the specified    * number of keys.    *    * @throws IllegalArgumentException if {@code expectedKeys< 0}    */
DECL|method|hashKeys (final int expectedKeys)
specifier|public
specifier|static
name|MultimapBuilderWithKeys
argument_list|<
name|Object
argument_list|>
name|hashKeys
parameter_list|(
specifier|final
name|int
name|expectedKeys
parameter_list|)
block|{
name|checkNonnegative
argument_list|(
name|expectedKeys
argument_list|,
literal|"expectedKeys"
argument_list|)
expr_stmt|;
return|return
operator|new
name|MultimapBuilderWithKeys
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|createMap
parameter_list|()
block|{
return|return
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
argument_list|(
name|expectedKeys
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**    * Uses a {@link LinkedHashMap} to map keys to value collections.    *    *<p>The collections returned by {@link Multimap#keySet()}, {@link Multimap#keys()}, and    * {@link Multimap#asMap()} will iterate through the keys in the order that they were first added    * to the multimap, save that if all values associated with a key are removed and then the key is    * added back into the multimap, that key will come last in the key iteration order.    */
DECL|method|linkedHashKeys ()
specifier|public
specifier|static
name|MultimapBuilderWithKeys
argument_list|<
name|Object
argument_list|>
name|linkedHashKeys
parameter_list|()
block|{
return|return
name|linkedHashKeys
argument_list|(
name|DEFAULT_EXPECTED_KEYS
argument_list|)
return|;
block|}
comment|/**    * Uses a {@link LinkedHashMap} to map keys to value collections, initialized to expect the    * specified number of keys.    *    *<p>The collections returned by {@link Multimap#keySet()}, {@link Multimap#keys()}, and    * {@link Multimap#asMap()} will iterate through the keys in the order that they were first added    * to the multimap, save that if all values associated with a key are removed and then the key is    * added back into the multimap, that key will come last in the key iteration order.    */
DECL|method|linkedHashKeys (final int expectedKeys)
specifier|public
specifier|static
name|MultimapBuilderWithKeys
argument_list|<
name|Object
argument_list|>
name|linkedHashKeys
parameter_list|(
specifier|final
name|int
name|expectedKeys
parameter_list|)
block|{
name|checkNonnegative
argument_list|(
name|expectedKeys
argument_list|,
literal|"expectedKeys"
argument_list|)
expr_stmt|;
return|return
operator|new
name|MultimapBuilderWithKeys
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|createMap
parameter_list|()
block|{
return|return
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
argument_list|(
name|expectedKeys
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**    * Uses a naturally-ordered {@link TreeMap} to map keys to value collections.    *    *<p>The collections returned by {@link Multimap#keySet()}, {@link Multimap#keys()}, and    * {@link Multimap#asMap()} will iterate through the keys in sorted order.    *    *<p>For all multimaps generated by the resulting builder, the {@link Multimap#keySet()} can be    * safely cast to a {@link java.util.SortedSet}, and the {@link Multimap#asMap()} can safely be    * cast to a {@link java.util.SortedMap}.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|treeKeys ()
specifier|public
specifier|static
name|MultimapBuilderWithKeys
argument_list|<
name|Comparable
argument_list|>
name|treeKeys
parameter_list|()
block|{
return|return
name|treeKeys
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Uses a {@link TreeMap} sorted by the specified comparator to map keys to value collections.    *    *<p>The collections returned by {@link Multimap#keySet()}, {@link Multimap#keys()}, and    * {@link Multimap#asMap()} will iterate through the keys in sorted order.    *    *<p>For all multimaps generated by the resulting builder, the {@link Multimap#keySet()} can be    * safely cast to a {@link java.util.SortedSet}, and the {@link Multimap#asMap()} can safely be    * cast to a {@link java.util.SortedMap}.    *    *<p>Multimaps generated by the resulting builder will not be serializable if {@code comparator}    * is not serializable.    */
DECL|method|treeKeys (final Comparator<K0> comparator)
specifier|public
specifier|static
parameter_list|<
name|K0
parameter_list|>
name|MultimapBuilderWithKeys
argument_list|<
name|K0
argument_list|>
name|treeKeys
parameter_list|(
specifier|final
name|Comparator
argument_list|<
name|K0
argument_list|>
name|comparator
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
return|return
operator|new
name|MultimapBuilderWithKeys
argument_list|<
name|K0
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
argument_list|<
name|K
extends|extends
name|K0
argument_list|,
name|V
argument_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|createMap
parameter_list|()
block|{
return|return
operator|new
name|TreeMap
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|(
name|comparator
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**    * Uses an {@link EnumMap} to map keys to value collections.    */
DECL|method|enumKeys ( final Class<K0> keyClass)
specifier|public
specifier|static
parameter_list|<
name|K0
extends|extends
name|Enum
argument_list|<
name|K0
argument_list|>
parameter_list|>
name|MultimapBuilderWithKeys
argument_list|<
name|K0
argument_list|>
name|enumKeys
parameter_list|(
specifier|final
name|Class
argument_list|<
name|K0
argument_list|>
name|keyClass
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|keyClass
argument_list|)
expr_stmt|;
return|return
operator|new
name|MultimapBuilderWithKeys
argument_list|<
name|K0
argument_list|>
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
argument_list|<
name|K
extends|extends
name|K0
argument_list|,
name|V
argument_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|createMap
parameter_list|()
block|{
comment|// K must actually be K0, since enums are effectively final
comment|// (their subclasses are inaccessible)
return|return
operator|(
name|Map
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
operator|)
operator|new
name|EnumMap
argument_list|<
name|K0
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|(
name|keyClass
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|class|ArrayListSupplier
specifier|private
specifier|static
specifier|final
class|class
name|ArrayListSupplier
parameter_list|<
name|V
parameter_list|>
implements|implements
name|Supplier
argument_list|<
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|expectedValuesPerKey
specifier|private
specifier|final
name|int
name|expectedValuesPerKey
decl_stmt|;
DECL|method|ArrayListSupplier (int expectedValuesPerKey)
name|ArrayListSupplier
parameter_list|(
name|int
name|expectedValuesPerKey
parameter_list|)
block|{
name|this
operator|.
name|expectedValuesPerKey
operator|=
name|checkNonnegative
argument_list|(
name|expectedValuesPerKey
argument_list|,
literal|"expectedValuesPerKey"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|List
argument_list|<
name|V
argument_list|>
name|get
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
block|}
DECL|enum|LinkedListSupplier
specifier|private
enum|enum
name|LinkedListSupplier
implements|implements
name|Supplier
argument_list|<
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
DECL|method|instance ()
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|Supplier
argument_list|<
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
name|instance
parameter_list|()
block|{
comment|// Each call generates a fresh LinkedList, which can serve as a List<V> for any V.
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
name|Supplier
argument_list|<
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
name|result
init|=
operator|(
name|Supplier
operator|)
name|INSTANCE
decl_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|get
parameter_list|()
block|{
return|return
operator|new
name|LinkedList
argument_list|<
name|Object
argument_list|>
argument_list|()
return|;
block|}
block|}
DECL|class|HashSetSupplier
specifier|private
specifier|static
specifier|final
class|class
name|HashSetSupplier
parameter_list|<
name|V
parameter_list|>
implements|implements
name|Supplier
argument_list|<
name|Set
argument_list|<
name|V
argument_list|>
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|expectedValuesPerKey
specifier|private
specifier|final
name|int
name|expectedValuesPerKey
decl_stmt|;
DECL|method|HashSetSupplier (int expectedValuesPerKey)
name|HashSetSupplier
parameter_list|(
name|int
name|expectedValuesPerKey
parameter_list|)
block|{
name|this
operator|.
name|expectedValuesPerKey
operator|=
name|checkNonnegative
argument_list|(
name|expectedValuesPerKey
argument_list|,
literal|"expectedValuesPerKey"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|Set
argument_list|<
name|V
argument_list|>
name|get
parameter_list|()
block|{
return|return
operator|new
name|HashSet
argument_list|<
name|V
argument_list|>
argument_list|(
name|expectedValuesPerKey
argument_list|)
return|;
block|}
block|}
DECL|class|LinkedHashSetSupplier
specifier|private
specifier|static
specifier|final
class|class
name|LinkedHashSetSupplier
parameter_list|<
name|V
parameter_list|>
implements|implements
name|Supplier
argument_list|<
name|Set
argument_list|<
name|V
argument_list|>
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|expectedValuesPerKey
specifier|private
specifier|final
name|int
name|expectedValuesPerKey
decl_stmt|;
DECL|method|LinkedHashSetSupplier (int expectedValuesPerKey)
name|LinkedHashSetSupplier
parameter_list|(
name|int
name|expectedValuesPerKey
parameter_list|)
block|{
name|this
operator|.
name|expectedValuesPerKey
operator|=
name|checkNonnegative
argument_list|(
name|expectedValuesPerKey
argument_list|,
literal|"expectedValuesPerKey"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|Set
argument_list|<
name|V
argument_list|>
name|get
parameter_list|()
block|{
return|return
operator|new
name|LinkedHashSet
argument_list|<
name|V
argument_list|>
argument_list|(
name|expectedValuesPerKey
argument_list|)
return|;
block|}
block|}
DECL|class|TreeSetSupplier
specifier|private
specifier|static
specifier|final
class|class
name|TreeSetSupplier
parameter_list|<
name|V
parameter_list|>
implements|implements
name|Supplier
argument_list|<
name|SortedSet
argument_list|<
name|V
argument_list|>
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|comparator
specifier|private
specifier|final
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
name|comparator
decl_stmt|;
DECL|method|TreeSetSupplier (Comparator<? super V> comparator)
name|TreeSetSupplier
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
name|comparator
parameter_list|)
block|{
name|this
operator|.
name|comparator
operator|=
name|checkNotNull
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|SortedSet
argument_list|<
name|V
argument_list|>
name|get
parameter_list|()
block|{
return|return
operator|new
name|TreeSet
argument_list|<
name|V
argument_list|>
argument_list|(
name|comparator
argument_list|)
return|;
block|}
block|}
DECL|class|EnumSetSupplier
specifier|private
specifier|static
specifier|final
class|class
name|EnumSetSupplier
parameter_list|<
name|V
extends|extends
name|Enum
parameter_list|<
name|V
parameter_list|>
parameter_list|>
implements|implements
name|Supplier
argument_list|<
name|Set
argument_list|<
name|V
argument_list|>
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|clazz
specifier|private
specifier|final
name|Class
argument_list|<
name|V
argument_list|>
name|clazz
decl_stmt|;
DECL|method|EnumSetSupplier (Class<V> clazz)
name|EnumSetSupplier
parameter_list|(
name|Class
argument_list|<
name|V
argument_list|>
name|clazz
parameter_list|)
block|{
name|this
operator|.
name|clazz
operator|=
name|checkNotNull
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|Set
argument_list|<
name|V
argument_list|>
name|get
parameter_list|()
block|{
return|return
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|clazz
argument_list|)
return|;
block|}
block|}
comment|/**    * An intermediate stage in a {@link MultimapBuilder} in which the key-value collection map    * implementation has been specified, but the value collection implementation has not.    *    * @param<K0> The upper bound on the key type of the generated multimap.    */
DECL|class|MultimapBuilderWithKeys
specifier|public
specifier|abstract
specifier|static
class|class
name|MultimapBuilderWithKeys
parameter_list|<
name|K0
parameter_list|>
block|{
DECL|field|DEFAULT_EXPECTED_VALUES_PER_KEY
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_EXPECTED_VALUES_PER_KEY
init|=
literal|2
decl_stmt|;
DECL|method|MultimapBuilderWithKeys ()
name|MultimapBuilderWithKeys
parameter_list|()
block|{}
DECL|method|createMap ()
specifier|abstract
parameter_list|<
name|K
extends|extends
name|K0
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|createMap
parameter_list|()
function_decl|;
comment|/**      * Uses an {@link ArrayList} to store value collections.      */
DECL|method|arrayListValues ()
specifier|public
name|ListMultimapBuilder
argument_list|<
name|K0
argument_list|,
name|Object
argument_list|>
name|arrayListValues
parameter_list|()
block|{
return|return
name|arrayListValues
argument_list|(
name|DEFAULT_EXPECTED_VALUES_PER_KEY
argument_list|)
return|;
block|}
comment|/**      * Uses an {@link ArrayList} to store value collections, initialized to expect the specified      * number of values per key.      *      * @throws IllegalArgumentException if {@code expectedValuesPerKey< 0}      */
DECL|method|arrayListValues (final int expectedValuesPerKey)
specifier|public
name|ListMultimapBuilder
argument_list|<
name|K0
argument_list|,
name|Object
argument_list|>
name|arrayListValues
parameter_list|(
specifier|final
name|int
name|expectedValuesPerKey
parameter_list|)
block|{
return|return
operator|new
name|ListMultimapBuilder
argument_list|<
name|K0
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|K0
parameter_list|,
name|V
parameter_list|>
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
block|{
return|return
name|Multimaps
operator|.
name|newListMultimap
argument_list|(
name|MultimapBuilderWithKeys
operator|.
name|this
operator|.
expr|<
name|K
argument_list|,
name|V
operator|>
name|createMap
argument_list|()
argument_list|,
operator|new
name|ArrayListSupplier
argument_list|<
name|V
argument_list|>
argument_list|(
name|expectedValuesPerKey
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**      * Uses a {@link LinkedList} to store value collections.      */
DECL|method|linkedListValues ()
specifier|public
name|ListMultimapBuilder
argument_list|<
name|K0
argument_list|,
name|Object
argument_list|>
name|linkedListValues
parameter_list|()
block|{
return|return
operator|new
name|ListMultimapBuilder
argument_list|<
name|K0
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|K0
parameter_list|,
name|V
parameter_list|>
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
block|{
return|return
name|Multimaps
operator|.
name|newListMultimap
argument_list|(
name|MultimapBuilderWithKeys
operator|.
name|this
operator|.
expr|<
name|K
argument_list|,
name|V
operator|>
name|createMap
argument_list|()
argument_list|,
name|LinkedListSupplier
operator|.
expr|<
name|V
operator|>
name|instance
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**      * Uses a {@link HashSet} to store value collections.      */
DECL|method|hashSetValues ()
specifier|public
name|SetMultimapBuilder
argument_list|<
name|K0
argument_list|,
name|Object
argument_list|>
name|hashSetValues
parameter_list|()
block|{
return|return
name|hashSetValues
argument_list|(
name|DEFAULT_EXPECTED_VALUES_PER_KEY
argument_list|)
return|;
block|}
comment|/**      * Uses a {@link HashSet} to store value collections, initialized to expect the specified number      * of values per key.      *      * @throws IllegalArgumentException if {@code expectedValuesPerKey< 0}      */
DECL|method|hashSetValues (final int expectedValuesPerKey)
specifier|public
name|SetMultimapBuilder
argument_list|<
name|K0
argument_list|,
name|Object
argument_list|>
name|hashSetValues
parameter_list|(
specifier|final
name|int
name|expectedValuesPerKey
parameter_list|)
block|{
name|checkNonnegative
argument_list|(
name|expectedValuesPerKey
argument_list|,
literal|"expectedValuesPerKey"
argument_list|)
expr_stmt|;
return|return
operator|new
name|SetMultimapBuilder
argument_list|<
name|K0
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|K0
parameter_list|,
name|V
parameter_list|>
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
block|{
return|return
name|Multimaps
operator|.
name|newSetMultimap
argument_list|(
name|MultimapBuilderWithKeys
operator|.
name|this
operator|.
expr|<
name|K
argument_list|,
name|V
operator|>
name|createMap
argument_list|()
argument_list|,
operator|new
name|HashSetSupplier
argument_list|<
name|V
argument_list|>
argument_list|(
name|expectedValuesPerKey
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**      * Uses a {@link LinkedHashSet} to store value collections.      */
DECL|method|linkedHashSetValues ()
specifier|public
name|SetMultimapBuilder
argument_list|<
name|K0
argument_list|,
name|Object
argument_list|>
name|linkedHashSetValues
parameter_list|()
block|{
return|return
name|linkedHashSetValues
argument_list|(
name|DEFAULT_EXPECTED_VALUES_PER_KEY
argument_list|)
return|;
block|}
comment|/**      * Uses a {@link LinkedHashSet} to store value collections, initialized to expect the specified      * number of values per key.      *      * @throws IllegalArgumentException if {@code expectedValuesPerKey< 0}      */
DECL|method|linkedHashSetValues (final int expectedValuesPerKey)
specifier|public
name|SetMultimapBuilder
argument_list|<
name|K0
argument_list|,
name|Object
argument_list|>
name|linkedHashSetValues
parameter_list|(
specifier|final
name|int
name|expectedValuesPerKey
parameter_list|)
block|{
name|checkNonnegative
argument_list|(
name|expectedValuesPerKey
argument_list|,
literal|"expectedValuesPerKey"
argument_list|)
expr_stmt|;
return|return
operator|new
name|SetMultimapBuilder
argument_list|<
name|K0
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|K0
parameter_list|,
name|V
parameter_list|>
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
block|{
return|return
name|Multimaps
operator|.
name|newSetMultimap
argument_list|(
name|MultimapBuilderWithKeys
operator|.
name|this
operator|.
expr|<
name|K
argument_list|,
name|V
operator|>
name|createMap
argument_list|()
argument_list|,
operator|new
name|LinkedHashSetSupplier
argument_list|<
name|V
argument_list|>
argument_list|(
name|expectedValuesPerKey
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**      * Uses a naturally-ordered {@link TreeSet} to store value collections.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|treeSetValues ()
specifier|public
name|SortedSetMultimapBuilder
argument_list|<
name|K0
argument_list|,
name|Comparable
argument_list|>
name|treeSetValues
parameter_list|()
block|{
return|return
name|treeSetValues
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses a {@link TreeSet} ordered by the specified comparator to store value collections.      *      *<p>Multimaps generated by the resulting builder will not be serializable if      * {@code comparator} is not serializable.      */
DECL|method|treeSetValues (final Comparator<V0> comparator)
specifier|public
parameter_list|<
name|V0
parameter_list|>
name|SortedSetMultimapBuilder
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
name|treeSetValues
parameter_list|(
specifier|final
name|Comparator
argument_list|<
name|V0
argument_list|>
name|comparator
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|comparator
argument_list|,
literal|"comparator"
argument_list|)
expr_stmt|;
return|return
operator|new
name|SortedSetMultimapBuilder
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|K0
parameter_list|,
name|V
extends|extends
name|V0
parameter_list|>
name|SortedSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
block|{
return|return
name|Multimaps
operator|.
name|newSortedSetMultimap
argument_list|(
name|MultimapBuilderWithKeys
operator|.
name|this
operator|.
expr|<
name|K
argument_list|,
name|V
operator|>
name|createMap
argument_list|()
argument_list|,
operator|new
name|TreeSetSupplier
argument_list|<
name|V
argument_list|>
argument_list|(
name|comparator
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**      * Uses an {@link EnumSet} to store value collections.      */
DECL|method|enumSetValues ( final Class<V0> valueClass)
specifier|public
parameter_list|<
name|V0
extends|extends
name|Enum
argument_list|<
name|V0
argument_list|>
parameter_list|>
name|SetMultimapBuilder
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
name|enumSetValues
parameter_list|(
specifier|final
name|Class
argument_list|<
name|V0
argument_list|>
name|valueClass
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|valueClass
argument_list|,
literal|"valueClass"
argument_list|)
expr_stmt|;
return|return
operator|new
name|SetMultimapBuilder
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|K
extends|extends
name|K0
parameter_list|,
name|V
extends|extends
name|V0
parameter_list|>
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
block|{
comment|// V must actually be V0, since enums are effectively final
comment|// (their subclasses are inaccessible)
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
name|Supplier
argument_list|<
name|Set
argument_list|<
name|V
argument_list|>
argument_list|>
name|factory
init|=
operator|(
name|Supplier
operator|)
operator|new
name|EnumSetSupplier
argument_list|<
name|V0
argument_list|>
argument_list|(
name|valueClass
argument_list|)
decl_stmt|;
return|return
name|Multimaps
operator|.
name|newSetMultimap
argument_list|(
name|MultimapBuilderWithKeys
operator|.
name|this
operator|.
expr|<
name|K
argument_list|,
name|V
operator|>
name|createMap
argument_list|()
argument_list|,
name|factory
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
comment|/**    * Returns a new, empty {@code Multimap} with the specified implementation.    */
DECL|method|build ()
specifier|public
specifier|abstract
parameter_list|<
name|K
extends|extends
name|K0
parameter_list|,
name|V
extends|extends
name|V0
parameter_list|>
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
function_decl|;
comment|/**    * Returns a {@code Multimap} with the specified implementation, initialized with the entries of    * {@code multimap}.    */
DECL|method|build ( Multimap<? extends K, ? extends V> multimap)
specifier|public
parameter_list|<
name|K
extends|extends
name|K0
parameter_list|,
name|V
extends|extends
name|V0
parameter_list|>
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
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
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|result
init|=
name|build
argument_list|()
decl_stmt|;
name|result
operator|.
name|putAll
argument_list|(
name|multimap
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**    * A specialization of {@link MultimapBuilder} that generates {@link ListMultimap} instances.    */
DECL|class|ListMultimapBuilder
specifier|public
specifier|abstract
specifier|static
class|class
name|ListMultimapBuilder
parameter_list|<
name|K0
parameter_list|,
name|V0
parameter_list|>
extends|extends
name|MultimapBuilder
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
block|{
DECL|method|ListMultimapBuilder ()
name|ListMultimapBuilder
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|build ()
specifier|public
specifier|abstract
parameter_list|<
name|K
extends|extends
name|K0
parameter_list|,
name|V
extends|extends
name|V0
parameter_list|>
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|build ( Multimap<? extends K, ? extends V> multimap)
specifier|public
parameter_list|<
name|K
extends|extends
name|K0
parameter_list|,
name|V
extends|extends
name|V0
parameter_list|>
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
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
operator|(
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|super
operator|.
name|build
argument_list|(
name|multimap
argument_list|)
return|;
block|}
block|}
comment|/**    * A specialization of {@link MultimapBuilder} that generates {@link SetMultimap} instances.    */
DECL|class|SetMultimapBuilder
specifier|public
specifier|abstract
specifier|static
class|class
name|SetMultimapBuilder
parameter_list|<
name|K0
parameter_list|,
name|V0
parameter_list|>
extends|extends
name|MultimapBuilder
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
block|{
DECL|method|SetMultimapBuilder ()
name|SetMultimapBuilder
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|build ()
specifier|public
specifier|abstract
parameter_list|<
name|K
extends|extends
name|K0
parameter_list|,
name|V
extends|extends
name|V0
parameter_list|>
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|build ( Multimap<? extends K, ? extends V> multimap)
specifier|public
parameter_list|<
name|K
extends|extends
name|K0
parameter_list|,
name|V
extends|extends
name|V0
parameter_list|>
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
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
operator|(
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|super
operator|.
name|build
argument_list|(
name|multimap
argument_list|)
return|;
block|}
block|}
comment|/**    * A specialization of {@link MultimapBuilder} that generates {@link SortedSetMultimap} instances.    */
DECL|class|SortedSetMultimapBuilder
specifier|public
specifier|abstract
specifier|static
class|class
name|SortedSetMultimapBuilder
parameter_list|<
name|K0
parameter_list|,
name|V0
parameter_list|>
extends|extends
name|SetMultimapBuilder
argument_list|<
name|K0
argument_list|,
name|V0
argument_list|>
block|{
DECL|method|SortedSetMultimapBuilder ()
name|SortedSetMultimapBuilder
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|build ()
specifier|public
specifier|abstract
parameter_list|<
name|K
extends|extends
name|K0
parameter_list|,
name|V
extends|extends
name|V0
parameter_list|>
name|SortedSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|build ( Multimap<? extends K, ? extends V> multimap)
specifier|public
parameter_list|<
name|K
extends|extends
name|K0
parameter_list|,
name|V
extends|extends
name|V0
parameter_list|>
name|SortedSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
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
operator|(
name|SortedSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|super
operator|.
name|build
argument_list|(
name|multimap
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

