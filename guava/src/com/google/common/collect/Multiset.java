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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Spliterator
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
name|Consumer
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
name|ObjIntConsumer
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
comment|/**  * A collection that supports order-independent equality, like {@link Set}, but may have duplicate  * elements. A multiset is also sometimes called a<i>bag</i>.  *  *<p>Elements of a multiset that are equal to one another are referred to as<i>occurrences</i> of  * the same single element. The total number of occurrences of an element in a multiset is called  * the<i>count</i> of that element (the terms "frequency" and "multiplicity" are equivalent, but  * not used in this API). Since the count of an element is represented as an {@code int}, a multiset  * may never contain more than {@link Integer#MAX_VALUE} occurrences of any one element.  *  *<p>{@code Multiset} refines the specifications of several methods from {@code Collection}. It  * also defines an additional query operation, {@link #count}, which returns the count of an  * element. There are five new bulk-modification operations, for example {@link #add(Object, int)},  * to add or remove multiple occurrences of an element at once, or to set the count of an element to  * a specific value. These modification operations are optional, but implementations which support  * the standard collection operations {@link #add(Object)} or {@link #remove(Object)} are encouraged  * to implement the related methods as well. Finally, two collection views are provided: {@link  * #elementSet} contains the distinct elements of the multiset "with duplicates collapsed", and  * {@link #entrySet} is similar but contains {@link Entry Multiset.Entry} instances, each providing  * both a distinct element and the count of that element.  *  *<p>In addition to these required methods, implementations of {@code Multiset} are expected to  * provide two {@code static} creation methods: {@code create()}, returning an empty multiset, and  * {@code create(Iterable<? extends E>)}, returning a multiset containing the given initial  * elements. This is simply a refinement of {@code Collection}'s constructor recommendations,  * reflecting the new developments of Java 5.  *  *<p>As with other collection types, the modification operations are optional, and should throw  * {@link UnsupportedOperationException} when they are not implemented. Most implementations should  * support either all add operations or none of them, all removal operations or none of them, and if  * and only if all of these are supported, the {@code setCount} methods as well.  *  *<p>A multiset uses {@link Object#equals} to determine whether two instances should be considered  * "the same,"<i>unless specified otherwise</i> by the implementation.  *  *<p><b>Warning:</b> as with normal {@link Set}s, it is almost always a bad idea to modify an  * element (in a way that affects its {@link Object#equals} behavior) while it is contained in a  * multiset. Undefined behavior and bugs will result.  *  *<p>Common implementations include {@link ImmutableMultiset}, {@link HashMultiset}, and {@link  * ConcurrentHashMultiset}.  *  *<p>If your values may be zero, negative, or outside the range of an int, you may wish to use  * {@link com.google.common.util.concurrent.AtomicLongMap} instead. Note, however, that unlike  * {@code Multiset}, {@code AtomicLongMap} does not automatically remove zeros.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#multiset"> {@code  * Multiset}</a>.  *  * @author Kevin Bourrillion  * @since 2.0  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|Multiset
specifier|public
interface|interface
name|Multiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|Collection
argument_list|<
name|E
argument_list|>
block|{
comment|// Query Operations
comment|/**    * Returns the total number of all occurrences of all elements in this multiset.    *    *<p><b>Note:</b> this method does not return the number of<i>distinct elements</i> in the    * multiset, which is given by {@code entrySet().size()}.    */
annotation|@
name|Override
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
comment|/**    * Returns the number of occurrences of an element in this multiset (the<i>count</i> of the    * element). Note that for an {@link Object#equals}-based multiset, this gives the same result as    * {@link Collections#frequency} (which would presumably perform more poorly).    *    *<p><b>Note:</b> the utility method {@link Iterables#frequency} generalizes this operation; it    * correctly delegates to this method when dealing with a multiset, but it can also accept any    * other iterable type.    *    * @param element the element to count occurrences of    * @return the number of occurrences of the element in this multiset; possibly zero but never    *     negative    */
DECL|method|count (@ullable @ompatibleWithR) Object element)
name|int
name|count
parameter_list|(
annotation|@
name|Nullable
annotation|@
name|CompatibleWith
argument_list|(
literal|"E"
argument_list|)
name|Object
name|element
parameter_list|)
function_decl|;
comment|// Bulk Operations
comment|/**    * Adds a number of occurrences of an element to this multiset. Note that if {@code occurrences ==    * 1}, this method has the identical effect to {@link #add(Object)}. This method is functionally    * equivalent (except in the case of overflow) to the call {@code    * addAll(Collections.nCopies(element, occurrences))}, which would presumably perform much more    * poorly.    *    * @param element the element to add occurrences of; may be null only if explicitly allowed by the    *     implementation    * @param occurrences the number of occurrences of the element to add. May be zero, in which case    *     no change will be made.    * @return the count of the element before the operation; possibly zero    * @throws IllegalArgumentException if {@code occurrences} is negative, or if this operation would    *     result in more than {@link Integer#MAX_VALUE} occurrences of the element    * @throws NullPointerException if {@code element} is null and this implementation does not permit    *     null elements. Note that if {@code occurrences} is zero, the implementation may opt to    *     return normally.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|add (@ullable E element, int occurrences)
name|int
name|add
parameter_list|(
annotation|@
name|Nullable
name|E
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
function_decl|;
comment|/**    * Adds a single occurrence of the specified element to this multiset.    *    *<p>This method refines {@link Collection#add}, which only<i>ensures</i> the presence of the    * element, to further specify that a successful call must always increment the count of the    * element, and the overall size of the collection, by one.    *    *<p>To both add the element and obtain the previous count of that element, use {@link    * #add(Object, int) add}{@code (element, 1)} instead.    *    * @param element the element to add one occurrence of; may be null only if explicitly allowed by    *     the implementation    * @return {@code true} always, since this call is required to modify the multiset, unlike other    *     {@link Collection} types    * @throws NullPointerException if {@code element} is null and this implementation does not permit    *     null elements    * @throws IllegalArgumentException if {@link Integer#MAX_VALUE} occurrences of {@code element}    *     are already contained in this multiset    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|add (E element)
name|boolean
name|add
parameter_list|(
name|E
name|element
parameter_list|)
function_decl|;
comment|/**    * Removes a number of occurrences of the specified element from this multiset. If the multiset    * contains fewer than this number of occurrences to begin with, all occurrences will be removed.    * Note that if {@code occurrences == 1}, this is functionally equivalent to the call {@code    * remove(element)}.    *    * @param element the element to conditionally remove occurrences of    * @param occurrences the number of occurrences of the element to remove. May be zero, in which    *     case no change will be made.    * @return the count of the element before the operation; possibly zero    * @throws IllegalArgumentException if {@code occurrences} is negative    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|remove (@ullable @ompatibleWithR) Object element, int occurrences)
name|int
name|remove
parameter_list|(
annotation|@
name|Nullable
annotation|@
name|CompatibleWith
argument_list|(
literal|"E"
argument_list|)
name|Object
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
function_decl|;
comment|/**    * Removes a<i>single</i> occurrence of the specified element from this multiset, if present.    *    *<p>This method refines {@link Collection#remove} to further specify that it<b>may not</b>    * throw an exception in response to {@code element} being null or of the wrong type.    *    *<p>To both remove the element and obtain the previous count of that element, use {@link    * #remove(Object, int) remove}{@code (element, 1)} instead.    *    * @param element the element to remove one occurrence of    * @return {@code true} if an occurrence was found and removed    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|remove (@ullable Object element)
name|boolean
name|remove
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|)
function_decl|;
comment|/**    * Adds or removes the necessary occurrences of an element such that the element attains the    * desired count.    *    * @param element the element to add or remove occurrences of; may be null only if explicitly    *     allowed by the implementation    * @param count the desired count of the element in this multiset    * @return the count of the element before the operation; possibly zero    * @throws IllegalArgumentException if {@code count} is negative    * @throws NullPointerException if {@code element} is null and this implementation does not permit    *     null elements. Note that if {@code count} is zero, the implementor may optionally return    *     zero instead.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|setCount (E element, int count)
name|int
name|setCount
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
function_decl|;
comment|/**    * Conditionally sets the count of an element to a new value, as described in {@link    * #setCount(Object, int)}, provided that the element has the expected current count. If the    * current count is not {@code oldCount}, no change is made.    *    * @param element the element to conditionally set the count of; may be null only if explicitly    *     allowed by the implementation    * @param oldCount the expected present count of the element in this multiset    * @param newCount the desired count of the element in this multiset    * @return {@code true} if the condition for modification was met. This implies that the multiset    *     was indeed modified, unless {@code oldCount == newCount}.    * @throws IllegalArgumentException if {@code oldCount} or {@code newCount} is negative    * @throws NullPointerException if {@code element} is null and the implementation does not permit    *     null elements. Note that if {@code oldCount} and {@code newCount} are both zero, the    *     implementor may optionally return {@code true} instead.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|setCount (E element, int oldCount, int newCount)
name|boolean
name|setCount
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|oldCount
parameter_list|,
name|int
name|newCount
parameter_list|)
function_decl|;
comment|// Views
comment|/**    * Returns the set of distinct elements contained in this multiset. The element set is backed by    * the same data as the multiset, so any change to either is immediately reflected in the other.    * The order of the elements in the element set is unspecified.    *    *<p>If the element set supports any removal operations, these necessarily cause<b>all</b>    * occurrences of the removed element(s) to be removed from the multiset. Implementations are not    * expected to support the add operations, although this is possible.    *    *<p>A common use for the element set is to find the number of distinct elements in the multiset:    * {@code elementSet().size()}.    *    * @return a view of the set of distinct elements in this multiset    */
DECL|method|elementSet ()
name|Set
argument_list|<
name|E
argument_list|>
name|elementSet
parameter_list|()
function_decl|;
comment|/**    * Returns a view of the contents of this multiset, grouped into {@code Multiset.Entry} instances,    * each providing an element of the multiset and the count of that element. This set contains    * exactly one entry for each distinct element in the multiset (thus it always has the same size    * as the {@link #elementSet}). The order of the elements in the element set is unspecified.    *    *<p>The entry set is backed by the same data as the multiset, so any change to either is    * immediately reflected in the other. However, multiset changes may or may not be reflected in    * any {@code Entry} instances already retrieved from the entry set (this is    * implementation-dependent). Furthermore, implementations are not required to support    * modifications to the entry set at all, and the {@code Entry} instances themselves don't even    * have methods for modification. See the specific implementation class for more details on how    * its entry set handles modifications.    *    * @return a set of entries representing the data of this multiset    */
DECL|method|entrySet ()
name|Set
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
function_decl|;
comment|/**    * An unmodifiable element-count pair for a multiset. The {@link Multiset#entrySet} method returns    * a view of the multiset whose elements are of this class. A multiset implementation may return    * Entry instances that are either live "read-through" views to the Multiset, or immutable    * snapshots. Note that this type is unrelated to the similarly-named type {@code Map.Entry}.    *    * @since 2.0    */
DECL|interface|Entry
interface|interface
name|Entry
parameter_list|<
name|E
parameter_list|>
block|{
comment|/**      * Returns the multiset element corresponding to this entry. Multiple calls to this method      * always return the same instance.      *      * @return the element corresponding to this entry      */
DECL|method|getElement ()
name|E
name|getElement
parameter_list|()
function_decl|;
comment|/**      * Returns the count of the associated element in the underlying multiset. This count may either      * be an unchanging snapshot of the count at the time the entry was retrieved, or a live view of      * the current count of the element in the multiset, depending on the implementation. Note that      * in the former case, this method can never return zero, while in the latter, it will return      * zero if all occurrences of the element were since removed from the multiset.      *      * @return the count of the element; never negative      */
DECL|method|getCount ()
name|int
name|getCount
parameter_list|()
function_decl|;
comment|/**      * {@inheritDoc}      *      *<p>Returns {@code true} if the given object is also a multiset entry and the two entries      * represent the same element and count. That is, two entries {@code a} and {@code b} are equal      * if:      *      *<pre>{@code      * Objects.equal(a.getElement(), b.getElement())      *&& a.getCount() == b.getCount()      * }</pre>      */
annotation|@
name|Override
comment|// TODO(kevinb): check this wrt TreeMultiset?
DECL|method|equals (Object o)
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
comment|/**      * {@inheritDoc}      *      *<p>The hash code of a multiset entry for element {@code element} and count {@code count} is      * defined as:      *      *<pre>{@code      * ((element == null) ? 0 : element.hashCode()) ^ count      * }</pre>      */
annotation|@
name|Override
DECL|method|hashCode ()
name|int
name|hashCode
parameter_list|()
function_decl|;
comment|/**      * Returns the canonical string representation of this entry, defined as follows. If the count      * for this entry is one, this is simply the string representation of the corresponding element.      * Otherwise, it is the string representation of the element, followed by the three characters      * {@code " x "} (space, letter x, space), followed by the count.      */
annotation|@
name|Override
DECL|method|toString ()
name|String
name|toString
parameter_list|()
function_decl|;
block|}
comment|/**    * Runs the specified action for each distinct element in this multiset, and the number of    * occurrences of that element. For some {@code Multiset} implementations, this may be more    * efficient than iterating over the {@link #entrySet()} either explicitly or with {@code    * entrySet().forEach(action)}.    *    * @since 21.0    */
annotation|@
name|Beta
DECL|method|forEachEntry (ObjIntConsumer<? super E> action)
specifier|default
name|void
name|forEachEntry
parameter_list|(
name|ObjIntConsumer
argument_list|<
name|?
super|super
name|E
argument_list|>
name|action
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|entrySet
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
name|getElement
argument_list|()
argument_list|,
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Comparison and hashing
comment|/**    * Compares the specified object with this multiset for equality. Returns {@code true} if the    * given object is also a multiset and contains equal elements with equal counts, regardless of    * order.    */
annotation|@
name|Override
comment|// TODO(kevinb): caveats about equivalence-relation?
DECL|method|equals (@ullable Object object)
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
function_decl|;
comment|/**    * Returns the hash code for this multiset. This is defined as the sum of    *    *<pre>{@code    * ((element == null) ? 0 : element.hashCode()) ^ count(element)    * }</pre>    *    *<p>over all distinct elements in the multiset. It follows that a multiset and its entry set    * always have the same hash code.    */
annotation|@
name|Override
DECL|method|hashCode ()
name|int
name|hashCode
parameter_list|()
function_decl|;
comment|/**    * {@inheritDoc}    *    *<p>It is recommended, though not mandatory, that this method return the result of invoking    * {@link #toString} on the {@link #entrySet}, yielding a result such as {@code [a x 3, c, d x 2,    * e]}.    */
annotation|@
name|Override
DECL|method|toString ()
name|String
name|toString
parameter_list|()
function_decl|;
comment|// Refined Collection Methods
comment|/**    * {@inheritDoc}    *    *<p>Elements that occur multiple times in the multiset will appear multiple times in this    * iterator, though not necessarily sequentially.    */
annotation|@
name|Override
DECL|method|iterator ()
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
function_decl|;
comment|/**    * Determines whether this multiset contains the specified element.    *    *<p>This method refines {@link Collection#contains} to further specify that it<b>may not</b>    * throw an exception in response to {@code element} being null or of the wrong type.    *    * @param element the element to check for    * @return {@code true} if this multiset contains at least one occurrence of the element    */
annotation|@
name|Override
DECL|method|contains (@ullable Object element)
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|)
function_decl|;
comment|/**    * Returns {@code true} if this multiset contains at least one occurrence of each element in the    * specified collection.    *    *<p>This method refines {@link Collection#containsAll} to further specify that it<b>may not</b>    * throw an exception in response to any of {@code elements} being null or of the wrong type.    *    *<p><b>Note:</b> this method does not take into account the occurrence count of an element in    * the two collections; it may still return {@code true} even if {@code elements} contains several    * occurrences of an element and this multiset contains only one. This is no different than any    * other collection type like {@link List}, but it may be unexpected to the user of a multiset.    *    * @param elements the collection of elements to be checked for containment in this multiset    * @return {@code true} if this multiset contains at least one occurrence of each element    *     contained in {@code elements}    * @throws NullPointerException if {@code elements} is null    */
annotation|@
name|Override
DECL|method|containsAll (Collection<?> elements)
name|boolean
name|containsAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|elements
parameter_list|)
function_decl|;
comment|/**    * {@inheritDoc}    *    *<p><b>Note:</b> This method ignores how often any element might appear in {@code c}, and only    * cares whether or not an element appears at all. If you wish to remove one occurrence in this    * multiset for every occurrence in {@code c}, see {@link Multisets#removeOccurrences(Multiset,    * Multiset)}.    *    *<p>This method refines {@link Collection#removeAll} to further specify that it<b>may not</b>    * throw an exception in response to any of {@code elements} being null or of the wrong type.    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|removeAll (Collection<?> c)
name|boolean
name|removeAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
function_decl|;
comment|/**    * {@inheritDoc}    *    *<p><b>Note:</b> This method ignores how often any element might appear in {@code c}, and only    * cares whether or not an element appears at all. If you wish to remove one occurrence in this    * multiset for every occurrence in {@code c}, see {@link Multisets#retainOccurrences(Multiset,    * Multiset)}.    *    *<p>This method refines {@link Collection#retainAll} to further specify that it<b>may not</b>    * throw an exception in response to any of {@code elements} being null or of the wrong type.    *    * @see Multisets#retainOccurrences(Multiset, Multiset)    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|retainAll (Collection<?> c)
name|boolean
name|retainAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
function_decl|;
comment|/**    * {@inheritDoc}    *    *<p>Elements that occur multiple times in the multiset will be passed to the {@code Consumer}    * correspondingly many times, though not necessarily sequentially.    */
annotation|@
name|Override
DECL|method|forEach (Consumer<? super E> action)
specifier|default
name|void
name|forEach
parameter_list|(
name|Consumer
argument_list|<
name|?
super|super
name|E
argument_list|>
name|action
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|entrySet
argument_list|()
operator|.
name|forEach
argument_list|(
name|entry
lambda|->
block|{
name|E
name|elem
init|=
name|entry
operator|.
name|getElement
argument_list|()
decl_stmt|;
name|int
name|count
init|=
name|entry
operator|.
name|getCount
argument_list|()
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
name|count
condition|;
name|i
operator|++
control|)
block|{
name|action
operator|.
name|accept
argument_list|(
name|elem
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|spliterator ()
specifier|default
name|Spliterator
argument_list|<
name|E
argument_list|>
name|spliterator
parameter_list|()
block|{
return|return
name|Multisets
operator|.
name|spliteratorImpl
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

