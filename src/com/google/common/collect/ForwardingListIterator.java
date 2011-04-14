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
name|GwtCompatible
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

begin_comment
comment|/**  * A list iterator which forwards all its method calls to another list  * iterator. Subclasses should override one or more methods to modify the  * behavior of the backing iterator as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  * @author Mike Bostock  * @since Guava release 02 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ForwardingListIterator
specifier|public
specifier|abstract
class|class
name|ForwardingListIterator
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingIterator
argument_list|<
name|E
argument_list|>
implements|implements
name|ListIterator
argument_list|<
name|E
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingListIterator ()
specifier|protected
name|ForwardingListIterator
parameter_list|()
block|{}
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
specifier|abstract
name|ListIterator
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|add (E element)
specifier|public
name|void
name|add
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|delegate
argument_list|()
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hasPrevious ()
specifier|public
name|boolean
name|hasPrevious
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|hasPrevious
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|nextIndex ()
specifier|public
name|int
name|nextIndex
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|nextIndex
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|previous ()
specifier|public
name|E
name|previous
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|previous
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|previousIndex ()
specifier|public
name|int
name|previousIndex
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|previousIndex
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|set (E element)
specifier|public
name|void
name|set
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|delegate
argument_list|()
operator|.
name|set
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

