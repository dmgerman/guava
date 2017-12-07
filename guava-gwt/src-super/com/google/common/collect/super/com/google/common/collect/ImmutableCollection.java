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
name|Iterator
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
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_comment
comment|/**  * GWT emulated version of {@link ImmutableCollection}.  *  * @author Jesse Wilson  */
end_comment

begin_class
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
extends|extends
name|AbstractCollection
argument_list|<
name|E
argument_list|>
implements|implements
name|Serializable
block|{
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
name|ForwardingImmutableCollection
argument_list|<
name|Object
argument_list|>
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|ImmutableCollection ()
name|ImmutableCollection
parameter_list|()
block|{}
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
DECL|method|contains (@ullableDecl Object object)
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|object
parameter_list|)
block|{
return|return
name|object
operator|!=
literal|null
operator|&&
name|super
operator|.
name|contains
argument_list|(
name|object
argument_list|)
return|;
block|}
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
DECL|field|asList
specifier|private
specifier|transient
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|asList
decl_stmt|;
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
DECL|method|unsafeDelegate (Collection<E> delegate)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ImmutableCollection
argument_list|<
name|E
argument_list|>
name|unsafeDelegate
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|)
block|{
return|return
operator|new
name|ForwardingImmutableCollection
argument_list|<
name|E
argument_list|>
argument_list|(
name|delegate
argument_list|)
return|;
block|}
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/** GWT emulated version of {@link ImmutableCollection.Builder}. */
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
DECL|method|Builder ()
name|Builder
parameter_list|()
block|{}
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
name|checkNotNull
argument_list|(
name|elements
argument_list|)
expr_stmt|;
comment|// for GWT
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
name|checkNotNull
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
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
name|checkNotNull
argument_list|(
name|elements
argument_list|)
expr_stmt|;
comment|// for GWT
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
name|checkNotNull
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
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
name|checkNotNull
argument_list|(
name|elements
argument_list|)
expr_stmt|;
comment|// for GWT
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
name|checkNotNull
argument_list|(
name|elements
operator|.
name|next
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
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

