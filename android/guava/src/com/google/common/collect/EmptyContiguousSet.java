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
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
import|;
end_import

begin_comment
comment|/**  * An empty contiguous set.  *  * @author Gregory Kick  */
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
literal|"rawtypes"
argument_list|)
comment|// allow ungenerified Comparable types
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|EmptyContiguousSet
specifier|final
class|class
name|EmptyContiguousSet
parameter_list|<
name|C
extends|extends
name|Comparable
parameter_list|>
extends|extends
name|ContiguousSet
argument_list|<
name|C
argument_list|>
block|{
DECL|method|EmptyContiguousSet (DiscreteDomain<C> domain)
name|EmptyContiguousSet
parameter_list|(
name|DiscreteDomain
argument_list|<
name|C
argument_list|>
name|domain
parameter_list|)
block|{
name|super
argument_list|(
name|domain
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|first ()
specifier|public
name|C
name|first
parameter_list|()
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|last ()
specifier|public
name|C
name|last
parameter_list|()
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
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
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|intersection (ContiguousSet<C> other)
specifier|public
name|ContiguousSet
argument_list|<
name|C
argument_list|>
name|intersection
parameter_list|(
name|ContiguousSet
argument_list|<
name|C
argument_list|>
name|other
parameter_list|)
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|range ()
specifier|public
name|Range
argument_list|<
name|C
argument_list|>
name|range
parameter_list|()
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|range (BoundType lowerBoundType, BoundType upperBoundType)
specifier|public
name|Range
argument_list|<
name|C
argument_list|>
name|range
parameter_list|(
name|BoundType
name|lowerBoundType
parameter_list|,
name|BoundType
name|upperBoundType
parameter_list|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|headSetImpl (C toElement, boolean inclusive)
name|ContiguousSet
argument_list|<
name|C
argument_list|>
name|headSetImpl
parameter_list|(
name|C
name|toElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|subSetImpl ( C fromElement, boolean fromInclusive, C toElement, boolean toInclusive)
name|ContiguousSet
argument_list|<
name|C
argument_list|>
name|subSetImpl
parameter_list|(
name|C
name|fromElement
parameter_list|,
name|boolean
name|fromInclusive
parameter_list|,
name|C
name|toElement
parameter_list|,
name|boolean
name|toInclusive
parameter_list|)
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|tailSetImpl (C fromElement, boolean fromInclusive)
name|ContiguousSet
argument_list|<
name|C
argument_list|>
name|tailSetImpl
parameter_list|(
name|C
name|fromElement
parameter_list|,
name|boolean
name|fromInclusive
parameter_list|)
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|contains (@heckForNull Object object)
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|object
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// not used by GWT emulation
annotation|@
name|Override
DECL|method|indexOf (@heckForNull Object target)
name|int
name|indexOf
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|target
parameter_list|)
block|{
return|return
operator|-
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|UnmodifiableIterator
argument_list|<
name|C
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|emptyIterator
argument_list|()
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// NavigableSet
annotation|@
name|Override
DECL|method|descendingIterator ()
specifier|public
name|UnmodifiableIterator
argument_list|<
name|C
argument_list|>
name|descendingIterator
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|emptyIterator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
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
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|asList ()
specifier|public
name|ImmutableList
argument_list|<
name|C
argument_list|>
name|asList
parameter_list|()
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
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
literal|"[]"
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@heckForNull Object object)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|Set
condition|)
block|{
name|Set
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|Set
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|that
operator|.
name|isEmpty
argument_list|()
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// not used in GWT
annotation|@
name|Override
DECL|method|isHashCodeFast ()
name|boolean
name|isHashCodeFast
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// serialization
DECL|class|SerializedForm
specifier|private
specifier|static
specifier|final
class|class
name|SerializedForm
parameter_list|<
name|C
extends|extends
name|Comparable
parameter_list|>
implements|implements
name|Serializable
block|{
DECL|field|domain
specifier|private
specifier|final
name|DiscreteDomain
argument_list|<
name|C
argument_list|>
name|domain
decl_stmt|;
DECL|method|SerializedForm (DiscreteDomain<C> domain)
specifier|private
name|SerializedForm
parameter_list|(
name|DiscreteDomain
argument_list|<
name|C
argument_list|>
name|domain
parameter_list|)
block|{
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
block|}
DECL|method|readResolve ()
specifier|private
name|Object
name|readResolve
parameter_list|()
block|{
return|return
operator|new
name|EmptyContiguousSet
argument_list|<>
argument_list|(
name|domain
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
annotation|@
name|GwtIncompatible
comment|// serialization
annotation|@
name|Override
DECL|method|writeReplace ()
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|SerializedForm
argument_list|<>
argument_list|(
name|domain
argument_list|)
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// NavigableSet
annotation|@
name|Override
DECL|method|createDescendingSet ()
name|ImmutableSortedSet
argument_list|<
name|C
argument_list|>
name|createDescendingSet
parameter_list|()
block|{
return|return
name|ImmutableSortedSet
operator|.
name|emptySet
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|reverse
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

