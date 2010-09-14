begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Collection
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * An immutable collection. Does not permit null elements.  *  *<p>In addition to the {@link Collection} methods, this class has an {@link  * #asList()} method, which returns a list view of the collection's elements.  *  *<p><b>Note</b>: Although this class is not final, it cannot be subclassed  * outside of this package as it has no public or protected constructors. Thus,  * instances of this type are guaranteed to be immutable.  *  * @author Jesse Wilson  * @since 2 (imported from Google Collections Library)  */
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
DECL|class|ImmutableCollection
specifier|public
specifier|abstract
class|class
name|ImmutableCollection
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Collection
argument_list|<
name|E
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|EMPTY_IMMUTABLE_COLLECTION
specifier|static
specifier|final
name|ImmutableCollection
argument_list|<
name|Object
argument_list|>
name|EMPTY_IMMUTABLE_COLLECTION
init|=
operator|new
name|EmptyImmutableCollection
argument_list|()
decl_stmt|;
DECL|method|ImmutableCollection ()
name|ImmutableCollection
parameter_list|()
block|{}
comment|/**    * Returns an unmodifiable iterator across the elements in this collection.    */
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
DECL|method|toArray ()
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|ObjectArrays
operator|.
name|toArrayImpl
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|toArray (T[] other)
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
name|other
parameter_list|)
block|{
return|return
name|ObjectArrays
operator|.
name|toArrayImpl
argument_list|(
name|this
argument_list|,
name|other
argument_list|)
return|;
block|}
DECL|method|contains (@ullable Object object)
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
name|object
operator|!=
literal|null
operator|&&
name|Iterators
operator|.
name|contains
argument_list|(
name|iterator
argument_list|()
argument_list|,
name|object
argument_list|)
return|;
block|}
DECL|method|containsAll (Collection<?> targets)
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|targets
parameter_list|)
block|{
return|return
name|Collections2
operator|.
name|containsAllImpl
argument_list|(
name|this
argument_list|,
name|targets
argument_list|)
return|;
block|}
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|size
argument_list|()
operator|==
literal|0
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
name|Collections2
operator|.
name|toStringImpl
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    */
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
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    */
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
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    */
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
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    */
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
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    */
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
comment|/**    * Guaranteed to throw an exception and leave the collection unmodified.    *    * @throws UnsupportedOperationException always    */
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
comment|/**    * Returns a list view of the collection.    *    * @since 2    */
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|false
argument_list|)
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
name|ImmutableAsList
argument_list|<
name|E
argument_list|>
argument_list|(
name|toArray
argument_list|()
argument_list|,
name|this
argument_list|)
return|;
block|}
block|}
DECL|method|isPartialView ()
specifier|abstract
name|boolean
name|isPartialView
parameter_list|()
function_decl|;
DECL|class|EmptyImmutableCollection
specifier|private
specifier|static
class|class
name|EmptyImmutableCollection
extends|extends
name|ImmutableCollection
argument_list|<
name|Object
argument_list|>
block|{
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
literal|0
return|;
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
literal|true
return|;
block|}
DECL|method|contains (@ullable Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
name|UnmodifiableIterator
argument_list|<
name|Object
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|EMPTY_ITERATOR
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
operator|new
name|Object
index|[
literal|0
index|]
decl_stmt|;
DECL|method|toArray ()
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|EMPTY_ARRAY
return|;
block|}
DECL|method|toArray (T[] array)
annotation|@
name|Override
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
if|if
condition|(
name|array
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|array
index|[
literal|0
index|]
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|array
return|;
block|}
DECL|method|createAsList ()
annotation|@
name|Override
name|ImmutableList
argument_list|<
name|Object
argument_list|>
name|createAsList
parameter_list|()
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
DECL|method|isPartialView ()
annotation|@
name|Override
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
comment|/**    * Nonempty collection stored in an array.    */
DECL|class|ArrayImmutableCollection
specifier|private
specifier|static
class|class
name|ArrayImmutableCollection
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableCollection
argument_list|<
name|E
argument_list|>
block|{
DECL|field|elements
specifier|private
specifier|final
name|E
index|[]
name|elements
decl_stmt|;
DECL|method|ArrayImmutableCollection (E[] elements)
name|ArrayImmutableCollection
parameter_list|(
name|E
index|[]
name|elements
parameter_list|)
block|{
name|this
operator|.
name|elements
operator|=
name|elements
expr_stmt|;
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|elements
operator|.
name|length
return|;
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
literal|false
return|;
block|}
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|forArray
argument_list|(
name|elements
argument_list|)
return|;
block|}
DECL|method|createAsList ()
annotation|@
name|Override
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|createAsList
parameter_list|()
block|{
return|return
name|elements
operator|.
name|length
operator|==
literal|1
condition|?
operator|new
name|SingletonImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|elements
index|[
literal|0
index|]
argument_list|)
else|:
operator|new
name|RegularImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|elements
argument_list|)
return|;
block|}
DECL|method|isPartialView ()
annotation|@
name|Override
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
comment|/*    * Serializes ImmutableCollections as their logical contents. This ensures    * that implementation types do not leak into the serialized representation.    */
DECL|class|SerializedForm
specifier|private
specifier|static
class|class
name|SerializedForm
implements|implements
name|Serializable
block|{
DECL|field|elements
specifier|final
name|Object
index|[]
name|elements
decl_stmt|;
DECL|method|SerializedForm (Object[] elements)
name|SerializedForm
parameter_list|(
name|Object
index|[]
name|elements
parameter_list|)
block|{
name|this
operator|.
name|elements
operator|=
name|elements
expr_stmt|;
block|}
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|elements
operator|.
name|length
operator|==
literal|0
condition|?
name|EMPTY_IMMUTABLE_COLLECTION
else|:
operator|new
name|ArrayImmutableCollection
argument_list|<
name|Object
argument_list|>
argument_list|(
name|Platform
operator|.
name|clone
argument_list|(
name|elements
argument_list|)
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
DECL|method|writeReplace ()
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|SerializedForm
argument_list|(
name|toArray
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Abstract base class for builders of {@link ImmutableCollection} types.    */
DECL|class|Builder
specifier|abstract
specifier|static
class|class
name|Builder
parameter_list|<
name|E
parameter_list|>
block|{
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
block|}
end_class

end_unit

