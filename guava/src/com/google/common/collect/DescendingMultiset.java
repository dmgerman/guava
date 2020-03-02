begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|NavigableSet
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
comment|/**  * A skeleton implementation of a descending multiset. Only needs {@code forwardMultiset()} and  * {@code entryIterator()}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|DescendingMultiset
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
annotation|@
name|Nullable
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
decl_stmt|;
annotation|@
name|Override
DECL|method|comparator ()
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
annotation|@
name|Nullable
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|elementSet
decl_stmt|;
annotation|@
name|Override
DECL|method|elementSet ()
specifier|public
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|elementSet
parameter_list|()
block|{
name|NavigableSet
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
name|NavigableElementSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|this
argument_list|)
return|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|pollFirstEntry ()
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
annotation|@
name|Override
DECL|method|pollLastEntry ()
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
annotation|@
name|Override
DECL|method|headMultiset (E toElement, BoundType boundType)
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
annotation|@
name|Override
DECL|method|subMultiset ( E fromElement, BoundType fromBoundType, E toElement, BoundType toBoundType)
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
annotation|@
name|Override
DECL|method|tailMultiset (E fromElement, BoundType boundType)
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
annotation|@
name|Override
DECL|method|delegate ()
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
annotation|@
name|Override
DECL|method|descendingMultiset ()
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
annotation|@
name|Override
DECL|method|firstEntry ()
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
annotation|@
name|Override
DECL|method|lastEntry ()
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
annotation|@
name|Nullable
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
annotation|@
name|WeakOuter
class|class
name|EntrySetImpl
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
return|return
operator|new
name|EntrySetImpl
argument_list|()
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
DECL|method|toArray ()
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
name|standardToArray
argument_list|(
name|array
argument_list|)
return|;
block|}
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

