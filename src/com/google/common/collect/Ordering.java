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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|VisibleForTesting
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
name|HashSet
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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

begin_comment
comment|/**  * A comparator with added methods to support common functions. For example:  *<pre>   {@code  *  *   if (Ordering.from(comparator).reverse().isOrdered(list)) { ... }}</pre>  *  *<p>The {@link #from(Comparator)} method returns the equivalent {@code  * Ordering} instance for a pre-existing comparator. You can also skip the  * comparator step and extend {@code Ordering} directly:<pre>   {@code  *  *   Ordering<String> byLengthOrdering = new Ordering<String>() {  *     public int compare(String left, String right) {  *       return Ints.compare(left.length(), right.length());  *     }  *   };}</pre>  *  * Except as noted, the orderings returned by the factory methods of this  * class are serializable if and only if the provided instances that back them  * are. For example, if {@code ordering} and {@code function} can themselves be  * serialized, then {@code ordering.onResultOf(function)} can as well.  *  * @author Jesse Wilson  * @author Kevin Bourrillion  * @since 2 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Ordering
specifier|public
specifier|abstract
class|class
name|Ordering
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Comparator
argument_list|<
name|T
argument_list|>
block|{
comment|// Static factories
comment|/**    * Returns a serializable ordering that uses the natural order of the values.    * The ordering throws a {@link NullPointerException} when passed a null    * parameter.    *    *<p>The type specification is {@code<C extends Comparable>}, instead of    * the technically correct {@code<C extends Comparable<? super C>>}, to    * support legacy types from before Java 5.    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// TODO: the right way to explain this??
DECL|method|natural ()
specifier|public
specifier|static
parameter_list|<
name|C
extends|extends
name|Comparable
parameter_list|>
name|Ordering
argument_list|<
name|C
argument_list|>
name|natural
parameter_list|()
block|{
return|return
operator|(
name|Ordering
operator|)
name|NaturalOrdering
operator|.
name|INSTANCE
return|;
block|}
comment|/**    * Returns an ordering for a pre-existing {@code comparator}. Note    * that if the comparator is not pre-existing, and you don't require    * serialization, you can subclass {@code Ordering} and implement its    * {@link #compare(Object, Object) compare} method instead.    *    * @param comparator the comparator that defines the order    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|from (Comparator<T> comparator)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Ordering
argument_list|<
name|T
argument_list|>
name|from
parameter_list|(
name|Comparator
argument_list|<
name|T
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
operator|(
name|comparator
operator|instanceof
name|Ordering
operator|)
condition|?
operator|(
name|Ordering
argument_list|<
name|T
argument_list|>
operator|)
name|comparator
else|:
operator|new
name|ComparatorOrdering
argument_list|<
name|T
argument_list|>
argument_list|(
name|comparator
argument_list|)
return|;
block|}
comment|/**    * Simply returns its argument.    *    * @deprecated no need to use this    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|from (Ordering<T> ordering)
annotation|@
name|Deprecated
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Ordering
argument_list|<
name|T
argument_list|>
name|from
parameter_list|(
name|Ordering
argument_list|<
name|T
argument_list|>
name|ordering
parameter_list|)
block|{
return|return
name|checkNotNull
argument_list|(
name|ordering
argument_list|)
return|;
block|}
comment|/**    * Returns an ordering that compares objects according to the order in    * which they appear in the given list. Only objects present in the list    * (according to {@link Object#equals}) may be compared. This comparator    * imposes a "partial ordering" over the type {@code T}. Subsequent changes    * to the {@code valuesInOrder} list will have no effect on the returned    * comparator. Null values in the list are not supported.    *    *<p>The returned comparator throws an {@link ClassCastException} when it    * receives an input parameter that isn't among the provided values.    *    *<p>The generated comparator is serializable if all the provided values are    * serializable.    *    * @param valuesInOrder the values that the returned comparator will be able    *     to compare, in the order the comparator should induce    * @return the comparator described above    * @throws NullPointerException if any of the provided values is null    * @throws IllegalArgumentException if {@code valuesInOrder} contains any    *     duplicate values (according to {@link Object#equals})    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|explicit (List<T> valuesInOrder)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Ordering
argument_list|<
name|T
argument_list|>
name|explicit
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|valuesInOrder
parameter_list|)
block|{
return|return
operator|new
name|ExplicitOrdering
argument_list|<
name|T
argument_list|>
argument_list|(
name|valuesInOrder
argument_list|)
return|;
block|}
comment|/**    * Returns an ordering that compares objects according to the order in    * which they are given to this method. Only objects present in the argument    * list (according to {@link Object#equals}) may be compared. This comparator    * imposes a "partial ordering" over the type {@code T}. Null values in the    * argument list are not supported.    *    *<p>The returned comparator throws a {@link ClassCastException} when it    * receives an input parameter that isn't among the provided values.    *    *<p>The generated comparator is serializable if all the provided values are    * serializable.    *    * @param leastValue the value which the returned comparator should consider    *     the "least" of all values    * @param remainingValuesInOrder the rest of the values that the returned    *     comparator will be able to compare, in the order the comparator should    *     follow    * @return the comparator described above    * @throws NullPointerException if any of the provided values is null    * @throws IllegalArgumentException if any duplicate values (according to    *     {@link Object#equals(Object)}) are present among the method arguments    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|explicit ( T leastValue, T... remainingValuesInOrder)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Ordering
argument_list|<
name|T
argument_list|>
name|explicit
parameter_list|(
name|T
name|leastValue
parameter_list|,
name|T
modifier|...
name|remainingValuesInOrder
parameter_list|)
block|{
return|return
name|explicit
argument_list|(
name|Lists
operator|.
name|asList
argument_list|(
name|leastValue
argument_list|,
name|remainingValuesInOrder
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Exception thrown by a {@link Ordering#explicit(List)} or {@link    * Ordering#explicit(Object, Object[])} comparator when comparing a value    * outside the set of values it can compare. Extending {@link    * ClassCastException} may seem odd, but it is required.    */
comment|// TODO: consider making this exception type public. or consider getting rid
comment|// of it.
annotation|@
name|VisibleForTesting
DECL|class|IncomparableValueException
specifier|static
class|class
name|IncomparableValueException
extends|extends
name|ClassCastException
block|{
DECL|field|value
specifier|final
name|Object
name|value
decl_stmt|;
DECL|method|IncomparableValueException (Object value)
name|IncomparableValueException
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|super
argument_list|(
literal|"Cannot compare value: "
operator|+
name|value
argument_list|)
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
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
comment|/**    * Returns an arbitrary ordering over all objects, for which {@code compare(a,    * b) == 0} implies {@code a == b} (identity equality). There is no meaning    * whatsoever to the order imposed, but it is constant for the life of the VM.    *    *<p>Because the ordering is identity-based, it is not "consistent with    * {@link Object#equals(Object)}" as defined by {@link Comparator}. Use    * caution when building a {@link SortedSet} or {@link SortedMap} from it, as    * the resulting collection will not behave exactly according to spec.    *    *<p>This ordering is not serializable, as its implementation relies on    * {@link System#identityHashCode(Object)}, so its behavior cannot be    * preserved across serialization.    *    * @since 2    */
DECL|method|arbitrary ()
specifier|public
specifier|static
name|Ordering
argument_list|<
name|Object
argument_list|>
name|arbitrary
parameter_list|()
block|{
return|return
name|ArbitraryOrderingHolder
operator|.
name|ARBITRARY_ORDERING
return|;
block|}
DECL|class|ArbitraryOrderingHolder
specifier|private
specifier|static
class|class
name|ArbitraryOrderingHolder
block|{
DECL|field|ARBITRARY_ORDERING
specifier|static
specifier|final
name|Ordering
argument_list|<
name|Object
argument_list|>
name|ARBITRARY_ORDERING
init|=
operator|new
name|ArbitraryOrdering
argument_list|()
decl_stmt|;
block|}
DECL|class|ArbitraryOrdering
annotation|@
name|VisibleForTesting
specifier|static
class|class
name|ArbitraryOrdering
extends|extends
name|Ordering
argument_list|<
name|Object
argument_list|>
block|{
DECL|field|uids
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|Integer
argument_list|>
name|uids
init|=
name|Platform
operator|.
name|tryWeakKeys
argument_list|(
operator|new
name|MapMaker
argument_list|()
argument_list|)
operator|.
name|makeComputingMap
argument_list|(
operator|new
name|Function
argument_list|<
name|Object
argument_list|,
name|Integer
argument_list|>
argument_list|()
block|{
specifier|final
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|public
name|Integer
name|apply
parameter_list|(
name|Object
name|from
parameter_list|)
block|{
return|return
name|counter
operator|.
name|getAndIncrement
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
DECL|method|compare (Object left, Object right)
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|left
parameter_list|,
name|Object
name|right
parameter_list|)
block|{
if|if
condition|(
name|left
operator|==
name|right
condition|)
block|{
return|return
literal|0
return|;
block|}
name|int
name|leftCode
init|=
name|identityHashCode
argument_list|(
name|left
argument_list|)
decl_stmt|;
name|int
name|rightCode
init|=
name|identityHashCode
argument_list|(
name|right
argument_list|)
decl_stmt|;
if|if
condition|(
name|leftCode
operator|!=
name|rightCode
condition|)
block|{
return|return
name|leftCode
operator|<
name|rightCode
condition|?
operator|-
literal|1
else|:
literal|1
return|;
block|}
comment|// identityHashCode collision (rare, but not as rare as you'd think)
name|int
name|result
init|=
name|uids
operator|.
name|get
argument_list|(
name|left
argument_list|)
operator|.
name|compareTo
argument_list|(
name|uids
operator|.
name|get
argument_list|(
name|right
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
comment|// extremely, extremely unlikely.
block|}
return|return
name|result
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
literal|"Ordering.arbitrary()"
return|;
block|}
comment|/*      * We need to be able to mock identityHashCode() calls for tests, because it      * can take 1-10 seconds to find colliding objects. Mocking frameworks that      * can do magic to mock static method calls still can't do so for a system      * class, so we need the indirection. In production, Hotspot should still      * recognize that the call is 1-morphic and should still be willing to      * inline it if necessary.      */
DECL|method|identityHashCode (Object object)
name|int
name|identityHashCode
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|System
operator|.
name|identityHashCode
argument_list|(
name|object
argument_list|)
return|;
block|}
block|}
comment|/**    * Returns an ordering that compares objects by the natural ordering of their    * string representations as returned by {@code toString()}. It does not    * support null values.    *    *<p>The comparator is serializable.    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|usingToString ()
specifier|public
specifier|static
name|Ordering
argument_list|<
name|Object
argument_list|>
name|usingToString
parameter_list|()
block|{
return|return
name|UsingToStringOrdering
operator|.
name|INSTANCE
return|;
block|}
comment|/**    * Returns an ordering which tries each given comparator in order until a    * non-zero result is found, returning that result, and returning zero only if    * all comparators return zero. The returned ordering is based on the state of    * the {@code comparators} iterable at the time it was provided to this    * method.    *    *<p>The returned ordering is equivalent to that produced using {@code    * Ordering.from(comp1).compound(comp2).compound(comp3) . . .}.    *    *<p><b>Warning:</b> Supplying an argument with undefined iteration order,    * such as a {@link HashSet}, will produce non-deterministic results.    *    * @param comparators the comparators to try in order    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|compound ( Iterable<? extends Comparator<? super T>> comparators)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Ordering
argument_list|<
name|T
argument_list|>
name|compound
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|Comparator
argument_list|<
name|?
super|super
name|T
argument_list|>
argument_list|>
name|comparators
parameter_list|)
block|{
return|return
operator|new
name|CompoundOrdering
argument_list|<
name|T
argument_list|>
argument_list|(
name|comparators
argument_list|)
return|;
block|}
comment|/**    * Constructs a new instance of this class (only invokable by the subclass    * constructor, typically implicit).    */
DECL|method|Ordering ()
specifier|protected
name|Ordering
parameter_list|()
block|{}
comment|// Non-static factories
comment|/**    * Returns an ordering which first uses the ordering {@code this}, but which    * in the event of a "tie", then delegates to {@code secondaryComparator}.    * For example, to sort a bug list first by status and second by priority, you    * might use {@code byStatus.compound(byPriority)}. For a compound ordering    * with three or more components, simply chain multiple calls to this method.    *    *<p>An ordering produced by this method, or a chain of calls to this method,    * is equivalent to one created using {@link Ordering#compound(Iterable)} on    * the same component comparators.    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|compound ( Comparator<? super U> secondaryComparator)
specifier|public
parameter_list|<
name|U
extends|extends
name|T
parameter_list|>
name|Ordering
argument_list|<
name|U
argument_list|>
name|compound
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|U
argument_list|>
name|secondaryComparator
parameter_list|)
block|{
return|return
operator|new
name|CompoundOrdering
argument_list|<
name|U
argument_list|>
argument_list|(
name|this
argument_list|,
name|checkNotNull
argument_list|(
name|secondaryComparator
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns the reverse of this ordering; the {@code Ordering} equivalent to    * {@link Collections#reverseOrder(Comparator)}.    */
comment|// type parameter<S> lets us avoid the extra<String> in statements like:
comment|// Ordering<String> o = Ordering.<String>natural().reverse();
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|reverse ()
specifier|public
parameter_list|<
name|S
extends|extends
name|T
parameter_list|>
name|Ordering
argument_list|<
name|S
argument_list|>
name|reverse
parameter_list|()
block|{
return|return
operator|new
name|ReverseOrdering
argument_list|<
name|S
argument_list|>
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**    * Returns a new ordering on {@code F} which orders elements by first applying    * a function to them, then comparing those results using {@code this}. For    * example, to compare objects by their string forms, in a case-insensitive    * manner, use:<pre>   {@code    *    *   Ordering.from(String.CASE_INSENSITIVE_ORDER)    *       .onResultOf(Functions.toStringFunction())}</pre>    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|onResultOf (Function<F, ? extends T> function)
specifier|public
parameter_list|<
name|F
parameter_list|>
name|Ordering
argument_list|<
name|F
argument_list|>
name|onResultOf
parameter_list|(
name|Function
argument_list|<
name|F
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|function
parameter_list|)
block|{
return|return
operator|new
name|ByFunctionOrdering
argument_list|<
name|F
argument_list|,
name|T
argument_list|>
argument_list|(
name|function
argument_list|,
name|this
argument_list|)
return|;
block|}
comment|/**    * Returns a new ordering which sorts iterables by comparing corresponding    * elements pairwise until a nonzero result is found; imposes "dictionary    * order". If the end of one iterable is reached, but not the other, the    * shorter iterable is considered to be less than the longer one. For example,    * a lexicographical natural ordering over integers considers {@code    * []< [1]< [1, 1]< [1, 2]< [2]}.    *    *<p>Note that {@code ordering.lexicographical().reverse()} is not    * equivalent to {@code ordering.reverse().lexicographical()} (consider how    * each would order {@code [1]} and {@code [1, 1]}).    *    * @since 2    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
comment|// type parameter<S> lets us avoid the extra<String> in statements like:
comment|// Ordering<Iterable<String>> o =
comment|//     Ordering.<String>natural().lexicographical();
DECL|method|lexicographical ()
specifier|public
parameter_list|<
name|S
extends|extends
name|T
parameter_list|>
name|Ordering
argument_list|<
name|Iterable
argument_list|<
name|S
argument_list|>
argument_list|>
name|lexicographical
parameter_list|()
block|{
comment|/*      * Note that technically the returned ordering should be capable of      * handling not just {@code Iterable<S>} instances, but also any {@code      * Iterable<? extends S>}. However, the need for this comes up so rarely      * that it doesn't justify making everyone else deal with the very ugly      * wildcard.      */
return|return
operator|new
name|LexicographicalOrdering
argument_list|<
name|S
argument_list|>
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**    * Returns an ordering that treats {@code null} as less than all other values    * and uses {@code this} to compare non-null values.    */
comment|// type parameter<S> lets us avoid the extra<String> in statements like:
comment|// Ordering<String> o = Ordering.<String>natural().nullsFirst();
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|nullsFirst ()
specifier|public
parameter_list|<
name|S
extends|extends
name|T
parameter_list|>
name|Ordering
argument_list|<
name|S
argument_list|>
name|nullsFirst
parameter_list|()
block|{
return|return
operator|new
name|NullsFirstOrdering
argument_list|<
name|S
argument_list|>
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**    * Returns an ordering that treats {@code null} as greater than all other    * values and uses this ordering to compare non-null values.    */
comment|// type parameter<S> lets us avoid the extra<String> in statements like:
comment|// Ordering<String> o = Ordering.<String>natural().nullsLast();
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|nullsLast ()
specifier|public
parameter_list|<
name|S
extends|extends
name|T
parameter_list|>
name|Ordering
argument_list|<
name|S
argument_list|>
name|nullsLast
parameter_list|()
block|{
return|return
operator|new
name|NullsLastOrdering
argument_list|<
name|S
argument_list|>
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|// Regular instance methods
comment|/**    * {@link Collections#binarySearch(List, Object, Comparator) Searches}    * {@code sortedList} for {@code key} using the binary search algorithm. The    * list must be sorted using this ordering.    *    * @param sortedList the list to be searched    * @param key the key to be searched for    */
DECL|method|binarySearch (List<? extends T> sortedList, T key)
specifier|public
name|int
name|binarySearch
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|sortedList
parameter_list|,
name|T
name|key
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|binarySearch
argument_list|(
name|sortedList
argument_list|,
name|key
argument_list|,
name|this
argument_list|)
return|;
block|}
comment|/**    * Returns a copy of the given iterable sorted by this ordering. The input is    * not modified. The returned list is modifiable, serializable, and has random    * access.    *    *<p>Unlike {@link Sets#newTreeSet(Iterable)}, this method does not discard    * elements that are duplicates according to the comparator. The sort    * performed is<i>stable</i>, meaning that such elements will appear in the    * resulting list in the same order they appeared in the input.    *    * @param iterable the elements to be copied and sorted    * @return a new list containing the given elements in sorted order    */
DECL|method|sortedCopy (Iterable<E> iterable)
specifier|public
parameter_list|<
name|E
extends|extends
name|T
parameter_list|>
name|List
argument_list|<
name|E
argument_list|>
name|sortedCopy
parameter_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|iterable
parameter_list|)
block|{
name|List
argument_list|<
name|E
argument_list|>
name|list
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|iterable
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|list
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
comment|/**    * Returns an<i>immutable</i> copy of the given iterable sorted by this    * ordering. The input is not modified.    *    *<p>Unlike {@link Sets#newTreeSet(Iterable)}, this method does not discard    * elements that are duplicates according to the comparator. The sort    * performed is<i>stable</i>, meaning that such elements will appear in the    * resulting list in the same order they appeared in the input.    *    * @param iterable the elements to be copied and sorted    * @return a new immutable list containing the given elements in sorted order    * @throws NullPointerException if {@code iterable} or any of its elements is    *     null    * @since 3    */
DECL|method|immutableSortedCopy ( Iterable<E> iterable)
specifier|public
parameter_list|<
name|E
extends|extends
name|T
parameter_list|>
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|immutableSortedCopy
parameter_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|iterable
parameter_list|)
block|{
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|sortedCopy
argument_list|(
name|iterable
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns {@code true} if each element in {@code iterable} after the first is    * greater than or equal to the element that preceded it, according to this    * ordering. Note that this is always true when the iterable has fewer than    * two elements.    */
DECL|method|isOrdered (Iterable<? extends T> iterable)
specifier|public
name|boolean
name|isOrdered
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|iterable
parameter_list|)
block|{
name|Iterator
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|it
init|=
name|iterable
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|T
name|prev
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|T
name|next
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|compare
argument_list|(
name|prev
argument_list|,
name|next
argument_list|)
operator|>
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
name|prev
operator|=
name|next
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**    * Returns {@code true} if each element in {@code iterable} after the first is    *<i>strictly</i> greater than the element that preceded it, according to    * this ordering. Note that this is always true when the iterable has fewer    * than two elements.    */
DECL|method|isStrictlyOrdered (Iterable<? extends T> iterable)
specifier|public
name|boolean
name|isStrictlyOrdered
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|iterable
parameter_list|)
block|{
name|Iterator
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|it
init|=
name|iterable
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|T
name|prev
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|T
name|next
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|compare
argument_list|(
name|prev
argument_list|,
name|next
argument_list|)
operator|>=
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
name|prev
operator|=
name|next
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**    * Returns the largest of the specified values according to this ordering. If    * there are multiple largest values, the first of those is returned.    *    * @param iterable the iterable whose maximum element is to be determined    * @throws NoSuchElementException if {@code iterable} is empty    * @throws ClassCastException if the parameters are not<i>mutually    *     comparable</i> under this ordering.    */
DECL|method|max (Iterable<E> iterable)
specifier|public
parameter_list|<
name|E
extends|extends
name|T
parameter_list|>
name|E
name|max
parameter_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|iterable
parameter_list|)
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
comment|// let this throw NoSuchElementException as necessary
name|E
name|maxSoFar
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|maxSoFar
operator|=
name|max
argument_list|(
name|maxSoFar
argument_list|,
name|iterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|maxSoFar
return|;
block|}
comment|/**    * Returns the largest of the specified values according to this ordering. If    * there are multiple largest values, the first of those is returned.    *    * @param a value to compare, returned if greater than or equal to the rest.    * @param b value to compare    * @param c value to compare    * @param rest values to compare    * @throws ClassCastException if the parameters are not<i>mutually    *     comparable</i> under this ordering.    */
DECL|method|max (E a, E b, E c, E... rest)
specifier|public
parameter_list|<
name|E
extends|extends
name|T
parameter_list|>
name|E
name|max
parameter_list|(
name|E
name|a
parameter_list|,
name|E
name|b
parameter_list|,
name|E
name|c
parameter_list|,
name|E
modifier|...
name|rest
parameter_list|)
block|{
name|E
name|maxSoFar
init|=
name|max
argument_list|(
name|max
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|,
name|c
argument_list|)
decl_stmt|;
for|for
control|(
name|E
name|r
range|:
name|rest
control|)
block|{
name|maxSoFar
operator|=
name|max
argument_list|(
name|maxSoFar
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
return|return
name|maxSoFar
return|;
block|}
comment|/**    * Returns the larger of the two values according to this ordering. If the    * values compare as 0, the first is returned.    *    *<p><b>Implementation note:</b> this method is invoked by the default    * implementations of the other {@code max} overloads, so overriding it will    * affect their behavior.    *    * @param a value to compare, returned if greater than or equal to b.    * @param b value to compare.    * @throws ClassCastException if the parameters are not<i>mutually    *     comparable</i> under this ordering.    */
DECL|method|max (E a, E b)
specifier|public
parameter_list|<
name|E
extends|extends
name|T
parameter_list|>
name|E
name|max
parameter_list|(
name|E
name|a
parameter_list|,
name|E
name|b
parameter_list|)
block|{
return|return
name|compare
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
operator|>=
literal|0
condition|?
name|a
else|:
name|b
return|;
block|}
comment|/**    * Returns the smallest of the specified values according to this ordering. If    * there are multiple smallest values, the first of those is returned.    *    * @param iterable the iterable whose minimum element is to be determined    * @throws NoSuchElementException if {@code iterable} is empty    * @throws ClassCastException if the parameters are not<i>mutually    *     comparable</i> under this ordering.    */
DECL|method|min (Iterable<E> iterable)
specifier|public
parameter_list|<
name|E
extends|extends
name|T
parameter_list|>
name|E
name|min
parameter_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|iterable
parameter_list|)
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
comment|// let this throw NoSuchElementException as necessary
name|E
name|minSoFar
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|minSoFar
operator|=
name|min
argument_list|(
name|minSoFar
argument_list|,
name|iterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|minSoFar
return|;
block|}
comment|/**    * Returns the smallest of the specified values according to this ordering. If    * there are multiple smallest values, the first of those is returned.    *    * @param a value to compare, returned if less than or equal to the rest.    * @param b value to compare    * @param c value to compare    * @param rest values to compare    * @throws ClassCastException if the parameters are not<i>mutually    *     comparable</i> under this ordering.    */
DECL|method|min (E a, E b, E c, E... rest)
specifier|public
parameter_list|<
name|E
extends|extends
name|T
parameter_list|>
name|E
name|min
parameter_list|(
name|E
name|a
parameter_list|,
name|E
name|b
parameter_list|,
name|E
name|c
parameter_list|,
name|E
modifier|...
name|rest
parameter_list|)
block|{
name|E
name|minSoFar
init|=
name|min
argument_list|(
name|min
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|,
name|c
argument_list|)
decl_stmt|;
for|for
control|(
name|E
name|r
range|:
name|rest
control|)
block|{
name|minSoFar
operator|=
name|min
argument_list|(
name|minSoFar
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
return|return
name|minSoFar
return|;
block|}
comment|/**    * Returns the smaller of the two values according to this ordering. If the    * values compare as 0, the first is returned.    *    *<p><b>Implementation note:</b> this method is invoked by the default    * implementations of the other {@code min} overloads, so overriding it will    * affect their behavior.    *    * @param a value to compare, returned if less than or equal to b.    * @param b value to compare.    * @throws ClassCastException if the parameters are not<i>mutually    *     comparable</i> under this ordering.    */
DECL|method|min (E a, E b)
specifier|public
parameter_list|<
name|E
extends|extends
name|T
parameter_list|>
name|E
name|min
parameter_list|(
name|E
name|a
parameter_list|,
name|E
name|b
parameter_list|)
block|{
return|return
name|compare
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
operator|<=
literal|0
condition|?
name|a
else|:
name|b
return|;
block|}
comment|// Never make these public
DECL|field|LEFT_IS_GREATER
specifier|static
specifier|final
name|int
name|LEFT_IS_GREATER
init|=
literal|1
decl_stmt|;
DECL|field|RIGHT_IS_GREATER
specifier|static
specifier|final
name|int
name|RIGHT_IS_GREATER
init|=
operator|-
literal|1
decl_stmt|;
block|}
end_class

end_unit

