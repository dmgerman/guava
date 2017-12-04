begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * GWT emulated version of {@link ImmutableList}. TODO(cpovirk): more doc  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|ForwardingImmutableList
specifier|abstract
class|class
name|ForwardingImmutableList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableList
argument_list|<
name|E
argument_list|>
block|{
DECL|method|ForwardingImmutableList ()
name|ForwardingImmutableList
parameter_list|()
block|{}
DECL|method|delegateList ()
specifier|abstract
name|List
argument_list|<
name|E
argument_list|>
name|delegateList
parameter_list|()
function_decl|;
DECL|method|indexOf (@ullable Object object)
specifier|public
name|int
name|indexOf
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
name|delegateList
argument_list|()
operator|.
name|indexOf
argument_list|(
name|object
argument_list|)
return|;
block|}
DECL|method|lastIndexOf (@ullable Object object)
specifier|public
name|int
name|lastIndexOf
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
name|delegateList
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
name|object
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
name|delegateList
argument_list|()
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
DECL|method|subList (int fromIndex, int toIndex)
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
return|return
name|unsafeDelegateList
argument_list|(
name|delegateList
argument_list|()
operator|.
name|subList
argument_list|(
name|fromIndex
argument_list|,
name|toIndex
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toArray ()
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
comment|// Note that ArrayList.toArray() doesn't work here because it returns E[]
comment|// instead of Object[].
return|return
name|delegateList
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|Object
index|[
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|delegateList
argument_list|()
operator|.
name|equals
argument_list|(
name|obj
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
name|delegateList
argument_list|()
operator|.
name|hashCode
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
name|Iterators
operator|.
name|unmodifiableIterator
argument_list|(
name|delegateList
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
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
name|object
operator|!=
literal|null
operator|&&
name|delegateList
argument_list|()
operator|.
name|contains
argument_list|(
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|containsAll (Collection<?> targets)
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|targets
parameter_list|)
block|{
return|return
name|delegateList
argument_list|()
operator|.
name|containsAll
argument_list|(
name|targets
argument_list|)
return|;
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|delegateList
argument_list|()
operator|.
name|size
argument_list|()
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
name|delegateList
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toArray (T[] other)
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
name|other
parameter_list|)
block|{
return|return
name|delegateList
argument_list|()
operator|.
name|toArray
argument_list|(
name|other
argument_list|)
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
name|delegateList
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

