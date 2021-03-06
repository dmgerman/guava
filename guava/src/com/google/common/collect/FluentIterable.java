begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|InlineMe
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
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
comment|/**  * A discouraged (but not deprecated) precursor to Java's superior {@link Stream} library.  *  *<p>The following types of methods are provided:  *  *<ul>  *<li>chaining methods which return a new {@code FluentIterable} based in some way on the  *       contents of the current one (for example {@link #transform})  *<li>element extraction methods which facilitate the retrieval of certain elements (for example  *       {@link #last})  *<li>query methods which answer questions about the {@code FluentIterable}'s contents (for  *       example {@link #anyMatch})  *<li>conversion methods which copy the {@code FluentIterable}'s contents into a new collection  *       or array (for example {@link #toList})  *</ul>  *  *<p>Several lesser-used features are currently available only as static methods on the {@link  * Iterables} class.  *  *<p><a id="streams"></a>  *  *<h3>Comparison to streams</h3>  *  *<p>{@link Stream} is similar to this class, but generally more powerful, and certainly more  * standard. Key differences include:  *  *<ul>  *<li>A stream is<i>single-use</i>; it becomes invalid as soon as any "terminal operation" such  *       as {@code findFirst()} or {@code iterator()} is invoked. (Even though {@code Stream}  *       contains all the right method<i>signatures</i> to implement {@link Iterable}, it does not  *       actually do so, to avoid implying repeat-iterability.) {@code FluentIterable}, on the other  *       hand, is multiple-use, and does implement {@link Iterable}.  *<li>Streams offer many features not found here, including {@code min/max}, {@code distinct},  *       {@code reduce}, {@code sorted}, the very powerful {@code collect}, and built-in support for  *       parallelizing stream operations.  *<li>{@code FluentIterable} contains several features not available on {@code Stream}, which are  *       noted in the method descriptions below.  *<li>Streams include primitive-specialized variants such as {@code IntStream}, the use of which  *       is strongly recommended.  *<li>Streams are standard Java, not requiring a third-party dependency.  *</ul>  *  *<h3>Example</h3>  *  *<p>Here is an example that accepts a list from a database call, filters it based on a predicate,  * transforms it by invoking {@code toString()} on each element, and returns the first 10 elements  * as a {@code List}:  *  *<pre>{@code  * ImmutableList<String> results =  *     FluentIterable.from(database.getClientList())  *         .filter(Client::isActiveInLastMonth)  *         .transform(Object::toString)  *         .limit(10)  *         .toList();  * }</pre>  *  * The approximate stream equivalent is:  *  *<pre>{@code  * List<String> results =  *     database.getClientList()  *         .stream()  *         .filter(Client::isActiveInLastMonth)  *         .map(Object::toString)  *         .limit(10)  *         .collect(Collectors.toList());  * }</pre>  *  * @author Marcin Mikosik  * @since 12.0  */
end_comment

begin_annotation
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|class|FluentIterable
specifier|public
specifier|abstract
name|class
name|FluentIterable
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
expr|implements
name|Iterable
argument_list|<
name|E
argument_list|>
block|{
comment|// We store 'iterable' and use it instead of 'this' to allow Iterables to perform instanceof
comment|// checks on the _original_ iterable when FluentIterable.from is used.
comment|// To avoid a self retain cycle under j2objc, we store Optional.absent() instead of
comment|// Optional.of(this). To access the delegate iterable, call #getDelegate(), which converts to
comment|// absent() back to 'this'.
DECL|field|iterableDelegate
specifier|private
name|final
name|Optional
argument_list|<
name|Iterable
argument_list|<
name|E
argument_list|>
argument_list|>
name|iterableDelegate
block|;
comment|/** Constructor for use by subclasses. */
DECL|method|FluentIterable ()
specifier|protected
name|FluentIterable
argument_list|()
block|{
name|this
operator|.
name|iterableDelegate
operator|=
name|Optional
operator|.
name|absent
argument_list|()
block|;   }
DECL|method|FluentIterable (Iterable<E> iterable)
name|FluentIterable
argument_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|iterable
argument_list|)
block|{
name|this
operator|.
name|iterableDelegate
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|iterable
argument_list|)
block|;   }
DECL|method|getDelegate ()
specifier|private
name|Iterable
argument_list|<
name|E
argument_list|>
name|getDelegate
argument_list|()
block|{
return|return
name|iterableDelegate
operator|.
name|or
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**    * Returns a fluent iterable that wraps {@code iterable}, or {@code iterable} itself if it is    * already a {@code FluentIterable}.    *    *<p><b>{@code Stream} equivalent:</b> {@link Collection#stream} if {@code iterable} is a {@link    * Collection}; {@link Streams#stream(Iterable)} otherwise.    */
DECL|method|from (final Iterable<E> iterable)
specifier|public
specifier|static
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
name|FluentIterable
argument_list|<
name|E
argument_list|>
name|from
argument_list|(
name|final
name|Iterable
argument_list|<
name|E
argument_list|>
name|iterable
argument_list|)
block|{
return|return
operator|(
name|iterable
operator|instanceof
name|FluentIterable
operator|)
operator|?
operator|(
name|FluentIterable
argument_list|<
name|E
argument_list|>
operator|)
name|iterable
operator|:
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
end_expr_stmt

begin_comment
unit|};   }
comment|/**    * Returns a fluent iterable containing {@code elements} in the specified order.    *    *<p>The returned iterable is an unmodifiable view of the input array.    *    *<p><b>{@code Stream} equivalent:</b> {@link java.util.stream.Stream#of(Object[])    * Stream.of(T...)}.    *    * @since 20.0 (since 18.0 as an overload of {@code of})    */
end_comment

begin_expr_stmt
unit|@
name|Beta
DECL|method|from (E[] elements)
specifier|public
specifier|static
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
name|FluentIterable
argument_list|<
name|E
argument_list|>
name|from
argument_list|(
name|E
index|[]
name|elements
argument_list|)
block|{
return|return
name|from
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
end_expr_stmt

begin_comment
comment|/**    * Construct a fluent iterable from another fluent iterable. This is obviously never necessary,    * but is intended to help call out cases where one migration from {@code Iterable} to {@code    * FluentIterable} has obviated the need to explicitly convert to a {@code FluentIterable}.    *    * @deprecated instances of {@code FluentIterable} don't need to be converted to {@code    *     FluentIterable}    */
end_comment

begin_annotation
annotation|@
name|Deprecated
end_annotation

begin_annotation
annotation|@
name|InlineMe
argument_list|(
name|replacement
operator|=
literal|"checkNotNull(iterable)"
argument_list|,
name|staticImports
operator|=
block|{
literal|"com.google.common.base.Preconditions.checkNotNull"
block|}
argument_list|)
end_annotation

begin_expr_stmt
DECL|method|from (FluentIterable<E> iterable)
specifier|public
specifier|static
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
name|FluentIterable
argument_list|<
name|E
argument_list|>
name|from
argument_list|(
name|FluentIterable
argument_list|<
name|E
argument_list|>
name|iterable
argument_list|)
block|{
return|return
name|checkNotNull
argument_list|(
name|iterable
argument_list|)
return|;
block|}
end_expr_stmt

begin_comment
comment|/**    * Returns a fluent iterable that combines two iterables. The returned iterable has an iterator    * that traverses the elements in {@code a}, followed by the elements in {@code b}. The source    * iterators are not polled until necessary.    *    *<p>The returned iterable's iterator supports {@code remove()} when the corresponding input    * iterator supports it.    *    *<p><b>{@code Stream} equivalent:</b> {@link Stream#concat}.    *    * @since 20.0    */
end_comment

begin_annotation
annotation|@
name|Beta
end_annotation

begin_expr_stmt
DECL|method|concat ( Iterable<? extends T> a, Iterable<? extends T> b)
specifier|public
specifier|static
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|FluentIterable
argument_list|<
name|T
argument_list|>
name|concat
argument_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|a
operator|,
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|b
argument_list|)
block|{
return|return
name|concatNoDefensiveCopy
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
return|;
block|}
end_expr_stmt

begin_comment
comment|/**    * Returns a fluent iterable that combines three iterables. The returned iterable has an iterator    * that traverses the elements in {@code a}, followed by the elements in {@code b}, followed by    * the elements in {@code c}. The source iterators are not polled until necessary.    *    *<p>The returned iterable's iterator supports {@code remove()} when the corresponding input    * iterator supports it.    *    *<p><b>{@code Stream} equivalent:</b> use nested calls to {@link Stream#concat}, or see the    * advice in {@link #concat(Iterable...)}.    *    * @since 20.0    */
end_comment

begin_annotation
annotation|@
name|Beta
end_annotation

begin_expr_stmt
DECL|method|concat ( Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c)
specifier|public
specifier|static
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|FluentIterable
argument_list|<
name|T
argument_list|>
name|concat
argument_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|a
operator|,
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|b
operator|,
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|c
argument_list|)
block|{
return|return
name|concatNoDefensiveCopy
argument_list|(
name|a
argument_list|,
name|b
argument_list|,
name|c
argument_list|)
return|;
block|}
end_expr_stmt

begin_comment
comment|/**    * Returns a fluent iterable that combines four iterables. The returned iterable has an iterator    * that traverses the elements in {@code a}, followed by the elements in {@code b}, followed by    * the elements in {@code c}, followed by the elements in {@code d}. The source iterators are not    * polled until necessary.    *    *<p>The returned iterable's iterator supports {@code remove()} when the corresponding input    * iterator supports it.    *    *<p><b>{@code Stream} equivalent:</b> use nested calls to {@link Stream#concat}, or see the    * advice in {@link #concat(Iterable...)}.    *    * @since 20.0    */
end_comment

begin_annotation
annotation|@
name|Beta
end_annotation

begin_expr_stmt
DECL|method|concat ( Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c, Iterable<? extends T> d)
specifier|public
specifier|static
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|FluentIterable
argument_list|<
name|T
argument_list|>
name|concat
argument_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|a
operator|,
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|b
operator|,
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|c
operator|,
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|d
argument_list|)
block|{
return|return
name|concatNoDefensiveCopy
argument_list|(
name|a
argument_list|,
name|b
argument_list|,
name|c
argument_list|,
name|d
argument_list|)
return|;
block|}
end_expr_stmt

begin_comment
comment|/**    * Returns a fluent iterable that combines several iterables. The returned iterable has an    * iterator that traverses the elements of each iterable in {@code inputs}. The input iterators    * are not polled until necessary.    *    *<p>The returned iterable's iterator supports {@code remove()} when the corresponding input    * iterator supports it.    *    *<p><b>{@code Stream} equivalent:</b> to concatenate an arbitrary number of streams, use {@code    * Stream.of(stream1, stream2, ...).flatMap(s -> s)}. If the sources are iterables, use {@code    * Stream.of(iter1, iter2, ...).flatMap(Streams::stream)}.    *    * @throws NullPointerException if any of the provided iterables is {@code null}    * @since 20.0    */
end_comment

begin_annotation
annotation|@
name|Beta
end_annotation

begin_expr_stmt
DECL|method|concat ( Iterable<? extends T>.... inputs)
specifier|public
specifier|static
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|FluentIterable
argument_list|<
name|T
argument_list|>
name|concat
argument_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
operator|...
name|inputs
argument_list|)
block|{
return|return
name|concatNoDefensiveCopy
argument_list|(
name|Arrays
operator|.
name|copyOf
argument_list|(
name|inputs
argument_list|,
name|inputs
operator|.
name|length
argument_list|)
argument_list|)
return|;
block|}
end_expr_stmt

begin_comment
comment|/**    * Returns a fluent iterable that combines several iterables. The returned iterable has an    * iterator that traverses the elements of each iterable in {@code inputs}. The input iterators    * are not polled until necessary.    *    *<p>The returned iterable's iterator supports {@code remove()} when the corresponding input    * iterator supports it. The methods of the returned iterable may throw {@code    * NullPointerException} if any of the input iterators is {@code null}.    *    *<p><b>{@code Stream} equivalent:</b> {@code streamOfStreams.flatMap(s -> s)} or {@code    * streamOfIterables.flatMap(Streams::stream)}. (See {@link Streams#stream}.)    *    * @since 20.0    */
end_comment

begin_annotation
annotation|@
name|Beta
end_annotation

begin_expr_stmt
DECL|method|concat ( final Iterable<? extends Iterable<? extends T>> inputs)
specifier|public
specifier|static
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|FluentIterable
argument_list|<
name|T
argument_list|>
name|concat
argument_list|(
name|final
name|Iterable
argument_list|<
name|?
extends|extends
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|inputs
argument_list|)
block|{
name|checkNotNull
argument_list|(
name|inputs
argument_list|)
block|;
return|return
operator|new
name|FluentIterable
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
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
name|concat
argument_list|(
name|Iterators
operator|.
name|transform
argument_list|(
name|inputs
operator|.
name|iterator
argument_list|()
argument_list|,
name|Iterables
operator|.
expr|<
name|T
operator|>
name|toIterator
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
end_expr_stmt

begin_comment
unit|};   }
comment|/** Concatenates a varargs array of iterables without making a defensive copy of the array. */
end_comment

begin_expr_stmt
DECL|method|concatNoDefensiveCopy ( final Iterable<? extends T>... inputs)
unit|private
specifier|static
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|FluentIterable
argument_list|<
name|T
argument_list|>
name|concatNoDefensiveCopy
argument_list|(
name|final
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
operator|...
name|inputs
argument_list|)
block|{
for|for
control|(
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|input
range|:
name|inputs
control|)
block|{
name|checkNotNull
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
end_expr_stmt

begin_return
return|return
operator|new
name|FluentIterable
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
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
name|concat
argument_list|(
comment|/* lazily generate the iterators on each input only as needed */
operator|new
name|AbstractIndexedListIterator
argument_list|<
name|Iterator
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
argument_list|(
name|inputs
operator|.
name|length
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|get
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|inputs
index|[
name|i
index|]
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
block|)
function|;
block|}
end_return

begin_comment
unit|};   }
comment|/**    * Returns a fluent iterable containing no elements.    *    *<p><b>{@code Stream} equivalent:</b> {@link Stream#empty}.    *    * @since 20.0    */
end_comment

begin_expr_stmt
unit|@
name|Beta
DECL|method|of ()
specifier|public
specifier|static
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
name|FluentIterable
argument_list|<
name|E
argument_list|>
name|of
argument_list|()
block|{
return|return
name|FluentIterable
operator|.
name|from
argument_list|(
name|Collections
operator|.
expr|<
name|E
operator|>
name|emptyList
argument_list|()
argument_list|)
return|;
block|}
end_expr_stmt

begin_comment
comment|/**    * Returns a fluent iterable containing the specified elements in order.    *    *<p><b>{@code Stream} equivalent:</b> {@link java.util.stream.Stream#of(Object[])    * Stream.of(T...)}.    *    * @since 20.0    */
end_comment

begin_annotation
annotation|@
name|Beta
end_annotation

begin_expr_stmt
DECL|method|of ( @arametricNullness E element, E... elements)
specifier|public
specifier|static
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
name|FluentIterable
argument_list|<
name|E
argument_list|>
name|of
argument_list|(
annotation|@
name|ParametricNullness
name|E
name|element
argument_list|,
name|E
operator|...
name|elements
argument_list|)
block|{
return|return
name|from
argument_list|(
name|Lists
operator|.
name|asList
argument_list|(
name|element
argument_list|,
name|elements
argument_list|)
argument_list|)
return|;
block|}
end_expr_stmt

begin_comment
comment|/**    * Returns a string representation of this fluent iterable, with the format {@code [e1, e2, ...,    * en]}.    *    *<p><b>{@code Stream} equivalent:</b> {@code stream.collect(Collectors.joining(", ", "[", "]"))}    * or (less efficiently) {@code stream.collect(Collectors.toList()).toString()}.    */
end_comment

begin_function
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
name|getDelegate
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns the number of elements in this fluent iterable.    *    *<p><b>{@code Stream} equivalent:</b> {@link Stream#count}.    */
end_comment

begin_function
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
name|getDelegate
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns {@code true} if this fluent iterable contains any object for which {@code    * equals(target)} is true.    *    *<p><b>{@code Stream} equivalent:</b> {@code stream.anyMatch(Predicate.isEqual(target))}.    */
end_comment

begin_function
DECL|method|contains (@heckForNull Object target)
specifier|public
specifier|final
name|boolean
name|contains
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|target
parameter_list|)
block|{
return|return
name|Iterables
operator|.
name|contains
argument_list|(
name|getDelegate
argument_list|()
argument_list|,
name|target
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns a fluent iterable whose {@code Iterator} cycles indefinitely over the elements of this    * fluent iterable.    *    *<p>That iterator supports {@code remove()} if {@code iterable.iterator()} does. After {@code    * remove()} is called, subsequent cycles omit the removed element, which is no longer in this    * fluent iterable. The iterator's {@code hasNext()} method returns {@code true} until this fluent    * iterable is empty.    *    *<p><b>Warning:</b> Typical uses of the resulting iterator may produce an infinite loop. You    * should use an explicit {@code break} or be certain that you will eventually remove all the    * elements.    *    *<p><b>{@code Stream} equivalent:</b> if the source iterable has only a single element {@code    * e}, use {@code Stream.generate(() -> e)}. Otherwise, collect your stream into a collection and    * use {@code Stream.generate(() -> collection).flatMap(Collection::stream)}.    */
end_comment

begin_function
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
name|getDelegate
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns a fluent iterable whose iterators traverse first the elements of this fluent iterable,    * followed by those of {@code other}. The iterators are not polled until necessary.    *    *<p>The returned iterable's {@code Iterator} supports {@code remove()} when the corresponding    * {@code Iterator} supports it.    *    *<p><b>{@code Stream} equivalent:</b> {@link Stream#concat}.    *    * @since 18.0    */
end_comment

begin_function
annotation|@
name|Beta
DECL|method|append (Iterable<? extends E> other)
specifier|public
specifier|final
name|FluentIterable
argument_list|<
name|E
argument_list|>
name|append
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|other
parameter_list|)
block|{
return|return
name|FluentIterable
operator|.
name|concat
argument_list|(
name|getDelegate
argument_list|()
argument_list|,
name|other
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns a fluent iterable whose iterators traverse first the elements of this fluent iterable,    * followed by {@code elements}.    *    *<p><b>{@code Stream} equivalent:</b> {@code Stream.concat(thisStream, Stream.of(elements))}.    *    * @since 18.0    */
end_comment

begin_function
annotation|@
name|Beta
DECL|method|append (E... elements)
specifier|public
specifier|final
name|FluentIterable
argument_list|<
name|E
argument_list|>
name|append
parameter_list|(
name|E
modifier|...
name|elements
parameter_list|)
block|{
return|return
name|FluentIterable
operator|.
name|concat
argument_list|(
name|getDelegate
argument_list|()
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns the elements from this fluent iterable that satisfy a predicate. The resulting fluent    * iterable's iterator does not support {@code remove()}.    *    *<p><b>{@code Stream} equivalent:</b> {@link Stream#filter} (same).    */
end_comment

begin_function
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
name|getDelegate
argument_list|()
argument_list|,
name|predicate
argument_list|)
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns the elements from this fluent iterable that are instances of class {@code type}.    *    *<p><b>{@code Stream} equivalent:</b> {@code stream.filter(type::isInstance).map(type::cast)}.    * This does perform a little more work than necessary, so another option is to insert an    * unchecked cast at some later point:    *    *<pre>    * {@code @SuppressWarnings("unchecked") // safe because of ::isInstance check    * ImmutableList<NewType> result =    *     (ImmutableList) stream.filter(NewType.class::isInstance).collect(toImmutableList());}    *</pre>    */
end_comment

begin_function
annotation|@
name|GwtIncompatible
comment|// Class.isInstance
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
name|getDelegate
argument_list|()
argument_list|,
name|type
argument_list|)
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns {@code true} if any element in this fluent iterable satisfies the predicate.    *    *<p><b>{@code Stream} equivalent:</b> {@link Stream#anyMatch} (same).    */
end_comment

begin_function
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
name|getDelegate
argument_list|()
argument_list|,
name|predicate
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns {@code true} if every element in this fluent iterable satisfies the predicate. If this    * fluent iterable is empty, {@code true} is returned.    *    *<p><b>{@code Stream} equivalent:</b> {@link Stream#allMatch} (same).    */
end_comment

begin_function
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
name|getDelegate
argument_list|()
argument_list|,
name|predicate
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns an {@link Optional} containing the first element in this fluent iterable that satisfies    * the given predicate, if such an element exists.    *    *<p><b>Warning:</b> avoid using a {@code predicate} that matches {@code null}. If {@code null}    * is matched in this fluent iterable, a {@link NullPointerException} will be thrown.    *    *<p><b>{@code Stream} equivalent:</b> {@code stream.filter(predicate).findFirst()}.    */
end_comment

begin_function
annotation|@
name|SuppressWarnings
argument_list|(
literal|"nullness"
argument_list|)
comment|// Unsafe, but we can't do much about it now.
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
name|getDelegate
argument_list|()
argument_list|,
name|predicate
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns a fluent iterable that applies {@code function} to each element of this fluent    * iterable.    *    *<p>The returned fluent iterable's iterator supports {@code remove()} if this iterable's    * iterator does. After a successful {@code remove()} call, this fluent iterable no longer    * contains the corresponding element.    *    *<p><b>{@code Stream} equivalent:</b> {@link Stream#map}.    */
end_comment

begin_expr_stmt
DECL|method|transform ( Function<? super E, T> function)
specifier|public
name|final
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|FluentIterable
argument_list|<
name|T
argument_list|>
name|transform
argument_list|(
name|Function
argument_list|<
name|?
super|super
name|E
argument_list|,
name|T
argument_list|>
name|function
argument_list|)
block|{
return|return
name|from
argument_list|(
name|Iterables
operator|.
name|transform
argument_list|(
name|getDelegate
argument_list|()
argument_list|,
name|function
argument_list|)
argument_list|)
return|;
block|}
end_expr_stmt

begin_comment
comment|/**    * Applies {@code function} to each element of this fluent iterable and returns a fluent iterable    * with the concatenated combination of results. {@code function} returns an Iterable of results.    *    *<p>The returned fluent iterable's iterator supports {@code remove()} if this function-returned    * iterables' iterator does. After a successful {@code remove()} call, the returned fluent    * iterable no longer contains the corresponding element.    *    *<p><b>{@code Stream} equivalent:</b> {@link Stream#flatMap} (using a function that produces    * streams, not iterables).    *    * @since 13.0 (required {@code Function<E, Iterable<T>>} until 14.0)    */
end_comment

begin_expr_stmt
DECL|method|transformAndConcat ( Function<? super E, ? extends Iterable<? extends T>> function)
specifier|public
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|FluentIterable
argument_list|<
name|T
argument_list|>
name|transformAndConcat
argument_list|(
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
argument_list|)
block|{
return|return
name|FluentIterable
operator|.
name|concat
argument_list|(
name|transform
argument_list|(
name|function
argument_list|)
argument_list|)
return|;
block|}
end_expr_stmt

begin_comment
comment|/**    * Returns an {@link Optional} containing the first element in this fluent iterable. If the    * iterable is empty, {@code Optional.absent()} is returned.    *    *<p><b>{@code Stream} equivalent:</b> if the goal is to obtain any element, {@link    * Stream#findAny}; if it must specifically be the<i>first</i> element, {@code Stream#findFirst}.    *    * @throws NullPointerException if the first element is null; if this is a possibility, use {@code    *     iterator().next()} or {@link Iterables#getFirst} instead.    */
end_comment

begin_function
annotation|@
name|SuppressWarnings
argument_list|(
literal|"nullness"
argument_list|)
comment|// Unsafe, but we can't do much about it now.
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
name|getDelegate
argument_list|()
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
end_function

begin_comment
comment|/**    * Returns an {@link Optional} containing the last element in this fluent iterable. If the    * iterable is empty, {@code Optional.absent()} is returned. If the underlying {@code iterable} is    * a {@link List} with {@link java.util.RandomAccess} support, then this operation is guaranteed    * to be {@code O(1)}.    *    *<p><b>{@code Stream} equivalent:</b> {@code stream.reduce((a, b) -> b)}.    *    * @throws NullPointerException if the last element is null; if this is a possibility, use {@link    *     Iterables#getLast} instead.    */
end_comment

begin_function
annotation|@
name|SuppressWarnings
argument_list|(
literal|"nullness"
argument_list|)
comment|// Unsafe, but we can't do much about it now.
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
name|Iterable
argument_list|<
name|E
argument_list|>
name|iterable
init|=
name|getDelegate
argument_list|()
decl_stmt|;
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
comment|/*      * TODO(kevinb): consider whether this "optimization" is worthwhile. Users with SortedSets tend      * to know they are SortedSets and probably would not call this method.      */
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
end_function

begin_comment
comment|/**    * Returns a view of this fluent iterable that skips its first {@code numberToSkip} elements. If    * this fluent iterable contains fewer than {@code numberToSkip} elements, the returned fluent    * iterable skips all of its elements.    *    *<p>Modifications to this fluent iterable before a call to {@code iterator()} are reflected in    * the returned fluent iterable. That is, the its iterator skips the first {@code numberToSkip}    * elements that exist when the iterator is created, not when {@code skip()} is called.    *    *<p>The returned fluent iterable's iterator supports {@code remove()} if the {@code Iterator} of    * this fluent iterable supports it. Note that it is<i>not</i> possible to delete the last    * skipped element by immediately calling {@code remove()} on the returned fluent iterable's    * iterator, as the {@code Iterator} contract states that a call to {@code * remove()} before a    * call to {@code next()} will throw an {@link IllegalStateException}.    *    *<p><b>{@code Stream} equivalent:</b> {@link Stream#skip} (same).    */
end_comment

begin_function
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
name|getDelegate
argument_list|()
argument_list|,
name|numberToSkip
argument_list|)
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Creates a fluent iterable with the first {@code size} elements of this fluent iterable. If this    * fluent iterable does not contain that many elements, the returned fluent iterable will have the    * same behavior as this fluent iterable. The returned fluent iterable's iterator supports {@code    * remove()} if this fluent iterable's iterator does.    *    *<p><b>{@code Stream} equivalent:</b> {@link Stream#limit} (same).    *    * @param maxSize the maximum number of elements in the returned fluent iterable    * @throws IllegalArgumentException if {@code size} is negative    */
end_comment

begin_function
DECL|method|limit (int maxSize)
specifier|public
specifier|final
name|FluentIterable
argument_list|<
name|E
argument_list|>
name|limit
parameter_list|(
name|int
name|maxSize
parameter_list|)
block|{
return|return
name|from
argument_list|(
name|Iterables
operator|.
name|limit
argument_list|(
name|getDelegate
argument_list|()
argument_list|,
name|maxSize
argument_list|)
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Determines whether this fluent iterable is empty.    *    *<p><b>{@code Stream} equivalent:</b> {@code !stream.findAny().isPresent()}.    */
end_comment

begin_function
DECL|method|isEmpty ()
specifier|public
specifier|final
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
operator|!
name|getDelegate
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
return|;
block|}
end_function

begin_comment
comment|/**    * Returns an {@code ImmutableList} containing all of the elements from this fluent iterable in    * proper sequence.    *    *<p><b>{@code Stream} equivalent:</b> pass {@link ImmutableList#toImmutableList} to {@code    * stream.collect()}.    *    * @throws NullPointerException if any element is {@code null}    * @since 14.0 (since 12.0 as {@code toImmutableList()}).    */
end_comment

begin_function
annotation|@
name|SuppressWarnings
argument_list|(
literal|"nullness"
argument_list|)
comment|// Unsafe, but we can't do much about it now.
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
name|getDelegate
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns an {@code ImmutableList} containing all of the elements from this {@code    * FluentIterable} in the order specified by {@code comparator}. To produce an {@code    * ImmutableList} sorted by its natural ordering, use {@code toSortedList(Ordering.natural())}.    *    *<p><b>{@code Stream} equivalent:</b> pass {@link ImmutableList#toImmutableList} to {@code    * stream.sorted(comparator).collect()}.    *    * @param comparator the function by which to sort list elements    * @throws NullPointerException if any element of this iterable is {@code null}    * @since 14.0 (since 13.0 as {@code toSortedImmutableList()}).    */
end_comment

begin_function
annotation|@
name|SuppressWarnings
argument_list|(
literal|"nullness"
argument_list|)
comment|// Unsafe, but we can't do much about it now.
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
name|getDelegate
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns an {@code ImmutableSet} containing all of the elements from this fluent iterable with    * duplicates removed.    *    *<p><b>{@code Stream} equivalent:</b> pass {@link ImmutableSet#toImmutableSet} to {@code    * stream.collect()}.    *    * @throws NullPointerException if any element is {@code null}    * @since 14.0 (since 12.0 as {@code toImmutableSet()}).    */
end_comment

begin_function
annotation|@
name|SuppressWarnings
argument_list|(
literal|"nullness"
argument_list|)
comment|// Unsafe, but we can't do much about it now.
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
name|getDelegate
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns an {@code ImmutableSortedSet} containing all of the elements from this {@code    * FluentIterable} in the order specified by {@code comparator}, with duplicates (determined by    * {@code comparator.compare(x, y) == 0}) removed. To produce an {@code ImmutableSortedSet} sorted    * by its natural ordering, use {@code toSortedSet(Ordering.natural())}.    *    *<p><b>{@code Stream} equivalent:</b> pass {@link ImmutableSortedSet#toImmutableSortedSet} to    * {@code stream.collect()}.    *    * @param comparator the function by which to sort set elements    * @throws NullPointerException if any element of this iterable is {@code null}    * @since 14.0 (since 12.0 as {@code toImmutableSortedSet()}).    */
end_comment

begin_function
annotation|@
name|SuppressWarnings
argument_list|(
literal|"nullness"
argument_list|)
comment|// Unsafe, but we can't do much about it now.
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
name|getDelegate
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns an {@code ImmutableMultiset} containing all of the elements from this fluent iterable.    *    *<p><b>{@code Stream} equivalent:</b> pass {@link ImmutableMultiset#toImmutableMultiset} to    * {@code stream.collect()}.    *    * @throws NullPointerException if any element is null    * @since 19.0    */
end_comment

begin_function
annotation|@
name|SuppressWarnings
argument_list|(
literal|"nullness"
argument_list|)
comment|// Unsafe, but we can't do much about it now.
DECL|method|toMultiset ()
specifier|public
specifier|final
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
name|toMultiset
parameter_list|()
block|{
return|return
name|ImmutableMultiset
operator|.
name|copyOf
argument_list|(
name|getDelegate
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns an immutable map whose keys are the distinct elements of this {@code FluentIterable}    * and whose value for each key was computed by {@code valueFunction}. The map's iteration order    * is the order of the first appearance of each key in this iterable.    *    *<p>When there are multiple instances of a key in this iterable, it is unspecified whether    * {@code valueFunction} will be applied to more than one instance of that key and, if it is,    * which result will be mapped to that key in the returned map.    *    *<p><b>{@code Stream} equivalent:</b> {@code stream.collect(ImmutableMap.toImmutableMap(k -> k,    * valueFunction))}.    *    * @throws NullPointerException if any element of this iterable is {@code null}, or if {@code    *     valueFunction} produces {@code null} for any key    * @since 14.0    */
end_comment

begin_function
annotation|@
name|SuppressWarnings
argument_list|(
literal|"nullness"
argument_list|)
comment|// Unsafe, but we can't do much about it now.
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
name|getDelegate
argument_list|()
argument_list|,
name|valueFunction
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Creates an index {@code ImmutableListMultimap} that contains the results of applying a    * specified function to each item in this {@code FluentIterable} of values. Each element of this    * iterable will be stored as a value in the resulting multimap, yielding a multimap with the same    * size as this iterable. The key used to store that value in the multimap will be the result of    * calling the function on that value. The resulting multimap is created as an immutable snapshot.    * In the returned multimap, keys appear in the order they are first encountered, and the values    * corresponding to each key appear in the same order as they are encountered.    *    *<p><b>{@code Stream} equivalent:</b> {@code stream.collect(Collectors.groupingBy(keyFunction))}    * behaves similarly, but returns a mutable {@code Map<K, List<E>>} instead, and may not preserve    * the order of entries).    *    * @param keyFunction the function used to produce the key for each value    * @throws NullPointerException if any element of this iterable is {@code null}, or if {@code    *     keyFunction} produces {@code null} for any key    * @since 14.0    */
end_comment

begin_function
annotation|@
name|SuppressWarnings
argument_list|(
literal|"nullness"
argument_list|)
comment|// Unsafe, but we can't do much about it now.
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
name|getDelegate
argument_list|()
argument_list|,
name|keyFunction
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns a map with the contents of this {@code FluentIterable} as its {@code values}, indexed    * by keys derived from those values. In other words, each input value produces an entry in the    * map whose key is the result of applying {@code keyFunction} to that value. These entries appear    * in the same order as they appeared in this fluent iterable. Example usage:    *    *<pre>{@code    * Color red = new Color("red", 255, 0, 0);    * ...    * FluentIterable<Color> allColors = FluentIterable.from(ImmutableSet.of(red, green, blue));    *    * Map<String, Color> colorForName = allColors.uniqueIndex(toStringFunction());    * assertThat(colorForName).containsEntry("red", red);    * }</pre>    *    *<p>If your index may associate multiple values with each key, use {@link #index(Function)    * index}.    *    *<p><b>{@code Stream} equivalent:</b> {@code    * stream.collect(ImmutableMap.toImmutableMap(keyFunction, v -> v))}.    *    * @param keyFunction the function used to produce the key for each value    * @return a map mapping the result of evaluating the function {@code keyFunction} on each value    *     in this fluent iterable to that value    * @throws IllegalArgumentException if {@code keyFunction} produces the same key for more than one    *     value in this fluent iterable    * @throws NullPointerException if any element of this iterable is {@code null}, or if {@code    *     keyFunction} produces {@code null} for any key    * @since 14.0    */
end_comment

begin_function
annotation|@
name|SuppressWarnings
argument_list|(
literal|"nullness"
argument_list|)
comment|// Unsafe, but we can't do much about it now.
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
name|getDelegate
argument_list|()
argument_list|,
name|keyFunction
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns an array containing all of the elements from this fluent iterable in iteration order.    *    *<p><b>{@code Stream} equivalent:</b> if an object array is acceptable, use {@code    * stream.toArray()}; if {@code type} is a class literal such as {@code MyType.class}, use {@code    * stream.toArray(MyType[]::new)}. Otherwise use {@code stream.toArray( len -> (E[])    * Array.newInstance(type, len))}.    *    * @param type the type of the elements    * @return a newly-allocated array into which all the elements of this fluent iterable have been    *     copied    */
end_comment

begin_function
annotation|@
name|GwtIncompatible
comment|// Array.newArray(Class, int)
comment|/*    * Both the declaration of our Class<E> parameter and its usage in a call to Iterables.toArray    * produce a nullness error: E may be a nullable type, and our nullness checker has Class's type    * parameter bounded to non-null types. To avoid that, we'd use Class<@Nonnull E> if we could.    * (Granted, this is only one of many nullness-checking problems that arise from letting    * FluentIterable support null elements, and most of the other produce outright unsoundness.)    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"nullness"
argument_list|)
DECL|method|toArray (Class<E> type)
specifier|public
specifier|final
annotation|@
name|Nullable
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
name|getDelegate
argument_list|()
argument_list|,
name|type
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Copies all the elements from this fluent iterable to {@code collection}. This is equivalent to    * calling {@code Iterables.addAll(collection, this)}.    *    *<p><b>{@code Stream} equivalent:</b> {@code stream.forEachOrdered(collection::add)} or {@code    * stream.forEach(collection::add)}.    *    * @param collection the collection to copy elements to    * @return {@code collection}, for convenience    * @since 14.0    */
end_comment

begin_function
annotation|@
name|CanIgnoreReturnValue
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
name|Iterable
argument_list|<
name|E
argument_list|>
name|iterable
init|=
name|getDelegate
argument_list|()
decl_stmt|;
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
operator|(
name|Collection
argument_list|<
name|E
argument_list|>
operator|)
name|iterable
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
end_function

begin_comment
comment|/**    * Returns a {@link String} containing all of the elements of this fluent iterable joined with    * {@code joiner}.    *    *<p><b>{@code Stream} equivalent:</b> {@code joiner.join(stream.iterator())}, or, if you are not    * using any optional {@code Joiner} features, {@code    * stream.collect(Collectors.joining(delimiter)}.    *    * @since 18.0    */
end_comment

begin_function
annotation|@
name|Beta
DECL|method|join (Joiner joiner)
specifier|public
specifier|final
name|String
name|join
parameter_list|(
name|Joiner
name|joiner
parameter_list|)
block|{
return|return
name|joiner
operator|.
name|join
argument_list|(
name|this
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns the element at the specified position in this fluent iterable.    *    *<p><b>{@code Stream} equivalent:</b> {@code stream.skip(position).findFirst().get()} (but note    * that this throws different exception types, and throws an exception if {@code null} would be    * returned).    *    * @param position position of the element to return    * @return the element at the specified position in this fluent iterable    * @throws IndexOutOfBoundsException if {@code position} is negative or greater than or equal to    *     the size of this fluent iterable    */
end_comment

begin_function
annotation|@
name|ParametricNullness
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
name|getDelegate
argument_list|()
argument_list|,
name|position
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Returns a stream of this fluent iterable's contents (similar to calling {@link    * Collection#stream} on a collection).    *    *<p><b>Note:</b> the earlier in the chain you can switch to {@code Stream} usage (ideally not    * going through {@code FluentIterable} at all), the more performant and idiomatic your code will    * be. This method is a transitional aid, to be used only when really necessary.    *    * @since 21.0    */
end_comment

begin_function
DECL|method|stream ()
specifier|public
specifier|final
name|Stream
argument_list|<
name|E
argument_list|>
name|stream
parameter_list|()
block|{
return|return
name|Streams
operator|.
name|stream
argument_list|(
name|getDelegate
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/** Function that transforms {@code Iterable<E>} into a fluent iterable. */
end_comment

begin_expr_stmt
DECL|class|FromIterableFunction
specifier|private
specifier|static
name|class
name|FromIterableFunction
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
expr|implements
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
block|{     @
name|Override
DECL|method|apply (Iterable<E> fromObject)
specifier|public
name|FluentIterable
argument_list|<
name|E
argument_list|>
name|apply
argument_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|fromObject
argument_list|)
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
end_expr_stmt

unit|} }
end_unit

