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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ListIterator
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
comment|/**  * A list which forwards all its method calls to another list. Subclasses should  * override one or more methods to modify the behavior of the backing list as  * desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p>This class does not implement {@link java.util.RandomAccess}. If the  * delegate supports random access, the {@code ForwardingList} subclass should  * implement the {@code RandomAccess} interface.  *  *<p><b>Warning:</b> The methods of {@code ForwardingList} forward  *<b>indiscriminately</b> to the methods of the delegate. For example,  * overriding {@link #add} alone<b>will not</b> change the behavior of {@link  * #addAll}, which can lead to unexpected behavior. In this case, you should  * override {@code addAll} as well, either providing your own implementation, or  * delegating to the provided {@code standardAddAll} method.  *  *<p>The {@code standard} methods and any collection views they return are not  * guaranteed to be thread-safe, even when all of the methods that they depend  * on are thread-safe.  *  * @author Mike Bostock  * @author Louis Wasserman  * @since 2.0 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ForwardingList
specifier|public
specifier|abstract
class|class
name|ForwardingList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingCollection
argument_list|<
name|E
argument_list|>
implements|implements
name|List
argument_list|<
name|E
argument_list|>
block|{
comment|// TODO(user): identify places where thread safety is actually lost
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingList ()
specifier|protected
name|ForwardingList
parameter_list|()
block|{}
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
specifier|abstract
name|List
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|add (int index, E element)
specifier|public
name|void
name|add
parameter_list|(
name|int
name|index
parameter_list|,
name|E
name|element
parameter_list|)
block|{
name|delegate
argument_list|()
operator|.
name|add
argument_list|(
name|index
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addAll (int index, Collection<? extends E> elements)
specifier|public
name|boolean
name|addAll
parameter_list|(
name|int
name|index
parameter_list|,
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|addAll
argument_list|(
name|index
argument_list|,
name|elements
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|get (int index)
specifier|public
name|E
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|indexOf (Object element)
specifier|public
name|int
name|indexOf
parameter_list|(
name|Object
name|element
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|indexOf
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|lastIndexOf (Object element)
specifier|public
name|int
name|lastIndexOf
parameter_list|(
name|Object
name|element
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|listIterator ()
specifier|public
name|ListIterator
argument_list|<
name|E
argument_list|>
name|listIterator
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|listIterator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|listIterator (int index)
specifier|public
name|ListIterator
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
name|delegate
argument_list|()
operator|.
name|listIterator
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|remove (int index)
specifier|public
name|E
name|remove
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|remove
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|set (int index, E element)
specifier|public
name|E
name|set
parameter_list|(
name|int
name|index
parameter_list|,
name|E
name|element
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|set
argument_list|(
name|index
argument_list|,
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|subList (int fromIndex, int toIndex)
specifier|public
name|List
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
return|return
name|delegate
argument_list|()
operator|.
name|subList
argument_list|(
name|fromIndex
argument_list|,
name|toIndex
argument_list|)
return|;
block|}
DECL|method|equals (@ullable Object object)
annotation|@
name|Override
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
DECL|method|hashCode ()
annotation|@
name|Override
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
comment|/**    * A sensible default implementation of {@link #add(Object)}, in terms of    * {@link #add(int, Object)}. If you override {@link #add(int, Object)}, you    * may wish to override {@link #add(Object)} to forward to this    * implementation.    *    * @since 7.0    */
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
name|size
argument_list|()
argument_list|,
name|element
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/**    * A sensible default implementation of {@link #addAll(int, Collection)}, in    * terms of the {@code add} method of {@link #listIterator(int)}. If you    * override {@link #listIterator(int)}, you may wish to override {@link    * #addAll(int, Collection)} to forward to this implementation.    *    * @since 7.0    */
DECL|method|standardAddAll ( int index, Iterable<? extends E> elements)
specifier|protected
name|boolean
name|standardAddAll
parameter_list|(
name|int
name|index
parameter_list|,
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
parameter_list|)
block|{
return|return
name|Lists
operator|.
name|addAllImpl
argument_list|(
name|this
argument_list|,
name|index
argument_list|,
name|elements
argument_list|)
return|;
block|}
comment|/**    * A sensible default implementation of {@link #indexOf}, in terms of {@link    * #listIterator()}. If you override {@link #listIterator()}, you may wish to    * override {@link #indexOf} to forward to this implementation.    *    * @since 7.0    */
DECL|method|standardIndexOf (@ullable Object element)
specifier|protected
name|int
name|standardIndexOf
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|)
block|{
return|return
name|Lists
operator|.
name|indexOfImpl
argument_list|(
name|this
argument_list|,
name|element
argument_list|)
return|;
block|}
comment|/**    * A sensible default implementation of {@link #lastIndexOf}, in terms of    * {@link #listIterator(int)}. If you override {@link #listIterator(int)}, you    * may wish to override {@link #lastIndexOf} to forward to this    * implementation.    *    * @since 7.0    */
DECL|method|standardLastIndexOf (@ullable Object element)
specifier|protected
name|int
name|standardLastIndexOf
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|)
block|{
return|return
name|Lists
operator|.
name|lastIndexOfImpl
argument_list|(
name|this
argument_list|,
name|element
argument_list|)
return|;
block|}
comment|/**    * A sensible default implementation of {@link #iterator}, in terms of    * {@link #listIterator()}. If you override {@link #listIterator()}, you may    * wish to override {@link #iterator} to forward to this implementation.    *    * @since 7.0    */
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
name|listIterator
argument_list|()
return|;
block|}
comment|/**    * A sensible default implementation of {@link #listIterator()}, in terms of    * {@link #listIterator(int)}. If you override {@link #listIterator(int)}, you    * may wish to override {@link #listIterator()} to forward to this    * implementation.    *    * @since 7.0    */
DECL|method|standardListIterator ()
specifier|protected
name|ListIterator
argument_list|<
name|E
argument_list|>
name|standardListIterator
parameter_list|()
block|{
return|return
name|listIterator
argument_list|(
literal|0
argument_list|)
return|;
block|}
comment|/**    * A sensible default implementation of {@link #listIterator(int)}, in terms    * of {@link #size}, {@link #get(int)}, {@link #set(int, Object)}, {@link    * #add(int, Object)}, and {@link #remove(int)}. If you override any of these    * methods, you may wish to override {@link #listIterator(int)} to forward to    * this implementation.    *    * @since 7.0    */
DECL|method|standardListIterator (int start)
annotation|@
name|Beta
specifier|protected
name|ListIterator
argument_list|<
name|E
argument_list|>
name|standardListIterator
parameter_list|(
name|int
name|start
parameter_list|)
block|{
return|return
name|Lists
operator|.
name|listIteratorImpl
argument_list|(
name|this
argument_list|,
name|start
argument_list|)
return|;
block|}
comment|/**    * A sensible default implementation of {@link #subList(int, int)}. If you    * override any other methods, you may wish to override {@link #subList(int,    * int)} to forward to this implementation.    *    * @since 7.0    */
DECL|method|standardSubList (int fromIndex, int toIndex)
annotation|@
name|Beta
specifier|protected
name|List
argument_list|<
name|E
argument_list|>
name|standardSubList
parameter_list|(
name|int
name|fromIndex
parameter_list|,
name|int
name|toIndex
parameter_list|)
block|{
return|return
name|Lists
operator|.
name|subListImpl
argument_list|(
name|this
argument_list|,
name|fromIndex
argument_list|,
name|toIndex
argument_list|)
return|;
block|}
comment|/**    * A sensible definition of {@link #equals(Object)} in terms of {@link #size}    * and {@link #iterator}. If you override either of those methods, you may    * wish to override {@link #equals(Object)} to forward to this implementation.    *    * @since 7.0    */
DECL|method|standardEquals (@ullable Object object)
annotation|@
name|Beta
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
name|Lists
operator|.
name|equalsImpl
argument_list|(
name|this
argument_list|,
name|object
argument_list|)
return|;
block|}
comment|/**    * A sensible definition of {@link #hashCode} in terms of {@link #iterator}.    * If you override {@link #iterator}, you may wish to override {@link    * #hashCode} to forward to this implementation.    *    * @since 7.0    */
DECL|method|standardHashCode ()
annotation|@
name|Beta
specifier|protected
name|int
name|standardHashCode
parameter_list|()
block|{
return|return
name|Lists
operator|.
name|hashCodeImpl
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

