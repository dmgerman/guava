begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
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
name|java
operator|.
name|io
operator|.
name|Serializable
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
comment|/**  * A strategy for determining whether two instances are considered equivalent. Examples of  * equivalences are the {@linkplain #identity() identity equivalence} and {@linkplain #equals equals  * equivalence}.  *  * @author Bob Lee  * @author Ben Yu  * @author Gregory Kick  * @since 10.0 (<a href="http://code.google.com/p/guava-libraries/wiki/Compatibility"  *>mostly source-compatible</a> since 4.0)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Equivalence
specifier|public
specifier|abstract
class|class
name|Equivalence
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**    * Constructor for use by subclasses.    */
DECL|method|Equivalence ()
specifier|protected
name|Equivalence
parameter_list|()
block|{}
comment|/**    * Returns {@code true} if the given objects are considered equivalent.    *    *<p>The {@code equivalent} method implements an equivalence relation on object references:    *    *<ul>    *<li>It is<i>reflexive</i>: for any reference {@code x}, including null, {@code    *     equivalent(x, x)} returns {@code true}.    *<li>It is<i>symmetric</i>: for any references {@code x} and {@code y}, {@code    *     equivalent(x, y) == equivalent(y, x)}.    *<li>It is<i>transitive</i>: for any references {@code x}, {@code y}, and {@code z}, if    *     {@code equivalent(x, y)} returns {@code true} and {@code equivalent(y, z)} returns {@code    *     true}, then {@code equivalent(x, z)} returns {@code true}.    *<li>It is<i>consistent</i>: for any references {@code x} and {@code y}, multiple invocations    *     of {@code equivalent(x, y)} consistently return {@code true} or consistently return {@code    *     false} (provided that neither {@code x} nor {@code y} is modified).    *</ul>    */
DECL|method|equivalent (@ullable T a, @Nullable T b)
specifier|public
specifier|final
name|boolean
name|equivalent
parameter_list|(
annotation|@
name|Nullable
name|T
name|a
parameter_list|,
annotation|@
name|Nullable
name|T
name|b
parameter_list|)
block|{
if|if
condition|(
name|a
operator|==
name|b
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|a
operator|==
literal|null
operator|||
name|b
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|doEquivalent
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
return|;
block|}
comment|/**    * Returns {@code true} if {@code a} and {@code b} are considered equivalent.    *    *<p>Called by {@link #equivalent}. {@code a} and {@code b} are not the same    * object and are not nulls.    *    * @since 10.0 (previously, subclasses would override equivalent())    */
DECL|method|doEquivalent (T a, T b)
specifier|protected
specifier|abstract
name|boolean
name|doEquivalent
parameter_list|(
name|T
name|a
parameter_list|,
name|T
name|b
parameter_list|)
function_decl|;
comment|/**    * Returns a hash code for {@code t}.    *    *<p>The {@code hash} has the following properties:    *<ul>    *<li>It is<i>consistent</i>: for any reference {@code x}, multiple invocations of    *     {@code hash(x}} consistently return the same value provided {@code x} remains unchanged    *     according to the definition of the equivalence. The hash need not remain consistent from    *     one execution of an application to another execution of the same application.    *<li>It is<i>distributable accross equivalence</i>: for any references {@code x} and {@code y},    *     if {@code equivalent(x, y)}, then {@code hash(x) == hash(y)}. It is<i>not</i> necessary    *     that the hash be distributable accorss<i>inequivalence</i>. If {@code equivalence(x, y)}    *     is false, {@code hash(x) == hash(y)} may still be true.    *<li>{@code hash(null)} is {@code 0}.    *</ul>    */
DECL|method|hash (@ullable T t)
specifier|public
specifier|final
name|int
name|hash
parameter_list|(
annotation|@
name|Nullable
name|T
name|t
parameter_list|)
block|{
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|doHash
argument_list|(
name|t
argument_list|)
return|;
block|}
comment|/**    * Returns a hash code for non-null object {@code t}.    *    *<p>Called by {@link #hash}.    *    * @since 10.0 (previously, subclasses would override hash())    */
DECL|method|doHash (T t)
specifier|protected
specifier|abstract
name|int
name|doHash
parameter_list|(
name|T
name|t
parameter_list|)
function_decl|;
comment|/**    * Returns a new equivalence relation for {@code F} which evaluates equivalence by first applying    * {@code function} to the argument, then evaluating using {@code this}. That is, for any pair of    * non-null objects {@code x} and {@code y}, {@code    * equivalence.onResultOf(function).equivalent(a, b)} is true if and only if {@code    * equivalence.equivalent(function.apply(a), function.apply(b))} is true.    *    *<p>For example:    *    *<pre>   {@code    *    Equivalence<Person> SAME_AGE = Equivalence.equals().onResultOf(GET_PERSON_AGE);}</pre>    *     *<p>{@code function} will never be invoked with a null value.    *     *<p>Note that {@code function} must be consistent according to {@code this} equivalence    * relation. That is, invoking {@link Function#apply} multiple times for a given value must return    * equivalent results.    * For example, {@code Equivalence.identity().onResultOf(Functions.toStringFunction())} is broken    * because it's not guaranteed that {@link Object#toString}) always returns the same string    * instance.    *     * @since 10.0    */
DECL|method|onResultOf (Function<F, ? extends T> function)
specifier|public
specifier|final
parameter_list|<
name|F
parameter_list|>
name|Equivalence
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
name|FunctionalEquivalence
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
comment|/**    * Returns a wrapper of {@code reference} that implements    * {@link Wrapper#equals(Object) Object.equals()} such that    * {@code wrap(a).equals(wrap(b))} if and only if {@code equivalent(a, b)}.    *     * @since 10.0    */
DECL|method|wrap (@ullable S reference)
specifier|public
specifier|final
parameter_list|<
name|S
extends|extends
name|T
parameter_list|>
name|Wrapper
argument_list|<
name|S
argument_list|>
name|wrap
parameter_list|(
annotation|@
name|Nullable
name|S
name|reference
parameter_list|)
block|{
return|return
operator|new
name|Wrapper
argument_list|<
name|S
argument_list|>
argument_list|(
name|this
argument_list|,
name|reference
argument_list|)
return|;
block|}
comment|/**    * Wraps an object so that {@link #equals(Object)} and {@link #hashCode()} delegate to an    * {@link Equivalence}.    *    *<p>For example, given an {@link Equivalence} for {@link String strings} named {@code equiv}    * that tests equivalence using their lengths:    *    *<pre>   {@code    *   equiv.wrap("a").equals(equiv.wrap("b")) // true    *   equiv.wrap("a").equals(equiv.wrap("hello")) // false}</pre>    *    *<p>Note in particular that an equivalence wrapper is never equal to the object it wraps.    *    *<pre>   {@code    *   equiv.wrap(obj).equals(obj) // always false}</pre>    *    * @since 10.0    */
DECL|class|Wrapper
specifier|public
specifier|static
specifier|final
class|class
name|Wrapper
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Serializable
block|{
DECL|field|equivalence
specifier|private
specifier|final
name|Equivalence
argument_list|<
name|?
super|super
name|T
argument_list|>
name|equivalence
decl_stmt|;
DECL|field|reference
annotation|@
name|Nullable
specifier|private
specifier|final
name|T
name|reference
decl_stmt|;
DECL|method|Wrapper (Equivalence<? super T> equivalence, @Nullable T reference)
specifier|private
name|Wrapper
parameter_list|(
name|Equivalence
argument_list|<
name|?
super|super
name|T
argument_list|>
name|equivalence
parameter_list|,
annotation|@
name|Nullable
name|T
name|reference
parameter_list|)
block|{
name|this
operator|.
name|equivalence
operator|=
name|checkNotNull
argument_list|(
name|equivalence
argument_list|)
expr_stmt|;
name|this
operator|.
name|reference
operator|=
name|reference
expr_stmt|;
block|}
comment|/** Returns the (possibly null) reference wrapped by this instance. */
DECL|method|get ()
annotation|@
name|Nullable
specifier|public
name|T
name|get
parameter_list|()
block|{
return|return
name|reference
return|;
block|}
comment|/**      * Returns {@code true} if {@link Equivalence#equivalent(Object, Object)} applied to the wrapped      * references is {@code true} and both wrappers use the {@link Object#equals(Object) same}      * equivalence.      */
DECL|method|equals (@ullable Object obj)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|Wrapper
condition|)
block|{
name|Wrapper
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|Wrapper
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
comment|// note: not necessarily a Wrapper<T>
if|if
condition|(
name|this
operator|.
name|equivalence
operator|.
name|equals
argument_list|(
name|that
operator|.
name|equivalence
argument_list|)
condition|)
block|{
comment|/*            * We'll accept that as sufficient "proof" that either equivalence should be able to            * handle either reference, so it's safe to circumvent compile-time type checking.            */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Equivalence
argument_list|<
name|Object
argument_list|>
name|equivalence
init|=
operator|(
name|Equivalence
argument_list|<
name|Object
argument_list|>
operator|)
name|this
operator|.
name|equivalence
decl_stmt|;
return|return
name|equivalence
operator|.
name|equivalent
argument_list|(
name|this
operator|.
name|reference
argument_list|,
name|that
operator|.
name|reference
argument_list|)
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Returns the result of {@link Equivalence#hash(Object)} applied to the wrapped reference.      */
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|equivalence
operator|.
name|hash
argument_list|(
name|reference
argument_list|)
return|;
block|}
comment|/**      * Returns a string representation for this equivalence wrapper. The form of this string      * representation is not specified.      */
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|equivalence
operator|+
literal|".wrap("
operator|+
name|reference
operator|+
literal|")"
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
comment|/**    * Returns an equivalence over iterables based on the equivalence of their elements.  More    * specifically, two iterables are considered equivalent if they both contain the same number of    * elements, and each pair of corresponding elements is equivalent according to    * {@code this}.  Null iterables are equivalent to one another.    *     *<p>Note that this method performs a similar function for equivalences as {@link    * com.google.common.collect.Ordering#lexicographical} does for orderings.    *    * @since 10.0    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|pairwise ()
specifier|public
specifier|final
parameter_list|<
name|S
extends|extends
name|T
parameter_list|>
name|Equivalence
argument_list|<
name|Iterable
argument_list|<
name|S
argument_list|>
argument_list|>
name|pairwise
parameter_list|()
block|{
comment|// Ideally, the returned equivalence would support Iterable<? extends T>. However,
comment|// the need for this is so rare that it's not worth making callers deal with the ugly wildcard.
return|return
operator|new
name|PairwiseEquivalence
argument_list|<
name|S
argument_list|>
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**    * Returns a predicate that evaluates to true if and only if the input is    * equivalent to {@code target} according to this equivalence relation.    *     * @since 10.0    */
annotation|@
name|Beta
DECL|method|equivalentTo (@ullable T target)
specifier|public
specifier|final
name|Predicate
argument_list|<
name|T
argument_list|>
name|equivalentTo
parameter_list|(
annotation|@
name|Nullable
name|T
name|target
parameter_list|)
block|{
return|return
operator|new
name|EquivalentToPredicate
argument_list|<
name|T
argument_list|>
argument_list|(
name|this
argument_list|,
name|target
argument_list|)
return|;
block|}
DECL|class|EquivalentToPredicate
specifier|private
specifier|static
specifier|final
class|class
name|EquivalentToPredicate
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Predicate
argument_list|<
name|T
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|equivalence
specifier|private
specifier|final
name|Equivalence
argument_list|<
name|T
argument_list|>
name|equivalence
decl_stmt|;
DECL|field|target
annotation|@
name|Nullable
specifier|private
specifier|final
name|T
name|target
decl_stmt|;
DECL|method|EquivalentToPredicate (Equivalence<T> equivalence, @Nullable T target)
name|EquivalentToPredicate
parameter_list|(
name|Equivalence
argument_list|<
name|T
argument_list|>
name|equivalence
parameter_list|,
annotation|@
name|Nullable
name|T
name|target
parameter_list|)
block|{
name|this
operator|.
name|equivalence
operator|=
name|checkNotNull
argument_list|(
name|equivalence
argument_list|)
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
block|}
DECL|method|apply (@ullable T input)
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
annotation|@
name|Nullable
name|T
name|input
parameter_list|)
block|{
return|return
name|equivalence
operator|.
name|equivalent
argument_list|(
name|input
argument_list|,
name|target
argument_list|)
return|;
block|}
DECL|method|equals (@ullable Object obj)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|EquivalentToPredicate
condition|)
block|{
name|EquivalentToPredicate
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|EquivalentToPredicate
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|equivalence
operator|.
name|equals
argument_list|(
name|that
operator|.
name|equivalence
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|target
argument_list|,
name|that
operator|.
name|target
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hashCode
argument_list|(
name|equivalence
argument_list|,
name|target
argument_list|)
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
name|equivalence
operator|+
literal|".equivalentTo("
operator|+
name|target
operator|+
literal|")"
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
comment|/**    * Returns an equivalence that delegates to {@link Object#equals} and {@link Object#hashCode}.    * {@link Equivalence#equivalent} returns {@code true} if both values are null, or if neither    * value is null and {@link Object#equals} returns {@code true}. {@link Equivalence#hash} returns    * {@code 0} if passed a null value.    *    * @since 13.0    * @since 8.0 (in Equivalences with null-friendly behavior)    * @since 4.0 (in Equivalences)    */
DECL|method|equals ()
specifier|public
specifier|static
name|Equivalence
argument_list|<
name|Object
argument_list|>
name|equals
parameter_list|()
block|{
return|return
name|Equals
operator|.
name|INSTANCE
return|;
block|}
comment|/**    * Returns an equivalence that uses {@code ==} to compare values and {@link    * System#identityHashCode(Object)} to compute the hash code.  {@link Equivalence#equivalent}    * returns {@code true} if {@code a == b}, including in the case that a and b are both null.    *    * @since 13.0    * @since 4.0 (in Equivalences)    */
DECL|method|identity ()
specifier|public
specifier|static
name|Equivalence
argument_list|<
name|Object
argument_list|>
name|identity
parameter_list|()
block|{
return|return
name|Identity
operator|.
name|INSTANCE
return|;
block|}
DECL|class|Equals
specifier|static
specifier|final
class|class
name|Equals
extends|extends
name|Equivalence
argument_list|<
name|Object
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|INSTANCE
specifier|static
specifier|final
name|Equals
name|INSTANCE
init|=
operator|new
name|Equals
argument_list|()
decl_stmt|;
DECL|method|doEquivalent (Object a, Object b)
annotation|@
name|Override
specifier|protected
name|boolean
name|doEquivalent
parameter_list|(
name|Object
name|a
parameter_list|,
name|Object
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|equals
argument_list|(
name|b
argument_list|)
return|;
block|}
DECL|method|doHash (Object o)
annotation|@
name|Override
specifier|public
name|int
name|doHash
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|.
name|hashCode
argument_list|()
return|;
block|}
DECL|method|readResolve ()
specifier|private
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1
decl_stmt|;
block|}
DECL|class|Identity
specifier|static
specifier|final
class|class
name|Identity
extends|extends
name|Equivalence
argument_list|<
name|Object
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|INSTANCE
specifier|static
specifier|final
name|Identity
name|INSTANCE
init|=
operator|new
name|Identity
argument_list|()
decl_stmt|;
DECL|method|doEquivalent (Object a, Object b)
annotation|@
name|Override
specifier|protected
name|boolean
name|doEquivalent
parameter_list|(
name|Object
name|a
parameter_list|,
name|Object
name|b
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
DECL|method|doHash (Object o)
annotation|@
name|Override
specifier|protected
name|int
name|doHash
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|System
operator|.
name|identityHashCode
argument_list|(
name|o
argument_list|)
return|;
block|}
DECL|method|readResolve ()
specifier|private
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1
decl_stmt|;
block|}
block|}
end_class

end_unit

