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
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
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
name|CompatibleWith
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
name|Map
operator|.
name|Entry
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
name|function
operator|.
name|BiConsumer
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
comment|/**  * A collection that maps keys to values, similar to {@link Map}, but in which each key may be  * associated with<i>multiple</i> values. You can visualize the contents of a multimap either as a  * map from keys to<i>nonempty</i> collections of values:  *  *<ul>  *<li>a â 1, 2  *<li>b â 3  *</ul>  *  * ... or as a single "flattened" collection of key-value pairs:  *  *<ul>  *<li>a â 1  *<li>a â 2  *<li>b â 3  *</ul>  *  *<p><b>Important:</b> although the first interpretation resembles how most multimaps are  *<i>implemented</i>, the design of the {@code Multimap} API is based on the<i>second</i> form.  * So, using the multimap shown above as an example, the {@link #size} is {@code 3}, not {@code 2},  * and the {@link #values} collection is {@code [1, 2, 3]}, not {@code [[1, 2], [3]]}. For those  * times when the first style is more useful, use the multimap's {@link #asMap} view (or create a  * {@code Map<K, Collection<V>>} in the first place).  *  *<h3>Example</h3>  *  *<p>The following code:  *  *<pre>{@code  * ListMultimap<String, String> multimap = ArrayListMultimap.create();  * for (President pres : US_PRESIDENTS_IN_ORDER) {  *   multimap.put(pres.firstName(), pres.lastName());  * }  * for (String firstName : multimap.keySet()) {  *   List<String> lastNames = multimap.get(firstName);  *   out.println(firstName + ": " + lastNames);  * }  * }</pre>  *  * ... produces output such as:  *  *<pre>{@code  * Zachary: [Taylor]  * John: [Adams, Adams, Tyler, Kennedy]  // Remember, Quincy!  * George: [Washington, Bush, Bush]  * Grover: [Cleveland, Cleveland]        // Two, non-consecutive terms, rep'ing NJ!  * ...  * }</pre>  *  *<h3>Views</h3>  *  *<p>Much of the power of the multimap API comes from the<i>view collections</i> it provides.  * These always reflect the latest state of the multimap itself. When they support modification, the  * changes are<i>write-through</i> (they automatically update the backing multimap). These view  * collections are:  *  *<ul>  *<li>{@link #asMap}, mentioned above  *<li>{@link #keys}, {@link #keySet}, {@link #values}, {@link #entries}, which are similar to the  *       corresponding view collections of {@link Map}  *<li>and, notably, even the collection returned by {@link #get get(key)} is an active view of  *       the values corresponding to {@code key}  *</ul>  *  *<p>The collections returned by the {@link #replaceValues replaceValues} and {@link #removeAll  * removeAll} methods, which contain values that have just been removed from the multimap, are  * naturally<i>not</i> views.  *  *<h3>Subinterfaces</h3>  *  *<p>Instead of using the {@code Multimap} interface directly, prefer the subinterfaces {@link  * ListMultimap} and {@link SetMultimap}. These take their names from the fact that the collections  * they return from {@code get} behave like (and, of course, implement) {@link List} and {@link  * Set}, respectively.  *  *<p>For example, the "presidents" code snippet above used a {@code ListMultimap}; if it had used a  * {@code SetMultimap} instead, two presidents would have vanished, and last names might or might  * not appear in chronological order.  *  *<p><b>Warning:</b> instances of type {@code Multimap} may not implement {@link Object#equals} in  * the way you expect. Multimaps containing the same key-value pairs, even in the same order, may or  * may not be equal and may or may not have the same {@code hashCode}. The recommended subinterfaces  * provide much stronger guarantees.  *  *<h3>Comparison to a map of collections</h3>  *  *<p>Multimaps are commonly used in places where a {@code Map<K, Collection<V>>} would otherwise  * have appeared. The differences include:  *  *<ul>  *<li>There is no need to populate an empty collection before adding an entry with {@link #put  *       put}.  *<li>{@code get} never returns {@code null}, only an empty collection.  *<li>A key is contained in the multimap if and only if it maps to at least one value. Any  *       operation that causes a key to have zero associated values has the effect of  *<i>removing</i> that key from the multimap.  *<li>The total entry count is available as {@link #size}.  *<li>Many complex operations become easier; for example, {@code  *       Collections.min(multimap.values())} finds the smallest value across all keys.  *</ul>  *  *<h3>Implementations</h3>  *  *<p>As always, prefer the immutable implementations, {@link ImmutableListMultimap} and {@link  * ImmutableSetMultimap}. General-purpose mutable implementations are listed above under "All Known  * Implementing Classes". You can also create a<i>custom</i> multimap, backed by any {@code Map}  * and {@link Collection} types, using the {@link Multimaps#newMultimap Multimaps.newMultimap}  * family of methods. Finally, another popular way to obtain a multimap is using {@link  * Multimaps#index Multimaps.index}. See the {@link Multimaps} class for these and other static  * utilities related to multimaps.  *  *<h3>Other Notes</h3>  *  *<p>As with {@code Map}, the behavior of a {@code Multimap} is not specified if key objects  * already present in the multimap change in a manner that affects {@code equals} comparisons. Use  * caution if mutable objects are used as keys in a {@code Multimap}.  *  *<p>All methods that modify the multimap are optional. The view collections returned by the  * multimap may or may not be modifiable. Any modification method that is not supported will throw  * {@link UnsupportedOperationException}.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#multimap"> {@code  * Multimap}</a>.  *  * @author Jared Levy  * @since 2.0  */
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
comment|/**    * Returns the number of key-value pairs in this multimap.    *    *<p><b>Note:</b> this method does not return the number of<i>distinct keys</i> in the multimap,    * which is given by {@code keySet().size()} or {@code asMap().size()}. See the opening section of    * the {@link Multimap} class documentation for clarification.    */
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
comment|/**    * Returns {@code true} if this multimap contains no key-value pairs. Equivalent to {@code size()    * == 0}, but can in some cases be more efficient.    */
DECL|method|isEmpty ()
name|boolean
name|isEmpty
parameter_list|()
function_decl|;
comment|/**    * Returns {@code true} if this multimap contains at least one key-value pair with the key {@code    * key}.    */
DECL|method|containsKey (@ompatibleWithR) @ullable Object key)
name|boolean
name|containsKey
parameter_list|(
annotation|@
name|CompatibleWith
argument_list|(
literal|"K"
argument_list|)
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
function_decl|;
comment|/**    * Returns {@code true} if this multimap contains at least one key-value pair with the value    * {@code value}.    */
DECL|method|containsValue (@ompatibleWithR) @ullable Object value)
name|boolean
name|containsValue
parameter_list|(
annotation|@
name|CompatibleWith
argument_list|(
literal|"V"
argument_list|)
annotation|@
name|Nullable
name|Object
name|value
parameter_list|)
function_decl|;
comment|/**    * Returns {@code true} if this multimap contains at least one key-value pair with the key {@code    * key} and the value {@code value}.    */
DECL|method|containsEntry ( @ompatibleWithR) @ullable Object key, @CompatibleWith(R) @Nullable Object value)
name|boolean
name|containsEntry
parameter_list|(
annotation|@
name|CompatibleWith
argument_list|(
literal|"K"
argument_list|)
annotation|@
name|Nullable
name|Object
name|key
parameter_list|,
annotation|@
name|CompatibleWith
argument_list|(
literal|"V"
argument_list|)
annotation|@
name|Nullable
name|Object
name|value
parameter_list|)
function_decl|;
comment|// Modification Operations
comment|/**    * Stores a key-value pair in this multimap.    *    *<p>Some multimap implementations allow duplicate key-value pairs, in which case {@code put}    * always adds a new key-value pair and increases the multimap size by 1. Other implementations    * prohibit duplicates, and storing a key-value pair that's already in the multimap has no effect.    *    * @return {@code true} if the method increased the size of the multimap, or {@code false} if the    *     multimap already contained the key-value pair and doesn't allow duplicates    */
annotation|@
name|CanIgnoreReturnValue
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
comment|/**    * Removes a single key-value pair with the key {@code key} and the value {@code value} from this    * multimap, if such exists. If multiple key-value pairs in the multimap fit this description,    * which one is removed is unspecified.    *    * @return {@code true} if the multimap changed    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|remove ( @ompatibleWithR) @ullable Object key, @CompatibleWith(R) @Nullable Object value)
name|boolean
name|remove
parameter_list|(
annotation|@
name|CompatibleWith
argument_list|(
literal|"K"
argument_list|)
annotation|@
name|Nullable
name|Object
name|key
parameter_list|,
annotation|@
name|CompatibleWith
argument_list|(
literal|"V"
argument_list|)
annotation|@
name|Nullable
name|Object
name|value
parameter_list|)
function_decl|;
comment|// Bulk Operations
comment|/**    * Stores a key-value pair in this multimap for each of {@code values}, all using the same key,    * {@code key}. Equivalent to (but expected to be more efficient than):    *    *<pre>{@code    * for (V value : values) {    *   put(key, value);    * }    * }</pre>    *    *<p>In particular, this is a no-op if {@code values} is empty.    *    * @return {@code true} if the multimap changed    */
annotation|@
name|CanIgnoreReturnValue
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
comment|/**    * Stores all key-value pairs of {@code multimap} in this multimap, in the order returned by    * {@code multimap.entries()}.    *    * @return {@code true} if the multimap changed    */
annotation|@
name|CanIgnoreReturnValue
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
comment|/**    * Stores a collection of values with the same key, replacing any existing values for that key.    *    *<p>If {@code values} is empty, this is equivalent to {@link #removeAll(Object) removeAll(key)}.    *    * @return the collection of replaced values, or an empty collection if no values were previously    *     associated with the key. The collection<i>may</i> be modifiable, but updating it will have    *     no effect on the multimap.    */
annotation|@
name|CanIgnoreReturnValue
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
comment|/**    * Removes all values associated with the key {@code key}.    *    *<p>Once this method returns, {@code key} will not be mapped to any values, so it will not    * appear in {@link #keySet()}, {@link #asMap()}, or any other views.    *    * @return the values that were removed (possibly empty). The returned collection<i>may</i> be    *     modifiable, but updating it will have no effect on the multimap.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|removeAll (@ompatibleWithR) @ullable Object key)
name|Collection
argument_list|<
name|V
argument_list|>
name|removeAll
parameter_list|(
annotation|@
name|CompatibleWith
argument_list|(
literal|"K"
argument_list|)
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
function_decl|;
comment|/** Removes all key-value pairs from the multimap, leaving it {@linkplain #isEmpty empty}. */
DECL|method|clear ()
name|void
name|clear
parameter_list|()
function_decl|;
comment|// Views
comment|/**    * Returns a view collection of the values associated with {@code key} in this multimap, if any.    * Note that when {@code containsKey(key)} is false, this returns an empty collection, not {@code    * null}.    *    *<p>Changes to the returned collection will update the underlying multimap, and vice versa.    */
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
comment|/**    * Returns a view collection of all<i>distinct</i> keys contained in this multimap. Note that the    * key set contains a key if and only if this multimap maps that key to at least one value.    *    *<p>Changes to the returned set will update the underlying multimap, and vice versa. However,    *<i>adding</i> to the returned set is not possible.    */
DECL|method|keySet ()
name|Set
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
function_decl|;
comment|/**    * Returns a view collection containing the key from each key-value pair in this multimap,    *<i>without</i> collapsing duplicates. This collection has the same size as this multimap, and    * {@code keys().count(k) == get(k).size()} for all {@code k}.    *    *<p>Changes to the returned multiset will update the underlying multimap, and vice versa.    * However,<i>adding</i> to the returned collection is not possible.    */
DECL|method|keys ()
name|Multiset
argument_list|<
name|K
argument_list|>
name|keys
parameter_list|()
function_decl|;
comment|/**    * Returns a view collection containing the<i>value</i> from each key-value pair contained in    * this multimap, without collapsing duplicates (so {@code values().size() == size()}).    *    *<p>Changes to the returned collection will update the underlying multimap, and vice versa.    * However,<i>adding</i> to the returned collection is not possible.    */
DECL|method|values ()
name|Collection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
function_decl|;
comment|/**    * Returns a view collection of all key-value pairs contained in this multimap, as {@link Entry}    * instances.    *    *<p>Changes to the returned collection or the entries it contains will update the underlying    * multimap, and vice versa. However,<i>adding</i> to the returned collection is not possible.    */
DECL|method|entries ()
name|Collection
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
function_decl|;
comment|/**    * Performs the given action for all key-value pairs contained in this multimap. If an ordering is    * specified by the {@code Multimap} implementation, actions will be performed in the order of    * iteration of {@link #entries()}. Exceptions thrown by the action are relayed to the caller.    *    *<p>To loop over all keys and their associated value collections, write {@code    * Multimaps.asMap(multimap).forEach((key, valueCollection) -> action())}.    *    * @since 21.0    */
DECL|method|forEach (BiConsumer<? super K, ? super V> action)
specifier|default
name|void
name|forEach
parameter_list|(
name|BiConsumer
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
super|super
name|V
argument_list|>
name|action
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|entries
argument_list|()
operator|.
name|forEach
argument_list|(
name|entry
lambda|->
name|action
operator|.
name|accept
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
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns a view of this multimap as a {@code Map} from each distinct key to the nonempty    * collection of that key's associated values. Note that {@code this.asMap().get(k)} is equivalent    * to {@code this.get(k)} only when {@code k} is a key contained in the multimap; otherwise it    * returns {@code null} as opposed to an empty collection.    *    *<p>Changes to the returned map or the collections that serve as its values will update the    * underlying multimap, and vice versa. The map does not support {@code put} or {@code putAll},    * nor do its entries support {@link Entry#setValue setValue}.    */
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
comment|/**    * Compares the specified object with this multimap for equality. Two multimaps are equal when    * their map views, as returned by {@link #asMap}, are also equal.    *    *<p>In general, two multimaps with identical key-value mappings may or may not be equal,    * depending on the implementation. For example, two {@link SetMultimap} instances with the same    * key-value mappings are equal, but equality of two {@link ListMultimap} instances depends on the    * ordering of the values for each key.    *    *<p>A non-empty {@link SetMultimap} cannot be equal to a non-empty {@link ListMultimap}, since    * their {@link #asMap} views contain unequal collections as values. However, any two empty    * multimaps are equal, because they both have empty {@link #asMap} views.    */
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
comment|/**    * Returns the hash code for this multimap.    *    *<p>The hash code of a multimap is defined as the hash code of the map view, as returned by    * {@link Multimap#asMap}.    *    *<p>In general, two multimaps with identical key-value mappings may or may not have the same    * hash codes, depending on the implementation. For example, two {@link SetMultimap} instances    * with the same key-value mappings will have the same {@code hashCode}, but the {@code hashCode}    * of {@link ListMultimap} instances depends on the ordering of the values for each key.    */
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

