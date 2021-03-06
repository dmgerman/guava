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
name|Beta
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
comment|/**  * A navigable set which forwards all its method calls to another navigable set. Subclasses should  * override one or more methods to modify the behavior of the backing set as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p><b>Warning:</b> The methods of {@code ForwardingNavigableSet} forward<i>indiscriminately</i>  * to the methods of the delegate. For example, overriding {@link #add} alone<i>will not</i> change  * the behavior of {@link #addAll}, which can lead to unexpected behavior. In this case, you should  * override {@code addAll} as well, either providing your own implementation, or delegating to the  * provided {@code standardAddAll} method.  *  *<p><b>{@code default} method warning:</b> This class does<i>not</i> forward calls to {@code  * default} methods. Instead, it inherits their default implementations. When those implementations  * invoke methods, they invoke methods on the {@code ForwardingNavigableSet}.  *  *<p>Each of the {@code standard} methods uses the set's comparator (or the natural ordering of the  * elements, if there is no comparator) to test element equality. As a result, if the comparator is  * not consistent with equals, some of the standard implementations may violate the {@code Set}  * contract.  *  *<p>The {@code standard} methods and the collection views they return are not guaranteed to be  * thread-safe, even when all of the methods that they depend on are thread-safe.  *  * @author Louis Wasserman  * @since 12.0  */
end_comment

begin_annotation
annotation|@
name|GwtIncompatible
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|class|ForwardingNavigableSet
specifier|public
specifier|abstract
name|class
name|ForwardingNavigableSet
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|ForwardingSortedSet
argument_list|<
name|E
argument_list|>
expr|implements
name|NavigableSet
argument_list|<
name|E
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingNavigableSet ()
specifier|protected
name|ForwardingNavigableSet
argument_list|()
block|{}
expr|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|delegate
argument_list|()
block|;    @
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
name|delegate
argument_list|()
operator|.
name|lower
argument_list|(
name|e
argument_list|)
return|;
block|}
comment|/**    * A sensible definition of {@link #lower} in terms of the {@code descendingIterator} method of    * {@link #headSet(Object, boolean)}. If you override {@link #headSet(Object, boolean)}, you may    * wish to override {@link #lower} to forward to this implementation.    */
expr|@
name|CheckForNull
DECL|method|standardLower (@arametricNullness E e)
specifier|protected
name|E
name|standardLower
argument_list|(
annotation|@
name|ParametricNullness
name|E
name|e
argument_list|)
block|{
return|return
name|Iterators
operator|.
name|getNext
argument_list|(
name|headSet
argument_list|(
name|e
argument_list|,
literal|false
argument_list|)
operator|.
name|descendingIterator
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
end_expr_stmt

begin_function
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|floor (@arametricNullness E e)
specifier|public
name|E
name|floor
parameter_list|(
annotation|@
name|ParametricNullness
name|E
name|e
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|floor
argument_list|(
name|e
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #floor} in terms of the {@code descendingIterator} method of    * {@link #headSet(Object, boolean)}. If you override {@link #headSet(Object, boolean)}, you may    * wish to override {@link #floor} to forward to this implementation.    */
end_comment

begin_function
annotation|@
name|CheckForNull
DECL|method|standardFloor (@arametricNullness E e)
specifier|protected
name|E
name|standardFloor
parameter_list|(
annotation|@
name|ParametricNullness
name|E
name|e
parameter_list|)
block|{
return|return
name|Iterators
operator|.
name|getNext
argument_list|(
name|headSet
argument_list|(
name|e
argument_list|,
literal|true
argument_list|)
operator|.
name|descendingIterator
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
end_function

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
name|delegate
argument_list|()
operator|.
name|ceiling
argument_list|(
name|e
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #ceiling} in terms of the {@code iterator} method of {@link    * #tailSet(Object, boolean)}. If you override {@link #tailSet(Object, boolean)}, you may wish to    * override {@link #ceiling} to forward to this implementation.    */
end_comment

begin_function
annotation|@
name|CheckForNull
DECL|method|standardCeiling (@arametricNullness E e)
specifier|protected
name|E
name|standardCeiling
parameter_list|(
annotation|@
name|ParametricNullness
name|E
name|e
parameter_list|)
block|{
return|return
name|Iterators
operator|.
name|getNext
argument_list|(
name|tailSet
argument_list|(
name|e
argument_list|,
literal|true
argument_list|)
operator|.
name|iterator
argument_list|()
argument_list|,
literal|null
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
name|delegate
argument_list|()
operator|.
name|higher
argument_list|(
name|e
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #higher} in terms of the {@code iterator} method of {@link    * #tailSet(Object, boolean)}. If you override {@link #tailSet(Object, boolean)}, you may wish to    * override {@link #higher} to forward to this implementation.    */
end_comment

begin_function
annotation|@
name|CheckForNull
DECL|method|standardHigher (@arametricNullness E e)
specifier|protected
name|E
name|standardHigher
parameter_list|(
annotation|@
name|ParametricNullness
name|E
name|e
parameter_list|)
block|{
return|return
name|Iterators
operator|.
name|getNext
argument_list|(
name|tailSet
argument_list|(
name|e
argument_list|,
literal|false
argument_list|)
operator|.
name|iterator
argument_list|()
argument_list|,
literal|null
argument_list|)
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
name|delegate
argument_list|()
operator|.
name|pollFirst
argument_list|()
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #pollFirst} in terms of the {@code iterator} method. If you    * override {@link #iterator} you may wish to override {@link #pollFirst} to forward to this    * implementation.    */
end_comment

begin_function
annotation|@
name|CheckForNull
DECL|method|standardPollFirst ()
specifier|protected
name|E
name|standardPollFirst
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|pollNext
argument_list|(
name|iterator
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
name|delegate
argument_list|()
operator|.
name|pollLast
argument_list|()
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #pollLast} in terms of the {@code descendingIterator} method.    * If you override {@link #descendingIterator} you may wish to override {@link #pollLast} to    * forward to this implementation.    */
end_comment

begin_function
annotation|@
name|CheckForNull
DECL|method|standardPollLast ()
specifier|protected
name|E
name|standardPollLast
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|pollNext
argument_list|(
name|descendingIterator
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|ParametricNullness
DECL|method|standardFirst ()
specifier|protected
name|E
name|standardFirst
parameter_list|()
block|{
return|return
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|ParametricNullness
DECL|method|standardLast ()
specifier|protected
name|E
name|standardLast
parameter_list|()
block|{
return|return
name|descendingIterator
argument_list|()
operator|.
name|next
argument_list|()
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
name|delegate
argument_list|()
operator|.
name|descendingSet
argument_list|()
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible implementation of {@link NavigableSet#descendingSet} in terms of the other methods    * of {@link NavigableSet}, notably including {@link NavigableSet#descendingIterator}.    *    *<p>In many cases, you may wish to override {@link ForwardingNavigableSet#descendingSet} to    * forward to this implementation or a subclass thereof.    *    * @since 12.0    */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|StandardDescendingSet
specifier|protected
class|class
name|StandardDescendingSet
extends|extends
name|Sets
operator|.
name|DescendingSet
argument_list|<
name|E
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|StandardDescendingSet ()
specifier|public
name|StandardDescendingSet
parameter_list|()
block|{
name|super
argument_list|(
name|ForwardingNavigableSet
operator|.
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

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
name|delegate
argument_list|()
operator|.
name|descendingIterator
argument_list|()
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
name|delegate
argument_list|()
operator|.
name|subSet
argument_list|(
name|fromElement
argument_list|,
name|fromInclusive
argument_list|,
name|toElement
argument_list|,
name|toInclusive
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #subSet(Object, boolean, Object, boolean)} in terms of the    * {@code headSet} and {@code tailSet} methods. In many cases, you may wish to override {@link    * #subSet(Object, boolean, Object, boolean)} to forward to this implementation.    */
end_comment

begin_function
annotation|@
name|Beta
DECL|method|standardSubSet ( @arametricNullness E fromElement, boolean fromInclusive, @ParametricNullness E toElement, boolean toInclusive)
specifier|protected
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|standardSubSet
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
name|tailSet
argument_list|(
name|fromElement
argument_list|,
name|fromInclusive
argument_list|)
operator|.
name|headSet
argument_list|(
name|toElement
argument_list|,
name|toInclusive
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #subSet(Object, Object)} in terms of the {@link #subSet(Object,    * boolean, Object, boolean)} method. If you override {@link #subSet(Object, boolean, Object,    * boolean)}, you may wish to override {@link #subSet(Object, Object)} to forward to this    * implementation.    */
end_comment

begin_function
annotation|@
name|Override
DECL|method|standardSubSet ( @arametricNullness E fromElement, @ParametricNullness E toElement)
specifier|protected
name|SortedSet
argument_list|<
name|E
argument_list|>
name|standardSubSet
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
name|subSet
argument_list|(
name|fromElement
argument_list|,
literal|true
argument_list|,
name|toElement
argument_list|,
literal|false
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
name|delegate
argument_list|()
operator|.
name|headSet
argument_list|(
name|toElement
argument_list|,
name|inclusive
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #headSet(Object)} in terms of the {@link #headSet(Object,    * boolean)} method. If you override {@link #headSet(Object, boolean)}, you may wish to override    * {@link #headSet(Object)} to forward to this implementation.    */
end_comment

begin_function
DECL|method|standardHeadSet (@arametricNullness E toElement)
specifier|protected
name|SortedSet
argument_list|<
name|E
argument_list|>
name|standardHeadSet
parameter_list|(
annotation|@
name|ParametricNullness
name|E
name|toElement
parameter_list|)
block|{
return|return
name|headSet
argument_list|(
name|toElement
argument_list|,
literal|false
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
name|delegate
argument_list|()
operator|.
name|tailSet
argument_list|(
name|fromElement
argument_list|,
name|inclusive
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #tailSet(Object)} in terms of the {@link #tailSet(Object,    * boolean)} method. If you override {@link #tailSet(Object, boolean)}, you may wish to override    * {@link #tailSet(Object)} to forward to this implementation.    */
end_comment

begin_function
DECL|method|standardTailSet (@arametricNullness E fromElement)
specifier|protected
name|SortedSet
argument_list|<
name|E
argument_list|>
name|standardTailSet
parameter_list|(
annotation|@
name|ParametricNullness
name|E
name|fromElement
parameter_list|)
block|{
return|return
name|tailSet
argument_list|(
name|fromElement
argument_list|,
literal|true
argument_list|)
return|;
block|}
end_function

unit|}
end_unit

