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
name|CollectPreconditions
operator|.
name|checkNonnegative
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
name|ObjectArrays
operator|.
name|checkElementsNotNull
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
comment|/**  * A subtype of {@link Collection} making additional guarantees: its contents will never change, it  * will never contain {@code null}, and its iteration order is deterministic.  *  *<p><b>Note:</b> {@code ImmutableCollection} itself exists primarily as a common supertype for  * more useful types like {@link ImmutableSet} and {@link ImmutableList}. Like {@code Collection},  * it has no defined {@link #equals} behavior, which can lead to surprises and bugs, so (like {@code  * Collection}) it should not be used directly.  *  *<p>Example usage:<pre>   {@code  *  *   class Foo {  *     private static final ImmutableSet<String> RESERVED_CODES =  *         ImmutableSet.of("AZ", "CQ", "ZX");  *  *     private final ImmutableSet<String> codes;  *  *     public Foo(Iterable<String> codes) {  *       this.codes = ImmutableSet.copyOf(codes);  *       checkArgument(Collections.disjoint(this.codes, RESERVED_CODES));  *   }  *  *<h3>About<i>all</i> public {@code Immutable-} types in this package</h3>  *  *<h4>Guarantees</h4>  *  *<p>Each makes the following guarantees:  *  *<ul>  *<li>Its contents can never change. Any attempt to add, remove or replace an element results in an  *     {@link UnsupportedOperationException}. Note that this guarantee of<i>immutability</i> is  *     stronger than that of {@link Collections#unmodifiableCollection}, which only prevents  *     modification operations from being invoked on the reference it returns, while any other code  *     having a reference to the inner collection can still modify it at will.  *<li>It can never contain {@code null} as an element, key or value. An attempt to do so results in  *     a {@link NullPointerException}.  *<li>Its iteration order is deterministic. What that order is, specifically, depends on how the  *     collection was created. See the appropriate factory method for details.  *<li>It cannot be subclassed outside this package (which would permit these guarantees to be  *     violated).  *<li>It is thread-safe.  *</ul>  *  *<h4>Types, not implementations</h4>  *  *<p>Each of these public classes, such as {@code ImmutableList}, is a<i>type</i>, not a  * specific<i>implementation</i> (unlike the case of, say, {@link ArrayList}). That is, they should  * be thought of as interfaces in virtually every important sense, just ones that classes outside  * this package can't implement.  *  *<p>For your field types and method return types, use the immutable type (like {@code  * ImmutableList}) instead of the corresponding basic collection interface type (like {@link List})  * unless the semantic guarantees listed above are not considered relevant. On the other hand, a  *<i>parameter</i> type of {@code ImmutableList} can be a nuisance to callers; instead, accept  * {@link List} (or even {@link Iterable}) and pass it to {@link ImmutableList#copyOf(Collection)}  * yourself.  *  *<h4>Creation</h4>  *  *<p>With the exception of {@code ImmutableCollection} itself, each {@code Immutable} type provides  * the static operations you need to obtain instances of that type:  *  *<ul>  *<li>Static methods named {@code of} accepting an explicit list of elements or entries  *<li>Static methods named {@code copyOf} accepting an existing collection (or similar) whose  *     contents should be copied  *<li>A static nested {@code Builder} class which can be used to progressively populate a new  *     immutable instance  *</ul>  *  *<h4>Other common properties</h4>  *  *<ul>  *<li>View collections, such as {@link ImmutableMap#keySet} or {@link ImmutableList#subList},  *     return the appropriate {@code Immutable} type. This is true even when the language does not  *     permit the method's return type to express it (for example in the case of {@link  *     ImmutableListMultimap#asMap}).  *  *<h4>Performance notes</h4>  *  *<ul>  *<li>When a {@code copyOf} method is passed a collection that is already immutable, in most cases  *     it can return quickly without actually copying anything. This means that making defensive  *     copies at API boundaries as a habit is not necessarily expensive in the long run.  *<li>Implementations can be generally assumed to prioritize memory efficiency and speed of access  *     over speed of creation.  *<li>The performance of using the associated {@code Builder} class can generally be assumed to be  *     no worse, and possibly better, than creating a mutable collection and copying it.  *<li>Implementations generally do not cache hash codes. If your key type has a slow {@code  *     hashCode} implementation, it should cache it itself.  *</ul>  *  *<h4>Notable subtypes (not exhaustive)</h4>  *  *<ul>  *<li>{@code ImmutableCollection}  *<ul>  *<li>{@link ImmutableSet}  *<ul>  *<li>{@link ImmutableSortedSet}  *</ul>  *<li>{@link ImmutableList}  *<li>{@link ImmutableMultiset}  *</ul>  *<li>{@link ImmutableMap}  *<ul>  *<li>{@link ImmutableSortedMap}  *<li>{@link ImmutableBiMap}  *</ul>  *<li>{@link ImmutableMultimap}  *<ul>  *<li>{@link ImmutableListMultimap}  *<li>{@link ImmutableSetMultimap}  *</ul>  *<li>{@link ImmutableTable}  *<li>{@link ImmutableRangeSet}  *<li>{@link ImmutableRangeMap}  *</ul>  *  *<h3>See also</h3>  *  *<p>See the Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/ImmutableCollectionsExplained">  * immutable collections</a>.  *  * @since 2.0 (imported from Google Collections Library)  */
end_comment

begin_class
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
DECL|method|ImmutableCollection ()
name|ImmutableCollection
parameter_list|()
block|{}
comment|/**    * Returns an unmodifiable iterator across the elements in this collection.    */
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
DECL|method|toArray ()
specifier|public
specifier|final
name|Object
index|[]
name|toArray
parameter_list|()
block|{
name|int
name|size
init|=
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|==
literal|0
condition|)
block|{
return|return
name|ObjectArrays
operator|.
name|EMPTY_ARRAY
return|;
block|}
name|Object
index|[]
name|result
init|=
operator|new
name|Object
index|[
name|size
index|]
decl_stmt|;
name|copyIntoArray
argument_list|(
name|result
argument_list|,
literal|0
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|toArray (T[] other)
specifier|public
specifier|final
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|toArray
parameter_list|(
name|T
index|[]
name|other
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|other
argument_list|)
expr_stmt|;
name|int
name|size
init|=
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|other
operator|.
name|length
operator|<
name|size
condition|)
block|{
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
name|copyIntoArray
argument_list|(
name|other
argument_list|,
literal|0
argument_list|)
expr_stmt|;
return|return
name|other
return|;
block|}
annotation|@
name|Override
DECL|method|contains (@ullable Object object)
specifier|public
specifier|abstract
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
function_decl|;
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
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
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|remove (Object object)
specifier|public
specifier|final
name|boolean
name|remove
parameter_list|(
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
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
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
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
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
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
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
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
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
comment|/*    * TODO(kevinb): Restructure code so ImmutableList doesn't contain this    * variable, which it doesn't use.    */
DECL|field|asList
specifier|private
specifier|transient
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|asList
decl_stmt|;
comment|/**    * Returns an {@code ImmutableList} containing the same elements, in the same order, as this    * collection.    *    *<p><b>Performance note:</b> in most cases this method can return quickly without actually    * copying anything. The exact circumstances under which the copy is performed are undefined and    * subject to change.    *    * @since 2.0    */
DECL|method|asList ()
specifier|public
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|asList
parameter_list|()
block|{
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|list
init|=
name|asList
decl_stmt|;
return|return
operator|(
name|list
operator|==
literal|null
operator|)
condition|?
operator|(
name|asList
operator|=
name|createAsList
argument_list|()
operator|)
else|:
name|list
return|;
block|}
DECL|method|createAsList ()
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|createAsList
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
comment|/**    * Returns {@code true} if this immutable collection's implementation contains references to    * user-created objects that aren't accessible via this collection's methods. This is generally    * used to determine whether {@code copyOf} implementations should make an explicit copy to avoid    * memory leaks.    */
DECL|method|isPartialView ()
specifier|abstract
name|boolean
name|isPartialView
parameter_list|()
function_decl|;
comment|/**    * Copies the contents of this immutable collection into the specified array at the specified    * offset.  Returns {@code offset + size()}.    */
DECL|method|copyIntoArray (Object[] dst, int offset)
name|int
name|copyIntoArray
parameter_list|(
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
comment|/**    * Abstract base class for builders of {@link ImmutableCollection} types.    *    * @since 10.0    */
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
block|{     }
comment|/**      * Adds {@code element} to the {@code ImmutableCollection} being built.      *      *<p>Note that each builder class covariantly returns its own type from      * this method.      *      * @param element the element to add      * @return this {@code Builder} instance      * @throws NullPointerException if {@code element} is null      */
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
comment|/**      * Adds each element of {@code elements} to the {@code ImmutableCollection}      * being built.      *      *<p>Note that each builder class overrides this method in order to      * covariantly return its own type.      *      * @param elements the elements to add      * @return this {@code Builder} instance      * @throws NullPointerException if {@code elements} is null or contains a      *     null element      */
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
comment|/**      * Adds each element of {@code elements} to the {@code ImmutableCollection}      * being built.      *      *<p>Note that each builder class overrides this method in order to      * covariantly return its own type.      *      * @param elements the elements to add      * @return this {@code Builder} instance      * @throws NullPointerException if {@code elements} is null or contains a      *     null element      */
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
comment|/**      * Adds each element of {@code elements} to the {@code ImmutableCollection}      * being built.      *      *<p>Note that each builder class overrides this method in order to      * covariantly return its own type.      *      * @param elements the elements to add      * @return this {@code Builder} instance      * @throws NullPointerException if {@code elements} is null or contains a      *     null element      */
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
comment|/**      * Returns a newly-created {@code ImmutableCollection} of the appropriate      * type, containing the elements provided to this builder.      *      *<p>Note that each builder class covariantly returns the appropriate type      * of {@code ImmutableCollection} from this method.      */
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
DECL|class|ArrayBasedBuilder
specifier|abstract
specifier|static
class|class
name|ArrayBasedBuilder
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableCollection
operator|.
name|Builder
argument_list|<
name|E
argument_list|>
block|{
DECL|field|contents
name|Object
index|[]
name|contents
decl_stmt|;
DECL|field|size
name|int
name|size
decl_stmt|;
DECL|method|ArrayBasedBuilder (int initialCapacity)
name|ArrayBasedBuilder
parameter_list|(
name|int
name|initialCapacity
parameter_list|)
block|{
name|checkNonnegative
argument_list|(
name|initialCapacity
argument_list|,
literal|"initialCapacity"
argument_list|)
expr_stmt|;
name|this
operator|.
name|contents
operator|=
operator|new
name|Object
index|[
name|initialCapacity
index|]
expr_stmt|;
name|this
operator|.
name|size
operator|=
literal|0
expr_stmt|;
block|}
comment|/**      * Expand the absolute capacity of the builder so it can accept at least      * the specified number of elements without being resized.      */
DECL|method|ensureCapacity (int minCapacity)
specifier|private
name|void
name|ensureCapacity
parameter_list|(
name|int
name|minCapacity
parameter_list|)
block|{
if|if
condition|(
name|contents
operator|.
name|length
operator|<
name|minCapacity
condition|)
block|{
name|this
operator|.
name|contents
operator|=
name|ObjectArrays
operator|.
name|arraysCopyOf
argument_list|(
name|this
operator|.
name|contents
argument_list|,
name|expandedCapacity
argument_list|(
name|contents
operator|.
name|length
argument_list|,
name|minCapacity
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|add (E element)
specifier|public
name|ArrayBasedBuilder
argument_list|<
name|E
argument_list|>
name|add
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|ensureCapacity
argument_list|(
name|size
operator|+
literal|1
argument_list|)
expr_stmt|;
name|contents
index|[
name|size
operator|++
index|]
operator|=
name|element
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
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
name|checkElementsNotNull
argument_list|(
name|elements
argument_list|)
expr_stmt|;
name|ensureCapacity
argument_list|(
name|size
operator|+
name|elements
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|elements
argument_list|,
literal|0
argument_list|,
name|contents
argument_list|,
name|size
argument_list|,
name|elements
operator|.
name|length
argument_list|)
expr_stmt|;
name|size
operator|+=
name|elements
operator|.
name|length
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
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
if|if
condition|(
name|elements
operator|instanceof
name|Collection
condition|)
block|{
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
init|=
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|elements
decl_stmt|;
name|ensureCapacity
argument_list|(
name|size
operator|+
name|collection
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|addAll
argument_list|(
name|elements
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
block|}
end_class

end_unit

