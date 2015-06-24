begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * This class provides a skeletal implementation of the {@link SortedMultiset} interface.  *  *<p>The {@link #count} and {@link #size} implementations all iterate across the set returned by  * {@link Multiset#entrySet()}, as do many methods acting on the set returned by  * {@link #elementSet()}. Override those methods for better performance.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|AbstractSortedMultiset
specifier|abstract
class|class
name|AbstractSortedMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractMultiset
argument_list|<
name|E
argument_list|>
implements|implements
name|SortedMultiset
argument_list|<
name|E
argument_list|>
block|{
DECL|field|comparator
annotation|@
name|GwtTransient
specifier|final
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
decl_stmt|;
comment|// needed for serialization
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|AbstractSortedMultiset ()
name|AbstractSortedMultiset
parameter_list|()
block|{
name|this
argument_list|(
operator|(
name|Comparator
operator|)
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|AbstractSortedMultiset (Comparator<? super E> comparator)
name|AbstractSortedMultiset
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
name|this
operator|.
name|comparator
operator|=
name|checkNotNull
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
block|}
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
return|return
operator|(
name|NavigableSet
argument_list|<
name|E
argument_list|>
operator|)
name|super
operator|.
name|elementSet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createElementSet ()
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|createElementSet
parameter_list|()
block|{
return|return
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
return|return
name|comparator
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
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entryIterator
init|=
name|entryIterator
argument_list|()
decl_stmt|;
return|return
name|entryIterator
operator|.
name|hasNext
argument_list|()
condition|?
name|entryIterator
operator|.
name|next
argument_list|()
else|:
literal|null
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
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entryIterator
init|=
name|descendingEntryIterator
argument_list|()
decl_stmt|;
return|return
name|entryIterator
operator|.
name|hasNext
argument_list|()
condition|?
name|entryIterator
operator|.
name|next
argument_list|()
else|:
literal|null
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
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entryIterator
init|=
name|entryIterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|entryIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Entry
argument_list|<
name|E
argument_list|>
name|result
init|=
name|entryIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|result
operator|=
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|result
operator|.
name|getElement
argument_list|()
argument_list|,
name|result
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|entryIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
return|return
literal|null
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
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entryIterator
init|=
name|descendingEntryIterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|entryIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Entry
argument_list|<
name|E
argument_list|>
name|result
init|=
name|entryIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|result
operator|=
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|result
operator|.
name|getElement
argument_list|()
argument_list|,
name|result
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|entryIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|subMultiset ( @ullable E fromElement, BoundType fromBoundType, @Nullable E toElement, BoundType toBoundType)
specifier|public
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|subMultiset
parameter_list|(
annotation|@
name|Nullable
name|E
name|fromElement
parameter_list|,
name|BoundType
name|fromBoundType
parameter_list|,
annotation|@
name|Nullable
name|E
name|toElement
parameter_list|,
name|BoundType
name|toBoundType
parameter_list|)
block|{
comment|// These are checked elsewhere, but NullPointerTester wants them checked eagerly.
name|checkNotNull
argument_list|(
name|fromBoundType
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|toBoundType
argument_list|)
expr_stmt|;
return|return
name|tailMultiset
argument_list|(
name|fromElement
argument_list|,
name|fromBoundType
argument_list|)
operator|.
name|headMultiset
argument_list|(
name|toElement
argument_list|,
name|toBoundType
argument_list|)
return|;
block|}
DECL|method|descendingEntryIterator ()
specifier|abstract
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|descendingEntryIterator
parameter_list|()
function_decl|;
DECL|method|descendingIterator ()
name|Iterator
argument_list|<
name|E
argument_list|>
name|descendingIterator
parameter_list|()
block|{
return|return
name|Multisets
operator|.
name|iteratorImpl
argument_list|(
name|descendingMultiset
argument_list|()
argument_list|)
return|;
block|}
DECL|field|descendingMultiset
specifier|private
specifier|transient
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|descendingMultiset
decl_stmt|;
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
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|result
init|=
name|descendingMultiset
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|descendingMultiset
operator|=
name|createDescendingMultiset
argument_list|()
else|:
name|result
return|;
block|}
DECL|method|createDescendingMultiset ()
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|createDescendingMultiset
parameter_list|()
block|{
annotation|@
name|WeakOuter
class|class
name|DescendingMultisetImpl
extends|extends
name|DescendingMultiset
argument_list|<
name|E
argument_list|>
block|{
annotation|@
name|Override
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|forwardMultiset
parameter_list|()
block|{
return|return
name|AbstractSortedMultiset
operator|.
name|this
return|;
block|}
annotation|@
name|Override
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
name|descendingEntryIterator
argument_list|()
return|;
block|}
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
name|descendingIterator
argument_list|()
return|;
block|}
block|}
return|return
operator|new
name|DescendingMultisetImpl
argument_list|()
return|;
block|}
block|}
end_class

end_unit

