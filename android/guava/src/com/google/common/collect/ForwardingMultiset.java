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
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
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
comment|/**  * A multiset which forwards all its method calls to another multiset. Subclasses should override  * one or more methods to modify the behavior of the backing multiset as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p><b>Warning:</b> The methods of {@code ForwardingMultiset} forward<b>indiscriminately</b> to  * the methods of the delegate. For example, overriding {@link #add(Object, int)} alone<b>will  * not</b> change the behavior of {@link #add(Object)}, which can lead to unexpected behavior. In  * this case, you should override {@code add(Object)} as well, either providing your own  * implementation, or delegating to the provided {@code standardAdd} method.  *  *<p><b>{@code default} method warning:</b> This class does<i>not</i> forward calls to {@code  * default} methods. Instead, it inherits their default implementations. When those implementations  * invoke methods, they invoke methods on the {@code ForwardingMultiset}.  *  *<p>The {@code standard} methods and any collection views they return are not guaranteed to be  * thread-safe, even when all of the methods that they depend on are thread-safe.  *  * @author Kevin Bourrillion  * @author Louis Wasserman  * @since 2.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ForwardingMultiset
specifier|public
specifier|abstract
class|class
name|ForwardingMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingCollection
argument_list|<
name|E
argument_list|>
implements|implements
name|Multiset
argument_list|<
name|E
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingMultiset ()
specifier|protected
name|ForwardingMultiset
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|Multiset
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|count (Object element)
specifier|public
name|int
name|count
parameter_list|(
name|Object
name|element
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|count
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|add (E element, int occurrences)
specifier|public
name|int
name|add
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|add
argument_list|(
name|element
argument_list|,
name|occurrences
argument_list|)
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|remove (Object element, int occurrences)
specifier|public
name|int
name|remove
parameter_list|(
name|Object
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|remove
argument_list|(
name|element
argument_list|,
name|occurrences
argument_list|)
return|;
block|}
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
return|return
name|delegate
argument_list|()
operator|.
name|elementSet
argument_list|()
return|;
block|}
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
return|return
name|delegate
argument_list|()
operator|.
name|entrySet
argument_list|()
return|;
block|}
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
name|object
operator|==
name|this
operator|||
name|delegate
argument_list|()
operator|.
name|equals
argument_list|(
name|object
argument_list|)
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
name|delegate
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|setCount (E element, int count)
specifier|public
name|int
name|setCount
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|setCount
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|setCount (E element, int oldCount, int newCount)
specifier|public
name|boolean
name|setCount
parameter_list|(
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
name|delegate
argument_list|()
operator|.
name|setCount
argument_list|(
name|element
argument_list|,
name|oldCount
argument_list|,
name|newCount
argument_list|)
return|;
block|}
comment|/**    * A sensible definition of {@link #contains} in terms of {@link #count}. If you override {@link    * #count}, you may wish to override {@link #contains} to forward to this implementation.    *    * @since 7.0    */
annotation|@
name|Override
DECL|method|standardContains (@ullable Object object)
specifier|protected
name|boolean
name|standardContains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
name|count
argument_list|(
name|object
argument_list|)
operator|>
literal|0
return|;
block|}
comment|/**    * A sensible definition of {@link #clear} in terms of the {@code iterator} method of {@link    * #entrySet}. If you override {@link #entrySet}, you may wish to override {@link #clear} to    * forward to this implementation.    *    * @since 7.0    */
annotation|@
name|Override
DECL|method|standardClear ()
specifier|protected
name|void
name|standardClear
parameter_list|()
block|{
name|Iterators
operator|.
name|clear
argument_list|(
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * A sensible, albeit inefficient, definition of {@link #count} in terms of {@link #entrySet}. If    * you override {@link #entrySet}, you may wish to override {@link #count} to forward to this    * implementation.    *    * @since 7.0    */
annotation|@
name|Beta
DECL|method|standardCount (@ullable Object object)
specifier|protected
name|int
name|standardCount
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|?
argument_list|>
name|entry
range|:
name|this
operator|.
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
name|object
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
comment|/**    * A sensible definition of {@link #add(Object)} in terms of {@link #add(Object, int)}. If you    * override {@link #add(Object, int)}, you may wish to override {@link #add(Object)} to forward to    * this implementation.    *    * @since 7.0    */
DECL|method|standardAdd (E element)
specifier|protected
name|boolean
name|standardAdd
parameter_list|(
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
comment|/**    * A sensible definition of {@link #addAll(Collection)} in terms of {@link #add(Object)} and    * {@link #add(Object, int)}. If you override either of these methods, you may wish to override    * {@link #addAll(Collection)} to forward to this implementation.    *    * @since 7.0    */
annotation|@
name|Beta
annotation|@
name|Override
DECL|method|standardAddAll (Collection<? extends E> elementsToAdd)
specifier|protected
name|boolean
name|standardAddAll
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
comment|/**    * A sensible definition of {@link #remove(Object)} in terms of {@link #remove(Object, int)}. If    * you override {@link #remove(Object, int)}, you may wish to override {@link #remove(Object)} to    * forward to this implementation.    *    * @since 7.0    */
annotation|@
name|Override
DECL|method|standardRemove (Object element)
specifier|protected
name|boolean
name|standardRemove
parameter_list|(
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
comment|/**    * A sensible definition of {@link #removeAll} in terms of the {@code removeAll} method of {@link    * #elementSet}. If you override {@link #elementSet}, you may wish to override {@link #removeAll}    * to forward to this implementation.    *    * @since 7.0    */
annotation|@
name|Override
DECL|method|standardRemoveAll (Collection<?> elementsToRemove)
specifier|protected
name|boolean
name|standardRemoveAll
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
comment|/**    * A sensible definition of {@link #retainAll} in terms of the {@code retainAll} method of {@link    * #elementSet}. If you override {@link #elementSet}, you may wish to override {@link #retainAll}    * to forward to this implementation.    *    * @since 7.0    */
annotation|@
name|Override
DECL|method|standardRetainAll (Collection<?> elementsToRetain)
specifier|protected
name|boolean
name|standardRetainAll
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
comment|/**    * A sensible definition of {@link #setCount(Object, int)} in terms of {@link #count(Object)},    * {@link #add(Object, int)}, and {@link #remove(Object, int)}. {@link #entrySet()}. If you    * override any of these methods, you may wish to override {@link #setCount(Object, int)} to    * forward to this implementation.    *    * @since 7.0    */
DECL|method|standardSetCount (E element, int count)
specifier|protected
name|int
name|standardSetCount
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
return|return
name|Multisets
operator|.
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
comment|/**    * A sensible definition of {@link #setCount(Object, int, int)} in terms of {@link #count(Object)}    * and {@link #setCount(Object, int)}. If you override either of these methods, you may wish to    * override {@link #setCount(Object, int, int)} to forward to this implementation.    *    * @since 7.0    */
DECL|method|standardSetCount (E element, int oldCount, int newCount)
specifier|protected
name|boolean
name|standardSetCount
parameter_list|(
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
name|Multisets
operator|.
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
comment|/**    * A sensible implementation of {@link Multiset#elementSet} in terms of the following methods:    * {@link ForwardingMultiset#clear}, {@link ForwardingMultiset#contains}, {@link    * ForwardingMultiset#containsAll}, {@link ForwardingMultiset#count}, {@link    * ForwardingMultiset#isEmpty}, the {@link Set#size} and {@link Set#iterator} methods of {@link    * ForwardingMultiset#entrySet}, and {@link ForwardingMultiset#remove(Object, int)}. In many    * situations, you may wish to override {@link ForwardingMultiset#elementSet} to forward to this    * implementation or a subclass thereof.    *    * @since 10.0    */
annotation|@
name|Beta
DECL|class|StandardElementSet
specifier|protected
class|class
name|StandardElementSet
extends|extends
name|Multisets
operator|.
name|ElementSet
argument_list|<
name|E
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|StandardElementSet ()
specifier|public
name|StandardElementSet
parameter_list|()
block|{}
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
name|ForwardingMultiset
operator|.
name|this
return|;
block|}
block|}
comment|/**    * A sensible definition of {@link #iterator} in terms of {@link #entrySet} and {@link    * #remove(Object)}. If you override either of these methods, you may wish to override {@link    * #iterator} to forward to this implementation.    *    * @since 7.0    */
DECL|method|standardIterator ()
specifier|protected
name|Iterator
argument_list|<
name|E
argument_list|>
name|standardIterator
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
comment|/**    * A sensible, albeit inefficient, definition of {@link #size} in terms of {@link #entrySet}. If    * you override {@link #entrySet}, you may wish to override {@link #size} to forward to this    * implementation.    *    * @since 7.0    */
DECL|method|standardSize ()
specifier|protected
name|int
name|standardSize
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
comment|/**    * A sensible, albeit inefficient, definition of {@link #equals} in terms of {@code    * entrySet().size()} and {@link #count}. If you override either of these methods, you may wish to    * override {@link #equals} to forward to this implementation.    *    * @since 7.0    */
DECL|method|standardEquals (@ullable Object object)
specifier|protected
name|boolean
name|standardEquals
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
comment|/**    * A sensible definition of {@link #hashCode} as {@code entrySet().hashCode()} . If you override    * {@link #entrySet}, you may wish to override {@link #hashCode} to forward to this    * implementation.    *    * @since 7.0    */
DECL|method|standardHashCode ()
specifier|protected
name|int
name|standardHashCode
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
comment|/**    * A sensible definition of {@link #toString} as {@code entrySet().toString()} . If you override    * {@link #entrySet}, you may wish to override {@link #toString} to forward to this    * implementation.    *    * @since 7.0    */
annotation|@
name|Override
DECL|method|standardToString ()
specifier|protected
name|String
name|standardToString
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

