begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Static utility methods pertaining to {@code Predicate} instances.  *  *<p>All methods returns serializable predicates as long as they're given  * serializable parameters.  *  *<p>See the Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/FunctionalExplained">the  * use of {@code Predicate}</a>.  *  * @author Kevin Bourrillion  * @since 2.0 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|Predicates
specifier|public
specifier|final
class|class
name|Predicates
block|{
DECL|method|Predicates ()
specifier|private
name|Predicates
parameter_list|()
block|{}
comment|// TODO(kevinb): considering having these implement a VisitablePredicate
comment|// interface which specifies an accept(PredicateVisitor) method.
comment|/**    * Returns a predicate that always evaluates to {@code true}.    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|alwaysTrue ()
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|alwaysTrue
parameter_list|()
block|{
return|return
name|ObjectPredicate
operator|.
name|ALWAYS_TRUE
operator|.
name|withNarrowedType
argument_list|()
return|;
block|}
comment|/**    * Returns a predicate that always evaluates to {@code false}.    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|alwaysFalse ()
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|alwaysFalse
parameter_list|()
block|{
return|return
name|ObjectPredicate
operator|.
name|ALWAYS_FALSE
operator|.
name|withNarrowedType
argument_list|()
return|;
block|}
comment|/**    * Returns a predicate that evaluates to {@code true} if the object reference    * being tested is null.    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|isNull ()
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|isNull
parameter_list|()
block|{
return|return
name|ObjectPredicate
operator|.
name|IS_NULL
operator|.
name|withNarrowedType
argument_list|()
return|;
block|}
comment|/**    * Returns a predicate that evaluates to {@code true} if the object reference    * being tested is not null.    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|method|notNull ()
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|notNull
parameter_list|()
block|{
return|return
name|ObjectPredicate
operator|.
name|NOT_NULL
operator|.
name|withNarrowedType
argument_list|()
return|;
block|}
comment|/**    * Returns a predicate that evaluates to {@code true} if the given predicate    * evaluates to {@code false}.    */
DECL|method|not (Predicate<T> predicate)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|not
parameter_list|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|predicate
parameter_list|)
block|{
return|return
operator|new
name|NotPredicate
argument_list|<
name|T
argument_list|>
argument_list|(
name|predicate
argument_list|)
return|;
block|}
comment|/**    * Returns a predicate that evaluates to {@code true} if each of its    * components evaluates to {@code true}. The components are evaluated in    * order, and evaluation will be "short-circuited" as soon as a false    * predicate is found. It defensively copies the iterable passed in, so future    * changes to it won't alter the behavior of this predicate. If {@code    * components} is empty, the returned predicate will always evaluate to {@code    * true}.    */
DECL|method|and ( Iterable<? extends Predicate<? super T>> components)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|and
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|Predicate
argument_list|<
name|?
super|super
name|T
argument_list|>
argument_list|>
name|components
parameter_list|)
block|{
return|return
operator|new
name|AndPredicate
argument_list|<
name|T
argument_list|>
argument_list|(
name|defensiveCopy
argument_list|(
name|components
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a predicate that evaluates to {@code true} if each of its    * components evaluates to {@code true}. The components are evaluated in    * order, and evaluation will be "short-circuited" as soon as a false    * predicate is found. It defensively copies the array passed in, so future    * changes to it won't alter the behavior of this predicate. If {@code    * components} is empty, the returned predicate will always evaluate to {@code    * true}.    */
DECL|method|and (Predicate<? super T>.... components)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|and
parameter_list|(
name|Predicate
argument_list|<
name|?
super|super
name|T
argument_list|>
modifier|...
name|components
parameter_list|)
block|{
return|return
operator|new
name|AndPredicate
argument_list|<
name|T
argument_list|>
argument_list|(
name|defensiveCopy
argument_list|(
name|components
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a predicate that evaluates to {@code true} if both of its    * components evaluate to {@code true}. The components are evaluated in    * order, and evaluation will be "short-circuited" as soon as a false    * predicate is found.    */
DECL|method|and (Predicate<? super T> first, Predicate<? super T> second)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|and
parameter_list|(
name|Predicate
argument_list|<
name|?
super|super
name|T
argument_list|>
name|first
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|T
argument_list|>
name|second
parameter_list|)
block|{
return|return
operator|new
name|AndPredicate
argument_list|<
name|T
argument_list|>
argument_list|(
name|Predicates
operator|.
expr|<
name|T
operator|>
name|asList
argument_list|(
name|checkNotNull
argument_list|(
name|first
argument_list|)
argument_list|,
name|checkNotNull
argument_list|(
name|second
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a predicate that evaluates to {@code true} if any one of its    * components evaluates to {@code true}. The components are evaluated in    * order, and evaluation will be "short-circuited" as soon as a    * true predicate is found. It defensively copies the iterable passed in, so    * future changes to it won't alter the behavior of this predicate. If {@code    * components} is empty, the returned predicate will always evaluate to {@code    * false}.    */
DECL|method|or ( Iterable<? extends Predicate<? super T>> components)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|or
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|Predicate
argument_list|<
name|?
super|super
name|T
argument_list|>
argument_list|>
name|components
parameter_list|)
block|{
return|return
operator|new
name|OrPredicate
argument_list|<
name|T
argument_list|>
argument_list|(
name|defensiveCopy
argument_list|(
name|components
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a predicate that evaluates to {@code true} if any one of its    * components evaluates to {@code true}. The components are evaluated in    * order, and evaluation will be "short-circuited" as soon as a    * true predicate is found. It defensively copies the array passed in, so    * future changes to it won't alter the behavior of this predicate. If {@code    * components} is empty, the returned predicate will always evaluate to {@code    * false}.    */
DECL|method|or (Predicate<? super T>.... components)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|or
parameter_list|(
name|Predicate
argument_list|<
name|?
super|super
name|T
argument_list|>
modifier|...
name|components
parameter_list|)
block|{
return|return
operator|new
name|OrPredicate
argument_list|<
name|T
argument_list|>
argument_list|(
name|defensiveCopy
argument_list|(
name|components
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a predicate that evaluates to {@code true} if either of its    * components evaluates to {@code true}. The components are evaluated in    * order, and evaluation will be "short-circuited" as soon as a    * true predicate is found.    */
DECL|method|or ( Predicate<? super T> first, Predicate<? super T> second)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|or
parameter_list|(
name|Predicate
argument_list|<
name|?
super|super
name|T
argument_list|>
name|first
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|T
argument_list|>
name|second
parameter_list|)
block|{
return|return
operator|new
name|OrPredicate
argument_list|<
name|T
argument_list|>
argument_list|(
name|Predicates
operator|.
expr|<
name|T
operator|>
name|asList
argument_list|(
name|checkNotNull
argument_list|(
name|first
argument_list|)
argument_list|,
name|checkNotNull
argument_list|(
name|second
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a predicate that evaluates to {@code true} if the object being    * tested {@code equals()} the given target or both are null.    */
DECL|method|equalTo (@ullable T target)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|equalTo
parameter_list|(
annotation|@
name|Nullable
name|T
name|target
parameter_list|)
block|{
return|return
operator|(
name|target
operator|==
literal|null
operator|)
condition|?
name|Predicates
operator|.
expr|<
name|T
operator|>
name|isNull
argument_list|()
else|:
operator|new
name|IsEqualToPredicate
argument_list|<
name|T
argument_list|>
argument_list|(
name|target
argument_list|)
return|;
block|}
comment|/**    * Returns a predicate that evaluates to {@code true} if the object reference    * being tested is a member of the given collection. It does not defensively    * copy the collection passed in, so future changes to it will alter the    * behavior of the predicate.    *    *<p>This method can technically accept any {@code Collection<?>}, but using    * a typed collection helps prevent bugs. This approach doesn't block any    * potential users since it is always possible to use {@code    * Predicates.<Object>in()}.    *    * @param target the collection that may contain the function input    */
DECL|method|in (Collection<? extends T> target)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|in
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|target
parameter_list|)
block|{
return|return
operator|new
name|InPredicate
argument_list|<
name|T
argument_list|>
argument_list|(
name|target
argument_list|)
return|;
block|}
comment|/**    * Returns the composition of a function and a predicate. For every {@code x},    * the generated predicate returns {@code predicate(function(x))}.    *    * @return the composition of the provided function and predicate    */
DECL|method|compose ( Predicate<B> predicate, Function<A, ? extends B> function)
specifier|public
specifier|static
parameter_list|<
name|A
parameter_list|,
name|B
parameter_list|>
name|Predicate
argument_list|<
name|A
argument_list|>
name|compose
parameter_list|(
name|Predicate
argument_list|<
name|B
argument_list|>
name|predicate
parameter_list|,
name|Function
argument_list|<
name|A
argument_list|,
name|?
extends|extends
name|B
argument_list|>
name|function
parameter_list|)
block|{
return|return
operator|new
name|CompositionPredicate
argument_list|<
name|A
argument_list|,
name|B
argument_list|>
argument_list|(
name|predicate
argument_list|,
name|function
argument_list|)
return|;
block|}
comment|// End public API, begin private implementation classes.
comment|// Package private for GWT serialization.
DECL|enum|ObjectPredicate
enum|enum
name|ObjectPredicate
implements|implements
name|Predicate
argument_list|<
name|Object
argument_list|>
block|{
DECL|enumConstant|ALWAYS_TRUE
name|ALWAYS_TRUE
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|,
DECL|enumConstant|ALWAYS_FALSE
name|ALWAYS_FALSE
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|,
DECL|enumConstant|IS_NULL
name|IS_NULL
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|==
literal|null
return|;
block|}
block|}
block|,
DECL|enumConstant|NOT_NULL
name|NOT_NULL
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|!=
literal|null
return|;
block|}
block|}
block|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// these Object predicates work for any T
DECL|method|withNarrowedType ()
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|withNarrowedType
parameter_list|()
block|{
return|return
operator|(
name|Predicate
argument_list|<
name|T
argument_list|>
operator|)
name|this
return|;
block|}
block|}
comment|/** @see Predicates#not(Predicate) */
DECL|class|NotPredicate
specifier|private
specifier|static
class|class
name|NotPredicate
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
DECL|field|predicate
specifier|final
name|Predicate
argument_list|<
name|T
argument_list|>
name|predicate
decl_stmt|;
DECL|method|NotPredicate (Predicate<T> predicate)
name|NotPredicate
parameter_list|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|predicate
parameter_list|)
block|{
name|this
operator|.
name|predicate
operator|=
name|checkNotNull
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (T t)
specifier|public
name|boolean
name|apply
parameter_list|(
name|T
name|t
parameter_list|)
block|{
return|return
operator|!
name|predicate
operator|.
name|apply
argument_list|(
name|t
argument_list|)
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
operator|~
name|predicate
operator|.
name|hashCode
argument_list|()
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
name|obj
operator|instanceof
name|NotPredicate
condition|)
block|{
name|NotPredicate
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|NotPredicate
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|predicate
operator|.
name|equals
argument_list|(
name|that
operator|.
name|predicate
argument_list|)
return|;
block|}
return|return
literal|false
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
literal|"Not("
operator|+
name|predicate
operator|.
name|toString
argument_list|()
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
DECL|field|COMMA_JOINER
specifier|private
specifier|static
specifier|final
name|Joiner
name|COMMA_JOINER
init|=
name|Joiner
operator|.
name|on
argument_list|(
literal|","
argument_list|)
decl_stmt|;
comment|/** @see Predicates#and(Iterable) */
DECL|class|AndPredicate
specifier|private
specifier|static
class|class
name|AndPredicate
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
DECL|field|components
specifier|private
specifier|final
name|List
argument_list|<
name|?
extends|extends
name|Predicate
argument_list|<
name|?
super|super
name|T
argument_list|>
argument_list|>
name|components
decl_stmt|;
DECL|method|AndPredicate (List<? extends Predicate<? super T>> components)
specifier|private
name|AndPredicate
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|Predicate
argument_list|<
name|?
super|super
name|T
argument_list|>
argument_list|>
name|components
parameter_list|)
block|{
name|this
operator|.
name|components
operator|=
name|components
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (T t)
specifier|public
name|boolean
name|apply
parameter_list|(
name|T
name|t
parameter_list|)
block|{
comment|// Avoid using the Iterator to avoid generating garbage (issue 820).
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|components
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|components
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|apply
argument_list|(
name|t
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
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
comment|// add a random number to avoid collisions with OrPredicate
return|return
name|components
operator|.
name|hashCode
argument_list|()
operator|+
literal|0x12472c2c
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
name|obj
operator|instanceof
name|AndPredicate
condition|)
block|{
name|AndPredicate
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|AndPredicate
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|components
operator|.
name|equals
argument_list|(
name|that
operator|.
name|components
argument_list|)
return|;
block|}
return|return
literal|false
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
literal|"And("
operator|+
name|COMMA_JOINER
operator|.
name|join
argument_list|(
name|components
argument_list|)
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
comment|/** @see Predicates#or(Iterable) */
DECL|class|OrPredicate
specifier|private
specifier|static
class|class
name|OrPredicate
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
DECL|field|components
specifier|private
specifier|final
name|List
argument_list|<
name|?
extends|extends
name|Predicate
argument_list|<
name|?
super|super
name|T
argument_list|>
argument_list|>
name|components
decl_stmt|;
DECL|method|OrPredicate (List<? extends Predicate<? super T>> components)
specifier|private
name|OrPredicate
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|Predicate
argument_list|<
name|?
super|super
name|T
argument_list|>
argument_list|>
name|components
parameter_list|)
block|{
name|this
operator|.
name|components
operator|=
name|components
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (T t)
specifier|public
name|boolean
name|apply
parameter_list|(
name|T
name|t
parameter_list|)
block|{
comment|// Avoid using the Iterator to avoid generating garbage (issue 820).
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|components
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|components
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|apply
argument_list|(
name|t
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
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
comment|// add a random number to avoid collisions with AndPredicate
return|return
name|components
operator|.
name|hashCode
argument_list|()
operator|+
literal|0x053c91cf
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
name|obj
operator|instanceof
name|OrPredicate
condition|)
block|{
name|OrPredicate
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|OrPredicate
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|components
operator|.
name|equals
argument_list|(
name|that
operator|.
name|components
argument_list|)
return|;
block|}
return|return
literal|false
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
literal|"Or("
operator|+
name|COMMA_JOINER
operator|.
name|join
argument_list|(
name|components
argument_list|)
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
comment|/** @see Predicates#equalTo(Object) */
DECL|class|IsEqualToPredicate
specifier|private
specifier|static
class|class
name|IsEqualToPredicate
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
DECL|field|target
specifier|private
specifier|final
name|T
name|target
decl_stmt|;
DECL|method|IsEqualToPredicate (T target)
specifier|private
name|IsEqualToPredicate
parameter_list|(
name|T
name|target
parameter_list|)
block|{
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (T t)
specifier|public
name|boolean
name|apply
parameter_list|(
name|T
name|t
parameter_list|)
block|{
return|return
name|target
operator|.
name|equals
argument_list|(
name|t
argument_list|)
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
name|target
operator|.
name|hashCode
argument_list|()
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
name|obj
operator|instanceof
name|IsEqualToPredicate
condition|)
block|{
name|IsEqualToPredicate
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|IsEqualToPredicate
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|target
operator|.
name|equals
argument_list|(
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
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"IsEqualTo("
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
comment|/** @see Predicates#in(Collection) */
DECL|class|InPredicate
specifier|private
specifier|static
class|class
name|InPredicate
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
DECL|field|target
specifier|private
specifier|final
name|Collection
argument_list|<
name|?
argument_list|>
name|target
decl_stmt|;
DECL|method|InPredicate (Collection<?> target)
specifier|private
name|InPredicate
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|target
parameter_list|)
block|{
name|this
operator|.
name|target
operator|=
name|checkNotNull
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (T t)
specifier|public
name|boolean
name|apply
parameter_list|(
name|T
name|t
parameter_list|)
block|{
try|try
block|{
return|return
name|target
operator|.
name|contains
argument_list|(
name|t
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
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
name|obj
operator|instanceof
name|InPredicate
condition|)
block|{
name|InPredicate
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|InPredicate
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|target
operator|.
name|equals
argument_list|(
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
name|target
operator|.
name|hashCode
argument_list|()
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
literal|"In("
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
comment|/** @see Predicates#compose(Predicate, Function) */
DECL|class|CompositionPredicate
specifier|private
specifier|static
class|class
name|CompositionPredicate
parameter_list|<
name|A
parameter_list|,
name|B
parameter_list|>
implements|implements
name|Predicate
argument_list|<
name|A
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|p
specifier|final
name|Predicate
argument_list|<
name|B
argument_list|>
name|p
decl_stmt|;
DECL|field|f
specifier|final
name|Function
argument_list|<
name|A
argument_list|,
name|?
extends|extends
name|B
argument_list|>
name|f
decl_stmt|;
DECL|method|CompositionPredicate (Predicate<B> p, Function<A, ? extends B> f)
specifier|private
name|CompositionPredicate
parameter_list|(
name|Predicate
argument_list|<
name|B
argument_list|>
name|p
parameter_list|,
name|Function
argument_list|<
name|A
argument_list|,
name|?
extends|extends
name|B
argument_list|>
name|f
parameter_list|)
block|{
name|this
operator|.
name|p
operator|=
name|checkNotNull
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|this
operator|.
name|f
operator|=
name|checkNotNull
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (A a)
specifier|public
name|boolean
name|apply
parameter_list|(
name|A
name|a
parameter_list|)
block|{
return|return
name|p
operator|.
name|apply
argument_list|(
name|f
operator|.
name|apply
argument_list|(
name|a
argument_list|)
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
name|obj
operator|instanceof
name|CompositionPredicate
condition|)
block|{
name|CompositionPredicate
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|that
init|=
operator|(
name|CompositionPredicate
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|f
operator|.
name|equals
argument_list|(
name|that
operator|.
name|f
argument_list|)
operator|&&
name|p
operator|.
name|equals
argument_list|(
name|that
operator|.
name|p
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
name|f
operator|.
name|hashCode
argument_list|()
operator|^
name|p
operator|.
name|hashCode
argument_list|()
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
name|p
operator|.
name|toString
argument_list|()
operator|+
literal|"("
operator|+
name|f
operator|.
name|toString
argument_list|()
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|asList ( Predicate<? super T> first, Predicate<? super T> second)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|Predicate
argument_list|<
name|?
super|super
name|T
argument_list|>
argument_list|>
name|asList
parameter_list|(
name|Predicate
argument_list|<
name|?
super|super
name|T
argument_list|>
name|first
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|T
argument_list|>
name|second
parameter_list|)
block|{
return|return
name|Arrays
operator|.
expr|<
name|Predicate
argument_list|<
name|?
super|super
name|T
argument_list|>
operator|>
name|asList
argument_list|(
name|first
argument_list|,
name|second
argument_list|)
return|;
block|}
DECL|method|defensiveCopy (T... array)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|defensiveCopy
parameter_list|(
name|T
modifier|...
name|array
parameter_list|)
block|{
return|return
name|defensiveCopy
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|array
argument_list|)
argument_list|)
return|;
block|}
DECL|method|defensiveCopy (Iterable<T> iterable)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|defensiveCopy
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|iterable
parameter_list|)
block|{
name|ArrayList
argument_list|<
name|T
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|T
name|element
range|:
name|iterable
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|checkNotNull
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
block|}
end_class

end_unit

