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
name|Iterator
import|;
end_import

begin_comment
comment|/**  * An iterator which forwards all its method calls to another iterator.  * Subclasses should override one or more methods to modify the behavior of the  * backing iterator as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  * @author Kevin Bourrillion  * @since 2 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ForwardingIterator
specifier|public
specifier|abstract
class|class
name|ForwardingIterator
parameter_list|<
name|T
parameter_list|>
extends|extends
name|ForwardingObject
implements|implements
name|Iterator
argument_list|<
name|T
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingIterator ()
specifier|protected
name|ForwardingIterator
parameter_list|()
block|{}
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
specifier|abstract
name|Iterator
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|hasNext
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|T
name|next
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|delegate
argument_list|()
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

