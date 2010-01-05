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
name|Iterator
import|;
end_import

begin_comment
comment|/**  * A collection which forwards all its method calls to another collection.  * Subclasses should override one or more methods to modify the behavior of  * the backing collection as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  * @see ForwardingObject  * @author Kevin Bourrillion  * @since 2010.01.04<b>stable</b> (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ForwardingCollection
specifier|public
specifier|abstract
class|class
name|ForwardingCollection
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingObject
implements|implements
name|Collection
argument_list|<
name|E
argument_list|>
block|{
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
specifier|abstract
name|Collection
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
function_decl|;
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|removeAll (Collection<?> collection)
specifier|public
name|boolean
name|removeAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|removeAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|contains (Object object)
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|contains
argument_list|(
name|object
argument_list|)
return|;
block|}
DECL|method|toArray ()
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|toArray
argument_list|()
return|;
block|}
DECL|method|toArray (T[] array)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|toArray
parameter_list|(
name|T
index|[]
name|array
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
return|;
block|}
DECL|method|add (E element)
specifier|public
name|boolean
name|add
parameter_list|(
name|E
name|element
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|add
argument_list|(
name|element
argument_list|)
return|;
block|}
DECL|method|remove (Object object)
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|remove
argument_list|(
name|object
argument_list|)
return|;
block|}
DECL|method|containsAll (Collection<?> collection)
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|containsAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
DECL|method|addAll (Collection<? extends E> collection)
specifier|public
name|boolean
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|addAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
DECL|method|retainAll (Collection<?> collection)
specifier|public
name|boolean
name|retainAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|retainAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|delegate
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

