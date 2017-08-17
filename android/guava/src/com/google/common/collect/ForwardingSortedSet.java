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
comment|/**  * A sorted set which forwards all its method calls to another sorted set.  * Subclasses should override one or more methods to modify the behavior of the  * backing sorted set as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p><b>Warning:</b> The methods of {@code ForwardingSortedSet} forward  *<i>indiscriminately</i> to the methods of the delegate. For example,  * overriding {@link #add} alone<i>will not</i> change the behavior of {@link  * #addAll}, which can lead to unexpected behavior. In this case, you should  * override {@code addAll} as well, either providing your own implementation, or  * delegating to the provided {@code standardAddAll} method.  *  *<p><b>{@code default} method warning:</b> This class does<i>not</i> forward calls to {@code  * default} methods. Instead, it inherits their default implementations. When those implementations  * invoke methods, they invoke methods on the {@code ForwardingSortedSet}.  *  *<p>Each of the {@code standard} methods, where appropriate, uses the set's  * comparator (or the natural ordering of the elements, if there is no  * comparator) to test element equality. As a result, if the comparator is not  * consistent with equals, some of the standard implementations may violate the  * {@code Set} contract.  *  *<p>The {@code standard} methods and the collection views they return are not  * guaranteed to be thread-safe, even when all of the methods that they depend  * on are thread-safe.  *  * @author Mike Bostock  * @author Louis Wasserman  * @since 2.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ForwardingSortedSet
specifier|public
specifier|abstract
class|class
name|ForwardingSortedSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingSet
argument_list|<
name|E
argument_list|>
implements|implements
name|SortedSet
argument_list|<
name|E
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingSortedSet ()
specifier|protected
name|ForwardingSortedSet
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|SortedSet
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
function_decl|;
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
name|delegate
argument_list|()
operator|.
name|comparator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|first ()
specifier|public
name|E
name|first
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|first
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|headSet (E toElement)
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
name|delegate
argument_list|()
operator|.
name|headSet
argument_list|(
name|toElement
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|last ()
specifier|public
name|E
name|last
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|last
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|subSet (E fromElement, E toElement)
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
name|delegate
argument_list|()
operator|.
name|subSet
argument_list|(
name|fromElement
argument_list|,
name|toElement
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|tailSet (E fromElement)
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
name|delegate
argument_list|()
operator|.
name|tailSet
argument_list|(
name|fromElement
argument_list|)
return|;
block|}
comment|// unsafe, but worst case is a CCE is thrown, which callers will be expecting
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|unsafeCompare (Object o1, Object o2)
specifier|private
name|int
name|unsafeCompare
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
init|=
name|comparator
argument_list|()
decl_stmt|;
return|return
operator|(
name|comparator
operator|==
literal|null
operator|)
condition|?
operator|(
operator|(
name|Comparable
argument_list|<
name|Object
argument_list|>
operator|)
name|o1
operator|)
operator|.
name|compareTo
argument_list|(
name|o2
argument_list|)
else|:
operator|(
operator|(
name|Comparator
argument_list|<
name|Object
argument_list|>
operator|)
name|comparator
operator|)
operator|.
name|compare
argument_list|(
name|o1
argument_list|,
name|o2
argument_list|)
return|;
block|}
comment|/**    * A sensible definition of {@link #contains} in terms of the {@code first()}    * method of {@link #tailSet}. If you override {@link #tailSet}, you may wish    * to override {@link #contains} to forward to this implementation.    *    * @since 7.0    */
annotation|@
name|Override
annotation|@
name|Beta
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
try|try
block|{
comment|// any ClassCastExceptions are caught
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|SortedSet
argument_list|<
name|Object
argument_list|>
name|self
init|=
operator|(
name|SortedSet
argument_list|<
name|Object
argument_list|>
operator|)
name|this
decl_stmt|;
name|Object
name|ceiling
init|=
name|self
operator|.
name|tailSet
argument_list|(
name|object
argument_list|)
operator|.
name|first
argument_list|()
decl_stmt|;
return|return
name|unsafeCompare
argument_list|(
name|ceiling
argument_list|,
name|object
argument_list|)
operator|==
literal|0
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
decl||
name|NoSuchElementException
decl||
name|NullPointerException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
comment|/**    * A sensible definition of {@link #remove} in terms of the {@code iterator()}    * method of {@link #tailSet}. If you override {@link #tailSet}, you may wish    * to override {@link #remove} to forward to this implementation.    *    * @since 7.0    */
annotation|@
name|Override
annotation|@
name|Beta
DECL|method|standardRemove (@ullable Object object)
specifier|protected
name|boolean
name|standardRemove
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
try|try
block|{
comment|// any ClassCastExceptions are caught
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|SortedSet
argument_list|<
name|Object
argument_list|>
name|self
init|=
operator|(
name|SortedSet
argument_list|<
name|Object
argument_list|>
operator|)
name|this
decl_stmt|;
name|Iterator
argument_list|<
name|Object
argument_list|>
name|iterator
init|=
name|self
operator|.
name|tailSet
argument_list|(
name|object
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|ceiling
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|unsafeCompare
argument_list|(
name|ceiling
argument_list|,
name|object
argument_list|)
operator|==
literal|0
condition|)
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|ClassCastException
decl||
name|NullPointerException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**    * A sensible default implementation of {@link #subSet(Object, Object)} in    * terms of {@link #headSet(Object)} and {@link #tailSet(Object)}. In some    * situations, you may wish to override {@link #subSet(Object, Object)} to    * forward to this implementation.    *    * @since 7.0    */
annotation|@
name|Beta
DECL|method|standardSubSet (E fromElement, E toElement)
specifier|protected
name|SortedSet
argument_list|<
name|E
argument_list|>
name|standardSubSet
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|E
name|toElement
parameter_list|)
block|{
return|return
name|tailSet
argument_list|(
name|fromElement
argument_list|)
operator|.
name|headSet
argument_list|(
name|toElement
argument_list|)
return|;
block|}
block|}
end_class

end_unit

