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
name|checkArgument
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
name|Joiner
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Predicates
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
name|primitives
operator|.
name|Ints
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractCollection
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

begin_comment
comment|/**  * Provides static methods for working with {@code Collection} instances.  *  * @author Chris Povirk  * @author Mike Bostock  * @author Jared Levy  * @since Guava release 02 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Collections2
specifier|public
specifier|final
class|class
name|Collections2
block|{
DECL|method|Collections2 ()
specifier|private
name|Collections2
parameter_list|()
block|{}
comment|/**    * Returns the elements of {@code unfiltered} that satisfy a predicate. The    * returned collection is a live view of {@code unfiltered}; changes to one    * affect the other.    *    *<p>The resulting collection's iterator does not support {@code remove()},    * but all other collection methods are supported. When given an element that    * doesn't satisfy the predicate, the collection's {@code add()} and {@code    * addAll()} methods throw an {@link IllegalArgumentException}. When methods    * such as {@code removeAll()} and {@code clear()} are called on the filtered    * collection, only elements that satisfy the filter will be removed from the    * underlying collection.    *    *<p>The returned collection isn't threadsafe or serializable, even if    * {@code unfiltered} is.    *    *<p>Many of the filtered collection's methods, such as {@code size()},    * iterate across every element in the underlying collection and determine    * which elements satisfy the filter. When a live view is<i>not</i> needed,    * it may be faster to copy {@code Iterables.filter(unfiltered, predicate)}    * and use the copy.    *    *<p><b>Warning:</b> {@code predicate} must be<i>consistent with equals</i>,    * as documented at {@link Predicate#apply}. Do not provide a predicate such    * as {@code Predicates.instanceOf(ArrayList.class)}, which is inconsistent    * with equals. (See {@link Iterables#filter(Iterable, Class)} for related    * functionality.)    */
DECL|method|filter ( Collection<E> unfiltered, Predicate<? super E> predicate)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Collection
argument_list|<
name|E
argument_list|>
name|filter
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|unfiltered
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|E
argument_list|>
name|predicate
parameter_list|)
block|{
if|if
condition|(
name|unfiltered
operator|instanceof
name|FilteredCollection
condition|)
block|{
comment|// Support clear(), removeAll(), and retainAll() when filtering a filtered
comment|// collection.
return|return
operator|(
operator|(
name|FilteredCollection
argument_list|<
name|E
argument_list|>
operator|)
name|unfiltered
operator|)
operator|.
name|createCombined
argument_list|(
name|predicate
argument_list|)
return|;
block|}
return|return
operator|new
name|FilteredCollection
argument_list|<
name|E
argument_list|>
argument_list|(
name|checkNotNull
argument_list|(
name|unfiltered
argument_list|)
argument_list|,
name|checkNotNull
argument_list|(
name|predicate
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Delegates to {@link Collection#contains}.  Returns {@code false} on {@code    * ClassCastException}    */
DECL|method|safeContains (Collection<?> collection, Object object)
specifier|static
name|boolean
name|safeContains
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|,
name|Object
name|object
parameter_list|)
block|{
try|try
block|{
return|return
name|collection
operator|.
name|contains
argument_list|(
name|object
argument_list|)
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
DECL|class|FilteredCollection
specifier|static
class|class
name|FilteredCollection
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Collection
argument_list|<
name|E
argument_list|>
block|{
DECL|field|unfiltered
specifier|final
name|Collection
argument_list|<
name|E
argument_list|>
name|unfiltered
decl_stmt|;
DECL|field|predicate
specifier|final
name|Predicate
argument_list|<
name|?
super|super
name|E
argument_list|>
name|predicate
decl_stmt|;
DECL|method|FilteredCollection (Collection<E> unfiltered, Predicate<? super E> predicate)
name|FilteredCollection
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|unfiltered
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|E
argument_list|>
name|predicate
parameter_list|)
block|{
name|this
operator|.
name|unfiltered
operator|=
name|unfiltered
expr_stmt|;
name|this
operator|.
name|predicate
operator|=
name|predicate
expr_stmt|;
block|}
DECL|method|createCombined (Predicate<? super E> newPredicate)
name|FilteredCollection
argument_list|<
name|E
argument_list|>
name|createCombined
parameter_list|(
name|Predicate
argument_list|<
name|?
super|super
name|E
argument_list|>
name|newPredicate
parameter_list|)
block|{
return|return
operator|new
name|FilteredCollection
argument_list|<
name|E
argument_list|>
argument_list|(
name|unfiltered
argument_list|,
name|Predicates
operator|.
expr|<
name|E
operator|>
name|and
argument_list|(
name|predicate
argument_list|,
name|newPredicate
argument_list|)
argument_list|)
return|;
comment|// .<E> above needed to compile in JDK 5
block|}
annotation|@
name|Override
DECL|method|add (E element)
specifier|public
name|boolean
name|add
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|predicate
operator|.
name|apply
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|unfiltered
operator|.
name|add
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addAll (Collection<? extends E> collection)
specifier|public
name|boolean
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|collection
parameter_list|)
block|{
for|for
control|(
name|E
name|element
range|:
name|collection
control|)
block|{
name|checkArgument
argument_list|(
name|predicate
operator|.
name|apply
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|unfiltered
operator|.
name|addAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|Iterables
operator|.
name|removeIf
argument_list|(
name|unfiltered
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|contains (Object element)
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|element
parameter_list|)
block|{
try|try
block|{
comment|// unsafe cast can result in a CCE from predicate.apply(), which we
comment|// will catch
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|E
name|e
init|=
operator|(
name|E
operator|)
name|element
decl_stmt|;
comment|/*          * We check whether e satisfies the predicate, when we really mean to          * check whether the element contained in the set does. This is ok as          * long as the predicate is consistent with equals, as required.          */
return|return
name|predicate
operator|.
name|apply
argument_list|(
name|e
argument_list|)
operator|&&
name|unfiltered
operator|.
name|contains
argument_list|(
name|element
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
annotation|@
name|Override
DECL|method|containsAll (Collection<?> collection)
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
for|for
control|(
name|Object
name|element
range|:
name|collection
control|)
block|{
if|if
condition|(
operator|!
name|contains
argument_list|(
name|element
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
annotation|@
name|Override
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
operator|!
name|Iterators
operator|.
name|any
argument_list|(
name|unfiltered
operator|.
name|iterator
argument_list|()
argument_list|,
name|predicate
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|filter
argument_list|(
name|unfiltered
operator|.
name|iterator
argument_list|()
argument_list|,
name|predicate
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|remove (Object element)
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|element
parameter_list|)
block|{
try|try
block|{
comment|// unsafe cast can result in a CCE from predicate.apply(), which we
comment|// will catch
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|E
name|e
init|=
operator|(
name|E
operator|)
name|element
decl_stmt|;
comment|// See comment in contains() concerning predicate.apply(e)
return|return
name|predicate
operator|.
name|apply
argument_list|(
name|e
argument_list|)
operator|&&
name|unfiltered
operator|.
name|remove
argument_list|(
name|element
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
annotation|@
name|Override
DECL|method|removeAll (final Collection<?> collection)
specifier|public
name|boolean
name|removeAll
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|collection
argument_list|)
expr_stmt|;
name|Predicate
argument_list|<
name|E
argument_list|>
name|combinedPredicate
init|=
operator|new
name|Predicate
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|E
name|input
parameter_list|)
block|{
return|return
name|predicate
operator|.
name|apply
argument_list|(
name|input
argument_list|)
operator|&&
name|collection
operator|.
name|contains
argument_list|(
name|input
argument_list|)
return|;
block|}
block|}
decl_stmt|;
return|return
name|Iterables
operator|.
name|removeIf
argument_list|(
name|unfiltered
argument_list|,
name|combinedPredicate
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|retainAll (final Collection<?> collection)
specifier|public
name|boolean
name|retainAll
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|collection
argument_list|)
expr_stmt|;
name|Predicate
argument_list|<
name|E
argument_list|>
name|combinedPredicate
init|=
operator|new
name|Predicate
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|E
name|input
parameter_list|)
block|{
comment|// See comment in contains() concerning predicate.apply(e)
return|return
name|predicate
operator|.
name|apply
argument_list|(
name|input
argument_list|)
operator|&&
operator|!
name|collection
operator|.
name|contains
argument_list|(
name|input
argument_list|)
return|;
block|}
block|}
decl_stmt|;
return|return
name|Iterables
operator|.
name|removeIf
argument_list|(
name|unfiltered
argument_list|,
name|combinedPredicate
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|size
argument_list|(
name|iterator
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toArray ()
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
comment|// creating an ArrayList so filtering happens once
return|return
name|Lists
operator|.
name|newArrayList
argument_list|(
name|iterator
argument_list|()
argument_list|)
operator|.
name|toArray
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toArray (T[] array)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|toArray
parameter_list|(
name|T
index|[]
name|array
parameter_list|)
block|{
return|return
name|Lists
operator|.
name|newArrayList
argument_list|(
name|iterator
argument_list|()
argument_list|)
operator|.
name|toArray
argument_list|(
name|array
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
name|Iterators
operator|.
name|toString
argument_list|(
name|iterator
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**    * Returns a collection that applies {@code function} to each element of    * {@code fromCollection}. The returned collection is a live view of {@code    * fromCollection}; changes to one affect the other.    *    *<p>The returned collection's {@code add()} and {@code addAll()} methods    * throw an {@link UnsupportedOperationException}. All other collection    * methods are supported, as long as {@code fromCollection} supports them.    *    *<p>The returned collection isn't threadsafe or serializable, even if    * {@code fromCollection} is.    *    *<p>When a live view is<i>not</i> needed, it may be faster to copy the    * transformed collection and use the copy.    */
DECL|method|transform (Collection<F> fromCollection, Function<? super F, T> function)
specifier|public
specifier|static
parameter_list|<
name|F
parameter_list|,
name|T
parameter_list|>
name|Collection
argument_list|<
name|T
argument_list|>
name|transform
parameter_list|(
name|Collection
argument_list|<
name|F
argument_list|>
name|fromCollection
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|F
argument_list|,
name|T
argument_list|>
name|function
parameter_list|)
block|{
return|return
operator|new
name|TransformedCollection
argument_list|<
name|F
argument_list|,
name|T
argument_list|>
argument_list|(
name|fromCollection
argument_list|,
name|function
argument_list|)
return|;
block|}
DECL|class|TransformedCollection
specifier|static
class|class
name|TransformedCollection
parameter_list|<
name|F
parameter_list|,
name|T
parameter_list|>
extends|extends
name|AbstractCollection
argument_list|<
name|T
argument_list|>
block|{
DECL|field|fromCollection
specifier|final
name|Collection
argument_list|<
name|F
argument_list|>
name|fromCollection
decl_stmt|;
DECL|field|function
specifier|final
name|Function
argument_list|<
name|?
super|super
name|F
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|function
decl_stmt|;
DECL|method|TransformedCollection (Collection<F> fromCollection, Function<? super F, ? extends T> function)
name|TransformedCollection
parameter_list|(
name|Collection
argument_list|<
name|F
argument_list|>
name|fromCollection
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|F
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|function
parameter_list|)
block|{
name|this
operator|.
name|fromCollection
operator|=
name|checkNotNull
argument_list|(
name|fromCollection
argument_list|)
expr_stmt|;
name|this
operator|.
name|function
operator|=
name|checkNotNull
argument_list|(
name|function
argument_list|)
expr_stmt|;
block|}
DECL|method|clear ()
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|fromCollection
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|isEmpty ()
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|fromCollection
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|transform
argument_list|(
name|fromCollection
operator|.
name|iterator
argument_list|()
argument_list|,
name|function
argument_list|)
return|;
block|}
DECL|method|size ()
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|fromCollection
operator|.
name|size
argument_list|()
return|;
block|}
block|}
comment|/**    * Returns {@code true} if the collection {@code self} contains all of the    * elements in the collection {@code c}.    *    *<p>This method iterates over the specified collection {@code c}, checking    * each element returned by the iterator in turn to see if it is contained in    * the specified collection {@code self}. If all elements are so contained,    * {@code true} is returned, otherwise {@code false}.    *    * @param self a collection which might contain all elements in {@code c}    * @param c a collection whose elements might be contained by {@code self}    */
DECL|method|containsAllImpl (Collection<?> self, Collection<?> c)
specifier|static
name|boolean
name|containsAllImpl
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|self
parameter_list|,
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|self
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|c
control|)
block|{
if|if
condition|(
operator|!
name|self
operator|.
name|contains
argument_list|(
name|o
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
comment|/**    * An implementation of {@link Collection#toString()}.    */
DECL|method|toStringImpl (final Collection<?> collection)
specifier|static
name|String
name|toStringImpl
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
name|newStringBuilderForCollection
argument_list|(
name|collection
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
decl_stmt|;
name|STANDARD_JOINER
operator|.
name|appendTo
argument_list|(
name|sb
argument_list|,
name|Iterables
operator|.
name|transform
argument_list|(
name|collection
argument_list|,
operator|new
name|Function
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|apply
parameter_list|(
name|Object
name|input
parameter_list|)
block|{
return|return
name|input
operator|==
name|collection
condition|?
literal|"(this Collection)"
else|:
name|input
return|;
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**    * Returns best-effort-sized StringBuilder based on the given collection size.    */
DECL|method|newStringBuilderForCollection (int size)
specifier|static
name|StringBuilder
name|newStringBuilderForCollection
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|size
operator|>=
literal|0
argument_list|,
literal|"size must be non-negative"
argument_list|)
expr_stmt|;
return|return
operator|new
name|StringBuilder
argument_list|(
operator|(
name|int
operator|)
name|Math
operator|.
name|min
argument_list|(
name|size
operator|*
literal|8L
argument_list|,
name|Ints
operator|.
name|MAX_POWER_OF_TWO
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Used to avoid http://bugs.sun.com/view_bug.do?bug_id=6558557    */
DECL|method|cast (Iterable<T> iterable)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Collection
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
name|Collection
argument_list|<
name|T
argument_list|>
operator|)
name|iterable
return|;
block|}
DECL|field|STANDARD_JOINER
specifier|static
specifier|final
name|Joiner
name|STANDARD_JOINER
init|=
name|Joiner
operator|.
name|on
argument_list|(
literal|", "
argument_list|)
decl_stmt|;
comment|// TODO(user): Maybe move the mathematical methods to a separate
comment|// package-permission class.
block|}
end_class

end_unit

