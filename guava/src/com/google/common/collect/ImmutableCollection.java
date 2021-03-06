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
name|DoNotCall
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
name|DoNotMock
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
name|AbstractCollection
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
name|Spliterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Spliterators
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
name|Predicate
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
comment|/**  * A {@link Collection} whose contents will never change, and which offers a few additional  * guarantees detailed below.  *  *<p><b>Warning:</b> avoid<i>direct</i> usage of {@link ImmutableCollection} as a type (just as  * with {@link Collection} itself). Prefer subtypes such as {@link ImmutableSet} or {@link  * ImmutableList}, which have well-defined {@link #equals} semantics, thus avoiding a common source  * of bugs and confusion.  *  *<h3>About<i>all</i> {@code Immutable-} collections</h3>  *  *<p>The remainder of this documentation applies to every public {@code Immutable-} type in this  * package, whether it is a subtype of {@code ImmutableCollection} or not.  *  *<h4>Guarantees</h4>  *  *<p>Each makes the following guarantees:  *  *<ul>  *<li><b>Shallow immutability.</b> Elements can never be added, removed or replaced in this  *       collection. This is a stronger guarantee than that of {@link  *       Collections#unmodifiableCollection}, whose contents change whenever the wrapped collection  *       is modified.  *<li><b>Null-hostility.</b> This collection will never contain a null element.  *<li><b>Deterministic iteration.</b> The iteration order is always well-defined, depending on  *       how the collection was created. Typically this is insertion order unless an explicit  *       ordering is otherwise specified (e.g. {@link ImmutableSortedSet#naturalOrder}). See the  *       appropriate factory method for details. View collections such as {@link  *       ImmutableMultiset#elementSet} iterate in the same order as the parent, except as noted.  *<li><b>Thread safety.</b> It is safe to access this collection concurrently from multiple  *       threads.  *<li><b>Integrity.</b> This type cannot be subclassed outside this package (which would allow  *       these guarantees to be violated).  *</ul>  *  *<h4>"Interfaces", not implementations</h4>  *  *<p>These are classes instead of interfaces to prevent external subtyping, but should be thought  * of as interfaces in every important sense. Each public class such as {@link ImmutableSet} is a  *<i>type</i> offering meaningful behavioral guarantees. This is substantially different from the  * case of (say) {@link HashSet}, which is an<i>implementation</i>, with semantics that were  * largely defined by its supertype.  *  *<p>For field types and method return types, you should generally use the immutable type (such as  * {@link ImmutableList}) instead of the general collection interface type (such as {@link List}).  * This communicates to your callers all of the semantic guarantees listed above, which is almost  * always very useful information.  *  *<p>On the other hand, a<i>parameter</i> type of {@link ImmutableList} is generally a nuisance to  * callers. Instead, accept {@link Iterable} and have your method or constructor body pass it to the  * appropriate {@code copyOf} method itself.  *  *<p>Expressing the immutability guarantee directly in the type that user code references is a  * powerful advantage. Although Java offers certain immutable collection factory methods, such as  * {@link Collections#singleton(Object)} and<a  * href="https://docs.oracle.com/javase/9/docs/api/java/util/Set.html#immutable">{@code Set.of}</a>,  * we recommend using<i>these</i> classes instead for this reason (as well as for consistency).  *  *<h4>Creation</h4>  *  *<p>Except for logically "abstract" types like {@code ImmutableCollection} itself, each {@code  * Immutable} type provides the static operations you need to obtain instances of that type. These  * usually include:  *  *<ul>  *<li>Static methods named {@code of}, accepting an explicit list of elements or entries.  *<li>Static methods named {@code copyOf} (or {@code copyOfSorted}), accepting an existing  *       collection whose contents should be copied.  *<li>A static nested {@code Builder} class which can be used to populate a new immutable  *       instance.  *</ul>  *  *<h4>Warnings</h4>  *  *<ul>  *<li><b>Warning:</b> as with any collection, it is almost always a bad idea to modify an element  *       (in a way that affects its {@link Object#equals} behavior) while it is contained in a  *       collection. Undefined behavior and bugs will result. It's generally best to avoid using  *       mutable objects as elements at all, as many users may expect your "immutable" object to be  *<i>deeply</i> immutable.  *</ul>  *  *<h4>Performance notes</h4>  *  *<ul>  *<li>Implementations can be generally assumed to prioritize memory efficiency, then speed of  *       access, and lastly speed of creation.  *<li>The {@code copyOf} methods will sometimes recognize that the actual copy operation is  *       unnecessary; for example, {@code copyOf(copyOf(anArrayList))} should copy the data only  *       once. This reduces the expense of habitually making defensive copies at API boundaries.  *       However, the precise conditions for skipping the copy operation are undefined.  *<li><b>Warning:</b> a view collection such as {@link ImmutableMap#keySet} or {@link  *       ImmutableList#subList} may retain a reference to the entire data set, preventing it from  *       being garbage collected. If some of the data is no longer reachable through other means,  *       this constitutes a memory leak. Pass the view collection to the appropriate {@code copyOf}  *       method to obtain a correctly-sized copy.  *<li>The performance of using the associated {@code Builder} class can be assumed to be no  *       worse, and possibly better, than creating a mutable collection and copying it.  *<li>Implementations generally do not cache hash codes. If your element or key type has a slow  *       {@code hashCode} implementation, it should cache it itself.  *</ul>  *  *<h4>Example usage</h4>  *  *<pre>{@code  * class Foo {  *   private static final ImmutableSet<String> RESERVED_CODES =  *       ImmutableSet.of("AZ", "CQ", "ZX");  *  *   private final ImmutableSet<String> codes;  *  *   public Foo(Iterable<String> codes) {  *     this.codes = ImmutableSet.copyOf(codes);  *     checkArgument(Collections.disjoint(this.codes, RESERVED_CODES));  *   }  * }  * }</pre>  *  *<h3>See also</h3>  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/ImmutableCollectionsExplained"> immutable collections</a>.  *  * @since 2.0  */
end_comment

begin_class
annotation|@
name|DoNotMock
argument_list|(
literal|"Use ImmutableList.of or another implementation"
argument_list|)
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// we're overriding default serialization
annotation|@
name|ElementTypesAreNonnullByDefault
comment|// TODO(kevinb): I think we should push everything down to "BaseImmutableCollection" or something,
comment|// just to do everything we can to emphasize the "practically an interface" nature of this class.
DECL|class|ImmutableCollection
specifier|public
specifier|abstract
class|class
name|ImmutableCollection
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractCollection
argument_list|<
name|E
argument_list|>
implements|implements
name|Serializable
block|{
comment|/*    * We expect SIZED (and SUBSIZED, if applicable) to be added by the spliterator factory methods.    * These are properties of the collection as a whole; SIZED and SUBSIZED are more properties of    * the spliterator implementation.    */
DECL|field|SPLITERATOR_CHARACTERISTICS
specifier|static
specifier|final
name|int
name|SPLITERATOR_CHARACTERISTICS
init|=
name|Spliterator
operator|.
name|IMMUTABLE
operator||
name|Spliterator
operator|.
name|NONNULL
operator||
name|Spliterator
operator|.
name|ORDERED
decl_stmt|;
DECL|method|ImmutableCollection ()
name|ImmutableCollection
parameter_list|()
block|{}
comment|/** Returns an unmodifiable iterator across the elements in this collection. */
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
specifier|abstract
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|spliterator ()
specifier|public
name|Spliterator
argument_list|<
name|E
argument_list|>
name|spliterator
parameter_list|()
block|{
return|return
name|Spliterators
operator|.
name|spliterator
argument_list|(
name|this
argument_list|,
name|SPLITERATOR_CHARACTERISTICS
argument_list|)
return|;
block|}
DECL|field|EMPTY_ARRAY
specifier|private
specifier|static
specifier|final
name|Object
index|[]
name|EMPTY_ARRAY
init|=
block|{}
decl_stmt|;
annotation|@
name|Override
DECL|method|toArray ()
specifier|public
specifier|final
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|toArray
argument_list|(
name|EMPTY_ARRAY
argument_list|)
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
comment|/*    * This suppression is here for two reasons:    *    * 1. b/192354773 in our checker affects toArray declarations.    *    * 2. `other[size] = null` is unsound. We could "fix" this by requiring callers to pass in an    * array with a nullable element type. But probably they usually want an array with a non-nullable    * type. That said, we could *accept* a `@Nullable T[]` (which, given that we treat arrays as    * covariant, would still permit a plain `T[]`) and return a plain `T[]`. But of course that would    * require its own suppression, since it is also unsound. toArray(T[]) is just a mess from a    * nullness perspective. The signature below at least has the virtue of being relatively simple.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"nullness"
argument_list|)
DECL|method|toArray (T[] other)
specifier|public
name|final
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|T
index|[]
name|toArray
argument_list|(
name|T
index|[]
name|other
argument_list|)
block|{
name|checkNotNull
argument_list|(
name|other
argument_list|)
block|;
name|int
name|size
operator|=
name|size
argument_list|()
block|;
if|if
condition|(
name|other
operator|.
name|length
operator|<
name|size
condition|)
block|{
name|Object
index|[]
name|internal
init|=
name|internalArray
argument_list|()
decl_stmt|;
if|if
condition|(
name|internal
operator|!=
literal|null
condition|)
block|{
return|return
name|Platform
operator|.
name|copy
argument_list|(
name|internal
argument_list|,
name|internalArrayStart
argument_list|()
argument_list|,
name|internalArrayEnd
argument_list|()
argument_list|,
name|other
argument_list|)
return|;
block|}
name|other
operator|=
name|ObjectArrays
operator|.
name|newArray
argument_list|(
name|other
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
end_class

begin_elseif
elseif|else
if|if
condition|(
name|other
operator|.
name|length
operator|>
name|size
condition|)
block|{
name|other
index|[
name|size
index|]
operator|=
literal|null
expr_stmt|;
block|}
end_elseif

begin_expr_stmt
name|copyIntoArray
argument_list|(
name|other
argument_list|,
literal|0
argument_list|)
expr_stmt|;
end_expr_stmt

begin_return
return|return
name|other
return|;
end_return

begin_comment
unit|}
comment|/** If this collection is backed by an array of its elements in insertion order, returns it. */
end_comment

begin_function
unit|@
name|CheckForNull
DECL|method|internalArray ()
name|Object
index|[]
name|internalArray
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
end_function

begin_comment
comment|/**    * If this collection is backed by an array of its elements in insertion order, returns the offset    * where this collection's elements start.    */
end_comment

begin_function
DECL|method|internalArrayStart ()
name|int
name|internalArrayStart
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
end_function

begin_comment
comment|/**    * If this collection is backed by an array of its elements in insertion order, returns the offset    * where this collection's elements end.    */
end_comment

begin_function
DECL|method|internalArrayEnd ()
name|int
name|internalArrayEnd
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
end_function

begin_function_decl
annotation|@
name|Override
DECL|method|contains (@heckForNull Object object)
specifier|public
specifier|abstract
name|boolean
name|contains
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|object
parameter_list|)
function_decl|;
end_function_decl

begin_comment
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
end_comment

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Deprecated
annotation|@
name|Override
annotation|@
name|DoNotCall
argument_list|(
literal|"Always throws UnsupportedOperationException"
argument_list|)
DECL|method|add (E e)
specifier|public
specifier|final
name|boolean
name|add
parameter_list|(
name|E
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
end_function

begin_comment
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
end_comment

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Deprecated
annotation|@
name|Override
annotation|@
name|DoNotCall
argument_list|(
literal|"Always throws UnsupportedOperationException"
argument_list|)
DECL|method|remove (@heckForNull Object object)
specifier|public
specifier|final
name|boolean
name|remove
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|object
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
end_function

begin_comment
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
end_comment

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Deprecated
annotation|@
name|Override
annotation|@
name|DoNotCall
argument_list|(
literal|"Always throws UnsupportedOperationException"
argument_list|)
DECL|method|addAll (Collection<? extends E> newElements)
specifier|public
specifier|final
name|boolean
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|newElements
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
end_function

begin_comment
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
end_comment

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Deprecated
annotation|@
name|Override
annotation|@
name|DoNotCall
argument_list|(
literal|"Always throws UnsupportedOperationException"
argument_list|)
DECL|method|removeAll (Collection<?> oldElements)
specifier|public
specifier|final
name|boolean
name|removeAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|oldElements
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
end_function

begin_comment
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
end_comment

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Deprecated
annotation|@
name|Override
annotation|@
name|DoNotCall
argument_list|(
literal|"Always throws UnsupportedOperationException"
argument_list|)
DECL|method|removeIf (Predicate<? super E> filter)
specifier|public
specifier|final
name|boolean
name|removeIf
parameter_list|(
name|Predicate
argument_list|<
name|?
super|super
name|E
argument_list|>
name|filter
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
end_function

begin_comment
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
end_comment

begin_function
annotation|@
name|Deprecated
annotation|@
name|Override
annotation|@
name|DoNotCall
argument_list|(
literal|"Always throws UnsupportedOperationException"
argument_list|)
DECL|method|retainAll (Collection<?> elementsToKeep)
specifier|public
specifier|final
name|boolean
name|retainAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|elementsToKeep
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
end_function

begin_comment
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
end_comment

begin_function
annotation|@
name|Deprecated
annotation|@
name|Override
annotation|@
name|DoNotCall
argument_list|(
literal|"Always throws UnsupportedOperationException"
argument_list|)
DECL|method|clear ()
specifier|public
specifier|final
name|void
name|clear
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
end_function

begin_comment
comment|/**    * Returns an {@code ImmutableList} containing the same elements, in the same order, as this    * collection.    *    *<p><b>Performance note:</b> in most cases this method can return quickly without actually    * copying anything. The exact circumstances under which the copy is performed are undefined and    * subject to change.    *    * @since 2.0    */
end_comment

begin_function
DECL|method|asList ()
specifier|public
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|asList
parameter_list|()
block|{
switch|switch
condition|(
name|size
argument_list|()
condition|)
block|{
case|case
literal|0
case|:
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
case|case
literal|1
case|:
return|return
name|ImmutableList
operator|.
name|of
argument_list|(
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
return|;
default|default:
return|return
operator|new
name|RegularImmutableAsList
argument_list|<
name|E
argument_list|>
argument_list|(
name|this
argument_list|,
name|toArray
argument_list|()
argument_list|)
return|;
block|}
block|}
end_function

begin_comment
comment|/**    * Returns {@code true} if this immutable collection's implementation contains references to    * user-created objects that aren't accessible via this collection's methods. This is generally    * used to determine whether {@code copyOf} implementations should make an explicit copy to avoid    * memory leaks.    */
end_comment

begin_function_decl
DECL|method|isPartialView ()
specifier|abstract
name|boolean
name|isPartialView
parameter_list|()
function_decl|;
end_function_decl

begin_comment
comment|/**    * Copies the contents of this immutable collection into the specified array at the specified    * offset. Returns {@code offset + size()}.    */
end_comment

begin_function
annotation|@
name|CanIgnoreReturnValue
DECL|method|copyIntoArray (@ullable Object[] dst, int offset)
name|int
name|copyIntoArray
parameter_list|(
annotation|@
name|Nullable
name|Object
index|[]
name|dst
parameter_list|,
name|int
name|offset
parameter_list|)
block|{
for|for
control|(
name|E
name|e
range|:
name|this
control|)
block|{
name|dst
index|[
name|offset
operator|++
index|]
operator|=
name|e
expr_stmt|;
block|}
return|return
name|offset
return|;
block|}
end_function

begin_function
DECL|method|writeReplace ()
name|Object
name|writeReplace
parameter_list|()
block|{
comment|// We serialize by default to ImmutableList, the simplest thing that works.
return|return
operator|new
name|ImmutableList
operator|.
name|SerializedForm
argument_list|(
name|toArray
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Abstract base class for builders of {@link ImmutableCollection} types.    *    * @since 10.0    */
end_comment

begin_class
annotation|@
name|DoNotMock
DECL|class|Builder
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
parameter_list|<
name|E
parameter_list|>
block|{
DECL|field|DEFAULT_INITIAL_CAPACITY
specifier|static
specifier|final
name|int
name|DEFAULT_INITIAL_CAPACITY
init|=
literal|4
decl_stmt|;
DECL|method|expandedCapacity (int oldCapacity, int minCapacity)
specifier|static
name|int
name|expandedCapacity
parameter_list|(
name|int
name|oldCapacity
parameter_list|,
name|int
name|minCapacity
parameter_list|)
block|{
if|if
condition|(
name|minCapacity
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"cannot store more than MAX_VALUE elements"
argument_list|)
throw|;
block|}
comment|// careful of overflow!
name|int
name|newCapacity
init|=
name|oldCapacity
operator|+
operator|(
name|oldCapacity
operator|>>
literal|1
operator|)
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|newCapacity
operator|<
name|minCapacity
condition|)
block|{
name|newCapacity
operator|=
name|Integer
operator|.
name|highestOneBit
argument_list|(
name|minCapacity
operator|-
literal|1
argument_list|)
operator|<<
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|newCapacity
operator|<
literal|0
condition|)
block|{
name|newCapacity
operator|=
name|Integer
operator|.
name|MAX_VALUE
expr_stmt|;
comment|// guaranteed to be>= newCapacity
block|}
return|return
name|newCapacity
return|;
block|}
DECL|method|Builder ()
name|Builder
parameter_list|()
block|{}
comment|/**      * Adds {@code element} to the {@code ImmutableCollection} being built.      *      *<p>Note that each builder class covariantly returns its own type from this method.      *      * @param element the element to add      * @return this {@code Builder} instance      * @throws NullPointerException if {@code element} is null      */
annotation|@
name|CanIgnoreReturnValue
DECL|method|add (E element)
specifier|public
specifier|abstract
name|Builder
argument_list|<
name|E
argument_list|>
name|add
parameter_list|(
name|E
name|element
parameter_list|)
function_decl|;
comment|/**      * Adds each element of {@code elements} to the {@code ImmutableCollection} being built.      *      *<p>Note that each builder class overrides this method in order to covariantly return its own      * type.      *      * @param elements the elements to add      * @return this {@code Builder} instance      * @throws NullPointerException if {@code elements} is null or contains a null element      */
annotation|@
name|CanIgnoreReturnValue
DECL|method|add (E... elements)
specifier|public
name|Builder
argument_list|<
name|E
argument_list|>
name|add
parameter_list|(
name|E
modifier|...
name|elements
parameter_list|)
block|{
for|for
control|(
name|E
name|element
range|:
name|elements
control|)
block|{
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Adds each element of {@code elements} to the {@code ImmutableCollection} being built.      *      *<p>Note that each builder class overrides this method in order to covariantly return its own      * type.      *      * @param elements the elements to add      * @return this {@code Builder} instance      * @throws NullPointerException if {@code elements} is null or contains a null element      */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addAll (Iterable<? extends E> elements)
specifier|public
name|Builder
argument_list|<
name|E
argument_list|>
name|addAll
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
parameter_list|)
block|{
for|for
control|(
name|E
name|element
range|:
name|elements
control|)
block|{
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Adds each element of {@code elements} to the {@code ImmutableCollection} being built.      *      *<p>Note that each builder class overrides this method in order to covariantly return its own      * type.      *      * @param elements the elements to add      * @return this {@code Builder} instance      * @throws NullPointerException if {@code elements} is null or contains a null element      */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addAll (Iterator<? extends E> elements)
specifier|public
name|Builder
argument_list|<
name|E
argument_list|>
name|addAll
parameter_list|(
name|Iterator
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
parameter_list|)
block|{
while|while
condition|(
name|elements
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|add
argument_list|(
name|elements
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Returns a newly-created {@code ImmutableCollection} of the appropriate type, containing the      * elements provided to this builder.      *      *<p>Note that each builder class covariantly returns the appropriate type of {@code      * ImmutableCollection} from this method.      */
DECL|method|build ()
specifier|public
specifier|abstract
name|ImmutableCollection
argument_list|<
name|E
argument_list|>
name|build
parameter_list|()
function_decl|;
block|}
end_class

unit|}
end_unit

