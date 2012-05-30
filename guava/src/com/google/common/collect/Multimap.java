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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A collection that maps keys to values, similar to {@link Map}, but in which  * each key may be associated with<i>multiple</i> values. You can visualize the  * contents of a multimap either as a map from keys to collections of values:  *  *<ul>  *<li>a â 1, 2  *<li>b â 3  *</ul>  *  * ... or as a single "flattened" collection of key-value pairs:  *  *<ul>  *<li>a â 1  *<li>a â 2  *<li>b â 3  *</ul>  *  *<p><b>Important:</b> although the first interpretation resembles how most  * multimaps are<i>implemented</i>, the design of the {@code Multimap} API is  * based on the<i>second</i> form. So, using the multimap shown above as an  * example, the {@link #size} is {@code 3}, not {@code 2}, and the {@link  * #values} collection is {@code [1, 2, 3]}, not {@code [[1, 2], [3]]}. For  * those times when the first style is more useful, use the multimap's {@link  * #asMap} view.  *  *<h3>Example</h3>  *  *<p>The following code:<pre>   {@code  *  *   ListMultimap<String, String> multimap = ArrayListMultimap.create();  *   for (President pres : US_PRESIDENTS_IN_ORDER) {  *     multimap.put(pres.firstName(), pres.lastName());  *   }  *   for (String firstName : multimap.keySet()) {  *     List<String> lastNames = multimap.get(firstName);  *     out.println(firstName + ": " + lastNames);  *   }}</pre>  *  * ... produces output such as:<pre>   {@code  *  *   Zachary: [Taylor]  *   John: [Adams, Adams, Tyler, Kennedy]  *   George: [Washington, Bush, Bush]  *   Grover: [Cleveland]  *   ...}</pre>  *  *<h3>Views</h3>  *  *<p>Much of the power of the multimap API comes from the<i>view  * collections</i> it provides. These always reflect the latest state of the  * multimap itself. When they support modification, the changes are  *<i>write-through</i> (they automatically update the backing multimap). These  * view collections are:  *  *<ul>  *<li>{@link #asMap}, mentioned above</li>  *<li>{@link #keys}, {@link #keySet}, {@link #values}, {@link #entries}, which  *     are similar to the corresponding view collections of {@link Map}  *<li>and, notably, even the collection returned by {@link #get get(key)} is an  *     active view of the values corresponding to {@code key}  *</ul>  *  *<p>The collections returned by the {@link #replaceValues replaceValues} and  * {@link #removeAll removeAll} methods, which contain values that have just  * been removed from the multimap, are naturally<i>not</i> views.  *  *<h3>Subinterfaces</h3>  *  *<p>Instead of using the {@code Multimap} interface directly, prefer the  * subinterfaces {@link ListMultimap} and {@link SetMultimap}. These take their  * names from the fact that the collections they return from {@code get} behave  * like (and, of course, implement) {@link List} and {@link Set}, respectively.  *  *<p>For example, the "presidents" code snippet above used a {@code  * ListMultimap}; if it had used a {@code SetMultimap} instead, two presidents  * would have vanished, and last names might or might not appear in  * chronological order.  *  *<h3>Uses</h3>  *  *<p>Multimaps are commonly used anywhere a {@code Map<K, Collection<V>>} would  * otherwise have appeared. The advantages include:  *  *<ul>  *<li>There is no need to populate an empty collection before adding an entry  *     with {@link #put put}.  *<li>{@code get} never returns {@code null}, only an empty collection.  *<li>It will not retain empty collections after the last value for a key is  *     removed. As a result, {@link #containsKey} behaves logically, and the  *     multimap won't leak memory.  *<li>The total entry count is available as {@link #size}.  *<li>Many complex operations become easier; for example, {@code  *     Collections.min(multimap.values())} finds the smallest value across all  *     keys.  *</ul>  *  *<h3>Implementations</h3>  *  *<p>As always, prefer the immutable implementations, {@link  * ImmutableListMultimap} and {@link ImmutableSetMultimap}. General-purpose  * mutable implementations are listed above under "All Known Implementing  * Classes". You can also create a<i>custom</i> multimap, backed by any {@code  * Map} and {@link Collection} types, using the {@link Multimaps#newMultimap  * Multimaps.newMultimap} family of methods. Finally, another popular way to  * obtain a multimap is using {@link Multimaps#index Multimaps.index}. See  * the {@link Multimaps} class for these and other static utilities related  * to multimaps.  *  *<h3>Other Notes</h3>  *  *<p>All methods that modify the multimap are optional. The view collections  * returned by the multimap may or may not be modifiable. Any modification  * method that is not supported will throw {@link  * UnsupportedOperationException}.  *  *<p>See the Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#Multimap">  * {@code Multimap}</a>.  *  * @author Jared Levy  * @since 2.0 (imported from Google Collections Library)  */
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
comment|/**    * Removes a single key-value pair from the multimap.    *    * @param key key of entry to remove from the multimap    * @param value value of entry to remove the multimap    * @return {@code true} if the multimap changed    */
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

