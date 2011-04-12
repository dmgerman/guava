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
name|Set
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
comment|/**  * A collection similar to a {@code Map}, but which may associate multiple  * values with a single key. If you call {@link #put} twice, with the same key  * but different values, the multimap contains mappings from the key to both  * values.  *  *<p>The methods {@link #get}, {@link #keySet}, {@link #keys}, {@link #values},  * {@link #entries}, and {@link #asMap} return collections that are views of the  * multimap. If the multimap is modifiable, updating it can change the contents  * of those collections, and updating the collections will change the multimap.  * In contrast, {@link #replaceValues} and {@link #removeAll} return collections  * that are independent of subsequent multimap changes.  *  *<p>Depending on the implementation, a multimap may or may not allow duplicate  * key-value pairs. In other words, the multimap contents after adding the same  * key and value twice varies between implementations. In multimaps allowing  * duplicates, the multimap will contain two mappings, and {@code get} will  * return a collection that includes the value twice. In multimaps not  * supporting duplicates, the multimap will contain a single mapping from the  * key to the value, and {@code get} will return a collection that includes the  * value once.  *  *<p>All methods that alter the multimap are optional, and the views returned  * by the multimap may or may not be modifiable. When modification isn't  * supported, those methods will throw an {@link UnsupportedOperationException}.  *  * @author Jared Levy  * @param<K> the type of keys maintained by this multimap  * @param<V> the type of mapped values  * @since 2 (imported from Google Collections Library)  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|Multimap
specifier|public
interface|interface
name|Multimap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
block|{
comment|// Query Operations
comment|/** Returns the number of key-value pairs in the multimap. */
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
comment|/** Returns {@code true} if the multimap contains no key-value pairs. */
DECL|method|isEmpty ()
name|boolean
name|isEmpty
parameter_list|()
function_decl|;
comment|/**    * Returns {@code true} if the multimap contains any values for the specified    * key.    *    * @param key key to search for in multimap    */
DECL|method|containsKey (@ullable Object key)
name|boolean
name|containsKey
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
function_decl|;
comment|/**    * Returns {@code true} if the multimap contains the specified value for any    * key.    *    * @param value value to search for in multimap    */
DECL|method|containsValue (@ullable Object value)
name|boolean
name|containsValue
parameter_list|(
annotation|@
name|Nullable
name|Object
name|value
parameter_list|)
function_decl|;
comment|/**    * Returns {@code true} if the multimap contains the specified key-value pair.    *    * @param key key to search for in multimap    * @param value value to search for in multimap    */
DECL|method|containsEntry (@ullable Object key, @Nullable Object value)
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
function_decl|;
comment|// Modification Operations
comment|/**    * Stores a key-value pair in the multimap.    *    *<p>Some multimap implementations allow duplicate key-value pairs, in which    * case {@code put} always adds a new key-value pair and increases the    * multimap size by 1. Other implementations prohibit duplicates, and storing    * a key-value pair that's already in the multimap has no effect.    *    * @param key key to store in the multimap    * @param value value to store in the multimap    * @return {@code true} if the method increased the size of the multimap, or    *     {@code false} if the multimap already contained the key-value pair and    *     doesn't allow duplicates    */
DECL|method|put (@ullable K key, @Nullable V value)
name|boolean
name|put
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|,
annotation|@
name|Nullable
name|V
name|value
parameter_list|)
function_decl|;
comment|/**    * Removes a key-value pair from the multimap.    *    * @param key key of entry to remove from the multimap    * @param value value of entry to remove the multimap    * @return {@code true} if the multimap changed    */
DECL|method|remove (@ullable Object key, @Nullable Object value)
name|boolean
name|remove
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
function_decl|;
comment|// Bulk Operations
comment|/**    * Stores a collection of values with the same key.    *    * @param key key to store in the multimap    * @param values values to store in the multimap    * @return {@code true} if the multimap changed    */
DECL|method|putAll (@ullable K key, Iterable<? extends V> values)
name|boolean
name|putAll
parameter_list|(
annotation|@
name|Nullable
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
function_decl|;
comment|/**    * Copies all of another multimap's key-value pairs into this multimap. The    * order in which the mappings are added is determined by    * {@code multimap.entries()}.    *    * @param multimap mappings to store in this multimap    * @return {@code true} if the multimap changed    */
DECL|method|putAll (Multimap<? extends K, ? extends V> multimap)
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
function_decl|;
comment|/**    * Stores a collection of values with the same key, replacing any existing    * values for that key.    *    * @param key key to store in the multimap    * @param values values to store in the multimap    * @return the collection of replaced values, or an empty collection if no    *     values were previously associated with the key. The collection    *<i>may</i> be modifiable, but updating it will have no effect on the    *     multimap.    */
DECL|method|replaceValues (@ullable K key, Iterable<? extends V> values)
name|Collection
argument_list|<
name|V
argument_list|>
name|replaceValues
parameter_list|(
annotation|@
name|Nullable
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
function_decl|;
comment|/**    * Removes all values associated with a given key.    *    * @param key key of entries to remove from the multimap    * @return the collection of removed values, or an empty collection if no    *     values were associated with the provided key. The collection    *<i>may</i> be modifiable, but updating it will have no effect on the    *     multimap.    */
DECL|method|removeAll (@ullable Object key)
name|Collection
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
function_decl|;
comment|/**    * Removes all key-value pairs from the multimap.    */
DECL|method|clear ()
name|void
name|clear
parameter_list|()
function_decl|;
comment|// Views
comment|/**    * Returns a collection view of all values associated with a key. If no    * mappings in the multimap have the provided key, an empty collection is    * returned.    *    *<p>Changes to the returned collection will update the underlying multimap,    * and vice versa.    *    * @param key key to search for in multimap    * @return the collection of values that the key maps to    */
DECL|method|get (@ullable K key)
name|Collection
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
function_decl|;
comment|/**    * Returns the set of all keys, each appearing once in the returned set.    * Changes to the returned set will update the underlying multimap, and vice    * versa.    *    * @return the collection of distinct keys    */
DECL|method|keySet ()
name|Set
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
function_decl|;
comment|/**    * Returns a collection, which may contain duplicates, of all keys. The number    * of times of key appears in the returned multiset equals the number of    * mappings the key has in the multimap. Changes to the returned multiset will    * update the underlying multimap, and vice versa.    *    * @return a multiset with keys corresponding to the distinct keys of the    *     multimap and frequencies corresponding to the number of values that    *     each key maps to    */
DECL|method|keys ()
name|Multiset
argument_list|<
name|K
argument_list|>
name|keys
parameter_list|()
function_decl|;
comment|/**    * Returns a collection of all values in the multimap. Changes to the returned    * collection will update the underlying multimap, and vice versa.    *    * @return collection of values, which may include the same value multiple    *     times if it occurs in multiple mappings    */
DECL|method|values ()
name|Collection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
function_decl|;
comment|/**    * Returns a collection of all key-value pairs. Changes to the returned    * collection will update the underlying multimap, and vice versa. The entries    * collection does not support the {@code add} or {@code addAll} operations.    *    * @return collection of map entries consisting of key-value pairs    */
DECL|method|entries ()
name|Collection
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
function_decl|;
comment|/**    * Returns a map view that associates each key with the corresponding values    * in the multimap. Changes to the returned map, such as element removal, will    * update the underlying multimap. The map does not support {@code setValue()}    * on its entries, {@code put}, or {@code putAll}.    *    *<p>When passed a key that is present in the map, {@code    * asMap().get(Object)} has the same behavior as {@link #get}, returning a    * live collection. When passed a key that is not present, however, {@code    * asMap().get(Object)} returns {@code null} instead of an empty collection.    *    * @return a map view from a key to its collection of values    */
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
parameter_list|()
function_decl|;
comment|// Comparison and hashing
comment|/**    * Compares the specified object with this multimap for equality. Two    * multimaps are equal when their map views, as returned by {@link #asMap},    * are also equal.    *    *<p>In general, two multimaps with identical key-value mappings may or may    * not be equal, depending on the implementation. For example, two    * {@link SetMultimap} instances with the same key-value mappings are equal,    * but equality of two {@link ListMultimap} instances depends on the ordering    * of the values for each key.    *    *<p>A non-empty {@link SetMultimap} cannot be equal to a non-empty    * {@link ListMultimap}, since their {@link #asMap} views contain unequal    * collections as values. However, any two empty multimaps are equal, because    * they both have empty {@link #asMap} views.    */
annotation|@
name|Override
DECL|method|equals (@ullable Object obj)
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
function_decl|;
comment|/**    * Returns the hash code for this multimap.    *    *<p>The hash code of a multimap is defined as the hash code of the map view,    * as returned by {@link Multimap#asMap}.    */
annotation|@
name|Override
DECL|method|hashCode ()
name|int
name|hashCode
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

