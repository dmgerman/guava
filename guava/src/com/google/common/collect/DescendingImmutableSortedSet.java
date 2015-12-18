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
name|GwtIncompatible
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
comment|/**  * Skeletal implementation of {@link ImmutableSortedSet#descendingSet()}.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|DescendingImmutableSortedSet
class|class
name|DescendingImmutableSortedSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
block|{
DECL|field|forward
specifier|private
specifier|final
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|forward
decl_stmt|;
DECL|method|DescendingImmutableSortedSet (ImmutableSortedSet<E> forward)
name|DescendingImmutableSortedSet
parameter_list|(
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|forward
parameter_list|)
block|{
name|super
argument_list|(
name|Ordering
operator|.
name|from
argument_list|(
name|forward
operator|.
name|comparator
argument_list|()
argument_list|)
operator|.
name|reverse
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|forward
operator|=
name|forward
expr_stmt|;
block|}
annotation|@
name|Override
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
name|forward
operator|.
name|contains
argument_list|(
name|object
argument_list|)
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
name|forward
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|forward
operator|.
name|descendingIterator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|headSetImpl (E toElement, boolean inclusive)
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|headSetImpl
parameter_list|(
name|E
name|toElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
return|return
name|forward
operator|.
name|tailSet
argument_list|(
name|toElement
argument_list|,
name|inclusive
argument_list|)
operator|.
name|descendingSet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|subSetImpl ( E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|subSetImpl
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|boolean
name|fromInclusive
parameter_list|,
name|E
name|toElement
parameter_list|,
name|boolean
name|toInclusive
parameter_list|)
block|{
return|return
name|forward
operator|.
name|subSet
argument_list|(
name|toElement
argument_list|,
name|toInclusive
argument_list|,
name|fromElement
argument_list|,
name|fromInclusive
argument_list|)
operator|.
name|descendingSet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|tailSetImpl (E fromElement, boolean inclusive)
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|tailSetImpl
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
return|return
name|forward
operator|.
name|headSet
argument_list|(
name|fromElement
argument_list|,
name|inclusive
argument_list|)
operator|.
name|descendingSet
argument_list|()
return|;
block|}
annotation|@
name|Override
annotation|@
name|GwtIncompatible
argument_list|(
literal|"NavigableSet"
argument_list|)
DECL|method|descendingSet ()
specifier|public
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|descendingSet
parameter_list|()
block|{
return|return
name|forward
return|;
block|}
annotation|@
name|Override
annotation|@
name|GwtIncompatible
argument_list|(
literal|"NavigableSet"
argument_list|)
DECL|method|descendingIterator ()
specifier|public
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
name|descendingIterator
parameter_list|()
block|{
return|return
name|forward
operator|.
name|iterator
argument_list|()
return|;
block|}
annotation|@
name|Override
annotation|@
name|GwtIncompatible
argument_list|(
literal|"NavigableSet"
argument_list|)
DECL|method|createDescendingSet ()
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
name|createDescendingSet
parameter_list|()
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"should never be called"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|lower (E element)
specifier|public
name|E
name|lower
parameter_list|(
name|E
name|element
parameter_list|)
block|{
return|return
name|forward
operator|.
name|higher
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|floor (E element)
specifier|public
name|E
name|floor
parameter_list|(
name|E
name|element
parameter_list|)
block|{
return|return
name|forward
operator|.
name|ceiling
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|ceiling (E element)
specifier|public
name|E
name|ceiling
parameter_list|(
name|E
name|element
parameter_list|)
block|{
return|return
name|forward
operator|.
name|floor
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|higher (E element)
specifier|public
name|E
name|higher
parameter_list|(
name|E
name|element
parameter_list|)
block|{
return|return
name|forward
operator|.
name|lower
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|indexOf (@ullable Object target)
name|int
name|indexOf
parameter_list|(
annotation|@
name|Nullable
name|Object
name|target
parameter_list|)
block|{
name|int
name|index
init|=
name|forward
operator|.
name|indexOf
argument_list|(
name|target
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|==
operator|-
literal|1
condition|)
block|{
return|return
name|index
return|;
block|}
else|else
block|{
return|return
name|size
argument_list|()
operator|-
literal|1
operator|-
name|index
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
name|forward
operator|.
name|isPartialView
argument_list|()
return|;
block|}
block|}
end_class

end_unit

