begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|SortedSet
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
comment|/**  * Basic implementation of the {@link SortedSetMultimap} interface. It's a  * wrapper around {@link AbstractMultimap} that converts the returned  * collections into sorted sets. The {@link #createCollection} method  * must return a {@code SortedSet}.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AbstractSortedSetMultimap
specifier|abstract
class|class
name|AbstractSortedSetMultimap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|SortedSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/**    * Creates a new multimap that uses the provided map.    *    * @param map place to store the mapping from each key to its corresponding    *     values    */
DECL|method|AbstractSortedSetMultimap (Map<K, Collection<V>> map)
specifier|protected
name|AbstractSortedSetMultimap
parameter_list|(
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
parameter_list|)
block|{
name|super
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
DECL|method|createCollection ()
annotation|@
name|Override
specifier|abstract
name|SortedSet
argument_list|<
name|V
argument_list|>
name|createCollection
parameter_list|()
function_decl|;
comment|// Following Javadoc copied from Multimap and SortedSetMultimap.
comment|/**    * Returns a collection view of all values associated with a key. If no    * mappings in the multimap have the provided key, an empty collection is    * returned.    *    *<p>Changes to the returned collection will update the underlying multimap,    * and vice versa.    *    *<p>Because a {@code SortedSetMultimap} has unique sorted values for a given    * key, this method returns a {@link SortedSet}, instead of the    * {@link Collection} specified in the {@link Multimap} interface.    */
DECL|method|get (@ullable K key)
annotation|@
name|Override
specifier|public
name|SortedSet
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
return|return
operator|(
name|SortedSet
argument_list|<
name|V
argument_list|>
operator|)
name|super
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**    * Removes all values associated with a given key. The returned collection is    * immutable.    *    *<p>Because a {@code SortedSetMultimap} has unique sorted values for a given    * key, this method returns a {@link SortedSet}, instead of the    * {@link Collection} specified in the {@link Multimap} interface.    */
DECL|method|removeAll (@ullable Object key)
annotation|@
name|Override
specifier|public
name|SortedSet
argument_list|<
name|V
argument_list|>
name|removeAll
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
block|{
return|return
operator|(
name|SortedSet
argument_list|<
name|V
argument_list|>
operator|)
name|super
operator|.
name|removeAll
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**    * Stores a collection of values with the same key, replacing any existing    * values for that key. The returned collection is immutable.    *    *<p>Because a {@code SortedSetMultimap} has unique sorted values for a given    * key, this method returns a {@link SortedSet}, instead of the    * {@link Collection} specified in the {@link Multimap} interface.    *    *<p>Any duplicates in {@code values} will be stored in the multimap once.    */
DECL|method|replaceValues ( K key, Iterable<? extends V> values)
annotation|@
name|Override
specifier|public
name|SortedSet
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
return|return
operator|(
name|SortedSet
argument_list|<
name|V
argument_list|>
operator|)
name|super
operator|.
name|replaceValues
argument_list|(
name|key
argument_list|,
name|values
argument_list|)
return|;
block|}
comment|/**    * Returns a map view that associates each key with the corresponding values    * in the multimap. Changes to the returned map, such as element removal, will    * update the underlying multimap. The map does not support {@code setValue()}    * on its entries, {@code put}, or {@code putAll}.    *    *<p>When passed a key that is present in the map, {@code    * asMap().get(Object)} has the same behavior as {@link #get}, returning a    * live collection. When passed a key that is not present, however, {@code    * asMap().get(Object)} returns {@code null} instead of an empty collection.    *    *<p>Though the method signature doesn't say so explicitly, the returned map    * has {@link SortedSet} values.    */
DECL|method|asMap ()
annotation|@
name|Override
specifier|public
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
parameter_list|()
block|{
return|return
name|super
operator|.
name|asMap
argument_list|()
return|;
block|}
comment|/**    * {@inheritDoc}    *    * Consequently, the values do not follow their natural ordering or the    * ordering of the value comparator.    */
DECL|method|values ()
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
return|return
name|super
operator|.
name|values
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
literal|430848587173315748L
decl_stmt|;
block|}
end_class

end_unit

