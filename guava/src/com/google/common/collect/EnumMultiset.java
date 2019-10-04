begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|CollectPreconditions
operator|.
name|checkRemove
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
name|primitives
operator|.
name|Ints
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
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
name|Arrays
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
name|NoSuchElementException
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
name|ObjIntConsumer
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
comment|/**  * Multiset implementation specialized for enum elements, supporting all single-element operations  * in O(1).  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#multiset"> {@code  * Multiset}</a>.  *  * @author Jared Levy  * @since 2.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|EnumMultiset
specifier|public
specifier|final
class|class
name|EnumMultiset
parameter_list|<
name|E
extends|extends
name|Enum
parameter_list|<
name|E
parameter_list|>
parameter_list|>
extends|extends
name|AbstractMultiset
argument_list|<
name|E
argument_list|>
implements|implements
name|Serializable
block|{
comment|/** Creates an empty {@code EnumMultiset}. */
DECL|method|create (Class<E> type)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Enum
argument_list|<
name|E
argument_list|>
parameter_list|>
name|EnumMultiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Class
argument_list|<
name|E
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|EnumMultiset
argument_list|<
name|E
argument_list|>
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/**    * Creates a new {@code EnumMultiset} containing the specified elements.    *    *<p>This implementation is highly efficient when {@code elements} is itself a {@link Multiset}.    *    * @param elements the elements that the multiset should contain    * @throws IllegalArgumentException if {@code elements} is empty    */
DECL|method|create (Iterable<E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Enum
argument_list|<
name|E
argument_list|>
parameter_list|>
name|EnumMultiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|elements
parameter_list|)
block|{
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
init|=
name|elements
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|iterator
operator|.
name|hasNext
argument_list|()
argument_list|,
literal|"EnumMultiset constructor passed empty Iterable"
argument_list|)
expr_stmt|;
name|EnumMultiset
argument_list|<
name|E
argument_list|>
name|multiset
init|=
operator|new
name|EnumMultiset
argument_list|<>
argument_list|(
name|iterator
operator|.
name|next
argument_list|()
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
decl_stmt|;
name|Iterables
operator|.
name|addAll
argument_list|(
name|multiset
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|multiset
return|;
block|}
comment|/**    * Returns a new {@code EnumMultiset} instance containing the given elements. Unlike {@link    * EnumMultiset#create(Iterable)}, this method does not produce an exception on an empty iterable.    *    * @since 14.0    */
DECL|method|create (Iterable<E> elements, Class<E> type)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Enum
argument_list|<
name|E
argument_list|>
parameter_list|>
name|EnumMultiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|elements
parameter_list|,
name|Class
argument_list|<
name|E
argument_list|>
name|type
parameter_list|)
block|{
name|EnumMultiset
argument_list|<
name|E
argument_list|>
name|result
init|=
name|create
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|Iterables
operator|.
name|addAll
argument_list|(
name|result
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|field|type
specifier|private
specifier|transient
name|Class
argument_list|<
name|E
argument_list|>
name|type
decl_stmt|;
DECL|field|enumConstants
specifier|private
specifier|transient
name|E
index|[]
name|enumConstants
decl_stmt|;
DECL|field|counts
specifier|private
specifier|transient
name|int
index|[]
name|counts
decl_stmt|;
DECL|field|distinctElements
specifier|private
specifier|transient
name|int
name|distinctElements
decl_stmt|;
DECL|field|size
specifier|private
specifier|transient
name|long
name|size
decl_stmt|;
comment|/** Creates an empty {@code EnumMultiset}. */
DECL|method|EnumMultiset (Class<E> type)
specifier|private
name|EnumMultiset
parameter_list|(
name|Class
argument_list|<
name|E
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|checkArgument
argument_list|(
name|type
operator|.
name|isEnum
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|enumConstants
operator|=
name|type
operator|.
name|getEnumConstants
argument_list|()
expr_stmt|;
name|this
operator|.
name|counts
operator|=
operator|new
name|int
index|[
name|enumConstants
operator|.
name|length
index|]
expr_stmt|;
block|}
DECL|method|isActuallyE (@ullable Object o)
specifier|private
name|boolean
name|isActuallyE
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Enum
condition|)
block|{
name|Enum
argument_list|<
name|?
argument_list|>
name|e
init|=
operator|(
name|Enum
argument_list|<
name|?
argument_list|>
operator|)
name|o
decl_stmt|;
name|int
name|index
init|=
name|e
operator|.
name|ordinal
argument_list|()
decl_stmt|;
return|return
name|index
operator|<
name|enumConstants
operator|.
name|length
operator|&&
name|enumConstants
index|[
name|index
index|]
operator|==
name|e
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**    * Returns {@code element} cast to {@code E}, if it actually is a nonnull E. Otherwise, throws    * either a NullPointerException or a ClassCastException as appropriate.    */
DECL|method|checkIsE (@ullable Object element)
name|void
name|checkIsE
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|element
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isActuallyE
argument_list|(
name|element
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ClassCastException
argument_list|(
literal|"Expected an "
operator|+
name|type
operator|+
literal|" but got "
operator|+
name|element
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|distinctElements ()
name|int
name|distinctElements
parameter_list|()
block|{
return|return
name|distinctElements
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
name|Ints
operator|.
name|saturatedCast
argument_list|(
name|size
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|count (@ullable Object element)
specifier|public
name|int
name|count
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isActuallyE
argument_list|(
name|element
argument_list|)
condition|)
block|{
return|return
literal|0
return|;
block|}
name|Enum
argument_list|<
name|?
argument_list|>
name|e
init|=
operator|(
name|Enum
argument_list|<
name|?
argument_list|>
operator|)
name|element
decl_stmt|;
return|return
name|counts
index|[
name|e
operator|.
name|ordinal
argument_list|()
index|]
return|;
block|}
comment|// Modification Operations
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|add (E element, int occurrences)
specifier|public
name|int
name|add
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
name|checkIsE
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|checkNonnegative
argument_list|(
name|occurrences
argument_list|,
literal|"occurrences"
argument_list|)
expr_stmt|;
if|if
condition|(
name|occurrences
operator|==
literal|0
condition|)
block|{
return|return
name|count
argument_list|(
name|element
argument_list|)
return|;
block|}
name|int
name|index
init|=
name|element
operator|.
name|ordinal
argument_list|()
decl_stmt|;
name|int
name|oldCount
init|=
name|counts
index|[
name|index
index|]
decl_stmt|;
name|long
name|newCount
init|=
operator|(
name|long
operator|)
name|oldCount
operator|+
name|occurrences
decl_stmt|;
name|checkArgument
argument_list|(
name|newCount
operator|<=
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
literal|"too many occurrences: %s"
argument_list|,
name|newCount
argument_list|)
expr_stmt|;
name|counts
index|[
name|index
index|]
operator|=
operator|(
name|int
operator|)
name|newCount
expr_stmt|;
if|if
condition|(
name|oldCount
operator|==
literal|0
condition|)
block|{
name|distinctElements
operator|++
expr_stmt|;
block|}
name|size
operator|+=
name|occurrences
expr_stmt|;
return|return
name|oldCount
return|;
block|}
comment|// Modification Operations
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|remove (@ullable Object element, int occurrences)
specifier|public
name|int
name|remove
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isActuallyE
argument_list|(
name|element
argument_list|)
condition|)
block|{
return|return
literal|0
return|;
block|}
name|Enum
argument_list|<
name|?
argument_list|>
name|e
init|=
operator|(
name|Enum
argument_list|<
name|?
argument_list|>
operator|)
name|element
decl_stmt|;
name|checkNonnegative
argument_list|(
name|occurrences
argument_list|,
literal|"occurrences"
argument_list|)
expr_stmt|;
if|if
condition|(
name|occurrences
operator|==
literal|0
condition|)
block|{
return|return
name|count
argument_list|(
name|element
argument_list|)
return|;
block|}
name|int
name|index
init|=
name|e
operator|.
name|ordinal
argument_list|()
decl_stmt|;
name|int
name|oldCount
init|=
name|counts
index|[
name|index
index|]
decl_stmt|;
if|if
condition|(
name|oldCount
operator|==
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
elseif|else
if|if
condition|(
name|oldCount
operator|<=
name|occurrences
condition|)
block|{
name|counts
index|[
name|index
index|]
operator|=
literal|0
expr_stmt|;
name|distinctElements
operator|--
expr_stmt|;
name|size
operator|-=
name|oldCount
expr_stmt|;
block|}
else|else
block|{
name|counts
index|[
name|index
index|]
operator|=
name|oldCount
operator|-
name|occurrences
expr_stmt|;
name|size
operator|-=
name|occurrences
expr_stmt|;
block|}
return|return
name|oldCount
return|;
block|}
comment|// Modification Operations
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|setCount (E element, int count)
specifier|public
name|int
name|setCount
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|checkIsE
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|checkNonnegative
argument_list|(
name|count
argument_list|,
literal|"count"
argument_list|)
expr_stmt|;
name|int
name|index
init|=
name|element
operator|.
name|ordinal
argument_list|()
decl_stmt|;
name|int
name|oldCount
init|=
name|counts
index|[
name|index
index|]
decl_stmt|;
name|counts
index|[
name|index
index|]
operator|=
name|count
expr_stmt|;
name|size
operator|+=
name|count
operator|-
name|oldCount
expr_stmt|;
if|if
condition|(
name|oldCount
operator|==
literal|0
operator|&&
name|count
operator|>
literal|0
condition|)
block|{
name|distinctElements
operator|++
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|oldCount
operator|>
literal|0
operator|&&
name|count
operator|==
literal|0
condition|)
block|{
name|distinctElements
operator|--
expr_stmt|;
block|}
return|return
name|oldCount
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
name|Arrays
operator|.
name|fill
argument_list|(
name|counts
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|size
operator|=
literal|0
expr_stmt|;
name|distinctElements
operator|=
literal|0
expr_stmt|;
block|}
DECL|class|Itr
specifier|abstract
class|class
name|Itr
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Iterator
argument_list|<
name|T
argument_list|>
block|{
DECL|field|index
name|int
name|index
init|=
literal|0
decl_stmt|;
DECL|field|toRemove
name|int
name|toRemove
init|=
operator|-
literal|1
decl_stmt|;
DECL|method|output (int index)
specifier|abstract
name|T
name|output
parameter_list|(
name|int
name|index
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
for|for
control|(
init|;
name|index
operator|<
name|enumConstants
operator|.
name|length
condition|;
name|index
operator|++
control|)
block|{
if|if
condition|(
name|counts
index|[
name|index
index|]
operator|>
literal|0
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
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|T
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
name|T
name|result
init|=
name|output
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|toRemove
operator|=
name|index
expr_stmt|;
name|index
operator|++
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|checkRemove
argument_list|(
name|toRemove
operator|>=
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|counts
index|[
name|toRemove
index|]
operator|>
literal|0
condition|)
block|{
name|distinctElements
operator|--
expr_stmt|;
name|size
operator|-=
name|counts
index|[
name|toRemove
index|]
expr_stmt|;
name|counts
index|[
name|toRemove
index|]
operator|=
literal|0
expr_stmt|;
block|}
name|toRemove
operator|=
operator|-
literal|1
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|elementIterator ()
name|Iterator
argument_list|<
name|E
argument_list|>
name|elementIterator
parameter_list|()
block|{
return|return
operator|new
name|Itr
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
name|E
name|output
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|enumConstants
index|[
name|index
index|]
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|entryIterator ()
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entryIterator
parameter_list|()
block|{
return|return
operator|new
name|Itr
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
name|Entry
argument_list|<
name|E
argument_list|>
name|output
parameter_list|(
specifier|final
name|int
name|index
parameter_list|)
block|{
return|return
operator|new
name|Multisets
operator|.
name|AbstractEntry
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|E
name|getElement
parameter_list|()
block|{
return|return
name|enumConstants
index|[
name|index
index|]
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getCount
parameter_list|()
block|{
return|return
name|counts
index|[
name|index
index|]
return|;
block|}
block|}
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|forEachEntry (ObjIntConsumer<? super E> action)
specifier|public
name|void
name|forEachEntry
parameter_list|(
name|ObjIntConsumer
argument_list|<
name|?
super|super
name|E
argument_list|>
name|action
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|action
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|enumConstants
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|counts
index|[
name|i
index|]
operator|>
literal|0
condition|)
block|{
name|action
operator|.
name|accept
argument_list|(
name|enumConstants
index|[
name|i
index|]
argument_list|,
name|counts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
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
name|Multisets
operator|.
name|iteratorImpl
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// java.io.ObjectOutputStream
DECL|method|writeObject (ObjectOutputStream stream)
specifier|private
name|void
name|writeObject
parameter_list|(
name|ObjectOutputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|stream
operator|.
name|defaultWriteObject
argument_list|()
expr_stmt|;
name|stream
operator|.
name|writeObject
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|Serialization
operator|.
name|writeMultiset
argument_list|(
name|this
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
comment|/**    * @serialData the {@code Class<E>} for the enum type, the number of distinct elements, the first    *     element, its count, the second element, its count, and so on    */
annotation|@
name|GwtIncompatible
comment|// java.io.ObjectInputStream
DECL|method|readObject (ObjectInputStream stream)
specifier|private
name|void
name|readObject
parameter_list|(
name|ObjectInputStream
name|stream
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|stream
operator|.
name|defaultReadObject
argument_list|()
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// reading data stored by writeObject
name|Class
argument_list|<
name|E
argument_list|>
name|localType
init|=
operator|(
name|Class
argument_list|<
name|E
argument_list|>
operator|)
name|stream
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|type
operator|=
name|localType
expr_stmt|;
name|enumConstants
operator|=
name|type
operator|.
name|getEnumConstants
argument_list|()
expr_stmt|;
name|counts
operator|=
operator|new
name|int
index|[
name|enumConstants
operator|.
name|length
index|]
expr_stmt|;
name|Serialization
operator|.
name|populateMultiset
argument_list|(
name|this
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// Not needed in emulated source
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

