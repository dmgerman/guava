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
name|common
operator|.
name|annotations
operator|.
name|GwtIncompatible
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
name|Function
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
name|Optional
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
name|Predicate
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
name|SortedSet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|CheckReturnValue
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
comment|/**  * {@code FluentIterable} provides a rich interface for manipulating {@code Iterable} instances in a  * chained fashion. A {@code FluentIterable} can be created from an {@code Iterable}, or from a set  * of elements. The following types of methods are provided on {@code FluentIterable}:  *<ul>  *<li>chained methods which return a new {@code FluentIterable} based in some way on the contents  * of the current one (for example {@link #transform})  *<li>conversion methods which copy the {@code FluentIterable}'s contents into a new collection or  * array (for example {@link #toList})  *<li>element extraction methods which facilitate the retrieval of certain elements (for example  * {@link #last})  *<li>query methods which answer questions about the {@code FluentIterable}'s contents (for example  * {@link #anyMatch})  *</ul>  *  *<p>Here is an example that merges the lists returned by two separate database calls, transforms  * it by invoking {@code toString()} on each element, and returns the first 10 elements as an  * {@code ImmutableList}:<pre>   {@code  *  *   FluentIterable  *       .from(database.getClientList())  *       .filter(activeInLastMonth())  *       .transform(Functions.toStringFunction())  *       .limit(10)  *       .toList();}</pre>  *  *<p>Anything which can be done using {@code FluentIterable} could be done in a different fashion  * (often with {@link Iterables}), however the use of {@code FluentIterable} makes many sets of  * operations significantly more concise.  *  * @author Marcin Mikosik  * @since 12.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|FluentIterable
specifier|public
specifier|abstract
class|class
name|FluentIterable
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Iterable
argument_list|<
name|E
argument_list|>
block|{
comment|// We store 'iterable' and use it instead of 'this' to allow Iterables to perform instanceof
comment|// checks on the _original_ iterable when FluentIterable.from is used.
DECL|field|iterable
specifier|private
specifier|final
name|Iterable
argument_list|<
name|E
argument_list|>
name|iterable
decl_stmt|;
comment|/** Constructor for use by subclasses. */
DECL|method|FluentIterable ()
specifier|protected
name|FluentIterable
parameter_list|()
block|{
name|this
operator|.
name|iterable
operator|=
name|this
expr_stmt|;
block|}
DECL|method|FluentIterable (Iterable<E> iterable)
name|FluentIterable
parameter_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|iterable
parameter_list|)
block|{
name|this
operator|.
name|iterable
operator|=
name|checkNotNull
argument_list|(
name|iterable
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns a fluent iterable that wraps {@code iterable}, or {@code iterable} itself if it    * is already a {@code FluentIterable}.    */
DECL|method|from (final Iterable<E> iterable)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|FluentIterable
argument_list|<
name|E
argument_list|>
name|from
parameter_list|(
specifier|final
name|Iterable
argument_list|<
name|E
argument_list|>
name|iterable
parameter_list|)
block|{
return|return
operator|(
name|iterable
operator|instanceof
name|FluentIterable
operator|)
condition|?
operator|(
name|FluentIterable
argument_list|<
name|E
argument_list|>
operator|)
name|iterable
else|:
operator|new
name|FluentIterable
argument_list|<
name|E
argument_list|>
argument_list|(
name|iterable
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|iterable
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
return|;
block|}
comment|/**    * Construct a fluent iterable from another fluent iterable. This is obviously never necessary,    * but is intended to help call out cases where one migration from {@code Iterable} to    * {@code FluentIterable} has obviated the need to explicitly convert to a {@code FluentIterable}.    *    * @deprecated instances of {@code FluentIterable} don't need to be converted to    *     {@code FluentIterable}    */
annotation|@
name|Deprecated
DECL|method|from (FluentIterable<E> iterable)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|FluentIterable
argument_list|<
name|E
argument_list|>
name|from
parameter_list|(
name|FluentIterable
argument_list|<
name|E
argument_list|>
name|iterable
parameter_list|)
block|{
return|return
name|checkNotNull
argument_list|(
name|iterable
argument_list|)
return|;
block|}
comment|/**    * Returns a string representation of this fluent iterable, with the format    * {@code [e1, e2, ..., en]}.    */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|Iterables
operator|.
name|toString
argument_list|(
name|iterable
argument_list|)
return|;
block|}
comment|/**    * Returns the number of elements in this fluent iterable.    */
DECL|method|size ()
specifier|public
specifier|final
name|int
name|size
parameter_list|()
block|{
return|return
name|Iterables
operator|.
name|size
argument_list|(
name|iterable
argument_list|)
return|;
block|}
comment|/**    * Returns {@code true} if this fluent iterable contains any object for which    * {@code equals(element)} is true.    */
DECL|method|contains (@ullable Object element)
specifier|public
specifier|final
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|)
block|{
return|return
name|Iterables
operator|.
name|contains
argument_list|(
name|iterable
argument_list|,
name|element
argument_list|)
return|;
block|}
comment|/**    * Returns a fluent iterable whose {@code Iterator} cycles indefinitely over the elements of    * this fluent iterable.    *    *<p>That iterator supports {@code remove()} if {@code iterable.iterator()} does. After    * {@code remove()} is called, subsequent cycles omit the removed element, which is no longer in    * this fluent iterable. The iterator's {@code hasNext()} method returns {@code true} until    * this fluent iterable is empty.    *    *<p><b>Warning:</b> Typical uses of the resulting iterator may produce an infinite loop. You    * should use an explicit {@code break} or be certain that you will eventually remove all the    * elements.    */
annotation|@
name|CheckReturnValue
DECL|method|cycle ()
specifier|public
specifier|final
name|FluentIterable
argument_list|<
name|E
argument_list|>
name|cycle
parameter_list|()
block|{
return|return
name|from
argument_list|(
name|Iterables
operator|.
name|cycle
argument_list|(
name|iterable
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns the elements from this fluent iterable that satisfy a predicate. The    * resulting fluent iterable's iterator does not support {@code remove()}.    */
annotation|@
name|CheckReturnValue
DECL|method|filter (Predicate<? super E> predicate)
specifier|public
specifier|final
name|FluentIterable
argument_list|<
name|E
argument_list|>
name|filter
parameter_list|(
name|Predicate
argument_list|<
name|?
super|super
name|E
argument_list|>
name|predicate
parameter_list|)
block|{
return|return
name|from
argument_list|(
name|Iterables
operator|.
name|filter
argument_list|(
name|iterable
argument_list|,
name|predicate
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns the elements from this fluent iterable that are instances of class {@code type}.    *    * @param type the type of elements desired    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"Class.isInstance"
argument_list|)
annotation|@
name|CheckReturnValue
DECL|method|filter (Class<T> type)
specifier|public
specifier|final
parameter_list|<
name|T
parameter_list|>
name|FluentIterable
argument_list|<
name|T
argument_list|>
name|filter
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|from
argument_list|(
name|Iterables
operator|.
name|filter
argument_list|(
name|iterable
argument_list|,
name|type
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns {@code true} if any element in this fluent iterable satisfies the predicate.    */
DECL|method|anyMatch (Predicate<? super E> predicate)
specifier|public
specifier|final
name|boolean
name|anyMatch
parameter_list|(
name|Predicate
argument_list|<
name|?
super|super
name|E
argument_list|>
name|predicate
parameter_list|)
block|{
return|return
name|Iterables
operator|.
name|any
argument_list|(
name|iterable
argument_list|,
name|predicate
argument_list|)
return|;
block|}
comment|/**    * Returns {@code true} if every element in this fluent iterable satisfies the predicate.    * If this fluent iterable is empty, {@code true} is returned.    */
DECL|method|allMatch (Predicate<? super E> predicate)
specifier|public
specifier|final
name|boolean
name|allMatch
parameter_list|(
name|Predicate
argument_list|<
name|?
super|super
name|E
argument_list|>
name|predicate
parameter_list|)
block|{
return|return
name|Iterables
operator|.
name|all
argument_list|(
name|iterable
argument_list|,
name|predicate
argument_list|)
return|;
block|}
comment|/**    * Returns an {@link Optional} containing the first element in this fluent iterable that    * satisfies the given predicate, if such an element exists.    *    *<p><b>Warning:</b> avoid using a {@code predicate} that matches {@code null}. If {@code null}    * is matched in this fluent iterable, a {@link NullPointerException} will be thrown.    */
DECL|method|firstMatch (Predicate<? super E> predicate)
specifier|public
specifier|final
name|Optional
argument_list|<
name|E
argument_list|>
name|firstMatch
parameter_list|(
name|Predicate
argument_list|<
name|?
super|super
name|E
argument_list|>
name|predicate
parameter_list|)
block|{
return|return
name|Iterables
operator|.
name|tryFind
argument_list|(
name|iterable
argument_list|,
name|predicate
argument_list|)
return|;
block|}
comment|/**    * Returns a fluent iterable that applies {@code function} to each element of this    * fluent iterable.    *    *<p>The returned fluent iterable's iterator supports {@code remove()} if this iterable's    * iterator does. After a successful {@code remove()} call, this fluent iterable no longer    * contains the corresponding element.    */
DECL|method|transform (Function<? super E, T> function)
specifier|public
specifier|final
parameter_list|<
name|T
parameter_list|>
name|FluentIterable
argument_list|<
name|T
argument_list|>
name|transform
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|E
argument_list|,
name|T
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|from
argument_list|(
name|Iterables
operator|.
name|transform
argument_list|(
name|iterable
argument_list|,
name|function
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Applies {@code function} to each element of this fluent iterable and returns    * a fluent iterable with the concatenated combination of results.  {@code function}    * returns an Iterable of results.    *    *<p>The returned fluent iterable's iterator supports {@code remove()} if this    * function-returned iterables' iterator does. After a successful {@code remove()} call,    * the returned fluent iterable no longer contains the corresponding element.    *    * @since 13.0 (required {@code Function<E, Iterable<T>>} until 14.0)    */
DECL|method|transformAndConcat ( Function<? super E, ? extends Iterable<? extends T>> function)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|FluentIterable
argument_list|<
name|T
argument_list|>
name|transformAndConcat
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|E
argument_list|,
name|?
extends|extends
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|function
parameter_list|)
block|{
return|return
name|from
argument_list|(
name|Iterables
operator|.
name|concat
argument_list|(
name|transform
argument_list|(
name|function
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns an {@link Optional} containing the first element in this fluent iterable.    * If the iterable is empty, {@code Optional.absent()} is returned.    *    * @throws NullPointerException if the first element is null; if this is a possibility, use    *     {@code iterator().next()} or {@link Iterables#getFirst} instead.    */
DECL|method|first ()
specifier|public
specifier|final
name|Optional
argument_list|<
name|E
argument_list|>
name|first
parameter_list|()
block|{
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
init|=
name|iterable
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
name|iterator
operator|.
name|hasNext
argument_list|()
condition|?
name|Optional
operator|.
name|of
argument_list|(
name|iterator
operator|.
name|next
argument_list|()
argument_list|)
else|:
name|Optional
operator|.
expr|<
name|E
operator|>
name|absent
argument_list|()
return|;
block|}
comment|/**    * Returns an {@link Optional} containing the last element in this fluent iterable.    * If the iterable is empty, {@code Optional.absent()} is returned.    *    * @throws NullPointerException if the last element is null; if this is a possibility, use    *     {@link Iterables#getLast} instead.    */
DECL|method|last ()
specifier|public
specifier|final
name|Optional
argument_list|<
name|E
argument_list|>
name|last
parameter_list|()
block|{
comment|// Iterables#getLast was inlined here so we don't have to throw/catch a NSEE
comment|// TODO(kevinb): Support a concurrently modified collection?
if|if
condition|(
name|iterable
operator|instanceof
name|List
condition|)
block|{
name|List
argument_list|<
name|E
argument_list|>
name|list
init|=
operator|(
name|List
argument_list|<
name|E
argument_list|>
operator|)
name|iterable
decl_stmt|;
if|if
condition|(
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Optional
operator|.
name|absent
argument_list|()
return|;
block|}
return|return
name|Optional
operator|.
name|of
argument_list|(
name|list
operator|.
name|get
argument_list|(
name|list
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
return|;
block|}
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
init|=
name|iterable
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
name|Optional
operator|.
name|absent
argument_list|()
return|;
block|}
comment|/*      * TODO(kevinb): consider whether this "optimization" is worthwhile. Users      * with SortedSets tend to know they are SortedSets and probably would not      * call this method.      */
if|if
condition|(
name|iterable
operator|instanceof
name|SortedSet
condition|)
block|{
name|SortedSet
argument_list|<
name|E
argument_list|>
name|sortedSet
init|=
operator|(
name|SortedSet
argument_list|<
name|E
argument_list|>
operator|)
name|iterable
decl_stmt|;
return|return
name|Optional
operator|.
name|of
argument_list|(
name|sortedSet
operator|.
name|last
argument_list|()
argument_list|)
return|;
block|}
while|while
condition|(
literal|true
condition|)
block|{
name|E
name|current
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|current
argument_list|)
return|;
block|}
block|}
block|}
comment|/**    * Returns a view of this fluent iterable that skips its first {@code numberToSkip}    * elements. If this fluent iterable contains fewer than {@code numberToSkip} elements,    * the returned fluent iterable skips all of its elements.    *    *<p>Modifications to this fluent iterable before a call to {@code iterator()} are    * reflected in the returned fluent iterable. That is, the its iterator skips the first    * {@code numberToSkip} elements that exist when the iterator is created, not when {@code skip()}    * is called.    *    *<p>The returned fluent iterable's iterator supports {@code remove()} if the    * {@code Iterator} of this fluent iterable supports it. Note that it is<i>not</i>    * possible to delete the last skipped element by immediately calling {@code remove()} on the    * returned fluent iterable's iterator, as the {@code Iterator} contract states that a call    * to {@code * remove()} before a call to {@code next()} will throw an    * {@link IllegalStateException}.    */
annotation|@
name|CheckReturnValue
DECL|method|skip (int numberToSkip)
specifier|public
specifier|final
name|FluentIterable
argument_list|<
name|E
argument_list|>
name|skip
parameter_list|(
name|int
name|numberToSkip
parameter_list|)
block|{
return|return
name|from
argument_list|(
name|Iterables
operator|.
name|skip
argument_list|(
name|iterable
argument_list|,
name|numberToSkip
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Creates a fluent iterable with the first {@code size} elements of this    * fluent iterable. If this fluent iterable does not contain that many elements,    * the returned fluent iterable will have the same behavior as this fluent iterable.    * The returned fluent iterable's iterator supports {@code remove()} if this    * fluent iterable's iterator does.    *    * @param size the maximum number of elements in the returned fluent iterable    * @throws IllegalArgumentException if {@code size} is negative    */
annotation|@
name|CheckReturnValue
DECL|method|limit (int size)
specifier|public
specifier|final
name|FluentIterable
argument_list|<
name|E
argument_list|>
name|limit
parameter_list|(
name|int
name|size
parameter_list|)
block|{
return|return
name|from
argument_list|(
name|Iterables
operator|.
name|limit
argument_list|(
name|iterable
argument_list|,
name|size
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Determines whether this fluent iterable is empty.    */
DECL|method|isEmpty ()
specifier|public
specifier|final
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
operator|!
name|iterable
operator|.
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
return|;
block|}
comment|/**    * Returns an {@code ImmutableList} containing all of the elements from this fluent iterable in    * proper sequence.    *    * @since 14.0 (since 12.0 as {@code toImmutableList()}).    */
DECL|method|toList ()
specifier|public
specifier|final
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|toList
parameter_list|()
block|{
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|iterable
argument_list|)
return|;
block|}
comment|/**    * Returns an {@code ImmutableList} containing all of the elements from this {@code    * FluentIterable} in the order specified by {@code comparator}.  To produce an {@code    * ImmutableList} sorted by its natural ordering, use {@code toSortedList(Ordering.natural())}.    *    * @param comparator the function by which to sort list elements    * @throws NullPointerException if any element is null    * @since 14.0 (since 13.0 as {@code toSortedImmutableList()}).    */
annotation|@
name|Beta
DECL|method|toSortedList (Comparator<? super E> comparator)
specifier|public
specifier|final
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|toSortedList
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
name|Ordering
operator|.
name|from
argument_list|(
name|comparator
argument_list|)
operator|.
name|immutableSortedCopy
argument_list|(
name|iterable
argument_list|)
return|;
block|}
comment|/**    * Returns an {@code ImmutableSet} containing all of the elements from this fluent iterable with    * duplicates removed.    *    * @since 14.0 (since 12.0 as {@code toImmutableSet()}).    */
DECL|method|toSet ()
specifier|public
specifier|final
name|ImmutableSet
argument_list|<
name|E
argument_list|>
name|toSet
parameter_list|()
block|{
return|return
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|iterable
argument_list|)
return|;
block|}
comment|/**    * Returns an {@code ImmutableSortedSet} containing all of the elements from this {@code    * FluentIterable} in the order specified by {@code comparator}, with duplicates (determined by    * {@code comparator.compare(x, y) == 0}) removed. To produce an {@code ImmutableSortedSet} sorted    * by its natural ordering, use {@code toSortedSet(Ordering.natural())}.    *    * @param comparator the function by which to sort set elements    * @throws NullPointerException if any element is null    * @since 14.0 (since 12.0 as {@code toImmutableSortedSet()}).    */
DECL|method|toSortedSet (Comparator<? super E> comparator)
specifier|public
specifier|final
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|toSortedSet
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
name|ImmutableSortedSet
operator|.
name|copyOf
argument_list|(
name|comparator
argument_list|,
name|iterable
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable map for which the elements of this {@code FluentIterable} are the keys in    * the same order, mapped to values by the given function. If this iterable contains duplicate    * elements, the returned map will contain each distinct element once in the order it first    * appears.    *    * @throws NullPointerException if any element of this iterable is {@code null}, or if {@code    *     valueFunction} produces {@code null} for any key    * @since 14.0    */
DECL|method|toMap (Function<? super E, V> valueFunction)
specifier|public
specifier|final
parameter_list|<
name|V
parameter_list|>
name|ImmutableMap
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|toMap
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|E
argument_list|,
name|V
argument_list|>
name|valueFunction
parameter_list|)
block|{
return|return
name|Maps
operator|.
name|toMap
argument_list|(
name|iterable
argument_list|,
name|valueFunction
argument_list|)
return|;
block|}
comment|/**    * Creates an index {@code ImmutableListMultimap} that contains the results of applying a    * specified function to each item in this {@code FluentIterable} of values. Each element of this    * iterable will be stored as a value in the resulting multimap, yielding a multimap with the same    * size as this iterable. The key used to store that value in the multimap will be the result of    * calling the function on that value. The resulting multimap is created as an immutable snapshot.    * In the returned multimap, keys appear in the order they are first encountered, and the values    * corresponding to each key appear in the same order as they are encountered.    *    * @param keyFunction the function used to produce the key for each value    * @throws NullPointerException if any of the following cases is true:    *<ul>    *<li>{@code keyFunction} is null    *<li>An element in this fluent iterable is null    *<li>{@code keyFunction} returns {@code null} for any element of this iterable    *</ul>    * @since 14.0    */
DECL|method|index (Function<? super E, K> keyFunction)
specifier|public
specifier|final
parameter_list|<
name|K
parameter_list|>
name|ImmutableListMultimap
argument_list|<
name|K
argument_list|,
name|E
argument_list|>
name|index
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|E
argument_list|,
name|K
argument_list|>
name|keyFunction
parameter_list|)
block|{
return|return
name|Multimaps
operator|.
name|index
argument_list|(
name|iterable
argument_list|,
name|keyFunction
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable map for which the {@link java.util.Map#values} are the elements of this    * {@code FluentIterable} in the given order, and each key is the product of invoking a supplied    * function on its corresponding value.    *    * @param keyFunction the function used to produce the key for each value    * @throws IllegalArgumentException if {@code keyFunction} produces the same key for more than one    *     value in this fluent iterable    * @throws NullPointerException if any element of this fluent iterable is null, or if    *     {@code keyFunction} produces {@code null} for any value    * @since 14.0    */
DECL|method|uniqueIndex (Function<? super E, K> keyFunction)
specifier|public
specifier|final
parameter_list|<
name|K
parameter_list|>
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|E
argument_list|>
name|uniqueIndex
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|E
argument_list|,
name|K
argument_list|>
name|keyFunction
parameter_list|)
block|{
return|return
name|Maps
operator|.
name|uniqueIndex
argument_list|(
name|iterable
argument_list|,
name|keyFunction
argument_list|)
return|;
block|}
comment|/**    * Returns an array containing all of the elements from this fluent iterable in iteration order.    *    * @param type the type of the elements    * @return a newly-allocated array into which all the elements of this fluent iterable have    *     been copied    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"Array.newArray(Class, int)"
argument_list|)
DECL|method|toArray (Class<E> type)
specifier|public
specifier|final
name|E
index|[]
name|toArray
parameter_list|(
name|Class
argument_list|<
name|E
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|Iterables
operator|.
name|toArray
argument_list|(
name|iterable
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**    * Copies all the elements from this fluent iterable to {@code collection}. This is equivalent to    * calling {@code Iterables.addAll(collection, this)}.    *    * @param collection the collection to copy elements to    * @return {@code collection}, for convenience    * @since 14.0    */
DECL|method|copyInto (C collection)
specifier|public
specifier|final
parameter_list|<
name|C
extends|extends
name|Collection
argument_list|<
name|?
super|super
name|E
argument_list|>
parameter_list|>
name|C
name|copyInto
parameter_list|(
name|C
name|collection
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|collection
argument_list|)
expr_stmt|;
if|if
condition|(
name|iterable
operator|instanceof
name|Collection
condition|)
block|{
name|collection
operator|.
name|addAll
argument_list|(
name|Collections2
operator|.
name|cast
argument_list|(
name|iterable
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|E
name|item
range|:
name|iterable
control|)
block|{
name|collection
operator|.
name|add
argument_list|(
name|item
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|collection
return|;
block|}
comment|/**    * Returns the element at the specified position in this fluent iterable.    *    * @param position position of the element to return    * @return the element at the specified position in this fluent iterable    * @throws IndexOutOfBoundsException if {@code position} is negative or greater than or equal to    *     the size of this fluent iterable    */
DECL|method|get (int position)
specifier|public
specifier|final
name|E
name|get
parameter_list|(
name|int
name|position
parameter_list|)
block|{
return|return
name|Iterables
operator|.
name|get
argument_list|(
name|iterable
argument_list|,
name|position
argument_list|)
return|;
block|}
comment|/**    * Function that transforms {@code Iterable<E>} into a fluent iterable.    */
DECL|class|FromIterableFunction
specifier|private
specifier|static
class|class
name|FromIterableFunction
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Function
argument_list|<
name|Iterable
argument_list|<
name|E
argument_list|>
argument_list|,
name|FluentIterable
argument_list|<
name|E
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|apply (Iterable<E> fromObject)
specifier|public
name|FluentIterable
argument_list|<
name|E
argument_list|>
name|apply
parameter_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|fromObject
parameter_list|)
block|{
return|return
name|FluentIterable
operator|.
name|from
argument_list|(
name|fromObject
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

