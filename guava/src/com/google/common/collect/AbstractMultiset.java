begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|collect
operator|.
name|Multisets
operator|.
name|setCountImpl
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
name|Objects
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|j2objc
operator|.
name|annotations
operator|.
name|WeakOuter
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
name|Iterator
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * This class provides a skeletal implementation of the {@link Multiset}  * interface. A new multiset implementation can be created easily by extending  * this class and implementing the {@link Multiset#entrySet()} method, plus  * optionally overriding {@link #add(Object, int)} and  * {@link #remove(Object, int)} to enable modifications to the multiset.  *  *<p>The {@link #count} and {@link #size} implementations all iterate across  * the set returned by {@link Multiset#entrySet()}, as do many methods acting on  * the set returned by {@link #elementSet()}. Override those methods for better  * performance.  *  * @author Kevin Bourrillion  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AbstractMultiset
specifier|abstract
class|class
name|AbstractMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractCollection
argument_list|<
name|E
argument_list|>
implements|implements
name|Multiset
argument_list|<
name|E
argument_list|>
block|{
comment|// Query Operations
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|Multisets
operator|.
name|sizeImpl
argument_list|(
name|this
argument_list|)
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
name|entrySet
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|contains (@ullable Object element)
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|)
block|{
return|return
name|count
argument_list|(
name|element
argument_list|)
operator|>
literal|0
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
name|Multisets
operator|.
name|iteratorImpl
argument_list|(
name|this
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
for|for
control|(
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
range|:
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|Objects
operator|.
name|equal
argument_list|(
name|entry
operator|.
name|getElement
argument_list|()
argument_list|,
name|element
argument_list|)
condition|)
block|{
return|return
name|entry
operator|.
name|getCount
argument_list|()
return|;
block|}
block|}
return|return
literal|0
return|;
block|}
comment|// Modification Operations
annotation|@
name|Override
DECL|method|add (@ullable E element)
specifier|public
name|boolean
name|add
parameter_list|(
annotation|@
name|Nullable
name|E
name|element
parameter_list|)
block|{
name|add
argument_list|(
name|element
argument_list|,
literal|1
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|add (@ullable E element, int occurrences)
specifier|public
name|int
name|add
parameter_list|(
annotation|@
name|Nullable
name|E
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|remove (@ullable Object element)
specifier|public
name|boolean
name|remove
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|)
block|{
return|return
name|remove
argument_list|(
name|element
argument_list|,
literal|1
argument_list|)
operator|>
literal|0
return|;
block|}
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|setCount (@ullable E element, int count)
specifier|public
name|int
name|setCount
parameter_list|(
annotation|@
name|Nullable
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
return|return
name|setCountImpl
argument_list|(
name|this
argument_list|,
name|element
argument_list|,
name|count
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setCount (@ullable E element, int oldCount, int newCount)
specifier|public
name|boolean
name|setCount
parameter_list|(
annotation|@
name|Nullable
name|E
name|element
parameter_list|,
name|int
name|oldCount
parameter_list|,
name|int
name|newCount
parameter_list|)
block|{
return|return
name|setCountImpl
argument_list|(
name|this
argument_list|,
name|element
argument_list|,
name|oldCount
argument_list|,
name|newCount
argument_list|)
return|;
block|}
comment|// Bulk Operations
comment|/**    * {@inheritDoc}    *    *<p>This implementation is highly efficient when {@code elementsToAdd}    * is itself a {@link Multiset}.    */
annotation|@
name|Override
DECL|method|addAll (Collection<? extends E> elementsToAdd)
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
name|elementsToAdd
parameter_list|)
block|{
return|return
name|Multisets
operator|.
name|addAllImpl
argument_list|(
name|this
argument_list|,
name|elementsToAdd
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|removeAll (Collection<?> elementsToRemove)
specifier|public
name|boolean
name|removeAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|elementsToRemove
parameter_list|)
block|{
return|return
name|Multisets
operator|.
name|removeAllImpl
argument_list|(
name|this
argument_list|,
name|elementsToRemove
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|retainAll (Collection<?> elementsToRetain)
specifier|public
name|boolean
name|retainAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|elementsToRetain
parameter_list|)
block|{
return|return
name|Multisets
operator|.
name|retainAllImpl
argument_list|(
name|this
argument_list|,
name|elementsToRetain
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
name|Iterators
operator|.
name|clear
argument_list|(
name|entryIterator
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Views
DECL|field|elementSet
specifier|private
specifier|transient
name|Set
argument_list|<
name|E
argument_list|>
name|elementSet
decl_stmt|;
annotation|@
name|Override
DECL|method|elementSet ()
specifier|public
name|Set
argument_list|<
name|E
argument_list|>
name|elementSet
parameter_list|()
block|{
name|Set
argument_list|<
name|E
argument_list|>
name|result
init|=
name|elementSet
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|elementSet
operator|=
name|result
operator|=
name|createElementSet
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**    * Creates a new instance of this multiset's element set, which will be    * returned by {@link #elementSet()}.    */
DECL|method|createElementSet ()
name|Set
argument_list|<
name|E
argument_list|>
name|createElementSet
parameter_list|()
block|{
return|return
operator|new
name|ElementSet
argument_list|()
return|;
block|}
annotation|@
name|WeakOuter
DECL|class|ElementSet
class|class
name|ElementSet
extends|extends
name|Multisets
operator|.
name|ElementSet
argument_list|<
name|E
argument_list|>
block|{
annotation|@
name|Override
DECL|method|multiset ()
name|Multiset
argument_list|<
name|E
argument_list|>
name|multiset
parameter_list|()
block|{
return|return
name|AbstractMultiset
operator|.
name|this
return|;
block|}
block|}
DECL|method|entryIterator ()
specifier|abstract
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entryIterator
parameter_list|()
function_decl|;
DECL|method|distinctElements ()
specifier|abstract
name|int
name|distinctElements
parameter_list|()
function_decl|;
DECL|field|entrySet
specifier|private
specifier|transient
name|Set
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entrySet
decl_stmt|;
annotation|@
name|Override
DECL|method|entrySet ()
specifier|public
name|Set
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
name|Set
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|result
init|=
name|entrySet
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|entrySet
operator|=
name|result
operator|=
name|createEntrySet
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|WeakOuter
DECL|class|EntrySet
class|class
name|EntrySet
extends|extends
name|Multisets
operator|.
name|EntrySet
argument_list|<
name|E
argument_list|>
block|{
annotation|@
name|Override
DECL|method|multiset ()
name|Multiset
argument_list|<
name|E
argument_list|>
name|multiset
parameter_list|()
block|{
return|return
name|AbstractMultiset
operator|.
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|entryIterator
argument_list|()
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
name|distinctElements
argument_list|()
return|;
block|}
block|}
DECL|method|createEntrySet ()
name|Set
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|createEntrySet
parameter_list|()
block|{
return|return
operator|new
name|EntrySet
argument_list|()
return|;
block|}
comment|// Object methods
comment|/**    * {@inheritDoc}    *    *<p>This implementation returns {@code true} if {@code object} is a multiset    * of the same size and if, for each element, the two multisets have the same    * count.    */
annotation|@
name|Override
DECL|method|equals (@ullable Object object)
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
return|return
name|Multisets
operator|.
name|equalsImpl
argument_list|(
name|this
argument_list|,
name|object
argument_list|)
return|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>This implementation returns the hash code of {@link    * Multiset#entrySet()}.    */
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|entrySet
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>This implementation returns the result of invoking {@code toString} on    * {@link Multiset#entrySet()}.    */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|entrySet
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

