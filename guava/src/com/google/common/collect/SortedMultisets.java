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
name|collect
operator|.
name|Multiset
operator|.
name|Entry
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
name|Weak
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
name|NoSuchElementException
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
name|CheckForNull
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
comment|/**  * Provides static utility methods for creating and working with {@link SortedMultiset} instances.  *  * @author Louis Wasserman  */
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
name|ElementTypesAreNonnullByDefault
DECL|class|SortedMultisets
specifier|final
class|class
name|SortedMultisets
block|{
DECL|method|SortedMultisets ()
specifier|private
name|SortedMultisets
parameter_list|()
block|{}
comment|/** A skeleton implementation for {@link SortedMultiset#elementSet}. */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"JdkObsolete"
argument_list|)
comment|// TODO(b/6160855): Switch GWT emulations to NavigableSet.
DECL|class|ElementSet
specifier|static
name|class
name|ElementSet
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|Multisets
operator|.
name|ElementSet
argument_list|<
name|E
argument_list|>
expr|implements
name|SortedSet
argument_list|<
name|E
argument_list|>
block|{     @
DECL|field|multiset
name|Weak
specifier|private
name|final
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|multiset
block|;
DECL|method|ElementSet (SortedMultiset<E> multiset)
name|ElementSet
argument_list|(
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|multiset
argument_list|)
block|{
name|this
operator|.
name|multiset
operator|=
name|multiset
block|;     }
expr|@
name|Override
DECL|method|multiset ()
name|final
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|multiset
argument_list|()
block|{
return|return
name|multiset
return|;
block|}
expr|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
argument_list|()
block|{
return|return
name|Multisets
operator|.
name|elementIterator
argument_list|(
name|multiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
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
name|multiset
argument_list|()
operator|.
name|comparator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|subSet (@arametricNullness E fromElement, @ParametricNullness E toElement)
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|subSet
parameter_list|(
annotation|@
name|ParametricNullness
name|E
name|fromElement
parameter_list|,
annotation|@
name|ParametricNullness
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
annotation|@
name|Override
DECL|method|headSet (@arametricNullness E toElement)
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|headSet
parameter_list|(
annotation|@
name|ParametricNullness
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
annotation|@
name|Override
DECL|method|tailSet (@arametricNullness E fromElement)
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|tailSet
parameter_list|(
annotation|@
name|ParametricNullness
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
annotation|@
name|Override
annotation|@
name|ParametricNullness
DECL|method|first ()
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
annotation|@
name|Override
annotation|@
name|ParametricNullness
DECL|method|last ()
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
end_class

begin_comment
comment|/** A skeleton navigable implementation for {@link SortedMultiset#elementSet}. */
end_comment

begin_annotation
annotation|@
name|GwtIncompatible
end_annotation

begin_comment
comment|// Navigable
end_comment

begin_expr_stmt
DECL|class|NavigableElementSet
specifier|static
name|class
name|NavigableElementSet
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|ElementSet
argument_list|<
name|E
argument_list|>
expr|implements
name|NavigableSet
argument_list|<
name|E
argument_list|>
block|{
DECL|method|NavigableElementSet (SortedMultiset<E> multiset)
name|NavigableElementSet
argument_list|(
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|multiset
argument_list|)
block|{
name|super
argument_list|(
name|multiset
argument_list|)
block|;     }
expr|@
name|Override
expr|@
name|CheckForNull
DECL|method|lower (@arametricNullness E e)
specifier|public
name|E
name|lower
argument_list|(
annotation|@
name|ParametricNullness
name|E
name|e
argument_list|)
block|{
return|return
name|getElementOrNull
argument_list|(
name|multiset
argument_list|()
operator|.
name|headMultiset
argument_list|(
name|e
argument_list|,
name|OPEN
argument_list|)
operator|.
name|lastEntry
argument_list|()
argument_list|)
return|;
block|}
expr|@
name|Override
expr|@
name|CheckForNull
DECL|method|floor (@arametricNullness E e)
specifier|public
name|E
name|floor
argument_list|(
annotation|@
name|ParametricNullness
name|E
name|e
argument_list|)
block|{
return|return
name|getElementOrNull
argument_list|(
name|multiset
argument_list|()
operator|.
name|headMultiset
argument_list|(
name|e
argument_list|,
name|CLOSED
argument_list|)
operator|.
name|lastEntry
argument_list|()
argument_list|)
return|;
block|}
end_expr_stmt

begin_function
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|ceiling (@arametricNullness E e)
specifier|public
name|E
name|ceiling
parameter_list|(
annotation|@
name|ParametricNullness
name|E
name|e
parameter_list|)
block|{
return|return
name|getElementOrNull
argument_list|(
name|multiset
argument_list|()
operator|.
name|tailMultiset
argument_list|(
name|e
argument_list|,
name|CLOSED
argument_list|)
operator|.
name|firstEntry
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|higher (@arametricNullness E e)
specifier|public
name|E
name|higher
parameter_list|(
annotation|@
name|ParametricNullness
name|E
name|e
parameter_list|)
block|{
return|return
name|getElementOrNull
argument_list|(
name|multiset
argument_list|()
operator|.
name|tailMultiset
argument_list|(
name|e
argument_list|,
name|OPEN
argument_list|)
operator|.
name|firstEntry
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|descendingSet ()
specifier|public
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|descendingSet
parameter_list|()
block|{
return|return
operator|new
name|NavigableElementSet
argument_list|<>
argument_list|(
name|multiset
argument_list|()
operator|.
name|descendingMultiset
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|descendingIterator ()
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|descendingIterator
parameter_list|()
block|{
return|return
name|descendingSet
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|pollFirst ()
specifier|public
name|E
name|pollFirst
parameter_list|()
block|{
return|return
name|getElementOrNull
argument_list|(
name|multiset
argument_list|()
operator|.
name|pollFirstEntry
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|pollLast ()
specifier|public
name|E
name|pollLast
parameter_list|()
block|{
return|return
name|getElementOrNull
argument_list|(
name|multiset
argument_list|()
operator|.
name|pollLastEntry
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|subSet ( @arametricNullness E fromElement, boolean fromInclusive, @ParametricNullness E toElement, boolean toInclusive)
specifier|public
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|subSet
parameter_list|(
annotation|@
name|ParametricNullness
name|E
name|fromElement
parameter_list|,
name|boolean
name|fromInclusive
parameter_list|,
annotation|@
name|ParametricNullness
name|E
name|toElement
parameter_list|,
name|boolean
name|toInclusive
parameter_list|)
block|{
return|return
operator|new
name|NavigableElementSet
argument_list|<>
argument_list|(
name|multiset
argument_list|()
operator|.
name|subMultiset
argument_list|(
name|fromElement
argument_list|,
name|BoundType
operator|.
name|forBoolean
argument_list|(
name|fromInclusive
argument_list|)
argument_list|,
name|toElement
argument_list|,
name|BoundType
operator|.
name|forBoolean
argument_list|(
name|toInclusive
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|headSet (@arametricNullness E toElement, boolean inclusive)
specifier|public
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|headSet
parameter_list|(
annotation|@
name|ParametricNullness
name|E
name|toElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
return|return
operator|new
name|NavigableElementSet
argument_list|<>
argument_list|(
name|multiset
argument_list|()
operator|.
name|headMultiset
argument_list|(
name|toElement
argument_list|,
name|BoundType
operator|.
name|forBoolean
argument_list|(
name|inclusive
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|tailSet (@arametricNullness E fromElement, boolean inclusive)
specifier|public
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|tailSet
parameter_list|(
annotation|@
name|ParametricNullness
name|E
name|fromElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
return|return
operator|new
name|NavigableElementSet
argument_list|<>
argument_list|(
name|multiset
argument_list|()
operator|.
name|tailMultiset
argument_list|(
name|fromElement
argument_list|,
name|BoundType
operator|.
name|forBoolean
argument_list|(
name|inclusive
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
end_function

begin_expr_stmt
unit|}    private
DECL|method|getElementOrThrow (@heckForNull Entry<E> entry)
specifier|static
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
name|E
name|getElementOrThrow
argument_list|(
annotation|@
name|CheckForNull
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
argument_list|)
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
end_expr_stmt

begin_return
return|return
name|entry
operator|.
name|getElement
argument_list|()
return|;
end_return

begin_expr_stmt
unit|}    @
name|CheckForNull
DECL|method|getElementOrNull (@heckForNull Entry<E> entry)
specifier|private
specifier|static
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
name|E
name|getElementOrNull
argument_list|(
annotation|@
name|CheckForNull
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
argument_list|)
block|{
return|return
operator|(
name|entry
operator|==
literal|null
operator|)
operator|?
literal|null
operator|:
name|entry
operator|.
name|getElement
argument_list|()
return|;
block|}
end_expr_stmt

unit|}
end_unit

