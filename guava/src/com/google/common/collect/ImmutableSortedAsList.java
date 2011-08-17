begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the  * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  * express or implied. See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|base
operator|.
name|Preconditions
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * List returned by {@code ImmutableSortedSet.asList()} when the set isn't empty.  *  * @author Jared Levy  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
DECL|class|ImmutableSortedAsList
specifier|final
class|class
name|ImmutableSortedAsList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableList
argument_list|<
name|E
argument_list|>
implements|implements
name|SortedIterable
argument_list|<
name|E
argument_list|>
block|{
DECL|field|backingSet
specifier|private
specifier|final
specifier|transient
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|backingSet
decl_stmt|;
DECL|field|backingList
specifier|private
specifier|final
specifier|transient
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|backingList
decl_stmt|;
DECL|method|ImmutableSortedAsList ( ImmutableSortedSet<E> backingSet, ImmutableList<E> backingList)
name|ImmutableSortedAsList
parameter_list|(
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|backingSet
parameter_list|,
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|backingList
parameter_list|)
block|{
name|this
operator|.
name|backingSet
operator|=
name|backingSet
expr_stmt|;
name|this
operator|.
name|backingList
operator|=
name|backingList
expr_stmt|;
block|}
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
name|backingSet
operator|.
name|comparator
argument_list|()
return|;
block|}
comment|// Override contains(), indexOf(), and lastIndexOf() to be O(log N) instead of O(N).
DECL|method|contains (@ullable Object target)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|target
parameter_list|)
block|{
comment|// TODO: why not contains(target)?
return|return
name|backingSet
operator|.
name|indexOf
argument_list|(
name|target
argument_list|)
operator|>=
literal|0
return|;
block|}
DECL|method|indexOf (@ullable Object target)
annotation|@
name|Override
specifier|public
name|int
name|indexOf
parameter_list|(
annotation|@
name|Nullable
name|Object
name|target
parameter_list|)
block|{
return|return
name|backingSet
operator|.
name|indexOf
argument_list|(
name|target
argument_list|)
return|;
block|}
DECL|method|lastIndexOf (@ullable Object target)
annotation|@
name|Override
specifier|public
name|int
name|lastIndexOf
parameter_list|(
annotation|@
name|Nullable
name|Object
name|target
parameter_list|)
block|{
return|return
name|backingSet
operator|.
name|indexOf
argument_list|(
name|target
argument_list|)
return|;
block|}
comment|// The returned ImmutableSortedAsList maintains the contains(), indexOf(), and
comment|// lastIndexOf() performance benefits.
DECL|method|subList (int fromIndex, int toIndex)
annotation|@
name|Override
specifier|public
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|subList
parameter_list|(
name|int
name|fromIndex
parameter_list|,
name|int
name|toIndex
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkPositionIndexes
argument_list|(
name|fromIndex
argument_list|,
name|toIndex
argument_list|,
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|(
name|fromIndex
operator|==
name|toIndex
operator|)
condition|?
name|ImmutableList
operator|.
expr|<
name|E
operator|>
name|of
argument_list|()
else|:
operator|new
name|RegularImmutableSortedSet
argument_list|<
name|E
argument_list|>
argument_list|(
name|backingList
operator|.
name|subList
argument_list|(
name|fromIndex
argument_list|,
name|toIndex
argument_list|)
argument_list|,
name|backingSet
operator|.
name|comparator
argument_list|()
argument_list|)
operator|.
name|asList
argument_list|()
return|;
block|}
comment|// The ImmutableAsList serialized form has the correct behavior.
DECL|method|writeReplace ()
annotation|@
name|Override
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|ImmutableAsList
operator|.
name|SerializedForm
argument_list|(
name|backingSet
argument_list|)
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
name|backingList
operator|.
name|iterator
argument_list|()
return|;
block|}
DECL|method|get (int index)
annotation|@
name|Override
specifier|public
name|E
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|backingList
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
DECL|method|listIterator ()
annotation|@
name|Override
specifier|public
name|UnmodifiableListIterator
argument_list|<
name|E
argument_list|>
name|listIterator
parameter_list|()
block|{
return|return
name|backingList
operator|.
name|listIterator
argument_list|()
return|;
block|}
DECL|method|listIterator (int index)
annotation|@
name|Override
specifier|public
name|UnmodifiableListIterator
argument_list|<
name|E
argument_list|>
name|listIterator
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|backingList
operator|.
name|listIterator
argument_list|(
name|index
argument_list|)
return|;
block|}
DECL|method|size ()
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|backingList
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|equals (@ullable Object obj)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
block|{
return|return
name|backingList
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|backingList
operator|.
name|hashCode
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
name|backingList
operator|.
name|isPartialView
argument_list|()
return|;
block|}
block|}
end_class

end_unit

