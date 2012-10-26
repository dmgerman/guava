begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not  * use this file except in compliance with the License. You may obtain a copy of  * the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the  * License for the specific language governing permissions and limitations under  * the License.  */
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
name|BoundType
operator|.
name|CLOSED
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
name|BoundType
operator|.
name|OPEN
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
name|collect
operator|.
name|Multiset
operator|.
name|Entry
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
name|NoSuchElementException
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
name|java
operator|.
name|util
operator|.
name|SortedSet
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
comment|/**  * Provides static utility methods for creating and working with  * {@link SortedMultiset} instances.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|SortedMultisets
specifier|final
class|class
name|SortedMultisets
block|{
DECL|method|SortedMultisets ()
specifier|private
name|SortedMultisets
parameter_list|()
block|{   }
comment|/**    * A skeleton implementation for {@link SortedMultiset#elementSet}.    */
DECL|class|ElementSet
specifier|static
specifier|abstract
class|class
name|ElementSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|Multisets
operator|.
name|ElementSet
argument_list|<
name|E
argument_list|>
implements|implements
name|SortedSet
argument_list|<
name|E
argument_list|>
block|{
DECL|method|multiset ()
annotation|@
name|Override
specifier|abstract
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|multiset
parameter_list|()
function_decl|;
DECL|method|comparator ()
annotation|@
name|Override
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|()
block|{
return|return
name|multiset
argument_list|()
operator|.
name|comparator
argument_list|()
return|;
block|}
DECL|method|subSet (E fromElement, E toElement)
annotation|@
name|Override
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|subSet
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|E
name|toElement
parameter_list|)
block|{
return|return
name|multiset
argument_list|()
operator|.
name|subMultiset
argument_list|(
name|fromElement
argument_list|,
name|CLOSED
argument_list|,
name|toElement
argument_list|,
name|OPEN
argument_list|)
operator|.
name|elementSet
argument_list|()
return|;
block|}
DECL|method|headSet (E toElement)
annotation|@
name|Override
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|headSet
parameter_list|(
name|E
name|toElement
parameter_list|)
block|{
return|return
name|multiset
argument_list|()
operator|.
name|headMultiset
argument_list|(
name|toElement
argument_list|,
name|OPEN
argument_list|)
operator|.
name|elementSet
argument_list|()
return|;
block|}
DECL|method|tailSet (E fromElement)
annotation|@
name|Override
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|tailSet
parameter_list|(
name|E
name|fromElement
parameter_list|)
block|{
return|return
name|multiset
argument_list|()
operator|.
name|tailMultiset
argument_list|(
name|fromElement
argument_list|,
name|CLOSED
argument_list|)
operator|.
name|elementSet
argument_list|()
return|;
block|}
DECL|method|first ()
annotation|@
name|Override
specifier|public
name|E
name|first
parameter_list|()
block|{
return|return
name|getElementOrThrow
argument_list|(
name|multiset
argument_list|()
operator|.
name|firstEntry
argument_list|()
argument_list|)
return|;
block|}
DECL|method|last ()
annotation|@
name|Override
specifier|public
name|E
name|last
parameter_list|()
block|{
return|return
name|getElementOrThrow
argument_list|(
name|multiset
argument_list|()
operator|.
name|lastEntry
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|getElementOrThrow (Entry<E> entry)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|E
name|getElementOrThrow
parameter_list|(
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
parameter_list|)
block|{
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
return|return
name|entry
operator|.
name|getElement
argument_list|()
return|;
block|}
DECL|method|getElementOrNull (@ullable Entry<E> entry)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|E
name|getElementOrNull
parameter_list|(
annotation|@
name|Nullable
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
parameter_list|)
block|{
return|return
operator|(
name|entry
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|entry
operator|.
name|getElement
argument_list|()
return|;
block|}
comment|/**    * A skeleton implementation of a descending multiset.  Only needs    * {@code forwardMultiset()} and {@code entryIterator()}.    */
DECL|class|DescendingMultiset
specifier|static
specifier|abstract
class|class
name|DescendingMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingMultiset
argument_list|<
name|E
argument_list|>
implements|implements
name|SortedMultiset
argument_list|<
name|E
argument_list|>
block|{
DECL|method|forwardMultiset ()
specifier|abstract
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|forwardMultiset
parameter_list|()
function_decl|;
DECL|field|comparator
specifier|private
specifier|transient
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
decl_stmt|;
DECL|method|comparator ()
annotation|@
name|Override
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|()
block|{
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|result
init|=
name|comparator
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
return|return
name|comparator
operator|=
name|Ordering
operator|.
name|from
argument_list|(
name|forwardMultiset
argument_list|()
operator|.
name|comparator
argument_list|()
argument_list|)
operator|.
operator|<
name|E
operator|>
name|reverse
argument_list|()
return|;
block|}
return|return
name|result
return|;
block|}
DECL|field|elementSet
specifier|private
specifier|transient
name|SortedSet
argument_list|<
name|E
argument_list|>
name|elementSet
decl_stmt|;
DECL|method|elementSet ()
annotation|@
name|Override
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|elementSet
parameter_list|()
block|{
name|SortedSet
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
return|return
name|elementSet
operator|=
operator|new
name|SortedMultisets
operator|.
name|ElementSet
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|multiset
parameter_list|()
block|{
return|return
name|DescendingMultiset
operator|.
name|this
return|;
block|}
block|}
return|;
block|}
return|return
name|result
return|;
block|}
DECL|method|pollFirstEntry ()
annotation|@
name|Override
specifier|public
name|Entry
argument_list|<
name|E
argument_list|>
name|pollFirstEntry
parameter_list|()
block|{
return|return
name|forwardMultiset
argument_list|()
operator|.
name|pollLastEntry
argument_list|()
return|;
block|}
DECL|method|pollLastEntry ()
annotation|@
name|Override
specifier|public
name|Entry
argument_list|<
name|E
argument_list|>
name|pollLastEntry
parameter_list|()
block|{
return|return
name|forwardMultiset
argument_list|()
operator|.
name|pollFirstEntry
argument_list|()
return|;
block|}
DECL|method|headMultiset (E toElement, BoundType boundType)
annotation|@
name|Override
specifier|public
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|headMultiset
parameter_list|(
name|E
name|toElement
parameter_list|,
name|BoundType
name|boundType
parameter_list|)
block|{
return|return
name|forwardMultiset
argument_list|()
operator|.
name|tailMultiset
argument_list|(
name|toElement
argument_list|,
name|boundType
argument_list|)
operator|.
name|descendingMultiset
argument_list|()
return|;
block|}
DECL|method|subMultiset (E fromElement, BoundType fromBoundType, E toElement, BoundType toBoundType)
annotation|@
name|Override
specifier|public
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|subMultiset
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|BoundType
name|fromBoundType
parameter_list|,
name|E
name|toElement
parameter_list|,
name|BoundType
name|toBoundType
parameter_list|)
block|{
return|return
name|forwardMultiset
argument_list|()
operator|.
name|subMultiset
argument_list|(
name|toElement
argument_list|,
name|toBoundType
argument_list|,
name|fromElement
argument_list|,
name|fromBoundType
argument_list|)
operator|.
name|descendingMultiset
argument_list|()
return|;
block|}
DECL|method|tailMultiset (E fromElement, BoundType boundType)
annotation|@
name|Override
specifier|public
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|tailMultiset
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|BoundType
name|boundType
parameter_list|)
block|{
return|return
name|forwardMultiset
argument_list|()
operator|.
name|headMultiset
argument_list|(
name|fromElement
argument_list|,
name|boundType
argument_list|)
operator|.
name|descendingMultiset
argument_list|()
return|;
block|}
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|forwardMultiset
argument_list|()
return|;
block|}
DECL|method|descendingMultiset ()
annotation|@
name|Override
specifier|public
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|descendingMultiset
parameter_list|()
block|{
return|return
name|forwardMultiset
argument_list|()
return|;
block|}
DECL|method|firstEntry ()
annotation|@
name|Override
specifier|public
name|Entry
argument_list|<
name|E
argument_list|>
name|firstEntry
parameter_list|()
block|{
return|return
name|forwardMultiset
argument_list|()
operator|.
name|lastEntry
argument_list|()
return|;
block|}
DECL|method|lastEntry ()
annotation|@
name|Override
specifier|public
name|Entry
argument_list|<
name|E
argument_list|>
name|lastEntry
parameter_list|()
block|{
return|return
name|forwardMultiset
argument_list|()
operator|.
name|firstEntry
argument_list|()
return|;
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
DECL|method|entrySet ()
annotation|@
name|Override
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
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|entrySet
operator|=
name|createEntrySet
argument_list|()
else|:
name|result
return|;
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
name|Multisets
operator|.
name|EntrySet
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
name|Multiset
argument_list|<
name|E
argument_list|>
name|multiset
parameter_list|()
block|{
return|return
name|DescendingMultiset
operator|.
name|this
return|;
block|}
annotation|@
name|Override
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
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|forwardMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|iterator ()
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
name|Multisets
operator|.
name|iteratorImpl
argument_list|(
name|this
argument_list|)
return|;
block|}
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
name|standardToArray
argument_list|()
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
return|return
name|standardToArray
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
name|entrySet
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

