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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Ranges
operator|.
name|create
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
name|Predicate
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
name|NoSuchElementException
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A range, sometimes known as an<i>interval</i>, is a<i>convex</i>  * (informally, "contiguous" or "unbroken") portion of a particular domain.  * Formally, convexity means that for any {@code a<= b<= c},  * {@code range.contains(a)&& range.contains(c)} implies that {@code  * range.contains(b)}.  *  *<p>A range is characterized by its lower and upper<i>bounds</i> (extremes),  * each of which can<i>open</i> (exclusive of its endpoint),<i>closed</i>  * (inclusive of its endpoint), or<i>unbounded</i>. This yields nine basic  * types of ranges:  *  *<ul>  *<li>{@code (aâ¥b) = {x | a< x< b}}  *<li>{@code [aâ¥b] = {x | a<= x<= b}}  *<li>{@code [aâ¥b) = {x | a<= x< b}}  *<li>{@code (aâ¥b] = {x | a< x<= b}}  *<li>{@code (aâ¥+â) = {x | x> a}}  *<li>{@code [aâ¥+â) = {x | x>= a}}  *<li>{@code (-ââ¥b) = {x | x< b}}  *<li>{@code (-ââ¥b] = {x | x<= b}}  *<li>{@code (-ââ¥+â) = all values}  *</ul>  *  * (The notation {@code {x | statement}} is read "the set of all<i>x</i> such  * that<i>statement</i>.")  *  *<p>Notice that we use a square bracket ({@code [ ]}) to denote that an range  * is closed on that end, and a parenthesis ({@code ( )}) when it is open or  * unbounded.  *  *<p>The values {@code a} and {@code b} used above are called<i>endpoints</i>.  * The upper endpoint may not be less than the lower endpoint. The endpoints may  * be equal only if at least one of the bounds is closed:  *  *<ul>  *<li>{@code [aâ¥a]} : singleton range  *<li>{@code [aâ¥a); (aâ¥a]} : {@linkplain #isEmpty empty}, but valid  *<li>{@code (aâ¥a)} :<b>invalid</b>  *</ul>  *  *<p>Instances of this type can be obtained using the static factory methods in  * the {@link Ranges} class.  *  *<p>Instances of {@code Range} are immutable. It is strongly encouraged to  * use this class only with immutable data types. When creating a range over a  * mutable type, take great care not to allow the value objects to mutate after  * the range is created.  *  *<p>In this and other range-related specifications, concepts like "equal",  * "same", "unique" and so on are based on {@link Comparable#compareTo}  * returning zero, not on {@link Object#equals} returning {@code true}. Of  * course, when these methods are kept<i>consistent</i> (as defined in {@link  * Comparable}), this is not an issue.  *  *<p>A range {@code a} is said to be the<i>maximal</i> range having property  *<i>P</i> if, for all ranges {@code b} also having property<i>P</i>, {@code  * a.encloses(b)}. Likewise, {@code a} is<i>minimal</i> when {@code  * b.encloses(a)} for all {@code b} having property<i>P</i>. See, for example,  * the definition of {@link #intersection}.  *  *<p>This class can be used with any type which implements {@code Comparable};  * it does not require {@code Comparable<? super C>} because this would be  * incompatible with pre-Java 5 types. If this class is used with a perverse  * {@code Comparable} type ({@code Foo implements Comparable<Bar>} where {@code  * Bar} is not a supertype of {@code Foo}), any of its methods may throw {@link  * ClassCastException}. (There is no good reason for such a type to exist.)  *  *<p>When evaluated as a {@link Predicate}, a range yields the same result as  * invoking {@link #contains}.  *  * @author Kevin Bourrillion  * @author Gregory Kick  * @since Guava release 10  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|Beta
DECL|class|Range
specifier|public
specifier|final
class|class
name|Range
parameter_list|<
name|C
extends|extends
name|Comparable
parameter_list|>
implements|implements
name|Predicate
argument_list|<
name|C
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|lowerBound
specifier|final
name|Cut
argument_list|<
name|C
argument_list|>
name|lowerBound
decl_stmt|;
DECL|field|upperBound
specifier|final
name|Cut
argument_list|<
name|C
argument_list|>
name|upperBound
decl_stmt|;
DECL|method|Range (Cut<C> lowerBound, Cut<C> upperBound)
name|Range
parameter_list|(
name|Cut
argument_list|<
name|C
argument_list|>
name|lowerBound
parameter_list|,
name|Cut
argument_list|<
name|C
argument_list|>
name|upperBound
parameter_list|)
block|{
if|if
condition|(
name|lowerBound
operator|.
name|compareTo
argument_list|(
name|upperBound
argument_list|)
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid range: "
operator|+
name|toString
argument_list|(
name|lowerBound
argument_list|,
name|upperBound
argument_list|)
argument_list|)
throw|;
block|}
name|this
operator|.
name|lowerBound
operator|=
name|lowerBound
expr_stmt|;
name|this
operator|.
name|upperBound
operator|=
name|upperBound
expr_stmt|;
block|}
comment|/**    * Returns {@code true} if this range has a lower endpoint.    */
DECL|method|hasLowerBound ()
specifier|public
name|boolean
name|hasLowerBound
parameter_list|()
block|{
return|return
name|lowerBound
operator|!=
name|Cut
operator|.
name|belowAll
argument_list|()
return|;
block|}
comment|/**    * Returns the lower endpoint of this range.    *    * @throws IllegalStateException if this range is unbounded below (that is,    *     {@link #hasLowerBound()} is false)    */
DECL|method|lowerEndpoint ()
specifier|public
name|C
name|lowerEndpoint
parameter_list|()
block|{
return|return
name|lowerBound
operator|.
name|endpoint
argument_list|()
return|;
block|}
comment|/**    * Returns the type of this range's lower bound: {@link BoundType#CLOSED} if    * the range includes its lower endpoint, {@link BoundType#OPEN} if it does    * not.    *    * @throws IllegalStateException if this range is unbounded below (that is,    *     {@link #hasLowerBound()} is false)    */
DECL|method|lowerBoundType ()
specifier|public
name|BoundType
name|lowerBoundType
parameter_list|()
block|{
return|return
name|lowerBound
operator|.
name|typeAsLowerBound
argument_list|()
return|;
block|}
comment|/**    * Returns {@code true} if this range has an upper endpoint.    */
DECL|method|hasUpperBound ()
specifier|public
name|boolean
name|hasUpperBound
parameter_list|()
block|{
return|return
name|upperBound
operator|!=
name|Cut
operator|.
name|aboveAll
argument_list|()
return|;
block|}
comment|/**    * Returns the upper endpoint of this range.    *    * @throws IllegalStateException if this range is unbounded above (that is,    *     {@link #hasUpperBound()} is false)    */
DECL|method|upperEndpoint ()
specifier|public
name|C
name|upperEndpoint
parameter_list|()
block|{
return|return
name|upperBound
operator|.
name|endpoint
argument_list|()
return|;
block|}
comment|/**    * Returns the type of this range's upper bound: {@link BoundType#CLOSED} if    * the range includes its upper endpoint, {@link BoundType#OPEN} if it does    * not.    *    * @throws IllegalStateException if this range is unbounded above (that is,    *     {@link #hasUpperBound()} is false)    */
DECL|method|upperBoundType ()
specifier|public
name|BoundType
name|upperBoundType
parameter_list|()
block|{
return|return
name|upperBound
operator|.
name|typeAsUpperBound
argument_list|()
return|;
block|}
comment|/**    * Returns {@code true} if this range is of the form {@code [vâ¥v)} or {@code    * (vâ¥v]}. (This does not encompass ranges of the form {@code (vâ¥v)}, because    * such ranges are<i>invalid</i> and can't be constructed at all.)    *    *<p>Note that certain discrete ranges such as the integer range {@code    * (3â¥4)} are<b>not</b> considered empty, even though they contain no actual    * values.    */
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|lowerBound
operator|.
name|equals
argument_list|(
name|upperBound
argument_list|)
return|;
block|}
comment|/**    * Returns {@code true} if {@code value} is within the bounds of this    * range. For example, on the range {@code [0â¥2)}, {@code contains(1)}    * is true, while {@code contains(2)} is false.    */
DECL|method|contains (C value)
specifier|public
name|boolean
name|contains
parameter_list|(
name|C
name|value
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
comment|// let this throw CCE if there is some trickery going on
return|return
name|lowerBound
operator|.
name|isLessThan
argument_list|(
name|value
argument_list|)
operator|&&
operator|!
name|upperBound
operator|.
name|isLessThan
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**    * Equivalent to {@link #contains}; provided only to satisfy the {@link    * Predicate} interface. When using a reference of type {@code Range}, always    * invoke {@link #contains} directly instead.    */
DECL|method|apply (C input)
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|C
name|input
parameter_list|)
block|{
return|return
name|contains
argument_list|(
name|input
argument_list|)
return|;
block|}
comment|/**    * Returns {@code true} if every element in {@code values} is {@linkplain    * #contains contained} in this range.    */
DECL|method|containsAll (Iterable<? extends C> values)
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|C
argument_list|>
name|values
parameter_list|)
block|{
if|if
condition|(
name|Iterables
operator|.
name|isEmpty
argument_list|(
name|values
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// this optimizes testing equality of two range-backed sets
if|if
condition|(
name|values
operator|instanceof
name|SortedSet
condition|)
block|{
name|SortedSet
argument_list|<
name|?
extends|extends
name|C
argument_list|>
name|set
init|=
name|cast
argument_list|(
name|values
argument_list|)
decl_stmt|;
name|Comparator
argument_list|<
name|?
argument_list|>
name|comparator
init|=
name|set
operator|.
name|comparator
argument_list|()
decl_stmt|;
if|if
condition|(
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|equals
argument_list|(
name|comparator
argument_list|)
operator|||
name|comparator
operator|==
literal|null
condition|)
block|{
return|return
name|contains
argument_list|(
name|set
operator|.
name|first
argument_list|()
argument_list|)
operator|&&
name|contains
argument_list|(
name|set
operator|.
name|last
argument_list|()
argument_list|)
return|;
block|}
block|}
for|for
control|(
name|C
name|value
range|:
name|values
control|)
block|{
if|if
condition|(
operator|!
name|contains
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**    * Returns {@code true} if the bounds of {@code other} do not extend outside    * the bounds of this range. Examples:    *    *<ul>    *<li>{@code [3â¥6]} encloses {@code [4â¥5]}    *<li>{@code (3â¥6)} encloses {@code (3â¥6)}    *<li>{@code [3â¥6]} encloses {@code [4â¥4)} (even though the latter is    *     empty)    *<li>{@code (3â¥6]} does not enclose {@code [3â¥6]}    *<li>{@code [4â¥5]} does not enclose {@code (3â¥6)} (even though it contains    *     every value contained by the latter range)    *<li>{@code [3â¥6]} does not enclose {@code (1â¥1]} (even though it contains    *     every value contained by the latter range)    *</ul>    *    * Note that if {@code a.encloses(b)}, then {@code b.contains(v)} implies    * {@code a.contains(v)}, but as the last two examples illustrate, the    * converse is not always true.    *    *<p>The encloses relation has the following properties:    *    *<ul>    *<li>reflexive: {@code a.encloses(a)} is always true    *<li>antisymmetric: {@code a.encloses(b)&& b.encloses(a)} implies {@code    *     a.equals(b)}    *<li>transitive: {@code a.encloses(b)&& b.encloses(c)} implies {@code    *     a.encloses(c)}    *<li>not a total ordering: {@code !a.encloses(b)} does not imply {@code    *     b.encloses(a)}    *<li>there exists a {@linkplain Ranges#all maximal} range, for which    *     {@code encloses} is always true    *<li>there also exist {@linkplain #isEmpty minimal} ranges, for    *     which {@code encloses(b)} is always false when {@code !equals(b)}    *</ul>    */
DECL|method|encloses (Range<C> other)
specifier|public
name|boolean
name|encloses
parameter_list|(
name|Range
argument_list|<
name|C
argument_list|>
name|other
parameter_list|)
block|{
return|return
name|lowerBound
operator|.
name|compareTo
argument_list|(
name|other
operator|.
name|lowerBound
argument_list|)
operator|<=
literal|0
operator|&&
name|upperBound
operator|.
name|compareTo
argument_list|(
name|other
operator|.
name|upperBound
argument_list|)
operator|>=
literal|0
return|;
block|}
comment|/**    * Returns the maximal range {@linkplain #encloses enclosed} by both this    * range and {@code other}, if such a range exists. For example, the    * intersection of {@code [1â¥5]} and {@code (3â¥7)} is {@code (3â¥5]}. The    * resulting range may be empty; for example, {@code [1â¥5)} intersected with    * {@code [5â¥7)} yields the empty range {@code [5â¥5)}.    *    *<p>The intersection operation has the following properties:    *    *<ul>    *<li>commutative: {@code a.intersection(b)} produces the same result as    *     {@code b.intersection(a)}    *<li>associative: {@code a.intersection(b).intersection(c)} produces the    *     same result as {@code a.intersection(b.intersection(c))}    *<li>idempotent: {@code a.intersection(a)} equals {@code a}    *<li>identity ({@link Ranges#all}): {@code a.intersection(Ranges.all())}    *     equals {@code a}    *</ul>    *    * @throws IllegalArgumentException if no range exists that is enclosed by    *     both these ranges    */
DECL|method|intersection (Range<C> other)
specifier|public
name|Range
argument_list|<
name|C
argument_list|>
name|intersection
parameter_list|(
name|Range
argument_list|<
name|C
argument_list|>
name|other
parameter_list|)
block|{
name|Cut
argument_list|<
name|C
argument_list|>
name|newLower
init|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|max
argument_list|(
name|lowerBound
argument_list|,
name|other
operator|.
name|lowerBound
argument_list|)
decl_stmt|;
name|Cut
argument_list|<
name|C
argument_list|>
name|newUpper
init|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|min
argument_list|(
name|upperBound
argument_list|,
name|other
operator|.
name|upperBound
argument_list|)
decl_stmt|;
return|return
name|create
argument_list|(
name|newLower
argument_list|,
name|newUpper
argument_list|)
return|;
block|}
comment|/**    * Returns the minimal range that {@linkplain #encloses encloses} both this    * range and {@code other}. For example, the span of {@code [1â¥3]} and    * {@code (5â¥7)} is {@code [1â¥7)}. Note that the span may contain values    * that are not contained by either original range.    *    *<p>The span operation has the following properties:    *    *<ul>    *<li>closed: the range {@code a.span(b)} exists for all ranges {@code a} and    *     {@code b}    *<li>commutative: {@code a.span(b)} equals {@code b.span(a)}    *<li>associative: {@code a.span(b).span(c)} equals {@code a.span(b.span(c))}    *<li>idempotent: {@code a.span(a)} equals {@code a}    *</ul>    */
DECL|method|span (Range<C> other)
specifier|public
name|Range
argument_list|<
name|C
argument_list|>
name|span
parameter_list|(
name|Range
argument_list|<
name|C
argument_list|>
name|other
parameter_list|)
block|{
name|Cut
argument_list|<
name|C
argument_list|>
name|newLower
init|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|min
argument_list|(
name|lowerBound
argument_list|,
name|other
operator|.
name|lowerBound
argument_list|)
decl_stmt|;
name|Cut
argument_list|<
name|C
argument_list|>
name|newUpper
init|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|max
argument_list|(
name|upperBound
argument_list|,
name|other
operator|.
name|upperBound
argument_list|)
decl_stmt|;
return|return
name|create
argument_list|(
name|newLower
argument_list|,
name|newUpper
argument_list|)
return|;
block|}
comment|/**    * Returns an {@link ImmutableSortedSet} containing the same values in the    * given domain {@linkplain Range#contains contained} by this range.    *    *<p><b>Note</b>: {@code a.asSet().equals(b.asSet())} does not imply {@code    * a.equals(b)}! For example, {@code a} and {@code b} could be {@code [2â¥4]}    * and {@code (1â¥5)}, or the empty ranges {@code [3â¥3)} and {@code [4â¥4)}.    *    *<p><b>Warning:</b> Be extremely careful what you do with the {@code asSet}    * view of a large range (such as {@code Ranges.greaterThan(0)}). Certain    * operations on such a set can be performed efficiently, but others (such as    * {@link Set#hashCode} or {@link Collections#frequency}) can cause major    * performance problems.    *    *<p>The returned set's {@link Object#toString} method returns a short-hand    * form of set's contents such as {@code "[1â¥100]}"}.    *    * @throws IllegalArgumentException if neither this range nor the domain has a    *     lower bound, or if neither has an upper bound    */
comment|// TODO(kevinb): commit in spec to which methods are efficient?
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|false
argument_list|)
DECL|method|asSet (DiscreteDomain<C> domain)
specifier|public
name|ContiguousSet
argument_list|<
name|C
argument_list|>
name|asSet
parameter_list|(
name|DiscreteDomain
argument_list|<
name|C
argument_list|>
name|domain
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|domain
argument_list|)
expr_stmt|;
name|Range
argument_list|<
name|C
argument_list|>
name|effectiveRange
init|=
name|this
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|hasLowerBound
argument_list|()
condition|)
block|{
name|effectiveRange
operator|=
name|effectiveRange
operator|.
name|intersection
argument_list|(
name|Ranges
operator|.
name|atLeast
argument_list|(
name|domain
operator|.
name|minValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|hasUpperBound
argument_list|()
condition|)
block|{
name|effectiveRange
operator|=
name|effectiveRange
operator|.
name|intersection
argument_list|(
name|Ranges
operator|.
name|atMost
argument_list|(
name|domain
operator|.
name|maxValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// Per class spec, we are allowed to throw CCE if necessary
name|boolean
name|empty
init|=
name|effectiveRange
operator|.
name|isEmpty
argument_list|()
operator|||
name|compareOrThrow
argument_list|(
name|lowerBound
operator|.
name|leastValueAbove
argument_list|(
name|domain
argument_list|)
argument_list|,
name|upperBound
operator|.
name|greatestValueBelow
argument_list|(
name|domain
argument_list|)
argument_list|)
operator|>
literal|0
decl_stmt|;
return|return
name|empty
condition|?
operator|new
name|EmptyContiguousSet
argument_list|<
name|C
argument_list|>
argument_list|(
name|domain
argument_list|)
else|:
operator|new
name|RegularContiguousSet
argument_list|<
name|C
argument_list|>
argument_list|(
name|effectiveRange
argument_list|,
name|domain
argument_list|)
return|;
block|}
comment|/**    * Returns the canonical form of this range in the given domain. The canonical    * form has the following properties:    *    *<ul>    *<li>equivalence: {@code a.canonical().contains(v) == a.contains(v)} for    *     all {@code v} (in other words, {@code    *     a.canonical(domain).asSet(domain).equals(a.asSet(domain))}    *<li>uniqueness: unless {@code a.isEmpty()},    *     {@code a.asSet(domain).equals(b.asSet(domain))} implies    *     {@code a.canonical(domain).equals(b.canonical(domain))}    *<li>idempotence: {@code    *     a.canonical(domain).canonical(domain).equals(a.canonical(domain))}    *</ul>    *    * Furthermore, this method guarantees that the range returned will be one    * of the following canonical forms:    *    *<ul>    *<li>[startâ¥end)    *<li>[startâ¥+â)    *<li>(-ââ¥end) (only if type {@code C} is unbounded below)    *<li>(-ââ¥+â) (only if type {@code C} is unbounded below)    *</ul>    */
DECL|method|canonical (DiscreteDomain<C> domain)
specifier|public
name|Range
argument_list|<
name|C
argument_list|>
name|canonical
parameter_list|(
name|DiscreteDomain
argument_list|<
name|C
argument_list|>
name|domain
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|domain
argument_list|)
expr_stmt|;
name|Cut
argument_list|<
name|C
argument_list|>
name|lower
init|=
name|lowerBound
operator|.
name|canonical
argument_list|(
name|domain
argument_list|)
decl_stmt|;
name|Cut
argument_list|<
name|C
argument_list|>
name|upper
init|=
name|upperBound
operator|.
name|canonical
argument_list|(
name|domain
argument_list|)
decl_stmt|;
return|return
operator|(
name|lower
operator|==
name|lowerBound
operator|&&
name|upper
operator|==
name|upperBound
operator|)
condition|?
name|this
else|:
name|create
argument_list|(
name|lower
argument_list|,
name|upper
argument_list|)
return|;
block|}
comment|/**    * Returns {@code true} if {@code object} is a range having the same    * endpoints and bound types as this range. Note that discrete ranges    * such as {@code (1â¥4)} and {@code [2â¥3]} are<b>not</b> equal to one    * another, despite the fact that they each contain precisely the same set of    * values. Similarly, empty ranges are not equal unless they have exactly    * the same representation, so {@code [3â¥3)}, {@code (3â¥3]}, {@code (4â¥4]}    * are all unequal.    */
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
if|if
condition|(
name|object
operator|instanceof
name|Range
condition|)
block|{
name|Range
argument_list|<
name|?
argument_list|>
name|other
init|=
operator|(
name|Range
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|lowerBound
operator|.
name|equals
argument_list|(
name|other
operator|.
name|lowerBound
argument_list|)
operator|&&
name|upperBound
operator|.
name|equals
argument_list|(
name|other
operator|.
name|upperBound
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/** Returns a hash code for this range. */
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|lowerBound
operator|.
name|hashCode
argument_list|()
operator|*
literal|31
operator|+
name|upperBound
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/**    * Returns a string representation of this range, such as {@code "[3â¥5)"}    * (other examples are listed in the class documentation).    */
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|toString
argument_list|(
name|lowerBound
argument_list|,
name|upperBound
argument_list|)
return|;
block|}
DECL|method|toString (Cut<?> lowerBound, Cut<?> upperBound)
specifier|private
specifier|static
name|String
name|toString
parameter_list|(
name|Cut
argument_list|<
name|?
argument_list|>
name|lowerBound
parameter_list|,
name|Cut
argument_list|<
name|?
argument_list|>
name|upperBound
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|16
argument_list|)
decl_stmt|;
name|lowerBound
operator|.
name|describeAsLowerBound
argument_list|(
name|sb
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'\u2025'
argument_list|)
expr_stmt|;
name|upperBound
operator|.
name|describeAsUpperBound
argument_list|(
name|sb
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**    * Used to avoid http://bugs.sun.com/view_bug.do?bug_id=6558557    */
DECL|method|cast (Iterable<T> iterable)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|SortedSet
argument_list|<
name|T
argument_list|>
name|cast
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|iterable
parameter_list|)
block|{
return|return
operator|(
name|SortedSet
argument_list|<
name|T
argument_list|>
operator|)
name|iterable
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// this method may throw CCE
DECL|method|compareOrThrow (Comparable left, Comparable right)
specifier|static
name|int
name|compareOrThrow
parameter_list|(
name|Comparable
name|left
parameter_list|,
name|Comparable
name|right
parameter_list|)
block|{
return|return
name|left
operator|.
name|compareTo
argument_list|(
name|right
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
end_class

end_unit

