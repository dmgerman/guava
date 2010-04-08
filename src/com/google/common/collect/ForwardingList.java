begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A list which forwards all its method calls to another list. Subclasses should  * override one or more methods to modify the behavior of the backing list as  * desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p>This class does not implement {@link java.util.RandomAccess}. If the  * delegate supports random access, the {@code ForwardingList} subclass should  * implement the {@code RandomAccess} interface.  *  * @author Mike Bostock  * @since 2 (imported from Google Collections Library)  */
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
block|}
end_class

end_unit

