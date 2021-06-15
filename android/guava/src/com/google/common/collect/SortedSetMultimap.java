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
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
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
name|SortedMap
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
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
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
comment|/**  * A {@code SetMultimap} whose set of values for a given key are kept sorted; that is, they comprise  * a {@link SortedSet}. It cannot hold duplicate key-value pairs; adding a key-value pair that's  * already in the multimap has no effect. This interface does not specify the ordering of the  * multimap's keys. See the {@link Multimap} documentation for information common to all multimaps.  *  *<p>The {@link #get}, {@link #removeAll}, and {@link #replaceValues} methods each return a {@link  * SortedSet} of values, while {@link Multimap#entries()} returns a {@link Set} of map entries.  * Though the method signature doesn't say so explicitly, the map returned by {@link #asMap} has  * {@code SortedSet} values.  *  *<p><b>Warning:</b> As in all {@link SetMultimap}s, do not modify either a key<i>or a value</i>  * of a {@code SortedSetMultimap} in a way that affects its {@link Object#equals} behavior (or its  * position in the order of the values). Undefined behavior and bugs will result.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#multimap"> {@code  * Multimap}</a>.  *  * @author Jared Levy  * @since 2.0  */
end_comment

begin_annotation
annotation|@
name|GwtCompatible
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|interface|SortedSetMultimap
specifier|public
expr|interface
name|SortedSetMultimap
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
name|SetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|// Following Javadoc copied from Multimap.
comment|/**    * Returns a collection view of all values associated with a key. If no mappings in the multimap    * have the provided key, an empty collection is returned.    *    *<p>Changes to the returned collection will update the underlying multimap, and vice versa.    *    *<p>Because a {@code SortedSetMultimap} has unique sorted values for a given key, this method    * returns a {@link SortedSet}, instead of the {@link java.util.Collection} specified in the    * {@link Multimap} interface.    */
block|@
name|Override
DECL|method|get (@arametricNullness K key)
name|SortedSet
argument_list|<
name|V
argument_list|>
name|get
argument_list|(
annotation|@
name|ParametricNullness
name|K
name|key
argument_list|)
block|;
comment|/**    * Removes all values associated with a given key.    *    *<p>Because a {@code SortedSetMultimap} has unique sorted values for a given key, this method    * returns a {@link SortedSet}, instead of the {@link java.util.Collection} specified in the    * {@link Multimap} interface.    */
block|@
name|CanIgnoreReturnValue
expr|@
name|Override
DECL|method|removeAll (@heckForNull Object key)
name|SortedSet
argument_list|<
name|V
argument_list|>
name|removeAll
argument_list|(
annotation|@
name|CheckForNull
name|Object
name|key
argument_list|)
block|;
comment|/**    * Stores a collection of values with the same key, replacing any existing values for that key.    *    *<p>Because a {@code SortedSetMultimap} has unique sorted values for a given key, this method    * returns a {@link SortedSet}, instead of the {@link java.util.Collection} specified in the    * {@link Multimap} interface.    *    *<p>Any duplicates in {@code values} will be stored in the multimap once.    */
block|@
name|CanIgnoreReturnValue
expr|@
name|Override
DECL|method|replaceValues (@arametricNullness K key, Iterable<? extends V> values)
name|SortedSet
argument_list|<
name|V
argument_list|>
name|replaceValues
argument_list|(
annotation|@
name|ParametricNullness
name|K
name|key
argument_list|,
name|Iterable
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|values
argument_list|)
block|;
comment|/**    * Returns a map view that associates each key with the corresponding values in the multimap.    * Changes to the returned map, such as element removal, will update the underlying multimap. The    * map does not support {@code setValue()} on its entries, {@code put}, or {@code putAll}.    *    *<p>When passed a key that is present in the map, {@code asMap().get(Object)} has the same    * behavior as {@link #get}, returning a live collection. When passed a key that is not present,    * however, {@code asMap().get(Object)} returns {@code null} instead of an empty collection.    *    *<p><b>Note:</b> The returned map's values are guaranteed to be of type {@link SortedSet}. To    * obtain this map with the more specific generic type {@code Map<K, SortedSet<V>>}, call {@link    * Multimaps#asMap(SortedSetMultimap)} instead.<b>However</b>, the returned map<i>itself</i> is    * not necessarily a {@link SortedMap}: A {@code SortedSetMultimap} must expose the<i>values</i>    * for a given key in sorted order, but it need not expose the<i>keys</i> in sorted order.    * Individual {@code SortedSetMultimap} implementations, like those built with {@link    * MultimapBuilder#treeKeys()}, may make additional guarantees.    */
block|@
name|Override
DECL|method|asMap ()
name|Map
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|asMap
argument_list|()
block|;
comment|/**    * Returns the comparator that orders the multimap values, with {@code null} indicating that    * natural ordering is used.    */
block|@
name|CheckForNull
DECL|method|valueComparator ()
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
name|valueComparator
argument_list|()
block|; }
end_expr_stmt

end_unit

